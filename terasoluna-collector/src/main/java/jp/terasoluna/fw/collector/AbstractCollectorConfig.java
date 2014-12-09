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

package jp.terasoluna.fw.collector;

import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.validate.ValidationErrorHandler;

import org.springframework.validation.Validator;

/**
 * AbstractCollector設定項目
 */
public class AbstractCollectorConfig {
    /** キューサイズ */
    protected int queueSize = AbstractCollector.DEFAULT_QUEUE_SIZE;

    /** CollectorExceptionHandler */
    protected CollectorExceptionHandler exceptionHandler = null;

    /** Validator */
    protected Validator validator = null;

    /** ValidationErrorHandler */
    protected ValidationErrorHandler validationErrorHandler = null;

    /** コンストラクタで処理を実行するフラグ（true:実行する、false:実行しない） */
    protected boolean executeByConstructor = false;

    /**
     * キューサイズを取得する。
     * @return キューサイズ
     */
    public int getQueueSize() {
        return queueSize;
    }

    /**
     * キューサイズを設定する。
     * @param queueSize キューサイズ
     */
    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    /**
     * CollectorExceptionHandlerを取得する。
     * @return CollectorExceptionHandler
     */
    public CollectorExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    /**
     * CollectorExceptionHandlerを設定する。
     * @param exceptionHandler CollectorExceptionHandler
     */
    public void setExceptionHandler(CollectorExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Validatorを取得する。
     * @return Validator
     */
    public Validator getValidator() {
        return validator;
    }

    /**
     * Validatorを設定する。
     * @param validator Validator
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * ValidationErrorHandlerを取得する。
     * @return ValidationErrorHandler
     */
    public ValidationErrorHandler getValidationErrorHandler() {
        return validationErrorHandler;
    }

    /**
     * ValidationErrorHandlerを設定する。
     * @param validationErrorHandler ValidationErrorHandler
     */
    public void setValidationErrorHandler(
            ValidationErrorHandler validationErrorHandler) {
        this.validationErrorHandler = validationErrorHandler;
    }

    /**
     * コンストラクタで処理を実行するフラグを取得する。
     * @return コンストラクタで処理を実行するフラグ
     */
    public boolean isExecuteByConstructor() {
        return executeByConstructor;
    }

    /**
     * コンストラクタで処理を実行するフラグを設定する。
     * @param executeByConstructor コンストラクタで処理を実行するフラグ
     */
    public void setExecuteByConstructor(boolean executeByConstructor) {
        this.executeByConstructor = executeByConstructor;
    }
}
