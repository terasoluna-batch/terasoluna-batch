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

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import jp.terasoluna.fw.batch.constants.JobStatusConstants;
import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementParam;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementUpdateParam;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;
import static uk.org.lidalia.slf4jtest.LoggingEvent.debug;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;
import static uk.org.lidalia.slf4jtest.LoggingEvent.warn;
import static uk.org.lidalia.slf4jtest.LoggingEvent.info;

/**
 * JobStatusChangerImplのテストケースクラス
 */
public class JobStatusChangerImplTest {

    private SystemDao mockSystemDao = mock(SystemDao.class);

    private PlatformTransactionManager mockPlatformTransactionManager = mock(PlatformTransactionManager.class);

    private JobStatusChanger jobStatusChanger = new JobStatusChangerImpl(mockSystemDao, mockPlatformTransactionManager);

    private TestLogger logger = TestLoggerFactory
            .getTestLogger(JobStatusChangerImpl.class);

    @Before
    public void setUp() {
        // テスト入力データ設定
        Mockito.reset(mockSystemDao, mockPlatformTransactionManager);
    }

    /**
     * テスト後処理：ロガーのクリアを行う。
     */
    @After
    public void tearDown() {
        logger.clear();
    }

    /**
     * コンストラクタテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・とくになし
     * 確認項目
     * ・パラメータとフィールド変数が一致するかどうか
     * </pre>
     */
    @Test
    public void testJobStatusChangerImpl() {
        // 結果検証
        assertSame(mockSystemDao,
                ((JobStatusChangerImpl) jobStatusChanger).systemDao);
        assertSame(
                mockPlatformTransactionManager,
                ((JobStatusChangerImpl) jobStatusChanger).adminTransactionManager);
    }

    /**
     * changeToStartStatusテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・有効なジョブシーケンスIDが渡されること
     * ・ジョブのステータスがJOB_STATUS_UNEXECUTIONであること
     * 確認項目
     * ・trueが返却されること
     * [DAL025023]のログが出力されること
     * ・PlatformTransactionManager#commit()が呼び出されること
     * ・PlatformTransactionManager#rollback()が呼び出されないこと
     * </pre>
     */
    @Test
    public void testChangeToStartStatus01() {
        // テスト入力データ設定
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(new BatchJobData() {
                    {
                        setJobSequenceId("0000000001");
                        setCurAppStatus(JobStatusConstants.JOB_STATUS_UNEXECUTION);
                    }
                });
        when(
                mockSystemDao
                        .updateJobTable(any(BatchJobManagementUpdateParam.class)))
                .thenReturn(1);
        when(mockTransactionStatus.isCompleted()).thenReturn(true);

        // テスト実行
        // 結果検証
        assertTrue(jobStatusChanger.changeToStartStatus("00000001"));
        assertThat(
                logger.getLoggingEvents(),
                is(asList(debug("[DAL025023] update status jobSequenceId:00000001 changeStatus:1"))));
        verify(mockPlatformTransactionManager).commit(mockTransactionStatus);
        verify(mockPlatformTransactionManager, never()).rollback(
                mockTransactionStatus);
    }

    /**
     * changeToStartStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・無効(データなし)のジョブシーケンスIDが渡されること
     * 確認項目
     * ・falseが返却されること
     * ・[EAL025026]、[WAL025013]のログが出力されること
     * ・PlatformTransactionManager#rollback()が呼び出されること
     * </pre>
     */
    @Test
    public void testChangeToStartStatus02() {
        // テスト入力データ設定
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(null);

        // テスト実行
        // 結果検証
        assertFalse(jobStatusChanger.changeToStartStatus("00000001"));
        assertThat(
                logger.getLoggingEvents(),
                is(asList(
                        error("[EAL025026] Job record Not Found. jobSequenceId:00000001"),
                        warn("[WAL025013] An unexpected event has detected at the job status update processing. It will be attempt to roll-back. jobSequenceId:00000001"))));
        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * changeToStartStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・ジョブシーケンスIDとしてnullが渡されること
     * 確認項目
     * ・falseが返却されること
     * ・[EAL025026]、[WAL025013]のログが出力されること
     * ・PlatformTransactionManager#rollback()が呼び出されること
     * </pre>
     */
    @Test
    public void testChangeToStartStatus03() {
        // テスト入力データ設定
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(null);

        // テスト実行
        // 結果検証
        assertFalse(jobStatusChanger.changeToStartStatus(null));
        assertThat(
                logger.getLoggingEvents(),
                is(asList(
                        error("[EAL025026] Job record Not Found. jobSequenceId:null"),
                        warn("[WAL025013] An unexpected event has detected at the job status update processing. It will be attempt to roll-back. jobSequenceId:null"))));
        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * changeToStartStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・有効なジョブシーケンスIDが渡されること
     * ・ジョブのステータスがJOB_STATUS_UNEXECUTIONではないこと
     * 確認項目
     * ・falseが返却されること
     * ・[IAL025004]、[WAL025013]のログが出力されること
     * ・PlatformTransactionManager#rollback()が呼び出されること
     * </pre>
     */
    @Test
    public void testChangeToStartStatus04() {
        // テスト入力データ設定
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(new BatchJobData() {
                    {
                        setJobSequenceId("0000000001");
                        setCurAppStatus(JobStatusConstants.JOB_STATUS_FAILURE);
                    }
                });

        // テスト実行
        // 結果検証
        assertFalse(jobStatusChanger.changeToStartStatus("00000001"));
        assertThat(
                logger.getLoggingEvents(),
                is(asList(
                        info("[IAL025004] ステータスが判定基準外(ジョブシーケンスコード:00000001 blogicの戻り値:null イベント:0 ジョブレコードのステータス値:3 判定:false)"),
                        warn("[WAL025013] An unexpected event has detected at the job status update processing. It will be attempt to roll-back. jobSequenceId:00000001"))));
        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * changeToStartStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・有効なジョブシーケンスIDが渡されること
     * ・ジョブのステータスがJOB_STATUS_UNEXECUTIONであること
     * ・ジョブステータスの更新に失敗すること
     * 確認項目
     * ・falseが返却されること
     * ・[DAL025023]、[EAL025026]、[WAL025013]のログが出力されること
     * ・PlatformTransactionManager#rollback()が呼び出されること
     * </pre>
     */
    @Test
    public void testChangeToStartStatus05() {
        // テスト入力データ設定
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(new BatchJobData() {
                    {
                        setJobSequenceId("0000000001");
                        setCurAppStatus(JobStatusConstants.JOB_STATUS_UNEXECUTION);
                    }
                });
        when(
                mockSystemDao
                        .updateJobTable(any(BatchJobManagementUpdateParam.class)))
                .thenReturn(-1);

        // テスト実行
        // 結果検証
        assertFalse(jobStatusChanger.changeToStartStatus("00000001"));
        assertThat(
                logger.getLoggingEvents(),
                is(asList(
                        debug("[DAL025023] update status jobSequenceId:00000001 changeStatus:1"),
                        error("[EAL025025] Job status update error.(JOB_SEQ_ID:00000001) blogicStatus:[null])"),
                        warn("[WAL025013] An unexpected event has detected at the job status update processing. It will be attempt to roll-back. jobSequenceId:00000001"))));
        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * changeToStartStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・有効なジョブシーケンスIDが渡されること
     * ・ジョブのステータスがJOB_STATUS_UNEXECUTIONであること
     * ・TransactionStatusとしてnullが返却されること
     * 確認項目
     * ・falseが返却されること
     * ・PlatformTransactionManager#commit()が呼び出されること
     * ・PlatformTransactionManager#rollback()が呼び出されないこと
     * </pre>
     */
    @Test
    public void testChangeToStartStatus06() {
        // テスト入力データ設定
        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(null);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(new BatchJobData() {
                    {
                        setJobSequenceId("0000000001");
                        setCurAppStatus(JobStatusConstants.JOB_STATUS_UNEXECUTION);
                    }
                });
        when(
                mockSystemDao
                        .updateJobTable(any(BatchJobManagementUpdateParam.class)))
                .thenReturn(1);

        // テスト実行
        // 結果検証
        assertTrue(jobStatusChanger.changeToStartStatus("00000001"));
        assertThat(
                logger.getLoggingEvents(),
                is(asList(debug("[DAL025023] update status jobSequenceId:00000001 changeStatus:1"))));
        verify(mockPlatformTransactionManager).commit(null);
        verify(mockPlatformTransactionManager, never()).rollback(null);
    }

    /**
     * changeToStartStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・有効なジョブシーケンスIDが渡されること
     * ・ジョブのステータスがJOB_STATUS_UNEXECUTIONであること
     * 確認項目
     * ・systemDao#selectJob()で例外がスローされること
     * ・[WAL025013]のログが出力されること
     * ・PlatformTransactionManager#commit()が呼び出されないこと
     * ・PlatformTransactionManager#rollback()が呼び出されること
     * </pre>
     */
    @Test
    public void testChangeToStartStatus07() {
        // テスト入力データ設定
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);
        RuntimeException runtimeException = new RuntimeException("Test exception.");

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenThrow(runtimeException);

        // テスト実行
        // 結果検証
        try {
            assertFalse(jobStatusChanger.changeToStartStatus("00000001"));
            fail();
        } catch (Exception e) {
            assertSame(runtimeException, e);
        }

        assertThat(
                logger.getLoggingEvents(),
                is(asList(warn("[WAL025013] An unexpected event has detected at the job status update processing. It will be attempt to roll-back. jobSequenceId:00000001"))));
        verify(mockPlatformTransactionManager, never()).commit(
                mockTransactionStatus);
        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * changeToStartStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・有効なジョブシーケンスIDが渡されること
     * ・ジョブのステータスがJOB_STATUS_UNEXECUTIONであること
     * 確認項目
     * ・systemDao#updateJob()で例外がスローされること
     * ・[DAL025023]、[WAL025013]のログが出力されること
     * ・PlatformTransactionManager#commit()が呼び出されないこと
     * ・PlatformTransactionManager#rollback()が呼び出されないこと
     * </pre>
     */
    @Test
    public void testChangeToStartStatus08() {
        // テスト入力データ設定
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);
        RuntimeException runtimeException = new RuntimeException("Test exception.");

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(new BatchJobData() {
                    {
                        setJobSequenceId("0000000001");
                        setCurAppStatus(JobStatusConstants.JOB_STATUS_UNEXECUTION);
                    }
                });
        when(
                mockSystemDao
                        .updateJobTable(any(BatchJobManagementUpdateParam.class)))
                .thenThrow(runtimeException);

        // テスト実行
        // 結果検証
        try {
            assertFalse(jobStatusChanger.changeToStartStatus("00000001"));
            fail();
        } catch (Exception e) {
            assertSame(runtimeException, e);
        }
        assertThat(
                logger.getLoggingEvents(),
                is(asList(
                        debug("[DAL025023] update status jobSequenceId:00000001 changeStatus:1"),
                        warn("[WAL025013] An unexpected event has detected at the job status update processing. It will be attempt to roll-back. jobSequenceId:00000001"))));
        verify(mockPlatformTransactionManager, never()).commit(
                mockTransactionStatus);
        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * changeToEndStatusテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・有効なジョブシーケンスIDが渡されること
     * ・ジョブのステータスがJOB_STATUS_EXECUTINGであること
     * 確認項目
     * ・trueが返却されること
     * [DAL025023]のログが出力されること
     * ・PlatformTransactionManager#commit()が呼び出されること
     * </pre>
     */
    @Test
    public void testChangeToEndStatus01() {
        // テスト入力データ設定
        BLogicResult bLogicResult = new BLogicResult();
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(new BatchJobData() {
                    {
                        setJobSequenceId("0000000001");
                        setCurAppStatus(JobStatusConstants.JOB_STATUS_EXECUTING);
                    }
                });
        when(
                mockSystemDao
                        .updateJobTable(any(BatchJobManagementUpdateParam.class)))
                .thenReturn(1);
        when(mockTransactionStatus.isCompleted()).thenReturn(true);

        // テスト実行
        // 結果検証
        assertTrue(jobStatusChanger.changeToEndStatus("00000001", bLogicResult));
        assertThat(
                logger.getLoggingEvents(),
                is(asList(debug("[DAL025023] update status jobSequenceId:00000001 changeStatus:2"))));
        verify(mockPlatformTransactionManager).commit(mockTransactionStatus);
        verify(mockPlatformTransactionManager, never()).rollback(
                mockTransactionStatus);
    }

    /**
     * changeToEndStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・無効(データなし)のジョブシーケンスIDが渡されること
     * 確認項目
     * ・falseが返却されること
     * ・[EAL025026]、[WAL025013]のログが出力されること
     * ・PlatformTransactionManager#rollback()が呼び出されること
     * </pre>
     */
    @Test
    public void testChangeToEndStatus02() {
        // テスト入力データ設定
        BLogicResult bLogicResult = new BLogicResult();
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(null);

        // テスト実行
        // 結果検証
        assertFalse(jobStatusChanger
                .changeToEndStatus("00000001", bLogicResult));
        assertThat(
                logger.getLoggingEvents(),
                is(asList(
                        error("[EAL025026] Job record Not Found. jobSequenceId:00000001"),
                        warn("[WAL025013] An unexpected event has detected at the job status update processing. It will be attempt to roll-back. jobSequenceId:00000001"))));
        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * changeToEndStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・ジョブシーケンスIDとしてnullが渡されること
     * 確認項目
     * ・falseが返却されること
     * ・[EAL025026]、[WAL025013]のログが出力されること
     * ・PlatformTransactionManager#rollback()が呼び出されること
     * </pre>
     */
    @Test
    public void testChangeToEndStatus03() {
        // テスト入力データ設定
        BLogicResult bLogicResult = new BLogicResult();
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);

        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(null);

        // テスト実行
        // 結果検証
        assertFalse(jobStatusChanger.changeToEndStatus(null, bLogicResult));
        assertThat(
                logger.getLoggingEvents(),
                is(asList(
                        error("[EAL025026] Job record Not Found. jobSequenceId:null"),
                        warn("[WAL025013] An unexpected event has detected at the job status update processing. It will be attempt to roll-back. jobSequenceId:null"))));
        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * changeToEndStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・有効なジョブシーケンスIDが渡されること
     * ・bLogicResultとしてnullが渡されること
     * 確認項目
     * ・NullPointerExceptionがスローされること
     * ・[WAL025013]のログが出力されること
     * ・PlatformTransactionManager#rollback()が呼び出されること
     * </pre>
     */
    @Test
    public void testChangeToEndStatus04() {
        // テスト入力データ設定
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(new BatchJobData() {
                    {
                        setJobSequenceId("0000000001");
                        setCurAppStatus(JobStatusConstants.JOB_STATUS_EXECUTING);
                    }
                });

        try {
            // テスト実行
            jobStatusChanger.changeToEndStatus("00000001", null);
            fail();
        } catch (Exception e) {
            // 結果検証
            assertTrue(e instanceof NullPointerException);
        }
        assertThat(
                logger.getLoggingEvents(),
                is(asList(warn("[WAL025013] An unexpected event has detected at the job status update processing. It will be attempt to roll-back. jobSequenceId:00000001"))));
        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * changeToEndStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・有効なジョブシーケンスIDが渡されること
     * ・ジョブのステータスがJOB_STATUS_EXECUTINGではないこと
     * 確認項目
     * ・falseが返却されること
     * ・[IAL025004]、[WAL025013]のログが出力されること
     * ・PlatformTransactionManager#rollback()が呼び出されること
     * </pre>
     */
    @Test
    public void testChangeToEndStatus05() {
        // テスト入力データ設定
        BLogicResult bLogicResult = new BLogicResult();
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);

        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(new BatchJobData() {
                    {
                        setJobSequenceId("0000000001");
                        setCurAppStatus(JobStatusConstants.JOB_STATUS_PROCESSED);
                    }
                });

        // テスト実行
        // 結果検証
        assertFalse(jobStatusChanger
                .changeToEndStatus("00000001", bLogicResult));
        assertThat(
                logger.getLoggingEvents(),
                is(asList(
                        info("[IAL025004] ステータスが判定基準外(ジョブシーケンスコード:00000001 blogicの戻り値:null イベント:1 ジョブレコードのステータス値:2 判定:false)"),
                        warn("[WAL025013] An unexpected event has detected at the job status update processing. It will be attempt to roll-back. jobSequenceId:00000001"))));
        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * changeToEndStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・有効なジョブシーケンスIDが渡されること
     * ・ジョブステータスの更新に失敗すること
     * 確認項目
     * ・falseが返却されること
     * ・[DAL025023]、[EAL025025]、[WAL025013]のログが出力されること
     * ・PlatformTransactionManager#rollback()が呼び出されること
     * </pre>
     */
    @Test
    public void testChangeToEndStatus06() {
        // テスト入力データ設定
        BLogicResult bLogicResult = new BLogicResult();
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);

        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(new BatchJobData() {
                    {
                        setJobSequenceId("0000000001");
                        setCurAppStatus(JobStatusConstants.JOB_STATUS_EXECUTING);
                    }
                });

        // テスト実行
        // 結果検証
        assertFalse(jobStatusChanger
                .changeToEndStatus("00000001", bLogicResult));
        assertThat(
                logger.getLoggingEvents(),
                is(asList(
                        debug("[DAL025023] update status jobSequenceId:00000001 changeStatus:2"),
                        error("[EAL025025] Job status update error.(JOB_SEQ_ID:00000001) blogicStatus:[-1])"),
                        warn("[WAL025013] An unexpected event has detected at the job status update processing. It will be attempt to roll-back. jobSequenceId:00000001"))));
        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * changeToEndStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・有効なジョブシーケンスIDが渡されること
     * ・TransactionStatusとしてnullが返却されること
     * 確認項目
     * ・trueが返却されること
     * [DAL025023]のログが出力されること
     * ・PlatformTransactionManager#commit()が呼び出されること
     * ・PlatformTransactionManager#rollback()が呼び出されないこと
     * </pre>
     */
    @Test
    public void testChangeToEndStatus07() {
        // テスト入力データ設定
        BLogicResult bLogicResult = new BLogicResult();

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(null);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(new BatchJobData() {
                    {
                        setJobSequenceId("0000000001");
                        setCurAppStatus(JobStatusConstants.JOB_STATUS_EXECUTING);
                    }
                });
        when(
                mockSystemDao
                        .updateJobTable(any(BatchJobManagementUpdateParam.class)))
                .thenReturn(1);

        // テスト実行
        // 結果検証
        assertTrue(jobStatusChanger.changeToEndStatus("00000001", bLogicResult));
        assertThat(
                logger.getLoggingEvents(),
                is(asList(debug("[DAL025023] update status jobSequenceId:00000001 changeStatus:2"))));
        verify(mockPlatformTransactionManager).commit(null);
        verify(mockPlatformTransactionManager, never()).rollback(null);
    }

    /**
     * changeToEndStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・有効なジョブシーケンスIDが渡されること
     * ・ジョブのステータスがJOB_STATUS_EXECUTINGであること
     * 確認項目
     * ・systemDao#selectJob()で例外がスローされること
     * ・[WAL025013]のログが出力されること
     * ・PlatformTransactionManager#commit()が呼び出されないこと
     * ・PlatformTransactionManager#rollback()が呼び出されること
     * </pre>
     */
    @Test
    public void testChangeToEndStatus08() {
        // テスト入力データ設定
        BLogicResult bLogicResult = new BLogicResult();
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);
        RuntimeException runtimeException = new RuntimeException("Test exception.");

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenThrow(runtimeException);

        // テスト実行
        // 結果検証
        try {
            assertFalse(jobStatusChanger.changeToEndStatus("00000001",
                    bLogicResult));
            fail();
        } catch (Exception e) {
            assertSame(runtimeException, e);
        }
        assertThat(
                logger.getLoggingEvents(),
                is(asList(warn("[WAL025013] An unexpected event has detected at the job status update processing. It will be attempt to roll-back. jobSequenceId:00000001"))));
        verify(mockPlatformTransactionManager, never()).commit(
                mockTransactionStatus);
        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

    /**
     * changeToEndStatusテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * ・有効なジョブシーケンスIDが渡されること
     * ・ジョブのステータスがJOB_STATUS_EXECUTINGであること
     * 確認項目
     * ・systemDao#updateJob()で例外がスローされること
     * ・[DAL025023]、[WAL025013]のログが出力されること
     * ・PlatformTransactionManager#commit()が呼び出されないこと
     * ・PlatformTransactionManager#rollback()が呼び出されること
     * </pre>
     */
    @Test
    public void testChangeToEndStatus09() {
        // テスト入力データ設定
        BLogicResult bLogicResult = new BLogicResult();
        TransactionStatus mockTransactionStatus = mock(TransactionStatus.class);
        RuntimeException runtimeException = new RuntimeException("Test exception.");

        when(
                mockPlatformTransactionManager
                        .getTransaction(any(DefaultTransactionDefinition.class)))
                .thenReturn(mockTransactionStatus);
        when(mockSystemDao.selectJob(any(BatchJobManagementParam.class)))
                .thenReturn(new BatchJobData() {
                    {
                        setJobSequenceId("0000000001");
                        setCurAppStatus(JobStatusConstants.JOB_STATUS_EXECUTING);
                    }
                });
        when(
                mockSystemDao
                        .updateJobTable(any(BatchJobManagementUpdateParam.class)))
                .thenThrow(runtimeException);

        // テスト実行
        // 結果検証
        try {
            assertFalse(jobStatusChanger.changeToEndStatus("00000001",
                    bLogicResult));
            fail();
        } catch (Exception e) {
            assertSame(runtimeException, e);
        }
        assertThat(
                logger.getLoggingEvents(),
                is(asList(
                        debug("[DAL025023] update status jobSequenceId:00000001 changeStatus:2"),
                        warn("[WAL025013] An unexpected event has detected at the job status update processing. It will be attempt to roll-back. jobSequenceId:00000001"))));
        verify(mockPlatformTransactionManager, never()).commit(
                mockTransactionStatus);
        verify(mockPlatformTransactionManager).rollback(mockTransactionStatus);
    }

}
