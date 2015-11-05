/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import jp.terasoluna.fw.collector.dao.UserListQueryResultHandleDao;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.collector.validate.ValidationErrorHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.Validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.test.context.ContextConfiguration;
import jp.terasoluna.fw.collector.unit.testcase.junit4.DaoTestCaseJunit4;
import jp.terasoluna.fw.collector.unit.testcase.junit4.loader.DaoTestCaseContextLoader;

/**
 * DaoValidateCollectorTest
 */
@ContextConfiguration(locations = {
"classpath:jp/terasoluna/fw/collector/db/dataSource.xml" }, loader = DaoTestCaseContextLoader.class)
public class DaoValidateCollector001Test extends DaoTestCaseJunit4 {

    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(
            DaoValidateCollector001Test.class);

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
     * @throws Exception
     */
    @Test
    public void testDaoValidateCollector001() throws Exception {
        String methodName = null;
        Object bindParams = null;
        Validator validator = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(userListQueryResultHandleDao, methodName, bindParams, validator);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testDaoValidateCollector002() throws Exception {
        String methodName = null;
        Object bindParams = null;
        Validator validator = null;
        ValidationErrorHandler validationErrorHandler = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(userListQueryResultHandleDao, methodName, bindParams, validator, validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testDaoValidateCollector003() throws Exception {
        String methodName = null;
        Object bindParams = null;
        boolean relation1n = false;
        Validator validator = null;
        ValidationErrorHandler validationErrorHandler = null;

        @SuppressWarnings("deprecation")
        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(userListQueryResultHandleDao, methodName, bindParams, relation1n, validator, validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testDaoValidateCollector004() throws Exception {
        String methodName = null;
        Object bindParams = null;
        int queueSize = 10;
        Validator validator = null;
        ValidationErrorHandler validationErrorHandler = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(userListQueryResultHandleDao, methodName, bindParams, queueSize, validator, validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testDaoValidateCollector005() throws Exception {
        String methodName = null;
        Object bindParams = null;
        int queueSize = 10;
        Validator validator = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(userListQueryResultHandleDao, methodName, bindParams, queueSize, exceptionHandler, validator);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testDaoValidateCollector006() throws Exception {
        String methodName = null;
        Object bindParams = null;
        int queueSize = 10;
        Validator validator = null;
        ValidationErrorHandler validationErrorHandler = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(userListQueryResultHandleDao, methodName, bindParams, queueSize, exceptionHandler, validator, validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testDaoValidateCollector007() throws Exception {
        String methodName = null;
        Object bindParams = null;
        int queueSize = 10;
        boolean relation1n = false;
        Validator validator = null;
        CollectorExceptionHandler exceptionHandler = null;

        @SuppressWarnings("deprecation")
        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(userListQueryResultHandleDao, methodName, bindParams, queueSize, relation1n, exceptionHandler, null, validator);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testDaoValidateCollector010() throws Exception {
        String methodName = null;
        Object bindParams = null;
        Validator validator = new ValidatorStub();
        ValidationErrorHandler validationErrorHandler = new ValidationErrorHandlerStub();

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(userListQueryResultHandleDao, methodName, bindParams, validator, validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testDaoValidateCollector011() throws Exception {
        String methodName = null;
        Object bindParams = null;
        Validator validator = new ValidatorStub();
        ValidationErrorHandler validationErrorHandler = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(userListQueryResultHandleDao, methodName, bindParams, validator, validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * testDaoValidateCollector012
     * @throws Exception
     */
    @SuppressWarnings("resource")
    @Test
    public void testDaoValidateCollector012() throws Exception {
        DaoCollectorConfig config = null;

        try {
            new DaoValidateCollector<UserBean>(config);
            fail("失���");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
            assertEquals("The parameter is null.", e.getMessage());
        }
    }

    /**
     * testDaoValidateCollector012
     * @throws Exception
     */
    @Test
    public void testDaoValidateCollector013() throws Exception {
        DaoCollectorConfig config = new DaoCollectorConfig(userListQueryResultHandleDao, null, null);
        config.setExecuteByConstructor(true);
        DaoCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(config);
        assertTrue((Boolean) ReflectionTestUtils.getField(dbc, "beginning"));
    }
}
