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
 * <li>@InputFileColumn(columnIndex = 0)<br>
 * String noMappingColumn1
 * </ul>
 * </ul>
 */
@FileFormat()
public class AbstractFileLineIterator_Stub52 {

    @SuppressWarnings("unused")
    @InputFileColumn(columnIndex = 0)
    private String column1 = null;

}
