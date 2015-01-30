package jp.terasoluna.fw.file.dao.standard;

import java.util.Map;

import jp.terasoluna.fw.file.dao.standard.AbstractFileLineIterator;
import jp.terasoluna.fw.file.dao.standard.ColumnParser;

/**
 * AbstractFileLineIteratorの実装クラス。
 * <p>
 * 以下の実装がされている。<br>
 * <ul>
 * <li>{@link #getEncloseChar()}が'\"'を返す
 * <li>{@link #getDelimiter()}が','を返す
 * </ul>
 * @param <T> ファイル行オブジェクト。
 */
public class AbstractFileLineIteratorImpl03<T> extends
                                               AbstractFileLineIterator<T> {

    public AbstractFileLineIteratorImpl03(String string, Class<T> clazz,
            Map<String, ColumnParser> textSetterMap) {
        super(string, clazz, textSetterMap);
    }

    @Override
    protected char getDelimiter() {
        return ',';
    }

    @Override
    protected char getEncloseChar() {
        return '\"';
    }

    @Override
    protected String[] separateColumns(String fileLineString) {
        return null;
    }

}
