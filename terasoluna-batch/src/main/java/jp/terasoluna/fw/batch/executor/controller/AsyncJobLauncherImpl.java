package jp.terasoluna.fw.batch.executor.controller;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.exception.handler.ExceptionStatusHandler;
import jp.terasoluna.fw.batch.executor.worker.JobExecutorTemplate;
import jp.terasoluna.fw.logger.TLogger;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 非同期バッチ実行機能で多重度の最大値を{@code ThreadPoolTaskExecutor}の最大スレッドプールサイズとした非同期のジョブ起動を行う。<br>
 * 最大プールサイズ以上のジョブの実行が行われた場合、スレッドプールに空きができるまで待ち状態になる。 本機能を利用するにはジョブ実行テンプレート
 * {@code JobExecutorTemplate}のBean定義が必要となる。 以下は{@code AsyncJobLauncher}のBean定義の設定例である。 <code><pre>
 *    &lt;task:executor id=&quot;threadPoolTaskExecutor&quot;
 *            pool-size=&quot;4&quot;          ←最大プールサイズ
 *            queue-capacity=&quot;4&quot; /&gt;  ←キューサイズの上限
 *    &lt;bean id=&quot;asyncJobLauncher&quot; class=&quot;jp.terasoluna.fw.batch.executor.controller.AsyncJobLauncherImpl&quot;&gt;
 *            &lt;constructor-arg index=&quot;0&quot; ref=&quot;threadPoolTaskExecutor&quot;/&gt;
 *            &lt;constructor-arg index=&quot;1&quot; ref=&quot;jobExecutorTemplate&quot;/&gt;
 *    &lt;/bean&gt;
 * </pre></code> Bean定義を利用した多重度の設定では以下の制約に留意すること。
 * <ol>
 * <li>Bean定義のスレッドプール設定は{@code pool-size}によるハイフン区切り （&quot;1-3&quot;）で、コアプールサイズ-最大プールサイズがそれぞれ定義可能だが 本機能では同数を設定すること</li>
 * <li>キューサイズの上限値は最大プールサイズ未満の値を設定しないこと （無設定時は{@code Integer.MAX_VALUE}が上限となる）</li>
 * </ol>
 * 最大プールサイズ及びキューサイズの詳細は{@code ThreadPoolExecutor}の APIドキュメントを参照のこと。 最大プールサイズ以上のジョブ実行が行われた場合はスレッドプールに
 * 空きが生じるまで待ち状態となるが、この待ち状態が公平性（先入れ-先出し） を保ったまま解決されるかを{@code setFair()}メソッドで設定することができる。 （公平性あり：{@code true}
 * がデフォルトであり、DIコンテナの起動後の変更は無効。）
 * @see org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
 * @see java.util.concurrent.ThreadPoolExecutor
 * @since 3.6
 */
public class AsyncJobLauncherImpl implements AsyncJobLauncher,
                                  InitializingBean {

    /**
     * ロガー。
     */
    private static final TLogger LOGGER = TLogger.getLogger(
            AsyncJobLauncherImpl.class);

    /**
     * {@code ThreadPoolTaskExecutor}のデリゲータ。
     */
    protected ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * ジョブ実行のテンプレート機能。
     */
    protected JobExecutorTemplate jobExecutorTemplate;

    /**
     * フレームワーク機能内部で発生した例外によるハンドリング機能。
     */
    protected ExceptionStatusHandler exceptionStatusHandler;

    /**
     * 非同期バッチ待ち状態解決の公平性。デフォルト：{@code true}
     */
    protected boolean fair = true;

    /**
     * スレッドプールの上限以上のタスク流入を防ぐためのセマフォ。
     */
    protected Semaphore taskPoolLimit = null;

    /**
     * 残留ジョブがある場合、シャットダウンを保留する再チェックまでのスリープ時間。
     */
    @Value("${executor.jobTerminateWaitInterval:-1}")
    protected volatile long executorJobTerminateWaitIntervalTime;

    /**
     * コンストラクタ。<br>
     * @param threadPoolTaskExecutorDelegate {@code ThreadPoolTaskExecutor}のデリゲータ
     * @param threadPoolTaskExecutor {@code ThreadPoolTaskExecutor}のデリゲータ
     * @param jobExecutorTemplate ジョブの前処理と主処理を定義するテンプレート
     */
    public AsyncJobLauncherImpl(
            ThreadPoolTaskExecutor threadPoolTaskExecutor,
            JobExecutorTemplate jobExecutorTemplate,
            ExceptionStatusHandler exceptionStatusHandler) {
        Assert.notNull(threadPoolTaskExecutor, LOGGER.getLogMessage(
                LogId.EAL025055));
        Assert.notNull(jobExecutorTemplate, LOGGER.getLogMessage(
                LogId.EAL025057));
        Assert.notNull(exceptionStatusHandler, LOGGER.getLogMessage(
                LogId.EAL025091));
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.jobExecutorTemplate = jobExecutorTemplate;
        this.exceptionStatusHandler = exceptionStatusHandler;
    }

    /**
     * スレッドプール上限に達し、待ち状態となったジョブの公平性（先入れ-先出し）を設定する。<br>
     * @param fair {@code true}の場合は公平性あり
     */
    public void setFair(boolean fair) {
        this.fair = fair;
    }

    /**
     * スレッドプールから実行タスクを割り当て、ジョブを実行する。<br>
     * 最大プールサイズの上限に達している場合は待ち受けが行われる。
     * @param jobSequenceId ジョブのシーケンスコード
     */
    @Override
    public void executeJob(final String jobSequenceId) {
        Assert.notNull(jobSequenceId);
        try {
            taskPoolLimit.acquire();
            boolean beforeExecuteStatus = false;
            try {
                beforeExecuteStatus = jobExecutorTemplate.beforeExecute(
                        jobSequenceId);
            } catch (RuntimeException e) {
                taskPoolLimit.release();
                throw e;
            }
            if (!beforeExecuteStatus) {
                taskPoolLimit.release();
                LOGGER.warn(LogId.WAL025009, jobSequenceId);
                return;
            }
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        jobExecutorTemplate.executeWorker(jobSequenceId);
                    } catch (RuntimeException e) {
                        exceptionStatusHandler.handleException(e);
                    } finally {
                        taskPoolLimit.release();
                    }
                }
            });
        } catch (TaskRejectedException e) {
            LOGGER.error(LogId.EAL025047, e, jobSequenceId);
            taskPoolLimit.release();
        } catch (InterruptedException e) {
            LOGGER.error(LogId.EAL025054, e, jobSequenceId);
        }
    }

    /**
     * スレッドプールをシャットダウンする。<br>
     * プール内の全てのタスクが終了するまで本メソッドは終了しない。
     */
    @Override
    public void shutdown() {
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.shutdown();
        while (true) {
            int executeCount = threadPoolTaskExecutor.getActiveCount();
            if (executeCount == 0) {
                break;
            }
            try {
                LOGGER.debug(LogId.DAL025031, executeCount);
                TimeUnit.MILLISECONDS.sleep(
                        executorJobTerminateWaitIntervalTime);
            } catch (InterruptedException e) {
                // Do nothing.
            }
        }
    }

    /**
     * SpringによるDIコンテナ生成時、プロパティ設定後にコールバックされる初期化処理。<br>
     * @throws Exception 予期しない例外
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        Assert.state(executorJobTerminateWaitIntervalTime > 0, LOGGER
                .getLogMessage(LogId.EAL025058));

        int maxPoolSize = threadPoolTaskExecutor.getMaxPoolSize();
        LOGGER.debug(LogId.DAL025061, maxPoolSize, fair);
        taskPoolLimit = new Semaphore(maxPoolSize, fair);
    }
}
