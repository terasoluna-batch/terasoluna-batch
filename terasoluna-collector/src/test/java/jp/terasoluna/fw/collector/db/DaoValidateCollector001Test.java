/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import java.util.List;

import jp.terasoluna.fw.collector.dao.UserListQueryResultHandleDao;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.collector.validate.ValidationErrorHandler;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;

import jp.terasoluna.utlib.UTUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

/**
 * DaoValidateCollectorTest
 */
public class DaoValidateCollector001Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory
            .getLog(DaoValidateCollector001Test.class);

    private UserListQueryResultHandleDao userListQueryResultHandleDao = null;

    @Override
    protected void addConfigLocations(List<String> configLocations) {
        configLocations.add("jp/terasoluna/fw/collector/db/dataSource.xml");
    }

    public void setUserListQueryResultHandleDao(UserListQueryResultHandleDao userListQueryResultHandleDao) {
        this.userListQueryResultHandleDao = userListQueryResultHandleDao;
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
     * @throws Exception
     */
    public void testDaoValidateCollector001() throws Exception {
        String methodName = null;
        Object bindParams = null;
        Validator validator = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(
                userListQueryResultHandleDao, methodName, bindParams, validator);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDaoValidateCollector002() throws Exception {
        String methodName = null;
        Object bindParams = null;
        Validator validator = null;
        ValidationErrorHandler validationErrorHandler = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(
                userListQueryResultHandleDao, methodName, bindParams, validator,
                validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDaoValidateCollector003() throws Exception {
        String methodName = null;
        Object bindParams = null;
        boolean relation1n = false;
        Validator validator = null;
        ValidationErrorHandler validationErrorHandler = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(
                userListQueryResultHandleDao, methodName, bindParams, relation1n, validator,
                validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDaoValidateCollector004() throws Exception {
        String methodName = null;
        Object bindParams = null;
        int queueSize = 10;
        Validator validator = null;
        ValidationErrorHandler validationErrorHandler = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(
                userListQueryResultHandleDao, methodName, bindParams, queueSize, validator,
                validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDaoValidateCollector005() throws Exception {
        String methodName = null;
        Object bindParams = null;
        int queueSize = 10;
        Validator validator = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(
                userListQueryResultHandleDao, methodName, bindParams, queueSize,
                exceptionHandler, validator);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDaoValidateCollector006() throws Exception {
        String methodName = null;
        Object bindParams = null;
        int queueSize = 10;
        Validator validator = null;
        ValidationErrorHandler validationErrorHandler = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(
                userListQueryResultHandleDao, methodName, bindParams, queueSize,
                exceptionHandler, validator, validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDaoValidateCollector007() throws Exception {
        String methodName = null;
        Object bindParams = null;
        int queueSize = 10;
        boolean relation1n = false;
        Validator validator = null;
        CollectorExceptionHandler exceptionHandler = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(
                userListQueryResultHandleDao, methodName, bindParams, queueSize, relation1n,
                exceptionHandler, null, validator);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDaoValidateCollector010() throws Exception {
        String methodName = null;
        Object bindParams = null;
        Validator validator = new ValidatorStub();
        ValidationErrorHandler validationErrorHandler = new ValidationErrorHandlerStub();

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(
                userListQueryResultHandleDao, methodName, bindParams, validator,
                validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDaoValidateCollector011() throws Exception {
        String methodName = null;
        Object bindParams = null;
        Validator validator = new ValidatorStub();
        ValidationErrorHandler validationErrorHandler = null;

        DaoValidateCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(
                userListQueryResultHandleDao, methodName, bindParams, validator,
                validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * testDaoValidateCollector012
     * @throws Exception
     */
    public void testDaoValidateCollector012() throws Exception {
        DaoCollectorConfig config = null;

        @SuppressWarnings("unused")
        DaoCollector<UserBean> dbc = null;
        try {
            dbc = new DaoValidateCollector<UserBean>(config);
            fail("Ž¸”s");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
            assertEquals("The parameter is null.", e.getMessage());
        }
    }

    /**
     * testDaoValidateCollector012
     * @throws Exception
     */
    public void testDaoValidateCollector013() throws Exception {
        DaoCollectorConfig config = new DaoCollectorConfig(
                userListQueryResultHandleDao,
                null,
                null);
        config.setExecuteByConstructor(true);
        DaoCollector<UserBean> dbc = new DaoValidateCollector<UserBean>(config);
        assertTrue((Boolean) UTUtil.getPrivateField(dbc, "beginning"));
    }
}
