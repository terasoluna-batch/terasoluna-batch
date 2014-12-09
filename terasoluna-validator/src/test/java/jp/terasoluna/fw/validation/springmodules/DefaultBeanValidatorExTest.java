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

import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResources;

/**
 * {@link jp.terasoluna.fw.validation.springmodules.DefaultBeanValidatorEx}
 * クラスのテスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * Spring-ModulesのDefaultBeanValidator抽象クラス。
 * <p>
 * 
 * @see jp.terasoluna.fw.validation.springmodules.DefaultBeanValidatorEx
 */
public class DefaultBeanValidatorExTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * 
     * @param args
     *            java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(DefaultBeanValidatorExTest.class);
    }

    /**
     * 初期化処理を行う。
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 終了処理を行う。
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * コンストラクタ。
     * 
     * @param name
     *            このテストケースの名前。
     */
    public DefaultBeanValidatorExTest(String name) {
        super(name);
    }

    /**
     * testCleanupValidator01() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：A,E <br>
     * <br>
     * 入力値：(引数) validator:CommonsValidatorExインスタンス<br>
     * (前提条件) validator.getValidatorException():ValidatorExceptionインスタンス<br>
     * 
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException（terasoluna-spring-validator)<br>
     * ・原因例外‐ValidatorException(commons)<br>
     * 
     * <br>
     * 引数validatorにValidatorExceptionが設定されている場合、ランタイム例外にラップしてスローすることのテスト。 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testCleanupValidator01() throws Exception {
        // 前処理
        ValidatorResources resources = new ValidatorResources();
        CommonsValidatorEx commonsValidatorEx = new CommonsValidatorEx(
                resources, null);
        ValidatorException validatorException = new ValidatorException();
        UTUtil.setPrivateField(commonsValidatorEx, "validatorException",
                validatorException);

        DefaultBeanValidatorEx defaultBeanValidatorEx = new DefaultBeanValidatorEx();
        try {
            // テスト実施
            defaultBeanValidatorEx.cleanupValidator(commonsValidatorEx);
            fail();
        } catch (jp.terasoluna.fw.validation.springmodules.ValidatorException e) {
            // 判定
            assertSame(validatorException, e.getCause());
        }
    }

    /**
     * testCleanupValidator02() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：A,E <br>
     * <br>
     * 入力値：(引数) validator:CommonsValidatorExインスタンス<br>
     * (前提条件) validator.getValidatorException():null<br>
     * 
     * <br>
     * 期待値： <br>
     * 引数validatorにValidatorExceptionが設定されていない場合、例外をスローせずに処理を終了することのテスト。 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testCleanupValidator02() throws Exception {
        // 前処理
        ValidatorResources resources = new ValidatorResources();
        CommonsValidatorEx commonsValidatorEx = new CommonsValidatorEx(
                resources, null);
        UTUtil.setPrivateField(commonsValidatorEx, "validatorException", null);

        DefaultBeanValidatorEx defaultBeanValidatorEx = new DefaultBeanValidatorEx();
        try {
            // テスト実施
            defaultBeanValidatorEx.cleanupValidator(commonsValidatorEx);
        } catch (jp.terasoluna.fw.validation.springmodules.ValidatorException e) {
            // 判定
            fail();
        }
    }

    /**
     * testCleanupValidator03() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：A,E <br>
     * <br>
     * 入力値：(引数) validator:CommonsValidatorEx以外のインスタンス<br>
     * 
     * <br>
     * 期待値： <br>
     * 引数validatorがvalidatorCommonsValidatorExインスタンスでない場合、外をスローせずに処理を終了することのテスト。
     * <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testCleanupValidator03() throws Exception {
        // 前処理
        ValidatorResources resources = new ValidatorResources();
        Validator validator = new Validator(resources);

        DefaultBeanValidatorEx defaultBeanValidatorEx = new DefaultBeanValidatorEx();
        try {

            defaultBeanValidatorEx.cleanupValidator(validator);
        } catch (jp.terasoluna.fw.validation.springmodules.ValidatorException e) {
            // 判定
            fail();
        }
    }

}
