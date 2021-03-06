package jp.terasoluna.fw.file.dao.standard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;

import jp.terasoluna.fw.file.dao.standard.ColumnParser;

/**
 * ColumnParser実装クラス
 * <p>
 * {@link #parse(String, Object, Method, String)}は、必ず IllegalArgumentExceptionをスローする。
 */
public class AbstractFileLineIterator_ColumnParserStub01 implements
                                                        ColumnParser {

    public void parse(String column, Object t, Method method,
            String columnFormat) throws IllegalArgumentException,
                                IllegalAccessException,
                                InvocationTargetException, ParseException {
        throw new IllegalArgumentException("ColumnParserでのエラーです");
    }

}
