/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import java.sql.SQLException;
import java.util.List;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.dao.UserListQueryResultHandleDao;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;
import jp.terasoluna.fw.exception.SystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

/**
 * DaoCollectorTest
 */
public class DaoCollector002Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(DaoCollector002Test.class);

    private UserListQueryResultHandleDao userListQueryResultHandleDao = null;

    private int previousThreadCount = 0;

    @Override
    protected void addConfigLocations(List<String> configLocations) {
        configLocations.add("jp/terasoluna/fw/collector/db/dataSource.xml");
    }

    public void setUserListQueryResultHandleDao(UserListQueryResultHandleDao userListQueryResultHandleDao) {
        this.userListQueryResultHandleDao = userListQueryResultHandleDao;
    }

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        DaoCollector.setVerbose(true);
        super.onSetUpBeforeTransaction();
    }

    @Override
    protected void onTearDownAfterTransaction() throws Exception {
        DaoCollector.setVerbose(false);
        super.onTearDownAfterTransaction();
    }

    @Override
    protected void onSetUp() throws Exception {
        System.gc();
        super.onSetUp();
        this.previousThreadCount = CollectorTestUtil.getCollectorThreadCount();
    }

    @Override
    protected void onTearDown() throws Exception {
        System.gc();
        CollectorTestUtil.allInterrupt();
        super.onTearDown();
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollector#DaoCollector(java.lang.Object, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDaoCollectorObjectStringObject001()
                                                                     throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int retryCount = 3;
        boolean retryFlg = false;

        do {
            retryFlg = false;
            Collector<UserBean> col = new DaoCollector<UserBean>(
                    this.userListQueryResultHandleDao, "collect", null);
            try {
                for (UserBean user : col) {
                    count_first++;
                }
            } catch (Throwable e) {
                if (e instanceof AssertionError) {
                    throw (AssertionError) e;
                }
                if (e.getCause() instanceof DataAccessException
                        && e.getCause().getCause() instanceof SQLException) {
                    SQLException sqle = (SQLException) e.getCause().getCause();
                    logger.info("SQLState:" + sqle.getSQLState());
                    logger.info("ErrorCode:" + sqle.getErrorCode());
                    logger.info("", e);
                    // Oracleの場合はORA-00054
                    if (sqle.getErrorCode() == 54) {
                        // リトライフラグを立てる
                        retryFlg = true;
                        retryCount--;
                        // ウェイト
                        Thread.sleep(1000);
                        continue;
                    }
                }
                throw new SystemException(e);
            } finally {
                DaoCollector.closeQuietly(col);
            }

        } while (retryFlg && retryCount > 0);

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        if (retryFlg && retryCount == 0) {
            logger.info("リトライカウントオーバー");
            fail();
            return;
        }

        for (int i = 0; i < 2; i++) {
            int count = 0;

            long startTime = System.currentTimeMillis();

            Collector<UserBean> col2 = new DaoCollector<UserBean>(
                    this.userListQueryResultHandleDao, "collect", null);
            try {
                for (UserBean user : col2) {
                    count++;
                }
            } finally {
                DaoCollector.closeQuietly(col2);
            }

            // コレクタスレッド数チェック
            assertTrue(CollectorTestUtil
                    .lessThanCollectorThreadCount(0 + this.previousThreadCount));

            long endTime = System.currentTimeMillis();
            assertEquals(count_first, count);
        }
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollector#DaoCollector(java.lang.Object, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDaoCollectorObjectStringObject002()
                                                                     throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;

        DaoCollectorConfig config = new DaoCollectorConfig(
                this.userListQueryResultHandleDao, "collect", null);
        config.addExecuteByConstructor(true);

        Collector<UserBean> col = new DaoCollector<UserBean>(config);
        try {
            for (UserBean user : col) {
                count_first++;
            }
        } finally {
            DaoCollector.closeQuietly(col);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        for (int i = 0; i < 2; i++) {
            int count = 0;

            long startTime = System.currentTimeMillis();

            Collector<UserBean> col2 = new DaoCollector<UserBean>(
                    this.userListQueryResultHandleDao, "collect", null);
            try {
                for (UserBean user : col2) {
                    count++;
                }
            } finally {
                DaoCollector.closeQuietly(col2);
            }

            // コレクタスレッド数チェック
            assertTrue(CollectorTestUtil
                    .lessThanCollectorThreadCount(0 + this.previousThreadCount));

            assertEquals(count_first, count);
        }
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollector#DaoCollector(java.lang.Object, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDaoCollectorObjectStringObject003()
                                                                     throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;

        Collector<OrderBean> col = new DaoCollector<OrderBean>(
                this.userListQueryResultHandleDao, "collectOrder", null);
        try {
            for (OrderBean order : col) {
                List<OrderDetailBean> orderDetailList = order
                        .getOrderDetailList();
                for (OrderDetailBean orderDetail : orderDetailList) {
                    count_detail++;
                }
                count_first++;
            }
        } finally {
            DaoCollector.closeQuietly(col);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(4, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollector#DaoCollector(java.lang.Object, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDaoCollectorObjectStringObject004()
                                                                     throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;

        Collector<OrderBean> col = new DaoCollector<OrderBean>(
                this.userListQueryResultHandleDao, "collectOrder", null, true);
        try {
            for (OrderBean order : col) {
                List<OrderDetailBean> orderDetailList = order
                        .getOrderDetailList();
                for (OrderDetailBean orderDetail : orderDetailList) {
                    count_detail++;
                }
                count_first++;
            }
        } finally {
            DaoCollector.closeQuietly(col);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(4, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollector#DaoCollector(java.lang.Object, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDaoCollectorObjectStringObject005()
                                                                     throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;

        DaoCollectorPrePostProcess prepost = null;

        Collector<OrderBean> col = new DaoCollector<OrderBean>(
                this.userListQueryResultHandleDao, "collectOrder", null, 1, true, null,
                prepost);
        try {
            for (OrderBean order : col) {
                List<OrderDetailBean> orderDetailList = order
                    .getOrderDetailList();
                for (OrderDetailBean orderDetail : orderDetailList) {
                    count_detail++;
                }
                count_first++;
            }
        } finally {
            DaoCollector.closeQuietly(col);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(4, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollector#DaoCollector(java.lang.Object, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDaoCollectorObjectStringObject006()
                                                                     throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;

        Collector<Order2Bean> col = new DaoCollector<Order2Bean>(
                this.userListQueryResultHandleDao, "collectOrder2", null);
        try {
            for (Order2Bean order : col) {

                OrderDetailBean orderDetail = order.getOrderDetail();
                if (orderDetail != null) {
                    count_detail++;
                }
                count_first++;
            }
        } finally {
            DaoCollector.closeQuietly(col);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(12, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollector#DaoCollector(java.lang.Object, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDaoCollectorObjectStringObject007()
                                                                     throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;

        Collector<Order2Bean> col = new DaoCollector<Order2Bean>(
                this.userListQueryResultHandleDao, "collectOrder2", null, true);
        try {
            for (Order2Bean order : col) {
                @SuppressWarnings("unused")
                Order2Bean prevOrder = col.getPrevious();
                @SuppressWarnings("unused")
                Order2Bean nextOrder = col.getNext();
                OrderDetailBean orderDetail = order.getOrderDetail();
                if (orderDetail != null) {
                    count_detail++;
                }
                count_first++;
            }
        } finally {
            DaoCollector.closeQuietly(col);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(12, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoCollector#DaoCollector(java.lang.Object, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDaoCollectorObjectStringObject008()
                                                                     throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;

        DaoCollectorPrePostProcessStub prepost = new DaoCollectorPrePostProcessStub();

        Collector<Order2Bean> col = new DaoCollector<Order2Bean>(
                this.userListQueryResultHandleDao, "collectOrder2", null, 1, true, null,
                prepost);
        try {
            for (Order2Bean order : col) {
                @SuppressWarnings("unused")
                Order2Bean prevOrder = col.getPrevious();
                @SuppressWarnings("unused")
                Order2Bean nextOrder = col.getNext();
                OrderDetailBean orderDetail = order.getOrderDetail();
                if (orderDetail != null) {
                        count_detail++;
                }
                count_first++;
            }
        } finally {
            DaoCollector.closeQuietly(col);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(12, count_first);
        assertEquals(12, count_detail);
        assertEquals(1, prepost.getPreCount());
        assertEquals(1, prepost.getPostCount());
    }

}
