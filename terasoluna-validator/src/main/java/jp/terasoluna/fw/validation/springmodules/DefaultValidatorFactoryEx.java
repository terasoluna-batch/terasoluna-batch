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

import org.apache.commons.validator.Validator;
import org.springframework.validation.Errors;
import org.springmodules.validation.commons.DefaultValidatorFactory;

/**
 * terasoluna-validator-springのCommonsValidatorExインスタンスを生成するファクトリクラス。
 * 
 * <p>{@link jp.terasoluna.fw.validation.FieldChecks}クラスにて
 * 使用されるエラーインタフェースの実装に
 * Springフレームワークの{@link org.springframework.validation.Errors}クラスを
 * 扱うためのクラス。
 * Springフレームワーク、Spring-Modulesを使用することを前提としており、
 * Bean定義ファイルに以下のような設定が必要となる。</p>
 * 
 * <hr>
 * 
 * <h5>Bean定義ファイル設定例</h5>
 * <code><pre>
 * &lt;!-- Validatorファクトリ設定 --&gt;
 * &lt;bean id="validatorFactory" 
 *   class="jp.terasoluna.fw.validation.springmodules.DefaultValidatorFactoryEx"&gt; 
 *   &lt;property name="validationConfigLocations"&gt; 
 *     &lt;list&gt; 
 *       &lt;value&gt;/WEB-INF/validation/validator-rules.xml&lt;/value&gt;
 *       &lt;value&gt;/WEB-INF/validation/validator-rules-ex.xml&lt;/value&gt;
 *       &lt;value&gt;/WEB-INF/validation/validation.xml&lt;/value&gt; 
 *     &lt;/list&gt; 
 *   &lt;/property&gt; 
 * &lt;/bean&gt; 
 *   
 * &lt;!-- Validator設定 --&gt;
 * &lt;bean id="beanValidator" class="jp.terasoluna.fw.validation.springmodules.DefaultBeanValidatorEx"&gt; 
 *   &lt;property name="validatorFactory"&gt;&lt;ref local="validatorFactory"/&gt;&lt;/property&gt; 
 * &lt;/bean&gt;
 * </pre></code>
 * 
 * @see jp.terasoluna.fw.validation.ValidationErrors
 * @see jp.terasoluna.fw.validation.springmodules.SpringValidationErrors
 *
 */
public class DefaultValidatorFactoryEx extends DefaultValidatorFactory {

    /**
     * Validatorインスタンスに設定するエラーオブジェクトのキー。
     */
    public static final String TERASOLUNA_ERRORS_KEY = 
        "jp.terasoluna.fw.validation.ValidationErrors";
    
    /**
     * Validatorインスタンスを取得する。
     * 
     * @param beanName 検証するJavaBeanの名前。
     * @param bean 検証対象のJavaBean。
     * @param errors Springフレームワークのエラー情報。
     * @return Validatorインスタンス。
     */
    @Override
    public Validator getValidator(
            String beanName, Object bean, Errors errors) {
        Validator validator = 
            new CommonsValidatorEx(getValidatorResources(), beanName);
        
        // BindExceptionをラップしたエラークラスをValidatorに設定する
        SpringValidationErrors commonErrors = createSpringValidationErrors();
        commonErrors.setErrors(errors);
        validator.setParameter(TERASOLUNA_ERRORS_KEY, commonErrors);
        
        validator.setParameter(Validator.BEAN_PARAM, bean);
        return validator;
    }
    
    /**
     * 共通入力値検証エラーオブジェクトを生成する。
     * @return 共通入力値検証エラーオブジェクト。
     */
    protected SpringValidationErrors createSpringValidationErrors() {
        return new SpringValidationErrors();
    }
}
