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
import jp.terasoluna.fw.dao.QueryRowHandleDAO;
import jp.terasoluna.fw.dao.event.DataRowHandler;

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
public class DBCollectorConfigTest {

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
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#DBCollectorConfig(jp.terasoluna.fw.dao.QueryRowHandleDAO, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testDBCollectorConfig001() {
        QueryRowHandleDAO queryRowHandleDAO = null;
        String sqlID = null;
        Object bindParams = null;

        // テスト
        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        assertNotNull(config);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#DBCollectorConfig(jp.terasoluna.fw.dao.QueryRowHandleDAO, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testDBCollectorConfig002() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();

        // テスト
        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        assertNotNull(config);
        assertEquals(queryRowHandleDAO, config.getQueryRowHandleDAO());
        assertEquals(sqlID, config.getSqlID());
        assertEquals(bindParams, config.getBindParams());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#addQueueSize(int)} のためのテスト・メソッド。
     */
    @Test
    public void testAddQueueSize001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        int queueSize = 123;

        // テスト
        config.addQueueSize(queueSize);

        assertNotNull(config);
        assertEquals(queryRowHandleDAO, config.getQueryRowHandleDAO());
        assertEquals(sqlID, config.getSqlID());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(queueSize, config.getQueueSize());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#addExceptionHandler(jp.terasoluna.fw.collector.exception.CollectorExceptionHandler)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testAddExceptionHandler001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        CollectorExceptionHandler exceptionHandler = new CollectorExceptionHandler() {
            public CollectorExceptionHandlerStatus handleException(
                    DataValueObject dataValueObject) {
                return null;
            }
        };

        // テスト
        config.addExceptionHandler(exceptionHandler);

        assertNotNull(config);
        assertEquals(queryRowHandleDAO, config.getQueryRowHandleDAO());
        assertEquals(sqlID, config.getSqlID());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(exceptionHandler, config.getExceptionHandler());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#addValidator(org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testAddValidator001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        Validator validator = new Validator() {
            @SuppressWarnings("unchecked")
            public boolean supports(Class clazz) {
                return false;
            }

            public void validate(Object target, Errors errors) {
            }
        };

        // テスト
        config.addValidator(validator);

        assertNotNull(config);
        assertEquals(queryRowHandleDAO, config.getQueryRowHandleDAO());
        assertEquals(sqlID, config.getSqlID());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(validator, config.getValidator());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#addValidationErrorHandler(jp.terasoluna.fw.collector.validate.ValidationErrorHandler)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testAddValidationErrorHandler001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        ValidationErrorHandler validationErrorHandler = new ValidationErrorHandler() {
            public ValidateErrorStatus handleValidationError(
                    DataValueObject dataValueObject, Errors errors) {
                return null;
            }
        };

        // テスト
        config.addValidationErrorHandler(validationErrorHandler);

        assertNotNull(config);
        assertEquals(queryRowHandleDAO, config.getQueryRowHandleDAO());
        assertEquals(sqlID, config.getSqlID());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(validationErrorHandler, config.getValidationErrorHandler());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#addRelation1n(boolean)} のためのテスト・メソッド。
     */
    @Test
    public void testAddRelation1n001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        boolean relation1n = true;

        // テスト
        config.addRelation1n(relation1n);

        assertNotNull(config);
        assertEquals(queryRowHandleDAO, config.getQueryRowHandleDAO());
        assertEquals(sqlID, config.getSqlID());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(relation1n, config.isRelation1n());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#addDbCollectorPrePostProcess(jp.terasoluna.fw.collector.db.DBCollectorPrePostProcess)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testAddDBCollectorPrePostProcess001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        DBCollectorPrePostProcess dbCollectorPrePostProcess = new DBCollectorPrePostProcess() {
            public <P> void postprocessComplete(DBCollector<P> collector) {
            }

            public <P> DBCollectorPrePostProcessStatus postprocessException(
                    DBCollector<P> collector, Throwable throwable) {
                return null;
            }

            public <P> void preprocess(DBCollector<P> collector) {
            }
        };

        // テスト
        config.addDbCollectorPrePostProcess(dbCollectorPrePostProcess);

        assertNotNull(config);
        assertEquals(queryRowHandleDAO, config.getQueryRowHandleDAO());
        assertEquals(sqlID, config.getSqlID());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(dbCollectorPrePostProcess, config
                .getDbCollectorPrePostProcess());
    }

    /**
     * {@link jp.terasoluna.fw.collector.file.DBCollectorConfig#addExecuteByConstructor(boolean)} のためのテスト・メソッド。
     */
    @Test
    public void testAddExecuteByConstructor001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        boolean executeByConstructor = true;

        // テスト
        config.addExecuteByConstructor(executeByConstructor);

        assertNotNull(config);
        assertEquals(queryRowHandleDAO, config.getQueryRowHandleDAO());
        assertEquals(sqlID, config.getSqlID());
        assertEquals(bindParams, config.getBindParams());
        assertEquals(executeByConstructor, config.isExecuteByConstructor());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#getQueryRowHandleDAO()} のためのテスト・メソッド。
     */
    @Test
    public void testGetQueryRowHandleDAO001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        // テスト
        QueryRowHandleDAO result = config.getQueryRowHandleDAO();

        assertNotNull(result);
        assertEquals(queryRowHandleDAO, result);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#setQueryRowHandleDAO(jp.terasoluna.fw.dao.QueryRowHandleDAO)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testSetQueryRowHandleDAO001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        QueryRowHandleDAO queryRowHandleDAO2 = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        // テスト
        config.setQueryRowHandleDAO(queryRowHandleDAO2);

        assertNotNull(config.getQueryRowHandleDAO());
        assertEquals(queryRowHandleDAO2, config.getQueryRowHandleDAO());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#getSqlID()} のためのテスト・メソッド。
     */
    @Test
    public void testGetSqlID001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        // テスト
        String result = config.getSqlID();

        assertNotNull(result);
        assertEquals(sqlID, result);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#setSqlID(java.lang.String)} のためのテスト・メソッド。
     */
    @Test
    public void testSetSqlID001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };

        String sqlID = "hoge";
        String sqlID2 = "hogehoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        // テスト
        config.setSqlID(sqlID2);

        assertNotNull(config.getSqlID());
        assertEquals(sqlID2, config.getSqlID());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#getBindParams()} のためのテスト・メソッド。
     */
    @Test
    public void testGetBindParams001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        // テスト
        Object result = config.getBindParams();

        assertNotNull(result);
        assertEquals(bindParams, result);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#setBindParams(java.lang.Object)} のためのテスト・メソッド。
     */
    @Test
    public void testSetBindParams001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };

        String sqlID = "hoge";
        Object bindParams = new Object();
        Object bindParams2 = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        // テスト
        config.setBindParams(bindParams2);

        assertNotNull(config.getBindParams());
        assertEquals(bindParams2, config.getBindParams());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#isRelation1n()} のためのテスト・メソッド。
     */
    @Test
    public void testIsRelation1n001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();
        boolean relation1n = true;

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);
        config.setRelation1n(relation1n);

        // テスト
        boolean result = config.isRelation1n();

        assertNotNull(result);
        assertEquals(relation1n, result);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#setRelation1n(boolean)} のためのテスト・メソッド。
     */
    @Test
    public void testSetRelation1n001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };

        String sqlID = "hoge";
        Object bindParams = new Object();
        boolean relation1n = true;

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        // テスト
        config.setRelation1n(relation1n);

        assertNotNull(config.isRelation1n());
        assertEquals(relation1n, config.isRelation1n());
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#getDbCollectorPrePostProcess()} のためのテスト・メソッド。
     */
    @Test
    public void testGetDbCollectorPrePostProcess001() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };
        String sqlID = "hoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);
        DBCollectorPrePostProcess dbCollectorPrePostProcess = new DBCollectorPrePostProcess() {
            public <P> void postprocessComplete(DBCollector<P> collector) {
            }

            public <P> DBCollectorPrePostProcessStatus postprocessException(
                    DBCollector<P> collector, Throwable throwable) {
                return null;
            }

            public <P> void preprocess(DBCollector<P> collector) {
            }
        };
        config.setDbCollectorPrePostProcess(dbCollectorPrePostProcess);

        // テスト
        DBCollectorPrePostProcess result = config
                .getDbCollectorPrePostProcess();

        assertNotNull(result);
        assertEquals(dbCollectorPrePostProcess, result);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollectorConfig#setDbCollectorPrePostProcess(jp.terasoluna.fw.collector.db.DBCollectorPrePostProcess)}
     * のためのテスト・メソッド。
     */
    @Test
    public void setDbCollectorPrePostProcess() {
        QueryRowHandleDAO queryRowHandleDAO = new QueryRowHandleDAO() {
            public void executeWithRowHandler(String sqlID, Object bindParams,
                    DataRowHandler rowHandler) {
            }
        };

        String sqlID = "hoge";
        Object bindParams = new Object();

        DBCollectorConfig config = new DBCollectorConfig(queryRowHandleDAO,
                sqlID, bindParams);

        DBCollectorPrePostProcess dbCollectorPrePostProcess = new DBCollectorPrePostProcess() {
            public <P> void postprocessComplete(DBCollector<P> collector) {
            }

            public <P> DBCollectorPrePostProcessStatus postprocessException(
                    DBCollector<P> collector, Throwable throwable) {
                return null;
            }

            public <P> void preprocess(DBCollector<P> collector) {
            }
        };
        config.setDbCollectorPrePostProcess(dbCollectorPrePostProcess);
        DBCollectorPrePostProcess dbCollectorPrePostProcess2 = new DBCollectorPrePostProcess() {
            public <P> void postprocessComplete(DBCollector<P> collector) {
            }

            public <P> DBCollectorPrePostProcessStatus postprocessException(
                    DBCollector<P> collector, Throwable throwable) {
                return null;
            }

            public <P> void preprocess(DBCollector<P> collector) {
            }
        };

        // テスト
        config.setDbCollectorPrePostProcess(dbCollectorPrePostProcess2);

        assertNotNull(config.getDbCollectorPrePostProcess());
        assertEquals(dbCollectorPrePostProcess2, config
                .getDbCollectorPrePostProcess());
    }

}
