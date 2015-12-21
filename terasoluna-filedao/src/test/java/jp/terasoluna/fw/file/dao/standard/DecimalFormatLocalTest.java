package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DecimalFormat;

import org.junit.Test;

import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.DecimalFormatLocal} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> DecimalFormatがスレッドセーフではないため、ThreadLocalを使用してスレッドセーフにする。
 * <p>
 * @see jp.terasoluna.fw.file.dao.standard.DecimalFormatLocal
 */
public class DecimalFormatLocalTest {

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
    @Test
    public void testDecimalFormatLocalStringpattern01() throws Exception {
        // 前処理(引数)
        String pattern = new String();

        // テスト実施
        DecimalFormatLocal decimalFormatLocal = new DecimalFormatLocal(pattern);

        // 判定
        assertNotNull(decimalFormatLocal);
        assertSame(pattern, ReflectionTestUtils.getField(decimalFormatLocal,
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
    @Test
    public void testInitialValue01() throws Exception {
        // 前処理
        DecimalFormatLocal decimalFormatLocal = new DecimalFormatLocal(null);

        // 前処理(状態)
        ReflectionTestUtils.setField(decimalFormatLocal, "pattern", null);

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
    @Test
    public void testInitialValue02() throws Exception {
        // 前処理
        DecimalFormatLocal decimalFormatLocal = new DecimalFormatLocal(null);
        String pattern = "-\\#,##0.##";

        // 前処理(状態)
        ReflectionTestUtils.setField(decimalFormatLocal, "pattern", pattern);

        // テスト実施
        DecimalFormat result = decimalFormatLocal.initialValue();

        // 判定
        assertNotNull(result);
        assertTrue(result instanceof DecimalFormat);
        assertEquals(pattern, DecimalFormat.class.cast(result).toPattern());
    }
}
