/*
 * Copyright (c) 2007 NTT DATA Corporation
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

package jp.terasoluna.fw.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * プロパティファイルからプロパティを取得するユーティリティクラス。
 * <p>
 * デフォルトでは ApplicationResources ファイルを読み込むが、 ApplicationResources ファイルで以下のように指定することにより、 他のプロパティファイルを追加で読み込むこともできる。
 * </p>
 * <strong>ApplicationResources.propertiesの設定書式</strong><br>
 * <code><pre>
 *   add.property.file.1 = <i>&lt;追加プロパティファイル名1&gt;</i>
 *   add.property.file.2 = <i>&lt;追加プロパティファイル名2&gt;</i>
 *   ...
 * </pre></code>
 * <p>
 * また、プロパティファイルを個別に指定した以下の機能がある
 * <ol>
 * <li>部分キー検索による値取得</li>
 * <li>部分キー取得</li>
 * </ol>
 * 詳細は、 getPropertyNames() メソッド、 getPropertiesValues() メソッドを参照。
 * </p>
 */
public class PropertyUtil {

    /**
     * ログクラス。
     */
    private static Log log = LogFactory.getLog(PropertyUtil.class);

    /**
     * デフォルトプロパティファイル名。
     */
    public static final String DEFAULT_PROPERTY_FILE = "ApplicationResources.properties";

    /**
     * 追加プロパティファイル指定のプリフィックス。
     */
    private static final String ADD_PROPERTY_PREFIX = "add.property.file.";

    /**
     * プロパティファイルの拡張子。
     */
    private static final String PROPERTY_EXTENSION = ".properties";

    /**
     * プロパティのキーと値を保持するオブジェクト。
     */
    private static TreeMap<String, String> props = new TreeMap<String, String>();

    /**
     * 読み込んだプロパティファイル名リスト。
     */
    private static Set<String> files = new HashSet<String>();

    /**
     * クラスロード時にプロパティファイルを読み込み初期化する。
     */
    static {
        StringBuilder key = new StringBuilder();
        load(DEFAULT_PROPERTY_FILE);
        if (props != null) {
            for (int i = 1;; i++) {
                key.setLength(0);
                key.append(ADD_PROPERTY_PREFIX);
                key.append(i);
                String path = getProperty(key.toString());
                if (path == null) {
                    break;
                }
                addPropertyFile(path);
            }
        }
        overrideProperties();
    }

    /**
     * 指定されたプロパティファイルを読み込む。
     * <p>
     * 読み込まれたプロパティファイルは、 以前読み込んだ内容に追加される。
     * </p>
     * @param name プロパティファイル名
     */
    private static void load(String name) {
        StringBuilder key = new StringBuilder();
        Properties p = readPropertyFile(name);
        for (@SuppressWarnings("rawtypes") Map.Entry e : p.entrySet()) {
            // 読み込んだものをすべてpropsに追加する。
            props.put((String) e.getKey(), (String) e.getValue());
        }

        if (p != null) {
            for (int i = 1;; i++) {
                key.setLength(0);
                key.append(ADD_PROPERTY_PREFIX);
                key.append(i);
                String addfile = p.getProperty(key.toString());
                if (addfile == null) {
                    break;
                }
                String path = getPropertiesPath(name, addfile);
                addPropertyFile(path);
            }
        }
    }

    /**
     * 指定されたプロパティファイルを読み込む。
     * <p>
     * 以前読み込んだ内容に追加される。
     * </p>
     * @param name プロパティファイル名
     * @return プロパティリスト
     */
    private static Properties readPropertyFile(String name) {
        // カレントスレッドのコンテキストクラスローダを使用すると
        // WEB-INF/classesのプロパティファイルを読むことができない場合がある。
        // だがJNLPでリソースを取得するには、メインスレッドのコンテキスト
        // クラスローダを利用しなければならないため両方を併用する。
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(name);
        if (is == null) {
            is = PropertyUtil.class.getResourceAsStream("/" + name);
        }

        Properties p = new Properties();
        try {
            try {
                p.load(is);
                files.add(name);

            } catch (NullPointerException e) {
                System.err.println("!!! PANIC: Cannot load " + name + " !!!");
                System.err.println(ExceptionUtil.getStackTrace(e));
            } catch (IOException e) {
                System.err.println("!!! PANIC: Cannot load " + name + " !!!");
                System.err.println(ExceptionUtil.getStackTrace(e));
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return p;
    }

    /**
     * プロパティファイルから読み込まれた内容を、 コマンドラインの &quot;-D&quot; オプション等で指定された システムプロパティで上書きする。
     */
    private static void overrideProperties() {
        Enumeration<String> enumeration = Collections.enumeration(props
                .keySet());
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = System.getProperty(name);
            if (value != null) {
                props.put(name, value);
            }
        }
    }

    /**
     * 指定されたプロパティファイルを追加で読み込む。
     * <p>
     * 複数回呼び出しても1度しか読み込まれない。 プロパティファイル名の ".properties" は省略できる。
     * </p>
     * @param name プロパティファイル名
     */
    public static synchronized void addPropertyFile(String name) {
        if (!name.endsWith(PROPERTY_EXTENSION)) {
            StringBuilder nameBuf = new StringBuilder();
            nameBuf.append(name);
            nameBuf.append(PROPERTY_EXTENSION);
            name = nameBuf.toString();
        }
        if (!files.contains(name)) {
            load(name);
        }
    }

    /**
     * 指定されたキーのプロパティを取得する。
     * <p>
     * 参照値が &quot;@&quot; 付きの文字列である時、間接キーとみなし もう一度 &quot;@&quot; を外した文字列をキーとして検索する。 <code>key=@key</code>
     * という形で定義されている時、無限ループを回避するため、 <code>@key</code>を直接返却する。 先頭が &quot;@&quot; である文字列を値として設定する際には 先頭の &quot;@@&quot; を
     * &quot;@&quot; に変更しプロパティファイル に設定する事で、間接キー検索の機能を回避できる。
     * </p>
     * @param key プロパティのキー
     * @return 指定されたキーのプロパティの値
     */
    public static String getProperty(String key) {
        String result = props.get(key);

        // (キー)=@(キー)の時、無限ループ回避
        if (result != null && result.equals("@" + key)) {
            return result;
        }
        // @@の場合は間接キー検索を回避し、@と見なす。
        if (result != null && result.startsWith("@@")) {
            return result.substring(1);
        }
        if (result != null && result.startsWith("@")) {
            result = getProperty(result.substring(1));
        }

        return result;
    }

    /**
     * 指定されたキーのプロパティを取得する。
     * <p>
     * プロパティが見つからなかった場合には、指定されたデフォルトが返される。
     * </p>
     * @param key プロパティのキー
     * @param defaultValue プロパティのデフォルト値
     * @return 指定されたキーのプロパティの値
     */
    public static String getProperty(String key, String defaultValue) {
        String result = props.get(key);
        if (result == null) {
            return defaultValue;
        }
        return result;
    }

    /**
     * プロパティのすべてのキーのリストを取得する。
     * @return プロパティのすべてのキーのリスト
     */
    public static Enumeration<String> getPropertyNames() {
        return Collections.enumeration(props.keySet());
    }

    /**
     * 指定されたプリフィックスから始まるキーのリストを取得する。
     * @param keyPrefix キーのプリフィックス
     * @return 指定されたプリフィックスから始まるキーのリスト
     */
    public static Enumeration<String> getPropertyNames(String keyPrefix) {
        Map<String, String> map = props.tailMap(keyPrefix);
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            if (!name.startsWith(keyPrefix)) {
                return Collections.enumeration(props.subMap(keyPrefix, name)
                        .keySet());
            }
        }
        return Collections.enumeration(map.keySet());
    }

    /**
     * プロパティファイル名、部分キー文字列を指定することにより 値セットを取得する。
     * @param propertyName プロパティファイル名
     * @param keyPrefix 部分キー文字列
     * @return 値セット
     */
    public static Set<?> getPropertiesValues(String propertyName,
            String keyPrefix) {

        Properties localProps = loadProperties(propertyName);
        if (localProps == null) {
            return null;
        }

        Enumeration<String> keyEnum = getPropertyNames(localProps, keyPrefix);
        if (keyEnum == null) {
            return null;
        }

        return getPropertiesValues(localProps, keyEnum);
    }

    /**
     * プロパティを指定し、部分キープリフィックスに合致する キー一覧を取得する。
     * @param localProps プロパティ
     * @param keyPrefix 部分キープリフィックス
     * @return 部分キープリフィックスに合致するキー一覧
     */
    public static Enumeration<String> getPropertyNames(Properties localProps,
            String keyPrefix) {

        if (localProps == null || keyPrefix == null) {
            return null;
        }

        Collection<String> matchedNames = new ArrayList<String>();
        Enumeration<?> propNames = localProps.propertyNames();
        while (propNames.hasMoreElements()) {
            String name = (String) propNames.nextElement();
            if (name.startsWith(keyPrefix)) {
                matchedNames.add(name);
            }
        }
        return Collections.enumeration(matchedNames);
    }

    /**
     * キー一覧に対し、プロパティより取得した値を取得する。
     * @param localProps プロパティ
     * @param propertyNames キーの一覧
     * @return 値セット
     */
    public static Set<String> getPropertiesValues(Properties localProps,
            Enumeration<String> propertyNames) {

        if (localProps == null || propertyNames == null) {
            return null;
        }

        Set<String> retSet = new HashSet<String>();
        while (propertyNames.hasMoreElements()) {
            retSet.add(localProps.getProperty(propertyNames.nextElement()));
        }
        return retSet;
    }

    /**
     * 指定したプロパティファイル名で、プロパティオブジェクトを取得する。
     * @param propertyName プロパティファイル
     * @return プロパティオブジェクト
     */
    public static Properties loadProperties(String propertyName) {
        // propertyNameがnullまたは空文字の時、nullを返却する。
        if (propertyName == null || "".equals(propertyName)) {
            return null;
        }
        Properties retProps = new Properties();

        StringBuilder resourceName = new StringBuilder();
        resourceName.append(propertyName);
        if (!propertyName.endsWith(PROPERTY_EXTENSION)) {
            resourceName.append(PROPERTY_EXTENSION);
        }

        // カレントスレッドのコンテキストクラスローダを使用すると
        // WEB-INF/classesのプロパティファイルを読むことができない場合がある。
        // だがJNLPでリソースを取得するには、メインスレッドのコンテキスト
        // クラスローダを利用しなければならないため両方を併用する。
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(resourceName.toString());
        if (is == null) {
            is = PropertyUtil.class.getResourceAsStream("/" + propertyName
                    + PROPERTY_EXTENSION);
        }

        try {
            retProps.load(is);
        } catch (NullPointerException npe) {
            log.warn("*** Can not find property-file [" + propertyName
                    + ".properties] ***", npe);
            retProps = null;
        } catch (IOException ie) {
            log.error("", ie);
            retProps = null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ie) {
                log.error("", ie);
                retProps = null;
            }
        }
        return retProps;
    }

    /**
     * プロパティファイルの読み出しパスを取得する。 プロパティファイルを追加を行ったプロパティファイルが 存在するディレクトリをベースにして追加されたプロパティファイルを読む為、 プロパティファイルの読み出しディレクトリを取得する。
     * @param resource 追加指定を記述しているプロパティファイル
     * @param addFile 追加するプロパティファイル
     * @return 追加するプロパティファイルの読み出しパス
     */
    private static String getPropertiesPath(String resource, String addFile) {
        File file = new File(resource);
        String dir = file.getParent();
        if (dir != null) {
            StringBuilder dirBuf = new StringBuilder();
            dirBuf.setLength(0);
            dirBuf.append(dir);
            dirBuf.append(File.separator);
            dir = dirBuf.toString();
        } else {
            dir = "";
        }
        StringBuilder retBuf = new StringBuilder();
        retBuf.setLength(0);
        retBuf.append(dir);
        retBuf.append(addFile);
        return retBuf.toString();
    }
}
