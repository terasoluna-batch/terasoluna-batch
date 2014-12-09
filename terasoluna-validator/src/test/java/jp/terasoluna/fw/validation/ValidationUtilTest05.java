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

import jp.terasoluna.utlib.PropertyTestCase;

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
public class ValidationUtilTest05 extends PropertyTestCase {

    /**
     * このテストケースを実行する為の
     * GUI アプリケーションを起動する。
     *
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(ValidationUtilTest05.class);
    }

    /**
     * 初期化処理を行う。
     *
     * @throws Exception このメソッドで発生した例外
     * @see jp.terasoluna.utlib.spring.PropertyTestCase#setUpData()
     */
    @Override
    protected void setUpData() throws Exception {
    }

    /**
     * 終了処理を行う。
     *
     * @throws Exception このメソッドで発生した例外
     * @see jp.terasoluna.utlib.spring.PropertyTestCase#cleanUpData()
     */
    @Override
    protected void cleanUpData() throws Exception {
    }

    /**
     * コンストラクタ。
     *
     * @param name このテストケースの名前。
     */
    public ValidationUtilTest05(String name) {
        super(name);
    }

    /**
     * testIsByteInRange01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueがnullの場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsByteInRange01() throws Exception {
        // 前処理
        String value = null;
        String encoding = null;
        int min = 0;
        int max = 0;

        // テスト実施
        boolean result = ValidationUtil.isByteInRange(
                value, encoding, min, max);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsByteInRange02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:""<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが空白の場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsByteInRange02() throws Exception {
        // 前処理
        String value = "";
        String encoding = null;
        int min = 0;
        int max = 0;

        // テスト実施
        boolean result = ValidationUtil.isByteInRange(
                value, encoding, min, max);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsByteInRange03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:"abc"<br>
     *         (引数) encoding:null<br>
     *         (引数) min:0<br>
     *         (引数) max:10<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueがNotNull、encodingがnullで、正常に長さがとれ、指定範囲内の場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsByteInRange03() throws Exception {
        // 前処理
        String value = "abc";
        String encoding = null;
        int min = 0;
        int max = 10;

        // テスト実施
        boolean result = ValidationUtil.isByteInRange(
                value, encoding, min, max);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsByteInRange04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:"abc"<br>
     *         (引数) encoding:""<br>
     *         (引数) min:5<br>
     *         (引数) max:10<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueがNotNull、encodingが空白で、正常に長さがとれ、指定範囲外の場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsByteInRange04() throws Exception {
        // 前処理
        String value = "abc";
        String encoding = "";
        int min = 5;
        int max = 10;

        // テスト実施
        boolean result = ValidationUtil.isByteInRange(
                value, encoding, min, max);

        // 判定
        assertFalse(result);
    }

    /**
     * testIsByteInRange05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:"abc"<br>
     *         (引数) encoding:"UTF-8"<br>
     *         (引数) min:3<br>
     *         (引数) max:3<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueがNotNull、encodingがNotNullで、正常に長さがとれ、指定範囲内の場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsByteInRange05() throws Exception {
        // 前処理
        String value = "abc";
        String encoding = "UTF-8";
        int min = 3;
        int max = 3;

        // テスト実施
        boolean result = ValidationUtil.isByteInRange(
                value, encoding, min, max);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsByteInRange06()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) value:"abc"<br>
     *         (引数) encoding:"aaa"<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:例外：IllegalArgumentException<br>
     *
     * <br>
     * 引数valueがNotNull、encodingが不正な文字列の場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsByteInRange06() throws Exception {
        // 前処理
        String value = "abc";
        String encoding = "aaa";
        int min = 5;
        int max = 10;

        // テスト実施
        try {
            ValidationUtil.isByteInRange(
                    value, encoding, min, max);
            fail("例外が発生しない。");
        } catch (IllegalArgumentException e) {
            // 判定
            //例外が発生すればOK。
            assertNotNull(e);
        }
    }

    /**
     * testIsDateInRange01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueがnullの場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange01() throws Exception {
        // 前処理
        String value = null;
        String startDateStr = null;
        String endDateStr = null;
        String datePattern = null;
        String datePatternStrict = null;

        // テスト実施
        boolean result = ValidationUtil.isDateInRange(value, startDateStr,
                endDateStr,datePattern, datePatternStrict);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsDateInRange02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:""<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが空白の場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange02() throws Exception {
        // 前処理
        String value = "";
        String startDateStr = null;
        String endDateStr = null;
        String datePattern = null;
        String datePatternStrict = null;

        // テスト実施
        boolean result = ValidationUtil.isDateInRange(value, startDateStr,
                endDateStr,datePattern, datePatternStrict);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsDateInRange03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) datePattern:null<br>
     *         (引数) datePatternStrict:"yyyy/MM/dd"<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueが正常に日付に変換できない場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange03() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String startDateStr = null;
        String endDateStr = null;
        String datePattern = null;
        String datePatternStrict = "yyyy/MM/dd";

        // テスト実施
        boolean result = ValidationUtil.isDateInRange(value, startDateStr,
                endDateStr,datePattern, datePatternStrict);

        // 判定
        assertFalse(result);
    }

    /**
     * testIsDateInRange04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:"2005/11/01"<br>
     *         (引数) startDateStr:null<br>
     *         (引数) endDateStr:null<br>
     *         (引数) datePattern:null<br>
     *         (引数) datePatternStrict:"yyyy/MM/dd"<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数startDateStr、endDateStrがnullの場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange04() throws Exception {
        // 前処理
        String value = "2005/11/01";
        String startDateStr = null;
        String endDateStr = null;
        String datePattern = null;
        String datePatternStrict = "yyyy/MM/dd";

        // テスト実施
        boolean result = ValidationUtil.isDateInRange(value, startDateStr,
                endDateStr,datePattern, datePatternStrict);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsDateInRange05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) startDateStr:""<br>
     *         (引数) endDateStr:""<br>
     *         (引数) datePattern:"yyyy/MM/dd"<br>
     *         (引数) datePatternStrict:null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数startDateStr、endDateStrが空白の場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange05() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String startDateStr = "";
        String endDateStr = "";
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = null;

        // テスト実施
        boolean result = ValidationUtil.isDateInRange(value, startDateStr,
                endDateStr,datePattern, datePatternStrict);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsDateInRange06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:"2005/11/01"<br>
     *         (引数) startDateStr:"2005/12/1"<br>
     *         (引数) endDateStr:null<br>
     *         (引数) datePattern:null<br>
     *         (引数) datePatternStrict:"yyyy/MM/dd"<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:-<br>
     *         (状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ："startDate is unparseable[2005/12/1]"<br>
     *
     * <br>
     * 引数startDateStrがNotNullだが、日付に変換できない場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange06() throws Exception {
        // 前処理
        String value = "2005/11/01";
        String startDateStr = "2005/12/1";
        String endDateStr = null;
        String datePattern = null;
        String datePatternStrict = "yyyy/MM/dd";

        // テスト実施
        try {
            ValidationUtil.isDateInRange(value, startDateStr,
                    endDateStr,datePattern, datePatternStrict);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("startDate is unparseable[2005/12/1]", e.getMessage());
        }
    }

    /**
     * testIsDateInRange07()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) startDateStr:"2005/12/1"<br>
     *         (引数) endDateStr:null<br>
     *         (引数) datePattern:"yyyy/MM/dd"<br>
     *         (引数) datePatternStrict:null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数startDateStrが正常に日付に変換できだが、valueがstartDateStrより以前だった場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange07() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String startDateStr = "2005/12/1";
        String endDateStr = null;
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = null;

        // テスト実施
        boolean result = ValidationUtil.isDateInRange(value, startDateStr,
                endDateStr,datePattern, datePatternStrict);

        // 判定
        assertFalse(result);
    }

    /**
     * testIsDateInRange08()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:"2005/11/01"<br>
     *         (引数) startDateStr:null<br>
     *         (引数) endDateStr:"2005/10/1"<br>
     *         (引数) datePattern:null<br>
     *         (引数) datePatternStrict:"yyyy/MM/dd"<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:-<br>
     *         (状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ："endDate is unparseable[2005/10/1]"<br>
     *
     * <br>
     * 引数endDateStrがNotNullだが、日付に変換できない場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange08() throws Exception {
        // 前処理
        String value = "2005/11/01";
        String startDateStr = null;
        String endDateStr = "2005/10/1";
        String datePattern = null;
        String datePatternStrict = "yyyy/MM/dd";

        // テスト実施
        try {
            ValidationUtil.isDateInRange(value, startDateStr,
                    endDateStr,datePattern, datePatternStrict);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("endDate is unparseable[2005/10/1]", e.getMessage());
        }
    }

    /**
     * testIsDateInRange09()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) startDateStr:null<br>
     *         (引数) endDateStr:"2005/10/1"<br>
     *         (引数) datePattern:"yyyy/MM/dd"<br>
     *         (引数) datePatternStrict:null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数endDateStrが正常に日付に変換できだが、valueがendDateStrより以後だった場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange09() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String startDateStr = null;
        String endDateStr = "2005/10/1";
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = null;

        // テスト実施
        boolean result = ValidationUtil.isDateInRange(value, startDateStr,
                endDateStr,datePattern, datePatternStrict);

        // 判定
        assertFalse(result);
    }

    /**
     * testIsDateInRange10()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) startDateStr:"2005/10/1"<br>
     *         (引数) endDateStr:"2005/12/1"<br>
     *         (引数) datePattern:"yyyy/MM/dd"<br>
     *         (引数) datePatternStrict:null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数value、startDateStr、endDateStrすべて正常に日付に変換でき、valueがstartDateStrとendDateStrの間の日付の場合
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange10() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String startDateStr = "2005/10/1";
        String endDateStr = "2005/12/1";
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = null;

        // テスト実施
        boolean result = ValidationUtil.isDateInRange(value, startDateStr,
                endDateStr,datePattern, datePatternStrict);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsDateInRange11()
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
     * datePattern、datePatternStrictがnullの場合、IllegalArgumentExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange11() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String startDateStr = "2005/10/1";
        String endDateStr = "2005/12/1";
        String datePattern = null;
        String datePatternStrict = null;

        // テスト実施
        try {
            ValidationUtil.isDateInRange(value, startDateStr,
                    endDateStr,datePattern, datePatternStrict);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("datePattern or datePatternStrict must be specified.",
                    e.getMessage());
        }
    }

    /**
     * testIsDateInRange12()
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
     * datePattern、datePatternStrictが空文字の場合、IllegalArgumentExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange12() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String startDateStr = "2005/10/1";
        String endDateStr = "2005/12/1";
        String datePattern = "";
        String datePatternStrict = "";

        // テスト実施
        try {
            ValidationUtil.isDateInRange(value, startDateStr,
                    endDateStr,datePattern, datePatternStrict);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("datePattern or datePatternStrict must be specified.",
                    e.getMessage());
        }
    }

    /**
     * testIsDateInRange13()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) datePattern:"abc"<br>
     *         (引数) datePatternStrict:null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ：<br>
     *                    "Illegal pattern character 'b'"<br>
     *
     * <br>
     * datePatternが不正な場合、IllegalArgumentExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange13() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String startDateStr = "2005/10/1";
        String endDateStr = "2005/12/1";
        String datePattern = "abc";
        String datePatternStrict = null;

        // テスト実施
        try {
            ValidationUtil.isDateInRange(value, startDateStr,
                    endDateStr,datePattern, datePatternStrict);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal pattern character 'b'",
                    e.getMessage());
        }
    }

    /**
     * testIsDateInRange14()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) datePattern:null<br>
     *         (引数) datePatternStrict:"abc"<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ：<br>
     *                    "Illegal pattern character 'b'"<br>
     *
     * <br>
     * datePatternStrictが不正な場合、IllegalArgumentExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange14() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String startDateStr = "2005/10/1";
        String endDateStr = "2005/12/1";
        String datePattern = null;
        String datePatternStrict = "abc";

        // テスト実施
        try {
            ValidationUtil.isDateInRange(value, startDateStr,
                    endDateStr,datePattern, datePatternStrict);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal pattern character 'b'",
                    e.getMessage());
        }
    }

    /**
     * testIsDateInRange15()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) startDateStr:"test"<br>
     *         (引数) datePattern:"yyyy/MM/dd"<br>
     *         (引数) datePatternStrict:null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ："startDate is unparseable[test]"<br>
     *
     * <br>
     * startDateStrが日付に変換できない場合、IllegalArgumentExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange15() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String startDateStr = "test";
        String endDateStr = null;
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = null;

        // テスト実施
        try {
            ValidationUtil.isDateInRange(value, startDateStr,
                    endDateStr,datePattern, datePatternStrict);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("startDate is unparseable[test]",
                    e.getMessage());
        }
    }

    /**
     * testIsDateInRange16()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) endDateStr:"test"<br>
     *         (引数) datePattern:"yyyy/MM/dd"<br>
     *         (引数) datePatternStrict:null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    メッセージ："endDate is unparseable[test]"<br>
     *
     * <br>
     * endDateStrが日付に変換できない場合、IllegalArgumentExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange16() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String startDateStr = null;
        String endDateStr = "test";
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = null;

        // テスト実施
        try {
            ValidationUtil.isDateInRange(value, startDateStr,
                    endDateStr,datePattern, datePatternStrict);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("endDate is unparseable[test]",
                    e.getMessage());
        }
    }

    /**
     * testIsDateInRange17()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：F,G
     * <br><br>
     * 入力値：(引数) value:"2005/11/1"<br>
     *         (引数) startDateStr:"2005/11/1"<br>
     *         (引数) endDateStr:"2005/11/1"<br>
     *         (引数) datePattern:"yyyy/MM/dd"<br>
     *         (引数) datePatternStrict:null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 入力値とstartDateStr、endDateStrの日付が等しい場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange17() throws Exception {
        // 前処理
        String value = "2005/11/1";
        String startDateStr = "2005/11/1";
        String endDateStr = "2005/11/1";
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = null;

        // テスト実施
        boolean result = ValidationUtil.isDateInRange(value, startDateStr,
                    endDateStr,datePattern, datePatternStrict);

        // 判定
        assertTrue(result);
    }

    /**
     * testIsDateInRange18()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"test"<br>
     *         (引数) startDateStr:"2005/1/1"<br>
     *         (引数) endDateStr:"2005/12/31"<br>
     *         (引数) datePattern:"yyyy/MM/dd"<br>
     *         (引数) datePatternStrict:null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 入力値が日付に変換できない場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsDateInRange18() throws Exception {
        // 前処理
        String value = "test";
        String startDateStr = "2005/11/1";
        String endDateStr = "2005/12/31";
        String datePattern = "yyyy/MM/dd";
        String datePatternStrict = null;

        // テスト実施
        boolean result = ValidationUtil.isDateInRange(value, startDateStr,
                    endDateStr,datePattern, datePatternStrict);

        // 判定
        assertFalse(result);
    }

}
