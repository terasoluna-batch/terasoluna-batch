package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;

/**
 * AbstractFileLineWriterの試験で利用するファイル行オブジェクトのスタブクラス。<br>
 * <br>
 * 以下の@FileFormatの設定を持つ<br>
 * <ul>
 * <li>全項目：デフォルト値</li>
 * </ul>
 * <br>
 * <code>@OutputFileColumn設定ありのフィールドを持つ<br>
 * <ul>
 * <li>フィールド：String column1
 *     <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：0<br>
 * > その他項目：デフォルト値</li> </ul> 各フィールドのgetter/setterメソッドを持たない。
 * @author 趙俸徹
 */
@FileFormat()
public class AbstractFileLineWriter_Stub25 {

    /**
     * column1
     */
    @SuppressWarnings("unused")
    @OutputFileColumn(columnIndex = 0)
    private String column1 = null;

}
