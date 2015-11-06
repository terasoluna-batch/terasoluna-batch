/*
 * Copyright (c) 2012 NTT DATA Corporation
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

package jp.terasoluna.fw.collector.unit.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import jp.terasoluna.fw.collector.unit.exception.UTRuntimeException;

/**
 * システム環境変数のテスト用ユーティリティ
 * 
 * <p>
 * システム環境変数(<code>System.getEnv("xxx")</code>で取得できる値)をプログラム上で変更・削除できます。 <br>
 * このユーティリティをJUnitで使用する場合、必ず後処理で<code>{@link SystemEnvUtils#restoreEnv()}</code> を実行して、変更前の状態に戻すこと。
 * </p>
 * @since 2.1.0
 */
public class SystemEnvUtils {
    /**
     * システム環境変数を保持するクラス名。
     */
    protected static final Class<?> processEnvironmentClass;

    /** システムプロパティのOS種別(Windows) */
    protected static final String OS_WIN = "WIN";

    /** システムプロパティのOS種別(Unix) */
    protected static final String OS_UNIX = "NIX";

    /** システムプロパティのOS種別(Linux) */
    protected static final String OS_LINUX = "NUX";

    /** 環境変数を保持するプロキシマップ */
    protected static final Map<String, String> proxy;

    /**
     * 保存したシステム環境変数マップ。
     */
    protected static final Map<String, String> savedMap = new TreeMap<String, String>();

    static {
        proxy = new HashMap<String, String>();
        try {
            processEnvironmentClass = Class
                    .forName("java.lang.ProcessEnvironment");
        } catch (ClassNotFoundException e) {
            throw new UTRuntimeException(e);
        }
        String osKind = System.getProperty("os.name").toUpperCase();
        String fieldName = null;
        if (osKind.indexOf(OS_WIN) >= 0) {
            fieldName = "theCaseInsensitiveEnvironment";
        } else if (osKind.indexOf(OS_UNIX) >= 0 || osKind.indexOf(OS_LINUX) >= 0) {
            fieldName = "theUnmodifiableEnvironment";
        } // Other OS...
        forceProxyMap(fieldName);
   }

    /**
     * フィールド名で示されたstatic final 指定のマップオブジェクトを
     * proxyマップに差し替える。
     * 
     * @param fieldName 差し替え対象のフィールド名
     */
    @SuppressWarnings("unchecked")
    protected static void forceProxyMap(String fieldName) {
        try {
            Field f = processEnvironmentClass.getDeclaredField(fieldName);
            f.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            Map<String, String> env = (Map<String, String>)f.get(null);
            proxy.putAll(env);
            savedMap.putAll(env);
            f.set(null, proxy);
        } catch (SecurityException e) {
            throw new UTRuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new UTRuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new UTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new UTRuntimeException(e);
        }
    }
    
    /**
     * システム環境変数を設定します。
     * 
     * @param key 環境変数名
     * @param value 環境変数値
     */
    public static synchronized void setEnv(String key, String value) {
        proxy.put(key, value);
    }

    /**
     * システム環境変数を削除します。
     * 
     * @param key 環境変数名
     */
    public static synchronized void removeEnv(String key) {
        proxy.remove(key);
    }

    /**
     * システム環境変数を変更前に戻します。
     */
    public static synchronized void restoreEnv() {
        proxy.clear();
        proxy.putAll(savedMap);
    }
    
}
