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
 * {@link jp.terasoluna.fw.file.dao.standard.VariableFileUpdateDAO} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> 可変長ファイル用のFileLineWriterを生成する。<br>
 * AbstractFileUpdateDAOのサブクラス。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.VariableFileUpdateDAO
 */
public class VariableFileUpdateDAOTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(VariableFileUpdateDAOTest.class);
    }

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VMOUTUtil.initialize();
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
    public VariableFileUpdateDAOTest(String name) {
        super(name);
    }

    /**
     * testExecute01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:VariableFileUpdateDAO_execute01.txt<br>
     * 　データを持たないファイルのパス<br>
     * (引数) clazz:VariableFileUpdateDAO_Stub01<br>
     * 　空実装<br>
     * (状態) AbstractFileUpdateDAO.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"java.lang.String"=NullColumnFormatterインスタンス<br>
     * <br>
     * 期待値：(戻り値) FileLineWriter:VariableFileLineWriterのインスタンス<br>
     * (状態変化) VariableFileLineWriter:コンストラクタが1回呼ばれること。<br>
     * 引数が呼び出しパラメータに渡ってくること。<br>
     * <br>
     * 引数がそれぞれnot nullであれば、戻り値が帰ってくることを確認する。<br>
     * このメソッドは、VariableFileLineWriterのコンストラクタを呼び出すだけなので、引数のバリエーションは一つしか行わない。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testExecute01() throws Exception {
        // テスト対象のインスタンス化
        VariableFileUpdateDAO fileUpdateDAO = new VariableFileUpdateDAO();

        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileUpdateDAO_Stub01> clazz = VariableFileUpdateDAO_Stub01.class;

        // 前提条件の設定
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        UTUtil.setPrivateField(fileUpdateDAO, "columnFormatterMap",
                columnFormatterMap);

        // テスト実施
        FileLineWriter fileLineWriter = fileUpdateDAO.execute(fileName, clazz);

        // 返却値の確認
        assertEquals(VariableFileLineWriter.class, fileLineWriter.getClass());

        // 状態変化の確認
        List arguments = VMOUTUtil.getArguments(VariableFileLineWriter.class,
                "<init>", 0);
        assertEquals(fileName, arguments.get(0));
        assertEquals(clazz, arguments.get(1));
        assertEquals(columnFormatterMap, arguments.get(2));
    }

}
