package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Test;

import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.DateFormatLocal} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> SimpleDateFormatがスレッドセーフではないため、ThreadLocalを使用してスレッドセーフにする。
 * <p>
 * @see jp.terasoluna.fw.file.dao.standard.DateFormatLocal
 */
public class DateFormatLocalTest {

    /**
     * testDateFormatLocalStringpattern01() <br>
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
    public void testDateFormatLocalStringpattern01() throws Exception {
        // 前処理(引数)
        String pattern = new String();

        // テスト実施
        DateFormatLocal dateFormatLocal = new DateFormatLocal(pattern);

        // 判定
        assertNotNull(dateFormatLocal);
        assertSame(pattern, ReflectionTestUtils.getField(dateFormatLocal,
                "pattern"));
    }

    /**
     * testDateFormatinitialValue01() <br>
     * <br>
     * (異常系) <br>
     * 観点：E.G <br>
     * <br>
     * 入力値：(状態) pattern:null<br>
     * <br>
     * 期待値：(状態変化) なし:NullPointerException<br>
     * <br>
     * 前提条件がnullの場合NullPointerExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testDateFormatinitialValue01() throws Exception {
        // 前処理
        DateFormatLocal dateFormatLocal = new DateFormatLocal(null);

        // 前処理(状態)
        ReflectionTestUtils.setField(dateFormatLocal, "pattern", null);

        try {
            // テスト実施
            dateFormatLocal.initialValue();
            fail("NullPointerExceptionが発生しませんでした。");
        } catch (NullPointerException e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    /**
     * testDateFormatinitialValue02() <br>
     * <br>
     * (正常系) <br>
     * 観点：E.F <br>
     * <br>
     * 入力値：(状態) pattern:"yyyy/MM/dd"<br>
     * <br>
     * 期待値：(戻り値) DateFormat:以下の要素を持つSimpleDateFormatインスタンス<br>
     * pattern： "yyyy/MM/dd"<br>
     * setLenient： FALSE<br>
     * <br>
     * 前提条件がnullじゃない場合正常実施することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testDateFormatinitialValue02() throws Exception {
        // 前処理
        DateFormatLocal dateFormatLocal = new DateFormatLocal(null);

        // 前処理(状態)
        String pattern = "yyyy/MM/dd";
        ReflectionTestUtils.setField(dateFormatLocal, "pattern", pattern);

        // テスト実施
        DateFormat result = dateFormatLocal.initialValue();

        // 判定
        assertNotNull(result);
        assertTrue(result instanceof SimpleDateFormat);
        assertEquals(pattern, SimpleDateFormat.class.cast(result).toPattern());
        assertFalse(result.isLenient());
    }
}
