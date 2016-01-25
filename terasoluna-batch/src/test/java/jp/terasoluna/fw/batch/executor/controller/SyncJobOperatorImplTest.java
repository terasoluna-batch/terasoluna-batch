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

package jp.terasoluna.fw.batch.executor.controller;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.BLogicResolver;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParamConverter;
import jp.terasoluna.fw.batch.exception.handler.BLogicExceptionHandlerResolver;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.ApplicationContextResolver;
import jp.terasoluna.fw.batch.executor.BLogicExecutor;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.unit.util.SystemEnvUtils;

/**
 * {@code SyncJobOperatorImpl}のテストケース
 *
 * @since 3.6
 */
public class SyncJobOperatorImplTest {

    private SyncJobOperatorImpl target;

    private ApplicationContextResolver applicationContextResolver;

    private BLogicParamConverter blogicParamConverter;

    private BLogicExceptionHandlerResolver blogicExceptionHandlerResolver;

    private BLogicResolver blogicResolver;

    private BLogic blogic;

    private ApplicationContext blogicContext;

    private ExceptionHandler exceptionHandler;

    private BLogicExecutor blogicExecutor;

    /**
     * テスト前処理
     *
     * @throws Exception 予期しない例外
     */
    @Before
    public void setUp() throws Exception {
        this.applicationContextResolver = mock(
                ApplicationContextResolver.class);
        this.blogicParamConverter = mock(BLogicParamConverter.class);
        this.blogicExceptionHandlerResolver = mock(
                BLogicExceptionHandlerResolver.class);
        this.blogicResolver = mock(BLogicResolver.class);

        this.blogic = mock(BLogic.class);
        this.blogicContext = mock(ApplicationContext.class);
        this.exceptionHandler = mock(ExceptionHandler.class);
        this.blogicExecutor = mock(BLogicExecutor.class);

        when(applicationContextResolver.resolveApplicationContext(
                any(BatchJobData.class))).thenReturn(blogicContext);
        when(blogicParamConverter.convertBLogicParam(
                any(BatchJobData.class))).thenReturn(new BLogicParam());
        when(blogicResolver.resolveBLogic(any(ApplicationContext.class),
                anyString())).thenReturn(blogic);
        when(blogicExceptionHandlerResolver.resolveExceptionHandler(
                any(ApplicationContext.class), anyString())).thenReturn(
                exceptionHandler);

        this.target = new SyncJobOperatorImpl(applicationContextResolver,
                blogicParamConverter, blogicExceptionHandlerResolver,
                blogicResolver, blogicExecutor);
    }

    /**
     * コンストラクタのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・{@code applicationContextResolver}がnullであるとき、アサーションエラーとして
     * 　{@code IllegalArgumentException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testSyncJobOperatorImpl01() throws Exception {

        try {
            // テスト実行
            new SyncJobOperatorImpl(null, blogicParamConverter,
                    blogicExceptionHandlerResolver, blogicResolver,
                    blogicExecutor);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(),
 is(
                    "[EAL025056] [Assertion failed] - SyncJobOperatorImpl requires to set applicationContextResolver. please confirm the settings."));
        }
    }

    /**
     * コンストラクタのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・{@code blogicParamConverter}がnullであるとき、アサーションエラーとして
     * 　{@code IllegalArgumentException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testSyncJobOperatorImpl02() throws Exception {

        try {
            // テスト実行
            new SyncJobOperatorImpl(applicationContextResolver, null,
                    blogicExceptionHandlerResolver, blogicResolver,
                    blogicExecutor);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(),
 is(
                    "[EAL025056] [Assertion failed] - SyncJobOperatorImpl requires to set blogicParamConverter. please confirm the settings."));
        }
    }

    /**
     * コンストラクタのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・{@code blogicExceptionHandlerResolver}がnullであるとき、アサーションエラーとして
     * 　{@code IllegalArgumentException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testSyncJobOperatorImpl03() throws Exception {

        try {
            // テスト実行
            new SyncJobOperatorImpl(applicationContextResolver,
                    blogicParamConverter, null, blogicResolver, blogicExecutor);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(),
                    is("[EAL025056] [Assertion failed] - SyncJobOperatorImpl requires to set blogicExceptionHandlerResolver. please confirm the settings."));
        }
    }

    /**
     * コンストラクタのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・{@code blogicResolver}がnullであるとき、アサーションエラーとして
     * 　{@code IllegalArgumentException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testSyncJobOperatorImpl04() throws Exception {

        try {
            // テスト実行
            new SyncJobOperatorImpl(applicationContextResolver,
                    blogicParamConverter, blogicExceptionHandlerResolver, null,
                    blogicExecutor);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(),
                    is("[EAL025056] [Assertion failed] - SyncJobOperatorImpl requires to set blogicResolver. please confirm the settings."));
        }
    }

    /**
     * コンストラクタのテスト 【異常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・{@code blogicExecutor}がnullであるとき、アサーションエラーとして
     * 　{@code IllegalArgumentException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testSyncJobOperatorImpl05() throws Exception {

        try {
            // テスト実行
            new SyncJobOperatorImpl(applicationContextResolver,
                    blogicParamConverter, blogicExceptionHandlerResolver,
                    blogicResolver, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(),
                    is("[EAL025056] [Assertion failed] - SyncJobOperatorImpl requires to set blogicExecutor. please confirm the settings."));
        }
    }

    /**
     * コンストラクタのテスト 【正常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・コンストラクタ引数が全てnot nullのとき、初期化処理が正常終了すること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testSyncJobOperatorImpl06() throws Exception {

        // テスト実行
        SyncJobOperatorImpl target = new SyncJobOperatorImpl(
                applicationContextResolver, blogicParamConverter,
                blogicExceptionHandlerResolver, blogicResolver, blogicExecutor);

        assertThat(target.applicationContextResolver,
                is(applicationContextResolver));
        assertThat(target.blogicParamConverter, is(blogicParamConverter));
        assertThat(target.blogicExceptionHandlerResolver,
                is(blogicExceptionHandlerResolver));
        assertThat(target.blogicResolver, is(blogicResolver));
        assertThat(target.blogicExecutor, is(blogicExecutor));
    }

    /**
     * start()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・例外をスローすることなくビジネスロジックが終了した場合、
     * 　ビジネスロジックのステータスコードが返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testStart01() throws Exception {

        BLogicResult blogicResult = new BLogicResult();
        blogicResult.setBlogicStatus(234);
        BLogicParam blogicParam = new BLogicParam();
        doReturn(blogicParam).when(blogicParamConverter).convertBLogicParam(any(BatchJobData.class));
        doReturn(blogicResult).when(blogicExecutor).execute(blogicContext, blogic, blogicParam, exceptionHandler);

        // テスト実行
        int status = target.start(new String[] { "jobAppCd" });

        assertThat(status, is(234));
        verify(blogicExecutor).execute(blogicContext, blogic, blogicParam, exceptionHandler);
        verify(applicationContextResolver).closeApplicationContext(blogicContext);
    }

    /**
     * convertBatchJobData()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・空配列を渡した場合プロパティが設定されていない{@code BatchJobData}が返却される。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testConvertBatchJobData01() throws Exception {

        // テスト実行
        BatchJobData batchJobData = target.convertBatchJobData(new String[0]);

        assertThat(batchJobData.getJobAppCd(), is(""));
        assertThat(batchJobData.getJobArgNm1(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm2(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm3(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm4(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm5(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm6(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm7(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm8(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm9(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm10(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm11(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm12(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm13(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm14(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm15(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm16(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm17(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm18(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm19(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm20(), is(nullValue()));
        assertThat(batchJobData.getAddDateTime(), is(nullValue()));
        assertThat(batchJobData.getBLogicAppStatus(), is(""));
        assertThat(batchJobData.getCurAppStatus(), is(""));
        assertThat(batchJobData.getJobSequenceId(), is(""));
        assertThat(batchJobData.getUpdDateTime(), is(nullValue()));
    }

    /**
     * convertBatchJobData()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・第1引数のみ設定した配列を渡した場合、ジョブ業務コードのみ設定され、
     * ジョブシーケンスコード、業務ステータス、ステータスが空文字として設定された
     * {@code BatchJobData}が返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testConvertBatchJobData02() throws Exception {

        // テスト実行
        BatchJobData batchJobData = target.convertBatchJobData(
                new String[] { "jobAppCd" });

        assertThat(batchJobData.getJobAppCd(), is("jobAppCd"));
        assertThat(batchJobData.getJobArgNm1(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm2(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm3(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm4(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm5(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm6(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm7(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm8(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm9(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm10(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm11(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm12(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm13(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm14(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm15(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm16(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm17(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm18(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm19(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm20(), is(nullValue()));
        assertThat(batchJobData.getAddDateTime(), is(nullValue()));
        assertThat(batchJobData.getBLogicAppStatus(), is(""));
        assertThat(batchJobData.getCurAppStatus(), is(""));
        assertThat(batchJobData.getJobSequenceId(), is(""));
        assertThat(batchJobData.getUpdDateTime(), is(nullValue()));
    }

    /**
     * convertBatchJobData()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・第19引数まで設定した配列を渡した場合、ジョブ業務コードと引数1～19が設定された
     * {@code BatchJobData}が返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testConvertBatchJobData03() throws Exception {

        // テスト実行
        BatchJobData batchJobData = target.convertBatchJobData(
                new String[] { "jobAppCd", "1", "2", "3", "4", "5", "6", "7",
                        "8", "9", "10", "11", "12", "13", "14", "15", "16",
                        "17", "18", "19" });

        assertThat(batchJobData.getJobAppCd(), is("jobAppCd"));
        assertThat(batchJobData.getJobArgNm1(), is("1"));
        assertThat(batchJobData.getJobArgNm2(), is("2"));
        assertThat(batchJobData.getJobArgNm3(), is("3"));
        assertThat(batchJobData.getJobArgNm4(), is("4"));
        assertThat(batchJobData.getJobArgNm5(), is("5"));
        assertThat(batchJobData.getJobArgNm6(), is("6"));
        assertThat(batchJobData.getJobArgNm7(), is("7"));
        assertThat(batchJobData.getJobArgNm8(), is("8"));
        assertThat(batchJobData.getJobArgNm9(), is("9"));
        assertThat(batchJobData.getJobArgNm10(), is("10"));
        assertThat(batchJobData.getJobArgNm11(), is("11"));
        assertThat(batchJobData.getJobArgNm12(), is("12"));
        assertThat(batchJobData.getJobArgNm13(), is("13"));
        assertThat(batchJobData.getJobArgNm14(), is("14"));
        assertThat(batchJobData.getJobArgNm15(), is("15"));
        assertThat(batchJobData.getJobArgNm16(), is("16"));
        assertThat(batchJobData.getJobArgNm17(), is("17"));
        assertThat(batchJobData.getJobArgNm18(), is("18"));
        assertThat(batchJobData.getJobArgNm19(), is("19"));
        assertThat(batchJobData.getJobArgNm20(), is(nullValue()));
        assertThat(batchJobData.getAddDateTime(), is(nullValue()));
        assertThat(batchJobData.getBLogicAppStatus(), is(""));
        assertThat(batchJobData.getCurAppStatus(), is(""));
        assertThat(batchJobData.getJobSequenceId(), is(""));
        assertThat(batchJobData.getUpdDateTime(), is(nullValue()));
    }

    /**
     * convertBatchJobData()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・第21引数まで設定した配列を渡した場合、ジョブ業務コードと引数1～20が設定された
     * {@code BatchJobData}が返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testConvertBatchJobData04() throws Exception {

        // テスト実行
        BatchJobData batchJobData = target.convertBatchJobData(
                new String[] { "jobAppCd", "1", "2", "3", "4", "5", "6", "7",
                        "8", "9", "10", "11", "12", "13", "14", "15", "16",
                        "17", "18", "19", "20" });

        assertThat(batchJobData.getJobAppCd(), is("jobAppCd"));
        assertThat(batchJobData.getJobArgNm1(), is("1"));
        assertThat(batchJobData.getJobArgNm2(), is("2"));
        assertThat(batchJobData.getJobArgNm3(), is("3"));
        assertThat(batchJobData.getJobArgNm4(), is("4"));
        assertThat(batchJobData.getJobArgNm5(), is("5"));
        assertThat(batchJobData.getJobArgNm6(), is("6"));
        assertThat(batchJobData.getJobArgNm7(), is("7"));
        assertThat(batchJobData.getJobArgNm8(), is("8"));
        assertThat(batchJobData.getJobArgNm9(), is("9"));
        assertThat(batchJobData.getJobArgNm10(), is("10"));
        assertThat(batchJobData.getJobArgNm11(), is("11"));
        assertThat(batchJobData.getJobArgNm12(), is("12"));
        assertThat(batchJobData.getJobArgNm13(), is("13"));
        assertThat(batchJobData.getJobArgNm14(), is("14"));
        assertThat(batchJobData.getJobArgNm15(), is("15"));
        assertThat(batchJobData.getJobArgNm16(), is("16"));
        assertThat(batchJobData.getJobArgNm17(), is("17"));
        assertThat(batchJobData.getJobArgNm18(), is("18"));
        assertThat(batchJobData.getJobArgNm19(), is("19"));
        assertThat(batchJobData.getJobArgNm20(), is("20"));
        assertThat(batchJobData.getAddDateTime(), is(nullValue()));
        assertThat(batchJobData.getBLogicAppStatus(), is(""));
        assertThat(batchJobData.getCurAppStatus(), is(""));
        assertThat(batchJobData.getJobSequenceId(), is(""));
        assertThat(batchJobData.getUpdDateTime(), is(nullValue()));
    }

    /**
     * convertBatchJobData()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・第22引数まで設定した配列を渡した場合、ジョブ業務コードと第1～21引数
     * までの配列が{@code BatchJobData}に設定されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testConvertBatchJobData05() throws Exception {

        // テスト実行
        BatchJobData batchJobData = target.convertBatchJobData(
                new String[] { "jobAppCd", "1", "2", "3", "4", "5", "6", "7",
                        "8", "9", "10", "11", "12", "13", "14", "15", "16",
                        "17", "18", "19", "20", "21" });

        assertThat(batchJobData.getJobAppCd(), is("jobAppCd"));
        assertThat(batchJobData.getJobArgNm1(), is("1"));
        assertThat(batchJobData.getJobArgNm2(), is("2"));
        assertThat(batchJobData.getJobArgNm3(), is("3"));
        assertThat(batchJobData.getJobArgNm4(), is("4"));
        assertThat(batchJobData.getJobArgNm5(), is("5"));
        assertThat(batchJobData.getJobArgNm6(), is("6"));
        assertThat(batchJobData.getJobArgNm7(), is("7"));
        assertThat(batchJobData.getJobArgNm8(), is("8"));
        assertThat(batchJobData.getJobArgNm9(), is("9"));
        assertThat(batchJobData.getJobArgNm10(), is("10"));
        assertThat(batchJobData.getJobArgNm11(), is("11"));
        assertThat(batchJobData.getJobArgNm12(), is("12"));
        assertThat(batchJobData.getJobArgNm13(), is("13"));
        assertThat(batchJobData.getJobArgNm14(), is("14"));
        assertThat(batchJobData.getJobArgNm15(), is("15"));
        assertThat(batchJobData.getJobArgNm16(), is("16"));
        assertThat(batchJobData.getJobArgNm17(), is("17"));
        assertThat(batchJobData.getJobArgNm18(), is("18"));
        assertThat(batchJobData.getJobArgNm19(), is("19"));
        assertThat(batchJobData.getJobArgNm20(), is("20"));
        assertThat(batchJobData.getAddDateTime(), is(nullValue()));
        assertThat(batchJobData.getBLogicAppStatus(), is(""));
        assertThat(batchJobData.getCurAppStatus(), is(""));
        assertThat(batchJobData.getJobSequenceId(), is(""));
        assertThat(batchJobData.getUpdDateTime(), is(nullValue()));
    }

    /**
     * convertBatchJobData()のテスト 【正常系】
     * <pre>
     * 事前条件
     * 以下を設定する。
     * ・java実行引数のジョブ業務コード："jobAppCd"
     * ・環境変数のジョブ業務コード："envJobAppCd"
     * ・環境変数のビジネスロジックステータス："blogicAppStatus"
     * ・環境変数の現在のアプリケーションステータス："curAppStatus"
     * ・環境変数のジョブシーケンスID : "jobSequenceId"
     * 確認項目
     * ・{@code BatchJobData}のジョブ業務コードはjava実行引数"jobAppCd"が設定されること。
     * ・{@code BatchJobData}のビジネスロジックステータスは環境変数"blogicAppStatus"が設定されること。
     * ・{@code BatchJobData}の現在のアプリケーションステータスは環境変数"curAppStatus"が設定されること。
     * ・{@code BatchJobData}の現在のジョブシーケンスIDは環境変数"jobSequenceId"が設定されること。
     *  </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testConvertBatchJobData06() throws Exception {
        try {
            SystemEnvUtils.setEnv("JOB_APP_CD", "envJobAppCd");
            SystemEnvUtils.setEnv("BLOGIC_APP_STATUS", "blogicAppStatus");
            SystemEnvUtils.setEnv("CUR_APP_STATUS", "curAppStatus");
            SystemEnvUtils.setEnv("JOB_SEQ_ID", "jobSequenceId");

            // テスト実行
            BatchJobData batchJobData = target.convertBatchJobData(
                    new String[] { "jobAppCd" });
            assertThat(batchJobData.getJobAppCd(), is("jobAppCd"));
            assertThat(batchJobData.getBLogicAppStatus(),
                    is("blogicAppStatus"));
            assertThat(batchJobData.getCurAppStatus(), is("curAppStatus"));
            assertThat(batchJobData.getJobSequenceId(), is("jobSequenceId"));
        } finally {
            SystemEnvUtils.restoreEnv();
        }
    }

    /**
     * convertBatchJobData()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・java起動引数：引数なし
     * ・環境変数：ジョブ業務コード、第1～20引数を指定する。
     * 確認項目
     * ・{@code BatchJobData}のジョブ業務コード、および第1～20引数は
     * 環境変数から設定したものであること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testConvertBatchJobData07() throws Exception {

        BatchJobData batchJobData;
        try {
            SystemEnvUtils.setEnv("JOB_APP_CD", "envJobAppCd");
            SystemEnvUtils.setEnv("JOB_ARG_NM1", "env1");
            SystemEnvUtils.setEnv("JOB_ARG_NM2", "env2");
            SystemEnvUtils.setEnv("JOB_ARG_NM3", "env3");
            SystemEnvUtils.setEnv("JOB_ARG_NM4", "env4");
            SystemEnvUtils.setEnv("JOB_ARG_NM5", "env5");
            SystemEnvUtils.setEnv("JOB_ARG_NM6", "env6");
            SystemEnvUtils.setEnv("JOB_ARG_NM7", "env7");
            SystemEnvUtils.setEnv("JOB_ARG_NM8", "env8");
            SystemEnvUtils.setEnv("JOB_ARG_NM9", "env9");
            SystemEnvUtils.setEnv("JOB_ARG_NM10", "env10");
            SystemEnvUtils.setEnv("JOB_ARG_NM11", "env11");
            SystemEnvUtils.setEnv("JOB_ARG_NM12", "env12");
            SystemEnvUtils.setEnv("JOB_ARG_NM13", "env13");
            SystemEnvUtils.setEnv("JOB_ARG_NM14", "env14");
            SystemEnvUtils.setEnv("JOB_ARG_NM15", "env15");
            SystemEnvUtils.setEnv("JOB_ARG_NM16", "env16");
            SystemEnvUtils.setEnv("JOB_ARG_NM17", "env17");
            SystemEnvUtils.setEnv("JOB_ARG_NM18", "env18");
            SystemEnvUtils.setEnv("JOB_ARG_NM19", "env19");
            SystemEnvUtils.setEnv("JOB_ARG_NM20", "env20");

            // テスト実行
            batchJobData = target.convertBatchJobData(new String[] {});
        } finally {
            SystemEnvUtils.restoreEnv();
        }

        assertThat(batchJobData.getJobAppCd(), is("envJobAppCd"));
        assertThat(batchJobData.getJobArgNm1(), is("env1"));
        assertThat(batchJobData.getJobArgNm2(), is("env2"));
        assertThat(batchJobData.getJobArgNm3(), is("env3"));
        assertThat(batchJobData.getJobArgNm4(), is("env4"));
        assertThat(batchJobData.getJobArgNm5(), is("env5"));
        assertThat(batchJobData.getJobArgNm6(), is("env6"));
        assertThat(batchJobData.getJobArgNm7(), is("env7"));
        assertThat(batchJobData.getJobArgNm8(), is("env8"));
        assertThat(batchJobData.getJobArgNm9(), is("env9"));
        assertThat(batchJobData.getJobArgNm10(), is("env10"));
        assertThat(batchJobData.getJobArgNm11(), is("env11"));
        assertThat(batchJobData.getJobArgNm12(), is("env12"));
        assertThat(batchJobData.getJobArgNm13(), is("env13"));
        assertThat(batchJobData.getJobArgNm14(), is("env14"));
        assertThat(batchJobData.getJobArgNm15(), is("env15"));
        assertThat(batchJobData.getJobArgNm16(), is("env16"));
        assertThat(batchJobData.getJobArgNm17(), is("env17"));
        assertThat(batchJobData.getJobArgNm18(), is("env18"));
        assertThat(batchJobData.getJobArgNm19(), is("env19"));
        assertThat(batchJobData.getJobArgNm20(), is("env20"));
        assertThat(batchJobData.getAddDateTime(), is(nullValue()));
        assertThat(batchJobData.getBLogicAppStatus(), is(""));
        assertThat(batchJobData.getCurAppStatus(), is(""));
        assertThat(batchJobData.getJobSequenceId(), is(""));
        assertThat(batchJobData.getUpdDateTime(), is(nullValue()));
    }

    /**
     * convertBatchJobData()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・java起動引数：ジョブ業務コード、第1～10引数を指定する。
     * ・環境変数：ジョブ業務コード、第1～20引数を指定する。
     * 確認項目
     * ・{@code BatchJobData}のジョブ業務コード、および第1～10引数はjava起動引数
     * 第11～20引数は環境変数から設定したものであること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testConvertBatchJobData08() throws Exception {

        BatchJobData batchJobData;
        try {
            SystemEnvUtils.setEnv("JOB_APP_CD", "envJobAppCd");
            SystemEnvUtils.setEnv("JOB_ARG_NM1", "env1");
            SystemEnvUtils.setEnv("JOB_ARG_NM2", "env2");
            SystemEnvUtils.setEnv("JOB_ARG_NM3", "env3");
            SystemEnvUtils.setEnv("JOB_ARG_NM4", "env4");
            SystemEnvUtils.setEnv("JOB_ARG_NM5", "env5");
            SystemEnvUtils.setEnv("JOB_ARG_NM6", "env6");
            SystemEnvUtils.setEnv("JOB_ARG_NM7", "env7");
            SystemEnvUtils.setEnv("JOB_ARG_NM8", "env8");
            SystemEnvUtils.setEnv("JOB_ARG_NM9", "env9");
            SystemEnvUtils.setEnv("JOB_ARG_NM10", "env10");
            SystemEnvUtils.setEnv("JOB_ARG_NM11", "env11");
            SystemEnvUtils.setEnv("JOB_ARG_NM12", "env12");
            SystemEnvUtils.setEnv("JOB_ARG_NM13", "env13");
            SystemEnvUtils.setEnv("JOB_ARG_NM14", "env14");
            SystemEnvUtils.setEnv("JOB_ARG_NM15", "env15");
            SystemEnvUtils.setEnv("JOB_ARG_NM16", "env16");
            SystemEnvUtils.setEnv("JOB_ARG_NM17", "env17");
            SystemEnvUtils.setEnv("JOB_ARG_NM18", "env18");
            SystemEnvUtils.setEnv("JOB_ARG_NM19", "env19");
            SystemEnvUtils.setEnv("JOB_ARG_NM20", "env20");

            // テスト実行
            batchJobData = target.convertBatchJobData(
                    new String[] { "jobAppCd", "1", "2", "3", "4", "5", "6",
                            "7", "8", "9", "10" });
        } finally {
            SystemEnvUtils.restoreEnv();
        }

        assertThat(batchJobData.getJobAppCd(), is("jobAppCd"));
        assertThat(batchJobData.getJobArgNm1(), is("1"));
        assertThat(batchJobData.getJobArgNm2(), is("2"));
        assertThat(batchJobData.getJobArgNm3(), is("3"));
        assertThat(batchJobData.getJobArgNm4(), is("4"));
        assertThat(batchJobData.getJobArgNm5(), is("5"));
        assertThat(batchJobData.getJobArgNm6(), is("6"));
        assertThat(batchJobData.getJobArgNm7(), is("7"));
        assertThat(batchJobData.getJobArgNm8(), is("8"));
        assertThat(batchJobData.getJobArgNm9(), is("9"));
        assertThat(batchJobData.getJobArgNm10(), is("10"));
        assertThat(batchJobData.getJobArgNm11(), is("env11"));
        assertThat(batchJobData.getJobArgNm12(), is("env12"));
        assertThat(batchJobData.getJobArgNm13(), is("env13"));
        assertThat(batchJobData.getJobArgNm14(), is("env14"));
        assertThat(batchJobData.getJobArgNm15(), is("env15"));
        assertThat(batchJobData.getJobArgNm16(), is("env16"));
        assertThat(batchJobData.getJobArgNm17(), is("env17"));
        assertThat(batchJobData.getJobArgNm18(), is("env18"));
        assertThat(batchJobData.getJobArgNm19(), is("env19"));
        assertThat(batchJobData.getJobArgNm20(), is("env20"));
        assertThat(batchJobData.getAddDateTime(), is(nullValue()));
        assertThat(batchJobData.getBLogicAppStatus(), is(""));
        assertThat(batchJobData.getCurAppStatus(), is(""));
        assertThat(batchJobData.getJobSequenceId(), is(""));
        assertThat(batchJobData.getUpdDateTime(), is(nullValue()));
    }

    /**
     * convertBatchJobData()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・java起動引数：ジョブ業務コード、第1～10引数を指定する。
     * ・環境変数：ジョブ業務コード、第12～19引数を指定する。
     * 確認項目
     * ・{@code BatchJobData}のジョブ業務コード、および第1～10引数はjava起動引数
     * ・{@code BatchJobData}の第11引数はnull
     * ・{@code BatchJobData}の第12～19引数は環境変数から設定した引数
     * ・{@code BatchJobData}の第20引数はnull
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testConvertBatchJobData09() throws Exception {

        BatchJobData batchJobData;
        try {
            SystemEnvUtils.setEnv("JOB_APP_CD", "envJobAppCd");
            SystemEnvUtils.setEnv("JOB_ARG_NM12", "env12");
            SystemEnvUtils.setEnv("JOB_ARG_NM13", "env13");
            SystemEnvUtils.setEnv("JOB_ARG_NM14", "env14");
            SystemEnvUtils.setEnv("JOB_ARG_NM15", "env15");
            SystemEnvUtils.setEnv("JOB_ARG_NM16", "env16");
            SystemEnvUtils.setEnv("JOB_ARG_NM17", "env17");
            SystemEnvUtils.setEnv("JOB_ARG_NM18", "env18");
            SystemEnvUtils.setEnv("JOB_ARG_NM19", "env19");

            // テスト実行
            batchJobData = target.convertBatchJobData(
                    new String[] { "jobAppCd", "1", "2", "3", "4", "5", "6",
                            "7", "8", "9", "10" });
        } finally {
            SystemEnvUtils.restoreEnv();
        }

        assertThat(batchJobData.getJobAppCd(), is("jobAppCd"));
        assertThat(batchJobData.getJobArgNm1(), is("1"));
        assertThat(batchJobData.getJobArgNm2(), is("2"));
        assertThat(batchJobData.getJobArgNm3(), is("3"));
        assertThat(batchJobData.getJobArgNm4(), is("4"));
        assertThat(batchJobData.getJobArgNm5(), is("5"));
        assertThat(batchJobData.getJobArgNm6(), is("6"));
        assertThat(batchJobData.getJobArgNm7(), is("7"));
        assertThat(batchJobData.getJobArgNm8(), is("8"));
        assertThat(batchJobData.getJobArgNm9(), is("9"));
        assertThat(batchJobData.getJobArgNm10(), is("10"));
        assertThat(batchJobData.getJobArgNm11(), is(nullValue()));
        assertThat(batchJobData.getJobArgNm12(), is("env12"));
        assertThat(batchJobData.getJobArgNm13(), is("env13"));
        assertThat(batchJobData.getJobArgNm14(), is("env14"));
        assertThat(batchJobData.getJobArgNm15(), is("env15"));
        assertThat(batchJobData.getJobArgNm16(), is("env16"));
        assertThat(batchJobData.getJobArgNm17(), is("env17"));
        assertThat(batchJobData.getJobArgNm18(), is("env18"));
        assertThat(batchJobData.getJobArgNm19(), is("env19"));
        assertThat(batchJobData.getJobArgNm20(), is(nullValue()));
        assertThat(batchJobData.getAddDateTime(), is(nullValue()));
        assertThat(batchJobData.getBLogicAppStatus(), is(""));
        assertThat(batchJobData.getCurAppStatus(), is(""));
        assertThat(batchJobData.getJobSequenceId(), is(""));
        assertThat(batchJobData.getUpdDateTime(), is(nullValue()));
    }

    /**
     * testGetenv01のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・空文字を渡した場合、空文字が取得できること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetenv01() throws Exception {
        // テスト実行
        String result = target.getenv("");
        assertThat(result, is(""));
    }

    /**
     * testGetenv02のテスト 【正常系】
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetenv02() throws Exception {
        try {
            SystemEnvUtils.setEnv("jobAppCd", "B000001");
            // テスト実行
            String result = target.getenv("jobAppCd");
            assertThat(result, is("B000001"));
        } finally {
            SystemEnvUtils.restoreEnv();
        }
    }
}
