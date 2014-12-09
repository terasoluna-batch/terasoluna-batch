package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;

/**
 * FileFormatアノテーションの設定を持つ、ファイル行オブジェクトスタブクラス
 * <p>
 * 以下の設定を持つ<br>
 * <ul>
 * <li>@FileFormat(delimiter=';', encloseChar=';', lineFeedChar = "\r", fileEncoding ="MS932", headerLineCount=1,
 * trailerLineCount=1)
 * <li>属性
 * <ul>
 * <li>なし
 * </ul>
 * </ul>
 */
@FileFormat(delimiter = ';', encloseChar = ';', lineFeedChar = "\r", fileEncoding = "MS932", headerLineCount = 1, trailerLineCount = 1)
public class AbstractFileLineIterator_Stub04 {

}
