/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import java.sql.SQLException;
import java.util.List;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.util.ControlBreakChecker;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.dao.QueryRowHandleDAO;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;
import jp.terasoluna.fw.exception.SystemException;
import junit.framework.AssertionFailedError;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

/**
 * DBCollectorTest
 */
public class DBCollector002Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(DBCollector002Test.class);

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
    protected void onSetUpBeforeTransaction() throws Exception {
        DBCollector.setVerbose(true);
        super.onSetUpBeforeTransaction();
    }

    @Override
    protected void onTearDownAfterTransaction() throws Exception {
        DBCollector.setVerbose(false);
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
     * {@link jp.terasoluna.fw.collector.db.DBCollector#DBIteratorTest(jp.terasoluna.fw.dao.QueryRowHandleDAO, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDBCollectorTestQueryRowHandleDAOStringObject001()
                                                                     throws Exception {
        if (this.queryRowHandleDAO == null) {
            fail("queryRowHandleDAOがnullです。");
        }

        int count_first = 0;
        int retryCount = 3;
        boolean retryFlg = false;

        do {
            retryFlg = false;
            Collector<UserBean> col = new DBCollector<UserBean>(
                    this.queryRowHandleDAO, "selectUserList", null);
            try {
                for (UserBean user : col) {
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
                }
            } catch (Throwable e) {
                if (e instanceof AssertionFailedError) {
                    throw (AssertionFailedError) e;
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
                DBCollector.closeQuietly(col);
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

            Collector<UserBean> col2 = new DBCollector<UserBean>(
                    this.queryRowHandleDAO, "selectUserList", null);
            try {
                for (UserBean user : col2) {
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
                }
            } finally {
                DBCollector.closeQuietly(col2);
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
     * {@link jp.terasoluna.fw.collector.db.DBCollector#DBCollectorTest(jp.terasoluna.fw.dao.QueryRowHandleDAO, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDBCollectorTestQueryRowHandleDAOStringObject002()
                                                                     throws Exception {
        if (this.queryRowHandleDAO == null) {
            fail("queryRowHandleDAOがnullです。");
        }

        int count_first = 0;

        DBCollectorConfig config = new DBCollectorConfig(
                this.queryRowHandleDAO, "selectUserList", null);
        config.addExecuteByConstructor(true);

        Collector<UserBean> col = new DBCollector<UserBean>(config);
        try {
            for (UserBean user : col) {
                UserBean prevUser = null;
                UserBean nextUser = null;
                UserBean currentUser = null;

                if (count_first > 4) {
                    prevUser = col.getPrevious();
                    nextUser = col.getNext();
                    currentUser = col.getCurrent();
                }

                // ユーザIDが切り替わったら
                if (ControlBreakChecker.isPreBreak(col, "userId")) {
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
                if (ControlBreakChecker.isBreak(col, "userId")) {
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
            DBCollector.closeQuietly(col);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        for (int i = 0; i < 2; i++) {
            int count = 0;

            long startTime = System.currentTimeMillis();

            Collector<UserBean> col2 = new DBCollector<UserBean>(
                    this.queryRowHandleDAO, "selectUserList", null);
            try {
                for (UserBean user : col2) {
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
                }
            } finally {
                DBCollector.closeQuietly(col2);
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
     * {@link jp.terasoluna.fw.collector.db.DBCollector#DBCollectorTest(jp.terasoluna.fw.dao.QueryRowHandleDAO, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDBCollectorTestQueryRowHandleDAOStringObject003()
                                                                     throws Exception {
        if (this.queryRowHandleDAO == null) {
            fail("queryRowHandleDAOがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;

        Collector<OrderBean> col = new DBCollector<OrderBean>(
                this.queryRowHandleDAO, "selectOrder", null);
        try {
            for (OrderBean order : col) {
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
            DBCollector.closeQuietly(col);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(4, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollector#DBCollectorTest(jp.terasoluna.fw.dao.QueryRowHandleDAO, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDBCollectorTestQueryRowHandleDAOStringObject004()
                                                                     throws Exception {
        if (this.queryRowHandleDAO == null) {
            fail("queryRowHandleDAOがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;

        Collector<OrderBean> col = new DBCollector<OrderBean>(
                this.queryRowHandleDAO, "selectOrder", null, true);
        try {
            for (OrderBean order : col) {
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
            DBCollector.closeQuietly(col);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(4, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollector#DBCollectorTest(jp.terasoluna.fw.dao.QueryRowHandleDAO, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDBCollectorTestQueryRowHandleDAOStringObject005()
                                                                     throws Exception {
        if (this.queryRowHandleDAO == null) {
            fail("queryRowHandleDAOがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;

        DBCollectorPrePostProcess prepost = null;

        Collector<OrderBean> col = new DBCollector<OrderBean>(
                this.queryRowHandleDAO, "selectOrder", null, 1, true, null,
                prepost);
        try {
            for (OrderBean order : col) {
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
            DBCollector.closeQuietly(col);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(4, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollector#DBCollectorTest(jp.terasoluna.fw.dao.QueryRowHandleDAO, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDBCollectorTestQueryRowHandleDAOStringObject006()
                                                                     throws Exception {
        if (this.queryRowHandleDAO == null) {
            fail("queryRowHandleDAOがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;

        Collector<OrderBean> col = new DBCollector<OrderBean>(
                this.queryRowHandleDAO, "selectOrder2", null);
        try {
            for (OrderBean order : col) {

                // 注文IDが切り替わったら
                if (ControlBreakChecker.isPreBreak(col, "ordrId")) {
                    // コントロールブレイク前処理
                    if (logger.isInfoEnabled()) {
                        logger.info("コントロールブレイク前処理");
                    }
                }

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

                // 注文IDが切り替わったら
                if (ControlBreakChecker.isBreak(col, "ordrId")) {
                    // コントロールブレイク後処理
                    if (logger.isInfoEnabled()) {
                        logger.info("コントロールブレイク後処理");
                    }
                }

                count_first++;
            }
        } finally {
            DBCollector.closeQuietly(col);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(12, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollector#DBCollectorTest(jp.terasoluna.fw.dao.QueryRowHandleDAO, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDBCollectorTestQueryRowHandleDAOStringObject007()
                                                                     throws Exception {
        if (this.queryRowHandleDAO == null) {
            fail("queryRowHandleDAOがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;

        Collector<OrderBean> col = new DBCollector<OrderBean>(
                this.queryRowHandleDAO, "selectOrder2", null, true);
        try {
            for (OrderBean order : col) {
                @SuppressWarnings("unused")
                OrderBean prevOrder = col.getPrevious();
                @SuppressWarnings("unused")
                OrderBean nextOrder = col.getNext();
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
            DBCollector.closeQuietly(col);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));

        assertEquals(12, count_first);
        assertEquals(12, count_detail);
    }

    /**
     * {@link jp.terasoluna.fw.collector.db.DBCollector#DBCollectorTest(jp.terasoluna.fw.dao.QueryRowHandleDAO, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDBCollectorTestQueryRowHandleDAOStringObject008()
                                                                     throws Exception {
        if (this.queryRowHandleDAO == null) {
            fail("queryRowHandleDAOがnullです。");
        }

        int count_first = 0;
        int count_detail = 0;

        DBCollectorPrePostProcessStub prepost = new DBCollectorPrePostProcessStub();

        Collector<OrderBean> col = new DBCollector<OrderBean>(
                this.queryRowHandleDAO, "selectOrder2", null, 1, true, null,
                prepost);
        try {
            for (OrderBean order : col) {
                @SuppressWarnings("unused")
                OrderBean prevOrder = col.getPrevious();
                @SuppressWarnings("unused")
                OrderBean nextOrder = col.getNext();
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
            DBCollector.closeQuietly(col);
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
