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

import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.dao.ibatis.StoredProcedureDAOiBatisImpl}
 * クラスのブラックボックステスト。
 *
 * <p>
 * <h4>【クラスの概要】</h4>
 * StoredProcedureDAOインタフェースのiBATIS用実装クラス。
 * <p>
 *
 * @see jp.terasoluna.fw.dao.ibatis.StoredProcedureDAOiBatisImpl
 */
public class StoredProcedureDAOiBatisImplTest extends TestCase {

    /**
     * テスト対象クラス
     */
    private StoredProcedureDAOiBatisImpl dao = new StoredProcedureDAOiBatisImpl();

    /**
     * このテストケースを実行する為の
     * GUI アプリケーションを起動する。
     *
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(StoredProcedureDAOiBatisImplTest.class);
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
        dao = new StoredProcedureDAOiBatisImpl();
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
    public StoredProcedureDAOiBatisImplTest(String name) {
        super(name);
    }

    /**
     * testExecuteForObject01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     *         (引数) bindParams:"1"<br>
     *
     * <br>
     * 期待値：(状態変化) queryForObjectの呼出確認:引数がsqlId、bindParamsで呼び出されている事を確認<br>
     *
     * <br>
     * defineConnection、queryForObjectを正常に呼び出す場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testExecuteForObject01() throws Exception {
        // 前処理
        dao.setSqlMapClientTemplate(new StoredProcedureDAOiBatisImpl_SqlMapClientTemplateStub01());

        // テスト実施
        dao.executeForObject("sqlId", "1");

        // 判定
        StoredProcedureDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapTemp = (StoredProcedureDAOiBatisImpl_SqlMapClientTemplateStub01) dao
                .getSqlMapClientTemplate();
        assertTrue(sqlMapTemp.isCalled());
        assertEquals("sqlId", sqlMapTemp.getStatementName());
        assertEquals("1", sqlMapTemp.getParameterObject());
    }

}
