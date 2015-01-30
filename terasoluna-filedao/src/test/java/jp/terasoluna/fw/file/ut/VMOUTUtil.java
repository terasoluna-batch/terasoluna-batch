/*
 * $Id: VMOUTUtil.java 5230 2007-09-28 10:04:13Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */
package jp.terasoluna.fw.file.ut;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import jp.co.dgic.testing.common.virtualmock.InternalMockObjectManager;
import jp.co.dgic.testing.common.virtualmock.MockObjectManager;

/**
 * djUnitのVirtual Mock Object機能を利用するためのユーティリティクラス。 JUnitのテストケースから利用する場合は、setUpメソッドにおいて {@link VMOUTUtil#initialize()}
 * メソッドを呼び出す必要がある。
 * @author 池田　貴之
 * @see jp.co.dgic.testing.common.virtualmock.MockObjectManager
 */
public class VMOUTUtil {

    /**
     * VMOの初期化を行う。 <code>JUnit</code>のテストケースから利用する場合は、 <code>setUp</code>メソッド内部で必ずこのメソッドを呼び出さなければならない。
     */
    public static void initialize() {
        MockObjectManager.initialize();
    }

    /**
     * 指定したメソッドの<code>index</code>回目の呼び出しをキャンセルし、 指定したオブジェクトを返却する。
     * @param cls 書き換えたいクラス。
     * @param methodname 書き換えたいメソッド名。
     * @param index 何回目の呼び出しを書き換えたいか。オフセットはゼロ。
     * @param returnValue 返却したいオブジェクト。プリミティブ型を返す場合はラッパーオブジェクトを指定する。 <code>null</code>も指定可能。
     */
    @SuppressWarnings("unchecked")
    public static void setReturnValueAt(Class cls, String methodname,
            int index, Object returnValue) {
        if (returnValue == null) {
            MockObjectManager.setReturnNullAt(cls.getName(), methodname, index);
        } else {
            MockObjectManager.setReturnValueAt(cls.getName(), methodname,
                    index, returnValue);
        }
    }

    /**
     * 指定したメソッドの毎回の呼び出しをキャンセルし、指定したオブジェクトを返却する。
     * @param cls 書き換えたいクラス。
     * @param methodname 書き換えたいメソッド名。
     * @param returnValue 返却したいオブジェクト。プリミティブ型を返す場合はラッパーオブジェクトを指定する。 <code>null</code>も指定可能。
     */
    @SuppressWarnings("unchecked")
    public static void setReturnValueAtAllTimes(Class cls, String methodname,
            Object returnValue) {
        if (returnValue == null) {
            MockObjectManager
                    .setReturnNullAtAllTimes(cls.getName(), methodname);
        } else {
            MockObjectManager.setReturnValueAtAllTimes(cls.getName(),
                    methodname, returnValue);
        }
    }

    /**
     * 指定したメソッドの<code>index</code>回目の呼び出しをキャンセルし、指定した例外を返却する。
     * @param cls 書き換えたいクラス。
     * @param methodname 書き換えたいメソッド名。
     * @param index 何回目の呼び出しを書き換えたいか。オフセットはゼロ。
     * @param exception 返却したい例外。
     */
    @SuppressWarnings("unchecked")
    public static void setExceptionAt(Class cls, String methodname, int index,
            Throwable exception) {
        MockObjectManager.setReturnValueAt(cls.getName(), methodname, index,
                exception);
    }

    /**
     * 指定したメソッドの毎回の呼び出しをキャンセルし、指定した例外を返却する。
     * @param cls 書き換えたいクラス。
     * @param methodname 書き換えたいメソッド名。
     * @param exception 返却したい例外。
     */
    @SuppressWarnings("unchecked")
    public static void setExceptionAtAllTimes(Class cls, String methodname,
            Throwable exception) {
        MockObjectManager.setReturnValueAtAllTimes(cls.getName(), methodname,
                exception);
    }

    /**
     * 指定したメソッドの<code>index</code>回目の呼び出しをキャンセルする。 <code>void</code>型のメソッドのみで利用可能。
     * @param cls 書き換えたいクラス。
     * @param methodname 書き換えたいメソッド名。
     * @param index 何回目の呼び出しを書き換えたいか。オフセットはゼロ。
     */
    @SuppressWarnings("unchecked")
    public static void cancelMethodAt(Class cls, String methodname, int index) {
        MockObjectManager.setReturnValueAt(cls.getName(), methodname, index);
    }

    /**
     * 指定したメソッドの毎回の呼び出しをキャンセルする。<code>void</code>型のメソッドのみで利用可能。
     * @param cls 書き換えたいクラス。
     * @param methodname 書き換えたいメソッド名。
     */
    @SuppressWarnings("unchecked")
    public static void cancelMethodAtAllTimes(Class cls, String methodname) {
        MockObjectManager.setReturnValueAtAllTimes(cls.getName(), methodname);
    }

    /**
     * 指定したメソッドが呼び出されたかどうかを確認する。
     * @param cls 確認したいクラス。
     * @param methodname 確認したいメソッド名。
     * @return 呼び出されていればtrue。
     */
    @SuppressWarnings("unchecked")
    public static boolean isCalled(Class cls, String methodname) {
        return MockObjectManager.isCalled(cls.getName(), methodname);
    }

    /**
     * 指定したメソッドが呼び出された回数を取得する。
     * @param cls 確認したいクラス。
     * @param methodname 確認したいメソッド名。
     * @return 呼び出された回数。
     */
    @SuppressWarnings("unchecked")
    public static int getCallCount(Class cls, String methodname) {
        return MockObjectManager.getCallCount(cls.getName(), methodname);
    }

    /**
     * 指定されたメソッドの<code>methodindex</code>回目の呼び出しにおいて、 <code>argumentindex</code>個目の引数がとる値を取得する。
     * @param cls 確認したいクラス。
     * @param methodname 確認したいメソッド名。
     * @param methodindex 何回目の呼び出しを確認したいか。オフセットはゼロ。
     * @param argumentindex 何個目の引数を取得したいか。オフセットはゼロ。
     * @return 取得した引数の値。
     */
    @SuppressWarnings("unchecked")
    public static Object getArgument(Class cls, String methodname,
            int methodindex, int argumentindex) {
        return MockObjectManager.getArgument(cls.getName(), methodname,
                methodindex, argumentindex);
    }

    /**
     * 指定されたメソッドの<code>methodindex</code>回目の呼び出しのすべての引数を取得する。
     * @param cls 確認したいクラス。
     * @param methodname 確認したいメソッド名。
     * @param methodindex 何回目の呼び出しを確認したいか。オフセットはゼロ。
     * @return 取得した引数の値のList。
     */
    @SuppressWarnings("unchecked")
    public static List getArguments(Class cls, String methodname,
            int methodindex) {
        ArrayList<Object> list = new ArrayList<Object>();
        int argumentindex = 0;
        while (true) {
            try {
                list.add(getArgument(cls, methodname, methodindex,
                        argumentindex++));
            } catch (ArrayIndexOutOfBoundsException ex) {
                break;
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static Hashtable getTestDataTable() throws NoSuchFieldException {
        return (Hashtable) UTUtil.getField(InternalMockObjectManager
                .getTestData(), "valueTable");
    }

    @SuppressWarnings("unchecked")
    public static Hashtable getAllTimesTestDataTable()
                                                      throws NoSuchFieldException {
        return (Hashtable) UTUtil.getField(InternalMockObjectManager
                .getTestData(), "valueAtAllTimesTable");
    }

    @SuppressWarnings("unchecked")
    public static Hashtable getArgumentValues() throws NoSuchFieldException {
        return (Hashtable) UTUtil.getField(InternalMockObjectManager
                .getCallsMade(), "argumentValues");
    }

}
