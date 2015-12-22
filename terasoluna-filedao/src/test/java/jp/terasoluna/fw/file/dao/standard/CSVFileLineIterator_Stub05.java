package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;

/**
 * FileFormatアノテーションの設定を持つ、ファイル行オブジェクトスタブクラス
 * <ul>
 * <li>@FileFormat(encloseChar = '"')
 * <li>属性
 * <ul>
 * <li>@InputFileColumn(columnIndex = 0)<br>
 * String column01
 * </ul>
 * <ul>
 * <li>@InputFileColumn(columnIndex = 1)<br>
 * String column02
 * </ul>
 * <ul>
 * <li>@InputFileColumn(columnIndex = 2)<br>
 * String column03
 * </ul>
 * </ul>
 */
@FileFormat(encloseChar = '"')
public class CSVFileLineIterator_Stub05 {

    @InputFileColumn(columnIndex = 0)
    private String column1 = null;

    @InputFileColumn(columnIndex = 1)
    private String column2 = null;

    @InputFileColumn(columnIndex = 2)
    private String column3 = null;

    /**
     * @return column1
     */
    public String getColumn1() {
        return column1;
    }

    /**
     * @param column1 設定する column1
     */
    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    /**
     * @return column2
     */
    public String getColumn2() {
        return column2;
    }

    /**
     * @param column2 設定する column2
     */
    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    /**
     * @return column3
     */
    public String getColumn3() {
        return column3;
    }

    /**
     * @param column3 設定する column3
     */
    public void setColumn3(String column3) {
        this.column3 = column3;
    }

}
