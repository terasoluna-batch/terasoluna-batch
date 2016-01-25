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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;

import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * {@code AdminConnectionRetryInterceptor}のテストケース。<br>
 * @since 3.6
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beansDef/AdminConnectionRetryInterceptor.xml")
public class AdminConnectionRetryInterceptorTest {

    @Resource
    protected AdminConnectionRetryInterceptor adminConnectionRetryInterceptor;

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            AdminConnectionRetryInterceptor.class);

    private long maxRetryCount;

    private long retryInterval;

    private long retryReset;

    /**
     * テスト前処理：インスタンスのフィールド初期値を退避。
     */
    @Before
    public void setUp() {
        maxRetryCount = (long) ReflectionTestUtils.getField(
                adminConnectionRetryInterceptor, "maxRetryCount");
        retryInterval = (long) ReflectionTestUtils.getField(
                adminConnectionRetryInterceptor, "retryInterval");
        retryReset = (long) ReflectionTestUtils.getField(
                adminConnectionRetryInterceptor, "retryReset");
    }

    /**
     * テスト後処理：インスタンスのフィールド値を元に戻し、ロガーのクリアを行なう。
     */
    @After
    public void tearDown() {
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
                "maxRetryCount", maxRetryCount);
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
                "retryInterval", retryInterval);
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
                "retryReset", retryReset);
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
        assertNull(adminConnectionRetryInterceptor.invoke(
                mockMethodInvocation));

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
            adminConnectionRetryInterceptor.invoke(mockMethodInvocation);
            fail();
        } catch (DataAccessException e) {
            assertSame(recoverableDataAccessException, e);
        }
        assertEquals(Level.ERROR, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[EAL025063] Connection retry count exceeded limit. maxRetryCount:0.",
                logger.getLoggingEvents().get(0).getMessage());
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
            adminConnectionRetryInterceptor.invoke(mockMethodInvocation);
            fail();
        } catch (TransactionException e) {
            assertSame(transactionSystemException, e);
        }
        assertEquals(Level.ERROR, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[EAL025063] Connection retry count exceeded limit. maxRetryCount:0.",
                logger.getLoggingEvents().get(0).getMessage());
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
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
                "maxRetryCount", 3);
        when(mockMethodInvocation.proceed()).thenThrow(
                new RecoverableDataAccessException(null)).thenThrow(
                        new RecoverableDataAccessException(null)).thenReturn(
                                null);

        // テスト実行
        // 検証
        assertNull(adminConnectionRetryInterceptor.invoke(
                mockMethodInvocation));
        verify(mockMethodInvocation, times(3)).proceed();
        assertEquals(Level.INFO, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:1, retryMaxCount:3, retryReset:600,000 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(0).getMessage());
        assertEquals(Level.INFO, logger.getLoggingEvents().get(1).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:2, retryMaxCount:3, retryReset:600,000 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(1).getMessage());
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
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
                "maxRetryCount", 3);
        when(mockMethodInvocation.proceed()).thenThrow(
                new TransactionSystemException("test exception")).thenThrow(
                        new TransactionSystemException("test exception"))
                .thenReturn(null);

        // テスト実行
        // 検証
        assertNull(adminConnectionRetryInterceptor.invoke(
                mockMethodInvocation));
        verify(mockMethodInvocation, times(3)).proceed();
        assertEquals(Level.INFO, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:1, retryMaxCount:3, retryReset:600,000 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(0).getMessage());
        assertEquals(Level.INFO, logger.getLoggingEvents().get(1).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:2, retryMaxCount:3, retryReset:600,000 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(1).getMessage());
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
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
                "maxRetryCount", 3);

        // テスト実行
        // 検証
        try {
            when(mockMethodInvocation.proceed()).thenThrow(
                    new RecoverableDataAccessException(null)).thenThrow(
                            new RecoverableDataAccessException(null)).thenThrow(
                                    new RecoverableDataAccessException(null))
                    .thenThrow(recoverableDataAccessException).thenReturn(null);
            adminConnectionRetryInterceptor.invoke(mockMethodInvocation);
            fail();
        } catch (DataAccessException e) {
            assertSame(recoverableDataAccessException, e);
        }
        verify(mockMethodInvocation, times(4)).proceed();
        assertEquals(Level.INFO, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:1, retryMaxCount:3, retryReset:600,000 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(0).getMessage());
        assertEquals(Level.INFO, logger.getLoggingEvents().get(1).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:2, retryMaxCount:3, retryReset:600,000 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(1).getMessage());
        assertEquals(Level.INFO, logger.getLoggingEvents().get(2).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:3, retryMaxCount:3, retryReset:600,000 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(2).getMessage());
        assertEquals(Level.ERROR, logger.getLoggingEvents().get(3).getLevel());
        assertEquals(
                "[EAL025063] Connection retry count exceeded limit. maxRetryCount:3.",
                logger.getLoggingEvents().get(3).getMessage());

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
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
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
            adminConnectionRetryInterceptor.invoke(mockMethodInvocation);
            fail();
        } catch (TransactionException e) {
            assertSame(transactionException, e);
        }
        verify(mockMethodInvocation, times(4)).proceed();
        assertEquals(Level.INFO, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:1, retryMaxCount:3, retryReset:600,000 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(0).getMessage());
        assertEquals(Level.INFO, logger.getLoggingEvents().get(1).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:2, retryMaxCount:3, retryReset:600,000 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(1).getMessage());
        assertEquals(Level.INFO, logger.getLoggingEvents().get(2).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:3, retryMaxCount:3, retryReset:600,000 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(2).getMessage());
        assertEquals(Level.ERROR, logger.getLoggingEvents().get(3).getLevel());
        assertEquals(
                "[EAL025063] Connection retry count exceeded limit. maxRetryCount:3.",
                logger.getLoggingEvents().get(3).getMessage());

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
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
                "maxRetryCount", 3);
        ReflectionTestUtils.setField(adminConnectionRetryInterceptor,
                "retryReset", 300);

        // テスト実行
        // 検証
        when(mockMethodInvocation.proceed()).thenThrow(
                new RecoverableDataAccessException(null)).thenThrow(
                        new RecoverableDataAccessException(null)).thenThrow(
                                new RecoverableDataAccessException(null))
                .thenThrow(new RecoverableDataAccessException(null)).thenReturn(
                        null);
        assertNull(adminConnectionRetryInterceptor.invoke(
                mockMethodInvocation));
        verify(mockMethodInvocation, times(5)).proceed();
        assertEquals(Level.INFO, logger.getLoggingEvents().get(0).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:1, retryMaxCount:3, retryReset:300 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(0).getMessage());
        assertEquals(Level.INFO, logger.getLoggingEvents().get(1).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:1, retryMaxCount:3, retryReset:300 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(1).getMessage());
        assertEquals(Level.INFO, logger.getLoggingEvents().get(2).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:1, retryMaxCount:3, retryReset:300 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(2).getMessage());
        assertEquals(Level.INFO, logger.getLoggingEvents().get(3).getLevel());
        assertEquals(
                "[IAL025017] RetryDetails. currentRetryCount:1, retryMaxCount:3, retryReset:300 ms, retryInterval:500 ms",
                logger.getLoggingEvents().get(3).getMessage());

    }

}
