package jp.terasoluna.fw.batch.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.terasoluna.fw.batch.executor.SecurityManagerEx.ExitException;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.unit.util.SystemEnvUtils;

public class SyncBatchExecutorTest {

    final SecurityManager sm = System.getSecurityManager();

    public SyncBatchExecutorTest() {
        super();
    }

    @Before
    public void setUp() {
        System.setSecurityManager(new SecurityManagerEx());
    }

    @After
    public void tearDown() {
        SystemEnvUtils.restoreEnv();
        System.setSecurityManager(sm);
    }

    /**
     * mainテスト01【異常系】
     * 
     * <pre>
     * 事前条件
     * ・SyncBatchExecutorの起動引数にfooを渡す
     * ・beansDef/foo.xmlが存在しない
     * 確認事項
     * ・終了コードが-1であること
     * ・IDがWAL025002のWARNログが出力されること
     * </pre>
     * 
     * @throws Exception
     */
    @Test
    public void testMain01() throws Exception {
        try {
            SyncBatchExecutor.main(new String[] { "foo" });
            fail("異常です");
        } catch (ExitException e) {
            assertEquals(-1, e.state);
        }
    }

    /**
     * mainテスト02【正常系】
     * 
     * <pre>
     * 事前条件
     * ・SyncBatchExecutorの起動引数にTestSyncBatchExecutor01を渡す
     * ・beansDef/TestSyncBatchExecutor01.xmlが存在する
     * ・beanNameがTestSyncBatchExecutor01のBeanがロードされる
     * 確認事項
     * ・終了コードが100であること
     * </pre>
     * 
     * @throws Exception
     */
    @Test
    public void testMain02() throws Exception {
        try {
            SyncBatchExecutor.main(new String[] { "TestSyncBatchExecutor01" });
            fail("異常です");
        } catch (ExitException e) {
            assertEquals(100, e.state);
        }
    }

    /**
     * mainテスト03【異常系】
     * 
     * <pre>
     * 事前条件
     * ・SyncBatchExecutorの起動引数に何も渡さない
     * ・環境変数JOB_APP_CDが未設定
     * 確認事項
     * ・終了コードが-1であること
     * </pre>
     * 
     * @throws Exception
     */
    @Test
    public void testMain03() throws Exception {
        try {
            SystemEnvUtils.removeEnv(SyncBatchExecutor.ENV_JOB_APP_CD);
            SyncBatchExecutor.main(new String[] {});
            fail("異常です");
        } catch (ExitException e) {
            assertEquals(-1, e.state);
        }
    }

    /**
     * mainテスト04【異常系】
     * 
     * <pre>
     * 事前条件
     * ・SyncBatchExecutorの起動引数に何も渡さない
     * ・環境変数JOB_APP_CDにTestSyncBatchExecutor01を渡す
     * ・beansDef/TestSyncBatchExecutor01.xmlが存在する
     * ・beanNameがTestSyncBatchExecutor01のBeanがロードされる
     * 確認事項
     * ・終了コードが100であること
     * </pre>
     * 
     * @throws Exception
     */
    @Test
    public void testMain04() throws Exception {
        try {
            SystemEnvUtils.setEnv(SyncBatchExecutor.ENV_JOB_APP_CD,
                    "TestSyncBatchExecutor01");
            SyncBatchExecutor.main(new String[] {});
            fail("異常です");
        } catch (ExitException e) {
            assertEquals(100, e.state);
        }
    }

    /**
     * mainテスト05【正常系】
     * 
     * <pre>
     * 事前条件
     * ・SyncBatchExecutorの起動引数にTestSyncBatchExecutor01 param1 param2 param3 param4 param5 param6 param7 param8 param9 param10 param11 param12 param13 param14 param15 param16 param17 param18 param19 param20 を渡す
     * ・beansDef/TestSyncBatchExecutor01.xmlが存在する
     * ・beanNameがTestSyncBatchExecutor01のBeanがロードされる
     * 確認事項
     * ・param1～20までID:DAL025044のログに出力されること
     * ・終了コードが100であること
     * </pre>
     * 
     * @throws Exception
     */
    @Test
    public void testMain05() throws Exception {
        try {
            SyncBatchExecutor.main(new String[] { "TestSyncBatchExecutor01",
                    "param1", "param2", "param3", "param4", "param5", "param6",
                    "param7", "param8", "param9", "param10", "param11",
                    "param12", "param13", "param14", "param15", "param16",
                    "param17", "param18", "param19", "param20" });
            fail("異常です");
        } catch (ExitException e) {
            assertEquals(100, e.state);
        }
    }

    /**
     * mainテスト06【正常系】
     * 
     * <pre>
     * 事前条件
     * ・SyncBatchExecutorの起動引数にTestSyncBatchExecutor01 param1 param2 param3 param4 param5 param6 param7 param8 param9 param10 param11 param12 param13 param14 param15 param16 param17 param18 param19 param20 param21を渡す
     * ・beansDef/TestSyncBatchExecutor01.xmlが存在する
     * ・beanNameがTestSyncBatchExecutor01のBeanがロードされる
     * 確認事項
     * ・param1～20までID:DAL025044のログに出力されること
     * ・終了コードが100であること
     * </pre>
     * 
     * @throws Exception
     */
    @Test
    public void testMain06() throws Exception {
        try {
            SyncBatchExecutor.main(new String[] { "TestSyncBatchExecutor01",
                    "param1", "param2", "param3", "param4", "param5", "param6",
                    "param7", "param8", "param9", "param10", "param11",
                    "param12", "param13", "param14", "param15", "param16",
                    "param17", "param18", "param19", "param20", "param21" });
            fail("異常です");
        } catch (ExitException e) {
            assertEquals(100, e.state);
        }
    }

    /**
     * mainテスト07【正常系】
     * 
     * <pre>
     * 事前条件
     * ・SyncBatchExecutorの起動引数にTestSyncBatchExecutor01を渡す
     * ・beansDef/TestSyncBatchExecutor01.xmlが存在する
     * ・beanNameがTestSyncBatchExecutor01のBeanがロードされる
     * ・環境変数JOB_SEQ_IDにseq01が設定されている
     * 確認事項
     * ・終了コードが100であること
     * ・ID:DAL025044のDEBUGログにjobSequenceId=seq01がふくまれること
     * </pre>
     * 
     * @throws Exception
     */
    @Test
    public void testMain07() throws Exception {
        try {
            SystemEnvUtils.setEnv(SyncBatchExecutor.ENV_JOB_SEQ_ID, "seq01");
            SyncBatchExecutor.main(new String[] { "TestSyncBatchExecutor01" });
            fail("異常です");
        } catch (ExitException e) {
            assertEquals(100, e.state);
        }
    }

    /**
     * mainテスト08【正常系】
     * 
     * <pre>
     * 事前条件
     * ・SyncBatchExecutorの起動引数にTestSyncBatchExecutor01が設定されている
     * ・環境変数JOB_ARG_NM1～20にそれぞれparam1 param2 param3 param4 param5 param6 param7 param8 param9 param10 param11 param12 param13 param14 param15 param16 param17 param18 param19 param20が設定されている
     * ・beansDef/TestSyncBatchExecutor01.xmlが存在する
     * ・beanNameがTestSyncBatchExecutor01のBeanがロードされる
     * 確認事項
     * ・param1～20までID:DAL025044のログに出力されること
     * ・終了コードが100であること
     * </pre>
     * 
     * @throws Exception
     */
    @Test
    public void testMain08() throws Exception {
        try {
            for (int i = 1; i <= 20; i++) {
                SystemEnvUtils.setEnv("JOB_ARG_NM" + i, "param" + i);
            }
            SyncBatchExecutor.main(new String[] { "TestSyncBatchExecutor01" });
            fail("異常です");
        } catch (ExitException e) {
            assertEquals(100, e.state);
        }
    }

    /**
     * mainテスト09【正常系】
     * 
     * <pre>
     * 事前条件
     * ・SyncBatchExecutorの起動引数にTestSyncBatchExecutor01が設定されている
     * ・環境変数JOB_ARG_NM1～20にそれぞれparam1 param2 param3 param4 param5 param6 param7 param8 param9 param10 param11 param12 param13 param14 param15 param16 param17 param18 param19 param20 param21が設定されている
     * ・beansDef/TestSyncBatchExecutor01.xmlが存在する
     * ・beanNameがTestSyncBatchExecutor01のBeanがロードされる
     * 確認事項
     * ・param1～20までID:DAL025044のログに出力されること
     * ・終了コードが100であること
     * </pre>
     * 
     * @throws Exception
     */
    @Test
    public void testMain09() throws Exception {
        try {
            for (int i = 1; i <= 21; i++) {
                SystemEnvUtils.setEnv("JOB_ARG_NM" + i, "param" + i);
            }
            SyncBatchExecutor.main(new String[] { "TestSyncBatchExecutor01" });
            fail("異常です");
        } catch (ExitException e) {
            assertEquals(100, e.state);
        }
    }

    /**
     * getParamテスト01
     * 
     * <pre>
     * 事前条件：
     * getParamのget+第二引数+第三引数のメソッドが存在する
     * 確認項目：
     * ・get+第二引数+第三引数のメソッドの結果が返却されること
     * </pre>
     * 
     * @throws Exception
     */
    @Test
    public void testGetParam01() throws Exception {
        Method method = SyncBatchExecutor.class.getDeclaredMethod("getParam",
                new Class[] { Object.class, String.class, int.class });
        method.setAccessible(true);
        String getParam = (String) method.invoke(SyncBatchExecutor.class,
                new Object[] { new GetParamBean(), "Foo", 1 });

        assertEquals("foo1", getParam);
    }

    /**
     * getParamテスト02
     * 
     * <pre>
     * 事前条件：
     * 
     * 確認項目：
     * ・nullが返却されること
     * ・スタックトレースにjava.lang.SecurityExceptionが出力されること
     * </pre>
     * 
     * @throws Exception
     */
    // public void testGetParam02() throws Exception {
    // 発生不可能
    // }
    /**
     * getParamテスト03
     * 
     * <pre>
     * 事前条件：
     * get+第二引数+第三引数のメソッドが存在しない
     * 確認項目：
     * ・nullが返却されること
     * ・スタックトレースにjava.lang.NoSuchMethodExceptionが出力されること
     * </pre>
     * 
     * @throws Exception
     */
    @Test
    public void testGetParam03() throws Exception {
        Method method = SyncBatchExecutor.class.getDeclaredMethod("getParam",
                new Class[] { Object.class, String.class, int.class });
        method.setAccessible(true);
        String getParam = (String) method.invoke(SyncBatchExecutor.class,
                new Object[] { new BatchJobData(), "HogeMethod", 1 });
        assertEquals(null, getParam);
    }

    /**
     * getParamテスト04
     * 
     * <pre>
     * 事前条件：
     * 
     * 確認項目：
     * ・nullが返却されること
     * ・スタックトレースにIllegalArgumentExceptionが出力されること
     * </pre>
     * 
     * @throws Exception
     */
    // public void testGetParam04() throws Exception {
    // 発生不可能
    // }
    /**
     * getParamテスト05
     * 
     * <pre>
     * 事前条件：
     * 
     * 確認項目：
     * ・nullが返却されること
     * ・スタックトレースにIllegalAccessExceptionが出力されること
     * </pre>
     * 
     * @throws Exception
     */

    // public void testGetParam05() throws Exception {
    // 発生不可能
    // }
    /**
     * getParamテスト06
     * 
     * <pre>
     * 事前条件：
     * 
     * 確認項目：
     * ・nullが返却されること
     * ・スタックトレースにjava.lang.reflect.InvocationTargetExceptionが出力されること
     * </pre>
     * 
     * @throws Exception
     */
    @Test
    public void testGetParam06() throws Exception {
        Method method = SyncBatchExecutor.class.getDeclaredMethod("getParam",
                new Class[] { Object.class, String.class, int.class });
        method.setAccessible(true);
        String getParam = (String) method.invoke(SyncBatchExecutor.class,
                new Object[] { new GetParamBean(), "Foo", 6 });
        assertEquals(null, getParam);
    }

    /**
     * GetParamBean
     */
    public static class GetParamBean {
        private String foo1 = "foo1";

        public String getFoo1() {
            return foo1;
        }

        public String getFoo6() {
            throw new NullPointerException();
        }
    }

    /**
     * testSetParam01
     * @throws Exception
     */
    @Test
    public void testSetParam01() throws Exception {
        SetParamBean bean = new SetParamBean();
        Method method = SyncBatchExecutor.class.getDeclaredMethod("setParam",
                new Class[] { Object.class, String.class, int.class,
                        String.class });
        method.setAccessible(true);
        method.invoke(SyncBatchExecutor.class, new Object[] { bean, "Foo", 1,
                "hoge" });
        assertEquals("hoge", bean.getFoo1());
    }

    // SecurityExceptionのスタックトレースが出力されること
    // public void testSetParam02() {
    // 発生できず
    // }

    /**
     * testSetParam03<br>
     * NoSuchMethodExceptionのスタックトレースが出力されること
     * @throws Exception
     */
    @Test
    public void testSetParam03() throws Exception {
        SetParamBean bean = new SetParamBean();
        Method method = SyncBatchExecutor.class.getDeclaredMethod("setParam",
                new Class[] { Object.class, String.class, int.class,
                        String.class });
        method.setAccessible(true);
        method.invoke(SyncBatchExecutor.class, new Object[] { bean, "Foo", 3,
                "hoge" });
        assertEquals(null, bean.getFoo1());
    }

    // IllegalArgumentExceptionのスタックトレースが出力されること
    // public void testSetParam04() {
    // 発生できず
    // }

    // IllegalAccessExceptionのスタックトレースが出力されること
    // public void testSetParam05() {
    // 発生できず
    // }

    /**
     * testSetParam06<br>
     * InvocationTargetExceptionのスタックトレースが出力されること
     * @throws Exception
     */
    @Test
    public void testSetParam06() throws Exception {
        SetParamBean bean = new SetParamBean();
        Method method = SyncBatchExecutor.class.getDeclaredMethod("setParam",
                new Class[] { Object.class, String.class, int.class,
                        String.class });
        method.setAccessible(true);
        method.invoke(SyncBatchExecutor.class, new Object[] { bean, "Foo", 5,
                "hoge" });
        assertEquals(null, bean.getFoo1());
    }

    /**
     * SetParamBean
     */
    public static class SetParamBean {
        private String foo1;

        public String getFoo1() {
            return foo1;
        }

        public void setFoo1(String foo1) {
            this.foo1 = foo1;
        }

        public void setFoo5(String foo1) {
            throw new NullPointerException();
        }
    }
}
