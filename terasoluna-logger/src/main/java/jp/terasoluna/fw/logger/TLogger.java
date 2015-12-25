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
package jp.terasoluna.fw.logger;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.terasoluna.fw.message.MessageManager;

/**
 * 汎用ロガークラス。<br>
 * <p>
 * CommonsLoggingのロガーをラップしたロガーです。<br>
 * ログメッセージをプロパティファイルで管理し、ログ出力メソッドの引数にログIDを渡すことで、プロパティファイル中のログIDに対するメッセージを出力できます。
 * </p>
 * <h3>ロガー取得</h3>
 * <p>
 * ロガーの取得は他のロガーライブラリとほぼ同じです。
 * </p>
 * 
 * <pre>
 * TLogger logger = TLogger.getLogger(XX.class);
 * または
 * TLogger logger = TLogger.getLogger("カテゴリ名");
 * </pre>
 * 
 * <h3>ログ出力</h3>
 * <p>
 * 次のようなログメッセージプロパティファイルがある場合
 * </p>
 * 
 * <pre>
 * DEB001=debug message
 * ERR001=error message
 * </pre>
 * <p>
 * このメッセージを出力するには
 * </p>
 * 
 * <pre>
 * logger.debug(&quot;DEB001&quot;);
 * logger.error(&quot;ERR001&quot;);
 * </pre>
 * <p>
 * のようにログレベルに応じたメソッドにログIDを渡して実行します。 出力メッセージは
 * </p>
 * 
 * <pre>
 * [DEB001] debug message
 * [ERR001] error message
 * </pre>
 * <p>
 * となります。メッセージの前に[ログID]が自動で付きます。<br>
 * 
 * ログレベルは
 * <ul>
 * <li>FATAL</li>
 * <li>ERROR</li>
 * <li>WARN</li>
 * <li>INFO</li>
 * <li>DEBUG</li>
 * <li>TRACE</li>
 * </ul>
 * があります。<code>log(String logId)</code>メソッドを使用すると、ログIDの一文字目を見てログレベルを判断します。
 * </p>
 * <h3>パラメータ置換</h3>
 * <p>
 * 出力するログメッセージを作成する際に、{@link java.text.MessageFormat}
 * を使用しています。置換パラメータを可変長配列で渡すことができます。
 * </p>
 * 
 * <pre>
 * DEB002={0} is {1}.
 * </pre>
 * <p>
 * という定義がある場合、
 * </p>
 * 
 * <pre>
 * logger.debug(&quot;DEB002&quot;, &quot;hoge&quot;, &quot;foo&quot;);
 * </pre>
 * <p>
 * を実行すると出力メッセージは
 * </p>
 * 
 * <pre>
 * [DEB002] hoge is foo.
 * </pre>
 * <p>
 * となります。 <br>
 * 内部でメッセージ文字列を作成する際にログレベルのチェックを行っているので、
 * </p>
 * 
 * <pre>
 * if (logger.isDebugEnabled()) {
 *     logger.debug(&quot;DEB002&quot;, &quot;hoge&quot;, &quot;foo&quot;);
 * }
 * </pre>
 * <p>
 * というif文を書く必要がありません。(ただし、
 * パラメータを作成する際にメソッドを呼び出している場合はif文を書いて明示的にログレベルチェックを行ってください。)
 * </p>
 * <h3>メッセージプロパティファイルにないメッセージの出力</h3>
 * <p>
 * メッセージプロパティファイル中のログIDを渡す他に、直接メッセージを渡す方法があります。第1引数にfalse
 * を設定し、第2引数にメッセージ本文を直接記述できます。第3引数以降は置換パラメータです。この場合は当然ログIDは出力されません。
 * </p>
 * 
 * <pre>
 * logger.warn(false, &quot;warn!!&quot;);
 * logger.info(false, &quot;Hello {0}!&quot;, &quot;World&quot;);
 * </pre>
 * <p>
 * 出力メッセージは
 * </p>
 * 
 * <pre>
 * warn!!
 * Hello World!
 * </pre>
 * <p>
 * となります。
 * </p>
 * <h3>設定ファイル</h3>
 * <p>
 * クラスパス直下の<code>META-INF</code>ディレクトリに<code>terasoluna-logger.properties</code>
 * を作成してください。
 * </p>
 * <h4>メッセージプロパティファイルのベースネーム設定</h4>
 * <p>
 * <code>terasoluna-logger.properties</code>の<code>message.basename</code>
 * キーにメッセージプロパティファイルのベースネームをクラスパス相対(FQCN)で設定してください。<br>
 * {@link java.util.ResourceBundle}で読み込むので、国際化に対応しています。
 * </p>
 * 
 * <pre>
 * message.basename = hoge
 * </pre>
 * <p>
 * と書くとクラスパス直下のhoge.propertiesが読み込まれます。
 * </p>
 * 
 * <pre>
 * message.basename=hoge,foo,bar
 * </pre>
 * <p>
 * のように半角カンマ区切りで設定すると全てを読み込みます。<br>
 * <code>META-INF/terasoluna-logger.properies</code>の
 * <code>message.basename</code>はモジュール毎に設定できます。 ロガーは全てのモジュール(jar)が持つ、
 * <code>message.basename</code>の値をマージしてメッセージを取得します。 <br>
 * これにより、モジュール毎にログメッセージを管理することができます。
 * </p>
 * <h4>出力ログIDフォーマット設定</h4>
 * <p>
 * ログ出力時に自動で付加されるログIDのフォーマットを設定できます。<br>
 * <code>message.id.format</code>キーに
 * {@link java.lang.String#format(String, Object...)}
 * のフォーマット形式で設定してください。ログIDが文字列として渡されます。 <br>
 * 設定しない場合は「[%s]」がデフォルト値として使用されます。 <br>
 * </p>
 * 
 * <pre>
 * message.id.format=[%-8s]
 * </pre>
 * <p>
 * のように設定すると、モジュール間で異なる長さのログIDを左寄せで揃えて出力できます。 <br>
 * この設定値はモジュール毎に管理することはできません。 <br>
 * クラスローダの読み込み優先度が一番高い<code>terasoluna-logger.properties</code>の値が反映されます。
 * (通常、アプリ側の設定となります。)
 * </p>
 */
public class TLogger implements Log {
    /**
     * ロガー実体。
     */
    private final Log logger;

    /**
     * ロガー設定ファイル。
     */
    private static final String CONFIG_FILENAME = "META-INF/terasoluna-logger.properties";
    /**
     * メッセージ管理。
     */
    private static final MessageManager MESSAGE_MANAGER = new MessageManager(
            CONFIG_FILENAME);

    /**
     * ロケールを保持するスレッドローカル。
     */
    private static final ThreadLocal<Locale> locale = new ThreadLocal<Locale>();

    /**
     * コンストラクタ。
     * 
     * @param clazz カテゴリ名となるクラス
     */
    protected TLogger(Class<?> clazz) {
        logger = LogFactory.getLog(clazz);
    }

    /**
     * コンストラクタ。
     * 
     * @param name カテゴリ名
     */
    protected TLogger(String name) {
        logger = LogFactory.getLog(name);
    }

    /**
     * ロケールを設定します。
     * 
     * @param locale ロケール
     */
    public static void setLocale(Locale locale) {
        TLogger.locale.set(locale);
    }

    /**
     * ログメッセージを作成します。
     * 
     * @param resource リソース有無
     * @param logIdOrPattern ログID（リソース有の場合） / ログメッセージパターン(リソース無の場合)
     * @param args 置換パラメータ
     * @return ログメッセージ文字列
     * @see {@link MessageManager#getMessage(boolean, String, Locale, Object...)}
     */
    protected String createMessage(boolean resource, String logIdOrPattern,
            Object... args) {
        String message = MESSAGE_MANAGER.getMessage(resource, logIdOrPattern,
                locale.get(), args);
        return message;
    }

    /**
     * ロガーを取得します。
     * 
     * @param clazz カテゴリ名となるクラス
     * @return ロガー
     */
    public static TLogger getLogger(Class<?> clazz) {
        return new TLogger(clazz);
    }

    /**
     * ロガーを取得します。
     * 
     * @param name カテゴリ名
     * @return ロガー
     */
    public static TLogger getLogger(String name) {
        return new TLogger(name);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isFatalEnabled() {
        return logger.isFatalEnabled();
    }

    /**
     * FATALログを出力します。
     * 
     * @param resource リソース有無
     * @param logIdOrPattern ログID（リソース有の場合） / ログメッセージパターン(リソース無の場合)
     * @param args 置換パラメータ
     */
    public void fatal(boolean resource, String logIdOrPattern, Object... args) {
        if (isFatalEnabled()) {
            String message = createMessage(resource, logIdOrPattern, args);
            logger.fatal(message);
        }
    }

    /**
     * FATALログを出力します。
     * 
     * @param resource リソース有無
     * @param logIdOrPattern ログID（リソース有の場合） / ログメッセージパターン(リソース無の場合)
     * @param throwable 起因例外
     * @param args 置換パラメータ
     */
    public void fatal(boolean resource, String logIdOrPattern,
            Throwable throwable, Object... args) {
        if (isFatalEnabled()) {
            String message = createMessage(resource, logIdOrPattern, args);
            logger.fatal(message, throwable);
        }
    }

    /**
     * FATALログを出力します。
     * 
     * @param logId ログID
     * @param args 置換パラメータ
     */
    public void fatal(String logId, Object... args) {
        fatal(true, logId, args);
    }

    /**
     * FATALログを出力します。
     * 
     * @param logId ログID
     * @param throwable 起因例外
     * @param args 置換パラメータ
     */
    public void fatal(String logId, Throwable throwable, Object... args) {
        fatal(true, logId, throwable, args);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    /**
     * ERRORログを出力します。<br>
     * <p>
     * ロガーのログレベルがERRORより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param resource リソース有無
     * @param logIdOrPattern ログID（リソース有の場合） / ログメッセージパターン(リソース無の場合)
     * @param args 置換パラメータ
     */
    public void error(boolean resource, String logIdOrPattern, Object... args) {
        if (isErrorEnabled()) {
            String message = createMessage(resource, logIdOrPattern, args);
            logger.error(message);
        }
    }

    /**
     * ERRORログを出力します。<br>
     * <p>
     * ロガーのログレベルがERRORより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param resource リソース有無
     * @param logIdOrPattern ログID（リソース有の場合） / ログメッセージパターン(リソース無の場合)
     * @param throwable 起因例外
     * @param args 置換パラメータ
     */
    public void error(boolean resource, String logIdOrPattern,
            Throwable throwable, Object... args) {
        if (isErrorEnabled()) {
            String message = createMessage(resource, logIdOrPattern, args);
            logger.error(message, throwable);
        }
    }

    /**
     * ERRORログを出力します。<br>
     * <p>
     * ロガーのログレベルがERRORより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param args 置換パラメータ
     */
    public void error(String logId, Object... args) {
        error(true, logId, args);
    }

    /**
     * ERRORログを出力します。<br>
     * <p>
     * ロガーのログレベルがERRORより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param throwable 起因例外
     * @param args 置換パラメータ
     */
    public void error(String logId, Throwable throwable, Object... args) {
        error(true, logId, throwable, args);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    /**
     * WARNログを出力します。<br>
     * <p>
     * ロガーのログレベルがWARNより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param resource リソース有無
     * @param logIdOrPattern ログID（リソース有の場合） / ログメッセージパターン(リソース無の場合)
     * @param args 置換パラメータ
     */
    public void warn(boolean resource, String logIdOrPattern, Object... args) {
        if (isWarnEnabled()) {
            String message = createMessage(resource, logIdOrPattern, args);
            logger.warn(message);
        }
    }

    /**
     * WARNログを出力します。<br>
     * <p>
     * ロガーのログレベルがWARNより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param resource リソース有無
     * @param logIdOrPattern ログID（リソース有の場合） / ログメッセージパターン(リソース無の場合)
     * @param throwable 起因例外
     * @param args 置換パラメータ
     */
    public void warn(boolean resource, String logIdOrPattern,
            Throwable throwable, Object... args) {
        if (isWarnEnabled()) {
            String message = createMessage(resource, logIdOrPattern, args);
            logger.warn(message, throwable);
        }
    }

    /**
     * WARNログを出力します。<br>
     * <p>
     * ロガーのログレベルがWARNより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId 　ログID
     * @param args 置換パラメータ
     */
    public void warn(String logId, Object... args) {
        warn(true, logId, args);
    }

    /**
     * WARNログを出力します。<br>
     * <p>
     * ロガーのログレベルがWARNより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param throwable 起因例外
     * @param args 置換パラメータ
     */
    public void warn(String logId, Throwable throwable, Object... args) {
        warn(true, logId, throwable, args);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    /**
     * INFOログを出力します。<br>
     * <p>
     * ロガーのログレベルがINFOより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param resource リソース有無
     * @param logIdOrPattern ログID（リソース有の場合） / ログメッセージパターン(リソース無の場合)
     * @param args 置換パラメータ
     */
    public void info(boolean resource, String logIdOrPattern, Object... args) {
        if (isInfoEnabled()) {
            String message = createMessage(resource, logIdOrPattern, args);
            logger.info(message);
        }
    }

    /**
     * INFOログを出力します。<br>
     * <p>
     * ロガーのログレベルがINFOより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param resource リソース有無
     * @param logIdOrPattern ログID（リソース有の場合） / ログメッセージパターン(リソース無の場合)
     * @param throwable 起因例外
     * @param args 置換パラメータ
     */
    public void info(boolean resource, String logIdOrPattern,
            Throwable throwable, Object... args) {
        if (isInfoEnabled()) {
            String message = createMessage(resource, logIdOrPattern, args);
            logger.info(message, throwable);
        }
    }

    /**
     * INFOログを出力します。<br>
     * <p>
     * ロガーのログレベルがINFOより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param args 置換パラメータ
     */
    public void info(String logId, Object... args) {
        info(true, logId, args);
    }

    /**
     * INFOログを出力します。<br>
     * <p>
     * ロガーのログレベルがINFOより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param throwable 起因例外
     * @param args 置換パラメータ
     */
    public void info(String logId, Throwable throwable, Object... args) {
        info(true, logId, throwable, args);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    /**
     * DEBUGログを出力します。<br>
     * <p>
     * ロガーのログレベルがDEBUGより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param resource リソース有無
     * @param logIdOrPattern ログID（リソース有の場合） / ログメッセージパターン(リソース無の場合)
     * @param args 置換パラメータ
     */
    public void debug(boolean resource, String logIdOrPattern, Object... args) {
        if (isDebugEnabled()) {
            String message = createMessage(resource, logIdOrPattern, args);
            logger.debug(message);
        }
    }

    /**
     * DEBUGログを出力します。<br>
     * <p>
     * ロガーのログレベルがDEBUGより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param resource リソース有無
     * @param logIdOrPattern ログID（リソース有の場合） / ログメッセージパターン(リソース無の場合)
     * @param throwable 起因例外
     * @param args 置換パラメータ
     */
    public void debug(boolean resource, String logIdOrPattern,
            Throwable throwable, Object... args) {
        if (isDebugEnabled()) {
            String message = createMessage(resource, logIdOrPattern, args);
            logger.debug(message, throwable);
        }
    }

    /**
     * DEBUGログを出力します。<br>
     * <p>
     * ロガーのログレベルがDEBUGより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param args 置換パラメータ
     */
    public void debug(String logId, Object... args) {
        debug(true, logId, args);
    }

    /**
     * DEBUGログを出力します。<br>
     * <p>
     * ロガーのログレベルがDEBUGより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param throwable 起因例外
     * @param args 置換パラメータ
     */
    public void debug(String logId, Throwable throwable, Object... args) {
        debug(true, logId, throwable, args);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    /**
     * TRACEログを出力します。<br>
     * <p>
     * ロガーのログレベルがTRACEより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param resource リソース有無
     * @param logIdOrPattern ログID（リソース有の場合） / ログメッセージパターン(リソース無の場合)
     * @param args 置換パラメータ
     */
    public void trace(boolean resource, String logIdOrPattern, Object... args) {
        if (isTraceEnabled()) {
            String message = createMessage(resource, logIdOrPattern, args);
            logger.trace(message);
        }
    }

    /**
     * TRACEログを出力します。<br>
     * <p>
     * ロガーのログレベルがTRACEより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param resource リソース有無
     * @param logIdOrPattern ログID（リソース有の場合） / ログメッセージパターン(リソース無の場合)
     * @param throwable 起因例外
     * @param args 置換パラメータ
     */
    public void trace(boolean resource, String logIdOrPattern,
            Throwable throwable, Object... args) {
        if (isTraceEnabled()) {
            String message = createMessage(resource, logIdOrPattern, args);
            logger.trace(message, throwable);
        }
    }

    /**
     * TRACEログを出力します。<br>
     * <p>
     * ロガーのログレベルがTRACEより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param args 置換パラメータ
     */
    public void trace(String logId, Object... args) {
        trace(true, logId, args);
    }

    /**
     * TRACEログを出力します。<br>
     * <p>
     * ロガーのログレベルがTRACEより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param throwable 起因例外
     * @param args 置換パラメータ
     */
    public void trace(String logId, Throwable throwable, Object... args) {
        trace(true, logId, throwable, args);
    }

    /**
     * ログIDの以下の規約に合ったレベルのログを出力します。<br>
     * <p>
     * ログIDの先頭文字が
     * <ul>
     * <li>T...TRACEログ</li>
     * <li>D...DEBUGログ</li>
     * <li>I...INFOログ</li>
     * <li>W...WARNログ</li>
     * <li>E...ERRORログ</li>
     * <li>F...FATALログ</li>
     * <li>それ以外...DEBUGログ</li> </u>
     * </p>
     * 
     * @param logId ログID
     * @param args 置換パラメータ
     */
    public void log(String logId, Object... args) {
        if (logId != null && logId.length() > 0) {
            char messageType = logId.charAt(0);
            switch (messageType) {
            case 'T':
                trace(logId, args);
                break;
            case 'D':
                debug(logId, args);
                break;
            case 'I':
                info(logId, args);
                break;
            case 'W':
                warn(logId, args);
                break;
            case 'E':
                error(logId, args);
                break;
            case 'F':
                fatal(logId, args);
                break;
            default:
                debug(logId, args);
                break;
            }
        }
    }

    /**
     * ログIDの以下の規約に合ったレベルのログを出力します。<br>
     * <p>
     * ログIDの先頭文字が
     * <ul>
     * <li>T...TRACEログ</li>
     * <li>D...DEBUGログ</li>
     * <li>I...INFOログ</li>
     * <li>W...WARNログ</li>
     * <li>E...ERRORログ</li>
     * <li>F...FATALログ</li>
     * <li>それ以外...DEBUGログ</li> </u>
     * </p>
     * 
     * @param logId ログID
     * @param args 置換パラメータ
     */
    public void log(String logId, Throwable throwable, Object... args) {
        if (logId != null && logId.length() > 0) {
            char messageType = logId.charAt(0);
            switch (messageType) {
            case 'T':
                trace(logId, throwable, args);
                break;
            case 'D':
                debug(logId, throwable, args);
                break;
            case 'I':
                info(logId, throwable, args);
                break;
            case 'W':
                warn(logId, throwable, args);
                break;
            case 'E':
                error(logId, throwable, args);
                break;
            case 'F':
                fatal(logId, throwable, args);
                break;
            default:
                debug(logId, throwable, args);
                break;
            }
        }
    }

    /**
     * TRACEログを出力します。<br>
     * <p>
     * CommonsLoggingのLogインタフェースを実装するためのAPIであり、使用しないでください。<br>
     * 代わりに{@link #trace(String, Object...)}を使用してください。
     * </p>
     * 
     * @param message メッセージ
     * 
     */
    @Deprecated
    public void trace(Object message) {
        trace(false, "{0}", message);
    }

    /**
     * TRACEログを出力します。<br>
     * <p>
     * CommonsLoggingのLogインタフェースを実装するためのAPIであり、使用しないでください。<br>
     * 代わりに{@link #trace(String, Throwable, Object...)}を使用してください。
     * </p>
     * 
     * @param message メッセージ
     * @param t 起因例外
     * 
     */
    @Deprecated
    public void trace(Object message, Throwable t) {
        trace(false, "{0}", t, message);
    }

    /**
     * DEBUGログを出力します。<br>
     * <p>
     * CommonsLoggingのLogインタフェースを実装するためのAPIであり、使用しないでください。<br>
     * 代わりに{@link #debug(String, Object...)}を使用してください。
     * </p>
     * 
     * @param message メッセージ
     * 
     */
    @Deprecated
    public void debug(Object message) {
        debug(false, "{0}", message);
    }

    /**
     * DEBUGログを出力します。<br>
     * <p>
     * CommonsLoggingのLogインタフェースを実装するためのAPIであり、使用しないでください。<br>
     * 代わりに{@link #debug(String, Throwable, Object...)}を使用してください。
     * </p>
     * 
     * @param message メッセージ
     * @param t 起因例外
     * 
     */
    @Deprecated
    public void debug(Object message, Throwable t) {
        debug(false, "{0}", t, message);
    }

    /**
     * INFOログを出力します。<br>
     * <p>
     * CommonsLoggingのLogインタフェースを実装するためのAPIであり、使用しないでください。<br>
     * 代わりに{@link #info(String, Object...)}を使用してください。
     * </p>
     * 
     * @param message メッセージ
     * 
     */
    @Deprecated
    public void info(Object message) {
        info(false, "{0}", message);
    }

    /**
     * INFOログを出力します。<br>
     * <p>
     * CommonsLoggingのLogインタフェースを実装するためのAPIであり、使用しないでください。<br>
     * 代わりに{@link #info(String, Throwable, Object...)}を使用してください。
     * </p>
     * 
     * @param message メッセージ
     * @param t 起因例外
     * 
     */
    @Deprecated
    public void info(Object message, Throwable t) {
        info(false, "{0}", t, message);
    }

    /**
     * WARNログを出力します。<br>
     * <p>
     * CommonsLoggingのLogインタフェースを実装するためのAPIであり、使用しないでください。<br>
     * 代わりに{@link #warn(String, Object...)}を使用してください。
     * </p>
     * 
     * @param message メッセージ
     * 
     */
    @Deprecated
    public void warn(Object message) {
        warn(false, "{0}", message);
    }

    /**
     * WARNログを出力します。<br>
     * <p>
     * CommonsLoggingのLogインタフェースを実装するためのAPIであり、使用しないでください。<br>
     * 代わりに{@link #warn(String, Throwable, Object...)}を使用してください。
     * </p>
     * 
     * @param message メッセージ
     * @param t 起因例外
     * 
     */
    @Deprecated
    public void warn(Object message, Throwable t) {
        warn(false, "{0}", t, message);
    }

    /**
     * ERRORログを出力します。<br>
     * <p>
     * CommonsLoggingのLogインタフェースを実装するためのAPIであり、使用しないでください。<br>
     * 代わりに{@link #error(String, Object...)}を使用してください。
     * </p>
     * 
     * @param message メッセージ
     * 
     */
    @Deprecated
    public void error(Object message) {
        error(false, "{0}", message);
    }

    /**
     * ERRORログを出力します。<br>
     * <p>
     * CommonsLoggingのLogインタフェースを実装するためのAPIであり、使用しないでください。<br>
     * 代わりに{@link #error(String, Throwable, Object...)}を使用してください。
     * </p>
     * 
     * @param message メッセージ
     * @param t 起因例外
     * 
     */
    @Deprecated
    public void error(Object message, Throwable t) {
        error(false, "{0}", t, message);
    }

    /**
     * FATALログを出力します。<br>
     * <p>
     * CommonsLoggingのLogインタフェースを実装するためのAPIであり、使用しないでください。<br>
     * 代わりに{@link #fatal(String, Object...)}を使用してください。
     * </p>
     * 
     * @param message メッセージ
     * 
     */
    @Deprecated
    public void fatal(Object message) {
        fatal(false, "{0}", message);
    }

    /**
     * FATALログを出力します。<br>
     * <p>
     * CommonsLoggingのLogインタフェースを実装するためのAPIであり、使用しないでください。<br>
     * 代わりに{@link #fatal(String, Throwable, Object...)}を使用してください。
     * </p>
     * 
     * @param message メッセージ
     * @param t 起因例外
     * 
     */
    @Deprecated
    public void fatal(Object message, Throwable t) {
        fatal(false, "{0}", t, message);
    }

    /**
     * ログメッセージを取得します。
     * 
     * @param logId ログID
     * @param args 置換パラメータ
     * @return ログメッセージ
     */
    public String getLogMessage(String logId, Object... args) {
        String message = createMessage(true, logId, args);
        return message;
    }

    // CommonsLoggingのLogインタフェースで利用するためのAPIを用意したための対応

    /**
     * TRACEログを出力します。<br>
     * <p>
     * ロガーのログレベルがTRACEより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     */
    public void trace(String logId) {
        trace(logId, (Object[]) null);
    }

    /**
     * TRACEログを出力します。<br>
     * <p>
     * ロガーのログレベルがTRACEより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param throwable 起因例外
     */
    public void trace(String logId, Throwable throwable) {
        trace(logId, throwable, (Object[]) null);
    }

    /**
     * DEBUGログを出力します。<br>
     * <p>
     * ロガーのログレベルがDEBUGより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     */
    public void debug(String logId) {
        debug(logId, (Object[]) null);
    }

    /**
     * DEBUGログを出力します。<br>
     * <p>
     * ロガーのログレベルがDEBUGより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param throwable 起因例外
     */
    public void debug(String logId, Throwable throwable) {
        debug(logId, throwable, (Object[]) null);
    }

    /**
     * WARNログを出力します。<br>
     * <p>
     * ロガーのログレベルがWARNより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     */
    public void warn(String logId) {
        warn(logId, (Object[]) null);
    }

    /**
     * TRACEログを出力します。<br>
     * <p>
     * ロガーのログレベルがTRACEより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param throwable 起因例外
     */
    public void warn(String logId, Throwable throwable) {
        warn(logId, throwable, (Object[]) null);
    }

    /**
     * INFOログを出力します。<br>
     * <p>
     * ロガーのログレベルがINFOより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     */
    public void info(String logId) {
        info(logId, (Object[]) null);
    }

    /**
     * INFOログを出力します。<br>
     * <p>
     * ロガーのログレベルがINFOより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param throwable 起因例外
     */
    public void info(String logId, Throwable throwable) {
        info(logId, throwable, (Object[]) null);
    }

    /**
     * ERRORログを出力します。<br>
     * <p>
     * ロガーのログレベルがERRORより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     */
    public void error(String logId) {
        error(logId, (Object[]) null);
    }

    /**
     * ERRORログを出力します。<br>
     * <p>
     * ロガーのログレベルがERRORより高い場合は出力されません。<br>
     * 詳細は{@link TLogger}を参照してください。
     * </p>
     * 
     * @param logId ログID
     * @param throwable 起因例外
     */
    public void error(String logId, Throwable throwable) {
        error(logId, throwable, (Object[]) null);
    }

    /**
     * FATALログを出力します。
     * 
     * @param logId ログID
     */
    public void fatal(String logId) {
        fatal(logId, (Object[]) null);
    }

    /**
     * FATALログを出力します。
     * 
     * @param logId ログID
     * @param throwable 起因例外
     */
    public void fatal(String logId, Throwable throwable) {
        fatal(logId, throwable, (Object[]) null);
    }
}
