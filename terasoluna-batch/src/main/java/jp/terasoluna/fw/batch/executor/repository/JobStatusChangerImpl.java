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

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import jp.terasoluna.fw.batch.constants.EventConstants;
import jp.terasoluna.fw.batch.constants.JobStatusConstants;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementParam;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementUpdateParam;
import jp.terasoluna.fw.logger.TLogger;

/**
 * ジョブの実行ステータス更新クラス。<br>
 * @since 3.6
 * @see JobStatusChanger
 */
public class JobStatusChangerImpl implements JobStatusChanger {

    private static final int EXPTECTED_UPDATE_JOB_COUNT = 1;

    private static final TLogger LOGGER = TLogger
            .getLogger(JobStatusChangerImpl.class);

    protected SystemDao systemDao;

    protected PlatformTransactionManager adminTransactionManager;

    /**
     * コンストラクタ。
     */
    public JobStatusChangerImpl(SystemDao systemDao,
            PlatformTransactionManager adminTransactionManager) {
        super();
        this.systemDao = systemDao;
        this.adminTransactionManager = adminTransactionManager;
        Assert.notNull(systemDao, LOGGER.getLogMessage(LogId.EAL025089,
                "JobStatusChangerImpl", "systemDao"));
        Assert.notNull(adminTransactionManager, LOGGER.getLogMessage(
                LogId.EAL025089, "JobStatusChangerImpl",
                "adminTransactionManager"));
    }
    
    /**
     * ステータス更新処理
     * @param event EVENT_STATUS_START or EVENT_STATUS_NORMAL_TERMINATION
     * @param jobSequenceId ジョブシーケンスID
     * @param bLogicResult ビジネスロジックの実行結果 (EVENT_STATUS_NORMAL_TERMINATIONのときのみ)
     * @return 更新に成功したらtrue。<br>
     *         BatchJobDataが取得できないとき、ジョブステータスが想定外のとき、ジョブステータスの更新が正常に行えなかったときはfalse。
     */
    private boolean changeJobStatus(String event, String jobSequenceId,
            BLogicResult bLogicResult) {
        String targetStatus = null;
        String targetEvent = null;
        String nextStatus = null;
        TransactionStatus transactionStatus = null;

        targetEvent = event;
        if (EventConstants.EVENT_STATUS_START.equals(event)) {
            targetStatus = JobStatusConstants.JOB_STATUS_UNEXECUTION;
            nextStatus = JobStatusConstants.JOB_STATUS_EXECUTING;
        } else {
            targetStatus = JobStatusConstants.JOB_STATUS_EXECUTING;
            nextStatus = JobStatusConstants.JOB_STATUS_PROCESSED;
        }

        try {
            transactionStatus = adminTransactionManager
                    .getTransaction(new DefaultTransactionDefinition());

            // ジョブデータ取得
            BatchJobManagementParam param = new BatchJobManagementParam();
            param.setJobSequenceId(jobSequenceId);
            param.setForUpdate(true);
            BatchJobData batchJobData = systemDao.selectJob(param);
            if (batchJobData == null) {
                LOGGER.error(LogId.EAL025026, jobSequenceId);
                return false;
            }

            // 開始前ステータス判定
            if (!targetStatus.equals(batchJobData.getCurAppStatus())) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(LogId.IAL025004, jobSequenceId, batchJobData
                            .getBLogicAppStatus(), targetEvent, batchJobData
                            .getCurAppStatus(), targetStatus);
                }
                return false;
            }

            // ジョブステータス更新
            LOGGER.debug(LogId.DAL025023, jobSequenceId, nextStatus);
            BatchJobManagementUpdateParam updateParam = new BatchJobManagementUpdateParam();
            updateParam.setJobSequenceId(batchJobData.getJobSequenceId());
            if (EventConstants.EVENT_STATUS_START.equals(event)) {
                updateParam.setBLogicAppStatus(batchJobData
                        .getBLogicAppStatus());
            } else {
                updateParam.setBLogicAppStatus(Integer.toString(bLogicResult
                        .getBlogicStatus()));
            }
            updateParam.setCurAppStatus(nextStatus);
            int count = systemDao.updateJobTable(updateParam);
            if (count != EXPTECTED_UPDATE_JOB_COUNT) {
                LOGGER.error(LogId.EAL025025, updateParam.getJobSequenceId(),
                        updateParam.getCurAppStatus());
                return false;
            }
            adminTransactionManager.commit(transactionStatus);
        } finally {
            if (transactionStatus != null && !transactionStatus.isCompleted()) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(LogId.IAL025019, transactionStatus);
                }
                adminTransactionManager.rollback(transactionStatus);
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean changeToStartStatus(String jobSequenceId) {
        return changeJobStatus(EventConstants.EVENT_STATUS_START,
                jobSequenceId, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean changeToEndStatus(String jobSequenceId,
            BLogicResult bLogicResult) {
        return changeJobStatus(EventConstants.EVENT_STATUS_NORMAL_TERMINATION,
                jobSequenceId, bLogicResult);
    }

}
