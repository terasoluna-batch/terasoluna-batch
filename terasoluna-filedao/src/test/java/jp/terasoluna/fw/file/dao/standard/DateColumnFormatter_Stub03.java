package jp.terasoluna.fw.file.dao.standard;

import java.util.Date;

import jp.terasoluna.fw.file.annotation.FileFormat;

/**
 * 例外を発生させるファイル行オブジェクトスタブクラス
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat()
 */
@FileFormat()
public class DateColumnFormatter_Stub03 {
    @SuppressWarnings("unused")
    private Date date;

    public Date getDate() {
        throw new RuntimeException("例外発生");
    }

}
