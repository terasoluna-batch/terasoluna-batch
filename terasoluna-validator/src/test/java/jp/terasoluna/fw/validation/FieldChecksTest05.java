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

import jp.terasoluna.utlib.LogUTUtil;
import junit.framework.TestCase;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.Var;

/**
 * {@link jp.terasoluna.fw.validation.FieldChecks} クラスのブラックボックステスト。
 *
 * <p>
 * <h4>【クラスの概要】</h4>
 * TERASOLUNAの入力チェック機能で共通に使用される検証ルールクラス。
 * <p>
 *
 * @see jp.terasoluna.fw.validation.FieldChecks
 */
public class FieldChecksTest05 extends TestCase {

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
        junit.swingui.TestRunner.run(FieldChecksTest05.class);
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
    public FieldChecksTest05(String name) {
        super(name);
    }

    /**
     * testValidateFloatRange01()
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
    public void testValidateFloatRange01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateFloatRange(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateFloatRange02()
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
    public void testValidateFloatRange02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateFloatRange("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateFloatRange03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * 引数のbeanがfloat型に変換できない場合、エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateFloatRange03() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateFloatRange("test", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("test", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateFloatRange04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"1.4E-45"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                floatRangeMin=null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのfloatRangeMinがnullの場合、範囲の最小値がFloat.MIN_VALUEであることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateFloatRange04() throws Exception {
        // テスト実施
        // 判定
        assertTrue(
                new FieldChecks().validateFloatRange("1.4E-45", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateFloatRange05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"1.4E-45"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                floatRangeMin=""<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのfloatRangeMinが空文字の場合、範囲の最小値がFloat.MIN_VALUEであることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateFloatRange05() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("floatRangeMin");
        var.setValue("");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(
                new FieldChecks().validateFloatRange("1.4E-45", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateFloatRange06()
     * <br><br>
     *
     * (正常系) or (異常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"3.4028235E38"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                floatRangeMax=null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのfloatRangeMaxがnullの場合、範囲の最大値がFloat.MAX_VALUEであることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateFloatRange06() throws Exception {
        // テスト実施
        // 判定
        assertTrue(
                new FieldChecks().validateFloatRange(
                        "3.4028235E38", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateFloatRange07()
     * <br><br>
     *
     * (正常系) or (異常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"3.4028235E38"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                floatRangeMax=""<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのfloatRangeMaxが空文字の場合、範囲の最大値がFloat.MAX_VALUEであることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateFloatRange07() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("floatRangeMax");
        var.setValue("");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(
                new FieldChecks().validateFloatRange(
                        "3.4028235E38", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateFloatRange08()
     * <br><br>
     *
     * (正常系) or (異常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"0.5E1"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                floatRangeMin="test"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file. - floatRangeMin is not number. You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file. - floatRangeMin is not number. You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのfloatRangeMinの値がfloat型に変換できない場合、ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateFloatRange08() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("floatRangeMin");
        var.setValue("test");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateFloatRange("0.5E1", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                    "- floatRangeMin is not number. You'll have to check " +
                    "it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateFloatRange09()
     * <br><br>
     *
     * (正常系) or (異常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"0.5E1"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                floatRangeMax="test"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file. - floatRangeMax is not number. You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file. - floatRangeMax is not number. You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのfloatRangeMaxの値がfloat型に変換できない場合、ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateFloatRange09() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("floatRangeMax");
        var.setValue("test");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateFloatRange("0.5E1", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                    "- floatRangeMax is not number. You'll have to check " +
                    "it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateFloatRange10()
     * <br><br>
     *
     * (正常系) or (異常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"0.5E1"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                floatRangeMin="0.1E1"<br>
     *                floatRangeMax="0.1E2"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * beanの値が、floatRangeMinとfloatRangeMaxの範囲内の値の場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateFloatRange10() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("floatRangeMin");
        var1.setValue("0.1E1");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("floatRangeMax");
        var2.setValue("0.1E2");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(
                new FieldChecks().validateFloatRange("0.5E1", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);

    }

    /**
     * testValidateFloatRange11()
     * <br><br>
     *
     * (正常系) or (異常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"0.5E1"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                floatRangeMin="0.1E2"<br>
     *                floatRangeMax="0.1E3"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * beanの値が、floatRangeMinの値より小さい場合、エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateFloatRange11() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("floatRangeMin");
        var1.setValue("0.1E2");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("floatRangeMax");
        var2.setValue("0.1E3");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(
                new FieldChecks().validateFloatRange("0.5E1", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("0.5E1", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateFloatRange12()
     * <br><br>
     *
     * (正常系) or (異常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"0.5E1"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                floatRangeMin="0.1E1"<br>
     *                floatRangeMax="0.4E1"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * beanの値が、floatRangeMaxの値より大きい場合、エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateFloatRange12() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("floatRangeMin");
        var1.setValue("0.1E1");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("floatRangeMax");
        var2.setValue("0.4E1");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(
                new FieldChecks().validateFloatRange("0.5E1", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("0.5E1", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateFloatRange13()
     * <br><br>
     *
     * (正常系) or (異常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"0.5E1"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                floatRangeMin="0.5E1"<br>
     *                floatRangeMax="0.5E1"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * beanの値と、floatRangeMax,floatRangeMinの値と等しい場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateFloatRange13() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("floatRangeMin");
        var1.setValue("0.5E1");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("floatRangeMax");
        var2.setValue("0.5E1");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(
                new FieldChecks().validateFloatRange("0.5E1", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateMaxLength01()
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
    public void testValidateMaxLength01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateMaxLength(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateMaxLength02()
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
     * 期待値：(状態変化) return: true<br>
     *
     * <br>
     * varのmaxlengthがnullの場合、ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMaxLength02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateMaxLength("", va, field, errors));
        
        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateMaxLength03()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                maxlength=null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file. - maxlength is not number. You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file. - maxlength is not number. You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのmaxlengthがnullの場合、ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMaxLength03() throws Exception {
        // テスト実施
        // 判定
        try {
            new FieldChecks().validateMaxLength("test", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                    "- maxlength is not number. You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateMaxLength04()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                maxlength=""<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file. - maxlength is not number. You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file. - maxlength is not number. You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのmaxlengthが空文字の場合、ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMaxLength04() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("maxlength");
        var.setValue("");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateMaxLength("test", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                    "- maxlength is not number. You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateMaxLength05()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                maxlength="abc"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file. - maxlength is not number. You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file. - maxlength is not number. You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのmaxlengthが数値に変換できない場合、ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMaxLength05() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("maxlength");
        var.setValue("abc");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateMaxLength("test", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                    "- maxlength is not number. You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateMaxLength06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                maxlength="5"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 入力された文字の桁数が、varのmaxlengthの値より小さい場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMaxLength06() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("maxlength");
        var.setValue("5");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateMaxLength("test", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);

    }

    /**
     * testValidateMaxLength07()
     * <br><br>
     *
     * (正常系) or (異常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                maxlength="3"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * 入力された文字の桁数が、varのmaxlengthの値より大きい場合、エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMaxLength07() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("maxlength");
        var.setValue("3");
        field.addVar(var);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateMaxLength("test", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("test", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));

    }

    /**
     * testValidateMaxLength08()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                maxlength="4"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 入力された文字の桁数が、varのmaxlengthの値と等しい場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMaxLength08() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("maxlength");
        var.setValue("4");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateMaxLength("test", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);

    }

    /**
     * testValidateMinLength01()
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
    public void testValidateMinLength01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateMinLength(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateMinLength02()
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
     * 期待値：(状態変化) return : true<br>
     *
     * <br>
     * varのminlengthがnullの場合、ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMinLength02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateMinLength("", va, field, errors));
     
        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateMinLength03()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                minlength=null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - minlength is not number.
     *                    You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - minlength is not number.
     *                    You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのminlengthがnullの場合、ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMinLength03() throws Exception {
        // テスト実施
        // 判定
        try {
            new FieldChecks().validateMinLength("test", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                    "- minlength is not number. You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateMinLength04()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                minlength=""<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - minlength is not number.
     *                    You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - minlength is not number.
     *                    You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのminlengthが空文字の場合、ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMinLength04() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("minlength");
        var.setValue("");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateMinLength("test", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                    "- minlength is not number. You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateMinLength05()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                minlength="abc"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - minlength is not number.
     *                    You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - minlength is not number.
     *                    You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのminlengthが数値に変換できない場合、ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMinLength05() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("minlength");
        var.setValue("abc");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateMinLength("test", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                    "- minlength is not number. You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateMinLength06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                minlength="3"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 入力された文字の桁数が、varのminlengthの値より大きい場合、
     * trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMinLength06() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("minlength");
        var.setValue("3");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateMinLength("test", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);

    }

    /**
     * testValidateMinLength07()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                minlength="5"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * 入力された文字の桁数が、varのminlengthの値より小さい場合、
     * エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMinLength07() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("minlength");
        var.setValue("5");
        field.addVar(var);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateMinLength("test", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("test", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));

    }

    /**
     * testValidateMinLength08()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                minlength="4"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 入力された文字の桁数が、varのminlengthの値と等しい場合、
     * trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateMinLength08() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("minlength");
        var.setValue("4");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateMinLength("test", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);

    }

}
