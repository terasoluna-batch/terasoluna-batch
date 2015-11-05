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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import org.junit.Test;

/**
 * StringUtil ブラックボックステスト。<br>
 * (前提条件)<br>
 * ・プロパティファイルに、以下のキーと値が正常に記述されていること<br>
 * ログ出力ディレクトリ(log4j.file.dir)<br>
 * ログファイル名(log4j.file.name)<br>
 */
public class StringUtilTest {

    /**
     * ファイルログ出力先のディレクトリ名をプロパティから取得するキー値。
     */
    public static final String FILE_DIR = "log4j.file.dir";

    /**
     * ファイルログ出力先のファイル名をプロパティから取得するキー値。
     */
    private static final String FILE_NAME = "log4j.file.name";

    /**
     * 実行環境のOSで用いられる改行コードを取得するキー値
     */
    private static final String LINE_SEP = System.getProperty("line.separator");

    /**
     * ファイルログ出力先の絶対パス。
     */
    String LOG_FILE_NAME = PropertyUtil.getProperty(FILE_DIR) + "/"
            + PropertyUtil.getProperty(FILE_NAME);

    /**
     * testIsWhitespace01() (正常系)<br>
     * 観点：A<br>
     * 入力値：半角スペース<br>
     * 期待値：true<br>
     * 半角スペースキャラクタを入力した時、trueが<br>
     * 返却されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testIsWhitespace01() throws Exception {
        // 入力値の設定
        char input = ' ';

        // テスト実行と結果確認
        assertTrue(StringUtil.isWhitespace(input));
    }

    /**
     * testIsWhitespace02() (正常系)<br>
     * 観点：A<br>
     * 入力値：半角文字列<br>
     * 期待値：false<br>
     * 半角英字キャラクタを入力した時、falseが<br>
     * 返却されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testIsWhitespace02() throws Exception {
        // 入力値の設定
        char input = 'a';

        // テスト実行と結果確認
        assertFalse(StringUtil.isWhitespace(input));
    }

    /**
     * testIsWhitespace03() (正常系)<br>
     * 観点：A<br>
     * 入力値：0<br>
     * 期待値：false<br>
     * 0キャラクタを設定した時、falseが返却されること<br>
     * @throws Exception 例外
     */
    @Test
    public void testIsWhitespace03() throws Exception {
        // 入力値の設定
        // char input = '\u0000';
        char input = 0;

        // テスト実行と結果確認
        assertFalse(StringUtil.isWhitespace(input));
    }

    /**
     * testRtrim01() (正常系)<br>
     * 観点：A<br>
     * 入力値：両側に半角スペース1つずつある文字列<br>
     * 期待値：右側の半角スペース1つのみ除かれる<br>
     * 左右両側にスペースが存在する文字列を入力した時、<br>
     * 右スペースのみ除去されていることを確認。
     * @throws Exception 例外
     */
    @Test
    public void testRtrim01() throws Exception {
        // 入力値の設定
        String input = " TERASOLUNA ";

        // テスト実行
        String result = StringUtil.rtrim(input);

        // 結果確認
        assertEquals(" TERASOLUNA", result);
    }

    /**
     * testRtrim02() (正常系)<br>
     * 観点：B<br>
     * 入力値：両側に半角スペースが複数ある文字列<br>
     * 期待値：右側の半角スペースがすべて除かれる<br>
     * 文字列右端に半角スペースが複数存在するとき、 連続した右端スペースが除去されていることを確認。
     * @throws Exception 例外
     */
    @Test
    public void testRtrim02() throws Exception {
        // 入力値の設定
        String input = "   TERASOLUNA   ";

        // テスト実行
        String result = StringUtil.rtrim(input);

        // 結果確認
        assertEquals("   TERASOLUNA", result);
    }

    /**
     * testRtrim03() (正常系)<br>
     * 観点：A<br>
     * 入力値：右側に半角スペースがない文字列<br>
     * 期待値：入力と同じ<br>
     * 右端にスペースがないとき、入力値と同じ文字列が<br>
     * 返却されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testRtrim03() throws Exception {
        // 入力値の設定
        String input = " TERASOLUNA";

        // テスト実行
        String result = StringUtil.rtrim(input);

        // 結果確認
        assertEquals(" TERASOLUNA", result);
    }

    /**
     * testRtrim04() (正常系)<br>
     * 観点：C<br>
     * 入力値：null<br>
     * 期待値：null<br>
     * nullが入力値に設定された時、そのままnullで<br>
     * 返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testRtrim04() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト実行
        String result = StringUtil.rtrim(input);

        // 結果確認
        assertNull(result);
    }

    /**
     * testRtrim05() (正常系)<br>
     * 観点：A<br>
     * 入力値：""(空文字)<br>
     * 期待値：""(空文字)<br>
     * 空文字が入力値に設定された時、そのまま空文字が返却されること
     * @throws Exception 例外
     */
    @Test
    public void testRtrim05() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト実行
        String result = StringUtil.rtrim(input);

        // 結果確認
        assertEquals("", result);
    }

    /**
     * testLtrim01() (正常系)<br>
     * 観点：A<br>
     * 入力値：両側に半角スペースが1つずつある文字列<br>
     * 期待値：左側の半角スペースのみ除かれた文字列<br>
     * 左右両側にスペースが存在する文字列を入力した時、<br>
     * 左スペースのみ除去されていることを確認。
     * @throws Exception 例外
     */
    @Test
    public void testLtrim01() throws Exception {
        // 入力値の設定
        String input = " TERASOLUNA ";

        // テスト実行
        String result = StringUtil.ltrim(input);

        // 結果確認
        assertEquals("TERASOLUNA ", result);
    }

    /**
     * testLtrim02() (正常系)<br>
     * 観点：B<br>
     * 入力値：両側に半角スペースが複数ある文字列<br>
     * 期待値：左側の半角スペースがすべて除かれた文字列<br>
     * 文字列左端に半角スペースが複数存在するとき、 連続した左端スペースが除去されていることを確認。
     * @throws Exception 例外
     */
    @Test
    public void testLtrim02() throws Exception {
        // 入力値の設定
        String input = "   TERASOLUNA   ";

        // テスト実行
        String result = StringUtil.ltrim(input);

        // 結果確認
        assertEquals("TERASOLUNA   ", result);
    }

    /**
     * testLtrim03() (正常系)<br>
     * 観点：A<br>
     * 入力値：左側に半角スペースがない文字列<br>
     * 期待値：入力と同じ<br>
     * 左端にスペースがないとき、入力値と同じ文字列が<br>
     * 返却されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testLtrim03() throws Exception {
        // 入力値の設定
        String input = "TERASOLUNA ";

        // テスト実行
        String result = StringUtil.ltrim(input);

        // 結果確認
        assertEquals("TERASOLUNA ", result);
    }

    /**
     * testLtrim04() (正常系)<br>
     * 観点：C<br>
     * 入力値：null<br>
     * 期待値：null<br>
     * nullが入力値に設定された時、そのままnullで<br>
     * 返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testLtrim04() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト実行
        String result = StringUtil.ltrim(input);

        // 結果確認
        assertNull(result);
    }

    /**
     * testLtrim05() (正常系)<br>
     * 観点：A<br>
     * 入力値：""(空文字)<br>
     * 期待値：""(空文字)<br>
     * 空文字が入力値に設定された時、そのまま空文字が返却されること
     * @throws Exception 例外
     */
    @Test
    public void testLtrim05() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト実行
        String result = StringUtil.ltrim(input);

        // 結果確認
        assertEquals("", result);
    }

    /**
     * testTrim01() (正常系)<br>
     * 観点：A<br>
     * 入力値：両側に半角スペースが1つずつある文字列<br>
     * 期待値：両側の半角スペースが除かれた文字列<br>
     * 両側に半角スペースが１つある文字列が入力された時、<br>
     * スペースが除去された文字列が返却されること。 StringUtils.trim()の呼び出し確認のために、１ケースのみとする。
     * @throws Exception 例外
     */
    @Test
    public void testTrim01() throws Exception {
        // 入力値の設定
        String input = " TERASOLUNA ";

        // テスト実行
        String result = StringUtil.trim(input);

        // 結果確認
        assertEquals("TERASOLUNA", result);
    }

    /**
     * testIsZenHankakuSpace01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) c:"　"(全角スペース）<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 全角スペースキャラクタを入力した時、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsZenHankakuSpace01() throws Exception {
        // 入力値の設定
        char input = '　';

        // テスト実行と結果確認
        assertTrue(StringUtil.isZenHankakuSpace(input));
    }

    /**
     * testIsZenHankakuSpace02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) c:"a"<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 半角英字キャラクタを入力した時、falseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsZenHankakuSpace02() throws Exception {
        // 入力値の設定
        char input = 'a';

        // テスト実行と結果確認
        assertFalse(StringUtil.isZenHankakuSpace(input));
    }

    /**
     * testIsZenHankakuSpace03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) c:0<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 0キャラクタを設定した時、falseが返却されること <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsZenHankakuSpace03() throws Exception {
        // 入力値の設定
        char input = 0;

        // テスト実行と結果確認
        assertFalse(StringUtil.isZenHankakuSpace(input));
    }

    /**
     * testRtrimZ01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) str:" 　 TERASOLUNA 　 "<br>
     * <br>
     * 期待値：(戻り値) String:" 　 TERASOLUNA"<br>
     * <br>
     * 左右両側にスペースが存在する文字列を入力した時、右スペースのみ除去されていることを確認。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRtrimZ01() throws Exception {
        // 入力値の設定
        String input = " 　 TERASOLUNA 　 ";

        // テスト実行
        String result = StringUtil.rtrimZ(input);

        // 結果確認
        assertEquals(" 　 TERASOLUNA", result);
    }

    /**
     * testRtrimZ02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) str:" 　  　TERASOLUNA 　 　 "<br>
     * <br>
     * 期待値：(戻り値) String:" 　  　TERASOLUNA"<br>
     * <br>
     * 文字列右端に全半角スペースが複数存在するとき、連続した右端スペースが除去されていることを確認。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRtrimZ02() throws Exception {
        // 入力値の設定
        String input = " 　  　TERASOLUNA 　 　 ";

        // テスト実行
        String result = StringUtil.rtrimZ(input);

        // 結果確認
        assertEquals(" 　  　TERASOLUNA", result);
    }

    /**
     * testRtrimZ03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) str:"　 TERASOLUNA"<br>
     * <br>
     * 期待値：(戻り値) String:"　 TERASOLUNA"<br>
     * <br>
     * 右端にスペースがないとき、入力値と同じ文字列が返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRtrimZ03() throws Exception {
        // 入力値の設定
        String input = "　 TERASOLUNA";

        // テスト実行
        String result = StringUtil.rtrimZ(input);

        // 結果確認
        assertEquals("　 TERASOLUNA", result);
    }

    /**
     * testRtrimZ04() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) str:null<br>
     * <br>
     * 期待値：(戻り値) String:null<br>
     * <br>
     * nullが入力値に設定された時、そのままnullで返却されること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRtrimZ04() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト実行
        String result = StringUtil.rtrimZ(input);

        // 結果確認
        assertNull(result);
    }

    /**
     * testRtrimZ05() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) str:""<br>
     * <br>
     * 期待値：(戻り値) String:""<br>
     * <br>
     * 空文字が入力値に設定された時、そのまま空文字が返却されること <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRtrimZ05() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト実行
        String result = StringUtil.rtrimZ(input);

        // 結果確認
        assertEquals("", result);
    }

    /**
     * testLtrimZ01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) str:"　  TERASOLUNA 　 "<br>
     * <br>
     * 期待値：(戻り値) String:"TERASOLUNA 　 "<br>
     * <br>
     * 左右両側にスペースが存在する文字列を入力した時、左スペースのみ除去されていることを確認。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testLtrimZ01() throws Exception {
        // 入力値の設定
        String input = "　  TERASOLUNA 　 ";

        // テスト実行
        String result = StringUtil.ltrimZ(input);

        // 結果確認
        assertEquals("TERASOLUNA 　 ", result);
    }

    /**
     * testLtrimZ02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) str:" 　  　TERASOLUNA 　 　 "<br>
     * <br>
     * 期待値：(戻り値) String:"TERASOLUNA 　 　 "<br>
     * <br>
     * 文字列左端に全半角スペースが複数存在するとき、連続した左端スペースが除去されていることを確認。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testLtrimZ02() throws Exception {
        // 入力値の設定
        String input = " 　  　TERASOLUNA 　 　 ";

        // テスト実行
        String result = StringUtil.ltrimZ(input);

        // 結果確認
        assertEquals("TERASOLUNA 　 　 ", result);
    }

    /**
     * testLtrimZ03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) str:"TERASOLUNA 　"<br>
     * <br>
     * 期待値：(戻り値) String:"TERASOLUNA 　"<br>
     * <br>
     * 左端にスペースがないとき、入力値と同じ文字列が返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testLtrimZ03() throws Exception {
        // 入力値の設定
        String input = "TERASOLUNA 　";

        // テスト実行
        String result = StringUtil.ltrimZ(input);

        // 結果確認
        assertEquals("TERASOLUNA 　", result);
    }

    /**
     * testLtrimZ04() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) str:null<br>
     * <br>
     * 期待値：(戻り値) String:null<br>
     * <br>
     * nullが入力値に設定された時、そのままnullで返却されること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testLtrimZ04() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト実行
        String result = StringUtil.ltrimZ(input);

        // 結果確認
        assertNull(result);
    }

    /**
     * testLtrimZ05() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) str:""<br>
     * <br>
     * 期待値：(戻り値) String:""<br>
     * <br>
     * 空文字が入力値に設定された時、そのまま空文字が返却されること <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testLtrimZ05() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト実行
        String result = StringUtil.ltrimZ(input);

        // 結果確認
        assertEquals("", result);
    }

    /**
     * testTrim01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) str:" 　 TERASOLUNA 　 "<br>
     * <br>
     * 期待値：(戻り値) String:"TERASOLUNA"<br>
     * <br>
     * 両側に全角半角スペースが１以上ある文字列が入力された時、スペースが除去された文字列が返却されること。<br>
     * StringUtil.rtrimZ()とStringUtil.ltrimZ()の呼び出し確認のために、１ケースのみとする。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrimZ01() throws Exception {
        // 入力値の設定
        String input = " 　 TERASOLUNA 　 ";

        // テスト実行
        String result = StringUtil.trimZ(input);

        // 結果確認
        assertEquals("TERASOLUNA", result);
    }

    /**
     * testToShortClassName01() (正常系)<br>
     * 観点：A<br>
     * 入力値：パッケージ修飾つきのクラス名<br>
     * 期待値：パッケージ修飾のないクラス名<br>
     * パッケージ名＋ピリオド＋クラス名が設定された時、<br>
     * クラス名が返却されること ClassUtils.getShortClassName()の呼び出し確認のために、１ケースのみとする。
     * @throws Exception 例外
     */
    @Test
    public void testToShortClassName01() throws Exception {
        // 入力値の設定
        String input = "jp.terasoluna.util.StringUtil";

        // テスト実行
        String result = StringUtil.toShortClassName(input);

        // 結果確認
        assertEquals("StringUtil", result);
    }

    /**
     * testGetExtention01() (正常系)<br>
     * 観点：A<br>
     * 入力値：拡張子つきのファイル名<br>
     * 期待値：拡張子<br>
     * ファイル名＋ピリオド＋拡張子が設定された時、<br>
     * ピリオド＋拡張子が返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testGetExtention01() throws Exception {
        // 入力値の設定
        String input = "sample.txt";

        // テスト実行
        String result = StringUtil.getExtension(input);

        // 結果確認
        assertEquals(".txt", result);
    }

    /**
     * testGetExtention02() (正常系)<br>
     * 観点：A<br>
     * 入力値：拡張子のないファイル名<br>
     * 期待値：空文字列<br>
     * ピリオド＋拡張子が存在しない場合、<br>
     * 入力値はファイル名のみと解釈され、空文字が返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testGetExtention02() throws Exception {
        // 入力値の設定
        String input = "sample";

        // テスト実行
        String result = StringUtil.getExtension(input);

        // 結果確認
        assertEquals("", result);
    }

    /**
     * testGetExtention03() (正常系)<br>
     * 観点：A<br>
     * 入力値：拡張子つきのファイル名(拡張子以外に"."がある)<br>
     * 期待値：拡張子<br>
     * 左端のピリオド＋拡張子が返却されること
     * @throws Exception 例外
     */
    @Test
    public void testGetExtention03() throws Exception {
        // 入力値の設定
        String input = "sample.txt.bak";

        // テスト実行
        String result = StringUtil.getExtension(input);

        // 結果確認
        assertEquals(".bak", result);
    }

    /**
     * testGetExtention04() (正常系)<br>
     * 観点：C<br>
     * 入力値：null<br>
     * 期待値：null<br>
     * 引数をnullで設定した時、nullが返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testGetExtention04() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト実行
        String result = StringUtil.getExtension(input);

        // 結果確認
        assertNull(result);
    }

    /**
     * testGetExtention05() (正常系)<br>
     * 観点：A<br>
     * 入力値：空文字列<br>
     * 期待値：空文字列<br>
     * 空文字列が設定された時、そのまま空文字が返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testGetExtention05() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト実行
        String result = StringUtil.getExtension(input);

        // 結果確認
        assertEquals("", result);
    }

    /**
     * testGetExtention06() (正常系)<br>
     * 観点：A<br>
     * 入力値：末尾にピリオドがついたファイル名<br>
     * 期待値：ピリオドのみの文字列<br>
     * 末尾がピリオドのとき、ピリオドのみ返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testGetExtention06() throws Exception {
        // 入力値の設定
        String input = "sample.txt.";

        // テスト実行
        String result = StringUtil.getExtension(input);

        // 結果確認
        assertEquals(".", result);
    }

    /**
     * testToHexString01() (正常系)<br>
     * 観点：A<br>
     * 入力値：要素数1のバイト配列<br>
     * 期待値：入力要素の16進変換<br>
     * 入力値の16進数2桁に変換されていること。
     * @throws Exception 例外
     */
    @Test
    public void testToHexString01() throws Exception {
        // 入力値の設定
        byte[] byteArray = { 0 };

        // テスト実行
        String result = StringUtil.toHexString(byteArray, "-");

        // 結果確認
        assertEquals("00", result);
    }

    /**
     * testToHexString02() (正常系)<br>
     * 観点：C<br>
     * 入力値：要素数0のバイト配列<br>
     * 期待値：空文字列<br>
     * 空文字列が設定された時、空文字がそのまま返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testToHexString02() throws Exception {
        // 入力値の設定
        byte[] byteArray = {};

        // テスト実行
        String result = StringUtil.toHexString(byteArray, "-");

        // 結果確認
        assertEquals("", result);
    }

    /**
     * testToHexString03() (正常系)<br>
     * 観点：B<br>
     * 入力値：要素数3のバイト配列<br>
     * 期待値：入力要素を16進変換し、デリミタで結合した文字列<br>
     * ３つの要素を16進数に変換し、要素間をデリミタで区切られた<br>
     * 文字列が、返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testToHexString03() throws Exception {
        // 入力値の設定
        byte[] byteArray = { 0, 10, 100 };

        // テスト実行
        String result = StringUtil.toHexString(byteArray, "/");

        // 結果確認
        assertEquals("00/0A/64", result);
    }

    /**
     * testToHexString04() (正常系)<br>
     * 観点：C<br>
     * 入力値：要素数3のバイト配列、デリミタが空文字列<br>
     * 期待値：入力要素を16進変換し、結合した文字列<br>
     * デリミタが空文字の時、入力要素が16進変換され、そのまま 結合されていること。
     * @throws Exception 例外
     */
    @Test
    public void testToHexString04() throws Exception {
        // 入力値の設定
        byte[] byteArray = { 0, 10, 100 };

        // テスト実行
        String result = StringUtil.toHexString(byteArray, "");

        // 結果確認
        assertEquals("000A64", result);
    }

    /**
     * testToHexString05() (正常系)<br>
     * 観点：C<br>
     * 入力値：要素数3のバイト配列、デリミタがnull<br>
     * 期待値：入力要素を16進変換し、結合した文字列<br>
     * デリミタがnullの時、入力要素が16進変換され、そのまま 結合されていること。
     * @throws Exception 例外
     */
    @Test
    public void testToHexString05() throws Exception {
        // 入力値の設定
        byte[] byteArray = { 0, 10, 100 };

        // テスト実行
        String result = StringUtil.toHexString(byteArray, null);

        // 結果確認
        assertEquals("000A64", result);
    }

    /**
     * testToHexString06() (異常系)<br>
     * 観点：C<br>
     * 入力値：配列がnull、デリミタが"/"<br>
     * 期待値：NullPointerException発生<br>
     * 配列がnullの時、<br>
     * NullPointerExceptiが発生することを確認
     * @throws Exception 例外
     */
    @Test
    public void testToHexString06() throws Exception {
        // 入力値の設定
        byte[] byteArray = null;

        try {
            // テスト実行
            StringUtil.toHexString(byteArray, "/");
            fail();
        } catch (NullPointerException e) {
            return;
        }

    }

    /**
     * testParseCSVString01() (正常系)<br>
     * 観点：A<br>
     * 入力値：1単語(カンマなし)<br>
     * 期待値：入力文字を1つの要素とする文字列配列<br>
     * 配列要素のうち、分割されずにString配列の要素として、 そのまま設定されていること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVString01() throws Exception {
        // 入力値の設定
        String input = "abcde";

        // テスト実行
        String[] result = StringUtil.parseCSV(input);

        // 結果確認
        // 期待値
        String[] hope = { "abcde" };
        assertEquals(hope.length, result.length);
        assertEquals(hope[0], result[0]);
    }

    /**
     * testParseCSVString02() (正常系)<br>
     * 観点：C<br>
     * 入力値：null<br>
     * 期待値：要素数0の文字列配列<br>
     * 入力値がnullのとき、文字列長0の文字列が返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVString02() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト実行
        String[] result = StringUtil.parseCSV(input);

        // 結果確認
        assertEquals(0, result.length);
    }

    /**
     * testParseCSVString03() (正常系)<br>
     * 観点：B<br>
     * 入力値：5単語カンマ区切り(空文字列を先頭と末尾および途中に含む)<br>
     * 期待値：カンマ間の5つの文字列を要素とする文字列配列<br>
     * String[]配列がカンマ毎で区切られ、配列化されていること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVString03() throws Exception {
        // 入力値の設定
        String input = ",abcde,,あいうえお,";

        // テスト実行
        String[] result = StringUtil.parseCSV(input);

        // 結果確認
        // 期待値
        String[] hope = { "", "abcde", "", "あいうえお", "" };
        assertEquals(hope.length, result.length);
        for (int i = 0; i < hope.length; i++) {
            assertEquals(hope[i], result[i]);
        }
    }

    /**
     * testParseCSVString04() (正常系)<br>
     * 観点：B<br>
     * 入力値：空文字<br>
     * 期待値：空の配列<br>
     * 入力値が空文字のとき、空の配列が返却されることと。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVString04() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト実行
        String[] result = StringUtil.parseCSV(input);

        // 結果確認
        // 期待値
        String[] hope = { "" };
        assertEquals(hope.length, result.length);
        assertEquals(hope[0], result[0]);
    }

    /**
     * testParseCSVStringString01() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字=""<br>
     * エスケープ文字="\\" 期待値：空の配列<br>
     * 入力値が空文字のとき、第一要素が空文字の文字配列が返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString01() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV("", "\\");

        // 結果確認
        assertEquals(1, result.length);
        assertEquals("", result[0]);
    }

    /**
     * testParseCSVStringString02() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字=null<br>
     * エスケープ文字="\\" 期待値：空の配列<br>
     * 入力値がnullのとき、要素数0の空の配列が返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString02() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV(null, "\\");

        // 結果確認
        assertEquals(0, result.length);
    }

    /**
     * testParseCSVStringString03() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字="a,b"<br>
     * エスケープ文字="\\" 期待値：String[]{"a", "b"}<br>
     * エスケープ文字がカンマの前に存在しない場合、 カンマ区切りの配列が返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString03() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV("a,b", "\\");

        // 結果確認
        assertEquals(2, result.length);
        String[] hope = new String[] { "a", "b" };
        for (int i = 0; i < result.length; i++) {
            assertEquals(hope[i], result[i]);
        }
    }

    /**
     * testParseCSVStringString04() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字=",a,b"<br>
     * エスケープ文字="\\" 期待値：String[]{"", "a", "b"}<br>
     * 入力文字の先頭がカンマであるとき、第一要素が空文字であること。 カンマ区切りの配列が返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString04() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV(",a,b", "\\");

        // 結果確認
        assertEquals(3, result.length);
        String[] hope = new String[] { "", "a", "b" };
        for (int i = 0; i < hope.length; i++) {
            assertEquals(hope[i], result[i]);
        }
    }

    /**
     * testParseCSVStringString05() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字="a,b,"<br>
     * エスケープ文字="\\" 期待値：String[]{"a", "b", ""}<br>
     * 入力文字の末尾がカンマであるとき、最終要素が空文字であること。 カンマ区切りの配列が返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString05() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV("a,b,", "\\");

        // 結果確認
        assertEquals(3, result.length);
        String[] hope = new String[] { "a", "b", "" };
        for (int i = 0; i < hope.length; i++) {
            assertEquals(hope[i], result[i]);
        }
    }

    /**
     * testParseCSVStringString06() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字="a\\,b,c"<br>
     * エスケープ文字="\\" 期待値：String[]{"a,b", "c"}<br>
     * 入力文字の中にエスケープ文字＋カンマがあるとき、 文字区切りが行なわれないこと。 それ以外のカンマは区切られること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString06() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV("a\\,b,c", "\\");

        // 結果確認
        assertEquals(2, result.length);
        String[] hope = new String[] { "a,b", "c" };
        for (int i = 0; i < hope.length; i++) {
            assertEquals(hope[i], result[i]);
        }
    }

    /**
     * testParseCSVStringString07() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字="a\\b,c"<br>
     * エスケープ文字="\\" 期待値：String[]{"ab", "c"}<br>
     * 入力文字の中にエスケープ文字＋カンマ以外の文字があるとき、 文字区切りが行なわれず、返却文字列にエスケープ文字が混入していないこと。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString07() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV("a\\b,c", "\\");

        // 結果確認
        assertEquals(2, result.length);
        String[] hope = new String[] { "ab", "c" };
        for (int i = 0; i < hope.length; i++) {
            assertEquals(hope[i], result[i]);
        }
    }

    /**
     * testParseCSVStringString08() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字="\\,ab,c"<br>
     * エスケープ文字="\\" 期待値：String[]{",ab", "c"}<br>
     * 先頭がエスケープ文字＋カンマ文字であるとき、 返却される先頭の要素の第一文字がカンマであること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString08() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV("\\,ab,c", "\\");

        // 結果確認
        assertEquals(2, result.length);
        String[] hope = new String[] { ",ab", "c" };
        for (int i = 0; i < hope.length; i++) {
            assertEquals(hope[i], result[i]);
        }
    }

    /**
     * testParseCSVStringString09() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字="ab,c\\,"<br>
     * エスケープ文字="\\" 期待値：String[]{"ab", "c,"}<br>
     * 終端がエスケープ文字＋カンマ文字であるとき、 返却される終端の要素の最終文字がカンマであること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString09() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV("ab,c\\,", "\\");

        // 結果確認
        assertEquals(2, result.length);
        String[] hope = new String[] { "ab", "c," };
        for (int i = 0; i < hope.length; i++) {
            assertEquals(hope[i], result[i]);
        }
    }

    /**
     * testParseCSVStringString10() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字="ab,,,c"<br>
     * エスケープ文字="\\" 期待値：String[]{"ab", "", "", "c"}<br>
     * カンマが複数個連続で入力された時、カンマ区切り数分の 空文字要素が返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString10() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV("ab,,,c", "\\");

        // 結果確認
        assertEquals(4, result.length);
        String[] hope = new String[] { "ab", "", "", "c" };
        for (int i = 0; i < hope.length; i++) {
            assertEquals(hope[i], result[i]);
        }
    }

    /**
     * testParseCSVStringString11() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字="ab!,#,,c"<br>
     * エスケープ文字="#!" 期待値：String[]{"ab,,", "c"}<br>
     * エスケープ文字とカンマが複数個連続で入力された時、 カンマのみ連続して配列要素に設定されること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString11() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV("ab!,#,,c", "#!");

        // 結果確認
        assertEquals(2, result.length);
        String[] hope = new String[] { "ab,,", "c" };
        for (int i = 0; i < hope.length; i++) {
            assertEquals(hope[i], result[i]);
        }
    }

    /**
     * testParseCSVStringString12() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字="ab#!@,c"<br>
     * エスケープ文字="!@#" 期待値：String[]{"ab!@,", "c"}<br>
     * エスケープ文字列が複数個連続して入力された場合
     * <ol>
     * <li>エスケープ文字の直後のエスケープ文字が通常文字として 出力されること。</li>
     * <li>さらに続いてエスケープ文字が存在する時、 エスケープ文字列として作用すること。</li>
     * </ol>
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString12() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV("ab#!@,c", "!@#");

        // 結果確認
        assertEquals(1, result.length);
        String[] hope = new String[] { "ab!,c" };
        for (int i = 0; i < hope.length; i++) {
            assertEquals(hope[i], result[i]);
        }
    }

    /**
     * testParseCSVStringString13() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字="ab,c"<br>
     * エスケープ文字="" 期待値：String[]{"ab", "c"}<br>
     * エスケープ文字列が空文字の時、エスケープが行なわれず カンマ区切り文字列が配列に変換され、返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString13() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV("ab,c", "");

        // 結果確認
        assertEquals(2, result.length);
        String[] hope = new String[] { "ab", "c" };
        for (int i = 0; i < hope.length; i++) {
            assertEquals(hope[i], result[i]);
        }
    }

    /**
     * testParseCSVStringString14() (正常系)<br>
     * 観点：C<br>
     * 入力値：入力文字="ab,c"<br>
     * エスケープ文字=null 期待値：String[]{"ab", "c"}<br>
     * エスケープ文字列がnullの時、エスケープが行なわれず カンマ区切り文字列が配列に変換され、返却されること。
     * @throws Exception 例外
     */
    @Test
    public void testParseCSVStringString14() throws Exception {
        // 入力値の設定

        // テスト実行
        String[] result = StringUtil.parseCSV("ab,c", null);

        // 結果確認
        assertEquals(2, result.length);
        String[] hope = new String[] { "ab", "c" };
        for (int i = 0; i < hope.length; i++) {
            assertEquals(hope[i], result[i]);
        }
    }

    /**
     * testDump01。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値 :String(要素が１つ)<br>
     * 期待値 :ダンプ対象文字列<br>
     * 説明：ダンプ対象文字列に変換されていることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testDump01() throws Exception {
        // 入力値の設定
        final String num = "1";
        final String area = "東京";

        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put(num, area);

        // テスト実行
        String result = StringUtil.dump(map);

        // 結果
        StringBuffer sb = new StringBuffer();
        sb.append(LINE_SEP);
        sb.append("Map{");
        sb.append(LINE_SEP);
        sb.append("1=東京");
        sb.append(LINE_SEP);
        sb.append("}");

        // 結果確認
        assertEquals(sb.toString(), result);
    }

    /**
     * testDump02。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値 :String(要素が複数)<br>
     * 期待値 :ダンプ対象文字列<br>
     * 説明：ダンプ対象文字列に複数、変換されていることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testDump02() throws Exception {
        // 入力値の設定
        final String[] num = { "1", "2", "3" };
        final String[] area = { "東京", "京都", "兵庫" };

        Map<String, String> map = new LinkedHashMap<String, String>();
        for (int i = 0; i < num.length; i++) {
            map.put(num[i], area[i]);
        }
        // テスト実行
        String result = StringUtil.dump(map);

        // 結果
        StringBuffer sb = new StringBuffer();
        sb.append(LINE_SEP);
        sb.append("Map{");
        sb.append(LINE_SEP);
        sb.append("1=東京");
        sb.append(LINE_SEP);
        sb.append("2=京都");
        sb.append(LINE_SEP);
        sb.append("3=兵庫");
        sb.append(LINE_SEP);
        sb.append("}");

        // 結果確認
        assertEquals(sb.toString(), result);
    }

    /**
     * testDump03。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値 :空<br>
     * 期待値 :ダンプ対象文字列<br>
     * 説明：配列ダンプ対象文字列に変換されていることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testDump03() throws Exception {
        // 入力値の設定
        @SuppressWarnings("rawtypes")
        Map<?, ?> map = new LinkedHashMap();

        // テスト実行
        String result = StringUtil.dump(map);

        // 結果
        StringBuffer sb = new StringBuffer();
        sb.append(LINE_SEP);
        sb.append("Map{");
        sb.append(LINE_SEP);
        sb.append("}");

        // 結果確認
        assertEquals(sb.toString(), result);
    }

    /**
     * testDump04。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値 :配列中にnull<br>
     * 期待値 :nullという文字列で出力される。<br>
     * 説明：配列要素中にnullが検出された場合、<br>
     * "null"という文字列が出力されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testDump04() throws Exception {
        // 入力値の設定
        final String[] num = { "1", "2", "3" };
        final String[] area = { "東京", null, "兵庫" };

        Map<String, String> map = new LinkedHashMap<String, String>();
        for (int i = 0; i < num.length; i++) {
            map.put(num[i], area[i]);
        }
        // テスト実行
        String result = StringUtil.dump(map);

        // 結果
        StringBuffer sb = new StringBuffer();
        sb.append(LINE_SEP);
        sb.append("Map{");
        sb.append(LINE_SEP);
        sb.append("1=東京");
        sb.append(LINE_SEP);
        sb.append("2=null");
        sb.append(LINE_SEP);
        sb.append("3=兵庫");
        sb.append(LINE_SEP);
        sb.append("}");

        // 結果確認
        assertEquals(sb.toString(), result);
    }

    /**
     * testDump05。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値 :キーがnull<br>
     * 期待値 :"null"が表示されること<br>
     * 説明：キーがnullの場合、<br>
     * "null"が表示されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testDump05() throws Exception {
        // 入力値の設定
        final String num = null;
        final String area = "東京";

        Map<String, String> map = new LinkedHashMap<String, String>();

        map.put(num, area);
        // テスト実行
        String result = StringUtil.dump(map);
        // 結果
        StringBuffer sb = new StringBuffer();
        sb.append(LINE_SEP);
        sb.append("Map{");
        sb.append(LINE_SEP);
        sb.append("null=東京");
        sb.append(LINE_SEP);
        sb.append("}");
        // 結果確認
        assertEquals(sb.toString(), result);
    }

    /**
     * testDump06。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値 :配列(キーが１つ)<br>
     * 期待値 :配列ダンプ対象文字列<br>
     * 説明：配列ダンプ対象文字列に変換されていることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testDump06() throws Exception {
        // 入力値の設定
        final String num = "1";
        final Vector<String> area = new Vector<String>();
        area.add(0, "東京");
        area.add(1, "大阪");

        @SuppressWarnings("rawtypes")
        Map<String, Vector> map = new LinkedHashMap<String, Vector>();
        map.put(num, area);

        // テスト実行
        String result = StringUtil.dump(map);

        // 結果確認
        assertEquals(LINE_SEP + "Map{" + LINE_SEP + "1=[東京, 大阪]" + LINE_SEP
                + "}", result);
    }

    /**
     * testDump07。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値 :配列(キーが複数)<br>
     * 期待値 :配列ダンプ対象文字列<br>
     * 説明：配列ダンプ対象文字列に変換されていることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testDump07() throws Exception {
        // 入力値の設定
        final String num1 = "1";
        final String num2 = "2";
        Vector<String> area = new Vector<String>();
        Vector<String> tel = new Vector<String>();

        area.add(0, "東京");
        area.add(1, "大阪");
        tel.add(0, "03");
        tel.add(1, "06");

        @SuppressWarnings("rawtypes")
        Map<String, Vector> map = new LinkedHashMap<String, Vector>();
        map.put(num1, area);
        map.put(num2, tel);

        // テスト実行
        String result = StringUtil.dump(map);

        // 結果
        StringBuffer sb = new StringBuffer();
        sb.append(LINE_SEP);
        sb.append("Map{");
        sb.append(LINE_SEP);
        sb.append("1=[東京, 大阪]");
        sb.append(LINE_SEP);
        sb.append("2=[03, 06]");
        sb.append(LINE_SEP);
        sb.append("}");

        // 結果確認
        assertEquals(sb.toString(), result);
    }

    /**
     * testDump08。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値 :mapがnull<br>
     * 期待値 :null<br>
     * 説明：mapがnullの場合、<br>
     * nullが返却されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testDump08() throws Exception {
        // 入力値の設定
        Map<?, ?> map = null;

        // テスト実行
        String result = StringUtil.dump(map);

        // 結果確認
        assertNull(result);
    }

    /**
     * testDump9。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値 :キーが複数（中身が配列）<br>
     * 期待値 :配列ダンプ対象文字列<br>
     * 説明：配列ダンプ対象文字列に変換されていることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testDump9() throws Exception {
        // 入力値の設定
        final String num1 = "1";
        final String num2 = "2";
        Vector<String> area = new Vector<String>();
        String tel[] = { "03", "06" };

        area.add(0, "東京");
        area.add(1, "大阪");

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(num1, area);
        map.put(num2, tel);

        // テスト実行
        String result = StringUtil.dump(map);

        // 結果
        StringBuffer sb = new StringBuffer();
        sb.append(LINE_SEP);
        sb.append("Map{");
        sb.append(LINE_SEP);
        sb.append("1=[東京, 大阪]");
        sb.append(LINE_SEP);
        sb.append("2={03,06}");
        sb.append(LINE_SEP);
        sb.append("}");

        // 結果確認
        assertEquals(sb.toString(), result);
    }

    /**
     * testGetArraysStr01。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値 :String配列<br>
     * 期待値 :配列ダンプ対象文字列<br>
     * 説明：配列ダンプ対象文字列に変換されていることを確認する。 ArrayUtils.toString()の呼び出し確認のために、１ケースのみとする。
     * @throws Exception 例外
     */
    @Test
    public void testGetArraysStr01() throws Exception {
        // 入力値の設定
        final String[] str = { "1", "2", "3", "4", "5" };

        // テスト実行
        String result = StringUtil.getArraysStr(str);

        // 結果確認
        assertEquals("{1,2,3,4,5}", result);
    }

    /**
     * testHankakuToZenkaku01。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角文字(一文字)<br>
     * 期待値：全角文字 説明：半角普通英数字一文字が全角普通英数字に変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku01() throws Exception {
        // 入力値の設定
        String input = "A";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("Ａ", result);
    }

    /**
     * testHankakuToZenkaku02。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角文字(複数文字)<br>
     * 期待値：全角文字 説明：半角普通文字が複数、全角普通文字に変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku02() throws Exception {
        // 入力値の設定
        String input = "ｱﾞ!A8";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("ア゛！Ａ８", result);
    }

    /**
     * testHankakuToZenkaku03。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角サ(一文字)<br>
     * 期待値：全角サ 説明：半角サ一文字が全角サに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku03() throws Exception {
        // 入力値の設定
        String input = "ｻ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("サ", result);
    }

    /**
     * testHankakuToZenkaku04。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角カサタハウ(複数文字)<br>
     * 期待値：全角カサタハウ 説明：半角カサタハウが複数、全角カサタハウに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku04() throws Exception {
        // 入力値の設定
        String input = "ｶｻﾀﾊｳ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("カサタハウ", result);
    }

    /**
     * testHankakuToZenkaku05。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角濁点ｶﾞ(一文字)<br>
     * 期待値：全角濁点ガ 説明：半角濁点一文字が全角濁点に変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku05() throws Exception {
        // 入力値の設定
        String input = "ｶﾞ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("ガ", result);
    }

    /**
     * testHankakuToZenkaku06。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角濁点ｶﾞｻﾞﾀﾞﾊﾞｳﾞ(複数文字)<br>
     * 期待値：全角濁点ガザダバヴ 説明：半角濁点が複数文字、全角濁点に変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku06() throws Exception {
        // 入力値の設定
        String input = "ｶﾞｻﾞﾀﾞﾊﾞｳﾞ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("ガザダバヴ", result);
    }

    /**
     * testHankakuToZenkaku07。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角半濁点ﾊﾟ行(一文字)<br>
     * 期待値：全角半濁点パ行 説明：半角半濁点(一文字)が全角半濁点に変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku07() throws Exception {
        // 入力値の設定
        String input = "ﾎﾟ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("ポ", result);
    }

    /**
     * testHankakuToZenkaku08。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角半濁点ﾊﾟ行(複数文字)<br>
     * 期待値：全角半濁点パ行 説明：半角半濁点が複数文字、全角半濁点に変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku08() throws Exception {
        // 入力値の設定
        String input = "ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("パピプペポ", result);
    }

    /**
     * testHankakuToZenkaku09。<br>
     * （正常系）A<br>
     * 観点：<br>
     * <br>
     * 入力値：半角ﾜ<br>
     * 期待値：全角ワ 説明：半角ワが全角ワに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku09() throws Exception {
        // 入力値の設定
        String input = "ﾜ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("ワ", result);
    }

    /**
     * testHankakuToZenkaku10。<br>
     * （正常系）A<br>
     * 観点：<br>
     * <br>
     * 入力値：半角ｦ<br>
     * 期待値：全角ヲ 説明：半角ヲが全角ヲに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku10() throws Exception {
        // 入力値の設定
        String input = "ｦ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("ヲ", result);
    }

    /**
     * testHankakuToZenkaku11。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角ﾜ"<br>
     * 期待値：全角ワ” 説明：半角濁点ワが全角濁点ワに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku11() throws Exception {
        // 入力値の設定
        String input = "ﾜﾞ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("\u30f7", result);
    }

    /**
     * testHankakuToZenkaku12。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角ｦ"<br>
     * 期待値：全角ヲ” 説明：半角濁点ヲが全角濁点ヲに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku12() throws Exception {
        // 入力値の設定
        String input = "ｦﾞ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("\u30fa", result);
    }

    /**
     * testHankakuToZenkaku13。<br>
     * （正常系）A<br>
     * 観点：<br>
     * <br>
     * 入力値：半角ﾜ<br>
     * 期待値：全角ワ 説明：半角ワが文字列の途中にある時、全角ワに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku13() throws Exception {
        // 入力値の設定
        String input = "Aﾜ1";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("Ａワ１", result);
    }

    /**
     * testHankakuToZenkaku14。<br>
     * （正常系）A<br>
     * 観点：<br>
     * <br>
     * 入力値：半角ｦ<br>
     * 期待値：全角ヲ 説明：半角ヲが文字列の途中にある時、全角ヲに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku14() throws Exception {
        // 入力値の設定
        String input = "Bｦ8";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("Ｂヲ８", result);
    }

    /**
     * testHankakuToZenkaku15。<br>
     * （正常系）A<br>
     * 観点：<br>
     * <br>
     * 入力値：半角ﾜﾞ<br>
     * 期待値：全角ﾜﾞ 説明：半角濁点ワが文字列の途中にある時、全角濁点ワに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku15() throws Exception {
        // 入力値の設定
        String input = "Bﾜﾞ8";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("Ｂ\u30f7８", result);
    }

    /**
     * testHankakuToZenkaku16。<br>
     * （正常系）A<br>
     * 観点：<br>
     * <br>
     * 入力値：半角ｦﾞ<br>
     * 期待値：全角ｦﾞ 説明：半角濁点ワが文字列の途中にある時、全角濁点ワに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku16() throws Exception {
        // 入力値の設定
        String input = "Bｦﾞ8";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("Ｂ\u30fa８", result);
    }

    /**
     * testHankakuToZenkaku17。<br>
     * （正常系）A<br>
     * 観点：<br>
     * <br>
     * 入力値：半角ﾜ<br>
     * 期待値：全角ワ 説明：半角ワが文字列の末尾にある時、全角ワに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku17() throws Exception {
        // 入力値の設定
        String input = "ｱAﾜ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("アＡワ", result);
    }

    /**
     * testHankakuToZenkaku18。<br>
     * （正常系）A<br>
     * 観点：<br>
     * <br>
     * 入力値：半角ｦ<br>
     * 期待値：全角ヲ 説明：半角ヲが文字列の末尾にある時、全角ヲに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku18() throws Exception {
        // 入力値の設定
        String input = "tBｦ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("ｔＢヲ", result);
    }

    /**
     * testHankakuToZenkaku19。<br>
     * （正常系）A<br>
     * 観点：<br>
     * <br>
     * 入力値：半角ﾜﾞ<br>
     * 期待値：全角ﾜﾞ 説明：半角濁点ワが文字列の末尾にある時、全角濁点ワに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku19() throws Exception {
        // 入力値の設定
        String input = "ｻBﾜﾞ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("サＢ\u30f7", result);
    }

    /**
     * testHankakuToZenkaku20。<br>
     * （正常系）A<br>
     * 観点：<br>
     * <br>
     * 入力値：半角ｦﾞ<br>
     * 期待値：全角ｦﾞ 説明：半角濁点ヲが文字列の末尾にある時、全角濁点ヲに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku20() throws Exception {
        // 入力値の設定
        String input = "ｱBｦﾞ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("アＢ\u30fa", result);
    }

    /**
     * testHankakuToZenkaku21。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角全角混合文字"<br>
     * 期待値：全角文字 説明：入力値に全角文字と半角文字を混合させたものを設定し、<br>
     * 全て全角文字に変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku21() throws Exception {
        // 入力値の設定
        String input = "サｼズｾﾞソ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("サシズゼソ", result);
    }

    /**
     * testHankakuToZenkaku22。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：null<br>
     * 期待値：null 説明：置換対象文字列がnullの時、nullを戻り値とし処理を終了することを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku22() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertNull(result);
    }

    /**
     * testHankakuToZenkaku23。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：空文字<br>
     * 期待値：空文字 説明：置換対象文字列が空文字の時、空文字を戻り値とし処理を終了することを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku23() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("", result);
    }

    /**
     * testHankakuToZenkaku24。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角半濁点文字ｶﾟｷﾟ<br>
     * 期待値：全角半濁点文字カ゜キ゜ 説明：半角半濁点文字が全角半濁点文字に変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku24() throws Exception {
        // 入力値の設定
        String input = "ｶﾟｷﾟ";

        // テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        // 結果確認
        assertEquals("カ゜キ゜", result);
    }

    /**
     * testHankakuToZenkaku25。<br>
     *
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角半濁点文字ﾜﾟｦﾟﾟ<br>
     * 期待値：全角半濁点文字ワ゜ヲ゜
     *
     * 説明：半角半濁点文字が全角半濁点文字に変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testHankakuToZenkaku25() throws Exception {
        //入力値の設定
        String input = "ﾜﾟｦﾟ";

        //テスト実行
        String result = StringUtil.hankakuToZenkaku(input);

        //結果確認
        assertEquals("ワ゜ヲ゜", result);
    }

    /**
     * testZenkakuToHankaku01。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：全角文字<br>
     * 期待値：半角文字（複数文字変換）<br>
     * 説明：全角文字が半角文字に複数文字変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testZenkakuToHankaku01() throws Exception {
        // 入力値の設定
        String input = "Ａ！ア";

        // テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        // 結果確認
        assertEquals("A!ｱ", result);
    }

    /**
     * testZenkakuToHankaku02。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：全角文字<br>
     * 期待値：半角文字（一文字変換）<br>
     * 説明：全角文字に対して半角文字に一文字変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testZenkakuToHankaku02() throws Exception {
        // 入力値の設定
        String input = "Ａ";

        // テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        // 結果確認
        assertEquals("A", result);
    }

    /**
     * testZenkakuToHankaku02。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：全角カサタハ<br>
     * 期待値：半角カサタハ）<br>
     * 説明：全角カサタハが半角カサタハに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testZenkakuToHankaku03() throws Exception {
        // 入力値の設定
        String input = "カサタハ";

        // テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        // 結果確認
        assertEquals("ｶｻﾀﾊ", result);
    }

    /**
     * testZenkakuToHankaku04。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：全角濁点<br>
     * 期待値：半角濁点<br>
     * 説明：全角濁点文字が二文字に分解されて表示されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testZenkakuToHankaku04() throws Exception {
        // 入力値の設定
        String input = "ガザダ";

        // テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        // 結果確認
        assertEquals("ｶﾞｻﾞﾀﾞ", result);
    }

    /**
     * testZenkakuToHankaku05。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：全角半濁点文字<br>
     * 期待値：半角半濁点文字<br>
     * 説明：全角濁点・半濁点文字が二文字に分解されて表示されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testZenkakuToHankaku05() throws Exception {
        // 入力値の設定
        String input = "パポ";

        // テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        // 結果確認
        assertEquals("ﾊﾟﾎﾟ", result);
    }

    /**
     * testZenkakuToHankaku06。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：全角ワ<br>
     * 期待値：半角ワ<br>
     * 説明：全角ワが半角ワに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testZenkakuToHankaku06() throws Exception {
        // 入力値の設定
        String input = "Ａワ";

        // テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        // 結果確認
        assertEquals("Aﾜ", result);
    }

    /**
     * testZenkakuToHankaku07。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：全角ヲ<br>
     * 期待値：半角ヲ<br>
     * 説明：全角ヲが半角ヲに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testZenkakuToHankaku07() throws Exception {
        // 入力値の設定
        String input = "Ａヲ";

        // テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        // 結果確認
        assertEquals("Aｦ", result);
    }

    /**
     * testZenkakuToHankaku08。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：全角濁点ワ<br>
     * 期待値：半角濁点ワ<br>
     * 説明：全角濁点ワが半角濁点ﾜに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testZenkakuToHankaku08() throws Exception {
        // 入力値の設定
        String input = "ア\u30f7";

        // テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        // 結果確認
        assertEquals("ｱﾜﾞ", result);
    }

    /**
     * testZenkakuToHankaku09。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：全角濁点ヲ<br>
     * 期待値：半角濁点ヲ<br>
     * 説明：全角濁点ヲが半角濁点ｦに変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testZenkakuToHankaku09() throws Exception {
        // 入力値の設定
        String input = "\u30faヴ";

        // テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        // 結果確認
        assertEquals("ｦﾞｳﾞ", result);
    }

    /**
     * testZenkakuToHankaku10。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角・全角混合文字<br>
     * 期待値：半角文字に全て変換されること<br>
     * 説明：半角・全角混合文字が全て半角に変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testZenkakuToHankaku10() throws Exception {
        // 入力値の設定
        String input = "サ\u30faｲAピ";

        // テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        // 結果確認
        assertEquals("ｻｦﾞｲAﾋﾟ", result);
    }

    /**
     * testZenkakuToHankaku11。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：空文字<br>
     * 期待値：空文字<br>
     * 説明：置換対象文字列が空文字の時、空文字を戻り値とし処理を終了することを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testZenkakuToHankaku11() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        // 結果確認
        assertEquals("", result);
    }

    /**
     * testZenkakuToHankakuu12。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：null<br>
     * 期待値：null<br>
     * 説明：置換対象文字列がnullの時、nullを戻り値とし処理を終了することを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testZenkakuToHankaku12() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        // 結果確認
        assertEquals(null, result);
    }

    /**
     * testZenkakuToHankakuu13。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：半角化不可能な文字<br>
     * 期待値：全角文字<br>
     * 説明：半角化不可能な文字列がそのまま全角文字列で出力されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testZenkakuToHankaku13() throws Exception {
        // 入力値の設定
        String input = "入力値";

        // テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        // 結果確認
        assertEquals("入力値", result);
    }

    /**
     * testZenkakuToHankakuu14。<br>
     *
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：ワ゜ヲ゜<br>
     * 期待値：ﾜﾟｦﾟ<br>
     *
     * 説明：半角化不可能な文字列がそのまま全角文字列で出力されることを確認する。
     * @throws Exception 例外 */
    @Test
    public void testZenkakuToHankaku14() throws Exception {
        //入力値の設定
        String input = "ワ゜ヲ゜";

        //テスト実行
        String result = StringUtil.zenkakuToHankaku(input);

        //結果確認
        assertEquals("ﾜﾟｦﾟ", result);
    }

    /**
     * testFilter01。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：複数文字列（HTML中にそのまま出力すると問題がある文字を一部含む）<br>
     * 期待値：HTMLメタ文字<br>
     * 説明：二文字以上の文字列中、一部がHTMLメタ文字に変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testFilter01() throws Exception {
        // 入力値の設定
        String input = "a & b";

        // テスト実行
        String result = StringUtil.filter(input);

        // 結果確認
        assertEquals("a &amp; b", result);
    }

    /**
     * testFilter02。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：複数文字列（HTML中にそのまま出力すると問題がある文字を含まない）<br>
     * 期待値：そのまま出力される<br>
     * 説明：HTMLメタ文字に変換されることなくそのまま出力されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testFilter02() throws Exception {
        // 入力値の設定
        String input = "abc";

        // テスト実行
        String result = StringUtil.filter(input);

        // 結果確認
        assertEquals("abc", result);
    }

    /**
     * testFilter03。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：HTML中にそのまま出力すると問題がある文字（複数文字）<br>
     * 期待値：HTMLメタ文字<br>
     * 説明：HTMLメタ文字に全てが変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testFilter03() throws Exception {
        // 入力値の設定
        String input = "< & > \" '";

        // テスト実行
        String result = StringUtil.filter(input);

        // 結果確認
        assertEquals("&lt; &amp; &gt; &quot; &#39;", result);
    }

    /**
     * testFilter04。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：HTML中にそのまま出力すると問題がある文字（一文字変換）<br>
     * 期待値：HTMLメタ文字<br>
     * 説明：HTMLメタ文字に一文字変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testFilter04() throws Exception {
        // 入力値の設定
        String input = "<";

        // テスト実行
        String result = StringUtil.filter(input);

        // 結果確認
        assertEquals("&lt;", result);
    }

    /**
     * testFilter05。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：HTML中にそのまま出力しても問題のない文字（一文字変換）<br>
     * 期待値：そのまま出力されること<br>
     * 説明：HTMLメタ文字に変換されることなく、そのまま出力されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testFilter05() throws Exception {
        // 入力値の設定
        String input = "ア";

        // テスト実行
        String result = StringUtil.filter(input);

        // 結果確認
        assertEquals("ア", result);
    }

    /**
     * testFilter06。<br>
     * （異常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：null<br>
     * 期待値：null<br>
     * 説明：置換対象文字列がnullのとき、nullが返却されることを確認する。<br>
     * @throws Exception 例外
     */
    @Test
    public void testFilter06() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト実行
        String result = StringUtil.filter(input);

        // 結果確認
        assertNull(result);
    }

    /**
     * testFilter07。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：空文字<br>
     * 期待値：空文字<br>
     * 説明：HTMLメタ文字に変換されることなく、空文字がそのまま出力されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testFilter07() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト実行
        String result = StringUtil.filter(input);

        // 結果確認
        assertEquals("", result);
    }

    /**
     * testToLikeCondition01。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：検索条件文字列<br>
     * 期待値：LIKE述語のパターン文字列(一文字変換）<br>
     * 説明：検索条件文字列が一文字LIKE述語のパターン文字列に変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testToLikeCondition01() throws Exception {
        // 入力値の設定
        String input = "a%";

        // テスト実行
        String result = StringUtil.toLikeCondition(input);

        // 結果確認
        assertEquals("a~%%", result);
    }

    /**
     * testToLikeCondition02。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：エスケープ文字<br>
     * 期待値：エスケープ<br>
     * 説明：LIKE述語のパターン文字列で用いるエスケープ文字が<br>
     * エスケープ文字でエスケープされることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testToLikeCondition02() throws Exception {
        // 入力値の設定
        String input = "~";

        // テスト実行
        String result = StringUtil.toLikeCondition(input);

        // 結果確認
        assertEquals("~~%", result);
    }

    /**
     * testToLikeCondition03。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：検索条件文字列<br>
     * 期待値：LIKE述語のパターン文字列（複数文字変換）<br>
     * 説明：検索条件文字列が複数文字文字LIKE述語のパターン文字列に変換されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testToLikeCondition03() throws Exception {
        // 入力値の設定
        String input = "_a%";

        // テスト実行
        String result = StringUtil.toLikeCondition(input);

        // 結果確認
        assertEquals("~_a~%%", result);
    }

    /**
     * testToLikeCondition04。<br>
     * （正常系）<br>
     * 観点：A<br>
     * <br>
     * 入力値：検索条件文字列以外の文字列<br>
     * 期待値：そのまま出力されること<br>
     * 説明：検索条件文字列以外の文字列は変換されることなく、そのまま出力されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testToLikeCondition04() throws Exception {
        // 入力値の設定
        String input = "aa";

        // テスト実行
        String result = StringUtil.toLikeCondition(input);

        // 結果確認
        assertEquals("aa%", result);
    }

    /**
     * testToLikeCondition05。<br>
     * （異常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：null）<br>
     * 期待値：null<br>
     * 説明：置換対象文字列がnullのとき、nullが返却されることを確認する。<br>
     * @throws Exception 例外
     */
    @Test
    public void testToLikeCondition05() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト実行
        String result = StringUtil.toLikeCondition(input);

        // 結果確認
        assertNull(result);
    }

    /**
     * testToLikeCondition06。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：空文字<br>
     * 期待値：%<br>
     * 説明：置換対象文字列が空文字のとき、"%"が出力されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testToLikeCondition06() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト実行
        String result = StringUtil.toLikeCondition(input);

        // 結果確認
        assertEquals("%", result);
    }

    /**
     * testToLikeCondition07。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：％<br>
     * 期待値：~％%<br>
     * 説明：置換対象文字列が"％"のとき、"~％%"が出力されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testToLikeCondition07() throws Exception {
        // 入力値の設定
        String input = "％";

        // テスト実行
        String result = StringUtil.toLikeCondition(input);

        // 結果確認
        assertEquals("~％%", result);
    }

    /**
     * testToLikeCondition08。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：＿<br>
     * 期待値：~＿%<br>
     * 説明：置換対象文字列が"＿"のとき、"~＿%"が出力されることを確認する。
     * @throws Exception 例外
     */
    @Test
    public void testToLikeCondition08() throws Exception {
        // 入力値の設定
        String input = "＿";

        // テスト実行
        String result = StringUtil.toLikeCondition(input);

        // 結果確認
        assertEquals("~＿%", result);
    }

    /**
     * testCapitalizeInitial01。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値："abc"<br>
     * 期待値："Abc"<br>
     * 説明：一文字目が正常に大文字に変換できる場合
     * @throws Exception 例外
     */
    @Test
    public void testCapitalizeInitial01() throws Exception {
        // 入力値の設定
        String input = "abc";

        // テスト実行
        String result = StringUtil.capitalizeInitial(input);

        // 結果確認
        assertEquals("Abc", result);
    }

    /**
     * testCapitalizeInitial02。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値："Abc"<br>
     * 期待値："Abc"<br>
     * 説明：一文字目が最初から大文字の場合
     * @throws Exception 例外
     */
    @Test
    public void testCapitalizeInitial02() throws Exception {
        // 入力値の設定
        String input = "Abc";

        // テスト実行
        String result = StringUtil.capitalizeInitial(input);

        // 結果確認
        assertEquals("Abc", result);
    }

    /**
     * testCapitalizeInitial03。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値："123"<br>
     * 期待値："123"<br>
     * 説明：一文字目が大文字に変換できない文字の場合
     * @throws Exception 例外
     */
    @Test
    public void testCapitalizeInitial03() throws Exception {
        // 入力値の設定
        String input = "123";

        // テスト実行
        String result = StringUtil.capitalizeInitial(input);

        // 結果確認
        assertEquals("123", result);
    }

    /**
     * testCapitalizeInitial04。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：""<br>
     * 期待値：""<br>
     * 説明：空白の場合
     * @throws Exception 例外
     */
    @Test
    public void testCapitalizeInitial04() throws Exception {
        // 入力値の設定
        String input = "";

        // テスト実行
        String result = StringUtil.capitalizeInitial(input);

        // 結果確認
        assertEquals("", result);
    }

    /**
     * testCapitalizeInitial05。<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：<br>
     * 期待値：null<br>
     * 説明：nullの場合
     * @throws Exception 例外
     */
    @Test
    public void testCapitalizeInitial05() throws Exception {
        // 入力値の設定
        String input = null;

        // テスト実行
        String result = StringUtil.capitalizeInitial(input);

        // 結果確認
        assertEquals(null, result);
    }

    /**
     * testGetByteLength01<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：value=null<br>
     * 期待値：0<br>
     * 説明：引数valueがnullの場合<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetByteLength01() throws Exception {
        // 前処理
        String value = null;
        String encoding = null;

        // テスト実行
        int i = StringUtil.getByteLength(value, encoding);

        // 判定
        assertEquals(0, i);
    }

    /**
     * testGetByteLength02<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：value=空文字<br>
     * 期待値：0<br>
     * 説明：引数valueが空文字の場合<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetByteLength02() throws Exception {
        // 前処理
        String value = "";
        String encoding = null;

        // テスト実行
        int i = StringUtil.getByteLength(value, encoding);

        // 判定
        assertEquals(0, i);
    }

    /**
     * testGetByteLength03<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：value="aaa", encoding=null<br>
     * 期待値：3<br>
     * 説明：引数encodingがnullの場合<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetByteLength03() throws Exception {
        // 前処理
        String value = "aaa";
        String encoding = null;

        // テスト実行
        int i = StringUtil.getByteLength(value, encoding);

        // 判定
        assertEquals(3, i);
    }

    /**
     * testGetByteLength04<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：value="aaa", encoding=空文字<br>
     * 期待値：3<br>
     * 説明：引数encodingが空文字の場合<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetByteLength04() throws Exception {
        // 前処理
        String value = "aaa";
        String encoding = "";

        // テスト実行
        int i = StringUtil.getByteLength(value, encoding);

        // 判定
        assertEquals(3, i);
    }

    /**
     * testGetByteLength05<br>
     * （正常系）<br>
     * 観点：C<br>
     * <br>
     * 入力値：value="あああ", encoding="UTF-8"<br>
     * 期待値：9<br>
     * 説明：引数encodingが正しい文字エンコーディングの場合<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetByteLength05() throws Exception {
        // 前処理
        String value = "あああ";
        String encoding = "UTF-8";

        // テスト実行
        int i = StringUtil.getByteLength(value, encoding);

        // 判定
        assertEquals(9, i);
    }

    /**
     * testGetByteLength06<br>
     * （異常系）<br>
     * 観点：G<br>
     * <br>
     * 入力値：value="aaa", encoding="aaa"<br>
     * 期待値：例外：UnsupportedEncodingException<br>
     * 説明：引数encodingが不正な文字エンコーディングの場合<br>
     * @throws Exception 例外
     */
    @Test
    public void testGetByteLength06() throws Exception {
        // 前処理
        String value = "aaa";
        String encoding = "aaa";

        // テスト実行
        try {
            StringUtil.getByteLength(value, encoding);
            fail();
        } catch (UnsupportedEncodingException e) {
            // 判定
            return;
        }
    }
}
