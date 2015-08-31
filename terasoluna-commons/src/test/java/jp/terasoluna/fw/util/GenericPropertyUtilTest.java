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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.terasoluna.utlib.UTUtil;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;
import static uk.org.lidalia.slf4jtest.LoggingEvent.trace;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.GenericPropertyUtil} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> <code>JavaBean</code>のプロパティの<code>Generics</code>を扱うためのユーティリティクラス。
 * <p>
 * @see jp.terasoluna.fw.util.GenericPropertyUtil
 */
public class GenericPropertyUtilTest {

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            GenericPropertyUtil.class);

    /**
     * PropertyUtilsBean。
     */
    private PropertyUtilsBean defaultPropertyUtilsBean = null;

    /**
     * 初期化処理を行う。
     */
    @Before
    public void setUp() {
        BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
        defaultPropertyUtilsBean = beanUtilsBean.getPropertyUtils();
    }

    /**
     * 終了処理を行う。
     * @throws Exception このメソッドで発生した例外
     */
    @After
    public void tearDown() throws Exception {
        BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
        UTUtil.setPrivateField(beanUtilsBean, "propertyUtilsBean",
                defaultPropertyUtilsBean);
        logger.clear();
    }

    /**
     * testResolveCollectionType01()
     * <br><br>
     * (異常系)<br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * <br>
     * 引数beanがnullの場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveCollectionType01() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveCollectionType(null, null);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveCollectionType02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:Objectインスタンス<br>
     * (引数) name:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * <br>
     * 引数nameがnullの場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveCollectionType02() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveCollectionType(new Object(), null);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveCollectionType03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:ObjectのClassインスタンス<br>
     * (引数) name:""（空文字）<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * <br>
     * 引数nameが空文字の場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveCollectionType03() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveCollectionType(new Object(), "");
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveCollectionType04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:ObjectのClassインスタンス<br>
     * (引数) name:"   "（空白文字列）<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * <br>
     * 引数nameが空白文字列の場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveCollectionType04() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveCollectionType(new Object(), "   ");
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveCollectionType05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドList<String> list0を持つが、そのgetterを持たないクラス。<br>
     * (引数) name:"list0"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * <br>
     * 対応するメソッドが存在しない場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveCollectionType05() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveCollectionType(
                    new GenericPropertyUtil_Stub01(), "list0");
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveCollectionType06() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) bean:フィールドList<String> list1と、そのgetterを持つクラス。<br>
     * (引数) name:"list1"<br>
     * <br>
     * 期待値：(戻り値) Class:String.class<br>
     * <br>
     * 要素型が取得できることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveCollectionType06() throws Exception {

        // テスト実施
        Class actual = GenericPropertyUtil.resolveCollectionType(
                new GenericPropertyUtil_Stub01(), "list1");

        // 判定
        assertEquals(String.class, actual);
    }

    /**
     * testResolveCollectionType07() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) bean:フィールドList<Map<String, Object>> list2と、そのgetterを持つクラス。<br>
     * (引数) name:"list2"<br>
     * <br>
     * 期待値：(戻り値) Class:Map.class<br>
     * <br>
     * パラメータ付の型が取得できることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveCollectionType07() throws Exception {
        // テスト実施
        Class actual = GenericPropertyUtil.resolveCollectionType(
                new GenericPropertyUtil_Stub01(), "list2");

        // 判定
        assertEquals(Map.class, actual);
    }

    /**
     * testResolveCollectionType08() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) bean:フィールドList<String[]> list3と、そのgetterを持つクラス。<br>
     * (引数) name:"list3"<br>
     * <br>
     * 期待値：(戻り値) Class:String[].class<br>
     * <br>
     * 配列型が取得できることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveCollectionType08() throws Exception {
        // テスト実施
        Class actual = GenericPropertyUtil.resolveCollectionType(
                new GenericPropertyUtil_Stub01(), "list3");

        // 判定
        assertEquals(String[].class, actual);
    }

    /**
     * testResolveCollectionType09() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドint integerと、そのgetterを持つクラス。<br>
     * (引数) name:"integer"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     * <br>
     * 指定したプロパティがプリミティブ型の場合、IllegalStateExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveCollectionType09() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveCollectionType(
                    new GenericPropertyUtil_Stub01(), "integer");
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            assertEquals(IllegalStateException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveCollectionType10() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドObject objectと、そのgetterを持つクラス。<br>
     * (引数) name:"object"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     * <br>
     * 指定したプロパティがCollection以外の型の場合、IllegalStateExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveCollectionType10() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveCollectionType(
                    new GenericPropertyUtil_Stub01(), "object");
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            assertEquals(IllegalStateException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveCollectionType11() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドList<? extends String> list5とそのgetterを持つクラス。<br>
     * (引数) name:"list5"<br>
     * <br>
     * 期待値：(戻り値) Class:Object.class<br>
     * <br>
     * 型パラメータ指定がワイルドカードの場合、Objectが返却されることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveCollectionType11() throws Exception {
        // テスト実施
        Class actual = GenericPropertyUtil.resolveCollectionType(
                new GenericPropertyUtil_Stub01(), "list5");
        // 判定
        assertEquals(Object.class, actual);
    }

    /**
     * testResolveTypeObjectStringClassint01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:null<br>
     * (引数) index:0<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："Argument 'bean' ("<br>
     * + Object.class.getName() + " is null"<br>
     * <br>
     * 引数beanがnullの場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveTypeObjectStringClassint01() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(null, (String) null, null, 0);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            String message = "Argument 'bean' (" + Object.class.getName()
                    + " is null";
            assertEquals(message, e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeObjectStringClassint02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:Objectインスタンス<br>
     * (引数) name:null<br>
     * (引数) index:0<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："Argument 'name' ("<br>
     * + String.class.getName() + " is empty"<br>
     * <br>
     * 引数nameがnullの場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveTypeObjectStringClassint02() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(new Object(), (String) null, null,
                    0);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            String message = "Argument 'name' (" + String.class.getName()
                    + " is empty";
            assertEquals(message, e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeObjectStringClassint03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:Objectインスタンス<br>
     * (引数) name:""（空文字）<br>
     * (引数) index:0<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："Argument 'name' ("<br>
     * + String.class.getName() + " is empty"<br>
     * <br>
     * 引数nameが空文字の場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveTypeObjectStringClassint03() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(new Object(), "", null, 0);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            String message = "Argument 'name' (" + String.class.getName()
                    + " is empty";
            assertEquals(message, e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeObjectStringClassint04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:Objectインスタンス<br>
     * (引数) name:"   "（空白文字列）<br>
     * (引数) index:0<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："Argument 'name' ("<br>
     * + String.class.getName() + " is empty"<br>
     * <br>
     * 引数nameが空白文字列の場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveTypeObjectStringClassint04() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(new Object(), "   ", null, 0);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            String message = "Argument 'name' (" + String.class.getName()
                    + " is empty";
            assertEquals(message, e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeObjectStringClassint05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドMap<String, Object> map0を持つが、そのgetterを持たないクラス。<br>
     * (引数) name:"map0"<br>
     * (引数) genericClass:Map.class<br>
     * (引数) index:0<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * <br>
     * 対応するメソッドが存在しない場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveTypeObjectStringClassint05() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(new GenericPropertyUtil_Stub01(),
                    "map0", Map.class, 0);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeObjectStringClassint06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドMap<String, Object> map1とそのgetterを持つクラス。<br>
     * (引数) name:"map1"<br>
     * (引数) genericClass:Map.class<br>
     * (引数) index:-1<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * <br>
     * 引数indexが負の数である場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveTypeObjectStringClassint06() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(new GenericPropertyUtil_Stub01(),
                    "map1", Map.class, -1);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeObjectStringClassint07() <br>
     * <br>
     * （正常系） <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) bean:フィールドMap<String, Object> map1とそのgetterを持つクラス。<br>
     * (引数) name:"map1"<br>
     * (引数) genericClass:Map.class<br>
     * (引数) index:0<br>
     * <br>
     * 期待値：(戻り値) Class:String.class<br>
     * <br>
     * 引数indexが型パラメータ数の範囲内である場合に型が取得できることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveTypeObjectStringClassint07() throws Exception {

        // テスト実施
        Class actual = GenericPropertyUtil.resolveType(
                new GenericPropertyUtil_Stub01(), "map1", Map.class, 0);

        // 判定
        assertEquals(String.class, actual);
    }

    /**
     * testResolveTypeObjectStringClassint08() <br>
     * <br>
     * （正常系） <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) bean:フィールドMap<String, Object> map1とそのgetterを持つクラス。<br>
     * (引数) name:"map1"<br>
     * (引数) genericClass:Map.class<br>
     * (引数) index:1<br>
     * <br>
     * 期待値：(戻り値) Class:Object.class<br>
     * <br>
     * 引数indexが型パラメータ数の範囲内である場合に型が取得できることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveTypeObjectStringClassint08() throws Exception {
        // テスト実施
        Class actual = GenericPropertyUtil.resolveType(
                new GenericPropertyUtil_Stub01(), "map1", Map.class, 0);

        // 判定
        assertEquals(String.class, actual);
    }

    /**
     * testResolveTypeObjectStringClassint09() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドMap<String, Object> map1とそのgetterを持つクラス。<br>
     * (引数) name:"map1"<br>
     * (引数) genericClass:Map.class<br>
     * (引数) index:2<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * <br>
     * 引数indexが型パラメータ数の範囲外である場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveTypeObjectStringClassint09() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(new GenericPropertyUtil_Stub01(),
                    "map1", Map.class, 2);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeObjectStringClassint10() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドMap<String, Object> map1とそのgetterを持つクラス。<br>
     * (引数) name:"map1"<br>
     * (引数) genericClass:null<br>
     * (引数) index:1<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * <br>
     * 引数genericClassがnullである場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveTypeObjectStringClassint10() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(new GenericPropertyUtil_Stub01(),
                    "map1", null, 1);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeObjectStringClassint11() <br>
     * <br>
     * （正常系） <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) bean:フィールドMap<String[], List<String>> map2とそのgetterを持つクラス。<br>
     * (引数) name:"map2"<br>
     * (引数) genericClass:Map.class<br>
     * (引数) index:0<br>
     * <br>
     * 期待値：(戻り値) Class:String[].class<br>
     * <br>
     * 配列型が取得できることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveTypeObjectStringClassint11() throws Exception {

        // テスト実施
        Class actual = GenericPropertyUtil.resolveType(
                new GenericPropertyUtil_Stub01(), "map2", Map.class, 0);

        // 判定
        assertEquals(String[].class, actual);
    }

    /**
     * testResolveTypeObjectStringClassint12() <br>
     * <br>
     * （正常系） <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) bean:フィールドMap<String[], List<String>> map2とそのgetterを持つクラス。<br>
     * (引数) name:"map2"<br>
     * (引数) genericClass:Map.class<br>
     * (引数) index:1<br>
     * <br>
     * 期待値：(戻り値) Class:List.class<br>
     * <br>
     * パラメータ付の型が取得できることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveTypeObjectStringClassint12() throws Exception {
        // テスト実施
        Class actual = GenericPropertyUtil.resolveType(
                new GenericPropertyUtil_Stub01(), "map2", Map.class, 1);

        // 判定
        assertEquals(List.class, actual);
    }

    /**
     * testResolveTypeObjectStringClassint13() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドMap<String[], List<String>> map2とそのgetterを持つクラス。<br>
     * (引数) name:"map2"<br>
     * (引数) genericClass:String.class<br>
     * (引数) index:1<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     * <br>
     * genericClassがフィールドの型と合致しない場合、 IllegalStateExceptionがスローされることを確認するテスト。。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveTypeObjectStringClassint13() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(new GenericPropertyUtil_Stub01(),
                    "map2", String.class, 1);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            assertEquals(IllegalStateException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeObjectStringClassint14() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドいint integerとそのgetterを持つクラス。<br>
     * (引数) name:"integer"<br>
     * (引数) genericClass:int.class<br>
     * (引数) index:1<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     * メッセージ："No parameterizedType was detected."<br>
     * <br>
     * 指定したプロパティがプリミティブ型の場合、 IllegalStateExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveTypeObjectStringClassint14() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(new GenericPropertyUtil_Stub01(),
                    "integer", int.class, 1);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            assertEquals("No parameterizedType was detected.", e.getMessage());
            assertEquals(IllegalStateException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeObjectStringClassint15() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドいObject objectとそのgetterを持つクラス。<br>
     * (引数) name:"object"<br>
     * (引数) genericClass:Object.class<br>
     * (引数) index:1<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     * メッセージ："No parameterizedType was detected."<br>
     * <br>
     * 指定したプロパティがオブジェクト型の場合、 IllegalStateExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveTypeObjectStringClassint15() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(new GenericPropertyUtil_Stub01(),
                    "object", Object.class, 1);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            assertEquals("No parameterizedType was detected.", e.getMessage());
            assertEquals(IllegalStateException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeObjectStringClassint16() <br>
     * <br>
     * （正常系） <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) bean:フィールドMap<?, ?> map3とそのgetterを持つクラス。<br>
     * (引数) name:"map3"<br>
     * (引数) genericClass:Map.class<br>
     * (引数) index:1<br>
     * <br>
     * 期待値：(戻り値) Class:Object.class<br>
     * <br>
     * 型パラメータ指定がワイルドカードの場合、Objectが返却されることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveTypeObjectStringClassint16() throws Exception {
        // テスト実施
        Class actual = GenericPropertyUtil.resolveType(
                new GenericPropertyUtil_Stub01(), "map3", Map.class, 0);

        // 判定
        assertEquals(Object.class, actual);
    }

    /**
     * testGetMethod01() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドString string、getter, setterを持たないクラス。<br>
     * (引数) name:"string"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ：beanの完全修飾クラス名 + " has no getter for property string"<br>
     * <br>
     * 対応するフィールドが存在しない場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetMethod01() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.getMethod(new GenericPropertyUtil_Stub02(),
                    "string");
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            String message = GenericPropertyUtil_Stub02.class.getName()
                    + " has no getter for property string";
            assertEquals(message, e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testGetMethod02() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドString string1を持つが、そのgetterを持たないクラス。<br>
     * (引数) name:"string1"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ：beanの完全修飾クラス名 + " has no getter for property string1"<br>
     * <br>
     * 対応するメソッドが存在しない場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetMethod02() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.getMethod(new GenericPropertyUtil_Stub02(),
                    "string1");
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            String message = GenericPropertyUtil_Stub02.class.getName()
                    + " has no getter for property string1";
            assertEquals(message, e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testGetMethod03() <br>
     * <br>
     * （正常系） <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) bean:フィールドString string2とそのgetterを持つクラス。<br>
     * (引数) name:"string2"<br>
     * <br>
     * 期待値：(戻り値) Method:beanのgetString2メソッド<br>
     * <br>
     * 対応するメソッドが返却されることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetMethod03() throws Exception {
        // テスト実施
        Method actual = GenericPropertyUtil.getMethod(
                new GenericPropertyUtil_Stub02(), "string2");

        // 判定
        Method expected = GenericPropertyUtil_Stub02.class.getDeclaredMethod(
                "getString2", new Class[0]);
        assertEquals(expected, actual);
    }

    /**
     * testGetMethod04() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドbean1を持つが、そのgetterを持たないクラス。<br>
     * (引数) name:"bean1.string"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："Failed to detect getter for "<br>
     * + beanの完全修飾クラス名 + "#bean1.string"<br>
     * ラップされた例外：NoSuchMethodException<br>
     * <br>
     * PropertyUtils#getPropertyDescriptorでNoSuchMethodExceptionがスローされる場合のテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetMethod04() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.getMethod(new GenericPropertyUtil_Stub02(),
                    "bean1.string");
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            String message = "Failed to detect getter for "
                    + GenericPropertyUtil_Stub02.class.getName()
                    + "#bean1.string";
            assertEquals(message, e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
            assertTrue(e.getCause() instanceof NoSuchMethodException);
        }
    }

    /**
     * testGetMethod05() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドbean2とそのgetterを持つクラス。<br>
     * (getterではRuntimeExceptionがスローされる)<br>
     * (引数) name:"bean2.string"<br>
     * (状態) PropertyUtils#getPropertyDescriptorの実行結果: InvocationTargetExceptionをスロー ※JavaBeanのgetterでRuntimeExceptionをスロー<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："Failed to detect getter for "<br>
     * + beanの完全修飾クラス名 + "#bean2.string"<br>
     * ラップされた例外：InvocationTargetException<br>
     * <br>
     * PropertyUtils#getPropertyDescriptorでInvocationTargetExceptionがスローされる場合のテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetMethod05() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.getMethod(new GenericPropertyUtil_Stub02(),
                    "bean2.string");
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            String message = "Failed to detect getter for "
                    + GenericPropertyUtil_Stub02.class.getName()
                    + "#bean2.string";
            assertEquals(message, e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
            assertTrue(e.getCause() instanceof InvocationTargetException);
        }
    }

    /**
     * testGetMethod06() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:フィールドString string2とそのgetterを持つクラス。<br>
     * (引数) name:"string2"<br>
     * (状態) PropertyUtils#getPropertyDescriptorの実行結果: IllegalAccessExceptionをスロー
     * ※PropertyUtilsBeanのスタブでIllegalAccessExceptionをスロー<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："Failed to detect getter for "<br>
     * + beanの完全修飾クラス名 + "#string2"<br>
     * ラップされた例外：IllegalAccessException<br>
     * <br>
     * PropertyUtils#getPropertyDescriptorでIllegalAccessExceptionがスローされる場合のテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetMethod06() throws Exception {
        // 前処理
        BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
        UTUtil.setPrivateField(beanUtilsBean, "propertyUtilsBean",
                new GenericPropertyUtil_PropertyUtilsBeanStub01());
        try {

            // テスト実施
            GenericPropertyUtil.getMethod(new GenericPropertyUtil_Stub02(),
                    "string2");
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            String message = "Failed to detect getter for "
                    + GenericPropertyUtil_Stub02.class.getName() + "#string2";
            assertEquals(message, e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
            assertTrue(e.getCause() instanceof IllegalAccessException);
        }
    }

    /**
     * testResolveTypeClassClassTypeint01() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) genericClass:null<br>
     * (引数) index:0<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："Argument 'genericsClass' ("<br>
     * + Class.class.getName() + ") is null"<br>
     * <br>
     * 引数genericClassがnullの場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveTypeClassClassTypeint01() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType((Class) null, (Class) null, null,
                    0);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            String message = "Argument 'genericsClass' (" + Class.class
                    .getName() + ") is null";
            assertEquals(message, e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeClassClassTypeint02() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) genericClass:List.class<br>
     * (引数) clazz:null<br>
     * (引数) index:0<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     * メッセージ：genericClass+ " is not assignable from " + clazz<br>
     * <br>
     * 引数clazzがnullの場合にIllegalStateExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveTypeClassClassTypeint02() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(List.class, (Class) null, null, 0);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            String message = List.class + " is not assignable from " + "null";
            assertEquals(message, e.getMessage());
            assertEquals(IllegalStateException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeClassClassTypeint03() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) genericClass:List.class<br>
     * (引数) clazz:Object.class<br>
     * (引数) index:0<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     * メッセージ：genericClass+ " is not assignable from " + clazz<br>
     * <br>
     * 引数clazzがgenericClassのサブクラスではない場合にIllegalStateExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveTypeClassClassTypeint03() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(List.class, Object.class, null, 0);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalStateException e) {
            String message = List.class + " is not assignable from "
                    + Object.class;
            assertEquals(message, e.getMessage());
            assertEquals(IllegalStateException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeClassClassTypeint04() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) genericClass:List.class<br>
     * (引数) clazz:ArrayList継承クラス(パラメータにStringを指定)<br>
     * (引数) type:null<br>
     * (引数) index:-1<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："Argument 'index'(-1) is out of bounds of"<br>
     * + " generics parameters")<br>
     * <br>
     * 引数indexが負の数である場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveTypeClassClassTypeint04() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(List.class,
                    GenericPropertyUtil_ArrayListStub01.class, null, -1);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            String message = "Argument 'index'(-1) is out of bounds of"
                    + " generics parameters";
            assertEquals(message, e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeClassClassTypeint05() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) genericClass:List.class<br>
     * (引数) clazz:ArrayList継承クラス(パラメータにStringを指定)<br>
     * (引数) type:null<br>
     * (引数) index:0<br>
     * <br>
     * 期待値：(戻り値) Class:String.class<br>
     * <br>
     * 引数indexが型パラメータ数の範囲内である場合に型が取得できることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveTypeClassClassTypeint05() throws Exception {
        // テスト実施
        Class actual = GenericPropertyUtil.resolveType(List.class,
                GenericPropertyUtil_ArrayListStub01.class, null, 0);
        // 判定
        assertEquals(String.class, actual);
    }

    /**
     * testResolveTypeClassClassTypeint06() <br>
     * <br>
     * （異常系） <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) genericClass:List.class<br>
     * (引数) clazz:ArrayList継承クラス(パラメータにStringを指定)<br>
     * (引数) type:null<br>
     * (引数) index:1<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："Argument 'index'(1) is out of bounds of"<br>
     * + " generics parameters")<br>
     * <br>
     * 引数indexが型パラメータ数の範囲外である場合にIllegalArgumentExceptionがスローされることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testResolveTypeClassClassTypeint06() throws Exception {
        try {
            // テスト実施
            GenericPropertyUtil.resolveType(List.class,
                    GenericPropertyUtil_ArrayListStub01.class, null, 1);
            // 判定
            fail("例外がスローされませんでした。");
        } catch (IllegalArgumentException e) {
            String message = "Argument 'index'(1) is out of bounds of"
                    + " generics parameters";
            assertEquals(message, e.getMessage());
            assertEquals(IllegalArgumentException.class.getName(), e.getClass()
                    .getName());
        }
    }

    /**
     * testResolveTypeClassClassTypeint07() <br>
     * <br>
     * （正常系） <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) genericClass:List.class<br>
     * (引数) clazz:ArrayList.class<br>
     * (引数) type:List.class<br>
     * (引数) index:0<br>
     * <br>
     * 期待値：(戻り値) Class:Object.class<br>
     * (状態変化) ログ出力:<br>
     * ログレベル：TRACE メッセージ："Concrete type of Type(E) was not found in ancestorList([java.util.AbstractList<E>, java.util.List
     * <E>])"<br>
     * <br>
     * 型が特定できない場合にObjectが返却されることを確認するテスト。 （経路確認のために特別にトレースログをチェックする） <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveTypeClassClassTypeint07() throws Exception {
        // テスト実施
        Class actual = GenericPropertyUtil.resolveType(List.class,
                ArrayList.class, List.class, 0);
        // 判定
        assertEquals(Object.class, actual);
        assertThat(logger.getLoggingEvents(), is(asList(trace(
                "Concrete type of Type(E) was "
                        + "not found in ancestorList([java.util.AbstractList<E>, "
                        + "java.util.List<E>])"))));
    }

    /**
     * testResolveTypeClassClassTypeint08() <br>
     * <br>
     * （正常系） <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) genericClass:List.class<br>
     * (引数) clazz:ArrayList.class<br>
     * (引数) type:List<String>を表すParameterizedType<br>
     * (引数) index:0<br>
     * <br>
     * 期待値：(戻り値) Class:String.class<br>
     * (状態変化) ログ出力:<br>
     * ログレベル：TRACE メッセージ："Argument 'genericClass'(java.util.List) does not declare type parameter"<br>
     * <br>
     * typeで型が特定される場合に、型パラメータが返却されることを確認するテスト。 （経路確認のために特別にトレースログをチェックする） <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveTypeClassClassTypeint08() throws Exception {
        // 前処理
        Method method = GenericPropertyUtil_Stub01.class.getMethod("getList1",
                new Class[0]);
        Type type = method.getGenericReturnType();

        // テスト実施
        Class actual = GenericPropertyUtil.resolveType(List.class, List.class,
                type, 0);
        // 判定
        assertEquals(String.class, actual);
        assertThat(logger.getLoggingEvents(), is(asList(trace("Argument "
                + "'genericClass'(java.util.List) does not "
                + "declare type parameter"))));
    }
}
