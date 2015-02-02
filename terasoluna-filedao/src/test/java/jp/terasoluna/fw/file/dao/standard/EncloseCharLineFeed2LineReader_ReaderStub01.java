package jp.terasoluna.fw.file.dao.standard;

import java.io.IOException;
import java.io.Reader;

/**
 * 例外(IOException)をスローするスタブクラス
 */
public class EncloseCharLineFeed2LineReader_ReaderStub01 extends Reader {

    @Override
    public void close() throws IOException {
        throw new IOException();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        throw new IOException();
    }

    @Override
    public boolean ready() throws IOException {
        throw new IOException();
    }
}
