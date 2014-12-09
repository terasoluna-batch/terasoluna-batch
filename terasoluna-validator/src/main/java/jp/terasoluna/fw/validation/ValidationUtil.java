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

package jp.terasoluna.fw.validation;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;

import jp.terasoluna.fw.util.PropertyUtil;
import jp.terasoluna.fw.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.UrlValidator;

/**
 * 検証ロジックのユーティリティクラス。
 *
 *
 */
public class ValidationUtil {
    /**
     * 半角カナのチェックに使用する文字列。
     */
    protected static String hankakuKanaList =
        "ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣";

    /**
     * 全角カナのチェックに使用する文字列。
     */
    protected static String zenkakuKanaList =
        "アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソ"
            + "ザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホ"
            + "バビブベボパピプペポマミムメモヤユヨャュョラリルレロ"
            + "ワヮヰヱヲッンー";

    /**
     * <code>ApplicationResources</code>
     * ファイルに定義された半角文字テーブルを取得するキー。
     */
    protected static final String HANKAKU_KANA_LIST_KEY
        = "validation.hankaku.kana.list";

    /**
     * <code>ApplicationResources</code>
     * ファイルに定義された全角文字テーブルを取得するキー。
     */
    protected static final String ZENKAKU_KANA_LIST_KEY
        = "validation.zenkaku.kana.list";

    /**
     * UNICODE文字コード'&#165;u0000'から'&#165;u00ff'の
     * 範囲内に存在する全角文字列。
     */
    protected static final String ZENKAKU_BEGIN_U00_LIST =
        "＼￠￡§¨￢°±´¶×÷";

    static {
        // 半角カナ・全角カナ定義の変更
        setHankakuKanaList();
        setZenkakuKanaList();
    }

    /**
     * 半角カナ定義を設定する。
     */
    protected static void setHankakuKanaList() {
        String value = null;
        // 半角カナ文字列テーブルを取得
        value = PropertyUtil.getProperty(HANKAKU_KANA_LIST_KEY);
        if (value != null) {
            hankakuKanaList = value;
        }
    }

    /**
     * 全角カナ定義を設定する。
     */
    protected static void setZenkakuKanaList() {
        // 全角カナ文字列テーブルを取得
        String value = null;
        value = PropertyUtil.getProperty(ZENKAKU_KANA_LIST_KEY);
        if (value != null) {
            zenkakuKanaList = value;
        }
    }

    /**
     * 指定された文字が半角カナ文字であることをチェックする。
     *
     * @param c 文字
     * @return 半角カナ文字であれば true
     */
    protected static boolean isHankakuKanaChar(char c) {
        return hankakuKanaList.indexOf(c) >= 0;
    }

    /**
     * 指定された文字が半角文字であることをチェックする。
     *
     * @param c 文字
     * @return 半角文字であれば true
     */
    protected static boolean isHankakuChar(char c) {
        return (c <= '\u00ff' && ZENKAKU_BEGIN_U00_LIST.indexOf(c) < 0)
                || isHankakuKanaChar(c);
    }

    /**
     * 指定された文字が全角文字であることをチェックする。
     *
     * @param c 文字
     * @return 全角文字であれば true
     */
    protected static boolean isZenkakuChar(char c) {
        return !isHankakuChar(c);
    }

    /**
     * 指定された文字が全角カナ文字であることをチェックする。
     *
     * @param c 文字
     * @return 全角カナ文字であれば true
     */
    protected static boolean isZenkakuKanaChar(char c) {
        return zenkakuKanaList.indexOf(c) >= 0;
    }

    /**
     * 検証値が正規表現に合致することをチェックする。
     *
     * @param value 検証値
     * @param mask 正規表現
     * @return
     *            検証値が正規表現に合致するならば
     *            <code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
     */
    public static boolean matchRegexp(String value, String mask) {
        if (!StringUtils.isEmpty(value)
                && !GenericValidator.matchRegexp(value, mask)) {
            return false;
        }
        return true;
    }

    /**
     * 検証値が英数字のみからなる文字列であることをチェックする。
     *
     * <code>null</code> 文字列、空文字列は、正当とみなされる。
     *
     * @param value 検証値
     * @return
     *            検証値が英数字のみからなる文字列であるならば
     *            <code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
     */
    public static boolean isAlphaNumericString(String value) {
        return matchRegexp(value, "^([0-9]|[a-z]|[A-Z])*$");
    }

    /**
     * 検証値が大文字英数字のみからなる文字列であることをチェックする。
     *
     * <code>null</code> 文字列、空文字列は、正当とみなされる。
     *
     * @param value 検証値
     * @return
     *            検証値が大文字英数字のみからなる文字列であるならば
     *            <code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
     */
    public static boolean isUpperAlphaNumericString(String value) {
        return matchRegexp(value, "^([0-9]|[A-Z])*$");
    }

    /**
     * 検証値が数字のみからなる文字列であることをチェックする。
     *
     * <code>null</code> 文字列、空文字列は、正当とみなされる。
     *
     * @param value 検証値
     * @return
     *            検証値が数字のみからなる文字列であるならば
     *            <code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
     */
    public static boolean isNumericString(String value) {
        return matchRegexp(value, "^([0-9])*$");
    }

    /**
     * 検証値が指定された桁数であることをチェックする。
     * <br>
     * 桁数チェックは、整数部と小数部に分けて、以下のように行う。
     * <ul>
     * <li>整数部の桁数チェック
     * <ol>
     * <li><code>isAccordedInteger</code>が<code>true</code>の場合、
     * 整数部の桁数が、<code>integerLength</code>の値と
     * 合致するかどうかをチェックする。
     *
     * <li><code>isAccordedInteger</code>が<code>false</code>の場合、
     * 整数部の桁数が、<code>integerLength</code>の値以下であることを
     * チェックする。
     * </ol>
     *
     * <li>小数部の桁数チェック
     * <ol>
     * <li><code>isAccordedScale</code>が<code>true</code>の場合、
     * 小数部の桁数が、<code>scaleLength</code>の値と
     * 合致するかどうかをチェックする。
     *
     * <li><code>isAccordedScale</code>が<code>true</code>の場合、
     * 小数部の桁数が、<code>scaleLength</code>の値以下であることを
     * チェックする。
     * </ol>
     * </ul>
     *
     * @param value 検証値
     * @param integerLength 整数部の桁数
     * @param isAccordedInteger
     *           整数部の桁数一致チェックを行う場合、
     *           <code>true</code>を指定する。
     *           整数部の桁数以内チェックを行う場合、
     *           <code>false</code>を指定する。
     * @param scaleLength 小数部の桁数
     * @param isAccordedScale
     *           小数部の桁数一致チェックを行う場合、
     *           <code>true</code>を指定する。
     *           小数部の桁数以内チェックを行う場合、
     *           <code>false</code>を指定する。
     *
     * @return
     *            検証値が指定された桁数であるならば
     *            <code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
     */
    public static boolean isNumber(
            BigDecimal value, int integerLength, boolean isAccordedInteger,
            int scaleLength, boolean isAccordedScale) {

        // 検証値がnullの時、true返却
        if (value == null) {
            return true;
        }

        // 整数部チェックを行う
        // 整数部絶対値のみ抽出
        BigInteger bi = value.toBigInteger().abs();
        // 整数桁数
        int length = bi.toString().length();
        if (!checkNumberFigures(length, integerLength, isAccordedInteger)) {
            return false;
        }

        // 小数部チェックを行う
        int scale = value.scale();
        if (!checkNumberFigures(scale, scaleLength, isAccordedScale)) {
            return false;
        }

        return true;
    }

    /**
     * 桁数チェックを行うためのヘルパメソッド。
     *
     * @param length 実際の桁数
     * @param checkLength 比較を行うための桁数
     * @param isAccorded
     *           桁数一致チェックを行う場合、<code>true</code>を指定する。
     *           桁数以内チェックを行う場合、<code>false</code>を指定する。
     * @return
     *            実際の桁数が指定された桁数であるならば
     *            <code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
     */
    protected static boolean checkNumberFigures(
            int length, int checkLength, boolean isAccorded) {
        // 桁数オーバ時は、falseを返却
        if (length > checkLength) {
            return false;
        }

        // 一致指定されているとき
        if (isAccorded) {
            // 桁数不一致は、falseを返却
            if (length != checkLength) {
                return false;
            }
        }
        return true;
    }

    /**
     * 検証値が半角カナ文字のみからなる文字列であるかどうかを
     * チェックする。
     *
     * <code>null</code> 文字列、空文字列は、正当とみなされる。
     *
     * @param value 検証値
     * @return
     *            検証値が半角カナ文字のみからなる文字列であるならば
     *            <code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
     */
    public static boolean isHankakuKanaString(String value) {

        // 検証値がnullまたは空文字の時、true返却
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (!isHankakuKanaChar(chars[i])) {
                return false;
            }
        }
        return true;

    }

    /**
     * 検証値が半角文字のみからなる文字列であるかどうかを
     * チェックする。
     *
     * <code>null</code> 文字列、空文字列は、正当とみなされる。
     *
     * @param value 検証値
     * @return
     *            検証値が半角文字のみからなる文字列であるならば
     *            <code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
     */
    public static boolean isHankakuString(String value) {

        // 検証値がnullまたは空文字の時、true返却
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (!isHankakuChar(chars[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 検証値が全角文字のみからなる文字列であるかどうかを
     * チェックする。
     *
     * <code>null</code> 文字列、空文字列は、正当とみなされる。
     *
     * @param value 検証値
     * @return
     *            検証値が全角文字のみからなる文字列であるならば
     *            <code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
     */
    public static boolean isZenkakuString(String value) {

        // 検証値がnullまたは空文字の時、true返却
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (!isZenkakuChar(chars[i])) {
                return false;
            }
        }
        return true;

    }

    /**
     * 検証値が全角カナ文字のみからなる文字列であるかどうかを
     * チェックする。
     *
     * <code>null</code> 文字列、空文字列は、正当とみなされる。
     *
     * @param value 検証値
     * @return
     *            検証値が全角カナ文字のみからなる文字列であるならば
     *            <code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
    */
    public static boolean isZenkakuKanaString(String value) {

        // 検証値がnullまたは空文字の時、true返却
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (!isZenkakuKanaChar(chars[i])) {
                return false;
            }
        }
        return true;

    }

    /**
     * 検証値が禁止文字を含まないことをチェックする。
     * エスケープが必要な文字は「\」文字列を付加する。
     * 例えばダブルコーテーション「"」を禁止文字にする場合、
     * 「\"」とエスケープ文字列を付加する必要がある。
     *
     * 禁止文字が<code>null</code>文字列、または空文字列は正当とみなされる。
     *
     * @param value 検証値
     * @param prohibitedChars 禁止文字の文字列
     * @return 検証値が禁止文字を含んでいなければ<code>true</code>、
     * それ以外は<code>false</code>を返す。
     */
    public static boolean hasNotProhibitedChar(
            String value, String prohibitedChars) {

        // 検証値がnullまたは空文字の時、true返却
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        char[] chars = value.toCharArray();

        // 入力禁止文字列が未設定の場合、チェックを行わない
        if (prohibitedChars == null || "".equals(prohibitedChars)) {
            return true;
        }

        // 検証
        for (int i = 0; i < chars.length; i++) {
            if (prohibitedChars.indexOf(chars[i]) >= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 配列、または、<code>Collection</code>の要素数が、
     * 指定された最大値・最小値の範囲内であるかどうかをチェックする。
     *
     * 検査対象の配列・<code>Collection</code>が<code>null</code>の
     * 場合は、要素数0として処理が行われる。また、検査対象の値が配列、
     * Collectionではない場合は、IllegalArgumentExceptionがスローされる。
     *
     * @param obj 検査対象の配列・<code>Collection</code>
     * @param min 要素数の最小値
     * @param max 要素数の最大値
     * @return
     *            検査対象の配列・<code>Collection</code>が
     *            指定された最大値・最小値の範囲内であるならば
     *            <code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
     */
    public static boolean isArrayInRange(Object obj, int min, int max) {

        // 検証値の配列長
        int targetLength = 0;
        if (obj == null) {
            targetLength = 0;
        } else if (obj instanceof Collection) {
            targetLength = ((Collection) obj).size();
        } else if (obj.getClass().isArray()) {
            targetLength = Array.getLength(obj);
        } else {
            // 検証値が配列型ではない場合、IllegalArgumentExceptionをスロー
            throw new IllegalArgumentException(obj.getClass().getName() +
                    " is neither Array nor Collection.");
        }

        // 入力された要素数が指定範囲以外ならばfalseを返却
        if (!GenericValidator.isInRange(targetLength, min, max)) {
            return false;
        }
        return true;
    }


    /**
     * 検証値がURL形式の文字列であるかどうかをチェックする。
     *
     * <code>null</code> 文字列、空文字列は、正当とみなされる。
     *
     * @param value 検証値
     * @param allowallschemes 全てのスキームを許可するかどうかを指定
     * @param allow2slashes ダブルスラッシュを許可するかどうかを指定
     * @param nofragments URL分割を禁止するかどうかを指定
     * @param schemesVar 許可するスキーム。
     * 複数ある場合はカンマ区切りで指定する。
     * @return
     *            検証値がURL形式の文字列であるならば
     *            <code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
     */
    public static boolean isUrl(
            String value, boolean allowallschemes, boolean allow2slashes,
            boolean nofragments, String schemesVar) {

        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // オプションの設定
        int options = 0;
        if (allowallschemes) {
            options += UrlValidator.ALLOW_ALL_SCHEMES ;
        }
        if (allow2slashes) {
            options += UrlValidator.ALLOW_2_SLASHES;
        }
        if (nofragments) {
            options += UrlValidator.NO_FRAGMENTS;
        }

        // オプションがない場合はデフォルトのGenericValidatorを使用
        if (options == 0 && schemesVar == null) {
            if (GenericValidator.isUrl(value)) {
                return true;
            }
            return false;
        }

        // スキームをString[]に変換
        String[] schemes = null;
        if (schemesVar != null) {

            StringTokenizer st = new StringTokenizer(schemesVar, ",");
            schemes = new String[st.countTokens()];

            int i = 0;
            while (st.hasMoreTokens()) {
                schemes[i++] = st.nextToken().trim();
            }
        }

        // オプションありの場合はUrlValidatorを使用
        UrlValidator urlValidator = new UrlValidator(schemes, options);
        if (urlValidator.isValid(value)) {
            return true;
        }
        return false;
    }

    /**
     * 検証値のバイト列長が指定した最大値・最小値の範囲内であるかどうかを
     * チェックする。サポートされていないエンコーディングを指定した場合、
     * 例外が発生する。
     *
     * <code>null</code> 文字列、空文字列は、正当とみなされる。
     *
     * @param value 検証値
     * @param encoding チェック時の文字列の<code>encoding</code>名
     * @param min 最大値
     * @param max 最小値
     * @return
     *            検証値のバイト列長が指定した最大値・最小値の
     *            範囲内であるならば<code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
     */
    public static boolean isByteInRange(
            String value, String encoding, int min, int max) {

        // 検証値がnullまたは空文字の時、true返却
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 指定エンコーディングでバイト長を取得
        int bytesLength = 0;
        try {
            bytesLength = StringUtil.getByteLength(value, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        // バイト数チェック
        if (!GenericValidator.isInRange(bytesLength, min, max)) {
            return false;
        }
        return true;
    }

    /**
     * 日付が指定した範囲内であるかどうかチェックする。
     *
     * <code>null</code> 文字列、空文字列は、正当とみなされる。
     *
     * @param value 検証値
     * @param startDateStr
     *            日付範囲の開始の閾値となる日付。
     *            <code>datePattern</code>、または、
     *            <code>datePatternStrict</code>で指定した形式で設定すること。
     * @param endDateStr
     *            日付範囲の終了の閾値となる日付。
     *            <code>datePattern</code>、または、
     *            <code>datePatternStrict</code>で指定した形式で設定すること。
     * @param datePattern フォーマットする日付パターン。
     * @param datePatternStrict フォーマットする日付パターン。
     * @return
     *            検証値が半角カナ文字のみからなる文字列であるならば
     *            <code>true</code>を返す。
     *            それ以外の場合、<code>false</code>を返す。
     */
    public static boolean isDateInRange(
            String value, String startDateStr, String endDateStr,
            String datePattern, String datePatternStrict) {

        // 検証値がnullまたは空文字の時、true返却
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 入力日付の妥当性チェック
        Date result = toDate(value, datePattern, datePatternStrict);
        if (result == null) {
            return false;
        }

        if (GenericValidator.isBlankOrNull(startDateStr)
                && GenericValidator.isBlankOrNull(endDateStr)) {
            // 日付範囲が指定されていない場合は正常とみなす
            return true;
        }

        // 開始日付以降かどうかチェック
        if (!GenericValidator.isBlankOrNull(startDateStr)) {
            Date startDate =
                toDate(startDateStr, datePattern, datePatternStrict);

            if (startDate == null) {
                throw new IllegalArgumentException("startDate is unparseable["
                    + startDateStr + "]");
            }

            if (result.before(startDate)) {
                return false;
            }
        }

        // 終了日付以前かどうかチェック
        if (!GenericValidator.isBlankOrNull(endDateStr)) {
            Date endDate = toDate(endDateStr, datePattern, datePatternStrict);

            if (endDate == null) {
                throw new IllegalArgumentException("endDate is unparseable["
                    + endDateStr + "]");
            }

            if (result.after(endDate)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 文字列をDate型に変換する。
     * <br>
     * 変換は以下のように行われる。
     * いずれの場合も、厳格な日付チェックが行われるため、
     * 2000/02/31のような、存在しない日付が<code>value</code>
     * に指定された場合、<code>null</code>が返却される。
     * <ul>
     * <li>
     * <code>datePattern</code>が<code>null</code>、および、
     * 空文字でない場合<br>
     * 文字数を考慮しない日付変換が行われる。
     * たとえば、<code>datePattern</code>がyyyy/MM/ddの場合、
     * 2000/1/1を変換すると、2000/01/01を表すDate型が返却される。
     * <li>
     * <code>datePatternStrict</code>が<code>null</code>、および、
     * 空文字でない場合<br>
     * 文字数を考慮した日付変換が行われる。
     * たとえば、<code>datePattern</code>がyyyy/MM/ddの場合、
     * 2000/1/1を変換すると、nullが返却される。
     * <li>
     * <code>datePattern</code>と<code>datePatternStrict</code>の
     * いずれもが、<code>null</code>、および、 空文字でない場合<br>
     * <code>datePattern</code>が優先して利用される。
     * <li>
     * <code>datePattern</code>と<code>datePatternStrict</code>の
     * いずれもが<code>null</code>、または、空文字の場合<br>
     * 例外が返却される。
     * </ul>
     * <li>
     * <code>value</code>が<code>null</code>、および、
     * 空文字の場合、<code>null</code>が返却される。
     *
     * @param value 変換対象の文字列
     * @param datePattern 日付パターン（文字数を考慮しないパターン指定）
     * @param datePatternStrict 日付パターン（文字数を考慮したパターン指定）
     * @return 文字列から変換されたDateインスタンス。変換が不可能な場合はnull。
     */
    public static Date toDate(String value, String datePattern,
            String datePatternStrict) {

        if (StringUtils.isEmpty(value)) {
            return null;
        }

        Date result = null;
        
        // 桁数チェックなしの変換
        if (datePattern != null && datePattern.length() > 0) {
            result = GenericTypeValidator.formatDate(value,
                            datePattern, false);

        // 桁数チェックありの変換
        } else if (datePatternStrict != null
                && datePatternStrict.length() > 0) {
            result = GenericTypeValidator.formatDate(value,
                            datePatternStrict, true);

        // 日付パターンが設定されていない
        } else {
            throw new IllegalArgumentException(
                    "datePattern or datePatternStrict must be specified.");
        }
        
        return result;
    }
}
