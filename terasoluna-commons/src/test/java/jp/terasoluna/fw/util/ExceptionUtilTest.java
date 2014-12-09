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

import javax.servlet.ServletException;

import jp.terasoluna.fw.exception.SystemException;
import junit.framework.TestCase;

/**
 * ExceptionUtil ブラックボックステスト。<br>
 * <br>
 * (前提条件)
 * 　　　　なし<br>
 * 
 * <br>
 */
public class ExceptionUtilTest extends TestCase {

    /**
     * Constructor for ExceptionUtilTest.
     * @param arg0
     */
    public ExceptionUtilTest(String arg0) {
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
     * testGetStackTrace01()。<br>
     * 
     * （正常系）<br>
     * 観点：A<br>
     * 入力値 :1つの例外オブジェクト<br>
     * 期待値 :スタックトレース<br>
     *
     *  1つの例外オブジェクトからスタックトレースが取得できること。<br>
     *
     */
    public void testGetStackTrace01() {
        //初期設定
        NullPointerException ne = new NullPointerException();

        //テスト実行
        String result = ExceptionUtil.getStackTrace(ne);

        //SystemException、NullPointerExceptionの
        //スタックトレースの第一行目が含まれていること。
        assertTrue(result.indexOf("java.lang.NullPointerException") != -1);
    }

    /**
     * testGetStackTrace02()。<br>
     * 
     * （正常系）<br>
     * 観点：A<br>
     * 入力値 :例外オブジェクトを内包する例外オブジェクト<br>
     * 期待値 :発生左記の例外から順に連結されたスタックトレース<br>
     *
     * 例外オブジェクトを内包する例外オブジェクトに対して実行すると、
     * 発生先の例外から順にスタックトレースが連結されて取得できること。<br>
     *
     */
    public void testGetStackTrace02() {
        //初期設定
        SystemException se = new SystemException(new ServletException(new NullPointerException()));

        //テスト実行
        String result = ExceptionUtil.getStackTrace(se);


        //SystemException、ServletException、NullPointerExceptionの
        //スタックトレースの第一行目が含まれていること。
        assertTrue(
            result.indexOf(
                "jp.terasoluna.fw.exception.SystemException")
                != -1);
        assertTrue(result.indexOf("\njavax.servlet.ServletException") != -1);
        assertTrue(result.indexOf("\njava.lang.NullPointerException") != -1);

        // SystemException→ServletExceptionの順で、出力されていること
        assertTrue(
            result.indexOf(
                "jp.terasoluna.framework.exception.SystemException")
                < result.indexOf("\njavax.servlet.ServletException"));

        // ServletException→NullPointerExceptionの順で、出力されていること
        assertTrue(
            result.indexOf(
                "\njavax.servlet.ServletException")
                < result.indexOf("\njava.lang.NullPointerException"));
    }

    /**
     * testGetStackTrace03()。<br>
     * 
     * （正常系）<br>
     * 観点：C<br>
     * 入力値 :引数がnull<br>
     * 期待値 :空文字<br>
     *
     * 引数がnullの時、空のスタックトレースが返却されることを確認する。<br>
     *
     */
    public void testGetStackTrace03() {
        //テスト実行
        String trace = ExceptionUtil.getStackTrace(null);

        //結果確認
        assertEquals("", trace);
    }
}
