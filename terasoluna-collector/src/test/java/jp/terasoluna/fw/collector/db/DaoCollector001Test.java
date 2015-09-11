/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import jp.terasoluna.fw.collector.dao.UserListQueryResultHandleDao;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.exception.SystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.test.context.ContextConfiguration;
import jp.terasoluna.fw.collector.unit.testcase.junit4.DaoTestCaseJunit4;
import jp.terasoluna.fw.collector.unit.testcase.junit4.loader.DaoTestCaseContextLoader;

/**
 * DaoCollectorTest
 */
@ContextConfiguration(locations = {
        "classpath:jp/terasoluna/fw/collector/db/dataSource.xml" }, loader = DaoTestCaseContextLoader.class)
public class DaoCollector001Test extends DaoTestCaseJunit4 {

    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(DaoCollector001Test.class);

    private UserListQueryResultHandleDao userListQueryResultHandleDao = null;

    public void setUserListQueryResultHandleDao(
            UserListQueryResultHandleDao userListQueryResultHandleDao) {
        this.userListQueryResultHandleDao = userListQueryResultHandleDao;
    }

    @Before
    public void onSetUp() throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        System.gc();
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
    }

    @After
    public void onTearDown() throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        System.gc();
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
    }

    /**
     * testDaoCollector001
     * @throws Exception
     */
    @Test
    public void testDaoCollector001() throws Exception {
        DaoCollector<UserBean> dbc = new DaoCollector<UserBean>();

        assertNotNull(dbc);
    }

    /**
     * testDaoCollector002
     * @throws Exception
     */
    @Test
    public void testDaoCollector002() throws Exception {
        String methodName = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(userListQueryResultHandleDao, methodName, bindParams, exceptionHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * testDaoCollector003
     * @throws Exception
     */
    @Test
    public void testDaoCollector003() throws Exception {
        DaoCollectorConfig config = null;

        DaoCollector<UserBean> dbc = null;
        try {
            dbc = new DaoCollector<UserBean>(config);
            fail("å¤±æÉ÷");
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
    @Test
    public void testGetResultHandler001() throws Exception {
        String methodName = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(userListQueryResultHandleDao, methodName, bindParams, exceptionHandler);

        dbc.queueingResultHandlerClass = QueueingResultHandlerStub001.class;

        QueueingResultHandler drh = dbc.getResultHandler();

        dbc.close();
        assertNotNull(drh);
    }

    /**
     * testGetResultHandler002
     * @throws Exception
     */
    @Test
    public void testGetResultHandler002() throws Exception {
        String methodName = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(userListQueryResultHandleDao, methodName, bindParams, exceptionHandler);

        dbc.queueingResultHandlerClass = QueueingResultHandlerStub002.class;

        try {
            dbc.getResultHandler();
            fail("å¤±æÉ÷");
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
    @Test
    public void testGetResultHandler003() throws Exception {
        String methodName = null;
        Object bindParams = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(userListQueryResultHandleDao, methodName, bindParams, exceptionHandler);

        dbc.queueingResultHandlerClass = QueueingResultHandlerStub003.class;

        try {
            dbc.getResultHandler();
            fail("å¤±æÉ÷");
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
