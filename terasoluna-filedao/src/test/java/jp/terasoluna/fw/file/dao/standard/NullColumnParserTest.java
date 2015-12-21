package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.NullColumnParser} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> String型のファイル行オブジェクトの属性にファイルから読み込んだカラムの文字列を格納する。
 * <p>
 * @see jp.terasoluna.fw.file.dao.standard.NullColumnParser
 */
public class NullColumnParserTest {

    /**
     * testParse01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E.F <br>
     * <br>
     * 入力値：(引数) column:Stringインスタンス<br>
     * (引数) ファイル行オブジェクト<br>
     * t:ファイル行オブジェクトスタブ<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性の setterメソッド<br>
     * method:対象となるsetterメソッドの可視性がpublic<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) ファイル行オブジェクト(t)の属性:columnで設定した 文字列が格納される。<br>
     * <br>
     * ファイル行オブジェクトのString型属性に、引数columnの値が設定される ことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse01() throws Exception {
        // テスト対象のインスタンス化
        NullColumnParser nullColumnParser = new NullColumnParser();

        // 引数の設定
        String column = "AA";
        NullColumnParser_Stub01 t = new NullColumnParser_Stub01();
        Method method = NullColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { String.class });
        String columnFormat = "BB";

        // 前提条件の設定
        // なし

        // テスト実施
        nullColumnParser.parse(column, t, method, columnFormat);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result = ReflectionTestUtils.getField(t, "a");
        assertSame(column, result);
    }

    /**
     * testParse02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:Stringインスタンス<br>
     * (引数) ファイル行オブジェクト<br>
     * t:ファイル行オブジェクトスタブ<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性の setterメソッド<br>
     * method:対象となるsetterメソッドの可視性がprivate<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalAccessExceptionが発生する ことを確認する。<br>
     * <br>
     * ファイル行オブジェクトのString型属性のsetterメソッドに アクセスできない場合、IllegalAccessExceptionをスローすることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse02() throws Exception {
        // テスト対象のインスタンス化
        NullColumnParser nullColumnParser = new NullColumnParser();

        // 引数の設定
        String column = "AA";
        NullColumnParser_Stub01 t = new NullColumnParser_Stub01();
        Method method = NullColumnParser_Stub01.class.getDeclaredMethod(
                "setAPrivate", new Class[] { String.class });
        String columnFormat = "BB";

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            nullColumnParser.parse(column, t, method, columnFormat);
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
     * (引数) ファイル行オブジェクト<br>
     * t:ファイル行オブジェクトスタブ<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性の setterメソッド<br>
     * method:setterメソッドが例外をスローする<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) 例外:InvocationTargetExceptionが発生する ことを確認する。<br>
     * <br>
     * ファイル行オブジェクトのString型属性のsetterメソッドが例外を スローする場合、setterメソッドがスローした例外をラップする InvocationTargetExceptionをスローすることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse03() throws Exception {
        // テスト対象のインスタンス化
        NullColumnParser nullColumnParser = new NullColumnParser();

        // 引数の設定
        String column = "AA";
        NullColumnParser_Stub01 t = new NullColumnParser_Stub01();
        Method method = NullColumnParser_Stub01.class.getDeclaredMethod(
                "setAException", new Class[] { String.class });
        String columnFormat = "BB";

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            nullColumnParser.parse(column, t, method, columnFormat);

            // 判定
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
     * (引数) ファイル行オブジェクト<br>
     * t:ファイル行オブジェクトスタブ<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性の setterメソッド<br>
     * method:setterメソッドの引数が多数ある<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentExceptionが発生すること を確認する。<br>
     * <br>
     * ファイル行オブジェクトのString型属性のsetterメソッドの引数が 多数ある場合、IllegalArgumentExceptionをスローすることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse04() throws Exception {
        // テスト対象のインスタンス化
        NullColumnParser nullColumnParser = new NullColumnParser();

        // 引数の設定
        String column = "AA";
        NullColumnParser_Stub01 t = new NullColumnParser_Stub01();
        Method method = NullColumnParser_Stub01.class.getDeclaredMethod(
                "setAAndB", new Class[] { String.class, String.class });
        String columnFormat = "BB";

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            nullColumnParser.parse(column, t, method, columnFormat);

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
     * (正常系) <br>
     * 観点：E.F <br>
     * <br>
     * 入力値：(引数) column:null<br>
     * (引数) ファイル行オブジェクト<br>
     * t:ファイル行オブジェクトスタブ<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性の setterメソッド<br>
     * method:対象となるsetterメソッドの可視性がpublic<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) ファイル行オブジェクト(t)の属性:nullが 設定される。<br>
     * <br>
     * columnがNullの場合に、ファイル行オブジェクトの属性にnullが 設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse05() throws Exception {
        // テスト対象のインスタンス化
        NullColumnParser nullColumnParser = new NullColumnParser();

        // 引数の設定
        String column = null;
        NullColumnParser_Stub01 t = new NullColumnParser_Stub01();
        Method method = NullColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { String.class });
        String columnFormat = null;

        // 前提条件の設定
        // なし

        // テスト実施
        nullColumnParser.parse(column, t, method, columnFormat);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result = ReflectionTestUtils.getField(t, "a");
        assertNull(result);
    }

    /**
     * testParse06() <br>
     * <br>
     * (正常系) <br>
     * 観点：E.F <br>
     * <br>
     * 入力値：(引数) column:Stringインスタンス<br>
     * (引数) ファイル行オブジェクト<br>
     * t:ファイル行オブジェクトスタブ<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性のsetterメソッド<br>
     * method:対象となるsetterメソッドの可視性がpublic<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:null<br>
     * <br>
     * 期待値：(状態変化) ファイル行オブジェクト(t)の属性:columnで設定した文字列が格納される。<br>
     * <br>
     * columnFormatがNullの場合に、ファイル行オブジェクトのString型属性に、引数columnの値が設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse06() throws Exception {
        // テスト対象のインスタンス化
        NullColumnParser nullColumnParser = new NullColumnParser();

        // 引数の設定
        String column = "AA";
        NullColumnParser_Stub01 t = new NullColumnParser_Stub01();
        Method method = NullColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { String.class });
        String columnFormat = "BB";

        // 前提条件の設定
        // なし

        // テスト実施
        nullColumnParser.parse(column, t, method, columnFormat);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result = ReflectionTestUtils.getField(t, "a");
        assertSame(column, result);
    }
}
