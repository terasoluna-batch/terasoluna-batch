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

package jp.terasoluna.fw.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 日付・時刻・カレンダー関連のユーティリティクラス。
 *
 */
public class DateUtil {

    /**
     * ログクラス
     */
    private static Log log = LogFactory.getLog(DateUtil.class);

    /**
     * システム時刻を取得する。
     *
     * <p>WebサーバとAPサーバを分離したりクラスタ構成にした場合は、マシンに
     * よりシステム日付が異なる可能性がある。これを避けるため、システム日付
     * 取得には必ずこのメソッドを利用し、必要に応じて特定マシンの日付を取得
     * するなどの措置がとれるようにしておく。</p>
     *
     * @return システム時刻
     */
    public static java.util.Date getSystemTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * java.util.Dateインスタンスを和暦として指定のフォーマットに
     * 変換する。
     *
     * <p>
     *  ApplicationResources.properties
     *  で指定された日付フォーマットを用いて、西暦、和暦の変換を
     *  行うことができる。<br>
     *  下記は、和暦の境界となる日付、和暦名、アルファベット頭文字の
     *  設定例である。
     *  <strong> ApplicationResources.properties による
     *  和暦の設定例</strong><br>
     *  <code><pre>
     *  wareki.gengo.0.name = 平成
     *  wareki.gengo.0.roman = H
     *  wareki.gengo.0.startDate = 1989/01/08
     *  wareki.gengo.1.name = 昭和
     *  wareki.gengo.1.roman = S
     *  wareki.gengo.1.startDate = 1926/12/25
     *  wareki.gengo.2.name = 大正
     *  wareki.gengo.2.roman = T
     *  wareki.gengo.2.startDate = 1912/07/30
     *  wareki.gengo.3.name = 明治
     *  wareki.gengo.3.roman = M
     *  wareki.gengo.3.startDate = 1868/09/04
     *  </pre></code>
     * </p>
     *
     * <strong>フォーマット文字列</strong><br>
     * <p>フォーマットは、java.text.SimpleDateFormat クラスの
     * <i>時刻パターン文字列</i> として解釈されるが、以下のパターン文字の解釈が
     * （デフォルトロケールの） SimpleDateFormat クラスと異なる。
     * </p>
     *
     * <div width="90%" align="center">
     *  <table border="1">
     *   <tr>
     *    <th>記号</th>
     *    <th><code>&nbsp;SimpleDateFormat</code>&nbsp;</th>
     *    <th><code>&nbsp;dateToWarekiString()</code>&nbsp;</th>
     *   </tr>
     *   <tr>
     *    <td>G</td>
     *    <td align="left">紀元<br><br>例：<br>AD</td>
     *    <td align="left">和暦元号<br><br>
     *                     例：<br>
     *                    （4個以上の連続したパターン文字）<br>
     *                     明治、大正、昭和、平成<br>
     *                     （3個以下の連続したパターン文字）<br>
     *                     M、T、S、H</td>
     *   </tr>
     *   <tr>
     *    <td>y</td>
     *    <td align="left">年（西暦）<br><br>例：<br>2002</td>
     *    <td align="left">年（和暦）<br><br>例：<br>14</td>
     *   </tr>
     *   <tr>
     *    <td>E</td>
     *    <td align="left">曜日<br><br>例：<br>Tuesday</td>
     *    <td align="left">曜日（日本語表記）<br><br>
     *                     例：<br>
     *                    （4個以上の連続したパターン文字）<br>
     *                     月曜日、火曜日、水曜日<br>
     *                     （3個以下の連続したパターン文字）<br>
     *                     月、火、水</td>
     *   </tr>
     * </table>
     * </div>
     *
     * <p>これらのうち、曜日（E）については SimpleDateFotmat
     * のインスタンス作成時に、ロケールを &quot;ja&quot;
     * に指定することで変換される。</p>
     *
     * <p>和暦元号名、および和暦年については、getWarekiGengoName()、
     * getWarekiGengoRoman()、getWarekiYear() メソッドによって取得する。
     * これらのメソッドで参照する和暦の設定は、AplicationResources ファイルで
     * 以下の書式で行う。</p>
     *
     * <p><code><pre>
     * wareki.gengo.<i>ID</i>.name=<i>元号名</i>
     * wareki.gengo.<i>ID</i>.roman=<i>元号のローマ字表記</i>
     * wareki.gengo.<i>ID</i>.startDate=<i>元号法施行日（西暦:yyyy/MM/dd形式）</i>
     * </pre></code></p>
     *
     * <p>IDは、上記の三つの設定を関連付けするためのものであり、任意の文字列を
     * 指定できる。</p>
     *
     * @param format フォーマット
     * @param date 文字列に変換する時刻データ
     * @return 和暦としてフォーマットされた文字列
     */
    public static String dateToWarekiString(String format,
                                            java.util.Date date) {

        // SimpleDateFormatによるフォーマットの前に元号'G'、および年'y'の
        // パターン文字を和暦に置換する
        StringBuilder sb = new StringBuilder();
        boolean inQuote = false; // シングルクォートの中であるかどうか
        char prevCh = 0;
        int count = 0;
        for (int i = 0; i < format.length(); i++) {
            char ch = format.charAt(i);
            if (ch != prevCh && count > 0) {
                if (prevCh == 'G' && count >= 4) {
                    sb.append(getWarekiGengoName(date));
                } else if (prevCh == 'G') {
                    // 元号のローマ字表記の場合は、クォートしておく
                    sb.append('\'');
                    sb.append(getWarekiGengoRoman(date));
                    sb.append('\'');
                } else if (prevCh == 'y') {
                    sb.append(getWarekiYear(date));
                }
                count = 0;
            }

            if (ch == '\'') {
                sb.append('\'');
                inQuote = !inQuote;
            } else if (!inQuote && (ch == 'G' || ch == 'y')) {
                // chは和暦変換で独自に解釈するフォーマット文字である場合は、
                // 繰り返し回数をカウントする。
                prevCh = ch;
                ++count;
            } else {
                // その他の文字は、素通しする
                sb.append(ch);
            }
        }

        // フォーマット中の最後のアイテムを処理する。
        if (count > 0) {
            if (prevCh == 'G' && count >= 4) {
                sb.append(getWarekiGengoName(date));
            } else if (prevCh == 'G') {
                sb.append('\'');
                sb.append(getWarekiGengoRoman(date));
                sb.append('\'');
            } else if (prevCh == 'y') {
                sb.append(getWarekiYear(date));
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat(sb.toString(),
                                                    Locale.JAPAN);

        sdf.getCalendar().setLenient(false);
        sdf.setLenient(false);
        return sdf.format(date);
    }

    /**
     * ApplicationResources ファイルにおいて和暦関連の設定を
     * 取得する際のキーのプリフィックス。
     */
    private static final String GENGO_KEY = "wareki.gengo.";

    /**
     * 元号施行日から元号名へのマップ。
     */
    private static final Map<Date, String> GENGO_NAME
            = new HashMap<Date, String>();

    /**
     * 元号施行日から元号のローマ字表記（短縮形）へのマップ。
     */
    private static final Map<Date, String> GENGO_ROMAN =
            new HashMap<Date, String>();

    /**
     * 和暦の各元号が施行された西暦日付のリスト。リストの先頭から、
     * 古い順に並べられる。
     */
    private static final Date[] GENGO_BEGIN_DATES;

    /**
     * 和暦の各元号が施行された西暦年のリスト。リストの先頭から、
     * 古い順に並べられる。
     */
    private static final int[] GENGO_BEGIN_YEARS;

    /**
     * クラスロード時に、ApplicationResources ファイルで指定された
     * とおりに和暦データを初期化する。
     */
    static {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        // プロパティから「元号の開始日」と「元号」のマップを作成する
        Enumeration<String> enumaration =
            PropertyUtil.getPropertyNames(GENGO_KEY);
        Set<String> ids = new HashSet<String>();

        GENGO_LOOP:
        while (enumaration.hasMoreElements()) {
            String key = enumaration.nextElement();
            String id = key.substring(GENGO_KEY.length(),
                                        key.lastIndexOf("."));
            if (!ids.contains(id)) {
                String name
                    = PropertyUtil.getProperty(GENGO_KEY + id + ".name", "");
                String roman
                    = PropertyUtil.getProperty(GENGO_KEY + id + ".roman", "");

                String start
                    = PropertyUtil.getProperty(GENGO_KEY + id + ".startDate");
                if (start == null) {
                    log.error(GENGO_KEY + id + ".startDate not found");
                    continue GENGO_LOOP;
                }

                try {
                    Date date = sdf.parse(start);

                    GENGO_NAME.put(date, name);
                    GENGO_ROMAN.put(date, roman);
                    log.info("registerd: "
                                + date + ", " + name + ", " + roman);
                } catch (ParseException e) {
                    log.error(e.getMessage());
                }

                ids.add(id);
            }
        }

        // 元号の開始日の配列を作成し、ソートしておく
        Set<Date> keySet = GENGO_NAME.keySet();
        int size = keySet.size();
        GENGO_BEGIN_DATES = keySet.toArray(new Date[size]);
        Arrays.sort(GENGO_BEGIN_DATES);

        // 「元号の開始日の配列」と対応するように元号の開始年の配列を作成する
        GENGO_BEGIN_YEARS = new int[size];
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < GENGO_BEGIN_DATES.length; i++) {
            calendar.setTime(GENGO_BEGIN_DATES[i]);
            GENGO_BEGIN_YEARS[i] = calendar.get(Calendar.YEAR);
        }
    }

    /**
     * 指定された日付の和暦元号を取得する。
     *
     * <p>
     * 和暦元号は、ApplicationResources ファイルで指定する。
     * </p>
     *
     * @param date 日付
     * @return 和暦元号
     */
    public static String getWarekiGengoName(Date date) {
        for (int i = GENGO_BEGIN_DATES.length - 1; i >= 0; i--) {
            if (!date.before(GENGO_BEGIN_DATES[i])) {
                return GENGO_NAME.get(GENGO_BEGIN_DATES[i]);
            }
        }
        throw new IllegalArgumentException("Wareki Gengo Name not found for "
                                            + date);
    }

    /**
     * 指定された日付の和暦元号のローマ字表記（短縮形）を取得する。
     *
     * <p>和暦元号のローマ字表記は、ApplicationResourcesファイルで指定する。</p>
     *
     * @param date 日付
     * @return 和暦元号のローマ字表記
     */
    public static String getWarekiGengoRoman(Date date) {
        for (int i = GENGO_BEGIN_DATES.length - 1; i >= 0; i--) {
            if (!date.before(GENGO_BEGIN_DATES[i])) {
                return GENGO_ROMAN.get(GENGO_BEGIN_DATES[i]);
            }
        }
        throw new IllegalArgumentException("Wareki Gengo Roman not found for "
                                            + date);
    }

    /**
     * 指定された日付の和暦年を取得する。
     *
     * @param date 日付
     * @return 和暦年
     */
    public static int getWarekiYear(Date date) {
        for (int i = GENGO_BEGIN_DATES.length - 1; i >= 0; i--) {
            if (!date.before(GENGO_BEGIN_DATES[i])) {
                Calendar calendar = Calendar.getInstance();

                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);

                return year - GENGO_BEGIN_YEARS[i] + 1;
            }
        }
        throw new IllegalArgumentException("Wareki Gengo not found for "
                                            + date);
    }

}
