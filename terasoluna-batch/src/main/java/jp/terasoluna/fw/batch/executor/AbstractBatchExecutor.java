/*
 * Copyright (c) 2011 NTT DATA Corporation
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

import java.beans.Introspector;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import jp.terasoluna.fw.batch.annotation.JobComponent;
import jp.terasoluna.fw.batch.annotation.util.GenericBeanFactoryAccessorEx;
import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.message.MessageAccessor;
import jp.terasoluna.fw.batch.util.MessageUtil;
import jp.terasoluna.fw.logger.TLogger;
import jp.terasoluna.fw.util.PropertyUtil;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 同期バッチエグゼキュータ抽象クラス。<br>
 * <br>
 * 同期ジョブ起動用のバッチエグゼキュータ。
 * @see jp.terasoluna.fw.batch.executor.BatchExecutor
 * @see jp.terasoluna.fw.batch.executor.SyncBatchExecutor
 */
public abstract class AbstractBatchExecutor implements BatchExecutor {

    /**
     * ログ.
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(AbstractBatchExecutor.class);

    /**
     * 環境変数：ジョブシーケンスコード.
     */
    protected static final String ENV_JOB_SEQ_ID = "JOB_SEQ_ID";

    /**
     * 環境変数：ジョブ業務コード.
     */
    protected static final String ENV_JOB_APP_CD = "JOB_APP_CD";

    /**
     * 環境変数：bean定義ファイルパス.
     */
    protected static final String ENV_JOB_BEAN_DEFINITION_PATH = "JOB_BEAN_DEFINITION_PATH";

    /**
     * 環境変数：引数の最大個数.
     */
    protected static final int ENV_JOB_ARG_MAX = 20;

    /**
     * 環境変数：引数.
     */
    protected static final String ENV_JOB_ARG_NM = "JOB_ARG_NM";

    /**
     * 環境変数：引数1.
     */
    protected static final String ENV_JOB_ARG_NM1 = "JOB_ARG_NM1";

    /**
     * 環境変数：引数2.
     */
    protected static final String ENV_JOB_ARG_NM2 = "JOB_ARG_NM2";

    /**
     * 環境変数：引数3.
     */
    protected static final String ENV_JOB_ARG_NM3 = "JOB_ARG_NM3";

    /**
     * 環境変数：引数4.
     */
    protected static final String ENV_JOB_ARG_NM4 = "JOB_ARG_NM4";

    /**
     * 環境変数：引数5.
     */
    protected static final String ENV_JOB_ARG_NM5 = "JOB_ARG_NM5";

    /**
     * 環境変数：引数6.
     */
    protected static final String ENV_JOB_ARG_NM6 = "JOB_ARG_NM6";

    /**
     * 環境変数：引数7.
     */
    protected static final String ENV_JOB_ARG_NM7 = "JOB_ARG_NM7";

    /**
     * 環境変数：引数8.
     */
    protected static final String ENV_JOB_ARG_NM8 = "JOB_ARG_NM8";

    /**
     * 環境変数：引数9.
     */
    protected static final String ENV_JOB_ARG_NM9 = "JOB_ARG_NM9";

    /**
     * 環境変数：引数10.
     */
    protected static final String ENV_JOB_ARG_NM10 = "JOB_ARG_NM10";

    /**
     * 環境変数：引数11.
     */
    protected static final String ENV_JOB_ARG_NM11 = "JOB_ARG_NM11";

    /**
     * 環境変数：引数12.
     */
    protected static final String ENV_JOB_ARG_NM12 = "JOB_ARG_NM12";

    /**
     * 環境変数：引数13.
     */
    protected static final String ENV_JOB_ARG_NM13 = "JOB_ARG_NM13";

    /**
     * 環境変数：引数14.
     */
    protected static final String ENV_JOB_ARG_NM14 = "JOB_ARG_NM14";

    /**
     * 環境変数：引数15.
     */
    protected static final String ENV_JOB_ARG_NM15 = "JOB_ARG_NM15";

    /**
     * 環境変数：引数16.
     */
    protected static final String ENV_JOB_ARG_NM16 = "JOB_ARG_NM16";

    /**
     * 環境変数：引数17.
     */
    protected static final String ENV_JOB_ARG_NM17 = "JOB_ARG_NM17";

    /**
     * 環境変数：引数18.
     */
    protected static final String ENV_JOB_ARG_NM18 = "JOB_ARG_NM18";

    /**
     * 環境変数：引数19.
     */
    protected static final String ENV_JOB_ARG_NM19 = "JOB_ARG_NM19";

    /**
     * 環境変数：引数20.
     */
    protected static final String ENV_JOB_ARG_NM20 = "JOB_ARG_NM20";

    /**
     * 環境変数：業務ステータス.
     */
    protected static final String ENV_BLOGIC_APP_STATUS = "BLOGIC_APP_STATUS";

    /**
     * 環境変数：ステータス.
     */
    protected static final String ENV_CUR_APP_STATUS = "CUR_APP_STATUS";

    /**
     * システム用DAO定義（ステータス参照・更新用）取得用キー.
     */
    protected static final String SYSTEM_DATASOURCE_DAO = "systemDataSource.systemDao";

    /**
     * システム用transactionManager定義（ステータス参照・更新用）取得用キー.
     */
    protected static final String SYSTEM_DATASOURCE_TRANSACTION_MANAGER = "systemDataSource.transactionManager";

    /**
     * 管理用Bean定義ファイルを配置するクラスパス.
     */
    protected static final String BEAN_DEFINITION_ADMIN_CLASSPATH_KEY = "beanDefinition.admin.classpath";

    /**
     * 管理用Bean定義（基本部）
     */
    protected static final String BEAN_DEFINITION_DEFAULT = "beanDefinition.admin.default";

    /**
     * 管理用Bean定義（データソース部）
     */
    protected static final String BEAN_DEFINITION_DATASOURCE = "beanDefinition.admin.dataSource";

    /**
     * 業務用Bean定義ファイルを配置するクラスパス.
     */
    protected static final String BEAN_DEFINITION_BUSINESS_CLASSPATH_KEY = "beanDefinition.business.classpath";

    /**
     * Bean定義ファイル名.
     */
    protected static final String PROPERTY_BEAN_FILENAME_SUFFIX = ".xml";

    /**
     * メッセージソースアクセサのBean名取得キー.
     */
    protected static final String BEAN_MESSAGE_ACCESSOR_DEFAULT = "messageAccessor.default";

    /**
     * JobComponentアノテーション有効化フラグ取得キー.
     */
    protected static final String ENABLE_JOBCOMPONENT_ANNOTATION = "enableJobComponentAnnotation";

    /**
     * BLogicのBean名に付与する接尾語.
     */
    protected static final String DEFAULT_BLOGIC_BEAN_NAME_SUFFIX = "BLogic";

    /**
     * 例外ハンドラのBean名に付与する接尾語.
     */
    protected static final String DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME_SUFFIX = "ExceptionHandler";

    /**
     * デフォルトの例外ハンドラのBean名.
     */
    protected static final String DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME = "defaultExceptionHandler";

    /**
     * アプリケーションコンテキストクラス名.
     */
    protected static final String APPLICATION_CONTEXT = "org.springframework.context.support.ClassPathXmlApplicationContext";

    /**
     * 置換文字列接頭語
     */
    protected static final String REPLACE_STRING_PREFIX = "\\$\\{";

    /**
     * 置換文字列接尾語
     */
    protected static final String REPLACE_STRING_SUFFIX = "\\}";

    /**
     * 置換文字列：ジョブ業務コード
     */
    protected static final String REPLACE_STRING_JOB_APP_CD = "jobAppCd";

    /**
     * 置換文字列：ジョブ業務コード（大文字）
     */
    protected static final String REPLACE_STRING_JOB_APP_CD_UPPER = "jobAppCdUpper";

    /**
     * 置換文字列：ジョブ業務コード（小文字）
     */
    protected static final String REPLACE_STRING_JOB_APP_CD_LOWER = "jobAppCdLower";

    /**
     * 置換文字列：引数.
     */
    protected static final String REPLACE_STRING_JOB_ARG = "jobArg";

    /**
     * 置換文字列：引数の最大個数.
     */
    protected static final int REPLACE_STRING_JOB_ARG_MAX = 20;

    /**
     * バッチ引数の数.
     */
    protected static final int ARGUMENT_COUNT = 20;

    /**
     * バッチ引数のフィールド名.
     */
    protected static final String FIELD_JOB_ARG = "JobArgNm";

    /**
     * クラスローダ.
     */
    protected static ClassLoader cl = null;

    /**
     * システム用DAO定義（ステータス参照・更新用）.
     */
    protected SystemDao systemDao = null;

    /**
     * システム用transactionManager定義（ステータス参照・更新用）.
     */
    protected PlatformTransactionManager sysTransactionManager = null;

    /**
     * システム用アプリケーションコンテキスト.
     */
    protected ApplicationContext defaultApplicationContext = null;

    /**
     * JobComponentアノテーション有効化フラグ
     */
    protected boolean enableJobComponentAnnotation = false;

    /**
     * It's not a good idea to put code that can fail in a class initializer, but for sake of argument, here's how you configure
     * an SQL Map.
     */
    static {
        // クラスローダ取得（カレントスレッドのコンテキストクラスローダ）
        cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            LOGGER.error(LogId.EAL025002);
        }
    }

    /**
     * コンストラクタ
     */
    protected AbstractBatchExecutor() {
        super();
        init();
    }

    /**
     * 初期化
     */
    protected void init() {
        // システム共通AppContextName初期化
        initDefaultAppContext();

        // システム共通DAO初期化
        initSystemDatasourceDao();

        // エラーメッセージの初期化
        initDefaultErrorMessage();

        // JobComponentアノテーション有効化フラグ取得
        String enableJobComponentAnnotationStr = PropertyUtil
                .getProperty(ENABLE_JOBCOMPONENT_ANNOTATION);
        if (enableJobComponentAnnotationStr != null
                && enableJobComponentAnnotationStr.length() != 0) {
            this.enableJobComponentAnnotation = Boolean
                    .parseBoolean(enableJobComponentAnnotationStr);
        }
    }

    /**
     * 管理用に用いられるApplicationContextを初期化する.
     */
    protected void initDefaultAppContext() {
        // システムアプリケーションコンテキスト取得
        String defaultAppContextName = getDefaultBeanFileName();
        if (defaultAppContextName == null || "".equals(defaultAppContextName)) {
            LOGGER.error(LogId.EAL025003);
            return;
        }
        defaultApplicationContext = getApplicationContext(defaultAppContextName);
        if (defaultApplicationContext == null) {
            LOGGER.error(LogId.EAL025004, defaultAppContextName);
            return;
        }
    }

    /**
     * システム共通で用いられるDAOをBean定義ファイルから取得する.
     */
    protected void initSystemDatasourceDao() {
        // AbstractJobBatchExecutorに移動
    }

    /**
     * エラーメッセージの初期化.
     */
    protected void initDefaultErrorMessage() {
        if (defaultApplicationContext == null) {
            return;
        }

        // メッセージソースアクセサのBean名取得
        String value = PropertyUtil.getProperty(BEAN_MESSAGE_ACCESSOR_DEFAULT);

        if (value == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(LogId.WAL025001, value, getThreadMessage());
            }
            return;
        }

        // メッセージソースアクセサ取得.
        if (defaultApplicationContext.containsBean(value)) {
            MessageAccessor messageAccessor = null;
            try {
                messageAccessor = defaultApplicationContext
                        .getBean(value, MessageAccessor.class);
            } catch (Throwable e) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(LogId.WAL025001, value, getThreadMessage());
                }
            }
            if (messageAccessor != null) {
                MessageUtil.setMessageAccessor(messageAccessor);
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(LogId.TAL025001, getThreadMessage());
                }
            } else {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(LogId.TAL025009, getThreadMessage());
                }
            }
        } else {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(LogId.WAL025001, value, getThreadMessage());
            }
        }
    }

    /**
     * スレッドグループとスレッド名を返すメソッド.
     * @return String
     */
    protected String getThreadMessage() {
        StringBuilder sb = new StringBuilder();
        Thread ct = Thread.currentThread();

        if (ct != null && ct.getThreadGroup() != null) {
            sb.append(" tg:[");
            sb.append(ct.getThreadGroup().getName());
            sb.append("]");
        }
        if (ct != null) {
            sb.append(" t:[");
            sb.append(ct.getName());
            sb.append("]");
        }

        return sb.toString();
    }

    /**
     * ジョブ用ApplicationContextを初期化する.
     * @param jobAppCd String
     * @param jobRecord BatchJobData
     * @return ApplicationContext
     */
    protected ApplicationContext initJobAppContext(String jobAppCd,
            BatchJobData jobRecord) {
        ApplicationContext context = null;
        String beanFileName = null;

        beanFileName = getBeanFileName(jobAppCd, jobRecord);
        LOGGER.debug(LogId.DAL025018, beanFileName);

        if (beanFileName != null && 0 < beanFileName.length()) {
            context = getApplicationContext(beanFileName);

            if (context == null) {
                LOGGER.error(LogId.EAL025006, beanFileName);
            }

            LOGGER.debug(LogId.DAL025019);
        }

        return context;
    }

    /**
     * ApplicationContextをクローズする.
     * @param context
     */
    protected void closeApplicationContext(ApplicationContext context) {
        MessageUtil.removeMessageAccessor();

        if (context instanceof AbstractApplicationContext) {
            AbstractApplicationContext aac = (AbstractApplicationContext) context;
            aac.close();
            aac.destroy();
        }
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.executor.BatchExecutor#executeBatch(jp. terasoluna.fw.batch.executor.vo.BatchJobData)
     */
    public BLogicResult executeBatch(BatchJobData jobRecord) {
        BLogicResult result = new BLogicResult();

        // バッチジョブレコードデータをBLogicParamに変換する
        BLogicParam param = convertBLogicParam(jobRecord);
        if (param == null) {
            // 異常終了
            return result;
        }

        LOGGER.debug(LogId.DAL025044, param);

        // ジョブ業務コード（第一引数）からクラス名を取得
        String jobAppCd = jobRecord.getJobAppCd();

        // BLogicのBean名取得
        String blogicBeanName = getBlogicBeanName(jobAppCd);
        if ((blogicBeanName == null || blogicBeanName.length() == 0)) {
            LOGGER.error(LogId.EAL025007, jobRecord.getJobAppCd());
            return result;
        }

        // 例外ハンドラのBean名取得
        String exceptionHandlerBeanName = getExceptionHandlerBeanName(jobAppCd);
        if ((exceptionHandlerBeanName == null || exceptionHandlerBeanName
                .length() == 0)) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(LogId.EAL025007, jobRecord.getJobAppCd());
            }
            return result;
        }

        // Bean定義ファイル取得
        ApplicationContext context = initJobAppContext(jobAppCd, jobRecord);
        ThreadGroupApplicationContextHolder.setApplicationContext(context);

        try {
            // バッチ実行
            if (blogicBeanName != null && 0 < blogicBeanName.length()) {
                // クラス
                result = executeBatchClass(blogicBeanName,
                        exceptionHandlerBeanName, param, context);
            }
        } finally {
            ThreadGroupApplicationContextHolder.removeApplicationContext();
            // ジョブ用ApplicationContextをクローズする
            closeApplicationContext(context);
        }

        return result;
    }

    /**
     * バッチジョブレコードデータをBLogicParamに変換する
     * @param jobRecord BatchJobData
     * @return BLogicParam
     */
    protected BLogicParam convertBLogicParam(BatchJobData jobRecord) {
        BLogicParam param = new BLogicParam();
        param.setJobSequenceId(jobRecord.getJobSequenceId());
        param.setJobAppCd(jobRecord.getJobAppCd());

        // 引数コピー
        boolean ret = argumentCopy(jobRecord, param, FIELD_JOB_ARG);
        if (!ret) {
            // 異常終了
            return null;
        }
        return param;
    }

    /**
     * 管理用Bean定義（基本部）ファイル名を取得する。
     * @return 管理用Bean定義（基本部）ファイル名
     */
    protected static String getDefaultBeanFileName() {
        StringBuilder str = new StringBuilder();
        String classpath = PropertyUtil
                .getProperty(BEAN_DEFINITION_ADMIN_CLASSPATH_KEY);
        String beanFileName = PropertyUtil.getProperty(BEAN_DEFINITION_DEFAULT);
        str.append(classpath == null ? "" : classpath);
        str.append(beanFileName == null ? "" : beanFileName);
        LOGGER.debug(LogId.DAL025020, str);
        return str.toString();
    }

    /**
     * 管理用Bean定義（データソース部）ファイル名を取得する。
     * @return 管理用Bean定義（データソース部）ファイル名
     */
    protected static String getDataSourceBeanFileName() {
        StringBuilder str = new StringBuilder();
        String classpath = PropertyUtil
                .getProperty(BEAN_DEFINITION_ADMIN_CLASSPATH_KEY);
        String beanFileName = PropertyUtil
                .getProperty(BEAN_DEFINITION_DATASOURCE);
        str.append(classpath == null ? "" : classpath);
        str.append(beanFileName == null ? "" : beanFileName);
        LOGGER.debug(LogId.DAL025020, str);
        return str.toString();
    }

    /**
     * <h6>バッチクラス実行.</h6>
     * @param blogicBeanName ビジネスロジックBean名
     * @param exceptionHandlerBeanName 例外ハンドラBean名
     * @param param ビジネスロジックパラメータ
     * @param context コンテキスト
     * @return 実行ステータス
     */
    protected BLogicResult executeBatchClass(String blogicBeanName,
            String exceptionHandlerBeanName, BLogicParam param,
            ApplicationContext context) {
        BLogicResult result = new BLogicResult();
        BLogic blogic = null;
        ExceptionHandler exceptionHandler = null;
        String findBlogicBeanName = null;

        // ApplicationContextが取得できなかった場合
        if (context == null) {
            LOGGER.error(LogId.EAL025008);
            return result;
        }

        // アノテーションを検索
        if (this.enableJobComponentAnnotation) {
            GenericBeanFactoryAccessorEx gbfa = new GenericBeanFactoryAccessorEx(
                    context);
            Map<String, Object> jobMap = gbfa
                    .getBeansWithAnnotation(JobComponent.class);
            if (param != null) {
                Set<Entry<String, Object>> jobEs = jobMap.entrySet();
                for (Entry<String, Object> jobEnt : jobEs) {
                    Object jobObj = jobEnt.getValue();
                    if (jobObj != null) {
                        JobComponent jobComp = jobObj.getClass().getAnnotation(
                                JobComponent.class);
                        if (jobComp != null
                                && param.getJobAppCd().equals(jobComp.jobId())) {
                            findBlogicBeanName = jobEnt.getKey();
                            break;
                        }
                    }
                }
            }
        }

        if (findBlogicBeanName == null) {
            // ビジネスロジックのBeanが存在するか確認
            if (context.containsBean(blogicBeanName)) {
                findBlogicBeanName = blogicBeanName;
            } else if (context.containsBean(Introspector
                    .decapitalize(blogicBeanName))) {
                findBlogicBeanName = Introspector.decapitalize(blogicBeanName);
            }
        }

        // ビジネスロジックのインスタンスをBean定義から取得
        if (findBlogicBeanName != null) {
            try {
                blogic = (BLogic) context.getBean(findBlogicBeanName,
                        BLogic.class);
            } catch (Throwable e) {
                LOGGER.error(LogId.EAL025009, blogicBeanName);
                return result;
            }
        }

        if (blogic == null) {
            LOGGER.error(LogId.EAL025009, blogicBeanName);
            return result;
        }

        // 例外ハンドラのBeanが存在するか確認
        String findExceptionHandlerBeanName = null;
        if (context.containsBean(exceptionHandlerBeanName)) {
            findExceptionHandlerBeanName = exceptionHandlerBeanName;
        } else if (context.containsBean(Introspector
                .decapitalize(exceptionHandlerBeanName))) {
            findExceptionHandlerBeanName = Introspector
                    .decapitalize(exceptionHandlerBeanName);
        }

        // 例外ハンドラのインスタンスをBean定義から取得
        if (findExceptionHandlerBeanName != null) {
            try {
                exceptionHandler = (ExceptionHandler) context.getBean(
                        findExceptionHandlerBeanName, ExceptionHandler.class);
            } catch (Throwable e) {
                LOGGER.trace(LogId.TAL025002, e, exceptionHandlerBeanName);
                // 例外ハンドラは見つからなくても続行
            }
        }

        // 業務個別例外ハンドラがない場合は共通例外ハンドラを取得する
        if (exceptionHandler == null) {
            // デフォルトの例外ハンドラのインスタンスをBean定義から取得
            if (context.containsBean(getDefaultExceptionHandlerBeanName())) {
                try {
                    exceptionHandler = (ExceptionHandler) context.getBean(
                            getDefaultExceptionHandlerBeanName(),
                            ExceptionHandler.class);
                } catch (Throwable e) {
                    LOGGER.trace(LogId.TAL025002, e, exceptionHandlerBeanName);
                    // 例外ハンドラは見つからなくても続行
                }
            }
        }

        try {
            // BLogic#executeメソッド実行
            int blogicStatus = blogic.execute(param);

            result.setBlogicStatus(blogicStatus);

        } catch (Throwable e) {
            // 例外ハンドラが存在する場合
            if (exceptionHandler != null) {
                // 例外処理を行う
                result.setBlogicStatus(exceptionHandler
                        .handleThrowableException(e));
            }
        }

        return result;
    }

    /**
     * <h6>引数フィールド(1～20)コピー.</h6>
     * @param from コピー元インスタンス
     * @param to コピー先インスタンス
     * @param field フィールド名
     * @return コピーが成功したらtrue
     */
    protected boolean argumentCopy(Object from, Object to, String field) {
        for (int i = 1; i <= ARGUMENT_COUNT; i++) {
            // getterメソッド名生成
            StringBuilder getterName = new StringBuilder();
            getterName.append("get");
            getterName.append(field);
            getterName.append(i);

            // getter実行
            String argument = (String) getMethod(from, getterName.toString());

            if (argument != null) {
                // setterメソッド名生成
                StringBuilder setterName = new StringBuilder();
                setterName.append("set");
                setterName.append(field);
                setterName.append(i);

                // setter実行
                boolean ret = setMethod(to, setterName.toString(), argument);
                if (!ret) {
                    // 異常終了
                    return ret;
                }
            }
        }
        return true;
    }

    /**
     * <h6>パラメータ設定.</h6>
     * @param obj 対象インスタンス
     * @param methodName メソッド名
     * @param argument 引数
     * @return メソッドが実行できればtrue
     */
    protected boolean setMethod(Object obj, String methodName, String argument) {

        if (obj == null) {
            LOGGER.error(LogId.EAL025010);
            return false;
        }

        try {
            // Beanにパラメータ設定
            Method method = obj.getClass().getMethod(methodName,
                    new Class[] { String.class });
            method.invoke(obj, new Object[] { argument });
        } catch (SecurityException e) {
            LOGGER.error(LogId.EAL025011, e);
            return false;
        } catch (NoSuchMethodException e) {
            LOGGER.error(LogId.EAL025011, e);
            return false;
        } catch (IllegalArgumentException e) {
            LOGGER.error(LogId.EAL025011, e);
            return false;
        } catch (InvocationTargetException e) {
            LOGGER.error(LogId.EAL025011, e);
            return false;
        } catch (IllegalAccessException e) {
            LOGGER.error(LogId.EAL025011, e);
            return false;
        }

        return true;
    }

    /**
     * <h6>パラメータ設定.</h6>
     * @param obj 対象インスタンス
     * @param methodName メソッド名
     * @return パラメータが設定されたオブジェクト
     */
    protected Object getMethod(Object obj, String methodName) {
        Method method = null;
        Object result = null;

        if (obj == null) {
            LOGGER.error(LogId.EAL025010);
            return null;
        }

        try {
            // Beanからパラメータ取得
            method = obj.getClass().getMethod(methodName, new Class[] {});
            result = method.invoke(obj, new Object[] {});
        } catch (SecurityException e) {
            LOGGER.error(LogId.EAL025012, e);
            return null;
        } catch (NoSuchMethodException e) {
            LOGGER.error(LogId.EAL025012, e);
            return null;
        } catch (IllegalArgumentException e) {
            LOGGER.error(LogId.EAL025012, e);
            return null;
        } catch (IllegalAccessException e) {
            LOGGER.error(LogId.EAL025012, e);
            return null;
        } catch (InvocationTargetException e) {
            LOGGER.error(LogId.EAL025012, e);
            return null;
        }
        return result;
    }

    /**
     * <h6>アプリケーションコンテキスト取得.</h6>
     * @param batchBeanFileName Bean定義ファイル名
     * @return アプリケーションコンテキスト
     */
    protected static ApplicationContext getApplicationContext(
            String... batchBeanFileName) {
        ApplicationContext ctx = null;
        Class<?> clazz = null;
        Constructor<?> constructor = null;
        try {
            // クラス読み込み
            clazz = cl.loadClass(APPLICATION_CONTEXT);
        } catch (ClassNotFoundException e) {
            LOGGER.error(LogId.EAL025013, e);
            return null;
        }
        try {
            // コンストラクタ取得
            Class<?>[] arrClass = new Class[] { String[].class };
            constructor = clazz.getConstructor(arrClass);
        } catch (SecurityException e) {
            LOGGER.error(LogId.EAL025014, e);
            return null;
        } catch (NoSuchMethodException e) {
            LOGGER.error(LogId.EAL025015, e);
            return null;
        }
        try {
            Object[] array = new Object[] { (Object[]) batchBeanFileName };
            // コンテキストのインスタンス生成
            ctx = (ApplicationContext) constructor.newInstance(array);
            return ctx;
        } catch (IllegalArgumentException e) {
            // 何もしない
            LOGGER.warn(LogId.WAL025002, e);
        } catch (InstantiationException e) {
            // 何もしない
            LOGGER.warn(LogId.WAL025002, e);
        } catch (IllegalAccessException e) {
            // 何もしない
            LOGGER.warn(LogId.WAL025002, e);
        } catch (InvocationTargetException e) {
            // 何もしない
            LOGGER.warn(LogId.WAL025002, e);
        } catch (RuntimeException e) {
            // 何もしない
            LOGGER.warn(LogId.WAL025002, e);
        }

        return ctx;
    }

    /**
     * <h6>Bean定義ファイル名取得.</h6>
     * @param jobAppCd ジョブアプリケーションコード
     * @param jobRecord BatchJobData
     * @return Bean定義ファイル名
     */
    protected String getBeanFileName(String jobAppCd, BatchJobData jobRecord) {
        StringBuilder str = new StringBuilder();

        String classpath = PropertyUtil
                .getProperty(BEAN_DEFINITION_BUSINESS_CLASSPATH_KEY);

        // 置換文字列を置換する
        classpath = replaceString(classpath, jobAppCd, jobRecord);

        str.append(classpath == null ? "" : classpath);
        str.append(jobAppCd == null ? "" : jobAppCd);
        str.append(PROPERTY_BEAN_FILENAME_SUFFIX);

        return str.toString();
    }

    /**
     * 置換文字列を置換する
     * @param value 入力文字列
     * @param jobAppCd ジョブアプリケーションコード
     * @param jobRecord BatchJobData
     * @return 結果文字列
     */
    protected String replaceString(String value, String jobAppCd,
            BatchJobData jobRecord) {
        String result = value;

        if (result != null && jobAppCd != null && result.length() != 0
                && jobAppCd.length() != 0) {
            Map<String, String> kv = new HashMap<String, String>();
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();

            sb.setLength(0);
            sb.append(REPLACE_STRING_PREFIX);
            sb.append(REPLACE_STRING_JOB_APP_CD);
            sb.append(REPLACE_STRING_SUFFIX);
            kv.put(sb.toString(), jobAppCd);

            sb.setLength(0);
            sb.append(REPLACE_STRING_PREFIX);
            sb.append(REPLACE_STRING_JOB_APP_CD_UPPER);
            sb.append(REPLACE_STRING_SUFFIX);
            kv.put(sb.toString(), jobAppCd.toUpperCase());

            sb.setLength(0);
            sb.append(REPLACE_STRING_PREFIX);
            sb.append(REPLACE_STRING_JOB_APP_CD_LOWER);
            sb.append(REPLACE_STRING_SUFFIX);
            kv.put(sb.toString(), jobAppCd.toLowerCase());

            for (int i = 1; i <= REPLACE_STRING_JOB_ARG_MAX; i++) {
                sb.setLength(0);
                sb.append(REPLACE_STRING_PREFIX);
                sb.append(REPLACE_STRING_JOB_ARG);
                sb.append(i);
                sb.append(REPLACE_STRING_SUFFIX);
                sb2.setLength(0);
                sb2.append("get");
                sb2.append(FIELD_JOB_ARG);
                sb2.append(i);
                kv.put(sb.toString(), (String) getMethod(jobRecord, sb2
                        .toString()));
            }

            for (Entry<String, String> et : kv.entrySet()) {
                result = result.replaceAll(et.getKey(), et.getValue());
            }
        }

        return result;
    }

    /**
     * <h6>実行するBLogicのBean名を取得する.</h6>
     * @param jobAppCd ジョブアプリケーションコード
     * @return BLogicのBean名
     */
    protected String getBlogicBeanName(String jobAppCd) {
        StringBuilder str = new StringBuilder();

        if (jobAppCd != null && jobAppCd.length() != 0) {
            str.append(jobAppCd);
            str.append(DEFAULT_BLOGIC_BEAN_NAME_SUFFIX);
        }

        return str.toString();
    }

    /**
     * <h6>例外ハンドラのBean名を取得する.</h6>
     * @param jobAppCd ジョブアプリケーションコード
     * @return 例外ハンドラのBean名
     */
    protected String getExceptionHandlerBeanName(String jobAppCd) {
        StringBuilder str = new StringBuilder();

        if (jobAppCd != null && jobAppCd.length() != 0) {
            str.append(jobAppCd);
            str.append(DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME_SUFFIX);
        }

        return str.toString();
    }

    /**
     * <h6>デフォルトの例外ハンドラのBean名を取得する.</h6>
     * @return 例外ハンドラのBean名
     */
    protected String getDefaultExceptionHandlerBeanName() {
        return DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME;
    }

    /**
     * システム用アプリケーションコンテキスト取得.
     * @return the defaultApplicationContext
     */
    protected ApplicationContext getDefaultApplicationContext() {
        return defaultApplicationContext;
    }

    /**
     * システム用DAO定義（ステータス参照・更新用）
     * @return the queryDao
     */
    public SystemDao getSystemDao() {
        return systemDao;
    }

    /**
     * システム用transactionManager定義（ステータス参照・更新用）
     * @return the sysTransactionManager
     */
    public PlatformTransactionManager getSysTransactionManager() {
        return sysTransactionManager;
    }

}
