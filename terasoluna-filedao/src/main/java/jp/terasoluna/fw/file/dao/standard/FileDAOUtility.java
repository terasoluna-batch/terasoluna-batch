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
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.terasoluna.fw.file.annotation.PaddingType;
import jp.terasoluna.fw.file.annotation.TrimType;
import jp.terasoluna.fw.file.dao.FileException;

/**
 * FileDAO用のユーティリティ。
 * <p>
 * パディング処理、トリム処理を提供する。
 * </p>
 */
public class FileDAOUtility {

    /**
     * ファイルエンコーディングのキャッシュ。
     */
    private static final Map<String, Map<Character, Boolean>> encodingCache = new ConcurrentHashMap<String, Map<Character, Boolean>>();

    /**
     * パディング処理。<br>
     * <br>
     * カラムの文字列をアノテーションで指定された文字でパディングする。<br>
     * 文字列に追加されるのは、パディング文字「paddingChar」で指定した文字。<br>
     * パディング文字は半角1文字であるので、全角文字が入力された場合は入力エラーとなる。
     * @param columnString パディング処理前の１カラム分の文字列
     * @param fileEncoding ファイルエンコーディング
     * @param columnBytes パディング処理後の1カラムのバイト数
     * @param paddingChar パディング文字
     * @param paddingType パディングタイプ
     * @return パディング処理済の１カラム分の文字列
     */
    public static String padding(String columnString, String fileEncoding,
            int columnBytes, char paddingChar, PaddingType paddingType) {

        // NONEのときはそのまま文字列を返却する
        if (PaddingType.NONE.equals(paddingType)) {
            return columnString;
        }

        // 半角文字の判定
        if (!isHalfWidthChar(fileEncoding, paddingChar)) {
            throw new FileException("Padding char is not half-width character.");
        }

        try {
            // パディング処理後のバイト数より対象文字列が長い場合はパディング処理しない。
            int paddingSize = columnBytes
                    - columnString.getBytes(fileEncoding).length;

            if (paddingSize <= 0) {
                return columnString;
            }

            StringBuilder columnBuilder = new StringBuilder(columnBytes);

            char[] fillChars = new char[paddingSize];
            Arrays.fill(fillChars, paddingChar);

            if (PaddingType.LEFT.equals(paddingType)) {
                columnBuilder.append(fillChars).append(columnString);
                return columnBuilder.toString();

            } else if (PaddingType.RIGHT.equals(paddingType)) {
                columnBuilder.append(columnString).append(fillChars);
                return columnBuilder.toString();

            } else {
                return columnString;
            }
        } catch (UnsupportedEncodingException e) {
            throw new FileException("Specified Encoding : " + fileEncoding
                    + " is not supported", e);
        }
    }

    /**
     * トリム処理。<br>
     * <br>
     * カラムの文字列をアノテーションで指定された文字でトリムする。<br>
     * 文字列から取り除かれるのは、トリム文字「trimChar」で指定した文字。<br>
     * トリム文字は半角1文字であるので、全角文字が入力された場合は入力エラーとなる。
     * @param columnString トリム処理前の１カラム分の文字列
     * @param fileEncoding ファイルエンコーディング
     * @param trimChar トリム文字(半角)
     * @param trimType トリムタイプ
     * @return トリム処理後の１カラム分の文字列
     */
    public static String trim(String columnString, String fileEncoding,
            char trimChar, TrimType trimType) {

        // NONEのときはそのまま文字列を返却する
        if (TrimType.NONE.equals(trimType)) {
            return columnString;
        }

        // 半角文字の判定
        if (!isHalfWidthChar(fileEncoding, trimChar)) {
            throw new FileException("Trim char is not half-width character.");
        }

        int start = 0;
        int length = columnString.length();

        if (TrimType.LEFT.equals(trimType) || TrimType.BOTH.equals(trimType)) {
            while ((start < length) && columnString.charAt(start) == trimChar) {
                start++;
            }
        }
        if (TrimType.RIGHT.equals(trimType) || TrimType.BOTH.equals(trimType)) {
            while ((start < length)
                    && columnString.charAt(length - 1) == trimChar) {
                length--;
            }
        }
        return columnString.substring(start, length);
    }

    /**
     * 半角文字かチェックを行う。
     * @param fileEncoding ファイルエンコーディング
     * @param checkChar チェックを行う文字
     * @return 半角文字の場合にtrueを返却する
     * @throws FileException 存在しないエンコーディングの場合
     */
    private static boolean isHalfWidthChar(String fileEncoding, char checkChar)
                                                                               throws FileException {

        Map<Character, Boolean> cache = encodingCache.get(fileEncoding);
        if (cache == null) {
            cache = new ConcurrentHashMap<Character, Boolean>();
            encodingCache.put(fileEncoding, cache);
        }

        Boolean result = cache.get(checkChar);
        if (result == null) {
            try {
                result = (1 == Character.toString(checkChar).getBytes(
                        fileEncoding).length);
                cache.put(checkChar, result);
            } catch (UnsupportedEncodingException e) {
                throw new FileException("Specified Encoding : " + fileEncoding
                        + " is not supported", e);
            }
        }
        return result.booleanValue();
    }
}
