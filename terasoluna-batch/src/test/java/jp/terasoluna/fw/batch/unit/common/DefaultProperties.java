package jp.terasoluna.fw.batch.unit.common;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.terasoluna.fw.batch.unit.exception.UTRuntimeException;
import jp.terasoluna.fw.batch.unit.util.ClassLoaderUtils;

import org.springframework.util.Assert;

/**
 * UTライブラリで使用するデフォルトプロパティを管理するクラスです。
 * 
 * <pre>
 * デフォルトプロパティはterasoluna-unit.propertiesに設定されています。
 * terasoluna-unit-override.propertiesに記述することで、
 * ユーザー側でデフォルトプロパティを上書きできます。
 * </pre>
 */
public class DefaultProperties {
    /**
     * UTライブラリで使用するデフォルトプロパティファイル(システム固有値)
     */
    private static final String DEFAULT_FILE_PATH = "terasoluna-unit.properties";
    /**
     * UTライブラリで使用するデフォルトプロパティファイル(上書き用)
     */
    private static final String OVERRIDE_FILE_PATH = "terasoluna-unit-override.properties";

    private static final ConcurrentMap<String, String> properties = new ConcurrentHashMap<String, String>();

    static {
        // プロパティファイルからキー・値を読み込みます。
        Properties defaults = new Properties();
        Properties override = new Properties();
        loadProperties(defaults, DEFAULT_FILE_PATH);
        loadProperties(override, OVERRIDE_FILE_PATH);
        for (Entry<?, ?> e : defaults.entrySet()) {
            properties.put((String) e.getKey(), (String) e.getValue());
        }
        // 上書き
        for (Entry<?, ?> e : override.entrySet()) {
            properties.put((String) e.getKey(), (String) e.getValue());
        }
    }

    /**
     * プロパティファイルを読み込みます。
     * 
     * <pre>
     * プロパティファイルが存在しない場合は読み込みません。
     * 読み込み中にIO例外が発生した場合は{@link UTRuntimeException}をスローします。
     * </pre>
     * 
     * @param props プロパティ
     * @param filePath 読み込むプロパティファイル
     */
    private static void loadProperties(Properties props, String filePath)
                                                                         throws UTRuntimeException {
        Assert.notNull(props);
        Assert.notNull(filePath);
        ClassLoader cl = ClassLoaderUtils.getClassLoader();
        if (cl != null) {
            InputStream strm = cl.getResourceAsStream(filePath);
            try {
                if (strm != null) {
                    try {
                        props.load(strm);
                    } catch (IOException e) {
                        throw new UTRuntimeException(e);
                    }
                }
            } finally {
                if (strm != null) {
                    try {
                        strm.close();
                    } catch (IOException e) {
                        throw new UTRuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * デフォルト値を返却します。
     * 
     * @param key デフォルトプロパティのキー
     * @return デフォルトパティの値
     */
    public static String getValue(String key) {
        return properties.get(key);
    }

    /**
     * デフォルト値を返却します。
     * 
     * @param key デフォルトプロパティのキー({@link PropertyKeys}形式)
     * @return デフォルトパティの値
     */
    public static String getValue(PropertyKeys key) {
        return getValue(key.getKey());
    }
}
