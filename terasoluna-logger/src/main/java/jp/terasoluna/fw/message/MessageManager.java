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
package jp.terasoluna.fw.message;

import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

import jp.terasoluna.fw.message.execption.MessageRuntimeException;

/**
 * メッセージ管理クラス
 * 
 */
public class MessageManager {
    /**
     * メッセージプロパティファイルのベースネームリスト
     */
    protected final List<String> basenames = new CopyOnWriteArrayList<String>();
    /**
     * メッセージIDのフォーマット
     */
    protected String messageIdFormat = "[%s] ";
    /**
     * メッセージIDが見つからなかった場合に例外をスローするか否か
     */
    protected boolean throwIfResourceNotFound = false;
    /**
     * メッセージフォーマッタ
     */
    protected final MessageFormatter messageFormatter;
    /**
     * メッセージIDのフォーマットのキーのデフォルト値
     */
    protected static final String DEFAULT_MESSAGE_ID_FORMAT_KEY = "message.id.format";
    /**
     * メッセージプロパティファイルベースネームのキーのデフォルト値
     */
    protected static final String DEFAULT_MESSAGE_BASE_NAME_KEY = "message.basename";
    /**
     * メッセージIDが見つからなかった場合に例外をスローするか否かのキーのデフォルト値
     */
    protected static final String DEFAULT_THROW_IF_RESOURCE_NOT_FOUND_KEY = "throw.if.resource.not.found";
    /**
     * メッセージフォーマッタのFQCNを指定するキーのデフォルト値
     */
    protected static final String DEFAULT_MESSAGE_FORMATTER_FQCN_KEY = "message.formatter.fqcn";

    /**
     * クラスローダを返却します。
     * 
     * <p>
     * 呼び出されたスレッドにコンテキスト・クラスローダが設定されている場合はそのコンテキスト・クラスローダを返却します。<br>
     * そうでない場合はこのクラスをロードしたクラスローダを返却します。
     * </p>
     * 
     * @return クラスローダ
     */
    protected static ClassLoader getClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread()
                .getContextClassLoader();
        if (contextClassLoader != null) {
            return contextClassLoader;
        }
        ClassLoader thisClassLoader = MessageManager.class.getClassLoader();
        return thisClassLoader;
    }

    /**
     * コンストラクタ
     * <p>
     * メッセージIDのフォーマットのキー、メッセージプロパティファイルベースネームのキー、 メッセージIDが見つからなかった場合に例外をスローするか否かのキー、メッセージフォーマッタのFQCNを指定するキーはデフォルト値を設定し、 {@link MessageManager#MessageManager(String, String, String, String, String)}
     * を呼び出します。
     * </p>
     * 
     * @see MessageManager#MessageManager(String, String, String, String)
     * @param configFile 設定ファイルパス(クラスローダ相対)
     */
    public MessageManager(String configFile) {
        this(configFile, DEFAULT_MESSAGE_ID_FORMAT_KEY,
                DEFAULT_MESSAGE_BASE_NAME_KEY,
                DEFAULT_THROW_IF_RESOURCE_NOT_FOUND_KEY,
                DEFAULT_MESSAGE_FORMATTER_FQCN_KEY);
    }

    /**
     * コンストラクタ
     * 
     * クラスローダからプロパティファイルを取得し、新しいメッセージマネージャーを構築します。
     * 
     * <p>
     * 引数で取得した設定ファイルパスに該当するプロパティファイルを取得します。<br>
     * プロパティファイル取得の際は以下の項目を合わせて設定します。<br>
     * （設定値がない場合は、デフォルト値を使用します。）<br>
     * ・メッセージIDのフォーマットのキー<br>
     * ・メッセージプロパティファイルのベースネームのキー<br>
     * ・メッセージID不明時の例外スロー有無<br>
     * ・メッセージフォーマッタのFQCN<br>
     * 
     * プロパティファイルから得たメッセージIDのフォーマット、例外スローフラグはメンバ変数に格納します。<br>
     * メッセージベースネームはクラスローダから取得できるものを全て追加し、文字列のリストのメンバ変数に格納します。
     * </p>
     * 
     * @param configFile 設定ファイルパス(クラスローダ相対)
     * @param messageIdFormatKey メッセージIDのフォーマットのキー
     * @param messageBaseNameKey メッセージプロパティファイルベースネームのキー
     * @param throwIfResourceNotFoundKey メッセージIDが見つからなかった場合に例外をスローするか否かのキー
     * @param messageFormatterFqcnKey メッセージフォーマッタのFQCNを指定するキー
     */
    public MessageManager(String configFile, String messageIdFormatKey,
                          String messageBaseNameKey,
                          String throwIfResourceNotFoundKey,
                          String messageFormatterFqcnKey) {
        try {
            ClassLoader cl = getClassLoader();
            {
                String format = null;
                String throwIfNotFound = null;
                String messageFormatterFqcn = null;
                // messageIdFormat,throwIfResourceNotFound,messageFormatterFqcnはクラスローダで優先度の高いもの
                try (InputStream strm = cl.getResourceAsStream(configFile)) {
                    if (strm != null) {
                        Properties p = new Properties();
                        p.load(strm);
                        format = p.getProperty(messageIdFormatKey);
                        throwIfNotFound = p.getProperty(
                                throwIfResourceNotFoundKey);
                        messageFormatterFqcn = p.getProperty(
                                messageFormatterFqcnKey);
                    }
                }
                if (format != null) {
                    messageIdFormat = format;
                }
                if (throwIfNotFound != null) {
                    throwIfResourceNotFound = Boolean
                            .parseBoolean(throwIfNotFound);
                }
                if (messageFormatterFqcn != null) {
                    try {
                        Class<?> clazz = Class.forName(messageFormatterFqcn,
                                true, cl);
                        messageFormatter = (MessageFormatter) clazz
                                .newInstance();
                    } catch (Exception e) {
                        StringBuilder sb = new StringBuilder(
                                "MessageFormatter[").append(
                                messageFormatterFqcn).append("] is not found.");
                        throw new MessageRuntimeException(sb.toString(), e);
                    }
                } else {
                    messageFormatter = new DefaultMessageFormatter();
                }
            }

            Enumeration<URL> urls = cl.getResources(configFile);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    Properties p = new Properties();
                    InputStream strm = url.openStream();
                    p.load(strm);
                    // messageBasenameはクラスローダから読み込めるものは全て追加する
                    if (p.containsKey(messageBaseNameKey)) {
                        String[] basenameArray = p.getProperty(
                                messageBaseNameKey).split(",");
                        for (String s : basenameArray) {
                            String basename = s.trim();
                            if (!"".equals(basename)) {
                                basenames.add(basename);
                            }
                        }
                    }
                }
            }
        } catch (MessageRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new MessageRuntimeException(e);
        }
    }

    /**
     * メッセージプロパティファイル内のベースネームに対応したリソースバンドルを返却します。
     * 
     * 引数のロケールがnullの場合は、デフォルトのロケースを使用します。 引数のベースネームに対応するリソースが存在しない場合は、例外スローフラグが正の場合に限り、ベースネームを文字出力します。 ベースネームの文字出力と同時に{@link MessageRuntimeException}をスローします。 例外スローフラグが負の場合は、null値を設定したリソースバンドルを返却します。
     * 
     * @param basename メッセージプロパティファイルのベースネーム
     * @param locale ロケール
     * @return リソースバンドル
     */
    protected ResourceBundle getResourceBundle(String basename, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        ResourceBundle bundle = null;
        try {
            bundle = ResourceBundle.getBundle(basename, locale);
        } catch (MissingResourceException e) {
            if (throwIfResourceNotFound) {
                StringBuilder sb = new StringBuilder("resource[").append(
                        basename).append("] is not found");
                throw new MessageRuntimeException(sb.toString(), e);
            }
        }
        return bundle;
    }

    /**
     * プロパティファイルに存在する、メッセージIDに対応したメッセージを返却します。
     * 
     * <dl>
     * <dt><code>bundle</code>が<code>false</code>の場合</dt>
     * <dd>
     * null値を返却します。
     * 
     * <dt><code>key</code>が<code>false</code>の場合</dt> null値を返却します。
     * 
     * </dl>
     * 
     * <p>
     * 引数のリソースバンドルに対して、引数のメッセージIDに対応するメッセージを取得します。 メッセージIDに対応するメッセージが存在しない場合は、例外スローフラグが正の場合に限り、取得できなかったメッセージID名を文字出力します。 メッセージID名の文字出力と同時に{@link MessageRuntimeException}をスローします。
     * 例外スローフラグが負の場合は、null値を返却します。
     * </p>
     * 
     * @param bundle リソースバンドル
     * @param key メッセージID
     * @return メッセージIDにより取得するメッセージ
     */
    protected String getStringOrNull(ResourceBundle bundle, String key) {
        if (bundle == null) {
            return null;
        }
        if (key == null) {
            if (throwIfResourceNotFound) {
                throw new MessageRuntimeException("key is null");
            }
            return null;
        }

        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    /**
     * メッセージパターンを返却します。
     * 
     * @param messageId メッセージID
     * @param locale メッセージのロケール
     * @return メッセージIDに対するメッセージパターン
     */
    protected String getMessagePattern(String messageId, Locale locale) {
        String message = null;
        for (String basename : basenames) {
            ResourceBundle bundle = getResourceBundle(basename, locale);
            message = getStringOrNull(bundle, messageId);
            if (message != null) {
                break;
            }
        }
        if (message == null && throwIfResourceNotFound) {
            StringBuilder sb = new StringBuilder("key[").append(messageId)
                    .append("] is not found");
            throw new MessageRuntimeException(sb.toString());
        }
        return message;
    }

    /**
     * メッセージを返却します。
     * 
     * ロケールにnull値を設定し、 {@link MessageManager#getMessage(boolean, String, Locale, Object...)} を呼び出します。 ロケールにはデフォルト値が使用されます。
     * 
     * @param resource リソースの有無
     * @param messageIdOrPattern メッセージID（リソース有の場合） / メッセージパターン(リソース無の場合)
     * @param args 置換パラメータ
     * @return メッセージ
     * @throws MessageRuntimeException 不正なパターンを指定した場合
     */
    public String getMessage(boolean resource, String messageIdOrPattern,
            Object... args) throws MessageRuntimeException {
        return getMessage(resource, messageIdOrPattern, null, args);
    }

    /**
     * メッセージを返却します。
     * 
     * <dl>
     * <dt><code>resource</code>が<code>true</code>の場合</dt>
     * <dd>
     * メッセージIDに対するメッセージパターンをメッセージプロパティファイルベースネームリスト対応する各プロパティファイルから探索します。<br>
     * 最初に見つかったメッセージパターンに置換パラメータを埋め込んだメッセージを返却します。パターンは {@link MessageFormat} の形式にしてください。不正なパターンを指定した場合、{@link MessageRuntimeException}をスローします<br>
     * ベースネームに対応したプロパティファイルは指定したロケールで取得します。ロケールにnullを設定した場合は {@link Locale#getDefault()}が使用されます。</dd>
     * 
     * <dt><code>resource</code>が<code>false</code>の場合</dt>
     * <dd>
     * メッセージパターン(<code>messageIdOrPattern</code>)に置換パラメータを埋め込んだメッセージを返却します。パターンは {@link MessageFormat}の形式にしてください。不正なパターンを指定した場合、 {@link MessageRuntimeException}をスローします。</dd>
     * </dl>
     * 
     * @param resource リソースの有無
     * @param messageIdOrPattern メッセージID（リソース有の場合） / メッセージパターン(リソース無の場合)
     * @param locale ロケール
     * @param args 置換パラメータ
     * @return メッセージ
     * @throws MessageRuntimeException 不正なパターンを指定した場合
     */
    public String getMessage(boolean resource, String messageIdOrPattern,
            Locale locale, Object... args) throws MessageRuntimeException {
        String pattern = null;
        StringBuilder message = new StringBuilder();

        if (resource) {
            pattern = getMessagePattern(messageIdOrPattern, locale);
            message.append(String.format(messageIdFormat, messageIdOrPattern));
        } else {
            pattern = messageIdOrPattern;
        }

        if (pattern != null) {
            try {
                String body = messageFormatter.format(pattern, args);
                message.append(body);
            } catch (IllegalArgumentException e) {
                StringBuilder sb = new StringBuilder(
                        "message pattern is illeagal. pattern=")
                        .append(pattern).append("]");
                if (resource) {
                    sb.append(" logId=");
                    sb.append(messageIdOrPattern);
                }
                throw new MessageRuntimeException(sb.toString(), e);
            }
        }
        return message.toString();
    }
}
