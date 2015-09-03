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

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import jp.terasoluna.fw.util.PropertyTestCase;
import jp.terasoluna.utlib.UTUtil;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;
import static uk.org.lidalia.slf4jtest.LoggingEvent.warn;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * PropertyUtil ブラックボックステスト。<br>
 * (前提条件)<br>
 * ・プロパティファイルに以下のような設定をしておく<br>
 * property.test001.id.0 = test<br>
 * property.test002.id.0 = test0<br>
 * property.test002.id.1 = test1<br>
 * property.test002.id.2 = test2<br>
 * property.test003.id.0 =<br>
 * property.test004.id.0 = testA<br>
 * property.test004.id.0 = testB<br>
 * property.test004.id.1 = testA<br>
 * fileutiltest.dir.base = /tmp/test<br>
 * codelist.gengo1.define.1 = 江戸<br>
 * codelist.gengo1.define.2 = 明治<br>
 * codelist.gengo1.define.3 = 大正<br>
 * codelist.gengo2.define.1 = 昭和<br>
 * codelist.gengo2.define.2 = 平成<br>
 * codelist.sql1.sql.0=select values01,values01,values01 from table_kamoTest where Key1 between ? and ?<br>
 * @property.test0 = testtest<br>
 *                 property.test100.id.0 = @property.test100.id.0<br>
 *                 property.test005 = @property.test001.id.0<br>
 *                 property.test007.id.0=@@test007<br>
 *                 property.test008.id.0=@@<br>
 *                 property.test009.id.0=@<br>
 */
@SuppressWarnings("unused")
public class PropertyUtilTest extends PropertyTestCase {

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            PropertyUtil.class);

    @Before
    public void setUpData() throws Exception {
        addProperty("system.name", "SAMPLE1");
        addProperty("fileutiltest.dir.base", "/tmp/test");
        addProperty("property.test001.id.0", "test");
        addProperty("property.test002.id.0", "test0");
        addProperty("property.test002.id.1", "test1");
        addProperty("property.test002.id.2", "test2");
        addProperty("property.test003.id.0", "");
        addProperty("property.test004.id.0", "testA");
        addProperty("property.test004.id.0", "testB");
        addProperty("@property.test0", "testtest");
        addProperty("property.test100.id.0", "@property.test100.id.0");
        addProperty("property.test005", "@property.test001.id.0");
        addProperty("property.test006.id.0", "testA");
        addProperty("property.test007.id.0", "@@test007");
        addProperty("property.test008.id.0", "@@");
        addProperty("property.test009.id.0", "@");
        addProperty("codelist.gengo1.define.1", "江戸");
        addProperty("codelist.gengo1.define.2", "明治");
        addProperty("codelist.gengo1.define.3", "大正");
        addProperty("codelist.gengo2.define.1", "昭和");
        addProperty("codelist.gengo2.define.2", "平成");
        addProperty("codelist.sql1.sql.0",
                "select values01,values01,values01 from table_kamoTest where Key1 between ? and ?");
    }

    @After
    public void cleanUpData() throws Exception {
        clearProperty();
    }

    /**
     * testAddPropertyFile01()。<br>
     * (正常系)<br>
     * 観点：F<br>
     * 入力値：存在するプロパティファイル(.propertiesなし)<br>
     * 期待値：PropertyUtilクラスのfilesフィールドにファイル名が含まれていること<br>
     * @throws Exception 例外
     */
    @Test
    public void testAddPropertyFile01() throws Exception {
        // 入力値の設定
        String input = "system";

        // テスト対象の実行
        PropertyUtil.addPropertyFile(input);

        // 結果確認
        // PropertyUtilクラスのfilesフィールドにファイル名が含まれていること
        Field field = PropertyUtil.class.getDeclaredField("files");
        field.setAccessible(true);
        Set st = (Set) field.get(PropertyUtil.class);
        assertTrue(st.contains("system.properties"));
    }

    /**
     * testAddPropertyFile02()。<br>
     * (正常系)<br>
     * 観点：F<br>
     * 入力値：存在するプロパティファイル(.propertiesあり)<br>
     * 期待値：PropertyUtilクラスのfilesフィールドにファイル名が含まれていること<br>
     * @throws Exception 例外
     */
    @Test
    public void testAddPropertyFile02() throws Exception {
        // 入力値の設定
        String input = "system.properties";

        // テスト対象の実行
        PropertyUtil.addPropertyFile(input);

        // 結果確認
        // PropertyUtilクラスのfilesフィールドにファイル名が含まれていること
        Field field = PropertyUtil.class.getDeclaredField("files");
        field.setAccessible(true);
        Set st = (Set) field.get(PropertyUtil.class);
        assertTrue(st.contains("system.properties"));
    }

    /**
     * testAddPropertyFile03()。<br>
     * (正常系)<br>
     * 観点：C,F<br>
     * 入力値：存在しないプロパティファイル<br>
     * 期待値：PropertyUtilクラスのfilesフィールドにファイル名が含まれていないこと<br>
     * @throws Exception 例外
     */
    @Test
    public void testAddPropertyFile03() throws Exception {
        // 入力値の設定
        String input = "xxxxx";

        // テスト対象の実行
        PropertyUtil.addPropertyFile(input);

        // 結果の確認
        // PropertyUtilクラスのfilesフィールドにファイル名が含まれていないこと
        Field field = PropertyUtil.class.getDeclaredField("files");
        field.setAccessible(true);
        Set st = (Set) field.get(PropertyUtil.class);
        assertFalse(st.contains("xxxxx.properties"));
    }

    /**
     * testAddPropertyFile04()。<br>
     * (正常系)<br>
     * 観点：C,G<br>
     * 入力値：null<br>
     * 期待値：NullPointerException<br>
     * @throws Exception 例外
     */
    @Test
    public void testAddPropertyFile04() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト対象の実行
        try {
            PropertyUtil.addPropertyFile(input);
            fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * testAddPropertyFile05()。<br>
     * (正常系)<br>
     * 観点：C,F<br>
     * 入力値：""(空文字)<br>
     * 期待値：<br>
     * @throws Exception 例外
     */
    @Test
    public void testAddPropertyFile05() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト対象の実行
        PropertyUtil.addPropertyFile(input);

        // 結果の確認
        // PropertyUtilクラスのfilesフィールドにファイル名が含まれていないこと
        Field field = PropertyUtil.class.getDeclaredField("files");
        field.setAccessible(true);
        Set st = (Set) field.get(PropertyUtil.class);
        assertFalse(st.contains(".properties"));
    }

    /**
     * testAddPropertyFile06()。<br>
     * (正常系)<br>
     * 観点：F<br>
     * 入力値：存在するプロパティファイル、複数回読み込む<br>
     * 期待値：一度しか読み込まれないことを<br>
     * @throws Exception 例外
     */
    @Test
    public void testAddPropertyFile06() throws Exception {
        // 入力値の設定
        String input = "system";

        // テスト対象の実行
        PropertyUtil.addPropertyFile(input);
        PropertyUtil.addPropertyFile(input);
        // 結果確認
        // PropertyUtilクラスのfilesフィールドにファイル名が含まれていること
        Field field = PropertyUtil.class.getDeclaredField("files");
        field.setAccessible(true);
        Set st = (Set) field.get(PropertyUtil.class);
        assertTrue(st.contains("system.properties"));
        assertTrue(st.size() == 1);
    }

    /**
     * testGetProperty01(String)。<br>
     * (正常系)<br>
     * 観点：F<br>
     * 入力値：存在するキー<br>
     * 期待値：キーのプロパティ値<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyString01() throws Exception {
        // 入力値の設定
        String input = "property.test001.id.0";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input);

        // 結果確認
        assertEquals("test", str);
    }

    /**
     * testGetProperty02(String)。<br>
     * (正常系)<br>
     * 観点：C,F<br>
     * 入力値：存在しないキー<br>
     * 期待値：Null値<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyString02() throws Exception {
        // 入力値の設定
        String input = "property.test001.id.1";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input);

        // 結果確認
        assertNull(str);
    }

    /**
     * testGetProperty03(String) 。<br>
     * (異常系)<br>
     * 観点：G<br>
     * 入力値：null<br>
     * 期待値：NullPointerException<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyString03() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト対象の実行
        try {
            String str = PropertyUtil.getProperty(input);
            fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * testGetProperty04(String)。<br>
     * (正常系)<br>
     * 観点：C<br>
     * 入力値：""(空文字)<br>
     * 期待値：Null値<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyString04() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input);

        // 結果確認
        assertNull(str);
    }

    /**
     * testGetProperty05(String) 。<br>
     * (正常系)<br>
     * 観点：C,F<br>
     * 入力値：プロパティ値が空文字のキー<br>
     * 期待値：空文字<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyString05() throws Exception {
        // 入力値の設定
        String input = "property.test003.id.0";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input);

        // 結果確認
        assertEquals("", str);
    }

    /**
     * testGetProperty06(String)。<br>
     * (正常系)<br>
     * 観点：F<br>
     * 入力値：複数存在するキー<br>
     * 期待値：後に設定されたキーのプロパティ値<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyString06() throws Exception {
        // 入力値の設定
        String input = "property.test004.id.0";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input);

        // 結果確認
        assertEquals("testB", str);
    }

    /**
     * testGetProperty07(String) 。<br>
     * (正常系)<br>
     * 観点：F<br>
     * 入力値：<code>@key</code><br>
     * 期待値：後に設定されたキーのプロパティ値<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyString07() throws Exception {
        // 入力値の設定
        String input = "@property.test0";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input);

        // 結果確認
        assertEquals("testtest", str);
    }

    /**
     * testGetProperty08(String)。<br>
     * (正常系)<br>
     * 観点：A,F<br>
     * 入力値：<code>key=@key</code><br>
     * 期待値：後に設定されたキーのプロパティ値<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyString08() throws Exception {
        // 入力値の設定
        String input = "property.test100.id.0";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input);

        // 結果確認
        assertEquals("@property.test100.id.0", str);
    }

    /**
     * testGetProperty09(String)。<br>
     * (正常系)<br>
     * 観点：A,F<br>
     * 入力値：<code>key=@value</code><br>
     * 期待値：<code>@</code>を外したプロパティ値<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyString09() throws Exception {
        // 入力値の設定
        String input = "property.test005";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input);

        // 結果確認
        assertEquals("test", str);
    }

    /**
     * testGetProperty10(String)。<br>
     * (正常系)<br>
     * 観点：A,F<br>
     * 入力値：<code>key=@@value</code><br>
     * 期待値：@value<br>
     * property.test007.id.0=@@test007とプロパティファイルに設定し、 @test007が得られることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyString10() throws Exception {
        // 入力値の設定
        String input = "property.test007.id.0";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input);

        // 結果確認
        assertEquals("@test007", str);
    }

    /**
     * testGetProperty12(String)。<br>
     * (正常系)<br>
     * 観点：A,F<br>
     * 入力値：<code>key=@@</code><br>
     * 期待値：@<br>
     * property.test008.id.0=@@とプロパティファイルに設定し、 @が得られることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyString11() throws Exception {
        // 入力値の設定
        String input = "property.test008.id.0";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input);

        // 結果確認
        assertEquals("@", str);
    }

    /**
     * testGetProperty13(String)。<br>
     * (正常系)<br>
     * 観点：A,F<br>
     * 入力値：<code>key=@</code><br>
     * 期待値：@<br>
     * property.test009.id.0=@とプロパティファイルに設定し、 Nullが返って来ることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyString12() throws Exception {
        // 入力値の設定
        String input = "property.test009.id.0";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input);

        // 結果確認
        assertNull(str);
    }

    /**
     * testGetProperty01(String, String)。<br>
     * (正常系)<br>
     * 観点：F<br>
     * 入力値：key=存在するキー<br>
     * default=デフォルト値<br>
     * 期待値：キーのプロパティ値<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyStringString01() throws Exception {
        // 入力値の設定
        String input1 = "property.test001.id.0";
        String input2 = "default";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input1, input2);

        // 結果確認
        assertEquals("test", str);
    }

    /**
     * testGetProperty02(String, String)。<br>
     * (正常系)<br>
     * 観点：F<br>
     * 入力値：key=存在しないキー<br>
     * default=デフォルト値<br>
     * 期待値：デフォルト値<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyStringString02() throws Exception {
        // 入力値の設定
        String input1 = "property.test001.id.1";
        String input2 = "default";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input1, input2);

        // 結果確認
        assertEquals("default", str);
    }

    /**
     * testGetProperty03(String, String)。<br>
     * (異常系)<br>
     * 観点：C,F<br>
     * 入力値：key=null<br>
     * default=デフォルト値<br>
     * 期待値：NullPointerException<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyStringString03() throws Exception {
        // 入力値の設定
        String input1 = null;
        String input2 = "default";

        // テスト対象の実行
        try {
            String str = PropertyUtil.getProperty(input1, input2);
            fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * testGetProperty04(String, String)。<br>
     * (正常系)<br>
     * 観点：C,F<br>
     * 入力値：key=存在するキー<br>
     * default=null<br>
     * 期待値：キーのプロパティ値<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyStringString04() throws Exception {
        // 入力値の設定
        String input1 = "property.test001.id.0";
        String input2 = null;

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input1, input2);

        // 結果確認
        assertEquals("test", str);
    }

    /**
     * testGetProperty05(String, String)。<br>
     * (正常系)<br>
     * 観点：C<br>
     * 入力値：key=存在しないキー<br>
     * default=null<br>
     * 期待値：null(デフォルト)<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyStringString05() throws Exception {
        // 入力値の設定
        String input1 = "property.test001.id.1";
        String input2 = null;

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input1, input2);

        // 結果確認
        assertNull(str);
    }

    /**
     * testGetProperty06(String, String) 。<br>
     * (正常系)<br>
     * 観点：C,F<br>
     * 入力値：key=""(空文字)<br>
     * default=デフォルト値<br>
     * 期待値：デフォルト値<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyStringString06() throws Exception {
        // 入力値の設定
        String input1 = "";
        String input2 = "default";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input1, input2);

        // 結果確認
        assertEquals("default", str);
    }

    /**
     * testGetProperty07(String, String)。<br>
     * (正常系)<br>
     * 観点：C,F<br>
     * 入力値：key=存在するキー<br>
     * default=""(空文字)<br>
     * 期待値：キーのプロパティ値<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyStringString07() throws Exception {
        // 入力値の設定
        String input1 = "property.test001.id.0";
        String input2 = "";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input1, input2);

        // 結果確認
        assertEquals("test", str);
    }

    /**
     * testGetProperty08(String, String)。<br>
     * (正常系)<br>
     * 観点：C,F<br>
     * 入力値：key=存在しないキー<br>
     * default=""(空文字)<br>
     * 期待値：""(デフォルト)<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyStringString08() throws Exception {
        // 入力値の設定
        String input1 = "property.test001.id.1";
        String input2 = "";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input1, input2);

        // 結果確認
        assertEquals("", str);
    }

    /**
     * testGetProperty09(String, String)。<br>
     * (正常系)<br>
     * 観点：C,F<br>
     * 入力値：key=プロパティ値が""(空文字)のキー<br>
     * default=デフォルト値<br>
     * 期待値：空文字<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyStringString09() throws Exception {
        // 入力値の設定
        String input1 = "property.test003.id.0";
        String input2 = "default";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input1, input2);

        // 結果確認
        assertEquals("", str);
    }

    /**
     * testGetProperty10(String, String)。<br>
     * (正常系)<br>
     * 観点：F<br>
     * 入力値：key=複数存在するキー<br>
     * default=デフォルト値<br>
     * 期待値：キーのプロパティ値<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyStringString10() throws Exception {
        // 入力値の設定
        String input1 = "property.test004.id.0";
        String input2 = "default";

        // テスト対象の実行
        String str = PropertyUtil.getProperty(input1, input2);

        // 結果確認
        assertEquals("testB", str);
    }

    /**
     * testGetPropertyNames01()。<br>
     * (正常系)<br>
     * 観点：F<br>
     * 入力値：なし<br>
     * 期待値：すべてのキー<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyNames01() throws Exception {
        // テスト対象の実行
        Enumeration en = PropertyUtil.getPropertyNames();

        // 結果の確認
        // Enumerationから要素を取り出し、Vector配列に追加する
        Vector<String> v = new Vector<String>();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            v.add(key);
        }
        // キーの存在確認
        assertTrue(v.contains("system.name"));
        assertTrue(v.contains("property.test001.id.0"));
        assertTrue(v.contains("property.test002.id.0"));
        assertTrue(v.contains("property.test002.id.1"));
        assertTrue(v.contains("property.test002.id.2"));
        assertTrue(v.contains("property.test003.id.0"));
        assertTrue(v.contains("property.test004.id.0"));
        assertTrue(v.contains("property.test004.id.0"));
        assertTrue(v.contains("@property.test0"));
        assertTrue(v.contains("fileutiltest.dir.base"));
        assertTrue(v.contains("property.test100.id.0"));
        assertTrue(v.contains("property.test005"));
        assertTrue(v.contains("property.test006.id.0"));
        assertTrue(v.contains("property.test007.id.0"));
        assertTrue(v.contains("property.test008.id.0"));
        assertTrue(v.contains("property.test009.id.0"));
        assertTrue(v.contains("codelist.gengo1.define.1"));
        assertTrue(v.contains("codelist.gengo1.define.2"));
        assertTrue(v.contains("codelist.gengo1.define.3"));
        assertTrue(v.contains("codelist.gengo2.define.1"));
        assertTrue(v.contains("codelist.gengo2.define.2"));
        assertTrue(v.contains("codelist.sql1.sql.0"));
    }

    /**
     * testGetPropertyNames01(String) 。<br>
     * (正常系)<br>
     * 観点：F<br>
     * 入力値：key=存在するプリフィックス<br>
     * 期待値：キーリスト(1件)<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyNamesString01() throws Exception {
        // 入力値の設定
        String input1 = "property.test001.id";

        // テスト対象の実行
        Enumeration enume = PropertyUtil.getPropertyNames(input1);

        // 結果確認
        assertEquals("property.test001.id.0", enume.nextElement());
        assertFalse(enume.hasMoreElements());
    }

    /**
     * testGetPropertyNames02(String)。<br>
     * (正常系)<br>
     * 観点：C,F<br>
     * 入力値：key=存在しないプリフィックス<br>
     * 期待値：キーリスト(0件)<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyNamesString02() throws Exception {
        // 入力値の設定
        String input1 = "property.test999.id";

        // テスト対象の実行
        Enumeration enume = PropertyUtil.getPropertyNames(input1);

        // 結果確認
        assertFalse(enume.hasMoreElements());
    }

    /**
     * testGetPropertyNames03(String)。<br>
     * (正常系)<br>
     * 観点：F<br>
     * 入力値：key=存在するプリフィックス<br>
     * 期待値：キーリスト(3件)<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyNamesString03() throws Exception {
        // 入力値の設定
        String input1 = "property.test002.id";

        // テスト対象の実行
        Enumeration enume = PropertyUtil.getPropertyNames(input1);

        // 結果確認
        assertEquals("property.test002.id.0", enume.nextElement());
        assertEquals("property.test002.id.1", enume.nextElement());
        assertEquals("property.test002.id.2", enume.nextElement());
        assertFalse(enume.hasMoreElements());
    }

    /**
     * testGetPropertyNames04(String) 。<br>
     * (異常系)<br>
     * 観点：C,G<br>
     * 入力値：key=null<br>
     * 期待値：NullPointerException<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyNamesString04() throws Exception {
        // 入力値の設定
        String input1 = null;

        // テスト対象の実行
        try {
            Enumeration enume = PropertyUtil.getPropertyNames(input1);
            fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * testGetPropertyNames05(String)。<br>
     * (正常系)<br>
     * 観点：C,F<br>
     * 入力値：key=""<br>
     * 期待値：キーリスト(全件)<br>
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertyNamesString05() throws Exception {
        // 入力値の設定
        String input1 = "";

        // テスト対象の実行
        Enumeration<String> actualEnum = PropertyUtil.getPropertyNames(input1);

        // 結果確認
        Field field = PropertyUtil.class.getDeclaredField("props");
        field.setAccessible(true);
        Map<String, String> expectedProps = (Map<String, String>) field.get(
                PropertyUtil.class);

        Enumeration<String> expectedEnum = Collections.enumeration(expectedProps
                .keySet());
        while (expectedEnum.hasMoreElements()) {
            String actualStr = (String) actualEnum.nextElement();
            String expectedStr = (String) expectedEnum.nextElement();
            assertEquals(expectedStr, actualStr);
        }
    }

    /**
     * testGetPropertyNames06(String)。<br>
     * (正常系)<br>
     * 観点：F<br>
     * 入力値：key=複数存在するキーのプリフィックス<br>
     * 期待値：キーリスト(1件)<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyNamesString06() throws Exception {
        // 入力値の設定
        String input1 = "property.test004.id";

        // テスト対象の実行
        Enumeration enume = PropertyUtil.getPropertyNames(input1);

        // 結果確認
        assertEquals("property.test004.id.0", enume.nextElement());
        assertFalse(enume.hasMoreElements());
    }

    /**
     * testGetPropertiesValues01(String, String)。<br>
     * （正常系）<br>
     * 観点：F<br>
     * <br>
     * 入力値 :プロパティファイル名,部分キー文字列<br>
     * 期待値 :値セット（中身が１つ）<br>
     * 説明：部分キー文字列に該当する値が１つの時、 指定されたプロパティファイルから値が取得されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertiesValuesString01() throws Exception {
        // 入力値の設定
        // プロパティファイル名
        String input = "test";
        // 部分キー文字列
        String key = "file";
        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, key);

        // 結果確認
        assertTrue(result.contains("/tmp/test"));
    }

    /**
     * testGetPropertiesValues02(String, String)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティファイル名,部分キー文字列<br>
     * 期待値 :値セット（中身が複数）<br>
     * 説明：部分キー文字列に該当する値が複数の時、 指定されたプロパティファイルから値セットが取得されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertiesValuesString02() throws Exception {
        // 入力値の設定
        // プロパティファイル名
        String input = "test";
        // 部分キー文字列
        String key = "code";
        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, key);

        // 結果確認
        assertTrue(result.contains("select values01,values01,values01 "
                + "from table_kamoTest where Key1 between ? and ?"));
        assertTrue(result.contains("\u660e\u6cbb"));
        assertTrue(result.contains("\u662d\u548c"));
        assertTrue(result.contains("\u6c5f\u6238"));
        assertTrue(result.contains("\u5e73\u6210"));
        assertTrue(result.contains("\u5927\u6b63"));
    }

    /**
     * testGetPropertiesValues03(String, String)。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値 :プロパティファイル名,部分キー文字列がnull<br>
     * 期待値 :null<br>
     * 説明：部分キー文字列がNullの時、nullが返却されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertiesValuesString03() throws Exception {
        // 入力値の設定
        // プロパティファイル名
        String input = "test";
        // 部分キー文字列
        String key = null;
        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, key);

        // 結果確認
        assertNull(result);
    }

    /**
     * testGetPropertiesValues04(String, String)。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値 :プロパティファイル名null,部分キー文字列<br>
     * 期待値 :null<br>
     * 説明：プロパティファイル名がNullの時、 Nullを戻り値として処理を終了することを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertiesValuesString04() throws Exception {
        // 入力値の設定
        // プロパティファイル名
        String input = null;
        // 部分キー文字列
        String key = "file";

        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, key);

        // 結果確認
        assertNull(result);
    }

    /**
     * testGetPropertiesValues05(String, String)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティファイル名,該当するキーがない部分キー文字列<br>
     * 期待値 :値セット（中身が空）<br>
     * 説明：部分キー文字列に該当する値がない場合、 空の"Enumeration"が返却されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertiesValuesString05() throws Exception {
        // 入力値の設定
        // プロパティファイル名
        String input = "test_message_01";
        // 部分キー文字列
        String key = "file";
        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, key);

        // 結果確認
        assertTrue(result.isEmpty());
    }

    /**
     * testGetPropertiesValues06(String, String)。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値 :プロパティファイル名空文字,部分キー文字列<br>
     * 期待値 :null<br>
     * 説明：プロパティファイル名が空文字の時、 Nullを戻り値として処理を終了することを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertiesValuesString06() throws Exception {
        // 入力値の設定
        // プロパティファイル名
        String input = "";
        // 部分キー文字列
        String key = "file";

        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, key);

        // 結果確認
        assertNull(result);
    }

    /**
     * testGetPropertiesValues07(String, String)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティファイル名,部分キー文字列は空文字<br>
     * 期待値 :値セット（全て選択される）<br>
     * 説明：部分キー文字列が空文字の場合、 選択されたプロパティファイルの全て値が返却されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertiesValuesString07() throws Exception {
        // 入力値の設定
        // プロパティファイル名
        String input = "test_message_01";
        // 部分キー文字列
        String key = "";
        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, key);

        // 結果確認
        assertTrue(result.contains("{0}デフォルトメッセージ"));
        assertTrue(result.contains("例外メッセージ"));
        assertTrue(result.contains(""));
    }

    /**
     * testGetPropertiesValues08(String, String)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティファイル名,部分キー文字列(複数存在するキーを含む)<br>
     * 期待値 :値セット（中身が複数）<br>
     * 説明：部分キー文字列に複数存在するキーを含めた時、 値セットが1つ取得されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertiesValuesString08() throws Exception {
        // 入力値の設定
        // プロパティファイル名
        String input = "test";
        // 部分キー文字列
        String key = "property.test004";
        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, key);

        // 結果確認
        assertTrue(result.contains("testB"));
        assertFalse(result.contains("testA"));
    }

    /**
     * testGetPropertyNames01(Properties, String)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティ（1つ）、部分キープリフィックス<br>
     * 期待値 :対応するキー一覧（1つ）<br>
     * 説明：プロパティオブジェクトの中身が１つの時、 対応するキーが1つ取得されていることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertyNamesPropertiesString01() throws Exception {
        // 入力値の設定
        String key1 = "SystemExceptionHandlerTest.key";
        String value1 = "{0}message";

        Properties input = new Properties();
        input.setProperty(key1, value1);

        String keyprefix = "System";

        // テスト対象の実行
        Enumeration result = PropertyUtil.getPropertyNames(input, keyprefix);

        // 結果確認
        Set set = new HashSet();
        set.add("SystemExceptionHandlerTest.key");
        assertTrue(set.contains(result.nextElement()));
        assertFalse(result.hasMoreElements());
    }

    /**
     * testGetPropertyNames02(Properties, String)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティ（複数）、部分キープリフィックス<br>
     * 期待値 :対応するキー一覧（1つ）<br>
     * 説明：プロパティオブジェクトの中身が複数の時、 対応するキーが1つ取得されていることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertyNamesPropertiesString02() throws Exception {
        // 入力値の設定
        String key1 = "SystemExceptionHandlerTest.key";
        String value1 = "{0}message";

        String key2 = "property.test004.id.0";
        String value2 = "testA";

        Properties input = new Properties();
        input.setProperty(key1, value1);
        input.setProperty(key2, value2);

        String keyprefix = "System";

        // テスト対象の実行
        Enumeration result = PropertyUtil.getPropertyNames(input, keyprefix);

        // 結果確認
        Set set = new HashSet();
        set.add("SystemExceptionHandlerTest.key");
        assertTrue(set.contains(result.nextElement()));
        assertFalse(result.hasMoreElements());
    }

    /**
     * testGetPropertyNames03(Properties, String)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティ（複数）、部分キープリフィックス<br>
     * 期待値 :対応するキー一覧（複数）<br>
     * 説明：プロパティオブジェクトの中身が複数の時、 対応するキーが複数取得されていることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertyNamesPropertiesString03() throws Exception {
        // 入力値の設定
        String key1 = "SystemExceptionHandlerTest.key";
        String value1 = "{0}message";

        String key2 = "property.test002.id.2";
        String value2 = "test2";

        String key3 = "property.test004.id.0";
        String value3 = "testB";

        Properties input = new Properties();
        input.setProperty(key1, value1);
        input.setProperty(key2, value2);
        input.setProperty(key3, value3);

        String keyprefix = "property";

        // テスト対象の実行
        Enumeration result = PropertyUtil.getPropertyNames(input, keyprefix);

        // 結果確認
        // 生成されたEnumurationに入ってることの確認
        Set set = new HashSet();
        set.add("property.test002.id.2");
        set.add("property.test004.id.0");
        assertTrue(set.contains(result.nextElement()));
        assertTrue(set.contains(result.nextElement()));
        assertFalse(result.hasMoreElements());
    }

    /**
     * testGetPropertyNames04(Properties, String)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティ（複数）、部分キープリフィックス<br>
     * 期待値 :空のEnumeration<br>
     * 説明：プロパティオブジェクトに対応する部分キープリフィックスがない場合、 空のEnumerationが返却されていることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyNamesPropertiesString04() throws Exception {
        // 入力値の設定
        String key1 = "SystemExceptionHandlerTest.key";
        String value1 = "{0}message";

        String key2 = "property.test002.id.2";
        String value2 = "test2";

        String key3 = "property.test004.id.0";
        String value3 = "testB";

        Properties input = new Properties();
        input.setProperty(key1, value1);
        input.setProperty(key2, value2);
        input.setProperty(key3, value3);

        String keyprefix = "a";

        // テスト対象の実行
        Enumeration result = PropertyUtil.getPropertyNames(input, keyprefix);

        // 結果確認
        assertFalse(result.hasMoreElements());
    }

    /**
     * testGetPropertyNames05(Properties, String)。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値 :プロパティ（Null）、部分キープリフィックス<br>
     * 期待値 :null<br>
     * 説明：プロパティオブジェクトがnullの場合、 nullを戻り値として処理を終了することを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyNamesPropertiesString05() throws Exception {
        // 入力値の設定
        Properties input = null;

        String keyprefix = "properties";

        // テスト対象の実行
        Enumeration result = PropertyUtil.getPropertyNames(input, keyprefix);

        // 結果確認
        assertNull(result);
    }

    /**
     * testGetPropertyNames06(Properties, String)。<br>
     * （正常系）<br>
     * 観点：C,F<br>
     * <br>
     * 入力値 :プロパティ、部分キープリフィックス(null)<br>
     * 期待値 :null<br>
     * 説明：部分キープリフィックスがnullの場合、 nullを戻り値として処理を終了することを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyNamesPropertiesString06() throws Exception {
        // 入力値の設定
        Properties input = new Properties();
        String key1 = "SystemExceptionHandlerTest.key";
        String value1 = "{0}message";
        input.setProperty(key1, value1);

        String keyprefix = null;

        // テスト対象の実行
        Enumeration result = PropertyUtil.getPropertyNames(input, keyprefix);

        // 結果確認
        assertNull(result);
    }

    /**
     * testGetPropertyNames07(Properties, String)。<br>
     * （正常系）<br>
     * 観点：C,F<br>
     * <br>
     * 入力値 :プロパティ（空）、部分キープリフィックス<br>
     * 期待値 :空のEnumeration<br>
     * 説明：プロパティオブジェクトが空の場合、 空のEnumerationが返却されていることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertyNamesPropertiesString07() throws Exception {
        // 入力値の設定
        Properties input = new Properties();

        String keyprefix = "properties";

        // テスト対象の実行
        Enumeration result = PropertyUtil.getPropertyNames(input, keyprefix);

        // 結果確認
        assertFalse(result.hasMoreElements());
    }

    /**
     * testGetPropertyNames08(Properties, String)。<br>
     * （正常系）<br>
     * 観点：C,F<br>
     * <br>
     * 入力値 :プロパティ、部分キープリフィックス(空文字)<br>
     * 期待値 :対応する全てのキー一覧<br>
     * 説明：部分キープリフィックスが空文字の場合、 対応する全てのキー一覧が返却されることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertyNamesPropertiesString08() throws Exception {
        // 入力値の設定
        Properties input = new Properties();

        String key1 = "property.test002.id.2";
        String value1 = "test2";

        String key2 = "property.test004.id.0";
        String value2 = "testB";

        String key3 = "property.test001.id.0";
        String value3 = "test";

        input.setProperty(key1, value1);
        input.setProperty(key2, value2);
        input.setProperty(key3, value3);

        String keyprefix = "";

        // テスト対象の実行
        Enumeration result = PropertyUtil.getPropertyNames(input, keyprefix);

        // 結果確認
        Set set = new HashSet();
        set.add("property.test002.id.2");
        set.add("property.test004.id.0");
        set.add("property.test001.id.0");
        assertTrue(set.contains(result.nextElement()));
        assertTrue(set.contains(result.nextElement()));
        assertTrue(set.contains(result.nextElement()));
        assertFalse(result.hasMoreElements());
    }

    /**
     * testGetPropertyNames09(Properties, String)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :キーが複数存在するプロパティオブジェクト<br>
     * 期待値 :対応する全てのキー一覧<br>
     * 説明：キーが複数存在するプロパティオブジェクトの場合 複数のうち1つ返却されることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertyNamesPropertiesString09() throws Exception {
        // 入力値の設定
        String key1 = "SystemExceptionHandlerTest.key";
        String value1 = "{0}message";

        String key2 = "property.test002.id.2";
        String value2 = "test2";

        String key3 = "property.test004.id.0";
        String value3 = "testA";

        String key4 = "property.test004.id.0";
        String value4 = "testB";

        Properties input = new Properties();
        input.setProperty(key1, value1);
        input.setProperty(key2, value2);
        input.setProperty(key3, value3);
        input.setProperty(key4, value4);

        String keyprefix = "pro";

        // テスト対象の実行
        Enumeration result = PropertyUtil.getPropertyNames(input, keyprefix);

        // 結果確認
        // 生成されたEnumurationに入ってることの確認
        Set set = new HashSet();
        set.add("property.test002.id.2");
        set.add("property.test004.id.0");
        assertTrue(set.contains(result.nextElement()));
        assertTrue(set.contains(result.nextElement()));
        assertFalse(result.hasMoreElements());
    }

    /**
     * testGetPropertiesValues01(Properties, Enumeration)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティ（1つ）、キーの一覧（1つ）<br>
     * 期待値 :値セット（1つ）<br>
     * 説明：プロパティオブジェクトの中身が１つで、キー一覧の中身も１つの時、 指定されたプロパティから値1つ取得されていることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertiesValues01() throws Exception {
        // 入力値の設定
        String key1 = "SystemExceptionHandlerTest.key";
        String value1 = "{0}message";

        Properties input = new Properties();
        input.setProperty(key1, value1);

        Enumeration em = new StringTokenizer("SystemExceptionHandlerTest.key");
        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, em);

        // 結果確認
        assertTrue(result.contains("{0}message"));
    }

    /**
     * testGetPropertiesValues02(Properties, Enumeration)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティ（複数）、キーの一覧（複数）<br>
     * 期待値 :値セット（複数）<br>
     * 説明：プロパティオブジェクトの中身が複数の時、 指定されたプロパティから値が複数取得されていることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertiesValues02() throws Exception {
        // 入力値の設定
        String key1 = "property.test001.id.0";
        String value1 = "test";

        String key2 = "property.test002.id.0";
        String value2 = "test0";

        String key3 = "property.test002.id.1";
        String value3 = "test1";

        Properties input = new Properties();
        input.setProperty(key1, value1);
        input.setProperty(key2, value2);
        input.setProperty(key3, value3);

        Enumeration em = new StringTokenizer("property.test001.id.0 property.test002.id.0 property.test002.id.1");

        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, em);

        // 結果確認
        assertTrue(result.contains("test0"));
        assertTrue(result.contains("test1"));
        assertTrue(result.contains("test"));
    }

    /**
     * testGetPropertiesValues03(Properties, Enumeration)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティ（値に空があるものを含む）、キーの一覧（複数）<br>
     * 期待値 :値セット（値が空のものについては、空と表示される）<br>
     * 説明：プロパティオブジェクトの中に、キーに対する値が空なものが含まれる場合 " "で取得されていることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertiesValues03() throws Exception {
        // 入力値の設定
        String key1 = "property.test001.id.0";
        String value1 = "test";

        String key2 = "property.test002.id.0";
        String value2 = "test0";

        String key3 = "property.test003.id.0";
        String value3 = "";

        Properties input = new Properties();
        input.setProperty(key1, value1);
        input.setProperty(key2, value2);
        input.setProperty(key3, value3);

        Enumeration em = new StringTokenizer("property.test001.id.0 property.test002.id.0 property.test003.id.0");

        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, em);

        // 結果確認
        assertTrue(result.contains("test0"));
        assertTrue(result.contains("test"));
        assertTrue(result.contains(""));
    }

    /**
     * testGetPropertiesValues04(Properties, Enumeration)。<br>
     * （正常系）<br>
     * 観点：C,F<br>
     * <br>
     * 入力値 :プロパティがnull、キーの一覧<br>
     * 期待値 :null<br>
     * 説明：プロパティがnullの時、nullを戻り値として処理を終了することを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertiesValues04() throws Exception {
        // 入力値の設定
        Properties input = null;

        Enumeration em = new StringTokenizer("property.test001.id.0");
        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, em);

        // 結果確認
        assertNull(result);
    }

    /**
     * testGetPropertiesValues05(Properties, Enumeration)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティ、キーの一覧がnull<br>
     * 期待値 :null<br>
     * 説明：キーの一覧がnullの時、nullを戻り値として処理を終了することを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertiesValues05() throws Exception {
        // 入力値の設定
        Properties input = new Properties();
        String key1 = "property.test001.id.0";
        String value1 = "test";
        input.setProperty(key1, value1);

        Enumeration em = null;
        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, em);

        // 結果確認
        assertNull(result);
    }

    /**
     * testGetPropertiesValues06(Properties, Enumeration)。<br>
     * （正常系）<br>
     * 観点：C,F<br>
     * <br>
     * 入力値 :プロパティが空、キーの一覧<br>
     * 期待値 :"null"<br>
     * 説明：プロパティが空の時、"null"で取得されることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertiesValues06() throws Exception {
        // 入力値の設定
        Properties input = new Properties();

        Enumeration em = new StringTokenizer("property.test001.id.0");
        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, em);

        // 結果確認
        assertTrue(result.contains(null));
    }

    /**
     * testGetPropertiesValues07(Properties, Enumeration)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティ、キーの一覧が空<br>
     * 期待値 :空<br>
     * 説明：キーの一覧が空の時、空が取得されることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertiesValues07() throws Exception {
        // 入力値の設定
        Properties input = new Properties();
        String key1 = "property.test001.id.0";
        String value1 = "test";
        input.setProperty(key1, value1);

        Enumeration em = new StringTokenizer("");

        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, em);

        // 結果確認
        assertTrue(result.isEmpty());
    }

    /**
     * testGetPropertiesValues08(Properties, Enumeration)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :キーが複数存在するプロパティオブジェクト<br>
     * 期待値 :値セット<br>
     * 説明：キーが複数存在するプロパティオブジェクトの場合 複数のうち1つの値が取得されていることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertiesValues08() throws Exception {
        // 入力値の設定
        Properties input = new Properties();
        String key1 = "property.test004.id.0";
        String value1 = "testA";

        String key2 = "property.test004.id.0";
        String value2 = "testB";

        input.setProperty(key1, value1);
        input.setProperty(key2, value2);

        Enumeration em = new StringTokenizer("property.test004.id.0");

        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, em);

        // 結果確認
        assertTrue(result.contains("testB"));
        assertFalse(result.contains("testA"));
    }

    /**
     * testGetPropertiesValues09(Properties, Enumeration)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :キー一覧に存在しないプロパティキー<br>
     * 期待値 :"null"<br>
     * 説明：プロパティのキーがキー一覧に存在しない場合、"null"で取得されることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPropertiesValues09() throws Exception {
        // 入力値の設定
        Properties input = new Properties();
        String key1 = "property.test001.id.0";
        String value1 = "test";

        input.setProperty(key1, value1);

        Enumeration em = new StringTokenizer("property.test004.id.0");

        // テスト対象の実行
        Set result = PropertyUtil.getPropertiesValues(input, em);

        // 結果確認
        assertTrue(result.contains(null));
        assertFalse(result.contains("test"));
    }

    /**
     * testLoadProperties01(String)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティファイル名<br>
     * 期待値 :プロパティオブジェクト（中身が１つ）<br>
     * 説明：プロパティオブジェクトの中身が１つの時、 指定されたプロパティファイルがロードされていることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLoadProperties01() throws Exception {
        // 入力値の設定
        String input = "test_message_01_en_US";

        // テスト対象の実行
        Properties result = PropertyUtil.loadProperties(input);

        // 結果確認
        assertTrue(result.containsKey("SystemExceptionHandlerTest.key"));
        assertTrue(result.containsValue("{0}message"));
    }

    /**
     * testLoadProperties02(String)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :プロパティファイル名<br>
     * 期待値 :プロパティオブジェクト（中身が複数）<br>
     * 説明：指定されたプロパティファイルの中身の個数分ロードされていることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testLoadProperties02() throws Exception {
        // 入力値の設定
        String input = "test_message_01";

        // テスト対象の実行
        Properties result = PropertyUtil.loadProperties(input);

        // 結果確認
        assertTrue(result.containsKey(
                "SystemExceptionHandlerTest.error.message"));
        assertTrue(result.containsValue(
                "\u4f8b\u5916\u30e1\u30c3\u30bb\u30fc\u30b8"));
        assertTrue(result.containsKey(
                "SystemExceptionHandlerTest.error.message.null"));
        assertTrue(result.containsValue(""));
        assertTrue(result.containsKey("SystemExceptionHandlerTest.key"));
        assertTrue(result.containsValue(
                "{0}\u30c7\u30d5\u30a9\u30eb\u30c8\u30e1\u30c3\u30bb\u30fc\u30b8"));
    }

    /**
     * testLoadProperties03(String)。<br>
     * （正常系）<br>
     * 観点：C,F<br>
     * <br>
     * 入力値 :null<br>
     * 期待値 :null<br>
     * 説明：プロパティファイル名が存在しない場合Nullを戻り値として処理を終することを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testLoadProperties03() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト対象の実行
        Properties result = PropertyUtil.loadProperties(input);

        // 結果確認
        assertNull(result);
    }

    /**
     * testLoadProperties04(String)。<br>
     * （正常系）<br>
     * 観点：C,F<br>
     * <br>
     * 入力値 :""(空文字)<br>
     * 期待値 :null<br>
     * 説明：プロパティファイル名が存在しない場合Nullを戻り値として処理を終了することを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testLoadProperties04() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト対象の実行
        Properties result = PropertyUtil.loadProperties(input);

        // 結果確認
        assertNull(result);
    }

    /**
     * testLoadProperties05(String)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :中身に何も入っていないプロパティファイル名<br>
     * 期待値 :空のプロパティオブジェクト<br>
     * 説明：指定されたプロパティファイルの中身が空の時、 空のプロパティオブジェクトが取り出されていることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testLoadProperties05() throws Exception {
        // 入力値の設定
        String input = "test_message_10";

        // テスト対象の実行
        Properties result = PropertyUtil.loadProperties(input);

        // 結果確認
        assertTrue(result.isEmpty());
    }

    /**
     * testLoadProperties06(String)。<br>
     * （正常系）<br>
     * 観点：F<br>
     * <br>
     * 入力値 :存在しないプロパティファイル名<br>
     * 期待値 :null<br>
     * ログ："*** Can not find property-file [test_me.properties] ***" 説明：存在しないファイル名が指定された時、 nullを戻り値として処理を終了することを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testLoadProperties06() throws Exception {
        // 入力値の設定
        String input = "test_me";

        // テスト対象の実行
        Properties result = PropertyUtil.loadProperties(input);

        // 結果確認
        assertTrue(logger.getLoggingEvents().get(0).getMessage().equals(
                "*** Can not find property-file" + " [test_me.properties] ***")
                && logger.getLoggingEvents().get(0).getLevel().equals(
                        Level.WARN));
        assertNull(result);
    }

    /**
     * testLoadProperties07(String)。<br>
     * （正常系）<br>
     * 観点：A,F<br>
     * <br>
     * 入力値 :拡張子を含むプロパティファイル名<br>
     * 期待値 :プロパティオブジェクト<br>
     * 説明：引数のプロパティファイル名にすでに拡張子が含まれている場合、 指定されたプロパティファイルがロードされていることを確認する。
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLoadProperties07() throws Exception {
        // 入力値の設定
        String input = "test_message_01_en_US.properties";

        // テスト対象の実行
        Properties result = PropertyUtil.loadProperties(input);

        // 結果確認
        assertTrue(result.containsKey("SystemExceptionHandlerTest.key"));
        assertTrue(result.containsValue("{0}message"));
    }

    /**
     * testGetPropertiesPathStringString01()。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値 :ディレクトリ付きファイル名= subDir/PropertyUtil.class<br>
     * 結合対象のファイル名=hoge.txt 期待値 :subDir"ファイルセパレータ(OSにより異なる)"hoge.txt<br>
     * 第一引数のフルパスファイル名から、ディレクトリ＋第二引数ファイル名 が出力されることを確認する。
     * @throws Exception
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertiesPathStringString01() throws Exception {
        // テスト設定
        // getPropertiesPathの引数クラス要素
        Class[] clz = new Class[] { String.class, String.class };
        // getPropertiesPathの引数オブジェクト要素
        Object[] obj = new Object[] { "subDir/PropertyUtil.class", "hoge.txt" };

        // テスト実行
        Method method = PropertyUtil.class.getDeclaredMethod(
                "getPropertiesPath", clz);
        method.setAccessible(true);
        Object retObj = method.invoke(PropertyUtil.class, obj);

        // テスト結果
        assertEquals("subDir" + System.getProperty("file.separator")
                + "hoge.txt", retObj);
    }

    /**
     * testGetPropertiesPathStringString02()。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値 : (引数)resource：null<br>
     * (引数)addFile："/hoge.txt"<br>
     * 期待値 : (戻り値)String：-<br>
     * (例外)：NullPointerException 引数resourceがnullの場合
     * @throws Exception
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertiesPathStringString02() throws Exception {
        // テスト設定
        // getPropertiesPathの引数クラス要素
        Class[] clz = new Class[] { String.class, String.class };
        // getPropertiesPathの引数オブジェクト要素
        Object[] obj = new Object[] { null, "hoge.txt" };

        // テスト実行
        try {
            Method method = PropertyUtil.class.getDeclaredMethod(
                    "getPropertiesPath", clz);
            method.setAccessible(true);
            method.invoke(PropertyUtil.class, obj);
            fail();
        } catch (InvocationTargetException e) {
            assertTrue(e.getTargetException() instanceof NullPointerException);
        } catch (NullPointerException e) {
            fail();
        }
    }

    /**
     * testGetPropertiesPathStringString03()。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値 : (引数)resource："subDir/PropertyUtil.class"<br>
     * (引数)addFile：null<br>
     * 期待値 : (戻り値)String："subDir/null"<br>
     * (例外)：- 引数addFileがnullの場合
     * @throws Exception
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertiesPathStringString03() throws Exception {
        // テスト設定
        // getPropertiesPathの引数クラス要素
        Class[] clz = new Class[] { String.class, String.class };
        // getPropertiesPathの引数オブジェクト要素
        Object[] obj = new Object[] { "subDir/PropertyUtil.class", null };

        // テスト実行
        Method method = PropertyUtil.class.getDeclaredMethod(
                "getPropertiesPath", clz);
        method.setAccessible(true);
        Object retObj = method.invoke(PropertyUtil.class, obj);

        // テスト結果
        assertEquals("subDir" + System.getProperty("file.separator") + "null",
                retObj);
    }

    /**
     * testGetPropertiesPathStringString04()。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値 : (引数)resource：""<br>
     * (引数)addFile：""<br>
     * 期待値 : (戻り値)String：""<br>
     * (例外)：- 引数が空白の場合
     * @throws Exception
     * @throws Exception 例外
     */
    @Test
    public void testGetPropertiesPathStringString04() throws Exception {
        // テスト設定
        // getPropertiesPathの引数クラス要素
        Class[] clz = new Class[] { String.class, String.class };
        // getPropertiesPathの引数オブジェクト要素
        Object[] obj = new Object[] { "", "" };

        // テスト実行
        Method method = PropertyUtil.class.getDeclaredMethod(
                "getPropertiesPath", clz);
        method.setAccessible(true);
        Object retObj = method.invoke(PropertyUtil.class, obj);

        // テスト結果
        assertEquals("", retObj);
    }

}
