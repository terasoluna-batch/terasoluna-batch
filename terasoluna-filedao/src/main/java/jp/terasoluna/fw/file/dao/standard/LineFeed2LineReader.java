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
import java.io.Reader;

import jp.terasoluna.fw.file.dao.FileException;

/**
 * ファイルからデータ部のデータを1行分読み取り、文字列として呼出元に返却する。
 */
public class LineFeed2LineReader implements LineReader {

    /**
     * ファイルアクセス用の文字ストリーム。
     */
    private Reader reader = null;

    /**
     * 行区切り文字。
     */
    private String lineFeedChar = null;

    /**
     * コンストラクタ。
     * @param reader ファイルアクセス用の文字ストリーム
     * @param lineFeedChar 行区切り文字
     * @throws IllegalArgumentException 引数の設定が間違った場合。
     */
    public LineFeed2LineReader(Reader reader, String lineFeedChar) {

        if (reader == null) {
            throw new IllegalArgumentException("reader is required.");
        }

        if (lineFeedChar == null) {
            throw new IllegalArgumentException("lineFeedChar is required.");
        }

        if (lineFeedChar.length() != 2) {
            throw new IllegalArgumentException("lineFeedChar should be defined"
                    + " by 2 digit of character string.");
        }

        this.reader = reader;
        this.lineFeedChar = lineFeedChar;
    }

    /**
     * ファイルからデータ部のデータを1行分読み取り、文字列として呼出元に返却する。
     * @return データ部の１行分の文字列
     * @throws FileException Readerの処理で例外が発生した場合。
     */
    @Override
    public String readLine() {
        StringBuilder currentLineStringBuilder = new StringBuilder();

        // チェック対象文字の直前の文字
        char previousChar = Character.MIN_VALUE;

        // チェック対象文字
        char currentChar = Character.MIN_VALUE;

        // 行区切り文字の1文字目。
        char lineFeedChar1 = lineFeedChar.charAt(0);

        // 行区切り文字の2文字目。
        char lineFeedChar2 = lineFeedChar.charAt(1);

        int chr = 0;
        try {
            while ((chr = reader.read()) != -1) {
                currentChar = (char) chr;
                if (currentChar == lineFeedChar2) {
                    if (previousChar == lineFeedChar1) {
                        currentLineStringBuilder.delete(
                                (currentLineStringBuilder.length() - 1),
                                (currentLineStringBuilder.length()));
                        break;
                    }
                }
                previousChar = currentChar;
                currentLineStringBuilder.append(currentChar);
            }
        } catch (IOException e) {
            throw new FileException("Reader control operation was failed.", e);
        }
        return currentLineStringBuilder.toString();
    }

}
