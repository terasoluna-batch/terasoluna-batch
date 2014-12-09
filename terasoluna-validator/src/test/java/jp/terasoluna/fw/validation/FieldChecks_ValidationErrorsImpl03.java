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

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;


/**
 * {@link FieldChecksExtend}をテストするための{@link ValidationErrors}実装クラス。
 * 
 */
public class FieldChecks_ValidationErrorsImpl03 implements ValidationErrors {
    
    /**
     * 設定されるエラーメッセージ。
     */
    protected String errorMessage = null;
    
    /**
     * エラー情報を追加する。
     * 
     * @param bean 検査対象のJavaBeanインスタンス
     * @param field フィールドインスタンス
     * @param va Validatorにより用意されたValidatorAction
     */
    public void addError(Object bean, Field field, ValidatorAction va) {
        this.errorMessage = "errorMessage";
    }
}
