/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import java.util.List;

import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.dao.QueryRowHandleDAO;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * DBCollectorTest
 * Callの動作確認と、DBCollectorPrePostProcess連携の確認用
 */
public class DBCollector007Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(DBCollector007Test.class);

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
     * Call()のテスト（リトライ機能）
     * Call()メソッドの処理において、expStatusがRETRYの時にループ処理を繰り返し実行することを確認する
     */
    @Test
    public void testCall001() {
    	DBCollectorPrePostProcessStub002 dbcppp = new DBCollectorPrePostProcessStub002();
    	// configの引数に指定したSQLIDは存在しないテーブルを参照（CallでExceptionを起こさせる）
    	DBCollectorConfig config = new DBCollectorConfig(
    			this.queryRowHandleDAO, "selectUserListDummy", null);
    	config.setExecuteByConstructor(true);
    	config.setDbCollectorPrePostProcess(dbcppp);
    	DBCollector<UserBean> dbc = new DBCollector<UserBean>(config);
    	try {
    		dbc.call();
    	} catch (Exception e) {
    		fail();
    	}
    	
    	assertTrue(dbcppp.getRetryFlag());
    	
    }
    
    /**
     * Call()のテスト
     * SQL実行時にエラーが発生せず正常終了する場合
     * @throws Exception
     */
    @Test
    public void testCall002() throws Exception {
    	DBCollectorPrePostProcessStub004 dbcppp = new DBCollectorPrePostProcessStub004();
    	DBCollectorConfig config = new DBCollectorConfig(
    			this.queryRowHandleDAO, "selectUserList", null);
    	config.setExecuteByConstructor(true);
    	config.setDbCollectorPrePostProcess(dbcppp);
    	DBCollector<UserBean> dbc = new DBCollector<UserBean>(config);
    	dbc.rowHandler = new QueueingDataRowHandlerImpl();
    	Integer returncode = new Integer(99);
    	
    	// Call実行
    	try {
    		returncode = dbc.call();
    	} catch (Exception e) {
    		fail();
    	}
    	
    	// Call戻り値確認
    	assertEquals(0, returncode.intValue());
    	// PrePostProcess実行結果確認
    	assertTrue(dbcppp.getExecPreprocFlg());
    	assertTrue(dbcppp.getExecPostProcCompFlg());
    	assertFalse(dbcppp.getExecPostProcExcpFlg());
    }

    /**
     * Call()のテスト
     * SQL実行時にエラーが発生し、エラーのステータスがTHROWである場合
     * @throws Exception
     */
    @Test
    public void testCall003() throws Exception {
    	DBCollectorPrePostProcessStub004 dbcppp = new DBCollectorPrePostProcessStub004();
    	// configの引数に指定したSQLIDは存在しないテーブルを参照（CallでExceptionを起こさせる）
    	DBCollectorConfig config = new DBCollectorConfig(
    			this.queryRowHandleDAO, "selectUserListDummy", null);
    	config.setExecuteByConstructor(true);
    	config.setDbCollectorPrePostProcess(dbcppp);
    	DBCollector<UserBean> dbc = new DBCollector<UserBean>(config);
    	dbc.rowHandler = new QueueingDataRowHandlerImpl();
    	Integer returncode = new Integer(99);
    	
    	// Call実行
    	try {
    		returncode = dbc.call();
    	} catch (Exception e) {
    		fail();
    	}
    	
    	// Call戻り値確認
    	assertEquals(-1, returncode.intValue());
    	// PrePostProcess実行結果確認
    	assertTrue(dbcppp.getExecPreprocFlg());
    	assertTrue(dbcppp.getExecPostProcCompFlg());
    	assertTrue(dbcppp.getExecPostProcExcpFlg());
    }

    /**
     * Call()のテスト
     * SQL実行時にエラーが発生し、エラーのステータスがNULLである場合
     * @throws Exception
     */
    @Test
    public void testCall004() throws Exception {
    	DBCollectorPrePostProcessStub005 dbcppp = new DBCollectorPrePostProcessStub005();
    	// configの引数に指定したSQLIDは存在しないテーブルを参照（CallでExceptionを起こさせる）
    	DBCollectorConfig config = new DBCollectorConfig(
    			this.queryRowHandleDAO, "selectUserListDummy", null);
    	config.setExecuteByConstructor(true);
    	config.setDbCollectorPrePostProcess(dbcppp);
    	DBCollector<UserBean> dbc = new DBCollector<UserBean>(config);
    	dbc.rowHandler = new QueueingDataRowHandlerImpl();
    	Integer returncode = new Integer(99);
    	
    	// Call実行
    	try {
    		returncode = dbc.call();
    	} catch (Exception e) {
    		fail();
    	}
    	
    	// Call戻り値確認
    	assertEquals(-1, returncode.intValue());
    	// PrePostProcess実行結果確認
    	assertTrue(dbcppp.getExecPreprocFlg());
    	assertTrue(dbcppp.getExecPostProcCompFlg());
    	assertTrue(dbcppp.getExecPostProcExcpFlg());
    }

    /**
     * Call()のテスト
     * SQL実行時にエラーが発生し、エラーのステータスがENDである場合
     * @throws Exception
     */
    @Test
    public void testCall005() throws Exception {
    	DBCollectorPrePostProcessStub006 dbcppp = new DBCollectorPrePostProcessStub006();
    	// configの引数に指定したSQLIDは存在しないテーブルを参照（CallでExceptionを起こさせる）
    	DBCollectorConfig config = new DBCollectorConfig(
    			this.queryRowHandleDAO, "selectUserListDummy", null);
    	config.setExecuteByConstructor(true);
    	config.setDbCollectorPrePostProcess(dbcppp);
    	DBCollector<UserBean> dbc = new DBCollector<UserBean>(config);
    	dbc.rowHandler = new QueueingDataRowHandlerImpl();
    	Integer returncode = new Integer(99);
    	
    	// Call実行
    	try {
    		returncode = dbc.call();
    	} catch (Exception e) {
    		fail();
    	}
    	
    	// Call戻り値確認
    	assertEquals(0, returncode.intValue());
    	// PrePostProcess実行結果確認
    	assertTrue(dbcppp.getExecPreprocFlg());
    	assertTrue(dbcppp.getExecPostProcCompFlg());
    	assertTrue(dbcppp.getExecPostProcExcpFlg());
    }
    
    /**
     * Call()のテスト
     * SQL実行時にエラーが発生し、エラーのステータスがRETRYである場合
     * @throws Exception
     */
//    @Test
//    public void testCall00x() throws Exception {
//    	// testCall001と内容が重複するため省略
//    	fail();
//    }
    
    
    /**
     * preprocess()のテスト
     * DBCollectorからDBCollectorPrePostProcess#preprocess(DBCollector<P> collector)
     * への値の受け渡しが正常にできることを確認する
     */
    @Test
    public void testPreprocess001() throws Exception{
    	DBCollectorPrePostProcessStub003 dbcppp = new DBCollectorPrePostProcessStub003();
    	DBCollectorConfig config = new DBCollectorConfig(
    			this.queryRowHandleDAO, "selectUserListDummy", null);
    	config.setDbCollectorPrePostProcess(dbcppp);
    	DBCollector<UserBean> dbc = new DBCollector<UserBean>(config);
    	// preprocess実行前の確認（rowHandlerはnull）
    	assertNull(dbc.rowHandler);
    	
    	// preprocess実行（パラメータが正常に渡ればrowHandlerが設定される）
    	dbc.preprocess();
    	
    	// preprocess実行後確認（rowHandlerが設定されていること）
    	assertTrue(dbc.rowHandler instanceof QueueingDataRowHandlerImpl);
    }

    /**
     * postprocessException(Throwable th)のテスト
     * DBCollectorからDBCollectorPrePostProcess#postprocessException(DBCollector<P> collector, Throwable throwable)
     * への値の受け渡しが正常にできることを確認する
     */
    @Test
    public void testPostprocessException001() throws Exception {
    	DBCollectorPrePostProcessStub003 dbcppp = new DBCollectorPrePostProcessStub003();
    	DBCollectorConfig config = new DBCollectorConfig(
    			this.queryRowHandleDAO, "selectUserListDummy", null);
    	config.setDbCollectorPrePostProcess(dbcppp);
    	DBCollector<UserBean> dbc = new DBCollector<UserBean>(config);
    	
    	Exception ex = new Exception("postprocessExceptionテスト");
    	// preprocess実行（パラメータが正常に渡ればDBCollectorPreProcessStatus.THROWが戻り値になる）
    	DBCollectorPrePostProcessStatus status = dbc.postprocessException(ex);
    	
    	// preprocess実行後確認（statusがTHROWならOK）
    	assertEquals(DBCollectorPrePostProcessStatus.THROW, status);

    }

    /**
     * postprocessComplete()のテスト
     * DBCollectorからDBCollectorPrePostProcess#postprocessComplete(DBCollector<P> collector)
     * への値の受け渡しが正常にできることを確認する
     */
    @Test
    public void testPostprocessComplete001() throws Exception {
    	DBCollectorPrePostProcessStub003 dbcppp = new DBCollectorPrePostProcessStub003();
    	DBCollectorConfig config = new DBCollectorConfig(
    			this.queryRowHandleDAO, "selectUserListDummy", null);
    	config.setDbCollectorPrePostProcess(dbcppp);
    	DBCollector<UserBean> dbc = new DBCollector<UserBean>(config);
    	// preprocess実行前の確認（rowHandlerはnull）
    	assertNull(dbc.rowHandler);
    	
    	// preprocess実行（パラメータが正常に渡ればrowHandlerが設定される）
    	dbc.postprocessComplete();
    	
    	// preprocess実行後確認（rowHandlerが設定されていること）
    	assertTrue(dbc.rowHandler instanceof QueueingDataRowHandlerImpl);

    }
}
