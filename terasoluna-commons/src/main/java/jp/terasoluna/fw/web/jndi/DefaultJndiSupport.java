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

import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jndi.JndiLocatorSupport;

/**
 * <p>TERASOLUNAが提供するJNDI関連のユーティリティデフォルト実装クラス。</p>
 *
 * <p>
 * WebAPコンテナのJNDIリソースを扱うユーティリティである。<br>
 * JNDIの認証情報が必要な場合は、Bean定義ファイルに必要なプロパティを
 * 以下のように設定し、initializeメソッドを実行すること。
 * Bean定義ファイル経由でこのクラスのインスタンスを生成する場合は
 * init-method属性でinitializeメソッドを指定すること。
 * <br>
 * <table border="1">
 * <caption><strong>Bean定義ファイルの設定</strong></caption>
 * <tr>
 *   <td><center><strong>認証情報名</strong></center></td>
 *   <td><center><strong>説明</strong></center></td>
 *   <td><center><strong>例</strong></center></td>
 * </tr>
 * <tr>
 *   <td>factory</td>
 *   <td>JNDIファクトリクラス名を指定する。</td>
 *   <td>weblogic.jndi.WLInitialContextFactory</td>
 * </tr>
 * <tr>
 *   <td>url</td>
 *   <td>JNDIプロバイダがおかれているURIを指定する。</td>
 *   <td>t3://localhost:7001</td>
 * </tr>
 * <tr>
 *   <td>username</td>
 *   <td>JNDIサーバのユーザ名を指定する。</td>
 *   <td>weblogic</td>
 * </tr>
 * <tr>
 *   <td>password</td>
 *   <td>JNDIサーバのパスワードを指定する。</td>
 *   <td>password</td>
 * </tr>
 * </table>
 * </p>
 * <br>
 *
 * WebLogicのようにJNDIリソース名にプリフィックス「java:comp/env/」を付けては
 * いけない場合、プロパティ「jndiPrefix」をfalseに設定する。<br>
 * デフォルトはfalseである。<br>
 * <br>
 *
 * <strong>WebLogicのBean定義ファイル設定例</strong>
 * <code><pre>
 * &lt;bean id=&quot;jndiSupport&quot; scope="singleton"
 *       class=&quot;jp.terasoluna.fw.web.jndi.DefaultJndiSupport&quot;&gt;
 *       init-method="initialize"&gt;
 *   &lt;!-- セッタインジェクションで認証情報設定 --&gt;
 *   &lt;property name="jndiEnvironmentMap"&gt;
 *     &lt;map&gt;
 *       &lt;entry key="factory"&gt;
 *         &lt;value&gt;weblogic.jndi.WLInitialContextFactory&lt;/value&gt;
 *       &lt;/entry&gt;
 *       &lt;entry key="url"&gt;
 *         &lt;value&gt;t3://localhost:7001&lt;/value&gt;
 *       &lt;/entry&gt;
 *       &lt;entry key="username"&gt;
 *         &lt;value&gt;weblogic&lt;/value&gt;
 *       &lt;/entry&gt;
 *       &lt;entry key="password"&gt;
 *         &lt;value&gt;password&lt;/value&gt;
 *       &lt;/entry&gt;
 *     &lt;/map&gt;
 *   &lt;/property&gt;
 *   &lt;!-- プロパティjndiPrefixの設定 --&gt;
 *   &lt;property name="jndiPrefix"&gt;&lt;value&gt;<strong>false</strong>&lt;/value&gt;&lt;/property&gt;
 * &lt;/bean&gt;
 * </pre></code>
 * </p>
 * <strong>TomcatのBean定義ファイル設定例</strong>
 * <code><pre>
 * &lt;bean id=&quot;jndiSupport&quot; scope="singleton"
 *       class=&quot;jp.terasoluna.fw.web.jndi.DefaultJndiSupport&quot; &gt;<br>
 *   &lt;!-- プロパティjndiPrefixの設定（デフォルト値はfalse） --&gt;
 *   &lt;property name="jndiPrefix"&gt;&lt;value&gt;<strong>false</strong>&lt;/value&gt;&lt;/property&gt;
 * &lt;/bean&gt;
 * </pre></code>
 * </p>
 * <strong>使用方法</strong>
 * <p>
 * Bean定義ファイルにサービスロジックの設定を以下のように行う。
 * <code><pre>
 * &lt;bean id="jndiLogic" scope="singleton"
 *   class="jp.sample.JndiLogic"&gt;
 *   &lt;property name=<strong>"jndiSupport"</strong>&gt;
 *     &lt;ref bean=<strong>"jndiSupport"</strong> /&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;<br>
 * &lt;!-- JndiSupport設定 --&gt;
 * &lt;bean id=<strong>"jndiSupport"</strong>  scope="singleton"
 *   class="jp.terasoluna.fw.web.jndi.DefaultJndiSupport" /&gt;
 * </code></pre>
 *
 * サービスロジックで以下のように{@link DefaultJndiSupport}を取得する。<br>
 *
 * <code><pre>
 * public class JndiLogic {
 *   private JndiSupport <strong>jndiSupport</strong> = null;
 *
 *   public void setJndiSupport(jndiSupport) {
 *     this.jndiSupport = jndiSupport;
 *   }
 *
 *   public Object jndiLookup(String name) {
 *     return <strong>jndiSupport.lookup(name)</strong>;
 *   }
 * }
 * </code></pre>
 * </p>
 *
 */
public class DefaultJndiSupport extends JndiLocatorSupport implements
        JndiSupport {

    /**
     * ログクラス。
     */
    private static Log log = LogFactory.getLog(DefaultJndiSupport.class);

    /**
     * JNDIファクトリクラス名をjndiEnvironmentMapからから取得するときのキー。
     */
    private static final String JNDI_FACTORY_KEY = "factory";

    /**
     * JNDIプロバイダのURLをjndiEnvironmentMapからから取得するときのキー。
     */
    private static final String JNDI_URL_KEY = "url";

    /**
     * JNDIユーザ名をjndiEnvironmentMapからから取得するときのキー。
     */
    private static final String JNDI_USERNAME_KEY = "username";

    /**
     * JNDIパスワード名をjndiEnvironmentMapからから取得するときのキー。
     */
    private static final String JNDI_PASSWORD_KEY = "password";

    /**
     * JNDI認証情報を格納する<code>Map</code>。
     */
    private Map<String, String> jndiEnvironmentMap = null;

    /**
     * リソース名のプリフィックスのセッター。
     * スーパークラスのresourceRefの値を設定する。
     * この属性がtrueの場合、プリフィックス"java:comp/env/"をリソース名につける。
     *
     * @param jndiPrefix リソース名のプリフィックス付加フラグ
     */
    public void setJndiPrefix(boolean jndiPrefix) {
        super.setResourceRef(jndiPrefix);
    }

    /**
     * リソース名のプリフィックスのゲッター。
     * スーパークラスのresourceRefの値を取得する。
     * この属性がtrueの場合、プリフィックス"java:comp/env/"をリソース名につける。
     *
     * @return jndiPrefix リソース名のプリフィックス付加フラグ
     */
    public boolean isJndiPrefix() {
        return super.isResourceRef();
    }

    /**
     * jndiEnvironmentMapを取得する。
     * @return JNDI認証情報を格納する<code>Map</code>。
     */
    public Map<String, String> getJndiEnvironmentMap() {
        return jndiEnvironmentMap;
    }

    /**
     * jndiEnvironmentMapを設定する。
     * @param jndiEnvironmentMap JNDI認証情報を格納する<code>Map</code>。
     */
    public void setJndiEnvironmentMap(Map<String, String> jndiEnvironmentMap) {
        this.jndiEnvironmentMap = jndiEnvironmentMap;
    }

    /**
     * コンストラクタ。
     */
    public DefaultJndiSupport() {
    }

    /**
     * JndiTemplateの環境設定を行う。
     */
    public void initialize() {

        // JNDI環境設定がされている場合のみ（Weblogicの場合）
        if (jndiEnvironmentMap != null) {

            // jndiEnvironmentMapから認証情報を取得
            String factory = jndiEnvironmentMap.get(JNDI_FACTORY_KEY);
            String url = jndiEnvironmentMap.get(JNDI_URL_KEY);
            String username = jndiEnvironmentMap.get(JNDI_USERNAME_KEY);
            String password = jndiEnvironmentMap.get(JNDI_PASSWORD_KEY);

            Properties environment = new Properties();
            environment.put(Context.INITIAL_CONTEXT_FACTORY, factory);
            environment.put(Context.PROVIDER_URL, url);

            if (!"".equals(username) && username != null) {
                environment.put(Context.SECURITY_PRINCIPAL, username);
                if (password == null) {
                    password = "";
                }
                environment.put(Context.SECURITY_CREDENTIALS, password);
            }

            // 認証情報プロパティの設定
            getJndiTemplate().setEnvironment(environment);

            // ログ出力
            if (log.isInfoEnabled()) {
                log.info("Initialize Weblogic JNDI Resource");
                log.info(Context.INITIAL_CONTEXT_FACTORY + " = " + factory);
                log.info(Context.PROVIDER_URL + " = " + url);
                log.info(Context.SECURITY_PRINCIPAL + " = " + username);
                log.info(Context.SECURITY_CREDENTIALS + " = " + password);
            }
        }
    }

    /**
     * 名前をオブジェクトにバインドして、
     * 既存のバインディングを上書きする。
     *
     * @param name オブジェクト名
     * @param obj バインドされるオブジェクト
     */
    public void rebind(String name, Object obj) {
        if (name == null || obj == null) {
            log.error("Illegal arguments error : name="
                    + name + ", obj=" + obj);
            throw new IllegalArgumentException();
        }
        // リソース名のプリフィックス設定
        String jndiNameToUse = convertJndiName(name);
        try {
            getJndiTemplate().rebind(jndiNameToUse, obj);
        } catch (NamingException e) {
            log.error("Illegal JNDI context name.");
            throw new JndiException(e);
        }
    }

    /**
     * 指定されたオブジェクトをアンバインドする。
     *
     * @param name オブジェクト名
     */
    public void unbind(String name) {
        if (name == null) {
            log.error("Illegal arguments error : name=" + name);
            throw new IllegalArgumentException();
        }
        // リソース名のプリフィックス設定
        String jndiNameToUse = convertJndiName(name);
        try {
            getJndiTemplate().unbind(jndiNameToUse);
        } catch (NamingException e) {
            log.error("Illegal JNDI context name.");
            throw new JndiException(e);
        }
    }

    /**
     * 指定されたオブジェクトを取得する。
     *
     * @param name オブジェクト名
     * @return オブジェクト
     */
    @Override
    public Object lookup(String name) {
        if (name == null) {
            log.error("Illegal arguments error : name=" + name);
            throw new IllegalArgumentException();
        }
        // リソース名のプリフィックス設定
        String jndiNameToUse = convertJndiName(name);
        try {
            return getJndiTemplate().lookup(jndiNameToUse);
        } catch (NamingException e) {
            log.error("Illegal JNDI context name.");
            throw new JndiException(e);
        }
    }
}