package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;

/**
 * privateのgetメソッドを持つファイル行オブジェクト。
 */
@FileFormat()
public class IntColumnFormatter_Stub02 {

    @InputFileColumn(columnIndex = 0)
    @OutputFileColumn(columnIndex = 0)
    private int intValue;

    @SuppressWarnings("unused")
    private int getIntValue() {
        return intValue;
    }
}
