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

public class FileCollector013Test extends DaoTestCase {
    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(FileCollector013Test.class);

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

    public void testFileCollector013() throws Exception {
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
        // this.csvFileQueryDAO, url.getPath(), B000001Data.class, 1);

        int count_first = 0;

        Collector<B000001Data> it3 = new FileCollector<B000001Data>(
                this.csvFileQueryDAO, url.getPath(), B000001Data.class, 1, null);

        try {
            for (B000001Data data : it3) {
                count_first++;
            }
        } finally {
            // クローズ
            FileCollector.closeQuietly(it3);
        }

        assertEquals(1000, count_first);

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        count_first = 0;

        Collector<B000001Data> it = new FileCollector<B000001Data>(
                this.csvFileQueryDAO, url.getPath(), B000001Data.class, 1, null);

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
                    this.csvFileQueryDAO, url.getPath(), B000001Data.class, 1,
                    null);

            try {
                for (B000001Data data : it2) {
                    if (logger.isInfoEnabled()) {
                        StringBuilder sb = new StringBuilder();
                        if (data != null) {
                            sb.append("UserId:[");
                            sb.append(String.format("%2s", data.getId()));
                            sb.append("],");
                            sb.append("FirstName:[");
                            sb
                                    .append(String.format("%4s", data
                                            .getFirstname()));
                            sb.append("],");
                            sb.append("FamilyName:[");
                            sb.append(String
                                    .format("%4s", data.getFamilyname()));
                            sb.append("],");
                            sb.append("UserAge:[");
                            sb.append(String.format("%2s", data.getAge()));
                            sb.append("])");
                            if (false) {
                                logger.info(sb.toString());
                            }
                        } else {
                            sb.append("UserBean is null.##############");
                            logger.info(sb.toString());
                        }
                    }
                    count++;

                }
            } finally {
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

            assertEquals(count_first, count);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

    }

}
