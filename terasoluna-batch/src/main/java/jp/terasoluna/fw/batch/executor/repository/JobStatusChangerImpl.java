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

    private static final int EXPECTED_UPDATE_JOB_COUNT = 1;

    private static final TLogger LOGGER = TLogger
            .getLogger(JobStatusChangerImpl.class);

    protected SystemDao systemDao;

    protected PlatformTransactionManager adminTransactionManager;

    /**
     * メソッド呼び出し時の引数
     */
    private class ChangeJobStatusParam {
        String jobSequenceId;

        String eventStatus;

        String targetJobStatus;

        String nextJobStatus;

        BLogicResult bLogicResult;

        BatchJobData batchJobData;

        String statusForUpdate;
    }

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
     * BatchJobData取得
     * @param changeJobStatusParam BatchJobData取得用の引数
     * @return BatchJobData 取得したBatchJobData
     */
    private BatchJobData getBatchJobData(
            ChangeJobStatusParam changeJobStatusParam) {
        // ジョブデータ取得
        BatchJobManagementParam param = new BatchJobManagementParam();

        param.setJobSequenceId(changeJobStatusParam.jobSequenceId);
        param.setForUpdate(true);
        BatchJobData batchJobData = systemDao.selectJob(param);
        if (batchJobData == null) {
            LOGGER.error(LogId.EAL025026, changeJobStatusParam.jobSequenceId);
            return null;
        }

        return batchJobData;
    }

    /**
     * ステータス判定
     * @param changeJobStatusParam ステータス判定用の引数
     * @return 対象ステータスのときはtrue。BatchJobDataがnull、あるいは、ステータスが対象外のときはfalse。
     */
    private boolean isJobStatusValid(ChangeJobStatusParam changeJobStatusParam) {
        // ステータス判定
        if (changeJobStatusParam.batchJobData == null) {
            return false;
        }
        if (!changeJobStatusParam.targetJobStatus
                .equals(changeJobStatusParam.batchJobData.getCurAppStatus())) {
            LOGGER.info(LogId.IAL025004, changeJobStatusParam.jobSequenceId,
                    changeJobStatusParam.batchJobData.getBLogicAppStatus(),
                    changeJobStatusParam.eventStatus,
                    changeJobStatusParam.batchJobData.getCurAppStatus(),
                    "false");
            return false;
        }
        return true;
    }

    /**
     * ジョブステータス更新
     * @param changeJobStatusParam ジョブステータス更新用の引数
     * @return 正常に更新できた(更新件数が1)ときはtrue、更新件数が1以外のときはfalse
     */
    private boolean updateBatchJobStatus(
            ChangeJobStatusParam changeJobStatusParam) {
        // ジョブステータス更新
        LOGGER.debug(LogId.DAL025023, changeJobStatusParam.jobSequenceId,
                changeJobStatusParam.nextJobStatus);
        BatchJobManagementUpdateParam updateParam = new BatchJobManagementUpdateParam();

        updateParam.setJobSequenceId(changeJobStatusParam.jobSequenceId);
        updateParam.setCurAppStatus(changeJobStatusParam.statusForUpdate);
        int count = systemDao.updateJobTable(updateParam);
        if (count != EXPECTED_UPDATE_JOB_COUNT) {
            LOGGER.error(LogId.EAL025025, updateParam.getJobSequenceId(),
                    updateParam.getCurAppStatus());
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean changeToStartStatus(String jobSequenceId) {
        TransactionStatus transactionStatus = null;
        ChangeJobStatusParam changeJobStatusParam = new ChangeJobStatusParam();
        boolean returnValue = true;

        changeJobStatusParam.jobSequenceId = jobSequenceId;
        changeJobStatusParam.eventStatus = EventConstants.EVENT_STATUS_START;
        changeJobStatusParam.targetJobStatus = JobStatusConstants.JOB_STATUS_UNEXECUTION;
        changeJobStatusParam.nextJobStatus = JobStatusConstants.JOB_STATUS_EXECUTING;

        try {
            transactionStatus = adminTransactionManager
                    .getTransaction(new DefaultTransactionDefinition());

            changeJobStatusParam.batchJobData = getBatchJobData(changeJobStatusParam);
            if (isJobStatusValid(changeJobStatusParam)) {
                changeJobStatusParam.statusForUpdate = changeJobStatusParam.batchJobData
                        .getBLogicAppStatus();
                if (updateBatchJobStatus(changeJobStatusParam)) {
                    adminTransactionManager.commit(transactionStatus);
                }
            }
        } finally {
            if (transactionStatus != null && !transactionStatus.isCompleted()) {
                LOGGER.warn(LogId.WAL025013, jobSequenceId);
                adminTransactionManager.rollback(transactionStatus);
                returnValue = false;
            }
        }
        return returnValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean changeToEndStatus(String jobSequenceId,
            BLogicResult bLogicResult) {
        TransactionStatus transactionStatus = null;
        ChangeJobStatusParam changeJobStatusParam = new ChangeJobStatusParam();
        boolean returnValue = true;

        changeJobStatusParam.jobSequenceId = jobSequenceId;
        changeJobStatusParam.bLogicResult = bLogicResult;
        changeJobStatusParam.eventStatus = EventConstants.EVENT_STATUS_NORMAL_TERMINATION;
        changeJobStatusParam.targetJobStatus = JobStatusConstants.JOB_STATUS_EXECUTING;
        changeJobStatusParam.nextJobStatus = JobStatusConstants.JOB_STATUS_PROCESSED;

        try {
            transactionStatus = adminTransactionManager
                    .getTransaction(new DefaultTransactionDefinition());

            changeJobStatusParam.batchJobData = getBatchJobData(changeJobStatusParam);
            if (isJobStatusValid(changeJobStatusParam)) {
                changeJobStatusParam.statusForUpdate = Integer
                        .toString(changeJobStatusParam.bLogicResult
                                .getBlogicStatus());
                if (updateBatchJobStatus(changeJobStatusParam)) {
                    adminTransactionManager.commit(transactionStatus);
                }
            }
        } finally {
            if (transactionStatus != null && !transactionStatus.isCompleted()) {
                LOGGER.warn(LogId.WAL025013, jobSequenceId);
                adminTransactionManager.rollback(transactionStatus);
                returnValue = false;
            }
        }
        return returnValue;
    }

}
