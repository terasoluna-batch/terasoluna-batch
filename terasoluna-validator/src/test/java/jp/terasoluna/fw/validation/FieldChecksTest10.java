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
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;

import java.util.ArrayList;
import java.util.List;

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
public class FieldChecksTest10 {

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
     * 終了処理を行う。
     */
    @After
    public void tearDown() {
        logger.clear();
    }

    /**
     * testValidateDateRange01() <br>
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
    public void testValidateDateRange01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDateRange(null, va, field,
                errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDateRange02() <br>
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
    public void testValidateDateRange02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDateRange("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDateRange03() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:"2005/11/22"<br>
     * (引数) va:not null<br>
     * (引数) field:var<br>
     * datePattern="abc"<br>
     * datePatternStrict="yyyy.MM.dd"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ：Illegal pattern character 'b'<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ：Illegal pattern character 'b'<br>
     * <br>
     * varのdatePatternに不正なパターン文字が含まれる場合、 ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDateRange03() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("datePattern");
        var1.setValue("abc");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("datePatternStrict");
        var2.setValue("yyyy.MM.dd");
        field.addVar(var2);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateDateRange("2005/11/22", va, field,
                    errors);
            fail();
        } catch (ValidatorException e) {
            assertEquals("Illegal pattern character 'b'", e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Illegal pattern character 'b'"))));
        }
    }

    /**
     * testValidateDateRange04() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:"2005/11/22"<br>
     * (引数) va:not null<br>
     * (引数) field:var<br>
     * datePatternStrict="abc"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ：Illegal pattern character 'b'<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ：Illegal pattern character 'b'<br>
     * <br>
     * varのdatePatternStrictに不正なパターン文字が含まれる場合、 ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDateRange04() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("datePatternStrict");
        var.setValue("abc");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateDateRange("2005/11/22", va, field,
                    errors);
            fail();
        } catch (ValidatorException e) {
            assertEquals("Illegal pattern character 'b'", e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Illegal pattern character 'b'"))));
        }
    }

    /**
     * testValidateDateRange05() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:"2005/11/22"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * datePattern=null<br>
     * datePatternStrict=null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ：datePattern or datePatternStrict must be specified.<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ：datePattern or datePatternStrict must be specified.<br>
     * <br>
     * varのdatePattern、datePatternStrictがnullの場合、 ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDateRange05() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("datePattern");
        var1.setValue(null);
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("datePatternStrict");
        var2.setValue(null);
        field.addVar(var2);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateDateRange("2005/11/22", va, field,
                    errors);
            fail();
        } catch (ValidatorException e) {
            assertEquals("datePattern or datePatternStrict must be specified.",
                    e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "datePattern or datePatternStrict must be specified."))));
        }
    }

    /**
     * testValidateDateRange06() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:"2005/11/22"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * datePattern=""<br>
     * datePatternStrict=""<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ：datePattern or datePatternStrict must be specified.<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ：datePattern or datePatternStrict must be specified.<br>
     * <br>
     * varのdatePattern、datePatternStrictが空文字の場合、ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDateRange06() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("datePattern");
        var1.setValue("");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("datePatternStrict");
        var2.setValue("");
        field.addVar(var2);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateDateRange("2005/11/22", va, field,
                    errors);
            fail();
        } catch (ValidatorException e) {
            assertEquals("datePattern or datePatternStrict must be specified.",
                    e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "datePattern or datePatternStrict must be specified."))));
        }
    }

    /**
     * testValidateDateRange07() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"test"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * datePattern="yyyy/MM/dd"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * beanが日付に変換できない場合、エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDateRange07() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("datePattern");
        var.setValue("yyyy/MM/dd");
        field.addVar(var);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateDateRange("test", va, field,
                errors));
        assertEquals(1, errors.addErrorCount);
        assertEquals("test", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateDateRange08() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:"2005/11/22"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * datePattern="yyyy/MM/dd"<br>
     * startDate="test"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ：startDate is unparseable[test]<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ：startDate is unparseable[test]<br>
     * <br>
     * startDateが日付に変換できない場合、ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDateRange08() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("datePattern");
        var1.setValue("yyyy/MM/dd");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("startDate");
        var2.setValue("test");
        field.addVar(var2);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateDateRange("2005/11/22", va, field,
                    errors);
            fail();
        } catch (ValidatorException e) {
            assertEquals("startDate is unparseable[test]", e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "startDate is unparseable[test]"))));
        }
    }

    /**
     * testValidateDateRange09() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:"2005/11/22"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * datePattern="yyyy/MM/dd"<br>
     * endDate="test"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ：endDate is unparseable[test]<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ：endDate is unparseable[test]<br>
     * <br>
     * endDateが日付に変換できない場合、ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDateRange09() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("datePattern");
        var1.setValue("yyyy/MM/dd");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("endDate");
        var2.setValue("test");
        field.addVar(var2);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateDateRange("2005/11/22", va, field,
                    errors);
            fail();
        } catch (ValidatorException e) {
            assertEquals("endDate is unparseable[test]", e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "endDate is unparseable[test]"))));
        }
    }

    /**
     * testValidateDateRange10() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"2005/11/22"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * datePattern="yyyy/MM/dd"<br>
     * startDate="2005/11/23"<br>
     * endDate="2005/12/31"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数として addErrorsが呼び出される。<br>
     * <br>
     * beanの日付がstartDateの日付以前の場合、 エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDateRange10() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("datePattern");
        var1.setValue("yyyy/MM/dd");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("startDate");
        var2.setValue("2005/11/23");
        field.addVar(var2);

        Var var3 = new Var();
        var3.setName("endDate");
        var3.setValue("2005/12/31");
        field.addVar(var3);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateDateRange("2005/11/22", va, field,
                errors));
        assertEquals(1, errors.addErrorCount);
        assertEquals("2005/11/22", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateDateRange11() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"2005/11/22"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * datePattern="yyyy/MM/dd"<br>
     * startDate="2005/1/1"<br>
     * endDate="2005/11/21"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * beanの日付がendDateの日付以降の場合、エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDateRange11() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("datePattern");
        var1.setValue("yyyy/MM/dd");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("startDate");
        var2.setValue("2005/1/1");
        field.addVar(var2);

        Var var3 = new Var();
        var3.setName("endDate");
        var3.setValue("2005/11/21");
        field.addVar(var3);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateDateRange("2005/11/22", va, field,
                errors));
        assertEquals(1, errors.addErrorCount);
        assertEquals("2005/11/22", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateDateRange12() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"2005/11/22"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * datePattern="yyyy/MM/dd"<br>
     * startDate="2005/11/22"<br>
     * endDate="2005/11/22"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * beanの日付とstartDate,endDateの日付が等しい場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDateRange12() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("datePattern");
        var1.setValue("yyyy/MM/dd");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("startDate");
        var2.setValue("2005/11/22");
        field.addVar(var2);

        Var var3 = new Var();
        var3.setName("endDate");
        var3.setValue("2005/11/22");
        field.addVar(var3);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDateRange("2005/11/22", va, field,
                errors));
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDateRange13() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:"2005/1/1"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * datePatternStrict="yyyy/MM/dd"<br>
     * startDate="2005/01/01"<br>
     * endDate="2005/01/01"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * beanの日付とdatePatternStrictのパターンの文字数が等しくない場合、 エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDateRange13() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("datePatternStrict");
        var1.setValue("yyyy/MM/dd");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("startDate");
        var2.setValue("2005/01/01");
        field.addVar(var2);

        Var var3 = new Var();
        var3.setName("endDate");
        var3.setValue("2005/01/01");
        field.addVar(var3);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateDateRange("2005/1/1", va, field,
                errors));
        assertEquals(1, errors.addErrorCount);
        assertEquals("2005/1/1", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateArraysIndex01() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:null<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ："validation target bean is null."<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ："validation target bean is null."<br>
     * <br>
     * 引数のbeanがnullの場合、ValidatorExceptionがスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex01() throws Exception {
        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArraysIndex(null, va, field, errors);
        } catch (ValidatorException e) {
            assertEquals("validation target bean is null.", e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "validation target bean is null."))));
        }
    }

    /**
     * testValidateArraysIndex02() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:not null<br>
     * (引数) va:mehodParams=""<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ："Mistake on validation rule file. - Can not get argument class. You'll have to check it over. "<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ："Mistake on validation rule file. - Can not get argument class. You'll have to check it over. "<br>
     * <br>
     * vaからmethodParamsが取得できない場合、 ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex02() throws Exception {
        // 前処理
        va.setMethodParams("");

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArraysIndex(new Object(), va, field,
                    errors);
        } catch (ValidatorException e) {
            String expect = "Mistake on validation rule file. "
                    + "- Can not get argument class. You'll have to check it over. ";
            assertEquals(expect, e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(expect))));
        }
    }

    /**
     * testValidateArraysIndex03() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:not null<br>
     * (引数) va:mehodParams="aaaaa"<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ："Mistake on validation rule file. - Can not get argument class. You'll have to check it over. "<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ："Mistake on validation rule file. - Can not get argument class. You'll have to check it over. "<br>
     * <br>
     * vaからmethodParamsが取得できない場合、ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex03() throws Exception {
        // 前処理
        va.setMethodParams("aaaaa");

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArraysIndex(new Object(), va, field,
                    errors);
        } catch (ValidatorException e) {
            String expect = "Mistake on validation rule file. "
                    + "- Can not get argument class. You'll have to check it over. ";
            assertEquals(expect, e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(expect))));
        }
    }

    /**
     * testValidateArraysIndex04() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(引数) bean:not null<br>
     * (引数) va:mehodParams="java.lang.String"<br>
     * name="hoge"<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ："Mistake on validation rule file. - Can not get validateMethod. You'll have to check it over. "<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ："Mistake on validation rule file. - Can not get validateMethod. You'll have to check it over. "<br>
     * <br>
     * vaに設定されたnameのメソッドが存在しない場合、ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex04() throws Exception {
        // 前処理
        va.setMethodParams("java.lang.String");
        va.setName("hoge");

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArraysIndex(new Object(), va, field,
                    errors);
        } catch (ValidatorException e) {
            String expect = "Mistake on validation rule file. "
                    + "- Can not get validateMethod. You'll have to check it over. ";
            assertEquals(expect, e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(expect))));
        }
    }

    /**
     * testValidateArraysIndex05() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:JavaBean<br>
     * String[] array = {<br>
     * "a", "b", "c"<br>
     * };<br>
     * (引数) va:methodParams="java.lang.Object, org.apache.commons.validator.ValidatorAction, org.apache.commons.validator.Field,
     * jp.terasoluna.fw.validation.ValidationErrors"<br>
     * name="requiredArray"<br>
     * (引数) field:property="array"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * varのプロパティで指定されたフィールドが配列型のフィールドで、 全てのフィールドについてvaのnameで指定されたチェック違反がない場合、 trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex05() throws Exception {
        // 前処理
        // JavaBean
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setArray(new String[] { "a", "b", "c" });

        // ValidatorAction
        va.setMethodParams("java.lang.Object,"
                + "org.apache.commons.validator.ValidatorAction,"
                + "org.apache.commons.validator.Field,"
                + "jp.terasoluna.fw.validation.ValidationErrors");
        va.setName("requiredArray");

        // Field
        field.setProperty("array");

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateArraysIndex(bean, va, field,
                errors));
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateArraysIndex06() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:JavaBean<br>
     * List list = {<br>
     * "a", null, null, "d"<br>
     * };<br>
     * (引数) va:methodParams="java.lang.Object, org.apache.commons.validator.ValidatorAction, org.apache.commons.validator.Field,
     * jp.terasoluna.fw.validation.ValidationErrors"<br>
     * name="requiredArray"<br>
     * (引数) field:property="list"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:2回呼び出される<br>
     * １：bean=引数のbeanと同一<br>
     * field:<br>
     * key="lsit[1]"<br>
     * property="list[1]"<br>
     * va=引数のvaと同一<br>
     * ２：bean=引数のbeanと同一l<br>
     * field:<br>
     * key="list[2]"<br>
     * property="list[2]"<br>
     * va=引数のvaと同一<br>
     * <br>
     * varのプロパティで指定されたフィールドがCollection型のフィールドで、 vaのnameで指定されたチェックの違反が複数存在する場合、 その回数分errorsのaddErrorsが呼び出されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex06() throws Exception {
        // 前処理
        // JavaBean
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add(null);
        list.add(null);
        list.add("d");
        bean.setList(list);

        // ValidatorAction
        va.setMethodParams("java.lang.Object,"
                + "org.apache.commons.validator.ValidatorAction,"
                + "org.apache.commons.validator.Field,"
                + "jp.terasoluna.fw.validation.ValidationErrors");
        va.setName("requiredArray");

        // Field
        field.setProperty("list");

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateArraysIndex(bean, va, field,
                errors));
        assertEquals(2, errors.addErrorCount);
        assertSame(bean, errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertEquals("list[1]", ((Field) errors.fieldList.get(0)).getKey());
        assertEquals("list[1]", ((Field) errors.fieldList.get(0))
                .getProperty());

        assertSame(bean, errors.beanList.get(1));
        assertSame(va, errors.vaList.get(1));
        assertEquals("list[2]", ((Field) errors.fieldList.get(1)).getKey());
        assertEquals("list[2]", ((Field) errors.fieldList.get(1))
                .getProperty());
    }

    /**
     * testValidateArraysIndex07() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:JavaBean<br>
     * int[] intArray = {<br>
     * 0,2,5<br>
     * };<br>
     * (引数) va:methodParams="java.lang.Object, org.apache.commons.validator.ValidatorAction, org.apache.commons.validator.Field,
     * jp.terasoluna.fw.validation.ValidationErrors"<br>
     * name="intRangeArray"<br>
     * (引数) field:property="intArray"<br>
     * var:<br>
     * intRangeMin=1<br>
     * intRangeMax=3<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:2回呼び出される<br>
     * １：bean=引数のbeanと同一<br>
     * field:<br>
     * key="intArray[0]"<br>
     * property="intArray[0]"<br>
     * var:<br>
     * intRangeMin="1"<br>
     * intRangeMax="3"<br>
     * va=引数のvaと同一<br>
     * ２：bean=引数のbeanと同一<br>
     * field:<br>
     * key="intArray[2]"<br>
     * property="intArray[2]"<br>
     * var:<br>
     * intRangeMin="1"<br>
     * intRangeMax="3"<br>
     * va=引数のvaと同一<br>
     * <br>
     * varのプロパティで指定されたフィールドがプリミティブ配列型のフィールドで、 vaのnameで指定されたチェックの違反が複数存在する場合、 その回数分errorsのaddErrorsが呼び出されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex07() throws Exception {
        // 前処理
        // JavaBean
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setIntArray(new int[] { 0, 2, 5 });

        // ValidatorAction
        va.setMethodParams("java.lang.Object,"
                + "org.apache.commons.validator.ValidatorAction,"
                + "org.apache.commons.validator.Field,"
                + "jp.terasoluna.fw.validation.ValidationErrors");
        va.setName("intRangeArray");

        // Field
        field.setProperty("intArray");
        Var var1 = new Var();
        var1.setName("intRangeMin");
        var1.setValue("1");
        field.addVar(var1);

        Var var2 = new Var();
        var2.setName("intRangeMax");
        var2.setValue("3");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateArraysIndex(bean, va, field,
                errors));
        assertEquals(2, errors.addErrorCount);
        assertSame(bean, errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertEquals("intArray[0]", ((Field) errors.fieldList.get(0)).getKey());
        assertEquals("intArray[0]", ((Field) errors.fieldList.get(0))
                .getProperty());

        assertSame(bean, errors.beanList.get(1));
        assertSame(va, errors.vaList.get(1));
        assertEquals("intArray[2]", ((Field) errors.fieldList.get(1)).getKey());
        assertEquals("intArray[2]", ((Field) errors.fieldList.get(1))
                .getProperty());
    }

    /**
     * testValidateArraysIndex08() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:JavaBean<br>
     * String field = null;<br>
     * (引数) va:methodParams="java.lang.Object, org.apache.commons.validator.ValidatorAction, org.apache.commons.validator.Field,
     * jp.terasoluna.fw.validation.ValidationErrors"<br>
     * name="requiredArray"<br>
     * (引数) field:property="field"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean=引数のbeanと同一<br>
     * field:<br>
     * key="field"<br>
     * property="field"<br>
     * va=引数のvaと同一<br>
     * <br>
     * varのプロパティで指定されたフィールドが配列、Collection型ではない場合、 そのフィールドに対してのチェックが行われることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex08() throws Exception {
        // 前処理
        // JavaBean
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setField(null);

        // ValidatorAction
        va.setMethodParams("java.lang.Object,"
                + "org.apache.commons.validator.ValidatorAction,"
                + "org.apache.commons.validator.Field,"
                + "jp.terasoluna.fw.validation.ValidationErrors");
        va.setName("requiredArray");

        // Field
        field.setProperty("field");

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateArraysIndex(bean, va, field,
                errors));
        assertEquals(1, errors.addErrorCount);
        assertSame(bean, errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertEquals("field", ((Field) errors.fieldList.get(0)).getKey());
        assertEquals("field", ((Field) errors.fieldList.get(0)).getProperty());
    }

    /**
     * testValidateArraysIndex09() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:JavaBean<br>
     * ∟JavaBean[] beanArray※要素数３<br>
     * ∟JavaBean[0]<br>
     * String[] array = {<br>
     * "a", null, "c"<br>
     * };<br>
     * ∟JavaBean[1]=null<br>
     * ∟JavaBean[2]<br>
     * String[] array = {<br>
     * "a", null, null<br>
     * };<br>
     * (引数) va:methodParams="java.lang.Object, org.apache.commons.validator.ValidatorAction, org.apache.commons.validator.Field,
     * jp.terasoluna.fw.validation.ValidationErrors"<br>
     * name="requiredArray"<br>
     * (引数) field:property="beanArray.array"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:3回呼び出される<br>
     * １：bean=引数のbeanと同一<br>
     * field:<br>
     * key="beanArray[0].array[1]"<br>
     * property="beanArray[0].array[1]"<br>
     * va=引数のvaと同一<br>
     * ２：bean=引数のbeanと同一<br>
     * field:<br>
     * key="beanArray[2].array[1]"<br>
     * property="beanArray[2].array[1]"<br>
     * va=引数のvaと同一<br>
     * ３：bean=引数のbeanと同一<br>
     * field:<br>
     * key="beanArray[2].array[2]"<br>
     * property="beanArray[2].array[2]"<br>
     * va=引数のvaと同一<br>
     * <br>
     * varのプロパティで指定されたフィールドが配列型のフィールドで、 全てのフィールドについてvaのnameで指定されたチェック違反がない場合、 trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex09() throws Exception {
        // 前処理
        // JavaBean-beanArray[0]
        FieldChecks_JavaBeanStub01 beanArray0 = new FieldChecks_JavaBeanStub01();
        beanArray0.setArray(new String[] { "a", null, "c" });

        // JavaBean-beanArray[1]
        FieldChecks_JavaBeanStub01 beanArray1 = null;

        // JavaBean-beanArray[2]
        FieldChecks_JavaBeanStub01 beanArray2 = new FieldChecks_JavaBeanStub01();
        beanArray2.setArray(new String[] { "a", null, null });

        // JavaBean
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setBeanArray(new Object[] { beanArray0, beanArray1, beanArray2 });

        // ValidatorAction
        va.setMethodParams("java.lang.Object,"
                + "org.apache.commons.validator.ValidatorAction,"
                + "org.apache.commons.validator.Field,"
                + "jp.terasoluna.fw.validation.ValidationErrors");
        va.setName("requiredArray");

        // Field
        field.setProperty("beanArray.array");

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateArraysIndex(bean, va, field,
                errors));
        assertEquals(3, errors.addErrorCount);
        assertSame(bean, errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertEquals("beanArray[0].array[1]", ((Field) errors.fieldList.get(0))
                .getKey());
        assertEquals("beanArray[0].array[1]", ((Field) errors.fieldList.get(0))
                .getProperty());

        assertSame(bean, errors.beanList.get(1));
        assertSame(va, errors.vaList.get(1));
        assertEquals("beanArray[2].array[1]", ((Field) errors.fieldList.get(1))
                .getKey());
        assertEquals("beanArray[2].array[1]", ((Field) errors.fieldList.get(1))
                .getProperty());

        assertSame(bean, errors.beanList.get(2));
        assertSame(va, errors.vaList.get(1));
        assertEquals("beanArray[2].array[2]", ((Field) errors.fieldList.get(2))
                .getKey());
        assertEquals("beanArray[2].array[2]", ((Field) errors.fieldList.get(2))
                .getProperty());
    }

    /**
     * testValidateArraysIndex10() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) bean:JavaBean<br>
     * String[] array = {};<br>
     * (引数) va:methodParams="java.lang.Object, org.apache.commons.validator.ValidatorAction, org.apache.commons.validator.Field,
     * jp.terasoluna.fw.validation.ValidationErrors"<br>
     * name="requiredArray"<br>
     * (引数) field:property="array"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * varのプロパティで指定されたフィールドが配列型のフィールドで、 要素がない場合trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex10() throws Exception {
        // 前処理
        // JavaBean
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setArray(new String[] {});

        // ValidatorAction
        va.setMethodParams("java.lang.Object,"
                + "org.apache.commons.validator.ValidatorAction,"
                + "org.apache.commons.validator.Field,"
                + "jp.terasoluna.fw.validation.ValidationErrors");
        va.setName("requiredArray");

        // Field
        field.setProperty("array");

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateArraysIndex(bean, va, field,
                errors));
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateArraysIndex11() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:JavaBean<br>
     * String[] array = {};<br>
     * (引数) va:null<br>
     * ※本来ありえないがカバレッジの為条件に追加<br>
     * (引数) field:property="array"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ：null<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ：null<br>
     * 例外：NullPointerException<br>
     * <br>
     * 処理中にValidatorException、InvocationTargetException以外の例外が発生した場合、 ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex11() throws Exception {
        // 前処理
        // JavaBean
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setArray(new String[] {});

        // ValidatorAction
        va = null;

        // Field
        field.setProperty("array");

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArraysIndex(bean, va, field, errors);
            fail();
        } catch (ValidatorException e) {
            // OK
            assertNull(e.getMessage());
            assertThat(logger.getLoggingEvents().get(0).getMessage(), is(
                    equalTo("null")));
            assertThat(logger.getLoggingEvents().get(0).getThrowable().get(),
                    instanceOf(NullPointerException.class));
        }
    }

    /**
     * testValidateArraysIndex12() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:JavaBean<br>
     * String[] array = {<br>
     * "a"<br>
     * };<br>
     * (引数) va:methodParams="java.lang.Object, org.apache.commons.validator.ValidatorAction, org.apache.commons.validator.Field,
     * jp.terasoluna.fw.validation.ValidationErrors"<br>
     * name="maskArray"<br>
     * (引数) field:property="array"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ："var[mask] must be specified."<br>
     * <br>
     * 処理中にValidatorExceptionをラップしたInvocationTargetExceptionが発生した場合、 ラップしたValidatorExceptionがスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex12() throws Exception {
        // 前処理
        // JavaBean
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setArray(new String[] { "a" });

        // ValidatorAction
        va.setMethodParams("java.lang.Object,"
                + "org.apache.commons.validator.ValidatorAction,"
                + "org.apache.commons.validator.Field,"
                + "jp.terasoluna.fw.validation.ValidationErrors");
        va.setName("maskArray");

        // Field
        field.setProperty("array");

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArraysIndex(bean, va, field, errors);
            fail();
        } catch (ValidatorException e) {
            // OK
            assertEquals("var[mask] must be specified.", e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(e
                    .getMessage()))));
        }
    }

    /**
     * testValidateArraysIndex13() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:JavaBean<br>
     * String[] array = {"a", null, "c"}<br>
     * (引数) va:methodParams="java.lang.Object, org.apache.commons.validator.ValidatorAction, org.apache.commons.validator.Field,
     * jp.terasoluna.fw.validation.ValidationErrors"<br>
     * name="requiredArray"<br>
     * (引数) field:property="array"<br>
     * (引数) errors:addErrorsでRuntimeExceptionが発生<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ：RuntimeException.getMessage();<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ：RuntimeException.getMessage();<br>
     * 例外：RuntimeException<br>
     * <br>
     * 処理中にValidatorException以外の例外をラップした InvocationTargetExceptionが発生した場合、 ラップした例外のメッセージを保持したValidatorExceptionがスローされることを確認する。
     * <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex13() throws Exception {
        // 前処理
        // JavaBean
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setArray(new String[] { "a", null, "c" });

        // ValidatorAction
        va.setMethodParams("java.lang.Object,"
                + "org.apache.commons.validator.ValidatorAction,"
                + "org.apache.commons.validator.Field,"
                + "jp.terasoluna.fw.validation.ValidationErrors");
        va.setName("requiredArray");

        // Field
        field.setProperty("array");

        // errors （エラー追加時に例外が発生する）
        FieldChecks_ValidationErrorsImpl02 errors2 = new FieldChecks_ValidationErrorsImpl02();

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArraysIndex(bean, va, field, errors2);
            fail();
        } catch (ValidatorException e) {
            // OK
            assertEquals(new RuntimeException().getMessage(), e.getMessage());
        }
    }

    /**
     * testValidateArraysIndex14() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:JavaBean<br>
     * String[] array = {};<br>
     * (引数) va:methodParams="java.lang.Object, org.apache.commons.validator.ValidatorAction, org.apache.commons.validator.Field,
     * jp.terasoluna.fw.validation.ValidationErrors"<br>
     * name="requiredArray"<br>
     * (引数) field:null<br>
     * ※本来ありえないがカバレッジの為条件に追加<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ：null<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ：null<br>
     * 例外：NullPointerException<br>
     * <br>
     * 処理中にValidatorException、InvocationTargetException以外の例外が発生した場合、 ValidatorExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateArraysIndex14() throws Exception {
        // 前処理
        // JavaBean
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setArray(new String[] {});

        // ValidatorAction
        va.setMethodParams("java.lang.Object,"
                + "org.apache.commons.validator.ValidatorAction,"
                + "org.apache.commons.validator.Field,"
                + "jp.terasoluna.fw.validation.ValidationErrors");
        va.setName("requiredArray");

        // Field
        field = null;

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArraysIndex(bean, va, field, errors);
            fail();
        } catch (ValidatorException e) {
            // OK
            assertNull(e.getMessage());
            assertThat(logger.getLoggingEvents().get(0).getMessage(), is(
                    equalTo("null")));
            assertThat(logger.getLoggingEvents().get(0).getThrowable().get(),
                    instanceOf(NullPointerException.class));
        }
    }
}
