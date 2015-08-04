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

import jp.terasoluna.fw.batch.executor.vo.BLogicResult;

/**
 * ジョブの実行ステータス更新を行うインタフェース。<br>
 * @since 3.6
 */
public interface JobStatusChanger {

    /**
     * ジョブの実行ステータスを「実行中」に更新する。<br>
     * @param jobSequenceId ジョブのシーケンスコード
     * @return 更新に成功したらtrue。<br>
     *         BatchJobDataが取得できないとき、ジョブステータスが想定外のとき、ジョブステータスの更新が正常に行えなかったときはfalse。
     */
    boolean changeToStartStatus(String jobSequenceId);

    /**
     * ジョブの実行ステータスを「処理済み」に更新する。<br>
     * @param jobSequenceId ジョブのシーケンスコード
     * @param blogicResult ビジネスロジックの実行結果
     * @return 更新に成功したらtrue。<br>
     *         BatchJobDataが取得できないとき、ジョブステータスが想定外のとき、ジョブステータスの更新が正常に行えなかったときはfalse。
     */
    boolean changeToEndStatus(String jobSequenceId, BLogicResult bLogicResult);
}
