package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;

/**
 * AbstractFileLineWriterの試験で利用するファイル行オブジェクトのスタブクラス。<br>
 * <br>
 * 以下の@FileFormatの設定を持つ<br>
 * <ul>
 * <li>全項目：デフォルト値</li>
 * </ul>
 * <br>
 * <code>@OutputFileColumn設定ありのフィールドを持つ<br>
 * <ul>
 * <li>フィールド：String column1
 *     <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：0<br>
 * > bytes：48<br>
 * > その他項目：デフォルト値</li> </ul>
 * <ul>
 * <li>フィールド：String column2 <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：1<br>
 * > bytes：0(※ここが0)<br>
 * > その他項目：デフォルト値</li>
 * </ul>
 * <ul>
 * <li>フィールド：String column3 <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：2<br>
 * > bytes：48<br>
 * > その他項目：デフォルト値</li>
 * </ul>
 * @author 趙俸徹
 */
@FileFormat()
public class AbstractFileLineWriter_Stub36 {

    /**
     * column1
     */
    @OutputFileColumn(columnIndex = 0, bytes = 48)
    private String column1 = null;

    /**
     * column2
     */
    @OutputFileColumn(columnIndex = 1, bytes = 0)
    private String column2 = null;

    /**
     * column3
     */
    @OutputFileColumn(columnIndex = 2, bytes = 48)
    private String column3 = null;

    /**
     * column1を取得する。
     * @return column1
     */
    public String getColumn1() {
        return column1;
    }

    /**
     * column1を設定する。
     * @param column1 column1
     */
    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    /**
     * column2を取得する。
     * @return column2
     */
    public String getColumn2() {
        return column2;
    }

    /**
     * column2を設定する。
     * @param column2 column2
     */
    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    /**
     * column3を取得する。
     * @return column3
     */
    public String getColumn3() {
        return column3;
    }

    /**
     * column3を設定する。
     * @param column3 column3
     */
    public void setColumn3(String column3) {
        this.column3 = column3;
    }
}
