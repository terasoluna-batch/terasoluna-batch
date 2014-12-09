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

package jp.terasoluna.fw.batch.executor;

import java.util.concurrent.ConcurrentHashMap;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.logger.TLogger;

import org.springframework.context.ApplicationContext;

public class ThreadGroupApplicationContextHolder {
    /** ロガー. */
    private static final TLogger LOGGER = TLogger
            .getLogger(ThreadGroupApplicationContextHolder.class);

    /** スレッドグループ毎にApplicationContextを保持する. */
    private static final ConcurrentHashMap<ThreadGroup, ApplicationContext> tga = new ConcurrentHashMap<ThreadGroup, ApplicationContext>();

    /**
     * コンストラクタ
     */
    protected ThreadGroupApplicationContextHolder() {
    }

    /**
     * ApplicationContextを取得する.<br>
     * <p>
     * カレントスレッドが所属するスレッドグループに割り当てられたApplicationContextを取得する。
     * </p>
     * @return ApplicationContextを返却する
     */
    public static ApplicationContext getCurrentThreadGroupApplicationContext() {
        return getThreadGroupApplicationContext(getThreadGroup());
    }

    /**
     * ApplicationContextを取得する.<br>
     * <p>
     * 引数で渡したスレッドグループに割り当てられたApplicationContextを取得する。
     * </p>
     * @param threadGroup ThreadGroup
     * @return ApplicationContextを返却する
     */
    public static ApplicationContext getThreadGroupApplicationContext(
            ThreadGroup threadGroup) {
        ApplicationContext applicationContext = null;

        if (threadGroup != null) {
            applicationContext = tga.get(threadGroup);

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(LogId.TAL025003, getThreadMessage());
            }

        }

        return applicationContext;
    }

    /**
     * ApplicationContextを設定する.<br>
     * <p>
     * ここで設定するApplicationContextはスレッドグループ毎に保持される。
     * </p>
     * @param applicationContext ApplicationContext
     */
    protected static void setApplicationContext(
            ApplicationContext applicationContext) {
        if (applicationContext == null) {

            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(LogId.WAL025004, getThreadMessage());
            }

            return;
        }

        ThreadGroup tg = getThreadGroup();
        if (tg != null) {
            tga.put(tg, applicationContext);

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(LogId.TAL025004, getThreadMessage());
            }

        }
    }

    /**
     * ApplicationContextを削除する.<br>
     */
    protected static void removeApplicationContext() {
        ThreadGroup tg = getThreadGroup();
        if (tg != null) {
            tga.remove(tg);

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(LogId.TAL025005, getThreadMessage());
            }

        }
    }

    /**
     * スレッドグループを取得する.
     * @return ThreadGroup
     */
    private static ThreadGroup getThreadGroup() {
        Thread ct = Thread.currentThread();
        if (ct != null && ct.getThreadGroup() != null) {
            return ct.getThreadGroup();
        }
        return null;
    }

    /**
     * スレッドグループとスレッド名を返すメソッド.<br>
     * デバッグ用メッセージを返す。
     * @return String
     */
    private static String getThreadMessage() {
        StringBuilder sb = new StringBuilder();
        Thread ct = Thread.currentThread();

        if (ct != null && getThreadGroup() != null) {
            sb.append(" tg:[");
            sb.append(getThreadGroup().getName());
            sb.append("]");
        }
        if (ct != null) {
            sb.append(" t:[");
            sb.append(ct.getName());
            sb.append("]");
        }

        return sb.toString();
    }
}
