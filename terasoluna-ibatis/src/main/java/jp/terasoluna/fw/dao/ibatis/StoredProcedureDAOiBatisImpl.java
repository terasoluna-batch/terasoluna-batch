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

import jp.terasoluna.fw.dao.StoredProcedureDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * StoredProcedureDAOインタフェースのiBATIS用実装クラス。
 *<p/>
 * このクラスは、Bean定義ファイルにBean定義を行いサービス層に
 * インジェクションして使用する。以下に設定例および実装例を示す。<br/>
 *
 * <br/>
 * <fieldset style="border:1pt solid black;padding:10px;width:100%;">
 * <legend>
 * Bean定義ファイルの例
 * </legend>
 * <p/>
 * <code>
 * &lt;bean id="listBLogic"
 * class="jp.strutspring.blogic.ListBLogic"&gt;<br/>
 * &nbsp;&nbsp;&lt;property name="dao"&gt;&lt;ref
 * local="<b>spDAO</b>"/&gt;&lt;/property&gt;<br/>
 * &lt;/bean&gt;<br/>
 * <br/>
 * &lt;bean id="<b>spDAO</b>"<br/>
 * &nbsp;&nbsp;class="<b>jp.terasoluna.
 * fw.dao.ibatis.StoredProcedureDAOiBatisImpl</b>"&gt;<br/>
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
 * </fieldset>
 * <p/>
 * <fieldset style="border:1pt solid black;padding:10px;width:100%;">
 * <legend>
 * サービス層での使用方法
 * </legend>
 * <p/>
 * <code>
 * public class UserGetBLogic{<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;private StoredProcedureDAO dao = null;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;public StoredProcedureDAO getDao() {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return dao;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;public void setDao(StoredProcedureDAO dao) {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;this.dao = dao;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;public String execute(ActionForm form) {<br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * UserBean<String, String> userBean = new UserBean<String, String>();<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * userBean.setInputId("1");<br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * <b>dao.executeForObject("getUserName", userBean);</b><br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * String userName = userBean.getName();<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return "success";<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
 * }
 * </code>
 * </fieldset>
 * <p/>
 * <fieldset style="border:1pt solid black;padding:10px;width:100%;">
 * <legend>
 * iBATISの設定例
 * </legend>
 * <p/>
 * <code>
 * &lt;parameterMap id="UserBean"
 * class="jp.strutspring.blogic.UserBean"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;parameter property="inputId"
 * javaType="java.lang.String" mode="IN"/&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;parameter property="name"
 * javaType="java.lang.String" mode="OUT"/&gt;<br/>
 * &lt;/parameterMap&gt;<br/>
 * &lt;procedure id="getUserName" parameterMap="UserBean"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;{call TESTPROCEDURE(?,?)}<br/>
 * &lt;/procedure&gt;
 * </code>
 * </fieldset>
 * <p/>
 */
public class StoredProcedureDAOiBatisImpl extends SqlMapClientDaoSupport
        implements StoredProcedureDAO {

    /**
     * ログインスタンス
     */
    private static Log log = LogFactory
            .getLog(StoredProcedureDAOiBatisImpl.class);

    /**
     * 指定されたSQLIDのストアドプロシージャーを実行する。
     * ストアドプロシージャーの結果であるアウトパラメータは、
     * 引数のbindParamsに反映される。
     *
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     */
    public void executeForObject(String sqlID, Object bindParams) {

        if (log.isDebugEnabled()) {
            log.debug("executeForObject Start.");
        }

        //SqlMapClientの取得
        SqlMapClientTemplate sqlMapTemp = getSqlMapClientTemplate();

        //SQLの実行：値の取得
        sqlMapTemp.queryForObject(sqlID, bindParams);

        if (log.isDebugEnabled()) {
            log.debug("executeForObject End.");
        }

    }

}
