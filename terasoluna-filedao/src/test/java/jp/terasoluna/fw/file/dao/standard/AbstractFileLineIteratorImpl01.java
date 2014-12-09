package jp.terasoluna.fw.file.dao.standard;

import java.util.Map;

import jp.terasoluna.fw.file.dao.standard.AbstractFileLineIterator;
import jp.terasoluna.fw.file.dao.standard.ColumnParser;

/**
 * AbstractFileLineIteratorの実装クラス。
 * <p>
 * AbstractFileLineIteratorのテストのために利用する。<br>
 * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す。
 * @param <T> ファイル行オブジェクト。
 */
public class AbstractFileLineIteratorImpl01<T> extends
                                               AbstractFileLineIterator<T> {

    public AbstractFileLineIteratorImpl01(String fileName, Class<T> clazz,
            Map<String, ColumnParser> columnParserMap) {
        super(fileName, clazz, columnParserMap);
        super.init();
    }

    @Override
    protected char getDelimiter() {
        return 0;
    }

    @Override
    protected char getEncloseChar() {
        return 0;
    }

    @Override
    protected String[] separateColumns(String fileLineString) {
        if (fileLineString == null || "".equals(fileLineString)) {
            return new String[0];
        }

        return fileLineString.split(",");
    }

    @Override
    protected boolean isCheckColumnAnnotationCount() {
        return false;
    }
}
