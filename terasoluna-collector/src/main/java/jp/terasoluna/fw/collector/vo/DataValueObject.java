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

package jp.terasoluna.fw.collector.vo;

import jp.terasoluna.fw.collector.exception.CollectorExceptionHandlerStatus;
import jp.terasoluna.fw.collector.validate.ValidateErrorStatus;

/**
 * DataValueObject.<br>
 * キューにデータや例外を格納する際の箱。
 */
public class DataValueObject {
    /** データカウント（取得したデータが何件目のデータかを示す（1件目=1）） */
    protected long dataCount = -1;

    /** 実行結果データ */
    protected Object value = null;

    /** 発生した例外 */
    protected Throwable throwable = null;

    /** 入力チェックステータス */
    protected ValidateErrorStatus validateStatus = null;

    /** コレクタステータス */
    protected CollectorStatus collectorStatus = null;

    /** 例外ハンドラステータス */
    protected CollectorExceptionHandlerStatus exceptionHandlerStatus = null;

    /**
     * コンストラクタ（実行結果データ）<br>
     * @param value Object 実行結果データ
     */
    public DataValueObject(Object value) {
        this.value = value;
    }

    /**
     * コンストラクタ（実行結果データ）<br>
     * @param value Object 実行結果データ
     * @param dataCount long データカウンタ
     */
    public DataValueObject(Object value, long dataCount) {
        this.value = value;
        this.dataCount = dataCount;
    }

    /**
     * コンストラクタ（発生した例外）<br>
     * @param throwable Throwable 発生した例外
     */
    public DataValueObject(Throwable throwable) {
        this.throwable = throwable;
    }

    /**
     * コンストラクタ（発生した例外）<br>
     * @param throwable Throwable 発生した例外
     * @param dataCount long データカウンタ
     */
    public DataValueObject(Throwable throwable, long dataCount) {
        this.throwable = throwable;
        this.dataCount = dataCount;
    }

    /**
     * コンストラクタ（入力チェックステータス）<br>
     * @param validateStatus ValidateStatus 入力チェックステータス
     */
    public DataValueObject(ValidateErrorStatus validateStatus) {
        this.validateStatus = validateStatus;
    }

    /**
     * コンストラクタ（コレクタステータス）<br>
     * @param collectorStatus CollectorStatus コレクタステータス
     */
    public DataValueObject(CollectorStatus collectorStatus) {
        this.collectorStatus = collectorStatus;
    }

    /**
     * データカウントを取得する.<br>
     * @return long
     */
    public long getDataCount() {
        return dataCount;
    }

    /**
     * 実行結果データ<br>
     * @return 実行結果データ
     */
    public Object getValue() {
        return value;
    }

    /**
     * 発生した例外<br>
     * @return 発生した例外
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * 発生した例外<br>
     * @param throwable 発生した例外
     */
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    /**
     * 入力チェックステータス<br>
     * @return 入力チェックステータス
     */
    public ValidateErrorStatus getValidateStatus() {
        return validateStatus;
    }

    /**
     * コレクタステータス<br>
     * @return コレクタステータス
     */
    public CollectorStatus getCollectorStatus() {
        return collectorStatus;
    }

    /**
     * コレクタステータス<br>
     * @param collectorStatus コレクタステータス
     */
    public void setCollectorStatus(CollectorStatus collectorStatus) {
        this.collectorStatus = collectorStatus;
    }

    /**
     * 例外ハンドラステータス<br>
     * @return 例外ハンドラステータス
     */
    public CollectorExceptionHandlerStatus getExceptionHandlerStatus() {
        return exceptionHandlerStatus;
    }

    /**
     * 例外ハンドラステータス<br>
     * @param exceptionHandlerStatus 例外ハンドラステータス
     */
    public void setExceptionHandlerStatus(
            CollectorExceptionHandlerStatus exceptionHandlerStatus) {
        this.exceptionHandlerStatus = exceptionHandlerStatus;
    }
}
