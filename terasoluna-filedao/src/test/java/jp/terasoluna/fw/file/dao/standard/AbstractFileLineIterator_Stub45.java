package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;
import jp.terasoluna.fw.file.annotation.NullStringConverter;
import jp.terasoluna.fw.file.annotation.StringConverterToLowerCase;
import jp.terasoluna.fw.file.annotation.StringConverterToUpperCase;

/**
 * FileFormatアノテーションの設定を持つ、ファイル行オブジェクトスタブ
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat()
 * <li>属性
 * <ul>
 * <li>@InputFileColumn(columnIndex = 2)<br>
 * String column3
 * <li>@InputFileColumn(columnIndex = 4, stringConverter = StringConverterToLowerCase.class)<br>
 * String column5
 * <li>@InputFileColumn(columnIndex = 0, stringConverter = NullStringConverter.class)<br>
 * String column1
 * <li>@InputFileColumn(columnIndex = 3, stringConverter = StringConverterToUpperCase.class)<br>
 * String column4
 * <li>@InputFileColumn(columnIndex = 1, stringConverter = StringConverterToLowerCase.class)<br>
 * String column2
 * </ul>
 * </ul>
 */
@FileFormat()
public class AbstractFileLineIterator_Stub45 {

    @InputFileColumn(columnIndex = 2)
    private String column3 = null;

    @InputFileColumn(columnIndex = 4, stringConverter = StringConverterToLowerCase.class)
    private String column5 = null;

    @InputFileColumn(columnIndex = 0, stringConverter = NullStringConverter.class)
    private String column1 = null;

    @InputFileColumn(columnIndex = 3, stringConverter = StringConverterToUpperCase.class)
    private String column4 = null;

    @InputFileColumn(columnIndex = 1, stringConverter = StringConverterToLowerCase.class)
    private String column2 = null;

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

    public String getColumn4() {
        return column4;
    }

    public void setColumn4(String column4) {
        this.column4 = column4;
    }

    public String getColumn5() {
        return column5;
    }

    public void setColumn5(String column5) {
        this.column5 = column5;
    }

}
