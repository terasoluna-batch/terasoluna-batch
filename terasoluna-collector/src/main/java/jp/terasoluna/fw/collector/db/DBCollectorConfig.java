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

package jp.terasoluna.fw.collector.db;

import jp.terasoluna.fw.collector.AbstractCollectorConfig;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.validate.ValidationErrorHandler;
import jp.terasoluna.fw.dao.QueryRowHandleDAO;

import org.springframework.validation.Validator;

/**
 * DBCollector設定項目
 */
public class DBCollectorConfig extends AbstractCollectorConfig {

    /** QueryRowHandleDAO */
    protected QueryRowHandleDAO queryRowHandleDAO = null;

    /** 実行するSQLのID */
    protected String sqlID = null;

    /** SQLにバインドする値を格納したオブジェクト */
    protected Object bindParams = null;

    /** 1:Nマッピング使用フラグ（使用時はtrue） */
    protected boolean relation1n = false;

    /** DBCollector前後処理 */
    protected DBCollectorPrePostProcess dbCollectorPrePostProcess = null;

    /**
     * コンストラクタ
     * @param queryRowHandleDAO QueryRowHandleDAOインスタンス
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     */
    public DBCollectorConfig(QueryRowHandleDAO queryRowHandleDAO, String sqlID,
            Object bindParams) {
        this.queryRowHandleDAO = queryRowHandleDAO;
        this.sqlID = sqlID;
        this.bindParams = bindParams;
    }

    /**
     * キューサイズを設定する
     * @param queueSize キューサイズ
     * @return DBCollectorConfig
     */
    public DBCollectorConfig addQueueSize(int queueSize) {
        this.setQueueSize(queueSize);
        return this;
    }

    /**
     * CollectorExceptionHandlerを設定する
     * @param exceptionHandler CollectorExceptionHandler
     * @return DBCollectorConfig
     */
    public DBCollectorConfig addExceptionHandler(
            CollectorExceptionHandler exceptionHandler) {
        this.setExceptionHandler(exceptionHandler);
        return this;
    }

    /**
     * Validatorを設定する
     * @param validator Validator
     * @return DBCollectorConfig
     */
    public DBCollectorConfig addValidator(Validator validator) {
        this.setValidator(validator);
        return this;
    }

    /**
     * ValidationErrorHandlerを設定する
     * @param validationErrorHandler ValidationErrorHandler
     * @return DBCollectorConfig
     */
    public DBCollectorConfig addValidationErrorHandler(
            ValidationErrorHandler validationErrorHandler) {
        this.setValidationErrorHandler(validationErrorHandler);
        return this;
    }

    /**
     * 1:Nマッピング使用フラグを設定する
     * @param relation1n 1:Nマッピング使用フラグ
     * @return DBCollectorConfig
     */
    public DBCollectorConfig addRelation1n(boolean relation1n) {
        this.setRelation1n(relation1n);
        return this;
    }

    /**
     * DBCollector前後処理を設定する
     * @param dbCollectorPrePostProcess DBCollector前後処理
     * @return DBCollectorConfig
     */
    public DBCollectorConfig addDbCollectorPrePostProcess(
            DBCollectorPrePostProcess dbCollectorPrePostProcess) {
        this.setDbCollectorPrePostProcess(dbCollectorPrePostProcess);
        return this;
    }

    /**
     * コンストラクタで処理を実行するフラグを設定する
     * @param executeByConstructor コンストラクタで処理を実行するフラグ
     * @return DBCollectorConfig
     */
    public DBCollectorConfig addExecuteByConstructor(
            boolean executeByConstructor) {
        this.setExecuteByConstructor(executeByConstructor);
        return this;
    }

    /**
     * QueryRowHandleDAOを取得する。
     * @return QueryRowHandleDAO
     */
    public QueryRowHandleDAO getQueryRowHandleDAO() {
        return queryRowHandleDAO;
    }

    /**
     * QueryRowHandleDAOを設定する。
     * @param queryRowHandleDAO QueryRowHandleDAO
     */
    public void setQueryRowHandleDAO(QueryRowHandleDAO queryRowHandleDAO) {
        this.queryRowHandleDAO = queryRowHandleDAO;
    }

    /**
     * 実行するSQLのIDを取得する。
     * @return 実行するSQLのID
     */
    public String getSqlID() {
        return sqlID;
    }

    /**
     * 実行するSQLのIDを設定する。
     * @param sqlID 実行するSQLのID
     */
    public void setSqlID(String sqlID) {
        this.sqlID = sqlID;
    }

    /**
     * SQLにバインドする値を格納したオブジェクトを取得する。
     * @return SQLにバインドする値を格納したオブジェクト
     */
    public Object getBindParams() {
        return bindParams;
    }

    /**
     * SQLにバインドする値を格納したオブジェクトを設定する。
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     */
    public void setBindParams(Object bindParams) {
        this.bindParams = bindParams;
    }

    /**
     * 1:Nマッピング使用フラグを取得する。
     * @return 1:Nマッピング使用フラグ
     */
    public boolean isRelation1n() {
        return relation1n;
    }

    /**
     * 1:Nマッピング使用フラグを設定する。
     * @param relation1n 1:Nマッピング使用フラグ
     */
    public void setRelation1n(boolean relation1n) {
        this.relation1n = relation1n;
    }

    /**
     * DBCollector前後処理を取得する。
     * @return DBCollector前処理
     */
    public DBCollectorPrePostProcess getDbCollectorPrePostProcess() {
        return dbCollectorPrePostProcess;
    }

    /**
     * DBCollector前後処理を設定する。
     * @param dbCollectorPrePostProcess DBCollector前後処理
     */
    public void setDbCollectorPrePostProcess(
            DBCollectorPrePostProcess dbCollectorPrePostProcess) {
        this.dbCollectorPrePostProcess = dbCollectorPrePostProcess;
    }
}
