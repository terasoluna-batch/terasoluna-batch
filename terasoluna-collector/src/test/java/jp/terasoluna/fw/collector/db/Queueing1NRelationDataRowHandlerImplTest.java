package jp.terasoluna.fw.collector.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.atomic.AtomicLong;

import jp.terasoluna.fw.collector.vo.DataValueObject;
import jp.terasoluna.fw.exception.SystemException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Queueing1NRelationDataRowHandlerImplTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        Queueing1NRelationDataRowHandlerImpl.setVerbose(true);
    }

    @After
    public void tearDown() throws Exception {
        Queueing1NRelationDataRowHandlerImpl.setVerbose(false);
        Thread.interrupted();
    }

    /**
     * testHandleRow001
     */
    @Test
    public void testHandleRow001() {
        Queueing1NRelationDataRowHandlerImpl drh = new Queueing1NRelationDataRowHandlerImpl();

        assertNotNull(drh);

        try {
            drh.handleResult(null);
            drh.handleResult(null);
            drh.handleResult(null);
            drh.handleResult(null);
            drh.handleResult(null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * testHandleRow002
     */
    @Test
    public void testHandleRow002() {
        Queueing1NRelationDataRowHandlerImpl drh = new Queueing1NRelationDataRowHandlerImpl();

        assertNotNull(drh);

        try {
            DummyResultContext context = new DummyResultContext();
            context.setResultObject("hoge1");
            drh.handleResult(context);
            context.setResultObject("hoge2");
            drh.handleResult(context);
            drh.handleResult(null);
            context.setResultObject("hoge3");
            drh.handleResult(context);
            context.setResultObject("hoge4");
            drh.handleResult(context);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * testHandleRow003
     */
    @Test
    public void testHandleRow003() {
        Queueing1NRelationDataRowHandlerImpl drh = new Queueing1NRelationDataRowHandlerImpl();
        DBCollector<HogeBean> dbCollector = new DBCollectorStub004(5);
        drh.setDbCollector(dbCollector);

        assertNotNull(drh);

        try {
            DummyResultContext context = new DummyResultContext();
            context.setResultObject("hoge1");
            drh.handleResult(context);
            context.setResultObject("hoge2");
            drh.handleResult(context);
            drh.handleResult(null);
            context.setResultObject("hoge3");
            drh.handleResult(context);
            context.setResultObject("hoge4");
            drh.handleResult(context);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * testHandleRow004
     */
    @Test
    public void testHandleRow004() throws Exception {
        final Queueing1NRelationDataRowHandlerImpl drh = new Queueing1NRelationDataRowHandlerImpl();
        DBCollector<HogeBean> dbCollector = new DBCollectorStub001();
        drh.setDbCollector(dbCollector);

        ExecutorService service = Executors.newSingleThreadExecutor();
        ErrorFeedBackRunnable runnable = new ErrorFeedBackRunnable() {
            @Override
            public void doRun() throws Exception {
                Thread.currentThread().interrupt();
                try {
                    // 割り込み発生時はhandleRowは処理されず、InterruptedRuntimeExceptionが発生すること。
                    DummyResultContext context = new DummyResultContext();
                    context.setResultObject("rowObject");
                    drh.handleResult(context);
                    fail();
                } catch (InterruptedRuntimeException e) {
                    assertNull(e.getCause());
                    assertNull(drh.prevRow);
                }
            }
        };

        service.submit(runnable);
        runnable.throwErrorOrExceptionIfThrown();
    }

    /**
     * testHandleRow005
     */
    @Test
    public void testHandleRow005() throws Exception {
        Queueing1NRelationDataRowHandlerImpl drh = new Queueing1NRelationDataRowHandlerImpl();
        DBCollectorStub001 dbCollector = new DBCollectorStub001();
        drh.setDbCollector(dbCollector);

        assertNotNull(drh);

        try {
            DummyResultContext context = new DummyResultContext();
            context.setResultObject("hoge1");
            drh.handleResult(context);
            context.setResultObject("hoge2");
            drh.handleResult(context);
            dbCollector.exceptionFlag = true;
            context.setResultObject("hoge3");
            drh.handleResult(context);
            fail("失敗");
        } catch (InterruptedRuntimeException e) {
            assertNotNull(e.getCause());
            assertTrue(e.getCause() instanceof InterruptedException);
        }
    }

    /**
     * testDelayCollect001
     */
    @Test
    public void testDelayCollect001() {
        Queueing1NRelationDataRowHandlerImpl drh = new Queueing1NRelationDataRowHandlerImpl();
        drh.prevRow = null;
        drh.dataCount = new AtomicLong(0);
        DBCollectorStub001 dbCollector = new DBCollectorStub001();
        drh.setDbCollector(dbCollector);

        // prevRowがnullの時はキュー追加が行われないこと。
        drh.delayCollect();

        assertEquals(0L, drh.dataCount.get());
    }

    /**
     * testDelayCollect002
     */
    @Test
    public void testDelayCollect002() {
        Queueing1NRelationDataRowHandlerImpl drh = new Queueing1NRelationDataRowHandlerImpl();
        HogeBean bean = new HogeBean();
        bean.setHoge("hoge1");
        drh.prevRow = bean;
        drh.dataCount = new AtomicLong(0);
        DBCollectorStub004 dbCollector = new DBCollectorStub004(1);
        dbCollector.setFinish(false);
        drh.setDbCollector(dbCollector);

        // 1件のキュー追加が行われること。
        drh.delayCollect();

        assertEquals(1L, drh.dataCount.get());
        DataValueObject obj1 = dbCollector.getQueue().poll();
        assertTrue(obj1.getValue() instanceof HogeBean);
        assertEquals("hoge1", ((HogeBean) obj1.getValue()).getHoge());
        assertEquals(1L, obj1.getDataCount());
    }

    /**
     * testDelayCollect003
     */
    @Test
    public void testDelayCollect003() {
        Queueing1NRelationDataRowHandlerImpl drh = new Queueing1NRelationDataRowHandlerImpl();
        DBCollectorStub004 dbCollector = new DBCollectorStub004(5);
        drh.setDbCollector(dbCollector);
        HogeBean hoge1 = new HogeBean();
        hoge1.setHoge("hoge1");
        HogeBean hoge2 = new HogeBean();
        hoge2.setHoge("hoge2");
        HogeBean hoge3 = new HogeBean();
        hoge3.setHoge("hoge3");
        HogeBean hoge4 = new HogeBean();
        hoge4.setHoge("hoge4");

        // 以下、スレッドに割り込みが発生しない限り継続されること。
        // queueは空(prevRow=hoge1)
        DummyResultContext context = new DummyResultContext();
        context.setResultObject(hoge1);
        drh.handleResult(context);
        // queue=[hoge1](prevRow=hoge2)
        context.setResultObject(hoge2);
        drh.handleResult(context);
        // queue=[hoge1,hoge2](prevRow=hoge2)
        drh.delayCollect();
        // queue=[hoge1,hoge2,hoge2(プロパティ初期化)](prevRow=hoge3)
        context.setResultObject(hoge3);
        drh.handleResult(context);
        // queue=[hoge1,hoge2,hoge2(プロパティ初期化),hoge3](prevRow=hoge3)
        drh.delayCollect();
        // queue=[hoge1,hoge2,hoge2(プロパティ初期化),hoge3,hoge3(プロパティ初期化)](prevRow=hoge4)
        context.setResultObject(hoge4);
        drh.handleResult(context);

        assertEquals(5L, drh.dataCount.get());
        DataValueObject obj1 = dbCollector.getQueue().poll();
        assertTrue(obj1.getValue() instanceof HogeBean);
        assertEquals("hoge1", ((HogeBean) obj1.getValue()).getHoge());
        assertEquals(1L, obj1.getDataCount());

        DataValueObject obj2 = dbCollector.getQueue().poll();
        assertTrue(obj2.getValue() instanceof HogeBean);
        assertEquals("hoge2", ((HogeBean) obj2.getValue()).getHoge());
        assertEquals(2L, obj2.getDataCount());

        DataValueObject obj3 = dbCollector.getQueue().poll();
        assertTrue(obj3.getValue() instanceof HogeBean);
        // prevRowと同一のインスタンスであるhoge2のパラメータは初期化されていること。
        assertNull(((HogeBean) obj3.getValue()).getHoge());
        assertEquals(3L, obj3.getDataCount());

        DataValueObject obj4 = dbCollector.getQueue().poll();
        assertTrue(obj4.getValue() instanceof HogeBean);
        assertEquals("hoge3", ((HogeBean) obj4.getValue()).getHoge());
        assertEquals(4L, obj4.getDataCount());

        DataValueObject obj5 = dbCollector.getQueue().poll();
        assertTrue(obj5.getValue() instanceof HogeBean);
        // prevRowと同一のインスタンスであるhoge3のパラメータは初期化されていること。
        assertNull(((HogeBean) obj5.getValue()).getHoge());
        assertEquals(5L, obj5.getDataCount());

        assertEquals(0, dbCollector.getQueue().size());

        assertEquals("hoge4", ((HogeBean)drh.prevRow).getHoge());
    }

    /**
     * testDelayCollect004
     */
    @Test
    public void testDelayCollect004() {
        Queueing1NRelationDataRowHandlerImpl drh = new Queueing1NRelationDataRowHandlerImpl();
        drh.prevRow = new TestBean001();
        DBCollector<HogeBean> dbCollector = new DBCollectorStub002();
        ((DBCollectorStub002) dbCollector).exceptionFlag = false;

        drh.setDbCollector(dbCollector);

        assertNotNull(drh);

        try {
            DummyResultContext context = new DummyResultContext();
            context.setResultObject("hoge1");
            drh.handleResult(context);
            context.setResultObject("hoge2");
            drh.handleResult(context);
            drh.delayCollect();
            fail("失敗");
        } catch (SystemException e) {
            assertNotNull(e);
            assertEquals(SystemException.class, e.getClass());
            assertNotNull(e.getCause());
            assertEquals(InvocationTargetException.class, e.getCause()
                    .getClass());
        }
    }

    /**
     * testDelayCollect005
     */
    @Test
    public void testDelayCollect005() throws Exception {
        final Queueing1NRelationDataRowHandlerImpl drh = new Queueing1NRelationDataRowHandlerImpl();
        final DBCollectorStub004 dbCollector = new DBCollectorStub004(2);
        drh.setDbCollector(dbCollector);

        ExecutorService service = Executors.newSingleThreadExecutor();
        ErrorFeedBackRunnable runnable = new ErrorFeedBackRunnable() {
            @Override
            public void doRun() throws Exception {
                try {
                    drh.prevRow = "hoge1";
                    drh.delayCollect();
                    drh.prevRow = "hoge2";
                    Thread.currentThread().interrupt();
                    // 割り込み発生時はdelayCollectは処理されず、InterruptedRuntimeExceptionが発生すること。
                    drh.delayCollect();
                    fail();
                } catch (InterruptedRuntimeException e) {
                    assertNull(e.getCause());
                }
            }
        };
        service.submit(runnable);
        runnable.throwErrorOrExceptionIfThrown();

        // "hoge1"のみキューイングされていること。
        assertEquals(1, dbCollector.getQueue().size());
    }

    /**
     * エラーをフィードバックできるRunnable実装。
     * 別スレッドで実施したい内容を doRun() throws Exception に実装する。
     * 試験終了時、throwErrorOrExceptionIfThrownメソッドを実行すると、
     * doRunメソッドにて想定外のエラーが発生した場合に、そのエラーがスローされる。
     */
    abstract class ErrorFeedBackRunnable implements Runnable {
        private Exception exception;
        private Error error;
        private CountDownLatch latch = new CountDownLatch(1);

        public void run() {
            try {
                doRun();
            } catch (Exception e) {
                exception = e;
            } catch (Error e) {
                error = e;
            } finally {
                latch.countDown();
            }
        }

        public void throwErrorOrExceptionIfThrown() throws Exception {
            latch.await();
            if (error != null) {
                throw error;
            } else if (exception != null) {
                throw exception;
            }
        }

        abstract void doRun() throws Exception;
    }
}
