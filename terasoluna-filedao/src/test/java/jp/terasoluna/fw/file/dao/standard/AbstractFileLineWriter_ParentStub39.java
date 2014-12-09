package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;

/**
 * AbstractFileLineWriterの試験で利用するファイル行オブジェクトの親スタブクラス。<br>
 * <br>
 * 以下の@FileFormatの設定を持つ<br>
 * <ul>
 * <li>全項目：デフォルト値以外</li>
 * </ul>
 * フィールドを持たない。
 * @author 趙俸徹
 */
@FileFormat(lineFeedChar = "\r", delimiter = '|', encloseChar = '\"', fileEncoding = "UTF-8", headerLineCount = 5, trailerLineCount = 5, overWriteFlg = true)
public class AbstractFileLineWriter_ParentStub39 {

}
