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

package jp.terasoluna.fw.batch.executor.controller;

/**
 * バッチ処理を構成する非同期ジョブ実行管理を行うインタフェース。<br>
 *
 * @since 3.6
 */
public interface AsyncJobLauncher {

    /**
     * ジョブシーケンスコードから非同期ジョブを起動する。<br>
     *
     * @param jobSequenceId ジョブのシーケンスコード
     */
    void executeJob(String jobSequenceId);

    /**
     * ジョブ実行管理機能をシャットダウンする。
     */
    void shutdown();
}
