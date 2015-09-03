/*
 * $Id: AbstractFileLineWriterTest.java 5819 2007-12-20 05:55:47Z fukuyot $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.terasoluna.fw.file.annotation.NullStringConverter;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;
import jp.terasoluna.fw.file.annotation.PaddingType;
import jp.terasoluna.fw.file.annotation.StringConverter;
import jp.terasoluna.fw.file.annotation.StringConverterToLowerCase;
import jp.terasoluna.fw.file.annotation.StringConverterToUpperCase;
import jp.terasoluna.fw.file.annotation.TrimType;
import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.dao.FileLineException;
import jp.terasoluna.fw.file.ut.VMOUTUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.AbstractFileLineWriter} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイルアクセス(出力)のスーパークラス。抽象クラスのため、AbstractFileLineWriterImplクラスを作成して試験を実施する。
 * <p>
 * @author 奥田 哲司
 * @author 趙 俸徹
 * @see jp.terasoluna.fw.file.dao.standard.AbstractFileLineWriter
 */
public class AbstractFileLineWriterTest<T> {

    private static final String TEMP_FILE_NAME = AbstractFileLineWriterTest.class
            .getResource("AbstractFileLineWriterTest_tmp.txt").getPath();

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(AbstractFileLineWriterTest.class);
    }

    @Before
    public void setUp() throws Exception {
        VMOUTUtil.initialize();
        // ファイルの初期化
        File file = new File(TEMP_FILE_NAME);
        // file.delete();
        while (!file.delete()) {
            System.gc();
        }
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
     * testAbstractFileLineWriter01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_<init>01_fileName"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter："|"(デフォルト値以外)<br>
     * - encloseChar："\""(デフォルト値以外)<br>
     * - lineFeedChar："\r"(デフォルト値以外)<br>
     * - fileEncoding："UTF-8"(デフォルト値以外)<br>
     * - overWriteFlg：true(デフォルト値以外)<br>
     * ・フィールドを持ってない<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値：(状態変化) this.fileName:引数fileNameと同じインスタンス<br>
     * (状態変化) this.clazz:引数clazzのと同じインスタンス<br>
     * (状態変化) lineFeedChar:"\r"<br>
     * (状態変化) fileEncoding:"UTF-8"<br>
     * (状態変化) columnFormatterMap:引数columnFormatterMap同じインスタンス<br>
     * (状態変化) 例外:ない<br>
     * <br>
     * 正常パターン。<br>
     * 引数に設定された@FileFormatの情報に従って正しくAbstractFileLineWriterインスタンスが生成されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineWriter01() throws Exception {
        // 前処理(引数)
        String fileName = "AbstractFileLineWriter_<init>01_fileName";

        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> result = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);

        // 判定(戻り値)
        assertNotNull(result);

        assertSame(fileName, ReflectionTestUtils.getField(result, "fileName"));

        assertSame(clazz, ReflectionTestUtils.getField(result, "clazz"));

        assertEquals("\r", ReflectionTestUtils.getField(result,
                "lineFeedChar"));

        assertEquals("UTF-8", ReflectionTestUtils.getField(result,
                "fileEncoding"));

        assertSame(columnFormatterMap, ReflectionTestUtils.getField(result,
                "columnFormatterMap"));
    }

    /**
     * testAbstractFileLineWriter02() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:null<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter："|"(デフォルト値以外)<br>
     * - encloseChar："\""(デフォルト値以外)<br>
     * - lineFeedChar："\r"(デフォルト値以外)<br>
     * - fileEncoding："UTF-8"(デフォルト値以外)<br>
     * - overWriteFlg：true(デフォルト値以外)<br>
     * ・フィールドを持ってない<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："fileName is required."<br>
     * ・原因例外：IllegalArgumentException<br>
     * ・ファイル名：null<br>
     * <br>
     * 例外。<br>
     * ファイル名が設定されていない(null)場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineWriter02() throws Exception {
        // 前処理(引数)
        String fileName = null;

        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        try {
            new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)

            assertTrue(FileException.class.isAssignableFrom(e.getClass()));

            assertEquals("fileName is required.", e.getMessage());

            assertTrue(IllegalArgumentException.class.isAssignableFrom(e
                    .getCause().getClass()));
        }
    }

    /**
     * testAbstractFileLineWriter03() <br>
     * <br>
     * (異常系) <br>
     * 観点：E,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_<init>03_fileName"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持てない<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："FileFormat annotation is not found."<br>
     * ・原因例外：IllegalStateException<br>
     * ・ファイル名：引数fileNameと同じインスタンス。<br>
     * <br>
     * 例外。<br>
     * 引数clazzに渡されたクラスインスタンスに、@FileFormatの設定が存在しない場合は、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineWriter03() throws Exception {
        // 前処理(引数)
        String fileName = "AbstractFileLineWriter_<init>03_fileName";

        Class<AbstractFileLineWriter_Stub02> clazz = AbstractFileLineWriter_Stub02.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        try {
            new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub02>(fileName, clazz, columnFormatterMap);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));

            assertEquals("FileFormat annotation is not found.", e.getMessage());

            assertTrue(IllegalStateException.class.isAssignableFrom(e.getCause()
                    .getClass()));

            assertEquals(fileName, e.getFileName());
        }
    }

    /**
     * testAbstractFileLineWriter04() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,E <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_<init>04_fileName"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter："|"(デフォルト値以外)<br>
     * - encloseChar："\""(デフォルト値以外)<br>
     * - lineFeedChar：""(空文字、デフォルト値)<br>
     * - fileEncoding：""(空文字、デフォルト値)<br>
     * - overWriteFlg：true(デフォルト値以外)<br>
     * ・フィールドを持ってない<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値：(状態変化) this.fileName:引数fileNameと同じインスタンス<br>
     * (状態変化) this.clazz:引数clazzのと同じインスタンス<br>
     * (状態変化) lineFeedChar:システムの行区切り文字<br>
     * System.getProperty("line.separator")<br>
     * (状態変化) fileEncoding:システムのファイルエンコーディング<br>
     * System.getProperty("file.encoding")<br>
     * (状態変化) columnFormatterMap:引数columnFormatterMap同じインスタンス<br>
     * <br>
     * 正常パターン。<br>
     * 引数clazzに渡されたクラスインスタンスの＠FileFormatに「lineFeedChar」と「fileEncoding」が空文字で設定されている場合、AbstractFileLineWriterクラスのthis.
     * lineFeddCharとthis.fileEncodingがシステムデフォルト値で初期化されて生成されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineWriter04() throws Exception {
        // 前処理(引数)
        String fileName = "AbstractFileLineWriter_<init>04_fileName";

        Class<AbstractFileLineWriter_Stub03> clazz = AbstractFileLineWriter_Stub03.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        AbstractFileLineWriter<AbstractFileLineWriter_Stub03> result = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub03>(fileName, clazz, columnFormatterMap);
        // 判定(戻り値)
        assertNotNull(result);

        assertSame(fileName, ReflectionTestUtils.getField(result, "fileName"));

        assertSame(clazz, ReflectionTestUtils.getField(result, "clazz"));

        assertEquals(System.getProperty("line.separator"), ReflectionTestUtils.getField(result, "lineFeedChar"));

        assertEquals(System.getProperty("file.encoding"), ReflectionTestUtils.getField(result, "fileEncoding"));

        assertSame(columnFormatterMap, ReflectionTestUtils.getField(result,
                "columnFormatterMap"));
    }

    /**
     * testAbstractFileLineWriter05() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_<init>05_fileName"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter：";"(encloseCharと同じ値)<br>
     * - encloseChar：";"(delimiterと同じ値)<br>
     * - lineFeedChar："\r"(デフォルト値以外)<br>
     * - fileEncoding："UTF-8"(デフォルト値以外)<br>
     * - overWriteFlg：true(デフォルト値以外)<br>
     * ・フィールドを持ってない<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："Delimiter is the same as EncloseChar and is no use."<br>
     * ・原因例外：IllegalStateException<br>
     * ・ファイル名：引数fileNameと同じインスタンス。<br>
     * <br>
     * 例外。<br>
     * 引数clazzに渡されたクラスインスタンスの@FileFormaに「delimiter」と「encloseChar」が同じ場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineWriter05() throws Exception {
        // 前処理(引数)
        String fileName = "AbstractFileLineWriter_<init>05_fileName";

        Class<AbstractFileLineWriter_Stub04> clazz = AbstractFileLineWriter_Stub04.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        // テスト実施
        try {
            new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub04>(fileName, clazz, columnFormatterMap);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));

            assertEquals("Delimiter is the same as EncloseChar and is no use.",
                    e.getMessage());

            assertTrue(IllegalStateException.class.isAssignableFrom(e.getCause()
                    .getClass()));

            assertEquals(fileName, e.getFileName());
        }
    }

    /**
     * testAbstractFileLineWriter06() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * ""<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter："|"(デフォルト値以外)<br>
     * - encloseChar："\""(デフォルト値以外)<br>
     * - lineFeedChar："\r"(デフォルト値以外)<br>
     * - fileEncoding："UTF-8"(デフォルト値以外)<br>
     * - overWriteFlg：true(デフォルト値以外)<br>
     * ・フィールドを持ってない<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："fileName is required."<br>
     * ・原因例外：IllegalArgumentException<br>
     * ・ファイル名：""(空文字)<br>
     * <br>
     * 例外。<br>
     * ファイル名が空文字の場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineWriter06() throws Exception {
        // 前処理(引数)
        String fileName = "";

        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        try {
            new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));

            assertEquals("fileName is required.", e.getMessage());

            assertTrue(IllegalArgumentException.class.isAssignableFrom(e
                    .getCause().getClass()));
        }
    }

    /**
     * testAbstractFileLineWriter07() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_<init>07_fileName"<br>
     * (引数) clazz:null<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："clazz is required."<br>
     * ・原因例外：IllegalArgumentException<br>
     * ・ファイル名：引数fileNameと同じインスタンス。<br>
     * <br>
     * 例外。<br>
     * 引数clazzが「null」の場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineWriter07() throws Exception {
        // 前処理(引数)
        String fileName = "AbstractFileLineWriter_<init>07_fileName";

        Class<AbstractFileLineWriter_Stub01> clazz = null;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        try {
            new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));

            assertEquals("clazz is required.", e.getMessage());

            assertTrue(IllegalArgumentException.class.isAssignableFrom(e
                    .getCause().getClass()));
        }
    }

    /**
     * testAbstractFileLineWriter08() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_<init>08_fileName"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter："|"(デフォルト値以外)<br>
     * - encloseChar："\""(デフォルト値以外)<br>
     * - lineFeedChar："\r"(デフォルト値以外)<br>
     * - fileEncoding："UTF-8"(デフォルト値以外)<br>
     * - overWriteFlg：true(デフォルト値以外)<br>
     * ・フィールドを持ってない<br>
     * (引数) columnFormatterMap:null<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："columnFormaterMap is required."<br>
     * ・原因例外：IllegalArgumentException<br>
     * ・ファイル名：引数fileNameと同じインスタンス。<br>
     * <br>
     * 例外。<br>
     * 引数columnFormatterMapが「null」の場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineWriter08() throws Exception {
        // 前処理(引数)
        String fileName = "AbstractFileLineWriter_<init>08_fileName";

        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = null;

        // テスト実施
        try {
            new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));

            assertEquals("columnFormatterMap is required.", e.getMessage());

            assertTrue(IllegalArgumentException.class.isAssignableFrom(e
                    .getCause().getClass()));
        }
    }

    /**
     * testAbstractFileLineWriter09() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_<init>09_fileName"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter："|"(デフォルト値以外)<br>
     * - encloseChar："\""(デフォルト値以外)<br>
     * - lineFeedChar："\r"(デフォルト値以外)<br>
     * - fileEncoding："UTF-8"(デフォルト値以外)<br>
     * - overWriteFlg：true(デフォルト値以外)<br>
     * ・フィールドを持ってない<br>
     * (引数) columnFormatterMap:要素を持たないMap<String, ColumnFormatter>インスタンス<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："columnFormaterMap is required."<br>
     * ・原因例外：IllegalArgumentException<br>
     * ・ファイル名：引数fileNameと同じインスタンス。<br>
     * <br>
     * 例外。<br>
     * 引数columnFormatterMapはあるが、そのMapに要素が無い場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineWriter09() throws Exception {
        // 前処理(引数)
        String fileName = "AbstractFileLineWriter_<init>09_fileName";

        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();

        // テスト実施
        try {
            new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));

            assertEquals("columnFormatterMap is required.", e.getMessage());

            assertTrue(IllegalArgumentException.class.isAssignableFrom(e
                    .getCause().getClass()));
        }
    }

    /**
     * testAbstractFileLineWriter10() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_<init>10_fileName"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter："|"(デフォルト値以外)<br>
     * - encloseChar："\""(デフォルト値以外)<br>
     * - lineFeedChar："\r\n\t"(デフォルト値以外、3桁以上)<br>
     * - fileEncoding："UTF-8"(デフォルト値以外)<br>
     * - overWriteFlg：true(デフォルト値以外)<br>
     * ・フィールドを持ってない<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："lineFeedChar length must be 1 or 2. but: 3"<br>
     * ・原因例外：IllegalStateException<br>
     * ・ファイル名：引数fileNameと同じインスタンス。<br>
     * <br>
     * 例外。<br>
     * ＠FileFormatのlineFeedChar定義が3文字以上の場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineWriter10() throws Exception {
        // 前処理(引数)
        String fileName = "AbstractFileLineWriter_<init>10_fileName";

        Class<AbstractFileLineWriter_Stub05> clazz = AbstractFileLineWriter_Stub05.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        try {
            new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub05>(fileName, clazz, columnFormatterMap);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));

            assertEquals("lineFeedChar length must be 1 or 2. but: 3", e
                    .getMessage());

            assertTrue(IllegalStateException.class.isAssignableFrom(e.getCause()
                    .getClass()));
        }
    }

    /**
     * testAbstractFileLineWriter11() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_<init>11_fileName"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・フィールドを持ってない<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値：(状態変化) this.fileName:引数fileNameと同じインスタンス<br>
     * (状態変化) this.clazz:引数clazzのと同じインスタンス<br>
     * (状態変化) lineFeedChar:システムの行区切り文字<br>
     * System.getProperty("line.separator")<br>
     * (状態変化) fileEncoding:システムのファイルエンコーディング<br>
     * System.getProperty("file.encoding")<br>
     * (状態変化) columnFormatterMap:引数columnFormatterMap同じインスタンス<br>
     * (状態変化) 例外:ない<br>
     * <br>
     * 正常パターン。<br>
     * 引数に設定された@FileFormatの情報がデフォルトままの場合、AbstractFileLineWriterインスタンスが問題なく生成されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineWriter11() throws Exception {
        // 前処理(引数)
        String fileName = "AbstractFileLineWriter_<init>11_fileName";

        Class<AbstractFileLineWriter_Stub08> clazz = AbstractFileLineWriter_Stub08.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        AbstractFileLineWriter<AbstractFileLineWriter_Stub08> result = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub08>(fileName, clazz, columnFormatterMap);

        // 判定(戻り値)
        assertNotNull(result);

        assertSame(fileName, ReflectionTestUtils.getField(result, "fileName"));

        assertSame(clazz, ReflectionTestUtils.getField(result, "clazz"));

        assertEquals(System.getProperty("line.separator"), ReflectionTestUtils.getField(result, "lineFeedChar"));

        assertEquals(System.getProperty("file.encoding"), ReflectionTestUtils.getField(result, "fileEncoding"));

        assertSame(columnFormatterMap, ReflectionTestUtils.getField(result,
                "columnFormatterMap"));
    }

    /**
     * testAbstractFileLineWriter12() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_<init>13_fileName"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・フィールドを持ってない<br>
     * <br>
     * 以下親クラスの定義<br>
     * ・@FileFormatの設定を持たない<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値：(状態変化) this.fileName:引数fileNameと同じインスタンス<br>
     * (状態変化) this.clazz:引数clazzのと同じインスタンス<br>
     * (状態変化) lineFeedChar:システムの行区切り文字<br>
     * System.getProperty("line.separator")<br>
     * (状態変化) fileEncoding:システムのファイルエンコーディング<br>
     * System.getProperty("file.encoding")<br>
     * (状態変化) columnFormatterMap:引数columnFormatterMap同じインスタンス<br>
     * (状態変化) 例外:ない<br>
     * <br>
     * 正常パターン。<br>
     * 引数clazzが＠FileFormatを持たないクラスを継承している場合、設定された@FileFormatの情報に従ってAbstractFileLineWriterインスタンスが生成されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineWriter12() throws Exception {
        // 前処理(引数)
        String fileName = "AbstractFileLineWriter_<init>12_fileName";

        Class<AbstractFileLineWriter_Stub38> clazz = AbstractFileLineWriter_Stub38.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        AbstractFileLineWriter<AbstractFileLineWriter_Stub38> result = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub38>(fileName, clazz, columnFormatterMap);

        // 判定(戻り値)
        assertNotNull(result);

        assertSame(fileName, ReflectionTestUtils.getField(result, "fileName"));

        assertSame(clazz, ReflectionTestUtils.getField(result, "clazz"));

        assertEquals(System.getProperty("line.separator"), ReflectionTestUtils.getField(result, "lineFeedChar"));

        assertEquals(System.getProperty("file.encoding"), ReflectionTestUtils.getField(result, "fileEncoding"));

        assertSame(columnFormatterMap, ReflectionTestUtils.getField(result,
                "columnFormatterMap"));
    }

    /**
     * testAbstractFileLineWriter13() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_<init>12_fileName"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・フィールドを持ってない<br>
     * <br>
     * 以下親クラスの定義<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値以外<br>
     * ・フィールドを持ってない<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値：(状態変化) this.fileName:引数fileNameと同じインスタンス<br>
     * (状態変化) this.clazz:引数clazzのと同じインスタンス<br>
     * (状態変化) lineFeedChar:システムの行区切り文字<br>
     * System.getProperty("line.separator")<br>
     * (状態変化) fileEncoding:システムのファイルエンコーディング<br>
     * System.getProperty("file.encoding")<br>
     * (状態変化) columnFormatterMap:引数columnFormatterMap同じインスタンス<br>
     * (状態変化) 例外:ない<br>
     * <br>
     * 正常パターン。<br>
     * 引数clazzが＠FileFormatを持つクラスを継承している場合、親クラスではなくclazzに設定された@FileFormatの情報に従ってAbstractFileLineWriterインスタンスが生成されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineWriter13() throws Exception {
        // 前処理(引数)
        String fileName = "AbstractFileLineWriter_<init>13_fileName";

        Class<AbstractFileLineWriter_Stub39> clazz = AbstractFileLineWriter_Stub39.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // テスト実施
        AbstractFileLineWriter<AbstractFileLineWriter_Stub39> result = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub39>(fileName, clazz, columnFormatterMap);

        // 判定(戻り値)
        assertNotNull(result);

        assertSame(fileName, ReflectionTestUtils.getField(result, "fileName"));

        assertSame(clazz, ReflectionTestUtils.getField(result, "clazz"));

        assertEquals(System.getProperty("line.separator"), ReflectionTestUtils.getField(result, "lineFeedChar"));

        assertEquals(System.getProperty("file.encoding"), ReflectionTestUtils.getField(result, "fileEncoding"));

        assertSame(columnFormatterMap, ReflectionTestUtils.getField(result,
                "columnFormatterMap"));
    }

    /**
     * testInit01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "File_1Line.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter："|"(デフォルト値以外)<br>
     * - encloseChar："\""(デフォルト値以外)<br>
     * - lineFeedChar："\r"(デフォルト値以外)<br>
     * - fileEncoding："UTF-8"(デフォルト値以外)<br>
     * - overWriteFlg：true(デフォルト値以外)<br>
     * ・フィールドを持ってない<br>
     * (状態) this.calledInit:true<br>
     * (状態) this.fileEncoding:引数clazzの@FileFormatの設定に従う。<br>
     * (状態) this.writer:以下の設定を持つBufferedWriterインスタンス<br>
     * ・new BufferedWriter(<br>
     * new OutputStreamWriter(<br>
     * (new FileOutputStream(fileName, true)),<br>
     * fileEncoding))<br>
     * <br>
     * ※Writerの生成構造は複雑なため確認し難い。<br>
     * 今回はDJUnitで呼出し時の引数を確認することにする。<br>
     * (状態) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "File_1Line.txt"<br>
     * ・1行データあり<br>
     * <br>
     * 期待値：(状態変化) this.writer:以下の設定を持つBufferedWriterインスタンス<br>
     * ・new BufferedWriter(<br>
     * new OutputStreamWriter(<br>
     * (new FileOutputStream(fileName, true)),<br>
     * fileEncoding))<br>
     * <br>
     * ※変化なし<br>
     * (状態変化) #buildFields():呼ばれない<br>
     * (状態変化) #buildStringConverters():呼ばれない<br>
     * (状態変化) #buildMethods():呼ばれない<br>
     * (状態変化) file#delete():呼ばれない<br>
     * (状態変化) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "File_1Line.txt"<br>
     * ・1行データあり<br>
     * <br>
     * ※変化なし<br>
     * <br>
     * 正常パターン<br>
     * 既にinit()が実行されている場合は、処理を行わないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInit01() throws Exception {
        // 前処理(引数)
        String fileName = TEMP_FILE_NAME;

        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // 前処理(試験対象)
        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);

        BufferedWriter writer = null;
        BufferedReader postReader = null;
        try {
            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "calledInit",
                    Boolean.TRUE);

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"));

            ReflectionTestUtils.setField(fileLineWriter, "writer", writer);

            // 前処理(メソッド)
            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter_Stub01.class,
                    "buildFields", 0, null);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter_Stub01.class,
                    "buildStringConverts", 0, null);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter_Stub01.class,
                    "buildMethods", 0, null);

            // テスト実施
            fileLineWriter.init();

            // 判定(フィールド)
            assertSame(writer, ReflectionTestUtils.getField(fileLineWriter,
                    "writer"));

            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "buildFields"));

            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "buildStringConverts"));

            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "buildMethods"));

            assertFalse(VMOUTUtil.isCalled(File.class, "delete"));

            // 判定(ファイル)
            assertTrue(new File(fileName).exists());

            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (postReader != null) {
                postReader.close();
            }
        }
    }

    /**
     * testInit02() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "File_1Line.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter："|"(デフォルト値以外)<br>
     * - encloseChar："\""(デフォルト値以外)<br>
     * - lineFeedChar："\r"(デフォルト値以外)<br>
     * - fileEncoding："UTF-8"(デフォルト値以外)<br>
     * - overWriteFlg：true(デフォルト値以外)<br>
     * ・フィールドを持ってない<br>
     * (状態) this.calledInit:false<br>
     * (状態) this.fileEncoding:引数clazzの@FileFormatの設定に従う。<br>
     * (状態) this.writer:null<br>
     * (状態) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "File_1Line.txt"<br>
     * ・1行データあり<br>
     * <br>
     * 期待値：(状態変化) this.writer:以下の設定を持つBufferedWriterインスタンス<br>
     * ・new BufferedWriter(<br>
     * new OutputStreamWriter(<br>
     * (new FileOutputStream(fileName, true)),<br>
     * fileEncoding))<br>
     * <br>
     * ※Writerの生成構造は複雑なため確認し難い。<br>
     * 今回はDJUnitで呼出し時の引数を確認することにする。<br>
     * (状態変化) #buildFields():1回呼ばれる<br>
     * (状態変化) #buildStringConverters():1回呼ばれる<br>
     * (状態変化) #buildMethods():1回呼ばれる<br>
     * (状態変化) file#delete():1回呼ばれる<br>
     * (状態変化) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "File_1Line.txt"<br>
     * ・データなし<br>
     * <br>
     * 正常パターン。<br>
     * (overWriteFlg設定：True)<br>
     * 正しいファイル行オブジェクトに対するinit()処理が行われることを確認する。<br>
     * また、書き込み対象ファイルの情報が削除されていることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testInit02() throws Exception {
        // 前処理(引数)
        String fileName = TEMP_FILE_NAME;

        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // 前処理(試験対象)
        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "calledInit",
                Boolean.FALSE);

        ReflectionTestUtils.setField(fileLineWriter, "writer", null);

        BufferedReader postReader = null;
        try {
            // テスト実施
            fileLineWriter.init();

            // 判定(フィールド)
            assertNotNull(ReflectionTestUtils.getField(fileLineWriter,
                    "writer"));
            assertEquals(1, VMOUTUtil.getCallCount(BufferedWriter.class,
                    "<init>"));
            List bufferedWriterInitArguments = VMOUTUtil.getArguments(
                    BufferedWriter.class, "<init>", 0);
            assertEquals(1, bufferedWriterInitArguments.size());
            assertTrue(OutputStreamWriter.class.isAssignableFrom(
                    bufferedWriterInitArguments.get(0).getClass()));
            assertEquals(1, VMOUTUtil.getCallCount(OutputStreamWriter.class,
                    "<init>"));
            List outputStreamWriterInitArguments = VMOUTUtil.getArguments(
                    OutputStreamWriter.class, "<init>", 0);
            assertEquals(2, outputStreamWriterInitArguments.size());
            assertTrue(FileOutputStream.class.isAssignableFrom(
                    outputStreamWriterInitArguments.get(0).getClass()));
            assertEquals("UTF-8", outputStreamWriterInitArguments.get(1));
            assertEquals(1, VMOUTUtil.getCallCount(FileOutputStream.class,
                    "<init>"));
            List fileOutputStreamInitArguments = VMOUTUtil.getArguments(
                    FileOutputStream.class, "<init>", 0);
            assertEquals(2, fileOutputStreamInitArguments.size());
            assertEquals(fileName, fileOutputStreamInitArguments.get(0));
            assertFalse(Boolean.class.cast(fileOutputStreamInitArguments.get(
                    1)));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "buildFields"));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "buildStringConverters"));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "buildMethods"));

            // 判定(ファイル)
            assertTrue(new File(fileName).exists());

            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            assertFalse(postReader.ready());

        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(
                    fileLineWriter, "writer");
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }
        }
    }

    /**
     * testInit03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testInit03.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter："|"(デフォルト値以外)<br>
     * - encloseChar："\""(デフォルト値以外)<br>
     * - lineFeedChar："\r"(デフォルト値以外)<br>
     * - fileEncoding："UTF-8"(デフォルト値以外)<br>
     * - overWriteFlg：false(デフォルト値)<br>
     * ・フィールドを持ってない<br>
     * (状態) this.calledInit:false<br>
     * (状態) this.fileEncoding:引数clazzの@FileFormatの設定に従う。<br>
     * (状態) this.writer:null<br>
     * (状態) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testInit03.txt"<br>
     * ・1行データあり<br>
     * <br>
     * 期待値：(状態変化) this.writer:以下の設定を持つBufferedWriterインスタンス<br>
     * ・new BufferedWriter(<br>
     * new OutputStreamWriter(<br>
     * (new FileOutputStream(fileName, true)),<br>
     * fileEncoding))<br>
     * <br>
     * ※Writerの生成構造は複雑なため確認し難い。<br>
     * 今回はDJUnitで呼出し時の引数を確認することにする。<br>
     * (状態変化) #buildFields():1回呼ばれる<br>
     * (状態変化) #buildStringConverters():1回呼ばれる<br>
     * (状態変化) #buildMethods():1回呼ばれる<br>
     * (状態変化) file#delete():呼ばれない<br>
     * (状態変化) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testInit03.txt"<br>
     * ・1行データあり<br>
     * <br>
     * ※変化なし<br>
     * <br>
     * 正常パターン。<br>
     * (overWriteFlg設定：false(デフォルト値))<br>
     * 正しいファイル行オブジェクトに対するinit()処理が行われることを確認する。<br>
     * また、書き込み対象ファイルの情報が削除されないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testInit03() throws Exception {
        // 前処理(引数)
        String fileName = TEMP_FILE_NAME;

        Class<AbstractFileLineWriter_Stub06> clazz = AbstractFileLineWriter_Stub06.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // 前処理(試験対象)
        AbstractFileLineWriter<AbstractFileLineWriter_Stub06> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub06>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "calledInit",
                Boolean.FALSE);

        ReflectionTestUtils.setField(fileLineWriter, "writer", null);

        BufferedReader postReader = null;
        try {
            // テスト実施
            fileLineWriter.init();

            // 判定(フィールド)
            assertNotNull(ReflectionTestUtils.getField(fileLineWriter,
                    "writer"));
            assertEquals(1, VMOUTUtil.getCallCount(BufferedWriter.class,
                    "<init>"));
            List bufferedWriterInitArguments = VMOUTUtil.getArguments(
                    BufferedWriter.class, "<init>", 0);
            assertEquals(1, bufferedWriterInitArguments.size());
            assertTrue(OutputStreamWriter.class.isAssignableFrom(
                    bufferedWriterInitArguments.get(0).getClass()));
            assertEquals(1, VMOUTUtil.getCallCount(OutputStreamWriter.class,
                    "<init>"));
            List outputStreamWriterInitArguments = VMOUTUtil.getArguments(
                    OutputStreamWriter.class, "<init>", 0);
            assertEquals(2, outputStreamWriterInitArguments.size());
            assertTrue(FileOutputStream.class.isAssignableFrom(
                    outputStreamWriterInitArguments.get(0).getClass()));
            assertEquals("UTF-8", outputStreamWriterInitArguments.get(1));
            assertEquals(1, VMOUTUtil.getCallCount(FileOutputStream.class,
                    "<init>"));
            List fileOutputStreamInitArguments = VMOUTUtil.getArguments(
                    FileOutputStream.class, "<init>", 0);
            assertEquals(2, fileOutputStreamInitArguments.size());
            assertEquals(fileName, fileOutputStreamInitArguments.get(0));
            assertTrue(Boolean.class.cast(fileOutputStreamInitArguments.get(
                    1)));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "buildFields"));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "buildStringConverters"));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "buildMethods"));

            assertFalse(VMOUTUtil.isCalled(File.class, "delete"));

            // 判定(ファイル)
            assertTrue(new File(fileName).exists());

            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(
                    fileLineWriter, "writer");
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }
        }
    }

    /**
     * testInit04() <br>
     * <br>
     * (正常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testInit04.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter："|"(デフォルト値以外)<br>
     * - encloseChar："\""(デフォルト値以外)<br>
     * - lineFeedChar："\r"(デフォルト値以外)<br>
     * - fileEncoding："UTF-X"(存在しないエンコーディング)<br>
     * - overWriteFlg：true(デフォルト値以外)<br>
     * ・フィールドを持ってない<br>
     * (状態) this.calledInit:false<br>
     * (状態) this.fileEncoding:引数clazzの@FileFormatの設定に従う。<br>
     * (状態) this.writer:null<br>
     * (状態) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testInit04.txt"<br>
     * ・1行データあり<br>
     * <br>
     * 期待値：(状態変化) #buildFields():1回呼ばれる<br>
     * (状態変化) #buildStringConverters():1回呼ばれる<br>
     * (状態変化) #buildMethods():1回呼ばれる<br>
     * (状態変化) file#delete():呼ばれない<br>
     * (状態変化) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testInit03.txt"<br>
     * ・1行データあり<br>
     * <br>
     * ※変化なし<br>
     * (状態変化) -:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："Failed in generation of writer."<br>
     * ・原因例外：UnsupportedEncodingException<br>
     * ・fileName：引数fileNameと同じインスタンス。<br>
     * <br>
     * 異常パターン。<br>
     * 存在しないエンコーディングが設定された場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInit04() throws Exception {
        // 前処理(引数)
        String fileName = TEMP_FILE_NAME;

        Class<AbstractFileLineWriter_Stub07> clazz = AbstractFileLineWriter_Stub07.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // 前処理(試験対象)
        AbstractFileLineWriter<AbstractFileLineWriter_Stub07> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub07>(fileName, clazz, columnFormatterMap);
        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "calledInit",
                Boolean.FALSE);

        ReflectionTestUtils.setField(fileLineWriter, "writer", null);

        BufferedReader postReader = null;
        try {
            // テスト実施
            fileLineWriter.init();
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("Failed in generation of writer.", e.getMessage());
            assertTrue(UnsupportedEncodingException.class.isAssignableFrom(e
                    .getCause().getClass()));
            assertEquals(fileName, e.getFileName());

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "buildFields"));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "buildStringConverters"));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "buildMethods"));

            assertFalse(VMOUTUtil.isCalled(File.class, "delete"));

            // 判定(ファイル)
            assertTrue(new File(fileName).exists());

            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }
        }
    }

    /**
     * testInit05() <br>
     * <br>
     * (正常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * ".txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter："|"(デフォルト値以外)<br>
     * - encloseChar："\""(デフォルト値以外)<br>
     * - lineFeedChar："\r"(デフォルト値以外)<br>
     * - fileEncoding："UTF-8"(デフォルト値以外)<br>
     * - overWriteFlg：false(デフォルト値)<br>
     * ・フィールドを持ってない<br>
     * (状態) this.calledInit:false<br>
     * (状態) this.fileEncoding:引数clazzの@FileFormatの設定に従う。<br>
     * (状態) this.writer:null<br>
     * (状態) ファイル:クラスパスに以下のファイルおよび、ディレクトリは存在しない。<br>
     * "dummy/.txt"<br>
     * <br>
     * 期待値：(状態変化) #buildFields():1回呼ばれる<br>
     * (状態変化) #buildStringConverters():1回呼ばれる<br>
     * (状態変化) #buildMethods():1回呼ばれる<br>
     * (状態変化) file#delete():呼ばれない<br>
     * (状態変化) ファイル:クラスパスに以下のファイルおよび、ディレクトリは存在しない。<br>
     * "dummy/.txt"<br>
     * (状態変化) -:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："Failed in generation of writer."<br>
     * ・原因例外：FileNotFoundException<br>
     * ・fileName：引数fileNameと同じインスタンス。<br>
     * <br>
     * 異常パターン。<br>
     * 存在しないファイルが設定された場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInit05() throws Exception {
        // 前処理(引数)
        URL url = this.getClass().getResource("File_Empty.txt");

        String fileName = url.getPath().substring(0, url.getPath().indexOf(
                "File_Empty.txt"))
                + "dummy/AbstractFileLineWriter_testInit05.txt";

        Class<AbstractFileLineWriter_Stub06> clazz = AbstractFileLineWriter_Stub06.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // 前処理(試験対象)
        AbstractFileLineWriter<AbstractFileLineWriter_Stub06> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub06>(fileName, clazz, columnFormatterMap);
        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "calledInit",
                Boolean.FALSE);

        ReflectionTestUtils.setField(fileLineWriter, "writer", null);

        try {
            // テスト実施
            fileLineWriter.init();
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("Failed in generation of writer.", e.getMessage());
            assertTrue(FileNotFoundException.class.isAssignableFrom(e.getCause()
                    .getClass()));
            assertEquals(fileName, e.getFileName());

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "buildFields"));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "buildStringConverters"));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "buildMethods"));

            assertFalse(VMOUTUtil.isCalled(File.class, "delete"));

            // 判定(ファイル)
            assertFalse(new File(fileName).exists());
        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * testBuildFields01() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildFields01.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・フィールドを持ってない<br>
     * (状態) this.filelds:null<br>
     * (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値：(状態変化) this.fields:要素を持たないField配列<br>
     * <br>
     * 正常ケース<br>
     * (親クラスあり、フィールド定義なし)<br>
     * ファイル行オブジェクトクラス(親クラスも含む)にフィールド定義がない場合、fieldsが要素を持たない配列で初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testBuildFields01() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub38> clazz = AbstractFileLineWriter_Stub38.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub38> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub38>(fileName, clazz, columnFormatterMap);

        ReflectionTestUtils.setField(fileLineWriter, "fields", null);

        // テスト実施
        ReflectionTestUtils.invokeMethod(fileLineWriter, "buildFields", null);

        // 判定(状態変化、フィールド)
        Field[] resultFields = (Field[]) ReflectionTestUtils.getField(fileLineWriter,
                "fields");
        assertEquals(0, resultFields.length);
    }

    /**
     * testBuildFields02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildFields02.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn1<br>
     * (状態) this.filelds:null<br>
     * (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値： (状態変化) this.fields:要素を持たないField配列<br>
     * <br>
     * 正常ケース<br>
     * (親クラスなし、フィールド定義あり：1個)<br>
     * (@OutputFileColumn設定なし：1個)<br>
     * ファイル行オブジェクトクラスにフィールド定義がある場合、fieldsが正しく初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testBuildFields02() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub09> clazz = AbstractFileLineWriter_Stub09.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub09> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub09>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "fields", null);

        // テスト実施
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 判定(状態変化、フィールド)
        Field[] resultFields = (Field[]) ReflectionTestUtils.getField(fileLineWriter,
                "fields");
        assertEquals(0, resultFields.length);
    }

    /**
     * testBuildFields03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildFields03.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         (状態) this.filelds:null<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         <br>
     *                         期待値：(状態変化) this.fields:以下の要素を持つField配列<br>
     *                         １．Fieldオブジェクト：column1<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (親クラスなし、フィールド定義あり：1個)<br>
     *                         (@OutputFileColumn設定あり：1個)<br>
     *                         ファイル行オブジェクトクラスにフィールド定義がある場合、fieldsが正しく初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testBuildFields03() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "fields", null);

        // テスト実施
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 判定(状態変化、フィールド)
        Field[] resultFields = (Field[]) ReflectionTestUtils.getField(fileLineWriter,
                "fields");
        assertEquals(1, resultFields.length);
        Field resultFields1 = resultFields[0];
        assertEquals("column1", resultFields1.getName());
    }

    /**
     * testBuildFields04() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildFields04.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn1<br>
     * - フィールド：String noMappingColumn2<br>
     * - フィールド：String noMappingColumn3<br>
     * (状態) this.filelds:null<br>
     * (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"int"=IntColumnFormatter<br>
     * ・"java.lang.String"=NullColumnFormatter<br>
     * <br>
     * 期待値：(状態変化) this.fields:要素を持たないField配列<br>
     * <br>
     * 正常ケース<br>
     * (親クラスなし、フィールド定義あり：3個)<br>
     * (@OutputFileColumn設定なし：3個)<br>
     * ファイル行オブジェクトクラスにフィールド定義がある場合、fieldsが正しく初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testBuildFields04() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub11> clazz = AbstractFileLineWriter_Stub11.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub11> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub11>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "fields", null);

        // テスト実施
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 判定(状態変化、フィールド)
        Field[] resultFields = (Field[]) ReflectionTestUtils.getField(fileLineWriter,
                "fields");
        assertEquals(0, resultFields.length);
    }

    /**
     * testBuildFields05() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildFields05.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn1<br>
     * - フィールド：String noMappingColumn2<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         (状態) this.filelds:null<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         <br>
     *                         期待値：(状態変化) this.fields:以下の要素を持つField配列<br>
     *                         １．Fieldオブジェクト：column1<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (親クラスなし、フィールド定義あり：3個)<br>
     *                         (@OutputFileColumn設定なし：2個)<br>
     *                         (@OutputFileColumn設定あり：1個)<br>
     *                         ファイル行オブジェクトクラスにフィールド定義がある場合、fieldArrayとfieldsが正しく初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testBuildFields05() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub12> clazz = AbstractFileLineWriter_Stub12.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub12> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub12>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "fields", null);

        // テスト実施
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 判定(状態変化、フィールド)
        Field[] resultFields = (Field[]) ReflectionTestUtils.getField(fileLineWriter,
                "fields");
        assertEquals(1, resultFields.length);
        Field resultFields1 = resultFields[0];
        assertNotNull(resultFields1);
        assertEquals("column1", resultFields1.getName());
    }

    /**
     * testBuildFields06() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildFields06.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         (状態) this.filelds:null<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         <br>
     *                         期待値：(状態変化) this.fields:以下の要素を持つField配列<br>
     *                         １．Fieldオブジェクト：column1<br>
     *                         ２．Fieldオブジェクト：column2<br>
     *                         ３．Fieldオブジェクト：column3<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (親クラスなし、フィールド定義あり：3個)<br>
     *                         (@OutputFileColumn設定あり：3個)<br>
     *                         ファイル行オブジェクトクラスにフィールド定義がある場合、fieldsが正しく初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testBuildFields06() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub13> clazz = AbstractFileLineWriter_Stub13.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub13> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub13>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "fields", null);

        // テスト実施
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 判定(状態変化、フィールド)
        Field[] resultFields = (Field[]) ReflectionTestUtils.getField(fileLineWriter,
                "fields");
        assertEquals(3, resultFields.length);
        Field resultFieldsArray1 = resultFields[0];
        assertNotNull(resultFieldsArray1);
        assertEquals("column1", resultFieldsArray1.getName());
        Field resultFieldsArray2 = resultFields[1];
        assertNotNull(resultFieldsArray2);
        assertEquals("column2", resultFieldsArray2.getName());
        Field resultFieldsArray3 = resultFields[2];
        assertNotNull(resultFieldsArray3);
        assertEquals("column3", resultFieldsArray3.getName());
    }

    /**
     * testBuildFields07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildFields07.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         ※columnIndexが重複している。<br>
     *                         (状態) this.filelds:null<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         <br>
     *                         期待値：(状態変化) 例外:以下の設定を持つFileExceptionが発生する<br>
     *                         ･メッセージ："Column Index is duplicate : 1"<br>
     *                         ・fileName：this.fileNameと同じインスタンス。<br>
     *                         <br>
     *                         異常ケース<br>
     *                         (親クラスなし、フィールド定義あり：3個)<br>
     *                         (@OutputFileColumn設定あり：3個)<br>
     *                         ファイル行オブジェクトクラスにフィールド定義でcolumnIndexが重複した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testBuildFields07() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub14> clazz = AbstractFileLineWriter_Stub14.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub14> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub14>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "fields", null);

        // テスト実施
        try {
            Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
            method.setAccessible(true);
            method.invoke(fileLineWriter);
            fail("FileExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getTargetException().getClass()));

            assertEquals("Column Index is duplicate : 1", e.getTargetException().getMessage());

            assertEquals(fileName, ((FileException)e.getTargetException()).getFileName());
        }
    }

    /**
     * testBuildFields08() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildFields08.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         ※各フィールドの順番は逆順（順番ではない）のこと。<br>
     *                         (状態) this.filelds:null<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         <br>
     *                         期待値：(状態変化)this.fields:以下の要素を持つField配列<br>
     *                         １．Fieldオブジェクト：column1<br>
     *                         ２．Fieldオブジェクト：column2<br>
     *                         ３．Fieldオブジェクト：column3<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (親クラスなし、フィールド定義あり：3個)<br>
     *                         (@OutputFileColumn設定あり：3個)<br>
     *                         ファイル行オブジェクトクラスにフィールド定義がある場合、fieldsが正しく初期化されることを確認する。<br>
     *                         また、fieldの定義順番ではなくcolumnIndex順番に格納されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testBuildFields08() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub15> clazz = AbstractFileLineWriter_Stub15.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub15> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub15>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "fields", null);

        // テスト実施
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 判定(状態変化、フィールド)
        Field[] resultFields = (Field[]) ReflectionTestUtils.getField(fileLineWriter,
                "fields");
        assertEquals(3, resultFields.length);
        Field resultFieldsArray1 = resultFields[0];
        assertNotNull(resultFieldsArray1);
        assertEquals("column1", resultFieldsArray1.getName());
        Field resultFieldsArray2 = resultFields[1];
        assertNotNull(resultFieldsArray2);
        assertEquals("column2", resultFieldsArray2.getName());
        Field resultFieldsArray3 = resultFields[2];
        assertNotNull(resultFieldsArray3);
        assertEquals("column3", resultFieldsArray3.getName());

    }

    /**
     * testBuildFields09() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildFields09.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         (状態) this.filelds:null<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         <br>
     *                         期待値：(状態変化) 例外:以下の設定を持つFileExceptionが発生する<br>
     *                         ･メッセージ："Column Index in FileLineObject is bigger than the total number of the field."<br>
     *                         ・原因例外：IllegalStateException<br>
     *                         ・fileName：this.fileNameと同じインスタンス。<br>
     *                         <br>
     *                         異常ケース<br>
     *                         (親クラスなし、フィールド定義あり：1個)<br>
     *                         (@OutputFileColumn設定あり：1個)<br>
     *                         ファイル行オブジェクトクラスにフィールド定義でcolumnIndexをフィールドの数分以上を設定した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testBuildFields09() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub16> clazz = AbstractFileLineWriter_Stub16.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub16> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub16>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "fields", null);

        // テスト実施
        try {
            Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
            method.setAccessible(true);
            method.invoke(fileLineWriter);
            fail("FileExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定(例外)
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getTargetException().getCause()
                    .getClass()));
            assertEquals("Column Index in FileLineObject is bigger than the "
                    + "total number of the field.", e.getTargetException().getMessage());
            assertEquals(fileName, ((FileException)e.getTargetException()).getFileName());

            // 判定(状態変化、フィールド)
            Field[] resultFields = (Field[]) ReflectionTestUtils.getField(
                    fileLineWriter, "fields");
            assertNull(resultFields);
        }
    }

    /**
     * testBuildFields10() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildFields10.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：-1<br>
     *                         > その他項目：デフォルト値<br>
     *                         (状態) this.filelds:null<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         <br>
     *                         期待値：(状態変化) 例外:以下の設定を持つFileExceptionが発生する<br>
     *                         ･メッセージ："Column Index in FileLineObject is the minus number."<br>
     *                         ・原因例外：IllegalStateException<br>
     *                         ・fileName：this.fileNameと同じインスタンス。<br>
     *                         <br>
     *                         異常ケース<br>
     *                         (親クラスなし、フィールド定義あり：1個)<br>
     *                         (@OutputFileColumn設定あり：1個)<br>
     *                         ファイル行オブジェクトクラスにフィールド定義でcolumnIndexをマイナス値を設定した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testBuildFields10() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub17> clazz = AbstractFileLineWriter_Stub17.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub17> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub17>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "fields", null);

        // テスト実施
        try {
            Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
            method.setAccessible(true);
            method.invoke(fileLineWriter);
            fail("FileExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定(例外)
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getTargetException().getCause()
                    .getClass()));
            assertEquals("Column Index in FileLineObject is the minus number.",
                    e.getTargetException().getMessage());
            assertEquals(fileName, ((FileException)e.getTargetException()).getFileName());

            // 判定(状態変化、フィールド)
            Field[] resultFields = (Field[]) ReflectionTestUtils.getField(
                    fileLineWriter, "fields");
            assertNull(resultFields);
        }
    }

    /**
     * testBuildFields11() <br>
     * <br>
     * (異常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildFields11.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn1<br>
     * - フィールド：String noMappingColumn2<br>
     * - フィールド：String noMappingColumn3<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         <br>
     *                         ※columnIndexに欠番がある。<br>
     *                         (状態) this.filelds:null<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         <br>
     *                         期待値：(状態変化) 例外:以下の設定を持つFileExceptionが発生する<br>
     *                         ･メッセージ："columnIndex in FileLineObject is not sequential order."<br>
     *                         ・原因例外：IllegalStateException<br>
     *                         ・fileName：this.fileNameと同じインスタンス。<br>
     *                         <br>
     *                         異常ケース<br>
     *                         (親クラスなし、フィールド定義あり：5個)<br>
     *                         (@OutputFileColumn設定なし：3個)<br>
     *                         (@OutputFileColumn設定あり：2個)<br>
     *                         ファイル行オブジェクトクラスにフィールド定義でcolumnIndexの定義で欠番がある場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testBuildFields11() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub18> clazz = AbstractFileLineWriter_Stub18.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub18> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub18>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "fields", null);

        // テスト実施
        try {
            Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
            method.setAccessible(true);
            method.invoke(fileLineWriter);
            fail("FileExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getTargetException().getClass()));
            assertEquals(
                    "columnIndex in FileLineObject is not sequential order.", e.getTargetException()
                            .getMessage());
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getTargetException().getCause()
                    .getClass()));
        }
    }

    /**
     * testBuildFields12() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildFields12.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn1<br>
     * - フィールド：String noMappingColumn2<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         <br>
     *                         以下親クラスの定義<br>
     *                         ・@OutputFileColumn設定ありのフィールドを持つ<br>
     *                         - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         <br>
     *                         ※親クラスにフィールドの情報がある。<br>
     *                         (状態) this.filelds:null<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         <br>
     *                         期待値：(状態変化) this.fields:以下の要素を持つField配列<br>
     *                         １．Fieldオブジェクト：column1<br>
     *                         ２．Fieldオブジェクト：column2<br>
     *                         ３．Fieldオブジェクト：column3<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (親クラスあり、フィールド定義あり：5個)<br>
     *                         (@OutputFileColumn設定なし：2個)<br>
     *                         (@OutputFileColumn設定あり：3個(親クラス1個))<br>
     *                         ファイル行オブジェクトクラスにフィールド定義がある正しくされている場合、fieldsが正しく初期化されることを確認する。<br>
     *                         親クラスのフィールドの定義も認識することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testBuildFields12() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub19> clazz = AbstractFileLineWriter_Stub19.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub19> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub19>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "fields", null);

        // テスト実施
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 判定(状態変化、フィールド)
        Field[] resultFields = (Field[]) ReflectionTestUtils.getField(fileLineWriter,
                "fields");
        assertEquals(3, resultFields.length);
        Field resultFields1 = resultFields[0];
        assertNotNull(resultFields1);
        assertEquals("column1", resultFields1.getName());
        Field resultFields2 = resultFields[1];
        assertNotNull(resultFields2);
        assertEquals("column2", resultFields2.getName());
        Field resultFields3 = resultFields[2];
        assertNotNull(resultFields3);
        assertEquals("column3", resultFields3.getName());
    }

    /**
     * testBuildFields13() <br>
     * <br>
     * (異常系) <br>
     * 観点：B,G <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildFields15.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：long column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         ※サポートしないタイプのフィールドがある。<br>
     *                         (状態) this.filelds:null<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         <br>
     *                         期待値：(状態変化) this.fields:null<br>
     *                         (状態変化) 例外:以下の設定を持つFileExceptionが発生する<br>
     *                         ･メッセージ："There is a type which isn't supported in a mapping target field in FileLineObject."<br>
     *                         ・原因例外：IllegalStateException<br>
     *                         ・fileName：this.fileNameと同じインスタンス。<br>
     *                         <br>
     *                         異常ケース<br>
     *                         (親クラスなし、フィールド定義あり：3個)<br>
     *                         (@OutputFileColumn設定あり：3個)<br>
     *                         ファイル行オブジェクトクラスのフィールド型がthis.columnFormatterrMapに存在しない型の場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testBuildFields13() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub40> clazz = AbstractFileLineWriter_Stub40.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub40> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub40>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "fields", null);

        // テスト実施
        try {
            Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
            method.setAccessible(true);
            method.invoke(fileLineWriter);
            fail("FileExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定(例外)
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getTargetException().getCause()
                    .getClass()));
            assertEquals("There is a type which isn't supported in a mapping "
                    + "target field in FileLineObject.", e.getTargetException().getMessage());
            assertEquals(fileName, ((FileException)e.getTargetException()).getFileName());

            // 判定(状態変化、フィールド)
            Field[] resultFields = (Field[]) ReflectionTestUtils.getField(
                    fileLineWriter, "fields");
            assertNull(resultFields);
        }
    }

    /**
     * testBuildStringConverters01() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildStringConverters01.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・フィールドを持ってない<br>
     * (状態) this.stringConverterCacheMap:要素を持たないMapインスタンス<br>
     * <br>
     * 期待値：(状態変化) this.stringConverterCacheMap:要素を持たないMapインスタンス<br>
     * (状態変化) this.stringConverters:要素を持たないStringConverter配列<br>
     * <br>
     * 正常ケース<br>
     * ファイル行オブジェクトクラスがフィールドを持たない場合、stringConveters配列が空で初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @SuppressWarnings("unchecked")
    @Test
    public void testBuildStringConverters01() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub08> clazz = AbstractFileLineWriter_Stub08.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub08> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub08>(fileName, clazz, columnFormatterMap);
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 前処理(フィールド)
        Map<Class, StringConverter> preStringConverterCacheMap = new HashMap<Class, StringConverter>();
        Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        field.set(AbstractFileLineWriter.class, preStringConverterCacheMap);

        try {
            // テスト実施
            method = AbstractFileLineWriter.class.getDeclaredMethod("buildStringConverters");
            method.setAccessible(true);
            method.invoke(fileLineWriter);

            // 判定(状態変化、フィールド)
            field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            Map<Class, StringConverter> stringConverterCacheMap =
                    (Map<Class, StringConverter>) field.get(fileLineWriter);        
            assertNotNull(stringConverterCacheMap);
            assertEquals(0, stringConverterCacheMap.size());

            field = AbstractFileLineWriter.class.getDeclaredField("stringConverters");
            field.setAccessible(true);
            StringConverter[] stringConverters = (StringConverter[]) field.get(fileLineWriter);        
            assertNotNull(stringConverters);
            assertEquals(0, stringConverters.length);
        } finally {
            field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());
        }
    }

    /**
     * testBuildStringConverters02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildStringConverters02.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > StringConverter.class：NullStringConverter.class<br>
     *                         > その他項目：デフォルト値<br>
     *                         (状態) this.stringConverterCacheMap:要素を持たないMapインスタンス<br>
     *                         <br>
     *                         期待値：(状態変化) this.stringConverterCacheMap:以下の要素を持つMapインスタンス<br>
     *                         ・key：NullStringConverter.class,<br>
     *                         value：NullStringConverterインスタンス<br>
     *                         (状態変化) this.stringConverters:以下の要素を持つStringConverter配列<br>
     *                         1．NullStringConverterインスタンス(キャッシュされたものと同じインスタンス)<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (stringConverter設定ありフィールド：1個)<br>
     *                         ファイル行オブジェクトクラスのフィールドにstringConverter設定がある場合、stringConverters配列が正しく生成されることと、
     *                         そのインスタンスがキャッシュされることを確認する 。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @SuppressWarnings("unchecked")
    @Test
    public void testBuildStringConverters02() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub20> clazz = AbstractFileLineWriter_Stub20.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub20> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub20>(fileName, clazz, columnFormatterMap);
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 前処理(フィールド)
        Map<Class, StringConverter> preStringConverterCacheMap = new HashMap<Class, StringConverter>();
        Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        field.set(AbstractFileLineWriter.class, preStringConverterCacheMap);

        try {
            // テスト実施
            method = AbstractFileLineWriter.class.getDeclaredMethod("buildStringConverters");
            method.setAccessible(true);
            method.invoke(fileLineWriter);

            // 判定(状態変化、フィールド)
            Map<Class, StringConverter> stringConverterCacheMap = (Map<Class, StringConverter>) ReflectionTestUtils.getField(fileLineWriter, "stringConverterCacheMap");
            assertNotNull(stringConverterCacheMap);
            assertEquals(1, stringConverterCacheMap.size());
            assertTrue(stringConverterCacheMap.containsKey(
                    NullStringConverter.class));
            StringConverter nullStringConverter = stringConverterCacheMap.get(
                    NullStringConverter.class);
            assertNotNull(nullStringConverter);
            assertTrue(NullStringConverter.class.isAssignableFrom(
                    nullStringConverter.getClass()));

            StringConverter[] stringConverters = (StringConverter[]) ReflectionTestUtils.getField(fileLineWriter, "stringConverters");
            assertNotNull(stringConverters);
            assertEquals(1, stringConverters.length);
            StringConverter stringConverters1 = stringConverters[0];
            assertSame(nullStringConverter, stringConverters1);
        } finally {
            field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());
        }
    }

    /**
     * testBuildStringConverters03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildStringConverters03.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > StringConverter.class：NullStringConverter.class<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > StringConverter.class：NullStringConverter.class<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > StringConverter.class：NullStringConverter.class<br>
     *                         > その他項目：デフォルト値<br>
     *                         (状態) this.stringConverterCacheMap:要素を持たないMapインスタンス<br>
     *                         <br>
     *                         期待値：(状態変化) this.stringConverterCacheMap:以下の要素を持つMapインスタンス<br>
     *                         ・key：NullStringConverter.class,<br>
     *                         value：NullStringConverterインスタンス<br>
     *                         (状態変化) this.stringConverters:以下の要素を持つStringConverter配列<br>
     *                         1．NullStringConverterインスタンス(キャッシュされたものと同じインスタンス)<br>
     *                         2．NullStringConverterインスタンス(キャッシュされたものと同じインスタンス)<br>
     *                         3．NullStringConverterインスタンス(キャッシュされたものと同じインスタンス)<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (stringConverter設定ありフィールド：3個、<br>
     *                         同じstringConverterを利用する。)<br>
     *                         stringConverter設定がある場合、stringConverters配列が正しく生成されることと、そのインスタンスがキャッシュされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @SuppressWarnings("unchecked")
    @Test
    public void testBuildStringConverters03() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub21> clazz = AbstractFileLineWriter_Stub21.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub21> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub21>(fileName, clazz, columnFormatterMap);
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 前処理(フィールド)
        Map<Class, StringConverter> preStringConverterCacheMap = new HashMap<Class, StringConverter>();
        Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        field.set(AbstractFileLineWriter.class, preStringConverterCacheMap);

        try {
            // テスト実施
            method = AbstractFileLineWriter.class.getDeclaredMethod("buildStringConverters");
            method.setAccessible(true);
            method.invoke(fileLineWriter);

            // 判定(状態変化、フィールド)
            field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            Map<Class, StringConverter> stringConverterCacheMap =
                    (Map<Class, StringConverter>) field.get(fileLineWriter);        
            assertNotNull(stringConverterCacheMap);
            assertEquals(1, stringConverterCacheMap.size());
            assertTrue(stringConverterCacheMap.containsKey(
                    NullStringConverter.class));
            StringConverter nullStringConverter = stringConverterCacheMap.get(
                    NullStringConverter.class);
            assertNotNull(nullStringConverter);
            assertTrue(NullStringConverter.class.isAssignableFrom(
                    nullStringConverter.getClass()));

            field = AbstractFileLineWriter.class.getDeclaredField("stringConverters");
            field.setAccessible(true);
            StringConverter[] stringConverters = (StringConverter[]) field.get(fileLineWriter);        
            
            assertNotNull(stringConverters);
            assertEquals(3, stringConverters.length);
            StringConverter stringConverters1 = stringConverters[0];
            assertNotNull(stringConverters1);
            assertSame(nullStringConverter, stringConverters1);
            StringConverter stringConverters2 = stringConverters[1];
            assertNotNull(stringConverters2);
            assertSame(nullStringConverter, stringConverters2);
            StringConverter stringConverters3 = stringConverters[2];
            assertNotNull(stringConverters3);
            assertSame(nullStringConverter, stringConverters3);
        } finally {
            field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());
        }
    }

    /**
     * testBuildStringConverters04() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildStringConverters04.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > stringConverter：NullStringConverter.class<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > stringConverter：SringConverterToLowerCase.class<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > stringConverter：SringConverterToUpperCase.class<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column4<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：3<br>
     *                         > stringConverter：NullStringConverter.class<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column5<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：4<br>
     *                         > stringConverter：SringConverterToLowerCase.class<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column6<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：5<br>
     *                         > stringConverter：SringConverterToUpperCase.class<br>
     *                         > その他項目：デフォルト値<br>
     *                         (状態) this.stringConverterCacheMap:要素を持たないMapインスタンス<br>
     *                         <br>
     *                         期待値：(状態変化) this.stringConverterCacheMap:以下の要素を持つMapインスタンス<br>
     *                         ・key：NullStringConverter.class,<br>
     *                         value：NullStringConverterインスタンス<br>
     *                         ・key：SringConverterToLowerCase.class,<br>
     *                         value：SringConverterToLowerCaseインスタンス<br>
     *                         ・key：SringConverterToUpperCase.class,<br>
     *                         value：SringConverterToUpperCaseインスタンス<br>
     *                         (状態変化) this.stringConverters:以下の要素を持つStringConverter配列<br>
     *                         1．NullStringConverterインスタンス(キャッシュされたものと同じインスタンス)<br>
     *                         2．SringConverterToLowerCaseインスタンス(キャッシュされたものと同じインスタンス)<br>
     *                         3．SringConverterToUpperCaseインスタンス(キャッシュされたものと同じインスタンス)<br>
     *                         4．NullStringConverterインスタンス(キャッシュされたものと同じインスタンス)<br>
     *                         5．SringConverterToLowerCaseインスタンス(キャッシュされたものと同じインスタンス)<br>
     *                         6．SringConverterToUpperCaseインスタンス(キャッシュされたものと同じインスタンス)<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (stringConverter設定ありフィールド：5個、<br>
     *                         同じstringConverterを利用する。)<br>
     *                         stringConverter設定がある場合、stringConverters配列が正しく生成されることと、そのインスタンスがキャッシュされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @SuppressWarnings("unchecked")
    @Test
    public void testBuildStringConverters04() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub22> clazz = AbstractFileLineWriter_Stub22.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub22> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub22>(fileName, clazz, columnFormatterMap);
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 前処理(フィールド)
        Map<Class, StringConverter> preStringConverterCacheMap = new HashMap<Class, StringConverter>();
        Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        field.set(AbstractFileLineWriter.class, preStringConverterCacheMap);

        try {
            // テスト実施
            method = AbstractFileLineWriter.class.getDeclaredMethod("buildStringConverters");
            method.setAccessible(true);
            method.invoke(fileLineWriter);

            // 判定(状態変化、フィールド)
            field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            Map<Class, StringConverter> stringConverterCacheMap =
                    (Map<Class, StringConverter>) field.get(fileLineWriter);        

            assertNotNull(stringConverterCacheMap);
            assertEquals(3, stringConverterCacheMap.size());
            assertTrue(stringConverterCacheMap.containsKey(
                    NullStringConverter.class));
            StringConverter nullStringConverter = stringConverterCacheMap.get(
                    NullStringConverter.class);
            assertNotNull(nullStringConverter);
            assertTrue(NullStringConverter.class.isAssignableFrom(
                    nullStringConverter.getClass()));
            assertTrue(stringConverterCacheMap.containsKey(
                    NullStringConverter.class));
            StringConverter stringConverterToLowerCase = stringConverterCacheMap
                    .get(StringConverterToLowerCase.class);
            assertNotNull(stringConverterToLowerCase);
            assertTrue(StringConverterToLowerCase.class.isAssignableFrom(
                    stringConverterToLowerCase.getClass()));
            assertTrue(stringConverterCacheMap.containsKey(
                    StringConverterToUpperCase.class));
            StringConverter stringConverterToUpperCase = stringConverterCacheMap
                    .get(StringConverterToUpperCase.class);
            assertNotNull(stringConverterToUpperCase);
            assertTrue(StringConverterToUpperCase.class.isAssignableFrom(
                    stringConverterToUpperCase.getClass()));

            field = AbstractFileLineWriter.class.getDeclaredField("stringConverters");
            field.setAccessible(true);
            StringConverter[] stringConverters = (StringConverter[]) field.get(fileLineWriter); 
            assertNotNull(stringConverters);
            assertEquals(6, stringConverters.length);
            StringConverter stringConverters1 = stringConverters[0];
            assertNotNull(stringConverters1);
            assertSame(nullStringConverter, stringConverters1);
            StringConverter stringConverters2 = stringConverters[1];
            assertNotNull(stringConverters2);
            assertSame(stringConverterToLowerCase, stringConverters2);
            StringConverter stringConverters3 = stringConverters[2];
            assertNotNull(stringConverters3);
            assertSame(stringConverterToUpperCase, stringConverters3);
            StringConverter stringConverters4 = stringConverters[3];
            assertNotNull(stringConverters4);
            assertSame(nullStringConverter, stringConverters4);
            StringConverter stringConverters5 = stringConverters[4];
            assertNotNull(stringConverters5);
            assertSame(stringConverterToLowerCase, stringConverters5);
            StringConverter stringConverters6 = stringConverters[5];
            assertNotNull(stringConverters6);
            assertSame(stringConverterToUpperCase, stringConverters6);
        } finally {
            field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());
        }
    }

    /**
     * testBuildStringConverters05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildStringConverters05.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > stringConverter：デフォルトコンストラクタを持ってないStringConverterのクラスインスタンス<br>
     *                         > その他項目：デフォルト値<br>
     *                         (状態) this.stringConverterCacheMap:要素を持たないMapインスタンス<br>
     *                         <br>
     *                         期待値：(状態変化) -:以下の情報を持つFileLineException()が発生する<br>
     *                         ・メッセージ："Failed in an instantiate of a stringConverter."<br>
     *                         ・原因例外：InstantiationException<br>
     *                         ・ファイル名：fileNameと同じインスタンス<br>
     *                         ・行数：-1<br>
     *                         ・カラム名：column1<br>
     *                         ・カラム番号：0<br>
     *                         <br>
     *                         異常ケース<br>
     *                         指定したStringConverterにデフォルトコンストラクタが存在しない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBuildStringConverters05() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub23> clazz = AbstractFileLineWriter_Stub23.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub23> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub23>(fileName, clazz, columnFormatterMap);
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 前処理(フィールド)
        Map<Class, StringConverter> preStringConverterCacheMap = new HashMap<Class, StringConverter>();
        Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        field.set(AbstractFileLineWriter.class, preStringConverterCacheMap);

        try {
            // テスト実施
            method = AbstractFileLineWriter.class.getDeclaredMethod("buildStringConverters");
            method.setAccessible(true);
            method.invoke(fileLineWriter);
            fail("FileLineExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定(例外)
            assertTrue(FileLineException.class.isAssignableFrom(e.getTargetException().getClass()));
            assertEquals("Failed in an instantiate of a stringConverter.", e.getTargetException()
                    .getMessage());
            assertTrue(InstantiationException.class.isAssignableFrom(e.getTargetException()
                    .getCause().getClass()));
            assertEquals(fileName, ((FileLineException)e.getTargetException()).getFileName());
            assertEquals(-1, ((FileLineException)e.getTargetException()).getLineNo());
            assertEquals("column1", ((FileLineException)e.getTargetException()).getColumnName());
            assertEquals(0, ((FileLineException)e.getTargetException()).getColumnIndex());
        } finally {
            field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());
        }
    }

    /**
     * testBuildStringConverters06() <br>
     * <br>
     * (異常系) <br>
     * 観点：Ｇ <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildStringConverters06.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > stringConverter：デフォルトコンストラクタがprivateで宣言されているStringConverterのクラスインスタンス<br>
     *                         > その他項目：デフォルト値<br>
     *                         (状態) this.stringConverterCacheMap:要素を持たないMapインスタンス<br>
     *                         <br>
     *                         期待値：(状態変化) -:以下の情報を持つFileLineException()が発生する<br>
     *                         ・メッセージ："Failed in an instantiate of a stringConverter."<br>
     *                         ・原因例外：IllegalAccessException<br>
     *                         ・ファイル名：fileNameと同じインスタンス<br>
     *                         ・行数：-1<br>
     *                         ・カラム名：column1<br>
     *                         ・カラム番号：0<br>
     *                         <br>
     *                         異常ケース<br>
     *                         指定したStringConverterのデフォルトコンストラクタがprivateで宣言されている場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBuildStringConverters06() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub24> clazz = AbstractFileLineWriter_Stub24.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub24> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub24>(fileName, clazz, columnFormatterMap);
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 前処理(フィールド)
        Map<Class, StringConverter> preStringConverterCacheMap = new HashMap<Class, StringConverter>();
        Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        field.set(AbstractFileLineWriter.class, preStringConverterCacheMap);

        try {
            // テスト実施
            method = AbstractFileLineWriter.class.getDeclaredMethod("buildStringConverters");
            method.setAccessible(true);
            method.invoke(fileLineWriter);
            fail("FileLineExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定(例外)
            assertTrue(FileLineException.class.isAssignableFrom(e.getTargetException().getClass()));
            assertEquals("Failed in an instantiate of a stringConverter.", e.getTargetException()
                    .getMessage());
            assertTrue(IllegalAccessException.class.isAssignableFrom(e.getTargetException()
                    .getCause().getClass()));
            assertEquals(fileName, ((FileLineException)e.getTargetException()).getFileName());
            assertEquals(-1, ((FileLineException)e.getTargetException()).getLineNo());
            assertEquals("column1", ((FileLineException)e.getTargetException()).getColumnName());
            assertEquals(0, ((FileLineException)e.getTargetException()).getColumnIndex());
        } finally {
            field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());
        }
    }

    /**
     * testBuildStringConverters07() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildStringConverters07.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         (状態) this.stringConverterCacheMap:要素を持たないMapインスタンス<br>
     *                         <br>
     *                         期待値：(状態変化) this.stringConverterCacheMap:以下の要素を持つMapインスタンス<br>
     *                         ・key：NullStringConverter.class,<br>
     *                         value：NullStringConverterインスタンス<br>
     *                         (状態変化) this.stringConverters:以下の要素を持つStringConverter配列<br>
     *                         1．NullStringConverterインスタンス(キャッシュされたものと同じインスタンス)<br>
     *                         2．NullStringConverterインスタンス(キャッシュされたものと同じインスタンス)<br>
     *                         3．NullStringConverterインスタンス(キャッシュされたものと同じインスタンス)<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (stringConverter設定ないフィールド：3個)<br>
     *                         stringConverter設定がない場合、NullStringConverterで初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @SuppressWarnings("unchecked")
    @Test
    public void testBuildStringConverters07() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub13> clazz = AbstractFileLineWriter_Stub13.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub13> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub13>(fileName, clazz, columnFormatterMap);
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);

        // 前処理(フィールド)
        Map<Class, StringConverter> preStringConverterCacheMap = new HashMap<Class, StringConverter>();
        Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        field.set(AbstractFileLineWriter.class, preStringConverterCacheMap);

        try {
            // テスト実施
            method = AbstractFileLineWriter.class.getDeclaredMethod("buildStringConverters");
            method.setAccessible(true);
            method.invoke(fileLineWriter);

            // 判定(状態変化、フィールド)
            Map<Class, StringConverter> stringConverterCacheMap = (Map<Class, StringConverter>) ReflectionTestUtils.getField(fileLineWriter, "stringConverterCacheMap");
            assertNotNull(stringConverterCacheMap);
            assertEquals(1, stringConverterCacheMap.size());
            assertTrue(stringConverterCacheMap.containsKey(
                    NullStringConverter.class));
            StringConverter nullStringConverter = stringConverterCacheMap.get(
                    NullStringConverter.class);
            assertNotNull(nullStringConverter);
            assertTrue(NullStringConverter.class.isAssignableFrom(
                    nullStringConverter.getClass()));

            StringConverter[] stringConverters = (StringConverter[]) ReflectionTestUtils.getField(fileLineWriter, "stringConverters");
            assertNotNull(stringConverters);
            assertEquals(3, stringConverters.length);
            StringConverter stringConverters1 = stringConverters[0];
            assertNotNull(stringConverters1);
            assertSame(nullStringConverter, stringConverters1);
            StringConverter stringConverters2 = stringConverters[1];
            assertNotNull(stringConverters2);
            assertSame(nullStringConverter, stringConverters2);
            StringConverter stringConverters3 = stringConverters[2];
            assertNotNull(stringConverters3);
            assertSame(nullStringConverter, stringConverters3);
        } finally {
            field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());
        }
    }

    /**
     * testBuildMethods01() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildMethodsConverters01.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・フィールドを持ってない<br>
     * <br>
     * 期待値：(状態変化) this.methods:要素を持たないMethod配列インスタンス<br>
     * <br>
     * 正常ケース<br>
     * ファイル行オブジェクトクラスがフィールドを持たない場合、methods配列が空で初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBuildMethods01() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub08> clazz = AbstractFileLineWriter_Stub08.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub08> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub08>(fileName, clazz, columnFormatterMap);
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);
        method = AbstractFileLineWriter.class.getDeclaredMethod("buildStringConverters");
        method.setAccessible(true);
        result = method.invoke(fileLineWriter);

        try {
            // テスト実施
            method = AbstractFileLineWriter.class.getDeclaredMethod("buildMethods");
            method.setAccessible(true);
            result = method.invoke(fileLineWriter);
            // 判定(状態変化、フィールド)
            Method[] methods = (Method[]) ReflectionTestUtils.getField(fileLineWriter,
                    "methods");
            assertNotNull(methods);
            assertEquals(0, methods.length);
        } finally {
            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());
        }
    }

    /**
     * testBuildMethods02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildMethodsConverters02.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         <br>
     *                         期待値：(状態変化) this.methods:以下の要素を持つMethod配列インスタンス<br>
     *                         １．Methodオブジェクト<br>
     *                         - メソッド名：getColumn1<br>
     *                         - 引数：なし<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (@OutputFileColumn設定ありフィールド：1個、<br>
     *                         フィールドに対するgetterメソッドあり)<br>
     *                         ファイル行オブジェクトクラスが@OutputFileColumn設定ありフィールドを持つ場合、methods配列が正しく初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBuildMethods02() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);
        method = AbstractFileLineWriter.class.getDeclaredMethod("buildStringConverters");
        method.setAccessible(true);
        result = method.invoke(fileLineWriter);

        try {
            // テスト実施
            method = AbstractFileLineWriter.class.getDeclaredMethod("buildMethods");
            method.setAccessible(true);
            method.invoke(fileLineWriter);

            // 判定(状態変化、フィールド)
            Method[] methods = (Method[]) ReflectionTestUtils.getField(fileLineWriter,
                    "methods");
            assertNotNull(methods);
            assertEquals(1, methods.length);
            Method methods1 = methods[0];
            assertEquals("getColumn1", methods1.getName());
            assertEquals(0, methods1.getParameterTypes().length);
        } finally {
            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());
        }
    }

    /**
     * testBuildMethods03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildMethodsConverters03.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持たない。<br>
     *                         <br>
     *                         期待値：(状態変化) -:以下の設定を持つFileExceptionインスタンス<br>
     *                         ・メッセージ："The getter method of column doesn't exist."<br>
     *                         ・原因例外：NoSuchMethodException<br>
     *                         ・ファイル名：fileNameと同じインスタンス<br>
     *                         <br>
     *                         異常ケース<br>
     *                         (@OutputFileColumn設定ありフィールド：1個、<br>
     *                         フィールドに対するgetterメソッドなし)<br>
     *                         フィールドに対するgetterメソッドがない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBuildMethods03() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub25> clazz = AbstractFileLineWriter_Stub25.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub25> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub25>(fileName, clazz, columnFormatterMap);
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);
        method = AbstractFileLineWriter.class.getDeclaredMethod("buildStringConverters");
        method.setAccessible(true);
        result = method.invoke(fileLineWriter);

        try {
            // テスト実施
            method = AbstractFileLineWriter.class.getDeclaredMethod("buildMethods");
            method.setAccessible(true);
            method.invoke(fileLineWriter);
            fail("FileExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getTargetException().getClass()));
            assertEquals("The getter method of column doesn't exist.", e.getTargetException()
                    .getMessage());
            assertTrue(NoSuchMethodException.class.isAssignableFrom(e.getTargetException().getCause()
                    .getClass()));
            assertEquals(fileName, ((FileException)e.getTargetException()).getFileName());

        } finally {
            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());
        }
    }

    /**
     * testBuildMethods04() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildMethodsConverters04.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         <br>
     *                         期待値：(状態変化) this.methods:以下の要素を持つMethod配列インスタンス<br>
     *                         1．Methodオブジェクト<br>
     *                         - メソッド名：getColumn1<br>
     *                         - 引数：なし<br>
     *                         2．Methodオブジェクト<br>
     *                         - メソッド名：getColumn2<br>
     *                         - 引数：なし<br>
     *                         3．Methodオブジェクト<br>
     *                         - メソッド名：getColumn3<br>
     *                         - 引数：なし<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (@OutputFileColumn設定ありフィールド：3個、<br>
     *                         フィールドに対するgetterメソッドあり)<br>
     *                         ファイル行オブジェクトクラスが@OutputFileColumn設定ありフィールドを持つ場合、methods配列が正しく初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBuildMethods04() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub13> clazz = AbstractFileLineWriter_Stub13.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub13> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub13>(fileName, clazz, columnFormatterMap);
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);
        method = AbstractFileLineWriter.class.getDeclaredMethod("buildStringConverters");
        method.setAccessible(true);
        result = method.invoke(fileLineWriter);

        try {
            // テスト実施
            method = AbstractFileLineWriter.class.getDeclaredMethod("buildMethods");
            method.setAccessible(true);
            method.invoke(fileLineWriter);

            // 判定(状態変化、フィールド)
            Method[] methods = (Method[]) ReflectionTestUtils.getField(fileLineWriter,
                    "methods");
            assertNotNull(methods);
            assertEquals(3, methods.length);
            Method methods1 = methods[0];
            assertEquals("getColumn1", methods1.getName());
            assertEquals(0, methods1.getParameterTypes().length);
            Method methods2 = methods[1];
            assertEquals("getColumn2", methods2.getName());
            assertEquals(0, methods2.getParameterTypes().length);
            Method methods3 = methods[2];
            assertEquals("getColumn3", methods3.getName());
            assertEquals(0, methods3.getParameterTypes().length);
        } finally {
            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());
        }
    }

    /**
     * testBuildMethods05() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testBuildMethodsConverters05.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn1<br>
     * - フィールド：String noMappingColumn2<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         <br>
     *                         以下親クラスの定義<br>
     *                         ・@OutputFileColumn設定ありのフィールドを持つ<br>
     *                         - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         <br>
     *                         ※親クラスにフィールドの情報がある。<br>
     *                         <br>
     *                         期待値：(状態変化) this.methods:以下の要素を持つMethod配列インスタンス<br>
     *                         1．Methodオブジェクト<br>
     *                         - メソッド名：getColumn1<br>
     *                         - 引数：なし<br>
     *                         2．Methodオブジェクト<br>
     *                         - メソッド名：getColumn2<br>
     *                         - 引数：なし<br>
     *                         3．Methodオブジェクト<br>
     *                         - メソッド名：getColumn3<br>
     *                         - 引数：なし<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (@OutputFileColumn設定ありフィールド：3個、<br>
     *                         フィールドに対するgetterメソッドあり、<br>
     *                         親クラスにもフィールド定義が存在する)<br>
     *                         ファイル行オブジェクトクラスが@OutputFileColumn設定ありフィールドを持つ場合、methods配列が正しく初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBuildMethods05() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub19> clazz = AbstractFileLineWriter_Stub19.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub19> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub19>(fileName, clazz, columnFormatterMap);
        Method method = AbstractFileLineWriter.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineWriter);
        method = AbstractFileLineWriter.class.getDeclaredMethod("buildStringConverters");
        method.setAccessible(true);
        result = method.invoke(fileLineWriter);

        try {
            // テスト実施
            method = AbstractFileLineWriter.class.getDeclaredMethod("buildMethods");
            method.setAccessible(true);
            method.invoke(fileLineWriter);

            // 判定(状態変化、フィールド)
            Method[] methods = (Method[]) ReflectionTestUtils.getField(fileLineWriter,
                    "methods");
            assertNotNull(methods);
            assertEquals(3, methods.length);
            Method methods1 = methods[0];
            assertEquals("getColumn1", methods1.getName());
            assertEquals(0, methods1.getParameterTypes().length);
            Method methods2 = methods[1];
            assertEquals("getColumn2", methods2.getName());
            assertEquals(0, methods2.getParameterTypes().length);
            Method methods3 = methods[2];
            assertEquals("getColumn3", methods3.getName());
            assertEquals(0, methods3.getParameterTypes().length);
        } finally {
            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());
        }
    }

    /**
     * testPrintHeaderLine01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) headerLine:要素を持たないListインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintHeaderLine01.txt"<br>
     * (状態) writeTrailer:false<br>
     * (状態) writeData:false<br>
     * <br>
     * 期待値：(状態変化) #printList():1回呼ばれる。<br>
     * 引数の確認を行う。<br>
     * <br>
     * 正常ケース<br>
     * データ部とトレイラ部の書き込み処理を行う前に実行された場合、対象Listの出力処理を行うことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintHeaderLine01() throws Exception {
        // 前処理(試験対象)
        String fileName = TEMP_FILE_NAME;
        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);
        try {
            fileLineWriter.init();

            // 前処理(引数)
            List<String> headerLine = new ArrayList<String>();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "writeData",
                    Boolean.FALSE);

            // テスト実施
            fileLineWriter.printHeaderLine(headerLine);

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "printList"));
            List printListArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "printList", 0);
            assertSame(headerLine, printListArguments.get(0));
        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");
            if (writer != null) {
                writer.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintHeaderLine02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) headerLine:要素を持たないListインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintHeaderLine02.txt"<br>
     * (状態) writeTrailer:true<br>
     * (状態) writeData:false<br>
     * <br>
     * 期待値：(状態変化) #printList():呼ばれない<br>
     * (状態変化) -:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："Header part should be called before data part or trailer part."<br>
     * ・原因例外：IllegalStateException<br>
     * ・ファイル名：fileNameと同じインスタンス<br>
     * <br>
     * 異常ケース<br>
     * ヘッダ行を出力する前にトレイラ行が出力されていた場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintHeaderLine02() throws Exception {
        // 前処理(試験対象)
        String fileName = TEMP_FILE_NAME;

        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);
        try {
            fileLineWriter.init();

            // 前処理(引数)
            List<String> headerLine = new ArrayList<String>();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.TRUE);

            ReflectionTestUtils.setField(fileLineWriter, "writeData",
                    Boolean.FALSE);

            // テスト実施
            fileLineWriter.printHeaderLine(headerLine);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("Header part should be called before data part or "
                    + "trailer part.", e.getMessage());
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getCause()
                    .getClass()));
            assertEquals(fileName, e.getFileName());

            // 判定(状態変化、メソッド)
            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "printList"));
        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");
            if (writer != null) {
                writer.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintHeaderLine03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) headerLine:要素を持たないListインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintHeaderLine03.txt"<br>
     * (状態) writeTrailer:false<br>
     * (状態) writeData:true<br>
     * <br>
     * 期待値：(状態変化) #printList():呼ばれない<br>
     * (状態変化) -:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："Header part should be called before data part or trailer part."<br>
     * ・原因例外：IllegalStateException<br>
     * ・ファイル名：fileNameと同じインスタンス<br>
     * <br>
     * 異常ケース<br>
     * ヘッダ行を出力する前にデータ行が出力されていた場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintHeaderLine03() throws Exception {
        // 前処理(試験対象)
        String fileName = TEMP_FILE_NAME;

        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);
        try {
            fileLineWriter.init();

            // 前処理(引数)
            List<String> headerLine = new ArrayList<String>();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "writeData",
                    Boolean.TRUE);

            // テスト実施
            fileLineWriter.printHeaderLine(headerLine);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("Header part should be called before data part or "
                    + "trailer part.", e.getMessage());
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getCause()
                    .getClass()));
            assertEquals(fileName, e.getFileName());

            // 判定(状態変化、メソッド)
            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "printList"));
        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");
            if (writer != null) {
                writer.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine01.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.writeTrailer:true<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     *                         (状態) #getColumn():異常終了<br>
     *                         ・以下の例外を返す。<br>
     *                         - ArrayIndexOutOfBoundException<br>
     *                         (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     *                         (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     *                         (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         期待値：(状態変化) this.currentLineCount:0<br>
     *                         (状態変化) #getColumn():呼ばれない<br>
     *                         (状態変化) #getWriter().write():呼ばれない<br>
     *                         (状態変化) #setWriteData():呼ばれない<br>
     *                         (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         ※変化なし<br>
     *                         (状態変化) 例外:以下の情報を持つFileExceptionが発生する<br>
     *                         ・メッセージ："Header part or data part should be called before TrailerPart"<br>
     *                         ・原因例外：IllegalStateException<br>
     *                         ・ファイル名：fileNameと同じインスタンス<br>
     *                         <br>
     *                         異常ケース<br>
     *                         トレイラ部の出力が既に完了されている場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine01() throws Exception {

        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub10 t = new AbstractFileLineWriter_Stub10();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.TRUE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setExceptionAtAllTimes(AbstractFileLineWriter.class,
                    "getColumn", new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);
            fail("FileExceptionが発生しませんでした。");

        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("Header part or data part should be called before "
                    + "TrailerPart", e.getMessage());
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getCause()
                    .getClass()));
            assertEquals(fileName, e.getFileName());

            // 判定(状態変化、フィールド)
            assertEquals(0, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "getColumn"));

            assertFalse(VMOUTUtil.isCalled(Writer.class, "writer"));

            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "setWriteData"));

            // 判定(ファイル)
            writer.flush();

            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine02.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・フィールドを持ってない<br>
     * (状態) this.writeTrailer:false<br>
     * (状態) this.currentLineCount:0<br>
     * (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     * (状態) #getColumn():異常終了<br>
     * ・以下の例外を返す。<br>
     * - ArrayIndexOutOfBoundException<br>
     * (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     * (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     * (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     * ・内容はない(Obyte)<br>
     * <br>
     * 期待値：(状態変化) this.currentLineCount:1<br>
     * (状態変化) #getColumn():呼ばれない<br>
     * (状態変化) #getWriter().write():1回呼ばれる<br>
     * 引数を確認する。<br>
     * (状態変化) #setWriteData():1回呼ばれる<br>
     * 引数を確認する。<br>
     * (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     * ・内容：行区切り文字１つのみ<br>
     * <br>
     * 正常ケース<br>
     * (出力対象フィールド：なし、区切り文字：デフォルト値、<br>
     * 囲み文字：デフォルト値)<br>
     * 出力対象のフィールドがない場合、行区切り文字のみファイルに出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine02() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub08> clazz = AbstractFileLineWriter_Stub08.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub08> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub08>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub08 t = new AbstractFileLineWriter_Stub08();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setExceptionAtAllTimes(AbstractFileLineWriter.class,
                    "getColumn", new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "getColumn"));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine03.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.writeTrailer:false<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     *                         (状態) #getColumn():正常終了<br>
     *                         ・以下の結果を返す。<br>
     *                         - 1回目："testPrintDataLine03_column1"<br>
     *                         - 2回目以後：ArrayIndexOutOfBoundException<br>
     *                         (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     *                         (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     *                         (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         期待値：(状態変化) this.currentLineCount:1<br>
     *                         (状態変化) #getColumn():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #getWriter().write():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #setWriteData():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容："testPrintDataLine03_column1<行区切り文字>"<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (出力対象フィールド：1個、区切り文字：デフォルト値、<br>
     *                         囲み文字：デフォルト値)<br>
     *                         出力対象のフィールドが１つある場合、出力対象フィールドの情報が出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine03() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub10 t = new AbstractFileLineWriter_Stub10();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 0, "testPrintDataLine03_column1");
            VMOUTUtil.setExceptionAt(AbstractFileLineWriter.class, "getColumn",
                    1, new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "getColumn"));
            List getColumnArtument = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 0);
            assertEquals(2, getColumnArtument.size());
            assertSame(t, getColumnArtument.get(0));
            assertEquals(0, getColumnArtument.get(1));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = "testPrintDataLine03_column1"
                    + systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine04() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine04.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.writeTrailer:false<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     *                         (状態) #getColumn():正常終了<br>
     *                         ・以下の結果を返す。<br>
     *                         - 1回目："testPrintDataLine04_column1"<br>
     *                         - 2回目："testPrintDataLine04_column2"<br>
     *                         - 3回目："testPrintDataLine04_column3"<br>
     *                         - 4回目以後：ArrayIndexOutOfBoundException<br>
     *                         (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     *                         (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     *                         (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         期待値：(状態変化) this.currentLineCount:1<br>
     *                         (状態変化) #getColumn():3回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #getWriter().write():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #setWriteData():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容：
     *                         "testPrintDataLine04_column1<区切り文字>testPrintDataLine04_column2<区切り文字>testPrintDataLine04_column3<行区切り文字>"
     *                         <br>
     *                         <br>
     *                         正常ケース<br>
     *                         (出力対象フィールド：3個、区切り文字：デフォルト値、<br>
     *                         囲み文字：デフォルト値)<br>
     *                         出力対象のフィールドが3つある場合、全出力対象フィールドの情報とその間に区切り文字が出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine04() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub13> clazz = AbstractFileLineWriter_Stub13.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub13> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub13>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub13 t = new AbstractFileLineWriter_Stub13();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 0, "testPrintDataLine04_column1");
            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 1, "testPrintDataLine04_column2");
            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 2, "testPrintDataLine04_column3");
            VMOUTUtil.setExceptionAt(AbstractFileLineWriter.class, "getColumn",
                    3, new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertEquals(3, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "getColumn"));
            List getColumnArtuments1 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 0);
            assertEquals(2, getColumnArtuments1.size());
            assertSame(t, getColumnArtuments1.get(0));
            assertEquals(0, getColumnArtuments1.get(1));
            List getColumnArtuments2 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 1);
            assertEquals(2, getColumnArtuments2.size());
            assertSame(t, getColumnArtuments2.get(0));
            assertEquals(1, getColumnArtuments2.get(1));
            List getColumnArtuments3 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 2);
            assertEquals(2, getColumnArtuments3.size());
            assertSame(t, getColumnArtuments3.get(0));
            assertEquals(2, getColumnArtuments3.get(1));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = "testPrintDataLine04_column1,"
                    + "testPrintDataLine04_column2,testPrintDataLine04_column3"
                    + systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine05.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.writeTrailer:false<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     *                         既にクローズされている。<br>
     *                         (状態) #getColumn():正常終了<br>
     *                         ・以下の結果を返す。<br>
     *                         - 1回目："testPrintDataLine05_column1"<br>
     *                         - 2回目以後：ArrayIndexOutOfBoundException<br>
     *                         (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     *                         (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     *                         (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         期待値：(状態変化) this.currentLineCount:0<br>
     *                         (状態変化) #getColumn():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #getWriter().write():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #setWriteData():呼ばれない<br>
     *                         (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         ※変化なし<br>
     *                         (状態変化) 例外:以下の情報を持つFileExceptionが発生する<br>
     *                         ・メッセージ："Processing of writer was failed."<br>
     *                         ・原因例外：IOException<br>
     *                         ・ファイル名：fileNameと同じインスタンス<br>
     *                         <br>
     *                         異常ケース<br>
     *                         ファイル書き込み用のwriterが既にクローズされた場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine05() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;
        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;

        // 前処理(引数)
        AbstractFileLineWriter_Stub10 t = new AbstractFileLineWriter_Stub10();

        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));
            writer.close();

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 0, "testPrintDataLine05_column1");
            VMOUTUtil.setExceptionAt(AbstractFileLineWriter.class, "getColumn",
                    1, new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("Processing of writer was failed.", e.getMessage());
            assertTrue(IOException.class.isAssignableFrom(e.getCause()
                    .getClass()));
            assertEquals(fileName, e.getFileName());

            // 判定(状態変化、フィールド)
            assertEquals(0, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "getColumn"));
            List getColumnArtument = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 0);
            assertEquals(2, getColumnArtument.size());
            assertSame(t, getColumnArtument.get(0));
            assertEquals(0, getColumnArtument.get(1));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = "testPrintDataLine05_column1"
                    + systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "setWriteData"));

            // 判定(ファイル)
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine06() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine06.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar：'\"'<br>
     * - その他：デフォルト値<br>
     * ・フィールドを持ってない<br>
     * (状態) this.writeTrailer:false<br>
     * (状態) this.currentLineCount:0<br>
     * (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     * (状態) #getColumn():異常終了<br>
     * ・以下の例外を返す。<br>
     * - ArrayIndexOutOfBoundException<br>
     * (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     * (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     * (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     * ・内容はない(Obyte)<br>
     * <br>
     * 期待値：(状態変化) this.currentLineCount:1<br>
     * (状態変化) #getColumn():呼ばれない<br>
     * (状態変化) #getWriter().write():1回呼ばれる<br>
     * 引数を確認する。<br>
     * (状態変化) #setWriteData():1回呼ばれる<br>
     * 引数を確認する。<br>
     * (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     * ・内容：行区切り文字１つのみ<br>
     * <br>
     * 正常ケース<br>
     * (出力対象フィールド：なし、区切り文字：デフォルト値、<br>
     * 囲み文字：デフォルト値以外)<br>
     * 出力対象のフィールドがない場合、行区切り文字のみファイルに出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine06() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub26> clazz = AbstractFileLineWriter_Stub26.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub26> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub26>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub26 t = new AbstractFileLineWriter_Stub26();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setExceptionAtAllTimes(AbstractFileLineWriter.class,
                    "getColumn", new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "getColumn"));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine07() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine07.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar：'\"'<br>
     * - その他：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.writeTrailer:false<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     *                         (状態) #getColumn():正常終了<br>
     *                         ・以下の結果を返す。<br>
     *                         - 1回目："testPrintDataLine07_column1"<br>
     *                         - 2回目以後：ArrayIndexOutOfBoundException<br>
     *                         (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     *                         (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     *                         (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         期待値：(状態変化) this.currentLineCount:1<br>
     *                         (状態変化) #getColumn():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #getWriter().write():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #setWriteData():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容："\"testPrintDataLine07_column1\"<行区切り文字>"<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (出力対象フィールド：1個、区切り文字：デフォルト値、<br>
     *                         囲み文字：デフォルト値以外)<br>
     *                         出力対象のフィールドが１つある場合、囲み文字に囲まれた出力対象フィールドの情報が出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine07() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub27> clazz = AbstractFileLineWriter_Stub27.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub27> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub27>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub27 t = new AbstractFileLineWriter_Stub27();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 0, "testPrintDataLine07_column1");
            VMOUTUtil.setExceptionAt(AbstractFileLineWriter.class, "getColumn",
                    1, new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "getColumn"));
            List getColumnArtument = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 0);
            assertEquals(2, getColumnArtument.size());
            assertSame(t, getColumnArtument.get(0));
            assertEquals(0, getColumnArtument.get(1));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = "\"testPrintDataLine07_column1\""
                    + systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine08() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine08.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar：'\"'<br>
     * - その他：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.writeTrailer:false<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     *                         (状態) #getColumn():正常終了<br>
     *                         ・以下の結果を返す。<br>
     *                         - 1回目："testPrintDataLine08_column1"<br>
     *                         - 2回目："testPrintDataLine08_column2"<br>
     *                         - 3回目："testPrintDataLine08_column3"<br>
     *                         - 4回目以後：ArrayIndexOutOfBoundException<br>
     *                         (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     *                         (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     *                         (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         期待値：(状態変化) this.currentLineCount:1<br>
     *                         (状態変化) #getColumn():3回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #getWriter().write():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #setWriteData():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容：
     *                         "\"testPrintDataLine08_column1\"<区切り文字>\"testPrintDataLine08_column2\"<区切り文字>\"testPrintDataLine08_column3\"<行区切り文字>"
     *                         <br>
     *                         <br>
     *                         正常ケース<br>
     *                         (出力対象フィールド：3個、区切り文字：デフォルト値、<br>
     *                         囲み文字：デフォルト値以外)<br>
     *                         出力対象のフィールドが3つある場合、囲み文字に囲まれた全出力対象フィールドの情報とその間に区切り文字が出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine08() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub28> clazz = AbstractFileLineWriter_Stub28.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub28> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub28>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub28 t = new AbstractFileLineWriter_Stub28();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 0, "testPrintDataLine08_column1");
            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 1, "testPrintDataLine08_column2");
            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 2, "testPrintDataLine08_column3");
            VMOUTUtil.setExceptionAt(AbstractFileLineWriter.class, "getColumn",
                    3, new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertEquals(3, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "getColumn"));
            List getColumnArtuments1 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 0);
            assertEquals(2, getColumnArtuments1.size());
            assertSame(t, getColumnArtuments1.get(0));
            assertEquals(0, getColumnArtuments1.get(1));
            List getColumnArtuments2 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 1);
            assertEquals(2, getColumnArtuments2.size());
            assertSame(t, getColumnArtuments2.get(0));
            assertEquals(1, getColumnArtuments2.get(1));
            List getColumnArtuments3 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 2);
            assertEquals(2, getColumnArtuments3.size());
            assertSame(t, getColumnArtuments3.get(0));
            assertEquals(2, getColumnArtuments3.get(1));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = "\"testPrintDataLine08_column1\","
                    + "\"testPrintDataLine08_column2\",\"testPrintDataLine08_column3\""
                    + systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine09() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine09.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter：'|'<br>
     * - その他：デフォルト値<br>
     * ・フィールドを持ってない<br>
     * (状態) this.writeTrailer:false<br>
     * (状態) this.currentLineCount:0<br>
     * (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     * (状態) #getColumn():異常終了<br>
     * ・以下の例外を返す。<br>
     * - ArrayIndexOutOfBoundException<br>
     * (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     * (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     * (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     * ・内容はない(Obyte)<br>
     * <br>
     * 期待値：(状態変化) this.currentLineCount:1<br>
     * (状態変化) #getColumn():呼ばれない<br>
     * (状態変化) #getWriter().write():1回呼ばれる<br>
     * 引数を確認する。<br>
     * (状態変化) #setWriteData():1回呼ばれる<br>
     * 引数を確認する。<br>
     * (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     * ・内容：行区切り文字１つのみ<br>
     * <br>
     * 正常ケース<br>
     * (出力対象フィールド：なし、囲み文字：デフォルト値、<br>
     * 区切り文字：Character.MIN_VALUE)<br>
     * 出力対象のフィールドがない場合、行区切り文字のみファイルに出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine09() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub29> clazz = AbstractFileLineWriter_Stub29.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub29> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub29>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub29 t = new AbstractFileLineWriter_Stub29();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setExceptionAtAllTimes(AbstractFileLineWriter.class,
                    "getColumn", new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "getColumn"));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine10() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine10.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter：'|'<br>
     * - その他：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.writeTrailer:false<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     *                         (状態) #getColumn():正常終了<br>
     *                         ・以下の結果を返す。<br>
     *                         - 1回目："testPrintDataLine10_column1"<br>
     *                         - 2回目以後：ArrayIndexOutOfBoundException<br>
     *                         (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     *                         (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     *                         (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         期待値：(状態変化) this.currentLineCount:1<br>
     *                         (状態変化) #getColumn():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #getWriter().write():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #setWriteData():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容："testPrintDataLine10_column1<行区切り文字>"<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (出力対象フィールド：1個、囲み文字：デフォルト値、<br>
     *                         区切り文字：Character.MIN_VALUE)<br>
     *                         出力対象のフィールドが１つある場合、出力対象フィールドの情報が出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine10() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;
        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub30> clazz = AbstractFileLineWriter_Stub30.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub30> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub30>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub30 t = new AbstractFileLineWriter_Stub30();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 0, "testPrintDataLine10_column1");
            VMOUTUtil.setExceptionAt(AbstractFileLineWriter.class, "getColumn",
                    1, new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "getColumn"));
            List getColumnArtument = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 0);
            assertEquals(2, getColumnArtument.size());
            assertSame(t, getColumnArtument.get(0));
            assertEquals(0, getColumnArtument.get(1));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = "testPrintDataLine10_column1"
                    + systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine11() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine11.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter：'|'<br>
     * - その他：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.writeTrailer:false<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     *                         (状態) #getColumn():正常終了<br>
     *                         ・以下の結果を返す。<br>
     *                         - 1回目："testPrintDataLine11_column1"<br>
     *                         - 2回目："testPrintDataLine11_column2"<br>
     *                         - 3回目："testPrintDataLine11_column3"<br>
     *                         - 4回目以後：ArrayIndexOutOfBoundException<br>
     *                         (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     *                         (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     *                         (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         期待値：(状態変化) this.currentLineCount:1<br>
     *                         (状態変化) #getColumn():3回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #getWriter().write():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #setWriteData():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容："testPrintDataLine11_column1|testPrintDataLine11_column2|testPrintDataLine11_column3<行区切り文字>"
     *                         <br>
     *                         <br>
     *                         正常ケース<br>
     *                         (出力対象フィールド：3個、囲み文字：デフォルト値、<br>
     *                         区切り文字：Character.MIN_VALUE)<br>
     *                         出力対象のフィールドが3つある場合、全出力対象フィールドの情報とその間に区切り文字が出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine11() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;
        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub31> clazz = AbstractFileLineWriter_Stub31.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub31> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub31>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub31 t = new AbstractFileLineWriter_Stub31();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 0, "testPrintDataLine11_column1");
            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 1, "testPrintDataLine11_column2");
            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 2, "testPrintDataLine11_column3");
            VMOUTUtil.setExceptionAt(AbstractFileLineWriter.class, "getColumn",
                    3, new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertEquals(3, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "getColumn"));
            List getColumnArtuments1 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 0);
            assertEquals(2, getColumnArtuments1.size());
            assertSame(t, getColumnArtuments1.get(0));
            assertEquals(0, getColumnArtuments1.get(1));
            List getColumnArtuments2 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 1);
            assertEquals(2, getColumnArtuments2.size());
            assertSame(t, getColumnArtuments2.get(0));
            assertEquals(1, getColumnArtuments2.get(1));
            List getColumnArtuments3 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 2);
            assertEquals(2, getColumnArtuments3.size());
            assertSame(t, getColumnArtuments3.get(0));
            assertEquals(2, getColumnArtuments3.get(1));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = "testPrintDataLine11_column1|"
                    + "testPrintDataLine11_column2|testPrintDataLine11_column3"
                    + systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine12() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine12.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.writeTrailer:false<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #getWriter():null<br>
     *                         (状態) #getColumn():正常終了<br>
     *                         ・以下の結果を返す。<br>
     *                         - 1回目："testPrintDataLine12_column1"<br>
     *                         - 2回目以後：ArrayIndexOutOfBoundException<br>
     *                         (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     *                         (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     *                         (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         期待値：(状態変化) this.currentLineCount:0<br>
     *                         (状態変化) #getColumn():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #getWriter().write():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #setWriteData():呼ばれない<br>
     *                         (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         ※変化なし<br>
     *                         (状態変化) 例外:NullPointerExceptionが発生する<br>
     *                         <br>
     *                         異常ケース<br>
     *                         getWriter()の結果がnullの場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine12() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;

        // 前処理(引数)
        AbstractFileLineWriter_Stub10 t = new AbstractFileLineWriter_Stub10();

        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = null;

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 0, "testPrintDataLine12_column1");
            VMOUTUtil.setExceptionAt(AbstractFileLineWriter.class, "getColumn",
                    1, new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);
            fail("NullPointerExceptionが発生しませんでした。");
        } catch (NullPointerException e) {
            // 判定(例外)
            assertTrue(NullPointerException.class.isAssignableFrom(e
                    .getClass()));

            // 判定(状態変化、フィールド)
            assertEquals(0, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "getColumn"));
            List getColumnArtument = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 0);
            assertEquals(2, getColumnArtument.size());
            assertSame(t, getColumnArtument.get(0));
            assertEquals(0, getColumnArtument.get(1));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = "testPrintDataLine12_column1"
                    + systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "setWriteData"));

            // 判定(ファイル)
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine13() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine13.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter：Character.MIN_VALUE<br>
     * - encloseChar：'\"'<br>
     * - その他：デフォルト値<br>
     * ・フィールドを持ってない<br>
     * (状態) this.writeTrailer:false<br>
     * (状態) this.currentLineCount:0<br>
     * (状態) #getWriter():not null<br>
     * (状態) #getColumn():異常終了<br>
     * ・以下の例外を返す。<br>
     * - ArrayIndexOutOfBoundException<br>
     * (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     * (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     * (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     * ・内容はない(Obyte)<br>
     * <br>
     * 期待値：(状態変化) this.currentLineCount:1<br>
     * (状態変化) #getColumn():呼ばれない<br>
     * (状態変化) #getWriter().write():1回呼ばれる<br>
     * 引数を確認する。<br>
     * (状態変化) #setWriteData():1回呼ばれる<br>
     * 引数を確認する。<br>
     * (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     * ・内容：行区切り文字１つのみ<br>
     * <br>
     * 正常ケース<br>
     * (出力対象フィールド：なし、囲み文字：デフォルト値以外、<br>
     * 区切り文字：Character.MIN_VALUE)<br>
     * 出力対象のフィールドがない場合、行区切り文字のみファイルに出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine13() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub32> clazz = AbstractFileLineWriter_Stub32.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub32> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub32>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub32 t = new AbstractFileLineWriter_Stub32();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setExceptionAtAllTimes(AbstractFileLineWriter.class,
                    "getColumn", new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "getColumn"));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine14() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine14.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter：Character.MIN_VALUE<br>
     * - encloseChar：'\"'<br>
     * - その他：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.writeTrailer:false<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     *                         (状態) #getColumn():正常終了<br>
     *                         ・以下の結果を返す。<br>
     *                         - 1回目："testPrintDataLine14_column1"<br>
     *                         - 2回目以後：ArrayIndexOutOfBoundException<br>
     *                         (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     *                         (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     *                         (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         期待値：(状態変化) this.currentLineCount:1<br>
     *                         (状態変化) #getColumn():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #getWriter().write():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #setWriteData():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容："\"testPrintDataLine14_column1\"<行区切り文字>"<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (出力対象フィールド：1個、区切り文字：デフォルト値、<br>
     *                         囲み文字：デフォルト値以外)<br>
     *                         出力対象のフィールドが１つある場合、囲み文字に囲まれた出力対象フィールドの情報のみ出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine14() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;
        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub33> clazz = AbstractFileLineWriter_Stub33.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub33> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub33>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub33 t = new AbstractFileLineWriter_Stub33();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 0, "testPrintDataLine14_column1");
            VMOUTUtil.setExceptionAt(AbstractFileLineWriter.class, "getColumn",
                    1, new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "getColumn"));
            List getColumnArtument = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 0);
            assertEquals(2, getColumnArtument.size());
            assertSame(t, getColumnArtument.get(0));
            assertEquals(0, getColumnArtument.get(1));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = "\"testPrintDataLine14_column1\""
                    + systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine15() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine15.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - delimiter：Character.MIN_VALUE<br>
     * - encloseChar：'\"'<br>
     * - その他：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.writeTrailer:false<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     *                         (状態) #getColumn():正常終了<br>
     *                         ・以下の結果を返す。<br>
     *                         - 1回目："testPrintDataLine15_column1"<br>
     *                         - 2回目："testPrintDataLine15_column2"<br>
     *                         - 3回目："testPrintDataLine15_column3"<br>
     *                         - 4回目以後：ArrayIndexOutOfBoundException<br>
     *                         (状態) #getDelimiter():this.clazzのフィールド定義に従う。<br>
     *                         (状態) #getEncloseChar():this.clazzのフィールド定義に従う。<br>
     *                         (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         期待値：(状態変化) this.currentLineCount:1<br>
     *                         (状態変化) #getColumn():3回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #getWriter().write():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #setWriteData():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容：
     *                         "\"testPrintDataLine15_column1\"\"testPrintDataLine15_column2\"\"testPrintDataLine15_column3\"<行区切り文字>"
     *                         <br>
     *                         <br>
     *                         正常ケース<br>
     *                         (出力対象フィールド：3個、区切り文字：デフォルト値、<br>
     *                         囲み文字：デフォルト値以外)<br>
     *                         出力対象のフィールドが3つある場合、囲み文字に囲まれた全出力対象フィールドの情報が出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine15() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub34> clazz = AbstractFileLineWriter_Stub34.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub34> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub34>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub34 t = new AbstractFileLineWriter_Stub34();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 0, "testPrintDataLine15_column1");
            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 1, "testPrintDataLine15_column2");
            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 2, "testPrintDataLine15_column3");
            VMOUTUtil.setExceptionAt(AbstractFileLineWriter.class, "getColumn",
                    3, new ArrayIndexOutOfBoundsException("わざと"));

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertEquals(3, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "getColumn"));
            List getColumnArtuments1 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 0);
            assertEquals(2, getColumnArtuments1.size());
            assertSame(t, getColumnArtuments1.get(0));
            assertEquals(0, getColumnArtuments1.get(1));
            List getColumnArtuments2 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 1);
            assertEquals(2, getColumnArtuments2.size());
            assertSame(t, getColumnArtuments2.get(0));
            assertEquals(1, getColumnArtuments2.get(1));
            List getColumnArtuments3 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 2);
            assertEquals(2, getColumnArtuments3.size());
            assertSame(t, getColumnArtuments3.get(0));
            assertEquals(2, getColumnArtuments3.get(1));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = "\"testPrintDataLine15_column1\""
                    + Character.MIN_VALUE + "\"testPrintDataLine15_column2\""
                    + Character.MIN_VALUE + "\"testPrintDataLine15_column3\""
                    + systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine16() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine16.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・フィールドを持ってない<br>
     * (状態) this.writeTrailer:false<br>
     * (状態) this.currentLineCount:0<br>
     * (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     * (状態) #getColumn():異常終了<br>
     * ・以下の例外を返す。<br>
     * - ArrayIndexOutOfBoundException<br>
     * (状態) #getDelimiter():Character.MIN_VALUEを返す<br>
     * (状態) #getEncloseChar():Character.MIN_VALUEを返す<br>
     * (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     * ・内容はない(Obyte)<br>
     * <br>
     * 期待値：(状態変化) this.currentLineCount:1<br>
     * (状態変化) #getColumn():呼ばれない<br>
     * (状態変化) #getWriter().write():1回呼ばれる<br>
     * 引数を確認する。<br>
     * (状態変化) #setWriteData():1回呼ばれる<br>
     * 引数を確認する。<br>
     * (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     * ・内容：行区切り文字１つのみ<br>
     * <br>
     * 正常ケース<br>
     * (出力対象フィールド：なし、<br>
     * 囲み文字：Character.MIN_VALUE、<br>
     * 区切り文字：Character.MIN_VALUE)<br>
     * 出力対象のフィールドがない場合、行区切り文字のみファイルに出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine16() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;
        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub08> clazz = AbstractFileLineWriter_Stub08.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub08> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub08>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub08 t = new AbstractFileLineWriter_Stub08();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setExceptionAtAllTimes(AbstractFileLineWriter.class,
                    "getColumn", new ArrayIndexOutOfBoundsException("わざと"));

            VMOUTUtil.setReturnValueAtAllTimes(
                    AbstractFileLineWriterImpl01.class, "getDelimiter",
                    Character.MIN_VALUE);

            VMOUTUtil.setReturnValueAtAllTimes(
                    AbstractFileLineWriterImpl01.class, "getEncloseChar",
                    Character.MIN_VALUE);

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "getColumn"));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine17() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine17.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.writeTrailer:false<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     *                         (状態) #getColumn():正常終了<br>
     *                         ・以下の結果を返す。<br>
     *                         - 1回目："testPrintDataLine10_column1"<br>
     *                         - 2回目以後：ArrayIndexOutOfBoundException<br>
     *                         (状態) #getDelimiter():Character.MIN_VALUEを返す<br>
     *                         (状態) #getEncloseChar():Character.MIN_VALUEを返す<br>
     *                         (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         期待値：(状態変化) this.currentLineCount:1<br>
     *                         (状態変化) #getColumn():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #getWriter().write():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #setWriteData():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容："testPrintDataLine17_column1<行区切り文字>"<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (出力対象フィールド：1個、<br>
     *                         囲み文字：Character.MIN_VALUE、<br>
     *                         区切り文字：Character.MIN_VALUE)<br>
     *                         出力対象のフィールドが１つある場合、出力対象フィールドの情報が出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine17() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub10 t = new AbstractFileLineWriter_Stub10();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 0, "testPrintDataLine17_column1");
            VMOUTUtil.setExceptionAt(AbstractFileLineWriter.class, "getColumn",
                    1, new ArrayIndexOutOfBoundsException("わざと"));

            VMOUTUtil.setReturnValueAtAllTimes(
                    AbstractFileLineWriterImpl01.class, "getDelimiter",
                    Character.MIN_VALUE);

            VMOUTUtil.setReturnValueAtAllTimes(
                    AbstractFileLineWriterImpl01.class, "getEncloseChar",
                    Character.MIN_VALUE);

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "getColumn"));
            List getColumnArtument = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 0);
            assertEquals(2, getColumnArtument.size());
            assertSame(t, getColumnArtument.get(0));
            assertEquals(0, getColumnArtument.get(1));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");
            String expectationResultData = "testPrintDataLine17_column1"
                    + systemLineSeparator;
            assertEquals(expectationResultData, writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintDataLine18() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) t:this.clazzのインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintDataLine18.txt"<br>
     * (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.writeTrailer:false<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #getWriter():this.fileNameファイルに対するWriterインスタンス<br>
     *                         (状態) #getColumn():正常終了<br>
     *                         ・以下の結果を返す。<br>
     *                         - 1回目："testPrintDataLine11_column1"<br>
     *                         - 2回目："testPrintDataLine11_column2"<br>
     *                         - 3回目："testPrintDataLine11_column3"<br>
     *                         - 4回目以後：ArrayIndexOutOfBoundException<br>
     *                         (状態) #getDelimiter():Character.MIN_VALUEを返す<br>
     *                         (状態) #getEncloseChar():Character.MIN_VALUEを返す<br>
     *                         (状態) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容はない(Obyte)<br>
     *                         <br>
     *                         期待値：(状態変化) this.currentLineCount:1<br>
     *                         (状態変化) #getColumn():3回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #getWriter().write():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #setWriteData():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) ファイル:クラスパスにthis.fileNameに対するファイルが存在する。<br>
     *                         ・内容："testPrintDataLine18_column1testPrintDataLine18_column2testPrintDataLine18_column3<行区切り文字>"
     *                         <br>
     *                         <br>
     *                         正常ケース<br>
     *                         (出力対象フィールド：3個、<br>
     *                         囲み文字：Character.MIN_VALUE、<br>
     *                         区切り文字：Character.MIN_VALUE)<br>
     *                         出力対象のフィールドが3つある場合、全出力対象フィールドの情報が出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintDataLine18() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub13> clazz = AbstractFileLineWriter_Stub13.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub13> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub13>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            AbstractFileLineWriter_Stub13 t = new AbstractFileLineWriter_Stub13();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "getWriter", writer);

            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 0, "testPrintDataLine18_column1");
            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 1, "testPrintDataLine18_column2");
            VMOUTUtil.setReturnValueAt(AbstractFileLineWriter.class,
                    "getColumn", 2, "testPrintDataLine18_column3");
            VMOUTUtil.setExceptionAt(AbstractFileLineWriter.class, "getColumn",
                    3, new ArrayIndexOutOfBoundsException("わざと"));

            VMOUTUtil.setReturnValueAtAllTimes(
                    AbstractFileLineWriterImpl01.class, "getDelimiter",
                    Character.MIN_VALUE);

            VMOUTUtil.setReturnValueAtAllTimes(
                    AbstractFileLineWriterImpl01.class, "getEncloseChar",
                    Character.MIN_VALUE);

            // テスト実施
            fileLineWriter.printDataLine(t);

            // 判定(状態変化、フィールド)
            assertEquals(1, ReflectionTestUtils.getField(fileLineWriter,
                    "currentLineCount"));

            // 判定(状態変化、メソッド)
            assertEquals(3, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "getColumn"));
            List getColumnArtuments1 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 0);
            assertEquals(2, getColumnArtuments1.size());
            assertSame(t, getColumnArtuments1.get(0));
            assertEquals(0, getColumnArtuments1.get(1));
            List getColumnArtuments2 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 1);
            assertEquals(2, getColumnArtuments2.size());
            assertSame(t, getColumnArtuments2.get(0));
            assertEquals(1, getColumnArtuments2.get(1));
            List getColumnArtuments3 = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "getColumn", 2);
            assertEquals(2, getColumnArtuments3.size());
            assertSame(t, getColumnArtuments3.get(0));
            assertEquals(2, getColumnArtuments3.get(1));

            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments.size());
            String systemLineSeparator = System.getProperty("line.separator");

            String expectationResultData = null;

            // mavenから起動するとなぜか結果が異なるため、mavenから起動時は想定値を変更する
            if (!("jp.co.dgic.testing.common.DJUnitClassLoader".equals(System
                    .getProperty("java.system.class.loader")))) {
                expectationResultData = "testPrintDataLine18_column1"
                        + "testPrintDataLine18_column2testPrintDataLine18_column3"
                        + systemLineSeparator;
            } else {
                expectationResultData = "testPrintDataLine18_column1,"
                        + "testPrintDataLine18_column2,testPrintDataLine18_column3"
                        + systemLineSeparator;
            }

            // System.out.println("========");
            // System.out.println(expectationResultData);
            // System.out.println("----");
            // System.out.println(writeArguments.get(0));
            // System.out.println("========");
            assertEquals("writeArguments.get(0)", expectationResultData,
                    writeArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "setWriteData"));
            List setWriteDataArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "setWriteData", 0);
            assertEquals(1, setWriteDataArguments.size());
            assertTrue(Boolean.class.cast(setWriteDataArguments.get(0)));

            // 判定(ファイル)
            writer.flush();
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintTrailerLine01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) trailerLine:要素を持たないListインスタンス<br>
     * (状態) writeTailer:false<br>
     * <br>
     * 期待値：(状態変化) writeTrailer:true<br>
     * (状態変化) #printList():1回呼ばれる<br>
     * 引数を確認する。<br>
     * <br>
     * 正常ケース<br>
     * 対象データの出力処理が行われることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintTrailerLine01() throws Exception {
        // 前処理(試験対象)
        String fileName = TEMP_FILE_NAME;

        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);
        try {
            fileLineWriter.init();

            // 前処理(引数)
            List<String> trailerLine = new ArrayList<String>();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                    Boolean.FALSE);

            // テスト実施
            fileLineWriter.printTrailerLine(trailerLine);

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "printList"));
            List printListArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "printList", 0);
            assertSame(trailerLine, printListArguments.get(0));
        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");
            if (writer != null) {
                writer.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintList01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) stringList:以下の要素を持つListインスタンス<br>
     * ・"testPrintList01_data1"<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintList01.txt"<br>
     * (状態) this.lineFeedChar:システムデフォルト値<br>
     * (状態) this.writer:this.fileNameファイルに対するWriterインスタンス<br>
     * 既にクローズされている。<br>
     * (状態) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testPrintList01.txt"<br>
     * ・内容なし(0Byte)<br>
     * <br>
     * 期待値：(状態変化) writer#write():1回呼ばれる。<br>
     * 引数を確認する。<br>
     * (状態変化) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testPrintList01.txt"<br>
     * ・内容なし(0Byte)<br>
     * <br>
     * ※変化なし<br>
     * (状態変化) -:以下の情報を持つFileExceptionが発生する<br>
     * ・メッセージ："Processing of writer was failed."<br>
     * ・原因例外：IOException<br>
     * ・ファイル名：fileNameと同じインスタンス<br>
     * <br>
     * 異常ケース<br>
     * writerが既にクローズされている場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintList01() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            List<String> stringList = new ArrayList<String>();
            stringList.add("testPrintList01_data1");

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "lineFeedChar", System
                    .getProperty("line.separator"));

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            // テスト実施
            Method method = AbstractFileLineWriter.class.getDeclaredMethod("printList", new Class[] {
                    List.class });
            method.setAccessible(true);
            Object result = method.invoke(fileLineWriter, new Object[] { stringList });

            fail("FileExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getTargetException().getClass()));
            assertEquals("Processing of writer was failed.", e.getTargetException().getMessage());
            assertTrue(IOException.class.isAssignableFrom(e.getTargetException().getCause()
                    .getClass()));
            assertEquals(fileName, ((FileException)e.getTargetException()).getFileName());

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals("testPrintList01_data1", writeArguments.get(0));

            // 判定(ファイル)
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintList02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(引数) stringList:要素を持たないListインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintList02.txt"<br>
     * (状態) this.lineFeedChar:システムデフォルト値<br>
     * (状態) this.writer:this.fileNameファイルに対するWriterインスタンス<br>
     * (状態) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testPrintList02.txt"<br>
     * ・内容なし(0Byte)<br>
     * <br>
     * 期待値：(状態変化) writer#write():呼ばれない。<br>
     * (状態変化) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testPrintList02.txt"<br>
     * ・内容なし(0Byte)<br>
     * <br>
     * ※変化なし<br>
     * <br>
     * 正常ケース。<br>
     * 出力対象データがない場合、何も出力されないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintList02() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            List<String> stringList = new ArrayList<String>();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "lineFeedChar", System
                    .getProperty("line.separator"));

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // テスト実施
            Method method = AbstractFileLineWriter.class.getDeclaredMethod("printList", new Class[] {
                    List.class });
            method.setAccessible(true);
            Object result = method.invoke(fileLineWriter, new Object[] { stringList });

            // 判定(状態変化、メソッド)
            assertFalse(VMOUTUtil.isCalled(Writer.class, "write"));

            // 判定(ファイル)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.flush();

            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintList03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(引数) stringList:以下の要素を持つListインスタンス<br>
     * ・"testPrintList03_data1"<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintList03.txt"<br>
     * (状態) this.lineFeedChar:システムデフォルト値<br>
     * (状態) this.writer:this.fileNameファイルに対するWriterインスタンス<br>
     * (状態) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testPrintList03.txt"<br>
     * ・内容なし(0Byte)<br>
     * <br>
     * 期待値：(状態変化) writer#write():2回呼ばれる。<br>
     * 引数を確認する。<br>
     * (状態変化) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testPrintList03.txt"<br>
     * ・内容："testPrintList03_data1<行区切り文字>"<br>
     * <br>
     * 正常ケース。<br>
     * 出力対象データが１つある場合、そのデータと行区切り文字が出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintList03() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            List<String> stringList = new ArrayList<String>();
            stringList.add("testPrintList03_data1");

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "lineFeedChar", System
                    .getProperty("line.separator"));

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // テスト実施
            Method method = AbstractFileLineWriter.class.getDeclaredMethod("printList", new Class[] {
                    List.class });
            method.setAccessible(true);
            Object result = method.invoke(fileLineWriter, new Object[] { stringList });

            // 判定(状態変化、メソッド)
            assertEquals(2, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments1 = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments1.size());
            assertEquals("testPrintList03_data1", writeArguments1.get(0));
            List writeArguments2 = VMOUTUtil.getArguments(Writer.class, "write",
                    1);
            assertEquals(1, writeArguments2.size());
            String systemLineSeparator = System.getProperty("line.separator");
            assertEquals(systemLineSeparator, writeArguments2.get(0));

            // 判定(ファイル)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.flush();

            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String expectationResultData = "testPrintList03_data1"
                    + systemLineSeparator;
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintList04() <br>
     * <br>
     * (正常系) <br>
     * 観点：D <br>
     * <br>
     * 入力値：(引数) stringList:以下の要素を持つListインスタンス<br>
     * ・"testPrintList04_data1"<br>
     * ・"testPrintList04_data2"<br>
     * ・"testPrintList04_data3"<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintList04.txt"<br>
     * (状態) this.lineFeedChar:システムデフォルト値<br>
     * (状態) this.writer:this.fileNameファイルに対するWriterインスタンス<br>
     * (状態) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testPrintList04.txt"<br>
     * ・内容なし(0Byte)<br>
     * <br>
     * 期待値：(状態変化) writer#write():6回呼ばれる。<br>
     * 引数を確認する。<br>
     * (状態変化) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testPrintList03.txt"<br>
     * ・内容："testPrintList04_data1<行区切り文字>testPrintList04_data2<行区切り文字>testPrintList04_data3<行区切り文字>"<br>
     * <br>
     * 正常ケース。<br>
     * 出力対象データが3つある場合、そのデータと行区切り文字が出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintList04() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            List<String> stringList = new ArrayList<String>();
            stringList.add("testPrintList04_data1");
            stringList.add("testPrintList04_data2");
            stringList.add("testPrintList04_data3");

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "lineFeedChar", System
                    .getProperty("line.separator"));

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // テスト実施
            Method method = AbstractFileLineWriter.class.getDeclaredMethod("printList", new Class[] {
                    List.class });
            method.setAccessible(true);
            Object result = method.invoke(fileLineWriter, new Object[] { stringList });

            // 判定(状態変化、メソッド)
            assertEquals(6, VMOUTUtil.getCallCount(Writer.class, "write"));
            List writeArguments1 = VMOUTUtil.getArguments(Writer.class, "write",
                    0);
            assertEquals(1, writeArguments1.size());
            assertEquals("testPrintList04_data1", writeArguments1.get(0));
            List writeArguments2 = VMOUTUtil.getArguments(Writer.class, "write",
                    1);
            assertEquals(1, writeArguments2.size());
            String systemLineSeparator = System.getProperty("line.separator");
            assertEquals(systemLineSeparator, writeArguments2.get(0));
            List writeArguments3 = VMOUTUtil.getArguments(Writer.class, "write",
                    2);
            assertEquals(1, writeArguments3.size());
            assertEquals("testPrintList04_data2", writeArguments3.get(0));
            List writeArguments4 = VMOUTUtil.getArguments(Writer.class, "write",
                    3);
            assertEquals(1, writeArguments4.size());
            assertEquals(systemLineSeparator, writeArguments4.get(0));
            List writeArguments5 = VMOUTUtil.getArguments(Writer.class, "write",
                    4);
            assertEquals(1, writeArguments5.size());
            assertEquals("testPrintList04_data3", writeArguments5.get(0));
            List writeArguments6 = VMOUTUtil.getArguments(Writer.class, "write",
                    5);
            assertEquals(1, writeArguments6.size());
            assertEquals(systemLineSeparator, writeArguments6.get(0));

            // 判定(ファイル)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.flush();

            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String expectationResultData = "testPrintList04_data1"
                    + systemLineSeparator + "testPrintList04_data2"
                    + systemLineSeparator + "testPrintList04_data3"
                    + systemLineSeparator;
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testPrintList05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) stringList:null<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testPrintList05.txt"<br>
     * (状態) this.lineFeedChar:システムデフォルト値<br>
     * (状態) this.writer:this.fileNameファイルに対するWriterインスタンス<br>
     * (状態) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testPrintList05.txt"<br>
     * ・内容なし(0Byte)<br>
     * <br>
     * 期待値：(状態変化) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testPrintList02.txt"<br>
     * ・内容なし(0Byte)<br>
     * <br>
     * ※変化なし<br>
     * (状態変化) -:NullPointerExceptionが発生する<br>
     * <br>
     * 異常ケース<br>
     * 引数がnullの場合は例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPrintList05() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(引数)
            List<String> stringList = null;

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "lineFeedChar", System
                    .getProperty("line.separator"));

            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // テスト実施
            Method method = AbstractFileLineWriter.class.getDeclaredMethod("printList", new Class[] {
                    List.class });
            method.setAccessible(true);
            Object result = method.invoke(fileLineWriter, new Object[] { stringList });
            fail("NullPointerExceptionが発生しませんでした。");
        } catch (InvocationTargetException e) {
            // 判定(例外)
            assertTrue(e.getTargetException() instanceof NullPointerException);
            assertTrue(NullPointerException.class.isAssignableFrom(e.getTargetException()
                    .getClass()));

            // 判定(状態変化、メソッド)
            assertFalse(VMOUTUtil.isCalled(Writer.class, "write"));

            // 判定(ファイル)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.flush();

            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testCloseFile01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testCloseFile01.txt"<br>
     * (状態) this.writer:this.fileNameファイルに対するWriterインスタンス<br>
     * 既にクローズされている。<br>
     * (状態) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testCloseFile01.txt"<br>
     * ・内容なし(0Byte)<br>
     * <br>
     * 期待値：(状態変化) writer#flush():1回呼ばれる。<br>
     * (状態変化) writer#close():呼ばれない。<br>
     * (状態変化) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testCloseFile01.txt"<br>
     * ・内容なし(0Byte)<br>
     * <br>
     * ※変化なし<br>
     * (状態変化) FileException:以下の情報を持つFileExceptionが発生する<br>
     * ・メッセージ："Closing of writer was failed."<br>
     * ・原因例外：IOException<br>
     * ・ファイル名：fileNameと同じインスタンス<br>
     * <br>
     * 異常ケース<br>
     * writerが既にクローズされている場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testCloseFile01() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.close();

            // テスト実施
            fileLineWriter.closeFile();
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("Closing of writer was failed.", e.getMessage());
            assertTrue(IOException.class.isAssignableFrom(e.getCause()
                    .getClass()));
            assertEquals(fileName, e.getFileName());

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "flush"));

            assertFalse(VMOUTUtil.isCalled(Writer.class, "write"));

            // 判定(ファイル)
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertFalse(postReader.ready());
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testCloseFile02() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testCloseFile02.txt"<br>
     * (状態) this.writer:this.fileNameファイルに対するWriterインスタンス<br>
     * 出力データとして以下のデータを持っている。<br>
     * ・"testCloseFile02_data1"<br>
     * (状態) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testCloseFile02.txt"<br>
     * ・内容なし(0Byte)<br>
     * <br>
     * 期待値：(状態変化) writer#flush():1回呼ばれる。<br>
     * (状態変化) writer#close():1回呼ばれる。<br>
     * (状態変化) ファイル:クラスパスに以下のファイルが存在する。<br>
     * "AbstractFileLineWriter_testCloseFile02.txt"<br>
     * ・内容："testCloseFile02_data1"<br>
     * <br>
     * 正常ケース<br>
     * writerに設定された全ての情報がファイルに出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testCloseFile02() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub10> clazz = AbstractFileLineWriter_Stub10.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub10> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub10>(fileName, clazz, columnFormatterMap);

        Reader postReader = null;
        Writer writer = null;
        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            writer = (Writer) ReflectionTestUtils.getField(fileLineWriter, "writer");
            writer.write("testCloseFile02_data1");

            // テスト実施
            fileLineWriter.closeFile();

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(Writer.class, "flush"));

            assertFalse(VMOUTUtil.isCalled(Writer.class, "write"));

            // 判定(ファイル)
            postReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), System
                    .getProperty("file.encoding")));
            assertTrue(postReader.ready());

            String expectationResultData = "testCloseFile02_data1";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());

        } finally {
            if (writer != null) {
                writer.close();
            }

            if (postReader != null) {
                postReader.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testGetColumn01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報を持つthis.clazzのインスタンス<br>
     * ・column1："testGetColumn01_data1"<br>
     * ・column2："testGetColumn01_data2"<br>
     * ・column3："testGetColumn01_data3"<br>
     * (引数) index:1<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testGetColumn01.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > bytes：48<br>
     *                         > stringConverter：以下の処理を持つStringConverterのクラスインスタンス<br>
     *                         - 入力されたデータに"_convert()"を追加した結果を返す。<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.methods:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.stringConverters:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         ・"java.util.Date"=DateColumnFormatter<br>
     *                         ・"java.math.BigDecimal"=DecimalColumnFormatter<br>
     *                         (状態) this.fileEncoding:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #isCheckByte():false<br>
     *                         (状態) FileDAOUtility#trim():正常終了<br>
     *                         入力されたデータに"_trim()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) FileDAOUtility#padding():正常終了<br>
     *                         入力されたデータに"_padding()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) ColumnFormatter#format():異常終了<br>
     *                         IllegalAccessExceptionが発生する。<br>
     *                         <br>
     *                         期待値：(状態変化) FileDAOUtility#trim():呼ばれない<br>
     *                         (状態変化) FileDAOUtility#padding():呼ばれない<br>
     *                         (状態変化) StringConverter#convert():呼ばれない<br>
     *                         (状態変化) #isCheckByte():呼ばれない<br>
     *                         (状態変化) -:以下の情報を持つFileLineExceptionが発生する<br>
     *                         ・メッセージ："Failed in column data formatting."<br>
     *                         ・原因例外：IllegalAccessException<br>
     *                         ・ファイル名：this.fileNameと同じインスタンス<br>
     *                         ・行数：1<br>
     *                         ・カラム名：column2<br>
     *                         ・カラム番号:1<br>
     *                         <br>
     *                         異常ケース<br>
     *                         ファイル行オブジェクトからデータを取得する処理でIlleageAccessExceptionが発生した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn01() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;
        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub35> clazz = AbstractFileLineWriter_Stub35.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        columnFormatterMap.put("java.util.Date", new DateColumnFormatter());
        columnFormatterMap.put("java.math.BigDecimal",
                new DecimalColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub35> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub35>(fileName, clazz, columnFormatterMap);

        // 前処理(引数)
        AbstractFileLineWriter_Stub35 t = new AbstractFileLineWriter_Stub35();
        t.setColumn1("testGetColumn01_data1");
        t.setColumn2("testGetColumn01_data2");
        t.setColumn3("testGetColumn01_data3");

        int index = 1;
        IllegalAccessException illegalAccessException = new IllegalAccessException("testGetColumn01例外");
        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "isCheckByte", Boolean.FALSE);

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "trim", 0,
                    "testGetColumn01_data2_trim()");

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "padding", 0,
                    "testGetColumn01_data2_trim()_padding()");

            VMOUTUtil.setExceptionAtAllTimes(NullColumnFormatter.class,
                    "format", illegalAccessException);

            // テスト実施
            fileLineWriter.getColumn(t, index);
            fail("FileLineExceptionが発生しませんでした。");
        } catch (FileLineException e) {
            // 判定(例外)
            assertTrue(FileLineException.class.isAssignableFrom(e.getClass()));
            assertEquals("Failed in column data formatting.", e.getMessage());
            assertSame(illegalAccessException, e.getCause());
            assertEquals(fileName, e.getFileName());
            assertEquals(1, e.getLineNo());
            assertEquals("column2", e.getColumnName());
            assertEquals(1, e.getColumnIndex());

            // 判定(状態変化、メソッド)
            assertFalse(VMOUTUtil.isCalled(FileDAOUtility.class, "trim"));

            assertFalse(VMOUTUtil.isCalled(FileDAOUtility.class, "padding"));

            assertFalse(VMOUTUtil.isCalled(
                    AbstractFileLineWriter_StringConverterStub03.class,
                    "convert"));

            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "isCheckByte"));
        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");

            if (writer != null) {
                writer.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testGetColumn02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報を持つthis.clazzのインスタンス<br>
     * ・column1："testGetColumn02_data1"<br>
     * ・column2："testGetColumn02_data2"<br>
     * ・column3："testGetColumn02_data3"<br>
     * (引数) index:1<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testGetColumn02.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > bytes：48<br>
     *                         > stringConverter：以下の処理を持つStringConverterのクラスインスタンス<br>
     *                         - 入力されたデータに"_convert()"を追加した結果を返す。<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.methods:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.stringConverters:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         ・"java.util.Date"=DateColumnFormatter<br>
     *                         ・"java.math.BigDecimal"=DecimalColumnFormatter<br>
     *                         (状態) this.fileEncoding:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #isCheckByte():false<br>
     *                         (状態) FileDAOUtility#trim():正常終了<br>
     *                         入力されたデータに"_trim()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) FileDAOUtility#padding():正常終了<br>
     *                         入力されたデータに"_padding()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) ColumnFormatter#format():異常終了<br>
     *                         llegalArgumentExceptionが発生する。<br>
     *                         <br>
     *                         期待値：(状態変化) FileDAOUtility#trim():呼ばれない<br>
     *                         (状態変化) FileDAOUtility#padding():呼ばれない<br>
     *                         (状態変化) StringConverter#convert():呼ばれない<br>
     *                         (状態変化) #isCheckByte():呼ばれない<br>
     *                         (状態変化) -:以下の情報を持つFileLineExceptionが発生する<br>
     *                         ・メッセージ："Failed in column data formatting."<br>
     *                         ・原因例外：IllegalArgumentException<br>
     *                         ・ファイル名：this.fileNameと同じインスタンス<br>
     *                         ・行数：1<br>
     *                         ・カラム名：column2<br>
     *                         ・カラム番号:1<br>
     *                         <br>
     *                         異常ケース<br>
     *                         ファイル行オブジェクトからデータを取得する処理でillegalArgumentExceptionが発生した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn02() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub35> clazz = AbstractFileLineWriter_Stub35.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        columnFormatterMap.put("java.util.Date", new DateColumnFormatter());
        columnFormatterMap.put("java.math.BigDecimal",
                new DecimalColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub35> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub35>(fileName, clazz, columnFormatterMap);

        // 前処理(引数)
        AbstractFileLineWriter_Stub35 t = new AbstractFileLineWriter_Stub35();
        t.setColumn1("testGetColumn02_data1");
        t.setColumn2("testGetColumn02_data2");
        t.setColumn3("testGetColumn02_data3");

        int index = 1;
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("testGetColumn02例外");
        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "isCheckByte", Boolean.FALSE);

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "trim", 0,
                    "testGetColumn02_data2_trim()");

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "padding", 0,
                    "testGetColumn02_data2_trim()_padding()");

            VMOUTUtil.setExceptionAtAllTimes(NullColumnFormatter.class,
                    "format", illegalArgumentException);

            // テスト実施
            fileLineWriter.getColumn(t, index);
            fail("FileLineExceptionが発生しませんでした。");
        } catch (FileLineException e) {
            // 判定(例外)
            assertTrue(FileLineException.class.isAssignableFrom(e.getClass()));
            assertEquals("Failed in column data formatting.", e.getMessage());
            assertSame(illegalArgumentException, e.getCause());
            assertEquals(fileName, e.getFileName());
            assertEquals(1, e.getLineNo());
            assertEquals("column2", e.getColumnName());
            assertEquals(1, e.getColumnIndex());

            // 判定(状態変化、メソッド)
            assertFalse(VMOUTUtil.isCalled(FileDAOUtility.class, "trim"));

            assertFalse(VMOUTUtil.isCalled(FileDAOUtility.class, "padding"));

            assertFalse(VMOUTUtil.isCalled(
                    AbstractFileLineWriter_StringConverterStub03.class,
                    "convert"));

            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "isCheckByte"));
        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");

            if (writer != null) {
                writer.close();
            }
            
            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testGetColumn03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報を持つthis.clazzのインスタンス<br>
     * ・column1："testGetColumn03_data1"<br>
     * ・column2："testGetColumn03_data2"<br>
     * ・column3："testGetColumn03_data3"<br>
     * (引数) index:1<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testGetColumn03.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > bytes：48<br>
     *                         > stringConverter：以下の処理を持つStringConverterのクラスインスタンス<br>
     *                         - 入力されたデータに"_convert()"を追加した結果を返す。<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.methods:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.stringConverters:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         ・"java.util.Date"=DateColumnFormatter<br>
     *                         ・"java.math.BigDecimal"=DecimalColumnFormatter<br>
     *                         (状態) this.fileEncoding:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #isCheckByte():false<br>
     *                         (状態) FileDAOUtility#trim():正常終了<br>
     *                         入力されたデータに"_trim()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) FileDAOUtility#padding():正常終了<br>
     *                         入力されたデータに"_padding()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) ColumnFormatter#format():異常終了<br>
     *                         InvocationTargetExceptionが発生する。<br>
     *                         <br>
     *                         期待値：(状態変化) FileDAOUtility#trim():呼ばれない<br>
     *                         (状態変化) FileDAOUtility#padding():呼ばれない<br>
     *                         (状態変化) StringConverter#convert():呼ばれない<br>
     *                         (状態変化) #isCheckByte():呼ばれない<br>
     *                         (状態変化) -:以下の情報を持つFileLineExceptionが発生する<br>
     *                         ・メッセージ："Failed in column data formatting."<br>
     *                         ・原因例外：InvocationTargetException<br>
     *                         ・ファイル名：this.fileNameと同じインスタンス<br>
     *                         ・行数：1<br>
     *                         ・カラム名：column2<br>
     *                         ・カラム番号:1<br>
     *                         <br>
     *                         異常ケース<br>
     *                         ファイル行オブジェクトからデータを取得する処理でInvocationTargetExceptionが発生した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn03() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;
        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub35> clazz = AbstractFileLineWriter_Stub35.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        columnFormatterMap.put("java.util.Date", new DateColumnFormatter());
        columnFormatterMap.put("java.math.BigDecimal",
                new DecimalColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub35> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub35>(fileName, clazz, columnFormatterMap);

        // 前処理(引数)
        AbstractFileLineWriter_Stub35 t = new AbstractFileLineWriter_Stub35();
        t.setColumn1("testGetColumn03_data1");
        t.setColumn2("testGetColumn03_data2");
        t.setColumn3("testGetColumn03_data3");

        int index = 1;
        InvocationTargetException invocationTargetException = new InvocationTargetException(new Exception("testGetColumn03例外"));
        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "isCheckByte", Boolean.FALSE);

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "trim", 0,
                    "testGetColumn03_data2_trim()");

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "padding", 0,
                    "testGetColumn03_data2_trim()_padding()");

            VMOUTUtil.setExceptionAtAllTimes(NullColumnFormatter.class,
                    "format", invocationTargetException);

            // テスト実施
            fileLineWriter.getColumn(t, index);
            fail("FileLineExceptionが発生しませんでした。");
        } catch (FileLineException e) {
            // 判定(例外)
            assertTrue(FileLineException.class.isAssignableFrom(e.getClass()));
            assertEquals("Failed in column data formatting.", e.getMessage());
            assertSame(invocationTargetException, e.getCause());
            assertEquals(fileName, e.getFileName());
            assertEquals(1, e.getLineNo());
            assertEquals("column2", e.getColumnName());
            assertEquals(1, e.getColumnIndex());

            // 判定(状態変化、メソッド)
            assertFalse(VMOUTUtil.isCalled(FileDAOUtility.class, "trim"));

            assertFalse(VMOUTUtil.isCalled(FileDAOUtility.class, "padding"));

            assertFalse(VMOUTUtil.isCalled(
                    AbstractFileLineWriter_StringConverterStub03.class,
                    "convert"));

            assertFalse(VMOUTUtil.isCalled(AbstractFileLineWriter.class,
                    "isCheckByte"));
        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");

            if (writer != null) {
                writer.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testGetColumn04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報を持つthis.clazzのインスタンス<br>
     * ・column1："testGetColumn04_data1"<br>
     * ・column2："testGetColumn04_data2XX"(bytes設定と合わないデータ)<br>
     * ・column3："testGetColumn04_data3"<br>
     * (引数) index:1<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testGetColumn04.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > bytes：48<br>
     *                         > stringConverter：以下の処理を持つStringConverterのクラスインスタンス<br>
     *                         - 入力されたデータに"_convert()"を追加した結果を返す。<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.methods:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.stringConverters:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         ・"java.util.Date"=DateColumnFormatter<br>
     *                         ・"java.math.BigDecimal"=DecimalColumnFormatter<br>
     *                         (状態) this.fileEncoding:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #isCheckByte():true<br>
     *                         (状態) FileDAOUtility#trim():正常終了<br>
     *                         入力されたデータに"_trim()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) FileDAOUtility#padding():正常終了<br>
     *                         入力されたデータに"_padding()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) ColumnFormatter#format():正常終了<br>
     *                         正しくフィールドの情報を返す。<br>
     *                         <br>
     *                         期待値：(状態変化) FileDAOUtility#trim():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) FileDAOUtility#padding():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) StringConverter#convert():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #isCheckByte():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) -:以下の情報を持つFileLineExceptionが発生する<br>
     *                         ・メッセージ："The data size is different from bytes value of the set value of the column ."<br>
     *                         ・原因例外：IllegalStateException<br>
     *                         ・ファイル名：this.fileNameと同じインスタンス<br>
     *                         ・行数：1<br>
     *                         ・カラム名：column2<br>
     *                         ・カラム番号:1<br>
     *                         <br>
     *                         異常ケース<br>
     *                         (バイト数チェックあり)<br>
     *                         取得対象フィールド値のバイト数がアノテーションの設定値と異なる場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn04() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub35> clazz = AbstractFileLineWriter_Stub35.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        columnFormatterMap.put("java.util.Date", new DateColumnFormatter());
        columnFormatterMap.put("java.math.BigDecimal",
                new DecimalColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub35> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub35>(fileName, clazz, columnFormatterMap);

        // 前処理(引数)
        AbstractFileLineWriter_Stub35 t = new AbstractFileLineWriter_Stub35();
        t.setColumn1("testGetColumn04_data1");
        t.setColumn2("testGetColumn04_data2XX");
        t.setColumn3("testGetColumn04_data3");

        int index = 1;

        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "isCheckByte", Boolean.TRUE);

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "trim", 0,
                    "testGetColumn04_data2XX_trim()");

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "padding", 0,
                    "testGetColumn04_data2XX_trim()_padding()");

            // テスト実施
            fileLineWriter.getColumn(t, index);
            fail("FileLineExceptionが発生しませんでした。");
        } catch (FileLineException e) {
            // 判定(例外)
            assertTrue(FileLineException.class.isAssignableFrom(e.getClass()));
            assertEquals("The data size is different from bytes value of the "
                    + "set value of the column .", e.getMessage());
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getCause()
                    .getClass()));
            assertEquals(fileName, e.getFileName());
            assertEquals("getLineNo", 1, e.getLineNo());
            assertEquals("column2", e.getColumnName());
            assertEquals("getColumnIndex", 1, e.getColumnIndex());

            // 判定(状態変化、メソッド)
            assertEquals("FileDAOUtility", 1, VMOUTUtil.getCallCount(
                    FileDAOUtility.class, "trim"));
            List trimArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "trim", 0);
            assertEquals(4, trimArguments.size());
            assertEquals("testGetColumn04_data2XX", trimArguments.get(0));
            assertEquals(System.getProperty("file.encoding"), trimArguments.get(
                    1));
            assertEquals(' ', trimArguments.get(2));
            assertEquals(TrimType.NONE, trimArguments.get(3));

            assertEquals("FileDAOUtility", 1, VMOUTUtil.getCallCount(
                    FileDAOUtility.class, "padding"));
            List paddingArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "padding", 0);
            assertEquals(5, paddingArguments.size());
            assertEquals("testGetColumn04_data2XX_trim()", paddingArguments.get(
                    0));
            assertEquals(System.getProperty("file.encoding"), paddingArguments
                    .get(1));
            assertEquals(48, paddingArguments.get(2));
            assertEquals(' ', paddingArguments.get(3));
            assertEquals(PaddingType.NONE, paddingArguments.get(4));

            // mavenから起動するとなぜかconvertが取得できないため、スキップする
            if (!("jp.co.dgic.testing.common.DJUnitClassLoader".equals(System
                    .getProperty("java.system.class.loader")))) {
                assertEquals("AbstractFileLineWriter_StringConverterStub03", 1,
                        VMOUTUtil.getCallCount(
                                AbstractFileLineWriter_StringConverterStub03.class,
                                "convert"));
                List convertArguments = VMOUTUtil.getArguments(
                        AbstractFileLineWriter_StringConverterStub03.class,
                        "convert", 0);
                assertEquals("convertArguments", 1, convertArguments.size());
                assertEquals("testGetColumn04_data2XX_trim()_padding()",
                        convertArguments.get(0));
            }

            assertEquals("isCheckByte", 1, VMOUTUtil.getCallCount(
                    AbstractFileLineWriter.class, "isCheckByte"));
            List isCheckByteArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "isCheckByte", 0);
            assertEquals("isCheckByteArguments", 1, isCheckByteArguments
                    .size());

        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");

            if (writer != null) {
                writer.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testGetColumn05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報を持つthis.clazzのインスタンス<br>
     * ・column1："testGetColumn05_data1"<br>
     * ・column2："testGetColumn05_data2"<br>
     * ・column3："testGetColumn05_data3"<br>
     * (引数) index:1<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testGetColumn05.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > bytes：48<br>
     *                         > stringConverter：以下の処理を持つStringConverterのクラスインスタンス<br>
     *                         - 入力されたデータに"_convert()"を追加した結果を返す。<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.methods:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.stringConverters:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         ・"java.util.Date"=DateColumnFormatter<br>
     *                         ・"java.math.BigDecimal"=DecimalColumnFormatter<br>
     *                         (状態) this.fileEncoding:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #isCheckByte():true<br>
     *                         (状態) FileDAOUtility#trim():正常終了<br>
     *                         入力されたデータに"_trim()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) FileDAOUtility#padding():正常終了<br>
     *                         入力されたデータに"_padding()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) ColumnFormatter#format():正常終了<br>
     *                         正しくフィールドの情報を返す。<br>
     *                         (状態) String#getBytes():異常終了<br>
     *                         UnsupportedEncodingExceptionが発生する。<br>
     *                         <br>
     *                         期待値：(状態変化) FileDAOUtility#trim():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) FileDAOUtility#padding():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) StringConverter#convert():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #isCheckByte():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) -:以下の情報を持つFileExceptionが発生する<br>
     *                         ・メッセージ："fileEncoding which isn't supported was set."<br>
     *                         ・原因例外：UnsupportedEncodingException<br>
     *                         ・ファイル名：this.fileNameと同じインスタンス<br>
     *                         <br>
     *                         異常ケース<br>
     *                         (バイト数チェックあり)<br>
     *                         取得対象フィールド値のバイト数チェックでUnsupportedEncodingExceptionが発生した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn05() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub35> clazz = AbstractFileLineWriter_Stub35.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        columnFormatterMap.put("java.util.Date", new DateColumnFormatter());
        columnFormatterMap.put("java.math.BigDecimal",
                new DecimalColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub35> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub35>(fileName, clazz, columnFormatterMap);

        // 前処理(引数)
        AbstractFileLineWriter_Stub35 t = new AbstractFileLineWriter_Stub35();
        t.setColumn1("testGetColumn05_data1");
        t.setColumn2("testGetColumn05_data2");
        t.setColumn3("testGetColumn05_data3");

        int index = 1;

        UnsupportedEncodingException unsupportedEncodingException = new UnsupportedEncodingException("testGetColumn05例外");

        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "isCheckByte", Boolean.TRUE);

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "trim", 0,
                    "testGetColumn05_data2_trim()");

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "padding", 0,
                    "testGetColumn05_data2_trim()_padding()");

            VMOUTUtil.setExceptionAt(String.class, "getBytes", 0,
                    unsupportedEncodingException);

            // テスト実施
            fileLineWriter.getColumn(t, index);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("fileEncoding which isn't supported was set.", e
                    .getMessage());
            assertSame(unsupportedEncodingException, e.getCause());
            assertEquals(fileName, e.getFileName());

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(FileDAOUtility.class,
                    "trim"));
            List trimArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "trim", 0);
            assertEquals(4, trimArguments.size());
            assertEquals("testGetColumn05_data2", trimArguments.get(0));
            assertEquals(System.getProperty("file.encoding"), trimArguments.get(
                    1));
            assertEquals(' ', trimArguments.get(2));
            assertEquals(TrimType.NONE, trimArguments.get(3));

            assertEquals(1, VMOUTUtil.getCallCount(FileDAOUtility.class,
                    "padding"));
            List paddingArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "padding", 0);
            assertEquals(5, paddingArguments.size());
            assertEquals("testGetColumn05_data2_trim()", paddingArguments.get(
                    0));
            assertEquals(System.getProperty("file.encoding"), paddingArguments
                    .get(1));
            assertEquals(48, paddingArguments.get(2));
            assertEquals(' ', paddingArguments.get(3));
            assertEquals(PaddingType.NONE, paddingArguments.get(4));

            // mavenから起動するとなぜかconvertが取得できないため、スキップする
            if (!("jp.co.dgic.testing.common.DJUnitClassLoader".equals(System
                    .getProperty("java.system.class.loader")))) {
                assertEquals("AbstractFileLineWriter_StringConverterStub03", 1,
                        VMOUTUtil.getCallCount(
                                AbstractFileLineWriter_StringConverterStub03.class,
                                "convert"));
                List convertArguments = VMOUTUtil.getArguments(
                        AbstractFileLineWriter_StringConverterStub03.class,
                        "convert", 0);
                assertEquals(1, convertArguments.size());
                assertEquals("testGetColumn05_data2_trim()_padding()",
                        convertArguments.get(0));
            }

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "isCheckByte"));
            List isCheckByteArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "isCheckByte", 0);
            assertEquals(1, isCheckByteArguments.size());

        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");

            if (writer != null) {
                writer.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testGetColumn06() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報を持つthis.clazzのインスタンス<br>
     * ・column1："testGetColumn06_data1"<br>
     * ・column2：null<br>
     * ・column3："testGetColumn06_data3"<br>
     * (引数) index:1<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testGetColumn06.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > bytes：48<br>
     *                         > stringConverter：以下の処理を持つStringConverterのクラスインスタンス<br>
     *                         - 入力されたデータに"_convert()"を追加した結果を返す。<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.methods:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.stringConverters:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=以下の処理を持つColumnFormatterのクラスインスタンス<br>
     *                         - 必ずnullを返す。<br>
     *                         ・"java.util.Date"=DateColumnFormatter<br>
     *                         ・"java.math.BigDecimal"=DecimalColumnFormatter<br>
     *                         (状態) this.fileEncoding:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #isCheckByte():false<br>
     *                         (状態) FileDAOUtility#trim():正常終了<br>
     *                         入力されたデータに"_trim()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) FileDAOUtility#padding():正常終了<br>
     *                         入力されたデータに"_padding()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) ColumnFormatter#format():正常終了<br>
     *                         nullを返す。<br>
     *                         <br>
     *                         期待値：(戻り値) String:"_trim()_padding()_convert()"<br>
     *                         (状態変化) FileDAOUtility#trim():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) FileDAOUtility#padding():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) StringConverter#convert():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #isCheckByte():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (バイト数チェックなし)<br>
     *                         ColumnFormmater#format()の結果、フィールド値がnullの場合、空文字として処理されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn06() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub35> clazz = AbstractFileLineWriter_Stub35.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String",
                new AbstractFileLineWriter_ColumnFormatterStub01());
        columnFormatterMap.put("java.util.Date", new DateColumnFormatter());
        columnFormatterMap.put("java.math.BigDecimal",
                new DecimalColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub35> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub35>(fileName, clazz, columnFormatterMap);

        // 前処理(引数)
        AbstractFileLineWriter_Stub35 t = new AbstractFileLineWriter_Stub35();
        t.setColumn1("testGetColumn06_data1");
        t.setColumn2(null);
        t.setColumn3("testGetColumn06_data3");

        int index = 1;

        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "isCheckByte", Boolean.FALSE);

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "trim", 0,
                    "_trim()");

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "padding", 0,
                    "_trim()_padding()");

            // テスト実施
            String result = fileLineWriter.getColumn(t, index);

            // 判定(戻り値)
            assertNotNull(result);
            assertEquals("_trim()_padding()_convert()", result);

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(FileDAOUtility.class,
                    "trim"));
            List trimArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "trim", 0);
            assertEquals(4, trimArguments.size());
            assertEquals("", trimArguments.get(0));
            assertEquals(System.getProperty("file.encoding"), trimArguments.get(
                    1));
            assertEquals(' ', trimArguments.get(2));
            assertEquals(TrimType.NONE, trimArguments.get(3));

            assertEquals(1, VMOUTUtil.getCallCount(FileDAOUtility.class,
                    "padding"));
            List paddingArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "padding", 0);
            assertEquals(5, paddingArguments.size());
            assertEquals("_trim()", paddingArguments.get(0));
            assertEquals(System.getProperty("file.encoding"), paddingArguments
                    .get(1));
            assertEquals(48, paddingArguments.get(2));
            assertEquals(' ', paddingArguments.get(3));
            assertEquals(PaddingType.NONE, paddingArguments.get(4));

            // mavenから起動するとなぜかconvertが取得できないため、スキップする
            if (!("jp.co.dgic.testing.common.DJUnitClassLoader".equals(System
                    .getProperty("java.system.class.loader")))) {
                assertEquals("AbstractFileLineWriter_StringConverterStub03", 1,
                        VMOUTUtil.getCallCount(
                                AbstractFileLineWriter_StringConverterStub03.class,
                                "convert"));
                List convertArguments = VMOUTUtil.getArguments(
                        AbstractFileLineWriter_StringConverterStub03.class,
                        "convert", 0);
                assertEquals(1, convertArguments.size());
                assertEquals("_trim()_padding()", convertArguments.get(0));
            }

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "isCheckByte"));
            List isCheckByteArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "isCheckByte", 0);
            assertEquals(1, isCheckByteArguments.size());

        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");

            if (writer != null) {
                writer.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testGetColumn07() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報を持つthis.clazzのインスタンス<br>
     * ・column1："testGetColumn07_data1"<br>
     * ・column2：""(空文字)<br>
     * ・column3："testGetColumn07_data3"<br>
     * (引数) index:0<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testGetColumn07.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > bytes：48<br>
     *                         > stringConverter：以下の処理を持つStringConverterのクラスインスタンス<br>
     *                         - 入力されたデータに"_convert()"を追加した結果を返す。<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.methods:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.stringConverters:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         ・"java.util.Date"=DateColumnFormatter<br>
     *                         ・"java.math.BigDecimal"=DecimalColumnFormatter<br>
     *                         (状態) this.fileEncoding:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #isCheckByte():false<br>
     *                         (状態) FileDAOUtility#trim():正常終了<br>
     *                         入力されたデータに"_trim()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) FileDAOUtility#padding():正常終了<br>
     *                         入力されたデータに"_padding()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) ColumnFormatter#format():正常終了<br>
     *                         正しくフィールドの情報を返す。<br>
     *                         <br>
     *                         期待値：(戻り値) String:"_trim()_padding()_convert()"<br>
     *                         (状態変化) FileDAOUtility#trim():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) FileDAOUtility#padding():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) StringConverter#convert():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #isCheckByte():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (バイト数チェックなし)<br>
     *                         ColumnFormmater#format()の結果、フィールド値が空文字の場合、そのまま処理されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn07() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub35> clazz = AbstractFileLineWriter_Stub35.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String",
                new AbstractFileLineWriter_ColumnFormatterStub01());
        columnFormatterMap.put("java.util.Date", new DateColumnFormatter());
        columnFormatterMap.put("java.math.BigDecimal",
                new DecimalColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub35> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub35>(fileName, clazz, columnFormatterMap);

        // 前処理(引数)
        AbstractFileLineWriter_Stub35 t = new AbstractFileLineWriter_Stub35();
        t.setColumn1("testGetColumn07_data1");
        t.setColumn2("");
        t.setColumn3("testGetColumn07_data3");

        int index = 1;

        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "isCheckByte", Boolean.FALSE);

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "trim", 0,
                    "_trim()");

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "padding", 0,
                    "_trim()_padding()");

            // テスト実施
            String result = fileLineWriter.getColumn(t, index);

            // 判定(戻り値)
            assertNotNull(result);
            assertEquals("_trim()_padding()_convert()", result);

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(FileDAOUtility.class,
                    "trim"));
            List trimArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "trim", 0);
            assertEquals(4, trimArguments.size());
            assertEquals("", trimArguments.get(0));
            assertEquals(System.getProperty("file.encoding"), trimArguments.get(
                    1));
            assertEquals(' ', trimArguments.get(2));
            assertEquals(TrimType.NONE, trimArguments.get(3));

            assertEquals(1, VMOUTUtil.getCallCount(FileDAOUtility.class,
                    "padding"));
            List paddingArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "padding", 0);
            assertEquals(5, paddingArguments.size());
            assertEquals("_trim()", paddingArguments.get(0));
            assertEquals(System.getProperty("file.encoding"), paddingArguments
                    .get(1));
            assertEquals(48, paddingArguments.get(2));
            assertEquals(' ', paddingArguments.get(3));
            assertEquals(PaddingType.NONE, paddingArguments.get(4));

            // mavenから起動するとなぜかconvertが取得できないため、スキップする
            if (!("jp.co.dgic.testing.common.DJUnitClassLoader".equals(System
                    .getProperty("java.system.class.loader")))) {
                assertEquals("AbstractFileLineWriter_StringConverterStub03", 1,
                        VMOUTUtil.getCallCount(
                                AbstractFileLineWriter_StringConverterStub03.class,
                                "convert"));
                List convertArguments = VMOUTUtil.getArguments(
                        AbstractFileLineWriter_StringConverterStub03.class,
                        "convert", 0);
                assertEquals(1, convertArguments.size());
                assertEquals("_trim()_padding()", convertArguments.get(0));
            }

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "isCheckByte"));
            List isCheckByteArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "isCheckByte", 0);
            assertEquals(1, isCheckByteArguments.size());

        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");

            if (writer != null) {
                writer.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testGetColumn09() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報を持つthis.clazzのインスタンス<br>
     * ・column1："testGetColumn09_data1"<br>
     * ・column2："testGetColumn09_data2"<br>
     * ・column3："testGetColumn09_data3"<br>
     * (引数) index:0<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testGetColumn09.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > bytes：48<br>
     *                         > stringConverter：以下の処理を持つStringConverterのクラスインスタンス<br>
     *                         - 入力されたデータに"_convert()"を追加した結果を返す。<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > bytes：48<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.methods:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.stringConverters:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         ・"java.util.Date"=DateColumnFormatter<br>
     *                         ・"java.math.BigDecimal"=DecimalColumnFormatter<br>
     *                         (状態) this.fileEncoding:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #isCheckByte():false<br>
     *                         (状態) FileDAOUtility#trim():正常終了<br>
     *                         入力されたデータに"_trim()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) FileDAOUtility#padding():正常終了<br>
     *                         入力されたデータに"_padding()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) ColumnFormatter#format():正常終了<br>
     *                         正しくフィールドの情報を返す。<br>
     *                         <br>
     *                         期待値：(戻り値) String:"testGetColumn09_data2_trim()_padding()_convert()"<br>
     *                         (状態変化) FileDAOUtility#trim():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) FileDAOUtility#padding():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) StringConverter#convert():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #isCheckByte():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         <br>
     *                         正常ケース<br>
     *                         (バイト数チェックあり)<br>
     *                         取得対象フィールド値のバイト数がアノテーションの設定値と一致した場合、問題なく処理されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn09() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub35> clazz = AbstractFileLineWriter_Stub35.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        columnFormatterMap.put("java.util.Date", new DateColumnFormatter());
        columnFormatterMap.put("java.math.BigDecimal",
                new DecimalColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub35> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub35>(fileName, clazz, columnFormatterMap);

        // 前処理(引数)
        AbstractFileLineWriter_Stub35 t = new AbstractFileLineWriter_Stub35();
        t.setColumn1("testGetColumn09_data1");
        t.setColumn2("testGetColumn09_data2");
        t.setColumn3("testGetColumn09_data3");

        int index = 1;

        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "isCheckByte", Boolean.TRUE);

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "trim", 0,
                    "testGetColumn09_data2_trim()");

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "padding", 0,
                    "testGetColumn09_data2_trim()_padding()");

            // テスト実施
            String result = fileLineWriter.getColumn(t, index);

            // 判定(戻り値)
            assertNotNull(result);
            assertEquals("testGetColumn09_data2_trim()_padding()_convert()",
                    result);

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(FileDAOUtility.class,
                    "trim"));
            List trimArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "trim", 0);
            assertEquals(4, trimArguments.size());
            assertEquals("testGetColumn09_data2", trimArguments.get(0));
            assertEquals(System.getProperty("file.encoding"), trimArguments.get(
                    1));
            assertEquals(' ', trimArguments.get(2));
            assertEquals(TrimType.NONE, trimArguments.get(3));

            assertEquals(1, VMOUTUtil.getCallCount(FileDAOUtility.class,
                    "padding"));
            List paddingArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "padding", 0);
            assertEquals(5, paddingArguments.size());
            assertEquals("testGetColumn09_data2_trim()", paddingArguments.get(
                    0));
            assertEquals(System.getProperty("file.encoding"), paddingArguments
                    .get(1));
            assertEquals(48, paddingArguments.get(2));
            assertEquals(' ', paddingArguments.get(3));
            assertEquals(PaddingType.NONE, paddingArguments.get(4));

            // mavenから起動するとなぜかconvertが取得できないため、スキップする
            if (!("jp.co.dgic.testing.common.DJUnitClassLoader".equals(System
                    .getProperty("java.system.class.loader")))) {
                assertEquals("AbstractFileLineWriter_StringConverterStub03", 1,
                        VMOUTUtil.getCallCount(
                                AbstractFileLineWriter_StringConverterStub03.class,
                                "convert"));
                List convertArguments = VMOUTUtil.getArguments(
                        AbstractFileLineWriter_StringConverterStub03.class,
                        "convert", 0);
                assertEquals(1, convertArguments.size());
                assertEquals("testGetColumn09_data2_trim()_padding()",
                        convertArguments.get(0));
            }

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "isCheckByte"));
            List isCheckByteArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "isCheckByte", 0);
            assertEquals(1, isCheckByteArguments.size());

        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");

            if (writer != null) {
                writer.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testGetColumn10() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報を持つthis.clazzのインスタンス<br>
     * ・column1："testGetColumn10_data1"<br>
     * ・column2："testGetColumn10_data2"<br>
     * ・column3："testGetColumn10_data3"<br>
     * (引数) index:0<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testGetColumn10.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.methods:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.stringConverters:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         ・"java.util.Date"=DateColumnFormatter<br>
     *                         ・"java.math.BigDecimal"=DecimalColumnFormatter<br>
     *                         (状態) this.fileEncoding:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #isCheckByte():true<br>
     *                         (状態) FileDAOUtility#trim():正常終了<br>
     *                         入力されたデータに"_trim()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) FileDAOUtility#padding():正常終了<br>
     *                         入力されたデータに"_padding()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) ColumnFormatter#format():正常終了<br>
     *                         正しくフィールドの情報を返す。<br>
     *                         <br>
     *                         期待値：(状態変化) FileDAOUtility#trim():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) FileDAOUtility#padding():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) StringConverter#convert():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #isCheckByte():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) -:以下の情報を持つFileLineExceptionが発生する<br>
     *                         ・メッセージ："bytes is not set or a number equal to or less than 0 is set."<br>
     *                         ・原因例外：IllegalStateException<br>
     *                         ・ファイル名：this.fileNameと同じインスタンス<br>
     *                         ・行数：1<br>
     *                         ・カラム名：column2<br>
     *                         ・カラム番号:1<br>
     *                         <br>
     *                         異常ケース<br>
     *                         (バイト数チェックあり)<br>
     *                         対象フィールドに対してバイト数がマイナス値(デフォルト値など)で設定されていた場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn10() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;
        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub13> clazz = AbstractFileLineWriter_Stub13.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        columnFormatterMap.put("java.util.Date", new DateColumnFormatter());
        columnFormatterMap.put("java.math.BigDecimal",
                new DecimalColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub13> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub13>(fileName, clazz, columnFormatterMap);

        // 前処理(引数)
        AbstractFileLineWriter_Stub13 t = new AbstractFileLineWriter_Stub13();
        t.setColumn1("testGetColumn10_data1");
        t.setColumn2("testGetColumn10_data2");
        t.setColumn3("testGetColumn10_data3");

        int index = 1;

        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "isCheckByte", Boolean.TRUE);

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "trim", 0,
                    "testGetColumn10_data2_trim()");

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "padding", 0,
                    "testGetColumn10_data2_trim()_padding()");

            // テスト実施
            fileLineWriter.getColumn(t, index);
            fail("FileLineExceptionが発生しませんでした。");
        } catch (FileLineException e) {
            // 判定(例外)
            assertTrue(FileLineException.class.isAssignableFrom(e.getClass()));
            assertEquals("bytes is not set or a number equal to or less than 0 "
                    + "is set.", e.getMessage());
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getCause()
                    .getClass()));
            assertEquals(fileName, e.getFileName());
            assertEquals(1, e.getLineNo());
            assertEquals("column2", e.getColumnName());
            assertEquals(1, e.getColumnIndex());

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(FileDAOUtility.class,
                    "trim"));
            List trimArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "trim", 0);
            assertEquals(4, trimArguments.size());
            assertEquals("testGetColumn10_data2", trimArguments.get(0));
            assertEquals(System.getProperty("file.encoding"), trimArguments.get(
                    1));
            assertEquals(' ', trimArguments.get(2));
            assertEquals(TrimType.NONE, trimArguments.get(3));

            assertEquals(1, VMOUTUtil.getCallCount(FileDAOUtility.class,
                    "padding"));
            List paddingArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "padding", 0);
            assertEquals(5, paddingArguments.size());
            assertEquals("testGetColumn10_data2_trim()", paddingArguments.get(
                    0));
            assertEquals(System.getProperty("file.encoding"), paddingArguments
                    .get(1));
            assertEquals(-1, paddingArguments.get(2));
            assertEquals(' ', paddingArguments.get(3));
            assertEquals(PaddingType.NONE, paddingArguments.get(4));

            assertEquals(1, VMOUTUtil.getCallCount(NullStringConverter.class,
                    "convert"));
            List convertArguments = VMOUTUtil.getArguments(
                    NullStringConverter.class, "convert", 0);
            assertEquals(1, convertArguments.size());
            assertEquals("testGetColumn10_data2_trim()_padding()",
                    convertArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "isCheckByte"));
            List isCheckByteArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "isCheckByte", 0);
            assertEquals(1, isCheckByteArguments.size());

        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");

            if (writer != null) {
                writer.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testGetColumn11() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) ｔ:以下の情報を持つthis.clazzのインスタンス<br>
     * ・column1："testGetColumn11_data1"<br>
     * ・column2："testGetColumn11_data2"<br>
     * ・column3："testGetColumn11_data3"<br>
     * (引数) index:0<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineWriter_testGetColumn11.txt"<br>
     * (状態) this.clazz:以下の設定を持つClassインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@OutputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column2<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：1<br>
     *                         > bytes：0<br>
     *                         > その他項目：デフォルト値<br>
     *                         - フィールド：String column3<br>
     * @OutputFileColumn設定<br>
     *                         > columnIndex：2<br>
     *                         > その他項目：デフォルト値<br>
     *                         ・各フィールドのgetter/setterメソッドを持つ。<br>
     *                         (状態) this.fields:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.methods:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.stringConverters:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     *                         ・"int"=IntColumnFormatter<br>
     *                         ・"java.lang.String"=NullColumnFormatter<br>
     *                         ・"java.util.Date"=DateColumnFormatter<br>
     *                         ・"java.math.BigDecimal"=DecimalColumnFormatter<br>
     *                         (状態) this.fileEncoding:this.clazzのフィールド定義に従う。<br>
     *                         (状態) this.currentLineCount:0<br>
     *                         (状態) #isCheckByte():true<br>
     *                         (状態) FileDAOUtility#trim():正常終了<br>
     *                         入力されたデータに"_trim()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) FileDAOUtility#padding():正常終了<br>
     *                         入力されたデータに"_padding()"を追加した結果を返す。<br>
     *                         <br>
     *                         ※引数確認のため<br>
     *                         (状態) ColumnFormatter#format():正常終了<br>
     *                         正しくフィールドの情報を返す。<br>
     *                         <br>
     *                         期待値：(状態変化) FileDAOUtility#trim():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) FileDAOUtility#padding():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) StringConverter#convert():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) #isCheckByte():1回呼ばれる<br>
     *                         引数を確認する。<br>
     *                         (状態変化) -:以下の情報を持つFileLineExceptionが発生する<br>
     *                         ・メッセージ："bytes is not set or a number equal to or less than 0 is set."<br>
     *                         ・原因例外：IllegalStateException<br>
     *                         ・ファイル名：this.fileNameと同じインスタンス<br>
     *                         ・行数：1<br>
     *                         ・カラム名：column2<br>
     *                         ・カラム番号:1<br>
     *                         <br>
     *                         異常ケース<br>
     *                         (バイト数チェックあり)<br>
     *                         対象フィールドに対してバイト数が0で設定されていた場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetColumn11() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;
        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub36> clazz = AbstractFileLineWriter_Stub36.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        columnFormatterMap.put("java.util.Date", new DateColumnFormatter());
        columnFormatterMap.put("java.math.BigDecimal",
                new DecimalColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub36> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub36>(fileName, clazz, columnFormatterMap);

        // 前処理(引数)
        AbstractFileLineWriter_Stub36 t = new AbstractFileLineWriter_Stub36();
        t.setColumn1("testGetColumn11_data1");
        t.setColumn2("testGetColumn11_data2");
        t.setColumn3("testGetColumn11_data3");

        int index = 1;

        try {
            fileLineWriter.init();

            // 前処理(フィールド)
            ReflectionTestUtils.setField(fileLineWriter, "currentLineCount", 0);

            // 前処理(メソッド)
            VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineWriter.class,
                    "isCheckByte", Boolean.TRUE);

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "trim", 0,
                    "testGetColumn11_data2_trim()");

            VMOUTUtil.setReturnValueAt(FileDAOUtility.class, "padding", 0,
                    "testGetColumn11_data2_trim()_padding()");

            // テスト実施
            fileLineWriter.getColumn(t, index);
            fail("FileLineExceptionが発生しませんでした。");
        } catch (FileLineException e) {
            // 判定(例外)
            assertTrue(FileLineException.class.isAssignableFrom(e.getClass()));
            assertEquals("bytes is not set or a number equal to or less than 0 "
                    + "is set.", e.getMessage());
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getCause()
                    .getClass()));
            assertEquals(fileName, e.getFileName());
            assertEquals(1, e.getLineNo());
            assertEquals("column2", e.getColumnName());
            assertEquals(1, e.getColumnIndex());

            // 判定(状態変化、メソッド)
            assertEquals(1, VMOUTUtil.getCallCount(FileDAOUtility.class,
                    "trim"));
            List trimArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "trim", 0);
            assertEquals(4, trimArguments.size());
            assertEquals("testGetColumn11_data2", trimArguments.get(0));
            assertEquals(System.getProperty("file.encoding"), trimArguments.get(
                    1));
            assertEquals(' ', trimArguments.get(2));
            assertEquals(TrimType.NONE, trimArguments.get(3));

            assertEquals(1, VMOUTUtil.getCallCount(FileDAOUtility.class,
                    "padding"));
            List paddingArguments = VMOUTUtil.getArguments(FileDAOUtility.class,
                    "padding", 0);
            assertEquals(5, paddingArguments.size());
            assertEquals("testGetColumn11_data2_trim()", paddingArguments.get(
                    0));
            assertEquals(System.getProperty("file.encoding"), paddingArguments
                    .get(1));
            assertEquals(0, paddingArguments.get(2));
            assertEquals(' ', paddingArguments.get(3));
            assertEquals(PaddingType.NONE, paddingArguments.get(4));

            assertEquals(1, VMOUTUtil.getCallCount(NullStringConverter.class,
                    "convert"));
            List convertArguments = VMOUTUtil.getArguments(
                    NullStringConverter.class, "convert", 0);
            assertEquals(1, convertArguments.size());
            assertEquals("testGetColumn11_data2_trim()_padding()",
                    convertArguments.get(0));

            assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineWriter.class,
                    "isCheckByte"));
            List isCheckByteArguments = VMOUTUtil.getArguments(
                    AbstractFileLineWriter.class, "isCheckByte", 0);
            assertEquals(1, isCheckByteArguments.size());
        } finally {
            Writer writer = (Writer) ReflectionTestUtils.getField(fileLineWriter,
                    "writer");

            if (writer != null) {
                writer.close();
            }

            Field field = AbstractFileLineWriter.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            field.set(AbstractFileLineWriter.class, new HashMap<Class, StringConverter>());        
        }
    }

    /**
     * testGetFileName01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) fileName:not null<br>
     * <br>
     * 期待値：(戻り値) fileName:not null<br>
     * <br>
     * fileNameのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetFileName01() throws Exception {
        // 前処理(引数)
        String fileName = "fileName";

        // 前処理(試験対象)
        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "fileName", fileName);

        // テスト実施
        String result = fileLineWriter.getFileName();

        // 判定(戻り値)
        assertNotNull(result);
        assertSame(fileName, result);
    }

    /**
     * testGetLineFeedChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) this.lineFeedChar:not null<br>
     * <br>
     * 期待値：(戻り値) lineFeedChar:not null<br>
     * <br>
     * lineFeedCharのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testGetLineFeedChar01() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);
        // 前処理(引数)
        String lineFeedChar = "testGetLineFeedChar01_lineFeedChar";

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "lineFeedChar",
                lineFeedChar);

        // テスト実施
        String result = fileLineWriter.getLineFeedChar();

        // 判定(戻り値)
        assertNotNull(result);
        assertSame(lineFeedChar, result);
    }

    /**
     * testSetColumnFormatterMap01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) columnFormatterMap:not null<br>
     * <br>
     * 期待値：(状態変化) columnFormatterMap:not null<br>
     * <br>
     * columnFormatterMapのsetterメソッドの値が正しく設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSetColumnFormatterMap01() throws Exception {
        // 前処理(引数)
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "columnFormatterMap",
                null);

        // テスト実施
        fileLineWriter.setColumnFormatterMap(columnFormatterMap);

        // 判定(状態変化、フィールド)
        Map<String, ColumnFormatter> resultMap = (Map<String, ColumnFormatter>) ReflectionTestUtils.getField(fileLineWriter, "columnFormatterMap");
        assertNotNull(resultMap);
        assertSame(columnFormatterMap, resultMap);
    }

    /**
     * testGetWriter01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) this.writer:not null<br>
     * <br>
     * 期待値：(戻り値) writer:not null<br>
     * <br>
     * writerのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testGetWriter01() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(fileName, true)), System
                    .getProperty("file.encoding")));

            ReflectionTestUtils.setField(fileLineWriter, "writer", writer);
            // テスト実施
            Writer result = fileLineWriter.getWriter();

            // 判定(戻り値)
            assertNotNull(result);
            assertSame(writer, result);
        } finally {
            if (writer != null) {
                writer.close();
            }
            // 試験後生成されるファイルの削除
            File file = new File(fileName);
            file.delete();
        }
    }

    /**
     * testGetFields01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) this.fields:not null<br>
     * <br>
     * 期待値：(戻り値) fields:not null<br>
     * <br>
     * fieldsのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    @Test
    public void testGetFields01() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        Field[] fields = new Field[] { null, null, null };
        ReflectionTestUtils.setField(fileLineWriter, "fields", fields);

        // テスト実施
        Field[] result = fileLineWriter.getFields();

        // 判定(戻り値)
        assertNotNull(result);
        assertSame(fields, result);
    }

    /**
     * testGetMethods01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) this.getMethods:not null<br>
     * <br>
     * 期待値：(戻り値) methods:not null<br>
     * <br>
     * methodsのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetMethods01() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        Method[] methods = new Method[] { null, null, null };
        ReflectionTestUtils.setField(fileLineWriter, "methods", methods);

        // テスト実施
        Method[] result = fileLineWriter.getMethods();

        // 判定(戻り値)
        assertNotNull(result);
        assertSame(methods, result);
    }

    /**
     * testSetWriteData01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) writeData:true<br>
     * (状態) this.writeData:false<br>
     * <br>
     * 期待値：(状態変化) writeData:true<br>
     * <br>
     * writeDataのsetterメソッドの値が正しく設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetWriteData01() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "writeData",
                Boolean.FALSE);

        // テスト実施
        fileLineWriter.setWriteData(true);

        // 判定(状態変化、フィールド)
        boolean resultBoolean = Boolean.class.cast(ReflectionTestUtils.getField(
                fileLineWriter, "writeData"));
        assertNotNull(resultBoolean);
        assertTrue(resultBoolean);
    }

    /**
     * testCheckWriteTrailer01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(状態) this.isWriteTrailer:false<br>
     * <br>
     * 期待値： <br>
     * トレイラを出力していないときは何もしない <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCheckWriteTrailer01() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                Boolean.FALSE);

        // テスト実施
        try {
            fileLineWriter.checkWriteTrailer();
        } catch (FileException e) {
            // 判定
            fail("FileException例外が発生しました。");

        }
    }

    /**
     * testCheckWriteTrailer02() <br>
     * <br>
     * (正常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) this.isWriteTrailer:true<br>
     * <br>
     * 期待値：(状態変化) -:FileExceptionが発生する<br>
     * 原因例外：IllegalStateException<br>
     * ファイル名が入力値のfileNameに一致することを確認する。<br>
     * Header part or data part should be called before TrailerPart",<br>
     * <br>
     * トレイラの出力が完了している場合、例外がスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCheckWriteTrailer02() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub01> clazz = AbstractFileLineWriter_Stub01.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        AbstractFileLineWriter<AbstractFileLineWriter_Stub01> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub01>(fileName, clazz, columnFormatterMap);

        // 前処理(フィールド)
        ReflectionTestUtils.setField(fileLineWriter, "writeTrailer",
                Boolean.TRUE);

        // テスト実施
        try {
            fileLineWriter.checkWriteTrailer();
            fail("FileException例外が発生しませんでした。");
        } catch (FileException e) {
            // 判定(例外)
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("Header part or data part should be called before "
                    + "TrailerPart", e.getMessage());
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getCause()
                    .getClass()));
            assertEquals(fileName, e.getFileName());
        }
    }

    /**
     * testIsCheckByte01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) outputFileColumn:not null<br>
     * (状態) outputFileColumn#bytes():0<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * バイト数が0の場合、falseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsCheckByte01() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Class<AbstractFileLineWriter_Stub13> clazz = AbstractFileLineWriter_Stub13.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        AbstractFileLineWriter<AbstractFileLineWriter_Stub13> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub13>(fileName, clazz, columnFormatterMap);

        // 前処理(引数)
        Field column2 = AbstractFileLineWriter_Stub13.class
                .getDeclaredFields()[1];
        OutputFileColumn outputFileColumn = column2.getAnnotation(
                OutputFileColumn.class);

        // テスト実施
        boolean result = fileLineWriter.isCheckByte(outputFileColumn);

        // 判定(戻り値)
        assertFalse(result);
    }

    /**
     * testIsCheckByte02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) outputFileColumn:not null<br>
     * (状態) outputFileColumn#bytes():1<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * バイト数が48の場合、trueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsCheckByte02() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";
        Class<AbstractFileLineWriter_Stub37> clazz = AbstractFileLineWriter_Stub37.class;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("int", new IntColumnFormatter());
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        AbstractFileLineWriter<AbstractFileLineWriter_Stub37> fileLineWriter = new AbstractFileLineWriterImpl01<AbstractFileLineWriter_Stub37>(fileName, clazz, columnFormatterMap);

        // 前処理(引数)
        Field column2 = AbstractFileLineWriter_Stub37.class
                .getDeclaredFields()[1];
        OutputFileColumn outputFileColumn = column2.getAnnotation(
                OutputFileColumn.class);

        // テスト実施
        boolean result = fileLineWriter.isCheckByte(outputFileColumn);

        // 判定(戻り値)
        assertTrue(result);
    }

    /**
     * testIsCheckByte03() <br>
     * <br>
     * (正常系) <br>
     * <br>
     * 入力値：(引数) int:-1<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * バイト数が-1の場合、falseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsCheckByte03() throws Exception {
        // 前処理(試験対象)
        String fileName = "fileName";

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        AbstractFileLineWriter<FileLineObject_Empty> fileLineWriter = new AbstractFileLineWriterImpl01<FileLineObject_Empty>(fileName, FileLineObject_Empty.class, columnFormatterMap);

        // テスト実施
        boolean result = fileLineWriter.isCheckByte(1);

        // 判定(戻り値)
        assertTrue(result);
    }
}
