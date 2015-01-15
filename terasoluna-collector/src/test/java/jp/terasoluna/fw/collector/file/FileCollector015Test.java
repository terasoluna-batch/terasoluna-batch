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

public class FileCollector015Test extends DaoTestCase {
    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(FileCollector015Test.class);

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

    public void testFileCollector015() throws Exception {
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

        // Collector<B000001Data> ac = new FileCollector<B000001Data>(
        // this.csvFileQueryDAO, url.getPath(), B000001Data.class, 100);

        int count_first = 0;

        Collector<B000001Data> it = new FileCollector<B000001Data>(
                this.csvFileQueryDAO, url.getPath(), B000001Data.class, 100,
                null);

        try {
            for (B000001Data data : it) {
                count_first++;

                if (count_first > 10) {
                    break;
                }

            }
        } finally {
            // クローズ
            FileCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(1 + this.previousThreadCount));

        for (int i = 0; i < 7; i++) {
            int count = 0;

            long startTime = System.currentTimeMillis();

            Collector<B000001Data> it2 = new FileCollector<B000001Data>(
                    this.csvFileQueryDAO, url.getPath(), B000001Data.class,
                    100, null);
            try {
                for (B000001Data data : it2) {
                    if (logger.isInfoEnabled() && data == null) {
                        logger.info("UserBean is null.##############");
                    }
                    count++;

                    if (i % 2 == 0 && count > 10) {
                        break;
                    }
                }
            } finally {
                // クローズ
                FileCollector.closeQuietly(it2);
            }

            long endTime = System.currentTimeMillis();

            if (logger.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("i:[");
                sb.append(String.format("%04d", i));
                sb.append("]");
                sb.append(" milliSec:[");
                sb.append(String.format("%04d", (endTime - startTime)));
                sb.append("]");
                logger.info(sb.toString());
            }

            if (i % 2 == 0) {
                assertEquals(count_first, count);
            }
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

    }

}
