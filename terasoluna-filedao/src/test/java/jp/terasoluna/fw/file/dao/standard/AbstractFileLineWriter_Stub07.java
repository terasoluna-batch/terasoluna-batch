package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;

/**
 * AbstractFileLineWriterの試験で利用するファイル行オブジェクトのスタブクラス。<br>
 * <br>
 * 以下の@FileFormatの設定を持つ<br>
 * <ul>
 * <li>delimiter："|"(デフォルト値以外)</li>
 * <li>encloseChar："\""(デフォルト値以外)</li>
 * <li>lineFeedChar："\r"(デフォルト値以外)</li>
 * <li>fileEncoding："UTF-X"(存在しないエンコーディング)</li>
 * <li>overWriteFlg：false(デフォルト値)</li>
 * </ul>
 * <br>
 * フィールドは持たない<br>
 * @author 趙俸徹
 */
@FileFormat(delimiter = '|', encloseChar = '\"', lineFeedChar = "\r", fileEncoding = "UTF-X", overWriteFlg = false)
public class AbstractFileLineWriter_Stub07 {
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
