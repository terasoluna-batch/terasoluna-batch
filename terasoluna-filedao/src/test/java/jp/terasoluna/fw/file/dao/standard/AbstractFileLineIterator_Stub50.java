package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;

/**
 * FileFormatアノテーションの設定を持つ、ファイル行オブジェクトスタブ
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat()
 * <li>属性
 * <ul>
 * <li>String noMappingColumn1
 * </ul>
 * </ul>
 */
@FileFormat()
public class AbstractFileLineIterator_Stub50 {

    private String noMappingColumn1 = null;

    public String getNoMappingColumn1() {
        return noMappingColumn1;
    }

    public void setNoMappingColumn1(String noMappingColumn1) {
        this.noMappingColumn1 = noMappingColumn1;
    }
}
