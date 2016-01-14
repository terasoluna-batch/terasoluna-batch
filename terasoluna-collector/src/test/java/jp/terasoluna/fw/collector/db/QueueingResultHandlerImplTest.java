package jp.terasoluna.fw.collector.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class QueueingResultHandlerImplTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        QueueingResultHandlerImpl.setVerbose(true);
    }

    @After
    public void tearDown() throws Exception {
        QueueingResultHandlerImpl.setVerbose(false);
        Thread.interrupted();
    }

    /**
     * testHandleResult
     */
    @Test
    public void testHandleResult001() {
        QueueingResultHandlerImpl<HogeBean> drh = new QueueingResultHandlerImpl<>();
        DummyResultContext ctxInNull = new DummyResultContext();
        ctxInNull.setResultObject(null);
        assertNotNull(drh);
        try {
            drh.handleResult(ctxInNull);
            drh.handleResult(ctxInNull);
            drh.handleResult(ctxInNull);
            drh.handleResult(ctxInNull);
            drh.handleResult(ctxInNull);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * testHandleResult
     */
    @Test
    public void testHandleResult002() {
        QueueingResultHandlerImpl<HogeBean> drh = new QueueingResultHandlerImpl<>();

        assertNotNull(drh);
        try {
            DummyResultContext context = new DummyResultContext();
            context.setResultObject(HogeBean.buider().hoge("hoge1").build());
            drh.handleResult(context);
            context = new DummyResultContext();
            context.setResultObject(HogeBean.buider().hoge("hoge2").build());
            drh.handleResult(context);
            DummyResultContext contextInNull = new DummyResultContext();
            contextInNull.setResultObject(null);
            drh.handleResult(contextInNull);
            context = new DummyResultContext();
            context.setResultObject(HogeBean.buider().hoge("hoge3").build());
            drh.handleResult(context);
            context = new DummyResultContext();
            context.setResultObject(HogeBean.buider().hoge("hoge4").build());
            drh.handleResult(context);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * testHandleResult
     */
    @Test
    public void testHandleResult003() {
        QueueingResultHandlerImpl<HogeBean> drh = new QueueingResultHandlerImpl<>();
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

    @Test
    public void testHandleResult004() throws Exception {
        final QueueingResultHandlerImpl<HogeBean> drh = new QueueingResultHandlerImpl<>();
        DaoCollector<HogeBean> daoCollector = new DaoCollectorStub001();
        drh.setDaoCollector(daoCollector);

        ExecutorService service = Executors.newSingleThreadExecutor();
        ErrorFeedBackRunnable runnable = new ErrorFeedBackRunnable() {
            @Override
            public void doRun() {
                Thread.currentThread().interrupt();
                // 割り込み発生時はhandleResultは処理されず、スレッドが割り込み状態のままであること。
                DummyResultContext context = new DummyResultContext();
                context.setResultObject(HogeBean.buider().hoge("hoge1").build());
                drh.handleResult(context);
                assertTrue(Thread.currentThread().isInterrupted());
                assertNull(drh.prevRow);
            }
        };
        service.submit(runnable);
        runnable.throwErrorOrExceptionIfThrown();
    }

    /**
     * testHandleResult
     */
    @Test
    public void testHandleResult005() {
        QueueingResultHandlerImpl<HogeBean> drh = new QueueingResultHandlerImpl<>();
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

    @Test
    public void testDelayCollect001() throws Exception {
        final QueueingResultHandlerImpl<HogeBean> drh = new QueueingResultHandlerImpl<>();
        DaoCollectorStub004 daoCollector = new DaoCollectorStub004(1);
        drh.setDaoCollector(daoCollector);
        drh.prevRow = HogeBean.buider().hoge("rowObject").build();

        ExecutorService service = Executors.newSingleThreadExecutor();
        ErrorFeedBackRunnable runnable = new ErrorFeedBackRunnable() {
            @Override
            public void doRun() throws Exception {
                Thread.currentThread().interrupt();
                // 割り込み発生時はdelayCollectは処理されず、スレッドが割り込み状態のままであること。
                drh.delayCollect();
                assertTrue(Thread.currentThread().isInterrupted());
            }
        };
        service.submit(runnable);
        runnable.throwErrorOrExceptionIfThrown();

        // 割り込み例外によりキューイングされていないこと。
        assertEquals(0, daoCollector.getQueue().size());
    }

    @Test
    public void testDelayCollect002() throws Exception {
        final QueueingResultHandlerImpl<HogeBean> drh = new QueueingResultHandlerImpl<>();
        DaoCollectorStub003 daoCollector = new DaoCollectorStub003();
        drh.setDaoCollector(daoCollector);
        drh.prevRow = HogeBean.buider().hoge("rowObject").build();
        ExecutorService service = Executors.newSingleThreadExecutor();
        ErrorFeedBackRunnable runnable = new ErrorFeedBackRunnable() {
            @Override
            public void doRun() throws Exception {
                drh.delayCollect();
                assertTrue(Thread.currentThread().isInterrupted());
            }
        };
        Future<?> future = service.submit(runnable);
        while (true) {
            try {
                // コレクタのキュー挿入スレッドがブロックされるまで少し待つ
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
            }
            if (daoCollector.isBlocked()) {
                break;
            }
        }
        // タスクキャンセルを実行。
        future.cancel(true);
        runnable.throwErrorOrExceptionIfThrown();
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
