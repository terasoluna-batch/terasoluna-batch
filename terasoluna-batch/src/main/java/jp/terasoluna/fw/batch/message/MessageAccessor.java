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

package jp.terasoluna.fw.batch.message;

/**
 * メッセージを取得するインタフェース。<br>
 */
public interface MessageAccessor {

    /**
     * メッセージを取得する。<br>
     * メッセージIDで指定したメッセージを取得します。<br>
     * メッセージIDに対応するメッセージが存在しない場合、例外（NoSuchMessageException）がスローされます。<br>
     * 置換文字列配列が引数に渡された場合、メッセージを置換して返却します。<br>
     * 置換文字列を指定しない場合、nullを指定してください。<br>
     * 置換文字列配列の要素順は置換文字の順序と対応するように設定してください。
     * @param code メッセージID
     * @param args 置換文字列の配列
     * @return String メッセージ
     */
    public String getMessage(String code, Object[] args);

}
