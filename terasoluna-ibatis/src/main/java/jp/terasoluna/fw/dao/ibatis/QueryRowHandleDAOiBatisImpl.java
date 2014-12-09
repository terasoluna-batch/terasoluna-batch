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

package jp.terasoluna.fw.dao.ibatis;

import jp.terasoluna.fw.dao.QueryRowHandleDAO;
import jp.terasoluna.fw.dao.event.DataRowHandler;
import jp.terasoluna.fw.dao.ibatis.event.RowHandlerWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * <p>
 * QueryRowHandleDAOインタフェースのiBATIS実装クラス。<br>
 * 参照系SQLの結果を1行ずつ処理する。
 * </p>
 * 
 * <p>
 * executeWithRowHandlerメソッドの引数にDataRowHandler実装クラスを渡して使用する。<br>
 * executeWithRowHandlerメソッド自体は、SQLの実行結果を返さないことに注意する。<br>
 * SQLの実行結果の1行ごとにDataRowHandler#handleRow()が呼ばれ、
 * 引数に1行のデータを格納したオブジェクトが渡される。<br>
 * DataRowHandler#handleRow()には、1行分のデータ処理を実装する必要がある。<br>
 * </p>
 * 
 * <p>
 * <fieldset style="border:1pt solid black;padding:10px;width:100%;">
 * <legend>注意事項</legend>
 * iBATISマッピング定義ファイルの&lt;statement&gt;要素、&lt;select&gt;要素、
 * &lt;procedure&gt;要素にて大量データを返すようなクエリを記述する場合には、
 * fetchSize属性に適切な値を設定しておくこと。<br>
 * fetchSize属性にはJDBCドライバとデータベース間の通信において、
 * 一度の通信で取得するデータの件数を設定する。<br>
 * fetchSize属性を省略した場合は各JDBCドライバのデフォルト値が利用される。
 * </fieldset>
 * </p>
 * 
 * @see jp.terasoluna.fw.dao.event.DataRowHandler
 * @see jp.terasoluna.fw.dao.QueryRowHandleDAO
 */
public class QueryRowHandleDAOiBatisImpl extends SqlMapClientDaoSupport
        implements QueryRowHandleDAO {

    /**
     * ログインスタンス
     */
    private static Log log = LogFactory.
            getLog(QueryRowHandleDAOiBatisImpl.class);

    /**
     * SQLの実行結果をDataRowHandlerで1行ずつ処理する。
     *
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param rowHandler 1行取得ごとに処理するハンドラ
     */
    public void executeWithRowHandler(final String sqlID,
            final Object bindParams, final DataRowHandler rowHandler) {
        if (log.isDebugEnabled()) {
            log.debug("executeWithRowHandler Start.");
        }

        // SqlMapClientTemplateの取得
        SqlMapClientTemplate sqlMapTemp = getSqlMapClientTemplate();

        // SQLの実行
        sqlMapTemp.queryWithRowHandler(sqlID, bindParams,
                new RowHandlerWrapper(rowHandler));

        if (log.isDebugEnabled()) {
            log.debug("executeWithRowHandler End.");
        }
    }

}
