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

import java.util.List;
import java.util.Map;

import jp.terasoluna.fw.dao.IllegalClassTypeException;
import jp.terasoluna.utlib.LogUTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.dao.ibatis.QueryDAOiBatisImpl}
 * クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * QueryDAOインタフェースのiBATIS用実装クラス。
 * <p>
 * 
 * @see jp.terasoluna.fw.dao.ibatis.QueryDAOiBatisImpl
 */
public class QueryDAOiBatisImplTest extends TestCase {

    /**
     * テスト対象クラス
     */
    private QueryDAOiBatisImpl dao = new QueryDAOiBatisImpl();

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * 
     * @param args
     *            java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(QueryDAOiBatisImplTest.class);
    }

    /**
     * 初期化処理を行う。
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dao = new QueryDAOiBatisImpl();
    }

    /**
     * 終了処理を行う。
     * 
     * @throws Exception
     *             このメソッドで発生した例外
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
     * @param name
     *            このテストケースの名前。
     */
    public QueryDAOiBatisImplTest(String name) {
        super(name);
    }

    /**
     * testExecuteForObject01() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) clazz:"java.lang.String"<br>
     * (状態) queryForObjectの戻りのObject:String("abc")<br>
     * 
     * <br>
     * 期待値：(戻り値) E:String("abc")<br>
     * queryForObjectの戻りのObjectが引数clazzのクラスに
     * 変換されて返却されることを確認。<br>
     * (状態変化) queryForObjectの呼出確認:引数がsqlId、
     * bindParamsで呼び出されている事を確認<br>
     * 
     * <br>
     * queryForObjectの戻り値が、引数clazzの方にキャストでき、返却される場合
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForObject01() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new QueryDAOiBatisImpl_SqlMapClientTemplateStub01());

        // テスト実施
        String str = dao.executeForObject("sqlId", "1", String.class);

        // 戻り値の確認
        assertEquals("abc", str);

        // 呼び出し確認
        QueryDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapTemp =
            (QueryDAOiBatisImpl_SqlMapClientTemplateStub01) dao
                .getSqlMapClientTemplate();
        assertTrue(sqlMapTemp.isCalled());
        assertEquals("sqlId", sqlMapTemp.getStatementName());
        assertEquals("1", sqlMapTemp.getParameterObject());
    }

    /**
     * testExecuteForObject02() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) clazz:null<br>
     * (状態) queryForObjectの戻りのObject:String("abc")<br>
     * 
     * <br>
     * 期待値：(状態変化) queryForObjectの呼出確認:引数がsqlId、
     * bindParamsで呼び出されている事を確認<br>
     * (状態変化) 例外:IllegalClassTypeException<br>
     * (状態変化) ログ:＜メッセージ＞<br>
     * "The mistake Class Type of the argument."<br>
     * 
     * <br>
     * 引数clazzがnullの場合 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForObject02() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new QueryDAOiBatisImpl_SqlMapClientTemplateStub01());

        // テスト実施
        String str = dao.executeForObject("sqlId", "1", null);

        // 戻り値の確認
        assertNull(str);

        // 呼び出し確認
        QueryDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapTemp =
            (QueryDAOiBatisImpl_SqlMapClientTemplateStub01) dao
                .getSqlMapClientTemplate();
        assertTrue(sqlMapTemp.isCalled());
        assertEquals("sqlId", sqlMapTemp.getStatementName());
        assertEquals("1", sqlMapTemp.getParameterObject());
    }

    /**
     * testExecuteForObject03() <br>
     * <br>
     * 
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) clazz:"java.lang.Integer"<br>
     * (状態) queryForObjectの戻りのObject:String("abc")<br>
     * 
     * <br>
     * 期待値： (状態変化) queryForObjectの呼出確認:引数がsqlId、
     * bindParamsで呼び出されている事を確認<br>
     * (状態変化) 例外:IllegalClassTypeException<br>
     * ラップされた例外：ClassCastException (状態変化) ログ:＜メッセージ＞<br>
     * "The mistake Class Type of the argument."<br>
     * 
     * <br>
     * 引数clazzとqueryForObjectの戻りに方が違う場合 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForObject03() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new QueryDAOiBatisImpl_SqlMapClientTemplateStub01());

        // テスト実施
        try {
            dao.executeForObject("sqlId", "1", Integer.class);
            fail();
        } catch (IllegalClassTypeException e) {
            assertEquals(ClassCastException.class.getName(), e.getCause()
                    .getClass().getName());
            assertTrue(LogUTUtil.checkError(
                    IllegalClassTypeException.ERROR_ILLEGAL_CLASS_TYPE));
        }

        // 呼び出し確認
        QueryDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapTemp =
            (QueryDAOiBatisImpl_SqlMapClientTemplateStub01) dao
                .getSqlMapClientTemplate();
        assertTrue(sqlMapTemp.isCalled());
        assertEquals("sqlId", sqlMapTemp.getStatementName());
        assertEquals("1", sqlMapTemp.getParameterObject());
    }

    /**
     * testExecuteForObject04() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) clazz:"java.lang.String"<br>
     * (状態) queryForObjectの戻りのObject:null<br>
     * 
     * <br>
     * 期待値：(戻り値) E:null<br>
     * (状態変化) queryForObjectの呼出確認:引数がsqlId、
     * bindParamsで呼び出されている事を確認<br>
     * 
     * <br>
     * queryForObjectの戻り値が、nullの場合 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForObject04() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new QueryDAOiBatisImpl_SqlMapClientTemplateStub02());

        // テスト実施
        String str = dao.executeForObject("sqlId", "1", String.class);

        // 戻り値の確認
        assertNull(str);

        // 呼び出し確認
        QueryDAOiBatisImpl_SqlMapClientTemplateStub02 sqlMapTemp =
            (QueryDAOiBatisImpl_SqlMapClientTemplateStub02) dao
                .getSqlMapClientTemplate();
        assertTrue(sqlMapTemp.isCalled());
        assertEquals("sqlId", sqlMapTemp.getStatementName());
        assertEquals("1", sqlMapTemp.getParameterObject());
    }

    /**
     * testExecuteForMap01() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (状態) queryForMapの戻りのMap:not null<br>
     * 
     * <br>
     * 期待値：(戻り値) Map:not null<br>
     * (状態変化) queryForObjectの呼出確認:引数がsqlId、bindParams で呼び出されている事を確認<br>
     * 
     * <br>
     * queryForObjectの戻りがnot nullで、その戻りがそのまま返却される場合 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForMap01() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new QueryDAOiBatisImpl_SqlMapClientTemplateStub03());

        // テスト実施
        Map<String, Object> str = dao.executeForMap("sqlId", "1");

        // 戻り値の確認
        assertNotNull(str);

        // 呼び出し確認
        QueryDAOiBatisImpl_SqlMapClientTemplateStub03 sqlMapTemp =
            (QueryDAOiBatisImpl_SqlMapClientTemplateStub03) dao
                .getSqlMapClientTemplate();
        assertTrue(sqlMapTemp.isCalled());
        assertEquals("sqlId", sqlMapTemp.getStatementName());
        assertEquals("1", sqlMapTemp.getParameterObject());
    }

    /**
     * testExecuteForObjectArrayStringObjectClass01() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) clazz:"java.lang.String"<br>
     * (状態) queryForListの戻りのList:not null<br>
     * [String("a"),String("b"), String("c")]<br>
     * 
     * <br>
     * 期待値：(戻り値) E[]:String("a")<br>
     * String("b")<br>
     * String("c")<br>
     * (状態変化) queryForListの呼出確認:引数がsqlId、bindParamsで
     * 呼び出されている事を確認<br>
     * 
     * <br>
     * queryForListの戻りがnot nullで、引数clazzの型の配列に変換できた場合 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForObjectArrayStringObjectClass01() 
            throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new QueryDAOiBatisImpl_SqlMapClientTemplateStub01());

        // テスト実施
        String[] str = dao.executeForObjectArray("sqlId", "1", String.class);

        // 戻り値の確認
        assertEquals("a", str[0]);
        assertEquals("b", str[1]);
        assertEquals("c", str[2]);

        // 呼び出し確認
        QueryDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapTemp =
            (QueryDAOiBatisImpl_SqlMapClientTemplateStub01) dao
                .getSqlMapClientTemplate();
        assertTrue(sqlMapTemp.isCalled());
        assertEquals("sqlId", sqlMapTemp.getStatementName());
        assertEquals("1", sqlMapTemp.getParameterObject());
    }

    /**
     * testExecuteForObjectArrayStringObjectClass02() <br>
     * <br>
     * 
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) clazz:null<br>
     * (状態) queryForListの戻りのList:not null<br>
     * 
     * <br>
     * 期待値：(状態変化) 例外:llegalClassTypeException<br>
     * (状態変化) ログ:＜メッセージ＞<br>
     * "The mistake Class Type of the argument."<br>
     * 
     * <br>
     * 引数clazzがnullの場合 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForObjectArrayStringObjectClass02() 
            throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new QueryDAOiBatisImpl_SqlMapClientTemplateStub01());

        // テスト実施
        try {
            dao.executeForObjectArray("sqlId", "1", null);
            fail();
        } catch (IllegalClassTypeException e) {
            assertNull(e.getCause());
            assertTrue(LogUTUtil.checkError(
                    IllegalClassTypeException.ERROR_ILLEGAL_CLASS_TYPE));
        }

        // 呼び出し確認
        QueryDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapTemp =
            (QueryDAOiBatisImpl_SqlMapClientTemplateStub01) dao
                .getSqlMapClientTemplate();
        assertFalse(sqlMapTemp.isCalled());
    }

    /**
     * testExecuteForObjectArrayStringObjectClass03() <br>
     * <br>
     * 
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) clazz:"java.lang.Integer"<br>
     * (状態) queryForListの戻りのList:"not null<br>
     * [String(""a""),String(""b""), String(""c"")]"<br>
     * 
     * <br>
     * 期待値：(状態変化) 例外:IllegalClassTypeException<br>
     * ラップされた例外：<br>
     * ArrayStoreException<br>
     * (状態変化) ログ:＜メッセージ＞<br>
     * "The mistake Class Type of the argument."<br>
     * 
     * <br>
     * queryForListの戻りと引数clazzの型が違う場合 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForObjectArrayStringObjectClass03() 
            throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new QueryDAOiBatisImpl_SqlMapClientTemplateStub01());

        // テスト実施
        try {
            dao.executeForObjectArray("sqlId", "1", Integer.class);
            fail();
        } catch (IllegalClassTypeException e) {
            assertEquals(ArrayStoreException.class.getName(), e.getCause()
                    .getClass().getName());
            assertTrue(LogUTUtil.checkError(
                    IllegalClassTypeException.ERROR_ILLEGAL_CLASS_TYPE));
        }

        // 呼び出し確認
        QueryDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapTemp =
            (QueryDAOiBatisImpl_SqlMapClientTemplateStub01) dao
                .getSqlMapClientTemplate();
        assertTrue(sqlMapTemp.isCalled());
        assertEquals("sqlId", sqlMapTemp.getStatementName());
        assertEquals("1", sqlMapTemp.getParameterObject());
    }

    /**
     * testExecuteForMapArrayStringObject01() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * 
     * <br>
     * 期待値：(戻り値) Map配列<br>
     * [0]=Map["abc","123"]<br>
     * (状態変化) executeForObjectArrayの呼出確認:引数がsqlId、 bindParams,
     * Map.classで呼び出されている事を確認<br>
     * 
     * <br>
     * 内部では、executeForObjectArrayを呼び出しているため、
     * テストはexecuteForObjectArrayのテストケースに包含する。 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForMapArrayStringObject01() throws Exception {
        // 前処理
        dao = new QueryDAOiBatisImpl_QueryDAOiBatisImplStub01();

        // テスト実施
        Map<String, Object>[] map = dao.executeForMapArray("sqlId", "1");

        // 判定
        assertTrue(map[0].containsKey("abc"));
        assertEquals("123", map[0].get("abc"));
        assertTrue(((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao)
                .isCalled());
        assertEquals("sqlId",
                ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao).getSqlID());
        assertEquals("1", ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao)
                .getBindParams());
        assertEquals(Map.class.getName(),
                ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao).getClazz()
                        .getName());
    }

    /**
     * testExecuteForObjectArrayStringObjectClassintint01() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) clazz:"java.lang.String"<br>
     * (引数) beginIndex:10<br>
     * (引数) maxCount:100<br>
     * (状態) queryForListの戻りのList:not null<br>
     * [String("a"),String("b"), String("c")]<br>
     * 
     * <br>
     * 期待値：(戻り値) E[]:String("a")<br>
     * String("b")<br>
     * String("c")<br>
     * (状態変化) queryForListの呼出確認:引数がsqlID, bindParams, beginIndex,
     * maxCountで呼び出されている事を確認<br>
     * 
     * <br>
     * queryForListの戻りがnot nullで、引数clazzの型の配列に変換できた場合 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForObjectArrayStringObjectClassintint01()
            throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new QueryDAOiBatisImpl_SqlMapClientTemplateStub01());

        // テスト実施
        String[] str = dao.executeForObjectArray("sqlId", "1", String.class,
                10, 100);

        // 戻り値の確認
        assertEquals("a", str[0]);
        assertEquals("b", str[1]);
        assertEquals("c", str[2]);

        // 呼び出し確認
        QueryDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapTemp =
            (QueryDAOiBatisImpl_SqlMapClientTemplateStub01) dao
                .getSqlMapClientTemplate();
        assertTrue(sqlMapTemp.isCalled());
        assertEquals("sqlId", sqlMapTemp.getStatementName());
        assertEquals("1", sqlMapTemp.getParameterObject());
        assertEquals(10, sqlMapTemp.getSkipResults());
        assertEquals(100, sqlMapTemp.getMaxResults());
    }

    /**
     * testExecuteForObjectArrayStringObjectClassintint02() <br>
     * <br>
     * 
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) clazz:null<br>
     * (状態) queryForListの戻りのList:not null<br>
     * 
     * <br>
     * 期待値：(状態変化) 例外:llegalClassTypeException<br>
     * (状態変化) ログ:＜メッセージ＞<br>
     * "The mistake Class Type of the argument."<br>
     * 
     * <br>
     * 引数clazzがnullの場合 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForObjectArrayStringObjectClassintint02()
            throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new QueryDAOiBatisImpl_SqlMapClientTemplateStub01());

        // テスト実施
        try {
            dao.executeForObjectArray("sqlId", "1", null, 10, 100);
            fail();
        } catch (IllegalClassTypeException e) {
            assertNull(e.getCause());
            assertTrue(LogUTUtil.checkError(
                    IllegalClassTypeException.ERROR_ILLEGAL_CLASS_TYPE));
        }

        // 呼び出し確認
        QueryDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapTemp =
            (QueryDAOiBatisImpl_SqlMapClientTemplateStub01) dao
                .getSqlMapClientTemplate();
        assertFalse(sqlMapTemp.isCalled());
    }

    /**
     * testExecuteForObjectArrayStringObjectClassintint03() <br>
     * <br>
     * 
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) clazz:"java.lang.String"<br>
     * (状態) queryForListの戻りのList:not null<br>
     * [Integer(1)]<br>
     * 
     * <br>
     * 期待値：(状態変化) 例外:IllegalClassTypeException<br>
     * ラップされた例外：<br>
     * ArrayStoreException<br>
     * (状態変化) ログ:＜メッセージ＞<br>
     * "The mistake Class Type of the argument."<br>
     * 
     * <br>
     * queryForListの戻りと引数clazzの型が違う場合 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForObjectArrayStringObjectClassintint03()
            throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new QueryDAOiBatisImpl_SqlMapClientTemplateStub01());

        // テスト実施
        try {
            dao.executeForObjectArray("sqlId", "1", Integer.class, 10, 100);
            fail();
        } catch (IllegalClassTypeException e) {
            assertEquals(ArrayStoreException.class.getName(), e.getCause()
                    .getClass().getName());
            assertTrue(LogUTUtil.checkError(
                    IllegalClassTypeException.ERROR_ILLEGAL_CLASS_TYPE));
        }

        // 呼び出し確認
        QueryDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapTemp =
            (QueryDAOiBatisImpl_SqlMapClientTemplateStub01) dao
                .getSqlMapClientTemplate();
        assertTrue(sqlMapTemp.isCalled());
        assertEquals("sqlId", sqlMapTemp.getStatementName());
        assertEquals("1", sqlMapTemp.getParameterObject());
        assertEquals(10, sqlMapTemp.getSkipResults());
        assertEquals(100, sqlMapTemp.getMaxResults());
    }

    /**
     * testExecuteForMapArrayStringObjectintint01() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) beginIndex:10<br>
     * (引数) maxCount:100<br>
     * 
     * <br>
     * 期待値：(戻り値) Map配列<br>
     * [0]=Map["abc","123"]<br>
     * (状態変化) executeForObjectArrayの呼出確認:引数がsqlId、 bindParams,
     * Map.class,beginIndex, maxCountで呼び出されている事を確認<br>
     * 
     * <br>
     * 内部では、executeForObjectArrayを呼び出しているため、
     * テストはexecuteForObjectArrayのテストケースに包含する。 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForMapArrayStringObjectintint01() throws Exception {
        // 前処理
        dao = new QueryDAOiBatisImpl_QueryDAOiBatisImplStub01();

        // テスト実施
        Map<String, Object>[] map = dao.executeForMapArray("sqlId", "1", 10,
                100);

        // 判定
        assertTrue(map[0].containsKey("abc"));
        assertEquals("123", map[0].get("abc"));
        assertTrue(((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao)
                .isCalled());
        assertEquals("sqlId",
                ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao).getSqlID());
        assertEquals("1", ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao)
                .getBindParams());
        assertEquals(Map.class.getName(),
                ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao).getClazz()
                        .getName());
        assertEquals(10, ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao)
                .getBeginIndex());
        assertEquals(100, ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao)
                .getMaxCount());
    }

    /**
     * testExecuteForObjectListStringObjectClass01() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) clazz:"java.lang.String"<br>
     * (状態) queryForListの戻りのList:not null<br>
     * [String("a"),String("b"), String("c")]<br>
     * 
     * <br>
     * 期待値：(戻り値) List<E>:String("a")<br>
     * String("b")<br>
     * String("c")<br>
     * (状態変化) queryForListの呼出確認:引数がsqlId、bindParamsで
     * 呼び出されている事を確認<br>
     * 
     * <br>
     * queryForListの戻りがnot nullの場合 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForObjectListStringObjectClass01() 
            throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new QueryDAOiBatisImpl_SqlMapClientTemplateStub01());

        // テスト実施
        List<String> str = dao.executeForObjectList("sqlId", "1");

        // 戻り値の確認
        assertEquals("a", str.get(0));
        assertEquals("b", str.get(1));
        assertEquals("c", str.get(2));

        // 呼び出し確認
        QueryDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapTemp =
            (QueryDAOiBatisImpl_SqlMapClientTemplateStub01) dao
                .getSqlMapClientTemplate();
        assertTrue(sqlMapTemp.isCalled());
        assertEquals("sqlId", sqlMapTemp.getStatementName());
        assertEquals("1", sqlMapTemp.getParameterObject());
    }

    /**
     * testExecuteForMapListStringObject01() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * 
     * <br>
     * 期待値：(戻り値) MapのList<br>
     * List.get(0)=Map["abc","123"]<br>
     * (状態変化) executeForObjectListの呼出確認:引数がsqlId、 bindParams,
     * Map.classで呼び出されている事を確認<br>
     * 
     * <br>
     * 内部では、executeForObjectListを呼び出しているため、
     * テストはexecuteForObjectListのテストケースに包含する。 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForMapListStringObject01() throws Exception {
        // 前処理
        dao = new QueryDAOiBatisImpl_QueryDAOiBatisImplStub01();

        // テスト実施
        List<Map<String, Object>> map = dao.executeForMapList("sqlId", "1");

        // 判定
        assertTrue(map.get(0).containsKey("abc"));
        assertEquals("123", map.get(0).get("abc"));
        assertTrue(((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao)
                .isCalled());
        assertEquals("sqlId",
                ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao).getSqlID());
        assertEquals("1", ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao)
                .getBindParams());
    }

    /**
     * testExecuteForObjectListStringObjectClassintint01() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) clazz:"java.lang.String"<br>
     * (引数) beginIndex:10<br>
     * (引数) maxCount:100<br>
     * (状態) queryForListの戻りのList:not null<br>
     * [String("a"),String("b"), String("c")]<br>
     * 
     * <br>
     * 期待値：(戻り値) List<E>:String("a")<br>
     * String("b")<br>
     * String("c")<br>
     * (状態変化) queryForListの呼出確認:引数がsqlID, bindParams, beginIndex,
     * maxCountで呼び出されている事を確認<br>
     * 
     * <br>
     * queryForListの戻りがnot nullで、引数clazzの型の配列に変換できた場合 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForObjectListStringObjectClassintint01()
            throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(
                new QueryDAOiBatisImpl_SqlMapClientTemplateStub01());

        // テスト実施
        List<String> str = dao.executeForObjectList("sqlId", "1", 
                10, 100);

        // 戻り値の確認
        assertEquals("a", str.get(0));
        assertEquals("b", str.get(1));
        assertEquals("c", str.get(2));

        // 呼び出し確認
        QueryDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapTemp =
            (QueryDAOiBatisImpl_SqlMapClientTemplateStub01) dao
                .getSqlMapClientTemplate();
        assertTrue(sqlMapTemp.isCalled());
        assertEquals("sqlId", sqlMapTemp.getStatementName());
        assertEquals("1", sqlMapTemp.getParameterObject());
        assertEquals(10, sqlMapTemp.getSkipResults());
        assertEquals(100, sqlMapTemp.getMaxResults());
    }

    /**
     * testExecuteForMapListStringObjectintint01() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     * (引数) bindParams:"1"<br>
     * (引数) beginIndex:10<br>
     * (引数) maxCount:100<br>
     * 
     * <br>
     * 期待値：(戻り値) MapのList<br>
     * List.get(0)=Map["abc","123"]<br>
     * (状態変化) executeForObjectListの呼出確認:引数がsqlId、 bindParams,
     * Map.class,beginIndex, maxCountで呼び出されている事を確認<br>
     * 
     * <br>
     * 内部では、executeForObjectListを呼び出しているため、
     * テストはexecuteForObjectListのテストケースに包含する。 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testExecuteForMapListStringObjectintint01() throws Exception {
        // 前処理
        dao = new QueryDAOiBatisImpl_QueryDAOiBatisImplStub01();

        // テスト実施
        List<Map<String, Object>> map = dao.executeForMapList("sqlId", "1", 10,
                100);

        // 判定
        assertTrue(map.get(0).containsKey("abc"));
        assertEquals("123", map.get(0).get("abc"));
        assertTrue(((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao)
                .isCalled());
        assertEquals("sqlId",
                ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao).getSqlID());
        assertEquals("1", ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao)
                .getBindParams());

        assertEquals(10, ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao)
                .getBeginIndex());
        assertEquals(100, ((QueryDAOiBatisImpl_QueryDAOiBatisImplStub01) dao)
                .getMaxCount());
    }
    
}
