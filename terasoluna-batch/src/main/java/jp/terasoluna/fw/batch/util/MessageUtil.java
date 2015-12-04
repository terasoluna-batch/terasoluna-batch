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

package jp.terasoluna.fw.batch.util;

import java.util.concurrent.ConcurrentHashMap;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.message.MessageAccessor;
import jp.terasoluna.fw.logger.TLogger;

/**
 * メッセージ管理ユーティリティ。<br>
 * <br>
 * <p>
 * メッセージの管理を行いたい場合に利用する。<br>
 * 本クラスのクラス変数に保存されるMessageAccessorはスレッドグループ毎にインスタンスが保持される。
 * </p>
 * @deprecated バージョン3.6.0より、本クラスによるメッセージ管理機能は非推奨である。業務ロジック内でメッセージ管理機能を使用する場合、commonContext.xmlに{@code MessageAccessorImpl}を定義の上、業務ロジッククラスで{@code MessageAccessor}をインジェクションすること。
 * @see jp.terasoluna.fw.batch.message.MessageAccessor
 * @see jp.terasoluna.fw.batch.message.MessageAccessorImpl
 */
@Deprecated
public class MessageUtil {

    /** ロガー. */
    private static final TLogger LOGGER = TLogger.getLogger(MessageUtil.class);

    /** スレッドグループ毎にメッセージソースアクセサを保持する. */
    private static final ConcurrentHashMap<ThreadGroup, MessageAccessor> tgm = new ConcurrentHashMap<ThreadGroup, MessageAccessor>();

    /**
     * コンストラクタ.
     */
    protected MessageUtil() {
    }

    /**
     * コードに応じたメッセージを返却する<br>
     * @param errorCode コード
     * @return コードに応じたメッセージを返却する
     */
    public static String getMessage(String code) {
        return getMessage(code, null);
    }

    /**
     * コードに応じたメッセージを返却する<br>
     * @param errorCode コード
     * @return コードに応じたメッセージを返却する
     */
    public static String getMessage(String code, Object[] args) {
        String mes = null;
        MessageAccessor ma = null;

        ThreadGroup tg = getThreadGroup();
        if (tg != null) {
            ma = tgm.get(tg);

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(LogId.TAL025006, getThreadMessage());
            }

        }

        if (ma != null) {
            try {
                mes = ma.getMessage(code, args);
            } catch (Exception e) {
                // 何もしない
            }
        } else {
            LOGGER.debug(LogId.DAL025043);

        }

        // メッセージが見つからなかった場合
        if (mes == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Message not found. CODE:[");
            sb.append(code);
            sb.append("]");
            return sb.toString();
        }

        return mes;
    }

    /**
     * メッセージソースアクセサを設定する.<br>
     * <p>
     * ここで設定するMessageAccessorはスレッドグループ毎に保持される。
     * </p>
     * @param messageAccessor MessageAccessor
     */
    public static void setMessageAccessor(MessageAccessor messageAccessor) {
        if (messageAccessor == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(LogId.WAL025008, getThreadMessage());
            }

            return;
        }

        ThreadGroup tg = getThreadGroup();
        if (tg != null) {
            tgm.put(tg, messageAccessor);

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(LogId.TAL025007, getThreadMessage());
            }

        }
    }

    /**
     * メッセージソースアクセサを削除する.<br>
     */
    public static void removeMessageAccessor() {
        ThreadGroup tg = getThreadGroup();
        if (tg != null) {
            tgm.remove(tg);

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(LogId.TAL025008, getThreadMessage());
            }

        }
    }

    /**
     * スレッドグループを取得する.
     * @return ThreadGroup
     */
    private static ThreadGroup getThreadGroup() {
        Thread ct = Thread.currentThread();
        if (ct != null && ct.getThreadGroup() != null) {
            return ct.getThreadGroup();
        }
        return null;
    }

    /**
     * スレッドグループとスレッド名を返すメソッド.<br>
     * デバッグ用メッセージを返す。
     * @return String
     */
    private static String getThreadMessage() {
        StringBuilder sb = new StringBuilder();
        Thread ct = Thread.currentThread();

        if (ct != null && getThreadGroup() != null) {
            sb.append(" tg:[");
            sb.append(getThreadGroup().getName());
            sb.append("]");
        }
        if (ct != null) {
            sb.append(" t:[");
            sb.append(ct.getName());
            sb.append("]");
        }

        return sb.toString();
    }
}
