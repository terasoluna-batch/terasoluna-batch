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

public class Queueing1NRelationResultHandlerImplTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {
        Queueing1NRelationResultHandlerImpl.setVerbose(true);
    }

    @SuppressWarnings("deprecation")
    @After
    public void tearDown() throws Exception {
        Queueing1NRelationResultHandlerImpl.setVerbose(false);
        Thread.interrupted();
    }

    /**
     * testHandleResult001
     */
    @Test
    public void testHandleResult001() {
        @SuppressWarnings("deprecation")
        Queueing1NRelationResultHandlerImpl<HogeBean> drh = new Queueing1NRelationResultHandlerImpl<>();

        assertNotNull(drh);
        DummyResultContext contextInNull = new DummyResultContext();
        contextInNull.setResultObject(null);
        try {
            drh.handleResult(contextInNull);
            drh.handleResult(contextInNull);
            drh.handleResult(contextInNull);
            drh.handleResult(contextInNull);
            drh.handleResult(contextInNull);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * testHandleResult002
     */
    @Test
    public void testHandleResult002() {
        @SuppressWarnings("deprecation")
        Queueing1NRelationResultHandlerImpl<HogeBean> drh = new Queueing1NRelationResultHandlerImpl<>();

        assertNotNull(drh);

        try {
            DummyResultContext context = new DummyResultContext();
            context.setResultObject(HogeBean.buider().hoge("hoge1").build());
            drh.handleResult(context);
            context.setResultObject(HogeBean.buider().hoge("hoge2").build());
            drh.handleResult(context);
            DummyResultContext contextInNull = new DummyResultContext();
            contextInNull.setResultObject(null);
            drh.handleResult(contextInNull);
            context.setResultObject(HogeBean.buider().hoge("hoge3").build());
            drh.handleResult(context);
            context.setResultObject(HogeBean.buider().hoge("hoge4").build());
            drh.handleResult(context);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * testHandleResult003
     */
    @Test
    public void testHandleResult003() {
        @SuppressWarnings("deprecation")
        Queueing1NRelationResultHandlerImpl<HogeBean> drh = new Queueing1NRelationResultHandlerImpl<>();
        DaoCollector<HogeBean> daoCollector = new DaoCollectorStub004(5);
        drh.setDaoCollector(daoCollector);

        assertNotNull(drh);

        try {
            DummyResultContext context = new DummyResultContext();
            context.setResultObject(HogeBean.buider().hoge("hoge1").build());
            drh.handleResult(context);
            context.setResultObject(HogeBean.buider().hoge("hoge2").build());
            drh.handleResult(context);
            DummyResultContext contextInNull = new DummyResultContext();
            contextInNull.setResultObject(null);
            drh.handleResult(contextInNull);
            context.setResultObject(HogeBean.buider().hoge("hoge3").build());
            drh.handleResult(context);
            context.setResultObject(HogeBean.buider().hoge("hoge4").build());
            drh.handleResult(context);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * testHandleResult004
     */
    @Test
    public void testHandleResult004() throws Exception {
        @SuppressWarnings("deprecation")
        final Queueing1NRelationResultHandlerImpl<HogeBean> drh = new Queueing1NRelationResultHandlerImpl<>();
        DaoCollector<HogeBean> daoCollector = new DaoCollectorStub001();
        drh.setDaoCollector(daoCollector);

        ExecutorService service = Executors.newSingleThreadExecutor();
        ErrorFeedBackRunnable runnable = new ErrorFeedBackRunnable() {
            @Override
            public void doRun() throws Exception {
                Thread.currentThread().interrupt();
                    // 割り込み発生時はhandleResultは処理されず、スレッドが割り込み状態となっていること。
                    DummyResultContext context = new DummyResultContext();
                    context.setResultObject(HogeBean.buider().hoge("rowObject").build());
                    drh.handleResult(context);
                    assertTrue(Thread.currentThread().isInterrupted());
                    assertNull(drh.prevRow);
            }
        };

        service.submit(runnable);
        runnable.throwErrorOrExceptionIfThrown();
    }

    /**
     * testHandleResult005
     */
    @Test
    public void testHandleResult005() throws Exception {
        @SuppressWarnings("deprecation")
        Queueing1NRelationResultHandlerImpl<HogeBean> drh = new Queueing1NRelationResultHandlerImpl<>();
        DaoCollectorStub001 daoCollector = new DaoCollectorStub001();
        drh.setDaoCollector(daoCollector);

        assertNotNull(drh);

        DummyResultContext context = new DummyResultContext();
        context.setResultObject(HogeBean.buider().hoge("hoge1").build());
        drh.handleResult(context);
        context.setResultObject(HogeBean.buider().hoge("hoge2").build());
        drh.handleResult(context);
        daoCollector.exceptionFlag = true;
        context.setResultObject(HogeBean.buider().hoge("hoge3").build());
        drh.handleResult(context);
        assertTrue(Thread.currentThread().isInterrupted());
    }

    /**
     * testDelayCollect001
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testDelayCollect001() {
        Queueing1NRelationResultHandlerImpl<HogeBean> drh = new Queueing1NRelationResultHandlerImpl<>();
        drh.prevRow = null;
        drh.dataCount = new AtomicLong(0);
        DaoCollectorStub001 daoCollector = new DaoCollectorStub001();
        drh.setDaoCollector(daoCollector);

        // prevRowがnullの時はキュー追加が行われないこと。
        drh.delayCollect();

        assertEquals(0L, drh.dataCount.get());
    }

    /**
     * testDelayCollect002
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testDelayCollect002() {
        Queueing1NRelationResultHandlerImpl<HogeBean> drh = new Queueing1NRelationResultHandlerImpl<>();
        HogeBean bean = new HogeBean();
        bean.setHoge("hoge1");
        drh.prevRow = bean;
        drh.dataCount = new AtomicLong(0);
        DaoCollectorStub004 daoCollector = new DaoCollectorStub004(1);
        daoCollector.setFinish(false);
        drh.setDaoCollector(daoCollector);

        // 1件のキュー追加が行われること。
        drh.delayCollect();

        assertEquals(1L, drh.dataCount.get());
        DataValueObject obj1 = daoCollector.getQueue().poll();
        assertTrue(obj1.getValue() instanceof HogeBean);
        assertEquals("hoge1", ((HogeBean) obj1.getValue()).getHoge());
        assertEquals(1L, obj1.getDataCount());
    }

    /**
     * testDelayCollect003
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testDelayCollect003() {
        Queueing1NRelationResultHandlerImpl<HogeBean> drh = new Queueing1NRelationResultHandlerImpl<>();
        DaoCollectorStub004 daoCollector = new DaoCollectorStub004(5);
        drh.setDaoCollector(daoCollector);
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
        DataValueObject obj1 = daoCollector.getQueue().poll();
        assertTrue(obj1.getValue() instanceof HogeBean);
        assertEquals("hoge1", ((HogeBean) obj1.getValue()).getHoge());
        assertEquals(1L, obj1.getDataCount());

        DataValueObject obj2 = daoCollector.getQueue().poll();
        assertTrue(obj2.getValue() instanceof HogeBean);
        assertEquals("hoge2", ((HogeBean) obj2.getValue()).getHoge());
        assertEquals(2L, obj2.getDataCount());

        DataValueObject obj3 = daoCollector.getQueue().poll();
        assertTrue(obj3.getValue() instanceof HogeBean);
        // prevRowと同一のインスタンスであるhoge2のパラメータは初期化されていること。
        assertNull(((HogeBean) obj3.getValue()).getHoge());
        assertEquals(3L, obj3.getDataCount());

        DataValueObject obj4 = daoCollector.getQueue().poll();
        assertTrue(obj4.getValue() instanceof HogeBean);
        assertEquals("hoge3", ((HogeBean) obj4.getValue()).getHoge());
        assertEquals(4L, obj4.getDataCount());

        DataValueObject obj5 = daoCollector.getQueue().poll();
        assertTrue(obj5.getValue() instanceof HogeBean);
        // prevRowと同一のインスタンスであるhoge3のパラメータは初期化されていること。
        assertNull(((HogeBean) obj5.getValue()).getHoge());
        assertEquals(5L, obj5.getDataCount());

        assertEquals(0, daoCollector.getQueue().size());

        assertEquals("hoge4", ((HogeBean)drh.prevRow).getHoge());
    }

    /**
     * testDelayCollect004
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testDelayCollect004() {
        Queueing1NRelationResultHandlerImpl drh = new Queueing1NRelationResultHandlerImpl();
        drh.prevRow = new TestBean001();
        DaoCollector<HogeBean> daoCollector = new DaoCollectorStub002();
        ((DaoCollectorStub002) daoCollector).exceptionFlag = false;

        drh.setDaoCollector(daoCollector);

        assertNotNull(drh);

        try {
            DummyResultContext context = new DummyResultContext();
            context.setResultObject(HogeBean.buider().hoge("hoge1").build());
            drh.handleResult(context);
            context.setResultObject(HogeBean.buider().hoge("hoge2").build());
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
        @SuppressWarnings("deprecation")
        final Queueing1NRelationResultHandlerImpl drh = new Queueing1NRelationResultHandlerImpl();
        final DaoCollectorStub004 daoCollector = new DaoCollectorStub004(2);
        drh.setDaoCollector(daoCollector);

        ExecutorService service = Executors.newSingleThreadExecutor();
        ErrorFeedBackRunnable runnable = new ErrorFeedBackRunnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void doRun() throws Exception {
                drh.prevRow = "hoge1";
                drh.delayCollect();
                drh.prevRow = "hoge2";
                Thread.currentThread().interrupt();
                // 割り込み発生時はdelayCollectは処理されず、スレッドが割り込み状態のままであること。
                drh.delayCollect();
                assertTrue(Thread.currentThread().isInterrupted());
            }
        };
        service.submit(runnable);
        runnable.throwErrorOrExceptionIfThrown();

        // "hoge1"のみキューイングされていること。
        assertEquals(1, daoCollector.getQueue().size());
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
