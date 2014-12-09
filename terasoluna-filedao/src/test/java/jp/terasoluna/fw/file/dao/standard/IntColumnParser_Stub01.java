/*
 * $Id: IntColumnParser_Stub01.java 6004 2008-01-11 10:18:33Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;

/**
 * FileFormatアノテーションを持ち、属性を持たないファイル行オブジェクトスタブ
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat()
 * <li>属性
 * <ul>
 * <li>@InputFileColumn(columnIndex = 0) <br>
 * @OutputFileColumn(columnIndex = 0) <br>
 *                               a</li> <li>@InputFileColumn(columnIndex = 1) <br>
 * @OutputFileColumn(columnIndex = 1) <br>
 *                               b</li>
 *                               </ul>
 *                               </ul>
 */
@FileFormat()
public class IntColumnParser_Stub01 {

    @SuppressWarnings("unused")
    @InputFileColumn(columnIndex = 0)
    @OutputFileColumn(columnIndex = 0)
    private int a;

    @SuppressWarnings("unused")
    @InputFileColumn(columnIndex = 1)
    @OutputFileColumn(columnIndex = 1)
    private int b;

    public void setA(int a) {
        this.a = a;
    }

    @SuppressWarnings("unused")
    private void setAPrivate(int a) {
        this.a = a;
    }

    public void setAException(int a) throws Exception {
        throw new Exception();
    }

    public void setAAndB(int a, int b) {
        this.a = a;
        this.b = b;
    }

}
