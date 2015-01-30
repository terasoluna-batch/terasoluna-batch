package jp.terasoluna.fw.file.dao.standard;

import java.io.IOException;
import java.io.Reader;

/**
 * Reader実装クラス
 * <p>
 * {@link #read()}を呼び出すと、IOExceptionをスローする
 */
public class LineFeed2LineReader_Stub02 extends Reader {

    @Override
    public void close() throws IOException {

    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        throw new IOException();
    }

    @Override
    public boolean ready() throws IOException {
        return true;
    }
}
