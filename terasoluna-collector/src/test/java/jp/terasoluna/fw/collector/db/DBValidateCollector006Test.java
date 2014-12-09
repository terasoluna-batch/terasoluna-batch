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
import org.springframework.validation.Validator;

/**
 * DBValidateCollectorTest
 */
public class DBValidateCollector006Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory
            .getLog(DBValidateCollector006Test.class);

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
     * {@link jp.terasoluna.fw.collector.db.DBValidateCollector#DBValidateCollectorTest(jp.terasoluna.fw.dao.QueryRowHandleDAO, java.lang.String, java.lang.Object, int)}
     * のためのテスト・メソッド。
     */
    public void testDBValidateCollectorTestQueryRowHandleDAOStringObjectInt004()
                                                                                throws Exception {
        if (this.queryRowHandleDAO == null) {
            fail("queryRowHandleDAOがnullです。");
        }

        int count_first = 0;
        Validator validator = null;

        Collector<UserBean> it = new DBValidateCollector<UserBean>(
                this.queryRowHandleDAO, "selectUserList", null, 1, validator);
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
            DBValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(1 + this.previousThreadCount));

        for (int i = 0; i < 2; i++) {
            int count = 0;

            long startTime = System.currentTimeMillis();

            Collector<UserBean> it2 = new DBValidateCollector<UserBean>(
                    this.queryRowHandleDAO, "selectUserList", null, 1,
                    validator);
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

                    // あえて毎回、途中で抜ける
                    if (count > 900) {
                        break;
                    }
                }
            } finally {
                // クローズ
                DBValidateCollector.closeQuietly(it2);
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

            assertEquals(901, count);
        }
    }

}
