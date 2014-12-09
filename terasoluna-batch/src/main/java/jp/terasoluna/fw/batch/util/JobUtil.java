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

package jp.terasoluna.fw.batch.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jp.terasoluna.fw.batch.constants.JobStatusConstants;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.exception.BatchException;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListParam;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementParam;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementUpdateParam;
import jp.terasoluna.fw.dao.QueryDAO;
import jp.terasoluna.fw.dao.UpdateDAO;
import jp.terasoluna.fw.logger.TLogger;

import org.springframework.dao.DataAccessException;

/**
 * ジョブ管理情報関連ユーティリティ。<br>
 * <br>
 * 主にフレームワークのAbstractJobBatchExecutorから利用されるユーティリティ。<br>
 * @see jp.terasoluna.fw.batch.executor.AbstractJobBatchExecutor
 */
public class JobUtil {

    /**
     * ロガー.
     */
    private static final TLogger LOGGER = TLogger.getLogger(JobUtil.class);

    /**
     * コンストラクタ
     */
    protected JobUtil() {
    }

    /**
     * <h6>ジョブリスト取得.</h6>
     * @param queryDAO QueryDAO
     * @return ジョブリスト
     */
    public static List<BatchJobListResult> selectJobList(QueryDAO queryDAO) {
        return selectJobList(null, queryDAO);
    }

    /**
     * <h6>ジョブリスト取得.</h6>
     * @param queryDAO QueryDAO
     * @param beginIndex 取得する開始インデックス
     * @param maxCount 取得する件数
     * @return ジョブリスト
     */
    public static List<BatchJobListResult> selectJobList(QueryDAO queryDAO,
            int beginIndex, int maxCount) {
        return selectJobList(null, queryDAO, beginIndex, maxCount);
    }

    /**
     * <h6>ジョブリスト取得.</h6>
     * @param jobAppCd ジョブ業務コード
     * @param queryDAO QueryDAO
     * @return ジョブリスト
     */
    public static List<BatchJobListResult> selectJobList(String jobAppCd,
            QueryDAO queryDAO) {
        return selectJobList(jobAppCd, queryDAO, -1, -1);
    }

    /**
     * <h6>ジョブリスト取得.</h6> ※未実施ステータスのジョブのみ取得
     * @param jobAppCd ジョブ業務コード
     * @param queryDAO QueryDAO
     * @param beginIndex 取得する開始インデックス
     * @param maxCount 取得する件数
     * @return ジョブリスト
     */
    public static List<BatchJobListResult> selectJobList(String jobAppCd,
            QueryDAO queryDAO, int beginIndex, int maxCount) {
        // ステータス
        List<String> curAppStatusList = new ArrayList<String>();

        // ステータス（未実施）
        curAppStatusList.add(JobStatusConstants.JOB_STATUS_UNEXECUTION);

        return selectJobList(jobAppCd, curAppStatusList, queryDAO, beginIndex,
                maxCount);
    }

    /**
     * <h6>ジョブリスト取得.</h6>
     * @param jobAppCd ジョブ業務コード
     * @param curAppStatusList 取得するステータスの一覧
     * @param queryDAO QueryDAO
     * @param beginIndex 取得する開始インデックス
     * @param maxCount 取得する件数
     * @return ジョブリスト
     */
    public static List<BatchJobListResult> selectJobList(String jobAppCd,
            List<String> curAppStatusList, QueryDAO queryDAO, int beginIndex,
            int maxCount) {

        BatchJobListParam param = new BatchJobListParam();

        // ジョブ業務コード
        param.setJobAppCd(jobAppCd);

        // 取得するステータス
        if (curAppStatusList != null) {
            param.setCurAppStatusList(curAppStatusList);
        }

        List<BatchJobListResult> result = null;
        try {
            if (beginIndex == -1 || maxCount == -1) {
                result = queryDAO.executeForObjectList(
                        "batchExecutor.selectJobList", param);
            } else {
                result = queryDAO.executeForObjectList(
                        "batchExecutor.selectJobList", param, beginIndex,
                        maxCount);
            }
        } catch (Exception e) {
            throw new BatchException(LOGGER.getLogMessage(LogId.EAL025039), e);
        }

        return result;
    }

    /**
     * <h6>ジョブ1件取得.</h6>
     * @param jobSequenceId
     * @param forUpdate
     * @param queryDAO
     * @return
     */
    public static BatchJobData selectJob(String jobSequenceId,
            boolean forUpdate, QueryDAO queryDAO) {
        BatchJobManagementParam param = new BatchJobManagementParam();

        // ジョブシーケンスコード
        param.setJobSequenceId(jobSequenceId);

        // FOR UPDATE付与
        param.setForUpdate(forUpdate);

        BatchJobData result = null;
        try {
            result = (BatchJobData) queryDAO.executeForObject(
                    "batchExecutor.selectJob", param, BatchJobData.class);
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(LogId.EAL025040, e);
            }
            if (e instanceof DataAccessException) {
                throw (DataAccessException) e;
            }
        }

        return result;
    }

    /**
     * <h6>ジョブレコード更新.</h6>
     * @param jobSequenceId
     * @param curAppStatus
     * @param jobRetCount
     * @param errAppStatus
     * @param updateDAO
     * @return
     */
    public static boolean updateJobStatus(String jobSequenceId,
            String curAppStatus, String jobRetCount, String blogicAppStatus,
            QueryDAO queryDAO, UpdateDAO updateDAO) {
        BatchJobManagementUpdateParam param = new BatchJobManagementUpdateParam();

        // ジョブシーケンスコード
        param.setJobSequenceId(jobSequenceId);

        // 業務ステータス
        param.setBLogicAppStatus(blogicAppStatus);

        // ステータス
        param.setCurAppStatus(curAppStatus);

        // 更新日時（ミリ秒）
        Timestamp updDateTime = getCurrentTime(queryDAO);
        param.setUpdDateTime(updDateTime);

        int count = -1;
        try {
            count = updateDAO.execute("batchExecutor.updateJobTable", param);
        } catch (Exception e) {
            LOGGER.error(LogId.EAL025041, e);
            if (e instanceof DataAccessException) {
               throw (DataAccessException) e;
            }
            return false;
        }

        if (count != 1) {
            LOGGER.error(LogId.EAL025042);

            return false;
        }

        return true;
    }

    /**
     * <h6>カレント時刻を取得する.</h6>
     * @param queryDAO
     * @return Timestamp カレント時刻
     */
    @Deprecated
    public static Timestamp getCurrentTime(QueryDAO queryDAO) {
        Timestamp result = null;
        try {
            result = (Timestamp) queryDAO.executeForObject(
                    "batchExecutor.currentTimeReader", null, Timestamp.class);
        } catch (Exception e) {
            LOGGER.error(LogId.EAL025043, e);
            if (e instanceof DataAccessException) {
                throw (DataAccessException) e;
            }
        }
        return result;
    }

    /**
     * <h6>カレント日付を取得する.</h6>
     * @param queryDAO
     * @return Date カレント日付
     */
    @Deprecated
    public static Date getCurrentDate(QueryDAO queryDAO) {
        Date result = null;
        try {
            result = (Date) queryDAO.executeForObject(
                    "batchExecutor.currentDateReader", null, Date.class);
        } catch (Exception e) {
            LOGGER.error(LogId.EAL025043, e);

        }
        return result;
    }

    /**
     * <h6>指定された環境変数の値を取得する.</h6>
     * <p>
     * システム環境で変数を定義しない場合は ""（空文字） を返す
     * </p>
     * @param name
     * @return
     */
    public static String getenv(String name) {
        String ret = System.getenv(name);
        if (ret == null) {
            return "";
        }
        return ret;
    }
}
