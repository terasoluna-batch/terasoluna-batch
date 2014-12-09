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

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.util.GenericsUtil} クラスのテスト。
 *
 * <p>
 * <h4>【クラスの概要】</h4>
 * <code>Generics</code>を扱うためのユーティリティクラス。
 * <p>
 *
 * @see jp.terasoluna.fw.client.util.GenericsUtil
 */
public class GenericsUtilTest extends TestCase {


    /**
     * 初期化処理を行う。
     *
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 終了処理を行う。
     *
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * コンストラクタ。
     *
     * @param name このテストケースの名前。
     */
    public GenericsUtilTest(String name) {
        super(name);
    }

    /**
     * testResolveParameterizedClassClassClass01()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) genericClass:null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ："Argument 'genericsClass' ("<br>
     *                    + Class.class.getName() + ") is null"<br>
     *
     * <br>
     * 引数genericClassがnullの場合にIllegalArgumentExceptionが
     * スローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testResolveParameterizedClassClassClass01() throws Exception {

        try {
            //  テスト実施
            GenericsUtil.resolveParameterizedClass(null, null);
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            //  判定
            assertEquals("Argument 'genericsClass' ("
                    + Class.class.getName() + ") is null",
                    e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testResolveParameterizedClassClassClass02()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) genericClass:ObjectのClassインスタンス<br>
     *         (引数) descendantClass:null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ："Argument 'descendantClass'("<br>
     *                    + Class.class.getName() + ") is null"<br>
     *
     * <br>
     * 引数descendantClassがnullの場合にIllegalArgumentExceptionが
     * スローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testResolveParameterizedClassClassClass02() throws Exception {
        try {
            //  テスト実施
            GenericsUtil.resolveParameterizedClass(Object.class, null);
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            //  判定
            assertEquals("Argument 'descendantClass'("
                    + Class.class.getName() + ") is null",
                    e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testResolveParameterizedClassClassClass03()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を持たない親クラス<br>
     *         (引数) descendantClass:genericClassの子クラス<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     *
     * <br>
     * 引数genericClass型パラメータ宣言を持たない親クラスである場合に、
     * IllegalStateExceptionがスローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testResolveParameterizedClassClassClass03() throws Exception {
        try {
            //  テスト実施
            GenericsUtil.resolveParameterizedClass(Object.class, String.class);
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            //  判定
            assertEquals(IllegalStateException.class.getName(),
                    e.getClass().getName());

        }
    }

    /**
     * testResolveParameterizedClassClassClass04()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を1つ持つ親クラス<br>
     *         (引数) descendantClass:genericClassの子クラス
     *                  (型パラメータの具体クラスを指定していない)<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     *
     * <br>
     * 型パラメータに具体クラスが指定されていない場合に、
     * IllegalStateExceptionがスローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testResolveParameterizedClassClassClass04() throws Exception {
        try {
            //  テスト実施
            GenericsUtil.resolveParameterizedClass(
                    FutureTask.class, GenericsUtil_FutureTaskStub01.class);
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            //  判定
            assertEquals(IllegalStateException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testResolveParameterizedClassClassClass05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を1つ持つ親クラス<br>
     *         (引数) descendantClass:genericClassの子クラス
     *                  (型パラメータの具体クラスにStringを指定)<br>
     *
     * <br>
     * 期待値：(戻り値) Class[]:要素0 = String.class<br>
     *
     * <br>
     * descendantClassが子クラスである場合に、
     * 型パラメータの具体クラスが取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveParameterizedClassClassClass05() throws Exception {
        //  テスト実施
        Class[] result = GenericsUtil.resolveParameterizedClass(
                FutureTask.class, GenericsUtil_FutureTaskStub02.class);

        //  判定
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(String.class, result[0]);
    }

    /**
     * testResolveParameterizedClassClassClass06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を1つ持つ親クラス<br>
     *         (引数) descendantClass:genericClassの孫クラス
     *                  (型パラメータの具体クラスにMap<String, Object>を指定)<br>
     *
     * <br>
     * 期待値：(戻り値) Class[]:要素0 = Map.class<br>
     *
     * <br>
     * descendantClassが孫クラスである場合に、型パラメータの具体クラスが
     * 取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveParameterizedClassClassClass06() throws Exception {
        //  テスト実施
        Class[] result = GenericsUtil.resolveParameterizedClass(
                FutureTask.class, GenericsUtil_FutureTaskStub03.class);

        //  判定
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(Map.class, result[0]);
    }

    /**
     * testResolveParameterizedClassClassClass07()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を3つ持つ親クラス<br>
     *         (引数) descendantClass:genericClassの子クラス
     *                  (型パラメータの具体クラスにString[], Integer, Booleanを指定)<br>
     *
     * <br>
     * 期待値：(戻り値) Class[]:要素0 = String[].class<br>
     *                  要素1 = Integer.class<br>
     *                  要素2 = Boolean.class<br>
     *
     * <br>
     * 型パラメータが複数の場合に、型パラメータの具体クラスが取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveParameterizedClassClassClass07() throws Exception {
        //  テスト実施
        Class[] result = GenericsUtil.resolveParameterizedClass(
                GenericsUtil_Stub01.class, GenericsUtil_Stub02.class);

        //  判定
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(String[].class, result[0]);
        assertEquals(Integer.class, result[1]);
        assertEquals(Boolean.class, result[2]);
    }

    /**
     * testResolveParameterizedClassClassClass08()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を3つ持つ親インタフェース<br>
     *         (引数) descendantClass:genericClassの実装クラス
     *              (型パラメータの具体クラスにString[], Integer, Booleanを指定)<br>
     *
     * <br>
     * 期待値：(戻り値) Class[]:要素0 = String[].class<br>
     *                  要素1 = Integer.class<br>
     *                  要素2 = Boolean.class<br>
     *
     * <br>
     * 型パラメータ宣言がインタフェースで行われている場合に、型パラメータの具体クラスが取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveParameterizedClassClassClass08() throws Exception {
        //  テスト実施
        Class[] result = GenericsUtil.resolveParameterizedClass(
                GenericsUtil_Stub03.class, GenericsUtil_Stub04.class);

        //  判定
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(String[].class, result[0]);
        assertEquals(Integer.class, result[1]);
        assertEquals(Boolean.class, result[2]);
    }

    /**
     * testResolveParameterizedClassClassClass09()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を3つ持つ親インタフェース<br>
     *         (引数) descendantClass:genericClassの継承インタフェースの実装クラス
     *              (型パラメータの具体クラスにString[], Integer, Booleanを指定)<br>
     *
     * <br>
     * 期待値：(戻り値) Class[]:要素0 = String[].class<br>
     *                  要素1 = Integer.class<br>
     *                  要素2 = Boolean.class<br>
     *
     * <br>
     * 型パラメータ宣言がインタフェースで行われており、インタフェースが継承されている場合に、
     * 型パラメータの具体クラスが取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveParameterizedClassClassClass09() throws Exception {
        //  テスト実施
        Class[] result = GenericsUtil.resolveParameterizedClass(
                GenericsUtil_Stub03.class, GenericsUtil_Stub06.class);

        //  判定
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(String[].class, result[0]);
        assertEquals(Integer.class, result[1]);
        assertEquals(Boolean.class, result[2]);
    }

    /**
     * testResolveParameterizedClassClassClass10()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を3つ持つ親インタフェース<br>
     *         (引数) descendantClass:genericClassの継承インタフェースの実装クラスのサブクラス
     *              (型パラメータは実装クラスで指定)
     *              (型パラメータの具体クラスにString[], Integer, Booleanを指定)<br>
     *
     * <br>
     * 期待値：(戻り値) Class[]:要素0 = String[].class<br>
     *                  要素1 = Integer.class<br>
     *                  要素2 = Boolean.class<br>
     *
     * <br>
     * 型パラメータ宣言がインタフェースで行われており、インタフェースが継承され、
     * かつ、実装クラスが継承されている場合に、
     * 型パラメータの具体クラスが取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveParameterizedClassClassClass10() throws Exception {
        //  テスト実施
        Class[] result = GenericsUtil.resolveParameterizedClass(
                GenericsUtil_Stub03.class, GenericsUtil_Stub07.class);

        //  判定
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(String[].class, result[0]);
        assertEquals(Integer.class, result[1]);
        assertEquals(Boolean.class, result[2]);
    }

    /**
     * testResolveParameterizedClassClassClass11()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を3つ持つ親インタフェース<br>
     *         (引数) descendantClass:genericClassの継承インタフェースの実装クラスのサブクラス
     *              (型パラメータはサブクラスで指定)
     *              (型パラメータの具体クラスにString[], Integer, Booleanを指定)<br>
     *
     * <br>
     * 期待値：(戻り値) Class[]:
     *                  要素0 = Boolean.class<br>
     *                  要素1 = String[].class<br>
     *                  要素2 = Integer.class<br>
     *
     * <br>
     * 型パラメータ宣言がインタフェースで行われており、インタフェースが継承され、
     * かつ、実装クラスが継承されている場合に、
     * 型パラメータの具体クラスが取得できることを確認するテスト。
     * 型パラメータの宣言順が入れ替えられていた場合でも正しく取得できることの
     * テストを包含する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveParameterizedClassClassClass11() throws Exception {
        //  テスト実施
        Class[] result = GenericsUtil.resolveParameterizedClass(
                GenericsUtil_Stub03.class, GenericsUtil_Stub09.class);

        //  判定
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(Boolean.class, result[0]);
        assertEquals(String[].class, result[1]);
        assertEquals(Integer.class, result[2]);
    }

    /**
     * testResolveParameterizedClassClassClassint01()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) genericClass:null<br>
     *         (引数) descendantClass:null<br>
     *         (引数) index:0<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ："Argument 'genericsClass' ("<br>
     *                    + Class.class.getName() + ") is null"<br>
     *
     * <br>
     * 引数genericClassがnullの場合にIllegalArgumentExceptionが
     * スローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testResolveParameterizedClassClassClassint01() throws Exception {

        try {
            //  テスト実施
            GenericsUtil.resolveParameterizedClass(null, null, 0);
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            //  判定
            assertEquals("Argument 'genericsClass' ("
                    + Class.class.getName() + ") is null",
                    e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testResolveParameterizedClassClassClassint02()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) genericClass:ObjectのClassインスタンス<br>
     *         (引数) descendantClass:null<br>
     *         (引数) index:0<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ："Argument 'descendantClass'("<br>
     *                    + Class.class.getName() + ") is null"<br>
     *
     * <br>
     * 引数descendantClassがnullの場合にIllegalArgumentExceptionが
     * スローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testResolveParameterizedClassClassClassint02() throws Exception {
        try {
            //  テスト実施
            GenericsUtil.resolveParameterizedClass(Object.class, null, 0);
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            //  判定
            assertEquals("Argument 'descendantClass'("
                    + Class.class.getName() + ") is null",
                    e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testResolveParameterizedClassClassClassint03()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を持たない親クラス<br>
     *         (引数) descendantClass:genericClassの子クラス<br>
     *         (引数) index:0<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     *
     * <br>
     * 引数genericClass型パラメータ宣言を持たない親クラスである場合に、
     * IllegalStateExceptionがスローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testResolveParameterizedClassClassClassint03() throws Exception {
        try {
            //  テスト実施
            GenericsUtil.resolveParameterizedClass(Object.class, String.class, 0);
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            //  判定
            assertEquals(IllegalStateException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testResolveParameterizedClassClassClassint04()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を1つ持つ親クラス<br>
     *         (引数) descendantClass:genericClassの子クラス
     *                  (型パラメータの具体クラスを指定していない)<br>
     *         (引数) index:0<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     *
     * <br>
     * 型パラメータに具体クラスが指定されていない場合に、
     * IllegalStateExceptionがスローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testResolveParameterizedClassClassClassint04() throws Exception {
        try {
            //  テスト実施
            GenericsUtil.resolveParameterizedClass(
                    FutureTask.class, GenericsUtil_FutureTaskStub01.class, 0);
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            //  判定
            assertEquals(IllegalStateException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testResolveParameterizedClassClassClassint05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を1つ持つ親クラス<br>
     *         (引数) descendantClass:genericClassの子クラス
     *                  (型パラメータの具体クラスにStringを指定)<br>
     *         (引数) index:0<br>
     *
     * <br>
     * 期待値：(戻り値) Class:String.class<br>
     *
     * <br>
     * descendantClassが子クラスである場合に、
     * 型パラメータの具体クラスが取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveParameterizedClassClassClassint05() throws Exception {
        //  テスト実施
        Class result = GenericsUtil.resolveParameterizedClass(
                FutureTask.class, GenericsUtil_FutureTaskStub02.class, 0);

        //  判定
        assertEquals(String.class, result);
    }

    /**
     * testResolveParameterizedClassClassClassint06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を1つ持つ親クラス<br>
     *         (引数) descendantClass:genericClassの孫クラス
     *                  (型パラメータの具体クラスにMap<String, Object>を指定)<br>
     *         (引数) index:0<br>
     *
     * <br>
     * 期待値：(戻り値) Class:Map.class<br>
     *
     * <br>
     * descendantClassが孫クラスである場合に、型パラメータの具体クラスが
     * 取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveParameterizedClassClassClassint06() throws Exception {
        //  テスト実施
        Class result = GenericsUtil.resolveParameterizedClass(
                FutureTask.class, GenericsUtil_FutureTaskStub03.class, 0);

        //  判定
        assertEquals(Map.class, result);
    }

    /**
     * testResolveParameterizedClassClassClassint07()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を1つ持つ親クラス<br>
     *         (引数) descendantClass:genericClassの子クラス
     *                  (型パラメータの具体クラスにStringを指定)<br>
     *         (引数) index:-1<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ："Argument 'index'(-1) is out of
     *                    bounds of generics parameters"<br>
     * <br>
     * 引数indexが負の数である場合に、IllegalArgumentExceptionが
     * スローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testResolveParameterizedClassClassClassint07() throws Exception {
        try {
            //  テスト実施
            GenericsUtil.resolveParameterizedClass(
                    FutureTask.class, GenericsUtil_FutureTaskStub02.class, -1);
        } catch (IllegalArgumentException e) {
            //  判定
            assertEquals("Argument 'index'(-1) is out of bounds of " +
                    "generics parameters",
                    e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testResolveParameterizedClassClassClassint08()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を1つ持つ親クラス<br>
     *         (引数) descendantClass:genericClassの孫クラス
     *                  (型パラメータの具体クラスにIntegerを指定)<br>
     *         (引数) index:1<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ："Argument 'index'(1) is out of
     *                    bounds of generics parameters"<br>
     *
     * <br>
     * 引数indexがパラメータ数より多い場合に、IllegalArgumentExceptionが
     * スローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testResolveParameterizedClassClassClassint08() throws Exception {
        try {
            //  テスト実施
            GenericsUtil.resolveParameterizedClass(
                    FutureTask.class, GenericsUtil_FutureTaskStub03.class, 1);
        } catch (IllegalArgumentException e) {
            //  判定
            assertEquals("Argument 'index'(1) is out of bounds of " +
                    "generics parameters",
                    e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testResolveParameterizedClassClassClassint09()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を3つ持つ親クラス<br>
     *         (引数) descendantClass:genericClassの子クラス
     *                  (型パラメータの具体クラスにString[], Integer, Booleanを指定)<br>
     *         (引数) index:0<br>
     *
     * <br>
     * 期待値：(戻り値) Class:String[].class<br>
     *
     * <br>
     * 型パラメータが複数の場合に、型パラメータの具体クラスが取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveParameterizedClassClassClassint09() throws Exception {
        //  テスト実施
        Class result = GenericsUtil.resolveParameterizedClass(
                GenericsUtil_Stub01.class, GenericsUtil_Stub02.class, 0);

        //  判定
        assertEquals(String[].class, result);
    }

    /**
     * testResolveParameterizedClassClassClassint10()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を3つ持つ親インタフェース<br>
     *         (引数) descendantClass:genericClassの実装クラス
     *              (型パラメータの具体クラスにString[], Integer, Booleanを指定)<br>
     *         (引数) index:2<br>
     *
     * <br>
     * 期待値：(戻り値) Class:Boolean.class<br>
     *
     * <br>
     * 型パラメータ宣言がインタフェースで行われている場合に、型パラメータの具体クラスが取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveParameterizedClassClassClassint10() throws Exception {
        //  テスト実施
        Class result = GenericsUtil.resolveParameterizedClass(
                GenericsUtil_Stub03.class, GenericsUtil_Stub04.class, 2);

        //  判定
        assertEquals(Boolean.class, result);
    }


    /**
     * testResolveParameterizedClassClassClassint11()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を3つ持つ親インタフェース<br>
     *         (引数) descendantClass:genericClassの継承インタフェースの実装クラス
     *              (型パラメータの具体クラスにString[], Integer, Booleanを指定)<br>
     *         (引数) index:1<br>
     *
     * <br>
     * 期待値：(戻り値) Class:Integer.class<br>
     *
     * <br>
     * 型パラメータ宣言がインタフェースで行われており、インタフェースが継承されている場合に、
     * 型パラメータの具体クラスが取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveParameterizedClassClassClassint11() throws Exception {
        //  テスト実施
        Class result = GenericsUtil.resolveParameterizedClass(
                GenericsUtil_Stub03.class, GenericsUtil_Stub06.class, 1);

        //  判定
        assertEquals(Integer.class, result);
    }

    /**
     * testResolveParameterizedClassClassClassint12()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を3つ持つ親インタフェース<br>
     *         (引数) descendantClass:genericClassの継承インタフェースの実装クラスのサブクラス
     *              (型パラメータの具体クラスにString[], Integer, Booleanを指定)<br>
     *         (引数) index:1<br>
     *
     * <br>
     * 期待値：(戻り値) Class:Integer.class<br>
     *
     * <br>
     * 型パラメータ宣言がインタフェースで行われており、インタフェースが継承され、
     * かつ、実装クラスが継承されている場合に、
     * 型パラメータの具体クラスが取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveParameterizedClassClassClassint12() throws Exception {
        //  テスト実施
        Class result = GenericsUtil.resolveParameterizedClass(
                GenericsUtil_Stub03.class, GenericsUtil_Stub07.class, 1);

        //  判定
        assertEquals(Integer.class, result);
    }
    /**
     * testGetAncestorTypeList01()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を持たない親クラス<br>
     *         (引数) descendantClass:genericClassの子クラス<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     *                    メッセージ："Argument 'genericClass'("<br>
     *                    + genericClass.getName()<br>
     *                    + ") does not declare type parameter"<br>
     *
     * <br>
     * 引数genericClass型パラメータ宣言を持たない親クラスである場合に、
     * IllegalStateExceptionがスローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetAncestorTypeList01() throws Exception {
        try {
            //  テスト実施
            GenericsUtil.getAncestorTypeList(Object.class, String.class);
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            //  判定
            assertEquals("Argument 'genericClass'("
                        + Object.class.getName()
                        + ") does not declare type parameter",
                    e.getMessage());
            assertEquals(IllegalStateException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testGetAncestorTypeList02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を1つ持つ親クラス<br>
     *         (引数) descendantClass:genericClassの子クラス
     *              (型パラメータの具体クラスにStringを指定)<br>
     *
     * <br>
     * 期待値：(戻り値) List<ParameterizedType>:要素0 = genericClassの<br>
     *                  ParameterizedType<br>
     *
     * <br>
     * 1世代継承している場合に、ParameterizedTypeのリストが取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetAncestorTypeList02() throws Exception {
        //  テスト実施
        List<ParameterizedType> result = GenericsUtil.getAncestorTypeList(
                FutureTask.class, GenericsUtil_FutureTaskStub02.class);

        //  判定
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(GenericsUtil_FutureTaskStub02.class.getGenericSuperclass(),
                result.get(0));
    }

    /**
     * testGetAncestorTypeList03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を1つ持つ親クラス<br>
     *         (引数) descendantClass:genericClassの孫クラス
     *                  (型パラメータの具体クラスにIntegerを指定)<br>
     *
     * <br>
     * 期待値：(戻り値) List<ParameterizedType>:要素0 = genericClassの<br>
     *                  ParameterizedType<br>
     *                  要素1 = genericClassの子クラスの<br>
     *                  ParameterizedType<br>
     *
     * <br>
     * 複数世代継承している場合に、ParameterizedTypeのリストが取得できることを
     * 確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetAncestorTypeList03() throws Exception {
        //  テスト実施
        List<ParameterizedType> result = GenericsUtil.getAncestorTypeList(
                FutureTask.class, GenericsUtil_FutureTaskStub03.class);

        //  判定
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(
                getParameterizedClassList(
                        FutureTask.class, GenericsUtil_FutureTaskStub03.class),
                result);
    }

    /**
     * testGetAncestorTypeList04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を1つ持つインタフェース<br>
     *         (引数) descendantClass:genericClassの実装クラス
     *                  (型パラメータの具体クラスにIntegerを指定)<br>
     *
     * <br>
     * 期待値：(戻り値) List<ParameterizedType>:要素0 = genericClassの<br>
     *                  ParameterizedType<br>
     *
     * <br>
     * インタフェースを１つ実装している場合に、ParameterizedTypeのリストが
     * 取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetAncestorTypeList04() throws Exception {
        //  テスト実施
        List<ParameterizedType> result = GenericsUtil.getAncestorTypeList(
                Callable.class, GenericsUtil_CallableStub01.class);

        //  判定
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                GenericsUtil_CallableStub01.class.getGenericInterfaces()[0],
                result.get(0));
    }

    /**
     * testGetAncestorTypeList05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:型パラメータ宣言を1つ持つインタフェース<br>
     *         (引数) descendantClass:genericClassの実装クラスの子クラス
     *              (型パラメータの具体クラスにIntegerを指定、
     *              また、Comparable<T>, Comparator<T>を実装)<br>
     *
     * <br>
     * 期待値：(戻り値) List<ParameterizedType>:要素0 = genericClassの<br>
     *                  ParameterizedType<br>
     *
     * <br>
     * インタフェースを複数実装している場合に、ParameterizedTypeのリストが
     * 取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetAncestorTypeList05() throws Exception {
        //  テスト実施
        List<ParameterizedType> result = GenericsUtil.getAncestorTypeList(
                Callable.class, GenericsUtil_CallableStub02.class);

        //  判定
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                GenericsUtil_CallableStub02.class.getGenericInterfaces()[0],
                result.get(0));
    }

    /**
     * testGetAncestorTypeList06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) genericClass:Comparable.class<br>
     *         (引数) descendantClass:Callableの実装クラスの子クラス
     *                  (型パラメータの具体クラスにIntegerを指定、
     *                  また、Comparable<T>, Comparator<T>を実装)<br>
     *
     * <br>
     * 期待値：(戻り値) List<ParameterizedType>:要素0 = Comparableの<br>
     *                  ParameterizedType<br>
     *                  要素1 = Comparatorの<br>
     *                  ParameterizedType<br>
     *
     * <br>
     * インタフェースを複数実装しており、かつ、複数世代で実装されている場合に、
     * ParameterizedTypeのリストが取得できることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetAncestorTypeList06() throws Exception {
        //  テスト実施
        List<ParameterizedType> result = GenericsUtil.getAncestorTypeList(
                Callable.class, GenericsUtil_CallableStub03.class);

        //  判定
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                GenericsUtil_CallableStub03.class.getGenericInterfaces()[0],
                result.get(0));
    }

    /**
     * testCheckParameterizedType01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) type:String.class<br>
     *         (引数) genericClass:ArrayList.class<br>
     *         (引数) ancestorTypeList:空のArrayList<ParameterizedType><br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) ancestorTypeList:変化なし<br>
     *
     * <br>
     * typeがParameterizedTypeではない場合に、リストに何も追加されず
     * falseが返却されることのテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testCheckParameterizedType01() throws Exception {
        //  前処理
        List<ParameterizedType> list = new ArrayList<ParameterizedType>();

        //  テスト実施
        boolean result = GenericsUtil.checkParameterizedType(
                ArrayList.class, List.class, list);

        //  判定
        assertFalse(result);
        assertTrue(list.isEmpty());
    }

    /**
     * testCheckParameterizedType02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) type:ArrayList継承クラスについて、
     *              ArrayList<E>のgetParameterizedType()<br>
     *         (引数) genericClass:Map.class<br>
     *         (引数) ancestorTypeList:空のArrayList<ParameterizedType><br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) ancestorTypeList:変化なし<br>
     *
     * <br>
     * typeがgenericClassの子クラスではない場合に、
     * リストに何も追加されずfalseが返却されることのテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testCheckParameterizedType02() throws Exception {
        //  前処理
        List<ParameterizedType> list = new ArrayList<ParameterizedType>();

        //  テスト実施
        boolean result = GenericsUtil.checkParameterizedType(
                GenericsUtil_ArrayListStub01.class, Map.class, list);

        //  判定
        assertFalse(result);
        assertTrue(list.isEmpty());
    }

    /**
     * testCheckParameterizedType03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C, E
     * <br><br>
     * 入力値：(引数) type:ArrayList継承クラスについて、
     *                  ArrayList<E>のgetParameterizedType()<br>
     *         (引数) genericClass:List.class<br>
     *         (引数) ancestorTypeList:空のArrayList<ParameterizedType><br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) ancestorTypeList:
     *              要素0 = ArrayList<E>のgetParameterizedType()<br>
     *
     * <br>
     * typeの実際のクラスがgenericClassと同一ではない場合に、リストに追加され、
     * falseが返却されることのテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testCheckParameterizedType03() throws Exception {
        //  前処理
        List<ParameterizedType> list = new ArrayList<ParameterizedType>();

        //  テスト実施
        boolean result = GenericsUtil.checkParameterizedType(
                GenericsUtil_ArrayListStub01.class.getGenericSuperclass(),
                List.class, list);

        //  判定
        assertFalse(result);
        assertEquals(1, list.size());
        assertEquals(
                GenericsUtil_ArrayListStub01.class.getGenericSuperclass(),
                list.get(0));
    }

    /**
     * testCheckParameterizedType04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C, E
     * <br><br>
     * 入力値：(引数) type:ArrayList継承クラスについて、
     *                  List<E>のgetParameterizedType()<br>
     *         (引数) genericClass:List.class<br>
     *         (引数) ancestorTypeList:空のArrayList<ParameterizedType><br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) ancestorTypeList:要素0 = List<E>のgetParameterizedType()<br>
     *
     * <br>
     * typeの実際のクラスがgenericClassと同一である場合に、リストに追加され、
     * trueが返却されることのテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings({ "null", "unchecked" })
    public void testCheckParameterizedType04() throws Exception {
        //  前処理
        Class listClass = GenericsUtil_ArrayListStub01.class;
        while (listClass != null && !listClass.equals(AbstractList.class)) {
            listClass = listClass.getSuperclass();
        }
        if (listClass == null) {
            fail(AbstractList.class.getName() + "が取得できません。");
        }
        Type type = listClass.getGenericInterfaces()[0];

        List<ParameterizedType> list = new ArrayList<ParameterizedType>();

        //  テスト実施
        boolean result = GenericsUtil.checkParameterizedType(
                type, List.class, list);

        // 判定
        assertTrue(result);
        assertEquals(1, list.size());
        assertEquals(type, list.get(0));
    }

    /**
     * testResolveTypeVariable01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) type:String.class<br>
     *         (引数) ancestorTypeList:空のArrayList<ParameterizedType><br>
     *
     * <br>
     * 期待値：(戻り値) Class:String.class<br>
     *
     * <br>
     * typeがClass型である場合に、typeがそのまま返却されることのテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveTypeVariable01() throws Exception {
        // 前処理
        Class stringClass = String.class;

        //  テスト実施
        Class result = GenericsUtil.resolveTypeVariable(
                stringClass, new ArrayList<ParameterizedType>());

        //  判定
        assertSame(stringClass, result);
    }

    /**
     * testResolveTypeVariable02()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：C, G
     * <br><br>
     * 入力値：(引数) type:ArrayList継承クラス<String>のList<E>について型変数E<br>
     *         (引数) ancestorTypeList:空のArrayList<ParameterizedType><br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     *                    メッセージ："Concrete type of Type(E) was not
     *                    found in ancestorList("<br>
     *                    + ancestorTypeList + ")"<br>
     *
     * <br>
     * ancestorTypeList中にtypeに関連する親がない場合、
     * IllegalStateExceptionがスローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testResolveTypeVariable02() throws Exception {
        // 前処理
        Type type = getTypeVariable(
                AbstractList.class, GenericsUtil_ArrayListStub01.class, 0);

        List<ParameterizedType> list = new ArrayList<ParameterizedType>();
        try {
            //  テスト実施
            GenericsUtil.resolveTypeVariable(
                    type, list);
            fail("例外が発生しませんでした。");
        } catch (IllegalStateException e) {
            //  判定
            assertEquals(
                    "Concrete type of Type(" + type
                    + ") was not found in ancestorList("
                    + list + ")",
                    e.getMessage());
            assertEquals(IllegalStateException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testResolveTypeVariable03()
     * <br><br>
     *
     *  (異常系)
     * <br>
     * 観点：C, G
     * <br><br>
     * 入力値：(引数) type:GenericArrayTypeインスタンス<br>
     *         (引数) ancestorTypeList:空のArrayList<ParameterizedType><br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     *
     * <br>
     * isClassOrTypeVariableでIllegalStateExceptionがスローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testResolveTypeVariable03() throws Exception {
        try {
            //  テスト実施
            GenericsUtil.resolveTypeVariable(
                    new GenericsUtil_GenericArrayTypeStub03(),
                    new ArrayList<ParameterizedType>());
            fail("例外が発生しませんでした。");
        } catch (IllegalStateException e) {
            //  判定
            assertNotNull(e);
            assertEquals(IllegalStateException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testResolveTypeVariable04()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：C, G
     * <br><br>
     * 入力値：(引数) type:Methodで宣言された型変数<br>
     *         (引数) ancestorTypeList:ArrayList<ParameterizedType><br>
     *                内容は、ArrayList継承クラス<String>の
     *                親クラスのParameterizedTypeの全て<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     *                    メッセージ："TypeVariable("<br>
     *                    + targetType.getName()
     *                    + " is not declared at Class "<br>
     *                    + "(ie. is declared at Method or Constructor)")<br>
     *
     * <br>
     * typeがクラスで宣言されていない場合、IllegalStateExceptionが
     * スローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveTypeVariable04() throws Exception {
        // 前処理
        Method emptyListMethod =
            Collections.class.getMethod("emptyList", new Class[0]);
        TypeVariable type = emptyListMethod.getTypeParameters()[0];

        List<ParameterizedType> list = getParameterizedClassList(
                List.class, GenericsUtil_ArrayListStub01.class);

        try {
            //  テスト実施
            GenericsUtil.resolveTypeVariable(type, list);
            fail("例外が発生しませんでした。");
        } catch (IllegalStateException e) {
            //  判定
            assertEquals("TypeVariable("
                    + type.getName()
                    + " is not declared at Class "
                    + "(ie. is declared at Method or Constructor)",
                e.getMessage());
            assertEquals(IllegalStateException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testResolveTypeVariable05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) type:ArrayList継承クラス<String>のList<E>について型変数E<br>
     *         (引数) ancestorTypeList:ArrayList<ParameterizedType><br>
     *                内容は、ArrayList継承クラス<String>の親クラスの
     *                ParameterizedTypeの全てに加え、
     *                ComparatorのParameterizedType<br>
     *
     * <br>
     * 期待値：(戻り値) Class:Map.class<br>
     *
     * <br>
     * 型変数宣言が1つの場合、指定した型変数の実際の型が返却されることのテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveTypeVariable05() throws Exception {
        // 前処理
        Type type = getTypeVariable(
                AbstractList.class, GenericsUtil_ArrayListStub01.class, 0);

        List<ParameterizedType> list = getParameterizedClassList(
                List.class, GenericsUtil_ArrayListStub01.class);
        list.add((ParameterizedType) Integer.class.getGenericInterfaces()[0]);

        //  テスト実施
        Class result = GenericsUtil.resolveTypeVariable(type, list);

        //  判定
        assertEquals(Map.class, result);
    }

    /**
     * testResolveTypeVariable06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) type:HashMap継承クラス<String, Integer>(
     *              KとVの順番を入れ替えること)の
     *              Map<K, V>について型変数V<br>
     *         (引数) ancestorTypeList:ArrayList<ParameterizedType><br>
     *                内容は、HashMap継承クラス<String, Integer>の
     *                親クラスのParameterizedTypeの全て。<br>
     *
     * <br>
     * 期待値：(戻り値) Class:Integer.class<br>
     *
     * <br>
     * 型変数宣言が複数の場合、指定した型変数の実際の型が返却されることのテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveTypeVariable06() throws Exception {
        // 前処理
        Type type = getTypeVariable(
                HashMap.class, GenericsUtil_HashMapStub02.class, 1);

        List<ParameterizedType> list = getParameterizedClassList(
                Map.class, GenericsUtil_HashMapStub02.class);

        //  テスト実施
        Class result = GenericsUtil.resolveTypeVariable(type, list);

        //  判定
        assertEquals(Integer.class, result);
    }

    /**
     * testisNotTypeVariable01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) type:String.class<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * typeがClassである場合に、trueが返却されることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testsNotTypeVariable01() throws Exception {
        //  テスト実施
        boolean result = GenericsUtil.isNotTypeVariable(String.class);

        //  判定
        assertTrue(result);
    }

    /**
     * testIsNotTypeVariable02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) type:ArrayList継承クラスについて、
     * ArrayList<E>のE<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * typeがTypeValiableである場合に、falseが返却されることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNotTypeVariable02() throws Exception {
        //  前処理
        Type type = getTypeVariable(
                AbstractList.class, GenericsUtil_ArrayListStub01.class, 0);

        //  テスト実施
        boolean result = GenericsUtil.isNotTypeVariable(type);

        //  判定
        assertFalse(result);
    }

    /**
     * testIsNotTypeVariable03()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：C, G
     * <br><br>
     * 入力値：(引数) type:WildCardTypeインスタンス<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     *                    メッセージ："Type("<br>
     *                    + type + ") is not instance of "<br>
     *                    + TypeVariable.class.getName() + " nor "<br>
     *                    + Class.class.getName()<br>
     *
     * <br>
     * typeがClassでもTypeValiableでもない場合、IllegalStateExceptionが
     * スローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNotTypeVariable03() throws Exception {

        // 前処理
        Type type = new GenericsUtil_WildCardTypeStub01();

        try {
            //  テスト実施
            GenericsUtil.isNotTypeVariable(type);
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            //  判定
            assertEquals("Type("
                    + type + ") is not instance of "
                    + TypeVariable.class.getName() + ", "
                    + ParameterizedType.class.getName() + ", "
                    + GenericArrayType.class.getName() + " nor "
                    + Class.class.getName(),
                e.getMessage());
            assertEquals(IllegalStateException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testIsNotTypeVariable04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) type:ArrayList継承クラスについて、
     * ArrayList<E>のParameterizedType()<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * typeがParameterizedTypeである場合に、trueが返却されることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNotTypeVariable04() throws Exception {
        //  前処理
        Type type = GenericsUtil_ArrayListStub01.class.getGenericSuperclass();

        //  テスト実施
        boolean result = GenericsUtil.isNotTypeVariable(type);

        //  判定
        assertTrue(result);
    }

    /**
     * testIsNotTypeVariable05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) type: GenericArrayTypeインスタンス<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * typeがGenericArrayTypeである場合に、trueが返却されることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNotTypeVariable05() throws Exception {
        //  前処理
        Type type = new GenericsUtil_GenericArrayTypeStub01();

        //  テスト実施
        boolean result = GenericsUtil.isNotTypeVariable(type);

        //  判定
        assertTrue(result);
    }

    /**
     * <code>startClass</code>から<code>endClass</code>までの
     * 継承関係にある、<code>ParameterizedType</code>をリストにして
     * 返却する。
     *
     * @param <T> クラス。
     * @param endClass 終端クラス。
     * @param startClass 開始クラス。
     * @return <code>ParameterizedType</code>のリスト。
     */
    @SuppressWarnings("unchecked")
    private <T> List<ParameterizedType> getParameterizedClassList(
            Class<T> endClass, Class<? extends T> startClass) {
        List<ParameterizedType> result = new ArrayList<ParameterizedType>();
        Class clazz = startClass;

        while (clazz != null) {
            Type type = clazz.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                result.add((ParameterizedType) type);
            }
            if (clazz.equals(endClass)) {
                break;
            }
           clazz = clazz.getSuperclass();
        }
        return result;
    }

    /**
     * インタフェースクラスの型変数を取得する。
     *
     * @param <T> クラス。
     * @param firstImplementation インタフェースを最初に実装したクラス。
     * @param implementClass 取得もとの実装クラス。
     * @param index 何番目に型変数が宣言されたか。
     * @return 型変数。
     */
    @SuppressWarnings({ "null", "unchecked" })
    private <T> Type getTypeVariable(Class<T> firstImplementation,
            Class<? extends T> implementClass, int index) {
        Class clazz = implementClass;
        while (clazz != null && !clazz.equals(firstImplementation)) {
            clazz = clazz.getSuperclass();
        }
        if (clazz == null) {
            fail(firstImplementation.getName() + "が取得できません。");
        }
        ParameterizedType parameterizedClass =
            (ParameterizedType) clazz.getGenericInterfaces()[0];
        Type type = parameterizedClass.getActualTypeArguments()[index];
        return type;
    }

    /**
     * 型変数を取得する。
     * @param clazz 取得もとの実装クラス。
     * @param index 何番目に型変数が宣言されたか。
     *
     * @return 型変数。
     */
    private <T> Type getTypeArgument(Class<T> clazz, int index) {
        Type type = clazz.getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            fail("ParameterizedTypeを取得できません。");
        }
        Type argument = ((ParameterizedType) type).getActualTypeArguments()[index];
        return argument;
    }

    /**
     * testGetRawClass01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) type:String.class<br>
     *
     * <br>
     * 期待値：(戻り値) Class:String.class<br>
     *
     * <br>
     * typeがClassである場合に、そのまま返却されることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testGetRawClass01() throws Exception {
        //  前処理
        Type type = String.class;

        //  テスト実施
        Class result = GenericsUtil.getRawClass(type);

        //  判定
        assertEquals(String.class, result);
    }

    /**
     * testGetRawClass02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) type:ArrayList継承クラスについて、ArrayList<E>のE<br>
     *                (EはMap<String, Object>を表すParameterizedType)<br>
     *
     * <br>
     * 期待値：(戻り値) Class:Map.class<br>
     *
     * <br>
     * typeがParameterizedTypeである場合に、その元となるクラスが返却されることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testGetRawClass02() throws Exception {
        //  前処理
        Type type = getTypeArgument(
                GenericsUtil_ArrayListStub01.class, 0);

        //  テスト実施
        Class result = GenericsUtil.getRawClass(type);

        //  判定
        assertEquals(Map.class, result);
    }

    /**
     * testGetRawClass03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) type:ArrayList継承クラスについて、ArrayList<E>のE<br>
     *                (EはString[]を表すGenericArrayType)<br>
     *
     * <br>
     * 期待値：(戻り値) Class:String[].class<br>
     *
     * <br>
     * typeがGenericArrayTypeである場合に、その元となる配列クラスが返却されることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testGetRawClass03() throws Exception {
        //  前処理
        Type type = getTypeArgument(
                GenericsUtil_ArrayListStub02.class, 0);

        //  テスト実施
        Class result = GenericsUtil.getRawClass(type);

        //  判定
        assertEquals(String[].class, result);
    }

    /**
     * testGetRawClass04()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：C, G
     * <br><br>
     * 入力値：(引数) type:WildCardTypeインスタンス<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     *                    メッセージ："Type("<br>
     *                    + type + ") is not instance of "<br>
     *                    + ParameterizedType.class.getName() + ", "<br>
     *                    + GenericArrayType.class.getName() + " nor "<br>
     *                    + Class.class.getName()<br>
     *
     * <br>
     * typeがClassでもTypeValiableでもない場合、IllegalStateExceptionがスローされることを確認するテスト。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetRawClass04() throws Exception {
        // 前処理
        Type type = new GenericsUtil_WildCardTypeStub01();

        try {
            //  テスト実施
            GenericsUtil.getRawClass(type);
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            //  判定
            assertEquals("Type("
                    + type + ") is not instance of "
                    + ParameterizedType.class.getName() + ", "
                    + GenericArrayType.class.getName() + " nor "
                    + Class.class.getName(),
                e.getMessage());
            assertEquals(IllegalStateException.class.getName(),
                    e.getClass().getName());
        }
    }

    /**
     * testGetRawClass05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) type:GenericArrayTypeインスタンス
     *        （getGenericComponentTypeの戻り値:GenericArrayTypeインスタンス
     *            (getGenericComponentTypeの戻り値:String.class)）<br>
     *
     * <br>
     * 期待値：(戻り値) Class:String[].class<br>
     *
     * <br>
     * typeがGenericArrayTypeでかつ、getGenericComponentClassの戻り値が
     * GenericArrayTypeである場合に、正常に動作しないこと
     * （そのgetGenericComponentClassの2元配列が取得される）を確認するテスト。
     * ※現状のJDKでは、このような事態は発生しない。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testGetRawClass05() throws Exception {
        //  前処理
        Type type = new GenericsUtil_GenericArrayTypeStub01();

        //  テスト実施
        Class result = GenericsUtil.getRawClass(type);

        //  判定
        assertEquals(String[][].class, result);
    }
}
