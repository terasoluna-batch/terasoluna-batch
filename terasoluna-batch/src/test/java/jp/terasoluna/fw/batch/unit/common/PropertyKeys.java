package jp.terasoluna.fw.batch.unit.common;

/*
 * Copyright (c) 2012 NTT DATA Corporation
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

/*
 * Copyright (c) 2012 NTT DATA Corporation
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

/**
 * プロパティのキーを表現する列挙型です。
 * 
 * <pre>
 * 列挙型を小文字にし、「_」を「.」に置換したものがプロパティのキーになります。
 * 
 * AAA_BBB => aaa.bbb
 * 
 * この列挙型で管理するプロパティのキーに大文字を使用できないことに注意してください。
 * </pre>
 * 
 * @author maki
 * 
 */
public enum PropertyKeys {
    /**
     * WEBアプリケーションパス
     */
    WEBAPP_PATH,
    /**
     * WEB-INFパス
     */
    WEBINF_DIR,
    /**
     * META-INFパス
     */
    METAINF_DIR,
    /**
     *
     */
    CONTEXT_FILE,
    /**
     * アプリケーションコンテキスト定義ファイルパス
     */
    APPLICATIONCONTEXT_FILE,
    /**
     * モジュール定義ファイルパス
     */
    MODULECONTEXT_FILE,
    /**
     * モックDAO定義ファイルパス
     */
    MOCKDAOBEANS_FILE,
    /**
     * BLogicResultの成功を表す文字列
     */
    SUCCESS_STRING,
    /**
     * BLogicResultの失敗を表す文字列
     */
    FAILURE_STRING;

    /**
     * プロパティのキー
     */
    private final String key;

    private PropertyKeys() {
        key = name().toLowerCase().replace("_", ".");
    }

    /**
     * プロパティのキー形式で返却します。
     * 
     * @return プロパティのキー
     */
    public String getKey() {
        return key;
    }
}
