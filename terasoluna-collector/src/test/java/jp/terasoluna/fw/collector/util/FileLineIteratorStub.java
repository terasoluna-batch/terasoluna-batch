package jp.terasoluna.fw.collector.util;

import java.util.List;

import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.dao.FileLineIterator;

public class FileLineIteratorStub<T> implements FileLineIterator<T> {

    private boolean closeFileCalled = false;;

    private boolean closeException = false;

    public boolean isCloseFileCalled() {
        return closeFileCalled;
    }

    public void setCloseFileCalled(boolean closeFileCalled) {
        this.closeFileCalled = closeFileCalled;
    }

    public void closeFile() {
        this.closeFileCalled = true;
        if (this.closeException) {
            throw new FileException("hoge");
        }
    }

    public boolean isCloseException() {
        return closeException;
    }

    public void setCloseException(boolean closeException) {
        this.closeException = closeException;
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
