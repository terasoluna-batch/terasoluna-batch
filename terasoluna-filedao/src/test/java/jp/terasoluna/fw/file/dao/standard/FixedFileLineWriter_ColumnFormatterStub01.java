package jp.terasoluna.fw.file.dao.standard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jp.terasoluna.fw.file.dao.standard.ColumnFormatter;

/**
 * <code>ColumnFormatter</code>をテストするためのスタブクラス
 * <p>
 * 空実装
 */
public class FixedFileLineWriter_ColumnFormatterStub01 implements
                                                      ColumnFormatter {

    public String format(Object t, Method method, String string)
                                                                throws IllegalArgumentException,
                                                                IllegalAccessException,
                                                                InvocationTargetException {
        return "aaaaa" + String.valueOf('\"');
    }

}
