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

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;
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
public class FieldChecksTest02 {

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
     * testValidateRequired01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
=======
     * testValidateRequired01()
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
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * 引数のbeanがnullの場合、エラーを追加し、falseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateRequired01() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateRequired(null, va, field,
                errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertNull(errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));

    }

    /**
     * testValidateRequired02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
     * <br>
     * 入力値：(引数) bean:""<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * 引数のbeanが空文字の場合、エラーを追加し、 falseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateRequired02() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateRequired("", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));

    }

    /**
     * testValidateRequired03() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
     * <br>
     * 入力値：(引数) bean:" "<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * 引数のbeanが半角のスペースの場合、エラーを追加し、 falseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateRequired03() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateRequired(" ", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals(" ", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));

    }

    /**
     * testValidateRequired04() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"test"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * 引数のbeanがnull、空文字でない場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateRequired04() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateRequired("test", va, field,
                errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateMask01() <br>
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
    public void testValidateMask01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateMask(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateMask02() <br>
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
    public void testValidateMask02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateMask("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateMask03() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:"ABC"<br>
     * (引数) va:not null<br>
     * (引数) field:var:mask=null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ："var[mask] must be specified."<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ："var[mask] must be specified."<br>
     * <br>
     * fieldのvarからmaskが取得できない場合、 ValidatorExceptionがスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateMask03() throws Exception {
        // テスト実施
        // 判定
        try {
            new FieldChecks().validateMask("ABC", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            assertEquals("var[mask] must be specified.", e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "var[mask] must be specified."))));
        }
    }

    /**
     * testValidateMask04() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:"ABC"<br>
     * (引数) va:not null<br>
     * (引数) field:var:mask=""(空文字)<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ："var[mask] must be specified."<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ："var[mask] must be specified."<br>
     * <br>
     * fieldのvarからmaskが取得できない場合、 ValidatorExceptionがスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateMask04() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("mask");
        var.setValue("");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateMask("ABC", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            assertEquals("var[mask] must be specified.", e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "var[mask] must be specified."))));
        }
    }

    /**
     * testValidateMask05() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"ABC"<br>
     * (引数) va:not null<br>
     * (引数) field:var:mask=""^([0-9]|[a-z]|[A-Z])*$""<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * 引数のbeanの値が、指定された正規表現と一致する場合、 trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateMask05() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("mask");
        var.setValue("^([0-9]|[a-z]|[A-Z])*$");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateMask("ABC", va, field, errors));
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateMask06() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"あいう"<br>
     * (引数) va:not null<br>
     * (引数) field:var:mask=""^([0-9]|[a-z]|[A-Z])*$""<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * 引数のbeanの値が、指定された正規表現と一致しない場合、エラーを追加して、 falseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateMask06() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("mask");
        var.setValue("^([0-9]|[a-z]|[A-Z])*$");
        field.addVar(var);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateMask("あいう", va, field, errors));
        assertEquals(1, errors.addErrorCount);
        assertEquals("あいう", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateByte01() <br>
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
    public void testValidateByte01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateByte(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateByte02() <br>
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
    public void testValidateByte02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateByte("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateByte03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"0"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * 引数のbeanの値が、byteに変換できる場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateByte03() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateByte("0", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateByte04() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"あ"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * 引数のbeanの値が、byteに変換できない場合、エラーを追加して、falseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateByte04() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateByte("あ", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("あ", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateShort01() <br>
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
    public void testValidateShort01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateShort(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateShort02() <br>
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
    public void testValidateShort02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateShort("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateShort03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"0"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * 引数のbeanの値が、shortに変換できる場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateShort03() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateShort("0", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateShort04() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"あ"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * 引数のbeanの値が、shortに変換できない場合、エラーを追加して、falseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateShort04() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateShort("あ", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("あ", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateInteger01() <br>
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
    public void testValidateInteger01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateInteger(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateInteger02() <br>
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
    public void testValidateInteger02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateInteger("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateInteger03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"-2147483648"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * 引数のbeanの値が、intに変換できる場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateInteger03() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateInteger("-2147483648", va, field,
                errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateInteger04() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"あ"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * 引数のbeanの値が、intに変換できない場合、エラーを追加して、 falseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateInteger04() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateInteger("あ", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("あ", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

}
