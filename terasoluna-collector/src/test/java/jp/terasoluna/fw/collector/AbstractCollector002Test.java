package jp.terasoluna.fw.collector;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import jp.terasoluna.fw.collector.concurrent.ArrayBlockingQueueEx;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandlerStatus;
import jp.terasoluna.fw.collector.validate.ValidateErrorStatus;
import jp.terasoluna.fw.collector.vo.CollectorStatus;
import jp.terasoluna.fw.collector.vo.DataValueObject;
import jp.terasoluna.fw.ex.unit.util.ReflectionUtils;
import jp.terasoluna.fw.exception.SystemException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AbstractCollector002Test {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        AbstractCollector.setVerbose(true);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        AbstractCollector.setVerbose(false);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * 
     */
    @Test
    public void testExecute001() {
        AbstractCollectorStubHasNext1<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext1<AbstractCollectorTestBean>();

        // テスト
        try {
            col.execute();
        } catch (SystemException e) {
            assertNotNull(e);
            assertEquals(SystemException.class, e.getClass());
            return;            
        } finally {
            col.close();            
        }

        fail();
        return;
    }

    /**
     * 
     */
    @Test
    public void testHasNext001() {
        AbstractCollectorStubHasNext2<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext2<AbstractCollectorTestBean>();

        // テスト
        boolean result = col.hasNext();

        col.close();
        assertFalse(result);
    }

    /**
     * 
     */
    @Test
    public void testHasNext002() {
        AbstractCollectorStubHasNext3<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext3<AbstractCollectorTestBean>();

        // テスト
        boolean result = col.hasNext();

        col.close();
        assertFalse(result);
    }

    /**
     * 
     */
    @Test
    public void testHasNext003() {
        AbstractCollectorStubHasNext3<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext3<AbstractCollectorTestBean>();

        // テスト
        boolean result = col.hasNext();

        col.close();
        assertFalse(result);
    }

    /**
     * 
     */
    @Test
    public void testHasNext004() {
        AbstractCollectorStubHasNext4<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext4<AbstractCollectorTestBean>();

        // テスト
        boolean result = col.hasNext();

        col.close();
        assertFalse(result);
    }

    /**
     * 
     */
    @Test
    public void testNext001() {
        AbstractCollectorStubNext1<AbstractCollectorTestBean> col = new AbstractCollectorStubNext1<AbstractCollectorTestBean>();

        AbstractCollectorTestBean result = null;
        try {
            result = col.next();
            fail();
        } catch (NoSuchElementException e) {
            assertNotNull(e);
            assertNull(result);
        } finally {
            col.close();            
        }
    }

    /**
     * 
     */
    @Test
    public void testNext002() {
        AbstractCollectorStubNext2<AbstractCollectorTestBean> col = new AbstractCollectorStubNext2<AbstractCollectorTestBean>();

        @SuppressWarnings("unused")
        AbstractCollectorTestBean result = null;
        try {
            result = col.next();
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals(SystemException.class, e.getClass());
            assertEquals(Exception.class, e.getCause().getClass());
            assertEquals(null, e.getCause().getMessage());
            return;
        } finally {
            col.close();
        }

        fail();
    }

    /**
     * 
     */
    @Test
    public void testNext003() {
        AbstractCollectorStubNext3<AbstractCollectorTestBean> col = new AbstractCollectorStubNext3<AbstractCollectorTestBean>();

        @SuppressWarnings("unused")
        AbstractCollectorTestBean result = null;
        try {
            result = col.next();
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals(SystemException.class, e.getClass());
            assertEquals(Exception.class, e.getCause().getClass());
            assertEquals("hoge", e.getCause().getMessage());
            return;
        } finally {
            col.close();
        }

        fail();
    }

    /**
     * 
     */
    @Test
    public void testNext004() {
        AbstractCollectorStubNext4<AbstractCollectorTestBean> col = new AbstractCollectorStubNext4<AbstractCollectorTestBean>();
        col.exceptionHandler = new CollectorExceptionHandlerStub1();

        AbstractCollectorTestBean result = null;

        try {
            result = col.next();
            fail();
        } catch (NoSuchElementException e) {
            assertNotNull(e);
            assertNull(result);
        } finally {
            col.close();
        }
    }

    /**
     * 
     */
    @Test
    public void testGetNext() {
        AbstractCollectorStubGetNext1<AbstractCollectorTestBean> col = new AbstractCollectorStubGetNext1<AbstractCollectorTestBean>();
        col.exceptionHandler = new CollectorExceptionHandlerStub1();

        AbstractCollectorTestBean result = null;

        try {
            result = col.getNext();
            fail();
        } catch (Throwable throwable) {
            // NOP
        } finally {
            col.close();
        }

        assertNull(result);
    }

    // @Test
    // public void testGetPrevious() {
    // fail("まだ実装されていません");
    // }
    //
    // @Test
    // public void testGetCurrent() {
    // fail("まだ実装されていません");
    // }
    //
    // @Test
    // public void testClose() {
    // fail("まだ実装されていません");
    // }
    //
    // @Test
    // public void testRemove() {
    // fail("まだ実装されていません");
    // }
    //
    // @Test
    // public void testIterator() {
    // fail("まだ実装されていません");
    // }
    //
    // @Test
    // public void testSetQueueSize() {
    // // fail("まだ実装されていません");
    // }
    //
    // @Test
    // public void testGetSleepWait() {
    // // fail("まだ実装されていません");
    // }
    //
    // @Test
    // public void testSetSleepWait() {
    // // fail("まだ実装されていません");
    // }
    //
    // @Test
    // public void testCloseQuietly() {
    // // fail("まだ実装されていません");
    // }

    
    /**
     * hasNext()のテスト
     * スキップ処理において、ValidateErrorStatusがSKIPの場合の動作確認
     */
    @Test
    public void testHasNext005() throws InterruptedException{
        AbstractCollectorStubHasNext5<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext5<AbstractCollectorTestBean>(2);
        DataValueObject vo1 = new DataValueObject("hoge");
        DataValueObject skip = new DataValueObject(ValidateErrorStatus.SKIP); 
        
        col.addQueue(skip);
        col.addQueue(vo1);
        
        // テスト
        boolean result = col.hasNext();

        col.close();
        assertTrue(result);
    }    


    /**
     * hasNext()のテスト
     * スキップ処理において、ValidateErrorStatusがENDの場合の動作確認
     */
    @Test
    public void testHasNext006() throws InterruptedException{
        AbstractCollectorStubHasNext5<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext5<AbstractCollectorTestBean>(2);
        DataValueObject vo1 = new DataValueObject(new Object());
        DataValueObject end = new DataValueObject(ValidateErrorStatus.END); 
        
        col.addQueue(end);
        col.addQueue(vo1);
        
        // テスト
        boolean result = col.hasNext();

        col.close();
        assertFalse(result);
    }

    /**
     * hasNext()のテスト
     * スキップ処理において、例外が発生し、例外ハンドラの処理結果がSKIPとなった場合の動作確認
     */
    @Test
    public void testHasNext007() throws Exception {
		AbstractCollectorStubHasNext5<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext5<AbstractCollectorTestBean>(
				2, CollectorExceptionHandlerStatus.SKIP);
        DataValueObject vo1 = new DataValueObject(new Object());
        DataValueObject skip = new DataValueObject(new Exception("hasNext()テスト：例外ハンドラの処理結果SKIP")); 

        col.addQueue(skip);
        col.addQueue(vo1);

        // テスト
        boolean result = col.hasNext();

        col.close();
        assertTrue(result);
    }
    
    /**
     * hasNext()のテスト
     * スキップ処理において、例外が発生し、例外ハンドラの処理結果がENDとなった場合の動作確認
     */
    @Test
    public void testHasNext008() throws Exception {
		AbstractCollectorStubHasNext5<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext5<AbstractCollectorTestBean>(
				2, CollectorExceptionHandlerStatus.END);
        DataValueObject vo1 = new DataValueObject(new Object());
        DataValueObject end = new DataValueObject(new Exception("hasNext()テスト：例外ハンドラの処理結果END")); 

        col.addQueue(end);
        col.addQueue(vo1);
        
        // テスト
        boolean result = col.hasNext();

        col.close();
        assertFalse(result);
    }
    
    /**
     * handleException()のテスト
     * 判定済みでないDataValueObjectが渡された場合、exceptionHandler#handleExceptionの結果を返すことを確認
     */
    @Test
    public void testHandleException001() throws Exception {
		AbstractCollectorStubHasNext5<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext5<AbstractCollectorTestBean>(
				2, CollectorExceptionHandlerStatus.END);
        DataValueObject end = new DataValueObject(new Exception("hasNext()テスト：例外ハンドラの処理結果END"));
    	
        // テスト
        assertEquals(CollectorExceptionHandlerStatus.END, col.handleException(end));
        assertEquals(CollectorExceptionHandlerStatus.END, end.getExceptionHandlerStatus());

        col.close();
    }
    
    /**
     * handleException()のテスト
     * exceptionHandlerStatus に判定済みの結果が格納されている場合は、改めて判定は行わず判定済みの結果を返すことを確認。
     * 本テストコードでは、handleExeption の結果が CollectorExceptionHandlerStatus.END となっていても
     * 判定済みの結果 CollectorExceptionHandlerStatus.SKIP が返却されることを確認している。
     */
    @Test
    public void testHandleException002() throws Exception {
		AbstractCollectorStubHasNext5<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext5<AbstractCollectorTestBean>(
				2, CollectorExceptionHandlerStatus.END);
        DataValueObject skip = new DataValueObject(new Exception("hasNext()テスト：例外ハンドラの処理結果SKIP"));
        skip.setExceptionHandlerStatus(CollectorExceptionHandlerStatus.SKIP);
    	
        // テスト
        assertEquals(skip.getExceptionHandlerStatus(), col.handleException(skip));

        col.close();
    }
    
    /**
     * handleException()のテスト
     * exceptionHandler == nullのCollectorである場合、nullを返すことを確認
     */
    @Test
    public void testHandleException003() throws Exception {
		AbstractCollectorStubHasNext5<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext5<AbstractCollectorTestBean>(2);
        DataValueObject skip = new DataValueObject("hoge");
    	
        // テスト
        assertNull(col.handleException(skip));

        col.close();
    }
    
    /**
     * setFinish()のテスト
     * NotificationBlockingQueueのインスタンスである場合、
     * キューに対しキューイングの終了を通知する処理を実行することを確認する
     */
    @Test
    public void testSetFinish001() throws Exception {
    	// AbstractCollectorStubSetFinish1ではArrayBlockingQueueを使用する
    	AbstractCollectorStubSetFinish1<AbstractCollectorTestBean> col = new AbstractCollectorStubSetFinish1<AbstractCollectorTestBean>();
    	// キューを作成する。キューはexecuteメソッド内でのみcreateされる。
    	col.execute();

    	// AbstractCollectorの終了フラグfinishを確認
    	boolean finish = (Boolean) ReflectionUtils.getField(col, AbstractCollector.class, "finish");
    	assertFalse(finish);
    	
    	// テスト実施
    	col.setFinish();
    	// 終了フラグがキューに入っていることを確認
    	assertEquals(CollectorStatus.END, col.getQueue().peek().getCollectorStatus());
    	// finishQueueingFlagを確認。
    	// finishQueueingが実行されていればフラグはtrueになっているはず。
    	ArrayBlockingQueueEx<DataValueObject> arrayBlockingQueueEx = (ArrayBlockingQueueEx<DataValueObject>) col.getQueue();
    	boolean finishQueueingFlag = (Boolean) ReflectionUtils.getField(arrayBlockingQueueEx, "finishQueueingFlag");
    	assertTrue(finishQueueingFlag);
    	
    	// AbstractCollectorの終了フラグfinishを確認
    	finish = (Boolean) ReflectionUtils.getField(col, AbstractCollector.class, "finish");
    	assertTrue(finish);
    }

    /**
     * setFinish()のテスト
     * NotificationBlockingQueueのインスタンスでない場合、
     * キューに対しキューイングの終了を通知する処理を実行せず終了することを確認する
     */
    @Test
    public void testSetFinish002() throws Exception {
    	// AbstractCollectorStubSetFinish2ではArrayBlockingQueueを使用する
    	AbstractCollectorStubSetFinish2<AbstractCollectorTestBean> col = new AbstractCollectorStubSetFinish2<AbstractCollectorTestBean>();
    	// キューを作成する。キューはexecuteメソッド内でのみcreateされる。
    	col.execute();
    	
    	// AbstractCollectorの終了フラグfinishを確認
    	boolean finish = (Boolean) ReflectionUtils.getField(col, AbstractCollector.class, "finish");
    	assertFalse(finish);
    	
    	// テスト実施
    	col.setFinish();
    	// 終了フラグがキューに入っていることを確認
    	assertEquals(CollectorStatus.END, col.getQueue().peek().getCollectorStatus());
    	
    	// AbstractCollectorの終了フラグfinishを確認
    	finish = (Boolean) ReflectionUtils.getField(col, AbstractCollector.class, "finish");
    	assertTrue(finish);    	
    }
}
