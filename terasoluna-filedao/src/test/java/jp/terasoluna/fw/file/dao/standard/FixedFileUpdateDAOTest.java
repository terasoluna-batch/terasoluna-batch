package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import jp.terasoluna.fw.file.dao.FileLineWriter;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.FixedFileUpdateDAO} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> 固定長ファイル用のFileLineWriterを生成する。<br>
 * AbstractFileUpdateDAOのサブクラス。
 * <p>
 * @see jp.terasoluna.fw.file.dao.standard.FixedFileUpdateDAO
 */
public class FixedFileUpdateDAOTest {

    /**
     * testExecute01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:not null かつ""(空文字)でない<br>
     * Stringインスタンス<br>
     * "aaa"<br>
     * (引数) clazz: not null(FileFormatアノテーションを持つスタブを使用)<br>
     * (状態) getColumnFormatterMap（）:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"java.lang.String"=NullColumnFormatterインスタンス<br>
     * <br>
     * 期待値：(戻り値) fileLineWriter:FixedFileLineWriterのインスタンス<br>
     * (状態変化) FixedFileLineWriter#FixedFileLineWriter(): 1回呼ばれる<br>
     * 引数を確認する<br>
     * <br>
     * 正常パターン<br>
     * 引数がそれぞれnot nullであれば、戻り値が帰ってくることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExecute01() throws Exception {
        // テスト対象のインスタンス化
        FixedFileUpdateDAO fileUpdateDAO = new FixedFileUpdateDAO();

        // 引数の設定
        String fileName = "aaa";
        Class<FixedFileUpdateDAO_Stub01> clazz = FixedFileUpdateDAO_Stub01.class;

        // 前提条件の設定
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        ReflectionTestUtils.setField(fileUpdateDAO, "columnFormatterMap",
                columnFormatterMap);

        // テスト実施
        FileLineWriter<FixedFileUpdateDAO_Stub01> fileLineWriter = fileUpdateDAO
                .execute(fileName, clazz);

        // 返却値の確認
        assertEquals(FixedFileLineWriter.class.getName(), fileLineWriter
                .getClass().getName());

        // 状態変化の確認
        assertSame(fileName, ReflectionTestUtils.getField(fileLineWriter,
                "fileName"));
        assertSame(clazz, ReflectionTestUtils.getField(fileLineWriter,
                "clazz"));
        assertSame(columnFormatterMap, ReflectionTestUtils.getField(
                fileLineWriter, "columnFormatterMap"));

        // 後処理
        fileLineWriter.closeFile();
        // テスト後ファイルを削除
        File file = new File("aaa");
        file.delete();
    }

}
