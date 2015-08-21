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

package jp.terasoluna.fw.batch.executor;

import java.util.concurrent.TimeUnit;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.util.BatchUtil;
import jp.terasoluna.fw.logger.TLogger;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.transaction.TransactionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;

/**
 * コネクションのリトライを行なうインターセプター<br>
 * @since 3.6
 */
public class ConnectionRetryInterceptor implements MethodInterceptor {

    private static final TLogger LOGGER = TLogger
            .getLogger(ConnectionRetryInterceptor.class);

    /**
     * 最大リトライ回数
     */
    @Value("${batchTaskExecutor.dbAbnormalRetryMax:0}")
    private volatile long maxRetryCount;

    /**
     * データベース異常時のリトライ間隔（ミリ秒）
     */
    @Value("${batchTaskExecutor.dbAbnormalRetryInterval:20000}")
    private volatile long retryInterval;

    /**
     * リトライ回数をリセットする、前回からの発生間隔のデフォルト値
     */
    @Value("${batchTaskExecutor.dbAbnormalRetryReset:600000}")
    private volatile long retryReset;

    /**
     * @{inheritDoc
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        int retryCount = 0;
        Exception cause = null;
        Object returnObject = null;
        long lastExceptionTime = System.currentTimeMillis();
        while (true) {
            try {
                cause = null;
                returnObject = invocation.proceed();
                break;
            } catch (DataAccessException | TransactionException | RetryableExecuteException e) {
                if (System.currentTimeMillis() - lastExceptionTime > retryReset) {
                    // 互換性を考慮し、ログは出力しない。
                    retryCount = 0;
                }
                lastExceptionTime = System.currentTimeMillis();

                if (e instanceof RetryableExecuteException) {
                    // リトライオーバー時のスロー対象にする。
                    // 互換性を考慮し、ログは出力しない。
                    cause = ((RetryableExecuteException) e.getCause());
                } else {
                    cause = e;
                }

                if (retryCount >= maxRetryCount) {
                    LOGGER.error(LogId.EAL025031, cause);
                    break;
                }

                TimeUnit.MILLISECONDS.sleep(retryInterval);
                retryCount++;
                LOGGER.info(LogId.IAL025017, retryCount, maxRetryCount,
                        retryReset, retryInterval);
                // 互換性を考慮し、トレースログはそのまま出力する。
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(LogId.TAL025010, BatchUtil.getMemoryInfo());
                }
            }
        }
        if (cause != null) {
            throw cause;
        }
        return returnObject;
    }

}
