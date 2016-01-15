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

import java.io.UnsupportedEncodingException;
import java.util.Map;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;
import jp.terasoluna.fw.file.dao.FileException;

/**
 * 固定長ファイル用のファイルアクセス(データ取得)クラス。
 * <p>
 * 固定長ファイルからデータを読み込み、 1行分のデータをファイル行オブジェクトに格納する。
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
 * <tr>
 * <tr>
 * <td> <code>ファイルエンコーディング</code></td>
 * <td> <code>fileEncodeing</code></td>
 * <td> <code>システムのファイルエンコーディング</code></td>
 * <td> <code>オプション</code></td>
 * <tr>
 * <tr>
 * <td> <code>ヘッダ行数</code></td>
 * <td> <code>headerLineCount</code></td>
 * <td> <code>0</code></td>
 * <td> <code>オプション</code></td>
 * <tr>
 * <tr>
 * <td> <code>トレイラ行数</code></td>
 * <td> <code>trailerLineCount</code></td>
 * <td> <code>0</code></td>
 * <td> <code>オプション</code></td>
 * <tr>
 * </table>
 * </div> <br>
 * ⅱ．@{@link InputFileColumn}、@{@link jp.terasoluna.fw.file.annotation.OutputFileColumn}の設定項目<br>
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
public class FixedFileLineIterator<T> extends AbstractFileLineIterator<T> {

    /**
     * 区切り文字。固定長ファイルは「,(カンマ)」で固定。
     */
    private static final char DELIMITER = ',';

    /**
     * 囲み文字。固定長ファイルは「'\u0000'」で固定。
     */
    private static final char ENCLOSE_CHAR = Character.MIN_VALUE;

    /**
     * コンストラクタ。
     * <p>
     * 区切り文字が初期値以外の場合、 囲み文字が初期値以外の場合は<code>FileException</code>がスローされる。
     * @param fileName ファイル名
     * @param clazz 結果クラス
     * @param columnParserMap フォーマット処理リスト
     */
    public FixedFileLineIterator(String fileName, Class<T> clazz,
            Map<String, ColumnParser> columnParserMap) {

        super(fileName, clazz, columnParserMap);

        FileFormat fileFormat = clazz.getAnnotation(FileFormat.class);

        // 区切り文字が初期値以外の場合、例外をスローする。
        if (fileFormat.delimiter() != DELIMITER) {
            throw new FileException("Delimiter can not change.",
                    new IllegalStateException(), fileName);
        }

        // 囲み文字が初期値以外の場合、例外をスローする。
        if (fileFormat.encloseChar() != ENCLOSE_CHAR) {
            throw new FileException("EncloseChar can not change.",
                    new IllegalStateException(), fileName);
        }

        // 行区切り文字が無い場合、ヘッダ・トレイラは利用不可なので例外をスローする。
        if ("".equals(fileFormat.lineFeedChar())
                && (fileFormat.headerLineCount() > 0 || fileFormat
                        .trailerLineCount() > 0)) {
            throw new FileException("HeaderLineCount or trailerLineCount cannot be used.",
                    new IllegalStateException(), fileName);
        }
        
        // 行区切り文字を設定する。
        // 固定長の場合のみ改行無しを許可するため、superクラスでの設定を上書きする。
        setLineFeedChar(fileFormat.lineFeedChar());

        // 初期化処理を行う。
        super.init();
    }

    /**
     * 読み込んだ固定長のレコードをアノテーションのbyte数、 columnIndexに従って分解する。<br>
     * 引数<code>fileLineString</code>が<code>null</code>もしくは 空文字の場合は、要素を持たない<code>String</code>配列を返します。
     * <p>
     * 処理の順序は、<br>
     * <ul>
     * <li>ファイル行オブジェクトで定義したバイト数の合計とファイルから読み取った1行あたりのバイト数を比較する</li>
     * <li>ファイル行オブジェクトのアノテーションで定義したbytes数分の文字列を生成する</li>
     * </ul>
     * <p>
     * @param fileLineString 固定長ファイルの1レコード分の文字列
     * @return データ部１行の文字列を分解した文字配列
     */
    @Override
    protected String[] separateColumns(String fileLineString) {

        // レコード文字列がnullか空文字の場合は要素０の配列を返却
        if (fileLineString == null || "".equals(fileLineString)) {
            return new String[0];
        }

        int[] columnBytes = getColumnBytes();
        String[] results = new String[columnBytes.length];

        // ファイル行オブジェクトで定義したバイト数の合計とファイルから読み取った1行あたりのバイト数を比較する
        try {
            // ファイルから読み取った1行あたりのバイト数と、ファイル行オブジェクトで
            // 定義したバイト数の合計を比較する。
            byte[] bytes = fileLineString.getBytes(getFileEncoding());
            if (getTotalBytes() != bytes.length) {
                throw new FileException("Total Columns byte is different "
                        + "from Total FileLineObject's columns byte.",
                        new IllegalStateException(), this.getFileName());
            }

            // 　ファイル行オブジェクトのアノテーションで定義したbytes数分の文字列を生成する
            int byteIndex = 0;

            for (int i = 0; i < columnBytes.length; i++) {
                results[i] = new String(bytes, byteIndex, columnBytes[i],
                        getFileEncoding());
                byteIndex += columnBytes[i];
            }
        } catch (UnsupportedEncodingException e) {
            throw new FileException(
                    "fileEncoding which isn't supported was set.", e, this
                            .getFileName());
        }

        return results;
    }

    /**
     * 対象カラムに対するバイト数チェックを行うかを返す。<br>
     * FixedFileLineIteratorは固定長のため、行単位でバイト数チェックを行う。<br>
     * そのためカラムに対するバイト数チェックは行わない。
     * @param inputFileColumn 対象カラムのInputFileColumn情報
     * @return false
     */
    @Override
    protected boolean isCheckByte(InputFileColumn inputFileColumn) {
        return false;
    }

    /**
     * 対象カラムに対するバイト数チェックを行うかを返す。<br>
     * FixedFileLineIteratorは固定長のため、行単位でバイト数チェックを行う。<br>
     * そのためカラムに対するバイト数チェックは行わない。
     * @param columnByte 対象カラムのバイト数
     * @return false
     */
    @Override
    protected boolean isCheckByte(int columnByte) {
        return false;
    }

    /**
     * 囲み文字が設定されていない事をチェックするかを返す。<br>
     * FixedFileLineIteratorは固定長のため、囲み文字は設定しない。<br>
     * そのため囲み文字が設定されていない事をチェックする。
     * @return チェックを行う場合はtrue。
     */
    @Override
    protected boolean isCheckEncloseChar() {
        return true;
    }

    /**
     * 区切り文字を取得する。<br>
     * 固定長ファイルは「,(カンマ)」で固定。
     * @return 行区切り文字
     */
    @Override
    public char getDelimiter() {

        return DELIMITER;
    }

    /**
     * 囲み文字を取得する。<br>
     * 固定長ファイルは「'\u0000'」で固定。
     * @return 囲み文字
     */
    @Override
    public char getEncloseChar() {

        return ENCLOSE_CHAR;
    }
}
