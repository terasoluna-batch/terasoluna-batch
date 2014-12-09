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

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <code>JavaBean</code>のプロパティの
 * <code>Generics</code>を扱うためのユーティリティクラス。
 *
 */
public class GenericPropertyUtil {

    /**
     * ログクラス。
     */
    private static final Log log =
        LogFactory.getLog(GenericPropertyUtil.class);

    /**
     * <code>JavaBean</code>の
     * <code>Collection</code>型プロパティの要素の型を取得する。
     * <p>
     * <h5>取得例</h5>
     * <pre><code>
     * public class Bean {
     *     private List&lt;String&gt; list;
     *     public List&lt;String&gt; getList() {
     *         return this.list;
     *     }
     * }
     * </code></pre>
     * 上記のような<code>Bean</code>に対して、以下のように使用すると、
     * String.classを取得できる。
     * <pre><code>
     * Bean bean = new Bean();
     * Class elementType =
     *     GenericCollectionUtil.resolveCollectionType(
     *         bean, "list");
     * </code></pre>
     *
     * @param bean <code>JavaBean</code>インスタンス。
     * @param name <code>Collection</code>型プロパティの名前。
     * @return <code>Collection</code>の要素の型。
     *      特定できない場合は<code>Object</code>型が返却される。
     * @throws IllegalArgumentException 引数<code>bean</code>が
     *      <code>null</code>の場合。引数<code>name</code>が
     *      <code>null</code>、空文字、空白文字列の場合。
     *      <code>JavaBean</code>のプロパティの
     *      取得メソッドを取得できなかった場合
     * @throws IllegalStateException 指定されたプロパティが<code>Collection</code>実装クラス
     *      ではない場合。
     */
    @SuppressWarnings("unchecked")
    public static Class resolveCollectionType(Object bean, String name)
            throws IllegalArgumentException, IllegalStateException {
        return resolveType(bean, name, Collection.class, 0);
    }

    /**
     * <code>JavaBean</code>の
     * <code>Generics</code>型プロパティで指定された型を取得する。
     * <p>
     * <h5>取得例</h5>
     * <pre><code>
     * public class Bean {
     *     private Map&lt;String, Boolean&gt; map;
     *     public Map&lt;String, Boolean&gt; getMap() {
     *         return this.map;
     *     }
     * }
     * </code></pre>
     * 上記のような<code>Bean</code>に対して、以下のように使用すると、
     * String.classを取得できる。
     * <pre><code>
     * Bean bean = new Bean();
     * Class keyType =
     *     GenericCollectionUtil.resolveType(
     *         bean, "map", Map.class, 0);
     * </code></pre>
     *
     * @param bean <code>JavaBean</code>インスタンス。
     * @param name <code>Generics</code>型プロパティの名前。
     * @param genericClass <code>Generics</code>型プロパティの
     *      型定義を行っているクラス。
     * @param index 型パラメータの宣言順序。
     * @return <code>Generics</code>型プロパティで指定された型。
     *      特定できない場合は<code>Object</code>型が返却される。
     * @throws IllegalArgumentException 引数<code>bean</code>が
     *      <code>null</code>の場合。引数<code>name</code>が
     *      <code>null</code>、空文字、空白文字列の場合。
     *      引数<code>genericClass</code>が<code>null</code>の場合。
     *      引数<code>index</code>が<code>0</code>より小さい、または、
     *      宣言された型パラメータ数以上の場合。
     *      <code>JavaBean</code>のプロパティの
     *      取得メソッドを取得できなかった場合
     * @throws IllegalStateException 型パラメータが<code>WildCardType</code>である場合。
     */
    @SuppressWarnings("unchecked")
    public static Class resolveType(Object bean, String name,
            Class genericClass, int index)
            throws IllegalArgumentException, IllegalStateException {
        if (bean == null) {
            throw new IllegalArgumentException(
                    "Argument 'bean' ("
                    + Object.class.getName() + " is null");
        }
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException(
                    "Argument 'name' ("
                    + String.class.getName() + " is empty");
        }

        Method method = getMethod(bean, name);
        return resolveType(
                genericClass, method.getReturnType(),
                method.getGenericReturnType(), index);
    }

    /**
     * <code>JavaBean</code>のプロパティの取得メソッドを
     * 取得する。
     *
     * @param bean <code>JavaBean</code>インスタンス。
     * @param name <code>Generics</code>型プロパティの名前。
     * @return <code>JavaBean</code>に定義されたプロパティの取得メソッド。
     * @throws IllegalArgumentException <code>JavaBean</code>のプロパティの
     * 取得メソッドを取得できなかった場合。
     */
    protected static Method getMethod(Object bean, String name)
            throws IllegalArgumentException {
        PropertyDescriptor descriptor = null;
        try {
            descriptor = PropertyUtils.getPropertyDescriptor(bean, name);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Failed to detect getter for "
                    + bean.getClass().getName() + "#" + name, e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Failed to detect getter for "
                    + bean.getClass().getName() + "#" + name, e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Failed to detect getter for "
                    + bean.getClass().getName() + "#" + name, e);
        }
        Method method = null;
        if (descriptor != null) {
            method = descriptor.getReadMethod();
        }
        if (method == null) {
            throw new IllegalArgumentException(bean.getClass().getName()
                    + " has no getter for property " + name);
        }
        return method;
    }

    /**
     * フィールド、または、メソッドの情報を元に
     * <code>Generics</code>型で指定された型を取得する。
     *
     * @param genericClass <code>Generics</code>型プロパティの
     *      型定義を行っているクラス。
     * @param clazz 具体的な型パラメータを指定したクラス。
     * @param type 具体的な型パラメータを指定したクラスのインスタンスの
     *      <code>Type</code>インスタンス。
     * @param index 型パラメータの宣言順序。
     * @return <code>Generics</code>型で指定された型。
     *      特定できない場合は<code>Object</code>型が返却される。
     * @throws IllegalArgumentException 引数<code>genericClass</code>が
     *      <code>null</code>の場合。
     *      引数<code>clazz</code>が<code>null</code>の場合。
     *      引数<code>index</code>が<code>0</code>より小さい、または、
     *      宣言された型パラメータ数以上の場合。
     * @throws IllegalStateException 型パラメータが<code>WildCardType</code>である場合。
     */
    @SuppressWarnings("unchecked")
    protected static Class resolveType(Class genericClass, Class clazz,
            Type type, int index)
            throws IllegalArgumentException, IllegalStateException {
        if (genericClass == null) {
            throw new IllegalArgumentException(
                    "Argument 'genericsClass' ("
                    + Class.class.getName() + ") is null");
        }
        if (clazz == null || !genericClass.isAssignableFrom(clazz)) {
            throw new IllegalStateException(
                    genericClass+ " is not assignable from " + clazz);
        }

        List<ParameterizedType> ancestorTypeList = null;
        try {
            ancestorTypeList =
                GenericsUtil.getAncestorTypeList(genericClass, clazz);
        } catch (IllegalStateException e) {
            if (log.isTraceEnabled()) {
                log.trace(e.getMessage());
            }
        }
        if (ancestorTypeList == null) {
            ancestorTypeList = new ArrayList<ParameterizedType>();
        }
        if (type instanceof ParameterizedType) {
            ancestorTypeList.add(0, (ParameterizedType) type);
        }
        if (ancestorTypeList.size() <= 0) {
            throw new IllegalStateException(
                    "No parameterizedType was detected.");
        }
        ParameterizedType parameterizedType =
            ancestorTypeList.get(ancestorTypeList.size() - 1);
        Type[] actualTypes = parameterizedType.getActualTypeArguments();

        // インスタンスで宣言された型パラメータを実際の型に解決する。
        if (index < 0 || index >= actualTypes.length) {
            throw new IllegalArgumentException(
                    "Argument 'index'(" + Integer.toString(index)
                    + ") is out of bounds of"
                    + " generics parameters");
        }

        Class resolved = Object.class;
        try {
            resolved = GenericsUtil.resolveTypeVariable(
                    actualTypes[index], ancestorTypeList);
        } catch (IllegalStateException e) {
            if (log.isTraceEnabled()) {
                log.trace(e.getMessage());
            }
        }
        return resolved;
    }

}
