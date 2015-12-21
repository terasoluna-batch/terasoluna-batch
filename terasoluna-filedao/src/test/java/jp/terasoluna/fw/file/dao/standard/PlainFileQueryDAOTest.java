package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import jp.terasoluna.fw.file.dao.FileLineIterator;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.PlainFileQueryDAO} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイル行オブジェクトを使用しないのデータ取得用のFileLineIterator生成クラス。
 * <p>
 * @see jp.terasoluna.fw.file.dao.standard.PlainFileQueryDAO
 */
public class PlainFileQueryDAOTest {

    /**
     * testExcecute01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:PlainFleQueryDAO01.txt<br>
     * データを持たないファイルのパス<br>
     * (引数) clazz:PlainFileQueryDAO_Stub01<br>
     * 空実装<br>
     * (状態) AbstractFileQueryDAO.columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     * ・"java.lang.String"=NullColumnParserインスタンス<br>
     * <br>
     * 期待値：(戻り値) FileLineIterator:PlainFileLineIteratorのインスタンス<br>
     * (状態変化) PlainFileLineIterator#PlainFileLineIterator():1回呼ばれること。<br>
     * 引数が渡されることを確認する。<br>
     * <br>
     * PlainFileLineIteratorインスタンスが生成されることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExcecute01() throws Exception {
        // テスト対象のインスタンス化
        PlainFileQueryDAO plainFileQueryDAO = new PlainFileQueryDAO();

        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<PlainFileQueryDAO_Stub01> clazz = PlainFileQueryDAO_Stub01.class;

        // 前提条件の設定
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        ReflectionTestUtils.setField(plainFileQueryDAO, "columnParserMap",
                columnParserMap);

        // テスト実施
        FileLineIterator<PlainFileQueryDAO_Stub01> result = plainFileQueryDAO
                .execute(fileName, clazz);

        // 返却値の確認
        assertEquals(PlainFileLineIterator.class, result.getClass());

        // 状態変化の確認
        assertSame(fileName, ReflectionTestUtils.getField(result, "fileName"));
        assertSame(clazz, ReflectionTestUtils.getField(result, "clazz"));
        assertSame(columnParserMap, ReflectionTestUtils.getField(result,
                "columnParserMap"));
    }

}
