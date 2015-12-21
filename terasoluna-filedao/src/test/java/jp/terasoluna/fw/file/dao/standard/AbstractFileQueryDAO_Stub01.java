package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.dao.FileLineIterator;
import jp.terasoluna.fw.file.dao.standard.AbstractFileQueryDAO;

/**
 * <code>AbstractFileQueryDAO</code>をテストするためのスタブクラス。
 * <p>
 * 空実装
 */
public class AbstractFileQueryDAO_Stub01 extends AbstractFileQueryDAO {

    @Override
    public <T> FileLineIterator<T> execute(String fileName, Class<T> clazz) {
        return null;
    }

}
