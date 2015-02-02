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

import java.lang.reflect.InvocationTargetException;

import jp.terasoluna.fw.util.ClassLoadException;
import jp.terasoluna.utlib.LogUTUtil;
import junit.framework.TestCase;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.Var;

/**
 * {@link FieldChecks} クラスのテスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * TERASOLUNAの入力チェック機能で共通に使用される検証ルールクラス。
 * <p>
 * 
 * @see jp.terasoluna.fw.validation.FieldChecks
 */
public class FieldChecksTest11 extends TestCase {
    
    /**
     * このテストケースを実行する為の
     * GUI アプリケーションを起動する。
     * 
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(FieldChecksTest11.class);
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
        LogUTUtil.flush();
        FieldChecks_MultiFieldValidatorImpl01.result = false;
        FieldChecks_MultiFieldValidatorImpl01.validateCalledCount = 0;
        FieldChecks_MultiFieldValidatorImpl01.value = null;
        FieldChecks_MultiFieldValidatorImpl01.fields = null;
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
    public FieldChecksTest11(String name) {
        super(name);
    }

    /**
     * testValidateMultiField01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C, F, I
     * <br><br>
     * 入力値：(引数) bean:null<br>
     *         (引数) va:ValidatorActionnクラスのインスタンス<br>
     *         (引数) field:Fieldクラスのインスタンス<br>
     *                <br>
     *                Msg("message, "message")<br>
     *         (引数) errors:MockValidationErrorsクラスのインスタンス<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:フィールドerrorMessageがnullであることを確認する。<br>
     *         (状態変化) ログ:ログレベル：ERROR<br>
     *                    メッセージ："bean is null."<br>
     *         
     * <br>
     * 引数beanがnullの場合にエラーログを出力して、TRUEが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMultiField01() throws Exception {
        // 前処理
        Object bean = null;
        ValidatorAction va = new ValidatorAction();
        Field field = new Field();
        FieldChecks_ValidationErrorsImpl03 errors = new FieldChecks_ValidationErrorsImpl03();

        // テスト実施
        FieldChecks fieldChecks = new FieldChecks();
        boolean result = 
            fieldChecks.validateMultiField(bean, va, field, errors);

        // 判定
        assertTrue(result);
        assertNull(errors.errorMessage);
        assertTrue(LogUTUtil.checkError("bean is null."));
    }

    /**
     * testValidateMultiField02()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：C, F, G, I
     * <br><br>
     * 入力値：(引数) bean:""（空文字）<br>
     *         (引数) va:ValidatorActionnクラスのインスタンス<br>
     *         (引数) field:以下のフィールドを設定したFieldクラスのインスタンス<br>
     *                <br>
     *                var：multiFieldValidator=null<br>
     *         (引数) errors:MockValidationErrorsクラスのインスタンス<br>
     *         
     * <br>
     * 期待値：(状態変化) errors:フィールドerrorMessageがnullであることを確認する。<br>
     *         (状態変化) 例外:例外：IllegalArgumentException<br>
     *                    メッセージ：
     *                    "var value[multiFieldValidator] is required."<br>
     *         (状態変化) ログ:ログレベル：ERROR<br>
     *                    メッセージ：
     *                    "var value[multiFieldValidator] is required."<br>
     *         
     * <br>
     * 引数fieldの、var-name：multiFieldValidatorに対応するvar-valueがnullの場合に、
     * エラーログを出力して、IllegalArgumentExceptionがスローされることを確認するテスト。<br>
     * <br>
     * ※引数beanが空文字の場合にチェックが続行されることを確認するテストを包含する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMultiField02() throws Exception {
        // 前処理
        Object bean = "";
        ValidatorAction va = new ValidatorAction();
        Field field = new Field();
        Var var = new Var("multiFieldValidator", null, null);
        field.addVar(var);
        FieldChecks_ValidationErrorsImpl03 errors = new FieldChecks_ValidationErrorsImpl03();

        // テスト実施
        FieldChecks fieldChecks = new FieldChecks();
        try {
            fieldChecks.validateMultiField(bean, va, field, errors);
            fail("IllegalArgumentExceptionがスローされなかった。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertNull(errors.errorMessage);
            assertEquals(IllegalArgumentException.class.getName(), 
                        e.getClass().getName());
            assertEquals("var value[multiFieldValidator] is required.", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                        "var value[multiFieldValidator] is required."));
        }
    }

    /**
     * testValidateMultiField03()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：C, F, G, I
     * <br><br>
     * 入力値：(引数) bean:"bean"<br>
     *         (引数) va:ValidatorActionnクラスのインスタンス<br>
     *         (引数) field:以下のフィールドを設定したFieldクラスのインスタンス<br>
     *                <br>
     *                var：multiFieldValidator=""（空文字）<br>
     *         (引数) errors:MockValidationErrorsクラスのインスタンス<br>
     *         
     * <br>
     * 期待値：(状態変化) errors:フィールドerrorMessageがnullであることを確認する。<br>
     *         (状態変化) 例外:例外：IllegalArgumentException<br>
     *                    メッセージ：
     *                    "var value[multiFieldValidator] is required."<br>
     *         (状態変化) ログ:ログレベル：ERROR<br>
     *                    メッセージ：
     *                    "var value[multiFieldValidator] is required."<br>
     *         
     * <br>
     * 引数fieldの、var-name：multiFieldValidatorに対応するvar-valueが空文字の場合に、
     * エラーログを出力して、IllegalArgumentExceptionがスローされることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMultiField03() throws Exception {
        // 前処理
        Object bean = "bean";
        ValidatorAction va = new ValidatorAction();
        Field field = new Field();
        Var var = new Var("multiFieldValidator", "", null);
        field.addVar(var);
        FieldChecks_ValidationErrorsImpl03 errors = new FieldChecks_ValidationErrorsImpl03();

        // テスト実施
        FieldChecks fieldChecks = new FieldChecks();
        try {
            fieldChecks.validateMultiField(bean, va, field, errors);
            fail("IllegalArgumentExceptionがスローされなかった。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertNull(errors.errorMessage);
            assertEquals(IllegalArgumentException.class.getName(), 
                        e.getClass().getName());
            assertEquals("var value[multiFieldValidator] is required.", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                        "var value[multiFieldValidator] is required."));
        }
    }

    /**
     * testValidateMultiField04()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：F, G, I
     * <br><br>
     * 入力値：(引数) bean:"bean"<br>
     *         (引数) va:ValidatorActionnクラスのインスタンス<br>
     *         (引数) field:以下のフィールドを設定したFieldクラスのインスタンス<br>
     *                <br>
     *                var：multiFieldValidator="not.Exist"<br>
     *         (引数) errors:MockValidationErrorsクラスのインスタンス<br>
     *         
     * <br>
     * 期待値：(状態変化) errors:フィールドerrorMessageがnullであることを確認する。<br>
     *         (状態変化) 例外:例外：IllegalArgumentException<br>
     *                    メッセージ："var value[multiFieldValidator] is invalid."<br>
     *                    ラップされた例外：ClassLoadException<br>
     *         (状態変化) ログ:ログレベル：ERROR<br>
     *                    メッセージ："var value[multiFieldValidator] is invalid."<br>
     *                    ラップされた例外：ClassLoadException<br>
     *         
     * <br>
     * 引数fieldの、var-name：multiFieldValidatorに対応するvar-valueがクラスパス上に
     * 存在しないクラス名の場合に、エラーログを出力して、IllegalArgumentExceptionが
     * スローされることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMultiField04() throws Exception {
        // 前処理
        Object bean = "bean";
        ValidatorAction va = new ValidatorAction();
        Field field = new Field();
        Var var = new Var("multiFieldValidator", "not.Exist", null);
        field.addVar(var);
        FieldChecks_ValidationErrorsImpl03 errors = new FieldChecks_ValidationErrorsImpl03();

        // テスト実施
        FieldChecks fieldChecks = new FieldChecks();
        try {
            fieldChecks.validateMultiField(bean, va, field, errors);
            fail("IllegalArgumentExceptionがスローされなかった。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertNull(errors.errorMessage);
            assertEquals(IllegalArgumentException.class.getName(), 
                        e.getClass().getName());
            assertEquals("var value[multiFieldValidator] is invalid.", 
                        e.getMessage());
            assertTrue(e.getCause() instanceof ClassLoadException);
            assertTrue(LogUTUtil.checkError(
                        "var value[multiFieldValidator] is invalid.", 
                        new ClassLoadException(new RuntimeException())));
        }
    }

    /**
     * testValidateMultiField05()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：F, G, I
     * <br><br>
     * 入力値：(引数) bean:"bean"<br>
     *         (引数) va:ValidatorActionnクラスのインスタンス<br>
     *         (引数) field:以下のフィールドを設定したFieldクラスのインスタンス<br>
     *                <br>
     *                var：multiFieldValidator="java.lang.String"<br>
     *         (引数) errors:MockValidationErrorsクラスのインスタンス<br>
     *         
     * <br>
     * 期待値：(状態変化) errors:フィールドerrorMessageがnullであることを確認する。<br>
     *         (状態変化) 例外:例外：IllegalArgumentException<br>
     *                    メッセージ："var value[multiFieldValidator] is invalid."<br>
     *                    ラップされた例外：ClassCastException<br>
     *         (状態変化) ログ:ログレベル：ERROR<br>
     *                    メッセージ："var value[multiFieldValidator] is invalid."<br>
     *                    ラップされた例外：ClassCastException<br>
     *         
     * <br>
     * 引数fieldの、var-name：multiFieldValidatorに対応するvar-valueが
     * MultiFieldValidatorを実装していないクラス名の場合に、エラーログを出力して、
     * IllegalArgumentExceptionがスローされることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMultiField05() throws Exception {
        // 前処理
        Object bean = "bean";
        ValidatorAction va = new ValidatorAction();
        Field field = new Field();
        Var var = new Var("multiFieldValidator", "java.lang.String", null);
        field.addVar(var);
        FieldChecks_ValidationErrorsImpl03 errors = new FieldChecks_ValidationErrorsImpl03();

        // テスト実施
        FieldChecks fieldChecks = new FieldChecks();
        try {
            fieldChecks.validateMultiField(bean, va, field, errors);
            fail("IllegalArgumentExceptionがスローされなかった。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertNull(errors.errorMessage);
            assertEquals(IllegalArgumentException.class.getName(), 
                        e.getClass().getName());
            assertEquals("var value[multiFieldValidator] is invalid.", 
                        e.getMessage());
            assertTrue(e.getCause() instanceof ClassCastException);
            assertTrue(LogUTUtil.checkError(
                    "var value[multiFieldValidator] is invalid.", 
                    new ClassCastException()));
        }
    }

    /**
     * testValidateMultiField06()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"bean"<br>
     *         (引数) va:ValidatorActionnクラスのインスタンス<br>
     *         (引数) field:以下のフィールドを設定したFieldクラスのインスタンス<br>
     *                <br>
     *         var：multiFieldValidator=
     *         "jp.terasoluna.fw.validation.
     *         FieldChecks_MultiFieldValidatorImpl01"<br>
     *                var：fields=null<br>
     *         (引数) errors:MockValidationErrorsクラスのインスタンス<br>
     *         (状態) MultiFieldValidator#validateの戻り値:TRUEに設定<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:フィールドerrorMessageがnullであることを確認する。<br>
     *         (状態変化) MultiFieldValidator:フィールドvalidateCalledCountが
     *         1であることを確認する。<br>
     *                    <br>
     *                    フィールドvalueが"bean"であることを確認する。<br>
     *                    <br>
     *                    フィールドfieldsの配列長が0であることを確認する。<br>
     *         
     * <br>
     * fieldのvar-name：fieldsに対応するvar-valueがnullの場合、
     * MultiFieldValidator#validateの第二引数に空の配列が渡されることを確認するテスト。<br>
     * <br>
     * ※引数beanが文字列の場合に、MultiFieldValidator#validateの
     * 第一引数にその文字列が渡されることを確認するテストを包含する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMultiField06() throws Exception {
        // 前処理
        Object bean = "bean";
        ValidatorAction va = new ValidatorAction();
        Field field = new Field();
        Var var1 = new Var("multiFieldValidator", 
                "jp.terasoluna.fw.validation."
                + "FieldChecks_MultiFieldValidatorImpl01", null);
        Var var2 = new Var("fields", null, null);
        field.addVar(var1);
        field.addVar(var2);
        FieldChecks_ValidationErrorsImpl03 errors = new FieldChecks_ValidationErrorsImpl03();
        FieldChecks_MultiFieldValidatorImpl01.result = true;

        // テスト実施
        FieldChecks fieldChecks = new FieldChecks();

        boolean result = 
            fieldChecks.validateMultiField(bean, va, field, errors);
    
        // 判定
        assertTrue(result);
        assertNull(errors.errorMessage);
        assertEquals(1, FieldChecks_MultiFieldValidatorImpl01.
                validateCalledCount);
        assertEquals("bean", FieldChecks_MultiFieldValidatorImpl01.value);
        assertNotNull(FieldChecks_MultiFieldValidatorImpl01.fields);
        assertEquals(0, 
                FieldChecks_MultiFieldValidatorImpl01.fields.length);
    }

    /**
     * testValidateMultiField07()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"bean"<br>
     *         (引数) va:ValidatorActionnクラスのインスタンス<br>
     *         (引数) field:以下のフィールドを設定したFieldクラスのインスタンス<br>
     *                <br>
     *                var：multiFieldValidator=
     *                "jp.terasoluna.fw.validation.
     *                FieldChecks_MultiFieldValidatorImpl01"<br>
     *                var：fields=""（空文字）<br>
     *         (引数) errors:MockValidationErrorsクラスのインスタンス<br>
     *         (状態) MultiFieldValidator#validateの戻り値:TRUEに設定<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:フィールドerrorMessageがnullであることを確認する。<br>
     *         (状態変化) MultiFieldValidator:フィールドvalidateCalledCountが1で
     *         あることを確認する。<br>
     *                    <br>
     *                    フィールドvalueが"bean"であることを確認する。<br>
     *                    <br>
     *                    フィールドfieldsの配列長が0であることを確認する。<br>
     *         
     * <br>
     * fieldのvar-name：fieldsに対応するvar-valueが空文字の場合、
     * MultiFieldValidator#validateの第二引数に空の配列が渡されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMultiField07() throws Exception {
        // 前処理
        Object bean = "bean";
        ValidatorAction va = new ValidatorAction();
        Field field = new Field();
        Var var1 = new Var("multiFieldValidator", 
                "jp.terasoluna.fw.validation."
                + "FieldChecks_MultiFieldValidatorImpl01", null);
        Var var2 = new Var("fields", "", null);
        field.addVar(var1);
        field.addVar(var2);
        FieldChecks_ValidationErrorsImpl03 errors = new FieldChecks_ValidationErrorsImpl03();
        FieldChecks_MultiFieldValidatorImpl01.result = true;

        // テスト実施
        FieldChecks fieldChecks = new FieldChecks();

        boolean result = 
            fieldChecks.validateMultiField(bean, va, field, errors);
    
        // 判定
        assertTrue(result);
        assertNull(errors.errorMessage);
        assertEquals(1, FieldChecks_MultiFieldValidatorImpl01.
                validateCalledCount);
        assertEquals("bean", FieldChecks_MultiFieldValidatorImpl01.value);
        assertNotNull(FieldChecks_MultiFieldValidatorImpl01.fields);
        assertEquals(0, 
                FieldChecks_MultiFieldValidatorImpl01.fields.length);
    }

    /**
     * testValidateMultiField08()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:以下のフィールドを設定したFieldChecksExtend_BeanStub01クラスのインスタンス<br>
     *                <br>
     *                field1=Objectクラスのインタンス1<br>
     *                field2=Objectクラスのインタンス2<br>
     *         (引数) va:ValidatorActionnクラスのインスタンス<br>
     *         (引数) field:以下のフィールドを設定したFieldクラスのインスタンス<br>
     *                <br>
     *                property="field1"<br>
     *                var：multiFieldValidator=
     *                "jp.terasoluna.fw.validation.
     *                FieldChecks_MultiFieldValidatorImpl01"<br>
     *                var：fields="field2"<br>
     *         (引数) errors:MockValidationErrorsクラスのインスタンス<br>
     *         (状態) MultiFieldValidator#validateの戻り値:TRUEに設定<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:フィールドerrorMessageがnullであることを確認する。<br>
     *         (状態変化) MultiFieldValidator:フィールドvalidateCalledCountが
     *         1であることを確認する。<br>
     *                    <br>
     *                    フィールドvalueが、設定されたObjectクラスのインタンス1と
     *                    同じインスタンスであることを確認する。<br>
     *                    <br>
     *                    フィールドfieldsの配列長が1であることを確認する。<br>
     *                    <br>
     *                    フィールドfieldsが以下の1要素を持つことを確認する。<br>
     *                    fields[0]=Objectクラスのインタンス2と同じインスタンス<br>
     *         
     * <br>
     * fieldのvar-name：fieldsに対応するvar-valueに、カンマ区切り無しの文字列が
     * 指定されている場合、その名前に対応するプロパティ値を引数beanから取得し、
     * 長さ1の配列としてMultiFieldValidatorの第二引数に渡されることを確認するテスト。<br>
     * <br>
     * ※引数beanがJavaBeanの場合に、引数fieldのプロパティ名に対応するプロパティ値を
     * 引数のbeanから取得し、それがMultiFieldValidator#validateの第一引数に渡される
     * ことを確認するテストを包含する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMultiField08() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub02 bean = new FieldChecks_JavaBeanStub02();
        Object testValue1 = new Object();
        Object testValue2 = new Object();
        bean.field1 = testValue1;
        bean.field2 = testValue2;
        ValidatorAction va = new ValidatorAction();
        Field field = new Field();
        field.setProperty("field1");
        Var var1 = new Var("multiFieldValidator", 
                "jp.terasoluna.fw.validation."
                + "FieldChecks_MultiFieldValidatorImpl01", null);
        Var var2 = new Var("fields", "field2", null);
        field.addVar(var1);
        field.addVar(var2);
        FieldChecks_ValidationErrorsImpl03 errors = new FieldChecks_ValidationErrorsImpl03();
        FieldChecks_MultiFieldValidatorImpl01.result = true;

        // テスト実施
        FieldChecks fieldChecks = new FieldChecks();

        boolean result = 
            fieldChecks.validateMultiField(bean, va, field, errors);
    
        // 判定
        assertTrue(result);
        assertNull(errors.errorMessage);
        assertEquals(1, FieldChecks_MultiFieldValidatorImpl01.
                validateCalledCount);
        assertSame(testValue1, 
                FieldChecks_MultiFieldValidatorImpl01.value);
        assertNotNull(FieldChecks_MultiFieldValidatorImpl01.fields);
        assertEquals(1, 
                FieldChecks_MultiFieldValidatorImpl01.fields.length);
        assertSame(testValue2, 
                FieldChecks_MultiFieldValidatorImpl01.fields[0]);
    }

    /**
     * testValidateMultiField09()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A, D, F, I
     * <br><br>
     * 入力値：(引数) bean:以下のフィールドを設定したFieldChecksExtend_BeanStub01クラスのインスタンス<br>
     *                <br>
     *                field1=Objectクラスのインタンス1<br>
     *                field2=Objectクラスのインタンス2<br>
     *                field3=Objectクラスのインタンス3<br>
     *         (引数) va:ValidatorActionnクラスのインスタンス<br>
     *         (引数) field:以下のフィールドを設定したFieldクラスのインスタンス<br>
     *                <br>
     *                property="invalidProperty"<br>
     *                var：multiFieldValidator=
     *                "jp.terasoluna.fw.validation.
     *                FieldChecks_MultiFieldValidatorImpl01"<br>
     *                var：fields="field1 ,,invalidProperty,field2,field3"<br>
     *         (引数) errors:MockValidationErrorsクラスのインスタンス<br>
     *         (状態) MultiFieldValidator#validateの戻り値:FALSEに設定<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:フィールドerrorMessageが"errorMessage"であることを確認する。<br>
     *         (状態変化) MultiFieldValidator:フィールドvalidateCalledCountが
     *         1であることを確認する。<br>
     *                    <br>
     *                    フィールドvalueが、nullであることを確認する。<br>
     *                    <br>
     *                    フィールドfieldsの配列長が3であることを確認する。<br>
     *                    <br>
     *                    フィールドfieldsが以下の1要素を持つことを確認する。<br>
     *                    fields[0]=Objectクラスのインタンス1と同じインスタンス<br>
     *                    fields[1]=Objectクラスのインタンス2と同じインスタンス<br>
     *                    fields[2]=Objectクラスのインタンス3と同じインスタンス<br>
     *         (状態変化) ログ:ログレベル：ERROR<br>
     *                    メッセージ："Unknown property 'invalidProperty'"<br>  
     *                    ラップされた例外：NoSuchMethodException<br>
     *                    <br>
     *                    ログレベル：ERROR<br>
     *                    メッセージ："Unknown property 'invalidProperty'"<br>
     *                    ラップされた例外：NoSuchMethodException<br>
     *         
     * <br>
     * fieldのvar-name：fieldsに対応するvar-valueに、カンマ区切りの文字列が
     * 指定されている場合、カンマで区切られた全ての名前に対応する全プロパティ値を
     * 引数のbeanから取得し、長さ3の配列としてMultiFieldValidatorの第二引数に
     * 渡されることを確認するテスト。<br>
     * <br>
     * ※MultiFieldValidator#validateの返却値がfalseの場合、エラーメッセージを
     * 追加してfalseが返却されることを確認するテストを包含する。<br>
     * <br>
     * ※相関チェック対象フィールド値を引数のbeanから取得する際に、
     * PropertyUtils#getPropertyにおいて発生したNoSuchMethodExceptionをラップした
     * エラーログを出力してチェックが続行されることを確認するテストを包含する。<br>
     * <br>
     * ※相関チェック依存フィールド値を引数のbeanから取得する際に、
     * PropertyUtils#getPropertyにおいて発生したNoSuchMethodExceptionをラップした
     * エラーログを出力してチェックが続行されることを確認するテストを包含する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMultiField09() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub02 bean = new FieldChecks_JavaBeanStub02();
        Object testValue1 = new Object();
        Object testValue2 = new Object();
        Object testValue3 = new Object();
        bean.field1 = testValue1;
        bean.field2 = testValue2;
        bean.field3 = testValue3;
        ValidatorAction va = new ValidatorAction();
        Field field = new Field();
        field.setProperty("invalidProperty");
        Var var1 = new Var("multiFieldValidator", 
                "jp.terasoluna.fw.validation."
                + "FieldChecks_MultiFieldValidatorImpl01", null);
        Var var2 = new Var(
                "fields", "field1 ,,invalidProperty,field2,field3", null);
        field.addVar(var1);
        field.addVar(var2);
        FieldChecks_ValidationErrorsImpl03 errors = new FieldChecks_ValidationErrorsImpl03();
        FieldChecks_MultiFieldValidatorImpl01.result = false;

        // テスト実施
        FieldChecks fieldChecks = new FieldChecks();

        boolean result = 
            fieldChecks.validateMultiField(bean, va, field, errors);
    
        // 判定
        assertFalse(result);
        assertEquals("errorMessage", errors.errorMessage);
        assertEquals(1, FieldChecks_MultiFieldValidatorImpl01.
                validateCalledCount);
        assertNull(FieldChecks_MultiFieldValidatorImpl01.value);
        assertNotNull(FieldChecks_MultiFieldValidatorImpl01.fields);
        assertEquals(3, 
                FieldChecks_MultiFieldValidatorImpl01.fields.length);
        assertSame(testValue1, 
                FieldChecks_MultiFieldValidatorImpl01.fields[0]);
        assertSame(testValue2, 
                FieldChecks_MultiFieldValidatorImpl01.fields[1]);
        assertSame(testValue3, 
                FieldChecks_MultiFieldValidatorImpl01.fields[2]);
        assertTrue(LogUTUtil.checkError("Unknown property 'invalidProperty' on class 'class jp.terasoluna.fw.validation.FieldChecks_JavaBeanStub02'",
                new NoSuchMethodException()));
        assertTrue(LogUTUtil.checkError("Unknown property 'invalidProperty' on class 'class jp.terasoluna.fw.validation.FieldChecks_JavaBeanStub02'",
                new NoSuchMethodException()));
    }

    /**
     * testValidateMultiField10()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A, F, I
     * <br><br>
     * 入力値：(引数) bean:以下を実装したFieldChecksExtend_BeanStub03クラスのインスタンス<br>
     *                <br>
     *                field1のgetterでRuntimeExceptionをラップした
     *                InvocationTargetExceptionをスローする。<br>
     *                field2のgetterでRuntimeExceptionをラップした
     *                InvocationTargetExceptionをスローする。<br>
     *         (引数) va:ValidatorActionnクラスのインスタンス<br>
     *         (引数) field:以下のフィールドを設定したFieldクラスのインスタンス<br>
     *                <br>
     *                property="field1"<br>
     *                var：multiFieldValidator=
     *                "jp.terasoluna.fw.validation.
     *                FieldChecks_MultiFieldValidatorImpl01"<br>
     *                var：fields="field2"<br>
     *         (引数) errors:MockValidationErrorsクラスのインスタンス<br>
     *         (状態) MultiFieldValidator#validateの戻り値:TRUEに設定<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:フィールドerrorMessageがnullであることを確認する。<br>
     *         (状態変化) MultiFieldValidator:フィールドvalidateCalledCountが
     *         1であることを確認する。<br>
     *                    <br>
     *                    フィールドvalueがnullであることを確認する。<br>
     *                    <br>
     *                    フィールドfieldsの配列長が0であることを確認する。<br>
     *         (状態変化) ログ:ログレベル：ERROR<br>
     *                    ラップされた例外：InvocationTargetException<br>
     *                    <br>
     *                    ログレベル：ERROR<br>
     *                    ラップされた例外：InvocationTargetException<br>
     *         
     * <br>
     * 相関チェック対象フィールド値を引数のbeanから取得する際に、
     * PropertyUtils#getPropertyにおいて発生したInvocationTargetExceptionをラップした
     * エラーログを出力してチェックが続行されることを確認するテスト。<br>
     * <br>
     * ※相関チェック依存フィールド値を引数のbeanから取得する際に、
     * PropertyUtils#getPropertyにおいて発生したInvocationTargetExceptionをラップした
     * エラーログを出力してチェックが続行されることを確認するテストを包含する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMultiField10() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub03 bean = new FieldChecks_JavaBeanStub03();
        ValidatorAction va = new ValidatorAction();
        Field field = new Field();
        field.setProperty("field1");
        Var var1 = new Var("multiFieldValidator", 
                "jp.terasoluna.fw.validation."
                + "FieldChecks_MultiFieldValidatorImpl01", null);
        Var var2 = new Var("fields", "field2", null);
        field.addVar(var1);
        field.addVar(var2);
        FieldChecks_ValidationErrorsImpl03 errors = new FieldChecks_ValidationErrorsImpl03();
        FieldChecks_MultiFieldValidatorImpl01.result = true;

        // テスト実施
        FieldChecks fieldChecks = new FieldChecks();

        boolean result = 
            fieldChecks.validateMultiField(bean, va, field, errors);
    
        // 判定
        assertTrue(result);
        assertNull(errors.errorMessage);
        assertEquals(1, FieldChecks_MultiFieldValidatorImpl01.
                validateCalledCount);
        assertNull(FieldChecks_MultiFieldValidatorImpl01.value);
        assertNotNull(FieldChecks_MultiFieldValidatorImpl01.fields);
        assertEquals(0, 
                FieldChecks_MultiFieldValidatorImpl01.fields.length);
        assertTrue(LogUTUtil.checkError(null, 
                new InvocationTargetException(new RuntimeException())));
        assertTrue(LogUTUtil.checkError(null, 
                new InvocationTargetException(new RuntimeException())));
    }

}
