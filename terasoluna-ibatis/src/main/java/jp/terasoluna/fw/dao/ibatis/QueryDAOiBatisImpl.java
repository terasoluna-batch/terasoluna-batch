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

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import jp.terasoluna.fw.dao.IllegalClassTypeException;
import jp.terasoluna.fw.dao.QueryDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * QueryDAOインタフェースのiBATIS用実装クラス。
 * <p/>
 * このクラスは、Bean定義ファイルにBean定義を行いサービス層に
 * インジェクションして使用する。以下に設定例および実装例を示す。<br/>
 * 
 * <br/> <fieldset style="border:1pt solid black;padding:10px;width:100%;">
 * <legend> Bean定義ファイルの例 </legend> <p/> <code>
 * &lt;bean id="listBLogic"
 * class="jp.strutspring.blogic.ListBLogic"&gt;<br/>
 * &nbsp;&nbsp;&lt;property name="dao"&gt;&lt;ref
 * local="<b>queryDAO</b>"/&gt;&lt;/property&gt;<br/>
 * &lt;/bean&gt;<br/>
 * <br/>
 * &lt;bean id="<b>queryDAO</b>"<br/>
 * &nbsp;&nbsp;class="<b>jp.terasoluna.
 * fw.dao.ibatis.QueryDAOiBatisImpl</b>"&gt;<br/>
 * &nbsp;&nbsp;&lt;property name="sqlMapClient"&gt;&lt;ref
 * local="sqlMapClient"/&gt;&lt;/property&gt;<br/>
 * &lt;/bean&gt;<br/>
 * <br/>
 * &lt;bean id="sqlMapClient"<br/>
 * &nbsp;&nbsp;&nbsp;
 * class="org.springframework.orm.ibatis.SqlMapClientFactoryBean"&gt;<br/>
 * &nbsp;&nbsp;&lt;property name="configLocation"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;value&gt;sqlMapConfig.xml&lt;/value&gt;<br/>
 * &nbsp;&nbsp;&lt;/property&gt;<br/>
 * &lt;/bean&gt;
 * </code>
 * </fieldset> <p/> <fieldset style="border:1pt solid
 * black;padding:10px;width:100%;">
 * <legend> サービス層での使用方法：取得データが1件の場合 </legend>
 * <p/> <code>
 * public&nbsp;class&nbsp;ListBLogic{<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;private&nbsp;QueryDAO&nbsp;dao&nbsp;=&nbsp;null;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;public&nbsp;QueryDAO&nbsp;getDao()&nbsp;{<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return&nbsp;dao;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
 * &nbsp;&nbsp;&nbsp;
 * public&nbsp;void&nbsp;setDao(QueryDAO&nbsp;dao)&nbsp;{<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * this.dao&nbsp;=&nbsp;dao;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;public&nbsp;String
 * execute(ActionForm&nbsp;form)&nbsp;{<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;UserBean&nbsp;bean<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=
 * <b>dao.executeForObject("getUser","10000000",UserBean.class);</b><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return&nbsp;"success";<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
 * }
 * </code>
 * </fieldset> <p/> 
 * <fieldset style="border:1pt solid
 * black;padding:10px;width:100%;">
 * <legend> サービス層での使用方法：取得データが複数件の場合(全件取得)List版</legend>
 * <p/>
 * <code>
 * public&nbsp;String&nbsp;execute(ActionForm&nbsp;form)&nbsp;{</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;List<UserBean>&nbsp;bean</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=
 * <b>dao.executeForObjectList("getUser","10000000");</b></br>
 * &nbsp;&nbsp;&nbsp;&nbsp;...</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;return&nbsp;"success";</br>
 * }
 * </code>
 * </fieldset>
 * <p/>
 * <fieldset style="border:1pt solid
 * black;padding:10px;width:100%;">
 * <legend> サービス層での使用方法：取得データが複数件の場合(全件取得)配列版</legend>
 * <p/> 
 * <code>
 * public&nbsp;String&nbsp;execute(ActionForm&nbsp;form)&nbsp;{</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;UserBean[]&nbsp;bean</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=
 * <b>dao.executeForObjectArray("getUser","10000000",UserBean.class);</b></br>
 * &nbsp;&nbsp;&nbsp;&nbsp;...</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;return&nbsp;"success";</br>
 * }
 * </code>
 * </fieldset> <p/> <fieldset style="border:1pt solid
 * black;padding:10px;width:100%;">
 * <legend> サービス層での使用方法：取得データが複数件の場合
 * (指定インデックスからの指定件数分を取得)List版
 * </legend>
 * <br>
 * &nbsp;&nbsp;引数に取得開始インデックスおよび件数の指定を行う。
 * 以下の例では、21件目から10件を取得する。 <p/> 
 * <code>
 * public&nbsp;String&nbsp;execute(ActionForm&nbsp;form)&nbsp;{</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;List<UserBean>&nbsp;bean</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=
 * <b>dao.executeForObjectList("getUser","10000000",
 * 20,&nbsp;10);</b></br>
 * &nbsp;&nbsp;&nbsp;&nbsp;...</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;return&nbsp;"success";</br>
 * }
 * </code>
 * </fieldset> <p/> <fieldset style="border:1pt solid
 * black;padding:10px;width:100%;">
 * <legend> サービス層での使用方法：取得データが複数件の場合
 * (指定インデックスからの指定件数分を取得)配列版
 * </legend>
 * <br>
 * &nbsp;&nbsp;引数に取得開始インデックスおよび件数の指定を行う。
 * 以下の例では、21件目から10件を取得する。 <p/>
 * <code>
 * public&nbsp;String&nbsp;execute(ActionForm&nbsp;form)&nbsp;{</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;UserBean[]&nbsp;bean</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=
 * <b>dao.executeForObjectArray("getUser","10000000",UserBean.class,
 * 20,&nbsp;10);</b></br>
 * &nbsp;&nbsp;&nbsp;&nbsp;...</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;return&nbsp;"success";</br>
 * }
 * </code>
 * </fieldset> <p/>
 * 
 */
public class QueryDAOiBatisImpl extends SqlMapClientDaoSupport implements
        QueryDAO {

    /**
     * ログインスタンス
     */
    private static Log log = LogFactory.getLog(QueryDAOiBatisImpl.class);

    /**
     * 参照系SQLを実行し、結果を指定されたオブジェクトとして返却する。
     * SQLの結果オブジェクトと、指定された型が違った場合は、例外を発生させる。
     * 
     * @param <E>
     *            返却値の型
     * @param sqlID
     *            実行するSQLのID
     * @param bindParams
     *            SQLにバインドする値を格納したオブジェクト
     * @param clazz
     *            返却値のクラス
     * @return SQLの実行結果
     */
    @SuppressWarnings("unchecked")
    public <E> E executeForObject(String sqlID,
                                   Object bindParams,
                                   Class clazz) {

        if (log.isDebugEnabled()) {
            log.debug("executeForObject Start.");
        }

        // SqlMapClientTemplateの取得
        SqlMapClientTemplate sqlMapTemp = getSqlMapClientTemplate();

        // SQLの実行：値の取得
        Object obj = sqlMapTemp.queryForObject(sqlID, bindParams);
        if (log.isDebugEnabled() && obj != null) {
            log.debug("Return type:" + obj.getClass().getName());
        }

        E rObj = null;
        try {
            if (clazz != null && obj != null) {
                rObj = (E) clazz.cast(obj);
            }
        } catch (ClassCastException e) {
            log.error(IllegalClassTypeException.ERROR_ILLEGAL_CLASS_TYPE);
            throw new IllegalClassTypeException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("executeForObject End.");
        }

        return rObj;
    }

    /**
     * 参照系SQLを実行し、Mapとして返却する。
     * 
     * @param sqlID
     *            実行するSQLのID
     * @param bindParams
     *            SQLにバインドする値を格納したオブジェクト
     * @return SQLの実行結果
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> executeForMap(String sqlID, Object bindParams) {

        if (log.isDebugEnabled()) {
            log.debug("executeForMap Start.");
        }

        Map<String, Object> rObj = this.executeForObject(sqlID, bindParams,
                Map.class);

        if (log.isDebugEnabled()) {
            log.debug("executeForMap End.");
        }

        return rObj;
    }

    /**
     * 参照系SQLを実行し、結果を指定されたオブジェクトの配列として返却する。
     * SQLの結果オブジェクトと、指定された型が違った場合は、例外を発生させる。
     * 結果0件時は空配列が返却される。
     * @param <E>
     *            返却値の型
     * @param sqlID
     *            実行するSQLのID
     * @param bindParams
     *            SQLにバインドする値を格納したオブジェクト
     * @param clazz
     *            返却値のクラス
     * @return SQLの実行結果
     */
    @SuppressWarnings("unchecked")
    public <E> E[] executeForObjectArray(String sqlID, Object bindParams,
            Class clazz) {

        if (log.isDebugEnabled()) {
            log.debug("executeForObjectArray Start.");
        }

        if (clazz == null) {
            log.error(IllegalClassTypeException.ERROR_ILLEGAL_CLASS_TYPE);
            throw new IllegalClassTypeException();
        }

        // SqlMapClientの取得
        SqlMapClientTemplate sqlMapTemp = getSqlMapClientTemplate();

        // SQLの実行：値の取得
        List<E> list = sqlMapTemp.queryForList(sqlID, bindParams);

        // 配列に変換
        E[] retArray = (E[]) Array.newInstance(clazz, list.size());
        try {
            list.toArray(retArray);
        } catch (ArrayStoreException e) {
            log.error(IllegalClassTypeException.ERROR_ILLEGAL_CLASS_TYPE);
            throw new IllegalClassTypeException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("executeForObjectArray End.");
        }

        return retArray;
    }

    /**
     * 参照系SQLを実行し、Mapの配列として返却する。
     * Map配列の変換に失敗した場合は、例外を発生させる。
     * 結果0件時は空配列が返却される。
     * @param sqlID
     *            実行するSQLのID
     * @param bindParams
     *            SQLにバインドする値を格納したオブジェクト
     * @return SQLの実行結果
     */
    public Map<String, Object>[] executeForMapArray(String sqlID,
            Object bindParams) {

        if (log.isDebugEnabled()) {
            log.debug("executeForMapArray Start.");
        }

        Map<String, Object>[] map = executeForObjectArray(sqlID, bindParams,
                Map.class);

        if (log.isDebugEnabled()) {
            log.debug("executeForMapArray End.");
        }

        return map;
    }

    /**
     * 参照系SQLを実行し、結果を指定されたオブジェクトの配列として返却する。
     * 返却される結果は、引数にて指定されたインデックスから指定された件数である。
     * SQLの結果オブジェクトと、指定された型が違った場合は、例外を発生させる。
     * 結果0件時は空配列が返却される。
     * @param <E>
     *            返却値の型
     * @param sqlID
     *            実行するSQLのID
     * @param bindParams
     *            SQLにバインドする値を格納したオブジェクト
     * @param clazz
     *            返却値のクラス
     * @param beginIndex
     *            取得する開始インデックス
     * @param maxCount
     *            取得する件数
     * @return SQLの実行結果
     */
    @SuppressWarnings("unchecked")
    public <E> E[] executeForObjectArray(String sqlID, Object bindParams,
            Class clazz, int beginIndex, int maxCount) {

        if (log.isDebugEnabled()) {
            log.debug("executeForObjectArray Start.");
        }

        if (clazz == null) {
            log.error(IllegalClassTypeException.ERROR_ILLEGAL_CLASS_TYPE);
            throw new IllegalClassTypeException();
        }

        // SqlMapClientの取得
        SqlMapClientTemplate sqlMapTemp = getSqlMapClientTemplate();

        // SQLの実行：値の取得
        List<E> list = sqlMapTemp.queryForList(sqlID, bindParams, beginIndex,
                maxCount);

        // 配列に変換
        E[] retArray = (E[]) Array.newInstance(clazz, list.size());
        try {
            list.toArray(retArray);
        } catch (ArrayStoreException e) {
            log.error(IllegalClassTypeException.ERROR_ILLEGAL_CLASS_TYPE);
            throw new IllegalClassTypeException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("executeForObjectArray End.");
        }

        return retArray;
    }

    /**
     * 参照系SQLを実行し、Mapの配列として返却する。
     * 返却される結果は、引数にて指定されたインデックスから指定された件数である。
     * Map配列の変換に失敗した場合は、例外を発生させる。
     * 結果0件時は空配列が返却される。
     * @param sqlID
     *            実行するSQLのID
     * @param bindParams
     *            SQLにバインドする値を格納したオブジェクト
     * @param beginIndex
     *            取得する開始インデックス
     * @param maxCount
     *            取得する件数
     * @return SQLの実行結果
     */
    public Map<String, Object>[] executeForMapArray(String sqlID,
            Object bindParams, int beginIndex, int maxCount) {

        if (log.isDebugEnabled()) {
            log.debug("executeForMapArray Start.");
        }

        Map<String, Object>[] map = executeForObjectArray(sqlID, bindParams,
                Map.class, beginIndex, maxCount);

        if (log.isDebugEnabled()) {
            log.debug("executeForMapArray End.");
        }

        return map;
    }

    /**
     * 参照系SQLを実行し、結果を指定されたオブジェクトのListとして返却する。
     * 結果0件時は空リストが返却される。
     * @param <E>
     *            返却値の型
     * @param sqlID
     *            実行するSQLのID
     * @param bindParams
     *            SQLにバインドする値を格納したオブジェクト
     * @return SQLの実行結果リスト
     */
    @SuppressWarnings("unchecked")
    public <E> List<E> executeForObjectList(String sqlID, Object bindParams) {

        if (log.isDebugEnabled()) {
            log.debug("executeForObjectList Start.");
        }

        // SqlMapClientの取得
        SqlMapClientTemplate sqlMapTemp = getSqlMapClientTemplate();

        // SQLの実行：値の取得
        List<E> list = sqlMapTemp.queryForList(sqlID, bindParams);
        
        if (log.isDebugEnabled()) {
            log.debug("executeForObjectList End.");
        }

        return list;
    }
    
    /**
     * 参照系SQLを実行し、MapのListとして返却する。
     * 結果0件時は空リストが返却される。
     * @param sqlID
     *            実行するSQLのID
     * @param bindParams
     *            SQLにバインドする値を格納したオブジェクト
     * @return SQLの実行結果
     */
    public List<Map<String, Object>> executeForMapList(String sqlID,
            Object bindParams) {

        if (log.isDebugEnabled()) {
            log.debug("executeForMapList Start.");
        }

        List<Map<String, Object>> mapList = executeForObjectList(sqlID, bindParams);

        if (log.isDebugEnabled()) {
            log.debug("executeForMapList End.");
        }

        return mapList;
    }    

    /**
     * 参照系SQLを実行し、結果を指定されたオブジェクトのListとして返却する。
     * 返却される結果は、引数にて指定されたインデックスから指定された件数である。
     * 結果0件時は空リストが返却される。
     * @param <E>
     *            返却値の型
     * @param sqlID
     *            実行するSQLのID
     * @param bindParams
     *            SQLにバインドする値を格納したオブジェクト
     * @param beginIndex
     *            取得する開始インデックス
     * @param maxCount
     *            取得する件数
     * @return SQLの実行結果
     */
    @SuppressWarnings("unchecked")
    public <E> List<E> executeForObjectList(String sqlID, Object bindParams,
            int beginIndex, int maxCount) {

        if (log.isDebugEnabled()) {
            log.debug("executeForObjectList Start.");
        }

        // SqlMapClientの取得
        SqlMapClientTemplate sqlMapTemp = getSqlMapClientTemplate();

        // SQLの実行：値の取得
        List<E> list = sqlMapTemp.queryForList(sqlID, bindParams, beginIndex,
                maxCount);
        
        if (log.isDebugEnabled()) {
            log.debug("executeForObjectList End.");
        }

        return list;
    }    
    
    /**
     * 参照系SQLを実行し、MapのListとして返却する。
     * 返却される結果は、引数にて指定されたインデックスから指定された件数である。
     * 結果0件時は空リストが返却される。
     * @param sqlID
     *            実行するSQLのID
     * @param bindParams
     *            SQLにバインドする値を格納したオブジェクト
     * @param beginIndex
     *            取得する開始インデックス
     * @param maxCount
     *            取得する件数
     * @return SQLの実行結果
     */
    public List<Map<String, Object>> executeForMapList(String sqlID,
            Object bindParams, int beginIndex, int maxCount) {

        if (log.isDebugEnabled()) {
            log.debug("executeForMapList Start.");
        }

        List<Map<String, Object>> mapList = executeForObjectList(sqlID, bindParams,
                beginIndex, maxCount);

        if (log.isDebugEnabled()) {
            log.debug("executeForMapList End.");
        }

        return mapList;
    }
    
}