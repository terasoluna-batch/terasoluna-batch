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
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.Var;

import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;
import static java.util.Arrays.asList;

/**
 * {@link jp.terasoluna.fw.validation.FieldChecks} クラスのブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4> TERASOLUNAの入力チェック機能で共通に使用される検証ルールクラス。
 * <p>
 * @see jp.terasoluna.fw.validation.FieldChecks
 */
public class FieldChecksTest07 extends TestCase {

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            FieldChecks.class);

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(FieldChecksTest07.class);
    }

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 終了処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        logger.clear();
        super.tearDown();
    }

    /**
     * コンストラクタ。
     * @param name このテストケースの名前。
     */
    public FieldChecksTest07(String name) {
        super(name);
    }

    /**
     * testValidateHankakuKanaString01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
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
    public void testValidateHankakuKanaString01() throws Exception {
        // 前処理
        // bean : null
        Object bean = null;
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateHankakuKanaString(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateHankakuKanaString02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
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
    public void testValidateHankakuKanaString02() throws Exception {
        // 前処理
        // bean : ""
        Object bean = "";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateHankakuKanaString(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateHankakuKanaString03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:"ﾊﾝｶｸ"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * 引数のbeanが半角カナ文字のみで構成されている場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateHankakuKanaString03() throws Exception {
        // 前処理
        // bean : "ﾊﾝｶｸ"
        Object bean = "ﾊﾝｶｸ";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateHankakuKanaString(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateHankakuKanaString04() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:"ハンカク"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * 引数のbeanに半角カナ以外の文字が含まれている場合、エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateHankakuKanaString04() throws Exception {
        // 前処理
        // bean : "ハンカク"
        Object bean = "ハンカク";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateHankakuKanaString(bean, va, field,
                errors);

        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors,
                "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors,
                "fieldList");
        assertSame(field, fieldList.get(0));
    }

    /**
     * testValidateHankakuString01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
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
    public void testValidateHankakuString01() throws Exception {
        // 前処理
        // bean : null
        Object bean = null;
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateHankakuString(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateHankakuString02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
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
    public void testValidateHankakuString02() throws Exception {
        // 前処理
        // bean : ""
        Object bean = "";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateHankakuString(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateHankakuString03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:"1aｱ"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * 引数のbeanが半角文字のみで構成されている場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateHankakuString03() throws Exception {
        // 前処理
        // bean : "1aｱ"
        Object bean = "1aｱ";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateHankakuString(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateHankakuString04() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:"全角ア"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * 引数のbeanに半角以外の文字が含まれている場合、エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateHankakuString04() throws Exception {
        // 前処理
        // bean : "全角ア"
        Object bean = "全角ア";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateHankakuString(bean, va, field,
                errors);

        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors,
                "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors,
                "fieldList");
        assertSame(field, fieldList.get(0));
    }

    /**
     * testValidateZenkakuString01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
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
    public void testValidateZenkakuString01() throws Exception {
        // 前処理
        // bean : null
        Object bean = null;
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateZenkakuString(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateZenkakuString02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
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
    public void testValidateZenkakuString02() throws Exception {
        // 前処理
        // bean : ""
        Object bean = "";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateZenkakuString(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateZenkakuString03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:"全角ア"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * 引数のbeanが全角文字のみで構成されている場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateZenkakuString03() throws Exception {
        // 前処理
        // bean : "全角ア"
        Object bean = "全角ア";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateZenkakuString(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateZenkakuString04() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:"1aｱ"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * 引数のbeanに全角以外の文字が含まれている場合、エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateZenkakuString04() throws Exception {
        // 前処理
        // bean : "1aｱ"
        Object bean = "1aｱ";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateZenkakuString(bean, va, field,
                errors);

        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors,
                "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors,
                "fieldList");
        assertSame(field, fieldList.get(0));
    }

    /**
     * testValidateZenkakuKanaString01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
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
    public void testValidateZenkakuKanaString01() throws Exception {
        // 前処理
        // bean : null
        Object bean = null;
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateZenkakuKanaString(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateZenkakuKanaString02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
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
    public void testValidateZenkakuKanaString02() throws Exception {
        // 前処理
        // bean : ""
        Object bean = "";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateZenkakuKanaString(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateZenkakuKanaString03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:"ゼンカク"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * 引数のbeanが全角カナ文字のみで構成されている場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateZenkakuKanaString03() throws Exception {
        // 前処理
        // bean : "ゼンカク"
        Object bean = "ゼンカク";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateZenkakuKanaString(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateZenkakuKanaString04() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:"1aあ"<br>
     * (引数) va:not null<br>
     * (引数) field:not null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * 引数のbeanに全角カナ以外の文字が含まれている場合、エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateZenkakuKanaString04() throws Exception {
        // 前処理
        // bean : "1aあ"
        Object bean = "1aあ";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateZenkakuKanaString(bean, va, field,
                errors);

        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors,
                "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors,
                "fieldList");
        assertSame(field, fieldList.get(0));
    }

    /**
     * testValidateProhibited01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
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
    public void testValidateProhibited01() throws Exception {
        // 前処理
        // bean : null
        Object bean = null;
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateProhibited(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateProhibited02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
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
    public void testValidateProhibited02() throws Exception {
        // 前処理
        // bean : ""
        Object bean = "";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateProhibited(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateProhibited03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:"test"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * chars=null<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ："var[chars] must be specified."<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ："var[chars] must be specified."<br>
     * <br>
     * varのcharsがnullの場合、ValidatorExceptionがスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateProhibited03() throws Exception {
        // 前処理
        // bean : "test"
        Object bean = "test";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        Var var = new Var();
        var.setName("chars");
        var.setValue(null);
        field.addVar(var);

        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        try {
            // テスト実施
            @SuppressWarnings("unused")
            boolean b = new FieldChecks().validateProhibited(bean, va, field,
                    errors);
            fail();
        } catch (ValidatorException e) {
            // 判定
            String message = "var[chars] must be specified.";
            assertEquals(message, e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(message))));
        }
    }

    /**
     * testValidateProhibited04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:"test"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * chars=""<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     * メッセージ："var[chars] must be specified."<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * メッセージ："var[chars] must be specified."<br>
     * <br>
     * varのcharsがnullの場合、ValidatorExceptionがスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateProhibited04() throws Exception {
        // 前処理
        // bean : "test"
        Object bean = "test";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        Var var = new Var();
        var.setName("chars");
        var.setValue("");
        field.addVar(var);

        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        try {
            // テスト実施
            @SuppressWarnings("unused")
            boolean b = new FieldChecks().validateProhibited(bean, va, field,
                    errors);
            fail();
        } catch (ValidatorException e) {
            // 判定
            String message = "var[chars] must be specified.";
            assertEquals(message, e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(message))));
        }
    }

    /**
     * testValidateProhibited05() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:"③②①"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * chars="あ①t"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * beanにvarのcharsで指定された文字が含まれる場合、エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateProhibited05() throws Exception {
        // 前処理
        // bean : "③②①"
        Object bean = "③②①";
        // va : not null
        ValidatorAction va = new ValidatorAction();

        // field : not null
        Field field = new Field();
        Var var = new Var();
        var.setName("chars");
        var.setValue("あ①t");
        field.addVar(var);

        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateProhibited(bean, va, field,
                errors);

        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors,
                "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors,
                "fieldList");
        assertSame(field, fieldList.get(0));
    }

    /**
     * testValidateProhibited06() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:"③②①"<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * chars="④⑤⑥"<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) errors:呼び出されない<br>
     * <br>
     * beanにvarのcharsで指定された文字が含まれない場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateProhibited06() throws Exception {
        // 前処理
        // bean : "③②①"
        Object bean = "③②①";
        // va : not null
        ValidatorAction va = new ValidatorAction();

        // field : not null
        Field field = new Field();
        Var var = new Var();
        var.setName("chars");
        var.setValue("④⑤⑥");
        field.addVar(var);

        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateProhibited(bean, va, field,
                errors);

        // 判定
        assertTrue(b);
        // 呼出確認
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateProhibited07() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:"③② "<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * chars="あ①t "<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * beanにvarのcharsで指定された文字が含まれる場合、エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateProhibited07() throws Exception {
        // 前処理
        // bean : "③② "
        Object bean = "③② ";
        // va : not null
        ValidatorAction va = new ValidatorAction();

        // field : not null
        Field field = new Field();
        Var var = new Var();
        var.setName("chars");
        var.setValue("あ①t ");
        field.addVar(var);

        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateProhibited(bean, va, field,
                errors);

        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors,
                "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors,
                "fieldList");
        assertSame(field, fieldList.get(0));
    }

    /**
     * testValidateProhibited08() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:"    "<br>
     * (引数) va:not null<br>
     * (引数) field:var:<br>
     * chars="あ①t "<br>
     * (引数) errors:not null<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     * <br>
     * beanにvarのcharsで指定された文字が含まれる場合、エラーを追加してfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateProhibited08() throws Exception {
        // 前処理
        // bean : " "
        Object bean = "    ";
        // va : not null
        ValidatorAction va = new ValidatorAction();

        // field : not null
        Field field = new Field();
        Var var = new Var();
        var.setName("chars");
        var.setValue("あ①t ");
        field.addVar(var);

        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors = new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        boolean b = new FieldChecks().validateProhibited(bean, va, field,
                errors);

        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors,
                "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors,
                "fieldList");
        assertSame(field, fieldList.get(0));
    }

}
