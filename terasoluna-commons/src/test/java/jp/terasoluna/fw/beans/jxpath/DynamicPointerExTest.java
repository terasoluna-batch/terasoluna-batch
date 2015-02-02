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

import java.util.Locale;

import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

import org.apache.commons.jxpath.DynamicPropertyHandler;
import org.apache.commons.jxpath.MapDynamicPropertyHandler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

/**
 * {@link jp.terasoluna.fw.beans.jxpath.DynamicPointerEx} クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * null値を扱うためのMapポインタ拡張クラス。<br>
 * 前提条件：
 * <p>
 * 
 * @see jp.terasoluna.fw.beans.jxpath.DynamicPointerEx
 */
public class DynamicPointerExTest extends TestCase {


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
    public DynamicPointerExTest(String name) {
        super(name);
    }

    /**
     * testDynamicPointerExQname01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) name:not null<br>
     *         (引数) bean:new Object()<br>
     *         (引数) handler:not null<br>
     *         (引数) locale:Locale("ja")<br>
     *         (状態) this.handler:null<br>
     *         
     * <br>
     * 期待値：(状態変化) this.handler:引数で設定された値。<br>
     *         
     * <br>
     * コンストラクタを呼び出すテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testDynamicPointerExQname01() throws Exception {
        // 前処理
    	QName qName = new QName("name");
    	Object bean = new Object();
    	DynamicPropertyHandler handler = new MapDynamicPropertyHandler();
    	Locale locale = new Locale("ja");

        // テスト実施
    	DynamicPointerEx result = new DynamicPointerEx(qName, bean, handler,locale);

        // 判定
    	assertSame(handler, UTUtil.getPrivateField(result, "handler"));
    }

    /**
     * testDynamicPointerExNodePointer01()
     * <br><br>
     * 
     * (正常系) 
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) parent:not null<br>
     *         (引数) name:not null<br>
     *         (引数) bean:new Object()<br>
     *         (引数) handler:not null<br>
     *         (状態) this.handler:null<br>
     *         
     * <br>
     * 期待値：(状態変化) this.handler:引数で設定された値。<br>
     *         
     * <br>
     * コンストラクタを呼び出すテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testDynamicPointerExNodePointer01() throws Exception {
        // 前処理
    	QName qName = new QName("name");
    	Object bean = new Object();
    	DynamicPropertyHandler handler = new MapDynamicPropertyHandler();
    	Locale locale = new Locale("ja");
    	NodePointer nodePointer = NodePointer.newNodePointer(qName, bean, locale);

        // テスト実施
    	DynamicPointerEx result = new DynamicPointerEx(nodePointer, qName, bean, handler);

        // 判定
    	assertSame(handler, UTUtil.getPrivateField(result, "handler"));
    }

    /**
     * testGetPropertyPointer01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(状態) this.handler:not null<br>
     *         
     * <br>
     * 期待値：(戻り値) PropertyPointer:new DynamicPropertyPointerEx{<br>
     *                      parent=this<br>
     *                      handler=前提条件のhandler<br>
     *                  }<br>
     *         
     * <br>
     * プロパティポインタを取得するメソッドのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetPropertyPointer01() throws Exception {
        // 前処理
    	QName qName = new QName("name");
    	Object bean = new Object();
    	DynamicPropertyHandler handler = new MapDynamicPropertyHandler();
    	Locale locale = new Locale("ja");
    	DynamicPointerEx pointer = new DynamicPointerEx(qName, bean, handler,locale);

        // テスト実施
    	Object result = pointer.getPropertyPointer();

        // 判定
    	assertSame(DynamicPropertyPointerEx.class, result.getClass());
    	assertSame(pointer, ((DynamicPropertyPointerEx) result).getParent());
    	assertSame(handler, UTUtil.getPrivateField(result, "handler"));
    }

}
