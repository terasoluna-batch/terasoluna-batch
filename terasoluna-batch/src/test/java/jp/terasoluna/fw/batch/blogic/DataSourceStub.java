package jp.terasoluna.fw.batch.blogic;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class DataSourceStub implements DataSource {

	private Connection createConnectionStub(){
        Connection conn = (Connection) Proxy.newProxyInstance(DataSourceStub.class.getClassLoader(), new Class[]{ Connection.class }, new InvocationHandler(){

            public Object invoke(Object proxy, Method method, Object[] args)
                                                                            throws Throwable {
                if (method.getReturnType() == boolean.class) {
                    return Boolean.FALSE;
                }
                if (method.getReturnType() == int.class) {
                    return 0;
                }
                if ("rollback".equals(method.getName())) {
                    throw new SQLException("abcd");
                }
                return null;
            }
        });
        return conn;
	}
    public Connection getConnection() throws SQLException {
        return createConnectionStub();
    }

    public Connection getConnection(String username, String password)
                                                                     throws SQLException {
        return createConnectionStub();
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public Logger getParentLogger() {
        return null;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    public void setLoginTimeout(int seconds) throws SQLException {
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

}
