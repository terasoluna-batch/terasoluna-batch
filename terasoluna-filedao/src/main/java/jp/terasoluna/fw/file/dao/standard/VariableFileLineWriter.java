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
import jp.terasoluna.fw.file.dao.FileException;

/**
 * 可変長ファイル用のファイルアクセス(データ書込)クラス。
 * <p>
 * ファイル行オブジェクトからデータを読み込み、1行分のデータを可変長形式で ファイルに書き込む。
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
 * <td> <code>ファイル上書きフラグ</code></td>
 * <td> <code>overWriteFlg</code></td>
 * <td> <code>false</code></td>
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
public class VariableFileLineWriter<T> extends AbstractFileLineWriter<T> {

    /**
     * 区切り文字。可変長ファイル出力の場合、デフォルトは「,(カンマ)」。
     */
    private char delimiter = ',';

    /**
     * 囲み文字。
     */
    private char encloseChar = Character.MIN_VALUE;

    /**
     * コンストラクタ。
     * @param fileName ファイル名
     * @param clazz パラメータクラス
     * @param columnFormatterMap テキスト取得ルール
     */
    public VariableFileLineWriter(String fileName, Class<T> clazz,
            Map<String, ColumnFormatter> columnFormatterMap) {

        super(fileName, clazz, columnFormatterMap);

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

        // 区切り文字を設定する。
        delimiter = fileFormat.delimiter();

        // 囲み文字を設定する。
        encloseChar = fileFormat.encloseChar();

        // 初期化処理
        super.init();
    }

    /**
     * <p>
     * ファイル行オブジェクトからカラムインデックスと一致する属性の値を取得する。
     * </p>
     * <p>
     * 囲み文字がデータ中にある場合は、同一囲み文字を付加してエスケープ文字とする。
     * </p>
     * @param t ファイル行オブジェクト
     * @param index カラムのインデックス
     * @return カラムの文字列
     */
    @Override
    protected String getColumn(T t, int index) {
        String columnString = super.getColumn(t, index);
        // エスケープ処理
        if (getColumnEncloseChar()[index] == Character.MIN_VALUE) {
            return columnString;
        } else {
            // エスケープ編集用データ
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < columnString.length(); i++) {
                char columnChar = columnString.charAt(i);
                builder.append(columnChar);
                if (getColumnEncloseChar()[index] == columnChar) {
                    builder.append(columnChar);
                }
            }
            return builder.toString();
        }
    }

    /**
     * 区切り文字を取得する。
     * @return 区切り文字
     */
    public char getDelimiter() {

        return delimiter;
    }

    /**
     * 囲み文字を取得する。
     * @return 囲み文字
     */
    public char getEncloseChar() {

        return encloseChar;
    }

}
