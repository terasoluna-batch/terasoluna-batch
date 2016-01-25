/*
 * Copyright (c) 2016 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.executor;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static uk.org.lidalia.slf4jtest.LoggingEvent.*;
import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.BLogicResolver;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParamConverter;
import jp.terasoluna.fw.batch.exception.handler.BLogicExceptionHandlerResolver;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.repository.JobControlFinder;
import jp.terasoluna.fw.batch.executor.repository.JobStatusChanger;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.springframework.dao.DataAccessException;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * AsyncJobWorkerImplのテスト
 * @since 3.6
 */
public class AsyncJobWorkerImplTest {

    private TestLogger logger = TestLoggerFactory
            .getTestLogger(AsyncJobWorkerImpl.class);

    private BLogicResolver mockBLogicResolver;

    private BLogicExceptionHandlerResolver mockBLogicExceptionHandlerResolver;

    private ApplicationContextResolver mockBLogicApplicationContextResolver;

    private JobControlFinder mockJobControlFinder;

    private BLogicParamConverter mockBLogicParamConverter;

    private BLogicExecutor mockBLogicExecutor;

    private JobStatusChanger mockJobStatusChanger;

    /**
     * AsyncJobWorkerImplの各フィールドのモックを生成する
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.mockBLogicResolver = mock(BLogicResolver.class);
        this.mockBLogicExceptionHandlerResolver = mock(BLogicExceptionHandlerResolver.class);
        this.mockBLogicApplicationContextResolver = mock(ApplicationContextResolver.class);
        this.mockJobControlFinder = mock(JobControlFinder.class);
        this.mockBLogicParamConverter = mock(BLogicParamConverter.class);
        this.mockBLogicExecutor = mock(BLogicExecutor.class);
        this.mockJobStatusChanger = mock(JobStatusChanger.class);
    }

    /**
     * AsyncJobWorkerImplの各フィールドのモックをクリアにする
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.mockBLogicResolver = null;
        this.mockBLogicExceptionHandlerResolver = null;
        this.mockBLogicApplicationContextResolver = null;
        this.mockJobControlFinder = null;
        this.mockBLogicParamConverter = null;
        this.mockBLogicExecutor = null;
        this.mockJobStatusChanger = null;
        logger.clear();
    }

    /**
     * コンストラクタのテスト01 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code AsyncJobWorkerImpl}の{@code blogicResolver}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobWorkerImpl01() throws Exception {
        try {
            // テスト実行
            new AsyncJobWorkerImpl(null, mockBLogicExceptionHandlerResolver,
                    mockBLogicApplicationContextResolver, 
                    mockJobControlFinder, 
                    mockBLogicParamConverter, 
                    mockBLogicExecutor, 
                    mockJobStatusChanger);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(
                    "[EAL025056] [Assertion failed] - AsyncJobWorkerImpl requires to set BLogicResolver. please confirm the settings."));
        }
    }

    /**
     * コンストラクタのテスト02 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code AsyncJobWorkerImpl}の{@code blogicExceptionHandlerResolver}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobWorkerImpl02() throws Exception {
        try {
            // テスト実行
            new AsyncJobWorkerImpl(mockBLogicResolver,
                    null, 
                    mockBLogicApplicationContextResolver, 
                    mockJobControlFinder, 
                    mockBLogicParamConverter, 
                    mockBLogicExecutor, 
                    mockJobStatusChanger);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(
                    "[EAL025056] [Assertion failed] - AsyncJobWorkerImpl requires to set BLogicExceptionHandlerResolver. please confirm the settings."));
        }
    }

    /**
     * コンストラクタのテスト03 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code AsyncJobWorkerImpl}の{@code blogicApplicationContextResolver}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobWorkerImpl03() throws Exception {
        try {
            // テスト実行
            new AsyncJobWorkerImpl(mockBLogicResolver,
                    mockBLogicExceptionHandlerResolver, 
                    null, 
                    mockJobControlFinder, 
                    mockBLogicParamConverter, 
                    mockBLogicExecutor, 
                    mockJobStatusChanger);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(
                    "[EAL025056] [Assertion failed] - AsyncJobWorkerImpl requires to set ApplicationContextResolver. please confirm the settings."));
        }
    }

    /**
     * コンストラクタのテスト04 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code AsyncJobWorkerImpl}の{@code jobControlFinder}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobWorkerImpl04() throws Exception {
        try {
            // テスト実行
            new AsyncJobWorkerImpl(mockBLogicResolver,
                    mockBLogicExceptionHandlerResolver, 
                    mockBLogicApplicationContextResolver, 
                    null, 
                    mockBLogicParamConverter, 
                    mockBLogicExecutor, 
                    mockJobStatusChanger);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(
                    "[EAL025056] [Assertion failed] - AsyncJobWorkerImpl requires to set JobControlFinder. please confirm the settings."));
        }
    }

    /**
     * コンストラクタのテスト05 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code AsyncJobWorkerImpl}の{@code blogicParamConverter}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobWorkerImpl05() throws Exception {
        try {
            // テスト実行
            new AsyncJobWorkerImpl(mockBLogicResolver,
                    mockBLogicExceptionHandlerResolver, 
                    mockBLogicApplicationContextResolver, 
                    mockJobControlFinder, 
                    null, 
                    mockBLogicExecutor, 
                    mockJobStatusChanger);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(
                    "[EAL025056] [Assertion failed] - AsyncJobWorkerImpl requires to set BLogicParamConverter. please confirm the settings."));
        }
    }

    /**
     * コンストラクタのテスト06 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code AsyncJobWorkerImpl}の{@code blogicExecutor}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobWorkerImpl06() throws Exception {
        try {
            // テスト実行
            new AsyncJobWorkerImpl(mockBLogicResolver,
                    mockBLogicExceptionHandlerResolver,
                    mockBLogicApplicationContextResolver, 
                    mockJobControlFinder, 
                    mockBLogicParamConverter, 
                    null, 
                    mockJobStatusChanger);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(
                    "[EAL025056] [Assertion failed] - AsyncJobWorkerImpl requires to set BLogicExecutor. please confirm the settings."));
        }
    }

    /**
     * コンストラクタのテスト07 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code AsyncJobWorkerImpl}の{@code jobStatusChanger}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobWorkerImpl07() throws Exception {
        try {
            // テスト実行
            new AsyncJobWorkerImpl(mockBLogicResolver,
                    mockBLogicExceptionHandlerResolver,
                    mockBLogicApplicationContextResolver, 
                    mockJobControlFinder, 
                    mockBLogicParamConverter, 
                    mockBLogicExecutor, 
                    null);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(
                    "[EAL025056] [Assertion failed] - AsyncJobWorkerImpl requires to set JobStatusChanger. please confirm the settings."));
        }
    }

    /**
     * コンストラクタのテスト08 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code AsyncJobWorkerImpl}の{@code blogicResolver}、{@code blogicExceptionHandlerResolver}、{@code blogicApplicationContextResolver}、{@code jobControlFinder}、{@code blogicParamConverter}、{@code blogicExecutor}、{@code jobStatusChanger}が正しく設定されていること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAsyncJobWorkerImpl08() throws Exception {
        // テスト実行
        AsyncJobWorkerImpl target = new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger);

        assertThat(target.blogicResolver, is(mockBLogicResolver));
        assertThat(target.blogicExceptionHandlerResolver,
                is(mockBLogicExceptionHandlerResolver));
        assertThat(target.blogicApplicationContextResolver,
                is(mockBLogicApplicationContextResolver));
        assertThat(target.jobControlFinder,
                is(mockJobControlFinder));
        assertThat(target.blogicParamConverter, is(mockBLogicParamConverter));
        assertThat(target.blogicExecutor, is(mockBLogicExecutor));
        assertThat(target.jobStatusChanger, is(mockJobStatusChanger));
    }

    /**
     * {@code beforeExecute}のテスト01 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・ジョブシーケンスコードに該当するレコードを更新できた場合、{@code true}を返却すること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testBeforeExecute01() throws Exception {
        when(mockJobStatusChanger.changeToStartStatus(anyString())).thenReturn(
                true);

        AsyncJobWorkerImpl target = new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger);

        // テスト実行
        boolean actual = target.beforeExecute("0000001");

        assertTrue(actual);
        verify(mockJobStatusChanger).changeToStartStatus("0000001");
    }

    /**
     * {@code beforeExecute}のテスト02 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・ジョブシーケンスコードに該当するレコードを更新できなかった(既にジョブステータスコードが「実行中：1」)場合、{@code false}を返却すること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testBeforeExecute02() throws Exception {
        when(mockJobStatusChanger.changeToStartStatus(anyString())).thenReturn(
                false);

        AsyncJobWorkerImpl target = new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder,
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger);

        // テスト実行
        boolean actual = target.beforeExecute("0000001");

        assertFalse(actual);
        verify(mockJobStatusChanger).changeToStartStatus("0000001");
    }

    /**
     * {@code beforeExecute}のテスト03 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・ジョブシーケンスコードに該当するレコードを更新時に例外がスローされた場合、その例外がそのままスローされてくること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testBeforeExecute03() throws Exception {
        @SuppressWarnings("serial")
        Exception ex = new DataAccessException("dummy exception") {};
        when(mockJobStatusChanger.changeToStartStatus(anyString())).thenThrow(
                ex);

        AsyncJobWorkerImpl target = new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger);

        // テスト実行
        try {
            target.beforeExecute("0000001");
            fail("The exception has not been detected.");
        } catch (DataAccessException dae) {
            assertSame(ex, dae);
            verify(mockJobStatusChanger).changeToStartStatus("0000001");
        }
    }

    /**
     * {@code executeWorker}のテスト01 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code JobControlFinder}にて{@code BatchJobData}を取得する際に例外が発生した場合、
     *  {@code afterExecuteWorker}及び{@code BLogicApplicationContextResolver}の{closeApplicationContext}が呼ばれ、終了すること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteWorker01() throws Exception {
        Exception ex = new IllegalArgumentException();
        when(mockJobControlFinder.resolveBatchJobData(anyString()))
                .thenThrow(ex);

        ArgumentCaptor<BLogicResult> blogicResultCaptor = ArgumentCaptor
                .forClass(BLogicResult.class);

        AsyncJobWorkerImpl target = spy(new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger));
        doNothing().when(target).afterExecuteWorker(anyString(),
                blogicResultCaptor.capture());
        doReturn(true).when(target).beforeExecute(anyString());

        // テスト実行
        target.executeWorker("0000001");

        verify(mockBLogicApplicationContextResolver, never())
                .resolveApplicationContext(any(BatchJobData.class));
        verify(mockBLogicResolver, never()).resolveBLogic(
                any(ApplicationContext.class), eq("0000001"));
        verify(mockBLogicParamConverter, never()).convertBLogicParam(
                any(BatchJobData.class));
        verify(mockBLogicExceptionHandlerResolver, never())
                .resolveExceptionHandler(any(ApplicationContext.class),
                        anyString());

        verify(target).afterExecuteWorker(eq("0000001"),
                any(BLogicResult.class));
        verify(mockBLogicApplicationContextResolver)
                .closeApplicationContext(null);

        BLogicResult blogicResult = blogicResultCaptor.getValue();
        assertThat(blogicResult.getBlogicStatus(), is(255));
        assertThat(blogicResult.getBlogicThrowable(), nullValue());

        verify(mockBLogicExecutor, never()).execute(
                any(ApplicationContext.class), any(BLogic.class),
                any(BLogicParam.class), any(ExceptionHandler.class));

        assertThat(logger.getLoggingEvents(), is(asList(error(ex,
                "[EAL025055] Failed to pre-processing of the BLogic. JobSequenceId:0000001"))));
    }

    /**
     * {@code executeWorker}のテスト02 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code BLogicApplicationContextResolver}にてジョブ用の{@code ApplicationContext}を取得する際に例外が発生した場合、
     *  {@code afterExecuteWorker}及び{@code BLogicApplicationContextResolver}の{closeApplicationContext}が呼ばれ、終了すること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteWorker02() throws Exception {
        Exception ex = new IllegalArgumentException();
        when(
                mockBLogicApplicationContextResolver
                        .resolveApplicationContext(any(BatchJobData.class)))
                .thenThrow(ex);

        ArgumentCaptor<BLogicResult> blogicResultCaptor = ArgumentCaptor
                .forClass(BLogicResult.class);

        AsyncJobWorkerImpl target = spy(new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger));
        doNothing().when(target).afterExecuteWorker(anyString(),
                blogicResultCaptor.capture());
        doReturn(true).when(target).beforeExecute(anyString());

        // テスト実行
        target.executeWorker("0000001");

        verify(mockJobControlFinder).resolveBatchJobData(
                "0000001");
        verify(mockBLogicResolver, never()).resolveBLogic(
                any(ApplicationContext.class), anyString());
        verify(mockBLogicParamConverter, never()).convertBLogicParam(
                any(BatchJobData.class));
        verify(mockBLogicExceptionHandlerResolver, never())
                .resolveExceptionHandler(any(ApplicationContext.class),
                        anyString());

        verify(target).afterExecuteWorker(eq("0000001"),
                any(BLogicResult.class));
        verify(mockBLogicApplicationContextResolver)
                .closeApplicationContext(null);

        BLogicResult blogicResult = blogicResultCaptor.getValue();
        assertThat(blogicResult.getBlogicStatus(), is(255));
        assertThat(blogicResult.getBlogicThrowable(), nullValue());

        verify(mockBLogicExecutor, never()).execute(
                any(ApplicationContext.class), any(BLogic.class),
                any(BLogicParam.class), any(ExceptionHandler.class));

        assertThat(logger.getLoggingEvents(), is(asList(error(ex,
                "[EAL025055] Failed to pre-processing of the BLogic. JobSequenceId:0000001"))));
    }

    /**
     * {@code executeWorker}のテスト03 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code BLogicResolver}にて{@code BLogic}のBeanを取得する際に例外が発生した場合、
     *  {@code afterExecuteWorker}及び{@code BLogicApplicationContextResolver}の{closeApplicationContext}が呼ばれ、終了すること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteWorker03() throws Exception {
        Exception ex = new IllegalArgumentException();
        when(
                mockBLogicResolver.resolveBLogic(any(ApplicationContext.class),
                        anyString())).thenThrow(ex);

        ArgumentCaptor<BLogicResult> blogicResultCaptor = ArgumentCaptor
                .forClass(BLogicResult.class);

        AsyncJobWorkerImpl target = spy(new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger));
        doNothing().when(target).afterExecuteWorker(anyString(),
                blogicResultCaptor.capture());
        doReturn(true).when(target).beforeExecute(anyString());

        BatchJobData batchJobData = new BatchJobData();
        batchJobData.setJobAppCd("0000001");
        doReturn(batchJobData).when(mockJobControlFinder)
                .resolveBatchJobData("seq0000001");

        // テスト実行
        target.executeWorker("seq0000001");

        verify(mockJobControlFinder).resolveBatchJobData(
                "seq0000001");
        verify(mockBLogicApplicationContextResolver)
                .resolveApplicationContext(any(BatchJobData.class));
        verify(mockBLogicParamConverter, never()).convertBLogicParam(
                any(BatchJobData.class));
        verify(mockBLogicExceptionHandlerResolver, never())
                .resolveExceptionHandler(any(ApplicationContext.class),
                        anyString());

        verify(target).afterExecuteWorker(eq("seq0000001"),
                any(BLogicResult.class));
        verify(mockBLogicApplicationContextResolver)
                .closeApplicationContext(null);

        BLogicResult blogicResult = blogicResultCaptor.getValue();
        assertThat(blogicResult.getBlogicStatus(), is(255));
        assertThat(blogicResult.getBlogicThrowable(), nullValue());

        verify(mockBLogicExecutor, never()).execute(
                any(ApplicationContext.class), any(BLogic.class),
                any(BLogicParam.class), any(ExceptionHandler.class));

        assertThat(logger.getLoggingEvents(), is(asList(error(ex,
                "[EAL025055] Failed to pre-processing of the BLogic. JobSequenceId:seq0000001"))));
    }

    /**
     * {@code executeWorker}のテスト04 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code BLogicParamConverter}にて{@code BLogicParam}を取得する際に例外が発生した場合、
     *  {@code afterExecuteWorker}及び{@code BLogicApplicationContextResolver}の{closeApplicationContext}が呼ばれ、終了すること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteWorker04() throws Exception {
        Exception ex = new IllegalArgumentException();
        when(
                mockBLogicParamConverter
                        .convertBLogicParam(any(BatchJobData.class)))
                .thenThrow(ex);

        ArgumentCaptor<BLogicResult> blogicResultCaptor = ArgumentCaptor
                .forClass(BLogicResult.class);

        AsyncJobWorkerImpl target = spy(new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger));
        doNothing().when(target).afterExecuteWorker(anyString(),
                blogicResultCaptor.capture());
        doReturn(true).when(target).beforeExecute(anyString());


        BatchJobData batchJobData = new BatchJobData();
        batchJobData.setJobAppCd("0000001");
        doReturn(batchJobData).when(mockJobControlFinder)
                .resolveBatchJobData("seq0000001");

        // テスト実行
        target.executeWorker("seq0000001");

        verify(mockJobControlFinder).resolveBatchJobData(
                "seq0000001");
        verify(mockBLogicApplicationContextResolver)
                .resolveApplicationContext(any(BatchJobData.class));
        verify(mockBLogicResolver).resolveBLogic(
                any(ApplicationContext.class), eq("0000001"));
        verify(mockBLogicExceptionHandlerResolver, never())
                .resolveExceptionHandler(any(ApplicationContext.class),
                        anyString());

        verify(target).afterExecuteWorker(eq("seq0000001"),
                any(BLogicResult.class));
        verify(mockBLogicApplicationContextResolver)
                .closeApplicationContext(null);

        BLogicResult blogicResult = blogicResultCaptor.getValue();
        assertThat(blogicResult.getBlogicStatus(), is(255));
        assertThat(blogicResult.getBlogicThrowable(), nullValue());

        verify(mockBLogicExecutor, never()).execute(
                any(ApplicationContext.class), any(BLogic.class),
                any(BLogicParam.class), any(ExceptionHandler.class));

        assertThat(logger.getLoggingEvents(), is(asList(error(ex,
                "[EAL025055] Failed to pre-processing of the BLogic. JobSequenceId:seq0000001"))));
    }

    /**
     * {@code executeWorker}のテスト05 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code BLogicExceptionHandlerResolver}にて{@code BLogicExceptionHandler}を取得する際に例外が発生した場合、
     *  後続の処理である、{@code BLogicExecutor}の{@code execute}、{@code afterExecuteWorker}及び
     *  {@code BLogicApplicationContextResolver}の{closeApplicationContext}が呼ばれ、終了すること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteWorker05() throws Exception {
        BatchJobData batchJobData = new BatchJobData() {
            {
                setJobAppCd("0000001");
            }
        };
        when(mockJobControlFinder.resolveBatchJobData("0000001"))
                .thenReturn(batchJobData);

        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext();
        when(
                mockBLogicApplicationContextResolver
                        .resolveApplicationContext(batchJobData)).thenReturn(
                applicationContext);
        BLogic blogic = mock(BLogic.class);
        doReturn(0).when(blogic).execute(any(BLogicParam.class));
        
        when(mockBLogicResolver.resolveBLogic(applicationContext, "0000001"))
                .thenReturn(blogic);

        BLogicParam param = new BLogicParam();
        when(mockBLogicParamConverter.convertBLogicParam(batchJobData))
                .thenReturn(param);
        when(
                mockBLogicExceptionHandlerResolver.resolveExceptionHandler(
                        applicationContext, "0000001")).thenThrow(
                new IllegalArgumentException());
        BLogicResult result = new BLogicResult() {
            {
                setBlogicStatus(1);
            }
        };
        when(
                mockBLogicExecutor.execute(applicationContext, blogic, param,
                        null)).thenReturn(result);

        ArgumentCaptor<BLogicResult> blogicResultCaptor = ArgumentCaptor
                .forClass(BLogicResult.class);

        AsyncJobWorkerImpl target = spy(new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger));
        doNothing().when(target).afterExecuteWorker(anyString(),
                blogicResultCaptor.capture());
        doReturn(true).when(target).beforeExecute(anyString());

        // テスト実行
        target.executeWorker("0000001");

        verify(mockBLogicExecutor).execute(applicationContext,
                blogic, param, null);
        verify(target).afterExecuteWorker(eq("0000001"),
                any(BLogicResult.class));
        verify(mockBLogicApplicationContextResolver)
                .closeApplicationContext(applicationContext);

        BLogicResult blogicResult = blogicResultCaptor.getValue();
        assertThat(blogicResult.getBlogicStatus(), is(1));
        assertThat(blogicResult.getBlogicThrowable(), nullValue());

        assertThat(logger.getLoggingEvents(), is(asList(warn(
                "[WAL025010] The BLogic execution continues without an ExceptionHandler."))));
    }

    /**
     * {@code executeWorker}のテスト06 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code BLogicExecutor}の{@code execute}で例外がスローされた場合、ログを出力し、
     *  {@code afterExecuteWorker}及び{@code BLogicApplicationContextResolver}の{closeApplicationContext}が呼ばれ、終了すること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteWorker06() throws Exception {
        Exception ex = new IllegalArgumentException();
        when(
                mockBLogicExecutor.execute(any(ApplicationContext.class),
                        any(BLogic.class), any(BLogicParam.class),
                        any(ExceptionHandler.class))).thenThrow(ex);
        ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
        when(exceptionHandler.handleThrowableException(any(Throwable.class))).thenReturn(0);
        
        when(
                mockBLogicExceptionHandlerResolver.resolveExceptionHandler(
                        any(ApplicationContext.class), anyString()))
                .thenReturn(exceptionHandler);

        ArgumentCaptor<BLogicResult> blogicResultCaptor = ArgumentCaptor
                .forClass(BLogicResult.class);

        AsyncJobWorkerImpl target = spy(new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger));
        doNothing().when(target).afterExecuteWorker(anyString(),
                blogicResultCaptor.capture());
        doReturn(true).when(target).beforeExecute(anyString());

        BatchJobData batchJobData = new BatchJobData();
        batchJobData.setJobAppCd("0000001");
        doReturn(batchJobData).when(mockJobControlFinder)
                .resolveBatchJobData("seq0000001");

        // テスト実行
        target.executeWorker("seq0000001");

        verify(target).afterExecuteWorker(eq("seq0000001"),
                any(BLogicResult.class));
        verify(mockBLogicApplicationContextResolver)
                .closeApplicationContext(any(ApplicationContext.class));

        BLogicResult blogicResult = blogicResultCaptor.getValue();
        assertThat(blogicResult.getBlogicStatus(), is(255));
        assertThat(blogicResult.getBlogicThrowable(), nullValue());

        assertThat(logger.getLoggingEvents(), is(asList(error(ex,
                "[EAL025059] The BLogic execution has failed during processing. JobSequenceId:seq0000001"))));
    }

    /**
     * {@code executeWorker}のテスト07 【正常系】<br>
     *
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     *  {@code beforeExecute()}がfalseを返却した際、INFOログを出力し後続を処理せず終了すること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteWorker07() throws Exception {
        Exception ex = new IllegalArgumentException();
        when(
                mockBLogicExecutor.execute(any(ApplicationContext.class),
                        any(BLogic.class), any(BLogicParam.class),
                        any(ExceptionHandler.class))).thenThrow(ex);
        ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
        when(exceptionHandler.handleThrowableException(any(Throwable.class))).thenReturn(0);

        when(
                mockBLogicExceptionHandlerResolver.resolveExceptionHandler(
                        any(ApplicationContext.class), anyString()))
                .thenReturn(exceptionHandler);

        ArgumentCaptor<BLogicResult> blogicResultCaptor = ArgumentCaptor
                .forClass(BLogicResult.class);

        AsyncJobWorkerImpl target = spy(new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver,
                mockBLogicApplicationContextResolver,
                mockJobControlFinder,
                mockBLogicParamConverter,
                mockBLogicExecutor,
                mockJobStatusChanger));
        doNothing().when(target).afterExecuteWorker(anyString(),
                blogicResultCaptor.capture());
        doReturn(false).when(target).beforeExecute(anyString());

        // テスト実行
        target.executeWorker("0000001");

        verify(target, never()).afterExecuteWorker(eq("0000001"),
                any(BLogicResult.class));
        verify(mockBLogicApplicationContextResolver, never())
                .closeApplicationContext(any(ApplicationContext.class));

        assertThat(logger.getLoggingEvents(), is(asList(info(
                "[IAL025021] Skipped job execution, because target job was not found. jobSequenceId:0000001"))));
    }

    /**
     * {@code afterExecuteWorker}のテスト01 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code JobStatusChanger}の戻り値が{@code true}の場合、何もログが吐かれずに終了すること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAfterExecuteWorker01() throws Exception {
        when(
                mockJobStatusChanger.changeToEndStatus(anyString(),
                        any(BLogicResult.class))).thenReturn(true);
        BLogicResult blogicResult = new BLogicResult() {
            {
                setBlogicStatus(1);
            }
        };
        AsyncJobWorkerImpl target = new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger);

        // テスト実行
        target.afterExecuteWorker("000001", blogicResult);

        verify(mockJobStatusChanger).changeToEndStatus("000001",
                blogicResult);
    }

    /**
     * {@code afterExecuteWorker}のテスト02 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code JobStatusChanger}の戻り値が{@code false}の場合、ログが吐かれて終わること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAfterExecuteWorker02() throws Exception {
        when(
                mockJobStatusChanger.changeToEndStatus(anyString(),
                        any(BLogicResult.class))).thenReturn(false);
        BLogicResult blogicResult = new BLogicResult() {
            {
                setBlogicStatus(1);
            }
        };
        AsyncJobWorkerImpl target = new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger);

        // テスト実行
        target.afterExecuteWorker("000001", blogicResult);

        verify(mockJobStatusChanger).changeToEndStatus("000001",
                blogicResult);

        assertThat(logger.getLoggingEvents(), is(asList(error(
                "[EAL025025] Job status update error. JobSequenceId:000001 blogicStatus:1"))));
    }

    /**
     * {@code afterExecuteWorker}のテスト03 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code JobStatusChanger}から例外がスローされた場合、ログが吐かれて終わること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAfterExecuteWorker03() throws Exception {
        Exception ex = new RuntimeException("for test.");
        when(mockJobStatusChanger.changeToEndStatus(anyString(), any(
                BLogicResult.class))).thenThrow(ex);
        BLogicResult blogicResult = new BLogicResult() {
            {
                setBlogicStatus(1);
            }
        };
        AsyncJobWorkerImpl target = new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder, 
                mockBLogicParamConverter,
                mockBLogicExecutor, 
                mockJobStatusChanger);

        // テスト実行
        target.afterExecuteWorker("000001", blogicResult);

        verify(mockJobStatusChanger).changeToEndStatus("000001",
                blogicResult);

        assertThat(logger.getLoggingEvents(), is(asList(error(ex,
                "[EAL025025] Job status update error. JobSequenceId:000001 blogicStatus:1"))));
    }

    /**
     * {@code afterExecuteWorker}のテスト04 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・引数の{@code BLogicResult}が{@code null}の場合、ログに{@code null}の情報が吐かれて終わること
     * </pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAfterExecuteWorker04() throws Exception {
        when(
                mockJobStatusChanger.changeToEndStatus(anyString(),
                        any(BLogicResult.class))).thenReturn(false);

        AsyncJobWorkerImpl target = new AsyncJobWorkerImpl(mockBLogicResolver,
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockJobControlFinder, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger);

        // テスト実行
        target.afterExecuteWorker("000001", null);

        verify(mockJobStatusChanger)
                .changeToEndStatus("000001", null);

        assertThat(logger.getLoggingEvents(), is(asList(error(
                "[EAL025025] Job status update error. JobSequenceId:000001 blogicStatus:null"))));
    }
}
