package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;

/**
 * FileFormatアノテーションの設定を持つ、ファイル行オブジェクトスタブ
 * <p>
 * AbstractFileLineIterator_Stub35を継承するサブクラス。<br>
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat()
 * <li>属性
 * <ul>
 * <li>@InputFileColumn(columnIndex=2)<br>
 * String noMappingColumn3
 * <li>String noMappingColumn3
 * </ul>
 * </ul>
 */
@FileFormat()
public class AbstractFileLineIterator_Stub36 extends
                                            AbstractFileLineIterator_Stub35 {

    @InputFileColumn(columnIndex = 2)
    private String column3 = null;

    private String noMappingColumn3 = null;

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

    public String getNoMappingColumn3() {
        return noMappingColumn3;
    }

    public void setNoMappingColumn3(String noMappingColumn3) {
        this.noMappingColumn3 = noMappingColumn3;
    }

}
