package jp.terasoluna.fw.file.dao.standard;

import java.math.BigDecimal;
import java.util.Date;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;
import jp.terasoluna.fw.file.annotation.StringConverterToUpperCase;
import jp.terasoluna.fw.file.annotation.TrimType;

/**
 * FileFormatアノテーションの設定を持つ、ファイル行オブジェクトスタブクラス
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat()
 * <li>属性
 * <ul>
 * <li>@InputFileColumn(columnIndex = 0, bytes = 10, columnFormat = "yyyy/MM/dd") Date hiduke
 * </ul>
 * <ul>
 * <li>@InputFileColumn(columnIndex = 1, bytes = 5, stringConverter = StringConverterToUpperCase.class, trimChar = '0', trimType
 * = TrimType.LEFT) String shopId
 * </ul>
 * <ul>
 * <li>@InputFileColumn(columnIndex = 2, bytes = 9, columnFormat = "#########") BigDecimal uriage
 * </ul>
 * </ul>
 */
@FileFormat(lineFeedChar = "\r\n")
public class FixedFileLineIterator_Stub04 {

    @InputFileColumn(columnIndex = 0, bytes = 10, columnFormat = "yyyy/MM/dd")
    private Date hiduke = null;

    @InputFileColumn(columnIndex = 1, bytes = 5, stringConverter = StringConverterToUpperCase.class, trimChar = '0', trimType = TrimType.LEFT)
    private String shopId = null;

    @InputFileColumn(columnIndex = 2, bytes = 9, columnFormat = "#########")
    private BigDecimal uriage = null;

    /**
     * @return hiduke を戻します。
     */
    public Date getHiduke() {
        return hiduke;
    }

    /**
     * @param hiduke 設定する hiduke。
     */
    public void setHiduke(Date hiduke) {
        this.hiduke = hiduke;
    }

    /**
     * @return shopId を戻します。
     */
    public String getShopId() {
        return shopId;
    }

    /**
     * @param shopId 設定する shopId。
     */
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    /**
     * @return uriage を戻します。
     */
    public BigDecimal getUriage() {
        return uriage;
    }

    /**
     * @param uriage 設定する uriage。
     */
    public void setUriage(BigDecimal uriage) {
        this.uriage = uriage;
    }
}
