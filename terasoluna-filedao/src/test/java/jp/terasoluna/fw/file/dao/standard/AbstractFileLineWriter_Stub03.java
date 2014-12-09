package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;

/**
 * AbstractFileLineWriterの試験で利用するファイル行オブジェクトのスタブクラス。<br>
 * <br>
 * 以下の@FileFormatの設定を持つ<br>
 * <ul>
 * <li>delimiter："|"(デフォルト値以外)</li>
 * <li>encloseChar："\""(デフォルト値以外)</li>
 * <li>lineFeedChar：""(空文字、デフォルト値)</li>
 * <li>fileEncoding：""(空文字、デフォルト値)</li>
 * <li>overWriteFlg：true(デフォルト値以外)</li>
 * </ul>
 * <br>
 * フィールドは持たない<br>
 * @author 趙俸徹
 */
@FileFormat(delimiter = '|', encloseChar = '\"', lineFeedChar = "", fileEncoding = "", overWriteFlg = true)
public class AbstractFileLineWriter_Stub03 {

}
