package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.NullStringConverter;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;
import jp.terasoluna.fw.file.annotation.StringConverterToLowerCase;
import jp.terasoluna.fw.file.annotation.StringConverterToUpperCase;

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
 * > stringConverter：NullStringConverter.class<br>
 * > その他項目：デフォルト値</li> </ul>
 * <ul>
 * <li>フィールド：String column2 <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：1<br>
 * > stringConverter：StringConverterToLowerCase.class<br>
 * > その他項目：デフォルト値</li>
 * </ul>
 * <ul>
 * <li>フィールド：String column3 <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：2<br>
 * > stringConverter：StringConverterToUpperCase.class<br>
 * > その他項目：デフォルト値</li>
 * </ul>
 * <ul>
 * <li>フィールド：String column4 <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：3<br>
 * > stringConverter：NullStringConverter.class<br>
 * > その他項目：デフォルト値</li>
 * </ul>
 * <ul>
 * <li>フィールド：String column5 <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：4<br>
 * > stringConverter：StringConverterToLowerCase.class<br>
 * > その他項目：デフォルト値</li>
 * </ul>
 * <ul>
 * <li>フィールド：String column6 <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：5<br>
 * > stringConverter：StringConverterToUpperCase.class<br>
 * > その他項目：デフォルト値</li>
 * </ul>
 * @author 趙俸徹
 */
@FileFormat()
public class AbstractFileLineWriter_Stub22 {

    /**
     * column1
     */
    @OutputFileColumn(columnIndex = 0, stringConverter = NullStringConverter.class)
    private String column1 = null;

    /**
     * column2
     */
    @OutputFileColumn(columnIndex = 1, stringConverter = StringConverterToLowerCase.class)
    private String column2 = null;

    /**
     * column3
     */
    @OutputFileColumn(columnIndex = 2, stringConverter = StringConverterToUpperCase.class)
    private String column3 = null;

    /**
     * column4
     */
    @OutputFileColumn(columnIndex = 3, stringConverter = NullStringConverter.class)
    private String column4 = null;

    /**
     * column5
     */
    @OutputFileColumn(columnIndex = 4, stringConverter = StringConverterToLowerCase.class)
    private String column5 = null;

    /**
     * column6
     */
    @OutputFileColumn(columnIndex = 5, stringConverter = StringConverterToUpperCase.class)
    private String column6 = null;

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

    /**
     * column4を取得する。
     * @return column4
     */
    public String getColumn4() {
        return column4;
    }

    /**
     * column4を設定する。
     * @param column4 column4
     */
    public void setColumn4(String column4) {
        this.column4 = column4;
    }

    /**
     * column5を取得する。
     * @return column5
     */
    public String getColumn5() {
        return column5;
    }

    /**
     * column5を設定する。
     * @param column5 column5
     */
    public void setColumn5(String column5) {
        this.column5 = column5;
    }

    /**
     * column6を取得する。
     * @return column6
     */
    public String getColumn6() {
        return column6;
    }

    /**
     * column6を設定する。
     * @param column6 column6
     */
    public void setColumn6(String column6) {
        this.column6 = column6;
    }

}
