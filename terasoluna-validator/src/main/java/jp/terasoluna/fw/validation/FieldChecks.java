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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jp.terasoluna.fw.beans.IndexedBeanWrapper;
import jp.terasoluna.fw.beans.JXPathIndexedBeanWrapperImpl;
import jp.terasoluna.fw.util.BeanUtil;
import jp.terasoluna.fw.util.ClassLoadException;
import jp.terasoluna.fw.util.ClassUtil;
import jp.terasoluna.fw.util.PropertyAccessException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.util.ValidatorUtils;

/**
 * TERASOLUNAの入力チェック機能で共通に使用される検証ルールクラス。
 *
 * このクラスが提供する検証ルールとしては、以下のものがある。</p>
 *
 * <table border="1">
 * <tr>
 *  <td><center><b>検証名</b></center></td>
 *  <td><center><b>メソッド名</b></center></td>
 *  <td><center><b>バリデーション定義ファイル（validation.xml）に
 *  記述するルール名</b></center></td>
 * </tr>
 * <tr>
 *  <td>必須チェック</td>
 *  <td><code>{@link #validateRequired(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>requierd</code></td>
 * </tr>
 * <tr>
 *  <td>正規表現チェック</td>
 *  <td><code>{@link #validateMask(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>mask</code></td>
 * </tr>
 * <tr>
 *  <td><code>byte</code>型チェック</td>
 *  <td><code>{@link #validateByte(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>byte</code></td>
 * </tr>
 * <tr>
 *  <td><code>short</code>型チェック</td>
 *  <td><code>{@link #validateShort(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>short</code></td>
 * </tr>
 * <tr>
 *  <td><code>integer</code>型チェック</td>
 *  <td><code>{@link #validateInteger(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>integer</code></td>
 * </tr>
 * <tr>
 *  <td><code>long</code>型チェック</td>
 *  <td><code>{@link #validateLong(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>long</code></td>
 * </tr>
 * <tr>
 *  <td><code>float</code>型チェック</td>
 *  <td><code>{@link #validateFloat(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>float</code></td>
 * </tr>
 * <tr>
 *  <td><code>double</code>型チェック</td>
 *  <td><code>{@link #validateDouble(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>double</code></td>
 * </tr>
 * <tr>
 *  <td>日付型チェック</td>
 *  <td><code>{@link #validateDate(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>date</code></td>
 * </tr>
 * <tr>
 *  <td>整数指定範囲チェック</td>
 *  <td><code>{@link #validateIntRange(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>intRange</code></td>
 * </tr>
 * <tr>
 *  <td>実数指定範囲チェック</td>
 *  <td><code>{@link #validateDoubleRange(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>doubleRange</code></td>
 * </tr>
 * <tr>
 *  <td>浮動小数点数指定範囲チェック</td>
 *  <td><code>{@link #validateFloatRange(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>floatRange</code></td>
 * </tr>
 * <tr>
 *  <td>最大文字数チェック</td>
 *  <td><code>{@link #validateMaxLength(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>maxLength</code></td>
 * </tr>
 * <tr>
 *  <td>最小文字数チェック</td>
 *  <td><code>{@link #validateMinLength(
 *  Object, ValidatorAction, Field, ValidationErrors)}</code></td>
 *  <td><code>minLength</code></td>
 * </tr>
 * <tr>
 *  <td>英数字チェック</td>
 *  <td><code>{@link #validateAlphaNumericString(Object, ValidatorAction,
 *  Field, ValidationErrors)}</code></td>
 *  <td><code>alphaNumericString</code></td>
 * </tr>
 * <tr>
 *  <td>大文字英数字チェック</td>
 *  <td><code>{@link #validateCapAlphaNumericString(Object, ValidatorAction,
 *  Field, ValidationErrors)}</code></td>
 *  <td><code>capAlphaNumericString</code></td>
 * </tr>
 * <tr>
 *  <td>数値チェック</td>
 *  <td><code>{@link #validateNumber(Object, ValidatorAction, Field,
 *  ValidationErrors)}</code></td>
 *  <td><code>number</code></td>
 * </tr>
 * <tr>
 *  <td>半角カナ文字チェック</td>
 *  <td><code>{@link #validateHankakuKanaString(Object, ValidatorAction,
 *  Field, ValidationErrors)}</code></td>
 *  <td><code>hankakuKanaString</code></td>
 * </tr>
 * <tr>
 *  <td>半角文字チェック</td>
 *  <td><code>{@link #validateHankakuString(Object, ValidatorAction, Field,
 *  ValidationErrors)}</code></td>
 *  <td><code>hankakuString</code></td>
 * </tr>
 * <tr>
 *  <td>全角カナ文字チェック</td>
 *  <td><code>{@link #validateZenkakuKanaString(Object, ValidatorAction,
 *  Field, ValidationErrors)}</code></td>
 *  <td><code>zenkakuKanaString</code></td>
 * </tr>
 * <tr>
 *  <td>全角文字チェック</td>
 *  <td><code>{@link #validateZenkakuString(Object, ValidatorAction, Field,
 *  ValidationErrors)}</code></td>
 *  <td><code>zenkakuString</code></td>
 * </tr>
 * <tr>
 *  <td>入力禁止文字チェック</td>
 *  <td><code>{@link #validateProhibited(Object, ValidatorAction, Field,
 *  ValidationErrors)}</code></td>
 *  <td><code>prohibited</code></td>
 * </tr>
 * <tr>
 *  <td>文字列長一致チェック</td>
 *  <td><code>{@link #validateStringLength(Object, ValidatorAction, Field,
 *  ValidationErrors)}</code></td>
 *  <td><code>stringLength</code></td>
 * </tr>
 * <tr>
 *  <td>数字文字列チェック</td>
 *  <td><code>{@link #validateNumericString(Object, ValidatorAction, Field,
 *  ValidationErrors)}</code></td>
 *  <td><code>numericString</code></td>
 * </tr>
 * <tr>
 *  <td>URLチェック</td>
 *  <td><code>{@link #validateUrl(Object, ValidatorAction, Field,
 *  ValidationErrors)}</code></td>
 *  <td><code>url</code></td>
 * </tr>
 * <tr>
 *  <td>配列長一致チェック</td>
 *  <td><code>{@link #validateArrayRange(Object, ValidatorAction, Field,
 *  ValidationErrors)}</code></td>
 *  <td><code>arrayRange</code></td>
 * </tr>
 * <tr>
 *  <td>バイト数指定範囲内チェック</td>
 *  <td><code>{@link #validateByteRange(Object, ValidatorAction, Field,
 *  ValidationErrors)}</code></td>
 *  <td><code>byteRange</code></td>
 * </tr>
 * <tr>
 *  <td>日付文字列指定範囲内チェック</td>
 *  <td><code>{@link #validateDateRange(Object, ValidatorAction, Field,
 *  ValidationErrors)}</code></td>
 *  <td><code>dateRange</code></td>
 * </tr>
 * <tr>
 *  <td>配列・コレクション型フィールド全要素チェック</td>
 *  <td><code>{@link #validateArraysIndex(Object, ValidatorAction, Field,
 *  ValidationErrors)}</code></td>
 *  <td>他の単体フィールド検証ルール名＋"Array"(配列長一致チェックは除く)</td>
 * </tr>
 * <tr>
 *  <td>相関チェック</td>
 *  <td><code>{@link #validateMultiField(Object, ValidatorAction, Field,
 *  ValidationErrors)}</code></td>
 *  <td>multiField</td>
 * </tr>
 * </table>
 *
 * <p>このクラスではStrutsのValidWhenを利用した入力チェックメソッドを
 * サポートしていない。相関チェックを行なう場合、各フレームワークで
 * 別途、相関チェックを行う仕組みを用意すること。</p>
 *
 * <p>必須チェック以外のチェックルールでは、
 * 半角スペースのみの文字列が入力値として渡されてきた場合、エラーと判定されない。
 * エラーとする場合は必須チェックと組み合わせるか、
 * 半角スペースのチェックを追加すること。</p>
 * 
 * <p>この検証ルールクラスを利用するためには、アクションごとに検証内容を記述した
 * バリデーション定義ファイル(validation.xml) を作成する必要がある。</p>
 *
 * <h5>validation.xmlの記述例（単体フィールド検証）</h5>
 * <code><pre>
 *  &lt;formset&gt;
 *    ・・・
 *    &lt;!-- 単体のフィールド検証 --&gt;
 *    &lt;form name="testBean"&gt;
 *      &lt;field property="field"
 *          depends="required,alphaNumericString,maxlength"&gt;
 *        &lt;arg key="testBean.field" position="0"/&gt;
 *        &lt;arg key="${var:maxlength}" position="1"/&gt;
 *        &lt;var&gt;
 *          &lt;var-name&gt;maxlength&lt;/var-name&gt;
 *          &lt;var-value&gt;10&lt;/var-value&gt;
 *        &lt;/var&gt;
 *      &lt;/field&gt;
 *    &lt;/form&gt;
 *    ・・・
 *  &lt;/formset&gt;
 * </pre></code>
 *
 * <h5>配列・コレクション型フィールドの入力チェック</h5>
 *
 * <p>このクラスの
 * {@link #validateArraysIndex(
 * Object, ValidatorAction, Field, ValidationErrors)}
 * メソッドを使用することにより、配列・コレクション型の入力チェックが
 * 可能になる。</p>
 *
 * <p>例えば、fieldsという配列のプロパティを持つbeanインスタンスに対して、
 * fields要素に対する必須チェックを行なう場合、validation.xmlのプロパティ名に
 * fieldsと記述する。
 * 実行時には、システム側でfieldsプロパティの配列を０番目から順に走査して、
 * 全要素に対してチェックメソッドを実行する。
 * 実行するルール（validation.xmlのdepends指定）は、ルール名に”Array”を
 * 加えた名前を指定する。</p>
 *
 * 配列・コレクション型の一覧検証に対応するルールは以下の通りである。
 * <ul>
 *   <li><code>requiredArray</code></li>
 *   <li><code>minLengthArray</code></li>
 *   <li><code>maxLengthArray</code></li>
 *   <li><code>maskArray</code></li>
 *   <li><code>byteArray</code></li>
 *   <li><code>shortArray</code></li>
 *   <li><code>integerArray</code></li>
 *   <li><code>longArray</code></li>
 *   <li><code>floatArray</code></li>
 *   <li><code>doubleArray</code></li>
 *   <li><code>dateArray</code></li>
 *   <li><code>intRangeArray</code></li>
 *   <li><code>doubleRangeArray</code></li>
 *   <li><code>floatRangeArray</code></li>
 *   <li><code>creditCardArray</code></li>
 *   <li><code>emailArray</code></li>
 *   <li><code>urlArray</code></li>
 *   <li><code>alphaNumericStringArray</code></li>
 *   <li><code>hankakuKanaStringArray</code></li>
 *   <li><code>hankakuStringArray</code></li>
 *   <li><code>zenkakuStringArray</code></li>
 *   <li><code>zenkakuKanaStringArray</code></li>
 *   <li><code>capAlphaNumericStringArray</code></li>
 *   <li><code>numberArray</code></li>
 *   <li><code>numericStringArray</code></li>
 *   <li><code>prohibitedArray</code></li>
 *   <li><code>stringLengthArray</code></li>
 *   <li><code>dateRangeArray</code></li>
 *   <li><code>byteRangeArray</code></li>
 * </ul>
 *
 * <p>ルールを追加した場合、配列・コレクション用のメソッドを別途作成する
 * 必要がある。</p>
 *
 * <p>入力チェックでエラーが検出された場合、以下の情報を返す。
 * <ul>
 *   <li>エラーコード</li>
 *   <li>エラーが発生したプロパティ名</li>
 *   <li>置換文字列</li>
 * </ul>
 * </p>
 *
 * @see jp.terasoluna.fw.beans.JXPathIndexedBeanWrapperImpl
 * @see jp.terasoluna.fw.beans.IndexedBeanWrapper
 */
public class FieldChecks {

    /**
     * 本クラスで利用するログ。
     */
    private static Log log = LogFactory.getLog(FieldChecks.class);

    /**
     * 入力値のNull検証と、スペースを除いた入力値の文字列長が0より大きいか
     * 検証する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     */
    public boolean validateRequired(
            Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);

        // 検証
        if (GenericValidator.isBlankOrNull(value)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 入力値が指定された正規表現に適合するか検証する。
     *
     * <p>以下は、文字列が半角英数字であるときのみtrueを返却する
     * 検証の設定例である。
     *
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;maskField&quot;
     *      depends=&quot;mask&quot;&gt;
     *    &lt;arg key=&quot;sample.escape&quot; position="0"/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;mask&lt;/var-name&gt;
     *      &lt;var-value&gt;^([0-9]|[a-z]|[A-Z])*$&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     *
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> mask </td>
     *   <td>正規表現</td>
     *   <td>true</td>
     *   <td>入力文字列が指定された正規表現に合致するときは <code>true</code>
     *       が返却される。指定されない場合はValidatorException
     *       がスローされる。</td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     * @exception ValidatorException 設定ファイルからmask(正規表現)の値が
     * 取得できない場合にスローされる。
     */
    public boolean validateMask(
            Object bean, ValidatorAction va,
            Field field, ValidationErrors errors)
            throws ValidatorException {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 正規表現
        String mask = field.getVarValue("mask");

        // varからmaskが取得できない場合はValidatorException
        if (StringUtils.isEmpty(mask)) {
            log.error("var[mask] must be specified.");
            throw new ValidatorException("var[mask] must be specified.");
        }

        // 検証
        if (!GenericValidator.matchRegexp(value, mask)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 入力値がbyte型に変換可能か検証する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     */
    public boolean validateByte(
            Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証
        if (GenericTypeValidator.formatByte(value) == null) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 入力値がshort型に変換可能か検証する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     */
    public boolean validateShort(
            Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証
        if (GenericTypeValidator.formatShort(value) == null) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 入力値がint型に変換可能か検証する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     */
    public boolean validateInteger(
            Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) {
        // 検証
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証
        if (GenericTypeValidator.formatInt(value) == null) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 入力値がlong型に変換可能か検証する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     */
    public boolean validateLong(
            Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証
        if (GenericTypeValidator.formatLong(value) == null) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 入力値がfloat型に変換可能か検証する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     */
    public boolean validateFloat(
            Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証
        if (GenericTypeValidator.formatFloat(value) == null) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 入力値がdouble型に変換可能か検証する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     */
    public boolean validateDouble(
            Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証
        if (GenericTypeValidator.formatDouble(value) == null) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 入力値が有効な日付か検証する。
     *
     * <p>fieldに「datePattern」変数が定義されている場合、
     * <code>java.text.SimpleDateFormat</code>クラスを
     * 利用したフォーマットの検証が行われる。</p>
     *
     * <p>fieldに「datePatternStrict」変数が定義されている場合、
     * <code>java.text.SimpleDateFormat</code>クラスを利用したフォーマットと
     * 桁数の検証が行われる。
     * 例えば、'2/12/1999'は'MM/dd/yyyy'形式のフォーマットにすると
     * 月（Month)が２桁でないため、検証エラーとなる。</p>
     *
     * <p>「datePattern」変数と、「datePatternStrict」変数の両方が指定された
     * 場合、「datePattern」変数が優先して使用される。
     *  「datePattern」変数と、「datePatternStrict」変数の両方が指定されていない
     *  場合、例外が発生する。また、日付パターンに無効な文字列が含まれている
     *  場合も例外が発生する</p>
     *
     * <p>フォーマット時には必ず「setLenient=false」が設定されるため、
     * 2000/02/31のような存在しない日付は、いずれの場合も許容されない。</p>
     *
     * 以下は、文字列がyyyy/MM/dd型の日付パターンに合致することを検証
     * する場合の設定例である。
     *
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;dateField&quot;
     *      depends=&quot;date&quot;&gt;
     *    &lt;arg key=&quot;sample.dateField&quot; position="0"/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;datePattern&lt;/var-name&gt;
     *      &lt;var-value&gt;yyyy/MM/dd&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     *
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> datePattern </td>
     *   <td>日付パターン</td>
     *   <td>false</td>
     *   <td>日付パターンを指定する。入力値の桁数チェックは行わない。
     *   たとえば、日付パターンがyyyy/MM/ddの場合、2001/1/1はエラーにならない。
     *   datePatternとdatePatternStrictが両方指定されている場合は、
     *   datePatternが優先して使用される。
     *   </td>
     *  </tr>
     *  <tr>
     *   <td> datePatternStrict </td>
     *   <td>日付パターン</td>
     *   <td>false</td>
     *   <td>日付パターンを指定する。入力値の桁数が、
     *   指定された日付パターンの桁数に合致するかのチェックを行う。
     *   たとえば、日付パターンがyyyy/MM/ddの場合、2001/1/1はエラーになる。
     *   datePatternとdatePatternStrictが両方指定されている場合は、
     *   datePatternが優先して使用される。
     *   </td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     * @throws ValidatorException validation定義ファイルの設定ミスがあった場合
     * スローされる例外。
     */
    public boolean validateDate(
            Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) throws ValidatorException {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // フォーマット用の日付パターン
        String datePattern = field.getVarValue("datePattern");
        String datePatternStrict = field.getVarValue("datePatternStrict");

        // 検証
        Date result = null;
        try {
            result =
                ValidationUtil.toDate(value, datePattern, datePatternStrict);
        } catch (IllegalArgumentException e) {
            // 日付パターンが不正な場合
            String message = "Mistake on validation definition file. "
                + "- datePattern or datePatternStrict is invalid. "
                + "You'll have to check it over. ";
            log.error(message, e);
            throw new ValidatorException(message);
        }
        if (result == null) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 入力値が指定されたint型に変換可能であり、
     * かつ指定された範囲内か検証する。
     *
     * <p>以下は、文字列が10から100までの範囲内の数値であるときのみ
     * trueを返却する検証の設定例である。
     *
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;intField&quot;
     *      depends=&quot;intRange&quot;&gt;
     *    &lt;arg key=&quot;sample.intField&quot; position="0"/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;intRangeMin&lt;/var-name&gt;
     *      &lt;var-value&gt;10&lt;/var-value&gt;
     *    &lt;/var&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;intRangeMax&lt;/var-name&gt;
     *      &lt;var-value&gt;100&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     *
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> intRangeMin </td>
     *   <td>最小値</td>
     *   <td>false</td>
     *   <td>範囲指定の最小値を設定する。設定しない場合、Integerの最小値が
     *   指定される。
     *   数値以外の文字列が入力された場合、例外がスローされる。</td>
     *  </tr>
     *  <tr>
     *   <td> intRangeMax </td>
     *   <td>最大値</td>
     *   <td>false</td>
     *   <td>範囲指定の最大値を設定する。設定しない場合、Integerの最大値が
     *   指定される。
     *   数値以外の文字列が入力された場合、例外がスローされる。</td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     * @throws ValidatorException validation定義ファイルの設定ミスがあった場合
     * スローされる例外。
     */
    public boolean validateIntRange(
            Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) throws ValidatorException {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証値をintに変換 --- Integer型ではない場合、検証エラー。
        int intValue = 0;
        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            rejectValue(errors, field, va, bean);
            return false;
        }

        // 範囲指定値 --- 設定値がInteger型ではない場合、例外。
        //                設定なしはデフォルト値を使用する。
        String strMin = field.getVarValue("intRangeMin");
        int min = Integer.MIN_VALUE;
        if (!GenericValidator.isBlankOrNull(strMin)) {
            try {
                min = Integer.parseInt(strMin);
            } catch (NumberFormatException e) {
                String message = "Mistake on validation definition file. "
                    + "- intRangeMin is not number. "
                    + "You'll have to check it over. ";
                log.error(message, e);
                throw new ValidatorException(message);
            }
        }
        String strMax = field.getVarValue("intRangeMax");
        int max = Integer.MAX_VALUE;
        if (!GenericValidator.isBlankOrNull(strMax)) {
            try {
                max = Integer.parseInt(strMax);
            } catch (NumberFormatException e) {
                String message = "Mistake on validation definition file. "
                    + "- intRangeMax is not number. "
                    + "You'll have to check it over. ";
                log.error(message, e);
                throw new ValidatorException(message);
            }
        }

        // 検証
        if (!GenericValidator.isInRange(intValue, min, max)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 入力値が指定されたdouble型に変換可能であり、
     * かつ指定された範囲内か検証する。
     *
     * <p>以下は、文字列が10から100までの範囲内の数値であるときのみ
     * trueを返却する検証の設定例である。
     *
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;doubleField&quot;
     *      depends=&quot;doubleRange&quot;&gt;
     *    &lt;arg key=&quot;sample.doubleField&quot; position="0"/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;doubleRangeMin&lt;/var-name&gt;
     *      &lt;var-value&gt;10.0&lt;/var-value&gt;
     *    &lt;/var&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;doubleRangeMax&lt;/var-name&gt;
     *      &lt;var-value&gt;100.0&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     *
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> doubleRangeMin </td>
     *   <td>最小値</td>
     *   <td>false</td>
     *   <td>範囲指定の最小値を設定する。設定しない場合、Doubleの最小値が
     *   指定される。
     *   数値以外の文字列が入力された場合、例外がスローされる。</td>
     *  </tr>
     *  <tr>
     *   <td> doubleRangeMax </td>
     *   <td>最大値</td>
     *   <td>false</td>
     *   <td>範囲指定の最大値を設定する。設定しない場合、Doubleの最大値が
     *   指定される。
     *   数値以外の文字列が入力された場合、例外がスローされる。</td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     * @throws ValidatorException validation定義ファイルの設定ミスがあった場合
     * スローされる例外。
     */
    public boolean validateDoubleRange(
            Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) throws ValidatorException {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証値をdoubleに変換 --- Double型ではない場合、検証エラー。
        double dblValue = 0;
        try {
            dblValue = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            rejectValue(errors, field, va, bean);
            return false;
        }

        // 範囲指定値 --- 設定値がDouble型ではない場合、例外。
        //                設定なしはデフォルト値を使用する。
        String strMin = field.getVarValue("doubleRangeMin");
        double min = Double.MIN_VALUE;
        if (!GenericValidator.isBlankOrNull(strMin)) {
            try {
                min = Double.parseDouble(strMin);
            } catch (NumberFormatException e) {
                String message = "Mistake on validation definition file. "
                    + "- doubleRangeMin is not number. "
                    + "You'll have to check it over. ";
                log.error(message, e);
                throw new ValidatorException(message);
            }
        }
        String strMax = field.getVarValue("doubleRangeMax");
        double max = Double.MAX_VALUE;
        if (!GenericValidator.isBlankOrNull(strMax)) {
            try {
                max = Double.parseDouble(strMax);
            } catch (NumberFormatException e) {
                String message = "Mistake on validation definition file. "
                    + "- doubleRangeMax is not number. "
                    + "You'll have to check it over. ";
                log.error(message, e);
                throw new ValidatorException(message);
            }
        }

        // 検証
        if (!GenericValidator.isInRange(dblValue, min, max)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 入力値が指定されたfloat型に変換可能であり、
     * かつ指定された範囲内か検証する。
     *
     * <p>以下は、文字列が10から100までの範囲内の数値であるときのみ
     * trueを返却する検証の設定例である。
     *
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;floatField&quot;
     *      depends=&quot;floatRange&quot;&gt;
     *    &lt;arg key=&quot;sample.floatField&quot; position="0"/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;floatRangeMin&lt;/var-name&gt;
     *      &lt;var-value&gt;10&lt;/var-value&gt;
     *    &lt;/var&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;floatRangeMax&lt;/var-name&gt;
     *      &lt;var-value&gt;100&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     *
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> floatRangeMin </td>
     *   <td>最小値</td>
     *   <td>false</td>
     *   <td>範囲指定の最小値を設定する。設定しない場合、Floatの最小値が
     *   指定される。
     *   数値以外の文字列が入力された場合、例外がスローされる。</td>
     *  </tr>
     *  <tr>
     *   <td> floatRangeMax </td>
     *   <td>最大値</td>
     *   <td>false</td>
     *   <td>範囲指定の最大値を設定する。設定しない場合、Floatの最大値が
     *   指定される。
     *   数値以外の文字列が入力された場合、例外がスローされる。</td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     * @throws ValidatorException validation定義ファイルの設定ミスがあった場合
     * スローされる例外。
     */
    public boolean validateFloatRange(
            Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) throws ValidatorException {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証値をfloatに変換 --- Float型ではない場合、検証エラー。
        float floatValue = 0;
        try {
            floatValue = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            rejectValue(errors, field, va, bean);
            return false;
        }

        // 範囲指定値 --- 設定値がFloat型ではない場合、例外。
        //                設定なしはデフォルト値を使用する。
        String strMin = field.getVarValue("floatRangeMin");
        float min = Float.MIN_VALUE;
        if (!GenericValidator.isBlankOrNull(strMin)) {
            try {
                min = Float.parseFloat(strMin);
            } catch (NumberFormatException e) {
                String message = "Mistake on validation definition file. "
                    + "- floatRangeMin is not number. "
                    + "You'll have to check it over. ";
                log.error(message, e);
                throw new ValidatorException(message);
            }
        }
        String strMax = field.getVarValue("floatRangeMax");
        float max = Float.MAX_VALUE;
        if (!GenericValidator.isBlankOrNull(strMax)) {
            try {
                max = Float.parseFloat(strMax);
            } catch (NumberFormatException e) {
                String message = "Mistake on validation definition file. "
                    + "- floatRangeMax is not number. "
                    + "You'll have to check it over. ";
                log.error(message, e);
                throw new ValidatorException(message);
            }
        }

        // 検証
        if (!GenericValidator.isInRange(floatValue, min, max)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 入力値の文字数が指定された最大文字数以下か検証する。
     *
     * <p>以下は、文字列が10文字以下であるときのみ
     * trueを返却する検証の設定例である。
     *
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;stringField&quot;
     *      depends=&quot;maxLength&quot;&gt;
     *    &lt;arg key=&quot;sample.stringField&quot; position="0"/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;maxlength&lt;/var-name&gt;
     *      &lt;var-value&gt;10&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     *
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> maxlength </td>
     *   <td>最大文字数</td>
     *   <td>true</td>
     *   <td>文字列の最大文字数を設定する。
     *   数値以外の文字列が入力された場合、例外がスローされる。</td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     * @throws ValidatorException validation定義ファイルの設定ミスがあった場合
     * スローされる例外。
     */
    public boolean validateMaxLength(
            Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) throws ValidatorException {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 最大桁数
        int max = 0;
        try {
            max = Integer.parseInt(field.getVarValue("maxlength"));
        } catch (NumberFormatException e) {
            String message = "Mistake on validation definition file. "
                + "- maxlength is not number. "
                + "You'll have to check it over. ";
            log.error(message, e);
            throw new ValidatorException(message);
        }

        // 検証
        if (!GenericValidator.maxLength(value, max)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 入力値の文字数が指定された最小文字数以上か検証する。
     *
     * <p>以下は、文字列が10文字以上であるときのみ
     * trueを返却する検証の設定例である。
     *
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;stringField&quot;
     *      depends=&quot;minLength&quot;&gt;
     *    &lt;arg key=&quot;sample.stringField&quot; position="0"/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;minlength&lt;/var-name&gt;
     *      &lt;var-value&gt;10&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     *
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> minlength </td>
     *   <td>最小文字数</td>
     *   <td>true</td>
     *   <td>文字列の最小文字数を設定する。
     *   数値以外の文字列が入力された場合、例外がスローされる。</td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va 検証中の<code>ValidatorAction</code>インスタンス。
     * @param field 検証中の<code>Field</code>インスタンス。
     * @param errors 検証エラーが発生した場合、
     * エラー情報を格納するオブジェクト。
     * @return 検証に成功した場合は<code>true</code>を返す。
     * 検証エラーがある場合、<code>false</code>を返す。
     * @throws ValidatorException validation定義ファイルの設定ミスがあった場合
     * スローされる例外。
     */
    public boolean validateMinLength(
        Object bean, ValidatorAction va, Field field, ValidationErrors errors)
        throws ValidatorException {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 最小桁数
        int min = 0;
        try {
            min = Integer.parseInt(field.getVarValue("minlength"));
        } catch (NumberFormatException e) {
            String message = "Mistake on validation definition file. "
                + "- minlength is not number. "
                + "You'll have to check it over. ";
            log.error(message, e);
            throw new ValidatorException(message);
        }

        // 検証
        if (!GenericValidator.minLength(value, min)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 指定されたフィールドが英数字であることをチェックする。
     * 正規表現<code>^([0-9]|[a-z]|[A-Z])*$</code>を使用する。
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param errors エラー
     * @return 入力値が正しければ true
     */
    public boolean validateAlphaNumericString(Object bean,
            ValidatorAction va, Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証
        if (!ValidationUtil.isAlphaNumericString(value)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 指定されたフィールドが大文字英数字であることをチェックする。
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param errors エラー
     * @return 入力値が正しければ true
     */
    public boolean validateCapAlphaNumericString(Object bean,
            ValidatorAction va, Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証
        if (!ValidationUtil.isUpperAlphaNumericString(value)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 指定されたフィールドが数値であることをチェックする。
     * 全角数字を使用すると検証に失敗する。
     * チェックに引っかかった場合は、
     * エラー情報をerrorsに追加し、falseを返却する。
     *
     * <p>まず、入力された文字列を用い、BigDecimal 型を生成する
     * ここで生成不可能ならばエラー処理を行なう。
     *
     * 次に整数部の桁数が指定されている場合に、桁数の確認を行う。
     * validation.xml で isAccordedInteger()
     * が "true" 指定されている場合のみ
     * 整数桁数の同一チェックが行われる。
     * チェックに引っかかった場合は、エラー処理を行なう。
     *
     * 最後に小数部の桁数が指定されている場合に、桁数の確認を行う。
     * validation.xmlでisAccordedScaleが"true"である場合のみ
     * 小数桁数の同一チェックが行われる。
     *
     * <p>
     * 下記は、整数部3、小数部2である数値を検証する例である。
     *
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;numberField&quot;
     *      depends=&quot;number&quot;&gt;
     *    &lt;arg key=&quot;sample.numberField&quot; position="0"/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;integerLength&lt;/var-name&gt;
     *      &lt;var-value&gt;3&lt;/var-value&gt;
     *    &lt;/var&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;scale&lt;/var-name&gt;
     *      &lt;var-value&gt;2&lt;/var-value&gt;
     *    &lt;/var&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;isAccordedInteger&lt;/var-name&gt;
     *      &lt;var-value&gt;true&lt;/var-value&gt;
     *    &lt;/var&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;isAccordedScale&lt;/var-name&gt;
     *      &lt;var-value&gt;true&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> <code>integerLength</code> </td>
     *   <td> 整数部桁数 </td>
     *   <td> <code>false</code> </td>
     *   <td>整数の桁数を設定する。<code>isAccordedInteger</code>指定が
     *       無いときは、指定桁数以内の検証を行う。
     *       省略時は、<code>int</code>型の最大値が指定される。
     *       非数値を設定した場合、例外がスローされる。</td>
     *  </tr>
     *  <tr>
     *   <td> <code>scale</code> </td>
     *   <td> 小数部桁数 </td>
     *   <td> <code>false</code> </td>
     *   <td>小数値の桁数を設定する、<code>isAccordedScale</code>指定が
     *       無いときは、指定桁数以内の検証を行う。
     *       省略時は、<code>int</code>型の最大値が指定される。
     *       非数値を設定した場合、例外がスローされる。</td>
     *  </tr>
     *  <tr>
     *   <td> <code>isAccordedInteger</code> </td>
     *   <td> 整数桁数一致チェック </td>
     *   <td> <code>false</code> </td>
     *   <td> <code>true</code>が指定されたとき、整数桁数の一致チェックが
     *        行なわれる。省略時、<code>true</code>以外の文字列が設定された時は
     *        桁数以内チェックとなる。</td>
     *  </tr>
     *  <tr>
     *   <td> <code>isAccordedScale</code> </td>
     *   <td> 小数桁数一致チェック </td>
     *   <td> <code>false</code> </td>
     *   <td> <code>true</code>が指定されたとき、小数桁数の一致チェックが
     *        行なわれる。省略時、<code>true</code>以外の文字列が設定された時は
     *        桁数以内チェックとなる。</td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param errors エラー
     * @return 入力値が正しければ true
     * @throws ValidatorException validation定義ファイルの設定ミスがあった場合
     * スローされる例外。
     */
    public boolean validateNumber(Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) throws ValidatorException {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証値が半角ではない場合エラー。
        if (!ValidationUtil.isHankakuString(value)) {
            rejectValue(errors, field, va, bean);
            return false;
        }

        // 検証値をBigDecimalに変換
        BigDecimal number = null;
        try {
            number = new BigDecimal(value);
        } catch (NumberFormatException e) {
            rejectValue(errors, field, va, bean);
            return false;
        }

        // 整数部桁数取得
        int integerLength = Integer.MAX_VALUE;
        String integerLengthStr = field.getVarValue("integerLength");
        if (!GenericValidator.isBlankOrNull(integerLengthStr)) {
            try {
                integerLength = Integer.parseInt(integerLengthStr);
            } catch (NumberFormatException e) {
                String message = "Mistake on validation definition file. "
                    + "- integerLength is not number. "
                    + "You'll have to check it over. ";
                log.error(message, e);
                throw new ValidatorException(message);
            }
        }

        // 小数部桁数取得
        int scaleLength = Integer.MAX_VALUE;
        String scaleStr = field.getVarValue("scale");
        if (!GenericValidator.isBlankOrNull(scaleStr)) {
            try {
                scaleLength = Integer.parseInt(scaleStr);
            } catch (NumberFormatException e) {
                String message = "Mistake on validation definition file. "
                    + "- scale is not number. "
                    + "You'll have to check it over. ";
                log.error(message, e);
                throw new ValidatorException(message);
            }
        }

        // 整数桁数一致チェックか
        boolean isAccordedInteger =
            "true".equals(field.getVarValue("isAccordedInteger"));
        // 小数桁数一致チェックか
        boolean isAccordedScale =
            "true".equals(field.getVarValue("isAccordedScale"));

        // 検証
        if (!ValidationUtil.isNumber(
                number, integerLength, isAccordedInteger,
                scaleLength, isAccordedScale)) {
            rejectValue(errors, field, va, bean);
            return false;
        }

        return true;
    }

    /**
     * 指定されたフィールドが半角カナ文字列であることをチェックする。
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param errors エラー
     * @return 入力値が正しければ true
     */
    public boolean validateHankakuKanaString(Object bean,
            ValidatorAction va, Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証
        if (!ValidationUtil.isHankakuKanaString(value)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 指定されたフィールドが半角文字列であることをチェックする。
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param errors エラー
     * @return 入力値が正しければ true
     */
    public boolean validateHankakuString(Object bean,
            ValidatorAction va, Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証
        if (!ValidationUtil.isHankakuString(value)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 指定されたフィールドが全角文字列であることをチェックする。
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param errors エラー
     * @return 入力値が正しければ true
     */
    public boolean validateZenkakuString(Object bean,
            ValidatorAction va, Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証
        if (!ValidationUtil.isZenkakuString(value)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 指定されたフィールドが全角カタカナ文字列で あることをチェックする。<br>
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param errors エラー
     * @return 入力値が正しければ true
     */
    public boolean validateZenkakuKanaString(Object bean,
            ValidatorAction va, Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証
        if (!ValidationUtil.isZenkakuKanaString(value)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 指定されたフィールドに入力禁止文字列が混入しているか
     * どうかをチェックする。
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * <p>以下は、禁止文字列チェックの設定例である。
     *
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;stringField&quot;
     *      depends=&quot;prohibited&quot;&gt;
     *    &lt;arg key=&quot;sample.stringField&quot; position="0"/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;chars&lt;/var-name&gt;
     *      &lt;var-value&gt;!&quot;#$%&amp;'()&lt;&gt;&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> chars </td>
     *   <td>入力禁止キャラクタ</td>
     *   <td> true </td>
     *   <td>入力を禁止する文字。設定されない場合は、ValidatorException
     *   がスローされる。</td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param errors エラー
     * @return 入力値が正しければ true
     * @exception ValidatorException 設定ファイルからcharsの値が取得できない
     * 場合にスローされる。
     */
    public boolean validateProhibited(Object bean, ValidatorAction va,
            Field field, ValidationErrors errors)
            throws ValidatorException {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 入力禁止文字列
        String prohibitedStr = field.getVarValue("chars");

        // charsが取得できない場合はValidatorExceptionをスロー
        if (StringUtils.isEmpty(prohibitedStr)) {
            log.error("var[chars] must be specified.");
            throw new ValidatorException("var[chars] must be specified.");
        }

        // 検証
        if (!ValidationUtil.hasNotProhibitedChar(value, prohibitedStr)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 指定されたフィールドが半角数字であることをチェックする。
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param errors エラー
     * @return 入力値が正しければ true
     */
    public boolean validateNumericString(Object bean,
            ValidatorAction va, Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 検証
        if (!ValidationUtil.isNumericString(value)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 指定されたフィールドの文字列長が一致していることをチェックする。
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * <p>以下は、文字列長が5であるときのみtrueを返却する
     * 検証の設定例である。
     *
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;stringField&quot;
     *      depends=&quot;stringLength&quot;&gt;
     *    &lt;arg key=&quot;sample.stringField&quot; position="0"/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;stringLength&lt;/var-name&gt;
     *      &lt;var-value&gt;5&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> stringLength </td>
     *   <td>入力文字列長</td>
     *   <td> false </td>
     *   <td>入力文字列長を指定する。
     *        省略時は、<code>int</code>型の最大値が指定される。</td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param errors エラー
     * @return 入力値が正しければ true
     * @throws ValidatorException validation定義ファイルの設定ミスがあった場合
     * スローされる例外。
     */
    public boolean validateStringLength(Object bean,
            ValidatorAction va, Field field, ValidationErrors errors)
            throws ValidatorException {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 文字列長
        int length = Integer.MAX_VALUE;
        String lengthStr = field.getVarValue("stringLength");

        try {
            length = Integer.valueOf(lengthStr).intValue();
        } catch (NumberFormatException e) {
            String message = "Mistake on validation definition file. "
                + "- stringLength is not number. "
                + "You'll have to check it over. ";
            log.error(message, e);
            throw new ValidatorException(message);
        }

        // 検証
        if (value.length() != length) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 指定されたフィールドの配列・コレクションの長さが、
     * 指定数の範囲内であることをチェックする。
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * <p>以下は、配列・コレクションの長さが４～７であるときのみtrueを返却する
     * 検証の設定例である。
     *
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;arrayField&quot;
     *      depends=&quot;arrayRange&quot;&gt;
     *    &lt;arg key=&quot;sample.arrayField&quot; position="0"/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;minArrayLength&lt;/var-name&gt;
     *      &lt;var-value&gt;4&lt;/var-value&gt;
     *    &lt;/var&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;maxArrayLength&lt;/var-name&gt;
     *      &lt;var-value&gt;7&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> minArrayLength </td>
     *   <td>最小配列数</td>
     *   <td>false</td>
     *   <td>配列の最小配列数を指定する。
     *        最小配列数の指定がない場合、０が指定される。</td>
     *  </tr>
     *  <tr>
     *   <td> maxArrayLength </td>
     *   <td>最大配列数</td>
     *   <td>false</td>
     *   <td>配列の最大配列数を指定する。
     *        最大配列数の指定がない場合、
     *        <code>int</code>型の最大値が指定される。</td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param errors エラー
     * @return 入力値が正しければ true
     * @throws ValidatorException validation定義ファイルの設定ミスがあった場合
     * スローされる例外。
     */
    public boolean validateArrayRange(Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) throws ValidatorException {

        // チェック対象のbeanがnullの場合はValidatorExceptionをスロー
        if (bean == null) {
            String message = "target of validateArrayRange must be not null.";
            log.error(message);
            throw new ValidatorException(message);
        }

        try {
            Class type =
                BeanUtil.getBeanPropertyType(bean, field.getProperty());
            if (type == null) {
                String message = "Cannot get property type[" +
                    bean.getClass().getName() + "." +
                    field.getProperty() + "]";
                log.error(message);
                throw new ValidatorException(message);
            } else if (!type.isArray()
                    && !Collection.class.isAssignableFrom(type)) {
                String message = "property [" + bean.getClass().getName() +
                    "." + field.getProperty() +
                    "] must be instance of Array or Collection.";
                log.error(message);
                throw new ValidatorException(message);
            }
        } catch (PropertyAccessException e) {
            String message = "Cannot get property type[" +
                bean.getClass().getName() + "." +
                field.getProperty() + "]";
            log.error(message, e);
            throw new ValidatorException(message);
        }

        // 検証値
        Object obj = null;
        try {
            obj = BeanUtil.getBeanProperty(bean, field.getProperty());
        } catch (PropertyAccessException e) {
            String message = "Cannot get property [" +
                bean.getClass().getName() +
                "." + field.getProperty() + "]";
            log.error(message, e);
            throw new ValidatorException(message);
        }

        // 指定する配列の最小サイズ
        int min = 0;
        String minStr = field.getVarValue("minArrayLength");
        if (!GenericValidator.isBlankOrNull(minStr)) {
            try {
                min = Integer.parseInt(minStr);
            } catch (NumberFormatException e) {
                String message = "Mistake on validation definition file. "
                    + "- minArrayLength is not number. "
                    + "You'll have to check it over. ";
                log.error(message, e);
                throw new ValidatorException(message);
            }
        }

        // 指定する配列の最大サイズ
        int max = Integer.MAX_VALUE;
        String maxStr = field.getVarValue("maxArrayLength");
        if (!GenericValidator.isBlankOrNull(maxStr)) {
            try {
                max = Integer.parseInt(maxStr);
            } catch (NumberFormatException e) {
                String message = "Mistake on validation definition file. "
                    + "- maxArrayLength is not number. "
                    + "You'll have to check it over. ";
                log.error(message, e);
                throw new ValidatorException(message);
            }
        }

        // 検証
        try {
            if (!ValidationUtil.isArrayInRange(obj, min, max)) {
                rejectValue(errors, field, va, bean);
                return false;
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new ValidatorException(e.getMessage());
        }
        return true;
    }

    /**
     * 指定されたフィールドがURL形式かどうかチェックする。<br>
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * <p>以下は、HTTP、FTPプロトコル指定可能、ダブルスラッシュ指定可能、
     * URLの分割可能である検証の設定例である。
     * </p>
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;date&quot;
     *      depends=&quot;url&quot;&gt;
     *    &lt;arg key=&quot;label.date&quot; position=&quot;0&quot;/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;allowallschemes&lt;/var-name&gt;
     *      &lt;var-value&gt;false&lt;/var-value&gt;
     *    &lt;/var&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;allow2slashes&lt;/var-name&gt;
     *      &lt;var-value&gt;true&lt;/var-value&gt;
     *    &lt;/var&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;nofragments&lt;/var-name&gt;
     *      &lt;var-value&gt;false&lt;/var-value&gt;
     *    &lt;/var&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;schemes&lt;/var-name&gt;
     *      &lt;var-value&gt;http,ftp&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> allowallschemes </td>
     *   <td>true（or false）</td>
     *   <td>false</td>
     *   <td>全てのスキームを許可するか判断するフラグ。デフォルトfalse。</td>
     *  </tr>
     *  <tr>
     *   <td> allow2slashes </td>
     *   <td>true（or false）</td>
     *   <td>false</td>
     *   <td>ダブルスラッシュを許可するか判断するフラグ。デフォルトfalse。</td>
     *  </tr>
     *  <tr>
     *   <td> nofragments </td>
     *   <td>true（or false）</td>
     *   <td>false</td>
     *   <td>URL分割禁止か判断するフラグ。デフォルトfalse。</td>
     *  </tr>
     *  <tr>
     *   <td> schemes </td>
     *   <td>プロトコル</td>
     *   <td>false</td>
     *   <td>許可するスキーム。複数ある場合はカンマ区切りで指定。
     *       デフォルトはhttp, https, ftp。</td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param errors エラー
     * @return 入力値が正しければ true
     */
    public boolean validateUrl(Object bean, ValidatorAction va,
            Field field, ValidationErrors errors) {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // オプションの変数を取得する
        boolean allowallschemes =
            "true".equals(field.getVarValue("allowallschemes"));
        boolean allow2slashes =
            "true".equals(field.getVarValue("allow2slashes"));
        boolean nofragments =
            "true".equals(field.getVarValue("nofragments"));
        String schemesVar = allowallschemes ? null : field
                .getVarValue("schemes");

        // 検証
        if (!ValidationUtil.isUrl(
                value, allowallschemes, allow2slashes,
                nofragments, schemesVar)) {
            rejectValue(errors, field, va, bean);
            return false;
        }
        return true;
    }

    /**
     * 指定されたフィールドのバイト列長が指定した範囲内であることを
     * チェックする。
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * <p>以下は、バイト列長が5以上、10以下であるときのみ true
     * を返却する検証の設定例である。
     *
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;stringField&quot;
     *      depends=&quot;byteRange&quot;&gt;
     *    &lt;arg key=&quot;sample.stringField&quot; position="0"/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;maxByteLength&lt;/var-name&gt;
     *      &lt;var-value&gt;10&lt;/var-value&gt;
     *    &lt;/var&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;minByteLength&lt;/var-name&gt;
     *      &lt;var-value&gt;5&lt;/var-value&gt;
     *    &lt;/var&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;encoding&lt;/var-name&gt;
     *      &lt;var-value&gt;Windows-31J&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> maxByteLength </td>
     *   <td>最大バイト数</td>
     *   <td>false</td>
     *   <td>入力文字列バイト長を検証するための最大バイト長。
     *        省略時は、<code>int</code>型の最大値が指定される。</td>
     *  </tr>
     *  <tr>
     *   <td> minByteLength </td>
     *   <td>最小バイト数</td>
     *   <td>false</td>
     *   <td>入力文字列バイト長を検証するための最小バイト長。
     *        省略時は、0が指定される。</td>
     *  </tr>
     *  <tr>
     *   <td> encoding </td>
     *   <td>文字エンコーディング</td>
     *   <td>false</td>
     *   <td>入力文字をバイト列に変換する際に使用する文字エンコーディング。
     *   省略された場合はVMのデフォルトエンコーディングが使用される。</td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のオブジェクト
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドオブジェクト
     * @param errors エラー
     * @return 入力値が正しければ true
     * @throws ValidatorException validation定義ファイルの設定ミスがあった場合
     * スローされる例外。
     */
    public boolean validateByteRange(Object bean,
            ValidatorAction va, Field field, ValidationErrors errors)
            throws ValidatorException {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // エンコーディング
        String encoding = field.getVarValue("encoding");

        // 最小バイト列長
        int min = 0;
        String minStr = field.getVarValue("minByteLength");
        if (!GenericValidator.isBlankOrNull(minStr)) {
            try {
                min = Integer.parseInt(minStr);
            } catch (NumberFormatException e) {
                String message = "Mistake on validation definition file. "
                    + "- minByteLength is not number. "
                    + "You'll have to check it over. ";
                log.error(message, e);
                throw new ValidatorException(message);
            }
        }

        // 最大バイト列長
        int max = Integer.MAX_VALUE;
        String maxStr = field.getVarValue("maxByteLength");
        if (!GenericValidator.isBlankOrNull(maxStr)) {
            try {
                max = Integer.parseInt(maxStr);
            } catch (NumberFormatException e) {
                String message = "Mistake on validation definition file. "
                    + "- maxByteLength is not number. "
                    + "You'll have to check it over. ";
                log.error(message, e);
                throw new ValidatorException(message);
            }
        }

        // 検証
        try {
            if (!ValidationUtil.isByteInRange(value, encoding, min, max)) {
                rejectValue(errors, field, va, bean);
                return false;
            }
        } catch (IllegalArgumentException e) {
            log.error("encoding[" + encoding + "] is not supported.");
            throw new ValidatorException("encoding[" + encoding +
                    "] is not supported.");
        }
        return true;
    }

    /**
     * 日付が指定した範囲内であるかどうかチェックする。
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * <p>以下は、日付が2000/01/01から2010/12/31の範囲内であるかどうかの
     * 検証の設定例である。
     *
     * <h5>validation.xmlの記述例</h5>
     * <code><pre>
     * &lt;form name=&quot;sample&quot;&gt;
     *  ・・・
     *  &lt;field property=&quot;date&quot;
     *      depends=&quot;dateRange&quot;&gt;
     *    &lt;arg key=&quot;date&quot; position=&quot;0&quot;/&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;startDate&lt;/var-name&gt;
     *      &lt;var-value&gt;2000/01/01&lt;/var-value&gt;
     *    &lt;/var&gt;
     *    &lt;var&gt;
     *      &lt;var-name&gt;endDate&lt;/var-name&gt;
     *      &lt;var-value&gt;2010/12/31&lt;/var-value&gt;
     *    &lt;/var&gt;
     *  &lt;/field&gt;
     *  ・・・
     * &lt;/form&gt;
     * </pre></code>
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> startDate </td>
     *   <td>開始日付</td>
     *   <td>false</td>
     *   <td>日付範囲の開始の閾値となる日付。
     *   日付パターンで指定した形式で設定すること。</td>
     *  </tr>
     *  <tr>
     *   <td> endDate </td>
     *   <td>終了日付</td>
     *   <td>false</td>
     *   <td>日付範囲の終了の閾値となる日付。
     *   日付パターンで指定した形式で設定すること。</td>
     *  </tr>
     *  <tr>
     *   <td> datePattern </td>
     *   <td>日付パターン</td>
     *   <td>false</td>
     *   <td>日付パターンを指定する。入力値の桁数チェックは行わない。
     *   たとえば、日付パターンがyyyy/MM/ddの場合、2001/1/1はエラーにならない。
     *   datePatternとdatePatternStrictが両方指定されている場合は、
     *   datePatternが優先して使用される。
     *   </td>
     *  </tr>
     *  <tr>
     *   <td> datePatternStrict </td>
     *   <td>日付パターン</td>
     *   <td>false</td>
     *   <td>日付パターンを指定する。入力値の桁数が、
     *   指定された日付パターンの桁数に合致するかのチェックを行う。
     *   たとえば、日付パターンがyyyy/MM/ddの場合、2001/1/1はエラーになる。
     *   datePatternとdatePatternStrictが両方指定されている場合は、
     *   datePatternが優先して使用される。
     *   </td>
     *  </tr>

     * </table>
     *
     * @param bean 検査対象のオブジェクト
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドオブジェクト
     * @param errors エラー
     * @return 入力値が正しければ true
     * @throws ValidatorException datePatternまたはdatePatternStrictに不正な
     * パターン文字が含まれる場合、startDateまたはendDateが日付に変換できない
     * 場合にスローされる
     */
    public boolean validateDateRange(Object bean,
            ValidatorAction va, Field field, ValidationErrors errors)
            throws ValidatorException {
        // 検証値
        String value = extractValue(bean, field);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 日付入力パターン
        String datePattern = field.getVarValue("datePattern");
        String datePatternStrict = field.getVarValue("datePatternStrict");

        // 範囲指定する日付
        String startDateStr = field.getVarValue("startDate");
        String endDateStr = field.getVarValue("endDate");

        // 検証
        try {
            if (!ValidationUtil.isDateInRange(value, startDateStr, endDateStr,
                    datePattern, datePatternStrict)) {
                rejectValue(errors, field, va, bean);
                return false;
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new ValidatorException(e.getMessage());
        }
        return true;
    }

    /**
     * 指定されたフィールドに一致する全てのプロパティ値をチェックする。
     * チェックに引っかかった場合は、エラー情報をerrorsに追加し、
     * falseを返却する。
     *
     * <p>配列・コレクション型に対してのフィールドチェックが可能である。
     * 配列・コレクションの全ての要素に対して、validation.xmlで
     * 定義したdepends名から”Array”を取ったチェックルールを呼び出す。
     * depends="requiredArray"　⇒ "required" （必須チェック）
     *
     * @param bean 検査対象のJavaBeanインスタンス
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param errors エラー
     * @return 要素すべての入力値が正しければ true
     * @throws ValidatorException validation定義ファイルの設定ミスがあった場合
     * 、チェック対象のbeanがnullである場合にスローされる例外。
     */
    public boolean validateArraysIndex(Object bean,
            ValidatorAction va, Field field, ValidationErrors errors)
            throws ValidatorException {
        if (bean == null) {
            log.error("validation target bean is null.");
            throw new ValidatorException("validation target bean is null.");
        }

        Class[] paramClass = null;  // 検証メソッドの引数の型
        Method method = null;       // 検証メソッド
        try {
            paramClass = getParamClass(va);
            if (paramClass == null || paramClass.length == 0) {
                String message = "Mistake on validation rule file. "
                    + "- Can not get argument class. "
                    + "You'll have to check it over. ";
                log.error(message);
                throw new ValidatorException(message);
            }
            
            method = getMethod(va, paramClass);
            if (method == null) {
                String message = "Mistake on validation rule file. "
                    + "- Can not get validateMethod. "
                    + "You'll have to check it over. ";
                log.error(message);
                throw new ValidatorException(message);
            }
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            throw new ValidatorException(e.getMessage());
        }

        try {
            // 配列のインデックスが変わっても、値が変化しないパラメータ
            Object[] argParams = new Object[paramClass.length];
            argParams[0] = bean;
            argParams[1] = va;
            argParams[3] = errors;

            // 検証値（配列要素）の取り出し
            IndexedBeanWrapper bw = getIndexedBeanWrapper(bean);
            Map<String, Object> propertyMap =
                bw.getIndexedPropertyValues(field.getKey());

            boolean isValid = true; // 検証フラグ

            for (String key : propertyMap.keySet()) {
                // インデックス付きのプロパティ名でフィールドを書き換える
                Field indexedField = (Field) field.clone();
                indexedField.setKey(key);
                indexedField.setProperty(key);

                argParams[2] = indexedField; // フィールド

                // 入力チェックメソッドの呼び出し
                boolean bool = (Boolean) method.invoke(
                        this, argParams);
                if (!bool) {
                    isValid = false;
                }
            }
            return isValid;
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (t instanceof ValidatorException) {
                throw (ValidatorException) t;
            } 
            log.error(t.getMessage(), t);
            throw new ValidatorException(t.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ValidatorException(e.getMessage());
        }
    }

    /**
     * IndexedBeanWrapper実装クラスを取得する。
     * @param bean ターゲットのBean
     * @return IndexedBeanWrapperの実装
     */
	protected IndexedBeanWrapper getIndexedBeanWrapper(Object bean) {
		return new JXPathIndexedBeanWrapperImpl(bean);
	}

    /**
     * 検証ルールに渡される引数クラス配列を取得する。
     *
     * @param va Validatorにより用意されたValidatorAction
     * @return 引数クラス配列
     */
    protected Class[] getParamClass(ValidatorAction va) {

        StringTokenizer st = new StringTokenizer(va.getMethodParams(), ",");
        Class[] paramClass = new Class[st.countTokens()];

        for (int i = 0; st.hasMoreTokens(); i++) {
            try {
                String key = st.nextToken().trim();
                paramClass[i] = ClassUtils.getClass(key);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        return paramClass;
    }

    /**
     * 配列・コレクションの要素を検証するメソッドを取得する。
     *
     * @param va ValidatorAction
     * @param paramClass 引数クラス配列
     * @return 検証メソッド
     */
    protected Method getMethod(
            ValidatorAction va, Class[] paramClass) {

        String methodNameSource = va.getName();
        if (methodNameSource == null || "".equals(methodNameSource)) {
            // メソッド名指定がnullまたは空文字のときnull返却。
            return null;
        }

        // name属性から"Array"を除去したメソッド名を生成する
        // xxxxArray→validateXxxx
        char[] chars = methodNameSource.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        String validate = "validate" + new String(chars);
        String methodName = validate.substring(0, validate.length()
                - "Array".length());

        Method method = null;
        try {
            method = FieldChecks.class.getMethod(methodName, paramClass);
        } catch (NoSuchMethodException e) {
            return null;
        }
        return method;
    }

    /**
     * 複数フィールドの相関チェックを行う。
     *
     * この検証ルールの実行には{@link MultiFieldValidator} の実装クラスが必要。<br>
     * 実装したクラスは <code>validation.xml</code> に設定を行う。<br>
     * エラーとなった場合には、エラー情報を生成し、
     * 指定されたエラー情報リストに追加する。
     * この検証ルールにはデフォルトのエラーメッセージがないため、
     * メッセージは <code>validation.xml</code> に必ず記述すること。<br>
     * valueフィールドの値が、value1フィールドの値以上、
     * value2フィールドの値以下であることを検証する場合、以下のように実装、
     * 設定を行う。
     * <h5>{@link MultiFieldValidator} の実装例</h5>
     * <code><pre>
     * public boolean validate(Object value, Object[] depends) {
     *     int value0 = Integer.parseInt(value);
     *     int value1 = Integer.parseInt(depends[0]);
     *     int value2 = Integer.parseInt(depends[1]);
     *     return (value1 <= value0 && value2 >= value0);
     * }
     * </pre></code>
     * <h5>validation.xmlの設定例</h5>
     * <code><pre>
     * &lt;form name=&quot;/validateMultiField&quot;&gt;
     *   &lt;field property=&quot;value&quot; depends=&quot;multiField&quot;&gt;
     *     &lt;msg key=&quot;errors.multiField&quot;
     *             name=&quot;multiField&quot;/&gt;
     *     &lt;arg key=&quot;label.value&quot; position=&quot;0&quot; /&gt;
     *     &lt;arg key=&quot;label.value1&quot; position=&quot;1&quot; /&gt;
     *     &lt;arg key=&quot;label.value2&quot; position=&quot;2&quot; /&gt;
     *     &lt;var&gt;
     *       &lt;var-name&gt;fields&lt;/var-name&gt;
     *       &lt;var-value&gt;value1,value2&lt;/var-value&gt;
     *     &lt;/var&gt;
     *     &lt;var&gt;
     *       &lt;var-name&gt;multiFieldValidator&lt;/var-name&gt;
     *       &lt;var-value&gt;sample.SampleMultiFieldValidator&lt;/var-value&gt;
     *     &lt;/var&gt;
     *   &lt;/field&gt;
     * &lt;/form&gt;
     * </pre></code>
     * <h5>メッセージリソースファイルの設定例</h5>
     * <code>
     * errors.multiField={0}は{1}から{2}の間の値を入力してください。
     * </code>
     *
     * <h5>validation.xmlに設定を要する&lt;var&gt;要素</h5>
     * <table border="1">
     *  <tr>
     *   <td><center><b><code>var-name</code></b></center></td>
     *   <td><center><b><code>var-value</code></b></center></td>
     *   <td><center><b>必須性</b></center></td>
     *   <td><center><b>概要</b></center></td>
     *  </tr>
     *  <tr>
     *   <td> fields </td>
     *   <td>検証に必要な他のフィールド名</td>
     *   <td>false</td>
     *   <td>複数のフィールドを指定する場合はフィールド名をカンマ区切りで
     *   指定する。</td>
     *  </tr>
     *  <tr>
     *   <td> multiFieldValidator </td>
     *   <td>{@link MultiFieldValidator} 実装クラス名</td>
     *   <td>true</td>
     *   <td>複数フィールドの相関チェックを行う {@link MultiFieldValidator}
     *   実装クラス名。</td>
     *  </tr>
     * </table>
     *
     * @param bean 検査対象のオブジェクト
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドオブジェクト
     * @param errors エラー
     * @return 入力値が正しければ <code>true</code>
     */
    public boolean validateMultiField(Object bean,
                                        ValidatorAction va,
                                        Field field,
                                        ValidationErrors errors) {

        // beanがnullの時、エラーログを出力し、trueを返却する。
        if (bean == null) {
            log.error("bean is null.");
            return true;
        }

        // 検証対象の値を取得
        Object value = null;
        if (bean instanceof String) {
            value = bean;
        } else {
            try {
                value = PropertyUtils.getProperty(bean, field.getProperty());
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                log.error(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage(), e);
            }
        }
        // 他のフィールドに依存した必須入力チェックを考慮し、
        // 検証値がnullまたは空文字の場合でも複数フィールドチェックは実行する。
        
        // MultiFieldValidator実装クラス名を取得
        String multiFieldValidatorClass
            = field.getVarValue("multiFieldValidator");

        if (multiFieldValidatorClass == null
                || "".equals(multiFieldValidatorClass)) {
            log.error("var value[multiFieldValidator] is required.");
            throw new IllegalArgumentException(
                    "var value[multiFieldValidator] is required.");
        }

        MultiFieldValidator mfv = null;
        try {
            mfv = (MultiFieldValidator) ClassUtil.create(
                    multiFieldValidatorClass);
        } catch (ClassLoadException e) {
            log.error("var value[multiFieldValidator] is invalid.", e);
            throw new IllegalArgumentException(
                    "var value[multiFieldValidator] is invalid.", e);
        } catch (ClassCastException e) {
            log.error("var value[multiFieldValidator] is invalid.", e);
            throw new IllegalArgumentException(
                    "var value[multiFieldValidator] is invalid.", e);
        }
        
        // 検証の依存フィールドの値を取得
        String fields = field.getVarValue("fields");
        List<Object> valueList = new ArrayList<Object>();
        if (fields != null) {
            StringTokenizer st = new StringTokenizer(fields, ",");
            while (st.hasMoreTokens()) {
                String f = st.nextToken();
                f = f.trim();
                try {
                    valueList.add(PropertyUtils.getProperty(bean, f));
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    log.error(e.getMessage(), e);
                } catch (NoSuchMethodException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        
        if (log.isDebugEnabled()) {
            log.debug("dependent fields:" + valueList);
        }

        Object[] values = new Object[valueList.size()];

        valueList.toArray(values);

        boolean result = mfv.validate(value, values);

        if (!result) {
            rejectValue(errors, field, va, bean);
            return false;
        }

        return true;
    }

    /**
     * オブジェクトから検証値を取り出す。beanがNullの場合はNullを返す。
     * beanがString型の場合、beanを返す。
     * beanがNumber型とBoolean型とCharacter型の場合、bean.toString()を返す。
     * それ以外の場合、beanと<code>Field</code>オブジェクトの値から、
     * <code>ValidatorUtils</code>クラスのメソッドを利用して値を取得する。
     *
     * @param bean 検証対象のオブジェクト。
     * @param field <code>Field</code>オブジェクト。
     * @return 検証値。
     * @see ValidatorUtils#getValueAsString(Object, String)
     */
    protected String extractValue(Object bean, Field field) {
        String value = null;

        if (bean == null) {
            return null;
        } else if (bean instanceof String) {
            value = (String) bean;
        } else if (bean instanceof Number
            ||  bean instanceof Boolean
            ||  bean instanceof Character) {
            value = bean.toString();
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }
        return value;
    }

    /**
     * 入力チェックエラーが生じた場合に、エラー情報を、
     * TERASOLUNA共通のエラーインタフェースに引き渡す。
     *
     * @param errors エラー
     * @param va Validatorにより用意されたValidatorAction
     * @param field フィールドインスタンス
     * @param bean 入力エラーが生じたJavaBeanインスタンス
     */
    protected void rejectValue(ValidationErrors errors, Field field,
            ValidatorAction va, Object bean) {
        errors.addError(bean, field, va);
    }
}