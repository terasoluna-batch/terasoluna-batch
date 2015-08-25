/*
 * Copyright (c) 2015 NTT DATA Corporation
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

import jp.terasoluna.fw.batch.exception.handler.ExceptionStatusHandler;
import jp.terasoluna.fw.batch.executor.repository.BatchJobDataRepository;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * {@code AsyncJobOperatorImpl}のテストケース。<br>
 *
 * @since 3.6
 */
public class AsyncJobOperatorImplTest {

    protected BatchJobDataRepository batchJobDataRepository;

    protected AsyncJobLauncher asyncJobLauncher;

    protected AsyncBatchStopper asyncBatchStopper;

    protected ExceptionStatusHandler exceptionStatusHandler;

    /**
     * テスト前処理。<br>
     */
    @Before
    public void setUp() {
        this.batchJobDataRepository = mock(BatchJobDataRepository.class);
        this.asyncJobLauncher = mock(AsyncJobLauncher.class);
        this.asyncBatchStopper = mock(AsyncBatchStopper.class);
        this.exceptionStatusHandler = mock(ExceptionStatusHandler.class);
    }

    /**
     * コンストラクタのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・{@code BatchJobDataRepository}がnullであるとき、アサーションエラーとして
     * 　{@code IllegalArgumentException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobOperatorImpl01() throws Exception {
        try {
            // テスト実行
            new AsyncJobOperatorImpl(null, asyncJobLauncher, asyncBatchStopper,
                    exceptionStatusHandler);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "[EAL025074] [Assertion failed] - AsyncJobOperatorImpl constructor needs BatchJobDataRepository",
                    e.getMessage());
        }
    }

    /**
     * コンストラクタのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・{@code AsyncJobLauncher}がnullであるとき、アサーションエラーとして
     * {@code IllegalArgumentException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobOperatorImpl02() throws Exception {
        try {
            // テスト実行
            new AsyncJobOperatorImpl(batchJobDataRepository, null,
                    asyncBatchStopper, exceptionStatusHandler);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "[EAL025075] [Assertion failed] - AsyncJobOperatorImpl constructor needs AsyncJobLauncher",
                    e.getMessage());
        }
    }

    /**
     * コンストラクタのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・{@code AsyncBatchStopper}がnullであるとき、アサーションエラーとして
     * {@code IllegalArgumentException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobOperatorImpl03() throws Exception {
        try {
            // テスト実行
            new AsyncJobOperatorImpl(batchJobDataRepository, asyncJobLauncher,
                    null, exceptionStatusHandler);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "[EAL025076] [Assertion failed] - AsyncJobOperatorImpl constructor needs AsyncBatchStopper",
                    e.getMessage());
        }
    }

    /**
     * コンストラクタのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・{@code ExceptionStatusHandler}がnullであるとき、アサーションエラーとして
     * {@code IllegalArgumentException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobOperatorImpl04() throws Exception {
        try {
            // テスト実行
            new AsyncJobOperatorImpl(batchJobDataRepository, asyncJobLauncher,
                    asyncBatchStopper, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "[EAL025077] [Assertion failed] - AsyncJobOperatorImpl constructor needs ExceptionStatusHandler",
                    e.getMessage());
        }
    }

    /**
     * コンストラクタのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・コンストラクタ引数で渡された引数がフィールドに退避されていること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobOperatorImpl05() throws Exception {
        // テスト実行
        AsyncJobOperatorImpl asyncJobOperator = new AsyncJobOperatorImpl(
                batchJobDataRepository, asyncJobLauncher, asyncBatchStopper,
                exceptionStatusHandler);

        assertSame(batchJobDataRepository,
                asyncJobOperator.batchJobDataRepository);
        assertSame(asyncJobLauncher, asyncJobOperator.asyncJobLauncher);
        assertSame(asyncBatchStopper, asyncJobOperator.asyncBatchStopper);
        assertSame(exceptionStatusHandler,
                asyncJobOperator.exceptionStatusHandler);
    }

    /**
     * {@code start}のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・コンストラクタのアサーションを全て成功させていること。
     * 確認項目
     * ・{@code AsyncBatchStopper#canStop()}が{@code false}を返す時、ループ内の処理が
     * 行われず終了すること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testStart01() throws Exception {
        doReturn(true).when(asyncBatchStopper).canStop();
        String[] args = new String[] {};
        doReturn(new BatchJobListResult()).when(batchJobDataRepository)
                .resolveBatchJobResult(args);
        AsyncJobOperatorImpl asyncJobOperator = new AsyncJobOperatorImpl(
                batchJobDataRepository, asyncJobLauncher, asyncBatchStopper,
                exceptionStatusHandler);

        // テスト実行
        assertEquals(0, asyncJobOperator.start(new String[] {}));

        // BatchJobDataRepository#resolveBatchJobResult()が１度も呼ばれていないこと。
        verify(batchJobDataRepository, never()).resolveBatchJobResult(args);

        // シャットダウン処理が呼び出されていること。
        verify(asyncJobLauncher).shutdown();
    }

    /**
     * {@code start}のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・コンストラクタのアサーションを全て成功させていること。
     * 確認項目
     * ・{@code BatchJobDataRepository#resolveBatchJobResult()}が{@code null}
     * を返す時、ポーリングスリープが行われてジョブが実行されないこと。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testStart02() throws Exception {
        doReturn(false).doReturn(true).when(asyncBatchStopper).canStop();
        String[] args = new String[] {};
        doReturn(null).when(batchJobDataRepository).resolveBatchJobResult(args);
        doNothing().when(asyncJobLauncher).executeJob(anyString());
        AsyncJobOperatorImpl asyncJobOperator = new AsyncJobOperatorImpl(
                batchJobDataRepository, asyncJobLauncher, asyncBatchStopper,
                exceptionStatusHandler);

        // テスト実行
        assertEquals(0, asyncJobOperator.start(new String[] {}));

        // AsyncJobLauncherが一度も実行されないこと。
        verify(asyncJobLauncher, never()).executeJob(anyString());

        // シャットダウン処理が呼び出されていること。
        verify(asyncJobLauncher).shutdown();
    }

    /**
     * {@code start}のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・コンストラクタのアサーションを全て成功させていること。
     * 確認項目
     * ・実行対象のジョブが1回取得できた場合、1度だけジョブが実行されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testStart03() throws Exception {
        doReturn(false).doReturn(true).when(asyncBatchStopper).canStop();
        String[] args = new String[] {};
        BatchJobListResult result = new BatchJobListResult() {{
            setJobSequenceId("jobSequenceId");
        }};
        doReturn(result).when(batchJobDataRepository)
                .resolveBatchJobResult(args);
        doNothing().when(asyncJobLauncher).executeJob(anyString());
        AsyncJobOperatorImpl asyncJobOperator = new AsyncJobOperatorImpl(
                batchJobDataRepository, asyncJobLauncher, asyncBatchStopper,
                exceptionStatusHandler);

        // テスト実行
        assertEquals(0, asyncJobOperator.start(new String[] {}));

        // AsyncJobLauncher#executeJob()が1回だけ呼び出されていること
        verify(asyncJobLauncher, times(1)).executeJob("jobSequenceId");

        // シャットダウン処理が呼び出されていること。
        verify(asyncJobLauncher).shutdown();
    }

    /**
     * {@code start}のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・コンストラクタのアサーションを全て成功させていること。
     * 確認項目
     * ・実行対象のジョブが3回取得できた場合、3回ジョブが実行されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testStart04() throws Exception {
        doReturn(false).doReturn(false).doReturn(false).doReturn(true)
                .when(asyncBatchStopper).canStop();
        String[] args = new String[] {};
        BatchJobListResult result = new BatchJobListResult() {{
            setJobSequenceId("jobSequenceId");
        }};
        doReturn(result).when(batchJobDataRepository)
                .resolveBatchJobResult(args);
        doNothing().when(asyncJobLauncher).executeJob(anyString());
        AsyncJobOperatorImpl asyncJobOperator = new AsyncJobOperatorImpl(
                batchJobDataRepository, asyncJobLauncher, asyncBatchStopper,
                exceptionStatusHandler);

        // テスト実行
        assertEquals(0, asyncJobOperator.start(new String[] {}));

        // AsyncJobLauncher#executeJob()が3回呼び出されていること
        verify(asyncJobLauncher, times(3)).executeJob("jobSequenceId");

        // シャットダウン処理が呼び出されていること。
        verify(asyncJobLauncher).shutdown();
    }

    /**
     * {@code start}のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・コンストラクタのアサーションを全て成功させていること。
     * 確認項目
     * ・ジョブの実行中に例外が発生した場合、
     * {@code ExceptionStatusHandler#handleException()}が呼び出され、
     * この終了コードが返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testStart05() throws Exception {
        doReturn(false).when(asyncBatchStopper).canStop();
        String[] args = new String[] {};
        BatchJobListResult result = new BatchJobListResult() {{
            setJobSequenceId("jobSequenceId");
        }};
        doReturn(result).when(batchJobDataRepository)
                .resolveBatchJobResult(args);
        doNothing().when(asyncJobLauncher).executeJob(anyString());
        IllegalStateException e = new IllegalStateException(
                "job execution failed.");
        doThrow(e).when(asyncJobLauncher).executeJob("jobSequenceId");
        doReturn(100).when(exceptionStatusHandler).handleException(e);
        AsyncJobOperatorImpl asyncJobOperator = new AsyncJobOperatorImpl(
                batchJobDataRepository, asyncJobLauncher, asyncBatchStopper,
                exceptionStatusHandler);

        // テスト実行
        assertEquals(100, asyncJobOperator.start(new String[] {}));

        // ExceptionStatusHandler#handleException()が呼び出されていること。
        verify(exceptionStatusHandler).handleException(e);

        // シャットダウン処理が呼び出されていること。
        verify(asyncJobLauncher).shutdown();
    }

    /**
     * {@code testPollingSleep}のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・ポーリングループ時のスリープで例外が発生しないこと。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testPollingSleep01() throws Exception {
        AsyncJobOperatorImpl asyncJobOperator = new AsyncJobOperatorImpl(
                batchJobDataRepository, asyncJobLauncher, asyncBatchStopper,
                exceptionStatusHandler);
        asyncJobOperator.jobIntervalTime = 1L;

        // テスト実行
        asyncJobOperator.pollingSleep();
    }
    
    /**
     * {@code testPollingSleep}のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・ポーリングループ時のスリープで割り込みが発生した場合InterruptedExceptionが発生すること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testPollingSleep02() throws Exception {
        AsyncJobOperatorImpl asyncJobOperator = new AsyncJobOperatorImpl(
                batchJobDataRepository, asyncJobLauncher, asyncBatchStopper,
                exceptionStatusHandler);
        asyncJobOperator.jobIntervalTime = 1L;

        // スリープの呼び出し前から割り込み状態にする。
        Thread.currentThread().interrupt();

        try {
            // テスト実行
            asyncJobOperator.pollingSleep();
            fail();
        } catch (InterruptedException e) {
        }
    }
    
}
