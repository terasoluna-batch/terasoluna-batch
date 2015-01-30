package jp.terasoluna.fw.file.dao.standard;

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
 * <li>@InputFileColumn(columnIndex = 0, bytes = 5, stringConverter = StringConverterToUpperCase.class, trimChar = '0', trimType
 * = TrimType.LEFT) String shopId
 * </ul>
 * </ul>
 */
@FileFormat(lineFeedChar = "\r\n")
public class FixedFileLineIterator_Stub03 {

    @InputFileColumn(columnIndex = 0, bytes = 5, stringConverter = StringConverterToUpperCase.class, trimChar = '0', trimType = TrimType.LEFT)
    private String shopId = null;

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

}
