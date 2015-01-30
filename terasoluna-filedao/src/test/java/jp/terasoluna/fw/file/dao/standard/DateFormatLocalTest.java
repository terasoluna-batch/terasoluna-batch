/*
 * $Id:$
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.DateFormatLocal} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> SimpleDateFormatがスレッドセーフではないため、ThreadLocalを使用してスレッドセーフにする。
 * <p>
 * @author 姜 恩美
 * @see jp.terasoluna.fw.file.dao.standard.DateFormatLocal
 */
public class DateFormatLocalTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(DateFormatLocalTest.class);
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
    public DateFormatLocalTest(String name) {
        super(name);
    }

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
    public void testDateFormatLocalStringpattern01() throws Exception {
        // 前処理(引数)
        String pattern = new String();

        // テスト実施
        DateFormatLocal dateFormatLocal = new DateFormatLocal(pattern);

        // 判定
        assertNotNull(dateFormatLocal);
        assertSame(pattern, UTUtil.getPrivateField(dateFormatLocal, "pattern"));
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
    public void testDateFormatinitialValue01() throws Exception {
        // 前処理
        DateFormatLocal dateFormatLocal = new DateFormatLocal(null);

        // 前処理(状態)
        UTUtil.setPrivateField(dateFormatLocal, "pattern", null);

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
     * 　pattern：　"yyyy/MM/dd"<br>
     * 　setLenient：　FALSE<br>
     * <br>
     * 前提条件がnullじゃない場合正常実施することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testDateFormatinitialValue02() throws Exception {
        // 前処理
        DateFormatLocal dateFormatLocal = new DateFormatLocal(null);

        // 前処理(状態)
        String pattern = "yyyy/MM/dd";
        UTUtil.setPrivateField(dateFormatLocal, "pattern", pattern);

        // テスト実施
        DateFormat result = dateFormatLocal.initialValue();

        // 判定
        assertNotNull(result);
        assertTrue(result instanceof SimpleDateFormat);
        assertEquals(pattern, SimpleDateFormat.class.cast(result).toPattern());
        assertFalse(result.isLenient());
    }
}
