/*
 * Copyright (c) 2016 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.terasoluna.fw.batch.executor.controller;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.worker.AsyncJobWorker;
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
 * 非同期型ジョブを実行するためにフレームワークが提供するデフォルトの実装クラス。ThreadPoolTaskExecutorを使用して非同期型ジョブを実行する。<br>
 * 最大プールサイズ以上のジョブの実行が行われた場合、スレッドプールに空きができるまで待ち状態になる。
 * 本機能を利用するにはワーカスレッド処理{@code AsyncJobWorker}のBean定義が必要となる。
 * 以下は{@code AsyncJobLauncher}と、非同期ジョブの多重度を決定する{@code ThreadPoolTaskExecutor}のBean定義の設定例である。
 * <code><pre>
 * &lt;bean id=&quot;asyncJobLauncher&quot; class=&quot;jp.terasoluna.fw.batch.executor.controller.AsyncJobLauncherImpl&quot;&gt;
 *     &lt;constructor-arg index=&quot;0&quot; ref=&quot;threadPoolTaskExecutor&quot;/&gt;
 *     &lt;constructor-arg index=&quot;1&quot; ref=&quot;asyncJobWorker&quot;/&gt;
 * &lt;/bean&gt;
 * &lt;bean id=&quot;threadPoolTaskExecutor&quot; class=&quot;org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor&quot;&gt;
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
     * ワーカスレッド処理機能。
     */
    protected AsyncJobWorker asyncJobWorker;

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
     * @param threadPoolTaskExecutor ワーカスレッドの実行環境であるスレッドプール
     * @param asyncJobWorker ワーカスレッドの処理機能
     */
    public AsyncJobLauncherImpl(ThreadPoolTaskExecutor threadPoolTaskExecutor,
            AsyncJobWorker asyncJobWorker) {

        Assert.notNull(threadPoolTaskExecutor, LOGGER.getLogMessage(
                LogId.EAL025089, this.getClass().getSimpleName(),
                "ThreadPoolTaskExecutor"));
        Assert.notNull(asyncJobWorker, LOGGER.getLogMessage(LogId.EAL025089,
                this.getClass().getSimpleName(), "AsyncJobWorker"));

        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.asyncJobWorker = asyncJobWorker;
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
                        asyncJobWorker.executeWorker(jobSequenceId);
                    } catch (Throwable t) {
                        LOGGER.error(LogId.EAL025053, t);
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
                .getLogMessage(LogId.EAL025089, this.getClass().getSimpleName(),
                        "executor.jobTerminateWaitInterval"));

        int maxPoolSize = threadPoolTaskExecutor.getMaxPoolSize();
        LOGGER.debug(LogId.DAL025061, maxPoolSize, fair);
        taskPoolLimit = new Semaphore(maxPoolSize, fair);
    }
}
