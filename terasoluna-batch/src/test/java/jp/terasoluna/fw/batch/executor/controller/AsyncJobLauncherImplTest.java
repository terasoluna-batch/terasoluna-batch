/*
 * Copyright (c) 2011 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.terasoluna.fw.batch.executor.controller;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;
import static uk.org.lidalia.slf4jtest.LoggingEvent.info;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import jp.terasoluna.fw.batch.executor.AsyncJobWorker;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * {@code AsyncJobLauncherImpl}のテストケース。<br>
 *
 * @since 3.6
 */
public class AsyncJobLauncherImplTest {

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private AsyncJobWorker asyncJobWorker;

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            AsyncJobLauncherImpl.class);

    /**
     * テスト前処理：コンストラクタ引数のモック生成を行う。
     */
    @Before
    public void setUp() {
        threadPoolTaskExecutor = Mockito.mock(ThreadPoolTaskExecutor.class);
        asyncJobWorker = Mockito.mock(AsyncJobWorker.class);
        logger.clear();
    }

    /**
     * テスト後処理：コンストラクタ引数のモックの{@code null}化とロガーのクリアを行う。
     */
    @After
    public void tearDown() {
        threadPoolTaskExecutor = null;
        asyncJobWorker = null;
        logger.clear();
    }

    /**
     * コンストラクタのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code threadPoolTaskExecutor}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローすること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobLauncherImpl01() throws Exception {

        // テスト実行
        try {
            new AsyncJobLauncherImpl(null, asyncJobWorker);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "[EAL025056] [Assertion failed] - AsyncJobLauncherImpl requires to set ThreadPoolTaskExecutor. please confirm the settings.",
                    e.getMessage());
        }
    }

    /**
     * コンストラクタのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code asyncJobWorker}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローすること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobLauncherImpl02() throws Exception {

        // テスト実行
        try {
            new AsyncJobLauncherImpl(threadPoolTaskExecutor, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "[EAL025056] [Assertion failed] - AsyncJobLauncherImpl requires to set AsyncJobWorker. please confirm the settings.",
                    e.getMessage());
        }
    }

    /**
     * コンストラクタのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・引数に渡した{@code threadPoolTaskExecutor}、{@code asyncJobWorker}が、フィールドに退避されていること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobLauncherImpl03() throws Exception {

        // テスト実行
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);

        assertSame(threadPoolTaskExecutor,
                asyncJobLauncher.threadPoolTaskExecutor);
        assertSame(asyncJobWorker, asyncJobLauncher.asyncJobWorker);
    }

    /**
     * setFair()メソッドのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・引数に渡した{@code boolean}値が、フィールドに退避されていること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testSetFair() throws Exception {
        AsyncJobLauncherImpl asyncJobLauncher =
                new AsyncJobLauncherImpl(threadPoolTaskExecutor, asyncJobWorker);

        // テスト実行
        asyncJobLauncher.setFair(false);

        assertEquals(false, asyncJobLauncher.fair);
    }

    /**
     * executeJob()メソッドのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・引数が{@code null}値の場合、{@code IllegalArgumentException}をスローすること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteJob01() throws Exception {
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);

        try {
            // テスト実行
            asyncJobLauncher.executeJob(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "[Assertion failed] - this argument is required; it must not be null",
                    e.getMessage());
        }
    }

    /**
     * executeJob()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code TaskRejectedException}を捕捉した場合、エラーログが出力されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteJob03() throws Exception {
        Semaphore semaphore = new Semaphore(10);
        doThrow(TaskRejectedException.class).when(threadPoolTaskExecutor)
                .execute(any(Runnable.class));

        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        asyncJobLauncher.taskPoolLimit = semaphore;

        // テスト実行
        asyncJobLauncher.executeJob("0000000001");

        // AsyncJobWorker#executeWorker()が呼び出されていないこと。
        verify(asyncJobWorker, never()).executeWorker(anyString());

        // セマフォのサイズが元の値に戻っていること。
        assertEquals(10, semaphore.availablePermits());

        // エラーログ（EAL025054）とTaskRejectedExceptionの例外メッセージが出力されていること。
        assertTrue(logger.getLoggingEvents()
                .get(0)
                .getThrowable()
                .get() instanceof TaskRejectedException);
        assertEquals(
                "[EAL025047] This job cannot be accepted for execution. jobSequenceId:0000000001",
                logger.getLoggingEvents().get(0).getMessage());
    }

    /**
     * executeJob()メソッドのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code InterruptedException}を捕捉した場合、主処理が実行されず、エラーログが出力されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteJob04() throws Exception {
        Semaphore semaphore = spy(new Semaphore(10));
        doThrow(InterruptedException.class).when(semaphore).acquire();
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        asyncJobLauncher.taskPoolLimit = semaphore;

        // テスト実行
        asyncJobLauncher.executeJob("0000000001");

        // エラーログ（EAL025054）とTaskRejectedExceptionの例外メッセージが出力されていること。
        assertTrue(logger.getLoggingEvents()
                .get(0)
                .getThrowable()
                .get() instanceof InterruptedException);
        assertEquals(
                "[EAL025054] When the job is waiting for a available worker thread, it has been interrupted. jobSequenceId:0000000001",
                logger.getLoggingEvents().get(0).getMessage());
    }

    /**
     * executeJob()メソッドのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code ThreadPoolTaskExecutor}により、{@code AsyncJobWorker#executeWorker()}が呼び出されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteJob05() throws Exception {
        doNothing().when(asyncJobWorker).executeWorker(anyString());
        Semaphore semaphore = new Semaphore(10);
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock)
                    throws Throwable {
                Runnable runnable = invocationOnMock.getArgumentAt(0,
                        Runnable.class);
                runnable.run();
                return null;
            }
        }).when(threadPoolTaskExecutor).execute(any(Runnable.class));
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        asyncJobLauncher.taskPoolLimit = semaphore;

        // テスト実行
        asyncJobLauncher.executeJob("0000000001");

        // AsyncJobWorker#executeWorker()が呼び出されていること。
        verify(asyncJobWorker).executeWorker("0000000001");

        // セマフォのサイズが元の値に戻っていること。
        assertEquals(10, semaphore.availablePermits());
    }

    /**
     * executeJob()メソッドのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code AsyncJobWorker#executeWorker()}により、 {@code RuntimeException}がスローされた場合、セマフォ開放が行われていること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteJob07() throws Exception {
        final RuntimeException runtimeException = new RuntimeException(
                "exception in AsyncJobWorker#executeWorker()");
        doThrow(runtimeException).when(asyncJobWorker)
                .executeWorker("0000000001");
        Semaphore semaphore = new Semaphore(10);
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = invocation.getArgumentAt(0, Runnable.class);
                runnable.run();
                return null;
            }
        }).when(threadPoolTaskExecutor).execute(any(Runnable.class));
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        asyncJobLauncher.taskPoolLimit = semaphore;

        // テスト実行
        asyncJobLauncher.executeJob("0000000001");

        // セマフォのサイズが元に戻っていること
        assertEquals(10, asyncJobLauncher.taskPoolLimit.availablePermits());

        // ThreadPoolTaskExecutor#execute()が呼び出されていること。
        verify(threadPoolTaskExecutor).execute(any(Runnable.class));
    }

    /**
     * executeJob()メソッドのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・セマフォの上限3以上のジョブがサブミットされた場合、メインスレッドで
     * 　セマフォの待ち受けが発生し、実行中タスクのいずれかが終了した後に
     * 　待ち受けされたタスクが実行されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */

    @Test
    public void testExecuteJob08() throws Exception {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor() {
            private static final long serialVersionUID = 1L;

            {
                setCorePoolSize(3);
                setMaxPoolSize(3);
                initialize();
            }
        };
        final ThreadPoolTaskExecutor mockThreadPoolTaskExecutor = spy(
                threadPoolTaskExecutor);
        // ThreadPoolTaskExecutorのエミュレーション。
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                mockThreadPoolTaskExecutor.execute(
                        invocation.getArgumentAt(0, Runnable.class));
                return null;
            }
        }).when(mockThreadPoolTaskExecutor).execute(any(Runnable.class));

        Queue<String> queue = new ArrayBlockingQueue<>(10, true);
        final CountDownLatch runningLatch1 = new CountDownLatch(1);
        final AnswerWithLock answer1 = new AnswerWithLock(queue, runningLatch1);
        final CountDownLatch runningLatch2 = new CountDownLatch(1);
        final AnswerWithLock answer2 = new AnswerWithLock(queue, runningLatch2);
        final CountDownLatch runningLatch3 = new CountDownLatch(1);
        final AnswerWithLock answer3 = new AnswerWithLock(queue, runningLatch3);
        final CountDownLatch runningLatch4 = new CountDownLatch(1);
        final AnswerWithLock answer4 = new AnswerWithLock(queue, runningLatch4);

        doAnswer(answer1).doAnswer(answer2)
                .doAnswer(answer3)
                .doAnswer(answer4)
                .when(asyncJobWorker)
                .executeWorker(anyString());
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);

        asyncJobLauncher.taskPoolLimit = new Semaphore(3);

        // テスト実行
        asyncJobLauncher.executeJob("0000000001");
        // ジョブ1の開始メッセージがキューに送られるまで待ち受け
        runningLatch1.await();
        assertEquals("executing job.0000000001", queue.remove());

        asyncJobLauncher.executeJob("0000000002");
        // ジョブ2の開始メッセージがキューに送られるまで待ち受け
        runningLatch2.await();
        assertEquals("executing job.0000000002", queue.remove());

        asyncJobLauncher.executeJob("0000000003"); // ジョブ実行後、2秒でジョブ終了

        // ジョブ3の開始メッセージがキューに送られるまで待ち受け
        runningLatch3.await();
        // この時点でセマフォ上限値までジョブが実行中であること。
        assertEquals(0, asyncJobLauncher.taskPoolLimit.availablePermits());

        // セマフォ解放まで実行されないジョブ4
        asyncJobLauncher.executeJob("0000000004");

        // ### メインスレッドはここでジョブ3の終了までセマフォの開放を待つ ###

        // ジョブ4の開始メッセージがキューに送られるまで待ち受け
        runningLatch4.await();

        // 1,2,4のジョブを終了させる。
        answer1.endJob();
        answer2.endJob();
        answer4.endJob();

        while (threadPoolTaskExecutor.getActiveCount() > 0) {
            // スレッドが全て終了するまで待ち合わせる
            TimeUnit.MILLISECONDS.sleep(100L);
        }
        threadPoolTaskExecutor.shutdown();

        // ジョブ3の開始 ⇒ ジョブ3の終了（2秒） ⇒ ジョブ4の開始
        // という順番でジョブが実行されていること。
        assertEquals("executing job.0000000003", queue.remove());
        assertEquals("end job.0000000003", queue.remove()); // ジョブ4開始より先にジョブ3が終了
        assertEquals("executing job.0000000004",
                queue.remove()); // ジョブ3の終了後にジョブ4開始

        // セマフォのサイズが元に戻っていること
        assertEquals(3, asyncJobLauncher.taskPoolLimit.availablePermits());
    }

    /**
     * executeJob()メソッドのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・{@code AsyncJobWorker#executeWorker()}で例外がスローされること
     * 確認項目
     * ・{@code AsyncJobWorker#executeWorker()}により、
     * 　{@code RuntimeException}がスローされた場合、セマフォ解放が行われていること。
     * ・{@code ThreadPoolTaskExecutor}により、{@code AsyncJobWorker#executeWorker()}が呼び出されること。
     * ・ExceptionStatusHandlerImplでのEAL025053ログが出力されること。
     * </pre>
     */
    @Test
    public void testExecuteJob09() throws Exception {
        final RuntimeException runtimeException = new RuntimeException(
                "exception in AsyncJobWorker#executeWorker()");
        doThrow(runtimeException).when(asyncJobWorker)
                .executeWorker("0000000001");
        Semaphore semaphore = new Semaphore(10);
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock)
                    throws Throwable {
                Runnable runnable = invocationOnMock.getArgumentAt(0,
                        Runnable.class);
                runnable.run();
                return null;
            }
        }).when(threadPoolTaskExecutor).execute(any(Runnable.class));
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        asyncJobLauncher.taskPoolLimit = semaphore;

        // テスト実行
        asyncJobLauncher.executeJob("0000000001");

        // AsyncJobWorker#executeWorker()が呼び出されていること。
        verify(asyncJobWorker).executeWorker("0000000001");

        // セマフォのサイズが元の値に戻っていること。
        assertEquals(10, semaphore.availablePermits());

        // ExceptionStatusHandlerImplでのログが出力されていること。
        assertThat(logger.getLoggingEvents(), is(asList(error(runtimeException,
                "[EAL025053] An exception occurred. please see below the stacktrace."))));
    }

    /**
     * shutdown()メソッドのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・待ち受けタスクがない。
     * 確認項目
     * ・スレッド開放待ち受けのINFOログが出力されないこと。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testShutdown01() throws Exception {
        ThreadPoolExecutor mockThreadPoolExecutor = mock(ThreadPoolExecutor.class);
        doReturn(true).when(mockThreadPoolExecutor).awaitTermination(
                anyLong(), eq(TimeUnit.MILLISECONDS));
        doReturn(mockThreadPoolExecutor).when(threadPoolTaskExecutor)
                .getThreadPoolExecutor();
        doNothing().when(threadPoolTaskExecutor).shutdown();
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        asyncJobLauncher.executorJobTerminateWaitIntervalTime = 1L;

        // テスト実行
        asyncJobLauncher.shutdown();

        // シャットダウン後の待ち合わせが行われていない。
        // ⇒ログにINFOログが出力されていないこと。
        assertEquals(0, logger.getLoggingEvents().size());
    }

    /**
     * shutdown()メソッドのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・1ループまで待ち受けタスクが存在する。
     * 確認項目
     * ・スレッド開放待ち受けのINFOログが1度出力されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testShutdown02() throws Exception {
        ThreadPoolExecutor mockThreadPoolExecutor = mock(ThreadPoolExecutor.class);
        doReturn(false).doReturn(true).when(
                mockThreadPoolExecutor).awaitTermination(anyLong(),
                eq(TimeUnit.MILLISECONDS));
        doReturn(mockThreadPoolExecutor).when(threadPoolTaskExecutor)
                .getThreadPoolExecutor();
        doNothing().when(threadPoolTaskExecutor).shutdown();
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        asyncJobLauncher.executorJobTerminateWaitIntervalTime = 1L;

        // テスト実行
        asyncJobLauncher.shutdown();

        // シャットダウン後の待ち合わせが行われていない。
        // ⇒ログにINFOログが1度だけ出力されていること。
        assertThat(logger.getLoggingEvents().size(), is(1));
        assertThat(logger.getLoggingEvents(), is(asList(info(
                "[IAL025020] Waiting to shutdown all tasks in ThreadPoolTaskExecutor."))));
    }

    /**
     * shutdown()メソッドのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・待ち受け時にスレッド割り込み⇒次回終了する。
     * 確認項目
     * ・シャットダウン待ち合わせINFOログが1度だけ出力される。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testShutdown03() throws Exception {
        ThreadPoolExecutor mockThreadPoolExecutor = mock(ThreadPoolExecutor.class);
        doThrow(new InterruptedException()).doReturn(true).when(
                mockThreadPoolExecutor).awaitTermination(anyLong(),
                eq(TimeUnit.MILLISECONDS));
        doReturn(mockThreadPoolExecutor).when(threadPoolTaskExecutor)
                .getThreadPoolExecutor();
        doNothing().when(threadPoolTaskExecutor).shutdown();
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        asyncJobLauncher.executorJobTerminateWaitIntervalTime = 1L;

        // テスト実行
        asyncJobLauncher.shutdown();

        // 待ち受け時割り込みによるInterruptedExceptionのハンドリングにより、
        // 割り込み状態が発生していないこと
        assertFalse(Thread.currentThread().isInterrupted());

        // 割り込み発生によりシャットダウン後の待ち合わせが行われていない。
        // ⇒ログにINFOログが1度だけ出力されていること。
        assertThat(logger.getLoggingEvents().size(), is(1));
        assertThat(logger.getLoggingEvents(), is(asList(info(
                "[IAL025020] Waiting to shutdown all tasks in ThreadPoolTaskExecutor."))));
    }

    /**
     * afterPropertiesSet()メソッドのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・特になし。
     * 確認項目
     * ・{@code ThreadPoolTaskExecutor}の最大サイズが1のとき、同数のセマフォが生成されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAfterPropertiesSet01() throws Exception {
        doReturn(1).when(threadPoolTaskExecutor).getMaxPoolSize();
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        asyncJobLauncher.executorJobTerminateWaitIntervalTime = 1L;
        asyncJobLauncher.setFair(false);

        // テスト実行
        asyncJobLauncher.afterPropertiesSet();

        // セマフォの利用可能数が1であること
        assertEquals(1, asyncJobLauncher.taskPoolLimit.availablePermits());

        // セマフォの公平性がfalseであること。
        assertFalse(asyncJobLauncher.taskPoolLimit.isFair());
    }

    /**
     * afterPropertiesSet()メソッドのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・{@code ThreadPoolTaskExecutor}の最大サイズが2
     * ・{@code setFair()}が無指定
     * 確認項目
     * ・利用可能数2、公平性{@code true}のセマフォが生成されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAfterPropertiesSet02() throws Exception {
        doReturn(2).when(threadPoolTaskExecutor).getMaxPoolSize();
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        asyncJobLauncher.executorJobTerminateWaitIntervalTime = 1L;

        // テスト実行
        asyncJobLauncher.afterPropertiesSet();

        // セマフォの利用可能数が2であること
        assertEquals(2, asyncJobLauncher.taskPoolLimit.availablePermits());

        // セマフォの公平性がtrueであること。
        assertTrue(asyncJobLauncher.taskPoolLimit.isFair());
    }

    /**
     * afterPropertiesSet()メソッドのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・{@code executorJobTerminateWaitIntervalTime}が未設定
     * 確認項目
     * ・{@code IllegalStateException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAfterPropertiesSet04() throws Exception {
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        asyncJobLauncher.executorJobTerminateWaitIntervalTime = -1; // @Valueのデフォルト値

        try {
            // テスト実行
            asyncJobLauncher.afterPropertiesSet();
            fail();
        } catch (IllegalStateException e) {
            assertEquals(
                    "[EAL025056] [Assertion failed] - AsyncJobLauncherImpl requires to set executor.jobTerminateWaitInterval. please confirm the settings.",
                    e.getMessage());
        }
    }

    /**
     * terminated()メソッドのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・{@code ThreadPoolExecutor}のawaitTermination()でtrueが返却される。
     * 確認項目
     * ・trueが返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testTerminated01() throws Exception {
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        ThreadPoolExecutor mockThreadPoolExecutor = mock(ThreadPoolExecutor.class);
        doReturn(true).when(mockThreadPoolExecutor).awaitTermination(
                anyLong(), eq(TimeUnit.MILLISECONDS));

        // テスト実行
        assertThat(asyncJobLauncher.terminated(mockThreadPoolExecutor), is(true));
    }

    /**
     * terminated()メソッドのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・{@code ThreadPoolExecutor}のawaitTermination()でfalseが返却される。
     * 確認項目
     * ・falseが返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testTerminated02() throws Exception {
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        ThreadPoolExecutor mockThreadPoolExecutor = mock(ThreadPoolExecutor.class);
        doReturn(false).when(mockThreadPoolExecutor).awaitTermination(
                anyLong(), eq(TimeUnit.MILLISECONDS));

        // テスト実行
        assertThat(asyncJobLauncher.terminated(mockThreadPoolExecutor), is(false));
    }

    /**
     * terminated()メソッドのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・{@code ThreadPoolExecutor}のawaitTermination()で{@code InterruptedException}がスローされる。
     * 確認項目
     * ・falseが返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testTerminated03() throws Exception {
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                threadPoolTaskExecutor, asyncJobWorker);
        ThreadPoolExecutor mockThreadPoolExecutor = mock(ThreadPoolExecutor.class);
        doThrow(new InterruptedException()).when(mockThreadPoolExecutor).awaitTermination(
                anyLong(), eq(TimeUnit.MILLISECONDS));

        // テスト実行
        assertThat(asyncJobLauncher.terminated(mockThreadPoolExecutor), is(false));
    }
}

/**
 * テスト内部で呼び出される非同期ジョブの代理として動作するアンサークラス。<br>
 *
 * @since 3.6
 */
class AnswerWithLock implements Answer<Object> {

    private static final Logger logger = LoggerFactory.getLogger(
            AnswerWithLock.class);

    private Queue<String> queue;

    private CountDownLatch runningLatch;

    private CountDownLatch endLatch = new CountDownLatch(1);

    /**
     * コンストラクタ。<br>
     *
     * @param queue        実行メッセージの格納キュー
     * @param runningLatch 開始メッセージのインキューまで進行停止
     */
    AnswerWithLock(Queue<String> queue, CountDownLatch runningLatch) {
        this.queue = queue;
        this.runningLatch = runningLatch;
    }

    /**
     * {@code AsyncJobWorker#executeWorker()}呼び出し時のアンサー。<br>
     *
     * @param invocation 実行対象
     * @return 常にnull
     * @throws Throwable 意図しない例外
     */
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        try {
            String jobSequenceId = invocation.getArgumentAt(0, String.class);

            queue.add("executing job." + jobSequenceId);
            logger.trace("executing job.{}", jobSequenceId);
            runningLatch.countDown();
            if ("0000000003".equals(jobSequenceId)) {
                TimeUnit.MILLISECONDS.sleep(2000L);
                logger.trace("# sleep end.{}", jobSequenceId);
            } else {
                endLatch.await();
                logger.trace("# job ending.{}", jobSequenceId);
            }
            queue.add("end job." + jobSequenceId);
            logger.trace("end job.{}", jobSequenceId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    /**
     * ジョブの終了指示。<br>
     * 本メソッドが実行されるまで待ち状態となる。
     */
    public void endJob() {
        this.endLatch.countDown();
    }
}
