package jp.terasoluna.fw.collector.file;

import java.util.List;

import jp.terasoluna.fw.file.dao.FileLineIterator;

public class FileLineIteratorStub<T> implements FileLineIterator<T> {

    public void closeFile() {
        throw new RuntimeException("hoge");
    }

    public List<String> getHeader() {

        return null;
    }

    public List<String> getTrailer() {

        return null;
    }

    public boolean hasNext() {

        return false;
    }

    public T next() {

        return null;
    }

    public void skip(int skipLines) {

    }

    public void remove() {

    }

}
