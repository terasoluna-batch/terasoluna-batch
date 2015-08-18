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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.AlgorithmParameterGenerator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import jp.terasoluna.utlib.UTUtil;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.client.util.ConvertUtil} クラスのテスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * 型変換を行うためのユーティリティクラス。
 * <p>
 * 
 * @see jp.terasoluna.fw.client.util.ConvertUtil
 */
public class ConvertUtilTest {
    
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
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @After
    public void tearDown() throws Exception {
        BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
        UTUtil.setPrivateField(beanUtilsBean, "propertyUtilsBean",
                defaultPropertyUtilsBean);
    }

    /**
     * testToArray01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) obj:null<br>
     *         
     * <br>
     * 期待値：(戻り値) Object[]:要素を持たないObject[] (要素数が0であることを確認)<br>
     *         
     * <br>
     * 引数objがnullだった場合、要素を持たないObject[]が返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToArray01() throws Exception {
        // テスト実施
        Object[] result = ConvertUtil.toArray(null); 
        
        // 判定
        assertEquals(0, result.length);
    }

    /**
     * testToArray02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) obj:""(空文字)<br>
     *         
     * <br>
     * 期待値：(戻り値) Object[]:１要素を持つObject[] (要素数が１であることを確認)<br>
     *                   *要素0:""(空文字)<br>
     *         
     * <br>
     * 引数objが""(空文字)だった場合、１要素を持つObject[]が返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToArray02() throws Exception {
        // テスト実施
        Object[] result = ConvertUtil.toArray(""); 
        
        // 判定
        assertEquals(1, result.length);
        assertEquals("", result[0]);
    }

    /**
     * testToArray03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) obj:"  "(空白文字列)<br>
     *         
     * <br>
     * 期待値：(戻り値) Object[]:１要素を持つObject[] (要素数が１であることを確認)<br>
     *                   *要素0:"  "(空白文字列)<br>
     *         
     * <br>
     * 引数objが"  "(空白文字列)だった場合、１要素を持つObject[]が返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToArray03() throws Exception {
        // テスト実施
        Object[] result = ConvertUtil.toArray("  "); 
        
        // 判定
        assertEquals(1, result.length);
        assertEquals("  ", result[0]);
    }

    /**
     * testToArray04()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) obj:"array"<br>
     *         
     * <br>
     * 期待値：(戻り値) Object[]:１要素を持つObject[] (要素数が１であることを確認)<br>
     *                   *要素0:"array"<br>
     *         
     * <br>
     * 引数objが通常の文字列だった場合、１要素を持つObject[]が返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToArray04() throws Exception {
        // テスト実施
        Object[] result = ConvertUtil.toArray("array"); 
        
        // 判定
        assertEquals(1, result.length);
        assertEquals("array", result[0]);
    }

    /**
     * testToArray05()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D
     * <br><br>
     * 入力値：(引数) obj:要素を持たないObject[]<br>
     *         
     * <br>
     * 期待値：(戻り値) Object[]:要素を持たないObject[] (要素数が０であることを確認)<br>
     *         
     * <br>
     * 引数objが要素を持たない配列だった場合、要素を持たないObject[]が返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToArray05() throws Exception {
        // 前処理
        Object obj = new Object[0];

        // テスト実施
        Object[] result = ConvertUtil.toArray(obj); 
        
        // 判定
        assertEquals(0, result.length);
    }

    /**
     * testToArray06()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D
     * <br><br>
     * 入力値：(引数) obj:１要素を保持するString[]<br>
     *                 *要素０:"array"<br>
     *         
     * <br>
     * 期待値：(戻り値) Object[]:１要素を持つObject[] (要素数が１であることを確認)<br>
     *                   *要素0:"array"<br>
     *         
     * <br>
     * 引数objが１要素を保持する配列だった場合、１要素を保持するObject[]が返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToArray06() throws Exception {
        // 前処理
        String[] obj = {"array"};

        // テスト実施
        Object[] result = ConvertUtil.toArray(obj); 
        
        // 判定
        assertEquals(1, result.length);
        assertEquals("array", result[0]);
    }

    /**
     * testToArray07()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D
     * <br><br>
     * 入力値：(引数) obj:３要素を保持するObject[]<br>
     *                 *要素０:"array"<br>
     *                 *要素１:1<br>
     *                 *要素２:Mapインスタンス (key="foo" value="something")<br>
     *         
     * <br>
     * 期待値：(戻り値) Object[]:３要素を持つObject[] (要素数が３であることを確認)<br>
     *                   *要素0:"array"<br>
     *                   *要素1:1<br>
     *                   *要素2:Mapインスタンス(key="foo" value="something")<br>
     *         
     * <br>
     * 引数objが３要素を保持する配列だった場合、３要素を保持するObject[]が返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testToArray07() throws Exception {
        // 前処理
        Object[] obj = new Object[3];
        obj[0] = "array";
        obj[1] = 1;
        Map map = new HashMap();
        map.put("foo", "something");
        obj[2] = map;

        // テスト実施
        Object[] result = ConvertUtil.toArray(obj); 
        
        // 判定
        assertEquals(3, result.length);
        assertEquals("array", result[0]);
        assertEquals(1, result[1]);
        assertEquals(map, result[2]);
        Map mapResult = (Map) result[2];
        assertEquals("something", mapResult.get("foo"));
    }

    /**
     * testToArray08()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D
     * <br><br>
     * 入力値：(引数) obj:要素を持たないCollection<br>
     *         
     * <br>
     * 期待値：(戻り値) Object[]:要素を持たないObject[] (要素数が０であることを確認)<br>
     *         
     * <br>
     * 引数objが要素を持たないCollectionだった場合、要素を持たないObject[]が返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToArray08() throws Exception {
        // 前処理
        Collection obj = new Vector();

        // テスト実施
        Object[] result = ConvertUtil.toArray(obj); 
        
        // 判定
        assertEquals(0, result.length);
    }

    /**
     * testToArray09()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D
     * <br><br>
     * 入力値：(引数) obj:１要素を保持するCollection<br>
     *                 *要素0:"collection"<br>
     *         
     * <br>
     * 期待値：(戻り値) Object[]:１要素を持つObject[] (要素数が１であることを確認)<br>
     *                   *要素0:"collection"<br>
     *         
     * <br>
     * 引数objが１要素を保持するCollectionだった場合、１要素を保持するObject[]が返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testToArray09() throws Exception {
        // 前処理
        Collection obj = new Vector();
        obj.add("collection");

        // テスト実施
        Object[] result = ConvertUtil.toArray(obj); 
        
        // 判定
        assertEquals(1, result.length);
        assertEquals("collection", result[0]);
    }

    /**
     * testToArray10()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D
     * <br><br>
     * 入力値：(引数) obj:３要素を保持するCollection<br>
     *                 *要素0:"collection"<br>
     *                 *要素1:1<br>
     *                 *要素2:Mapインスタンス(key="key" value="something")<br>
     *         
     * <br>
     * 期待値：(戻り値) Object[]:３要素を持つObject[] (要素数が３であることを確認)<br>
     *                   *要素0:"collection"<br>
     *                   *要素1:1<br>
     *                   *要素2:Mapインスタンス(key="foo" value="something")<br>
     *         
     * <br>
     * 引数objが３要素を保持するcollectionだった場合、３要素を保持するObject[]が返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testToArray10() throws Exception {
        // 前処理
        Collection obj = new Vector();
        obj.add("collection");
        obj.add(1);
        Map map = new HashMap();
        map.put("foo", "something");
        obj.add(map);

        // テスト実施
        Object[] result = ConvertUtil.toArray(obj); 
        
        // 判定
        assertEquals(3, result.length);
        assertEquals("collection", result[0]);
        assertEquals(1, result[1]);
        Map mapResult = (Map) result[2];
        assertEquals("something", mapResult.get("foo"));
    }

    /**
     * testToList01()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：C,G
     * <br><br>
     * 入力値：(引数) obj:null<br>
     *         (引数) elementClass:null<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    例外のメッセージ:
     *                    "Argument 'elementClass' (" + Class.class.getName() 
     *                    　+ ") is null"<br>
     *         
     * <br>
     * 引数elementClassがnullだった場合、IllegalArgumentExceptionがスローされることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToList01() throws Exception {
        // テスト実施
        try {
            ConvertUtil.toList(null, null);
            fail("IllegalArgumentExceptionがスローされませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
            assertEquals("Argument 'elementClass' (" + Class.class.getName()
                    + ") is null",
                    e.getMessage());
        }

    }

    /**
     * testToList02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) obj:null<br>
     *         (引数) elementClass:Object.class<br>
     *         
     * <br>
     * 期待値：(戻り値) List:要素を持たないList<Object> (要素数が０であることを確認)<br>
     *         
     * <br>
     * 引数objがnullだった場合、elementsClassで指定した型のListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToList02() throws Exception {
        // テスト実施
        List<Object> result = ConvertUtil.toList(null, Object.class);

        // 判定
        assertEquals(0, result.size());
    }

    /**
     * testToList03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) obj:""(空文字)<br>
     *         (引数) elementClass:Object.class<br>
     *         
     * <br>
     * 期待値：(戻り値) List:１要素を持つList<Object> (要素数が１であることを確認)<br>
     *                   *要素0:""(空文字)<br>
     *         
     * <br>
     * 引数objが""(空文字)だった場合、elementsClassで指定した型のListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToList03() throws Exception {
        // テスト実施
        List<Object> result = ConvertUtil.toList("", Object.class);

        // 判定
        assertEquals(1, result.size());
        assertEquals("", result.get(0));
        
    }

    /**
     * testToList04()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) obj:"  "(空白文字列)<br>
     *         (引数) elementClass:Object.class<br>
     *         
     * <br>
     * 期待値：(戻り値) List:１要素を持つList<Object> (要素数が１であることを確認)<br>
     *                   *要素0:"  "(空白文字列)<br>
     *         
     * <br>
     * 引数objが""(空文字)だった場合、elementsClassで指定した型のListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToList04() throws Exception {
        // テスト実施
        List<Object> result = ConvertUtil.toList("  ", Object.class);

        // 判定
        assertEquals(1, result.size());
        assertEquals("  ", result.get(0));
    }

    /**
     * testToList05()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) obj:"list"<br>
     *         (引数) elementClass:Object.class<br>
     *         
     * <br>
     * 期待値：(戻り値) List:１要素を持つList<Object> (要素数が１であることを確認)<br>
     *                   *要素0:"list"<br>
     *         
     * <br>
     * 引数objが通常文字列だった場合、elementsClassで指定した型のListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToList05() throws Exception {
        // テスト実施
        List<Object> result = ConvertUtil.toList("list", Object.class);

        // 判定
        assertEquals(1, result.size());
        assertEquals("list", result.get(0));
    }

    /**
     * testToList06()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) obj:"list"<br>
     *         (引数) elementClass:Thread.class<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    例外のメッセージ:
     *                    "Unable to cast '" + 引数objの完全修飾クラス名 
     *                    　+ "' to '" + 引数elementClassの完全修飾クラス名 + "'"<br>
     *                    ラップされた例外:ClassCastException<br>
     *                    ラップされた例外のメッセージ:"Unable to cast '" 
     *                    　+ 引数objの完全修飾クラス名 + "' to '" 
     *                    + 引数elementClassの完全修飾クラス名 + "'"<br>
     *         
     * <br>
     * 引数objの型がelementClassで指定した型と同一、もしくはサブクラスでは
     * なかった場合、IllegalArgumentExceptionがスローされることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testToList06() throws Exception {
        // テスト実施
        try {
            ConvertUtil.toList("list", Thread.class);
            fail("IllegalArgumentExceptionがスローされませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
            assertEquals("Unable to cast '" + "list".getClass().getName() 
                    + "' to '" + Thread.class.getName() + "'",
                    e.getMessage());
            assertEquals(ClassCastException.class.getName(),
                    e.getCause().getClass().getName());
            assertEquals("Unable to cast '" + "list".getClass().getName() 
                    + "' to '" + Thread.class.getName() + "'",
                    e.getCause().getMessage());
        }

    }

    /**
     * testToList07()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D
     * <br><br>
     * 入力値：(引数) obj:要素を持たないObject[]<br>
     *         (引数) elementClass:String.class<br>
     *         
     * <br>
     * 期待値：(戻り値) List:要素を持たないList<String> (要素数が０であることを確認)<br>
     *         
     * <br>
     * 引数objが要素を持たない配列だった場合、elementsClassで指定した型のListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToList07() throws Exception {
        // 前処理
        Object[] obj = new Object[0];
        
        // テスト実施
        List<String> result = ConvertUtil.toList(obj, String.class);

        // 判定
        assertEquals(0, result.size());
    }

    /**
     * testToList08()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D
     * <br><br>
     * 入力値：(引数) obj:１要素を保持するObject[]<br>
     *                 *要素0:"foo"<br>
     *         (引数) elementClass:String.class<br>
     *         
     * <br>
     * 期待値：(戻り値) List:１要素を持つList<String> (要素数が１であることを確認)<br>
     *                   *要素0:"foo"<br>
     *         
     * <br>
     * 引数objが１要素を保持する配列だった場合、elementsClassで指定した型のListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToList08() throws Exception {
        // 前処理
        Object[] obj = {"foo"};
        
        // テスト実施
        List<String> result = ConvertUtil.toList(obj, String.class);

        // 判定
        assertEquals(1, result.size());
        assertEquals("foo", result.get(0));
    }

    /**
     * testToList09()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D
     * <br><br>
     * 入力値：(引数) obj:３要素を保持するObject[]<br>
     *                 *要素0:"foo"<br>
     *                 *要素2:"bar"<br>
     *                 *要素3:"baz"<br>
     *         (引数) elementClass:String.class<br>
     *         
     * <br>
     * 期待値：(戻り値) List:３要素を持つList<String> (要素数が３であることを確認)<br>
     *                   *要素0:"foo"<br>
     *                   *要素1:"bar"<br>
     *                   *要素2:"baz"<br>
     *         
     * <br>
     * 引数objが３要素を保持する配列だった場合、elementsClassで指定した型のListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToList09() throws Exception {
        // 前処理
        Object[] obj = new Object[3];
        obj[0] = "foo";
        obj[1] = "bar";
        obj[2] = "baz";
        
        // テスト実施
        List<String> result = ConvertUtil.toList(obj, String.class);

        // 判定
        assertEquals(3, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
        assertEquals("baz", result.get(2));
    }

    /**
     * testToList10()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) obj:３要素を保持するObject[]<br>
     *                 *要素0:"foo"<br>
     *                 *要素2:Threadインスタンス<br>
     *                 *要素3:"baz"<br>
     *         (引数) elementClass:String.class<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    例外のメッセージ:
     *                    　"Unable to cast '" + Threadの完全修飾クラス名
     *                    　+ "' to '" + 引数elementClassの完全修飾クラス名 + "'"<br>
     *                    ラップされた例外:ClassCastException<br>
     *                    ラップされた例外のメッセージ:
     *                    　"Unable to cast '" + Threadの完全修飾クラス名
     *                    + "' to '" + 引数elementClassの完全修飾クラス名 + "'"<br>
     *         
     * <br>
     * 引数objの配列にelementClassで指定した型と同一、もしくはサブクラスではない
     * 要素が含まれていた場合、IllegalArgumentExceptionがスローされることを
     * 確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToList10() throws Exception {
        // 前処理
        Object[] obj = new Object[3];
        obj[0] = "foo";
        obj[1] = new Thread();
        obj[2] = "baz";

        // テスト実施
        try {
            ConvertUtil.toList(obj, String.class);
            fail("IllegalArgumentExceptionがスローされませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
            assertEquals("Unable to cast '" + Thread.class.getName()
                    + "' to '" + String.class.getName() + "'",
                    e.getMessage());
            assertEquals(ClassCastException.class.getName(),
                    e.getCause().getClass().getName());
            assertEquals("Unable to cast '" + Thread.class.getName()
                    + "' to '" + String.class.getName() + "'",
                    e.getCause().getMessage());
        }
    }

    /**
     * testToList11()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D
     * <br><br>
     * 入力値：(引数) obj:要素を持たないCollection<br>
     *         (引数) elementClass:String.class<br>
     *         
     * <br>
     * 期待値：(戻り値) List:要素を持たないList<String> (要素数が０であることを確認)<br>
     *         
     * <br>
     * 引数objが要素を持たないCollectionだった場合、elementsClassで指定した型のListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToList11() throws Exception {
        // 前処理
        Collection obj = new Vector(); 
        
        // テスト実施
        List<String> result = ConvertUtil.toList(obj, String.class);

        // 判定
        assertEquals(0, result.size());

    }

    /**
     * testToList12()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D
     * <br><br>
     * 入力値：(引数) obj:１要素を保持するCollection<br>
     *                 *要素0:"foo"<br>
     *         (引数) elementClass:String.class<br>
     *         
     * <br>
     * 期待値：(戻り値) List:１要素を持つList<String> (要素数が１であることを確認)<br>
     *                   *要素0:"foo"<br>
     *         
     * <br>
     * 引数objが１要素を保持するCollectionだった場合、elementsClassで指定した型のListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testToList12() throws Exception {
        // 前処理
        Collection obj = new Vector(); 
        obj.add("foo");
        
        // テスト実施
        List<String> result = ConvertUtil.toList(obj, String.class);

        // 判定
        assertEquals(1, result.size());
        assertEquals("foo", result.get(0));
    }

    /**
     * testToList13()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D
     * <br><br>
     * 入力値：(引数) obj:３要素を保持するCollection<br>
     *                 *要素0:"foo"<br>
     *                 *要素2:"bar"<br>
     *                 *要素3:"baz"<br>
     *         (引数) elementClass:String.class<br>
     *         
     * <br>
     * 期待値：(戻り値) List:３要素を持つList<String> (要素数が３であることを確認)<br>
     *                   *要素0:"foo"<br>
     *                   *要素1:"bar"<br>
     *                   *要素2:"baz"<br>
     *         
     * <br>
     * 引数objが３要素を保持するCollectionだった場合、elementsClassで指定した型のListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testToList13() throws Exception {
        // 前処理
        Collection obj = new Vector(); 
        obj.add("foo");
        obj.add("bar");
        obj.add("baz");
        
        // テスト実施
        List<String> result = ConvertUtil.toList(obj, String.class);

        // 判定
        assertEquals(3, result.size());
        assertEquals("foo", result.get(0));    
        assertEquals("bar", result.get(1));    
        assertEquals("baz", result.get(2));    
    }

    /**
     * testToList14()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) obj:３要素を保持するCollection<br>
     *                 *要素0:"foo"<br>
     *                 *要素2:Threadインスタンス<br>
     *                 *要素3:"baz"<br>
     *         (引数) elementClass:String.class<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    例外のメッセージ:
     *                    "Unable to cast '" + Threadの完全修飾クラス名
     *                    　+ "' to '" + 引数elementClassの完全修飾クラス名 + "'"<br>
     *                    ラップされた例外:ClassCastException<br>
     *                    ラップされた例外のメッセージ:
     *                    "Unable to cast '" + Threadの完全修飾クラス名
     *                    + "' to '" + 引数elementClassの完全修飾クラス名 + "'"<br>
     *         
     * <br>
     * 引数objのCollectionにelementClassで指定した型と同一、もしくはサブクラス
     * ではない要素が含まれていた場合、IllegalArgumentExceptionがスローされること
     * を確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToList14() throws Exception {
        // 前処理
        Object[] obj = new Object[3];
        obj[0] = "foo";
        obj[1] = new Thread();
        obj[2] = "baz";

        // テスト実施
        try {
            ConvertUtil.toList(obj, String.class);
            fail("IllegalArgumentExceptionがスローされませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
            assertEquals("Unable to cast '" + Thread.class.getName()
                    + "' to '" + String.class.getName() + "'",
                    e.getMessage());
            assertEquals(ClassCastException.class.getName(),
                    e.getCause().getClass().getName());
            assertEquals("Unable to cast '" + Thread.class.getName()
                    + "' to '" + String.class.getName() + "'",
                    e.getCause().getMessage());
        }
    }

    /**
     * testConvertObjectClass01()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) obj:"object"<br>
     *         (引数) clazz:null<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    例外のメッセージ:
     *                    "Argument 'clazz' (" + Object.class.getName() 
     *                    + ") is null"<br>
     *         
     * <br>
     * 引数clazzがnullだった場合、IllegalArgumentExceptionがスローされることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertObjectClass01() throws Exception {
        // テスト実施
        try {
            ConvertUtil.convert("object", null);
            fail("IllegalArgumentExceptionがスローされませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
            assertEquals("Argument 'clazz' (" + Object.class.getName()
                    + ") is null",
                    e.getMessage());
        }

    }

    /**
     * testConvertObjectClass02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) obj:null<br>
     *         (引数) clazz:Object.class<br>
     *         
     * <br>
     * 期待値：(戻り値) <T>:null<br>
     *         
     * <br>
     * 引数objがnullだった場合、nullが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertObjectClass02() throws Exception {
        // テスト実施
        // 判定
        assertNull(ConvertUtil.convert(null, Object.class));
    }

    /**
     * testConvertObjectClass03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) obj:List (ArrayListでインスタンス化する)<br>
     *         (引数) clazz:ArrayList.class<br>
     *         
     * <br>
     * 期待値：(戻り値) <T>:Listインスタンス (引数objと同じインスタンスであることを確認)<br>
     *         
     * <br>
     * 引数objと同じインスタンスが返却されるのを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertObjectClass03() throws Exception {
        // 前処理
        List list = new ArrayList();

        // テスト実施
        List result = ConvertUtil.convert(list, ArrayList.class);

        // 判定
        assertSame(list, result);
    }

    /**
     * testConvertObjectClass04()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) obj:Threadインスタンス<br>
     *         (引数) clazz:AlgorithmParameterGenerator.class<br>
     *         
     * <br>
     * 期待値：(戻り値) <T>:IllegalArgumentExceptionがスローされる。<br>
     *         
     * <br>
     * 引数objの型がclazzの型と互換性がない場合、IllegalArgumentExceptionがスローされることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertObjectClass04() throws Exception {
        // 前処理
        Thread thread = new Thread();

        // テスト実施
        try {
           ConvertUtil.convert(thread,
                    AlgorithmParameterGenerator.class);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * testConvertIfNotNull01()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) obj:"object"<br>
     *         (引数) clazz:null<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    例外のメッセージ:
     *                    "Argument 'clazz' (" + Object.class.getName() 
     *                    　+ ") is null"<br>
     *         
     * <br>
     * 引数clazzがnullだった場合、IllegalArgumentExceptionがスローされることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertIfNotNull01() throws Exception {
        // テスト実施
        try {
            ConvertUtil.convertIfNotNull("object", null);
            fail("IllegalArgumentExceptionがスローされませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
            assertEquals("Argument 'clazz' (" + Object.class.getName()
                    + ") is null",
                    e.getMessage());
        }
    }

    /**
     * testConvertIfNotNull02()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) obj:null<br>
     *         (引数) clazz:Object.class<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    例外のメッセージ:
     *                    "Unable to cast 'null' to '" 
     *                    　+ 引数clazzの完全修飾クラス名 + "'"<br>
     *                    ラップされた例外:ClassCastException<br>
     *                    ラップされた例外のメッセージ:
     *                    "Unable to cast 'null' to '" 
     *                    　+ 引数clazzの完全修飾クラス名 + "'"<br>
     *         
     * <br>
     * 引数objがnullだった場合、IllegalArgumentExceptionがスローされることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertIfNotNull02() throws Exception {
        // テスト実施
        try {
            ConvertUtil.convertIfNotNull(null, Object.class);
            fail("IllegalArgumentExceptionがスローされませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
            assertEquals("Unable to cast 'null' to '" + Object.class.getName()
                    + "'", e.getMessage());
            assertEquals(ClassCastException.class.getName(),
                    e.getCause().getClass().getName());
            assertEquals("Unable to cast 'null' to '" + Object.class.getName()
                    + "'", e.getCause().getMessage());
        }
    }

    /**
     * testConvertIfNotNull03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) obj:List (ArrayListでインスタンス化する)<br>
     *         (引数) clazz:ArrayList.class<br>
     *         
     * <br>
     * 期待値：(戻り値) <T>:Listインスタンス (引数objと同じインスタンスであることを確認)<br>
     *         
     * <br>
     * 引数objと同じインスタンスが返却されるのを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertIfNotNull03() throws Exception {
        // 前処理
        List list = new ArrayList();

        // テスト実施
        List result = ConvertUtil.convertIfNotNull(list, ArrayList.class);

        // 判定
        assertSame(list, result);
    }

    /**
     * testConvertIfNotNull04()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) obj:Threadインスタンス<br>
     *         (引数) clazz:AlgorithmParameterGenerator.class<br>
     *         
     * <br>
     * 期待値：(戻り値) IllegalArgumentExceptionがスローされること。<br>
     *         
     * <br>
     * 引数objの型がclazzの型と互換性がない場合、IllegalArgumentExceptionがスローされることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertIfNotNull04() throws Exception {
        // 前処理
        Thread thread = new Thread();

        // テスト実施
        try {
            ConvertUtil.convert(thread,
                    AlgorithmParameterGenerator.class);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * testConvertObjectClassboolean01()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) obj:null<br>
     *         (引数) clazz:null<br>
     *         (引数) allowsNull:true<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    例外のメッセージ:
     *                    "Argument 'clazz' (" + Object.class.getName() 
     *                      + ") is null"<br>
     *         
     * <br>
     * 引数clazzがnullだった場合、IllegalArgumentExceptionがスローされることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertObjectClassboolean01() throws Exception {
        // テスト実施
        try {
            ConvertUtil.convert(null, null, true);
            fail("IllegalArgumentExceptionがスローされませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
            assertEquals("Argument 'clazz' (" + Object.class.getName()
                    + ") is null", e.getMessage());
        }

    }

    /**
     * testConvertObjectClassboolean02()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) obj:null<br>
     *         (引数) clazz:Object.class<br>
     *         (引数) allowsNull:false<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    例外のメッセージ:
     *                    "Unable to cast 'null' to '" 
     *                      + 引数clazzの完全修飾クラス名 + "'"<br>
     *                    ラップされた例外:ClassCastException<br>
     *                    ラップされた例外のメッセージ:
     *                    "Unable to cast 'null' to '" 
     *                      + 引数clazzの完全修飾クラス名 + "'"<br>
     *         
     * <br>
     * 引数clazzがnullではなく、objがnull、allowsNullがfalseだった場合、
     * IllegalArgumentExceptionがスローされることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertObjectClassboolean02() throws Exception {
        // テスト実施
        try {
            ConvertUtil.convert(null, Object.class, false);
            fail("IllegalArgumentExceptionがスローされませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
            assertEquals("Unable to cast 'null' to '" + Object.class.getName()
                    + "'", e.getMessage());
            assertEquals(ClassCastException.class.getName(),
                    e.getCause().getClass().getName());
            assertEquals("Unable to cast 'null' to '" + Object.class.getName()
                    + "'", e.getCause().getMessage());
        }    
    }

    /**
     * testConvertObjectClassboolean03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) obj:null<br>
     *         (引数) clazz:Object.class<br>
     *         (引数) allowsNull:true<br>
     *         
     * <br>
     * 期待値：(戻り値) <T>:null<br>
     *         
     * <br>
     * 引数clazzがnullではなく、objがnull、allowsNullがtrueだった場合、nullが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertObjectClassboolean03() throws Exception {
        // テスト実施
        // 判定
        assertNull(ConvertUtil.convert(null, Object.class, true));
    }

    /**
     * testConvertObjectClassboolean04()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) obj:Threadインスタンス<br>
     *         (引数) clazz:Thread.class<br>
     *         (引数) allowsNull:true<br>
     *         
     * <br>
     * 期待値：(戻り値) <T>:Threadインスタンス (引数objと同一インスタンスであることを確認)<br>
     *         
     * <br>
     * 引数clazzの型とobjの型が同じだった場合、clazzの型のインスタンスが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertObjectClassboolean04() throws Exception {
        // 前処理
        Thread thread = new Thread();
        
        // テスト実施
        Thread result = ConvertUtil.convert(thread, Thread.class, true);

        // 判定
        assertSame(thread, result);
    }

    /**
     * testConvertObjectClassboolean05()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) obj:List (ArrayListでインスタンス化する)<br>
     *         (引数) clazz:ArrayList.class<br>
     *         (引数) allowsNull:true<br>
     *         
     * <br>
     * 期待値：(戻り値) <T>:Listインスタンス (引数objと同じインスタンスであることを確認)<br>
     *         
     * <br>
     * 引数objと同じインスタンスが返却されるのを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertObjectClassboolean05() throws Exception {
        //  前処理
        List list = new ArrayList();
        
        // テスト実施
        List result = ConvertUtil.convert(list, ArrayList.class, true);

        // 判定
        assertNotNull(result);
        assertSame(list, result);
    }

    /**
     * testConvertObjectClassboolean06()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) obj:Threadインスタンス<br>
     *         (引数) clazz:AlgorithmParameterGenerator.class<br>
     *         (引数) allowsNull:true<br>
     *         
     * <br>
     * 期待値：(戻り値) <T>:IllegalArgumentExceptionがスローされること。<br>
     *         
     * <br>
     * 引数clazzの型がobjの型と互換性がなかった場合、IllegalArgumentExceptionがスローされることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertObjectClassboolean06() throws Exception {
        //  前処理
        Thread thread = new Thread();
        
        // テスト実施
        try {
            ConvertUtil.convert(thread,
                    AlgorithmParameterGenerator.class,
                    true);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * testConvertObjectClassboolean07()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) obj:"abc"<br>
     *         (引数) clazz:BigInteger.class<br>
     *         (引数) allowsNull:false<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    ラップされた例外:ConversionException<br>
     *         
     * <br>
     * CnvertUtils#convertで例外が発生した場合、IllegalArgumentExceptionがスローされることを確認するテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertObjectClassboolean07() throws Exception {
        
        // テスト実施
        try {
            ConvertUtil.convert("abc", BigInteger.class, false);
            fail("IllegalArgumentExceptionがスローされませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
            assertTrue(e.getCause() instanceof ConversionException);
        }
    }

    /**
     * testConvertPrimitiveArrayToList01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:null<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:null<br>
     *         
     * <br>
     * 引数valueがnullだった場合、nullが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList01() throws Exception {
        // テスト実施
        // 判定
        assertNull(ConvertUtil.convertPrimitiveArrayToList(null));
    }

    /**
     * testConvertPrimitiveArrayToList02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:""(空文字)<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:""(空文字)<br>
     *         
     * <br>
     * 引数valueが""(空文字)だった場合、""(空文字)が返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList02() throws Exception {
        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList("");
        
        // 判定
        assertNotNull(result);
        assertEquals("", result);
    }

    /**
     * testConvertPrimitiveArrayToList03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:"  "(空白文字列)<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:"  "(空白文字列)<br>
     *         
     * <br>
     * 引数valueが"  "(空白文字列)だった場合、""(空文字)が返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList03() throws Exception {
        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList("  ");
        
        // 判定
        assertNotNull(result);
        assertEquals("  ", result);
    }

    /**
     * testConvertPrimitiveArrayToList04()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：E
     * <br><br>
     * 入力値：(引数) value:"noArray"<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:"noArray"<br>
     *         
     * <br>
     * 引数valueが通常文字列だった場合、引数の値がそのまま返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList04() throws Exception {
        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList("noarray");
        
        // 判定
        assertNotNull(result);
        assertEquals("noarray", result);
    }

    /**
     * testConvertPrimitiveArrayToList05()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:要素を保持しないObject[]<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:要素を保持しないObject[] (要素数が0であることを確認)<br>
     *         
     * <br>
     * 引数valueがプリミティブ型以外の配列(要素数0)だった場合、引数の値がそのまま返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList05() throws Exception {
        // 前処理
        Object[] value = new Object[0];

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof Object[]);
        Object[] arrayResult = (Object[]) result;
        assertEquals(0, arrayResult.length);
    }

    /**
     * testConvertPrimitiveArrayToList06()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するObject[]<br>
     *                 *要素0:"foo"<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するObject[] (要素数が1であることを確認)<br>
     *                   *要素0:"foo"<br>
     *         
     * <br>
     * 引数valueがプリミティブ型以外の配列(要素数1)だった場合、引数の値がそのまま返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList06() throws Exception {
        // 前処理
        Object[] value = {"foo"};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof Object[]);
        Object[] arrayResult = (Object[]) result;
        assertEquals(1, arrayResult.length);
        assertEquals("foo", arrayResult[0]);
        
    }

    /**
     * testConvertPrimitiveArrayToList07()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するObject[]<br>
     *                 *要素0:"foo"<br>
     *                 *要素1:"bar"<br>
     *                 *要素2:"baz"<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するObject[] (要素数が3であることを確認)<br>
     *                   *要素0:"foo"<br>
     *                   *要素1:"bar"<br>
     *                   *要素3:"baz"<br>
     *         
     * <br>
     * 引数valueがプリミティブ型以外の配列(要素数3)だった場合、引数の値がそのまま返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList07() throws Exception {
        // 前処理
        Object[] value = {"foo", "bar", "baz"};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof Object[]);
        Object[] arrayResult = (Object[]) result;
        assertEquals(3, arrayResult.length);
        assertEquals("foo", arrayResult[0]);
        assertEquals("bar", arrayResult[1]);
        assertEquals("baz", arrayResult[2]);
    }

    /**
     * testConvertPrimitiveArrayToList08()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:要素を保持しないboolean[]<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:要素を保持しないList (sizeが0であることを確認)<br>
     *         
     * <br>
     * 引数valueが要素を保持しないboolean型の配列だった場合、要素を保持しないListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList08() throws Exception {
        // 前処理
        boolean[] value = new boolean[0];

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(0, listResult.size());
    }

    /**
     * testConvertPrimitiveArrayToList09()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するboolean[]<br>
     *                 *要素0:true<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが1であることを確認)<br>
     *                   *要素0:true<br>
     *         
     * <br>
     * 引数valueがboolean型の配列(要素数1)だった場合、boolean型の値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList09() throws Exception {
        // 前処理
        boolean[] value = {true};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(1, listResult.size());
        assertTrue((Boolean) listResult.get(0));
    }

    /**
     * testConvertPrimitiveArrayToList10()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するboolean[]<br>
     *                 *要素0:true<br>
     *                 *要素1:false<br>
     *                 *要素2:true<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが3であることを確認)<br>
     *                   *要素0:true<br>
     *                   *要素1:false<br>
     *                   *要素2:true<br>
     *         
     * <br>
     * 引数valueがboolean型の配列(要素数3)だった場合、boolean型の値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList10() throws Exception {
        // 前処理
        boolean[] value = {true, false, true};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(3, listResult.size());
        assertTrue((Boolean) listResult.get(0));
        assertFalse((Boolean) listResult.get(1));
        assertTrue((Boolean) listResult.get(2));
    }

    /**
     * testConvertPrimitiveArrayToList11()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:要素を保持しないbyte[]<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:要素を保持しないList (sizeが0であることを確認)<br>
     *         
     * <br>
     * 引数valueが要素を保持しないbyte型の配列だった場合、要素を保持しないListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList11() throws Exception {
        // 前処理
        byte[] value = new byte[0];

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(0, listResult.size());
    }

    /**
     * testConvertPrimitiveArrayToList12()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するbyte[]<br>
     *                 *要素0: (byte) 1<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが1であることを確認)<br>
     *                   *要素0:"1"<br>
     *         
     * <br>
     * 引数valueがbyte型の配列(要素数1)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList12() throws Exception {
        // 前処理
        byte[] value = {1};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(1, listResult.size());
        assertEquals("1", listResult.get(0));
    }

    /**
     * testConvertPrimitiveArrayToList13()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するbyte[]<br>
     *                 *要素0: (byte) 1<br>
     *                 *要素1: (byte) 2<br>
     *                 *要素2: (byte) 3<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが3であることを確認)<br>
     *                   *要素0:"1"<br>
     *                   *要素1:"2"<br>
     *                   *要素2:"3"<br>
     *         
     * <br>
     * 引数valueがbyte型の配列(要素数3)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList13() throws Exception {
        // 前処理
        byte[] value = {1, 2, 3};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(3, listResult.size());
        assertEquals("1", listResult.get(0));    
        assertEquals("2", listResult.get(1));    
        assertEquals("3", listResult.get(2));    
    }

    /**
     * testConvertPrimitiveArrayToList14()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:要素を保持しないchar[]<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:要素を保持しないList (sizeが0であることを確認)<br>
     *         
     * <br>
     * 引数valueが要素を保持しないchar型の配列だった場合、要素を保持しないListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList14() throws Exception {
        // 前処理
        char[] value = new char[0];

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(0, listResult.size());
    }

    /**
     * testConvertPrimitiveArrayToList15()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するchar[]<br>
     *                 *要素0: 'A'<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが1であることを確認)<br>
     *                   *要素0: "A"<br>
     *         
     * <br>
     * 引数valueがchar型の配列(要素数1)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList15() throws Exception {
        // 前処理
        char[] value = {'A'};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(1, listResult.size());
        assertEquals("A", listResult.get(0));    
    }

    /**
     * testConvertPrimitiveArrayToList16()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するchar[]<br>
     *                 *要素0: 'A'<br>
     *                 *要素1: 'B'<br>
     *                 *要素2: 'C'<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが3であることを確認)<br>
     *                   *要素0: "A"<br>
     *                   *要素1: "B"<br>
     *                   *要素2: "C"<br>
     *         
     * <br>
     * 引数valueがchar型の配列(要素数3)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList16() throws Exception {
        // 前処理
        char[] value = {'A', 'B', 'C'};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(3, listResult.size());
        assertEquals("A", listResult.get(0));    
        assertEquals("B", listResult.get(1));    
        assertEquals("C", listResult.get(2));    
    }

    /**
     * testConvertPrimitiveArrayToList17()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:要素を保持しないdouble[]<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:要素を保持しないList (sizeが0であることを確認)<br>
     *         
     * <br>
     * 引数valueが要素を保持しないdouble型の配列だった場合、要素を保持しないListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList17() throws Exception {
        // 前処理
        double[] value = new double[0];

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(0, listResult.size());
    }

    /**
     * testConvertPrimitiveArrayToList18()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するdouble[]<br>
     *                 *要素0: 123.456<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが1であることを確認)<br>
     *                   *要素0: "123.456"<br>
     *         
     * <br>
     * 引数valueがdouble型の配列(要素数1)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList18() throws Exception {
        // 前処理
        double[] value = {123.456};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(1, listResult.size());
        assertEquals("123.456", listResult.get(0));    
    }

    /**
     * testConvertPrimitiveArrayToList19()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するdouble[]<br>
     *                 *要素0: 123.456<br>
     *                 *要素1: 12.34<br>
     *                 *要素2: 1.2<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが3であることを確認)<br>
     *                   *要素0: "123.456"<br>
     *                   *要素1: "12.34"<br>
     *                   *要素2: "1.2"<br>
     *         
     * <br>
     * 引数valueがdouble型の配列(要素数3)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList19() throws Exception {
        // 前処理
        double[] value = {123.456, 12.34, 1.2};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(3, listResult.size());
        assertEquals("123.456", listResult.get(0));    
        assertEquals("12.34", listResult.get(1));    
        assertEquals("1.2", listResult.get(2));    
    }

    /**
     * testConvertPrimitiveArrayToList20()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:要素を保持しないfloat[]<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:要素を保持しないList (sizeが0であることを確認)<br>
     *         
     * <br>
     * 引数valueが要素を保持しないfloat型の配列だった場合、要素を保持しないListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList20() throws Exception {
        // 前処理
        float[] value = new float[0];

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(0, listResult.size());
    }

    /**
     * testConvertPrimitiveArrayToList21()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するfloat[]<br>
     *                 *要素0: 12.3F<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが1であることを確認)<br>
     *                   *要素0: "12.3"<br>
     *         
     * <br>
     * 引数valueがfloat型の配列(要素数1)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList21() throws Exception {
        // 前処理
        float[] value = {12.3F};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(1, listResult.size());
        assertEquals("12.3", listResult.get(0));    
    }

    /**
     * testConvertPrimitiveArrayToList22()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するfloat[]<br>
     *                 *要素0: 12.3F<br>
     *                 *要素1: 1.2F<br>
     *                 *要素2: 1F<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが3であることを確認)<br>
     *                   *要素0: "12.3"<br>
     *                   *要素1: "1.2"<br>
     *                   *要素2: "1.0"<br>
     *         
     * <br>
     * 引数valueがfloat型の配列(要素数3)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList22() throws Exception {
        // 前処理
        float[] value = {12.3F, 1.2F, 1F};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(3, listResult.size());
        assertEquals("12.3", listResult.get(0));    
        assertEquals("1.2", listResult.get(1));    
        assertEquals("1.0", listResult.get(2));    
    }

    /**
     * testConvertPrimitiveArrayToList23()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:要素を保持しないint[]<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:要素を保持しないList (sizeが0であることを確認)<br>
     *         
     * <br>
     * 引数valueが要素を保持しないint型の配列だった場合、要素を保持しないListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList23() throws Exception {
        // 前処理
        int[] value = new int[0];

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(0, listResult.size());
    }

    /**
     * testConvertPrimitiveArrayToList24()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するint[]<br>
     *                 *要素0: 1<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが1であることを確認)<br>
     *                   *要素0: "1"<br>
     *         
     * <br>
     * 引数valueがint型の配列(要素数1)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList24() throws Exception {
        // 前処理
        int[] value = {1};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(1, listResult.size());
        assertEquals("1", listResult.get(0));    
    }

    /**
     * testConvertPrimitiveArrayToList25()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するint[]<br>
     *                 *要素0: 1<br>
     *                 *要素1: 2<br>
     *                 *要素2: 3<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが3であることを確認)<br>
     *                   *要素0: "1"<br>
     *                   *要素1: "2"<br>
     *                   *要素2: "3"<br>
     *         
     * <br>
     * 引数valueがint型の配列(要素数3)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList25() throws Exception {
        // 前処理
        int[] value = {1, 2, 3};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(3, listResult.size());
        assertEquals("1", listResult.get(0));    
        assertEquals("2", listResult.get(1));    
        assertEquals("3", listResult.get(2));    
    }

    /**
     * testConvertPrimitiveArrayToList26()
     * <br><br>
     * 
     * (正常系) or (異常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:要素を保持しないlong[]<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:要素を保持しないList (sizeが0であることを確認)<br>
     *         
     * <br>
     * 引数valueが要素を保持しないlong型の配列だった場合、要素を保持しないListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList26() throws Exception {
        // 前処理
        long[] value = new long[0];

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(0, listResult.size());
    }

    /**
     * testConvertPrimitiveArrayToList27()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するlobg[]<br>
     *                 *要素0: 1L<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが1であることを確認)<br>
     *                   *要素0: "1"<br>
     *         
     * <br>
     * 引数valueがlong型の配列(要素数1)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList27() throws Exception {
        // 前処理
        long[] value = {1L};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(1, listResult.size());
        assertEquals("1", listResult.get(0));    
    }

    /**
     * testConvertPrimitiveArrayToList28()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するlong[]<br>
     *                 *要素0: 1L<br>
     *                 *要素1: 2L<br>
     *                 *要素2: 3L<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが3であることを確認)<br>
     *                   *要素0: "1"<br>
     *                   *要素1: "2"<br>
     *                   *要素2: "3"<br>
     *         
     * <br>
     * 引数valueがlong型の配列(要素数3)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList28() throws Exception {
        // 前処理
        long[] value = {1L, 2L, 3L};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(3, listResult.size());
        assertEquals("1", listResult.get(0));    
        assertEquals("2", listResult.get(1));    
        assertEquals("3", listResult.get(2));    
    }

    /**
     * testConvertPrimitiveArrayToList29()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:要素を保持しないshort[]<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:要素を保持しないList (sizeが0であることを確認)<br>
     *         
     * <br>
     * 引数valueが要素を保持しないshort型の配列だった場合、要素を保持しないListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList29() throws Exception {
        // 前処理
        short[] value = new short[0];

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(0, listResult.size());

    }

    /**
     * testConvertPrimitiveArrayToList30()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するshort[]<br>
     *                 *要素0: (short) 1<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが1であることを確認)<br>
     *                   *要素0: "1"<br>
     *         
     * <br>
     * 引数valueがshort型の配列(要素数1)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList30() throws Exception {
        // 前処理
        short[] value = {(short) 1};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(1, listResult.size());
    }

    /**
     * testConvertPrimitiveArrayToList31()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D,E
     * <br><br>
     * 入力値：(引数) value:以下の要素を保持するshort[]<br>
     *                 *要素0: (short) 1<br>
     *                 *要素1: (short) 2<br>
     *                 *要素2: (short) 3<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:以下の要素を保持するList(sizeが3であることを確認)<br>
     *                   *要素0: "1"<br>
     *                   *要素1: "2"<br>
     *                   *要素2: "3"<br>
     *         
     * <br>
     * 引数valueがshort型の配列(要素数3)だった場合、String型に変換された値を保持するListが返却されることを確認するテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvertPrimitiveArrayToList31() throws Exception {
        // 前処理
        short[] value = {(short) 1, (short) 2, (short) 3};

        // テスト実施
        Object result = ConvertUtil.convertPrimitiveArrayToList(value);
        
        // 判定
        assertNotNull(result);
        assertTrue(result instanceof List);
        List listResult = (List) result;
        assertEquals(3, listResult.size());
        assertEquals("1", listResult.get(0));    
        assertEquals("2", listResult.get(1));    
        assertEquals("3", listResult.get(2));    
    }

    /**
     * testToListOfMap01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D, E
     * <br><br>
     * 入力値：(引数) obj:null<br>
     *         
     * <br>
     * 期待値：(戻り値) List<Map<String, Object>:要素を持たないObject[]<br>
     *         
     * <br>
     * 引数objがnullの場合、要素を持たないList<Map<String,Object>インスタンスが返ることを確認するテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToListOfMap01() throws Exception {
        // 前処理
        // テスト実施
        List<Map<String, Object>> actual = ConvertUtil.toListOfMap(null);

        // 判定
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    /**
     * testToListOfMap02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D, E
     * <br><br>
     * 入力値：(引数) obj:3要素を持つJavaBeanスタブのリスト<br>
     *                要素0=JavaBeanスタブインスタンス<br>
     *                ＊フィールドA="value00"<br>
     *                要素1=JavaBeanスタブインスタンス<br>
     *                ＊フィールドA="value01"<br>
     *                要素2=JavaBeanスタブインスタンス<br>
     *                ＊フィールドA="value02"<br>
     *         
     * <br>
     * 期待値：(戻り値) List<Map<String, Object>:
     *      3要素を持つList<Map<String, Object>>インスタンス<br>
     *                  <br>
     *                  要素0-=Map<String,Object>インスタンス<br>
     *                  ＊key="a",value="value00"<br>
     *                  要素1=Map<String,Object>インスタンス<br>
     *                  ＊key="a",value="value01"<br>
     *                  要素2=Map<String,Object>インスタンス<br>
     *                  ＊key="a",value="value02"<br>
     *         
     * <br>
     * 引数objが複数のJavaBean要素を持つリストの場合、
     * 3つの要素を持つList<Map<String,Object>インスタンスが返ることを確認するテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToListOfMap02() throws Exception {

        List<ConvertUtil_Stub01> obj = new ArrayList<ConvertUtil_Stub01>();
        ConvertUtil_Stub01 bean = new ConvertUtil_Stub01();
        bean.setA("value00");
        obj.add(bean);
        ConvertUtil_Stub01 bean02 = new ConvertUtil_Stub01();
        bean02.setA("value01");
        obj.add(bean02);
        ConvertUtil_Stub01 bean03 = new ConvertUtil_Stub01();
        bean03.setA("value02");
        obj.add(bean03);

        // テスト実施
        List<Map<String, Object>> actual = ConvertUtil.toListOfMap(obj);

        // 判定
        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertEquals("value00", actual.get(0).get("a"));
        assertEquals("value01", actual.get(1).get("a"));
        assertEquals("value02", actual.get(2).get("a"));
    }

    /**
     * testToListOfMap03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D, E
     * <br><br>
     * 入力値：(引数) obj:3要素を持つJavaBeanスタブ配列<br>
     *                要素0=JavaBeanスタブインスタンス<br>
     *                ＊フィールドA="value00"<br>
     *                要素1=JavaBeanスタブインスタンス<br>
     *                ＊フィールドA="value01"<br>
     *                要素2=JavaBeanスタブインスタンス<br>
     *                ＊フィールドA="value02"<br>
     *         
     * <br>
     * 期待値：(戻り値) List<Map<String, Object>:
     * 3要素を持つList<Map<String, Object>>インスタンス<br>
     *                  <br>
     *                  要素0-=Map<String,Object>インスタンス<br>
     *                  ＊key="a",value="value00"<br>
     *                  要素1=Map<String,Object>インスタンス<br>
     *                  ＊key="a",value="value01"<br>
     *                  要素2=Map<String,Object>インスタンス<br>
     *                  ＊key="a",value="value02"<br>
     *         
     * <br>
     * 引数objが複数のJavaBean要素を持つ配列の場合、
     * 3つの要素を持つList<Map<String,Object>インスタンスが返ることを確認するテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToListOfMap03() throws Exception {
        ConvertUtil_Stub01 bean = new ConvertUtil_Stub01();
        bean.setA("value00");
        ConvertUtil_Stub01 bean02 = new ConvertUtil_Stub01();
        bean02.setA("value01");
        ConvertUtil_Stub01 bean03 = new ConvertUtil_Stub01();
        bean03.setA("value02");
        
        ConvertUtil_Stub01[] obj =
            new ConvertUtil_Stub01[] {bean, bean02, bean03};

        // テスト実施
        List<Map<String, Object>> actual = ConvertUtil.toListOfMap(obj);

        // 判定
        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertEquals("value00", actual.get(0).get("a"));
        assertEquals("value01", actual.get(1).get("a"));
        assertEquals("value02", actual.get(2).get("a"));
    }

    /**
     * testToListOfMap04()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D, E
     * <br><br>
     * 入力値：(引数) obj:3要素を持つList<Map<String, Object>>インスタンス<br>
     *                <br>
     *                要素0=Map<String,Object>インスタンス<br>
     *                ＊key="a",value="value00"<br>
     *                要素1=Map<String,Object>インスタンス<br>
     *                ＊key="a",value="value01"<br>
     *                要素2=Map<String,Object>インスタンス<br>
     *                ＊key="a",value="value02"<br>
     *         
     * <br>
     * 期待値：(戻り値) List<Map<String, Object>:
     *      3要素を持つList<Map<String, Object>>インスタンス<br>
     *                  <br>
     *                  要素0-=Map<String,Object>インスタンス<br>
     *                  ＊key="a",value="value00"<br>
     *                  要素1=Map<String,Object>インスタンス<br>
     *                  ＊key="a",value="value01"<br>
     *                  要素2=Map<String,Object>インスタンス<br>
     *                  ＊key="a",value="value02"<br>
     *         
     * <br>
     * 引数objが複数のMap要素を持つリストの場合、
     * 3つの要素を持つList<Map<String,Object>インスタンスが返ることを確認するテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToListOfMap04() throws Exception {
        List<Map<String, Object>> obj = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = new HashMap<String, Object>();
        row.put("a", "value00");
        obj.add(row);
        Map<String, Object> row02 = new HashMap<String, Object>();
        row02.put("a", "value01");
        obj.add(row02);
        Map<String, Object> row03 = new HashMap<String, Object>();
        row03.put("a", "value02");
        obj.add(row03);


        // テスト実施
        List<Map<String, Object>> actual = ConvertUtil.toListOfMap(obj);

        // 判定
        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertEquals("value00", actual.get(0).get("a"));
        assertEquals("value01", actual.get(1).get("a"));
        assertEquals("value02", actual.get(2).get("a"));
    }

    /**
     * testToListOfMap05()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：D, E
     * <br><br>
     * 入力値：(引数) obj:3要素を持つMap<String, Object>インスタンスの配列<br>
     *                <br>
     *                要素0=Map<String,Object>インスタンス<br>
     *                ＊key="a",value="value00"<br>
     *                要素1=Map<String,Object>インスタンス<br>
     *                ＊key="a",value="value01"<br>
     *                要素2=Map<String,Object>インスタンス<br>
     *                ＊key="a",value="value02"<br>
     *         
     * <br>
     * 期待値：(戻り値) List<Map<String, Object>:
     *      3要素を持つList<Map<String, Object>>インスタンス<br>
     *                  <br>
     *                  要素0-=Map<String,Object>インスタンス<br>
     *                  ＊key="a",value="value00"<br>
     *                  要素1=Map<String,Object>インスタンス<br>
     *                  ＊key="a",value="value01"<br>
     *                  要素2=Map<String,Object>インスタンス<br>
     *                  ＊key="a",value="value02"<br>
     *         
     * <br>
     * 引数objが複数のMap要素を持つ配列の場合、
     * 3つの要素を持つList<Map<String,Object>インスタンスが返ることを確認するテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToListOfMap05() throws Exception {
        Map<String, Object> row = new HashMap<String, Object>();
        row.put("a", "value00");
        Map<String, Object> row02 = new HashMap<String, Object>();
        row02.put("a", "value01");
        Map<String, Object> row03 = new HashMap<String, Object>();
        row03.put("a", "value02");
        
        Map[] obj = new Map[] {row, row02, row03};


        // テスト実施
        List<Map<String, Object>> actual = ConvertUtil.toListOfMap(obj);

        // 判定
        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertEquals("value00", actual.get(0).get("a"));
        assertEquals("value01", actual.get(1).get("a"));
        assertEquals("value02", actual.get(2).get("a"));
    }
    
    /**
     * testToListOfMap06()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) obj:JavaBeanスタブインスタンス<br>
     *                ＊フィールドA="value00"<br>
     *         
     * <br>
     * 期待値：(戻り値) List<Map<String, Object>:
     *      1要素を持つList<Map<String, Object>>インスタンス<br>
     *                  <br>
     *                  要素0-=Map<String,Object>インスタンス<br>
     *                  ＊key=A,value="value00"<br>
     *         
     * <br>
     * 引数objがJavaBeanの場合、1つの要素を持つ
     * List<Map<String,Object>インスタンスが返ることを確認するテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToListOfMap06() throws Exception {
        ConvertUtil_Stub01 obj = new ConvertUtil_Stub01();
        obj.setA("value00");

        // テスト実施
        List<Map<String, Object>> actual = ConvertUtil.toListOfMap(obj);

        // 判定
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("value00", actual.get(0).get("a"));
    }

    /**
     * testToListOfMap08()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) obj:JavaBeanスタブインスタンス<br>
     *                ＊フィールドA="value00"<br>
     *         (状態) PropertyUtils#describeの実行結果:
     *                  InvocationTargetExceptionをスロー
     *                    ※JavaBeanのgetterでRuntimeExceptionをスロー<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    ラップされた例外：InvocationTargetException<br>
     *         
     * <br>
     * PropertyUtils#descriveでInvocationTargetExceptionがスローされた場合のテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToListOfMap08() throws Exception {
        // 前処理
        ConvertUtil_Stub02 obj = new ConvertUtil_Stub02();
        obj.setA("value00");

        try {
            // テスト実施
            ConvertUtil.toListOfMap(obj);
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
            assertTrue(e.getCause() instanceof InvocationTargetException);
        }

    }

    /**
     * testToListOfMap09()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) obj:JavaBeanスタブインスタンス<br>
     *                ＊フィールドA="value00"<br>
     *         (状態) PropertyUtils#describeの実行結果:
     *                  IllegalAccessExceptionをスロー
     *                    ※PropertyUtilsBeanのスタブでIllegalAccessExceptionをスロー<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    ラップされた例外：IllegalAccessException<br>
     *         
     * <br>
     * PropertyUtils#descriveでIllegalAccessExceptionがスローされた場合のテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToListOfMap09() throws Exception {
        // 前処理
        List<ConvertUtil_Stub01> obj = new ArrayList<ConvertUtil_Stub01>();
        ConvertUtil_Stub01 bean = new ConvertUtil_Stub01();
        bean.setA("value00");
        obj.add(bean);
        BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
        UTUtil.setPrivateField(beanUtilsBean, "propertyUtilsBean",
                new ConvertUtil_PropertyUtilsBeanStub01());
        
        try {
            // テスト実施
            ConvertUtil.toListOfMap(obj);
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
            assertTrue(e.getCause() instanceof IllegalAccessException);
        }

    }

    /**
     * testToListOfMap10()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) obj:JavaBeanスタブインスタンス<br>
     *                ＊フィールドA="value00"<br>
     *         (状態) PropertyUtils#describeの実行結果:
     *                  NoSuchMethodExceptionをスロー
     *                    ※PropertyUtilsBeanのスタブでNoSuchMethodExceptionをスロー<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    ラップされた例外：NoSuchMethodException<br>
     *         
     * <br>
     * PropertyUtils#descriveでNoSuchMethodExceptionがスローされた場合のテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToListOfMap10() throws Exception {
        // 前処理
        List<ConvertUtil_Stub01> obj = new ArrayList<ConvertUtil_Stub01>();
        ConvertUtil_Stub01 bean = new ConvertUtil_Stub01();
        bean.setA("value00");
        obj.add(bean);
        BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
        UTUtil.setPrivateField(beanUtilsBean, "propertyUtilsBean",
                new ConvertUtil_PropertyUtilsBeanStub02());
        
        try {
            // テスト実施
            ConvertUtil.toListOfMap(obj);
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(IllegalArgumentException.class.getName(),
                    e.getClass().getName());
            assertTrue(e.getCause() instanceof NoSuchMethodException);
        }

    }
}
