package jp.terasoluna.fw.batch.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import jp.terasoluna.fw.batch.annotation.util.MockApplicationContext;
import jp.terasoluna.fw.batch.executor.AsyncBatchExecutor.BatchServantTaskEndTracker;
import jp.terasoluna.fw.batch.executor.SecurityManagerEx.ExitException;
import jp.terasoluna.fw.batch.executor.concurrent.BatchServant;
import jp.terasoluna.fw.batch.executor.concurrent.BatchThreadPoolTaskExecutor;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;
import jp.terasoluna.fw.ex.unit.util.SystemEnvUtils;
import jp.terasoluna.fw.ex.unit.util.TerasolunaPropertyUtils;

import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import static org.junit.Assert.*;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;
import static uk.org.lidalia.slf4jtest.LoggingEvent.info;

public class AsyncBatchExecutorTest {

    final SecurityManager sm = System.getSecurityManager();

    private TestLogger abeLogger = TestLoggerFactory
            .getTestLogger(AsyncBatchExecutor.class);

    private TestLogger ajbeLogger = TestLoggerFactory
            .getTestLogger(AbstractJobBatchExecutor.class);

    public AsyncBatchExecutorTest() {
        super();
    }

    @Before
    public void setUp() throws Exception {
        TerasolunaPropertyUtils.saveProperties();
        System.setSecurityManager(new SecurityManagerEx());
    }

    @After
    public void tearDown() throws Exception {
        TerasolunaPropertyUtils.restoreProperties();
        SystemEnvUtils.restoreEnv();
        System.setSecurityManager(sm);
        abeLogger.clear();
        ajbeLogger.clear();
    }

    /**
     * mainテスト01 【異常系】
     * <pre>
     * 事前条件
     * ・admin.dataSourceのbean定義ファイルから取得するsystemDaoがnull
     * ・起動引数がfoo
     * 確認項目
     * ・EAL025052のログが出力されること
     * ・IAL025018のログが出力されること
     * ・IAL025006のログにfooが出力されること
     * ・リターンコードが255
     * </pre>
     */
    @Test
    public void testMain01() throws Exception {
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.dataSource",
                "AsyncBatchExecutorTest01.xml");
        // TerasolunaPropertyUtils.addProperty("executor.endMonitoringFile",
        // "src/test/resources/test_terminate");

        try {
            AsyncBatchExecutor.main(new String[] { "foo" });
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertTrue(ajbeLogger.getLoggingEvents()
                    .contains(error("[EAL025052] System DAO is null.")));
            assertTrue(abeLogger.getLoggingEvents()
                    .contains(info("[IAL025018] System DAO is null.")));
            assertTrue(abeLogger.getLoggingEvents()
                    .contains(info("[IAL025006] jobAppCd:[foo]")));
            assertEquals(255, e.state);
        }
    }

    /**
     * mainテスト03 【異常系】
     * <pre>
     * 事前条件
     * ・admin.dataSourceのbean定義ファイルから取得するtransactionManagerがnull
     * ・起動引数がfoo
     * 確認項目
     * ・EAL025022のログが出力されること
     * ・IAL025016のログが出力されること
     * ・IAL025006のログにfooが出力されること
     * ・リターンコードが255
     * </pre>
     */
    @Test
    public void testMain03() throws Exception {
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.dataSource",
                "AsyncBatchExecutorTest03.xml");
        // TerasolunaPropertyUtils.addProperty("executor.endMonitoringFile",
        // "src/test/resources/test_terminate");

        try {
            AsyncBatchExecutor.main(new String[] { "foo" });
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertTrue(ajbeLogger.getLoggingEvents().contains(
                    error("[EAL025022] transactionManager is null.")));
            assertTrue(abeLogger.getLoggingEvents().contains(
                    info("[IAL025016] PlatformTransactionManager is null.")));
            assertTrue(abeLogger.getLoggingEvents()
                    .contains(info("[IAL025006] jobAppCd:[foo]")));
            assertEquals(255, e.state);
        }
    }

    /**
     * mainテスト04 【正常系】
     * <pre>
     * 事前条件
     * ・起動引数が空
     * ・環境変数JOB_APP_CDが未設定
     * 確認項目
     * ・IAL025006のログにjobAppCdが空文字として出力されること。
     * </pre>
     */
    @Test
    public void testMain04() throws Exception {
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.dataSource",
                "AsyncBatchExecutorTest01.xml");
        // TerasolunaPropertyUtils.addProperty("executor.endMonitoringFile",
        // "src/test/resources/test_terminate");
        SystemEnvUtils.removeEnv(AsyncBatchExecutor.ENV_JOB_APP_CD);
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertTrue(abeLogger.getLoggingEvents()
                    .contains(info("[IAL025006] jobAppCd:[]")));
            assertEquals(255, e.state);
        }
    }

    /**
     * mainテスト05 【正常系】
     * <pre>
     * 事前条件
     * ・起動引数が空
     * ・環境変数JOB_APP_CDがhogeが設定
     * 確認項目
     * ・IAL025006のログにhogeが出力されること
     * </pre>
     */
    @Test
    public void testMain05() throws Exception {
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.dataSource",
                "AsyncBatchExecutorTest01.xml");
        // TerasolunaPropertyUtils.addProperty("executor.endMonitoringFile",
        // "src/test/resources/test_terminate");
        SystemEnvUtils.setEnv(AsyncBatchExecutor.ENV_JOB_APP_CD, "hoge");
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertTrue(abeLogger.getLoggingEvents()
                    .contains(info("[IAL025006] jobAppCd:[hoge]")));
            assertEquals(255, e.state);
        }
    }

    /**
     * mainテスト06 【正常系】
     * <pre>
     * 事前条件
     * ・『データベース異常時のリトライ回数』に&quot;abc&quot;が設定
     * 確認項目
     * ・リターンコードが255
     * </pre>
     */
    @Test
    public void testMain06() throws Exception {
        TerasolunaPropertyUtils
                .removeProperty("batchTaskExecutor.dbAbnormalRetryMax");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.dbAbnormalRetryMax", "abc");

        SystemEnvUtils.setEnv(AsyncBatchExecutor.ENV_JOB_APP_CD, "hoge");
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertEquals(255, e.state);
        }
    }

    /**
     * mainテスト07 【正常系】
     * <pre>
     * 事前条件
     * ・『データベース異常時のリトライ間隔（ミリ秒）』に&quot;abc&quot;が設定
     * 確認項目
     * ・リターンコードが255
     * </pre>
     */
    @Test
    public void testMain07() throws Exception {
        TerasolunaPropertyUtils
                .removeProperty("batchTaskExecutor.dbAbnormalRetryInterval");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.dbAbnormalRetryInterval",
                        "abc");

        SystemEnvUtils.setEnv(AsyncBatchExecutor.ENV_JOB_APP_CD, "hoge");
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertEquals(255, e.state);
        }
    }

    /**
     * mainテスト08 【正常系】
     * <pre>
     * 事前条件
     * ・『データベース異常時のリトライ回数をリセットする前回からの発生間隔（ミリ秒）』に&quot;abc&quot;が設定
     * 確認項目
     * ・リターンコードが255
     * </pre>
     */
    @Test
    public void testMain08() throws Exception {
        TerasolunaPropertyUtils
                .removeProperty("batchTaskExecutor.dbAbnormalRetryReset");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.dbAbnormalRetryReset", "abc");

        SystemEnvUtils.setEnv(AsyncBatchExecutor.ENV_JOB_APP_CD, "hoge");
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertEquals(255, e.state);
        }
    }

    /**
     * mainテスト09 【正常系】
     * <pre>
     * 事前条件
     * ・『ジョブ実行リトライ間隔（ミリ秒）』に&quot;abc&quot;が設定
     * 確認項目
     * ・リターンコードが255
     * </pre>
     */
    @Test
    public void testMain09() throws Exception {
        TerasolunaPropertyUtils
                .removeProperty("batchTaskExecutor.executeRetryInterval");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.executeRetryInterval", "abc");

        SystemEnvUtils.setEnv(AsyncBatchExecutor.ENV_JOB_APP_CD, "hoge");
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertEquals(255, e.state);
        }
    }

    /**
     * mainテスト10 【正常系】
     * <pre>
     * 事前条件
     * ・『ジョブ実行リトライ回数』に&quot;abc&quot;が設定
     * 確認項目
     * ・リターンコードが255
     * </pre>
     */
    @Test
    public void testMain10() throws Exception {
        TerasolunaPropertyUtils
                .removeProperty("batchTaskExecutor.executeRetryCountMax");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.executeRetryCountMax", "abc");

        SystemEnvUtils.setEnv(AsyncBatchExecutor.ENV_JOB_APP_CD, "hoge");
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertEquals(255, e.state);
        }
    }

    /**
     * mainテスト11 【正常系】
     * <pre>
     * 事前条件
     * ・『空きスレッド残数閾値のデフォルト値』に&quot;abc&quot;が設定
     * 確認項目
     * ・リターンコードが255
     * </pre>
     */
    @Test
    public void testMain11() throws Exception {
        TerasolunaPropertyUtils.removeProperty(
                "batchTaskExecutor.availableThreadThresholdCount");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.availableThreadThresholdCount",
                        "abc");

        SystemEnvUtils.setEnv(AsyncBatchExecutor.ENV_JOB_APP_CD, "hoge");
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertEquals(255, e.state);
        }
    }

    /**
     * mainテスト12 【正常系】
     * <pre>
     * 事前条件
     * ・『空きスレッド残数閾値以下の場合のウェイト時間（ミリ秒）のデフォルト値』に&quot;abc&quot;が設定
     * 確認項目
     * ・リターンコードが255
     * </pre>
     */
    @Test
    public void testMain12() throws Exception {
        TerasolunaPropertyUtils.removeProperty(
                "batchTaskExecutor.availableThreadThresholdWait");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.availableThreadThresholdWait",
                        "abc");

        SystemEnvUtils.setEnv(AsyncBatchExecutor.ENV_JOB_APP_CD, "hoge");
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertEquals(255, e.state);
        }
    }

    /**
     * mainテスト13 【正常系】
     * <pre>
     * 事前条件
     * ・『ジョブ実行リトライ間隔（ミリ秒）』に&quot;10&quot;が設定
     * 確認項目
     * ・リターンコードが0
     * </pre>
     */
    @Test
    public void testMain13() throws Exception {
        // テストデータ設定
        URL srcUrl = this.getClass().getResource("/testdata/test01.txt");
        String endFilePath = srcUrl.getPath();

        TerasolunaPropertyUtils
                .removeProperty("batchTaskExecutor.executeRetryInterval");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.executeRetryInterval", "10");
        TerasolunaPropertyUtils.removeProperty("executor.endMonitoringFile");
        TerasolunaPropertyUtils
                .addProperty("executor.endMonitoringFile", endFilePath);

        SystemEnvUtils.setEnv(AsyncBatchExecutor.ENV_JOB_APP_CD, "hoge");
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertEquals(0, e.state);
        }
    }

    /**
     * mainテスト14 【正常系】
     * <pre>
     * 事前条件
     * ・『ジョブ実行リトライ回数』に&quot;10&quot;が設定
     * 確認項目
     * ・リターンコードが0
     * </pre>
     */
    @Test
    public void testMain14() throws Exception {
        // テストデータ設定
        URL srcUrl = this.getClass().getResource("/testdata/test01.txt");
        String endFilePath = srcUrl.getPath();

        TerasolunaPropertyUtils
                .removeProperty("batchTaskExecutor.executeRetryCountMax");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.executeRetryCountMax", "10");
        TerasolunaPropertyUtils.removeProperty("executor.endMonitoringFile");
        TerasolunaPropertyUtils
                .addProperty("executor.endMonitoringFile", endFilePath);

        SystemEnvUtils.setEnv(AsyncBatchExecutor.ENV_JOB_APP_CD, "hoge");
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertEquals(0, e.state);
        }
    }

    /**
     * mainテスト15 【正常系】
     * <pre>
     * 事前条件
     * ・『空きスレッド残数閾値のデフォルト値』に&quot;10&quot;が設定
     * 確認項目
     * ・リターンコードが0
     * </pre>
     */
    @Test
    public void testMain15() throws Exception {
        // テストデータ設定
        URL srcUrl = this.getClass().getResource("/testdata/test01.txt");
        String endFilePath = srcUrl.getPath();

        TerasolunaPropertyUtils.removeProperty(
                "batchTaskExecutor.availableThreadThresholdCount");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.availableThreadThresholdCount",
                        "10");
        TerasolunaPropertyUtils.removeProperty("executor.endMonitoringFile");
        TerasolunaPropertyUtils
                .addProperty("executor.endMonitoringFile", endFilePath);

        SystemEnvUtils.setEnv(AsyncBatchExecutor.ENV_JOB_APP_CD, "hoge");
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertEquals(0, e.state);
        }
    }

    /**
     * mainテスト16 【正常系】
     * <pre>
     * 事前条件
     * ・『空きスレッド残数閾値以下の場合のウェイト時間（ミリ秒）のデフォルト値』に&quot;10&quot;が設定
     * 確認項目
     * ・リターンコードが0
     * </pre>
     */
    @Test
    public void testMain16() throws Exception {
        // テストデータ設定
        URL srcUrl = this.getClass().getResource("/testdata/test01.txt");
        String endFilePath = srcUrl.getPath();

        TerasolunaPropertyUtils.removeProperty(
                "batchTaskExecutor.availableThreadThresholdWait");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.availableThreadThresholdWait",
                        "10");
        TerasolunaPropertyUtils.removeProperty("executor.endMonitoringFile");
        TerasolunaPropertyUtils
                .addProperty("executor.endMonitoringFile", endFilePath);

        SystemEnvUtils.setEnv(AsyncBatchExecutor.ENV_JOB_APP_CD, "hoge");
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertEquals(0, e.state);
        }
    }

    /**
     * mainテスト17 【正常系】
     * <pre>
     * 事前条件
     * ・『空きスレッド残数閾値以下の場合のウェイト時間（ミリ秒）のデフォルト値』に&quot;10&quot;が設定
     * 確認項目
     * ・リターンコードが0
     * </pre>
     */
    @Test
    public void testMain17() throws Exception {
        // テストデータ設定
        URL srcUrl = this.getClass().getResource("/testdata/test01.txt");
        String endFilePath = srcUrl.getPath();

        TerasolunaPropertyUtils.removeProperty(
                "batchTaskExecutor.availableThreadThresholdWait");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.availableThreadThresholdWait",
                        "10");
        TerasolunaPropertyUtils.removeProperty("executor.endMonitoringFile");
        TerasolunaPropertyUtils
                .addProperty("executor.endMonitoringFile", endFilePath);

        SystemEnvUtils.setEnv(AsyncBatchExecutor.ENV_JOB_APP_CD, "hoge");
        try {
            AsyncBatchExecutor.main(new String[] {});
            fail("例外は発生しません");
        } catch (ExitException e) {
            assertEquals(0, e.state);
        }
    }

    /**
     * testCloseRootApplicationContext001
     *
     * @throws Exception
     */
    @Test
    public void testCloseRootApplicationContext001() throws Exception {
        ApplicationContext context = null;
        AsyncBatchExecutor.closeRootApplicationContext(context);
    }

    /**
     * testCloseRootApplicationContext002
     *
     * @throws Exception
     */
    @Test
    public void testCloseRootApplicationContext002() throws Exception {
        ApplicationContext context = new AbstractApplicationContext() {

            @Override
            protected void closeBeanFactory() {
            }

            @Override
            public ConfigurableListableBeanFactory getBeanFactory()
                    throws IllegalStateException {
                return null;
            }

            @Override
            protected void refreshBeanFactory()
                    throws BeansException, IllegalStateException {
            }
        };

        AsyncBatchExecutor.closeRootApplicationContext(context);
    }

    /**
     * testLogOutputTaskExecutor001
     *
     * @throws Exception
     */
    @Test
    public void testLogOutputTaskExecutor001() throws Exception {
        Log log = new Log() {
            public void debug(Object message) {
            }

            public void debug(Object message, Throwable t) {
            }

            public void error(Object message) {
            }

            public void error(Object message, Throwable t) {
            }

            public void fatal(Object message) {
            }

            public void fatal(Object message, Throwable t) {
            }

            public void info(Object message) {
            }

            public void info(Object message, Throwable t) {
            }

            public boolean isDebugEnabled() {
                return false;
            }

            public boolean isErrorEnabled() {
                return false;
            }

            public boolean isFatalEnabled() {
                return false;
            }

            public boolean isInfoEnabled() {
                return false;
            }

            public boolean isTraceEnabled() {
                return false;
            }

            public boolean isWarnEnabled() {
                return false;
            }

            public void trace(Object message) {
            }

            public void trace(Object message, Throwable t) {
            }

            public void warn(Object message) {
            }

            public void warn(Object message, Throwable t) {
            }
        };

        ThreadPoolTaskExecutor taskExec = new BatchThreadPoolTaskExecutor();
        taskExec.setCorePoolSize(1);
        taskExec.setMaxPoolSize(1);
        taskExec.initialize();

        AsyncBatchExecutor.logOutputTaskExecutor(log, taskExec);
    }

    /**
     * testCheckEndFile001
     *
     * @throws Exception
     */
    @Test
    public void testCheckEndFile001() throws Exception {
        String endFilePath = null;
        boolean result = AsyncBatchExecutor.checkEndFile(endFilePath);
        assertFalse(result);
    }

    /**
     * testCheckEndFile002
     *
     * @throws Exception
     */
    @Test
    public void testCheckEndFile002() throws Exception {
        // テストデータ設定
        URL srcUrl = this.getClass().getResource("/testdata/test01.txt");

        String endFilePath = srcUrl.getPath();
        boolean result = AsyncBatchExecutor.checkEndFile(endFilePath);
        assertTrue(result);
    }

    /**
     * testCheckTaskQueue001
     *
     * @throws Exception
     */
    @Test
    public void testCheckTaskQueue001() throws Exception {
        ThreadPoolTaskExecutor taskExec = new BatchThreadPoolTaskExecutor();
        taskExec.setCorePoolSize(1);
        taskExec.setMaxPoolSize(1);
        taskExec.initialize();

        boolean result = AsyncBatchExecutor.checkTaskQueue(taskExec);
        assertTrue(result);
    }

    /**
     * testCheckTaskQueue002
     *
     * @throws Exception
     */
    @Test
    public void testCheckTaskQueue002() throws Exception {
        ThreadPoolTaskExecutor taskExec = new ThreadPoolTaskExecutor();
        taskExec.setCorePoolSize(1);
        taskExec.setMaxPoolSize(1);
        taskExec.setQueueCapacity(1);
        taskExec.initialize();

        Runnable task = new Runnable() {
            public void run() {
                try {
                    System.out.println("aaa");
                    Thread.sleep(5000);
                    System.out.println("bbb");
                } catch (InterruptedException e) {
                    System.out.println("ccc");
                }
            }
        };
        taskExec.execute(task);

        Thread.sleep(1000);

        boolean result = AsyncBatchExecutor.checkTaskQueue(taskExec);
        assertTrue(result);

        taskExec.shutdown();
        Thread.sleep(500);
    }

    /**
     * testCheckTaskQueue003
     *
     * @throws Exception
     */
    @Test
    public void testCheckTaskQueue003() throws Exception {
        ThreadPoolTaskExecutor taskExec = new BatchThreadPoolTaskExecutor();
        taskExec.setCorePoolSize(1);
        taskExec.setMaxPoolSize(1);
        taskExec.initialize();

        Runnable task = new Runnable() {
            public void run() {
                try {
                    System.out.println("aaa");
                    Thread.sleep(5000);
                    System.out.println("bbb");
                } catch (InterruptedException e) {
                    System.out.println("ccc");
                }
            }
        };
        taskExec.execute(task);

        Thread.sleep(1000);

        boolean result = AsyncBatchExecutor.checkTaskQueue(taskExec);
        assertFalse(result);

        taskExec.shutdown();
        Thread.sleep(500);
    }

    /**
     * testExecuteJob001
     *
     * @throws Exception
     */
    @Test
    public void testExecuteJob001() throws Exception {
        // パラメータ
        AsyncBatchExecutor executor = null;
        ApplicationContext ctx = null;
        ThreadPoolTaskExecutor taskExecutor = null;
        String batchTaskServantName = null;
        BatchJobListResult batchJobListResult = null;

        // テスト
        boolean result = AsyncBatchExecutor
                .executeJob(executor, ctx, taskExecutor, batchTaskServantName,
                        batchJobListResult);

        // 検証
        assertFalse(result);
    }

    /**
     * testExecuteJob002
     *
     * @throws Exception
     */
    @Test
    public void testExecuteJob002() throws Exception {
        // パラメータ
        AsyncBatchExecutor executor = new AsyncBatchExecutor() {
            @Override
            public SystemDao getSystemDao() {
                return null;
            }
        };
        ApplicationContext ctx = new MockApplicationContext();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        String batchTaskServantName = "batchTaskServant";
        BatchJobListResult batchJobListResult = new BatchJobListResult();

        // テスト
        boolean result = AsyncBatchExecutor
                .executeJob(executor, ctx, taskExecutor, batchTaskServantName,
                        batchJobListResult);

        // 検証
        assertFalse(result);
    }

    /**
     * testExecuteJob004
     *
     * @throws Exception
     */
    @Test
    public void testExecuteJob004() throws Exception {
        // パラメータ
        AsyncBatchExecutor executor = new AsyncBatchExecutor() {
            @Override
            public PlatformTransactionManager getSysTransactionManager() {
                return null;
            }
        };
        ApplicationContext ctx = new MockApplicationContext();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        String batchTaskServantName = "batchTaskServant";
        BatchJobListResult batchJobListResult = new BatchJobListResult();

        // テスト
        boolean result = AsyncBatchExecutor
                .executeJob(executor, ctx, taskExecutor, batchTaskServantName,
                        batchJobListResult);

        // 検証
        assertFalse(result);
    }

    /**
     * testExecuteJob005
     *
     * @throws Exception
     */
    @Test
    public void testExecuteJob005() throws Exception {
        // パラメータ
        AsyncBatchExecutor executor = new AsyncBatchExecutor();
        ApplicationContext ctx = new MockApplicationContext();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        String batchTaskServantName = "batchTaskServant";
        BatchJobListResult batchJobListResult = new BatchJobListResult();

        // テスト
        boolean result = AsyncBatchExecutor
                .executeJob(executor, ctx, taskExecutor, batchTaskServantName,
                        batchJobListResult);

        // 検証
        assertFalse(result);
    }

    /**
     * testExecuteJob006
     *
     * @throws Exception
     */
    @Test
    public void testExecuteJob006() throws Exception {
        // パラメータ
        AsyncBatchExecutor executor = new AsyncBatchExecutor();
        MockApplicationContext ctx = new MockApplicationContext() {
            @Override
            public Object getBean(String key, Class arg1)
                    throws BeansException {
                return null;
            }
        };
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        String batchTaskServantName = "batchServant";
        BatchJobListResult batchJobListResult = new BatchJobListResult();

        // テスト
        boolean result = AsyncBatchExecutor
                .executeJob(executor, ctx, taskExecutor, batchTaskServantName,
                        batchJobListResult);

        // 検証
        assertFalse(result);
    }

    /**
     * testExecuteJob007
     *
     * @throws Exception
     */
    @Test
    public void testExecuteJob007() throws Exception {
        // パラメータ
        AsyncBatchExecutor executor = new AsyncBatchExecutor() {
            @Override
            protected boolean startBatchStatus(String jobSequenceId,
                    SystemDao systemDao,
                    PlatformTransactionManager transactionManager) {
                return false;
            }
        };
        MockApplicationContext ctx = new MockApplicationContext();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor() {
            private static final long serialVersionUID = 5460413290026263577L;

            @Override
            public void execute(Runnable task) {
                throw new IllegalStateException("BatchServantが実行されたらエラー");
            }
        };
        String batchTaskServantName = "batchServant";
        BatchJobListResult batchJobListResult = new BatchJobListResult();

        BatchServant batchServant = new BatchServant() {
            public void setJobSequenceId(String jobSequenceId) {
            }

            public void run() {
            }
        };
        ctx.addBeanMap(batchTaskServantName, batchServant);

        // テスト
        boolean result = AsyncBatchExecutor
                .executeJob(executor, ctx, taskExecutor, batchTaskServantName,
                        batchJobListResult);

        // 検証(ジョブ管理テーブルステータス更新が正常に終了しなかった場合もポーリングループは継続させる。)
        assertTrue(result);
    }

    /**
     * testExecuteJob008
     *
     * @throws Exception
     */
    @Test
    public void testExecuteJob008() throws Exception {
        // パラメータ
        TerasolunaPropertyUtils
                .removeProperty("batchTaskExecutor.executeRetryCountMax");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.executeRetryCountMax", "10");
        TerasolunaPropertyUtils
                .removeProperty("batchTaskExecutor.executeRetryInterval");
        TerasolunaPropertyUtils
                .addProperty("batchTaskExecutor.executeRetryInterval", "10");

        AsyncBatchExecutor executor = new AsyncBatchExecutor() {
            @Override
            protected boolean startBatchStatus(String jobSequenceId,
                    SystemDao systemDao,
                    PlatformTransactionManager transactionManager) {
                return true;
            }
        };
        MockApplicationContext ctx = new MockApplicationContext();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor() {
            @Override
            public void execute(Runnable task) {
                throw new TaskRejectedException("hoge");
            }
        };
        String batchTaskServantName = "batchServant";
        BatchJobListResult batchJobListResult = new BatchJobListResult();

        BatchServant batchServant = new BatchServant() {
            public void setJobSequenceId(String jobSequenceId) {
            }

            public void run() {
            }
        };
        ctx.addBeanMap(batchTaskServantName, batchServant);
        AsyncBatchExecutor.destroyCandidateThreadGroupList.clear();

        // テスト
        boolean result = AsyncBatchExecutor
                .executeJob(executor, ctx, taskExecutor, batchTaskServantName,
                        batchJobListResult);

        // 検証
        assertFalse(result);
        assertSame(taskExecutor.getThreadGroup(),
                AsyncBatchExecutor.destroyCandidateThreadGroupList.get(0));
    }

    /**
     * testExecuteJob009
     *
     * @throws Exception
     */
    @Test
    public void testExecuteJob009() throws Exception {
        // パラメータ
        AsyncBatchExecutor executor = new AsyncBatchExecutor() {
            @Override
            protected boolean startBatchStatus(String jobSequenceId,
                    SystemDao systemDao,
                    PlatformTransactionManager transactionManager) {
                return true;
            }
        };
        MockApplicationContext ctx = new MockApplicationContext();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor() {
            @Override
            public void execute(Runnable task) {
            }
        };
        String batchTaskServantName = "batchServant";
        BatchJobListResult batchJobListResult = new BatchJobListResult();

        BatchServant batchServant = new BatchServant() {
            public void setJobSequenceId(String jobSequenceId) {
            }

            public void run() {
            }
        };
        ctx.addBeanMap(batchTaskServantName, batchServant);

        // テスト
        boolean result = AsyncBatchExecutor
                .executeJob(executor, ctx, taskExecutor, batchTaskServantName,
                        batchJobListResult);

        // 検証
        assertTrue(result);
    }

    /**
     * testBatchServantTaskEndTrackerRun001
     * runメソッド実行時に、
     * BatchServantTaskEndTrackerにラップされているBatchServantのrunメソッドが実行され、
     * AsyncBatchExecutor.destroyCandidateThreadGroupListに、
     * コンストラクタで与えたThreadGroupが登録されることを確認する。
     *
     * @throws Exception
     */
    @Test
    public void testBatchServantTaskEndTrackerRun001() throws Exception {
        // パラメータ
        final AtomicBoolean runWasExecuted = new AtomicBoolean(false);
        BatchServant batchServant = new BatchServant() {
            public void setJobSequenceId(String jobSequenceId) {
            }

            public void run() {
                runWasExecuted.set(true);
            }
        };
        ThreadGroup testTG = new ThreadGroup(
                "testBatchServantTaskEndTrackerRun001-TG");
        BatchServantTaskEndTracker tracker = new BatchServantTaskEndTracker(
                batchServant, testTG);
        AsyncBatchExecutor.destroyCandidateThreadGroupList.clear();

        // テスト
        tracker.run();

        try {
            // 検証
            assertTrue(runWasExecuted.get());
            assertSame(testTG,
                    AsyncBatchExecutor.destroyCandidateThreadGroupList.get(0));
        } finally {
            testTG.destroy();
            AsyncBatchExecutor.destroyCandidateThreadGroupList.remove(testTG);
        }
    }

    /**
     * testDestroyThreadGroupsIfPossible001
     * AsyncBatchExecutor.destroyCandidateThreadGroupListが0件のとき、
     * どのThreadGroupもdestroyしないことを確認する。
     * (アクティブスレッド数が0のThreadGroupでも、
     * AsyncBatchExecutor.destroyCandidateThreadGroupListに登録されていなければ
     * destroyしないことを確認する。)
     *
     * @throws Exception
     */
    @Test
    public void testDestroyThreadGroupsIfPossible001() throws Exception {
        // パラメータ
        ThreadGroup testTG = new ThreadGroup(
                "testDestroyThreadGroupsIfPossible001-TG");
        AsyncBatchExecutor.destroyCandidateThreadGroupList.clear();

        // テスト
        AsyncBatchExecutor.destroyThreadGroupsIfPossible();

        try {
            // 検証
            assertFalse(testTG.isDestroyed());
        } finally {
            testTG.destroy();
        }
    }

    /**
     * testDestroyThreadGroupsIfPossible002
     * AsyncBatchExecutor.destroyCandidateThreadGroupListが1件であり、
     * 登録されているThreadGroupのアクティブスレッド数が0個の場合、
     * ・ThreadGroupをdestroyすること
     * ・destroyしたThreadGroupをAsyncBatchExecutor.destroyCandidateThreadGroupListがから削除すること
     * を確認する。
     *
     * @throws Exception
     */
    @Test
    public void testDestroyThreadGroupsIfPossible002() throws Exception {
        // パラメータ
        ThreadGroup testTG = new ThreadGroup(
                "testDestroyThreadGroupsIfPossible002-TG");
        AsyncBatchExecutor.destroyCandidateThreadGroupList.clear();
        AsyncBatchExecutor.destroyCandidateThreadGroupList.add(testTG);

        // テスト
        AsyncBatchExecutor.destroyThreadGroupsIfPossible();

        // 検証
        assertTrue(testTG.isDestroyed());
        assertEquals(0,
                AsyncBatchExecutor.destroyCandidateThreadGroupList.size());
    }

    /**
     * testDestroyThreadGroupsIfPossible003
     * AsyncBatchExecutor.destroyCandidateThreadGroupListが1件であり、
     * 登録されているThreadGroupのアクティブスレッド数が1個の場合、
     * ・ThreadGroupをdestroyしないこと
     * ・ThreadGroupをAsyncBatchExecutor.destroyCandidateThreadGroupListがから削除しないこと
     * を確認する。
     *
     * @throws Exception
     */
    @Test
    public void testDestroyThreadGroupsIfPossible003() throws Exception {
        // パラメータ
        ThreadGroup testTG = new ThreadGroup(
                "testDestroyThreadGroupsIfPossible003-TG");
        AsyncBatchExecutor.destroyCandidateThreadGroupList.clear();
        AsyncBatchExecutor.destroyCandidateThreadGroupList.add(testTG);
        Thread testThread = new Thread(testTG,
                "testDestroyThreadGroupsIfPossible003-T") {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        };
        testThread.start();

        // テスト
        AsyncBatchExecutor.destroyThreadGroupsIfPossible();
        assertEquals(1,
                AsyncBatchExecutor.destroyCandidateThreadGroupList.size());
        assertEquals(testTG,
                AsyncBatchExecutor.destroyCandidateThreadGroupList.get(0));

        try {
            // 検証
            assertFalse(testTG.isDestroyed());
        } finally {
            testThread.interrupt();
            while (testTG.activeCount() != 0) {
                Thread.sleep(100);
            }
            testTG.destroy();
            AsyncBatchExecutor.destroyCandidateThreadGroupList.remove(testTG);
        }
    }

    /**
     * testDestroyThreadGroupsIfPossible005
     * AsyncBatchExecutor.destroyCandidateThreadGroupListが4件であり、
     * アクティブスレッド数が1個のThreadGroupが2個、
     * アクティブスレッド数が0個のThreadGroupが2個、登録されている場合、
     * ・アクティブスレッド数が1個のThreadGroupはdestroyされないこと
     * ・アクティブスレッド数が0個のThreadGroupはdestroyされること
     * ・destroyしたThreadGroupをAsyncBatchExecutor.destroyCandidateThreadGroupListから削除すること
     * ・destroyしなかったThreadGroupをAsyncBatchExecutor.destroyCandidateThreadGroupListから削除しないこと
     * を確認する。
     *
     * @throws Exception
     */
    @Test
    public void testDestroyThreadGroupsIfPossible004() throws Exception {
        // パラメータ
        ThreadGroup testTG1 = new ThreadGroup(
                "testDestroyThreadGroupsIfPossible004-TG1");
        ThreadGroup testTG2 = new ThreadGroup(
                "testDestroyThreadGroupsIfPossible004-TG2");
        ThreadGroup testTG3 = new ThreadGroup(
                "testDestroyThreadGroupsIfPossible004-TG3");
        ThreadGroup testTG4 = new ThreadGroup(
                "testDestroyThreadGroupsIfPossible004-TG4");
        AsyncBatchExecutor.destroyCandidateThreadGroupList.clear();
        AsyncBatchExecutor.destroyCandidateThreadGroupList.add(testTG1);
        AsyncBatchExecutor.destroyCandidateThreadGroupList.add(testTG2);
        AsyncBatchExecutor.destroyCandidateThreadGroupList.add(testTG3);
        AsyncBatchExecutor.destroyCandidateThreadGroupList.add(testTG4);
        Thread testThread1 = new Thread(testTG1,
                "testDestroyThreadGroupsIfPossible004-T1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        };
        Thread testThread3 = new Thread(testTG3,
                "testDestroyThreadGroupsIfPossible003-T3") {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        };
        testThread1.start();
        testThread3.start();

        // テスト
        AsyncBatchExecutor.destroyThreadGroupsIfPossible();
        assertEquals(2,
                AsyncBatchExecutor.destroyCandidateThreadGroupList.size());
        assertEquals(testTG1,
                AsyncBatchExecutor.destroyCandidateThreadGroupList.get(0));
        assertEquals(testTG3,
                AsyncBatchExecutor.destroyCandidateThreadGroupList.get(1));

        try {
            // 検証
            assertFalse(testTG1.isDestroyed());
            assertTrue(testTG2.isDestroyed());
            assertFalse(testTG3.isDestroyed());
            assertTrue(testTG4.isDestroyed());
        } finally {
            testThread1.interrupt();
            testThread3.interrupt();
            while (testTG1.activeCount() != 0) {
                Thread.sleep(100);
            }
            testTG1.destroy();
            AsyncBatchExecutor.destroyCandidateThreadGroupList.remove(testTG1);
            while (testTG3.activeCount() != 0) {
                Thread.sleep(100);
            }
            testTG3.destroy();
            AsyncBatchExecutor.destroyCandidateThreadGroupList.remove(testTG3);
        }
    }

    /**
     * testLogActiveThreadGroupsInfo001
     * activeGroupCount実行後にThreadGroupが増えない状況においては、
     * enumerateを1回だけ実行し、
     * enumerateの戻り値＜enumerateの引数の配列要素数
     * を満たすことを確認する。
     *
     * @throws Exception
     */
    @Test
    public void testLogActiveThreadGroupsInfo001() throws Exception {
        final AtomicInteger enumerateCallCount = new AtomicInteger(0);
        final AtomicBoolean enumerateSuccess = new AtomicBoolean(false);
        ThreadGroup testParentTG = new ThreadGroup(
                "testLogActiveThreadGroupsInfo001-ParentTG") {
            @Override
            public int activeGroupCount() {
                return super.activeGroupCount();
            }

            @Override
            public int enumerate(ThreadGroup[] list) {
                enumerateCallCount.incrementAndGet();
                int ret = super.enumerate(list);
                enumerateSuccess.set(ret < list.length);
                return ret;
            }
        };

        Thread testThread = new Thread(testParentTG,
                "testLogActiveThreadGroupsInfo001-ParentT") {
            @Override
            public void run() {
                ThreadGroup testTG = new ThreadGroup(
                        "testLogActiveThreadGroupsInfo001-TG");

                // テスト
                AsyncBatchExecutor.logActiveThreadGroupsInfo();

                testTG.destroy();
            }

        };
        testThread.start();
        testThread.join();

        // 検証
        assertEquals(1, enumerateCallCount.get());
        assertTrue(enumerateSuccess.get());
    }

    /**
     * testLogActiveThreadGroupsInfo002
     * activeGroupCount実行後にThreadGroupが増えたことにより、
     * enumerateの戻り値＜enumerateの引数の配列要素数(activeGroupCountの戻り値+1)
     * を満たさないとき、
     * enumerateの戻り値＜enumerateの引数の配列要素数
     * を満たすよう、再度activeGroupCountとenumerateを実行することを確認する。
     *
     * @throws Exception
     */
    @Test
    public void testLogActiveThreadGroupsInfo002() throws Exception {
        final AtomicBoolean firstCount = new AtomicBoolean(true);
        final AtomicInteger enumerateCallCount = new AtomicInteger(0);
        final AtomicBoolean enumerateSuccess = new AtomicBoolean(false);
        ThreadGroup testParentTG = new ThreadGroup(
                "testLogActiveThreadGroupsInfo002-ParentTG") {
            @Override
            public int activeGroupCount() {
                if (firstCount.get()) {
                    // 最初の1回だけ、1つ少ない値を返すことで、
                    // activeGroupCount実行後にThreadGroupが増えた状況をエミュレート。
                    firstCount.set(false);
                    return super.activeGroupCount() - 1;
                }
                return super.activeGroupCount();
            }

            @Override
            public int enumerate(ThreadGroup[] list) {
                enumerateCallCount.incrementAndGet();
                int ret = super.enumerate(list);
                enumerateSuccess.set(ret < list.length);
                return ret;
            }
        };

        Thread testThread = new Thread(testParentTG,
                "testLogActiveThreadGroupsInfo002-ParentT") {
            @Override
            public void run() {
                ThreadGroup testTG = new ThreadGroup(
                        "testLogActiveThreadGroupsInfo002-TG");

                // テスト
                AsyncBatchExecutor.logActiveThreadGroupsInfo();

                testTG.destroy();
            }
        };
        testThread.start();
        testThread.join();

        // 検証
        assertEquals(2, enumerateCallCount.get());
        assertTrue(enumerateSuccess.get());
    }
}
