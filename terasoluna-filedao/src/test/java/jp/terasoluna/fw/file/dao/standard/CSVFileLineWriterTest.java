/*
 * $Id:$
 *
 * Copyright (c) 2006-2015 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
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

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.CSVFileLineWriter} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイル行オブジェクトからデータを読み込み、 1行分のデータをCSV形式でファイル に書き込む。<br>
 * AbstractFileLineWriterのサブクラス。
 * <p>
 * @author 奥田 哲司
 * @author 趙 俸徹
 * @see jp.terasoluna.fw.file.dao.standard.CSVFileLineWriter
 */
public class CSVFileLineWriterTest {

    private static final String TEMP_FILE_NAME = CSVFileLineWriterTest.class
            .getResource("CSVFileLineWriterTest_tmp.txt").getPath();

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
     * testCSVFileLineWriter01() <br>
     * <br>
     * (異常系) <br>
     * <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileName:"(パス)CSVFileLineWriter_testCSVFileLineWriter01.txt"<br>
     * (引数) clazz:CSVFileLineWriter_Stub05インスタンス<br>
     * @FileFormatの設定<br>
     *                    delimiter='、'<br>
     *                    (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                    ・"java.lang.String"=NullColumnFormatter.java<br>
     *                    <br>
     *                    期待値：(状態変化) 例外:"Delimiter can not change."のメッセージ、IllegalStateException、ファイル名を持つFileExceptionが発生する。<br>
     *                    <br>
     *                    例外。@FileFormatのdelimiterに初期値以外を設定した場合、例外が発生することを確認する。<br>
     *                    ファイル名が入力値のfileNameに一致することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCSVFileLineWriter01() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタの試験だから不要

        // 引数の設定
        String fileName = TEMP_FILE_NAME;

        Class<CSVFileLineWriter_Stub05> clazz = CSVFileLineWriter_Stub05.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // 前提条件の設定
        // なし
        CSVFileLineWriter<CSVFileLineWriter_Stub05> writer = null;

        try {
            // テスト実施
            writer = new CSVFileLineWriter<CSVFileLineWriter_Stub05>(fileName, clazz, columnFormatterMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(FileException.class, e.getClass());
            assertEquals("Delimiter can not change.", e.getMessage());
            assertEquals(IllegalStateException.class, e.getCause().getClass());
            assertEquals(fileName, e.getFileName());
        } finally {
            if (writer != null) {
                writer.closeFile();
            }
        }
    }

    /**
     * testCSVFileLineWriter02() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:"(パス)CSVFileLineWriter_testCSVFileLineWriter02.txt"<br>
     * (引数) clazz:CSVFileLineWriter_Stub01<br>
     * @FileFormatの設定<br>
     *                    delimiter以外=デフォルト値以外<br>
     *                    (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                    ・"java.lang.String"=NullColumnFormatter.java<br>
     *                    <br>
     *                    期待値：(状態変化) this.encloseChar:引数clazzのアノテーションFileFormatのencloseChar()の値。<br>
     *                    (状態変化) AbstractFileLineWriter#AbstractFileLineWriter():1回呼び出されること<br>
     *                    引数を確認すること<br>
     *                    (状態変化) AbstractFileLineWriter#init():1回呼び出されること<br>
     *                    <br>
     * @FileFormatのdelimiter以外の設定をデフォルト値ではないデータで設定した場合、コンストラクタの呼び出しが正常に行われることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCSVFileLineWriter02() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタの試験だから不要

        // 引数の設定
        String fileName = TEMP_FILE_NAME;

        Class<CSVFileLineWriter_Stub01> clazz = CSVFileLineWriter_Stub01.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // 前提条件の設定
        // なし

        CSVFileLineWriter<CSVFileLineWriter_Stub01> result = null;
        try {
            // テスト実施
            result = new CSVFileLineWriter<CSVFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);

            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals('\"', ReflectionTestUtils.getField(result,
                    "encloseChar"));

            assertEquals(fileName, ReflectionTestUtils.getField(result,
                    "fileName"));
            assertEquals(CSVFileLineWriter_Stub01.class, ReflectionTestUtils
                    .getField(result, "clazz"));
            assertEquals(columnFormatterMap, ReflectionTestUtils.getField(
                    result, "columnFormatterMap"));
        } finally {
            // テスト対象のクローズ処理
            if (result != null) {
                result.closeFile();
            }
        }
    }

    /**
     * 異常系<br>
     * ファイル行オブジェクトにOutputFileColumnアノテーションが無し
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCSVFileLineWriter03() throws Exception {
        // 引数の設定
        String fileName = TEMP_FILE_NAME;

        Class<FileLineObject_Empty> clazz = FileLineObject_Empty.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        CSVFileLineWriter<FileLineObject_Empty> writer = null;
        // テスト実施
        try {
            new CSVFileLineWriter<FileLineObject_Empty>(fileName, clazz, columnFormatterMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals("OutputFileColumn is not found.", e.getMessage());
            assertEquals(fileName, e.getFileName());
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        } finally {
            if (writer != null) {
                writer.closeFile();
            }
        }
    }

    /**
     * testGetColumn01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) t:CSVFileLineWriter_Stub06インスタンス<br>
     * @FileFormat()<br>
     *                   String変数column01<br>
     *                   アノテーション：@OutputFileColumn(columnIndex = 0)<br>
     *                   値："abcdef"<br>
     *                   (引数) index:0<br>
     *                   <br>
     *                   期待値：(戻り値) String:"abcdef"<br>
     *                   <br>
     *                   引数tに設定されたクラス（囲み文字が設定されていない場合）のカラムインデックス1の属性値（囲み文字が含まれない）が取得できることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetColumn01() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        CSVFileLineWriter<CSVFileLineWriter_Stub06> lineWriter = new CSVFileLineWriter<CSVFileLineWriter_Stub06>(fileName, CSVFileLineWriter_Stub06.class, columnFormatterMap);

        // 引数の設定
        CSVFileLineWriter_Stub06 stub = new CSVFileLineWriter_Stub06();
        stub.setColumn01("abcdef");

        // 前提条件の設定
        // なし

        try {
            // テスト実施
            String result = lineWriter.getColumn(stub, 0);

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
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) t:CSVFileLineWriter_Stub07インスタンス<br>
     * @FileFormat(encloseChar='\"')<br>
     *                                   String変数column01<br>
     *                                   アノテーション：@OutputFileColumn(columnIndex = 0)<br>
     *                                   値："abcdef"<br>
     *                                   (引数) index:0<br>
     *                                   <br>
     *                                   期待値：(戻り値) String:"abcdef"<br>
     *                                   <br>
     *                                   引数tに設定されたクラス（囲み文字が設定されている場合）のカラムインデックス1の属性値（囲み文字が含まれない）が取得できることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetColumn02() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        CSVFileLineWriter<CSVFileLineWriter_Stub07> lineWriter = new CSVFileLineWriter<CSVFileLineWriter_Stub07>(fileName, CSVFileLineWriter_Stub07.class, columnFormatterMap);

        // 引数の設定
        CSVFileLineWriter_Stub07 stub = new CSVFileLineWriter_Stub07();
        stub.setColumn01("abcdef");

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            String result = lineWriter.getColumn(stub, 0);

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
     * 観点：E <br>
     * <br>
     * 入力値：(引数) t:CSVFileLineWriter_Stub07インスタンス<br>
     * @FileFormat(encloseChar='\"')<br>
     *                                   String変数column01<br>
     *                                   アノテーション：@OutputFileColumn(columnIndex = 0)<br>
     *                                   値："ab\"cdef"<br>
     *                                   (引数) index:0<br>
     *                                   <br>
     *                                   期待値：(戻り値) String:"ab""cdef"<br>
     *                                   <br>
     *                                   引数tに設定されたクラス（囲み文字が設定されている場合）のカラムインデックス1の属性値（囲み文字が含まれる）がエスケープ処理されて、取得できることを確認するテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetColumn03() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        CSVFileLineWriter<CSVFileLineWriter_Stub07> lineWriter = new CSVFileLineWriter<CSVFileLineWriter_Stub07>(fileName, CSVFileLineWriter_Stub07.class, columnFormatterMap);

        // 引数の設定
        CSVFileLineWriter_Stub07 stub = new CSVFileLineWriter_Stub07();
        ReflectionTestUtils.setField(stub, "column01", "ab\"cdef");

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            String result = lineWriter.getColumn(stub, 0);

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
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) t:CSVFileLineWriter_Stub06インスタンス<br>
     * @FileFormat()<br>
     *                   String変数column01<br>
     *                   アノテーション：@OutputFileColumn(columnIndex = 0)<br>
     *                   値："abcdef"<br>
     *                   (引数) index:1<br>
     *                   <br>
     *                   (状態変化) 例外:ArrayIndexOutOfBoundsException<br>
     *                   AbstractFileLineWriter#getColumn()で発生する<br>
     *                   <br>
     *                   引数indexにカラムインデックスに存在しない値を渡すと、ArrayIndexOutOfBoundsExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetColumn04() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        CSVFileLineWriter<CSVFileLineWriter_Stub06> lineWriter = new CSVFileLineWriter<CSVFileLineWriter_Stub06>(fileName, CSVFileLineWriter_Stub06.class, columnFormatterMap);

        // 引数の設定
        CSVFileLineWriter_Stub06 stub = new CSVFileLineWriter_Stub06();
        ReflectionTestUtils.setField(stub, "column01", "abcdef");

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            lineWriter.getColumn(stub, 1);
            fail("ArrayIndexOutOfBoundsExceptionがスローされませんでした。");
        } catch (ArrayIndexOutOfBoundsException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(ArrayIndexOutOfBoundsException.class, e.getClass());
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
     * 入力値：(状態) this.delimiter:"','"<br>
     * <br>
     * 期待値：(戻り値) this.delimiter:"','"<br>
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

        CSVFileLineWriter<CSVFileLineWriter_Stub04> lineWriter = new CSVFileLineWriter<CSVFileLineWriter_Stub04>(fileName, CSVFileLineWriter_Stub04.class, columnFormatterMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // デフォルトで以下になっているため、何もしない
        // this.delimiter:"','"

        // テスト実施
        try {
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
     * 入力値：(状態) this.encloseChar:'\u0000'<br>
     * <br>
     * 期待値：(戻り値) this.encloseChar:'\u0000'<br>
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

        CSVFileLineWriter<CSVFileLineWriter_Stub04> lineWriter = new CSVFileLineWriter<CSVFileLineWriter_Stub04>(fileName, CSVFileLineWriter_Stub04.class, columnFormatterMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // デフォルトで以下になっているため、何もしない
        // this.encloseChar:\u0000'
        // テスト実施
        try {
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
     * OutputFileColumnのcolumnEncloseCharによって、個々のカラムに囲み文字を設定
     * @throws Exception
     */
    @Test
    public void testPrintDataLine01() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        CSVFileLineWriter<CSVFileLine_Stub01> fileLineWriter = new CSVFileLineWriter<CSVFileLine_Stub01>(fileName, CSVFileLine_Stub01.class, columnFormatterMap);

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
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
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
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        CSVFileLineWriter<CSVFileLine_Stub02> fileLineWriter = new CSVFileLineWriter<CSVFileLine_Stub02>(fileName, CSVFileLine_Stub02.class, columnFormatterMap);

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
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
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

        CSVFileLineWriter<CSVFileLine_Stub03> fileLineWriter = new CSVFileLineWriter<CSVFileLine_Stub03>(fileName, CSVFileLine_Stub03.class, columnFormatterMap);

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
        ReflectionTestUtils.setField(fileLineWriter, "lineFeedChar", "\r\n");
        ReflectionTestUtils.setField(fileLineWriter, "delimiter", '_');
        ReflectionTestUtils.setField(fileLineWriter, "outputFileColumns", null);
        ReflectionTestUtils.setField(fileLineWriter, "columnFormats",
                new String[] { "", "", "", "" });
        ReflectionTestUtils.setField(fileLineWriter, "columnBytes", new int[] {
                -1, -1, -1, -1 });
        // ReflectionTestUtils.setField(fileLineWriter, "totalBytes", 0);
        ReflectionTestUtils.setField(fileLineWriter, "paddingTypes",
                new PaddingType[] { PaddingType.NONE, PaddingType.NONE,
                        PaddingType.NONE, PaddingType.NONE });
        ReflectionTestUtils.setField(fileLineWriter, "paddingChars", charArray);
        ReflectionTestUtils.setField(fileLineWriter, "trimChars", charArray);
        ReflectionTestUtils.setField(fileLineWriter, "columnEncloseChar",
                charArray);
        ReflectionTestUtils.setField(fileLineWriter, "stringConverters",
                new NullStringConverter[] { new NullStringConverter(),
                        new NullStringConverter(), new NullStringConverter(),
                        new NullStringConverter() });

        // テスト実施
        fileLineWriter.printDataLine(t1);

        fileLineWriter.closeFile();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertEquals("1_22_333_4444", reader.readLine());
        } finally {
            reader.close();
        }
    }
}
