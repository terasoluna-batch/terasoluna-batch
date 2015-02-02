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

import junit.framework.TestCase;

import org.springframework.validation.Errors;

/**
 * {@link jp.terasoluna.fw.validation.springmodules.
 * BaseMultiFieldValidator} クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * 相関チェックを行なう抽象クラス。<br>
 * 前提条件：検査対象のオブジェクト、エラーオブジェクトはNull値にならない。
 * <p>
 * 
 * @see jp.terasoluna.fw.validation.springmodules.
 * BaseMultiFieldValidator
 */
public class BaseMultiFieldValidatorTest extends TestCase {

    /**
     * このテストケースを実行する為の
     * GUI アプリケーションを起動する。
     * 
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(BaseMultiFieldValidatorTest.class);
    }

    /**
     * 初期化処理を行う。
     * 
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 終了処理を行う。
     * 
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * コンストラクタ。
     * 
     * @param name このテストケースの名前。
     */
    public BaseMultiFieldValidatorTest(String name) {
        super(name);
    }

    /**
     * testValidate01()
     * <br><br>
     * 
     * (正常系) 
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) obj:not null<br>
     *         (引数) errorｓ:not null<br>
     *         (状態) super.validate（）:エラーが発生しないパターン<br>
     *                （引数のerrorsのerrors.hasErrors()メソッドが
     *                FALSEになるようにする）<br>
     *         
     * <br>
     * 期待値：(状態変化) super.validate（）:
     *         メソッドが呼び出されることを確認する。<br>
     *         (状態変化) validateMultiField():
     *         メソッドが呼び出されることを確認する。
     *         引数を受け取ったことを確認する。<br>
     *         
     * <br>
     * 単項目チェックでエラーが発生せず、
     * 相関チェックメソッドを実行するパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidate01() throws Exception {
        // 前処理 --------------------------------------------------------------
        BaseMultiFieldValidatorImpl01 validator = 
            new BaseMultiFieldValidatorImpl01();
        
        // ValidatorFactoryの作成
        BaseMultiFieldValidator_ValidatorFactoryStub01 factory =
            new BaseMultiFieldValidator_ValidatorFactoryStub01();
        BaseMutiFieldValidator_ValidatorStub01 commonsValidator =
            new BaseMutiFieldValidator_ValidatorStub01();
        factory.setValidator(commonsValidator);
        
        // 属性を設定
        validator.setValidatorFactory(factory);
        
        // メソッド引数
        Object obj = new Object();
        Errors errors = 
            new BaseMultiFieldValidator_BindExceptionStub01(obj, "");

        // テスト実施 ----------------------------------------------------------
        validator.validate(obj, errors);

        // 判定
        assertTrue(commonsValidator.isValidate);
        assertSame(obj, validator.obj);
        assertSame(errors, validator.errors);
    }

    /**
     * testValidate02()
     * <br><br>
     * 
     * (正常系) 
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) obj:not null<br>
     *         (引数) errorｓ:not null<br>
     *         (状態) super.validate（）:エラーが発生するパターン<br>
     *                （引数のerrorsのerrors.hasErrors()メソッドが
     *                TRUEになるようにする）<br>
     *         
     * <br>
     * 期待値：(状態変化) super.validate（）:
     *          メソッドが呼び出されることを確認する。
     *          引数を受け取ったことを確認する。<br>
     *         (状態変化) validateMultiField():
     *         メソッドが呼び出されないことを確認する。<br>
     *         
     * <br>
     * 単項目チェックでエラーが発生し、
     * 相関チェックメソッドが実行されないパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidate02() throws Exception {
        // 前処理 --------------------------------------------------------------
        BaseMultiFieldValidatorImpl01 validator = 
            new BaseMultiFieldValidatorImpl01();
        
        // ValidatorFactoryの作成
        BaseMultiFieldValidator_ValidatorFactoryStub01 factory =
            new BaseMultiFieldValidator_ValidatorFactoryStub01();
        BaseMutiFieldValidator_ValidatorStub01 commonsValidator =
            new BaseMutiFieldValidator_ValidatorStub01();
        factory.setValidator(commonsValidator);
        
        // 属性を設定
        validator.setValidatorFactory(factory);
        
        // メソッド引数
        Object obj = new Object();
        Errors errors = 
            new BaseMultiFieldValidator_BindExceptionStub02(obj, "");

        // テスト実施 ----------------------------------------------------------
        validator.validate(obj, errors);

        // 判定
        assertTrue(commonsValidator.isValidate);
        assertSame(null, validator.obj);
        assertSame(null, validator.errors);
    }

}
