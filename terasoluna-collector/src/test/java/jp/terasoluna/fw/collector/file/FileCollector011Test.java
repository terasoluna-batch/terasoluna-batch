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

public class FileCollector011Test extends DaoTestCase {
    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(FileCollector011Test.class);

    private FileQueryDAO csvFileQueryDAO = null;

    private int previousThreadCount = 0;

    public void setCsvFileQueryDAO(FileQueryDAO csvFileQueryDAO) {
        this.csvFileQueryDAO = csvFileQueryDAO;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        FileCollector.setVerbose(false);
        super.onSetUpBeforeTransaction();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onTearDownAfterTransaction() throws Exception {
        FileCollector.setVerbose(false);
        super.onTearDownAfterTransaction();
    }

    @SuppressWarnings("deprecation")
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

    public void testFileCollectorFinalize001() throws Exception {
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

        FileCollectorConfig<B000001Data> config = new FileCollectorConfig<B000001Data>(
                this.csvFileQueryDAO, url.getPath(), B000001Data.class);
        config.addExecuteByConstructor(true);

        @SuppressWarnings("resource")
        Collector<B000001Data> it = new FileCollector<B000001Data>(config);

        try {
            // it = ac.execute();

            while (it.hasNext()) {
                it.next();
                // あえて途中で抜ける
                break;
            }
        } finally {
            // あえてクローズせずに放置
            // FileCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(1 + this.previousThreadCount));
    }

    public void testFileCollector011() throws Exception {
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

        int count_first = 0;

        Collector<B000001Data> it = new FileCollector<B000001Data>(
                this.csvFileQueryDAO, url.getPath(), B000001Data.class);

        try {
            // it = ac.execute();

            while (it.hasNext()) {
                it.next();
                count_first++;
            }
        } finally {
            // クローズ
            FileCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        for (int i = 0; i < 7; i++) {
            int count = 0;

            long startTime = System.currentTimeMillis();

            Collector<B000001Data> it2 = new FileCollector<B000001Data>(
                    this.csvFileQueryDAO, url.getPath(), B000001Data.class);

            try {
                for (B000001Data data : it2) {
                    if (logger.isInfoEnabled() && data == null) {
                            logger.info("UserBean is null.##############");
                    }

                    count++;

                }
            } finally {
                FileCollector.closeQuietly(it2);
            }

            // コレクタスレッド数チェック
            assertTrue(CollectorTestUtil
                    .lessThanCollectorThreadCount(0 + this.previousThreadCount));

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

            assertEquals(count_first, count);
        }

    }

}
