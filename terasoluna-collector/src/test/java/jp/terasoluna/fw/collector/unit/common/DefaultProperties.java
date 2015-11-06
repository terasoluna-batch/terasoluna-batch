package jp.terasoluna.fw.collector.unit.common;

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

import jp.terasoluna.fw.collector.unit.exception.UTRuntimeException;
import jp.terasoluna.fw.collector.unit.util.ClassLoaderUtils;

import org.springframework.util.Assert;

/**
 * UTライブラリで使用するデフォルトプロパティを管理するクラスです。
 * 
 * <pre>
 * プロパティはterasoluna-unit.propertiesに設定されています。
 * 環境にあわせて適宜変更してください。
 * </pre>
 */
public class DefaultProperties {
    /**
     * UTライブラリで使用するプロパティファイル
     */
    private static final String DEFAULT_FILE_PATH = "terasoluna-unit.properties";

    private static final ConcurrentMap<String, String> properties = new ConcurrentHashMap<String, String>();

    static {
        // プロパティファイルからキー・値を読み込みます。
        Properties defaults = new Properties();
        loadProperties(defaults, DEFAULT_FILE_PATH);
        for (Entry<?, ?> e : defaults.entrySet()) {
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
     * プロパティ値を返却します。
     * 
     * @param key プロパティのキー
     * @return プロパティの値
     */
    public static String getValue(String key) {
        return properties.get(key);
    }

    /**
     * プロパティ値を返却します。
     * 
     * @param key プロパティのキー({@link PropertyKeys}形式)
     * @return プロパティの値
     */
    public static String getValue(PropertyKeys key) {
        return getValue(key.getKey());
    }
}
