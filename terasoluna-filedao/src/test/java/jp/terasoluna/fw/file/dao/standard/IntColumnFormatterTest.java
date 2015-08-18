/*
 * $Id:$
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import jp.terasoluna.fw.file.ut.VMOUTUtil;
import jp.terasoluna.utlib.UTUtil;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.IntColumnFormatter} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> アノテーションcolumnFormatの記述に従い、文字列の変換処理を行う。
 * <p>
 * @author 奥田 哲司
 * @see jp.terasoluna.fw.file.dao.standard.IntColumnFormatter
 */
public class IntColumnFormatterTest {

    /**
     * 初期化処理を行う。
     */
    @Before
    public void setUp() {
        VMOUTUtil.initialize();
    }

    /**
     * testFormat01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:フィールドを1つ持つスタブクラス<br>
     * (引数) ファイル行オブジェクト(t)にあるint型属性の getterメソッド<br>
     * method:対象となるgetterメソッドの可視性がpublic<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:空のStringインスタンス<br>
     * (状態) 文字列を取り出すファイル行オブジェクト(t)のint型の属性: int i=3<br>
     * <br>
     * 期待値：(戻り値) 文字列:引数のmethodのint型属性に 格納されている値の文字列。<br>
     * "3"<br>
     * <br>
     * ファイル行オブジェクトからint型属性に格納されているオブジェクトの 文字列を取得することができることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFormat01() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new IntColumnFormatter();

        // 引数の設定
        IntColumnFormatter_Stub01 t = new IntColumnFormatter_Stub01();
        Method method = t.getClass().getMethod("getIntValue");
        String columnFormat = new String();

        // 前提条件の設定
        t.setIntValue(3);

        // テスト実施
        String testResult = columnFormatter.format(t, method, columnFormat);

        // 返却値の確認
        assertEquals("3", testResult);

        // 状態変化の確認
        // なし
    }

    /**
     * testFormat02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:フィールドを1つ持つスタブクラス<br>
     * (引数) ファイル行オブジェクト(t)にあるint型属性の getterメソッド<br>
     * method:対象となるgetterメソッドの可視性がprivate<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:空のStringインスタンス<br>
     * (状態) 文字列を取り出すファイル行オブジェクト(t)の int型の属性:int i=0<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalAccessExceptionが発生することを 確認する。<br>
     * <br>
     * ファイル行オブジェクトのint型属性のgetterメソッドにアクセスできない場合、 IllegalAccessExceptionをスローすることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFormat02() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new IntColumnFormatter();

        // 引数の設定
        IntColumnFormatter_Stub02 t = new IntColumnFormatter_Stub02();
        Method method = t.getClass().getDeclaredMethod("getIntValue",
                new Class[0]);
        String columnFormat = new String();

        // 前提条件の設定
        UTUtil.setPrivateField(t, "intValue", 0);

        try {
            // テスト実施
            columnFormatter.format(t, method, columnFormat);
            fail("IllegalAccessExceptionがスローされませんでした。");
        } catch (IllegalAccessException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(IllegalAccessException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testFormat03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:フィールドを1つ持つスタブクラス<br>
     * (引数) ファイル行オブジェクト(t)にあるint型属性の getterメソッド<br>
     * method:getterメソッドが例外をスローする<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:空のStringインスタンス<br>
     * (状態) 文字列を取り出すファイル行オブジェクト(t)の int型の属性:int i=0<br>
     * <br>
     * 期待値：(状態変化) 例外:InvocationTargetExceptionが発生することを 確認する。<br>
     * <br>
     * ファイル行オブジェクトのint型属性のgetterメソッドが例外をスローする場合、 getterメソッドがスローした例外をラップする InvocationTargetExceptionをスローすることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFormat03() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new IntColumnFormatter();

        // 引数の設定
        IntColumnFormatter_Stub03 t = new IntColumnFormatter_Stub03();
        Method method = t.getClass().getMethod("getIntValue");
        String columnFormat = new String();

        // 前提条件の設定
        UTUtil.setPrivateField(t, "intValue", 0);

        try {
            // テスト実施
            columnFormatter.format(t, method, columnFormat);
            fail("InvocationTargetExceptionがスローされませんでした。");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(InvocationTargetException.class.getName(), e
                    .getClass().getName());
        }
    }

    /**
     * testFormat04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:フィールドを1つ持つスタブクラス<br>
     * (引数) ファイル行オブジェクト(t)にある int型属性のgetterメソッド<br>
     * method:getterの引数が多数ある<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:空のStringインスタンス<br>
     * (状態) 文字列を取り出すファイル行オブジェクト(t)の int型の属性:int i=0<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentExceptionが発生することを 確認する。<br>
     * <br>
     * ファイル行オブジェクトのint型属性のgetterメソッドが多数の引数を持つ場合、 IllegalArgumentExceptionをスローすることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFormat04() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new IntColumnFormatter();

        // 引数の設定
        IntColumnFormatter_Stub04 t = new IntColumnFormatter_Stub04();
        Method method = t.getClass().getMethod("getIntValue",
                new Class[] { int.class, String.class });
        String columnFormat = new String();

        // 前提条件の設定
        UTUtil.setPrivateField(t, "intValue", 0);

        try {
            // テスト実施
            columnFormatter.format(t, method, columnFormat);
            fail("IllegalArgumentExceptionがスローされませんでした。");
        } catch (IllegalArgumentException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }
}
