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

/**
 *  ClassUtilクラスで発生した予期された例外をラップします。
 * 
 * <p>
 *  実際に発生した例外については、 getCause() メソッドから取得する。
 * </p>
 * 
 * @see jp.terasoluna.fw.util.ClassUtil
 * 
 */
public class ClassLoadException extends Exception {

    /**
     * シリアルバージョンID。
     */
    private static final long serialVersionUID = -7229283425927441452L;

    /**
     * コンストラクタ
     * 
     * @param cause ラップする例外
     */
    public ClassLoadException(Throwable cause) {
        super(cause);
    }

}
