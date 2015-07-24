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

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * <code>ThreadPoolTaskExecutor#execute()</code>のメソッド呼び出しを委譲するデリゲータ。<br>
 * @see ThreadPoolTaskExecutor
 * @since 3.6
 */
public interface ThreadPoolTaskExecutorDelegate {

    /**
     * <code>ThreadPoolTaskExecutor</code>を取得する。<br>
     * @return threadPooltaskExecutorインスタンス
     */
    ThreadPoolTaskExecutor getThreadPoolTaskExecutor();

    /**
     * ジョインポイントとして<code>ThreadPooltaskExecutor#execute()</code>を委譲する。<br>
     * @param task 非同期実行タスク
     */
    void execute(Runnable task);
}
