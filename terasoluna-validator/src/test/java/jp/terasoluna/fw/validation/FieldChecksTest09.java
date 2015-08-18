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
public class FieldChecksTest09 {

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
     * testValidateUrl01()
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
    public void testValidateUrl01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateUrl(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateUrl02()
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
    public void testValidateUrl02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateUrl("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateUrl03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:abc://terasoluna.com/index.html#fragment<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                allowallschemes=true<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのallowallschemesがtrueのとき、beanのスキーマ名が不正な場合でもtrueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateUrl03() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("allowallschemes");
        var.setValue("true");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateUrl(
            "abc://terasoluna.com/index.html#fragment", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateUrl04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:abc://terasoluna.com/index.html<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                allowallschemes=null<br>
     *                schemes=http,ftp<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * varのallowallschemesがnullで、beanのスキーマ名がvarのschemesに指定されたスキーマと一致しない場合、エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateUrl04() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("schemes");
        var.setValue("http,ftp");
        field.addVar(var);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateUrl(
            "abc://terasoluna.com/index.html", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("abc://terasoluna.com/index.html", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateUrl05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:abc://terasoluna.com/index.html<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                allowallschemes=""<br>
     *                schemes=http,ftp<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * varのallowallschemesが空文字で、
     * beanのスキーマ名がvarのschemesに指定されたスキーマと一致しない場合、
     * エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateUrl05() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("allowallschemes");
        var1.setValue("");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("schemes");
        var2.setValue("http,ftp");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateUrl(
            "abc://terasoluna.com/index.html", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("abc://terasoluna.com/index.html", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateUrl06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:abc://terasoluna.com/index.html<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                allowallschemes="test"<br>
     *                schemes=http,ftp<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * varのallowallschemesがtrue以外の文字列で、
     * beanのスキーマ名がvarのschemesに指定されたスキーマと一致しない場合、
     * エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateUrl06() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("allowallschemes");
        var1.setValue("test");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("schemes");
        var2.setValue("http,ftp");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateUrl(
            "abc://terasoluna.com/index.html", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("abc://terasoluna.com/index.html", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateUrl07()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:abc://terasoluna.com/index.html<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                allowallschemes="test"<br>
     *                schemes=http,ftp,abc<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのallowallschemesがtrue以外の文字列で、
     * beanのスキーマ名がvarのschemesに指定されたスキーマと一致する場合、
     * trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateUrl07() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("allowallschemes");
        var1.setValue("test");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("schemes");
        var2.setValue("http,ftp,abc");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateUrl(
            "abc://terasoluna.com/index.html", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateUrl08()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:abc://terasoluna.com//index.html<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                allowallschemes="true"<br>
     *                allow2slashes="true"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのallow2slashesがtrueのとき、beanのurlの区切りり文字に「//」が含まれていてもtrueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateUrl08() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("allowallschemes");
        var1.setValue("true");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("allow2slashes");
        var2.setValue("true");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateUrl(
            "abc://terasoluna.com//index.html", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateUrl09()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:abc://terasoluna.com//index.html<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                allowallschemes="true"<br>
     *                allow2slashes=null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * varのallow2slashesがnullで、
     * beanのurlの区切りり文字に「//」が含まれている場合、
     * エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateUrl09() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("allowallschemes");
        var.setValue("true");
        field.addVar(var);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateUrl(
            "abc://terasoluna.com//index.html", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("abc://terasoluna.com//index.html", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateUrl10()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:abc://terasoluna.com//index.html<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                allowallschemes="true"<br>
     *                allow2slashes=""<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * varのallow2slashesが空文字で、
     * beanのurlの区切りり文字に「//」が含まれている場合、
     * エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateUrl10() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("allowallschemes");
        var1.setValue("true");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("allow2slashes");
        var2.setValue("");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateUrl(
            "abc://terasoluna.com//index.html", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("abc://terasoluna.com//index.html", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateUrl11()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:abc://terasoluna.com//index.html<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                allowallschemes="true"<br>
     *                allow2slashes="test"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * varのallow2slashesがtrue以外の文字列で、
     * beanのurlの区切りり文字に「//」が含まれている場合、
     * エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateUrl11() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("allowallschemes");
        var1.setValue("true");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("allow2slashes");
        var2.setValue("test");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateUrl(
            "abc://terasoluna.com//index.html", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("abc://terasoluna.com//index.html", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateUrl12()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:abc://terasoluna.com/index.html#fragment<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                allowallschemes="true"<br>
     *                nofragments="true"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * varのnofragmentsがtrueで、beanのurlにフラグメントが存在する場合、
     * エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateUrl12() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("allowallschemes");
        var1.setValue("true");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("nofragments");
        var2.setValue("true");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateUrl(
            "abc://terasoluna.com/index.html#fragment", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("abc://terasoluna.com/index.html#fragment",
                errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateUrl13()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:abc://terasoluna.com/index.html#fragment<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                allowallschemes="true"<br>
     *                nofragments=null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのnofragmentsがnullで、beanのurlにフラグメントが存在する場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateUrl13() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("allowallschemes");
        var.setValue("true");
        field.addVar(var);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateUrl(
            "abc://terasoluna.com/index.html#fragment", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateUrl14()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:abc://terasoluna.com/index.html#fragment<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                allowallschemes="true"<br>
     *                nofragments=""<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのnofragmentsが空文字で、beanのurlにフラグメントが存在する場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateUrl14() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("allowallschemes");
        var1.setValue("true");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("nofragments");
        var2.setValue("");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateUrl(
            "abc://terasoluna.com/index.html#fragment", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateUrl15()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:abc://terasoluna.com/index.html#fragment<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                allowallschemes="true"<br>
     *                nofragments="test"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのnofragmentsがtrue以外の文字列で、
     * beanのurlにフラグメントが存在する場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateUrl15() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("allowallschemes");
        var1.setValue("true");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("nofragments");
        var2.setValue("test");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateUrl(
            "abc://terasoluna.com/index.html#fragment", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateByteRange01()
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
    public void testValidateByteRange01() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateByteRange(null, va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateByteRange02()
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
    public void testValidateByteRange02() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateByteRange("", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateByteRange03()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                minByteLength="test"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - minByteLength is not number.
     *                    You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - minByteLength is not number.
     *                    You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのmaxByteLengthの値が数値に変換できない場合、
     * ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateByteRange03() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("minByteLength");
        var.setValue("test");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateByteRange("test", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                    "- minByteLength is not number. " +
                    "You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateByteRange04()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                maxByteLength="test"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - maxByteLength is not number.
     *                    You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file.
     *                    - maxByteLength is not number.
     *                    You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのmaxByteLengthの値が数値に変換できない場合、
     * ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateByteRange04() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("maxByteLength");
        var.setValue("test");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateByteRange("test", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "Mistake on validation definition file. " +
                    "- maxByteLength is not number. " +
                    "You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateByteRange05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *                minByteLength=null<br>
     *                maxByteLength=null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのminByteLength、maxByteLengthがnullの場合、
     * 範囲が0～Integer.MAX_LENGTHとしてチェックが行われることを確認する。<br>
     * ※境界値テストは物理的に不可能なため省略する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateByteRange05() throws Exception {
        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateByteRange("test", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateByteRange06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *                minByteLength=""<br>
     *                maxByteLength=""<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのminByteLength、maxByteLengthが空文字の場合、
     * 範囲が0～Integer.MAX_LENGTHとしてチェックが行われることを確認する。<br>
     * ※境界値テストは物理的に不可能なため省略する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateByteRange06() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("minByteLength");
        var1.setValue("");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("minByteLength");
        var2.setValue("");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateByteRange("test", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateByteRange07()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                encoding="test"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："encoding[test] is not supported."<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："encoding[test] is not supported."<br>
     *
     * <br>
     * varのencodingがサポートされないエンコーディングの場合、
     * ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateByteRange07() throws Exception {
        // 前処理
        Var var = new Var();
        var.setName("encoding");
        var.setValue("test");
        field.addVar(var);

        // テスト実施
        // 判定
        try {
            new FieldChecks().validateByteRange("test", va, field, errors);
            fail();
        } catch (ValidatorException e) {
            String message = "encoding[test] is not supported.";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message));
        }
    }

    /**
     * testValidateByteRange08()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"あいう"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                minByteLength="6"<br>
     *                maxByteLength="6"<br>
     *                encoding=null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのencodingがnullの場合、
     * デフォルトのエンコーディングでエンコードが行われることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateByteRange08() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("minByteLength");
        var1.setValue("6");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("maxByteLength");
        var2.setValue("6");
        field.addVar(var2);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateByteRange("あいう", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateByteRange09()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"あいう"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                minByteLength="6"<br>
     *                maxByteLength="6"<br>
     *                encoding=""<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのencodingが空文字の場合、
     * デフォルトのエンコーディングでエンコードが行われることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateByteRange09() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("minByteLength");
        var1.setValue("6");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("maxByteLength");
        var2.setValue("6");
        field.addVar(var2);
        
        Var var3 = new Var();
        var3.setName("encoding");
        var3.setValue("");
        field.addVar(var3);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateByteRange("あいう", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateByteRange10()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"あいう"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                minByteLength="7"<br>
     *                maxByteLength="10"<br>
     *                encoding="UTF-8"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * beanの値が指定したエンコーディングで範囲内である場合、
     * trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateByteRange10() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("minByteLength");
        var1.setValue("7");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("maxByteLength");
        var2.setValue("10");
        field.addVar(var2);
        
        Var var3 = new Var();
        var3.setName("encoding");
        var3.setValue("UTF-8");
        field.addVar(var3);

        // テスト実施
        // 判定
        assertTrue(new FieldChecks().validateByteRange("あいう", va, field, errors));

        // addErrors確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateByteRange11()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"あいう"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                minByteLength="7"<br>
     *                maxByteLength="10"<br>
     *                encoding="Windows-31J"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * beanの値をvarのencodingでエンコードしたバイト数が
     * minByteLengthの値より小さい場合、
     * エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateByteRange11() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("minByteLength");
        var1.setValue("7");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("maxByteLength");
        var2.setValue("10");
        field.addVar(var2);
        
        Var var3 = new Var();
        var3.setName("encoding");
        var3.setValue("Windows-31J");
        field.addVar(var3);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateByteRange("あいう", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("あいう", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

    /**
     * testValidateByteRange12()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"あいう"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                minByteLength="5"<br>
     *                maxByteLength="8"<br>
     *                encoding="UTF-8"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数として
     *                           addErrorsが呼び出される。<br>
     *
     * <br>
     * beanの値をvarのencodingでエンコードしたバイト数が
     * maxByteLengthの値より大きい場合、
     * エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testValidateByteRange12() throws Exception {
        // 前処理
        Var var1 = new Var();
        var1.setName("minByteLength");
        var1.setValue("5");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("maxByteLength");
        var2.setValue("8");
        field.addVar(var2);
        
        Var var3 = new Var();
        var3.setName("encoding");
        var3.setValue("UTF-8");
        field.addVar(var3);

        // テスト実施
        // 判定
        assertFalse(new FieldChecks().validateByteRange("あいう", va, field, errors));

        // addErrors確認
        assertEquals(1, errors.addErrorCount);
        assertEquals("あいう", errors.beanList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(field, errors.fieldList.get(0));
    }

}
