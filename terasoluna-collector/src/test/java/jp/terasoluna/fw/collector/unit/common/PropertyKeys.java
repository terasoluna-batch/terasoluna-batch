package jp.terasoluna.fw.collector.unit.common;

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
     * コンテキストファイルのパス
     */
    CONTEXTFILE_DIR,
    /**
     * アプリケーションコンテキスト定義ファイル名
     */
    APPLICATIONCONTEXT_FILE;

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
