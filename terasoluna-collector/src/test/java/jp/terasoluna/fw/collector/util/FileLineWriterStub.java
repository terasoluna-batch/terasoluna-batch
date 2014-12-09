package jp.terasoluna.fw.collector.util;

import java.util.List;

import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.dao.FileLineWriter;

public class FileLineWriterStub<T> implements FileLineWriter<T> {

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

    public void printDataLine(T t) {

    }

    public void printHeaderLine(List<String> headerLine) {

    }

    public void printTrailerLine(List<String> trailerLine) {

    }

}
