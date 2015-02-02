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
package jp.terasoluna.fw.message.execption;

/**
 * メッセージ実行時例外クラス（内部クラス）
 *
 */
@SuppressWarnings("serial")
public class MessageRuntimeException extends RuntimeException {

    /**
     * コンストラクタ
     * 
     * @param message メッセージ
     */
    public MessageRuntimeException(String message) {
        super(message);
    }

    /**
     * コンストラクタ
     * 
     * @param cause 起因例外
     */
    public MessageRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * コンストラクタ
     * 
     * @param message メッセージ
     * @param cause 起因例外
     */
    public MessageRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
