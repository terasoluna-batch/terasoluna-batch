package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;

/**
 * FileFormatアノテーションを持ち、属性を持たないファイル行オブジェクトスタブ
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat(delimiter以外の設定はデフォルト値ではない)
 */
@FileFormat(lineFeedChar = "\r", encloseChar = '\"', fileEncoding = "UTF-8", headerLineCount = 1, trailerLineCount = 1, overWriteFlg = true)
public class CSVFileLineWriter_Stub01 {
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
