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

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <code>Generics</code>を扱うためのユーティリティクラス。
 *
 */
public class GenericsUtil {
    /**
     * ログクラス。
     */
    private static final Log log = LogFactory.getLog(GenericsUtil.class);

    /**
     * クラスの型パラメータの実際の型を取得する。
     * <p>
     * <h5>実際の型の取得の可否</h5>
     * このクラスで実際の型を取得できるのは、クラス宣言で実際の型が
     * 指定されている場合である。 クラス宣言で実際の型が指定されていない場合、
     * クラス宣言で<code>WildCardType</code>が
     * 指定されている場合、および、コード中で変数宣言の際に実際の型が
     * 指定されている場合は取得できない。
     * <ul>
     *   <li>取得できる例(子クラスので具体クラスが指定されている)
     *     <code><pre>
     *     public class Descendant extends Generic&lt;String, Integer&gt; {
     *        ...
     *     }
     *     </pre></code>
     * この場合、正しく[<code>String</code>, <code>Integer</code>]の
     * 配列が取得できる
     *   </li>
     *   <li>取得できる例(子クラスのクラス宣言で配列が指定されている)
     *     <code><pre>
     *     public class Descendant extends Generic&lt;String[], Integer&gt; {
     *        ...
     *     }
     *     </pre></code>
     * この場合、正しく[<code>String[]</code>, <code>Integer</code>]の
     * 配列が取得できる
     *   </li>
     *   <li>取得できる例(子クラスのクラス宣言で型パラメータを持つ具体クラスが指定されている)
     *     <code><pre>
     *     public class Descendant
     *         extends Generic&lt;String[], Map&lt;String, Object&gt;&gt; {
     *        ...
     *     }
     *     </pre></code>
     * この場合、正しく[<code>String[]</code>, <code>Map</code>]の
     * 配列が取得できる
     *   </li>
     *   <li>取得できない例(子クラスのクラス宣言で型パラメータが指定されている)
     *     <code><pre>
     *     public class Descendant&lt;P, Q&gt; extends Generic&lt;S, T&gt; {
     *        ...
     *     }
     *     </pre></code>
     *   </li>
     *   <li>取得できない例(子クラスのクラス宣言でワイルドカードが指定されている)
     *     <code><pre>
     *     public class Descendant&lt;P extends String, Q super Bean&gt;
     *         extends Generic&lt;S, T&gt; {
     *        ...
     *     }
     *     </pre></code>
     *   </li>
     *   <li>取得できない例(コード中で具体クラスが指定されている)
     *     <code><pre>
     *     Generic&lt;String, Integer&gt; descendant =
     *         new Generic&lt;String, Integer&gt;();
     *     </pre></code>
     *   </li>
     * </ul>
     * </p>
     * <p>
     * <h5>多世代継承の場合</h5>
     * <code>genericType</code>から<code>descendantClass</code>まで
     * 多世代の継承がある場合も、実際の型を取得することができる。
     * <ul>
     *   <li>多世代継承の例
     *     <code><pre>
     *       public class Child&lt;S, T&gt; extends Generic&lt;S, T&gt; {
     *          ...
     *       }
     *
     *       public class GrandChild&lt;S, T&gt; extends Child&lt;S, T&gt; {
     *          ...
     *       }
     *
     *       public class Descendant extends GrandChild&lt;String, Integer&gt; {
     *          ...
     *       }
     * </pre></code>
     * この場合、正しく[<code>String</code>, <code>Integer</code>]の
     * 配列が取得できる
     *   </li>
     * </ul>
     * </p>
     * <p>
     * <h5>型パラメータの順番が変更された場合</h5>
     * <code>genericType</code>から<code>descendantClass</code>までの
     * 継承の過程で型パラメータの順番が入れ替わった場合でも、
     * <code>genercClass</code>で宣言された順番で実際の型を取得できる。
     * <ul>
     *   <li>順番が入れ替わる場合の例
     *     <code><pre>
     *       public class Generic&lt;S, T&gt; {
     *          ...
     *       }
     *
     *       public class Child&lt;A, T, B, S&gt; extends Generic&lt;S, T&gt; {
     *          ...
     *       }
     *
     *       public class Descendant
     *            extends Generic&lt;Boolean, Integer, Double, String&gt; {
     *          ...
     *       }
     * </pre></code>
     * この場合、正しく[<code>String</code>, <code>Integer</code>]の
     * 配列が取得できる
     *    </li>
     * </ul>
     * </p>
     *
     * @param <T> 型パラメータ宣言をしたクラスの型。
     * @param genericClass 型パラメータ宣言をしたクラス。
     * @param descendantClass <code>genericsClass</code>を継承し、
     *      具体的な型パラメータを指定したクラス。
     * @return 実際の型を表す<code>Class</code>インスタンスの配列。
     *               順番は<code>genercClass</code>で宣言された順番である。
     * @throws IllegalArgumentException 引数<code>genericClass</code>が
     *      <code>null</code>の場合。
     *      引数<code>descendantClass</code>が<code>null</code>の場合。
     * @throws IllegalStateException
     *       <code>descendantClass</code>の実装で型パラメータが
     *          具体クラスとして指定されていない場合。
     *      引数<code>genercClass</code>が型パラメータを宣言したクラスでは
     *      なかった場合。
     */
    @SuppressWarnings("unchecked")
    public static <T>  Class[] resolveParameterizedClass(
            Class<T> genericClass, Class<? extends T> descendantClass)
            throws IllegalArgumentException, IllegalStateException {
        if (genericClass == null) {
            throw new IllegalArgumentException(
                    "Argument 'genericsClass' ("
                    + Class.class.getName() + ") is null");
        }
        if (descendantClass == null) {
            throw new IllegalArgumentException(
                    "Argument 'descendantClass'("
                    + Class.class.getName() + ") is null");
        }

        List<ParameterizedType> ancestorTypeList =
                getAncestorTypeList(genericClass, descendantClass);

        ParameterizedType parameterizedType =
            ancestorTypeList.get(ancestorTypeList.size() - 1);
        // parameterizedTypeの実際の型引数を表す Type オブジェクトの配列
        // 例：AbstractBLogic<P, R>の型引数はPとR。
        Type[] actualTypes = parameterizedType.getActualTypeArguments();

        // インスタンスで宣言された型パラメータを実際の型に解決する。
        Class[] actualClasses = new Class[actualTypes.length];
        for (int i = 0; i < actualTypes.length; i++) {
            // actualTypes[i]→i番目の型引数。
            // ancestorList→型パラメータ宣言しているクラスのリスト。
            actualClasses[i] =
                resolveTypeVariable(actualTypes[i], ancestorTypeList);
        }
        return actualClasses;
    }

    /**
     * 型パラメータの実際の型を取得する。
     * <p>
     * <h5>実際の型の取得の可否</h5>
     * このクラスで実際の型を取得できるのは、クラス宣言で実際の型が
     * 指定されている場合である。 クラス宣言で実際の型が指定されていない場合、
     * クラス宣言で<code>WildCardType</code>が
     * 指定されている場合、および、コード中で変数宣言の際に実際の型が
     * 指定されている場合は取得できない。
     * <ul>
     *   <li>取得できる例(子クラスので具体クラスが指定されている)
     *     <code><pre>
     *     public class Descendant extends Generic&lt;String, Integer&gt; {
     *        ...
     *     }
     *     </pre></code>
     * この場合、正しく<code>String</code>または<code>Integer</code>が
     * 取得できる
     *   </li>
     *   <li>取得できる例(子クラスのクラス宣言で配列が指定されている)
     *     <code><pre>
     *     public class Descendant extends Generic&lt;String[], Integer&gt; {
     *        ...
     *     }
     *     </pre></code>
     * この場合、正しく<code>String[]</code>または<code>Integer</code>が
     * 取得できる
     *   </li>
     *   <li>取得できる例(子クラスのクラス宣言で型パラメータを持つ具体クラスが指定されている)
     *     <code><pre>
     *     public class Descendant
     *         extends Generic&lt;String[], Map&lt;String, Object&gt;&gt; {
     *        ...
     *     }
     *     </pre></code>
     * この場合、正しく<code>String[]</code>または<code>Map</code>
     * が取得できる
     *   </li>
     *   <li>取得できない例(子クラスのクラス宣言で型パラメータが指定されている)
     *     <code><pre>
     *     public class Descendant&lt;P, Q&gt; extends Generic&lt;S, T&gt; {
     *        ...
     *     }
     *     </pre></code>
     *   </li>
     *   <li>取得できない例(子クラスのクラス宣言でワイルドカードが指定されている)
     *     <code><pre>
     *     public class Descendant&lt;P extends String, Q super Bean&gt;
     *         extends Generic&lt;S, T&gt; {
     *        ...
     *     }
     *     </pre></code>
     *   </li>
     *   <li>取得できない例(コード中で具体クラスが指定されている)
     *     <code><pre>
     *     Generic&lt;String, Integer&gt; descendant =
     *         new Generic&lt;String, Integer&gt;();
     *     </pre></code>
     *   </li>
     * </ul>

     * </p>
     * <p>
     * <h5>多世代継承の場合</h5>
     * <code>genericType</code>から<code>descendantClass</code>まで
     * 多世代の継承がある場合も、実際の型を取得することができる。
     * <ul>
     *   <li>多世代継承の例
     *     <code><pre>
     *       public class Child&lt;S, T&gt; extends Generic&lt;S, T&gt; {
     *          ...
     *       }
     *
     *       public class GrandChild&lt;S, T&gt; extends Child&lt;S, T&gt; {
     *          ...
     *       }
     *
     *       public class Descendant extends GrandChild&lt;String, Integer&gt; {
     *          ...
     *       }
     * </pre></code>
     * この場合、正しく<code>String</code>または<code>Integer</code>
     * が取得できる
     *   </li>
     * </ul>
     * </p>
     * <p>
     * <h5>型パラメータの順番が変更された場合</h5>
     * <code>genericType</code>から<code>descendantClass</code>までの
     * 継承の過程で型パラメータの順番が入れ替わった場合でも、
     * <code>genercClass</code>で宣言された順番で実際の型を取得できる。
     * <ul>
     *   <li>順番が入れ替わる場合の例
     *     <code><pre>
     *       public class Generic&lt;S, T&gt; {
     *          ...
     *       }
     *
     *       public class Child&lt;A, T, B, S&gt; extends Generic&lt;S, T&gt; {
     *          ...
     *       }
     *
     *       public class Descendant
     *            extends Generic&lt;Boolean, Integer, Double, String&gt; {
     *          ...
     *       }
     * </pre></code>
     * この場合、正しく<code>String</code>または<code>Integer</code>
     * が取得できる
     *    </li>
     * </ul>
     * </p>
     *
     * @param <T> 型パラメータ宣言をしたクラスの型。
     * @param genericClass 型パラメータ宣言をしたクラス。
     * @param descendantClass <code>genericsClass</code>を継承し、
     *      具体的な型パラメータを指定したクラス。
     * @param index 実際の型を取得する型パラメータの宣言順序。
     * @return 実際の型を表す<code>Class</code>インスタンス。
     * @throws IllegalArgumentException 引数<code>genericClass</code>が
     *      <code>null</code>の場合。
     *      引数<code>descendantClass</code>が<code>null</code>の場合。
     *      引数<code>index</code>が<code>0</code>より小さい、または、
     *      宣言された型パラメータ数以上の場合。
     * @throws IllegalStateException
     *       <code>descendantClass</code>の実装で型パラメータが
     *          具体クラスとして指定されていない場合。
     *      引数<code>genercClass</code>が型パラメータを宣言したクラスでは
     *      なかった場合。
     */
    @SuppressWarnings("unchecked")
    public static <T> Class resolveParameterizedClass(
            Class<T> genericClass, Class<? extends T> descendantClass,
            int index)
            throws IllegalArgumentException, IllegalStateException {
        if (genericClass == null) {
            throw new IllegalArgumentException(
                    "Argument 'genericsClass' ("
                    + Class.class.getName() + ") is null");
        }
        if (descendantClass == null) {
            throw new IllegalArgumentException(
                    "Argument 'descendantClass'("
                    + Class.class.getName() + ") is null");
        }

        List<ParameterizedType> ancestorTypeList =
                getAncestorTypeList(genericClass, descendantClass);

        ParameterizedType parameterizedType =
            ancestorTypeList.get(ancestorTypeList.size() - 1);
        // parameterizedTypeの実際の型引数を表す Type オブジェクトの配列
        // 例：AbstractBLogic<P, R>の型引数はPとR。
        Type[] actualTypes = parameterizedType.getActualTypeArguments();

        // インスタンスで宣言された型パラメータを実際の型に解決する。
        if (index < 0 || index >= actualTypes.length) {
            throw new IllegalArgumentException(
                    "Argument 'index'(" + Integer.toString(index)
                    + ") is out of bounds of"
                    + " generics parameters");
        }

        // actualTypes[index]→index番目の型引数。
        // ancestorList→型パラメータ宣言しているクラスのリスト。
        return resolveTypeVariable(actualTypes[index], ancestorTypeList);
    }

    /**
     * 特定の型から型パラメータを宣言したクラスまでの
     * <code>ParameterizedType</code>のリストを取得する。
     *
     * @param <T> 型パラメータ宣言をしたクラスの型。
     * @param genericClass 型パラメータ宣言をしたクラス。
     * @param descendantClass <code>genericsClass</code>を継承し、
     *      具体的な型パラメータを指定したクラス。
     * @return 特定の型から型パラメータを宣言したクラスまでの
     *      <code>ParameterizedType</code>のリスト。
     * @throws IllegalStateException <code>descendantClass</code>の
     *      実装で型パラメータが具体クラスとして指定されていない場合。
     *      引数<code>genercClass</code>が型パラメータを宣言したクラスでは
     *      なかった場合。
     */
    @SuppressWarnings("unchecked")
    protected static <T> List<ParameterizedType> getAncestorTypeList(
            Class<T> genericClass, Class<? extends T> descendantClass)
           throws IllegalStateException {
        List<ParameterizedType> ancestorTypeList =
            new ArrayList<ParameterizedType>();
        Class clazz = descendantClass;
        boolean isInterface = genericClass.isInterface();

        while (clazz != null) {
            Type type = clazz.getGenericSuperclass();
            if (checkParameterizedType(type, genericClass, ancestorTypeList)) {
                break;
            }

            // 型パラメータを宣言したクラスがインタフェースの場合、
            // インタフェースについてもチェックを行う。
            if (!isInterface) {
                clazz = clazz.getSuperclass();
                continue;
            }
            if (checkInterfaceAncestors(
                    genericClass, ancestorTypeList, clazz)) {
                break;
            }

            // クラスのインタフェース内に、指定したインタフェースが存在しなかった
            // 場合に備えて、親クラスをチェック対象にする。
            // 現状この箇所を通過することはないと思われる。
            // 念のため、チェックを残しておく。
            // 理由は、GenericsのリフレクションAPIについては実装が不定であるためである。
            // 問題がある場合は、削除すること。
            clazz = clazz.getSuperclass();
        }

        // 型パラメータ宣言しているクラスのインスタンスを取得。
        // 例：AbstractBLogic<P, R>クラス。
        if (ancestorTypeList.isEmpty()) {
            throw new IllegalStateException(
                    "Argument 'genericClass'("
                    + genericClass.getName()
                    + ") does not declare type parameter");
        }

        // この箇所のチェックで例外が出る場合はないと思われるが、
        // 念のため、チェックを残しておく。
        // 理由は、GenericsのリフレクションAPIについては実装が不定であるためである。
        // 問題がある場合は、削除すること。
        ParameterizedType targetType =
            ancestorTypeList.get(ancestorTypeList.size() - 1);
        if (!targetType.getRawType().equals(genericClass)) {
            throw new IllegalStateException("Class("
                    + descendantClass.getName()
                    + ") is not concrete class of Class("
                    + genericClass.getName() + ")");
        }
        return ancestorTypeList;
    }

    /**
     * インタフェース型から型パラメータを宣言したクラスまでの
     * <code>ParameterizedType</code>のリストを取得する。
     *
     * @param <T> 型パラメータを宣言したクラスの型。
     *
     * @param genericClass 型パラメータを宣言したクラス。
     * @param ancestorTypeList <code>ParameterizedType</code>を
     *      追加するリスト。
     * @param clazz 検査対象のインタフェース型。
     * @return 型パラメータを宣言したクラスが見つかった場合は<code>true</code>。
     *      見つからなかった場合は<code>false</code>。
     */
    @SuppressWarnings("unchecked")
    protected static <T> boolean checkInterfaceAncestors(Class<T> genericClass,
            List<ParameterizedType> ancestorTypeList, Class clazz) {
        boolean genericTypeFound = false;
        Type[] interfaceTypes = clazz.getGenericInterfaces();
        for (Type interfaceType : interfaceTypes) {
            genericTypeFound = checkParameterizedType(
                    interfaceType, genericClass, ancestorTypeList);
            if (genericTypeFound) {
                return true;
            }
            Class[] interfaces = clazz.getInterfaces();
            for (Class interfaceClass : interfaces) {
                if (checkInterfaceAncestors(
                        genericClass, ancestorTypeList, interfaceClass)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <code>Type</code>型をチェックし、<code>ParameterizedType</code>
     * かつ、型パラメータを宣言したクラスのサブクラスである場合、リストに追加する。
     *
     * @param <T> 型パラメータを宣言したクラスの型。
     *
     * @param type 検査対象の型。
     * @param genericClass 型パラメータを宣言したクラス。
     * @param ancestorTypeList <code>ParameterizedType</code>を
     *      追加するリスト。
     * @return <code>type</code>が型パラメータを宣言したクラスと同じクラスの場合。
     */
    @SuppressWarnings("unchecked")
    protected static <T> boolean checkParameterizedType(
            Type type, Class<T> genericClass,
            List<ParameterizedType> ancestorTypeList) {
        if (!(type instanceof ParameterizedType)) {
            return false;
        }

        // ParameterizedTypeのインスタンスである場合、ParameterizedType
        // にキャストする。
        ParameterizedType parameterlizedType = (ParameterizedType) type;

        // インタフェースのGenericsの場合、異なるParameterizedTypeが
        // 渡されるかもしれないのでチェック。
        // ただし、現状では異なるものが渡されることはないと思われる。
        // 念のため、チェックを残しておく。
        // 理由は、GenericsのリフレクションAPIについては実装が不定であるためである。
        // 問題がある場合は、削除すること。
        if (!genericClass.isAssignableFrom(
                (Class) parameterlizedType.getRawType())) {
            return false;
        }
        ancestorTypeList.add(parameterlizedType);

        // #getRawType型パラメータ宣言のないTypeを取得する。
        if (parameterlizedType.getRawType().equals(genericClass)) {
            return true;
        }
        return false;
    }

    /**
     * 型パラメータの具体的な<code>Type</code>を取得する。
     *
     * @param type 解決する必要のある<code>Type</code>インスタンス。
     * @param ancestorTypeList <code>type</code>の具体的な型が
     *      宣言されている可能性のある<code>ParameterizedType</code>のリスト。
     * @return 実行時の型変数。
     * @throws IllegalStateException 引数<code>type</code>が
     *      <code>Class</code>型、および、
     *      <code>TypeVariable</code>型ではない場合。
     *      引数<code>type</code>がメソッド、
     *      または、コンストラクタで宣言されている場合。
     *      引数<code>type</code>の実際の型が<code>Class</code>ではない
     *      (ワイルドカード、配列)場合。
     */
    @SuppressWarnings("unchecked")
    protected static Class resolveTypeVariable (Type type,
            List<ParameterizedType> ancestorTypeList)
            throws IllegalStateException {

        if (isNotTypeVariable(type)) {
            return getRawClass(type);
        }

        // TypeVariable:型変数を定義するインタフェース。
        TypeVariable targetType = (TypeVariable) type;
        Type actualType = null;
        for (int i = ancestorTypeList.size() - 1; i >= 0; i--) {
            ParameterizedType ancestorType = ancestorTypeList.get(i);

            // 型パラメータが宣言されているクラスを取得
            GenericDeclaration declaration = targetType.getGenericDeclaration();
            if (!(declaration instanceof Class)) {
                throw new IllegalStateException("TypeVariable("
                        + targetType.getName() + " is not declared at Class "
                        + "(ie. is declared at Method or Constructor)");
            }

            // 祖先クラスがGenericsの宣言元でない場合は飛ばす。
            Class declaredClass = (Class) declaration;
            if (declaredClass != ancestorType.getRawType()) {
                continue;
            }

            // 型パラメータの宣言順序を解決して、代入された型引数を取得。
            // 例：ConcreteAbstractBLogic<R,P> extends AbstractBLogic<P,R>
            //      のような場合に正しくtypeに対応するパラメータを取り出す。
            Type[] parameterTypes = declaredClass.getTypeParameters();
            int index = ArrayUtils.indexOf(parameterTypes, targetType);
            if (index == -1) {
                // この箇所のチェックで例外が出る場合はないと思われるが、
                // 念のため、チェックを残しておく。
                // 理由は、GenericsのリフレクションAPIについては実装が不定であるためである。
                // 問題がある場合は、削除すること。
                throw new IllegalStateException("Class("
                        + declaredClass.getName()
                        + ") does not declare TypeValidable("
                        + targetType.getName() + ")");
            }
            actualType = ancestorType.getActualTypeArguments()[index];
            if (log.isDebugEnabled()) {
                log.debug("resolved " + targetType.getName()
                        + " -> " + actualType);
            }

            if (isNotTypeVariable(actualType)) {
                return getRawClass(actualType);
            }
            targetType = (TypeVariable) actualType;
        }

        throw new IllegalStateException("Concrete type of Type(" + type
                + ") was not found in ancestorList("
                + ancestorTypeList + ")");
    }

    /**
     * 引数<code>type</code>が<code>Class</code>型
     * であるか、<code>TypeVariable</code>型かを判定する。
     *
     * @param type <code>Type</code>インスタンス。
     * @return 引数<code>type</code>が
     *      <code>Class, ParameterizedType, GenericArrayType</code>の場合
     *        <code>true</code>。
     *      引数<code>type</code>が<code>TypeVariable</code>の場合
     *        <code>false</code>。
     * @throws IllegalStateException 引数<code>type</code>が
     *      <code>Class</code>、<code>ParameterizedType</code>、
     *      <code>GenericArrayType</code>、<code>TypeVariable</code>の
     *      いずれでもない場合。
     */
    protected static boolean isNotTypeVariable(Type type)
        throws IllegalStateException {
        if (type instanceof Class) {
            return true;
        } else if (type instanceof TypeVariable) {
            return false;
        } else if (type instanceof ParameterizedType) {
            return true;
        } else if (type instanceof GenericArrayType) {
            return true;
        }
        throw new IllegalStateException("Type("
                + type + ") is not instance of "
                + TypeVariable.class.getName() + ", "
                + ParameterizedType.class.getName() + ", "
                + GenericArrayType.class.getName() + " nor "
                + Class.class.getName());
    }

    /**
     * 引数<code>type</code>の実際の型を返却する。
     *
     * @param type <code>Type</code>インスタンス。
     * @return <code>Class</code>インスタンス。
     * @throws IllegalStateException 引数<code>type</code>が
     *      <code>Class</code>、<code>ParameterizedType</code>、
     *      <code>GenericArrayType</code>のいずれでもない場合。
     */
    @SuppressWarnings("unchecked")
    protected static Class getRawClass(Type type)
            throws IllegalStateException {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType =
                ((GenericArrayType) type).getGenericComponentType();
            Class componentClass = getRawClass(componentType);
            return Array.newInstance(componentClass, 0).getClass();
        }
        throw new IllegalStateException("Type("
                + type + ") is not instance of "
                + ParameterizedType.class.getName() + ", "
                + GenericArrayType.class.getName() + " nor "
                + Class.class.getName());
    }

}
