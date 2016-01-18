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

package jp.terasoluna.fw.collector;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CollectorThreadFactory.<br>
 * <p>
 * コレクタが生成するスレッド用のスレッドファクトリ。
 * </p>
 */
public class CollectorThreadFactory implements ThreadFactory {

    static final AtomicInteger poolNumber = new AtomicInteger(1);

    public static final String COLLECTOR_THREAD_NAME_PREFIX = "CollectorThreadFactory";

    public static final String COLLECTOR_THREAD_NAME_SEPARATOR = "-";

    public static final String COLLECTOR_THREAD_NAME_MIDDLE = "thread";

    final ThreadGroup group;

    final AtomicInteger threadNumber = new AtomicInteger(1);

    final String namePrefix;

    /**
     * CollectorThreadFactoryコンストラクタ.
     */
    public CollectorThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
                .getThreadGroup();

        StringBuilder prefixSb = new StringBuilder();
        prefixSb.append(COLLECTOR_THREAD_NAME_PREFIX);
        prefixSb.append(COLLECTOR_THREAD_NAME_SEPARATOR);
        prefixSb.append(poolNumber.getAndIncrement());
        prefixSb.append(COLLECTOR_THREAD_NAME_SEPARATOR);
        prefixSb.append(COLLECTOR_THREAD_NAME_MIDDLE);
        prefixSb.append(COLLECTOR_THREAD_NAME_SEPARATOR);
        namePrefix = prefixSb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Thread newThread(Runnable runnable) {
        StringBuilder nameSb = new StringBuilder();
        nameSb.append(namePrefix);
        nameSb.append(threadNumber.getAndIncrement());

        Thread t = new Thread(group, runnable, nameSb.toString(), 0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }

}
