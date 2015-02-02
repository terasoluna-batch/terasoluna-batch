/*
 * $Id: VariableFileLineIteratorTest.java 5655 2007-12-04 06:41:04Z pakucn $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.terasoluna.fw.file.annotation.NullStringConverter;
import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.ut.VMOUTUtil;
import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.VariableFileLineIterator} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> 可変長ファイル用のファイルアクセス(データ取得)クラス。
 * <p>
 * @author 奥田哲司
 * @author 趙俸徹
 * @see jp.terasoluna.fw.file.dao.standard.VariableFileLineIterator
 */
public class VariableFileLineIteratorTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(VariableFileLineIteratorTest.class);
    }

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VMOUTUtil.initialize();
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
    public VariableFileLineIteratorTest(String name) {
        super(name);
    }

    /**
     * testVariableFileLineIterator01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:File_Empty.txt<br>
     * 　データを持たないファイルのパス<br>
     * (引数) clazz:VariableFileLineIterator_Stub02<br>
     * 　delimiter、encloseCharが初期値以外<br>
     * (引数) columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     * ・"java.lang.String"=NullColumnParser.java<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineIterator#AbstractFileLineIterator():1回呼ばれる。<br>
     * パラメータに引数が渡されること。<br>
     * (状態変化) this.delimiter:入力値<br>
     * (状態変化) this.encloseChar:入力値<br>
     * (状態変化) AbstractFileLineIterator#init():1回呼び出されること<br>
     * <br>
     * 正常パターン。<br>
     * 引数clazzに指定されているクラスの値でdelimiter、encloseCharが初期化されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testVariableFileLineIterator01() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタの試験なので不要

        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub02> clazz = VariableFileLineIterator_Stub02.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // 前提条件の設定
        // なし

        // テスト実施
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub02>(
                fileName, clazz, columnParserMap);

        // 返却値の確認
        // なし

        // 状態変化の確認
        // 判定
        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
                "<init>"));

        List arguments = VMOUTUtil.getArguments(AbstractFileLineIterator.class,
                "<init>", 0);
        assertEquals(3, arguments.size());
        assertEquals(fileName, arguments.get(0));
        assertSame(clazz, arguments.get(1));
        assertSame(columnParserMap, arguments.get(2));

        assertEquals('"', variableFileLineIterator.getEncloseChar());
        assertEquals('、', variableFileLineIterator.getDelimiter());

        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
                "init"));
    }

    /**
     * testVariableFileLineIterator02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileName:File_Empty.txt<br>
     * 　データを持たないファイルのパス<br>
     * (引数) clazz:VariableFileLineIterator_Stub03<br>
     * 　delimiterが'\u0000'かつencloseCharが初期値（\u0000）以外<br>
     * (引数) columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     * ・"java.lang.String"=NullColumnParser.java<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineIterator#AbstractFileLineIterator():1回呼ばれる。<br>
     * パラメータに引数が渡されること。<br>
     * (状態変化) 例外:"Delimiter can not use '\u0000'."のメッセージ、IllegalStateException、ファイル名を持つFileExceptionが発生する。<br>
     * <br>
     * 例外。@FileFormatのdelimiterに'\u0000'を設定した場合、例外が発生することを確認する。<br>
     * ファイル名が入力値のfileNameに一致することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testVariableFileLineIterator02() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタの試験なので不要

        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub03> clazz = VariableFileLineIterator_Stub03.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            new VariableFileLineIterator<VariableFileLineIterator_Stub03>(
                    fileName, clazz, columnParserMap);
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            // 判定
            assertEquals(1, VMOUTUtil.getCallCount(
                    AbstractFileLineIterator.class, "<init>"));

            List arguments = VMOUTUtil.getArguments(
                    AbstractFileLineIterator.class, "<init>", 0);
            assertEquals(3, arguments.size());
            assertEquals(fileName, arguments.get(0));
            assertSame(clazz, arguments.get(1));
            assertSame(columnParserMap, arguments.get(2));

            assertEquals("Delimiter can not use '\\u0000'.", e.getMessage());
            assertEquals(fileName, e.getFileName());
            assertSame(IllegalStateException.class, e.getCause().getClass());
        }
    }

    /**
     * testVariableFileLineIterator03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:File_Empty.txt<br>
     * 　データを持たないファイルのパス<br>
     * (引数) clazz:VariableFileLineIterator_Stub01<br>
     * 　delimiter、encloseCharが初期値<br>
     * (引数) columnParserMap:以下の要素を持つMap<String, ColumnParser>インスタンス<br>
     * ・"java.lang.String"=NullColumnParser.java<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineIterator#AbstractFileLineIterator():1回呼ばれる。<br>
     * パラメータに引数が渡されること。<br>
     * (状態変化) this.delimiter:初期値<br>
     * (状態変化) this.encloseChar:初期値<br>
     * (状態変化) AbstractFileLineIterator#init():1回呼び出されること<br>
     * <br>
     * 引数clazzで指定したクラスの@FileFormatでdelimiter、encloseCharが初期値の場合は、delimiter、encloseCharが初期値のままであることのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testVariableFileLineIterator03() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタの試験なので不要

        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub01> clazz = VariableFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // 前提条件の設定
        // なし

        // テスト実施
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 返却値の確認
        // なし

        // 状態変化の確認
        // 判定
        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
                "<init>"));

        List arguments = VMOUTUtil.getArguments(AbstractFileLineIterator.class,
                "<init>", 0);
        assertEquals(3, arguments.size());
        assertEquals(fileName, arguments.get(0));
        assertSame(clazz, arguments.get(1));
        assertSame(columnParserMap, arguments.get(2));

        assertEquals(Character.MIN_VALUE, variableFileLineIterator
                .getEncloseChar());
        assertEquals(',', variableFileLineIterator.getDelimiter());

        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
                "init"));
    }

    /**
     * 異常系<br>
     * 改行文字に区切り文字が含まれる
     */
    public void testVariableFileLineIterator04() throws Exception {
        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();

        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // テスト実施
        try {
            new VariableFileLineIterator<VariableFileLine_Stub01>(fileName,
                    VariableFileLine_Stub01.class, columnParserMap);
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
    public void testVariableFileLineIterator05() throws Exception {
        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();

        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // テスト実施
        try {
            new VariableFileLineIterator<VariableFileLine_Stub02>(fileName,
                    VariableFileLine_Stub02.class, columnParserMap);
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
     * ファイル行オブジェクトにInputFileColumnアノテーションが無し
     * @throws Exception このメソッドで発生した例外
     */
    public void testVariableFileLineIterator06() throws Exception {
        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<FileLineObject_Empty> clazz = FileLineObject_Empty.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);

        // テスト実施
        try {
            new VariableFileLineIterator<FileLineObject_Empty>(fileName, clazz,
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
     * testSeparateColumns01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) fileLineString:null<br>
     * (状態) delimiter:','<br>
     * <br>
     * 期待値：(戻り値) columns[]:new String[0]<br>
     * <br>
     * 正常パターン。<br>
     * nullが引数として渡された場合、要素数0の配列を返却することを確認する。<br>
     * 通常の処理でこの返却値が戻ることはない。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub10> clazz = VariableFileLineIterator_Stub10.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub10>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = null;

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(0, result.length);

        // 状態変化の確認
        // なし
    }

    /**
     * testSeparateColumns02() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"aaa"<br>
     * (状態) this.encloseChar:Character.MIN_VALUE<br>
     * (状態) delimiter:','<br>
     * <br>
     * 期待値：(戻り値) columns[]:{"aaa"}<br>
     * <br>
     * 正常パターン。(囲み文字がない場合の処理)<br>
     * 要素数1の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub10> clazz = VariableFileLineIterator_Stub10.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub10>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "aaa";

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(1, result.length);
        assertEquals("aaa", result[0]);

        // 状態変化の確認
        // なし
    }

    /**
     * testSeparateColumns03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"aaa,aaa,aaa"<br>
     * (状態) this.encloseChar:Character.MIN_VALUE<br>
     * (状態) delimiter:','<br>
     * <br>
     * 期待値：(戻り値) columns[]:{"aaa","aaa","aaa"}<br>
     * <br>
     * 正常パターン。(囲み文字がない場合の処理)<br>
     * 要素数3の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub10> clazz = VariableFileLineIterator_Stub10.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub10>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "aaa,aaa,aaa";

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(3, result.length);
        assertEquals("aaa", result[0]);
        assertEquals("aaa", result[1]);
        assertEquals("aaa", result[2]);

        // 状態変化の確認
        // なし
    }

    /**
     * testSeparateColumns04() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"\"aaa\""<br>
     * (状態) this.encloseChar:'\"'<br>
     * (状態) delimiter:','<br>
     * <br>
     * 期待値：(戻り値) columns[]:{"aaa"}<br>
     * <br>
     * 正常パターン。(囲み文字がある場合の処理)<br>
     * 要素数1の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub11> clazz = VariableFileLineIterator_Stub11.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub11>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "\"aaa\"";

        // 前提条件
        UTUtil.setPrivateField(variableFileLineIterator, "columnEncloseChar",
                new char[] { '\"', '\"' });

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(1, result.length);
        assertEquals("aaa", result[0]);

        // 状態変化の確認
        // なし
    }

    /**
     * testSeparateColumns05() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"\"aaa\",\"aaa\",\"aaa\""<br>
     * (状態) this.encloseChar:'\"'<br>
     * (状態) delimiter:','<br>
     * <br>
     * 期待値：(戻り値) columns[]:{"aaa","aaa","aaa"}<br>
     * <br>
     * 正常パターン。(囲み文字がある場合の処理。)<br>
     * 要素数3の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns05() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub11> clazz = VariableFileLineIterator_Stub11.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub11>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "\"aaa\",\"aaa\",\"aaa\"";

        // 前提条件
        UTUtil.setPrivateField(variableFileLineIterator, "columnEncloseChar",
                new char[] { '\"', '\"', '\"' });

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(3, result.length);
        assertEquals("aaa", result[0]);
        assertEquals("aaa", result[1]);
        assertEquals("aaa", result[2]);

        // 状態変化の確認
        // なし
    }

    /**
     * testSeparateColumns06() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"\"aa\ra\",\"aa,a\",\"aa\"\"a\""<br>
     * 囲み文字あり、<br>
     * 行区切り文字が含まれる、<br>
     * 区切り文字が含まれる、<br>
     * 囲み文字が含まれる場合<br>
     * (状態) this.encloseChar:'\"'<br>
     * (状態) super.lineFeedChar:\r<br>
     * (状態) delimiter:','<br>
     * <br>
     * 期待値：(戻り値) columns[]:{"aa\ra","aa,a","aa\"a"}<br>
     * <br>
     * 正常パターン。(囲み文字がある場合の処理。)<br>
     * 要素数4の配列を返却することを確認する。<br>
     * 行区切り文字、区切り文字、囲み文字がそれぞれ引数の文字列にf組まれている場合、エスケープされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns06() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub12> clazz = VariableFileLineIterator_Stub12.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub12>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "\"aa\ra\",\"aa,a\",\"aa\"\"a\"";

        // 前提条件
        UTUtil.setPrivateField(variableFileLineIterator, "columnEncloseChar",
                new char[] { '\"', '\"', '\"', '\"', '\"', '\"', '\"', '\"' });

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(3, result.length);
        assertEquals("aa\ra", result[0]);
        assertEquals("aa,a", result[1]);
        assertEquals("aa\"a", result[2]);

        // 状態変化の確認
        // なし
    }

    /**
     * testSeparateColumns07() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) fileLineString:空文字<br>
     * (状態) delimiter:','<br>
     * <br>
     * 期待値：(戻り値) columns[]:new String[0]<br>
     * <br>
     * 正常パターン。<br>
     * 空文字が引数として渡された場合、要素数0の配列を返却することを確認する。<br>
     * 通常の処理でこの返却値が戻ることはない。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns07() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub13> clazz = VariableFileLineIterator_Stub13.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub13>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "";

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(0, result.length);

        // 状態変化の確認
        // なし
    }

    /**
     * testSeparateColumns08() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"aaa,aaa,aaa"<br>
     * (状態) this.encloseChar:'\"'<br>
     * (状態) delimiter:','<br>
     * <br>
     * 期待値：(戻り値) columns[]:{"aaa","aaa","aaa"}<br>
     * <br>
     * 正常パターン。(encloseCharが設定されており、区切り文字がない場合の処理)<br>
     * 要素数3の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns08() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub11> clazz = VariableFileLineIterator_Stub11.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub11>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "aaa,aaa,aaa";

        // 前提条件
        UTUtil.setPrivateField(variableFileLineIterator, "columnEncloseChar",
                new char[] { Character.MIN_VALUE, Character.MIN_VALUE,
                        Character.MIN_VALUE });

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(3, result.length);
        assertEquals("aaa", result[0]);
        assertEquals("aaa", result[1]);
        assertEquals("aaa", result[2]);

        // 状態変化の確認
        // なし
    }

    /**
     * testSeparateColumns09() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:",,,,"<br>
     * (状態) this.encloseChar:Character.MIN_VALUE<br>
     * (状態) delimiter:','<br>
     * <br>
     * 期待値：(戻り値) columns[]:{"", "", "", "", ""}<br>
     * <br>
     * 正常パターン。(区切り文字がない場合の処理)<br>
     * 空白文字５の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns09() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub10> clazz = VariableFileLineIterator_Stub10.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub10>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = ",,,,";

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(5, result.length);
        assertEquals("", result[0]);
        assertEquals("", result[1]);
        assertEquals("", result[2]);
        assertEquals("", result[3]);
        assertEquals("", result[4]);

        // 状態変化の確認
        // なし
    }

    /**
     * testSeparateColumns10() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"aaa#aaa#aaa"<br>
     * (状態) this.encloseChar:Character.MIN_VALUE<br>
     * (状態) delimiter:'#'<br>
     * <br>
     * 期待値：(戻り値) columns[]:{"aaa","aaa","aaa"}<br>
     * <br>
     * 正常パターン。(囲み文字がない場合の処理)<br>
     * 区切り文字をデフォルト以外のものに設定した場合でも、要素数3の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns10() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub14> clazz = VariableFileLineIterator_Stub14.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub14>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "aaa#aaa#aaa";

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(3, result.length);
        assertEquals("aaa", result[0]);
        assertEquals("aaa", result[1]);
        assertEquals("aaa", result[2]);

        // 状態変化の確認
        // なし
    }

    /**
     * testSeparateColumns11() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:",bbb,ccc"<br>
     * (状態) this.encloseChar:Character.MIN_VALUE<br>
     * (状態) delimiter:','<br>
     * <br>
     * 期待値：(戻り値) columns[]:{"","bbb","ccc"}<br>
     * <br>
     * 正常パターン。(囲み文字がない場合の処理)<br>
     * 空文字が先頭にある場合でも、要素数3の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns11() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub10> clazz = VariableFileLineIterator_Stub10.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub10>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = ",bbb,ccc";

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(3, result.length);
        assertEquals("", result[0]);
        assertEquals("bbb", result[1]);
        assertEquals("ccc", result[2]);

        // 状態変化の確認
        // なし
    }

    /**
     * testSeparateColumns12() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"aaa,,ccc"<br>
     * (状態) this.encloseChar:Character.MIN_VALUE<br>
     * (状態) delimiter:','<br>
     * <br>
     * 期待値：(戻り値) columns[]:{"aaa","","ccc"}<br>
     * <br>
     * 正常パターン。(囲み文字がない場合の処理)<br>
     * 空文字が途中に含まれる場合でも、要素数3の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns12() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub10> clazz = VariableFileLineIterator_Stub10.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub10>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "aaa,,ccc";

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(3, result.length);
        assertEquals("aaa", result[0]);
        assertEquals("", result[1]);
        assertEquals("ccc", result[2]);

        // 状態変化の確認
        // なし
    }

    /**
     * testSeparateColumns13() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"aaa,bbb,"<br>
     * (状態) this.encloseChar:Character.MIN_VALUE<br>
     * (状態) delimiter:','<br>
     * <br>
     * 期待値：(戻り値) columns[]:{"aaa","bbb",""}<br>
     * <br>
     * 正常パターン。(囲み文字がない場合の処理)<br>
     * 空文字が最後に含まれる場合でも、要素数3の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns13() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub10> clazz = VariableFileLineIterator_Stub10.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub10>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "aaa,bbb,";

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(3, result.length);
        assertEquals("aaa", result[0]);
        assertEquals("bbb", result[1]);
        assertEquals("", result[2]);

        // 状態変化の確認
        // なし
    }

    /**
     * testSeparateColumns14() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"\"aa\"bb\""<br>
     * (状態) this.encloseChar:'\"'<br>
     * (状態) delimiter:','<br>
     * <br>
     * 期待値：(戻り値) columns[]:{"aabb\""}<br>
     * <br>
     * 囲み文字が設定されており、エスケープされていない囲み文字が内部データとして格納されている場合は、予期せぬデータが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testSeparateColumns14() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub11> clazz = VariableFileLineIterator_Stub11.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub11>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "\"aa\"bb\"";

        // 前提条件
        UTUtil.setPrivateField(variableFileLineIterator, "columnEncloseChar",
                new char[] { '\"', '\"' });

        // テスト実施
        String[] result = variableFileLineIterator
                .separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(1, result.length);
        assertEquals("aabb\"", result[0]);

        // 状態変化の確認
        // なし
    }

    /**
     * testGetEncloseChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(状態) this.encloseChar:not null<br>
     * 'a'<br>
     * <br>
     * 期待値：(戻り値) encloseChar:not null<br>
     * 'a'<br>
     * <br>
     * encloseCharのgetterが正常に動作することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testGetEncloseChar01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<VariableFileLineIterator_Stub20> clazz = VariableFileLineIterator_Stub20.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());
        VariableFileLineIterator variableFileLineIterator = new VariableFileLineIterator<VariableFileLineIterator_Stub20>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        char result = variableFileLineIterator.getEncloseChar();

        // 返却値の確認
        assertEquals('a', result);

        // 状態変化の確認
        // なし
    }

    /**
     * 正常系<br>
     * InputFileColumnのcolumnEncloseCharによって、個々のカラムに囲み文字を設定
     * @throws Exception
     */
    public void testNext01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        VariableFileLineIterator<CSVFileLine_Stub01> fileLineIterator = new VariableFileLineIterator<CSVFileLine_Stub01>(
                fileName, CSVFileLine_Stub01.class, columnParserMap);

        // テスト実施
        CSVFileLine_Stub01 result1 = fileLineIterator.next();
        CSVFileLine_Stub01 result2 = fileLineIterator.next();
        CSVFileLine_Stub01 result3 = fileLineIterator.next();

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
     * FileFormatのencloseCharとInputFileColumnのcolumnEncloseCharによって、カラムに囲み文字を設定
     * @throws Exception
     */
    public void testNext02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        VariableFileLineIterator<CSVFileLine_Stub02> fileLineIterator = new VariableFileLineIterator<CSVFileLine_Stub02>(
                fileName, CSVFileLine_Stub02.class, columnParserMap);

        // テスト実施
        CSVFileLine_Stub02 result1 = fileLineIterator.next();
        CSVFileLine_Stub02 result2 = fileLineIterator.next();
        CSVFileLine_Stub02 result3 = fileLineIterator.next();

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
    public void testNext03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next03.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // 様々な設定がされているファイル行オブジェクトを設定
        VariableFileLineIterator<CSVFileLine_Stub03> fileLineIterator = new VariableFileLineIterator<CSVFileLine_Stub03>(
                fileName, CSVFileLine_Stub03.class, columnParserMap);

        // ファイル行オブジェクトに設定してあった値を全て上書き
        // 以下の設定が適用されれば、ファイル行オブジェクトの
        // アノテーションにアクセスしていないことになる。
        char[] charArray = new char[] { 0, 0, 0, 0 };
        // 前提条件
        UTUtil.setPrivateField(fileLineIterator, "lineFeedChar", "\r\n");
        UTUtil.setPrivateField(fileLineIterator, "delimiter", '_');
        UTUtil.setPrivateField(fileLineIterator, "inputFileColumns", null);
        UTUtil.setPrivateField(fileLineIterator, "columnFormats", new String[] {
                "", "", "", "" });
        UTUtil.setPrivateField(fileLineIterator, "columnBytes", new int[] { -1,
                -1, -1, -1 });
        UTUtil.setPrivateField(fileLineIterator, "totalBytes", 0);
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
        CSVFileLine_Stub03 result1 = fileLineIterator.next();
        CSVFileLine_Stub03 result2 = fileLineIterator.next();
        CSVFileLine_Stub03 result3 = fileLineIterator.next();

        // 返却値の確認
        assertEquals("\"1\"", result1.getColumn1());
        assertEquals("22", result1.getColumn2());
        assertEquals("333", result1.getColumn3());
        assertEquals("|4444|", result1.getColumn4());

        assertEquals("\"5\"", result2.getColumn1());
        assertEquals("66", result2.getColumn2());
        assertEquals("777", result2.getColumn3());
        assertEquals("|8888|", result2.getColumn4());

        assertEquals("\"9\"", result3.getColumn1());
        assertEquals("AA", result3.getColumn2());
        assertEquals("BBB", result3.getColumn3());
        assertEquals("|CCCC|", result3.getColumn4());
    }

    /**
     * testGetEncloseCharcter001.
     * @throws Exception
     */
    public void testGetEncloseCharcter001() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next03.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // 様々な設定がされているファイル行オブジェクトを設定
        VariableFileLineIterator<CSVFileLine_Stub03> fileLineIterator = new VariableFileLineIterator<CSVFileLine_Stub03>(
                fileName, CSVFileLine_Stub03.class, columnParserMap);

        char[] columnEncloseChar = new char[] {};
        int index = 0;

        // テスト実施
        Object result = UTUtil.invokePrivate(fileLineIterator,
                "getEncloseCharcter", char[].class, int.class,
                columnEncloseChar, index);

        assertNotNull(result);
        assertEquals(Character.class, result.getClass());
        assertEquals(Character.valueOf('c'), (Character) result);
    }

    /**
     * testGetEncloseCharcter002.
     * @throws Exception
     */
    public void testGetEncloseCharcter002() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next03.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // 様々な設定がされているファイル行オブジェクトを設定
        VariableFileLineIterator<CSVFileLine_Stub03> fileLineIterator = new VariableFileLineIterator<CSVFileLine_Stub03>(
                fileName, CSVFileLine_Stub03.class, columnParserMap);

        char[] columnEncloseChar = new char[] { 'A', 'B', 'C' };
        int index = 0;

        // テスト実施
        Object result = UTUtil.invokePrivate(fileLineIterator,
                "getEncloseCharcter", char[].class, int.class,
                columnEncloseChar, index);

        assertNotNull(result);
        assertEquals(Character.class, result.getClass());
        assertEquals(Character.valueOf('A'), (Character) result);
    }

    /**
     * testGetEncloseCharcter003.
     * @throws Exception
     */
    public void testGetEncloseCharcter003() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next03.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // 様々な設定がされているファイル行オブジェクトを設定
        VariableFileLineIterator<CSVFileLine_Stub03> fileLineIterator = new VariableFileLineIterator<CSVFileLine_Stub03>(
                fileName, CSVFileLine_Stub03.class, columnParserMap);

        char[] columnEncloseChar = new char[] { 'A', 'B', 'C' };
        int index = 1;

        // テスト実施
        Object result = UTUtil.invokePrivate(fileLineIterator,
                "getEncloseCharcter", char[].class, int.class,
                columnEncloseChar, index);

        assertNotNull(result);
        assertEquals(Character.class, result.getClass());
        assertEquals(Character.valueOf('B'), (Character) result);
    }

    /**
     * testGetEncloseCharcter004.
     * @throws Exception
     */
    public void testGetEncloseCharcter004() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next03.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // 様々な設定がされているファイル行オブジェクトを設定
        VariableFileLineIterator<CSVFileLine_Stub03> fileLineIterator = new VariableFileLineIterator<CSVFileLine_Stub03>(
                fileName, CSVFileLine_Stub03.class, columnParserMap);

        char[] columnEncloseChar = new char[] { 'A', 'B', 'C' };
        int index = 2;

        // テスト実施
        Object result = UTUtil.invokePrivate(fileLineIterator,
                "getEncloseCharcter", char[].class, int.class,
                columnEncloseChar, index);

        assertNotNull(result);
        assertEquals(Character.class, result.getClass());
        assertEquals(Character.valueOf('C'), (Character) result);
    }

    /**
     * testGetEncloseCharcter005.
     * @throws Exception
     */
    public void testGetEncloseCharcter005() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next03.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // 様々な設定がされているファイル行オブジェクトを設定
        VariableFileLineIterator<CSVFileLine_Stub03> fileLineIterator = new VariableFileLineIterator<CSVFileLine_Stub03>(
                fileName, CSVFileLine_Stub03.class, columnParserMap);

        char[] columnEncloseChar = new char[] { 'A', 'B', 'C' };
        int index = 3;

        // テスト実施
        Object result = UTUtil.invokePrivate(fileLineIterator,
                "getEncloseCharcter", char[].class, int.class,
                columnEncloseChar, index);

        assertNotNull(result);
        assertEquals(Character.class, result.getClass());
        assertEquals(Character.valueOf('c'), (Character) result);
    }

    /**
     * testGetEncloseCharcter006.
     * @throws Exception
     */
    public void testGetEncloseCharcter006() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next03.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // 様々な設定がされているファイル行オブジェクトを設定
        VariableFileLineIterator<CSVFileLine_Stub03> fileLineIterator = new VariableFileLineIterator<CSVFileLine_Stub03>(
                fileName, CSVFileLine_Stub03.class, columnParserMap);

        char[] columnEncloseChar = new char[] { 'A', 'B', 'C' };
        int index = 4;

        // テスト実施
        Object result = UTUtil.invokePrivate(fileLineIterator,
                "getEncloseCharcter", char[].class, int.class,
                columnEncloseChar, index);

        assertNotNull(result);
        assertEquals(Character.class, result.getClass());
        assertEquals(Character.valueOf('c'), (Character) result);
    }
}
