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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Method;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
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
public class FieldChecksTest01 {

    /**
     * テスト用インスタンス。
     */
    private ValidatorAction va = null;

    /**
     * テスト用インスタンス。
     */
    private Field field = null;

    /**
     * 初期化処理を行う。
     */
    @Before
    public void setUp() {
        va = new ValidatorAction();
        field = new Field();
    }

    /**
     * testGetParamClass01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) va:not null<br>
     *         (状態) va.methodParams:""<br>
     *
     * <br>
     * 期待値：(戻り値) Class[]:Class[]{}<br>
     *                  要素数0<br>
     *
     * <br>
     * vaのmethodParamsが空文字のとき、
     * 要素数0のClass型配列が取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetParamClass01() throws Exception {
        // 前処理
        va.setMethodParams("");

        // テスト実施
        Class[] result = new FieldChecks().getParamClass(va);

        // 判定
        assertEquals(0, result.length);
    }

    /**
     * testGetParamClass02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) va:not null<br>
     *         (状態) va.methodParams:
     *                "java.lang.String,java.lang.Integer,java.lang.Boolean"<br>
     *
     * <br>
     * 期待値：(戻り値) Class[]:{String.class,<br>
     *                   Integer.class,<br>
     *                   Boolean.class}<br>
     *
     * <br>
     * vaのmethodParamsに設定されている、
     * カンマ区切りのクラス名のクラスインスタンスが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetParamClass02() throws Exception {
        // 前処理
        va.setMethodParams(
                "java.lang.String,java.lang.Integer,java.lang.Boolean");

        // テスト実施
        Class[] result = new FieldChecks().getParamClass(va);

        // 判定
        assertEquals(3, result.length);
        assertSame(String.class, result[0]);
        assertSame(Integer.class, result[1]);
        assertSame(Boolean.class, result[2]);
    }

    /**
     * testGetParamClass03()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) va:not null<br>
     *         (状態) va.methodParams:"java.lang.String,bbb"<br>
     *
     * <br>
     * 期待値：(戻り値) Class[]:null<br>
     *
     * <br>
     * vaのmethodParamsにクラスパスに存在しないクラス名が含まれている場合、
     * nullが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetParamClass03() throws Exception {
        // 前処理
        va.setMethodParams("java.lang.String,bbb");

        // テスト実施
        // 判定
        assertNull(new FieldChecks().getParamClass(va));

    }

    /**
     * testGetMethod01()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：C,F
     * <br><br>
     * 入力値：(引数) va:not null<br>
     *         (状態) va.getName:null<br>
     *
     * <br>
     * 期待値：(戻り値) Method:null<br>
     *
     * <br>
     * vaから取得したメソッド名がnullの場合、nullが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetMethod01() throws Exception {
        // 前処理
        va.setName(null);

        // テスト実施
        // 判定
        assertNull(new FieldChecks().getMethod(va, new Class[]{}));
    }

    /**
     * testGetMethod02()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：C,F
     * <br><br>
     * 入力値：(引数) va:not null<br>
     *         (状態) va.getName:空文字<br>
     *
     * <br>
     * 期待値：(戻り値) Method:null<br>
     *
     * <br>
     * vaから取得したメソッド名が空文字の場合、nullが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetMethod02() throws Exception {
        // 前処理
        va.setName("");

        // テスト実施
        // 判定
        assertNull(new FieldChecks().getMethod(va, new Class[]{}));
    }

    /**
     * testGetMethod03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) va:not null<br>
     *         (引数) paramClass:{Object.class,<br>
     *                 ValidatorAction.class,<br>
     *                 Field.class,<br>
     *                 ValidationErrors.class}<br>
     *         (状態) va.getName:"requiredArray"<br>
     *
     * <br>
     * 期待値：(戻り値) Method:new FieldChecks()#validateRequired<br>
     *
     * <br>
     * vaから取得したメソッドの最後の5文字を除き、
     * 先頭にvalidateを付与したメソッドが存在する場合、
     * そのメソッドのインスタンスが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetMethod03() throws Exception {
        // 前処理
        va.setName("requiredArray");
        Class[] paramClass = {
            Object.class,
            ValidatorAction.class,
            Field.class,
            ValidationErrors.class
        };

        // テスト実施
        Method result = new FieldChecks().getMethod(va,paramClass);

        // 判定
        assertEquals(FieldChecks.class, result.getDeclaringClass());
        assertEquals("validateRequired", result.getName());
    }

    /**
     * testGetMethod04()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) va:not null<br>
     *         (引数) paramClass:{Object.class,<br>
     *                 ValidatorAction.class,<br>
     *                 Field.class}<br>
     *         (状態) va.getName:"requiredArray"<br>
     *
     * <br>
     * 期待値：(戻り値) Method:null<br>
     *
     * <br>
     * vaから取得したメソッドの最後の5文字を除き、
     * 先頭にvalidateを付与したメソッドが存在するが、
     * 引数のパターンがparamClassと一致しない場合、
     * nullが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetMethod04() throws Exception {
        // 前処理
        va.setName("requiredArray");
        Class[] paramClass = {
            Object.class,
            ValidatorAction.class,
            Field.class
        };

        // テスト実施
        // 判定
        assertNull(new FieldChecks().getMethod(va,paramClass));
    }

    /**
     * testGetMethod05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) va:not null<br>
     *         (引数) paramClass:{Object.class,<br>
     *                 ValidatorAction.class,<br>
     *                 Field.class,<br>
     *                 ValidationErrors.class}<br>
     *         (状態) va.getName:"requiredXXXXX"<br>
     *
     * <br>
     * 期待値：(戻り値) Method:new FieldChecks()#validateRequired<br>
     *
     * <br>
     * vaから取得したメソッドの最後の5文字を除き、
     * 先頭にvalidateを付与したメソッドが存在する場合、
     * そのメソッドのインスタンスが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetMethod05() throws Exception {
        // 前処理
        va.setName("requiredXXXXX");
        Class[] paramClass = {
            Object.class,
            ValidatorAction.class,
            Field.class,
            ValidationErrors.class
        };

        // テスト実施
        Method result = new FieldChecks().getMethod(va,paramClass);

        // 判定
        assertEquals(FieldChecks.class, result.getDeclaringClass());
        assertEquals("validateRequired", result.getName());
    }

    /**
     * testExtractValue01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C,F
     * <br><br>
     * 入力値：(引数) bean:null<br>
     *
     * <br>
     * 期待値：(戻り値) String:null<br>
     *
     * <br>
     * 引数のbeanがnullの場合、nullが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractValue01() throws Exception {
        // テスト実施
        // 判定
        assertNull(new FieldChecks().extractValue(null, field));
    }

    /**
     * testExtractValue02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *
     * <br>
     * 期待値：(戻り値) String:"test"<br>
     *
     * <br>
     * 引数のbeanがString型の場合、beanの値が返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractValue02() throws Exception {
        // テスト実施
        // 判定
        assertEquals("test", new FieldChecks().extractValue("test", field));
    }

    /**
     * testExtractValue03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:JavaBean {<br>
     *                   field="testProperty"<br>
     *                }<br>
     *         (引数) field:property="field"<br>
     *
     * <br>
     * 期待値：(戻り値) String:"testProperty"<br>
     *
     * <br>
     * beanに、fieldのproperty属性値のフィールドが存在する場合、
     * その値が取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractValue03() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean
            = new FieldChecks_JavaBeanStub01();
        field.setProperty("field");

        // テスト実施
        // 判定
        assertEquals("testProperty", new FieldChecks().extractValue(bean, field));
    }

    /**
     * testExtractValue04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:JavaBean {<br>
     *                   field="testProperty"<br>
     *                }<br>
     *         (引数) field:property="field2"<br>
     *
     * <br>
     * 期待値：(戻り値) String:null<br>
     *
     * <br>
     * beanに、fieldのproperty属性値のフィールドが存在しない場合、
     * nullが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractValue04() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean
            = new FieldChecks_JavaBeanStub01();
        field.setProperty("field2");

        // テスト実施
        // 判定
        assertNull(new FieldChecks().extractValue(bean, field));
    }

    /**
     * testExtractValue05()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:new Integer(12345)<br>
     *         
     * <br>
     * 期待値：(戻り値) String:"12345"<br>
     *         
     * <br>
     * 引数のbeanがNumber型の場合、beanの値が返却されることを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractValue05() throws Exception {
        // テスト実施
        // 判定
        assertEquals("12345", new FieldChecks().extractValue(new Integer(12345), field));
    }

    /**
     * testExtractValue06()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:new Boolean(true)<br>
     *         
     * <br>
     * 期待値：(戻り値) String:"true"<br>
     *         
     * <br>
     * 引数のbeanがBoolean型の場合、beanの値が返却されることを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractValue06() throws Exception {
        // テスト実施
        // 判定
        assertEquals("true", new FieldChecks().extractValue(new Boolean(true), field)); 
    }

    /**
     * testExtractValue07()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) bean:new Character('@')<br>
     *         
     * <br>
     * 期待値：(戻り値) String:"@"<br>
     *         
     * <br>
     * 引数のbeanがCharacter型の場合、beanの値が返却されることを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractValue07() throws Exception {
        // テスト実施
        // 判定
        assertEquals("@", new FieldChecks().extractValue(new Character('@'), field)); 
    }

    /**
     * testRejectValue01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) errors:not null<br>
     *         (引数) field:not null<br>
     *         (引数) va:not null<br>
     *         (引数) bean:not null<br>
     *
     * <br>
     * 期待値：(状態変化) errors:bean,field,vaを引数として
     * addErrorsが呼び出される。<br>
     *
     * <br>
     * bean,field,vaを引数としてaddErrorsが呼び出されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRejectValue01() throws Exception {
        // 前処理
        FieldChecks_JavaBeanStub01 bean = new FieldChecks_JavaBeanStub01();
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();

        // テスト実施
        new FieldChecks().rejectValue(errors, field, va, bean);

        // 判定
        assertEquals(1, errors.addErrorCount);
        assertSame(field, errors.fieldList.get(0));
        assertSame(va, errors.vaList.get(0));
        assertSame(bean, errors.beanList.get(0));
    }

}
