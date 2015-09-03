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

import static org.junit.Assert.assertSame;

import java.util.Locale;

import java.lang.reflect.Field;
import org.apache.commons.jxpath.DynamicPropertyHandler;
import org.apache.commons.jxpath.MapDynamicPropertyHandler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.dynamic.DynamicPropertyPointer;

/**
 * {@link jp.terasoluna.fw.beans.jxpath.DynamicPointerEx} クラスのブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4> null値を扱うためのMapポインタ拡張クラス。<br>
 * 前提条件：
 * <p>
 * @see jp.terasoluna.fw.beans.jxpath.DynamicPointerEx
 */
public class DynamicPointerExTest {

    /**
     * testDynamicPointerExQname01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) name:not null<br>
     * (引数) bean:new Object()<br>
     * (引数) handler:not null<br>
     * (引数) locale:Locale("ja")<br>
     * (状態) this.handler:null<br>
     * <br>
     * 期待値：(状態変化) this.handler:引数で設定された値。<br>
     * <br>
     * コンストラクタを呼び出すテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testDynamicPointerExQname01() throws Exception {
        // 前処理
        QName qName = new QName("name");
        Object bean = new Object();
        DynamicPropertyHandler handler = new MapDynamicPropertyHandler();
        Locale locale = new Locale("ja");

        // テスト実施
        DynamicPointerEx result = new DynamicPointerEx(qName, bean, handler, locale);

        // 判定
        Field field = DynamicPointerEx.class.getDeclaredField("handler");
        field.setAccessible(true);
        Object resultHandler = field.get(result);
        assertSame(handler, resultHandler);
    }

    /**
     * testDynamicPointerExNodePointer01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) parent:not null<br>
     * (引数) name:not null<br>
     * (引数) bean:new Object()<br>
     * (引数) handler:not null<br>
     * (状態) this.handler:null<br>
     * <br>
     * 期待値：(状態変化) this.handler:引数で設定された値。<br>
     * <br>
     * コンストラクタを呼び出すテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testDynamicPointerExNodePointer01() throws Exception {
        // 前処理
        QName qName = new QName("name");
        Object bean = new Object();
        DynamicPropertyHandler handler = new MapDynamicPropertyHandler();
        Locale locale = new Locale("ja");
        NodePointer nodePointer = NodePointer.newNodePointer(qName, bean,
                locale);

        // テスト実施
        DynamicPointerEx result = new DynamicPointerEx(nodePointer, qName, bean, handler);

        // 判定
        Field field = DynamicPointerEx.class.getDeclaredField("handler");
        field.setAccessible(true);
        Object resultHandler = field.get(result);
        assertSame(handler, resultHandler);
    }

    /**
     * testGetPropertyPointer01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(状態) this.handler:not null<br>
     * <br>
     * 期待値：(戻り値) PropertyPointer:new DynamicPropertyPointerEx{<br>
     * parent=this<br>
     * handler=前提条件のhandler<br>
     * }<br>
     * <br>
     * プロパティポインタを取得するメソッドのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetPropertyPointer01() throws Exception {
        // 前処理
        QName qName = new QName("name");
        Object bean = new Object();
        DynamicPropertyHandler handler = new MapDynamicPropertyHandler();
        Locale locale = new Locale("ja");
        DynamicPointerEx pointer = new DynamicPointerEx(qName, bean, handler, locale);

        // テスト実施
        Object result = pointer.getPropertyPointer();

        // 判定
        assertSame(DynamicPropertyPointerEx.class, result.getClass());
        assertSame(pointer, ((DynamicPropertyPointerEx) result).getParent());
        Field field = DynamicPropertyPointer.class.getDeclaredField("handler");
        field.setAccessible(true);
        Object resultHandler = field.get(result);
        assertSame(handler, resultHandler);
    }

}
