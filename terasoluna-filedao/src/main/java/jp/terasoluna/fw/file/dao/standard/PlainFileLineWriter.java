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

import java.io.IOException;
import java.util.Map;

import jp.terasoluna.fw.file.dao.FileException;

/**
 * ファイル行オブジェクトを用いないファイル書込機能。
 * <p>
 * ビジネスロジックなどから受け取った文字列をファイルに出力する。 他のファイルアクセス機能とは異なり、ファイル行オブジェクトを使わない。
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
 * <td> <code>ファイル上書きフラグ</code></td>
 * <td> <code>overWriteFlg</code></td>
 * <td> <code>false</code></td>
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
public class PlainFileLineWriter extends AbstractFileLineWriter<String> {

    /**
     * コンストラクタ。
     * @param fileName ファイル名
     * @param clazz パラメータクラス
     * @param columnFormatterMap テキスト取得ルール
     */
    @SuppressWarnings("unchecked")
    public PlainFileLineWriter(String fileName, Class clazz,
            Map<String, ColumnFormatter> columnFormatterMap) {
        super(fileName, clazz, columnFormatterMap);

        // 初期化処理
        super.init();
    }

    /**
     * 引数<code>t</code>の文字列をファイルに書き込む。
     * @param t 文字列
     * @throws NullPointerException <code>t</code>が<code>null</code>の場合
     */
    @Override
    public void printDataLine(String t) {
        checkWriteTrailer();
        try {
            getWriter().write(t);
            getWriter().write(getLineFeedChar());
        } catch (IOException e) {
            throw new FileException("writer control operation was failed.", e,
                    getFileName());
        }
        setWriteData(true);
    }

    /**
     * 区切り文字を取得する。<br>
     * 「0」で固定。
     * @return 区切り文字
     */
    @Override
    public char getDelimiter() {
        return 0;
    }

    /**
     * 囲み文字を取得する。<br>
     * 「0」で固定。
     * @return 囲み文字
     */
    @Override
    public char getEncloseChar() {
        return 0;
    }

    /**
     * ファイル行オブジェクトにアノテーションが設定されている事をチェックするかどうかを返す。<br>
     * PlainFileLineWriterではチェックを行わないため、false。
     * @return false
     */
    @Override
    protected boolean isCheckColumnAnnotationCount() {
        return false;
    }
}
