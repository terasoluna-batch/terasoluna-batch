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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import jp.terasoluna.utlib.LogUTUtil;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.Var;
import org.junit.Before;
import org.junit.Test;

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
public class FieldChecksTest03 {

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
     * testValidateLong01()
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
    @Test
    public void testValidateLong01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateLong(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateLong02()
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
    @Test
    public void testValidateLong02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateLong("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateLong03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"-9223372036854775808"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanの値が、longに変換できる場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateLong03() throws Exception {
        // テスト実施
        // 判定
        assertTrue(
            new FieldChecks().validateLong(
                "-9223372036854775808", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateLong04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"あ"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * 引数のbeanの値が、longに変換できない場合、エラーを追加して、
     * falseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateLong04() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateLong("あ", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("あ", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateFloat01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F,G
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
    @Test
    public void testValidateFloat01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateFloat(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateFloat02()
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
    @Test
    public void testValidateFloat02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateFloat("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateFloat03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"1.4E-45"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanの値が、floatに変換できる場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateFloat03() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateFloat("1.4E-45", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateFloat04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"あ"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * 引数のbeanの値が、floatに変換できない場合、エラーを追加して、falseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateFloat04() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateFloat("あ", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("あ", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateDouble01()
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
    @Test
    public void testValidateDouble01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDouble(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDouble02()
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
    @Test
    public void testValidateDouble02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDouble("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDouble03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"4.9E-324"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanの値が、doubleに変換できる場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDouble03() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDouble("4.9E-324", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDouble04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"あ"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * 引数のbeanの値が、doubleに変換できない場合、エラーを追加して、falseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDouble04() throws Exception {
        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateDouble("あ", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("あ", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateDate01()
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
    @Test
    public void testValidateDate01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDate(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDate02()
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
    @Test
    public void testValidateDate02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDate("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDate03()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"2005/11/17"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                datePattern=null<br>
     *                datePatternStrict=null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file.
     *                     - datePattern or datePatternStrict is invalid.
     *                     You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file.
     *                     - datePattern or datePatternStrict is invalid.
     *                      You'll have to check it over. ", new IllegalArgumentException()<br>
     *
     * <br>
     * datePattern、datePatternStrictが両方ともnullの場合、
     * ValidatorExceptionがスローされることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDate03() throws Exception {
        // テスト実施
        // 判定
        try {
            new FieldChecks().validateDate("2005/11/17", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                "- datePattern or datePatternStrict is invalid." +
                " You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new IllegalArgumentException()));
        }
    }

    /**
     * testValidateDate04()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"2005/11/17"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                datePattern=""<br>
     *                datePatternStrict=""<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file.
     *                     - datePattern or datePatternStrict is invalid.
     *                     You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file.
     *                     - datePattern or datePatternStrict is invalid.
     *                      You'll have to check it over. ", new IllegalArgumentException()<br>
     *
     * <br>
     * datePattern、datePatternStrictが両方ともnullの場合、
     * ValidatorExceptionがスローされることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDate04() throws Exception {
        // 前準備
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
            new FieldChecks().validateDate("2005/11/17", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                "- datePattern or datePatternStrict is invalid." +
                " You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new IllegalArgumentException()));
        }
    }

    /**
     * testValidateDate05()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"2005/11/17"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                datePattern="abc"<br>
     *                datePatternStrict=""<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file.
     *                     - datePattern or datePatternStrict is invalid.
     *                     You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file.
     *                     - datePattern or datePatternStrict is invalid.
     *                      You'll have to check it over. ", new IllegalArgumentException()<br>
     *
     * <br>
     * datePatternに日付形式として解釈できない文字が含まれる場合、
     * ValidatorExceptionがスローされることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDate05() throws Exception {
        // 前準備
        Var var1 = new Var();
        var1.setName("datePattern");
        var1.setValue("abc");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("datePatternStrict");
        var2.setValue("");
        field.addVar(var2);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateDate("2005/11/17", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                "- datePattern or datePatternStrict is invalid." +
                " You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new IllegalArgumentException()));
        }
    }

    /**
     * testValidateDate06()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"2005/11/17"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                datePattern=null<br>
     *                datePatternStrict="abc"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file.
     *                     - datePattern or datePatternStrict is invalid.
     *                     You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file.
     *                     - datePattern or datePatternStrict is invalid.
     *                      You'll have to check it over. ", new IllegalArgumentException()<br>
     *
     * <br>
     * datePatternStrictに日付形式として解釈できない文字が含まれる場合、
     * ValidatorExceptionがスローされることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDate06() throws Exception {
        // 前準備
        Var var = new Var();
        var.setName("datePatternStrict");
        var.setValue("abc");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateDate("2005/11/17", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                "- datePattern or datePatternStrict is invalid." +
                " You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new IllegalArgumentException()));
        }
    }

    /**
     * testValidateDate07()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"2005/1/1"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                datePattern="yyyy/MM/dd"<br>
     *                datePatternStrict="yyyy.MM.dd"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * datePattern、datePatternStrictの両方に正しい日付形式が指定される場合、
     * datePatternの形式で入力値の解釈が行われることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDate07() throws Exception {
        // 前準備
        Var var1 = new Var();
        var1.setName("datePattern");
        var1.setValue("yyyy/MM/dd");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("datePatternStrict");
        var2.setValue("yyyy.MM.dd");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateDate("2005/1/1", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateDate08()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"2005/1/1"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                datePatternStrict="yyyy/MM/dd"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * datePatternStrictに日付形式が指定されており、形式が完全に一致しない場合、エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDate08() throws Exception {
        // 前準備
        Var var = new Var();
        var.setName("datePatternStrict");
        var.setValue("yyyy/MM/dd");
        field.addVar(var);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateDate("2005/1/1", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("2005/1/1", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateDate09()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"2005/2/29"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                datePattern="yyyy/MM/dd"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * 存在しない日付が入力された場合、エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateDate09() throws Exception {
        // 前準備
        Var var = new Var();
        var.setName("datePattern");
        var.setValue("yyyy/MM/dd");
        field.addVar(var);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateDate("2005/2/29", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("2005/2/29", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

}
