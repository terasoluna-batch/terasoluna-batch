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

package jp.terasoluna.fw.beans;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.terasoluna.utlib.UTUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.WrapDynaBean;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidSyntaxException;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import org.junit.After;
import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.beans.JXPathIndexedBeanWrapperImpl} クラスのブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4> JavaBeanの配列・コレクション型属性にアクセスできるクラス。<br>
 * 前提条件：
 * <p>
 * @see jp.terasoluna.fw.beans.JXPathIndexedBeanWrapperImpl
 */
public class JXPathIndexedBeanWrapperImplTest01 {

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            JXPathIndexedBeanWrapperImpl.class);

    /**
     * 終了処理を行う。
     */
    @After
    public void tearDown() {
        logger.clear();
    }

    /**
     * testJXPathIndexedBeanWrapperImpl01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) obj:not null<br>
     * (状態) this.context:null<br>
     * <br>
     * 期待値：(状態変化) this.context:引数で設定された値。<br>
     * <br>
     * ターゲットとなるJavaBeanをcontext属性に設定するコンストラクタのテスト。正常ケース。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testJXPathIndexedBeanWrapperImpl01() throws Exception {
        // 前処理
        Object obj = new Object();

        // テスト実施
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(obj);

        // 判定
        JXPathContext context = (JXPathContext) UTUtil.getPrivateField(bw,
                "context");
        assertSame(obj, context.getContextBean());
    }

    /**
     * testJXPathIndexedBeanWrapperImpl02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) obj:null<br>
     * (状態) this.context:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException("TargetBean is null!")<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * TargetBean is null!<br>
     * <br>
     * 引数のJavaBeanがNullの場合のテスト。例外を投げる。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testJXPathIndexedBeanWrapperImpl02() throws Exception {
        // 前処理
        try {
            new JXPathIndexedBeanWrapperImpl(null);
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals("TargetBean is null!", e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "TargetBean is null!"))));
        }
    }

    /**
     * testGetIndexedPropertyValuesJavaBean01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) propertyName:null<br>
     * (状態) this.context:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException("PropertyName is empty!")<br>
     * (状態変化) ログ:ログレベル：エラーPropertyName is empty!<br>
     * <br>
     * 引数のプロパティ名がNullのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean01() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(new Object());

        // テスト実施
        try {
            bw.getIndexedPropertyValues(null);
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals("PropertyName is empty!", e.getMessage());
            logger.setEnabledLevels(Level.ERROR);
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "PropertyName is empty!"))));
        }
    }

    /**
     * testGetIndexedPropertyValuesJavaBean02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) propertyName:""（空文字）<br>
     * (状態) this.context:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException("PropertyName is empty!")<br>
     * (状態変化) ログ:ログレベル：エラーPropertyName is empty!<br>
     * <br>
     * 引数のプロパティ名が空文字のテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean02() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(new Object());

        // テスト実施
        try {
            bw.getIndexedPropertyValues("");
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals("PropertyName is empty!", e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "PropertyName is empty!"))));
        }
    }

    /**
     * testGetIndexedPropertyValuesJavaBean03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) propertyName:"."(ドット１つ）<br>
     * (状態) this.context:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ：Property name is null or blank.<br>
     * 原因例外：JXPathException<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Property name is null or blank.<br>
     * <br>
     * 不正なプロパティ名が入れられた場合のテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean03() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(new Object());

        // テスト実施
        try {
            bw.getIndexedPropertyValues(".");
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals("Property name is null or blank.", e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Property name is null or blank."))));
        }
    }

    /**
     * testGetIndexedPropertyValuesJavaBean04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) propertyName:"/"<br>
     * (状態) this.context:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ：Invalid character has found within property name. '/' Cannot use [ / " ' ]<br>
     * 原因例外：JXPathException<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Invalid character has found within property name. '/' Cannot use [ / " ' ]<br>
     * <br>
     * プロパティ名に/（スラッシュ）が入っているバターン <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean04() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(new Object());

        // テスト実施
        try {
            bw.getIndexedPropertyValues("/");
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            String expect = "Invalid character has found within property name. "
                    + "'/' Cannot use [ / \" ' ]";
            assertEquals(expect, e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(expect))));
        }
    }

    /**
     * testGetIndexedPropertyValuesJavaBean05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) propertyName:"["<br>
     * (状態) this.context:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ：Cannot get Index. Invalid property name. '['<br>
     * 原因例外：JXPathException<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Cannot get Index. Invalid property name. '['<br>
     * <br>
     * プロパティ名に[（配列の記号）が入っているバターン <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean05() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(new Object());

        // テスト実施
        try {
            bw.getIndexedPropertyValues("[");
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            String expect = "Cannot get Index. Invalid property name. '['";
            assertEquals(expect, e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(expect))));
        }
    }

    /**
     * testGetIndexedPropertyValuesJavaBean06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) propertyName:"[a]"<br>
     * (状態) this.context:not null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ：Invalid character has found within property name. '[a]' Cannot use [ [] ]<br>
     * 原因例外：JXPathException<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Invalid character has found within property name. '[a]' Cannot use [ [] ]<br>
     * <br>
     * プロパティ名に]（配列の記号）が入っているバターン <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean06() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(new Object());

        // テスト実施
        try {
            bw.getIndexedPropertyValues("[a]");
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            String expect = "Invalid character has found within property name. '[a]' Cannot use [ [] ]";
            assertEquals(expect, e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(expect))));
        }
    }

    /**
     * testGetIndexedPropertyValuesJavaBean07() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) propertyName:"property"<br>
     * (状態) this.context:JavaBean｛<br>
     * (プロパティ名=値)<br>
     * property=null<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * property=null<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * null値の値を取得するテスト。(Object型） <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean07() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        bean.setProperty(null);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("property");

        // 判定
        assertEquals(1, result.size());
        assertNull(result.get("property"));
        assertNull(PropertyUtils.getProperty(bean, "property"));
        assertNull(BeanUtils.getProperty(bean, "property"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean07() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) propertyName:"property"<br>
     * (状態) this.context:JavaBean｛<br>
     * (プロパティ名=値)<br>
     * property=null<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * property=null<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * null値の値を取得するテスト。(String型） <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean07_2() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        bean.setProperty2(null);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("property2");

        // 判定
        assertEquals(1, result.size());
        assertTrue(result.containsKey("property2"));
        assertNull(result.get("property2"));
        assertNull(PropertyUtils.getProperty(bean, "property2"));
        assertNull(BeanUtils.getProperty(bean, "property2"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean07() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) propertyName:"property"<br>
     * (状態) this.context:JavaBean｛<br>
     * (プロパティ名=値)<br>
     * property=null<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * property=null<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * null値の値を取得するテスト。(Date型） <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean07_3() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        bean.setProperty3(null);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("property3");

        // 判定
        assertEquals(1, result.size());
        assertTrue(result.containsKey("property3"));
        assertNull(result.get("property3"));
        assertNull(PropertyUtils.getProperty(bean, "property3"));
        assertNull(BeanUtils.getProperty(bean, "property3"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean07() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) propertyName:"property"<br>
     * (状態) this.context:JavaBean｛<br>
     * (プロパティ名=値)<br>
     * property=null<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * property=null<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * null値の値を取得するテスト。(List型） <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean07_4() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        bean.setProperty4(null);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("property4");

        // 判定
        assertEquals(1, result.size());
        assertTrue(result.containsKey("property4"));
        assertNull(result.get("property4"));
        assertNull(PropertyUtils.getProperty(bean, "property4"));
        assertNull(BeanUtils.getProperty(bean, "property4"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean07() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) propertyName:"property"<br>
     * (状態) this.context:JavaBean｛<br>
     * (プロパティ名=値)<br>
     * property=null<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * property=null<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * null値の値を取得するテスト。(int[]型） <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean07_5() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        bean.setProperty5(null);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("property5");

        // 判定
        assertEquals(1, result.size());
        assertTrue(result.containsKey("property5"));
        assertNull(result.get("property5"));
        assertNull(PropertyUtils.getProperty(bean, "property5"));
        assertNull(BeanUtils.getProperty(bean, "property5"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean07() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) propertyName:"property"<br>
     * (状態) this.context:JavaBean｛<br>
     * (プロパティ名=値)<br>
     * property=new ArrayList()<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * <br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * 空のリスト値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean07_6() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        bean.setProperty4(new ArrayList());

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("property4");

        // 判定
        assertEquals(0, result.size());
    }

    /**
     * testGetIndexedPropertyValuesJavaBean07() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) propertyName:"property"<br>
     * (状態) this.context:JavaBean｛<br>
     * (プロパティ名=値)<br>
     * property=new int[]<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * <br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * 空のint配列を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean07_7() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        bean.setProperty5(new int[] {});

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("property5");

        // 判定
        assertEquals(0, result.size());
    }

    /**
     * testGetIndexedPropertyValuesJavaBean07() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) propertyName:"property"<br>
     * (状態) this.context:JavaBean｛<br>
     * (プロパティ名=値)<br>
     * property=null<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * property=null<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * null値の値を取得するテスト。(List型） <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean07_8() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        bean.setObjectArray(null);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("objectArray");

        // 判定
        assertEquals(1, result.size());
        assertTrue(result.containsKey("objectArray"));
        assertNull(result.get("objectArray"));
        assertNull(PropertyUtils.getProperty(bean, "objectArray"));
        assertNull(BeanUtils.getProperty(bean, "objectArray"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean08() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) propertyName:"foo"<br>
     * (状態) this.context:JavaBean｛<br>
     * （プロパティ名=値）<br>
     * foo="test"<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * foo="test"<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * String型の値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean08() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        bean.setProperty("test");

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> map = bw.getIndexedPropertyValues("property");

        // 判定
        assertEquals(1, map.size());
        assertTrue(map.containsKey("property"));
        assertEquals("test", map.get("property"));
        assertEquals("test", PropertyUtils.getProperty(bean, "property"));
        assertEquals("test", BeanUtils.getProperty(bean, "property"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean09() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:"foo"<br>
     * (状態) this.context:JavaBean｛<br>
     * （プロパティ名=値）<br>
     * foo=false<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * foo=false<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * boolean型の値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean09() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        bean.setBoolProperty(false);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "boolProperty");

        // 判定
        assertEquals(1, result.size());
        assertTrue(result.containsKey("boolProperty"));
        assertFalse((Boolean) result.get("boolProperty"));
        assertEquals("false", PropertyUtils.getProperty(bean, "boolProperty")
                .toString());
        assertEquals("false", BeanUtils.getProperty(bean, "boolProperty"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean10() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:"foo.bar.hoge"<br>
     * (状態) this.context:JavaBean｛<br>
     * （プロパティ名=値）<br>
     * foo.bar.hoge="test"<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * foo.bar.hoge="test"<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * ネストした値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean10() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub03 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub03();
        // foo
        JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Foo foo = new JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Foo();

        // bar
        JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Bar bar = new JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Bar();
        // hoge
        bar.setHoge("test");

        // foo.bar.hoge
        foo.setBar(bar);
        bean.setFoo(foo);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "foo.bar.hoge");

        // 判定
        assertEquals(1, result.size());
        assertTrue(result.containsKey("foo.bar.hoge"));
        assertEquals("test", result.get("foo.bar.hoge"));
        assertEquals("test", PropertyUtils.getProperty(bean, "foo.bar.hoge"));
        assertEquals("test", BeanUtils.getProperty(bean, "foo.bar.hoge"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean11() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:"foo.bar.hoge"<br>
     * (状態) this.context:JavaBean｛<br>
     * （プロパティ名=値）<br>
     * foo[0].bar.hoge="test0"<br>
     * foo[1].bar.hoge="test1"<br>
     * foo[2].bar.hoge="test2"<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * foo[0].bar.hoge="test0"<br>
     * foo[1].bar.hoge="test1"<br>
     * foo[2].bar.hoge="test2"<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * オブジェクト配列の値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean11() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04();

        // foos[0]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo1 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();

        // foos[1]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo2 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();

        // foos[2]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo3 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();

        // foos
        bean.setFoos(new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo[] {
                foo1, foo2, foo3 });

        // foos[0].bar
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar bar1 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar();
        foo1.setBar(bar1);

        // foos[1].bar
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar bar2 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar();
        foo2.setBar(bar2);

        // foos[2].bar
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar bar3 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar();
        foo3.setBar(bar3);

        // foos[0].bar.hoge="test0"
        bar1.setHoge("test0");
        // foos[1].bar.hoge="test1"
        bar2.setHoge("test1");
        // foos[2].bar.hoge="test2"
        bar3.setHoge("test2");

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "foos.bar.hoge");

        // 判定
        assertEquals(3, result.size());
        assertEquals("test0", result.get("foos[0].bar.hoge"));
        assertEquals("test1", result.get("foos[1].bar.hoge"));
        assertEquals("test2", result.get("foos[2].bar.hoge"));
        assertEquals("test0", PropertyUtils.getProperty(bean,
                "foos[0].bar.hoge"));
        assertEquals("test0", BeanUtils.getProperty(bean, "foos[0].bar.hoge"));
        assertEquals("test1", PropertyUtils.getProperty(bean,
                "foos[1].bar.hoge"));
        assertEquals("test1", BeanUtils.getProperty(bean, "foos[1].bar.hoge"));
        assertEquals("test2", PropertyUtils.getProperty(bean,
                "foos[2].bar.hoge"));
        assertEquals("test2", BeanUtils.getProperty(bean, "foos[2].bar.hoge"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean12() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:"foo.bar.hoge"<br>
     * (状態) this.context:JavaBean｛<br>
     * （プロパティ名=値）<br>
     * foo[0].bar.hoge="test0"<br>
     * foo[1].bar.hoge="test1"<br>
     * foo[2].bar.hoge="test2"<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * foo[0].bar.hoge="test0"<br>
     * foo[1].bar.hoge="test1"<br>
     * foo[2].bar.hoge="test2"<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * List値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean12() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04();

        // foo
        List<JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo> fooList = new ArrayList<JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo>();

        // foo[0],foo[1],foo[2]
        // foo[0]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo1 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();
        fooList.add(foo1);

        // foo[1]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo2 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();
        fooList.add(foo2);

        // foo[2]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo3 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();
        fooList.add(foo3);

        // foo[0].bar
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar bar1 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar();
        foo1.setBar(bar1);

        // foo[1].bar
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar bar2 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar();
        foo2.setBar(bar2);

        // foo[2].bar
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar bar3 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar();
        foo3.setBar(bar3);

        // foo[0].bar.hoge="test0"
        bar1.setHoge("test0");
        // foo[1].bar.hoge="test1"
        bar2.setHoge("test1");
        // foo[2].bar.hoge="test2"
        bar3.setHoge("test2");

        bean.setFoo(fooList);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "foo.bar.hoge");

        // 判定
        assertEquals(3, result.size());
        assertEquals("test0", result.get("foo[0].bar.hoge"));
        assertEquals("test1", result.get("foo[1].bar.hoge"));
        assertEquals("test2", result.get("foo[2].bar.hoge"));
        assertEquals("test0", PropertyUtils.getProperty(bean,
                "foo[0].bar.hoge"));
        assertEquals("test0", BeanUtils.getProperty(bean, "foo[0].bar.hoge"));
        assertEquals("test1", PropertyUtils.getProperty(bean,
                "foo[1].bar.hoge"));
        assertEquals("test1", BeanUtils.getProperty(bean, "foo[1].bar.hoge"));
        assertEquals("test2", PropertyUtils.getProperty(bean,
                "foo[2].bar.hoge"));
        assertEquals("test2", BeanUtils.getProperty(bean, "foo[2].bar.hoge"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean13() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:"foo.bar.hoge"<br>
     * (状態) this.context:JavaBean｛<br>
     * （プロパティ名=値）<br>
     * foo[0].bar.hoge=null<br>
     * foo[1].bar.hoge="test1"<br>
     * foo[2].bar.hoge="test2"<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * foo[0].bar.hoge=null<br>
     * foo[1].bar.hoge="test1"<br>
     * foo[2].bar.hoge="test2"<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * 配列＋null混じりの値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean13() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04();

        // foo
        List<JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo> fooList = new ArrayList<JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo>();

        // foo[0],foo[1],foo[2]
        // foo[0]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo1 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();
        fooList.add(foo1);

        // foo[1]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo2 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();
        fooList.add(foo2);

        // foo[2]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo3 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();
        fooList.add(foo3);

        // foo[0].bar
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar bar1 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar();
        foo1.setBar(bar1);

        // foo[1].bar
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar bar2 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar();
        foo2.setBar(bar2);

        // foo[2].bar
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar bar3 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar();
        foo3.setBar(bar3);

        // foo[0].bar.hoge=null
        bar1.setHoge(null);
        // foo[1].bar.hoge="test1"
        bar2.setHoge("test1");
        // foo[2].bar.hoge="test2"
        bar3.setHoge("test2");

        bean.setFoo(fooList);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "foo.bar.hoge");

        // 判定
        assertEquals(3, result.size());
        assertNull(result.get("foo[0].bar.hoge"));
        assertEquals("test1", result.get("foo[1].bar.hoge"));
        assertEquals("test2", result.get("foo[2].bar.hoge"));
        assertNull(PropertyUtils.getProperty(bean, "foo[0].bar.hoge"));
        assertNull(BeanUtils.getProperty(bean, "foo[0].bar.hoge"));
        assertEquals("test1", PropertyUtils.getProperty(bean,
                "foo[1].bar.hoge"));
        assertEquals("test1", BeanUtils.getProperty(bean, "foo[1].bar.hoge"));
        assertEquals("test2", PropertyUtils.getProperty(bean,
                "foo[2].bar.hoge"));
        assertEquals("test2", BeanUtils.getProperty(bean, "foo[2].bar.hoge"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean14() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:"foo.bar.hoge"<br>
     * (状態) this.context:JavaBean｛<br>
     * （プロパティ名=値）<br>
     * foo.bar[0].hoge[0]="test0"<br>
     * foo.bar[1].hoge[0]="test1"<br>
     * foo.bar[2].hoge[0]="test2"<br>
     * foo.bar[2].hoge[1]="test3"<br>
     * foo.bar[2].hoge[2]="test4"<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * foo.bar[0].hoge[0]="test0"<br>
     * foo.bar[1].hoge[0]="test1"<br>
     * foo.bar[2].hoge[0]="test2"<br>
     * foo.bar[2].hoge[1]="test3"<br>
     * foo.bar[2].hoge[2]="test4"<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * ネスト＋配列の値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean14() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub05();

        // foo
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Foo foo = new JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Foo();
        bean.setFoo(foo);

        // foo.bar[0]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar bar0 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar();
        // foo.bar[1]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar bar1 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar();
        // foo.bar[2]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar bar2 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar();
        // foo.bar[]
        List<JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar> barList = new ArrayList<JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar>();
        // foo.bar[0]
        barList.add(bar0);
        // foo.bar[1]
        barList.add(bar1);
        // foo.bar[2]
        barList.add(bar2);

        // foo.bar
        foo.setBar(barList);

        // foo.bar[0].hoge[0]="test0"
        List<String> hoge0 = new ArrayList<String>();
        hoge0.add("test0");
        bar0.setHoge(hoge0);

        // foo.bar[1].hoge[0]="test1"
        List<String> hoge1 = new ArrayList<String>();
        hoge1.add("test1");
        bar1.setHoge(hoge1);

        // foo.bar[2].hoge[0]="test2"
        // foo.bar[2].hoge[1]="test3"
        // foo.bar[2].hoge[2]="test4"
        List<String> hoge2 = new ArrayList<String>();
        hoge2.add("test2");
        hoge2.add("test3");
        hoge2.add("test4");
        bar2.setHoge(hoge2);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "foo.bar.hoge");

        // 判定
        assertEquals(5, result.size());
        assertEquals("test0", result.get("foo.bar[0].hoge[0]"));
        assertEquals("test1", result.get("foo.bar[1].hoge[0]"));
        assertEquals("test2", result.get("foo.bar[2].hoge[0]"));
        assertEquals("test3", result.get("foo.bar[2].hoge[1]"));
        assertEquals("test4", result.get("foo.bar[2].hoge[2]"));
        assertEquals("test0", PropertyUtils.getProperty(bean,
                "foo.bar[0].hoge[0]"));
        assertEquals("test0", BeanUtils.getProperty(bean,
                "foo.bar[0].hoge[0]"));
        assertEquals("test1", PropertyUtils.getProperty(bean,
                "foo.bar[1].hoge[0]"));
        assertEquals("test1", BeanUtils.getProperty(bean,
                "foo.bar[1].hoge[0]"));
        assertEquals("test2", PropertyUtils.getProperty(bean,
                "foo.bar[2].hoge[0]"));
        assertEquals("test2", BeanUtils.getProperty(bean,
                "foo.bar[2].hoge[0]"));
        assertEquals("test3", PropertyUtils.getProperty(bean,
                "foo.bar[2].hoge[1]"));
        assertEquals("test3", BeanUtils.getProperty(bean,
                "foo.bar[2].hoge[1]"));
        assertEquals("test4", PropertyUtils.getProperty(bean,
                "foo.bar[2].hoge[2]"));
        assertEquals("test4", BeanUtils.getProperty(bean,
                "foo.bar[2].hoge[2]"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean15() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:"foo.bar.hoge"<br>
     * (状態) this.context:JavaBean｛<br>
     * （プロパティ名=値）<br>
     * foo.bar[0].hoge[0]="test0"<br>
     * foo.bar[1]=null<br>
     * foo.bar[2].hoge[0]="test2"<br>
     * foo.bar[2].hoge[1]="test3"<br>
     * foo.bar[2].hoge[2]="test4"<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * foo.bar[0].hoge[0]="test0"<br>
     * foo.bar[2].hoge[0]="test2"<br>
     * foo.bar[2].hoge[1]="test3"<br>
     * foo.bar[2].hoge[2]="test4"<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * ネスト＋配列＋nullが混じるパターンの値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean15() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub05();

        // foo
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Foo foo = new JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Foo();
        bean.setFoo(foo);

        // foo.bar[0]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar bar0 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar();
        // foo.bar[1]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar bar1 = null;
        // foo.bar[2]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar bar2 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar();
        // foo.bar[]
        List<JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar> barList = new ArrayList<JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar>();
        // foo.bar[0]
        barList.add(bar0);
        // foo.bar[1]
        barList.add(bar1);
        // foo.bar[2]
        barList.add(bar2);

        // foo.bar
        foo.setBar(barList);

        // foo.bar[0].hoge[0]="test0"
        List<String> hoge0 = new ArrayList<String>();
        hoge0.add("test0");
        bar0.setHoge(hoge0);

        // foo.bar[1]=null

        // foo.bar[2].hoge[0]="test2"
        // foo.bar[2].hoge[1]="test3"
        // foo.bar[2].hoge[2]="test4"
        List<String> hoge2 = new ArrayList<String>();
        hoge2.add("test2");
        hoge2.add("test3");
        hoge2.add("test4");
        bar2.setHoge(hoge2);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "foo.bar.hoge");

        // 判定
        assertEquals(4, result.size());
        assertEquals("test0", result.get("foo.bar[0].hoge[0]"));
        assertEquals("test2", result.get("foo.bar[2].hoge[0]"));
        assertEquals("test3", result.get("foo.bar[2].hoge[1]"));
        assertEquals("test4", result.get("foo.bar[2].hoge[2]"));

        assertEquals("test0", PropertyUtils.getProperty(bean,
                "foo.bar[0].hoge[0]"));
        assertEquals("test0", BeanUtils.getProperty(bean,
                "foo.bar[0].hoge[0]"));
        assertEquals("test2", PropertyUtils.getProperty(bean,
                "foo.bar[2].hoge[0]"));
        assertEquals("test2", BeanUtils.getProperty(bean,
                "foo.bar[2].hoge[0]"));
        assertEquals("test3", PropertyUtils.getProperty(bean,
                "foo.bar[2].hoge[1]"));
        assertEquals("test3", BeanUtils.getProperty(bean,
                "foo.bar[2].hoge[1]"));
        assertEquals("test4", PropertyUtils.getProperty(bean,
                "foo.bar[2].hoge[2]"));
        assertEquals("test4", BeanUtils.getProperty(bean,
                "foo.bar[2].hoge[2]"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean16() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:"map（key）"<br>
     * (状態) this.context:JavaBean｛<br>
     * （プロパティ名=値）<br>
     * map=Map{<br>
     * key="test"<br>
     * }<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * map(key)="test"<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * Map型の属性を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean16() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "test");
        // map.put("key", null);
        bean.setMap(map);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("map(key)");

        // 判定
        assertEquals(1, result.size());
        assertEquals("test", result.get("map(key)"));
        assertEquals("test", PropertyUtils.getProperty(bean, "map(key)"));
        assertEquals("test", BeanUtils.getProperty(bean, "map(key)"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean17() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:"map（key）"<br>
     * (状態) this.context:JavaBean｛<br>
     * （プロパティ名=値）<br>
     * map=Map{<br>
     * key=(List)<br>
     * ∟[0] = 1<br>
     * [1] = 2<br>
     * [2] = 3<br>
     * }<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * map(key)[0]=1<br>
     * map(key)[1]=2<br>
     * map(key)[2]=3<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * Map型の属性からList値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean17() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", list);
        bean.setMap(map);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("map(key)");

        // 判定
        assertEquals(3, result.size());
        assertEquals(1, result.get("map(key)[0]"));
        assertEquals(2, result.get("map(key)[1]"));
        assertEquals(3, result.get("map(key)[2]"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean18() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:"map（key）.value"<br>
     * (状態) this.context:JavaBean｛<br>
     * （プロパティ名=値）<br>
     * map=Map{<br>
     * key=new Bean()<br>
     * ∟value="test"<br>
     * }<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * map(key).value="test"<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * ネストしたMap型の属性を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean18() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 subBean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        subBean.setProperty("test");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", subBean);
        bean.setMap(map);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "map(key).property");

        // 判定
        assertEquals(1, result.size());
        assertEquals("test", result.get("map(key).property"));
        assertEquals("test", PropertyUtils.getProperty(bean,
                "map(key).property"));
        assertEquals("test", BeanUtils.getProperty(bean, "map(key).property"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean19() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:"aaa"<br>
     * (状態) this.context:JavaBean｛<br>
     * （プロパティ名=値）<br>
     * foo="test"<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * 空のマップ<br>
     * <br>
     * 【JavaBeanのテスト】<br>
     * 存在しないプロパティ名にアクセスしたときのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesJavaBean19() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub01 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub01();
        bean.setProperty("test");

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(bean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("aaa");

        // 判定
        assertEquals(0, result.size());
    }

    /**
     * testGetIndexedPropertyValuesMap01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:key<br>
     * (状態) this.context:Map<String, Object><br>
     * key="value"<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * key="value"<br>
     * <br>
     * 【Map型のテスト】<br>
     * String型の値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap01() throws Exception {
        // 前処理
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", "value");

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(map);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("key");

        // 判定
        assertEquals(1, result.size());
        assertEquals("value", result.get("key"));
        assertEquals("value", PropertyUtils.getProperty(map, "key"));
        assertEquals("value", BeanUtils.getProperty(map, "key"));
    }

    /**
     * testGetIndexedPropertyValuesMap02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:key<br>
     * (状態) this.context:Map<String, Object><br>
     * key=null<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * key=null<br>
     * <br>
     * 【Map型のテスト】<br>
     * null値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap02() throws Exception {
        // 前処理
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", null);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(map);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("key");

        // 判定
        assertEquals(1, result.size());
        assertNull(result.get("key"));
        assertNull(PropertyUtils.getProperty(map, "key"));
        assertNull(BeanUtils.getProperty(map, "key"));
    }

    /**
     * testGetIndexedPropertyValuesMap03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:key<br>
     * (状態) this.context:Map<String, Object><br>
     * 空のMap<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * 空のMap<br>
     * <br>
     * 【Map型のテスト】<br>
     * 存在しないキー名をもとにオブジェクトを取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap03() throws Exception {
        // 前処理
        Map<String, Object> map = new HashMap<String, Object>();

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(map);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("key");

        // 判定
        assertEquals(0, result.size());
    }

    /**
     * testGetIndexedPropertyValuesMap04() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:ints<br>
     * (状態) this.context:Map<String, Object><br>
     * ints=int[]{1,2,3}<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * ints[0]=1<br>
     * ints[1]=2<br>
     * ints[2]=3<br>
     * <br>
     * 【Map型のテスト】<br>
     * int型配列を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap04() throws Exception {
        // 前処理
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ints", new int[] { 1, 2, 3 });

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(map);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("ints");

        // 判定
        assertEquals(3, result.size());
        assertEquals(1, result.get("ints[0]"));
        assertEquals(2, result.get("ints[1]"));
        assertEquals(3, result.get("ints[2]"));
    }

    /**
     * testGetIndexedPropertyValuesMap05() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:numbers<br>
     * (状態) this.context:Map<String, Object><br>
     * numbers=Long[]{1,2,3}<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * numbers[0]=1<br>
     * numbers[1]=2<br>
     * numbers[2]=3<br>
     * <br>
     * 【Map型のテスト】<br>
     * Long型配列を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap05() throws Exception {
        // 前処理
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("numbers", new Long[] { new Long(1), new Long(2),
                new Long(3) });

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(map);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("numbers");

        // 判定
        assertEquals(3, result.size());
        assertEquals(new Long(1), result.get("numbers[0]"));
        assertEquals(new Long(2), result.get("numbers[1]"));
        assertEquals(new Long(3), result.get("numbers[2]"));
    }

    /**
     * testGetIndexedPropertyValuesMap06() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:numbers<br>
     * (状態) this.context:Map<String, Object><br>
     * numbers=List{1,2,3}<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * numbers[0]=1<br>
     * numbers[1]=2<br>
     * numbers[2]=3<br>
     * <br>
     * 【Map型のテスト】<br>
     * Long型Listを取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap06() throws Exception {
        // 前処理
        Map<String, Object> map = new HashMap<String, Object>();
        List<Long> list = new ArrayList<Long>();
        list.add(new Long(1));
        list.add(new Long(2));
        list.add(new Long(3));
        map.put("numbers", list);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(map);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues("numbers");

        // 判定
        assertEquals(3, result.size());
        assertEquals(new Long(1), result.get("numbers[0]"));
        assertEquals(new Long(2), result.get("numbers[1]"));
        assertEquals(new Long(3), result.get("numbers[2]"));
    }

    /**
     * testGetIndexedPropertyValuesMap07() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:bean.subBean.property<br>
     * (状態) this.context:Map<String, Object><br>
     * foo=new Foo()<br>
     * ∟bar=new Bar()<br>
     * ∟hoge="value"<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * bean.subBean.property="value"<br>
     * <br>
     * 【Map型のテスト】<br>
     * ネストしたプロパティを取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap07() throws Exception {
        // 前処理
        Map<String, Object> map = new HashMap<String, Object>();

        // bar="value"
        JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Bar bar = new JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Bar();
        bar.setHoge("value");

        // foo
        JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Foo foo = new JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Foo();
        foo.setBar(bar);

        map.put("foo", foo);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(map);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "foo.bar.hoge");

        // 判定
        assertEquals(1, result.size());
        assertEquals("value", result.get("foo.bar.hoge"));
        assertEquals("value", PropertyUtils.getProperty(map, "foo.bar.hoge"));
        assertEquals("value", BeanUtils.getProperty(map, "foo.bar.hoge"));
    }

    /**
     * testGetIndexedPropertyValuesMap08() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:beans.subBean.property<br>
     * (状態) this.context:Map<String, Object><br>
     * foo=new Foo[3]<br>
     * ∟[0] new Foo()<br>
     * ∟bar=new Bar()<br>
     * ∟hoge="value1"<br>
     * ∟[1] null<br>
     * ∟[2] new Foo()<br>
     * ∟bar=new Bar()<br>
     * ∟hoge="value2"<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * foo[0].bar.hoge="value1"<br>
     * foo[2].bar.hoge="value3"<br>
     * <br>
     * 【Map型のテスト】<br>
     * ネスト＋配列プロパティを取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap08() throws Exception {
        // 前処理
        Map<String, Object> map = new HashMap<String, Object>();

        // foo
        List<JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo> fooList = new ArrayList<JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo>();

        // foo[0]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo0 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();
        fooList.add(foo0);

        // foo[1]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo1 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();
        fooList.add(foo1);

        // foo[2]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo2 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();
        fooList.add(foo2);

        // foo[0].bar.hoge="value"
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar bar0 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar();
        bar0.setHoge("value1");
        foo0.setBar(bar0);

        // foo[1].bar=null
        foo1.setBar(null);

        // foo[2].bar.hoge="value"
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar bar2 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Bar();
        bar2.setHoge("value3");
        foo2.setBar(bar2);

        map.put("foo", fooList);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(map);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "foo.bar.hoge");

        // 判定
        assertEquals(2, result.size());
        assertEquals("value1", result.get("foo[0].bar.hoge"));
        assertEquals("value3", result.get("foo[2].bar.hoge"));
    }

    /**
     * testGetIndexedPropertyValuesMap09() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:beans.property<br>
     * (状態) this.context:Map<String, Object><br>
     * foo=new Bean[3]<br>
     * ∟[0] new Bean()<br>
     * ∟property="value1"<br>
     * ∟[1] new Bean()<br>
     * ∟property=null<br>
     * ∟[2] new Bean()<br>
     * ∟property="value3"<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * beans[0].property="value1"<br>
     * beans[1].property=null<br>
     * beans[2].property="value3"<br>
     * <br>
     * 【Map型のテスト】<br>
     * 配列＋null混じりのプロパティを取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap09() throws Exception {
        // 前処理
        Map<String, Object> map = new HashMap<String, Object>();

        // foo
        List<JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo> fooList = new ArrayList<JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo>();

        // foo[0]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo0 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();
        foo0.setProperty("value1");
        fooList.add(foo0);

        // foo[1]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo1 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();
        foo1.setProperty(null);
        fooList.add(foo1);

        // foo[2]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo foo2 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub04.Foo();
        foo2.setProperty("value3");
        fooList.add(foo2);

        map.put("foo", fooList);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(map);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "foo.property");

        // 判定
        assertEquals(3, result.size());
        assertEquals("value1", result.get("foo[0].property"));
        assertNull(result.get("foo[1].property"));
        assertEquals("value3", result.get("foo[2].property"));
    }

    /**
     * testGetIndexedPropertyValuesMap10() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) propertyName:aa/aa.bbb<br>
     * (状態) this.context:Map<String, Object><br>
     * 空のMap<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ：Invalid character has found within property name. 'aa/aa.bbb' Cannot use [ / " ' ]<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Invalid character has found within property name. 'aa/aa.bbb' Cannot use [ / " ' ]<br>
     * <br>
     * 【Map型のテスト】<br>
     * 不正なMapキーのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap10() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(new HashMap<String, Object>());

        // テスト実施
        try {
            bw.getIndexedPropertyValues("aa/aa.bbb");
            fail();
        } catch (IllegalArgumentException e) {
            // OK
            String expect = "Invalid character has found within property name. 'aa/aa.bbb' Cannot use [ / \" ' ]";
            assertEquals(expect, e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(expect))));
        }
    }

    /**
     * testGetIndexedPropertyValuesMap11() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) propertyName:aa[aa.bbb<br>
     * (状態) this.context:Map<String, Object><br>
     * 空のMap<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     * メッセージ：Cannot get Index. Invalid property name. 'aa[aa.bbb'<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Cannot get Index. Invalid property name. 'aa[aa.bbb'<br>
     * <br>
     * 【Map型のテスト】<br>
     * 不正なMapキーのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap11() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(new HashMap<String, Object>());

        // テスト実施
        try {
            bw.getIndexedPropertyValues("aa[aa.bbb");
            fail();
        } catch (IllegalArgumentException e) {
            // OK
            String expect = "Cannot get Index. Invalid property name. 'aa[aa.bbb'";
            assertEquals(expect, e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(expect))));
        }
    }

    /**
     * testGetIndexedPropertyValuesMap12() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) propertyName:aa]aa.bbb<br>
     * (状態) this.context:Map<String, Object><br>
     * 空のMap<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     * メッセージ：Cannot get Index. Invalid property name. 'aa]aa.bbb'<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Cannot get Index. Invalid property name. 'aa]aa.bbb'<br>
     * <br>
     * 【Map型のテスト】<br>
     * 不正なMapキーのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap12() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(new HashMap<String, Object>());

        // テスト実施
        try {
            bw.getIndexedPropertyValues("aa]aa.bbb");
            fail();
        } catch (IllegalArgumentException e) {
            // OK
            String expect = "Cannot get Index. Invalid property name. 'aa]aa.bbb'";
            assertEquals(expect, e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(expect))));
        }
    }

    /**
     * testGetIndexedPropertyValuesMap13() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:aa.aa.bb<br>
     * (状態) this.context:Map<String, Object><br>
     * aa.aa=new Bean()<br>
     * ∟bb="test"<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * 空のMap<br>
     * <br>
     * 【Map型のテスト】<br>
     * 不正なMapキーのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap13() throws Exception {
        // 前処理
        Map<String, Object> map = new HashMap<String, Object>();
        JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Bar bar = new JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Bar();
        bar.setHoge("test");
        map.put("aa.aa", map);
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(map);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "aa.aa.bar.hoge");
        assertEquals(0, result.size());
    }

    /**
     * testGetIndexedPropertyValuesMap14() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) propertyName:aa'aa.bb<br>
     * (状態) this.context:Map<String, Object><br>
     * 空のMap<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ：Invalid character has found within property name. 'aa'aa.bb' Cannot use [ / " ' ]<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Invalid character has found within property name. 'aa'aa.bb' Cannot use [ / " ' ]<br>
     * <br>
     * 【Map型のテスト】<br>
     * 不正なMapキーのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap14() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(new HashMap<String, Object>());

        // テスト実施
        try {
            bw.getIndexedPropertyValues("aa'aa.bbb");
            fail();
        } catch (IllegalArgumentException e) {
            // OK
            String expect = "Invalid character has found within property name. 'aa'aa.bbb' Cannot use [ / \" ' ]";
            assertEquals(expect, e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(expect))));
        }
    }

    /**
     * testGetIndexedPropertyValuesMap15() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) propertyName:aa"aa.bb<br>
     * (状態) this.context:Map<String, Object><br>
     * 空のMap<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ：Invalid character has found within property name. 'aa"aa.bb' Cannot use [ / " ' ]<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Invalid character has found within property name. 'aa"aa.bb' Cannot use [ / " ' ]<br>
     * <br>
     * 【Map型のテスト】<br>
     * 不正なMapキーのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap15() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(new HashMap<String, Object>());

        // テスト実施
        try {
            bw.getIndexedPropertyValues("aa\"aa.bbb");
            fail();
        } catch (IllegalArgumentException e) {
            // OK
            String expect = "Invalid character has found within property name. 'aa\"aa.bbb' Cannot use [ / \" ' ]";
            assertEquals(expect, e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(expect))));
        }
    }

    /**
     * testGetIndexedPropertyValuesMap16() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) propertyName:aa(aa.bb<br>
     * (状態) this.context:Map<String, Object><br>
     * 空のMap<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ：Invalid property name. PropertyName: 'aa(aa.bbb'XPath: '/aa(aa/bbb'<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Invalid property name. PropertyName: 'aa(aa.bbb'XPath: '/aa(aa/bbb'<br>
     * <br>
     * 【Map型のテスト】<br>
     * 不正なMapキーのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap16() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(new HashMap<String, Object>());

        // テスト実施
        try {
            bw.getIndexedPropertyValues("aa(aa.bbb");
            fail();
        } catch (IllegalArgumentException e) {
            // OK
            String expect = "Invalid property name. PropertyName: 'aa(aa.bbb'XPath: '/aa(aa/bbb'";
            assertEquals(expect, e.getMessage());
            assertSame(JXPathInvalidSyntaxException.class, e.getCause()
                    .getClass());
        }
    }

    /**
     * testGetIndexedPropertyValuesMap17() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) propertyName:aa)aa.bb<br>
     * (状態) this.context:Map<String, Object><br>
     * 空のMap<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ：Invalid property name. PropertyName: 'aa)aa.bbb'XPath: '/aa)aa/bbb'<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Invalid property name. PropertyName: 'aa)aa.bbb'XPath: '/aa)aa/bbb'<br>
     * <br>
     * 【Map型のテスト】<br>
     * 不正なMapキーのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesMap17() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(new HashMap<String, Object>());

        // テスト実施
        try {
            bw.getIndexedPropertyValues("aa)aa.bbb");
            fail();
        } catch (IllegalArgumentException e) {
            // OK
            String expect = "Invalid property name. PropertyName: 'aa)aa.bbb'XPath: '/aa)aa/bbb'";
            assertEquals(expect, e.getMessage());
            assertSame(JXPathInvalidSyntaxException.class, e.getCause()
                    .getClass());
        }
    }

    /**
     * testGetIndexedPropertyValuesJavaBean01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:"foo.bar.hoge"<br>
     * (状態) this.context:DynaBean｛<br>
     * （プロパティ名=値）<br>
     * foo.bar.hoge="test"<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * foo.bar.hoge="test"<br>
     * <br>
     * 【DynaBeanのテスト】<br>
     * ネストした値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesDynaBean01() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub03 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub03();
        // foo
        JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Foo foo = new JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Foo();

        // bar
        JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Bar bar = new JXPathIndexedBeanWrapperImpl_JavaBeanStub03.Bar();
        // hoge
        bar.setHoge("test");

        // foo.bar.hoge
        foo.setBar(bar);
        bean.setFoo(foo);

        WrapDynaBean dynaBean = new WrapDynaBean(bean);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(dynaBean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "foo.bar.hoge");

        // 判定
        assertEquals(1, result.size());
        assertTrue(result.containsKey("foo.bar.hoge"));
        assertEquals("test", result.get("foo.bar.hoge"));
        assertEquals("test", PropertyUtils.getProperty(dynaBean,
                "foo.bar.hoge"));
        assertEquals("test", BeanUtils.getProperty(dynaBean, "foo.bar.hoge"));
    }

    /**
     * testGetIndexedPropertyValuesJavaBean02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) propertyName:"foo.bar.hoge"<br>
     * (状態) this.context:DynaBean｛<br>
     * （プロパティ名=値）<br>
     * foo.bar[0].hoge[0]="test0"<br>
     * foo.bar[1]=null<br>
     * foo.bar[2].hoge[0]="test2"<br>
     * foo.bar[2].hoge[1]="test3"<br>
     * foo.bar[2].hoge[2]="test4"<br>
     * ｝<br>
     * <br>
     * 期待値：(戻り値) Map<String, Object>:Map<String, Object><br>
     * foo.bar[0].hoge[0]="test0"<br>
     * foo.bar[2].hoge[0]="test2"<br>
     * foo.bar[2].hoge[1]="test3"<br>
     * foo.bar[2].hoge[2]="test4"<br>
     * <br>
     * 【DynaBeanのテスト】<br>
     * ネスト＋配列＋nullが混じるパターンの値を取得するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetIndexedPropertyValuesDynaBean02() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05 bean = new JXPathIndexedBeanWrapperImpl_JavaBeanStub05();

        // foo
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Foo foo = new JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Foo();
        bean.setFoo(foo);

        // foo.bar[0]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar bar0 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar();
        // foo.bar[1]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar bar1 = null;
        // foo.bar[2]
        JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar bar2 = new JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar();
        // foo.bar[]
        List<JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar> barList = new ArrayList<JXPathIndexedBeanWrapperImpl_JavaBeanStub05.Bar>();
        // foo.bar[0]
        barList.add(bar0);
        // foo.bar[1]
        barList.add(bar1);
        // foo.bar[2]
        barList.add(bar2);

        // foo.bar
        foo.setBar(barList);

        // foo.bar[0].hoge[0]="test0"
        List<String> hoge0 = new ArrayList<String>();
        hoge0.add("test0");
        bar0.setHoge(hoge0);

        // foo.bar[1]=null

        // foo.bar[2].hoge[0]="test2"
        // foo.bar[2].hoge[1]="test3"
        // foo.bar[2].hoge[2]="test4"
        List<String> hoge2 = new ArrayList<String>();
        hoge2.add("test2");
        hoge2.add("test3");
        hoge2.add("test4");
        bar2.setHoge(hoge2);

        WrapDynaBean dynaBean = new WrapDynaBean(bean);

        JXPathIndexedBeanWrapperImpl bw = new JXPathIndexedBeanWrapperImpl(dynaBean);

        // テスト実施
        Map<String, Object> result = bw.getIndexedPropertyValues(
                "foo.bar.hoge");

        // 判定
        assertEquals(4, result.size());
        assertEquals("test0", result.get("foo.bar[0].hoge[0]"));
        assertEquals("test2", result.get("foo.bar[2].hoge[0]"));
        assertEquals("test3", result.get("foo.bar[2].hoge[1]"));
        assertEquals("test4", result.get("foo.bar[2].hoge[2]"));
        assertEquals("test0", PropertyUtils.getProperty(dynaBean,
                "foo.bar[0].hoge[0]"));
        assertEquals("test0", BeanUtils.getProperty(dynaBean,
                "foo.bar[0].hoge[0]"));
    }
}
