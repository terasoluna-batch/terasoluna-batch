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

/**
 * ファイル操作関連のユーティリティクラス。
 *
 * <p>
 *  セッションIDに対応したディレクトリの作成、取得、削除を行う。
 *  各セッションIDに対応したディレクトリは、 ApplicationRecoures 
 *  ファイルの &quot;session.dir.base&quot; で示されたディレクトリの中に
 *  作成される。<br>
 *  セッションに対応したディレクトリは、サーバ側で作成したPDFの帳票など
 *  セッションに紐付いた一時的なデータを格納する際に利用できる。<br>
 *  セッションに紐付いたディレクトリは、 HttpSession 
 *  オブジェクトが無効化された際に、このユーティリティクラスのメソッドを
 *  用いて削除する。<br>
 *  セッションの生成・破棄を監視する機能については、
 *  HttpSessionListenerを参照。
 * </p>
 *
 */
public class FileUtil {

    /**
     * セッションに対応付けされたディレクトリを作成する際に、各ディレクトリを
     * 格納する親ディレクトリ名を  ApplicationResource 
     * ファイルから取得するためのキー。
     */
    private static final String SESSION_DIR_BASE_KEY = "session.dir.base";

    /**
     * 指定されたセッションIDに対応するディレクトリ名を取得する。
     *
     * @param sessionId セッションID
     * @return セッションIDのハッシュ値として生成されたディレクトリ名
     */
    public static String getSessionDirectoryName(String sessionId) {
        byte[] hash = HashUtil.hashSHA1(sessionId);
        return StringUtil.toHexString(hash, "");
    }

    /**
     * 指定されたセッションIDに対応するディレクトリを取得する。
     * 
     * <p>
     * プロパティキーの設定を行なわなかった場合、
     * もしくは空文字の場合は tempディレクトリを用いる。
     * </p>
     * @param sessionId セッションID
     * @return セッションIDに対応するディレクトリとなるファイルオブジェクト
     */
    public static File getSessionDirectory(String sessionId) {
        String dirBase = PropertyUtil.getProperty(SESSION_DIR_BASE_KEY);

        if (dirBase == null || "".equals(dirBase)) {
            dirBase = File.separator + "temp";
        }

        String dirName = getSessionDirectoryName(sessionId);
        return new File(dirBase + File.separator + dirName);
    }

    /**
     * 指定されたセッションIDに対応するディレクトリを作成する。
     *
     * <p>作成が成功した場合には、true を返す。</p>
     *
     * @param sessionId セッションID
     * @return ディレクトリの作成に成功すれば true
     */
    public static boolean makeSessionDirectory(String sessionId) {
        if (sessionId == null || "".equals(sessionId)) {
            return false;
        }
        return getSessionDirectory(sessionId).mkdirs();
    }

    /**
     * 指定されたセッションIDに対応するディレクトリを削除する。
     *
     * <p>削除が成功した場合には、true を返す。</p>
     *
     * @param sessionId セッションID
     * @return ディレクトリの削除に成功すれば true 
     */
    public static boolean removeSessionDirectory(String sessionId) {
        return rmdirs(getSessionDirectory(sessionId));
    }

    /**
     * 指定されたディレクトリを削除する。
     *
     * <p>ディレクトリ内にファイル、ディレクトリが
     * ある場合でも、再帰的に削除される。</p>
     *
     * @param dir 削除するディレクトリ
     * @return ディレクトリの削除に成功すれば true
     */
    public static boolean rmdirs(File dir) {
        if (dir == null) {
            return false;
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    rmdirs(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return dir.delete();
    }
}
