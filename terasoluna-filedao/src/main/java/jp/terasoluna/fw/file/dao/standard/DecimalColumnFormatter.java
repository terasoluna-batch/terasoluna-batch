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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * カラムフォーマット(ファイル書込）を行うクラス。
 * <p>
 * ファイル行オブジェクトからデータを取得し、文字列をFileUpdateDAOに返却する。 アノテーションの記述に従いDate型のフォーマット処理を行う。
 * </p>
 */
public class DecimalColumnFormatter implements ColumnFormatter {

    /**
     * 数値フォーマットに対応する<code>DecimalFormat</code>を保持するマップ。
     */
    private Map<String, DecimalFormatLocal> dfMap = new ConcurrentHashMap<String, DecimalFormatLocal>();

    /**
     * BigDecimal型のフォーマット処理を行い、文字列を返却する。
     * <p>
     * 引数<code>columnFormat</code>が<code>null</code>もしくは、 空文字だった場合は、フォーマットをせず文字列として返却する。
     * @param t ファイル行オブジェクト
     * @param method カラムフォーマットを行う属性のゲッタメソッド
     * @param columnFormat カラムフォーマット用の文字列
     * @return 文字列
     * @throws IllegalArgumentException ファイル行オブジェクトのgetterメソッドのアクセスに失敗したとき
     * @throws IllegalAccessException ファイル行オブジェクトへの設定が失敗したとき
     * @throws InvocationTargetException ファイル行オブジェクトのメソッドが例外をスローしたとき
     */
    @Override
    public String format(Object t, Method method, String columnFormat)
                                                                      throws IllegalArgumentException,
                                                                      IllegalAccessException,
                                                                      InvocationTargetException {

        Object column = method.invoke(t);

        // カラム値がnullの場合は空文字を返す。
        if (column == null) {
            return "";
        }

        // 数値のフォーマット
        if (columnFormat != null && !"".equals(columnFormat)) {
            DecimalFormatLocal dfLocal = dfMap.get(columnFormat);
            if (dfLocal == null) {
                dfLocal = new DecimalFormatLocal(columnFormat);
                dfMap.put(columnFormat, dfLocal);
            }
            return dfLocal.get().format(column);
        } else {
            return BigDecimal.class.cast(column).toPlainString();
        }

    }
}
