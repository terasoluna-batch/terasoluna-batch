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
import java.util.Map;
import java.util.TreeMap;

import jp.terasoluna.fw.util.PropertyUtil;

/**
 * TERASOLUNAが提供する{@link PropertyUtil}の内部に保持されているプロパティを直接操作するためのユーティリティです。
 * 
 * <pre>
 * TerasolunaPropertyUtils.saveProperties(); // PropertyUtilの内部に保持されているプロパティを保存
 * TerasolunaPropertyUtils.addProperty(&quot;property.key&quot;, &quot;変更後の値&quot;);
 * xxx.execute(); // 試験実行 (property.key対するプロパティ値を使用する)
 * TerasolunaPropertyUtils.restoreProperties(); // PropertyUtilの内部に保持されていたプロパティを復元
 * </pre>
 * 
 */
public final class TerasolunaPropertyUtils {

    /**
     * プロパティマップの保存先マップ。
     */
    protected static final Map<String, String> savedMap = new TreeMap<String, String>();

    static {
        // ロードした時点でPropertyUtilsに保持されているものは保存先Mapに保存する
        savedMap.putAll(getPropertiesMap());
    }

    /**
     * {@link PropertyUtil}の内部に保持されているプロパティマップを返却します。
     * 
     * @return プロパティマップ
     */
    @SuppressWarnings("unchecked")
    public synchronized static Map<String, String> getPropertiesMap() {
        try {
            Field field = PropertyUtil.class.getDeclaredField("props");
            field.setAccessible(true);
            return (Map<String, String>) field.get(PropertyUtil.class); 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@link PropertyUtil}の内部に保持されているプロパティマップにプロパティを追加します。
     * 
     * @param key
     *            追加するプロパティのキー。
     * @param value
     *            追加するプロパティの値。
     */
    public synchronized static void addProperty(String key, String value) {
        if (!savedMap.containsKey(key) && getPropertiesMap().containsKey(key)) {
            // 未保存データの場合は保存する
            savedMap.put(key, getPropertiesMap().get(key));
        }
        getPropertiesMap().put(key, value);
    }

    /**
     * {@link PropertyUtil}の内部に保持されているプロパティマップにプロパティを追加します。
     * 
     * @param properties
     *            追加するプロパティのマップ。
     */
    public synchronized static void addProperties(Map<String, String> properties) {
        getPropertiesMap().putAll(properties);
    }

    /**
     * {@link PropertyUtil}の内部に保持されているプロパティマップからプロパティを削除します。
     * 
     * @param key
     *            削除するプロパティのキー。
     */
    public synchronized static void removeProperty(String key) {
        getPropertiesMap().remove(key);
    }

    /**
     * {@link PropertyUtil}の内部に保持されているプロパティマップを空にします。
     */
    public synchronized static void clearProperties() {
        getPropertiesMap().clear();
    }

    /**
     * {@link PropertyUtil}の内部に保持されているプロパティマップを保存します。
     */
    public synchronized static void saveProperties() {
        savedMap.clear();
        savedMap.putAll(getPropertiesMap());
    }

    /**
     * 保存されているプロパティマップ{@link PropertyUtil}の内部に保持されているプロパティマップに戻します。
     */
    public synchronized static void restoreProperties() {
        getPropertiesMap().clear();
        getPropertiesMap().putAll(savedMap);
    }
}