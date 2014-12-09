package jp.terasoluna.fw.file.dao.standard;

import java.math.BigDecimal;
import java.util.Date;

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
 * <li>@InputFileColumn(columnIndex = 1)<br>
 * String column2
 * <li>@InputFileColumn(columnIndex = 0)<br>
 * int column1
 * <li>@InputFileColumn(columnIndex = 3, columnFormat="yyyy/MM/dd")<br>
 * Date column4 = null
 * <li>@InputFileColumn(columnIndex = 2, columnFormat = "###,###")<br>
 * BigDecimal column3
 * </ul>
 * </ul>
 */
@FileFormat(encloseChar = '\"')
public class AbstractFileLineIterator_Stub55 {

    @InputFileColumn(columnIndex = 1)
    private String column2 = null;

    @InputFileColumn(columnIndex = 0)
    private int column1 = 0;

    @InputFileColumn(columnIndex = 3, columnFormat = "yyyy/MM/dd")
    private Date column4 = null;

    @InputFileColumn(columnIndex = 2, columnFormat = "###,###")
    private BigDecimal column3 = null;

    public int getColumn1() {
        return column1;
    }

    public void setColumn1(int column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public BigDecimal getColumn3() {
        return column3;
    }

    public void setColumn3(BigDecimal column3) {
        this.column3 = column3;
    }

    public Date getColumn4() {
        return column4;
    }

    public void setColumn4(Date column4) {
        this.column4 = column4;
    }
}
