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

import java.util.List;

/**
 * UpdateDAOインタフェース。
 * 
 * 更新系SQLを実行するためのDAOインタフェースである。
 * 
 */
public interface UpdateDAO {

    /**
     * 引数sqlIDで指定されたSQLを実行して、結果件数を返却する。
     * 実行するSQLは「insert, update delete」の3種類とする。
     * 
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @return SQLの実行結果件数を返却
     */
    int execute(String sqlID, Object bindParams);

    /**
     * バッチ追加メソッド。
     * バッチ処理として追加したいSQLのSQLIDとバインドパラメータを
     * 引数に渡す。
     * 
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @deprecated addBatchの代わりに{@link #executeBatch(List)}
     * を使用すること
     */
    @Deprecated
    void addBatch(String sqlID, Object bindParams);

    /**
     * バッチ処理の実行メソッド。
     * 
     * @return SQLの実行結果
     * @deprecated executeBatchの代わりに{@link #executeBatch(List)}
     * を使用すること
     */
    @Deprecated
    int executeBatch();
    
    /**
     * バッチ更新処理を行うメソッド。<br/>
     * 引数の{@link SqlHolder}のリストで指定されたすべてのSQLを実行する。
     * DAOインスタンスに状態を持たせない為、バッチ更新対象のSQLはすべて
     * このメソッド内で実行まで完結する必要がある。
     * 
     * @param sqlHolders バッチ更新対象のsqlId、パラメータを格納した
     * SqlHolderインスタンスのリスト
     * @return SQLの実行結果件数
     */
    int executeBatch(List<SqlHolder> sqlHolders);

}
