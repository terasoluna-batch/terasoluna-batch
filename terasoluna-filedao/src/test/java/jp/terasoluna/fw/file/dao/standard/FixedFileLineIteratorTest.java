/*
 * $Id: FixedFileLineIteratorTest.java 5354 2007-10-03 06:06:25Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import jp.terasoluna.fw.file.annotation.InputFileColumn;
import jp.terasoluna.fw.file.annotation.NullStringConverter;
import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.ut.VMOUTUtil;
import jp.terasoluna.utlib.UTUtil;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.FixedFileLineIterator} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> 固定長ファイル用のファイルアクセス(データ取得)クラス。
 * <p>
 * @author 奥田哲司
 * @author 趙俸徹
 * @see jp.terasoluna.fw.file.dao.standard.FixedFileLineIterator
 */
public class FixedFileLineIteratorTest {

    /**
     * 初期化処理を行う。
     */
    @Before
    public void setUp() {
        VMOUTUtil.initialize();
    }

    /**
     * testFixedFileLineIterator01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:"aaa.txt"<br>
     * (引数) clazz:以下の設定を持つFileFormatアノテーションを持つスタブ<br>
     * FixedFileLineIterator_Stub01<br>
     * 　 アノテーションFileFormat：初期値<br>
     * (引数) columnParserMap:以下の設定を持つHashMapのインスタンス<br>
     * 要素1<br>
     * key:"java.lang.String"<br>
     * value:ColumnParserインスタンス<br>
     * FixedFileLineWriter_ColumnParserStub01インスタンス<br>
     * 空実装<br>
     * (状態) totalDefineBytes:0<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineIteratorコンストラクタ:1回呼ばれる。<br>
     * 引数と同じインスタンスが渡される。<br>
     * (状態変化) AbstractFileLineIterator#init():1回呼ばれる。<br>
     * <br>
     * 正常にコンストラクタの処理が行われることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testFixedFileLineIterator01() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub01> clazz = FixedFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser parser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", parser);
        columnParserMap.put("java.util.Date", parser);
        columnParserMap.put("java.math.BigDecimal", parser);
        columnParserMap.put("java.lang.int", parser);

        // 前提条件なし

        // テスト実施
        new FixedFileLineIterator<FixedFileLineIterator_Stub01>(fileName,
                clazz, columnParserMap);

        // 返却値なし

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
                "<init>"));
        List arguments = VMOUTUtil.getArguments(AbstractFileLineIterator.class,
                "<init>", 0);
        assertEquals(fileName, arguments.get(0));
        assertEquals(clazz, arguments.get(1));
        assertSame(columnParserMap, arguments.get(2));
        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
                "init"));
    }

    /**
     * testFixedFileLineIterator02() <br>
     * <br>
     * (正常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileName:"aaa.txt"<br>
     * (引数) clazz:以下の設定を持つFileFormatアノテーションを持つスタブ<br>
     * FixedFileLineIterator_Stub05<br>
     * 　アノテーションFileFormat：delimiterが初期値以外<br>
     * @FileFormat(delimiter='、')<br> (引数) columnParserMap:以下の設定を持つHashMapのインスタンス<br>
     *                                要素1<br>
     *                                key:"java.lang.String"<br>
     *                                value:ColumnParserインスタンス<br>
     *                                FixedFileLineWriter_ColumnParserStub01インスタンス<br>
     *                                空実装<br>
     *                                (状態) totalDefineBytes:0<br>
     * <br>
     *                                期待値：(状態変化) AbstractFileLineIteratorコンストラクタ:1回呼ばれる。<br>
     *                                引数と同じインスタンスが渡される。<br>
     *                                (状態変化) AbstractFileLineIterator#init():呼ばれない。<br>
     *                                (状態変化)
     *                                なし:"Delimiter can not change."のメッセージ、IllegalStateException、ファイル名を持つFileExceptionが発生する。<br>
     * <br>
     *                                例外。@FileFormatのdelimiterに初期値以外を設定した場合、例外が発生することを確認する。<br>
     *                                ファイル名が入力値のfileNameに一致することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testFixedFileLineIterator02() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub05> clazz = FixedFileLineIterator_Stub05.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser parser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", parser);
        columnParserMap.put("java.util.Date", parser);
        columnParserMap.put("java.math.BigDecimal", parser);
        columnParserMap.put("java.lang.int", parser);

        // 前提条件なし

        // テスト実施
        try {
            new FixedFileLineIterator<FixedFileLineIterator_Stub05>(fileName,
                    clazz, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(
                    AbstractFileLineIterator.class, "<init>"));
            List arguments = VMOUTUtil.getArguments(
                    AbstractFileLineIterator.class, "<init>", 0);
            assertEquals(fileName, arguments.get(0));
            assertEquals(clazz, arguments.get(1));
            assertSame(columnParserMap, arguments.get(2));
            assertEquals(IllegalStateException.class, e.getCause().getClass());
            assertEquals("Delimiter can not change.", e.getMessage());
            assertEquals(fileName, e.getFileName());
        }
    }

    /**
     * testFixedFileLineIterator03() <br>
     * <br>
     * (正常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileName:"aaa.txt"<br>
     * (引数) clazz:以下の設定を持つFileFormatアノテーションを持つスタブ<br>
     * FixedFileLineIterator_Stub06<br>
     * 　アノテーションFileFormat：encloseCharが初期値以外<br>
     * @FileFormat(encloseChar='"')<br> (引数) columnParserMap:以下の設定を持つHashMapのインスタンス<br>
     *                                  要素1<br>
     *                                  key:"java.lang.String"<br>
     *                                  value:ColumnParserインスタンス<br>
     *                                  FixedFileLineWriter_ColumnParserStub01インスタンス<br>
     *                                  空実装<br>
     *                                  (状態) totalDefineBytes:0<br>
     * <br>
     *                                  期待値：(状態変化) AbstractFileLineIteratorコンストラクタ:1回呼ばれる。<br>
     *                                  引数と同じインスタンスが渡される。<br>
     *                                  (状態変化) AbstractFileLineIterator#init():呼ばれない。<br>
     *                                  (状態変化)
     *                                  なし:"EncloseChar can not change."のメッセージ、IllegalStateException、ファイル名を持つFileExceptionが発生する。<br>
     * <br>
     *                                  例外。@FileFormatのencloseCharに初期値以外を設定した場合、例外が発生することを確認する。<br>
     *                                  ファイル名が入力値のfileNameに一致することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testFixedFileLineIterator03() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub06> clazz = FixedFileLineIterator_Stub06.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser parser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", parser);
        columnParserMap.put("java.util.Date", parser);
        columnParserMap.put("java.math.BigDecimal", parser);
        columnParserMap.put("java.lang.int", parser);

        // 前提条件なし

        // テスト実施
        try {
            new FixedFileLineIterator<FixedFileLineIterator_Stub06>(fileName,
                    clazz, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(
                    AbstractFileLineIterator.class, "<init>"));
            List arguments = VMOUTUtil.getArguments(
                    AbstractFileLineIterator.class, "<init>", 0);
            assertEquals(fileName, arguments.get(0));
            assertEquals(clazz, arguments.get(1));
            assertSame(columnParserMap, arguments.get(2));
            assertEquals(IllegalStateException.class, e.getCause().getClass());
            assertEquals("EncloseChar can not change.", e.getMessage());
            assertEquals(fileName, e.getFileName());
        }
    }

    /**
     * testFixedFileLineIterator04() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:"aaa.txt"<br>
     * (引数) clazz:以下の設定を持つFileFormatアノテーションを持つスタブ<br>
     * FixedFileLineIterator_Stub02<br>
     * 　 アノテーションFileFormat：初期値<br>
     * 属性：<br>
     * 　　　@InputFileColumn(columnIndex = 1, bytes = 5)<br>
     * 　　　String column1;<br>
     * 　　　@InputFileColumn(columnIndex = 1, bytes = 3)<br>
     * 　　　String column2;<br>
     * (引数) columnParserMap:以下の設定を持つHashMapのインスタンス<br>
     * 要素1<br>
     * key:"java.lang.String"<br>
     * value:ColumnParserインスタンス<br>
     * FixedFileLineWriter_ColumnParserStub01インスタンス<br>
     * 空実装<br>
     * (状態) totalDefineBytes:0<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineIteratorコンストラクタ:1回呼ばれる。<br>
     * 引数と同じインスタンスが渡される。<br>
     * (状態変化) AbstractFileLineIterator#init():1回呼ばれる。<br>
     * <br>
     * 正常にコンストラクタの処理が行われることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testFixedFileLineIterator04() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub02> clazz = FixedFileLineIterator_Stub02.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser parser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", parser);
        columnParserMap.put("java.util.Date", parser);
        columnParserMap.put("java.math.BigDecimal", parser);
        columnParserMap.put("java.lang.int", parser);

        // 前提条件なし

        // テスト実施
        new FixedFileLineIterator<FixedFileLineIterator_Stub02>(fileName,
                clazz, columnParserMap);

        // 返却値なし

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
                "<init>"));
        List arguments = VMOUTUtil.getArguments(AbstractFileLineIterator.class,
                "<init>", 0);
        assertEquals(fileName, arguments.get(0));
        assertEquals(clazz, arguments.get(1));
        assertSame(columnParserMap, arguments.get(2));
        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
                "init"));
    }

    /**
     * 異常系<br>
     * ファイル行オブジェクトにInputFileColumnアノテーションが無し
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFixedFileLineIterator05() throws Exception {
        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<FileLineObject_Empty> clazz = FileLineObject_Empty.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);

        // テスト実施
        try {
            new FixedFileLineIterator<FileLineObject_Empty>(fileName, clazz,
                    columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals("InputFileColumn is not found.", e.getMessage());
            assertEquals(fileName, e.getFileName());
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    /**
     * 異常系<br>
     * InputFileColumnにEncloseCharが設定されている場合、エラーとする
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFixedFileLineIterator06() throws Exception {
        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);

        // テスト実施
        try {
            new FixedFileLineIterator<CSVFileLine_Stub01>(fileName,
                    CSVFileLine_Stub01.class, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals("columnEncloseChar can not change.", e.getMessage());
            assertEquals(fileName, e.getFileName());
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    /**
     * 異常系<br>
     * 行区切り無しの固定長形式ファイルでヘッダ行数を指定した場合、<br>
     * 異常終了する事を確認する。
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFixedFileLineIterator07() throws Exception {
        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);

        // テスト実施
        try {
            new FixedFileLineIterator<FixedFileLine_Stub03>(fileName,
                    FixedFileLine_Stub03.class, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals("HeaderLineCount or trailerLineCount cannot be used.",
                    e.getMessage());
            assertEquals(fileName, e.getFileName());
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    /**
     * 異常系<br>
     * 行区切り無しの固定長形式ファイルでトレイラ行数を指定した場合、<br>
     * 異常終了する事を確認する。
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFixedFileLineIterator08() throws Exception {
        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);

        // テスト実施
        try {
            new FixedFileLineIterator<FixedFileLine_Stub04>(fileName,
                    FixedFileLine_Stub04.class, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals("HeaderLineCount or trailerLineCount cannot be used.",
                    e.getMessage());
            assertEquals(fileName, e.getFileName());
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    /**
     * testSeparateColumns01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) fileLineString:null<br>
     * <br>
     * 期待値：(戻り値) String[]:new String[0]<br>
     * <br>
     * 正常パターン。<br>
     * nullが引数として渡された場合、要素数0の配列を返却することを確認する。<br>
     * 通常の処理でこの返却値が戻ることはない。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSeparateColumns01() throws Exception {
        // テスト対象のインスタンス化
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub01> clazz = FixedFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser parser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", parser);
        columnParserMap.put("java.util.Date", parser);
        columnParserMap.put("java.math.BigDecimal", parser);
        columnParserMap.put("java.lang.int", parser);
        FixedFileLineIterator<FixedFileLineIterator_Stub01> lineIterator = new FixedFileLineIterator<FixedFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = null;

        // 前提条件なし

        // テスト実施
        String[] columns = lineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(0, columns.length);

        // 状態変化なし
    }

    /**
     * testSeparateColumns02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileLineString:"aaa"<br>
     * (状態) this.fields[]:配列要素が0<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegaｌStateExceptionが発生。FileExceptionにラップされることを確認する。<br>
     * ファイル名が入力値のfileNameに一致することを確認する。<br>
     * メッセージ："Total Columns byte is different from Total FileLineObject's columns byte."<br>
     * <br>
     * 例外。アノテーションで設定したバイト数の合計と、ファイルの1行あたりのバイト数が異なる場合、例外が発生することを確認する。<br>
     * ファイル行オブジェクトのインスタンス変数がない場合。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSeparateColumns02() throws Exception {
        // テスト対象のインスタンス化
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub01> clazz = FixedFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser parser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", parser);
        columnParserMap.put("java.util.Date", parser);
        columnParserMap.put("java.math.BigDecimal", parser);
        columnParserMap.put("java.lang.int", parser);
        FixedFileLineIterator<FixedFileLineIterator_Stub01> lineIterator = new FixedFileLineIterator<FixedFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "aaa";

        // 前提条件(インスタンス化でfileds[]に設定される)

        // テスト実施
        try {
            lineIterator.separateColumns(fileLineString);
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalStateException.class, e.getCause().getClass());
            assertEquals(fileName, e.getFileName());
            String message = "Total Columns byte is different from "
                    + "Total FileLineObject's columns byte.";
            assertEquals(message, e.getMessage());
        }
    }

    /**
     * testSeparateColumns03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"12345"<br>
     * (状態) this.fields[]:配列要素が1<br>
     * (状態) this.field[]のアノテーションInputFileColumn:@InputFileColumn(columnIndex = 0, bytes = 5, stringConverter =
     * StringConverterToUpperCase.class, trimChar = '0', trimType = TrimType.LEFT)<br>
     * private String shopId = null;<br>
     * (状態) AbstractFileLineIterator.fileEncoding:システムデフォルト<br>
     * <br>
     * 期待値：(戻り値) String[]:以下の要素を持つString配列<br>
     * 　要素1："12345"<br>
     * <br>
     * 正常パターン。配列要素1の文字列型配列を返却する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSeparateColumns03() throws Exception {
        // テスト対象のインスタンス化
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub03> clazz = FixedFileLineIterator_Stub03.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        FixedFileLineIterator<FixedFileLineIterator_Stub03> lineIterator = new FixedFileLineIterator<FixedFileLineIterator_Stub03>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "12345";

        // 前提条件(インスタンス化でfileds[]に設定される)

        // テスト実施
        String[] columns = lineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(1, columns.length);
        assertEquals("12345", columns[0]);

        // 状態変化なし
    }

    /**
     * testSeparateColumns04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileLineString:"12345"<br>
     * (状態) this.fields[]:配列要素が1<br>
     * (状態) this.field[]のアノテーションInputFileColumn:@InputFileColumn(columnIndex = 0, bytes = 5, stringConverter =
     * StringConverterToUpperCase.class, trimChar = '0', trimType = TrimType.LEFT)<br>
     * private String shopId = null;<br>
     * (状態) AbstractFileLineIterator.fileEncoding:存在しないエンコード<br>
     * <br>
     * 期待値：(状態変化) 例外:UnsupoortedEncodingExceptionが発生。FileExceptionにラップされることを確認する<br>
     * ファイル名が入力値のfileNameに一致することを確認する。<br>
     * メッセージ："fileEncoding which isn't supported was set."<br>
     * <br>
     * 例外。設定されているエンコードが存在しない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSeparateColumns04() throws Exception {
        // テスト対象のインスタンス化
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub03> clazz = FixedFileLineIterator_Stub03.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        FixedFileLineIterator<FixedFileLineIterator_Stub03> lineIterator = new FixedFileLineIterator<FixedFileLineIterator_Stub03>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "12345";

        // 前提条件の設定
        UTUtil.setPrivateField(lineIterator, "fileEncoding", "aaa");

        // テスト実施
        try {
            lineIterator.separateColumns(fileLineString);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(UnsupportedEncodingException.class, e.getCause()
                    .getClass());
            assertEquals(fileName, e.getFileName());
            assertEquals("fileEncoding which isn't supported was set.", e
                    .getMessage());
        }
    }

    /**
     * testSeparateColumns05() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"2006/12/10aaaaa123456789"<br>
     * (状態) this.fields[]:配列要素が3<br>
     * (状態) this.field[]のアノテーションInputFileColumn:@InputFileColumn(columnIndex = 0, bytes = 10, columnFormat = "yyyy/MM/dd")<br>
     * private Date hiduke = null;<br>
     * <br>
     * @InputFileColumn(columnIndex = 1, bytes = 5, stringConverter = StringConverterToUpperCase.class, trimChar = '0', trimType
     *                              = TrimType.LEFT)<br>
     *                              private String shopId = null;<br>
     * <br>
     * @InputFileColumn(columnIndex = 2, bytes = 9, columnFormat = "#########")<br>
     *                              private BigDecimal uriage = null;<br>
     *                              (状態) AbstractFileLineIterator.fileEncoding:システムデフォルト<br>
     * <br>
     *                              期待値：(戻り値) String[]:以下の要素を持つString配列<br>
     *                              　要素1："2006/12/10"<br>
     *                              　要素2："aaaaa"<br>
     *                              　要素3："123456789"<br>
     * <br>
     *                              正常パターン<br>
     *                              配列要素3の文字列方配列を返却する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSeparateColumns05() throws Exception {
        // テスト対象のインスタンス化
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub04> clazz = FixedFileLineIterator_Stub04.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        FixedFileLineIterator<FixedFileLineIterator_Stub04> lineIterator = new FixedFileLineIterator<FixedFileLineIterator_Stub04>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "2006/12/10aaaaa123456789";

        // 前提条件(インスタンス化でfileds[]に設定される)

        // テスト実施
        String[] columns = lineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(3, columns.length);
        assertEquals("2006/12/10", columns[0]);
        assertEquals("aaaaa", columns[1]);
        assertEquals("123456789", columns[2]);

        // 状態変化なし
    }

    /**
     * testSeparateColumns06() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:空文字<br>
     * <br>
     * 期待値：(戻り値) String[]:new String[0]<br>
     * <br>
     * 正常パターン。<br>
     * 空文字が引数として渡された場合、要素数0の配列を返却することを確認する。<br>
     * 通常の処理でこの返却値が戻ることはない。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSeparateColumns06() throws Exception {
        // テスト対象のインスタンス化
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub01> clazz = FixedFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        FixedFileLineIterator<FixedFileLineIterator_Stub01> lineIterator = new FixedFileLineIterator<FixedFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "";

        // 前提条件(インスタンス化でfileds[]に設定される)

        // テスト実施
        String[] columns = lineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(0, columns.length);
    }

    /**
     * testSeparateColumns07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileLineString:"2006/01/01あああああ012345678"（全角文字を使用）<br>
     * (状態) this.fields[]:配列要素が3<br>
     * (状態) this.field[]のアノテーションInputFileColumn:@InputFileColumn(columnIndex = 0, bytes = 10, columnFormat = "yyyy/MM/dd")<br>
     * private Date hiduke = null;<br>
     * <br>
     * @InputFileColumn(columnIndex = 1, bytes = 5, stringConverter = StringConverterToUpperCase.class, trimChar = '0', trimType
     *                              = TrimType.LEFT)<br>
     *                              private String shopId = null;<br>
     * <br>
     * @InputFileColumn(columnIndex = 2, bytes = 9, columnFormat = "#########")<br>
     *                              private BigDecimal uriage = null;<br>
     *                              (状態) AbstractFileLineIterator.fileEncoding:システムデフォルト<br>
     * <br>
     *                              期待値：(状態変化) 例外:IllegaｌStateExceptionが発生。FileExceptionにラップされることを確認する。<br>
     *                              ファイル名が入力値のfileNameに一致することを確認する。<br>
     *                              メッセージ："Total Columns byte is different from Total FileLineObject's columns byte."<br>
     * <br>
     *                              例外。アノテーションで設定したバイト数の合計と、ファイルの1行あたりのバイト数が異なる場合、例外が発生することを確認する。<br>
     *                              ファイル行オブジェクトのインスタンス変数がない場合。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSeparateColumns07() throws Exception {
        // テスト対象のインスタンス化
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub04> clazz = FixedFileLineIterator_Stub04.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        FixedFileLineIterator<FixedFileLineIterator_Stub04> lineIterator = new FixedFileLineIterator<FixedFileLineIterator_Stub04>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "2006/01/01あああああ012345678";

        // 前提条件(インスタンス化でfileds[]に設定される)

        // テスト実施
        try {
            lineIterator.separateColumns(fileLineString);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalStateException.class, e.getCause().getClass());
            assertEquals(fileName, e.getFileName());
            String message = "Total Columns byte is different from "
                    + "Total FileLineObject's columns byte.";
            assertEquals(message, e.getMessage());
        }
    }

    /**
     * testIsCheckByte01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) inputFileColumn:null<br>
     * (状態) -:なし<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) -:なし<br>
     * <br>
     * falseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsCheckByte01() throws Exception {
        // テスト対象のインスタンス化
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub01> clazz = FixedFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);

        FixedFileLineIterator<FixedFileLineIterator_Stub01> lineIterator = new FixedFileLineIterator<FixedFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        InputFileColumn inputFileColumn = null;

        // 前提条件なし

        // テスト実施
        boolean result = lineIterator.isCheckByte(inputFileColumn);

        // 返却値の確認
        assertFalse(result);

        // 状態変化なし
    }

    /**
     * testGetDelimiter01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値： <br>
     * 期待値：(戻り値) DELIMITER:,(カンマ)'<br>
     * <br>
     * delimiterのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetDelimiter01() throws Exception {
        // テスト対象のインスタンス化
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub01> clazz = FixedFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();

        ColumnParser columnParser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);

        FixedFileLineIterator<FixedFileLineIterator_Stub01> lineIterator = new FixedFileLineIterator<FixedFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 引数なし

        // 前提条件なし

        // テスト実施
        char result = lineIterator.getDelimiter();

        // 返却値の確認
        assertEquals(',', result);

        // 状態変化なし
    }

    /**
     * testGetEncloseChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値： <br>
     * 期待値：(戻り値) ENCLOSE_CHAR:Character.MIN_VALUE<br>
     * <br>
     * encloseCharのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetEncloseChar01() throws Exception {
        // テスト対象のインスタンス化
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineIterator_Stub01> clazz = FixedFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();

        ColumnParser columnParser = new FixedFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);

        FixedFileLineIterator<FixedFileLineIterator_Stub01> lineIterator = new FixedFileLineIterator<FixedFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 引数なし

        // 前提条件なし

        // テスト実施
        char result = lineIterator.getEncloseChar();

        // 返却値の確認
        assertEquals(Character.MIN_VALUE, result);

        // 状態変化なし
    }

    /**
     * 正常系<br>
     * 固定長改行無し
     * @throws Exception
     */
    @Test
    public void testNext01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "FixedFileLineIterator_next01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        columnParserMap.put("java.util.Date", new DateColumnParser());
        columnParserMap.put("java.math.BigDecimal", new DecimalColumnParser());
        columnParserMap.put("int", new IntColumnParser());

        FixedFileLineIterator<FixedFileLine_Stub01> fileLineIterator = new FixedFileLineIterator<FixedFileLine_Stub01>(
                fileName, FixedFileLine_Stub01.class, columnParserMap);

        // テスト実施
        FixedFileLine_Stub01 result1 = fileLineIterator.next();
        FixedFileLine_Stub01 result2 = fileLineIterator.next();
        FixedFileLine_Stub01 result3 = fileLineIterator.next();

        // 返却値の確認
        assertEquals("1", result1.getColumn1());
        assertEquals("22", result1.getColumn2());
        assertEquals("333", result1.getColumn3());
        assertEquals("4444", result1.getColumn4());

        assertEquals("5", result2.getColumn1());
        assertEquals("66", result2.getColumn2());
        assertEquals("777", result2.getColumn3());
        assertEquals("8888", result2.getColumn4());

        assertEquals("9", result3.getColumn1());
        assertEquals("AA", result3.getColumn2());
        assertEquals("BBB", result3.getColumn3());
        assertEquals("CCCC", result3.getColumn4());
    }

    /**
     * 正常系<br>
     * 固定長
     * @throws Exception
     */
    @Test
    public void testNext02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "FixedFileLineIterator_next02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        columnParserMap.put("java.util.Date", new DateColumnParser());
        columnParserMap.put("java.math.BigDecimal", new DecimalColumnParser());
        columnParserMap.put("int", new IntColumnParser());

        FixedFileLineIterator<FixedFileLine_Stub02> fileLineIterator = new FixedFileLineIterator<FixedFileLine_Stub02>(
                fileName, FixedFileLine_Stub02.class, columnParserMap);

        // テスト実施
        FixedFileLine_Stub02 result1 = fileLineIterator.next();
        FixedFileLine_Stub02 result2 = fileLineIterator.next();
        FixedFileLine_Stub02 result3 = fileLineIterator.next();

        // 返却値の確認
        assertEquals("1", result1.getColumn1());
        assertEquals("22", result1.getColumn2());
        assertEquals("333", result1.getColumn3());
        assertEquals("4444", result1.getColumn4());

        assertEquals("5", result2.getColumn1());
        assertEquals("66", result2.getColumn2());
        assertEquals("777", result2.getColumn3());
        assertEquals("8888", result2.getColumn4());

        assertEquals("9", result3.getColumn1());
        assertEquals("AA", result3.getColumn2());
        assertEquals("BBB", result3.getColumn3());
        assertEquals("CCCC", result3.getColumn4());
    }

    /**
     * 正常系<br>
     * キャッシュしているアノテーションの情報を利用している事を確認する。<br>
     * @throws Exception
     */
    @Test
    public void testNext03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // 様々な設定がされているファイル行オブジェクトを設定
        FixedFileLineIterator<CSVFileLine_Stub04> fileLineIterator = new FixedFileLineIterator<CSVFileLine_Stub04>(
                fileName, CSVFileLine_Stub04.class, columnParserMap);

        // ファイル行オブジェクトに設定してあった値を全て上書き
        // 以下の設定が適用されれば、ファイル行オブジェクトの
        // アノテーションにアクセスしていないことになる。
        char[] charArray = new char[] { 0, 0, 0, 0 };
        // 前提条件
        UTUtil.setPrivateField(fileLineIterator, "lineFeedChar", "\r\n");
        UTUtil.setPrivateField(fileLineIterator, "inputFileColumns", null);
        UTUtil.setPrivateField(fileLineIterator, "columnFormats", new String[] {
                "", "", "", "" });
        UTUtil.setPrivateField(fileLineIterator, "columnBytes", new int[] { 4,
                3, 4, 6 });
        UTUtil.setPrivateField(fileLineIterator, "totalBytes", 17);

        UTUtil.setPrivateField(fileLineIterator, "trimChars", charArray);
        UTUtil
                .setPrivateField(fileLineIterator, "columnEncloseChar",
                        charArray);
        UTUtil.setPrivateField(fileLineIterator, "stringConverters",
                new NullStringConverter[] { new NullStringConverter(),
                        new NullStringConverter(), new NullStringConverter(),
                        new NullStringConverter() });
        LineReader reader = (LineReader) UTUtil.getPrivateField(
                fileLineIterator, "lineReader");
        UTUtil.setPrivateField(reader, "lineFeedChar", "\r\n");

        // テスト実施
        CSVFileLine_Stub04 result1 = fileLineIterator.next();
        CSVFileLine_Stub04 result2 = fileLineIterator.next();
        CSVFileLine_Stub04 result3 = fileLineIterator.next();

        // 返却値の確認
        assertEquals("\"1\",", result1.getColumn1());
        assertEquals("22,", result1.getColumn2());
        assertEquals("333,", result1.getColumn3());
        assertEquals("|4444|", result1.getColumn4());

        assertEquals("\"5\",", result2.getColumn1());
        assertEquals("66,", result2.getColumn2());
        assertEquals("777,", result2.getColumn3());
        assertEquals("|8888|", result2.getColumn4());

        assertEquals("\"9\",", result3.getColumn1());
        assertEquals("AA,", result3.getColumn2());
        assertEquals("BBB,", result3.getColumn3());
        assertEquals("|CCCC|", result3.getColumn4());
    }
}
