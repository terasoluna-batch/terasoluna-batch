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

package jp.terasoluna.fw.batch.blogic;

import static java.util.Arrays.*;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static uk.org.lidalia.slf4jtest.LoggingEvent.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * BLogicResolverのテストケースクラス
 */
public class BLogicResolverImplTest {

    BLogicResolverImpl blogicResolverImpl;

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            BLogicResolverImpl.class);

    @Before
    public void setUp() {
        // テスト準備
        blogicResolverImpl = new BLogicResolverImpl();
    }

    /**
     * テスト後処理：ロガーのクリアを行う。
     */
    @After
    public void tearDown() {
        logger.clear();
    }

    /**
     * resolveBLogicテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・beansDef/B000003.xmlにBLogic(B000003)の定義が存在する
     * 確認項目
     * ・BLogicのインスタンスが生成されること
     * </pre>
     */
    @Test
    public void testResolveBLogic01() {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "beansDef/B000003.xml");
        // テスト実施
        BLogic blogic = blogicResolverImpl.resolveBLogic(applicationContext,
                "B000003");
        // 結果検証
        assertTrue(blogic instanceof BLogic);
    }

    /**
     * resolveBLogicテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・beansDef/B000003.xmlにBLogic(DEFINE_NOT_EXIST)の定義が存在しない
     * 確認項目
     * ・NoSuchBeanDefinitionException例外がスローされること
     * ・[EAL025009]のログが出力されること
     * </pre>
     */
    @Test
    public void testResolveBLogic02() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "beansDef/B000003.xml");
        try {
            // テスト実施
            blogicResolverImpl.resolveBLogic(applicationContext,
                    "DEFINE_NOT_EXIST");
            fail();
        } catch (Exception e) {
            // 結果検証
            assertTrue(e instanceof NoSuchBeanDefinitionException);
        }
        assertThat(logger.getLoggingEvents(), is(asList(error(
                "[EAL025009] BLogic bean not found. beanName:DEFINE_NOT_EXISTBLogic"))));
    }

    /**
     * resolveBLogicテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・beansDef/B000003.xmlにBLogic(B000003 : 先頭小文字)の定義が存在する
     * 確認項目
     * ・BLogicのインスタンスが生成されること
     * </pre>
     */
    @Test
    public void testResolveBLogic03() {
        // テスト準備
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "beansDef/B000003.xml");
        ApplicationContext mockContext = spy(applicationContext);
        BLogic mockBLogic = mock(BLogic.class);

        // 先頭大文字はfalseを返却させる
        doReturn(false).when(mockContext).containsBean("B000003BLogic");
        // 先頭小文字のBeanを準備する
        doReturn(mockBLogic).when(mockContext).getBean("b000003BLogic",
                BLogic.class);

        // テスト実施
        BLogic blogic = blogicResolverImpl.resolveBLogic(mockContext,
                "B000003");
        // 結果検証
        assertTrue(blogic instanceof BLogic);
    }

    /**
     * getBLogicBeanNameテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・beansDef/B000003.xmlにBLogic(B000003)の定義が存在する
     * 確認項目
     * ・BLogicのインスタンスが生成されること
     * </pre>
     */
    @Test
    public void testGetBLogicBeanName01() {

        // テスト実施
        // 結果検証
        assertEquals("B000003" + "BLogic", blogicResolverImpl.getBLogicBeanName(
                "B000003"));
    }

    /**
     * getBLogicBeanNameテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・ジョブアプリケーションコードとしてnullを渡す
     * 確認項目
     * ・空文字列が返却されること
     * </pre>
     */
    @Test
    public void testGetBLogicBeanName02() {
        // テスト実施
        // 結果検証
        assertEquals("", blogicResolverImpl.getBLogicBeanName(null));
    }

}
