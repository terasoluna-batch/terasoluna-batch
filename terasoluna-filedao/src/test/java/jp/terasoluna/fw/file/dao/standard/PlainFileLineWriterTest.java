package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import jp.terasoluna.fw.file.dao.FileException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.PlainFileLineWriter} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ビジネスロジックなどから受け取った文字列をファイルに出力する。 他のファイルアクセス機能とは異なり、ファイル行オブジェクトを使わない。<br>
 * AbstractFileLineWriterのサブクラス。
 * <p>
 * @see jp.terasoluna.fw.file.dao.standard.PlainFileLineWriter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractFileLineWriter.class)
public class PlainFileLineWriterTest {

    private static final String TEMP_FILE_NAME = PlainFileLineWriterTest.class
            .getResource("PlainFileLineWriterTest_tmp.txt").getPath();

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(PlainFileLineWriterTest.class);
    }

    @Before
    public void setUp() throws Exception {
//        VMOUTUtil.initialize();
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
     * testPlainFileLineWriter01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) fileName:PlainFileLineWriter01.txt<br>
     * 　データを持たないファイルのパス<br>
     * (引数) clazz:PlainFileLineWriter_Stub01<br>
     * 　@FileFormatの設定有り、すべて初期値。<br>
     * (引数) columnFormatterMap:以下の要素を持つMap<String, ColumnFormatter>インスタンス<br>
     * ・"java.lang.String"=NullColumnFormatterインスタンス<br>
     * <br>
     * 期待値：(状態変化) super:1回呼び出されることを確認する。<br>
     * 呼び出されるときの引数が、引数fileName,clazz,columnFormatterMapと同じインスタンスであること<br>
     * (状態変化) super.init:1回呼び出されることを確認する。<br>
     * <br>
     * 親クラスのコンストラクタが呼ばれることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPlain01() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        String fileName = TEMP_FILE_NAME;

        Class<PlainFileLineWriter_Stub01> clazz = PlainFileLineWriter_Stub01.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        // 前提条件の設定
        // なし

        PlainFileLineWriter pflw = PowerMockito.mock(PlainFileLineWriter.class);
        PowerMockito.whenNew(PlainFileLineWriter.class).withAnyArguments().thenReturn(pflw);

        // テスト実施
        PlainFileLineWriter plainFileLineWriter = new PlainFileLineWriter(
                fileName, clazz, columnFormatterMap);

        // 返却値の確認
        // なし

        // 状態変化の確認
        // 親クラスのコンストラクタコールは取得できないため、テスト対象の引数を確認している。
        PowerMockito.verifyNew(PlainFileLineWriter.class).withArguments(fileName, clazz, columnFormatterMap);
        plainFileLineWriter.closeFile();
    }

    /**
     * testPrintDataLine01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E.F <br>
     * <br>
     * 入力値：(引数) t:Stringインスタンス<br>
     * (状態) AbstractFileLineWriter#checkWriteTrailer():正常実行<br>
     * (状態) Writer.writer():正常実行<br>
     * (状態) AbstractFileLineWriter#getLineFeedChar():Stringインスタンス<br>
     * (状態) AbstractFileLineWriter#getWriter():Writerインスタンス<br>
     * (状態) AbstractFileLineWriter#setWriteData():正常実行<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineWriter#checkWriteTrailer():1回呼ばれる<br>
     * (状態変化) Writer.writer():2回呼ばれる<br>
     * (状態変化) AbstractFileLineWriter#getLineFeedChar():1回呼ばれる<br>
     * (状態変化) AbstractFileLineWriter#getWriter():2回呼ばれる<br>
     * (状態変化) AbstractFileLineWriter#setWriteData():1回呼ばれる<br>
     * <br>
     * 正常パターン <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPrintDataLine01() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        PlainFileLineWriter plainFileLineWriter = Mockito.spy(new PlainFileLineWriter(
                fileName, PlainFileLineWriter_Stub01.class, columnFormatterMap));

        // 引数の設定
        String t = "データ";

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        plainFileLineWriter.printDataLine(t);

        // 返却値の確認
        // なし

        // 状態変化の確認
        Mockito.verify(plainFileLineWriter).checkWriteTrailer();
        Mockito.verify(plainFileLineWriter).getLineFeedChar();
        Mockito.verify(plainFileLineWriter, Mockito.times(2)).getWriter();
        Mockito.verify(plainFileLineWriter).setWriteData(Mockito.anyBoolean());
        plainFileLineWriter.closeFile();
    }

    /**
     * testPrintDataLine02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) t:Stringインスタンス<br>
     * (状態) AbstractFileLineWriter#checkWriteTrailer():正常実行<br>
     * (状態) Writer.writer():IOException<br>
     * 例外発生<br>
     * (状態) AbstractFileLineWriter#getLineFeedChar():Stringインスタンス<br>
     * (状態) AbstractFileLineWriter#getWriter():Writerインスタンス<br>
     * (状態) AbstractFileLineWriter#setWriteData():正常実行<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineWriter#checkWriteTrailer():1回呼ばれる<br>
     * (状態変化) Writer.writer():１回呼ばれる<br>
     * (状態変化) AbstractFileLineWriter#getLineFeedChar():呼ばれない<br>
     * (状態変化) AbstractFileLineWriter#getWriter():1回呼ばれる<br>
     * (状態変化) AbstractFileLineWriter#setWriteData():呼ばれない<br>
     * (状態変化) なし:以下の要素を持つFileException例外発生<br>
     * ・メッセージ："writer control operation was failed."<br>
     * ・原因例外：Writer.writer()から発生したIOException<br>
     * ・ファイル名（getFileNameの結果）<br>
     * <br>
     * Writer.writer()からIOExceptionが発生した場合FileExceptionがスローされるのを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPrintDataLine02() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Class<PlainFileLineWriter_Stub01> clazz = PlainFileLineWriter_Stub01.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        PlainFileLineWriter plainFileLineWriter = Mockito.spy(new PlainFileLineWriter(
                fileName, clazz, columnFormatterMap));

        // 引数の設定
        String t = "データ";

        // 前提条件の設定
        IOException exception = new IOException();
        Writer writer = PowerMockito.mock(Writer.class);
        Mockito.doThrow(exception).when(writer).write(Mockito.anyString());
        Mockito.doReturn(writer).when(plainFileLineWriter).getWriter();

        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        try {
            plainFileLineWriter.printDataLine(t);
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            Mockito.verify(plainFileLineWriter).checkWriteTrailer();
            Mockito.verify(plainFileLineWriter, Mockito.never()).getLineFeedChar();
            Mockito.verify(plainFileLineWriter).getWriter();
            Mockito.verify(plainFileLineWriter, Mockito.never()).setWriteData(Mockito.anyBoolean());

            assertSame(FileException.class, e.getClass());
            assertEquals("writer control operation was failed.", e.getMessage());
            assertSame(exception, e.getCause());
            assertEquals(fileName, e.getFileName());
        } finally {
            plainFileLineWriter.closeFile();
        }
    }

    /**
     * testPrintDataLine03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) t:Stringインスタンス<br>
     * (状態) AbstractFileLineWriter#checkWriteTrailer():FileException<br>
     * 例外発生<br>
     * (状態) Writer.writer():正常実行<br>
     * (状態) AbstractFileLineWriter#getLineFeedChar():Stringインスタンス<br>
     * (状態) AbstractFileLineWriter#getWriter():Writerインスタンス<br>
     * (状態) AbstractFileLineWriter#setWriteData():正常実行<br>
     * <br>
     * 期待値：(状態変化) AbstractFileLineWriter#checkWriteTrailer():1回呼ばれる<br>
     * (状態変化) Writer.writer():呼ばれない<br>
     * (状態変化) AbstractFileLineWriter#getLineFeedChar():呼ばれない<br>
     * (状態変化) AbstractFileLineWriter#getWriter():呼ばれない<br>
     * (状態変化) AbstractFileLineWriter#setWriteData():呼ばれない<br>
     * (状態変化) なし:checkWriteTrailer()から発生したFileException<br>
     * <br>
     * AbstractFileLineWriter#checkWriteTrailer()からFileExceptionがスローされるのを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPrintDataLine03() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Class<PlainFileLineWriter_Stub01> clazz = PlainFileLineWriter_Stub01.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        PlainFileLineWriter plainFileLineWriter = Mockito.spy(new PlainFileLineWriter(
                fileName, clazz, columnFormatterMap));

        // 引数の設定
        String t = "データ";

        // 前提条件の設定
        FileException exception = new FileException("checkWriteTrailerからのエラーです");
        Mockito.doThrow(exception).when(plainFileLineWriter).checkWriteTrailer();

        Writer writer = PowerMockito.mock(Writer.class);
        Mockito.doReturn(writer).when(plainFileLineWriter).getWriter();

        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        try {
            plainFileLineWriter.printDataLine(t);
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            Mockito.verify(plainFileLineWriter).checkWriteTrailer();
            Mockito.verify(writer, Mockito.never()).write(Mockito.anyString());
            Mockito.verify(plainFileLineWriter, Mockito.never()).getLineFeedChar();
            Mockito.verify(plainFileLineWriter, Mockito.never()).getWriter();
            Mockito.verify(plainFileLineWriter, Mockito.never()).setWriteData(Mockito.anyBoolean());

            assertSame(exception, e);
        } finally {
            plainFileLineWriter.closeFile();
        }
    }

    /**
     * testPrintDataLine04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) t:null<br>
     * (状態) AbstractFileLineWriter#checkWriteTrailer():正常実行<br>
     * (状態) Writer.writer():正常実行<br>
     * (状態) AbstractFileLineWriter#getLineFeedChar():Stringインスタンス<br>
     * (状態) AbstractFileLineWriter#getWriter():Writerインスタンス<br>
     * (状態) AbstractFileLineWriter#setWriteData():正常実行<br>
     * <br>
     * 期待値：(状態変化) なし:NullPointerException<br>
     * <br>
     * 引数ｔにNullを設定した場合は、例外がスローされることを確認する <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testPrintDataLine04() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Class<PlainFileLineWriter_Stub01> clazz = PlainFileLineWriter_Stub01.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        PlainFileLineWriter plainFileLineWriter = new PlainFileLineWriter(
                fileName, clazz, columnFormatterMap);

        // 引数の設定
        String t = null;

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        try {
            plainFileLineWriter.printDataLine(t);
            fail("NullPointerExceptionがスローされませんでした");
        } catch (NullPointerException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(NullPointerException.class, e.getClass());
        } finally {
            plainFileLineWriter.closeFile();
        }
    }

    /**
     * 正常系<br>
     * FileFormatのencloseCharとdelimiterが設定されていても、無視する
     * @throws Exception
     */
    @Test
    public void testPrintDataLine05() throws Exception {
        // 前処理(ファイル)
        String fileName = TEMP_FILE_NAME;

        // 前処理(試験対象)
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());

        PlainFileLineWriter fileLineWriter = new PlainFileLineWriter(fileName,
                PlainFileLineIterator_Stub02.class, columnFormatterMap);

        // テスト実施
        fileLineWriter.printDataLine("\"1\",'22',\"333\",|4444|");
        fileLineWriter.printDataLine("\"5\",'66',\"777\",|8888|");
        fileLineWriter.printDataLine("\"9\",'AA',\"BBB\",|CCCC|");

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
     * testGetDelimiter01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値： <br>
     * 期待値：(戻り値) char:0<br>
     * <br>
     * getDelimiter()が実行するとき必ず０を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetDelimiter01() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Class<PlainFileLineWriter_Stub01> clazz = PlainFileLineWriter_Stub01.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        PlainFileLineWriter plainFileLineWriter = new PlainFileLineWriter(
                fileName, clazz, columnFormatterMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // なし

        // テスト実施
        char result = plainFileLineWriter.getDelimiter();

        // 返却値の確認
        assertEquals(0, result);

        // 状態変化の確認
        // なし

        plainFileLineWriter.closeFile();
    }

    /**
     * testGetEncloseChar01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値： <br>
     * 期待値：(戻り値) char:0<br>
     * <br>
     * getEncloseChar()が実行するとき必ず０を返却することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetEncloseChar01() throws Exception {
        // テスト対象のインスタンス化
        String fileName = TEMP_FILE_NAME;

        Class<PlainFileLineWriter_Stub01> clazz = PlainFileLineWriter_Stub01.class;
        Map<String, ColumnFormatter> columnFormatterMap = new HashMap<String, ColumnFormatter>();
        columnFormatterMap.put("java.lang.String", new NullColumnFormatter());
        PlainFileLineWriter plainFileLineWriter = new PlainFileLineWriter(
                fileName, clazz, columnFormatterMap);

        // 引数の設定
        // なし

        // 前提条件の設定
        // なし

        // テスト実施
        char result = plainFileLineWriter.getEncloseChar();

        // 返却値の確認
        assertEquals(0, result);

        // 状態変化の確認
        // なし

        plainFileLineWriter.closeFile();
    }

}
