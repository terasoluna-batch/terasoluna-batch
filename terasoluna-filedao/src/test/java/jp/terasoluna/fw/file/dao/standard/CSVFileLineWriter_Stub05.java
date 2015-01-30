package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;

/**
 * FileFormatアノテーションを持ち、属性を持たないファイル行オブジェクトスタブ
 * <p>
 * FileFormatアノテーションで、区切り文字を設定している。
 */
@FileFormat(delimiter = '、')
public class CSVFileLineWriter_Stub05 {
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
