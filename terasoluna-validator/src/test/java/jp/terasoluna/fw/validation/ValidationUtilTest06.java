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

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.terasoluna.fw.validation.PropertyTestCase;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.validation.ValidationUtil} クラスのブラックボックステスト。
 *
 * <p>
 * <h4>【クラスの概要】</h4>
 * 検証ロジックのユーティリティクラス。
 * <p>
 *
 * @see jp.terasoluna.fw.validation.ValidationUtil
 */
public class ValidationUtilTest06 extends PropertyTestCase {

    /**
     * 初期化処理を行う。
     *
     * @throws Exception このメソッドで発生した例外
     * @see jp.terasoluna.utlib.spring.PropertyTestCase#setUpData()
     */
    @Before
    public void setUpData() throws Exception {
    }

    /**
     * 終了処理を行う。
     *
     * @throws Exception このメソッドで発生した例外
     * @see jp.terasoluna.utlib.spring.PropertyTestCase#cleanUpData()
     */
    @After
    public void cleanUpData() throws Exception {
    }

    /**
     * testToDate01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:null<br>
     *
     * <br>
     * 期待値：(戻り値) Date:null<br>
     *
     * <br>
     * 引数valueがnullの場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToDate01() throws Exception {
        // 前処理
        String value = null;
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = null;

        // テスト実施
        Date result = ValidationUtil.toDate(
                value, datePattern, datePatternStrict);

        // 判定
        assertNull(result);
    }

    /**
     * testToDate02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:""<br>
     *
     * <br>
     * 期待値：(戻り値) Date:null<br>
     *
     * <br>
     * 引数valueが空白の場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToDate02() throws Exception {
        // 前処理
        String value = "";
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = null;

        // テスト実施
        Date result = ValidationUtil.toDate(
                value, datePattern, datePatternStrict);

        // 判定
        assertNull(result);
    }

    /**
     * testToDate03()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) datePattern:null<br>
     *         (引数) datePatternStrict:null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ："datePattern or datePatternStrict must be specified."<br>
     *
     * <br>
     * 引数datePattern、datePatternStrictがnullの場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToDate03() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String datePattern = null;
        String datePatternStrict = null;

        // テスト実施
        try {
            ValidationUtil.toDate(value, datePattern, datePatternStrict);
            fail("例外が発生しない。");
        } catch (Exception e) {
            //判定
            assertEquals("datePattern or datePatternStrict must be specified.",
                    e.getMessage());
        }
    }

    /**
     * testToDate04()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) datePattern:""<br>
     *         (引数) datePatternStrict:""<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ："datePattern or datePatternStrict must be specified."<br>
     *
     * <br>
     * 引数datePattern、datePatternStrictが空白の場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToDate04() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String datePattern = "";
        String datePatternStrict = "";

        // テスト実施
        try {
            ValidationUtil.toDate(value, datePattern, datePatternStrict);
            fail("例外が発生しない。");
        } catch (Exception e) {
            //判定
            assertEquals("datePattern or datePatternStrict must be specified.",
                    e.getMessage());
        }
    }

    /**
     * testToDate05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) datePattern:"yyyy/MM/dd"<br>
     *         (引数) datePatternStrict:"yyyy.MM.dd"<br>
     *
     * <br>
     * 期待値：(戻り値) Date:"2005/11/1"のDate型<br>
     *
     * <br>
     * 引数datePattern,datePatternStrictがNotNullで正常にDateに変換できる場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToDate05() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = "yyyy.MM.dd";

        // テスト実施
        Date result = ValidationUtil.toDate(
                value, datePattern, datePatternStrict);

        // 判定
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        Date hope = format.parse(value);
        assertEquals(hope, result);
        assertEquals("2005/11/01", format.format(result));
    }

    /**
     * testToDate06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) datePattern:null<br>
     *         (引数) datePatternStrict:"yyyy/MM/dd"<br>
     *
     * <br>
     * 期待値：(戻り値) Date:null<br>
     *
     * <br>
     * 引数datePatternStrictがNotNullでDateに変換できず、nullとなる場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToDate06() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String datePattern = null;
        String datePatternStrict = "yyyy/MM/dd";

        // テスト実施
        Date result = ValidationUtil.toDate(
                value, datePattern, datePatternStrict);

        // 判定
        assertNull(result);
    }

    /**
     * testToDate07()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"2005/11/24"<br>
     *         (引数) datePattern:"yyyy/MM/dd"<br>
     *
     * <br>
     * 期待値：(戻り値) Date:"2005/11/24"のDate型<br>
     *
     * <br>
     * 引数datePatternがNotNullで正常にDateに変換できる場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToDate07() throws Exception {
        // 前処理
        String value = "2005/11/24";
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = null;

        // テスト実施
        Date result = ValidationUtil.toDate(
                value, datePattern, datePatternStrict);

        // 判定
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        Date hope = format.parse(value);
        assertEquals(hope, result);
        assertEquals(value, format.format(result));
    }

    /**
     * testToDate08()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"2005/11/24"<br>
     *         (引数) datePattern:null<br>
     *         (引数) datePatternStrict:"yyyy/MM/dd"<br>
     *
     * <br>
     * 期待値：(戻り値) Date:"2005/11/24"のDate型<br>
     *
     * <br>
     * 引数datePatternStrictがNotNullで正常にDateに変換できる場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToDate08() throws Exception {
        // 前処理
        String value = "2005/11/24";
        String datePattern = null;
        String datePatternStrict = "yyyy/MM/dd";

        // テスト実施
        Date result = ValidationUtil.toDate(
                value, datePattern, datePatternStrict);

        // 判定
        SimpleDateFormat format = new SimpleDateFormat(datePatternStrict);
        Date hope = format.parse(value);
        assertEquals(hope, result);
        assertEquals(value, format.format(result));
    }

    /**
     * testToDate09()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) value:"2005/11/24"<br>
     *         (引数) datePattern:"asdf"<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:例外：IllegalArgumentException<br>
     *                    メッセージ：Illegal pattern character 'f'<br>
     *
     * <br>
     * datePatternに無効な文字列がある場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToDate09() throws Exception {
        // 前処理
        String value = "2005/11/24";
        String datePattern = "asdf";
        String datePatternStrict = null;

        // テスト実施
        try {
            ValidationUtil.toDate(value, datePattern, datePatternStrict);
            fail("例外が発生しない。");
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal pattern character 'f'", e.getMessage());
        }
    }

    /**
     * testToDate10()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) value:"2005/11/24"<br>
     *         (引数) datePattern:null<br>
     *         (引数) datePatternStrict:"asdf"<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:例外：IllegalArgumentException<br>
     *                    メッセージ：Illegal pattern character 'f'<br>
     *
     * <br>
     * datePatternStrictに無効な文字列がある場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToDate10() throws Exception {
        // 前処理
        String value = "2005/11/24";
        String datePattern = null;
        String datePatternStrict = "asdf";

        // テスト実施
        try {
            ValidationUtil.toDate(value, datePattern, datePatternStrict);
            fail("例外が発生しない。");
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal pattern character 'f'", e.getMessage());
        }
    }

    /**
     * testToDate11()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"2005/2/29"<br>
     *         (引数) datePattern:"yyyy/MM/dd"<br>
     *
     * <br>
     * 期待値：(戻り値) Date:null<br>
     *
     * <br>
     * 入力値が存在しない日付の場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToDate11() throws Exception {
        // 前処理
        String value = "2005/2/29";
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = null;

        // テスト実施
        Date result = ValidationUtil.toDate(
                value, datePattern, datePatternStrict);

        // 判定
        assertNull(result);
    }

    /**
     * testToDate12()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"2005/02/29"<br>
     *         (引数) datePattern:null<br>
     *         (引数) datePatternStrict:"yyyy/MM/dd"<br>
     *
     * <br>
     * 期待値：(戻り値) Date:null<br>
     *
     * <br>
     * 入力値が存在しない日付の場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToDate12() throws Exception {
        // 前処理
        String value = "2005/02/29";
        String datePattern = null;
        String datePatternStrict = "yyyy/MM/dd";

        // テスト実施
        Date result = ValidationUtil.toDate(
                value, datePattern, datePatternStrict);

        // 判定
        assertNull(result);
    }

    /**
     * testToDate13()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"test"<br>
     *         (引数) datePattern:"yyyy/MM/dd"<br>
     *
     * <br>
     * 期待値：(戻り値) Date:null<br>
     *
     * <br>
     * 入力値が日付に変換できない場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testToDate13() throws Exception {
        // 前処理
        String value = "test";
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = null;

        // テスト実施
        Date result = ValidationUtil.toDate(
                value, datePattern, datePatternStrict);

        // 判定
        assertNull(result);
    }

}
