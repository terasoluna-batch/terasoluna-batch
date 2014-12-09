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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * {@link QueryDAOiBatisImpl}の試験のために使用されるスタブ。
 * 
 * {@link QueryDAOiBatisImpl}からの呼び出し確認用に使用される。
 * 
 */
public class QueryDAOiBatisImpl_SqlMapClientTemplateStub01 extends SqlMapClientTemplate {

    /**
     * コンストラクタ
     */
    public QueryDAOiBatisImpl_SqlMapClientTemplateStub01() {

        //SqlMapClientの設定
        SqlMapClient sqlMapClient = new QueryDAOiBatisImpl_SqlMapClientStub01();
        setSqlMapClient(sqlMapClient);

        //データソースの設定
        DataSource dataSource = new QueryDAOiBatisImpl_DataSourceStub01();
        setDataSource(dataSource);
    }

    /**
     * QueryDAOiBatisImplテスト用queryForObjectメソッド
     */
    @Override
    public Object queryForObject(String statementName, Object parameterObject) throws DataAccessException {
        called = true;
        this.statementName = statementName;
        this.parameterObject = parameterObject;
        return "abc";
    }

    /**
     * QueryDAOiBatisImplテスト用queryForMapメソッド
     */
    @Override
    public Map<String, Object> queryForMap(String statementName, Object parameterObject, String keyProperty) throws DataAccessException {
        called = true;
        this.statementName = statementName;
        this.parameterObject = parameterObject;
        this.keyProperty = keyProperty;
        return new HashMap<String, Object>();
    }

   /**
     * QueryDAOiBatisImplテスト用queryForListメソッド
     */
    @Override
    public List queryForList(String statementName, Object parameterObject) throws DataAccessException {
        called = true;
        this.statementName = statementName;
        this.parameterObject = parameterObject;
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        return list;
    }

    /**
      * QueryDAOiBatisImplテスト用queryForListメソッド
      */
    @Override
    public List queryForList(String statementName, Object parameterObject, int skipResults, int maxResults) throws DataAccessException {
        called = true;
        this.statementName = statementName;
        this.parameterObject = parameterObject;
        this.skipResults = skipResults;
        this.maxResults = maxResults;
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        return list;
    }

    /*
     * 呼び出し確認用変数
     */
    private boolean called = false;
    private String statementName = null;
    private Object parameterObject = null;
    private String keyProperty = "";
    private int skipResults = 0;
    private int maxResults = 0;

    public boolean isCalled() {
        return called;
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public String getStatementName() {
        return statementName;
    }

    public String getKeyProperty() {
        return keyProperty;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public int getSkipResults() {
        return skipResults;
    }

}
