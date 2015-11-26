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
import jp.terasoluna.fw.batch.exception.IllegalClassTypeException;
import jp.terasoluna.fw.batch.executor.controller.JobOperator;

import jp.terasoluna.fw.logger.TLogger;
import jp.terasoluna.fw.util.PropertyUtil;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

/**
 * 非同期型ジョブ起動機能のエントリポイント。<br>
 * TERASOLUNAフレームワークの非同期DIコンテナを作成し、{@code JobOperator}による非同期ジョブ機能の起動を行う。
 * バージョンアップ時の注意：本クラスはバージョン3.6から{@code AbstractJobBatchExecutor}を継承しない。<br>
 * 本クラス、および{@code AbstractJobBatchExecutor}の独自拡張を行っている場合は以下参照先にある
 * 代替機能についてそれぞれ拡張を行うこと。
 *
 * @see AdminContextResolver
 * @see JobOperator
 * @see jp.terasoluna.fw.batch.executor.controller.AsyncJobLauncher
 * @see jp.terasoluna.fw.batch.executor.worker.JobExecutorTemplate
 */
public class AsyncBatchExecutor {

    /**
     * ログ.
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(AsyncBatchExecutor.class);

    /**
     * フレームワークの管理用ApplicationContextの解決を行うクラスのプロパティ名
     */
    private static final String ADMIN_CONTEXT_RESOLVER_NAME = "adminContextResolver.class";

    /**
     * {@code JobOperator}のBean名
     */
    private static final String JOB_OPERATOR_BEAN_NAME = "jobOperator";

    /**
     * {@code JobOperator}の取得に失敗した場合のプロセス終了コード
     */
    public static final int FAIL_TO_OBTAIN_JOB_OPERATOR_CODE = 99;

    /**
     * メインメソッド.<br>
     *
     * @param args コマンド起動時引数
     */
    public static void main(String[] args) {
        AsyncBatchExecutor asyncBatchExecutor = new AsyncBatchExecutor();
        int status = asyncBatchExecutor.doMain(args);
        System.exit(status);
    }

    /**
     * JobOperatorを起動し、この戻り値を終了ステータスとして返却する内部メインメソッド.
     *
     * @param args コマンド起動時引数
     * @return プロセス終了ステータスコード
     */
    int doMain(String[] args) {
        JobOperator jobOperator = null;
        try {
            jobOperator = findAdminContextResolver().resolveAdminContext()
                    .getBean(JOB_OPERATOR_BEAN_NAME, JobOperator.class);
        } catch (Exception e) {
            LOGGER.error(LogId.EAL025093, e);
            return FAIL_TO_OBTAIN_JOB_OPERATOR_CODE;
        }
        return jobOperator.start(args);
    }

    /**
     * フレームワーク管理用{@code ApplicationContext}を解決する
     * リゾルバクラスをロードし、このインスタンスを返却する。
     *
     * @return フレームワーク管理用{@code ApplicationContext}リゾルバ
     */
    AdminContextResolver findAdminContextResolver() {
        String adminContextResolverClassName = PropertyUtil
                .getProperty(ADMIN_CONTEXT_RESOLVER_NAME,
                        DefaultAdminContextResolver.class.getName());
        try {
            Class<?> resolverClass = ClassUtils
                    .forName(adminContextResolverClassName,
                            AsyncBatchExecutor.class.getClassLoader());
            return BeanUtils.instantiateClass(resolverClass,
                    AdminContextResolver.class);
        } catch (ClassNotFoundException | BeanInstantiationException e) {
            throw new IllegalClassTypeException(e);
        }
    }
}
