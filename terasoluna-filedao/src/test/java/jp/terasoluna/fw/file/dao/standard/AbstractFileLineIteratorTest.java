/*
 * $Id:$
 *
 * Copyright (c) 2006-2015 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import jp.terasoluna.fw.file.annotation.*;
import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.dao.FileLineException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.AbstractFileLineIterator} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイルアクセス(読取)のスーパークラス。抽象クラスのため、AbstractFileLineIteratorImplクラスを作成して試験を実施する。
 * <p>
 * @author 奥田哲司
 * @author 趙俸徹
 * @see jp.terasoluna.fw.file.dao.standard.AbstractFileLineIterator
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FileDAOUtility.class, AbstractFileLineIterator.class, BufferedReader.class})
public class AbstractFileLineIteratorTest {

    /**
     * testAbstractFileLineIteratorStringClassMap01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_＜init＞01"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * @FileFormatの設定を持つ<br> - delimiter："|"(デフォルト値以外)<br>
     *                       - encloseChar："\""(デフォルト値以外)<br>
     *                       - lineFeedChar："\r"(デフォルト値以外)<br>
     *                       - fileEncoding："MS932"(デフォルト値以外)<br>
     *                       - headerLineCount：1(デフォルト値以外)<br>
     *                       - trailerLineCount：1(デフォルト値以外)<br>
     *                       (引数) columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     *                       ・"int"=IntColumnParser<br>
     *                       ・"java.lang.String"=NullColumnParser.java<br>
     *                       (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     *                       　空実装<br>
     * <br>
     *                       期待値：(状態変化) this.fileName:引数fileNameと同じインスタンス<br>
     *                       "AbstractFileLineIterator_＜init＞01"<br>
     *                       (状態変化) this.clazz:引数clazzのと同じインスタンス<br>
     *                       (状態変化) lineFeedChar:"\r"<br>
     *                       (状態変化) fileEncoding:"MS932"<br>
     *                       (状態変化) headerLineCount:1<br>
     *                       (状態変化) trailerLineCount:1<br>
     *                       (状態変化) this.columnParserMap:引数columnParserMap同じインスタンス<br>
     *                       (状態変化) 例外:ない<br>
     * <br>
     *                       正常パターン。<br>
     *                       引数に設定された情報によりAbstractFileLineIteratorクラスが初期化されて生成されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testAbstractFileLineIteratorStringClassMap01() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = "AbstractFileLineIterator_＜init＞01";
        Class<AbstractFileLineIterator_Stub01> clazz = AbstractFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // テスト実施
        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 返却値の確認
        // なし

        // 状態変化の確認
        assertEquals(fileName, ReflectionTestUtils.getField(fileLineIterator,
                "fileName"));
        assertSame(AbstractFileLineIterator_Stub01.class, ReflectionTestUtils.getField(fileLineIterator, "clazz"));
        assertEquals("\r", ReflectionTestUtils.getField(fileLineIterator,
                "lineFeedChar"));
        assertEquals("MS932", ReflectionTestUtils.getField(fileLineIterator,
                "fileEncoding"));
        assertEquals(1, ReflectionTestUtils.getField(fileLineIterator,
                "headerLineCount"));
        assertEquals(1, ReflectionTestUtils.getField(fileLineIterator,
                "trailerLineCount"));
        assertSame(columnParserMap, ReflectionTestUtils.getField(fileLineIterator,
                "columnParserMap"));
    }

    /**
     * testAbstractFileLineIteratorStringClassMap02() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:null<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * @FileFormatの設定を持つ<br> - delimiter："|"(デフォルト値以外)<br>
     *                       - encloseChar："\""(デフォルト値以外)<br>
     *                       - lineFeedChar："\r"(デフォルト値以外)<br>
     *                       - fileEncoding："MS932"(デフォルト値以外)<br>
     *                       - headerLineCount：1(デフォルト値以外)<br>
     *                       - trailerLineCount：1(デフォルト値以外)<br>
     *                       (引数) columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     *                       ・"int"=IntColumnParser<br>
     *                       ・"java.lang.String"=NullColumnParser.java<br>
     *                       (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     *                       　空実装<br>
     * <br>
     *                       期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                       ・メッセージ："fileName is required."<br>
     *                       ・原因例外：IllegalArgumentException<br>
     *                       ・ファイル名：null<br>
     * <br>
     *                       例外。<br>
     *                       ファイル名が設定されていない(null)場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testAbstractFileLineIteratorStringClassMap02() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = null;
        Class<AbstractFileLineIterator_Stub01> clazz = AbstractFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // テスト実施
        try {
            new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01>(
                    fileName, clazz, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(FileException.class, e.getClass());
            assertEquals("fileName is required.", e.getMessage());
            assertSame(IllegalArgumentException.class, e.getCause().getClass());
            assertNull(e.getFileName());
        }
    }

    /**
     * testAbstractFileLineIteratorStringClassMap03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_＜init＞03"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * @FileFormatの設定を持てない<br> (引数) columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     *                         ・"int"=IntColumnParser<br>
     *                         ・"java.lang.String"=NullColumnParser.java<br>
     *                         (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     *                         　空実装<br>
     * <br>
     *                         期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                         ・メッセージ："FileFormat annotation is not found."<br>
     *                         ・原因例外：IllegalStateException<br>
     *                         ・ファイル名：引数fileNameと同じインスタンス。<br>
     * <br>
     *                         例外。<br>
     *                         引数clazzに渡されたクラスインスタンスに、@FileFormatの設定が存在しない場合は、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineIteratorStringClassMap03() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = "AbstractFileLineIterator_＜init＞03";
        Class<AbstractFileLineIterator_Stub02> clazz = AbstractFileLineIterator_Stub02.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // テスト実施
        try {
            new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub02>(
                    fileName, clazz, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(FileException.class, e.getClass());
            assertEquals("FileFormat annotation is not found.", e.getMessage());
            assertSame(IllegalStateException.class, e.getCause().getClass());
            assertSame(fileName, e.getFileName());
        }
    }

    /**
     * testAbstractFileLineIteratorStringClassMap04() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,E <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_＜init＞04"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * @FileFormatの設定を持つ<br> - delimiter："|"(デフォルト値以外)<br>
     *                       - encloseChar："\""(デフォルト値以外)<br>
     *                       - lineFeedChar：""(空文字)<br>
     *                       - fileEncoding：""(空文字)<br>
     *                       - headerLineCount：1(デフォルト値以外)<br>
     *                       - trailerLineCount：1(デフォルト値以外)<br>
     *                       (引数) columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     *                       ・"int"=IntColumnParser<br>
     *                       ・"java.lang.String"=NullColumnParser.java<br>
     *                       (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     *                       　空実装<br>
     * <br>
     *                       期待値：(状態変化) this.fileName:引数fileNameと同じインスタンス<br>
     *                       "AbstractFileLineIterator_＜init＞04"<br>
     *                       (状態変化) this.clazz:引数clazzのと同じインスタンス<br>
     *                       (状態変化) lineFeedChar:システムの行区切り文字<br>
     *                       System.getProperty("line.separator")<br>
     *                       (状態変化) fileEncoding:システムのファイルエンコーディング<br>
     *                       System.getProperty("file.encoding")<br>
     *                       (状態変化) headerLineCount:1<br>
     *                       (状態変化) trailerLineCount:1<br>
     *                       (状態変化) this.columnParserMap:引数columnParserMap同じインスタンス<br>
     * <br>
     *                       正常パターン。<br>
     *                       引数clazzに渡されたクラスインスタンスの＠FileFormatに「lineFeedChar」と「fileEncoding」が空文字で設定されている場合、AbstractFileLineIteratorクラスのthis
     *                       .lineFeddCharとthis.fileEncodingがシステムデフォルト値で初期化されて生成されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineIteratorStringClassMap04() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = "AbstractFileLineIterator_＜init＞04";
        Class<AbstractFileLineIterator_Stub03> clazz = AbstractFileLineIterator_Stub03.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // テスト実施
        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub03> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub03>(
                fileName, clazz, columnParserMap);

        // 返却値の確認
        // なし

        // 状態変化の確認
        assertEquals(fileName, ReflectionTestUtils.getField(fileLineIterator,
                "fileName"));
        assertSame(AbstractFileLineIterator_Stub03.class, ReflectionTestUtils.getField(fileLineIterator, "clazz"));
        assertSame(System.getProperty("line.separator"), ReflectionTestUtils.getField(fileLineIterator, "lineFeedChar"));
        assertSame(System.getProperty("file.encoding"), ReflectionTestUtils.getField(
                fileLineIterator, "fileEncoding"));
        assertEquals(1, ReflectionTestUtils.getField(fileLineIterator,
                "headerLineCount"));
        assertEquals(1, ReflectionTestUtils.getField(fileLineIterator,
                "trailerLineCount"));
        assertSame(columnParserMap, ReflectionTestUtils.getField(fileLineIterator,
                "columnParserMap"));
    }

    /**
     * testAbstractFileLineIteratorStringClassMap05() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_＜init＞05"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * @FileFormatの設定を持つ<br> - delimiter：";"(encloseCharと同じ値)<br>
     *                       - encloseChar：";"(delimiterと同じ値)<br>
     *                       - lineFeedChar："\r"(デフォルト値以外)<br>
     *                       - fileEncoding："MS932"(デフォルト値以外)<br>
     *                       - headerLineCount：1(デフォルト値以外)<br>
     *                       - trailerLineCount：1(デフォルト値以外)<br>
     *                       (引数) columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     *                       ・"int"=IntColumnParser<br>
     *                       ・"java.lang.String"=NullColumnParser.java<br>
     *                       (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     *                       　空実装<br>
     * <br>
     *                       期待値：(状態変化) lineFeedChar:I8<br>
     *                       (状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                       ・メッセージ："Delimiter is the same as EncloseChar and is no use."<br>
     *                       ・原因例外：IllegalStateException<br>
     *                       ・ファイル名：引数fileNameと同じインスタンス。<br>
     * <br>
     *                       例外。<br>
     *                       引数clazzに渡されたクラスインスタンスの@FileFormaに「delimiter」と「encloseChar」が同じ場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineIteratorStringClassMap05() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = "AbstractFileLineIterator_＜init＞05";
        Class<AbstractFileLineIterator_Stub04> clazz = AbstractFileLineIterator_Stub04.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // テスト実施
        try {
            new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub04>(
                    fileName, clazz, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(FileException.class, e.getClass());
            assertEquals("Delimiter is the same as EncloseChar and is no use.",
                    e.getMessage());
            assertSame(IllegalStateException.class, e.getCause().getClass());
            assertSame(fileName, e.getFileName());
        }
    }

    /**
     * testAbstractFileLineIteratorStringClassMap06() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * ""<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * @FileFormatの設定を持つ<br> - delimiter："|"(デフォルト値以外)<br>
     *                       - encloseChar："\""(デフォルト値以外)<br>
     *                       - lineFeedChar："\r"(デフォルト値以外)<br>
     *                       - fileEncoding："MS932"(デフォルト値以外)<br>
     *                       - headerLineCount：1(デフォルト値以外)<br>
     *                       - trailerLineCount：1(デフォルト値以外)<br>
     *                       (引数) columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     *                       ・"int"=IntColumnParser<br>
     *                       ・"java.lang.String"=NullColumnParser.java<br>
     *                       (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     *                       　空実装<br>
     * <br>
     *                       期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                       ・メッセージ："fileName is required."<br>
     *                       ・原因例外：IllegalArgumentException<br>
     *                       ・ファイル名：""(空文字)<br>
     * <br>
     *                       例外。<br>
     *                       ファイル名が空文字の場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testAbstractFileLineIteratorStringClassMap06() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = "";
        Class<AbstractFileLineIterator_Stub01> clazz = AbstractFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // テスト実施
        try {
            new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01>(
                    fileName, clazz, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(FileException.class, e.getClass());
            assertEquals("fileName is required.", e.getMessage());
            assertSame(IllegalArgumentException.class, e.getCause().getClass());
            assertEquals("", e.getFileName());
        }
    }

    /**
     * testAbstractFileLineIteratorStringClassMap07() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_＜init＞07"<br>
     * (引数) clazz:null<br>
     * (引数) columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     * ・"int"=IntColumnParser<br>
     * ・"java.lang.String"=NullColumnParser.java<br>
     * (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
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
    @SuppressWarnings("unchecked")
    public void testAbstractFileLineIteratorStringClassMap07() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = "AbstractFileLineIterator_＜init＞07";
        Class<AbstractFileLineIterator_Stub01> clazz = null;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // テスト実施
        try {
            new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01>(
                    fileName, clazz, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(FileException.class, e.getClass());
            assertEquals("clazz is required.", e.getMessage());
            assertSame(IllegalArgumentException.class, e.getCause().getClass());
            assertSame(fileName, e.getFileName());
        }
    }

    /**
     * testAbstractFileLineIteratorStringClassMap08() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_＜init＞08"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * @FileFormatの設定を持つ<br> - delimiter："|"(デフォルト値以外)<br>
     *                       - encloseChar："\""(デフォルト値以外)<br>
     *                       - lineFeedChar："\r"(デフォルト値以外)<br>
     *                       - fileEncoding："MS932"(デフォルト値以外)<br>
     *                       - headerLineCount：1(デフォルト値以外)<br>
     *                       - trailerLineCount：1(デフォルト値以外)<br>
     *                       (引数) columnParserMap:null<br>
     *                       (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     *                       　空実装<br>
     * <br>
     *                       期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                       ・メッセージ："columnFormaterMap is required."<br>
     *                       ・原因例外：IllegalArgumentException<br>
     *                       ・ファイル名：引数fileNameと同じインスタンス。<br>
     * <br>
     *                       例外。<br>
     *                       引数columnParserMapが「null」の場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testAbstractFileLineIteratorStringClassMap08() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = "AbstractFileLineIterator_＜init＞08";
        Class<AbstractFileLineIterator_Stub01> clazz = AbstractFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = null;

        // テスト実施
        try {
            new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01>(
                    fileName, clazz, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(FileException.class, e.getClass());
            assertEquals("columnFormaterMap is required.", e.getMessage());
            assertSame(IllegalArgumentException.class, e.getCause().getClass());
            assertSame(fileName, e.getFileName());
        }
    }

    /**
     * testAbstractFileLineIteratorStringClassMap09() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_＜init＞11"<br>
     * (引数) clazz:以下の設定を持つ抽象Classインスタンス<br>
     * @FileFormatの設定を持つ<br> - delimiter："|"(デフォルト値以外)<br>
     *                       - encloseChar："\""(デフォルト値以外)<br>
     *                       - lineFeedChar：""(空文字)<br>
     *                       - fileEncoding：""(空文字)<br>
     *                       - headerLineCount：1(デフォルト値以外)<br>
     *                       - trailerLineCount：1(デフォルト値以外)<br>
     * <br>
     *                       ※抽象クラスである。<br>
     *                       (引数) columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     *                       ・"int"=IntColumnParser<br>
     *                       ・"java.lang.String"=NullColumnParser.java<br>
     *                       (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     *                       　空実装<br>
     * <br>
     *                       期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                       ・メッセージ："Failed in instantiation of clazz."<br>
     *                       ・原因例外：InstantiationException<br>
     *                       ・ファイル名：引数fileNameと同じインスタンス。<br>
     * <br>
     *                       例外。<br>
     *                       インスタンス化できないClassが引数Clazzに設定された場合に例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineIteratorStringClassMap09() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = "AbstractFileLineIterator_＜init＞11";
        Class<AbstractFileLineIterator_Stub05> clazz = AbstractFileLineIterator_Stub05.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // テスト実施
        try {
            new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub05>(
                    fileName, clazz, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(FileException.class, e.getClass());
            assertEquals("Failed in instantiation of clazz.", e.getMessage());
            assertSame(InstantiationException.class, e.getCause().getClass());
            assertSame(fileName, e.getFileName());
        }
    }

    /**
     * testAbstractFileLineIteratorStringClassMap10() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_＜init＞12"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * @FileFormatの設定を持つ<br> - delimiter："|"(デフォルト値以外)<br>
     *                       - encloseChar："\""(デフォルト値以外)<br>
     *                       - lineFeedChar：""(空文字)<br>
     *                       - fileEncoding：""(空文字)<br>
     *                       - headerLineCount：1(デフォルト値以外)<br>
     *                       - trailerLineCount：1(デフォルト値以外)<br>
     * <br>
     *                       ※デフォルトコンストラクタが「private」で宣言されている。<br>
     *                       (引数) columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     *                       ・"int"=IntColumnParser<br>
     *                       ・"java.lang.String"=NullColumnParser.java<br>
     *                       (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     *                       　空実装<br>
     * <br>
     *                       期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                       ・メッセージ："clazz's nullary  constructor is not accessible"<br>
     *                       ・原因例外：IllegalAccessException<br>
     *                       ・ファイル名：引数fileNameと同じインスタンス。<br>
     * <br>
     *                       例外。<br>
     *                       デフォルトコンストラクタの直接アクセスが出来ないClassが引数Clazzに設定された場合に例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testAbstractFileLineIteratorStringClassMap10() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = "AbstractFileLineIterator_＜init＞12";
        Class<AbstractFileLineIterator_Stub06> clazz = AbstractFileLineIterator_Stub06.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // テスト実施
        try {
            new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub06>(
                    fileName, clazz, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(FileException.class, e.getClass());
            assertEquals("clazz's nullary  constructor is not accessible", e
                    .getMessage());
            assertSame(IllegalAccessException.class, e.getCause().getClass());
            assertSame(fileName, e.getFileName());
        }
    }

    /**
     * testAbstractFileLineIteratorStringClassMap11() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(引数) fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_＜init＞13"<br>
     * (引数) clazz:以下の設定を持つClassインスタンス<br>
     * @FileFormatの設定を持つ<br> - delimiter："|"(デフォルト値以外)<br>
     *                       - encloseChar："\""(デフォルト値以外)<br>
     *                       - lineFeedChar："\r"(デフォルト値以外)<br>
     *                       - fileEncoding："MS932"(デフォルト値以外)<br>
     *                       - headerLineCount：1(デフォルト値以外)<br>
     *                       - trailerLineCount：1(デフォルト値以外)<br>
     *                       (引数) columnParserMap:要素を持たないMap<String, ColumnParser>インスタンス<br>
     *                       (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     *                       　空実装<br>
     * <br>
     *                       期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                       ・メッセージ："columnFormaterMap is required."<br>
     *                       ・原因例外：IllegalArgumentException<br>
     *                       ・ファイル名：引数fileNameと同じインスタンス。<br>
     * <br>
     *                       例外。<br>
     *                       引数columnParserMapはあるが、そのMapに要素が無い場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testAbstractFileLineIteratorStringClassMap11() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = "AbstractFileLineIterator_＜init＞13";
        Class<AbstractFileLineIterator_Stub01> clazz = AbstractFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();

        // テスト実施
        try {
            new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01>(
                    fileName, clazz, columnParserMap);
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(FileException.class, e.getClass());
            assertEquals("columnFormaterMap is required.", e.getMessage());
            assertSame(IllegalArgumentException.class, e.getCause().getClass());
            assertSame(fileName, e.getFileName());
        }
    }

    /**
     * testHasNext01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.reader:not null<br>
     * Readerインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_bulidFields01.txt"<br>
     * (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_hasNext01.txt"ファイルが存在する。<br>
     * -------------------<br>
     * 1行目<br>
     * -------------------<br>
     * ※特殊データ<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * (状態変化) reader.ready():1回呼ばれる<br>
     * <br>
     * 正常パターン<br>
     * フィールドreaderから次の行のレコードの取得が可能な場合はtrueが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNext01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_hasNext01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub07> fileLineIterator = new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // なし

        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.doNothing().when(mockReader).mark(1);
        Mockito.doReturn(0).when(mockReader).read();
        ReflectionTestUtils.setField(fileLineIterator, "reader", mockReader);

        // テスト実施
        boolean result = fileLineIterator.hasNext();

        // 返却値の確認
        assertTrue(result);

        // 状態変化の確認
        Mockito.verify(mockReader).read();
    }

    /**
     * testHasNext02() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.reader:not null<br>
     * Readerインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_hasNext02.txt"<br>
     * (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_hasNext02.txt"ファイルが存在する。<br>
     * -------------------<br>
     * 空<br>
     * -------------------<br>
     * ※特殊データ<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * (状態変化) reader.ready():1回呼ばれる<br>
     * <br>
     * 正常パターン<br>
     * フィールドreaderから次の行のレコードの取得が不可能な場合はfalseが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNext02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_hasNext02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub07> fileLineIterator = new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // なし

        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.doNothing().when(mockReader).mark(1);
        Mockito.doReturn(-1).when(mockReader).read();
        Mockito.doNothing().when(mockReader).reset();
        ReflectionTestUtils.setField(fileLineIterator, "reader", mockReader);

        // テスト実施
        boolean result = fileLineIterator.hasNext();

        // 返却値の確認
        assertFalse(result);

        // 状態変化の確認
        Mockito.verify(mockReader).read();
    }

    /**
     * testHasNext03() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.reader:not null<br>
     * Readerインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_hasNext02.txt"<br>
     * (状態) reader.ready():IOExceptionを発生する。<br>
     * <br>
     * 期待値：(状態変化) reader.ready():1回呼ばれる<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："Processing of reader was failed."<br>
     * ・原因例外：IOException<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 結果クラスが設定されていない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNext03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_hasNext02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub07> fileLineIterator = new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap);

        // 引数の設定
        // なし


        // 前提条件の設定
        IOException exception = new IOException();
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.doNothing().when(mockReader).mark(1);
        Mockito.doThrow(exception).when(mockReader).read();
        ReflectionTestUtils.setField(fileLineIterator, "reader", mockReader);

        // テスト実施
        try {
            fileLineIterator.hasNext();
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            Mockito.verify(mockReader).read();

            assertSame(FileException.class, e.getClass());
            assertEquals("Processing of reader was failed.", e.getMessage());
            assertSame(exception, e.getCause());
            assertEquals(fileName, e.getFileName());
        }
    }

    /**
     * testHasNext04() <br>
     * <br>
     * (異常系) <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.reader:not null<br>
     * Readerインスタンス<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * <br>
     * (状態) reader.ready():IOExceptionを発生する。<br>
     * <br>
     * 期待値：<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："Processing of reader#reset was failed."<br>
     * ・原因例外：IOException<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 結果クラスが設定されていない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testHasNext04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub07> fileLineIterator = new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        IOException exception = new IOException();
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.doNothing().when(mockReader).mark(1);
        Mockito.doReturn(0).when(mockReader).read();
        Mockito.doThrow(exception).when(mockReader).reset();
        ReflectionTestUtils.setField(fileLineIterator, "reader", mockReader);

        // テスト実施
        try {
            fileLineIterator.hasNext();
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認

            assertSame(FileException.class, e.getClass());
            assertEquals("Processing of reader#reset was failed.", e
                    .getMessage());
            assertSame(exception, e.getCause());
            assertEquals(fileName, e.getFileName());
        }
    }

    /**
     * testNext01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:インタフェースなどのインスタンス化できないClassのインスタンス<br>
     * ※インスタンス生成後に設定する。<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_next06.txt"<br>
     * (状態) this.currentLineCount:0<br>
     * (状態) this.readTrailer:false<br>
     * (状態) this.isCheckByte():true<br>
     * <br>
     * 期待値：(状態変化) this.hasNext():2回呼ばれる<br>
     * ※readLine()の中でhasNextが１回呼ばれている。<br>
     * (状態変化) this.readLine():1回呼ばれる<br>
     * (状態変化) this.separateColumns(String):呼ばれない<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："Failed in an instantiate of a FileLineObject."<br>
     * ・原因例外：InstantiationException<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 例外。<br>
     * フィールドclazzにインスタンス化できないクラスが設定された場合、FileExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next06.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub07> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub07>(
                    fileName, AbstractFileLineIterator_Stub07.class, columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "clazz",
                AbstractFileLineIterator_Stub14.class);
        // テスト対象のインスタンス化時に設定している。

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
            Mockito.verify(fileLineIterator).readLine();
            Mockito.verify(fileLineIterator, Mockito.never()).separateColumns(Mockito.anyString());

            assertSame(FileException.class, e.getClass());
            assertEquals("Failed in an instantiate of a FileLineObject.", e
                    .getMessage());
            assertSame(InstantiationException.class, e.getCause().getClass());
            assertSame(fileName, e.getFileName());
        }
    }

    /**
     * testNext02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:デフォルトコンストラクタの直接実行が不可能(private宣言)なClassのインスタンス<br>
     * ※インスタンス生成後に設定する。<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_next06.txt"<br>
     * (状態) this.currentLineCount:0<br>
     * (状態) this.readTrailer:false<br>
     * (状態) this.isCheckByte():true<br>
     * <br>
     * 期待値：(状態変化) this.hasNext():2回呼ばれる<br>
     * ※readLine()の中でhasNextが１回呼ばれている。<br>
     * (状態変化) this.readLine():1回呼ばれる<br>
     * (状態変化) this.separateColumns(String):呼ばれない<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："Failed in an instantiate of a FileLineObject."<br>
     * ・原因例外：IllegalAccessException<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 例外。<br>
     * フィールドclazzのコンストラクタが直接実行できないように設定されている場合、FileExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next06.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("int", new IntColumnParser());
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub07> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "clazz",
                AbstractFileLineIterator_Stub15.class);
        // テスト対象のインスタンス化時に設定している。

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
            Mockito.verify(fileLineIterator).readLine();
            Mockito.verify(fileLineIterator, Mockito.never()).separateColumns(Mockito.anyString());

            assertSame(FileException.class, e.getClass());
            assertEquals("Failed in an instantiate of a FileLineObject.", e
                    .getMessage());
            assertSame(IllegalAccessException.class, e.getCause().getClass());
            assertSame(fileName, e.getFileName());
        }
    }

    /**
     * testNext03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・フィールドおよび、@InputFileColumnの設定はない<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_next06.txt"<br>
     * (状態) this.currentLineCount:0<br>
     * (状態) this.readTrailer:false<br>
     * (状態) this.isCheckByte():true<br>
     * (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next06.txt"ファイルが存在する。<br>
     * -------------------<br>
     * 空行<br>
     * 空行<br>
     * 空行<br>
     * -------------------<br>
     * ※特殊データ<br>
     * <br>
     * 期待値：(戻り値) this.clazz.getClass():this.clazzで設定されているクラスのインスタンス<br>
     * (状態変化) FileDAOUtility.trim(String, String, char, TrimType):呼ばれない<br>
     * (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):呼ばれない<br>
     * (状態変化) NullColumnParser#parse():呼ばれない<br>
     * (状態変化) DateColumnParser#parse():呼ばれない<br>
     * (状態変化) DecimalColumnParser#parse():呼ばれない<br>
     * (状態変化) IntColumnParser#parse():呼ばれない<br>
     * (状態変化) NullStringConverter#convert():呼ばれない<br>
     * (状態変化) this.hasNext():2回呼ばれる<br>
     * ※readLine()の中でhasNextが１回呼ばれている。<br>
     * (状態変化) this.readLine():1回呼ばれる<br>
     * (状態変化) this.separateColumns(String):1回呼ばれる<br>
     * (状態変化) this.currentLineCount:1<br>
     * <br>
     * ファイルから読み取ったカラムが空文字で、ファイル行オブジェクトclazzにフィールドがない場合は、ファイル行オブジェクトが取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next06.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        columnParserMap.put("int", intColumnParser);
        columnParserMap.put("java.lang.String", nullColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub08> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub08>(
                fileName, AbstractFileLineIterator_Stub08.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施
        Object result = fileLineIterator.next();

        // 返却値の確認
        assertSame(AbstractFileLineIterator_Stub08.class, result.getClass());

        // 状態変化の確認
        PowerMockito.verifyStatic(Mockito.never());
        FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(), Mockito.any(TrimType.class));
        PowerMockito.verifyStatic(Mockito.never());
        FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyChar(), Mockito.any(PaddingType.class));
        Mockito.verify(nullColumnParser, Mockito.never()).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(intColumnParser, Mockito.never()).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
        Mockito.verify(fileLineIterator).readLine();
        Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());
        assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                fileLineIterator, "currentLineCount"));
    }

    /**
     * testNext04() <br>
     * <br>
     * (異常系) <br>
     * 観点：F,G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定がないフィールドを持つ<br>
     * - フィールド：String column1<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_bulidFields01.txt"<br>
     * (状態) this.currentLineCount:0<br>
     * (状態) this.readTrailer:false<br>
     * (状態) this.isCheckByte():true<br>
     * (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_bulidFields01.txt"ファイルが存在する。<br>
     * -------------------<br>
     * test<br>
     * -------------------<br>
     * ※特殊データ<br>
     * <br>
     * 期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):呼ばれない<br>
     * (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):呼ばれない<br>
     * (状態変化) NullColumnParser#parse():呼ばれない<br>
     * (状態変化) DateColumnParser#parse():呼ばれない<br>
     * (状態変化) DecimalColumnParser#parse():呼ばれない<br>
     * (状態変化) IntColumnParser#parse():呼ばれない<br>
     * (状態変化) NullStringConverter#convert():呼ばれない<br>
     * (状態変化) this.hasNext():2回呼ばれる<br>
     * ※readLine()の中でhasNextが１回呼ばれている。<br>
     * (状態変化) this.readLine():1回呼ばれる<br>
     * (状態変化) this.separateColumns(String):1回呼ばれる<br>
     * (状態変化) this.currentLineCount:0<br>
     * (状態変化) 例外:以下の情報を持つFileLineExceptionが発生することを確認する。<br>
     * ・メッセージ："Column Count is different from FileLineObject's column counts"<br>
     * ・原因例外：IllegalStateException<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * ・行番号：1<br>
     * ・カラム名：null<br>
     * ・カラムインデックス：-1<br>
     * <br>
     * ファイルから読み取ったカラム数とファイル行オブジェクトのカラム数が合わない場合に、FileLineExceptionが発生することを確認する。<br>
     * ここでは、ファイルから読み取ったカラムが1つ存在し、ファイル行オブジェクトclazzに@InputFileColumn定義がないフィールドのみある場合を試験している。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next08.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        columnParserMap.put("int", intColumnParser);
        columnParserMap.put("java.lang.String", nullColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub08> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub08>(
                fileName, AbstractFileLineIterator_Stub08.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。
        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileLineExceptionがスローされませんでした");
        } catch (FileLineException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic(Mockito.never());
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                    Mockito.any(TrimType.class));
            PowerMockito.verifyStatic(Mockito.never());
            FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(),
                    Mockito.anyInt(), Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(nullColumnParser, Mockito.never()).parse(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(intColumnParser, Mockito.never()).parse(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
            Mockito.verify(fileLineIterator).readLine();
            Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

            assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                    fileLineIterator, "currentLineCount"));

            assertSame(FileLineException.class, e.getClass());
            assertEquals(
                    "Column Count is different from FileLineObject's column counts",
                    e.getMessage());
            assertSame(IllegalStateException.class, e.getCause().getClass());
            assertSame(fileName, e.getFileName());
            assertEquals("getLineNo", 1, e.getLineNo());
            assertNull(e.getColumnName());
            assertEquals(-1, e.getColumnIndex());
        }
    }

    /**
     * testNext05() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar："\""<br>
     * - その他項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：Date column4<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > columnFormat：yyyy/MM/dd<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：BigDecimal column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > columnFormat：###,###<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next09.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next09.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,line1,111111,1980/01/21<br>
     *                        -------------------<br>
     *                        ※正常データ<br>
     * <br>
     *                        期待値：(戻り値) this.clazz.getClass():this.clazzで設定されているクラスのインスタンス<br>
     *                        - column1：1<br>
     *                        - column2："line1"<br>
     *                        - column3：111111<br>
     *                        - column4：1980/01/21<br>
     *                        (状態変化) FileDAOUtility.trim(String, String, char, TrimType):4回呼ばれる<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):4回呼ばれる<br>
     *                        (状態変化) NullColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) DateColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) DecimalColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) IntColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) NullStringConverter#convert():1回呼ばれる<br>
     *                        (状態変化) this.hasNext():2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回呼ばれる<br>
     *                        (状態変化) this.currentLineCount:1<br>
     * <br>
     *                        正常。(バイト数チェックする)<br>
     *                        対象ファイルの内容が正しく設定されたファイル行オブジェクトが取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext05() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next09.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09>(
                fileName, AbstractFileLineIterator_Stub09.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施
        AbstractFileLineIterator_Stub09 result = fileLineIterator.next();

        // 返却値の確認
        assertEquals("getColumn1", 1, result.getColumn1());
        assertEquals("line1", result.getColumn2());
        assertEquals(new BigDecimal(111111), result.getColumn3());
        Calendar column4 = new GregorianCalendar();
        column4.set(1980, 0, 21, 0, 0, 0);
        assertEquals(column4.getTime().toString(), result.getColumn4()
                .toString());

        // 状態変化の確認
        PowerMockito.verifyStatic(Mockito.times(4));
        FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(), Mockito.any(TrimType.class));
        PowerMockito.verifyStatic(Mockito.times(4));
        FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyInt(), Mockito.anyChar(), Mockito.any(PaddingType.class));
        Mockito.verify(nullColumnParser).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(dateColumnParser).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(decimalColumnParser).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(intColumnParser).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
        Mockito.verify(fileLineIterator).readLine();
        Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

        assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                fileLineIterator, "currentLineCount"));
    }

    /**
     * testNext06() <br>
     * <br>
     * (異常系) <br>
     * 観点：E,G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > bytes：5<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next12.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next12.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        ABCDE,1234<br>
     *                        -------------------<br>
     *                        ※桁数異常データ<br>
     * <br>
     *                        期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):1回呼ばれる<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):1回呼ばれる<br>
     *                        (状態変化) NullColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) DateColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DecimalColumnParser#parse():呼ばれない<br>
     *                        (状態変化) IntColumnParser#parse():呼ばれない<br>
     *                        (状態変化) NullStringConverter#convert():1回呼ばれる<br>
     *                        (状態変化) this.hasNext():2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回呼ばれる<br>
     *                        (状態変化) this.currentLineCount:0<br>
     *                        (状態変化) 例外:以下の情報を持つFileLineExceptionが発生することを確認する。<br>
     *                        ・メッセージ："Data size is different from a set point of a column."<br>
     *                        ・原因例外：IllegalStateException<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     *                        ・行番号：1<br>
     *                        ・カラム名：column2<br>
     *                        ・カラムインデックス：1<br>
     * <br>
     *                        例外。<br>
     *                        フィールドclazzに@InputFileColumnにbytes定義があり入力されたファイルの情報がbytes設定にあってない場合、FileLineExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext06() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next12.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub10> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub10>(
                fileName, AbstractFileLineIterator_Stub10.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileLineExceptionがスローされませんでした");
        } catch (FileLineException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic();
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(), Mockito.any(TrimType.class));
            PowerMockito.verifyStatic();
            FileDAOUtility.padding(
                    Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                    Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(nullColumnParser).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(dateColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(decimalColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(intColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
            Mockito.verify(fileLineIterator).readLine();
            Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

            assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                    fileLineIterator, "currentLineCount"));

            assertSame(FileLineException.class, e.getClass());
            assertEquals(
                    "Data size is different from a set point of a column.", e
                            .getMessage());
            assertSame(IllegalStateException.class, e.getCause().getClass());
            assertSame(fileName, e.getFileName());
            assertEquals("getLineNo", 1, e.getLineNo());
            assertEquals("column2", e.getColumnName());
            assertEquals("getColumnIndex", 1, e.getColumnIndex());
        }
    }

    /**
     * testNext07() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > bytes：5<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next13.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next13.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        ABCDE,12345<br>
     *                        -------------------<br>
     *                        ※正常データ<br>
     * <br>
     *                        期待値：(戻り値) this.clazz.getClass():this.clazzで設定されているクラスのインスタンス<br>
     *                        - column1："ABCDE"<br>
     *                        - column2："12345"<br>
     *                        (状態変化) FileDAOUtility.trim(String, String, char, TrimType):2回呼ばれる<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):2回呼ばれる<br>
     *                        (状態変化) NullColumnParser#parse():2回呼ばれる<br>
     *                        (状態変化) DateColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DecimalColumnParser#parse():呼ばれない<br>
     *                        (状態変化) IntColumnParser#parse():呼ばれない<br>
     *                        (状態変化) NullStringConverter#convert():2回呼ばれる<br>
     *                        (状態変化) this.hasNext():2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回呼ばれる<br>
     *                        (状態変化) this.currentLineCount:1<br>
     * <br>
     *                        正常。<br>
     *                        フィールドclazzに@InputFileColumnにbytes定義があり入力されたファイルの情報がbytes設定にあっている場合、対象ファイルの内容が正しく設定されたファイル行オブジェクトが取得されることを確認する
     *                        。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext07() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next13.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub10> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub10>(
                fileName, AbstractFileLineIterator_Stub10.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施
        AbstractFileLineIterator_Stub10 result = fileLineIterator.next();

        // 返却値の確認
        assertEquals("ABCDE", result.getColumn1());
        assertEquals("12345", result.getColumn2());

        // 状態変化の確認
        PowerMockito.verifyStatic(Mockito.times(2));
        FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(), Mockito.any(TrimType.class));
        PowerMockito.verifyStatic(Mockito.times(2));
        FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyInt(), Mockito.anyChar(), Mockito.any(PaddingType.class));
        Mockito.verify(nullColumnParser, Mockito.times(2)).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(nullColumnParser, Mockito.times(2)).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(dateColumnParser, Mockito.never()).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(decimalColumnParser, Mockito.never()).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(intColumnParser, Mockito.never()).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
        Mockito.verify(fileLineIterator).readLine();
        Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

        assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                fileLineIterator, "currentLineCount"));
    }

    /**
     * testNext08() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next15.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=ColumnParserスタブ(実行すると例外を投げる。)<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) ColumnParser.parse():IllegalArgumentException例外が発生<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next15.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,ABCDE<br>
     *                        -------------------<br>
     *                        ※正常データ<br>
     * <br>
     *                        期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):2回呼ばれる<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):2回呼ばれる<br>
     *                        (状態変化) IntColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) NullStringConverter#convert():2回呼ばれる<br>
     *                        (状態変化) this.hasNext():2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回呼ばれる<br>
     *                        (状態変化) this.currentLineCount:0<br>
     *                        (状態変化) 例外:以下の情報を持つFileLineExceptionが発生することを確認する。<br>
     *                        ・メッセージ："Failed in coluomn data parsing."<br>
     *                        ・原因例外：llegalArgumentException<br>
     *                        (ColumnParser.parse()で発生した例外)<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     *                        ・行番号：1<br>
     *                        ・カラム名：column2<br>
     *                        ・カラムインデックス：1<br>
     * <br>
     *                        例外。<br>
     *                        対象ファイルから取得したデータをフィールドの型にあわせてパーズする処理でllegalArgumentExceptionが発生した場合、FileLineExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext08() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next15.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String",
                new AbstractFileLineIterator_ColumnParserStub01());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub11> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub11>(
                fileName, AbstractFileLineIterator_Stub11.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileLineExceptionがスローされませんでした");
        } catch (FileLineException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic(Mockito.times(2));
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(), Mockito.any(TrimType.class));
            PowerMockito.verifyStatic(Mockito.times(2));
            FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                    Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(intColumnParser).parse(Mockito.anyString(), Mockito.anyObject(),
                    Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
            Mockito.verify(fileLineIterator).readLine();
            Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

            assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                    fileLineIterator, "currentLineCount"));

            assertSame(FileLineException.class, e.getClass());
            assertEquals("Failed in coluomn data parsing.", e.getMessage());
            assertSame(IllegalArgumentException.class, e.getCause().getClass());
            assertEquals(fileName, e.getFileName());
            assertEquals("getLineNo", 1, e.getLineNo());
            assertEquals("column2", e.getColumnName());
            assertEquals("getColumnIndex", 1, e.getColumnIndex());
        }
    }

    /**
     * testNext09() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next15.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=ColumnParserスタブ(実行すると例外を投げる。)<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) ColumnParser.parse():IllegalAccessException例外が発生<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next15.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,ABCDE<br>
     *                        -------------------<br>
     *                        ※正常データ<br>
     * <br>
     *                        期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):2回呼ばれる<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):2回呼ばれる<br>
     *                        (状態変化) IntColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) NullStringConverter#convert():2回呼ばれる<br>
     *                        (状態変化) this.hasNext():2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回呼ばれる<br>
     *                        (状態変化) this.currentLineCount:0<br>
     *                        (状態変化) 例外:以下の情報を持つFileLineExceptionが発生することを確認する。<br>
     *                        ・メッセージ："Failed in coluomn data parsing."<br>
     *                        ・原因例外：IllegalAccessException<br>
     *                        (ColumnParser.parse()で発生した例外)<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     *                        ・行番号：1<br>
     *                        ・カラム名：column2<br>
     *                        ・カラムインデックス：1<br>
     * <br>
     *                        例外。<br>
     *                        対象ファイルから取得したデータをフィールドの型にあわせてパーズする処理でIllegalAccessExceptionが発生した場合、FileLineExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext09() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next15.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String",
                new AbstractFileLineIterator_ColumnParserStub02());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub11> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub11>(
                fileName, AbstractFileLineIterator_Stub11.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileLineExceptionがスローされませんでした");
        } catch (FileLineException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic(Mockito.times(2));
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                    Mockito.any(TrimType.class));
            PowerMockito.verifyStatic(Mockito.times(2));
            FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                    Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(intColumnParser).parse(Mockito.anyString(), Mockito.anyObject(),
                    Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
            Mockito.verify(fileLineIterator).readLine();
            Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

            assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                    fileLineIterator, "currentLineCount"));

            assertSame(FileLineException.class, e.getClass());
            assertEquals("Failed in coluomn data parsing.", e.getMessage());
            assertSame(IllegalAccessException.class, e.getCause().getClass());
            assertEquals(fileName, e.getFileName());
            assertEquals("getLineNo", 1, e.getLineNo());
            assertEquals("column2", e.getColumnName());
            assertEquals("getColumnIndex", 1, e.getColumnIndex());
        }
    }

    /**
     * testNext10() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next15.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=ColumnParserスタブ(実行すると例外を投げる。)<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) ColumnParser.parse():InvocationTargetException例外が発生<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next15.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,ABCDE<br>
     *                        -------------------<br>
     *                        ※正常データ<br>
     * <br>
     *                        期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):2回呼ばれる<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):2回呼ばれる<br>
     *                        (状態変化) IntColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) NullStringConverter#convert():2回呼ばれる<br>
     *                        (状態変化) this.hasNext():2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回呼ばれる<br>
     *                        (状態変化) this.currentLineCount:0<br>
     *                        (状態変化) 例外:以下の情報を持つFileLineExceptionが発生することを確認する。<br>
     *                        ・メッセージ："Failed in coluomn data parsing."<br>
     *                        ・原因例外：InvocationTargetException<br>
     *                        (ColumnParser.parse()で発生した例外)<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     *                        ・行番号：1<br>
     *                        ・カラム名：column2<br>
     *                        ・カラムインデックス：1<br>
     * <br>
     *                        例外。<br>
     *                        対象ファイルから取得したデータをフィールドの型にあわせてパーズする処理でInvocationTargetExceptionが発生した場合、FileLineExceptionが発生することを確認する。
     * <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext10() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next15.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String",
                new AbstractFileLineIterator_ColumnParserStub03());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub11> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub11>(
                fileName, AbstractFileLineIterator_Stub11.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileLineExceptionがスローされませんでした");
        } catch (FileLineException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic(Mockito.times(2));
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                    Mockito.any(TrimType.class));
            PowerMockito.verifyStatic(Mockito.times(2));
            FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                    Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(intColumnParser).parse(Mockito.anyString(), Mockito.anyObject(),
                    Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
            Mockito.verify(fileLineIterator).readLine();
            Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

            assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                    fileLineIterator, "currentLineCount"));

            assertSame(FileLineException.class, e.getClass());
            assertEquals("Failed in coluomn data parsing.", e.getMessage());
            assertSame(InvocationTargetException.class, e.getCause().getClass());
            assertEquals(fileName, e.getFileName());
            assertEquals("getLineNo", 1, e.getLineNo());
            assertEquals("column2", e.getColumnName());
            assertEquals("getColumnIndex", 1, e.getColumnIndex());
        }
    }

    /**
     * testNext11() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next15.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=ColumnParserスタブ(実行すると例外を投げる。)<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) ColumnParser.parse():ParsrException例外発生<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next15.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,ABCDE<br>
     *                        -------------------<br>
     *                        ※正常データ<br>
     * <br>
     *                        期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):2回呼ばれる<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):2回呼ばれる<br>
     *                        (状態変化) IntColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) NullStringConverter#convert():2回呼ばれる<br>
     *                        (状態変化) this.hasNext():2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回呼ばれる<br>
     *                        (状態変化) this.currentLineCount:0<br>
     *                        (状態変化) 例外:以下の情報を持つFileLineExceptionが発生することを確認する。<br>
     *                        ・メッセージ："Failed in coluomn data parsing."<br>
     *                        ・原因例外：ParsrException<br>
     *                        (ColumnParser.parse()で発生した例外)<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     *                        ・行番号：1<br>
     *                        ・カラム名：column2<br>
     *                        ・カラムインデックス：1<br>
     * <br>
     *                        例外。<br>
     *                        対象ファイルから取得したデータをフィールドの型にあわせてパーズする処理でParsrExceptionが発生した場合、FileLineExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext11() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next15.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String",
                new AbstractFileLineIterator_ColumnParserStub04());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub11> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub11>(
                fileName, AbstractFileLineIterator_Stub11.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileLineExceptionがスローされませんでした");
        } catch (FileLineException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic(Mockito.times(2));
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(),
                    Mockito.anyChar(), Mockito.any(TrimType.class));

            PowerMockito.verifyStatic(Mockito.times(2));
            FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(),
                    Mockito.anyInt(), Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(intColumnParser).parse(Mockito.anyString(), Mockito.anyObject(),
                    Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
            Mockito.verify(fileLineIterator).readLine();
            Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

            assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                    fileLineIterator, "currentLineCount"));

            assertSame(FileLineException.class, e.getClass());
            assertEquals("Failed in coluomn data parsing.", e.getMessage());
            assertSame(ParseException.class, e.getCause().getClass());
            assertEquals(fileName, e.getFileName());
            assertEquals("getLineNo", 1, e.getLineNo());
            assertEquals("column2", e.getColumnName());
            assertEquals("getColumnIndex", 1, e.getColumnIndex());
        }
    }

    /**
     * testNext12() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar："\""<br>
     * - その他項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：Date column4<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > columnFormat：yyyy/MM/dd<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：BigDecimal column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > columnFormat：###,###<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next19.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next19.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,line1,111111,1980/01/21<br>
     *                        2,line2,222222,1980/02/21<br>
     *                        3,line3,333333,1980/03/21<br>
     *                        -------------------<br>
     *                        ※複数正常データ<br>
     * <br>
     *                        期待値：(戻り値) this.clazz.getClass():this.clazzで設定されているクラスのインスタンス<br>
     *                        ・1回目の実行結果<br>
     *                        - column1：1<br>
     *                        - column2："line1"<br>
     *                        - column3：111111<br>
     *                        - column4：1980/01/21<br>
     * <br>
     *                        ・2回目の実行結果<br>
     *                        - column1：2<br>
     *                        - column2："line2"<br>
     *                        - column3：222222<br>
     *                        - column4：1980/02/21<br>
     * <br>
     *                        ・3回目の実行結果<br>
     *                        - column1：3<br>
     *                        - column2："line3"<br>
     *                        - column3：333333<br>
     *                        - column4：1980/03/21<br>
     *                        (状態変化) FileDAOUtility.trim(String, String, char, TrimType):1回の実行毎に4回呼ばれる<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):1回の実行毎に4回呼ばれる<br>
     *                        (状態変化) NullColumnParser#parse():1回の実行毎に1回呼ばれる<br>
     *                        (状態変化) DateColumnParser#parse():1回の実行毎に1回呼ばれる<br>
     *                        (状態変化) DecimalColumnParser#parse():1回の実行毎に1回呼ばれる<br>
     *                        (状態変化) IntColumnParser#parse():1回の実行毎に1回呼ばれる<br>
     *                        (状態変化) NullStringConverter#convert():1回の実行毎に4回呼ばれる<br>
     *                        (状態変化) this.hasNext():1回の実行毎に2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回の実行毎に1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回の実行毎に1回呼ばれる<br>
     *                        (状態変化) this.currentLineCount:・1回目実行後：1<br>
     *                        ・2回目実行後：2<br>
     *                        ・3回目実行後：3<br>
     * <br>
     *                        正常。<br>
     *                        対象ファイルにある複数行の情報が各1行に対してnext()メソッドを呼ぶ度に正しく設定されたファイル行オブジェクトが取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext12() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next19.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09>(
                fileName, AbstractFileLineIterator_Stub09.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施（1回目）
        AbstractFileLineIterator_Stub09 result = fileLineIterator.next();

        // 返却値の確認
        assertEquals("getColumn1", 1, result.getColumn1());
        assertEquals("line1", result.getColumn2());
        assertEquals(new BigDecimal(111111), result.getColumn3());
        Calendar column4 = new GregorianCalendar();
        column4.set(1980, 0, 21, 0, 0, 0);
        assertEquals(column4.getTime().toString(), result.getColumn4()
                .toString());

        // 状態変化の確認
        PowerMockito.verifyStatic(Mockito.times(4));
        FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(), Mockito.any(TrimType.class));
        PowerMockito.verifyStatic(Mockito.times(4));
        FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyChar(), Mockito.any(PaddingType.class));
        Mockito.verify(nullColumnParser).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(dateColumnParser).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(decimalColumnParser).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(intColumnParser).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
        Mockito.verify(fileLineIterator).readLine();
        Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

        assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                fileLineIterator, "currentLineCount"));

        // テスト実施（2回目）
        AbstractFileLineIterator_Stub09 result02 = fileLineIterator.next();

        // 返却値の確認
        assertEquals("getColumn1", 2, result02.getColumn1());
        assertEquals("line2", result02.getColumn2());
        assertEquals(new BigDecimal(222222), result02.getColumn3());
        column4.set(1980, 1, 21, 0, 0, 0);
        assertEquals(column4.getTime().toString(), result02.getColumn4()
                .toString());

        // 状態変化の確認（呼び出し回数は1回目＋今回になる）
        PowerMockito.verifyStatic(Mockito.times(8));
        FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(), Mockito.any(TrimType.class));
        PowerMockito.verifyStatic(Mockito.times(8));
        FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyChar(), Mockito.any(PaddingType.class));
        Mockito.verify(nullColumnParser, Mockito.times(2)).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(dateColumnParser, Mockito.times(2)).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(decimalColumnParser, Mockito.times(2)).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(intColumnParser, Mockito.times(2)).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(fileLineIterator, Mockito.times(4)).hasNext();
        Mockito.verify(fileLineIterator, Mockito.times(2)).readLine();
        Mockito.verify(fileLineIterator, Mockito.times(2)).separateColumns(Mockito.anyString());

        assertEquals("currentLineCount", 2, ReflectionTestUtils.getField(
                fileLineIterator, "currentLineCount"));

        // テスト実施（3回目）
        AbstractFileLineIterator_Stub09 result03 = fileLineIterator.next();

        // 返却値の確認
        assertEquals(3, result03.getColumn1());
        assertEquals("line3", result03.getColumn2());
        assertEquals(new BigDecimal(333333), result03.getColumn3());
        column4.set(1980, 2, 21, 0, 0, 0);
        assertEquals(column4.getTime().toString(), result03.getColumn4()
                .toString());

        // 状態変化の確認（呼び出し回数は1回目＋2回目＋今回になる）
        PowerMockito.verifyStatic(Mockito.times(12));
        FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(), Mockito.any(TrimType.class));
        PowerMockito.verifyStatic(Mockito.times(12));
        FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyChar(), Mockito.any(PaddingType.class));
        Mockito.verify(nullColumnParser, Mockito.times(3)).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(dateColumnParser, Mockito.times(3)).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(decimalColumnParser, Mockito.times(3)).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(intColumnParser, Mockito.times(3)).parse(Mockito.anyString(), Mockito.anyObject(),
                Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(fileLineIterator, Mockito.times(6)).hasNext();
        Mockito.verify(fileLineIterator, Mockito.times(3)).readLine();
        Mockito.verify(fileLineIterator, Mockito.times(3)).separateColumns(Mockito.anyString());

        assertEquals(3, ReflectionTestUtils.getField(fileLineIterator,
                "currentLineCount"));
    }

    /**
     * testNext13() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "File_Empty.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"File_Empty.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        空<br>
     *                        -------------------<br>
     *                        ※hasNext()がfalseになるデータ<br>
     * <br>
     *                        期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):呼ばれない<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):呼ばれない<br>
     *                        (状態変化) NullColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DateColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DecimalColumnParser#parse():呼ばれない<br>
     *                        (状態変化) IntColumnParser#parse():呼ばれない<br>
     *                        (状態変化) NullStringConverter#convert():呼ばれない<br>
     *                        (状態変化) this.hasNext():1回呼ばれる<br>
     *                        (状態変化) this.readLine():呼ばれない<br>
     *                        (状態変化) this.separateColumns(String):呼ばれない<br>
     *                        (状態変化) this.currentLineCount:0<br>
     *                        (状態変化) 例外:以下の情報を持つFileLineExceptionが発生することを確認する。<br>
     *                        ・メッセージ："The data which can be acquired doesn't exist."<br>
     *                        ・原因例外：NoSuchElementException<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     *                        ・行番号：0<br>
     *                        ・カラム名：null<br>
     *                        ・カラムインデックス：-1<br>
     * <br>
     *                        例外。<br>
     *                        対象ファイルの内容が空の場合（ファイルに読めるデータがない場合）に、FileLineExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext13() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next20.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub12> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub12>(
                fileName, AbstractFileLineIterator_Stub12.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileLineExceptionがスローされませんでした。");
        } catch (FileLineException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic(Mockito.never());
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                    Mockito.any(TrimType.class));
            PowerMockito.verifyStatic(Mockito.never());
            FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                    Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(nullColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(dateColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(decimalColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(intColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator).hasNext();
            Mockito.verify(fileLineIterator, Mockito.never()).readLine();
            Mockito.verify(fileLineIterator, Mockito.never()).separateColumns(Mockito.anyString());
            assertEquals(0, ReflectionTestUtils.getField(fileLineIterator,
                    "currentLineCount"));

            assertEquals(FileLineException.class, e.getClass());
            assertEquals("The data which can be acquired doesn't exist.", e
                    .getMessage());
            assertSame(NoSuchElementException.class, e.getCause().getClass());
            assertEquals(fileName, e.getFileName());
            assertEquals("getLineNo", 1, e.getLineNo());
            assertNull(e.getColumnName());
            assertEquals(-1, e.getColumnIndex());
        }
    }

    /**
     * testNext14() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar："\""<br>
     * - その他項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：Date column4<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > columnFormat：yyyy/MM/dd<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：BigDecimal column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > columnFormat：###,###<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next09.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:true<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next09.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,line1,111111,1980/01/21<br>
     *                        -------------------<br>
     *                        ※正常データ<br>
     * <br>
     *                        期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):呼ばれない<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):呼ばれない<br>
     *                        (状態変化) NullColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DateColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DecimalColumnParser#parse():呼ばれない<br>
     *                        (状態変化) IntColumnParser#parse():呼ばれない<br>
     *                        (状態変化) NullStringConverter#convert():呼ばれない<br>
     *                        (状態変化) this.hasNext():呼ばれない<br>
     *                        (状態変化) this.readLine():呼ばれない<br>
     *                        (状態変化) this.separateColumns(String):呼ばれない<br>
     *                        (状態変化) this.currentLineCount:0<br>
     *                        (状態変化) 例外:以下の情報を持つFileLineExceptionが発生することを確認する。<br>
     *                        ・メッセージ："Data part should be called before trailer part."<br>
     *                        ・原因例外：IllegalStateException<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     *                        ・行番号：0<br>
     *                        ・カラム名：null<br>
     *                        ・カラムインデックス：-1<br>
     * <br>
     *                        例外。<br>
     *                        トレイラ部のデータ取得が行われた後にnext()が呼ばれた場合に、FileLineExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext14() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next09.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09>(
                fileName, AbstractFileLineIterator_Stub09.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "readTrailer", true);
        // その他は、テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileLineExceptionがスローされませんでした。");
        } catch (FileLineException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic(Mockito.never());
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                    Mockito.any(TrimType.class));
            PowerMockito.verifyStatic(Mockito.never());
            FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                    Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(nullColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(dateColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(decimalColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(intColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator, Mockito.never()).hasNext();
            Mockito.verify(fileLineIterator, Mockito.never()).readLine();
            Mockito.verify(fileLineIterator, Mockito.never()).separateColumns(Mockito.anyString());
            assertEquals(0, ReflectionTestUtils.getField(fileLineIterator,
                    "currentLineCount"));

            assertEquals(FileLineException.class, e.getClass());
            assertEquals("Data part should be called before trailer part.", e
                    .getMessage());
            assertSame(IllegalStateException.class, e.getCause().getClass());
            assertEquals(fileName, e.getFileName());
            assertEquals(0, e.getLineNo());
            assertNull(e.getColumnName());
            assertEquals(-1, e.getColumnIndex());
        }
    }

    /**
     * testNext15() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar："\""<br>
     * - その他項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：Date column4<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > columnFormat：yyyy/MM/dd<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：BigDecimal column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > columnFormat：###,###<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next23.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next23.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,line1,111111<br>
     *                        -------------------<br>
     *                        ※カラム数が３でフィールドの数と合ってない。<br>
     * <br>
     *                        期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):呼ばれない<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):呼ばれない<br>
     *                        (状態変化) NullColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DateColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DecimalColumnParser#parse():呼ばれない<br>
     *                        (状態変化) IntColumnParser#parse():呼ばれない<br>
     *                        (状態変化) NullStringConverter#convert():呼ばれない<br>
     *                        (状態変化) this.hasNext():2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回呼ばれる<br>
     *                        (状態変化) this.currentLineCount:0<br>
     *                        (状態変化) 例外:以下の情報を持つFileLineExceptionが発生することを確認する。<br>
     *                        ・メッセージ："Column Count is different from FileLineObject's column counts"<br>
     *                        ・原因例外：IllegalStateException<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     *                        ・行番号：1<br>
     *                        ・カラム名：null<br>
     *                        ・カラムインデックス：-1<br>
     * <br>
     *                        例外。<br>
     *                        ファイル行オブジェクトのマッピング対象フィールドの数と合わないデータを対象ファイルから読む場合に、FileLineExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext15() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next23.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09>(
                fileName, AbstractFileLineIterator_Stub09.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileLineExceptionがスローされませんでした。");
        } catch (FileLineException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic(Mockito.never());
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                    Mockito.any(TrimType.class));
            PowerMockito.verifyStatic(Mockito.never());
            FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                    Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(nullColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(dateColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(decimalColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(intColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
            Mockito.verify(fileLineIterator).readLine();
            Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

            assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                    fileLineIterator, "currentLineCount"));

            assertEquals(FileLineException.class, e.getClass());
            assertEquals("Column Count is different from FileLineObject's "
                    + "column counts", e.getMessage());
            assertSame(IllegalStateException.class, e.getCause().getClass());
            assertEquals(fileName, e.getFileName());
            assertEquals("getLineNo", 1, e.getLineNo());
            assertNull(e.getColumnName());
            assertEquals(-1, e.getColumnIndex());
        }
    }

    /**
     * testNext16() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar："\""<br>
     * - その他項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：Date column4<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > columnFormat：yyyy/MM/dd<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：BigDecimal column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > columnFormat：###,###<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next09.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.readLine():FileException例外が発生<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next09.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,line1,111111,1980/01/21<br>
     *                        -------------------<br>
     *                        ※正常データ<br>
     * <br>
     *                        期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):呼ばれない<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):呼ばれない<br>
     *                        (状態変化) NullColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DateColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DecimalColumnParser#parse():呼ばれない<br>
     *                        (状態変化) IntColumnParser#parse():呼ばれない<br>
     *                        (状態変化) NullStringConverter#convert():呼ばれない<br>
     *                        (状態変化) this.hasNext():１回呼ばれる<br>
     *                        ※readLine()の中でエラーが起きるため、内部でhasNextは呼ばない<br>
     *                        (状態変化) this.readLine():1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):呼ばれない<br>
     *                        (状態変化) this.currentLineCount:0<br>
     *                        (状態変化) 例外:this.readLine()で発生したFileExceptionがそのままスローされることを確認する。<br>
     * <br>
     *                        例外。<br>
     *                        対象ファイルの読みに失敗した場合に、原因例外がそのままスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext16() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next09.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09>(
                fileName, AbstractFileLineIterator_Stub09.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        FileException exception = new FileException("readLineでのエラーです。");
        Mockito.doThrow(exception).when(fileLineIterator).readLine();

        // その他は、テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic(Mockito.never());
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                    Mockito.any(TrimType.class));
            PowerMockito.verifyStatic(Mockito.never());
            FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                    Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(nullColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(dateColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(decimalColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(intColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator).hasNext();
            Mockito.verify(fileLineIterator).readLine();
            Mockito.verify(fileLineIterator, Mockito.never()).separateColumns(Mockito.anyString());
            assertEquals(0, ReflectionTestUtils.getField(fileLineIterator,
                    "currentLineCount"));

            assertSame(exception, e);
        }
    }

    /**
     * testNext17() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar："\""<br>
     * - その他項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：Date column4<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > columnFormat：yyyy/MM/dd<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：BigDecimal column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > columnFormat：###,###<br>
     *                        > その他項目：デフォルト値<br>
     *                        ・@InputFileColumn設定なしのフィールドを持つ<br>
     *                        - フィールド：String noMappingColumn1<br>
     *                        - フィールド：String noMappingColumn2<br>
     *                        - フィールド：String noMappingColumn3<br>
     *                        - フィールド：String noMappingColumn4<br>
     *                        ※クラス定義時、@有り無しのフィールドの順番を混ぜて定義すること。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next19.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next19.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,line1,111111,1980/01/21<br>
     *                        2,line2,222222,1980/02/21<br>
     *                        3,line3,333333,1980/03/21<br>
     *                        -------------------<br>
     *                        ※複数正常データ<br>
     * <br>
     *                        期待値：(戻り値) this.clazz.getClass():this.clazzで設定されているクラスのインスタンス<br>
     *                        ・1回目の実行結果<br>
     *                        - column1：1<br>
     *                        - column2："line1"<br>
     *                        - column3：111111<br>
     *                        - column4：1980/01/21<br>
     * <br>
     *                        ・2回目の実行結果<br>
     *                        - column1：2<br>
     *                        - column2："line2"<br>
     *                        - column3：222222<br>
     *                        - column4：1980/02/21<br>
     * <br>
     *                        ・3回目の実行結果<br>
     *                        - column1：3<br>
     *                        - column2："line3"<br>
     *                        - column3：333333<br>
     *                        - column4：1980/03/21<br>
     *                        (状態変化) FileDAOUtility.trim(String, String, char, TrimType):1回の実行毎に4回呼ばれる<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):1回の実行毎に4回呼ばれる<br>
     *                        (状態変化) NullColumnParser#parse():1回の実行毎に4回呼ばれる<br>
     *                        (状態変化) DateColumnParser#parse():1回の実行毎に4回呼ばれる<br>
     *                        (状態変化) DecimalColumnParser#parse():1回の実行毎に4回呼ばれる<br>
     *                        (状態変化) IntColumnParser#parse():1回の実行毎に4回呼ばれる<br>
     *                        (状態変化) NullStringConverter#convert():1回の実行毎に4回呼ばれる<br>
     *                        (状態変化) this.hasNext():1回の実行毎に2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回の実行毎に1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回の実行毎に1回呼ばれる<br>
     *                        (状態変化) this.currentLineCount:・1回目実行後：1<br>
     *                        ・2回目実行後：2<br>
     *                        ・3回目実行後：3<br>
     * <br>
     *                        正常。<br>
     *                        ファイル行オブジェクトに@InputFileColumnの設定がないフィールドが有っても問題なく以下の処理がされることを確認する。<br>
     *                        対象ファイルにある複数行の情報が各1行に対してnext()メソッドを呼ぶ度に正しく設定されたファイル行オブジェクトが取得される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext17() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next19.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub13> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub13>(
                fileName, AbstractFileLineIterator_Stub13.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。

        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);

        // テスト実施（1回目）
        AbstractFileLineIterator_Stub13 result = fileLineIterator.next();

        // 返却値の確認
        assertEquals("getColumn1", 1, result.getColumn1());
        assertEquals("line1", result.getColumn2());
        assertEquals(new BigDecimal(111111), result.getColumn3());
        Calendar column4 = new GregorianCalendar();
        column4.set(1980, 0, 21, 0, 0, 0);
        assertEquals(column4.getTime().toString(), result.getColumn4()
                .toString());

        // 状態変化の確認
        PowerMockito.verifyStatic(Mockito.times(4));
        FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                Mockito.any(TrimType.class));
        PowerMockito.verifyStatic(Mockito.times(4));
        FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyChar(), Mockito.any(PaddingType.class));
        Mockito.verify(nullColumnParser).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(dateColumnParser).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(decimalColumnParser).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(intColumnParser).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
        Mockito.verify(fileLineIterator).readLine();
        Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

        assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                fileLineIterator, "currentLineCount"));

        // テスト実施（2回目）
        AbstractFileLineIterator_Stub13 result02 = fileLineIterator.next();

        // 返却値の確認
        assertEquals(2, result02.getColumn1());
        assertEquals("line2", result02.getColumn2());
        assertEquals(new BigDecimal(222222), result02.getColumn3());
        column4.set(1980, 1, 21, 0, 0, 0);
        assertEquals(column4.getTime().toString(), result02.getColumn4()
                .toString());

        // 状態変化の確認（呼び出し回数は1回目＋今回になる）
        PowerMockito.verifyStatic(Mockito.times(8));
        FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                Mockito.any(TrimType.class));
        PowerMockito.verifyStatic(Mockito.times(8));
        FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyChar(), Mockito.any(PaddingType.class));
        Mockito.verify(nullColumnParser, Mockito.times(2)).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(dateColumnParser, Mockito.times(2)).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(decimalColumnParser, Mockito.times(2)).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(intColumnParser, Mockito.times(2)).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(fileLineIterator, Mockito.times(4)).hasNext();
        Mockito.verify(fileLineIterator, Mockito.times(2)).readLine();
        Mockito.verify(fileLineIterator, Mockito.times(2)).separateColumns(Mockito.anyString());

        assertEquals(2, ReflectionTestUtils.getField(fileLineIterator,
                "currentLineCount"));

        // テスト実施（3回目）
        AbstractFileLineIterator_Stub13 result03 = fileLineIterator.next();

        // 返却値の確認
        assertEquals(3, result03.getColumn1());
        assertEquals("line3", result03.getColumn2());
        assertEquals(new BigDecimal(333333), result03.getColumn3());
        column4.set(1980, 2, 21, 0, 0, 0);
        assertEquals(column4.getTime().toString(), result03.getColumn4()
                .toString());

        // 状態変化の確認（呼び出し回数は1回目＋2回目＋今回になる）
        PowerMockito.verifyStatic(Mockito.times(12));
        FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                Mockito.any(TrimType.class));
        PowerMockito.verifyStatic(Mockito.times(12));
        FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyChar(), Mockito.any(PaddingType.class));
        Mockito.verify(nullColumnParser, Mockito.times(3)).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(dateColumnParser, Mockito.times(3)).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(decimalColumnParser, Mockito.times(3)).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(intColumnParser, Mockito.times(3)).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(fileLineIterator, Mockito.times(6)).hasNext();
        Mockito.verify(fileLineIterator, Mockito.times(3)).readLine();
        Mockito.verify(fileLineIterator, Mockito.times(3)).separateColumns(Mockito.anyString());
        assertEquals(3, ReflectionTestUtils.getField(fileLineIterator,
                "currentLineCount"));
    }

    /**
     * testNext18() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar："\""<br>
     * - その他項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：Date column4<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > columnFormat：yyyy/MM/dd<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：BigDecimal column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > columnFormat：###,###<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next09.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) FileDAOUtility.trim(String, String, char, TrimType):FileException例外が発生<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next09.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,line1,111111,1980/01/21<br>
     *                        -------------------<br>
     *                        ※正常データ<br>
     * <br>
     *                        期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):1回呼ばれる<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):呼ばれない<br>
     *                        (状態変化) NullColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DateColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DecimalColumnParser#parse():呼ばれない<br>
     *                        (状態変化) IntColumnParser#parse():呼ばれない<br>
     *                        (状態変化) NullStringConverter#convert():呼ばれない<br>
     *                        (状態変化) this.hasNext():2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回呼ばれる<br>
     *                        (状態変化) this.currentLineCount:0<br>
     *                        (状態変化) 例外:FileDAOUtility.trim(String, String, char, TrimType)で発生したFileExceptionがそのままスローされることを確認する。<br>
     * <br>
     *                        例外。<br>
     *                        対象ファイル⇒ファイル行オブジェクトのマッピング処理中のトリム処理で例外が発生した場合に、原因例外がそのままスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext18() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next09.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09>(
                fileName, AbstractFileLineIterator_Stub09.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);
        FileException exception = new FileException("trimでのエラーです。");
        PowerMockito.doThrow(exception).when(FileDAOUtility.class, "trim", Mockito.anyString(),
                Mockito.anyString(), Mockito.anyChar(), Mockito.any(TrimType.class));
        // その他は、テスト対象のインスタンス化時に設定している。

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic();
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                    Mockito.any(TrimType.class));
            PowerMockito.verifyStatic(Mockito.never());
            FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                    Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(nullColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(dateColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(decimalColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(intColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
            Mockito.verify(fileLineIterator).readLine();
            Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

            assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                    fileLineIterator, "currentLineCount"));

            assertSame(exception, e);
        }
    }

    /**
     * testNext19() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar："\""<br>
     * - その他項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：Date column4<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > columnFormat：yyyy/MM/dd<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：BigDecimal column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > columnFormat：###,###<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next09.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) FileDAOUtility.padding(String, String, int, char, PaddingType):FileException例外が発生<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next09.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,line1,111111,1980/01/21<br>
     *                        -------------------<br>
     *                        ※正常データ<br>
     * <br>
     *                        期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):1回呼ばれる<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):1回呼ばれる<br>
     *                        (状態変化) NullColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DateColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DecimalColumnParser#parse():呼ばれない<br>
     *                        (状態変化) IntColumnParser#parse():呼ばれない<br>
     *                        (状態変化) NullStringConverter#convert():呼ばれない<br>
     *                        (状態変化) this.hasNext():2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回呼ばれる<br>
     *                        (状態変化) this.currentLineCount:0<br>
     *                        (状態変化) 例外:FileDAOUtility.padding(String, String, int, char,
     *                        PaddingType)で発生したFileExceptionがそのままスローされることを確認する。<br>
     * <br>
     *                        例外。<br>
     *                        対象ファイル⇒ファイル行オブジェクトのマッピング処理中のパディング処理で例外が発生した場合に、原因例外がそのままスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext19() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next09.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09>(
                fileName, AbstractFileLineIterator_Stub09.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);
        FileException exception = new FileException("paddingでのエラーです。");
        PowerMockito.doThrow(exception).when(FileDAOUtility.class, "padding", Mockito.anyString(),
                Mockito.anyString(), Mockito.anyInt(), Mockito.anyChar(), Mockito.any(PaddingType.class));

        // その他は、テスト対象のインスタンス化時に設定している。

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic();
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                    Mockito.any(TrimType.class));
            PowerMockito.verifyStatic();
            FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                    Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(nullColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(dateColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(decimalColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(intColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
            Mockito.verify(fileLineIterator).readLine();
            Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

            assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                    fileLineIterator, "currentLineCount"));

            assertSame(exception, e);
        }
    }

    /**
     * testNext20() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar："\""<br>
     * - その他項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：Date column4<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > columnFormat：yyyy/MM/dd<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：BigDecimal column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > columnFormat：###,###<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next09.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.hasNext():FileException例外が発生<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next09.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,line1,111111,1980/01/21<br>
     *                        -------------------<br>
     *                        ※正常データ<br>
     * <br>
     *                        期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):呼ばれない<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):呼ばれない<br>
     *                        (状態変化) NullColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DateColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DecimalColumnParser#parse():呼ばれない<br>
     *                        (状態変化) IntColumnParser#parse():呼ばれない<br>
     *                        (状態変化) NullStringConverter#convert():呼ばれない<br>
     *                        (状態変化) this.hasNext():１回呼ばれる<br>
     *                        (状態変化) this.readLine():呼ばれない<br>
     *                        (状態変化) this.separateColumns(String):呼ばれない<br>
     *                        (状態変化) this.currentLineCount:0<br>
     *                        (状態変化) 例外:this.hasNext()で発生したFileExceptionがそのままスローされることを確認する。<br>
     * <br>
     *                        例外。<br>
     *                        対象ファイルに対して次の処理データがあるかのチェック処理で例外が発生した場合に、原因例外がそのままスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext20() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next09.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09>(
                fileName, AbstractFileLineIterator_Stub09.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);
        FileException exception = new FileException("paddingでのエラーです。");
        Mockito.doThrow(exception).when(fileLineIterator).hasNext();
        // その他は、テスト対象のインスタンス化時に設定している。

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic(Mockito.never());
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                    Mockito.any(TrimType.class));
            PowerMockito.verifyStatic(Mockito.never());
            FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                    Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(nullColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(dateColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(decimalColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(intColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator).hasNext();
            Mockito.verify(fileLineIterator, Mockito.never()).readLine();
            Mockito.verify(fileLineIterator, Mockito.never()).separateColumns(Mockito.anyString());
            assertEquals(0, ReflectionTestUtils.getField(fileLineIterator,
                    "currentLineCount"));

            assertSame(exception, e);
        }
    }

    /**
     * testNext21() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar："\""<br>
     * - その他項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：Date column4<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > columnFormat：yyyy/MM/dd<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：BigDecimal column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > columnFormat：###,###<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next09.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():false<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next09.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        1,line1,111111,1980/01/21<br>
     *                        -------------------<br>
     *                        ※正常データ<br>
     * <br>
     *                        期待値：(戻り値) this.clazz.getClass():this.clazzで設定されているクラスのインスタンス<br>
     *                        - column1：1<br>
     *                        - column2："line1"<br>
     *                        - column3：111111<br>
     *                        - column4：1980/01/21<br>
     *                        (状態変化) FileDAOUtility.trim(String, String, char, TrimType):4回呼ばれる<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):4回呼ばれる<br>
     *                        (状態変化) NullColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) DateColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) DecimalColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) IntColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) NullStringConverter#convert():4回呼ばれる<br>
     *                        (状態変化) this.hasNext():2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回呼ばれる<br>
     *                        (状態変化) String.getBytes():呼ばれない<br>
     *                        (状態変化) this.currentLineCount:1<br>
     * <br>
     *                        正常。(バイト数チェックしない)<br>
     *                        対象ファイルの内容が正しく設定されたファイル行オブジェクトが取得されることを確認する。<br>
     *                        但し、バイト数チェックが走らないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext21() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next09.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub09>(
                fileName, AbstractFileLineIterator_Stub09.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);
        Mockito.doReturn(false).when(fileLineIterator).isCheckByte(Mockito.anyInt());


        // その他は、テスト対象のインスタンス化時に設定している。

        // テスト実施
        AbstractFileLineIterator_Stub09 result = fileLineIterator.next();

        // 返却値の確認
        assertEquals("getColumn1", 1, result.getColumn1());
        assertEquals("line1", result.getColumn2());
        assertEquals(new BigDecimal(111111), result.getColumn3());
        Calendar column4 = new GregorianCalendar();
        column4.set(1980, 0, 21, 0, 0, 0);
        assertEquals(column4.getTime().toString(), result.getColumn4()
                .toString());

        // 状態変化の確認
        PowerMockito.verifyStatic(Mockito.times(4));
        FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                Mockito.any(TrimType.class));
        PowerMockito.verifyStatic(Mockito.times(4));
        FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyChar(), Mockito.any(PaddingType.class));Mockito.verify(nullColumnParser).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(dateColumnParser).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(decimalColumnParser).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(intColumnParser).parse(Mockito.anyString(),
                Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
        Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
        Mockito.verify(fileLineIterator).readLine();
        Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

        assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                fileLineIterator, "currentLineCount"));
    }

    /**
     * testNext22() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl<br>
     * 読み取ったデータ1行分をカンマ（,）で区切って、カラムとして返す<br>
     * <br>
     * ※インスタンス生成時にthis.fileEncodingを"aaa"に置き換えること<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > bytes：5<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_next13.txt"<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     *                        ・"java.util.Date"=DateColumnParserインスタンス<br>
     *                        ・"java.math.BigDecimal"=DecimalColumnParserインスタンス<br>
     *                        ・"int"=IntColumnParserインスタンス<br>
     *                        (状態) this.readTrailer:false<br>
     *                        (状態) this.isCheckByte():true<br>
     *                        (状態) 対象ファイル:以下の内容を持つ"AbstractFileLineIterator_next13.txt"ファイルが存在する。<br>
     *                        -------------------<br>
     *                        ABCDE,12345<br>
     *                        -------------------<br>
     *                        ※正常データ<br>
     * <br>
     *                        期待値：(状態変化) FileDAOUtility.trim(String, String, char, TrimType):1回呼ばれる<br>
     *                        (状態変化) FileDAOUtility.padding(String, String, int, char, PaddingType):1回呼ばれる<br>
     *                        (状態変化) NullColumnParser#parse():1回呼ばれる<br>
     *                        (状態変化) DateColumnParser#parse():呼ばれない<br>
     *                        (状態変化) DecimalColumnParser#parse():呼ばれない<br>
     *                        (状態変化) IntColumnParser#parse():呼ばれない<br>
     *                        (状態変化) NullStringConverter#convert():1回呼ばれる<br>
     *                        (状態変化) this.hasNext():2回呼ばれる<br>
     *                        ※readLine()の中でhasNextが１回呼ばれている。<br>
     *                        (状態変化) this.readLine():1回呼ばれる<br>
     *                        (状態変化) this.separateColumns(String):1回呼ばれる<br>
     *                        (状態変化) this.currentLineCount:0<br>
     *                        (状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                        ・メッセージ："fileEncoding which isn't supported was set."<br>
     *                        ・原因例外：UnsupportedEncodingException<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     *                        例外。<br>
     *                        コンストラクタ生成後に、fileEncodingを不正なエンコーディング文字列に置き換えた場合は、#next()呼び出し時に例外が起きることを確認する。<br>
     * <br>
     *                        ※不正にクラスを書き換えなければ、コンストラクタで呼ばれる#buildLineReader()により、例外が起きる。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testNext22() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next13.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        NullColumnParser nullColumnParser = Mockito.spy(new NullColumnParser());
        DateColumnParser dateColumnParser = Mockito.spy(new DateColumnParser());
        DecimalColumnParser decimalColumnParser = Mockito.spy(new DecimalColumnParser());
        IntColumnParser intColumnParser = Mockito.spy(new IntColumnParser());
        columnParserMap.put("java.lang.String", nullColumnParser);
        columnParserMap.put("java.util.Date", dateColumnParser);
        columnParserMap.put("java.math.BigDecimal", decimalColumnParser);
        columnParserMap.put("int", intColumnParser);

        AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub10> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl01<AbstractFileLineIterator_Stub10>(
                fileName, AbstractFileLineIterator_Stub10.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定している。
        PowerMockito.mockStatic(FileDAOUtility.class, Mockito.CALLS_REAL_METHODS);
        ReflectionTestUtils.setField(fileLineIterator, "fileEncoding", "aaa");

        // テスト実施
        try {
            fileLineIterator.next();
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            PowerMockito.verifyStatic();
            FileDAOUtility.trim(Mockito.anyString(), Mockito.anyString(), Mockito.anyChar(),
                    Mockito.any(TrimType.class));
            PowerMockito.verifyStatic();
            FileDAOUtility.padding(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
                    Mockito.anyChar(), Mockito.any(PaddingType.class));
            Mockito.verify(nullColumnParser).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(dateColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(decimalColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(intColumnParser, Mockito.never()).parse(Mockito.anyString(),
                    Mockito.anyObject(), Mockito.any(Method.class), Mockito.anyString());
            Mockito.verify(fileLineIterator, Mockito.times(2)).hasNext();
            Mockito.verify(fileLineIterator).readLine();
            Mockito.verify(fileLineIterator).separateColumns(Mockito.anyString());

            assertEquals("currentLineCount", 1, ReflectionTestUtils.getField(
                    fileLineIterator, "currentLineCount"));

            assertEquals(FileException.class, e.getClass());
            assertEquals("fileEncoding which isn't supported was set.", e
                    .getMessage());
            assertEquals(UnsupportedEncodingException.class, e.getCause()
                    .getClass());
            assertEquals(fileName, e.getFileName());
        }
    }

    /**
     * testRemove01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つUnsupportedOperationException()が発生する。<br>
     * ・メッセージ："remove() isn't supported."<br>
     * <br>
     * メソッドを実行するとアンサポート例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRemove01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next06.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            fileLineIterator.remove();
            fail("UnsupportedOperationExceptionがスローされませんでした。");
        } catch (UnsupportedOperationException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(UnsupportedOperationException.class, e.getClass());
            assertEquals("remove() isn't supported.", e.getMessage());
        }
    }

    /**
     * testInit01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.calledInit:true<br>
     * (状態) this.buildFields():正常処理<br>
     * (状態) this.buildStringConverters():正常処理<br>
     * (状態) this.buildMethods():正常処理<br>
     * (状態) this.buildHeader():正常処理<br>
     * (状態) this.buildTrailerQueue():正常処理<br>
     * (状態) this.buildLineReader():正常処理<br>
     * <br>
     * 期待値：(状態変化) this.calledInit:true<br>
     * (状態変化) this.buildFields():呼ばれない<br>
     * (状態変化) this.buildStringConverters():呼ばれない<br>
     * (状態変化) this.buildMethods():呼ばれない<br>
     * (状態変化) this.buildLineReader():呼ばれない<br>
     * (状態変化) this.buildHeader():呼ばれない<br>
     * (状態変化) this.buildTrailerQueue():呼ばれない<br>
     * <br>
     * 正常。<br>
     * 既にinit()が呼ばれた場合は、init()処理が行わないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInit01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_next06.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = PowerMockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "calledInit", true);

        // テスト実施
        fileLineIterator.init();

        // 返却値の確認
        // なし

        // 状態変化の確認
        assertTrue((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                "calledInit"));
        PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildFields");
        PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildStringConverters");
        PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildMethods");
        PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildLineReader");
        PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildHeader");
        PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildTrailerQueue");
    }

    /**
     * testInit02() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.calledInit:false<br>
     * (状態) this.buildFields():正常処理<br>
     * (状態) this.buildStringConverters():正常処理<br>
     * (状態) this.buildMethods():正常処理<br>
     * (状態) this.buildHeader():正常処理<br>
     * (状態) this.buildTrailerQueue():正常処理<br>
     * (状態) this.buildLineReader():正常処理<br>
     * <br>
     * 期待値：(状態変化) this.calledInit:true<br>
     * (状態変化) this.buildFields():1回呼ばれる<br>
     * (状態変化) this.buildStringConverters():1回呼ばれる<br>
     * (状態変化) this.buildMethods():1回呼ばれる<br>
     * (状態変化) this.buildLineReader():1回呼ばれる<br>
     * (状態変化) this.buildHeader():1回呼ばれる<br>
     * (状態変化) this.buildTrailerQueue():1回呼ばれる<br>
     * <br>
     * 正常。<br>
     * 最初にInit()処理が呼ばれた場合は、init()処理が行われることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInit02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = PowerMockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "calledInit", false);

        // テスト実施
        fileLineIterator.init();

        // 返却値の確認
        // なし

        // 状態変化の確認
        assertTrue((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                "calledInit"));
        PowerMockito.verifyPrivate(fileLineIterator).invoke("buildFields");
        PowerMockito.verifyPrivate(fileLineIterator).invoke("buildStringConverters");
        PowerMockito.verifyPrivate(fileLineIterator).invoke("buildMethods");
        PowerMockito.verifyPrivate(fileLineIterator).invoke("buildLineReader");
        PowerMockito.verifyPrivate(fileLineIterator).invoke("buildHeader");
        PowerMockito.verifyPrivate(fileLineIterator).invoke("buildTrailerQueue");
    }

    /**
     * testInit03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.calledInit:false<br>
     * (状態) this.buildFields():FileException例外が発生する。<br>
     * (状態) this.buildStringConverters():正常処理<br>
     * (状態) this.buildMethods():正常処理<br>
     * (状態) this.buildHeader():正常処理<br>
     * (状態) this.buildTrailerQueue():正常処理<br>
     * (状態) this.buildLineReader():正常処理<br>
     * <br>
     * 期待値：(状態変化) this.calledInit:false<br>
     * (状態変化) this.buildFields():1回呼ばれる<br>
     * (状態変化) this.buildStringConverters():呼ばれない<br>
     * (状態変化) this.buildMethods():呼ばれない<br>
     * (状態変化) this.buildLineReader():呼ばれない<br>
     * (状態変化) this.buildHeader():呼ばれない<br>
     * (状態変化) this.buildTrailerQueue():呼ばれない<br>
     * (状態変化) 例外:this.buildFields()で発生した例外がそのまま投げられることを確認する。<br>
     * <br>
     * 例外。<br>
     * this.buildFields()処理で例外が発生した場合に、例外がそのまま投げられることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInit03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = PowerMockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "calledInit", false);
        FileException exception = new FileException("buildFieldsのエラーです");
        PowerMockito.doThrow(exception).when(fileLineIterator, "buildFields");

        // テスト実施
        try {
            fileLineIterator.init();
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertFalse((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                    "calledInit"));
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildFields");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildStringConverters");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildMethods");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildLineReader");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildHeader");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildTrailerQueue");

            assertSame(exception, e);
        }
    }

    /**
     * testInit04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.calledInit:false<br>
     * (状態) this.buildFields():正常処理<br>
     * (状態) this.buildStringConverters():FileLineException例外が発生する。<br>
     * (状態) this.buildMethods():正常処理<br>
     * (状態) this.buildHeader():正常処理<br>
     * (状態) this.buildTrailerQueue():正常処理<br>
     * (状態) this.buildLineReader():正常処理<br>
     * <br>
     * 期待値：(状態変化) this.calledInit:false<br>
     * (状態変化) this.buildFields():1回呼ばれる<br>
     * (状態変化) this.buildStringConverters():1回呼ばれる<br>
     * (状態変化) this.buildMethods():呼ばれない<br>
     * (状態変化) this.buildLineReader():呼ばれない<br>
     * (状態変化) this.buildHeader():呼ばれない<br>
     * (状態変化) this.buildTrailerQueue():呼ばれない<br>
     * (状態変化) 例外:this.buildStringConverter()で発生した例外がそのまま投げられることを確認する。<br>
     * <br>
     * 例外。<br>
     * this.buildStringConverter()処理で例外が発生した場合に、例外がそのまま投げられることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInit04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = PowerMockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "calledInit", false);
        FileLineException exception = new FileLineException(
                "buildStringConvertersのエラーです");
        PowerMockito.doThrow(exception).when(fileLineIterator, "buildStringConverters");

        // テスト実施
        try {
            fileLineIterator.init();
            fail("FileLineExceptionがスローされませんでした。");
        } catch (FileLineException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertFalse((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                    "calledInit"));
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildFields");
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildStringConverters");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildMethods");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildLineReader");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildHeader");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildTrailerQueue");

            assertSame(exception, e);
        }
    }

    /**
     * testInit05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.calledInit:false<br>
     * (状態) this.buildFields():正常処理<br>
     * (状態) this.buildStringConverters():正常処理<br>
     * (状態) this.buildMethods():FileException例外が発生する。<br>
     * (状態) this.buildHeader():正常処理<br>
     * (状態) this.buildTrailerQueue():正常処理<br>
     * (状態) this.buildLineReader():正常処理<br>
     * <br>
     * 期待値：(状態変化) this.calledInit:false<br>
     * (状態変化) this.buildFields():1回呼ばれる<br>
     * (状態変化) this.buildStringConverters():1回呼ばれる<br>
     * (状態変化) this.buildMethods():1回呼ばれる<br>
     * (状態変化) this.buildLineReader():呼ばれない<br>
     * (状態変化) this.buildHeader():呼ばれない<br>
     * (状態変化) this.buildTrailerQueue():呼ばれない<br>
     * (状態変化) 例外:this.buildMethods()で発生した例外がそのまま投げられることを確認する。<br>
     * <br>
     * 例外。<br>
     * this.buildMethods()処理で例外が発生した場合に、例外がそのまま投げられることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInit05() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = PowerMockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "calledInit", false);
        FileException exception = new FileException("buildMethodsのエラーです");
        PowerMockito.doThrow(exception).when(fileLineIterator, "buildMethods");

        // テスト実施
        try {
            fileLineIterator.init();
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertFalse((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                    "calledInit"));
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildFields");
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildStringConverters");
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildMethods");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildLineReader");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildHeader");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildTrailerQueue");

            assertSame(exception, e);
        }
    }

    /**
     * testInit06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.calledInit:false<br>
     * (状態) this.buildFields():正常処理<br>
     * (状態) this.buildStringConverters():正常処理<br>
     * (状態) this.buildMethods():正常処理<br>
     * (状態) this.buildHeader():FileException例外が発生する。<br>
     * (状態) this.buildTrailerQueue():正常処理<br>
     * (状態) this.buildLineReader():正常処理<br>
     * <br>
     * 期待値：(状態変化) this.calledInit:false<br>
     * (状態変化) this.buildFields():1回呼ばれる<br>
     * (状態変化) this.buildStringConverters():1回呼ばれる<br>
     * (状態変化) this.buildMethods():1回呼ばれる<br>
     * (状態変化) this.buildLineReader():1回呼ばれる<br>
     * (状態変化) this.buildHeader():1回呼ばれる<br>
     * (状態変化) this.buildTrailerQueue():呼ばれない<br>
     * (状態変化) 例外:this.buildHeader()で発生した例外がそのまま投げられることを確認する。<br>
     * <br>
     * 例外。<br>
     * this.buildHeader()処理で例外が発生した場合に、例外がそのまま投げられることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInit06() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = PowerMockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "calledInit", false);
        FileException exception = new FileException("buildHeadersのエラーです");
        PowerMockito.doThrow(exception).when(fileLineIterator, "buildHeader");

        // テスト実施
        try {
            fileLineIterator.init();
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertFalse((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                    "calledInit"));
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildFields");
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildStringConverters");
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildMethods");
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildLineReader");
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildHeader");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildTrailerQueue");

            assertSame(exception, e);
        }
    }

    /**
     * testInit07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.calledInit:false<br>
     * (状態) this.buildFields():正常処理<br>
     * (状態) this.buildStringConverters():正常処理<br>
     * (状態) this.buildMethods():正常処理<br>
     * (状態) this.buildHeader():正常処理<br>
     * (状態) this.buildTrailerQueue():FileException例外が発生する。<br>
     * (状態) this.buildLineReader():正常処理<br>
     * <br>
     * 期待値：(状態変化) this.calledInit:false<br>
     * (状態変化) this.buildFields():1回呼ばれる<br>
     * (状態変化) this.buildStringConverters():1回呼ばれる<br>
     * (状態変化) this.buildMethods():1回呼ばれる<br>
     * (状態変化) this.buildLineReader():1回呼ばれる<br>
     * (状態変化) this.buildHeader():1回呼ばれる<br>
     * (状態変化) this.buildTrailerQueue():1回呼ばれる<br>
     * (状態変化) 例外:this.buildTrailerQueue()で発生した例外がそのまま投げられることを確認する。<br>
     * <br>
     * 例外。<br>
     * this.buildTrailerQueue()処理で例外が発生した場合に、例外がそのまま投げられることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInit07() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = PowerMockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "calledInit", false);
        FileException exception = new FileException("buildTrailerQueueのエラーです");
        PowerMockito.doThrow(exception).when(fileLineIterator, "buildTrailerQueue");

        // テスト実施
        try {
            fileLineIterator.init();
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertFalse((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                    "calledInit"));
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildStringConverters");
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildMethods");
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildLineReader");
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildHeader");
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildTrailerQueue");

            assertSame(exception, e);
        }
    }

    /**
     * testInit08() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.calledInit:false<br>
     * (状態) this.buildFields():正常処理<br>
     * (状態) this.buildStringConverters():正常処理<br>
     * (状態) this.buildMethods():正常処理<br>
     * (状態) this.buildHeader():正常処理<br>
     * (状態) this.buildTrailerQueue():正常処理<br>
     * (状態) this.buildLineReader():FileException例外が発生する。<br>
     * <br>
     * 期待値：(状態変化) this.calledInit:false<br>
     * (状態変化) this.buildFields():1回呼ばれる<br>
     * (状態変化) this.buildStringConverters():1回呼ばれる<br>
     * (状態変化) this.buildMethods():1回呼ばれる<br>
     * (状態変化) this.buildLineReader():1回呼ばれる<br>
     * (状態変化) this.buildHeader():呼ばれない<br>
     * (状態変化) this.buildTrailerQueue():呼ばれない<br>
     * (状態変化) 例外:this.buildLineReader()で発生した例外がそのまま投げられることを確認する。<br>
     * <br>
     * 例外。<br>
     * this.buildLineReader()処理で例外が発生した場合に、例外がそのまま投げられることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInit08() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = PowerMockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "calledInit", false);
        FileException exception = new FileException("buildLineReaderのエラーです");
        PowerMockito.doThrow(exception).when(fileLineIterator, "buildLineReader");

        // テスト実施
        try {
            fileLineIterator.init();
            fail("FileExceptionがスローされませんでした。");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertFalse((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                    "calledInit"));
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildStringConverters");
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildMethods");
            PowerMockito.verifyPrivate(fileLineIterator).invoke("buildLineReader");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildHeader");
            PowerMockito.verifyPrivate(fileLineIterator, Mockito.never()).invoke("buildTrailerQueue");

            assertSame(exception, e);
        }
    }

    /**
     * testBuildLineReader01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl03<br>
     * 　#getEncloseChar()：'\"'を返す<br>
     * 　#getDelimiter()：','を返す<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_bulidFields01.txt"<br>
     * (状態) this.fileEncoding:システムデフォルト値<br>
     * (状態) this.lineFeedChar:"\r\n"<br>
     * (状態) this.lineReader:null<br>
     * <br>
     * 期待値：(状態変化) this.reader:"AbstractFileLineIterator_buildLineReader01.txt"に対するBufferedReaderインスタンス<br>
     * (状態変化) this.lineReader:以下の設定を持つEncloseCharLineFeed2LineReaderインスタンス<br>
     * ・delimiterCharacter：','<br>
     * ・encloseCharacter：''\"'<br>
     * ・lineFeedChar："\r\n"<br>
     * ・reader："AbstractFileLineIterator_buildLineReader01.txt"に対するBufferedReaderインスタンス<br>
     * <br>
     * 正常。<br>
     * ファイル行オブジェクトのクラスの@FileFormatの設定に「囲み文字」と「区切り文字」と「2桁の行区切り文字」が設定されている場合に、正しく処理されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildLineReader01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildLineReader01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl03<AbstractFileLineIterator_Stub20> fileLineIterator = new AbstractFileLineIteratorImpl03<AbstractFileLineIterator_Stub20>(
                fileName, AbstractFileLineIterator_Stub20.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "enclosed", true);
        ReflectionTestUtils.setField(fileLineIterator, "columnEncloseChar",
                new char[] {});

        // テスト実施
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object reader = ReflectionTestUtils.getField(fileLineIterator, "reader");
        assertEquals(BufferedReader.class, reader.getClass());

        Field field = AbstractFileLineIterator.class.getDeclaredField("lineReader");
        field.setAccessible(true);
        Object reader02 = field.get(fileLineIterator);        
        assertEquals(EncloseCharLineFeed2LineReader.class, reader02.getClass());
         field = EncloseCharLineFeed2LineReader.class.getDeclaredField("delimiterCharacter");
        field.setAccessible(true);
        assertEquals(',', field.get(reader02));
        assertEquals('\"', ReflectionTestUtils.getField(reader02, "encloseCharacter"));
        assertEquals("\r\n", ReflectionTestUtils.getField(reader02, "lineFeedChar"));
        assertSame(reader, ReflectionTestUtils.getField(reader02, "reader"));

        assertEquals("AbstractFileLineIterator_buildLineReader01", ((BufferedReader) reader).readLine());

        // 後処理
        ((BufferedReader) reader).close();
    }

    /**
     * testBuildLineReader02() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl03<br>
     * 　#getEncloseChar()：'\"'を返す<br>
     * 　#getDelimiter()：','を返す<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_buildLineReader01.txt"<br>
     * (状態) this.fileEncoding:システムデフォルト値<br>
     * (状態) this.lineFeedChar:"\r"<br>
     * (状態) this.lineReader:null<br>
     * <br>
     * 期待値：(状態変化) this.reader:"AbstractFileLineIterator_buildLineReader01.txt"に対するBufferedReaderインスタンス<br>
     * (状態変化) this.lineReader:以下の設定を持つEncloseCharLineFeed1LineReaderインスタンス<br>
     * ・delimiterCharacter：','<br>
     * ・encloseCharacter：''\"'<br>
     * ・lineFeedChar："\r"<br>
     * ・reader："AbstractFileLineIterator_buildLineReader01.txt"に対するBufferedReaderインスタンス<br>
     * <br>
     * 正常。<br>
     * ファイル行オブジェクトのクラスの@FileFormatの設定に「囲み文字」と「区切り文字」と「1桁の行区切り文字」が設定されている場合に、正しく処理されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildLineReader02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildLineReader01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl03<AbstractFileLineIterator_Stub21> fileLineIterator = new AbstractFileLineIteratorImpl03<AbstractFileLineIterator_Stub21>(
                fileName, AbstractFileLineIterator_Stub21.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "enclosed", true);
        ReflectionTestUtils.setField(fileLineIterator, "columnEncloseChar",
                new char[] {});

        // テスト実施
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object reader = ReflectionTestUtils.getField(fileLineIterator, "reader");
        assertEquals(BufferedReader.class, reader.getClass());

        Object reader02 = ReflectionTestUtils.getField(fileLineIterator, "lineReader");
        assertEquals(EncloseCharLineFeed1LineReader.class, reader02.getClass());
        assertEquals(',', ReflectionTestUtils.getField(reader02, "delimiterCharacter"));
        assertEquals('\"', ReflectionTestUtils.getField(reader02, "encloseCharacter"));
        assertEquals("\r", ReflectionTestUtils.getField(reader02, "lineFeedChar"));
        assertSame(reader, ReflectionTestUtils.getField(reader02, "reader"));

        assertEquals("AbstractFileLineIterator_buildLineReader01", ((BufferedReader) reader).readLine());

        // 後処理
        ((BufferedReader) reader).close();
    }

    /**
     * testBuildLineReader03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl04<br>
     * 　#getEncloseChar()：Character.MIN_VALUEを返す<br>
     * 　#getDelimiter()：','を返す<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_buildLineReader01.txt"<br>
     * (状態) this.fileEncoding:システムデフォルト値<br>
     * (状態) this.lineFeedChar:"\r\n"<br>
     * (状態) this.lineReader:null<br>
     * <br>
     * 期待値：(状態変化) this.reader:"AbstractFileLineIterator_buildLineReader01.txt"に対するBufferedReaderインスタンス<br>
     * (状態変化) this.lineReader:以下の設定を持つLineFeed2LineReaderインスタンス<br>
     * ・lineFeedChar："\r\n"<br>
     * ・reader："AbstractFileLineIterator_buildLineReader01.txt"に対するBufferedReaderインスタンス<br>
     * <br>
     * 正常。<br>
     * ファイル行オブジェクトのクラスの@FileFormatの設定に「区切り文字」と「2桁の行区切り文字」が設定されている場合に、正しく処理されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildLineReader03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildLineReader01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl04<AbstractFileLineIterator_Stub20> fileLineIterator = new AbstractFileLineIteratorImpl04<AbstractFileLineIterator_Stub20>(
                fileName, AbstractFileLineIterator_Stub20.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object reader = ReflectionTestUtils.getField(fileLineIterator, "reader");
        assertEquals(BufferedReader.class, reader.getClass());

        Object reader02 = ReflectionTestUtils.getField(fileLineIterator, "lineReader");
        assertEquals(LineFeed2LineReader.class, reader02.getClass());
        assertEquals("\r\n", ReflectionTestUtils.getField(reader02, "lineFeedChar"));
        assertSame(reader, ReflectionTestUtils.getField(reader02, "reader"));

        assertEquals("AbstractFileLineIterator_buildLineReader01", ((BufferedReader) reader).readLine());

        // 後処理
        ((BufferedReader) reader).close();
    }

    /**
     * testBuildLineReader04() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl04<br>
     * 　#getEncloseChar()：Character.MIN_VALUEを返す<br>
     * 　#getDelimiter()：','を返す<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_buildLineReader01.txt"<br>
     * (状態) this.fileEncoding:システムデフォルト値<br>
     * (状態) this.lineFeedChar:"\r"<br>
     * (状態) this.lineReader:null<br>
     * <br>
     * 期待値：(状態変化) this.reader:"AbstractFileLineIterator_buildLineReader01.txt"に対するBufferedReaderインスタンス<br>
     * (状態変化) this.lineReader:以下の設定を持つLineFeed1LineReaderインスタンス<br>
     * ・lineFeedChar："\r"<br>
     * ・reader："AbstractFileLineIterator_buildLineReader01.txt"に対するBufferedReaderインスタンス<br>
     * <br>
     * 正常。<br>
     * ファイル行オブジェクトのクラスの@FileFormatの設定に「区切り文字」と「1桁の行区切り文字」が設定されている場合に、正しく処理されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildLineReader04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildLineReader01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl04<AbstractFileLineIterator_Stub21> fileLineIterator = new AbstractFileLineIteratorImpl04<AbstractFileLineIterator_Stub21>(
                fileName, AbstractFileLineIterator_Stub21.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object reader = ReflectionTestUtils.getField(fileLineIterator, "reader");
        assertEquals(BufferedReader.class, reader.getClass());

        Object reader02 = ReflectionTestUtils.getField(fileLineIterator, "lineReader");
        assertEquals(LineFeed1LineReader.class, reader02.getClass());
        assertEquals("\r", ReflectionTestUtils.getField(reader02, "lineFeedChar"));
        assertSame(reader, ReflectionTestUtils.getField(reader02, "reader"));

        assertEquals("AbstractFileLineIterator_buildLineReader01", ((BufferedReader) reader).readLine());

        // 後処理
        ((BufferedReader) reader).close();
    }

    /**
     * testBuildLineReader05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl03<br>
     * 　#getEncloseChar()：'\"'を返す<br>
     * 　#getDelimiter()：','を返す<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_buildLineReader_noExist.txt"（存在しないファイル）<br>
     * ※インスタンス生成後に設定する。<br>
     * (状態) this.fileEncoding:システムデフォルト値<br>
     * (状態) this.lineFeedChar:"\r\n"<br>
     * (状態) this.lineReader:null<br>
     * ※インスタンス生成後に設定する。<br>
     * (状態) this.reader:null<br>
     * ※インスタンス生成後に設定する。<br>
     * <br>
     * 期待値：(状態変化) this.reader:null<br>
     * (状態変化) this.lineReader:null<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："Failed in generation of reader."<br>
     * ・原因例外：FileNotFoundException<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 例外。<br>
     * 存在しないファイルをfileNameに設定した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildLineReader05() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildLineReader01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl03<AbstractFileLineIterator_Stub20> fileLineIterator = new AbstractFileLineIteratorImpl03<AbstractFileLineIterator_Stub20>(
                fileName, AbstractFileLineIterator_Stub20.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        String fileName_test = "AbstractFileLineIterator_buildLineReader_noExist.txt";
        ReflectionTestUtils.setField(fileLineIterator, "fileName", fileName_test);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", null);
        ReflectionTestUtils.setField(fileLineIterator, "reader", null);
        // その他は、テスト対象のインスタンス化時に設定済み

        // テスト実施
        try {
            Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionがスローされませんでした。");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertNull(ReflectionTestUtils.getField(fileLineIterator, "reader"));
            assertNull(ReflectionTestUtils.getField(fileLineIterator, "lineReader"));

            assertSame(FileException.class, e.getTargetException().getClass());
            assertEquals("Failed in generation of reader.", e.getTargetException().getMessage());
            assertSame(FileNotFoundException.class, e.getTargetException().getCause().getClass());
            assertSame(fileName_test, ((FileException)e.getTargetException()).getFileName());
        }
    }

    /**
     * testBuildLineReader06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl03<br>
     * 　#getEncloseChar()：'\"'を返す<br>
     * 　#getDelimiter()：','を返す<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "File_1Linecsv"<br>
     * (状態) this.fileEncoding:存在しないエンコード<br>
     * ※インスタンス生成後に設定する<br>
     * (状態) this.lineFeedChar:"\r\n"<br>
     * (状態) this.lineReader:null<br>
     * ※インスタンス生成後に設定する。<br>
     * (状態) this.reader:null<br>
     * ※インスタンス生成後に設定する。<br>
     * <br>
     * 期待値：(状態変化) this.reader:null<br>
     * (状態変化) this.lineReader:null<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："Failed in generation of reader."<br>
     * ・原因例外：UnsupportedEncodingException<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 例外。<br>
     * 存在しないエンコードをfileEncodeに設定した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildLineReader06() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildLineReader01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl03<AbstractFileLineIterator_Stub20> fileLineIterator = new AbstractFileLineIteratorImpl03<AbstractFileLineIterator_Stub20>(
                fileName, AbstractFileLineIterator_Stub20.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "fileEncoding", "aaa");
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", null);
        ReflectionTestUtils.setField(fileLineIterator, "reader", null);
        // その他は、テスト対象のインスタンス化時に設定済み

        // テスト実施
        try {
            Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionがスローされませんでした。");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertNull(ReflectionTestUtils.getField(fileLineIterator, "reader"));
            assertNull(ReflectionTestUtils.getField(fileLineIterator, "lineReader"));

            assertSame(FileException.class, e.getTargetException().getClass());
            assertEquals("Failed in generation of reader.", e.getTargetException().getMessage());
            assertSame(UnsupportedEncodingException.class, e.getTargetException().getCause()
                    .getClass());
            assertSame(fileName, ((FileException)e.getTargetException()).getFileName());
        }
    }

    /**
     * testBuildLineReader07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl04<br>
     * 　#getEncloseChar()：Character.MIN_VALUEを返す<br>
     * 　#getDelimiter()：','を返す<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_buildLineReader01.txt"<br>
     * (状態) this.fileEncoding:システムデフォルト値<br>
     * (状態) this.lineFeedChar:"\r\r\r"<br>
     * ※インスタンス生成後に設定する<br>
     * (状態) this.lineReader:null<br>
     * ※インスタンス生成後に設定する。<br>
     * (状態) this.reader:null<br>
     * ※インスタンス生成後に設定する。<br>
     * <br>
     * 期待値：(状態変化) this.reader:"AbstractFileLineIterator_buildLineReader01.txt"に対するBufferedReaderインスタンス<br>
     * (状態変化) this.lineReader:null<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："lineFeedChar length must be 0 or 1 or 2. but: 3"<br>
     * ・原因例外：IllegalStateException<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 例外。<br>
     * ファイル行オブジェクトのクラスの@FileFormatの設定に「区切り文字」と「3桁以上の行区切り文字」が設定されている場合に、例外をスローする。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildLineReader07() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildLineReader01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl04<AbstractFileLineIterator_Stub20> fileLineIterator = new AbstractFileLineIteratorImpl04<AbstractFileLineIterator_Stub20>(
                fileName, AbstractFileLineIterator_Stub20.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "lineFeedChar", "\r\r\r");
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", null);
        ReflectionTestUtils.setField(fileLineIterator, "reader", null);
        // その他は、テスト対象のインスタンス化時に設定済み

        // テスト実施
        try {
            Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionがスローされませんでした。");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            Object reader = ReflectionTestUtils.getField(fileLineIterator, "reader");
            assertEquals(BufferedReader.class, reader.getClass());

            Object reader02 = ReflectionTestUtils.getField(fileLineIterator,
                    "lineReader");
            assertNull(reader02);

            assertSame(FileException.class, e.getTargetException().getClass());
            assertEquals("lineFeedChar length must be 0 or 1 or 2. but: 3", e.getTargetException()
                    .getMessage());
            assertSame(IllegalStateException.class, e.getTargetException().getCause().getClass());
            assertSame(fileName, ((FileException)e.getTargetException()).getFileName());
        }
    }

    /**
     * testBuildLineReader08() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl03<br>
     * 　#getEncloseChar()：'\"'を返す<br>
     * 　#getDelimiter()：','を返す<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_buildLineReader01.txt"<br>
     * (状態) this.fileEncoding:システムデフォルト値<br>
     * (状態) this.lineFeedChar:"\r\r\r"<br>
     * ※インスタンス生成後に設定する<br>
     * (状態) this.lineReader:null<br>
     * ※インスタンス生成後に設定する。<br>
     * (状態) this.reader:null<br>
     * ※インスタンス生成後に設定する。<br>
     * <br>
     * 期待値：(状態変化) this.reader:"AbstractFileLineIterator_buildLineReader01.txt"に対するBufferedReaderインスタンス<br>
     * (状態変化) this.lineReader:null<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："lineFeedChar length must be 0 or 1 or 2. but: 3"<br>
     * ・原因例外：IllegalStateException<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 例外。<br>
     * ファイル行オブジェクトのクラスの@FileFormatの設定に「囲み文字」と「区切り文字」と「3桁以上の行区切り文字」が設定されている場合に、例外をスローする。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildLineReader08() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildLineReader01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl03<AbstractFileLineIterator_Stub20> fileLineIterator = new AbstractFileLineIteratorImpl03<AbstractFileLineIterator_Stub20>(
                fileName, AbstractFileLineIterator_Stub20.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "lineFeedChar", "\r\r\r");
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", null);
        ReflectionTestUtils.setField(fileLineIterator, "reader", null);
        // その他は、テスト対象のインスタンス化時に設定済み

        // テスト実施
        try {
            Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionがスローされませんでした。");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            Object reader = ReflectionTestUtils.getField(fileLineIterator, "reader");
            assertEquals(BufferedReader.class, reader.getClass());

            Object reader02 = ReflectionTestUtils.getField(fileLineIterator,
                    "lineReader");
            assertNull(reader02);

            assertSame(FileException.class, e.getTargetException().getClass());
            assertEquals("lineFeedChar length must be 0 or 1 or 2. but: 3", e.getTargetException()
                    .getMessage());
            assertSame(IllegalStateException.class, e.getTargetException().getCause().getClass());
            assertSame(fileName, ((FileException)e.getTargetException()).getFileName());
        }
    }

    /**
     * testBuildLineReader09() <br>
     * <br>
     * (異常系) <br>
     * <br>
     * 入力値：markSupported がfalseを返す<br>
     * <br>
     * 期待値： 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："BufferedReader of this JVM dose not support mark method"<br>
     * <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildLineReader09() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl04<AbstractFileLineIterator_Stub21> fileLineIterator = new AbstractFileLineIteratorImpl04<AbstractFileLineIterator_Stub21>(
                fileName, AbstractFileLineIterator_Stub21.class,
                columnParserMap);

        // markSupportedがfalseを返却するように設定
        BufferedReader mockBufferedReader = Mockito.mock(BufferedReader.class);
        Mockito.doReturn(false).when(mockBufferedReader).markSupported();
        PowerMockito.whenNew(BufferedReader.class).withAnyArguments().thenReturn(mockBufferedReader);
        try {
            // テスト実施
            Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail();
        } catch (InvocationTargetException e) {
            assertEquals(
                    "BufferedReader of this JVM dose not support mark method",
                    e.getTargetException().getMessage());
        }
    }

    /**
     * testBuildFields01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・フィールドが持てない。<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_bulidFields01.txt"<br>
     * (状態) this.fields:null<br>
     * (状態) this.columnParserMap:以下の要素を持つ<br>
     * Map<String, ColumnParser>インスタンス<br>
     * ・"java.lang.String"=NullColumnParserインスタンス<br>
     * <br>
     * 期待値：(状態変化) Field.getAnnotation():呼ばれない<br>
     * (状態変化) this.fields:要素を持たないField配列インスタンス<br>
     * (状態変化) Class.getDeclaredFields():2回呼ばれる<br>
     * <br>
     * 正常。<br>
     * フィールドclazzがフィールドを持ってない場合、正常終了することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildFields01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "fields", null);

        // テスト実施
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし
        // 状態変化の確認
        Object result_fields = (Field[]) ReflectionTestUtils.getField(
                fileLineIterator, "fields");
        assertEquals(Field[].class, result_fields.getClass());
        Field[] fields = (Field[]) result_fields;
        assertEquals(0, fields.length);
    }

    /**
     * testBuildFields02() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn3<br>
     * - フィールド：String noMappingColumn1<br>
     * - フィールド：String noMappingColumn2<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_bulidFields01.txt"<br>
     * (状態) this.fields:null<br>
     * (状態) this.columnParserMap:以下の要素を持つ<br>
     * Map<String, ColumnParser>インスタンス<br>
     * ・"java.lang.String"=NullColumnParserインスタンス<br>
     * <br>
     * 期待値：(状態変化) Field.getAnnotation():3回呼ばれる<br>
     * (状態変化) this.fields:要素を持たないField配列インスタンス<br>
     * (状態変化) Class.getDeclaredFields():2回呼ばれる<br>
     * <br>
     * 正常。<br>
     * フィールドclazzが@InputFileColumn設定なしのフィールドのみ持つ場合、マッピング対象フィールドの情報が正しく設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildFields02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub30> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub30>(
                fileName, AbstractFileLineIterator_Stub30.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "fields", null);

        // テスト実施
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result_fields = (Field[]) ReflectionTestUtils.getField(
                fileLineIterator, "fields");
        assertEquals(Field[].class, result_fields.getClass());
        Field[] fields = (Field[]) result_fields;
        assertEquals(0, fields.length);
    }

    /**
     * testBuildFields03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexが重複しない。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.fields:null<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     * <br>
     *                        期待値：(状態変化) Field.getAnnotation():6回呼ばれる<br>
     *                        (状態変化) this.fields:this.clazzに対するフィールド要素3つを持つField配列インスタンス<br>
     *                        １．column1<br>
     *                        ２．column2<br>
     *                        ３．column3<br>
     * <br>
     *                        ※順番はcolumnIndex順<br>
     *                        (状態変化) Class.getDeclaredFields():2回呼ばれる<br>
     * <br>
     *                        正常。<br>
     *                        フィールドclazzが複数の@InputFileColumn設定ありのフィールドのみ持ち、また各フィールドのcolumnIndex値に重複がない場合、マッピング対象フィールドの情報が正しく設定されることを確認する
     *                        。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildFields03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub31> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub31>(
                fileName, AbstractFileLineIterator_Stub31.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "fields", null);

        // テスト実施
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result_fields = (Field[]) ReflectionTestUtils.getField(
                fileLineIterator, "fields");
        assertEquals(Field[].class, result_fields.getClass());
        Field[] fields = (Field[]) result_fields;
        assertEquals(3, fields.length);
        assertEquals("column1", fields[0].getName());
        assertEquals("column2", fields[1].getName());
        assertEquals("column3", fields[2].getName());
    }

    /**
     * testBuildFields04() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn3<br>
     * - フィールド：String noMappingColumn1<br>
     * - フィールド：String noMappingColumn2<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexが重複しない。<br>
     *                        ※クラス定義時、@有り無しのフィールドの順番を混ぜて定義すること。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.fields:null<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     * <br>
     *                        期待値：(状態変化) Field.getAnnotation():9回呼ばれる<br>
     *                        (状態変化) this.fields:this.clazzに対するフィールド要素3つを持つField配列インスタンス<br>
     *                        １．column1<br>
     *                        ２．column2<br>
     *                        ３．column3<br>
     * <br>
     *                        ※順番はcolumnIndex順<br>
     *                        (状態変化) Class.getDeclaredFields():2回呼ばれる<br>
     * <br>
     *                        正常。<br>
     *                        フィールドclazzが複数の@InputFileColumn設定あり・なしのフィールドを持ち、また各フィールドのcolumnIndex値に重複がない場合、マッピング対象フィールドの情報が正しく設定されることを確認する
     *                        。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildFields04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub32> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub32>(
                fileName, AbstractFileLineIterator_Stub32.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "fields", null);

        // テスト実施
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result_fields = (Field[]) ReflectionTestUtils.getField(
                fileLineIterator, "fields");
        assertEquals(Field[].class, result_fields.getClass());
        Field[] fields = (Field[]) result_fields;
        assertEquals(3, fields.length);
        assertEquals("column1", fields[0].getName());
        assertEquals("column2", fields[1].getName());
        assertEquals("column3", fields[2].getName());
    }

    /**
     * testBuildFields05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column3<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexが重複する。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.fields:null<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     * <br>
     *                        期待値：(状態変化) Field.getAnnotation():3回呼ばれる<br>
     *                        (状態変化) this.fields:null<br>
     *                        (状態変化) Class.getDeclaredFields():2回呼ばれる<br>
     *                        (状態変化) -:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                        ・メッセージ："Column Index is duplicate : 1"<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     *                        例外。<br>
     *                        フィールドclazzが複数の@InputFileColumn設定ありのフィールドのみ持ち、また各フィールドのcolumnIndex値が重複している場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildFields05() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub33> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub33>(
                fileName, AbstractFileLineIterator_Stub33.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "fields", null);

        // テスト実施
        try {
            Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionがスローされませんでした");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            Object result_fields = (Field[]) ReflectionTestUtils.getField(
                    fileLineIterator, "fields");
            assertNull(result_fields);

            assertEquals(FileException.class, e.getTargetException().getClass());
            assertEquals("Column Index is duplicate : 1", e.getTargetException().getMessage());
            assertSame(fileName, ((FileException)e.getTargetException()).getFileName());
        }
    }

    /**
     * testBuildFields06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn3<br>
     * - フィールド：String noMappingColumn1<br>
     * - フィールド：String noMappingColumn2<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column3<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexは重複しないが、連番ではない。<br>
     *                        ※クラス定義時、@有り無しのフィールドの順番を混ぜて定義すること。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.fields:null<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     * <br>
     *                        期待値：(状態変化) Field.getAnnotation():6回呼ばれる<br>
     *                        (状態変化) Class.getDeclaredFields():2回呼ばれる<br>
     *                        (状態変化) -:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                        ・メッセージ："columnIndex in FileLineObject is not sequential order."<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス<br>
     *                        ・ラップされた例外：IllegalStateException<br>
     * <br>
     *                        例外。<br>
     *                        フィールドclazzが複数の@InputFileColumn設定あり・なしのフィールドを持ち、また各フィールドのcolumnIndex値が順番ではない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildFields06() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub34> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub34>(
                fileName, AbstractFileLineIterator_Stub34.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "fields", null);

        // テスト実施
        try {
            Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionがスローされませんでした");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(FileException.class, e.getTargetException().getClass());
            assertEquals(
                    "columnIndex in FileLineObject is not sequential order.", e.getTargetException()
                            .getMessage());
            assertSame(fileName, ((FileException)e.getTargetException()).getFileName());
            assertSame(IllegalStateException.class, e.getTargetException().getCause().getClass());
        }
    }

    /**
     * testBuildFields07() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn3<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column3<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > その他項目：デフォルト値<br>
     * <br>
     *                        以下の設定は継承元の親クラスに定義されている。<br>
     *                        ・@InputFileColumn設定なしのフィールドを持つ<br>
     *                        - フィールド：String noMappingColumn1<br>
     *                        - フィールド：String noMappingColumn2<br>
     *                        ・@InputFileColumn設定ありのフィールドを持つ<br>
     *                        - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexは重複しない。<br>
     *                        ※クラスが継承関係にあり、親クラスにも@InputFileColumn設定ありのフィールドがある。<br>
     *                        ※クラス定義時、@有り無しのフィールドの順番を混ぜて定義すること。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.fields:null<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     * <br>
     *                        期待値：(状態変化) Field.getAnnotation():9回呼ばれる<br>
     *                        (状態変化) this.fields:this.clazzに対するフィールド要素3つを持つField配列インスタンス<br>
     *                        １．column1<br>
     *                        ２．column2<br>
     *                        ３．column3<br>
     * <br>
     *                        ※順番はcolumnIndex順<br>
     *                        (状態変化) Class.getDeclaredFields():3回呼ばれる<br>
     * <br>
     *                        正常。<br>
     *                        フィールドclazzが複数の@InputFileColumn設定あり・なしのフィールドを持ち、各フィールドのcolumnIndex値に重複がない、またフィールドの定義が親クラスにもある場合、
     *                        親クラスを含めたマッピング対象フィールドの情報が正しく設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildFields07() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub36> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub36>(
                fileName, AbstractFileLineIterator_Stub36.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "fields", null);

        // テスト実施
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result_fields = (Field[]) ReflectionTestUtils.getField(
                fileLineIterator, "fields");
        assertEquals(Field[].class, result_fields.getClass());
        Field[] fields = (Field[]) result_fields;
        assertEquals(3, fields.length);
        assertEquals("column1", fields[0].getName());
        assertEquals("column2", fields[1].getName());
        assertEquals("column3", fields[2].getName());
    }

    /**
     * testBuildFields08() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumnと@OutputFileColumnの設定があるフィールドを持つ<br>
     * - フィールド：String column3<br>
     * @InputFileColumnと@OutputFileColumnの設定<br> > columnIndex：2<br>
     *                                           > その他項目：デフォルト値<br>
     *                                           - フィールド：String column1<br>
     * @InputFileColumnと@OutputFileColumnの設定<br> > columnIndex：0<br>
     *                                           > その他項目：デフォルト値<br>
     *                                           - フィールド：String column2<br>
     * @InputFileColumnと@OutputFileColumnの設定<br> > columnIndex：1<br>
     *                                           > その他項目：デフォルト値<br>
     *                                           ※columnIndexが重複しない。<br>
     *                                           (状態) this.fileName:Stringインスタンス<br>
     *                                           "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                                           (状態) this.fields:null<br>
     *                                           (状態) this.columnParserMap:以下の要素を持つ<br>
     *                                           Map<String, ColumnParser>インスタンス<br>
     *                                           ・"java.lang.String"=NullColumnParserインスタンス<br>
     * <br>
     *                                           期待値：(状態変化) Field.getAnnotation():3回呼ばれる<br>
     *                                           (状態変化) this.fields:this.clazzに対するフィールド要素3つを持つField配列インスタンス<br>
     *                                           １．column1<br>
     *                                           ２．column2<br>
     *                                           ３．column3<br>
     * <br>
     *                                           ※順番はcolumnIndex順<br>
     *                                           (状態変化) Class.getDeclaredFields():2回呼ばれる<br>
     * <br>
     *                                           正常。<br>
     *                                           フィールドclazzが複数の@InputFileColumnと@OutputFileColumnの設定ありのフィールドを持ち、また各フィールドのcolumnIndex値に重複がない場合
     *                                           、マッピング対象フィールドの情報が正しく設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildFields08() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub37> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub37>(
                fileName, AbstractFileLineIterator_Stub37.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "fields", null);

        // テスト実施
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object result_fields = (Field[]) ReflectionTestUtils.getField(
                fileLineIterator, "fields");
        assertEquals(Field[].class, result_fields.getClass());
        Field[] fields = (Field[]) result_fields;
        assertEquals(3, fields.length);
        assertEquals("column1", fields[0].getName());
        assertEquals("column2", fields[1].getName());
        assertEquals("column3", fields[2].getName());
    }

    /**
     * testBuildFields09() <br>
     * <br>
     * (異常系) <br>
     * 観点：B,G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column3<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexが重複しない。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.fields:null<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     * <br>
     *                        期待値：(状態変化) Field.getAnnotation():1回呼ばれる<br>
     *                        (状態変化) this.fields:null<br>
     *                        (状態変化) Class.getDeclaredFields():2回呼ばれる<br>
     *                        (状態変化) -:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                        ・原因例外：IllegalStateException<br>
     *                        ・メッセージ："Column Index in FileLineObject is bigger than the total number of the field."<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     *                        異常。<br>
     *                        フィールドclazzが複数の@InputFileColumn設定ありのフィールドのみ持ち、また各フィールドのcolumnIndex値に重複がないが0から始まる連番ではない場合、例外が発生することを確認する
     *                        。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildFields09() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub38> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub38>(
                fileName, AbstractFileLineIterator_Stub38.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "fields", null);

        // テスト実施
        try {
            Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionが発生していません。");
        } catch (InvocationTargetException e) {
            // 例外の確認
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getTargetException()
                    .getCause().getClass()));
            assertEquals("Column Index in FileLineObject is bigger than the "
                    + "total number of the field.", e.getTargetException().getMessage());
            assertEquals(fileName, ((FileException)e.getTargetException()).getFileName());

            // 状態変化の確認
            Object result_fields = ReflectionTestUtils.getField(fileLineIterator,
                    "fields");
            assertNull(result_fields);
        }
    }

    /**
     * testBuildFields10() <br>
     * <br>
     * (異常系) <br>
     * 観点：B,G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：-1<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexが重複しない。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.fields:null<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     * <br>
     *                        期待値：(状態変化) Field.getAnnotation():3回呼ばれる<br>
     *                        (状態変化) this.fields:null<br>
     *                        (状態変化) Class.getDeclaredFields():2回呼ばれる<br>
     *                        (状態変化) -:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                        ・原因例外：IllegalStateException<br>
     *                        ・メッセージ："Column Index in FileLineObject is the minus number."<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     *                        異常。<br>
     *                        フィールドclazzが複数の@InputFileColumn設定ありのフィールドのみ持ち、また各フィールドのcolumnIndex値に重複がないが、マイナス値が設定された場合、例外が発生することを確認する
     *                        。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildFields10() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub39> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub39>(
                fileName, AbstractFileLineIterator_Stub39.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "fields", null);

        // テスト実施
        try {
            Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionが発生していません。");
        } catch (InvocationTargetException e) {
            // 例外の確認
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getTargetException()
                    .getCause().getClass()));
            assertEquals("Column Index in FileLineObject is the minus number.",
                    e.getTargetException().getMessage());
            assertEquals(fileName, ((FileException)e.getTargetException()).getFileName());

            // 状態変化の確認
            Object result_fields = ReflectionTestUtils.getField(fileLineIterator,
                    "fields");
            assertNull(result_fields);
        }
    }

    /**
     * testBuildFields11() <br>
     * <br>
     * (異常系) <br>
     * 観点：B,G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：long column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexが重複しない。<br>
     *                        ※this.columnParserMapに存在しないタイプのフィールドがある。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.fields:null<br>
     *                        (状態) this.columnParserMap:以下の要素を持つ<br>
     *                        Map<String, ColumnParser>インスタンス<br>
     *                        ・"java.lang.String"=NullColumnParserインスタンス<br>
     * <br>
     *                        期待値：(状態変化) Field.getAnnotation():2回呼ばれる<br>
     *                        (状態変化) this.fields:null<br>
     *                        (状態変化) Class.getDeclaredFields():2回呼ばれる<br>
     *                        (状態変化) -:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                        ・原因例外：IllegalStateException<br>
     *                        ・メッセージ："There is a type which isn't supported in a mapping target field in FileLineObject."<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     *                        異常。<br>
     *                        フィールドclazzが複数の@InputFileColumn設定ありのフィールドのみ持ち、対象フィールドがthis.columnParserMapに存在しない型のフィールドの場合、例外が発生することを確認する
     *                        。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildFields11() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub90> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub90>(
                fileName, AbstractFileLineIterator_Stub90.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "fields", null);

        // テスト実施
        try {
            Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionが発生していません。");
        } catch (InvocationTargetException e) {
            // 例外の確認
            assertTrue(IllegalStateException.class.isAssignableFrom(e.getTargetException()
                    .getCause().getClass()));
            assertEquals("There is a type which isn't supported in a mapping "
                    + "target field in FileLineObject.", e.getTargetException().getMessage());
            assertEquals(fileName, ((FileException)e.getTargetException()).getFileName());

            // 状態変化の確認
            Object result_fields = ReflectionTestUtils.getField(fileLineIterator,
                    "fields");
            assertNull(result_fields);
        }
    }

    /**
     * testBuildStringConverter01() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・フィールドが持てない。<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_bulidFields01.txt"<br>
     * (状態) this.stringConverter:null<br>
     * (状態) this.stringConverterCacheMap:要素を持ってないHashMap<Class, StringConverter>インスタンス<br>
     * (状態) this.currentLineCount:0<br>
     * (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     * 期待値：(状態変化) Field.getAnnotation（）:呼ばれない<br>
     * (状態変化) Class.newInstance():呼ばれない<br>
     * (状態変化) Map.containsKey(Object):呼ばれない<br>
     * (状態変化) Map.put(K, V):呼ばれない<br>
     * (状態変化) Map.get(Object):呼ばれない<br>
     * (状態変化) this.stringConverters:要素を持たないStringConverter配列インスタンス<br>
     * (状態変化) this.stringConverterCacheMap:要素を持ってないHashMap<Class, StringConverter>インスタンス<br>
     * <br>
     * 正常。<br>
     * フィールドclazzがフィールドを持ってない場合、StringConverter関連フィールドが正常に(StringConverter情報なし)初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildStringConverter01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Map<Class, StringConverter> cache_stringConverterCacheMap = new HashMap<Class, StringConverter>();
        ReflectionTestUtils.setField(fileLineIterator, "stringConverterCacheMap",
                cache_stringConverterCacheMap);

        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化ですでに設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildStringConverters");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        StringConverter[] stringConverters = (StringConverter[]) ReflectionTestUtils.getField(fileLineIterator, "stringConverters");
        assertEquals(0, stringConverters.length);
        Field field = AbstractFileLineIterator.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        Map<Class, StringConverter> stringConverterCacheMap = (Map<Class, StringConverter>) field.get(fileLineIterator);        
        assertEquals(0, stringConverterCacheMap.size());
    }

    /**
     * testBuildStringConverter02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn1<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_bulidFields01.txt"<br>
     * (状態) this.stringConverter:null<br>
     * (状態) this.stringConverterCacheMap:要素を持ってないHashMap<Class, StringConverter>インスタンス<br>
     * (状態) this.currentLineCount:0<br>
     * (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     * 期待値：(状態変化) Field.getAnnotation（）:呼ばれない<br>
     * (状態変化) Class.newInstance():呼ばれない<br>
     * (状態変化) Map.containsKey(Object):呼ばれない<br>
     * (状態変化) Map.put(K, V):呼ばれない<br>
     * (状態変化) Map.get(Object):呼ばれない<br>
     * (状態変化) this.stringConverters:要素を持たないStringConverter配列インスタンス<br>
     * (状態変化) this.stringConverterCacheMap:要素を持ってないHashMap<Class, StringConverter>インスタンス<br>
     * <br>
     * 正常。<br>
     * フィールドclazzが@InputFileColumn設定なしのフィールド（１つ）のみ持つ場合、StringConverter関連フィールドが正常に(StringConverter情報なし)初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildStringConverter02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub40> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub40>(
                fileName, AbstractFileLineIterator_Stub40.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Map<Class, StringConverter> cache_stringConverterCacheMap = new HashMap<Class, StringConverter>();
        ReflectionTestUtils.setField(fileLineIterator, "stringConverterCacheMap",
                cache_stringConverterCacheMap);

        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);
        // テスト対象のインスタンス化ですでに設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildStringConverters");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        StringConverter[] stringConverters = (StringConverter[]) ReflectionTestUtils.getField(fileLineIterator, "stringConverters");
        assertEquals(0, stringConverters.length);
        Field field = AbstractFileLineIterator.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        Map<Class, StringConverter> stringConverterCacheMap = (Map<Class, StringConverter>) field.get(fileLineIterator);
        assertEquals(0, stringConverterCacheMap.size());
    }

    /**
     * testBuildStringConverter03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > stringConverter：<br>
     *                        StringConverterToUpperCase.class<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.stringConverter:null<br>
     *                        (状態) this.stringConverterCacheMap:要素を持ってないHashMap<Class, StringConverter>インスタンス<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     *                        期待値：(状態変化) Class.newInstance():1回呼ばれる<br>
     *                        (状態変化) Map.containsKey(Object):1回呼ばれる<br>
     *                        (状態変化) Map.put(K, V):1回呼ばれる<br>
     *                        (状態変化) Map.get(Object):呼ばれない<br>
     *                        (状態変化) this.stringConverters:以下の要素を持つStringConverter配列インスタンス<br>
     *                        ・[0]：StringConverterToUpperCaseインスタンス<br>
     * <br>
     *                        ※this.stringConverterCacheMapに格納されているStringConverterToUpperCaseインスタンスと同じもの。<br>
     *                        (状態変化) this.stringConverterCacheMap:以下の要素を持つHashMap<Class, StringConverter>インスタンス<br>
     *                        ・ StringConverterToUpperCase.class<br>
     *                        =StringConverterToUpperCaseインスタンス<br>
     * <br>
     *                        正常。<br>
     *                        フィールドclazzが@InputFileColumn設定ありのフィールド（
     *                        １つ）のみ持つ場合、StringConverter配列には1つのStringConverterが、キャッシュには１つのStringConverterが設定される、かつ全部同じインスタンスのことを確
     *                        認 す る 。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildStringConverter03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub41> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub41>(
                fileName, AbstractFileLineIterator_Stub41.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Map<Class, StringConverter> cache_stringConverterCacheMap = new HashMap<Class, StringConverter>();
        ReflectionTestUtils.setField(fileLineIterator, "stringConverterCacheMap",
                cache_stringConverterCacheMap);

        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);
        // テスト対象のインスタンス化ですでに設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildStringConverters");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        StringConverter[] stringConverters = (StringConverter[]) ReflectionTestUtils.getField(fileLineIterator, "stringConverters");
        assertEquals(1, stringConverters.length);
        assertEquals(StringConverterToUpperCase.class, stringConverters[0]
                .getClass());
        Field field = AbstractFileLineIterator.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        Map<Class, StringConverter> stringConverterCacheMap = (Map<Class, StringConverter>) field.get(fileLineIterator);
        assertEquals(1, stringConverterCacheMap.size());
        assertSame(stringConverters[0], stringConverterCacheMap
                .get(StringConverterToUpperCase.class));
    }

    /**
     * testBuildStringConverter04() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn3<br>
     * - フィールド：String noMappingColumn1<br>
     * - フィールド：String noMappingColumn2<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_bulidFields01.txt"<br>
     * (状態) this.stringConverter:null<br>
     * (状態) this.stringConverterCacheMap:要素を持ってないHashMap<Class, StringConverter>インスタンス<br>
     * (状態) this.currentLineCount:0<br>
     * (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     * 期待値：(状態変化) Field.getAnnotation（）:呼ばれない<br>
     * (状態変化) Class.newInstance():呼ばれない<br>
     * (状態変化) Map.containsKey(Object):呼ばれない<br>
     * (状態変化) Map.put(K, V):呼ばれない<br>
     * (状態変化) Map.get(Object):呼ばれない<br>
     * (状態変化) this.stringConverters:要素を持たないStringConverter配列インスタンス<br>
     * (状態変化) this.stringConverterCacheMap:要素を持ってないHashMap<Class, StringConverter>インスタンス<br>
     * <br>
     * 正常。<br>
     * フィールドclazzが@InputFileColumn設定なしのフィールド（３つ）のみ持つ場合、StringConverter関連フィールドが正常に(StringConverter情報なし)初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildStringConverter04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub42> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub42>(
                fileName, AbstractFileLineIterator_Stub42.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Map<Class, StringConverter> cache_stringConverterCacheMap = new HashMap<Class, StringConverter>();
        ReflectionTestUtils.setField(fileLineIterator, "stringConverterCacheMap",
                cache_stringConverterCacheMap);

        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化ですでに設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildStringConverters");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        StringConverter[] stringConverters = (StringConverter[]) ReflectionTestUtils.getField(fileLineIterator, "stringConverters");
        assertEquals(0, stringConverters.length);
        Field field = AbstractFileLineIterator.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        Map<Class, StringConverter> stringConverterCacheMap = (Map<Class, StringConverter>) field.get(fileLineIterator);
        assertEquals(0, stringConverterCacheMap.size());
    }

    /**
     * testBuildStringConverter05() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > stringConverter：<br>
     *                        StringConverterToUpperCase.class<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > stringConverter：<br>
     *                        StringConverterToUpperCase.class<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > stringConverter：<br>
     *                        StringConverterToUpperCase.class<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexが重複しない。<br>
     *                        ※stringConverterが全部同じ。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.stringConverter:null<br>
     *                        (状態) this.stringConverterCacheMap:要素を持ってないHashMap<Class, StringConverter>インスタンス<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     *                        期待値：(状態変化) Class.newInstance():1回呼ばれる<br>
     *                        (状態変化) Map.containsKey(Object):3回呼ばれる<br>
     *                        (状態変化) Map.put(K, V):1回呼ばれる<br>
     *                        (状態変化) Map.get(Object):2回呼ばれる<br>
     *                        (状態変化) this.stringConverters:以下の要素を持つStringConverter配列インスタンス<br>
     *                        ・[0]：StringConverterToUpperCaseインスタンス<br>
     *                        ・[1]：StringConverterToUpperCaseインスタンス<br>
     *                        ・[2]：StringConverterToUpperCaseインスタンス<br>
     * <br>
     *                        ※３つ全部this.stringConverterCacheMapに格納されているStringConverterToUpperCaseインスタンスと同じもの。<br>
     *                        (状態変化) this.stringConverterCacheMap:以下の要素を持つHashMap<Class, StringConverter>インスタンス<br>
     *                        ・ StringConverterToUpperCase.class<br>
     *                        =StringConverterToUpperCaseインスタンス<br>
     * <br>
     *                        正常。<br>
     *                        フィールドclazzが@InputFileColumn設定ありのフィールド（
     *                        ３つ）のみ持ち、設定されたstringConverterも全フィールド同じの場合、StringConverter配列には３つのStringConverterが、キャッシュには１つのStringCo
     *                        n v e r t e r が 設 定 さ れ る 、 か つ 全 部 同 じ イ ン ス タ ン ス の こ と を 確 認 す る 。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildStringConverter05() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub43> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub43>(
                fileName, AbstractFileLineIterator_Stub43.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Map<Class, StringConverter> cache_stringConverterCacheMap = new HashMap<Class, StringConverter>();
        ReflectionTestUtils.setField(fileLineIterator, "stringConverterCacheMap",
                cache_stringConverterCacheMap);

        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化ですでに設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildStringConverters");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        StringConverter[] stringConverters = (StringConverter[]) ReflectionTestUtils.getField(fileLineIterator, "stringConverters");
        assertEquals(3, stringConverters.length);
        assertEquals(StringConverterToUpperCase.class, stringConverters[0]
                .getClass());
        assertSame(stringConverters[0], stringConverters[1]);
        assertSame(stringConverters[0], stringConverters[2]);
        Field field = AbstractFileLineIterator.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        Map<Class, StringConverter> stringConverterCacheMap = (Map<Class, StringConverter>) field.get(fileLineIterator);
        assertEquals(1, stringConverterCacheMap.size());
        assertSame(stringConverters[0], stringConverterCacheMap
                .get(StringConverterToUpperCase.class));
    }

    /**
     * testBuildStringConverter06() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定あり・なしのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > stringConverter：<br>
     *                        StringConverterToUpperCase.class<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String noMappingColumn1<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > stringConverter：<br>
     *                        StringConverterToUpperCase.class<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexが重複しない。<br>
     *                        ※stringConverterが全部同じ。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.stringConverter:null<br>
     *                        (状態) this.stringConverterCacheMap:要素を持ってないHashMap<Class, StringConverter>インスタンス<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     *                        期待値：(状態変化) Class.newInstance():1回呼ばれる<br>
     *                        (状態変化) Map.containsKey(Object):2回呼ばれる<br>
     *                        (状態変化) Map.put(K, V):1回呼ばれる<br>
     *                        (状態変化) Map.get(Object):1回呼ばれる<br>
     *                        (状態変化) this.stringConverters:以下の要素を持つStringConverter配列インスタンス<br>
     *                        ・[0]：StringConverterToUpperCaseインスタンス<br>
     *                        ・[1]：null<br>
     *                        ・[2]：StringConverterToUpperCaseインスタンス<br>
     * <br>
     *                        ※格納されているインスタンスはthis.stringConverterCacheMapに格納されているStringConverterToUpperCaseインスタンスと同じもの。<br>
     *                        (状態変化) this.stringConverterCacheMap:以下の要素を持つHashMap<Class, StringConverter>インスタンス<br>
     *                        ・ StringConverterToUpperCase.class<br>
     *                        =StringConverterToUpperCaseインスタンス<br>
     * <br>
     *                        正常。<br>
     *                        フィールドclazzが@InputFileColumn設定ありのフィール（
     *                        ２つ）ドとないフィールド（１つ）を持ち、設定されたstringConverterも全フィールド同じの場合、StringConverter配列には２つのStringConverterが、キャッシュに
     *                        は １ つ の S t r i n g C o n v e r t e r が 設 定 さ れ る 、 か つ 全 部 同 じ イ ン ス タ ン ス の こ と を 確 認 す る 。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildStringConverter06() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub44> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub44>(
                fileName, AbstractFileLineIterator_Stub44.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Map<Class, StringConverter> cache_stringConverterCacheMap = new HashMap<Class, StringConverter>();
        ReflectionTestUtils.setField(fileLineIterator, "stringConverterCacheMap",
                cache_stringConverterCacheMap);

        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化ですでに設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildStringConverters");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        StringConverter[] stringConverters = (StringConverter[]) ReflectionTestUtils.getField(fileLineIterator, "stringConverters");
        assertEquals(2, stringConverters.length);
        assertEquals(StringConverterToUpperCase.class, stringConverters[0]
                .getClass());
        assertSame(stringConverters[0], stringConverters[1]);
        Field field = AbstractFileLineIterator.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        Map<Class, StringConverter> stringConverterCacheMap = (Map<Class, StringConverter>) field.get(fileLineIterator);
        assertEquals(1, stringConverterCacheMap.size());
        assertSame(stringConverters[0], stringConverterCacheMap
                .get(StringConverterToUpperCase.class));
    }

    /**
     * testBuildStringConverter07() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定あり・なしのフィールドを持つ<br>
     * - フィールド：String column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column5<br>
     * @InputFileColumn設定<br> > columnIndex：4<br>
     *                        > stringConverter：<br>
     *                        StringConverterToLowerCase.class<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > stringConverter：<br>
     *                        NullStringConverter.class<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column4<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > stringConverter：<br>
     *                        StringConverterToUpperCase.class<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > stringConverter：<br>
     *                        StringConverterToLowerCase.class<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexが重複しない。<br>
     *                        ※2箇所で同じstringConverterを利用している。<br>
     *                        stringConverterの種類は３つ<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.stringConverter:null<br>
     *                        (状態) this.stringConverterCacheMap:要素を持ってないHashMap<Class, StringConverter>インスタンス<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     *                        期待値：(状態変化) Class.newInstance():3回呼ばれる<br>
     *                        (状態変化) Map.containsKey(Object):5回呼ばれる<br>
     *                        (状態変化) Map.put(K, V):3回呼ばれる<br>
     *                        (状態変化) Map.get(Object):2回呼ばれる<br>
     *                        (状態変化) this.stringConverters:以下の要素を持つStringConverter配列インスタンス<br>
     *                        ・[0]：NullStringConverterインスタンス<br>
     *                        ・[1]：StringConverterToLowerCaseインスタンス<br>
     *                        ・[2]：NullStringConverterインスタンス<br>
     *                        ・[3]：StringConverterToUpperCaseインスタンス<br>
     *                        ・[4]：StringConverterToLowerCaseインスタンス<br>
     * <br>
     *                        ※格納されているインスタンスはthis.stringConverterCacheMapに格納されている同じ型のインスタンスと同じもの。<br>
     *                        (状態変化) this.stringConverterCacheMap:以下の要素を持つHashMap<Class, StringConverter>インスタンス<br>
     *                        ・ NullStringConverter.class<br>
     *                        =NullStringConverterインスタンス<br>
     *                        ・ StringConverterToUpperCase.class<br>
     *                        =StringConverterToUpperCaseインスタンス<br>
     *                        ・ StringConverterToLowerCase.class<br>
     *                        =StringConverterToLowerCaseインスタンス<br>
     * <br>
     *                        正常。<br>
     *                        フィールドclazzが@InputFileColumn設定ありのフィールド（5つ）のみ持ち、設定されたstringConverterも重複(2箇所)がある場合、StringConverter配列には５つのStringConverterが
     *                        、キャッシュには３つのStringConverterが設定される、かつ同一タイプは同じインスタンスを利用することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildStringConverter07() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub45> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub45>(
                fileName, AbstractFileLineIterator_Stub45.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Map<Class, StringConverter> cache_stringConverterCacheMap = new HashMap<Class, StringConverter>();
        ReflectionTestUtils.setField(fileLineIterator, "stringConverterCacheMap",
                cache_stringConverterCacheMap);

        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化ですでに設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildStringConverters");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        StringConverter[] stringConverters = (StringConverter[]) ReflectionTestUtils.getField(fileLineIterator, "stringConverters");
        assertEquals(5, stringConverters.length);
        assertSame(NullStringConverter.class, stringConverters[0].getClass());
        assertSame(StringConverterToLowerCase.class, stringConverters[1]
                .getClass());
        assertSame(NullStringConverter.class, stringConverters[2].getClass());
        assertSame(StringConverterToUpperCase.class, stringConverters[3]
                .getClass());
        assertSame(StringConverterToLowerCase.class, stringConverters[4]
                .getClass());

        Field field = AbstractFileLineIterator.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        Map<Class, StringConverter> stringConverterCacheMap = (Map<Class, StringConverter>) field.get(fileLineIterator);
        assertEquals(3, stringConverterCacheMap.size());
        assertSame(stringConverters[0], stringConverterCacheMap
                .get(NullStringConverter.class));
        assertSame(stringConverters[1], stringConverterCacheMap
                .get(StringConverterToLowerCase.class));
        assertSame(stringConverters[2], stringConverterCacheMap
                .get(NullStringConverter.class));
        assertSame(stringConverters[3], stringConverterCacheMap
                .get(StringConverterToUpperCase.class));
        assertSame(stringConverters[4], stringConverterCacheMap
                .get(StringConverterToLowerCase.class));
    }

    /**
     * testBuildStringConverter08() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > stringConverter：<br>
     *                        StringConverter.class<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※stringConverterがインタフェース。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.stringConverter:null<br>
     *                        (状態) this.stringConverterCacheMap:要素を持ってないHashMap<Class, StringConverter>インスタンス<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     *                        期待値：(状態変化) Class.newInstance():2回呼ばれる<br>
     *                        (状態変化) Map.containsKey(Object):2回呼ばれる<br>
     *                        (状態変化) Map.put(K, V):1回呼ばれる<br>
     *                        (状態変化) Map.get(Object):呼ばれない<br>
     *                        (状態変化) this.stringConverters:null<br>
     *                        (状態変化) this.stringConverterCacheMap:以下の要素を持つHashMap<Class, StringConverter>インスタンス<br>
     *                        ・ NullStringConverter.class<br>
     *                        =NullStringConverterインスタンス<br>
     *                        (状態変化) 例外:以下の情報を持つFileLineExceptionが発生することを確認する。<br>
     *                        ・メッセージ："Failed in an instantiate of a stringConverter."<br>
     *                        ・原因例外：InstantiationException<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     *                        ・行番号：-1<br>
     *                        ・カラム名：column2<br>
     *                        ・カラムインデックス：1<br>
     * <br>
     *                        例外。<br>
     *                        フィールドclazzが@InputFileColumn設定ありのフィールドを持ち、設定されたstringConverterがインタフェースの場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildStringConverter08() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub46> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub46>(
                fileName, AbstractFileLineIterator_Stub46.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Map<Class, StringConverter> cache_stringConverterCacheMap = new HashMap<Class, StringConverter>();
        ReflectionTestUtils.setField(fileLineIterator, "stringConverterCacheMap",
                cache_stringConverterCacheMap);

        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化ですでに設定済み

        // テスト実施
        try {
            method = AbstractFileLineIterator.class.getDeclaredMethod("buildStringConverters");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileLineExceptionがスローされませんでした。");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            StringConverter[] stringConverters = (StringConverter[]) ReflectionTestUtils.getField(fileLineIterator, "stringConverters");
            assertNull(stringConverters);

            Field field = AbstractFileLineIterator.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            Map<Class, StringConverter> stringConverterCacheMap = (Map<Class, StringConverter>) field.get(fileLineIterator);
            assertEquals(1, stringConverterCacheMap.size());
            Object cacheMap01 = stringConverterCacheMap
                    .get(NullStringConverter.class);
            assertSame(NullStringConverter.class, cacheMap01.getClass());

            assertEquals(FileLineException.class, e.getTargetException().getClass());
            assertEquals("Failed in an instantiate of a stringConverter.", e.getTargetException()
                    .getMessage());
            assertEquals(InstantiationException.class, e.getTargetException().getCause().getClass());
            assertSame(fileName, ((FileLineException)e.getTargetException()).getFileName());
            assertEquals(-1, ((FileLineException)e.getTargetException()).getLineNo());
            assertEquals("column2", ((FileLineException)e.getTargetException()).getColumnName());
            assertEquals(1, ((FileLineException)e.getTargetException()).getColumnIndex());
        }
    }

    /**
     * testBuildStringConverter09() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > stringConverter：デフォルトで直接インスタンス化できない独自StringConverter.class<br>
     *                        > その他項目：デフォルト値<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.stringConverter:null<br>
     *                        (状態) this.stringConverterCacheMap:要素を持ってないHashMap<Class, StringConverter>インスタンス<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     *                        期待値：(状態変化) Class.newInstance():2回呼ばれる<br>
     *                        (状態変化) Map.containsKey(Object):2回呼ばれる<br>
     *                        (状態変化) Map.put(K, V):1回呼ばれる<br>
     *                        (状態変化) Map.get(Object):呼ばれない<br>
     *                        (状態変化) this.stringConverters:null<br>
     *                        (状態変化) this.stringConverterCacheMap:以下の要素を持つHashMap<Class, StringConverter>インスタンス<br>
     *                        ・ NullStringConverter.class<br>
     *                        =NullStringConverterインスタンス<br>
     *                        (状態変化) 例外:以下の情報を持つFileLineExceptionが発生することを確認する。<br>
     *                        ・メッセージ："Failed in an instantiate of a stringConverter."<br>
     *                        ・原因例外：IllegalAccessException<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     *                        ・行番号：-1<br>
     *                        ・カラム名：column2<br>
     *                        ・カラムインデックス：1<br>
     * <br>
     *                        例外。<br>
     *                        フィールドclazzが@InputFileColumn設定ありのフィールドを持ち、設定されたstringConverterがデフォルトコンストラクタでインスタンス化できないクラスの場合、例外が発生することを確認する
     *                        。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildStringConverter09() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub47> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub47>(
                fileName, AbstractFileLineIterator_Stub47.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Map<Class, StringConverter> cache_stringConverterCacheMap = new HashMap<Class, StringConverter>();
        ReflectionTestUtils.setField(fileLineIterator, "stringConverterCacheMap",
                cache_stringConverterCacheMap);

        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化ですでに設定済み

        // テスト実施
        try {
            method = AbstractFileLineIterator.class.getDeclaredMethod("buildStringConverters");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileLineExceptionがスローされませんでした。");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            StringConverter[] stringConverters = (StringConverter[]) ReflectionTestUtils.getField(fileLineIterator, "stringConverters");
            assertNull(stringConverters);

            Field field = AbstractFileLineIterator.class.getDeclaredField("stringConverterCacheMap");
            field.setAccessible(true);
            Map<Class, StringConverter> stringConverterCacheMap = (Map<Class, StringConverter>) field.get(fileLineIterator);
            assertEquals(1, stringConverterCacheMap.size());
            Object cacheMap01 = stringConverterCacheMap
                    .get(NullStringConverter.class);
            assertSame(NullStringConverter.class, cacheMap01.getClass());

            assertEquals(FileLineException.class, e.getTargetException().getClass());
            assertEquals("Failed in an instantiate of a stringConverter.", e.getTargetException()
                    .getMessage());
            assertEquals(IllegalAccessException.class, e.getTargetException().getCause().getClass());
            assertSame(fileName, ((FileLineException)e.getTargetException()).getFileName());
            assertEquals(-1, ((FileLineException)e.getTargetException()).getLineNo());
            assertEquals("column2", ((FileLineException)e.getTargetException()).getColumnName());
            assertEquals(1, ((FileLineException)e.getTargetException()).getColumnIndex());
        }
    }

    /**
     * testBuildStringConverter10() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > stringConverter：<br>
     *                        StringConverterToUpperCase.class<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > stringConverter：<br>
     *                        StringConverterToLowerCase<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexが重複しない。<br>
     *                        ※stringConverterが全部違う。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) this.stringConverter:null<br>
     *                        (状態) this.stringConverterCacheMap:以下の要素を持つHashMap<Class, StringConverter>インスタンス<br>
     *                        ・ NullStringConverter.class<br>
     *                        =NullStringConverterインスタンス<br>
     *                        ・ StringConverterToUpperCase.class<br>
     *                        =StringConverterToUpperCaseインスタンス<br>
     *                        ・ StringConverterToLowerCase.class<br>
     *                        =StringConverterToLowerCaseインスタンス<br>
     *                        (状態) this.currentLineCount:0<br>
     *                        (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     *                        期待値：(状態変化) Class.newInstance():呼ばれない<br>
     *                        (状態変化) Map.containsKey(Object):3回呼ばれる<br>
     *                        (状態変化) Map.put(K, V):呼ばれない<br>
     *                        (状態変化) Map.get(Object):3回呼ばれる<br>
     *                        (状態変化) this.stringConverters:以下の要素を持つStringConverter配列インスタンス<br>
     *                        ・[0]：NullStringConverterインスタンス<br>
     *                        ・[1]：StringConverterToLowerCaseインスタンス<br>
     *                        ・[2]：StringConverterToUpperCaseインスタンス<br>
     * <br>
     *                        ※格納されているインスタンスはthis.stringConverterCacheMapに格納されている同じ型のインスタンスと同じもの。<br>
     *                        (状態変化) this.stringConverterCacheMap:以下の要素を持つHashMap<Class, StringConverter>インスタンス<br>
     *                        ・ NullStringConverter.class<br>
     *                        =NullStringConverterインスタンス<br>
     *                        ・ StringConverterToUpperCase.class<br>
     *                        =StringConverterToUpperCaseインスタンス<br>
     *                        ・ StringConverterToLowerCase.class<br>
     *                        =StringConverterToLowerCaseインスタンス<br>
     * <br>
     *                        正常。<br>
     *                        Staticフィールドthis.stringConverterCacheMapにキャッシュがある場合、StringConverterのインスタンス生成なしでキャッシュを利用することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildStringConverter10() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub48> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub48>(
                fileName, AbstractFileLineIterator_Stub48.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Map<Class, StringConverter> cache_stringConverterCacheMap = new HashMap<Class, StringConverter>();
        StringConverter cache01 = new NullStringConverter();
        StringConverter cache02 = new StringConverterToLowerCase();
        StringConverter cache03 = new StringConverterToUpperCase();
        cache_stringConverterCacheMap.put(NullStringConverter.class, cache01);
        cache_stringConverterCacheMap.put(StringConverterToLowerCase.class,
                cache02);
        cache_stringConverterCacheMap.put(StringConverterToUpperCase.class,
                cache03);
        ReflectionTestUtils.setField(fileLineIterator, "stringConverterCacheMap",
                cache_stringConverterCacheMap);

        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化ですでに設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildStringConverters");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);
        // 返却値の確認
        // なし

        // 状態変化の確認
        StringConverter[] stringConverters = (StringConverter[]) ReflectionTestUtils.getField(fileLineIterator, "stringConverters");
        assertEquals(3, stringConverters.length);
        assertSame(cache01, stringConverters[0]);
        assertSame(cache02, stringConverters[1]);
        assertSame(cache03, stringConverters[2]);

        Field field = AbstractFileLineIterator.class.getDeclaredField("stringConverterCacheMap");
        field.setAccessible(true);
        Map<Class, StringConverter> stringConverterCacheMap = (Map<Class, StringConverter>) field.get(fileLineIterator);
        assertEquals(3, stringConverterCacheMap.size());
        assertSame(cache01, stringConverterCacheMap
                .get(NullStringConverter.class));
        assertSame(cache02, stringConverterCacheMap
                .get(StringConverterToLowerCase.class));
        assertSame(cache03, stringConverterCacheMap
                .get(StringConverterToUpperCase.class));
    }

    /**
     * testBuildMethods01() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・フィールドが持てない。<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_bulidFields01.txt"<br>
     * (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     * 期待値：(状態変化) Class.getMethod(String):呼ばれない<br>
     * (状態変化) this.methods:要素を持たないMethod配列インスタンス<br>
     * <br>
     * 正常。<br>
     * フィールドclazzがフィールドを持ってない場合、this.methodsにメソッド情報が生成されないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildMethods01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化で設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildMethods");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Method[] methods = (Method[]) ReflectionTestUtils.getField(fileLineIterator,
                "methods");
        assertEquals(0, methods.length);
    }

    /**
     * testBuildMethods02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn1<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_bulidFields01.txt"<br>
     * (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     * 期待値：(状態変化) Class.getMethod(String):呼ばれない<br>
     * (状態変化) this.methods:要素を持たないMethod配列インスタンス<br>
     * <br>
     * 正常。<br>
     * フィールドclazzが@InputFileColumn設定なしのフィールド（１つ）のみ持つ場合、this.methodsにメソッド情報が生成されないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildMethods02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub50> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub50>(
                fileName, AbstractFileLineIterator_Stub50.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化で設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildMethods");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Method[] methods = (Method[]) ReflectionTestUtils.getField(fileLineIterator,
                "methods");
        assertEquals(0, methods.length);
    }

    /**
     * testBuildMethods03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        ・フィールドに対するセッタメソッドが存在する。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     *                        期待値：(状態変化) Class.getMethod(String):1回呼ばれる<br>
     *                        (状態変化) this.methods:以下の要素を持つMethod配列インスタンス<br>
     *                        ・[0]：setColumn1(String)のメソッドインスタンス<br>
     * <br>
     *                        ※メソッド名と引数の型が確認ポイント<br>
     * <br>
     *                        正常。<br>
     *                        フィールドclazzが@InputFileColumn設定ありのフィールド（１つ）のみ持ち、そのフィールドに対するセッタメソッドが存在する場合、this.methodsにそのフィールドに対するセッタ
     *                        メ ソ ッ ド の 情 報 が 生 成 さ れ る こ と を 確 認 す る 。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildMethods03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub51> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub51>(
                fileName, AbstractFileLineIterator_Stub51.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化で設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildMethods");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Method[] methods = (Method[]) ReflectionTestUtils.getField(fileLineIterator,
                "methods");
        assertEquals(1, methods.length);
        assertEquals("setColumn1", methods[0].getName());
        Class[] method01_param = methods[0].getParameterTypes();
        assertEquals(1, method01_param.length);
        assertSame(String.class, method01_param[0]);
    }

    /**
     * testBuildMethods04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        ・フィールドに対するセッタメソッドが存在しない。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     *                        期待値：(状態変化) Class.getMethod(String):1回呼ばれる<br>
     *                        (状態変化) this.methods:null<br>
     *                        (状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     *                        ・メッセージ："The setter method of column doesn't exist."<br>
     *                        ・原因例外：NoSuchMethodException<br>
     *                        ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     *                        例外。<br>
     *                        フィールドclazzが@InputFileColumn設定ありのフィールドを持つが、そのフィールドに対するセッタメソッドを持たない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildMethods04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub52> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub52>(
                fileName, AbstractFileLineIterator_Stub52.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化で設定済み

        // テスト実施
        try {
            method = AbstractFileLineIterator.class.getDeclaredMethod("buildMethods");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionがスローされませんでした。");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            Method[] methods = (Method[]) ReflectionTestUtils.getField(
                    fileLineIterator, "methods");
            assertNull(methods);

            assertSame(FileException.class, e.getTargetException().getClass());
            assertEquals("The setter method of column doesn't exist.", e.getTargetException()
                    .getMessage());
            assertSame(NoSuchMethodException.class, e.getTargetException().getCause().getClass());
            assertSame(fileName, ((FileException)e.getTargetException()).getFileName());
        }
    }

    /**
     * testBuildMethods05() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定なしのフィールドを持つ<br>
     * - フィールド：String noMappingColumn3<br>
     * - フィールド：String noMappingColumn1<br>
     * - フィールド：String noMappingColumn2<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_bulidFields01.txt"<br>
     * (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     * 期待値：(状態変化) Class.getMethod(String):呼ばれない<br>
     * (状態変化) this.methods:要素を持たないMethod配列インスタンス<br>
     * <br>
     * 正常。<br>
     * フィールドclazzが@InputFileColumn設定なしのフィールド（３つ）のみ持つ場合、this.methodsにメソッド情報が生成されないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildMethods05() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub53> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub53>(
                fileName, AbstractFileLineIterator_Stub53.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化で設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildMethods");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Method[] methods = (Method[]) ReflectionTestUtils.getField(fileLineIterator,
                "methods");
        assertEquals(0, methods.length);
    }

    /**
     * testBuildMethods06() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - 全項目：デフォルト値<br>
     * ・@InputFileColumn設定あり・なしのフィールドを持つ<br>
     * - フィールド：String column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：String noMappingColumn1<br>
     *                        - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexが重複しない。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     *                        期待値：(状態変化) Class.getMethod(String):2回呼ばれる<br>
     *                        (状態変化) this.methods:以下の要素を持つMethod配列インスタンス<br>
     *                        ・[0]：setColumn1(String)のメソッドインスタンス<br>
     *                        ・[1]：setColumn2(String)のメソッドインスタンス<br>
     * <br>
     *                        ※メソッド名と引数の型が確認ポイント<br>
     * <br>
     *                        正常。<br>
     *                        フィールドclazzが@InputFileColumn設定ある・なしのフィールド（
     *                        ３つ）を両方持ち、@設定ありフィールドのセッタメソッドが存在する場合、this.methodsに＠設定あるフィールドに対するメソッド情報のみ生成されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildMethods06() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub54> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub54>(
                fileName, AbstractFileLineIterator_Stub54.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化で設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildMethods");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Method[] methods = (Method[]) ReflectionTestUtils.getField(fileLineIterator,
                "methods");
        assertEquals(2, methods.length);
        assertEquals("setColumn1", methods[0].getName());
        Class[] method01_param = methods[0].getParameterTypes();
        assertEquals(1, method01_param.length);
        assertSame(String.class, method01_param[0]);
        assertEquals("setColumn2", methods[1].getName());
        Class[] method02_param = methods[1].getParameterTypes();
        assertEquals(1, method01_param.length);
        assertSame(String.class, method02_param[0]);
    }

    /**
     * testBuildMethods07() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.clazz:以下の設定を持つインスタンス化可能Classのインスタンス<br>
     * ・@FileFormatの設定を持つ<br>
     * - encloseChar："\""<br>
     * - その他項目：デフォルト値<br>
     * ・@InputFileColumn設定ありのフィールドを持つ<br>
     * - フィールド：String column2<br>
     * @InputFileColumn設定<br> > columnIndex：1<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：int column1<br>
     * @InputFileColumn設定<br> > columnIndex：0<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：Date column4<br>
     * @InputFileColumn設定<br> > columnIndex：3<br>
     *                        > columnFormat：yyyy/MM/dd<br>
     *                        > その他項目：デフォルト値<br>
     *                        - フィールド：BigDecimal column3<br>
     * @InputFileColumn設定<br> > columnIndex：2<br>
     *                        > columnFormat：###,###<br>
     *                        > その他項目：デフォルト値<br>
     *                        ※columnIndexが重複しない。<br>
     *                        (状態) this.fileName:Stringインスタンス<br>
     *                        "AbstractFileLineIterator_bulidFields01.txt"<br>
     *                        (状態) #buildFieldsの実行:テスト前に#buildFields()を実行する。<br>
     * <br>
     *                        期待値：(状態変化) Class.getMethod(String):4回呼ばれる<br>
     *                        (状態変化) this.methods:以下の要素を持つMethod配列インスタンス<br>
     *                        ・[0]：setColumn1(int)のメソッドインスタンス<br>
     *                        ・[1]：setColumn2(String)のメソッドインスタンス<br>
     *                        ・[2]：setColumn3(BigDecimal)のメソッドインスタンス<br>
     *                        ・[3]：setColumn4(Date)のメソッドインスタンス<br>
     * <br>
     *                        ※メソッド名と引数の型が確認ポイント<br>
     * <br>
     *                        正常。<br>
     *                        フィールドclazzが@InputFileColumn設定なしのフィールド（４つ）のみ持ち、そのフィールドに対するセッタメソッドが存在する場合、this.methodsにフィールドに対するセッタメソ
     *                        ッ ド の 情 報 が 生 成 さ れ る こ と を 確 認 す る 。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildMethods07() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_bulidFields01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        columnParserMap.put("java.util.Date", new DateColumnParser());
        columnParserMap.put("java.math.BigDecimal", new DecimalColumnParser());
        columnParserMap.put("int", new IntColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub55> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub55>(
                fileName, AbstractFileLineIterator_Stub55.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildFields");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // テスト対象のインスタンス化で設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildMethods");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Method[] methods = (Method[]) ReflectionTestUtils.getField(fileLineIterator,
                "methods");
        assertEquals(4, methods.length);
        assertEquals("setColumn1", methods[0].getName());
        Class[] method01_param = methods[0].getParameterTypes();
        assertEquals(1, method01_param.length);
        assertSame(int.class, method01_param[0]);

        assertEquals("setColumn2", methods[1].getName());
        Class[] method02_param = methods[1].getParameterTypes();
        assertEquals(1, method02_param.length);
        assertSame(String.class, method02_param[0]);

        assertEquals("setColumn3", methods[2].getName());
        Class[] method03_param = methods[2].getParameterTypes();
        assertEquals(1, method03_param.length);
        assertSame(BigDecimal.class, method03_param[0]);

        assertEquals("setColumn4", methods[3].getName());
        Class[] method04_param = methods[3].getParameterTypes();
        assertEquals(1, method04_param.length);
        assertSame(Date.class, method04_param[0]);
    }

    /**
     * testBuildHeader01() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_buildHeader01.txt"<br>
     * (状態) this.headerLineCount:0<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"１行目データ"<br>
     * ・"２行目データ"<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.header:要素を持たないArrayList<String>インスタンス<br>
     * (状態) this.hasNext(）:this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(状態変化) this.header:要素を持たないArrayList<String>インスタンス<br>
     * (状態変化) LineReader.readLine():呼ばれない<br>
     * <br>
     * 正常パターン。<br>
     * ヘッダ部がない場合はthis.headerが空のことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildHeader01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildHeader01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub60> fileLineIterator = PowerMockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub60>(
                fileName, AbstractFileLineIterator_Stub60.class,
                columnParserMap));
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        LineReader mockLineReader = Mockito.mock(LineReader.class);
        Mockito.doReturn("").when(mockLineReader).readLine();
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", mockLineReader);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildHeader");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object header_object = ReflectionTestUtils.getField(fileLineIterator,
                "header");
        assertEquals(ArrayList.class, header_object.getClass());
        List header = (List) header_object;
        assertEquals(0, header.size());

        Object lineReader = ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(mockLineReader, Mockito.never()).readLine();
    }

    /**
     * testBuildHeader02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_buildHeader01.txt"<br>
     * (状態) this.headerLineCount:1<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"１行目データ"<br>
     * ・"２行目データ"<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.header:要素を持たないArrayList<String>インスタンス<br>
     * (状態) this.hasNext(）:this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(状態変化) this.header:以下の情報を持つArrayList<String>インスタンス<br>
     * ・[0]："１行目データ"<br>
     * (状態変化) LineReader.readLine():1回呼ばれる<br>
     * <br>
     * 正常パターン。<br>
     * ヘッダ部が1行の場合、this.headerに1行の情報が格納されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildHeader02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildHeader01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub61> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub61>(
                fileName, AbstractFileLineIterator_Stub61.class,
                columnParserMap);
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        LineReader mockLineReader = Mockito.mock(LineReader.class);
        Mockito.doReturn("1行目データ").when(mockLineReader).readLine();
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", mockLineReader);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildHeader");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object header_object = ReflectionTestUtils.getField(fileLineIterator,
                "header");
        assertEquals(ArrayList.class, header_object.getClass());
        List header = (List) header_object;
        assertEquals(1, header.size());
        assertEquals("1行目データ", header.get(0));

        Object lineReader = ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(mockLineReader).readLine();
    }

    /**
     * testBuildHeader03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_buildHeader01.txt"<br>
     * (状態) this.headerLineCount:3<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"１行目データ"<br>
     * ・"２行目データ"<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.header:要素を持たないArrayList<String>インスタンス<br>
     * (状態) this.hasNext(）:this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(状態変化) this.header:以下の情報を持つArrayList<String>インスタンス<br>
     * ・[0]："１行目データ"<br>
     * ・[1]："２行目データ"<br>
     * ・[2]："３行目データ"<br>
     * (状態変化) LineReader.readLine():3回呼ばれる<br>
     * <br>
     * 正常パターン。<br>
     * ヘッダ部が3行の場合、this.headerに3行の情報が格納されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildHeader03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildHeader01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub62> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub62>(
                fileName, AbstractFileLineIterator_Stub62.class,
                columnParserMap);
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        LineReader mockLineReader = Mockito.mock(LineReader.class);
        Mockito.doReturn("1行目データ").doReturn("2行目データ").doReturn("3行目データ").when(mockLineReader).readLine();
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", mockLineReader);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildHeader");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object header_object = ReflectionTestUtils.getField(fileLineIterator,
                "header");
        assertEquals(ArrayList.class, header_object.getClass());
        List header = (List) header_object;
        assertEquals(3, header.size());
        assertEquals("1行目データ", header.get(0));
        assertEquals("2行目データ", header.get(1));
        assertEquals("3行目データ", header.get(2));

        Object lineReader = ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(mockLineReader, Mockito.times(3)).readLine();
    }

    /**
     * testBuildHeader04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_buildHeader02.txt"<br>
     * (状態) this.headerLineCount:3<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"１行目データ"<br>
     * ・"２行目データ"<br>
     * <br>
     * ※ヘッダ行よりデータファイルの行数が少ない。<br>
     * (状態) this.header:要素を持たないArrayList<String>インスタンス<br>
     * (状態) this.hasNext(）:this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(状態変化) LineReader.readLine():2回呼ばれる<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："The data which can be acquired doesn't exist."<br>
     * ・原因例外：NoSuchElementException<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 例外<br>
     * ヘッダ部の行数より対象データの行数が少ない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildHeader04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildHeader02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub62> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub62>(
                fileName, AbstractFileLineIterator_Stub62.class,
                columnParserMap);
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator, "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        try {
            method = AbstractFileLineIterator.class.getDeclaredMethod("buildHeader");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionがスローされませんでした");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(FileException.class, e.getTargetException().getClass());
            assertEquals("The data which can be acquired doesn't exist.", e.getTargetException()
                    .getMessage());
            assertEquals(NoSuchElementException.class, e.getTargetException().getCause().getClass());
            assertSame(fileName, ((FileException)e.getTargetException()).getFileName());

            lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                    "lineReader");
            Mockito.verify(lineReader, Mockito.times(2)).readLine();
        }
    }

    /**
     * testBuildHeader05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_buildHeader01.txt"<br>
     * (状態) this.headerLineCount:1<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"１行目データ"<br>
     * ・"２行目データ"<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.header:要素を持たないArrayList<String>インスタンス<br>
     * (状態) this.hasNext(）:this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():FileExceptionが発生する。<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："Error occurred by reading processing of a File."<br>
     * ・原因例外：FileException(LineReader.readLine()の結果インスタンス)<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 例外<br>
     * ヘッダ部の行データ取得でFileExceptionが発生した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildHeader05() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildHeader01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub61> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub61>(
                fileName, AbstractFileLineIterator_Stub61.class,
                columnParserMap);
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // 引数の設定
        // なし

        // 前提条件の設定
        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        FileException exception = new FileException("readLineからの例外です");
        lineReader = Mockito.spy(lineReader);
        Mockito.doThrow(exception).when(lineReader).readLine();
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // テスト対象のインスタンス化で設定済み

        // テスト実施
        try {
            method = AbstractFileLineIterator.class.getDeclaredMethod("buildHeader");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionがスローされませんでした");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(FileException.class, e.getTargetException().getClass());
            assertEquals("Error occurred by reading processing of a File.", e.getTargetException()
                    .getMessage());
            assertSame(exception, e.getTargetException().getCause());
            assertEquals(fileName, ((FileException)e.getTargetException()).getFileName());
        }
    }

    /**
     * testBuildHeader06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_buildHeader01.txt"<br>
     * (状態) this.headerLineCount:1<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"１行目データ"<br>
     * ・"２行目データ"<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.header:要素を持たないArrayList<String>インスタンス<br>
     * (状態) this.hasNext(）:FileExceptionが発生する。<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(状態変化) 例外:this.hasNext(）で発生したFileExceptionがそのままスローされることを確認する。<br>
     * <br>
     * 例外<br>
     * 対象ファイルの処理対象行確認チェックでエラーが発生した場合、その例外がそのまま返されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildHeader06() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildHeader01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub61> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub61>(
                fileName, AbstractFileLineIterator_Stub61.class,
                columnParserMap));
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // 引数の設定
        // なし

        // 前提条件の設定
        FileException exception = new FileException("readLineからの例外です");
        Mockito.doThrow(exception).when(fileLineIterator).hasNext();
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        try {
            method = AbstractFileLineIterator.class.getDeclaredMethod("buildHeader");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionがスローされませんでした");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(exception, e.getTargetException());
        }
    }

    /**
     * testBuildTrailerQueue01() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_getTrailer01.txt"<br>
     * (状態) this.trailerLineCount:0<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.trailerQueue:null<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(状態変化) this.trailerQueue:null<br>
     * (状態変化) LineReader.readLine():呼ばれない<br>
     * <br>
     * 正常パターン。（トレイラ部0行）<br>
     * トレイラ部がない場合、トレイラキューが生成されないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildTrailerQueue01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_getTrailer01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub70> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub70>(
                fileName, AbstractFileLineIterator_Stub70.class,
                columnParserMap);
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildTrailerQueue");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object trailerQueue_object = ReflectionTestUtils.getField(fileLineIterator,
                "trailerQueue");
        assertNull(trailerQueue_object);

//        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
//                "lineReader");
        Mockito.verify(lineReader, Mockito.never()).readLine();
    }

    /**
     * testBuildTrailerQueue02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_getTrailer01.txt"<br>
     * (状態) this.trailerLineCount:1<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.trailerQueue:null<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(状態変化) this.trailerQueue:以下の要素を持つArrayBlockingQueue<String>インスタンス<br>
     * ・"３行目データ"<br>
     * (状態変化) LineReader.readLine():1回呼ばれる<br>
     * <br>
     * 正常パターン。（トレイラ部1行）<br>
     * トレイラ部が1行ある場合、トレイラキューが生成され、その中にデータ部の1行データが格納されていることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildTrailerQueue02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_getTrailer01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub71> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub71>(
                fileName, AbstractFileLineIterator_Stub71.class,
                columnParserMap);
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化で設定済み


        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildTrailerQueue");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object trailerQueue_object = ReflectionTestUtils.getField(fileLineIterator,
                "trailerQueue");
        assertEquals(ArrayBlockingQueue.class, trailerQueue_object.getClass());
        ArrayBlockingQueue trailerQueue = (ArrayBlockingQueue) trailerQueue_object;
        assertEquals(1, trailerQueue.size());
        assertEquals("3行目データ", trailerQueue.poll());

        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(lineReader).readLine();
    }

    /**
     * testBuildTrailerQueue03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_getTrailer01.txt"<br>
     * (状態) this.trailerLineCount:3<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.trailerQueue:null<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(状態変化) this.trailerQueue:以下の要素を持つArrayBlockingQueue<String>インスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態変化) LineReader.readLine():3回呼ばれる<br>
     * <br>
     * 正常パターン。（トレイラ部3行）<br>
     * トレイラ部が3行ある場合、トレイラキューが生成され、その中にデータ部の3行データが格納されていることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildTrailerQueue03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_getTrailer01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub72> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub72>(
                fileName, AbstractFileLineIterator_Stub72.class,
                columnParserMap);
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        method = AbstractFileLineIterator.class.getDeclaredMethod("buildTrailerQueue");
        method.setAccessible(true);
        result = method.invoke(fileLineIterator);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Object trailerQueue_object = ReflectionTestUtils.getField(fileLineIterator,
                "trailerQueue");
        assertEquals(ArrayBlockingQueue.class, trailerQueue_object.getClass());
        ArrayBlockingQueue trailerQueue = (ArrayBlockingQueue) trailerQueue_object;
        assertEquals(3, trailerQueue.size());
        assertEquals("3行目データ", trailerQueue.poll());
        assertEquals("4行目データ", trailerQueue.poll());
        assertEquals("5行目データ", trailerQueue.poll());

        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(lineReader, Mockito.times(3)).readLine();
    }

    /**
     * testBuildTrailerQueue04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_getTrailer01.txt"<br>
     * (状態) this.trailerLineCount:1<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.trailerQueue:null<br>
     * (状態) this.hasNext():FileException例外が発生する。<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(状態変化) this.trailerQueue:要素を持たないArrayBlockingQueue<String>インスタンス<br>
     * (状態変化) LineReader.readLine():呼ばれない<br>
     * (状態変化) 例外:this.hasNext(）で発生したFileExceptionがそのままスローされることを確認する。<br>
     * <br>
     * 例外。（トレイラ部1行）<br>
     * 次の処理対象行に対する存在チェック処理でエラーが発生した場合、その例外がそのまま返されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testBuildTrailerQueue04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_getTrailer01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub71> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub71>(
                fileName, AbstractFileLineIterator_Stub71.class,
                columnParserMap));
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化で設定済み
        FileException exception = new FileException("hasNextでのエラーです");
        Mockito.doThrow(exception).when(fileLineIterator).hasNext();

        // テスト実施
        try {
            method = AbstractFileLineIterator.class.getDeclaredMethod("buildTrailerQueue");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionがスローされませんでした");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            Object trailerQueue_object = ReflectionTestUtils.getField(
                    fileLineIterator, "trailerQueue");
            assertEquals(ArrayBlockingQueue.class, trailerQueue_object
                    .getClass());
            ArrayBlockingQueue trailerQueue = (ArrayBlockingQueue) trailerQueue_object;
            assertEquals(0, trailerQueue.size());

            lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                    "lineReader");
            Mockito.verify(lineReader, Mockito.never()).readLine();

            assertSame(exception, e.getTargetException());
        }
    }

    /**
     * testBuildTrailerQueue05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_getTrailer01.txt"<br>
     * (状態) this.trailerLineCount:1<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.trailerQueue:null<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():FileException例外が発生する。<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："Error occurred by reading processing of a File."<br>
     * ・原因例外：FileException(LineReader.readLine()の結果インスタンス)<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 例外。（トレイラ部1行）<br>
     * トレイラ部の行データ取得でFileExceptionが発生した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildTrailerQueue05() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_getTrailer01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub71> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub71>(
                fileName, AbstractFileLineIterator_Stub71.class,
                columnParserMap);
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化で設定済み
        FileException exception = new FileException("readLineでのエラーです");
//        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
//                "lineReader");
        Mockito.doThrow(exception).when(lineReader).readLine();
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // テスト実施
        try {
            method = AbstractFileLineIterator.class.getDeclaredMethod("buildTrailerQueue");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionがスローされませんでした");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(FileException.class, e.getTargetException().getClass());
            assertEquals("Error occurred by reading processing of a File.", e.getTargetException()
                    .getMessage());
            assertSame(exception, e.getTargetException().getCause());
            assertEquals(fileName, ((FileException)e.getTargetException()).getFileName());
        }
    }

    /**
     * testBuildTrailerQueue06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_buildTrailerQueue02.txt"<br>
     * (状態) this.trailerLineCount:3<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * <br>
     * ※トレイラ行よりデータファイルの行数が少ない。<br>
     * (状態) this.trailerQueue:null<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："The data which can be acquired doesn't exist."<br>
     * ・原因例外：NoSuchElementException<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * トレイラ部の行数より対象データの行数が少ない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testBuildTrailerQueue06() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_buildTrailerQueue02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub72> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub72>(
                fileName, AbstractFileLineIterator_Stub72.class,
                columnParserMap);
        Method method = AbstractFileLineIterator.class.getDeclaredMethod("buildLineReader");
        method.setAccessible(true);
        Object result = method.invoke(fileLineIterator);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        try {
            method = AbstractFileLineIterator.class.getDeclaredMethod("buildTrailerQueue");
            method.setAccessible(true);
            method.invoke(fileLineIterator);
            fail("FileExceptionがスローされませんでした");
        } catch (InvocationTargetException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(FileException.class, e.getTargetException().getClass());
            assertEquals("The data which can be acquired doesn't exist.", e.getTargetException()
                    .getMessage());
            assertEquals(NoSuchElementException.class, e.getTargetException().getCause().getClass());
            assertEquals(fileName, ((FileException)e.getTargetException()).getFileName());
        }
    }

    /**
     * testCloseFile01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_closeFile01.txt"<br>
     * (状態) this.reader:Readerインスタンス<br>
     * (状態) Reader.close():正常終了<br>
     * <br>
     * 期待値：(状態変化) reader.close():呼ばれる<br>
     * <br>
     * 正常<br>
     * クローズ処理が正しく実行されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCloseFile01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_closeFile01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap);
        fileLineIterator.init();

        BufferedReader bufferedReader = (BufferedReader) ReflectionTestUtils.getField(fileLineIterator, "reader");
        bufferedReader = Mockito.spy(bufferedReader);
        ReflectionTestUtils.setField(fileLineIterator, "reader", bufferedReader);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        fileLineIterator.closeFile();

        // 返却値の確認
        // なし

        // 状態変化の確認
        Mockito.verify(bufferedReader).close();
    }

    /**
     * testCloseFile02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_closeFile01.txt"<br>
     * (状態) this.reader:Readerインスタンス<br>
     * (状態) Reader.close():IOException例外が発生<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："Processing of reader was failed."<br>
     * ・原因例外：IOException(Reader.close()からのもの)<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 例外<br>
     * クロース処理でIOExceptionが発生した場合、FileExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCloseFile02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_closeFile01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap);
        fileLineIterator.init();

        BufferedReader bufferedReader = (BufferedReader) ReflectionTestUtils.getField(fileLineIterator, "reader");
        bufferedReader = Mockito.spy(bufferedReader);
        ReflectionTestUtils.setField(fileLineIterator, "reader", bufferedReader);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化で設定済み
        IOException exception = new IOException();
        Mockito.doThrow(exception).when(bufferedReader).close();

        // テスト実施
        try {
            fileLineIterator.closeFile();
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(FileException.class, e.getClass());
            assertEquals("Processing of reader was failed.", e.getMessage());
            assertSame(exception, e.getCause());
            assertEquals(fileName, e.getFileName());
        }
    }

    /**
     * testGetHeader01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.header:以下の情報を持つArrayList<String>インスタンス<br>
     * １．"ヘッダ行1"<br>
     * ２．"ヘッダ行2"<br>
     * ３．"ヘッダ行3"<br>
     * <br>
     * 期待値：(戻り値) List<String>:以下の情報を持つArrayList<String>インスタンス<br>
     * １．"ヘッダ行1"<br>
     * ２．"ヘッダ行2"<br>
     * ３．"ヘッダ行3"<br>
     * <br>
     * 正常パターン。<br>
     * ヘッダ部の情報が正しく取得されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetHeader01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub07>(
                fileName, AbstractFileLineIterator_Stub07.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        List<String> header = new ArrayList<String>();
        header.add("ヘッダ行1");
        header.add("ヘッダ行2");
        header.add("ヘッダ行3");
        ReflectionTestUtils.setField(fileLineIterator, "header", header);

        // テスト実施
        List result = fileLineIterator.getHeader();

        // 返却値の確認
        assertEquals(ArrayList.class, result.getClass());
        assertEquals("ヘッダ行1", result.get(0));
        assertEquals("ヘッダ行2", result.get(1));
        assertEquals("ヘッダ行3", result.get(2));

        // 状態変化の確認
        // なし
    }

    /**
     * testGetTrailer01() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_getTrailer01.txt"<br>
     * (状態) this.readTrailer:false<br>
     * (状態) this.trailerLineCount:0<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.trailerQueue:要素を持たないQueue<String>インスタンス<br>
     * (状態) this.trailer:要素を持たないArrayList<String>インスタンス<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(戻り値) List<String>:要素を持たないArrayList<String>インスタンス<br>
     * <br>
     * ※this.trailerと同じインスタンス<br>
     * (状態変化) this.trailer:要素を持たないArrayList<String>インスタンス<br>
     * (状態変化) this.readTrailer:true<br>
     * (状態変化) LineReader.readLine():3回呼ばれる<br>
     * <br>
     * 正常パターン。（トレイラ部0行）<br>
     * トレイラ部がない場合は結果が空のことを確認する。<br>
     * また、データ行の情報が全部飛ばされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetTrailer01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_getTrailer01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80>(
                fileName, AbstractFileLineIterator_Stub80.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        fileLineIterator.init();
        // テスト対象のインスタンス化で設定済み

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // テスト実施
        List<String> result = fileLineIterator.getTrailer();

        // 返却値の確認
        assertSame(ArrayList.class, result.getClass());
        assertEquals(0, result.size());

        // 状態変化の確認
        Object trailer_object = ReflectionTestUtils.getField(fileLineIterator,
                "trailer");
        assertSame(ArrayList.class, trailer_object.getClass());
        assertEquals(0, ((List) trailer_object).size());
        assertTrue((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                "readTrailer"));

        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(lineReader, Mockito.times(3)).readLine();
    }

    /**
     * testGetTrailer02() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_getTrailer02.txt"<br>
     * (状態) this.readTrailer:false<br>
     * (状態) this.trailerLineCount:1<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"２行目データ"<br>
     * (状態) this.trailer:要素を持たないArrayList<String>インスタンス<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(戻り値) List<String>:以下の要素を持つArrayList<String>インスタンス<br>
     * ・"５行目データ"<br>
     * <br>
     * ※this.trailerと同じインスタンス<br>
     * (状態変化) this.trailer:以下の要素を持つArrayList<String>インスタンス<br>
     * ・"５行目データ"<br>
     * (状態変化) this.readTrailer:true<br>
     * (状態変化) LineReader.readLine():3回呼ばれる<br>
     * <br>
     * 正常パターン。（トレイラ部1行）<br>
     * データ行を全部読んでない場合(複数行)にトレイラ部を取得する場合、正しくthis.trailer情報が返されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetTrailer02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_getTrailer02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81>(
                fileName, AbstractFileLineIterator_Stub81.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        fileLineIterator.init();
        // テスト対象のインスタンス化で設定済み

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // テスト実施
        List<String> result = fileLineIterator.getTrailer();

        // 返却値の確認
        assertSame(ArrayList.class, result.getClass());
        assertEquals(1, result.size());
        assertEquals("5行目データ", result.get(0));

        // 状態変化の確認
        Object trailer_object = ReflectionTestUtils.getField(fileLineIterator,
                "trailer");
        assertSame(ArrayList.class, trailer_object.getClass());
        List<String> trailer = (List<String>) trailer_object;
        assertEquals(1, trailer.size());
        assertEquals("5行目データ", trailer.get(0));

        assertTrue((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                "readTrailer"));

        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(lineReader, Mockito.times(3)).readLine();
    }

    /**
     * testGetTrailer03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_getTrailer02.txt"<br>
     * (状態) this.readTrailer:false<br>
     * (状態) this.trailerLineCount:3<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"５行目データ"<br>
     * (状態) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"２行目データ"<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * (状態) this.trailer:要素を持たないArrayList<String>インスタンス<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(戻り値) List<String>:以下の要素を持つArrayList<String>インスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * <br>
     * ※this.trailerと同じインスタンス<br>
     * (状態変化) this.trailer:以下の要素を持つArrayList<String>インスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態変化) this.readTrailer:true<br>
     * (状態変化) LineReader.readLine():1回呼ばれる<br>
     * <br>
     * 正常パターン。（トレイラ部3行）<br>
     * データ行を全部読んでない場合(複数行)にトレイラ部を取得する場合、正しくthis.trailer情報が返されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetTrailer03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_getTrailer02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub82> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub82>(
                fileName, AbstractFileLineIterator_Stub82.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        fileLineIterator.init();
        // テスト対象のインスタンス化で設定済み

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // テスト実施
        List<String> result = fileLineIterator.getTrailer();

        // 返却値の確認
        assertSame(ArrayList.class, result.getClass());
        assertEquals(3, result.size());
        assertEquals("3行目データ", result.get(0));
        assertEquals("4行目データ", result.get(1));
        assertEquals("5行目データ", result.get(2));

        // 状態変化の確認
        Object trailer_object = ReflectionTestUtils.getField(fileLineIterator,
                "trailer");
        assertSame(ArrayList.class, trailer_object.getClass());
        List<String> trailer = (List<String>) trailer_object;
        assertEquals(3, trailer.size());
        assertEquals("3行目データ", trailer.get(0));
        assertEquals("4行目データ", trailer.get(1));
        assertEquals("5行目データ", trailer.get(2));

        assertTrue((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                "readTrailer"));

        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(lineReader).readLine();

    }

    /**
     * testGetTrailer04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_getTrailer02.txt"<br>
     * (状態) this.readTrailer:false<br>
     * (状態) this.trailerLineCount:1<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"２行目データ"<br>
     * (状態) this.trailer:要素を持たないArrayList<String>インスタンス<br>
     * (状態) this.hasNext():FileException例外が発生する。<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(状態変化) this.trailer:要素を持たないArrayList<String>インスタンス<br>
     * (状態変化) this.readTrailer:false<br>
     * (状態変化) LineReader.readLine():呼ばれない<br>
     * (状態変化) なし:this.hasNext(）で発生したFileExceptionがそのままスローされることを確認する。<br>
     * <br>
     * 例外。（トレイラ部1行）<br>
     * 次の処理対象行に対する存在チェック処理でエラーが発生した場合、その例外がそのまま返されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetTrailer04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_getTrailer02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81>(
                fileName, AbstractFileLineIterator_Stub81.class,
                columnParserMap));

        // 引数の設定
        // なし

        // 前提条件の設定
        fileLineIterator.init();

        FileException exception = new FileException("hasNextでのエラーです");
        Mockito.doThrow(exception).when(fileLineIterator).hasNext();
        // テスト対象のインスタンス化で設定済み

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // テスト実施
        try {
            fileLineIterator.getTrailer();
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            Object trailer_object = ReflectionTestUtils.getField(fileLineIterator,
                    "trailer");
            assertSame(ArrayList.class, trailer_object.getClass());
            List<String> trailer = (List<String>) trailer_object;
            assertEquals(0, trailer.size());

            assertFalse((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                    "readTrailer"));

            lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                    "lineReader");
            Mockito.verify(lineReader, Mockito.never()).readLine();

            assertSame(exception, e);
        }
    }

    /**
     * testGetTrailer05() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_getTrailer02.txt"<br>
     * (状態) this.readTrailer:true<br>
     * (状態) this.trailerLineCount:1<br>
     * (状態) this.lineReader:情報を持たないLineReaderインスタンス<br>
     * (状態) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"５行目データ"<br>
     * (状態) this.trailer:以下の要素を持つArrayList<String>インスタンス<br>
     * ・"５行目データ"<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(戻り値) List<String>:以下の要素を持つArrayList<String>インスタンス<br>
     * ・"５行目データ"<br>
     * <br>
     * ※this.trailerと同じインスタンス<br>
     * (状態変化) this.trailer:以下の要素を持つArrayList<String>インスタンス<br>
     * ・"５行目データ"<br>
     * (状態変化) this.readTrailer:true<br>
     * (状態変化) LineReader.readLine():呼ばれない<br>
     * <br>
     * 正常パターン。（トレイラ部1行）<br>
     * トレイラ部が既に生成されている場合、キャッシュされているトレイラ部がそのまま返されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetTrailer05() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_getTrailer02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81>(
                fileName, AbstractFileLineIterator_Stub81.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        fileLineIterator.init();
        fileLineIterator.getTrailer();
        // テスト対象のインスタンス化で設定済み

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // テスト実施
        List<String> result = fileLineIterator.getTrailer();

        // 返却値の確認
        assertSame(ArrayList.class, result.getClass());
        assertEquals(1, result.size());
        assertEquals("5行目データ", result.get(0));

        // 状態変化の確認
        Object trailer_object = ReflectionTestUtils.getField(fileLineIterator,
                "trailer");
        assertSame(ArrayList.class, trailer_object.getClass());
        List<String> trailer = (List<String>) trailer_object;
        assertEquals(1, trailer.size());
        assertEquals("5行目データ", trailer.get(0));

        assertTrue((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                "readTrailer"));

        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(lineReader, Mockito.never()).readLine();
    }

    /**
     * testGetTrailer06() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_getTrailer03.txt"<br>
     * (状態) this.readTrailer:false<br>
     * (状態) this.trailerLineCount:1<br>
     * (状態) this.lineReader:情報を持たないLineReaderインスタンス<br>
     * (状態) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"５行目データ"<br>
     * (状態) this.trailer:要素を持たないArrayList<String>インスタンス<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(戻り値) List<String>:以下の要素を持つArrayList<String>インスタンス<br>
     * ・"５行目データ"<br>
     * <br>
     * ※this.trailerと同じインスタンス<br>
     * (状態変化) this.trailer:以下の要素を持つArrayList<String>インスタンス<br>
     * ・"５行目データ"<br>
     * (状態変化) this.readTrailer:true<br>
     * (状態変化) LineReader.readLine():呼ばれない<br>
     * <br>
     * 正常パターン。（トレイラ部1行）<br>
     * データ行を全部読んだ後にトレイラ部を取得する場合、正しくthis.trailer情報が返されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetTrailer06() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_getTrailer03.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81>(
                fileName, AbstractFileLineIterator_Stub81.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        fileLineIterator.init();
        // テスト対象のインスタンス化で設定済み

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // テスト実施
        List<String> result = fileLineIterator.getTrailer();

        // 返却値の確認
        assertSame(ArrayList.class, result.getClass());
        assertEquals(1, result.size());
        assertEquals("5行目データ", result.get(0));

        // 状態変化の確認
        Object trailer_object = ReflectionTestUtils.getField(fileLineIterator,
                "trailer");
        assertSame(ArrayList.class, trailer_object.getClass());
        List<String> trailer = (List<String>) trailer_object;
        assertEquals(1, trailer.size());
        assertEquals("5行目データ", trailer.get(0));

        assertTrue((Boolean) ReflectionTestUtils.getField(fileLineIterator,
                "readTrailer"));

        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(lineReader, Mockito.never()).readLine();
    }

    /**
     * testGetTrailer07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_getTrailer02.txt"<br>
     * (状態) this.readTrailer:false<br>
     * (状態) this.trailerLineCount:1<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"２行目データ"<br>
     * (状態) this.trailer:要素を持たないArrayList<String>インスタンス<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():FileException例外が発生する。<br>
     * <br>
     * 期待値：(状態変化) なし:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："Processing of lineReader was failed."<br>
     * ・原因例外：LineReader.readLine()でスローされた例外<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 例外。（トレイラ部1行）<br>
     * 次の処理対象行に対する存在チェック処理でエラーが発生した場合、その例外がそのまま返されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetTrailer07() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_getTrailer02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81>(
                fileName, AbstractFileLineIterator_Stub81.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        fileLineIterator.init();
        FileException exception = new FileException("readLineでのエラーです");
        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        Mockito.doThrow(exception).when(lineReader).readLine();
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // テスト対象のインスタンス化で設定済み

        // テスト実施
        try {
            fileLineIterator.getTrailer();
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(FileException.class, e.getClass());
            assertEquals("Processing of lineReader was failed.", e.getMessage());
            assertSame(exception, e.getCause());
            assertEquals(fileName, e.getFileName());
        }
    }

    /**
     * testReadLine01() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_skip01.txt"<br>
     * (状態) this.trailerQueue:null<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"１行目データ"<br>
     * ・"２行目データ"<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * ・"６行目データ"<br>
     * ・"７行目データ"<br>
     * (状態) this.trailerLineCount:0<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(戻り値) String:"１行目データ"<br>
     * (状態変化) this.trailerQueue:null<br>
     * (状態変化) LineReader.readLine():1回呼ばれる<br>
     * <br>
     * 正常パターン<br>
     * (トレイラ部なし)。<br>
     * トレイラ部がない場合、データ部のデータ1行分が返されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80>(
                fileName, AbstractFileLineIterator_Stub80.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        fileLineIterator.init();
        // テスト対象のインスタンス化で設定済み

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // テスト実施
        String result = fileLineIterator.readLine();

        // 返却値の確認
        assertEquals("1行目データ", result);

        // 状態変化の確認
        assertNull(ReflectionTestUtils.getField(fileLineIterator, "trailerQueue"));

        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(lineReader).readLine();
    }

    /**
     * testReadLine02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_readLine02.txt"<br>
     * (状態) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"３行目データ"<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * ・"６行目データ"<br>
     * ・"７行目データ"<br>
     * (状態) this.trailerLineCount:1<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(戻り値) String:"３行目データ"<br>
     * (状態変化) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"４行目データ"<br>
     * (状態変化) Queue#add():1回呼ばれる<br>
     * (状態変化) LineReader.readLine():1回呼ばれる<br>
     * <br>
     * 正常パターン<br>
     * (トレイラ部1行、２回目以後の実行)。<br>
     * トレイラ部が有るデータに対してreadLine()を1回以上実行した後にreadLine()を実行した場合、トレイラキューの内容が更新されキューの最初のデータがデータ部のデータとして返されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testReadLine02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_readLine02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81>(
                fileName, AbstractFileLineIterator_Stub81.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        fileLineIterator.init();

        // テスト対象のインスタンス化で設定済み

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        Queue trailerQueueSpy = (Queue) ReflectionTestUtils.getField(fileLineIterator, "trailerQueue");
        trailerQueueSpy = Mockito.spy(trailerQueueSpy);
        ReflectionTestUtils.setField(fileLineIterator, "trailerQueue", trailerQueueSpy);

        // テスト実施
        String result = fileLineIterator.readLine();

        // 返却値の確認
        assertEquals("3行目データ", result);

        // 状態変化の確認
        Object trailerQueue_object = ReflectionTestUtils.getField(fileLineIterator,
                "trailerQueue");
        ArrayBlockingQueue trailerQueue = (ArrayBlockingQueue) trailerQueue_object;
        assertEquals(1, trailerQueue.size());
        assertEquals("4行目データ", trailerQueue.poll());

        Mockito.verify(trailerQueue).add(Mockito.anyObject());

        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(lineReader).readLine();
    }

    /**
     * testReadLine03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_readLine02.txt"<br>
     * (状態) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"６行目データ"<br>
     * ・"７行目データ"<br>
     * (状態) this.trailerLineCount:3<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(戻り値) String:"３行目データ"<br>
     * (状態変化) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * ・"６行目データ"<br>
     * (状態変化) Queue#add():1回呼ばれる<br>
     * (状態変化) LineReader.readLine():1回呼ばれる<br>
     * <br>
     * 正常パターン<br>
     * (トレイラ部複数行、２回目以後の実行)。<br>
     * トレイラ部が有るデータに対してreadLine()を1回以上実行した後にreadLine()を実行した場合、トレイラキューの内容が更新されキューの最初のデータがデータ部のデータとして返されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testReadLine03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_readLine02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub82> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub82>(
                fileName, AbstractFileLineIterator_Stub82.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        fileLineIterator.init();

        // テスト対象のインスタンス化で設定済み

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        Queue trailerQueueSpy = (Queue) ReflectionTestUtils.getField(fileLineIterator, "trailerQueue");
        trailerQueueSpy = Mockito.spy(trailerQueueSpy);
        ReflectionTestUtils.setField(fileLineIterator, "trailerQueue", trailerQueueSpy);

        // テスト実施
        String result = fileLineIterator.readLine();

        // 返却値の確認
        assertEquals("3行目データ", result);

        // 状態変化の確認
        Object trailerQueue_object = ReflectionTestUtils.getField(fileLineIterator,
                "trailerQueue");
        ArrayBlockingQueue trailerQueue = (ArrayBlockingQueue) trailerQueue_object;
        assertEquals(3, trailerQueue.size());
        assertEquals("4行目データ", trailerQueue.poll());
        assertEquals("5行目データ", trailerQueue.poll());
        assertEquals("6行目データ", trailerQueue.poll());

        Mockito.verify(trailerQueue).add(Mockito.anyObject());

        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(lineReader).readLine();
    }

    /**
     * testReadLine04() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_readLine03.txt"<br>
     * (状態) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"７行目データ"<br>
     * (状態) this.lineReader:情報を持たないLineReaderインスタンス<br>
     * (状態) this.trailerLineCount:1<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義 ⇒ false<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(戻り値) String:null<br>
     * (状態変化) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"７行目データ"<br>
     * (状態変化) Queue#add():呼ばれない<br>
     * (状態変化) LineReader.readLine():呼ばれない<br>
     * <br>
     * データ部を全部読んだ後のreadLine()を実行した場合、nullを返すことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testReadLine04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_readLine03.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub81>(
                fileName, AbstractFileLineIterator_Stub81.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        fileLineIterator.init();

        // テスト対象のインスタンス化で設定済み

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        Queue trailerQueueSpy = (Queue) ReflectionTestUtils.getField(fileLineIterator, "trailerQueue");
        trailerQueueSpy = Mockito.spy(trailerQueueSpy);
        ReflectionTestUtils.setField(fileLineIterator, "trailerQueue", trailerQueueSpy);

        // テスト実施
        String result = fileLineIterator.readLine();

        // 返却値の確認
        assertNull(result);

        // 状態変化の確認
        Object trailerQueue_object = ReflectionTestUtils.getField(fileLineIterator,
                "trailerQueue");
        ArrayBlockingQueue trailerQueue = (ArrayBlockingQueue) trailerQueue_object;
        assertEquals(1, trailerQueue.size());
        assertEquals("7行目データ", trailerQueue.poll());

        Mockito.verify(trailerQueue, Mockito.never()).add(Mockito.anyObject());

        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(lineReader, Mockito.never()).readLine();
    }

    /**
     * testReadLine05() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_skip01.txt"<br>
     * (状態) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"4行目データ"<br>
     * ・"5行目データ"<br>
     * ・"6行目データ"<br>
     * ・"7行目データ"<br>
     * (状態) this.lineReader:情報を持たないLineReaderインスタンス<br>
     * (状態) this.trailerLineCount:3<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * <br>
     * 期待値：(戻り値) String:null<br>
     * (状態変化) this.trailerQueue:以下の要素を持つQueue<String>インスタンス<br>
     * ・"4行目データ"<br>
     * ・"5行目データ"<br>
     * ・"6行目データ"<br>
     * ・"7行目データ"<br>
     * (状態変化) Queue#add():呼ばれない<br>
     * (状態変化) LineReader.readLine():呼ばれない<br>
     * <br>
     * 正常パターン。<br>
     * ヘッダ部、トレイラ部は有るがデータ部がないデータに対してreadLine()を実行した場合、nullが返されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testReadLine05() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub83> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub83>(
                fileName, AbstractFileLineIterator_Stub83.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        fileLineIterator.init();
        // テスト対象のインスタンス化で設定済み

        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        lineReader = Mockito.spy(lineReader);
        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        Queue trailerQueueSpy = (Queue) ReflectionTestUtils.getField(fileLineIterator, "trailerQueue");
        trailerQueueSpy = Mockito.spy(trailerQueueSpy);
        ReflectionTestUtils.setField(fileLineIterator, "trailerQueue", trailerQueueSpy);

        // テスト実施
        String result = fileLineIterator.readLine();

        // 返却値の確認
        assertNull(result);

        // 状態変化の確認
        Object trailerQueue_object = ReflectionTestUtils.getField(fileLineIterator,
                "trailerQueue");
        ArrayBlockingQueue trailerQueue = (ArrayBlockingQueue) trailerQueue_object;
        assertEquals(4, trailerQueue.size());
        assertEquals("4行目データ", trailerQueue.poll());
        assertEquals("5行目データ", trailerQueue.poll());
        assertEquals("6行目データ", trailerQueue.poll());
        assertEquals("7行目データ", trailerQueue.poll());

        Mockito.verify(trailerQueue, Mockito.never()).add(Mockito.anyObject());

        lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        Mockito.verify(lineReader, Mockito.never()).readLine();
    }

    /**
     * testReadLine06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:Stringインスタンス<br>
     * "AbstractFileLineIterator_skip01.txt"<br>
     * (状態) this.trailerQueue:null<br>
     * (状態) this.lineReader:以下の情報を持つLineReaderインスタンス<br>
     * ・"１行目データ"<br>
     * ・"２行目データ"<br>
     * ・"３行目データ"<br>
     * ・"４行目データ"<br>
     * ・"５行目データ"<br>
     * ・"６行目データ"<br>
     * ・"７行目データ"<br>
     * (状態) this.trailerLineCount:0<br>
     * (状態) this.hasNext():this.lineReaderの定義に従う<br>
     * <br>
     * ※呼ばれるタイミングにあわせて定義<br>
     * (状態) LineReader.readLine():FileException例外が発生する。<br>
     * <br>
     * 期待値：(状態変化) 例外:以下の情報を持つFileExceptionが発生することを確認する。<br>
     * ・メッセージ："Processing of lineReader was failed."<br>
     * ・原因例外：LineReader.readLine()でスローされた例外<br>
     * ・ファイル名：フィールドfileNameと同じインスタンス。<br>
     * <br>
     * 例外。<br>
     * LineReaderから例外が発生した場合、その例外がFileExceptionにラップされてスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine06() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80>(
                fileName, AbstractFileLineIterator_Stub80.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        fileLineIterator.init();
        LineReader lineReader = (LineReader) ReflectionTestUtils.getField(fileLineIterator,
                "lineReader");
        FileException exception = new FileException("readLineからの例外です");
        lineReader = Mockito.spy(lineReader);
        Mockito.doThrow(exception).when(lineReader).readLine();

        ReflectionTestUtils.setField(fileLineIterator, "lineReader", lineReader);

        // テスト対象のインスタンス化で設定済み

        // テスト実施
        try {
            fileLineIterator.readLine();
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertEquals(FileException.class, e.getClass());
            assertEquals("Processing of lineReader was failed.", e.getMessage());
            assertSame(exception, e.getCause());
            assertEquals(fileName, e.getFileName());
        }
    }

    /**
     * testSkipint01() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(引数) skipLines:0<br>
     * (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
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
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80>(
                fileName, AbstractFileLineIterator_Stub80.class,
                columnParserMap));
        ReflectionTestUtils.setField(fileLineIterator, "currentLineCount", 0);

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
        assertEquals(0, ReflectionTestUtils.getField(fileLineIterator,
                "currentLineCount"));
    }

    /**
     * testSkipint02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(引数) skipLines:1<br>
     * (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
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
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80>(
                fileName, AbstractFileLineIterator_Stub80.class,
                columnParserMap));
        ReflectionTestUtils.setField(fileLineIterator, "currentLineCount", 0);

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
        assertEquals(1, ReflectionTestUtils.getField(fileLineIterator,
                "currentLineCount"));
    }

    /**
     * testSkipint03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(引数) skipLines:3<br>
     * (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.readLine():正常実行<br>
     * <br>
     * 期待値：(状態変化) this.readLine:3回呼ばれる<br>
     * (状態) this.currentLineCount:3<br>
     * <br>
     * 正常パターン。<br>
     * Skip対象行が３行の場合、対象データを３行読むことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSkipint03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80>(
                fileName, AbstractFileLineIterator_Stub80.class,
                columnParserMap));
        ReflectionTestUtils.setField(fileLineIterator, "currentLineCount", 0);

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
        assertEquals(3, ReflectionTestUtils.getField(fileLineIterator,
                "currentLineCount"));
    }

    /**
     * testSkipint04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) skipLines:1<br>
     * (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
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
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80>(
                fileName, AbstractFileLineIterator_Stub80.class,
                columnParserMap));
        ReflectionTestUtils.setField(fileLineIterator, "currentLineCount", 0);

        // 引数の設定
        int skipLines = 1;

        // 前提条件の設定
        fileLineIterator.init();
        FileException exception = new FileException("readLineからの例外です");
        Mockito.doThrow(exception).when(fileLineIterator).readLine();
        // テスト対象のインスタンス化で設定済み

        // テスト実施
        try {
            fileLineIterator.skip(skipLines);
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 例外の確認
            assertSame(exception, e);

            // 状態変化の確認
            assertEquals(0, ReflectionTestUtils.getField(fileLineIterator,
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
     * <br>
     * ※処理対象ファイルの行数を超える設定<br>
     * (状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.readLine():正常実行<br>
     * (状態) this.currentLineCount:0<br>
     * <br>
     * 期待値：(状態変化) this.readLine:7回呼ばれる<br>
     * (状態変化) this.currentLineCount:7<br>
     * ※ファイルのデータ行数<br>
     * (状態変化) なし:以下の設定を持つFileLineExceptionが発生する。<br>
     * ・メッセージ："The data which can be acquired doesn't exist."<br>
     * ・原因例外：NoSuchElementException<br>
     * ・ファイル名：処理対象ファイル名<br>
     * ・行番号：8<br>
     * <br>
     * 例外。<br>
     * Skip対象行の数が対象データの数を越える場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSkipint05() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80> fileLineIterator = Mockito.spy(
                new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80>(
                fileName, AbstractFileLineIterator_Stub80.class,
                columnParserMap));
        ReflectionTestUtils.setField(fileLineIterator, "currentLineCount", 0);

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
            assertEquals(8, e.getLineNo());

            // 状態変化の確認
            Mockito.verify(fileLineIterator, Mockito.times(7)).readLine();

            assertEquals(7, ReflectionTestUtils.getField(fileLineIterator,
                    "currentLineCount"));
        }
    }

    /**
     * testGetLineFeedChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.lineFeedChar:"\r"<br>
     * <br>
     * 期待値：(戻り値) this.lineFeedChar:"\r"<br>
     * <br>
     * lineFeedCharのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetLineFeedChar01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01>(
                fileName, AbstractFileLineIterator_Stub01.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // 前提条件でlineFeedCharに"\r"を設定済み

        // テスト実施
        String result = fileLineIterator.getLineFeedChar();

        // 返却値の確認
        assertEquals("\r", result);

        // 状態変化の確認
        // なし
    }

    /**
     * testGetFileEncoding01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileEncoding:not null<br>
     * <br>
     * 期待値：(戻り値) this.fileEncoding:not null<br>
     * <br>
     * fileEncodingのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetFileEncoding01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01>(
                fileName, AbstractFileLineIterator_Stub01.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // 前提条件でfileEncodingに"MS932"を設定済み

        // テスト実施
        String result = fileLineIterator.getFileEncoding();

        // 返却値の確認
        assertEquals("MS932", result);

        // 状態変化の確認
        // なし
    }

    /**
     * testGetHeaderLineCount01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.headerLineCount:not null<br>
     * <br>
     * 期待値：(戻り値) this.headerLineCount:not null<br>
     * <br>
     * headerLineCountのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetHeaderLineCount01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01>(
                fileName, AbstractFileLineIterator_Stub01.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // 前提条件でheaderLineCountに1を設定済み

        // テスト実施
        int result = fileLineIterator.getHeaderLineCount();

        // 返却値の確認
        assertEquals(1, result);

        // 状態変化の確認
        // なし
    }

    /**
     * testGetTrailerLineCount01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.trailerLineCount:100<br>
     * <br>
     * 期待値：(戻り値) this.trailerLineCount:100<br>
     * <br>
     * trailerLineCountのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetTrailerLineCount01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01>(
                fileName, AbstractFileLineIterator_Stub01.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // 前提条件でtrailerLineCountに1を設定済み

        // テスト実施
        int result = fileLineIterator.getTrailerLineCount();

        // 返却値の確認
        assertEquals(1, result);

        // 状態変化の確認
        // なし
    }

    /**
     * testGetCurrentLineCount01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.currentLineCount:100<br>
     * <br>
     * 期待値：(戻り値) this.currentLineCount:100<br>
     * <br>
     * currentLineCountのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetCurrentLineCount01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01>(
                fileName, AbstractFileLineIterator_Stub01.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        ReflectionTestUtils.setField(fileLineIterator, "currentLineCount", 1);

        // テスト実施
        int result = fileLineIterator.getCurrentLineCount();

        // 返却値の確認
        assertEquals(1, result);

        // 状態変化の確認
        // なし
    }

    /**
     * testGetFields01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fields:not null<br>
     * <br>
     * 期待値：(戻り値) this.fields:not null<br>
     * <br>
     * fieldsのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetFields01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01>(
                fileName, AbstractFileLineIterator_Stub01.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        Field[] fields = new Field[] {};
        ReflectionTestUtils.setField(fileLineIterator, "fields", fields);

        // テスト実施
        Field[] result = fileLineIterator.getFields();

        // 返却値の確認
        assertSame(fields, result);

        // 状態変化の確認
        // なし
    }

    /**
     * testGetFileName01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) AbstractFileLineIterator実装クラス:AbstractFileLineIteratorImpl02<br>
     * 　空実装<br>
     * (状態) this.fileName:not null<br>
     * <br>
     * 期待値：(戻り値) this.fileName:not null<br>
     * <br>
     * fileNameのgetterメソッドが正しく値を取得することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetFileName01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource(
                "AbstractFileLineIterator_skip01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01> fileLineIterator = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub01>(
                fileName, AbstractFileLineIterator_Stub01.class,
                columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        String fileName_dummy = "ファイル名";
        ReflectionTestUtils.setField(fileLineIterator, "fileName", fileName_dummy);

        // テスト実施
        String result = fileLineIterator.getFileName();

        // 返却値の確認
        assertEquals(fileName_dummy, result);

        // 状態変化の確認
        // なし
    }

    /**
     * testIsCheckByte01() <br>
     * <br>
     * (正常系) <br>
     * <br>
     * 入力値：(引数) inputFileColumn:not null<br>
     * (状態) inputFileColumn#bytes():1<br>
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     * <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsCheckByte01() throws Exception {
        // 前処理(試験対象)
        String fileName = this.getClass().getResource("File_Empty.txt")
                .getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIterator<FixedFileLine_Stub01> fileLineWriter = new AbstractFileLineIteratorImpl02<FixedFileLine_Stub01>(
                fileName, FixedFileLine_Stub01.class, columnParserMap);

        // 前処理(引数)
        Field column = FixedFileLine_Stub01.class.getDeclaredFields()[0];
        InputFileColumn inputFileColumn = column
                .getAnnotation(InputFileColumn.class);

        // テスト実施
        boolean result = fileLineWriter.isCheckByte(inputFileColumn);

        // 判定(戻り値)
        assertTrue(result);
    }

    /**
     * testIsCheckByte02() <br>
     * <br>
     * (異常系) <br>
     * <br>
     * 入力値：(引数) inputFileColumn:not null<br>
     * (状態) inputFileColumn#bytes():-1<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testIsCheckByte02() throws Exception {
        // 前処理(試験対象)
        String fileName = this.getClass().getResource("File_Empty.txt")
                .getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        AbstractFileLineIterator<AbstractFileLineIterator_Stub80> fileLineWriter = new AbstractFileLineIteratorImpl02<AbstractFileLineIterator_Stub80>(
                fileName, AbstractFileLineIterator_Stub80.class,
                columnParserMap);

        // 前処理(引数)
        Field column = AbstractFileLineIterator_Stub80.class
                .getDeclaredFields()[0];
        InputFileColumn inputFileColumn = column
                .getAnnotation(InputFileColumn.class);

        // テスト実施
        boolean result = fileLineWriter.isCheckByte(inputFileColumn);

        // 判定(戻り値)
        assertFalse(result);
    }

}
