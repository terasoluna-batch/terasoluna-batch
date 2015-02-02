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

import jp.terasoluna.fw.collector.AbstractCollectorConfig;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.validate.ValidationErrorHandler;
import jp.terasoluna.fw.file.dao.FileQueryDAO;

import org.springframework.validation.Validator;

/**
 * FileCollector設定項目
 */
public class FileCollectorConfig<P> extends AbstractCollectorConfig {
    /** FileQueryDAO */
    protected FileQueryDAO fileQueryDAO = null;

    /** ファイル名（絶対パスまたは相対パスのどちらか） */
    protected String fileName = null;

    /** 1行分の文字列を格納するファイル行オブジェクトクラス */
    protected Class<P> clazz = null;

    /**
     * コンストラクタ
     * @param fileQueryDAO FileQueryDAOインスタンス
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     */
    public FileCollectorConfig(FileQueryDAO fileQueryDAO, String fileName,
            Class<P> clazz) {
        this.fileQueryDAO = fileQueryDAO;
        this.fileName = fileName;
        this.clazz = clazz;
    }

    /**
     * キューサイズを設定する
     * @param queueSize キューサイズ
     * @return FileCollectorConfig&lt;P&gt;
     */
    public FileCollectorConfig<P> addQueueSize(int queueSize) {
        this.setQueueSize(queueSize);
        return this;
    }

    /**
     * CollectorExceptionHandlerを設定する
     * @param exceptionHandler CollectorExceptionHandler
     * @return FileCollectorConfig&lt;P&gt;
     */
    public FileCollectorConfig<P> addExceptionHandler(
            CollectorExceptionHandler exceptionHandler) {
        this.setExceptionHandler(exceptionHandler);
        return this;
    }

    /**
     * Validatorを設定する
     * @param validator Validator
     * @return FileCollectorConfig&lt;P&gt;
     */
    public FileCollectorConfig<P> addValidator(Validator validator) {
        this.setValidator(validator);
        return this;
    }

    /**
     * ValidationErrorHandlerを設定する
     * @param validationErrorHandler ValidationErrorHandler
     * @return FileCollectorConfig&lt;P&gt;
     */
    public FileCollectorConfig<P> addValidationErrorHandler(
            ValidationErrorHandler validationErrorHandler) {
        this.setValidationErrorHandler(validationErrorHandler);
        return this;
    }

    /**
     * コンストラクタで処理を実行するフラグを設定する
     * @param executeByConstructor コンストラクタで処理を実行するフラグ
     * @return FileCollectorConfig&lt;P&gt;
     */
    public FileCollectorConfig<P> addExecuteByConstructor(
            boolean executeByConstructor) {
        this.setExecuteByConstructor(executeByConstructor);
        return this;
    }

    /**
     * FileQueryDAO
     * @return FileQueryDAO
     */
    public FileQueryDAO getFileQueryDAO() {
        return fileQueryDAO;
    }

    /**
     * FileQueryDAO
     * @param fileQueryDAO FileQueryDAO
     */
    public void setFileQueryDAO(FileQueryDAO fileQueryDAO) {
        this.fileQueryDAO = fileQueryDAO;
    }

    /**
     * ファイル名（絶対パスまたは相対パスのどちらか）
     * @return ファイル名（絶対パスまたは相対パスのどちらか）
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * ファイル名（絶対パスまたは相対パスのどちらか）
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 1行分の文字列を格納するファイル行オブジェクトクラス
     * @return 1行分の文字列を格納するファイル行オブジェクトクラス
     */
    public Class<P> getClazz() {
        return clazz;
    }

    /**
     * 1行分の文字列を格納するファイル行オブジェクトクラス
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     */
    public void setClazz(Class<P> clazz) {
        this.clazz = clazz;
    }
}
