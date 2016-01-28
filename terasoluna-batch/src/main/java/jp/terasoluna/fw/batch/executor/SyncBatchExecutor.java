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

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.controller.JobOperator;
import jp.terasoluna.fw.logger.TLogger;
import org.springframework.context.ApplicationContext;

/**
 * 同期バッチエグゼキュータ。<br>
 * <p>
 * 指定のジョブ業務を実行する
 * </p>
 * @see JobOperator
 */
public class SyncBatchExecutor {

    private static final TLogger LOGGER = TLogger.getLogger(SyncBatchExecutor.class);

    /**
     * {@code JobOperator}の取得に失敗した場合のプロセス終了コード
     */
    public static final int FAIL_TO_OBTAIN_JOB_OPERATOR_CODE = 255;

    /**
     * {@code JobOperator}のBean名
     */
    private static final String JOB_OPERATOR_BEAN_NAME = "syncJobOperator";

    /**
     * メインメソッド.
     * @param args Java起動引数
     */
    public static void main(String[] args) {
        SyncBatchExecutor syncBatchExecutor = new SyncBatchExecutor();
        int status = syncBatchExecutor.doMain(args);
        System.exit(status);
    }

    /**
     * 同期バッチ起動主処理
     *
     * @param args Java起動引数
     * @return ステータスコード
     */
    int doMain(String[] args) {
        ApplicationContextResolver resolver = null;
        ApplicationContext context = null;
        try {
            LOGGER.info(LogId.IAL025014);
            resolver = findAdminContextResolver();
            context = resolver.resolveApplicationContext();
            JobOperator jobOperator = context.getBean(JOB_OPERATOR_BEAN_NAME,
                    JobOperator.class);
            int status = jobOperator.start(args);
            LOGGER.info(LogId.IAL025015, status);
            return status;
        } catch (Throwable t) {
            LOGGER.error(LogId.EAL025060, t);
            LOGGER.info(LogId.IAL025015, FAIL_TO_OBTAIN_JOB_OPERATOR_CODE);
            return FAIL_TO_OBTAIN_JOB_OPERATOR_CODE;
        } finally {
            if (context != null) {
                try {
                    resolver.closeApplicationContext(context);
                } catch (Exception e)  {
                    LOGGER.error(LogId.EAL025062, e);
                }
            }
        }
    }

    /**
     * {@code ApplicationContext}を解決するリゾルバを返却する。
     *
     * @return {@code ApplicationContext}リゾルバ
     */
    protected ApplicationContextResolver findAdminContextResolver() {
        return new ApplicationContextResolverImpl();
    }
}
