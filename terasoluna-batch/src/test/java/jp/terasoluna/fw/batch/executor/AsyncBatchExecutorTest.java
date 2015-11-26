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

import jp.terasoluna.fw.batch.exception.IllegalClassTypeException;
import jp.terasoluna.fw.batch.executor.controller.JobOperator;
import jp.terasoluna.fw.util.PropertyUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.context.ApplicationContext;
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

/**
 * {@code AsyncBatchExecutor}のテストケース。
 */
public class AsyncBatchExecutorTest {

    final SecurityManager sm = System.getSecurityManager();

    private TestLogger logger = TestLoggerFactory
            .getTestLogger(AsyncBatchExecutor.class);

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
            AsyncBatchExecutor.main(new String[0]);
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
        doReturn(28194).when(mockJobOperator).start(args);

        ApplicationContext mockContext = mock(ApplicationContext.class);
        doReturn(mockJobOperator).when(mockContext)
                .getBean("jobOperator", JobOperator.class);

        AdminContextResolver mockResolver = mock(
                AdminContextResolver.class);
        doReturn(mockContext).when(mockResolver).resolveAdminContext();

        AsyncBatchExecutor target = spy(new AsyncBatchExecutor());
        doReturn(mockResolver).when(target).findAdminContextResolver();

        // テスト実行
        assertEquals(28194, target.doMain(args));
    }

    /**
     * doMainのテスト 【異常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code findAdminContextResolver()}メソッドによるリゾルバのクラスロード
     * 　失敗により{@code IllegalClassTypeException}をスローした場合、
     * 　{@code doMain()}の戻り値が{@code FAIL_TO_OBTAIN_JOB_OPERATOR_CODE}であること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testDoMain02() throws Exception {
        AdminContextResolver mockResolver = mock(
                AdminContextResolver.class);
        Exception expectThrown = new IllegalClassTypeException(
                "class-load error.");
        doThrow(expectThrown).when(mockResolver).resolveAdminContext();

        AsyncBatchExecutor target = spy(new AsyncBatchExecutor());
        doReturn(mockResolver).when(target).findAdminContextResolver();

        // テスト実行
        assertEquals(AsyncBatchExecutor.FAIL_TO_OBTAIN_JOB_OPERATOR_CODE,
                target.doMain(new String[] {}));
        assertThat(logger.getLoggingEvents(), is(asList(error(expectThrown,
                "[EAL025093] Fail to obtain JobOperator."))));
    }

    /**
     * findAdminContextResolverのテスト 【正常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code PropertyUtil}経由で取得する{@code AdminApplicationContextResolver}
     * 　の実装クラス名が取得できない場合、{@code DefaultAdminContextResolver}クラスの
     * 　インスタンスが返却されること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testFindAdminContextResolver01() throws Exception {
        propsField.set(null, new TreeMap<String, String>());
        AsyncBatchExecutor target = new AsyncBatchExecutor();

        // テスト実行
        AdminContextResolver resolver = target
                .findAdminContextResolver();
        assertThat(resolver, is(instanceOf(DefaultAdminContextResolver.class)));
    }

    /**
     * findAdminContextResolverのテスト 【異常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・リゾルバクラスとして存在しないFQCNをプロパティのキーとして指定したとき、
     * {@code IllegalClassTypeException}がスローされること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testFindAdminContextResolver02() throws Exception {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("adminContextResolver.class", "undefined");
        propsField.set(null, map);
        AsyncBatchExecutor target = new AsyncBatchExecutor();

        // テスト実行
        try {
            target.findAdminContextResolver();
            fail();
        } catch (IllegalClassTypeException e) {
            assertTrue(e.getCause() instanceof ClassNotFoundException);
        }
    }

    /**
     * findAdminContextResolverのテスト 【異常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・リゾルバとしてインスタンス化できないクラスのFQCNをプロパティのキーとして指定したとき、
     * {@code IllegalClassTypeException}がスローされること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testFindAdminContextResolver03() throws Exception {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("adminContextResolver.class", "jp.terasoluna.fw.batch.executor.InstantiateFailClass");
        propsField.set(null, map);
        AsyncBatchExecutor target = new AsyncBatchExecutor();

        // テスト実行
        try {
            target.findAdminContextResolver();
            fail();
        } catch (IllegalClassTypeException e) {
            assertTrue(e.getCause() instanceof BeanInstantiationException);
        }
    }
}
