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

package jp.terasoluna.fw.batch.dao.support;

/**
 * バッチ更新実行結果クラス<br>
 */
public class BatchUpdateResult {

    /**
     * バッチ更新実行に使用したバッチ更新サポート
     */
    private BatchUpdateSupport executeBatchUpdateSupport = null;

    /**
     * バッチ更新実行結果.<br>
     */
    private Integer executeBatchResult = null;

    /**
     * バッチ更新実行時に発生した例外.<br>
     */
    private Throwable executeBatchException = null;

    /**
     * コンストラクタ
     */
    public BatchUpdateResult() {
    }

    /**
     * コンストラクタ
     * @param executeBatchUpdateSupport BatchUpdateSupport 更新実行に使用したバッチ更新サポート
     * @param executeBatchResult Integer バッチ更新実行結果
     */
    public BatchUpdateResult(BatchUpdateSupport executeBatchUpdateSupport,
            Integer executeBatchResult) {
        this.executeBatchUpdateSupport = executeBatchUpdateSupport;
        this.executeBatchResult = executeBatchResult;
    }

    /**
     * コンストラクタ
     * @param executeBatchUpdateSupport BatchUpdateSupport 更新実行に使用したバッチ更新サポート
     * @param executeBatchException Throwable バッチ更新実行時に発生した例外
     */
    public BatchUpdateResult(BatchUpdateSupport executeBatchUpdateSupport,
            Throwable executeBatchException) {
        this.executeBatchUpdateSupport = executeBatchUpdateSupport;
        this.executeBatchException = executeBatchException;
    }

    /**
     * バッチ更新実行に使用したバッチ更新サポートを取得する.<br>
     * @return
     */
    public BatchUpdateSupport getExecuteBatchUpdateSupport() {
        return executeBatchUpdateSupport;
    }

    /**
     * バッチ更新実行に使用したバッチ更新サポートを設定する.<br>
     * @param executeBatchUpdateSupport
     */
    public void setExecuteBatchUpdateSupport(
            BatchUpdateSupport executeBatchUpdateSupport) {
        this.executeBatchUpdateSupport = executeBatchUpdateSupport;
    }

    /**
     * バッチ更新実行結果を取得する.<br>
     * @return
     */
    public Integer getExecuteBatchResult() {
        return executeBatchResult;
    }

    /**
     * バッチ更新実行結果を設定する.<br>
     * @param executeBatchResult
     */
    public void setExecuteBatchResult(Integer executeBatchResult) {
        this.executeBatchResult = executeBatchResult;
    }

    /**
     * バッチ更新実行時に発生した例外を取得する.<br>
     * @return
     */
    public Throwable getExecuteBatchException() {
        return executeBatchException;
    }

    /**
     * バッチ更新実行時に発生した例外を設定する.<br>
     * @param executeBatchException
     */
    public void setExecuteBatchException(Throwable executeBatchException) {
        this.executeBatchException = executeBatchException;
    }

}
