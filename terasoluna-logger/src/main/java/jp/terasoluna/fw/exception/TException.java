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
package jp.terasoluna.fw.exception;

import java.util.Locale;

import jp.terasoluna.fw.logger.TLogger;

/**
 * 汎用例外クラス
 * 
 * <p>
 * メッセージの管理方法は{@link TLogger}と同様です。<br>
 * ただし、設定ファイルが<code>META-INF/terasoluna-logger.properties</code>ではなく、 <code>META-INF/terasoluna-exception.properties</code>になります。
 * </p>
 */
@SuppressWarnings("serial")
public class TException extends Exception {
    /**
     * メッセージID
     */
    private final String messageId;
    /**
     * 置換パラメータ
     */
    private final Object[] args;

    /**
     * コンストラクタ
     * 
     * @param messageId メッセージID
     * @param args 置換パラメータ
     */
    public TException(String messageId, Object... args) {
        super(getMessage(messageId, ExceptionConfig.getLocale(), args));
        this.messageId = messageId;
        this.args = args;
    }

    /**
     * コンストラクタ
     * 
     * @param messageId メッセージId
     * @param cause 起因例外
     * @param args 置換パラメータ
     */
    public TException(String messageId, Throwable cause, Object... args) {
        super(getMessage(messageId, ExceptionConfig.getLocale(), args), cause);
        this.messageId = messageId;
        this.args = args;
    }

    /**
     * メッセージを取得します。
     * 
     * @param messageId メッセージID
     * @param locale ロケール
     * @param args 置換パラメータ
     * @return メッセージ
     */
    protected static String getMessage(String messageId, Locale locale,
            Object... args) {
        return ExceptionConfig.MESSAGE_MANAGER.getMessage(true, messageId,
                locale, args);
    }

    /**
     * メッセージIDを取得します
     * 
     * @return メッセージID
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * 置換パラメータを取得します。
     * 
     * @return 置換パラメータ
     */
    public Object[] getArgs() {
        return args;
    }
}
