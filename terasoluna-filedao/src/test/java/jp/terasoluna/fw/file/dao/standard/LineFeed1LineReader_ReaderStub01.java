package jp.terasoluna.fw.file.dao.standard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * LineFeed1LineReadeの試験で利用するファイル行オブジェクトクラス。<br>
 * 空実装<br>
 * 入力された文字列をread()することで一文字づつ返す。
 * @author 姜恩美
 */
public class LineFeed1LineReader_ReaderStub01 extends Reader {

    private char[] data = new char[0];

    @Override
    public void close() throws IOException {
        throw new IOException();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {

        if (data.length < len) {
            throw new IllegalArgumentException();
        }
        if (len < off) {
            throw new IllegalArgumentException();
        }

        cbuf[0] = data[0];

        char[] temp = new char[data.length - 1];
        System.arraycopy(data, 1, temp, 0, data.length - 1);
        this.data = temp;
        return 1;
    }

    public boolean ready() throws IOException {
        return 0 < data.length;
    }

    public void setDataString(String dataString) {
        this.data = dataString.toCharArray();
    }

}
