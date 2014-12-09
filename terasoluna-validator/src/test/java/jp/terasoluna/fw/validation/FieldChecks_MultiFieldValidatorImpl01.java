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


/**
 * {@link FieldChecksExtend}をテストするための{@link MultiFieldValidator}実装クラス。
 * 
 */
public class FieldChecks_MultiFieldValidatorImpl01 implements
        MultiFieldValidator {

    /**
     * <code>validate</code>メソッドの結果とする値。
     */
    protected static boolean result = false;

    /**
     * <code>validate</code>メソッドがコールされたカウント。
     */
    protected static int validateCalledCount = 0;
    
    /**
     * <code>validate</code>メソッドの第一引数の値。
     */
    protected static Object value = null;

    /**
     * <code>validate</code>メソッドの第二引数の値。
     */
    protected static Object[] fields = null;
    
    /**
     * 複数フィールドの相関入力チェックを実行する。
     * <br>
     * 検証対象の値は第一引数で渡される。検証に必要な他のフィールドの
     * 値は第二引数に配列として渡される。検証エラーの場合は <code>false</code>
     * を返却すること。
     *
     * @param value 検証対象の値
     * @param fields 検証に必要な他のフィールドの値配列
     * @return エラーがなければ <code>true</code>
     */
    @SuppressWarnings("hiding")
    public boolean validate(Object value, Object[] fields) {
        FieldChecks_MultiFieldValidatorImpl01.validateCalledCount++;
        FieldChecks_MultiFieldValidatorImpl01.value = value;
        FieldChecks_MultiFieldValidatorImpl01.fields = fields;
        return FieldChecks_MultiFieldValidatorImpl01.result;
    }
}
