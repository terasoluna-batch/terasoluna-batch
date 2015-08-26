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
 *
 */

package jp.terasoluna.fw.batch.executor.controller;

import org.junit.After;
import org.junit.Test;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static uk.org.lidalia.slf4jtest.LoggingEvent.debug;

/**
 * SeparateGroupThreadFactoryTestのテストケース。
 *
 * @since 3.6
 */
public class SeparateGroupThreadFactoryTest {

    private static final TestLogger logger = TestLoggerFactory
            .getTestLogger(SeparateGroupThreadFactory.class);

    /**
     * ログキャプチャのバッファクリア.
     */
    @After
    public void tearDown() {
        logger.clear();
    }

    /**
     * getThreadGroupのテスト 【正常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・呼び出し順に付番されたスレッドグループが取得できること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetThreadGroup() throws Exception {
        SeparateGroupThreadFactory target = new SeparateGroupThreadFactory();

        // テスト実行
        ThreadGroup tg1 = target.getThreadGroup();
        ThreadGroup tg2 = target.getThreadGroup();
        ThreadGroup tg3 = target.getThreadGroup();

        assertEquals("AsyncBatchExecutorThreadGroup-1", tg1.getName());
        assertEquals("AsyncBatchExecutorThreadGroup-2", tg2.getName());
        assertEquals("AsyncBatchExecutorThreadGroup-3", tg3.getName());
    }

    /**
     * getThreadNamePrefixのテスト 【正常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・スレッド名プリフィックスが取得できること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetThreadNamePrefix() throws Exception {
        SeparateGroupThreadFactory target = new SeparateGroupThreadFactory();

        // テスト実行
        assertEquals("AsyncBatchExecutorThread-0-",
                target.getThreadNamePrefix());
    }

    /**
     * newThreadのテスト 【正常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code getThreadGroup()},{@code getThreadNamePrefix()}による
     * 　スレッドグループ名、スレッド名を利用した新規スレッドが生成されること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testNewThread() throws Exception {
        SeparateGroupThreadFactory target = new SeparateGroupThreadFactory();
        Runnable runnable1 = mock(Runnable.class);
        Runnable runnable2 = mock(Runnable.class);
        Runnable runnable3 = mock(Runnable.class);

        // テスト実行
        Thread th1 = target.newThread(runnable1);
        Thread th2 = target.newThread(runnable2);
        Thread th3 = target.newThread(runnable3);

        assertEquals("AsyncBatchExecutorThreadGroup-1",
                th1.getThreadGroup().getName());
        assertEquals("AsyncBatchExecutorThread-1-1", th1.getName());

        assertEquals("AsyncBatchExecutorThreadGroup-2",
                th2.getThreadGroup().getName());
        assertEquals("AsyncBatchExecutorThread-2-2", th2.getName());

        assertEquals("AsyncBatchExecutorThreadGroup-3",
                th3.getThreadGroup().getName());
        assertEquals("AsyncBatchExecutorThread-3-3", th3.getName());

        assertThat(logger.getLoggingEvents(), is(asList(
                debug("[DAL025027] tg:[AsyncBatchExecutorThreadGroup-1] t:[AsyncBatchExecutorThread-1-]"),
                debug("[DAL025027] tg:[AsyncBatchExecutorThreadGroup-2] t:[AsyncBatchExecutorThread-2-]"),
                debug("[DAL025027] tg:[AsyncBatchExecutorThreadGroup-3] t:[AsyncBatchExecutorThread-3-]"))));
    }
}
