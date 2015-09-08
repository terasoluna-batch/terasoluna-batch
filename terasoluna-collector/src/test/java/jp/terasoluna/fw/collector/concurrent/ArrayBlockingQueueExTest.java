package jp.terasoluna.fw.collector.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import jp.terasoluna.fw.collector.vo.DataValueObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class ArrayBlockingQueueExTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * コンストラクタの確認 ArrayBlockingQueueEx(int capacity, boolean fair) fairの指定がtrueの場合
     */
    @Test
    public void testConstructor001() {
        int capacity = 3;

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity, true);

        // capacity の確認
        int capacityResult = (Integer) ReflectionTestUtils.getField(queue,
                "capacity");
        assertEquals(capacity, capacityResult);

        // fair の確認(true に設定した場合は、FairSync, false の場合は NonFairSysc)
        // ReflectionUtil を利用し、queue の lock を取得
        // lockの公平性がFair （syncがFairSync のインスタンス）であることを確認する。
        ReentrantLock lockResult = (ReentrantLock) ReflectionTestUtils.getField(
                queue, "lock");
        assertTrue(lockResult.isFair());

    }

    /**
     * コンストラクタの確認 ArrayBlockingQueueEx(int capacity, boolean fair) fairの指定がfalseの場合
     */
    @Test
    public void testConstructor002() {
        int capacity = 3;

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity, false);

        // capacity の確認
        int capacityResult = (Integer) ReflectionTestUtils.getField(queue,
                "capacity");
        assertEquals(capacity, capacityResult);

        // fair の確認(true に設定した場合は、FairSync, false の場合は NonFairSysc)
        // ReflectionUtil を利用し、queue の lock を取得
        // lockの公平性がNonFair （syncがFairSync のインスタンスではない）であることを確認する。
        ReentrantLock lockResult = (ReentrantLock) ReflectionTestUtils.getField(
                queue, "lock");
        assertFalse(lockResult.isFair());

    }

    /**
     * コンストラクタの確認 ArrayBlockingQueueEx(int capacity)
     */
    @Test
    public void testConstructor003() {
        int capacity = 3;

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        // capacity の確認
        int capacityResult = (Integer) ReflectionTestUtils.getField(queue,
                "capacity");
        assertEquals(capacity, capacityResult);

    }

    // AbstractCollector#setFinish の試験にて実施する。
    // @Test
    // public void testFinishQueueing001() {
    // }

    /**
     * poll(long timeout, TimeUnit unit) のテスト 異常系：InterruptedExceptionの確認
     */
    @Test
    public void testPoll001() throws Exception {
        int capacity = 1;

        final long timeout = 20000;
        final TimeUnit unit = TimeUnit.MILLISECONDS;
        final CountDownLatch threadSync = new CountDownLatch(1);

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        // 別スレッドでofferを実行（キューが空なので待ちになる）
        ErrorFeedBackThread thread01 = new ErrorFeedBackThread() {
            @Override
            public void doRun() throws Exception {
                long timeStart = System.currentTimeMillis();
                threadSync.countDown();
                try {
                    // 待ち状態をつくる
                    queue.poll(timeout, unit);
                    fail();
                } catch (InterruptedException e) {
                    // 期待通り
                }
                long timeEnd = System.currentTimeMillis();
                long timeDiff = timeEnd - timeStart;
                // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
                if (timeDiff < 950) {
                    fail();
                }
            }
        };

        thread01.start();

        threadSync.await();
        Thread.sleep(1000);

        // 割り込み
        thread01.interrupt();

        thread01.throwErrorOrExceptionIfThrown();
    }

    /**
     * poll(long timeout, TimeUnit unit) のテスト 正常系：タイムアウト後にキューが空の場合にnullを返す確認
     */
    @Test
    public void testPoll002() throws Exception {
        int capacity = 1;

        long timeout = 1000;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        // キューが空であることの確認
        assertEquals(0, queue.size());

        // pollの実行と処理時間計測
        long timeStart = System.currentTimeMillis();
        DataValueObject objResult = queue.poll(timeout, unit);
        long timeEnd = System.currentTimeMillis();
        long timeDiff = timeEnd - timeStart;

        // 結果確認
        // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
        if (timeDiff < (timeout - 50)) {
            fail();
        }
        assertNull(objResult);
    }

    /**
     * poll(long timeout, TimeUnit unit) のテスト 正常系：キューイングの終了通知後にキューが空の場合にnullを返す確認
     */
    @Test
    public void testPoll003() throws Exception {
        int capacity = 1;

        long timeout = 20000;
        final long sleeptime = 1000;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        final CountDownLatch threadSync = new CountDownLatch(1);

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        Thread thread01 = new Thread() {
            @Override
            public void run() {
                try {
                    threadSync.await();
                    sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.finishQueueing();
            }
        };

        // キューが空であることの確認
        assertEquals(0, queue.size());

        thread01.start();

        // pollの実行と処理時間計測
        long timeStart = System.currentTimeMillis();
        threadSync.countDown();
        DataValueObject objResult = queue.poll(timeout, unit);
        long timeEnd = System.currentTimeMillis();
        long timeDiff = timeEnd - timeStart;

        // 結果確認
        // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
        if (timeDiff < (sleeptime - 50)) {
            fail();
        }
        assertNull(objResult);
    }

    /**
     * poll(long timeout, TimeUnit unit) のテスト 正常系：キューの先頭を取得し削除することの確認
     */
    @Test
    public void testPoll004() throws Exception {
        int capacity = 3;

        long timeout = 100;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        DataValueObject obj1 = new DataValueObject("hoge");
        DataValueObject obj2 = new DataValueObject("fuga");
        DataValueObject obj3 = new DataValueObject("piyo");

        // キューに要素を詰める（add使用）
        queue.add(obj1);
        queue.add(obj2);
        queue.add(obj3);
        int sizeBefore = queue.size();

        // pollで要素を1つ取得
        DataValueObject objPoll = queue.poll(timeout, unit);
        int sizeAfter = queue.size();

        // 取得した要素がaddで最初に詰めた要素（先頭の要素）であることを確認
        assertEquals(obj1, objPoll);

        // 要素数が1つ減っていることを確認
        assertEquals(1, sizeBefore - sizeAfter);
    }

    /**
     * poll() のテスト 正常系：poll()によりnotFullシグナルが送信されることの確認
     */
    @Test
    public void testPoll005() throws Exception {
        int capacity = 3;
        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);
        final CountDownLatch threadSync = new CountDownLatch(1);
        int count = 0;

        // 別スレッドでputを実行
        // put間隔をpoll間隔より短くすることで、キューが最大まで埋まり、put待ち状態を作る。
        ErrorFeedBackThread thread01 = new ErrorFeedBackThread() {
            @Override
            public void doRun() throws Exception {
                threadSync.countDown();
                for (int idx = 0; idx < 10; idx++) {
                    Thread.sleep(500);
                    queue.put(new DataValueObject(idx));
                }
            }
        };

        thread01.start();
        threadSync.await();

        // pollを実行
        // poll間隔をput間隔より長くすることで、キューが最大まで埋まり、put待ち状態を作る。
        for (int idx = 0; idx < 10; idx++) {
            Thread.sleep(1000);
            DataValueObject obj = queue.poll();
            if (obj != null) {
                count++;
            }
        }

        // pollで10回値が取れていることを確認
        // notFullシグナルが送信されていなければ、putがデットロックとなり値を10回取得できない。
        assertEquals(10, count);
    }

    /**
     * poll() のテスト 正常系：poll()でキューから値を順番に取得できることの確認
     */
    @Test
    public void testPoll006() throws Exception {
        int capacity = 3;
        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        DataValueObject obj1 = new DataValueObject("Value1");
        DataValueObject obj2 = new DataValueObject("Value2");
        DataValueObject obj3 = new DataValueObject("Value3");

        // キューに3件挿入
        queue.put(obj1);
        queue.put(obj2);
        queue.put(obj3);

        // poll()実施
        int queueSize0 = queue.size();
        DataValueObject pollObject1 = queue.poll();
        int queueSize1 = queue.size();
        DataValueObject pollObject2 = queue.poll();
        int queueSize2 = queue.size();
        DataValueObject pollObject3 = queue.poll();
        int queueSize3 = queue.size();
        DataValueObject pollObject4 = queue.poll();
        int queueSize4 = queue.size();

        // 結果確認
        assertEquals(3, queueSize0);
        assertEquals(2, queueSize1);
        assertEquals(1, queueSize2);
        assertEquals(0, queueSize3);
        assertEquals(0, queueSize4);
        assertEquals(obj1, pollObject1);
        assertEquals(obj2, pollObject2);
        assertEquals(obj3, pollObject3);
        assertNull(pollObject4);
    }

    /**
     * offer(E o, long timeout, TimeUnit unit) のテスト 異常系：NullPointerExceptionの確認
     */
    @Test
    public void testOffer001() {
        int capacity = 3;

        long timeout = 15;
        TimeUnit unit = TimeUnit.SECONDS;

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);
        try {
            queue.offer(null, timeout, unit);
            fail();
        } catch (NullPointerException e) {
            /* NOP */
        } catch (Exception ex) {
            fail();
        }
    }

    /**
     * offer(E o, long timeout, TimeUnit unit) のテスト 異常系：InterruptedExceptionの確認
     */
    @Test
    public void testOffer002() throws Exception {
        int capacity = 1;

        final long timeout = 20000;
        final TimeUnit unit = TimeUnit.MILLISECONDS;
        DataValueObject obj1 = new DataValueObject("hoge1");
        boolean result1 = false;
        final CountDownLatch threadSync = new CountDownLatch(1);

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        // キューをいっぱいにする
        result1 = queue.offer(obj1, timeout, unit);
        assertTrue(result1);

        // 別スレッドでofferを実行（キューがいっぱいなので待ちになる）
        ErrorFeedBackThread thread01 = new ErrorFeedBackThread() {
            @Override
            public void doRun() throws Exception {
                DataValueObject obj2 = new DataValueObject("hoge2");

                // 待ち状態をつくる
                long timeStart = System.currentTimeMillis();
                threadSync.countDown();
                try {
                    queue.offer(obj2, timeout, unit);
                    fail();
                } catch (InterruptedException e) {
                    // 期待通り
                }
                long timeEnd = System.currentTimeMillis();
                long timeDiff = timeEnd - timeStart;
                // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
                if (timeDiff < 950) {
                    fail();
                }
            }
        };

        thread01.start();

        threadSync.await();
        Thread.sleep(1000);

        // 割り込み
        thread01.interrupt();

        thread01.throwErrorOrExceptionIfThrown();
    }

    /**
     * offer(E o, long timeout, TimeUnit unit) のテスト 正常系： 指定された要素をこのキューの末尾に挿入する（順番通りになっていること） + 正常系の際の return true
     */
    @Test
    public void testOffer003() throws Exception {
        int capacity = 3;

        long timeout = 15;
        TimeUnit unit = TimeUnit.SECONDS;
        DataValueObject obj1 = new DataValueObject("hoge1");
        DataValueObject obj2 = new DataValueObject("hoge2");
        boolean result1;
        boolean result2;

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        result1 = queue.offer(obj1, timeout, unit);
        result2 = queue.offer(obj2, timeout, unit);

        assertTrue(result1);
        assertEquals(obj1.getValue(), queue.poll(timeout, unit).getValue());
        assertTrue(result2);
        assertEquals(obj2.getValue(), queue.poll(timeout, unit).getValue());
    }

    /**
     * offer(E o, long timeout, TimeUnit unit) のテスト 正常系： ロックしたままタイムアウトした場合
     */
    @Test
    public void testOffer004() throws Exception {
        int capacity = 1;

        long timeout = 1000;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        DataValueObject obj1 = new DataValueObject("hoge1");
        DataValueObject obj2 = new DataValueObject("hoge2");
        boolean result2;

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        // キューをいっぱいにする
        queue.offer(obj1, timeout, unit);

        // キューに入れようとする（タイムアウトになるまで待ち、falseを返す）
        long timeStart = System.currentTimeMillis();
        result2 = queue.offer(obj2, timeout, unit);
        long timeEnd = System.currentTimeMillis();
        long timeDiff = timeEnd - timeStart;

        // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
        if (timeDiff < (timeout - 50)) {
            fail();
        }
        assertFalse(result2);
        assertEquals(obj1.getValue(), queue.poll(timeout, unit).getValue());
        assertNull(queue.poll(timeout, unit));
    }

    /**
     * offer(E o, long timeout, TimeUnit unit) のテスト 正常系： signal 通知の確認
     */
    @Test
    public void testOffer005() throws Exception {
        int capacity = 1;

        long timeout = 50;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        DataValueObject obj1 = new DataValueObject("hoge");

        final AtomicBoolean checkflg = new AtomicBoolean();

        final CountDownLatch threadSync = new CountDownLatch(1);

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        // スレッドでpeekを実行させる（最初はキューが空なので待つ）
        Thread thread01 = new Thread() {
            public void run() {
                long timeStart = System.currentTimeMillis();
                threadSync.countDown();
                queue.peek();
                long timeEnd = System.currentTimeMillis();
                long timeDiff = timeEnd - timeStart;

                // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
                if (timeDiff < 950) {
                    fail();
                }
                checkflg.set(true);
            }
        };
        thread01.start();

        threadSync.await();
        Thread.sleep(1000);
        // フラグを確認する（offerより前にpeekが動いていないこと）
        assertFalse(checkflg.get());
        // キューに要素を詰める（→これによりシグナルが出ればpeekが動くはず）
        queue.offer(obj1, timeout, unit);

        // スレッドが動く時間分だけ待つ
        thread01.join();

        // フラグを確認する
        assertTrue(checkflg.get());
    }

    /**
     * offer(E o) のテスト 異常系： NullPointerException の確認
     */
    @Test
    public void testOffer006() throws Exception {
        int capacity = 1;

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        try {
            queue.offer(null);
            fail();
        } catch (NullPointerException e) {
            /* NOP */
        } catch (Exception e) {
            fail();
        }

    }

    /**
     * offer(E o) のテスト 正常系：キューの末尾に要素を挿入することの確認
     */
    @Test
    public void testOffer007() throws Exception {
        int capacity = 3;
        long timeout = 50;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        DataValueObject obj1 = new DataValueObject("hoge");
        DataValueObject obj2 = new DataValueObject("fuga");
        DataValueObject obj3 = new DataValueObject("piyo");
        boolean result1 = false;
        boolean result2 = false;
        boolean result3 = false;

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        result1 = queue.offer(obj1);
        result2 = queue.offer(obj2);
        result3 = queue.offer(obj3);

        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertEquals(obj1.getValue(), queue.poll(timeout, unit).getValue());
        assertEquals(obj2.getValue(), queue.poll(timeout, unit).getValue());
        assertEquals(obj3.getValue(), queue.poll(timeout, unit).getValue());
    }

    /**
     * offer(E o) のテスト 正常系： 要素が追加不可の場合にfalseを返す確認
     */
    @Test
    public void testOffer008() throws Exception {
        int capacity = 1;
        DataValueObject obj1 = new DataValueObject("hoge");
        DataValueObject obj2 = new DataValueObject("fuga");
        boolean result2 = false;

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        queue.offer(obj1);
        result2 = queue.offer(obj2);

        assertFalse(result2);
    }

    /**
     * offer(E o) のテスト 正常系： signal の確認
     */
    @Test
    public void testOffer009() throws Exception {
        int capacity = 1;

        DataValueObject obj1 = new DataValueObject("hoge");

        final AtomicBoolean checkflg = new AtomicBoolean();

        final CountDownLatch threadSync = new CountDownLatch(1);

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        // スレッドでpeekを実行させる（最初はキューが空なので待つ）
        Thread thread01 = new Thread() {
            public void run() {
                long timeStart = System.currentTimeMillis();
                threadSync.countDown();
                queue.peek();
                long timeEnd = System.currentTimeMillis();
                long timeDiff = timeEnd - timeStart;

                // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
                if (timeDiff < 950) {
                    fail();
                }
                checkflg.set(true);
            }
        };
        thread01.start();

        threadSync.await();
        Thread.sleep(1000);
        // フラグを確認する（offerより前にpeekが動いていないこと）
        assertFalse(checkflg.get());
        // キューに要素を詰める（→これによりシグナルが出ればpeekが動くはず）
        queue.offer(obj1);

        // スレッドが動く時間分だけ待つ
        thread01.join();

        // フラグを確認する
        assertTrue(checkflg.get());
    }

    /**
     * put(E o) のテスト 異常系： NullPointerException の確認
     */
    @Test
    public void testPut001() throws Exception {
        int capacity = 1;

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        try {
            queue.put(null);
            fail();
        } catch (NullPointerException e) {
            /* NOP */
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * put(E o) のテスト 異常系： InterruptedException の確認
     */
    @Test
    public void testPut002() throws Exception {
        int capacity = 1;

        DataValueObject obj1 = new DataValueObject("hoge1");

        final CountDownLatch threadSync = new CountDownLatch(1);

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        // キューをいっぱいにする
        queue.put(obj1);

        // 別スレッドでputを実行（キューがいっぱいなので待ちになる）
        ErrorFeedBackThread thread01 = new ErrorFeedBackThread() {
            @Override
            public void doRun() throws Exception {
                DataValueObject obj2 = new DataValueObject("hoge2");

                // 待ち状態をつくる
                long timeStart = System.currentTimeMillis();
                threadSync.countDown();
                try {
                    queue.put(obj2);
                    fail();
                } catch (InterruptedException e) {
                    // 期待通り
                }
                long timeEnd = System.currentTimeMillis();
                long timeDiff = timeEnd - timeStart;
                // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
                if (timeDiff < 950) {
                    fail();
                }
            }
        };

        thread01.start();

        threadSync.await();
        Thread.sleep(1000);

        // 割り込み
        thread01.interrupt();

        thread01.throwErrorOrExceptionIfThrown();
    }

    /**
     * put(E o) のテスト 正常系： 指定された要素をこのキューの末尾に追加することの確認
     */
    @Test
    public void testPut003() throws Exception {
        int capacity = 3;
        long timeout = 50;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        DataValueObject obj1 = new DataValueObject("hoge");
        DataValueObject obj2 = new DataValueObject("fuga");
        DataValueObject obj3 = new DataValueObject("piyo");

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        queue.put(obj1);
        queue.put(obj2);
        queue.put(obj3);

        assertEquals(obj1.getValue(), queue.poll(timeout, unit).getValue());
        assertEquals(obj2.getValue(), queue.poll(timeout, unit).getValue());
        assertEquals(obj3.getValue(), queue.poll(timeout, unit).getValue());

    }

    /**
     * put(E o)のテスト 正常系：空間が利用可能になるまで待機してから要素を追加することの確認
     */
    @Test
    public void testPut004() throws Exception {
        int capacity = 1;
        long timeout = 50;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        final AtomicBoolean checkflg = new AtomicBoolean();

        DataValueObject obj1 = new DataValueObject("hoge");
        final DataValueObject obj2 = new DataValueObject("fuga");

        final CountDownLatch threadSync = new CountDownLatch(1);

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);
        queue.put(obj1);

        Thread thread01 = new Thread() {
            public void run() {
                long timeStart = System.currentTimeMillis();
                threadSync.countDown();
                try {
                    queue.put(obj2); // キューが空くまで待ってから実行する
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long timeEnd = System.currentTimeMillis();
                long timeDiff = timeEnd - timeStart;
                // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
                if (timeDiff < 950) {
                    fail();
                }
                checkflg.set(true);
            }
        };
        thread01.start();
        threadSync.await();
        Thread.sleep(1000);
        // フラグを確認する（pollより前にputが動いていないこと）
        assertFalse(checkflg.get());
        queue.poll(timeout, unit);

        thread01.join();

        assertTrue(checkflg.get());
        assertEquals(obj2.getValue(), queue.poll(timeout, unit).getValue());

    }

    /**
     * put(E o) のテスト 正常系： signalの確認
     */
    @Test
    public void testPut005() throws Exception {
        int capacity = 1;

        DataValueObject obj1 = new DataValueObject("hoge");

        final AtomicBoolean checkflg = new AtomicBoolean();

        final CountDownLatch threadSync = new CountDownLatch(1);

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        // スレッドでpeekを実行させる（最初はキューが空なので待つ）
        Thread thread01 = new Thread() {
            public void run() {
                long timeStart = System.currentTimeMillis();
                threadSync.countDown();
                queue.peek();
                long timeEnd = System.currentTimeMillis();
                long timeDiff = timeEnd - timeStart;

                // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
                if (timeDiff < 950) {
                    fail();
                }
                checkflg.set(true);
            }
        };
        thread01.start();

        // キューに要素を詰める（→これによりシグナルが出ればpeekが動くはず）
        threadSync.await();
        Thread.sleep(1000);
        // フラグを確認する（putより前にpeekが動いていないこと）
        assertFalse(checkflg.get());
        queue.put(obj1);

        // スレッドが動く時間分だけ待つ
        thread01.join();

        // フラグを確認する
        assertTrue(checkflg.get());
    }

    /**
     * peek() のテスト 正常系：キューの先頭を取得するが削除しない確認
     */
    @Test
    public void testPeek001() throws Exception {
        int capacity = 3;
        long timeout = 50;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        DataValueObject obj1 = new DataValueObject("hoge");
        DataValueObject obj2 = new DataValueObject("fuga");
        DataValueObject obj3 = new DataValueObject("piyo");

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        queue.put(obj1);
        queue.put(obj2);
        queue.put(obj3);

        assertEquals(obj1.getValue(), queue.peek().getValue());

        assertEquals(obj1.getValue(), queue.poll(timeout, unit).getValue());
        assertEquals(obj2.getValue(), queue.poll(timeout, unit).getValue());
        assertEquals(obj3.getValue(), queue.poll(timeout, unit).getValue());
    }

    /**
     * peek() のテスト 正常系：キューが空の場合、キューに要素が入るのを待つ確認
     */
    @Test
    public void testPeek002() throws Exception {
        int capacity = 1;
        final long waittime = 1000;

        final DataValueObject obj1 = new DataValueObject("hoge");

        final CountDownLatch threadSync = new CountDownLatch(1);

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        Thread thread01 = new Thread() {
            @Override
            public void run() {
                try {
                    threadSync.await();
                    sleep(waittime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.add(obj1);
            }
        };

        // キューが空であることの確認
        assertEquals(0, queue.size());

        thread01.start();

        long timeStart = System.currentTimeMillis();
        threadSync.countDown();
        // 別スレッドでキューへ要素追加をしないとpeek()はいつまでも待ち続ける
        DataValueObject objPeek = queue.peek();
        long timeEnd = System.currentTimeMillis();
        long timeDiff = timeEnd - timeStart;

        // キューに要素が入るのを待ったことを確認
        // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
        if (timeDiff < (waittime - 50)) {
            fail();
        }
        // キューの要素が追加されていることの確認
        assertEquals(obj1.getValue(), objPeek.getValue());

    }

    /**
     * peek() のテスト 正常系：キューが空の場合、キューイングの終了通知を待つ確認
     */
    @Test
    public void testPeek003() throws Exception {
        int capacity = 1;
        final long waittime = 1000;
        final CountDownLatch threadSync = new CountDownLatch(1);

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        Thread thread01 = new Thread() {
            @Override
            public void run() {
                try {
                    threadSync.await();
                    sleep(waittime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.finishQueueing();
            }
        };

        // キューが空であることの確認
        assertEquals(0, queue.size());

        thread01.start();

        // 別スレッドでキューイング終了通知をしないとpeek()はいつまでも待ち続ける
        // キューの要素が追加されていない場合nullが返る
        long timeStart = System.currentTimeMillis();
        threadSync.countDown();
        DataValueObject objPeek = queue.peek();
        long timeEnd = System.currentTimeMillis();
        long timeDiff = timeEnd - timeStart;

        // キューイング終了まで待ったことを確認
        // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
        if (timeDiff < (waittime - 50)) {
            fail();
        }
        // Peekの結果がNULLであることを確認
        assertNull(objPeek);
    }

    /**
     * peek() のテスト 異常系：キューが空で待機している時にInterruptedExeceptionが発生した場合にnullを返す確認
     */
    @Test
    public void testPeek004() throws Exception {
        int capacity = 1;
        final long sleeptime = 1000;
        final CountDownLatch threadSync = new CountDownLatch(1);

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        // 別スレッドでpeekを実行（キューが空なので待ちになる）
        ErrorFeedBackThread thread01 = new ErrorFeedBackThread() {
            @Override
            public void doRun() throws Exception {
                // 待ち状態をつくる
                long timeStart = System.currentTimeMillis();
                threadSync.countDown();
                DataValueObject result = queue.peek();
                long timeEnd = System.currentTimeMillis();
                // resultの確認
                assertNull(result);
                // 処理時間の確認（interruptによって終了したか）
                long timeDiff = timeEnd - timeStart;
                // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
                if (timeDiff < (sleeptime - 50)) {
                    fail();
                }
            }
        };

        thread01.start();

        // 少し待つ
        threadSync.await();
        Thread.sleep(sleeptime);

        // 割り込み
        thread01.interrupt();

        thread01.throwErrorOrExceptionIfThrown();
    }

    /**
     * isEmpty() のテスト 正常系：キューの要素がない場合にtrueを返す確認（待機した結果要素がない）
     */
    @Test
    public void testIsEmpty001() throws Exception {
        int capacity = 1;

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        Thread thread01 = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.finishQueueing();
            }
        };

        thread01.start();

        // 別スレッドでキューイング終了通知を行うとtrueを返す
        assertTrue(queue.isEmpty());
    }

    /**
     * isEmpty() のテスト 正常系：キューの要素がある場合にfalseを返す確認（待機中に要素が追加される）
     */
    @Test
    public void testIsEmpty002() throws Exception {
        int capacity = 1;

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        Thread thread01 = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DataValueObject obj1 = new DataValueObject("hoge");
                queue.add(obj1);
            }
        };

        thread01.start();

        // 別スレッドでキューイング終了通知を行うとtrueを返す
        assertFalse(queue.isEmpty());

    }

    /**
     * isEmpty() のテスト 正常系：キューの要素がある場合にfalseを返す確認（最初から要素がある）
     */
    @Test
    public void testIsEmpty003() throws Exception {
        int capacity = 1;
        DataValueObject obj1 = new DataValueObject("hoge");

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        queue.add(obj1);

        assertFalse(queue.isEmpty());

    }

    /**
     * isEmpty() のテスト 正常系：キューの要素がない場合にtrueを返す確認（待機しない）
     */
    @Test
    public void testIsEmpty004() throws Exception {
        int capacity = 1;

        ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        queue.finishQueueing();

        assertTrue(queue.isEmpty());
    }

    /**
     * isEmpty() のテスト 異常系：キューが空で待機している時にInterruptedExeceptionが発生した場合にtrueを返す確認
     */
    @Test
    public void testIsEmpty005() throws Exception {
        int capacity = 1;
        final long sleeptime = 1000;
        final CountDownLatch threadSync = new CountDownLatch(1);

        final ArrayBlockingQueueEx<DataValueObject> queue = new ArrayBlockingQueueEx<DataValueObject>(capacity);

        // 別スレッドでisEmptyを実行（キューが空なので待ちになる）
        ErrorFeedBackThread thread01 = new ErrorFeedBackThread() {
            @Override
            public void doRun() throws Exception {
                // 待ち状態をつくる
                long timeStart = System.currentTimeMillis();
                threadSync.countDown();
                boolean result = queue.isEmpty();
                long timeEnd = System.currentTimeMillis();
                // resultの確認
                assertTrue(result);
                // 処理時間の確認（interruptによって終了したか）
                long timeDiff = timeEnd - timeStart;
                // 約1000ミリ秒の待ち確認(50ミリ秒の誤差を許容)
                if (timeDiff < (sleeptime - 50)) {
                    fail();
                }
            }
        };

        thread01.start();

        // 少し待つ
        threadSync.await();
        Thread.sleep(sleeptime);

        // 割り込み
        thread01.interrupt();

        thread01.throwErrorOrExceptionIfThrown();
    }

    /**
     * エラーをフィードバックできるスレッド。 別スレッドで実施したい内容を doRun() throws Exception に実装する。 試験終了時、throwErrorOrExceptionIfThrownメソッドを実行すると、
     * doRunメソッドにて想定外のエラーが発生した場合に、そのエラーがスローされる。
     */
    abstract class ErrorFeedBackThread extends Thread {
        private Exception exception;

        private Error error;

        @Override
        public void run() {
            try {
                doRun();
            } catch (Exception e) {
                exception = e;
            } catch (Error e) {
                error = e;
            }
        }

        public void throwErrorOrExceptionIfThrown() throws Exception {
            join();
            if (error != null) {
                throw error;
            } else if (exception != null) {
                throw exception;
            }
        }

        abstract void doRun() throws Exception;
    }
}
