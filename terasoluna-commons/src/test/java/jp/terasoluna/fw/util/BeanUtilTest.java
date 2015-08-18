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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import jp.terasoluna.utlib.UTUtil;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.NestedNullException;
import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.util.BeanUtil} クラスの ブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4> Bean関連のユーティリティクラス。
 * <p>
 * @see jp.terasoluna.fw.util.BeanUtil
 */
@SuppressWarnings("unused")
public class BeanUtilTest {

    /**
     * testSetBeanProperty01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:setParam1()メソッド内でException発生<br>
     * (引数) property:"param1"<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：Exception<br>
     * <br>
     * PropertyUtils#setProperty()メソッドでInvocationTargetExceptionが発生した時、 InvocationTargetExceptionがラップしていた例外インスタンスを
     * PropertyAccessExceptionでラップすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetBeanProperty01() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行
        try {
            BeanUtil.setBeanProperty(bean, "param1", "PARAM1");
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertEquals(Exception.class.getName(), e.getCause().getClass()
                    .getName());
        }
    }

    /**
     * testSetBeanProperty02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:param3を持たない<br>
     * (引数) property:"param3"<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：NoSuchMethodException<br>
     * <br>
     * PropertyUtils#setProperty()メソッドでNoSuchMethodExceptionが発生した時、PropertyAccessExceptionでラップすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetBeanProperty02() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行
        try {
            BeanUtil.setBeanProperty(bean, "param3", "PARAM3");
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertEquals(NoSuchMethodException.class.getName(), e.getCause()
                    .getClass().getName());
        }
    }

    /**
     * testSetBeanProperty03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:null<br>
     * (引数) property:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：IllegalArgumentException<br>
     * <br>
     * PropertyUtils#setProperty()メソッドでIllegalArgumentExceptionが発生した時、PropertyAccessExceptionでラップすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetBeanProperty03() throws Exception {
        // テスト実行
        try {
            BeanUtil.setBeanProperty(null, "param1", "PARAM1");
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertEquals(IllegalArgumentException.class.getName(), e.getCause()
                    .getClass().getName());
        }
    }

    /**
     * testSetBeanProperty04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:not null<br>
     * (引数) property:null<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：IllegalArgumentException<br>
     * <br>
     * PropertyUtils#setProperty()メソッドでIllegalArgumentExceptionが発生した時、PropertyAccessExceptionでラップすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetBeanProperty04() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行
        try {
            BeanUtil.setBeanProperty(bean, null, "PARAM1");
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertEquals(IllegalArgumentException.class.getName(), e.getCause()
                    .getClass().getName());
        }
    }

    /**
     * testSetBeanProperty05() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:param2=null<br>
     * (引数) property:"param2"<br>
     * (引数) value:"PARAM2"<br>
     * <br>
     * 期待値：(状態変化) bean:param2="PARAM2"<br>
     * <br>
     * PropertyUtils#setProperty()メソッドが正しく呼び出され、処理が正常に行なわれること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetBeanProperty05() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行
        BeanUtil.setBeanProperty(bean, "param2", "PARAM2");

        // テスト結果確認
        assertEquals("PARAM2", UTUtil.getPrivateField(bean, "param2"));
    }

    /**
     * testSetBeanProperty06() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:param2="PARAM2"<br>
     * (引数) property:"param2"<br>
     * (引数) value:null<br>
     * <br>
     * 期待値：(状態変化) bean:param2=null<br>
     * <br>
     * PropertyUtils#setProperty()メソッドが正しく呼び出され、処理が正常に行なわれること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetBeanProperty06() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();
        bean.setParam2("PARAM2");

        // テスト実行
        BeanUtil.setBeanProperty(bean, "param2", null);

        // テスト結果確認
        assertNull(UTUtil.getPrivateField(bean, "param2"));
    }

    /**
     * testSetBeanProperty07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:not null<br>
     * (引数) property:""<br>
     * (引数) value:"PARAM"<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：NoSuchMethodException<br>
     * <br>
     * propertyが空文字の時、PropertyAccessExceptionをスローすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetBeanProperty07() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行
        try {
            BeanUtil.setBeanProperty(bean, "", "PARAM");
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertEquals(NoSuchMethodException.class.getName(), e.getCause()
                    .getClass().getName());
        }
    }

    /**
     * testGetBeanProperty01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:getParam1()メソッド内でException発生<br>
     * (引数) property:"param1"<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：Exception<br>
     * <br>
     * PropertyUtils#getProperty()メソッドでInvocationTargetExceptionが発生した時、 InvocationTargetExceptionがラップしていた例外インスタンスを
     * PropertyAccessExceptionでラップすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanProperty01() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行
        try {
            BeanUtil.getBeanProperty(bean, "param1");
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertEquals(Exception.class.getName(), e.getCause().getClass()
                    .getName());
        }
    }

    /**
     * testGetBeanProperty02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:param3を持たない<br>
     * (引数) property:"param3"<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：NoSuchMethodException<br>
     * <br>
     * PropertyUtils#getProperty()メソッドでNoSuchMethodExceptionが発生した時、PropertyAccessExceptionでラップすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanProperty02() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行
        try {
            BeanUtil.getBeanProperty(bean, "param3");
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertEquals(NoSuchMethodException.class.getName(), e.getCause()
                    .getClass().getName());
        }
    }

    /**
     * testGetBeanProperty03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:null<br>
     * (引数) property:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：IllegalArgumentException<br>
     * <br>
     * PropertyUtils#getProperty()メソッドでIllegalArgumentExceptionが発生した時、PropertyAccessExceptionでラップすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanProperty03() throws Exception {
        // テスト実行
        try {
            BeanUtil.getBeanProperty(null, "param1");
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertEquals(IllegalArgumentException.class.getName(), e.getCause()
                    .getClass().getName());
        }
    }

    /**
     * testGetBeanProperty04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:not null<br>
     * (引数) property:null<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：IllegalArgumentException<br>
     * <br>
     * PropertyUtils#getProperty()メソッドでIllegalArgumentExceptionが発生した時、PropertyAccessExceptionでラップすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanProperty04() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行
        try {
            BeanUtil.getBeanProperty(bean, null);
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertEquals(IllegalArgumentException.class.getName(), e.getCause()
                    .getClass().getName());
        }
    }

    /**
     * testGetBeanProperty05() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:param2="PARAM2"<br>
     * (引数) property:"param2"<br>
     * <br>
     * 期待値：(戻り値) Object:"PARAM2"<br>
     * <br>
     * PropertyUtils#getProperty()メソッドが正しく呼び出され、処理が正常に行なわれること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanProperty05() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();
        bean.setParam2("PARAM2");

        // テスト実行・判定
        assertEquals("PARAM2", BeanUtil.getBeanProperty(bean, "param2"));
    }

    /**
     * testGetBeanProperty06() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:param2=null<br>
     * (引数) property:"param2"<br>
     * <br>
     * 期待値：(戻り値) Object:null<br>
     * <br>
     * PropertyUtils#getProperty()メソッドが正しく呼び出され、処理が正常に行なわれること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanProperty06() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行・判定
        assertNull(BeanUtil.getBeanProperty(bean, "param2"));
    }

    /**
     * testGetBeanProperty07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:not null<br>
     * (引数) property:""<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：NoSuchMethodException<br>
     * <br>
     * propertyが空文字の時、PropertyAccessExceptionをスローすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanProperty07() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行
        try {
            BeanUtil.getBeanProperty(bean, "");
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertEquals(NoSuchMethodException.class.getName(), e.getCause()
                    .getClass().getName());
        }
    }

    /**
     * testGetBeanPropertyType01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:getParam1()メソッド内でException発生<br>
     * (引数) property:"param1"<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：Exception<br>
     * <br>
     * PropertyUtils#getProperty()メソッドでInvocationTargetExceptionが発生した時、 InvocationTargetExceptionがラップしていた例外インスタンスを
     * PropertyAccessExceptionでラップすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanPropertyType01() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行
        try {
            BeanUtil.getBeanPropertyType(bean, "param1(0).");
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertSame(InvocationTargetException.class, e.getCause().getClass());
        }
    }

    /**
     * testGetBeanPropertyType02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:param3を持たない<br>
     * (引数) property:"param3"<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：NoSuchMethodException<br>
     * <br>
     * PropertyUtils#getProperty()メソッドでNoSuchMethodExceptionが発生した時、 PropertyAccessExceptionでラップすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanPropertyType02() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行
        assertNull(BeanUtil.getBeanPropertyType(bean, "param3"));

    }

    /**
     * testGetBeanPropertyType03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:null<br>
     * (引数) property:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：IllegalArgumentException<br>
     * <br>
     * PropertyUtils#getProperty()メソッドでIllegalArgumentExceptionが発生した時、PropertyAccessExceptionでラップすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanPropertyType03() throws Exception {
        // テスト実行
        try {
            BeanUtil.getBeanPropertyType(null, "param1");
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertEquals(IllegalArgumentException.class.getName(), e.getCause()
                    .getClass().getName());
        }
    }

    /**
     * testGetBeanPropertyType04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:not null<br>
     * (引数) property:null<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：IllegalArgumentException<br>
     * <br>
     * PropertyUtils#getProperty()メソッドでIllegalArgumentExceptionが発生した時、PropertyAccessExceptionでラップすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanPropertyType04() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行
        try {
            BeanUtil.getBeanPropertyType(bean, null);
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertEquals(IllegalArgumentException.class.getName(), e.getCause()
                    .getClass().getName());
        }
    }

    /**
     * testGetBeanPropertyType05() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:param2="PARAM2"<br>
     * (引数) property:"param2"<br>
     * <br>
     * 期待値：(戻り値) Object:"PARAM2"<br>
     * <br>
     * PropertyUtils#getProperty()メソッドが正しく呼び出され、処理が正常に行なわれること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanPropertyType05() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();
        bean.setParam2("PARAM2");

        // テスト実行・判定
        assertSame(String.class, BeanUtil.getBeanPropertyType(bean, "param2"));
    }

    /**
     * testGetBeanPropertyType06() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:param2=null<br>
     * (引数) property:"param2"<br>
     * <br>
     * 期待値：(戻り値) Object:null<br>
     * <br>
     * PropertyUtils#getProperty()メソッドが正しく呼び出され、処理が正常に行なわれること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanPropertyType06() throws Exception {
        // テスト用JavaBean生成
        BeanUtil_BeanStub01 bean = new BeanUtil_BeanStub01();

        // テスト実行・判定
        assertSame(String.class, BeanUtil.getBeanPropertyType(bean, "param2"));
    }

    /**
     * testGetBeanPropertyType07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) bean:not null<br>
     * (引数) property:""<br>
     * <br>
     * 期待値：(状態変化) 例外:PropertyAccessException：<br>
     * ラップした例外：NestedNullException<br>
     * <br>
     * propertyが空文字の時、PropertyAccessExceptionをスローすること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanPropertyType07() throws Exception {
        // テスト用JavaBean生成
        Map map = new HashMap();

        // テスト実行
        try {
            BeanUtil.getBeanPropertyType(map, "aa.(a)");
            fail();
        } catch (PropertyAccessException e) {
            // テスト結果確認
            assertEquals(NestedNullException.class.getName(), e.getCause()
                    .getClass().getName());
        }
    }

    /**
     * testGetBeanPropertyType08() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:BeanUtil_DynaBeanImpl01<br>
     * testString(String型)<br>
     * (引数) property:"testString"<br>
     * <br>
     * 期待値：(戻り値) Object:String.class<br>
     * <br>
     * DynaBeanのフィールドが配列・Collection型ではない場合 、処理が正常に行なわれること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanPropertyType08() throws Exception {
        // 前処理
        DynaProperty dynaProperty = new DynaProperty("testString", String.class);
        BeanUtil_DynaClassImpl01 dynaClass = new BeanUtil_DynaClassImpl01();
        dynaClass.setDynaProperty(dynaProperty);
        DynaBean bean = new BeanUtil_DynaBeanImpl01(dynaClass);

        // テスト実施
        // 判定
        assertEquals(String.class, BeanUtil.getBeanPropertyType(bean,
                "testString"));
    }

    /**
     * testGetBeanPropertyType09() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:BeanUtil_DynaBeanImpl01<br>
     * testArray(String[]型)<br>
     * (引数) property:"testArray"<br>
     * <br>
     * 期待値：(戻り値) Object:String[].class<br>
     * <br>
     * DynaBeanのフィールドが配列・Collection型です場合 、処理が正常に行なわれること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanPropertyType09() throws Exception {
        // 前処理
        DynaProperty dynaProperty = new DynaProperty("testArray",
                String[].class);
        BeanUtil_DynaClassImpl01 dynaClass = new BeanUtil_DynaClassImpl01();
        dynaClass.setDynaProperty(dynaProperty);
        DynaBean bean = new BeanUtil_DynaBeanImpl01(dynaClass);

        // テスト実施
        // 判定
        assertEquals(String[].class, BeanUtil.getBeanPropertyType(bean,
                "testArray"));
    }

    /**
     * testGetBeanPropertyType10() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) bean:BeanUtil_DynaBeanImpl01<br>
     * (引数) property:"testNull"<br>
     * <br>
     * 期待値：(戻り値) Object:null<br>
     * <br>
     * 指定された属性がDynaBeanに存在しない場合、処理が正常に行なわれること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBeanPropertyType10() throws Exception {
        // 前処理
        BeanUtil_DynaClassImpl01 dynaClass = new BeanUtil_DynaClassImpl01();
        dynaClass.setDynaProperty(null);
        DynaBean bean = new BeanUtil_DynaBeanImpl01(dynaClass);

        // テスト実施
        // 判定
        assertEquals(null, BeanUtil.getBeanPropertyType(bean, "testNull"));
    }
}
