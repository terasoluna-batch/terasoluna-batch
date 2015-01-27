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

import org.springframework.validation.Validator;

/**
 * DaoCollector設定項目
 */
public class DaoCollectorConfig extends AbstractCollectorConfig {

    /** QueryResultHandleDao */
    protected Object queryResultHandleDao = null;

    /** 実行するメソッド名 */
    protected String methodName = null;

    /** SQLにバインドする値を格納したオブジェクト */
    protected Object bindParams = null;

    /** 1:Nマッピング使用フラグ（使用時はtrue） */
    protected boolean relation1n = false;

    /** DaoCollector前後処理 */
    protected DaoCollectorPrePostProcess daoCollectorPrePostProcess = null;

    /**
     * コンストラクタ
     * @param queryResultHandleDao QueryResultHandleDaoインスタンス
     * @param methodName 実行するメソッド名
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     */
    public DaoCollectorConfig(Object queryResultHandleDao, String methodName,
            Object bindParams) {
        this.queryResultHandleDao = queryResultHandleDao;
        this.methodName = methodName;
        this.bindParams = bindParams;
    }

    /**
     * キューサイズを設定する
     * @param queueSize キューサイズ
     * @return DaoCollectorConfig
     */
    public DaoCollectorConfig addQueueSize(int queueSize) {
        this.setQueueSize(queueSize);
        return this;
    }

    /**
     * CollectorExceptionHandlerを設定する
     * @param exceptionHandler CollectorExceptionHandler
     * @return DaoCollectorConfig
     */
    public DaoCollectorConfig addExceptionHandler(
            CollectorExceptionHandler exceptionHandler) {
        this.setExceptionHandler(exceptionHandler);
        return this;
    }

    /**
     * Validatorを設定する
     * @param validator Validator
     * @return DaoCollectorConfig
     */
    public DaoCollectorConfig addValidator(Validator validator) {
        this.setValidator(validator);
        return this;
    }

    /**
     * ValidationErrorHandlerを設定する
     * @param validationErrorHandler ValidationErrorHandler
     * @return DaoCollectorConfig
     */
    public DaoCollectorConfig addValidationErrorHandler(
            ValidationErrorHandler validationErrorHandler) {
        this.setValidationErrorHandler(validationErrorHandler);
        return this;
    }

    /**
     * 1:Nマッピング使用フラグを設定する
     * @param relation1n 1:Nマッピング使用フラグ
     * @return DaoCollectorConfig
     */
    public DaoCollectorConfig addRelation1n(boolean relation1n) {
        this.setRelation1n(relation1n);
        return this;
    }

    /**
     * DaoCollector前後処理を設定する
     * @param daoCollectorPrePostProcess DaoCollector前後処理
     * @return DaoCollectorConfig
     */
    public DaoCollectorConfig addDaoCollectorPrePostProcess(
            DaoCollectorPrePostProcess daoCollectorPrePostProcess) {
        this.setDaoCollectorPrePostProcess(daoCollectorPrePostProcess);
        return this;
    }

    /**
     * コンストラクタで処理を実行するフラグを設定する
     * @param executeByConstructor コンストラクタで処理を実行するフラグ
     * @return DaoCollectorConfig
     */
    public DaoCollectorConfig addExecuteByConstructor(
            boolean executeByConstructor) {
        this.setExecuteByConstructor(executeByConstructor);
        return this;
    }

    /**
     * QueryResultHandleDaoを取得する。
     * @return QueryResultHandleDao
     */
    public Object getQueryResultHandleDao() {
        return queryResultHandleDao;
    }

    /**
     * QueryResultHandleDaoを設定する。
     * @param queryResultHandleDao QueryResultHandleDao
     */
    public void setQueryResultHandleDao(Object queryResultHandleDao) {
        this.queryResultHandleDao = queryResultHandleDao;
    }

    /**
     * 実行するメソッド名を取得する。
     * @return 実行するメソッド名
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 実行するメソッド名を設定する。
     * @param methodName 実行するメソッド名
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
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
     * DaoCollector前後処理を取得する。
     * @return DaoCollector前処理
     */
    public DaoCollectorPrePostProcess getDaoCollectorPrePostProcess() {
        return daoCollectorPrePostProcess;
    }

    /**
     * DaoCollector前後処理を設定する。
     * @param daoCollectorPrePostProcess DaoCollector前後処理
     */
    public void setDaoCollectorPrePostProcess(
            DaoCollectorPrePostProcess daoCollectorPrePostProcess) {
        this.daoCollectorPrePostProcess = daoCollectorPrePostProcess;
    }
}
