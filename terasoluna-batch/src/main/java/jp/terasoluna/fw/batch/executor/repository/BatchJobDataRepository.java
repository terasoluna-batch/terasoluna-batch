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

package jp.terasoluna.fw.batch.executor.repository;

import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;

/**
 * ジョブパラメータを解決するインタフェース。<br>
 * @since 3.6
 */
public interface BatchJobDataRepository {

    /**
     * ジョブ起動引数である{@code String}配列からジョブリスト取得用DAOの出力パラメータを取得する。<br>
     * @param args ジョブ起動引数
     * @return ジョブリスト取得用DAOの出力パラメータ
     */
    BatchJobListResult resolveBatchJobResult(String[] args);

    /**
     * ジョブ取得用DAOの入力パラメータからジョブパラメータを取得する。<br>
     * @param jobSequenceId ジョブのシーケンスID
     * @return ジョブパラメータ
     */
    BatchJobData resolveBatchJobData(String jobSequenceId);
}
