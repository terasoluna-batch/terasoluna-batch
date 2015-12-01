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

package jp.terasoluna.fw.batch.executor.worker;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static uk.org.lidalia.slf4jtest.LoggingEvent.*;
import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.BLogicApplicationContextResolver;
import jp.terasoluna.fw.batch.blogic.BLogicResolver;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParamConverter;
import jp.terasoluna.fw.batch.exception.handler.BLogicExceptionHandlerResolver;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.repository.BatchJobDataRepository;
import jp.terasoluna.fw.batch.executor.repository.JobStatusChanger;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.ex.unit.mock.spring.MockDataAccessException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * WorkerTemplateImpleのテスト
 * @since 3.6
 */
public class WorkerTemplateImplTest {

    private TestLogger logger = TestLoggerFactory
            .getTestLogger(WorkerTemplateImpl.class);

    private BLogicResolver mockBLogicResolver;

    private BLogicExceptionHandlerResolver mockBLogicExceptionHandlerResolver;

    private BLogicApplicationContextResolver mockBLogicApplicationContextResolver;

    private BatchJobDataRepository mockBatchJobDataRepository;

    private BLogicParamConverter mockBLogicParamConverter;

    private BLogicExecutor mockBLogicExecutor;

    private JobStatusChanger mockJobStatusChanger;

    /**
     * WorkerTemplateImplの各フィールドのモックを生成する
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.mockBLogicResolver = mock(BLogicResolver.class);
        this.mockBLogicExceptionHandlerResolver = mock(BLogicExceptionHandlerResolver.class);
        this.mockBLogicApplicationContextResolver = mock(BLogicApplicationContextResolver.class);
        this.mockBatchJobDataRepository = mock(BatchJobDataRepository.class);
        this.mockBLogicParamConverter = mock(BLogicParamConverter.class);
        this.mockBLogicExecutor = mock(BLogicExecutor.class);
        this.mockJobStatusChanger = mock(JobStatusChanger.class);
    }

    /**
     * WorkerTemplateImplの各フィールドのモックをクリアにする
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.mockBLogicResolver = null;
        this.mockBLogicExceptionHandlerResolver = null;
        this.mockBLogicApplicationContextResolver = null;
        this.mockBatchJobDataRepository = null;
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
     * ・{@code WorkerTemplateImpl}の{@code bLogicResolver}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testWorkerTemplateImpl01() throws Exception {
        try {
            // テスト実行
            new WorkerTemplateImpl(null, mockBLogicExceptionHandlerResolver, 
                    mockBLogicApplicationContextResolver, 
                    mockBatchJobDataRepository, 
                    mockBLogicParamConverter, 
                    mockBLogicExecutor, 
                    mockJobStatusChanger);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(
                    e.getMessage(),
                    is("[EAL025060] [Assertion failed] - WorkerTemplateImpl constructor needs BLogicResolver"));
        }
    }

    /**
     * コンストラクタのテスト02 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code WorkerTemplateImpl}の{@code bLogicExceptionHandlerResolver}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testWorkerTemplateImpl02() throws Exception {
        try {
            // テスト実行
            new WorkerTemplateImpl(mockBLogicResolver, 
                    null, 
                    mockBLogicApplicationContextResolver, 
                    mockBatchJobDataRepository, 
                    mockBLogicParamConverter, 
                    mockBLogicExecutor, 
                    mockJobStatusChanger);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(
                    e.getMessage(),
                    is("[EAL025061] [Assertion failed] - WorkerTemplateImpl constructor needs BLogicExceptionHandlerResolver"));
        }
    }

    /**
     * コンストラクタのテスト03 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code WorkerTemplateImpl}の{@code bLogicApplicationContextResolver}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testWorkerTemplateImpl03() throws Exception {
        try {
            // テスト実行
            new WorkerTemplateImpl(mockBLogicResolver, 
                    mockBLogicExceptionHandlerResolver, 
                    null, 
                    mockBatchJobDataRepository, 
                    mockBLogicParamConverter, 
                    mockBLogicExecutor, 
                    mockJobStatusChanger);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(
                    e.getMessage(),
                    is("[EAL025062] [Assertion failed] - WorkerTemplateImpl constructor needs BLogicApplicationContextResolver"));
        }
    }

    /**
     * コンストラクタのテスト04 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code WorkerTemplateImpl}の{@code batchJobDataRepository}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testWorkerTemplateImpl04() throws Exception {
        try {
            // テスト実行
            new WorkerTemplateImpl(mockBLogicResolver, 
                    mockBLogicExceptionHandlerResolver, 
                    mockBLogicApplicationContextResolver, 
                    null, 
                    mockBLogicParamConverter, 
                    mockBLogicExecutor, 
                    mockJobStatusChanger);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(
                    e.getMessage(),
                    is("[EAL025063] [Assertion failed] - WorkerTemplateImpl constructor needs BatchJobDataRepository"));
        }
    }

    /**
     * コンストラクタのテスト05 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code WorkerTemplateImpl}の{@code bLogicParamConverter}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testWorkerTemplateImpl05() throws Exception {
        try {
            // テスト実行
            new WorkerTemplateImpl(mockBLogicResolver, 
                    mockBLogicExceptionHandlerResolver, 
                    mockBLogicApplicationContextResolver, 
                    mockBatchJobDataRepository, 
                    null, 
                    mockBLogicExecutor, 
                    mockJobStatusChanger);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(
                    e.getMessage(),
                    is("[EAL025064] [Assertion failed] - WorkerTemplateImpl constructor needs BLogicParamConverter"));
        }
    }

    /**
     * コンストラクタのテスト06 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code WorkerTemplateImpl}の{@code bLogicExecutor}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testWorkerTemplateImpl06() throws Exception {
        try {
            // テスト実行
            new WorkerTemplateImpl(mockBLogicResolver, 
                    mockBLogicExceptionHandlerResolver,
                    mockBLogicApplicationContextResolver, 
                    mockBatchJobDataRepository, 
                    mockBLogicParamConverter, 
                    null, 
                    mockJobStatusChanger);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(
                    e.getMessage(),
                    is("[EAL025065] [Assertion failed] - WorkerTemplateImpl constructor needs BLogicExecutor"));
        }
    }

    /**
     * コンストラクタのテスト07 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code WorkerTemplateImpl}の{@code jobStatusChanger}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローする
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testWorkerTemplateImpl07() throws Exception {
        try {
            // テスト実行
            new WorkerTemplateImpl(mockBLogicResolver,
                    mockBLogicExceptionHandlerResolver,
                    mockBLogicApplicationContextResolver, 
                    mockBatchJobDataRepository, 
                    mockBLogicParamConverter, 
                    mockBLogicExecutor, 
                    null);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(
                    e.getMessage(),
                    is("[EAL025066] [Assertion failed] - WorkerTemplateImpl constructor needs BatchStatusChanger"));
        }
    }

    /**
     * コンストラクタのテスト08 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code WorkerTemplateImpl}の{@code bLogicResolver}、{@code bLogicExceptionHandlerResolver}、{@code bLogicApplicationContextResolver}、{@code batchJobDataRepository}、{@code bLogicParamConverter}、{@code bLogicExecutor}、{@code jobStatusChanger}が正しく設定されていること
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testWorkerTemplateImpl08() throws Exception {
        // テスト実行
        WorkerTemplateImpl target = new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger);

        assertThat(target.bLogicResolver, is(mockBLogicResolver));
        assertThat(target.bLogicExceptionHandlerResolver,
                is(mockBLogicExceptionHandlerResolver));
        assertThat(target.bLogicApplicationContextResolver,
                is(mockBLogicApplicationContextResolver));
        assertThat(target.batchJobDataRepository,
                is(mockBatchJobDataRepository));
        assertThat(target.bLogicParamConverter, is(mockBLogicParamConverter));
        assertThat(target.bLogicExecutor, is(mockBLogicExecutor));
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
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testBeforeExecute01() throws Exception {
        when(mockJobStatusChanger.changeToStartStatus(anyString())).thenReturn(
                true);

        WorkerTemplateImpl target = new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository, 
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
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testBeforeExecute02() throws Exception {
        when(mockJobStatusChanger.changeToStartStatus(anyString())).thenReturn(
                false);

        WorkerTemplateImpl target = new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository,
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger);

        // テスト実行
        boolean actual = target.beforeExecute("0000001");

        assertFalse(actual);
        verify(mockJobStatusChanger).changeToStartStatus("0000001");
        assertThat(
                logger.getLoggingEvents(),
                is(asList(info("[IAL025010] Job status update error.(JOB_SEQ_ID:0000001)"))));
    }

    /**
     * {@code beforeExecute}のテスト03 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・ジョブシーケンスコードに該当するレコードを更新時に例外がスローされた場合、{@code false}を返却すること
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testBeforeExecute03() throws Exception {
        Exception ex = new MockDataAccessException("dummy exception");
        when(mockJobStatusChanger.changeToStartStatus(anyString())).thenThrow(
                ex);

        WorkerTemplateImpl target = new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger);

        // テスト実行
        boolean actual = target.beforeExecute("0000001");

        assertFalse(actual);
        verify(mockJobStatusChanger).changeToStartStatus("0000001");
        assertThat(
                logger.getLoggingEvents(),
                is(asList(info(
                        ex,
                        "[IAL025010] Job status update error.(JOB_SEQ_ID:0000001)"))));
    }

    /**
     * {@code executeWorker}のテスト01 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code BatchJobDataRepository}にて{@code BatchJobData}を取得する際に例外が発生した場合、
     *  {@code afterExecuteWorker}及び{@code BLogicApplicationContextResolver}の{closeApplicationContext}が呼ばれ、終了すること
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteWorker01() throws Exception {
        Exception ex = new IllegalArgumentException();
        when(mockBatchJobDataRepository.resolveBatchJobData(anyString()))
                .thenThrow(ex);

        ArgumentCaptor<BLogicResult> bLogicResultCaptor = ArgumentCaptor
                .forClass(BLogicResult.class);

        WorkerTemplateImpl target = spy(new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger));
        doNothing().when(target).afterExecuteWorker(anyString(),
                bLogicResultCaptor.capture());

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

        BLogicResult bLogicResult = bLogicResultCaptor.getValue();
        assertThat(bLogicResult.getBlogicStatus(), is(-1));
        assertThat(bLogicResult.getBlogicThrowable(), nullValue());

        verify(mockBLogicExecutor, never()).execute(
                any(ApplicationContext.class), any(BLogic.class),
                any(BLogicParam.class), any(ExceptionHandler.class));

        assertThat(
                logger.getLoggingEvents(),
                is(asList(error(
                        ex,
                        "[EAL025068] JobSequenceId:0000001 : The previous processing of the BLogic execution has failed."))));
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
     * <\pre>
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

        ArgumentCaptor<BLogicResult> bLogicResultCaptor = ArgumentCaptor
                .forClass(BLogicResult.class);

        WorkerTemplateImpl target = spy(new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger));
        doNothing().when(target).afterExecuteWorker(anyString(),
                bLogicResultCaptor.capture());

        // テスト実行
        target.executeWorker("0000001");

        verify(mockBatchJobDataRepository).resolveBatchJobData(
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

        BLogicResult bLogicResult = bLogicResultCaptor.getValue();
        assertThat(bLogicResult.getBlogicStatus(), is(-1));
        assertThat(bLogicResult.getBlogicThrowable(), nullValue());

        verify(mockBLogicExecutor, never()).execute(
                any(ApplicationContext.class), any(BLogic.class),
                any(BLogicParam.class), any(ExceptionHandler.class));

        assertThat(
                logger.getLoggingEvents(),
                is(asList(error(
                        ex,
                        "[EAL025068] JobSequenceId:0000001 : The previous processing of the BLogic execution has failed."))));
    }

    /**
     * {@code executeWorker}のテスト03 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code BLogicResover}にて{@code BLogic}のBeanを取得する際に例外が発生した場合、
     *  {@code afterExecuteWorker}及び{@code BLogicApplicationContextResolver}の{closeApplicationContext}が呼ばれ、終了すること
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testExecuteWorker03() throws Exception {
        Exception ex = new IllegalArgumentException();
        when(
                mockBLogicResolver.resolveBLogic(any(ApplicationContext.class),
                        anyString())).thenThrow(ex);

        ArgumentCaptor<BLogicResult> bLogicResultCaptor = ArgumentCaptor
                .forClass(BLogicResult.class);

        WorkerTemplateImpl target = spy(new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger));
        doNothing().when(target).afterExecuteWorker(anyString(),
                bLogicResultCaptor.capture());

        // テスト実行
        target.executeWorker("0000001");

        verify(mockBatchJobDataRepository).resolveBatchJobData(
                "0000001");
        verify(mockBLogicApplicationContextResolver)
                .resolveApplicationContext(any(BatchJobData.class));
        verify(mockBLogicParamConverter, never()).convertBLogicParam(
                any(BatchJobData.class));
        verify(mockBLogicExceptionHandlerResolver, never())
                .resolveExceptionHandler(any(ApplicationContext.class),
                        anyString());

        verify(target).afterExecuteWorker(eq("0000001"),
                any(BLogicResult.class));
        verify(mockBLogicApplicationContextResolver)
                .closeApplicationContext(null);

        BLogicResult bLogicResult = bLogicResultCaptor.getValue();
        assertThat(bLogicResult.getBlogicStatus(), is(-1));
        assertThat(bLogicResult.getBlogicThrowable(), nullValue());

        verify(mockBLogicExecutor, never()).execute(
                any(ApplicationContext.class), any(BLogic.class),
                any(BLogicParam.class), any(ExceptionHandler.class));

        assertThat(
                logger.getLoggingEvents(),
                is(asList(error(
                        ex,
                        "[EAL025068] JobSequenceId:0000001 : The previous processing of the BLogic execution has failed."))));
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
     * <\pre>
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

        ArgumentCaptor<BLogicResult> bLogicResultCaptor = ArgumentCaptor
                .forClass(BLogicResult.class);

        WorkerTemplateImpl target = spy(new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger));
        doNothing().when(target).afterExecuteWorker(anyString(),
                bLogicResultCaptor.capture());

        // テスト実行
        target.executeWorker("0000001");

        verify(mockBatchJobDataRepository).resolveBatchJobData(
                "0000001");
        verify(mockBLogicApplicationContextResolver)
                .resolveApplicationContext(any(BatchJobData.class));
        verify(mockBLogicResolver).resolveBLogic(
                any(ApplicationContext.class), eq("0000001"));
        verify(mockBLogicExceptionHandlerResolver, never())
                .resolveExceptionHandler(any(ApplicationContext.class),
                        anyString());

        verify(target).afterExecuteWorker(eq("0000001"),
                any(BLogicResult.class));
        verify(mockBLogicApplicationContextResolver)
                .closeApplicationContext(null);

        BLogicResult bLogicResult = bLogicResultCaptor.getValue();
        assertThat(bLogicResult.getBlogicStatus(), is(-1));
        assertThat(bLogicResult.getBlogicThrowable(), nullValue());

        verify(mockBLogicExecutor, never()).execute(
                any(ApplicationContext.class), any(BLogic.class),
                any(BLogicParam.class), any(ExceptionHandler.class));

        assertThat(
                logger.getLoggingEvents(),
                is(asList(error(
                        ex,
                        "[EAL025068] JobSequenceId:0000001 : The previous processing of the BLogic execution has failed."))));
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
     * <\pre>
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
        when(mockBatchJobDataRepository.resolveBatchJobData("0000001"))
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

        ArgumentCaptor<BLogicResult> bLogicResultCaptor = ArgumentCaptor
                .forClass(BLogicResult.class);

        WorkerTemplateImpl target = spy(new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger));
        doNothing().when(target).afterExecuteWorker(anyString(),
                bLogicResultCaptor.capture());

        // テスト実行
        target.executeWorker("0000001");

        verify(mockBLogicExecutor).execute(applicationContext,
                blogic, param, null);
        verify(target).afterExecuteWorker(eq("0000001"),
                any(BLogicResult.class));
        verify(mockBLogicApplicationContextResolver)
                .closeApplicationContext(applicationContext);

        BLogicResult bLogicResult = bLogicResultCaptor.getValue();
        assertThat(bLogicResult.getBlogicStatus(), is(1));
        assertThat(bLogicResult.getBlogicThrowable(), nullValue());

        assertThat(
                logger.getLoggingEvents(),
                is(asList(warn("[WAL025014] The BLogic execution continues without an ExceptionHandler."))));
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
     * <\pre>
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

        ArgumentCaptor<BLogicResult> bLogicResultCaptor = ArgumentCaptor
                .forClass(BLogicResult.class);

        WorkerTemplateImpl target = spy(new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger));
        doNothing().when(target).afterExecuteWorker(anyString(),
                bLogicResultCaptor.capture());

        // テスト実行
        target.executeWorker("0000001");

        verify(target).afterExecuteWorker(eq("0000001"),
                any(BLogicResult.class));
        verify(mockBLogicApplicationContextResolver)
                .closeApplicationContext(any(ApplicationContext.class));

        BLogicResult bLogicResult = bLogicResultCaptor.getValue();
        assertThat(bLogicResult.getBlogicStatus(), is(-1));
        assertThat(bLogicResult.getBlogicThrowable(), nullValue());

        assertThat(
                logger.getLoggingEvents(),
                is(asList(error(
                        ex,
                        "[EAL025093] JobSequenceId:0000001 : The BLogic execution has failed during processing."))));
    }

    /**
     * {@code afterExecuteWorker}のテスト01 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code JobStatusChanger}の戻り値が{@code true}の場合、何もログが吐かれずに終了すること
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAfterExecuteWorker01() throws Exception {
        when(
                mockJobStatusChanger.changeToEndStatus(anyString(),
                        any(BLogicResult.class))).thenReturn(true);
        BLogicResult bLogicResult = new BLogicResult() {
            {
                setBlogicStatus(1);
            }
        };
        WorkerTemplateImpl target = new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger);

        // テスト実行
        target.afterExecuteWorker("000001", bLogicResult);

        verify(mockJobStatusChanger).changeToEndStatus("000001",
                bLogicResult);
    }

    /**
     * {@code afterExecuteWorker}のテスト02 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code JobStatusChanger}の戻り値が{@code false}の場合、ログが吐かれて終わること
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAfterExecuteWorker02() throws Exception {
        when(
                mockJobStatusChanger.changeToEndStatus(anyString(),
                        any(BLogicResult.class))).thenReturn(false);
        BLogicResult bLogicResult = new BLogicResult() {
            {
                setBlogicStatus(1);
            }
        };
        WorkerTemplateImpl target = new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger);

        // テスト実行
        target.afterExecuteWorker("000001", bLogicResult);

        verify(mockJobStatusChanger).changeToEndStatus("000001",
                bLogicResult);

        assertThat(
                logger.getLoggingEvents(),
                is(asList(error("[EAL025025] Job status update error.(JOB_SEQ_ID:000001) blogicStatus:[1])"))));
    }

    /**
     * {@code afterExecuteWorker}のテスト03 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・{@code JobStatusChanger}から例外がスローされた場合、ログが吐かれて終わること
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAfterExecuteWorker03() throws Exception {
        when(
                mockJobStatusChanger.changeToEndStatus(anyString(),
                        any(BLogicResult.class))).thenThrow(
                new RuntimeException());
        BLogicResult bLogicResult = new BLogicResult() {
            {
                setBlogicStatus(1);
            }
        };
        WorkerTemplateImpl target = new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository, 
                mockBLogicParamConverter,
                mockBLogicExecutor, 
                mockJobStatusChanger);

        // テスト実行
        target.afterExecuteWorker("000001", bLogicResult);

        verify(mockJobStatusChanger).changeToEndStatus("000001",
                bLogicResult);

        assertThat(
                logger.getLoggingEvents(),
                is(asList(error("[EAL025025] Job status update error.(JOB_SEQ_ID:000001) blogicStatus:[1])"))));
    }

    /**
     * {@code afterExecuteWorker}のテスト04 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認事項
     * ・引数の{@code BLogicResult}が{@code null}の場合、ログに{@code null}の情報が吐かれて終わること
     * <\pre>
     * 
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAfterExecuteWorker04() throws Exception {
        when(
                mockJobStatusChanger.changeToEndStatus(anyString(),
                        any(BLogicResult.class))).thenReturn(false);

        WorkerTemplateImpl target = new WorkerTemplateImpl(mockBLogicResolver, 
                mockBLogicExceptionHandlerResolver, 
                mockBLogicApplicationContextResolver, 
                mockBatchJobDataRepository, 
                mockBLogicParamConverter, 
                mockBLogicExecutor, 
                mockJobStatusChanger);

        // テスト実行
        target.afterExecuteWorker("000001", null);

        verify(mockJobStatusChanger)
                .changeToEndStatus("000001", null);

        assertThat(
                logger.getLoggingEvents(),
                is(asList(error("[EAL025025] Job status update error.(JOB_SEQ_ID:000001) blogicStatus:[null])"))));
    }
}
