/*
 * Copyright (c) 2007 NTT DATA Corporation
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

package jp.terasoluna.fw.dao.ibatis;

import java.util.ArrayList;
import java.util.List;

import jp.terasoluna.fw.dao.SqlHolder;
import jp.terasoluna.utlib.LogUTUtil;
import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.dao.ibatis.UpdateDAOiBatisImpl}
 * クラスのブラックボックステスト。
 *
 * <p>
 * <h4>【クラスの概要】</h4>
 * UpdateDAOインタフェースのiBATIS用実装クラス。
 * <p>
 *
 * @see jp.terasoluna.fw.dao.ibatis.UpdateDAOiBatisImpl
 */
public class UpdateDAOiBatisImplTest extends TestCase {

    /**
     * テスト対象クラス
     */
    private UpdateDAOiBatisImpl dao = new UpdateDAOiBatisImpl();

    /**
     * このテストケースを実行する為の
     * GUI アプリケーションを起動する。
     *
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(UpdateDAOiBatisImplTest.class);
    }

    /**
     * 初期化処理を行う。
     *
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dao = new UpdateDAOiBatisImpl();
        LogUTUtil.flush();
    }

    /**
     * 終了処理を行う。
     *
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        dao = null;
    }

    /**
     * コンストラクタ。
     *
     * @param name このテストケースの名前。
     */
    public UpdateDAOiBatisImplTest(String name) {
        super(name);
    }

    /**
     * testExecute01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     *         (引数) bindParams:"1"<br>
     *         (状態) updateの戻り値:1<br>
     *
     * <br>
     * 期待値：(戻り値) E:1<br>
     *         (状態変化) updateの呼出確認:引数がsqlId、bindParamsで呼び出されている事を確認<br>
     *
     * <br>
     * updateを正常に呼び出し、結果が返却される場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testExecute01() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(new UpdateDAOiBatisImpl_SqlMapClientTemplateStub01());

        // テスト実施
        int i = dao.execute("sqlId", "1");

        // 判定
        assertEquals(1, i);
        UpdateDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapTemp = (UpdateDAOiBatisImpl_SqlMapClientTemplateStub01) dao
                .getSqlMapClientTemplate();
        assertTrue(sqlMapTemp.isCalled());
        assertEquals("sqlId", sqlMapTemp.getStatementName());
        assertEquals("1", sqlMapTemp.getParameterObject());
    }

    /**
     * testAddBatch01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) sqlID:"sqlID1"<br>
     *         (引数) bindParams:"1"<br>
     *         (状態) batchSqls.get()にて取得したList「sqlHolders」:null<br>
     *         
     * <br>
     * 期待値：(状態変化) batchSqls.get()にて取得したList「sqlHolders」:List[0] = <br>
     *                        SqlHolder(sqlID="sqlID1",bindParams="1")<br>
     *         
     * <br>
     * sqlHoldersがnullの場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings({"unchecked","deprecation"})
    public void testAddBatch01() throws Exception {
        // 前処理

        // テスト実施
        dao.addBatch("sqlID1", "1");

        // 判定
        Object obj = UTUtil.getPrivateField(dao, "batchSqls");
        List<SqlHolder> sqlHolders
            = ((ThreadLocal<List<SqlHolder>>) obj).get();
        SqlHolder sqlHolder = sqlHolders.get(0);
        assertEquals("sqlID1", sqlHolder.getSqlID());
        assertEquals("1", sqlHolder.getBindParams());
    }

    /**
     * testAddBatch02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) sqlID:"sqlID2"<br>
     *         (引数) bindParams:"2"<br>
     *         (状態) batchSqls.get()にて取得したList「sqlHolders」:Not Null<br>
     *                List[0] = <br>
     *                    SqlHolder(sqlID="sqlID1",bindParams="1")<br>
     *         
     * <br>
     * 期待値：(状態変化) batchSqls.get()にて取得したList「sqlHolders」:List[0] = <br>
     *                        SqlHolder(sqlID="sqlID1",bindParams="1")<br>
     *                    List[1] = <br>
     *                        SqlHolder(sqlID="sqlID2",bindParams="2")<br>
     *         
     * <br>
     * sqlHoldersがNot nullの場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings({"unchecked","deprecation"})
    public void testAddBatch02() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new UpdateDAOiBatisImpl_SqlMapClientTemplateStub01());
        ThreadLocal<List<SqlHolder>> batchSqls
            = new ThreadLocal<List<SqlHolder>>();
        List<SqlHolder> sqlHolders = new ArrayList<SqlHolder>();
        sqlHolders.add(new SqlHolder("sqlID1","1"));
        batchSqls.set(sqlHolders);
        UTUtil.setPrivateField(dao, "batchSqls", batchSqls);

        // テスト実施
        dao.addBatch("sqlID2", "2");

        // 判定
        Object obj = UTUtil.getPrivateField(dao, "batchSqls");
        sqlHolders = ((ThreadLocal<List<SqlHolder>>) obj).get();
        SqlHolder sqlHolder = sqlHolders.get(0);
        assertEquals("sqlID1", sqlHolder.getSqlID());
        assertEquals("1", sqlHolder.getBindParams());
        sqlHolder = sqlHolders.get(1);
        assertEquals("sqlID2", sqlHolder.getSqlID());
        assertEquals("2", sqlHolder.getBindParams());
    }

    /**
     * testExecuteBatch01()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(状態) batchSqls.get()にて取得したList「sqlHolders」:null<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     *                    メッセージ：No batch sql. Call #addBatch(String, Object) at least 1 time.<br>
     *         (状態変化) ログ:＜メッセージ＞<br>
     *                    No batch sql. Call #addBatch(String, Object) at least 1 time.<br>
     *         
     * <br>
     * sqlHoldersが、nullの場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("deprecation")
    public void testExecuteBatch01() throws Exception {
        // 前処理

        // テスト実施
        try {
            dao.executeBatch();
            fail("テスト失敗");
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals("No SqlMapClient specified", e.getMessage());
        }
    }

    /**
     * testExecuteBatch02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(状態) batchSqls.get()にて取得したList「sqlHolders」:size = 0<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     *                    メッセージ：No batch sql. Call #addBatch(String, Object) at least 1 time.<br>
     *         (状態変化) ログ:＜メッセージ＞<br>
     *                    No batch sql. Call #addBatch(String, Object) at least 1 time.<br>
     *         
     * <br>
     * sqlHoldersが、size=0の場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("deprecation")
    public void testExecuteBatch02() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new UpdateDAOiBatisImpl_SqlMapClientTemplateStub01());
        ThreadLocal<List<SqlHolder>> batchSqls
            = new ThreadLocal<List<SqlHolder>>();
        List<SqlHolder> sqlHolders = new ArrayList<SqlHolder>();
        batchSqls.set(sqlHolders);
        UTUtil.setPrivateField(dao, "batchSqls", batchSqls);

        // テスト実施
        int i = dao.executeBatch();

        // 判定
        assertEquals(1, i);
        UpdateDAOiBatisImple_SqlMapSessionImpl sqlMap
            = (UpdateDAOiBatisImple_SqlMapSessionImpl) dao
                .getSqlMapClientTemplate().getSqlMapClient().openSession();
        assertTrue(sqlMap.isStartBatchCalled());
        assertFalse(sqlMap.isUpdateCalled());
        assertTrue(sqlMap.isExecuteBatchCalled());
    }

    /**
     * testExecuteBatch03()
     * <br><br>
     * 
     * (正常系) 
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(状態) batchSqls.get()にて取得したList「sqlHolders」:size = 1<br>
     *         
     * <br>
     * 期待値：(戻り値) int:1<br>
     *         (状態変化) startBatchの呼出確認:呼び出されることを確認する。<br>
     *         (状態変化) addBatchの呼出確認:呼び出されることを確認する。<br>
     *         (状態変化) executeBatchの呼出確認:呼び出されることを確認する。<br>
     *         (状態変化) batchSqls.get()にて取得したList「sqlHolders」:null<br>
     *         
     * <br>
     * sqlHoldersが、size=1の場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("deprecation")
    public void testExecuteBatch03() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(new UpdateDAOiBatisImpl_SqlMapClientTemplateStub01());
        ThreadLocal<List<SqlHolder>> batchSqls = new ThreadLocal<List<SqlHolder>>();
        List<SqlHolder> sqlHolders = new ArrayList<SqlHolder>();
        sqlHolders.add(new SqlHolder("sqlID1","1"));
        batchSqls.set(sqlHolders);
        UTUtil.setPrivateField(dao, "batchSqls", batchSqls);

        // テスト実施
        int i = dao.executeBatch();

        // 判定
        assertEquals(1, i);
        UpdateDAOiBatisImple_SqlMapSessionImpl sqlMap
            = (UpdateDAOiBatisImple_SqlMapSessionImpl) dao
                .getSqlMapClientTemplate().getSqlMapClient().openSession();
        assertTrue(sqlMap.isStartBatchCalled());
        assertTrue(sqlMap.isUpdateCalled());
        assertTrue(sqlMap.isExecuteBatchCalled());
        List<String> id = sqlMap.getId();
        List<Object> param = sqlMap.getParam();
        assertEquals("sqlID1", id.get(0));
        assertEquals("1", param.get(0));
    }

    /**
     * testExecuteBatch04()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(状態) batchSqls.get()にて取得したList「sqlHolders」:size = 3<br>
     *         
     * <br>
     * 期待値：(戻り値) int:3<br>
     *         (状態変化) startBatchの呼出確認:呼び出されることを確認する。<br>
     *         (状態変化) addBatchの呼出確認:呼び出されることを確認する。<br>
     *         (状態変化) executeBatchの呼出確認:呼び出されることを確認する。<br>
     *         (状態変化) batchSqls.get()にて取得したList「sqlHolders」:null<br>
     *         
     * <br>
     * sqlHoldersが、size=3の場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("deprecation")
    public void testExecuteBatch04() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(new UpdateDAOiBatisImpl_SqlMapClientTemplateStub01());
        ThreadLocal<List<SqlHolder>> batchSqls = new ThreadLocal<List<SqlHolder>>();
        List<SqlHolder> sqlHolders = new ArrayList<SqlHolder>();
        sqlHolders.add(new SqlHolder("sqlID1","1"));
        sqlHolders.add(new SqlHolder("sqlID2","2"));
        sqlHolders.add(new SqlHolder("sqlID3","3"));
        batchSqls.set(sqlHolders);
        UTUtil.setPrivateField(dao, "batchSqls", batchSqls);

        // テスト実施
        int i = dao.executeBatch();

        // 判定
        assertEquals(1, i);
        UpdateDAOiBatisImple_SqlMapSessionImpl sqlMap
            = (UpdateDAOiBatisImple_SqlMapSessionImpl) dao
                .getSqlMapClientTemplate().getSqlMapClient().openSession();
        assertTrue(sqlMap.isStartBatchCalled());
        assertTrue(sqlMap.isUpdateCalled());
        assertTrue(sqlMap.isExecuteBatchCalled());
        List<String> id = sqlMap.getId();
        List<Object> param = sqlMap.getParam();
        assertEquals("sqlID1", id.get(0));
        assertEquals("sqlID2", id.get(1));
        assertEquals("sqlID3", id.get(2));
        assertEquals("1", param.get(0));
        assertEquals("2", param.get(1));
        assertEquals("3", param.get(2));
    }

    /**
     * testExecuteBatchList01()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) sqlHolders:null<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ：No SqlMapClient specified<br>
     *         
     * <br>
     * sqlHoldersが、nullの場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExecuteBatchList01() throws Exception {
        // 前処理

        // テスト実施
        try {
            dao.executeBatch(null);
            fail("テスト失敗");
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals("No SqlMapClient specified", e.getMessage());
        }
    }

    /**
     * testExecuteBatchList02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) sqlHolders:size=0<br>
     *         
     * <br>
     * 期待値：(状態変化) startBatchの呼出確認:呼び出されることを確認する。<br>
     *         (状態変化) updateの呼出確認:呼び出されない<br>
     *         (状態変化) executeBatchの呼出確認:呼び出されることを確認する。<br>
     *         
     * <br>
     * sqlHoldersが、size=0の場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExecuteBatchList02() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new UpdateDAOiBatisImpl_SqlMapClientTemplateStub01());
        List<SqlHolder> sqlHolders = new ArrayList<SqlHolder>();

        // テスト実施
        int i = dao.executeBatch(sqlHolders);

        // 判定
        assertEquals(1, i);
        UpdateDAOiBatisImple_SqlMapSessionImpl sqlMap
            = (UpdateDAOiBatisImple_SqlMapSessionImpl) dao
                .getSqlMapClientTemplate().getSqlMapClient().openSession();
        assertTrue(sqlMap.isStartBatchCalled());
        assertFalse(sqlMap.isUpdateCalled());
        assertTrue(sqlMap.isExecuteBatchCalled());
    }

    /**
     * testExecuteBatchList03()
     * <br><br>
     * 
     * (正常系) 
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) sqlHolders:size=1<br>
     *         
     * <br>
     * 期待値：(戻り値) int:1<br>
     *         (状態変化) startBatchの呼出確認:呼び出されることを確認する。<br>
     *         (状態変化) updateの呼出確認:呼び出されることを確認する。<br>
     *         (状態変化) executeBatchの呼出確認:呼び出されることを確認する。<br>
     *         (状態変化) batchSqls.get()にて取得したList「sqlHolders」:null<br>
     *         
     * <br>
     * sqlHoldersが、size=1の場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExecuteBatchList03() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(new UpdateDAOiBatisImpl_SqlMapClientTemplateStub01());
        List<SqlHolder> sqlHolders = new ArrayList<SqlHolder>();
        sqlHolders.add(new SqlHolder("sqlID1","1"));

        // テスト実施
        int i = dao.executeBatch(sqlHolders);

        // 判定
        assertEquals(1, i);
        UpdateDAOiBatisImple_SqlMapSessionImpl sqlMap
            = (UpdateDAOiBatisImple_SqlMapSessionImpl) dao
                .getSqlMapClientTemplate().getSqlMapClient().openSession();
        assertTrue(sqlMap.isStartBatchCalled());
        assertTrue(sqlMap.isUpdateCalled());
        assertTrue(sqlMap.isExecuteBatchCalled());
        List<String> id = sqlMap.getId();
        List<Object> param = sqlMap.getParam();
        assertEquals("sqlID1", id.get(0));
        assertEquals("1", param.get(0));
    }

    /**
     * testExecuteBatchList04()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) sqlHolders:size=3<br>
     *         
     * <br>
     * 期待値：(戻り値) int:3<br>
     *         (状態変化) startBatchの呼出確認:呼び出されることを確認する。<br>
     *         (状態変化) updateの呼出確認:呼び出されることを確認する。<br>
     *         (状態変化) executeBatchの呼出確認:呼び出されることを確認する。<br>
     *         (状態変化) batchSqls.get()にて取得したList「sqlHolders」:null<br>
     *         
     * <br>
     * sqlHoldersが、size=3の場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExecuteBatchList04() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(new UpdateDAOiBatisImpl_SqlMapClientTemplateStub01());
        List<SqlHolder> sqlHolders = new ArrayList<SqlHolder>();
        sqlHolders.add(new SqlHolder("sqlID1","1"));
        sqlHolders.add(new SqlHolder("sqlID2","2"));
        sqlHolders.add(new SqlHolder("sqlID3","3"));

        // テスト実施
        int i = dao.executeBatch(sqlHolders);

        // 判定
        assertEquals(1, i);
        UpdateDAOiBatisImple_SqlMapSessionImpl sqlMap
            = (UpdateDAOiBatisImple_SqlMapSessionImpl) dao
                .getSqlMapClientTemplate().getSqlMapClient().openSession();
        assertTrue(sqlMap.isStartBatchCalled());
        assertTrue(sqlMap.isUpdateCalled());
        assertTrue(sqlMap.isExecuteBatchCalled());
        List<String> id = sqlMap.getId();
        List<Object> param = sqlMap.getParam();
        assertEquals("sqlID1", id.get(0));
        assertEquals("sqlID2", id.get(1));
        assertEquals("sqlID3", id.get(2));
        assertEquals("1", param.get(0));
        assertEquals("2", param.get(1));
        assertEquals("3", param.get(2));
    }

}
