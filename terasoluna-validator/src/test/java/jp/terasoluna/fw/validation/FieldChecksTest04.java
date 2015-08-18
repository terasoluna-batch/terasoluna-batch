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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.Var;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * {@link jp.terasoluna.fw.validation.FieldChecks} クラスのブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4> TERASOLUNAの入力チェック機能で共通に使用される検証ルールクラス。
 * <p>
 * @see jp.terasoluna.fw.validation.FieldChecks
 */
public class FieldChecksTest04 {

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            FieldChecks.class);

    /**
     * テスト用インスタンス。
     */
    private ValidatorAction va = null;

    /**
     * テスト用インスタンス。
     */
    private Field field = null;

    /**
     * テスト用インスタンス。
     */
    private FieldChecks_ValidationErrorsImpl01 errors = null;

    /**
     * 初期化処理を行う。
     */
    @Before
    public void setUp() {
        va = new ValidatorAction();
        field = new Field();
        errors = new FieldChecks_ValidationErrorsImpl01();
    }

    /**
<<<<<<< a57dbbe4830efc04ea52f49fcc2ac2e19bf2f42c
     * 終了処理を行う。
     */
    @After
    public void tearDown() {
        logger.clear();
    }

    /**
     * testValidateIntRange01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
=======
     * testValidateIntRange01()
     * <br><br>
     *
     * (正常系)
>>>>>>> Change JUnit3 to JUnit4. #257
     * <br>
     * 入力値：(引数) bean:null<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * 引数のbeanがnullの場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateIntRange(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateIntRange02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
     * <br>
     * 入力値：(引数) bean:""<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * 引数のbeanが空文字の場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateIntRange("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateIntRange03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"1.5"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * 引数のbeanがint型に変換できない場合、 エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange03() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateIntRange("1.5", va, field,
                errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("1.5", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateIntRange04() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"-2147483648"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMin=null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * varのintRangeMinがnullの場合、 範囲の最小値がInteger.MIN_VALUEであることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange04() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateIntRange("-2147483648", va, field,
                errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateIntRange05() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"-2147483649"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMin=null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * varのintRangeMinがnullの場合、 範囲の最小値がInteger.MIN_VALUEであることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange05() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateIntRange("-2147483649", va, field,
                errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("-2147483649", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateIntRange06() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"-2147483648"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMin=""<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * varのintRangeMinが空文字の場合、 範囲の最小値がInteger.MIN_VALUEであることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange06() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("intRangeMin");
        var.setValue("");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateIntRange("-2147483648", va, field,
                errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateIntRange07() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"-2147483649"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMin=""<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * varのintRangeMinが空文字の場合、 範囲の最小値がInteger.MIN_VALUEであることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange07() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("intRangeMin");
        var.setValue("");
        field.addVar(var);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateIntRange("-2147483649", va, field,
                errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("-2147483649", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateIntRange08() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"2147483647"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMax=null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * varのintRangeMaxがnullの場合、 範囲の最大値がInteger.MAX_VALUEであることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange08() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateIntRange("2147483647", va, field,
                errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateIntRange09() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"2147483648"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMax=null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * varのintRangeMaxがnullの場合、 範囲の最大値がInteger.MAX_VALUEであることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange09() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateIntRange("2147483648", va, field,
                errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("2147483648", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateIntRange10() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"2147483647"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMax=""<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * varのintRangeMaxが空文字の場合、 範囲の最大値がInteger.MAX_VALUEであることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange10() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("intRangeMin");
        var.setValue("");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateIntRange("2147483647", va, field,
                errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateIntRange11() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"2147483648"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMax=""<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * varのintRangeMaxが空文字の場合、 範囲の最大値がInteger.MAX_VALUEであることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange11() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("intRangeMin");
        var.setValue("");
        field.addVar(var);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateIntRange("2147483648", va, field,
                errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("2147483648", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateIntRange12() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:"5"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMin="1.5"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ："Mistake on validation definition file. - intRangeMin is not number. You'll have to check it over. "<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ："Mistake on validation definition file. - intRangeMin is not number. You'll have to check it over. ", new
     * NumberFormatException()<br>
     * <br>
     * varのintRangeMinの値がint型に変換できない場合、 ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange12() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("intRangeMin");
        var.setValue("1.5");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateIntRange("5", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. "
                    + "- intRangeMin is not number. You'll have to check "
                    + "it over. ";
            assertEquals(message, e.getMessage());
            assertThat(logger.getLoggingEvents().get(0).getMessage(), is(
                    equalTo(message)));
            assertThat(logger.getLoggingEvents().get(0).getThrowable().get(),
                    instanceOf(NumberFormatException.class));
        }
    }

    /**
     * testValidateIntRange13() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:"5"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMax="5.5"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ："Mistake on validation definition file. - intRangeMax is not number. You'll have to check it over. "<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ："Mistake on validation definition file. - intRangeMax is not number. You'll have to check it over. ", new
     * NumberFormatException()<br>
     * <br>
     * varのintRangeMaxの値がint型に変換できない場合、 ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange13() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("intRangeMax");
        var.setValue("5.5");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateIntRange("5", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. "
                    + "- intRangeMax is not number. You'll have to check "
                    + "it over. ";
            assertEquals(message, e.getMessage());
            assertThat(logger.getLoggingEvents().get(0).getMessage(), is(
                    equalTo(message)));
            assertThat(logger.getLoggingEvents().get(0).getThrowable().get(),
                    instanceOf(NumberFormatException.class));
        }
    }

    /**
     * testValidateIntRange14() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"5"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMin="1"<br>
     * intRangeMax="10"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * beanの値が、intRangeMinとintRangeMaxの範囲内の値の場合、 trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange14() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("intRangeMin");
        var1.setValue("1");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("intRangeMax");
        var2.setValue("10");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateIntRange("5", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);

    }

    /**
     * testValidateIntRange15() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"5"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMin="6"<br>
     * intRangeMax="10"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * beanの値が、intRangeMinの値より小さい場合、 エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange15() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("intRangeMin");
        var1.setValue("6");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("intRangeMax");
        var2.setValue("10");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateIntRange("5", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("5", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));

    }

    /**
     * testValidateIntRange16() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"5"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMin="1"<br>
     * intRangeMax="4"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * beanの値が、intRangeMaxの値より大きい場合、 エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange16() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("intRangeMin");
        var1.setValue("1");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("intRangeMax");
        var2.setValue("4");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateIntRange("5", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("5", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));

    }

    /**
     * testValidateIntRange17() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"5"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * intRangeMin="5"<br>
     * intRangeMax="5"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * beanの値と、intRangeMax,intRangeMinの値と等しい場合、 trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateIntRange17() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("intRangeMin");
        var1.setValue("5");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("intRangeMax");
        var2.setValue("5");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateIntRange("5", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);

    }

    /**
     * testValidateDoubleRange01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
     * <br>
     * 入力値：(引数) bean:null<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * 引数のbeanがnullの場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDoubleRange01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDoubleRange(null, va, field,
                errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDoubleRange02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
     * <br>
     * 入力値：(引数) bean:""<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * 引数のbeanが空文字の場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDoubleRange02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDoubleRange("", va, field,
                errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDoubleRange03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"test"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * 引数のbeanがdouble型に変換できない場合、 エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDoubleRange03() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateDoubleRange("test", va, field,
                errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("test", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateDoubleRange04() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"4.9E-324"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * doubleRangeMin=null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * varのdoubleRangeMinがnullの場合、 範囲の最小値がDouble.MIN_VALUEであることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDoubleRange04() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDoubleRange("4.9E-324", va, field,
                errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDoubleRange05() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"4.9E-324"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * doubleRangeMin=""<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * varのdoubleRangeMinが空文字の場合、 範囲の最小値がDouble.MIN_VALUEであることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDoubleRange05() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("doubleRangeMin");
        var.setValue("");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDoubleRange("4.9E-324", va, field,
                errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDoubleRange06() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"1.7976931348623157E308"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * doubleRangeMax=null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * varのdoubleRangeMaxがnullの場合、範囲の最大値がDouble.MAX_VALUEであることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDoubleRange06() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDoubleRange(
                "1.7976931348623157E308", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDoubleRange07() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"1.7976931348623157E308"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * doubleRangeMax=""<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * varのdoubleRangeMaxが空文字の場合、範囲の最大値がDouble.MAX_VALUEであることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDoubleRange07() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("doubleRangeMax");
        var.setValue("");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDoubleRange(
                "1.7976931348623157E308", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDoubleRange08() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"0.5E1"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * doubleRangeMin="test"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ："Mistake on validation definition file. - doubleRangeMin is not number. You'll have to check it over. "<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ："Mistake on validation definition file. - doubleRangeMin is not number. You'll have to check it over. ", new
     * NumberFormatException()<br>
     * <br>
     * varのdoubleRangeMinの値がdouble型に変換できない場合、 ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDoubleRange08() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("doubleRangeMin");
        var.setValue("test");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateDoubleRange("0.5E1", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. "
                    + "- doubleRangeMin is not number. You'll have to check "
                    + "it over. ";
            assertEquals(message, e.getMessage());
            assertThat(logger.getLoggingEvents().get(0).getMessage(), is(
                    equalTo(message)));
            assertThat(logger.getLoggingEvents().get(0).getThrowable().get(),
                    instanceOf(NumberFormatException.class));
        }
    }

    /**
     * testValidateDoubleRange09() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"0.5E1"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * doubleRangeMax="test"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ："Mistake on validation definition file. - doubleRangeMax is not number. You'll have to check it over. "<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ："Mistake on validation definition file. - doubleRangeMax is not number. You'll have to check it over. ", new
     * NumberFormatException()<br>
     * <br>
     * varのdoubleRangeMaxの値がdouble型に変換できない場合、 ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDoubleRange09() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("doubleRangeMax");
        var.setValue("test");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateDoubleRange("0.5E1", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. "
                    + "- doubleRangeMax is not number. You'll have to check "
                    + "it over. ";
            assertEquals(message, e.getMessage());
            assertThat(logger.getLoggingEvents().get(0).getMessage(), is(
                    equalTo(message)));
            assertThat(logger.getLoggingEvents().get(0).getThrowable().get(),
                    instanceOf(NumberFormatException.class));
        }
    }

    /**
     * testValidateDoubleRange10() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"0.5E1"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * doubleRangeMin="0.1E1"<br>
     * doubleRangeMax="0.1E2"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * beanの値が、doubleRangeMinとdoubleRangeMaxの範囲内の値の場合、 trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDoubleRange10() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("doubleRangeMin");
        var1.setValue("0.1E1");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("doubleRangeMax");
        var2.setValue("0.1E2");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDoubleRange("0.5E1", va, field,
                errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);

    }

    /**
     * testValidateDoubleRange11() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"0.5E1"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * doubleRangeMin="0.1E2"<br>
     * doubleRangeMax="0.1E3"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * beanの値が、doubleRangeMinの値より小さい場合、エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDoubleRange11() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("doubleRangeMin");
        var1.setValue("0.1E2");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("doubleRangeMax");
        var2.setValue("0.1E3");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateDoubleRange("0.5E1", va, field,
                errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("0.5E1", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateDoubleRange12() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"0.5E1"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * doubleRangeMin="0.1E1"<br>
     * doubleRangeMax="0.4E1"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * beanの値が、doubleRangeMaxの値より大きい場合、エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDoubleRange12() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("doubleRangeMin");
        var1.setValue("0.1E1");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("doubleRangeMax");
        var2.setValue("0.4E1");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateDoubleRange("0.5E1", va, field,
                errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("0.5E1", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateDoubleRange13() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"0.5E1"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * doubleRangeMin="0.5E1"<br>
     * doubleRangeMax="0.5E1"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * beanの値と、doubleRangeMax,doubleRangeMinの値と等しい場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDoubleRange13() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("doubleRangeMin");
        var1.setValue("0.5E1");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("doubleRangeMax");
        var2.setValue("0.5E1");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDoubleRange("0.5E1", va, field,
                errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

}
