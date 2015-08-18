/*
 * $Id: EncloseCharLineFeed2LineReaderTest.java 5354 2007-10-03 06:06:25Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.utlib.UTUtil;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.EncloseCharLineFeed2LineReader} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> 囲み文字有り、行区切り文字が2文字の場合のファイルから1行分の文字列を取得する処理を行う。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.EncloseCharLineFeed2LineReader
 */
public class EncloseCharLineFeed2LineReaderTest {

    /**
     * testEncloseCharLineFeed2LineReader01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) delimiterCharacter:not null<br>
     * 'a'<br>
     * (引数) encloseCharacter:not null<br>
     * 'b'<br>
     * (引数) reader:not null<br>
     * BufferedReaderのインスタンス<br>
     * (引数) lineFeedChar:not noll<br>
     * "\r\n"<br>
     * <br>
     * 期待値：(状態変化) this.delimiterCharacter:引数delimiterCharacterと同じインスタンス<br>
     * (状態変化) this.encloseCharacter:引数encloseCharacterと同じインスタンス<br>
     * (状態変化) this.reader:引数readerと同じインスタンス<br>
     * (状態変化) this.lineFeedChar:引数lineFeedCharと同じインスタンス<br>
     * <br>
     * オブジェクトが生成できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testEncloseCharLineFeed2LineReader01() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        char dChar = 'a';
        char[] eChar = { 'b' };
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r\n";

        // 前提条件なし

        // テスト実施
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 返却値なし

        // 状態変化の確認
        char delimiterCharacter = (Character) UTUtil.getPrivateField(testCalss,
                "delimiterCharacter");
        assertEquals(dChar, delimiterCharacter);
        char[] encloseCharacter = (char[]) UTUtil.getPrivateField(testCalss,
                "columnEncloseCharacter");
        assertEquals(eChar, encloseCharacter);
        Reader reader = (Reader) UTUtil.getPrivateField(testCalss, "reader");
        assertEquals(bufferedReader, reader);
        String lineFeedChar = (String) UTUtil.getPrivateField(testCalss,
                "lineFeedChar");
        assertEquals(parm, lineFeedChar);
    }

    /**
     * testEncloseCharLineFeed2LineReader02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) delimiterCharacter:Character.MIN_VALUE<br>
     * (引数) encloseCharacter:\"<br>
     * (引数) reader:BufferedReaderのインスタンス<br>
     * (引数) lineFeedChar:"\r\n"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："delimiterCharacter can not use '\\u0000'."<br>
     * <br>
     * 区切り文字が'\u0000'だった場合、IllegalArgumentExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testEncloseCharLineFeed2LineReader02() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        char delimiterCharacter = Character.MIN_VALUE;
        char[] encloseCharacter = { '"' };
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "\r\n";

        // 前提条件なし

        // テスト実施
        try {
            new EncloseCharLineFeed2LineReader(delimiterCharacter,
                    Character.MIN_VALUE, encloseCharacter, bufferedReader,
                    lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("delimiterCharacter can not use '\\u0000'.", e
                    .getMessage());
        }
    }

    /**
     * testEncloseCharLineFeed2LineReader03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) delimiterCharacter:,'<br>
     * (引数) encloseCharacter:Character.MIN_VALUE<br>
     * (引数) reader:BufferedReaderのインスタンス<br>
     * (引数) lineFeedChar:""<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："encloseCharacter can not use '\\u0000'."<br>
     * <br>
     * 囲み文字が'\u0000'だった場合、IllegalArgumentExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testEncloseCharLineFeed2LineReader03() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        char delimiterCharacter = ',';
        char[] encloseCharacter = null;
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "\r\n";

        // 前提条件なし

        // テスト実施
        try {
            new EncloseCharLineFeed2LineReader(delimiterCharacter,
                    Character.MIN_VALUE, encloseCharacter, bufferedReader,
                    lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("columnEncloseCharacter is required.", e.getMessage());
        }
    }

    /**
     * testEncloseCharLineFeed2LineReader04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) delimiterCharacter:,'<br>
     * (引数) encloseCharacter:"\"<br>
     * (引数) reader:null<br>
     * (引数) lineFeedChar:""<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："reader is required."<br>
     * <br>
     * ファイルアクセス用の文字ストリームがnullだった場合、IllegalArgumentExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testEncloseCharLineFeed2LineReader04() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        char delimiterCharacter = ',';
        char[] encloseCharacter = { '\"' };
        BufferedReader bufferedReader = null;
        String lineFeedChar = "\r\n";

        // 前提条件なし

        // テスト実施
        try {
            new EncloseCharLineFeed2LineReader(delimiterCharacter,
                    Character.MIN_VALUE, encloseCharacter, bufferedReader,
                    lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("reader is required.", e.getMessage());
        }
    }

    /**
     * testEncloseCharLineFeed2LineReader05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) delimiterCharacter:,'<br>
     * (引数) encloseCharacter:"\"<br>
     * (引数) reader:BufferedReaderのインスタンス<br>
     * (引数) lineFeedChar:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："lineFeedChar is required."<br>
     * <br>
     * 行区切り文字がnullだった場合、IllegalArgumentExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testEncloseCharLineFeed2LineReader05() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        char delimiterCharacter = ',';
        char[] encloseCharacter = { '\"' };
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lineFeedChar = null;

        // 前提条件なし

        // テスト実施
        try {
            new EncloseCharLineFeed2LineReader(delimiterCharacter,
                    Character.MIN_VALUE, encloseCharacter, bufferedReader,
                    lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("lineFeedChar is required.", e.getMessage());
        }
    }

    /**
     * testEncloseCharLineFeed2LineReader06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) delimiterCharacter:,'<br>
     * (引数) encloseCharacter:"\"<br>
     * (引数) reader:BufferedReaderのインスタンス<br>
     * (引数) lineFeedChar:""<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："lineFeedChar should be defined by 2 digit of character string."<br>
     * <br>
     * 行区切り文字が2文字以外だった場合、IllegalArgumentExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testEncloseCharLineFeed2LineReader06() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        char delimiterCharacter = ',';
        char[] encloseCharacter = { '\"' };
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "";

        // 前提条件なし

        // テスト実施
        try {
            new EncloseCharLineFeed2LineReader(delimiterCharacter,
                    Character.MIN_VALUE, encloseCharacter, bufferedReader,
                    lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("lineFeedChar should be defined by 2"
                    + " digit of character string.", e.getMessage());
        }
    }

    /**
     * testEncloseCharLineFeed2LineReader07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) delimiterCharacter:,'<br>
     * (引数) encloseCharacter:"\"<br>
     * (引数) reader:BufferedReaderのインスタンス<br>
     * (引数) lineFeedChar:"\r"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："lineFeedChar should be defined by 2 digit of character string."<br>
     * <br>
     * 行区切り文字が2文字以外だった場合、IllegalArgumentExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testEncloseCharLineFeed2LineReader07() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        char delimiterCharacter = ',';
        char[] encloseCharacter = { '\"' };
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "\r";

        // 前提条件なし

        // テスト実施
        try {
            new EncloseCharLineFeed2LineReader(delimiterCharacter,
                    Character.MIN_VALUE, encloseCharacter, bufferedReader,
                    lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("lineFeedChar should be defined by 2"
                    + " digit of character string.", e.getMessage());
        }
    }

    /**
     * testReadLine01() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) reader:not null<br>
     * Reader実装スタブ<br>
     * #ready(), #read()でIOExceptionをスローする<br>
     * (状態) lineFeedChar:not null<br>
     * "\r\n"<br>
     * (状態) delimiter:not null<br>
     * 'a'<br>
     * (状態) encloseCharacter:not null<br>
     * 'b'<br>
     * <br>
     * 期待値：(状態変化) なし:IOExceptionが発生。FileExceptioにラップされることを確認する。<br>
     * メッセージ："Reader control operation was failed."<br>
     * <br>
     * Readerがデータの読み取りに失敗した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine01() throws Exception {
        // Mock作成
        Reader reader = EasyMock.createMock(Reader.class);
        EasyMock.expect(reader.read()).andReturn(null).andThrow(
                new IOException());
        EasyMock.replay(reader);

        // テスト対象のインスタンス化
        char dChar = 'a';
        char[] eChar = { 'b' };
        String parm = "\r\n";
        EncloseCharLineFeed2LineReader target = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, reader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される。)

        // テスト実施
        try {
            target.readLine();
            fail("FileExceptionがスローされませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(FileException.class, e.getClass());
            assertEquals(IOException.class.getName(), e.getCause().getClass()
                    .getName());
            assertEquals("Reader control operation was failed.", e.getMessage());
        }
    }

    /**
     * testReadLine02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:""(空)<br>
     * (状態) reader:not null<br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:not null<br>
     * "\r\n"<br>
     * (状態) delimiter:not null<br>
     * 'a'<br>
     * (状態) encloseCharacter:not null<br>
     * 'b'<br>
     * <br>
     * 期待値：(戻り値) String:""<br>
     * <br>
     * 読み込んだデータがない（空文字）の場合、空文字が文字列として返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine02() throws Exception {
        // テスト対象のインスタンス化
        char dChar = 'a';
        char[] eChar = { 'b' };
        byte[] buf = "".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される。)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("", result);

        // 状態変化なし
    }

    /**
     * testReadLine03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"aaa,aaa,aaa\r\n"<br>
     * (状態) reader:not null<br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r\n'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"aaa,aaa,aaa"<br>
     * <br>
     * 囲み文字がない場合は、行区切り文字を除いた文字列が返却される <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine03() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "aaa,aaa,aaa\r\n".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される。)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("aaa,aaa,aaa", result);

        // 状態変化なし
    }

    /**
     * testReadLine04() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"\"aaa\",\"aaa\",\"aaa\"\r\n"<br>
     * (状態) reader:not null<br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r\n'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"\"aaa\",\"aaa\",\"aaa\""<br>
     * <br>
     * 囲み文字がある場合は、最後の行区切り文字を除いた文字列が返却される <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine04() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "\"aaa\",\"aaa\",\"aaa\"\r\n".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される。)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("\"aaa\",\"aaa\",\"aaa\"", result);

        // 状態変化なし
    }

    /**
     * testReadLine05() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"\"aa,a\",\"aa\"\"a\",\"aa\r\na\"\r\n"<br>
     * (状態) reader:not null<br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r\n'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"\"aa,a\",\"aa\"\"a\",\"aa\r\na\""<br>
     * <br>
     * 文字列の中に囲み文字、区切り文字、行区切り文字が含まれている場合。　（囲み文字のエスケープなど） <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine05() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "\"aa,a\",\"aa\"\"a\",\"aa\r\na\"\r\n".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される。)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("\"aa,a\",\"aa\"\"a\",\"aa\r\na\"", result);

        // 状態変化なし
    }

    /**
     * testReadLine06() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"\"aaa\",\"aaa\",\"aaa\"aaa\r\n"<br>
     * (状態) reader:not null<br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r\n'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"\"aaa\",\"aaa\",\"aaa\"aaa"<br>
     * <br>
     * 文字列が1行返却される。囲み文字(終了側)の後ろに文字列がつながる場合、区切り文字もしくは行区切り文字が来るまでの文字列は全て前のカラムに含まれる。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine06() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "\"aaa\",\"aaa\",\"aaa\"aaa\r\n".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される。)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("\"aaa\",\"aaa\",\"aaa\"aaa", result);

        // 状態変化なし
    }

    /**
     * testReadLine07() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"\r\n"<br>
     * (状態) reader:not null<br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r\n'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:""<br>
     * <br>
     * 文字列の中に行区切り文字のみ。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine07() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "\r\n".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される。)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("", result);
    }

    /**
     * testReadLine08() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"\n\r\n"<br>
     * (状態) reader:not null<br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r\n'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"\n"<br>
     * <br>
     * 行区切り文字を構成している文字列の１部＋行区切り文字を読み込んだ場合、構成文字のみ返却される <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine08() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "\n\r\n".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される。)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("\n", result);

        // 状態変化なし
    }

    /**
     * testReadLine09() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"aaa"<br>
     * (状態) reader:not null<br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r\n'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"aaa"<br>
     * <br>
     * 行区切り文字がないデータを読み込んだ場合、最終データまでの文字列が返却される <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine09() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "aaa".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される。)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("aaa", result);

        // 状態変化なし
    }

    /**
     * testReadLine10() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"aaa,aaa,aaa\r\naaa,aaa"<br>
     * (状態) reader:not null<br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r\n'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"aaa,aaa,aaa"<br>
     * <br>
     * 読み込んだデータの途中に行区切り文字が含まれている場合、そこまでの文字列が返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine10() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "aaa,aaa,aaa\r\naaa,aaa".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される。)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("aaa,aaa,aaa", result);

        // 状態変化なし
    }

    /**
     * testReadLine11() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"aaa,aa\ra,aaa\r\n"<br>
     * (状態) reader:not null<br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r\n'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"aaa,aa\ra,aaa"<br>
     * <br>
     * 読み込んだデータの途中に\rが含まれている場合、文字列の一部に含まれる。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine11() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "aaa,aa\ra,aaa\r\n".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される。)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("aaa,aa\ra,aaa", result);

        // 状態変化なし
    }

    /**
     * testReadLine12() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"aaa,ａａａ,ａａａ\r\n"<br>
     * (状態) reader:not null<br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r\n'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"aaa,ａａａ,ａａａ"<br>
     * <br>
     * 読み込んだデータの途中に全角文字が含まれる場合。<br>
     * 行区切り文字を除いた文字列が返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine12() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "aaa,ａａａ,ａａａ\r\n".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される。)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("aaa,ａａａ,ａａａ", result);

        // 状態変化なし
    }

    /**
     * 正常系<br>
     * カラム毎の囲み文字設定有り
     * @throws Exception
     */
    @Test
    public void testReadLine13() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', Character.MIN_VALUE, '|' };
        byte[] buf = "\"aaa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // テスト実施
        String result = testCalss.readLine();

        // 返却値なし
        assertEquals("\"aaa\",\"aaa\",|aaaaaa|", result);
    }

    /**
     * 正常系<br>
     * カラム毎の囲み文字設定有り
     * @throws Exception
     */
    @Test
    public void testReadLine14() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', Character.MIN_VALUE, '|' };
        byte[] buf = "\"a\r\naa\",\"aaa\",|aaa\r\naaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // テスト実施
        String result = testCalss.readLine();

        // 返却値なし
        assertEquals("\"a\r\naa\",\"aaa\",|aaa\r\naaa|", result);
    }

    /**
     * 正常系<br>
     * eColChar.length : 0
     * @throws Exception
     */
    @Test
    public void testReadLine15() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eColChar = {};
        char eChar = '\"';
        byte[] buf = "\"a\r\naa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, eChar, eColChar, bufferedReader, parm);

        // テスト実施
        String result = testCalss.readLine();

        // 返却値なし
        assertEquals("\"a\r\naa\",\"aaa\",|aaaaaa|", result);
    }

    /**
     * testGetEncloseCharcter001.
     * @throws Exception
     */
    @Test
    public void testGetEncloseCharcter001() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eColChar = { '\"', '\"', '\"', '\"' };
        char eChar = '\'';
        byte[] buf = "\"a\raa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, eChar, eColChar, bufferedReader, parm);

        int index = 0;

        // テスト実施
        Object result = UTUtil.invokePrivate(testCalss, "getEncloseCharcter",
                int.class, index);

        // 返却値なし
        assertNotNull(result);
        assertEquals(Character.valueOf('\"'), (Character) result);
    }

    /**
     * testGetEncloseCharcter002.
     * @throws Exception
     */
    @Test
    public void testGetEncloseCharcter002() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eColChar = { '\"', '\"', '\"', '\"' };
        char eChar = '\'';
        byte[] buf = "\"a\raa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, eChar, eColChar, bufferedReader, parm);

        int index = 1;

        // テスト実施
        Object result = UTUtil.invokePrivate(testCalss, "getEncloseCharcter",
                int.class, index);

        // 返却値なし
        assertNotNull(result);
        assertEquals(Character.valueOf('\"'), (Character) result);
    }

    /**
     * testGetEncloseCharcter003.
     * @throws Exception
     */
    @Test
    public void testGetEncloseCharcter003() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eColChar = { '\"', '\"', '\"', '\"' };
        char eChar = '\'';
        byte[] buf = "\"a\raa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, eChar, eColChar, bufferedReader, parm);

        int index = 3;

        // テスト実施
        Object result = UTUtil.invokePrivate(testCalss, "getEncloseCharcter",
                int.class, index);

        // 返却値なし
        assertNotNull(result);
        assertEquals(Character.valueOf('\"'), (Character) result);
    }

    /**
     * testGetEncloseCharcter004.
     * @throws Exception
     */
    @Test
    public void testGetEncloseCharcter004() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eColChar = { '\"', '\"', '\"', '\"' };
        char eChar = '\'';
        byte[] buf = "\"a\raa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, eChar, eColChar, bufferedReader, parm);

        int index = 4;

        // テスト実施
        Object result = UTUtil.invokePrivate(testCalss, "getEncloseCharcter",
                int.class, index);

        // 返却値なし
        assertNotNull(result);
        assertEquals(Character.valueOf('\''), (Character) result);
    }

    /**
     * testGetEncloseCharcter005.
     * @throws Exception
     */
    @Test
    public void testGetEncloseCharcter005() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eColChar = { '\"', '\"', '\"', '\"' };
        char eChar = '\'';
        byte[] buf = "\"a\raa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r\n";
        EncloseCharLineFeed2LineReader testCalss = new EncloseCharLineFeed2LineReader(
                dChar, eChar, eColChar, bufferedReader, parm);

        int index = 5;

        // テスト実施
        Object result = UTUtil.invokePrivate(testCalss, "getEncloseCharcter",
                int.class, index);

        // 返却値なし
        assertNotNull(result);
        assertEquals(Character.valueOf('\''), (Character) result);
    }

}
