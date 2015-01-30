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

package jp.terasoluna.fw.collector.file;

import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.validate.ExceptionValidationErrorHandler;
import jp.terasoluna.fw.collector.validate.ValidationErrorHandler;
import jp.terasoluna.fw.file.dao.FileQueryDAO;

import org.springframework.validation.Validator;

/**
 * FileValidateCollector.<br>
 * 独立した別スレッドを起動し、FileQueryDAOを非同期で実行する。
 * @param &ltP&gt
 */
public class FileValidateCollector<P> extends FileCollector<P> {

    /**
     * FileValidateCollectorコンストラクタ.<br>
     * @param fileQueryDAO FileQueryDAOインスタンス
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     * @param validator Validator 入力チェックを行うバリデータ
     */
    public FileValidateCollector(FileQueryDAO fileQueryDAO, String fileName,
            Class<P> clazz, Validator validator) {
        this(new FileCollectorConfig<P>(fileQueryDAO, fileName, clazz)
                .addValidator(validator));
    }

    /**
     * FileValidateCollectorコンストラクタ.<br>
     * @param fileQueryDAO FileQueryDAOインスタンス
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     * @param validator Validator 入力チェックを行うバリデータ
     * @param validationErrorHandler ValidationErrorHandler 入力チェックエラー時に行う処理
     */
    public FileValidateCollector(FileQueryDAO fileQueryDAO, String fileName,
            Class<P> clazz, Validator validator,
            ValidationErrorHandler validationErrorHandler) {
        this(new FileCollectorConfig<P>(fileQueryDAO, fileName, clazz)
                .addValidator(validator).addValidationErrorHandler(
                        validationErrorHandler));
    }

    /**
     * FileValidateCollectorコンストラクタ.<br>
     * @param fileQueryDAO FileQueryDAOインスタンス
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     * @param exceptionHandler 例外ハンドラ
     * @param validator Validator 入力チェックを行うバリデータ
     */
    public FileValidateCollector(FileQueryDAO fileQueryDAO, String fileName,
            Class<P> clazz, CollectorExceptionHandler exceptionHandler,
            Validator validator) {
        this(new FileCollectorConfig<P>(fileQueryDAO, fileName, clazz)
                .addExceptionHandler(exceptionHandler).addValidator(validator));
    }

    /**
     * FileValidateCollectorコンストラクタ.<br>
     * @param fileQueryDAO FileQueryDAOインスタンス
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     * @param exceptionHandler 例外ハンドラ
     * @param validator Validator 入力チェックを行うバリデータ
     * @param validationErrorHandler ValidationErrorHandler 入力チェックエラー時に行う処理
     */
    public FileValidateCollector(FileQueryDAO fileQueryDAO, String fileName,
            Class<P> clazz, CollectorExceptionHandler exceptionHandler,
            Validator validator, ValidationErrorHandler validationErrorHandler) {
        this(new FileCollectorConfig<P>(fileQueryDAO, fileName, clazz)
                .addExceptionHandler(exceptionHandler).addValidator(validator)
                .addValidationErrorHandler(validationErrorHandler));
    }

    /**
     * FileValidateCollectorコンストラクタ.<br>
     * @param fileQueryDAO FileQueryDAOインスタンス
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     * @param queueSize キューのサイズ（1以上を設定すること）
     * @param exceptionHandler 例外ハンドラ
     * @param validator Validator 入力チェックを行うバリデータ
     */
    public FileValidateCollector(FileQueryDAO fileQueryDAO, String fileName,
            Class<P> clazz, int queueSize,
            CollectorExceptionHandler exceptionHandler, Validator validator) {
        this(new FileCollectorConfig<P>(fileQueryDAO, fileName, clazz)
                .addQueueSize(queueSize).addExceptionHandler(exceptionHandler)
                .addValidator(validator));
    }

    /**
     * FileValidateCollectorコンストラクタ.<br>
     * @param fileQueryDAO FileQueryDAOインスタンス
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     * @param queueSize キューのサイズ（1以上を設定すること）
     * @param exceptionHandler 例外ハンドラ
     * @param validator Validator 入力チェックを行うバリデータ
     * @param validationErrorHandler ValidationErrorHandler 入力チェックエラー時に行う処理
     */
    public FileValidateCollector(FileQueryDAO fileQueryDAO, String fileName,
            Class<P> clazz, int queueSize,
            CollectorExceptionHandler exceptionHandler, Validator validator,
            ValidationErrorHandler validationErrorHandler) {
        this(new FileCollectorConfig<P>(fileQueryDAO, fileName, clazz)
                .addQueueSize(queueSize).addExceptionHandler(exceptionHandler)
                .addValidator(validator).addValidationErrorHandler(
                        validationErrorHandler));
    }

    /**
     * FileValidateCollectorコンストラクタ.<br>
     * @param config FileCollectorConfig FileCollector設定項目
     */
    public FileValidateCollector(FileCollectorConfig<P> config) {
        if (config == null) {
            throw new IllegalArgumentException("The parameter is null.");
        }

        this.fileQueryDAO = config.getFileQueryDAO();
        this.fileName = config.getFileName();
        this.clazz = config.getClazz();
        if (config.getQueueSize() > 0) {
            setQueueSize(config.getQueueSize());
        }
        this.validator = config.getValidator();
        if (config.getValidator() != null) {
            if (config.getValidationErrorHandler() != null) {
                this.validationErrorHandler = config
                        .getValidationErrorHandler();
            } else {
                this.validationErrorHandler = new ExceptionValidationErrorHandler();
            }
        }
        this.exceptionHandler = config.getExceptionHandler();

        if (config.isExecuteByConstructor()) {
            // 実行開始
            execute();
        }
    }
}
