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

/**
 * バッチサーバントインタフェース。<br>
 * <br>
 * 非同期バッチエグゼキュータから呼ばれ、指定されたジョブシーケンスコードからジョブを実行する。
 * @deprecated 本機能はバージョン3.6.0よりジョブ実行の前処理・主処理を扱うインタフェースとして{@code JobExecutorTemplate}に移行されたため、非推奨APIとする。
 * @see jp.terasoluna.fw.batch.executor.worker.JobExecutorTemplate
 */
@Deprecated
public interface BatchServant extends Runnable {

    /**
     * ジョブシーケンスコードを設定する
     * @param jobSequenceId the jobSequenceId to set
     */
    void setJobSequenceId(String jobSequenceId);

}
