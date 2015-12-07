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

package jp.terasoluna.fw.batch.executor.worker;

import jp.terasoluna.fw.batch.executor.ApplicationContextResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.BLogicResolver;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParamConverter;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.exception.handler.BLogicExceptionHandlerResolver;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.repository.BatchJobDataRepository;
import jp.terasoluna.fw.batch.executor.repository.JobStatusChanger;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.logger.TLogger;

/**
 * ジョブシーケンスコードで決定されるジョブの前処理と主処理を定義する実装クラス<br>
 * <p>
 * 前処理となる{@code beforeExecute}は実行された結果、{@code false}が返却された場合、 もしくは、{@code Exception}がスローされた場合、主処理である{@code executeWorker}
 * は実行されない。<br>
 * また、{@code beforeExecute}はメインスレッドで実行され、主処理の{@code executeWorker}はメインスレッドとは別のworkerスレッドとして実行される。 即ち、{@code beforeExecute}と
 * {@code executeWorker}は別のスレッドで実行されるため、注意すること。
 * </p>
 * @since 3.6
 */
public class WorkerTemplateImpl implements JobExecutorTemplate {

    /**
     * ロガー
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(WorkerTemplateImpl.class);

    /**
     * BLogicのインスタンスを取得するためのBLogicResolverオブジェクト
     */
    protected BLogicResolver bLogicResolver;

    /**
     * BLogic用の例外ハンドラのインスタンスを取得するためのBLogicExceptionHandlerResolverオブジェクト
     */
    protected BLogicExceptionHandlerResolver bLogicExceptionHandlerResolver;

    /**
     * ジョブ業務コードに対応するジョブ用DIコンテナを取得するためのApplicationContextResolverオブジェクト
     */
    protected ApplicationContextResolver bLogicApplicationContextResolver;

    /**
     * ジョブシーケンスコードに該当するBatchJobDataを取得するためのBatchJobDataRepositoryオブジェクト
     */
    protected BatchJobDataRepository batchJobDataRepository;

    /**
     * BatchJobDataからBLogicParamに変換するためのBLogicParamConverterオブジェクト
     */
    protected BLogicParamConverter bLogicParamConverter;

    /**
     * ジョブ管理テーブルのジョブステータスを変更するためのJobStatusChangerオブジェクト
     */
    protected JobStatusChanger jobStatusChanger;

    /**
     * BLogicの実行を移譲するBLogicExecutorオブジェクト
     */
    protected BLogicExecutor bLogicExecutor;

    /**
     * WorkerTemplateImplのコンストラクタ<br>
     * @param bLogicResolver BLogicのインスタンスを取得するためのBLogicResolverオブジェクト
     * @param bLogicExceptionHandlerResolver BLogic用の例外ハンドラのインスタンスを取得するためのBLogicExceptionHandlerResolverオブジェクト
     * @param bLogicApplicationContextResolver ジョブ業務コードに対応するジョブ用DIコンテナを取得するためのBLogicApplicationContextResolverオブジェクト
     * @param batchJobDataRepository ジョブシーケンスコードに該当するBatchJobDataを取得するためのBatchJobDataRepositoryオブジェクト
     * @param bLogicParamConverter BatchJobDataからBLogicParamに変換するためのBLogicParamConverterオブジェクト
     * @param bLogicExecutor BLogicの実行を移譲するBLogicExecutorオブジェクト
     * @param jobStatusChanger ジョブ管理テーブルのジョブステータスを変更するためのJobStatusChangerオブジェクト
     */
    protected WorkerTemplateImpl(BLogicResolver bLogicResolver,
            BLogicExceptionHandlerResolver bLogicExceptionHandlerResolver,
            ApplicationContextResolver bLogicApplicationContextResolver,
            BatchJobDataRepository batchJobDataRepository,
            BLogicParamConverter bLogicParamConverter,
            BLogicExecutor bLogicExecutor, JobStatusChanger jobStatusChanger) {
        Assert.notNull(bLogicResolver, LOGGER.getLogMessage(LogId.EAL025060));
        Assert.notNull(bLogicExceptionHandlerResolver, LOGGER
                .getLogMessage(LogId.EAL025061));
        Assert.notNull(bLogicApplicationContextResolver, LOGGER
                .getLogMessage(LogId.EAL025062));
        Assert.notNull(batchJobDataRepository, LOGGER
                .getLogMessage(LogId.EAL025063));
        Assert.notNull(bLogicParamConverter, LOGGER
                .getLogMessage(LogId.EAL025064));
        Assert.notNull(bLogicExecutor, LOGGER.getLogMessage(LogId.EAL025065));
        Assert.notNull(jobStatusChanger, LOGGER.getLogMessage(LogId.EAL025066));

        this.bLogicResolver = bLogicResolver;
        this.bLogicExceptionHandlerResolver = bLogicExceptionHandlerResolver;
        this.bLogicApplicationContextResolver = bLogicApplicationContextResolver;
        this.batchJobDataRepository = batchJobDataRepository;
        this.bLogicParamConverter = bLogicParamConverter;
        this.bLogicExecutor = bLogicExecutor;
        this.jobStatusChanger = jobStatusChanger;
    }

    /**
     * ジョブシーケンスコードに該当するジョブの前処理を行う<br>
     * <p>
     * ジョブシーケンスコードに該当するレコードのジョブステータスを「実行中：１」に変更する。 尚、このメソッドは、メインスレッド（ジョブの実行を制御するスレッド）で実行すること。
     * </p>
     * @param jobSequenceId ジョブシーケンスコード
     * @return 前処理の処理結果(falseならば主処理の実行を中断する)
     * @see jp.terasoluna.fw.batch.executor.worker.JobExecutorTemplate#beforeExecute(java.lang.String)
     */
    @Override
    public boolean beforeExecute(final String jobSequenceId) {
        try {
            boolean updated = jobStatusChanger
                    .changeToStartStatus(jobSequenceId);
            if (!updated) {
                LOGGER.info(LogId.IAL025010, jobSequenceId);
            }
            return updated;
        } catch (Exception e) {
            LOGGER.info(LogId.IAL025010, e, jobSequenceId);
        }
        // ステータス更新に失敗ているのでfalseで終了
        return false;
    }

    /**
     * ジョブシーケンスコードに該当するジョブの主処理を行う<br>
     * <p>
     * ジョブシーケンスコードに該当するBatchJobDataを取得後、ジョブ業務コード（jobAppCd）からBLogic、BLogicParam、例外ハンドラのそれぞれのインスタンスを取得し、
     * BLogicExecutorにBLogicの実行を移譲する。BLogicの実行後、ジョブシーケンスコードに該当するレコードのジョブステータスを 「処理済み：2」に変更する。<br>
     * 尚、このメソッドは、メインスレッドとは別スレッド（ジョブ実行用のworkerスレッド）で実行される。そのため、{@code beforeExecute}とは、 別スレッドで処理されることに注意すること。
     * </p>
     * @param jobSequenceId ジョブシーケンスコード
     * @see jp.terasoluna.fw.batch.executor.worker.JobExecutorTemplate#executeWorker(java.lang.String)
     */
    @Override
    public void executeWorker(final String jobSequenceId) {
        BLogicResult bLogicResult = new BLogicResult();
        ApplicationContext bLogicContext = null;
        ExceptionHandler bLogicExceptionHandler = null;
        BLogic bLogic = null;
        BLogicParam bLogicParam = null;

        try {
            BatchJobData batchJobData = batchJobDataRepository
                    .resolveBatchJobData(jobSequenceId);
            bLogicContext = bLogicApplicationContextResolver
                    .resolveApplicationContext(batchJobData);
            bLogic = bLogicResolver.resolveBLogic(bLogicContext, batchJobData.getJobAppCd());
            bLogicParam = bLogicParamConverter.convertBLogicParam(batchJobData);

            try {
                bLogicExceptionHandler = bLogicExceptionHandlerResolver
                        .resolveExceptionHandler(bLogicContext, batchJobData.getJobAppCd());
            } catch (Exception e) {
                // Do nothing
            }
            if (bLogicExceptionHandler == null) {
                // ExceptionHandlerがない場合でも処理を継続する
                LOGGER.warn(LogId.WAL025014);
            }

            try {
                bLogicResult = bLogicExecutor.execute(bLogicContext, bLogic,
                        bLogicParam, bLogicExceptionHandler);
            } catch (Exception e) {
                LOGGER.error(LogId.EAL025093, e, jobSequenceId);
            }
        } catch (Exception e) {
            LOGGER.error(LogId.EAL025068, e, jobSequenceId);
        } finally {
            afterExecuteWorker(jobSequenceId, bLogicResult);
            bLogicApplicationContextResolver
                    .closeApplicationContext(bLogicContext);
        }

    }

    /**
     * ジョブシーケンスコードに該当するジョブの後処理を行う<br>
     * <p>
     * ジョブシーケンスコードに該当するジョブのレコードに対し、ビジネスロジック戻り値を更新し、 ジョブステータスを「処理済み：2」に更新する。
     * </p>
     * @param jobSequenceId ジョブシーケンスコード
     * @param bLogicResult ビジネスロジック戻り値
     */
    protected void afterExecuteWorker(String jobSequenceId,
            BLogicResult bLogicResult) {
        Integer blogicStatus = bLogicResult == null ? null : bLogicResult
                .getBlogicStatus();

        try {
            boolean updated = jobStatusChanger.changeToEndStatus(jobSequenceId,
                    bLogicResult);
            if (!updated) {
                LOGGER.error(LogId.EAL025025, jobSequenceId, blogicStatus);
            }
        } catch (Exception e) {
            LOGGER.error(LogId.EAL025025, jobSequenceId, blogicStatus);
        }
    }

}
