/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import static org.junit.Assert.*;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandlerStatus;
import jp.terasoluna.fw.collector.validate.ValidateErrorStatus;
import jp.terasoluna.fw.collector.validate.ValidationErrorHandler;
import jp.terasoluna.fw.collector.vo.DataValueObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 */
public class DaoCollectorConfigTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#DaoCollectorConfig(Object, String, Object)} のためのテスト・メソッド。
     */
    @Test
    public void testDaoCollectorConfig001() {
        Object queryResultHandleDao = null;
        String methodName = null;
        Object bindParams = null;

        // テスト
        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        assertNotNull(config);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#DaoCollectorConfig(Object, String, Object)} のためのテスト・メソッド。
     */
    @Test
    public void testDaoCollectorConfig002() {
        Object queryResultHandleDao = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();

        // テスト
        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        assertNotNull(config);
        assertEquals(queryResultHandleDao, config.getQueryResultHandleDao());
        assertEquals(methodName, config.getMethodName());
        assertEquals(bindParams, config.getBindParams());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#addQueueSize(int)} のためのテスト・メソッド。
     */
    @Test
    public void testAddQueueSize001() {
        Object queryResultHandleDao = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        int queueSize = 123;

        // テスト
        config.addQueueSize(queueSize);

        assertNotNull(config);
        assertEquals(queryResultHandleDao, config.getQueryResultHandleDao());
        assertEquals(methodName, config.getMethodName());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(queueSize, config.getQueueSize());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#addExceptionHandler(jp.terasoluna.fw.collector.exception.CollectorExceptionHandler)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testAddExceptionHandler001() {
        Object queryResultHandleDao = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        CollectorExceptionHandler exceptionHandler = new CollectorExceptionHandler() {
            public CollectorExceptionHandlerStatus handleException(
                    DataValueObject dataValueObject) {
                return null;
            }
        };

        // テスト
        config.addExceptionHandler(exceptionHandler);

        assertNotNull(config);
        assertEquals(queryResultHandleDao, config.getQueryResultHandleDao());
        assertEquals(methodName, config.getMethodName());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(exceptionHandler, config.getExceptionHandler());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#addValidator(org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testAddValidator001() {
        Object queryResultHandleDao = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        Validator validator = new Validator() {
            public boolean supports(Class<?> clazz) {
                return false;
            }

            public void validate(Object target, Errors errors) {
            }
        };

        // テスト
        config.addValidator(validator);

        assertNotNull(config);
        assertEquals(queryResultHandleDao, config.getQueryResultHandleDao());
        assertEquals(methodName, config.getMethodName());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(validator, config.getValidator());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#addValidationErrorHandler(jp.terasoluna.fw.collector.validate.ValidationErrorHandler)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testAddValidationErrorHandler001() {
        Object queryResultHandleDao = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        ValidationErrorHandler validationErrorHandler = new ValidationErrorHandler() {
            public ValidateErrorStatus handleValidationError(
                    DataValueObject dataValueObject, Errors errors) {
                return null;
            }
        };

        // テスト
        config.addValidationErrorHandler(validationErrorHandler);

        assertNotNull(config);
        assertEquals(queryResultHandleDao, config.getQueryResultHandleDao());
        assertEquals(methodName, config.getMethodName());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(validationErrorHandler, config
                .getValidationErrorHandler());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#addRelation1n(boolean)} のためのテスト・メソッド。
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testAddRelation1n001() {
        Object queryResultHandleDao = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        boolean relation1n = true;

        // テスト
        config.addRelation1n(relation1n);

        assertNotNull(config);
        assertEquals(queryResultHandleDao, config.getQueryResultHandleDao());
        assertEquals(methodName, config.getMethodName());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(relation1n, config.isRelation1n());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#addDaoCollectorPrePostProcess(jp.terasoluna.fw.collector.db.DaoCollectorPrePostProcess)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testAddDaoCollectorPrePostProcess001() {
        Object queryResultHandleDao = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        DaoCollectorPrePostProcess daoCollectorPrePostProcess = new DaoCollectorPrePostProcess() {
            public <P> void postprocessComplete(DaoCollector<P> collector) {
            }

            public <P> DaoCollectorPrePostProcessStatus postprocessException(
                    DaoCollector<P> collector, Throwable throwable) {
                return null;
            }

            public <P> void preprocess(DaoCollector<P> collector) {
            }
        };

        // テスト
        config.addDaoCollectorPrePostProcess(daoCollectorPrePostProcess);

        assertNotNull(config);
        assertEquals(queryResultHandleDao, config.getQueryResultHandleDao());
        assertEquals(methodName, config.getMethodName());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(daoCollectorPrePostProcess, config
                .getDaoCollectorPrePostProcess());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#addExecuteByConstructor(boolean)} のためのテスト・メソッド。
     */
    @Test
    public void testAddExecuteByConstructor001() {
        Object queryResultHandleDao = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        boolean executeByConstructor = true;

        // テスト
        config.addExecuteByConstructor(executeByConstructor);

        assertNotNull(config);
        assertEquals(queryResultHandleDao, config.getQueryResultHandleDao());
        assertEquals(methodName, config.getMethodName());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(executeByConstructor, config.isExecuteByConstructor());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#getQueryResultHandleDao()} のためのテスト・メソッド。
     */
    @Test
    public void testGetQueryResultHandleDAO001() {
        Object queryResultHandleDao = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        // テスト
        Object result = config.getQueryResultHandleDao();

        assertNotNull(result);
        assertEquals(queryResultHandleDao, result);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#setQueryResultHandleDao(Object)} のためのテスト・メソッド。
     */
    @Test
    public void testSetQueryResultHandleDAO001() {
        Object queryResultHandleDao = new Object();
        Object queryResultHandleDao2 = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        // テスト
        config.setQueryResultHandleDao(queryResultHandleDao2);

        assertNotNull(config.getQueryResultHandleDao());
        assertEquals(queryResultHandleDao2, config.getQueryResultHandleDao());
    }

    /**
     * {@link DaoCollectorConfig#getMethodName()} のためのテスト・メソッド。
     */
    @Test
    public void testGetMethodName001() {
        Object queryResultHandleDao = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        // テスト
        String result = config.getMethodName();

        assertNotNull(result);
        assertEquals(methodName, result);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#setMethodName(String)} のためのテスト・メソッド。
     */
    @Test
    public void testSetMethodName001() {
        Object queryResultHandleDao = new Object();

        String methodName = "hoge";
        String methodName2 = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        // テスト
        config.setMethodName(methodName2);

        assertNotNull(config.getMethodName());
        assertEquals(methodName, config.getMethodName());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#getBindParams()} のためのテスト・メソッド。
     */
    @Test
    public void testGetBindParams001() {
        Object queryResultHandleDao = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        // テスト
        Object result = config.getBindParams();

        assertNotNull(result);
        assertEquals(bindParams, result);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#setBindParams(java.lang.Object)} のためのテスト・メソッド。
     */
    @Test
    public void testSetBindParams001() {
        Object queryResultHandleDao = new Object();

        String methodName = "hoge";
        Object bindParams = new Object();
        Object bindParams2 = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        // テスト
        config.setBindParams(bindParams2);

        assertNotNull(config.getBindParams());
        assertEquals(bindParams2, config.getBindParams());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#isRelation1n()} のためのテスト・メソッド。
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testIsRelation1n001() {
        Object queryResultHandleDao = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();
        boolean relation1n = true;

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);
        config.setRelation1n(relation1n);

        // テスト
        boolean result = config.isRelation1n();

        assertNotNull(result);
        assertEquals(relation1n, result);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#setRelation1n(boolean)} のためのテスト・メソッド。
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testSetRelation1n001() {
        Object queryResultHandleDao = new Object();

        String methodName = "hoge";
        Object bindParams = new Object();
        boolean relation1n = true;

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        // テスト
        config.setRelation1n(relation1n);

        assertNotNull(config.isRelation1n());
        assertEquals(relation1n, config.isRelation1n());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#getDaoCollectorPrePostProcess()} のためのテスト・メソッド。
     */
    @Test
    public void testGetDaoCollectorPrePostProcess001() {
        Object queryResultHandleDao = new Object();
        String methodName = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);
        DaoCollectorPrePostProcess daoCollectorPrePostProcess = new DaoCollectorPrePostProcess() {
            public <P> void postprocessComplete(DaoCollector<P> collector) {
            }

            public <P> DaoCollectorPrePostProcessStatus postprocessException(
                    DaoCollector<P> collector, Throwable throwable) {
                return null;
            }

            public <P> void preprocess(DaoCollector<P> collector) {
            }
        };
        config.setDaoCollectorPrePostProcess(daoCollectorPrePostProcess);

        // テスト
        DaoCollectorPrePostProcess result = config
                .getDaoCollectorPrePostProcess();

        assertNotNull(result);
        assertEquals(daoCollectorPrePostProcess, result);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollectorConfig#setDaoCollectorPrePostProcess(jp.terasoluna.fw.collector.db.DaoCollectorPrePostProcess)}
     * のためのテスト・メソッド。
     */
    @Test
    public void setDaoCollectorPrePostProcess() {
        Object queryResultHandleDao = new Object();

        String methodName = "hoge";
        Object bindParams = new Object();

        DaoCollectorConfig config = new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams);

        DaoCollectorPrePostProcess daoCollectorPrePostProcess = new DaoCollectorPrePostProcess() {
            public <P> void postprocessComplete(DaoCollector<P> collector) {
            }

            public <P> DaoCollectorPrePostProcessStatus postprocessException(
                    DaoCollector<P> collector, Throwable throwable) {
                return null;
            }

            public <P> void preprocess(DaoCollector<P> collector) {
            }
        };
        config.setDaoCollectorPrePostProcess(daoCollectorPrePostProcess);
        DaoCollectorPrePostProcess daoCollectorPrePostProcess2 = new DaoCollectorPrePostProcess() {
            public <P> void postprocessComplete(DaoCollector<P> collector) {
            }

            public <P> DaoCollectorPrePostProcessStatus postprocessException(
                    DaoCollector<P> collector, Throwable throwable) {
                return null;
            }

            public <P> void preprocess(DaoCollector<P> collector) {
            }
        };

        // テスト
        config.setDaoCollectorPrePostProcess(daoCollectorPrePostProcess2);

        assertNotNull(config.getDaoCollectorPrePostProcess());
        assertEquals(daoCollectorPrePostProcess2, config
                .getDaoCollectorPrePostProcess());
    }

}
