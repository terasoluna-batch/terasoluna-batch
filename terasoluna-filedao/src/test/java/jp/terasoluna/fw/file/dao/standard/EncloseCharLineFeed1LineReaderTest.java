/*
 * $Id: EncloseCharLineFeed1LineReaderTest.java 5354 2007-10-03 06:06:25Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao.standard;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;

import org.springframework.test.util.ReflectionTestUtils;

import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.EncloseCharLineFeed1LineReader} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> 囲み文字有り、行区切り文字が1文字の場合のファイルから1行分の文字列を取得する処理を行う。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.standard.EncloseCharLineFeed1LineReader
 */
public class EncloseCharLineFeed1LineReaderTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(EncloseCharLineFeed1LineReaderTest.class);
    }

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
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
    public EncloseCharLineFeed1LineReaderTest(String name) {
        super(name);
    }

    /**
     * testEncloseCharLineFeed1LineReader01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) delimiterCharacter:a'<br>
     * (引数) encloseCharacter:b'<br>
     * (引数) reader:BufferedReaderのインスタンス<br>
     * (引数) lineFeedChar:"\r"<br>
     * <br>
     * 期待値：(状態変化) this.delimiterCharacter:引数delimiterCharacterと同じインスタンス<br>
     * (状態変化) this.encloseCharacter:引数encloseCharacterと同じインスタンス<br>
     * (状態変化) this.reader:引数readerと同じインスタンス<br>
     * (状態変化) this.lineFeedChar:引数lineFeedCharと同じインスタンス<br>
     * <br>
     * オブジェクトが生成できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testEncloseCharLineFeed1LineReader01() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        char delimiterCharacter = 'a';
        char[] encloseCharacter = { 'b' };
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "\r";

        // 前提条件なし

        // テスト実施
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(delimiterCharacter, Character.MIN_VALUE, encloseCharacter, bufferedReader, lineFeedChar);

        // 返却値なし

        // 状態変化の確認
        char char01 = (Character) ReflectionTestUtils.getField(testCalss,
                "delimiterCharacter");
        assertEquals(delimiterCharacter, char01);
        char[] char02 = (char[]) ReflectionTestUtils.getField(testCalss,
                "columnEncloseCharacter");
        assertEquals(encloseCharacter, char02);
        Reader reader = (Reader) ReflectionTestUtils.getField(testCalss, "reader");
        assertEquals(bufferedReader, reader);
        String getLineFeedChar = (String) ReflectionTestUtils.getField(testCalss,
                "lineFeedChar");
        assertEquals(lineFeedChar, getLineFeedChar);
    }

    /**
     * testEncloseCharLineFeed1LineReader02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) delimiterCharacter:Character.MIN_VALUE<br>
     * (引数) encloseCharacter:\"<br>
     * (引数) reader:BufferedReaderのインスタンス<br>
     * (引数) lineFeedChar:""<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："delimiterCharacter can not use '\\u0000'."<br>
     * <br>
     * 区切り文字が'\u0000'だった場合、IllegalArgumentExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testEncloseCharLineFeed1LineReader02() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        char delimiterCharacter = Character.MIN_VALUE;
        char[] encloseCharacter = { '"' };
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "";

        // 前提条件なし

        // テスト実施
        try {
            new EncloseCharLineFeed1LineReader(delimiterCharacter, Character.MIN_VALUE, encloseCharacter, bufferedReader, lineFeedChar);
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
     * testEncloseCharLineFeed1LineReader03() <br>
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
    public void testEncloseCharLineFeed1LineReader03() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        char delimiterCharacter = ',';
        char[] encloseCharacter = null;
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "";

        // 前提条件なし

        // テスト実施
        try {
            new EncloseCharLineFeed1LineReader(delimiterCharacter, Character.MIN_VALUE, encloseCharacter, bufferedReader, lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("columnEncloseCharacter is required.", e.getMessage());
        }
    }

    /**
     * testEncloseCharLineFeed1LineReader04() <br>
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
    public void testEncloseCharLineFeed1LineReader04() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        char delimiterCharacter = ',';
        char[] encloseCharacter = { '\"' };
        BufferedReader bufferedReader = null;
        String lineFeedChar = "";

        // 前提条件なし

        // テスト実施
        try {
            new EncloseCharLineFeed1LineReader(delimiterCharacter, Character.MIN_VALUE, encloseCharacter, bufferedReader, lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("reader is required.", e.getMessage());
        }
    }

    /**
     * testEncloseCharLineFeed1LineReader05() <br>
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
    public void testEncloseCharLineFeed1LineReader05() throws Exception {
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
            new EncloseCharLineFeed1LineReader(delimiterCharacter, Character.MIN_VALUE, encloseCharacter, bufferedReader, lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("lineFeedChar is required.", e.getMessage());
        }
    }

    /**
     * testEncloseCharLineFeed1LineReader06() <br>
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
     * メッセージ："lineFeedChar should be defined by 1 digit of character string."<br>
     * <br>
     * 行区切り文字が1文字以外だった場合、IllegalArgumentExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testEncloseCharLineFeed1LineReader06() throws Exception {
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
            new EncloseCharLineFeed1LineReader(delimiterCharacter, Character.MIN_VALUE, encloseCharacter, bufferedReader, lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("lineFeedChar should be defined by 1"
                    + " digit of character string.", e.getMessage());
        }
    }

    /**
     * testEncloseCharLineFeed1LineReader07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) delimiterCharacter:,'<br>
     * (引数) encloseCharacter:"\"<br>
     * (引数) reader:BufferedReaderのインスタンス<br>
     * (引数) lineFeedChar:"\r\n"<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     * メッセージ："lineFeedChar should be defined by 1 digit of character string."<br>
     * <br>
     * 行区切り文字が1文字以外だった場合、IllegalArgumentExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testEncloseCharLineFeed1LineReader07() throws Exception {
        // テスト対象のインスタンス化なし

        // 引数の設定
        char delimiterCharacter = ',';
        char[] encloseCharacter = { '\"' };
        byte[] buf = {};
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lineFeedChar = "\r\n";

        // 前提条件なし

        // テスト実施
        try {
            new EncloseCharLineFeed1LineReader(delimiterCharacter, Character.MIN_VALUE, encloseCharacter, bufferedReader, lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("lineFeedChar should be defined by 1"
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
     * (状態) lineFeedChar:''\r'<br>
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
    public void testReadLine01() throws Exception {
        // Mock作成
        Reader reader = mock(Reader.class);
        when(reader.read()).thenThrow(new IOException());

        // テスト対象のインスタンス化
        char delimiterCharacter = 'a';
        char[] encloseCharacter = { 'b' };
        String parm = "\r";
        EncloseCharLineFeed1LineReader target = new EncloseCharLineFeed1LineReader(delimiterCharacter, Character.MIN_VALUE, encloseCharacter, reader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        try {
            target.readLine();
            fail("FileExceptionがスローされなかった");
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
     * (状態) reader:not null <br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r'<br>
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
    public void testReadLine02() throws Exception {
        // テスト対象のインスタンス化
        char dChar = 'a';
        char[] eChar = { 'b' };
        byte[] buf = "".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される)

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
     * 入力値：(状態) readerが読み取る文字列:"aaa,aaa,aaa\r"<br>
     * (状態) reader:not null <br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"aaa,aaa,aaa"<br>
     * <br>
     * 囲み文字がない場合は、行区切り文字を除いた文字列が返却される <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testReadLine03() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "aaa,aaa,aaa\r".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値なし
        assertEquals("aaa,aaa,aaa", result);
    }

    /**
     * testReadLine04() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"\"aaa\",\"aaa\",\"aaa\"\r"<br>
     * (状態) reader:not null <br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"\"aaa\",\"aaa\",\"aaa\""<br>
     * <br>
     * 囲み文字がある場合は、囲み文字があるままの文字列が返却される。<br>
     * 行区切り文字を除いた文字列が返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testReadLine04() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "\"aaa\",\"aaa\",\"aaa\"\r".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される)

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
     * 入力値：(状態) readerが読み取る文字列:"\"aa,a\",\"aa\"\"a\",\"aa\ra\"\r"<br>
     * (状態) reader:not null <br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"\"aa,a\",\"aa\"\"a\",\"aa\ra\""<br>
     * <br>
     * 文字列の中に囲み文字、区切り文字、行区切り文字が含まれている場合。　（囲み文字のエスケープなど）<br>
     * 行区切り文字を除いた文字列が返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testReadLine05() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "\"aa,a\",\"aa\"\"a\",\"aa\ra\"\r".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("\"aa,a\",\"aa\"\"a\",\"aa\ra\"", result);

        // 状態変化なし
    }

    /**
     * testReadLine06() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"\"aaa\",\"aaa\",\"aaa\"aaa\r"<br>
     * (状態) reader:not null <br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"\"aaa\",\"aaa\",\"aaa\"aaa"<br>
     * <br>
     * 文字列が1行返却される。囲み文字(終了側)の後ろに文字列がつながる場合、区切り文字もしくは行区切り文字が来るまでの文字列は全て前のカラムに含まれる。<br>
     * 行区切り文字を除いた文字列が返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testReadLine06() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "\"aaa\",\"aaa\",\"aaa\"aaa\r".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される)

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
     * 入力値：(状態) readerが読み取る文字列:"\r"<br>
     * (状態) reader:not null <br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:""<br>
     * <br>
     * 文字列の中に行区切り文字のみ。<br>
     * 行区切り文字を除いた文字列が返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testReadLine07() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"' };
        byte[] buf = "\r".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("", result);

        // 状態変化なし
    }

    /**
     * testReadLine08() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"aaa"<br>
     * (状態) reader:not null <br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:\r'<br>
     * (状態) delimiter:,'<br>
     * (状態) encloseCharacter:\"'<br>
     * <br>
     * 期待値：(戻り値) String:"aaa"<br>
     * <br>
     * 行区切り文字がないデータを読み込んだ場合、最終データまでの文字列が返却される <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testReadLine08() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"' };
        byte[] buf = "aaa".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値の確認
        assertEquals("aaa", result);

        // 状態変化なし
    }

    /**
     * testReadLine09() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"aaa,aaa,aaa\raaa,aaa"<br>
     * (状態) reader:not null <br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"aaa,aaa,aaa"<br>
     * <br>
     * 読み込んだデータに改行文字が含まれていたらそこまでの文字列が返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testReadLine09() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "aaa,aaa,aaa\raaa,aaa".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値なし
        assertEquals("aaa,aaa,aaa", result);
    }

    /**
     * testReadLine10() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"ａａａ,ａａａ,ａａａ\r"<br>
     * (状態) reader:not null <br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"ａａａ,ａａａ,ａａａ"<br>
     * <br>
     * 文字列の中に、全角文字が含まれている場合。<br>
     * 行区切り文字を除いた文字列が返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testReadLine10() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "ａａａ,ａａａ,ａａａ\r".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値なし
        assertEquals("ａａａ,ａａａ,ａａａ", result);
    }

    /**
     * testReadLine11() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"aaa,aaa,aaaaaa\r\naaa"<br>
     * (状態) reader:not null <br>
     * BufferedReaderのインスタンス<br>
     * (状態) lineFeedChar:'\r'<br>
     * (状態) delimiter:','<br>
     * (状態) encloseCharacter:'\"'<br>
     * <br>
     * 期待値：(戻り値) String:"aaa,aaa,aaaaaa"<br>
     * <br>
     * 読み込んだデータに改行文字(\r\n)が含まれていた場合、\rまでの文字列が返却される。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testReadLine11() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', '\"', '\"', '\"' };
        byte[] buf = "aaa,aaa,aaaaaa\r\naaa".getBytes();
        InputStream inputStream = new ByteArrayInputStream(buf);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // 引数なし

        // 前提条件(インスタンス化で設定される)

        // テスト実施
        String result = testCalss.readLine();

        // 返却値なし
        assertEquals("aaa,aaa,aaaaaa", result);
    }

    /**
     * 正常系<br>
     * カラム毎の囲み文字設定有り
     * @throws Exception
     */
    public void testReadLine12() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', Character.MIN_VALUE, '|' };
        byte[] buf = "\"aaa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

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
    public void testReadLine13() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eChar = { '\"', Character.MIN_VALUE, '|' };
        byte[] buf = "\"a\raa\",\"aaa\",|aaa\raaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, Character.MIN_VALUE, eChar, bufferedReader, parm);

        // テスト実施
        String result = testCalss.readLine();

        // 返却値なし
        assertEquals("\"a\raa\",\"aaa\",|aaa\raaa|", result);
    }

    /**
     * 正常系<br>
     * eColChar.length : 0
     * @throws Exception
     */
    public void testReadLine14() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eColChar = {};
        char eChar = '\"';
        byte[] buf = "\"a\raa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, eChar, eColChar, bufferedReader, parm);

        // テスト実施
        String result = testCalss.readLine();

        // 返却値なし
        assertEquals("\"a\raa\",\"aaa\",|aaaaaa|", result);
    }

    /**
     * testGetEncloseCharcter001.
     * @throws Exception
     */
    public void testGetEncloseCharcter001() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eColChar = { '\"', '\"', '\"', '\"' };
        char eChar = '\'';
        byte[] buf = "\"a\raa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, eChar, eColChar, bufferedReader, parm);

        int index = 0;

        // テスト実施
        Method method = EncloseCharLineFeed1LineReader.class.getDeclaredMethod("getEncloseCharcter", 
                int.class);
        method.setAccessible(true);
        Object result = method.invoke(testCalss, index);

        // 返却値なし
        assertNotNull(result);
        assertEquals(Character.valueOf('\"'), (Character) result);
    }

    /**
     * testGetEncloseCharcter002.
     * @throws Exception
     */
    public void testGetEncloseCharcter002() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eColChar = { '\"', '\"', '\"', '\"' };
        char eChar = '\'';
        byte[] buf = "\"a\raa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, eChar, eColChar, bufferedReader, parm);

        int index = 1;

        // テスト実施
        Method method = EncloseCharLineFeed1LineReader.class.getDeclaredMethod("getEncloseCharcter", 
                int.class);
        method.setAccessible(true);
        Object result = method.invoke(testCalss, index);

        // 返却値なし
        assertNotNull(result);
        assertEquals(Character.valueOf('\"'), (Character) result);
    }

    /**
     * testGetEncloseCharcter003.
     * @throws Exception
     */
    public void testGetEncloseCharcter003() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eColChar = { '\"', '\"', '\"', '\"' };
        char eChar = '\'';
        byte[] buf = "\"a\raa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, eChar, eColChar, bufferedReader, parm);

        int index = 3;

        // テスト実施
        Method method = EncloseCharLineFeed1LineReader.class.getDeclaredMethod("getEncloseCharcter", 
                int.class);
        method.setAccessible(true);
        Object result = method.invoke(testCalss, index);

        // 返却値なし
        assertNotNull(result);
        assertEquals(Character.valueOf('\"'), (Character) result);
    }

    /**
     * testGetEncloseCharcter004.
     * @throws Exception
     */
    public void testGetEncloseCharcter004() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eColChar = { '\"', '\"', '\"', '\"' };
        char eChar = '\'';
        byte[] buf = "\"a\raa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, eChar, eColChar, bufferedReader, parm);

        int index = 4;

        // テスト実施
        Method method = EncloseCharLineFeed1LineReader.class.getDeclaredMethod("getEncloseCharcter", 
                int.class);
        method.setAccessible(true);
        Object result = method.invoke(testCalss, index);

        // 返却値なし
        assertNotNull(result);
        assertEquals(Character.valueOf('\''), (Character) result);
    }

    /**
     * testGetEncloseCharcter005.
     * @throws Exception
     */
    public void testGetEncloseCharcter005() throws Exception {
        // テスト対象のインスタンス化
        char dChar = ',';
        char[] eColChar = { '\"', '\"', '\"', '\"' };
        char eChar = '\'';
        byte[] buf = "\"a\raa\",\"aaa\",|aaaaaa|\r\naaa".getBytes();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));

        String parm = "\r";
        EncloseCharLineFeed1LineReader testCalss = new EncloseCharLineFeed1LineReader(dChar, eChar, eColChar, bufferedReader, parm);

        int index = 5;

        // テスト実施
        Method method = EncloseCharLineFeed1LineReader.class.getDeclaredMethod("getEncloseCharcter", 
                int.class);
        method.setAccessible(true);
        Object result = method.invoke(testCalss, index);

        // 返却値なし
        assertNotNull(result);
        assertEquals(Character.valueOf('\''), (Character) result);
    }

}
