package jp.terasoluna.fw.collector.file;

import java.net.URL;
import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.db.UserBean;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.collector.validate.ValidationErrorHandler;
import jp.terasoluna.fw.file.dao.FileQueryDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.test.context.ContextConfiguration;
import jp.terasoluna.fw.collector.unit.testcase.junit4.DaoTestCaseJunit4;
import jp.terasoluna.fw.collector.unit.testcase.junit4.loader.DaoTestCaseContextLoader;

@ContextConfiguration(locations = {
        "classpath:jp/terasoluna/fw/collector/db/dataSource.xml" }, loader = DaoTestCaseContextLoader.class)
public class FileValidateCollector004Test extends DaoTestCaseJunit4 {
    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(
            FileValidateCollector004Test.class);

    private FileQueryDAO csvFileQueryDAO = null;

    public void setCsvFileQueryDAO(FileQueryDAO csvFileQueryDAO) {
        this.csvFileQueryDAO = csvFileQueryDAO;
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
        CollectorTestUtil.allInterrupt();
    }

    /**
     * testFileCollector004
     * @throws Exception
     */
    @Test
    public void testFileCollector004() throws Exception {
        if (this.csvFileQueryDAO == null) {
            fail("csvFileQueryDAOがnullです。");
        }

        URL url = getClass().getClassLoader().getResource("USER_TEST.csv");
        if (logger.isDebugEnabled()) {
            if (url != null) {
                logger.debug("url.getPath() : " + url.getPath());
            } else {
                logger.debug("url.getPath() : " + null);
            }
        }

        if (url == null) {
            fail("urlがnullです。");
        }

        // int count_first = 0;
        CollectorExceptionHandler exceptionHandler = new CollectorExceptionHandlerStub();
        Validator validator = new ValidatorStub();
        ValidationErrorHandler validationErrorHandler = null;

        Collector<B000001Data> it = new FileValidateCollector<B000001Data>(this.csvFileQueryDAO, url
                .getPath(), B000001Data.class, exceptionHandler, validator, validationErrorHandler);

        it.close();
    }

    /**
     * testFileValidateCollector005
     * @throws Exception
     */
    @SuppressWarnings("resource")
    @Test
    public void testFileValidateCollector005() throws Exception {
        FileCollectorConfig<UserBean> config = null;

        try {
            new FileValidateCollector<UserBean>(config);
            fail("失敗");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
            assertEquals("The parameter is null.", e.getMessage());
        }
    }

    /**
     * testFileCollector006
     * @throws Exception
     */
    @Test
    public void testFileCollector006() throws Exception {
        if (this.csvFileQueryDAO == null) {
            fail("csvFileQueryDAOがnullです。");
        }

        URL url = getClass().getClassLoader().getResource("USER_TEST.csv");
        if (logger.isDebugEnabled()) {
            if (url != null) {
                logger.debug("url.getPath() : " + url.getPath());
            } else {
                logger.debug("url.getPath() : " + null);
            }
        }

        if (url == null) {
            fail("urlがnullです。");
        }

        // int count_first = 0;
        CollectorExceptionHandler exceptionHandler = new CollectorExceptionHandlerStub();
        Validator validator = new ValidatorStub();
        ValidationErrorHandler validationErrorHandler = null;
        int queueSize = 123;

        Collector<B000001Data> it = new FileValidateCollector<B000001Data>(this.csvFileQueryDAO, url
                .getPath(), B000001Data.class, queueSize, exceptionHandler, validator, validationErrorHandler);

        it.close();
    }

}
