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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionException;

/**
 * コネクションのリトライを行なうインターセプター<br>
 * フレームワークによりデータベースに接続する際、retryInterval(デフォルト20000ミリ秒)の間待機しながら、最大maxRetryCount(デフォルト0回)回リトライを行なう。
 * なお、リトライ処理時間がretryReset(デフォルト600000ミリ秒)経過した場合、リトライ回数はリセットされる。<br />
 * 本機能を利用するにはBean定義が必要となる。
 * 以下はBean定義に記述される{@code BatchStatusChanger}、{@code BatchJobDataResolver}の公開メソッドに対して{@code AdminConnectionRetryInterceptor}によるコネクションリトライを行うための設定例である。
 * <code><pre>
 *  &lt;bean id=&quot;adminConnectionRetryInterceptor&quot; class=&quot;jp.terasoluna.fw.batch.executor.AdminConnectionRetryInterceptor&quot; /&gt;
 *  &lt;aop:config&gt;
 *      &lt;aop:pointcut id=&quot;adminConnectionRetryPointcut&quot;
 *        expression=&quot;execution(* jp.terasoluna.fw.batch.executor.repository.JobStatusChanger.*(..)) ||
 *                    execution(* jp.terasoluna.fw.batch.executor.repository.JobControlFinder.*(..))&quot;/&gt;
 *      &lt;aop:advisor advice-ref=&quot;adminConnectionRetryInterceptor&quot; pointcut-ref=&quot;adminConnectionRetryPointcut&quot;/&gt;
 *  &lt;/aop:config&gt;
 * </pre></code>
 * 以下はリトライ対象となる例外である。
 * <ol>
 * <li>org.springframework.dao.DataAccessException</li>
 * <li>org.springframework.transaction.TransactionException</li>
 * </ol>
 * リトライ正常終了時は例外をスローすることなく処理を終了する(リトライを示すINFOログは出力される)。リトライ回数を超えたときは、最後に発生した例外をスローする。
 * なお、retryResetを短めに設定すると(たとえば、retryReset > retryIntervalのような場合)、リトライ回数がリセットされるため例外がスローされる間無限ループになりうる点には注意が必要である。
 * @see org.springframework.dao.DataAccessException
 * @see org.springframework.transaction.TransactionException
 * @since 3.6
 */
public class AdminConnectionRetryInterceptor implements MethodInterceptor {

    private static final TLogger LOGGER = TLogger
            .getLogger(AdminConnectionRetryInterceptor.class);

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
     * リトライ回数をリセットする、前回からの発生間隔(ミリ秒)
     */
    @Value("${batchTaskExecutor.dbAbnormalRetryReset:600000}")
    private volatile long retryReset;

    /**
     * 
     * 対象となる例外が発生したときにデータベース接続のリトライを実施する
     * リトライは、retryIntervalの間待機したのちに、最大maxRetryCount回行なう。
     * 前回のリトライからretryResetミリ秒経過している場合は、リトライ実施回数カウンタをリセットする。
     * 
     * @param invocation 処理対象となるメソッド
     * @return メソッド実行結果
     * @throws Throwable リトライ処理から外部にスローされるThrowable
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        int retryCount = 0;
        Throwable cause = null;
        Object returnObject = null;
        long lastExceptionTime = System.currentTimeMillis();
        while (true) {
            try {
                cause = null;
                returnObject = invocation.proceed();
                break;
            } catch (DataAccessException | TransactionException e) {
                if (System.currentTimeMillis() - lastExceptionTime > retryReset) {
                    retryCount = 0;
                }
                lastExceptionTime = System.currentTimeMillis();

                cause = e;

                if (retryCount >= maxRetryCount) {
                    LOGGER.error(LogId.EAL025097, cause, maxRetryCount);
                    break;
                }

                TimeUnit.MILLISECONDS.sleep(retryInterval);
                retryCount++;
                LOGGER.info(LogId.IAL025017, retryCount, maxRetryCount,
                        retryReset, retryInterval);
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
