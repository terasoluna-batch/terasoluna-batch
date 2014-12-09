package jp.terasoluna.fw.collector.file;

import java.io.FileNotFoundException;
import java.util.List;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;
import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.dao.FileQueryDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

public class FileValidateCollector019Test extends DaoTestCase {
    /**
     * Log.
     */
    private static Log logger = LogFactory
            .getLog(FileValidateCollector019Test.class);

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

    public void testFileValidateCollector019() throws Exception {
        if (this.csvFileQueryDAO == null) {
            fail("csvFileQueryDAOがnullです。");
        }

        int count_first = 0;
        // int exception_count = 0;
        Validator validator = null;

        Collector<B000001Data> it = new FileValidateCollector<B000001Data>(
                this.csvFileQueryDAO, "NOT_FOUND.csv", B000001Data.class,
                validator);

        try {
            for (B000001Data data : it) {
                if (logger.isInfoEnabled()) {

                    StringBuilder sb = new StringBuilder();
                    if (data != null) {
                        sb.append("UserId:[");
                        sb.append(String.format("%2s", data.getId()));
                        sb.append("],");
                        sb.append("FirstName:[");
                        sb.append(String.format("%4s", data.getFirstname()));
                        sb.append("],");
                        sb.append("FamilyName:[");
                        sb.append(String.format("%4s", data.getFamilyname()));
                        sb.append("],");
                        sb.append("UserAge:[");
                        sb.append(String.format("%2s", data.getAge()));
                        sb.append("])");
                        if (false) {
                            logger.info(sb.toString());
                        }
                    }
                }
                count_first++;

            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            assertNotNull(e);
            assertEquals(FileException.class, e.getClass());
            assertNotNull(e.getCause());
            assertEquals(FileNotFoundException.class, e.getCause().getClass());
            assertEquals("Failed in generation of reader.", e.getMessage());
            return;
        } finally {
            // クローズ
            FileValidateCollector.closeQuietly(it);

            // コレクタスレッド数チェック
            assertTrue(CollectorTestUtil
                    .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        }

        fail();
    }

}
