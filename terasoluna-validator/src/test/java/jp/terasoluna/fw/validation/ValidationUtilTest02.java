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

import java.math.BigDecimal;

import jp.terasoluna.utlib.PropertyTestCase;
import jp.terasoluna.utlib.UTUtil;

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
public class ValidationUtilTest02 extends PropertyTestCase {

    /**
     * このテストケースを実行する為の
     * GUI アプリケーションを起動する。
     *
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(ValidationUtilTest02.class);
    }

    /**
     * 初期化処理を行う。
     *
     * @throws Exception このメソッドで発生した例外
     * @see jp.terasoluna.utlib.spring.PropertyTestCase#setUpData()
     */
    @Override
    protected void setUpData() throws Exception {
        UTUtil.setPrivateField(ValidationUtil.class, "hankakuKanaList",
        "ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣");
    UTUtil.setPrivateField(ValidationUtil.class, "zenkakuKanaList",
        "アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソ" +
        "ザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホ" +
        "バビブベボパピプペポマミムメモヤユヨャュョラリルレロ" +
        "ワヮヰヱヲッンー");
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
    public ValidationUtilTest02(String name) {
        super(name);
    }

    /**
     * testMatchRegexp01()
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
     * 引数valueがnullの場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testMatchRegexp01() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.matchRegexp(null, "^([0-9])*$"));
    }

    /**
     * testMatchRegexp02()
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
     * 引数valueが空文字の場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testMatchRegexp02() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.matchRegexp("", "^([0-9])*$"));
    }

    /**
     * testMatchRegexp03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"123"<br>
     *         (引数) mask:"^([0-9])*$"<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが引数maskの正規表現に該当する場合、trueが取得できることを確認する。<br>
     * ※チェック処理はGenericValidator.matchRegexp(String, String)が行なうため、各正規表現に対する詳細な確認は行なわない
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testMatchRegexp03() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.matchRegexp("123", "^([0-9])*$"));
    }

    /**
     * testMatchRegexp04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"a12"<br>
     *         (引数) mask:"^([0-9])*$"<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueが引数maskの正規表現に該当しない場合、trueが取得できることを確認する。<br>
     * ※チェック処理はGenericValidator.matchRegexp(String, String)が行なうため、各正規表現に対する詳細な確認は行なわない
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testMatchRegexp04() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.matchRegexp("a12", "^([0-9])*$"));
    }

    /**
     * testIsAlphaNumericString01()
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
     * 引数valueがnullの場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsAlphaNumericString01() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isAlphaNumericString(null));
    }

    /**
     * testIsAlphaNumericString02()
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
     * 引数valueが空文字の場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsAlphaNumericString02() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isAlphaNumericString(""));
    }

    /**
     * testIsAlphaNumericString03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"0aA"<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが半角英数字のみで構成される場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsAlphaNumericString03() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isAlphaNumericString("0aA"));
    }

    /**
     * testIsAlphaNumericString04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"Zg3%"<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueが半角英数字以外を含む場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsAlphaNumericString04() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isAlphaNumericString("Zg3%"));
    }

    /**
     * testIsUpperAlphaNumericString01()
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
     * 引数valueがnullの場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsUpperAlphaNumericString01() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isUpperAlphaNumericString(null));
    }

    /**
     * testIsUpperAlphaNumericString02()
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
     * 引数valueが空文字の場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsUpperAlphaNumericString02() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isUpperAlphaNumericString(""));
    }

    /**
     * testIsUpperAlphaNumericString03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"A0"<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが大文字半角英数字のみで構成される場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsUpperAlphaNumericString03() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isUpperAlphaNumericString("A0"));
    }

    /**
     * testIsUpperAlphaNumericString04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"Aa0"<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueが大文字半角英数字以外を含む場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsUpperAlphaNumericString04() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isUpperAlphaNumericString("Aa0"));
    }

    /**
     * testIsNumericString01()
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
     * 引数valueがnullの場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNumericString01() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isNumericString(null));
    }

    /**
     * testIsNumericString02()
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
     * 引数valueが空文字の場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNumericString02() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isNumericString(""));
    }

    /**
     * testIsNumericString03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"9876"<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが数字のみで構成される場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNumericString03() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isNumericString("9876"));
    }

    /**
     * testIsNumericString04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"Aa0"<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueが数字以外を含む場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNumericString04() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isNumericString("Aa0"));
    }

    /**
     * testIsNumber01()
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
     * valueがnullの場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNumber01() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isNumber(null, 3, false, 3, false));
    }

    /**
     * testIsNumber02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:BigDecimal("123.45")<br>
     *         (引数) integerLength:1<br>
     *         (引数) isAccordedInteger:false<br>
     *         (引数) scaleLength:3<br>
     *         (引数) isAccordedScale:false<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * isAccordedIntegerがfalseで、整数部桁数がintegerLengthより大きい場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNumber02() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isNumber(new BigDecimal("123.45"), 1, false,
                3, false));
    }

    /**
     * testIsNumber03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:BigDecimal("123.45")<br>
     *         (引数) integerLength:5<br>
     *         (引数) isAccordedInteger:true<br>
     *         (引数) scaleLength:3<br>
     *         (引数) isAccordedScale:false<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * isAccordedIntegerがtrueで、整数部桁数がintegerLengthと等しくない場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNumber03() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isNumber(new BigDecimal("123.45"), 5, true,
                3, false));
    }

    /**
     * testIsNumber04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:BigDecimal("123.45")<br>
     *         (引数) integerLength:5<br>
     *         (引数) isAccordedInteger:false<br>
     *         (引数) scaleLength:1<br>
     *         (引数) isAccordedScale:false<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 整数部桁数は条件を満たし、AccordedScaleがfalseで、小数部桁数がscaleLengthより大きい場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNumber04() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isNumber(new BigDecimal("123.45"), 5, false,
                1, false));
    }

    /**
     * testIsNumber05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:BigDecimal("123.45")<br>
     *         (引数) integerLength:3<br>
     *         (引数) isAccordedInteger:true<br>
     *         (引数) scaleLength:3<br>
     *         (引数) isAccordedScale:true<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 整数部桁数は条件を満たし、isAccordedScaleがtrueで、小数部桁数がscaleLengthと等しくない場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNumber05() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isNumber(new BigDecimal("123.45"), 3, true,
                3, true));
    }

    /**
     * testIsNumber06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:BigDecimal("123.45")<br>
     *         (引数) integerLength:5<br>
     *         (引数) isAccordedInteger:false<br>
     *         (引数) scaleLength:3<br>
     *         (引数) isAccordedScale:false<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * isAccordedInteger・scaleLengthがfalseの場合、整数部桁数・小数部桁数がintegerLength・scaleLengthより小さければtrueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNumber06() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isNumber(new BigDecimal("123.45"), 5, false,
                3, false));
    }

    /**
     * testIsNumber07()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:BigDecimal("123.45")<br>
     *         (引数) integerLength:3<br>
     *         (引数) isAccordedInteger:true<br>
     *         (引数) scaleLength:2<br>
     *         (引数) isAccordedScale:true<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * isAccordedInteger・scaleLengthがtrueの場合、整数部桁数・小数部桁数がintegerLength・scaleLengthと等しければtrueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNumber07() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isNumber(new BigDecimal("123.45"), 3, true,
                2, true));
    }
    /**
     * testIsNumber08()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:BigDecimal("123.45")<br>
     *         (引数) integerLength:3<br>
     *         (引数) isAccordedInteger:false<br>
     *         (引数) scaleLength:2<br>
     *         (引数) isAccordedScale:false<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * isAccordedInteger・scaleLengthがfalseの場合、整数部桁数・小数部桁数がintegerLength・scaleLengthと等しければtrueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNumber08() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isNumber(new BigDecimal("123.45"), 3, false,
                2, false));
    }

    /**
     * testIsNumber09()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:BigDecimal("123.00000")<br>
     *         (引数) integerLength:3<br>
     *         (引数) isAccordedInteger:false<br>
     *         (引数) scaleLength:2<br>
     *         (引数) isAccordedScale:false<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 整数部桁数は条件を満たし、AccordedScaleがfalseで、小数部桁数がscaleLengthより大きい場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsNumber09() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isNumber(new BigDecimal("123.00000"), 3,
                false, 2, false));
    }


}
