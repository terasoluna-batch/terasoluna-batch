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

package jp.terasoluna.fw.validation.springmodules;

import org.springframework.validation.Errors;
import org.springmodules.validation.commons.DefaultBeanValidator;

/**
 * 相関チェックを行なう抽象クラス。
 * 
 * <p>Spring-Modules Validatorによる単項目チェック以外に相関にチェックを
 * 行ないたい場合に使用する。
 * ただし、DBアクセスが必要なチェックは各業務クラスにてチェックすること。
 * サブクラスは{@link #validateMultiField(Object, Errors)}メソッドを
 * オーバーライドし、チェックロジックを記述する。</p>
 * 
 * <h5>サブクラスの実装例</h5>
 * 
 * <p>サブクラスは{@link #validateMultiField(Object, Errors)}メソッドを
 * オーバーライドする。
 * 引数のobjは検査対象のJavaBeanであるため、各JavaBeanの型にキャストしてから
 * 属性を取り出す。
 * エラーを追加する場合、引数errorsのrejectメソッド、rejectValueメソッドを
 * 実行する。 </p>
 * 
 * <pre>
 * protected void validateMultiField(Object obj, Errors errors) {
 *
 *     // JavaBeanの取得
 *     SampleBean bean = (SampleBean) obj;
 *
 *     // １つ目の属性
 *     String field1 = bean.getField1();
 * 
 *     // ２つ目の属性
 *     String field2 = bean.getField2();
 *       
 *     // １つ目の属性と２つ目の属性が同じではない場合、エラーを追加する
 *     if (!field1.equals(field2) {
 *         // エラーを追加する
 *         errors.reject("errors.sample");
 *     }
 * }
 * </pre>
 * 
 * <h5>Errorsインタフェースの代表的なエラー追加メソッド</h5>
 * 
 * <table border="1">
 * <tr>
 *  <td><center><b>メソッド名</b></center></td>
 *  <td><center><b>説明</b></center></td>
 * </tr>
 * 
 * <tr>
 *  <td>void rejectValue(String field, String errorCode)</center></td>
 *  <td>エラーを追加する。fieldにはJavaBeanのプロパティ名、
 * errorCodeにはリソースバンドルのキーを指定する。
 * 置換文字列がないエラーに使用する。</td>
 * </tr>
 * <tr>
 *  <td>void rejectValue(String field, String errorCode, Object[] errorArgs, 
 *  String defaultMessage)</center></td>
 *  <td>エラーを追加する。field、errorCodeは上記と同様。errorArgsは置換文字列、
 * defaultMessageはデフォルトメッセージを指定する。（Terasolunaでは
 * デフォルトメッセージは使用しないため、任意の文字列を設定する）</td>
 * </tr>
 * <tr>
 *  <td>void reject(String errorCode)</center></td>
 *  <td>エラーを追加する。リソースバンドルのキーのみ指定する。
 * field情報を指定しない相関チェック等に使用する。</td>
 * </tr>
 * <tr>
 *  <td>void reject(String errorCode, Object[] errorArgs, 
 *  String defaultMessage)</center></td>
 *  <td>エラーを追加する。errorArgsは置換文字列、defaultMessageは
 *  デフォルトメッセージを指定する。（Terasolunaでは
 *  デフォルトメッセージは使用しないため、任意の文字列を設定する）
 * field情報を指定しない相関チェック等に使用する。</td>
 * </tr>
 * </table>
 * 
 * <h5>Bean定義ファイルの記述例（入力チェッククラス）</h5>
 * 
 * <p>Spring上でこの入力チェック機能を使用する場合、
 * 入力チェックファクトリ（入力チェッククラスの初期化を行なうクラス）と
 * 入力チェッククラスをBean定義ファイルに設定する必要がある。</p>
 * <pre>
 * &lt;!-- 入力チェッククラスのファクトリ --&gt;
 * &lt;bean id="validatorFactory"     
 *   class="org.springmodules.commons.validator.DefaultValidatorFactory"&gt; 
 *   &lt;property name="validationConfigLocations"&gt; 
 *     &lt;list&gt; 
 *       &lt;!-- ここに記述したバリデーション定義ファイル（validation.xml）や
 *            バリデーションルール定義ファイル（validation-rules.xml）を
 *            Springフレームワークが設定ファイルとして認識する --&gt; 
 *       &lt;value&gt;<b>/WEB-INF/validation/validator-rules.xml</b>&lt;/value&gt;
 *       &lt;value&gt;<b>/WEB-INF/validation/validator-rules-ex.xml</b>&lt;/value&gt;
 *       &lt;value&gt;<b>/WEB-INF/validation/validation.xml</b>&lt;/value&gt; 
 *     &lt;/list&gt; 
 *   &lt;/property&gt; 
 * &lt;/bean&gt; 
 *   
 * &lt;!--　相関チェックを行なう入力チェッククラス 
 *   class属性に作成した相関チェッククラスを記述する--&gt;
 * &lt;bean id="sampleValidator"  <b>class="jp.terasoluna.sample2.validation.SampleMultiFieldValidator"</b>&gt; 
 *   &lt;property name="validatorFactory"&gt;&lt;ref local="validatorFactory"/&gt;&lt;/property&gt; 
 * &lt;/bean&gt;
 * </pre>
 * 
 * <h5>Bean定義ファイルの記述例（コントローラ）</h5>
 * 
 * <p>Bean定義ファイルで設定した入力チェック機能を使用するために、
 * コントローラのvalidator属性に上記で設定した入力チェック機能を
 * 設定する必要がある。</p>
 * <pre>
 * &lt;!-- サンプル入力チェック業務 --&gt;
 * &lt;bean name="/secure/blogic/validateSample.do" 
 *   class="jp.terasoluna.sample2.web.controller.ValidateSampleController"
 *   parent="xmlRequestController"&gt;  
 *   &lt;property name="sumService" ref="sumService"/&gt;  
 *   &lt;property name=<b>"validator"</b> ref=<b>“sampleValidator"</b>/&gt;
 *   &lt;property name="commandClass"&gt;
 *     &lt;value&gt;jp.terasoluna.sample2.dto.SampleDto&lt;/value&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * @see org.springmodules.validation.commons.DefaultBeanValidator
 * @see org.springframework.validation.BindException
 * @see org.springframework.validation.Errors
 */
public abstract class BaseMultiFieldValidator extends DefaultBeanValidator {

    /**
     * 入力チェックメソッド。
     * 設定ファイルによる単項目チェックを呼び出し、
     * エラーがなければ{@link #validateMultiField(Object, Errors)}
     * メソッドを呼び出す。
     * 
     * @param obj 検査対象のJavaBean
     * @param errors エラー
     */
    @Override
    public void validate(Object obj, Errors errors) {
        
        // 設定ファイルによる単項目チェック
        super.validate(obj, errors);
        
        // エラーがなければ相関チェックを行なう
        if (!errors.hasErrors()) {
            validateMultiField(obj, errors);
        }
    }

    /**
     * 相関チェックを行なうメソッド。
     * 単項目チェックでエラーが発生しない場合、呼び出される。
     * サブクラスはこのメソッドを実装する
     * 
     * @param obj 検査対象のJavaBean
     * @param errors エラー
     */
    protected abstract void validateMultiField(Object obj, Errors errors);
}
