/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import java.util.List;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.dao.UserListQueryRowHandleDao;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

/**
 * DaoValidateCollectorTest
 */
public class DaoValidateCollector005Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory
            .getLog(DaoValidateCollector005Test.class);

    private UserListQueryRowHandleDao userListQueryRowHandleDao = null;

    private int previousThreadCount = 0;

    @Override
    protected void addConfigLocations(List<String> configLocations) {
        configLocations.add("jp/terasoluna/fw/collector/db/dataSource.xml");
    }

    public void setUserListQueryRowHandleDao(UserListQueryRowHandleDao userListQueryRowHandleDao) {
        this.userListQueryRowHandleDao = userListQueryRowHandleDao;
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
     * {@link jp.terasoluna.fw.collector.db.DaoValidateCollector#DaoValidateCollector(Object, String, Object, int, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    public void testDaoValidateCollectorTestObjectStringObjectInt003()
                                                                                throws Exception {
        if (this.userListQueryRowHandleDao == null) {
            fail("userListQueryRowHandleDaoがnullです。");
        }

        int count_first = 0;
        Validator validator = null;

        Collector<UserBean> it = new DaoValidateCollector<UserBean>(
                this.userListQueryRowHandleDao, "collect", null, 1, validator);
        try {
            for (UserBean user : it) {
                count_first++;

                if (count_first > 10) {
                    break;
                }
            }
        } finally {
            DaoValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(1 + this.previousThreadCount));

        for (int i = 0; i < 10; i++) {
            int count = 0;

            long startTime = System.currentTimeMillis();

            Collector<UserBean> it2 = new DaoValidateCollector<UserBean>(
                    this.userListQueryRowHandleDao, "collect", null, 1,
                    validator);
            try {
                for (UserBean user : it2) {
                    if (logger.isInfoEnabled() && user == null) {
                        logger.info("UserBean is null.##############");
                    }

                    count++;

                    if (i % 2 == 0 && count > 10) {
                        break;
                    }
                }
            } finally {
                DaoValidateCollector.closeQuietly(it2);
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
