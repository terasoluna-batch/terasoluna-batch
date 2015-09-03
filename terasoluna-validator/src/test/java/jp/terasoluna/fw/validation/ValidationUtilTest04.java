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

package jp.terasoluna.fw.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jp.terasoluna.fw.validation.PropertyTestCase;
import jp.terasoluna.utlib.UTUtil;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.validation.ValidationUtil} クラスのブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4> 検証ロジックのユーティリティクラス。
 * <p>
 * @see jp.terasoluna.fw.validation.ValidationUtil
 */
public class ValidationUtilTest04 extends PropertyTestCase {

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     */
    @Before
    public void setUpData() throws Exception {
        Field field = ValidationUtil.class.getDeclaredField("hankakuKanaList");
        field.setAccessible(true);
        field.set(ValidationUtil.class,
                "ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣");
        field = ValidationUtil.class.getDeclaredField("zenkakuKanaList");
        field.setAccessible(true);
        field.set(ValidationUtil.class, "アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソ"
                + "ザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホ" + "バビブベボパピプペポマミムメモヤユヨャュョラリルレロ"
                + "ワヮヰヱヲッンー");
    }

    /**
     * 終了処理を行う。
     * @throws Exception このメソッドで発生した例外
     */
    @After
    public void cleanUpData() throws Exception {
    }

    /**
     * testHasNotProhibitedChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) value:null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数valueがnullの場合、trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNotProhibitedChar01() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.hasNotProhibitedChar(null, "abc"));
    }

    /**
     * testHasNotProhibitedChar02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) value:""<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数valueが空文字の場合、trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNotProhibitedChar02() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.hasNotProhibitedChar("", "abc"));
    }

    /**
     * testHasNotProhibitedChar03() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) value:"abc"<br>
     * (引数) prohibitedChars:null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数prohibitedCharsがnullの場合、trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNotProhibitedChar03() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.hasNotProhibitedChar("abc", null));
    }

    /**
     * testHasNotProhibitedChar04() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) value:"abc"<br>
     * (引数) prohibitedChars:""（空文字）<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数prohibitedCharsが空文字の場合、trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNotProhibitedChar04() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.hasNotProhibitedChar("abc", ""));
    }

    /**
     * testHasNotProhibitedChar05() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) value:"a"<br>
     * (引数) prohibitedChars:"abc"<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 引数valueが1文字でそれが禁止文字の場合、falseが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNotProhibitedChar05() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.hasNotProhibitedChar("a", "abc"));
    }

    /**
     * testHasNotProhibitedChar06() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) value:"d"<br>
     * (引数) prohibitedChars:"abc"<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数valueが1文字でそれが禁止文字でない場合、trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNotProhibitedChar06() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.hasNotProhibitedChar("d", "abc"));
    }

    /**
     * testHasNotProhibitedChar07() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) value:"abc"<br>
     * (引数) prohibitedChars:"cde"<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 引数valueが複数文字でそれが禁止文字を含む場合、falseが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNotProhibitedChar07() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.hasNotProhibitedChar("abc", "cde"));
    }

    /**
     * testHasNotProhibitedChar08() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) value:"abc"<br>
     * (引数) prohibitedChars:"def"<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数valueが複数文字でそれが禁止文字を含まない場合、trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNotProhibitedChar08() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.hasNotProhibitedChar("abc", "def"));
    }

    /**
     * testHasNotProhibitedChar09() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) value:"ab\""<br>
     * (引数) prohibitedChars:"cd\""<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 引数valueにエスケープが必要な文字を含みそれが禁止文字の場合、falseが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNotProhibitedChar09() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.hasNotProhibitedChar("ab\"", "cd\""));
    }

    /**
     * testHasNotProhibitedChar10() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) value:"ab\""<br>
     * (引数) prohibitedChars:"de\\"<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数valueにエスケープが必要な文字を含みそれが禁止文字でない場合、 trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNotProhibitedChar10() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.hasNotProhibitedChar("ab\"", "cd\\"));
    }

    /**
     * testIsArrayInRange01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) obj:null<br>
     * (引数) min:1<br>
     * (引数) max:5<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 引数objがnullの場合でminが１以上の場合、falseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsArrayInRange01() throws Exception {
        // 前処理
        Object obj = null;
        int min = 1;
        int max = 5;

        // テスト実施
        boolean result = ValidationUtil.isArrayInRange(obj, min, max);

        // 判定
        assertFalse(result);
    }

    /**
     * testIsArrayInRange02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) obj:""(String)<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * 状態変化：(例外) IllegalArgumentException<br>
     * メッセージ：java.lang.String is neither Array nor Collection. <br>
     * 引数objが配列・Collection型ではない場合、 IllegalArgumentExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsArrayInRange02() throws Exception {
        // 前処理
        Object obj = "";
        int min = 0;
        int max = 0;

        // テスト実施
        try {
            ValidationUtil.isArrayInRange(obj, min, max);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("java.lang.String is neither Array nor Collection.", e
                    .getMessage());
        }
    }

    /**
     * testIsArrayInRange03() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) obj:{"a","b","c"}<br>
     * （配列）<br>
     * (引数) min:0<br>
     * (引数) max:10<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数objが配列で、範囲内の場合 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsArrayInRange03() throws Exception {
        // 前処理
        Object obj = new String[] { "a", "b", "c" };
        int min = 0;
        int max = 10;

        // テスト実施
        boolean result = ValidationUtil.isArrayInRange(obj, min, max);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsArrayInRange04() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) obj:ArrayList<br>
     * [1="a"]<br>
     * [2="b"]<br>
     * [3="c"]<br>
     * (引数) min:5<br>
     * (引数) max:10<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 引数objがコレクションで、範囲外の場合 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsArrayInRange04() throws Exception {
        // 前処理
        List<String> obj = new ArrayList<String>();
        obj.add("a");
        obj.add("b");
        obj.add("c");
        int min = 5;
        int max = 10;

        // テスト実施
        boolean result = ValidationUtil.isArrayInRange(obj, min, max);

        // 判定
        assertFalse(result);
    }

    /**
     * testIsArrayInRange05() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) obj:int[] {<br>
     * 1,2,3<br>
     * };<br>
     * (引数) min:0<br>
     * (引数) max:2<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 引数がプリミティブ配列型で、範囲外の場合 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsArrayInRange05() throws Exception {
        // 前処理
        int[] array = new int[] { 1, 2, 3 };

        // テスト実施
        boolean result = ValidationUtil.isArrayInRange(array, 0, 2);

        // 判定
        assertFalse(result);
    }

    /**
     * testIsUrl01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) value:null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数valueがnullの場合 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsUrl01() throws Exception {
        // 前処理
        String value = null;
        boolean allowallschemes = false;
        boolean allow2slashes = false;
        boolean nofragments = false;
        String schemesVar = null;

        // テスト実施
        boolean result = ValidationUtil.isUrl(value, allowallschemes,
                allow2slashes, nofragments, schemesVar);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsUrl02() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) value:""<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数valueが空白の場合 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsUrl02() throws Exception {
        // 前処理
        String value = "";
        boolean allowallschemes = false;
        boolean allow2slashes = false;
        boolean nofragments = false;
        String schemesVar = null;

        // テスト実施
        boolean result = ValidationUtil.isUrl(value, allowallschemes,
                allow2slashes, nofragments, schemesVar);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsUrl03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) value:http://www.nttdata.co.jp/index.html<br>
     * (引数) allowallschemes:false<br>
     * (引数) allow2slashes:false<br>
     * (引数) nofragments:false<br>
     * (引数) schemesVar:null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数valueが正常なURLで、オプションがfalse、schemesVarがnullの場合 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsUrl03() throws Exception {
        // 前処理
        String value = "http://www.nttdata.co.jp/index.html";
        boolean allowallschemes = false;
        boolean allow2slashes = false;
        boolean nofragments = false;
        String schemesVar = null;

        // テスト実施
        boolean result = ValidationUtil.isUrl(value, allowallschemes,
                allow2slashes, nofragments, schemesVar);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsUrl04() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) value:http://www.nttdata.co.jp<br>
     * (引数) allowallschemes:false<br>
     * (引数) allow2slashes:false<br>
     * (引数) nofragments:false<br>
     * (引数) schemesVar:null<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数valueが正常なURLで、オプションがfalse、schemesVarがnullの場合 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsUrl04() throws Exception {
        // 前処理
        String value = "http://www.nttdata.co.jp";
        boolean allowallschemes = false;
        boolean allow2slashes = false;
        boolean nofragments = false;
        String schemesVar = null;

        // テスト実施
        boolean result = ValidationUtil.isUrl(value, allowallschemes,
                allow2slashes, nofragments, schemesVar);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsUrl05() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) value:http://www.nttdata.co.jp/index.html<br>
     * (引数) allowallschemes:false<br>
     * (引数) allow2slashes:false<br>
     * (引数) nofragments:false<br>
     * (引数) schemesVar:""<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 引数valueがURLではない文字列で、オプションがfalse、schemesVarが空白の場合 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsUrl05() throws Exception {
        // 前処理
        String value = "http://www.nttdata.co.jp/index.html";
        boolean allowallschemes = false;
        boolean allow2slashes = false;
        boolean nofragments = false;
        String schemesVar = "";

        // テスト実施
        boolean result = ValidationUtil.isUrl(value, allowallschemes,
                allow2slashes, nofragments, schemesVar);

        // 判定
        assertFalse(result);
    }

    /**
     * testIsUrl06() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) value:http://www.nttdata.co.jp/<br>
     * (引数) allowallschemes:true<br>
     * (引数) allow2slashes:true<br>
     * (引数) nofragments:true<br>
     * (引数) schemesVar:"http"<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数valueが正常なURLで、オプションがtrue、schemesVarがNotNullの場合 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsUrl06() throws Exception {
        // 前処理
        String value = "http://www.nttdata.co.jp/";
        boolean allowallschemes = true;
        boolean allow2slashes = true;
        boolean nofragments = true;
        String schemesVar = "http";

        // テスト実施
        boolean result = ValidationUtil.isUrl(value, allowallschemes,
                allow2slashes, nofragments, schemesVar);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsUrl07() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) value:https://www.nttdata.co.jp/index.html<br>
     * (引数) allowallschemes:true<br>
     * (引数) allow2slashes:true<br>
     * (引数) nofragments:true<br>
     * (引数) schemesVar:"http,ftp,https"<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数valueが正常なURLで、オプションがtrue、schemesVarがNotNull(カンマ区切りの複数)の場合 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsUrl07() throws Exception {
        // 前処理
        String value = "https://www.nttdata.co.jp/index.html";
        boolean allowallschemes = true;
        boolean allow2slashes = true;
        boolean nofragments = true;
        String schemesVar = "http,ftp,https";

        // テスト実施
        boolean result = ValidationUtil.isUrl(value, allowallschemes,
                allow2slashes, nofragments, schemesVar);

        // 判定
        assertTrue(result);
    }

}
