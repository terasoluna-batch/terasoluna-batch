/*
 * $Id:$
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import java.util.HashMap;
import java.util.Map;

import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.AbstractFileUpdateDAO} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイルのデータ取得用のFileLineWriterを生成する。<br>
 * （テスト実行のため、スタブを用意する）
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.AbstractFileUpdateDAO
 */
public class AbstractFileUpdateDAOTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(AbstractFileUpdateDAOTest.class);
    }

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 終了処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * コンストラクタ。
     * @param name このテストケースの名前。
     */
    public AbstractFileUpdateDAOTest(String name) {
        super(name);
    }

    /**
     * testSetColumnFormatterMap01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) columnFormatterMap: Map<String, ColumnFormatter>インスタンス<br>
     * <br>
     * 期待値：(状態変化) this.columnFormatterMap:引数で指定したMap<String, ColumnFormatter>インスタンス<br>
     * <br>
     * columnFormatterMapのsetterメソッドの値が 正しく設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testSetColumnFormatterMap01() throws Exception {
        // テスト対象のインスタンス化
        AbstractFileUpdateDAO abstractFileUpdateDAO = new AbstractFileUpdateDAO_Stub01();

        // 引数の設定
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();

        // 前提条件の設定
        // なし

        // テスト実施
        abstractFileUpdateDAO.setColumnFormatterMap(columnFormatterMap);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result = UTUtil.getPrivateField(abstractFileUpdateDAO,
                "columnFormatterMap");
        assertSame(columnFormatterMap, result);
    }

    /**
     * testGetColumnFormatterMap01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) columnFormatterMap: Map<String, ColumnFormatter>インスタンスl<br>
     * <br>
     * 期待値：(戻り値) columnFormatterMap: Map<String, ColumnFormatter>インスタンス<br>
     * <br>
     * columnFormatterMapのgetterメソッドが正しく値を 取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetColumnFormatterMap01() throws Exception {
        // テスト対象のインスタンス化
        AbstractFileUpdateDAO abstractFileUpdateDAO = new AbstractFileUpdateDAO_Stub01();

        // 引数の設定
        // なし

        // 前提条件の設定
        Map<String, ColumnFormatter> textGetterMap = new HashMap<String, ColumnFormatter>();
        UTUtil.setPrivateField(abstractFileUpdateDAO, "columnFormatterMap",
                textGetterMap);

        // テスト実施
        Map<String, ColumnFormatter> result = abstractFileUpdateDAO
                .getColumnFormatterMap();

        // 返却値の確認
        assertSame(textGetterMap, result);

        // 状態変化の確認
        // なし
    }

}
