/*
 * Copyright (c) 2016 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.executor;

/**
 * ジョブシーケンスコードにもとづいたジョブを実行するインタフェース。<br>
 */
public interface AsyncJobWorker {
    
    /**
     * ジョブの前処理を行う<br>
     * 
     * @param jobSequenceId ジョブシーケンスコード
     * @return 前処理の処理結果(true:成功、false:失敗)
     */
    boolean beforeExecute(String jobSequenceId);
    
    /**
     * ジョブの主処理を行う。<br>
     *
     * @param jobSequenceId ジョブシーケンスコード
     */
    void executeWorker(String jobSequenceId);
}
