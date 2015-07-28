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

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;

/**
 * ThreadPoolTaskExecutorDelegateImplのテストケースクラス
 */
public class ThreadPoolTaskExecutorDelegateImplTest {

    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    ThreadPoolTaskExecutorDelegate threadPoolTaskExecutorDelegate;

    /**
     * 初期化処理を行う。
     */
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
     * ・ThreadPoolTaskExecutorDelegateのインスタンス生成時に指定したThreadPoolTaskExecutorが設定されていること
     * 
     * </pre>
     */
    @Test
    public void testThreadPoolTaskExecutorDelegateImpl() {

        // 結果検証
        assertSame(
                threadPoolTaskExecutor,
                ((ThreadPoolTaskExecutorDelegateImpl) threadPoolTaskExecutorDelegate).threadPoolTaskExecutor);
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
        assertSame(threadPoolTaskExecutor, threadPoolTaskExecutorDelegate
                .getThreadPoolTaskExecutor());
    }

    /**
     * executeのテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・ThreadPoolTaskExecutor#execute()がデリゲータの引数と同じものを使用して呼び出されていること
     * </pre>
     */
    @Test
    public void testExecute() {
        // テスト準備
        ThreadPoolTaskExecutor mockExecutor = spy(threadPoolTaskExecutor);
        ((ThreadPoolTaskExecutorDelegateImpl) threadPoolTaskExecutorDelegate).threadPoolTaskExecutor = mockExecutor;
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("async task.");
            }
        };
        doCallRealMethod().when(mockExecutor).execute(runnable);

        // テスト実行
        threadPoolTaskExecutorDelegate.execute(runnable);

        // 結果検証
        verify(mockExecutor).execute(runnable);

    }

}
