/*
 * $Id:$
 *
 * Copyright (c) 2006-2015 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import org.springframework.test.util.ReflectionTestUtils;

import jp.terasoluna.fw.file.dao.FileLineWriter;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.PlainFileUpdateDAO} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイル行オブジェクトを用いないファイル出力用のFileLineWriterを生成する。<br>
 * AbstractFileUpdateDAOのサブクラス。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.PlainFileUpdateDAO
 */
public class PlainFileUpdateDAOTest {

    /**
     * testExecute01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:PlainFileUpdateDAO01.txt<br>
     * データを持たないファイルのパス<br>
     * (引数) clazz:PlainFileUpdateDAO_Stub01<br>
     * 空実装<br>
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
    @Test
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
        ReflectionTestUtils.setField(fileUpdateDAO, "columnFormatterMap",
                columnFormatterMap);

        // テスト実施
        FileLineWriter<PlainFileUpdateDAO_Stub01> fileLineWriter = fileUpdateDAO
                .execute(fileName, clazz);

        // 返却値の確認
        assertEquals(PlainFileLineWriter.class, fileLineWriter.getClass());

        assertSame(fileName, ReflectionTestUtils.getField(fileLineWriter,
                "fileName"));
        assertSame(clazz, ReflectionTestUtils.getField(fileLineWriter,
                "clazz"));
        assertSame(columnFormatterMap, ReflectionTestUtils.getField(
                fileLineWriter, "columnFormatterMap"));
    }
}
