package jp.terasoluna.fw.file.dao.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.springframework.test.util.ReflectionTestUtils;

import jp.terasoluna.fw.file.dao.FileException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.file.dao.standard.LineFeed1LineReader} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> 囲み文字なし、行区切り文字が1文字の場合のファイルから1行分の文字列を取得する処理を行う。
 * <p>
 * @see jp.terasoluna.fw.file.dao.standard.LineFeed1LineReader
 */
public class LineFeed1LineReaderTest {

    /**
     * testLineFeed1LineReader01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) reader:Readerインスタンス<br>
     * (引数) lineFeedChar:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) this.reader:引数readerと同一のインスタンス<br>
     * (状態変化) this.lineFeedChar:引数lineFeedCharと同一のインスタンス<br>
     * <br>
     * オブジェクトが生成できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testLineFeed1LineReader01() throws Exception {
        // 前処理(引数)
        Reader reader = new BufferedReader(new StringReader(""));
        String lineFeedChar = new String("a");

        // テスト実施
        LineFeed1LineReader lineFeed1LineReader = new LineFeed1LineReader(reader, lineFeedChar);

        // 判定
        assertNotNull(lineFeed1LineReader);

        Reader lineFeed1Reader = (Reader) ReflectionTestUtils.getField(
                lineFeed1LineReader, "reader");
        assertSame(reader, lineFeed1Reader);

        String lineFeed1LineFeedChar = (String) ReflectionTestUtils.getField(
                lineFeed1LineReader, "lineFeedChar");
        assertSame(lineFeedChar, lineFeed1LineFeedChar);
    }

    /**
     * testLineFeed1LineReader02() <br>
     * <br>
     * (異常系) <br>
     * 観点：E,G <br>
     * <br>
     * 入力値：(引数) reader:null<br>
     * (引数) lineFeedChar:Stringインスタンス<br>
     * <br>
     * 期待値：(状態変化) なし:IllegalArgumentExceptionが発生する<br>
     * ・メッセージ："reader is required."<br>
     * <br>
     * 引数readerがnullの場合IllegalArgumentExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testLineFeed1LineReader02() throws Exception {
        // 前処理(引数)
        Reader reader = null;
        String lineFeedChar = new String();

        try {
            // テスト実施
            new LineFeed1LineReader(reader, lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("reader is required.", e.getMessage());
        }
    }

    /**
     * testLineFeed1LineReader03() <br>
     * <br>
     * (異常系) <br>
     * 観点：E,G <br>
     * <br>
     * 入力値：(引数) reader:Readerインスタンス<br>
     * (引数) lineFeedChar:null<br>
     * <br>
     * 期待値：(状態変化) なし:IllegalArgumentExceptionが発生する<br>
     * ・メッセージ："lineFeedChar is required."<br>
     * <br>
     * 引数lineFeedCharがnullの場合IllegalArgumentExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testLineFeed1LineReader03() throws Exception {
        // 前処理(引数)
        Reader reader = new BufferedReader(new StringReader(""));
        String lineFeedChar = null;

        try {
            // テスト実施
            new LineFeed1LineReader(reader, lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("lineFeedChar is required.", e.getMessage());
        }
    }

    /**
     * testLineFeed1LineReader04() <br>
     * <br>
     * (異常系) <br>
     * 観点：E,G <br>
     * <br>
     * 入力値：(引数) reader:Readerインスタンス<br>
     * (引数) lineFeedChar:行区切り文字が1文字じゃない場合<br>
     * <br>
     * 期待値：(状態変化) なし:IllegalArgumentExceptionが発生する<br>
     * ・メッセージ："lineFeedChar should be defined by 1 digit of character string."<br>
     * <br>
     * 引数lineFeedCharの行区切り文字が1文字じゃない場合 IllegalArgumentExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testLineFeed1LineReader04() throws Exception {
        // 前処理(引数)
        Reader reader = new BufferedReader(new StringReader(""));
        String lineFeedChar = "aa";

        try {
            // テスト実施
            new LineFeed1LineReader(reader, lineFeedChar);
            fail("IllegalArgumentExceptionが発生しませんでした。");
        } catch (IllegalArgumentException e) {
            // 判定
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("lineFeedChar should be defined"
                    + " by 1 digit of character string.", e.getMessage());
        }
    }

    /**
     * testReadLine01() <br>
     * <br>
     * (異常系) <br>
     * 観点：F.G <br>
     * <br>
     * 入力値：(状態) reader:not null<br>
     * readerのスタブ<br>
     * IOExceptionが発生する。<br>
     * (状態) lineFeedChar:"\r"<br>
     * <br>
     * 期待値：(状態変化) -:IOExceptionが発生。FileExceptioにラップされることを確認する。<br>
     * <br>
     * 例外。readerが例外をスローした場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine01() throws Exception {
        // Mock作成
        Reader reader = mock(Reader.class);
        when(reader.read()).thenThrow(new IOException());

        String tempLineFeedChar = "\r";
        LineFeed1LineReader lineFeed1LineReader = new LineFeed1LineReader(reader, tempLineFeedChar);

        // テスト実施
        try {
            lineFeed1LineReader.readLine();
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 判定
            assertTrue(e instanceof FileException);
            assertEquals("Reader control operation was failed.", e
                    .getMessage());
            assertTrue(e.getCause() instanceof IOException);
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
     * Readerインスタンス<br>
     * (状態) lineFeedChar:"\r"<br>
     * <br>
     * 期待値：(戻り値) String:""<br>
     * <br>
     * 空の文字列が返却されるのを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine02() throws Exception {
        // 前処理(試験対象)

        String str = "";
        StringReader stringReader = new StringReader(str);
        BufferedReader bufReader = new BufferedReader(stringReader);

        String tempLineFeedChar = "\r";
        LineFeed1LineReader lineFeed1LineReader = new LineFeed1LineReader(bufReader, tempLineFeedChar);

        String lineFeedChar = "\r";
        ReflectionTestUtils.setField(lineFeed1LineReader, "lineFeedChar",
                lineFeedChar);

        // テスト実施
        String resutl = lineFeed1LineReader.readLine();

        // 判定
        assertEquals("", resutl);
    }

    /**
     * testReadLine03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"aaa,aaa,aaa\r"<br>
     * (状態) reader:not null <br>
     * Readerインスタンス<br>
     * (状態) lineFeedChar:"\r"<br>
     * <br>
     * 期待値：(戻り値) String:"aaa,aaa,aaa"<br>
     * <br>
     * 文字列が1行返却されるのを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine03() throws Exception {
        // 前処理(試験対象)
        String str = "aaa,aaa,aaa\r";
        StringReader stringReader = new StringReader(str);
        BufferedReader bufReader = new BufferedReader(stringReader);

        String tempLineFeedChar = "\r";
        LineFeed1LineReader lineFeed1LineReader = new LineFeed1LineReader(bufReader, tempLineFeedChar);

        // テスト実施
        String resutl = lineFeed1LineReader.readLine();

        // 判定
        assertEquals("aaa,aaa,aaa", resutl);
    }

    /**
     * testReadLine04() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"\"aaa\",\"aaa\",\"aaa\"\r"<br>
     * (状態) reader:not null <br>
     * Readerインスタンス<br>
     * (状態) lineFeedChar:"\r"<br>
     * <br>
     * 期待値：(戻り値) String:"\"aaa\",\"aaa\",\"aaa\""<br>
     * <br>
     * 文字列が1行返却される。囲み文字がある場合。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine04() throws Exception {
        // 前処理(試験対象)
        String str = "\"aaa\",\"aaa\",\"aaa\"\r";
        StringReader stringReader = new StringReader(str);
        BufferedReader bufReader = new BufferedReader(stringReader);

        String tempLineFeedChar = "\r";
        LineFeed1LineReader lineFeed1LineReader = new LineFeed1LineReader(bufReader, tempLineFeedChar);

        // テスト実施
        String resutl = lineFeed1LineReader.readLine();

        // 判定
        assertEquals("\"aaa\",\"aaa\",\"aaa\"", resutl);
    }

    /**
     * testReadLine05() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"\"aa,a\",\"aa\"\"a\",\"aaa\"\r"<br>
     * (状態) reader:not null <br>
     * Readerインスタンス<br>
     * (状態) lineFeedChar:"\r"<br>
     * <br>
     * 期待値：(戻り値) String:"\"aa,a\",\"aa\"\"a\",\"aaa\""<br>
     * <br>
     * 文字列の中に囲み文字、区切り文字が含まれている場合。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine05() throws Exception {
        // 前処理(試験対象)
        String str = "\"aa,a\",\"aa\"\"a\",\"aaa\"\r";
        StringReader stringReader = new StringReader(str);
        BufferedReader bufReader = new BufferedReader(stringReader);

        String tempLineFeedChar = "\r";
        LineFeed1LineReader lineFeed1LineReader = new LineFeed1LineReader(bufReader, tempLineFeedChar);

        // テスト実施
        String result = lineFeed1LineReader.readLine();

        // 判定
        assertEquals("\"aa,a\",\"aa\"\"a\",\"aaa\"", result);
    }

    /**
     * testReadLine06() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"\"aaa\",\"aaa\",\"aa\ra\"\r"<br>
     * (状態) reader:not null <br>
     * Readerインスタンス<br>
     * (状態) lineFeedChar:"\r"<br>
     * <br>
     * 期待値：(戻り値) String:"\"aaa\",\"aaa\",\"aa"<br>
     * <br>
     * 文字列の中に行区切り文字が含まれている場合。<br>
     * 行区切り文字のところで読み込みは終了する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine06() throws Exception {
        // 前処理(試験対象)
        String str = "\"aaa\",\"aaa\",\"aa\ra\"\r";
        StringReader stringReader = new StringReader(str);
        BufferedReader bufReader = new BufferedReader(stringReader);

        String tempLineFeedChar = "\r";
        LineFeed1LineReader lineFeed1LineReader = new LineFeed1LineReader(bufReader, tempLineFeedChar);

        // テスト実施
        String result = lineFeed1LineReader.readLine();

        // 判定
        assertEquals("\"aaa\",\"aaa\",\"aa", result);
    }

    /**
     * testReadLine07() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"\"aaa\",\"aaa\",\"aaa\"aaa\r"<br>
     * (状態) reader:not null <br>
     * Readerインスタンス<br>
     * (状態) lineFeedChar:"\r"<br>
     * <br>
     * 期待値：(戻り値) String:"\"aaa\",\"aaa\",\"aaa\"aaa"<br>
     * <br>
     * 文字列が1行返却される。囲み文字(終了側)の後ろに文字列がつながる場合、 区切り文字もしくは行区切り文字が来るまでの文字列は全て前のカラムに含まれる。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine07() throws Exception {
        // 前処理(試験対象)
        String str = "\"aaa\",\"aaa\",\"aaa\"aaa\r";
        StringReader stringReader = new StringReader(str);
        BufferedReader bufReader = new BufferedReader(stringReader);

        String tempLineFeedChar = "\r";
        LineFeed1LineReader lineFeed1LineReader = new LineFeed1LineReader(bufReader, tempLineFeedChar);

        // テスト実施
        String resutl = lineFeed1LineReader.readLine();

        // 判定
        assertEquals("\"aaa\",\"aaa\",\"aaa\"aaa", resutl);
    }

    /**
     * testReadLine08() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"aaa,aaa,aaa"<br>
     * (状態) reader:not null <br>
     * Readerインスタンス<br>
     * (状態) lineFeedChar:"\r"<br>
     * <br>
     * 期待値：(戻り値) String:"aaa,aaa,aaa"<br>
     * <br>
     * 行区切り文字が含まれてない場合すべてのデータが出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine08() throws Exception {
        // 前処理(試験対象)
        String str = "aaa,aaa,aaa";
        StringReader stringReader = new StringReader(str);
        BufferedReader bufReader = new BufferedReader(stringReader);

        String tempLineFeedChar = "\r";
        LineFeed1LineReader lineFeed1LineReader = new LineFeed1LineReader(bufReader, tempLineFeedChar);

        // テスト実施
        String resutl = lineFeed1LineReader.readLine();

        // 判定
        assertEquals("aaa,aaa,aaa", resutl);
    }

    /**
     * testReadLine09() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"\"ａａ,ａ\",\"ａａ\"\"ａ\",\"ａａａ\"\r"<br>
     * (状態) reader:not null <br>
     * Readerインスタンス<br>
     * (状態) lineFeedChar:"\r"<br>
     * <br>
     * 期待値：(戻り値) String:"\"ａａ,ａ\",\"ａａ\"\"ａ\",\"ａａａ\""<br>
     * <br>
     * 文字列の中に囲み文字、全角文字が含まれている場合。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine09() throws Exception {
        // 前処理(試験対象)
        String str = "\"ａａ,ａ\",\"ａａ\"\"ａ\",\"ａａａ\"\r";
        StringReader stringReader = new StringReader(str);
        BufferedReader bufReader = new BufferedReader(stringReader);

        String tempLineFeedChar = "\r";
        LineFeed1LineReader lineFeed1LineReader = new LineFeed1LineReader(bufReader, tempLineFeedChar);

        // テスト実施
        String resutl = lineFeed1LineReader.readLine();

        // 判定
        assertEquals("\"ａａ,ａ\",\"ａａ\"\"ａ\",\"ａａａ\"", resutl);
    }

    /**
     * testReadLine10() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,F <br>
     * <br>
     * 入力値：(状態) readerが読み取る文字列:"aaa,aaa,aaa\raaa,aaa,aaa\raaa,aaa,aaa\r"<br>
     * (状態) reader:not null <br>
     * Readerインスタンス<br>
     * (状態) lineFeedChar:"\r"<br>
     * <br>
     * 期待値：(戻り値) String:"aaa,aaa,aaa"<br>
     * <br>
     * 複数行分の文字列情報がある場合、結果文字列が1行分のみ返却されるのを確認する。<br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testReadLine10() throws Exception {
        // 前処理(試験対象)
        String str = "aaa,aaa,aaa\raaa,aaa,aaa\raaa,aaa,aaa\r";
        StringReader stringReader = new StringReader(str);
        BufferedReader bufReader = new BufferedReader(stringReader);

        String tempLineFeedChar = "\r";
        LineFeed1LineReader lineFeed1LineReader = new LineFeed1LineReader(bufReader, tempLineFeedChar);

        // テスト実施
        String resutl = lineFeed1LineReader.readLine();

        // 判定
        assertEquals("aaa,aaa,aaa", resutl);
    }
}
