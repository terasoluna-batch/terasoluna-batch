package jp.terasoluna.fw.collector.util;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import jp.terasoluna.fw.collector.AbstractCollector;
import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorExceptionHandlerStub1;
import jp.terasoluna.fw.collector.CollectorExceptionHandlerStub2;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandlerStatus;
import jp.terasoluna.fw.collector.validate.ExceptionValidationErrorHandler;
import jp.terasoluna.fw.collector.validate.ValidationErrorException;
import jp.terasoluna.fw.collector.validate.ValidationErrorHandler;
import jp.terasoluna.fw.collector.vo.DataValueObject;
import jp.terasoluna.fw.file.dao.FileLineException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * コレクタとコントロールブレイクの組み合わせ試験。 
 * 機能結合試験に該当するが、JUnitでも試験が可能であること、 組み合わせのパターンが多岐にわたることから、 JUnitの試験とした。
 */
public class ControlBreakCheckerTest2 {

    /**
     * ブレイクキーのカラム名
     */
    private static final String BREAK_KEY = "column1";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * コントロールブレイクと組み合わせのテスト。<br>
     * BUG_B30020の強化試験。
     * <p>
     * <ul>
     * <li>例外ハンドラなし(そのままエラーをスローする)</li>
     * </ul>
     * 期待値は以下の通り
     * 入力データ：[1(bkv1), 2(bkv1), 3(bkv1), (例外), 5(bkv2), 6(bkv2), 7(bkv3), 
     * (例外), 9(bkv3), 10(bkv4), 11(bkv4), (例外),
     * 13(bkv5), 14(bkv5), 15(bkv5)]
     * 結果：[1(※1), 2, 3(※4), (例外)(※2※4), 5(※2), 6(※3), 7(※1※4), 
     * (例外)(※2※4), 9(※2※3), 10(※1), 11(※4), (例外)(※2※4), 13(※2), 14, 15(※3)]
     * ※1:前ブレイク ※2:前ブレイク判定不能(前ブレイク判定時に例外スロー)
     * ※3:後ブレイク ※4:後ブレイク判定不能(後ブレイク判定時に例外スロー)
     */
    @Test
    public void testDefaultHandler001() throws Exception {

        ControlBreakCheckerTestBean bean01 = new ControlBreakCheckerTestBean("aaa", "1", "1");
        ControlBreakCheckerTestBean bean02 = new ControlBreakCheckerTestBean("aaa", "2", "1");
        ControlBreakCheckerTestBean bean03 = new ControlBreakCheckerTestBean("aaa", "3", "1");
        ControlBreakCheckerTestBean bean04 = new ControlBreakCheckerTestBean("bbb", "4", "Exception");
        ControlBreakCheckerTestBean bean05 = new ControlBreakCheckerTestBean("bbb", "5", "1");
        ControlBreakCheckerTestBean bean06 = new ControlBreakCheckerTestBean("bbb", "6", "1");
        ControlBreakCheckerTestBean bean07 = new ControlBreakCheckerTestBean("ccc", "7", "1");
        ControlBreakCheckerTestBean bean08 = new ControlBreakCheckerTestBean("ccc", "8", "Exception");
        ControlBreakCheckerTestBean bean09 = new ControlBreakCheckerTestBean("ccc", "9", "1");
        ControlBreakCheckerTestBean bean10 = new ControlBreakCheckerTestBean("ddd", "10", "1");
        ControlBreakCheckerTestBean bean11 = new ControlBreakCheckerTestBean("ddd", "11", "1");
        ControlBreakCheckerTestBean bean12 = new ControlBreakCheckerTestBean("ddd", "12", "Exception");
        ControlBreakCheckerTestBean bean13 = new ControlBreakCheckerTestBean("eee", "13", "1");
        ControlBreakCheckerTestBean bean14 = new ControlBreakCheckerTestBean("eee", "14", "1");
        ControlBreakCheckerTestBean bean15 = new ControlBreakCheckerTestBean("eee", "15", "1");
        ControlBreakCheckerTestBean[] beans = { bean01, bean02, bean03, bean04,
                bean05, bean06, bean07, bean08, bean09, bean10, bean11, bean12,
                bean13, bean14, bean15 };

        // ・入力チェックエラーハンドラ：なし
        ValidationErrorHandler argValidationErrorHandler = null;
        // 例外ハンドラ：なし
        CollectorExceptionHandler argExceptionHandler = null;
        Collector<ControlBreakCheckerTestBean> collector = createTestCollector(
                argValidationErrorHandler, argExceptionHandler, beans);

        // ####################第1要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=TRUE
        assertTrue(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        assertEquals("aaa", ControlBreakChecker.getPreBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第2要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        // ####################第3要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=(エラー)
        try {
            ControlBreakChecker.isBreak(collector, BREAK_KEY);
            fail();
        } catch (FileLineException e) {
            assertEquals("入力エラー", e.getMessage());
        }

        // ####################第4要素(例外)####################
        try {
            collector.next();
            fail();
        } catch (FileLineException e) {
            // ポインタを進める
        }
        try {
            ControlBreakChecker.isPreBreak(collector, BREAK_KEY);
            fail();
        } catch (FileLineException e) {
            // 前ブレイク判定不能
        }
        try {
            ControlBreakChecker.isBreak(collector, BREAK_KEY);
            fail();
        } catch (FileLineException e) {
            // 後ブレイク判定不能
        }
        // ####################第5要素####################
        assertTrue(collector.hasNext());
        collector.next();
        // 前ブレイク=(エラー)
        try {
            ControlBreakChecker.isPreBreak(collector, BREAK_KEY);
            fail();
        } catch (FileLineException e) {
            assertEquals("入力エラー", e.getMessage());
        }

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第6要素####################
        assertTrue(collector.hasNext());
        collector.next();
        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=TRUE
        assertTrue(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        assertEquals("bbb", ControlBreakChecker.getBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // ####################第7要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=TRUE
        assertTrue(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        assertEquals("ccc", ControlBreakChecker.getPreBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // 後ブレイク=(エラー)
        try {
            ControlBreakChecker.isBreak(collector, BREAK_KEY);
            fail();
        } catch (FileLineException e) {
            assertEquals("入力エラー", e.getMessage());
        }
        // ####################第8要素はエラー####################
        try {
            collector.next();
            fail();
        } catch (FileLineException e) {
            // ポインタを進める
        }
        try {
            ControlBreakChecker.isPreBreak(collector, BREAK_KEY);
            fail();
        } catch (FileLineException e) {
            // 前ブレイク判定不能
        }
        try {
            ControlBreakChecker.isBreak(collector, BREAK_KEY);
            fail();
        } catch (FileLineException e) {
            // 後ブレイク判定不能
        }
        // ####################第9要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=(エラー)
        try {
            ControlBreakChecker.isPreBreak(collector, BREAK_KEY);
            fail();
        } catch (FileLineException e) {
            assertEquals("入力エラー", e.getMessage());
        }

        // 後ブレイク=TRUE
        assertTrue(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        assertEquals("ccc", ControlBreakChecker.getBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // ####################第10要素####################
        assertTrue(collector.hasNext());
        collector.next();
        // 前ブレイク=TRUE
        assertTrue(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        assertEquals("ddd", ControlBreakChecker.getPreBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第11要素####################
        assertTrue(collector.hasNext());
        collector.next();
        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=(エラー)
        try {
            ControlBreakChecker.isBreak(collector, BREAK_KEY);
            fail();
        } catch (FileLineException e) {
            assertEquals("入力エラー", e.getMessage());
        }

        // ####################第12要素はエラー####################
        try {
            collector.next();
            fail();
        } catch (FileLineException e) {
            // ポインタを進める
        }
        try {
            ControlBreakChecker.isPreBreak(collector, BREAK_KEY);
            fail();
        } catch (FileLineException e) {
            // 前ブレイク判定不能
        }
        try {
            ControlBreakChecker.isBreak(collector, BREAK_KEY);
            fail();
        } catch (FileLineException e) {
            // 後ブレイク判定不能
        }
        // ####################第13要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=(エラー)
        try {
            ControlBreakChecker.isPreBreak(collector, BREAK_KEY);
            fail();
        } catch (FileLineException e) {
            assertEquals("入力エラー", e.getMessage());
        }
        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第14要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第15要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=TRUE
        assertTrue(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        assertEquals("eee", ControlBreakChecker.getBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        collector.close();
    }

    /**
     * コントロールブレイクと組み合わせのテスト。<br>
     * BUG_B30020の強化試験。
     * <p>
     * <ul>
     * <li>例外ハンドラあり(END)</li>
     * <li>入力データ上のブレイク前で例外→END</li>
     * </ul>
     * 期待値は以下の通り
     * 入力データ：[1(bkv1), 2(bkv1), (例外), 4(bkv2)]
     * 結果：[1(※1), 2(※3)]
     * ※1:前ブレイク ※2:前ブレイク判定不能(前ブレイク判定時に例外スロー)
     * ※3:後ブレイク ※4:後ブレイク判定不能(後ブレイク判定時に例外スロー)
     */
    @Test
    public void testErrorHandlerReturnsEnd001() throws Exception {
        ControlBreakCheckerTestBean bean01 = new ControlBreakCheckerTestBean("aaa", "1", "1");
        ControlBreakCheckerTestBean bean02 = new ControlBreakCheckerTestBean("aaa", "2", "1");
        ControlBreakCheckerTestBean bean03 = new ControlBreakCheckerTestBean("aaa", "3", "Exception"); // 例外
        ControlBreakCheckerTestBean bean04 = new ControlBreakCheckerTestBean("bbb", "4", "1");

        ControlBreakCheckerTestBean[] beans = { bean01, bean02, bean03, bean04 };

        // ・入力チェックエラーハンドラ：なし
        ValidationErrorHandler argValidationErrorHandler = null;
        // 例外ハンドラ：あり(ENDを返す)
        CollectorExceptionHandler argExceptionHandler = new CollectorExceptionHandlerStub1();
        Collector<ControlBreakCheckerTestBean> collector = createTestCollector(
                argValidationErrorHandler, argExceptionHandler, beans);

        // ####################第1要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=TRUE
        assertTrue(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        assertEquals("aaa", ControlBreakChecker.getPreBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第2要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=TRUE
        assertTrue(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        assertEquals("aaa", ControlBreakChecker.getBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // ####################第3要素####################
        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }

        collector.close();
    }

    /**
     * コントロールブレイクと組み合わせのテスト。<br>
     * BUG_B30020の強化試験。
     * <p>
     * <ul>
     * <li>例外ハンドラあり(END)</li>
     * <li>入力データ上のブレイク後で例外→END</li>
     * </ul>
     * 期待値は以下の通り
     * 入力データ：[1(bkv1), 2(bkv1), 3(bkv1), (例外), 5(bkv2)]
     * 結果：[1(※1), 2, 3(※3)]
     * ※1:前ブレイク ※2:前ブレイク判定不能(前ブレイク判定時に例外スロー)
     * ※3:後ブレイク ※4:後ブレイク判定不能(後ブレイク判定時に例外スロー)
     */
    @Test
    public void testErrorHandlerReturnsEnd002() throws Exception {
        ControlBreakCheckerTestBean bean01 = new ControlBreakCheckerTestBean("aaa", "1", "1");
        ControlBreakCheckerTestBean bean02 = new ControlBreakCheckerTestBean("aaa", "2", "1");
        ControlBreakCheckerTestBean bean03 = new ControlBreakCheckerTestBean("aaa", "3", "1");
        ControlBreakCheckerTestBean bean04 = new ControlBreakCheckerTestBean("bbb", "4", "Exception"); // 例外
        ControlBreakCheckerTestBean bean05 = new ControlBreakCheckerTestBean("bbb", "5", "1");

        ControlBreakCheckerTestBean[] beans = { bean01, bean02, bean03, bean04,
                bean05 };

        // ・入力チェックエラーハンドラ：なし
        ValidationErrorHandler argValidationErrorHandler = null;
        // 例外ハンドラ：あり(ENDを返す)
        CollectorExceptionHandler argExceptionHandler = new CollectorExceptionHandlerStub1();
        Collector<ControlBreakCheckerTestBean> collector = createTestCollector(
                argValidationErrorHandler, argExceptionHandler, beans);

        // ####################第1要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=TRUE
        assertTrue(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        assertEquals("aaa", ControlBreakChecker.getPreBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第2要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第3要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=TRUE
        assertTrue(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        assertEquals("aaa", ControlBreakChecker.getBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // ####################第4要素####################
        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }

        collector.close();
    }

    /**
     * コントロールブレイクと組み合わせのテスト。<br>
     * BUG_B30020の強化試験。
     * <p>
     * <ul>
     * <li>例外ハンドラあり(SKIP)</li>
     * <li>後ブレイク後に例外(ブレイク判定可能)</li>
     * </ul>
     * 期待値は以下の通り
     * 入力データ：[1(bkv1), 2(bkv1), (例外), 4(bkv1), 5(bkv1), (例外), 7(bkv2), 8(bkv2), (例外), 10(bkv3), 11(bkv3), (例外)]
     * 結果：[1(※1), 2, 4, 5(※3), 7(※1), 8(※3), 10(※1), 11(※3)]
     * ※1:前ブレイク ※2:前ブレイク判定不能(前ブレイク判定時に例外スロー)
     * ※3:後ブレイク ※4:後ブレイク判定不能(後ブレイク判定時に例外スロー)
     */
    @Test
    public void testErrorHandlerReturnsSkip001() throws Exception {
        ControlBreakCheckerTestBean bean01 = new ControlBreakCheckerTestBean("aaa", "1", "1");
        ControlBreakCheckerTestBean bean02 = new ControlBreakCheckerTestBean("aaa", "2", "1");
        ControlBreakCheckerTestBean bean03 = new ControlBreakCheckerTestBean("aaa", "3", "Exception");
        ControlBreakCheckerTestBean bean04 = new ControlBreakCheckerTestBean("aaa", "4", "1");
        ControlBreakCheckerTestBean bean05 = new ControlBreakCheckerTestBean("aaa", "5", "1");
        ControlBreakCheckerTestBean bean06 = new ControlBreakCheckerTestBean("aaa", "6", "Exception");
        ControlBreakCheckerTestBean bean07 = new ControlBreakCheckerTestBean("bbb", "7", "1");
        ControlBreakCheckerTestBean bean08 = new ControlBreakCheckerTestBean("bbb", "8", "1");
        ControlBreakCheckerTestBean bean09 = new ControlBreakCheckerTestBean("ccc", "9", "Exception");
        ControlBreakCheckerTestBean bean10 = new ControlBreakCheckerTestBean("ccc", "10", "1");
        ControlBreakCheckerTestBean bean11 = new ControlBreakCheckerTestBean("ccc", "11", "1");
        ControlBreakCheckerTestBean bean12 = new ControlBreakCheckerTestBean("ccc", "12", "Exception");

        ControlBreakCheckerTestBean[] beans = { bean01, bean02, bean03, bean04,
                bean05, bean06, bean07, bean08, bean09, bean10, bean11, bean12 };

        // ・入力チェックエラーハンドラ：なし
        ValidationErrorHandler argValidationErrorHandler = null;
        // 例外ハンドラ：あり(SKIPを返す)
        CollectorExceptionHandler argExceptionHandler = new CollectorExceptionHandlerStub2(CollectorExceptionHandlerStatus.SKIP);
        Collector<ControlBreakCheckerTestBean> collector = createTestCollector(
                argValidationErrorHandler, argExceptionHandler, beans);

        // ####################第1要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=TRUE
        assertTrue(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        assertEquals("aaa", ControlBreakChecker.getPreBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第2要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第3要素####################
        // ####################第4要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=TRUE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第5要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=TRUE
        assertTrue(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        assertEquals("aaa", ControlBreakChecker.getBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // ####################第6要素####################
        // ####################第7要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=TRUE
        assertTrue(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        assertEquals("bbb", ControlBreakChecker.getPreBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第8要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=TRUE
        assertTrue(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        assertEquals("bbb", ControlBreakChecker.getBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // ####################第9要素####################
        // ####################第10要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=TRUE
        assertTrue(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        assertEquals("ccc", ControlBreakChecker.getPreBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第11要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        // 後ブレイク=TRUE
        assertTrue(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        assertEquals("ccc", ControlBreakChecker.getBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // ####################第12要素####################
        collector.close();
    }

    /**
     * コントロールブレイクと組み合わせのテスト。<br>
     * BUG_B30019の強化試験。
     * <p>
     * <ul>
     * <li>入力チェックエラーハンドラあり(そのまま例外をスローする)</li>
     * </ul>
     * 期待値は以下の通り
     * 入力データ：[1(bkv1), 2(bkv1), 3(bkv1), 4(入力チェックエラー)(bkv2), 5(bkv2), 6(bkv2), 7(bkv3), 
     * 8(入力チェックエラー)(bkv3), 9(bkv3), 10(bkv4), 11(bkv4), 12(入力チェックエラー)(bkv4), 13(bkv5), 14(bkv5), 15(bkv5)]
     * 結果：[1(※1), 2, 3(※3), 4(入力チェックエラー)(※1), 5, 6(※3), 7(※1),
     * 8(入力チェックエラー), 9(※3), 10(※1), 11, 12(入力チェックエラー)(※3), 13(※1), 14, 15(※3)]
     * ※1:前ブレイク ※2:前ブレイク判定不能(前ブレイク判定時に例外スロー)
     * ※3:後ブレイク ※4:後ブレイク判定不能(後ブレイク判定時に例外スロー)
     */
    @Test
    public void testValidateErrorHandler001() throws Exception {

        ControlBreakCheckerTestBean bean01 = new ControlBreakCheckerTestBean("aaa", "1", "1");
        ControlBreakCheckerTestBean bean02 = new ControlBreakCheckerTestBean("aaa", "2", "1");
        ControlBreakCheckerTestBean bean03 = new ControlBreakCheckerTestBean("aaa", "3", "1");
        ControlBreakCheckerTestBean bean04 = new ControlBreakCheckerTestBean("bbb", "4", "validateError");
        ControlBreakCheckerTestBean bean05 = new ControlBreakCheckerTestBean("bbb", "5", "1");
        ControlBreakCheckerTestBean bean06 = new ControlBreakCheckerTestBean("bbb", "6", "1");
        ControlBreakCheckerTestBean bean07 = new ControlBreakCheckerTestBean("ccc", "7", "1");
        ControlBreakCheckerTestBean bean08 = new ControlBreakCheckerTestBean("ccc", "8", "validateError");
        ControlBreakCheckerTestBean bean09 = new ControlBreakCheckerTestBean("ccc", "9", "1");
        ControlBreakCheckerTestBean bean10 = new ControlBreakCheckerTestBean("ddd", "10", "1");
        ControlBreakCheckerTestBean bean11 = new ControlBreakCheckerTestBean("ddd", "11", "1");
        ControlBreakCheckerTestBean bean12 = new ControlBreakCheckerTestBean("ddd", "12", "validateError");
        ControlBreakCheckerTestBean bean13 = new ControlBreakCheckerTestBean("eee", "13", "1");
        ControlBreakCheckerTestBean bean14 = new ControlBreakCheckerTestBean("eee", "14", "1");
        ControlBreakCheckerTestBean bean15 = new ControlBreakCheckerTestBean("eee", "15", "1");
        ControlBreakCheckerTestBean[] beans = { bean01, bean02, bean03, bean04,
                bean05, bean06, bean07, bean08, bean09, bean10, bean11, bean12,
                bean13, bean14, bean15 };

        // ・入力チェックエラーハンドラ：あり
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        // 例外ハンドラ：なし
        CollectorExceptionHandler argExceptionHandler = null;
        Collector<ControlBreakCheckerTestBean> collector = createTestCollector(
                argValidationErrorHandler, argExceptionHandler, beans);

        // ####################第1要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=TRUE
        assertTrue(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        assertEquals("aaa", ControlBreakChecker.getPreBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第2要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第3要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=TRUE
        assertTrue(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        assertEquals("aaa", ControlBreakChecker.getBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // ####################第4要素####################
        try {
            collector.next();
            fail();
        } catch (ValidationErrorException e) {
            // ポインタを一つ進める
        }

        // 前ブレイク=TRUE
        assertTrue(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        assertEquals("bbb", ControlBreakChecker.getPreBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第5要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第6要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=TRUE
        assertTrue(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        assertEquals("bbb", ControlBreakChecker.getBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // ####################第7要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=TRUE
        assertTrue(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        assertEquals("ccc", ControlBreakChecker.getPreBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第8要素####################
        try {
            collector.next();
            fail();
        } catch (ValidationErrorException e) {
            // ポインタを一つ進める
        }

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第9要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=TRUE
        assertTrue(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        assertEquals("ccc", ControlBreakChecker.getBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // ####################第10要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=TRUE
        assertTrue(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        assertEquals("ddd", ControlBreakChecker.getPreBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第11要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第12要素####################
        try {
            collector.next();
            fail();
        } catch (ValidationErrorException e) {
            // ポインタを一つ進める
        }

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=TRUE
        assertTrue(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        assertEquals("ddd", ControlBreakChecker.getBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // ####################第13要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=TRUE
        assertTrue(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));
        assertEquals("eee", ControlBreakChecker.getPreBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第14要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=FALSE
        assertFalse(ControlBreakChecker.isBreak(collector, BREAK_KEY));

        // ####################第15要素####################
        assertTrue(collector.hasNext());
        collector.next();

        // 前ブレイク=FALSE
        assertFalse(ControlBreakChecker.isPreBreak(collector, BREAK_KEY));

        // 後ブレイク=TRUE
        assertTrue(ControlBreakChecker.isBreak(collector, BREAK_KEY));
        assertEquals("eee", ControlBreakChecker.getBreakKey(collector,
                BREAK_KEY).get(BREAK_KEY));

        collector.close();
    }

    /**
     * テストデータを提供するコレクタを生成する。 
     * Validator機能を持ち、CollectorTestBeanを格納できる。
     * CollectorTestBean1のcolumn3が"validateError"だと入力チェックエラーとなり
     * "Exception"だと入力エラーが発生する。
     * @param argValidationErrorHandler ValidationErrorHandler
     * @param argExceptionHandler ExceptionHandler
     * @param CollectorTestBean[] beans コレクタの中身になるCollectorTestBean1の配列
     * @return テストデータを提供するコレクタ
     */
    private static Collector<ControlBreakCheckerTestBean> createTestCollector(
            final ValidationErrorHandler argValidationErrorHandler,
            final CollectorExceptionHandler argExceptionHandler,
            final ControlBreakCheckerTestBean[] beans) {
        Collector<ControlBreakCheckerTestBean> collector = new AbstractCollector<ControlBreakCheckerTestBean>() {
            {
                this.validator = new Validator() {

                    // 入力チェックを行う。column3が"validateError"の場合は入力チェックエラーとする。
                    public void validate(Object target, Errors errors) {
                        ControlBreakCheckerTestBean data = (ControlBreakCheckerTestBean) target;
                        if ("validateError".equals(data.getColumn3())) {
                            errors.rejectValue("column3", "errors.numeric");
                        }
                    }

                    public boolean supports(Class<?> clazz) {
                        return (clazz == ControlBreakCheckerTestBean.class);
                    }
                };
                this.validationErrorHandler = argValidationErrorHandler;
                this.exceptionHandler = argExceptionHandler;
            }

            // column3が"Exception"だったらFileLineException（入力エラー）ということにする
            public Integer call() throws Exception {

                for (int count = 0; count < beans.length; count++) {
                    if ("Exception".equals(beans[count].getColumn3())) {
                        addQueue(new DataValueObject(new FileLineException("入力エラー"), count));
                    } else {
                        addQueue(new DataValueObject(beans[count], count));
                    }
                }

                setFinish();
                return 0;
            }

        };

        return collector;

    }

}
