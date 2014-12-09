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

import java.util.Locale;

import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorResources;
import org.springframework.validation.Errors;
import org.springmodules.validation.commons.ValidatorFactory;

/**
 * ValidatoFactoryのスタブクラス。Commons-Validatorを設定できる。
 *
 */
public class BaseMultiFieldValidator_ValidatorFactoryStub01 implements
        ValidatorFactory {
    private Validator validator = null;

    /**
     * @param validator 設定する validator。
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public Validator getValidator(String beanName, Object bean, Errors errors) {
        return this.validator;
    }

    public boolean hasRulesForBean(String beanName, Locale locale) {
        // TODO 自動生成されたメソッド・スタブ
        return false;
    }

    public ValidatorResources getValidatorResources() {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}
