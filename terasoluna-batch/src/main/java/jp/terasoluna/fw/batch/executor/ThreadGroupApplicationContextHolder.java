/*
 * Copyright (c) 2011-2016 NTT DATA Corporation
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

import org.springframework.context.ApplicationContext;

/**
 * スレッドグループ単位で、{@code ApplicationContext}を管理するホルダークラス
 * 
 * @deprecated バージョン3.6.0より、本クラスは非推奨である。<br>
 * スレッドグループ単位で{@code ApplicationContext}を共有する場合、手動で行うか、スレッドグループ全体の挙動に注意して使用すること。<br>
 * スレッドグループ内の各スレッドから単一の{@code ApplicationContext}を取得することは可能だが、
 * バージョン3.6.0よりスレッドグループのライフサイクルを考慮しないよう単純化し、単純なホルダーとしての機能しか持たないようになっている。
 */
@Deprecated
public class ThreadGroupApplicationContextHolder {

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
    public static void setApplicationContext(
            ApplicationContext applicationContext) {
        if (applicationContext == null) {
            return;
        }

        ThreadGroup tg = getThreadGroup();
        if (tg != null) {
            tga.put(tg, applicationContext);
        }
    }

    /**
     * ApplicationContextを削除する.<br>
     */
    public static void removeApplicationContext() {
        ThreadGroup tg = getThreadGroup();
        if (tg != null) {
            tga.remove(tg);
        }
    }

    /**
     * スレッドグループを取得する.
     * @return ThreadGroup
     */
    private static ThreadGroup getThreadGroup() {
        return Thread.currentThread().getThreadGroup();
    }
}
