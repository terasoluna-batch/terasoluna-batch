/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.Validator;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.dao.UserListQueryResultHandleDao;
import jp.terasoluna.fw.collector.unit.testcase.junit4.DaoTestCaseJunit4;
import jp.terasoluna.fw.collector.unit.testcase.junit4.loader.DaoTestCaseContextLoader;
import jp.terasoluna.fw.exception.SystemException;

/**
 * DaoValidateCollectorTest
 */
@ContextConfiguration(locations = {
        "classpath:jp/terasoluna/fw/collector/db/dataSource.xml" }, loader = DaoTestCaseContextLoader.class)
public class DaoValidateCollector002Test extends DaoTestCaseJunit4 {

    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(
            DaoValidateCollector002Test.class);

    private UserListQueryResultHandleDao userListQueryResultHandleDao = null;

    private int previousThreadCount = 0;

    public void setUserListQueryResultHandleDao(
            UserListQueryResultHandleDao userListQueryResultHandleDao) {
        this.userListQueryResultHandleDao = userListQueryResultHandleDao;
    }

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        DaoValidateCollector.setVerbose(true);
        super.onSetUpBeforeTransaction();
    }

    @Override
    protected void onTearDownAfterTransaction() throws Exception {
        DaoValidateCollector.setVerbose(false);
        super.onTearDownAfterTransaction();
    }

    @Before
    public void onSetUp() throws Exception {
        System.gc();
        this.previousThreadCount = CollectorTestUtil.getCollectorThreadCount();
    }

    @After
    public void onTearDown() throws Exception {
        System.gc();
        CollectorTestUtil.allInterrupt();
    }

    /**
     * {@link DaoValidateCollector#finalize()} のためのテスト・メソッド。
     */
    @Test
    public void testFinalize001() throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        Validator validator = null;

        @SuppressWarnings("resource")
        Collector<UserBean> it = new DaoValidateCollector<UserBean>(this.userListQueryResultHandleDao, "collect", null, validator);
        try {
            while (it.hasNext()) {
                it.next();
                // あえて途中で抜ける
                break;
            }
        } catch (Throwable e) {
            throw new SystemException(e);
        } finally {
            // あえてクローズせずに放置
            // DaoValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil.lessThanCollectorThreadCount(1
                + this.previousThreadCount));
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoValidateCollector#DaoValidateCollector(Object, String, Object, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testDaoValidateCollectorObjectStringObjectValidator001() throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int retryCount = 3;
        boolean retryFlg = false;
        Validator validator = null;

        do {
            retryFlg = false;
            Collector<UserBean> it = new DaoValidateCollector<UserBean>(this.userListQueryResultHandleDao, "collect", null, validator);
            try {
                while (it.hasNext()) {
                    it.next();
                    count_first++;
                }
            } catch (Throwable e) {
                if (e.getCause() instanceof DataAccessException && e.getCause()
                        .getCause() instanceof SQLException) {
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
                        TimeUnit.MILLISECONDS.sleep(1000);
                        continue;
                    }
                }
                throw new SystemException(e);
            } finally {
                DaoValidateCollector.closeQuietly(it);
            }

            // コレクタスレッド数チェック
            assertTrue(CollectorTestUtil.lessThanCollectorThreadCount(0
                    + this.previousThreadCount));

        } while (retryFlg && retryCount > 0);

        if (retryFlg && retryCount == 0) {
            logger.info("リトライカウントオーバー");
            fail();
            return;
        }

        for (int i = 0; i < 2; i++) {
            int count = 0;

            Collector<UserBean> it2 = new DaoValidateCollector<UserBean>(this.userListQueryResultHandleDao, "collect", null, validator);
            try {
                while (it2.hasNext()) {
                    it2.next();
                    count++;
                }
            } finally {
                DaoValidateCollector.closeQuietly(it2);
            }

            // コレクタスレッド数チェック
            assertTrue(CollectorTestUtil.lessThanCollectorThreadCount(0
                    + this.previousThreadCount));
            assertEquals(count_first, count);
        }
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoValidateCollector#DaoValidateCollector(Object, String, Object, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testDaoValidateCollectorObjectStringObjectValidator002() throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        Validator validator = null;

        Collector<UserBean> it = new DaoValidateCollector<UserBean>(this.userListQueryResultHandleDao, "collect", null, validator);
        try {
            for (UserBean user : it) {
                UserBean nextUser = null;

                if (count_first > 4) {
                    nextUser = it.getNext();
                }

                if (user != null && nextUser != null) {
                    String userIdStr = user.getUserId();
                    String nextUserIdStr = nextUser.getUserId();
                    int userId = Integer.parseInt(userIdStr);
                    int nextUserId = Integer.parseInt(nextUserIdStr);

                    assertEquals(userId, nextUserId - 1);
                }
                count_first++;
            }
        } finally {
            DaoValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil.lessThanCollectorThreadCount(0
                + this.previousThreadCount));

        for (int i = 0; i < 2; i++) {
            int count = 0;

            Collector<UserBean> it2 = new DaoValidateCollector<UserBean>(this.userListQueryResultHandleDao, "collect", null, validator);
            try {
                while (it2.hasNext()) {
                    it2.next();
                    count++;
                }
            } finally {
                DaoValidateCollector.closeQuietly(it2);
            }
            // コレクタスレッド数チェック
            assertTrue(CollectorTestUtil.lessThanCollectorThreadCount(0
                    + this.previousThreadCount));
            assertEquals(count_first, count);
        }
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoValidateCollector#DaoValidateCollector(Object, String, Object, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testDaoValidateCollectorObjectStringObjectValidator003() throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;
        Validator validator = null;

        Collector<OrderBean> it = new DaoValidateCollector<OrderBean>(this.userListQueryResultHandleDao, "collectOrder", null, validator);
        try {
            for (OrderBean order : it) {
                List<OrderDetailBean> orderDetailList = order
                        .getOrderDetailList();
                count_detail += orderDetailList.size();
                count_first++;
            }
        } finally {
            DaoValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil.lessThanCollectorThreadCount(0
                + this.previousThreadCount));

        assertEquals(4, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoValidateCollector#DaoValidateCollector(Object, String, Object, boolean, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testDaoValidateCollectorObjectStringObjectBooleanValidator001() throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;
        Validator validator = null;

        @SuppressWarnings("deprecation")
        Collector<OrderBean> it = new DaoValidateCollector<OrderBean>(this.userListQueryResultHandleDao, "collectOrder", null, true, validator);
        try {
            for (OrderBean order : it) {
                List<OrderDetailBean> orderDetailList = order
                        .getOrderDetailList();
                count_detail += orderDetailList.size();
                count_first++;
            }
        } finally {
            DaoValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil.lessThanCollectorThreadCount(0
                + this.previousThreadCount));

        assertEquals(4, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoValidateCollector#DaoValidateCollector(Object, String, Object, int, boolean, jp.terasoluna.fw.collector.exception.CollectorExceptionHandler, DaoCollectorPrePostProcess, org.springframework.validation.Validator, jp.terasoluna.fw.collector.validate.ValidationErrorHandler)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testDaoValidateCollectorObjectStringObjectIntBooleanCollectorExceptionHandlerDaoCollectorPrePostProcessValidatorValidationErrorHandler001() throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;
        Validator validator = null;

        @SuppressWarnings("deprecation")
        Collector<OrderBean> it = new DaoValidateCollector<OrderBean>(this.userListQueryResultHandleDao, "collectOrder", null, 1, true, null, null, validator, null);
        try {
            for (OrderBean order : it) {
                List<OrderDetailBean> orderDetailList = order
                        .getOrderDetailList();
                count_detail += orderDetailList.size();
                count_first++;
            }
        } finally {
            DaoValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil.lessThanCollectorThreadCount(0
                + this.previousThreadCount));

        assertEquals(4, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoValidateCollector#DaoValidateCollector(Object, String, Object, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testDaoValidateCollectorObjectStringObjectValidator004() throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;
        Validator validator = null;

        Collector<Order2Bean> it = new DaoValidateCollector<Order2Bean>(this.userListQueryResultHandleDao, "collectOrder2", null, validator);
        try {
            for (Order2Bean order : it) {
                OrderDetailBean orderDetail = order.getOrderDetail();
                if (orderDetail != null) {
                    count_detail++;
                }
                count_first++;
            }
        } finally {
            DaoValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil.lessThanCollectorThreadCount(0
                + this.previousThreadCount));

        assertEquals(12, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoValidateCollector#DaoValidateCollector(Object, String, Object, boolean, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testDaoValidateCollectorObjectStringObjectBooleanValidator002() throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;
        Validator validator = null;

        @SuppressWarnings("deprecation")
        Collector<Order2Bean> it = new DaoValidateCollector<Order2Bean>(this.userListQueryResultHandleDao, "collectOrder2", null, true, validator);
        try {
            for (Order2Bean order : it) {
                it.getPrevious();
                it.getNext();
                OrderDetailBean orderDetail = order.getOrderDetail();
                if (orderDetail != null) {
                    count_detail++;
                }
                count_first++;
            }
        } finally {
            DaoValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil.lessThanCollectorThreadCount(0
                + this.previousThreadCount));

        assertEquals(12, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DaoValidateCollector#DaoValidateCollector(Object, String, Object, int, boolean, jp.terasoluna.fw.collector.exception.CollectorExceptionHandler, jp.terasoluna.fw.collector.db.DaoCollectorPrePostProcess, org.springframework.validation.Validator,jp.terasoluna.fw.collector.validate.ValidationErrorHandler)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testDaoValidateCollectorObjectStringObjectIntBooleanCollectorExceptionHandlerDaoCollectorPrePostProcessValidatorValidationErrorHandler002() throws Exception {
        if (this.userListQueryResultHandleDao == null) {
            fail("userListQueryResultHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;
        Validator validator = null;

        @SuppressWarnings("deprecation")
        Collector<Order2Bean> it = new DaoValidateCollector<Order2Bean>(this.userListQueryResultHandleDao, "collectOrder2", null, 1, true, null, null, validator, null);
        try {
            for (Order2Bean order : it) {
                it.getPrevious();
                it.getNext();
                OrderDetailBean orderDetail = order.getOrderDetail();
                if (orderDetail != null) {
                    count_detail++;
                }
                count_first++;
            }
        } finally {
            DaoValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil.lessThanCollectorThreadCount(0
                + this.previousThreadCount));

        assertEquals(12, count_first);
        assertEquals(12, count_detail);
    }

}
