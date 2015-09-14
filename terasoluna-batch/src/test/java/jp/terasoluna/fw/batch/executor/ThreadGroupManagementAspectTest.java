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
 *
 */

package jp.terasoluna.fw.batch.executor;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.worker.BLogicExecutor;
import jp.terasoluna.fw.batch.message.MessageAccessor;
import jp.terasoluna.fw.batch.util.MessageUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static uk.org.lidalia.slf4jtest.LoggingEvent.debug;

/**
 * ThreadGroupManagementAspectのテストケース。
 * @since 3.6
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beansDef/ThreadGroupManagementAspectTest.xml")
public class ThreadGroupManagementAspectTest {

    @Autowired
    private BLogicExecutor blogicExecutor;

    private static final TestLogger logger = TestLoggerFactory.getTestLogger(
            MessageUtil.class);

    /**
     * ログキャプチャのバッファクリア.
     */
    @After
    public void tearDown() {
        logger.clear();
    }

    /**
     * コンストラクタのテスト 【正常系】 事前条件 ・特になし 確認項目 ・コンストラクタ引数の{@code MessageAccessor}がフィールド上に退避されていること。
     * @throws Throwable 予期しない例外
     */
    @Test
    public void testThreadGroupManagementAspect() throws Exception {
        MessageAccessor messageAccessor = mock(MessageAccessor.class);

        // テスト実行
        ThreadGroupManagementAspect target = new ThreadGroupManagementAspect(messageAccessor);

        assertSame(messageAccessor, target.messageAccessor);
    }

    /**
     * aroundExecuteのテスト 【正常系】 事前条件 ・特になし 確認項目 ・ジョインポイントのメソッドの引数が0個であるとき、{@code ThreadGroupApplicationContextHolder}に
     * 業務用ApplicationContextが設定されないこと。
     * @throws Throwable 予期しない例外
     */
    @Test
    public void testAroundExecute01() throws Throwable {
        final MessageAccessor messageAccessor = mock(MessageAccessor.class);
        final BLogicResult result = new BLogicResult();
        doReturn("test message").when(messageAccessor).getMessage("code", null);

        ThreadGroupManagementAspect target = new ThreadGroupManagementAspect(messageAccessor);
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        doReturn(new Object[0]).when(pjp).getArgs();
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                assertEquals("test message", MessageUtil.getMessage("code"));
                assertNull(ThreadGroupApplicationContextHolder
                        .getCurrentThreadGroupApplicationContext());
                return result;
            }
        }).when(pjp).proceed();

        // テスト実行
        assertSame(result, target.aroundExecute(pjp));
        assertEquals("Message not found. CODE:[code]", MessageUtil.getMessage(
                "code"));
        assertTrue(logger.getLoggingEvents().contains(debug(
                "[DAL025043] MessageAccessor is not found.")));
    }

    /**
     * aroundExecuteのテスト 【正常系】 事前条件 ・特になし 確認項目 ・ジョインポイントのメソッドの引数が1個で、{@code ApplicationContext}ではないとき、
     * 業務用ApplicationContextが設定されないこと。
     * @throws Throwable 予期しない例外
     */
    @Test
    public void testAroundExecute02() throws Throwable {
        final MessageAccessor messageAccessor = mock(MessageAccessor.class);
        final BLogicResult result = new BLogicResult();
        doReturn("test message").when(messageAccessor).getMessage("code", null);

        ThreadGroupManagementAspect target = new ThreadGroupManagementAspect(messageAccessor);
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        doReturn(new Object[] { new Object() }).when(pjp).getArgs();
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                assertEquals("test message", MessageUtil.getMessage("code"));
                assertNull(ThreadGroupApplicationContextHolder
                        .getCurrentThreadGroupApplicationContext());
                return result;
            }
        }).when(pjp).proceed();

        // テスト実行
        assertSame(result, target.aroundExecute(pjp));
        assertEquals("Message not found. CODE:[code]", MessageUtil.getMessage(
                "code"));
        assertTrue(logger.getLoggingEvents().contains(debug(
                "[DAL025043] MessageAccessor is not found.")));
    }

    /**
     * aroundExecuteのテスト 【正常系】 事前条件 ・特になし 確認項目 ・ジョインポイントのメソッドの引数が1個で、{@code ApplicationContext}であるとき、
     * {@code MessageUtil#getMessage()}で{@code MessageAccessor}が用いられ、
     * {@code ThreadGroupApplicationContextHolder#getApplicationContext()}で 同一の{@code ApplicationContext}
     * が取得でき、テスト対象メソッド終了後にクリアされていること。
     * @throws Throwable 予期しない例外
     */
    @Test
    public void testAroundExecute03() throws Throwable {
        final MessageAccessor messageAccessor = mock(MessageAccessor.class);
        final BLogicResult result = new BLogicResult();
        final ApplicationContext businessContext = mock(
                ApplicationContext.class);
        doReturn("test message").when(messageAccessor).getMessage("code", null);

        ThreadGroupManagementAspect target = new ThreadGroupManagementAspect(messageAccessor);
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        doReturn(new Object[] { businessContext }).when(pjp).getArgs();
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                assertEquals("test message", MessageUtil.getMessage("code"));
                assertSame(businessContext, ThreadGroupApplicationContextHolder
                        .getCurrentThreadGroupApplicationContext());
                return result;
            }
        }).when(pjp).proceed();

        // テスト実行
        assertSame(result, target.aroundExecute(pjp));
        assertNull(ThreadGroupApplicationContextHolder
                .getCurrentThreadGroupApplicationContext());
        assertEquals("Message not found. CODE:[code]", MessageUtil.getMessage(
                "code"));
        assertTrue(logger.getLoggingEvents().contains(debug(
                "[DAL025043] MessageAccessor is not found.")));
    }

    /**
     * aroundExecuteのテスト 【正常系】 事前条件 ・Bean定義ファイルにテスト対象クラスがアドバイスとして宣言された{@code BLogicExecutor}が存在すること。 確認項目 ・
     * {@code BLogicExecutor}実行時に{@code ThreadGroupManagementAspect}によるアドバイスが作用していること。
     * @throws Throwable 予期しない例外
     */
    @Test
    public void testAroundExecute04() throws Throwable {
        ApplicationContext businessContext = mock(ApplicationContext.class);
        BLogic blogic = mock(BLogic.class);
        ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        // テスト実行
        BLogicResult result = blogicExecutor.execute(businessContext, blogic,
                new BLogicParam(), exceptionHandler);

        // MockBLogicExecutorの実施確認
        assertEquals(100, result.getBlogicStatus());

        assertNull(ThreadGroupApplicationContextHolder
                .getCurrentThreadGroupApplicationContext());
        assertEquals("Message not found. CODE:[code]", MessageUtil.getMessage(
                "code"));
        assertTrue(logger.getLoggingEvents().contains(debug(
                "[DAL025043] MessageAccessor is not found.")));
        // アドバイス作用時のアサーションはMockBLogicExecutor内で実施。
    }

    /**
     * aroundExecuteのテスト 【異常系】 事前条件 ・特になし 確認項目 ・ジョインポイントから例外がスローされた場合でも、{@code MessageUtil}からの {@code MessageAccessor}と、
     * {@code ThreadGroupApplicationContextHolder} からの業務{@code ApplicationContext}のクリアが行われていること。
     * @throws Throwable 予期しない例外
     */
    @Test
    public void testAroundExecute05() throws Throwable {
        final MessageAccessor messageAccessor = mock(MessageAccessor.class);
        final ApplicationContext businessContext = mock(
                ApplicationContext.class);
        doReturn("test message").when(messageAccessor).getMessage("code", null);

        ThreadGroupManagementAspect target = new ThreadGroupManagementAspect(messageAccessor);
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        doReturn(new Object[] { businessContext }).when(pjp).getArgs();
        final IllegalStateException e = new IllegalStateException("exception throwing test.");
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                assertEquals("test message", MessageUtil.getMessage("code"));
                assertSame(businessContext, ThreadGroupApplicationContextHolder
                        .getCurrentThreadGroupApplicationContext());
                throw e;
            }
        }).when(pjp).proceed();

        try {
            // テスト実行
            target.aroundExecute(pjp);
            fail();
        } catch (IllegalStateException ise) {
            assertSame(e, ise);
        }
        assertNull(ThreadGroupApplicationContextHolder
                .getCurrentThreadGroupApplicationContext());
        assertEquals("Message not found. CODE:[code]", MessageUtil.getMessage(
                "code"));
        assertTrue(logger.getLoggingEvents().contains(debug(
                "[DAL025043] MessageAccessor is not found.")));
    }
}
