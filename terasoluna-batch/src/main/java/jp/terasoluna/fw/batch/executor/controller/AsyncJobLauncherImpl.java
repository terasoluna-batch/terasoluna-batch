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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 非同期バッチ実行機能で多重度の最大値を{@code ThreadPoolTaskExecutor}の最大スレッドプールサイズとした非同期のジョブ起動を行う。<br>
 * 最大プールサイズ以上のジョブの実行が行われた場合、スレッドプールに空きができるまで待ち状態になる。
 * 本機能を利用するにはジョブ実行テンプレート{@code JobExecutorTemplate}のBean定義が必要となる。
 * 以下は{@code AsyncJobLauncher}と、非同期ジョブの多重度を決定する{@code ThreadPoolTaskExecutor}のBean定義の設定例である。
 * <code><pre>
 * &lt;bean id=&quot;asyncJobLauncher&quot; class=&quot;jp.terasoluna.fw.batch.executor.controller.AsyncJobLauncherImpl&quot;&gt;
 *     &lt;constructor-arg index=&quot;0&quot; ref=&quot;threadPoolTaskExecutor&quot;/&gt;
 *     &lt;constructor-arg index=&quot;1&quot; ref=&quot;jobExecutorTemplate&quot;/&gt;
 *     &lt;constructor-arg index=&quot;2&quot; ref=&quot;exceptionStatusHandler&quot;/&gt;
 * &lt;/bean&gt;
 * &lt;bean id=&quot;threadPoolTaskExecutor&quot; class=&quot;org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor&quot;&gt;
 *     &lt;property name=&quot;threadFactory&quot; ref=&quot;separateGroupThreadFactory&quot;/&gt;
 *     &lt;property name=&quot;corePoolSize&quot; value=&quot;4&quot;/&gt;   ←コアプールサイズ
 *     &lt;property name=&quot;maxPoolSize&quot; value=&quot;4&quot;/&gt;    ←最大プールサイズ
 *     &lt;property name=&quot;queueCapacity&quot; value=&quot;4&quot;/&gt;  ←キューサイズの上限
 * &lt;/bean&gt;
 * </pre></code> {@code ThreadPoolTaskExecutor}のBean定義を利用した多重度の設定では以下の制約に留意すること。
 * <ol>
 * <li>コアプールサイズ(corePoolSize)と最大プールサイズ(maxPoolSize)はそれぞれ定義可能だが 本機能では同数を設定すること</li>
 * <li>キューサイズの上限値は最大プールサイズ未満の値を設定しないこと （無設定時は{@code Integer.MAX_VALUE}が上限となる）</li>
 * </ol>
 * 最大プールサイズ及びキューサイズの詳細は{@code ThreadPoolExecutor}の APIドキュメントを参照のこと。
 * 最大プールサイズ以上のジョブ実行が行われた場合はスレッドプールに空きが生じるまで待ち状態となるが、
 * この待ち状態が公平性（先入れ-先出し） を保ったまま解決されるかを{@code fair}プロパティで設定することができる。
 * （デフォルトは公平性あり：{@code true}であり、DIコンテナの起動後の変更は無効。）
 *
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
     * @param threadPoolTaskExecutor {@code ThreadPoolTaskExecutor}のデリゲータ
     * @param jobExecutorTemplate ジョブの前処理と主処理を定義するテンプレート
     * @param exceptionStatusHandler フレームワーク内部例外を処理するハンドラ
     */
    public AsyncJobLauncherImpl(ThreadPoolTaskExecutor threadPoolTaskExecutor,
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
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!jobExecutorTemplate.beforeExecute(jobSequenceId)) {
                            LOGGER.warn(LogId.IAL025022, jobSequenceId);
                            return;
                        }
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
        ThreadPoolExecutor threadPoolExecutor =
                threadPoolTaskExecutor.getThreadPoolExecutor();
        threadPoolExecutor.shutdown();
        while (!terminated(threadPoolExecutor)) {
            LOGGER.info(LogId.IAL025021);
        }
    }

    /**
     * スレッドプールのシャットダウン完了を指定時間以内に完了したらtrueを返却する。
     * 完了待ち状態で割り込みが発生した場合、falseを返却する。
     *
     * @param threadPoolExecutor スレッドプール
     * @return シャットダウンが指定時間以内に完了したらtrue
     */
    protected boolean terminated(ThreadPoolExecutor threadPoolExecutor) {
        try {
            return threadPoolExecutor.awaitTermination(
                    executorJobTerminateWaitIntervalTime,
                    TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // シャットダウン完了待ち受け中の割り込みは何もしない
        }
        return false;
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
