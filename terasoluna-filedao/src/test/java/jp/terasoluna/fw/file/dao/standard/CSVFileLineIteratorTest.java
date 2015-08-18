/*
 * $Id: CSVFileLineIteratorTest.java 5654 2007-12-04 06:34:19Z pakucn $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import jp.terasoluna.fw.file.annotation.NullStringConverter;
import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.ut.VMOUTUtil;
import jp.terasoluna.utlib.UTUtil;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.CSVFileLineIterator} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> CSVファイル用のファイルアクセス(データ取得)クラス。
 * <p>
 * @author 奥田哲司
 * @author 趙俸徹
 * @see jp.terasoluna.fw.file.dao.standard.CSVFileLineIterator
 */
public class CSVFileLineIteratorTest {

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     */
    @Before
    public void setUp() {
        VMOUTUtil.initialize();
    }

    /**
     * testCSVFileLineIterator01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:CSVFileLineIterator01.txt<br>
     * 　データを持たないファイルのパス<br>
     * (引数) clazz:以下の設定を持つFileFormatアノテーションを持つスタブ<br>
     * CSVFileLineIterator_Stub02<br>
     * 　アノテーションFileFormat：encloseChar(囲み文字)が初期値以外<br>
     * @FileFormat(encloseChar = '"')<br>
     *                         (引数) columnParserMap:以下の設定を持つHashMapのインスタンス<br>
     *                         要素1<br>
     *                         key:"java.lang.String"<br>
     *                         value:ColumnParserインスタンス<br>
     *                         CSVFileLineIterator_ColumnParserStub01インスタンス<br>
     *                         空実装<br>
     * <br>
     *                         期待値：(状態変化) AbstractFileLineIteratorコンストラクタ:1回呼ばれ、引数がすべて渡っていること。<br>
     *                         (状態変化) encloseChar:初期値以外(設定したもの)<br>
     *                         (状態変化) AbstractFileLineIterator#init():2回呼ばれる。<br>
     * <br>
     *                         正常パターン<br>
     *                         コンストラクタの呼出が正常に行われることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testCSVFileLineIterator01() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub02> clazz = CSVFileLineIterator_Stub02.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);

        // 前提条件なし

        // テスト実施
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub02>(
                fileName, clazz, columnParserMap);

        // 返却値なし

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
                "<init>"));
        List arguments = VMOUTUtil.getArguments(AbstractFileLineIterator.class,
                "<init>", 0);
        assertEquals(fileName, arguments.get(0));
        assertEquals(clazz, arguments.get(1));
        assertSame(columnParserMap, arguments.get(2));
        assertEquals('"', csvFileLineIterator.getEncloseChar());
        assertEquals(2, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
                "init"));
    }

    /**
     * testCSVFileLineIterator02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileName:CSVFileLineIterator01.txt<br>
     * 　データを持たないファイルのパス<br>
     * (引数) clazz:以下の設定を持つFileFormatアノテーションを持つスタブ<br>
     * CSVFileLineIterator_Stub03<br>
     * 　アノテーションFileFormat：delimiter(区切り文字)が初期値以外<br>
     * @FileFormat(delimiter = '"')<br>
     *                       (引数) columnParserMap:以下の設定を持つHashMapのインスタンス<br>
     *                       要素1<br>
     *                       key:"java.lang.String"<br>
     *                       value:ColumnParserインスタンス<br>
     *                       CSVFileLineIterator_ColumnParserStub01インスタンス<br>
     *                       空実装<br>
     * <br>
     *                       期待値：(状態変化) AbstractFileLineIteratorコンストラクタ:1回呼ばれる。<br>
     *                       引数と同じインスタンスが渡される。<br>
     *                       (状態変化) AbstractFileLineIterator#init():呼ばれない。<br>
     *                       (状態変化) なし:"Delimiter can not change."のメッセージ、IllegalStateException、ファイル名を持つFileExceptionが発生する。<br>
     * <br>
     *                       例外。@FileFormatのdelimiterに初期値以外を設定した場合、例外が発生することを確認する。<br>
     *                       ファイル名が入力値のfileNameに一致することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testCSVFileLineIterator02() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub03> clazz = CSVFileLineIterator_Stub03.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);

        // 前提条件なし

        // テスト実施
        try {
            new CSVFileLineIterator<CSVFileLineIterator_Stub03>(fileName,
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
            assertFalse(VMOUTUtil.isCalled(VariableFileLineIterator.class,
                    "init"));
            assertEquals("Delimiter can not change.", e.getMessage());
            assertEquals(fileName, e.getFileName());
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    /**
     * testCSVFileLineIterator03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:CSVFileLineIterator01.txt<br>
     * 　データを持たないファイルのパス<br>
     * (引数) clazz:以下の設定を持つFileFormatアノテーションを持つスタブ<br>
     * CSVFileLineIterator_Stub01<br>
     * 　アノテーションFileFormat：初期値<br>
     * @FileFormat()<br> (引数) columnParserMap:以下の設定を持つHashMapのインスタンス<br>
     *                   要素1<br>
     *                   key:"java.lang.String"<br>
     *                   value:ColumnParserインスタンス<br>
     *                   CSVFileLineIterator_ColumnParserStub01インスタンス<br>
     *                   空実装<br>
     * <br>
     *                   期待値：(状態変化) AbstractFileLineIteratorコンストラクタ:1回呼ばれ、引数がすべて渡っていること。<br>
     *                   (状態変化) encloseChar:初期値が設定されていること。<br>
     * <br>
     *                   引数clazzに設定されたクラスが囲み文字、区切り文字がデフォルトのままの場合は、コンストラクタ呼び出しが正常に行われることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testCSVFileLineIterator03() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub01> clazz = CSVFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);

        // 前提条件なし

        // テスト実施
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 返却値なし

        // 状態変化の確認
        assertEquals(1, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
                "<init>"));
        List arguments = VMOUTUtil.getArguments(AbstractFileLineIterator.class,
                "<init>", 0);
        assertEquals(fileName, arguments.get(0));
        assertEquals(clazz, arguments.get(1));
        assertSame(columnParserMap, arguments.get(2));
        assertEquals(Character.MIN_VALUE, csvFileLineIterator.getEncloseChar());
        assertEquals(2, VMOUTUtil.getCallCount(AbstractFileLineIterator.class,
                "init"));
    }

    /**
     * 異常系<br>
     * ファイル行オブジェクトにInputFileColumnアノテーションが無し
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCSVFileLineIterator04() throws Exception {
        // 引数の設定
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<FileLineObject_Empty> clazz = FileLineObject_Empty.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);

        // テスト実施
        try {
            new CSVFileLineIterator<FileLineObject_Empty>(fileName, clazz,
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
     * (状態) this.encloseChar:Character.MIN_VALUE<br>
     * <br>
     * 期待値：(戻り値) String[]:new String[0]<br>
     * <br>
     * 正常パターン。<br>
     * nullもしくは空文字が引数として渡された場合、要素数0の配列を返却することを確認する。<br>
     * 通常の処理でこの返却値が戻ることはない。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSeparateColumns01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub01> clazz = CSVFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = null;

        // 前提条件
        UTUtil.setPrivateField(csvFileLineIterator, "columnEncloseChar",
                new char[] {});

        // テスト実施
        String[] result = csvFileLineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(0, result.length);

        // 状態変化なし
    }

    /**
     * testSeparateColumns02() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileLineString:"aaa"<br>
     * (状態) this.encloseChar:Character.MIN_VALUE<br>
     * <br>
     * 期待値：(戻り値) String[]:{"aaa"}<br>
     * <br>
     * 正常パターン。(囲み文字がない場合の処理)<br>
     * 要素数1の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSeparateColumns02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub01> clazz = CSVFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "aaa";

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String[] result = csvFileLineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(1, result.length);
        assertEquals(fileLineString, result[0]);
    }

    /**
     * testSeparateColumns03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileLineString:"aaa,bbb,ccc"<br>
     * (状態) this.encloseChar:Character.MIN_VALUE<br>
     * <br>
     * 期待値：(戻り値) String[]:{"aaa","bbb","ccc"}<br>
     * <br>
     * 正常パターン。(囲み文字がない場合の処理)<br>
     * 要素数3の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSeparateColumns03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub01> clazz = CSVFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "aaa,bbb,ccc";

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String[] result = csvFileLineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(3, result.length);
        assertEquals("aaa", result[0]);
        assertEquals("bbb", result[1]);
        assertEquals("ccc", result[2]);

        // 状態変化なし
    }

    /**
     * testSeparateColumns04() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"\"aaa\""<br>
     * (状態) this.encloseChar:\"'<br>
     * (状態) this.fields[]:配列要素が0<br>
     * <br>
     * 期待値：(戻り値) String[]:{"aaa"}<br>
     * <br>
     * 正常パターン。(囲み文字がある場合の処理)<br>
     * 囲み文字がエスケープされ、要素数1の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSeparateColumns04() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub02> clazz = CSVFileLineIterator_Stub02.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub02>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "\"aaa\"";

        // 前提条件
        UTUtil.setPrivateField(csvFileLineIterator, "columnEncloseChar",
                new char[] { '\"', '\"' });

        // テスト実施
        String[] result = csvFileLineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(1, result.length);
        assertEquals("aaa", result[0]);
    }

    /**
     * testSeparateColumns05() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileLineString:"\"aaa\""<br>
     * (状態) this.encloseChar:\"<br>
     * (状態) this.fields[]:配列要素が1<br>
     * (状態) this.field[]のアノテーションInputFileColumn:@InputFileColumn(columnIndex = 0)<br>
     * private String column1 = null;<br>
     * <br>
     * 期待値：(戻り値) String[]:{"aaa"}<br>
     * <br>
     * 正常パターン。(囲み文字がない場合の処理)<br>
     * 要素数1の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSeparateColumns05() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub04> clazz = CSVFileLineIterator_Stub04.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub04>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "\"aaa\"";

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String[] result = csvFileLineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(1, result.length);
        assertEquals("aaa", result[0]);
    }

    /**
     * testSeparateColumns06() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"\"aaa\",\"bbb\",\"ccc\""<br>
     * (状態) this.encloseChar:\"'<br>
     * (状態) this.fields[]:配列要素が3<br>
     * (状態) this.field[]のアノテーションInputFileColumn:@InputFileColumn(columnIndex = 0)<br>
     * private String column1 = null;<br>
     * <br>
     * @InputFileColumn(columnIndex = 1)<br>
     *                              private String column2 = null;<br>
     * <br>
     * @InputFileColumn(columnIndex = 2)<br>
     *                              private String column3 = null;<br>
     * <br>
     *                              期待値：(戻り値) String[]:{"aaa","bbb","ccc"}<br>
     * <br>
     *                              正常パターン。(囲み文字がある場合の処理。)<br>
     *                              囲み文字がエスケープされ、要素数3の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSeparateColumns06() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub05> clazz = CSVFileLineIterator_Stub05.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub05>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "\"aaa\",\"bbb\",\"ccc\"";

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String[] result = csvFileLineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(3, result.length);
        assertEquals("aaa", result[0]);
        assertEquals("bbb", result[1]);
        assertEquals("ccc", result[2]);

        // 状態変化なし
    }

    /**
     * testSeparateColumns07() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"\"aa\ra\",\"bb,b\",\"cc\"\"c\""<br>
     * 囲み文字あり、<br>
     * 行区切り文字が含まれる、<br>
     * 区切り文字が含まれる、<br>
     * 囲み文字が含まれる場合<br>
     * (状態) this.encloseChar:\"'<br>
     * (状態) this.fields[]:配列要素が3<br>
     * (状態) this.field[]のアノテーションInputFileColumn:@InputFileColumn(columnIndex = 0)<br>
     * private String column1 = null;<br>
     * <br>
     * @InputFileColumn(columnIndex = 1)<br>
     *                              private String column2 = null;<br>
     * <br>
     * @InputFileColumn(columnIndex = 2)<br>
     *                              private String column3 = null;<br>
     * <br>
     *                              期待値：(戻り値) String[]:{"aa\ra","bb,b","cc\"c"}<br>
     * <br>
     *                              正常パターン。(囲み文字がある場合の処理。)<br>
     *                              要素数3の配列を返却することを確認する。<br>
     *                              区切り文字、囲み文字がそれぞれエスケープされることを確認する。行区切り文字はエスケープされないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSeparateColumns07() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub05> clazz = CSVFileLineIterator_Stub05.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub05>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "\"aa\ra\",\"bb,b\",\"cc\"\"c\"";

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String[] result = csvFileLineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(3, result.length);
        assertEquals("aa\ra", result[0]);
        assertEquals("bb,b", result[1]);
        assertEquals("cc\"c", result[2]);

        // 状態変化なし
    }

    /**
     * testSeparateColumns08() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"aaa,bbb,ccc"<br>
     * (状態) this.encloseChar:\"'<br>
     * <br>
     * 期待値：(戻り値) String[]:{"aaa","bbb","ccc"}<br>
     * <br>
     * 正常パターン。(encloseCharが設定されており、囲み文字がない場合の処理)<br>
     * 要素数3の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSeparateColumns08() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub02> clazz = CSVFileLineIterator_Stub02.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub02>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "aaa,bbb,ccc";

        // 前提条件
        UTUtil.setPrivateField(csvFileLineIterator, "columnEncloseChar",
                new char[] { Character.MIN_VALUE, Character.MIN_VALUE,
                        Character.MIN_VALUE });

        // テスト実施
        String[] result = csvFileLineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(3, result.length);
        assertEquals("aaa", result[0]);
        assertEquals("bbb", result[1]);
        assertEquals("ccc", result[2]);

        // 状態変化なし
    }

    /**
     * testSeparateColumns09() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) fileLineString:,,,,<br>
     * (状態) this.encloseChar:Character.MIN_VALUE<br>
     * <br>
     * 期待値：(戻り値) String[]:{"", "", "", "", ""}<br>
     * <br>
     * 正常パターン。(囲み文字がない場合の処理)<br>
     * 空白文字５の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSeparateColumns09() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub01> clazz = CSVFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = ",,,,";

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String[] result = csvFileLineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(5, result.length);
        assertEquals("", result[0]);
        assertEquals("", result[1]);
        assertEquals("", result[2]);
        assertEquals("", result[3]);
        assertEquals("", result[4]);

        // 状態変化なし
    }

    /**
     * testSeparateColumns10() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) fileLineString:""(空文字)<br>
     * (状態) this.encloseChar:Character.MIN_VALUE<br>
     * <br>
     * 期待値：(戻り値) String[]:new String[0]<br>
     * <br>
     * 空文字が引数として渡された場合、要素数0の配列を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSeparateColumns10() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub01> clazz = CSVFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "";

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String[] result = csvFileLineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(0, result.length);

        // 状態変化なし
    }

    /**
     * testSeparateColumns11() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) fileLineString:"\"aa\"bb\""<br>
     * (状態) this.encloseChar:\"'<br>
     * <br>
     * 期待値：(戻り値) String[]:{"aabb\""}<br>
     * <br>
     * 囲み文字が設定されており、エスケープされていない囲み文字が内部データとして格納されている場合は、予期せぬデータが返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSeparateColumns11() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub02> clazz = CSVFileLineIterator_Stub02.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub02>(
                fileName, clazz, columnParserMap);

        // 引数の設定
        String fileLineString = "\"aa\"bb\"";

        // 前提条件
        UTUtil.setPrivateField(csvFileLineIterator, "columnEncloseChar",
                new char[] { '\"', '\"', '\"' });

        // テスト実施
        String[] result = csvFileLineIterator.separateColumns(fileLineString);

        // 返却値の確認
        assertEquals(1, result.length);
        assertEquals("aabb\"", result[0]);

        // 状態変化なし
    }

    /**
     * testGetDelimiter01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) this.delimiter:','(固定)<br>
     * <br>
     * 期待値：(戻り値) char:','(固定)<br>
     * <br>
     * delimiterのgetterが正常に動作することを確認する。<br>
     * 必ず、カンマを返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetDelimiter01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub01> clazz = CSVFileLineIterator_Stub01.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub01>(
                fileName, clazz, columnParserMap);
        VMOUTUtil.setReturnValueAtAllTimes(AbstractFileLineIterator.class,
                "buildLineReader", null);

        // 引数なし

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        char result = csvFileLineIterator.getDelimiter();

        // 返却値の確認
        assertEquals(',', result);

        // 状態変化なし
    }

    /**
     * testGetEncloseChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) this.encloseChar:not null<br>
     * '"'<br>
     * <br>
     * 期待値：(戻り値) char:not null<br>
     * '"'<br>
     * <br>
     * encloseCharのgetterが正常に動作することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetEncloseChar01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("File_Empty.txt");
        String fileName = url.getPath();
        Class<CSVFileLineIterator_Stub02> clazz = CSVFileLineIterator_Stub02.class;
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        ColumnParser columnParser = new CSVFileLineIterator_ColumnParserStub01();
        columnParserMap.put("java.lang.String", columnParser);
        columnParserMap.put("java.util.Date", columnParser);
        columnParserMap.put("java.math.BigDecimal", columnParser);
        columnParserMap.put("java.lang.int", columnParser);
        CSVFileLineIterator csvFileLineIterator = new CSVFileLineIterator<CSVFileLineIterator_Stub02>(
                fileName, clazz, columnParserMap);

        // 引数なし

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        char result = csvFileLineIterator.getEncloseChar();

        // 返却値の確認
        assertEquals('"', result);

        // 状態変化なし
    }

    /**
     * 正常系<br>
     * InputFileColumnのcolumnEncloseCharによって、個々のカラムに囲み文字を設定
     * @throws Exception
     */
    @Test
    public void testNext01() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next01.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        CSVFileLineIterator<CSVFileLine_Stub01> fileLineIterator = new CSVFileLineIterator<CSVFileLine_Stub01>(
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
    @Test
    public void testNext02() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next02.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        CSVFileLineIterator<CSVFileLine_Stub02> fileLineIterator = new CSVFileLineIterator<CSVFileLine_Stub02>(
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
    @Test
    public void testNext03() throws Exception {
        // テスト対象のインスタンス化
        URL url = this.getClass().getResource("CsvFileLineIterator_next03.txt");
        String fileName = url.getPath();
        Map<String, ColumnParser> columnParserMap = new HashMap<String, ColumnParser>();
        columnParserMap.put("java.lang.String", new NullColumnParser());

        // 様々な設定がされているファイル行オブジェクトを設定
        CSVFileLineIterator<CSVFileLine_Stub03> fileLineIterator = new CSVFileLineIterator<CSVFileLine_Stub03>(
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
}
