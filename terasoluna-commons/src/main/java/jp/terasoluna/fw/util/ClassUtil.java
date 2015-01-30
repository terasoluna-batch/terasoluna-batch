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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *  文字列(String)から、インスタンスを生成するユーティリティクラス。
 * 
 * <li>通常のインスタンス生成処理</li>
 * <code><pre>
 *     Integer integer = new Integer("12");
 * </pre></code>
 * 
 * <li>通常の文字列(String)からのインスタンス生成処理</li>
 * <code><pre>
 *     Integer integer = null;
 * 
 *     Class createClass = null;
 *     Class paramClass = null;
 * 
 *     //クラスローダを取得する
 *     Thread t = Thread.currentThread();
 *     ClassLoader cl = t.getContextClassLoader();
 * 
 *     try {
 * 
 *         //生成するクラスのClassオブジェクトを取得
 *         createClass = cl.loadClass("java.lang.Integer");
 *         //コンストラクタの引数となるクラスのClassオブジェクトを取得
 *         paramClass = cl.loadClass("java.lang.String");
 * 
 *     } catch(ClassNotFoundException e) {
 *         //クラスファイルが見つからなかった場合。
 *     }
 * 
 *     try {
 * 
 *         //Constructorオブジェクトを取得
 *         Constructor constructor =
 *             classObject.getConstructor(new Class[]{paramClassObject});
 * 
 *     } catch(NoSuchMethodException e) {
 *         //指定された引数を定義したコンストラクタが無かった場合
 *     } catch(SecurityException e) {
 *         //情報へのアクセスが拒否された場合
 *     }
 * 
 *     try {
 * 
 *         //インスタンスの生成
 *         integer = constructor.newInstance(new Object{"12"});
 * 
 *     } catch (IllegalArgumentException e) {
 *         //不正な引数が渡された場合
 *     } catch (InstantiationException e) {
 *         //抽象クラスだった場合
 *     } catch (IllegalAccessException e) {
 *         //コンストラクタにアクセス出来なかった場合
 *     } catch (InvocationTargetException e) {
 *         //コンストラクタが例外をスローした場合
 *     }
 * 
 * </pre></code>
 * 
 * <li>このクラスを使用した場合のインスタンス生成処理</li>
 * <code><pre>
 *     Integer integer = null;
 * 
 *     try {
 *         integer = (Integer) ClassUtil.create(
 *             "java.lang.Integer", new Object[] {"12"});
 *     } catch(ClassLoadException e) {
 *         //インスタンス生成時に例外が発生した場合
 *     }
 * </pre></code>
 * 
 * @see jp.terasoluna.fw.util.ClassLoadException
 * 
 */
public final class ClassUtil {

    /**
     * 生成するオブジェクトのクラス名を元にインスタンスを生成します。
     * 
     * クラス名が null で渡された場合、
     *  NullPointerException がスローされます。
     * @param className
     * 生成するオブジェクトのクラス名
     * @return
     * 生成したインスタンス
     * @throws ClassLoadException
     * インスタンス生成時に発生した例外をラップした例外
     */
    public static Object create(String className) throws ClassLoadException {

        // 参照を生成
        Object object = null;

        // クラスローダを取得する
        Thread t = Thread.currentThread();
        ClassLoader cl = t.getContextClassLoader();

        try {
            // Classインスタンスを生成し、オブジェクトを生成する。
            object = cl.loadClass(className).newInstance();

        } catch (InstantiationException e) {
            // 抽象クラスだった場合
            throw new ClassLoadException(e);
        } catch (IllegalAccessException e) {
            // コンストラクタにアクセス出来なかった場合
            throw new ClassLoadException(e);
        } catch (ClassNotFoundException e) {
            // *.classファイルが見つからない場合
            throw new ClassLoadException(e);
        }

        // 生成されたオブジェクトを返す。
        return object;
    }

    /**
     * 生成するオブジェクトのクラス名を元にインスタンスを生成します。
     * 
     * クラス名が null で渡された場合、
     *  NullPointerException がスローされます。
     * @param className
     * 生成するオブジェクトのクラス名
     * @param constructorParameter
     * 生成するオブジェクトのコンストラクタのパラメータ<br>
     * (注:)このパラメータは生成するオブジェクトの引数の順番と対応する必要があります。
     * @return
     * 生成したインスタンス
     * @throws ClassLoadException
     * インスタンス生成時に発生した例外をラップした例外
     */
    public static Object create(String className,
                                 Object[] constructorParameter)
                                 throws ClassLoadException {

        // 参照の生成
        Constructor[] constructors = null;

        // クラスローダを取得する
        Thread t = Thread.currentThread();
        ClassLoader cl = t.getContextClassLoader();

        try {
            // このClassインスタンスの持つ、
            //全てのコンストラクタオブジェクトを取得。
            constructors = cl.loadClass(className).getConstructors();
        } catch (SecurityException e) {
            // 情報へのアクセスが拒否された場合
            throw new ClassLoadException(e);
        } catch (ClassNotFoundException e) {
            // *.classファイルが見つからない場合
            throw new ClassLoadException(e);
        }

        // 任意のオブジェクトが生成されるまで、
        // 全てのコンストラクタオブジェクトからの生成を試みる
        for (int i = 0; i < constructors.length; i++) {

            // 参照を生成
            Object object = null;

            try {
                // コンストラクタに引数を渡し、オブジェクトの生成を試みる。
                object = constructors[i].newInstance(constructorParameter);
            } catch (IllegalArgumentException e) {
                // 不正な引数が渡された場合
                continue;
            } catch (InstantiationException e) {
                // 抽象クラスだった場合
                throw new ClassLoadException(e);
            } catch (IllegalAccessException e) {
                // コンストラクタにアクセス出来なかった場合
                throw new ClassLoadException(e);
            } catch (InvocationTargetException e) {
                // コンストラクタがスローする例外をラップ
                throw new ClassLoadException(e);
            }

            // オブジェクトが生成されていた場合処理を終了
            if (object != null) {
                return object;
            }

        }

        // オブジェクトが、生成できなかった場合は、例外をスローする
        throw new ClassLoadException(
            new IllegalArgumentException("class name is " + className));
    }

}

