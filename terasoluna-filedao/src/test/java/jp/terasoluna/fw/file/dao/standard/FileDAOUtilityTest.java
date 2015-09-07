/*
 * $Id: FileDAOUtilityTest.java 5230 2007-09-28 10:04:13Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.terasoluna.fw.file.annotation.PaddingType;
import jp.terasoluna.fw.file.annotation.TrimType;
import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.ut.VMOUTUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.FileDAOUtility} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> パディング、トリム処理を提供する。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.FileDAOUtility
 */
public class FileDAOUtilityTest {

    @Before
    public void setUp() throws Exception {
        VMOUTUtil.initialize();
    }

    /**
     * testPadding01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:0<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.LEFT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1"<br>
     * <br>
     * 正常ケース<br>
     * (左パディング)<br>
     * 引数columnBytesがcolumnStringより小さい場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding01() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 0;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.LEFT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("1", result);
    }

    /**
     * testPadding02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:1<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.LEFT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1"<br>
     * <br>
     * 正常ケース<br>
     * (左パディング)<br>
     * 引数columnBytesがcolumnStringのバイト数と同じ場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding02() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 1;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.LEFT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("1", result);
    }

    /**
     * testPadding03() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.LEFT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"  1"(1の左に半角スペース2文字)<br>
     * <br>
     * 正常ケース<br>
     * (左パディング)<br>
     * 引数columnStringに不足した長さ分を元データの左にpaddingCharで埋めて文字列が取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding03() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 3;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.LEFT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("  1", result);
    }

    /**
     * testPadding04() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:0<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.RIGHT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1"<br>
     * <br>
     * 正常ケース<br>
     * (右パディング)<br>
     * 引数columnBytesがcolumnStringより小さい場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding04() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 0;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.RIGHT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("1", result);
    }

    /**
     * testPadding05() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:1<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.RIGHT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1"<br>
     * <br>
     * 正常ケース<br>
     * (右パディング)<br>
     * 引数columnBytesがcolumnStringのバイト数と同じ場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding05() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 1;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.RIGHT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("1", result);
    }

    /**
     * testPadding06() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.RIGHT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1  "(1の右に半角スペース2文字)<br>
     * <br>
     * 正常ケース<br>
     * (右パディング)<br>
     * 引数columnStringに不足した長さ分を元データの右にpaddingCharで埋めて文字列が取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding06() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 3;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.RIGHT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("1  ", result);
    }

    /**
     * testPadding07() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.NONE<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1"<br>
     * <br>
     * 正常ケース<br>
     * (パディングなし)<br>
     * columnStringがそのまま取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding07() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 3;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.NONE;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("1", result);
    }

    /**
     * testPadding08() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:'　'(全角空白文字)<br>
     * (引数) paddingType:PaddingType.LEFT<br>
     * <br>
     * 期待値：(状態変化) -:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："Padding char is not half-width character."<br>
     * <br>
     * 異常ケース<br>
     * (パディングあり)<br>
     * パディング文字は半角文字ではない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding08() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 3;
        char paddingChar = '　';
        PaddingType paddingType = PaddingType.LEFT;

        try {
            // テスト実施
            FileDAOUtility.padding(columnString, fileEncoding, columnBytes,
                    paddingChar, paddingType);
            fail("FileException例外が発生しませんでした。");
        } catch (FileException e) {
            // 判定
            assertEquals("Padding char is not half-width character.", e
                    .getMessage());
        }
    }

    /**
     * testPadding09() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:"XXX"<br>
     * ※存在しないエンコーディング<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.LEFT<br>
     * <br>
     * 期待値：(状態変化) -:以下の設定を持つFileExceptionが発生する。<br>
     * ・メッセージ："Specified Encoding : XXX is not supported"<br>
     * ・原因例外：UnsupportedEncodingException<br>
     * <br>
     * 異常ケース<br>
     * (パディングあり)<br>
     * 存在しないエンコーディングが指定された場合、例外が発生することを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding09() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "XXX";
        int columnBytes = 3;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.LEFT;

        try {
            // テスト実施
            FileDAOUtility.padding(columnString, fileEncoding, columnBytes,
                    paddingChar, paddingType);
            fail("FileException例外が発生しませんでした。");
        } catch (FileException e) {
            // 判定
            assertEquals("Specified Encoding : XXX is not supported", e
                    .getMessage());
            assertTrue(e.getCause() instanceof UnsupportedEncodingException);
        }
    }

    /**
     * testPadding10() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:"XXX"<br>
     * ※存在しないエンコーディング<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:'　'(全角空白文字)<br>
     * (引数) paddingType:PaddingType.NONE<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1"<br>
     * <br>
     * 正常ケース<br>
     * (パディングなし)<br>
     * 他引数の状態と関係なくcolumnStringがそのまま取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding10() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "XXX";
        int columnBytes = 3;
        char paddingChar = '　';
        PaddingType paddingType = PaddingType.NONE;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("1", result);
    }

    /**
     * testPadding11() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:""<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:0<br>
     * (引数) paddingChar:'!'(半角文字)<br>
     * (引数) paddingType:PaddingType.LEFT<br>
     * <br>
     * 期待値：(戻り値) 文字列:""<br>
     * <br>
     * 正常ケース<br>
     * (左パディング、対象データが空文字)<br>
     * 引数columnBytesがcolumnStringより小さい場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding11() throws Exception {
        // 前処理(引数)
        String columnString = "";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 0;
        char paddingChar = '!';
        PaddingType paddingType = PaddingType.LEFT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("", result);
    }

    /**
     * testPadding12() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:""<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:0<br>
     * (引数) paddingChar:'!'(半角文字)<br>
     * (引数) paddingType:PaddingType.LEFT<br>
     * <br>
     * 期待値：(戻り値) 文字列:""<br>
     * <br>
     * 正常ケース<br>
     * (左パディング、対象データが空文字)<br>
     * 引数columnBytesがcolumnStringのバイト数と同じ場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding12() throws Exception {
        // 前処理(引数)
        String columnString = "";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 0;
        char paddingChar = '!';
        PaddingType paddingType = PaddingType.LEFT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("", result);
    }

    /**
     * testPadding13() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:""<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:'!'(半角文字)<br>
     * (引数) paddingType:PaddingType.LEFT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"!!!"<br>
     * <br>
     * 正常ケース<br>
     * (左パディング、対象データが空文字)<br>
     * 引数columnStringに不足した長さ分を元データの左にpaddingCharで埋めて文字列が 取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding13() throws Exception {
        // 前処理(引数)
        String columnString = "";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 3;
        char paddingChar = '!';
        PaddingType paddingType = PaddingType.LEFT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("!!!", result);
    }

    /**
     * testPadding14() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:""<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:0<br>
     * (引数) paddingChar:'!'(半角文字)<br>
     * (引数) paddingType:PaddingType.RIGHT<br>
     * <br>
     * 期待値：(戻り値) 文字列:""<br>
     * <br>
     * 正常ケース<br>
     * (右パディング、対象データが空文字)<br>
     * 引数columnBytesがcolumnStringより小さい場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding14() throws Exception {
        // 前処理(引数)
        String columnString = "";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 0;
        char paddingChar = '!';
        PaddingType paddingType = PaddingType.RIGHT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("", result);
    }

    /**
     * testPadding15() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:""<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:0<br>
     * (引数) paddingChar:'!'(半角文字)<br>
     * (引数) paddingType:PaddingType.RIGHT<br>
     * <br>
     * 期待値：(戻り値) 文字列:""<br>
     * <br>
     * 正常ケース<br>
     * (右パディング、対象データが空文字)<br>
     * 引数columnBytesがcolumnStringのバイト数と同じ場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding15() throws Exception {
        // 前処理(引数)
        String columnString = "";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 0;
        char paddingChar = '!';
        PaddingType paddingType = PaddingType.RIGHT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("", result);
    }

    /**
     * testPadding16() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:""<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:'!'(半角文字)<br>
     * (引数) paddingType:PaddingType.RIGHT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"!!!"<br>
     * <br>
     * 正常ケース<br>
     * (右パディング、対象データが空文字)<br>
     * 引数columnStringに不足した長さ分を元データの右にpaddingCharで埋めて文字列が 取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding16() throws Exception {
        // 前処理(引数)
        String columnString = "";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 3;
        char paddingChar = '!';
        PaddingType paddingType = PaddingType.RIGHT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("!!!", result);
    }

    /**
     * testPadding17() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:""<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:'!'(半角文字)<br>
     * (引数) paddingType:PaddingType.NONE<br>
     * <br>
     * 期待値：(戻り値) 文字列:""<br>
     * <br>
     * 正常ケース<br>
     * (パディングなし、対象データが空文字)<br>
     * columnStringがそのまま取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding17() throws Exception {
        // 前処理(引数)
        String columnString = "";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 3;
        char paddingChar = '!';
        PaddingType paddingType = PaddingType.NONE;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("", result);
    }

    /**
     * testPadding18() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) columnString:null<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.LEFT<br>
     * <br>
     * 期待値：(状態変化) -:NullPointerExceptionが発生する。<br>
     * <br>
     * 異常ケース<br>
     * 引数columnStringにnullの場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding18() throws Exception {
        // 前処理(引数)
        String columnString = null;
        String fileEncoding = "Shift_JIS";
        int columnBytes = 3;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.LEFT;

        try {
            // テスト実施
            FileDAOUtility.padding(columnString, fileEncoding, columnBytes,
                    paddingChar, paddingType);
            fail("NullPointerExceptionが発生しませんでした。");
        } catch (NullPointerException e) {
            // 判定
            assertTrue(e instanceof NullPointerException);
        }
    }

    /**
     * testPadding19() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:null<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.LEFT<br>
     * <br>
     * 期待値：(状態変化) -:NullPointerExceptionが発生する。<br>
     * <br>
     * 異常ケース<br>
     * 引数fileEncodingにnullの場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding19() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = null;
        int columnBytes = 3;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.LEFT;

        try {
            // テスト実施
            FileDAOUtility.padding(columnString, fileEncoding, columnBytes,
                    paddingChar, paddingType);
            fail("NullPointerExceptionが発生しませんでした。");
        } catch (NullPointerException e) {
            // 判定
            assertTrue(e instanceof NullPointerException);
        }
    }

    /**
     * testPadding20() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:-3<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.LEFT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1"<br>
     * <br>
     * 正常ケース<br>
     * (左パディング)<br>
     * 引数columnBytesが0より小さい場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding20() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "Shift_JIS";
        int columnBytes = -3;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.LEFT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("1", result);
    }

    /**
     * testPadding21() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:null<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1"<br>
     * <br>
     * 正常ケース<br>
     * 引数paddingTypeがnullの場合、処理を行わないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding21() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 3;
        char paddingChar = ' ';
        PaddingType paddingType = null;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("1", result);
    }

    /**
     * testPadding22() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"１"(全角文字)<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.LEFT<br>
     * <br>
     * 期待値：(戻り値) 文字列:" 1"(1の左に半角スペース1文字)<br>
     * <br>
     * 正常ケース<br>
     * (左パディング、全角文字)<br>
     * 引数columnStringに不足した長さ分を元データの左にpaddingCharで 埋めて文字列が取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding22() throws Exception {
        // 前処理(引数)
        String columnString = "１";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 3;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.LEFT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals(" １", result);
    }

    /**
     * testPadding23() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"１"(全角文字)<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.RIGHT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1 "(1の右に半角スペース1文字)<br>
     * <br>
     * 正常ケース<br>
     * (右パディング、全角文字)<br>
     * 引数columnStringに不足した長さ分を元データの右にpaddingCharで埋めて文字列が取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding23() throws Exception {
        // 前処理(引数)
        String columnString = "１";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 3;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.RIGHT;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("１ ", result);
    }

    /**
     * testPadding24() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"１"(全角文字)<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) columnBytes:3<br>
     * (引数) paddingChar:' '(半角空白文字)<br>
     * (引数) paddingType:PaddingType.NONE<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1"<br>
     * <br>
     * 正常ケース<br>
     * (パディングなし、全角文字)<br>
     * columnStringがそのまま取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPadding24() throws Exception {
        // 前処理(引数)
        String columnString = "１";
        String fileEncoding = "Shift_JIS";
        int columnBytes = 3;
        char paddingChar = ' ';
        PaddingType paddingType = PaddingType.NONE;

        // テスト実施
        String result = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes, paddingChar, paddingType);

        // 判定
        assertNotNull(result);
        assertEquals("１", result);
    }

    /**
     * testTrim01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.LEFT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1"<br>
     * <br>
     * 正常ケース<br>
     * (左トリム)<br>
     * trimCharで設定した文字がcolumnStringにない場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim01() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.LEFT;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("1", result);
    }

    /**
     * testTrim02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"1aaa"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.LEFT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1aaa"<br>
     * <br>
     * 正常ケース<br>
     * (左トリム)<br>
     * 文字列の左側にtrimCharで設定した文字がない場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim02() throws Exception {
        // 前処理(引数)
        String columnString = "1aaa";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.LEFT;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("1aaa", result);
    }

    /**
     * testTrim03() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"aaa1aaa"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.LEFT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1aaa"<br>
     * <br>
     * 正常ケース<br>
     * (左トリム)<br>
     * columの文字列の先頭から順にtrimCharで設定した文字を削除する。 trimCharと異なる文字が現れた時点で処理が終わることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim03() throws Exception {
        // 前処理(引数)
        String columnString = "aaa1aaa";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.LEFT;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("1aaa", result);
    }

    /**
     * testTrim04() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"1"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.RIGHT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1"<br>
     * <br>
     * 正常ケース<br>
     * (右トリム)<br>
     * trimCharで設定した文字がない場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim04() throws Exception {
        // 前処理(引数)
        String columnString = "1";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.RIGHT;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("1", result);
    }

    /**
     * testTrim05() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"aaa1"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.RIGHT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"aaa1"<br>
     * <br>
     * 正常ケース<br>
     * (右トリム)<br>
     * 文字列の右側にtrimCharで設定した文字がない場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim05() throws Exception {
        // 前処理(引数)
        String columnString = "aaa1";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.RIGHT;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("aaa1", result);
    }

    /**
     * testTrim06() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"aaa1aaa"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.RIGHT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"aaa1"<br>
     * <br>
     * 正常ケース<br>
     * (右トリム)<br>
     * columの文字列の後ろから順にtrimCharで設定した文字を削除する。trimCharと異なる文字が現れた時点で処理が終わることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim06() throws Exception {
        // 前処理(引数)
        String columnString = "aaa1aaa";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.RIGHT;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("aaa1", result);
    }

    /**
     * testTrim07() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"aaa"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.NONE<br>
     * <br>
     * 期待値：(戻り値) 文字列:"aaa"<br>
     * <br>
     * 正常ケース<br>
     * (トリムなし)<br>
     * 正常パターン。<br>
     * columnがそのまま返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim07() throws Exception {
        // 前処理(引数)
        String columnString = "aaa";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.NONE;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("aaa", result);
    }

    /**
     * testTrim08() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) columnString:"aaa"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'あ'<br>
     * (引数) trimType:TrimType.LEFT<br>
     * <br>
     * 期待値：(状態変化) -:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："Trim char is not half-width character."<br>
     * <br>
     * 異常ケース<br>
     * (トリムあり)<br>
     * パディング文字は半角文字ではない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim08() throws Exception {
        // 前処理(引数)
        String columnString = "aaa";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'あ';
        TrimType trimType = TrimType.LEFT;

        try {
            // テスト実施
            FileDAOUtility.trim(columnString, fileEncoding, trimChar, trimType);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定
            assertEquals("Trim char is not half-width character.", e
                    .getMessage());
        }
    }

    /**
     * testTrim09() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) columnString:"aaa"<br>
     * (引数) fileEncoding:"XXX"<br>
     * ※存在しないエンコーディング<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.LEFT<br>
     * <br>
     * 期待値：(状態変化) -:以下の設定を持つFileExceptionが発生する。<br>
     * ・メッセージ："Specified Encoding : XXX is not supported"<br>
     * ・原因例外：UnsupportedEncodingException<br>
     * <br>
     * 異常ケース<br>
     * (トリムあり)<br>
     * 存在しないエンコーディングが指定された場合、例外が発生することを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim09() throws Exception {
        // 前処理(引数)
        String columnString = "aaa";
        String fileEncoding = "XXX";
        char trimChar = 'a';
        TrimType trimType = TrimType.LEFT;

        try {
            // テスト実施
            FileDAOUtility.trim(columnString, fileEncoding, trimChar, trimType);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定
            assertEquals("Specified Encoding : XXX is not supported", e
                    .getMessage());
            assertTrue(e.getCause() instanceof UnsupportedEncodingException);
        }
    }

    /**
     * testTrim10() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"aaa"<br>
     * (引数) fileEncoding:"XXX"<br>
     * ※存在しないエンコーディング<br>
     * (引数) trimChar:'あ'<br>
     * (引数) trimType:TrimType.NONE<br>
     * <br>
     * 期待値：(戻り値) 文字列:"aaa"<br>
     * <br>
     * 正常ケース<br>
     * (トリムなし)<br>
     * 他引数の状態と関係なくcolumnStringがそのまま取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim10() throws Exception {
        // 前処理(引数)
        String columnString = "aaa";
        String fileEncoding = "XXX";
        char trimChar = 'あ';
        TrimType trimType = TrimType.NONE;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("aaa", result);
    }

    /**
     * testTrim11() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:""<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:' '<br>
     * (引数) trimType:TrimType.LEFT<br>
     * <br>
     * 期待値：(戻り値) 文字列:""<br>
     * <br>
     * 正常ケース<br>
     * (左トリム、対象データが空文字)<br>
     * trimCharで設定した文字がcolumnStringにない場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim11() throws Exception {
        // 前処理(引数)
        String columnString = "";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.LEFT;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("", result);
    }

    /**
     * testTrim12() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:""<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.RIGHT<br>
     * <br>
     * 期待値：(戻り値) 文字列:""<br>
     * <br>
     * 正常ケース<br>
     * (右トリム、対象データが空文字)<br>
     * trimCharで設定した文字がない場合、処理が行われないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim12() throws Exception {
        // 前処理(引数)
        String columnString = "";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.RIGHT;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("", result);
    }

    /**
     * testTrim13() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:""<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.NONE<br>
     * <br>
     * 期待値：(戻り値) 文字列:""<br>
     * <br>
     * 正常ケース<br>
     * (トリムなし、対象データが空文字)<br>
     * 正常パターン。<br>
     * columnがそのまま返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim13() throws Exception {
        // 前処理(引数)
        String columnString = "";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.NONE;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("", result);
    }

    /**
     * testTrim14() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"aaa1aaa"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.BOTH<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1"<br>
     * <br>
     * 正常ケース<br>
     * (両トリム)<br>
     * columの文字列の先頭から順にtrimCharで設定した文字を削除する。trimCharと異なる文字が現れた時点で処理が終わることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim14() throws Exception {
        // 前処理(引数)
        String columnString = "aaa1aaa";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.BOTH;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("1", result);
    }

    /**
     * testTrim15() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:""<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.BOTH<br>
     * <br>
     * 期待値：(戻り値) 文字列:"1"<br>
     * <br>
     * 正常ケース<br>
     * (両トリム、対象データが空文字)<br>
     * 対象データがそのまま取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim15() throws Exception {
        // 前処理(引数)
        String columnString = "";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.BOTH;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("", result);
    }

    /**
     * testTrim16() <br>
     * <br>
     * (異常系) <br>
     * 観点：C, D, E, G <br>
     * <br>
     * 入力値：(引数) columnString:null<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.LEFT<br>
     * <br>
     * 期待値：(状態変化) -:NullPointerExceptionが発生する。<br>
     * <br>
     * 異常ケース<br>
     * 引数columnStringがnullの場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim16() throws Exception {
        // 前処理(引数)
        String columnString = null;
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.LEFT;

        try {
            // テスト実施
            FileDAOUtility.trim(columnString, fileEncoding, trimChar, trimType);
            fail("NullPointerExceptionが発生しませんでした。");
        } catch (NullPointerException e) {
            // 判定
            assertTrue(e instanceof NullPointerException);
        }
    }

    /**
     * testTrim17() <br>
     * <br>
     * (異常系) <br>
     * 観点：C, D, E, G <br>
     * <br>
     * 入力値：(引数) columnString:"aaa1aaa"<br>
     * (引数) fileEncoding:null<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.LEFT<br>
     * <br>
     * 期待値：(状態変化) -:NullPointerExceptionが発生する。<br>
     * <br>
     * 異常ケース<br>
     * 引数fileEncodingがnullの場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim17() throws Exception {
        // 前処理(引数)
        String columnString = "aaa1aaa";
        String fileEncoding = null;
        char trimChar = 'a';
        TrimType trimType = TrimType.LEFT;

        try {
            // テスト実施
            FileDAOUtility.trim(columnString, fileEncoding, trimChar, trimType);
            fail("NullPointerExceptionが発生しませんでした。");
        } catch (NullPointerException e) {
            // 判定
            assertTrue(e instanceof NullPointerException);
        }
    }

    /**
     * testTrim18() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"aaa1aaa"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:null<br>
     * <br>
     * 期待値：(戻り値) 文字列:"aaa1aaa"<br>
     * <br>
     * 正常ケース<br>
     * 引数trimTypeがnullの場合、処理を行わないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim18() throws Exception {
        // 前処理(引数)
        String columnString = "aaa1aaa";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = null;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("aaa1aaa", result);
    }

    /**
     * testTrim19() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"aaaaaa"<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.BOTH<br>
     * <br>
     * 期待値：(戻り値) 文字列:""<br>
     * <br>
     * 正常ケース<br>
     * (両トリム)<br>
     * 全文字がトリム対象文字の場合、空文字になることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim19() throws Exception {
        // 前処理(引数)
        String columnString = "aaaaaa";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.BOTH;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("", result);
    }

    /**
     * testTrim20() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"aaa１aaa"（全角文字）<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.LEFT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"１aaa"<br>
     * <br>
     * 正常ケース<br>
     * (左トリム、全角文字)<br>
     * columの文字列の先頭から順にtrimCharで設定した文字を削除する。 trimCharと異なる文字が現れた時点で処理が終わることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim20() throws Exception {
        // 前処理(引数)
        String columnString = "aaa１aaa";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.LEFT;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("１aaa", result);
    }

    /**
     * testTrim21() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"aaa１aaa"（全角文字）<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.RIGHT<br>
     * <br>
     * 期待値：(戻り値) 文字列:"aaa１"<br>
     * <br>
     * 正常ケース<br>
     * (右トリム、全角文字)<br>
     * columの文字列の後ろから順にtrimCharで設定した文字を削除する。 trimCharと異なる文字が現れた時点で処理が終わることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim21() throws Exception {
        // 前処理(引数)
        String columnString = "aaa１aaa";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.RIGHT;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("aaa１", result);
    }

    /**
     * testTrim22() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"aaa１aaa"（全角文字）<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.NONE<br>
     * <br>
     * 期待値：(戻り値) 文字列:"aaa１aaa"<br>
     * <br>
     * 正常ケース<br>
     * (トリムなし、全角文字)<br>
     * 正常パターン。<br>
     * columnがそのまま返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim22() throws Exception {
        // 前処理(引数)
        String columnString = "aaa１aaa";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.NONE;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("aaa１aaa", result);
    }

    /**
     * testTrim23() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) columnString:"aaa１aaa"(全角文字）<br>
     * (引数) fileEncoding:Shift_JIS<br>
     * (引数) trimChar:'a'<br>
     * (引数) trimType:TrimType.BOTH<br>
     * <br>
     * 期待値：(戻り値) 文字列:"１"<br>
     * <br>
     * 正常ケース<br>
     * (両トリム、全角文字)<br>
     * columの文字列の先頭から順にtrimCharで設定した文字を削除する。 trimCharと異なる文字が現れた時点で処理が終わることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testTrim23() throws Exception {
        // 前処理(引数)
        String columnString = "aaa１aaa";
        String fileEncoding = "Shift_JIS";
        char trimChar = 'a';
        TrimType trimType = TrimType.BOTH;

        // テスト実施
        String result = FileDAOUtility.trim(columnString, fileEncoding,
                trimChar, trimType);

        // 判定
        assertNotNull(result);
        assertEquals("１", result);
    }

    /**
     * testIsHalfWidthChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) fileEncoding:"Shift_JIS"<br>
     * (引数) checkChar:','<br>
     * (状態) encodingCache:要素を持たないConcurrentHashMapインスタンス<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) encodingCache:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："Shift_JIS"<br>
     * value：以下の要素を持つConcurrentHashMapインスタンス<br>
     * - key：',' | value：TRUE<br>
     * (状態変化) Map#put():2回呼ばれる<br>
     * <br>
     * 正常ケース<br>
     * (キャッシュなし)<br>
     * エンコーディングに合う半角文字が入力された場合、TRUEが返されることを確認する。<br>
     * また、その情報がキャッシュに残ることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testIsHalfWidthChar01() throws Exception {
        // 前処理(引数)
        String fileEncoding = "Shift_JIS";
        char checkChar = ',';

        // 前処理(状態)
        Field field = FileDAOUtility.class.getDeclaredField("encodingCache");
        field.setAccessible(true);
        Map<String, Map<Character, Boolean>> encodingCache =
                (Map<String, Map<Character, Boolean>>) field.get(FileDAOUtility.class); 
        encodingCache.clear();

        // テスト実施
        Method method = FileDAOUtility.class.getDeclaredMethod("isHalfWidthChar", new Class[] {
                String.class, char.class });
        method.setAccessible(true);
        Object result = method.invoke(FileDAOUtility.class, new Object[] { fileEncoding, checkChar });

        // 判定
        assertTrue(Boolean.class.cast(result));

        assertEquals(1, encodingCache.size());
        assertTrue(encodingCache.containsKey(fileEncoding));

        Map<Character, Boolean> shiftJISCacheMap = encodingCache
                .get(fileEncoding);
        assertEquals(1, shiftJISCacheMap.size());
        assertTrue(shiftJISCacheMap.containsKey(checkChar));
        assertTrue(Boolean.class.cast(shiftJISCacheMap.get(checkChar)));

        assertEquals(2, VMOUTUtil.getCallCount(Map.class, "put"));

        // 試験対象初期化
        encodingCache.clear();
    }

    /**
     * testIsHalfWidthChar02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) fileEncoding:"Shift_JIS"<br>
     * (引数) checkChar:','<br>
     * (状態) encodingCache:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："UTF-8"<br>
     * value：以下の要素を持つConcurrentHashMapインスタンス<br>
     * - key：',' | value：TRUE<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) encodingCache:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："UTF-8"<br>
     * value：以下の要素を持つConcurrentHashMapインスタンス<br>
     * - key：',' | value：TRUE<br>
     * 以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："Shift_JIS"<br>
     * value：以下の要素を持つConcurrentHashMapインスタンス<br>
     * - key：',' | value：TRUE<br>
     * (状態変化) Map#put():2回呼ばれる<br>
     * <br>
     * 正常ケース<br>
     * (キャッシュあり、エンコーディングに対するキャッシュなし)<br>
     * エンコーディングに合う半角文字が入力された場合、TRUEが返されることを確認する。<br>
     * また、その情報がキャッシュに残ることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testIsHalfWidthChar02() throws Exception {
        // 前処理(引数)
        String fileEncoding = "Shift_JIS";
        char checkChar = ',';

        // 前処理(状態)
        Field field = FileDAOUtility.class.getDeclaredField("encodingCache");
        field.setAccessible(true);
        Map<String, Map<Character, Boolean>> encodingCache =
                (Map<String, Map<Character, Boolean>>) field.get(FileDAOUtility.class); 
        encodingCache.clear();

        Map<Character, Boolean> inputEncodingCache = new ConcurrentHashMap<Character, Boolean>();

        inputEncodingCache.put(checkChar, Boolean.TRUE);
        encodingCache.put("UTF-8", inputEncodingCache);

        VMOUTUtil.initialize();

        // テスト実施
        Method method = FileDAOUtility.class.getDeclaredMethod("isHalfWidthChar", new Class[] {
                String.class, char.class });
        method.setAccessible(true);
        Object result = method.invoke(FileDAOUtility.class, new Object[] { fileEncoding, checkChar });

        // 判定(戻り値)
        assertTrue(Boolean.class.cast(result));

        // 判定(encodingCache)
        assertEquals(2, encodingCache.size());

        // 判定(UTF-8)
        assertTrue(encodingCache.containsKey("UTF-8"));
        Map<Character, Boolean> uTF8CacheMap = encodingCache.get("UTF-8");
        assertEquals(1, uTF8CacheMap.size());
        assertTrue(uTF8CacheMap.containsKey(checkChar));
        assertTrue(Boolean.class.cast(uTF8CacheMap.get(checkChar)));

        // 判定(Shif_JIS)
        assertTrue(encodingCache.containsKey(fileEncoding));
        Map<Character, Boolean> shiftJISCacheMap = encodingCache
                .get(fileEncoding);
        assertEquals(1, shiftJISCacheMap.size());
        assertTrue(shiftJISCacheMap.containsKey(checkChar));
        assertTrue(Boolean.class.cast(shiftJISCacheMap.get(checkChar)));

        assertEquals(2, VMOUTUtil.getCallCount(Map.class, "put"));
    }

    /**
     * testIsHalfWidthChar03() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) fileEncoding:"Shift_JIS"<br>
     * (引数) checkChar:','<br>
     * (状態) encodingCache:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："Shift_JIS"<br>
     * value：以下の要素を持つConcurrentHashMapインスタンス<br>
     * - key：',' | value：TRUE<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) encodingCache:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："Shift_JIS"<br>
     * value：以下の要素を持つConcurrentHashMapインスタンス<br>
     * - key：',' | value：TRUE<br>
     * (状態変化) Map#put():呼ばれない<br>
     * <br>
     * 正常ケース<br>
     * (キャッシュあり、エンコーディング・チェック文字に対するキャッシュあり)<br>
     * エンコーディングに合う半角文字が入力された場合、TRUEが返されることを確認する。<br>
     * また、その情報がキャッシュから取得されたことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testIsHalfWidthChar03() throws Exception {
        // 前処理(引数)
        String fileEncoding = "Shift_JIS";
        char checkChar = ',';

        // 前処理(状態)
        Field field = FileDAOUtility.class.getDeclaredField("encodingCache");
        field.setAccessible(true);
        Map<String, Map<Character, Boolean>> encodingCache =
                (Map<String, Map<Character, Boolean>>) field.get(FileDAOUtility.class); 
        encodingCache.clear();

        Map<Character, Boolean> inputEncodingCache = new ConcurrentHashMap<Character, Boolean>();

        inputEncodingCache.put(checkChar, Boolean.TRUE);
        encodingCache.put("Shift_JIS", inputEncodingCache);

        VMOUTUtil.initialize();
        // テスト実施
        Method method = FileDAOUtility.class.getDeclaredMethod("isHalfWidthChar", new Class[] {
                String.class, char.class });
        method.setAccessible(true);
        Object result = method.invoke(FileDAOUtility.class, new Object[] { fileEncoding, checkChar });

        // 判定(戻り値)
        assertTrue(Boolean.class.cast(result));

        // 判定(encodingCache)
        assertEquals(1, encodingCache.size());

        assertTrue(encodingCache.containsKey(fileEncoding));
        Map<Character, Boolean> shiftJISCacheMap = encodingCache
                .get(fileEncoding);
        assertEquals(1, shiftJISCacheMap.size());
        assertTrue(shiftJISCacheMap.containsKey(checkChar));
        assertTrue(Boolean.class.cast(shiftJISCacheMap.get(checkChar)));
        assertFalse(VMOUTUtil.isCalled(Map.class, "put"));
    }

    /**
     * testIsHalfWidthChar04() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) fileEncoding:"Shift_JIS"<br>
     * (引数) checkChar:','<br>
     * (状態) encodingCache:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："Shift_JIS"<br>
     * value：以下の要素を持つConcurrentHashMapインスタンス<br>
     * - key：'、' | value：FALSE<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) encodingCache:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："Shift_JIS"<br>
     * value：以下の要素を持つConcurrentHashMapインスタンス<br>
     * - key：'、' | value：FALSE<br>
     * - key：',' | value：TRUE<br>
     * (状態変化) Map#put():1回呼ばれる<br>
     * <br>
     * 正常ケース<br>
     * (キャッシュあり、エンコーディングに対するキャッシュはあるが、 チェック文字に対するキャッシュではない。)<br>
     * エンコーディングに合う半角文字が入力された場合、TRUEが返されることを確認する。<br>
     * また、その情報がキャッシュされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testIsHalfWidthChar04() throws Exception {
        // 前処理(引数)
        String fileEncoding = "Shift_JIS";
        char checkChar = ',';

        // 前処理(状態)
        Field field = FileDAOUtility.class.getDeclaredField("encodingCache");
        field.setAccessible(true);
        Map<String, Map<Character, Boolean>> encodingCache =
                (Map<String, Map<Character, Boolean>>) field.get(FileDAOUtility.class); 
        encodingCache.clear();

        Map<Character, Boolean> inputEncodingCache = new ConcurrentHashMap<Character, Boolean>();
        inputEncodingCache.put('、', Boolean.FALSE);
        encodingCache.put("Shift_JIS", inputEncodingCache);

        VMOUTUtil.initialize();

        // テスト実施
        Method method = FileDAOUtility.class.getDeclaredMethod("isHalfWidthChar", new Class[] {
                String.class, char.class });
        method.setAccessible(true);
        Object result = method.invoke(FileDAOUtility.class, new Object[] { fileEncoding, checkChar });

        // 判定(戻り値)
        assertTrue(Boolean.class.cast(result));

        // 判定(encodingCache)
        assertEquals(1, encodingCache.size());
        assertTrue(encodingCache.containsKey(fileEncoding));

        Map<Character, Boolean> shiftJISCacheMap = encodingCache
                .get(fileEncoding);
        assertEquals(2, shiftJISCacheMap.size());
        assertTrue(shiftJISCacheMap.containsKey(checkChar));
        assertFalse(Boolean.class.cast(shiftJISCacheMap.get('、')));
        assertTrue(Boolean.class.cast(shiftJISCacheMap.get(checkChar)));

        assertEquals(1, VMOUTUtil.getCallCount(Map.class, "put"));
    }

    /**
     * testIsHalfWidthChar05() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, D, E <br>
     * <br>
     * 入力値：(引数) fileEncoding:"Shift_JIS"<br>
     * (引数) checkChar:'、'<br>
     * (状態) encodingCache:要素を持たないConcurrentHashMapインスタンス<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) encodingCache:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："Shift_JIS"<br>
     * value：以下の要素を持つConcurrentHashMapインスタンス<br>
     * - key：'、' | value：FALSE<br>
     * (状態変化) Map#put():2回呼ばれる<br>
     * <br>
     * 正常ケース<br>
     * (キャッシュなし)<br>
     * エンコーディングに合う全角文字が入力された場合、FALSEが返されることを確認する。<br>
     * また、その情報がキャッシュに残ることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testIsHalfWidthChar05() throws Exception {
        // 前処理(引数)
        String fileEncoding = "Shift_JIS";
        char checkChar = '、';

        // 前処理(状態)
        Field field = FileDAOUtility.class.getDeclaredField("encodingCache");
        field.setAccessible(true);
        Map<String, Map<Character, Boolean>> encodingCache =
                (Map<String, Map<Character, Boolean>>) field.get(FileDAOUtility.class); 
        encodingCache.clear();

        // テスト実施
        Method method = FileDAOUtility.class.getDeclaredMethod("isHalfWidthChar", new Class[] {
                String.class, char.class });
        method.setAccessible(true);
        Object result = method.invoke(FileDAOUtility.class, new Object[] { fileEncoding, checkChar });

        // 判定(戻り値)
        assertFalse(Boolean.class.cast(result));

        // 判定(encodingCache)
        assertEquals(1, encodingCache.size());
        assertTrue(encodingCache.containsKey(fileEncoding));

        Map<Character, Boolean> shiftJISCacheMap = encodingCache
                .get(fileEncoding);
        assertEquals(1, shiftJISCacheMap.size());
        assertTrue(shiftJISCacheMap.containsKey(checkChar));
        assertFalse(Boolean.class.cast(shiftJISCacheMap.get(checkChar)));

        assertEquals(2, VMOUTUtil.getCallCount(Map.class, "put"));

        // 試験対象初期化
        encodingCache.clear();
    }

    /**
     * testIsHalfWidthChar06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileEncoding:"XXX"<br>
     * (引数) checkChar:','<br>
     * (状態) encodingCache:要素を持たないConcurrentHashMapインスタンス<br>
     * <br>
     * 期待値：(状態変化) encodingCache:以下の要素を持つConcurrentHashMapインスタンス<br>
     * ・key："Shift_JIS"<br>
     * value：要素を持たないConcurrentHashMapインスタンス<br>
     * (状態変化) Map#put():1回呼ばれる<br>
     * (状態変化) -:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："Specified Encoding : XXX is not supported"<br>
     * ・原因例外：UnsupportedEncodingException<br>
     * <br>
     * 異常ケース<br>
     * (キャッシュなし)<br>
     * 存在しないエンコーディングが入力された場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testIsHalfWidthChar06() throws Exception {
        // 前処理(引数)
        String fileEncoding = "XXX";
        char checkChar = ',';

        // 前処理(状態)
        Field field = FileDAOUtility.class.getDeclaredField("encodingCache");
        field.setAccessible(true);
        Map<String, Map<Character, Boolean>> encodingCache =
                (Map<String, Map<Character, Boolean>>) field.get(FileDAOUtility.class); 
        encodingCache.clear();

        try {
            // テスト実施
            Method method = FileDAOUtility.class.getDeclaredMethod("isHalfWidthChar", new Class[] {
                    String.class, char.class });
            method.setAccessible(true);
            Object result = method.invoke(FileDAOUtility.class, new Object[] { fileEncoding, checkChar });
            fail("FileExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定(例外)
            assertTrue(e.getTargetException() instanceof FileException);
            assertEquals("Specified Encoding : XXX is not supported", e.getTargetException()
                    .getMessage());
            assertTrue(e.getTargetException().getCause() instanceof UnsupportedEncodingException);

            // 判定(encodingCache)
            assertEquals(1, encodingCache.size());

            assertTrue(encodingCache.containsKey(fileEncoding));
            Map<Character, Boolean> shiftJISCacheMap = encodingCache
                    .get(fileEncoding);
            assertEquals(0, shiftJISCacheMap.size());
            assertEquals(1, VMOUTUtil.getCallCount(Map.class, "put"));
        }

        // 試験対象初期化
        encodingCache.clear();
    }

    /**
     * testIsHalfWidthChar07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileEncoding:null<br>
     * (引数) checkChar:','<br>
     * (状態) encodingCache:要素を持たないConcurrentHashMapインスタンス<br>
     * <br>
     * 期待値：(状態変化) encodingCache:要素を持たないConcurrentHashMapインスタンス<br>
     * (状態変化) Map#put():呼ばれない<br>
     * (状態変化) -:NullPointerExceptionが発生する。<br>
     * <br>
     * 異常ケース<br>
     * (キャッシュなし)<br>
     * エンコーディングがnullの場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testIsHalfWidthChar07() throws Exception {
        // 前処理(引数)
        String fileEncoding = null;
        char checkChar = ',';

        // 前処理(状態)
        Field field = FileDAOUtility.class.getDeclaredField("encodingCache");
        field.setAccessible(true);
        Map<String, Map<Character, Boolean>> encodingCache =
                (Map<String, Map<Character, Boolean>>) field.get(FileDAOUtility.class); 
        encodingCache.clear();

        try {
            // テスト実施
            Method method = FileDAOUtility.class.getDeclaredMethod("isHalfWidthChar", new Class[] {
                    String.class, char.class });
            method.setAccessible(true);
            Object result = method.invoke(FileDAOUtility.class, new Object[] { fileEncoding, checkChar });
            fail("NullPointerExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定(例外)
            assertTrue(e.getTargetException() instanceof NullPointerException);
            assertEquals(0, encodingCache.size());
            assertFalse(VMOUTUtil.isCalled(Map.class, "put"));
        }
    }
}
