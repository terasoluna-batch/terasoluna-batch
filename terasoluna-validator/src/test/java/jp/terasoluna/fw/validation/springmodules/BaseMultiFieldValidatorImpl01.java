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

/**
 * BaseMultiFieldValidatorクラスの実装クラス。
 *
 */
public class BaseMultiFieldValidatorImpl01 extends BaseMultiFieldValidator {
    
    protected Object obj = null;
    protected Errors errors = null;

    @Override
    protected void validateMultiField(@SuppressWarnings("hiding") Object obj, 
            @SuppressWarnings("hiding") Errors errors) {
        this.obj = obj;
        this.errors = errors;
    }

    

}
