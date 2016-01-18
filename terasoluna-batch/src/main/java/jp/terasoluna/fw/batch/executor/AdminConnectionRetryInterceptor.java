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

import java.util.concurrent.TimeUnit;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.logger.TLogger;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionException;

/**
 * データベースアクセスで例外が発生した場合に、一定時間待ってからリトライするインターセプター実装。
 * <p>
 * 例外発生後にデータベースに再度アクセスする際は、"リトライ実施前の待機時間"を経過した後に行う。<br>
 * リトライ回数は、"最大リトライ"回数までとなる。<br>
 * 前回のリトライから"リトライ回数リセットまでの経過時間"後、リトライ回数実施カウンタをリセットする。<br>
 * これらの設定はプロパティファイルの以下の項目で指定する。
 * 
 * <table border>
 * <tr>
 * <th>プロパティ名</th>
 * <th>説明</th>
 * <th>デフォルト値</th>
 * </tr>
 * <tr>
 * <td>batchTaskExecutor.dbAbnormalRetryMax</td>
 * <td>最大リトライ回数</td>
 * <td>0(回)</td>
 * </tr>
 * <tr>
 * <td>batchTaskExecutor.dbAbnormalRetryInterval</td>
 * <td>リトライ実施前の待機時間</td>
 * <td>20000(ミリ秒)</td>
 * </tr>
 * <tr>
 * <td>batchTaskExecutor.dbAbnormalRetryReset</td>
 * <td>リトライ回数リセットまでの経過時間</td>
 * <td>600000(ミリ秒)</td>
 * </tr>
 * </table>
 * </p>
 * <p>
 * また、本機能を利用するにはBean定義が必要となる。<br>
 * 以下はBean定義に記述される{@code JobStatusChanger}、{@code JobControlFinder}のインタフェースで定義されたメソッドに対して
 * {@code AdminConnectionRetryInterceptor}によるコネクションリトライを行うための設定例である。
 * 
 * <pre>
 * {@code 
 * <bean id="adminConnectionRetryInterceptor"
 *     class="jp.terasoluna.fw.batch.executor.AdminConnectionRetryInterceptor" />
 * <aop:config>
 *     <aop:pointcut id="adminConnectionRetryPointcut"
 *         expression=" execution(* jp.terasoluna.fw.batch.executor.repository.JobStatusChanger.*(..))
 *                    || execution(* jp.terasoluna.fw.batch.executor.repository.JobControlFinder.*(..))" />
 *     <aop:advisor advice-ref="adminConnectionRetryInterceptor" pointcut-ref="adminConnectionRetryPointcut" />
 * </aop:config>
 * }
 * </pre>
 * </p>
 * <p>
 * 以下はリトライ対象となる例外である。
 * <ol>
 * <li>org.springframework.dao.DataAccessException</li>
 * <li>org.springframework.transaction.TransactionException</li>
 * </ol>
 * リトライによってデータベースアクセスが成功した場合は、例外をスローすることなく処理を終了する(リトライを示すINFOログは出力される)。 
 * リトライ回数を超えたときは、最後に発生した例外をスローする。
 * なお、"リトライ回数リセットまでの経過時間"を短めに設定すると(たとえば、"リトライ実施前の待機時間"以下のような設定をすると)、 
 * リトライ回数がすぐにリセットされてしまい例外がスローされる間スピンループしてしまう。このような設定は避けること。
 * </p>
 * 
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
     * 対象となる例外が発生したときにデータベース接続のリトライを実施する。
     * リトライは、"リトライ実施前の待機時間"の後に、最大リトライ回数を上限に行なう。
     * 前回のリトライから"リトライ回数リセットまでの経過時間"が経過している場合は、リトライ実施回数カウンタをリセットする。
     * 
     * @param invocation 処理対象となるメソッド
     * @return メソッド実行結果
     * @throws Throwable リトライ処理から外部にスローされるThrowable
     */
    @Override
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
                    LOGGER.error(LogId.EAL025063, cause, maxRetryCount);
                    break;
                }

                TimeUnit.MILLISECONDS.sleep(retryInterval);
                retryCount++;
                LOGGER.info(LogId.IAL025017, retryCount, maxRetryCount,
                        retryReset, retryInterval);
            }
        }
        if (cause != null) {
            throw cause;
        }
        return returnObject;
    }

}
