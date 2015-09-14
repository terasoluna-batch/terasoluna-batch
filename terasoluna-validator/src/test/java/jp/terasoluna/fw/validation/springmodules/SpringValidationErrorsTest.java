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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.FastHashMap;
import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Msg;
import org.apache.commons.validator.ValidatorAction;
import org.junit.Test;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.Errors;

/**
 * {@link jp.terasoluna.fw.validation.springmodules.SpringValidationErrors} クラスのブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4> Springフレームワークのorg.springframework.validation.Errorsインタフェースに エラー情報を追加するためのクラス。<br>
 * ※addErrorの引数FieldとValidatorActionにnullは入らない。
 * <p>
 * @see jp.terasoluna.fw.validation.springmodules.SpringValidationErrors
 */
public class SpringValidationErrorsTest {

    /**
     * testSetErrors01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) errors:Errorsインスタンス<br>
     * (状態) errors:null<br>
     * <br>
     * 期待値：(状態変化) errors:引数と同一のErrorsインスタンス<br>
     * <br>
     * 引数の値が属性に正常に設定されること <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetErrors01() throws Exception {
        // 前処理
        SpringValidationErrors validation = new SpringValidationErrors();
        ReflectionTestUtils.setField(validation, "errors", null);

        Errors errors = new ErrorsImpl01();

        // テスト実施
        validation.setErrors(errors);

        // 判定
        assertSame(errors, ReflectionTestUtils.getField(validation, "errors"));
    }

    /**
     * testGetErrors01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) なし:－<br>
     * (状態) errors:Errorsインスタンス<br>
     * <br>
     * 期待値：(戻り値) Errors:Errorsインスタンス<br>
     * <br>
     * 属性に設定されている値を正常に取得すること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetErrors01() throws Exception {
        // 前処理
        SpringValidationErrors validation = new SpringValidationErrors();
        Errors errors = new ErrorsImpl01();
        ReflectionTestUtils.setField(validation, "errors", errors);

        // テスト実施
        Errors result = validation.getErrors();

        // 判定
        assertSame(errors, result);
    }

    /**
     * testAddErrors01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) bean:Object<br>
     * (引数) field:Fieldインスタンス<br>
     * field.getKey()="key"<br>
     * field.getMsg("name")="messageKey"<br>
     * field.getArg("name", 0)="arg0"<br>
     * field.getArg("name", 1)="arg1"<br>
     * field.getArg("name", 2)="arg2"<br>
     * field.getArg("name", 3)="arg3"<br>
     * (引数) va:ValidationActionインスタンス<br>
     * va.getName()="name"<br>
     * <br>
     * 期待値：(状態変化) rejectValue():呼び出し確認と引数の確認：<br>
     * fieldCode="key"<br>
     * errorCode="messageKey"<br>
     * args={<br>
     * MessageSourceResolvableインスタンス{<br>
     * codes[0]={"arg0"}, arguments=null, defaultMessage="arg0"}, <br>
     * MessageSourceResolvableインスタンス{<br>
     * codes[1]={"arg1"}, arguments=null, defaultMessage="arg1"}, <br>
     * MessageSourceResolvableインスタンス{<br>
     * codes[2]={"arg2"}, arguments=null, defaultMessage="arg2"}, <br>
     * MessageSourceResolvableインスタンス{<br>
     * codes[3]={"arg3"}, arguments=null, defaultMessage="arg3"}, <br>
     * }<br>
     * <br>
     * 引数がnot nullの場合 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAddErrors01() throws Exception {
        // 前処理
        // 引数bean
        Object bean = new Object();
        // 引数field
        Field field = new Field();
        // field.getKey() : "key"
        field.setKey("key");

        // field.getMsg("name")の値を設定（errorCode取得のため）
        FastHashMap hMsgs = new FastHashMap();
        Msg msg = new Msg();
        msg.setKey("messageKey");
        hMsgs.put("name", msg);
        ReflectionTestUtils.setField(field, "hMsgs", hMsgs);

        // （Object[] args取得のため）
        @SuppressWarnings("rawtypes")
        Map[] args = new HashMap[4];

        // args[0]
        Arg arg = new Arg();
        arg.setKey("arg0");
        Map<String, Arg> hMap01 = new HashMap<String, Arg>();
        hMap01.put("name", arg);
        args[0] = hMap01;

        // args[1]
        arg = new Arg();
        arg.setKey("arg1");
        Map<String, Arg> hMap02 = new HashMap<String, Arg>();
        hMap02.put("name", arg);
        args[1] = hMap02;

        // args[2]
        arg = new Arg();
        arg.setKey("arg2");
        Map<String, Arg> hMap03 = new HashMap<String, Arg>();
        hMap03.put("name", arg);
        args[2] = hMap03;

        // args[3]
        arg = new Arg();
        arg.setKey("arg3");
        Map<String, Arg> hMap04 = new HashMap<String, Arg>();
        hMap04.put("name", arg);
        args[3] = hMap04;

        ReflectionTestUtils.setField(field, "args", args);

        // 引数va
        ValidatorAction va = new ValidatorAction();

        // va.getName : "name"
        va.setName("name");

        // SpringValidationErrorsインスタンス生成
        SpringValidationErrors validation = new SpringValidationErrors();

        // Errorsの設定 : ErrorsImpl01 - メソッドrejectValueと引数の呼出確認
        ErrorsImpl01 errors = new ErrorsImpl01();
        ReflectionTestUtils.setField(validation, "errors", errors);

        // テスト実施
        validation.addError(bean, field, va);

        // 判定
        ErrorsImpl01 assertErrors = (ErrorsImpl01) ReflectionTestUtils.getField(
                validation, "errors");
        // rejectValue呼出確認
        assertTrue(assertErrors.isRejectValue);

        // 引数field確認
        assertEquals("key", assertErrors.field);

        // 引数errorCode確認
        assertEquals("messageKey", assertErrors.errorCode);

        // assertSame(args, assertErrors.errorArgs);
        // 引数errorArgs確認
        Object[] objs = assertErrors.errorArgs;
        MessageSourceResolvable msr = null;
        for (int i = 0; i < objs.length; i++) {
            msr = (MessageSourceResolvable) objs[i];

            String[] strs = msr.getCodes();
            // codes[0] : "arg" + i
            assertEquals("arg" + i, strs[0]);
            // arguments : null
            assertNull(msr.getArguments());
            // defaultMessage : "arg" + i
            assertEquals("arg" + i, msr.getDefaultMessage());
        }

        // 引数defaultMessage確認
        assertEquals("messageKey", assertErrors.defaultMessage);
    }
}
