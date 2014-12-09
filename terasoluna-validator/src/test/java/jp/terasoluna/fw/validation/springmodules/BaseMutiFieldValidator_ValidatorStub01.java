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
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResults;

/**
 * Commons-Validatorのスタブクラス。メソッド呼び出し確認用。
 *
 */
public class BaseMutiFieldValidator_ValidatorStub01 extends Validator {

    /**
     * シリアルバージョンID。
     */
    private static final long serialVersionUID = 8741542862050179269L;
    protected boolean isValidate = false;
    
    
    /**
     * コンストラクタ
     */
    public BaseMutiFieldValidator_ValidatorStub01() {
        super(new ValidatorResources());
    }
    
    @Override
    public ValidatorResults validate() throws ValidatorException {
        isValidate = true;
        return null;
    }
}
