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
import java.util.Map;

/**
 * QueryDAOインタフェース。
 * 
 * 参照系SQLを実行するためのDAOインタフェースである。
 *
 */
public interface QueryDAO {

    /**
     * SQLの実行結果を指定された型にして返却する。
     * 
     * @param <E> 返却値の型
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param clazz 返却値のクラス
     * @return SQLの実行結果
     */
    <E> E executeForObject(String sqlID, Object bindParams, Class clazz);

    /**
     * SQLの実行結果をMapに格納して返却する。
     * 
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @return SQLの実行結果
     */
    Map<String, Object> executeForMap(String sqlID, Object bindParams);

    /**
     * SQLの実行結果を指定された型の配列にして返却する。
     * 結果0件時は空配列が返却される。
     * @param <E> 返却値の型
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param clazz 返却値のクラス
     * @return SQLの実行結果
     */
    <E> E[] executeForObjectArray(String sqlID,
            Object bindParams, Class clazz);

    /**
     * SQLの実行結果をMapの配列に格納して返却する。
     * 結果0件時は空配列が返却される。
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @return SQLの実行結果
     */
    Map<String, Object>[] executeForMapArray(String sqlID,
            Object bindParams);

    /**
     * SQLの実行結果を指定されたインデックスから指定された行数分、
     * 指定された型の配列にして返却する。
     * 結果0件時は空配列が返却される。
     * @param <E> 返却値の型
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param clazz 返却値のクラス
     * @param beginIndex 取得する開始インデックス
     * @param maxCount 取得する件数
     * @return SQLの実行結果
     */
    <E> E[] executeForObjectArray(String sqlID,
            Object bindParams, Class clazz, int beginIndex, int maxCount);

    /**
     * SQLの実行結果を指定されたインデックスから指定された行数分、
     * Mapの配列にして返却する。
     * 結果0件時は空配列が返却される。
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param beginIndex 取得する開始インデックス
     * @param maxCount 取得する件数
     * @return SQLの実行結果
     */
    Map<String, Object>[] executeForMapArray(String sqlID,
            Object bindParams, int beginIndex, int maxCount);

    /**
     * SQLの実行結果を指定された型のListで返却する。
     * 結果0件時は空リストが返却される。
     * @param <E> 返却値の型
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @return SQLの実行結果
     */
    <E> List<E> executeForObjectList(String sqlID,
            Object bindParams);

    /**
     * SQLの実行結果をMapのListに格納して返却する。
     * 結果0件時は空リストが返却される。
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @return SQLの実行結果
     */
    List<Map<String, Object>> executeForMapList(String sqlID,
            Object bindParams);

    /**
     * SQLの実行結果を指定されたインデックスから指定された行数分、
     * 指定された型のListで返却する。
     * 結果0件時は空リストが返却される。
     * @param <E> 返却値の型
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param beginIndex 取得する開始インデックス
     * @param maxCount 取得する件数
     * @return SQLの実行結果
     */
    <E> List<E> executeForObjectList(String sqlID,
            Object bindParams, int beginIndex, int maxCount);

    /**
     * SQLの実行結果を指定されたインデックスから指定された行数分、
     * MapのListにして返却する。
     * 結果0件時は空リストが返却される。
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param beginIndex 取得する開始インデックス
     * @param maxCount 取得する件数
     * @return SQLの実行結果
     */
    List<Map<String, Object>> executeForMapList(String sqlID,
            Object bindParams, int beginIndex, int maxCount);


}