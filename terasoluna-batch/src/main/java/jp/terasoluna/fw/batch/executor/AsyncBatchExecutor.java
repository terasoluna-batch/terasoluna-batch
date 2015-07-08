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

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.concurrent.BatchServant;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;
import jp.terasoluna.fw.batch.util.BatchUtil;
import jp.terasoluna.fw.batch.util.JobUtil;
import jp.terasoluna.fw.logger.TLogger;
import jp.terasoluna.fw.util.PropertyUtil;

import org.apache.commons.logging.Log;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;

/**
 * 非同期バッチエグゼキュータ。<br>
 * <br>
 * 常駐プロセスとして起動し、ジョブ管理テーブルに登録されたジョブを取得し、ジョブの実行をBatchServantクラスに移譲する。<br>
 * またジョブ管理テーブルにジョブ実行結果を更新する。<br>
 * @see jp.terasoluna.fw.batch.executor.AbstractJobBatchExecutor
 */
public class AsyncBatchExecutor extends AbstractJobBatchExecutor {

    /**
     * ロガー.
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(AsyncBatchExecutor.class);

    /**
     * タスクエグゼキュータのBean名
     */
    private static final String BATCH_TASK_EXECUTOR = "batchTaskExecutor.default";

    /**
     * スレッド実行用のBatchServantクラスのBean名
     */
    private static final String BATCH_TASK_SERVANT = "batchTaskExecutor.batchServant";

    /**
     * データベース異常時のリトライ回数定義名
     */
    private static final String BATCH_DB_ABNORMAL_RETRY_MAX = "batchTaskExecutor.dbAbnormalRetryMax";

    /**
     * データベース異常時のリトライ間隔定義名
     */
    private static final String BATCH_DB_ABNORMAL_RETRY_INTERVAL = "batchTaskExecutor.dbAbnormalRetryInterval";

    /**
     * データベース異常時のリトライ回数をリセットする前回からの発生間隔定義名
     */
    private static final String BATCH_DB_ABNORMAL_RETRY_RESET = "batchTaskExecutor.dbAbnormalRetryReset";

    /**
     * ジョブ実行リトライ間隔（ミリ秒）定義名
     */
    private static final String BATCH_EXECUTE_RETRY_INTERVAL = "batchTaskExecutor.executeRetryInterval";

    /**
     * ジョブ実行リトライ回数定義名
     */
    private static final String BATCH_EXECUTE_RETRY_COUNTMAX = "batchTaskExecutor.executeRetryCountMax";

    /**
     * 空きスレッド残数閾値のデフォルト値
     */
    private static final String BATCH_AVAILABLE_THREADTHRESHOLD_COUNT = "batchTaskExecutor.availableThreadThresholdCount";

    /**
     * 空きスレッド残数閾値以下の場合のウェイト時間（ミリ秒）のデフォルト値
     */
    private static final String BATCH_AVAILABLE_THREADTHRESHOLD_WAIT = "batchTaskExecutor.availableThreadThresholdWait";

    /**
     * データベース異常時のリトライ回数のデフォルト値
     */
    private static final long BATCH_DB_ABNORMAL_RETRY_MAX_DEFAULT = 0;

    /**
     * データベース異常時のリトライ間隔のデフォルト値（ミリ秒）
     */
    private static final long BATCH_DB_ABNORMAL_RETRY_INTERVAL_DEFAULT = 20000;

    /**
     * データベース異常時のリトライ回数をリセットする前回からの発生間隔のデフォルト値（ミリ秒）
     */
    private static final long BATCH_DB_ABNORMAL_RETRY_RESET_DEFAULT = 600000;

    /**
     * プロセス終了コード（正常）
     */
    private static final int PROCESS_END_STATUS_NORMAL = 0;

    /**
     * プロセス終了コード（異常）
     */
    private static final int PROCESS_END_STATUS_FAILURE = 255;

    /**
     * ジョブ実行リトライ間隔（ミリ秒）のデフォルト値
     */
    protected static final long DEFAULT_EXECUTE_RETRY_INTERVAL = 1000;

    /**
     * ジョブ実行リトライ回数のデフォルト値
     */
    protected static final long DEFAULT_EXECUTE_RETRY_COUNTMAX = 10;

    /**
     * 空きスレッド残数閾値のデフォルト値
     */
    protected static final long DEFAULT_AVAILABLE_THREADTHRESHOLD_COUNT = 1;

    /**
     * 空きスレッド残数閾値以下の場合のウェイト時間（ミリ秒）のデフォルト値
     */
    protected static final long DEFAULT_AVAILABLE_THREADTHRESHOLD_WAIT = 100;

    /** スレッドグループプリフィックス. */
    public static final String THREAD_GROUP_PREFIX = AsyncBatchExecutor.class
            .getSimpleName()
            + "ThreadGroup";

    /** スレッド名プリフィックス. */
    public static final String THREAD_NAME_PREFIX = AsyncBatchExecutor.class
            .getSimpleName()
            + "Thread";

    /** スレッドグループセパレータ. */
    public static final String THREAD_GROUP_SEPARATOR = "-";

    /** スレッド名セパレータ. */
    public static final String THREAD_NAME_SEPARATOR = "-";

    /** スレッドグループ番号. */
    protected static AtomicInteger threadGroupNo = new AtomicInteger(0);

    /**
     * ジョブ実行リトライ間隔（ミリ秒）
     */
    protected static long executeRetryInterval = DEFAULT_EXECUTE_RETRY_INTERVAL;

    /**
     * ジョブ実行リトライ回数
     */
    protected static long executeRetryCountMax = DEFAULT_EXECUTE_RETRY_COUNTMAX;

    /**
     * 空きスレッド残数閾値
     */
    protected static long availableThreadThresholdCount = DEFAULT_AVAILABLE_THREADTHRESHOLD_COUNT;

    /**
     * 空きスレッド残数閾値以下の場合のウェイト時間（ミリ秒）
     */
    protected static long availableThreadThresholdWait = DEFAULT_AVAILABLE_THREADTHRESHOLD_WAIT;

    /**
     * 破棄候補の(activeCountが0になったら破棄してよい)ThreadGroupのList。
     */
    protected static List<ThreadGroup> destroyCandidateThreadGroupList = new LinkedList<ThreadGroup>();

    /**
     * コンストラクタ
     */
    protected AsyncBatchExecutor() {
        super();
    }

    /**
     * メインメソッド.
     * @param args
     */
    public static void main(String[] args) {
        long retryCount = 0;
        long retryCountMax = BATCH_DB_ABNORMAL_RETRY_MAX_DEFAULT;
        long retryCountReset = BATCH_DB_ABNORMAL_RETRY_RESET_DEFAULT;
        long retryInterval = BATCH_DB_ABNORMAL_RETRY_INTERVAL_DEFAULT;
        int status = PROCESS_END_STATUS_FAILURE;
        long lastExceptionTime = System.currentTimeMillis();

        // プロパティから値を取得
        String dbAbnormalRetryMaxStr = PropertyUtil
                .getProperty(BATCH_DB_ABNORMAL_RETRY_MAX);
        String dbAbnormalRetryIntervalStr = PropertyUtil
                .getProperty(BATCH_DB_ABNORMAL_RETRY_INTERVAL);
        String dbAbnormalRetryResetStr = PropertyUtil
                .getProperty(BATCH_DB_ABNORMAL_RETRY_RESET);
        String executeRetryIntervalStr = PropertyUtil
                .getProperty(BATCH_EXECUTE_RETRY_INTERVAL);
        String executeRetryCountMaxStr = PropertyUtil
                .getProperty(BATCH_EXECUTE_RETRY_COUNTMAX);
        String availableThreadThresholdCountStr = PropertyUtil
                .getProperty(BATCH_AVAILABLE_THREADTHRESHOLD_COUNT);
        String availableThreadThresholdWaitStr = PropertyUtil
                .getProperty(BATCH_AVAILABLE_THREADTHRESHOLD_WAIT);

        // データベース異常時のリトライ回数
        if (dbAbnormalRetryMaxStr != null
                && dbAbnormalRetryMaxStr.length() != 0) {
            try {
                retryCountMax = Long.parseLong(dbAbnormalRetryMaxStr);
            } catch (NumberFormatException e) {
                LOGGER.error(LogId.EAL025046, e, BATCH_DB_ABNORMAL_RETRY_MAX,
                        dbAbnormalRetryMaxStr);
                System.exit(status);
                return;
            }
        }

        // データベース異常時のリトライ間隔（ミリ秒）
        if (dbAbnormalRetryIntervalStr != null
                && dbAbnormalRetryIntervalStr.length() != 0) {
            try {
                retryInterval = Long.parseLong(dbAbnormalRetryIntervalStr);
            } catch (NumberFormatException e) {
                LOGGER.error(LogId.EAL025046, e,
                        BATCH_DB_ABNORMAL_RETRY_INTERVAL,
                        dbAbnormalRetryIntervalStr);
                System.exit(status);
                return;
            }
        }

        // データベース異常時のリトライ回数をリセットする前回からの発生間隔（ミリ秒）
        if (dbAbnormalRetryResetStr != null
                && dbAbnormalRetryResetStr.length() != 0) {
            try {
                retryCountReset = Long.parseLong(dbAbnormalRetryResetStr);
            } catch (NumberFormatException e) {
                LOGGER.error(LogId.EAL025046, e, BATCH_DB_ABNORMAL_RETRY_RESET,
                        dbAbnormalRetryResetStr);
                System.exit(status);
                return;
            }
        }

        // ジョブ実行リトライ間隔（ミリ秒）
        if (executeRetryIntervalStr != null
                && executeRetryIntervalStr.length() != 0) {
            try {
                executeRetryInterval = Long.parseLong(executeRetryIntervalStr);
            } catch (NumberFormatException e) {
                LOGGER.error(LogId.EAL025046, e, BATCH_EXECUTE_RETRY_INTERVAL,
                        executeRetryIntervalStr);
                System.exit(status);
                return;
            }
        }

        // ジョブ実行リトライ回数
        if (executeRetryCountMaxStr != null
                && executeRetryCountMaxStr.length() != 0) {
            try {
                executeRetryCountMax = Long.parseLong(executeRetryCountMaxStr);
            } catch (NumberFormatException e) {
                LOGGER.error(LogId.EAL025046, e, BATCH_EXECUTE_RETRY_COUNTMAX,
                        executeRetryCountMaxStr);
                System.exit(status);
                return;
            }
        }

        // 空きスレッド残数閾値
        if (availableThreadThresholdCountStr != null
                && availableThreadThresholdCountStr.length() != 0) {
            try {
                availableThreadThresholdCount = Long
                        .parseLong(availableThreadThresholdCountStr);
            } catch (NumberFormatException e) {
                LOGGER.error(LogId.EAL025046, e,
                        BATCH_AVAILABLE_THREADTHRESHOLD_COUNT,
                        availableThreadThresholdCountStr);
                System.exit(status);
                return;
            }
        }

        // 空きスレッド残数閾値以下の場合のウェイト時間（ミリ秒）
        if (availableThreadThresholdWaitStr != null
                && availableThreadThresholdWaitStr.length() != 0) {
            try {
                availableThreadThresholdWait = Long
                        .parseLong(availableThreadThresholdWaitStr);
            } catch (NumberFormatException e) {
                LOGGER.error(LogId.EAL025046, e,
                        BATCH_AVAILABLE_THREADTHRESHOLD_WAIT,
                        availableThreadThresholdWaitStr);
                System.exit(status);
                return;
            }
        }

        do {
            try {
                status = executorMain(args);
                break;
            } catch (RetryableExecuteException e) {
                Throwable cause = e.getCause();
                // 前回から指定時間以上経過していたらリトライ回数リセット
                if ((System.currentTimeMillis() - lastExceptionTime) > retryCountReset) {
                    retryCount = 0;
                }
                lastExceptionTime = System.currentTimeMillis();

                // リトライ回数チェック
                if (retryCount >= retryCountMax) {
                    LOGGER.error(LogId.EAL025031, cause);
                    break;
                }

                // スリープ時間待つ
                try {
                    Thread.sleep(retryInterval);
                } catch (InterruptedException e1) {
                    // なにもしない
                }

                retryCount++;

                LOGGER.info(LogId.IAL025017, retryCount, retryCountMax,
                        retryCountReset, retryInterval);
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(LogId.TAL025010, BatchUtil.getMemoryInfo());
                }
            }
        } while (true);

        System.exit(status);
    }

    /**
     * エグゼキュータメインメソッド.
     * @param args
     */
    public static int executorMain(String[] args) {
        int status = PROCESS_END_STATUS_FAILURE;
        Exception exception = null;
        String jobAppCd = null;
        String batchTaskExecutorName = null;
        String batchTaskServantName = null;
        ThreadPoolTaskExecutor taskExecutor = null;

        LOGGER.info(LogId.IAL025005);

        // 第1引数からジョブ業務コードを取得
        if (args.length > 0) {
            jobAppCd = args[0];
        }

        // 引数に指定されていない場合は環境変数からジョブ業務コードを取得
        if (jobAppCd == null || jobAppCd.length() == 0) {
            jobAppCd = JobUtil.getenv(ENV_JOB_APP_CD);
            if (jobAppCd != null && jobAppCd.length() == 0) {
                jobAppCd = null;
            }
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(LogId.IAL025006, jobAppCd == null ? "" : jobAppCd);
        }

        // エグゼキュータ生成
        AsyncBatchExecutor executor = new AsyncBatchExecutor();

        // システムDAOを取得
        SystemDao systemDao = executor.getSystemDao();
        if (systemDao == null) {
            LOGGER.info(LogId.IAL025018);
            return status;
        }

        if (executor.getSysTransactionManager() == null) {
            LOGGER.info(LogId.IAL025016);
            return status;
        }

        // タスクエグゼキュータとBatchServantクラスのBean名取得
        batchTaskExecutorName = PropertyUtil.getProperty(BATCH_TASK_EXECUTOR);
        batchTaskServantName = PropertyUtil.getProperty(BATCH_TASK_SERVANT);

        // タスクエグゼキュータ取得
        ApplicationContext ctx = executor.getDefaultApplicationContext();
        if (ctx != null) {
            if (ctx.containsBean(batchTaskExecutorName)) {
                Object batchTaskExecutorObj = null;
                try {
                    batchTaskExecutorObj = ctx.getBean(batchTaskExecutorName,
                            ThreadPoolTaskExecutor.class);
                } catch (BeansException e) {
                    LOGGER.error(LogId.EAL025029, e, batchTaskExecutorName);
                }
                if (batchTaskExecutorObj instanceof ThreadPoolTaskExecutor) {
                    taskExecutor = (ThreadPoolTaskExecutor) batchTaskExecutorObj;
                }
            }
        }
        if (taskExecutor == null) {
            LOGGER.info(LogId.IAL025009);
            return status;
        }

        try {
            do {
                // ジョブリストから1件のみ取得（実行スレッドに空きがある場合のみ取得する）
                List<BatchJobListResult> jobList = null;
                if (checkTaskQueue(taskExecutor)) {
                    if (jobAppCd == null) {
                        jobList = JobUtil.selectJobList(systemDao, 0, 1);
                    } else {
                        jobList = JobUtil.selectJobList(jobAppCd, systemDao, 0,
                                1);
                    }
                }

                if (jobList != null && !jobList.isEmpty()) {
                    // リストの１件目のみ取得
                    BatchJobListResult batchJobListResult = jobList.get(0);

                    if (batchJobListResult != null) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug(LogId.DAL025026, batchJobListResult
                                    .getJobSequenceId());
                        }

                        if (LOGGER.isDebugEnabled()) {
                            // スレッドプールタスクエグゼキュータのステータスをデバッグログに出力
                            logOutputTaskExecutor(LOGGER, taskExecutor);
                        }

                        // 実行スレッドに空きがあればバッチ処理実行
                        if (checkTaskQueue(taskExecutor)) {

                            // バッチ処理実行
                            boolean executeResult = executeJob(executor, ctx,
                                    taskExecutor, batchTaskServantName,
                                    batchJobListResult);
                            if (!executeResult) {
                                break;
                            }
                        }
                    }
                }

                // 終了フラグファイルチェック
                if (checkEndFile(executor.getExecutorEndMonitoringFile())) {
                    // 終了フラグファイル発見
                    LOGGER.info(LogId.IAL025011);
                    break;
                }

                // ジョブリストが空のとき or 実行スレッドに空きが無い場合 は指定時間ウェイト
                if (jobList == null || jobList.size() == 0) {
                    // ジョブの実行間隔（ミリ秒）
                    if (executor.getJobIntervalTime() >= 0) {
                        try {
                            Thread.sleep(executor.getJobIntervalTime());
                        } catch (InterruptedException e) {
                            // 割り込み受信で処理終了
                            if (LOGGER.isInfoEnabled()) {
                                LOGGER.info(LogId.IAL025012, e.getMessage());
                            }
                            break;
                        }
                    }
                }

                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(LogId.TAL025010, BatchUtil.getMemoryInfo());
                }
                // 常駐用にループ
            } while (true);
        } catch (Exception e) {
            // 一旦保存しておく
            exception = e;
        } finally {
            LOGGER.debug(LogId.DAL025028);

            // 終了時にタスクがはけるまで待つ
            taskExecutor.setWaitForTasksToCompleteOnShutdown(true);

            LOGGER.debug(LogId.DAL025029);

            taskExecutor.shutdown();

            LOGGER.debug(LogId.DAL025030);

            while (taskExecutor.getActiveCount() != 0) {
                try {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(LogId.DAL025031, taskExecutor
                                .getActiveCount());
                    }

                    Thread.sleep(executor
                            .getExecutorJobTerminateWaitIntervalTime());
                } catch (InterruptedException e) {
                }
            }

            closeRootApplicationContext(ctx);
        }

        if (exception != null) {
            if (exception instanceof DataAccessException) {
                throw new RetryableExecuteException(exception);
            } else if (exception instanceof TransactionException) {
                throw new RetryableExecuteException(exception);
            }
            Throwable cause = exception.getCause();
            if (cause != null && cause instanceof DataAccessException) {
                // 原因例外がDataAccessExceptionの場合はステータス開始・変更時の
                // DBフェイルオーバーを考慮し、リトライ例外としてメインメソッドにスローする。
                throw new RetryableExecuteException(cause);
            }
        } else {
            LOGGER.info(LogId.IAL025013);

            status = PROCESS_END_STATUS_NORMAL;
        }
        return status;
    }

    /**
     * バッチ処理実行.
     * @param executor AsyncBatchExecutor
     * @param ctx ApplicationContext
     * @param taskExecutor ThreadPoolTaskExecutor
     * @param batchTaskServantName String
     * @param batchJobListResult BatchJobListResult
     * @return boolean
     */
    protected static boolean executeJob(AsyncBatchExecutor executor,
            ApplicationContext ctx, ThreadPoolTaskExecutor taskExecutor,
            String batchTaskServantName, BatchJobListResult batchJobListResult) {
        boolean status = false;
        BatchServant job = null;

        // 破棄してよいThreadGroupを破棄
        destroyThreadGroupsIfPossible();

        if (executor == null || ctx == null || taskExecutor == null
                || batchTaskServantName == null || batchJobListResult == null) {
            return status;
        }

        // システムDAOを取得
        SystemDao systemDao = executor.getSystemDao();
        if (systemDao == null) {
            LOGGER.info(LogId.IAL025018);
            return status;
        }

        // PlatformTransactionManagerを取得
        PlatformTransactionManager transactionManager = executor
                .getSysTransactionManager();
        if (transactionManager == null) {
            LOGGER.info(LogId.IAL025016);
            return status;
        }

        // コンテキストからBatchServantインスタンス取得
        if (ctx != null) {
            try {
                job = (BatchServant) ctx.getBean(batchTaskServantName,
                        BatchServant.class);
            } catch (BeansException e) {
                LOGGER.error(LogId.EAL025030, e, batchTaskServantName);
                return status;
            }
        }

        if (job == null) {
            LOGGER.error(LogId.EAL025030, batchTaskServantName);
            return status;
        } else {
            // ジョブステータス設定（開始）
            boolean st = executor.startBatchStatus(batchJobListResult
                    .getJobSequenceId(), systemDao, transactionManager);
            if (st) {
                // BatchServantにジョブシーケンスコードを設定
                job.setJobSequenceId(batchJobListResult.getJobSequenceId());

                // スレッドグループプリフィックス設定
                StringBuilder tgn = new StringBuilder();
                tgn.append(THREAD_GROUP_PREFIX);
                tgn.append(THREAD_GROUP_SEPARATOR);
                tgn.append(threadGroupNo.incrementAndGet());
                taskExecutor.setThreadGroupName(tgn.toString());

                // スレッド名プリフィックス設定
                StringBuilder tn = new StringBuilder();
                tn.append(THREAD_NAME_PREFIX);
                tn.append(THREAD_NAME_SEPARATOR);
                tn.append(threadGroupNo.get());
                tn.append(THREAD_NAME_SEPARATOR);
                taskExecutor.setThreadNamePrefix(tn.toString());

                if (LOGGER.isDebugEnabled()) {
                    LOGGER
                            .debug(LogId.DAL025027, tgn.toString(), tn
                                    .toString());
                }

                long executeRetryCount = 0;
                do {
                    try {
                        // ジョブ実行
                        taskExecutor.execute(new BatchServantTaskEndTracker(job, taskExecutor.getThreadGroup()));
                        break;
                    } catch (TaskRejectedException tre) {

                        // リトライ回数チェック
                        if (executeRetryCount >= executeRetryCountMax) {
                            LOGGER.error(LogId.EAL025047, batchJobListResult
                                    .getJobSequenceId());
                            synchronized (destroyCandidateThreadGroupList) {
                                destroyCandidateThreadGroupList.add(taskExecutor.getThreadGroup());
                            }
                            return status;
                        }

                        // スリープ時間待つ
                        try {
                            Thread.sleep(executeRetryInterval);
                        } catch (InterruptedException e1) {
                            // なにもしない
                        }

                        executeRetryCount++;
                    }
                } while (true);

                // 返却ステータスを正常に
                status = true;
            } else {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(LogId.IAL025010, batchJobListResult
                            .getJobSequenceId());
                }
                // ジョブステータス更新失敗時ポーリングループは継続させる。
                status = true;
            }
        }
        return status;
    }

    /**
     * タスクエグゼキュータに空きがあるかチェック
     * @param taskExecutor タスクエグゼキュータ
     * @return 空きの有無
     */
    protected static boolean checkTaskQueue(ThreadPoolTaskExecutor taskExecutor) {
        int maxPoolSize = 0;
        int activeCount = 0;

        if (taskExecutor != null) {
            activeCount = taskExecutor.getActiveCount();
            maxPoolSize = taskExecutor.getMaxPoolSize();

            // スレッド空き数が閾値以下の場合はウェイト
            if ((maxPoolSize - activeCount) <= availableThreadThresholdCount) {
                // スリープ時間待つ
                try {
                    Thread.sleep(availableThreadThresholdWait);
                } catch (InterruptedException e1) {
                    // なにもしない
                }
            }

            // スレッドの空きチェック
            if (activeCount < maxPoolSize) {
                // 空きあり
                return true;
            }
            // スレッドの空きチェック
            if (taskExecutor.getThreadPoolExecutor().getQueue()
                    .remainingCapacity() > 0) {
                // 空きあり
                return true;
            }
        }

        // 空きなし
        return false;
    }

    /**
     * 終了フラグファイルチェック
     * @param endFilePath 終了フラグファイルパス
     * @return 終了フラグファイルチェック結果
     */
    protected static boolean checkEndFile(String endFilePath) {
        if (endFilePath != null && endFilePath.length() != 0) {
            File endFile = new File(endFilePath);
            return endFile.exists();
        }
        return false;
    }

    /**
     * スレッドプールタスクエグゼキュータのステータスをデバッグログに出力
     * @param log Log
     * @param taskExec ThreadPoolTaskExecutor
     */
    protected static void logOutputTaskExecutor(Log log,
            ThreadPoolTaskExecutor taskExec) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(LogId.DAL025032, taskExec.getActiveCount(), taskExec
                    .getCorePoolSize(), taskExec.getMaxPoolSize(), taskExec
                    .getPoolSize(), taskExec.getThreadPoolExecutor()
                    .getActiveCount(), taskExec.getThreadPoolExecutor()
                    .getTaskCount(), taskExec.getThreadPoolExecutor()
                    .getQueue().size(), taskExec.getThreadPoolExecutor()
                    .getQueue().remainingCapacity());
        }
    }

    /**
     * ApplicationContextをクローズする.
     * @param context
     */
    protected static void closeRootApplicationContext(ApplicationContext context) {
        if (context instanceof AbstractApplicationContext) {
            AbstractApplicationContext aac = (AbstractApplicationContext) context;
            aac.close();
            aac.destroy();
        }
    }

    /**
     * BatchServantのrunメソッドの終了を検知して、
     * ジョブ実行時に新たに作成されていたThreadGroupを、破棄候補に加えるためのクラス。
     */
    protected static class BatchServantTaskEndTracker implements Runnable {

        /**
         * 非同期で実行するBatchServant(委譲先)。
         */
        private BatchServant job = null;

        /**
         * ジョブ実行時に新たに作成されていたThreadGroup。
         */
        private ThreadGroup newThreadGroup = null;

        /**
         * コンストラクタ。
         * スレッドプールのスレッドが使いまわされるとき、
         * runメソッドを実行するスレッドのThreadGroupと、ジョブ実行時に新たに作成されていたThreadGroupは、
         * 同一ではなく、
         * ジョブ実行時に新たに作成されていたThreadGroupの方を破棄候補に加える必要があるため、
         * ジョブ実行時に新たに作成されていたThreadGroupを引数で受け取る。
         * @param job 非同期で実行するBatchServant(委譲先)(null以外)
         * @param newThreadGroup ジョブ実行時に新たに作成されていたThreadGroup(null以外)
         */
        public BatchServantTaskEndTracker(BatchServant job, ThreadGroup newThreadGroup) {
            this.job = job;
            this.newThreadGroup = newThreadGroup;
        }

        /**
         * BatchServantのrunメソッドの終了時に、
         * ジョブ実行時に新たに作成されていたThreadGroupを、破棄候補に加える。
         * @see java.lang.Runnable#run()
         */
        public void run() {
            try {
                job.run();
            } finally {
                synchronized (destroyCandidateThreadGroupList) {
                    destroyCandidateThreadGroupList.add(newThreadGroup);
                }
            }
        }
    }

    /**
     * 破棄候補の各ThreadGroupのアクティブスレッド数を検査し、
     * アクティブスレッド数が0のThreadGroupのdestroyメソッドを実行する。
     * (スレッドプールに保持されているアイドルスレッドも、アクティブスレッド数にカウントされる。)
     */
    protected static void destroyThreadGroupsIfPossible() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(LogId.DAL025054);
            logActiveThreadGroupsInfo();
        }

        synchronized (destroyCandidateThreadGroupList) {
            Iterator<ThreadGroup> it = destroyCandidateThreadGroupList.iterator();
            while (it.hasNext()) {
                ThreadGroup threadGroup = it.next();
                int activeCount = threadGroup.activeCount();
                
                // 検査時点で、アクティブスレッドが居ないスレッドグループのみdestroy。
                // ここでアクティブスレッドが残っているものは、
                // 次回このメソッドが実行されたときに検査する。
                if (activeCount == 0) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(LogId.DAL025058, threadGroup.getName(), activeCount);
                    }
                    threadGroup.destroy();
                    it.remove();
                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(LogId.DAL025059, threadGroup.getName(), activeCount);
                    }
                }
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(LogId.DAL025055);
            logActiveThreadGroupsInfo();
        }
    }

    /**
     * アクティブThreadGroupsの情報をデバッグログ出力する。
     * ThreadGroupsのdestroy前後で、destroy前なのかdestroy後なのかの情報をログ出力後に、
     * このメソッドが実行されることを想定している。
     * LOGGER.isDebugEnabled()によるログレベルの判定は、このメソッドの呼び出し元で行うことを想定している。
     */
    protected static void logActiveThreadGroupsInfo() {
        ThreadGroup currentThreadGroup = Thread.currentThread().getThreadGroup();
        int activeGroupCount = currentThreadGroup.activeGroupCount();
        ThreadGroup[] threadGroups = new ThreadGroup[activeGroupCount + 1];
        int enumerateNum = currentThreadGroup.enumerate(threadGroups);
        while (threadGroups.length <= enumerateNum) {
            activeGroupCount = currentThreadGroup.activeGroupCount();
            threadGroups = new ThreadGroup[activeGroupCount + 1];
            enumerateNum = currentThreadGroup.enumerate(threadGroups);
        }
        int actualThreadGroupCount = 0;
        for (ThreadGroup threadGroup : threadGroups) {
            if (threadGroup == null) {
                continue;
            }
            LOGGER.debug(LogId.DAL025056, actualThreadGroupCount, threadGroup.getName(), threadGroup.getParent().getName());
            actualThreadGroupCount++;
        }
        LOGGER.debug(LogId.DAL025057, actualThreadGroupCount);
    }
}
