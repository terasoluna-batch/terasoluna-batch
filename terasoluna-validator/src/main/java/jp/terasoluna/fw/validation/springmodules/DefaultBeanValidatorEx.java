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
import org.springmodules.validation.commons.DefaultBeanValidator;

/**
 * Spring ModulesのDefaultBeanValidator継承クラス。
 * 
 * <p>
 * DefaultBeanValidatorは、Commons Validatorのvalidator()メソッドを呼び出した後、
 * finally句でcleanupValidator()メソッドを呼び出す。
 * </p>
 * 
 * <p>
 * 本クラスでは、cleanupValidator()メソッドをオーバーライドしている。
 * 引き数のバリデータにバリデート例外が格納されていた場合、
 * その例外をランタイムのバリデート例外にラップしてスローする。
 * </p>
 * 
 * <p>
 * 本クラスは、
 * cleanupValidatorの引き数としてCommonsValidatorExインスタンスが
 * 渡されることを前提としている。
 * CommonsValidatorExクラスは、
 * DefaultValidatorFactoryExクラスによって生成される。
 * よって、本クラスを利用する場合は、
 * DefaultValidatorFactoryExクラスも同時に利用しなければならない。
 * </p>
 * 
 * <p>
 * 本クラスを利用する場合に必要なBean定義ファイルの設定については、
 * DefaultValidatorFactoryExのJavaDocの記述を参照のこと。
 * </p>
 * 
 *
 */
public class DefaultBeanValidatorEx extends DefaultBeanValidator {

    /**
     * Validatorのクリーンアップメソッド。
     * validatorの属性に発生が存在した場合はValidatorExceptionをスローする。
     * 
     * @param validator Commons Validator
     * @throws jp.terasoluna.fw.validation.springmodules.ValidatorException 
     *          バリデート例外
     */
    @Override
    public void cleanupValidator(Validator validator) {
        
        // validatorはCommonsValidatorExの場合
        if (validator instanceof CommonsValidatorEx) {
            // validatorのチェックとき発生した例外を取得する
            ValidatorException validatorException = 
                ((CommonsValidatorEx) validator).getValidatorException();
            // この例外があった場合
            if (validatorException != null) {
                throw new jp.terasoluna.fw.validation.springmodules.
                    ValidatorException(validatorException);
            }
        }
    }

}
