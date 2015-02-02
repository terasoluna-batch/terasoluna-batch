package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;

/**
 * FileFormatアノテーションの設定を持つ、ファイル行オブジェクトスタブクラス
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat(delimiter = '#', encloseChar = '\"')
 * <li>属性
 * <ul>
 * <li>なし
 * </ul>
 * </ul>
 */
@FileFormat(lineFeedChar = "\r\n", delimiter = '#', encloseChar = '\"')
public class PlainFileLineIterator_Stub02 {

}
