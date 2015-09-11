package jp.terasoluna.fw.collector.file;

import java.net.URL;
import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.collector.validate.ValidateErrorStatus;
import jp.terasoluna.fw.file.dao.FileQueryDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.test.context.ContextConfiguration;
import jp.terasoluna.fw.collector.unit.testcase.junit4.DaoTestCaseJunit4;
import jp.terasoluna.fw.collector.unit.testcase.junit4.loader.DaoTestCaseContextLoader;

@ContextConfiguration(locations = {
        "classpath:jp/terasoluna/fw/collector/db/dataSource.xml" }, loader = DaoTestCaseContextLoader.class)
public class FileValidateCollector022Test extends DaoTestCaseJunit4 {
    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(
            FileValidateCollector022Test.class);

    private FileQueryDAO csvFileQueryDAO = null;

    private int previousThreadCount = 0;

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
        this.previousThreadCount = CollectorTestUtil.getCollectorThreadCount();
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

    @Test
    public void testFileValidateCollector022() throws Exception {
        if (this.csvFileQueryDAO == null) {
            fail("csvFileQueryDAOがnullです。");
        }

        URL url = getClass().getClassLoader().getResource(
                "USER_TEST5_NAME_EMPTY.csv");
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

        int count_first = 0;
        ValidatorStub validator = new ValidatorStub();
        SkipValidationErrorHandler validatorErrorHandler = new SkipValidationErrorHandler(ValidateErrorStatus.END);

        Collector<B000001Data> it = new FileValidateCollector<B000001Data>(this.csvFileQueryDAO, url
                .getPath(), B000001Data.class, validator, validatorErrorHandler);

        try {
            // it = ac.execute();

            while (it.hasNext()) {
                it.next();
                count_first++;
            }
        } finally {
            // クローズ
            FileValidateCollector.closeQuietly(it);
        }

        if (logger.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append(" CallSupports:[");
            sb.append(validator.getCallSupports());
            sb.append("]");
            sb.append(" CallValidate:[");
            sb.append(validator.getCallValidate());
            sb.append("]");
            sb.append(" ErrorFieldCount:[");
            sb.append(validatorErrorHandler.getErrorFieldCount());
            sb.append("]");
            sb.append(" count_first:[");
            sb.append(count_first);
            sb.append("]");
            logger.info(sb.toString());
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil.lessThanCollectorThreadCount(0
                + this.previousThreadCount));

        // Validator#supportsメソッドが呼ばれた回数
        assertEquals(13, validator.getCallSupports());

        // Validator#validateメソッドが呼ばれた回数
        assertEquals(13, validator.getCallValidate());

        // エラーが発生したフィールドの件数
        assertEquals(1, validatorErrorHandler.getErrorFieldCount());

        // ループした回数（エラーの分少なくなる）
        assertEquals(12, count_first);
    }

}
