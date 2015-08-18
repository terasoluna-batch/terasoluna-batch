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

package jp.terasoluna.fw.beans.jxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Locale;

import jp.terasoluna.utlib.UTUtil;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.beans.jxpath.DynamicPointerFactoryEx} クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * Map用ポインタファクトリの拡張クラス。<br>
 * 前提条件：
 * <p>
 * 
 * @see jp.terasoluna.fw.beans.jxpath.DynamicPointerFactoryEx
 */
public class DynamicPointerFactoryExTest {

    /**
     * testGetOrder01()
     * <br><br>
     * 
     * (正常系) 
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：
     * <br>
     * 期待値：(戻り値) -:750<br>
     *         
     * <br>
     * ソート順を取得するメソッド。固定値を返す。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetOrder01() throws Exception {
        // 前処理
    	DynamicPointerFactoryEx factory = new DynamicPointerFactoryEx();

        // テスト実施
    	assertEquals(750, factory.getOrder());
    }

    /**
     * testCreateNodePointerQname01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) name:not null<br>
     *         (引数) bean:new HashMap() {<br>
     *                    key="value"<br>
     *                }<br>
     *         (引数) locale:Locale("ja")<br>
     *         (状態) bi.isDynamic():true<br>
     *         
     * <br>
     * 期待値：(戻り値) NodePointer:new DynamicPointerEX {<br>
     *                      locale=引数のlocale<br>
     *                      name=引数のname<br>
     *                      bean=引数のbean<br>
     *                  }<br>
     *         
     * <br>
     * Map型のターゲットのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCreateNodePointerQname01() throws Exception {
        // 前処理
    	DynamicPointerFactoryEx factory = new DynamicPointerFactoryEx();
    	QName qName = new QName("name");
    	Object bean = new HashMap();
    	Locale locale = new Locale("ja");

        // テスト実施
    	NodePointer result = factory.createNodePointer(qName, bean, locale);

        // 判定
    	assertSame(DynamicPointerEx.class, result.getClass());
    	assertSame(locale, result.getLocale());
    	assertSame(qName, result.getName());
    	assertSame(bean, UTUtil.getPrivateField(result, "bean"));
    }

    /**
     * testCreateNodePointerQname02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) name:not null<br>
     *         (引数) bean:new Object()<br>
     *         (引数) locale:Locale("ja")<br>
     *         (状態) bi.isDynamic():false<br>
     *         
     * <br>
     * 期待値：(戻り値) NodePointer:null<br>
     *         
     * <br>
     * Map型ではないターゲットのテスト。Nullを返す。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCreateNodePointerQname02() throws Exception {
        // 前処理
    	DynamicPointerFactoryEx factory = new DynamicPointerFactoryEx();
    	QName qName = new QName("name");
    	Object bean = new Object();
    	Locale locale = new Locale("ja");

        // テスト実施
    	assertNull(factory.createNodePointer(qName, bean, locale));
    }

    /**
     * testCreateNodePointerNodePointer01()
     * <br><br>
     * 
     * (正常系) 
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) parent:not null<br>
     *         (引数) name:not null<br>
     *         (引数) bean:null<br>
     *         
     * <br>
     * 期待値：(戻り値) NodePointer:new NullPointer {<br>
     *                      parent=引数のparent<br>
     *                      name=引数のname<br>
     *                  }<br>
     *         
     * <br>
     * ターゲットがnullの場合のテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCreateNodePointerNodePointer01() throws Exception {
        // 前処理
    	DynamicPointerFactoryEx factory = new DynamicPointerFactoryEx();
    	QName qName = new QName("name");
    	Locale locale = new Locale("ja");
    	NodePointer nodePointer = NodePointer.newNodePointer(qName, null, locale);

        // テスト実施
    	NodePointer result = factory.createNodePointer(nodePointer, qName, null);

        // 判定
    	assertSame(NullPointer.class, result.getClass());
    	assertSame(qName, result.getName());
    	assertSame(nodePointer, result.getParent());
    }

    /**
     * testCreateNodePointerNodePointer02()
     * <br><br>
     * 
     * (正常系) 
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) parent:not null<br>
     *         (引数) name:not null<br>
     *         (引数) bean:new HashMap() {<br>
     *                    key="value"<br>
     *                }<br>
     *         (状態) bi.isDynamic():true<br>
     *         
     * <br>
     * 期待値：(戻り値) NodePointer:new DynamicPointerEX {<br>
     *                      parent=引数のparent<br>
     *                      name=引数のname<br>
     *                      bean=引数のbean<br>
     *                  }<br>
     *         
     * <br>
     * Map型のターゲットのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCreateNodePointerNodePointer02() throws Exception {
        // 前処理
    	DynamicPointerFactoryEx factory = new DynamicPointerFactoryEx();
    	QName qName = new QName("name");
    	Object bean = new HashMap();
    	
    	Locale locale = new Locale("ja");
    	NodePointer nodePointer = NodePointer.newNodePointer(qName, bean, locale);

        // テスト実施
    	NodePointer result = factory.createNodePointer(nodePointer, qName, bean);

        // 判定
    	assertSame(DynamicPointerEx.class, result.getClass());
    	assertSame(nodePointer, result.getParent());
    	assertSame(qName, result.getName());
    	assertSame(bean, UTUtil.getPrivateField(result, "bean"));
    }

    /**
     * testCreateNodePointerNodePointer03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) parent:not null<br>
     *         (引数) name:not null<br>
     *         (引数) bean:new Object()<br>
     *         (状態) bi.isDynamic():false<br>
     *         
     * <br>
     * 期待値：(戻り値) NodePointer:null<br>
     *         
     * <br>
     * Map型ではないターゲットのテスト。Nullを返す。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCreateNodePointerNodePointer03() throws Exception {
        // 前処理
    	DynamicPointerFactoryEx factory = new DynamicPointerFactoryEx();
    	QName qName = new QName("name");
    	Object bean = new Object();
    	
    	Locale locale = new Locale("ja");
    	NodePointer nodePointer = NodePointer.newNodePointer(qName, bean, locale);

        // テスト実施
    	assertNull(factory.createNodePointer(nodePointer, qName, bean));
    }

}
