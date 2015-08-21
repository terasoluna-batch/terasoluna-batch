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
import org.apache.ibatis.transaction.TransactionException;
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

/**
 * {@code ConnectionRetryInterceptor}のテストケース。<br>
 * @since 3.6
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beansDef/ConnectionRetryInterceptor.xml")
public class ConnectionRetryInterceptorTest {

    @Resource
    protected ConnectionRetryInterceptor connectionRetryInterceptor;

    private TestLogger logger = TestLoggerFactory
            .getTestLogger(ConnectionRetryInterceptor.class);

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
     */
    @Test
    public void testInvoke01() {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);

        // テスト実行
        // 検証
        try {
            when(mockMethodInvocation.proceed()).thenReturn(null);
            assertNull(connectionRetryInterceptor.invoke(mockMethodInvocation));
        } catch (Throwable e) {
            fail();
        }

    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・DataAccessExceptionの派生クラス(RecoverableDataAccessException)をスローすること
     * 確認項目
     * ・DataAccessExceptionの派生クラスがスローされること
     * </pre>
     */
    @Test
    public void testInvoke02() {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);

        // テスト実行
        // 検証
        try {
            when(mockMethodInvocation.proceed()).thenThrow(
                    new RecoverableDataAccessException(null));
            connectionRetryInterceptor.invoke(mockMethodInvocation);
            fail();
        } catch (Throwable e) {
            assertTrue(e instanceof DataAccessException);
        }

    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・RetryableExecuteExceptionをスローすること
     * 確認項目
     * ・causeとして設定したRetryableExecuteExceptionがスローされること
     * </pre>
     */
    @Test
    public void testInvoke03() {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        RetryableExecuteException cause = new RetryableExecuteException(new Exception("test exception"));

        // テスト実行
        // 検証
        try {
            when(mockMethodInvocation.proceed()).thenThrow(
                    new RetryableExecuteException(cause));
            connectionRetryInterceptor.invoke(mockMethodInvocation);
            fail();
        } catch (Throwable e) {
            assertSame((RetryableExecuteException) e, cause);
        }

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
     * ・リトライのログが出力されること
     * </pre>
     */
    @Test
    public void testInvoke04() {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "maxRetryCount", 3);

        // テスト実行
        // 検証
        try {
            when(mockMethodInvocation.proceed()).thenThrow(
                    new RecoverableDataAccessException(null)).thenThrow(
                    new RecoverableDataAccessException(null)).thenReturn(null);
            assertNull(connectionRetryInterceptor.invoke(mockMethodInvocation));
        } catch (Throwable e) {
            fail();
        }
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(0).getMessage());
        assertTrue(logger.getLoggingEvents().get(0).getLevel() == Level.INFO);
        assertTrue(logger.getLoggingEvents().get(1).getMessage().contains("[TAL025010] Java memory info"));
        assertTrue(logger.getLoggingEvents().get(1).getLevel() == Level.TRACE);
    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・最大試行回数内で処理が成功すること
     * ・最大試行回数 3、TransactionException例外スロー回数 2
     * 確認項目
     * ・例外がスローされないこと
     * ・リトライのログが出力されること
     * </pre>
     */
    @Test
    public void testInvoke05() {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "maxRetryCount", 3);

        // テスト実行
        // 検証
        try {
            when(mockMethodInvocation.proceed()).thenThrow(
                    new TransactionException("test exception")).thenThrow(
                    new TransactionException("test exception"))
                    .thenReturn(null);
            assertNull(connectionRetryInterceptor.invoke(mockMethodInvocation));
        } catch (Throwable e) {
            fail();
        }
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(0).getMessage());
        assertTrue(logger.getLoggingEvents().get(0).getLevel() == Level.INFO);
        assertTrue(logger.getLoggingEvents().get(1).getMessage().contains("[TAL025010] Java memory info"));
        assertTrue(logger.getLoggingEvents().get(1).getLevel() == Level.TRACE);

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
     * ・リトライのログが出力されること
     * </pre>
     * @throws Throwable
     */
    @Test
    public void testInvoke06() {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
        RetryableExecuteException cause = new RetryableExecuteException(new Exception("test exception"));
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "retryInterval", 500);
        ReflectionTestUtils.setField(connectionRetryInterceptor,
                "maxRetryCount", 3);

        // テスト実行
        // 検証
        try {
            when(mockMethodInvocation.proceed()).thenThrow(
                    new RetryableExecuteException(cause)).thenThrow(
                    new RetryableExecuteException(cause)).thenReturn(null);
            assertNull(connectionRetryInterceptor.invoke(mockMethodInvocation));
        } catch (Throwable e) {
            fail();
        }
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 600,000 ms, retryInterval : 500",
                logger.getLoggingEvents().get(0).getMessage());
        assertTrue(logger.getLoggingEvents().get(0).getLevel() == Level.INFO);
        assertTrue(logger.getLoggingEvents().get(1).getMessage().contains("[TAL025010] Java memory info"));
        assertTrue(logger.getLoggingEvents().get(1).getLevel() == Level.TRACE);

    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・最大試行回数を超えること
     * ・最大試行回数 3、RecoverableDataAccessException例外スロー回数 4
     * 確認項目
     * ・DataAccessException例外がスローされる
     * ・リトライ失敗のログが出力されること
     * </pre>
     */
    @Test
    public void testInvoke07() {
        // テスト準備
        MethodInvocation mockMethodInvocation = mock(MethodInvocation.class);
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
                    new RecoverableDataAccessException(null)).thenThrow(
                    new RecoverableDataAccessException(null)).thenReturn(null);
            connectionRetryInterceptor.invoke(mockMethodInvocation);
        } catch (Throwable e) {
            assertTrue(e instanceof DataAccessException);
        }
        assertEquals("[EAL025031] AsyncBatchExecutor ABNORMAL END", logger
                .getLoggingEvents().get(logger.getLoggingEvents().size() - 1)
                .getMessage());
        assertTrue(logger.getLoggingEvents().get(0).getLevel() == Level.INFO);
        assertTrue(logger.getLoggingEvents().get(1).getMessage().contains("[TAL025010] Java memory info"));
        assertTrue(logger.getLoggingEvents().get(1).getLevel() == Level.TRACE);

    }

    /**
     * invoke()メソッドのテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・最大試行回数を超えること
     * ・retryInterval > maxRetryCountなので、無限にリトライすること
     * 確認項目
     * ・例外がスローされないこと
     * ・リトライのログが出力されること
     * </pre>
     */
    @Test
    public void testInvoke08() {
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
        try {
            when(mockMethodInvocation.proceed()).thenThrow(
                    new RecoverableDataAccessException(null)).thenThrow(
                    new RecoverableDataAccessException(null)).thenThrow(
                    new RecoverableDataAccessException(null)).thenThrow(
                    new RecoverableDataAccessException(null)).thenReturn(null);
            connectionRetryInterceptor.invoke(mockMethodInvocation);
            assertNull(connectionRetryInterceptor.invoke(mockMethodInvocation));
        } catch (Throwable e) {
            fail();
        }
        assertEquals(
                "[IAL025017] retry: 1 ms, retryMax : 3 ms, retryReset : 300 ms, retryInterval : 500",
                logger.getLoggingEvents().get(0).getMessage());
        assertTrue(logger.getLoggingEvents().get(0).getLevel() == Level.INFO);
        assertTrue(logger.getLoggingEvents().get(1).getMessage().contains("[TAL025010] Java memory info"));
        assertTrue(logger.getLoggingEvents().get(1).getLevel() == Level.TRACE);

    }

}
