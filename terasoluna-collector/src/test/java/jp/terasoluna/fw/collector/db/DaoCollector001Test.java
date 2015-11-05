/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import java.util.List;

import jp.terasoluna.fw.collector.dao.UserListQueryResultHandleDao;
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

    private UserListQueryResultHandleDao userListQueryResultHandleDao = null;

    @Override
    protected void addConfigLocations(List<String> configLocations) {
        configLocations.add("jp/terasoluna/fw/collector/db/dataSource.xml");
    }

    public void setUserListQueryResultHandleDao(UserListQueryResultHandleDao userListQueryResultHandleDao) {
        this.userListQueryResultHandleDao = userListQueryResultHandleDao;
    }

    @SuppressWarnings("deprecation")
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
                userListQueryResultHandleDao, methodName, bindParams, exceptionHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * testDaoCollector003
     * @throws Exception
     */
    public void testDaoCollector003() throws Exception {
        DaoCollectorConfig config = null;

        DaoCollector<UserBean> dbc = null;
        try {
            dbc = new DaoCollector<UserBean>(config);
            fail("失敗");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
            assertEquals("The parameter is null.", e.getMessage());
        } finally {
            if (dbc != null) {
                dbc.close();
            }
        }
    }

    /**
     * testGetResultHandler001
     * @throws Exception
     */
    public void testGetResultHandler001() throws Exception {
        String methodName = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(
                userListQueryResultHandleDao, methodName, bindParams, exceptionHandler);

        dbc.queueingResultHandlerClass = QueueingResultHandlerStub001.class;

        QueueingResultHandler drh = dbc.getResultHandler();

        dbc.close();
        assertNotNull(drh);
    }

    /**
     * testGetResultHandler002
     * @throws Exception
     */
    public void testGetResultHandler002() throws Exception {
        String methodName = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(
                userListQueryResultHandleDao, methodName, bindParams, exceptionHandler);

        dbc.queueingResultHandlerClass = QueueingResultHandlerStub002.class;

        try {
            dbc.getResultHandler();
            fail("失敗");
        } catch (SystemException e) {
            assertNotNull(e);
            assertEquals(SystemException.class, e.getClass());
            assertNotNull(e.getCause());
            assertEquals(InstantiationException.class, e.getCause().getClass());
        } finally {
            dbc.close();
        }
    }

    /**
     * testGetResultHandler003
     * @throws Exception
     */
    public void testGetResultHandler003() throws Exception {
        String methodName = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(
                userListQueryResultHandleDao, methodName, bindParams, exceptionHandler);

        dbc.queueingResultHandlerClass = QueueingResultHandlerStub003.class;

        try {
            dbc.getResultHandler();
            fail("失敗");
        } catch (SystemException e) {
            assertNotNull(e);
            assertEquals(SystemException.class, e.getClass());
            assertNotNull(e.getCause());
            assertEquals(IllegalAccessException.class, e.getCause().getClass());
        } finally {
            dbc.close();
        }
    }

}
