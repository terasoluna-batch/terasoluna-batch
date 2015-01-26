/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import java.util.List;

import jp.terasoluna.fw.collector.dao.UserListQueryRowHandleDao;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;
import jp.terasoluna.fw.exception.SystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DaoCollectorTest
 */
public class DaoCollector001Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(DaoCollector001Test.class);

    private UserListQueryRowHandleDao userListQueryRowHandleDao = null;

    @Override
    protected void addConfigLocations(List<String> configLocations) {
        configLocations.add("jp/terasoluna/fw/collector/db/dataSource.xml");
    }

    public void setUserListQueryRowHandleDao(UserListQueryRowHandleDao userListQueryRowHandleDao) {
        this.userListQueryRowHandleDao = userListQueryRowHandleDao;
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
     * testDaoCollector001
     * @throws Exception
     */
    public void testDaoCollector001() throws Exception {
        DaoCollector<UserBean> dbc = new DaoCollector<UserBean>();

        assertNotNull(dbc);
    }

    /**
     * testDaoCollector002
     * @throws Exception
     */
    public void testDaoCollector002() throws Exception {
        String methodName = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(
                userListQueryRowHandleDao, methodName, bindParams, exceptionHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * testDaoCollector003
     * @throws Exception
     */
    public void testDaoCollector003() throws Exception {
        DaoCollectorConfig config = null;

        @SuppressWarnings("unused")
        DaoCollector<UserBean> dbc = null;
        try {
            dbc = new DaoCollector<UserBean>(config);
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
        String methodName = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(
                userListQueryRowHandleDao, methodName, bindParams, exceptionHandler);

        dbc.queueingDataRowHandlerClass = QueueingDataRowHandlerStub001.class;

        QueueingDataRowHandler drh = dbc.getDataRowHandler();

        assertNotNull(drh);
    }

    /**
     * testGetDataRowHandler002
     * @throws Exception
     */
    public void testGetDataRowHandler002() throws Exception {
        String methodName = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(
                userListQueryRowHandleDao, methodName, bindParams, exceptionHandler);

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
        String methodName = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(
                userListQueryRowHandleDao, methodName, bindParams, exceptionHandler);

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
