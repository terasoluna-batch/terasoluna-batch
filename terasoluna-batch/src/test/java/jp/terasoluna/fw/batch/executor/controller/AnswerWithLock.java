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

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * {@code JobExecutorTemplate#executeWorker()}実行時に呼び出されるアンサーオブジェクト。<br>
 *
 * @since 3.6
 */
public class AnswerWithLock implements Answer<Object> {

    private static final Logger logger = LoggerFactory
            .getLogger(AnswerWithLock.class);

    private Queue<String> queue;

    private CountDownLatch runningLatch;

    private CountDownLatch endLatch = new CountDownLatch(1);

    /**
     * コンストラクタ。<br>
     *
     * @param queue        実行メッセージの格納キュー
     * @param runningLatch 開始メッセージのインキューまで進行停止
     */
    public AnswerWithLock(Queue queue, CountDownLatch runningLatch) {
        this.queue = queue;
        this.runningLatch = runningLatch;
    }

    /**
     * {@code JobExecutorTemplate#executeWorker()}呼び出し時のアンサー。<br>
     *
     * @param invocation 実行対象
     * @return 常にnull
     * @throws Throwable 意図しない例外
     */
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        try {
            String jobSequenceId = invocation.getArgumentAt(0, String.class);

            queue.add("executing job." + jobSequenceId);
            logger.trace("executing job.{}", jobSequenceId);
            runningLatch.countDown();
            if ("0000000003".equals(jobSequenceId)) {
                TimeUnit.MILLISECONDS.sleep(2000L);
                logger.trace("# sleep end.{}", jobSequenceId);
            } else {
                endLatch.await();
                logger.trace("# job ending.{}", jobSequenceId);
            }
            queue.add("end job." + jobSequenceId);
            logger.trace("end job.{}", jobSequenceId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    /**
     * ジョブの終了指示。<br>
     * 本メソッドが実行されるまで待ち状態となる。
     */
    public void endJob() {
        this.endLatch.countDown();
    }
}
