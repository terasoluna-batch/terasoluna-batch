/*
 * $Id: DecimalColumnParserTest.java 5354 2007-10-03 06:06:25Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;

import jp.terasoluna.fw.file.ut.VMOUTUtil;
import jp.terasoluna.utlib.UTUtil;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.DecimalColumnParser} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> 指定された文字列をパーズし、BigDecimal型に変換する。<br>
 * 変換結果をファイル行オブジェクトのBigDecimal型の属性に値を格納する。
 * <p>
 * @author 奥田 哲司
 * @see jp.terasoluna.fw.file.dao.standard.DecimalColumnParser
 */
public class DecimalColumnParserTest {

    /**
     * 初期化処理を行う。
     */
    @Before
    public void setUp() {
        VMOUTUtil.initialize();
    }

    /**
     * testParse01() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,E <br>
     * <br>
     * 入力値：(引数) column:"123456"<br>
     * (引数) t:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal01<br>
     * - 型：BigDecimal<br>
     * - 初期値：null<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※setterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するsetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：1つ(BigDecimal型)<br>
     * ・処理：引数を対象フィールドに格納する。<br>
     * (引数) columnFormat:null<br>
     * <br>
     * 期待値：(状態変化) t:対象フィールドにBigDecimal.valueOf(123456)値が格納される。<br>
     * <br>
     * 正常ケース<br>
     * フォーマット用の文字列がnullの場合、 かつフィールドのsetterメソッドが正しく設定されている場合に、 引数の文字列が正しくBigDecimalに変換されて格納されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse01() throws Exception {
        // 前処理
        DecimalColumnParser decimalColumnParser = new DecimalColumnParser();

        // 前処理(引数)
        String column = "123456";
        DecimalColumnParser_FileLineObjectStub01 stub = new DecimalColumnParser_FileLineObjectStub01();

        stub.setDecimal01(null);

        Method method = DecimalColumnParser_FileLineObjectStub01.class
                .getDeclaredMethod("setDecimal01",
                        new Class[] { BigDecimal.class });
        String columnFormat = null;

        // テスト実施
        decimalColumnParser.parse(column, stub, method, columnFormat);

        // 判定
        BigDecimal result = stub.getDecimal01();
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(123456), result);
    }

    /**
     * testParse02() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) column:"-\\123,456.00"<br>
     * (引数) t:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal02<br>
     * - 型：BigDecimal<br>
     * - 初期値：null<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※setterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するsetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：1つ(BigDecimal型)<br>
     * ・処理：引数を対象フィールドに格納する。<br>
     * (引数) columnFormat:"-\\###,###,###.##"<br>
     * <br>
     * 期待値：(状態変化) t:対象フィールドにBigDecimal.valueOf(12345600, 2)値が格納される。<br>
     * <br>
     * 正常ケース<br>
     * フォーマット用の文字列がある場合、 かつフィールドのsetterメソッドが正しく設定されている場合に、 引数の文字列が正しくフォーマットに従ってBigDecimalに変換されて格納されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse02() throws Exception {
        // 前処理
        DecimalColumnParser decimalColumnParser = new DecimalColumnParser();

        // 前処理(引数)
        String column = "-\\123,456.00";
        DecimalColumnParser_FileLineObjectStub01 stub = new DecimalColumnParser_FileLineObjectStub01();

        stub.setDecimal02(null);

        Method method = DecimalColumnParser_FileLineObjectStub01.class
                .getDeclaredMethod("setDecimal02",
                        new Class[] { BigDecimal.class });
        String columnFormat = "-\\###,###,###.##";

        // テスト実施
        decimalColumnParser.parse(column, stub, method, columnFormat);

        // 判定
        BigDecimal result = stub.getDecimal02();
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(12345600, 2), result);
    }

    /**
     * testParse03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:"-\\123,456.00"<br>
     * (引数) t:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal03<br>
     * - 型：BigDecimal<br>
     * - 初期値：null<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※setterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するsetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：1つ(BigDecimal型)<br>
     * ・処理：引数を対象フィールドに格納する。<br>
     * (引数) columnFormat:"###,###,###.##"<br>
     * <br>
     * ※引数がフォーマットできない文字列<br>
     * <br>
     * 期待値：(状態変化) -:ParseExceptionが発生することを確認する。<br>
     * <br>
     * 異常ケース<br>
     * フォーマット文字列でパーシングできないデータが渡された場合、 ParseExceptionが発生することを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse03() throws Exception {
        // 前処理
        DecimalColumnParser decimalColumnParser = new DecimalColumnParser();

        // 前処理(引数)
        String column = "-\\123,456.00";
        DecimalColumnParser_FileLineObjectStub01 stub = new DecimalColumnParser_FileLineObjectStub01();

        stub.setDecimal03(null);

        Method method = DecimalColumnParser_FileLineObjectStub01.class
                .getDeclaredMethod("setDecimal03",
                        new Class[] { BigDecimal.class });
        String columnFormat = "###,###,###.##";

        try {
            // テスト実施
            decimalColumnParser.parse(column, stub, method, columnFormat);
            fail("ParseExceptionが発生しませんでした。");
        } catch (ParseException e) {
            // 判定
            assertTrue(e instanceof ParseException);
        }
    }

    /**
     * testParse04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:"-\\123,456.00"<br>
     * (引数) t:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal04<br>
     * - 型：BigDecimal<br>
     * - 初期値：null<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※setterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するsetterメソッドのインスタンス<br>
     * ・可視性：private<br>
     * ・引数：1つ(BigDecimal型)<br>
     * ・処理：引数を対象フィールドに格納する。<br>
     * (引数) columnFormat:"-\\###,###,###.##"<br>
     * <br>
     * 期待値：(状態変化) -:IllegalAccessExceptionが発生することを確認する。<br>
     * <br>
     * 異常ケース<br>
     * フィールドのsetterメソッドがprivateで宣言された場合、 IllegalAccessExceptionが発生することを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse04() throws Exception {
        // 前処理
        DecimalColumnParser decimalColumnParser = new DecimalColumnParser();

        // 前処理(引数)
        String column = "-\\123,456.00";
        DecimalColumnParser_FileLineObjectStub01 stub = new DecimalColumnParser_FileLineObjectStub01();

        UTUtil.setPrivateField(stub, "decimal04", null);

        Method method = DecimalColumnParser_FileLineObjectStub01.class
                .getDeclaredMethod("setDecimal04",
                        new Class[] { BigDecimal.class });
        String columnFormat = "\\###,###,###.##";

        try {
            // テスト実施
            decimalColumnParser.parse(column, stub, method, columnFormat);
            fail("IllegalAccessExceptionが発生しませんでした。");
        } catch (IllegalAccessException e) {
            // 判定
            assertTrue(e instanceof IllegalAccessException);
        }
    }

    /**
     * testParse05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:"-\\123,456.00"<br>
     * (引数) t:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal05<br>
     * - 型：BigDecimal<br>
     * - 初期値：null<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※setterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するsetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：1つ(BigDecimal型)<br>
     * ・処理：例外が発生する。<br>
     * (引数) columnFormat:"-\\###,###,###.##"<br>
     * <br>
     * 期待値：(状態変化) -:InvocationTargetExceptionが発生することを確認する。<br>
     * <br>
     * 異常ケース<br>
     * フィールドのsetterメソッドの処理で例外が発生した場合、 IvocationTargetExceptionが発生することを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse05() throws Exception {
        // 前処理
        DecimalColumnParser decimalColumnParser = new DecimalColumnParser();

        // 前処理(引数)
        String column = "-\\123,456.00";
        DecimalColumnParser_FileLineObjectStub01 stub = new DecimalColumnParser_FileLineObjectStub01();

        UTUtil.setPrivateField(stub, "decimal05", null);

        Method method = DecimalColumnParser_FileLineObjectStub01.class
                .getDeclaredMethod("setDecimal05",
                        new Class[] { BigDecimal.class });
        String columnFormat = "-\\###,###,###.##";

        try {
            // テスト実施
            decimalColumnParser.parse(column, stub, method, columnFormat);
            fail("InvocationTargetExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定
            assertTrue(e instanceof InvocationTargetException);
        }
    }

    /**
     * testParse06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:"-\\123,456.00"<br>
     * (引数) t:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal06<br>
     * - 型：BigDecimal<br>
     * - 初期値：null<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※setterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するsetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：２つ(BigDecimal型２つ)<br>
     * ・処理：一番目の引数を対象フィールドに格納する。<br>
     * (引数) columnFormat:"-\\###,###,###.##"<br>
     * <br>
     * 期待値：(状態変化) -:IllegalArgumentExceptionが発生することを確認する。<br>
     * <br>
     * 異常ケース<br>
     * フィールドのsetterメソッドが引数でBigDecimal型１つ以外を持つ場合、 llegalArgumentExceptionが発生することを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse06() throws Exception {
        // 前処理
        DecimalColumnParser decimalColumnParser = new DecimalColumnParser();

        // 前処理(引数)
        String column = "-\\123,456.00";
        DecimalColumnParser_FileLineObjectStub01 stub = new DecimalColumnParser_FileLineObjectStub01();

        UTUtil.setPrivateField(stub, "decimal06", null);

        Method method = DecimalColumnParser_FileLineObjectStub01.class
                .getDeclaredMethod("setDecimal06", new Class[] {
                        BigDecimal.class, BigDecimal.class });

        String columnFormat = "-\\###,###,###.##";

        try {
            // テスト実施
            decimalColumnParser.parse(column, stub, method, columnFormat);
            fail("IllegalArgumentExceptionが発生しませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    /**
     * testParse07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:null<br>
     * (引数) t:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal06<br>
     * - 型：BigDecimal<br>
     * - 初期値：null<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※setterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するsetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：1つ(BigDecimal型)<br>
     * ・処理：引数を対象フィールドに格納する。<br>
     * (引数) columnFormat:"-\\###,###,###.##"<br>
     * <br>
     * 期待値：(状態変化) -:NullPointerExceptionが発生することを確認する。<br>
     * <br>
     * 異常ケース<br>
     * 引数がnullの場合、NullPointerExceptionが発生することを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse07() throws Exception {
        // 前処理
        DecimalColumnParser decimalColumnParser = new DecimalColumnParser();

        // 前処理(引数)
        String column = null;
        DecimalColumnParser_FileLineObjectStub01 stub = new DecimalColumnParser_FileLineObjectStub01();

        UTUtil.setPrivateField(stub, "decimal07", null);

        Method method = DecimalColumnParser_FileLineObjectStub01.class
                .getDeclaredMethod("setDecimal07",
                        new Class[] { BigDecimal.class });
        String columnFormat = "-\\###,###,###.##";

        try {
            // テスト実施
            decimalColumnParser.parse(column, stub, method, columnFormat);
            fail("NullPointerExceptionが発生しませんでした。");
        } catch (NullPointerException e) {
            // 判定
            assertTrue(e instanceof NullPointerException);
        }
    }

    /**
     * testParse08() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:"abcあいうアイウ愛有"<br>
     * (引数) t:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal06<br>
     * - 型：BigDecimal<br>
     * - 初期値：null<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※setterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するsetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：1つ(BigDecimal型)<br>
     * ・処理：引数を対象フィールドに格納する。<br>
     * (引数) columnFormat:"-\\###,###,###.##"<br>
     * <br>
     * 期待値：(状態変化) -:ParseExceptionが発生することを確認する。<br>
     * <br>
     * 異常ケース<br>
     * 引数が数字ではない場合、ParseExceptionが発生することを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse08() throws Exception {
        // 前処理
        DecimalColumnParser decimalColumnParser = new DecimalColumnParser();

        // 前処理(引数)
        String column = "abcあいうアイウ愛有";
        DecimalColumnParser_FileLineObjectStub01 stub = new DecimalColumnParser_FileLineObjectStub01();

        UTUtil.setPrivateField(stub, "decimal08", null);

        Method method = DecimalColumnParser_FileLineObjectStub01.class
                .getDeclaredMethod("setDecimal08",
                        new Class[] { BigDecimal.class });
        String columnFormat = "-\\###,###,###.##";

        try {
            // テスト実施
            decimalColumnParser.parse(column, stub, method, columnFormat);
            fail("ParseExceptionが発生しませんでした。");
        } catch (ParseException e) {
            // 判定
            assertTrue(e instanceof ParseException);
            String errorMessage = "Unparseable number: \"abcあいうアイウ愛有\"";
            assertEquals(errorMessage, e.getMessage());
        }
    }

    /**
     * testParse09() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,E <br>
     * <br>
     * 入力値：(引数) column:"123456"<br>
     * (引数) t:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal09<br>
     * - 型：BigDecimal<br>
     * - 初期値：null<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※setterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するsetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：1つ(BigDecimal型)<br>
     * ・処理：引数を対象フィールドに格納する。<br>
     * (引数) columnFormat:""<br>
     * <br>
     * 期待値：(状態変化) t:対象フィールドにBigDecimal.valueOf(123456)値が格納される。<br>
     * <br>
     * 正常ケース<br>
     * フォーマット用の文字列が空文字の場合、 かつフィールドのsetterメソッドが正しく設定されている場合に、 引数の文字列が正しくBigDecimalに変換されて格納されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse09() throws Exception {
        // 前処理
        DecimalColumnParser decimalColumnParser = new DecimalColumnParser();

        // 前処理(引数)
        String column = "123456";
        DecimalColumnParser_FileLineObjectStub01 stub = new DecimalColumnParser_FileLineObjectStub01();

        stub.setDecimal09(null);

        Method method = DecimalColumnParser_FileLineObjectStub01.class
                .getDeclaredMethod("setDecimal09",
                        new Class[] { BigDecimal.class });
        String columnFormat = "";

        // テスト実施
        decimalColumnParser.parse(column, stub, method, columnFormat);

        // 判定
        BigDecimal result = stub.getDecimal09();
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(123456), result);
    }

    /**
     * testParse10() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) column:"-\\123,456.00"<br>
     * (引数) t:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal02<br>
     * - 型：BigDecimal<br>
     * - 初期値：null<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※setterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するsetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：1つ(BigDecimal型)<br>
     * ・処理：引数を対象フィールドに格納する。<br>
     * (引数) columnFormat:"-\\###,###,###.##"<br>
     * (状態) dfMap:要素を持たないConcurrentHashMapインスタンス<br>
     * <br>
     * 期待値：(状態変化) t:対象フィールドにBigDecimal.valueOf(12345600, 2)値が格納される。<br>
     * (状態変化) DecimalFormatLocal#<init>:1回呼ばれる<br>
     * <br>
     * 正常ケース<br>
     * フォーマット用の文字列に対するDecimalFormatLocalがキャッシュに存在しない場合、 問題なく実行されることを確認する。<br>
     * また、新しく生成されたフォーマット用の文字列に対する DecimalFormatLocalがキャッシュされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse10() throws Exception {
        // 前処理
        DecimalColumnParser decimalColumnParser = new DecimalColumnParser();

        // 前処理(引数)
        String column = "-\\123,456.00";
        DecimalColumnParser_FileLineObjectStub01 stub = new DecimalColumnParser_FileLineObjectStub01();

        stub.setDecimal10(null);

        Method method = DecimalColumnParser_FileLineObjectStub01.class
                .getDeclaredMethod("setDecimal10",
                        new Class[] { BigDecimal.class });
        String columnFormat = "-\\###,###,###.##";

        ConcurrentHashMap<String, DecimalFormatLocal> dfMap = new ConcurrentHashMap<String, DecimalFormatLocal>();
        UTUtil.setPrivateField(decimalColumnParser, "dfMap", dfMap);
        dfMap.clear();

        // テスト実施
        decimalColumnParser.parse(column, stub, method, columnFormat);

        // 判定
        BigDecimal result = stub.getDecimal10();
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(12345600, 2), result);

        assertSame(dfMap, UTUtil.getPrivateField(decimalColumnParser, "dfMap"));
        assertEquals(1, dfMap.size());
        assertTrue(dfMap.containsKey(columnFormat));
        DecimalFormatLocal dfMapValue = dfMap.get(columnFormat);
        assertNotNull(dfMapValue);
        assertEquals(columnFormat, UTUtil
                .getPrivateField(dfMapValue, "pattern"));

        assertEquals(1, VMOUTUtil.getCallCount(DecimalFormatLocal.class,
                "<init>"));
    }

    /**
     * testParse11() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) column:"-\\123,456.00"<br>
     * (引数) t:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal02<br>
     * - 型：BigDecimal<br>
     * - 初期値：null<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※setterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するsetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：1つ(BigDecimal型)<br>
     * ・処理：引数を対象フィールドに格納する。<br>
     * (引数) columnFormat:"-\\###,###,###.##"<br>
     * (状態) dfMap:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："-\\###,###,###.##"<br>
     * value：キーに対するDecimalFormatLocalインスタンス<br>
     * <br>
     * 期待値：(状態変化) t:対象フィールドにBigDecimal.valueOf(12345600, 2)値が格納される。<br>
     * (状態変化) dfMap:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："-\\###,###,###.##"<br>
     * value：キーに対するDecimalFormatLocalインスタンス<br>
     * (状態変化) DecimalFormatLocal#<init>:呼ばれない<br>
     * <br>
     * 正常ケース<br>
     * フォーマット用の文字列に対するDecimalFormatLocalがキャッシュに存在する場合、 問題なく実行されることを確認する。<br>
     * また、フォーマット用の文字列に対する DecimalFormatLocalが新しく生成されないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse11() throws Exception {
        DecimalColumnParser decimalColumnParser = new DecimalColumnParser();

        // 前処理(引数)
        String column = "-\\123,456.00";
        DecimalColumnParser_FileLineObjectStub01 stub = new DecimalColumnParser_FileLineObjectStub01();

        stub.setDecimal11(null);

        Method method = DecimalColumnParser_FileLineObjectStub01.class
                .getDeclaredMethod("setDecimal11",
                        new Class[] { BigDecimal.class });
        String columnFormat = "-\\###,###,###.##";

        ConcurrentHashMap<String, DecimalFormatLocal> dfMap = new ConcurrentHashMap<String, DecimalFormatLocal>();
        DecimalFormatLocal dfMapValue = new DecimalFormatLocal(columnFormat);
        dfMap.put(columnFormat, dfMapValue);
        UTUtil.setPrivateField(decimalColumnParser, "dfMap", dfMap);

        VMOUTUtil.initialize();

        // テスト実施
        decimalColumnParser.parse(column, stub, method, columnFormat);

        // 判定
        BigDecimal result = stub.getDecimal11();
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(12345600, 2), result);

        assertSame(dfMap, UTUtil.getPrivateField(decimalColumnParser, "dfMap"));
        assertEquals(1, dfMap.size());
        assertTrue(dfMap.containsKey(columnFormat));
        DecimalFormatLocal formatLocal = dfMap.get(columnFormat);
        assertSame(dfMapValue, formatLocal);

        assertFalse(VMOUTUtil.isCalled(DecimalFormatLocal.class, "<init>"));
    }
}
