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

package jp.terasoluna.fw.batch.executor;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.BLogicResolver;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParamConverter;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.exception.handler.BLogicExceptionHandlerResolver;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.repository.JobControlFinder;
import jp.terasoluna.fw.batch.executor.repository.JobStatusChanger;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.logger.TLogger;

/**
 * ジョブシーケンスコードにもとづいたジョブを、ワーカスレッドで実行する実装クラス<br>
 * <p>
 * 本クラスは、以下の3ステップに処理を分割している。
 * <ol>
 * <li>{@code #beforeExecute}:前処理。ジョブステータスを「実行中：1」に変更する</li>
 * <li>{@code #executeWorker}:主処理。BLogicを実行する。</li>
 * <li>{@code #afterExecuteWorker}:後処理。ジョブステータスを「処理済み：2」に変更する</li>
 * </ol>
 * それぞれ、前処理と後処理は、主処理のメソッドからそれぞれ呼び出される。
 * </p>
 * @since 3.6
 */
public class AsyncJobWorkerImpl implements AsyncJobWorker {

    /**
     * ロガー
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(AsyncJobWorkerImpl.class);

    /**
     * BLogicのインスタンスを取得するためのBLogicResolverオブジェクト
     */
    protected BLogicResolver blogicResolver;

    /**
     * BLogic用の例外ハンドラのインスタンスを取得するためのBLogicExceptionHandlerResolverオブジェクト
     */
    protected BLogicExceptionHandlerResolver blogicExceptionHandlerResolver;

    /**
     * ジョブ業務コードに対応するジョブ用DIコンテナを取得するためのApplicationContextResolverオブジェクト
     */
    protected ApplicationContextResolver blogicApplicationContextResolver;

    /**
     * ジョブシーケンスコードに該当するBatchJobDataを取得するためのJobControlFinderオブジェクト
     */
    protected JobControlFinder jobControlFinder;

    /**
     * BatchJobDataからBLogicParamに変換するためのBLogicParamConverterオブジェクト
     */
    protected BLogicParamConverter blogicParamConverter;

    /**
     * ジョブ管理テーブルのジョブステータスを変更するためのJobStatusChangerオブジェクト
     */
    protected JobStatusChanger jobStatusChanger;

    /**
     * BLogicの実行を移譲するBLogicExecutorオブジェクト
     */
    protected BLogicExecutor blogicExecutor;

    /**
     * AsyncJobWorkerImplのコンストラクタ
     * 
     * @param blogicResolver BLogicのインスタンスを取得するためのBLogicResolverオブジェクト
     * @param blogicExceptionHandlerResolver BLogic用の例外ハンドラのインスタンスを取得するためのBLogicExceptionHandlerResolverオブジェクト
     * @param blogicApplicationContextResolver ジョブ業務コードに対応するジョブ用DIコンテナを取得するためのBLogicApplicationContextResolverオブジェクト
     * @param jobControlFinder ジョブシーケンスコードに該当するBatchJobDataを取得するためのJobControlFinderオブジェクト
     * @param blogicParamConverter BatchJobDataからBLogicParamに変換するためのBLogicParamConverterオブジェクト
     * @param blogicExecutor BLogicの実行を移譲するBLogicExecutorオブジェクト
     * @param jobStatusChanger ジョブ管理テーブルのジョブステータスを変更するためのJobStatusChangerオブジェクト
     */
    protected AsyncJobWorkerImpl(BLogicResolver blogicResolver,
            BLogicExceptionHandlerResolver blogicExceptionHandlerResolver,
            ApplicationContextResolver blogicApplicationContextResolver,
            JobControlFinder jobControlFinder,
            BLogicParamConverter blogicParamConverter,
            BLogicExecutor blogicExecutor, JobStatusChanger jobStatusChanger) {
        Assert.notNull(blogicResolver, LOGGER.getLogMessage(LogId.EAL025056,
                this.getClass().getSimpleName(), "BLogicResolver"));
        Assert.notNull(blogicExceptionHandlerResolver, LOGGER.getLogMessage(
                LogId.EAL025056, this.getClass().getSimpleName(),
                "BLogicExceptionHandlerResolver"));
        Assert.notNull(blogicApplicationContextResolver, LOGGER.getLogMessage(
                LogId.EAL025056, this.getClass().getSimpleName(),
                "ApplicationContextResolver"));
        Assert.notNull(jobControlFinder, LOGGER.getLogMessage(LogId.EAL025056,
                this.getClass().getSimpleName(), "JobControlFinder"));
        Assert.notNull(blogicParamConverter, LOGGER.getLogMessage(
                LogId.EAL025056, this.getClass().getSimpleName(),
                "BLogicParamConverter"));
        Assert.notNull(blogicExecutor, LOGGER.getLogMessage(LogId.EAL025056,
                this.getClass().getSimpleName(), "BLogicExecutor"));
        Assert.notNull(jobStatusChanger, LOGGER.getLogMessage(LogId.EAL025056,
                this.getClass().getSimpleName(), "JobStatusChanger"));

        this.blogicResolver = blogicResolver;
        this.blogicExceptionHandlerResolver = blogicExceptionHandlerResolver;
        this.blogicApplicationContextResolver = blogicApplicationContextResolver;
        this.jobControlFinder = jobControlFinder;
        this.blogicParamConverter = blogicParamConverter;
        this.blogicExecutor = blogicExecutor;
        this.jobStatusChanger = jobStatusChanger;
    }

    /**
     * ジョブシーケンスコードに該当するジョブの前処理を行う<br>
     * <p>
     * ジョブシーケンスコードに該当するレコードのジョブステータスを「実行中：１」に変更する。
     * </p>
     * @param jobSequenceId ジョブシーケンスコード
     * @return 前処理の処理結果(true:更新成功、false:更新失敗)
     */
    protected boolean beforeExecute(final String jobSequenceId) {
        boolean updated = jobStatusChanger.changeToStartStatus(jobSequenceId);
        return updated;
    }

    /**
     * ジョブシーケンスコードに該当するジョブの主処理を行う<br>
     * <p>
     * <ul>
     * <li>{@code #beforeExecute}を呼び出す</li>
     * <li>ジョブシーケンスコードに該当するBatchJobDataを取得後、ジョブ業務コード（jobAppCd）からBLogic、BLogicParam、例外ハンドラのそれぞれのインスタンスを取得し、
     * BLogicExecutorにBLogicの実行を移譲する。</li>
     * <li>{@code #afterExecuteWorker}を呼び出す。</li>
     * </ul>
     * </p>
     * @param jobSequenceId ジョブシーケンスコード
     */
    @Override
    public void executeWorker(final String jobSequenceId) {

        if (!beforeExecute(jobSequenceId)) {
            LOGGER.info(LogId.IAL025021, jobSequenceId);
            return;
        }

        BLogicResult blogicResult = new BLogicResult();
        ApplicationContext blogicContext = null;
        ExceptionHandler blogicExceptionHandler = null;
        BLogic blogic = null;
        BLogicParam blogicParam = null;

        try {
            BatchJobData batchJobData = jobControlFinder
                    .resolveBatchJobData(jobSequenceId);
            blogicContext = blogicApplicationContextResolver
                    .resolveApplicationContext(batchJobData);
            blogic = blogicResolver.resolveBLogic(blogicContext, batchJobData.getJobAppCd());
            blogicParam = blogicParamConverter.convertBLogicParam(batchJobData);

            try {
                blogicExceptionHandler = blogicExceptionHandlerResolver
                        .resolveExceptionHandler(blogicContext, batchJobData.getJobAppCd());
            } catch (Exception e) {
                // Do nothing
            }
            if (blogicExceptionHandler == null) {
                // ExceptionHandlerがない場合でも処理を継続する
                LOGGER.warn(LogId.WAL025010);
            }

            try {
                blogicResult = blogicExecutor.execute(blogicContext, blogic,
                        blogicParam, blogicExceptionHandler);
            } catch (Exception e) {
                LOGGER.error(LogId.EAL025059, e, jobSequenceId);
            }
        } catch (Exception e) {
            LOGGER.error(LogId.EAL025055, e, jobSequenceId);
        } finally {
            afterExecuteWorker(jobSequenceId, blogicResult);
            blogicApplicationContextResolver
                    .closeApplicationContext(blogicContext);
        }

    }

    /**
     * ジョブシーケンスコードに該当するジョブの後処理を行う<br>
     * <p>
     * ジョブシーケンスコードに該当するジョブのレコードに対し、ビジネスロジック戻り値を更新し、 ジョブステータスを「処理済み：2」に更新する。
     * </p>
     * @param jobSequenceId ジョブシーケンスコード
     * @param blogicResult ビジネスロジック戻り値
     */
    protected void afterExecuteWorker(String jobSequenceId,
            BLogicResult blogicResult) {
        Integer blogicStatus = blogicResult == null ? null : blogicResult
                .getBlogicStatus();

        try {
            boolean updated = jobStatusChanger.changeToEndStatus(jobSequenceId,
                    blogicResult);
            if (!updated) {
                LOGGER.error(LogId.EAL025025, jobSequenceId, blogicStatus);
            }
        } catch (Exception e) {
            LOGGER.error(LogId.EAL025025, jobSequenceId, blogicStatus);
        }
    }

}
