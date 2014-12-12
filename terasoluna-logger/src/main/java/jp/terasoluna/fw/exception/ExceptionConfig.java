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

import jp.terasoluna.fw.message.MessageManager;

/**
 * 汎用例外クラスの設定クラス。
 * 
 */
public class ExceptionConfig {
    /**
     * 汎用例外クラスの設定ファイルパス
     */
    public static final String MESSAGE_CONFIG_FILE = "META-INF/terasoluna-exception.properties";

    /**
     * 汎用例外クラスのメッセージ管理クラス。
     */
    protected static final MessageManager MESSAGE_MANAGER = new MessageManager(
            MESSAGE_CONFIG_FILE);

    /**
     * 汎用例外クラスのロケール。
     */
    private static final ThreadLocal<Locale> locale = new ThreadLocal<Locale>();

    /**
     * コンストラクタ。
     */
    protected ExceptionConfig() {
    }

    /**
     * 汎用例外クラスのロケールを設定します。
     * 
     * @param locale ロケール
     */
    public static void setLocale(Locale locale) {
        ExceptionConfig.locale.set(locale);
    }

    /**
     * 汎用例外クラスのロケールを取得します。
     * 
     * @return ロケール
     */
    public static Locale getLocale() {
        return ExceptionConfig.locale.get();
    }
}
