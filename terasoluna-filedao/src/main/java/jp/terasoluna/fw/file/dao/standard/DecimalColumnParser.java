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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数値文字列のためのカラムパーサークラス。
 * <p>
 * 指定された文字列をパースし、BigDecimal型に変換する。 変換結果をファイル行オブジェクトのBigDecimal型の属性に値を格納する。
 * </p>
 */
public class DecimalColumnParser implements ColumnParser {

    /**
     * 数値フォーマットに対応する<code>DecimalFormat</code>を保持するマップ。
     */
    private Map<String, DecimalFormatLocal> dfMap = new ConcurrentHashMap<String, DecimalFormatLocal>();

    /**
     * 指定された文字列をパースし、BigDecimal型に変換する。変換結果をファイル行オブジェクトに格納する。
     * @param column カラムの文字列
     * @param t ファイル行オブジェクト
     * @param method カラムの文字列をファイル行オブジェクトに格納するメソッド
     * @param columnFormat パースする際のフォーマット文字列
     * @throws IllegalArgumentException フォーマット文字列がフォーマットとして不正であるとき
     * @throws IllegalAccessException ファイル行オブジェクトへの設定が失敗したとき
     * @throws InvocationTargetException ファイル行オブジェクトのメソッドが例外をスローしたとき
     * @throws ParseException パース処理が失敗したとき
     */
    public void parse(String column, Object t, Method method,
            String columnFormat) throws IllegalArgumentException,
                                IllegalAccessException,
                                InvocationTargetException, ParseException {

        // 数値のパース
        if (columnFormat != null && !"".equals(columnFormat)) {
            DecimalFormatLocal dfLocal = dfMap.get(columnFormat);
            if (dfLocal == null) {
                dfLocal = new DecimalFormatLocal(columnFormat);
                dfMap.put(columnFormat, dfLocal);
            }
            DecimalFormat decimalFormat = dfLocal.get();
            decimalFormat.setParseBigDecimal(true);
            method.invoke(t, decimalFormat.parse(column));
        } else {
            method.invoke(t, new BigDecimal(column));
        }
    }
}
