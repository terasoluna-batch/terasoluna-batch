package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;

/**
 * AbstractFileLineWriterの試験で利用するファイル行オブジェクトのスタブクラス。<br>
 * <br>
 * 以下の@FileFormatの設定を持つ<br>
 * <ul>
 * <li>delimiter："|"(デフォルト値以外)</li>
 * <li>encloseChar："\""(デフォルト値以外)</li>
 * <li>lineFeedChar："\r\n\t"(デフォルト値以外、3桁以上)</li>
 * <li>fileEncoding："UTF-8"(デフォルト値以外)</li>
 * <li>overWriteFlg：true(デフォルト値以外)</li>
 * </ul>
 * <br>
 * フィールドは持たない<br>
 * @author 趙俸徹
 */
@FileFormat(delimiter = '|', encloseChar = '\"', lineFeedChar = "\r\n\t", fileEncoding = "UTF-8", overWriteFlg = true)
public class AbstractFileLineWriter_Stub05 {

}
