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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 文字列操作を行うユーティリティクラス。
 *
 * <p>
 *  半角・全角変換、HTML特殊文字エスケープ、SQL文LIKE句
 *  エスケープ等、文字列操作に必要な機能を提供する。
 * </p>
 * 
 */
public class StringUtil {

    /**
     * 実行環境のOSで用いられる改行コードを取得する。
     */
    public static final String LINE_SEP
        = System.getProperty("line.separator");

    /**
     * 全角文字リスト。
     */
    private static final String ZENKAKU_LIST = 
        "！”＃＄％＆’（）＊＋，－．／０１２３４"
        + "５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨ"
        + "ＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［￥"
        + "］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐ"
        + "ｑｒｓｔｕｖｗｘｙｚ｛｜｝￣。「」、・"
        + "ァィゥェォャュョッーアイエオナニヌネノ"
        + "マミムメモヤユヨラリルレロン゛゜　";


    /**
     * 全角カナリスト(カ、サ、タ、ハ)行とウ。
     */
    private static final String ZENKAKU_KASATAHA_LIST = 
        "カキクケコサシスセソタチツテトハヒフヘホウ";

    /**
     * 全角カナリスト(ガ、ザ、ダ、バ)行とヴ。
     */
    private static final String ZENKAKU_GAZADABA_LIST = 
        "ガギグゲゴザジズゼゾダヂヅデドバビブベボヴ";

    /**
     * 全角カナ(ワ"[&yen;30f7])。
     */
    private static final Character ZENKAKU_WA_DAKUTEN = 
        new Character('\u30f7');

    /**
     * 全角カナ(ヲ"[&yen;30fa])。
     */
    private static final Character ZENKAKU_WO_DAKUTEN = 
        new Character('\u30fa');

    /**
     * 全角カナリスト(パ)行。
     */
    private static final String ZENKAKU_PA_LIST = "パピプペポ";

    /**
     * 半角文字リスト。
     */
    private static final String HANKAKU_LIST = 
        "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGH"
      + "IJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnop"
      + "qrstuvwxyz{|}~｡｢｣､･ｧｨｩｪｫｬｭｮｯｰｱｲｴｵﾅﾆﾇﾈﾉ"
      + "ﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾝﾞﾟ ";

    /**
     * 半角カナリスト(ｶ､ｻ､ﾀ､ﾊ)行とｳ。
     */
    private static final String HANKAKU_KASATAHA_LIST
        = "ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ";

    /**
     * 半角カナリスト(ﾊ)行。
     */
    private static final String HANKAKU_HA_LIST = "ﾊﾋﾌﾍﾎ";

    /**
     * 指定された文字が半角スペースかどうかを判別する。
     * この StringUtil のトリム系メソッドで共通で利用する。
     *
     * @param c 対象文字
     * @return ホワイトスペースであるときに true
     */
    public static boolean isWhitespace(char c) {
        return c == ' ';
    }

    /**
     * 指定された文字が全角または半角スペースかどうかを判別する。
     * この StringUtil のトリム系メソッドで共通で利用する。
     *
     * @param c 対象文字
     * @return 全角または半角スペースであるときに true
     */
    public static boolean isZenHankakuSpace(char c) {
        return (c == '　' || c == ' ');
    }

    /**
     * 文字列の右側のホワイトスペースを削除する。引数が
     * null のときは null を返す。
     *
     * <p>
     * 例えば Oracle の場合、 CHAR 型の列の値を
     * ResultSet.getString() で取得すると、定義長までスペースで
     * パディングされた文字列が返される。一方、 VARCHAR2 の場合は
     * 右端のスペースはトリミングされるため、そのままでは両者を正しく文字列
     * 比較することが出来ない。また、画面入力された文字列の右端にスペースが
     * 含まれている場合に、これを VARCHAR2 の列に挿入すると
     * スペースもそのまま格納されるが、右端のスペースをトリミングする動作が
     * 妥当な場合も多い。このようなときにこのメソッドを利用する。
     * </p>
     * ※ 全角スペースはトリミングされない。
     *
     * @param str 変換前の文字列
     * @return 変換後の文字列
     */
    public static String rtrim(String str) {
        if (str == null) {
            return null;
        }

        int length = str.length();
        while ((0 < length) && isWhitespace(str.charAt(length - 1))) {
            length--;
        }

        return length < str.length() ? str.substring(0, length) : str;
    }

    /**
     * 文字列の左側のホワイトスペースを削除する。
     * 
     * 引数が null のときは null を返す。<br>
     * ※ 全角スペースはトリミングされない。
     *
     * @param str 変換前の文字列
     * @return 変換後の文字列
     */
    public static String ltrim(String str) {
        if (str == null) {
            return null;
        }

        int start = 0;
        int length = str.length();
        while ((start < length) && isWhitespace(str.charAt(start))) {
            start++;
        }

        return start > 0 ? str.substring(start, length) : str;
    }

    /**
     * 文字列の両側のホワイトスペースを削除する。
     * 
     * 引数が null のときは null を返す。<br>
     * ※ 全角スペースはトリミングされない。
     *
     * @param str 変換前の文字列
     * @return 変換後の文字列
     */
    public static String trim(String str) {
        return StringUtils.trim(str);
    }

    /**
     * 文字列の右側の全角および半角スペースを削除する。引数が
     * null のときは null を返す。
     *
     * <p>
     * 例えば Oracle の場合、 CHAR 型の列の値を
     * ResultSet.getString() で取得すると、定義長までスペースで
     * パディングされた文字列が返される。一方、 VARCHAR2 の場合は
     * 右端のスペースはトリミングされるため、そのままでは両者を正しく文字列
     * 比較することが出来ない。また、画面入力された文字列の右端にスペースが
     * 含まれている場合に、これを VARCHAR2 の列に挿入すると
     * スペースもそのまま格納されるが、右端のスペースをトリミングする動作が
     * 妥当な場合も多い。このようなときにこのメソッドを利用する。
     * </p>
     *
     * @param str 変換前の文字列
     * @return 変換後の文字列
     */
    public static String rtrimZ(String str) {
        if (str == null) {
            return null;
        }

        int length = str.length();
        while ((0 < length) && isZenHankakuSpace(str.charAt(length - 1))) {
            length--;
        }

        return length < str.length() ? str.substring(0, length) : str;
    }

    /**
     * 文字列の左側の全角および半角スペースを削除する。
     * 
     * 引数が null のときは null を返す。<br>
     *
     * @param str 変換前の文字列
     * @return 変換後の文字列
     */
    public static String ltrimZ(String str) {
        if (str == null) {
            return null;
        }

        int start = 0;
        int length = str.length();
        while ((start < length) && isZenHankakuSpace(str.charAt(start))) {
            start++;
        }

        return start > 0 ? str.substring(start, length) : str;
    }

    /**
     * 文字列の両側の全角および半角スペースを削除する。
     * 
     * 引数が null のときは null を返す。<br>
     *
     * @param str 変換前の文字列
     * @return 変換後の文字列
     */
    public static String trimZ(String str) {
        return ltrimZ(rtrimZ(str));
    }

    /**
     * 指定されたクラス名から短縮クラス名（パッケージ修飾なし）を取得する。
     *
     * @param longClassName クラス名
     * @return 短縮クラス名
     */
    public static String toShortClassName(String longClassName) {
        return ClassUtils.getShortClassName(longClassName);
    }

    /**
     * 指定された文字列から末尾の拡張子を取得する。
     * 
     * 拡張子がない場合は空文字列を返す。
     * nameがnullの場合はnullを返す。
     *
     * @param name 拡張子つきの名前
     * @return 拡張子
     */
    public static String getExtension(String name) {
        if (name == null) {
            return null;
        }
        int index = name.lastIndexOf('.');
        return (index < 0) ? "" : name.substring(index);
    }

    /**
     * バイト配列を16進文字列に変換する。
     *
     * @param byteArray バイト配列
     * @param delim デリミタ
     * @return 16進文字列
     */
    public static String toHexString(byte[] byteArray, String delim) {
        if (delim == null) {
            delim = "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if (i > 0) {
                sb.append(delim);
            }
            String hex = Integer.toHexString(byteArray[i] & 0x00ff)
                    .toUpperCase();
            if (hex.length() < 2) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 指定された文字列の頭文字を大文字にする。
     *
     * @param str 変換前の文字列
     * @return 変換後の文字列
     */
    public static String capitalizeInitial(String str) {
        if (str == null || "".equals(str)) {
            return str;
        }
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * CSV形式の文字列を文字列の配列に変換する。
     * 
     * <p>
     * 文字列の先頭がカンマで
     * 始まっていたり、文字列の最後がカンマで終わっている場合には、
     * それぞれ変換結果の配列の最初か、あるいは最後の要素が空文字列となるように
     * 変換される。</p>
     * <p>カンマが連続している場合には、空文字列として変換される。</p>
     * <p>csvString が null だった場合には、
     * 要素数0の配列に変換される。
     *
     * @param csvString CSV形式の文字列
     * @return カンマで分解された文字列を要素に持つ配列
     */
    public static String[] parseCSV(String csvString) {
        if (csvString == null) {
            return new String[0];
        }
        if ("".equals(csvString)) {
            return new String[] { csvString };
        }

        Collection<String> result = new ArrayList<String>();

        char[] chars = csvString.toCharArray();
        int prevCommaIndex = -1;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ',') {
                if (i == prevCommaIndex + 1) {
                    result.add("");
                } else {
                    result.add(new String(chars, prevCommaIndex + 1, i
                            - (prevCommaIndex + 1)));
                }
                if (i == chars.length - 1) {
                    result.add("");
                }
                prevCommaIndex = i;
            } else if (i == chars.length - 1) {
                result.add(new String(chars,
                                    prevCommaIndex + 1,
                                    i - (prevCommaIndex + 1) + 1));
            }
        }

        return result.toArray(new String[0]);
    }

    /**
     * CSV形式の文字列を文字列の配列に変換する。
     * 
     * <p>
     * 文字列の先頭がカンマで
     * 始まっていたり、文字列の最後がカンマで終わっている場合には、
     * それぞれ変換結果の配列の最初か、あるいは最後の要素が空文字列となるように
     * 変換される。</p>
     * <p>カンマが連続している場合には、空文字列として変換される。</p>
     * <p>csvString が null だった場合には、
     * 要素数0の配列に変換される。<br>
     * エスケープ文字列に設定された文字列の次にあるカンマは区切り文字
     * としては認識しない。<br>
     * エスケープ文字列の後のエスケープ文字列はエスケープ文字として
     * 認識しない。
     * </p>
     * 
     * @param csvString CSV形式の文字列
     * @param escapeString エスケープ文字列
     * @return カンマで分解された文字列を要素に持つ配列
     */
    public static String[] parseCSV(String csvString, String escapeString) {
        if (csvString == null) {
            return new String[0];
        }
        if ("".equals(csvString)) {
            return new String[] { csvString };
        }

        Collection<String> result = new ArrayList<String>();

        char[] chars = csvString.toCharArray();
        StringBuilder str = new StringBuilder();
        boolean escape = false;
        for (int i = 0; i < chars.length; i++) {
            if (!escape && chars[i] == ',') {
                result.add(str.toString());
                str = new StringBuilder();
                escape = false;
            } else {
                if (escapeString != null
                    && escapeString.indexOf(chars[i]) >= 0) {
                    // エスケープ文字の後のエスケープ文字は通常の文字列と
                    // 認識する。
                    if (escape) {
                        str.append(chars[i]);
                        escape = false;
                    } else {
                        escape = true;
                    }
                } else {
                    escape = false;
                    str.append(chars[i]);
                }
            }
        }
        result.add(str.toString());
        return result.toArray(new String[0]);
    }

    /**
     * 引数のマップのダンプを取得する。
     * 
     * 値オブジェクトに配列が含まれている場合、各要素オブジェクトの
     * toString()を行い、文字列を出力する。
     *
     * @param map 入出力マップ
     * @return ダンプ文字列
     */
    public static String dump(Map<?, ?> map) {

        if (map == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(LINE_SEP);
        sb.append("Map{");
        sb.append(LINE_SEP);

        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            // キーオブジェクト
            String appendKey = null;
            if (key == null) {
                appendKey = "null";
            } else {
                appendKey = key.toString();
            }
            sb.append(appendKey);
            sb.append('=');
            Object valueObj = map.get(key);
            if (valueObj == null) {
                sb.append("null");
            } else if (valueObj.getClass().isArray()) {
                // 配列型ならば各要素を取得する
                sb.append(getArraysStr((Object[]) valueObj));
            } else {
                sb.append(valueObj.toString());
            }
            sb.append(LINE_SEP);
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * ダンプ対象の値オブジェクトが配列形式の場合、
     * 配列要素でなくなるまで繰り返し値を取得する。
     *
     * @param arrayObj 配列型オブジェクト
     * @return 配列ダンプ対象文字列
     */
    public static String getArraysStr(Object[] arrayObj) {
        return ArrayUtils.toString(arrayObj, null);
    }

    /**
     * 半角文字列を全角文字列に変換する。
     * 
     * <p>
     * カナ文字に濁点または半濁点が続く場合は、可能な限り１文字に変換する。<br>
     * (例) 'ｶ' + 'ﾞ' =&gt; 'ガ'</p>
     *
     * <p>またこの変換処理では以下の全角文字も変換先文字とされる。</p>
     *
     * <p><ul>
     *  <li>「ヴ」</li>
     *  <li>「ワ"」('ワ'に濁点：&yen;u30f7)</li>
     *  <li>「ヲ"」('ヲ'に濁点：&yen;30fa)</li>
     * </ul></p>
     *
     * @param value 半角文字列
     * @return 全角文字列
     */
    public static String hankakuToZenkaku(String value) {

        if (value == null || "".equals(value)) {
            return value;
        }

        char[] chars = value.toCharArray();
        StringBuilder returnValue = new StringBuilder();
        String getValue = null;
        Character nextvalue = null;

        for (int i = 0; i < chars.length; i++) {

            getValue = getZenkakuMoji(chars[i]);

            if (getValue != null) {
                returnValue.append(getValue);
            } else if (i == (chars.length - 1)) {
                // 最後の文字
                getValue = getZenkakuKasatahaMoji(chars[i]);
                if (getValue != null) {
                    // ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ
                    returnValue.append(getValue);
                } else if (new Character(chars[i]).equals(
                        new Character('ﾜ'))) {
                    returnValue.append("ワ");
                } else if (new Character(chars[i]).equals(
                        new Character('ｦ'))) {
                    returnValue.append("ヲ");
                } else {
                    returnValue.append(String.valueOf(chars[i]));
                }
            } else {
                nextvalue = new Character(chars[i + 1]);
                if (nextvalue.equals(new Character('ﾞ'))) {
                    getValue = getZenkakuDakuMoji(chars[i]);
                    if (getValue != null) {
                        // ｶﾞｷﾞｸﾞｹﾞｺﾞｻﾞｼﾞｽﾞｾﾞｿﾞﾀﾞﾁﾞﾂﾞﾃﾞﾄﾞﾊﾞﾋﾞﾌﾞﾍﾞﾎﾞｳﾞ
                        returnValue.append(getValue);
                        i++;
                    } else if (new Character(chars[i]).equals(
                            new Character('ﾜ'))) {
                        // ﾜﾞ
                        returnValue.append(ZENKAKU_WA_DAKUTEN);
                        i++;
                    } else if (new Character(chars[i]).equals(
                            new Character('ｦ'))) {
                        // ｦﾞ
                        returnValue.append(ZENKAKU_WO_DAKUTEN);
                        i++;
                    } else {
                        returnValue.append((String.valueOf(chars[i]) + "゛"));
                        i++;
                    }
                } else if (nextvalue.equals(new Character('ﾟ'))) {
                    getValue = getZenkakuHandakuMoji(chars[i]);
                    if (getValue != null) {
                        // ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ
                        returnValue.append(getValue);
                        i++;
                    } else {
                        // ｶﾟｷﾟｸﾟｹﾟｺﾟｻﾟｼﾟｽﾟｾﾟｿﾟﾀﾟﾁﾟﾂﾟﾃﾟﾄﾟｳﾟ
                        getValue = getZenkakuKasatahaMoji(chars[i]);
                        returnValue.append((String.valueOf(getValue) + "゜"));
                        i++;
                    }
                } else {
                    getValue = getZenkakuKasatahaMoji(chars[i]);
                    if (getValue != null) {
                        // ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ
                        returnValue.append(getValue);
                    } else if (new Character(chars[i]).equals(
                            new Character('ﾜ'))) {
                        returnValue.append("ワ");
                    } else if (new Character(chars[i]).equals(
                            new Character('ｦ'))) {
                        returnValue.append("ヲ");
                    } else {
                        returnValue.append(String.valueOf(chars[i]));
                    }
                }
            }
        }
        return returnValue.toString();
    }

    /**
     * 全角文字列を半角文字列に変換する。
     * 
     * <p>
     * 濁点または半濁点を持つカナ文字は、２文字に分解される。<br>
     * (例) 'ガ' =&gt; 'ｶ' + 'ﾞ'</p>
     *
     * <p>またこの変換処理では以下の全角文字も変換元文字と受け付ける。</p>
     *
     * <p><ul>
     *  <li>「ヴ」</li>
     *  <li>「ワ"」('ワ'に濁点：&yen;u30f7)</li>
     *  <li>「ヲ"」('ヲ'に濁点：&yen;30fa)</li>
     * </ul></p>
     *
     * @param value 全角文字列
     * @return 半角文字列
     */
    public static String zenkakuToHankaku(String value) {

        if (value == null || "".equals(value)) {
            return value;
        }

        char[] chars = value.toCharArray();
        StringBuilder returnValue = new StringBuilder();
        String getValue = null;

        for (int i = 0; i < chars.length; i++) {

            getValue = getHankakuMoji(chars[i]);

            if (getValue != null) {
                returnValue.append(getValue);
            } else {
                returnValue.append(String.valueOf(chars[i]));
            }
        }
        return returnValue.toString();
    }

    /**
     * 半角文字を全角文字に変換する。
     * 
     * 全角文字リストの変換処理を行う。
     * 
     * @param c 半角文字
     * @return 全角文字
     */
    private static String getZenkakuMoji(char c) {

        int index = HANKAKU_LIST.indexOf(c);

        if (index >= 0) {
            return String.valueOf(ZENKAKU_LIST.charAt(index));
        }
        return null;
    }

    /**
     * 半角文字を全角文字に変換する。
     * 
     * 全角カナ(ガ、ザ、ダ、バ)行とヴの変換処理を行う。
     * 
     * @param c 半角文字
     * @return 全角文字
     */
    private static String getZenkakuDakuMoji(char c) {

        int index = HANKAKU_KASATAHA_LIST.indexOf(c);
        if (index >= 0) {
            return String.valueOf(ZENKAKU_GAZADABA_LIST.charAt(index));
        }
        return null;
    }

    /**
     * 半角文字を全角文字に変換する。
     * 
     * 全角カナ(パ)行の変換処理を行う。
     * 
     * @param c 半角文字
     * @return 全角文字
     */
    private static String getZenkakuHandakuMoji(char c) {

        int index = HANKAKU_HA_LIST.indexOf(c);
        if (index >= 0) {
            return String.valueOf(ZENKAKU_PA_LIST.charAt(index));
        }
        return null;
    }

    /**
     * 半角文字を全角文字に変換する。
     * 
     * 全角カナ(カ、サ、タ、ハ)行とウの変換処理を行う。
     * 
     * @param c 半角文字
     * @return 全角文字
     */
    private static String getZenkakuKasatahaMoji(char c) {

        int index = HANKAKU_KASATAHA_LIST.indexOf(c);
        if (index >= 0) {
            return String.valueOf(ZENKAKU_KASATAHA_LIST.charAt(index));
        }
        return null;
    }

    /**
     * 全角文字を半角文字に変換する。
     * 
     * このメソッドでは以下の文字を対象とした変換処理を行う。<br>
     *
     * <p><ul>
     *  <li>半角文字リスト</li>
     *  <li>半角カナ(ｶ､ｻ､ﾀ､ﾊ)行とｳ</li>
     *  <li>半角カナ(ｶﾞ､ｻﾞ､ﾀﾞ､ﾊﾞ)行とｳﾞ</li>
     *  <li>半角カナ(ﾊﾟ)行</li>
     *  <li>半角カナ(ﾜﾞ､ｦﾞ)</li>
     * </ul></p>
     * 
     * @param c 全角文字
     * @return 半角文字
     */
    private static String getHankakuMoji(char c) {

        int index = 0;
        String value = null;

        index = ZENKAKU_LIST.indexOf(c);
        if (index >= 0) {
            return String.valueOf(HANKAKU_LIST.charAt(index));
        }

        index = ZENKAKU_KASATAHA_LIST.indexOf(c);
        if (index >= 0) {
            // カキクケコサシスセソタチツテトハヒフヘホウ
            return String.valueOf(HANKAKU_KASATAHA_LIST.charAt(index));
        }

        index = ZENKAKU_GAZADABA_LIST.indexOf(c);
        if (index >= 0) {
            // ガギグゲゴザジズゼゾ"ダヂヅデドバビブベボヴ
            value = String.valueOf(HANKAKU_KASATAHA_LIST.charAt(index));
            return value + "ﾞ";
        }

        index = ZENKAKU_PA_LIST.indexOf(c);
        if (index >= 0) {
            // パピプペポ
            value = String.valueOf(HANKAKU_HA_LIST.charAt(index));
            return value + "ﾟ";
        } else if ((new Character(c)).equals(new Character('ワ'))) {
            // ワ
            return "ﾜ";
        } else if ((new Character(c)).equals(new Character('ヲ'))) {
            // ヲ
            return "ｦ";
        } else if ((new Character(c)).equals(ZENKAKU_WA_DAKUTEN)) {
            // ワ"[\u30f7]
            return "ﾜﾞ";
        } else if ((new Character(c)).equals(ZENKAKU_WO_DAKUTEN)) {
            // ヲ"[\u30fa]
            return "ｦﾞ";
        } else {
            // 該当なし
            return null;
        }
    }

    /**
     * HTMLメタ文字列変換。
     * 
     * <p>
     *  "&lt;"、"&gt;"、"&amp;"、"&quot;"、"&#39;"といった、HTML中に
     *  そのまま出力すると問題がある文字を
     *  "&amp;lt;"、"&amp;gt;"、"&amp;amp;"、"&amp;quot;"、"&amp;#39;"
     *  に変換する。
     * </p>
     * <p>
     * nullが渡された場合はnullを返す。
     * </p
     *
     * @param str 変換する文字列
     * @return 変換後の文字列
     */
    public static String filter(String str) {
        if (str == null) {
            return null;
        }
        char[] cbuf = str.toCharArray();
        StringBuilder sbui = new StringBuilder();
        for (int i = 0; i < cbuf.length; i++) {
            if (cbuf[i] == '&') {
                sbui.append("&amp;");
            } else if (cbuf[i] == '<') {
                sbui.append("&lt;");
            } else if (cbuf[i] == '>') {
                sbui.append("&gt;");
            } else if (cbuf[i] == '"') {
                sbui.append("&quot;");
            } else if (cbuf[i] == '\'') {
                sbui.append("&#39;");
            } else {
                sbui.append(cbuf[i]);
            }
        }
        return sbui.toString();
    }

    /**
     * LIKE述語のパターン文字列で用いるエスケープ文字。
     */
    public static final String LIKE_ESC_CHAR = "~";

    /**
     * <p>検索条件文字列をLIKE述語のパターン文字列に変換する。</p>
     *
     * <p>変換ルールは以下の通り。</p>
     *
     * <ol>
     *   <li><code>LIKE_ESC_CHAR</code> を <code>LIKE_ESC_CHAR</code> で
     *       エスケープする。</li>
     *   <li>'%'と'_'と'％'と'＿'を <code>LIKE_ESC_CHAR</code> でエスケープする。</li>
     *   <li>末尾に'%'を追加する。</li>
     * </ol>
     *
     * <p>conditionがnullの場合はnullを返す。</p>
     *
     * @param condition 検索条件文字列
     * @return 変換後の検索条件文字列
     */
    public static String toLikeCondition(String condition) {
        if (condition == null) {
            return null;
        }
        final char esc = (LIKE_ESC_CHAR.toCharArray())[0];
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < condition.length(); i++) {
            char c = condition.charAt(i);
            if (c == esc) {
                result.append(esc);
                result.append(esc);
            } else if (c == '%' || c == '_' || c == '＿' || c == '％') {
                result.append(esc);
                result.append(c);
            } else {
                result.append(c);
            }
        }
        result.append('%');
        return result.toString();
    }

    /**
     * 指定された文字列のバイト列長を取得する。
     * 第二引数のエンコーディングでバイト列に変換するが、
     * エンコードが指定されていなかった場合はデフォルトのエンコーディングで
     * バイト列に変換を行う。
     *
     * @param value バイト列長を取得する対象の文字列
     * @param encoding 文字エンコーディング
     * @return バイト列長
     * @throws UnsupportedEncodingException サポートされていない
     * エンコーディングを指定したとき発生する例外。
     */
    public static int getByteLength(String value, String encoding)
            throws UnsupportedEncodingException {
        if (value == null || "".equals(value)) {
            return 0;
        }

        byte[] bytes = null;
        if (encoding == null || "".equals(encoding)) {
            bytes = value.getBytes();
        } else {
            try {
                bytes = value.getBytes(encoding);
            } catch (UnsupportedEncodingException e) {
                throw e;
            }
        }
        return bytes == null ? 0 : bytes.length;
    }
}
