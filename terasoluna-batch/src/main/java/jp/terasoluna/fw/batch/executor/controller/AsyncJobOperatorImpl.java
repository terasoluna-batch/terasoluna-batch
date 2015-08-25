/*
 * Copyright (c) 2015 NTT DATA Corporation
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
import jp.terasoluna.fw.batch.exception.handler.ExceptionStatusHandler;
import jp.terasoluna.fw.batch.executor.repository.BatchJobDataRepository;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;
import jp.terasoluna.fw.logger.TLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * 非同期ジョブの起動クラス。<br>
 * {@code start()}メソッド実行後、実行対象ジョブの検索によるポーリングと、
 * ポーリングループの終了監視を行う。<br>
 *
 * @since 3.6
 */
public class AsyncJobOperatorImpl implements JobOperator {

    /**
     * ロガー。<br>
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(AsyncJobOperatorImpl.class);
    
    /**
     * ジョブのポーリング間隔。<br>
     */
    @Value("${polling.interval:1000}")
    protected long jobIntervalTime;

    /**
     * ジョブの検索機能。<br>
     */
    protected BatchJobDataRepository batchJobDataRepository;

    /**
     * ジョブの起動機能。<br>
     */
    protected AsyncJobLauncher asyncJobLauncher;

    /**
     * 終了条件監視機能。<br>
     */
    protected AsyncBatchStopper asyncBatchStopper;

    /**
     * フレームワーク例外ハンドリング機能。<br>
     */
    protected ExceptionStatusHandler exceptionStatusHandler;

    /**
     * コンストラクタ。<br>
     * ジョブの起動とポーリングループの終了条件監視に必要となる機能を設定する。
     *
     * @param batchJobDataRepository ジョブの検索機能
     * @param asyncJobLauncher       ジョブの起動機能
     * @param asyncBatchStopper      終了条件監視機能
     * @param exceptionStatusHandler フレームワーク例外ハンドリング機能
     */
    public AsyncJobOperatorImpl(BatchJobDataRepository batchJobDataRepository,
            AsyncJobLauncher asyncJobLauncher,
            AsyncBatchStopper asyncBatchStopper,
            ExceptionStatusHandler exceptionStatusHandler) {
        Assert.notNull(batchJobDataRepository,
                LOGGER.getLogMessage(LogId.EAL025074));
        Assert.notNull(asyncJobLauncher, LOGGER.getLogMessage(LogId.EAL025075));
        Assert.notNull(asyncBatchStopper,
                LOGGER.getLogMessage(LogId.EAL025076));
        Assert.notNull(exceptionStatusHandler,
                LOGGER.getLogMessage(LogId.EAL025077));

        this.batchJobDataRepository = batchJobDataRepository;
        this.asyncJobLauncher = asyncJobLauncher;
        this.asyncBatchStopper = asyncBatchStopper;
        this.exceptionStatusHandler = exceptionStatusHandler;
    }

    /**
     * ジョブ起動処理のエントリポイント。<br>
     *
     * @param args 起動時引数
     * @return 終了ステータスコード
     */
    @Override
    public int start(String[] args) {
        try {
            while (!asyncBatchStopper.canStop()) {
                BatchJobListResult batchJobListResult = batchJobDataRepository
                        .resolveBatchJobResult(args);
                if (batchJobListResult == null) {
                    pollingSleep();
                    continue;
                }
                // ジョブの実行
                asyncJobLauncher
                        .executeJob(batchJobListResult.getJobSequenceId());
            }
        } catch (Exception e) {
            // ループ中断例外と返却ステータスコードの判定
            return exceptionStatusHandler.handleException(e);
        } finally {
            asyncJobLauncher.shutdown();
        }
        return 0;
    }

    /**
     * ポーリングにより実行対象となるジョブが見つからない場合一定時間スリープさせる。<br>
     * スリープの時間は{@code jobIntervalTime}プロパティで定められる。
     *
     * @throws InterruptedException 割り込み例外
     */
    protected void pollingSleep() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(jobIntervalTime);
    }
}
