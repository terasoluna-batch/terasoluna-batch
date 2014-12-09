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
import java.util.Collection;

import jp.terasoluna.fw.util.PropertyAccessException;
import jp.terasoluna.utlib.LogUTUtil;
import junit.framework.TestCase;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.Var;

/**
 * {@link jp.terasoluna.fw.validation.FieldChecks}
 * クラスのブラックボックステスト。
 *
 * <p>
 * <h4>【クラスの概要】</h4>
 * TERASOLUNAの入力チェック機能で共通に使用される検証ルールクラス。
 * <p>
 *
 * @see jp.terasoluna.fw.validation.FieldChecks
 */
public class FieldChecksTest08 extends TestCase {

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
     * このテストケースを実行する為の
     * GUI アプリケーションを起動する。
     *
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(FieldChecksTest08.class);
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
        va = new ValidatorAction();
        field = new Field();
        errors = new FieldChecks_ValidationErrorsImpl01();
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
    public FieldChecksTest08(String name) {
        super(name);
    }

    /**
     * testValidateNumericString01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C,F
     * <br><br>
     * 入力値：(引数) bean:null<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanがnullの場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumericString01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateNumericString(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateNumericString02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C,F
     * <br><br>
     * 入力値：(引数) bean:""<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanが空文字の場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumericString02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateNumericString("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateNumericString03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:100.05<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * 引数のbeanに数字以外の文字が含まれる場合、エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumericString03() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateNumericString(
                "100.05", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("100.05", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateNumericString04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:1234567890<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanが数字のみで構成されている場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumericString04() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateNumericString(
                "1234567890", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateStringLength01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C,F
     * <br><br>
     * 入力値：(引数) bean:null<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanがnullの場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateStringLength01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateStringLength(
                null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateStringLength02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C,F
     * <br><br>
     * 入力値：(引数) bean:""<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanが空文字の場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateStringLength02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateStringLength(
                "", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateStringLength03()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                stringLength=null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file. " +
                    "- stringLength is not number. " +
                    "You'll have to check it over. ";<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file. " +
                    "- stringLength is not number. " +
                    "You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのstringLengthがnullの場合、ValidatorExceptionがスローされることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateStringLength03() throws Exception {
        // テスト実施
        // 判定
        try {
            new FieldChecks().validateStringLength("test", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
            "- stringLength is not number. " +
            "You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateStringLength04()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                stringLength=""<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file. " +
     *        "- stringLength is not number. " +
     *        "You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file. " +
     *        "- stringLength is not number. " +
     *        "You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのstringLengthが空文字の場合、
     * ValidatorExceptionがスローされることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateStringLength04() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("stringLength");
        var.setValue("");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateStringLength("test", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
            "- stringLength is not number. " +
            "You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateStringLength05()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                stringLength="test"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - stringLength is not number.
     *                    You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - stringLength is not number.
     *                    You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのstringLengthが数値に変換できない場合、
     * ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateStringLength05() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("stringLength");
        var.setValue("test");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateStringLength("test", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                    "- stringLength is not number. " +
                    "You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateStringLength06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                stringLength="3"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * 引数のbeanの桁数が、varのstringLengthの値より大きい場合、
     * エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateStringLength06() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("stringLength");
        var.setValue("3");
        field.addVar(var);

        // テスト実施
        // 判定
        assertFalse(
                new FieldChecks().validateStringLength("test", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("test", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateStringLength07()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                stringLength="5"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * 引数のbeanの桁数が、varのstringLengthの値より小さい場合、
     * エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateStringLength07() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("stringLength");
        var.setValue("5");
        field.addVar(var);

        // テスト実施
        // 判定
        assertFalse(
                new FieldChecks().validateStringLength("test", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("test", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateStringLength08()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                stringLength="4"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanの桁数が、varのstringLengthの値と一致する場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateStringLength08() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("stringLength");
        var.setValue("4");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(
                new FieldChecks().validateStringLength("test", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateArrayRange01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C,F,G
     * <br><br>
     * 入力値：(引数) bean:null<br>
     *         (引数) va:not null<br>
     *         (引数) field:property="field"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ：
     *                    "target of validateArrayRange must be not null."<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ：
     *                    "target of validateArrayRange must be not null."<br>
     *
     * <br>
     * 引数のbeanがnullの場合、ValidatorExceptionがスローされることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange01() throws Exception {
        // 前処理
        field.setProperty("field");

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArrayRange(null, va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "target of validateArrayRange must be not null.";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message));
        }

    }

    /**
     * testValidateArrayRange02()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *                 testField属性が存在しない<br>
     *         (引数) va:not null<br>
     *         (引数) field:property="testField"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Cannot get property type[" +
     *                    JavaBean.class.getName+".testField]"<br>
     *         (状態変化) ログ:ValidatorException<br>
     *                    メッセージ："Cannot get property type[" +
     *                    JavaBean.class.getName+".testField]"<br>
     *
     * <br>
     * fieldのproperty属性で指定された属性がbeanに存在しない場合、
     * ValidatorExceptionがスローされることを確認する。<br>
     * ※BeanUtil.getPropertyTypeがnullを返却する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange02() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        field.setProperty("testField");

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArrayRange(bean, va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Cannot get property type[" +
                bean.getClass().getName() + ".testField]";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message));
        }
    }

    /**
     * testValidateArrayRange03()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *         (引数) va:not null<br>
     *         (引数) field:property=null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Cannot get property type[" +
     *                    JavaBean.class.getName+".null]"<br>
     *         (状態変化) ログ:ValidatorException<br>
     *                    メッセージ："Cannot get property type[" +
     *                    JavaBean.class.getName+".null]", new PropertyAccessException(new IllegalArgumentException())<br>
     *
     * <br>
     * fieldのproperty属性がnullの場合、
     * ValidatorExceptionがスローされることを確認する。<br>
     * ※BeanUtil.getPropertyTypeがPropertyAccessExceptionをスローする。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange03() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArrayRange(bean, va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Cannot get property type[" +
                bean.getClass().getName() + ".null]";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message,
                new PropertyAccessException(new IllegalArgumentException())));
        }
    }

    /**
     * testValidateArrayRange04()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *         (引数) va:not null<br>
     *         (引数) field:property=""<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Cannot get property type[" +
     *                    JavaBean.class.getName+".testField]"<br>
     *         (状態変化) ログ:ValidatorException<br>
     *                    メッセージ："Cannot get property type[" +
     *                    JavaBean.class.getName+".testField]"<br>
     *
     * <br>
     * fieldのproperty属性が空文字の場合、
     * ValidatorExceptionがスローされることを確認する。<br>
     * ※BeanUtil.getPropertyTypeがnullを返却する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange04() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        field.setProperty("");

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArrayRange(bean, va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Cannot get property type[" +
                bean.getClass().getName() + ".]";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message));
        }
    }

    /**
     * testValidateArrayRange05()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *                field1(String[]型)=null<br>
     *         (引数) va:not null<br>
     *         (引数) field:property="field1"<br>
     *                var:<br>
     *                minArrayLength="test"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - minArrayLength is not number.
     *                    You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - minArrayLength is not number.
     *                    You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのminArrayLengthが数値に変換できない場合、ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange05() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setField1(null);
        field.setProperty("field1");
        Var var = new Var();
        var.setName("minArrayLength");
        var.setValue("test");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArrayRange(bean, va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                    "- minArrayLength is not number. " +
                    "You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateArrayRange06()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *                field1(String[]型)=null<br>
     *         (引数) va:not null<br>
     *         (引数) field:property="field1"<br>
     *                var:<br>
     *                maxArrayLength="test"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - maxArrayLength is not number.
     *                    You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - maxArrayLength is not number.
     *                    You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのmaxArrayLengthが数値に変換できない場合、
     * ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange06() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setField1(null);
        field.setProperty("field1");
        Var var = new Var();
        var.setName("maxArrayLength");
        var.setValue("test");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArrayRange(bean, va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                    "- maxArrayLength is not number. " +
                    "You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateArrayRange07()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *                field1(String[]型)=null<br>
     *         (引数) va:not null<br>
     *         (引数) field:property="field1"<br>
     *                var:<br>
     *                minArrayLength="0"<br>
     *                maxArrayLength="0"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * beanのチェック対象のフィールド値がnullの場合、
     * 配列サイズ「0」としてチェックが行われることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange07() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setField1(null);
        field.setProperty("field1");
        Var var1 = new Var();
        var1.setName("minArrayLength");
        var1.setValue("0");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("maxArrayLength");
        var2.setValue("0");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateArrayRange(bean, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateArrayRange08()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *                field2(String型)="test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:property="field2"<br>
     *                var:<br>
     *                minArrayLength=Integer.MAX_VALUE<br>
     *                maxArrayLength=Integer.MAX_VALUE<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："property [" +
     *                    JavaBean.class.getName +
     *                    ".field2] must be instance of Array or Collection."
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："property [" +
     *                    JavaBean.class.getName +
     *                    ".field2] must be instance of Array or Collection."
     *
     * <br>
     * beanのチェック対象のフィールドが配列・Collection型ではない場合、
     * ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange08() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setField1(null);
        field.setProperty("field2");
        Var var1 = new Var();
        var1.setName("minArrayLength");
        var1.setValue(String.valueOf(Integer.MAX_VALUE));
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("maxArrayLength");
        var2.setValue(String.valueOf(Integer.MAX_VALUE));
        field.addVar(var2);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArrayRange(bean, va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "property [" +bean.getClass().getName() +
                ".field2] must be instance of Array or Collection.";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message));
        }
    }

    /**
     * testValidateArrayRange09()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *                field1(String[]型)={}<br>
     *                ※要素なし<br>
     *         (引数) va:not null<br>
     *         (引数) field:property="field1"<br>
     *                var:<br>
     *                maxArrayLength=Integer.MAX_VALUE<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのminArrayLengthが取得できない場合、
     * 範囲の最小値が0でチェックが行われることを確認。<br>
     * ※maxArrayLengthが省略された場合はInteger.MAX_LENGTHが最大値になるが
     * 物理的に不可能なためテストは行わない。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange09() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setField1(null);
        field.setProperty("field1");
        Var var = new Var();
        var.setName("maxArrayLength");
        var.setValue(String.valueOf(Integer.MAX_VALUE));
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateArrayRange(bean, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateArrayRange10()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *                field1(String[]型)={}<br>
     *                ※要素なし<br>
     *         (引数) va:not null<br>
     *         (引数) field:property="field1"<br>
     *                var:<br>
     *                minArrayLength=""<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのminArrayLengthが空文字の場合、範囲の最小値が0でチェックが
     * 行われることを確認。<br>
     * ※maxArrayLengthが空文字の場合はInteger.MAX_LENGTHが最大値になるが
     * 物理的に不可能なためテストは行わない。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange10() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        bean.setField1(null);
        field.setProperty("field1");
        Var var = new Var();
        var.setName("minArrayLength");
        var.setValue("");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateArrayRange(bean, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateArrayRange11()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *                field1(String[]型)={<br>
     *                    "test1","test2","test3"<br>
     *                }<br>
     *                ※要素３<br>
     *         (引数) va:not null<br>
     *         (引数) field:property="field1"<br>
     *                var:<br>
     *                minArrayLength="1"<br>
     *                maxArrayLength="5"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * beanのチェック対象の配列フィールドの要素数が、
     * varのminArrayLengthとmaxArrayLengthの範囲内のとき、
     * trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange11() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        String[] array = {
            "test1", "test2", "test3"
        };
        bean.setField1(array);
        field.setProperty("field1");
        Var var1 = new Var();
        var1.setName("minArrayLength");
        var1.setValue("1");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("maxArrayLength");
        var2.setValue("5");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateArrayRange(bean, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateArrayRange12()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *                field3(Collection型)={<br>
     *                    "test1","test2","test3"<br>
     *                }<br>
     *                ※要素３<br>
     *         (引数) va:not null<br>
     *         (引数) field:property="field3"<br>
     *                var:<br>
     *                minArrayLength="4"<br>
     *                maxArrayLength="10"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * beanのチェック対象のCollection型フィールドの要素数が、
     * varのminArrayLengthとmaxArrayLengthの範囲より少ない場合、
     * エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testValidateArrayRange12() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        Collection list = new ArrayList();
        list.add("test1");
        list.add("test2");
        list.add("test3");
        bean.setField3(list);
        field.setProperty("field3");
        Var var1 = new Var();
        var1.setName("minArrayLength");
        var1.setValue("4");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("maxArrayLength");
        var2.setValue("10");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateArrayRange(bean, va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertSame(bean, errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateArrayRange13()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *                field4(int[]型)={<br>
     *                    1,2,3,4,5,6,7,8,9,0<br>
     *                }<br>
     *                ※要素10<br>
     *         (引数) va:not null<br>
     *         (引数) field:property="field4"<br>
     *                var:<br>
     *                minArrayLength="1"<br>
     *                maxArrayLength="5"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * beanのチェック対象のプリミティブ配列型フィールドの要素数が、
     * varのminArrayLengthとmaxArrayLengthの範囲より大きい場合、
     * エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange13() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        int[] array = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 0
        };
        bean.setField4(array);
        field.setProperty("field4");
        Var var1 = new Var();
        var1.setName("minArrayLength");
        var1.setValue("1");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("maxArrayLength");
        var2.setValue("5");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateArrayRange(bean, va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertSame(bean, errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateArrayRange14()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *                field1(String[]型)={<br>
     *                    "test1","test2","test3"<br>
     *                }<br>
     *                ※要素３<br>
     *         (引数) va:not null<br>
     *         (引数) field:property="field1"<br>
     *                var:<br>
     *                minArrayLength="3"<br>
     *                maxArrayLength="3"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * beanのチェック対象の配列フィールドの要素数と、
     * varのminArrayLengthとmaxArrayLengthの値が等しいとき、
     * trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange14() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        String[] array = {
            "test1", "test2", "test3"
        };
        bean.setField1(array);
        field.setProperty("field1");
        Var var1 = new Var();
        var1.setName("minArrayLength");
        var1.setValue("3");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("maxArrayLength");
        var2.setValue("3");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateArrayRange(bean, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateArrayRange15()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:JavaBean<br>
     *                field5(Stiring[]型)<br>
     *                getField5で例外が発生する。<br>
     *         (引数) va:not null<br>
     *         (引数) field:property="field5"<br>
     *                var:<br>
     *                minArrayLength="3"<br>
     *                maxArrayLength="3"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Cannot get property [" +
     *                    JavaBean.class.getName+".field5]"<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Cannot get property [" +
     *                    JavaBean.class.getName+".field5]", 
                new PropertyAccessException(new IllegalArgumentException())));<br>
     *
     * <br>
     * チェック対象のプロパティの取得時に例外が発生した場合、
     * ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateArrayRange15() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        field.setProperty("field5");
        Var var1 = new Var();
        var1.setName("minArrayLength");
        var1.setValue("3");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("maxArrayLength");
        var2.setValue("3");
        field.addVar(var2);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateArrayRange(bean, va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Cannot get property [" +
                bean.getClass().getName() + ".field5]";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message,
                new PropertyAccessException(new IllegalArgumentException())));
        }
    }

}
