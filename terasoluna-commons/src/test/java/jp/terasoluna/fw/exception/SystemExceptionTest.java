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

package jp.terasoluna.fw.exception;

import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

/**
 * SystemException ブラックボックステスト。<br>
 *
 *
 * @version 2004/04/21
 */

public class SystemExceptionTest extends TestCase {

    /**
      * テスト用SystemExceptionフィールド。
      */
    private SystemException se1 = null;

    /**
     * SystemExceptionをテストする際に行う初期化処理。
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * testSystemExceptionThrowable01()<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：cause = not null<br>
     * 期待値：cause = not null, errorCode = ""が設定される。<br>
     * 
     * 概要：引数causeがNotNullで、メッセージがありの場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowable01() throws Exception {

        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        assertEquals("testException", throwWord);
        assertEquals("", errorCode);

    }

    /**
     * testSystemExceptionThrowable02()<br>
     *
     * (正常系)<br>
     * 観点：C<br>
     *
     * 入力値：cause = ""<br>
     * 期待値：cause = "", errorCode = ""が設定される。<br>
     * 
     * 概要：引数causeがNotNullで、メッセージが空白の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowable02() throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("");

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        assertEquals("", throwWord);
        assertEquals("", errorCode);
    }

    /**
     * testSystemExceptionThrowable03()<br>
     *
     * (正常系)<br>
     * 観点：C<br>
     *
     * 入力値：cause = null<br>
     * 期待値：cause = null, errorCode = ""が設定される。<br>
     * 
     * 概要：引数causeがnullの場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowable03() throws Exception {

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(null);

        // 出力値の確認。
        Throwable throWord = (Throwable) UTUtil.getPrivateField(se1, "cause");
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        assertNull(throWord);
        assertEquals("", errorCode);
    }

    /**
     * testSystemExceptionThrowableString01()<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：cause = not null, errorCode = not null<br>
     * 期待値：cause = not null, errorCode = not nullが設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableString01() throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String errorCode = "test01";

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, errorCode);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        assertEquals("testException", throwWord);
        assertEquals("test01", errorCode);
    }

    /**
     * testSystemExceptionThrowableString02()<br>
     *
     * (正常系)<br>
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = ""<br>
     * 期待値：cause = not null, errorCode = ""が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが空白の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableString02() throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, "");

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        assertEquals("testException", throwWord);
        assertEquals("", errorCode);
    }
    /**
     * testSystemExceptionThrowableString03<br>
     *
     * (正常系)<br>
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = null<br>
     * 期待値：cause = not null, errorCode = nullが設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeがnullの場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableString03() throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, null);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        assertEquals("testException", throwWord);
        assertNull(errorCode);
    }

    /**
     * testSystemExceptionThrowableStringStringArray01<br>
     *
     * (正常系)<br>
     *
     * 観点：A<br>
     *
     * 入力値：cause = not null, errorCode = not null, <br>
     *        optionStrings = not null<br>
     * 期待値：cause = not null, errorCode = not null, <br>
     *        options = not nullが設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数optionsが「{ "a" }」の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringArray01()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String errorCode = "test01";
        String[] optionStrings = { "a" };

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, errorCode, optionStrings);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("test01", errorCode);
        assertEquals("a", options[0]);
    }

    /**
     * testSystemExceptionThrowableStringStringArray02()<br>
     *
     * (正常系)<br>
     *
     * 観点：A<br>
     *
     * 入力値：cause = not null, errorCode = "", <br>
     *        optionStrings = not null<br>
     * 期待値：cause = not null, errorCode = "", <br>
     *        options = not nullが設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数optionsが「{ "a", "b" }」の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringArray02()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String[] optionStrings = { "a", "b" };

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, "", optionStrings);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("", errorCode);
        assertEquals("a", options[0]);
        assertEquals("b", options[1]);
    }

    /**
     * testSystemExceptionThrowableStringStringArray03()<br>
     *
     * (正常系)<br>
     *
     * 観点：A<br>
     *
     * 入力値：cause = not null, errorCode = null, <br>
     *        optionStrings = not null<br>
     * 期待値：cause = not null, errorCode = null, <br>
     *        options = not nullが設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数optionsが「{ "a", "b", "c" }」の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringArray03()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String[] optionStrings = { "a", "b", "c" };

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, null, optionStrings);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertNull(errorCode);
        assertEquals("a", options[0]);
        assertEquals("b", options[1]);
        assertEquals("c", options[2]);
    }

    /**
     * testSystemExceptionThrowableStringStringArray04<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = "", <br>
     *        optionStrings = null<br>
     * 期待値：cause = not null, errorCode = "", <br>
     *        options = nullが設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数optionsがnullの場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringArray04()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String[] optionStrings = null;

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, "", optionStrings);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("", errorCode);
        assertNull(options);

    }

    /**
     * testSystemExceptionThrowableStringStringArray05<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = null, <br>
     *        optionStrings = not null(配列要素に空白あり)<br>
     * 期待値：cause = not null, errorCode = null, <br>
     *        options = not null(配列要素に空白あり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数optionsが「{ "", "", "" }」の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringArray05()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String[] optionStrings = { "", "", "" };

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, null, optionStrings);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals(null, errorCode);
        assertEquals("", options[0]);
        assertEquals("", options[1]);
        assertEquals("", options[2]);

    }

    /**
     * testSystemExceptionThrowableStringStringArray06<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = not null, <br>
     *        optionStrings = not null(配列要素にnullあり)<br>
     * 期待値：cause = not null, errorCode = not null, <br>
     *        options = not null(配列要素にnullあり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数optionsが「{ null, null, null }」の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringArray06()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String errorCode = "test01";
        String[] optionStrings = { null, null, null };

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, errorCode, optionStrings);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("test01", errorCode);
        assertNull(options[0]);
        assertNull(options[1]);
        assertNull(options[2]);

    }

    /**
     * testSystemExceptionThrowableStringStringArray07<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = not null, <br>
     *        optionStrings = not null(配列要素に空白、nullあり)<br>
     * 期待値：cause = not null, errorCode = not null, <br>
     *        options = not null(配列要素に空白、nullあり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数optionsが「{ "a", "", null }」の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringArray07()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String errorCode = "test01";
        String[] optionStrings = { "a", "", null };

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, errorCode, optionStrings);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("test01", errorCode);
        assertEquals("a", options[0]);
        assertEquals("", options[1]);
        assertNull(options[2]);

    }

    /**
     * testSystemExceptionThrowableStringString01()<br>
     *
     * (正常系)<br>
     *
     * 観点：A<br>
     *
     * 入力値：cause = not null, errorCode = not null, s0 = not null<br>
     * 期待値：cause = not null, errorCode = not null, <br>
     *        options = not nullが設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数s0が"a"の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringString01() throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String errorCode = "test01";
        String s0 = "a";

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, errorCode, s0);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("test01", errorCode);
        assertEquals("a", options[0]);
    }

    /**
     * testSystemExceptionThrowableStringString02()<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = "", s0 = ""<br>
     * 期待値：cause = not null, errorCode = "", <br>
     *        options = not null(配列要素に空白あり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが""、引数s0が空白の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringString02() throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, "", "");

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("", errorCode);
        assertEquals("", options[0]);
    }

    /**
     * testSystemExceptionThrowableStringString03()<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = null, s0 = null<br>
     * 期待値：cause = not null, errorCode = null, <br>
     *        options = not null(配列要素にnullあり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeがnull、引数s0がnullの場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringString03() throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String s0 = null;

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, null, s0);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertNull(errorCode);
        assertNull(options[0]);
    }

    /**
     * testSystemExceptionThrowableStringStringString01()<br>
     *
     * (正常系)<br>
     *
     * 観点：A<br>
     *
     * 入力値：cause = not null, errorCode = not null,<br>
     *        s0 = not null, s1 = not null<br>
     * 期待値：cause = not null, errorCode = not null, <br>
     *        options = not nullが設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数s0が"a"、引数s1が"b"の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringString01()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String errorCode = "test01";
        String s0 = "a";
        String s1 = "b";

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, errorCode, s0, s1);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("test01", errorCode);
        assertEquals("a", options[0]);
        assertEquals("b", options[1]);
    }

    /**
     * testSystemExceptionThrowableStringStringString02()<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = not null, s0 = "", s1 = ""<br>
     * 期待値：cause = not null, errorCode = not null, <br>
     *        options = not null(配列要素に""あり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数s0が空白、引数s1が空白の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringString02()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String errorCode = "test01";

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, errorCode, "", "");

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("test01", errorCode);
        assertEquals("", options[0]);
        assertEquals("", options[1]);
    }

    /**
     * testSystemExceptionThrowableStringStringString03()<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = "", s0 = null, s1 = null<br>
     * 期待値：cause = not null, errorCode = "", <br>
     *        options = not null(配列要素にnullあり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが空白、引数s0がnull、引数s1がnullの場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringString03()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, "", null, null);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("", errorCode);
        assertNull(options[0]);
        assertNull(options[1]);
    }

    /**
     * testSystemExceptionThrowableStringStringString04()<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = null, s0 = "", s1 = not null<br>
     * 期待値：cause = not null, errorCode = null, <br>
     *        options = not null(配列要素に空白あり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeがnull、引数s0が空白、引数s1が"a"の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringString04()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String s1 = "a";

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, null, "", s1);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertNull(errorCode);
        assertEquals("", options[0]);
        assertEquals("a", options[1]);
    }

    /**
     * testSystemExceptionThrowableStringStringString05()<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = not null, s0 = null, s1 = not null<br>
     * 期待値：cause = not null, errorCode = not null, <br>
     *        options = not null(配列要素にnullあり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数s0がnull、引数s1が"a"の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringString05()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String errorCode = "test01";
        String s1 = "a";

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, errorCode, null, s1);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("test01", errorCode);
        assertNull(options[0]);
        assertEquals("a", options[1]);
    }

    /**
     * testSystemExceptionThrowableStringStringStringString01()<br>
     *
     * (正常系)<br>
     *
     * 観点：A<br>
     *
     * 入力値：cause = not null, errorCode = not null, <br>
     *        s0 = not null, s1 = not null, s2 = not null<br>
     * 期待値：cause = not null, errorCode = not null, <br>
     *        options = not nullが設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数s0が"a"、引数s1が"b"、引数s2が"c"の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringStringString01()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String errorCode = "test01";
        String s0 = "a";
        String s1 = "b";
        String s2 = "c";

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, errorCode, s0, s1, s2);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("test01", errorCode);
        assertEquals("a", options[0]);
        assertEquals("b", options[1]);
        assertEquals("c", options[2]);
    }

    /**
     * testSystemExceptionThrowableStringStringStringString02()<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = "", <br>
     *        s0 = "", s1 = "", s2 = ""<br>
     * 期待値：cause = not null, errorCode = "", <br>
     *        options = not null(配列要素に空白あり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが空白、引数s0が空白、引数s1が空白、引数s2が空白の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringStringString02()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, "", "", "", "");

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("", errorCode);
        assertEquals("", options[0]);
        assertEquals("", options[1]);
        assertEquals("", options[2]);
    }

    /**
     * testSystemExceptionThrowableStringStringStringString03()<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = null, <br>
     *        s0 = null, s1 = null, s2 = null<br>
     * 期待値：cause = not null, errorCode = null, <br>
     *        options = not null(配列要素に空白あり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeがnull、引数s0がnull、引数s1がnull、引数s2がnullの場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringStringString03()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, null, null, null, null);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertNull(errorCode);
        assertNull(options[0]);
        assertNull(options[1]);
        assertNull(options[2]);
    }

    /**
     * testSystemExceptionThrowableStringStringStringString04()<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = not null,<br>
     *        s0 = not null, s1 = "", s2 = null<br>
     * 期待値：cause = not null, errorCode = not null,<br>
     *        options = not nullが設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数s0が"a"、引数s1が空白、引数s2がnullの場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringStringString04()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String errorCode = "test01";
        String s0 = "a";

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, errorCode, s0, "", null);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("test01", errorCode);
        assertEquals("a", options[0]);
        assertEquals("", options[1]);
        assertNull(options[2]);
    }

    /**
     * testSystemExceptionThrowableStringStringStringStringString01()<br>
     *
     * (正常系)<br>
     *
     * 観点：A<br>
     *
     * 入力値：cause = not null, errorCode = null,<br>
     *        s0 = not null, s1 = not null, s2 = not null, s3 = not null<br>
     * 期待値：cause = not null, errorCode = null, <br>
     *        options = not nullが設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeがnull、引数s0が"a"、引数s1が"b"、引数s2が"c"、引数s3が"d"の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringStringStringString01()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String s0 = "a";
        String s1 = "b";
        String s2 = "c";
        String s3 = "d";

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, null, s0, s1, s2, s3);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertNull(errorCode);
        assertEquals("a", options[0]);
        assertEquals("b", options[1]);
        assertEquals("c", options[2]);
        assertEquals("d", options[3]);
    }

    /**
     * testSystemExceptionThrowableStringStringStringStringString02()<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = not null,<br>
     *        s0 = "", s1 = "", s2 = "", s3 = ""<br>
     * 期待値：cause = not null, errorCode = not null, <br>
     *        options = not null(配列要素に空白あり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが"test01"、引数s0が空白、引数s1が空白、引数s2が空白、引数s3が空白の場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringStringStringString02()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String errorCode = "test01";

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, errorCode, "", "", "", "");

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("test01", errorCode);
        assertEquals("", options[0]);
        assertEquals("", options[1]);
        assertEquals("", options[2]);
        assertEquals("", options[3]);
    }

    /**
     * testSystemExceptionThrowableStringStringStringStringString03()<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = "", <br>
     *        s0 = null, s1 = null, s2 = null, s3 = null<br>
     * 期待値：cause = not null, errorCode = "", <br>
     *        options = not null(配列要素にnullあり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeが空白、引数s0がnull、引数s1がnull、引数s2がnull、引数s3がnullの場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringStringStringString03()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, "", null, null, null, null);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertEquals("", errorCode);
        assertNull(options[0]);
        assertNull(options[1]);
        assertNull(options[2]);
        assertNull(options[3]);
    }

    /**
     * testSystemExceptionThrowableStringStringStringStringString04()<br>
     *
     * (正常系)<br>
     *
     * 観点：C<br>
     *
     * 入力値：cause = not null, errorCode = null,<br>
     *        s0 = not null, s1 = not null, s2 = "", s3 = null<br>
     * 期待値：cause = not null, errorCode = null, <br>
     *        options = not null(配列要素に空白、nullあり)が設定される。<br>
     * 
     * 概要：引数causeがNotNullでメッセージがあり、引数errorCodeがnull、引数s0が"a"、引数s1が"b"、引数s2が空白、引数s3がnullの場合
     * @throws Exception 例外
     */
    public void testSystemExceptionThrowableStringStringStringStringString04()
        throws Exception {
        // 入力値の設定。
        Throwable cause = new Throwable("testException");
        String s0 = "a";
        String s1 = "b";

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause, null, s0, s1, "", null);

        // 出力値の確認。
        String throwWord =
            ((Throwable) UTUtil.getPrivateField(se1, "cause")).getMessage();
        String errorCode = (String) UTUtil.getPrivateField(se1, "errorCode");
        String[] options = (String[]) UTUtil.getPrivateField(se1, "options");
        assertEquals("testException", throwWord);
        assertNull(errorCode);
        assertEquals("a", options[0]);
        assertEquals("b", options[1]);
        assertEquals("", options[2]);
        assertNull(options[3]);
    }

    /**
     * testGetErrorCode01()<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：cause = not null, errorCode = not null<br>
     * 期待値：SystemExceptionのerrorCode属性が取得できているか確認する。
     * 
     * 概要：※正常系一件のみテスト
     * @throws Exception 例外
     */
    public void testGetErrorCode01() throws Exception {
        // SystemExceptionの設定。
        Throwable cause = new Throwable("testException");

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause);
        UTUtil.setPrivateField(se1, "errorCode", "abc");

        // テスト対象メソッドの実行と出力値の確認。
        assertEquals("abc", se1.getErrorCode());
    }

    /**
     * testGetOptions01()<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：options = not null<br>
     * 期待値：SystemExceptionのoptions属性が取得できているか確認する。
     * 
     * 概要：※正常系一件のみテスト
     * @throws Exception 例外
     */
    public void testGetOptions01() throws Exception {
        // SystemExceptionの設定。
        Throwable cause = new Throwable("testException");
        String[] options = { "a", "b" };
        se1 = new SystemException(cause);
        UTUtil.setPrivateField(se1, "options", options);

        // テスト対象メソッドの実行と出力値の確認。
        assertEquals("a", se1.getOptions()[0]);
        assertEquals("b", se1.getOptions()[1]);
        assertEquals(2, se1.getOptions().length);
    }

    /**
     * testSetMessage01()<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：cause = not null, message = not null<br>
     * 期待値：SystemExceptionのmessage属性が入力できているか確認する。
     * 
     * 概要：※正常系一件のみテスト
     * @throws Exception 例外
     */
    public void testSetMessage01() throws Exception {
        // SystemExceptionの設定。
        Throwable cause = new Throwable("testException");
        se1 = new SystemException(cause);
        String message = "abc";

        // テスト対象メソッドの実行。
        se1.setMessage(message);

        // 出力値の確認。
        assertEquals("abc", UTUtil.getPrivateField(se1, "message"));
    }

    /**
     * testGetMessage01()<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：cause = not null, message = not null<br>
     * 期待値：SystemExceptionのmessage属性が取得できているか確認する。
     * 
     * 概要：messageの値がnot nullの場合、messageの値が取得できることを確認する。
     * @throws Exception 例外
     */
    public void testGetMessage01() throws Exception {
        // SystemExceptionの設定。
        Throwable cause = new Throwable("testException");

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause);
        UTUtil.setPrivateField(se1, "message", "abc");

        // テスト対象メソッドの実行と出力値の確認。
        assertEquals("abc", se1.getMessage());
    }

    /**
     * testGetMessage02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(状態) message:null<br>
     *         (状態) errorCode:"def"<br>
     *
     * <br>
     * 期待値：(戻り値) message:"def"<br>
     *
     * <br>
     * messageの値がnullの場合、errorCodeの値が取得できることを確認する。
     * <br>
     * 
     * 概要：messageの値がnullの場合、errorCodeの値が取得できることを確認する。
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetMessage02() throws Exception {
        // SystemExceptionの設定。
        Throwable cause = new Throwable("testException");

        // テスト対象コンストラクタの実行。
        se1 = new SystemException(cause);
        UTUtil.setPrivateField(se1, "message", null);
        UTUtil.setPrivateField(se1, "errorCode", "def");

        // テスト対象メソッドの実行と出力値の確認。
        assertEquals("def", se1.getMessage());
    }
}