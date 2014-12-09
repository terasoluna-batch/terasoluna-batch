package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;
import jp.terasoluna.fw.file.annotation.StringConverterToUpperCase;

/**
 * FileFormatアノテーションの設定を持つ、ファイル行オブジェクトスタブ
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat()
 * <li>属性
 * <ul>
 * <li>@InputFileColumn(columnIndex=0, stringConverter=StringConverterToUpperCase.class)<br>
 * String column1
 * </ul>
 * </ul>
 */
@FileFormat()
public class AbstractFileLineIterator_Stub41 {

    @InputFileColumn(columnIndex = 0, stringConverter = StringConverterToUpperCase.class)
    private String column1 = null;

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

}
