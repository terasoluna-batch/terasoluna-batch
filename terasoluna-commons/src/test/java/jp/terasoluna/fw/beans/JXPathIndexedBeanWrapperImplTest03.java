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

import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;
import static uk.org.lidalia.slf4jtest.LoggingEvent.info;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;


/**
 * {@link jp.terasoluna.fw.beans.JXPathIndexedBeanWrapperImpl} クラスのブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4> JavaBeanの配列・コレクション型属性にアクセスできるクラス。<br>
 * 前提条件：<br>
 * 当クラスのprotectedメソッドの引数nodeは not null である。
 * <p>
 * @see jp.terasoluna.fw.beans.JXPathIndexedBeanWrapperImpl
 */
public class JXPathIndexedBeanWrapperImplTest03 {

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
     * testToPropertyName01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) xpath:".[@name='bbb']"<br>
     * (状態) スタブisMapObject()の戻り値:true<br>
     * (状態) スタブextractMapKey()の戻り値:"bbb"<br>
     * (状態) スタブextractDecrementIndex()の戻り値:""<br>
     * <br>
     * 期待値：(戻り値) String:"bbb"<br>
     * (状態変化) isMapObject()の引数:".[@name='bbb']"<br>
     * (状態変化) extractMapKey()の引数:".[@name='bbb']"<br>
     * (状態変化) extractDecrementIndex()の引数:".[@name='bbb']"<br>
     * <br>
     * 【Map型・階層なしの試験】<br>
     * 想定した入力の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToPropertyName01() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImplStub04 target = new JXPathIndexedBeanWrapperImplStub04("");
        target.isMapObjectReturnValue = true;
        target.extractDecrementIndexReturnValue = "";

        // テスト実施
        result = target.toPropertyName(".[@name='bbb']");

        // 判定
        assertEquals("bbb", result);
        assertEquals(".[@name='bbb']", target.isMapObjectArg1.get(0));
        assertEquals(".[@name='bbb']", target.extractMapKeyArg1.get(0));
        assertEquals(".[@name='bbb']", target.extractDecrementIndexArg1.get(0));
    }

    /**
     * testToPropertyName02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) xpath:"aaa[@name='bbb']"<br>
     * (状態) スタブisMapObject()の戻り値:false<br>
     * (状態) スタブextractMapKey()の戻り値:"bbb"<br>
     * (状態) スタブisMapAttribute()の戻り値:true<br>
     * (状態) スタブextractMapAttributeName()の戻り値:"aaa"<br>
     * (状態) スタブextractDecrementIndex()の戻り値:""<br>
     * <br>
     * 期待値：(戻り値) String:"aaa(bbb)"<br>
     * (状態変化) isMapObject()の引数:"aaa[@name='bbb']"<br>
     * (状態変化) extractMapKey()の引数:"aaa[@name='bbb']"<br>
     * (状態変化) isMapAttribute()の引数:"aaa[@name='bbb']"<br>
     * (状態変化) extractMapAttributeName()の引数:"aaa[@name='bbb']"<br>
     * (状態変化) extractDecrementIndex()の引数:"aaa[@name='bbb']"<br>
     * <br>
     * 【Map属性・階層なしの試験】<br>
     * 想定した入力の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToPropertyName02() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImplStub04 target = new JXPathIndexedBeanWrapperImplStub04("");
        target.isMapObjectReturnValue = false;
        target.isMapAttributeReturnValue = true;
        target.extractDecrementIndexReturnValue = "";

        // テスト実施
        result = target.toPropertyName("aaa[@name='bbb']");

        // 判定
        assertEquals("aaa(bbb)", result);
        assertEquals("aaa[@name='bbb']", target.isMapObjectArg1.get(0));
        assertEquals("aaa[@name='bbb']", target.extractMapKeyArg1.get(0));
        assertEquals("aaa[@name='bbb']", target.isMapAttributeArg1.get(0));
        assertEquals("aaa[@name='bbb']", target.extractMapAttributeNameArg1.get(
                0));
        assertEquals("aaa[@name='bbb']", target.extractDecrementIndexArg1.get(
                0));
    }

    /**
     * testToPropertyName03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) xpath:"aaa[@name='bbb'][10]"<br>
     * (状態) スタブisMapObject()の戻り値:false<br>
     * (状態) スタブextractMapKey()の戻り値:"bbb"<br>
     * (状態) スタブisMapAttribute()の戻り値:true<br>
     * (状態) スタブextractMapAttributeName()の戻り値:"aaa"<br>
     * (状態) スタブextractDecrementIndex()の戻り値:"[9]"<br>
     * <br>
     * 期待値：(戻り値) String:"aaa(bbb)[9]"<br>
     * (状態変化) isMapObject()の引数:"aaa[@name='bbb'][10]"<br>
     * (状態変化) extractMapKey()の引数:"aaa[@name='bbb'][10]"<br>
     * (状態変化) isMapAttribute()の引数:"aaa[@name='bbb'][10]"<br>
     * (状態変化) extractMapAttributeName()の引数:"aaa[@name='bbb'][10]"<br>
     * (状態変化) extractDecrementIndex()の引数:"aaa[@name='bbb'][10]"<br>
     * <br>
     * 【Map属性配列・階層なしの試験】<br>
     * 想定した入力の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToPropertyName03() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImplStub04 target = new JXPathIndexedBeanWrapperImplStub04("");
        target.isMapObjectReturnValue = false;
        target.isMapAttributeReturnValue = true;
        target.extractDecrementIndexReturnValue = "[9]";

        // テスト実施
        result = target.toPropertyName("aaa[@name='bbb'][10]");

        // 判定
        assertEquals("aaa(bbb)[9]", result);
        assertEquals("aaa[@name='bbb'][10]", target.isMapObjectArg1.get(0));
        assertEquals("aaa[@name='bbb'][10]", target.extractMapKeyArg1.get(0));
        assertEquals("aaa[@name='bbb'][10]", target.isMapAttributeArg1.get(0));
        assertEquals("aaa[@name='bbb'][10]", target.extractMapAttributeNameArg1
                .get(0));
        assertEquals("aaa[@name='bbb'][10]", target.extractDecrementIndexArg1
                .get(0));
    }

    /**
     * testToPropertyName04() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) xpath:"aaa"<br>
     * (状態) スタブisMapObject()の戻り値:false<br>
     * (状態) スタブisMapAttribute()の戻り値:false<br>
     * (状態) スタブextractAttributeName()の戻り値:"aaa"<br>
     * (状態) スタブextractDecrementIndex()の戻り値:""<br>
     * <br>
     * 期待値：(戻り値) String:"aaa"<br>
     * (状態変化) isMapObject()の引数:"aaa"<br>
     * (状態変化) isMapAttribute()の引数:"aaa"<br>
     * (状態変化) extractAttributeName()の引数:"aaa"<br>
     * (状態変化) extractDecrementIndex()の引数:"aaa"<br>
     * <br>
     * 【bean/プリミティブ・階層なしの試験】<br>
     * 想定した入力の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToPropertyName04() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImplStub04 target = new JXPathIndexedBeanWrapperImplStub04("");
        target.isMapObjectReturnValue = false;
        target.isMapAttributeReturnValue = false;
        target.extractDecrementIndexReturnValue = "";

        // テスト実施
        result = target.toPropertyName("aaa");

        // 判定
        assertEquals("aaa", result);
        assertEquals("aaa", target.isMapObjectArg1.get(0));
        assertEquals("aaa", target.isMapAttributeArg1.get(0));
        assertEquals("aaa", target.extractAttributeNameArg1.get(0));
        assertEquals("aaa", target.extractDecrementIndexArg1.get(0));
    }

    /**
     * testToPropertyName05() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) xpath:"aaa[100]"<br>
     * (状態) スタブisMapObject()の戻り値:false<br>
     * (状態) スタブisMapAttribute()の戻り値:false<br>
     * (状態) スタブextractAttributeName()の戻り値:"aaa"<br>
     * (状態) スタブextractDecrementIndex()の戻り値:"[99]"<br>
     * <br>
     * 期待値：(戻り値) String:"aaa[99]"<br>
     * (状態変化) isMapObject()の引数:"aaa[100]"<br>
     * (状態変化) isMapAttribute()の引数:"aaa[100]"<br>
     * (状態変化) extractAttributeName()の引数:"aaa[100]"<br>
     * (状態変化) extractDecrementIndex()の引数:"aaa[100]"<br>
     * <br>
     * 【bean/プリミティブ配列・階層なしの試験】<br>
     * 想定した入力の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToPropertyName05() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImplStub04 target = new JXPathIndexedBeanWrapperImplStub04("");
        target.isMapObjectReturnValue = false;
        target.isMapAttributeReturnValue = false;
        target.extractDecrementIndexReturnValue = "[99]";

        // テスト実施
        result = target.toPropertyName("aaa[100]");

        // 判定
        assertEquals("aaa[99]", result);
        assertEquals("aaa[100]", target.isMapObjectArg1.get(0));
        assertEquals("aaa[100]", target.isMapAttributeArg1.get(0));
        assertEquals("aaa[100]", target.extractAttributeNameArg1.get(0));
        assertEquals("aaa[100]", target.extractDecrementIndexArg1.get(0));
    }

    /**
     * testToPropertyName06() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) xpath:"aaa[@name='bbb']/ddd[@name='eee']"<br>
     * (状態) スタブisMapObject()の戻り値:false<br>
     * (状態) スタブextractMapKey()の戻り値:1回目: "bbb"<br>
     * 2回目: "eee"<br>
     * (状態) スタブisMapAttribute()の戻り値:true<br>
     * (状態) スタブextractMapAttributeName()の戻り値:1回目: "aaa"<br>
     * 2回目: "ddd"<br>
     * (状態) スタブextractDecrementIndex()の戻り値:""<br>
     * <br>
     * 期待値：(戻り値) String:"aaa(bbb).ddd(eee)"<br>
     * (状態変化) isMapObject()の引数:1回目: "aaa[@name='bbb']"<br>
     * 2回目: "ddd[@name='eee']"<br>
     * (状態変化) extractMapKey()の引数:1回目: "aaa[@name='bbb']"<br>
     * 2回目: "ddd[@name='eee']"<br>
     * (状態変化) isMapAttribute()の引数:1回目: "aaa[@name='bbb']"<br>
     * 2回目: "ddd[@name='eee']"<br>
     * (状態変化) extractMapAttributeName()の引数:1回目: "aaa[@name='bbb']"<br>
     * 2回目: "ddd[@name='eee']"<br>
     * (状態変化) extractDecrementIndex()の引数:1回目: "aaa[@name='bbb']"<br>
     * 2回目: "ddd[@name='eee']"<br>
     * <br>
     * 【Map属性・階層ありの試験】<br>
     * 想定した入力の場合の試験。階層間の区切り文字ありパターン。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToPropertyName06() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImplStub04 target = new JXPathIndexedBeanWrapperImplStub04("");
        target.isMapObjectReturnValue = false;
        target.isMapAttributeReturnValue = true;
        target.extractDecrementIndexReturnValue = "";

        // テスト実施
        result = target.toPropertyName("aaa[@name='bbb']/ddd[@name='eee']");

        // 判定
        assertEquals("aaa(bbb).ddd(eee)", result);
        assertEquals("aaa[@name='bbb']", target.isMapObjectArg1.get(0));
        assertEquals("aaa[@name='bbb']", target.extractMapKeyArg1.get(0));
        assertEquals("ddd[@name='eee']", target.extractMapKeyArg1.get(1));
        assertEquals("aaa[@name='bbb']", target.isMapAttributeArg1.get(0));
        assertEquals("ddd[@name='eee']", target.isMapAttributeArg1.get(1));
        assertEquals("aaa[@name='bbb']", target.extractMapAttributeNameArg1.get(
                0));
        assertEquals("ddd[@name='eee']", target.extractMapAttributeNameArg1.get(
                1));
        assertEquals("aaa[@name='bbb']", target.extractDecrementIndexArg1.get(
                0));
        assertEquals("ddd[@name='eee']", target.extractDecrementIndexArg1.get(
                1));
    }

    /**
     * testToPropertyName07() <br>
     * <br>
     * (異常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) xpath:""<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException("XPath is null or blank.")<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * XPath is null or blank.<br>
     * <br>
     * xpathが空文字の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToPropertyName07() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        try {
            target.toPropertyName("");
            fail();

        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals("XPath is null or blank.", e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "XPath is null or blank."))));
        }
    }

    /**
     * testExtractAttributeName01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"aaa[bbb]"<br>
     * <br>
     * 期待値：(戻り値) String:"aaa"<br>
     * <br>
     * nodeの内容が ネストなしの想定した形式の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractAttributeName01() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.extractAttributeName("aaa[bbb]");

        // 判定
        assertEquals("aaa", result);
    }

    /**
     * testExtractAttributeName02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"aaa[bbb][ddd]"<br>
     * <br>
     * 期待値：(戻り値) String:"aaa[bbb]"<br>
     * <br>
     * nodeの内容がネストのある 想定した形式の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractAttributeName02() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.extractAttributeName("aaa[bbb][ddd]");

        // 判定
        assertEquals("aaa[bbb]", result);
    }

    /**
     * testExtractAttributeName03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"aaa.ccc.eee"<br>
     * <br>
     * 期待値：(戻り値) String:"aaa.ccc.eee"<br>
     * <br>
     * nodeの"["がない場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractAttributeName03() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.extractAttributeName("aaa.ccc.eee");

        // 判定
        assertEquals("aaa.ccc.eee", result);
    }

    /**
     * testExtractAttributeName04() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) node:""<br>
     * <br>
     * 期待値：(戻り値) String:""<br>
     * <br>
     * nodeが空文字の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractAttributeName04() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.extractAttributeName("");

        // 判定
        assertEquals("", result);
    }

    /**
     * testExtractMapAttributeName01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"aaa[bbb]"<br>
     * <br>
     * 期待値：(戻り値) String:"aaa"<br>
     * <br>
     * nodeの内容が 想定した形式の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractMapAttributeName01() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.extractMapAttributeName("aaa[bbb]");

        // 判定
        assertEquals("aaa", result);
    }

    /**
     * testExtractMapAttributeName02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"[bbb]"<br>
     * <br>
     * 期待値：(戻り値) String:""<br>
     * <br>
     * nodeの属性名がない場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractMapAttributeName02() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.extractMapAttributeName("[bbb]");

        // 判定
        assertEquals("", result);
    }

    /**
     * testExtractMapAttributeName03() <br>
     * <br>
     * (異常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"aaa"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException("Cannot get Map attribute. Invalid property name. 'aaa'")<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Cannot get Map attribute. Invalid property name. 'aaa'<br>
     * <br>
     * nodeの"["がない場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractMapAttributeName03() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        try {
            target.extractMapAttributeName("aaa");
            fail();

        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(
                    "Cannot get Map attribute. Invalid property name. 'aaa'", e
                            .getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Cannot get Map attribute. Invalid property name. 'aaa'"))));
        }
    }

    /**
     * testExtractMapAttributeName04() <br>
     * <br>
     * (異常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) node:""<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgmentException("Cannot get Map attribute. Invalid property name. ''")<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Cannot get Map attribute. Invalid property name. ''<br>
     * <br>
     * nodeが空文字の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractMapAttributeName04() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        try {
            target.extractMapAttributeName("");
            fail();

        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals("Cannot get Map attribute. Invalid property name. ''",
                    e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Cannot get Map attribute. Invalid property name. ''"))));
        }
    }

    /**
     * testExtractMapKey01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"aaa[@name='bbb']"<br>
     * <br>
     * 期待値：(戻り値) String:"bbb"<br>
     * <br>
     * nodeの内容が 想定した形式の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractMapKey01() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.extractMapKey("aaa[@name='bbb']");

        // 判定
        assertEquals("bbb", result);
    }

    /**
     * testExtractMapKey02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"aaa[@name='']"<br>
     * <br>
     * 期待値：(戻り値) String:""<br>
     * <br>
     * nodeのMapキーが空文字の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractMapKey02() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.extractMapKey("aaa[@name='']");

        // 判定
        assertEquals("", result);
    }

    /**
     * testExtractMapKey03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"aaa[@name='bbb'"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException("Cannot get Map key. Invalid property name. 'aaa[@name='bbb''")<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Cannot get Map key. Invalid property name. 'aaa[@name='bbb''<br>
     * <br>
     * nodeの"]"がない場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractMapKey03() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        try {
            target.extractMapKey("aaa[@name='bbb'");
            fail();

        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(
                    "Cannot get Map key. Invalid property name. 'aaa[@name='bbb''",
                    e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Cannot get Map key. Invalid property name. 'aaa[@name='bbb''"))));
        }
    }

    /**
     * testExtractMapKey04() <br>
     * <br>
     * (異常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"aaa@name='bbb']"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException("Cannot get Map key. Invalid property name. 'aaa@name='bbb']'")<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Cannot get Map key. Invalid property name. 'aaa@name='bbb']'<br>
     * <br>
     * nodeの"["がない場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractMapKey04() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        try {
            target.extractMapKey("aaa@name='bbb']");
            fail();

        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(
                    "Cannot get Map key. Invalid property name. 'aaa@name='bbb']'",
                    e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Cannot get Map key. Invalid property name. 'aaa@name='bbb']'"))));
        }
    }

    /**
     * testExtractMapKey05() <br>
     * <br>
     * (異常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"aaa"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException("Cannot get Map key. Invalid property name. 'aaa'")<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Cannot get Map key. Invalid property name. 'aaa'<br>
     * <br>
     * nodeの[]がない場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractMapKey05() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        try {
            target.extractMapKey("aaa");
            fail();

        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals("Cannot get Map key. Invalid property name. 'aaa'", e
                    .getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Cannot get Map key. Invalid property name. 'aaa'"))));
        }
    }

    /**
     * testExtractMapKey06() <br>
     * <br>
     * (異常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"aaa]@name='bbb'["<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException("Cannot get Map key. Invalid property name. 'aaa]@name='bbb'['")<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Cannot get Map key. Invalid property name. 'aaa]@name='bbb'['<br>
     * <br>
     * nodeの[]が逆向きの場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractMapKey06() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        try {
            target.extractMapKey("aaa]@name='bbb'[");
            fail();

        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals(
                    "Cannot get Map key. Invalid property name. 'aaa]@name='bbb'['",
                    e.getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Cannot get Map key. Invalid property name. 'aaa]@name='bbb'['"))));
        }
    }

    /**
     * testExtractMapKey07() <br>
     * <br>
     * (異常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) node:""<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException("Cannot get Map key. Invalid property name. ''")<br>
     * (状態変化) ログ:ログレベル：エラー<br>
     * Cannot get Map key. Invalid property name. ''<br>
     * <br>
     * nodeが空文字の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractMapKey07() throws Exception {
        // 前処理
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        try {
            target.extractMapKey("");
            fail();

        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals("Cannot get Map key. Invalid property name. ''", e
                    .getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Cannot get Map key. Invalid property name. ''"))));
        }
    }

    /**
     * testExtractDecrementIndex01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"[3]"<br>
     * (状態) スタブextractIncrementIndex()の戻り値:"test[3]&-1"<br>
     * <br>
     * 期待値：(戻り値) String:"[3]:-1"<br>
     * (状態変化) extractIncrementIndex()<br>
     * の引数:property="[3]"<br>
     * increment=-1<br>
     * (property+":"+incrementを当メソッドの戻り値とする)<br>
     * <br>
     * extractIncrementIndex()を正しく呼び出していることの試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testExtractDecrementIndex01() throws Exception {
        // 前処理
        String result = null;
        JXPathIndexedBeanWrapperImplStub02 target = new JXPathIndexedBeanWrapperImplStub02("");

        // テスト実施
        result = target.extractDecrementIndex("[3]");

        // 判定
        assertEquals("[3]:-1", result);
    }

    /**
     * testIsMapAttribute01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"z[@name]"<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * Mapオブジェクトの場合の試験。nodeの途中に"[@name" <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsMapAttribute01() throws Exception {
        // 前処理
        boolean result = false;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.isMapAttribute("z[@name]");

        // 判定
        assertTrue(result);
    }

    /**
     * testIsMapAttribute02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"@name"<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * Mapオブジェクト以外の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsMapAttribute02() throws Exception {
        // 前処理
        boolean result = true;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.isMapAttribute("@name");

        // 判定
        assertFalse(result);
    }

    /**
     * testIsMapAttribute03() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) node:""<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * nodeが空文字列の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsMapAttribute03() throws Exception {
        // 前処理
        boolean result = true;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.isMapAttribute("");

        // 判定
        assertFalse(result);
    }

    /**
     * testIsMapObject01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:".[@name"<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * Mapオブジェクトの場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsMapObject01() throws Exception {
        // 前処理
        boolean result = false;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.isMapObject(".[@name");

        // 判定
        assertTrue(result);
    }

    /**
     * testIsMapObject02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) node:"a.[@name=]"<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * Mapオブジェクト以外の場合の試験。".[@name"が先頭から始まらない。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsMapObject02() throws Exception {
        // 前処理
        boolean result = true;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.isMapObject("a.[@name=]");

        // 判定
        assertFalse(result);
    }

    /**
     * testIsMapObject03() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) node:""<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * nodeが空文字列の場合の試験。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsMapObject03() throws Exception {
        // 前処理
        boolean result = true;
        JXPathIndexedBeanWrapperImpl target = new JXPathIndexedBeanWrapperImpl("");

        // テスト実施
        result = target.isMapObject("");

        // 判定
        assertFalse(result);
    }

}
