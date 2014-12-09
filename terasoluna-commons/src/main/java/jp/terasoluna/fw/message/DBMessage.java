/*
 * Copyright (c) 2007 NTT DATA Corporation
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

package jp.terasoluna.fw.message;

import java.io.Serializable;

/**
 * メッセージリソースを保持するクラス。<br>
 * メッセージリソースにはメッセージコード、言語コード、国コード、
 * バリアントコード、メッセージ本体が格納される。
 * 
 * @see jp.terasoluna.fw.message.DataSourceMessageSource
 * @see jp.terasoluna.fw.message.DBMessageQuery
 * @see jp.terasoluna.fw.message.DBMessageResourceDAO
 * @see jp.terasoluna.fw.message.DBMessageResourceDAOImpl
 * 
 */
public class DBMessage implements Serializable {

    /**
     * シリアルバージョンID。
     */
    private static final long serialVersionUID = 299442236623116335L;

    /**
     * メッセージコード。
     */
    protected String code = null;

    /**
     * メッセージの言語コード。
     */
    protected String language = null;

    /**
     * メッセージの国コード。
     */
    protected String country = null;

    /**
     * メッセージのバリアントコード。
     */
    protected String variant = null;

    /**
     * メッセージ本体。
     */
    protected String message = null;

    /**
     * DBMessageにメッセージコード、言語コード、国コード、バリアントコード、
     * メッセージ本体を格納する。
     * 
     * @param code メッセージコード。
     * @param language メッセージの言語コード。
     * @param country メッセージの国コード。
     * @param variant メッセージのバリアントコード。
     * @param message メッセージ本体。
     */
    public DBMessage(String code, String language, String country,
            String variant, String message) {
        this.code = code;
        this.language = language;
        this.country = country;
        this.variant = variant;
        this.message = message;
    }

    /**
     * DBMessageオブジェクトからメッセージコードを取得する。
     * 
     * @return メッセージコード。
     */
    public String getCode() {
        return code;
    }

    /**
     * DBMessageオブジェクトからメッセージの言語コードを取得する。
     * 
     * @return メッセージの言語コード。
     */
    public String getLanguage() {
        return language;
    }

    /**
     * DBMessageオブジェクトからメッセージの国コードを取得する。
     * 
     * @return メッセージの国コード。
     */
    public String getCountry() {
        return country;
    }

    /**
     * DBMessageオブジェクトからメッセージのバリアントコードを取得する。
     * 
     * @return メッセージのバリアントコード。
     */
    public String getVariant() {
        return variant;
    }
    
    /**
     * DBMessageオブジェクトからメッセージ本体を取得する。
     * 
     * @return メッセージ本体。
     */
    public String getMessage() {
        return message;
    }
}
