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

package jp.terasoluna.fw.dao.ibatis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.terasoluna.fw.dao.SqlHolder;
import jp.terasoluna.fw.dao.UpdateDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * UpdateDAOインタフェースのiBATIS用実装クラス。
 * <p/>
 * このクラスは、Bean定義ファイルにBean定義を行いサービス層に
 * インジェクションして使用する。以下に設定例および実装例を示す。<br/>
 * <p/>
 * <b>注意点</b><br/>
 * executeBatchはiBATISのバッチ実行機能を使用している。
 * executeBatchの戻り値は、実行したSQLによる変更行数が返却するが、
 * java.sql.PreparedStatementを使用しているため、
 * ドライバにより正確な行数が取得できないケースがある。<br/>
 * 変更行数が正確に取得できないドライバを使用する場合、
 * 変更行数がトランザクションに影響を与える業務では
 * (変更行数が0件の場合エラー処理をするケース等)、
 * バッチ更新は使用しないこと。<br/>
 * 参考資料）<br/>
 * <a href="http://otndnld.oracle.co.jp/document/products/oracle10g/101/doc_v5/java.101/B13514-02.pdf#page=450"
 * target="_blank">
 * http://otndnld.oracle.co.jp/document/products/oracle10g/101/doc_v5/java.101/B13514-02.pdf</a>
 * <br/>
 * 450ページ「標準バッチ処理のOracle 実装の更新件数」を参照のこと。<br/>
 * 
 * <p/>
 * <fieldset style="border:1pt solid black;padding:10px;width:100%;">
 * <legend>
 * Bean定義ファイルの例
 * </legend>
 * <p/>
 * <code>
 * &lt;bean id="registBLogic"
 * class="jp.strutspring.blogic.RegistBLogic"&gt;<br/>
 * &nbsp;&nbsp;&lt;property
 * name="dao"&gt;&lt;ref local="<b>updateDAO</b>"/&gt;
 * &lt;/property&gt;<br/>
 * &lt;/bean&gt;<br/>
 * <br/>
 * &lt;bean id="<b>updateDAO</b>"<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;
 * class="<b>jp.terasoluna.
 * fw.dao.ibatis.UpdateDAOiBATISImpl</b>"&gt;<br/>
 * &nbsp;&nbsp;&lt;property name="sqlMapClient"&gt;
 * &lt;ref local="sqlMapClient"/&gt;&lt;/property&gt;<br/>
 * &lt;/bean&gt;<br/>
 * <br/>
 * &lt;bean id="sqlMapClient"<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * class="org.springframework.orm.ibatis.SqlMapClientFactoryBean"&gt;<br/>
 * &nbsp;&nbsp;&lt;property name="configLocation"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;value&gt;sqlMapConfig.xml&lt;/value&gt;<br/>
 * &nbsp;&nbsp;&lt;/property&gt;<br/>
 * &lt;/bean&gt;
 * </code>
 * </fieldset>
 * <p/>
 * <fieldset style="border:1pt solid black;padding:10px;width:100%;">
 * <legend>
 * サービス層での使用方法：データ一件の更新処理
 * </legend>
 * <p/>
 * <code>
 * public class RegistBLogic {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;private UpdateDAO dao = null;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;public UpdateDAO getDao() {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return dao;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;public void setDao(UpdateDAO dao) {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;this.dao = dao;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;public String execute() {<br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * UserBean bean = new UserBean();<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;bean.setId("1");<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;bean.setName("N.OUNO");<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;bean.setAge("20");<br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * <b>dao.execute("insertUser", bean);</b><br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return "success";<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
 * }
 * </code>
 * </fieldset>
 * <p/>
 * <fieldset style="border:1pt solid black;padding:10px;width:100%;">
 * <legend>
 * サービス層での使用方法：オンラインバッチ処理
 * </legend>
 * <p/>
 * <code>
 * public String execute() {<br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;List&lt;SqlHolder&gt; sqlHolders = new ArrayList&lt;SqlHolder&gt;();<br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;UserBean bean = new UserBean();<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;bean.setId("1");<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;bean.setName("N.OUNO");<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;bean.setAge("20");<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;SqlHolder holder = new SqlHolder("insertUser", bean);<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;sqlHolders.add(holder);<br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;UserBean bean2 = new UserBean();<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;bean.setId("2");<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;bean.setName("K.FUJIMOTO");<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;bean.setAge("21");<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;SqlHolder holder2 = new SqlHolder("insertUser", bean2);<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;sqlHolders.add(holder2);<br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;<b>dao.executeBatch(sqlHolders);</b><br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;...<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;return "success";<br/>
 * }
 * </code>
 * </fieldset>
 * <p/>
 *
 */
public class UpdateDAOiBatisImpl extends SqlMapClientDaoSupport implements
        UpdateDAO {
    /**
     * ログインスタンス
     */
    static Log log
        = LogFactory.getLog(UpdateDAOiBatisImpl.class);

    /**
     * バッチ実行用のSQL
     * @deprecated この変数は将来削除されます
     */
    @Deprecated
    protected final ThreadLocal<List<SqlHolder>> batchSqls 
        = new ThreadLocal<List<SqlHolder>>();
    
    /**
     * 引数sqlIDで指定されたSQLを実行して、結果件数を返却する。
     * 実行するSQLは「insert, update delete」の3種類とする。
     *
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @return SQLの実行結果件数を返却
     */
    public int execute(String sqlID, Object bindParams) {

        if (log.isDebugEnabled()) {
            log.debug("execute Start.");
        }

        //SqlMapClientの取得
        SqlMapClientTemplate sqlMapTemp = getSqlMapClientTemplate();

        // SQLの実行：データ追加。
        int row = sqlMapTemp.update(sqlID, bindParams);

        if (log.isDebugEnabled()) {
            log.debug("execute End. success count:" + row);
        }
        return row;
    }

    /**
     * バッチ追加メソッド。
     * 引数のSQLをスレッドローカルに保持する。
     * 複数のリクエストをまたいでSQLを保持することはできない。
     * 追加後に、<code>UpdateDAO#executeBatch()</code>で、一括実行を行う。
     * 
     * <b>注意：</b>このメソッドを使用すると、バッチ更新対象のSQLが
     * クリアされない可能性がある。{@link #executeBatch(List)}を使用すること。
     *
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @deprecated addBatchの代わりに{@link #executeBatch(List)}
     * を使用すること
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public void addBatch(final String sqlID, final Object bindParams) {
        // スレッドローカルの取得
        List<SqlHolder> sqlHolders = batchSqls.get();
        if (sqlHolders == null) {
            sqlHolders = new ArrayList<SqlHolder>();
            batchSqls.set(sqlHolders);
            if (log.isDebugEnabled()) {
                log.debug("Create new SqlHolder in ThreadLocal.");
            }
        }
        
        // バッチ用のSQLをスレッドローカルに保持する    
        sqlHolders.add(new SqlHolder(sqlID, bindParams));
        if (log.isDebugEnabled()) {
            log.debug("Add batch sql.  SQL_ID='" + sqlID 
                    + "' Parameters:" + bindParams);
        }
    }

    /**
     * バッチ処理の実行メソッド。
     * <code>{@link #addBatch(String, Object)}</code>で追加されたSQLを
     * 一括実行する。バッチ実行後はSQLをクリアする。
     * <code>{@link #addBatch(String, Object)}</code>でSQLを追加していない場合、
     * 実行時例外が発生する。
     * 
     * <b>注意：</b>このメソッドを使用すると、バッチ更新対象のSQLが
     * クリアされない可能性がある。{@link #executeBatch(List)}を使用すること。
     *
     * @return SQLの実行結果
     * @deprecated addBatchの代わりに{@link #executeBatch(List)}
     * を使用すること
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public int executeBatch() {
        // スレッドローカルからSQLを取り出す
        final List<SqlHolder> sqlHolders = batchSqls.get();
        
        // SQLバッチ実行
        Integer result = 0;
        try {
            result = (Integer) getSqlMapClientTemplate().execute(
                    new SqlMapClientCallback() {
                public Object doInSqlMapClient(SqlMapExecutor executor)
                      throws SQLException {
                    StringBuilder logStr = new StringBuilder();
                    if (log.isDebugEnabled()) {
                        log.debug("Batch SQL count:" + sqlHolders.size());
                    }
                    executor.startBatch();
                    for (SqlHolder sqlHolder : sqlHolders) {
                        executor.update(
                            sqlHolder.getSqlID(), sqlHolder.getBindParams());
                        
                        if (log.isDebugEnabled()) {
                            logStr.setLength(0);
                            logStr.append("Call update sql. - SQL_ID:'");
                            logStr.append(sqlHolder.getSqlID());
                            logStr.append("' Parameters:");
                            logStr.append(sqlHolder.getBindParams());
                            log.debug(logStr.toString());
                        }
                    }
                    return executor.executeBatch();
                }
            });
        } finally {
            // スレッドローカルからSQLを削除
            batchSqls.remove();
            if (log.isDebugEnabled()) {
                log.debug("Remove SqlHolder in ThreadLocal.");
            }
        }
        
        if (log.isDebugEnabled()) {
            log.debug("ExecuteBatch complete. Result:" + result);
        }
        return result.intValue();
    }

    /**
     * バッチ処理の実行メソッド。<br/>
     * 
     * 引数の{@link SqlHolder}のリストで指定されたすべてのSQLを実行する。<br/>
     * 
     * <br/>
     * <b>注意点</b><br/>
     * executeBatchはiBATISのバッチ実行機能を使用している。
     * executeBatchの戻り値は、実行したSQLによる変更行数が返却するが、
     * java.sql.PreparedStatementを使用しているため、
     * ドライバにより正確な行数が取得できないケースがある。<br/>
     * 変更行数が正確に取得できないドライバを使用する場合、
     * 変更行数がトランザクションに影響を与える業務では
     * (変更行数が0件の場合エラー処理をするケース等)、
     * バッチ更新は使用しないこと。<br/>
     * 参考資料）<br/>
     * <a href="http://otndnld.oracle.co.jp/document/products/oracle10g/101/doc_v5/java.101/B13514-02.pdf#page=450"
     * target="_blank">
     * http://otndnld.oracle.co.jp/document/products/oracle10g/101/doc_v5/java.101/B13514-02.pdf</a>
     * <br/>
     * 450ページ「標準バッチ処理のOracle 実装の更新件数」を参照のこと。<br/>
     * 
     * @param sqlHolders バッチ更新対象のsqlId、パラメータを格納した
     * SqlHolderインスタンスのリスト
     * @return SQLの実行結果件数
     */
    public int executeBatch(final List<SqlHolder> sqlHolders) {
        // SQLバッチ実行
        Integer result = 0;
        result = (Integer) getSqlMapClientTemplate().execute(
                new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor)
                  throws SQLException {
                StringBuilder logStr = new StringBuilder();
                if (log.isDebugEnabled()) {
                    log.debug("Batch SQL count:" + sqlHolders.size());
                }
                executor.startBatch();
                for (SqlHolder sqlHolder : sqlHolders) {
                    executor.update(
                        sqlHolder.getSqlID(), sqlHolder.getBindParams());
                    
                    if (log.isDebugEnabled()) {
                        logStr.setLength(0);
                        logStr.append("Call update sql. - SQL_ID:'");
                        logStr.append(sqlHolder.getSqlID());
                        logStr.append("' Parameters:");
                        logStr.append(sqlHolder.getBindParams());
                        log.debug(logStr.toString());
                    }
                }
                return executor.executeBatch();
            }
        });
        
        if (log.isDebugEnabled()) {
            log.debug("ExecuteBatch complete. Result:" + result);
        }
        return result.intValue();
    }
}
