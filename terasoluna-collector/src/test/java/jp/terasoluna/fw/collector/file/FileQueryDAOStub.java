package jp.terasoluna.fw.collector.file;

import jp.terasoluna.fw.file.dao.FileLineIterator;
import jp.terasoluna.fw.file.dao.FileQueryDAO;

public class FileQueryDAOStub implements FileQueryDAO {

    public <T> FileLineIterator<T> execute(String fileName, Class<T> clazz) {
        return new FileLineIteratorStub<T>();
    }

}
