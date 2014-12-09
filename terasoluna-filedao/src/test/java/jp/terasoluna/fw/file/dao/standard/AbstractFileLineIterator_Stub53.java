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
 * <li>String noMappingColumn3
 * <li>String noMappingColumn1
 * <li>String noMappingColumn2
 * </ul>
 * </ul>
 */
@FileFormat()
public class AbstractFileLineIterator_Stub53 {

    private String noMappingColumn3 = null;

    private String noMappingColumn1 = null;

    private String noMappingColumn2 = null;

    public String getNoMappingColumn1() {
        return noMappingColumn1;
    }

    public void setNoMappingColumn1(String noMappingColumn1) {
        this.noMappingColumn1 = noMappingColumn1;
    }

    public String getNoMappingColumn2() {
        return noMappingColumn2;
    }

    public void setNoMappingColumn2(String noMappingColumn2) {
        this.noMappingColumn2 = noMappingColumn2;
    }

    public String getNoMappingColumn3() {
        return noMappingColumn3;
    }

    public void setNoMappingColumn3(String noMappingColumn3) {
        this.noMappingColumn3 = noMappingColumn3;
    }

}
