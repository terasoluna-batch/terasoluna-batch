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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapSession;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.execution.BatchException;

/**
 * {@link UpdateDAOiBatisImpl}の試験のために使用されるスタブ。
 *
 * {@link UpdateDAOiBatisImpl}からの呼び出し確認用に使用される。
 *
 */
public class UpdateDAOiBatisImple_SqlMapSessionImpl implements SqlMapSession {

    /*
     * 呼び出し確認用変数
     */
    protected boolean startBatchCalled = false;
    protected boolean updateCalled = false;
    protected boolean executeBatchCalled = false;
    private List<String> id = new ArrayList<String>();
    private List<Object> param = new ArrayList<Object>();

    public UpdateDAOiBatisImple_SqlMapSessionImpl() {
        startBatchCalled = false;
        updateCalled = false;
        executeBatchCalled = false;
        id = new ArrayList<String>();
        param = new ArrayList<Object>();
    }

    public List<String> getId() {
        return id;
    }

    public List<Object> getParam() {
        return param;
    }

    public boolean isExecuteBatchCalled() {
        return executeBatchCalled;
    }

    public boolean isStartBatchCalled() {
        return startBatchCalled;
    }

    public boolean isUpdateCalled() {
        return updateCalled;
    }

    public int update(String id, Object parameterObject) throws SQLException {
        updateCalled = true;
        this.id.add(id);
        this.param.add(parameterObject);
        return 1;
    }

    public void startBatch() throws SQLException {
        startBatchCalled = true;
    }

    public int executeBatch() throws SQLException {
        executeBatchCalled = true;
        return 1;
    }


    public void close() {
    }

    public Object insert(String id, Object parameterObject) throws SQLException {
        return null;
    }

    public int delete(String id, Object parameterObject) throws SQLException {
        return 0;
    }

    public Object queryForObject(String id, Object parameterObject)
            throws SQLException {
        return null;
    }

    public Object queryForObject(String id, Object parameterObject,
            Object resultObject) throws SQLException {
        return null;
    }

    public List queryForList(String id, Object parameterObject)
            throws SQLException {
        return null;
    }

    public List queryForList(String id, Object parameterObject, int skip,
            int max) throws SQLException {
        return null;
    }

    public void queryWithRowHandler(String id, Object parameterObject,
            RowHandler rowHandler) throws SQLException {
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public PaginatedList queryForPaginatedList(String id,
            Object parameterObject, int pageSize) throws SQLException {
        return null;
    }

    public Map queryForMap(String id, Object parameterObject, String keyProp)
            throws SQLException {
        return null;
    }

    public Map queryForMap(String id, Object parameterObject, String keyProp,
            String valueProp) throws SQLException {
        return null;
    }

    public void startTransaction() throws SQLException {
    }

    public void startTransaction(int transactionIsolation) throws SQLException {
    }

    public void commitTransaction() throws SQLException {
    }

    public void endTransaction() throws SQLException {
    }

    public void setUserConnection(Connection connnection) throws SQLException {
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public Connection getUserConnection() throws SQLException {
        return null;
    }

    public Connection getCurrentConnection() throws SQLException {
        return null;
    }

    public DataSource getDataSource() {
        return null;
    }

    public int delete(String id) throws SQLException {
        return 0;
    }

    public List executeBatchDetailed() throws SQLException, BatchException {
        return null;
    }

    public Object insert(String id) throws SQLException {
        return null;
    }

    public List queryForList(String id) throws SQLException {
        return null;
    }

    public List queryForList(String id, int skip, int max) throws SQLException {
        return null;
    }

    public Object queryForObject(String id) throws SQLException {
        return null;
    }
    
    @SuppressWarnings("deprecation")
    @Deprecated    
    public PaginatedList queryForPaginatedList(String id, int pageSize) throws SQLException {
        return null;
    }

    public void queryWithRowHandler(String id, RowHandler rowHandler) throws SQLException {
    }

    public int update(String id) throws SQLException {
        return 0;
    }

}
