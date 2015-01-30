/*
 * $Id: CSVFileLineIterator_Stub01.java 1937 2006-12-21 07:10:23Z inouek $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */
package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;

/**
 * FileFormatアノテーションの設定を持つ、ファイル行オブジェクトスタブクラス
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat(delimiter = '"')
 * </ul>
 */
@FileFormat(delimiter = '"')
public class CSVFileLineIterator_Stub03 {
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
