/*
 * $Id:$
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import jp.terasoluna.utlib.UTUtil;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.AbstractFileQueryDAO} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイルのデータ取得用のFileLineIteratorを生成する。<br>
 * （テスト実行のため、スタブlを用意する）
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.AbstractFileQueryDAO
 */
public class AbstractFileQueryDAOTest {

    /**
     * testSetColumnParserMap01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) columnParserMap:Map<String, ColumnParser>インスタンス<br>
     * <br>
     * 期待値：(状態変化) this.columnParserMap:引数で指定した Map<String, ColumnParser>インスタンス<br>
     * <br>
     * columnParserMapのsetterメソッドの値が正しく設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetColumnParserMap01() throws Exception {
        // テスト対象のインスタンス化
        AbstractFileQueryDAO abstractFileQueryDAO = new AbstractFileQueryDAO_Stub01();

        // 引数の設定
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();

        // 前提条件の設定
        // なし

        // テスト実施
        abstractFileQueryDAO.setColumnParserMap(columnParserMap);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result = UTUtil.getPrivateField(abstractFileQueryDAO,
                "columnParserMap");
        assertSame(columnParserMap, result);
    }

    /**
     * testGetColumnParserMap01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) this.columnParserMap:Map<String, ColumnParser>インスタンス<br>
     * <br>
     * 期待値：(戻り値) columnParserMap:Map<String, ColumnParser>インスタンス<br>
     * <br>
     * columnParserMapのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetColumnParserMap01() throws Exception {
        // テスト対象のインスタンス化
        AbstractFileQueryDAO abstractFileQueryDAO = new AbstractFileQueryDAO_Stub01();

        // 引数の設定
        // なし

        // 前提条件の設定
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        UTUtil.setPrivateField(abstractFileQueryDAO, "columnParserMap",
                columnParserMap);

        // テスト実施
        Map<String, ColumnParser> result = abstractFileQueryDAO
                .getColumnParserMap();

        // 返却値の確認
        assertSame(columnParserMap, result);

        // 状態変化の確認
        // なし
    }

}
