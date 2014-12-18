/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import java.sql.SQLException;
import java.util.List;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.dao.UserListQueryRowHandleDao;
import jp.terasoluna.fw.collector.util.ControlBreakChecker;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;
import jp.terasoluna.fw.exception.SystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.Validator;

/**
 * DBValidateCollectorTest
 */
public class DBValidateCollector002Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory
            .getLog(DBValidateCollector002Test.class);

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
    protected void onSetUpBeforeTransaction() throws Exception {
        DBValidateCollector.setVerbose(true);
        super.onSetUpBeforeTransaction();
    }

    @Override
    protected void onTearDownAfterTransaction() throws Exception {
        DBValidateCollector.setVerbose(false);
        super.onTearDownAfterTransaction();
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
     * {@link jp.terasoluna.fw.collector.db.DBValidateCollector#DBValidateCollector(Object, String, Object, boolean, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    public void testDBValidateCollectorFinalize001() throws Exception {
        if (this.userListQueryRowHandleDao == null) {
            fail("userListQueryRowHandleDaoがnullです。");
        }

        Validator validator = null;

        Collector<UserBean> it = new DBValidateCollector<UserBean>(
                this.userListQueryRowHandleDao, "collect", null, validator);
        try {
            for (UserBean user : it) {

                // あえて途中で抜ける
                break;
            }
        } catch (Throwable e) {
            throw new SystemException(e);
        } finally {
            // あえてクローズせずに放置
            // DBValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(1 + this.previousThreadCount));
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBValidateCollector#DBValidateCollector(Object, String, Object, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    public void testDBValidateCollectorTestObjectStringObject001()
                                                                             throws Exception {
        if (this.userListQueryRowHandleDao == null) {
            fail("userListQueryRowHandleDaoがnullです。");
        }

        int count_first = 0;
        int retryCount = 3;
        boolean retryFlg = false;
        Validator validator = null;

        do {
            retryFlg = false;
            Collector<UserBean> it = new DBValidateCollector<UserBean>(
                    this.userListQueryRowHandleDao, "collect", null, validator);
            try {
                for (UserBean user : it) {
                    count_first++;
                }
            } catch (Throwable e) {
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
                DBValidateCollector.closeQuietly(it);
            }

            // コレクタスレッド数チェック
            assertTrue(CollectorTestUtil
                    .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        } while (retryFlg && retryCount > 0);

        if (retryFlg && retryCount == 0) {
            logger.info("リトライカウントオーバー");
            fail();
            return;
        }

        for (int i = 0; i < 2; i++) {
            int count = 0;

            long startTime = System.currentTimeMillis();

            Collector<UserBean> it2 = new DBValidateCollector<UserBean>(
                    this.userListQueryRowHandleDao, "collect", null, validator);
            try {
                for (UserBean user : it2) {
                    if (logger.isInfoEnabled() && user == null) {
                        logger.info("UserBean is null.##############");
                    }

                    count++;
                }
            } finally {
                DBValidateCollector.closeQuietly(it2);
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

    /**
     * {@link jp.terasoluna.fw.collector.db.DBValidateCollector#DBValidateCollector(Object, String, Object, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    public void testDBValidateCollectorTestObjectStringObject002()
                                                                             throws Exception {
        if (this.userListQueryRowHandleDao == null) {
            fail("userListQueryRowHandleDaoがnullです。");
        }

        int count_first = 0;
        Validator validator = null;

        Collector<UserBean> it = new DBValidateCollector<UserBean>(
                this.userListQueryRowHandleDao, "collect", null, validator);
        try {
            for (UserBean user : it) {
                UserBean prevUser = null;
                UserBean nextUser = null;
                UserBean currentUser = null;

                if (count_first > 4) {
                    prevUser = it.getPrevious();
                    nextUser = it.getNext();
                    currentUser = it.getCurrent();
                }

                // ユーザIDが切り替わったら
                if (ControlBreakChecker.isPreBreak(it, "userId")) {
                    // コントロールブレイク前処理
                    if (logger.isInfoEnabled()) {
                        logger.info("コントロールブレイク前処理");
                    }
                }
                // if (prevUser == null
                // || (user != null && !user.getUserId().equals(
                // prevUser.getUserId()))) {
                // // コントロールブレイク前処理
                // if (logger.isInfoEnabled()) {
                // logger.info("コントロールブレイク前処理");
                // }
                // }

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
                    sb.append("],");

                    sb.append(" Previous UserId:[");
                    if (prevUser != null) {
                        sb.append(String.format("%2s", prevUser.getUserId()));
                    } else {
                        sb.append("null");
                    }
                    sb.append("]");

                    sb.append(" Current UserId:[");
                    if (currentUser != null) {
                        sb
                                .append(String.format("%2s", currentUser
                                        .getUserId()));
                    } else {
                        sb.append("null");
                    }
                    sb.append("]");

                    sb.append(" Next UserId:[");
                    if (nextUser != null) {
                        sb.append(String.format("%2s", nextUser.getUserId()));
                    } else {
                        sb.append("null");
                    }
                    sb.append("]");

                    if (true) {
                        logger.info(sb.toString());
                    }

                    if (user != null && nextUser != null) {
                        String userIdStr = user.getUserId();
                        String nextUserIdStr = nextUser.getUserId();
                        int userId = Integer.parseInt(userIdStr);
                        int nextUserId = Integer.parseInt(nextUserIdStr);

                        assertEquals(userId, nextUserId - 1);
                    }
                }

                // ユーザIDが切り替わったら
                if (ControlBreakChecker.isBreak(it, "userId")) {
                    // コントロールブレイク後処理
                    if (logger.isInfoEnabled()) {
                        logger.info("コントロールブレイク後処理");
                    }
                }
                // if (nextUser == null
                // || (user != null && !user.getUserId().equals(
                // nextUser.getUserId()))) {
                // // コントロールブレイク後処理
                // if (logger.isInfoEnabled()) {
                // logger.info("コントロールブレイク後処理");
                // }
                // }

                count_first++;
            }
        } finally {
            DBValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        for (int i = 0; i < 2; i++) {
            int count = 0;

            long startTime = System.currentTimeMillis();

            Collector<UserBean> it2 = new DBValidateCollector<UserBean>(
                    this.userListQueryRowHandleDao, "collect", null, validator);
            try {
                for (UserBean user : it2) {
                    if (logger.isInfoEnabled() && user == null) {
                            logger.info("UserBean is null.##############");
                    }

                    count++;
                }
            } finally {
                DBValidateCollector.closeQuietly(it2);
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

    /**
     * {@link jp.terasoluna.fw.collector.db.DBValidateCollector#DBValidateCollector(Object, String, Object, boolean, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    public void testDBValidateCollectorTestObjectStringObject003()
                                                                             throws Exception {
        if (this.userListQueryRowHandleDao == null) {
            fail("userListQueryRowHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;
        Validator validator = null;

        Collector<OrderBean> it = new DBValidateCollector<OrderBean>(
                this.userListQueryRowHandleDao, "collectOrder", null, validator);
        try {
            for (OrderBean order : it) {
                // OrderBean nextOrder = it.following();
                if (logger.isInfoEnabled()) {
                    StringBuilder sb = new StringBuilder();

                    List<OrderDetailBean> orderDetailList = order
                            .getOrderDetailList();
                    for (OrderDetailBean orderDetail : orderDetailList) {
                        sb.setLength(0);
                        sb.append("OrdrId:[");
                        sb.append(String.format("%2s", order.getOrdrId()));
                        sb.append("],");
                        sb.append("CustId:[");
                        sb.append(String.format("%4s", order.getCustId()));
                        sb.append("],");
                        sb.append("OrderDate:[");
                        sb.append(String.format("%19s", order.getOrderDate()));
                        sb.append("],");

                        sb.append("DetlId:[");
                        sb
                                .append(String.format("%2s", orderDetail
                                        .getDetlId()));
                        sb.append("],");
                        sb.append("ProdId:[");
                        sb
                                .append(String.format("%3s", orderDetail
                                        .getProdId()));
                        sb.append("],");
                        sb.append("Quantity:[");
                        sb.append(String.format("%2s", orderDetail
                                .getQuantity()));
                        sb.append("],");
                        sb.append("Amount:[");
                        sb
                                .append(String.format("%5s", orderDetail
                                        .getAmount()));
                        sb.append("],");
                        sb.append("CustName:[");
                        sb.append(String.format("%12s", order.getCustName()));
                        sb.append("]");
                        sb.append("ProdName:[");
                        sb.append(String.format("%10s", orderDetail
                                .getProdName()));
                        sb.append("]");
                        if (true) {
                            logger.info(sb.toString());
                        }
                        count_detail++;
                    }
                    logger.info("-----");
                }

                count_first++;
            }
        } finally {
            DBValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(4, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBValidateCollector#DBValidateCollector(Object, String, Object, boolean, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    public void testDBValidateCollectorTestObjectStringObject004()
                                                                             throws Exception {
        if (this.userListQueryRowHandleDao == null) {
            fail("userListQueryRowHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;
        Validator validator = null;

        Collector<OrderBean> it = new DBValidateCollector<OrderBean>(
                this.userListQueryRowHandleDao, "collectOrder", null, true, validator);
        try {
            for (OrderBean order : it) {
                // OrderBean nextOrder = it.following();
                if (logger.isInfoEnabled()) {
                    StringBuilder sb = new StringBuilder();

                    List<OrderDetailBean> orderDetailList = order
                            .getOrderDetailList();
                    for (OrderDetailBean orderDetail : orderDetailList) {
                        sb.setLength(0);
                        sb.append("OrdrId:[");
                        sb.append(String.format("%2s", order.getOrdrId()));
                        sb.append("],");
                        sb.append("CustId:[");
                        sb.append(String.format("%4s", order.getCustId()));
                        sb.append("],");
                        sb.append("OrderDate:[");
                        sb.append(String.format("%19s", order.getOrderDate()));
                        sb.append("],");

                        sb.append("DetlId:[");
                        sb
                                .append(String.format("%2s", orderDetail
                                        .getDetlId()));
                        sb.append("],");
                        sb.append("ProdId:[");
                        sb
                                .append(String.format("%3s", orderDetail
                                        .getProdId()));
                        sb.append("],");
                        sb.append("Quantity:[");
                        sb.append(String.format("%2s", orderDetail
                                .getQuantity()));
                        sb.append("],");
                        sb.append("Amount:[");
                        sb
                                .append(String.format("%5s", orderDetail
                                        .getAmount()));
                        sb.append("],");
                        sb.append("CustName:[");
                        sb.append(String.format("%12s", order.getCustName()));
                        sb.append("]");
                        sb.append("ProdName:[");
                        sb.append(String.format("%10s", orderDetail
                                .getProdName()));
                        sb.append("]");
                        if (true) {
                            logger.info(sb.toString());
                        }
                        count_detail++;
                    }
                    logger.info("-----");
                }

                count_first++;
            }
        } finally {
            DBValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(4, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBValidateCollector#DBValidateCollector(Object, String, Object, int, boolean, jp.terasoluna.fw.collector.exception.CollectorExceptionHandler, DBCollectorPrePostProcess, org.springframework.validation.Validator, jp.terasoluna.fw.collector.validate.ValidationErrorHandler)}
     * のためのテスト・メソッド。
     */
    public void testDBValidateCollectorTestObjectStringObject005()
                                                                             throws Exception {
        if (this.userListQueryRowHandleDao == null) {
            fail("userListQueryRowHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;
        Validator validator = null;

        Collector<OrderBean> it = new DBValidateCollector<OrderBean>(
                this.userListQueryRowHandleDao, "collectOrder", null, 1, true, null,
                null, validator, null);
        try {
            for (OrderBean order : it) {
                // OrderBean nextOrder = it.following();
                if (logger.isInfoEnabled()) {
                    StringBuilder sb = new StringBuilder();

                    List<OrderDetailBean> orderDetailList = order
                            .getOrderDetailList();
                    for (OrderDetailBean orderDetail : orderDetailList) {
                        sb.setLength(0);
                        sb.append("OrdrId:[");
                        sb.append(String.format("%2s", order.getOrdrId()));
                        sb.append("],");
                        sb.append("CustId:[");
                        sb.append(String.format("%4s", order.getCustId()));
                        sb.append("],");
                        sb.append("OrderDate:[");
                        sb.append(String.format("%19s", order.getOrderDate()));
                        sb.append("],");

                        sb.append("DetlId:[");
                        sb
                                .append(String.format("%2s", orderDetail
                                        .getDetlId()));
                        sb.append("],");
                        sb.append("ProdId:[");
                        sb
                                .append(String.format("%3s", orderDetail
                                        .getProdId()));
                        sb.append("],");
                        sb.append("Quantity:[");
                        sb.append(String.format("%2s", orderDetail
                                .getQuantity()));
                        sb.append("],");
                        sb.append("Amount:[");
                        sb
                                .append(String.format("%5s", orderDetail
                                        .getAmount()));
                        sb.append("],");
                        sb.append("CustName:[");
                        sb.append(String.format("%12s", order.getCustName()));
                        sb.append("]");
                        sb.append("ProdName:[");
                        sb.append(String.format("%10s", orderDetail
                                .getProdName()));
                        sb.append("]");
                        if (true) {
                            logger.info(sb.toString());
                        }
                        count_detail++;
                    }
                    logger.info("-----");
                }

                count_first++;
            }
        } finally {
            DBValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(4, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBValidateCollector#DBValidateCollector(Object, String, Object, boolean, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    public void testDBValidateCollectorTestObjectStringObject006()
                                                                             throws Exception {
        if (this.userListQueryRowHandleDao == null) {
            fail("userListQueryRowHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;
        Validator validator = null;

        Collector<Order2Bean> it = new DBValidateCollector<Order2Bean>(
                this.userListQueryRowHandleDao, "collectOrder2", null, validator);
        try {
            for (Order2Bean order : it) {

                // 注文IDが切り替わったら
                if (ControlBreakChecker.isPreBreak(it, "ordrId")) {
                    // コントロールブレイク前処理
                    if (logger.isInfoEnabled()) {
                        logger.info("コントロールブレイク前処理");
                    }
                }

                if (logger.isInfoEnabled()) {
                    StringBuilder sb = new StringBuilder();

                    OrderDetailBean orderDetail = order.getOrderDetail();
                    if (orderDetail != null) {
                        sb.setLength(0);
                        sb.append("OrdrId:[");
                        sb.append(String.format("%2s", order.getOrdrId()));
                        sb.append("],");
                        sb.append("CustId:[");
                        sb.append(String.format("%4s", order.getCustId()));
                        sb.append("],");
                        sb.append("OrderDate:[");
                        sb.append(String.format("%19s", order.getOrderDate()));
                        sb.append("],");

                        sb.append("DetlId:[");
                        sb
                                .append(String.format("%2s", orderDetail
                                        .getDetlId()));
                        sb.append("],");
                        sb.append("ProdId:[");
                        sb
                                .append(String.format("%3s", orderDetail
                                        .getProdId()));
                        sb.append("],");
                        sb.append("Quantity:[");
                        sb.append(String.format("%2s", orderDetail
                                .getQuantity()));
                        sb.append("],");
                        sb.append("Amount:[");
                        sb
                                .append(String.format("%5s", orderDetail
                                        .getAmount()));
                        sb.append("],");
                        sb.append("CustName:[");
                        sb.append(String.format("%12s", order.getCustName()));
                        sb.append("]");
                        sb.append("ProdName:[");
                        sb.append(String.format("%10s", orderDetail
                                .getProdName()));
                        sb.append("]");
                        logger.info(sb.toString());
                        count_detail++;
                    }
                    logger.info("-----");
                }

                // 注文IDが切り替わったら
                if (ControlBreakChecker.isBreak(it, "ordrId")) {
                    // コントロールブレイク後処理
                    if (logger.isInfoEnabled()) {
                        logger.info("コントロールブレイク後処理");
                    }
                }

                count_first++;
            }
        } finally {
            DBValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(12, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBValidateCollector#DBValidateCollector(Object, String, Object, boolean, org.springframework.validation.Validator)}
     * のためのテスト・メソッド。
     */
    public void testDBValidateCollectorTestObjectStringObject007()
                                                                             throws Exception {
        if (this.userListQueryRowHandleDao == null) {
            fail("userListQueryRowHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;
        Validator validator = null;

        Collector<Order2Bean> it = new DBValidateCollector<Order2Bean>(
                this.userListQueryRowHandleDao, "collectOrder2", null, true, validator);
        try {
            for (Order2Bean order : it) {
                @SuppressWarnings("unused")
                Order2Bean prevOrder = it.getPrevious();
                @SuppressWarnings("unused")
                Order2Bean nextOrder = it.getNext();
                if (logger.isInfoEnabled()) {
                    StringBuilder sb = new StringBuilder();

                    OrderDetailBean orderDetail = order.getOrderDetail();
                    if (orderDetail != null) {
                        sb.setLength(0);
                        sb.append("OrdrId:[");
                        sb.append(String.format("%2s", order.getOrdrId()));
                        sb.append("],");
                        sb.append("CustId:[");
                        sb.append(String.format("%4s", order.getCustId()));
                        sb.append("],");
                        sb.append("OrderDate:[");
                        sb.append(String.format("%19s", order.getOrderDate()));
                        sb.append("],");

                        sb.append("DetlId:[");
                        sb
                                .append(String.format("%2s", orderDetail
                                        .getDetlId()));
                        sb.append("],");
                        sb.append("ProdId:[");
                        sb
                                .append(String.format("%3s", orderDetail
                                        .getProdId()));
                        sb.append("],");
                        sb.append("Quantity:[");
                        sb.append(String.format("%2s", orderDetail
                                .getQuantity()));
                        sb.append("],");
                        sb.append("Amount:[");
                        sb
                                .append(String.format("%5s", orderDetail
                                        .getAmount()));
                        sb.append("],");
                        sb.append("CustName:[");
                        sb.append(String.format("%12s", order.getCustName()));
                        sb.append("]");
                        sb.append("ProdName:[");
                        sb.append(String.format("%10s", orderDetail
                                .getProdName()));
                        sb.append("]");
                        logger.info(sb.toString());
                        count_detail++;
                    }
                    logger.info("-----");
                }

                count_first++;
            }
        } finally {
            DBValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(12, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBValidateCollector#DBValidateCollector(Object, String, Object, org.springframework.validation.Validator, jp.terasoluna.fw.collector.validate.ValidationErrorHandler)}
     * のためのテスト・メソッド。
     */
    public void testDBValidateCollectorTestObjectStringObject008()
                                                                             throws Exception {
        if (this.userListQueryRowHandleDao == null) {
            fail("userListQueryRowHandleDaoがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;
        Validator validator = null;

        Collector<Order2Bean> it = new DBValidateCollector<Order2Bean>(
                this.userListQueryRowHandleDao, "collectOrder2", null, 1, true, null,
                null, validator, null);
        try {
            for (Order2Bean order : it) {
                @SuppressWarnings("unused")
                Order2Bean prevOrder = it.getPrevious();
                @SuppressWarnings("unused")
                Order2Bean nextOrder = it.getNext();
                if (logger.isInfoEnabled()) {
                    StringBuilder sb = new StringBuilder();

                    OrderDetailBean orderDetail = order.getOrderDetail();
                    if (orderDetail != null) {
                        sb.setLength(0);
                        sb.append("OrdrId:[");
                        sb.append(String.format("%2s", order.getOrdrId()));
                        sb.append("],");
                        sb.append("CustId:[");
                        sb.append(String.format("%4s", order.getCustId()));
                        sb.append("],");
                        sb.append("OrderDate:[");
                        sb.append(String.format("%19s", order.getOrderDate()));
                        sb.append("],");

                        sb.append("DetlId:[");
                        sb
                                .append(String.format("%2s", orderDetail
                                        .getDetlId()));
                        sb.append("],");
                        sb.append("ProdId:[");
                        sb
                                .append(String.format("%3s", orderDetail
                                        .getProdId()));
                        sb.append("],");
                        sb.append("Quantity:[");
                        sb.append(String.format("%2s", orderDetail
                                .getQuantity()));
                        sb.append("],");
                        sb.append("Amount:[");
                        sb
                                .append(String.format("%5s", orderDetail
                                        .getAmount()));
                        sb.append("],");
                        sb.append("CustName:[");
                        sb.append(String.format("%12s", order.getCustName()));
                        sb.append("]");
                        sb.append("ProdName:[");
                        sb.append(String.format("%10s", orderDetail
                                .getProdName()));
                        sb.append("]");
                        logger.info(sb.toString());
                        count_detail++;
                    }
                    logger.info("-----");
                }

                count_first++;
            }
        } finally {
            DBValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(12, count_first);
        assertEquals(12, count_detail);
    }

}
