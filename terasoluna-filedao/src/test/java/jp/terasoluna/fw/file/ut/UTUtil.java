/*
 * $Id: UTUtil.java 5230 2007-09-28 10:04:13Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */
package jp.terasoluna.fw.file.ut;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * ◎ JUnitでの単体テストをサポートするユーティリティクラス。
 * @version 2003.08.29
 * @author 丹羽 隆
 */
public class UTUtil {

    /**
     * 日付のフォーマット文字列
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日付を比較するためのassertユーティリティメソッド。 <BR>
     * <BR>
     * selectRows()はDATE型の値を"yyyy-MM-dd HH:mm:ss.fffffffff"フォーマットで 返却するようになっているため、そのままでは値の比較がしづらい。<BR>
     * そのため、日付用のassertユーティリティメソッドを用意している。 <BR>
     * <BR>
     * 
     * <pre>
     * [使用例]
     * List list = UTUtil.selectRows(&quot;CUSTOMER&quot;, &quot;C_ID=1&quot;);
     * Map map = (Map) list.get(0);
     * UTUtil.assertEqualsDate(&quot;2003-01-01&quot;, map.get(&quot;C_REGIST_DATE&quot;));
     * </pre>
     * 
     * @param expected 期待値
     * @param actual 実際の値
     */
    public static void assertEqualsDate(Object expected, Object actual) {

        // パラメータがString型でなければ例外を返す。
        if ((expected != null) && (!(expected instanceof String))) {
            throw new IllegalArgumentException();
        }
        if ((actual != null) && (!(actual instanceof String))) {
            throw new IllegalArgumentException();
        }

        // "yyyy-MM-dd HH:mm:ss.fffffffff"の"yyyy-MM-dd"だけ取り出す。
        String actualDate = (actual != null) ? ((String) actual).substring(0,
                10) : null;

        // Stringにキャストした上でassertEqualsする。
        String expectedDate = (String) expected;
        assertEquals(expectedDate, actualDate);
    }

    /**
     * 時刻を比較するためのassertユーティリティメソッド。 <BR>
     * <BR>
     * selectRows()はDATE型の値を"yyyy-MM-dd HH:mm:ss.fffffffff"フォーマットで 返却するようになっているため、そのままでは値の比較がしづらい。<BR>
     * そのため、時刻用のassertユーティリティメソッドを用意している。 <BR>
     * <BR>
     * 
     * <pre>
     * [使用例]
     * List list = UTUtil.selectRows(&quot;CUSTOMER&quot;, &quot;C_ID=1&quot;);
     * Map map = (Map) list.get(0);
     * UTUtil.assertEqualsDate(&quot;12:34:56&quot;, map.get(&quot;C_UPDATE_TIME&quot;));
     * </pre>
     * 
     * @param expected 期待値
     * @param actual 実際の値
     */
    public static void assertEqualsTime(Object expected, Object actual) {

        // パラメータがString型でなければ例外を返す。
        if ((expected != null) && (!(expected instanceof String))) {
            throw new IllegalArgumentException();
        }
        if ((actual != null) && (!(actual instanceof String))) {
            throw new IllegalArgumentException();
        }

        // "yyyy-MM-dd HH:mm:ss.fffffffff"の"HH:mm:ss"だけ取り出す。
        String actualTime = (actual != null) ? ((String) actual).substring(11,
                19) : null;

        // Stringにキャストした上でassertEqualsする。
        String expectedTime = (String) expected;
        assertEquals(expectedTime, actualTime);
    }

    /**
     * 日付＋時刻を比較するためのassertユーティリティメソッド。 <BR>
     * <BR>
     * selectRows()はDATE型の値を"yyyy-MM-dd HH:mm:ss.fffffffff"フォーマットで 返却するようになっているため、そのままでは値の比較がしづらい。<BR>
     * そのため、日付＋時刻用のassertユーティリティメソッドを用意している。 <BR>
     * <BR>
     * 
     * <pre>
     * [使用例]
     * List list = UTUtil.selectRows(&quot;CUSTOMER&quot;, &quot;C_ID=1&quot;);
     * Map map = (Map) list.get(0);
     * UTUtil.assertEqualsDateTime(&quot;2003-01-01 12:00:05&quot;,
     *     map.get(&quot;C_UPDATE_DATE&quot;));
     * </pre>
     * 
     * @param expected 期待値
     * @param actual 実際の値
     */
    public static void assertEqualsDateTime(Object expected, Object actual) {

        // パラメータがString型でなければ例外を返す。
        if ((expected != null) && (!(expected instanceof String))) {
            throw new IllegalArgumentException();
        }
        if ((actual != null) && (!(actual instanceof String))) {
            throw new IllegalArgumentException();
        }

        // "yyyy-MM-dd HH:mm:ss.fffffffff"の"yyyy-MM-dd HH:mm:ss"だけ取り出す。
        String actualDateTime = (actual != null) ? ((String) actual).substring(
                0, 19) : null;

        // Stringにキャストした上でassertEqualsする。
        String expectedDateTime = (String) expected;
        assertEquals(expectedDateTime, actualDateTime);
    }

    /**
     * 現在日付と比較するためのassertユーティリティメソッド。 <BR>
     * <BR>
     * selectRows()はDATE型の値を"yyyy-MM-dd HH:mm:ss.fffffffff"フォーマットで 返却するようになっているため、そのままでは値の比較がしづらい。<BR>
     * そのため、現在日付と比較するassertユーティリティメソッドを用意している。
     * 
     * <pre>
     * [使用例]
     * List list = UTUtil.selectRows(&quot;CUSTOMER&quot;, &quot;C_ID=1&quot;);
     * Map map = (Map) list.get(0);
     * UTUtil.assertEqualsToday(map.get(&quot;C_UPDATE_DATE&quot;));
     * </pre>
     * 
     * @param actual 実際の値
     */
    public static void assertEqualsToday(Object actual) {

        // パラメータがString型でなければ例外を返す。
        if ((actual != null) && (!(actual instanceof String))) {
            throw new IllegalArgumentException();
        }

        // 現在日付の文字列を取得する。
        Date d = new Date();
        SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT);
        String currentDate = f.format(d);

        // 日付の文字列をassertEqualsする。
        assertEqualsDate(currentDate, actual);
    }

    /**
     * ファイルの中身をバイナリ比較する。
     * 
     * <pre>
     * [使用例]
     * class SampleTest extends TestCase {
     *     &#064;SuppressWarnings(&quot;unchecked&quot;) public void testDoSomething() throws Excepton {
     *         // なんらかの処理を実行し結果のファイルが戻される。
     *         File actual = Sample.doSomething();
     * 
     *         // 比較するための期待値データのファイルを取得する。
     *         // ファイルはSampleTestクラスと同じフォルダに置いてある。
     *         File expected = UTUtil.getFile(this, &quot;expected.txt&quot;);
     * 
     *         // ファイルの比較をする。
     *         UTUtil.assertEqualsFile(expected, actual);
     *          
     *     }
     * }
     * </pre>
     * 
     * @param expected 期待値のファイル
     * @param actual 実際の値のファイル
     */
    public static void assertEqualsFile(File expected, File actual) {

        // パラメータがnullの場合は例外を返す。
        if ((expected == null) || (actual == null)) {
            throw new IllegalArgumentException();
        }

        // 中身を比較するためのラッパーオブジェクト(FileContent)を生成
        FileContent expectedContent = new FileContent(expected);
        FileContent actualContent = new FileContent(actual);

        // assertにかける。
        assertTrue(expectedContent.equals(actualContent));
    }

    /**
     * テストクラスと同じフォルダに置いてあるテスト用ファイルの Fileオブジェクトを取得する。
     * 
     * <pre>
     * [使用例]
     * class SampleTest extends TestCase {
     *     &#064;SuppressWarnings(&quot;unchecked&quot;) public void testDoSomething() throws Excepton {
     *         // なんらかの処理を実行し結果のファイルが戻される。
     *         File actual = Sample.doSomething();
     * 
     *         // 比較するための期待値データのファイルを取得する。
     *         // ファイルはSampleTestクラスと同じフォルダに置いてある。
     *         File expected = UTUtil.getFile(SampleTest.class, &quot;expected.txt&quot;);
     * 
     *         // ファイルの比較をする。
     *         UTUtil.assertEqualsFile(expected, actual);
     *          
     *     }
     * }
     * </pre>
     * 
     * @param cls テストクラスのClassオブジェクト
     * @param filename ファイル名
     * @return 指定されたファイル名のFileオブジェクト。 ファイルが存在しない場合はnullを返す。
     */
    public static File getFile(Class<?> cls, String filename) {

        // ファイル名がヌル、空文字列の場合はヌルを返す。
        if ((filename == null) || ("".equals(filename))) {
            return null;
        }

        // 指定されたファイル名のURLを取得する。
        // "file://c:/folder/filename"のようなURLが得られる。
        URL url = cls.getResource(filename);

        // URLがヌルならばヌルを返す。
        if (url == null) {
            return null;
        }

        // URLからURIを生成する。
        // URISyntaxExceptionが発生することは事実上あり得ないので、catch以下の
        // コードはヌルを返すだけにしている。
        URI uri = null;
        try {
            uri = new URI(url.toString());
        } catch (URISyntaxException e) {
            return null;
        }

        // Fileオブジェクトを生成して返す。
        return new File(uri);
    }

    /**
     * テストクラスと同じフォルダに置いてあるテスト用ファイルの Fileオブジェクトを取得する。
     * 
     * <pre>
     * [使用例]
     * class SampleTest extends TestCase {
     *     &#064;SuppressWarnings(&quot;unchecked&quot;) public void testDoSomething() throws Excepton {
     *         // なんらかの処理を実行し結果のファイルが戻される。
     *         File actual = Sample.doSomething();
     * 
     *         // 比較するための期待値データのファイルを取得する。
     *         // ファイルはSampleTestクラスと同じフォルダに置いてある。
     *         File expected = UTUtil.getFile(this, &quot;expected.txt&quot;);
     * 
     *         // ファイルの比較をする。
     *         UTUtil.assertEqualsFile(expected, actual);
     *          
     *     }
     * }
     * </pre>
     * 
     * @param instance テストクラスのインスタンス
     * @param filename ファイル名
     * @return 指定されたファイル名のFileオブジェクト。 ファイルが存在しない場合はnullを返す。
     */
    public static File getFile(Object instance, String filename) {
        return getFile(instance.getClass(), filename);
    }

    /**
     * privateメソッド（staticでないもの）を呼び出す。<BR>
     * パラメータ0個～2個のメソッドには専用のメソッドが用意されているので、 そちらを利用した方がシンプルに記述できる。
     * 
     * <pre>
     * [使用例]
     * class Sample {
     *     private int calcAdd(int val1, int val2, int val3) {
     *         return val1 + val2 + val3;
     *     }
     * }
     * 
     * class SampleTest {
     *     &#064;SuppressWarnings(&quot;unchecked&quot;) public void testCalcAdd() {
     *         Sample sample = new Sample();
     *         Integer result = (Integer) UTUtil.invokePrivate(
     *             sample,
     *             &quot;calcAdd&quot;,
     *             new Class[] { int.class, int.class, int.class },
     *             new Object[] { new Integer(1), new Integer(2), 
     *                            new Integer(3) }
     *         );
     *         assertEquals(6, result.intValue());
     *     }
     * }
     * </pre>
     * 
     * @param target 呼び出す対象のオブジェクト
     * @param methodName 呼び出したいメソッドの名前
     * @param argTypes 引数の型の配列
     * @param args 引数の値の配列。 int,boolean等の基本データ型は、Integer, Boolean等のラッパークラスに 格納して値を渡す必要あり。
     * @return メソッドの戻り値。呼び出し側でダウンキャストが必要。 int, boolean等の基本データ型は、Integer, Boolean等の ラッパークラスに格納されて値が戻される。
     * @throws Exception
     */
    public static Object invokePrivate(Object target, String methodName,
            @SuppressWarnings("rawtypes") Class[] argTypes,
            Object[] args) throws Exception {

        return PrivateAccessUtil.invokePrivate(target, methodName, argTypes,
                args);
    }

    /**
     * privateメソッド（staticでないもの）を呼び出す（パラメータ0個用）。
     * 
     * <pre>
     * [使用例]
     * class Sample {
     *     private int getString() {
     *         return &quot;success&quot;;
     *     }
     * }
     * 
     * class SampleTest {
     *     &#064;SuppressWarnings(&quot;unchecked&quot;) public void testGetString() {
     *         Sample sample = new Sample();
     *         String result = 
     *             (String) UTUtil.invokePrivate(sample, &quot;getString&quot;):
     *         assertEquals(&quot;success&quot;, result);
     *     }
     * }
     * </pre>
     * 
     * @param target 呼び出す対象のオブジェクト
     * @param methodName 呼び出したいメソッドの名前
     * @return メソッドの戻り値。呼び出し側でダウンキャストが必要。 int, boolean等の基本データ型は、Integer, Boolean等の ラッパークラスに格納されて値が戻される。
     * @throws Exception
     */
    public static Object invokePrivate(Object target,
            String methodName) throws Exception {

        return invokePrivate(target, methodName, new Class[] {},
                new Object[] {});
    }

    /**
     * privateメソッド（staticでないもの）を呼び出す（パラメータ1個用）。
     * 
     * <pre>
     * [使用例]
     * class Sample {
     *     private long square(long val) {
     *         return val &circ; 2;
     *     }
     * }
     * 
     * class SampleTest {
     *     &#064;SuppressWarnings(&quot;unchecked&quot;) public void testSquare() {
     *         Sample sample = new Sample();
     *         Long result = (Long) UTUtil.invokePrivate(
     *             sample, &quot;square&quot;, long.class, new Long(2L)):
     *         assertEquals(4, result.longValue());
     *     }
     * }
     * </pre>
     * 
     * @param target 呼び出す対象のオブジェクト
     * @param methodName 呼び出したいメソッドの名前
     * @param argType 引数の型
     * @param arg 引数の値。 int,boolean等の基本データ型は、Integer, Boolean等のラッパークラスに 格納して値を渡す必要あり。
     * @return メソッドの戻り値。呼び出し側でダウンキャストが必要。 int, boolean等の基本データ型は、Integer, Boolean等の ラッパークラスに格納されて値が戻される。
     * @throws Exception
     */
    public static Object invokePrivate(Object target, String methodName,
            Class<?> argType, Object arg) throws Exception {

        return invokePrivate(target, methodName, new Class[] { argType },
                new Object[] { arg });
    }

    /**
     * privateメソッド（staticでないもの）を呼び出す（パラメータ2個用）。
     * 
     * <pre>
     * [使用例]
     * class Sample {
     *     private static int calcAdd(int val1, int val2) {
     *         return val1 + val2;
     *     }
     * }
     * 
     * class SampleTest {
     *     &#064;SuppressWarnings(&quot;unchecked&quot;) public void testCalcAdd() {
     *         Sample sample = new Sample();
     *         Integer result = (Integer) UTUtil.invokePrivate(
     *             sample,
     *             &quot;calcAdd&quot;,
     *             int.class,
     *             int.class,
     *             new Integer(1),
     *             new Integer(2)
     *         );
     *         assertEquals(3, result.intValue());
     *     }
     * }
     * </pre>
     * 
     * @param target 呼び出す対象のオブジェクト
     * @param methodName 呼び出したいメソッドの名前
     * @param argType1 第一引数の型
     * @param argType2 第二引数の型
     * @param arg1 第一引数の値。 int,boolean等の基本データ型は、Integer, Boolean等のラッパークラスに 格納して値を渡す必要あり。
     * @param arg2 第二引数の値。
     * @return メソッドの戻り値。呼び出し側でダウンキャストが必要。 int, boolean等の基本データ型は、Integer, Boolean等の ラッパークラスに格納されて値が戻される。
     * @throws Exception
     */
    public static Object invokePrivate(Object target, String methodName,
            Class<?> argType1, Class<?> argType2, Object arg1,
            Object arg2) throws Exception {

        return invokePrivate(target, methodName, new Class[] { argType1,
                argType2 }, new Object[] { arg1, arg2 });
    }

    /**
     * staticなprivateメソッドを呼び出す。 <BR>
     * <BR>
     * パラメータ0個～2個のメソッドには専用のメソッドが用意されているので、 そちらを利用した方がシンプルに記述できる。
     * 
     * <pre>
     * [使用例]
     * class Sample {
     *     private static int calcAdd(int val1, int val2, int val3) {
     *         return val1 + val2 + val3;
     *     }
     * }
     * 
     * class SampleTest {
     *     &#064;SuppressWarnings(&quot;unchecked&quot;) public void testCalcAdd() {
     *         Integer result = (Integer) UTUtil.invokePrivate(
     *             Sample.class,
     *             &quot;calcAdd&quot;,
     *             new Class[] { int.class, int.class, int.class },
     *             new Object[] { new Integer(1), new Integer(2), 
     *                            new Integer(3) }
     *         );
     *         assertEquals(6, result.intValue());
     *     }
     * }
     * </pre>
     * 
     * @param target 呼び出す対象のクラス
     * @param methodName 呼び出したいメソッドの名前
     * @param argTypes 引数の型の配列
     * @param args 引数の値の配列。 int,boolean等の基本データ型は、Integer, Boolean等のラッパークラスに 格納して値を渡す必要あり。
     * @return メソッドの戻り値。呼び出し側でダウンキャストが必要。 int, boolean等の基本データ型は、Integer, Boolean等の ラッパークラスに格納されて値が戻される。
     * @throws Exception
     */
    public static Object invokePrivate(Class<?> target, String methodName,
            @SuppressWarnings("rawtypes") Class[] argTypes,
            Object[] args) throws Exception {

        return PrivateAccessUtil.invokePrivate(target, methodName, argTypes,
                args);
    }

    /**
     * staticなprivateメソッドを呼び出す（パラメータ0個用）。
     * 
     * <pre>
     * [使用例]
     * class Sample {
     *     private static int getString() {
     *         return &quot;success&quot;;
     *     }
     * }
     * 
     * class SampleTest {
     *     &#064;SuppressWarnings(&quot;unchecked&quot;) public void testGetString() {
     *         String result = (String) UTUtil.invokePrivate(
     *             Sample.class, &quot;getString&quot;):
     *         assertEquals(&quot;success&quot;, result);
     *     }
     * }
     * </pre>
     * 
     * @param target 呼び出す対象のクラス
     * @param methodName 呼び出したいメソッドの名前
     * @return メソッドの戻り値。呼び出し側でダウンキャストが必要。 int, boolean等の基本データ型は、Integer, Boolean等の ラッパークラスに格納されて値が戻される。
     * @throws Exception
     */
    public static Object invokePrivate(Class<?> target,
            String methodName) throws Exception {

        return invokePrivate(target, methodName, new Class[] {},
                new Object[] {});
    }

    /**
     * staticなprivateメソッドを呼び出す（パラメータ1個用）。
     * 
     * <pre>
     * [使用例]
     * class Sample {
     *     private static long square(long val) {
     *         return val &circ; 2;
     *     }
     * }
     * 
     * class SampleTest {
     *     &#064;SuppressWarnings(&quot;unchecked&quot;) public void testSquare() {
     *         Long result = (Long) UTUtil.invokePrivate(
     *             Sample.class, &quot;square&quot;, long.class, new Long(2L)):
     *         assertEquals(4, result.longValue());
     *     }
     * }
     * </pre>
     * 
     * @param target 呼び出す対象のクラス
     * @param methodName 呼び出したいメソッドの名前
     * @param argType 引数の型
     * @param arg 引数の値。 int,boolean等の基本データ型は、Integer, Boolean等のラッパークラスに 格納して値を渡す必要あり。
     * @return メソッドの戻り値。呼び出し側でダウンキャストが必要。 int, boolean等の基本データ型は、Integer, Boolean等の ラッパークラスに格納されて値が戻される。
     * @throws Exception
     */
    public static Object invokePrivate(Class<?> target, String methodName,
            Class<?> argType, Object arg) throws Exception {

        return invokePrivate(target, methodName, new Class[] { argType },
                new Object[] { arg });
    }

    /**
     * staticなprivateメソッドを呼び出す（パラメータ2個用）。
     * 
     * <pre>
     * [使用例]
     * class Sample {
     *     private static int calcAdd(int val1, int val2) {
     *         return val1 + val2;
     *     }
     * }
     * 
     * class SampleTest {
     *     &#064;SuppressWarnings(&quot;unchecked&quot;) public void testCalcAdd() {
     *         Integer result = (Integer) UTUtil.invokePrivate(
     *             Sample.class,
     *             &quot;calcAdd&quot;,
     *             int.class,
     *             int.class,
     *             new Integer(1),
     *             new Integer(2)
     *         );
     *         assertEquals(3, result.intValue());
     *     }
     * }
     * </pre>
     * 
     * @param target 呼び出す対象のクラス
     * @param methodName 呼び出したいメソッドの名前
     * @param argType1 第一引数の型
     * @param argType2 第二引数の型
     * @param arg1 第一引数の値。 int,boolean等の基本データ型は、Integer, Boolean等のラッパークラスに 格納して値を渡す必要あり。
     * @param arg2 第二引数の値。
     * @return メソッドの戻り値。呼び出し側でダウンキャストが必要。 int, boolean等の基本データ型は、Integer, Boolean等の ラッパークラスに格納されて値が戻される。
     * @throws Exception
     */
    public static Object invokePrivate(Class<?> target, String methodName,
            Class<?> argType1, Class<?> argType2, Object arg1,
            Object arg2) throws Exception {

        return invokePrivate(target, methodName, new Class[] { argType1,
                argType2 }, new Object[] { arg1, arg2 });
    }

    /**
     * 指定したオブジェクトのprivateフィールドの値を返す。
     * @param target 対象のオブジェクト
     * @param fieldName 値を取得するprivateフィールドの名前
     * @return privateフィールドの値
     * @exception NoSuchFieldException
     */
    public static Object getField(Object target,
            String fieldName) throws NoSuchFieldException {

        // パラメータ値のチェック
        if (target == null) {
            throw new IllegalArgumentException();
        }
        if (fieldName == null || fieldName.equals("")) {
            throw new IllegalArgumentException();
        }

        // privateフィールド取得処理。
        // スーパークラス全てについて呼び出しをトライする。
        for (Class<?> c = target.getClass(); c != null; c = c.getSuperclass()) {
            try {
                Field field = c.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(target);
            } catch (Exception e) {
                // 取得しようとしたフィールドが存在しなかった場合、何もしない。
                // (親クラスで同じトライを繰り返す)
            }
        }
        // 取得しようとしたフィールドが存在しなかった場合
        throw new NoSuchFieldException("Could get value for field " + target
                .getClass().getName() + "." + fieldName);
    }

    /**
     * 指定したクラスのstaticなprivateフィールドの値を返す。
     * @param target 対象のクラス
     * @param fieldName 値を取得するprivateフィールドの名前
     * @return privateフィールドの値
     * @exception NoSuchFieldException
     */
    public static Object getField(Class<?> target,
            String fieldName) throws NoSuchFieldException {

        // パラメータ値のチェック
        if (target == null) {
            throw new IllegalArgumentException();
        }
        if (fieldName == null || fieldName.equals("")) {
            throw new IllegalArgumentException();
        }

        // privateフィールド取得処理。
        // スーパークラス全てについて呼び出しをトライする。
        Class<?> c = target;
        while (c != null) {
            try {
                Field field = c.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(c);
            } catch (Exception e) {
                // 取得しようとしたフィールドが存在しなかった場合、何もしない。
                // (親クラスで同じトライを繰り返す)
            }
            c = c.getSuperclass();
        }
        // 取得しようとしたフィールドが存在しなかった場合
        throw new NoSuchFieldException("Could get value for field " + target
                .getName() + "." + fieldName);
    }

    /**
     * 指定したオブジェクトのprivateフィールドに値を設定する。
     * @param target 対象のオブジェクト
     * @param fieldName 値を設定するprivateフィールドの名前
     * @param value 設定する値。 int,boolean等の基本データ型は、Integer, Boolean等のラッパークラスに 格納して値を渡す必要あり。
     * @exception NoSuchFieldException
     */
    public static void setField(Object target, String fieldName,
            Object value) throws NoSuchFieldException {

        // パラメータ値のチェック
        if (target == null) {
            throw new IllegalArgumentException();
        }
        if (fieldName == null || fieldName.equals("")) {
            throw new IllegalArgumentException();
        }

        // privateフィールド設定処理。
        // スーパークラス全てについて呼び出しをトライする。
        for (Class<?> c = target.getClass(); c != null; c = c.getSuperclass()) {
            try {
                Field field = c.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (Exception e) {
                // 設定しようとしたフィールドが存在しなかった場合、何もしない。
                // (親クラスで同じトライを繰り返す)
            }
        }
        // 設定しようとしたフィールドが存在しなかった場合
        throw new NoSuchFieldException("Could set value for field " + target
                .getClass().getName() + "." + fieldName);
    }

    /**
     * 指定したクラスのstaticなprivateフィールドに値を設定する。
     * @param target 対象のクラス
     * @param fieldName 値を設定するprivateフィールドの名前
     * @param value 設定する値。 int,boolean等の基本データ型は、Integer, Boolean等のラッパークラスに 格納して値を渡す必要あり。
     * @exception NoSuchFieldException
     */
    public static void setField(Class<?> target, String fieldName,
            Object value) throws NoSuchFieldException {

        // パラメータ値のチェック
        if (target == null) {
            throw new IllegalArgumentException();
        }
        if (fieldName == null || fieldName.equals("")) {
            throw new IllegalArgumentException();
        }

        Class<?> c = target;
        while (c != null) {
            try {
                Field field = c.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(c, value);
                return;
            } catch (Exception ex) {
                // 設定しようとしたフィールドが存在しなかった場合、何もしない。
                // (親クラスで同じトライを繰り返す)
            }
            c = c.getSuperclass();
        }
        // 設定しようとしたフィールドが存在しなかった場合
        throw new NoSuchFieldException("Could set value for static field "
                + target.getName() + "." + fieldName);
    }
}
