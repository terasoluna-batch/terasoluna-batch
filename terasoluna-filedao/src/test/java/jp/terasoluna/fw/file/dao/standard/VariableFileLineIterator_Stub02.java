package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;

/**
 * FileFormatアノテーションの設定を持つ、ファイル行オブジェクトスタブクラス
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat(delimiter='、', encloseChar='"')
 * <li>属性
 * <ul>
 * <li>なし
 * </ul>
 * </ul>
 */
@FileFormat(delimiter = '、', encloseChar = '"')
public class VariableFileLineIterator_Stub02 {
    @InputFileColumn(columnIndex = 0)
    @OutputFileColumn(columnIndex = 0)
    private String dummy;

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }
}
