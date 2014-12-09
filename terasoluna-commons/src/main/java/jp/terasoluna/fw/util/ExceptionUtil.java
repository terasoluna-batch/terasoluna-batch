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

package jp.terasoluna.fw.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 例外に関するユーティリティクラス。
 * 
 * <p>
 *  例外のスタックトレースをすべて出力する機能である。<br>
 *  ログの機能によっては、原因となった例外スタックトレースを
 *  最後まで表示しない。
 *  本機能は、原因となった例外を再帰的に取得し、
 *  スタックトレースとなる文字列を取得する。使用例は下記のとおりである。
 *  
 *  <strong>ExceptionUtilの使用例</strong><br>
 *  <code><pre>
 *  ・・・
 *  try {
 *     ・・・
 *  } catch (Exception e) {
 *      // 例外スタックトレースを最後まで出力
 *      log.error("error-message", ExceptionUtil.getStackTrace(e));
 *  }
 *  ・・・
 * </pre></code>
 * </p>
 * 
 */
public final class ExceptionUtil {
    
    /**
     * ログクラス
     */
    private static Log log = LogFactory.getLog(ExceptionUtil.class);
    
    /**
     * ServletExceptionのみ、例外時のスタックトレースの処理が異なるので、
     * それを識別するために使用する。
     */
    private static final String SERVLET_EXCEPTION_NAME = 
        "javax.servlet.ServletException";
    
    /**
     * ServletExceptionが発生した際に使用するメソッド名。
     * Servlet の例外が引き起こされた元になった例外を返すメソッドである。
     */
    private static final String GET_ROOT_CAUSE = "getRootCause";
    
    /**
     * 指定した例外のスタックトレースを取得する。
     * 
     * <p>
     *  指定した例外の原因となった例外が取得できれば、
     *  その例外のスタックトレースを再帰的に取得する。
     *  ただしgetRootCause()で拾うものについてはServletExceptionのみ対応。
     * </p>
     *
     * @param throwable 例外
     * @return 再帰的に辿られたスタックトレース
     */
    public static String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (throwable != null) {
            baos.reset();
            throwable.printStackTrace(new PrintStream(baos));
            sb.append(baos.toString());
            
            //throwableからClassオブジェクトを取り出す。
            Class throwableClass = throwable.getClass();
            
            // ServletException ならば getRootCause を使う
            if (SERVLET_EXCEPTION_NAME.equals(throwableClass.getName())) {
                try {
                    //throwable = ((ServletException) throwable).getRootCause()
                    //Classオブジェクトからメソッド名を指定して実行する。
                    Method method = throwableClass.getMethod(GET_ROOT_CAUSE);
                    throwable = (Throwable) method.invoke(throwable);
                } catch (NoSuchMethodException e) {
                    //一致するメソッドが見つからない場合
                    log.error(e.getMessage());
                    throwable = null;
                } catch (IllegalAccessException e) {
                    //基本となるメソッドにアクセスできない場合
                    log.error(e.getMessage());
                    throwable = null;
                } catch (InvocationTargetException e) {
                    //基本となるメソッドが例外をスローする場合
                    log.error(e.getMessage());
                    throwable = null;
                }
                
            } else {
                throwable = throwable.getCause();  
            }
        }
        return sb.toString();
    }

}
