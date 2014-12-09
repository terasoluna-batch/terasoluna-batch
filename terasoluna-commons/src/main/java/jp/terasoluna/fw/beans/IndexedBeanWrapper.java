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

package jp.terasoluna.fw.beans;

import java.util.Map;

/**
 * JavaBeanの配列・コレクション型属性へのアクセサを持つインタフェース。
 * 
 * <p>{@link #getIndexedPropertyValues(String)}メソッドで、
 * 配列型属性にアクセスする。
 * String型の引数にアクセスするプロパティ名を指定すると、
 * プロパティ名に一致する属性を全て取得する。
 * 戻り値はMap（キーがプロパティ名、値が属性値）が返される。
 * {@link #getIndexedPropertyValues(String)}メソッドは、配列型以外でも
 * 使用が可能である。</p>
 * 
 * <h5>配列型属性にアクセスする例</h5>
 * <p>
 * <pre>
 * public class TestBean {
 *     private String[] stringArray;
 *     
 *     ･･･（以下、getter/setterは略）
 * </pre>
 * <pre>
 * IndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);
 * Map map = bw.getIndexedPropertyValues("stringArray");
 * </pre>
 * </p>
 * 
 * <p>{@link #getIndexedPropertyValues(String)}メソッドでstringArray属性に
 * アクセスすると、stringArray[0]、stringArray[1]･･･stringArray[n]までの
 * プロパティ名と属性値をMap型にして返す。
 * 引数には”stringArray[0]”のように、直接要素を指定する必要はない。</p>
 * 
 *
 */
public interface IndexedBeanWrapper {
    /**
     * 指定したプロパティ名に一致する属性値を返す。
     * 取得したプロパティ名はインデックスをキーに昇順にソートされている。
     * @param propertyName プロパティ名
     * @return プロパティ名に一致する属性値を格納するMap
     * （プロパティ名、属性値）
     */
    Map<String, Object> getIndexedPropertyValues(String propertyName);
}
