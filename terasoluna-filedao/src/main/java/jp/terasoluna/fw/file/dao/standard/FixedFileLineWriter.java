/*
 * Copyright (c) 2007 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.terasoluna.fw.file.dao.standard;

import java.util.Map;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;
import jp.terasoluna.fw.file.dao.FileException;

/**
 * 固定長ファイル用のファイルアクセス(データ書込)クラス.
 * <p>
 * ファイル行オブジェクトからデータを読み込み、 1行分のデータを固定長形式でファイルに書き込む。
 * </p>
 * <b>※利用するファイル行オブジェクトのアノテーション項目</b><br>
 * ⅰ．@{@link FileFormat}の設定項目<br>
 * <div align="center">
 * <table width="90%" border="1" bgcolor="#FFFFFF">
 * <tr>
 * <td><b>論理項目名</b></td>
 * <td><b>物理項目名</b></td>
 * <td><b>デフォルト値</b></td>
 * <td><b>必須性</b></td>
 * </tr>
 * <tr>
 * <td> <code>行区切り文字</code></td>
 * <td> <code>lineFeedChar</code></td>
 * <td> <code>システムの行区切り文字</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * <tr>
 * <td> <code>ファイルエンコーディング</code></td>
 * <td> <code>fileEncoding</code></td>
 * <td> <code>システムのファイルエンコーディング</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * <tr>
 * <td> <code>ファイル上書きフラグ</code></td>
 * <td> <code>overWriteFlg</code></td>
 * <td> <code>false</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * </table>
 * </div> <br>
 * ⅱ．@{@link jp.terasoluna.fw.file.annotation.InputFileColumn}、@{@link OutputFileColumn}の設定項目<br>
 * <div align="center">
 * <table width="90%" border="1" bgcolor="#FFFFFF">
 * <tr>
 * <td><b>論理項目名</b></td>
 * <td><b>物理項目名</b></td>
 * <td><b>デフォルト値</b></td>
 * <td><b>必須性</b></td>
 * </tr>
 * <tr>
 * <td> <code>カラムインデックス</code></td>
 * <td> <code>columnIndex</code></td>
 * <td>-</td>
 * <td> <code>必須</code></td>
 * </tr>
 * <tr>
 * <td> <code>フォーマット</code></td>
 * <td> <code>columnFormat</code></td>
 * <td> <code>""</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * <tr>
 * <td> <code>バイト長</code></td>
 * <td> <code>bytes</code></td>
 * <td> <code>-1</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * <tr>
 * <td> <code>パディング種別</code></td>
 * <td> <code>paddingType</code></td>
 * <td> <code>パディングなし</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * <tr>
 * <td> <code>パディング文字</code></td>
 * <td> <code>paddingChar</code></td>
 * <td> <code>' '</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * <tr>
 * <td> <code>トリム種別</code></td>
 * <td> <code>trimType</code></td>
 * <td> <code>トリムなし</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * <tr>
 * <td> <code>トリム文字</code></td>
 * <td> <code>trimChar</code></td>
 * <td> <code>' '</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * <tr>
 * <td> <code>文字変換種別</code></td>
 * <td> <code>stringConverter</code></td>
 * <td> <code>NullStringConverter.class</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * </table>
 * </div> <br>
 * <b>※注意事項</b><br>
 * <ul>
 * 　
 * <li>区切り文字を設定することは出来ない。(エラー発生)</li> 　
 * <li>囲み文字を設定することは出来ない。(エラー発生)</li>
 * </ul>
 * @param <T> ファイル行オブジェクト。
 */
public class FixedFileLineWriter<T> extends AbstractFileLineWriter<T> {

    /**
     * 区切り文字。固定長ファイルは「'\u0000'」で固定。
     */
    private static final char DELIMITER = Character.MIN_VALUE;

    /**
     * 囲み文字。固定長ファイルは「'\u0000'」で固定。
     */
    private static final char ENCLOSE_CHAR = Character.MIN_VALUE;

    /**
     * コンストラクタ。
     * @param fileName ファイル名
     * @param clazz パラメータクラス
     * @param columnFormatterrMap テキスト取得ルール
     */
    public FixedFileLineWriter(String fileName, Class<T> clazz,
            Map<String, ColumnFormatter> columnFormatterrMap) {

        super(fileName, clazz, columnFormatterrMap);

        FileFormat fileFormat = clazz.getAnnotation(FileFormat.class);

        // 区切り文字が初期値以外の場合、例外をスローする。
        if (fileFormat.delimiter() != ',') {
            throw new FileException("Delimiter can not change.",
                    new IllegalStateException(), fileName);
        }

        // 囲み文字が初期値以外の場合、例外をスローする。
        if (fileFormat.encloseChar() != ENCLOSE_CHAR) {
            throw new FileException("EncloseChar can not change.",
                    new IllegalStateException(), fileName);
        }

        // 行区切り文字を設定する。
        // 固定長の場合のみ改行無しを許可するため、superクラスでの設定を上書きする。
        setLineFeedChar(fileFormat.lineFeedChar());

        // 初期化処理
        super.init();
    }

    /**
     * 対象カラムに対するバイト数チェックを行うかを返す。<br>
     * FixedFileLineWriterは固定長のため、必ずチェックを行われるようにtrueを返却する。<br>
     * 不具合予防のため、行単位のバイト数チェックは行わない。
     * @param outputFileColumn 対象カラムのOutputFileColumn情報
     * @return true
     */
    @Override
    protected boolean isCheckByte(OutputFileColumn outputFileColumn) {

        return true;
    }

    /**
     * 対象カラムに対するバイト数チェックを行うかを返す。<br>
     * FixedFileLineWriterは固定長のため、必ずチェックを行われるようにtrueを返却する。<br>
     * 不具合予防のため、行単位のバイト数チェックは行わない。
     * @param columnByte 対象カラムのバイト数
     * @return true
     */
    @Override
    protected boolean isCheckByte(int columnByte) {

        return true;
    }

    /**
     * 囲み文字が設定されていない事をチェックするかを返す。<br>
     * FixedFileLineWriterは固定長のため、囲み文字は設定しない。<br>
     * そのため囲み文字が設定されていない事をチェックする。
     * @return チェックを行う場合はtrue。
     */
    @Override
    protected boolean isCheckEncloseChar() {
        return true;
    }

    /**
     * 区切り文字を取得する。<br>
     * 固定長ファイルは「'\u0000'」で固定。
     * @return 区切り文字
     */
    public char getDelimiter() {

        return DELIMITER;
    }

    /**
     * 囲み文字を取得する。<br>
     * 固定長ファイルは「'\u0000'」で固定。
     * @return 囲み文字
     */
    public char getEncloseChar() {

        return ENCLOSE_CHAR;
    }

}
