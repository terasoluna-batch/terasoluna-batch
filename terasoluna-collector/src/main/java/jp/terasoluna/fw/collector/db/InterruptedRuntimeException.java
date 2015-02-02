/*
 * Copyright (c) 2014 NTT DATA Corporation
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

package jp.terasoluna.fw.collector.db;

/**
 * DBコレクタのクローズにより、キュー追加スレッドに割り込みが発生した際にスローされる例外。
 */
public class InterruptedRuntimeException extends RuntimeException {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 5607621359208001676L;

    /**
     * コンストラクタ.
     */
    public InterruptedRuntimeException() {
        super();
    }

    /**
     * コンストラクタ.
     *
     * @param cause Throwable
     */
    public InterruptedRuntimeException(Throwable cause) {
        super(cause);
    }
}
