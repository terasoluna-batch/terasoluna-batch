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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import jp.terasoluna.fw.dao.SqlHolder;
import jp.terasoluna.fw.dao.UpdateDAO;
import jp.terasoluna.fw.ex.unit.mock.MockUpdateDao;
import jp.terasoluna.fw.ex.unit.util.ReflectionUtils;
import jp.terasoluna.utlib.UTUtil;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BatchUpdateExecutorTest {

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
     * testBatchUpdateExecutor001
     */
    @Test
    public void testBatchUpdateExecutor001() {
        BatchUpdateExecutor bue = new BatchUpdateExecutor();
        assertNotNull(bue);
    }

    /**
     * testExecuteBatch001
     */
    @Test
    public void testExecuteBatch001() {
        Object value = null;
        UpdateDAO updateDAO = null;

        // パラメータ
        List<BatchUpdateResult> result = null;

        // テスト
        result = BatchUpdateExecutor.executeBatch(value, updateDAO);

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * testExecuteBatch002
     */
    @Test
    public void testExecuteBatch002() {
        Object value = new Object();
        UpdateDAO updateDAO = new MockUpdateDao();

        // パラメータ
        List<BatchUpdateResult> result = null;

        // テスト
        result = BatchUpdateExecutor.executeBatch(value, updateDAO);

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * testExecuteBatch003
     */
    @Test
    public void testExecuteBatch003() {
        BatchUpdateSupport bus = new BatchUpdateSupportImpl();
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ
        List<BatchUpdateResult> result = null;
        updateDAO.addResult(Integer.valueOf(0));

        // テスト
        result = BatchUpdateExecutor.executeBatch(bus, updateDAO);

        // 検証
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(null, result.get(0).getExecuteBatchException());
        assertEquals(BatchUpdateSupportImpl.class, result.get(0)
                .getExecuteBatchUpdateSupport().getClass());
        assertEquals(Integer.valueOf(0), result.get(0).getExecuteBatchResult());
    }

    /**
     * testExecuteBatch004
     */
    @Test
    public void testExecuteBatch004() {
        BatchUpdateSupport bus = new BatchUpdateSupportImpl();
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ
        List<BatchUpdateResult> result = null;
        updateDAO.addResult(new Exception("ERROR"));

        // テスト
        result = BatchUpdateExecutor.executeBatch(bus, updateDAO);

        // 検証
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getExecuteBatchException());
        assertNull(result.get(0).getExecuteBatchResult());
        assertEquals(BatchUpdateSupportImpl.class, result.get(0)
                .getExecuteBatchUpdateSupport().getClass());
        assertEquals(ClassCastException.class, result.get(0)
                .getExecuteBatchException().getClass());
    }

    /**
     * testExecuteBatch005
     */
    @Test
    public void testExecuteBatch005() {
        List<BatchUpdateSupport> busList = new ArrayList<BatchUpdateSupport>();
        BatchUpdateSupport bus1 = new BatchUpdateSupportImpl();
        BatchUpdateSupport bus2 = new BatchUpdateSupportImpl();
        busList.add(bus1);
        busList.add(bus2);
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ
        List<BatchUpdateResult> result = null;
        updateDAO.addResult(Integer.valueOf(0));

        // テスト
        result = BatchUpdateExecutor.executeBatch(busList, updateDAO);

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertNull(result.get(0).getExecuteBatchException());
        assertNotNull(result.get(0).getExecuteBatchResult());
        assertEquals(BatchUpdateSupportImpl.class, result.get(0)
                .getExecuteBatchUpdateSupport().getClass());
    }

    /**
     * testExecuteBatch006
     */
    @Test
    public void testExecuteBatch006() {
        BatchUpdateSupport[] busArray = new BatchUpdateSupport[2];
        BatchUpdateSupport bus1 = new BatchUpdateSupportImpl();
        BatchUpdateSupport bus2 = new BatchUpdateSupportImpl();
        busArray[0] = bus1;
        busArray[1] = bus2;
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ
        List<BatchUpdateResult> result = null;
        updateDAO.addResult(Integer.valueOf(0));

        // テスト
        result = BatchUpdateExecutor.executeBatch(busArray, updateDAO);

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertNull(result.get(0).getExecuteBatchException());
        assertNotNull(result.get(0).getExecuteBatchResult());
        assertEquals(BatchUpdateSupportImpl.class, result.get(0)
                .getExecuteBatchUpdateSupport().getClass());
    }

    /**
     * testExecuteBatch007
     */
    @Test
    public void testExecuteBatch007() {
        DummyJobContext djc = new DummyJobContext();
        MockUpdateDao updateDAO = new MockUpdateDao();

        // パラメータ
        List<BatchUpdateResult> result = null;
        updateDAO.addResult(Integer.valueOf(0));

        // テスト
        result = BatchUpdateExecutor.executeBatch(djc, updateDAO);

        // 検証
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getExecuteBatchException());
        assertNotNull(result.get(0).getExecuteBatchResult());
        assertEquals(BatchUpdateSupportImpl.class, result.get(0)
                .getExecuteBatchUpdateSupport().getClass());
    }

    /**
     * testClearAll001
     */
    @Test
    public void testClearAll001() {
        // パラメータ
        Object value = null;

        // テスト
        BatchUpdateExecutor.clearAll(value);

        // 検証
    }

    /**
     * testClearAll002
     */
    @Test
    public void testClearAll002() {
        // パラメータ
        Object value = new Object();

        // テスト
        BatchUpdateExecutor.clearAll(value);

        // 検証
    }

    /**
     * testClearAll003
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testClearAll003() {
        // パラメータ
        BatchUpdateSupport bus = new BatchUpdateSupportImpl();

        bus.addBatch("hogeSql", null);

        // テスト
        BatchUpdateExecutor.clearAll(bus);

        // 検証
        Map<String, Queue<SqlHolder>> batchSqlsMap = null;
        try {
            batchSqlsMap = (Map<String, Queue<SqlHolder>>) UTUtil
                    .getPrivateField(bus, "batchSqlsMap");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
            return;
        }
        assertNotNull(batchSqlsMap);
        assertEquals(0, batchSqlsMap.size());
    }

    /**
     * testClearAll004
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testClearAll004() {
        // パラメータ
        List<BatchUpdateSupport> busList = new ArrayList<BatchUpdateSupport>();
        BatchUpdateSupport bus1 = new BatchUpdateSupportImpl();
        BatchUpdateSupport bus2 = new BatchUpdateSupportImpl();
        bus1.addBatch("hogeSql", null);
        bus2.addBatch("hogeSql", null);
        busList.add(bus1);
        busList.add(bus2);

        // テスト
        BatchUpdateExecutor.clearAll(busList);

        // 検証
        Map<String, Queue<SqlHolder>> batchSqlsMap1 = null;
        Map<String, Queue<SqlHolder>> batchSqlsMap2 = null;
        try {
            batchSqlsMap1 = (Map<String, Queue<SqlHolder>>) UTUtil
                    .getPrivateField(bus1, "batchSqlsMap");
            batchSqlsMap2 = (Map<String, Queue<SqlHolder>>) UTUtil
                    .getPrivateField(bus2, "batchSqlsMap");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
            return;
        }
        assertNotNull(batchSqlsMap1);
        assertNotNull(batchSqlsMap2);
        assertEquals(0, batchSqlsMap1.size());
        assertEquals(0, batchSqlsMap2.size());
    }

    /**
     * testClearAll005
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testClearAll005() {
        // パラメータ
        BatchUpdateSupport[] busArray = new BatchUpdateSupport[2];
        BatchUpdateSupport bus1 = new BatchUpdateSupportImpl();
        BatchUpdateSupport bus2 = new BatchUpdateSupportImpl();
        bus1.addBatch("hogeSql", null);
        bus2.addBatch("hogeSql", null);
        busArray[0] = bus1;
        busArray[1] = bus2;

        // テスト
        BatchUpdateExecutor.clearAll(busArray);

        // 検証
        Map<String, Queue<SqlHolder>> batchSqlsMap1 = null;
        Map<String, Queue<SqlHolder>> batchSqlsMap2 = null;
        try {
            batchSqlsMap1 = (Map<String, Queue<SqlHolder>>) UTUtil
                    .getPrivateField(bus1, "batchSqlsMap");
            batchSqlsMap2 = (Map<String, Queue<SqlHolder>>) UTUtil
                    .getPrivateField(bus2, "batchSqlsMap");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
            return;
        }
        assertNotNull(batchSqlsMap1);
        assertNotNull(batchSqlsMap2);
        assertEquals(0, batchSqlsMap1.size());
        assertEquals(0, batchSqlsMap2.size());
    }

    /**
     * testClearAll006
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testClearAll006() {
        // パラメータ
        DummyJobContext djc = new DummyJobContext();
        BatchUpdateSupport bus = djc.getBatchUpdateSupport();
        bus.addBatch("hogeSql", null);

        // テスト
        BatchUpdateExecutor.clearAll(djc);

        // 検証
        Map<String, Queue<SqlHolder>> batchSqlsMap = null;
        try {
            batchSqlsMap = (Map<String, Queue<SqlHolder>>) UTUtil
                    .getPrivateField(bus, "batchSqlsMap");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
            return;
        }
        assertNotNull(batchSqlsMap);
        assertEquals(0, batchSqlsMap.size());
    }

    /**
     * testOutputExecptionLog01
     */
    @Test
    public void testOutputExecptionLog01() {
        try {
            ReflectionUtils.invoke(BatchUpdateExecutor.class,
                    "outputExceptionLog", new Class<?>[] { Throwable.class },
                    new Object[] { new Exception("test") });
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * testExecuteBatchInnerObject001
     */
    @Test
    public void testExecuteBatchInnerObject001() {
        Object value = null;
        UpdateDAO updateDAO = null;

        List<BatchUpdateResult> result = BatchUpdateExecutor
                .executeBatchInnerObject(value, updateDAO);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * testExecuteBatchInnerObject002
     */
    @Test
    public void testExecuteBatchInnerObject002() {
        TestBean001 value = new TestBean001();
        value.throwException = true;
        UpdateDAO updateDAO = null;

        List<BatchUpdateResult> result = BatchUpdateExecutor
                .executeBatchInnerObject(value, updateDAO);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * testClearAllInnerObject001
     * @throws Exception
     */
    @Test
    public void testClearAllInnerObject001() {
        Object value = null;

        BatchUpdateExecutor.clearAllInnerObject(value);
    }

    /**
     * testClearAllInnerObject002
     * @throws Exception
     */
    @Test
    public void testClearAllInnerObject002() {
        TestBean001 value = new TestBean001();
        value.throwException = true;

        BatchUpdateExecutor.clearAllInnerObject(value);
    }
}
