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

package jp.terasoluna.fw.message;

import org.springframework.test.util.ReflectionTestUtils;

import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.message.DBMessage} クラスのブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4> メッセージリソースを保持するクラス。
 * <p>
 * @see jp.terasoluna.fw.message.DBMessage
 * @version 2005/12/5
 */
public class DBMessageTest extends TestCase {

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 終了処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * コンストラクタ。
     * @param name このテストケースの名前。
     */
    public DBMessageTest(String name) {
        super(name);
    }

    /**
     * testDBMessage01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) code:"abc"<br>
     * (引数) language:"def"<br>
     * (引数) country:"ghi"<br>
     * (引数) variant:"jkl"<br>
     * (引数) message:"mno"<br>
     * <br>
     * 期待値：(状態変化) code:"abc"<br>
     * (状態変化) language:"def"<br>
     * (状態変化) country:"ghi"<br>
     * (状態変化) variant:"jkl"<br>
     * (状態変化) message:"mno"<br>
     * <br>
     * 引数として与えられた値になるかを確認。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testDBMessage01() throws Exception {
        // 前処理
        DBMessage db = new DBMessage("abc", "def", "ghi", "jkl", "mno");

        // テスト実施

        // 判定
        assertEquals("abc", ReflectionTestUtils.getField(db, "code"));
        assertEquals("def", ReflectionTestUtils.getField(db, "language"));
        assertEquals("ghi", ReflectionTestUtils.getField(db, "country"));
        assertEquals("jkl", ReflectionTestUtils.getField(db, "variant"));
        assertEquals("mno", ReflectionTestUtils.getField(db, "message"));
    }

    /**
     * testGetCode01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(状態) code:"abc"<br>
     * <br>
     * 期待値：(戻り値) String:"abc"<br>
     * <br>
     * 正常系1件のみテスト <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetCode01() throws Exception {
        // 前処理
        DBMessage db = new DBMessage(null, null, null, null, null);
        ReflectionTestUtils.setField(db, "code", "abc");

        // テスト実施
        String returnCode = db.getCode();

        // 判定
        assertEquals("abc", returnCode);
    }

    /**
     * testGetLanguage01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(状態) language:"abc"<br>
     * <br>
     * 期待値：(戻り値) String:"abc"<br>
     * <br>
     * 正常系1件のみテスト <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetLanguage01() throws Exception {
        // 前処理
        DBMessage db = new DBMessage(null, null, null, null, null);
        ReflectionTestUtils.setField(db, "language", "abc");

        // テスト実施
        String returnLanguage = db.getLanguage();

        // 判定
        assertEquals("abc", returnLanguage);
    }

    /**
     * testGetCountry01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(状態) country:"abc"<br>
     * <br>
     * 期待値：(戻り値) String:"abc"<br>
     * <br>
     * 正常系1件のみテスト <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetCountry01() throws Exception {
        // 前処理
        DBMessage db = new DBMessage(null, null, null, null, null);
        ReflectionTestUtils.setField(db, "country", "abc");

        // テスト実施
        String returnCountry = db.getCountry();
        // 判定
        assertEquals("abc", returnCountry);
    }

    /**
     * testGetVariant01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(状態) variant:"abc"<br>
     * <br>
     * 期待値：(戻り値) String:"abc"<br>
     * <br>
     * 正常系1件のみテスト <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetVariant01() throws Exception {
        // 前処理
        DBMessage db = new DBMessage(null, null, null, null, null);
        ReflectionTestUtils.setField(db, "variant", "abc");

        // テスト実施
        String returnVariant = db.getVariant();

        // 判定
        assertEquals("abc", returnVariant);
    }

    /**
     * testGetMessage01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(状態) message:"abc"<br>
     * <br>
     * 期待値：(戻り値) String:"abc"<br>
     * <br>
     * 正常系1件のみテスト <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetMessage01() throws Exception {
        // 前処理
        DBMessage db = new DBMessage(null, null, null, null, null);
        ReflectionTestUtils.setField(db, "message", "abc");

        // テスト実施
        String returnMessage = db.getMessage();

        // 判定
        assertEquals("abc", returnMessage);
    }
}
