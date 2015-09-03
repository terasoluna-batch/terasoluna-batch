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

package jp.terasoluna.fw.web.jndi;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;
import static uk.org.lidalia.slf4jtest.LoggingEvent.info;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.web.jndi.DefaultJndiSupport} クラスのブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4> TERASOLUNAが提供するJNDI関連のユーティリティデフォルト実装クラス。
 * <p>
 * @see jp.terasoluna.fw.web.jndi.DefaultJndiSupport
 */
public class DefaultJndiSupportTest extends TestCase {

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            DefaultJndiSupport.class);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        logger.clear();
        super.tearDown();
    }

    /**
     * testSetJndiPrefix01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) super.resourceRef:true<br>
     * (状態) super.resourceRef:false<br>
     * <br>
     * 期待値：(状態変化) super.resourceRef:true<br>
     * <br>
     * 引数に指定した値がスーパークラスのresourceRefに正常に格納されることを確認 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testSetJndiPrefix01() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();
        support.setResourceRef(false);

        // テスト実施
        support.setJndiPrefix(true);

        // 判定
        // スーパークラスのメソッド呼び出し
        boolean b = support.isResourceRef();
        assertTrue(b);
    }

    /**
     * testIsJndiPrefix01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(状態) super.resourceRef:true<br>
     * <br>
     * 期待値：(戻り値) super.resourceRef:true<br>
     * <br>
     * DefaultJndiSupportに格納されているsuper.resourceRefを正常に取得する ことを確認 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsJndiPrefix01() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();
        // スーパークラスのメソッドで前提条件設定
        support.setResourceRef(true);

        // テスト実施
        boolean b = support.isJndiPrefix();

        // 判定
        assertTrue(b);
    }

    /**
     * testGetJndiEnvironmentMap01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(状態) jndiEnvironmentMap:not null<br>
     * <br>
     * 期待値：(戻り値) Map:インスタンス変数のjndiEnvironmentMap<br>
     * <br>
     * インスタンス変数のjndiEnvironmentMapが取得できることを確認する。<br>
     * ※正常系1件のみ確認。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetJndiEnvironmentMap01() throws Exception {

        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();
        Map<?, ?> map = new HashMap();

        ReflectionTestUtils.setField(support, "jndiEnvironmentMap", map);

        // テスト実施
        Object result = support.getJndiEnvironmentMap();

        // 判定判定
        assertSame(map, result);
    }

    /**
     * testSetJndiEnvironmentMap01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) jndiEnvironmentMap:not null<br>
     * (状態) jndiEnvironmentMap:null<br>
     * <br>
     * 期待値：(状態変化) jndiEnvironmentMap:not null（引数に指定したMap）<br>
     * <br>
     * 引数に指定したMapがインスタンス変数jndiEnvironmentMapに設定されることを確認する。<br>
     * ※正常系1件のみ確認。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testSetJndiEnvironmentMap01() throws Exception {

        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();
        Map<String, String> map = new HashMap<String, String>();

        // テスト実施
        support.setJndiEnvironmentMap(map);

        // 判定判定
        Object result = ReflectionTestUtils.getField(support,
                "jndiEnvironmentMap");
        assertSame(map, result);
    }

    /**
     * testInitialize01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(状態) jndiEnvironmentMap:null<br>
     * <br>
     * 期待値：(状態変化) jndiEnvironmentMap:null<br>
     * <br>
     * jndiEnvironmentMapがnullだった場合、JndiTemplateの環境プロパティは設定されないことを確認 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testInitialize01() throws Exception {

        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();
        ReflectionTestUtils.setField(support, "jndiEnvironmentMap", null);

        // テスト実施
        support.initialize();

        // 判定
        Object result = ReflectionTestUtils.getField(support,
                "jndiEnvironmentMap");
        assertNull(result);
    }

    /**
     * testInitialize02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) jndiEnvironmentMap.get("factory")："factory"<br>
     * (状態) jndiEnvironmentMap.get("url")："url"<br>
     * (状態) jndiEnvironmentMap.get("username")：null<br>
     * <br>
     * 期待値：(状態変化) jndiEnvironmentMap:not null<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.INITIAL_CONTEXT_FACTORY):"factory"<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.PROVIDER_URL):"url"<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.SECURITY_PRINCIPAL):null<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.SECURITY_CREDENTIALS):null<br>
     * (状態変化) ログ:＜ログ＞<br>
     * インフォログ：<br>
     * "java.naming.factory.initial = factory"<br>
     * "java.naming.provider.url = url"<br>
     * "java.naming.security.principal = null"<br>
     * "java.naming.security.credentials = null"<br>
     * <br>
     * jndiEnvironmentMap.get("username")がnullだった場合、 JndiTemplateの環境プロパティにjndiFactoryとjndiUrlだけが設定されていることを確認 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testInitialize02() throws Exception {
        // 前処理
        Map<String, String> map = new HashMap<String, String>();
        map.put("factory", "factory");
        map.put("url", "url");

        DefaultJndiSupport support = new DefaultJndiSupport();
        ReflectionTestUtils.setField(support, "jndiEnvironmentMap", map);

        // テスト実施
        logger.clear();
        support.initialize();

        // 判定
        Object result = ReflectionTestUtils.getField(support,
                "jndiEnvironmentMap");
        assertSame(map, result);

        assertThat(logger.getLoggingEvents(), is(asList(info(
                "Initialize Weblogic JNDI Resource"), info(
                        "java.naming.factory.initial = factory"), info(
                                "java.naming.provider.url = url"), info(
                                        "java.naming.security.principal = null"),
                info("java.naming.security.credentials = null"))));

        Properties props = support.getJndiEnvironment();
        assertEquals("factory", props.get(Context.INITIAL_CONTEXT_FACTORY));
        assertEquals("url", props.get(Context.PROVIDER_URL));
        assertNull(props.get(Context.SECURITY_PRINCIPAL));
        assertNull(props.get(Context.SECURITY_CREDENTIALS));
    }

    /**
     * testInitialize03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) jndiEnvironmentMap.get("factory")："factory"<br>
     * (状態) jndiEnvironmentMap.get("url")："url"<br>
     * (状態) jndiEnvironmentMap.get("username")：""<br>
     * <br>
     * 期待値：(状態変化) jndiEnvironmentMap:not null<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.INITIAL_CONTEXT_FACTORY):"factory"<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.PROVIDER_URL):"url"<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.SECURITY_PRINCIPAL):null<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.SECURITY_CREDENTIALS):null<br>
     * (状態変化) ログ:＜ログ＞<br>
     * インフォログ：<br>
     * "java.naming.factory.initial = factory"<br>
     * "java.naming.provider.url = url"<br>
     * "java.naming.security.principal = "<br>
     * "java.naming.security.credentials = null"<br>
     * <br>
     * jndiEnvironmentMap.get("username")が空文字だった場合、JndiTemplateの環境 プロパティにjndiFactoryとjndiUrlだけが設定されていることを確認 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testInitialize03() throws Exception {

        // 前処理
        Map<String, String> map = new HashMap<String, String>();
        map.put("factory", "factory");
        map.put("url", "url");
        map.put("username", "");

        DefaultJndiSupport support = new DefaultJndiSupport();
        ReflectionTestUtils.setField(support, "jndiEnvironmentMap", map);

        // テスト実施
        support.initialize();

        // 判定
        Object result = ReflectionTestUtils.getField(support,
                "jndiEnvironmentMap");
        assertSame(map, result);

        assertThat(logger.getLoggingEvents(), is(asList(info(
                "Initialize Weblogic JNDI Resource"), info(
                        "java.naming.factory.initial = factory"), info(
                                "java.naming.provider.url = url"), info(
                                        "java.naming.security.principal = "),
                info("java.naming.security.credentials = null"))));

        Properties props = support.getJndiEnvironment();
        assertEquals("factory", props.get(Context.INITIAL_CONTEXT_FACTORY));
        assertEquals("url", props.get(Context.PROVIDER_URL));
        assertNull(props.get(Context.SECURITY_PRINCIPAL));
        assertNull(props.get(Context.SECURITY_CREDENTIALS));
    }

    /**
     * testInitialize04() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) jndiEnvironmentMap.get("factory")："factory"<br>
     * (状態) jndiEnvironmentMap.get("url")："url"<br>
     * (状態) jndiEnvironmentMap.get("username")："username"<br>
     * (状態) jndiEnvironmentMap.get("password")：null<br>
     * <br>
     * 期待値：(状態変化) jndiEnvironmentMap:not null<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.INITIAL_CONTEXT_FACTORY):"factory"<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.PROVIDER_URL):"url"<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.SECURITY_PRINCIPAL):"username"<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.SECURITY_CREDENTIALS):""<br>
     * (状態変化) ログ:＜ログ＞<br>
     * インフォログ：<br>
     * "java.naming.factory.initial = factory"<br>
     * "java.naming.provider.url = url"<br>
     * "java.naming.security.principal = username"<br>
     * "java.naming.security.credentials = "<br>
     * <br>
     * jndiEnvironmentMap.get("password")がnullだった場合、JndiTemplateの環境
     * プロパティにjndiFactoryとjndiUrlとjndiUsernameと空文字のjndiPasswordが設定されていることを確認 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testInitialize04() throws Exception {
        // 前処理
        Map<String, String> map = new HashMap<String, String>();
        map.put("factory", "factory");
        map.put("url", "url");
        map.put("username", "username");

        DefaultJndiSupport support = new DefaultJndiSupport();
        ReflectionTestUtils.setField(support, "jndiEnvironmentMap", map);

        // テスト実施
        support.initialize();

        // 判定
        Object result = ReflectionTestUtils.getField(support,
                "jndiEnvironmentMap");
        assertSame(map, result);

        assertThat(logger.getLoggingEvents(), is(asList(info(
                "Initialize Weblogic JNDI Resource"), info(
                        "java.naming.factory.initial = factory"), info(
                                "java.naming.provider.url = url"), info(
                                        "java.naming.security.principal = username"),
                info("java.naming.security.credentials = "))));

        Properties props = support.getJndiEnvironment();
        assertEquals("factory", props.get(Context.INITIAL_CONTEXT_FACTORY));
        assertEquals("url", props.get(Context.PROVIDER_URL));
        assertEquals("username", props.get(Context.SECURITY_PRINCIPAL));
        assertEquals("", props.get(Context.SECURITY_CREDENTIALS));
    }

    /**
     * testInitialize05() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) jndiEnvironmentMap.get("factory")："factory"<br>
     * (状態) jndiEnvironmentMap.get("url")："url"<br>
     * (状態) jndiEnvironmentMap.get("username")："username"<br>
     * (状態) jndiEnvironmentMap.get("password")：""<br>
     * <br>
     * 期待値：(状態変化) jndiEnvironmentMap:not null<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.INITIAL_CONTEXT_FACTORY):"factory"<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.PROVIDER_URL):"url"<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.SECURITY_PRINCIPAL):"username"<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.SECURITY_CREDENTIALS):""<br>
     * (状態変化) ログ:＜ログ＞<br>
     * インフォログ：<br>
     * "java.naming.factory.initial = factory"<br>
     * "java.naming.provider.url = url"<br>
     * "java.naming.security.principal = username"<br>
     * "java.naming.security.credentials = "<br>
     * <br>
     * jndiEnvironmentMap.get("password")が空文字だった場合、JndiTemplateの環境
     * プロパティにjndiFactoryとjndiUrlとjndiUsernameとjndiPasswordが設定されていることを確認 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testInitialize05() throws Exception {
        // 前処理
        Map<String, String> map = new HashMap<String, String>();
        map.put("factory", "factory");
        map.put("url", "url");
        map.put("username", "username");
        map.put("password", "");

        DefaultJndiSupport support = new DefaultJndiSupport();
        ReflectionTestUtils.setField(support, "jndiEnvironmentMap", map);

        // テスト実施
        support.initialize();

        // 判定
        Object result = ReflectionTestUtils.getField(support,
                "jndiEnvironmentMap");
        assertSame(map, result);

        assertThat(logger.getLoggingEvents(), is(asList(info(
                "Initialize Weblogic JNDI Resource"), info(
                        "java.naming.factory.initial = factory"), info(
                                "java.naming.provider.url = url"), info(
                                        "java.naming.security.principal = username"),
                info("java.naming.security.credentials = "))));

        Properties props = support.getJndiEnvironment();
        assertEquals("factory", props.get(Context.INITIAL_CONTEXT_FACTORY));
        assertEquals("url", props.get(Context.PROVIDER_URL));
        assertEquals("username", props.get(Context.SECURITY_PRINCIPAL));
        assertEquals("", props.get(Context.SECURITY_CREDENTIALS));
    }

    /**
     * testInitialize06() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) jndiEnvironmentMap.get("factory")："factory"<br>
     * (状態) jndiEnvironmentMap.get("url")："url"<br>
     * (状態) jndiEnvironmentMap.get("username")："username"<br>
     * (状態) jndiEnvironmentMap.get("password")："password"<br>
     * <br>
     * 期待値：(状態変化) jndiEnvironmentMap:not null<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.INITIAL_CONTEXT_FACTORY):"factory"<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.PROVIDER_URL):"url"<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.SECURITY_PRINCIPAL):"username"<br>
     * (状態変化) getJndiTemplate().get(<br>
     * Context.SECURITY_CREDENTIALS):"password"<br>
     * (状態変化) ログ:＜ログ＞<br>
     * インフォログ：<br>
     * "java.naming.factory.initial = factory"<br>
     * "java.naming.provider.url = url"<br>
     * "java.naming.security.principal = username"<br>
     * "java.naming.security.credentials = password"<br>
     * <br>
     * jndiEnvironmentMapに格納されている"factory", "url", "username", "password"がnullでも空文字でもなかった場合、JndiTemplateの環境
     * プロパティにjndiFactory、jndiUrl、jndiUsername、jndiPasswordが設定されていることを確認 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testInitialize06() throws Exception {
        // 前処理
        Map<String, String> map = new HashMap<String, String>();
        map.put("factory", "factory");
        map.put("url", "url");
        map.put("username", "username");
        map.put("password", "password");

        DefaultJndiSupport support = new DefaultJndiSupport();
        ReflectionTestUtils.setField(support, "jndiEnvironmentMap", map);

        // テスト実施
        support.initialize();

        // 判定
        Object result = ReflectionTestUtils.getField(support,
                "jndiEnvironmentMap");
        assertSame(map, result);

        assertThat(logger.getLoggingEvents(), is(asList(info(
                "Initialize Weblogic JNDI Resource"), info(
                        "java.naming.factory.initial = factory"), info(
                                "java.naming.provider.url = url"), info(
                                        "java.naming.security.principal = username"),
                info("java.naming.security.credentials = password"))));

        Properties props = support.getJndiEnvironment();
        assertEquals("factory", props.get(Context.INITIAL_CONTEXT_FACTORY));
        assertEquals("url", props.get(Context.PROVIDER_URL));
        assertEquals("username", props.get(Context.SECURITY_PRINCIPAL));
        assertEquals("password", props.get(Context.SECURITY_CREDENTIALS));
    }

    /**
     * testRebind01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) name:null<br>
     * (引数) obj:"abc"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * (状態変化) ログ:＜ログ＞<br>
     * エラーログ：<br>
     * "Illegal arguments error : name=" + name + ", obj=" + obj<br>
     * <br>
     * 引数nameがnullの場合、例外を起こすことを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testRebind01() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();

        String name = null;
        Object obj = "abc";

        try {
            // テスト実施
            support.rebind(name, obj);
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Illegal arguments error : name=null, obj=abc"))));
        }
    }

    /**
     * testRebind02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) name:""<br>
     * (引数) obj:"abc"<br>
     * (状態) JndiTemplate:DefaultJndiSupport_JndiTemplate_Stub01<br>
     * (状態) super.<br>
     * resourceRef:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallRebind:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:null<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * obj:null<br>
     * <br>
     * 期待値：(状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallRebind:true<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:""<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * obj:"abc"<br>
     * <br>
     * 引数nameが空文字であり、引数objがnot nullの場合、JndiTemplate.rebind()の呼び出し確認を行う <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testRebind02() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();

        String name = "";
        Object obj = "abc";

        // JndiTemplate取得
        DefaultJndiSupport_JndiTemplateStub01 template = new DefaultJndiSupport_JndiTemplateStub01();
        template.setCallRebind(false);
        template.setJndiNameToUse(null);
        template.setObj(null);
        support.setJndiTemplate(template);

        // super.resourceRef = false;
        support.setResourceRef(false);

        // テスト実施
        support.rebind(name, obj);

        // 判定
        boolean b = template.isCallRebind();
        assertTrue(b);
        assertEquals("", template.getJndiNameToUse());
        assertEquals("abc", template.getObj());
    }

    /**
     * testRebind03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) name:"abc"<br>
     * (引数) obj:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * (状態変化) ログ:＜ログ＞<br>
     * エラーログ：<br>
     * "Illegal arguments error : name=" + name + ", obj=" + obj<br>
     * <br>
     * 引数objがnullの場合、例外を起こすことを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testRebind03() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();

        String name = "abc";
        Object obj = null;

        try {
            // テスト実施
            support.rebind(name, obj);
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Illegal arguments error : name=abc, obj=null"))));
        }
    }

    /**
     * testRebind04() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) name:"abc"<br>
     * (引数) obj:"abc"<br>
     * (状態) JndiTemplate:DefaultJndiSupport_JndiTemplate_Stub01<br>
     * (状態) super.<br>
     * resourceRef:true<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallRebind:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:null<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * obj:null<br>
     * <br>
     * 期待値：(状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallRebind:true<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:"java:comp/env/abc"<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * obj:"abc"<br>
     * <br>
     * jndiPrefixがtrueであり、引数nameが"java:comp/env/"で始まらなかった場合、 "java:comp/env/"を引数nameに加えてJndiTemplate.rebind()の呼び出しを
     * 行っていることを確認を行う <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testRebind04() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();

        String name = "abc";
        Object obj = "abc";

        // JndiTemplate取得
        DefaultJndiSupport_JndiTemplateStub01 template = new DefaultJndiSupport_JndiTemplateStub01();
        template.setCallRebind(false);
        template.setJndiNameToUse(null);
        template.setObj(null);
        support.setJndiTemplate(template);

        // super.resourceRef = true;
        support.setResourceRef(true);

        // テスト実施
        support.rebind(name, obj);

        // 判定
        boolean b = template.isCallRebind();
        assertTrue(b);
        assertEquals("java:comp/env/abc", template.getJndiNameToUse());
        assertEquals("abc", template.getObj());
    }

    /**
     * testRebind05() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) name:"java:comp/env/abc"<br>
     * (引数) obj:"abc"<br>
     * (状態) JndiTemplate:DefaultJndiSupport_JndiTemplate_Stub02<br>
     * (状態) super.<br>
     * resourceRef:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallRebind:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:null<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * obj:null<br>
     * (状態) NamingException:発生<br>
     * <br>
     * 期待値：(状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallRebind:true<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:"java:comp/env/abc"<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * obj:"abc"<br>
     * (状態変化) 例外:JndiException：<br>
     * ラップした例外：NamingException<br>
     * (状態変化) ログ:＜ログ＞<br>
     * エラーログ：<br>
     * "Illegal JNDI context name."<br>
     * <br>
     * JndiTemplate.rebind()でNamingExceptionが発生した場合、JndiExceptionを 起こすことを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testRebind05() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();

        String name = "java:comp/env/abc";
        Object obj = "abc";

        // JndiTemplate取得
        DefaultJndiSupport_JndiTemplateStub02 template = new DefaultJndiSupport_JndiTemplateStub02();
        template.setCallRebind(false);
        template.setJndiNameToUse(null);
        template.setObj(null);
        support.setJndiTemplate(template);

        // super.resourceRef = false;
        support.setResourceRef(false);

        try {
            // テスト実施
            support.rebind(name, obj);
            fail();
        } catch (JndiException e) {
            boolean b = template.isCallRebind();
            assertTrue(b);
            assertEquals("java:comp/env/abc", template.getJndiNameToUse());
            assertEquals("abc", template.getObj());

            // 判定
            assertEquals(NamingException.class.getName(), e.getCause()
                    .getClass().getName());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Illegal JNDI context name."))));
        }
    }

    /**
     * testUnbind01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) name:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * (状態変化) ログ:＜ログ＞<br>
     * エラーログ：<br>
     * "Illegal arguments error : name=" + name<br>
     * <br>
     * 引数nameがnullの場合、例外を起こすことを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testUnbind01() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();

        String name = null;

        try {
            // テスト実施
            support.unbind(name);
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Illegal arguments error : name=null"))));
        }
    }

    /**
     * testUnbind02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) name:""<br>
     * (状態) JndiTemplate:DefaultJndiSupport_JndiTemplate_Stub01<br>
     * (状態) super.<br>
     * resourceRef:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallUnbind:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:null<br>
     * <br>
     * 期待値：(状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallUnbind:true<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:""<br>
     * <br>
     * 引数nameが空文字の場合、JndiTemplate.unbind()の呼び出し確認を行う <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testUnbind02() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();

        String name = "";

        // JndiTemplate取得
        DefaultJndiSupport_JndiTemplateStub01 template = new DefaultJndiSupport_JndiTemplateStub01();
        template.setCallUnbind(false);
        template.setJndiNameToUse(null);

        support.setJndiTemplate(template);

        // super.resourceRef = false;
        support.setResourceRef(false);

        // テスト実施
        support.unbind(name);

        // 判定
        boolean b = template.isCallUnbind();
        assertTrue(b);
        assertEquals("", template.getJndiNameToUse());
    }

    /**
     * testUnbind03() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) name:"abc"<br>
     * (状態) JndiTemplate:DefaultJndiSupport_JndiTemplate_Stub01<br>
     * (状態) super.<br>
     * resourceRef:true<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallUnbind:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:null<br>
     * <br>
     * 期待値：(状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallUnbind:true<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:"java:comp/env/abc"<br>
     * <br>
     * super.resourceRefがtrueであり、引数nameが"java:comp/env/"で 始まらなかった場合、"java:comp/env/"を引数nameに加えて
     * JndiTemplate.unbind()の呼び出しを行っていることを確認を行う <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testUnbind03() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();

        String name = "abc";

        // JndiTemplate取得
        DefaultJndiSupport_JndiTemplateStub01 template = new DefaultJndiSupport_JndiTemplateStub01();
        template.setCallRebind(false);
        template.setJndiNameToUse(null);

        support.setJndiTemplate(template);

        // super.resourceRef = true;
        support.setResourceRef(true);

        // テスト実施
        support.unbind(name);

        // 判定
        boolean b = template.isCallUnbind();
        assertTrue(b);
        assertEquals("java:comp/env/abc", template.getJndiNameToUse());
    }

    /**
     * testUnbind04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) name:"java:comp/env/abc"<br>
     * (状態) JndiTemplate:DefaultJndiSupport_JndiTemplate_Stub02<br>
     * (状態) super.<br>
     * resourceRef:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallUnbind:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:null<br>
     * (状態) NamingException:発生<br>
     * <br>
     * 期待値：(状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallUnbind:true<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:"java:comp/env/abc"<br>
     * (状態変化) 例外:JndiException：<br>
     * ラップした例外：NamingException<br>
     * (状態変化) ログ:＜ログ＞<br>
     * エラーログ：<br>
     * "Illegal JNDI context name."<br>
     * <br>
     * JndiTemplate.unbind()でNamingExceptionが発生した場合、JndiExceptionを 起こすことを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testUnbind04() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();

        String name = "java:comp/env/abc";

        // JndiTemplate取得
        DefaultJndiSupport_JndiTemplateStub02 template = new DefaultJndiSupport_JndiTemplateStub02();
        template.setCallRebind(false);
        template.setJndiNameToUse(null);

        support.setJndiTemplate(template);

        // super.resourceRef = false;
        support.setResourceRef(false);

        try {
            // テスト実施
            support.unbind(name);
            fail();
        } catch (JndiException e) {
            boolean b = template.isCallUnbind();
            assertTrue(b);
            assertEquals("java:comp/env/abc", template.getJndiNameToUse());

            // 判定
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Illegal JNDI context name."))));
            assertEquals(NamingException.class.getName(), e.getCause()
                    .getClass().getName());
        }
    }

    /**
     * testLookup01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) name:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * (状態変化) ログ:＜ログ＞<br>
     * エラーログ：<br>
     * "Illegal arguments error : name=" + name<br>
     * <br>
     * 引数nameがnullの場合、例外を起こすことを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testLookup01() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();

        String name = null;

        try {
            // テスト実施
            support.lookup(name);
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Illegal arguments error : name=null"))));
        }
    }

    /**
     * testLookup02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) name:""<br>
     * (状態) JndiTemplate:DefaultJndiSupport_JndiTemplate_Stub01<br>
     * (状態) super.<br>
     * resourceRef:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallLookup:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:null<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * lookup()戻り値:"return"<br>
     * <br>
     * 期待値：(戻り値) Object:"return"<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallLookup:true<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:""<br>
     * <br>
     * 引数nameが空文字の場合、JndiTemplate.lookup()の呼び出し確認を行う <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testLookup02() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();

        String name = "";

        // JndiTemplate取得
        DefaultJndiSupport_JndiTemplateStub01 template = new DefaultJndiSupport_JndiTemplateStub01();
        template.setCallLookup(false);
        template.setJndiNameToUse(null);

        support.setJndiTemplate(template);

        // super.resourceRef = false;
        support.setResourceRef(false);

        // テスト実施
        Object result = support.lookup(name);

        // 判定
        boolean b = template.isCallLookup();
        assertTrue(b);
        assertEquals("", template.getJndiNameToUse());
        assertEquals("return", result);
    }

    /**
     * testLookup03() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) name:"abc"<br>
     * (状態) JndiTemplate:DefaultJndiSupport_JndiTemplate_Stub01<br>
     * (状態) super.<br>
     * resourceRef:true<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallLookup:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:null<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * lookup()戻り値:"return"<br>
     * <br>
     * 期待値：(戻り値) Object:"return"<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallLookup:true<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:"java:comp/env/abc"<br>
     * <br>
     * jndiPrefixがtrueであり、引数nameが"java:comp/env/"で始まらなかった場合、 "java:comp/env/"を引数nameに加えてJndiTemplate.lookup()の呼び出しが
     * 行われていることを確認を行う <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testLookup03() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();

        String name = "abc";

        // JndiTemplate取得
        DefaultJndiSupport_JndiTemplateStub01 template = new DefaultJndiSupport_JndiTemplateStub01();
        template.setCallLookup(false);
        template.setJndiNameToUse(null);

        support.setJndiTemplate(template);

        // super.resourceRef = true;
        support.setResourceRef(true);

        // テスト実施
        Object result = support.lookup(name);

        // 判定
        boolean b = template.isCallLookup();
        assertTrue(b);
        assertEquals("java:comp/env/abc", template.getJndiNameToUse());
        assertEquals("return", result);
    }

    /**
     * testLookup04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) name:"java:comp/env/abc"<br>
     * (状態) JndiTemplate:DefaultJndiSupport_JndiTemplate_Stub02<br>
     * (状態) super.<br>
     * resourceRef:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallLookup:false<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:null<br>
     * (状態) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * lookup()戻り値:"return"<br>
     * (状態) NamingException:発生<br>
     * <br>
     * 期待値：(状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * isCallLookup:true<br>
     * (状態変化) DefaultJndiSupport_JndiTemplate_Stub01.<br>
     * jndiNameToUse:"java:comp/env/abc"<br>
     * (状態変化) 例外:JndiException：<br>
     * ラップした例外：NamingException<br>
     * (状態変化) ログ:＜ログ＞<br>
     * エラーログ：<br>
     * "Illegal JNDI context name."<br>
     * <br>
     * JndiTemplate.lookup()でNamingExceptionが発生した場合、 JndiExceptionを起こすことを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testLookup04() throws Exception {
        // 前処理
        DefaultJndiSupport support = new DefaultJndiSupport();

        String name = "java:comp/env/abc";

        // JndiTemplate取得
        DefaultJndiSupport_JndiTemplateStub02 template = new DefaultJndiSupport_JndiTemplateStub02();
        template.setCallLookup(false);
        template.setJndiNameToUse(null);

        support.setJndiTemplate(template);

        // super.resourceRef = false;
        support.setResourceRef(false);

        try {
            // テスト実施
            support.lookup(name);
            fail();
        } catch (JndiException e) {
            boolean b = template.isCallLookup();
            assertTrue(b);
            assertEquals("java:comp/env/abc", template.getJndiNameToUse());

            // 判定
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Illegal JNDI context name."))));
            assertEquals(NamingException.class.getName(), e.getCause()
                    .getClass().getName());
        }
    }
}
