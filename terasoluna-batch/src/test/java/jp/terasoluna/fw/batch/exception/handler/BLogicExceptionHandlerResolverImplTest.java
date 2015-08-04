/*
 * Copyright (c) 2015 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.exception.handler;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * BLogicExceptionHandlerResolverのテストケースクラス
 */
public class BLogicExceptionHandlerResolverImplTest {

    BLogicExceptionHandlerResolver bLogicExceptionHandlerResolver = new BLogicExceptionHandlerResolverImpl();

    /**
     * resolveExceptionHandlerテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・beansDef/B000013.xmlにExceptionHandler(B000013)の定義が存在する
     * 確認項目
     * ・ExceptionHandlerのインスタンスが生成されること
     * </pre>
     */
    @Test
    public void testResolveExceptionHandler01() {
        // テスト準備
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beansDef/B000013.xml");
        // テスト実施
        ExceptionHandler exceptionHandler = bLogicExceptionHandlerResolver
                .resolveExceptionHandler(applicationContext, "B000013");
        // 結果検証
        assertTrue(exceptionHandler instanceof ExceptionHandler);
        assertTrue(exceptionHandler.toString().startsWith(
                "jp.terasoluna.fw.batch.executor.B000013ExceptionHandlerImpl"));
    }

    /**
     * resolveExceptionHandlerテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・beansDef/B000014.xmlにExceptionHandler(B000014)の定義(先頭小文字)が存在する
     * 確認項目
     * ・ExceptionHandlerのインスタンスが生成されること
     * </pre>
     */
    @Test
    public void testResolveExceptionHandler02() {
        // テスト準備
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beansDef/B000014.xml");
        // テスト実施
        ExceptionHandler exceptionHandler = bLogicExceptionHandlerResolver
                .resolveExceptionHandler(applicationContext, "B000014");
        // 結果検証
        assertTrue(exceptionHandler instanceof ExceptionHandler);
        assertTrue(exceptionHandler.toString().startsWith(
                "jp.terasoluna.fw.batch.executor.B000014ExceptionHandlerImpl"));
    }

    /**
     * resolveExceptionHandlerテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・beansDef/B000013.xmlにExceptionHandler(DEFINE_NOT_EXIST)の定義が存在しない
     * 確認項目
     * ・DefaultExceptionHandlerのインスタンスが生成されること
     * </pre>
     */
    @Test
    public void testResolveExceptionHandler03() {
        // テスト準備
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beansDef/B000013.xml");
        // テスト実施
        // 結果検証
        assertTrue(bLogicExceptionHandlerResolver.resolveExceptionHandler(
                applicationContext, "DEFINE_NOT_EXIST") instanceof DefaultExceptionHandler);
    }

    /**
     * resolveExceptionHandlerテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・ApplicationContextとしてnullが渡されること
     * 確認項目
     * ・NullPointerExceptionがスローされること
     * </pre>
     */
    @Test
    public void testResolveExceptionHandler04() {
        try {
            // テスト実施
            bLogicExceptionHandlerResolver.resolveExceptionHandler(null,
                    "DEFINE_NOT_EXIST");
        } catch (Exception e) {
            // 結果検証
            assertTrue(e instanceof NullPointerException);
        }
    }

    /**
     * resolveExceptionHandlerテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・beansDef/B000015.xmlにExceptionHandlerの定義が存在しない
     * ・beansDef/B000015.xmlにDefaultExceptionHandlerの定義が存在しない
     * 確認項目
     * ・nullが返却されること
     * </pre>
     */
    @Test
    public void testResolveExceptionHandler05() {
        // テスト準備
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beansDef/B000015.xml");
        // テスト実施
        // 結果検証
        assertNull(bLogicExceptionHandlerResolver.resolveExceptionHandler(
                applicationContext, null));
    }

    /**
     * getExceptionHandlerBeanNameテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・引数にJobAppCdが渡されること
     * 確認項目
     * ・JobAppCd + "ExceptionHandler" が返却されること
     * </pre>
     */
    @Test
    public void testGetExceptionHandlerBeanName01() {
        // テスト実施
        // 結果検証
        assertEquals(
                "B0000001" + "ExceptionHandler",
                ((BLogicExceptionHandlerResolverImpl) bLogicExceptionHandlerResolver)
                        .getExceptionHandlerBeanName("B0000001"));
    }

    /**
     * getExceptionHandlerBeanNameテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・引数にnullが渡されること
     * 確認項目
     * ・空文字が返却されること
     * </pre>
     */
    @Test
    public void testGetExceptionHandlerBeanName02() {
        // テスト実施
        // 結果検証
        assertEquals(
                "",
                ((BLogicExceptionHandlerResolverImpl) bLogicExceptionHandlerResolver)
                        .getExceptionHandlerBeanName(null));
    }

}
