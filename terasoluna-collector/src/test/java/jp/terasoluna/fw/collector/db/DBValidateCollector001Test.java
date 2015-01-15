/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import java.util.List;

import jp.terasoluna.fw.collector.dao.UserListQueryRowHandleDao;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.collector.validate.ValidationErrorHandler;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;

import jp.terasoluna.utlib.UTUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

/**
 * DBValidateCollectorTest
 */
public class DBValidateCollector001Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory
            .getLog(DBValidateCollector001Test.class);

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
     * @throws Exception
     */
    public void testDBValidateCollector001() throws Exception {
        String methodName = null;
        Object bindParams = null;
        Validator validator = null;

        DBValidateCollector<UserBean> dbc = new DBValidateCollector<UserBean>(
                userListQueryRowHandleDao, methodName, bindParams, validator);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDBValidateCollector002() throws Exception {
        String methodName = null;
        Object bindParams = null;
        Validator validator = null;
        ValidationErrorHandler validationErrorHandler = null;

        DBValidateCollector<UserBean> dbc = new DBValidateCollector<UserBean>(
                userListQueryRowHandleDao, methodName, bindParams, validator,
                validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDBValidateCollector003() throws Exception {
        String methodName = null;
        Object bindParams = null;
        boolean relation1n = false;
        Validator validator = null;
        ValidationErrorHandler validationErrorHandler = null;

        DBValidateCollector<UserBean> dbc = new DBValidateCollector<UserBean>(
                userListQueryRowHandleDao, methodName, bindParams, relation1n, validator,
                validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDBValidateCollector004() throws Exception {
        String methodName = null;
        Object bindParams = null;
        int queueSize = 10;
        Validator validator = null;
        ValidationErrorHandler validationErrorHandler = null;

        DBValidateCollector<UserBean> dbc = new DBValidateCollector<UserBean>(
                userListQueryRowHandleDao, methodName, bindParams, queueSize, validator,
                validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDBValidateCollector005() throws Exception {
        String methodName = null;
        Object bindParams = null;
        int queueSize = 10;
        Validator validator = null;
        CollectorExceptionHandler exceptionHandler = null;

        DBValidateCollector<UserBean> dbc = new DBValidateCollector<UserBean>(
                userListQueryRowHandleDao, methodName, bindParams, queueSize,
                exceptionHandler, validator);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDBValidateCollector006() throws Exception {
        String methodName = null;
        Object bindParams = null;
        int queueSize = 10;
        Validator validator = null;
        ValidationErrorHandler validationErrorHandler = null;
        CollectorExceptionHandler exceptionHandler = null;

        DBValidateCollector<UserBean> dbc = new DBValidateCollector<UserBean>(
                userListQueryRowHandleDao, methodName, bindParams, queueSize,
                exceptionHandler, validator, validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDBValidateCollector007() throws Exception {
        String methodName = null;
        Object bindParams = null;
        int queueSize = 10;
        boolean relation1n = false;
        Validator validator = null;
        CollectorExceptionHandler exceptionHandler = null;

        DBValidateCollector<UserBean> dbc = new DBValidateCollector<UserBean>(
                userListQueryRowHandleDao, methodName, bindParams, queueSize, relation1n,
                exceptionHandler, null, validator);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDBValidateCollector010() throws Exception {
        String methodName = null;
        Object bindParams = null;
        Validator validator = new ValidatorStub();
        ValidationErrorHandler validationErrorHandler = new ValidationErrorHandlerStub();

        DBValidateCollector<UserBean> dbc = new DBValidateCollector<UserBean>(
                userListQueryRowHandleDao, methodName, bindParams, validator,
                validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * @throws Exception
     */
    public void testDBValidateCollector011() throws Exception {
        String methodName = null;
        Object bindParams = null;
        Validator validator = new ValidatorStub();
        ValidationErrorHandler validationErrorHandler = null;

        DBValidateCollector<UserBean> dbc = new DBValidateCollector<UserBean>(
                userListQueryRowHandleDao, methodName, bindParams, validator,
                validationErrorHandler);

        assertNotNull(dbc);
        dbc.close();
    }

    /**
     * testDBValidateCollector012
     * @throws Exception
     */
    public void testDBValidateCollector012() throws Exception {
        DBCollectorConfig config = null;

        @SuppressWarnings("unused")
        DBCollector<UserBean> dbc = null;
        try {
            dbc = new DBValidateCollector<UserBean>(config);
            fail("Ž¸”s");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
            assertEquals("The parameter is null.", e.getMessage());
        }
    }

    /**
     * testDBValidateCollector012
     * @throws Exception
     */
    public void testDBValidateCollector013() throws Exception {
        DBCollectorConfig config = new DBCollectorConfig(
                userListQueryRowHandleDao,
                null,
                null);
        config.setExecuteByConstructor(true);
        DBCollector<UserBean> dbc = new DBValidateCollector<UserBean>(config);
        assertTrue((Boolean) UTUtil.getPrivateField(dbc, "beginning"));
    }
}
