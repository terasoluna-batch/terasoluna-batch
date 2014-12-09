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

package jp.terasoluna.fw.dao;

/**
 * StoredProcedureDAOインタフェース。
 * 
 * StoredProcedureを実行するためのDAOインタフェースである。
 *
 */
public interface StoredProcedureDAO {

    /**
     * 指定されたSQLIDのストアドプロシージャーを実行する。
     * ストアドプロシージャーの結果であるアウトパラメータは、
     * 引数のbindParamsに反映される。
     * 
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     */
    void executeForObject(String sqlID, Object bindParams);
}
