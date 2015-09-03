/*
 * $Id: NullColumnFormatterTest.java 5354 2007-10-03 06:06:25Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.test.util.ReflectionTestUtils;

import jp.terasoluna.fw.file.ut.VMOUTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.NullColumnFormatter} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> アノテーションcolumnFormatの記述に従い、文字列の変換処理を行う。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.NullColumnFormatter
 */
public class NullColumnFormatterTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(NullColumnFormatterTest.class);
    }

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VMOUTUtil.initialize();
    }

    /**
     * 終了処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * コンストラクタ。
     * @param name このテストケースの名前。
     */
    public NullColumnFormatterTest(String name) {
        super(name);
    }

    /**
     * testFormat01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:オブジェクト<br>
     * 以下の値を持つ<br>
     * 　値："aaa"<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * <br>
     * 期待値：(戻り値) String:引数のmethodのString型属性に格納されている値の文字列。<br>
     * <br>
     * ファイル行オブジェクトからString型属性に格納されているオブジェクトの文字列を取得することができることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testFormat01() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new NullColumnFormatter();

        // 引数の設定
        NullColumnFormatter_Stub01 stub = new NullColumnFormatter_Stub01();
        ReflectionTestUtils.setField(stub, "string", "aaa");
        Method method = stub.getClass().getDeclaredMethod("getString");
        String columnFormat = new String();

        // 前提条件なし

        // テスト実施
        String testResult = columnFormatter.format(stub, method, columnFormat);

        // 返却値の確認
        assertEquals("aaa", testResult);

        // 状態変化なし
    }

    /**
     * testFormat02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:オブジェクト<br>
     * 以下の値を持つ<br>
     * 　値："aaa"<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：private<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) -:IllegalAccessExceptionが発生することを確認する。<br>
     * <br>
     * ファイル行オブジェクトのString型属性のgetterメソッドにアクセスできない場合、IllegalAccessExceptionをスローすることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testFormat02() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new NullColumnFormatter();

        // 引数の設定
        NullColumnFormatter_Stub02 stub = new NullColumnFormatter_Stub02();
        ReflectionTestUtils.setField(stub, "string", "aaa");
        Method method = stub.getClass().getDeclaredMethod("getString");
        String columnFormat = new String();

        // 前提条件なし

        // テスト実施
        try {
            columnFormatter.format(stub, method, columnFormat);
            fail("IllegalAccessExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalAccessException.class, e.getClass());
        }
    }

    /**
     * testFormat03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:オブジェクト<br>
     * 以下の値を持つ<br>
     * 　値：例外をスローする<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) -:InvocationTargetExceptionが発生することを確認する。<br>
     * <br>
     * ファイル行オブジェクトのString型属性のgetterメソッドが例外をスローする場合、getterメソッドがスローした例外をラップするInvocationTargetExceptionをスローすることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testFormat03() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new NullColumnFormatter();

        // 引数の設定
        NullColumnFormatter_Stub03 stub = new NullColumnFormatter_Stub03();
        ReflectionTestUtils.setField(stub, "string", "aaa");
        Method method = stub.getClass().getMethod("getString");
        String columnFormat = new String();

        // 前提条件なし

        // テスト実施
        try {
            columnFormatter.format(stub, method, columnFormat);
            fail("InvocationTargetExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(InvocationTargetException.class, e.getClass());
        }
    }

    /**
     * testFormat04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:オブジェクト<br>
     * 以下の値を持つ<br>
     * 　値："aaa"<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：あり<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) -:IllegalArgumentExceptionが発生することを確認する。<br>
     * <br>
     * ファイル行オブジェクトのString型属性のgetterメソッドが多数の引数を持つ場合、IllegalArgumentExceptionをスローすることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFormat04() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new NullColumnFormatter();

        // 引数の設定
        NullColumnFormatter_Stub04 stub = new NullColumnFormatter_Stub04();
        ReflectionTestUtils.setField(stub, "string", "aaa");
        Method method = stub.getClass().getDeclaredMethod("getString",
                new Class[] { String.class });
        String columnFormat = new String();

        // 前提条件なし

        // テスト実施
        try {
            columnFormatter.format(stub, method, columnFormat);
            fail("IllegalArgumentExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    /**
     * testFormat05() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:オブジェクト<br>
     * 以下の値を持つ<br>
     * 　値：null<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * <br>
     * 期待値：(戻り値) String:""(空文字)<br>
     * <br>
     * 引数ファイル行オブジェクトのフィールドがNullの場合、空文字が返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testFormat05() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new NullColumnFormatter();

        // 引数の設定
        NullColumnFormatter_Stub01 stub = new NullColumnFormatter_Stub01();
        Method method = stub.getClass().getDeclaredMethod("getString");
        String columnFormat = new String();

        // 前提条件なし

        // テスト実施
        String testResult = columnFormatter.format(stub, method, columnFormat);

        // 返却値の確認
        assertEquals("", testResult);

        // 状態変化なし
    }

    /**
     * testFormat06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:null<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性のgetterメソッド<br>
     * method:Methodインスタンス<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) -:NullPointException発生することを確認する<br>
     * <br>
     * 引数tがnullだった場合は、NullPointerExceptionがスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testFormat06() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new NullColumnFormatter();

        // 引数の設定
        NullColumnFormatter_Stub01 stub = new NullColumnFormatter_Stub01();
        Method method = stub.getClass().getDeclaredMethod("getString");
        String columnFormat = new String();

        // 前提条件なし

        // テスト実施
        try {
            columnFormatter.format(null, method, columnFormat);
            fail("NullPointerExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(NullPointerException.class, e.getClass());
        }
    }

    /**
     * testFormat07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:オブジェクト<br>
     * 以下の値を持つ<br>
     * 　値："aaa"<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性のgetterメソッド<br>
     * method:null<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) -:NullPointException発生することを確認する<br>
     * <br>
     * ファイル行オブジェクト(t)にあるString型属性のgetterメソッドmethodがNullの場合NullPointExceptionをスローすることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testFormat07() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new NullColumnFormatter();

        // 引数の設定
        NullColumnFormatter_Stub01 stub = new NullColumnFormatter_Stub01();
        ReflectionTestUtils.setField(stub, "string", "aaa");
        String columnFormat = new String();

        // 前提条件なし

        // テスト実施
        try {
            columnFormatter.format(stub, null, columnFormat);
            fail("NullPointerExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(NullPointerException.class, e.getClass());
        }
    }

    /**
     * testFormat08() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:オブジェクト<br>
     * 以下の値を持つ<br>
     * 　値："aaa"<br>
     * (引数) ファイル行オブジェクト(t)にあるString型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:null<br>
     * <br>
     * 期待値：(戻り値) String:引数のmethodのString型属性に格納されている値の文字列。<br>
     * <br>
     * フォーマット用の文字列columnFormatがNullの場合に正常実行の結果が設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testFormat08() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new NullColumnFormatter();

        // 引数の設定
        NullColumnFormatter_Stub01 stub = new NullColumnFormatter_Stub01();
        ReflectionTestUtils.setField(stub, "string", "aaa");
        Method method = stub.getClass().getDeclaredMethod("getString");
        String columnFormat = null;

        // 前提条件なし

        // テスト実施
        String testResult = columnFormatter.format(stub, method, columnFormat);

        // 返却値の確認
        assertEquals("aaa", testResult);

        // 状態変化なし
    }
}
