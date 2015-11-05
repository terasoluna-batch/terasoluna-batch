/*
 * $Id: PrivateAccessUtil.java 5230 2007-09-28 10:04:13Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */
package jp.terasoluna.fw.file.ut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ◎ privateメソッドを呼び出すためのユーティリティクラス。
 * @version 2003.08.29
 * @author 丹羽 隆
 */
class PrivateAccessUtil {

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
     *         Integer result = (Integer) PrivateAccessUtil.invokePrivate(
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

        // パラメータ値のチェック
        if (target == null) {
            throw new IllegalArgumentException();
        }
        if (methodName == null || methodName.equals("")) {
            throw new IllegalArgumentException();
        }
        if (argTypes.length != args.length) {
            throw new IllegalArgumentException();
        }

        // privateメソッド呼び出し処理。
        // スーパークラス全てについて呼び出しをトライする。
        Class<?> c = target.getClass();
        while (c != null) {
            try {
                Method method = c.getDeclaredMethod(methodName, argTypes);
                method.setAccessible(true);
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                // 呼び出したメソッドが例外を投げた場合。
                throw (Exception) e.getTargetException();
            } catch (Exception e) {
                // 呼び出そうとしたメソッドが存在しなかった場合、何もしない。
                // (親クラスで同じトライを繰り返す。)
            }
            c = c.getSuperclass();
        }
        // 呼び出そうとしたメソッドが存在しなかった場合。
        throw new NoSuchMethodException("Could not invoke " + target.getClass()
                .getName() + "." + methodName + "()");
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
     *            (String) PrivateAccessUtil.invokePrivate(sample, &quot;getString&quot;):
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
     *         Long result = (Long) PrivateAccessUtil.invokePrivate(
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
     *         Integer result = (Integer) PrivateAccessUtil.invokePrivate(
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
     *         Integer result = (Integer) PrivateAccessUtil.invokePrivate(
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

        // パラメータ値のチェック
        if (target == null) {
            throw new IllegalArgumentException();
        }
        if (methodName == null || methodName.equals("")) {
            throw new IllegalArgumentException();
        }
        if (argTypes.length != args.length) {
            throw new IllegalArgumentException();
        }

        // privateメソッド呼び出し処理。
        // スーパークラス全てについて呼び出しをトライする。
        Class<?> c = target;
        while (c != null) {
            try {
                Method method = c.getDeclaredMethod(methodName, argTypes);
                method.setAccessible(true);
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                // 呼び出したメソッドが例外を投げた場合。
                throw (Exception) e.getTargetException();
            } catch (Exception e) {
                // 呼び出そうとしたメソッドが存在しなかった場合、何もしない。
                // (親クラスで同じトライを繰り返す。)
            }
            c = c.getSuperclass();
        }
        // 呼び出そうとしたメソッドが存在しなかった場合。
        throw new NoSuchMethodException("Could not invoke " + target.getClass()
                .getName() + "." + methodName + "()");
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
     *         String result = (String) PrivateAccessUtil.invokePrivate(
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
     *         Long result = (Long) PrivateAccessUtil.invokePrivate(
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
     *         Integer result = (Integer) PrivateAccessUtil.invokePrivate(
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
}
