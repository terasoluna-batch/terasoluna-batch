package jp.terasoluna.fw.collector.file;

import java.net.URL;
import java.util.List;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;
import jp.terasoluna.fw.file.dao.FileQueryDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileValidateCollector021Test extends DaoTestCase {
    /**
     * Log.
     */
    private static Log logger = LogFactory
            .getLog(FileValidateCollector021Test.class);

    private FileQueryDAO csvFileQueryDAO = null;

    private int previousThreadCount = 0;

    public void setCsvFileQueryDAO(FileQueryDAO csvFileQueryDAO) {
        this.csvFileQueryDAO = csvFileQueryDAO;
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
        this.previousThreadCount = CollectorTestUtil.getCollectorThreadCount();
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
        CollectorTestUtil.allInterrupt();
        super.onTearDown();
    }

    @Override
    protected void addConfigLocations(List<String> configLocations) {
        configLocations.add("jp/terasoluna/fw/collector/db/dataSource.xml");
    }

    public void testFileValidateCollector021() throws Exception {
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
        SkipValidationErrorHandler validatorErrorHandler = new SkipValidationErrorHandler();

        Collector<B000001Data> it = new FileValidateCollector<B000001Data>(
                this.csvFileQueryDAO, url.getPath(), B000001Data.class,
                validator, validatorErrorHandler);

        try {
            // it = ac.execute();

            for (B000001Data data : it) {
                count_first++;

            }
        } finally {
            // クローズ
            FileValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        // Validator#supportsメソッドが呼ばれた回数
        assertEquals(1000, validator.getCallSupports());

        // Validator#validateメソッドが呼ばれた回数
        assertEquals(1000, validator.getCallValidate());

        // エラーが発生したフィールドの件数
        assertEquals(10, validatorErrorHandler.getErrorFieldCount());

        // ループした回数（エラーの分少なくなる）
        assertEquals(990, count_first);
    }

}
