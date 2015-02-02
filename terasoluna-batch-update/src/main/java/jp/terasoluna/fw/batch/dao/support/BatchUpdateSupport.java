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

package jp.terasoluna.fw.batch.dao.support;

import java.util.Comparator;
import java.util.List;

import jp.terasoluna.fw.dao.SqlHolder;
import jp.terasoluna.fw.dao.UpdateDAO;

/**
 * バッチ更新サポートインタフェース<br>
 * <p>
 * 本クラスのメソッド実行前にsortメソッドを実行している場合、<br>
 * もしくはコンストラクタや実行メソッドなどでComparatorを渡した場合は、<br>
 * ソート順に並び替えられる。<br>
 * </p>
 * @see UpdateDAO
 */
public interface BatchUpdateSupport {
    /** UpdateDAOがnullのときのエラーステータス */
    int ERROR_UPDATE_DAO_IS_NULL = -100;

    /** sqlIdOrderに指定されていないSQL-IDが指定されているときのエラーステータス */
    int ERROR_UNKNOWN_SQL_ID = -200;

    /**
     * バッチ実行用SQLを追加する。<br>
     * @param sqlID String
     * @param bindParams Object
     */
    void addBatch(final String sqlID, final Object bindParams);

    /**
     * バッチ実行を行う。<br>
     * <p>
     * バッチ実行が成功した場合は、保持しているバッチ実行SQLリストの削除を行う。
     * </p>
     * <p>
     * 本メソッドで実行する場合は、コンストラクタ引数でUpdateDAOを渡しておくこと。<br>
     * UpdateDAOがコンストラクタ引数で渡されていない場合は、-100が返却される。
     * </p>
     * @return SQLの実行結果
     * @see UpdateDAO
     */
    int executeBatch();

    /**
     * バッチ実行を行う。<br>
     * <p>
     * バッチ実行が成功した場合は、保持しているバッチ実行SQLリストの削除を行う。
     * </p>
     * <p>
     * 本メソッドで実行する場合は、コンストラクタ引数で渡されたUpdateDAOは使用されない。<br>
     * UpdateDAOがnullの場合は、-100が返却される。
     * </p>
     * @param updateDAO UpdateDAO
     * @return SQLの実行結果
     * @see UpdateDAO
     */
    int executeBatch(UpdateDAO updateDAO);

    /**
     * バッチ実行を行う。<br>
     * <p>
     * バッチ実行が成功した場合は、保持しているバッチ実行SQLリストの削除を行う。
     * </p>
     * <p>
     * 本メソッドで実行する場合は、コンストラクタ引数で渡されたUpdateDAOは使用されない。<br>
     * UpdateDAOがnullの場合は、-100が返却される。
     * </p>
     * <p>
     * Comparatorを渡すことによりSQL実行順序順序を制御できる。<br>
     * ※Comparatorを渡した場合は必ずソートが行われる。Comparatorの大小判断に基づき、昇順でソートが行われる。
     * </p>
     * @param updateDAO UpdateDAO
     * @param comparator Comparator&lt;String&gt;
     * @return SQLの実行結果
     * @see UpdateDAO
     */
    int executeBatch(UpdateDAO updateDAO, Comparator<String> comparator);

    /**
     * バッチ実行を行う。<br>
     * <p>
     * バッチ実行が成功した場合は、保持しているバッチ実行SQLリストの削除を行う。
     * </p>
     * <p>
     * 本メソッドで実行する場合は、コンストラクタ引数で渡されたUpdateDAOは使用されない。<br>
     * UpdateDAOがnullの場合は、-100が返却される。
     * </p>
     * <p>
     * 第二引数以降に実行するSQL-IDを順番に設定することによりSQL実行順序を制御できる。<br>
     * sqlIdOrderにSQL-IDを指定した場合は、必ずその順番でSQLが実行される。<br>
     * また、その際はsqlIdOrderに指定されていないSQL-IDが存在した場合は実行されずに-200が返却される。<br>
     * </p>
     * @param updateDAO UpdateDAO
     * @param sqlIdOrder SQL-IDの実行順序を指定する
     * @return SQLの実行結果
     * @see UpdateDAO
     */
    int executeBatch(UpdateDAO updateDAO, String... sqlIdOrder);

    /**
     * バッチ実行SQLリストのソートを行う。<br>
     * <p>
     * executeBatchやgetSqlHolderListを実行する前に本メソッドを実行することで、<br>
     * SQLの実行順序をSQL-IDの昇順に並び替えることができる。
     * </p>
     * <p>
     * ※本メソッド実行タイミングで実際に内部でソートが行われるかどうかは規定しない
     * </p>
     */
    void sort();

    /**
     * バッチ実行SQLリストのソートを行う。<br>
     * <p>
     * executeBatchやgetSqlHolderListを実行する前に本メソッドを実行することで、<br>
     * SQLの実行順序をComparatorに従って並び替えることができる。
     * </p>
     * <p>
     * Comparatorを渡すことによりSQLの並び替え順序を制御できる。<br>
     * （※Comparatorを渡した場合は必ずソートが行われる。Comparatorの大小判断に基づき、昇順でソートが行われる。）
     * </p>
     * <p>
     * ※本メソッド実行タイミングで実際に内部でソートが行われるかどうかは規定しない
     * </p>
     * @param comparator Comparator&lt;String&gt;
     */
    void sort(Comparator<String> comparator);

    /**
     * バッチ実行SQLリストの内容を削除する。<br>
     * <p>
     * 保持しているバッチ実行SQLリストの削除を行う。
     * </p>
     */
    void clear();

    /**
     * バッチ実行SQLリストの登録件数を取得する。<br>
     * @return バッチ実行SQLリストの登録件数
     */
    long size();

    /**
     * SQL-IDで整列されたSqlHolderリストを取得する。<br>
     * <p>
     * 保持しているバッチ実行SQLリストをSQL-ID順に整列したリストを返却する。
     * </p>
     * <p>
     * ※本メソッドを実行しても保持しているバッチ実行SQLリストは削除されない。
     * </p>
     * @return SqlHolderリスト
     */
    List<SqlHolder> getSqlHolderList();

    /**
     * SQL-IDで整列されたSqlHolderリストを取得する。<br>
     * <p>
     * 保持しているバッチ実行SQLリストをSQL-ID順に整列したリストを返却する。
     * </p>
     * <p>
     * ※本メソッドを実行しても保持しているバッチ実行SQLリストは削除されない。
     * </p>
     * <p>
     * Comparatorを渡すことによりSQLの並び替え順序を制御できる。<br>
     * ※Comparatorを渡した場合は必ずソートが行われる。Comparatorの大小判断に基づき、昇順でソートが行われる。
     * </p>
     * @param comparator Comparator&lt;String&gt;
     * @return SqlHolderリスト
     */
    List<SqlHolder> getSqlHolderList(Comparator<String> comparator);

    /**
     * SQL-IDで整列されたSqlHolderリストを取得する。<br>
     * <p>
     * 保持しているバッチ実行SQLリストをSQL-ID順に整列したリストを返却する。
     * </p>
     * <p>
     * ※本メソッドを実行しても保持しているバッチ実行SQLリストは削除されない。
     * </p>
     * <p>
     * 実行するSQL-IDを順番に設定することにより取得すSqlHolderリストの順序を制御できる。<br>
     * sqlIdOrderにSQL-IDを指定した場合は、必ずその順番でSQLが並び替えられる。<br>
     * また、その際はsqlIdOrderに指定されていないSQL-IDが存在した場合はnullが返却される。<br>
     * </p>
     * @param sqlIdOrder SQL-IDの実行順序を指定する
     * @return SqlHolderリスト
     */
    List<SqlHolder> getSqlHolderList(String... sqlIdOrder);

}
