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

import jp.terasoluna.fw.validation.ValidationErrors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.springframework.validation.Errors;
import org.springmodules.validation.commons.MessageUtils;

/**
 * {@link jp.terasoluna.fw.validation.FieldChecks}クラスにて、
 * Springフレームワークの{@link org.springframework.validation.Errors}
 * インタフェースにエラー情報を追加するためのクラス。
 * 
 * <p>本クラスは{@link jp.terasoluna.fw.validation.FieldChecks}クラス
 * にて使用されているエラーインタフェース
 * {@link jp.terasoluna.fw.validation.ValidationErrors}
 * の実装クラスである。
 * {@link jp.terasoluna.fw.validation.FieldChecks}クラスにて
 * Validator生成クラスの拡張クラスである
 * {@link jp.terasoluna.fw.validation.springmodules.DefaultValidatorFactoryEx}
 * を使用すると、本クラスが使用される。</p>
 * 
 * @see jp.terasoluna.fw.validation.FieldChecks
 * @see jp.terasoluna.fw.validation.springmodules.DefaultValidatorFactoryEx
 */
public class SpringValidationErrors implements ValidationErrors {
    
    /**
     * 本クラスで利用するログ。
     */
    private static Log log = 
        LogFactory.getLog(SpringValidationErrors.class);
    
    /**
     * ラップするSpringフレームワークのエラーオブジェクト。
     */
    private Errors errors = null;
    
    /**
     * Springフレームワークのエラーオブジェクトを設定する。
     * @param errors 設定する errors。
     */
    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    /**
     * Springフレームワークのエラーオブジェクトを取得する。
     * @return errors を戻します。
     */
    public Errors getErrors() {
        return errors;
    }
    
    /**
     * エラーを追加する。
     * ラップしたSpringフレームワークのエラーオブジェクトに
     * エラーを追加する。
     * 
     * @param bean 検証中のオブジェクト。
     * @param field commons-validatorのFieldオブジェクト。
     * @param va  commons-validatorのValidatorActionオブジェクト。
     */
    public void addError(Object bean, Field field, ValidatorAction va) {
        // エラー情報の取り出し
        String fieldCode = field.getKey();
        String errorCode = MessageUtils.getMessageKey(va, field);
        Object[] args = MessageUtils.getArgs(va, field);

        if (log.isDebugEnabled()) {
            log.debug("Rejecting value [field='" + fieldCode + "', errorCode='"
                    + errorCode + "']");
        }

        // エラー追加
        errors.rejectValue(fieldCode, errorCode, args, errorCode);
    }
}
