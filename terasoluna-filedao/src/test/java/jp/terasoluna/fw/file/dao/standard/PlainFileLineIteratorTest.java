/*
 * $Id:$
 *
 * Copyright (c) 2006-2015 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.dao.FileLineException;
import jp.terasoluna.utlib.UTUtil;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.PlainFileLineIterator} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイル行オブジェクトを用いないファイル読込クラス。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.PlainFileLineIterator
 */
public class PlainFileLineIteratorTest {

    /**
     * testPlainFileLineIterator01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) PlainFileLineIterator01.txt<br>
     * 　データを持たないファイルのパス<br>
     * (引数) clazz:PlainFileLineIterator_Stub01<br>
     * 　@FileFormatの設定有り、すべて初期値。<br>
     * (引数) columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     * ・"java.lang.String"=NullColumnParserインスタンス<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineIterator#AbstractFileLineIterator():1回呼び出されることを確認する。<br>
     * 呼び出されるときの引数が、引数fileName,clazz,columnParserMapと同じインスタンスであること<br>
     * (状態変化) AbstractFileLineIterator#init():1回呼び出されること。<br>
     * <br>
     * 親クラスのコンストラクタが呼ばれ、親クラスのinitメソッドが実行されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testPlainFileLineIterator01() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタの試験なので不要

        // 引数の設定
        URL url = this.getClass().getResource("PlainFileLineIterator01.txt");
        String fileName = url.getPath();
        Class<PlainFileLineIterator_Stub01> clazz = PlainFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // 前提条件の設定
        // なし

        // テスト実施
        PlainFileLineIterator result = Mockito.spy(new PlainFileLineIterator(fileName, clazz, columnParserMap));

        // 返却値の確認
        // なし

        // 状態変化の確認
        // 判定
        assertEquals(fileName, UTUtil.getPrivateField(result, "fileName"));
        assertSame(clazz, UTUtil.getPrivateField(result, "clazz"));
        assertSame(columnParserMap, UTUtil.getPrivateField(result, "columnParserMap"));
    }

    /**
     * testSeparateColumns01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileLineString:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) 例外:UnSupportedOperationException<br>
     * メッセージ："separateColumns(String) isn't supported."<br>
     * <br>
     * このクラスではseparateColumnsをサポートしていない。よって、UnSupportedOperationExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSeparateColumns01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("PlainFileLineIterator01.txt");
        String fileName = url.getPath();
        Class<PlainFileLineIterator_Stub01> clazz = PlainFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        PlainFileLineIterator plainFileLineIterator = new PlainFileLineIterator(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "aaa";

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            plainFileLineIterator.separateColumns(fileLineString);
            fail("UnSupportedOperationExceptionがスローされませんでした");
        } catch (UnsupportedOperationException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            // 判定
            assertSame(UnsupportedOperationException.class, e.getClass());
            assertEquals("separateColumns(String) isn't supported.", e
                    .getMessage());
        }
    }

    /**
     * testNext01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) 対象ファイル:以下の内容を持つ"PlainFileLineIterator_next01.txt"ファイルが存在する。<br>
     * -------------------<br>
     * １行目データ<br>
     * -------------------<br>
     * ※特殊データ<br>
     * (状態) this.readTrailer:false<br>
     * <br>
     * 期待値：(戻り値) String:Stringインスタンス<br>
     * （readLineの結果）<br>
     * (状態変化) AbstractFileLineIterator#readLine():1回呼ばれる<br>
     * (状態変化) currentLineCount:1<br>
     * <br>
     * hasNextメッソドがTRUEになっている場合readLineメッソドが呼ばれることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("PlainFileLineIterator01.txt");
        String fileName = url.getPath();
        Class<PlainFileLineIterator_Stub01> clazz = PlainFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        PlainFileLineIterator plainFileLineIterator = Mockito.spy(new PlainFileLineIterator(
                fileName, clazz, columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // なし

        // テスト実施
        String result = plainFileLineIterator.next();

        // 返却値の確認
        assertEquals("1行目データ", result);

        // 状態変化の確認
        Mockito.verify(plainFileLineIterator).readLine();
        assertEquals(1, UTUtil.getPrivateField(plainFileLineIterator,
                "currentLineCount"));
    }

    /**
     * testNext02() <br>
     * <br>
     * (異常系) <br>
     * 観点：F.G <br>
     * <br>
     * 入力値：(状態) 対象ファイル:以下の内容を持つ"PlainFileLineIterator_next02.txt"ファイルが存在する。<br>
     * -------------------<br>
     * 空<br>
     * -------------------<br>
     * ※特殊データ<br>
     * (状態) this.readTrailer:false<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineIterator#readLine():呼ばれない<br>
     * (状態変化) currentLineCount:0<br>
     * (状態変化) 例外:以下の情報を持つFileLineExceptionが発生することを確認する。<br>
     * ・メッセージ："The data which can be acquired doesn't exist."<br>
     * ・原因例外：NoSuchElementException<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * ・行番号：0<br>
     * ・カラム名：null<br>
     * ・カラムインデックス：-1<br>
     * <br>
     * hasNextメッソドがFALSEになっている場合<br>
     * NoSuchElementExceptionがスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<PlainFileLineIterator_Stub01> clazz = PlainFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        PlainFileLineIterator plainFileLineIterator = Mockito.spy(new PlainFileLineIterator(
                fileName, clazz, columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            plainFileLineIterator.next();
            fail("FileLineExceptionがスローされませんでした");
        } catch (FileLineException e) {
            // 返却値の確認
            // なし

            assertSame(FileLineException.class, e.getClass());
            assertEquals("The data which can be acquired doesn't exist.", e
                    .getMessage());
            assertSame(NoSuchElementException.class, e.getCause().getClass());
            assertEquals(fileName, e.getFileName());
            assertEquals(1, e.getLineNo());
            assertNull(e.getColumnName());
            assertEquals(-1, e.getColumnIndex());

            // 状態変化の確認
            Mockito.verify(plainFileLineIterator, Mockito.never()).readLine();
            assertEquals(0, UTUtil.getPrivateField(plainFileLineIterator,
                    "currentLineCount"));

        }
    }

    /**
     * testNext03() <br>
     * <br>
     * (異常系) <br>
     * 観点：F.G <br>
     * <br>
     * 入力値：(状態) 対象ファイル:以下の内容を持つ"PlainFileLineIterator_next03.txt"ファイルが存在する。<br>
     * -------------------<br>
     * 1行目データ<br>
     * 2行目データ<br>
     * -------------------<br>
     * ※特殊データ<br>
     * (状態) this.readTrailer:true<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineIterator#readLine():呼ばれない<br>
     * (状態変化) currentLineCount:0<br>
     * (状態変化) 例外:以下の情報を持つFileLineExceptionが発生することを確認する。<br>
     * ・メッセージ："Data part should be called before trailer part."<br>
     * ・原因例外：IllegalStateException<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * ・行番号：0<br>
     * <br>
     * hasNextメッソドがFALSEになっている場合<br>
     * NoSuchElementExceptionがスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("PlainFileLineIterator03.txt");
        String fileName = url.getPath();
        Class<PlainFileLineIterator_Stub03> clazz = PlainFileLineIterator_Stub03.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        PlainFileLineIterator plainFileLineIterator = Mockito.spy(new PlainFileLineIterator(
                fileName, clazz, columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        UTUtil.setPrivateField(plainFileLineIterator, "readTrailer", true);

        // テスト実施
        try {
            plainFileLineIterator.next();
            fail("FileLineExceptionがスローされませんでした");
        } catch (FileLineException e) {
            // 返却値の確認
            // なし

            assertSame(FileLineException.class, e.getClass());
            assertEquals("Data part should be called before trailer part.", e
                    .getMessage());
            assertSame(IllegalStateException.class, e.getCause().getClass());
            assertEquals(fileName, e.getFileName());
            assertEquals(0, e.getLineNo());

            // 状態変化の確認
            Mockito.verify(plainFileLineIterator, Mockito.never()).readLine();
            assertEquals(0, UTUtil.getPrivateField(plainFileLineIterator,
                    "currentLineCount"));

        }
    }

    /**
     * 正常系<br>
     * FileFormatのencloseCharとdelimiterが設定されていても、無視する
     * @throws Exception
     */
    @Test
    public void testNext04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        PlainFileLineIterator fileLineIterator = new PlainFileLineIterator(
                fileName, PlainFileLineIterator_Stub02.class, columnParserMap);

        // テスト実施
        String result1 = fileLineIterator.next();
        String result2 = fileLineIterator.next();
        String result3 = fileLineIterator.next();

        // 返却値の確認
        assertEquals("\"1\",22,333,|4444|", result1);
        assertEquals("\"5\",66,777,|8888|", result2);
        assertEquals("\"9\",AA,BBB,|CCCC|", result3);
    }

    /**
     * testSkipint01() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(引数) skipLines:0<br>
     * (状態) 対象ファイル:以下の内容を持つ"PlainFileLineIterator_skip01.txt"ファイルが存在する。<br>
     * -------------------<br>
     * 3行目データ<br>
     * -------------------<br>
     * (状態) this.readLine():正常実行<br>
     * (状態) this.currentLineCount:0<br>
     * <br>
     * 期待値：(状態変化) this.readLine:呼ばれない<br>
     * (状態変化) this.currentLineCount:0<br>
     * <br>
     * 正常パターン。<br>
     * Skip対象行がない場合、そのまま正常終了することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSkipint01() throws Exception {
        // テスト対象のインスタンス化
        URL url = PlainFileLineIteratorTest.class
                .getResource("PlainFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        PlainFileLineIterator fileLineIterator = Mockito.spy(new PlainFileLineIterator(
                fileName, PlainFileLineIterator_Stub01.class, columnParserMap));
        UTUtil.setPrivateField(fileLineIterator, "currentLineCount", 0);

        // 引数の設定
        int skipLines = 0;

        // 前提条件の設定
        fileLineIterator.init();
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        fileLineIterator.skip(skipLines);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Mockito.verify(fileLineIterator, Mockito.never()).readLine();
//        assertEquals(0, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
//                "readLine"));
        assertEquals(0, UTUtil.getPrivateField(fileLineIterator,
                "currentLineCount"));
    }

    /**
     * testSkipint02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(引数) skipLines:1<br>
     * (状態) 対象ファイル:以下の内容を持つ"PlainFileLineIterator_skip01.txt"ファイルが存在する。<br>
     * -------------------<br>
     * 3行目データ<br>
     * -------------------<br>
     * (状態) this.readLine():正常実行<br>
     * (状態) this.currentLineCount:0<br>
     * <br>
     * 期待値：(状態変化) this.readLine:1回呼ばれる<br>
     * (状態変化) this.currentLineCount:1<br>
     * <br>
     * 正常パターン。<br>
     * Skip対象行が１行の場合、対象データを１行読むことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSkipint02() throws Exception {
        // テスト対象のインスタンス化
        URL url = AbstractFileLineIteratorTest.class
                .getResource("PlainFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        PlainFileLineIterator fileLineIterator = Mockito.spy(new PlainFileLineIterator(
                fileName, PlainFileLineIterator_Stub01.class, columnParserMap));
        UTUtil.setPrivateField(fileLineIterator, "currentLineCount", 0);

        // 引数の設定
        int skipLines = 1;

        // 前提条件の設定
        fileLineIterator.init();
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        fileLineIterator.skip(skipLines);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Mockito.verify(fileLineIterator).readLine();
//        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
//                "readLine"));
        assertEquals(1, UTUtil.getPrivateField(fileLineIterator,
                "currentLineCount"));
    }

    /**
     * testSkipint03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(引数) skipLines:3<br>
     * (状態) 対象ファイル:以下の内容を持つ"PlainFileLineIterator_skip01.txt"ファイルが存在する。<br>
     * -------------------<br>
     * 3行目データ<br>
     * -------------------<br>
     * (状態) this.readLine():正常実行<br>
     * (状態) this.currentLineCount:0<br>
     * <br>
     * 期待値：(状態変化) this.readLine:3回呼ばれる<br>
     * (状態変化) this.currentLineCount:3<br>
     * <br>
     * 正常パターン。<br>
     * Skip対象行が３行の場合、対象データを３行読むことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSkipint03() throws Exception {
        // テスト対象のインスタンス化
        URL url = AbstractFileLineIteratorTest.class
                .getResource("PlainFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        PlainFileLineIterator fileLineIterator = Mockito.spy(new PlainFileLineIterator(
                fileName, PlainFileLineIterator_Stub01.class, columnParserMap));
        UTUtil.setPrivateField(fileLineIterator, "currentLineCount", 0);

        // 引数の設定
        int skipLines = 3;

        // 前提条件の設定
        fileLineIterator.init();
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        fileLineIterator.skip(skipLines);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Mockito.verify(fileLineIterator, Mockito.times(3)).readLine();
//        assertEquals(3, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
//                "readLine"));
        assertEquals(3, UTUtil.getPrivateField(fileLineIterator,
                "currentLineCount"));
    }

    /**
     * testSkipint04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) skipLines:1<br>
     * (状態) 対象ファイル:以下の内容を持つ"PlainFileLineIterator_skip01.txt"ファイルが存在する。<br>
     * -------------------<br>
     * 3行目データ<br>
     * -------------------<br>
     * (状態) this.readLine():FileException例外を発生する。<br>
     * (状態) this.currentLineCount:0<br>
     * <br>
     * 期待値：(状態変化) this.currentLineCount:0<br>
     * (状態変化) なし:this.readLine()で発生したFileExceptionがそのままスローされることを確認する。<br>
     * <br>
     * 例外。<br>
     * 対象データを読む処理で例外が発生した場合、その例外がそのまま返されることを確認する。。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSkipint04() throws Exception {
        // テスト対象のインスタンス化
        URL url = AbstractFileLineIteratorTest.class
                .getResource("PlainFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        PlainFileLineIterator fileLineIterator = Mockito.spy(new PlainFileLineIterator(
                fileName, PlainFileLineIterator_Stub01.class, columnParserMap));
        UTUtil.setPrivateField(fileLineIterator, "currentLineCount", 0);

        // 引数の設定
        int skipLines = 1;

        // 前提条件の設定
        fileLineIterator.init();
        FileException exception = new FileException("readLineからの例外です");
        Mockito.doThrow(exception).when(fileLineIterator).readLine();
//        VMOUTUtil.setExceptionAtAllTimes(AbstractFileLineIterator.class,
//                "readLine", exception);
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        try {
            fileLineIterator.skip(skipLines);
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 例外の確認
            assertSame(exception, e);

            // 状態変化の確認
            assertEquals(0, UTUtil.getPrivateField(fileLineIterator,
                    "currentLineCount"));
        }
    }

    /**
     * testSkipint05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) skipLines:100<br>
     * ※処理対象ファイルの行数を超える設定<br>
     * (状態) 対象ファイル:以下の内容を持つ"PlainFileLineIterator_skip01.txt"ファイルが存在する。<br>
     * -------------------<br>
     * 3行目データ<br>
     * -------------------<br>
     * (状態) this.readLine():正常実行<br>
     * (状態) this.currentLineCount:0<br>
     * <br>
     * 期待値：(状態変化) this.readLine:3回呼ばれる<br>
     * (状態変化) this.currentLineCount:3<br>
     * (状態変化) なし:以下の設定を持つFileLineExceptionが発生する。<br>
     * ・メッセージ："The data which can be acquired doesn't exist."<br>
     * ・原因例外：NoSuchElementException<br>
     * ・ファイル名：処理対象ファイル名<br>
     * ・行番号：4<br>
     * <br>
     * 例外。<br>
     * Skip対象行の数が対象データの数を越える場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSkipint05() throws Exception {
        // テスト対象のインスタンス化
        URL url = AbstractFileLineIteratorTest.class
                .getResource("PlainFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        PlainFileLineIterator fileLineIterator = Mockito.spy(new PlainFileLineIterator(
                fileName, PlainFileLineIterator_Stub01.class, columnParserMap));
        UTUtil.setPrivateField(fileLineIterator, "currentLineCount", 0);

        // 引数の設定
        int skipLines = 100;

        // 前提条件の設定
        fileLineIterator.init();

        // テスト実施
        try {
            fileLineIterator.skip(skipLines);
            fail("FileLineExceptionがスローされませんでした");
        } catch (FileLineException e) {
            // 例外の確認
            assertEquals("The data which can be acquired doesn't exist.", e
                    .getMessage());
            assertTrue(NoSuchElementException.class.isAssignableFrom(e
                    .getCause().getClass()));
            assertEquals(fileName, e.getFileName());
            assertEquals(4, e.getLineNo());

            // 状態変化の確認
            Mockito.verify(fileLineIterator, Mockito.times(3)).readLine();
//            assertEquals(3, VMOUTUtil.getCallCount(
//                    AbstractFileLineIterator.class, "readLine"));
            assertEquals(3, UTUtil.getPrivateField(fileLineIterator,
                    "currentLineCount"));
        }
    }

    /**
     * testGetDelimiter01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) delimiter:'#'<br>
     * <br>
     * 期待値：(戻り値) char:','<br>
     * <br>
     * delimiterのgetterが正常に動作することを確認する。<br>
     * 必ず','が返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetDelimiter01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("PlainFileLineIterator01.txt");
        String fileName = url.getPath();
        Class<PlainFileLineIterator_Stub02> clazz = PlainFileLineIterator_Stub02.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        PlainFileLineIterator plainFileLineIterator = new PlainFileLineIterator(
                fileName, clazz, columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        char result = plainFileLineIterator.getDelimiter();

        // 返却値の確認
        assertEquals(',', result);

        // 状態変化の確認
        // なし
    }

    /**
     * testGetEncloseChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) encloseChar:'\"'<br>
     * <br>
     * 期待値：(戻り値) char:Character.MIN_VALUE<br>
     * <br>
     * encloseCharのgetterが正常に動作することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetEncloseChar01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("PlainFileLineIterator01.txt");
        String fileName = url.getPath();
        Class<PlainFileLineIterator_Stub02> clazz = PlainFileLineIterator_Stub02.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        PlainFileLineIterator plainFileLineIterator = new PlainFileLineIterator(
                fileName, clazz, columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        char result = plainFileLineIterator.getEncloseChar();

        // 返却値の確認
        assertEquals(Character.MIN_VALUE, result);

        // 状態変化の確認
        // なし
    }

    /**
     * testGetCurrentLineCount01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) currentLineCount:5<br>
     * <br>
     * 期待値：(戻り値) int:5<br>
     * <br>
     * currentLineCountのgetterが正常に動作することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetCurrentLineCount01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("PlainFileLineIterator01.txt");
        String fileName = url.getPath();
        Class<PlainFileLineIterator_Stub02> clazz = PlainFileLineIterator_Stub02.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        PlainFileLineIterator plainFileLineIterator = new PlainFileLineIterator(
                fileName, clazz, columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        UTUtil.setPrivateField(plainFileLineIterator, "currentLineCount", 5);
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        int result = plainFileLineIterator.getCurrentLineCount();

        // 返却値の確認
        assertEquals(5, result);

        // 状態変化の確認
        // なし
    }

    /**
     * testGetTrailer01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) readTrailer:false<br>
     * <br>
     * 期待値：(戻り値) List<String>:super#getTrailer()の戻り値<br>
     * トレイラデータ<br>
     * (状態変化) readTrailer:true<br>
     * <br>
     * getTrailer()を呼ぶことによって、トレイラデータを取得し、トレイラ部処理確認用フラグがTrueに変化することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetTrailer01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("PlainFileLineIterator03.txt");
        String fileName = url.getPath();
        Class<PlainFileLineIterator_Stub03> clazz = PlainFileLineIterator_Stub03.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        PlainFileLineIterator plainFileLineIterator = new PlainFileLineIterator(
                fileName, clazz, columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        List<String> trailer = plainFileLineIterator.getTrailer();

        // 返却値の確認
        assertEquals(1, trailer.size());
        assertEquals("トレイラデータ", trailer.get(0));

        // 状態変化の確認
        assertTrue((Boolean) UTUtil.getPrivateField(plainFileLineIterator,
                "readTrailer"));
    }
}
