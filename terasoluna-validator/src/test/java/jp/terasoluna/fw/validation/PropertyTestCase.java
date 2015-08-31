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

package jp.terasoluna.fw.validation;

import java.util.List;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.Before;

/**
 * プロパティの読み込みを行うテストケースに使用するクラス。 このクラスを使用する場合、最初にプロパティをキャッシュし、最後に 元に戻すので、PropertyUTUtilクラスを使用してプロパティを変更しても
 * 他のテストケースに影響を与えないでテストを行うことができる。
 */
public abstract class PropertyTestCase {

    /**
     * PropertyUtilのフルパス名。
     */
    private static final String PROPERTY_UTIL_NAME = "jp.terasoluna.fw.util.PropertyUtil";

    /**
     * PropertyUtil内のプロパティを保持するMapのフィールド名。
     */
    private static final String PROPERTY_FIELD = "props";

    /**
     * プロパティをキャッシュするために使用する。
     */
    protected Map<String, String> cashMap = null;

    /**
     * プロパティをキャッシュする。
     */
    @Before
    public void setUp() throws Exception {
        // プロパティをキャッシュする。
        saveProps();
        setUpData();
    }

    /**
     * プロパティをテスト実行前に戻してから終了する。
     */
    @After
    public void tearDown() throws Exception {
        cleanUpData();
        // プロパティをテスト開始前に戻す。
        loadProps();
    }

    /**
     * 初期化メソッド。不要であれば空実装すること。
     * @throws Exception 初期化時例外
     */
    protected abstract void setUpData() throws Exception;

    /**
     * 終了時メソッド。不要であれば空実装すること。
     * @throws Exception 終了時例外
     */
    protected abstract void cleanUpData() throws Exception;

    /**
     * 現在のプロパティをキャッシュする
     */
    private void saveProps() {
        this.cashMap = new HashMap<String, String>();
        Map<String, String> m = getProps();
        for (Map.Entry e : m.entrySet()) {
            // 読み込んだものをすべてpropsに追加する。
            this.cashMap.put((String) e.getKey(), (String) e.getValue());
        }
    }

    /**
     * キャッシュされているプロパティに戻す。
     */
    private void loadProps() {
        Map<String, String> map = getProps();
        map.clear();
        map.putAll(this.cashMap);
    }

    /**
     * 現在保持しているプロパティの値をMapで返す。
     * @return 現在保持しているプロパティ
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getProps() {
        Class cls = null;
        Field field = null;
        Map<String, String> map = null;
        try {
            // Classオブジェクトからメソッド名を指定して実行する。
            cls = Class.forName(PROPERTY_UTIL_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            field = cls.getDeclaredField(PROPERTY_FIELD);
        } catch (NoSuchFieldException e) {
            // フィールドが見つからない場合。
            e.printStackTrace();
        }
        // privateをアクセス可能にする。
        field.setAccessible(true);
        try {
            map = (Map<String, String>) field.get(cls);
        } catch (IllegalAccessException e) {
            // フィールドにアクセスできない場合。
            e.printStackTrace();
        }
        return map;
    }

    /**
     * プロパティを１つ追加する。
     * @param key 追加するキー。
     * @param value 追加する値。
     */
    public void addProperty(String key, String value) {
        Map<String, String> currentMap = getProps();
        currentMap.put(key, value);
    }

    /**
     * プロパティをMapごと追加する。
     * @param m 追加するプロパティを持ったMap
     */
    public void addPropertyAll(Map<String, String> m) {
        Map<String, String> currentMap = getProps();
        currentMap.putAll(m);
    }

    /**
     * プロパティから該当するキー (およびそれに対応する値) を削除する。
     * @param key 削除したいプロパティのキー、またはキーのプレフィックス
     */
    public void deleteProperty(String key) {
        Map<String, String> currentMap = getProps();
        Iterator<String> iter = currentMap.keySet().iterator();
        List<String> delList = new ArrayList<String>();
        while (iter.hasNext()) {
            String value = iter.next();
            if (value.startsWith(key)) {
                delList.add(value);
            }
        }
        for (int i = 0; i < delList.size(); i++) {
            String delKey = delList.get(i);
            currentMap.remove(delKey);
        }
    }

    /**
     * プロパティをすべて削除する。
     */
    public void clearProperty() {
        Map<String, String> currentMap = getProps();
        currentMap.clear();
    }

}
