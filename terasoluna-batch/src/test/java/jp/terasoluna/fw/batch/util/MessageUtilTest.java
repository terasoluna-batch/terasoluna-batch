/*
 * Copyright (c) 2011 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.util;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.org.lidalia.slf4jtest.LoggingEvent.warn;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import jp.terasoluna.fw.batch.message.MessageAccessor;
import jp.terasoluna.fw.util.PropertyUtil;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * 事前条件<br>
 * <br>
 * ・src/test/resourcesフォルダ配下にAppricationResources.propertiesが存在すること。<br>
 * <br>
 * ・プロパティMessageAccessor.defaultの値が設定されていること。<br>
 * <fieldset><legend>batch.properties設定例</legend> #メッセージソースアクセサのBean名<br>
 * MessageAccessor.default=msgAcc </fieldset> <br>
 * ・Bean定義ファイルにプロパティで設定されたの値のBean名が設定されていること。<br>
 * <fieldset><legend>AdminContext.xml設定例</legend> &lt;!-- メッセージアクセサ --&gt;<br>
 * &lt;bean id=&quot;msgAcc&quot; class=&quot;jp.terasoluna.fw.batch.message.MessageAccessorImpl&quot; /&gt; </fieldset> <br>
 * ・messages.propertiesファイルが存在すること<br>
 */
@SuppressWarnings("deprecation")
public class MessageUtilTest {

    /**
     * プロパティ値取得値
     */
    private String value = null;

    /**
     * コンテナ用のフィールド
     */
    private ApplicationContext context;

    /**
     * MessageAccessorクラスのフィールド
     */
    private MessageAccessor messageAccessor;

    private static TestLogger logger = TestLoggerFactory.getTestLogger(
            MessageUtil.class);

    @Before
    public void setUp() {

        // メッセージソースアクセサのBean名取得
        context = new ClassPathXmlApplicationContext(
                "beansDef/AdminContext.xml");
        value = PropertyUtil.getProperty("messageAccessor.default");
        messageAccessor = (MessageAccessor) context.getBean(value,
                MessageAccessor.class);
        MessageUtil.setMessageAccessor(messageAccessor);
        
        logger.clear();
    }

    /**
     * testGetMessage01()<br>
     * <br>
     * 事前状態：messages.propertiesにerrors.alphaNumericStringが設定されていること<br>
     * <br>
     * テスト概要：メッセージキーに該当するメッセージを正常に取得することができることを確認する<br>
     * <br>
     * 確認項目：プロパティに設定したメッセージが取得されていることを確認する<br>
     * <br>
     * @throws Exception
     */
    @Test
    public void testGetMessage01() throws Exception {

        String result = MessageUtil.getMessage("errors.alphaNumericString");

        assertEquals("{0}には半角英数字で入力してください.", result);
    }

    /**
     * testGetMessage02()<br>
     * <br>
     * 事前状態：errors.alphaNumericStringが設定されていること<br>
     * <br>
     * テスト概要：パラメータを1つ持つメッセージキーに該当するメッセージを正常に取得することができることを確認する<br>
     * <br>
     * 確認項目：プロパティに設定したメッセージが取得されていることを確認する<br>
     * <br>
     * @throws Exception
     */
    @Test
    public void testGetMessage02() throws Exception {

        String[] args = { "test1" };
        String result = MessageUtil.getMessage("errors.alphaNumericString",
                args);

        assertEquals("test1には半角英数字で入力してください.", result);
    }

    /**
     * testGetMessage03()<br>
     * <br>
     * 事前状態：errors.rangeが設定されていること<br>
     * <br>
     * テスト概要：パラメータを３つ持つメッセージキーに該当するメッセージを正常に取得することができることを確認する<br>
     * <br>
     * 確認項目：プロパティに設定したメッセージが取得されていることを確認する<br>
     * <br>
     * @throws Exception
     */
    @Test
    public void testGetMessage03() throws Exception {

        Object[] args = { "test1", 10, 20 };
        String result = MessageUtil.getMessage("errors.range", args);

        assertEquals("test1には10から20までの範囲で入力してください.", result);
    }

    /**
     * testGetMessage04()<br>
     * <br>
     * 事前状態：testというメッセージキーが設定されていないこと<br>
     * <br>
     * テスト概要：メッセージキーに該当するメッセージが取得することができないことを確認する<br>
     * <br>
     * 確認項目：エラーメッセージが取得されていることを確認する<br>
     * <br>
     * @throws Exception
     */
    @Test
    public void testGetMessage04() throws Exception {

        String[] args = { "test1" };
        String result = MessageUtil.getMessage("test", args);

        assertNotNull(result);
        assertEquals("Message not found. CODE:[test]", result);
    }

    /**
     * testGetMessage04()<br>
     * <br>
     * テスト概要：nullを設定した場合に該当するメッセージが取得することができないことを確認する<br>
     * <br>
     * 確認項目：エラーメッセージが取得されていることを確認する<br>
     * <br>
     * @throws Exception
     */
    @Test
    public void testGetMessage05() throws Exception {

        String result = MessageUtil.getMessage(null);

        assertNotNull(result);
        assertEquals("Message not found. CODE:[null]", result);
    }

    /**
     * testGetMessage06
     */
    @Test
    public void testGetMessage06() {
        Thread th = Thread.currentThread();
        ThreadGroup g = th.getThreadGroup();
        try {
            ReflectionTestUtils.setField(th, "group", null);
            String result = MessageUtil.getMessage("hoge");
            assertEquals("Message not found. CODE:[hoge]", result);
        } finally {
            ReflectionTestUtils.setField(th, "group", g);
        }
    }

    /**
     * testSetMessageAccessor01
     * @throws Exception
     */
    @Test
    public void testSetMessageAccessor01() throws Exception {
        MessageUtil.setMessageAccessor(null);
        assertThat(logger.getLoggingEvents(), is(asList(
                warn("[WAL025008] MessageAccessor setting is not specified, it will be skipped.  tg:[main] t:[main]"))));
        assertTrue(true);
    }

    /**
     * testRemoveMessageAccessor01
     * @throws Exception
     */
    @Test
    public void testRemoveMessageAccessor01() throws Exception {
        MessageUtil.removeMessageAccessor();
        assertTrue(true);
    }

    /**
     * testGetThreadGroup01
     * @throws Exception
     */
    @Test
    public void testGetThreadGroup01() throws Exception {
        Method method = MessageUtil.class.getDeclaredMethod("getThreadGroup");
        method.setAccessible(true);
        Object threadGroup = (ThreadGroup) method.invoke(MessageUtil.class);
        assertTrue(threadGroup instanceof ThreadGroup);
        assertTrue(true);
    }

    /**
     * testMessageUtil001
     * @throws Exception
     */
    @Test
    public void testMessageUtil001() throws Exception {
        MessageUtil mu = new MessageUtil();
        assertNotNull(mu);
    }

}
