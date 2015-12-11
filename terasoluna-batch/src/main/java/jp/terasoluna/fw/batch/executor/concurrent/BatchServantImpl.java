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

package jp.terasoluna.fw.batch.executor.concurrent;

import jp.terasoluna.fw.batch.executor.AbstractJobBatchExecutor;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;

/**
 * バッチサーバント実装クラス。<br>
 * <br>
 * 非同期バッチエグゼキュータから呼ばれ、指定されたジョブシーケンスコードからジョブを実行する。
 * @see jp.terasoluna.fw.batch.executor.AbstractJobBatchExecutor
 * @deprecated 本クラスはバージョン3.6.0よりジョブ実行時の前処理・主処理実装として{@code AsyncJobWorkerImpl}に移行したため、本APIは非推奨とする。
 * @see jp.terasoluna.fw.batch.executor.worker.AsyncJobWorkerImpl
 */
@Deprecated
public class BatchServantImpl extends AbstractJobBatchExecutor implements
                                                              BatchServant {

    /**
     * ジョブ実行ステータス
     */
    private BLogicResult result = new BLogicResult();

    /**
     * ジョブシーケンスコード
     */
    private String jobSequenceId = null;

    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            // エラーメッセージの初期化
            this.initDefaultErrorMessage();
            // バッチ実行
            this.result = this.executeBatch(this.jobSequenceId);
        } finally {
            closeApplicationContext(defaultApplicationContext);
        }
    }

    /**
     * ジョブシーケンスコード
     * @return the jobSequenceId
     */
    public String getJobSequenceId() {
        return jobSequenceId;
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.executor.concurrent.BatchServant#setJobSequenceId(java.lang.String)
     */
    public void setJobSequenceId(String jobSequenceId) {
        this.jobSequenceId = jobSequenceId;
    }

    /**
     * ジョブ実行ステータス
     * @return the result
     */
    public BLogicResult getResult() {
        return result;
    }
}
