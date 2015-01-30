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

package jp.terasoluna.fw.batch.dao.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;

import jp.terasoluna.fw.dao.SqlHolder;
import jp.terasoluna.fw.dao.UpdateDAO;
import jp.terasoluna.fw.ex.unit.mock.DaoParam;
import jp.terasoluna.fw.ex.unit.mock.MockUpdateDao;
import jp.terasoluna.utlib.UTUtil;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BatchUpdateSupportImplTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * testBatchUpdateSupportImpl001<br>
     * <p>
     * コンストラクタのテスト<br>
     * 例外が発生しないこと<br>
     * </p>
     */
    @Test
    public void testBatchUpdateSupportImpl001() {
        BatchUpdateSupportImpl busi = null;

        // テスト
        busi = new BatchUpdateSupportImpl();

        // 検証
        assertNotNull(busi);
    }

    /**
     * testBatchUpdateSupportImplUpdateDAO001 <br>
     * <p>
     * コンストラクタのテスト<br>
     * 例外が発生しないこと<br>
     * </p>
     */
    @Test
    public void testBatchUpdateSupportImplUpdateDAO001() {
        BatchUpdateSupportImpl busi = null;
        UpdateDAO updateDAO = null;

        // テスト
        busi = new BatchUpdateSupportImpl(updateDAO);

        // 検証
        assertNotNull(busi);
        UpdateDAO updateDAOField = null;
        try {
            updateDAOField = (UpdateDAO) UTUtil.getPrivateField(busi,
                    "updateDAO");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
            return;
        }
        assertNull(updateDAOField);
    }

    /**
     * testBatchUpdateSupportImplUpdateDAO002 <br>
     * <p>
     * </p>
     */
    @Test
    public void testBatchUpdateSupportImplUpdateDAO002() {
        BatchUpdateSupportImpl busi = null;
        UpdateDAO updateDAO = new MockUpdateDao();

        // テスト
        busi = new BatchUpdateSupportImpl(updateDAO);

        // 検証
        assertNotNull(busi);
        Object updateDAOField = null;
        try {
            updateDAOField = UTUtil.getPrivateField(busi, "updateDAO");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(updateDAO, updateDAOField);
    }

    /**
     * testBatchUpdateSupportImplUpdateDAOComparator001 <br>
     * <p>
     * </p>
     */
    @Test
    public void testBatchUpdateSupportImplUpdateDAOComparator001() {
        BatchUpdateSupportImpl busi = null;
        UpdateDAO updateDAO = null;
        Comparator<String> comparator = null;

        // テスト
        busi = new BatchUpdateSupportImpl(updateDAO, comparator);

        // 検証
        assertNotNull(busi);
        Object updateDAOField = null;
        Object sqlIdOrderField = null;
        try {
            updateDAOField = UTUtil.getPrivateField(busi, "updateDAO");
            sqlIdOrderField = UTUtil.getPrivateField(busi, "sqlIdOrder");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
            return;
        }
        assertEquals(updateDAO, updateDAOField);
        assertEquals(comparator, sqlIdOrderField);
    }

    /**
     * testBatchUpdateSupportImplUpdateDAOComparator002 <br>
     * <p>
     * </p>
     */
    @Test
    public void testBatchUpdateSupportImplUpdateDAOComparator002() {
        BatchUpdateSupportImpl busi = null;
        UpdateDAO updateDAO = new MockUpdateDao();
        Comparator<String> comparator = null;

        // テスト
        busi = new BatchUpdateSupportImpl(updateDAO, comparator);

        // 検証
        assertNotNull(busi);
        Object updateDAOField = null;
        Object sqlIdOrderField = null;
        try {
            updateDAOField = UTUtil.getPrivateField(busi, "updateDAO");
            sqlIdOrderField = UTUtil.getPrivateField(busi, "sqlIdOrder");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
            return;
        }
        assertEquals(updateDAO, updateDAOField);
        assertEquals(comparator, sqlIdOrderField);
    }

    /**
     * testBatchUpdateSupportImplUpdateDAOComparator003 <br>
     * <p>
     * </p>
     */
    @Test
    public void testBatchUpdateSupportImplUpdateDAOComparator003() {
        BatchUpdateSupportImpl busi = null;
        UpdateDAO updateDAO = new MockUpdateDao();
        Comparator<String> comparator = new DummyComparatorImpl();

        // テスト
        busi = new BatchUpdateSupportImpl(updateDAO, comparator);

        // 検証
        assertNotNull(busi);
        Object updateDAOField = null;
        Object comparatorField = null;
        try {
            updateDAOField = UTUtil.getPrivateField(busi, "updateDAO");
            comparatorField = UTUtil.getPrivateField(busi, "comparator");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
            return;
        }
        assertEquals(updateDAO, updateDAOField);
        assertEquals(comparator, comparatorField);
    }

    /**
     * testBatchUpdateSupportImplUpdateDAOString001 <br>
     * <p>
     * </p>
     */
    @Test
    public void testBatchUpdateSupportImplUpdateDAOString001() {
        BatchUpdateSupportImpl busi = null;
        UpdateDAO updateDAO = null;
        String[] sqlIdOrder = null;

        // テスト
        busi = new BatchUpdateSupportImpl(updateDAO, sqlIdOrder);

        // 検証
        assertNotNull(busi);
        Object updateDAOField = null;
        Object sqlIdOrderField = null;
        try {
            updateDAOField = UTUtil.getPrivateField(busi, "updateDAO");
            sqlIdOrderField = UTUtil.getPrivateField(busi, "sqlIdOrder");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
            return;
        }
        assertEquals(updateDAO, updateDAOField);
        assertEquals(sqlIdOrder, sqlIdOrderField);
    }

    /**
     * testBatchUpdateSupportImplUpdateDAOString002 <br>
     * <p>
     * </p>
     */
    @Test
    public void testBatchUpdateSupportImplUpdateDAOString002() {
        BatchUpdateSupportImpl busi = null;
        UpdateDAO updateDAO = new MockUpdateDao();
        String[] sqlIdOrder = null;

        // テスト
        busi = new BatchUpdateSupportImpl(updateDAO, sqlIdOrder);

        // 検証
        assertNotNull(busi);
        Object updateDAOField = null;
        Object sqlIdOrderField = null;
        try {
            updateDAOField = UTUtil.getPrivateField(busi, "updateDAO");
            sqlIdOrderField = UTUtil.getPrivateField(busi, "sqlIdOrder");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
            return;
        }
        assertEquals(updateDAO, updateDAOField);
        assertEquals(sqlIdOrder, sqlIdOrderField);
    }

    /**
     * testBatchUpdateSupportImplUpdateDAOString003 <br>
     * <p>
     * </p>
     */
    @Test
    public void testBatchUpdateSupportImplUpdateDAOString003() {
        BatchUpdateSupportImpl busi = null;
        UpdateDAO updateDAO = new MockUpdateDao();
        String[] sqlIdOrder = { "hoge" };

        // テスト
        busi = new BatchUpdateSupportImpl(updateDAO, sqlIdOrder);

        // 検証
        assertNotNull(busi);
        Object updateDAOField = null;
        Object sqlIdOrderField = null;
        try {
            updateDAOField = UTUtil.getPrivateField(busi, "updateDAO");
            sqlIdOrderField = UTUtil.getPrivateField(busi, "sqlIdOrder");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
            return;
        }
        assertEquals(updateDAO, updateDAOField);
        assertEquals(sqlIdOrder, sqlIdOrderField);
    }

    /**
     * testAddBatch001 <br>
     * <p>
     * </p>
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddBatch001() {
        BatchUpdateSupportImpl busi = null;
        UpdateDAO updateDAO = new MockUpdateDao();

        // パラメータ設定
        busi = new BatchUpdateSupportImpl(updateDAO);

        // テスト
        busi.addBatch(null, null);

        // 検証
        Map<String, Queue<SqlHolder>> batchSqlsMap = null;
        try {
            batchSqlsMap = (Map<String, Queue<SqlHolder>>) UTUtil
                    .getPrivateField(busi, "batchSqlsMap");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(0, batchSqlsMap.size());
    }

    /**
     * testAddBatch002 <br>
     * <p>
     * </p>
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddBatch002() {
        BatchUpdateSupportImpl busi = null;
        UpdateDAO updateDAO = new MockUpdateDao();

        // パラメータ設定
        busi = new BatchUpdateSupportImpl(updateDAO);

        // テスト
        busi.addBatch("", null);

        // 検証
        Map<String, Queue<SqlHolder>> batchSqlsMap = null;
        try {
            batchSqlsMap = (Map<String, Queue<SqlHolder>>) UTUtil
                    .getPrivateField(busi, "batchSqlsMap");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(0, batchSqlsMap.size());
    }

    /**
     * testAddBatch003 <br>
     * <p>
     * </p>
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddBatch003() {
        BatchUpdateSupportImpl busi = null;
        UpdateDAO updateDAO = new MockUpdateDao();

        // パラメータ設定
        busi = new BatchUpdateSupportImpl(updateDAO);

        // テスト
        String sqlId1 = "hogeSQL";
        busi.addBatch(sqlId1, null);

        // 検証
        Map<String, Queue<SqlHolder>> batchSqlsMap = null;
        try {
            batchSqlsMap = (Map<String, Queue<SqlHolder>>) UTUtil
                    .getPrivateField(busi, "batchSqlsMap");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(1, batchSqlsMap.size());
        Set<Entry<String, Queue<SqlHolder>>> es = batchSqlsMap.entrySet();
        Entry et = es.toArray(new Entry[0])[0];
        assertEquals(sqlId1, et.getKey());
    }

    /**
     * testAddBatch004 <br>
     * <p>
     * </p>
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddBatch004() {
        BatchUpdateSupportImpl busi = null;
        UpdateDAO updateDAO = new MockUpdateDao();

        // パラメータ設定
        busi = new BatchUpdateSupportImpl(updateDAO);

        // テスト
        String sqlId1 = "hogeSQL";
        busi.addBatch(sqlId1, null);

        // 検証
        Map<String, Queue<SqlHolder>> batchSqlsMap = null;
        try {
            batchSqlsMap = (Map<String, Queue<SqlHolder>>) UTUtil
                    .getPrivateField(busi, "batchSqlsMap");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(1, batchSqlsMap.size());
        Set<Entry<String, Queue<SqlHolder>>> es = batchSqlsMap.entrySet();
        Entry et = es.toArray(new Entry[0])[0];
        assertEquals(sqlId1, et.getKey());
        assertNotNull(et.getValue());
        assertEquals(sqlId1, ((SqlHolder) ((Queue) et.getValue())
                .toArray(new SqlHolder[0])[0]).getSqlID());
        assertEquals(null, ((SqlHolder) ((Queue) et.getValue())
                .toArray(new SqlHolder[0])[0]).getBindParams());
    }

    /**
     * testAddBatch005 <br>
     * <p>
     * </p>
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddBatch005() {
        BatchUpdateSupportImpl busi = null;
        UpdateDAO updateDAO = new MockUpdateDao();

        // パラメータ設定
        busi = new BatchUpdateSupportImpl(updateDAO);

        // テスト
        String sqlId1 = "hogeSQL";
        DummyParam dummyParam1 = new DummyParam();
        busi.addBatch(sqlId1, dummyParam1);

        // 検証
        Map<String, Queue<SqlHolder>> batchSqlsMap = null;
        try {
            batchSqlsMap = (Map<String, Queue<SqlHolder>>) UTUtil
                    .getPrivateField(busi, "batchSqlsMap");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(1, batchSqlsMap.size());
        Set<Entry<String, Queue<SqlHolder>>> es = batchSqlsMap.entrySet();
        Entry et = es.toArray(new Entry[0])[0];
        assertEquals(sqlId1, et.getKey());
        assertNotNull(et.getValue());
        assertEquals(sqlId1, ((SqlHolder) ((Queue) et.getValue())
                .toArray(new SqlHolder[0])[0]).getSqlID());
        assertEquals(dummyParam1, ((SqlHolder) ((Queue) et.getValue())
                .toArray(new SqlHolder[0])[0]).getBindParams());
    }

    /**
     * testExecuteBatch001<br>
     * <p>
     * ソートせずに実行
     * </p>
     */
    @Test
    public void testExecuteBatch001() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        int result = busi.executeBatch();

        // 検証
        assertEquals(0, result);
        List<DaoParam> params = updateDAO.getParams();
        assertNotNull(params);
        assertEquals(10, params.size());
        // -- hogeSQLc
        assertEquals(sqlId1, params.get(0).getSqlId());
        assertEquals(dummyParam1, params.get(0).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId6, params.get(1).getSqlId());
        assertEquals(dummyParam6, params.get(1).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId2, params.get(2).getSqlId());
        assertEquals(dummyParam2, params.get(2).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId3, params.get(3).getSqlId());
        assertEquals(dummyParam3, params.get(3).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId7, params.get(4).getSqlId());
        assertEquals(dummyParam7, params.get(4).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId8, params.get(5).getSqlId());
        assertEquals(dummyParam8, params.get(5).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId4, params.get(6).getSqlId());
        assertEquals(dummyParam4, params.get(6).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId5, params.get(7).getSqlId());
        assertEquals(dummyParam5, params.get(7).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId9, params.get(8).getSqlId());
        assertEquals(dummyParam9, params.get(8).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId10, params.get(9).getSqlId());
        assertEquals(dummyParam10, params.get(9).getBindParams());
        // --
    }

    /**
     * testExecuteBatch002<br>
     * <p>
     * ソートして実行
     * </p>
     */
    @Test
    public void testExecuteBatch002() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        busi.sort();
        int result = busi.executeBatch();

        // 検証
        assertEquals(0, result);
        List<DaoParam> params = updateDAO.getParams();
        assertNotNull(params);
        assertEquals(10, params.size());
        // -- hogeSQLa
        assertEquals(sqlId4, params.get(0).getSqlId());
        assertEquals(dummyParam4, params.get(0).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId5, params.get(1).getSqlId());
        assertEquals(dummyParam5, params.get(1).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId9, params.get(2).getSqlId());
        assertEquals(dummyParam9, params.get(2).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId10, params.get(3).getSqlId());
        assertEquals(dummyParam10, params.get(3).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId2, params.get(4).getSqlId());
        assertEquals(dummyParam2, params.get(4).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId3, params.get(5).getSqlId());
        assertEquals(dummyParam3, params.get(5).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId7, params.get(6).getSqlId());
        assertEquals(dummyParam7, params.get(6).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId8, params.get(7).getSqlId());
        assertEquals(dummyParam8, params.get(7).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId1, params.get(8).getSqlId());
        assertEquals(dummyParam1, params.get(8).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId6, params.get(9).getSqlId());
        assertEquals(dummyParam6, params.get(9).getBindParams());
        // --
    }

    /**
     * testExecuteBatch003<br>
     * <p>
     * ソートして実行
     * </p>
     */
    @Test
    public void testExecuteBatch003() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        busi.sort(new DummyComparatorImpl());
        int result = busi.executeBatch();

        // 検証
        assertEquals(0, result);
        List<DaoParam> params = updateDAO.getParams();
        assertNotNull(params);
        assertEquals(10, params.size());
        // -- hogeSQLc
        assertEquals(sqlId1, params.get(0).getSqlId());
        assertEquals(dummyParam1, params.get(0).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId6, params.get(1).getSqlId());
        assertEquals(dummyParam6, params.get(1).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId2, params.get(2).getSqlId());
        assertEquals(dummyParam2, params.get(2).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId3, params.get(3).getSqlId());
        assertEquals(dummyParam3, params.get(3).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId7, params.get(4).getSqlId());
        assertEquals(dummyParam7, params.get(4).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId8, params.get(5).getSqlId());
        assertEquals(dummyParam8, params.get(5).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId4, params.get(6).getSqlId());
        assertEquals(dummyParam4, params.get(6).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId5, params.get(7).getSqlId());
        assertEquals(dummyParam5, params.get(7).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId9, params.get(8).getSqlId());
        assertEquals(dummyParam9, params.get(8).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId10, params.get(9).getSqlId());
        assertEquals(dummyParam10, params.get(9).getBindParams());
        // --
    }

    /**
     * testExecuteBatch004<br>
     * <p>
     * 実行順を指定して実行
     * </p>
     */
    @Test
    public void testExecuteBatch004() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        int result = busi.executeBatch(updateDAO, "hogeSQLc", "hogeSQLa",
                "hogeSQLb");

        // 検証
        assertEquals(0, result);
        List<DaoParam> params = updateDAO.getParams();
        assertNotNull(params);
        assertEquals(10, params.size());
        // -- hogeSQLc
        assertEquals(sqlId1, params.get(0).getSqlId());
        assertEquals(dummyParam1, params.get(0).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId6, params.get(1).getSqlId());
        assertEquals(dummyParam6, params.get(1).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId4, params.get(2).getSqlId());
        assertEquals(dummyParam4, params.get(2).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId5, params.get(3).getSqlId());
        assertEquals(dummyParam5, params.get(3).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId9, params.get(4).getSqlId());
        assertEquals(dummyParam9, params.get(4).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId10, params.get(5).getSqlId());
        assertEquals(dummyParam10, params.get(5).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId2, params.get(6).getSqlId());
        assertEquals(dummyParam2, params.get(6).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId3, params.get(7).getSqlId());
        assertEquals(dummyParam3, params.get(7).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId7, params.get(8).getSqlId());
        assertEquals(dummyParam7, params.get(8).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId8, params.get(9).getSqlId());
        assertEquals(dummyParam8, params.get(9).getBindParams());
        // --
    }

    /**
     * testExecuteBatch005<br>
     * <p>
     * 実行順を指定して実行
     * </p>
     */
    @Test
    public void testExecuteBatch005() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        int result = busi.executeBatch(updateDAO, (String) null);

        // 検証
        assertEquals(-200, result);
    }

    /**
     * testExecuteBatch006<br>
     * <p>
     * 実行順を指定して実行
     * </p>
     */
    @Test
    public void testExecuteBatch006() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        int result = busi.executeBatch(updateDAO, "hogeSQLaa", "hogeSQLbb",
                "hogeSQLc");

        // 検証
        assertEquals(-200, result);
    }

    /**
     * testExecuteBatch007<br>
     * <p>
     * 実行順を指定して実行
     * </p>
     */
    @Test
    public void testExecuteBatch007() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        int result = busi.executeBatch(updateDAO, "hogeSQLc", "hogeSQLa",
                "hogeSQLb", "hogeSQLd");

        // 検証
        assertEquals(0, result);
        List<DaoParam> params = updateDAO.getParams();
        assertNotNull(params);
        assertEquals(10, params.size());
        // -- hogeSQLc
        assertEquals(sqlId1, params.get(0).getSqlId());
        assertEquals(dummyParam1, params.get(0).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId6, params.get(1).getSqlId());
        assertEquals(dummyParam6, params.get(1).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId4, params.get(2).getSqlId());
        assertEquals(dummyParam4, params.get(2).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId5, params.get(3).getSqlId());
        assertEquals(dummyParam5, params.get(3).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId9, params.get(4).getSqlId());
        assertEquals(dummyParam9, params.get(4).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId10, params.get(5).getSqlId());
        assertEquals(dummyParam10, params.get(5).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId2, params.get(6).getSqlId());
        assertEquals(dummyParam2, params.get(6).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId3, params.get(7).getSqlId());
        assertEquals(dummyParam3, params.get(7).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId7, params.get(8).getSqlId());
        assertEquals(dummyParam7, params.get(8).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId8, params.get(9).getSqlId());
        assertEquals(dummyParam8, params.get(9).getBindParams());
        // --
    }

    /**
     * testExecuteBatchUpdateDAO001 <br>
     * <p>
     * </p>
     */
    @Test
    public void testExecuteBatchUpdateDAO001() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        int result = busi.executeBatch((UpdateDAO) null);

        // 検証
        assertEquals(BatchUpdateSupport.ERROR_UPDATE_DAO_IS_NULL, result);
    }

    /**
     * testExecuteBatchUpdateDAO002<br>
     * <p>
     * ソートせずに実行
     * </p>
     */
    @Test
    public void testExecuteBatchUpdateDAO002() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO1 = new MockUpdateDao();
        MockUpdateDao updateDAO2 = new MockUpdateDao();

        // パラメータ設定
        updateDAO1.addResult(Integer.valueOf(-1));
        updateDAO2.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO1);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        int result = busi.executeBatch(updateDAO2);

        // 検証
        assertEquals(0, result);
        List<DaoParam> params = updateDAO2.getParams();
        assertNotNull(params);
        assertEquals(10, params.size());
        // -- hogeSQLc
        assertEquals(sqlId1, params.get(0).getSqlId());
        assertEquals(dummyParam1, params.get(0).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId6, params.get(1).getSqlId());
        assertEquals(dummyParam6, params.get(1).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId2, params.get(2).getSqlId());
        assertEquals(dummyParam2, params.get(2).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId3, params.get(3).getSqlId());
        assertEquals(dummyParam3, params.get(3).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId7, params.get(4).getSqlId());
        assertEquals(dummyParam7, params.get(4).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId8, params.get(5).getSqlId());
        assertEquals(dummyParam8, params.get(5).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId4, params.get(6).getSqlId());
        assertEquals(dummyParam4, params.get(6).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId5, params.get(7).getSqlId());
        assertEquals(dummyParam5, params.get(7).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId9, params.get(8).getSqlId());
        assertEquals(dummyParam9, params.get(8).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId10, params.get(9).getSqlId());
        assertEquals(dummyParam10, params.get(9).getBindParams());
        // --
    }

    /**
     * testExecuteBatchUpdateDAO003<br>
     * <p>
     * ソートせずに実行
     * </p>
     */
    @Test
    public void testExecuteBatchUpdateDAO003() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        int result = busi.executeBatch(updateDAO);

        // 検証
        assertEquals(0, result);
        List<DaoParam> params = updateDAO.getParams();
        assertNotNull(params);
        assertEquals(10, params.size());
        // -- hogeSQLc
        assertEquals(sqlId1, params.get(0).getSqlId());
        assertEquals(dummyParam1, params.get(0).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId6, params.get(1).getSqlId());
        assertEquals(dummyParam6, params.get(1).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId2, params.get(2).getSqlId());
        assertEquals(dummyParam2, params.get(2).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId3, params.get(3).getSqlId());
        assertEquals(dummyParam3, params.get(3).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId7, params.get(4).getSqlId());
        assertEquals(dummyParam7, params.get(4).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId8, params.get(5).getSqlId());
        assertEquals(dummyParam8, params.get(5).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId4, params.get(6).getSqlId());
        assertEquals(dummyParam4, params.get(6).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId5, params.get(7).getSqlId());
        assertEquals(dummyParam5, params.get(7).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId9, params.get(8).getSqlId());
        assertEquals(dummyParam9, params.get(8).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId10, params.get(9).getSqlId());
        assertEquals(dummyParam10, params.get(9).getBindParams());
        // --
    }

    /**
     * testExecuteBatchUpdateDAO004<br>
     * <p>
     * ソートして実行
     * </p>
     */
    @Test
    public void testExecuteBatchUpdateDAO004() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO1 = new MockUpdateDao();
        MockUpdateDao updateDAO2 = new MockUpdateDao();

        // パラメータ設定
        updateDAO1.addResult(Integer.valueOf(-1));
        updateDAO2.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO1);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        busi.sort();
        int result = busi.executeBatch(updateDAO2);

        // 検証
        assertEquals(0, result);
        List<DaoParam> params = updateDAO2.getParams();
        assertNotNull(params);
        assertEquals(10, params.size());
        // -- hogeSQLa
        assertEquals(sqlId4, params.get(0).getSqlId());
        assertEquals(dummyParam4, params.get(0).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId5, params.get(1).getSqlId());
        assertEquals(dummyParam5, params.get(1).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId9, params.get(2).getSqlId());
        assertEquals(dummyParam9, params.get(2).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId10, params.get(3).getSqlId());
        assertEquals(dummyParam10, params.get(3).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId2, params.get(4).getSqlId());
        assertEquals(dummyParam2, params.get(4).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId3, params.get(5).getSqlId());
        assertEquals(dummyParam3, params.get(5).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId7, params.get(6).getSqlId());
        assertEquals(dummyParam7, params.get(6).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId8, params.get(7).getSqlId());
        assertEquals(dummyParam8, params.get(7).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId1, params.get(8).getSqlId());
        assertEquals(dummyParam1, params.get(8).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId6, params.get(9).getSqlId());
        assertEquals(dummyParam6, params.get(9).getBindParams());
        // --
    }

    /**
     * testExecuteBatchUpdateDAO005<br>
     * <p>
     * ソートして実行
     * </p>
     */
    @Test
    public void testExecuteBatchUpdateDAO005() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        busi.sort();
        int result = busi.executeBatch(updateDAO);

        // 検証
        assertEquals(0, result);
        List<DaoParam> params = updateDAO.getParams();
        assertNotNull(params);
        assertEquals(10, params.size());
        // -- hogeSQLa
        assertEquals(sqlId4, params.get(0).getSqlId());
        assertEquals(dummyParam4, params.get(0).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId5, params.get(1).getSqlId());
        assertEquals(dummyParam5, params.get(1).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId9, params.get(2).getSqlId());
        assertEquals(dummyParam9, params.get(2).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId10, params.get(3).getSqlId());
        assertEquals(dummyParam10, params.get(3).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId2, params.get(4).getSqlId());
        assertEquals(dummyParam2, params.get(4).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId3, params.get(5).getSqlId());
        assertEquals(dummyParam3, params.get(5).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId7, params.get(6).getSqlId());
        assertEquals(dummyParam7, params.get(6).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId8, params.get(7).getSqlId());
        assertEquals(dummyParam8, params.get(7).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId1, params.get(8).getSqlId());
        assertEquals(dummyParam1, params.get(8).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId6, params.get(9).getSqlId());
        assertEquals(dummyParam6, params.get(9).getBindParams());
        // --
    }

    /**
     * <br>
     * <p>
     * </p>
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testClear001() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        busi.clear();

        // 検証
        Map<String, Queue<SqlHolder>> batchSqlsMap = null;
        try {
            batchSqlsMap = (Map<String, Queue<SqlHolder>>) UTUtil
                    .getPrivateField(busi, "batchSqlsMap");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(0, batchSqlsMap.size());
    }

    /**
     * <br>
     * <p>
     * </p>
     */
    @Test
    public void testSize001() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        long count = busi.size();

        // 検証
        assertEquals(10, count);
    }

    /**
     * testGetSqlHolderList001<br>
     * <p>
     * ソートせずに実行
     * </p>
     */
    @Test
    public void testGetSqlHolderList001() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        List<SqlHolder> list = busi.getSqlHolderList();

        // 検証
        assertNotNull(list);
        assertEquals(10, list.size());
        assertEquals(sqlId1, list.get(0).getSqlID());
        assertEquals(sqlId6, list.get(1).getSqlID());
        assertEquals(sqlId2, list.get(2).getSqlID());
        assertEquals(sqlId3, list.get(3).getSqlID());
        assertEquals(sqlId7, list.get(4).getSqlID());
        assertEquals(sqlId8, list.get(5).getSqlID());
        assertEquals(sqlId4, list.get(6).getSqlID());
        assertEquals(sqlId5, list.get(7).getSqlID());
        assertEquals(sqlId9, list.get(8).getSqlID());
        assertEquals(sqlId10, list.get(9).getSqlID());
    }

    /**
     * testGetSqlHolderList002<br>
     * <p>
     * ソートして実行
     * </p>
     */
    @Test
    public void testGetSqlHolderList002() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        busi.sort();
        List<SqlHolder> list = busi.getSqlHolderList();

        // 検証
        assertNotNull(list);
        assertEquals(10, list.size());
        assertEquals(sqlId4, list.get(0).getSqlID());
        assertEquals(sqlId5, list.get(1).getSqlID());
        assertEquals(sqlId9, list.get(2).getSqlID());
        assertEquals(sqlId10, list.get(3).getSqlID());
        assertEquals(sqlId2, list.get(4).getSqlID());
        assertEquals(sqlId3, list.get(5).getSqlID());
        assertEquals(sqlId7, list.get(6).getSqlID());
        assertEquals(sqlId8, list.get(7).getSqlID());
        assertEquals(sqlId1, list.get(8).getSqlID());
        assertEquals(sqlId6, list.get(9).getSqlID());
    }

    /**
     * testGetSqlHolderList003<br>
     * <p>
     * ソートして実行
     * </p>
     */
    @Test
    public void testGetSqlHolderList003() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        busi.sort(new DummyComparatorImpl());
        List<SqlHolder> list = busi.getSqlHolderList();

        // 検証
        assertNotNull(list);
        assertEquals(10, list.size());
        assertEquals(sqlId1, list.get(0).getSqlID());
        assertEquals(sqlId6, list.get(1).getSqlID());
        assertEquals(sqlId2, list.get(2).getSqlID());
        assertEquals(sqlId3, list.get(3).getSqlID());
        assertEquals(sqlId7, list.get(4).getSqlID());
        assertEquals(sqlId8, list.get(5).getSqlID());
        assertEquals(sqlId4, list.get(6).getSqlID());
        assertEquals(sqlId5, list.get(7).getSqlID());
        assertEquals(sqlId9, list.get(8).getSqlID());
        assertEquals(sqlId10, list.get(9).getSqlID());
    }

    /**
     * testGetSqlHolderList004 <br>
     * <p>
     * </p>
     */
    @Test
    public void testGetSqlHolderList004() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        List<SqlHolder> list = busi.getSqlHolderList(new DummyComparatorImpl());

        // 検証
        assertNotNull(list);
        assertEquals(10, list.size());
        assertEquals(sqlId1, list.get(0).getSqlID());
        assertEquals(sqlId6, list.get(1).getSqlID());
        assertEquals(sqlId2, list.get(2).getSqlID());
        assertEquals(sqlId3, list.get(3).getSqlID());
        assertEquals(sqlId7, list.get(4).getSqlID());
        assertEquals(sqlId8, list.get(5).getSqlID());
        assertEquals(sqlId4, list.get(6).getSqlID());
        assertEquals(sqlId5, list.get(7).getSqlID());
        assertEquals(sqlId9, list.get(8).getSqlID());
        assertEquals(sqlId10, list.get(9).getSqlID());
    }

    /**
     * testGetSqlHolderList005 <br>
     * <p>
     * </p>
     */
    @Test
    public void testGetSqlHolderList005() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        List<SqlHolder> list = busi.getSqlHolderList("hogeSQLc", "hogeSQLa",
                "hogeSQLb");

        // 検証
        assertNotNull(list);
        assertEquals(10, list.size());
        assertEquals(sqlId1, list.get(0).getSqlID());
        assertEquals(sqlId6, list.get(1).getSqlID());
        assertEquals(sqlId4, list.get(2).getSqlID());
        assertEquals(sqlId5, list.get(3).getSqlID());
        assertEquals(sqlId9, list.get(4).getSqlID());
        assertEquals(sqlId10, list.get(5).getSqlID());
        assertEquals(sqlId2, list.get(6).getSqlID());
        assertEquals(sqlId3, list.get(7).getSqlID());
        assertEquals(sqlId7, list.get(8).getSqlID());
        assertEquals(sqlId8, list.get(9).getSqlID());
    }

    /**
     * testGetSqlHolderList006 <br>
     * <p>
     * </p>
     */
    @Test
    public void testGetSqlHolderList006() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        List<SqlHolder> list = busi.getSqlHolderList(null, "hogeSQLb");

        // 検証
        assertNull(list);
    }

    /**
     * testGetSqlHolderList007 <br>
     * <p>
     * </p>
     */
    @Test
    public void testGetSqlHolderList007() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        List<SqlHolder> list = busi.getSqlHolderList("hogeSQLcc", "hogeSQLaa",
                "hogeSQLbb");

        // 検証
        assertNull(list);
    }

    /**
     * testGetSqlHolderList008 <br>
     * <p>
     * </p>
     */
    @Test
    public void testGetSqlHolderList008() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        List<SqlHolder> list = busi.getSqlHolderList("hogeSQLc", "hogeSQLa",
                "hogeSQLb", "hogeSQLd");

        // 検証
        assertNotNull(list);
        assertEquals(10, list.size());
        assertEquals(sqlId1, list.get(0).getSqlID());
        assertEquals(sqlId6, list.get(1).getSqlID());
        assertEquals(sqlId4, list.get(2).getSqlID());
        assertEquals(sqlId5, list.get(3).getSqlID());
        assertEquals(sqlId9, list.get(4).getSqlID());
        assertEquals(sqlId10, list.get(5).getSqlID());
        assertEquals(sqlId2, list.get(6).getSqlID());
        assertEquals(sqlId3, list.get(7).getSqlID());
        assertEquals(sqlId7, list.get(8).getSqlID());
        assertEquals(sqlId8, list.get(9).getSqlID());
    }

    /**
     * testExecuteBatchUpdateDAOComparator001 <br>
     * <p>
     * </p>
     */
    @Test
    public void testExecuteBatchUpdateDAOComparator001() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();
        Comparator<String> comparator = null;

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        int result = busi.executeBatch((UpdateDAO) null, comparator);

        // 検証
        assertEquals(BatchUpdateSupport.ERROR_UPDATE_DAO_IS_NULL, result);
    }

    /**
     * testExecuteBatchUpdateDAOComparator002<br>
     * <p>
     * ソートせずに実行
     * </p>
     */
    @Test
    public void testExecuteBatchUpdateDAOComparator002() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO1 = new MockUpdateDao();
        MockUpdateDao updateDAO2 = new MockUpdateDao();
        Comparator<String> comparator = new DummyComparatorImpl();

        // パラメータ設定
        updateDAO1.addResult(Integer.valueOf(-1));
        updateDAO2.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO1);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        int result = busi.executeBatch(updateDAO2, comparator);

        // 検証
        assertEquals(0, result);
        List<DaoParam> params = updateDAO2.getParams();
        assertNotNull(params);
        assertEquals(10, params.size());
        // -- hogeSQLc
        assertEquals(sqlId1, params.get(0).getSqlId());
        assertEquals(dummyParam1, params.get(0).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId6, params.get(1).getSqlId());
        assertEquals(dummyParam6, params.get(1).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId2, params.get(2).getSqlId());
        assertEquals(dummyParam2, params.get(2).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId3, params.get(3).getSqlId());
        assertEquals(dummyParam3, params.get(3).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId7, params.get(4).getSqlId());
        assertEquals(dummyParam7, params.get(4).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId8, params.get(5).getSqlId());
        assertEquals(dummyParam8, params.get(5).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId4, params.get(6).getSqlId());
        assertEquals(dummyParam4, params.get(6).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId5, params.get(7).getSqlId());
        assertEquals(dummyParam5, params.get(7).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId9, params.get(8).getSqlId());
        assertEquals(dummyParam9, params.get(8).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId10, params.get(9).getSqlId());
        assertEquals(dummyParam10, params.get(9).getBindParams());
        // --
    }

    /**
     * testExecuteBatchUpdateDAOComparator003<br>
     * <p>
     * ソートせずに実行
     * </p>
     */
    @Test
    public void testExecuteBatchUpdateDAOComparator003() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();
        Comparator<String> comparator = new DummyComparatorImpl();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        int result = busi.executeBatch(updateDAO, comparator);

        // 検証
        assertEquals(0, result);
        List<DaoParam> params = updateDAO.getParams();
        assertNotNull(params);
        assertEquals(10, params.size());
        // -- hogeSQLc
        assertEquals(sqlId1, params.get(0).getSqlId());
        assertEquals(dummyParam1, params.get(0).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId6, params.get(1).getSqlId());
        assertEquals(dummyParam6, params.get(1).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId2, params.get(2).getSqlId());
        assertEquals(dummyParam2, params.get(2).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId3, params.get(3).getSqlId());
        assertEquals(dummyParam3, params.get(3).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId7, params.get(4).getSqlId());
        assertEquals(dummyParam7, params.get(4).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId8, params.get(5).getSqlId());
        assertEquals(dummyParam8, params.get(5).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId4, params.get(6).getSqlId());
        assertEquals(dummyParam4, params.get(6).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId5, params.get(7).getSqlId());
        assertEquals(dummyParam5, params.get(7).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId9, params.get(8).getSqlId());
        assertEquals(dummyParam9, params.get(8).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId10, params.get(9).getSqlId());
        assertEquals(dummyParam10, params.get(9).getBindParams());
        // --
    }

    /**
     * testExecuteBatchUpdateDAOComparator004<br>
     * <p>
     * ソートして実行
     * </p>
     */
    @Test
    public void testExecuteBatchUpdateDAOComparator004() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO1 = new MockUpdateDao();
        MockUpdateDao updateDAO2 = new MockUpdateDao();
        Comparator<String> comparator = new DummyComparatorImpl();

        // パラメータ設定
        updateDAO1.addResult(Integer.valueOf(-1));
        updateDAO2.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO1);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        busi.sort();
        int result = busi.executeBatch(updateDAO2, comparator);

        // 検証
        assertEquals(0, result);
        List<DaoParam> params = updateDAO2.getParams();
        assertNotNull(params);
        assertEquals(10, params.size());
        // -- hogeSQLc
        assertEquals(sqlId1, params.get(0).getSqlId());
        assertEquals(dummyParam1, params.get(0).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId6, params.get(1).getSqlId());
        assertEquals(dummyParam6, params.get(1).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId2, params.get(2).getSqlId());
        assertEquals(dummyParam2, params.get(2).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId3, params.get(3).getSqlId());
        assertEquals(dummyParam3, params.get(3).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId7, params.get(4).getSqlId());
        assertEquals(dummyParam7, params.get(4).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId8, params.get(5).getSqlId());
        assertEquals(dummyParam8, params.get(5).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId4, params.get(6).getSqlId());
        assertEquals(dummyParam4, params.get(6).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId5, params.get(7).getSqlId());
        assertEquals(dummyParam5, params.get(7).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId9, params.get(8).getSqlId());
        assertEquals(dummyParam9, params.get(8).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId10, params.get(9).getSqlId());
        assertEquals(dummyParam10, params.get(9).getBindParams());
        // --
    }

    /**
     * testExecuteBatchUpdateDAOComparator005<br>
     * <p>
     * ソートして実行
     * </p>
     */
    @Test
    public void testExecuteBatchUpdateDAOComparator005() {
        BatchUpdateSupportImpl busi = null;
        MockUpdateDao updateDAO = new MockUpdateDao();
        Comparator<String> comparator = new DummyComparatorImpl();

        // パラメータ設定
        updateDAO.addResult(Integer.valueOf(0));
        busi = new BatchUpdateSupportImpl(updateDAO);

        String sqlId1 = "hogeSQLc";
        DummyParamC dummyParam1 = new DummyParamC();
        String sqlId2 = "hogeSQLb";
        DummyParamB dummyParam2 = new DummyParamB();
        String sqlId3 = "hogeSQLb";
        DummyParamB dummyParam3 = new DummyParamB();
        String sqlId4 = "hogeSQLa";
        DummyParamA dummyParam4 = new DummyParamA();
        String sqlId5 = "hogeSQLa";
        DummyParamA dummyParam5 = new DummyParamA();
        String sqlId6 = "hogeSQLc";
        DummyParamC dummyParam6 = new DummyParamC();
        String sqlId7 = "hogeSQLb";
        DummyParamB dummyParam7 = new DummyParamB();
        String sqlId8 = "hogeSQLb";
        DummyParamB dummyParam8 = new DummyParamB();
        String sqlId9 = "hogeSQLa";
        DummyParamA dummyParam9 = new DummyParamA();
        String sqlId10 = "hogeSQLa";
        DummyParamA dummyParam10 = new DummyParamA();

        busi.addBatch(sqlId1, dummyParam1);
        busi.addBatch(sqlId2, dummyParam2);
        busi.addBatch(sqlId3, dummyParam3);
        busi.addBatch(sqlId4, dummyParam4);
        busi.addBatch(sqlId5, dummyParam5);
        busi.addBatch(sqlId6, dummyParam6);
        busi.addBatch(sqlId7, dummyParam7);
        busi.addBatch(sqlId8, dummyParam8);
        busi.addBatch(sqlId9, dummyParam9);
        busi.addBatch(sqlId10, dummyParam10);

        // テスト
        busi.sort();
        int result = busi.executeBatch(updateDAO, comparator);

        // 検証
        assertEquals(0, result);
        List<DaoParam> params = updateDAO.getParams();
        assertNotNull(params);
        assertEquals(10, params.size());
        // -- hogeSQLc
        assertEquals(sqlId1, params.get(0).getSqlId());
        assertEquals(dummyParam1, params.get(0).getBindParams());
        // -- hogeSQLc
        assertEquals(sqlId6, params.get(1).getSqlId());
        assertEquals(dummyParam6, params.get(1).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId2, params.get(2).getSqlId());
        assertEquals(dummyParam2, params.get(2).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId3, params.get(3).getSqlId());
        assertEquals(dummyParam3, params.get(3).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId7, params.get(4).getSqlId());
        assertEquals(dummyParam7, params.get(4).getBindParams());
        // -- hogeSQLb
        assertEquals(sqlId8, params.get(5).getSqlId());
        assertEquals(dummyParam8, params.get(5).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId4, params.get(6).getSqlId());
        assertEquals(dummyParam4, params.get(6).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId5, params.get(7).getSqlId());
        assertEquals(dummyParam5, params.get(7).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId9, params.get(8).getSqlId());
        assertEquals(dummyParam9, params.get(8).getBindParams());
        // -- hogeSQLa
        assertEquals(sqlId10, params.get(9).getSqlId());
        assertEquals(dummyParam10, params.get(9).getBindParams());
        // --
    }

}
