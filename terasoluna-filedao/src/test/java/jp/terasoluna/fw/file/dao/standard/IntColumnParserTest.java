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

import org.junit.Test;

import jp.terasoluna.utlib.UTUtil;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.IntColumnParser} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> 指定された文字列をパーズし、int型に変換する。<br>
 * 変換結果をファイル行オブジェクトのint型の属性に値を格納する。
 * <p>
 * @author 奥田 哲司
 * @see jp.terasoluna.fw.file.dao.standard.IntColumnParser
 */
public class IntColumnParserTest {

    /**
     * testParse01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) column:Stringインスタンス<br>
     * "1"<br>
     * (引数) ファイル行オブジェクト<br>
     * t:ファイル行オブジェクトスタブ<br>
     * (引数) ファイル行オブジェクト(t)にあるint型属性の setterメソッド<br>
     * method:対象となるsetterメソッドの可視性がpublic<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) ファイル行オブジェクト(t)の属性:columnで設定した 文字列がintに変換されて格納される。<br>
     * "1"<br>
     * <br>
     * ファイル行オブジェクトのint型属性に文字列を設定することができることを 確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse01() throws Exception {
        // テスト対象のインスタンス化
        IntColumnParser intColumnParser = new IntColumnParser();

        // 引数の設定
        String column = "1";
        IntColumnParser_Stub01 t = new IntColumnParser_Stub01();
        Method method = IntColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Integer.TYPE });
        String columnFormat = "";

        // 前提条件の設定
        // なし

        // テスト実施
        intColumnParser.parse(column, t, method, columnFormat);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result = UTUtil.getPrivateField(t, "a");
        assertEquals(1, result);
    }

    /**
     * testParse02() <br>
     * <br>
     * (正常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:Stringインスタンス<br>
     * "1"<br>
     * (引数) ファイル行オブジェクト<br>
     * t:ファイル行オブジェクトスタブ<br>
     * (引数) ファイル行オブジェクト(t)にあるint型属性の setterメソッド<br>
     * method:対象となるsetterメソッドの可視性がprivate<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalAccessExceptionが発生することを 確認する。<br>
     * <br>
     * ファイル行オブジェクトのint型属性のsetterメソッドにアクセスできない場合、 IllegalAccessExceptionをスローすることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse02() throws Exception {
        // テスト対象のインスタンス化
        IntColumnParser intColumnParser = new IntColumnParser();

        // 引数の設定
        String column = "1";
        IntColumnParser_Stub01 t = new IntColumnParser_Stub01();
        Method method = IntColumnParser_Stub01.class.getDeclaredMethod(
                "setAPrivate", new Class[] { Integer.TYPE });
        String columnFormat = "";

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            intColumnParser.parse(column, t, method, columnFormat);
            fail("IllegalAccessExceptionがスローされませんでした。");
        } catch (IllegalAccessException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(IllegalAccessException.class, e.getClass());
        }
    }

    /**
     * testParse03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:Stringインスタンス<br>
     * "1"<br>
     * (引数) ファイル行オブジェクト<br>
     * t:ファイル行オブジェクトスタブ<br>
     * (引数) ファイル行オブジェクト(t)にあるint型属性の setterメソッド<br>
     * method:setterメソッドが例外をスローする<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) 例外:InvocationTargetExceptionが発生することを 確認する。<br>
     * <br>
     * ファイル行オブジェクトのint型属性のsetterメソッドが例外をスローする場合、 setterメソッドがスローした例外をラップするInvocationTargetExceptionを スローすることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse03() throws Exception {
        // テスト対象のインスタンス化
        IntColumnParser intColumnParser = new IntColumnParser();

        // 引数の設定
        String column = "1";
        IntColumnParser_Stub01 t = new IntColumnParser_Stub01();
        Method method = IntColumnParser_Stub01.class.getDeclaredMethod(
                "setAException", new Class[] { Integer.TYPE });
        String columnFormat = "";

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            intColumnParser.parse(column, t, method, columnFormat);
            fail();
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(InvocationTargetException.class, e.getClass());
        }
    }

    /**
     * testParse04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:Stringインスタンス<br>
     * "1"<br>
     * (引数) ファイル行オブジェクト<br>
     * t:ファイル行オブジェクトスタブ<br>
     * (引数) ファイル行オブジェクト(t)にあるint型属性の setterメソッド<br>
     * method:setterメソッドの引数が多数ある<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentExceptionが発生することを 確認する。<br>
     * <br>
     * ファイル行オブジェクトのint型属性のsetterメソッドの引数が多数ある場合、 IllegalArgumentExceptionをスローすることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse04() throws Exception {
        // テスト対象のインスタンス化
        IntColumnParser intColumnParser = new IntColumnParser();

        // 引数の設定
        String column = "1";
        IntColumnParser_Stub01 t = new IntColumnParser_Stub01();
        Method method = IntColumnParser_Stub01.class.getDeclaredMethod(
                "setAAndB", new Class[] { Integer.TYPE, Integer.TYPE });
        String columnFormat = "";

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            intColumnParser.parse(column, t, method, columnFormat);

            // 判定
            fail();
        } catch (IllegalArgumentException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    /**
     * testParse05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:整数以外の文字列<br>
     * (引数) ファイル行オブジェクト<br>
     * t:ファイル行オブジェクトスタブ<br>
     * (引数) ファイル行オブジェクト(t)にあるint型属性の setterメソッド<br>
     * method:対象となるsetterメソッドの可視性がpublic<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) 例外:NumberFormatExceptionが発生することを確認する。<br>
     * <br>
     * 引数の文字列が整数以外(int型に変換できない)の場合、 NumberFormatExceptionをスローすることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse05() throws Exception {
        // テスト対象のインスタンス化
        IntColumnParser intColumnParser = new IntColumnParser();

        // 引数の設定
        String column = "a";
        IntColumnParser_Stub01 t = new IntColumnParser_Stub01();
        Method method = IntColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Integer.TYPE });
        String columnFormat = "";

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            intColumnParser.parse(column, t, method, columnFormat);

            // 判定
            fail();
        } catch (NumberFormatException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(NumberFormatException.class, e.getClass());
        }
    }

    /**
     * testParse06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:null<br>
     * (引数) ファイル行オブジェクト<br>
     * t:ファイル行オブジェクトスタブ<br>
     * (引数) ファイル行オブジェクト(t)にあるint型属性のsetterメソッド<br>
     * method:対象となるsetterメソッドの可視性がpublic<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) 例外:NumberFormatExceptionが発生することを確認する。<br>
     * <br>
     * 引数の文字列がnullの場合、NumberFormatExceptionをスローすることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse06() throws Exception {
        // テスト対象のインスタンス化
        IntColumnParser intColumnParser = new IntColumnParser();

        // 引数の設定
        String column = null;
        IntColumnParser_Stub01 t = new IntColumnParser_Stub01();
        Method method = IntColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Integer.TYPE });
        String columnFormat = "";

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            intColumnParser.parse(column, t, method, columnFormat);

            // 判定
            fail();
        } catch (NumberFormatException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(NumberFormatException.class, e.getClass());
        }
    }

    /**
     * testParse07() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) column:Stringインスタンス<br>
     * "1"<br>
     * (引数) ファイル行オブジェクト<br>
     * t:ファイル行オブジェクトスタブ<br>
     * (引数) ファイル行オブジェクト(t)にあるint型属性のsetterメソッド<br>
     * method:対象となるsetterメソッドの可視性がpublic<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:null<br>
     * <br>
     * 期待値：(状態変化) ファイル行オブジェクト(t)の属性:columnで設定した文字列がintに変換されて格納される。<br>
     * "1"<br>
     * <br>
     * 引数のフォーマット用文字列がnullの場合、ファイル行オブジェクトのint型属性に文字列を設定することができることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse07() throws Exception {
        // テスト対象のインスタンス化
        IntColumnParser intColumnParser = new IntColumnParser();

        // 引数の設定
        String column = "1";
        IntColumnParser_Stub01 t = new IntColumnParser_Stub01();
        Method method = IntColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Integer.TYPE });
        String columnFormat = null;

        // 前提条件の設定
        // なし

        // テスト実施
        intColumnParser.parse(column, t, method, columnFormat);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result = UTUtil.getPrivateField(t, "a");
        assertEquals(1, result);
    }
}
