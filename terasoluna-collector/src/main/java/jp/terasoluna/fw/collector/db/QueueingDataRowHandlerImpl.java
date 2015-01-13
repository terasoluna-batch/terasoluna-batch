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
 * QueueingDataRowHandlerの実装クラス<br>
 */
public class QueueingDataRowHandlerImpl implements QueueingDataRowHandler {

    /**
     * Log.
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(QueueingDataRowHandlerImpl.class);

    /** 冗長ログ出力フラグ. */
    protected static AtomicBoolean verboseLog = new AtomicBoolean(false);

    /**
     * 前回handleResultメソッドに渡されたオブジェクト
     */
    protected Object prevRow = null;

    /** DBCollector */
    protected DBCollector<?> dbCollector = null;

    /** データカウント */
    protected AtomicLong dataCount = new AtomicLong(0);

    /*
     * (non-Javadoc)
     * @see org.apache.ibatis.session.ResultHandler#handleResult(org.apache.ibatis.session.ResultContext)
     */
    public void handleResult(ResultContext context) {
        if (!Thread.currentThread().isInterrupted()) {
            delayCollect();
            this.prevRow = context.getResultObject();
        } else {
            // 割り込みが発生したらキューをスキップする
            if (verboseLog.get()) {
                LOGGER.trace(LogId.TAL041003);
            }
            // 割り込みが発生したら行フェッチを中断する。
            throw new InterruptedRuntimeException();
        }
    }

    /**
     * 前回handleResultメソッドに渡された<code>Row</code>データをキューに格納する。
     */
    public void delayCollect() {
        if (this.prevRow != null) {
            if (!Thread.currentThread().isInterrupted()) {
                long dtcnt = this.dataCount.incrementAndGet();
                try {
                    if (this.dbCollector != null) {
                        // 取得したオブジェクトを1件キューにつめる
                        this.dbCollector.addQueue(new DataValueObject(
                                this.prevRow, dtcnt));
                    }
                } catch (InterruptedException e) {
                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace(LogId.TAL041002, Thread.currentThread()
                                .getName());
                    }
                    // 呼出し元の例外キャッチ時にキュー溢れでブロックされないよう、本スレッドを「割り込み」状態とする。
                    Thread.currentThread().interrupt();
                    throw new InterruptedRuntimeException(e);
                }
            } else {
                // 割り込みが発生したらキューをスキップする
                if (verboseLog.get()) {
                    LOGGER.trace(LogId.TAL041003);
                }
                // 割り込みが発生したら行フェッチを中断する。
                throw new InterruptedRuntimeException();
            }
        }
    }

    /**
     * DBCollectorを設定する。<br>
     * @param dbCollector DBCollector&lt;?&gt;
     */
    public void setDbCollector(DBCollector<?> dbCollector) {
        this.dbCollector = dbCollector;
    }

    /**
     * 冗長ログ出力フラグを設定する。
     * @param verbose 冗長ログ出力フラグ
     */
    public static void setVerbose(boolean verbose) {
        verboseLog.set(verbose);
    }
}
