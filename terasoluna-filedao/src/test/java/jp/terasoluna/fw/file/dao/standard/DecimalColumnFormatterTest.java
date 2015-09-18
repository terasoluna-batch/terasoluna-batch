/*
 * $Id: DecimalColumnFormatterTest.java 5354 2007-10-03 06:06:25Z anh $
 *
 * Copyright (c) 2006-2015 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import org.springframework.test.util.ReflectionTestUtils;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.DecimalColumnFormatter} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> アノテーションcolumnFormatの記述に従い、文字列の変換処理を行う。
 * <p>
 * @author 奥田 哲司
 * @see jp.terasoluna.fw.file.dao.standard.DecimalColumnFormatter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DecimalColumnFormatter.class)
public class DecimalColumnFormatterTest {

    /**
     * testFormat01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,E <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal01<br>
     * - 型：BigDecimal<br>
     * - 設定値：new BigDecimal(1000000)<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※getterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するgetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：なし<br>
     * ・処理：対象フィールの情報を返す。<br>
     * (引数) columnFormat:null<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1000000"<br>
     * <br>
     * 正常ケース<br>
     * フォーマット用の文字列がnullの場合、 かつフィールドのgetterメソッドが正しく設定されている場合に、 対象フィールドの情報が数字のみの文字列(BigDecimal.toPlainString()の結果)
     * として取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFormat01() throws Exception {
        // 前処理
        DecimalColumnFormatter decimalColumnFormatter = new DecimalColumnFormatter();

        // 前処理(引数)
        DecimalColumnFormatter_FileLineObjectStub01 stub = new DecimalColumnFormatter_FileLineObjectStub01();
        stub.setDecimal01(new BigDecimal(1000000));

        Method method = stub.getClass().getMethod("getDecimal01");

        String columnFormat = null;

        // テスト実施
        String result = decimalColumnFormatter.format(stub, method,
                columnFormat);

        // 判定
        assertNotNull(result);
        assertEquals("1000000", result);
    }

    /**
     * testFormat02() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal02<br>
     * - 型：BigDecimal<br>
     * - 設定値：new BigDecimal(1000000)<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※getterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するgetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：なし<br>
     * ・処理：対象フィールの情報を返す。<br>
     * (引数) columnFormat:"\\##,###,###.00"<br>
     * <br>
     * 期待値：(戻り値) 文字列:"\\1,000,000.00"<br>
     * <br>
     * 正常ケース<br>
     * フォーマット用の文字列が正しく設定された場合、 かつフィールドのgetterメソッドが正しく設定されている場合に、 対象フィールドの情報がフォーマットに従った文字列として取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFormat02() throws Exception {
        // 前処理
        DecimalColumnFormatter decimalColumnFormatter = new DecimalColumnFormatter();

        // 前処理(引数)
        DecimalColumnFormatter_FileLineObjectStub01 stub = new DecimalColumnFormatter_FileLineObjectStub01();
        stub.setDecimal02(new BigDecimal(1000000));

        Method method = stub.getClass().getMethod("getDecimal02");

        String columnFormat = "\\##,###,###.00";

        // テスト実施
        String result = decimalColumnFormatter.format(stub, method,
                columnFormat);

        // 判定
        assertNotNull(result);
        assertEquals("\\1,000,000.00", result);
    }

    /**
     * testFormat03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal03<br>
     * - 型：BigDecimal<br>
     * - 設定値：new BigDecimal(1000000)<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※getterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するgetterメソッドのインスタンス<br>
     * ・可視性：private<br>
     * ・引数：なし<br>
     * ・処理：対象フィールの情報を返す。<br>
     * (引数) columnFormat:"\\##,###,###.00"<br>
     * <br>
     * 期待値：(状態変化) -:IllegalAccessExceptionが発生することを確認する。<br>
     * <br>
     * 異常ケース<br>
     * フィールドのgetterメソッドがprivateで宣言された場合、 IllegalAccessExceptionが発生することを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFormat03() throws Exception {
        // 前処理
        DecimalColumnFormatter decimalColumnFormatter = new DecimalColumnFormatter();

        // 前処理(引数)
        DecimalColumnFormatter_FileLineObjectStub01 stub = new DecimalColumnFormatter_FileLineObjectStub01();
        stub.setDecimal03(new BigDecimal(1000000));

        Method method = stub.getClass().getDeclaredMethod("getDecimal03");

        String columnFormat = "\\##,###,###.00";

        // テスト実施
        try {
            decimalColumnFormatter.format(stub, method, columnFormat);
            fail("IllegalAccessExceptionが発生しませんでした。");
        } catch (IllegalAccessException e) {
            // 判定
            assertTrue(e instanceof IllegalAccessException);
        }
    }

    /**
     * testFormat04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal04<br>
     * - 型：BigDecimal<br>
     * - 設定値：new BigDecimal(1000000)<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※getterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するgetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：なし<br>
     * ・処理：例外が発生する。<br>
     * (引数) columnFormat:"\\##,###,###.00"<br>
     * <br>
     * 期待値：(状態変化) -:InvocationTargetExceptionが発生することを確認する。<br>
     * <br>
     * 異常ケース<br>
     * フィールドのgetterメソッド処理中例外が発生した場合、 InvocationTargetExceptionが発生することを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFormat04() throws Exception {
        // 前処理
        DecimalColumnFormatter decimalColumnFormatter = new DecimalColumnFormatter();

        // 前処理(引数)
        DecimalColumnFormatter_FileLineObjectStub01 stub = new DecimalColumnFormatter_FileLineObjectStub01();
        stub.setDecimal04(new BigDecimal(1000000));

        Method method = stub.getClass().getMethod("getDecimal04");

        String columnFormat = "\\##,###,###.00";

        // テスト実施
        try {
            decimalColumnFormatter.format(stub, method, columnFormat);
            fail("InvocationTargetExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定
            assertTrue(e instanceof InvocationTargetException);
        }
    }

    /**
     * testFormat05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal05<br>
     * - 型：BigDecimal<br>
     * - 設定値：new BigDecimal(1000000)<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※getterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するgetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：ある<br>
     * ・処理：対象フィールの情報を返す。<br>
     * (引数) columnFormat:new String()<br>
     * <br>
     * 期待値：(状態変化) -:IllegalArgumentExceptionが発生することを確認する。<br>
     * <br>
     * 異常ケース<br>
     * フィールドのgetterメソッドとして引数なしのメソッドが存在しない場合、 IllegalArgumentExceptionが発生することを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFormat05() throws Exception {
        // 前処理
        DecimalColumnFormatter decimalColumnFormatter = new DecimalColumnFormatter();

        // 前処理(引数)
        DecimalColumnFormatter_FileLineObjectStub01 stub = new DecimalColumnFormatter_FileLineObjectStub01();
        stub.setDecimal05(new BigDecimal(1000000));

        Method method = stub.getClass().getMethod("getDecimal05", new Class[] {
                BigDecimal.class });

        String columnFormat = new String();

        // テスト実施
        try {
            decimalColumnFormatter.format(stub, method, columnFormat);
            fail("IllegalArgumentExceptionが発生しませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    /**
     * testFormat06() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,E <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal06<br>
     * - 型：BigDecimal<br>
     * - 設定値：null<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※getterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するgetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：なし<br>
     * ・処理：対象フィールの情報を返す。<br>
     * (引数) columnFormat:null<br>
     * <br>
     * 期待値：(戻り値) 文字列:""<br>
     * <br>
     * 正常ケース<br>
     * ファイル行オブジェクトのフィールド値がnullの場合、空文字が取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFormat06() throws Exception {
        // 前処理
        DecimalColumnFormatter decimalColumnFormatter = new DecimalColumnFormatter();

        // 前処理(引数)
        DecimalColumnFormatter_FileLineObjectStub01 stub = new DecimalColumnFormatter_FileLineObjectStub01();
        stub.setDecimal06(null);

        Method method = stub.getClass().getMethod("getDecimal06");

        String columnFormat = null;

        // テスト実施
        String result = decimalColumnFormatter.format(stub, method,
                columnFormat);

        // 判定
        assertNotNull(result);
        assertEquals("", result);

    }

    /**
     * testFormat07() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,E <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal07<br>
     * - 型：BigDecimal<br>
     * - 設定値：new BigDecimal(1000000)<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※getterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するgetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：なし<br>
     * ・処理：対象フィールの情報を返す。<br>
     * (引数) columnFormat:""<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1000000"<br>
     * <br>
     * 正常ケース<br>
     * フォーマット用の文字列が空文字の場合、かつフィールドのgetterメソッドが 正しく設定されている場合に、対象フィールドの情報が数字のみの文字列 (BigDecimal.toPlainString()の結果)として取得されることを確認する。
     * <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFormat07() throws Exception {
        // 前処理
        DecimalColumnFormatter decimalColumnFormatter = new DecimalColumnFormatter();

        // 前処理(引数)
        DecimalColumnFormatter_FileLineObjectStub01 stub = new DecimalColumnFormatter_FileLineObjectStub01();
        stub.setDecimal07(new BigDecimal(1000000));

        Method method = stub.getClass().getMethod("getDecimal07");

        String columnFormat = "";

        // テスト実施
        String result = decimalColumnFormatter.format(stub, method,
                columnFormat);

        // 判定
        assertNotNull(result);
        assertEquals("1000000", result);
    }

    /**
     * testFormat08() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal08<br>
     * - 型：BigDecimal<br>
     * - 設定値：new BigDecimal(1000000)<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※getterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するgetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：なし<br>
     * ・処理：対象フィールの情報を返す。<br>
     * (引数) columnFormat:"\\##,###,###.00"<br>
     * (状態) dfMap:要素を持たないConcurrentHashMapインスタンス<br>
     * <br>
     * 期待値：(戻り値) 文字列:"\\1,000,000.00"<br>
     * (状態変化) dfMap:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："\\##,###,###.00"<br>
     * value：キーに対するDecimalFormatLocalインスタンス<br>
     * (状態変化) DecimalFormatLocal#<init>:1回呼ばれる<br>
     * <br>
     * 正常ケース<br>
     * フォーマット用の文字列に対するDecimalFormatLocalがキャッシュに存在しない場合、 問題なく実行されることを確認する。<br>
     * また、新しく生成されたフォーマット用の文字列に対する DecimalFormatLocalがキャッシュされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFormat08() throws Exception {
        // 前処理
        DecimalColumnFormatter decimalColumnFormatter = new DecimalColumnFormatter();

        // 前処理(引数)
        DecimalColumnFormatter_FileLineObjectStub01 stub = new DecimalColumnFormatter_FileLineObjectStub01();
        stub.setDecimal08(new BigDecimal(1000000));

        Method method = stub.getClass().getMethod("getDecimal08");

        String columnFormat = "\\##,###,###.00";

        ConcurrentHashMap<String, DecimalFormatLocal> dfMap = new ConcurrentHashMap<String, DecimalFormatLocal>();
        ReflectionTestUtils.setField(decimalColumnFormatter, "dfMap", dfMap);
        dfMap.clear();

        DecimalFormatLocal dfl = Mockito.spy(
                new DecimalFormatLocal(columnFormat));
        PowerMockito.whenNew(DecimalFormatLocal.class).withArguments(
                columnFormat).thenReturn(dfl);

        // テスト実施
        String result = decimalColumnFormatter.format(stub, method,
                columnFormat);

        // 判定
        assertNotNull(result);
        assertEquals("\\1,000,000.00", result);

        assertSame(dfMap, ReflectionTestUtils.getField(decimalColumnFormatter,
                "dfMap"));
        assertEquals(1, dfMap.size());
        assertTrue(dfMap.containsKey(columnFormat));
        DecimalFormatLocal dfMapValue = dfMap.get(columnFormat);
        assertNotNull(dfMapValue);
        assertEquals(columnFormat, ReflectionTestUtils.getField(dfMapValue,
                "pattern"));
        PowerMockito.verifyNew(DecimalFormatLocal.class).withArguments(
                columnFormat);
    }

    /**
     * testFormat09() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報をObjectインスタンス<br>
     * ・フィールド<br>
     * - フィールド名：decimal09<br>
     * - 型：BigDecimal<br>
     * - 設定値：new BigDecimal(1000000)<br>
     * ・メソッド<br>
     * - フィールドに対するgetter、setterメソッドが存在する。<br>
     * ※getterメソッドの定義は引数methodの記述に従う。<br>
     * (引数) method:対象フィールドに対するgetterメソッドのインスタンス<br>
     * ・可視性：public<br>
     * ・引数：なし<br>
     * ・処理：対象フィールの情報を返す。<br>
     * (引数) columnFormat:"\\##,###,###.00"<br>
     * (状態) dfMap:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："\\##,###,###.00"<br>
     * value：キーに対するDecimalFormatLocalインスタンス<br>
     * <br>
     * 期待値：(戻り値) 文字列:"\\1,000,000.00"<br>
     * (状態変化) dfMap:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："\\##,###,###.00"<br>
     * value：キーに対するDecimalFormatLocalインスタンス<br>
     * (状態変化) DecimalFormatLocal#<init>:呼ばれない<br>
     * <br>
     * 正常ケース<br>
     * フォーマット用の文字列に対するDecimalFormatLocalがキャッシュに存在する場合、 問題なく実行されることを確認する。<br>
     * また、フォーマット用の文字列に対するDecimalFormatLocalが新しく生成されないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFormat09() throws Exception {
        // 前処理
        DecimalColumnFormatter decimalColumnFormatter = new DecimalColumnFormatter();

        // 前処理(引数)
        DecimalColumnFormatter_FileLineObjectStub01 stub = new DecimalColumnFormatter_FileLineObjectStub01();
        stub.setDecimal09(new BigDecimal(1000000));

        Method method = stub.getClass().getMethod("getDecimal09");

        String columnFormat = "\\##,###,###.00";

        ConcurrentHashMap<String, DecimalFormatLocal> dfMap = new ConcurrentHashMap<String, DecimalFormatLocal>();
        DecimalFormatLocal dfMapValue = Mockito.spy(
                new DecimalFormatLocal(columnFormat));
        dfMap.put(columnFormat, dfMapValue);
        ReflectionTestUtils.setField(decimalColumnFormatter, "dfMap", dfMap);

        PowerMockito.whenNew(DecimalFormatLocal.class).withArguments(
                columnFormat).thenReturn(dfMapValue);
        // テスト実施
        String result = decimalColumnFormatter.format(stub, method,
                columnFormat);

        // 判定
        assertNotNull(result);
        assertEquals("\\1,000,000.00", result);

        assertSame(dfMap, ReflectionTestUtils.getField(decimalColumnFormatter,
                "dfMap"));
        assertEquals(1, dfMap.size());
        assertTrue(dfMap.containsKey(columnFormat));
        DecimalFormatLocal formatLocal = dfMap.get(columnFormat);
        assertSame(dfMapValue, formatLocal);

        PowerMockito.verifyNew(DecimalFormatLocal.class, Mockito.never())
                .withArguments(columnFormat);
    }
}
