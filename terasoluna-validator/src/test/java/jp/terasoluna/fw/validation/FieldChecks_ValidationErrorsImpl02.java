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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;

/**
 * FieldChecksTestで使用するValidationErrorsのスタブクラス。
 */
public class FieldChecks_ValidationErrorsImpl02 implements ValidationErrors {

    /**
     * addErrorがコールされるとカウントアップする。
     */
    public int addErrorCount = 0;

    /**
     * 第一引数の値リスト。
     */
    public List beanList = new ArrayList();

    /**
     * 第二引数の値リスト。
     */
    public List fieldList = new ArrayList();

    /**
     * 第三引数の値リスト。
     */
    public List vaList = new ArrayList();

    /**
     * スタブメソッド。呼び出し確認のため、呼ばれた回数と引数をキャッシュする。
     * @param bean 検査対象のJavaBeanインスタンス
     * @param field フィールドインスタンス
     * @param va Validatorにより用意されたValidatorAction
     */
    @SuppressWarnings("unchecked")
    public void addError(Object bean, Field field, ValidatorAction va) {
        throw new RuntimeException();
    }

}
