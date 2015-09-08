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

import jp.terasoluna.fw.batch.exception.BatchException;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementParam;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementUpdateParam;
import jp.terasoluna.fw.batch.unit.utils.TerasolunaPropertyUtils;
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;

import static org.mockito.Mockito.*;

public class AbstractJobBatchExecutorTest extends DaoTestCase {

    /**
     * 利用するDAOクラス
     */
    private SystemDao systemDao = null;

    private PlatformTransactionManager transactionManager = null;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        deleteFromTable("job_control");

        update("INSERT INTO job_control (job_seq_id, job_app_cd, job_arg_nm1, job_arg_nm2, job_arg_nm3, job_arg_nm4, job_arg_nm5, job_arg_nm6, job_arg_nm7, job_arg_nm8, job_arg_nm9, job_arg_nm10, job_arg_nm11, job_arg_nm12, job_arg_nm13, job_arg_nm14, job_arg_nm15, job_arg_nm16, job_arg_nm17, job_arg_nm18, job_arg_nm19, job_arg_nm20, blogic_app_status, cur_app_status, add_date_time, upd_date_time) VALUES ('0000000001', 'B000001', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL)");
        update("INSERT INTO job_control (job_seq_id, job_app_cd, job_arg_nm1, job_arg_nm2, job_arg_nm3, job_arg_nm4, job_arg_nm5, job_arg_nm6, job_arg_nm7, job_arg_nm8, job_arg_nm9, job_arg_nm10, job_arg_nm11, job_arg_nm12, job_arg_nm13, job_arg_nm14, job_arg_nm15, job_arg_nm16, job_arg_nm17, job_arg_nm18, job_arg_nm19, job_arg_nm20, blogic_app_status, cur_app_status, add_date_time, upd_date_time) VALUES ('0000000002', 'B000001', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL)");
        update("INSERT INTO job_control (job_seq_id, job_app_cd, job_arg_nm1, job_arg_nm2, job_arg_nm3, job_arg_nm4, job_arg_nm5, job_arg_nm6, job_arg_nm7, job_arg_nm8, job_arg_nm9, job_arg_nm10, job_arg_nm11, job_arg_nm12, job_arg_nm13, job_arg_nm14, job_arg_nm15, job_arg_nm16, job_arg_nm17, job_arg_nm18, job_arg_nm19, job_arg_nm20, blogic_app_status, cur_app_status, add_date_time, upd_date_time) VALUES ('0000000003', 'B000001', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2', NULL, NULL)");
        update("INSERT INTO job_control (job_seq_id, job_app_cd, job_arg_nm1, job_arg_nm2, job_arg_nm3, job_arg_nm4, job_arg_nm5, job_arg_nm6, job_arg_nm7, job_arg_nm8, job_arg_nm9, job_arg_nm10, job_arg_nm11, job_arg_nm12, job_arg_nm13, job_arg_nm14, job_arg_nm15, job_arg_nm16, job_arg_nm17, job_arg_nm18, job_arg_nm19, job_arg_nm20, blogic_app_status, cur_app_status, add_date_time, upd_date_time) VALUES ('0000000004', 'B000001', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL)");
        update("INSERT INTO job_control (job_seq_id, job_app_cd, job_arg_nm1, job_arg_nm2, job_arg_nm3, job_arg_nm4, job_arg_nm5, job_arg_nm6, job_arg_nm7, job_arg_nm8, job_arg_nm9, job_arg_nm10, job_arg_nm11, job_arg_nm12, job_arg_nm13, job_arg_nm14, job_arg_nm15, job_arg_nm16, job_arg_nm17, job_arg_nm18, job_arg_nm19, job_arg_nm20, blogic_app_status, cur_app_status, add_date_time, upd_date_time) VALUES ('0000000005', 'B000001', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL)");

        systemDao = getBean("systemDao");
        transactionManager = getBean("adminTransactionManager");

        TerasolunaPropertyUtils.saveProperties();

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onTearDownAfterTransaction() throws Exception {

        TerasolunaPropertyUtils.restoreProperties();
        super.onTearDownAfterTransaction();
    }

    /**
     * ジョブステータス判定のテスト <br>
     * <br>
     * テスト概要： 起動時「0」で現在ジョブステータスが未実施「0」の場合、 ジョブステータス判定結果が実行中「1」を返却することを確認する。 <br>
     * <br>
     * 確認項目：実行中「1」が返却されることを確認する。<br>
     * <br>
     * @throws Exception
     */
    public void testJudgmentStatus01() throws Exception {

        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000001");
        param.setCurAppStatus("0");
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        String result = exe.judgmentStatus(param, "0000000001", "0", "0");

        assertEquals("1", result);

    }

    /**
     * ジョブステータス判定のテスト <br>
     * <br>
     * テスト概要： 起動時「0」で現在ジョブステータスが実行中「1」の場合、 ジョブステータス判定結果がnullを返却することを確認する。 <br>
     * <br>
     * 確認項目：nullが返却されることを確認する。<br>
     * <br>
     * @throws Exception
     */
    public void testJudgmentStatus02() throws Exception {

        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000001");
        param.setCurAppStatus("1");

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        String result = exe.judgmentStatus(param, "0000000001", "0", "0");

        assertNull(result);
    }

    /**
     * ジョブステータス判定のテスト <br>
     * <br>
     * テスト概要： 起動時「0」で現在ジョブステータスが処理済「2」の場合、 ジョブステータス判定結果がnullを返却することを確認する。 <br>
     * <br>
     * 確認項目：nullが返却されることを確認する。<br>
     * <br>
     * @throws Exception
     */
    public void testJudgmentStatus03() throws Exception {

        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000001");
        param.setCurAppStatus("2");

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        String result = exe.judgmentStatus(param, "0000000001", "0", "0");

        assertNull(result);
    }

    /**
     * ジョブステータス判定のテスト <br>
     * <br>
     * テスト概要： 正常終了時「1」で現在ジョブステータスが実行中「0」の場合、 ジョブステータス判定結果がnullを返却することを確認する。 <br>
     * <br>
     * 確認項目：nullが返却されることを確認する。<br>
     * <br>
     * @throws Exception
     */
    public void testJudgmentStatus04() throws Exception {

        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000001");
        param.setCurAppStatus("0");

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        String result = exe.judgmentStatus(param, "0000000001", "1", "0");

        assertNull(result);

    }

    /**
     * ジョブステータス判定のテスト <br>
     * <br>
     * テスト概要： 正常終了時「1」で現在ジョブステータスが実行中「1」の場合、 ジョブステータス判定結果が処理済「2」を返却することを確認する。 <br>
     * <br>
     * 確認項目：処理済「2」が返却されることを確認する。<br>
     * <br>
     * @throws Exception
     */
    public void testJudgmentStatus05() throws Exception {

        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000001");
        param.setCurAppStatus("1");

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        String result = exe.judgmentStatus(param, "0000000001", "1", "0");

        assertEquals("2", result);

    }

    /**
     * ジョブステータス判定のテスト <br>
     * <br>
     * テスト概要： 正常終了時「1」で現在ジョブステータスが処理済「2」の場合、 ジョブステータス判定結果がnullを返却することを確認する。 <br>
     * <br>
     * 確認項目：nullが返却されることを確認する。<br>
     * <br>
     * @throws Exception
     */
    public void testJudgmentStatus06() throws Exception {

        BatchJobData param = new BatchJobData();
        param.setJobAppCd("B000001");
        param.setCurAppStatus("2");

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        String result = exe.judgmentStatus(param, "0000000001", "1", "0");

        assertNull(result);

    }

    /**
     * ジョブステータス更新メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 起動時「0」でジョブ管理テーブルに登録されている ジョブシーケンスコード「0000000001」のジョブステータスが 未実施「0」の場合trueが返却されることを確認する。 <br>
     * <br>
     * 確認項目：<br>
     * trueが返却されること。<br>
     * DBのステータスが更新されていること。
     * @throws Exception
     */
    public void testUpdateBatchStatus01() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        boolean result = exe.updateBatchStatus("0000000001", "0", null,
                systemDao, transactionManager);

        BatchJobData row = queryForRowObject(
                "select * from job_control where job_seq_id = '0000000001'",
                BatchJobData.class);

        assertTrue(result);
        assertEquals("1", row.getCurAppStatus());
    }

    /**
     * ジョブステータス更新メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 起動時「0」でジョブ管理テーブルに登録されている ジョブシーケンスコード「0000000002」のジョブステータスが 実行中「1」の場合falseが返却されることを確認する。 <br>
     * <br>
     * 確認項目：<br>
     * falseが返却されること。<br>
     * DBのステータスが更新されていること。
     * @throws Exception
     */
    public void testUpdateBatchStatus02() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        boolean result = exe.updateBatchStatus("0000000002", "0", null,
                systemDao, transactionManager);

        BatchJobData row = queryForRowObject(
                "select * from job_control where job_seq_id = '0000000002'",
                BatchJobData.class);

        assertFalse(result);
        assertEquals("1", row.getCurAppStatus());
    }

    /**
     * ジョブステータス更新メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 起動時「0」でジョブ管理テーブルに登録されている ジョブシーケンスコード「0000000003」のジョブステータスが 処理済「2」の場合falseが返却されることを確認する。 <br>
     * <br>
     * 確認項目：<br>
     * falseが返却されること。<br>
     * DBのステータスが更新されていること。
     * @throws Exception
     */
    public void testUpdateBatchStatus03() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        boolean result = exe.updateBatchStatus("0000000003", "0", null,
                systemDao, transactionManager);

        BatchJobData row = queryForRowObject(
                "select * from job_control where job_seq_id = '0000000003'",
                BatchJobData.class);

        assertFalse(result);
        assertEquals("2", row.getCurAppStatus());
    }

    /**
     * ジョブステータス更新メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 正常終了時「1」でジョブ管理テーブルに登録されている ジョブシーケンスコード「0000000001」のジョブステータスが 未実施「0」の場合 falseが返却されることを確認する。 <br>
     * <br>
     * 確認項目：<br>
     * falseが返却されること。<br>
     * DBのステータスが更新されていること。
     * @throws Exception
     */
    public void testUpdateBatchStatus04() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        exe.updateBatchStatus("0000000001", "1", "0",
                systemDao, transactionManager);

        BatchJobData row = queryForRowObject(
                "select * from job_control where job_seq_id = '0000000001'",
                BatchJobData.class);

        assertFalse(false);
        assertEquals("0", row.getCurAppStatus());
    }

    /**
     * ジョブステータス更新メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 正常終了時「1」でジョブ管理テーブルに登録されている ジョブシーケンスコード「0000000002」のジョブステータスが 実行中「1」の場合 trueが返却されることを確認する。 <br>
     * <br>
     * 確認項目：<br>
     * trueが返却されること。<br>
     * DBのステータスが更新されていること。
     * @throws Exception
     */
    public void testUpdateBatchStatus05() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        boolean result = exe.updateBatchStatus("0000000002", "1", "0",
                systemDao, transactionManager);

        BatchJobData row = queryForRowObject(
                "select * from job_control where job_seq_id = '0000000002'",
                BatchJobData.class);

        assertTrue(result);
        assertEquals("2", row.getCurAppStatus());
    }

    /**
     * ジョブステータス更新メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 正常終了時「1」でジョブ管理テーブルに登録されている ジョブシーケンスコード「0000000003」のジョブステータスが 処理済「2」の場合 falseが返却されることを確認する。 <br>
     * <br>
     * 確認項目：<br>
     * falseが返却されること。<br>
     * DBのステータスが更新されていること。
     * @throws Exception
     */
    public void testUpdateBatchStatus06() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        boolean result = exe.updateBatchStatus("0000000003", "1", "0",
                systemDao, transactionManager);

        BatchJobData row = queryForRowObject(
                "select * from job_control where job_seq_id = '0000000003'",
                BatchJobData.class);

        assertFalse(result);
        assertEquals("2", row.getCurAppStatus());
    }

    /**
     * ジョブステータス更新メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * DBフェールオーバの発生を想定し、TransactionManager#getTransaction(tranDef)による トランザクション開始時にTransactionExceptionが発生した場合、呼出し元にスローされること。<br>
     * <br>
     * 確認項目：<br>
     * TransactionExceptionがスローされること。
     * @throws Exception
     */
    public void testUpdateBatchStatus07() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        PlatformTransactionManager transactionManager = new PlatformTransactionManagerStub01();
        try {
            exe.updateBatchStatus("0000000003", "1", "0", systemDao,
                    transactionManager);
            fail();
        } catch (TransactionException e) {
            assertEquals("トランザクション開始確認", e.getMessage());
        }
    }

    /**
     * ジョブステータス更新メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * DBフェールオーバの発生を想定し、JobUtil.updateJobStatus()による DB更新時にDataAccessExceptionが発生した場合、呼出し元にスローされること。<br>
     * <br>
     * 確認項目：<br>
     * DataAccessExceptionがスローされること。
     * @throws Exception
     */
    public void testUpdateBatchStatus08() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        SystemDao systemDao = mock(SystemDao.class);
        when(systemDao.updateJobTable(any(BatchJobManagementUpdateParam.class)))
                .thenThrow(new DataAccessException("DBステータス更新時例外確認用") {
                    private static final long serialVersionUID = 1L;
                });
        when(systemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(new BatchJobData() {
                    {
                        setJobSequenceId("0000000003");
                        setCurAppStatus("1");
                        setErrAppStatus("1");
                    }
                });
        try {
            exe.updateBatchStatus("0000000003", "1", "0", systemDao,
                    transactionManager);
            fail();
        } catch (DataAccessException e) {
            assertEquals("DBステータス更新時例外確認用", e.getMessage());
        }
    }

    /**
     * ジョブステータス更新メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * DBフェールオーバの発生を想定し、JobUtil.selectJob(jobSequenceId, true, systemDao)による DB参照時にDataAccessExceptionが発生した場合、呼出し元にスローされること。<br>
     * <br>
     * 確認項目：<br>
     * DataAccessExceptionがスローされること。
     * @throws Exception
     */
    public void testUpdateBatchStatus09() throws Exception {
        SystemDao systemDao = mock(SystemDao.class);
        when(systemDao.selectJob(any(BatchJobManagementParam.class))).thenThrow(
                new DataAccessException("DBステータス参照時例外確認用") {
                    private static final long serialVersionUID = 1L;
                });
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        try {
            exe.updateBatchStatus("0000000005", "1", "0", systemDao,
                    transactionManager);
            fail();
        } catch (DataAccessException e) {
            assertEquals("DBステータス参照時例外確認用", e.getMessage());
        }
    }

    /**
     * ジョブステータス更新メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * DBフェールオーバの発生を想定し、transactionManager.commit(tranStatus)による トランザクションコミット失敗時にTransactionExceptionが発生した場合、呼出し元にスローされること。<br>
     * <br>
     * 確認項目：<br>
     * TransactionExceptionがスローされること。
     * @throws Exception
     */
    public void testUpdateBatchStatus10() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        PlatformTransactionManager transactionManager = new PlatformTransactionManagerStub02();
        try {
            exe.updateBatchStatus("0000000005", "1", "0", systemDao,
                    transactionManager);
            fail();
        } catch (TransactionException e) {
            assertEquals("コミット確認用", e.getMessage());
        }
    }

    /**
     * ジョブステータス更新メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * DBフェールオーバ以外の例外発生を想定し、transactionManagerがnullの場合による トランザクション開始時にNullPointerExceptionが発生した場合、
     * BatchExceptionにラップされた状態で呼出し元にスローされること。<br>
     * <br>
     * 確認項目：<br>
     * BatchExceptionがスローされること。
     * @throws Exception
     */
    public void testUpdateBatchStatus11() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        try {
            exe.updateBatchStatus("0000000005", "1", "0", systemDao, null);
            fail();
        } catch (BatchException e) {
            assertTrue(e.getCause() instanceof NullPointerException);
        }
    }

    /**
     * ジョブステータス更新（ジョブ終了）メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 正常終了時「1」でジョブ管理テーブルに登録されている ジョブシーケンスコード「0000000001」のジョブステータスが 未実施「0」の場合 falseが返却されることを確認する。 <br>
     * <br>
     * 確認項目：<br>
     * falseが返却されること。<br>
     * DBのステータスが更新されていないこと。
     * @throws Exception
     */
    public void testEndBatchStatus01() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        BLogicResult blogicResult = new BLogicResult();
        boolean result = exe.endBatchStatus("0000000001", blogicResult,
                systemDao, transactionManager);

        BatchJobData row = queryForRowObject(
                "select * from job_control where job_seq_id = '0000000001'",
                BatchJobData.class);

        assertFalse(result);
        assertEquals("0", row.getCurAppStatus());

    }

    /**
     * ジョブステータス更新（ジョブ終了）メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 正常終了時「1」でジョブ管理テーブルに登録されている ジョブシーケンスコード「0000000002」のジョブステータスが 実行中「1」の場合 trueが返却されることを確認する。 <br>
     * <br>
     * 確認項目：<br>
     * trueが返却されること。<br>
     * DBのステータスが更新されていること。
     * @throws Exception
     */
    public void testEndBatchStatus02() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        BLogicResult blogicResult = new BLogicResult();
        boolean result = exe.endBatchStatus("0000000002", blogicResult,
                systemDao, transactionManager);

        BatchJobData row = queryForRowObject(
                "select * from job_control where job_seq_id = '0000000002'",
                BatchJobData.class);

        assertTrue(result);
        assertEquals("2", row.getCurAppStatus());

    }

    /**
     * ジョブステータス更新（ジョブ終了）メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 正常終了時「1」でジョブ管理テーブルに登録されている ジョブシーケンスコード「0000000003」のジョブステータスが 処理済「2」の場合 falseが返却されることを確認する。 <br>
     * <br>
     * 確認項目：<br>
     * falseが返却されること。<br>
     * DBのステータスが更新されていないこと。
     * @throws Exception
     */
    public void testEndBatchStatus03() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        BLogicResult blogicResult = new BLogicResult();
        boolean result = exe.endBatchStatus("0000000003", blogicResult,
                systemDao, transactionManager);

        BatchJobData row = queryForRowObject(
                "select * from job_control where job_seq_id = '0000000003'",
                BatchJobData.class);

        assertFalse(result);
        assertEquals("2", row.getCurAppStatus());

    }

    /**
     * ジョブステータス更新（ジョブ開始）メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 起動時「0」でジョブ管理テーブルに登録されている ジョブシーケンスコード「0000000001」のジョブステータスが 未実施「0」の場合 trueが返却されることを確認する。 <br>
     * <br>
     * 確認項目：<br>
     * trueが返却されること。<br>
     * DBのステータスが更新されていること。
     * @throws Exception
     */
    public void testStartBatchStatus01() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        boolean result = exe.startBatchStatus("0000000001", systemDao,
                transactionManager);

        BatchJobData row = queryForRowObject(
                "select * from job_control where job_seq_id = '0000000001'",
                BatchJobData.class);

        assertTrue(result);
        assertEquals("1", row.getCurAppStatus());

    }

    /**
     * ジョブステータス更新（ジョブ開始）メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 起動時「0」でジョブ管理テーブルに登録されている ジョブシーケンスコード「0000000002」のジョブステータスが 実行中「1」の場合falseが返却されることを確認する。 <br>
     * <br>
     * 確認項目：<br>
     * falseが返却されること。<br>
     * DBのステータスが更新されていないこと。
     * @throws Exception
     */
    public void testStartBatchStatus02() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        boolean result = exe.startBatchStatus("0000000002", systemDao,
                transactionManager);

        BatchJobData row = queryForRowObject(
                "select * from job_control where job_seq_id = '0000000002'",
                BatchJobData.class);

        assertFalse(result);
        assertEquals("1", row.getCurAppStatus());

    }

    /**
     * ジョブステータス更新（ジョブ開始）メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 起動時「0」でジョブ管理テーブルに登録されている ジョブシーケンスコード「0000000003」のジョブステータスが 処理済「2」の場合 falseが返却されることを確認する。 <br>
     * <br>
     * 確認項目：<br>
     * falseが返却されること。<br>
     * DBのステータスが更新されていないこと。
     * @throws Exception
     */
    public void testStartBatchStatus03() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        boolean result = exe.startBatchStatus("0000000003", systemDao,
                transactionManager);

        BatchJobData row = queryForRowObject(
                "select * from job_control where job_seq_id = '0000000003'",
                BatchJobData.class);

        assertFalse(result);
        assertEquals("2", row.getCurAppStatus());

    }

    /**
     * ジョブ実行メソッドのテスト<br>
     * <br>
     * テスト概要：<br>
     * 起動時「0」でジョブ管理テーブルに登録されている ジョブシーケンスコード「0000000001」のジョブが 正常に実行されることを確認する。 <br>
     * <br>
     * 確認項目：<br>
     * ジョブ終了コードに0が返却されること。<br>
     * DBのステータスが更新されていること。
     * @throws Exception
     */
    public void testExecuteBatch01() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        exe.systemDao = systemDao;
        exe.sysTransactionManager = transactionManager;
        BLogicResult result = exe.executeBatch("0000000001");

        assertNotNull(result);
        assertEquals(0, result.getBlogicStatus());
    }

    public void testExecuteBatch02() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        exe.systemDao = null;
        exe.sysTransactionManager = null;
        BLogicResult result = exe.executeBatch("0000000001");

        assertNotNull(result);
        assertEquals(-1, result.getBlogicStatus());
    }

    public void testExecuteBatch03() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        exe.systemDao = systemDao;
        exe.sysTransactionManager = transactionManager;
        BLogicResult result = exe.executeBatch("0000000000");

        assertNotNull(result);
        assertEquals(-1, result.getBlogicStatus());
    }

    public void testExecuteBatch04() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        exe.systemDao = systemDao;
        exe.sysTransactionManager = transactionManager;
        exe.changeStartStatus = true;
        BLogicResult result = exe.executeBatch("0000000001");

        assertNotNull(result);
        assertEquals(0, result.getBlogicStatus());
    }

    public void testExecuteBatch05() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        exe.systemDao = systemDao;
        exe.sysTransactionManager = transactionManager;
        exe.changeStartStatus = true;
        BLogicResult result = exe.executeBatch("0000000003");

        assertNotNull(result);
        // 更新ステータス不整合のため、ビジネスロジックは未実施のまま返却される。
        assertEquals(-1, result.getBlogicStatus());
    }

    /**
     * GetJobIntervalTimeメソッドのテスト<br>
     * <br>
     * テスト概要： getterメソッドのテストであるため、初期値が正確に返されることを確認する。
     * @throws Exception
     */
    public void testGetJobIntervalTime01() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        assertEquals(3000, exe.getJobIntervalTime());
    }

    /**
     * getExecutorEndMonitoringFileメソッドのテスト<br>
     * <br>
     * テスト概要：getterメソッドのテストであるため、初期値がnullであることを確認する。
     * @throws Exception
     */
    public void testGetExecutorEndMonitoringFile01() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        assertNotNull(exe.getExecutorEndMonitoringFile());
        assertEquals("/tmp/batch_terminate_file", exe
                .getExecutorEndMonitoringFile());
    }

    /**
     * getExecutorJobTerminateWaitIntervalTimeメソッドのテスト<br>
     * <br>
     * テスト概要：getterメソッドのテストであるため、初期値が5000であることを確認する。
     * @throws Exception
     */
    public void testGetExecutorJobTerminateWaitIntervalTime01() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        assertEquals(3000, exe.getExecutorJobTerminateWaitIntervalTime());
    }

    /**
     * setChangeStartStatusメソッドのテスト<br>
     * <br>
     * テスト概要：setterメソッドのテストであるため、初期値がfalseであることを確認する。
     * @throws Exception
     */
    public void testSetChangeStartStatus01() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        assertFalse(exe.changeStartStatus);

    }

    /**
     * setChangeStartStatusメソッドのテスト<br>
     * <br>
     * テスト概要：setterメソッドのテストであるため、引数にtrueを与え、trueが返却されることを確認する。
     * @throws Exception
     */
    public void testSetChangeStartStatus02() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        exe.setChangeStartStatus(true);

        assertTrue(exe.changeStartStatus);
    }

    public void testInitParameter01() {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty("polling.interval");
        exe.initParameter();

        assertEquals(1000, exe.jobIntervalTime);

    }

    public void testInitParameter02() {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty("polling.interval");
        TerasolunaPropertyUtils.addProperty("polling.interval", "5000");
        exe.initParameter();

        assertEquals(5000, exe.jobIntervalTime);

    }

    public void testInitParameter03() {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty("polling.interval");
        TerasolunaPropertyUtils.addProperty("polling.interval", "test");
        exe.initParameter();

        assertEquals(1000, exe.jobIntervalTime);

    }

    public void testInitParameter04() {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty("executor.endMonitoringFile");
        exe.initParameter();

        assertNull(exe.executorEndMonitoringFile);

    }

    public void testInitParameter05() {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty("executor.endMonitoringFile");
        TerasolunaPropertyUtils.addProperty("executor.endMonitoringFile",
                "/tmp/batch_terminate_file2");
        exe.initParameter();

        assertEquals("/tmp/batch_terminate_file2",
                exe.executorEndMonitoringFile);

    }

    public void testInitParameter06() {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty(
                "executor.jobTerminateWaitInterval");
        exe.initParameter();

        assertEquals(5000, exe.executorJobTerminateWaitIntervalTime);

    }

    public void testInitParameter07() {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty(
                "executor.jobTerminateWaitInterval");
        TerasolunaPropertyUtils.addProperty("executor.jobTerminateWaitInterval",
                "8000");
        exe.initParameter();

        assertEquals(8000, exe.executorJobTerminateWaitIntervalTime);

    }

    public void testInitParameter08() {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty(
                "executor.jobTerminateWaitInterval");
        TerasolunaPropertyUtils.addProperty("executor.jobTerminateWaitInterval",
                "test");
        exe.initParameter();

        assertEquals(5000, exe.executorJobTerminateWaitIntervalTime);

    }

    public void testInitSystemDatasourceDao01() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        TerasolunaPropertyUtils.removeProperty("systemDataSource.systemDao");
        exe.systemDao = null;
        exe.initSystemDatasourceDao();

        assertNull(exe.systemDao);
    }

    public void testInitSystemDatasourceDao02() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        TerasolunaPropertyUtils.removeProperty("systemDataSource.systemDao");
        TerasolunaPropertyUtils.addProperty("systemDataSource.systemDao",
                "test");
        exe.systemDao = null;
        exe.initSystemDatasourceDao();

        assertNull(exe.systemDao);
    }

    public void testInitSystemDatasourceDao03() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        TerasolunaPropertyUtils.removeProperty("systemDataSource.systemDao");
        TerasolunaPropertyUtils.addProperty("systemDataSource.systemDao",
                "adminTransactionManager");
        exe.systemDao = null;
        exe.initSystemDatasourceDao();

        assertNull(exe.systemDao);
    }

    public void testInitSystemDatasourceDao07() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        TerasolunaPropertyUtils.removeProperty(
                "systemDataSource.transactionManager");
        exe.sysTransactionManager = null;
        exe.initSystemDatasourceDao();

        assertNull(exe.sysTransactionManager);
    }

    public void testInitSystemDatasourceDao08() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        TerasolunaPropertyUtils.removeProperty(
                "systemDataSource.transactionManager");
        TerasolunaPropertyUtils.addProperty(
                "systemDataSource.transactionManager", "test");
        exe.sysTransactionManager = null;
        exe.initSystemDatasourceDao();

        assertNull(exe.sysTransactionManager);
    }

    public void testInitSystemDatasourceDao09() throws Exception {

        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();
        TerasolunaPropertyUtils.removeProperty(
                "systemDataSource.transactionManager");
        TerasolunaPropertyUtils.addProperty(
                "systemDataSource.transactionManager", "systemDao");
        exe.sysTransactionManager = null;
        exe.initSystemDatasourceDao();

        assertNull(exe.sysTransactionManager);
    }

    /**
     * testInitDefaultAppContext001
     * @throws Exception
     */
    public void testInitDefaultAppContext001() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        exe.initDefaultAppContext();
    }

    /**
     * testInitDefaultAppContext002
     * @throws Exception
     */
    public void testInitDefaultAppContext002() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty(
                "beanDefinition.admin.classpath");
        TerasolunaPropertyUtils.removeProperty("beanDefinition.admin.default");

        exe.initDefaultAppContext();
    }

    /**
     * testInitDefaultAppContext003
     * @throws Exception
     */
    public void testInitDefaultAppContext003() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty(
                "beanDefinition.admin.classpath");
        TerasolunaPropertyUtils.removeProperty(
                "beanDefinition.admin.dataSource");

        exe.initDefaultAppContext();
    }

    /**
     * testInitDefaultAppContext004
     * @throws Exception
     */
    public void testInitDefaultAppContext004() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty(
                "beanDefinition.admin.dataSource");
        TerasolunaPropertyUtils.removeProperty(
                "beanDefinition.admin.dataSource");

        exe.initDefaultAppContext();
    }

    /**
     * testInitSystemDatasourceDao001
     * @throws Exception
     */
    public void testInitSystemDatasourceDao001() throws Exception {
        AbstractJobBatchExecutor exe = new AsyncBatchExecutor();

        TerasolunaPropertyUtils.removeProperty(
                "beanDefinition.admin.dataSource");
        TerasolunaPropertyUtils.removeProperty(
                "beanDefinition.admin.dataSource");

        exe.initDefaultAppContext();
        exe.initSystemDatasourceDao();
    }
}
