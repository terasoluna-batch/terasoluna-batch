/*
 * $Id: DateColumnFormatterTest.java 5354 2007-10-03 06:06:25Z anh $
 * 
 * Copyright (c) 2006 NTT DATA Corporation
 * 
 */

package jp.terasoluna.fw.file.dao.standard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.test.util.ReflectionTestUtils;

import jp.terasoluna.fw.file.ut.VMOUTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.DateColumnFormatter} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> アノテーションcolumnFormatの記述に従い、文字列の変換処理を行う。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.DateColumnFormatter
 */
public class DateColumnFormatterTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(DateColumnFormatterTest.class);
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
    public DateColumnFormatterTest(String name) {
        super(name);
    }

    /**
     * testFormat01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:not null<br>
     * Date型フィールドを持つ<br>
     * 　値：new Date(0)<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:null<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(戻り値) String:引数のmethodのDate型属性に格納されている値がyyyyMMddフォーマットの文字列で出力される<br>
     * (状態変化) Map.get():メソッドが1回呼ばれる。<br>
     * (状態変化) Map.put():メソッドが1回呼ばれる。<br>
     * (状態変化) DateFormatLocalコンストラクタ:メソッドが1回呼ばれる。<br>
     * <br>
     * ファイル行オブジェクトからDate型属性に格納されているオブジェクトの文字列を取得することができることを確認する。フォーマット用の文字列がnullもしくは空文字の場合はフォーマット処理を行わず("yyyyMMdd")に出力されることを確認する
     * 。マップにフォーマット用の文字列をキャッシュする。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testFormat01() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new DateColumnFormatter();

        // 引数の設定
        DateColumnFormatter_Stub01 stub = new DateColumnFormatter_Stub01();
        ReflectionTestUtils.setField(stub, "date", new Date(0));
        Method method = stub.getClass().getMethod("getDate");
        String columnFormat = null;

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        ReflectionTestUtils.setField(columnFormatter, "map", map);

        // テスト実施
        String testResult = columnFormatter.format(stub, method, columnFormat);

        // 返却値の確認
        assertEquals("19700101", testResult);

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(Map.class, "get"));
        assertEquals("yyyyMMdd", VMOUTUtil.getArgument(Map.class, "get", 0, 0));
        assertEquals(1, VMOUTUtil.getCallCount(Map.class, "put"));
        List arguments = VMOUTUtil.getArguments(Map.class, "put", 0);
        assertEquals("yyyyMMdd", arguments.get(0));
        assertTrue(arguments.get(1) instanceof DateFormatLocal);
        Map getMap = (Map) ReflectionTestUtils.getField(columnFormatter, "map");
        assertEquals(1, getMap.size());
        assertEquals(1, VMOUTUtil.getCallCount(DateFormatLocal.class, "<init>"));
        assertEquals("yyyyMMdd", VMOUTUtil.getArgument(DateFormatLocal.class,
                "<init>", 0, 0));
    }

    /**
     * testFormat02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:not null<br>
     * Date型フィールドを持つ<br>
     * 　値：new Date(0)<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:""<br>
     * (状態) map:key:"yyyyMMdd"<br>
     * value:new DateFormatLocal("yyyyMMdd")<br>
     * <br>
     * 期待値：(戻り値) String:引数のmethodのDate型属性に格納されている値がyyyyMMddフォーマットの文字列で出力される<br>
     * (状態変化) Map.get():メソッドが1回呼ばれる。<br>
     * (状態変化) Map.put():メソッドが呼ばれない。<br>
     * (状態変化) DateFormatLocalコンストラクタ:メソッドが呼ばれない。<br>
     * <br>
     * ファイル行オブジェクトからDate型属性に格納されているオブジェクトの文字列を取得することができることを確認する。マップから取得したフォーマット用の文字列で設定されたとおりに出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testFormat02() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new DateColumnFormatter();

        // 引数の設定
        DateColumnFormatter_Stub01 stub = new DateColumnFormatter_Stub01();
        ReflectionTestUtils.setField(stub, "date", new Date(0));
        Method method = stub.getClass().getMethod("getDate");
        String columnFormat = "";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        map.put("yyyyMMdd", new DateFormatLocal("yyyyMMdd"));
        ReflectionTestUtils.setField(columnFormatter, "map", map);

        VMOUTUtil.initialize();

        // テスト実施
        String testResult = columnFormatter.format(stub, method, columnFormat);

        // 返却値の確認
        assertEquals("19700101", testResult);

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(Map.class, "get"));
        assertEquals("yyyyMMdd", VMOUTUtil.getArgument(Map.class, "get", 0, 0));
        assertFalse(VMOUTUtil.isCalled(Map.class, "put"));
        assertFalse(VMOUTUtil.isCalled(DateFormatLocal.class, "<init>"));
    }

    /**
     * testFormat03() <br>
     * <br>
     * (正常系)<br>
     * <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:not null<br>
     * Date型フィールドを持つ<br>
     * 　値：new Date(0)<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:""<br>
     * (状態) map:key:"yyyy-MM-dd"<br>
     * value:new DateFormatLocal("yyyy-MM-dd")<br>
     * <br>
     * 期待値：(戻り値) String:引数のmethodのDate型属性に格納されている値がyyyyMMddフォーマットの文字列で出力される<br>
     * (状態変化) Map.get():メソッドが1回呼ばれる。<br>
     * (状態変化) Map.put():メソッドが1回呼ばれる。<br>
     * (状態変化) DateFormatLocalコンストラクタ:メソッドが1回呼ばれる。<br>
     * <br>
     * ファイル行オブジェクトからDate型属性に格納されているオブジェクトの文字列を取得することができることを確認する。フォーマット用の文字列がnullもしくは空文字の場合はフォーマット処理を行わず("yyyyMMdd")に出力されることを確認する
     * 。マップにフォーマット用の文字列をキャッシュする。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testFormat03() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new DateColumnFormatter();

        // 引数の設定
        DateColumnFormatter_Stub01 stub = new DateColumnFormatter_Stub01();
        ReflectionTestUtils.setField(stub, "date", new Date(0));
        Method method = stub.getClass().getMethod("getDate");
        String columnFormat = "";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        map.put("yyyy-MM-dd", new DateFormatLocal("yyyy-MM-dd"));
        ReflectionTestUtils.setField(columnFormatter, "map", map);

        VMOUTUtil.initialize();

        // テスト実施
        String testResult = columnFormatter.format(stub, method, columnFormat);

        // 返却値の確認
        assertEquals("19700101", testResult);

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(Map.class, "get"));
        assertEquals("yyyyMMdd", VMOUTUtil.getArgument(Map.class, "get", 0, 0));
        assertEquals(1, VMOUTUtil.getCallCount(Map.class, "put"));
        List arguments = VMOUTUtil.getArguments(Map.class, "put", 0);
        assertEquals("yyyyMMdd", arguments.get(0));
        assertTrue(arguments.get(1) instanceof DateFormatLocal);
        Map getMap = (Map) ReflectionTestUtils.getField(columnFormatter, "map");
        assertEquals(2, getMap.size());
        assertEquals(1, VMOUTUtil.getCallCount(DateFormatLocal.class, "<init>"));
        assertEquals("yyyyMMdd", VMOUTUtil.getArgument(DateFormatLocal.class,
                "<init>", 0, 0));
    }

    /**
     * testFormat04() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:not null<br>
     * Date型フィールドを持つ<br>
     * 　値：new Date(0)<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy/MM/dd"<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(戻り値) String:引数のmethodのDate型属性に格納されている値の文字列。columnFormatで定義したフォーマットで出力される。<br>
     * (状態変化) Map.get():メソッドが1回呼ばれる。<br>
     * (状態変化) Map.put():メソッドが1回呼ばれる。<br>
     * (状態変化) DateFormatLocalコンストラクタ:メソッドが1回呼ばれる。<br>
     * <br>
     * ファイル行オブジェクトからDate型属性に格納されているオブジェクトの文字列を取得することができることを確認する。フォーマット用の文字列で設定されたとおりに出力されることを確認する。マップにフォーマット用の文字列をキャッシュする。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testFormat04() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new DateColumnFormatter();

        // 引数の設定
        DateColumnFormatter_Stub01 stub = new DateColumnFormatter_Stub01();
        ReflectionTestUtils.setField(stub, "date", new Date(0));
        Method method = stub.getClass().getMethod("getDate");
        String columnFormat = "yyyy/MM/dd";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        ReflectionTestUtils.setField(columnFormatter, "map", map);

        // テスト実施
        String testResult = columnFormatter.format(stub, method, columnFormat);

        // 返却値の確認
        assertEquals("1970/01/01", testResult);

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(Map.class, "get"));
        assertEquals(columnFormat, VMOUTUtil
                .getArgument(Map.class, "get", 0, 0));
        assertEquals(1, VMOUTUtil.getCallCount(Map.class, "put"));
        List arguments = VMOUTUtil.getArguments(Map.class, "put", 0);
        assertEquals(columnFormat, arguments.get(0));
        assertTrue(arguments.get(1) instanceof DateFormatLocal);
        Map getMap = (Map) ReflectionTestUtils.getField(columnFormatter, "map");
        assertEquals(1, getMap.size());
        assertEquals(1, VMOUTUtil.getCallCount(DateFormatLocal.class, "<init>"));
        assertEquals(columnFormat, VMOUTUtil.getArgument(DateFormatLocal.class,
                "<init>", 0, 0));
    }

    /**
     * testFormat05() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:not null<br>
     * Date型フィールドを持つ<br>
     * 　値：new Date(0)<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy/MM/dd"<br>
     * (状態) map:key:"yyyy/MM/dd"<br>
     * value:new DateFormatLocal("yyyy/MM/dd")<br>
     * <br>
     * 期待値：(戻り値) String:引数のmethodのDate型属性に格納されている値の文字列。columnFormatで定義したフォーマットで出力される。<br>
     * (状態変化) Map.get():メソッドが1回呼ばれる。<br>
     * (状態変化) Map.put():メソッドが呼ばれない。<br>
     * (状態変化) DateFormatLocalコンストラクタ:メソッドが呼ばれない。<br>
     * <br>
     * ファイル行オブジェクトからDate型属性に格納されているオブジェクトの文字列を取得することができることを確認する。マップから取得したフォーマット用の文字列で設定されたとおりに出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFormat05() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new DateColumnFormatter();

        // 引数の設定
        DateColumnFormatter_Stub01 stub = new DateColumnFormatter_Stub01();
        ReflectionTestUtils.setField(stub, "date", new Date(0));
        Method method = stub.getClass().getMethod("getDate");
        String columnFormat = "yyyy/MM/dd";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        map.put(columnFormat, new DateFormatLocal(columnFormat));
        ReflectionTestUtils.setField(columnFormatter, "map", map);

        VMOUTUtil.initialize();

        // テスト実施
        String testResult = columnFormatter.format(stub, method, columnFormat);

        // 返却値の確認
        assertEquals("1970/01/01", testResult);

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(Map.class, "get"));
        assertEquals(columnFormat, VMOUTUtil
                .getArgument(Map.class, "get", 0, 0));
        assertFalse(VMOUTUtil.isCalled(Map.class, "put"));
        assertFalse(VMOUTUtil.isCalled(DateFormatLocal.class, "<init>"));
    }

    /**
     * testFormat06() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:not null<br>
     * Date型フィールドを持つ<br>
     * 　値：new Date(0)<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy/MM/dd"<br>
     * (状態) map:key:"yyyy-MM-dd"<br>
     * value:new DateFormatLocal("yyyy-MM-dd")<br>
     * <br>
     * 期待値：(戻り値) String:引数のmethodのDate型属性に格納されている値の文字列。columnFormatで定義したフォーマットで出力される。<br>
     * (状態変化) Map.get():メソッドが1回呼ばれる。<br>
     * (状態変化) Map.put():メソッドが1回呼ばれる。<br>
     * (状態変化) DateFormatLocalコンストラクタ:メソッドが1回呼ばれる。<br>
     * <br>
     * ファイル行オブジェクトからDate型属性に格納されているオブジェクトの文字列を取得することができることを確認する。フォーマット用の文字列で設定されたとおりに出力されることを確認する。マップにフォーマット用の文字列をキャッシュする。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testFormat06() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new DateColumnFormatter();

        // 引数の設定
        DateColumnFormatter_Stub01 stub = new DateColumnFormatter_Stub01();
        ReflectionTestUtils.setField(stub, "date", new Date(0));
        Method method = stub.getClass().getMethod("getDate");
        String columnFormat = "yyyy/MM/dd";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        map.put("yyyy-MM-dd", new DateFormatLocal("yyyy-MM-dd"));
        ReflectionTestUtils.setField(columnFormatter, "map", map);

        VMOUTUtil.initialize();

        // テスト実施
        String testResult = columnFormatter.format(stub, method, columnFormat);

        // 返却値の確認
        assertEquals("1970/01/01", testResult);

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(Map.class, "get"));
        assertEquals(columnFormat, VMOUTUtil
                .getArgument(Map.class, "get", 0, 0));
        assertEquals(1, VMOUTUtil.getCallCount(Map.class, "put"));
        List arguments = VMOUTUtil.getArguments(Map.class, "put", 0);
        assertEquals(columnFormat, arguments.get(0));
        assertTrue(arguments.get(1) instanceof DateFormatLocal);
        Map getMap = (Map) ReflectionTestUtils.getField(columnFormatter, "map");
        assertEquals(2, getMap.size());
        assertEquals(1, VMOUTUtil.getCallCount(DateFormatLocal.class, "<init>"));
        assertEquals(columnFormat, VMOUTUtil.getArgument(DateFormatLocal.class,
                "<init>", 0, 0));
    }

    /**
     * testFormat07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:not null<br>
     * Date型フィールドを持つ<br>
     * 　値：new Date(0)<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：private<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy/MM/dd"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalAccessExceptionが発生することを確認する。<br>
     * <br>
     * ファイル行オブジェクトのDate型属性のgetterメソッドにアクセスできない場合、IllegalAccessExceptionをスローすることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFormat07() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new DateColumnFormatter();

        // 引数の設定
        DateColumnFormatter_Stub02 stub = new DateColumnFormatter_Stub02();
        ReflectionTestUtils.setField(stub, "date", new Date(0));
        Method method = stub.getClass().getDeclaredMethod("getDate");
        String columnFormat = "yyyy/MM/dd";

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
     * testFormat08() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:not null<br>
     * Date型フィールドを持つ<br>
     * 　値：例外をスローする<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy/MM/dd"<br>
     * <br>
     * 期待値：(状態変化) 例外:InvocationTargetExceptionが発生することを確認する。<br>
     * <br>
     * ファイル行オブジェクトのDate型属性のgetterメソッドが例外をスローする場合、getterメソッドがスローした例外をラップするInvocationTargetExceptionをスローすることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFormat08() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new DateColumnFormatter();

        // 引数の設定
        DateColumnFormatter_Stub03 stub = new DateColumnFormatter_Stub03();
        ReflectionTestUtils.setField(stub, "date", new Date(0));
        Method method = stub.getClass().getMethod("getDate");
        String columnFormat = "yyyy/MM/dd";

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
     * testFormat09() <br>
     * <br>
     * (異常系) <br>
     * <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:not null<br>
     * Date型フィールドを持つ<br>
     * 　値：new Date(0)<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：複数<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy/MM/dd"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentExceptionが発生することを確認する。<br>
     * <br>
     * ファイル行オブジェクトのDate型属性のgetterメソッドが多数の引数を持つ場合、IllegalArgumentExceptionをスローすることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFormat09() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new DateColumnFormatter();

        // 引数の設定
        DateColumnFormatter_Stub04 stub = new DateColumnFormatter_Stub04();
        ReflectionTestUtils.setField(stub, "date", new Date(0));
        Method method = stub.getClass().getMethod("getDate",
                new Class[] { Date.class });
        String columnFormat = "yyyy/MM/dd";

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
     * testFormat10() <br>
     * <br>
     * (正常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:not null<br>
     * Date型フィールドを持つ<br>
     * 　値：new Date(0)<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"AAA"<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentExceptionが発生することを確認する。<br>
     * <br>
     * columnFormatに正しくない日付パターンを入れた場合は、IllegalArgumentExceptionがスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFormat10() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new DateColumnFormatter();

        // 引数の設定
        DateColumnFormatter_Stub01 stub = new DateColumnFormatter_Stub01();
        ReflectionTestUtils.setField(stub, "date", new Date(0));
        Method method = stub.getClass().getMethod("getDate");
        String columnFormat = "AAA";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        ReflectionTestUtils.setField(columnFormatter, "map", map);

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
     * testFormat11() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:not null<br>
     * Date型フィールドを持つ<br>
     * 　値：null<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy/MM/dd"<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(戻り値) String:空文字<br>
     * (状態変化) Map.get():メソッドが呼ばれない。<br>
     * (状態変化) Map.put():メソッドが呼ばれない。<br>
     * (状態変化) DateFormatLocalコンストラクタ:メソッドが呼ばれない。<br>
     * <br>
     * 引数tのDate型フィールドにnullが設定されていた場合は、空文字が返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFormat11() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new DateColumnFormatter();

        // 引数の設定
        DateColumnFormatter_Stub01 stub = new DateColumnFormatter_Stub01();
        Method method = stub.getClass().getMethod("getDate");
        String columnFormat = "yyyy/MM/dd";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        ReflectionTestUtils.setField(columnFormatter, "map", map);

        // テスト実施
        String testResult = columnFormatter.format(stub, method, columnFormat);

        // 返却値の確認
        assertEquals("", testResult);

        // 状態変化の確認
        assertFalse(VMOUTUtil.isCalled(Map.class, "get"));
        assertFalse(VMOUTUtil.isCalled(Map.class, "put"));
        assertFalse(VMOUTUtil.isCalled(DateFormatLocal.class, "<init>"));
    }

    /**
     * testFormat12() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ファイル行オブジェクト<br>
     * ｔ:null<br>
     * (引数) ファイル行オブジェクト(t)にあるDate型属性のgetterメソッド<br>
     * method:以下の設定をもつMethodインスタンス<br>
     * 　可視性：public<br>
     * 　引数：なし<br>
     * (引数) フォーマット用の文字列<br>
     * columnFormat:"yyyy/MM/dd"<br>
     * (状態) map:要素なし<br>
     * <br>
     * 期待値：(状態変化) 例外:NullPointerExceptionが発生することを確認する。<br>
     * <br>
     * 引数tがnullだった場合は、NullPointerExceptionがスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFormat12() throws Exception {
        // テスト対象のインスタンス化
        ColumnFormatter columnFormatter = new DateColumnFormatter();

        // 引数の設定
        DateColumnFormatter_Stub01 stub = new DateColumnFormatter_Stub01();
        ReflectionTestUtils.setField(stub, "date", new Date(0));
        Method method = stub.getClass().getMethod("getDate");
        String columnFormat = "yyyy/MM/dd";

        // 前提条件の設定
        Map<String, DateFormatLocal> map = new ConcurrentHashMap<String, DateFormatLocal>();
        ReflectionTestUtils.setField(columnFormatter, "map", map);

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

}
