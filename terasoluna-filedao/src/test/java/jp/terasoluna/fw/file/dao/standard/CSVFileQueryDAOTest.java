/*
 * $Id: CSVFileQueryDAOTest.java 5576 2007-11-15 13:13:32Z pakucn $
 * 
 * Copyright (c) 2006 NTT DATA Corporation
 * 
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import jp.terasoluna.fw.file.dao.FileLineIterator;
import jp.terasoluna.fw.file.ut.VMOUTUtil;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.CSVFileQueryDAO} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> CSVファイル読取用のFileLineIterator生成クラス。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.CSVFileQueryDAO
 */
public class CSVFileQueryDAOTest {

    /**
     * 初期化処理を行う。
     */
    @Before
    public void setUp() {
        VMOUTUtil.initialize();
    }

    /**
     * testExecute01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E.F <br>
     * <br>
     * 入力値：(引数) fileName:CSVFleQueryDAO01.txt<br>
     * データを持たないファイルのパス<br>
     * (引数) clazz:FileFormatアノテーションを持つスタブを使用<br>
     * CSVFileQueryDAO_Stub01<br>
     * (状態) FileQueryDAO.columnParserMap:以下の設定を持つHashMapのインスタンス<br>
     * 要素1<br>
     * key:"java.lang.String"<br>
     * value:ColumnParserインスタンス<br>
     * CSVFileLineIterator_ColumnParserStub01インスタンス<br>
     * 空実装<br>
     * <br>
     * 期待値：(戻り値) FileLineIterator<T>:CSVFileLineIterator<T>のインスタンス<br>
     * (状態変化) CSVFileLineIterator:1回呼ばれる。<br>
     * 引数1：引数fileName<br>
     * 引数2：引数clazz<br>
     * 引数3：FileQueryDAO.columnParserMap<br>
     * <br>
     * 正常パターン <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testExecute01() throws Exception {
        // テスト対象のインスタンス化
        CSVFileQueryDAO fileQueryDAO = new CSVFileQueryDAO();

        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileQueryDAO_Stub01> clazz = CSVFileQueryDAO_Stub01.class;

        // 前提条件の設定
        Map<String, ColumnParser> columnParser = new HashMap<String, ColumnParser>();
        ColumnParser parser = new CSVFileQueryDAO_ColumnParserStub01();
        columnParser.put("java.lang.String", parser);
        ReflectionTestUtils.setField(fileQueryDAO, "columnParserMap",
                columnParser);

        // テスト実施
        FileLineIterator fileLineIterator = fileQueryDAO.execute(fileName,
                clazz);

        // 返却値の確認
        assertEquals(CSVFileLineIterator.class, fileLineIterator.getClass());

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(CSVFileLineIterator.class,
                "<init>"));
        List arguments = VMOUTUtil.getArguments(CSVFileLineIterator.class,
                "<init>", 0);
        assertEquals(fileName, arguments.get(0));
        assertEquals(clazz, arguments.get(1));
        assertSame(columnParser, arguments.get(2));
    }
}
