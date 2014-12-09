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

import jp.terasoluna.utlib.PropertyTestCase;
import jp.terasoluna.utlib.UTUtil;

/**
 * 
 * FileUtil ブラックボックステスト。<br>
 *
 * (前提条件)<br>
 * 　・プロパティファイル(system.properties)にて、セッション
 * 　　ディレクトリのベース名が以下のように指定されていること<br>
 * 　　（デフォルト状態ならOK）<br>
 * 　　　session.dir.base=/tmp/sessions<br>
 * 　・プロパティファイル(test.properties)に、削除テスト用の
 * 　　ディレクトリのベース名を以下のように追加指定すること<br>
 * 　　　fileutiltest.dir.base=/tmp/test<br>
 *
 */
@SuppressWarnings("unused")
public class FileUtilTest extends PropertyTestCase {

    /**
     * セッションディレクトリ名をプロパティから取得するキー値。
     */
    public static final String SESSIONS_DIR = "session.dir.base";

    /*
     * セッションディレクトリの絶対パス。
     */
    String TMP_SESSIONS_DIR = "/tmp/sessions/";

    /*
     * FileUtilテスト用ディレクトリの絶対パス。
     */
    String TMP_TEST_DIR = "/tmp/test/";

    /**
     * Constructor for FileUtilTest.
     * @param arg0
     */
    public FileUtilTest(String arg0) {
        super(arg0);
    }

    @Override
    protected void setUpData() throws Exception {
        addProperty("session.dir.base", "/tmp/sessions");
    }

    @Override
    protected void cleanUpData() throws Exception {
        deleteProperty("session.dir.base");
    }

    /**
     * getSessionDirectoryName(1)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：16桁の英数字<br>
     * 期待値：入力のハッシュ値を16進変換したもの<br>
     * @throws Exception 例外  */
    public void testGetSessionDirectoryName01() throws Exception {
        // 入力値の設定
        String input = "01234567abcdefgh";

        // テスト対象の実行
        String result = FileUtil.getSessionDirectoryName(input);

        // 結果の確認
        String hope = StringUtil.toHexString(HashUtil.hashSHA1(input), "");
        assertEquals(hope, result);
    }

    /**
     * getSessionDirectoryName(2)
     * 
     * (異常系)<br>
     * 観点：<br>
     * 
     * 入力値：null<br>
     * 期待値：NullPointerException<br>
     * @throws Exception 例外
     */
    public void testGetSessionDirectoryName02() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト対象の実行
        try {
            String result = FileUtil.getSessionDirectoryName(input);
            fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * getSessionDirectoryName(3)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：""(空文字)<br>
     * 期待値：入力のハッシュ値を16進変換したもの<br>
     * @throws Exception 例外 */
    public void testGetSessionDirectoryName03() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト対象の実行
        String result = FileUtil.getSessionDirectoryName(input);

        // 結果の確認
        String hope = StringUtil.toHexString(HashUtil.hashSHA1(input), "");
        assertEquals(hope, result);
    }

    /**
     * getSessionDirectory(1)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：16桁の英数字<br>
     * 期待値：入力に対応するディレクトリ名のディレクトリ<br>
     * @throws Exception 例外 */
    public void testGetSessionDirectory01() throws Exception {
        // 入力値の設定
        String input = "01234567abcdefgh";

        // テスト対象の実行
        File result = FileUtil.getSessionDirectory(input);

        // 結果の確認
        String dirName = StringUtil.toHexString(HashUtil.hashSHA1(input), "");
        File hope = new File(TMP_SESSIONS_DIR + dirName);
        assertEquals(hope, result);
    }

    /**
     * getSessionDirectory(2)
     * 
     * (異常系)<br>
     * 観点：<br>
     * 
     * 入力値：null<br>
     * 期待値：NullPointerException<br>
     * @throws Exception 例外 */
    public void testGetSessionDirectory02() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト対象の実行
        try {
            File result = FileUtil.getSessionDirectory(input);
            fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * getSessionDirectory(3)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：""(空文字)<br>
     * 期待値：入力に対応するディレクトリ名のディレクトリ<br>
     * @throws Exception 例外 */
    public void testGetSessionDirectory03() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト対象の実行
        File result = FileUtil.getSessionDirectory(input);

        // 結果の確認
        String dirName = StringUtil.toHexString(HashUtil.hashSHA1(input), "");
        File hope = new File(TMP_SESSIONS_DIR + dirName);
        assertEquals(hope, result);
    }

    /**
     * getSessionDirectory(4)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * プロパティのsession.dir.baseが設定されていない時、
     * デフォルトのディレクトリが返って来る事を確認する。
     * @throws Exception 例外 */
    public void testGetSessionDirectory04() throws Exception {
        // 入力値の設定
        String input = "01234567abcdefgh";

        // プロパティ削除
        deleteProperty("session.dir.base");

        // テスト対象の実行
        File result = FileUtil.getSessionDirectory(input);

        // 結果の確認
        String dirName = StringUtil.toHexString(HashUtil.hashSHA1(input), "");
        File hope = new File(File.separator + "temp" + File.separator + dirName);
        assertEquals(hope, result);
    }

    /**
     * getSessionDirectory(5)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * プロパティのsession.dir.baseの値が空文字の時、
     * デフォルトのディレクトリが返って来る事を確認する。
     * @throws Exception 例外 */
    @SuppressWarnings("unchecked")
    public void testGetSessionDirectory05() throws Exception {
        // 入力値の設定
        String input = "01234567abcdefgh";

        // プロパティの値を空文字に設定
        deleteProperty("session.dir.base");
        addProperty("session.dir.base", "");

        // テスト対象の実行
        File result = FileUtil.getSessionDirectory(input);

        // 結果の確認
        String dirName = StringUtil.toHexString(HashUtil.hashSHA1(input), "");
        File hope = new File(File.separator + "temp" + File.separator + dirName);
        assertEquals(hope, result);
    }
    
    /**
     * makeSessionDirectory(1)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：16桁の英数字<br>
     * 期待値：テスト対象の実行が成功し、入力に対応するディレクトリ名の
     * 　　　　ディレクトリが作成されていること<br>
     * @throws Exception 例外 */
    public void testMakeSessionDirectory01() throws Exception {
        // 入力値の設定
        String input = "01234567abcdefgh";

        // 事前準備
        String dirName = StringUtil.toHexString(HashUtil.hashSHA1(input), "");
        File dir = new File(TMP_SESSIONS_DIR + dirName);
        // 作成するディレクトリが存在する場合、削除
        if (dir.exists()) {
            dir.delete();
        }

        // テスト対象の実行
        boolean result = FileUtil.makeSessionDirectory(input);

        // 結果の確認
        assertTrue(result);
        // ディレクトリが作成されていることの確認
        if (!dir.exists()) {
            fail();
        }
    }

    /**
     * makeSessionDirectory(2)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：16桁の英数字（既に対応するディレクトリが存在する）<br>
     * 期待値：テスト対象の実行が失敗し、入力に対応するディレクトリ名の
     * 　　　　ディレクトリが作成されていること<br>
     * @throws Exception 例外 */
    public void testMakeSessionDirectory02() throws Exception {
        // 入力値の設定
        String input = "01234567abcdefgh";

        // 事前準備
        String dirName = StringUtil.toHexString(HashUtil.hashSHA1(input), "");
        File dir = new File(TMP_SESSIONS_DIR + dirName);
        // 作成されるディレクトリが存在する場合、削除
        if (dir.exists()) {
            dir.delete();
        }

        // テスト対象の実行（2回繰り返す）
        FileUtil.makeSessionDirectory(input);
        boolean result = FileUtil.makeSessionDirectory(input);

        // 結果の確認
        assertFalse(result);
        // ディレクトリが作成されていることの確認
        if (!dir.exists()) {
            fail();
        }
    }

    /**
     * makeSessionDirectory(3)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：null<br>
     * 期待値：false<br>
     * @throws Exception 例外 */
    public void testMakeSessionDirectory03() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト対象の実行
        boolean result = FileUtil.makeSessionDirectory(input);

        // 結果の確認
        assertFalse(result);
    }

    /**
     * makeSessionDirectory(4)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：""(空文字)<br>
     * 期待値：false<br>
     * @throws Exception 例外 */
    public void testMakeSessionDirectory04() throws Exception {
        // 入力値の設定
        String input = "";

        // 事前準備

        // テスト対象の実行
        boolean result = FileUtil.makeSessionDirectory(input);

        // 結果の確認
        assertFalse(result);
    }

    /**
     * rmdirs(1)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：任意のディレクトリ（サブディレクトリ、ファイルなし）<br>
     * 期待値：テスト対象の実行が成功し、ディレクトリが削除されること<br>
     * @throws Exception 例外 */
    public void testRmdirs01() throws Exception {
        // 入力値の設定
        File dir = new File(TMP_TEST_DIR + "rmdirs1");

        // 事前準備
        // 削除するディレクトリが存在しない場合、作成
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // テスト対象の実行
        boolean result = FileUtil.rmdirs(dir);

        // 結果の確認
        assertTrue(result);
        // ディレクトリが削除されていることの確認
        if (dir.exists()) {
            fail();
        }
    }

    /**
     * rmdirs(2)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：存在しないディレクトリ<br>
     * 期待値：テスト対象の実行が失敗し、ディレクトリが存在しないこと<br>
     * @throws Exception 例外 */
    public void testRmdirs02() throws Exception {
        // 入力値の設定
        File dir = new File(TMP_TEST_DIR + "rmdirs2");

        // 事前準備
        // ディレクトリが存在する場合、削除
        if (dir.exists()) {
            dir.delete();
        }

        // テスト対象の実行
        boolean result = FileUtil.rmdirs(dir);

        // 結果の確認
        assertFalse(result);
        // ディレクトリが存在しないことの確認
        if (dir.exists()) {
            fail();
        }
    }

    /**
     * rmdirs(3)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：任意のディレクトリ（サブディレクトリ、ファイルあり）<br>
     * 期待値：テスト対象の実行が成功し、ディレクトリが削除されること<br>
     * @throws Exception 例外 */
    public void testRmdirs03() throws Exception {
        // 入力値の設定
        File dir = new File(TMP_TEST_DIR + "rmdirs3");

        // 事前準備
        // サブディレクトリ、ファイルの設定
        File subdir = new File(TMP_TEST_DIR + "rmdirs3/tmp");
        File file = new File(TMP_TEST_DIR + "rmdirs3/tmp.txt");
        // 削除するディレクトリ、ファイルが存在しない場合、作成
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!subdir.exists()) {
            subdir.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }

        // テスト対象の実行
        boolean result = FileUtil.rmdirs(dir);

        // 結果の確認
        assertTrue(result);
        // 親ディレクトリが存在しないことの確認
        if (dir.exists()) {
            fail();
        }
    }

    /**
     * rmdirs(4)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：null<br>
     * 期待値：false<br>
     * @throws Exception 例外 */
    public void testRmdirs04() throws Exception {
        // 入力値の設定
        File dir = null;

        // テスト対象の実行
        boolean result = FileUtil.rmdirs(dir);

        // 結果の確認
        assertFalse(result);
    }

    /**
     * rmdirs(5)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：""(空文字)のパスのFileオブジェクト<br>
     * 期待値：false<br>
     * @throws Exception 例外 */
    public void testRmdirs05() throws Exception {
        // 入力値の設定
        File dir = new File("");

        // テスト対象の実行
        boolean result = FileUtil.rmdirs(dir);

        // 結果の確認
        assertFalse(result);
    }

    /**
     * rmdirs(6)
     * 
     * (正常系)<br>
     * 観点：<br>
     * 
     * 入力値：読み取り専用のディレクトリ（サブディレクトリ、ファイルなし）<br>
     * 期待値：テスト対象の実行が成功し、ディレクトリが削除されること<br>
     * @throws Exception 例外 */
    public void testRmdirs06() throws Exception {
        // 入力値の設定
        File dir = new File(TMP_TEST_DIR + "rmdirs6");

        // 事前準備
        // ディレクトリは作成しておく
        // 読み取り専用属性もそこで設定する
        if(!dir.exists()){
            dir.mkdir();
            dir.setReadOnly();
        }

        // テスト対象の実行
        boolean result = FileUtil.rmdirs(dir);

        // 結果の確認
        assertTrue(result);
        // ディレクトリが削除されていることの確認
        if (dir.exists()) {
            fail();
        }
    }

    /**
     * removeSessionDirectory01()
     * 
     * (正常系)<br>
     * 観点：A<br>
     * 
     * 入力値：sessionID="01234567abcdefgh"<br>
     * 期待値：true<br>
     * 
     * セッションIDに対応するディレクトリが存在し、ディレクトリの削除に成功した時、
     * trueが返却されることを確認する。
     * 
     * @throws Exception 例外 */
    public void testRemoveSessionDirectory01() throws Exception {
        // 初期設定
        String input = "01234567abcdefgh";
        UTUtil.invokePrivate(
            FileUtil.class,
            "makeSessionDirectory",
            String.class,
            input);
        // テスト実行
        // 結果確認
        assertTrue(FileUtil.removeSessionDirectory(input));
    }

    /**
     * removeSessionDirectory02()
     * 
     * (正常系)<br>
     * 観点：A<br>
     * 
     * 入力値：sessionID="01234567abcdefgh"<br>
     * 期待値：false<br>
     * 
     * セッションIDに対応するディレクトリが存在しないとき、
     * ディレクトリの削除に失敗しfalseが返却されることを確認する。
     * 
     * @throws Exception 例外 */
    public void testRemoveSessionDirectory02() throws Exception {
        // 初期設定
        String input = "01234567abcdefgh";
        File dir = (File) UTUtil.invokePrivate(FileUtil.class,
                "getSessionDirectory", String.class, input);
        dir.delete();

        // テスト実行
        // 結果確認
        assertFalse(FileUtil.removeSessionDirectory(input));
    }

    /**
     * removeSessionDirectory03()
     * 
     * (異常系)<br>
     * 観点：C,G<br>
     * 
     * 入力値：sessionID=null<br>
     * 期待値：NullPOinterException<br>
     * 
     * セッションIDがnullの時、NullPointerExceptionが発生することを確認する。
     * 
     * @throws Exception 例外 */
    public void testRemoveSessionDirectory03() throws Exception {
        try {
            // テスト対象の実行
            FileUtil.removeSessionDirectory(null);
            fail();
        } catch (NullPointerException e) {
            return;
        }
        // 結果確認
        fail();
    }
}
