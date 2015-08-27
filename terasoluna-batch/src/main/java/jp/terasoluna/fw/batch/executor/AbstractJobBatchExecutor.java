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

import jp.terasoluna.fw.batch.constants.EventConstants;
import jp.terasoluna.fw.batch.constants.JobStatusConstants;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.exception.BatchException;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.util.JobUtil;
import jp.terasoluna.fw.logger.TLogger;
import jp.terasoluna.fw.util.PropertyUtil;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 非同期バッチエグゼキュータ抽象クラス。<br>
 * <br>
 * 非同期ジョブ起動用のバッチエグゼキュータ。
 * @see jp.terasoluna.fw.batch.executor.BatchExecutor
 * @see jp.terasoluna.fw.batch.executor.AbstractBatchExecutor
 * @see jp.terasoluna.fw.batch.executor.AsyncBatchExecutor
 */
public abstract class AbstractJobBatchExecutor extends AbstractBatchExecutor {

    /**
     * ログ.
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(AbstractJobBatchExecutor.class);

    /**
     * ジョブの実行間隔（ミリ秒）取得用キー.
     */
    protected static final String JOB_INTERVAL_TIME = "polling.interval";

    /**
     * Executorの常駐モード時の終了フラグ監視ファイル（フルパスで記述）取得用キー.
     */
    protected static final String EXECUTOR_END_MONITORING_FILE = "executor.endMonitoringFile";

    /**
     * Executorのジョブ終了待ちチェック間隔（ミリ秒）取得用キー.
     */
    protected static final String EXECUTOR_JOB_TERMINATE_WAIT_INTERVAL_TIME = "executor.jobTerminateWaitInterval";

    /**
     * ジョブの実行間隔（ミリ秒）のデフォルト値
     */
    protected static final long DEFAULT_JOB_INTERVAL_TIME = 1000;

    /**
     * ジョブ実行後にGCを行う（true/false）のデフォルト値
     */
    protected static final boolean DEFAULT_JOB_AFTER_GC = true;

    /**
     * Executorの常駐モード時のジョブリスト取得間隔（ミリ秒）のデフォルト値
     */
    protected static final long DEFAULT_EXECUTOR_LOOP_INTERVAL_TIME = 1000;

    /**
     * Executorの常駐モード時の終了フラグ監視ファイル（フルパスで記述）のデフォルト値
     */
    protected static final String DEFAULT_EXECUTOR_END_MONITORING_FILE = null;

    /**
     * Executorのスレッドキューサイズチェック間隔（ミリ秒）のデフォルト値
     */
    protected static final long DEFAULT_EXECUTOR_QUEUE_CHECK_INTERVAL_TIME = 1000;

    /**
     * Executorのジョブ終了待ちチェック間隔（ミリ秒）のデフォルト値
     */
    protected static final long DEFAULT_EXECUTOR_JOB_TERMINATE_WAIT_INTERVAL_TIME = 5000;

    /**
     * ジョブの実行間隔（ミリ秒）
     */
    protected long jobIntervalTime = DEFAULT_JOB_INTERVAL_TIME;

    /**
     * Executorの常駐モード時の終了フラグ監視ファイル（フルパスで記述）
     */
    protected String executorEndMonitoringFile = DEFAULT_EXECUTOR_END_MONITORING_FILE;

    /**
     * Executorのジョブ終了待ちチェック間隔（ミリ秒）
     */
    protected long executorJobTerminateWaitIntervalTime = DEFAULT_EXECUTOR_JOB_TERMINATE_WAIT_INTERVAL_TIME;

    /**
     * 開始時のステータス変更を行うかどうか
     */
    protected boolean changeStartStatus = false;

    /**
     * コンストラクタ
     */
    protected AbstractJobBatchExecutor() {
        super();
        initParameter();
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.executor.AbstractBatchExecutor# initDefaultAppContext()
     */
    @Override
    protected void initDefaultAppContext() {
        // システムアプリケーションコンテキスト取得
        String defaultAppContextName = getDefaultBeanFileName();
        if (defaultAppContextName == null || "".equals(defaultAppContextName)) {
            LOGGER.error(LogId.EAL025003);
            return;
        }
        // データソースアプリケーションコンテキスト取得
        String dataSourceAppContextName = getDataSourceBeanFileName();
        if (dataSourceAppContextName == null
                || "".equals(dataSourceAppContextName)) {
            LOGGER.error(LogId.EAL025003);
            return;
        }

        defaultApplicationContext = getApplicationContext(
                defaultAppContextName, dataSourceAppContextName);
        if (defaultApplicationContext == null) {
            LOGGER.error(LogId.EAL025016, defaultAppContextName,
                    dataSourceAppContextName);
            return;
        }
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.executor.AbstractBatchExecutor# initSystemDatasourceDao()
     */
    @Override
    protected void initSystemDatasourceDao() {
        if (defaultApplicationContext == null) {
            return;
        }

        String systemDaoKey = PropertyUtil
                .getProperty(SYSTEM_DATASOURCE_DAO);
        String transactionManagerKey = PropertyUtil
                .getProperty(SYSTEM_DATASOURCE_TRANSACTION_MANAGER);

        // システムDAO取得
        if (systemDaoKey != null && systemDaoKey.length() != 0) {
            if (defaultApplicationContext.containsBean(systemDaoKey)) {
                try {
                    systemDao = defaultApplicationContext.getBean(
                            systemDaoKey, SystemDao.class);
                } catch (Throwable e) {
                    LOGGER.error(LogId.EAL025051, e, systemDaoKey);
                }
            }
        }

        // transactionManager取得
        if (transactionManagerKey != null
                && transactionManagerKey.length() != 0) {
            if (defaultApplicationContext.containsBean(transactionManagerKey)) {
                try {
                    sysTransactionManager = defaultApplicationContext
                            .getBean(transactionManagerKey,
                                    PlatformTransactionManager.class);
                } catch (Throwable e) {
                    LOGGER.error(LogId.EAL025019, e, transactionManagerKey);
                }
            }
        }

        if (systemDao == null) {
            LOGGER.error(LogId.EAL025052);
        }
        if (sysTransactionManager == null) {
            LOGGER.error(LogId.EAL025022);
        }
    }

    /**
     * 初期化
     */
    protected void initParameter() {
        // ジョブの実行間隔（ミリ秒）
        String jobIntervalTimeStr = PropertyUtil.getProperty(JOB_INTERVAL_TIME);
        if (jobIntervalTimeStr != null && jobIntervalTimeStr.length() != 0) {
            try {
                this.jobIntervalTime = Long.parseLong(jobIntervalTimeStr);
            } catch (NumberFormatException e) {
                this.jobIntervalTime = DEFAULT_JOB_INTERVAL_TIME;
            }
        } else {
            this.jobIntervalTime = DEFAULT_JOB_INTERVAL_TIME;
        }

        // Executorの常駐モード時の終了フラグ監視ファイル（フルパスで記述）
        String executorEndMonitoringFileStr = PropertyUtil
                .getProperty(EXECUTOR_END_MONITORING_FILE);
        if (executorEndMonitoringFileStr != null
                && executorEndMonitoringFileStr.length() != 0) {
            this.executorEndMonitoringFile = executorEndMonitoringFileStr;
        } else {
            this.executorEndMonitoringFile = DEFAULT_EXECUTOR_END_MONITORING_FILE;
        }

        // Executorのジョブ終了待ちチェック間隔（ミリ秒）
        String executorJobTerminateWaitIntervalTimeStr = PropertyUtil
                .getProperty(EXECUTOR_JOB_TERMINATE_WAIT_INTERVAL_TIME);
        if (executorJobTerminateWaitIntervalTimeStr != null
                && executorJobTerminateWaitIntervalTimeStr.length() != 0) {
            try {
                this.executorJobTerminateWaitIntervalTime = Long
                        .parseLong(executorJobTerminateWaitIntervalTimeStr);
            } catch (NumberFormatException e) {
                this.executorJobTerminateWaitIntervalTime = DEFAULT_EXECUTOR_JOB_TERMINATE_WAIT_INTERVAL_TIME;
            }
        } else {
            this.executorJobTerminateWaitIntervalTime = DEFAULT_EXECUTOR_JOB_TERMINATE_WAIT_INTERVAL_TIME;
        }

    }

    /**
     * <h6>バッチ実行.</h6>
     * @param jobSequenceId ジョブシーケンスコード
     * @return ビジネスロジック実行結果
     */
    public BLogicResult executeBatch(String jobSequenceId) {
        BLogicResult result = new BLogicResult();
        boolean st = false;

        LOGGER.info(LogId.IAL025001, jobSequenceId);

        // DAOが使用可能かチェック
        if (systemDao == null || sysTransactionManager == null) {
            LOGGER.error(LogId.EAL025023, jobSequenceId);
            return result;
        }

        // finallyブロックでの例外・ジョブステータス更新失敗時の中断判定
        boolean errorOccuresInFinally = false;
        try {
            // ジョブレコード取得
            BatchJobData jobRecord = null;
            try {
                jobRecord = JobUtil.selectJob(jobSequenceId, false,
                        systemDao);
            } catch (DataAccessException e) {
                LOGGER.error(LogId.EAL025049, jobSequenceId);
                return result;
            }
                    
            if (jobRecord == null) {
                LOGGER.error(LogId.EAL025024, jobSequenceId);
                return result;
            }

            if (changeStartStatus) {
                boolean status;
                try {
                    // ジョブステータス設定（開始）
                    status = startBatchStatus(jobSequenceId, systemDao, sysTransactionManager);
                } catch (DataAccessException e) {
                    LOGGER.error(LogId.EAL025050, e, jobSequenceId);
                    return result;
                } catch (TransactionException e) {
                    LOGGER.error(LogId.EAL025050, e, jobSequenceId);
                    return result;
                }
                if (!status) {
                    // ステータス更新失敗時の不整合状態検出時
                    LOGGER.error(LogId.EAL025050, jobSequenceId);
                    return result;
                }
            }

            // 念のためトリムする
            if (jobRecord.getJobAppCd() != null) {
                jobRecord.setJobAppCd(jobRecord.getJobAppCd().trim());
            }

            // バッチ処理実行
            result = executeBatch(jobRecord);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(LogId.DAL025021, result.getBlogicStatus());
            }
        } finally {
            try {
                // 処理済み →ジョブステータス設定（終了）
                st = endBatchStatus(jobSequenceId, result,
                        systemDao, sysTransactionManager);
            } catch (DataAccessException | TransactionException e) {
                LOGGER.error(LogId.EAL025025, e, jobSequenceId, result
                        .getBlogicStatus());
                errorOccuresInFinally = true;
            }
            if (!st) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(LogId.EAL025025, jobSequenceId, result
                            .getBlogicStatus());
                }
                errorOccuresInFinally = true;
            }
        }

        if (errorOccuresInFinally) {
            return result;
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(LogId.IAL025003, jobSequenceId, result
                    .getBlogicStatus());
        }
        return result;
    }

    /**
     * <h6>ジョブステータス更新（ジョブ開始）.</h6>
     * @param jobSequenceId 更新対象のジョブシーケンスコード
     * @param systemDao システム用DAO
     * @param transactionManager TransactionManager
     * @return ステータス更新が成功したらtrue
     */
    protected boolean startBatchStatus(String jobSequenceId, SystemDao systemDao,
                                       PlatformTransactionManager transactionManager) {
        return updateBatchStatus(jobSequenceId,
                EventConstants.EVENT_STATUS_START, null, systemDao,transactionManager);
    }

    /**
     * <h6>ジョブステータス更新（ジョブ終了）.</h6>
     * @param jobSequenceId 更新対象のジョブシーケンスコード
     * @param result ステータス
     * @param systemDao システム用DAO
     * @param transactionManager TransactionManager
     * @return ステータス更新が成功したらtrue
     */
    protected boolean endBatchStatus(String jobSequenceId, BLogicResult result,
            SystemDao systemDao, PlatformTransactionManager transactionManager) {
        String blogicStatus = null;
        if (result != null) {
            blogicStatus = Integer.toString(result.getBlogicStatus());
        }
        return updateBatchStatus(jobSequenceId,
                EventConstants.EVENT_STATUS_NORMAL_TERMINATION, blogicStatus,
                systemDao, transactionManager);
    }

    /**
     * <h6>ジョブステータス更新.</h6>
     * <p>
     * ステータス判定マップにしたがって、ジョブのステータスを反映
     * </p>
     * @param jobSequenceId 更新対象のジョブシーケンスコード
     * @param eventCode イベントコード
     * @param blogicStatus blogicの戻り値
     * @param systemDao SystemDAO
     * @param transactionManager TransactionManager
     * @return ステータス更新が成功したらtrue
     */
    protected boolean updateBatchStatus(String jobSequenceId, String eventCode,
            String blogicStatus, SystemDao systemDao, PlatformTransactionManager transactionManager) {
        TransactionStatus tranStatus = null;

        try {
            DefaultTransactionDefinition tranDef = new DefaultTransactionDefinition();

            // トランザクション開始
            tranStatus = transactionManager.getTransaction(tranDef);
            LOGGER.debug(LogId.DAL025022);

            // ジョブレコード取得
            BatchJobData job = JobUtil.selectJob(jobSequenceId, true, systemDao);
            if (job == null) {
                LOGGER.error(LogId.EAL025026, jobSequenceId);
                return false;
            }

            // ステータス判定処理
            String changeStatus = judgmentStatus(job, jobSequenceId, eventCode,
                    blogicStatus);
            if (changeStatus == null) {
                // ステータス不整合によるバッチ実行スキップ
                return false;
            }

            LOGGER.debug(LogId.DAL025023, jobSequenceId, changeStatus);

            // ステータス更新
            JobUtil.updateJobStatus(job.getJobSequenceId(), changeStatus, blogicStatus, systemDao);

            // トランザクションコミット
            transactionManager.commit(tranStatus);
            LOGGER.debug(LogId.DAL025024);
        } catch (Exception e) {
            LOGGER.error(LogId.EAL025027, e);
            if (e instanceof DataAccessException) {
                throw (DataAccessException) e;
            } else if (e instanceof TransactionException) {
                throw (TransactionException) e;
            }
            throw new BatchException(e);
        } finally {
            try {
                // トランザクション終了（トランザクションが未完了の場合はロールバックする）
                if (tranStatus != null && !tranStatus.isCompleted()) {
                    transactionManager.rollback(tranStatus);
                }
                LOGGER.debug(LogId.DAL025025);
            } catch (Exception e) {
                LOGGER.error(LogId.EAL025028, e);
            }
        }
        return true;
    }

    /**
     * <h6>ジョブステータスの更新判定メソッド</h6> イベントコードとジョブステータスを確認し、ジョブステータスの更新が必要か判定を行う。<br>
     * 更新が必要ない場合はInfoログを出力し、nullを返却する。
     * @param job ジョブレコード
     * @param jobSequenceId 更新対象のジョブシーケンスコード
     * @param eventCode イベントコード
     * @param blogicStatus blogicの戻り値
     * @return
     */
    protected String judgmentStatus(BatchJobData job, String jobSequenceId,
            String eventCode, String blogicStatus) {

        String judge = null;

        if (EventConstants.EVENT_STATUS_START.equals(eventCode)) {
            if (JobStatusConstants.JOB_STATUS_UNEXECUTION.equals(job
                    .getCurAppStatus())) {
                judge = JobStatusConstants.JOB_STATUS_EXECUTING;
            } else {
                judge = null;
            }
        } else {
            if (JobStatusConstants.JOB_STATUS_EXECUTING.equals(job
                    .getCurAppStatus())) {
                judge = JobStatusConstants.JOB_STATUS_PROCESSED;
            } else {
                judge = null;
            }
        }

        // ステータスNGチェック
        if (judge == null) {
            // INFOじゃなくない？
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(LogId.IAL025004, jobSequenceId, blogicStatus,
                        eventCode, job.getCurAppStatus(), judge);
            }
            return null;
        }

        return judge.toString();
    }

    /**
     * ジョブの実行間隔（ミリ秒）
     * @return the jobIntervalTime
     */
    public long getJobIntervalTime() {
        return jobIntervalTime;
    }

    /**
     * Executorの常駐モード時の終了フラグ監視ファイル（フルパスで記述）
     * @return the executorEndMonitoringFile
     */
    public String getExecutorEndMonitoringFile() {
        return executorEndMonitoringFile;
    }

    /**
     * Executorのジョブ終了待ちチェック間隔（ミリ秒）
     * @return the executorJobTerminateWaitIntervalTime
     */
    public long getExecutorJobTerminateWaitIntervalTime() {
        return executorJobTerminateWaitIntervalTime;
    }

    /**
     * 開始時のステータス変更を行うかどうか
     * @param changeStartStatus the changeStartStatus to set
     */
    public void setChangeStartStatus(boolean changeStartStatus) {
        this.changeStartStatus = changeStartStatus;
    }

}
