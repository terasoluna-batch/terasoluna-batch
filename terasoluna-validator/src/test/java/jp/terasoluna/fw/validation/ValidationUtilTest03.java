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
public class ValidationUtilTest03 extends PropertyTestCase {

    /**
     * このテストケースを実行する為の
     * GUI アプリケーションを起動する。
     *
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(ValidationUtilTest03.class);
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
    public ValidationUtilTest03(String name) {
        super(name);
    }

    /**
     * testCheckNumberFigures01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) length:3<br>
     *         (引数) checkLength:0<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * lengthがcheckLengthより大きい場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testCheckNumberFigures01() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.checkNumberFigures(3, 0, false));
    }

    /**
     * testCheckNumberFigures02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) length:0<br>
     *         (引数) checkLength:3<br>
     *         (引数) isAccorded:false<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * isAccordedがfalseの場合、lengthがcheckLength以下であれば、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testCheckNumberFigures02() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.checkNumberFigures(0, 3, false));
    }

    /**
     * testCheckNumberFigures03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) length:3<br>
     *         (引数) checkLength:3<br>
     *         (引数) isAccorded:false<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * isAccordedがfalseの場合、lengthがcheckLength以下であれば、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testCheckNumberFigures03() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.checkNumberFigures(3, 3, false));
    }

    /**
     * testCheckNumberFigures04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) length:3<br>
     *         (引数) checkLength:3<br>
     *         (引数) isAccorded:true<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * isAccordedがtrueの場合、lengthがcheckLengthと等しければ、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testCheckNumberFigures04() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.checkNumberFigures(3, 3, true));
    }

    /**
     * testCheckNumberFigures05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) length:0<br>
     *         (引数) checkLength:3<br>
     *         (引数) isAccorded:true<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * isAccordedがtrueの場合、lengthがcheckLengthと等しくなければ、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testCheckNumberFigures05() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.checkNumberFigures(0, 3, true));
    }

    /**
     * testIsHankakuKanaString01()
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
    public void testIsHankakuKanaString01() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isHankakuKanaString(null));
    }

    /**
     * testIsHankakuKanaString02()
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
    public void testIsHankakuKanaString02() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isHankakuKanaString(""));
    }

    /**
     * testIsHankakuKanaString03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"ア"<br>
     *         (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueが半角カナ文字でない場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsHankakuKanaString03() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isHankakuKanaString("ア"));
    }

    /**
     * testIsHankakuKanaString04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"ｱ"<br>
     *         (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが半角カナ文字の場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsHankakuKanaString04() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isHankakuKanaString("ｱ"));
    }

    /**
     * testIsHankakuKanaString05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"ｱｲA"<br>
     *         (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueが複数文字で半角カナ文字以外が含まれる場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsHankakuKanaString05() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isHankakuKanaString("ｱｲA"));
    }

    /**
     * testIsHankakuKanaString06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"ｱｲｳ"<br>
     *         (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが複数文字で半角カナ文字のみで構成されている場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsHankakuKanaString06() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isHankakuKanaString("ｱｲｳ"));
    }

    /**
     * testIsHankakuString01()
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
    public void testIsHankakuString01() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isHankakuString(null));
    }

    /**
     * testIsHankakuString02()
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
    public void testIsHankakuString02() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isHankakuString(""));
    }

    /**
     * testIsHankakuString03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"あ"<br>
     *         (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueが半角文字でない場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsHankakuString03() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isHankakuString("あ"));
    }

    /**
     * testIsHankakuString04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"a"<br>
     *         (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが半角文字の場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsHankakuString04() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isHankakuString("a"));
    }

    /**
     * testIsHankakuString05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"abあ"<br>
     *         (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueが複数文字で半角文字以外が含まれる場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsHankakuString05() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isHankakuString("abあ"));
    }

    /**
     * testIsHankakuString06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"1aｱ"<br>
     *         (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが複数文字で半角文字のみで構成されている場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsHankakuString06() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isHankakuString("1aｱ"));
    }

    /**
     * testIsZenkakuString01()
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
    public void testIsZenkakuString01() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isZenkakuString(null));
    }

    /**
     * testIsZenkakuString02()
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
    public void testIsZenkakuString02() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isZenkakuString(""));
    }

    /**
     * testIsZenkakuString03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"a"<br>
     *         (状態) zenkakuKanaList:アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueが全角文字でない場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsZenkakuString03() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isZenkakuString("a"));
    }

    /**
     * testIsZenkakuString04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"あ"<br>
     *         (状態) zenkakuKanaList:アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが全角文字の場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsZenkakuString04() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isZenkakuString("あ"));
    }

    /**
     * testIsZenkakuString05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"Ａあｲ"<br>
     *         (状態) zenkakuKanaList:アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueが複数文字で全角文字以外が含まれる場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsZenkakuString05() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isZenkakuString("Ａあｲ"));
    }

    /**
     * testIsZenkakuString06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"Ａあ全角"<br>
     *         (状態) zenkakuKanaList:アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが複数文字で全角文字のみで構成されている場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsZenkakuString06() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isZenkakuString("Ａあ全角"));
    }

    /**
     * testIsZenkakuKanaString01()
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
    public void testIsZenkakuKanaString01() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isZenkakuKanaString(null));
    }

    /**
     * testIsZenkakuKanaString02()
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
    public void testIsZenkakuKanaString02() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isZenkakuKanaString(""));
    }

    /**
     * testIsZenkakuKanaString03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"A"<br>
     *         (状態) isZenkakuKanaChar(char):アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueが全角カナ文字でない場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsZenkakuKanaString03() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isZenkakuKanaString("A"));
    }

    /**
     * testIsZenkakuKanaString04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"ア"<br>
     *         (状態) isZenkakuKanaChar(char):アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが全角カナ文字の場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsZenkakuKanaString04() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isZenkakuKanaString("ア"));
    }

    /**
     * testIsZenkakuKanaString05()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"アイｳ"<br>
     *         (状態) isZenkakuKanaChar(char):アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *
     * <br>
     * 引数valueが複数文字で全角カナ文字以外が含まれる場合、falseが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsZenkakuKanaString05() throws Exception {
        // テスト実施・判定
        assertFalse(ValidationUtil.isZenkakuKanaString("アイｳ"));
    }

    /**
     * testIsZenkakuKanaString06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：F
     * <br><br>
     * 入力値：(引数) value:"アイウ"<br>
     *         (状態) isZenkakuKanaChar(char):アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *
     * <br>
     * 引数valueが複数文字で全角カナ文字のみで構成されている場合、trueが取得できることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsZenkakuKanaString06() throws Exception {
        // テスト実施・判定
        assertTrue(ValidationUtil.isZenkakuKanaString("アイウ"));
    }

}
