package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;

import org.junit.Test;

import jp.terasoluna.fw.file.dao.FileLineWriter;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.Mockito;

import static org.junit.Assert.assertSame;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.CSVFileUpdateDAO} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> CSVファイル用のFileLineWriterを生成する。<br>
 * AbstractFileUpdateDAOのサブクラス。
 * <p>
 * @see jp.terasoluna.fw.file.dao.standard.CSVFileUpdateDAO
 */
public class CSVFileUpdateDAOTest {

    /**
     * testExecute01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E.F <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * (引数) clazz:Class<T>インスタンス<br>
     * (状態) AbstractFileUpdateDAO.columnFormatterMap: HashMapインスタンス<br>
     * <br>
     * 期待値：(戻り値) fileLineWriter:CSVFileLineWriter<T>インスタンス<br>
     * (状態変化) CSVFileLineWriter#CSVFileLineWriter():1回呼ばれる。<br>
     * 呼ばれるときの引数を確認する。<br>
     * (状態変化) AbstractFileUpdateDAO#getColumnFormatterMap(): 1回呼ばれる<br>
     * <br>
     * 正常パターン<br>
     * 引数がそれぞれnot nullであれば、戻り値が帰ってくることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExecute01() throws Exception {
        // テスト対象のインスタンス化
        CSVFileUpdateDAO fileUpdateDAO = Mockito.spy(new CSVFileUpdateDAO());

        // 引数の設定
        String fileName = "aaa";
        Class<CSVFileUpdateDAO_Stub01> clazz = CSVFileUpdateDAO_Stub01.class;

        // 前提条件の設定
        HashMap<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        ReflectionTestUtils.setField(fileUpdateDAO, "columnFormatterMap",
                columnFormatterMap);

        // テスト実施
        FileLineWriter<CSVFileUpdateDAO_Stub01> fileLineWriter = fileUpdateDAO
                .execute(fileName, clazz);

        // 返却値の確認
        assertEquals(CSVFileLineWriter.class, fileLineWriter.getClass());

        // 状態変化の確認
        Mockito.verify(fileUpdateDAO).getColumnFormatterMap();
        assertSame(fileName, ReflectionTestUtils.getField(fileLineWriter,
                "fileName"));
        assertSame(clazz, ReflectionTestUtils.getField(fileLineWriter,
                "clazz"));
        assertSame(columnFormatterMap, ReflectionTestUtils.getField(
                fileLineWriter, "columnFormatterMap"));

        // クローズ処理
        fileLineWriter.closeFile();
        // テスト後ファイルを削除
        File file = new File("aaa");
        file.delete();
    }

}
