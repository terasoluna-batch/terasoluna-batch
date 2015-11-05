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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.jxpath.DynamicPropertyHandler;
import org.apache.commons.jxpath.MapDynamicPropertyHandler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.beans.jxpath.DynamicPropertyPointerEx} クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * null値を扱うためのMap用プロパティポインタ拡張クラス。<br>
 * 前提条件：
 * <p>
 * 
 * @see jp.terasoluna.fw.beans.jxpath.DynamicPropertyPointerEx
 */
public class DynamicPropertyPointerExTest {

    /**
     * testGetLength01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(状態) getBaseValue():null<br>
     *         
     * <br>
     * 期待値：(戻り値) -:1<br>
     *         
     * <br>
     * 要素の値がnullの場合のテスト。１を返す。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetLength01() throws Exception {
        // 前処理
    	QName qName = new QName("name");
    	@SuppressWarnings("rawtypes")
        Map map = new HashMap();
    	map.put("key", null);
    	DynamicPropertyHandler handler = new MapDynamicPropertyHandler();
    	Locale locale = new Locale("ja");
    	NodePointer nodePointer = NodePointer.newNodePointer(qName, map, locale);
    	DynamicPropertyPointerEx pointer 
    		= new DynamicPropertyPointerEx(nodePointer, handler);
    	pointer.setPropertyName("key");

        // テスト実施
    	assertEquals(1, pointer.getLength());
    }

    /**
     * testGetLength02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(状態) getBaseValue():not null<br>
     *         (状態) ValueUtils.getLength(value):呼び出し確認を行なう<br>
     *         
     * <br>
     * 期待値：(戻り値) -:ValueUtils.getLength(value)の結果<br>
     *         
     * <br>
     * 要素がnullではない場合のテスト。ValueUtils.getLength()を返す。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetLength02() throws Exception {
        // 前処理
    	QName qName = new QName("name");
    	@SuppressWarnings("rawtypes")
        Map map = new HashMap();
    	map.put("key", new String[]{"a", "b", "c"});
    	DynamicPropertyHandler handler = new MapDynamicPropertyHandler();
    	Locale locale = new Locale("ja");
    	NodePointer nodePointer = NodePointer.newNodePointer(qName, map, locale);
    	DynamicPropertyPointerEx pointer 
    		= new DynamicPropertyPointerEx(nodePointer, handler);
    	pointer.setPropertyName("key");

        // テスト実施
    	assertEquals(3, pointer.getLength());
    }

}
