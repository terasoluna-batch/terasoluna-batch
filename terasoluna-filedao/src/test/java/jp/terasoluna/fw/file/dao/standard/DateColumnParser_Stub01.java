/*
 * $Id: DateColumnParser_Stub01.java 6004 2008-01-11 10:18:33Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */
package jp.terasoluna.fw.file.dao.standard;

import java.util.Date;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;

/**
 * FileFormatアノテーションの設定を持つ、ファイル行オブジェクトスタブクラス
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat()
 * <li>属性
 * <ul>
 * <li>@InputFileColumn(columnIndex=0)<br>
 * Date a
 * <li>@InputFileColumn(columnIndex=1)<br>
 * Date b
 * </ul>
 * </ul>
 * <p>
 * 属性はpublicで引数1つのsetterを持つ。
 */

@FileFormat()
public class DateColumnParser_Stub01 {

    @InputFileColumn(columnIndex = 0)
    private Date a;

    @InputFileColumn(columnIndex = 1)
    private Date b;

    public void setA(Date a) {
        this.a = a;
    }

    @SuppressWarnings("unused")
    private void setAPrivate(Date a) {
        this.a = a;
    }

    public void setAException(Date a) throws Exception {
        throw new Exception();
    }

    public void setAAndB(Date a, Date b) {
        this.a = a;
        this.b = b;
    }
}
