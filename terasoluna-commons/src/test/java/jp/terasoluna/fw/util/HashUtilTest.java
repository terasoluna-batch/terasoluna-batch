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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

/**
 * HashUtilブラックボックステスト<br>
 * 
 * (前提条件)<br>
 * ・とくになし<br>
 * 
 */
public class HashUtilTest extends TestCase {

    /**
     * Constructor for HashUtilTest.
     * @param arg0 テストケースのメソッド名
     */
    public HashUtilTest(String arg0) {
        super(arg0);
    }

    /**
     * @see TestCase#setUp()
     * @throws Exception テストコードの本質とかかわりの無い例外
     * 
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see TestCase#tearDown()
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * testHash01。<br>
     * 
     * (正常系)<br>
     * 観点：A<br>
     * 
     * 入力値：半角文字列<br>
     * 期待値：半角文字列のハッシュ値<br>
     * 
     * 半角文字列のハッシュ値の取得が出来ることの確認を確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHash01() throws Exception {
        // 入力値設定
        String paramAlgorithm = "MD5";
        String paramStr = "abc";

        // テスト実行
        byte[] resultHashValue = HashUtil.hash(paramAlgorithm, paramStr);

        // 結果確認
        assertTrue(
            MessageDigest.isEqual(
                MessageDigest.getInstance("MD5").digest("abc".getBytes()),
                resultHashValue));
    }

    /**
     * testHash02。<br>
     * 
     * (正常系)<br>
     * 観点：A<br>
     * 
     * 入力値：全角文字列<br>
     * 期待値：全角文字列のハッシュ値<br>
     * 
     * 全角文字列のハッシュ値の取得が出来ることの確認を確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHash02() throws Exception {
        // 入力値設定
        String paramAlgorithm = "MD5";
        String paramStr = "あいう";

        // テスト実行
        byte[] resultHashValue = HashUtil.hash(paramAlgorithm, paramStr);

        // 結果確認
        assertTrue(
            MessageDigest.isEqual(
                MessageDigest.getInstance("MD5").digest("あいう".getBytes()),
                resultHashValue));
    }
    /**
     * testHash03。<br>
     * 
     * (正常系)<br>
     * 観点：C<br>
     * 
     * 入力値：空文字列<br>
     * 期待値：空文字列のハッシュ値<br>
     * 
     * 空文字列のハッシュ値の取得が出来ることの確認を確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHash03() throws Exception {
        // 入力値設定
        String paramAlgorithm = "MD5";
        String paramStr = "";

        // テスト実行
        byte[] resultHashValue = HashUtil.hash(paramAlgorithm, paramStr);

        // 結果確認
        assertTrue(
            MessageDigest.isEqual(
                MessageDigest.getInstance("MD5").digest("".getBytes()),
                resultHashValue));
    }

    /**
     * testHash04。<br>
     * 
     * (正常系)<br>
     * 観点：A<br>
     * 
     * 入力値：ハッシュアルゴリズム指定を小文字<br>
     * 期待値：ハッシュ値の取得成功<br>
     * 
     * ハッシュアルゴリズム指定を小文字で行った場合にもハッシュ値の取得が出来ることを確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHash04() throws Exception {
        // 入力値設定
        String paramAlgorithm = "md5";
        String paramStr = "";

        // テスト実行
        byte[] resultHashValue = HashUtil.hash(paramAlgorithm, paramStr);

        // 結果確認
        assertTrue(
            MessageDigest.isEqual(
                MessageDigest.getInstance("MD5").digest("".getBytes()),
                resultHashValue));
    }

    /**
     * testHash05。<br>
     * 
     * (正常系)<br>
     * 観点：C<br>
     * 
     * 入力値：null文字列<br>
     * 期待値：NullPointerExceptionが発生<br>
     * 
     * null文字列のハッシュ値の取得時NullPointerExceptionが発生することを確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHash05() throws Exception {
        // 入力値設定
        String paramAlgorithm = "md5";
        String paramStr = null;

        // テスト実行
        byte[] resultHashValue = HashUtil.hash(paramAlgorithm, paramStr);

        // 結果確認
        assertNull(resultHashValue);
    }

    /**
     * testHash06。<br>
     * 
     * (異常系)<br>
     * 観点：A<br>
     * 
     * 入力値：不正なハッシュアルゴリズム<br>
     * 期待値：NoSuchAlgorithmExceptionが発生<br>
     * 
     * 不正なハッシュアルゴリズム指定時NoSuchAlgorithmExceptionが発生することを確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHash06() throws Exception {
        // 入力値設定
        String paramAlgorithm = "NoSuchAlgorithm";
        String paramStr = "abc";

        try {
            // テスト実行
            HashUtil.hash(paramAlgorithm, paramStr);
            fail();
        } catch (NoSuchAlgorithmException nsae) {
            return;
        }
    }

    /**
     * testHash07。<br>
     * 
     * (異常系)<br>
     * 観点：C<br>
     * 
     * 入力値：ハッシュアルゴリズムに空文字列指定<br>
     * 期待値：NoSuchAlgorithmExceptionが発生<br>
     * 
     * ハッシュアルゴリズムに空文字列指定時NoSuchAlgorithmExceptionが発生することを確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHash07() throws Exception {
        // 入力値設定
        String paramAlgorithm = "";
        String paramStr = "abc";

        try {
            // テスト実行
            HashUtil.hash(paramAlgorithm, paramStr);
            fail();
        } catch (NoSuchAlgorithmException nsae) {
            return;
        }
    }

    /**
     * testHash08。<br>
     * 
     * (正常系)<br>
     * 観点：C<br>
     * 
     * 入力値：ハッシュアルゴリズムにnull指定<br>
     * 期待値：NullPointerExceptionが発生<br>
     * 
     * ハッシュアルゴリズムにnull指定時NullPointerExceptionが発生することを確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHash08() throws Exception {
        // 入力値設定
        String paramAlgorithm = null;
        String paramStr = "abc";

        // テスト実行
        byte[] resultHashValue = HashUtil.hash(paramAlgorithm, paramStr);

        // 結果確認
        assertNull(resultHashValue);
    }

    /**
     * testHashMD501。<br>
     * 
     * (正常系)<br>
     * 観点：A<br>
     * 
     * 入力値：半角文字列<br>
     * 期待値：半角文字列のハッシュ値<br>
     * 
     * 半角文字列のハッシュ値の取得が出来ることを確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHashMD501() throws Exception {
        // 入力値設定
        String paramStr = "abc";

        // テスト実行        
        byte[] resultHashValue = HashUtil.hashMD5(paramStr);

        // 結果確認
        assertTrue(
            MessageDigest.isEqual(
                MessageDigest.getInstance("MD5").digest("abc".getBytes()),
                resultHashValue));

    }

    /**
     * testHashMD502。<br>
     * 
     * (正常系)<br>
     * 観点：A<br>
     * 
     * 入力値：全角文字列<br>
     * 期待値：全角文字列のハッシュ値<br>
     * 
     * 全角文字列のハッシュ値の取得が出来ることを確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHashMD502() throws Exception {
        // 入力値設定
        String paramStr = "あいう";

        // テスト実行
        byte[] resultHashValue = HashUtil.hashMD5(paramStr);

        // 結果確認
        assertTrue(
            MessageDigest.isEqual(
                MessageDigest.getInstance("MD5").digest("あいう".getBytes()),
                resultHashValue));
    }

    /**
     * testHashMD503。<br>
     * 
     * (正常系)<br>
     * 観点：C<br>
     * 
     * 入力値：空文字列<br>
     * 期待値：空文字列のハッシュ値<br>
     * 
     * 空文字列のハッシュ値の取得が出来ることを確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHashMD503() throws Exception {
        // 入力値設定
        String paramStr = "";

        // テスト実行        
        byte[] resultHashValue = HashUtil.hashMD5(paramStr);

        // 結果確認
        assertTrue(
            MessageDigest.isEqual(
                MessageDigest.getInstance("MD5").digest("".getBytes()),
                resultHashValue));
    }

    /**
     * testHashMD504。<br>
     * 
     * (正常系)<br>
     * 観点：C<br>
     * 
     * 入力値：null文字列<br>
     * 期待値：NullPointerExceptionが発生<br>
     * 
     * null文字列のハッシュ値の取得時NullPointerExceptionが発生することを確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHashMD504() throws Exception {
        // 入力値設定
        String paramStr = null;

        // テスト実行
        byte[] resultHashValue = HashUtil.hashMD5(paramStr);

        // 結果確認
        assertNull(resultHashValue);
    }

    /**
     * testHashSHA101。<br>
     * 
     * (正常系)<br>
     * 観点：A<br>
     * 
     * 入力値：半角文字列<br>
     * 期待値：半角文字列のハッシュ値<br>
     * 
     * 半角文字列のハッシュ値の取得が出来ることを確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHashSHA101() throws Exception {
        // 入力値設定
        String paramStr = "abc";

        // テスト実行
        byte[] resultHashValue = HashUtil.hashSHA1(paramStr);

        // 結果確認
        assertTrue(
            MessageDigest.isEqual(
                MessageDigest.getInstance("SHA1").digest("abc".getBytes()),
                resultHashValue));
    }

    /**
     * testHashSHA102。<br>
     * 
     * (正常系)<br>
     * 観点：A<br>
     * 
     * 入力値：全角文字列<br>
     * 期待値：全角文字列のハッシュ値<br>
     * 
     * 全角文字列のハッシュ値の取得が出来ることを確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHashSHA102() throws Exception {
        // 入力値設定
        String paramStr = "あいう";

        // テスト実行
        byte[] resultHashValue = HashUtil.hashSHA1(paramStr);

        // 結果確認
        assertTrue(
            MessageDigest.isEqual(
                MessageDigest.getInstance("SHA1").digest("あいう".getBytes()),
                resultHashValue));
    }

    /**
     * testHashSHA103。<br>
     * 
     * (正常系)<br>
     * 観点：C<br>
     * 
     * 入力値：空文字列<br>
     * 期待値：空文字列のハッシュ値<br>
     * 
     * 全角文字列のハッシュ値の取得が出来ることを確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHashSHA103() throws Exception {
        // 入力値設定
        String paramStr = "";

        // テスト実行
        byte[] resultHashValue = HashUtil.hashSHA1(paramStr);

        // 結果確認
        assertTrue(
            MessageDigest.isEqual(
                MessageDigest.getInstance("SHA1").digest("".getBytes()),
                resultHashValue));
    }

    /**
     * testHashSHA104。<br>
     * 
     * (正常系)<br>
     * 観点：C<br>
     * 
     * 入力値：null文字列<br>
     * 期待値：NullPointerExceptionが発生<br>
     * 
     * null文字列のハッシュ値の取得時NullPointerExceptionが発生することを確認する。<br>
     * 
     * @throws Exception テストコードの本質とかかわりの無い例外
     */
    public void testHashSHA104() throws Exception {
        // 入力値設定
        String paramStr = null;

        // テスト実行
        byte[] resultHashValue = HashUtil.hashSHA1(paramStr);

        // 結果確認
        assertNull(resultHashValue);
    }

}
