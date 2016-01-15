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
public class EncloseCharLineFeed1LineReader implements LineReader {

    /**
     * 区切り文字。
     */
    private char delimiterCharacter = Character.MIN_VALUE;

    /**
     * 囲み文字。
     */
    private char encloseCharacter = Character.MIN_VALUE;

    /**
     * カラムごとの囲み文字。
     */
    private char[] columnEncloseCharacter = null;

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
     * @param delimiterCharacter 区切り文字
     * @param encloseCharacter 囲み文字
     * @param columnEncloseCharacter カラムごとの囲み文字
     * @param reader ファイルアクセス用の文字ストリーム
     * @param lineFeedChar 行区切り文字
     * @throws IllegalArgumentException 引数の設定が間違った場合。
     */
    public EncloseCharLineFeed1LineReader(char delimiterCharacter,
            char encloseCharacter, char[] columnEncloseCharacter,
            Reader reader, String lineFeedChar) {

        if (delimiterCharacter == Character.MIN_VALUE) {
            throw new IllegalArgumentException(
                    "delimiterCharacter can not use '\\u0000'.");
        }

        if (columnEncloseCharacter == null) {
            throw new IllegalArgumentException(
                    "columnEncloseCharacter is required.");
        }

        if (reader == null) {
            throw new IllegalArgumentException("reader is required.");
        }

        if (lineFeedChar == null) {
            throw new IllegalArgumentException("lineFeedChar is required.");
        }

        if (lineFeedChar.length() != 1) {
            throw new IllegalArgumentException("lineFeedChar should be defined"
                    + " by 1 digit of character string.");
        }

        this.delimiterCharacter = delimiterCharacter;
        this.encloseCharacter = encloseCharacter;
        this.columnEncloseCharacter = columnEncloseCharacter;
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
        // 1カラム分の文字列を格納するバッファ
        StringBuilder currentLineStringBuilder = new StringBuilder();

        // チェック対象文字の直前の文字
        char previousChar = Character.MIN_VALUE;

        // チェック対象文字
        char currentChar = Character.MIN_VALUE;

        // 囲み文字が終了しているか確認するフラグ。trueならカラムは囲み文字で囲まれている。
        boolean isEnclosed = true;

        // エスケープシーケンスを読み込んだらtrueに。それ以外の場合はfalse。
        boolean isEscape = false;

        // 行区切り文字の1文字目。
        char lineFeedChar1 = lineFeedChar.charAt(0);

        int chr = 0;
        int fieldCount = 0;
        try {
            while ((chr = reader.read()) != -1) {
                currentChar = (char) chr;
                if (previousChar == Character.MIN_VALUE) {
                    previousChar = currentChar;
                }
                if (previousChar == getEncloseCharcter(fieldCount)) {
                    if (isEnclosed) {
                        if (currentChar == getEncloseCharcter(fieldCount)) {
                            isEnclosed = false;
                        }
                        currentLineStringBuilder.append(currentChar);
                    } else {
                        if (currentChar == getEncloseCharcter(fieldCount)
                                && !isEscape) {
                            isEscape = true;
                        } else if (currentChar == getEncloseCharcter(fieldCount)
                                && isEscape) {
                            isEscape = false;
                            currentLineStringBuilder
                                    .append(getEncloseCharcter(fieldCount));
                            currentLineStringBuilder.append(currentChar);
                        } else if (currentChar == delimiterCharacter) {
                            if (isEscape) {
                                currentLineStringBuilder
                                        .append(getEncloseCharcter(fieldCount));
                                previousChar = Character.MIN_VALUE;
                                isEnclosed = true;
                                isEscape = false;
                                fieldCount++;
                            }
                            currentLineStringBuilder.append(currentChar);
                        } else if (currentChar == lineFeedChar1) {
                            if (isEscape) {
                                currentLineStringBuilder
                                        .append(getEncloseCharcter(fieldCount));
                                previousChar = Character.MIN_VALUE;
                                isEnclosed = true;
                                isEscape = false;
                                break;
                            } else {
                                currentLineStringBuilder.append(currentChar);
                            }
                        } else {
                            if (isEscape) {
                                currentLineStringBuilder
                                        .append(getEncloseCharcter(fieldCount));
                                previousChar = Character.MIN_VALUE;
                                isEnclosed = true;
                                isEscape = false;
                            }
                            currentLineStringBuilder.append(currentChar);
                        }
                    }
                } else if (previousChar == lineFeedChar1) {
                    previousChar = Character.MIN_VALUE;
                    isEnclosed = true;
                    isEscape = false;
                    break;
                } else {
                    if (currentChar == delimiterCharacter) {
                        fieldCount++;
                        currentLineStringBuilder.append(currentChar);
                        previousChar = Character.MIN_VALUE;
                        isEnclosed = true;
                        isEscape = false;
                    } else if (currentChar == lineFeedChar1) {
                        previousChar = Character.MIN_VALUE;
                        isEnclosed = true;
                        isEscape = false;
                        break;
                    } else {
                        currentLineStringBuilder.append(currentChar);
                    }
                }
            }
        } catch (IOException e) {
            throw new FileException("Reader control operation was failed.", e);
        }

        return currentLineStringBuilder.toString();
    }

    /**
     * カラムに対応する囲み文字を取得する。
     * @param index カラムのインデックス
     * @return 囲み文字
     */
    private char getEncloseCharcter(int index) {
        if (columnEncloseCharacter.length == 0
                || index >= columnEncloseCharacter.length) {
            return encloseCharacter;
        } else {
            return columnEncloseCharacter[index];
        }
    }
}
