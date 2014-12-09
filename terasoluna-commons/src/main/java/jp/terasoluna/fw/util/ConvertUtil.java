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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;


/**
 * 型変換を行うためのユーティリティクラス。
 * 
 */
public class ConvertUtil {

    /**
     * <code>class</code>フィールドを表すフィールド名
     */
    public static final String CLASS_FIELDNAME = "class";

    /**
     * オブジェクトを配列に変換する。
     * <ul>
     *   <li><code>null</code>の場合 - <code>Object[0]</code>を返却</li>
     *   <li><code>Object[]</code>の場合 - そのまま返却</li>
     *   <li><code>Collection</code>の場合 - 配列に変換して返却</li>
     *   <li>それ以外の場合 - 要素を1つ持つ配列として返却</li>
     * </ul>
     * 
     * <p>
     * 型保障された配列が欲しい場合は、{@link #toList(Object, Class)}を
     * 使用して、下記のようにすること。
     * <code><pre>
     * List<String> list = ConvertUtil.toList(value, String.class);
     * String[] array = list.toArray(new String[list.size()]);
     * </pre></code>
     * </p>
     * 
     * @param obj オブジェクト。
     * @return オブジェクトを変換した配列。
     */
    public static Object[] toArray(Object obj) {
        if (obj == null) {
            return new Object[0];
        } else if (obj.getClass().isArray()) {
            return (Object[]) obj;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).toArray();
        }
        return new Object[]{ obj };
    }

    /**
     * オブジェクトをリストに変換する。
     * <ul>
     *   <li><code>null</code>の場合 - 要素を持たない<code>T</code>型のリストとして返却</li>
     *   <li><code>Object[]</code>の場合 - <code>T</code>型のリストに変換して返却</li>
     *   <li><code>Collection</code>の場合 - <code>T</code>型のリストとして返却</li>
     *   <li>それ以外の場合 - 要素を1つ持つ<code>T</code>型のリストとして返却</li>
     * </ul>
     * 
     * @param <E> 返却するリストの要素を表す型。
     * @param obj オブジェクト。
     * @param elementClass 返却するリストの要素を表す型。 
     * @return オブジェクトを変換したリスト。
     * @throws IllegalArgumentException 引数<code>clazz</code>が
     *           <code>null</code>の場合
     *           <code>obj</code>または、その要素が<code>T</code>型
     *           ではない場合。
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> toList(Object obj, Class<E> elementClass)
            throws IllegalArgumentException {
        if (elementClass == null) {
            throw new IllegalArgumentException("Argument 'elementClass' ("
                    +  Class.class.getName() + ") is null");
        }
        
        Object[] array = toArray(obj);
        List<E> result = new ArrayList<E>();
        for (Object element : array) {
            if (element != null
                    && !elementClass.isAssignableFrom(element.getClass())) {
                String message = "Unable to cast '"
                    + element.getClass().getName()
                    + "' to '" + elementClass.getName() + "'";
                throw new IllegalArgumentException(
                        message, new ClassCastException(message));
            }
            result.add((E) element);
        }
        return result;
    }

    /**
     * オブジェクトを<code>T</code>型に変換する。
     * 
     * @param <T> 変換後の型。
     * @param obj オブジェクト。
     * @param clazz 変換後の型。
     * @return 変換後のオブジェクト。
     * @throws IllegalArgumentException 変換に失敗した場合。
     */
    public static <T> T convert(Object obj, Class<T> clazz)
            throws IllegalArgumentException {
        return convert(obj, clazz, true);
    }

    /**
     * <code>null</code>ではないオブジェクトを
     * <code>T</code>型に変換する。
     * <p>
     * プリミティブ型に対応する値などの変換に利用する。
     * </p>
     * 
     * @param <T> 変換後の型。
     * @param obj オブジェクト。
     * @param clazz 変換後の型。
     * @return 変換後のオブジェクト。
     * @throws IllegalArgumentException 変換に失敗した場合。
     *      引数<code>obj</code>が<code>null</code>の場合。
     */
    public static <T> T convertIfNotNull(Object obj, Class<T> clazz)
            throws IllegalArgumentException {
        return convert(obj, clazz, false);
    }
    
    /**
     * オブジェクトを<code>T</code>型に変換する。
     * <p>
     * <ul>
     *  <li><code>allowsNull</code>が<code>false</code>かつ
     *        <code>obj</code>が<code>null</code> - 例外をスロー。
     *  <li><code>allowsNull</code>が<code>true</code>かつ
     *        <code>obj</code>が<code>null</code> - <code>null</code>を返却。
     *  <li><code>obj</code>が<code>clazz</code>型 - そのまま返却。
     *  <li><code>obj</code>が<code>clazz</code>型ではない
     *        - <code>ConvertUtils</code>を使用して適切な型に変換して返却。
     * </ul>
     * </p>
     * 
     * @param <T> 変換後の型。
     * @param obj オブジェクト。
     * @param clazz 変換後の型。
     * @param allowsNull 引数<code>obj</code>が<code>null</code>の
     *      場合を許容するかどうか。
     * @return 変換後のオブジェクト。
     * @throws IllegalArgumentException 引数<code>clazz</code>が
     *      <code>null</code>の場合。
     *      引数<code>allowsNull</code>が<code>false</code>かつ
     *        引数<code>obj</code>が<code>null</code>の場合。
     *      変換に失敗した場合。
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(
            Object obj, Class<T> clazz, boolean allowsNull)
            throws IllegalArgumentException {

        if (clazz == null) {
            throw new IllegalArgumentException("Argument 'clazz' ("
                    +  Object.class.getName() + ") is null");
        }

        if (obj == null) {
            if (!allowsNull) {
                String message =
                    "Unable to cast 'null' to '" + clazz.getName() + "'";
                throw new IllegalArgumentException(
                        message, new ClassCastException(message));
            }
            return null;
        }

        if (clazz.isAssignableFrom(obj.getClass())) {
            return (T) obj;
        }
        
        Object result = null;
        try {
            result = ConvertUtils.convert(obj.toString(), clazz);
        } catch (ConversionException e) {
            throw new IllegalArgumentException(e);
        }
        return (T) result;
    }

    /**
     * 引数<code>value</code>がプリミティブ型の配列であれば、
     * 要素を<code>String</code>に変換して<code>List</code>に格納
     * するユーティリティメソッド。
     * 
     * @param value プリミティブ型の配列。
     * @return 引数がプリミティブ型の配列の場合、全要素を格納した<code>List</code>。
     *          それ以外の場合は引数の<code>value</code>そのもの。
     */
    public static Object convertPrimitiveArrayToList(Object value) {
        if (value == null) {
            return value;
        }
        Class type = value.getClass().getComponentType();
        
        // valueが配列型ではない場合
        if (type == null) {
            return value;
        }
        
        // 配列の要素がプリミティブ型ではない場合
        if (!type.isPrimitive()) {
            return value;
        }
        
        List<Object> list = new ArrayList<Object>();
        
        if (value instanceof boolean[]) {
            for (boolean data : (boolean[]) value) {
                // String型に変換する必要はない。
                list.add(data);
            }
        } else if (value instanceof byte[]) {
            for (byte data : (byte[]) value) {
                list.add(Byte.toString(data));
            }
        } else if (value instanceof char[]) {
            for (char data : (char[]) value) {
                list.add(Character.toString(data));
            }
        } else if (value instanceof double[]) {
            for (double data : (double[]) value) {
                list.add(Double.toString(data));
            }
        } else if (value instanceof float[]) {
            for (float data : (float[]) value) {
                list.add(Float.toString(data));
            }
        } else if (value instanceof int[]) {
            for (int data : (int[]) value) {
                list.add(Integer.toString(data));
            }
        } else if (value instanceof long[]) {
            for (long data : (long[]) value) {
                list.add(Long.toString(data));
            }
        } else if (value instanceof short[]) {
            for (short data : (short[]) value) {
                list.add(Short.toString(data));
            }
        } 
        return list;
    }

    /**
     * オブジェクトのコレクションまたは配列をマップのリストに変換する。
     * <p>
     * リストの要素であるマップは{@link #CLASS_FIELDNAME}を除く全ての
     * フィールドについて、フィールド名をキーとする値を持つ。
     * ただし、フィールド名が大文字で始まっている場合、最初の1文字が
     * 小文字に変換されるので注意すること。
     * </p>
     * <ul>
     *   <li><code>null</code>の場合 - 要素を持たないマップのリストとして返却</li>
     *   <li><code>Object[]</code>の場合 - マップのリストに変換して返却</li>
     *   <li><code>Collection</code>の場合 - マップのリストとして返却</li>
     *   <li>それ以外の場合 - 要素を1つ持つマップのリストとして返却</li>
     * </ul>
     * @param obj オブジェクト。
     * @return オブジェクトを変換したマップのリスト。
     * @throws IllegalArgumentException 変換中に予期しない例外が発生した場合。
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> toListOfMap(Object obj)
            throws IllegalArgumentException {
        Object[] array = ConvertUtil.toArray(obj);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Object object : array) {
            
            Map<String, Object> map = null;
            if (object instanceof Map) {
                map = (Map) object;
            } else {
                try {
                    map = PropertyUtils.describe(object);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                } catch (InvocationTargetException e) {
                    throw new IllegalArgumentException(e);
                } catch (NoSuchMethodException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            map.remove(CLASS_FIELDNAME);
            result.add(map);
        }
    
        return result;
    }
}
