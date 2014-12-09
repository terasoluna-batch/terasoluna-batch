/**
 * 
 */
package jp.terasoluna.fw.dao.ibatis;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * {@link QueryRowHandleDAOiBatisImpl}の試験のために使用されるスタブ。
 * 
 * {@link QueryRowHandleDAOiBatisImpl}からの呼び出し確認用に使用される。
 * 
 */
public class QueryRowHandleDAOiBatisImpl_SqlMapClientTemplateStub01 extends
        SqlMapClientTemplate {
    /**
     * テスト用queryWithRowHandlerメソッド
     */
    @Override
    public void queryWithRowHandler(String statementName,
            Object parameterObject, RowHandler rowHandler)
            throws DataAccessException {
        called = true;
        this.statementName = statementName;
        this.parameterObject = parameterObject;
        rowHandler.handleRow(parameterObject);
    }

    /*
     * 呼び出し確認用変数
     */
    private boolean called = false;
    private String statementName = null;
    private Object parameterObject = null;
    public boolean isCalled() {
        return called;
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public String getStatementName() {
        return statementName;
    }

}
