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

package jp.terasoluna.fw.batch.blogic.vo;

import jp.terasoluna.fw.batch.executor.vo.BatchJobData;

/**
 * ジョブパラメータよりビジネスロジックの入力オブジェクトを変換するインタフェース。<br>
 * @since 3.6
 */
public interface BLogicParamConverter {

    /**
     * ジョブパラメータである<code>BatchJobData</code>をビジネスロジックの入力オブジェクトに変換する。<br>
     *
     * @param batchJobData ジョブパラメータ
     * @return ビジネスロジックの入力オブジェクト
     */
    BLogicParam convertBLogicParam(BatchJobData batchJobData);
}
