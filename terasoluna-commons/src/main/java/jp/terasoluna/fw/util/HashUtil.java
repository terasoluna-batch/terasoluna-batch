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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ハッシュ値を計算するユーティリティクラス。
 * 
 * <p>
 *  java.security.MessageDigestを用いて、文字列のハッシュ値を
 *  取得する。<br>
 *  MD5、SHA1、その他アルゴリズムを使用して取得する。
 *  <strong>使用例</strong><br>
 *  <code><pre>
 *   ・・・
 *    // DBに登録するユーザパスワードのハッシュ値を計算する。
 *    byte[] hash = HashUtil.hashMD5(userPassword);
 *   ・・・
 *  </pre></code>
 * </p>
 *
 */
public class HashUtil {

    /**
     * ログクラス。
     */
    private static Log log = LogFactory.getLog(HashUtil.class);

    /**
     * 指定されたアルゴリズムで文字列をハッシュ値を取得する。
     *
     * @param algorithm ハッシュアルゴリズム
     * @param str ハッシュ値の取得対象の文字列
     * @return ハッシュ値
     * @throws NoSuchAlgorithmException ハッシュアルゴリズムが不正なとき
     * 
     */
    public static byte[] hash(String algorithm, String str)
            throws NoSuchAlgorithmException {
        if (algorithm == null || str == null) {
            return null;
        }
        MessageDigest md = MessageDigest.getInstance(algorithm.toUpperCase());
        return md.digest(str.getBytes());
    }

    /**
     * MD5アルゴリズムで文字列のハッシュ値を取得する。
     * 
     * @param str ハッシュ値の取得対象の文字列
     * @return ハッシュ値
     */
    public static byte[] hashMD5(String str) {
        try {
            return hash("MD5", str);
        } catch (NoSuchAlgorithmException e) {
            log.error("The algorithm is not available"
                    + " in the caller's environment.", e);
            return null; // can't happen
        }
    }

    /**
     * SHA1アルゴリズムで文字列のハッシュ値を取得する。
     * 
     * @param str ハッシュ値の取得対象の文字列
     * @return ハッシュ値
     */
    public static byte[] hashSHA1(String str) {
        try {
            return hash("SHA1", str);
        } catch (NoSuchAlgorithmException e) {
            log.error("The algorithm is not available"
                    + " in the caller's environment.", e);
            return null; // can't happen
        }
    }
}
