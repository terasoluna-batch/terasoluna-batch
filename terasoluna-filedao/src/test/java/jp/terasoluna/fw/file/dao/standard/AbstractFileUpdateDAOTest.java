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
import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.AbstractFileUpdateDAO} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイルのデータ取得用のFileLineWriterを生成する。<br>
 * （テスト実行のため、スタブを用意する）
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.AbstractFileUpdateDAO
 */
public class AbstractFileUpdateDAOTest {

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
    @Test
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
        Object result = ReflectionTestUtils.getField(abstractFileUpdateDAO,
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
    @Test
    public void testGetColumnFormatterMap01() throws Exception {
        // テスト対象のインスタンス化
        AbstractFileUpdateDAO abstractFileUpdateDAO = new AbstractFileUpdateDAO_Stub01();

        // 引数の設定
        // なし

        // 前提条件の設定
        Map<String, ColumnFormatter> textGetterMap = new HashMap<String, ColumnFormatter>();
        ReflectionTestUtils.setField(abstractFileUpdateDAO, "columnFormatterMap",
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
