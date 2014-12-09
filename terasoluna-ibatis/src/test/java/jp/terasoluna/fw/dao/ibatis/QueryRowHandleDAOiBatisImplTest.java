/**
 * 
 */
package jp.terasoluna.fw.dao.ibatis;

import jp.terasoluna.fw.dao.event.DataRowHandler;
import jp.terasoluna.utlib.LogUTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.co.nttdata.illigra.lucy.illigralib.dao.ibatis.RowHandledQueryDAOiBatisImpl}
 * クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * RowHandledQueryDAOインタフェースのiBATIS用実装クラス。
 * <p>
 * 
 * @see jp.terasoluna.fw.dao.ibatis.QueryDAOiBatisImpl
 */
public class QueryRowHandleDAOiBatisImplTest extends TestCase {

    /**
     * テスト対象クラス
     */
    private QueryRowHandleDAOiBatisImpl dao = new QueryRowHandleDAOiBatisImpl();

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
        dao = new QueryRowHandleDAOiBatisImpl();
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
     * testExecuteWithRowHandler01()
     * <br><br>
     *
     * 正常系
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     *         (引数) bindParams:"hoge"<br>
     *         (引数) rowHandler:not null<br>
     *         (前提条件) sqlMapClientTemplate:SqlMapClientTemplateStub01<br>
     * <br>
     * 期待値：(戻り値) なし<br>
     *         (状態変化) SqlMapClientTemplateの呼出確認:引数がsqlID、bindParamsで
     *                    呼び出されている事を確認<br>
     *         (状態変化) DataRowHandlerの呼出確認:【INFOログ】"param=hoge"<br>
     *
     * <br>
     * バインドパラメータがnullでない場合、正常にDataRowHandlerが
     * 実行されることを確認
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testExecuteWithRowHandler01() throws Exception {
        // 前処理
        QueryRowHandleDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapClTemp = 
            new QueryRowHandleDAOiBatisImpl_SqlMapClientTemplateStub01();
        dao.setSqlMapClientTemplate(sqlMapClTemp);

        DataRowHandler rowHandler = 
                    new QueryRowHandleDAOiBatisImpl_DataRowHandlerImpl();

        // テスト実施
        dao.executeWithRowHandler("sqlId", "hoge", rowHandler);

        // 検査
        assertTrue(sqlMapClTemp.isCalled());
        assertEquals("sqlId", sqlMapClTemp.getStatementName());
        assertEquals("hoge", sqlMapClTemp.getParameterObject());
        // RowHandlerWrappere経由でDataRowHandlerが実行されたことの確認
        assertTrue(LogUTUtil.checkInfo("param=hoge"));
    }

    /**
     * testExecuteWithRowHandler02()
     * <br><br>
     *
     * 正常系
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) sqlID:"sqlId"<br>
     *         (引数) bindParams:null<br>
     *         (引数) rowHandler:not null<br>
     *         (前提条件) sqlMapClientTemplate:SqlMapClientTemplateStub01<br>
     * <br>
     * 期待値：(戻り値) なし<br>
     *         (状態変化) SqlMapClientTemplateの呼出確認:引数がsqlID、bindParamsで
     *                    呼び出されている事を確認<br>
     *         (状態変化) DataRowHandlerの呼出確認:【INFOログ】"param=hoge"<br>
     *
     * <br>
     * バインドパラメータがnullの場合、正常にDataRowHandlerが
     * 実行されることを確認
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testExecuteWithRowHandler02() throws Exception {
        // 前処理
        QueryRowHandleDAOiBatisImpl_SqlMapClientTemplateStub01 sqlMapClTemp = new QueryRowHandleDAOiBatisImpl_SqlMapClientTemplateStub01();
        dao.setSqlMapClientTemplate(sqlMapClTemp);

        DataRowHandler rowHandler = new QueryRowHandleDAOiBatisImpl_DataRowHandlerImpl();

        // テスト実施
        dao.executeWithRowHandler("sqlId", null, rowHandler);

        // 検査
        assertTrue(sqlMapClTemp.isCalled());
        assertEquals("sqlId", sqlMapClTemp.getStatementName());
        assertNull(sqlMapClTemp.getParameterObject());
        // RowHandlerWrappere経由でDataRowHandlerが実行されたことの確認
        assertTrue(LogUTUtil.checkInfo("param is null"));
    }

}
