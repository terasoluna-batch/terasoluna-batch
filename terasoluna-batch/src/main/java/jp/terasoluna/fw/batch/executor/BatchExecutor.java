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

package jp.terasoluna.fw.batch.executor;

import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;

/**
 * バッチエグゼキュータインタフェース。
 */
public interface BatchExecutor {

    /**
     * <h6>バッチ実行.</h6>
     * @param jobRecord 実行するジョブレコード
     * @return ビジネスロジック実行結果
     */
    BLogicResult executeBatch(BatchJobData jobRecord);

}
