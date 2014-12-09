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

package jp.terasoluna.fw.message;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.AbstractMessageSource;

/**
 * DAOから取得したメッセージリソースより、メッセージコード及びロケールをキー
 * として、メッセージもしくはメッセージフォーマットを決定するクラス。
 * 
 * <p>
 * 本クラスはクラスロード時にDBを参照し、DB中のメッセージリソースからメッセージ
 * もしくはメッセージフォーマットを決定するクラスである。
 * また、国際化に対応しており、言語コード、国コード、バリアントコードによる
 * ロケール判別が可能である。
 * </p>
 * <strong>使用方法</strong><br>
 * このクラスを利用するにはアプリケーションコンテキスト起動時にMessageSource
 * として設定し、またメッセージリソースを格納したDBとの接続をする
 * DAOオブジェクトとして設定する必要がある。<br>
 * <br>
 * <strong>設定例</strong><br>
 * Bean定義ファイルに以下の内容の記述をする。<br>
 * DAOとしてDBMessageResourceDAOを利用した場合<br>
 * 
 * <pre>
 * &lt;bean id = &quot;messageSource&quot;
 *   class = &quot;jp.terasoluna.fw.message.DataSourceMessageSource&quot;&gt;
 *   &lt;property name = &quot;DBMessageResourceDAO&quot;&gt;
 *     &lt;ref bean = &quot;dBMessageResourceDAO&quot;&gt;&lt;/ref&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * <strong>解説</strong><br>
 * &lt;bean&gt;要素のid属性に"messageSource" を指定することでMessageSource
 * として認識される。<br>
 * &lt;bean>要素内&lt;property&gt;要素にはDAOの設定を記述する。<br>
 * <br>
 * 
 * <br>
 * デフォルトロケールの変更<br>
 * デフォルトロケールは、メッセージリソースのロケールが設定されていない場合、
 * もしくは設定されていても正しくロケールが設定されていない場合に指定される
 * ロケールである。<br>
 * デフォルトロケールの初期設定は、クライアントのVMで使用されるロケールである。
 * <br>
 * デフォルトロケールは本クラス内に実装されているsetDefaultLocaleを利用する
 * ことで変更することが出来る。 <br>
 * <br>
 * <strong>設定例</strong><br>
 * Bean定義ファイル中に以下の内容の記述をする。<br>
 * デフォルトロケールを日本語（言語コード「ja」)にする場合。<br>
 * <br>
 * 
 * <pre>
 * &lt;bean id = &quot;messageSource&quot;
 *   class = &quot;jp.terasoluna.fw.message.DataSourceMessageSource&quot;&gt;
 *   &lt;property name = &quot;DBMessageResourceDAO&quot;&gt;
 *     &lt;ref bean = &quot;dBMessageResourceDAO&quot;&gt;&lt;/ref&gt;
 *   &lt;/property&gt;
 *   &lt;property name = &quot;defaultLocale&quot;&gt;
 *     &lt;value&gt;ja&lt;/value&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * <strong>解説</strong><br>
 * &lt;bean&gt;要素内&lt;properities&gt;要素のname属性にdefaultLocaleを指定し、
 * value属性にて設定したい値を指定する。
 * 
 * @see jp.terasoluna.fw.message.DBMessage
 * @see jp.terasoluna.fw.message.DBMessageQuery
 * @see jp.terasoluna.fw.message.DBMessageResourceDAO
 * @see jp.terasoluna.fw.message.DBMessageResourceDAOImpl
 * 
 * 
 */
public class DataSourceMessageSource extends AbstractMessageSource implements
        InitializingBean {

    /**
     * メッセージコード毎にロケールとメッセージフォーマットをマップで保持する。
     * <br>
     * Map &lt;Code, Map &lt;Locale, MessageFormat&gt;&gt;
     */
    protected final Map<String, Map<Locale, MessageFormat>> cachedMessageFormats
                            = new HashMap<String, Map<Locale, MessageFormat>>();

    /**
     * ロケール毎にメッセージコードとメッセージをマップで保持する。
     * <br/> Map &lt;Locale, Properties&gt;
     */
    protected Map<Locale, Properties> cachedMergedProperties
                            = new HashMap<Locale, Properties>();

    /**
     * ログクラス。
     */
    private static Log log = LogFactory.getLog(DataSourceMessageSource.class);
    
    /**
     * ロケールが指定されていない場合のデフォルトロケール。 メッセージリソース内
     * でロケールが指定されていない場合、 このロケールが設定される。
     * デフォルトではサーバー側JVMの言語コードのみをロケールとして使用する。
     */
    protected Locale defaultLocale
                            = new Locale(Locale.getDefault().getLanguage());
    
    /**
     * メッセージリソースを取得するDAO。
     */
    protected DBMessageResourceDAO dbMessageResourceDAO = null;

    /**
     * デフォルトロケールを設定する。設定しない場合はクライアントのVMのロケール
     * が設定される。VMのロケールが認識できない場合は英語が設定される。
     * 
     * @see #getMessageInternal
     * @see java.util.Locale#getDefault
     * 
     * @param defaultLocale
     *            デフォルトのロケール。
     */
    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    /**
     * DBMessageResourceDAOを設定する。
     * 
     * @param dbMessageResourceDAO
     *            全てのメッセージリソースを取得するDAO
     */
    public void setDbMessageResourceDAO(
            DBMessageResourceDAO dbMessageResourceDAO) {
        this.dbMessageResourceDAO = dbMessageResourceDAO;
    }

    /**
     * Webアプリケーションコンテキスト起動時に実行される。<br>
     * メッセージリソースからメッセージコード、ロケール、メッセージ
     * （メッセージフォーマット含む）の３項目で分類し、キャッシュに保持する。
     * 
     * @see #cachedMergedProperties
     * 
     */
    public void afterPropertiesSet() {
        if (log.isDebugEnabled()) {
            log.debug("afterPropertiesSet");
        }
        readMessagesFromDataSource();
    }

    /**
     * メッセージリソースをリロードする。
     * このメソッドを明示的に呼び出すことでDBから動的にメッセージリソースを
     * リロードする。DBの更新があった場合、このメソッドを呼び出すことで
     * メッセージリソースをリロードすることが可能。
     */
    public synchronized void reloadDataSourceMessage() {
        readMessagesFromDataSource();
    }
    
    /**
     * DAOからメッセージリソースを取得し、整理する。メッセージリソースをロケール
     * 別にまとめ、メッセージコードとメッセージ本体をセットにして格納する。
     * 取得した全てのメッセージリソースに対して実施する。<br>
     * メッセージリソースとは、メッセージコード、言語コード、国コード、
     * バリアントコード、メッセージ本体である。
     */
     protected synchronized void readMessagesFromDataSource() {
        if (log.isDebugEnabled()) {
            log.debug("readMessageFromDataSource");
        }
        cachedMergedProperties.clear();
        cachedMessageFormats.clear();
        // DAOからメッセージリソースを取得する
        List<DBMessage> messages = dbMessageResourceDAO.findDBMessages();
        //メッセージコードとメッセージ内容がnullではない場合、
        //キャッシュに読み込む
        for (DBMessage message : messages) {
            if (message.code != null && message.message != null) {
                mapMessage(message);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("get MessageResource from DAO.");
        }
    }

    /**
     * メッセージリソースをロケール別に整理し、メッセージコードとメッセージ本体
     * をセットにして、ハッシュテーブルに格納する。
     * 
     * @param message
     *            メッセージリソースを格納したDBMessageオブジェクト。
     */
    protected void mapMessage(DBMessage message) {
        // ロケールオブジェクトを言語コード、国コード、バリアントコードから
        // 生成する。
        Locale locale = createLocale(message);
        // ロケールに対応する全てのメッセージを取得する。
        Properties messages = getMessages(locale);
        // 取得した全てのメッセージに新規メッセージを追加する。
        messages.setProperty(message.getCode(), message.getMessage());
        if (log.isDebugEnabled()) {
            log.debug("add Message[" + message.getMessage() + "] (code["
                    + message.getCode() + "], locale[" + locale + "])");
        }
    }

    /**
     * Localeオブジェクトを生成する。<br>
     * 言語コード、国コード、バリアントコードからLocaleオブジェクトを生成する。
     * 言語コードが与えられていない場合は、デフォルトロケールの言語コードのみ
     * を格納し、Localeオブジェクトを生成する。
     * 
     * @param message メッセージリソース
     * 
     * @return
     *      言語コード、国コード、バリアントコードを格納したLocaleオブジェクト。
     *
     * @throws IllegalArgumentException
     *      メッセージコード及びメッセージが存在するメッセージリソースに
     *      ロケールが設定されていない。かつ、デフォルトロケールも設定出来ない
     *      場合のエラー。
     */
    protected Locale createLocale(DBMessage message) {
        if (message.getLanguage() == null) {
            if (defaultLocale != null) {
                return defaultLocale;
            }
            if (log.isErrorEnabled()) {
                log.error("Can't resolve Locale.Define Locale"
                        + " in MessageSource or Defaultlocale.");
            }
            throw new IllegalArgumentException("Can't resolve Locale."
                    + "Define Locale in MessageSource or Defaultlocale.");
        }
        if (message.getCountry() == null) {
            return new Locale(message.getLanguage());
        }
        if (message.getVariant() == null) {
            return new Locale(message.getLanguage(), message.getCountry());
        }
        return new Locale(message.getLanguage(), message.getCountry(),
                          message.getVariant());
    }

    /**
     * ロケールに対応する全てのメッセージを返却する。 指定されたロケールの
     * メッセージが存在しない場合は新たに生成し、nullを返却しない。
     * 
     * @param locale
     *            メッセージのロケール。
     * 
     * @return ロケールに対応した全てのメッセージ。 メッセージコードと
     * メッセージ本体が関連付けられ、格納されている。
     */
    protected Properties getMessages(Locale locale) {
        // ロケールをキーとし、全てのメッセージを取得する 。
        Properties messages = cachedMergedProperties.get(locale);
        // ロケールに対応した全てのメッセージが存在しなかった場合、
        // 新たに作成し、cachedMergedProperties内に格納する。
        if (messages == null) {
            messages = new Properties();
            cachedMergedProperties.put(locale, messages);
        }
        return messages;
    }

    /**
     * 引数として渡されたメッセージコードとロケールからメッセージを決定し、
     * メッセージを返却する。親クラスから呼び出されるメソッド。
     * AbstractMessageSourceのメソッドをオーバーライドしている。
     * 
     * @param code
     *            メッセージコード
     * @param locale
     *            メッセージのロケール
     * 
     * @return メッセージ本体
     */
    @Override
    protected synchronized String resolveCodeWithoutArguments(
            String code,
            Locale locale) {
        String msg = internalResolveCodeWithoutArguments(code, locale);
        if (msg == null) {
            if (log.isDebugEnabled()) {
                log.debug("could not resolve [" + code + "] for locale ["
                        + locale + "]");
            }
        }
        return msg;
    }

    /**
     * メッセージコードとロケールからメッセージを決定する。 引数として与えられた
     * ロケールでメッセージの決定が出来なかった場合、ロケールを変化させ、
     * メッセージの取得を試みる。
     * また、デフォルトロケールが与えられていた場合、デフォルトロケールでの
     * メッセージの決定を最後に試みる。
     * 
     * @param code
     *            メッセージコード
     * @param locale
     *            メッセージのロケール
     * 
     * @return メッセージ本体
     */
    protected String internalResolveCodeWithoutArguments(
            String code,
            Locale locale) {
        // メッセージコードとロケールに対応したメッセージ本体をmsgに格納する。
        String msg = getMessages(locale).getProperty(code);
        // メッセージ本体の取得が出来た場合、メッセージ本体を返却する。
        if (msg != null) {
            return msg;
        }
        // メッセージ本体の取得が出来なかった場合、ロケールを変化させて
        // メッセージ本体の取得を試みる。

        // ロケールオブジェクトのパターンの生成
        List<Locale> locales = getAlternativeLocales(locale);
        // メッセージコードと新たに生成したロケールに対応したメッセージを決定し、
        // メッセージ本体を返却します。
        for (int i = 0; i < locales.size(); i++) {
            msg = getMessages(locales.get(i)).getProperty(code);
            if (msg != null) {
                return msg;
            }
        }
        // メッセージが取得できなった場合はnullを返却する。
        return null;
    }

    /**
     * メッセージを決定する際のキーを生成する。 ロケールの値から
     * ロケールオブジェクトを生成し、リストに格納、返却する。
     * １．引数localeの言語コード、国コードを持つもの。（バリアントコードを削除。）
     * ２．引数localeの言語コードを持つもの。（国コード、バリアントコードを削除。）
     * ３．デフォルトロケールの言語コード、国コード、バリアントコードを持つもの。
     * ４．デフォルトロケールの言語コード、国コードを持つもの。
     * ５．デフォルトロケールの言語コードを持つもの。
     * 
     * @param locale
     *            ロケールオブジェクト
     * 
     * @return メッセージ決定のキーとなるロケールオブジェクト
     */
    protected List<Locale> getAlternativeLocales(Locale locale) {
        List<Locale> locales = new ArrayList<Locale>();
        // ロケール内にバリアントコードが存在する場合
        if (locale.getVariant().length() > 0) {
            // Locale(language,country,"")を設定
            locales.add(new Locale(locale.getLanguage(), locale.getCountry()));
        }
        // ロケール内に国コードが存在する場合
        if (locale.getCountry().length() > 0) {
            // Locale(language,"","")を設定
            locales.add(new Locale(locale.getLanguage()));
        }
        // デフォルトロケールが設定されている場合
        if (defaultLocale != null && !locale.equals(defaultLocale)) {
            if (defaultLocale.getVariant().length() > 0) {
                // Locale(language,country,"")を設定
                locales.add(defaultLocale);
            }
            if (defaultLocale.getCountry().length() > 0) {
                // Locale(language,country,"")を設定
                locales.add(new Locale(defaultLocale.getLanguage(),
                        defaultLocale.getCountry()));
            }
            // ロケール内に国コードが存在する場合
            if (defaultLocale.getLanguage().length() > 0) {
                // Locale(language,"","")を設定
                locales.add(new Locale(defaultLocale.getLanguage()));
            }
        }
        return locales;
    }

    /**
     * 引数として渡されたメッセージコードとロケールからメッセージフォーマットを
     * 決定し、メッセージフォーマットを返却する。
     * 親クラスから呼び出されるメソッド。AbstractMessageSourceのメソッドを
     * オーバーライドしている。
     * 
     * @param code
     *            メッセージコード
     * @param locale
     *            メッセージのロケール
     * 
     * @return メッセージフォーマット
     */
    @Override
    protected synchronized MessageFormat resolveCode(
            String code,
            Locale locale) {
        // メッセージコードとロケールに対応したメッセージ本体をmessageFormatに
        // 格納する。
        MessageFormat messageFormat = getMessageFormat(code, locale);
        // メッセージ本体の取得が出来た場合、メッセージフォーマットを返却する
        if (messageFormat != null) {
            if (log.isDebugEnabled()) {
                log.debug("resolved [" + code + "] for locale [" + locale
                        + "] => [" + messageFormat + "]");
            }
            return messageFormat;
        }
        // メッセージフォーマットの取得が出来なかった場合、ロケールを変化させて
        // メッセージフォーマットの取得を試みる。

        // ロケールオブジェクトのパターンの生成
        List<Locale> locales = getAlternativeLocales(locale);
        // メッセージコードと新たに生成したロケールに対応した
        // メッセージフォーマットを決定し、メッセージフォーマットを返却します。
        for (int i = 0; i < locales.size(); i++) {
            messageFormat = getMessageFormat(code, locales.get(i));
            if (messageFormat != null) {
                if (log.isDebugEnabled()) {
                    log.debug("resolved [" + code + "] for locale [" + locale
                            + "] => [" + messageFormat + "]");
                }
                return messageFormat;
            }
        }
        if (messageFormat == null) {
            if (log.isDebugEnabled()) {
                log.debug("could not resolve [" + code + "] for locale ["
                        + locale + "]");
            }
        }
        // メッセージフォーマットが取得出来なかった場合はnullを返却する。
        return null;
    }

    /**
     * 引数として渡されたメッセージコードとロケールからメッセージフォーマット
     * を決定する。
     * 
     * @param code
     *            メッセージコード
     * @param locale
     *            メッセージのロケール
     * 
     * @return 決定されたメッセージフォーマット
     */
    protected MessageFormat getMessageFormat(String code, Locale locale) {
        // メッセージコードに対応したロケールマップを取得する。
        Map<Locale, MessageFormat> localeMap
                                   = this.cachedMessageFormats.get(code);
        // ロケールマップが存在した場合、ロケールマップよりロケールに対応する
        // メッセージフォーマットを取得、返却する。
        if (localeMap != null) {
            MessageFormat result = localeMap.get(locale);
            if (result != null) {
                return result;
            }
        }
        
        String msg = getMessages(locale).getProperty(code);

        // メッセージが存在する場合
        if (msg != null) {

            // ロケールマップが存在しない場合、新たにロケールマップを生成し、
            // メッセージフォーマットを返却する。
            if (localeMap == null) {
                localeMap = new HashMap<Locale, MessageFormat>();
                this.cachedMessageFormats.put(code, localeMap);
            }
            // メッセージとロケールよりメッセージフォーマットを作成する。
            MessageFormat result = createMessageFormat(msg, locale);
            localeMap.put(locale, result);
            return result;
        }
        // メッセージフォーマットが取得出来なかった場合はnullを返却する。
        return null;
    }
}