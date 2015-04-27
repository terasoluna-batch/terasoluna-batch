/*
 * $Id:$
 *
 * Copyright (c) 2006-2015 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import jp.terasoluna.fw.file.annotation.NullStringConverter;
import jp.terasoluna.fw.file.annotation.PaddingType;
import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.utlib.UTUtil;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.VariableFileLineWriter} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイル行オブジェクトからデータを読み込み、1行分のデータを可変長形式で ファイルに書き込む。<br>
 * AbstractFileLineWriterのサブクラス。
 * <p>
 * @author 奥田哲司
 * @author 趙俸徹
 * @see jp.terasoluna.fw.file.dao.standard.VariableFileLineWriter
 */
public class VariableFileLineWriterTest {

    private static final String TEMP_FILE_NAME = VariableFileLineWriterTest.class
            .getResource("VariableFileLineWriterTest_tmp.txt").getPath();

    @Before
    public void setUp() throws Exception {
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
     * testVariableFileLineWriter01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileName:"(パス)VariableFileLineWriter_testVariableFileLineWriter01.txt"<br>
     * (引数) clazz:FileFormatアノテーションを持ち、delimiter、encloseCharが初期値<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"java.lang.String"=NullColumnFormatter.java<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineWriter#init():1回呼ばれること<br>
     * (状態変化) AbstractFileLineWriter#AbstractFileLineWriter():1回呼ばれること。<br>
     * 引数が渡ってくることを確認する。<br>
     * (状態変化) this.encloseChar:parameterClassのアノテーションFileFormatのencloseChar()の値。<br>
     * (状態変化) this.delimiter:parameterClassのアノテーションFileFormatのdelimiter()の値。<br>
     * <br>
     * 正常パターン<br>
     * @FileFormatがすべてデフォルト値の場合に、コンストラクタの呼出が正常に行われることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testVariableFileLineWriter01() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要
        // 引数の設定
        String fileName = TEMP_FILE_NAME;

        Class<VariableFileLineWriter_Stub01> clazz = VariableFileLineWriter_Stub01.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // 前提条件の設定
        // なし

        // テスト実施
        VariableFileLineWriter<VariableFileLineWriter_Stub01> result = null;
        try {
            result = new VariableFileLineWriter<VariableFileLineWriter_Stub01>(
                    fileName, clazz, columnFormatterMap);

            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(Character.MIN_VALUE, UTUtil.getPrivateField(result,
                    "encloseChar"));
            assertEquals(',', UTUtil.getPrivateField(result, "delimiter"));
        } finally {
            // テスト対象のクローズ処理
            if (result != null) {
                result.closeFile();
            }
        }
    }

    /**
     * testVariableFileLineWriter02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileName:"(パス)VariableFileLineWriter_testVariableFileLineWriter02.txt"<br>
     * (引数) clazz:FileFormatアノテーションを持ち、delimiterが'\u0000'、encloseCharが'\"'<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"java.lang.String"=NullColumnFormatter.java<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineWriter#init():呼ばれないこと。<br>
     * (状態変化) AbstractFileLineWriter#AbstractFileLineWriter():1回呼ばれること。<br>
     * 引数が渡ってくることを確認する。<br>
     * (状態変化) 例外:"Delimiter can not use '\u0000'."のメッセージ、IllegalStateException、ファイル名を持つFileExceptionが発生する。<br>
     * <br>
     * 例外。@FileFormatのdelimiterに'\u0000'を設定した場合、例外が発生することを確認する。<br>
     * ファイル名が入力値のfileNameに一致することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testVariableFileLineWriter02() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = TEMP_FILE_NAME;

        Class<VariableFileLineWriter_Stub04> clazz = VariableFileLineWriter_Stub04.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // 前提条件の設定
        // なし
        try {
            // テスト実施
            new VariableFileLineWriter<VariableFileLineWriter_Stub04>(fileName,
                    clazz, columnFormatterMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(FileException.class, e.getClass());
            assertEquals("Delimiter can not use '\\u0000'.", e.getMessage());
            assertEquals(IllegalStateException.class, e.getCause().getClass());
            assertEquals(fileName, e.getFileName());
        }
    }

    /**
     * testVariableFileLineWriter03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileName:"(パス)VariableFileLineWriter_testVariableFileLineWriter03.txt"<br>
     * (引数) clazz:FileFormatアノテーションを持ち、delimiterが'#'<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"java.lang.String"=NullColumnFormatter.java<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineWriter#init():呼ばれること<br>
     * (状態変化) AbstractFileLineWriter#AbstractFileLineWriter():1回呼ばれること。<br>
     * 引数が渡ってくることを確認する。<br>
     * (状態変化) this.encloseChar:parameterClassのアノテーションFileFormatのencloseChar()の値。<br>
     * (状態変化) this.delimiter:parameterClassのアノテーションFileFormatのdelimiter()の値。<br>
     * <br>
     * @FileFormatのdelimiterに'#'を設定、encloseCharに'"'を設定した場合、コンストラクタの呼出が正常に行われることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testVariableFileLineWriter03() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = TEMP_FILE_NAME;

        Class<VariableFileLineWriter_Stub07> clazz = VariableFileLineWriter_Stub07.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // 前提条件の設定
        // なし

        VariableFileLineWriter<VariableFileLineWriter_Stub07> result = null;
        try {
            // テスト実施
            result = new VariableFileLineWriter<VariableFileLineWriter_Stub07>(
                    fileName, clazz, columnFormatterMap);

            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals('"', UTUtil.getPrivateField(result, "encloseChar"));
            assertEquals('#', UTUtil.getPrivateField(result, "delimiter"));
        } finally {
            // テスト対象のクローズ処理
            if (result != null) {
                result.closeFile();
            }
        }
    }

    /**
     * 異常系<br>
     * 改行文字に区切り文字が含まれる
     */
    @Test
    public void testVariableFileLineWriter04() throws Exception {
        // 引数の設定
        String fileName = TEMP_FILE_NAME;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        try {
            new VariableFileLineWriter<VariableFileLine_Stub01>(fileName,
                    VariableFileLine_Stub01.class, columnFormatterMap);
            fail();
        } catch (FileException e) {
            assertEquals(
                    "delimiter is the same as lineFeedChar and is no use.", e
                            .getMessage());
            assertEquals(fileName, e.getFileName());
            assertSame(IllegalStateException.class, e.getCause().getClass());
        }
    }

    /**
     * 異常系<br>
     * 改行文字と区切り文字が同一
     */
    @Test
    public void testVariableFileLineWriter05() throws Exception {
        // 引数の設定
        String fileName = TEMP_FILE_NAME;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        try {
            new VariableFileLineWriter<VariableFileLine_Stub02>(fileName,
                    VariableFileLine_Stub02.class, columnFormatterMap);
            fail();
        } catch (FileException e) {
            assertEquals(
                    "delimiter is the same as lineFeedChar and is no use.", e
                            .getMessage());
            assertEquals(fileName, e.getFileName());
            assertSame(IllegalStateException.class, e.getCause().getClass());
        }
    }

    /**
     * 異常系<br>
     * ファイル行オブジェクトにOutputFileColumnアノテーションが無し
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testVariableFileLineWriter06() throws Exception {
        // 引数の設定
        String fileName = TEMP_FILE_NAME;
        Class<FileLineObject_Empty> clazz = FileLineObject_Empty.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        try {
            new VariableFileLineWriter<FileLineObject_Empty>(fileName, clazz,
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
     * testGetColumn01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) t:以下のカラムを一つ持つVariableFileLineWriterスタブ<br>
     * "abcdef"<br>
     * (引数) index:0<br>
     * (状態) this.encloseChar:Charcator.MIN_VALUE<br>
     * <br>
     * 期待値：(戻り値) String:abcdef<br>
     * <br>
     * 正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn01() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        VariableFileLineWriter<VariableFileLineWriter_Stub05> lineWriter = Mockito.spy(new VariableFileLineWriter<VariableFileLineWriter_Stub05>(
                fileName, VariableFileLineWriter_Stub05.class,
                columnFormatterMap));

        // 引数の設定
        VariableFileLineWriter_Stub05 t = new VariableFileLineWriter_Stub05();
        t.setColumn01("abcdef");
        int index = 0;

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定されている。

        try {
            // テスト実施
            String result = lineWriter.getColumn(t, index);
            // 返却値の確認
            assertEquals("abcdef", result);
        } finally {
            // テスト対象のクローズ処理
            lineWriter.closeFile();
        }
    }

    /**
     * testGetColumn02() <br>
     * <br>
     * (正常系 <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) t:以下のカラムを一つ持つVariableFileLineWriterスタブ<br>
     * "abcdef"<br>
     * (引数) index:0<br>
     * (状態) this.encloseChar:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:abcdef<br>
     * <br>
     * 正しく値を取得することを確認する。<br>
     * 出力対象にエスケープ対象の文字（囲み文字）が無い場合は通常の文字 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn02() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        VariableFileLineWriter<VariableFileLineWriter_Stub06> lineWriter = new VariableFileLineWriter<VariableFileLineWriter_Stub06>(
                fileName, VariableFileLineWriter_Stub06.class,
                columnFormatterMap);

        // 引数の設定
        VariableFileLineWriter_Stub06 t = new VariableFileLineWriter_Stub06();
        t.setColumn01("abcdef");
        int index = 0;

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定されている。

        try {
            // テスト実施
            String result = lineWriter.getColumn(t, index);

            // 返却値の確認
            assertEquals("abcdef", result);
        } finally {
            // テスト対象のクローズ処理
            lineWriter.closeFile();
        }
    }

    /**
     * testGetColumn03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) t:以下のカラムを一つ持つVariableFileLineWriterスタブ<br>
     * "ab\"cdef"<br>
     * (引数) index:0<br>
     * (状態) this.encloseChar:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"ab\"\"cdef"<br>
     * <br>
     * 正しく値を取得することを確認する。<br>
     * 出力対象にエスケープ対象の文字（囲み文字）がある場合に、エスケープされる <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn03() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Map<String, ColumnFormatter> textGetterMap = new HashMap<String, ColumnFormatter>();
        textGetterMap.put("java.lang.String", new NullColumnFormatter());
        VariableFileLineWriter<VariableFileLineWriter_Stub06> lineWriter = new VariableFileLineWriter<VariableFileLineWriter_Stub06>(
                fileName, VariableFileLineWriter_Stub06.class, textGetterMap);

        // 引数の設定
        VariableFileLineWriter_Stub06 t = new VariableFileLineWriter_Stub06();
        t.setColumn01("ab\"cdef");
        int index = 0;

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定されている。

        try {
            // テスト実施
            String result = lineWriter.getColumn(t, index);

            // 返却値の確認
            assertEquals("ab\"\"cdef", result);
        } finally {
            // テスト対象のクローズ処理
            lineWriter.closeFile();
        }
    }

    /**
     * testGetColumn04() <br>
     * <br>
     * (正常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) t:null<br>
     * (引数) index:0<br>
     * (状態) this.encloseChar:'\"'<br>
     * <br>
     * 期待値：(状態変化) 例外:NullPointerException<br>
     * <br>
     * 引数tをnullにすると、NullPointerExceptionがスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetColumn04() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Map<String, ColumnFormatter> textGetterMap = new HashMap<String, ColumnFormatter>();
        textGetterMap.put("java.lang.String", new NullColumnFormatter());
        VariableFileLineWriter<VariableFileLineWriter_Stub06> lineWriter = new VariableFileLineWriter<VariableFileLineWriter_Stub06>(
                fileName, VariableFileLineWriter_Stub06.class, textGetterMap);

        // 引数の設定
        VariableFileLineWriter_Stub06 t = null;
        int index = 0;

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定されている。

        // テスト実施
        try {
            lineWriter.getColumn(t, index);
            fail("NullPointerExceptionがスローされませんでした。");
        } catch (NullPointerException e) {
            assertEquals(NullPointerException.class, e.getClass());
        } finally {
            // テスト対象のクローズ処理
            lineWriter.closeFile();
        }
    }

    /**
     * testGetColumn05() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) t:以下のカラムを持つVariableFileLineWriterスタブ<br>
     * "abcdef"<br>
     * "aaabbb"<br>
     * (引数) index:1<br>
     * (状態) this.encloseChar:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:aaabbb<br>
     * <br>
     * indexの値に紐づくカラムの値（囲み文字なしの場合）が取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn05() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Map<String, ColumnFormatter> textGetterMap = new HashMap<String, ColumnFormatter>();
        textGetterMap.put("java.lang.String", new NullColumnFormatter());
        VariableFileLineWriter<VariableFileLineWriter_Stub08> lineWriter = new VariableFileLineWriter<VariableFileLineWriter_Stub08>(
                fileName, VariableFileLineWriter_Stub08.class, textGetterMap);

        // 引数の設定
        VariableFileLineWriter_Stub08 t = new VariableFileLineWriter_Stub08();
        t.setColumn01("abcdef");
        t.setColumn02("aaabbb");
        int index = 1;

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定されている。

        try {
            // テスト実施
            String result = lineWriter.getColumn(t, index);

            // 返却値の確認
            assertEquals("aaabbb", result);
        } finally {
            // テスト対象のクローズ処理
            lineWriter.closeFile();
        }
    }

    /**
     * testGetDelimiter01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) this.delimiter:not null<br>
     * ','<br>
     * <br>
     * 期待値：(戻り値) delimiter:not null<br>
     * ','<br>
     * <br>
     * delimiterのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetDelimiter01() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        VariableFileLineWriter<VariableFileLineWriter_Stub01> lineWriter = new VariableFileLineWriter<VariableFileLineWriter_Stub01>(
                fileName, VariableFileLineWriter_Stub01.class,
                columnFormatterMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        UTUtil.setPrivateField(lineWriter, "delimiter", ',');

        try {
            // テスト実施
            char result = lineWriter.getDelimiter();

            // 返却値の確認
            assertEquals(',', result);
            // 状態変化の確認
            // なし
        } finally {
            // テスト対象のクローズ処理
            lineWriter.closeFile();
        }
    }

    /**
     * testGetEncloseChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) this.encloseChar:not null<br>
     * '\u0000'<br>
     * <br>
     * 期待値：(戻り値) encloseChar:not null<br>
     * '\u0000'<br>
     * <br>
     * encloseCharのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetEncloseChar01() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        VariableFileLineWriter<VariableFileLineWriter_Stub01> lineWriter = new VariableFileLineWriter<VariableFileLineWriter_Stub01>(
                fileName, VariableFileLineWriter_Stub01.class,
                columnFormatterMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        UTUtil.setPrivateField(lineWriter, "encloseChar", '\u0000');

        try {
            // テスト実施
            char result = lineWriter.getEncloseChar();

            // 返却値の確認
            assertEquals('\u0000', result);

            // 状態変化の確認
            // なし
        } finally {
            // テスト対象のクローズ処理
            lineWriter.closeFile();
        }
    }

    /**
     * 正常系<br>
     * FileFormatのencloseCharとOutputFileColumnのcolumnEncloseCharによって、個々のカラムに囲み文字を設定
     * @throws Exception
     */
    @Test
    public void testPrintDataLine01() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        VariableFileLineWriter<CSVFileLine_Stub01> fileLineWriter = new VariableFileLineWriter<CSVFileLine_Stub01>(
                fileName, CSVFileLine_Stub01.class, columnFormatterMap);

        // 前処理(引数)
        CSVFileLine_Stub01 t1 = new CSVFileLine_Stub01();
        CSVFileLine_Stub01 t2 = new CSVFileLine_Stub01();
        CSVFileLine_Stub01 t3 = new CSVFileLine_Stub01();

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
            assertEquals("\"1\",22,333,|4444|", reader.readLine());
            assertEquals("\"5\",66,777,|8888|", reader.readLine());
            assertEquals("\"9\",AA,BBB,|CCCC|", reader.readLine());
        } finally {
            reader.close();
        }
    }

    /**
     * 正常系<br>
     * FileFormatのencloseCharとOutputFileColumnのcolumnEncloseCharによって、個々のカラムに囲み文字を設定
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

        VariableFileLineWriter<CSVFileLine_Stub02> fileLineWriter = new VariableFileLineWriter<CSVFileLine_Stub02>(
                fileName, CSVFileLine_Stub02.class, columnFormatterMap);

        // 前処理(引数)
        CSVFileLine_Stub02 t1 = new CSVFileLine_Stub02();
        CSVFileLine_Stub02 t2 = new CSVFileLine_Stub02();
        CSVFileLine_Stub02 t3 = new CSVFileLine_Stub02();

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
            assertEquals("\"1\",'22',\"333\",|4444|", reader.readLine());
            assertEquals("\"5\",'66',\"777\",|8888|", reader.readLine());
            assertEquals("\"9\",'AA',\"BBB\",|CCCC|", reader.readLine());
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

        VariableFileLineWriter<CSVFileLine_Stub03> fileLineWriter = new VariableFileLineWriter<CSVFileLine_Stub03>(
                fileName, CSVFileLine_Stub03.class, columnFormatterMap);

        // 前処理(引数)
        CSVFileLine_Stub03 t1 = new CSVFileLine_Stub03();

        t1.setColumn1("1");
        t1.setColumn2("22");
        t1.setColumn3("333");
        t1.setColumn4("4444");

        // ファイル行オブジェクトに設定してあった値を全て上書き
        // 以下の設定が適用されれば、ファイル行オブジェクトの
        // アノテーションにアクセスしていないことになる。
        char[] charArray = new char[] { 0, 0, 0, 0 };
        // 前提条件
        UTUtil.setPrivateField(fileLineWriter, "lineFeedChar", "\r\n");
        UTUtil.setPrivateField(fileLineWriter, "delimiter", '_');
        UTUtil.setPrivateField(fileLineWriter, "outputFileColumns", null);
        UTUtil.setPrivateField(fileLineWriter, "columnFormats", new String[] {
                "", "", "", "" });
        UTUtil.setPrivateField(fileLineWriter, "columnBytes", new int[] { -1,
                -1, -1, -1 });
        // UTUtil.setPrivateField(fileLineWriter, "totalBytes", 0);
        UTUtil.setPrivateField(fileLineWriter, "paddingTypes",
                new PaddingType[] { PaddingType.NONE, PaddingType.NONE,
                        PaddingType.NONE, PaddingType.NONE });
        UTUtil.setPrivateField(fileLineWriter, "paddingChars", charArray);
        UTUtil.setPrivateField(fileLineWriter, "trimChars", charArray);
        UTUtil.setPrivateField(fileLineWriter, "columnEncloseChar", charArray);
        UTUtil.setPrivateField(fileLineWriter, "stringConverters",
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
            assertEquals("1_22_333_4444", reader.readLine());
        } finally {
            reader.close();
        }
    }
}
