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

package jp.terasoluna.fw.batch.executor;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * {@code ConnectionRetryInterceptor}のテストケース。<br>
 * @since 3.6
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beansDef/ConnectionRetryInterceptor.xml")
public class ConnectionRetryInterceptorTest {

    @Resource
    protected ConnectionRetryInterceptor connectionRetryInterceptor;

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            ConnectionRetryInterceptor.class);

    private long maxRetryCount;

    private long retryInterval;

    private long retryReset;

    /**
     * テスト前処理：インスタンスのフィールド初期値を退避。
     */
    @Before
    public void setUp() {
        maxRetryCount = (long) ReflectionTestUtils.getField(
                connectionRetryInterceptor, "maxRetryCount");
        retryInterval = (long) ReflectionTestUtils.getField(
                connectionRetryInterceptor, "retryInterval");
        retryReset = (long) ReflectionTestUtils.getField(
                connectionRetryInterceptor, "retryReset");
    }

    /**
     * テスト後処理：インスタンスのフィールド値を元に戻し、ロガーのクリアを行なう。
     */
    @After
    public void tearDown() {
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "maxRetryCount", maxRetryCount);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "retryInterval", retryInterval);
        ReflectionTestUtils.setField(connectionRetryInterceptor, "retryReset",
                retryReset);
        logger.clear();
    }

    /**
     * invoke()メソッドのテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・例外が発生せずにメソッドが終了すること
     * </pre>
     * 
     * @throws Throwable 予期せぬ例外
     */
    @Test
    public void testInvoke01() throws Throwable {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        when(mockMethodInvocation.proceed()).thenReturn(null);

        // テスト実行
        // 検証
        assertNull(connectionRetryInterceptor.invoke(mockMethodInvocation));

    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・DataAccessExceptionの派生クラス(RecoverableDataAccessException)をスローすること
     * ・最大試行回数  0
     * 確認項目
     * ・DataAccessExceptionの派生クラスがスローされること
     * ・EAL025031のログが出力されること
     * </pre>
     * 
     * @throws Throwable 予期せぬ例外
     */
    @Test
    public void testInvoke02() throws Throwable {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        RecoverableDataAccessException recoverableDataAccessException = new RecoverableDataAccessException(null);

        // テスト実行
        // 検証
        try {
            when(mockMethodInvocation.proceed()).thenThrow(
                    recoverableDataAccessException);
            connectionRetryInterceptor.invoke(mockMethodInvocation);
            fail();
        } catch (DataAccessException e) {
            assertSame(recoverableDataAccessException, e);
        }
        assertEquals(Level.ERROR, logger.getLoggingEvents().get(0).getLevel());
        assertEquals("[EAL025031] AsyncBatchExecutor ABNORMAL END", logger
                .getLoggingEvents().get(0).getMessage());
    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・TransactionExceptionの派生クラス(TransactionSystemException)をスローすること
     * ・最大試行回数  0
     * 確認項目
     * ・TransactionExceptionの派生クラスがスローされること
     * ・EAL025031のログが出力されること
     * </pre>
     * 
     * @throws Throwable 予期せぬ例外
     */
    @Test
    public void testInvoke03() throws Throwable {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        TransactionException transactionSystemException = new TransactionSystemException("test exception");

        // テスト実行
        // 検証
        try {
            when(mockMethodInvocation.proceed()).thenThrow(
                    transactionSystemException);
            connectionRetryInterceptor.invoke(mockMethodInvocation);
            fail();
        } catch (TransactionException e) {
            assertSame(transactionSystemException, e);
        }
        assertEquals(Level.ERROR, logger.getLoggingEvents().get(0).getLevel());
        assertEquals("[EAL025031] AsyncBatchExecutor ABNORMAL END", logger
                .getLoggingEvents().get(0).getMessage());
    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・RetryableExecuteExceptionをスローすること
     * ・最大試行回数  0
     * 確認項目
     * ・causeとして設定したIllegalArgumentExceptionがスローされること
     * ・EAL025031のログが出力されること
     * </pre>
     * 
     * @throws Throwable 予期せぬ例外
     */
    @Test
    public void testInvoke04() throws Throwable {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        Exception cause = new IllegalArgumentException("test exception");

        // テスト実行
        // 検証
        try {
            when(mockMethodInvocation.proceed()).thenThrow(
                    new RetryableExecuteException(cause));
            connectionRetryInterceptor.invoke(mockMethodInvocation);
            fail();
        } catch (IllegalArgumentException e) {
            assertSame(cause, e);
        }
        assertEquals(Level.ERROR, logger.getLoggingEvents().get(0).getLevel());
        assertEquals("[EAL025031] AsyncBatchExecutor ABNORMAL END", logger
                .getLoggingEvents().get(0).getMessage());
    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・最大試行回数内で処理が成功すること
     * ・最大試行回数 3、RecoverableDataAccessException例外スロー回数 2
     * 確認項目
     * ・例外がスローされないこと
     * ・リトライ処理(MethodInvocation#proceed()が3回呼び出されること)
     * ・リトライ2回分のログが出力されること
     * </pre>
     * 
     * @throws Throwable 予期せぬ例外
     */
    @Test
    public void testInvoke05() throws Throwable {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "maxRetryCount", 3);
        when(mockMethodInvocation.proceed()).thenThrow(
                new RecoverableDataAccessException(null)).thenThrow(
                        new RecoverableDataAccessException(null)).thenReturn(
                                null);

        // テスト実行
        // 検証
        assertNull(connectionRetryInterceptor.invoke(mockMethodInvocation));
        verify(mockMethodInvocation, times(3)).proceed();
        assertEquals(Level.INFO, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(0).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(1).getLevel());
        assertTrue(logger.getLoggingEvents().get(1).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.INFO, logger.getLoggingEvents().get(2).getLevel());
        assertEquals(
                "[IAL025017] retry: 2 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(2).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(3).getLevel());
        assertTrue(logger.getLoggingEvents().get(3).getMessage().contains(
                "[TAL025010] Java memory info"));

    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・最大試行回数内で処理が成功すること
     * ・最大試行回数 3、TransactionSystemException例外スロー回数 2
     * 確認項目
     * ・例外がスローされないこと
     * ・リトライ処理(MethodInvocation#proceed()が3回呼び出されること)
     * ・リトライ2回分のログが出力されること
     * </pre>
     * 
     * @throws Throwable 予期せぬ例外
     */
    @Test
    public void testInvoke06() throws Throwable {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "maxRetryCount", 3);
        when(mockMethodInvocation.proceed()).thenThrow(
                new TransactionSystemException("test exception")).thenThrow(
                        new TransactionSystemException("test exception"))
                .thenReturn(null);

        // テスト実行
        // 検証
        assertNull(connectionRetryInterceptor.invoke(mockMethodInvocation));
        verify(mockMethodInvocation, times(3)).proceed();
        assertEquals(Level.INFO, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(0).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(1).getLevel());
        assertTrue(logger.getLoggingEvents().get(1).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.INFO, logger.getLoggingEvents().get(2).getLevel());
        assertEquals(
                "[IAL025017] retry: 2 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(2).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(3).getLevel());
        assertTrue(logger.getLoggingEvents().get(3).getMessage().contains(
                "[TAL025010] Java memory info"));

    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・最大試行回数内で処理が成功すること
     * ・最大試行回数 3、RetryableExecuteException例外スロー回数 2
     * 確認項目
     * ・例外がスローされないこと
     * ・リトライ処理(MethodInvocation#proceed()が3回呼び出されること)
     * ・リトライ3回分、リトライのログが出力されること
     * </pre>
     * 
     * @throws Throwable 予期せぬ例外
     */
    @Test
    public void testInvoke07() throws Throwable {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        Exception cause = new Exception("test exception");
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "maxRetryCount", 3);
        when(mockMethodInvocation.proceed()).thenThrow(
                new RetryableExecuteException(cause)).thenThrow(
                        new RetryableExecuteException(cause)).thenReturn(null);

        // テスト実行
        // 検証
        assertNull(connectionRetryInterceptor.invoke(mockMethodInvocation));
        verify(mockMethodInvocation, times(3)).proceed();
        assertEquals(Level.INFO, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(0).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(1).getLevel());
        assertTrue(logger.getLoggingEvents().get(1).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.INFO, logger.getLoggingEvents().get(2).getLevel());
        assertEquals(
                "[IAL025017] retry: 2 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(2).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(3).getLevel());
        assertTrue(logger.getLoggingEvents().get(3).getMessage().contains(
                "[TAL025010] Java memory info"));

    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・最大試行回数を超えること
     * ・最大試行回数 3、RecoverableDataAccessException例外スロー回数 4
     * 確認項目
     * ・DataAccessException例外の派生クラスがスローされること
     * ・リトライ処理(MethodInvocation#proceed()が4回呼び出されること)
     * ・リトライ3回分、リトライ失敗のログが出力されること
     * </pre>
     * 
     * @throws Throwable 予期せぬ例外
     */
    @Test
    public void testInvoke08() throws Throwable {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        RecoverableDataAccessException recoverableDataAccessException = new RecoverableDataAccessException(null);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "maxRetryCount", 3);

        // テスト実行
        // 検証
        try {
            when(mockMethodInvocation.proceed()).thenThrow(
                    new RecoverableDataAccessException(null)).thenThrow(
                            new RecoverableDataAccessException(null)).thenThrow(
                                    new RecoverableDataAccessException(null))
                    .thenThrow(recoverableDataAccessException).thenReturn(null);
            connectionRetryInterceptor.invoke(mockMethodInvocation);
            fail();
        } catch (DataAccessException e) {
            assertSame(recoverableDataAccessException, e);
        }
        verify(mockMethodInvocation, times(4)).proceed();
        assertEquals(Level.INFO, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(0).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(1).getLevel());
        assertTrue(logger.getLoggingEvents().get(1).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.INFO, logger.getLoggingEvents().get(2).getLevel());
        assertEquals(
                "[IAL025017] retry: 2 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(2).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(3).getLevel());
        assertTrue(logger.getLoggingEvents().get(3).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.INFO, logger.getLoggingEvents().get(4).getLevel());
        assertEquals(
                "[IAL025017] retry: 3 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(4).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(5).getLevel());
        assertTrue(logger.getLoggingEvents().get(5).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.ERROR, logger.getLoggingEvents().get(6).getLevel());
        assertEquals("[EAL025031] AsyncBatchExecutor ABNORMAL END", logger
                .getLoggingEvents().get(6).getMessage());

    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・最大試行回数を超えること
     * ・最大試行回数 3、TransactionSystemException例外スロー回数 4
     * 確認項目
     * ・TransactionException例外の派生クラスがスローされること
     * ・リトライ処理(MethodInvocation#proceed()が4回呼び出されること)
     * ・リトライ3回分のログ、リトライ失敗のログが出力されること
     * </pre>
     * 
     * @throws Throwable 予期せぬ例外
     */
    @Test
    public void testInvoke09() throws Throwable {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        TransactionException transactionException = new TransactionSystemException("test exception");
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "maxRetryCount", 3);

        // テスト実行
        // 検証
        try {
            when(mockMethodInvocation.proceed()).thenThrow(
                    new TransactionSystemException("test exception 1"))
                    .thenThrow(
                            new TransactionSystemException("test exception 2"))
                    .thenThrow(
                            new TransactionSystemException("test exception 3"))
                    .thenThrow(transactionException).thenReturn(null);
            connectionRetryInterceptor.invoke(mockMethodInvocation);
            fail();
        } catch (TransactionException e) {
            assertSame(transactionException, e);
        }
        verify(mockMethodInvocation, times(4)).proceed();
        assertEquals(Level.INFO, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(0).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(1).getLevel());
        assertTrue(logger.getLoggingEvents().get(1).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.INFO, logger.getLoggingEvents().get(2).getLevel());
        assertEquals(
                "[IAL025017] retry: 2 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(2).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(3).getLevel());
        assertTrue(logger.getLoggingEvents().get(3).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.INFO, logger.getLoggingEvents().get(4).getLevel());
        assertEquals(
                "[IAL025017] retry: 3 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(4).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(5).getLevel());
        assertTrue(logger.getLoggingEvents().get(5).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.ERROR, logger.getLoggingEvents().get(6).getLevel());
        assertEquals("[EAL025031] AsyncBatchExecutor ABNORMAL END", logger
                .getLoggingEvents().get(6).getMessage());

    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・最大試行回数を超えること
     * ・最大試行回数 3、RetryableExecuteException例外スロー回数 4
     * 確認項目
     * ・causeとして設定したIllegalArgumentExceptionがスローされること
     * ・リトライ処理(MethodInvocation#proceed()が4回呼び出されること)
     * ・リトライ失敗のログが出力されること
     * </pre>
     * 
     * @throws Throwable 予期せぬ例外
     */
    @Test
    public void testInvoke10() throws Throwable {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        Exception cause = new IllegalArgumentException("test exception");
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "maxRetryCount", 3);

        // テスト実行
        // 検証
        try {
            when(mockMethodInvocation.proceed()).thenThrow(
                    new RetryableExecuteException(cause)).thenThrow(
                            new RetryableExecuteException(cause)).thenThrow(
                                    new RetryableExecuteException(cause))
                    .thenThrow(new RetryableExecuteException(cause)).thenReturn(
                            null);
            connectionRetryInterceptor.invoke(mockMethodInvocation);
            fail();
        } catch (IllegalArgumentException e) {
            assertSame(cause, e);
        }
        verify(mockMethodInvocation, times(4)).proceed();
        assertEquals(Level.INFO, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(0).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(1).getLevel());
        assertTrue(logger.getLoggingEvents().get(1).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.INFO, logger.getLoggingEvents().get(2).getLevel());
        assertEquals(
                "[IAL025017] retry: 2 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(2).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(3).getLevel());
        assertTrue(logger.getLoggingEvents().get(3).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.INFO, logger.getLoggingEvents().get(4).getLevel());
        assertEquals(
                "[IAL025017] retry: 3 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(4).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(5).getLevel());
        assertTrue(logger.getLoggingEvents().get(5).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.ERROR, logger.getLoggingEvents().get(6).getLevel());
        assertEquals("[EAL025031] AsyncBatchExecutor ABNORMAL END", logger
                .getLoggingEvents().get(6).getMessage());

    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・正常終了するまで、最大試行回数を超えてリトライを繰り返す(retryInterval > retryResetなので、リトライ回数が都度リセットされる)こと
     * 確認項目
     * ・例外がスローされないこと
     * ・リトライ処理(MethodInvocation#proceed()が5回呼び出されること)
     * ・リトライ4回分のログが出力されること
     * </pre>
     * 
     * @throws Throwable 予期せぬ例外
     */
    @Test
    public void testInvoke11() throws Throwable {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "maxRetryCount", 3);
        ReflectionTestUtils.setField(connectionRetryInterceptor, "retryReset",
                300);

        // テスト実行
        // 検証
        when(mockMethodInvocation.proceed()).thenThrow(
                new RecoverableDataAccessException(null)).thenThrow(
                        new RecoverableDataAccessException(null)).thenThrow(
                                new RecoverableDataAccessException(null))
                .thenThrow(new RecoverableDataAccessException(null)).thenReturn(
                        null);
        assertNull(connectionRetryInterceptor.invoke(mockMethodInvocation));
        verify(mockMethodInvocation, times(5)).proceed();
        assertEquals(Level.INFO, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 300 ms, retryInterval : 500",
                logger.getLoggingEvents().get(0).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(1).getLevel());
        assertTrue(logger.getLoggingEvents().get(1).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.INFO, logger.getLoggingEvents().get(2).getLevel());
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 300 ms, retryInterval : 500",
                logger.getLoggingEvents().get(2).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(3).getLevel());
        assertTrue(logger.getLoggingEvents().get(3).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.INFO, logger.getLoggingEvents().get(4).getLevel());
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 300 ms, retryInterval : 500",
                logger.getLoggingEvents().get(4).getMessage());
        assertEquals(Level.TRACE, logger.getLoggingEvents().get(5).getLevel());
        assertTrue(logger.getLoggingEvents().get(5).getMessage().contains(
                "[TAL025010] Java memory info"));
        assertEquals(Level.INFO, logger.getLoggingEvents().get(6).getLevel());
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 300 ms, retryInterval : 500",
                logger.getLoggingEvents().get(6).getMessage());
        assertEquals(logger.getLoggingEvents().get(7).getLevel(), Level.TRACE);
        assertTrue(logger.getLoggingEvents().get(7).getMessage().contains(
                "[TAL025010] Java memory info"));

    }

}
