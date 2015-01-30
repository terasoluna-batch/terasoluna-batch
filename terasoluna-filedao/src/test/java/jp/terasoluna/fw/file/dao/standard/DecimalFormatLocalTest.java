/*
 * $Id:$
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import java.text.DecimalFormat;

import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.DecimalFormatLocal} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> DecimalFormatがスレッドセーフではないため、ThreadLocalを使用してスレッドセーフにする。
 * <p>
 * @author 姜 恩美
 * @see jp.terasoluna.fw.file.dao.standard.DecimalFormatLocal
 */
public class DecimalFormatLocalTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(DecimalFormatLocalTest.class);
    }

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 終了処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * コンストラクタ。
     * @param name このテストケースの名前。
     */
    public DecimalFormatLocalTest(String name) {
        super(name);
    }

    /**
     * testDecimalFormatLocalStringpattern01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E.F <br>
     * <br>
     * 入力値：(引数) pattern:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) this.pattern:引数patternと同じ値<br>
     * <br>
     * 引数patternと同じ値が設定されることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testDecimalFormatLocalStringpattern01() throws Exception {
        // 前処理(引数)
        String pattern = new String();

        // テスト実施
        DecimalFormatLocal decimalFormatLocal = new DecimalFormatLocal(pattern);

        // 判定
        assertNotNull(decimalFormatLocal);
        assertSame(pattern, UTUtil.getPrivateField(decimalFormatLocal,
                "pattern"));
    }

    /**
     * testInitialValue01() <br>
     * <br>
     * (異常系) <br>
     * 観点：E.Ｇ <br>
     * <br>
     * 入力値：(状態) pattern:null<br>
     * <br>
     * 期待値：(状態変化) なし:NullPointerException<br>
     * <br>
     * 前提条件がnullの場合NullPointerExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testInitialValue01() throws Exception {
        // 前処理
        DecimalFormatLocal decimalFormatLocal = new DecimalFormatLocal(null);

        // 前処理(状態)
        UTUtil.setPrivateField(decimalFormatLocal, "pattern", null);

        try {
            // テスト実施
            decimalFormatLocal.initialValue();
            fail("NullPointerExceptionが発生しませんでした。");
        } catch (NullPointerException e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    /**
     * testInitialValue02() <br>
     * <br>
     * (正常系) <br>
     * 観点：E.F <br>
     * <br>
     * 入力値：(状態) pattern:"-\\#,##0.##"<br>
     * <br>
     * 期待値：(戻り値) DecimalFormat:patternに対するDecimalFormatインスタンス<br>
     * <br>
     * 前提条件がnullじゃない場合正常実施することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testInitialValue02() throws Exception {
        // 前処理
        DecimalFormatLocal decimalFormatLocal = new DecimalFormatLocal(null);
        String pattern = "-\\#,##0.##";

        // 前処理(状態)
        UTUtil.setPrivateField(decimalFormatLocal, "pattern", pattern);

        // テスト実施
        DecimalFormat result = decimalFormatLocal.initialValue();

        // 判定
        assertNotNull(result);
        assertTrue(result instanceof DecimalFormat);
        assertEquals(pattern, DecimalFormat.class.cast(result).toPattern());
    }
}
