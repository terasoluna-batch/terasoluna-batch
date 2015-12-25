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

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.BLogicResolver;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParamConverter;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.exception.BatchException;
import jp.terasoluna.fw.batch.exception.handler.BLogicExceptionHandlerResolver;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.ApplicationContextResolver;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.BLogicExecutor;
import jp.terasoluna.fw.logger.TLogger;
import org.apache.commons.beanutils.BeanUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;

/**
 * 同期型ジョブの実行を管理するためにフレームワークが提供するデフォルトの実装クラス。<br>
 * メインスレッドでビジネスロジックを実行する。<br>
 *
 * @since 3.6
 */
public class SyncJobOperatorImpl implements JobOperator {

    /**
     * ロガー。
     */
    private static final TLogger LOGGER = TLogger.getLogger(
            SyncJobOperatorImpl.class);

    /**
     * 環境変数：ジョブシーケンスコード.
     */
    protected static final String ENV_JOB_SEQ_ID = "JOB_SEQ_ID";

    /**
     * 環境変数：ジョブ業務コード.
     */
    protected static final String ENV_JOB_APP_CD = "JOB_APP_CD";

    /**
     * 環境変数：業務ステータス.
     */
    protected static final String ENV_BLOGIC_APP_STATUS = "BLOGIC_APP_STATUS";

    /**
     * 環境変数：ステータス.
     */
    protected static final String ENV_CUR_APP_STATUS = "CUR_APP_STATUS";

    /**
     * 環境変数：引数の最大個数.
     */
    protected static final int ENV_JOB_ARG_MAX = 20;

    /**
     * 環境変数：引数.
     */
    protected static final String ENV_JOB_ARG_NM = "JOB_ARG_NM";

    /**
     * 引数パラメータの基本部分
     */
    private static final String JOB_ARG_PARAM_BASE = "jobArgNm";

    /**
     * ビジネスロジックインスタンス取得
     */
    protected BLogicResolver blogicResolver;

    /**
     * 業務例外ハンドラ取得
     */
    protected BLogicExceptionHandlerResolver blogicExceptionHandlerResolver;

    /**
     * {@code BatchJobData}からビジネスロジック入力情報への変換
     */
    protected BLogicParamConverter blogicParamConverter;

    /**
     * 業務アプリケーションコンテキスト取得
     */
    protected ApplicationContextResolver applicationContextResolver;

    /**
     * ビジネスロジック実行機能
     */
    protected BLogicExecutor blogicExecutor;

    /**
     * コンストラクタ。<br>
     *
     * @param applicationContextResolver     ビジネスロジック用アプリケーションコンテキスト取得機能
     * @param blogicParamConverter           ビジネスロジック入力オブジェクトへの変換機能
     * @param blogicExceptionHandlerResolver ビジネスロジック例外ハンドラ取得機能
     * @param blogicResolver                 ビジネスロジック取得機能
     * @param blogicExecutor                 ビジネスロジック実行機能
     */
    public SyncJobOperatorImpl(
            ApplicationContextResolver applicationContextResolver,
            BLogicParamConverter blogicParamConverter,
            BLogicExceptionHandlerResolver blogicExceptionHandlerResolver,
            BLogicResolver blogicResolver, BLogicExecutor blogicExecutor) {

        Assert.notNull(applicationContextResolver,
                LOGGER.getLogMessage(LogId.EAL025089, "SyncJobOperatorImpl",
                        "applicationContextResolver"));
        Assert.notNull(blogicParamConverter,
                LOGGER.getLogMessage(LogId.EAL025089, "SyncJobOperatorImpl",
                        "blogicParamConverter"));
        Assert.notNull(blogicExceptionHandlerResolver,
                LOGGER.getLogMessage(LogId.EAL025089, "SyncJobOperatorImpl",
                        "blogicExceptionHandlerResolver"));
        Assert.notNull(blogicResolver,
                LOGGER.getLogMessage(LogId.EAL025089, "SyncJobOperatorImpl",
                        "blogicResolver"));
        Assert.notNull(blogicExecutor,
                LOGGER.getLogMessage(LogId.EAL025089, "SyncJobOperatorImpl",
                        "blogicExecutor"));

        this.applicationContextResolver = applicationContextResolver;
        this.blogicParamConverter = blogicParamConverter;
        this.blogicExceptionHandlerResolver = blogicExceptionHandlerResolver;
        this.blogicResolver = blogicResolver;
        this.blogicExecutor = blogicExecutor;
    }

    /**
     * ジョブの起動処理
     *
     * @param args 起動時引数
     * @return ステータスコード
     */
    @Override
    public int start(String[] args) {
        BatchJobData batchJobData = convertBatchJobData(args);
        BLogicParam blogicParam = blogicParamConverter.convertBLogicParam(
                batchJobData);
        ApplicationContext blogicContext = applicationContextResolver.resolveApplicationContext(
                batchJobData);
        try {
            BLogic blogic = blogicResolver.resolveBLogic(blogicContext,
                    blogicParam.getJobAppCd());
            ExceptionHandler exceptionHandler = blogicExceptionHandlerResolver.resolveExceptionHandler(
                    blogicContext, blogicParam.getJobAppCd());

            BLogicResult result = blogicExecutor.execute(blogicContext, blogic,
                    blogicParam, exceptionHandler);
            return result.getBlogicStatus();
        } finally {
            applicationContextResolver.closeApplicationContext(blogicContext);
        }
    }

    /**
     * Java起動引数・環境変数からジョブレコードデータに変換する。
     *
     * @param args Java起動引数
     * @return ジョブレコードデータ
     */
    protected BatchJobData convertBatchJobData(String[] args) {

        // ジョブレコードデータ
        BatchJobData jobRecord = new BatchJobData();

        // 起動引数要素が上限以上存在するとき、args4jがCmdLineExceptionを
        // スローするので起動時引数配列要素数か、配列要素上限数の小さい方に補正する。
        int arrayLength = (ENV_JOB_ARG_MAX + 1 < args.length) ?
                ENV_JOB_ARG_MAX + 1 :
                args.length;
        String[] fixArgs = new String[arrayLength];
        System.arraycopy(args, 0, fixArgs, 0, arrayLength);

        try {
            new CmdLineParser(jobRecord).parseArgument(fixArgs);
        } catch (CmdLineException e) {
            // 検査例外だが配列要素数上限をオーバーせず、OptionHandlerを使用しない場合はキャッチできない例外
            throw new BatchException(e);
        }

        // 引数に「ジョブ業務コード」が指定されていなければ、環境変数から取得する
        if (jobRecord.getJobAppCd() == null
                || jobRecord.getJobAppCd().length() == 0) {
            jobRecord.setJobAppCd(getenv(ENV_JOB_APP_CD));
        }

        // 引数に「引数1」～「引数20」が指定されていなければ、環境変数から取得する
        StringBuilder envName = new StringBuilder();
        StringBuilder paramName = new StringBuilder();
        for (int i = 1; i <= ENV_JOB_ARG_MAX; i++) {

            if (i < fixArgs.length && fixArgs[i] != null && fixArgs[i].length() > 0) {
                // 起動引数に設定済みの場合、次の引数を確認する。
                continue;
            }

            envName.setLength(0);
            envName.append(ENV_JOB_ARG_NM);
            envName.append(i);

            String envParam = getenv(envName.toString());

            if (envParam.length() == 0) {
                continue;
            }

            paramName.setLength(0);
            paramName.append(JOB_ARG_PARAM_BASE);
            paramName.append(i);

            try {
                BeanUtils.setProperty(jobRecord, paramName.toString(), envParam);
            } catch (IllegalAccessException | InvocationTargetException e) {
                // 検査例外だが、Beanの型・プロパティを変更しない限りキャッチできない
                throw new BatchException(e);
            }
        }

        // ジョブシーケンスコード
        jobRecord.setJobSequenceId(getenv(ENV_JOB_SEQ_ID));

        // 業務ステータス
        jobRecord.setErrAppStatus(getenv(ENV_BLOGIC_APP_STATUS));

        // ステータス
        jobRecord.setCurAppStatus(getenv(ENV_CUR_APP_STATUS));

        return jobRecord;
    }

    /**
     * <h6>指定された環境変数の値を取得する.</h6>
     * <p>
     * システム環境で変数を定義しない場合は ""（空文字） を返す
     * </p>
     *
     * @param name 環境変数名
     * @return 環境変数の値
     */
    protected String getenv(String name) {
        String ret = System.getenv(name);
        if (ret == null) {
            return "";
        }
        return ret;
    }
}
