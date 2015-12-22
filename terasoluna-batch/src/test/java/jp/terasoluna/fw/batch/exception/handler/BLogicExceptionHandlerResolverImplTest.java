/*
 * Copyright (c) 2016 NTT DATA Corporation
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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * BLogicExceptionHandlerResolverのテストケースクラス
 */
public class BLogicExceptionHandlerResolverImplTest {

    private BLogicExceptionHandlerResolver target = new BLogicExceptionHandlerResolverImpl();

    private ApplicationContext ctx = mock(ApplicationContext.class);

    private ExceptionHandler exHandler = mock(ExceptionHandler.class);
    
    private String suffix = "ExceptionHandler";
    private String defaultName = "defaultExceptionHandler";
    
    /**
     * テスト後処理：mockのresetを行う
     */
    @After
    public void tearDown() {
        reset(ctx);
    }

    /**
     * resolveExceptionHandlerテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・ExceptionHandlerの定義が存在する
     * 確認項目
     * ・ExceptionHandlerのインスタンスが生成されること
     * </pre>
     */
    @Test
    public void testResolveExceptionHandler01() {
        // テスト準備
        String jobAppCd = "B01";
        String handlerName = jobAppCd + suffix;
        
        when(ctx.containsBean(handlerName)).thenReturn(true);
        when(ctx.getBean(handlerName, ExceptionHandler.class)).thenReturn(exHandler);

        // テスト実施
        ExceptionHandler result = target.resolveExceptionHandler(ctx, jobAppCd);
        // 結果検証
        assertSame(exHandler, result);
    }

    /**
     * resolveExceptionHandlerテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・ExceptionHandlerの定義(先頭小文字)が存在する
     * 確認項目
     * ・ExceptionHandlerのインスタンスが生成されること
     * </pre>
     */
    @Test
    public void testResolveExceptionHandler02() {
        // テスト準備
        String jobAppCd = "B01";
        String handlerName = jobAppCd + suffix;
        
        when(ctx.containsBean(handlerName)).thenReturn(false);
        when(ctx.containsBean("b01" + suffix)).thenReturn(true);
        when(ctx.getBean("b01" + suffix, ExceptionHandler.class)).thenReturn(exHandler);

        // テスト実施
        ExceptionHandler result = target.resolveExceptionHandler(ctx, jobAppCd);
        // 結果検証
        assertSame(exHandler, result);
    }

    /**
     * resolveExceptionHandlerテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・ExceptionHandlerの定義が存在しない。DefaultExceptionHandlerの定義は存在する。
     * 確認項目
     * ・DefaultExceptionHandlerのインスタンスが生成されること
     * </pre>
     */
    @Test
    public void testResolveExceptionHandler03() {
        // テスト準備
        when(ctx.containsBean(anyString())).thenReturn(false);
        when(ctx.containsBean(defaultName)).thenReturn(true);
        ExceptionHandler defaultHandler = new DefaultExceptionHandler();
        when(ctx.getBean(defaultName, ExceptionHandler.class)).thenReturn(defaultHandler);
        
        // テスト実施
        ExceptionHandler result = target.resolveExceptionHandler(ctx, "DEFINE_NOT_EXIST");
        // 結果検証
        assertSame(defaultHandler, result);
    }

    /**
     * resolveExceptionHandlerテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・ExceptionHandler/DefaultExceptionHandlerの定義がいずれも存在しない
     * 確認項目
     * ・nullが返却されること
     * </pre>
     */
    @Test
    public void testResolveExceptionHandler04() {
     // テスト準備
        when(ctx.containsBean(anyString())).thenReturn(false);
        // テスト実施
        ExceptionHandler result = target.resolveExceptionHandler(ctx, "DEFINE_NOT_EXIST");
        // 結果検証
        assertNull(result);
    }

    /**
     * resolveExceptionHandlerテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・ApplicationContextとしてnullが渡されること
     * 確認項目
     * ・nullが返却されること
     * </pre>
     */
    @Test
    public void testResolveExceptionHandler05() {
        ExceptionHandler result = target.resolveExceptionHandler(null, "DEFINE_NOT_EXIST");
        assertNull(result);
    }
    
    /**
     * resolveExceptionHandlerテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・ジョブ業務コードにnullが渡されること
     * 確認項目
     * ・nullが返却されること
     * </pre>
     */
    @Test
    public void testResolveExceptionHandler06() {
        // テスト実施
        ExceptionHandler result = target.resolveExceptionHandler(ctx, null);
        // 結果検証
        assertNull(result);
    }
    
    /**
     * resolveExceptionHandlerテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・ジョブ業務コードに空文字が渡されること
     * 確認項目
     * ・nullが返却されること
     * </pre>
     */
    @Test
    public void testResolveExceptionHandler07() {
        // テスト実施
        ExceptionHandler result = target.resolveExceptionHandler(ctx, "");
        // 結果検証
        assertNull(result);
    }
}
