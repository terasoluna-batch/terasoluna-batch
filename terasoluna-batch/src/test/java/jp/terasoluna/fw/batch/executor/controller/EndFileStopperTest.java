/*
 * Copyright (c) 2016 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.executor.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.Resource;

import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;
import static uk.org.lidalia.slf4jtest.LoggingEvent.*;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;

/**
 * EndFileStopperのテストケースクラス
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beansDef/AsyncBatchStopper.xml")
public class EndFileStopperTest {

    @Resource
    protected AsyncBatchStopper asyncBatchStopper;

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            EndFileStopper.class);

    /**
     * テスト後処理：ロガーのクリアを行う。
     */
    @After
    public void tearDown() {
        logger.clear();
    }

    /**
     * canStopテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・終了ファイルが存在する
     * 確認項目
     * ・trueが返却されること
     * ・INFOログが出力されること
     * </pre>
     * 
     * @throws IOException I/O 例外
     */
    @Test
    public void testCanStop001() throws IOException {
        // テストデータ準備 (batch.propertiesのexecutor.endMonitoringFileで指定しているファイル)
        Files.createFile(Paths.get("/tmp/batch_terminate_file"));

        // テスト実施
        // 結果検証
        try {
            // AfterPropertiesSetの出力ログは検証対象から除外するためにclearする。
            logger.clear();
            assertTrue(asyncBatchStopper.canStop());
        } finally {
            // テストデータ削除
            Files.deleteIfExists(Paths.get("/tmp/batch_terminate_file"));
        }
        assertThat(logger.getLoggingEvents(), is(asList(info(
                "[IAL025022] Detected the end file. This AsyncBatchExecutor processing will complete. path:/tmp/batch_terminate_file"))));
    }

    /**
     * canStopテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・終了ファイルが存在しない
     * 確認項目
     * ・falseが返却されること
     * ・INFOログが出力されること
     * </pre>
     */
    @Test
    public void testCanStop002() {
        // テスト実施
        // 結果検証
        assertFalse(asyncBatchStopper.canStop());
        assertThat(logger.getLoggingEvents(), IsNot.not(asList(info(
                "[IAL025022] Detected the end file. This AsyncBatchExecutor processing will complete. path:/tmp/batch_terminate_file"))));
    }

    /**
     * afterPropertiesSetテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・プロパティファイルでexecutor.endMonitoringFileに"/tmp/batch_terminate_file"が設定されている
     * 確認項目
     * ・"/tmp/batch_terminate_file"が返却されること
     * ・例外がスローされないこと
     * ・INFOログが出力されること
     * </pre>
     */
    @Test
    public void testAfterPropertiesSet01() {
        // テストデータ準備 (batch.propertiesのexecutor.endMonitoringFileで指定しているファイル)
        EndFileStopper endFileStopper = (EndFileStopper) asyncBatchStopper;

        // テスト実施
        // 結果検証
        endFileStopper.afterPropertiesSet();
        assertEquals(endFileStopper.endMonitoringFileName,
                "/tmp/batch_terminate_file");
        assertThat(logger.getLoggingEvents(), is(asList(info(
                "[IAL025025] The end file path:/tmp/batch_terminate_file exists:false."))));
    }

    /**
     * afterPropertiesSetテスト 【異常系】
     * 
     * <pre>
     * 事前条件
     * 確認項目
     * ・IllegalStateException例外がスローされること
     * ・ERRORログが出力されること
     * ・INFOログが出力されないこと
     * </pre>
     */
    @Test
    public void testAfterPropertiesSet02() {
        // テストデータ準備
        EndFileStopper endFileStopper = (EndFileStopper) asyncBatchStopper;
        String tempEndMonitoringFileName = endFileStopper.endMonitoringFileName;

        endFileStopper.endMonitoringFileName = "";
        try {
            // テスト実施
            endFileStopper.afterPropertiesSet();
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(),
                    "[EAL025056] [Assertion failed] - EndFileStopper requires to set executor.endMonitoringFile. please confirm the settings.");
            assertThat(logger.getLoggingEvents(), IsNot.not(asList(info(
                    "[IAL025025] The end file path:/tmp/batch_terminate_file exists:false."))));
        } finally {
            // テストデータ戻し
            endFileStopper.endMonitoringFileName = tempEndMonitoringFileName;
        }
    }
}
