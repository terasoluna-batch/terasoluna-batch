/*
 * Copyright (c) 2008 NTT DATA Corporation
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

package jp.terasoluna.fw.dao;

import jp.terasoluna.fw.dao.event.DataRowHandler;

/**
 * <p>
 * 参照系SQLの結果を1行ずつ処理するためのインタフェース。
 * </p>
 * 
 * <p>
 * 1行毎にDataRowHandler#handleRow()を呼び出し、
 * 1行のデータを格納したオブジェクトを引数に渡すよう実装すること。
 * </p>
 *
 * @see jp.terasoluna.fw.dao.event.DataRowHandler
 */
public interface QueryRowHandleDAO {
    /**
     * SQLの実行結果をDataRowHandlerで1行ずつ処理する。
     * 
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param rowHandler 1行取得ごとに処理するハンドラ
     */
    void executeWithRowHandler(final String sqlID, final Object bindParams,
            final DataRowHandler rowHandler);
}
