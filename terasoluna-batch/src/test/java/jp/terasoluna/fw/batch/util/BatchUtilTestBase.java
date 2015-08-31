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

package jp.terasoluna.fw.batch.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.terasoluna.fw.batch.exception.IllegalClassTypeException;

import org.apache.commons.logging.Log;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

/**
 * 
 * 
 */
public abstract class BatchUtilTestBase {
    // private Log log = LogFactory.getLog(BatchUtilTest.class);
    private Log log = getLog();

    abstract Log getLog();

    /**
     * testBatchUtil001
     * @throws Exception
     */
    @Test
    public void testBatchUtil001() throws Exception {
        BatchUtil bu = new BatchUtil();
        assertNotNull(bu);
    }

    /**
     * 文字列結合メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 入力された文字列配列の内容が正常に結合されるかどうかを確認する。 <br>
     * 確認項目：<br>
     * 結合された文字列が返却されること<br>
     * @throws Exception
     */
    @Test
    public void testCat01() throws Exception {

        // テスト入力データ設定
        String[] args = { "test1", "test2" };

        // テスト実施
        String result = BatchUtil.cat((Object[]) args);

        // 結果検証
        assertEquals("test1test2", result);
    }

    /**
     * 文字列結合メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * nullが設定された場合nullを返却することを確認する。 <br>
     * 確認項目：<br>
     * nullが返却されること<br>
     * @throws Exception
     */
    @Test
    public void testCat02() throws Exception {

        // テスト入力データ設定
        String[] args = null;

        // テスト実施
        String result = BatchUtil.cat((Object[]) args);

        // 結果検証
        assertNull(result);
    }

    /**
     * 文字列結合メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 空文字が設定された場合、空文字を返却することを確認する。 <br>
     * 確認項目：<br>
     * 空文字が返却されること<br>
     * @throws Exception
     */
    @Test
    public void testCat03() throws Exception {

        // テスト入力データ設定
        String[] args = { "" };

        // テスト実施
        String result = BatchUtil.cat((Object[]) args);

        // 結果検証
        assertEquals("", result);
    }

    /**
     * 文字列結合メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 3つ以上の文字列が設定されており、間の文字列にnullが含まれている場合、nullは飛ばして結合した文字列を返却することを確認する。 <br>
     * 確認項目：<br>
     * nullを飛ばした結合文字列が返却されること<br>
     * @throws Exception
     */
    @Test
    public void testCat04() throws Exception {

        // テスト入力データ設定
        String[] args = { "test1", null, "test2" };

        // テスト実施
        String result = BatchUtil.cat((Object[]) args);

        // 結果検証
        assertEquals("test1test2", result);
    }

    /**
     * インフォログの開始メッセージを取得するメソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 入力に文字列を与えた時、想定したログ文字列が返却されることを確認する。<br>
     * 確認項目：<br>
     * 入力文字列が埋め込まれたログ文字列が返却されること<br>
     * @throws Exception
     */
    @Test
    public void testGetInfoLogStartMsg01() throws Exception {

        // テスト入力データ設定
        String arg = "test1";

        // テスト実施
        String result = BatchUtil.getInfoLogStartMsg(arg);

        // 結果検証
        assertEquals("[test1] 処理開始", result);
    }

    /**
     * インフォログの開始メッセージを取得するメソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 入力にnullを与えた時、想定したログ文字列が返却されることを確認する。<br>
     * 確認項目：<br>
     * 入力文字列が埋め込まれないログ文字列が返却されること<br>
     * @throws Exception
     */
    @Test
    public void testGetInfoLogStartMsg02() throws Exception {

        // テスト入力データ設定
        String arg = null;

        // テスト実施
        String result = BatchUtil.getInfoLogStartMsg(arg);

        // 結果検証
        assertEquals("[] 処理開始", result);
    }

    /**
     * インフォログの開始メッセージを取得するメソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 入力に空文字を与えた時、想定したログ文字列が返却されることを確認する。<br>
     * 確認項目：<br>
     * 入力文字列が埋め込まれないログ文字列が返却されること<br>
     * @throws Exception
     */
    @Test
    public void testGetInfoLogStartMsg03() throws Exception {

        // テスト入力データ設定
        String arg = "";

        // テスト実施
        String result = BatchUtil.getInfoLogStartMsg(arg);

        // 結果検証
        assertEquals("[] 処理開始", result);
    }

    /**
     * インフォログの終了メッセージを取得するメソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 入力に文字列を与えた時、想定したログ文字列が返却されることを確認する。<br>
     * 確認項目：<br>
     * 入力文字列が埋め込まれたログ文字列が返却されること<br>
     * @throws Exception
     */
    @Test
    public void testGetInfoLogEndMsg01() throws Exception {

        // テスト入力データ設定
        String arg = "test1";

        // テスト実施
        String result = BatchUtil.getInfoLogEndMsg(arg);

        // 結果検証
        assertEquals("[test1] 処理終了", result);
    }

    /**
     * インフォログの終了メッセージを取得するメソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 入力に空文字を与えた時、想定したログ文字列が返却されることを確認する。<br>
     * 確認項目：<br>
     * 入力文字列が埋め込まれないログ文字列が返却されること<br>
     * @throws Exception
     */
    @Test
    public void testGetInfoLogEndMsg02() throws Exception {

        // テスト入力データ設定
        String arg = null;

        // テスト実施
        String result = BatchUtil.getInfoLogEndMsg(arg);

        // 結果検証
        assertEquals("[] 処理終了", result);
    }

    /**
     * インフォログの終了メッセージを取得するメソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 入力に空文字を与えた時、想定したログ文字列が返却されることを確認する。<br>
     * 確認項目：<br>
     * 入力文字列が埋め込まれないログ文字列が返却されること<br>
     * @throws Exception
     */
    @Test
    public void testGetInfoLogEndMsg03() throws Exception {

        // テスト入力データ設定
        String arg = "";

        // テスト実施
        String result = BatchUtil.getInfoLogEndMsg(arg);

        // 結果検証
        assertEquals("[] 処理終了", result);
    }

    /**
     * testGetTransactionDefinition01
     * @throws Exception
     */
    @Test
    public void testGetTransactionDefinition01() throws Exception {

        // テスト実施
        TransactionDefinition result = BatchUtil.getTransactionDefinition();

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testGetTransactionDefinition02
     * @throws Exception
     */
    @Test
    public void testGetTransactionDefinition02() throws Exception {

        // テスト実施
        TransactionDefinition result = BatchUtil.getTransactionDefinition(
                TransactionDefinition.PROPAGATION_REQUIRED,
                TransactionDefinition.ISOLATION_DEFAULT,
                TransactionDefinition.TIMEOUT_DEFAULT, false);

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testChangeListToArray01
     * @throws Exception
     */
    @Test
    public void testChangeListToArray01() throws Exception {

        // テスト入力データ設定
        List<String> list = new ArrayList<String>();
        list.add("test1");
        list.add("test2");
        list.add("test3");

        // テスト実施
        String[] result = BatchUtil.changeListToArray(list, String.class);

        // 結果検証
        assertEquals("test1", result[0]);
        assertEquals("test2", result[1]);
        assertEquals("test3", result[2]);
    }

    /**
     * testChangeListToArray02
     * @throws Exception
     */
    @Test
    public void testChangeListToArray02() throws Exception {

        // テスト入力データ設定
        List<String> list = new ArrayList<String>();
        list.add("test1");
        list.add("test2");
        list.add("test3");
        try {

            // テスト実施
            @SuppressWarnings("unused")
            String[] result = BatchUtil.changeListToArray(list, null);

            // 結果検証
            fail();
        } catch (IllegalClassTypeException e) {
            assertNotNull(e);
        }

    }

    /**
     * testChangeListToArray03
     * @throws Exception
     */
    @Test
    public void testChangeListToArray03() throws Exception {

        // テスト入力データ設定
        List<String> list = new ArrayList<String>();
        list.add("test1");
        list.add("test2");
        list.add("test3");
        try {
            // テスト実施
            @SuppressWarnings("unused")
            String[] result = BatchUtil.changeListToArray(list, Integer.class);

            // 結果検証
            fail();
        } catch (IllegalClassTypeException e) {
            assertNotNull(e);
        }

    }

    /**
     * testGetProperties01
     * @throws Exception
     */
    @Test
    public void testGetProperties01() throws Exception {

        // テスト実施
        List<String> result = BatchUtil.getProperties("messages", "errors");

        // 結果検証
        assertEquals(26, result.size());
    }

    /**
     * testStartTransaction01
     * @throws Exception
     */
    @Test
    public void testStartTransaction01() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = null;

        // テスト実施
        TransactionStatus result = BatchUtil.startTransaction(tran);

        // 結果検証
        assertNull(result);
    }

    /**
     * testStartTransaction02
     * @throws Exception
     */
    @Test
    public void testStartTransaction02() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();

        // テスト実施
        TransactionStatus result = BatchUtil.startTransaction(tran);

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testStartTransaction03
     * @throws Exception
     */
    @Test
    public void testStartTransaction03() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();

        // テスト実施
        TransactionStatus result = BatchUtil.startTransaction(tran, log);

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testStartTransaction04
     * @throws Exception
     */
    @Test
    public void testStartTransaction04() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();

        // テスト実施
        TransactionStatus result = BatchUtil.startTransaction(tran,
                TransactionDefinition.PROPAGATION_REQUIRED,
                TransactionDefinition.ISOLATION_DEFAULT,
                TransactionDefinition.TIMEOUT_DEFAULT, false);

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testStartTransaction05
     * @throws Exception
     */
    @Test
    public void testStartTransaction05() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();

        // テスト実施
        TransactionStatus result = BatchUtil.startTransaction(tran,
                TransactionDefinition.PROPAGATION_REQUIRED,
                TransactionDefinition.ISOLATION_DEFAULT,
                TransactionDefinition.TIMEOUT_DEFAULT, false, log);

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testStartTransaction06
     * @throws Exception
     */
    @Test
    public void testStartTransaction06() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionDefinition def = BatchUtil.getTransactionDefinition();

        // テスト実施
        TransactionStatus result = BatchUtil.startTransaction(tran, def);

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testStartTransaction07
     * @throws Exception
     */
    @Test
    public void testStartTransaction07() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionDefinition def = BatchUtil.getTransactionDefinition();

        // テスト実施
        TransactionStatus result = BatchUtil.startTransaction(tran, def, log);

        // 結果検証
        assertNotNull(result);
    }

    @Test
    public void testStartTransaction08() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionDefinition def = BatchUtil.getTransactionDefinition();

        // テスト実施
        TransactionStatus result = BatchUtil.startTransaction(tran, null, log);

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testStartTransactions01
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testStartTransactions01() throws Exception {
        // テスト入力データ設定
        TransactionDefinition def = BatchUtil.getTransactionDefinition();
        Map<String, PlatformTransactionManager> tranMap = new HashMap();
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        tranMap.put("tran", tran);

        // テスト実施
        Map<String, TransactionStatus> result = BatchUtil.startTransactions(
                def, tranMap);

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testStartTransactions02
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testStartTransactions02() throws Exception {
        // テスト入力データ設定
        TransactionDefinition def = BatchUtil.getTransactionDefinition();
        Map<String, PlatformTransactionManager> tranMap = new HashMap();
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        tranMap.put("tran", tran);

        // テスト実施
        Map<String, TransactionStatus> result = BatchUtil.startTransactions(
                def, tranMap, log);

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testStartTransactions03
     * @throws Exception
     */
    @Test
    public void testStartTransactions03() throws Exception {
        // テスト入力データ設定
        TransactionDefinition def = BatchUtil.getTransactionDefinition();
        Map<String, PlatformTransactionManager> tranMap = new LinkedHashMap<String, PlatformTransactionManager>();
        PlatformTransactionManagerStub3 tran1 = new PlatformTransactionManagerStub3();
        tran1.setFailStartTx(true);
        tranMap.put("tran1", tran1);
        PlatformTransactionManagerStub3 tran2 = new PlatformTransactionManagerStub3();
        tranMap.put("tran2", tran2);
        PlatformTransactionManagerStub3 tran3 = new PlatformTransactionManagerStub3();
        tranMap.put("tran3", tran3);
        PlatformTransactionManagerStub3 tran4 = new PlatformTransactionManagerStub3();
        tranMap.put("tran4", tran4);

        // テスト実施
        try {
            BatchUtil.startTransactions(def, tranMap, log);
            fail();
        } catch (TransactionException e) {
            // 結果検証
            assertTrue(tran1.wasCalledGetTransaction());
            assertFalse(tran2.wasCalledGetTransaction());
            assertFalse(tran3.wasCalledGetTransaction());
            assertFalse(tran4.wasCalledGetTransaction());
            assertFalse(tran1.wasCalledRollback());
            assertFalse(tran2.wasCalledRollback());
            assertFalse(tran3.wasCalledRollback());
            assertFalse(tran4.wasCalledRollback());
        }
    }

    /**
     * testStartTransactions04
     * @throws Exception
     */
    @Test
    public void testStartTransactions04() throws Exception {
        // テスト入力データ設定
        TransactionDefinition def = BatchUtil.getTransactionDefinition();
        Map<String, PlatformTransactionManager> tranMap = new LinkedHashMap<String, PlatformTransactionManager>();
        PlatformTransactionManagerStub3 tran1 = new PlatformTransactionManagerStub3();
        tranMap.put("tran1", tran1);
        PlatformTransactionManagerStub3 tran2 = new PlatformTransactionManagerStub3();
        tran2.setFailStartTx(true);
        tranMap.put("tran2", tran2);
        PlatformTransactionManagerStub3 tran3 = new PlatformTransactionManagerStub3();
        tranMap.put("tran3", tran3);
        PlatformTransactionManagerStub3 tran4 = new PlatformTransactionManagerStub3();
        tranMap.put("tran4", tran4);

        // テスト実施
        try {
            BatchUtil.startTransactions(def, tranMap, log);
            fail();
        } catch (TransactionException e) {
            // 結果検証
            assertTrue(tran1.wasCalledGetTransaction());
            assertTrue(tran2.wasCalledGetTransaction());
            assertFalse(tran3.wasCalledGetTransaction());
            assertFalse(tran4.wasCalledGetTransaction());
            assertTrue(tran1.wasCalledRollback());
            assertFalse(tran2.wasCalledRollback());
            assertFalse(tran3.wasCalledRollback());
            assertFalse(tran4.wasCalledRollback());
        }
    }

    /**
     * testStartTransactions05
     * @throws Exception
     */
    @Test
    public void testStartTransactions05() throws Exception {
        // テスト入力データ設定
        TransactionDefinition def = BatchUtil.getTransactionDefinition();
        Map<String, PlatformTransactionManager> tranMap = new LinkedHashMap<String, PlatformTransactionManager>();
        PlatformTransactionManagerStub3 tran1 = new PlatformTransactionManagerStub3();
        tranMap.put("tran1", tran1);
        PlatformTransactionManagerStub3 tran2 = new PlatformTransactionManagerStub3();
        tranMap.put("tran2", tran2);
        PlatformTransactionManagerStub3 tran3 = new PlatformTransactionManagerStub3();
        tranMap.put("tran3", tran3);
        PlatformTransactionManagerStub3 tran4 = new PlatformTransactionManagerStub3();
        tran4.setFailStartTx(true);
        tranMap.put("tran4", tran4);

        // テスト実施
        try {
            BatchUtil.startTransactions(def, tranMap, log);
            fail();
        } catch (TransactionException e) {
            // 結果検証
            assertTrue(tran1.wasCalledGetTransaction());
            assertTrue(tran2.wasCalledGetTransaction());
            assertTrue(tran3.wasCalledGetTransaction());
            assertTrue(tran4.wasCalledGetTransaction());
            assertTrue(tran1.wasCalledRollback());
            assertTrue(tran2.wasCalledRollback());
            assertTrue(tran3.wasCalledRollback());
            assertFalse(tran4.wasCalledRollback());
        }
    }

    /**
     * testStartTransactions06
     * @throws Exception
     */
    @Test
    public void testStartTransactions06() throws Exception {
        // テスト入力データ設定
        TransactionDefinition def = BatchUtil.getTransactionDefinition();
        Map<String, PlatformTransactionManager> tranMap = new LinkedHashMap<String, PlatformTransactionManager>();
        PlatformTransactionManagerStub3 tran1 = new PlatformTransactionManagerStub3();
        tranMap.put("tran1", tran1);
        PlatformTransactionManagerStub3 tran2 = new PlatformTransactionManagerStub3();
        tranMap.put("tran2", tran2);
        PlatformTransactionManagerStub3 tran3 = new PlatformTransactionManagerStub3();
        tranMap.put("tran3", tran3);
        PlatformTransactionManagerStub3 tran4 = new PlatformTransactionManagerStub3();
        tranMap.put("tran4", tran4);

        // テスト実施
        Map<?,?> statMap = BatchUtil.startTransactions(def, tranMap, log);
        // 結果検証
        assertTrue(statMap instanceof LinkedHashMap);
        assertTrue(tran1.wasCalledGetTransaction());
        assertTrue(tran2.wasCalledGetTransaction());
        assertTrue(tran3.wasCalledGetTransaction());
        assertTrue(tran4.wasCalledGetTransaction());
        assertFalse(tran1.wasCalledRollback());
        assertFalse(tran2.wasCalledRollback());
        assertFalse(tran3.wasCalledRollback());
        assertFalse(tran4.wasCalledRollback());
    }

    /**
     * testCommitTransaction01
     * @throws Exception
     */
    @Test
    public void testCommitTransaction01() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();

        // テスト実施
        try {
            BatchUtil.commitTransaction(tran, stat);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
    }

    /**
     * testCommitTransaction02
     * @throws Exception
     */
    @Test
    public void testCommitTransaction02() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();

        // テスト実施
        try {
            BatchUtil.commitTransaction(tran, stat, log);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
    }

    /**
     * testCommitTransactions01
     * @throws Exception
     */
    @Test
    public void testCommitTransactions01() throws Exception {
        // テスト入力データ設定
        Map<String, PlatformTransactionManager> tranMap = new HashMap<String, PlatformTransactionManager>();
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        tranMap.put("tran", tran);
        Map<String, TransactionStatus> statMap = new HashMap<String, TransactionStatus>();

        // テスト実施
        try {
            BatchUtil.commitTransactions(tranMap, statMap);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
    }

    /**
     * testCommitTransactions02
     * @throws Exception
     */
    @Test
    public void testCommitTransactions02() throws Exception {
        // テスト入力データ設定
        Map<String, PlatformTransactionManager> tranMap = new HashMap<String, PlatformTransactionManager>();
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        tranMap.put("tran", tran);
        Map<String, TransactionStatus> statMap = new HashMap<String, TransactionStatus>();
        statMap.put("tran", new TransactionStatusStub());

        // テスト実施
        try {
            BatchUtil.commitTransactions(tranMap, statMap, log);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
    }

    /**
     * testCommitTransactions03
     * @throws Exception
     */
    @Test
    public void testCommitTransaction03() throws Exception {
        // テスト入力データ設定
        Map<String, PlatformTransactionManager> tranMap = new HashMap<String, PlatformTransactionManager>();
        PlatformTransactionManagerStub4 tran = new PlatformTransactionManagerStub4();
        tranMap.put("tran1", tran);
        tranMap.put("tran2", tran);
        tranMap.put("tran3", tran);
        tranMap.put("tran4", tran);
        Map<String, TransactionStatus> statMap = new LinkedHashMap<String, TransactionStatus>();
        TransactionStatusStub status1 = new TransactionStatusStub();
        status1.setName("stat1");
        TransactionStatusStub status2 = new TransactionStatusStub();
        status2.setName("stat2");
        TransactionStatusStub status3 = new TransactionStatusStub();
        status3.setName("stat3");
        TransactionStatusStub status4 = new TransactionStatusStub();
        status4.setName("stat4");
        // LinkedHashMapに昇順でtransactionStatusを追加。
        statMap.put("tran1", status1);
        statMap.put("tran2", status2);
        statMap.put("tran3", status3);
        statMap.put("tran4", status4);

        // テスト実施
        BatchUtil.commitTransactions(tranMap, statMap, log);
        
        // 結果検証
        List<TransactionStatus> statusList = tran.getStatusList();
        // statMapのput順と逆順にコミットされていること。
        assertEquals("stat4", ((TransactionStatusStub) statusList.get(0)).getName());
        assertEquals("stat3", ((TransactionStatusStub) statusList.get(1)).getName());
        assertEquals("stat2", ((TransactionStatusStub) statusList.get(2)).getName());
        assertEquals("stat1", ((TransactionStatusStub) statusList.get(3)).getName());
    }

    /**
     * testEndTransaction01
     * @throws Exception
     */
    @Test
    public void testEndTransaction01() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();

        // テスト実施
        try {
            BatchUtil.endTransaction(tran, stat);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
    }

    /**
     * testEndTransaction02
     * @throws Exception
     */
    @Test
    public void testEndTransaction02() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();

        // テスト実施
        try {
            BatchUtil.endTransaction(tran, stat, log);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
    }

    /**
     * testEndTransactions01
     * @throws Exception
     */
    @Test
    public void testEndTransactions01() throws Exception {
        // テスト入力データ設定
        Map<String, PlatformTransactionManager> tranMap = new HashMap<String, PlatformTransactionManager>();
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        tranMap.put("tran", tran);
        Map<String, TransactionStatus> statMap = new HashMap<String, TransactionStatus>();
        statMap.put("tran", new TransactionStatusStub());

        // テスト実施
        boolean result = false;
        try {
            result = BatchUtil.endTransactions(tranMap, statMap);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
        assertTrue(result);
    }

    /**
     * testEndTransactions02
     * @throws Exception
     */
    @Test
    public void testEndTransactions02() throws Exception {
        // テスト入力データ設定
        Map<String, PlatformTransactionManager> tranMap = new HashMap<String, PlatformTransactionManager>();
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        PlatformTransactionManager tran2 = new PlatformTransactionManagerStub2();
        tranMap.put("tran", tran);
        tranMap.put("tran2", tran2);
        Map<String, TransactionStatus> statMap = new HashMap<String, TransactionStatus>();
        statMap.put("tran", new TransactionStatusStub());
        statMap.put("tran2", new TransactionStatusStub());

        // テスト実施
        boolean result = true;
        try {
            result = BatchUtil.endTransactions(tranMap, statMap, log);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
        assertFalse(result);
    }

    /**
     * testEndTransactions03
     * @throws Exception
     */
    @Test
    public void testEndTransactions03() throws Exception {
        // テスト入力データ設定
        Map<String, PlatformTransactionManager> tranMap = new HashMap<String, PlatformTransactionManager>();
        PlatformTransactionManagerStub4 tran = new PlatformTransactionManagerStub4();
        tranMap.put("tran1", tran);
        tranMap.put("tran2", tran);
        tranMap.put("tran3", tran);
        tranMap.put("tran4", tran);

        // TransactionStatusは全てロールバックされることを想定し、順序がtranMapのputと逆順で実施されること。
        Map<String, TransactionStatus> statMap = new LinkedHashMap<String, TransactionStatus>();
        TransactionStatusStub stat1 = new TransactionStatusStub();
        stat1.setName("stat1");
        stat1.setRollbackOnly();

        TransactionStatusStub stat2 = new TransactionStatusStub();
        stat2.setName("stat2");
        stat2.setRollbackOnly();

        TransactionStatusStub stat3 = new TransactionStatusStub();
        stat3.setName("stat3");
        stat3.setRollbackOnly();

        TransactionStatusStub stat4 = new TransactionStatusStub();
        stat4.setName("stat4");
        stat4.setRollbackOnly();

        statMap.put("tran1", stat1);
        statMap.put("tran2", stat2);
        statMap.put("tran3", stat3);
        statMap.put("tran4", stat4);

        // テスト実施
        boolean result = BatchUtil.endTransactions(tranMap, statMap, log);

        // 結果検証
        assertTrue(result);
        List<TransactionStatus> statList = tran.getStatusList();
        // statMapのputと逆順でロールバックされていること。
        assertEquals("stat4", ((TransactionStatusStub) statList.get(0)).getName());
        assertEquals("stat3", ((TransactionStatusStub) statList.get(1)).getName());
        assertEquals("stat2", ((TransactionStatusStub) statList.get(2)).getName());
        assertEquals("stat1", ((TransactionStatusStub) statList.get(3)).getName());
    }

    /**
     * testSetSavepoint01
     * @throws Exception
     */
    @Test
    public void testSetSavepoint01() throws Exception {
        // テスト入力データ設定

        // テスト実施
        Object result = null;
        try {
            result = BatchUtil.setSavepoint(new TransactionStatusStub());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testSetSavepoint02
     * @throws Exception
     */
    @Test
    public void testSetSavepoint02() throws Exception {
        // テスト入力データ設定

        // テスト実施
        Object result = null;
        try {
            result = BatchUtil.setSavepoint(new TransactionStatusStub(), log);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testReleaseSavepoint01
     * @throws Exception
     */
    @Test
    public void testReleaseSavepoint01() throws Exception {
        // テスト入力データ設定

        // テスト実施
        try {
            BatchUtil.releaseSavepoint(new TransactionStatusStub(),
                    new Object());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
    }

    /**
     * testReleaseSavepoint02
     * @throws Exception
     */
    @Test
    public void testReleaseSavepoint02() throws Exception {
        // テスト入力データ設定

        // テスト実施
        try {
            BatchUtil.releaseSavepoint(new TransactionStatusStub(),
                    new Object(), log);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
    }

    /**
     * testRollbackSavepoint01
     * @throws Exception
     */
    @Test
    public void testRollbackSavepoint01() throws Exception {
        // テスト入力データ設定

        // テスト実施
        try {
            BatchUtil.rollbackSavepoint(new TransactionStatusStub(),
                    new Object());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
    }

    /**
     * testRollbackSavepoint02
     * @throws Exception
     */
    @Test
    public void testRollbackSavepoint02() throws Exception {
        // テスト入力データ設定

        // テスト実施
        try {
            BatchUtil.rollbackSavepoint(new TransactionStatusStub(),
                    new Object(), log);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
    }

    /**
     * testRollbackTransaction01
     * @throws Exception
     */
    @Test
    public void testRollbackTransaction01() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();

        // テスト実施
        try {
            BatchUtil.rollbackTransaction(tran, stat);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
    }

    /**
     * testRollbackTransaction02
     * @throws Exception
     */
    @Test
    public void testRollbackTransaction02() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();

        // テスト実施
        try {
            BatchUtil.rollbackTransaction(tran, stat, log);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
    }

    /**
     * testCommitRestartTransaction01
     * @throws Exception
     */
    @Test
    public void testCommitRestartTransaction01() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();

        // テスト実施
        TransactionStatus result = null;
        try {
            result = BatchUtil.commitRestartTransaction(tran, stat);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testCommitRestartTransaction02
     * @throws Exception
     */
    @Test
    public void testCommitRestartTransaction02() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();

        // テスト実施
        TransactionStatus result = null;
        try {
            result = BatchUtil.commitRestartTransaction(tran, stat, log);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testCommitRestartTransaction03
     * @throws Exception
     */
    @Test
    public void testCommitRestartTransaction03() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();
        TransactionDefinition def = BatchUtil.getTransactionDefinition();

        // テスト実施
        TransactionStatus result = null;
        try {
            result = BatchUtil.commitRestartTransaction(tran, stat, def);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testCommitRestartTransaction04
     * @throws Exception
     */
    @Test
    public void testCommitRestartTransaction04() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();
        TransactionDefinition def = BatchUtil.getTransactionDefinition();

        // テスト実施
        TransactionStatus result = null;
        try {
            result = BatchUtil.commitRestartTransaction(tran, stat, def, log);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testRollbackRestartTransaction001
     * @throws Exception
     */
    @Test
    public void testRollbackRestartTransaction001() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();

        // テスト実施
        TransactionStatus result = null;
        try {
            result = BatchUtil.rollbackRestartTransaction(tran, stat);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testRollbackRestartTransaction002
     * @throws Exception
     */
    @Test
    public void testRollbackRestartTransaction002() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();

        // テスト実施
        TransactionStatus result = null;
        try {
            result = BatchUtil.rollbackRestartTransaction(tran, stat, log);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testRollbackRestartTransaction003
     * @throws Exception
     */
    @Test
    public void testRollbackRestartTransaction003() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();
        TransactionDefinition def = BatchUtil.getTransactionDefinition();

        // テスト実施
        TransactionStatus result = null;
        try {
            result = BatchUtil.rollbackRestartTransaction(tran, stat, def);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testRollbackRestartTransaction004
     * @throws Exception
     */
    @Test
    public void testRollbackRestartTransaction004() throws Exception {
        // テスト入力データ設定
        PlatformTransactionManager tran = new PlatformTransactionManagerStub();
        TransactionStatus stat = new TransactionStatusStub();
        TransactionDefinition def = BatchUtil.getTransactionDefinition();

        // テスト実施
        TransactionStatus result = null;
        try {
            result = BatchUtil.rollbackRestartTransaction(tran, stat, def, log);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }

        // 結果検証
        assertNotNull(result);
    }

    /**
     * testGetMemoryInfo001
     * @throws Exception
     */
    @Test
    public void testGetMemoryInfo001() throws Exception {
        String info = BatchUtil.getMemoryInfo();

        assertNotNull(info);
        assertTrue(info.startsWith("Java memory info : "));
    }
}
