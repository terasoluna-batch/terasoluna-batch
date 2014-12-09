package jp.terasoluna.fw.file.dao.standard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;

import jp.terasoluna.fw.file.dao.standard.ColumnParser;

/**
 * <code>ColumnParser</code>をテストするためのスタブクラス
 * <p>
 * 空実装
 */
public class FixedFileQueryDAO_ColumnParserStub01 implements ColumnParser {

    public void parse(String column, Object t, Method method,
            String columnFormat) throws IllegalArgumentException,
                                IllegalAccessException,
                                InvocationTargetException, ParseException {

    }

}
