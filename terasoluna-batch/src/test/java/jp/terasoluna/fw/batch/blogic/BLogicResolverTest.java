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

package jp.terasoluna.fw.batch.blogic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * BLogicResolverのテストケースクラス
 */
public class BLogicResolverTest {

    BLogicResolverImpl bLogicResolverImpl;

    ApplicationContext applicationContext;

    @Before
    public void setUp() {
        bLogicResolverImpl = new BLogicResolverImpl();
        applicationContext = new ClassPathXmlApplicationContext("beansDef/B000011.xml");
    }

    /**
     * setEnableJobComponentAnnotationテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・setEnableJobComponentAnnotation()メソッドでフィールドの値が設定されること
     * </pre>
     */
    @Test
    public void testSetEnableJobComponentAnnotation() {
        bLogicResolverImpl.setEnableJobComponentAnnotation(true);
        assertTrue(bLogicResolverImpl.enableJobComponentAnnotation);
        bLogicResolverImpl.setEnableJobComponentAnnotation(false);
        assertFalse(bLogicResolverImpl.enableJobComponentAnnotation);
    }

    /**
     * resolveBLogicテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・beansDef/B000011.xmlにBLogic(B000011)の定義が存在する
     * 確認項目
     * ・BLogicのインスタンスが生成されること
     * </pre>
     */
    @Test
    public void testResolveBLogic01() {

        BLogic bLogic = bLogicResolverImpl.resolveBLogic(applicationContext,
                "B000011");
        assertNotNull(bLogic);
    }

    /**
     * resolveBLogicテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・beansDef/B000011.xmlにBLogic(DEFINE_NOT_EXIST)の定義が存在しない
     * 確認項目
     * ・NoSuchBeanDefinitionException例外がスローされること
     * </pre>
     */
    @Test
    public void testResolveBLogic02() {
        try {
            bLogicResolverImpl.resolveBLogic(applicationContext,
                    "DEFINE_NOT_EXIST");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof NoSuchBeanDefinitionException);
        }
    }
}
