/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import java.util.List;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.dao.QueryRowHandleDAO;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DBCollectorTest
 */
public class DBCollector005Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(DBCollector005Test.class);

    private QueryRowHandleDAO queryRowHandleDAO = null;

    private int previousThreadCount = 0;

    @Override
    protected void addConfigLocations(List<String> configLocations) {
        configLocations.add("jp/terasoluna/fw/collector/db/dataSource.xml");
    }

    public void setQueryRowHandleDAO(QueryRowHandleDAO queryRowHandleDAO) {
        this.queryRowHandleDAO = queryRowHandleDAO;
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

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollector#DBCollectorTest(jp.terasoluna.fw.dao.QueryRowHandleDAO, java.lang.String, java.lang.Object, int)}
     * のためのテスト・メソッド。
     */
    public void testDBCollectorTestQueryRowHandleDAOStringObjectInt003()
                                                                        throws Exception {
        if (this.queryRowHandleDAO == null) {
            fail("queryRowHandleDAOがnullです。");
        }

        int count_first = 0;

        Collector<UserBean> it = new DBCollector<UserBean>(
                this.queryRowHandleDAO, "selectUserList", null, 1);
        try {
            for (UserBean user : it) {
                if (logger.isInfoEnabled()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("UserId:[");
                    sb.append(String.format("%2s", user.getUserId()));
                    sb.append("],");
                    sb.append("FirstName:[");
                    sb.append(String.format("%4s", user.getFirstName()));
                    sb.append("],");
                    sb.append("FamilyName:[");
                    sb.append(String.format("%4s", user.getFamilyName()));
                    sb.append("],");
                    sb.append("UserAge:[");
                    sb.append(String.format("%2s", user.getUserAge()));
                    sb.append("])");
                    if (false) {
                        logger.info(sb.toString());
                    }
                }

                count_first++;

                if (count_first > 10) {
                    break;
                }
            }
        } finally {
            DBCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(1 + this.previousThreadCount));

        for (int i = 0; i < 10; i++) {
            int count = 0;

            long startTime = System.currentTimeMillis();

            Collector<UserBean> it2 = new DBCollector<UserBean>(
                    this.queryRowHandleDAO, "selectUserList", null, 1);
            try {
                for (UserBean user : it2) {
                    if (logger.isInfoEnabled()) {
                        StringBuilder sb = new StringBuilder();
                        if (user != null) {
                            sb.append("UserId:[");
                            sb.append(String.format("%2s", user.getUserId()));
                            sb.append("],");
                            sb.append("FirstName:[");
                            sb
                                    .append(String.format("%4s", user
                                            .getFirstName()));
                            sb.append("],");
                            sb.append("FamilyName:[");
                            sb.append(String
                                    .format("%4s", user.getFamilyName()));
                            sb.append("],");
                            sb.append("UserAge:[");
                            sb.append(String.format("%2s", user.getUserAge()));
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

                    if (i % 2 == 0 && count > 10) {
                        break;
                    }
                }
            } finally {
                DBCollector.closeQuietly(it2);
            }

            // コレクタスレッド数チェック
            assertTrue(CollectorTestUtil
                    .lessThanCollectorThreadCount(1 + this.previousThreadCount));

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
    }

}
