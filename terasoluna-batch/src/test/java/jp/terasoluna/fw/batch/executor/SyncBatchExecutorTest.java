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

package jp.terasoluna.fw.batch.executor;

import jp.terasoluna.fw.batch.executor.controller.JobOperator;
import jp.terasoluna.fw.util.PropertyUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.ReflectionUtils;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import java.lang.reflect.Field;
import java.util.TreeMap;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;
import static uk.org.lidalia.slf4jtest.LoggingEvent.info;

/**
 * {@code SyncBatchExecutor}のテストケース。
 */
public class SyncBatchExecutorTest {

    final SecurityManager sm = System.getSecurityManager();

    private TestLogger logger = TestLoggerFactory
            .getTestLogger(SyncBatchExecutor.class);

    private Object originProps;

    private static Field propsField;

    /***
     * テストケース全体の前処理。<br>
     * テストケース終了後に復元対象となる{@code PropertyUtil}の内部フィールドである
     * propsを退避する。
     */
    @BeforeClass
    public static void setUpClass() {
        propsField = ReflectionUtils.findField(PropertyUtil.class, "props");
        propsField.setAccessible(true);
    }

    /**
     * テスト前処理。<br>
     * System.exit()でテストプロセスを止めないセキュリティマネージャを設定する。
     *
     * @throws Exception 予期しない例外
     */
    @Before
    public void setUp() throws Exception {
        System.setSecurityManager(new SecurityManagerEx());
        // PropertyUtilの内部プロパティを退避
        this.originProps = propsField.get(null);
    }

    /**
     * テスト後処理。<br>
     * セキュリティマネージャを元に戻す。
     *
     * @throws Exception 予期しない例外
     */
    @After
    public void tearDown() throws Exception {
        logger.clear();
        System.setSecurityManager(sm);
        // PropertyUtilの内部プロパティを復元
        propsField.set(null, this.originProps);
    }

    /**
     * mainのテスト 【正常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・doMain()メソッドがコールされ、モックの{@code JobOperator}から
     * 　終了ステータス23でプロセス終了すること。
     * 　（プロセス終了は{@code ExitException}スローにより代用する。
     * 　そのため例外キャッチを行っているが正常系としてテストを行う。）
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testMain() throws Exception {
        try {
            SyncBatchExecutor.main(new String[0]);
            fail();
        } catch (SecurityManagerEx.ExitException e) {
            assertEquals(23, e.state);
        }
    }

    /**
     * doMainのテスト 【正常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code JobOperator#start()}により、ステータスコードが返却されること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testDoMain01() throws Exception {
        JobOperator mockJobOperator = mock(JobOperator.class);
        String[] args = new String[] { "aaa", "bbb", "ccc" };
        doReturn(456).when(mockJobOperator).start(args);

        ApplicationContext mockContext = mock(ApplicationContext.class);
        doReturn(mockJobOperator).when(mockContext)
                .getBean("syncJobOperator", JobOperator.class);

        ApplicationContextResolver mockResolver = mock(
                ApplicationContextResolver.class);
        doReturn(mockContext).when(mockResolver).resolveApplicationContext();

        SyncBatchExecutor target = spy(new SyncBatchExecutor());
        doReturn(mockResolver).when(target).findAdminContextResolver();

        // テスト実行
        assertEquals(456, target.doMain(args));

        assertThat(logger.getLoggingEvents(), is(asList(
                info("[IAL025014] SyncBatchExecutor START"),
                info("[IAL025015] SyncBatchExecutor END blogicStatus:456"))));
    }

    /**
     * doMainのテスト 【異常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code resolveApplicationContext()}メソッドによる{@code ApplicationContext}の生成失敗により
     * {@code BeansException}をスローした場合、
     * {@code doMain()}の戻り値が{@code FAIL_TO_OBTAIN_JOB_OPERATOR_CODE}であること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testDoMain02() throws Exception {
        ApplicationContextResolver mockResolver = mock(
                ApplicationContextResolver.class);
        Exception expectThrown = new BeansException("applicationContext creation error.") {
            private static final long serialVersionUID = 2970034475371975813L;
        };
        doThrow(expectThrown).when(mockResolver).resolveApplicationContext();

        SyncBatchExecutor target = spy(new SyncBatchExecutor());
        doReturn(mockResolver).when(target).findAdminContextResolver();

        // テスト実行
        assertEquals(SyncBatchExecutor.FAIL_TO_OBTAIN_JOB_OPERATOR_CODE,
                target.doMain(new String[] {}));
        assertThat(logger.getLoggingEvents(), is(asList(
                info("[IAL025014] SyncBatchExecutor START"),
                error(expectThrown, "[EAL025060] Failed to obtain JobOperator."))));
    }

    /**
     * doMainのテスト 【異常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code ApplicationContext#getBean()}で例外をスローしたとき、
     * 　コンテキストのクローズ処理が呼び出されること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testDoMain03() throws Exception {
        ApplicationContextResolver mockResolver = mock(
                ApplicationContextResolver.class);
        ApplicationContext mockContext = mock(ApplicationContext.class);

        Exception expectThrown = new BeanCreationException(
                "class-load error.");
        doThrow(expectThrown).when(mockContext).getBean(anyString(), eq(JobOperator.class));
        doReturn(mockContext).when(mockResolver).resolveApplicationContext();

        SyncBatchExecutor target = spy(new SyncBatchExecutor());
        doReturn(mockResolver).when(target).findAdminContextResolver();

        // テスト実行
        assertEquals(SyncBatchExecutor.FAIL_TO_OBTAIN_JOB_OPERATOR_CODE,
                target.doMain(new String[] {}));
        assertThat(logger.getLoggingEvents(), is(asList(
                info("[IAL025014] SyncBatchExecutor START"),
                error(expectThrown, "[EAL025060] Failed to obtain JobOperator."))));

        verify(mockResolver).closeApplicationContext(any(ApplicationContext.class));
    }

    /**
     * doMainのテスト 【異常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code ApplicationContext#getBean()}で例外をスローし、コンテキストの
     * 　クローズ時にも例外をスローした場合、エラーログが出力されること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testDoMain04() throws Exception {
        ApplicationContextResolver mockResolver = mock(
                ApplicationContextResolver.class);
        ApplicationContext mockContext = mock(ApplicationContext.class);

        Exception expectThrown = new BeanCreationException(
                "class-load error.");
        Exception closeThrown = new ApplicationContextException("close error.");

        doThrow(expectThrown).when(mockContext).getBean(anyString(), eq(JobOperator.class));
        doReturn(mockContext).when(mockResolver).resolveApplicationContext();
        doThrow(closeThrown).when(mockResolver).closeApplicationContext(mockContext);

        SyncBatchExecutor target = spy(new SyncBatchExecutor());
        doReturn(mockResolver).when(target).findAdminContextResolver();

        // テスト実行
        assertEquals(SyncBatchExecutor.FAIL_TO_OBTAIN_JOB_OPERATOR_CODE,
                target.doMain(new String[] {}));
        assertThat(logger.getLoggingEvents(), is(asList(
                info("[IAL025014] SyncBatchExecutor START"),
                error(expectThrown, "[EAL025060] Failed to obtain JobOperator."),
                error(closeThrown, "[EAL025062] Failed to close the ApplicationContext."))));
    }

    /**
     * findAdminContextResolverのテスト 【正常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * 　{@code ApplicationContextResolverImpl}インスタンスが返却されること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testFindAdminContextResolver01() throws Exception {
        propsField.set(null, new TreeMap<String, String>());
        SyncBatchExecutor target = new SyncBatchExecutor();

        // テスト実行
        ApplicationContextResolver resolver = target
                .findAdminContextResolver();
        assertThat(resolver, is(instanceOf(ApplicationContextResolverImpl.class)));
    }
}
