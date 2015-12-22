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
 * </ul>
 */
@FileFormat(encloseChar = '"')
public class CSVFileLineIterator_Stub04 {

    @InputFileColumn(columnIndex = 0)
    private String column1 = null;

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

}
