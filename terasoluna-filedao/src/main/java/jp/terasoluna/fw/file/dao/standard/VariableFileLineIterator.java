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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.dao.FileException;

import org.apache.commons.lang3.StringUtils;

/**
 * 可変長ファイルファイル用のファイルアクセス(データ取得)クラス。
 * <p>
 * 可変長ファイルからデータを読み込み、1行分のデータをファイル行オブジェクトに 格納する。<br>
 * CSVファイルでは区切り文字がカンマで固定されているが、可変長ファイルでは カンマ以外を利用することが可能。
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
 * <td> <code>区切り文字</code></td>
 * <td> <code>delimiter</code></td>
 * <td> <code>','</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * <tr>
 * <td> <code>囲み文字</code></td>
 * <td> <code>encloseChar</code></td>
 * <td> <code>なし('\u0000')</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * <tr>
 * <td> <code>ファイルエンコーディング</code></td>
 * <td> <code>fileEncodeing</code></td>
 * <td> <code>システムのファイルエンコーディング</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * <tr>
 * <td> <code>ヘッダ行数</code></td>
 * <td> <code>headerLineCount</code></td>
 * <td> <code>0</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * <tr>
 * <td> <code>トレイラ行数</code></td>
 * <td> <code>trailerLineCount</code></td>
 * <td> <code>0</code></td>
 * <td> <code>オプション</code></td>
 * </tr>
 * </table>
 * </div> <br>
 * ⅱ．@{@link jp.terasoluna.fw.file.annotation.InputFileColumn}、@{@link jp.terasoluna.fw.file.annotation.OutputFileColumn}の設定項目<br>
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
 * <li>区切り文字にCaracter.MIN_VALUEを設定することは出来ない。(エラー発生)</li>
 * </ul>
 * @param <T> ファイル行オブジェクト
 */
public class VariableFileLineIterator<T> extends AbstractFileLineIterator<T> {

    /**
     * 区切り文字。
     */
    private char delimiter = ',';

    /**
     * 囲み文字。
     */
    private char encloseChar = Character.MIN_VALUE;

    /**
     * コンストラクタ。
     * @param fileName ファイル名
     * @param clazz ファイル行オブジェクトクラス
     * @param columnParserMap テキスト設定ルール
     */
    public VariableFileLineIterator(String fileName, Class<T> clazz,
            Map<String, ColumnParser> columnParserMap) {

        super(fileName, clazz, columnParserMap);

        FileFormat fileFormat = clazz.getAnnotation(FileFormat.class);

        // 区切り文字がCharacter.MIN_VALUEの場合、例外をスローする。
        if (fileFormat.delimiter() == Character.MIN_VALUE) {
            throw new FileException("Delimiter can not use '\\u0000'.",
                    new IllegalStateException(), fileName);
        }

        // 改行文字内に区切り文字が含まれている場合、例外をスローする。
        if (fileFormat.lineFeedChar().indexOf(fileFormat.delimiter()) >= 0) {
            throw new FileException(
                    "delimiter is the same as lineFeedChar and is no use.",
                    new IllegalStateException(), fileName);
        }

        // 囲み文字を設定する。
        this.encloseChar = fileFormat.encloseChar();

        // 区切り文字を設定する。
        this.delimiter = fileFormat.delimiter();

        // 初期化処理を行う。
        super.init();
    }

    /**
     * 読み込んだファイルのレコードを、区切り文字、 囲み文字に従って 文字配列に変換する。<br>
     * 引数<code>fileLineString</code>が<code>null</code>もしくは 空文字の場合は、要素を持たない<code>String</code>配列を返します。
     * @param fileLineString 可変長ファイルの1レコード分の文字列
     * @return 文字配列
     */
    @Override
    protected String[] separateColumns(String fileLineString) {

        if (fileLineString == null || "".equals(fileLineString)) {
            return new String[0];
        }

        // 1カラム分の文字列を格納する文字シーケンス
        StringBuilder columnBuilder = new StringBuilder();

        // チェック対象文字の直前の文字
        char previousChar = Character.MIN_VALUE;

        // 文字列を格納するための配列
        List<String> columnList = new ArrayList<String>();

        boolean isEnclosed = true;
        boolean isEscaped = false;

        int fieldCount = 0;
        char[] columnEncloseChar = getColumnEncloseChar();

        if (!isEnclosed()) {
            return StringUtils.splitByWholeSeparator(fileLineString, Character.toString(delimiter));
        } else {
            for (char currentChar : fileLineString.toCharArray()) {
                if (previousChar == Character.MIN_VALUE) {
                    previousChar = currentChar;
                }
                if (previousChar == getEncloseCharcter(columnEncloseChar,
                        fieldCount)) {
                    if (isEnclosed) {
                        if (currentChar == getEncloseCharcter(
                                columnEncloseChar, fieldCount)) {
                            isEnclosed = false;
                        }
                    } else {
                        if (currentChar == getEncloseCharcter(
                                columnEncloseChar, fieldCount)) {
                            if (isEscaped) {
                                columnBuilder.append(currentChar);
                                isEscaped = false;
                            } else {
                                isEscaped = true;
                            }
                        } else if (currentChar == getDelimiter()) {
                            if (isEscaped) {
                                columnList.add(columnBuilder.toString());
                                previousChar = Character.MIN_VALUE;
                                columnBuilder.delete(0, columnBuilder.length());
                                isEnclosed = true;
                                isEscaped = false;
                                fieldCount++;
                            } else {
                                columnBuilder.append(currentChar);
                                isEscaped = false;
                            }
                        } else {
                            columnBuilder.append(currentChar);
                        }
                    }
                } else {
                    if (currentChar != getDelimiter()) {
                        columnBuilder.append(currentChar);
                    } else {
                        columnList.add(columnBuilder.toString());
                        previousChar = Character.MIN_VALUE;
                        columnBuilder.delete(0, columnBuilder.length());
                        fieldCount++;
                    }
                }
            }
            columnList.add(columnBuilder.toString());
            return columnList.toArray(new String[columnList.size()]);
        }
    }

    /**
     * カラムに対応する囲み文字を取得する。
     * @param index カラムのインデックス
     * @return 囲み文字
     */
    private char getEncloseCharcter(char[] columnEncloseChar, int index) {
        if (columnEncloseChar.length == 0 || index >= columnEncloseChar.length) {
            return this.encloseChar;
        } else {
            return columnEncloseChar[index];
        }
    }

    /**
     * 区切り文字を取得する。
     * @return 区切り文字
     */
    @Override
    public char getDelimiter() {

        return delimiter;
    }

    /**
     * 囲み文字を取得する。
     * @return 囲み文字
     */
    @Override
    public char getEncloseChar() {

        return encloseChar;
    }
}
