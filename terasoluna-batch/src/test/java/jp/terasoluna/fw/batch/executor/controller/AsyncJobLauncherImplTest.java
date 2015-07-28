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

import jp.terasoluna.fw.batch.executor.worker.JobExecutorTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import java.util.concurrent.Semaphore;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static uk.org.lidalia.slf4jtest.LoggingEvent.debug;
import static uk.org.lidalia.slf4jtest.LoggingEvent.warn;

/**
 * {@code AsyncJobLauncherImpl}のテストケース。<br>
 *
 * @since 3.6
 */
public class AsyncJobLauncherImplTest {

    private ThreadPoolTaskExecutorDelegate delegate;

    private JobExecutorTemplate jobExecutorTemplate;

    private TestLogger logger = TestLoggerFactory
            .getTestLogger(AsyncJobLauncherImpl.class);

    /**
     * テスト前処理：コンストラクタ引数のモック生成を行う。
     */
    @Before
    public void setUp() {
        delegate = Mockito.mock(ThreadPoolTaskExecutorDelegate.class);
        jobExecutorTemplate = Mockito.mock(JobExecutorTemplate.class);
    }

    /**
     * テスト後処理：コンストラクタ引数のモックの{@code null}化とロガーのクリアを行う。
     */
    @After
    public void tearDown() {
        delegate = null;
        jobExecutorTemplate = null;
        logger.clear();
    }

    /**
     * コンストラクタのテスト 【異常系】
     *
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code threadPoolTaskExecutorDelegate}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローすること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobLauncherImpl01() throws Exception {

        // テスト実行
        try {
            new AsyncJobLauncherImpl(null, jobExecutorTemplate);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "[EAL025055] [Assertion failed] - AsyncJobLauncherImpl constructor needs ThreadPoolTaskExecutorDelegate",
                    e.getMessage());
        }
    }

    /**
     * コンストラクタのテスト 【異常系】
     *
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code jobExecutorTemplate}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローすること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobLauncherImpl02() throws Exception {

        // テスト実行
        try {
            new AsyncJobLauncherImpl(delegate, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "[EAL025057] [Assertion failed] - AsyncJobLauncherImpl constructor needs JobExecutorTemplate",
                    e.getMessage());
        }
    }

    /**
     * コンストラクタのテスト 【正常系】
     *
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・引数に渡した{@code threadPoolTaskExecutorDelegate}、{@code jobExecutorTemplate}が、フィールドに退避されていること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobLauncherImpl03() throws Exception {

        // テスト実行
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                delegate, jobExecutorTemplate);

        assertSame(delegate, asyncJobLauncher.threadPoolTaskExecutorDelegate);
        assertSame(jobExecutorTemplate, asyncJobLauncher.jobExecutorTemplate);
    }

    /**
     * setFair()メソッドのテスト 【正常系】
     *
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
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                delegate, jobExecutorTemplate);

        // テスト実行
        asyncJobLauncher.setFair(false);

        assertEquals(false, asyncJobLauncher.fair);
    }

    /**
     * executeJob()メソッドのテスト 【正常系】
     *
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
                delegate, jobExecutorTemplate);

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
     * ・前処理が{@code false}を返却した場合、主処理が実行されないこと。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteJob02() throws Exception {
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        doReturn(false).when(jobExecutorTemplate)
                .beforeExecute(stringArgumentCaptor.capture());
        Semaphore semaphore = new Semaphore(10);
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                delegate, jobExecutorTemplate);
        asyncJobLauncher.taskPoolLimit = semaphore;

        // テスト実行
        asyncJobLauncher.executeJob("0000000001");

        // 前処理引数にテスト実行時の引数が渡されていること。
        assertEquals("0000000001", stringArgumentCaptor.getValue());

        // ThreadPoolTaskExecutorDelegate#execute()が呼び出されていないこと。
        verify(delegate, never()).execute(any(Runnable.class));

        // セマフォのサイズが元の値に戻っていること。
        assertEquals(10, semaphore.availablePermits());

        // ログに警告ログが出力されていること。
        assertThat(logger.getLoggingEvents(), is(asList(
                warn("[WAL025009] Async job aborted in preprocessing because updating current application status was failed. jobSequenceId:[0000000001]"))));
    }

    /**
     * executeJob()メソッドのテスト 【異常系】
     *
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code TaskRejectedException}を捕捉した場合、主処理が実行されず、エラーログが出力されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteJob03() throws Exception {
        ArgumentCaptor<String> stringArgumentCaptorMain = ArgumentCaptor
                .forClass(String.class);
        ArgumentCaptor<String> stringArgumentCaptorBefore = ArgumentCaptor
                .forClass(String.class);
        doReturn(true).when(jobExecutorTemplate).beforeExecute(
                stringArgumentCaptorBefore.capture());
        doNothing().when(jobExecutorTemplate).executeWorker(
                stringArgumentCaptorMain.capture());
        Semaphore semaphore = new Semaphore(10);
        doThrow(TaskRejectedException.class).when(delegate).execute(
                any(Runnable.class));

        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                delegate, jobExecutorTemplate);
        asyncJobLauncher.taskPoolLimit = semaphore;

        // テスト実行
        asyncJobLauncher.executeJob("0000000001");

        // 前処理引数にテスト実行時の引数が渡されていること。
        assertEquals("0000000001", stringArgumentCaptorBefore.getValue());

        // JobExecutorTemplate#executeWorker()が呼び出されていないこと。
        verify(jobExecutorTemplate, never()).executeWorker(anyString());

        // セマフォのサイズが元の値に戻っていること。
        assertEquals(10, semaphore.availablePermits());

        // エラーログ（EAL025054）とTaskRejectedExceptionの例外メッセージが出力されていること。
        assertTrue(logger.getLoggingEvents().get(0).getThrowable()
                .get() instanceof TaskRejectedException);
        assertEquals(
                "[EAL025047] Thread starting went wrong.  jobSequenceId:0000000001",
                logger.getLoggingEvents().get(0).getMessage());
    }

    /**
     * executeJob()メソッドのテスト 【異常系】
     *
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
                delegate, jobExecutorTemplate);
        asyncJobLauncher.taskPoolLimit = semaphore;

        // テスト実行
        asyncJobLauncher.executeJob("0000000001");

        // 前処理が呼び出されていないこと。
        verify(jobExecutorTemplate, never()).beforeExecute(anyString());

        // エラーログ（EAL025054）とTaskRejectedExceptionの例外メッセージが出力されていること。
        assertTrue(logger.getLoggingEvents().get(0).getThrowable()
                .get() instanceof InterruptedException);
        assertEquals(
                "[EAL025054] The asynchronous job in wait state was interrupted. jobSequenceId[0000000001]",
                logger.getLoggingEvents().get(0).getMessage());
    }

    /**
     * executeJob()メソッドのテスト 【正常系】
     *
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code ThreadPoolTaskExecutor}により、{@code JobExecutorTemplate#executeWorker()}が呼び出されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteJob05() throws Exception {
        doReturn(true).when(jobExecutorTemplate).beforeExecute(anyString());
        doNothing().when(jobExecutorTemplate).executeWorker(anyString());
        Semaphore semaphore = new Semaphore(10);
        doThrow(InterruptedException.class).when(delegate).execute(any(Runnable.class));
        doAnswer(new Answer() {
            @Override public Object answer(InvocationOnMock invocationOnMock)
                    throws Throwable {
                Runnable runnable = invocationOnMock
                        .getArgumentAt(0, Runnable.class);
                runnable.run();
                return null;
            }
        }).when(delegate).execute(any(Runnable.class));
        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                delegate, jobExecutorTemplate);
        asyncJobLauncher.taskPoolLimit = semaphore;

        // テスト実行
        asyncJobLauncher.executeJob("0000000001");

        // JobExecutorTemplate#beforeExecute()が呼び出されていること。
        verify(jobExecutorTemplate).beforeExecute("0000000001");

        // JobExecutorTemplate#executeWorker()が呼び出されていること。
        verify(jobExecutorTemplate).executeWorker("0000000001");

        // セマフォのサイズが元の値に戻っていること。
        assertEquals(10, semaphore.availablePermits());
    }

    /**
     * shutdown()メソッドのテスト 【正常系】
     *
     * <pre>
     * 事前条件
     * ・待ち受けタスクがない。
     * 確認項目
     * ・スレッド開放待ち受けのデバッグログが出力されないこと。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testShutdown01() throws Exception {
        ThreadPoolTaskExecutor taskExecutor = mock(ThreadPoolTaskExecutor.class);
        doNothing().when(taskExecutor).shutdown();
        // 待ち合わせタスクは0
        doReturn(0).when(taskExecutor).getActiveCount();
        doReturn(taskExecutor).when(delegate).getThreadPoolTaskExecutor();
        AsyncJobLauncherImpl asyncJobLauncher =
                new AsyncJobLauncherImpl(delegate, jobExecutorTemplate);
        asyncJobLauncher.executorJobTerminateWaitIntervalTime = 1L;

        // テスト実行
        asyncJobLauncher.shutdown();

        // シャットダウン後の待ち合わせが行われていない。
        // ⇒ログにデバッグログが出力されていないこと。
        assertEquals(0, logger.getLoggingEvents().size());
    }

    /**
     * shutdown()メソッドのテスト 【正常系】
     *
     * <pre>
     * 事前条件
     * ・1ループまで待ち受けタスクが存在する。
     * 確認項目
     * ・スレッド開放待ち受けのデバッグログが1度出力されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testShutdown02() throws Exception {
        ThreadPoolTaskExecutor taskExecutor = mock(ThreadPoolTaskExecutor.class);
        doNothing().when(taskExecutor).shutdown();
        // 待ち合わせタスクは1(while文とログ出力時で2回コール)⇒0
        doReturn(1).doReturn(1).doReturn(0).when(taskExecutor).getActiveCount();
        doReturn(taskExecutor).when(delegate).getThreadPoolTaskExecutor();
        AsyncJobLauncherImpl asyncJobLauncher =
                new AsyncJobLauncherImpl(delegate, jobExecutorTemplate);
        asyncJobLauncher.executorJobTerminateWaitIntervalTime = 1L;

        // テスト実行
        asyncJobLauncher.shutdown();

        // シャットダウン後の待ち合わせが行われていない。
        // ⇒ログにデバッグログが1度だけ出力されていること。
        assertThat(logger.getLoggingEvents().size(), is(1));
        assertThat(logger.getLoggingEvents(), is(asList(
                debug("[DAL025031] AsyncBatchExecutor WAIT ENDS activeCount:[1]"))));
    }

    /**
     * shutdown()メソッドのテスト 【異常系】
     *
     * <pre>
     * 事前条件
     * ・待ち受け時にスレッド割り込みが発生する。
     * 確認項目
     * ・スリープタイム指定が無効となり、ただちに待ち合わせスレッド数判定に戻ること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testShutdown03() throws Exception {
        ThreadPoolTaskExecutor taskExecutor = mock(ThreadPoolTaskExecutor.class);
        doNothing().when(taskExecutor).shutdown();
        // 待ち合わせタスクは1(while文とログ出力時で2回コール)⇒0
        doReturn(1).doReturn(1).doReturn(0).when(taskExecutor).getActiveCount();
        doReturn(taskExecutor).when(delegate).getThreadPoolTaskExecutor();
        AsyncJobLauncherImpl asyncJobLauncher =
                new AsyncJobLauncherImpl(delegate, jobExecutorTemplate);
        asyncJobLauncher.executorJobTerminateWaitIntervalTime = 1000000L;
        Thread.currentThread().interrupt(); // スレッド割り込み

        // テスト実行
        asyncJobLauncher.shutdown();

        // 割り込み状態の確認と(もし割り込み状態が残留している場合は)クリア
        assertFalse(Thread.interrupted());

        // 割り込み発生によりシャットダウン後の待ち合わせが行われていない。
        // ⇒ログにデバッグログが1度だけ出力されていること。
        assertThat(logger.getLoggingEvents().size(), is(1));
        assertThat(logger.getLoggingEvents(), is(asList(
                debug("[DAL025031] AsyncBatchExecutor WAIT ENDS activeCount:[1]"))));
    }

    /**
     * afterPropertiesSet()メソッドのテスト 【正常系】
     *
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
        ThreadPoolTaskExecutor taskExecutor = mock(ThreadPoolTaskExecutor.class);
        doReturn(1).when(taskExecutor).getMaxPoolSize();
        doReturn(taskExecutor).when(delegate).getThreadPoolTaskExecutor();

        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                delegate, jobExecutorTemplate);
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
     *
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
        ThreadPoolTaskExecutor taskExecutor = mock(ThreadPoolTaskExecutor.class);
        doReturn(2).when(taskExecutor).getMaxPoolSize();
        doReturn(taskExecutor).when(delegate).getThreadPoolTaskExecutor();

        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                delegate, jobExecutorTemplate);
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
     *
     * <pre>
     * 事前条件
     * ・{@code ThreadPoolTaskExecutor}の最大サイズが-1
     * 確認項目
     * ・{@code IllegalStateException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAfterPropertiesSet03() throws Exception {
        ThreadPoolTaskExecutor taskExecutor = mock(ThreadPoolTaskExecutor.class);
        doReturn(-1).when(taskExecutor).getMaxPoolSize();
        doReturn(taskExecutor).when(delegate).getThreadPoolTaskExecutor();

        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(
                delegate, jobExecutorTemplate);
        asyncJobLauncher.executorJobTerminateWaitIntervalTime = 1L;

        try {
            // テスト実行
            asyncJobLauncher.afterPropertiesSet();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("[EAL025087] [Assertion failed] - maxPoolSize must be positive. maxPoolSize[-1]", e.getMessage());
        }
    }

    /**
     * afterPropertiesSet()メソッドのテスト 【異常系】
     *
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
        ThreadPoolTaskExecutor taskExecutor = mock(ThreadPoolTaskExecutor.class);
        doReturn(-1).when(taskExecutor).getMaxPoolSize();
        doReturn(taskExecutor).when(delegate).getThreadPoolTaskExecutor();

        AsyncJobLauncherImpl asyncJobLauncher = new AsyncJobLauncherImpl(delegate, jobExecutorTemplate);

        try {
            // テスト実行
            asyncJobLauncher.afterPropertiesSet();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("[EAL025058] [Assertion failed] - Property of executor.jobTerminateWaitInterval must be defined.", e.getMessage());
        }
    }
}
