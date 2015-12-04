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

import static jp.terasoluna.fw.batch.constants.JobStatusConstants.*;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

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

    private static final TLogger LOGGER = TLogger.getLogger(
            JobStatusChangerImpl.class);

    protected SystemDao systemDao;

    protected PlatformTransactionManager adminTransactionManager;

    /**
     * ステータス変更契機：起動時.
     */
    public static final String EVENT_STATUS_START = "0";

    /**
     * ステータス変更契機：正常終了時.
     */
    public static final String EVENT_STATUS_NORMAL_TERMINATION = "1";

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
     * {@inheritDoc}
     * この実装では、引数のNULLチェックを行わないため呼出し側でチェックする必要がある。<br>
     */
    @Override
    public boolean changeToStartStatus(String jobSequenceId) {
        TransactionStatus transactionStatus = null;
        try {
            transactionStatus = adminTransactionManager.getTransaction(
                    new DefaultTransactionDefinition());

            BatchJobData batchJobData = getBatchJobData(jobSequenceId);
            if (!isJobStatusValid(batchJobData, JOB_STATUS_UNEXECUTION,
                    EVENT_STATUS_START)) {
                return false;
            }

            if (!updateBatchJobStatus(jobSequenceId, batchJobData
                    .getBLogicAppStatus(), JOB_STATUS_EXECUTING)) {
                return false;
            }
            adminTransactionManager.commit(transactionStatus);
        } finally {
            if (transactionStatus != null && !transactionStatus.isCompleted()) {
                LOGGER.warn(LogId.WAL025013, jobSequenceId);
                adminTransactionManager.rollback(transactionStatus);
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * この実装では、引数のNULLチェックを行わないため呼出し側でチェックする必要がある。<br>
     */
    @Override
    public boolean changeToEndStatus(String jobSequenceId,
            BLogicResult blogicResult) {
        TransactionStatus transactionStatus = null;

        try {
            transactionStatus = adminTransactionManager.getTransaction(
                    new DefaultTransactionDefinition());

            BatchJobData batchJobData = getBatchJobData(jobSequenceId);
            if (!isJobStatusValid(batchJobData, JOB_STATUS_EXECUTING,
                    EVENT_STATUS_NORMAL_TERMINATION)) {
                return false;
            }
            String appStatus = Integer.toString(blogicResult.getBlogicStatus());
            if (!updateBatchJobStatus(jobSequenceId, appStatus,
                    JOB_STATUS_PROCESSED)) {
                return false;
            }
            adminTransactionManager.commit(transactionStatus);
        } finally {
            if (transactionStatus != null && !transactionStatus.isCompleted()) {
                LOGGER.warn(LogId.WAL025013, jobSequenceId);
                adminTransactionManager.rollback(transactionStatus);
            }
        }
        return true;
    }

    /**
     * BatchJobData取得
     * 
     * @param jobSequenceId ジョブシーケンスID
     * @return BatchJobData 取得したBatchJobData
     */
    private BatchJobData getBatchJobData(String jobSequenceId) {
        // ジョブデータ取得
        BatchJobManagementParam param = new BatchJobManagementParam();
        param.setJobSequenceId(jobSequenceId);
        param.setForUpdate(true);

        BatchJobData batchJobData = systemDao.selectJob(param);

        if (batchJobData == null) {
            LOGGER.error(LogId.EAL025026, jobSequenceId);
            return null;
        }

        return batchJobData;
    }

    /**
     * ステータス判定
     * 
     * @param batchJobData ジョブ実行時のパラメータ
     * @param expectJobStatus 期待するジョブのステータス
     * @param eventName ステータス変更契機の名称
     * @return 対象ステータスのときはtrue。BatchJobDataがnull、あるいは、ステータスが対象外のときはfalse。
     */
    private boolean isJobStatusValid(BatchJobData batchJobData,
            String expectJobStatus, String eventName) {
        // ステータス判定
        if (batchJobData == null) {
            return false;
        }
        if (!expectJobStatus.equals(batchJobData.getCurAppStatus())) {
            LOGGER.info(LogId.IAL025004, batchJobData.getJobSequenceId(),
                    batchJobData.getBLogicAppStatus(), eventName, batchJobData
                            .getCurAppStatus(), "false");
            return false;
        }
        return true;
    }

    /**
     * ジョブステータス更新
     * 
     * @param jobSequenceId ジョブシーケンスID
     * @param appStatus blogicAppStatusの更新後の値
     * @param nextStatus curAppStatusの更新後の値
     * @return 正常に更新できた(更新件数が1)ときはtrue、更新件数が1以外のときはfalse
     */
    private boolean updateBatchJobStatus(String jobSequenceId, String appStatus,
            String nextStatus) {
        // ジョブステータス更新
        LOGGER.debug(LogId.DAL025023, jobSequenceId, nextStatus);
        BatchJobManagementUpdateParam updateParam = new BatchJobManagementUpdateParam();

        updateParam.setJobSequenceId(jobSequenceId);
        updateParam.setBLogicAppStatus(appStatus);
        updateParam.setCurAppStatus(nextStatus);

        int count = systemDao.updateJobTable(updateParam);
        if (count != EXPECTED_UPDATE_JOB_COUNT) {
            LOGGER.error(LogId.EAL025025, jobSequenceId, appStatus);
            return false;
        }
        return true;
    }

}
