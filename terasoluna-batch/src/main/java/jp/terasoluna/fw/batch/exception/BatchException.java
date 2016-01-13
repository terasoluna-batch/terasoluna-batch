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

package jp.terasoluna.fw.batch.exception;

import jp.terasoluna.fw.batch.util.BatchUtil;
import jp.terasoluna.fw.batch.util.MessageUtil;

/**
 * バッチ例外。<br>
 * <br>
 * バッチ実行時に発生した例外情報を保持する。
 */
@SuppressWarnings("deprecation")
public class BatchException extends RuntimeException {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 7677068837918514733L;

    /**
     * メッセージID
     */
    @Deprecated
    private String messageId = null;

    /**
     * 例外情報特定のためのパラメータ
     */
    @Deprecated
    private Object[] params = null;

    /**
     * BatchExceptionを生成する
     */
    public BatchException() {
        super();
    }

    /**
     * BatchExceptionを生成する
     * @param message 例外メッセージ
     */
    public BatchException(String message) {
        super(message);
    }

    /**
     * BatchExceptionを生成する
     * @param message 例外メッセージ
     * @param cause 原因例外
     */
    public BatchException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * BatchExceptionを生成する
     * @param cause 原因例外
     */
    public BatchException(Throwable cause) {
        super(cause);
    }

    /**
     * BatchExceptionを生成する
     * @param messageId エラーコード
     * @param message エラーメッセージ
     * @deprecated 例外生成時にメッセージIDを指定する本メソッドをバージョン3.6から非推奨とする。
     *   コンストラクタ引数でエラーメッセージを直接指定すること。{@link #BatchException(String)}
     */
    @Deprecated
    public BatchException(String messageId, String message) {
        super(message);

        this.messageId = messageId;
    }

    /**
     * BatchExceptionを生成する
     * @param messageId メッセージID
     * @param message エラーメッセージ
     * @param cause 原因となった例外
     * @deprecated 例外生成時にメッセージIDを指定する本メソッドをバージョン3.6から非推奨とする。
     *   コンストラクタ引数でエラーメッセージを直接指定すること。{@link #BatchException(String, Throwable)}
     */
    @Deprecated
    public BatchException(String messageId, String message, Throwable cause) {
        super(message, cause);

        this.messageId = messageId;
    }

    /**
     * BatchExceptionを生成する
     * @param messageId メッセージID
     * @param message エラーメッセージ
     * @param params 例外情報特定のためのパラメータ
     * @deprecated 例外生成時にメッセージIDを指定する本メソッドをバージョン3.6から非推奨とする。
     *   コンストラクタ引数でエラーメッセージを直接指定すること。{@link #BatchException(String)}
     */
    @Deprecated
    public BatchException(String messageId, String message, Object... params) {
        super(message);

        this.messageId = messageId;
        this.params = params;
    }

    /**
     * BatchExceptionを生成する
     * @param messageId メッセージID
     * @param message エラーメッセージ
     * @param cause 原因となった例外
     * @param params 例外情報特定のためのパラメータ
     * @deprecated 例外生成時にメッセージIDを指定する本メソッドをバージョン3.6から非推奨とする。
     *   コンストラクタ引数でエラーメッセージを直接指定すること。{@link #BatchException(String, Throwable)}
     */
    @Deprecated
    public BatchException(String messageId, String message, Throwable cause,
            Object... params) {
        super(message, cause);

        this.messageId = messageId;
        this.params = params;
    }

    /**
     * BatchExceptionのファクトリメソッド
     * @param messageId メッセージID
     * @return 引数の内容で作成されたBatchExceptionインスタンス
     * @deprecated 例外生成時にメッセージIDを指定する本メソッドをバージョン3.6から非推奨とする。
     *   コンストラクタ引数でエラーメッセージを直接指定すること。{@link #BatchException(String)}
     */
    @Deprecated
    public static BatchException createException(String messageId) {
        return new BatchException(messageId, MessageUtil.getMessage(messageId));
    }

    /**
     * BatchExceptionのファクトリメソッド
     * @param messageId メッセージID
     * @param params 例外情報特定のためのパラメータ
     * @return 引数の内容で作成されたBatchExceptionインスタンス
     * @deprecated 例外生成時にメッセージIDを指定する本メソッドをバージョン3.6から非推奨とする。
     *   コンストラクタ引数でエラーメッセージを直接指定すること。{@link #BatchException(String)}
     */
    @Deprecated
    public static BatchException createException(String messageId,
            Object... params) {
        return new BatchException(messageId, MessageUtil.getMessage(messageId,
                params), params);
    }

    /**
     * BatchExceptionのファクトリメソッド
     * @param messageId メッセージID
     * @param cause 原因となった例外
     * @return 引数の内容で作成されたBatchExceptionインスタンス
     * @deprecated 例外生成時にメッセージIDを指定する本メソッドをバージョン3.6から非推奨とする。
     *   コンストラクタ引数でエラーメッセージを直接指定すること。{@link #BatchException(String, Throwable)}
     */
    @Deprecated
    public static BatchException createException(String messageId,
            Throwable cause) {
        return new BatchException(messageId, MessageUtil.getMessage(messageId),
                cause);
    }

    /**
     * BatchExceptionのファクトリメソッド
     * @param messageId メッセージID
     * @param cause 原因となった例外
     * @param params 例外情報特定のためのパラメータ
     * @return 引数の内容で作成されたBatchExceptionインスタンス
     * @deprecated 例外生成時にメッセージIDを指定する本メソッドをバージョン3.6から非推奨とする。
     *   コンストラクタ引数でエラーメッセージを直接指定すること。{@link #BatchException(String, Throwable)}
     */
    @Deprecated
    public static BatchException createException(String messageId,
            Throwable cause, Object... params) {
        return new BatchException(messageId, MessageUtil.getMessage(messageId,
                params), cause, params);
    }

    /**
     * ログ出力用文字列作成
     * @return ログ出力用文字列
     * @deprecated 例外生成時にメッセージIDを指定するメソッドをバージョン3.6から非推奨とするため、関連する本メソッドも非推奨とする。
     */
    @Deprecated
    public String getLogMessage() {

        StringBuilder logMsg = new StringBuilder();

        logMsg.append(BatchUtil.cat("[", messageId, "] ", getMessage()));

        if (params != null) {
            logMsg.append(" (\n");
            for (Object option : params) {
                logMsg.append(BatchUtil.cat("\t", option, "\n"));
            }
            logMsg.append(")");
        }

        return logMsg.toString();
    }

    /**
     * メッセージIDを取得.
     * @return the messageId
     * @deprecated 例外生成時にメッセージIDを指定するメソッドをバージョン3.6から非推奨とするため、関連する本メソッドも非推奨とする。
     */
    @Deprecated
    public String getMessageId() {
        return messageId;
    }
}
