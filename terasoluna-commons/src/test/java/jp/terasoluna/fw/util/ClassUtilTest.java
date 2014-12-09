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

import junit.framework.TestCase;

/**
 * ClassUtil ブラックボックステスト。<br>
 * <br>
 * (前提条件)<br>
 *     なし
 * <br>
 */
@SuppressWarnings("unused")
public class ClassUtilTest extends TestCase {

    /**
     * Constructor for ClassUtilTest.
     * @param arg0
     */
    public ClassUtilTest(String arg0) {
        super(arg0);
    }

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * testCreate01(String)。<br>
     * 
     * （正常系）<br>
     * 観点：E<br>
     * <br>
     * 入力値 :生成するオブジェクトのクラス名<br>
     * 期待値 :生成したインスタンス<br>
     *
     * 生成するオブジェクトのクラス名を元にインスタンスを生成することを確認する。<br>
     *
     */
    public void testCreateString01() {
        //初期設定
        String input = "java.lang.String";

        try {
            //テスト実行
            Object obj = ClassUtil.create(input);

            //結果確認
            assertEquals("java.lang.String", obj.getClass().getName());
        } catch (ClassLoadException e) {
            fail();
        }
    }

    /**
     * testCreate02(String)。<br>
     * 
     * （正常系）<br>
     * 観点：E<br>
     * <br>
     * 入力値 :生成するオブジェクトのクラス名(Terasoluna対応)<br>
     * 期待値 :生成したインスタンス<br>
     *
     * 生成するオブジェクトのTerasolunaに対応したクラス名を元に
     * インスタンスを生成することを確認する。<br>
     *
     */
    public void testCreateString02() {
        //初期設定
        String input = "jp.terasoluna.fw.util.ClassUtil";

        try {
            //テスト実行
            Object obj = ClassUtil.create(input);

            //結果確認
            assertEquals(
                "jp.terasoluna.fw.util.ClassUtil",
                obj.getClass().getName());
        } catch (ClassLoadException e) {
            fail();
        }
    }

    /**
     * testCreate03(String)。<br>
     * 
     * （正常系）<br>
     * 観点：G<br>
     * <br>
     * 入力値 :抽象クラス、インタフェースクラス名<br>
     * 期待値 :InstantiationException<br>
     *
     * 指定したクラスがnullコンストラクタを保持しないクラスの場合、
     * InstantiationExceptionが発生することを確認する。<br>
     *
     */
    public void testCreateString03() {
        //初期設定
        String input = "javax.swing.AbstractAction";

        try {
            //テスト実行
            ClassUtil.create(input);
            fail();
        } catch (ClassLoadException e) {
            if (e.getMessage().indexOf("InstantiationException") == -1) {
                //結果確認
                fail();
            }
        }
    }

    /**
     * testCreate04(String)。<br>
     * 
     * （正常系）<br>
     * 観点：G<br>
     * <br>
     * 入力値 :*.classがないクラス名<br>
     * 期待値 :ClassNotFoundException<br>
     *
     * 指定したクラスが存在しない場合、
     * ClassNotFoundExceptionが発生することを確認する。<br>
     *
     */
    public void testCreateString04() {
        //初期設定
        String input = "java.lang.Str";

        try {
            //テスト実行
            ClassUtil.create(input);
            fail();
        } catch (ClassLoadException e) {
            if (e.getMessage().indexOf("ClassNotFoundException") == -1) {
                //結果確認
                fail();
            }
        }
    }

    /**
     * testCreate05(String)。<br>
     * 
     * （正常系）<br>
     * 観点：G<br>
     * <br>
     * 入力値 :アクセスできないコンストラクタ<br>
     * 期待値 :IllegalAccessException<br>
     *
     * 指定したクラスのコンストラクタにアクセスできない場合、
     * IllegalAccessExceptionが発生することを確認する。<br>
     *
     */
    public void testCreateString05() {
        //初期設定
        String input = "java.lang.Void";

        try {
            //テスト実行
            ClassUtil.create(input);

        } catch (ClassLoadException e) {
            if (e.getMessage().indexOf("IllegalAccessException") == -1) {
                //結果確認
                fail();
            }
        }
    }

    /**
     * testCreate01(String, Object[])。<br>
     * 
     * （正常系）<br>
     * 観点：E<br>
     * <br>
     * 入力値 :生成するオブジェクトのクラス名、コンストラクタのパラメータ1つ<br>
     * 期待値 :生成したインスタンス<br>
     *
     * コンストラクタの引数が1つの時、
     * 生成するオブジェクトのクラス名を元にインスタンスを生成することを確認する。<br>
     *
     */
    public void testCreateStringObjectArray01() {
        //初期設定
        String input = "java.lang.Integer";

        try {
            //テスト実行
            Object obj = ClassUtil.create(input, new Object[] { "12" });
            java.lang.Integer resultObj = (java.lang.Integer) obj;

            //結果確認
            //クラス名
            assertEquals("java.lang.Integer", obj.getClass().getName());

            //内容
            assertEquals("12", resultObj.toString());

        } catch (ClassLoadException e) {
            fail();
        }
    }

    /**
     * testCreate02(String, Object[])。<br>
     * 
     * （正常系）<br>
     * 観点：E<br>
     * <br>
     * 入力値 :生成するオブジェクトのクラス名、コンストラクタのパラメータが空<br>
     * 期待値 :生成したインスタンス<br>
     *
     * コンストラクタの引数が空の時、
     * 生成するオブジェクトのクラス名を元にインスタンスを生成することを確認する。<br>
     *
     */
    public void testCreateStringObjectArray02() {
        //初期設定
        String input = "java.lang.String";

        try {
            //テスト実行
            Object obj = ClassUtil.create(input, new Object[]{});
            java.lang.String resultObj = (java.lang.String) obj;

            //結果確認
            //クラス名
            assertEquals("java.lang.String", obj.getClass().getName());

            //内容
            assertEquals("", resultObj);

        } catch (ClassLoadException e) {
            fail();
        }
    }

    /**
     * testCreate03(String, Object[])。<br>
     * 
     * （正常系）<br>
     * 観点：E<br>
     * <br>
     * 入力値 :生成するオブジェクトのクラス名、コンストラクタのパラメータ2つ<br>
     * 期待値 :生成したインスタンス<br>
     *
     * コンストラクタの引数が2つの時、
     * 生成するオブジェクトのクラス名を元にインスタンスを生成することを確認する。<br>
     *
     */
    public void testCreateStringObjectArray03() {
        //初期設定
        String input = "java.util.Locale";
        String language = "es";
        String country = "MEXICO";

        try {
            //テスト実行
            Object obj =
                ClassUtil.create(input, new Object[] { language, country });
            java.util.Locale resultObj = (java.util.Locale) obj;

            //結果確認
            //クラス名
            assertEquals("java.util.Locale", obj.getClass().getName());

            //内容
            assertEquals("MEXICO", resultObj.getCountry());
            assertEquals("es", resultObj.getLanguage());

        } catch (ClassLoadException e) {
            fail();
        }
    }

    /**
     * testCreate04(String, Object[])。<br>
     * 
     * （正常系）<br>
     * 観点：E<br>
     * <br>
     * 入力値 :生成するオブジェクトのクラス名(Terasoluna対応)、コンストラクタのパラメータ<br>
     * 期待値 :生成したインスタンス<br>
     *
     * 生成するオブジェクトのTerasolunaに対応したクラス名を元に
     * インスタンスを生成することを確認する。<br>
     *
     */
    public void testCreateStringObjectArray04() {
        //初期設定
        String input = ClassUtil.class.getName();

        try {
            //テスト実行
            Object obj = ClassUtil.create(input, new Object[] {});

            //結果確認
            assertEquals(input, obj.getClass().getName());

        } catch (ClassLoadException e) {
            fail();
        }
    }

    /**
     * testCreate05(String, Object[])。<br>
     * 
     * （正常系）<br>
     * 観点：G<br>
     * <br>
     * 入力値 :*.classがないクラス名<br>
     * 期待値 :ClassNotFoundException<br>
     *
     * 指定したクラスが存在しない場合、ClassNotFoundExceptionが発生し、
     * それをラップするClassLoadExceptionが発生することを確認する。<br>
     *
     */
    public void testCreateStringObjectArray05() {
        //初期設定
        String input = "java.lang.Str";

        try {
            //テスト実行
            ClassUtil.create(input, new Object[] {});
        } catch (ClassLoadException e) {
            if (e.getMessage().indexOf("ClassNotFoundException") == -1) {
                //結果確認
                fail();
            }
        }
    }

    /**
     * testCreate06(String, Object[])。<br>
     * 
     * （正常系）<br>
     * 観点：G<br>
     * <br>
     * 入力値 :抽象クラス<br>
     * 期待値 :InstantiationException<br>
     * 
     * 指定したクラスが抽象クラスの場合、InstantiationExceptionが発生し、
     * それをラップする例外ClassLoadExceptionが発生することを確認する。<br>
     *
     */
    public void testCreateStringObjectArray06() {
        //初期設定
        String input = "javax.swing.AbstractAction";

        try {
            //テスト実行
            ClassUtil.create(input, new Object[] { "1", "2" });
        } catch (ClassLoadException e) {
            if (e.getMessage().indexOf("InstantiationException") == -1) {
                //結果確認
                fail();
            }
        }
    }

    /**
     * testCreate07(String, Object[])。<br>
     * 
     * （正常系）<br>
     * 観点：G<br>
     * <br>
     * 入力値 :生成するオブジェクトのクラス名、コンストラクタのパラメータnull<br>
     * 期待値 :InvocationTargetException<br>
     *
     * オブジェクトが生成できなかった場合、InvocationTargetExceptionが発生し、
     * それをラップする例外ClassLoadExceptionが発生することを確認する。<br>
     *
     */
    public void testCreateStringObjectArray07() {
        //初期設定
        String input = "java.lang.String";

        try {
            //テスト実行
            ClassUtil.create(input, new Object[] { null });
        } catch (ClassLoadException e) {
            if (e.getMessage().indexOf("InvocationTargetException") == -1) {
                //結果確認
                fail();
            }
        }
    }

    /**
     * testCreate08(String, Object[])。<br>
     * 
     * （正常系）<br>
     * 観点：G<br>
     * <br>
     * 入力値 :生成するオブジェクトのクラス名、コンストラクタのパラメータ<br>
     * 期待値 :IllegalArgumentException<br>
     *
     * オブジェクトが生成できなかった場合、IllegalArgumentExceptionが発生し、
     * それをラップする例外ClassLoadExceptionが発生することを確認する。<br>
     *
     */
    public void testCreateStringObjectArray08() {
        //初期設定
        String input = "java.lang.Void";
        try {
            //テスト実行
            ClassUtil.create(input, new Object[] {});
        } catch (ClassLoadException e) {
            if (e.getMessage().indexOf("IllegalArgumentException") == -1) {
                //結果確認
                fail();
            }
            if (e.getCause().getMessage().indexOf("class name is java.lang.Void") == -1) {
                //結果確認
                fail();
            }
        }
    }
}
