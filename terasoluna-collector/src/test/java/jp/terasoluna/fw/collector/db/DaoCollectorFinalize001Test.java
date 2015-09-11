/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.dao.UserListQueryResultHandleDao;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.exception.SystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.test.context.ContextConfiguration;
import jp.terasoluna.fw.collector.unit.testcase.junit4.DaoTestCaseJunit4;
import jp.terasoluna.fw.collector.unit.testcase.junit4.loader.DaoTestCaseContextLoader;

/**
 * DaoCollectorTest
 */
@ContextConfiguration(locations = {
        "classpath:jp/terasoluna/fw/collector/db/dataSource.xml" }, loader = DaoTestCaseContextLoader.class)
public class DaoCollectorFinalize001Test extends DaoTestCaseJunit4 {

    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(
            DaoCollectorFinalize001Test.class);

    private UserListQueryResultHandleDao userListQueryResultHandleDao = null;

    private int previousThreadCount = 0;

    public void setUserListQueryResultHandleDao(
            UserListQueryResultHandleDao userListQueryResultHandleDao) {
        this.userListQueryResultHandleDao = userListQueryResultHandleDao;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        DaoCollector.setVerbose(true);
        super.onSetUpBeforeTransaction();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onTearDownAfterTransaction() throws Exception {
        DaoCollector.setVerbose(false);
        super.onTearDownAfterTransaction();
    }

    @After
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

    /**
     * {@link DaoCollector#finalize()} のためのテスト・メソッド。
     */
    @Test
    public void testDaoCollectorFinalize001() throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        {
            Collector<UserBean> col = new DaoCollector<UserBean>(this.userListQueryResultHandleDao, "collect", null);
            try {
                while (col.hasNext()) {
                    col.next();
                    break;
                }
            } catch (Throwable e) {
                throw new SystemException(e);
            } finally {
                // あえてクローズせずに放置
                // DaoCollector.closeQuietly(it);
            }
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil.lessThanCollectorThreadCount(1
                + this.previousThreadCount));

        System.gc();
        System.gc();
        System.gc();
    }

}
