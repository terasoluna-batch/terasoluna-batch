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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * {@link UpdateDAOiBatisImpl}の試験のために使用される。
 * 
 * {@link UpdateDAOiBatisImpl_SqlMapClientTemplateStub01}と
 * {@link UpdateDAOiBatisImpl_SqlMapClientTemplateStub02}
 * から使用される。
 * 
 */
public class UpdateDAOiBatisImpl_DataSourceStub01 implements DataSource {

    public Connection getConnection() throws SQLException {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    public Connection getConnection(String username, String password) throws SQLException {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    public PrintWriter getLogWriter() throws SQLException {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        // TODO 自動生成されたメソッド・スタブ
        
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        // TODO 自動生成されたメソッド・スタブ
        
    }

    public int getLoginTimeout() throws SQLException {
        // TODO 自動生成されたメソッド・スタブ
        return 0;
    }

    public Logger getParentLogger() {
        return null;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
