package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.Mockito;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.DateColumnParser} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> 指定された文字列をパーズし、Date型に変換する。<br>
 * 変換結果をファイル行オブジェクトのDate型の属性に値を格納する。
 * <p>
 * @see jp.terasoluna.fw.file.dao.standard.DateColumnParser
 */
public class DateColumnParserTest {

    /**
     * testParse01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) column:"20060101"<br>
     * (引数) ファイル行オブジェクト<br>
     * ｔ:Date型のフィールドを持つ<br>
     * 値：null<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性の setterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 可視性：public<br>
     * 引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:null もしくは 空文字<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(状態変化) ファイル行オブジェクト(t)の属性:columnで設定した yyyyMMdd形式の文字列をDate型にパースした値<br>
     * (状態変化) Map.get():メソッドが1回呼ばれる。<br>
     * (状態変化) Map.put():メソッドが1回呼ばれる。<br>
     * <br>
     * 引数columnFormatがnullか空文字で、マップが空の場合、 フォーマット用のインスタンスがキャッシャされることを確認する。<br>
     * ファイル行オブジェクトに、引数columnで設定した文字列を Date型にパースした値が設定できること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse01() throws Exception {
        // テスト対象のインスタンス化
        DateColumnParser dateColumnParser = new DateColumnParser();

        // 引数の設定
        String column = "20060101";
        DateColumnParser_Stub01 t = new DateColumnParser_Stub01();
        Method method = DateColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Date.class });
        String columnFormat = "";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = Mockito.spy(
                new ConcurrentHashMap<String, DateFormatLocal>());
        ReflectionTestUtils.setField(dateColumnParser, "map", map);

        // テスト実施
        dateColumnParser.parse(column, t, method, columnFormat);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result = ReflectionTestUtils.getField(t, "a");
        // 文字列からDate型に変換
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        simpleDateFormat.setLenient(false);
        Date expected = simpleDateFormat.parse(column);
        assertEquals(expected, result);

        Mockito.verify(map).get(Mockito.anyString());
        Mockito.verify(map).put(Mockito.anyString(), Mockito.any(
                DateFormatLocal.class));

        assertEquals(1, map.size());
        assertNotNull(map.get("yyyyMMdd"));
    }

    /**
     * testParse02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) column:"20060101"<br>
     * (引数) ファイル行オブジェクト<br>
     * ｔ:Date型のフィールドを持つ<br>
     * 値：null<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性の setterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 可視性：public<br>
     * 引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:null もしくは 空文字<br>
     * (状態) map:key:"yyyyMMdd"<br>
     * value:new DateFormatLocal("yyyyMMdd")<br>
     * <br>
     * 期待値：(状態変化) ファイル行オブジェクト(t)の属性:columnで設定した yyyyMMdd形式の文字列をDate型にパースした値<br>
     * (状態変化) Map.get():メソッドが1回呼ばれる。<br>
     * (状態変化) Map.put():メソッドが呼ばれない。<br>
     * <br>
     * 引数columnFormatがnullか空文字で、マップに"yyyyMMdd"をキーにした キャッシュが存在する場合は、新たにキャッシュされないこと。<br>
     * ファイル行オブジェクトに、引数columnで設定した文字列を Date型にパースした値が設定できること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse02() throws Exception {
        // テスト対象のインスタンス化
        DateColumnParser dateColumnParser = new DateColumnParser();

        // 引数の設定
        String column = "20060101";
        DateColumnParser_Stub01 t = new DateColumnParser_Stub01();
        Method method = DateColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Date.class });
        String columnFormat = "";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        DateFormatLocal cache = new DateFormatLocal("yyyyMMdd");
        map.put("yyyyMMdd", cache);
        map = Mockito.spy(map);
        ReflectionTestUtils.setField(dateColumnParser, "map", map);

        // テスト実施
        dateColumnParser.parse(column, t, method, columnFormat);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result = ReflectionTestUtils.getField(t, "a");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        simpleDateFormat.setLenient(false);
        Date expected = simpleDateFormat.parse(column);
        assertEquals(expected, result);
        Mockito.verify(map).get(Mockito.anyString());
        Mockito.verify(map, Mockito.never()).put(Mockito.anyString(), Mockito
                .any(DateFormatLocal.class));

        assertEquals(1, map.size());
        assertSame(cache, map.get("yyyyMMdd"));
    }

    /**
     * testParse03() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) column:"20060101"<br>
     * (引数) ファイル行オブジェクト<br>
     * ｔ:Date型のフィールドを持つ<br>
     * 値：null<br>
     * (引数) ファイル行オブジェクト(t)にある Date型属性のsetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 可視性：public<br>
     * 引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:null もしくは 空文字<br>
     * (状態) map:key:"yyyy-MM-dd"<br>
     * value:new DateFormatLocal("yyyy-MM-dd")<br>
     * <br>
     * 期待値：(状態変化) ファイル行オブジェクト(t)の属性:columnで設定した yyyyMMdd形式の文字列をDate型にパースした値<br>
     * (状態変化) Map.get():メソッドが1回呼ばれる。<br>
     * (状態変化) Map.put():メソッドが1回呼ばれる。<br>
     * <br>
     * 引数columnFormatがnullか空文字で、マップに"yyyyMMdd"以外をキーにした キャッシュが存在する場合は、"yyyyMMdd"をキーにしたフォーマット用 インスタンスがキャッシュされること。<br>
     * ファイル行オブジェクトに、引数columnで設定した文字列をDate型に パースした値が設定できること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse03() throws Exception {
        // テスト対象のインスタンス化
        DateColumnParser dateColumnParser = new DateColumnParser();

        // 引数の設定
        String column = "20060101";
        DateColumnParser_Stub01 t = new DateColumnParser_Stub01();
        Method method = DateColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Date.class });
        String columnFormat = "";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        DateFormatLocal cache = new DateFormatLocal("yyyy-MM-dd");
        map.put("yyyy-MM-dd", cache);
        map = Mockito.spy(map);
        ReflectionTestUtils.setField(dateColumnParser, "map", map);

        // テスト実施
        dateColumnParser.parse(column, t, method, columnFormat);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result = ReflectionTestUtils.getField(t, "a");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        simpleDateFormat.setLenient(false);
        Date expected = simpleDateFormat.parse(column);
        assertEquals(expected, result);
        Mockito.verify(map).get(Mockito.anyString());
        Mockito.verify(map).put(Mockito.anyString(), Mockito.any(
                DateFormatLocal.class));

        assertEquals(2, map.size());
        assertNotNull(map.get("yyyyMMdd"));
        assertSame(cache, map.get("yyyy-MM-dd"));
    }

    /**
     * testParse04() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) column:"2006-01-01"<br>
     * (引数) ファイル行オブジェクト<br>
     * ｔ:Date型のフィールドを持つ<br>
     * 値：null<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性の setterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 可視性：public<br>
     * 引数：1つ<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy-MM-dd"<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(状態変化) ファイル行オブジェクト(t)の属性:columnで設定した yyyy-MM-dd形式の文字列をDate型にパースした値<br>
     * (状態変化) Map.get():メソッドが1回呼ばれる。<br>
     * (状態変化) Map.put():メソッドが1回呼ばれる。<br>
     * <br>
     * 引数columnFormatにフォーマット用文字列が設定されており、 マップが空の場合は、columnFormatの文字列をキーにキャッシュされること。<br>
     * ファイル行オブジェクトに、引数columnで設定した文字列をDate型に パースした値が設定できること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse04() throws Exception {
        // テスト対象のインスタンス化
        DateColumnParser dateColumnParser = new DateColumnParser();

        // 引数の設定
        String column = "2006-01-01";
        DateColumnParser_Stub01 t = new DateColumnParser_Stub01();
        Method method = DateColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Date.class });
        String columnFormat = "yyyy-MM-dd";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        map = Mockito.spy(map);
        ReflectionTestUtils.setField(dateColumnParser, "map", map);

        // テスト実施
        dateColumnParser.parse(column, t, method, columnFormat);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result = ReflectionTestUtils.getField(t, "a");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setLenient(false);
        Date expected = simpleDateFormat.parse(column);
        assertEquals(expected, result);
        Mockito.verify(map).get(Mockito.anyString());
        Mockito.verify(map).put(Mockito.anyString(), Mockito.any(
                DateFormatLocal.class));

        assertEquals(1, map.size());
        assertNotNull(map.get("yyyy-MM-dd"));
    }

    /**
     * testParse05() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) column:"2006-01-01"<br>
     * (引数) ファイル行オブジェクト<br>
     * ｔ:Date型のフィールドを持つ<br>
     * 値：null<br>
     * (引数) ファイル行オブジェクト(t)にある Date型属性のsetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 可視性：public<br>
     * 引数：1つ<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy-MM-dd"<br>
     * (状態) map:key:"yyyy-MM-dd"<br>
     * value:new DateFormatLocal("yyyy-MM-dd")<br>
     * <br>
     * 期待値：(状態変化) ファイル行オブジェクト(t)の属性:columnで設定した yyyy-MM-dd形式の文字列をDate型にパースした値<br>
     * (状態変化) Map.get():メソッドが1回呼ばれる。<br>
     * (状態変化) Map.put():メソッドが呼ばれない。<br>
     * <br>
     * 引数columnFormatにフォーマット用文字列が設定されており、 マップにフォーマット用文字列をキーにしたキャッシュが存在する場合は、 新たにキャッシュされないこと。<br>
     * ファイル行オブジェクトに、引数columnで設定した文字列をDate型に パースした値が設定できること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse05() throws Exception {
        // テスト対象のインスタンス化
        DateColumnParser dateColumnParser = new DateColumnParser();

        // 引数の設定
        String column = "2006-01-01";
        DateColumnParser_Stub01 t = new DateColumnParser_Stub01();
        Method method = DateColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Date.class });
        String columnFormat = "yyyy-MM-dd";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        DateFormatLocal cache = new DateFormatLocal(columnFormat);
        map.put(columnFormat, cache);
        map = Mockito.spy(map);
        ReflectionTestUtils.setField(dateColumnParser, "map", map);

        // テスト実施
        dateColumnParser.parse(column, t, method, columnFormat);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result = ReflectionTestUtils.getField(t, "a");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setLenient(false);
        Date expected = simpleDateFormat.parse(column);
        assertEquals(expected, result);
        Mockito.verify(map).get(Mockito.anyString());
        Mockito.verify(map, Mockito.never()).put(Mockito.anyString(), Mockito
                .any(DateFormatLocal.class));

        assertEquals(1, map.size());
        assertSame(cache, map.get("yyyy-MM-dd"));
    }

    /**
     * testParse06() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) column:"2006-01-01"<br>
     * (引数) ファイル行オブジェクト<br>
     * ｔ:Date型のフィールドを持つ<br>
     * 値：null<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性の setterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 可視性：public<br>
     * 引数：1つ<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy-MM-dd"<br>
     * (状態) map:key:"yyyy/MM/dd"<br>
     * value:new DateFormatLocal("yyyy/MM/dd")<br>
     * <br>
     * 期待値：(状態変化) ファイル行オブジェクト(t)の属性:columnで設定した yyyy-MM-dd形式の文字列をDate型にパースした値<br>
     * (状態変化) Map.get():メソッドが1回呼ばれる。<br>
     * (状態変化) Map.put():メソッドが1回呼ばれる。<br>
     * <br>
     * 引数columnFormatにフォーマット用文字列が設定されており、 マップにフォーマット用文字列をキーにしたキャッシュが存在しない場合は、 キャッシュされること。<br>
     * ファイル行オブジェクトに、引数columnで設定した文字列をDate型に パースした値が設定できること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse06() throws Exception {
        // テスト対象のインスタンス化
        DateColumnParser dateColumnParser = new DateColumnParser();

        // 引数の設定
        String column = "2006-01-01";
        DateColumnParser_Stub01 t = new DateColumnParser_Stub01();
        Method method = DateColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Date.class });
        String columnFormat = "yyyy-MM-dd";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        DateFormatLocal cache = new DateFormatLocal("yyyy/MM/dd");
        map.put("yyyy/MM/dd", cache);
        map = Mockito.spy(map);
        ReflectionTestUtils.setField(dateColumnParser, "map", map);

        // テスト実施
        dateColumnParser.parse(column, t, method, columnFormat);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result = ReflectionTestUtils.getField(t, "a");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setLenient(false);
        Date expected = simpleDateFormat.parse(column);
        assertEquals(expected, result);
        Mockito.verify(map).get(Mockito.anyString());
        Mockito.verify(map).put(Mockito.anyString(), Mockito.any(
                DateFormatLocal.class));

        assertEquals(2, map.size());
        assertSame(cache, map.get("yyyy/MM/dd"));
        assertNotNull(map.get("yyyy-MM-dd"));
    }

    /**
     * testParse07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:"2006-01-01"<br>
     * (引数) ファイル行オブジェクト<br>
     * ｔ:Date型のフィールドを持つ<br>
     * 値：null<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性の setterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 可視性：public<br>
     * 引数：1つ<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:formatできない文字列<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(状態変化) 例外:ParseExceptionが発生することを確認する。<br>
     * <br>
     * フォーマット文字列にありえない値が設定された場合、ParseExceptionが 発生することを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse07() throws Exception {
        // テスト対象のインスタンス化
        DateColumnParser dateColumnParser = new DateColumnParser();

        // 引数の設定
        String column = "2006-01-01";
        DateColumnParser_Stub01 t = new DateColumnParser_Stub01();
        Method method = DateColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Date.class });
        String columnFormat = "yyyy-MM-dd-MM";

        // 前提条件の設定
        // デフォルトで要素なしmapを持つので、何もしない。

        try {
            // テスト実施
            dateColumnParser.parse(column, t, method, columnFormat);
            fail("ParseExceptionがスローされました。");
        } catch (ParseException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(ParseException.class, e.getClass());
            assertEquals("Unparseable date: \"2006-01-01\"", e.getMessage());
        }
    }

    /**
     * testParse08() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:"2006-01-01"<br>
     * (引数) ファイル行オブジェクト<br>
     * ｔ:Date型のフィールドを持つ<br>
     * 値：null<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性の setterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 可視性：private<br>
     * 引数：1つ<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy-MM-dd"<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalAccessExceptionが発生することを 確認する。<br>
     * <br>
     * ファイル行オブジェクトのDate型属性のsetterメソッドに アクセスできない場合、IllegalAccessExceptionをスローすることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse08() throws Exception {
        // テスト対象のインスタンス化
        DateColumnParser dateColumnParser = new DateColumnParser();

        // 引数の設定
        String column = "2006-01-01";
        DateColumnParser_Stub01 t = new DateColumnParser_Stub01();
        Method method = DateColumnParser_Stub01.class.getDeclaredMethod(
                "setAPrivate", new Class[] { Date.class });
        String columnFormat = "yyyy-MM-dd";

        // 前提条件の設定
        // デフォルトで要素なしmapを持つので、何もしない。

        try {
            // テスト実施
            dateColumnParser.parse(column, t, method, columnFormat);
            fail("IllegalAccessExceptionがスローされませんでした。");
        } catch (IllegalAccessException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(IllegalAccessException.class, e.getClass());
        }
    }

    /**
     * testParse09() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:"2006-01-01"<br>
     * (引数) ファイル行オブジェクト<br>
     * ｔ:Date型のフィールドを持つ<br>
     * 値：例外をスローする<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性の setterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 可視性：public<br>
     * 引数：1つ<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy-MM-dd"<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(状態変化) 例外:InvocationTargetExceptionが 発生することを確認する。<br>
     * <br>
     * ファイル行オブジェクトのDate型属性のsetterメソッドが例外を スローする場合、setterメソッドがスローした例外をラップする InvocationTargetExceptionをスローすることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse09() throws Exception {
        // テスト対象のインスタンス化
        DateColumnParser dateColumnParser = new DateColumnParser();

        // 引数の設定
        String column = "2006-01-01";
        DateColumnParser_Stub01 t = new DateColumnParser_Stub01();
        Method method = DateColumnParser_Stub01.class.getDeclaredMethod(
                "setAException", new Class[] { Date.class });
        String columnFormat = "yyyy-MM-dd";

        // 前提条件の設定
        // デフォルトで要素なしmapを持つので、何もしない。

        try {
            // テスト実施
            dateColumnParser.parse(column, t, method, columnFormat);
            fail("InvocationTargetExceptionがスローされました。");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(InvocationTargetException.class, e.getClass());
        }
    }

    /**
     * testParse10() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:"2006-01-01"<br>
     * (引数) ファイル行オブジェクト<br>
     * ｔ:Date型のフィールドを持つ<br>
     * 値：null<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性の setterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 可視性：public<br>
     * 引数：複数<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy-MM-dd"<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentExceptionが 発生することを確認する。<br>
     * <br>
     * ファイル行オブジェクトのDate型属性のsetterメソッドの引数が多数ある場合、 IllegalArgumentExceptionをスローすることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse10() throws Exception {
        // テスト対象のインスタンス化
        DateColumnParser dateColumnParser = new DateColumnParser();

        // 引数の設定
        String column = "2006-01-01";
        DateColumnParser_Stub01 t = new DateColumnParser_Stub01();
        Method method = DateColumnParser_Stub01.class.getDeclaredMethod(
                "setAAndB", new Class[] { Date.class, Date.class });
        String columnFormat = "yyyy-MM-dd";

        // 前提条件の設定
        // デフォルトで要素なしmapを持つので、何もしない。

        try {
            // テスト実施
            dateColumnParser.parse(column, t, method, columnFormat);
            fail("IllegalArgumentExceptionがスローされませんでした。");
        } catch (IllegalArgumentException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    /**
     * testParse11() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:"2008-01-32"<br>
     * (引数) ファイル行オブジェクト<br>
     * ｔ:Date型のフィールドを持つ<br>
     * 値：null<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性の setterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 可視性：public<br>
     * 引数：1つ<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy-MM-dd"<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(状態変化) 例外:ParseExceptionが発生することを確認する。<br>
     * <br>
     * Date型の属性の値が想定されない日付の場合、 ParseExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse11() throws Exception {
        // テスト対象のインスタンス化
        DateColumnParser dateColumnParser = new DateColumnParser();

        // 引数の設定
        String column = "2008-01-32";
        DateColumnParser_Stub01 t = new DateColumnParser_Stub01();
        Method method = DateColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Date.class });
        String columnFormat = "yyyy-MM-dd";

        // 前提条件の設定
        // デフォルトで要素なしmapを持つので、何もしない。

        try {
            // テスト実施
            dateColumnParser.parse(column, t, method, columnFormat);
            fail("ParseExceptionがスローされませんでした。");
        } catch (ParseException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(ParseException.class, e.getClass());
            assertEquals("Unparseable date: \"2008-01-32\"", e.getMessage());
        }
    }

    /**
     * testParse12() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:null<br>
     * (引数) ファイル行オブジェクト<br>
     * ｔ:Date型のフィールドを持つ<br>
     * 値：null<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性の setterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 可視性：public<br>
     * 引数：1つ<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy-MM-dd"<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(状態変化) 例外:NullPointerExceptionが 発生することを確認する。<br>
     * <br>
     * 引数columnにnullが設定された場合は、NullPointerExceptionが 発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse12() throws Exception {
        // テスト対象のインスタンス化
        DateColumnParser dateColumnParser = new DateColumnParser();

        // 引数の設定
        String column = null;
        DateColumnParser_Stub01 t = new DateColumnParser_Stub01();
        Method method = DateColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Date.class });
        String columnFormat = "yyyy-MM-dd";

        // 前提条件の設定
        // デフォルトで要素なしmapを持つので、何もしない。

        try {
            // テスト実施
            dateColumnParser.parse(column, t, method, columnFormat);
            fail("NullPointerExceptionがスローされませんでした。");
        } catch (NullPointerException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(NullPointerException.class, e.getClass());
        }
    }

    /**
     * testParse13() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) column:"20080101"<br>
     * (引数) ファイル行オブジェクト<br>
     * ｔ:Date型のフィールドを持つ<br>
     * 値：null<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性の setterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 可視性：public<br>
     * 引数：1つ<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy-MM-dd"<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(状態変化) 例外:ParseExceptionが発生することを確認する。<br>
     * <br>
     * 引数のcolumnのフォーマットとフォーマット用の文字列が異なる場合は、 ParseExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testParse13() throws Exception {
        // テスト対象のインスタンス化
        DateColumnParser dateColumnParser = new DateColumnParser();

        // 引数の設定
        String column = "20080101";
        DateColumnParser_Stub01 t = new DateColumnParser_Stub01();
        Method method = DateColumnParser_Stub01.class.getDeclaredMethod("setA",
                new Class[] { Date.class });
        String columnFormat = "yyyy-MM-dd";

        // 前提条件の設定
        // デフォルトで要素なしmapを持つので、何もしない。

        try {
            // テスト実施
            dateColumnParser.parse(column, t, method, columnFormat);
            fail("ParseExceptionがスローされませんでした。");
        } catch (ParseException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(ParseException.class, e.getClass());
            assertEquals("Unparseable date: \"20080101\"", e.getMessage());
        }
    }

}
