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

package jp.terasoluna.fw.dao;

import org.springframework.dao.DataAccessException;

/**
 * QueryDAOインターフェースの配列変換時に例外が発生した場合のRuntime例外クラス。
 * 
 * 引数のクラスと、戻り値の型が一致しない場合、
 * およびMapの配列変換時にDAO実装クラスから投げられる例外である。
 * 
 */
public class IllegalClassTypeException extends DataAccessException {

    /**
     * シリアルバージョンID。
     */
    private static final long serialVersionUID = -3147888263699426883L;
    
    /**
     * エラーメッセージ
     */
    public static final String ERROR_ILLEGAL_CLASS_TYPE
        = "The illegal Class Type of the argument.";

    /**
     * コンストラクタ。
     */
    public IllegalClassTypeException() {
        super(ERROR_ILLEGAL_CLASS_TYPE);
    }

    /**
     * コンストラクタ。
     *
     * @param message メッセージ
     */
    public IllegalClassTypeException(String message) {
        super(message);
    }

    /**
     * コンストラクタ。
     *
     * @param cause 原因となった例外
     */
    public IllegalClassTypeException(Throwable cause) {
        super(ERROR_ILLEGAL_CLASS_TYPE, cause);
    }

    /**
     * コンストラクタ。
     *
     * @param message メッセージ
     * @param cause 原因となった例外
     */
    public IllegalClassTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
