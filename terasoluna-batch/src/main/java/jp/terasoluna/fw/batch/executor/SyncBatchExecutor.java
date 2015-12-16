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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.logger.TLogger;

/**
 * 同期バッチエグゼキュータ。<br>
 * <p>
 * 指定のジョブ業務を実行する
 * </p>
 * @see jp.terasoluna.fw.batch.executor.AbstractBatchExecutor
 */
public class SyncBatchExecutor extends AbstractBatchExecutor {

    /**
     * ロガー.
     */
    private static final TLogger LOGGER  = TLogger.getLogger(SyncBatchExecutor.class);

    /**
     * 引数パラメータの基本部分
     */
    private static final String JOB_ARG_PARAM_BASE = "JobArgNm";

    /**
     * コンストラクタ
     */
    protected SyncBatchExecutor() {
        super();
    }

    /**
     * メインメソッド.
     * @param args
     */
    public static void main(String[] args) {
        BLogicResult result = new BLogicResult();
        String jobAppCd = null;

        LOGGER.info(LogId.IAL025014);

        // ジョブレコードデータ
        BatchJobData jobRecord = new BatchJobData();

        // 第１引数からジョブシーケンスコードを取得
        if (args.length > 0) {
            jobAppCd = args[0];
        }

        // 第２引数から第２１引数まで、ジョブへの引数を取得
        for (int i = 1; i < args.length && i <= ENV_JOB_ARG_MAX; i++) {
            String arg = args[i];
            if (arg != null && arg.length() != 0) {
                setParam(jobRecord, JOB_ARG_PARAM_BASE, i, arg);
            }
        }

        // 引数に「ジョブ業務コード」が指定されていなければ、環境変数から取得する
        if (jobAppCd == null || jobAppCd.length() == 0) {
            jobAppCd = getenv(ENV_JOB_APP_CD);
        }
        // ジョブ業務コード
        jobRecord.setJobAppCd(jobAppCd);

        // 引数に「引数1」～「引数20」が指定されていなければ、環境変数から取得する
        StringBuilder envName = new StringBuilder();
        for (int i = 1; i <= ENV_JOB_ARG_MAX; i++) {
            String param = getParam(jobRecord, JOB_ARG_PARAM_BASE, i);

            if (param == null || param.length() == 0) {
                envName.setLength(0);
                envName.append(ENV_JOB_ARG_NM);
                envName.append(i);

                param = getenv(envName.toString());

                if (param != null && param.length() != 0) {
                    setParam(jobRecord, JOB_ARG_PARAM_BASE, i, param);
                }
            }
        }

        // ジョブシーケンスコード
        jobRecord.setJobSequenceId(getenv(ENV_JOB_SEQ_ID));
        // 業務ステータス
        jobRecord.setErrAppStatus(getenv(ENV_BLOGIC_APP_STATUS));
        // ステータス
        jobRecord.setCurAppStatus(getenv(ENV_CUR_APP_STATUS));

        // バッチ処理実行
        SyncBatchExecutor executor = new SyncBatchExecutor();
        result = executor.executeBatch(jobRecord);

        if(LOGGER.isInfoEnabled()){
            LOGGER.info(LogId.IAL025015,result.getBlogicStatus());
        }

        System.exit(result.getBlogicStatus());
        return;
    }

    /**
     * パラメータを取得する
     * @param obj
     * @param paramName
     * @param i
     * @return
     */
    private static String getParam(Object obj, String paramName, int i) {
        String result = null;

        if (obj != null) {
            Method method = null;
            StringBuilder methodName = new StringBuilder();
            methodName.append("get");
            methodName.append(paramName);
            methodName.append(i);

            try {
                method = obj.getClass().getMethod(methodName.toString());
            } catch (SecurityException e) {
                LOGGER.error(LogId.EAL025014, e);
                return null;
            } catch (NoSuchMethodException e) {
                LOGGER.error(LogId.EAL025015, e);
                return null;
            }

            if (method != null) {
                Object resultObj = null;
                try {
                    resultObj = method.invoke(obj);
                } catch (IllegalArgumentException e) {
                    LOGGER.error(LogId.EAL025032, e);
                    return null;
                } catch (IllegalAccessException e) {
                    LOGGER.error(LogId.EAL025033, e);
                    return null;
                } catch (InvocationTargetException e) {
                    LOGGER.error(LogId.EAL025034, e);
                    return null;
                }

                if (resultObj instanceof String) {
                    result = (String) resultObj;
                }
            }
        }
        return result;
    }

    /**
     * パラメータ設定
     * @param obj 対象オブジェクト
     * @param paramName パラメータ名
     * @param i
     * @param value 設定する値
     */
    private static void setParam(Object obj, String paramName, int i,
            String value) {
        if (obj != null) {
            Method method = null;
            StringBuilder methodName = new StringBuilder();
            methodName.append("set");
            methodName.append(paramName);
            methodName.append(i);
            try {
                method = obj.getClass().getMethod(methodName.toString(),
                        String.class);
            } catch (SecurityException e) {
                LOGGER.error(LogId.EAL025014, e);
                return;
            } catch (NoSuchMethodException e) {
                LOGGER.error(LogId.EAL025015, e);
                return;
            }

            if (method != null) {
                try {
                    method.invoke(obj, value);
                } catch (IllegalArgumentException e) {
                    LOGGER.error(LogId.EAL025032, e);
                    return;
                } catch (IllegalAccessException e) {
                    LOGGER.error(LogId.EAL025033, e);
                    return;
                } catch (InvocationTargetException e) {
                    LOGGER.error(LogId.EAL025034, e);
                    return;
                }
            }
        }
    }

    /**
     * <h6>指定された環境変数の値を取得する.</h6>
     * <p>
     * システム環境で変数を定義しない場合は ""（空文字） を返す
     * </p>
     * @param name 環境変数名
     * @return 環境変数の値
     */
    static String getenv(String name) {
        String ret = System.getenv(name);
        if (ret == null) {
            return "";
        }
        return ret;
    }
}
