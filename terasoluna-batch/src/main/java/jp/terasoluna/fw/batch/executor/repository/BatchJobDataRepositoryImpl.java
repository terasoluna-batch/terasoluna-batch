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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import jp.terasoluna.fw.batch.constants.JobStatusConstants;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListParam;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementParam;

/**
 * ジョブパラメータ解決の実装クラス。<br>
 * @see jp.terasoluna.fw.batch.executor.repository.BatchJobDataRepository
 * @since 3.6
 */
public class BatchJobDataRepositoryImpl implements BatchJobDataRepository {

    @Autowired
    protected SystemDao systemDao;

    /**
     * ジョブ起動引数である{@code String}配列からジョブリスト取得用DAOの出力パラメータを取得する。<br>
     * @param args ジョブ起動引数
     * @return ジョブリスト取得用DAOの出力パラメータ
     */
    @Override
    public BatchJobListResult resolveBatchJobResult(String[] args) {
        String jobAppCd = null;
        if (args != null && args.length > 0) {
            jobAppCd = args[0];
        }
        BatchJobListParam param = new BatchJobListParam();
        param.setJobAppCd(jobAppCd);
        param.setCurAppStatusList(new ArrayList<String>() {
            {
                add(JobStatusConstants.JOB_STATUS_UNEXECUTION);
            }
        });
        List<BatchJobListResult> resultList = systemDao.selectJobList(param);
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    /**
     * ジョブ取得用DAOの入力パラメータからジョブパラメータを取得する。<br>
     * @param jobSequenceId ジョブのシーケンスID
     * @return ジョブパラメータ
     */
    @Override
    public BatchJobData resolveBatchJobData(String jobSequenceId) {
        BatchJobManagementParam batchJobManagementParam = new BatchJobManagementParam();
        batchJobManagementParam.setJobSequenceId(jobSequenceId);

        BatchJobData batchJobData = systemDao
                .selectJob(batchJobManagementParam);

        // 念のためトリムする
        if (batchJobData.getJobAppCd() != null) {
            batchJobData.setJobAppCd(batchJobData.getJobAppCd().trim());
        }

        return batchJobData;
    }

}
