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
 * Jakarta CommonsのValidator継承クラス
 * 
 * <p>
 * validate()メソッドをオーバーライドしている。
 * 親クラスのvalidate()メソッドを呼び出した際に
 * validation.xmlなどの記述ミスにより、バリデート例外が発生した場合、
 * その例外インスタンスを属性に保持する。
 * </p>
 * 
 * <p>
 * 使用前にclear()メソッドで初期化した場合はスレッドセーフとして使用可能。
 * </p>
 * 
 * <p>
 * 本クラスは、CommonsValidatorExによって生成される。
 * また、属性に保持した例外インスタンスは、
 * DefaultValidatorFactoryExによって利用される。
 * </p>
 * 
 * <p>
 * 本クラスを利用する場合に必要なBean定義ファイルの設定については、
 * DefaultValidatorFactoryExのJavaDocの記述を参照のこと。
 * </p>
 * 
 * 
 */
public class CommonsValidatorEx extends Validator {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = -7315991856716383283L;
    
    /**
     * XMLデータの検証時に発生した例外
     */
    private ValidatorException validatorException = null;

    /**
     * コンストラクタ
     * @param resources 検証リソース
     * @param formName フォーム名
     */
    public CommonsValidatorEx(ValidatorResources resources, String formName) {
        super(resources, formName);
    }

    /**
     * XMLデータの検証時に発生した例外を取得する
     * @return XMLデータの検証時に発生した例外
     */
    public ValidatorException getValidatorException() {
        return validatorException;
    }

    /**
     * 検証メソッド
     * Validatorのvalidate()メソッドを呼び出し、 
     * エラーがあったらクラスの属性に設定してスローする。 
     * @return 検証結果
     * @throws XMLデータの検証時に発生した例外
     */
    @Override
    public ValidatorResults validate() throws ValidatorException {
        try {
            return super.validate();
        } catch (ValidatorException e) {
            // XMLデータの検証時に例外が発生した場合は、発生した
            // 例外をvalidatorException属性に保持し、スローする
            validatorException = e;
            throw e;
        }
    }
    
    /**
     * クリアメソッド
     * Validatorのclear()メソッドを呼び出し、 
     * クラスのvalidatorException属性をnullに設定します。 
     */
    @Override
    public void clear() {
        super.clear();
        this.validatorException = null;
    }
}
