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

package jp.terasoluna.fw.collector.db;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import jp.terasoluna.fw.collector.LogId;
import jp.terasoluna.fw.collector.vo.DataValueObject;
import jp.terasoluna.fw.logger.TLogger;
import org.apache.ibatis.session.ResultContext;

/**
 * QueueingResultHandlerの実装クラス<br>
 */
public class QueueingResultHandlerImpl implements QueueingResultHandler {

    /**
     * Log.
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(QueueingResultHandlerImpl.class);

    /** 冗長ログ出力フラグ. */
    protected static AtomicBoolean verboseLog = new AtomicBoolean(false);

    /**
     * 前回handleResultメソッドに渡されたオブジェクト
     */
    protected Object prevRow = null;

    /** DaoCollector */
    protected DaoCollector<?> daoCollector = null;

    /** データカウント */
    protected AtomicLong dataCount = new AtomicLong(0);

    /*
     * (non-Javadoc)
     * @see org.apache.ibatis.session.ResultHandler#handleResult(org.apache.ibatis.session.ResultContext)
     */
    public void handleResult(ResultContext context) {
        delayCollect();
        if (Thread.currentThread().isInterrupted()) {
            // 割り込みが発生したらキューをスキップする
            if (verboseLog.get()) {
                LOGGER.trace(LogId.TAL041003);
            }
            context.stop();
            return;
        }
        this.prevRow = context.getResultObject();
    }

    /**
     * 前回handleResultメソッドに渡された<code>Row</code>データをキューに格納する。
     */
    public void delayCollect() {
        if (this.prevRow == null) {
            return;
        }
        if (Thread.currentThread().isInterrupted()) {
            return;
        }
        try {
            if (this.daoCollector != null) {
                // 取得したオブジェクトを1件キューにつめる
                this.daoCollector.addQueue(new DataValueObject(
                        this.prevRow, this.dataCount.incrementAndGet()));
            }
        } catch (InterruptedException e) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(LogId.TAL041002, Thread.currentThread()
                        .getName());
            }
            // 呼出し元の例外キャッチ時にキュー溢れでブロックされないよう、本スレッドを「割り込み」状態とする。
            Thread.currentThread().interrupt();
        }
    }

    /**
     * DaoCollectorを設定する。<br>
     * @param daoCollector DaoCollector&lt;?&gt;
     */
    public void setDaoCollector(DaoCollector<?> daoCollector) {
        this.daoCollector = daoCollector;
    }

    /**
     * 冗長ログ出力フラグを設定する。
     * @param verbose 冗長ログ出力フラグ
     */
    public static void setVerbose(boolean verbose) {
        verboseLog.set(verbose);
    }
}
