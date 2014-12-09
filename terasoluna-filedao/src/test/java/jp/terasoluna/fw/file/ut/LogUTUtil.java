/*
 * $Id: LogUTUtil.java 5230 2007-09-28 10:04:13Z anh $
 *
 * Copyright (c) 2005 NTT DATA Corporation
 */
package jp.terasoluna.fw.file.ut;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

/**
 * 試験用のユーティリティクラス。
 * <p>
 * <code>Log</code> インターフェース実装クラスであり <code>LogFactory</code> クラスでもある。。
 * </p>
 * @author 窪田 康大
 * @version 1.1 2005/03/01
 */
public class LogUTUtil extends LogFactoryImpl implements Log {

    // ------------------------------------------------------- Test Properties

    /**
     * ログの出力結果を格納した <code>List
     * </code> オブジェクト。
     */
    private final static List<LogObject> __logObjects = Collections
            .synchronizedList(new ArrayList<LogObject>());

    // ----------------------------------------------------------- Test Method

    /**
     * 蓄積されたログオブジェクトを消去する。
     */
    public static void initialize() {
        __logObjects.clear();
    }

    /**
     * 出力されたログのチェックを行う。
     * <p>
     * <code>fatal, error, warn, info, debug, trace</code> の順にログの出力確認を行う。
     * </p>
     * @param message 出力されているメッセージ。
     * @return 指定されたメッセージがログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean check(Object message) {
        return check(message, null);
    }

    /**
     * 出力されたログのチェックを行う。
     * <p>
     * <code>fatal, error, warn, info, debug, trace</code> の順にログの出力確認を行う。
     * </p>
     * @param message 出力されているメッセージ。
     * @param t 出力されている例外。
     * @return 指定されたメッセージと例外がログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean check(Object message, Throwable t) {
        return checkFatal(message, t) || checkError(message, t)
                || checkWarn(message, t) || checkInfo(message, t)
                || checkDebug(message, t) || checkTrace(message, t);
    }

    /**
     * <code>debug</code> レベルで出力されたログのチェックを行う。
     * @param message 出力されているメッセージ。
     * @return 指定されたメッセージがログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean checkDebug(Object message) {
        return checkDebug(message, null);
    }

    /**
     * <code>debug</code> レベルで出力されたログのチェックを行う。
     * @param message 出力されているメッセージ。
     * @param t 出力されている例外。
     * @return 指定されたメッセージと例外がログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean checkDebug(Object message, Throwable t) {
        return searchLogObject(message, t, LogObject.DEBUG);
    }

    /**
     * <code>error</code> レベルで出力されたログのチェックを行う。
     * @param message 出力されているメッセージ。
     * @return 指定されたメッセージがログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean checkError(Object message) {
        return checkError(message, null);
    }

    /**
     * <code>error</code> レベルで出力されたログのチェックを行う。
     * @param message 出力されているメッセージ。
     * @param t 出力されている例外。
     * @return 指定されたメッセージと例外がログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean checkError(Object message, Throwable t) {
        return searchLogObject(message, t, LogObject.ERROR);
    }

    /**
     * <code>fatal</code> レベルで出力されたログのチェックを行う。
     * @param message 出力されているメッセージ。
     * @return 指定されたメッセージがログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean checkFatal(Object message) {
        return checkFatal(message, null);
    }

    /**
     * <code>fatal</code> レベルで出力されたログのチェックを行う。
     * @param message 出力されているメッセージ。
     * @param t 出力されている例外。
     * @return 指定されたメッセージと例外がログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean checkFatal(Object message, Throwable t) {
        return searchLogObject(message, t, LogObject.FATAL);
    }

    /**
     * <code>info</code> レベルで出力されたログのチェックを行う。
     * @param message 出力されているメッセージ。
     * @return 指定されたメッセージがログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean checkInfo(Object message) {
        return checkInfo(message, null);
    }

    /**
     * <code>info</code> レベルで出力されたログのチェックを行う。
     * @param message 出力されているメッセージ。
     * @param t 出力されている例外。
     * @return 指定されたメッセージと例外がログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean checkInfo(Object message, Throwable t) {
        return searchLogObject(message, t, LogObject.INFO);
    }

    /**
     * <code>trace</code> レベルで出力されたログのチェックを行う。
     * @param message 出力されているメッセージ。
     * @return 指定されたメッセージがログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean checkTrace(Object message) {
        return checkTrace(message, null);
    }

    /**
     * <code>trace</code> レベルで出力されたログのチェックを行う。
     * @param message 出力されているメッセージ。
     * @param t 出力されている例外。
     * @return 指定されたメッセージと例外がログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean checkTrace(Object message, Throwable t) {
        return searchLogObject(message, t, LogObject.TRACE);
    }

    /**
     * <code>warn</code> レベルで出力されたログのチェックを行う。
     * @param message 出力されているメッセージ。
     * @return 指定されたメッセージがログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean checkWarn(Object message) {
        return checkWarn(message, null);
    }

    /**
     * <code>warn</code> レベルで出力されたログのチェックを行う。
     * @param message 出力されているメッセージ。
     * @param t 出力されている例外。
     * @return 指定されたメッセージと例外がログ出力されていた場合は、 <code>true</code>。
     */
    public static boolean checkWarn(Object message, Throwable t) {
        return searchLogObject(message, t, LogObject.WARN);
    }

    /**
     * このメソッドを呼び出したスレッドが、どのテストケースメソッドから発生したものかを調べる。
     * @return このメソッドを呼び出したスレッドの起動メソッド。
     */
    @SuppressWarnings("unchecked")
    protected static Method getCalledTestCaseMethod() {
        StackTraceElement[] elements = new Throwable().getStackTrace();
        for (int index = 0; index < elements.length; index++) {

            StackTraceElement stackTraceElement = elements[index];
            String elementClassName = stackTraceElement.getClassName();
            String elementMethodName = stackTraceElement.getMethodName();

            Class elementClass = null;
            try {
                elementClass = Class.forName(elementClassName);
            } catch (ClassNotFoundException e) {
                // do nothing.
            }

            Class targetClass = elementClass;
            while (targetClass != null) {
                if (TestCase.class.getName().equals(targetClass.getName())) {

                    if (elementMethodName.startsWith("test")) {
                        try {
                            return elementClass.getDeclaredMethod(
                                    elementMethodName, (Class[]) null);
                        } catch (SecurityException e) {
                            // do nothing.
                        } catch (NoSuchMethodException e) {
                            // do nothing.
                        }
                    }
                }

                targetClass = targetClass.getSuperclass();
            }
        }

        return null;
    }

    /**
     * ログのキューを保存し、標準出力にログを出力する。
     * @param message ログメッセージ
     * @param t トレース対象の例外
     * @param level ログレベル
     * @param logName <code>Log</code> インスタンスの名前
     */
    protected synchronized static void recordLogObject(Object message,
            Throwable t, String level, String logName) {
        LogObject logObject = new LogObject(getCalledTestCaseMethod(), message,
                t, level);

        __logObjects.add(logObject);

        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(logName);
        buffer.append("]");
        buffer.append(logObject.toString());

        System.out.println(buffer);
    }

    /**
     * 出力されたログのチェックを行う。
     * @param message 出力されているメッセージ。
     * @param t 出力されている例外。
     * @param level ログレベル。
     * @return 指定されたメッセージがログ出力されていた場合は、 <code>true</code>。
     */
    protected static boolean searchLogObject(Object message, Throwable t,
            String level) {
        LogObject logObject = new LogObject(getCalledTestCaseMethod(), message,
                t, level);
        boolean result = __logObjects.contains(logObject);
        __logObjects.remove(logObject);
        return result;
    }

    // ---------------------------------------------------- Logging Properties

    /**
     * このログインスタンス名。
     */
    private String _name;

    // ------------------------------------------------ LogFactory Constructor

    /**
     * コンストラクタ。
     */
    public LogUTUtil() {
        super();
        _name = LogUTUtil.class.getName();
    }

    // --------------------------------------------------- Logging Constructor

    /**
     * コンストラクタ。
     * @param name ログの対象となるクラス名
     */
    public LogUTUtil(String name) {
        super();
        _name = name;
    }

    // -------------------------------------------------------- Logging Method

    /**
     * <code>debug</code> レベルでロギング処理を行なう。
     * @param message 出力するメッセージ
     * @see org.apache.commons.logging.Log#debug(java.lang.Object)
     */
    public void debug(Object message) {
        debug(message, null);
    }

    /**
     * <code>debug</code> レベルでエラーロギング処理を行なう。
     * @param message 出力するメッセージ
     * @param t 原因となった例外
     * @see org.apache.commons.logging.Log#debug(java.lang.Object, java.lang.Throwable)
     */
    public void debug(Object message, Throwable t) {
        recordLogObject(message, t, LogObject.DEBUG, _name);
    }

    /**
     * <code>error</code> レベルでロギング処理を行なう。
     * @param message 出力するメッセージ
     * @see org.apache.commons.logging.Log#error(java.lang.Object)
     */
    public void error(Object message) {
        error(message, null);
    }

    /**
     * <code>error</code> レベルでエラーロギング処理を行なう。
     * @param message 出力するメッセージ
     * @param t 原因となった例外
     * @see org.apache.commons.logging.Log#error(java.lang.Object, java.lang.Throwable)
     */
    public void error(Object message, Throwable t) {
        recordLogObject(message, t, LogObject.ERROR, _name);
    }

    /**
     * <code>fatal</code> レベルでロギング処理を行なう。
     * @param message 出力するメッセージ
     * @see org.apache.commons.logging.Log#fatal(java.lang.Object)
     */
    public void fatal(Object message) {
        fatal(message, null);
    }

    /**
     * <code>fatal</code> レベルでエラーロギング処理を行なう。
     * @param message 出力するメッセージ
     * @param t 原因となった例外
     * @see org.apache.commons.logging.Log#fatal(java.lang.Object, java.lang.Throwable)
     */
    public void fatal(Object message, Throwable t) {
        recordLogObject(message, t, LogObject.FATAL, _name);
    }

    /**
     * <code>info</code> レベルでロギング処理を行なう。
     * @param message 出力するメッセージ
     * @see org.apache.commons.logging.Log#info(java.lang.Object)
     */
    public void info(Object message) {
        info(message, null);
    }

    /**
     * <code>info</code> レベルでエラーロギング処理を行なう。
     * @param message 出力するメッセージ
     * @param t 原因となった例外
     * @see org.apache.commons.logging.Log#info(java.lang.Object, java.lang.Throwable)
     */
    public void info(Object message, Throwable t) {
        recordLogObject(message, t, LogObject.INFO, _name);
    }

    /**
     * <code>trace</code> レベルでロギング処理を行なう。
     * @param message 出力するメッセージ
     * @since 1.1
     * @see org.apache.commons.logging.Log#trace(java.lang.Object)
     */
    public void trace(Object message) {
        trace(message, null);
    }

    /**
     * <code>trace</code> レベルでエラーロギング処理を行なう。
     * @param message 出力するメッセージ
     * @param t 原因となった例外
     * @see org.apache.commons.logging.Log#trace(java.lang.Object, java.lang.Throwable)
     */
    public void trace(Object message, Throwable t) {
        recordLogObject(message, t, LogObject.TRACE, _name);
    }

    /**
     * <code>warn</code> レベルでロギング処理を行なう。
     * @param message 出力するメッセージ
     * @since 1.1
     * @see org.apache.commons.logging.Log#warn(java.lang.Object)
     */
    public void warn(Object message) {
        warn(message, null);
    }

    /**
     * <code>warn</code> レベルでエラーロギング処理を行なう。
     * @param message 出力するメッセージ
     * @param t 原因となった例外
     * @see org.apache.commons.logging.Log#warn(java.lang.Object, java.lang.Throwable)
     */
    public void warn(Object message, Throwable t) {
        recordLogObject(message, t, LogObject.WARN, _name);
    }

    /**
     * <code>debug</code> レベルのログ処理が現在有効かどうかチェックする。
     * @return 常に <code>true</code>
     * @see org.apache.commons.logging.Log#isDebugEnabled()
     */
    public boolean isDebugEnabled() {
        return true;
    }

    /**
     * <code>error</code> レベルのログ処理が現在有効かどうかチェックする。
     * @return 常に <code>true</code>
     * @see org.apache.commons.logging.Log#isErrorEnabled()
     */
    public boolean isErrorEnabled() {
        return true;
    }

    /**
     * <code>fatal</code> レベルのログ処理が現在有効かどうかチェックする。
     * @return 常にtrue
     * @see org.apache.commons.logging.Log#isFatalEnabled()
     */
    public boolean isFatalEnabled() {
        return true;
    }

    /**
     * <code>info</code> レベルのログ処理が現在有効かどうかチェックする。
     * @return 常に <code>true</code>
     * @see org.apache.commons.logging.Log#isInfoEnabled()
     */
    public boolean isInfoEnabled() {
        return true;
    }

    /**
     * <code>trace</code> レベルのログ処理が現在有効かどうかチェックする。
     * @return 常に <code>true</code>
     * @see org.apache.commons.logging.Log#isTraceEnabled()
     */
    public boolean isTraceEnabled() {
        return true;
    }

    /**
     * <code>warn</code> レベルのログ処理が現在有効かどうかチェックする。
     * @return 常に <code>true</code>
     * @see org.apache.commons.logging.Log#isWarnEnabled()
     */
    public boolean isWarnEnabled() {
        return true;
    }

    // ----------------------------------------------------- LogFactory Method

    /**
     * <code>Log</code> インスタンスを生成する。
     * @param name この <code>Log</code> インスタンスに紐づく名前。
     * @return <code>LogUTUtil</code> インスタンス。
     * @see LogFactoryImpl#newInstance(java.lang.String)
     */
    @Override
    protected Log newInstance(String name) {
        return new LogUTUtil(name);
    }

    // ------------------------------------------------------------- LogObject

    /**
     * ログのキューを表すクラス。
     * @author 窪田 康大
     */
    private static class LogObject {

        // ------------------------------------- LogObject Constant Properties

        /**
         * <code>debug</code> レベルを表す文字列。
         */
        public static final String DEBUG = "DEBUG";

        /**
         * <code>error</code> レベルを表す文字列。
         */
        public static final String ERROR = "ERROR";

        /**
         * <code>error</code> レベルを表す文字列。
         */
        public static final String FATAL = "FATAL";

        /**
         * <code>info</code> レベルを表す文字列。
         */
        public static final String INFO = "INFO";

        /**
         * <code>trace</code> レベルを表す文字列。
         */
        public static final String TRACE = "TRACE";

        /**
         * <code>warn</code> レベルを表す文字列。
         */
        public static final String WARN = "WARN";

        // ---------------------------------------------- LogObject Properties

        /**
         * このオブジェクトを生成したスレッドに紐づくメソッドオブジェクト。
         */
        private final Method _calledMethod;

        /**
         * ログレベル。
         */
        private final String _level;

        /**
         * ログメッセージ。
         */
        private final Object _message;

        /**
         * ログ出力の原因となった例外。
         */
        private final Throwable _t;

        // --------------------------------------------- LogObject Constructor

        /**
         * コンストラクタ。
         * @param calledMethod このオブジェクトを生成したスレッドに紐づくメソッド
         * @param message ログメッセージ
         * @param t ログ出力の原因となった例外
         * @param level ログレベル
         */
        public LogObject(Method calledMethod, Object message, Throwable t,
                String level) {
            super();
            _calledMethod = calledMethod;
            _message = message;
            _t = t;
            _level = level;
        }

        // ---------------------------------------------------- Logging Method

        /**
         * このオブジェクトと他のオブジェクトが等しいかどうかを示す。
         * @param obj 比較対象の参照オブジェクト
         * @return obj 引数に指定されたオブジェクトとこのオブジェクトが等しい場合は <code>true</code> 、そうでない場合は <code>false</code>。
         */
        @Override
        public boolean equals(Object obj) {
            LogObject other = null;
            if (obj instanceof LogObject) {
                other = (LogObject) obj;
                if (other._calledMethod == null) {
                    return false;
                }
            } else {
                return false;
            }

            if (!other._calledMethod.equals(this._calledMethod)) {
                return false;
            }

            if ((other._message == null) ? this._message != null
                    : this._message == null
                            || !other._message.equals(this._message)) {
                return false;
            }

            if ((other._t == null) ? (this._t != null) : (this._t == null)
                    || !other._t.getClass().getName().equals(
                            this._t.getClass().getName())) {
                return false;
            }

            if ((other._level == null) ? (this._level != null)
                    : (this._level == null)
                            || !other._level.equals(this._level)) {
                return false;
            }

            return true;
        }

        /**
         * このインスタンスの情報を文字列として取得する。
         * @return このインスタンスの情報
         * @since 1.1
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("[");
            buffer.append(_level);
            if (_level.length() == 4) {
                buffer.append(" ");
            }
            buffer.append("] ");
            buffer.append(_message);

            if (_t != null) {
                ByteArrayOutputStream byteArrayOutputStream = null;
                PrintStream printStream = null;
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    printStream = new PrintStream(byteArrayOutputStream);
                    _t.printStackTrace(printStream);
                    buffer.append("\n");
                    buffer.append(byteArrayOutputStream.toString());
                } finally {
                    if (printStream != null) {
                        printStream.close();
                    } else if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (IOException e) {
                            // do nothing.
                        }
                    }
                }
            }

            return buffer.toString();
        }
    }

}
