package jp.terasoluna.fw.batch.blogic;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

public class DataSourceTransactionManagerStub extends
                                             DataSourceTransactionManager {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3561888809374196716L;

    /**
     * DataSourceTransactionManagerStub
     */
    public DataSourceTransactionManagerStub() {
        DataSource dataSource = new DataSourceStub();
        setDataSource(dataSource);
    }
}
