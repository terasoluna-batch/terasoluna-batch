/*
 * Copyright (c) 2015 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.executor.worker;

/**
 * ジョブシーケンスコードで決定されるジョブの前処理と主処理を定義するインタフェース。<br>
 */
public interface JobExecutorTemplate {

    /**
     * ジョブの前処理を行う。<br>
     *
     * @param jobSequenceId ジョブシーケンスコード
     * @return 前処理の処理結果(falseならば主処理の実行を中断する)
     */
    boolean beforeExecute(String jobSequenceId);

    /**
     * ジョブの主処理を行う。<br>
     *
     * @param jobSequenceId ジョブシーケンスコード
     */
    void executeWorker(String jobSequenceId);
}
