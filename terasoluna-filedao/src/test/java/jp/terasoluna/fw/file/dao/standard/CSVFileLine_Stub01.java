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

@FileFormat(lineFeedChar = "\r\n")
public class CSVFileLine_Stub01 {
    @InputFileColumn(columnIndex = 0, columnEncloseChar = '"')
    @OutputFileColumn(columnIndex = 0, columnEncloseChar = '"')
    private String column1 = null;

    @InputFileColumn(columnIndex = 1)
    @OutputFileColumn(columnIndex = 1)
    private String column2 = null;

    @InputFileColumn(columnIndex = 2)
    @OutputFileColumn(columnIndex = 2)
    private String column3 = null;

    @InputFileColumn(columnIndex = 3, columnEncloseChar = '|')
    @OutputFileColumn(columnIndex = 3, columnEncloseChar = '|')
    private String column4 = null;

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

    public String getColumn4() {
        return column4;
    }

    public void setColumn4(String column4) {
        this.column4 = column4;
    }
}
