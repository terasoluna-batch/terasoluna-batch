/*
 * $Id:$
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.terasoluna.fw.file.dao.FileLineWriter;
import jp.terasoluna.fw.file.ut.VMOUTUtil;
import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.PlainFileUpdateDAO} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイル行オブジェクトを用いないファイル出力用のFileLineWriterを生成する。<br>
 * AbstractFileUpdateDAOのサブクラス。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.PlainFileUpdateDAO
 */
public class PlainFileUpdateDAOTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(PlainFileUpdateDAOTest.class);
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
    public PlainFileUpdateDAOTest(String name) {
        super(name);
    }

    /**
     * testExecute01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:PlainFileUpdateDAO01.txt<br>
     * 　データを持たないファイルのパス<br>
     * (引数) clazz:PlainFileUpdateDAO_Stub01<br>
     * 　空実装<br>
     * (状態) AbstractFileUpdateDAO.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"java.lang.String"=NullColumnFormatterインスタンス<br>
     * <br>
     * 期待値：(戻り値) FileLineWriter:PlainFileLineWriterのインスタンス<br>
     * (状態変化) PlainFileLineWriter#PlainFileLineWriter():１回呼び出されること<br>
     * 引数が渡されること<br>
     * <br>
     * 正常パターン<br>
     * 引数がそれぞれnot nullであれば、戻り値が帰ってくることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testExecute01() throws Exception {
        // テスト対象のインスタンス化
        PlainFileUpdateDAO fileUpdateDAO = new PlainFileUpdateDAO();

        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<PlainFileUpdateDAO_Stub01> clazz = PlainFileUpdateDAO_Stub01.class;

        // 前提条件の設定
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        UTUtil.setPrivateField(fileUpdateDAO, "columnFormatterMap",
                columnFormatterMap);

        // テスト実施
        FileLineWriter fileLineWriter = fileUpdateDAO.execute(fileName, clazz);

        // 返却値の確認
        assertEquals(PlainFileLineWriter.class, fileLineWriter.getClass());

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(PlainFileLineWriter.class,
                "<init>"));
        List arguments = VMOUTUtil.getArguments(PlainFileLineWriter.class,
                "<init>", 0);
        assertSame(fileName, arguments.get(0));
        assertSame(clazz, arguments.get(1));
        assertSame(columnFormatterMap, arguments.get(2));
    }
}
