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
 * <li>@InputFileColumn(columnIndex=2)<br>
 * String column3
 * <li>@InputFileColumn(columnIndex=0)<br>
 * long column1
 * <li>@InputFileColumn(columnIndex=1)<br>
 * String column2
 * </ul>
 * </ul>
 */
@FileFormat()
public class AbstractFileLineIterator_Stub90 {

    @InputFileColumn(columnIndex = 2)
    private String column3 = null;

    @InputFileColumn(columnIndex = 0)
    private long column1 = -1;

    @InputFileColumn(columnIndex = 1)
    private String column2 = null;

    public long getColumn1() {
        return column1;
    }

    public void setColumn1(long column1) {
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

}
