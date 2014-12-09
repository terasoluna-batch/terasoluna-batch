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

/**
 * JNDI関連例外クラス。
 *
 * <p>
 *  JNDI関連エラーを表現する。
 * </p>
 *
 *  JndiExceptionクラスは、RuntimeExceptionクラスのサブクラスである。
 *  JndiExceptionをスローする場合には、そのメソッドのthorws句に明示的に記述する
 *  必要はない。<br>
 *
 *  コンストラクタでの生成時に、エラーメッセージを指定することができる。
 * </p>
 *
 */
public class JndiException extends RuntimeException {
    
    /**
     * シリアルバージョンID。
     */
    private static final long serialVersionUID = -7105599934896030074L;

    /**
     * コンストラクタ。
     *
     * @param cause 原因となった例外
     */
    public JndiException(Throwable cause) {
        super(cause);
    }
    
    /**
     * コンストラクタ。
     *
     * @param message エラーメッセージ
     * @param cause 原因となった例外
     */
    public JndiException(String message,
                            Throwable cause) {
        super(message, cause);
    }
}
