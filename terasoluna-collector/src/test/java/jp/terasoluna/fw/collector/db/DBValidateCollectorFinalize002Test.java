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
import jp.terasoluna.fw.exception.SystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

/**
 * DBValidateCollectorTest
 */
public class DBValidateCollectorFinalize002Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory
            .getLog(DBValidateCollectorFinalize002Test.class);

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
     * {@link jp.terasoluna.fw.collector.db.DBCollector#DBIteratorTest(jp.terasoluna.fw.dao.QueryRowHandleDAO, java.lang.String, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testDBValidateCollectorFinalize002() throws Exception {
        if (this.queryRowHandleDAO == null) {
            fail("queryRowHandleDAOがnullです。");
        }

        Validator validator = null;

        Collector<UserBean> it = new DBValidateCollector<UserBean>(
                this.queryRowHandleDAO, "selectUserList", null, validator);
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

                // あえて途中で抜ける
                break;
            }
        } catch (Throwable e) {
            throw new SystemException(e);
        } finally {
            // クローズ
            DBValidateCollector.closeQuietly(it);
        }

        // コレクタスレッド数チェック
        assertTrue(CollectorTestUtil
                .lessThanCollectorThreadCount(0 + this.previousThreadCount));
    }

}
