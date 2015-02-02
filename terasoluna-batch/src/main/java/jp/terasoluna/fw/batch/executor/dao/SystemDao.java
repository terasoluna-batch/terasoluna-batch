/*
 * Copyright (c) 2014 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.executor.dao;

import jp.terasoluna.fw.batch.executor.vo.*;
import org.apache.ibatis.session.RowBounds;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * フレームワークによるDBアクセス実行時に使用されるDAO。
 */
public interface SystemDao {

    /**
     * 特定条件下でジョブ管理テーブルのレコードを取得する。
     *
     * @param batchJobListParam 取得条件
     * @return ジョブ管理テーブルレコード
     */
    List<BatchJobListResult> selectJobList(BatchJobListParam batchJobListParam);

    /**
     * 特定条件下でジョブ管理テーブルのレコードを取得する。
     *
     * @param rowBounds 取得行制限
     * @param batchJobListParam 取得条件
     * @return ジョブ管理テーブルレコード
     */
    List<BatchJobListResult> selectJobList(RowBounds rowBounds,
                                           BatchJobListParam batchJobListParam);

    /**
     * ジョブ管理テーブルの特定レコードを取得する。
     *
     * @param batchJobManagementParam レコードの特定条件
     * @return ジョブ管理テーブルレコード
     */
    BatchJobData selectJob(BatchJobManagementParam batchJobManagementParam);

    /**
     * 現在時刻を取得する。
     *
     * @return 現在時刻
     */
    Timestamp readCurrentTime();

    /**
     * 現在日付を取得する。
     *
     * @return 現在日付
     */
    Date readCurrentDate();

    /**
     * ジョブ管理テーブルを更新する。
     *
     * @param batchJobManagementUpdateParam ジョブ管理テーブルの更新条件・内容
     * @return 更新件数
     */
    int updateJobTable(
            BatchJobManagementUpdateParam batchJobManagementUpdateParam);
}
