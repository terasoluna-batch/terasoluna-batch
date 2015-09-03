/*
 * $Id: LineFeed2LineReaderTest.java 5354 2007-10-03 06:06:25Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.springframework.test.util.ReflectionTestUtils;

import jp.terasoluna.fw.file.dao.FileException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.LineFeed2LineReader} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> 囲み文字なし、行区切り文字が2文字の場合のファイルから1行分の文字列を取得する処理を行う。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.LineFeed2LineReader
 */
public class LineFeed2LineReaderTest {

    /**
     * testLineFeed2LineReaderReaderString01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E,F <br>
     * <br>
     * 入力値：(引数) reader:Readerのインスタンス<br>
     * (引数) lineFeedChar:"\r\n"<br>
     * <br>
     * 期待値：(状態変化) this.reader:引数readerと同一のインスタンス<br>
     * (状態変化) this.lineFeedChar:引数lineFeedCharと同一のインスタンス<br>
     * <br>
     * 正常ケース。<br>
     * 渡された引数の情報を持つオブジェクトが生成されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testLineFeed2LineReader01() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "\r\n";

        // 前提条件の設定
        // なし

        // テスト実施
        LineFeed2LineReader result = new LineFeed2LineReader(reader, lineFeedChar);

        // 返却値の確認
        // なし

        // 状態変化の確認
        assertSame(reader, ReflectionTestUtils.getField(result, "reader"));
        assertEquals(lineFeedChar, ReflectionTestUtils.getField(result,
                "lineFeedChar"));
    }

    /**
     * testLineFeed2LineReaderReaderString02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) reader:null<br>
     * (引数) lineFeedChar:"\r\n"<br>
     * <br>
     * 期待値：(状態変化) -:IllegalArgumentException<br>
     * ・メッセージ："reader is required."<br>
     * <br>
     * 例外。<br>
     * 引数readerがnullの場合に、発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testLineFeed2LineReader02() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        BufferedReader reader = null;
        String lineFeedChar = "\r\n";

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            new LineFeed2LineReader(reader, lineFeedChar);
            fail("IllegalArgumentExceptionがスローされませんでした");
        } catch (IllegalArgumentException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(IllegalArgumentException.class, e.getClass());
            assertEquals("reader is required.", e.getMessage());
        }
    }

    /**
     * testLineFeed2LineReaderReaderString03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) reader:Readerのインスタンス<br>
     * (引数) lineFeedChar:null<br>
     * <br>
     * 期待値：(状態変化) -:IllegalArgumentException<br>
     * ・メッセージ："lineFeedChar is required."<br>
     * <br>
     * 例外。<br>
     * 引数lineFeedCharがnullの場合に、発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testLineFeed2LineReader03() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String lineFeedChar = null;

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            new LineFeed2LineReader(reader, lineFeedChar);
            fail("IllegalArgumentExceptionがスローされませんでした");
        } catch (IllegalArgumentException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(IllegalArgumentException.class, e.getClass());
            assertEquals("lineFeedChar is required.", e.getMessage());
        }
    }

    /**
     * testLineFeed2LineReaderReaderString04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) reader:Readerのインスタンス<br>
     * (引数) lineFeedChar:"123"<br>
     * <br>
     * 期待値：(状態変化) -:IllegalArgumentException<br>
     * ・メッセージ："lineFeedChar should be defined by 2 digit of character string."<br>
     * <br>
     * 例外。<br>
     * 引数lineFeedCharが2桁文字列ではない場合に、発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testLineFeed2LineReader04() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタなので不要

        // 引数の設定
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "123";

        // 前提条件の設定
        // なし

        // テスト実施
        try {
            new LineFeed2LineReader(reader, lineFeedChar);
            fail("IllegalArgumentExceptionがスローされませんでした");
        } catch (IllegalArgumentException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(IllegalArgumentException.class, e.getClass());
            assertEquals(
                    "lineFeedChar should be defined by 2 digit of character string.",
                    e.getMessage());
        }
    }

    /**
     * testReadLine01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) this.reader:情報を持たないReaderインスタンス<br>
     * (状態) this.lineFeedChar:"\r\n"<br>
     * (状態) Reader.ready():IOException例外が発生する。<br>
     * <br>
     * 期待値：(状態変化) -:以下の情報を持つFileExceptionが発生する<br>
     * ・原因例外：IOException(Reader.ready()で発生したもの)<br>
     * ・メッセージ："Reader control operation was failed."<br>
     * <br>
     * 例外。<br>
     * Readerの状態チェックで例外が発生した場合、 FileExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine01() throws Exception {
        // Mock作成
        Reader reader = mock(Reader.class);
        when(reader.read()).thenThrow(new IOException());

        // テスト対象のインスタンス化
        String lineFeedChar = "\r\n";
        LineFeed2LineReader lineFeed2LineReader = new LineFeed2LineReader(reader, lineFeedChar);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        try {
            lineFeed2LineReader.readLine();
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(FileException.class, e.getClass());
            assertEquals("Reader control operation was failed.", e
                    .getMessage());
            assertSame(IOException.class, e.getCause().getClass());
        }
    }

    /**
     * testReadLine02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) this.reader:以下の情報を持つReaderインスタンス<br>
     * ""(空)<br>
     * <br>
     * ※データがない。<br>
     * (状態) this.lineFeedChar:"\r\n"<br>
     * <br>
     * 期待値：(戻り値) String:""<br>
     * <br>
     * 正常パターン。<br>
     * Readerに取得情報が無い場合、空の文字列が返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine02() throws Exception {
        // テスト対象のインスタンス化
        byte[] buf = "".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "\r\n";
        LineFeed2LineReader lineFeed2LineReader = new LineFeed2LineReader(reader, lineFeedChar);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String result = lineFeed2LineReader.readLine();

        // 返却値の確認
        assertEquals("", result);

        // 状態変化の確認
        // なし
    }

    /**
     * testReadLine03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) this.reader:以下の情報を持つReaderインスタンス<br>
     * "aaa,aaa,aaa\r\n"<br>
     * <br>
     * ※最後の行区切り文字がある1行データ<br>
     * (状態) this.lineFeedChar:"\r\n"<br>
     * <br>
     * 期待値：(戻り値) String:"aaa,aaa,aaa"<br>
     * <br>
     * 正常パターン。<br>
     * Readerに１行の情報のみある場合(かつ最後に行区切り文字あり)、その１行の情報が返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine03() throws Exception {
        // テスト対象のインスタンス化
        String byteParm = "aaa,aaa,aaa\r\n";
        InputStream inputStream = new ByteArrayInputStream(byteParm.getBytes());
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "\r\n";
        LineFeed2LineReader lineFeed2LineReader = new LineFeed2LineReader(reader, lineFeedChar);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String result = lineFeed2LineReader.readLine();

        // 返却値の確認
        assertEquals("aaa,aaa,aaa", result);

        // 状態変化の確認
        // なし
    }

    /**
     * testReadLine04() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) this.reader:以下の情報を持つReaderインスタンス<br>
     * "\"aaa\",\"aaa\",\"aa\r\na\"\r\n"<br>
     * <br>
     * ※行区切り文字が複数ある複数行データ<br>
     * (状態) this.lineFeedChar:"\r\n"<br>
     * <br>
     * 期待値：(戻り値) String:"\"aaa\",\"aaa\",\"aa"<br>
     * <br>
     * 正常パターン。<br>
     * Readerに複数行の情報がある場合、最初の１行の情報のみ返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine04() throws Exception {
        // テスト対象のインスタンス化
        String byteParm = "\"aaa\",\"aaa\",\"aa\r\na\"\r\n";
        InputStream inputStream = new ByteArrayInputStream(byteParm.getBytes());
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "\r\n";
        LineFeed2LineReader lineFeed2LineReader = new LineFeed2LineReader(reader, lineFeedChar);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String result = lineFeed2LineReader.readLine();

        // 返却値の確認
        assertEquals("\"aaa\",\"aaa\",\"aa", result);

        // 状態変化の確認
        // なし
    }

    /**
     * testReadLine05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) this.reader:情報を持たないReaderインスタンス<br>
     * (状態) this.lineFeedChar:"\r\n"<br>
     * (状態) Reader.read():IOException例外が発生する。<br>
     * <br>
     * 期待値：(状態変化) -:以下の情報を持つFileExceptionが発生する<br>
     * ・原因例外：IOException(Reader.read()で発生したもの)<br>
     * ・メッセージ："Reader control operation was failed."<br>
     * <br>
     * 例外。<br>
     * Readerの読み処理で例外が発生した場合、FileExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine05() throws Exception {
        // Mock作成
        Reader reader = mock(Reader.class);
        when(reader.read()).thenThrow(new IOException());

        // テスト対象のインスタンス化
        String lineFeedChar = "\r\n";
        LineFeed2LineReader lineFeed2LineReader = new LineFeed2LineReader(reader, lineFeedChar);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        try {
            lineFeed2LineReader.readLine();
            fail("FileExceptionがスローされませんでした");
        } catch (FileException e) {
            // 返却値の確認
            // なし

            // 状態変化の確認
            assertSame(FileException.class, e.getClass());
            assertEquals("Reader control operation was failed.", e
                    .getMessage());
            assertSame(IOException.class, e.getCause().getClass());
        }
    }

    /**
     * testReadLine06() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) this.reader:以下の情報を持つReaderインスタンス<br>
     * "999,999,999"<br>
     * <br>
     * ※行区切り文字を含めないデータ<br>
     * (状態) this.lineFeedChar:"\r\n"<br>
     * <br>
     * 期待値：(戻り値) String:"999,999,999"<br>
     * <br>
     * 正常パターン。<br>
     * Readerに１行の情報のみある場合(かつ行区切り文字なし)、その１行の情報が返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine06() throws Exception {
        // テスト対象のインスタンス化
        String byteParm = "999,999,999";
        InputStream inputStream = new ByteArrayInputStream(byteParm.getBytes());
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "\r\n";
        LineFeed2LineReader lineFeed2LineReader = new LineFeed2LineReader(reader, lineFeedChar);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String result = lineFeed2LineReader.readLine();

        // 返却値の確認
        assertEquals(byteParm, result);

        // 状態変化の確認
        // なし
    }

    /**
     * testReadLine07() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) this.reader:以下の情報を持つReaderインスタンス<br>
     * "101010,101010,101010\rn"<br>
     * <br>
     * ※行区切り文字の最初の桁文字のみあるデータ<br>
     * (状態) this.lineFeedChar:"\r\n"<br>
     * <br>
     * 期待値：(戻り値) String:"101010,101010,101010\rn"<br>
     * <br>
     * 正常パターン。<br>
     * 行区切り文字の先頭文字のみデータに含まれている場合、その位置で行区切り処理が行わないことを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine07() throws Exception {
        // テスト対象のインスタンス化
        String byteParm = "101010,101010,101010\rn";
        InputStream inputStream = new ByteArrayInputStream(byteParm.getBytes());
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "\r\n";
        LineFeed2LineReader lineFeed2LineReader = new LineFeed2LineReader(reader, lineFeedChar);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String result = lineFeed2LineReader.readLine();

        // 返却値の確認
        assertEquals("101010,101010,101010\rn", result);

        // 状態変化の確認
        // なし

    }

    /**
     * testReadLine08() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E,F <br>
     * <br>
     * 入力値：(状態) this.reader:以下の情報を持つReaderインスタンス<br>
     * "あいう\r\n"<br>
     * <br>
     * ※行区切り文字の最初の桁文字のみあるデータ<br>
     * (状態) this.lineFeedChar:"\r\n"<br>
     * <br>
     * 期待値：(戻り値) String:"あいう"<br>
     * <br>
     * 文字列の中に全角文字が含まれている場合。１行の情報が返却されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine08() throws Exception {
        // テスト対象のインスタンス化
        String byteParm = "あいう\r\n";
        InputStream inputStream = new ByteArrayInputStream(byteParm.getBytes());
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "\r\n";
        LineFeed2LineReader lineFeed2LineReader = new LineFeed2LineReader(reader, lineFeedChar);

        // 引数の設定
        // なし

        // 前提条件の設定
        // テスト対象のインスタンス化時に設定済み

        // テスト実施
        String result = lineFeed2LineReader.readLine();

        // 返却値の確認
        assertEquals("あいう", result);

        // 状態変化の確認
        // なし

    }
}
