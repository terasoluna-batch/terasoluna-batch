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
public class ValidationUtilTest01 extends PropertyTestCase {

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     */
    @Before
    public void setUpData() throws Exception {
        UTUtil.setPrivateField(ValidationUtil.class, "hankakuKanaList",
                "ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣");
        UTUtil.setPrivateField(ValidationUtil.class, "zenkakuKanaList",
                "アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソ" + "ザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホ"
                        + "バビブベボパピプペポマミムメモヤユヨャュョラリルレロ" + "ワヮヰヱヲッンー");
    }

    /**
     * 終了処理を行う。
     * @throws Exception このメソッドで発生した例外
     */
    @After
    public void cleanUpData() throws Exception {
    }

    /**
     * testSetHankakuKanaList01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.listが存在しないこと。<br>
     * <br>
     * 期待値：(状態変化) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * <br>
     * validation.hankaku.kana.listに値が未設定の場合、hankakuKanaListはデフォルトのままであること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetHankakuKanaList01() throws Exception {
        // テスト実施
        ValidationUtil.setHankakuKanaList();

        // 判定
        assertEquals(
                "ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣",
                UTUtil.getPrivateField(ValidationUtil.class,
                        "hankakuKanaList"));
    }

    /**
     * testSetHankakuKanaList02() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.list=ｱ<br>
     * <br>
     * 期待値：(状態変化) hankakuKanaList:ｱ<br>
     * <br>
     * validation.hankaku.kana.listに値が設定されていた場合、hankakuKanaListは設定値となること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetHankakuKanaList02() throws Exception {
        // 前処理
        addProperty("validation.hankaku.kana.list", "ｱ");

        // テスト実施
        ValidationUtil.setHankakuKanaList();

        // 判定
        assertEquals("ｱ", UTUtil.getPrivateField(ValidationUtil.class,
                "hankakuKanaList"));
    }

    /**
     * testSetHankakuKanaList03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.list=<br>
     * ※空文字<br>
     * <br>
     * 期待値：(状態変化) hankakuKanaList:""<br>
     * <br>
     * validation.hankaku.kana.listに値が設定されていた場合、hankakuKanaListは設定値となること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetHankakuKanaList03() throws Exception {
        // 前処理
        addProperty("validation.hankaku.kana.list", "");

        // テスト実施
        ValidationUtil.setHankakuKanaList();

        // 判定
        assertEquals("", UTUtil.getPrivateField(ValidationUtil.class,
                "hankakuKanaList"));
    }

    /**
     * testSetZenkakuKanaList01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) zenkakuKanaList:アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     * (状態) プロパティファイル:validation.zenkaku.kana.listが存在しないこと。<br>
     * <br>
     * 期待値：(状態変化) zenkakuKanaList:アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     * <br>
     * validation.zenkaku.kana.listに値が未設定の場合、zenkakuKanaListはデフォルトのままであること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetZenkakuKanaList01() throws Exception {
        // テスト実施
        ValidationUtil.setZenkakuKanaList();

        // 判定
        assertEquals(
                "アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー",
                UTUtil.getPrivateField(ValidationUtil.class,
                        "zenkakuKanaList"));
    }

    /**
     * testSetZenkakuKanaList02() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) zenkakuKanaList:アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     * (状態) プロパティファイル:validation.zenkaku.kana.list=ア<br>
     * <br>
     * 期待値：(状態変化) zenkakuKanaList:ア<br>
     * <br>
     * validation.zenkaku.kana.listに値が設定されていた場合、zenkakuKanaListは設定値となること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetZenkakuKanaList02() throws Exception {
        // 前処理
        addProperty("validation.zenkaku.kana.list", "ア");

        // テスト実施
        ValidationUtil.setZenkakuKanaList();

        // 判定
        assertEquals("ア", UTUtil.getPrivateField(ValidationUtil.class,
                "zenkakuKanaList"));
    }

    /**
     * testSetZenkakuKanaList03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) zenkakuKanaList:アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     * (状態) プロパティファイル:validation.zenkaku.kana.list=<br>
     * ※空文字<br>
     * <br>
     * 期待値：(状態変化) zenkakuKanaList:""<br>
     * <br>
     * validation.zenkaku.kana.listに値が設定されていた場合、zenkakuKanaListは設定値となること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetZenkakuKanaList03() throws Exception {
        // 前処理
        addProperty("validation.zenkaku.kana.list", "");

        // テスト実施
        ValidationUtil.setZenkakuKanaList();

        // 判定
        assertEquals("", UTUtil.getPrivateField(ValidationUtil.class,
                "zenkakuKanaList"));
    }

    /**
     * testIsHankakuKanaChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * ※一文字ずつ確認<br>
     * (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:全ての文字についてtrue<br>
     * <br>
     * 引数に指定した文字がhankakuKanaListに含まれる場合、trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsHankakuKanaChar01() throws Exception {
        // 前処理
        String hankakuKanaList = "ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣";

        // テスト実施・判定
        for (int i = 0; i < hankakuKanaList.length(); i++) {
            assertTrue(ValidationUtil.isHankakuKanaChar(hankakuKanaList.charAt(
                    i)));
        }
    }

    /**
     * testIsHankakuKanaChar02() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:'｡'-1<br>
     * 'ﾟ'+1<br>
     * ※一文字ずつ確認<br>
     * (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:全ての文字についてfalse<br>
     * <br>
     * 引数に指定した文字がhankakuKanaListに含まれない場合、falseが取得できることを確認する。（半角カナの境界テスト） <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsHankakuKanaChar02() throws Exception {
        // 前処理
        char chStart = '｡' - 1;
        char chEnd = 'ﾟ' + 1;

        // テスト実施・判定
        assertFalse(ValidationUtil.isHankakuKanaChar(chStart));
        assertFalse(ValidationUtil.isHankakuKanaChar(chEnd));
    }

    /**
     * testIsHankakuKanaChar03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:'全'<br>
     * (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 引数に指定した文字がhankakuKanaListに含まれない場合、falseが取得できることを確認する。（全角文字） <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsHankakuKanaChar03() throws Exception {
        // 前処理
        char chZenkaku = '全';

        // テスト実施・判定
        assertFalse(ValidationUtil.isHankakuKanaChar(chZenkaku));
    }

    /**
     * testIsHankakuChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:'\u00ff'<br>
     * '｡'<br>
     * 'ﾟ'<br>
     * ※一文字ずつ確認<br>
     * (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:全ての文字についてtrue<br>
     * <br>
     * 引数に指定した文字が文字コード'\00ff'以下且つ、"＼￠￡§¨￢°±´¶×÷"ではなく、hankakuKanaListに含まれる場合、trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsHankakuChar01() throws Exception {
        // 前処理
        char chHankakuMax = '\u00ff';
        char chHankakuKanaStart = '｡';
        char chHankakuKanaEnd = 'ﾟ';

        // テスト実施・判定
        // 半角文字が設定されたとき、trueが返却されること
        assertTrue(ValidationUtil.isHankakuChar(chHankakuMax));
        assertTrue(ValidationUtil.isHankakuChar(chHankakuKanaStart));
        assertTrue(ValidationUtil.isHankakuChar(chHankakuKanaEnd));
    }

    /**
     * testIsHankakuChar02() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:'\u0100'<br>
     * '｡'-1<br>
     * 'ﾟ'+1<br>
     * ※一文字ずつ確認<br>
     * (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:全ての文字についてfalse<br>
     * <br>
     * 引数に指定した文字が文字コード'\00ff'以上、または、"＼￠￡§¨￢°±´¶×÷"に含まれる、または、hankakuKanaListに含まれない場合、falseが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsHankakuChar02() throws Exception {
        // 前処理
        char chUpperff = '\u0100';
        char chKanaStart = '｡' - 1;
        char chKanaEnd = 'ﾟ' + 1;

        // テスト実施・判定
        assertFalse(ValidationUtil.isHankakuChar(chUpperff));
        assertFalse(ValidationUtil.isHankakuChar(chKanaStart));
        assertFalse(ValidationUtil.isHankakuChar(chKanaEnd));
    }

    /**
     * testIsHankakuChar03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:'ア'<br>
     * '６'<br>
     * '＆'<br>
     * 'ａ'<br>
     * 'ｚ'<br>
     * 'Ａ'<br>
     * 'Ｚ'<br>
     * (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 引数に指定した文字が全角文字である場合、falseが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsHankakuChar03() throws Exception {
        // 前処理
        char[] input = { 'ア', '６', '＆', 'ａ', 'ｚ', 'Ａ', 'Ｚ' };

        // テスト実施・判定
        // 全角文字が設定されたとき、falseが返却されること
        for (char c : input) {
            assertFalse(ValidationUtil.isHankakuChar(c));
        }
    }

    /**
     * testIsHankakuChar04() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:"＼￠￡§¨￢°±´¶×÷"<br>
     * ※一文字ずつ確認<br>
     * (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 引数に指定した文字が文字コード'\00ff'以上、または、"＼￠￡§¨￢°±´¶×÷"に含まれる、または、hankakuKanaListに含まれない場合、falseが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsHankakuChar04() throws Exception {
        // 前処理
        String zenkakuBeginU00List = "＼￠￡§¨￢°±´¶×÷";

        // テスト実施・判定
        for (int i = 0; i < zenkakuBeginU00List.length(); i++) {
            assertFalse(ValidationUtil.isHankakuChar(zenkakuBeginU00List.charAt(
                    i)));
        }
    }

    /**
     * testIsZenkakuChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:'\u0100'<br>
     * '｡'-1<br>
     * 'ﾟ'+1<br>
     * ※一文字ずつ確認<br>
     * (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:全ての文字についてtrue<br>
     * <br>
     * 引数に指定した文字が文字コード'\00ff'より大きい、且つ、"＼￠￡§¨￢°±´¶×÷"に含まれるか、hankakuKanaListに含まれない場合、trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsZenkakuChar01() throws Exception {
        // 前処理
        char chZenkakuMin = '\u0100';
        char chZenkakuKanaStart = '｡' - 1;
        char chZenkakuKanaEnd = 'ﾟ' + 1;

        // テスト実施・判定
        // 全角文字列が設定されたとき、trueが返却されること
        assertTrue(ValidationUtil.isZenkakuChar(chZenkakuMin));
        assertTrue(ValidationUtil.isZenkakuChar(chZenkakuKanaStart));
        assertTrue(ValidationUtil.isZenkakuChar(chZenkakuKanaEnd));
    }

    /**
     * testIsZenkakuChar02() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:'\u00ff'<br>
     * '｡'<br>
     * 'ﾟ'<br>
     * ※一文字ずつ確認<br>
     * (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:全ての文字についてfalse<br>
     * <br>
     * 引数に指定した文字が文字コード'\00ff'以下且つ、"＼￠￡§¨￢°±´¶×÷"ではなく、hankakuKanaListに含まれる場合、falseが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsZenkakuChar02() throws Exception {
        // 前処理
        char chZenkakuMin = '\u00ff';
        char chZenkakuKanaStart = '｡';
        char chZenkakuKanaEnd = 'ﾟ';

        // テスト実施・判定
        // 半角文字が設定されたとき、falseが返却されること
        assertFalse(ValidationUtil.isZenkakuChar(chZenkakuMin));
        assertFalse(ValidationUtil.isZenkakuChar(chZenkakuKanaStart));
        assertFalse(ValidationUtil.isZenkakuChar(chZenkakuKanaEnd));
    }

    /**
     * testIsZenkakuChar03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:'ｱ'<br>
     * '6'<br>
     * '&'<br>
     * 'a'<br>
     * 'z'<br>
     * 'A'<br>
     * 'Z'<br>
     * (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 引数に指定した文字が半角文字である場合、falseが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsZenkakuChar03() throws Exception {
        // 前処理
        char[] input = { 'ｱ', '6', '&', 'a', 'z', 'A', 'Z' };

        // テスト実施・判定
        // 半角文字が設定されたとき、falseが返却されること
        for (char c : input) {
            assertFalse(ValidationUtil.isZenkakuChar(c));
        }
    }

    /**
     * testIsZenkakuChar04() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:"＼￠￡§¨￢°±´¶×÷"<br>
     * ※一文字ずつ確認<br>
     * (状態) hankakuKanaList:ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝﾟﾞｰ･､｡｢｣<br>
     * (状態) プロパティファイル:validation.hankaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * 引数に指定した文字が"＼￠￡§¨￢°±´¶×÷"に含まれる場合、trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsZenkakuChar04() throws Exception {
        // 前処理
        String zenkakuBeginU00List = "＼￠￡§¨￢°±´¶×÷";

        // テスト実施・判定
        for (int i = 0; i < zenkakuBeginU00List.length(); i++) {
            assertTrue(ValidationUtil.isZenkakuChar(zenkakuBeginU00List.charAt(
                    i)));
        }
    }

    /**
     * testIsZenkakuKanaChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     * ※一文字ずつ確認<br>
     * (状態) zenkakuKanaList:アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     * (状態) プロパティファイル:validation.zenkaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:全ての文字についてtrue<br>
     * <br>
     * 引数に指定した文字がzenkakuKanaListに含まれる場合、trueが取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsZenkakuKanaChar01() throws Exception {
        // 前処理
        String zenkakuKanaList = "アイウヴエオァィゥェォカキクケコ" + "ヵヶガギグゲゴサシスセソザジズゼゾタチツテト"
                + "ダヂヅデドナニヌネノハヒフヘホバビブベボ" + "パピプペポマミムメモヤユヨャュョラリルレロ" + "ワヮヰヱヲッンー";

        // テスト実施・判定
        for (int i = 0; i < zenkakuKanaList.length(); i++) {
            assertTrue(ValidationUtil.isZenkakuKanaChar(zenkakuKanaList.charAt(
                    i)));
        }
    }

    /**
     * testIsZenkakuKanaChar02() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:'ァ' - 1<br>
     * 'ー' + 1<br>
     * ※一文字ずつ確認<br>
     * (状態) zenkakuKanaList:アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     * (状態) プロパティファイル:validation.zenkaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:全ての文字についてfalse<br>
     * <br>
     * 引数に指定した文字がzenkakuKanaListに含まれない場合、falseが取得できることを確認する。（半角カナの境界テスト） <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsZenkakuKanaChar02() throws Exception {
        // 前処理
        char chStart = 'ァ' - 1;
        char chEnd = 'ー' + 1;

        // テスト実施・判定
        assertFalse(ValidationUtil.isZenkakuKanaChar(chStart));
        assertFalse(ValidationUtil.isZenkakuKanaChar(chEnd));
    }

    /**
     * testIsZenkakuKanaChar03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) c:'あ'<br>
     * (状態) zenkakuKanaList:アイウヴエオァィゥェォカキクケコヵヶガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨャュョラリルレロワヮヰヱヲッンー<br>
     * (状態) プロパティファイル:validation.zenkaku.kana.list<br>
     * が存在しないこと。<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 引数に指定した文字がzenkakuKanaListに含まれない場合、falseが取得できることを確認する。（全角平仮名） <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsZenkakuKanaChar03() throws Exception {
        // 前処理
        char chHiragana = 'あ';

        // テスト実施・判定
        assertFalse(ValidationUtil.isZenkakuKanaChar(chHiragana));
    }

}
