/*
 * $Id: FixedFileLineWriterTest.java 5576 2007-11-15 13:13:32Z pakucn $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.terasoluna.fw.file.annotation.NullStringConverter;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;
import jp.terasoluna.fw.file.annotation.PaddingType;
import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.ut.VMOUTUtil;
import jp.terasoluna.utlib.UTUtil;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.FixedFileLineWriter} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイル行オブジェクトからデータを読み込み、1行分のデータを固定長形式でファイルに書き込む。<br>
 * AbstractFileLineWriterのサブクラス。
 * <p>
 * @author 奥田哲司
 * @author 趙俸徹
 * @see jp.terasoluna.fw.file.dao.standard.FixedFileLineWriter
 */
public class FixedFileLineWriterTest {

    private static final String TEMP_FILE_NAME = FixedFileLineWriterTest.class
            .getResource("FixedFileLineWriterTest_tmp.txt").getPath();

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(FixedFileLineWriterTest.class);
    }

    @Before
    public void setUp() throws Exception {
        VMOUTUtil.initialize();
        // ファイルの初期化
        File file = new File(TEMP_FILE_NAME);
        file.delete();
        file.createNewFile();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        // ファイルの初期化
        File file = new File(TEMP_FILE_NAME);
        file.delete();
        file.createNewFile();
    }

    /**
     * testFixedFileLineWriter01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E.F <br>
     * <br>
     * 入力値：(引数) fileName:"File_Empty.txt"(相対パス)<br>
     * (引数) clazz:FixedFileLineWriter_Stub01<br>
     * @FileFormat()<br> (引数) columnFormatterrMap:以下の設定を持つHashMapのインスタンス<br>
     *                   要素1<br>
     *                   key:"test"<br>
     *                   value:ColumnFormatterインスタンス<br>
     *                   FixedFileLineWriter_ColumnFormatterStub01インスタンス<br>
     *                   空実装<br>
     * <br>
     *                   期待値：(状態変化) super:1回呼ばれる<br>
     *                   引数と同じインスタンスが設定される<br>
     *                   (状態変化) super.init:1回呼ばれる<br>
     * <br>
     *                   引数fileNameが相対パスで指定されたファイル名だった場合に、コンストラクタの呼び出しが正常に行われることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFixedFileLineWriter01() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        String fileName = TEMP_FILE_NAME;
        Class<FixedFileLineWriter_Stub01> clazz = FixedFileLineWriter_Stub01.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        ColumnFormatter formatter = new FixedFileLineWriter_ColumnFormatterStub01();
        columnFormatterMap.put("java.lang.String", formatter);

        // 前提条件なし

        // テスト実施
        FixedFileLineWriter<FixedFileLineWriter_Stub01> fileWriter = new FixedFileLineWriter<FixedFileLineWriter_Stub01>(
                fileName, clazz, columnFormatterMap);

        // 返却値なし

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                "<init>"));
        List arguments = VMOUTUtil.getArguments(AbstractFileLineWriter.class,
                "<init>", 0);
        assertEquals(fileName, arguments.get(0));
        assertEquals(clazz, arguments.get(1));
        assertSame(columnFormatterMap, arguments.get(2));
        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                "init"));

        // クローズ処理
        fileWriter.closeFile();
    }

    /**
     * testFixedFileLineWriter02() <br>
     * <br>
     * (異常系) <br>
     * 観点：F.G <br>
     * <br>
     * 入力値：(引数) fileName:(相対パス)<br>
     * (引数) clazz:FixedFileLineWriter_Stub02<br>
     * @FileFormat(delimiter='、')<br> (引数) columnFormatterrMap:以下の設定を持つHashMapのインスタンス<br>
     *                                要素1<br>
     *                                key:"test"<br>
     *                                value:ColumnFormatterインスタンス<br>
     *                                FixedFileLineWriter_ColumnFormatterStub01インスタンス<br>
     *                                空実装<br>
     * <br>
     *                                期待値：(状態変化) super:1回呼ばれる<br>
     *                                引数と同じインスタンスが設定される<br>
     *                                (状態変化) super.init:呼ばれない<br>
     *                                (状態変化)
     *                                なし:"Delimiter can not change."のメッセージ、IllegalStateException、ファイル名を持つFileExceptionが発生する 。<br>
     * <br>
     *                                例外。@FileFormatのdelimiterに初期値以外を設定した場合、例外が発生することを確認する。<br>
     *                                ファイル名が入力値のfileNameに一致することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFixedFileLineWriter02() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        String fileName = TEMP_FILE_NAME;
        Class<FixedFileLineWriter_Stub02> clazz = FixedFileLineWriter_Stub02.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        ColumnFormatter formatter = new FixedFileLineWriter_ColumnFormatterStub01();
        columnFormatterMap.put("java.lang.String", formatter);

        // テスト実施
        try {
            new FixedFileLineWriter<FixedFileLineWriter_Stub02>(fileName,
                    clazz, columnFormatterMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認]
            assertEquals(1, VMOUTUtil.getCallCount(
                    AbstractFileLineWriter.class, "<init>"));
            List arguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "<init>", 0);
            assertEquals(fileName, arguments.get(0));
            assertEquals(FixedFileLineWriter_Stub02.class, arguments.get(1));
            assertSame(columnFormatterMap, arguments.get(2));
            assertFalse(VMOUTUtil
                    .isCalled(AbstractFileLineWriter.class, "init"));

            assertEquals("Delimiter can not change.", e.getMessage());
            assertEquals(fileName, e.getFileName());
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    /**
     * testFixedFileLineWriter03() <br>
     * <br>
     * (異常系) <br>
     * 観点：F.G <br>
     * <br>
     * 入力値：(引数) fileName:(相対パス)<br>
     * (引数) clazz:FixedFileLineWriter_Stub03<br>
     * @FileFormat(encloseChar='"')<br> (引数) columnFormatterrMap:以下の設定を持つHashMapのインスタンス<br>
     *                                  要素1<br>
     *                                  key:"test"<br>
     *                                  value:ColumnFormatterインスタンス<br>
     *                                  FixedFileLineWriter_ColumnFormatterStub01インスタンス<br>
     *                                  空実装<br>
     * <br>
     *                                  期待値：(状態変化) super:1回呼ばれる<br>
     *                                  引数と同じインスタンスが設定される<br>
     *                                  (状態変化) super.init:呼ばれない<br>
     *                                  (状態変化)
     *                                  なし:"EncloseChar can not change."のメッセージ、IllegalStateException、ファイル名を持つFileExceptionが発生する
     *                                  。<br>
     * <br>
     *                                  例外。@FileFormatのencloseCharに初期値以外を設定した場合、例外が発生することを確認する。<br>
     *                                  ファイル名が入力値のfileNameに一致することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFixedFileLineWriter03() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        String fileName = TEMP_FILE_NAME;
        Class<FixedFileLineWriter_Stub03> clazz = FixedFileLineWriter_Stub03.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        ColumnFormatter formatter = new FixedFileLineWriter_ColumnFormatterStub01();
        columnFormatterMap.put("java.lang.String", formatter);

        // 前提条件なし

        // テスト実施
        try {
            new FixedFileLineWriter<FixedFileLineWriter_Stub03>(fileName,
                    clazz, columnFormatterMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(
                    AbstractFileLineWriter.class, "<init>"));
            List arguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "<init>", 0);
            assertEquals(fileName, arguments.get(0));
            assertEquals(FixedFileLineWriter_Stub03.class, arguments.get(1));
            assertSame(columnFormatterMap, arguments.get(2));
            assertFalse(VMOUTUtil
                    .isCalled(AbstractFileLineWriter.class, "init"));
            assertEquals("EncloseChar can not change.", e.getMessage());
            assertEquals(fileName, e.getFileName());
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    /**
     * testFixedFileLineWriter04() <br>
     * <br>
     * (正常系) <br>
     * 観点：E.F <br>
     * <br>
     * 入力値：(引数) fileName:"aaa.txt"（絶対パス）<br>
     * (引数) clazz:FixedFileLineWriter_Stub01<br>
     * @FileFormat()<br> (引数) columnFormatterrMap:以下の設定を持つHashMapのインスタンス<br>
     *                   要素1<br>
     *                   key:"test"<br>
     *                   value:ColumnFormatterインスタンス<br>
     *                   FixedFileLineWriter_ColumnFormatterStub01インスタンス<br>
     *                   空実装<br>
     * <br>
     *                   期待値：(状態変化) super:1回呼ばれる<br>
     *                   引数と同じインスタンスが設定される<br>
     *                   (状態変化) super.init:1回呼ばれる<br>
     * <br>
     *                   引数fileNameが絶対パスで指定されたファイル名だった場合に、コンストラクタの呼び出しが正常に行われることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFixedFileLineWriter04() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        URL url = FixedFileLineIteratorTest.class.getResource("/aaa.txt");
        String fileName = url.getPath();
        Class<FixedFileLineWriter_Stub01> clazz = FixedFileLineWriter_Stub01.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        ColumnFormatter formatter = new FixedFileLineWriter_ColumnFormatterStub01();
        columnFormatterMap.put("java.lang.String", formatter);

        // 前提条件なし

        // テスト実施
        FixedFileLineWriter<FixedFileLineWriter_Stub01> fileWriter = new FixedFileLineWriter<FixedFileLineWriter_Stub01>(
                fileName, clazz, columnFormatterMap);

        // 返却値なし

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                "<init>"));
        List arguments = VMOUTUtil.getArguments(AbstractFileLineWriter.class,
                "<init>", 0);
        assertEquals(fileName, arguments.get(0));
        assertEquals(clazz, arguments.get(1));
        assertSame(columnFormatterMap, arguments.get(2));
        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                "init"));

        // クローズ処理
        fileWriter.closeFile();
    }

    /**
     * 異常系<br>
     * ファイル行オブジェクトにOutputFileColumnアノテーションが無し
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFixedFileLineWriter05() throws Exception {
        // 引数の設定
        String fileName = TEMP_FILE_NAME;
        Class<FileLineObject_Empty> clazz = FileLineObject_Empty.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        try {
            new FixedFileLineWriter<FileLineObject_Empty>(fileName, clazz,
                    columnFormatterMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals("OutputFileColumn is not found.", e.getMessage());
            assertEquals(fileName, e.getFileName());
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    /**
     * 異常系<br>
     * OutputFileColumnにEncloseCharが設定されている場合、エラーとする
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFixedFileLineWriter06() throws Exception {
        // 引数の設定
        String fileName = TEMP_FILE_NAME;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        try {
            new FixedFileLineWriter<CSVFileLine_Stub01>(fileName,
                    CSVFileLine_Stub01.class, columnFormatterMap);
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
     * 正常系<br>
     * 行区切り無しの固定長形式ファイルでヘッダ行数を指定した場合、<br>
     * 異常終了しない事を確認する。
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFixedFileLineWriter07() throws Exception {
        // 引数の設定
        String fileName = TEMP_FILE_NAME;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        FixedFileLineWriter<FixedFileLine_Stub03> fileWriter = null;

        // テスト実施
        try {
            fileWriter = new FixedFileLineWriter<FixedFileLine_Stub03>(
                    fileName, FixedFileLine_Stub03.class, columnFormatterMap);
        } finally {// クローズ処理
            fileWriter.closeFile();
        }
    }

    /**
     * 正常系<br>
     * 行区切り無しの固定長形式ファイルでトレイラ行数を指定した場合、<br>
     * 異常終了しない事を確認する。
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFixedFileLineWriter08() throws Exception {
        // 引数の設定
        String fileName = TEMP_FILE_NAME;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        FixedFileLineWriter<FixedFileLine_Stub04> fileWriter = null;

        // テスト実施
        try {
            fileWriter = new FixedFileLineWriter<FixedFileLine_Stub04>(
                    fileName, FixedFileLine_Stub04.class, columnFormatterMap);
        } finally {// クローズ処理
            fileWriter.closeFile();
        }
    }

    /**
     * testIsCheckByte01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) outputFileColumn:null<br>
     * (状態) -:なし<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) -:なし<br>
     * <br>
     * trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsCheckBytes01() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;
        Class<FixedFileLineWriter_Stub01> clazz = FixedFileLineWriter_Stub01.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        ColumnFormatter formatter = new FixedFileLineWriter_ColumnFormatterStub01();
        columnFormatterMap.put("java.lang.String", formatter);

        FixedFileLineWriter<FixedFileLineWriter_Stub01> fileWriter = new FixedFileLineWriter<FixedFileLineWriter_Stub01>(
                fileName, clazz, columnFormatterMap);

        // 引数の設定
        OutputFileColumn outputFileColumn = null;

        // 前提条件なし

        // テスト実施
        boolean result = fileWriter.isCheckByte(outputFileColumn);
        assertTrue(result);

        // 状態変化なし

        // クローズ処理
        fileWriter.closeFile();
    }

    /**
     * testGetDelimiter01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値： <br>
     * 期待値：(戻り値) DELIMITER:not null<br>
     * '\u0000'<br>
     * <br>
     * delimiterのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetDelimiter01() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;
        Class<FixedFileLineWriter_Stub01> clazz = FixedFileLineWriter_Stub01.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        ColumnFormatter formatter = new FixedFileLineWriter_ColumnFormatterStub01();
        columnFormatterMap.put("java.lang.String", formatter);
        FixedFileLineWriter<FixedFileLineWriter_Stub01> lineWriter = new FixedFileLineWriter<FixedFileLineWriter_Stub01>(
                fileName, clazz, columnFormatterMap);

        // 引数なし

        // 前提条件なし

        // テスト実施
        char result = lineWriter.getDelimiter();

        // 返却値の確認
        assertEquals(Character.MIN_VALUE, result);

        // 状態変化なし

        // クローズ処理
        lineWriter.closeFile();
    }

    /**
     * testGetEncloseChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値： <br>
     * 期待値：(戻り値) ENCLOSE_CHAR:not null<br>
     * '\u0000'<br>
     * <br>
     * encloseCharのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetEncloseChar01() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;
        Class<FixedFileLineWriter_Stub01> clazz = FixedFileLineWriter_Stub01.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        ColumnFormatter formatter = new FixedFileLineWriter_ColumnFormatterStub01();
        columnFormatterMap.put("java.lang.String", formatter);
        FixedFileLineWriter<FixedFileLineWriter_Stub01> lineWriter = new FixedFileLineWriter<FixedFileLineWriter_Stub01>(
                fileName, clazz, columnFormatterMap);

        // 引数なし

        // 前提条件なし

        // テスト実施
        char result = lineWriter.getEncloseChar();

        // 返却値の確認
        assertEquals(Character.MIN_VALUE, result);

        // 状態変化なし

        // クローズ処理
        lineWriter.closeFile();
    }

    /**
     * 正常系<br>
     * 固定長改行無し
     * @throws Exception
     */
    @Test
    public void testPrintDataLine01() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<FixedFileLine_Stub01> clazz = FixedFileLine_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        FixedFileLineWriter<FixedFileLine_Stub01> fileLineWriter = new FixedFileLineWriter<FixedFileLine_Stub01>(
                fileName, clazz, columnFormatterMap);

        // 前処理(引数)
        FixedFileLine_Stub01 t1 = new FixedFileLine_Stub01();
        FixedFileLine_Stub01 t2 = new FixedFileLine_Stub01();
        FixedFileLine_Stub01 t3 = new FixedFileLine_Stub01();

        // 返却値の確認
        t1.setColumn1("1");
        t1.setColumn2("22");
        t1.setColumn3("333");
        t1.setColumn4("4444");
        t2.setColumn1("5");
        t2.setColumn2("66");
        t2.setColumn3("777");
        t2.setColumn4("8888");
        t3.setColumn1("9");
        t3.setColumn2("AA");
        t3.setColumn3("BBB");
        t3.setColumn4("CCCC");

        // テスト実施
        fileLineWriter.printDataLine(t1);
        fileLineWriter.printDataLine(t2);
        fileLineWriter.printDataLine(t3);

        fileLineWriter.closeFile();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), System
                            .getProperty("file.encoding")));
            assertEquals("122333444456677788889AABBBCCCC", reader.readLine());
        } finally {
            reader.close();
        }
    }

    /**
     * 正常系<br>
     * 固定長
     * @throws Exception
     */
    @Test
    public void testPrintDataLine02() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        FixedFileLineWriter<FixedFileLine_Stub02> fileLineWriter = new FixedFileLineWriter<FixedFileLine_Stub02>(
                fileName, FixedFileLine_Stub02.class, columnFormatterMap);

        // 前処理(引数)
        FixedFileLine_Stub02 t1 = new FixedFileLine_Stub02();
        FixedFileLine_Stub02 t2 = new FixedFileLine_Stub02();
        FixedFileLine_Stub02 t3 = new FixedFileLine_Stub02();

        t1.setColumn1("1");
        t1.setColumn2("22");
        t1.setColumn3("333");
        t1.setColumn4("4444");
        t2.setColumn1("5");
        t2.setColumn2("66");
        t2.setColumn3("777");
        t2.setColumn4("8888");
        t3.setColumn1("9");
        t3.setColumn2("AA");
        t3.setColumn3("BBB");
        t3.setColumn4("CCCC");

        // テスト実施
        fileLineWriter.printDataLine(t1);
        fileLineWriter.printDataLine(t2);
        fileLineWriter.printDataLine(t3);

        fileLineWriter.closeFile();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), System
                            .getProperty("file.encoding")));
            assertEquals("1223334444", reader.readLine());
            assertEquals("5667778888", reader.readLine());
            assertEquals("9AABBBCCCC", reader.readLine());
        } finally {
            reader.close();
        }
    }

    /**
     * 正常系<br>
     * キャッシュしているアノテーションの情報を利用している事を確認する。<br>
     * @throws Exception
     */
    @Test
    public void testPrintDataLine03() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        FixedFileLineWriter<CSVFileLine_Stub04> fileLineWriter = new FixedFileLineWriter<CSVFileLine_Stub04>(
                fileName, CSVFileLine_Stub04.class, columnFormatterMap);

        // 前処理(引数)
        CSVFileLine_Stub04 t1 = new CSVFileLine_Stub04();

        t1.setColumn1("1");
        t1.setColumn2("22");
        t1.setColumn3("333");
        t1.setColumn4("4444");

        // ファイル行オブジェクトに設定してあった値を全て上書き
        // 以下の設定が適用されれば、ファイル行オブジェクトの
        // アノテーションにアクセスしていないことになる。
        char[] charArray = new char[] { 0, 0, 0, 0 };
        // 前提条件
        ReflectionTestUtils.setField(fileLineWriter, "lineFeedChar", "\r\n");
        // ReflectionTestUtils.setField(fileLineWriter, "delimiter", '_');
        ReflectionTestUtils.setField(fileLineWriter, "outputFileColumns", null);
        ReflectionTestUtils.setField(fileLineWriter, "columnFormats", new String[] {
                "", "", "", "" });
        ReflectionTestUtils.setField(fileLineWriter, "columnBytes", new int[] { 1, 2,
                3, 4 });
        // ReflectionTestUtils.setField(fileLineWriter, "totalBytes", 0);
        ReflectionTestUtils.setField(fileLineWriter, "paddingTypes",
                new PaddingType[] { PaddingType.NONE, PaddingType.NONE,
                        PaddingType.NONE, PaddingType.NONE });
        ReflectionTestUtils.setField(fileLineWriter, "paddingChars", charArray);
        ReflectionTestUtils.setField(fileLineWriter, "trimChars", charArray);
        ReflectionTestUtils.setField(fileLineWriter, "columnEncloseChar", charArray);
        ReflectionTestUtils.setField(fileLineWriter, "stringConverters",
                new NullStringConverter[] { new NullStringConverter(),
                        new NullStringConverter(), new NullStringConverter(),
                        new NullStringConverter() });

        // テスト実施
        fileLineWriter.printDataLine(t1);

        fileLineWriter.closeFile();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), System
                            .getProperty("file.encoding")));
            assertEquals("1223334444", reader.readLine());
        } finally {
            reader.close();
        }
    }
}
