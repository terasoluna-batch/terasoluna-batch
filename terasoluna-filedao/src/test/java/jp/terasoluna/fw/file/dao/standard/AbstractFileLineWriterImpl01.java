package jp.terasoluna.fw.file.dao.standard;

import java.util.Map;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.dao.standard.AbstractFileLineWriter;
import jp.terasoluna.fw.file.dao.standard.ColumnFormatter;

/**
 * AbstractFileLineWriterのImplクラス。<br>
 * 空実装<br>
 * 「区切り文字」と「囲み文字」を設定できる。<br>
 * コンストラクタでinit()を実行していないため、コンストラクタ以外の試験では<br>
 * インスタンス生成後、必ずinit()を実行してから利用すること。
 * @param <T> ファイル行オブジェクト
 */
public class AbstractFileLineWriterImpl01<T> extends AbstractFileLineWriter<T> {

    /**
     * 区切り文字
     */
    private char delimiter = Character.MIN_VALUE;

    /**
     * 囲み文字
     */
    private char encloseChar = Character.MIN_VALUE;

    /**
     * コンストラクタ。
     * @param fileName ファイル名
     * @param clazz パラメータクラス
     * @param columnFormatterMap テキスト取得ルール
     */
    public AbstractFileLineWriterImpl01(String fileName, Class<T> clazz,
            Map<String, ColumnFormatter> columnFormatterMap) {
        super(fileName, clazz, columnFormatterMap);

        FileFormat fileFormat = clazz.getAnnotation(FileFormat.class);
        this.delimiter = fileFormat.delimiter();
        this.encloseChar = fileFormat.encloseChar();
    }

    /**
     * ファイル行オブジェクトにアノテーションが設定されている事をチェックするかどうかを返す。
     * @return チェックを行う場合はtrue。
     */
    @Override
    protected boolean isCheckColumnAnnotationCount() {
        return false;
    }

    /**
     * delimiterを取得する。
     * @return delimiter
     */
    public char getDelimiter() {
        return delimiter;
    }

    /**
     * delimiterを設定する。
     * @param delimiter delimiter
     */
    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * encloseCharを取得する。
     * @return encloseChar
     */
    public char getEncloseChar() {
        return encloseChar;
    }

    /**
     * encloseCharを設定する。
     * @param encloseChar encloseChar
     */
    public void setEncloseChar(char encloseChar) {
        this.encloseChar = encloseChar;
    }
}
