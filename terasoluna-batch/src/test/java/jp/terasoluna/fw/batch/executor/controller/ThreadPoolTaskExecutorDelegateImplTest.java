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

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * ThreadPoolTaskExecutorDelegateImplのテストケースクラス
 */
public class ThreadPoolTaskExecutorDelegateImplTest {

    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    ThreadPoolTaskExecutorDelegate threadPoolTaskExecutorDelegate;

    private class AsyncTask implements Runnable {

        @Override
        public void run() {
            System.out.println("async task.");
        }

    }

    @Before
    public void setUp() {
        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.initialize();
        threadPoolTaskExecutorDelegate = new ThreadPoolTaskExecutorDelegateImpl(threadPoolTaskExecutor);
    }

    /**
     * ThreadPoolTaskExecutorDelegateImplコンストラクタのテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・ThreadPoolTaskExecutorDelegateのインスタンスが生成されていること
     * </pre>
     */
    @Test
    public void testThreadPoolTaskExecutorDelegateImpl() {

        // 結果検証
        assertTrue(threadPoolTaskExecutorDelegate instanceof ThreadPoolTaskExecutorDelegate);
    }

    /**
     * getThreadPoolTaskExecutorのテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・ThreadPoolTaskExecutorを引数としてThreadPoolTaskExecutorDelegateのインスタンスを生成しておく
     * 確認項目
     * ・ThreadPoolTaskExecutorが返却されること
     * </pre>
     */
    @Test
    public void testGetThreadPoolTaskExecutor() {
        // 結果検証
        assertTrue(threadPoolTaskExecutorDelegate.getThreadPoolTaskExecutor()
                .equals(threadPoolTaskExecutor));
    }

    /**
     * executeのテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・例外が発生することなくexecuteメソッドが実行できること
     * </pre>
     */
    @Test
    public void testExecute() {
        threadPoolTaskExecutorDelegate.execute(new AsyncTask());
    }

}
