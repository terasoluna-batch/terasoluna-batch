/*
 * Copyright (c) 2014 NTT DATA Corporation
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

package jp.terasoluna.fw.collector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * CollectorThreadFactoryのテストケース。
 */
public class CollectorThreadFactoryTest {

    /**
     * 引数スレッドがデーモンスレッドの場合、非デーモンスレッドが取得できること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testNewThread01() throws Exception {
        Thread ownThread = new Thread();
        ownThread.setDaemon(true);
        CollectorThreadFactory factory = new CollectorThreadFactory();
        Thread target = factory.newThread(ownThread);
        assertFalse(target.isDaemon());
    }

    /**
     * 引数スレッドが高優先度の場合、ノーマルの優先度スレッドが取得できること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testNewThread02() throws Exception {
        Thread ownThread = new Thread();
        ownThread.setPriority(Thread.MAX_PRIORITY);
        CollectorThreadFactory factory = new CollectorThreadFactory();
        Thread target = factory.newThread(ownThread);
        assertEquals(Thread.NORM_PRIORITY, target.getPriority());
    }
}
