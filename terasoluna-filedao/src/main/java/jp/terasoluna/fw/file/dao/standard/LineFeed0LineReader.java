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
public class LineFeed0LineReader implements LineReader {

    /**
     * ファイルアクセス用の文字ストリーム。
     */
    private Reader reader = null;

    /**
     * ファイルエンコーディング
     */
    private String fileEncoding = null;

    /**
     * 1行分のバイト数
     */
    private int totalBytes = 0;

    /**
     * コンストラクタ。
     * @param reader ファイルアクセス用の文字ストリーム
     * @param fileEncoding ファイルエンコーディング
     * @param totalBytes 1行分のバイト数
     * @throws IllegalArgumentException 引数の設定が間違った場合。
     */
    public LineFeed0LineReader(Reader reader, String fileEncoding,
            int totalBytes) {

        if (reader == null) {
            throw new IllegalArgumentException("reader is required.");
        }

        if (fileEncoding == null) {
            throw new IllegalArgumentException("fileEncoding is required.");
        }

        if (totalBytes <= 0) {
            throw new IllegalArgumentException("totalBytes is larger than 0.");
        }

        this.reader = reader;
        this.fileEncoding = fileEncoding;
        this.totalBytes = totalBytes;
    }

    /**
     * ファイルからデータ部のデータを1行分読み取り、文字列として呼出元に返却する。
     * @return データ部の１行分の文字列
     * @throws FileException Readerの処理で例外が発生した場合。
     */
    public String readLine() {
        StringBuilder currentLineStringBuilder = new StringBuilder();

        int chr = 0;
        int size = 0;

        try {
            while ((chr = reader.read()) != -1) {
                char currentChar = (char) chr;
                currentLineStringBuilder.append(currentChar);

                size += Character.toString(currentChar).getBytes(fileEncoding).length;

                if (size >= totalBytes) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new FileException("Reader control operation was failed.", e);
        }

        return currentLineStringBuilder.toString();
    }

}
