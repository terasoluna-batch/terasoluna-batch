package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;

/**
 * FileFormatアノテーションの設定を持つ、ファイル行オブジェクトスタブ
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat()
 * <li>属性
 * <ul>
 * <li>@InputFileColumn(columnIndex=0)<br>
 * String column1
 * <li>String noMappingColumn1
 * <li>@InputFileColumn(columnIndex=1) String column2
 * </ul>
 * </ul>
 */
@FileFormat()
public class AbstractFileLineIterator_Stub54 {

    @InputFileColumn(columnIndex = 0)
    private String column1 = null;

    private String noMappingColumn1 = null;

    @InputFileColumn(columnIndex = 1)
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

    public String getNoMappingColumn1() {
        return noMappingColumn1;
    }

    public void setNoMappingColumn1(String noMappingColumn1) {
        this.noMappingColumn1 = noMappingColumn1;
    }

}
