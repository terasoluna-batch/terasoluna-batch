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
 * スレッド単位で、{@code ApplicationContext}を管理するホルダークラス
 * 
 * @deprecated バージョン3.6.0より、本クラスは非推奨である。<br>
 * 以前のバージョンのように、複数スレッド間で{@code ApplicationContext}を共有する場合、手動で行うこと。<br>
 * バージョン3.6.0よりスレッドグループを考慮せず、スレッド単位で管理している。
 * 互換性のためシグネチャは変更していない点に注意すること。
 */
@Deprecated
public class ThreadGroupApplicationContextHolder {

    /**
     * スレッド毎にApplicationContextを保持する.
     * 
     * @since 3.6 以前のバージョンでは{@code　ThreadGroup}をkeyに管理していたが、{@code Thread}をkeyに管理するように変更となった。
     */
    private static final ConcurrentHashMap<Thread, ApplicationContext> tga = new ConcurrentHashMap<Thread, ApplicationContext>();

    /**
     * コンストラクタ
     */
    protected ThreadGroupApplicationContextHolder() {
    }

    /**
     * ApplicationContextを取得する.<br>
     * <p>
     * カレントスレッドに割り当てられたApplicationContextを取得する。
     * </p>
     * @return ApplicationContextを返却する
     * @since 3.6 以前のバージョンでは{@code　ThreadGroup}に対応する{@code ApplicationContext}を返却していたが、
     * {@code Thread}に対応する{@code ApplicationContext}を返却するように変更となった。
     */
    public static ApplicationContext getCurrentThreadGroupApplicationContext() {
        // シグネチャに合わせるために、ThreadGroupを渡している。
        return getThreadGroupApplicationContext(Thread.currentThread().getThreadGroup());
    }

    /**
     * ApplicationContextを取得する.<br>
     * <p>
     * 引数で渡したスレッドグループを無視し、カレントスレッドに割り当てられたApplicationContextを取得する。
     * </p>
     * @param threadGroup ThreadGroup
     * @return ApplicationContextを返却する
     * @since 3.6 以前のバージョンでは{@code　ThreadGroup}に対応する{@code ApplicationContext}を返却していたが、
     * {@code Thread}に対応する{@code ApplicationContext}を返却するように変更となった。
     */
    public static ApplicationContext getThreadGroupApplicationContext(
            ThreadGroup threadGroup) {
        ApplicationContext applicationContext = null;

        if (threadGroup != null) {
            applicationContext = tga.get(Thread.currentThread());
        }

        return applicationContext;
    }

    /**
     * ApplicationContextを設定する.<br>
     * <p>
     * ここで設定するApplicationContextはスレッド毎に保持される。
     * </p>
     * @param applicationContext ApplicationContext
     * @since 3.6 以前のバージョンでは{@code　ThreadGroup}に対応する{@code ApplicationContext}を返却していたが、
     * {@code Thread}に対応する{@code ApplicationContext}を返却するように変更となった。
     */
    public static void setApplicationContext(
            ApplicationContext applicationContext) {
        if (applicationContext == null) {
            return;
        }
        tga.put(Thread.currentThread(), applicationContext);
    }

    /**
     * ApplicationContextを削除する.<br>
     * 
     * @since 3.6 以前のバージョンでは{@code　ThreadGroup}に対応する{@code ApplicationContext}を返却していたが、
     * {@code Thread}に対応する{@code ApplicationContext}を返却するように変更となった。
     */
    public static void removeApplicationContext() {
        if (tga.containsKey(Thread.currentThread())) {
            tga.remove(Thread.currentThread());
        }
    }
}
