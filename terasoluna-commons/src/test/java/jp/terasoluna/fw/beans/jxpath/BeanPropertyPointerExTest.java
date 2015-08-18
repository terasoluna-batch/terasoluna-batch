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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.apache.commons.jxpath.JXPathBasicBeanInfo;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.beans.jxpath.BeanPropertyPointerEx} クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * null値を扱うためのBeanプロパティポインタ拡張クラス。<br>
 * 前提条件：
 * <p>
 * 
 * @see jp.terasoluna.fw.beans.jxpath.BeanPropertyPointerEx
 */
public class BeanPropertyPointerExTest {

    /**
     * testGetLength01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(状態) super.getLength():1<br>
     *         
     * <br>
     * 期待値：(戻り値) -:1<br>
     *         
     * <br>
     * 要素数１を返すパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetLength01() throws Exception {
        // 前処理
    	QName qName = new QName("property");
    	BeanPropertyPointerEx_JavaBeanStub01 bean 
    		= new BeanPropertyPointerEx_JavaBeanStub01();
    	Locale locale = new Locale("ja");
    	NodePointer nodePointer = NodePointer.newNodePointer(qName, bean, locale);
    	
    	JXPathBasicBeanInfo beanInfo = new JXPathBasicBeanInfo(bean.getClass());
    	BeanPropertyPointerEx pointer = new BeanPropertyPointerEx(nodePointer, beanInfo);
    	pointer.setPropertyName("property");

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
     * 入力値：(状態) super.getLength():0<br>
     *         (状態) getBaseValue():null<br>
     *         
     * <br>
     * 期待値：(戻り値) -:1<br>
     *         
     * <br>
     * 要素数０かつ値がnullのパターンのテスト。１を返す。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetLength02() throws Exception {
        // 前処理
    	QName qName = new QName("property");
    	BeanPropertyPointerEx_JavaBeanStub01 bean 
    		= new BeanPropertyPointerEx_JavaBeanStub01();
    	bean.setListProperty(null);
    	Locale locale = new Locale("ja");
    	NodePointer nodePointer = NodePointer.newNodePointer(qName, bean, locale);
    	
    	JXPathBasicBeanInfo beanInfo = new JXPathBasicBeanInfo(bean.getClass());
    	BeanPropertyPointerEx pointer = new BeanPropertyPointerEx(nodePointer, beanInfo);
    	pointer.setPropertyName("listProperty");

        // テスト実施
    	assertEquals(1, pointer.getLength());
    }

    /**
     * testGetLength03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(状態) super.getLength():0<br>
     *         (状態) getBaseValue():not null<br>
     *         
     * <br>
     * 期待値：(戻り値) -:0<br>
     *         
     * <br>
     * 要素数０かつ値がnot nullのパターンのテスト。０を返す。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetLength03() throws Exception {
        // 前処理
    	QName qName = new QName("property");
    	BeanPropertyPointerEx_JavaBeanStub01 bean 
    		= new BeanPropertyPointerEx_JavaBeanStub01();
    	Locale locale = new Locale("ja");
    	NodePointer nodePointer = NodePointer.newNodePointer(qName, bean, locale);
    	
    	JXPathBasicBeanInfo beanInfo = new JXPathBasicBeanInfo(bean.getClass());
    	BeanPropertyPointerEx pointer = new BeanPropertyPointerEx(nodePointer, beanInfo);
    	pointer.setPropertyName("listProperty");

        // テスト実施
    	assertEquals(0, pointer.getLength());
    }

    /**
     * testIsCollection01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>s
     * 入力値：(状態) getBaseValue():null<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         
     * <br>
     * 値がnullの場合のテスト。Falseを返す。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsCollection01() throws Exception {
        // 前処理
    	QName qName = new QName("property");
    	BeanPropertyPointerEx_JavaBeanStub01 bean 
    		= new BeanPropertyPointerEx_JavaBeanStub01();
    	Locale locale = new Locale("ja");
    	NodePointer nodePointer = NodePointer.newNodePointer(qName, bean, locale);
    	
    	JXPathBasicBeanInfo beanInfo = new JXPathBasicBeanInfo(bean.getClass());
    	BeanPropertyPointerEx pointer = new BeanPropertyPointerEx(nodePointer, beanInfo);
    	pointer.setPropertyName("property");

        // テスト実施
    	assertFalse(pointer.isCollection());
    }

    /**
     * testIsCollection02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(状態) getBaseValue():not null<br>
     *         (状態) super.isCollection():呼び出し確認を行なう<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:super.isCollection()の結果<br>
     *         
     * <br>
     * 値がnullではない場合のテスト。Super.isCollection()の戻り値を返す。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsCollection02() throws Exception {
        // 前処理
    	QName qName = new QName("property");
    	BeanPropertyPointerEx_JavaBeanStub01 bean 
    		= new BeanPropertyPointerEx_JavaBeanStub01();
    	Locale locale = new Locale("ja");
    	NodePointer nodePointer = NodePointer.newNodePointer(qName, bean, locale);
    	
    	JXPathBasicBeanInfo beanInfo = new JXPathBasicBeanInfo(bean.getClass());
    	BeanPropertyPointerEx pointer = new BeanPropertyPointerEx(nodePointer, beanInfo);
    	pointer.setPropertyName("listProperty");

        // テスト実施
    	assertTrue(pointer.isCollection());
    }

}
