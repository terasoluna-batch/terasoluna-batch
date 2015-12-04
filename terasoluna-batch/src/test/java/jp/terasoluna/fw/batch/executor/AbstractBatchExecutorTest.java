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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.unit.util.TerasolunaPropertyUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * AbstractBatchExecutorTestのテストケースクラス
 */
public class AbstractBatchExecutorTest {

    /**
     * blogicBeanNameがnullの場合、BLogicResultのリターンコードが255が返却されることを確認する またログにJOB_APP_CD:[null]が出力されていることを確認する
     * @throws Exception
     */
    @Test
    public void testExecuteBatch01() throws Exception {

        // テスト入力データ設定
        BatchJobData param = new BatchJobData();

        // テスト実施
        AbstractBatchExecutor batchExecutor = new SyncBatchExecutor();
        BLogicResult result = batchExecutor.executeBatch(param);

        // 結果検証
        assertEquals(255, result.getBlogicStatus());
    }

    /**
     * blogicBeanNameが空文字の場合、BLogicResultのリターンコードが255が返却されることを確認する またログにJOB_APP_CD:[]が出力されていることを確認する
     * @throws Exception
     */
    @Test
    public void testExecuteBatch02() throws Exception {

        // テスト入力データ設定
        BatchJobData param = new BatchJobData();
        param.setJobAppCd("");
        // テスト実施
        AbstractBatchExecutor batchExecutor = new SyncBatchExecutor();
        BLogicResult result = batchExecutor.executeBatch(param);

        // 結果検証
        assertEquals(255, result.getBlogicStatus());
    }

    /**
     * blogicBeanNameがB000001の場合、BLogicResultのリターンコードが0が返却されることを確認する
     * テストを実施するためには、ジョブBean定義ファイル[B000001.xml]とB000001BLogic.javaが必要となる
     * @throws Exception
     */
    @Test
    public void testExecuteBatch03() throws Exception {

        // テスト入力データ設定
        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000001");
        // テスト実施
        AbstractBatchExecutor batchExecutor = new SyncBatchExecutor();
        BLogicResult result = batchExecutor.executeBatch(param);

        // 結果検証
        assertEquals(0, result.getBlogicStatus());
    }

    /**
     * blogicBeanNameがB000001の場合、BLogicResultのリターンコードが0が返却されることを確認する
     * テストを実施するためには、ジョブBean定義ファイル[B000001.xml]とB000001BLogic.javaが必要となる
     * @throws Exception
     */
    @Test
    public void testExecuteBatch04() throws Exception {

        // テスト入力データ設定
        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000001");
        param.setJobArgNm1("test1");
        // テスト実施
        AbstractBatchExecutor batchExecutor = new SyncBatchExecutor();
        BLogicResult result = batchExecutor.executeBatch(param);

        // 結果検証
        assertEquals(0, result.getBlogicStatus());
    }

    /**
     * blogicBeanNameがB000001の場合、BLogicResultのリターンコードが0が返却されることを確認する
     * テストを実施するためには、ジョブBean定義ファイル[B000001.xml]とB000001BLogic.javaが必要となる
     * @throws Exception
     */
    @Test
    public void testExecuteBatch05() throws Exception {

        // テスト入力データ設定
        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000001");
        param.setJobArgNm1("test1");
        param.setJobArgNm2("test2");
        param.setJobArgNm3("test3");
        param.setJobArgNm4("test4");
        param.setJobArgNm5("test5");
        param.setJobArgNm6("test6");
        param.setJobArgNm7("test7");
        param.setJobArgNm8("test8");
        param.setJobArgNm9("test9");
        param.setJobArgNm10("test10");
        param.setJobArgNm11("test11");
        param.setJobArgNm12("test12");
        param.setJobArgNm13("test13");
        param.setJobArgNm14("test14");
        param.setJobArgNm15("test15");
        param.setJobArgNm16("test16");
        param.setJobArgNm17("test17");
        param.setJobArgNm18("test18");
        param.setJobArgNm19("test19");
        param.setJobArgNm20("test20");
        // テスト実施
        AbstractBatchExecutor batchExecutor = new SyncBatchExecutor();
        BLogicResult result = batchExecutor.executeBatch(param);

        // 結果検証
        assertEquals(0, result.getBlogicStatus());
    }

    /**
     * blogicBeanNameがB000001の場合、BLogicResultのリターンコードが0が返却されることを確認する
     * テストを実施するためには、ジョブBean定義ファイル[B000001.xml]とB000001BLogic.javaが必要となる
     * @throws Exception
     */
    @Test
    public void testExecuteBatch06() throws Exception {

        // テスト入力データ設定
        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000002");
        // テスト実施
        AbstractBatchExecutor batchExecutor = new SyncBatchExecutor();
        BLogicResult result = batchExecutor.executeBatch(param);

        // 結果検証
        assertEquals(1, result.getBlogicStatus());
    }

    /**
     * blogicBeanNameがB000001の場合、BLogicResultのリターンコードが0が返却されることを確認する
     * テストを実施するためには、ジョブBean定義ファイル[B000001.xml]とB000001BLogic.javaが必要となる
     * @throws Exception
     */
    @Test
    public void testExecuteBatch07() throws Exception {

        // テスト入力データ設定
        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000003");
        // テスト実施
        AbstractBatchExecutor batchExecutor = new SyncBatchExecutor();
        BLogicResult result = batchExecutor.executeBatch(param);

        // 結果検証
        assertEquals(255, result.getBlogicStatus());
    }

    /**
     * blogicBeanNameがB000001の場合、BLogicResultのリターンコードが0が返却されることを確認する
     * テストを実施するためには、ジョブBean定義ファイル[B000001.xml]とB000001BLogic.javaが必要となる
     * @throws Exception
     */
    @Test
    public void testExecuteBatch08() throws Exception {

        // テスト入力データ設定
        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000004");
        // テスト実施
        AbstractBatchExecutor batchExecutor = new SyncBatchExecutor();
        BLogicResult result = batchExecutor.executeBatch(param);

        // 結果検証
        assertEquals(100, result.getBlogicStatus());
    }

    /**
     * 「バッチジョブレコードデータをBLogicParamに変換する」でnullが返るパターン
     * @throws Exception
     */
    @Test
    public void testExecuteBatch09() throws Exception {

        // テスト入力データ設定
        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000004");
        // テスト実施
        AbstractBatchExecutor batchExecutor = new SyncBatchExecutor() {
            @Override
            protected BLogicParam convertBLogicParam(BatchJobData jobRecord) {
                return null;
            }
        };
        BLogicResult result = batchExecutor.executeBatch(param);

        // 結果検証
        assertEquals(255, result.getBlogicStatus());
    }

    /**
     * 「例外ハンドラのBean名取得」でnullが返るパターン
     * @throws Exception
     */
    @Test
    public void testExecuteBatch10() throws Exception {

        // テスト入力データ設定
        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000004");
        // テスト実施
        AbstractBatchExecutor batchExecutor = new SyncBatchExecutor() {
            @Override
            protected String getExceptionHandlerBeanName(String jobAppCd) {
                return null;
            }
        };
        BLogicResult result = batchExecutor.executeBatch(param);

        // 結果検証
        assertEquals(255, result.getBlogicStatus());
    }

    /**
     * 「例外ハンドラのBean名取得」で空の文字列が返るパターン
     * @throws Exception
     */
    @Test
    public void testExecuteBatch11() throws Exception {

        // テスト入力データ設定
        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000004");
        // テスト実施
        AbstractBatchExecutor batchExecutor = new SyncBatchExecutor() {
            @Override
            protected String getExceptionHandlerBeanName(String jobAppCd) {
                return "";
            }
        };
        BLogicResult result = batchExecutor.executeBatch(param);

        // 結果検証
        assertEquals(255, result.getBlogicStatus());
    }

    /**
     * testConvertBLogicParam001
     * @throws Exception
     */
    @Test
    public void testConvertBLogicParam001() throws Exception {
        // テスト入力データ設定
        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000004");
        param.setJobArgNm1("argJobArgNm1");
        param.setJobArgNm2("argJobArgNm2");
        param.setJobArgNm3("argJobArgNm3");
        param.setJobArgNm4("argJobArgNm4");
        param.setJobArgNm5("argJobArgNm5");
        param.setJobArgNm6("argJobArgNm6");
        param.setJobArgNm7("argJobArgNm7");
        param.setJobArgNm8("argJobArgNm8");
        param.setJobArgNm9("argJobArgNm9");
        param.setJobArgNm10("argJobArgNm10");
        param.setJobArgNm11("argJobArgNm11");
        param.setJobArgNm12("argJobArgNm12");
        param.setJobArgNm13("argJobArgNm13");
        param.setJobArgNm14("argJobArgNm14");
        param.setJobArgNm15("argJobArgNm15");
        param.setJobArgNm16("argJobArgNm16");
        param.setJobArgNm17("argJobArgNm17");
        param.setJobArgNm18("argJobArgNm18");
        param.setJobArgNm19("argJobArgNm19");
        param.setJobArgNm20("argJobArgNm20");

        // テスト実施
        AbstractBatchExecutor batchExecutor = new SyncBatchExecutor() {
        };
        BLogicParam result = batchExecutor.convertBLogicParam(param);

        // 結果検証
        assertNotNull(result);
        assertEquals("B000004", result.getJobAppCd());
        assertEquals("argJobArgNm1", result.getJobArgNm1());
        assertEquals("argJobArgNm2", result.getJobArgNm2());
        assertEquals("argJobArgNm3", result.getJobArgNm3());
        assertEquals("argJobArgNm4", result.getJobArgNm4());
        assertEquals("argJobArgNm5", result.getJobArgNm5());
        assertEquals("argJobArgNm6", result.getJobArgNm6());
        assertEquals("argJobArgNm7", result.getJobArgNm7());
        assertEquals("argJobArgNm8", result.getJobArgNm8());
        assertEquals("argJobArgNm9", result.getJobArgNm9());
        assertEquals("argJobArgNm10", result.getJobArgNm10());
        assertEquals("argJobArgNm11", result.getJobArgNm11());
        assertEquals("argJobArgNm12", result.getJobArgNm12());
        assertEquals("argJobArgNm13", result.getJobArgNm13());
        assertEquals("argJobArgNm14", result.getJobArgNm14());
        assertEquals("argJobArgNm15", result.getJobArgNm15());
        assertEquals("argJobArgNm16", result.getJobArgNm16());
        assertEquals("argJobArgNm17", result.getJobArgNm17());
        assertEquals("argJobArgNm18", result.getJobArgNm18());
        assertEquals("argJobArgNm19", result.getJobArgNm19());
        assertEquals("argJobArgNm20", result.getJobArgNm20());
    }

    /**
     * testConvertBLogicParam002
     * @throws Exception
     */
    @Test
    public void testConvertBLogicParam002() throws Exception {
        // テスト入力データ設定
        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000004");

        // テスト実施
        AbstractBatchExecutor batchExecutor = new SyncBatchExecutor() {
            @Override
            protected boolean argumentCopy(Object from, Object to, String field) {
                super.argumentCopy(from, to, field);
                return false;
            }
        };
        BLogicParam result = batchExecutor.convertBLogicParam(param);

        // 結果検証
        assertNull(result);
    }

    /**
     * testExecuteBatchClass001
     * @throws Exception
     */
    @Test
    public void testExecuteBatchClass001() throws Exception {
        String batchClassName = null;
        String exceptionHandlerBeanName = null;
        BLogicParam param = null;

        // テスト
        AbstractBatchExecutor exe = new SyncBatchExecutor();
        ApplicationContext context = null;
        BLogicResult result = exe.executeBatchClass(batchClassName,
                exceptionHandlerBeanName, param, context);

        assertEquals(255, result.getBlogicStatus());

    }

    /**
     * testExecuteBatchClass002
     * @throws Exception
     */
    @Test
    public void testExecuteBatchClass002() throws Exception {
        String batchClassName = "B000001BLogic";
        String exceptionHandlerBeanName = "B000001ExceptionHandler";
        BLogicParam param = null;

        // テスト
        AbstractBatchExecutor exe = new SyncBatchExecutor();
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "beansDef/B000001.xml");
        BLogicResult result = exe.executeBatchClass(batchClassName,
                exceptionHandlerBeanName, param, context);

        assertEquals(0, result.getBlogicStatus());

    }

    /**
     * testExecuteBatchClass003
     * @throws Exception
     */
    @Test
    public void testExecuteBatchClass003() throws Exception {
        String batchClassName = "B000000BLogic";
        String exceptionHandlerBeanName = "B000001ExceptionHandler";
        BLogicParam param = null;

        // テスト
        AbstractBatchExecutor exe = new SyncBatchExecutor();
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "beansDef/B000001.xml");
        BLogicResult result = exe.executeBatchClass(batchClassName,
                exceptionHandlerBeanName, param, context);

        assertEquals(255, result.getBlogicStatus());

    }

    /**
     * testExecuteBatchClass004
     * @throws Exception
     */
    @Test
    public void testExecuteBatchClass004() throws Exception {
        String batchClassName = "B000004BLogic";
        String exceptionHandlerBeanName = "B000004ExceptionHandler";
        BLogicParam param = null;

        // テスト
        AbstractBatchExecutor exe = new SyncBatchExecutor();
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "beansDef/B000004.xml");
        BLogicResult result = exe.executeBatchClass(batchClassName,
                exceptionHandlerBeanName, param, context);

        assertEquals(100, result.getBlogicStatus());

    }

    /**
     * testExecuteBatchClass005
     * @throws Exception
     */
    @Test
    public void testExecuteBatchClass005() throws Exception {
        String batchClassName = "B000000BLogic";
        String exceptionHandlerBeanName = "B000000ExceptionHandler";
        BLogicParam param = null;

        // テスト
        AbstractBatchExecutor exe = new SyncBatchExecutor();
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "beansDef/B000001.xml");
        BLogicResult result = exe.executeBatchClass(batchClassName,
                exceptionHandlerBeanName, param, context);

        assertEquals(255, result.getBlogicStatus());

    }

    /**
     * testExecuteBatchClass006
     * @throws Exception
     */
    @Test
    public void testExecuteBatchClass006() throws Exception {
        String batchClassName = "B000006BLogic";
        String exceptionHandlerBeanName = "B000006ExceptionHandler";
        BLogicParam param = new BLogicParam();
        param.setJobAppCd("B000006");

        // テスト
        AbstractBatchExecutor exe = new SyncBatchExecutor();
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "beansDef/B000006.xml");
        BLogicResult result = exe.executeBatchClass(batchClassName,
                exceptionHandlerBeanName, param, context);

        assertEquals(0, result.getBlogicStatus());

    }

    /**
     * testExecuteBatchClass007
     * @throws Exception
     */
    @Test
    public void testExecuteBatchClass007() throws Exception {
        String batchClassName = "B000007BLogic";
        String exceptionHandlerBeanName = "B000007ExceptionHandler";
        BLogicParam param = null;

        // テスト
        AbstractBatchExecutor exe = new SyncBatchExecutor();
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "beansDef/B000007.xml");
        BLogicResult result = exe.executeBatchClass(batchClassName,
                exceptionHandlerBeanName, param, context);

        assertEquals(100, result.getBlogicStatus());

    }

    /**
     * testExecuteBatchClass008
     * @throws Exception
     */
    @Test
    public void testExecuteBatchClass008() throws Exception {
        String batchClassName = "B000008BLogic";
        String exceptionHandlerBeanName = "B000008ExceptionHandler";
        BLogicParam param = null;

        // テスト
        AbstractBatchExecutor exe = new SyncBatchExecutor();
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "beansDef/B000008.xml");
        BLogicResult result = exe.executeBatchClass(batchClassName,
                exceptionHandlerBeanName, param, context);

        assertEquals(255, result.getBlogicStatus());

    }

    /**
     * testExecuteBatchClass009
     * @throws Exception
     */
    @Test
    public void testExecuteBatchClass009() throws Exception {
        String batchClassName = "B000009BLogic";
        String exceptionHandlerBeanName = "B000009ExceptionHandler";
        BLogicParam param = null;

        // テスト
        AbstractBatchExecutor exe = new SyncBatchExecutor();
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "beansDef/B000009.xml");
        BLogicResult result = exe.executeBatchClass(batchClassName,
                exceptionHandlerBeanName, param, context);

        assertEquals(255, result.getBlogicStatus());

    }

    /**
     * testExecuteBatchClass010
     * @throws Exception
     */
    @Test
    public void testExecuteBatchClass010() throws Exception {
        // JobComponentアノテーション有効化フラグプロパティを削除し、
        // JobComponentアノテーションを無効化する
        TerasolunaPropertyUtils.removeProperty("enableJobComponentAnnotation");

        String batchClassName = "B000010BLogic";
        String exceptionHandlerBeanName = "B000010ExceptionHandler";
        BLogicParam param = new BLogicParam();
        param.setJobAppCd("B000010");

        // テスト
        AbstractBatchExecutor exe = new SyncBatchExecutor();
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "beansDef/B000010.xml");
        BLogicResult result = exe.executeBatchClass(batchClassName,
                exceptionHandlerBeanName, param, context);

        // JobComponentアノテーション無効化によりジョブが実行されない
        assertEquals(255, result.getBlogicStatus());

    }

    /**
     * testArgumentCopy001
     * @throws Exception
     */
    @Test
    public void testArgumentCopy001() throws Exception {
        AbstractBatchExecutor exe = new SyncBatchExecutor();

        Object from = null;
        Object to = null;
        String field = null;

        // テスト
        boolean result = exe.argumentCopy(from, to, field);

        assertTrue(result);
    }

    /**
     * testArgumentCopy002
     * @throws Exception
     */
    @Test
    public void testArgumentCopy002() throws Exception {
        AbstractBatchExecutor exe = new SyncBatchExecutor();

        HogeBean from = new HogeBean();
        from.setHoge1("hoge1");
        Object to = null;
        String field = "Hoge";

        // テスト
        boolean result = exe.argumentCopy(from, to, field);

        assertFalse(result);
    }

    @Test
    public void testSetMethod01() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();
        boolean result = exe.setMethod(null, "test", "test");

        assertEquals(false, result);

    }

    @Test
    public void testGetMethod01() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();
        Object result = exe.getMethod(null, "test1");

        assertNull(result);

    }

    @Test
    public void testGetDefaultApplicationContext01() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();
        ApplicationContext result = exe.getDefaultApplicationContext();

        assertNotNull(result);

    }

    @Test
    public void testGetSystemDao01() throws Exception {

        AbstractBatchExecutor exe = new AbstractBatchExecutor() {};

        SystemDao result = exe.getSystemDao();

        assertNull(result);
    }

    @Test
    public void testGetSystemDao02() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();

        SystemDao result = exe.getSystemDao();

        assertNull(result);
    }

    @Test
    public void testGetSysTransactionManager01() throws Exception {

        AbstractBatchExecutor exe = new AbstractBatchExecutor() {};

        PlatformTransactionManager result = exe.getSysTransactionManager();

        assertNull(result);
    }

    @Test
    public void testGetSysTransactionManager02() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();

        PlatformTransactionManager result = exe.getSysTransactionManager();

        assertNull(result);
    }

    @Before
    public void setUp() {
        TerasolunaPropertyUtils.saveProperties();
    }

    @After
    public void tearDown() {
        TerasolunaPropertyUtils.restoreProperties();
    }

    @Test
    public void testGetDefaultBeanFileName01() throws Exception {

        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.classpath");
        TerasolunaPropertyUtils.removeProperty("beanDefinition.admin.default");

        String result = AbstractBatchExecutor.getDefaultBeanFileName();

        assertEquals("", result);
    }

    @Test
    public void testGetDefaultBeanFileName02() throws Exception {

        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.classpath");
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.classpath",
                "beansDef/");
        TerasolunaPropertyUtils.removeProperty("beanDefinition.admin.default");

        String result = AbstractBatchExecutor.getDefaultBeanFileName();

        assertEquals("beansDef/", result);
    }

    @Test
    public void testGetDefaultBeanFileName03() throws Exception {

        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.classpath");
        TerasolunaPropertyUtils.removeProperty("beanDefinition.admin.default");
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.default",
                "AdminContext.xml");

        String result = AbstractBatchExecutor.getDefaultBeanFileName();

        assertEquals("AdminContext.xml", result);
    }

    @Test
    public void testGetDefaultBeanFileName04() throws Exception {

        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.classpath");
        TerasolunaPropertyUtils.removeProperty("beanDefinition.admin.default");
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.classpath",
                "beansDef/");
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.default",
                "AdminContext.xml");

        String result = AbstractBatchExecutor.getDefaultBeanFileName();

        assertEquals("beansDef/AdminContext.xml", result);
    }

    @Test
    public void testGetDefaultBeanFileName05() throws Exception {

        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.classpath");
        TerasolunaPropertyUtils.removeProperty("beanDefinition.admin.default");
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.classpath",
                null);
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.default",
                null);

        String result = AbstractBatchExecutor.getDefaultBeanFileName();

        assertEquals("", result);
    }

    @Test
    public void testGetDataSourceBeanFileName01() throws Exception {

        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.classpath");
        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.dataSource");

        String result = AbstractBatchExecutor.getDataSourceBeanFileName();

        assertEquals("", result);
    }

    @Test
    public void testGetDataSourceBeanFileName02() throws Exception {

        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.classpath");
        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.dataSource");
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.classpath",
                "beansDef/");

        String result = AbstractBatchExecutor.getDataSourceBeanFileName();

        assertEquals("beansDef/", result);
    }

    @Test
    public void testGetDataSourceBeanFileName03() throws Exception {

        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.classpath");
        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.dataSource");
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.dataSource",
                "AdminDataSource.xml");

        String result = AbstractBatchExecutor.getDataSourceBeanFileName();

        assertEquals("AdminDataSource.xml", result);
    }

    @Test
    public void testGetDataSourceBeanFileName04() throws Exception {

        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.classpath");
        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.dataSource");
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.classpath",
                "beansDef/");
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.dataSource",
                "AdminDataSource.xml");

        String result = AbstractBatchExecutor.getDataSourceBeanFileName();

        assertEquals("beansDef/AdminDataSource.xml", result);
    }

    @Test
    public void testGetDataSourceBeanFileName05() throws Exception {

        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.classpath");
        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.dataSource");
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.classpath",
                null);
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.dataSource",
                null);

        String result = AbstractBatchExecutor.getDataSourceBeanFileName();

        assertEquals("", result);
    }

    /**
     * testInitDefaultAppContext01
     * @throws Exception
     */
    @Test
    public void testInitDefaultAppContext01() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();

        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.admin.classpath");
        TerasolunaPropertyUtils.removeProperty("beanDefinition.admin.default");

        exe.initDefaultAppContext();
    }

    /**
     * testInitDefaultAppContext02
     * @throws Exception
     */
    @Test
    public void testInitDefaultAppContext02() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();

        exe.initDefaultAppContext();
    }

    /**
     * testInitDefaultAppContext03
     * @throws Exception
     */
    @Test
    public void testInitDefaultAppContext03() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty("beanDefinition.admin.default");
        TerasolunaPropertyUtils.addProperty("beanDefinition.admin.default",
                "hoge");

        exe.initDefaultAppContext();
    }

    /**
     * testInitDefaultErrorMessage01
     * @throws Exception
     */
    @Test
    public void testInitDefaultErrorMessage01() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();

        exe.defaultApplicationContext = null;

        exe.initDefaultErrorMessage();

    }

    /**
     * testInitDefaultErrorMessage02
     * @throws Exception
     */
    @Test
    public void testInitDefaultErrorMessage02() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty("messageAccessor.default");

        exe.initDefaultErrorMessage();

    }

    /**
     * testInitDefaultErrorMessage03
     * @throws Exception
     */
    @Test
    public void testInitDefaultErrorMessage03() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty("messageAccessor.default");
        TerasolunaPropertyUtils.addProperty("messageAccessor.default", null);

        exe.initDefaultErrorMessage();

    }

    /**
     * testInitDefaultErrorMessage04
     * @throws Exception
     */
    @Test
    public void testInitDefaultErrorMessage04() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty("messageAccessor.default");
        TerasolunaPropertyUtils
                .addProperty("messageAccessor.default", "msgAcc");

        exe.initDefaultErrorMessage();

    }

    @Test
    public void testGetBeanFileName01() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();
        String result = exe.getBeanFileName("", null);
        assertEquals("beansDef/.xml", result);
    }

    @Test
    public void testGetBeanFileName02() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();
        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.business.classpath");
        String result = exe.getBeanFileName("", null);

        assertEquals(".xml", result);
    }

    @Test
    public void testGetBeanFileName03() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();
        TerasolunaPropertyUtils
                .removeProperty("beanDefinition.business.classpath");
        String result = exe.getBeanFileName(null, null);

        assertEquals(".xml", result);
    }

    @Test
    public void testGetApplicationContext01() throws Exception {

        String[] batchBeanFileName = { "beansDef/B000000.xml" };

        ApplicationContext result = AbstractBatchExecutor
                .getApplicationContext(batchBeanFileName);

        assertNull(result);

    }

    @Test
    public void testGetApplicationContext02() throws Exception {

        String[] batchBeanFileName = { "beansDef/B000001.xml" };

        ApplicationContext result = AbstractBatchExecutor
                .getApplicationContext(batchBeanFileName);

        assertNotNull(result);
    }

    @Test
    public void testGetApplicationContext03() throws Exception {

        String[] batchBeanFileName = { "beansDef/B000001.xml",
                "beansDef/B000002.xml", "beansDef/B000003.xml" };

        ApplicationContext result = AbstractBatchExecutor
                .getApplicationContext(batchBeanFileName);

        assertNotNull(result);
    }

    @Test
    public void testGetApplicationContext04() throws Exception {

        String[] batchBeanFileName = { "beansDef/B000000.xml",
                "beansDef/B000001.xml", "beansDef/B000002.xml" };

        ApplicationContext result = AbstractBatchExecutor
                .getApplicationContext(batchBeanFileName);

        assertNull(result);

        // ログの確認をする
        // java.lang.reflect.InvocationTargetExceptionがスローされていること

    }

    @Test
    public void testGetApplicationContext05() throws Exception {

        String[] batchBeanFileName = { "beansDef/B000000.xml",
                "beansDef/B000001.xml", "beansDef/B000002.xml" };

        ClassLoader mockLoader = mock(ClassLoader.class);

        when(
                mockLoader
                        .loadClass("org.springframework.context.support.ClassPathXmlApplicationContext"))
                .thenThrow(new ClassNotFoundException());

        ClassLoader clBackup = AbstractBatchExecutor.cl;
        AbstractBatchExecutor.cl = mockLoader;
        ApplicationContext result = AbstractBatchExecutor
                .getApplicationContext(batchBeanFileName);

        assertNull(result);

        AbstractBatchExecutor.cl = clBackup;
        // ログの確認をする
        // ClassNotFoundExceptionがスローされていること

    }

    @Test
    public void testGetBlogicBeanName01() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();
        String result = exe.getBlogicBeanName("B000000");

        assertEquals("B000000BLogic", result);
    }

    @Test
    public void testGetBlogicBeanName02() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();
        String result = exe.getBlogicBeanName(null);

        assertEquals("", result);
    }

    @Test
    public void testGetBlogicBeanName03() throws Exception {

        AbstractBatchExecutor exe = new SyncBatchExecutor();
        String result = exe.getBlogicBeanName("");

        assertEquals("", result);
    }

    @Test
    public void testReplaceString01() throws Exception {
        AbstractBatchExecutor exe = new SyncBatchExecutor();
        BatchJobData jobData = new BatchJobData();
        String replaceString = exe.replaceString("value", null, jobData);
        assertEquals("value", replaceString);
    }

    @Test
    public void testReplaceString02() throws Exception {
        AbstractBatchExecutor exe = new SyncBatchExecutor();
        BatchJobData jobData = new BatchJobData();
        String replaceString = exe.replaceString("", "jobAppCd", jobData);
        assertEquals("", replaceString);
    }
}
