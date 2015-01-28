/**
 * 
 */
package jp.terasoluna.fw.collector.db;

import java.util.List;

import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.dao.UserListQueryResultHandleDao;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * DaoCollectorTest
 * Callの動作確認と、DaoCollectorPrePostProcess連携の確認用
 */
public class DaoCollector007Test extends DaoTestCase {

    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(DaoCollector007Test.class);

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
    	DaoCollectorPrePostProcessStub002 dbcppp = new DaoCollectorPrePostProcessStub002();
    	// configの引数に指定したSQLIDは存在しないテーブルを参照（CallでExceptionを起こさせる）
    	DaoCollectorConfig config = new DaoCollectorConfig(
    			this.userListQueryResultHandleDao, "collectDummy", null);
    	config.setExecuteByConstructor(true);
    	config.setDaoCollectorPrePostProcess(dbcppp);
    	DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(config);
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
    	DaoCollectorPrePostProcessStub004 dbcppp = new DaoCollectorPrePostProcessStub004();
    	DaoCollectorConfig config = new DaoCollectorConfig(
    			this.userListQueryResultHandleDao, "collect", null);
    	config.setExecuteByConstructor(true);
    	config.setDaoCollectorPrePostProcess(dbcppp);
    	DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(config);
    	dbc.resultHandler = new QueueingResultHandlerImpl();
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
    	DaoCollectorPrePostProcessStub004 dbcppp = new DaoCollectorPrePostProcessStub004();
    	// configの引数に指定したSQLIDは存在しないテーブルを参照（CallでExceptionを起こさせる）
    	DaoCollectorConfig config = new DaoCollectorConfig(
    			this.userListQueryResultHandleDao, "collectDummy", null);
    	config.setExecuteByConstructor(true);
    	config.setDaoCollectorPrePostProcess(dbcppp);
    	DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(config);
    	dbc.resultHandler = new QueueingResultHandlerImpl();
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
    	DaoCollectorPrePostProcessStub005 dbcppp = new DaoCollectorPrePostProcessStub005();
    	// configの引数に指定したSQLIDは存在しないテーブルを参照（CallでExceptionを起こさせる）
    	DaoCollectorConfig config = new DaoCollectorConfig(
    			this.userListQueryResultHandleDao, "collectDummy", null);
    	config.setExecuteByConstructor(true);
    	config.setDaoCollectorPrePostProcess(dbcppp);
    	DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(config);
    	dbc.resultHandler = new QueueingResultHandlerImpl();
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
    	DaoCollectorPrePostProcessStub006 dbcppp = new DaoCollectorPrePostProcessStub006();
    	// configの引数に指定したSQLIDは存在しないテーブルを参照（CallでExceptionを起こさせる）
    	DaoCollectorConfig config = new DaoCollectorConfig(
    			this.userListQueryResultHandleDao, "collectDummy", null);
    	config.setExecuteByConstructor(true);
    	config.setDaoCollectorPrePostProcess(dbcppp);
    	DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(config);
    	dbc.resultHandler = new QueueingResultHandlerImpl();
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
     * DaoCollectorからDaoCollectorPrePostProcess#preprocess(DaoCollector<P> collector)
     * への値の受け渡しが正常にできることを確認する
     */
    @Test
    public void testPreprocess001() throws Exception{
    	DaoCollectorPrePostProcessStub003 dbcppp = new DaoCollectorPrePostProcessStub003();
    	DaoCollectorConfig config = new DaoCollectorConfig(
    			this.userListQueryResultHandleDao, "collectDummy", null);
    	config.setDaoCollectorPrePostProcess(dbcppp);
    	DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(config);
    	// preprocess実行前の確認（resultHandlerはnull）
    	assertNull(dbc.resultHandler);
    	
    	// preprocess実行（パラメータが正常に渡ればresultHandlerが設定される）
    	dbc.preprocess();
    	
    	// preprocess実行後確認（resultHandlerが設定されていること）
    	assertTrue(dbc.resultHandler instanceof QueueingResultHandlerImpl);
    }

    /**
     * postprocessException(Throwable th)のテスト
     * DaoCollectorからDaoCollectorPrePostProcess#postprocessException(DaoCollector<P> collector, Throwable throwable)
     * への値の受け渡しが正常にできることを確認する
     */
    @Test
    public void testPostprocessException001() throws Exception {
    	DaoCollectorPrePostProcessStub003 dbcppp = new DaoCollectorPrePostProcessStub003();
    	DaoCollectorConfig config = new DaoCollectorConfig(
    			this.userListQueryResultHandleDao, "collectDummy", null);
    	config.setDaoCollectorPrePostProcess(dbcppp);
    	DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(config);
    	
    	Exception ex = new Exception("postprocessExceptionテスト");
    	// preprocess実行（パラメータが正常に渡ればDaoCollectorPreProcessStatus.THROWが戻り値になる）
    	DaoCollectorPrePostProcessStatus status = dbc.postprocessException(ex);
    	
    	// preprocess実行後確認（statusがTHROWならOK）
    	assertEquals(DaoCollectorPrePostProcessStatus.THROW, status);

    }

    /**
     * postprocessComplete()のテスト
     * DaoCollectorからDaoCollectorPrePostProcess#postprocessComplete(DaoCollector<P> collector)
     * への値の受け渡しが正常にできることを確認する
     */
    @Test
    public void testPostprocessComplete001() throws Exception {
    	DaoCollectorPrePostProcessStub003 dbcppp = new DaoCollectorPrePostProcessStub003();
    	DaoCollectorConfig config = new DaoCollectorConfig(
    			this.userListQueryResultHandleDao, "collectDummy", null);
    	config.setDaoCollectorPrePostProcess(dbcppp);
    	DaoCollector<UserBean> dbc = new DaoCollector<UserBean>(config);
    	// preprocess実行前の確認（resultHandlerはnull）
    	assertNull(dbc.resultHandler);
    	
    	// preprocess実行（パラメータが正常に渡ればresultHandlerが設定される）
    	dbc.postprocessComplete();
    	
    	// preprocess実行後確認（resultHandlerが設定されていること）
    	assertTrue(dbc.resultHandler instanceof QueueingResultHandlerImpl);

    }
}
