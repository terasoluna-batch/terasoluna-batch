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

package jp.terasoluna.fw.validation.springmodules;

import java.util.ArrayList;
import java.util.List;

import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorResources;
import org.springframework.validation.Errors;

/**
 * {@link jp.terasoluna.fw.validation.springmodules.DefaultValidatorFactoryEx}
 * クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * FieldChecksクラスにて使用されるエラーインタフェースの実装に
 * SpringフレームワークのErrorsクラスを扱うためのクラス。
 * <p>
 * 
 * @see jp.terasoluna.fw.validation.springmodules.DefaultValidatorFactoryEx
 */
public class DefaultValidatorFactoryExTest extends TestCase {

    /**
     * このテストケースを実行する為の
     * GUI アプリケーションを起動する。
     * 
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(DefaultValidatorFactoryExTest.class);
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
    public DefaultValidatorFactoryExTest(String name) {
        super(name);
    }

    /**
     * testGetValidator01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C, D
     * <br><br>
     * 入力値：(引数) beanName:null<br>
     *         (引数) bean:null<br>
     *         (引数) errors:null<br>
     *         
     * <br>
     * 期待値：(戻り値) Validator:Validatorインスタンス<br>
     *                  validator.getParameterValue("jp.terasoluna.fw.validation.ValidationErrors")=<br>
     *                  SpringValidatorErrors.getErrors()：null<br>
     *                  validator.getParameterValue("java.lang.Object")=null<br>
     *                  validator.getFormName()=null<br>
     *         
     * <br>
     * 引数がnullの場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetValidator01() throws Exception {
        // 前処理
        // beanName : null
        String beanName = null;
        
        // bean : null
        Object bean = null;
        
        // errors : null
        Errors errors = null;

        DefaultValidatorFactoryEx factory = new DefaultValidatorFactoryEx();
        
        // DefaultValidatorFactoryのvalidatorResourcesを設定する。
        ValidatorResources resources = new ValidatorResources();
        UTUtil.setPrivateField(factory, "validatorResources", resources);
        
        Validator validator = null;
        
        // テスト実施
        validator = factory.getValidator(beanName, bean, errors);

        // 判定
        // errors : null
        SpringValidationErrors resultErorrs = 
            (SpringValidationErrors) validator.getParameterValue(
                    "jp.terasoluna.fw.validation.ValidationErrors");
        assertNull(resultErorrs.getErrors());
        
        // bean : null
        assertNull(validator.getParameterValue("java.lang.Object"));
        
        // beanName : null
        assertNull(validator.getFormName());
    }

    /**
     * testGetValidator02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C, D
     * <br><br>
     * 入力値：(引数) beanName:""<br>
     *         (引数) bean:String("bean")<br>
     *         (引数) errors:空のErrorsインスタンス<br>
     *         
     * <br>
     * 期待値：(戻り値) Validator:Validatorインスタンス<br>
     *                  validator.getParameterValue
     *                  ("jp.terasoluna.fw.validation.ValidationErrors")=<br>
     *                  SpringValidatorErrors.getErrors()：空のErrorsインスタンス<br>
     *                  validator.getParameterValue("java.lang.Object")=String("bean")<br>
     *                  validator.getFormName()=""<br>
     *         
     * <br>
     * 引数beanNameが空文字であり、beanがnot nullであり、errorsが空のErrorsインスタンスの場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetValidator02() throws Exception {
        // 前処理
        // beanName : ""
        String beanName = "";
        
        // bean : String("bean")
        Object bean = "bean";
        
        // errors : 空のErrorsインスタンス
        Errors errors = new ErrorsImpl01();

        DefaultValidatorFactoryEx factory = new DefaultValidatorFactoryEx();
        
        // DefaultValidatorFactoryのvalidatorResourcesを設定する。
        ValidatorResources resources = new ValidatorResources();
        UTUtil.setPrivateField(factory, "validatorResources", resources);
        
        Validator validator = null;
        
        // テスト実施
        validator = factory.getValidator(beanName, bean, errors);

        // 判定
        // errors : 同じインスタンス
        SpringValidationErrors resultErorrs = 
            (SpringValidationErrors) validator.getParameterValue(
                    "jp.terasoluna.fw.validation.ValidationErrors");
        assertSame(errors, resultErorrs.getErrors());
        
        // bean : new String("bean")
        assertSame(bean, validator.getParameterValue("java.lang.Object"));
        
        // beanName : ""
        assertEquals(beanName, validator.getFormName());
    }

    /**
     * testGetValidator03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C, D
     * <br><br>
     * 入力値：(引数) beanName:"beanName"<br>
     *         (引数) bean:String("bean")<br>
     *         (引数) errors:要素1のErrorsインスタンス<br>
     *                {Object[0]=new Object}<br>
     *         
     * <br>
     * 期待値：(戻り値) Validator:Validatorインスタンス<br>
     *                  validator.getParameterValue
     *                  ("jp.terasoluna.fw.validation.ValidationErrors")=<br>
     *                  SpringValidatorErrors.getErrors()：<br>
     *                  要素1のErrorsインスタンス{Object[0]=new Object}<br>
     *                  validator.getParameterValue("java.lang.Object")=String("bean")<br>
     *                  validator.getFormName()="beanName"<br>
     *         
     * <br>
     * 引数beanNameが文字列であり、beanがnot nullであり、errorsが要素数1のErrorsインスタンスの場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetValidator03() throws Exception {
        // 前処理
        // beanName : "beanName"
        String beanName = "beanName";
        
        // bean : String("bean")
        Object bean = "bean";
        
        // errors : 要素1のErrorsインスタンス
        Errors errors = new ErrorsImpl01();
        List<Object> list = new ArrayList<Object>();
        list.add(0, new Object());
        UTUtil.setPrivateField(errors, "errors", list);

        DefaultValidatorFactoryEx factory = new DefaultValidatorFactoryEx();
        
        // DefaultValidatorFactoryのvalidatorResourcesを設定する。
        ValidatorResources resources = new ValidatorResources();
        UTUtil.setPrivateField(factory, "validatorResources", resources);
        
        Validator validator = null;
        
        // テスト実施
        validator = factory.getValidator(beanName, bean, errors);

        // 判定
        // errors : 同じインスタンス
        SpringValidationErrors resultErorrs = 
            (SpringValidationErrors) validator.getParameterValue(
                    "jp.terasoluna.fw.validation.ValidationErrors");
        assertSame(errors, resultErorrs.getErrors());

        // bean : new String("bean")
        assertSame(bean, validator.getParameterValue("java.lang.Object"));
        
        // beanName : "beanName"
        assertEquals(beanName, validator.getFormName());
    }

    /**
     * testGetValidator04()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C, D
     * <br><br>
     * 入力値：(引数) beanName:"beanName"<br>
     *         (引数) bean:String("bean")<br>
     *         (引数) errors:要素3のErrorsインスタンス<br>
     *                {Object[0]=new Object,<br>
     *                Object[1]=new Object,<br>
     *                Object[2]=new Object}<br>
     *         
     * <br>
     * 期待値：(戻り値) Validator:Validatorインスタンス<br>
     *                  validator.getParameterValue
     *                  ("jp.terasoluna.fw.validation.ValidationErrors")=<br>
     *                  SpringValidatorErrors.getErrors()：要素1のErrorsインスタンス<br>
     *                  {Object[0]=new Object,Object[1]=new Object,Object[2]=new Object}<br>
     *                  validator.getParameterValue("java.lang.Object")=String("bean")<br>
     *                  validator.getFormName()="beanName"<br>
     *         
     * <br>
     * 引数beanNameが文字列であり、beanがnot nullであり、errorsが要素数3のErrorsインスタンスの場合
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetValidator04() throws Exception {
        // 前処理
        // beanName : "beanName"
        String beanName = "beanName";
        
        // bean : String("bean")
        Object bean = "bean";
        
        // errors : 要素3のErrorsインスタンス
        Errors errors = new ErrorsImpl01();
        List<Object> list = new ArrayList<Object>();
        list.add(0, new Object());
        list.add(1, new Object());
        list.add(2, new Object());
        
        UTUtil.setPrivateField(errors, "errors", list);

        DefaultValidatorFactoryEx factory = new DefaultValidatorFactoryEx();
        
        // DefaultValidatorFactoryのvalidatorResourcesを設定する。
        ValidatorResources resources = new ValidatorResources();
        UTUtil.setPrivateField(factory, "validatorResources", resources);
        
        Validator validator = null;
        
        // テスト実施
        validator = factory.getValidator(beanName, bean, errors);

        // 判定
        // errors : 同じインスタンス
        SpringValidationErrors resultErorrs = 
            (SpringValidationErrors) validator.getParameterValue(
                    "jp.terasoluna.fw.validation.ValidationErrors");
        assertSame(errors, resultErorrs.getErrors());

        // bean : new String("bean")
        assertSame(bean, validator.getParameterValue("java.lang.Object"));
        
        // beanName : "beanName"
        assertEquals(beanName, validator.getFormName());
    }
}