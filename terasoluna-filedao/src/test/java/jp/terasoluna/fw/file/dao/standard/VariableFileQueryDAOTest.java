/*
 * $Id:$
 *
 * Copyright (c) 2006-2015 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jp.terasoluna.fw.file.dao.FileLineIterator;
import jp.terasoluna.utlib.UTUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.VariableFileQueryDAO} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> 可変長ファイル読取用のFileLineIterator生成クラス。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.VariableFileQueryDAO
 */
public class VariableFileQueryDAOTest {

    /**
     * testExcecute01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:VariableFileQueryDAO_execute01.txt<br>
     * 　データを持たないファイルのパス<br>
     * (引数) clazz:VariableFileQueryDAO_Stub01<br>
     * 　空実装<br>
     * (状態) AbstractFileQueryDAO.columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     * ・"java.lang.String"=NullColumnParser.java<br>
     * <br>
     * 期待値：(戻り値) FileLineIterator:VariableFileLineIteratorのインスタンス<br>
     * (状態変化) VariableFileLineIterator:コンストラクタが1回呼ばれること。<br>
     * パラメータに引数が渡されること。<br>
     * <br>
     * 引数がnullではない場合、VariableFileLineIteratorのインスタンスが取得できること。<br>
     * このメソッドは、VariableFileLineWriterのコンストラクタを呼び出すだけなので、引数のバリエーションは一つしか行わない。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testExcecute01() throws Exception {
        // テスト対象のインスタンス化
        VariableFileQueryDAO fileQueryDAO = new VariableFileQueryDAO();

        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileQueryDAO_Stub01> clazz = VariableFileQueryDAO_Stub01.class;

        // 前提条件の設定
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        UTUtil
                .setPrivateField(fileQueryDAO, "columnParserMap",
                        columnParserMap);

        // テスト実施
        FileLineIterator fileLineIterator = fileQueryDAO.execute(fileName,
                VariableFileQueryDAO_Stub01.class);

        // 返却値の確認
        assertEquals(VariableFileLineIterator.class, fileLineIterator
                .getClass());

        // 状態変化の確認
        assertEquals(fileName, UTUtil.getPrivateField(fileLineIterator, "fileName"));
        assertSame(clazz, UTUtil.getPrivateField(fileLineIterator, "clazz"));
        assertSame(columnParserMap, UTUtil.getPrivateField(fileLineIterator, "columnParserMap"));
    }

}
