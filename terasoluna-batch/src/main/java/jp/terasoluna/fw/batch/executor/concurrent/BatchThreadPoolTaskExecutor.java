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

package jp.terasoluna.fw.batch.executor.concurrent;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * バッチジョブ実行用スレッドプールエグゼキュータ。<br>
 */
public class BatchThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    /**
     * コンストラクタ
     */
    public BatchThreadPoolTaskExecutor() {
        super();
        // キューサイズに0を設定し無効化する
        super.setQueueCapacity(0);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor#setQueueCapacity(int)
     */
    public void setQueueCapacity(int queueCapacity) {
        // キューサイズに0を設定し無効化する
        super.setQueueCapacity(0);
    }

}
