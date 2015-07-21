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

package jp.terasoluna.fw.batch.executor.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * EndFileStopperのテストケースクラス
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beansDef/AsyncBatchStopper.xml")
public class EndFileStopperTest {

    @Resource
    protected AsyncBatchStopper asyncBatchStopper;

    /**
     * canStopテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・終了ファイルが存在する
     * 確認項目
     * ・trueが返却されること
     * </pre>
     * @throws IOException I/O 例外
     */
    @Test
    public void testCanStop001() throws IOException {
        // テストデータ準備 (batch.properties:executor.endMonitoringFileで指定しているファイル)
        Files.createFile(Paths.get("/tmp/batch_terminate_file"));

        // テスト実施
        // 結果検証
        try {
            assertTrue(asyncBatchStopper.canStop());
        } finally {
            // テストデータ削除
            Files.deleteIfExists(Paths.get("/tmp/batch_terminate_file"));
        }
    }

    /**
     * canStopテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・終了ファイルが存在しない
     * 確認項目
     * ・falseが返却されること
     * </pre>
     */
    @Test
    public void testCanStop002() {
        // テスト実施
        // 結果検証
        assertFalse(asyncBatchStopper.canStop());
    }

    /**
     * afterPropertiesSetテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * 確認項目
     * ・IllegalStateException例外がスローされること
     * </pre>
     */
    @Test
    public void testAfterPropertiesSet() {
        try {
            String tempEndMonitoringFileName = ((EndFileStopper) asyncBatchStopper).endMonitoringFileName;
            ((EndFileStopper) asyncBatchStopper).endMonitoringFileName = "";
            ((EndFileStopper) asyncBatchStopper).endMonitoringFileName = tempEndMonitoringFileName;
            ((EndFileStopper) asyncBatchStopper).afterPropertiesSet();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
        }
    }
}
