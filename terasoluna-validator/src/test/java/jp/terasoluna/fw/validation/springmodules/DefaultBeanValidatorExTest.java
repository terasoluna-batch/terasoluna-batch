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

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResources;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.validation.springmodules.DefaultBeanValidatorEx} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> Spring-ModulesのDefaultBeanValidator抽象クラス。
 * <p>
 * @see jp.terasoluna.fw.validation.springmodules.DefaultBeanValidatorEx
 */
public class DefaultBeanValidatorExTest {

    /**
     * testCleanupValidator01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A,E <br>
     * <br>
     * 入力値：(引数) validator:CommonsValidatorExインスタンス<br>
     * (前提条件) validator.getValidatorException():ValidatorExceptionインスタンス<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException（terasoluna-spring-validator)<br>
     * ・原因例外‐ValidatorException(commons)<br>
     * <br>
     * 引数validatorにValidatorExceptionが設定されている場合、ランタイム例外にラップしてスローすることのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCleanupValidator01() throws Exception {
        // 前処理
        ValidatorResources resources = new ValidatorResources();
        CommonsValidatorEx commonsValidatorEx = new CommonsValidatorEx(resources, null);
        ValidatorException validatorException = new ValidatorException();
        ReflectionTestUtils.setField(commonsValidatorEx, "validatorException",
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
     * (正常系) <br>
     * 観点：A,E <br>
     * <br>
     * 入力値：(引数) validator:CommonsValidatorExインスタンス<br>
     * (前提条件) validator.getValidatorException():null<br>
     * <br>
     * 期待値： <br>
     * 引数validatorにValidatorExceptionが設定されていない場合、例外をスローせずに処理を終了することのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCleanupValidator02() throws Exception {
        // 前処理
        ValidatorResources resources = new ValidatorResources();
        CommonsValidatorEx commonsValidatorEx = new CommonsValidatorEx(resources, null);
        ReflectionTestUtils.setField(commonsValidatorEx, "validatorException",
                null);

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
     * (正常系) <br>
     * 観点：A,E <br>
     * <br>
     * 入力値：(引数) validator:CommonsValidatorEx以外のインスタンス<br>
     * <br>
     * 期待値： <br>
     * 引数validatorがvalidatorCommonsValidatorExインスタンスでない場合、外をスローせずに処理を終了することのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
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
