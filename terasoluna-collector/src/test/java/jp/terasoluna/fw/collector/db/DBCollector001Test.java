/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import java.util.List;

import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.dao.QueryRowHandleDAO;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;
import jp.terasoluna.fw.exception.SystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DBCollectorTest
 */
public class DBCollector001Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(DBCollector001Test.class);

    private QueryRowHandleDAO queryRowHandleDAO = null;

    @Override
    protected void addConfigLocations(List<String> configLocations) {
        configLocations.add("jp/terasoluna/fw/collector/db/dataSource.xml");
    }

    public void setQueryRowHandleDAO(QueryRowHandleDAO queryRowHandleDAO) {
        this.queryRowHandleDAO = queryRowHandleDAO;
    }

    @Override
    protected void onSetUp() throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        System.gc();
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        super.onSetUp();
    }

    @Override
    protected void onTearDown() throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        System.gc();
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        super.onTearDown();
    }

    /**
     * testDBCollector001
     * @throws Exception
     */
    public void testDBCollector001() throws Exception {
        DBCollector<UserBean> dbc = new DBCollector<UserBean>();

        assertNotNull(dbc);
    }

    /**
     * testDBCollector002
     * @throws Exception
     */
    public void testDBCollector002() throws Exception {
        String sqlID = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DBCollector<UserBean> dbc = new DBCollector<UserBean>(
                queryRowHandleDAO, sqlID, bindParams, exceptionHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * testDBCollector003
     * @throws Exception
     */
    public void testDBCollector003() throws Exception {
        DBCollectorConfig config = null;

        @SuppressWarnings("unused")
        DBCollector<UserBean> dbc = null;
        try {
            dbc = new DBCollector<UserBean>(config);
            fail("Ž¸”s");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
            assertEquals("The parameter is null.", e.getMessage());
        }
    }

    /**
     * testGetDataRowHandler001
     * @throws Exception
     */
    public void testGetDataRowHandler001() throws Exception {
        String sqlID = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DBCollector<UserBean> dbc = new DBCollector<UserBean>(
                queryRowHandleDAO, sqlID, bindParams, exceptionHandler);

        dbc.queueingDataRowHandlerClass = QueueingDataRowHandlerStub001.class;

        QueueingDataRowHandler drh = dbc.getDataRowHandler();

        assertNotNull(drh);
    }

    /**
     * testGetDataRowHandler002
     * @throws Exception
     */
    public void testGetDataRowHandler002() throws Exception {
        String sqlID = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DBCollector<UserBean> dbc = new DBCollector<UserBean>(
                queryRowHandleDAO, sqlID, bindParams, exceptionHandler);

        dbc.queueingDataRowHandlerClass = QueueingDataRowHandlerStub002.class;

        @SuppressWarnings("unused")
        QueueingDataRowHandler drh = null;
        try {
            drh = dbc.getDataRowHandler();
            fail("Ž¸”s");
        } catch (SystemException e) {
            assertNotNull(e);
            assertEquals(SystemException.class, e.getClass());
            assertNotNull(e.getCause());
            assertEquals(InstantiationException.class, e.getCause().getClass());
        }
    }

    /**
     * testGetDataRowHandler003
     * @throws Exception
     */
    public void testGetDataRowHandler003() throws Exception {
        String sqlID = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DBCollector<UserBean> dbc = new DBCollector<UserBean>(
                queryRowHandleDAO, sqlID, bindParams, exceptionHandler);

        dbc.queueingDataRowHandlerClass = QueueingDataRowHandlerStub003.class;

        @SuppressWarnings("unused")
        QueueingDataRowHandler drh = null;
        try {
            drh = dbc.getDataRowHandler();
            fail("Ž¸”s");
        } catch (SystemException e) {
            assertNotNull(e);
            assertEquals(SystemException.class, e.getClass());
            assertNotNull(e.getCause());
            assertEquals(IllegalAccessException.class, e.getCause().getClass());
        }
    }

}
