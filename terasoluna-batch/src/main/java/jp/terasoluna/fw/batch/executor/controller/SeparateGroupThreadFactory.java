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

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.AsyncBatchExecutor;
import jp.terasoluna.fw.logger.TLogger;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@code ThreadPoolTaskExecutor}によるスレッド作成時にスレッドグループを個別に割り当てるスレッドファクトリ。
 *
 * 本クラスは{@code ThreadGroupApplicationContextHolder}を使用する際に
 * {@code ThreadPoolTaskExecutor}のスレッドファクトリとして必ず併用すること。
 * {@code ThreadGroupApplicationContextHolder}ではワーカスレッド上で参照される
 * 業務コンテキストの一意識別キーとしてスレッドグループ名を使用するため、
 * 本クラスを併用せずに{@code ThreadGroupApplicationContextHolder}を使用した場合、
 * 意図しない動作となる。
 * <p>
 * なお、スレッドグループとスレッド名のプリフィックスの内側に付与される
 * 番号は本クラスで管理するが、スレッド名の末尾に付与される番号は
 * {@code CustomizableThreadCreator}で管理される。
 * <p>
 * 加えて本クラスによるスレッドの新規生成処理は同期化・上限を考慮していないため、以下の制限を持つ。
 * <ol>
 * <li>異なるスレッドから本クラスによるスレッドの新規生成を行った場合、同名のスレッドが生成される恐れがある。</li>
 * <li>スレッドグループは新規スレッド生成毎に生成され、共有・破棄は行われないためメモリ上に蓄積する。</li>
 * </ol>
 * 以上より、本クラスのインジェクション対象である{@code ThreadPoolTaskExecutor}による
 * ワーカスレッド起動処理がメインスレッドのみで行われており、
 * {@code ThreadPoolTaskExecutor}のコアプールサイズと最大プールサイズが同数である
 * （ワーカスレッドが際限なく新規作成されない）動作環境である限りでは問題なく動作する。
 *
 * @see jp.terasoluna.fw.batch.executor.ThreadGroupApplicationContextHolder
 * @see org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
 * @see java.util.concurrent.ThreadPoolExecutor
 * @see org.springframework.util.CustomizableThreadCreator
 * @since 3.6
 */
public class SeparateGroupThreadFactory extends CustomizableThreadFactory {

    /**
     * ロガー.
     */
    private static final TLogger LOGGER = TLogger.getLogger(SeparateGroupThreadFactory.class);

    /**
     * スレッドグループプリフィックス.
     */
    public static final String THREAD_GROUP_PREFIX = AsyncBatchExecutor.class
            .getSimpleName()
            + "ThreadGroup";

    /**
     * スレッド名プリフィックス.
     */
    public static final String THREAD_NAME_PREFIX = AsyncBatchExecutor.class
            .getSimpleName()
            + "Thread";

    /**
     * スレッドグループセパレータ.
     */
    public static final String THREAD_GROUP_SEPARATOR = "-";

    /**
     * スレッド名セパレータ.
     */
    public static final String THREAD_NAME_SEPARATOR = "-";

    /**
     * スレッドグループ番号.
     */
    protected AtomicInteger threadGroupNo = new AtomicInteger(0);

    /**
     * スレッドプール上で新規作成される個々のスレッドに割り当てられる
     * スレッドグループを返却する。
     *
     * @return 新規スレッドに割り当てられるスレッドグループ
     */
    @Override
    public ThreadGroup getThreadGroup() {
        StringBuilder tgn = new StringBuilder();
        tgn.append(THREAD_GROUP_PREFIX);
        tgn.append(THREAD_GROUP_SEPARATOR);
        tgn.append(threadGroupNo.incrementAndGet());
        return new ThreadGroup(tgn.toString());
    }

    /**
     * スレッドプール上で新規作成されるワーカスレッドに付与されるスレッド名のプリフィックスを返却する。
     *
     * @return 新規ワーカスレッドに割り当てられるスレッド名のプリフィックス
     */
    @Override
    public String getThreadNamePrefix() {
        StringBuilder tn = new StringBuilder();
        tn.append(THREAD_NAME_PREFIX);
        tn.append(THREAD_NAME_SEPARATOR);
        tn.append(threadGroupNo.get());
        tn.append(THREAD_NAME_SEPARATOR);
        return tn.toString();
    }

    /**
     * 生成されたスレッドのスレッドグループ、およびスレッド名プリフィックスの
     * デバッグログを表示する。
     * 新規スレッドの生成は親クラスである{@code CustomizableThreadFactory}が行う。
     *
     * @param runnable 新規スレッド上で実行対象となる{@code Runnable}
     * @return スレッドプール上で実行される新規スレッド
     */
    @Override
    public Thread newThread(Runnable runnable) {
        Thread th = super.newThread(runnable);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(LogId.DAL025027, th.getThreadGroup().getName(),
                    getThreadNamePrefix());
        }
        return th;
    }
}
