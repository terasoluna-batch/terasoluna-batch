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

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import jp.terasoluna.fw.file.dao.FileLineException;

/**
 * ファイル行オブジェクトを用いないファイル読込機能。
 * <p>
 * テキストファイルから1行分のデータを読み取り文字列として呼出元に返却する。 他のファイルアクセス機能とは異なり、ファイル行オブジェクトを使わない。
 * </p>
 * <b>※利用するファイル行オブジェクトのアノテーション項目</b><br>
 * ⅰ．@{@link jp.terasoluna.fw.file.annotation.FileFormat}の設定項目<br>
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
 * <b>※注意事項</b><br>
 * <ul>
 * 　
 * <li>区切り文字と囲み文字の設定は無視する。</li>
 * </ul>
 */
public class PlainFileLineIterator extends AbstractFileLineIterator<Object> {

    /**
     * 区切り文字。
     */
    private char delimiter = ',';

    /**
     * 囲み文字。
     */
    private char encloseChar = Character.MIN_VALUE;

    /**
     * 現在ファイル入力処理済みのデータ部内の行数。
     */
    private int currentLineCount = 0;

    /**
     * トレイラ部処理確認用フラグ。
     */
    private boolean readTrailer = false;

    /**
     * コンストラクタ。
     * @param fileName ファイル名
     * @param clazz 結果クラス
     * @param columnParserMap フォーマット処理リスト
     */
    @SuppressWarnings("unchecked")
    public PlainFileLineIterator(String fileName, Class clazz,
            Map<String, ColumnParser> columnParserMap) {
        super(fileName, clazz, columnParserMap);

        // 初期化処理を行う。
        super.init();
    }

    /**
     * 文字列分割処理。
     * <p>
     * データ部のデータ１行分をファイル行オブジェクトのアノテーションの記述に従いカラムに分割する。<br>
     * このクラスでは処理は実装されていないため、<code>UnsupportedOperationException</code>がスローされる。
     * </p>
     * @param fileLineString データ部のデータ１行分
     * @return データ部１行の文字列を分解した文字配列
     */
    @Override
    public String[] separateColumns(String fileLineString) {
        throw new UnsupportedOperationException(
                "separateColumns(String) isn't supported.");
    }

    /**
     * 繰り返し処理でファイル行オブジェクトを返却する。
     * <p>
     * 次の行のレコードの情報をファイル行オブジェクトに格納して返却する。<br>
     * </p>
     * @return ファイルの１行分の文字列
     */
    @Override
    public String next() {
        if (readTrailer) {
            throw new FileLineException(
                    "Data part should be called before trailer part.",
                    new IllegalStateException(), getFileName(),
                    currentLineCount);
        }
        if (!hasNext()) {
            throw new FileLineException(
                    "The data which can be acquired doesn't exist.",
                    new NoSuchElementException(), getFileName(),
                    currentLineCount + 1, null, -1);
        }
        currentLineCount++;
        return readLine();
    }

    /**
     * データ部のデータを読み飛ばす処理を行う。<br>
     * @param skipLines 読み飛ばす行数。
     */
    public void skip(int skipLines) {
        for (int i = 0; i < skipLines; i++) {
            if (!hasNext()) {
                throw new FileLineException(
                        "The data which can be acquired doesn't exist.",
                        new NoSuchElementException(), getFileName(),
                        currentLineCount + 1);
            }
            readLine();
            currentLineCount++;
        }
    }

    /**
     * トレイラ部のデータを取得する。<br>
     * @return トレイラ部の文字列リスト
     * @see jp.terasoluna.fw.file.dao.standard.AbstractFileLineIterator#getTrailer()
     */
    @Override
    public List<String> getTrailer() {
        List<String> trailer = super.getTrailer();
        readTrailer = true;
        return trailer;
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

    /**
     * 現在ファイル入力処理済みのデータ部内の行数を取得する。
     * @return 現在ファイル入力処理済みのデータ部内の行数。
     */
    @Override
    public int getCurrentLineCount() {
        return currentLineCount;
    }

    /**
     * ファイル行オブジェクトにアノテーションが設定されている事をチェックするかどうかを返す。<br>
     * PlainFileLineIteratorではチェックを行わないため、false。
     * @return false
     */
    @Override
    protected boolean isCheckColumnAnnotationCount() {
        return false;
    }
}
