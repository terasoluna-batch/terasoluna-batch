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

package jp.terasoluna.fw.batch.blogic;

import java.sql.SQLException;
import java.util.Map;

import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.exception.BatchException;
import jp.terasoluna.fw.batch.util.BatchUtil;
import jp.terasoluna.fw.logger.TLogger;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

/**
 * トランザクション管理を行うビジネスロジック抽象クラス。<br>
 * <br>
 * フレームワーク側でトランザクション管理を行いたい場合、この抽象クラスを継承し、AbstractTransactionBLogic#doMainメソッドを実装してビジネスロジックが作成する。<br>
 * この抽象クラスを継承したビジネスロジックのトランザクションの振舞いは以下の通りである。
 * <ol>
 * <li>ビジネスロジック開始された時、トランザクションが開始される。</li>
 * <li>実行例外がスローされた時、トランザクション開始時までロールバックされる。</li>
 * <li>ビジネスロジック終了後、コミットされ、トランザクションが終了される。</li>
 * </ol>
 * @see jp.terasoluna.fw.batch.blogic.BLogic
 * @see jp.terasoluna.fw.batch.blogic.AbstractBLogic
 */
public abstract class AbstractTransactionBLogic extends AbstractBLogic {

    /**
     * プロセス終了コード（異常）
     */
    private static final int PROCESS_END_STATUS_FAILURE = 255;

    /**
     * ログ.
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(AbstractTransactionBLogic.class);

    /**
     * transactionManagerMap.
     */
    private Map<?, ?> transactionManagerMap = null;

    /**
     * transactionStatusMap
     */
    private Map<String, TransactionStatus> transactionStatusMap = null;

    /**
     * バッチ処理実行メソッド.
     * @see jp.terasoluna.fw.batch.blogic.BLogic#execute(java.lang.String[], com.ibatis.sqlmap.client.SqlMapClient)
     */
    public int execute(BLogicParam param) {
        int status = PROCESS_END_STATUS_FAILURE;
        ApplicationContext ctx = getApplicationContext();

        this.transactionManagerMap = ctx
                .getBeansOfType(PlatformTransactionManager.class);

        // トランザクション開始
        this.transactionStatusMap = startTransactions(this.transactionManagerMap);

        try {
            // 主処理
            status = doMain(param);

            // トランザクションコミット
            commitTransactions(this.transactionManagerMap,
                    this.transactionStatusMap);
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new BatchException(e);
        } finally {
            // トランザクション終了（未コミット時ロールバック）
            boolean et = endTransactions(this.transactionManagerMap,
                    this.transactionStatusMap);
            if (!et) {
                LOGGER.error(LogId.EAL025001);
            }
        }

        return status;
    }

    /**
     * 主処理.
     * @param param
     * @return
     * @throws SQLException
     */
    public abstract int doMain(BLogicParam param);

    /**
     * トランザクション開始.
     * @param trnMngMap PlatformTransactionManagerマップ
     * @return TransactionStatusマップ
     */
    private Map<String, TransactionStatus> startTransactions(Map<?, ?> trnMngMap) {
        return BatchUtil.startTransactions(
                BatchUtil.getTransactionDefinition(), trnMngMap, LOGGER);
    }

    /**
     * トランザクションコミット.
     * @param trnMngMap PlatformTransactionManagerマップ
     * @param tranStatMap TransactionStatusマップ
     */
    private void commitTransactions(Map<?, ?> trnMngMap,
            Map<String, TransactionStatus> tranStatMap) {
        BatchUtil.commitTransactions(trnMngMap, tranStatMap, LOGGER);
    }

    /**
     * トランザクション終了（未コミット時ロールバック）.
     * @param trnMngMap PlatformTransactionManagerマップ
     * @param tranStatMap TransactionStatusマップ
     * @return 正常ならtrue
     */
    private boolean endTransactions(Map<?, ?> trnMngMap,
            Map<String, TransactionStatus> tranStatMap) {
        return BatchUtil.endTransactions(trnMngMap, tranStatMap, LOGGER);
    }
}
