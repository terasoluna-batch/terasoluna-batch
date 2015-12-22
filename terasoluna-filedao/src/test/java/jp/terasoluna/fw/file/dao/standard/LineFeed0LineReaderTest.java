/**
 * 
 */
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

import jp.terasoluna.fw.file.dao.FileException;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 */
public class LineFeed0LineReaderTest {

    /**
     * 正常<br>
     * @throws Exception
     */
    @Test
    public void testLineFeed0LineReader01() throws Exception {
        // 前処理(引数)
        String str = "123456789012345678901234567890";
        Reader reader = new BufferedReader(new StringReader(str));
        String encoding = "UTF-8";
        int totalBytes = 10;

        // テスト実施
        LineFeed0LineReader target = new LineFeed0LineReader(reader, encoding, totalBytes);

        // 判定
        assertNotNull(reader);
        assertSame(reader, ReflectionTestUtils.getField(target, "reader"));
        assertEquals(encoding, ReflectionTestUtils.getField(target, "fileEncoding"));
        assertEquals(totalBytes, ReflectionTestUtils.getField(target, "totalBytes"));
    }

    /**
     * 異常<br>
     * reader : null
     * @throws Exception
     */
    @Test
    public void testLineFeed0LineReader02() throws Exception {
        // 前処理(引数)
        Reader reader = null;
        String encoding = "UTF-8";
        int totalBytes = 10;

        // テスト実施
        try {
            new LineFeed0LineReader(reader, encoding, totalBytes);
            fail();
        } catch (Exception e) {
            // 判定
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("reader is required.", e.getMessage());
        }
    }

    /**
     * 異常<br>
     * encoding : null
     * @throws Exception
     */
    @Test
    public void testLineFeed0LineReader03() throws Exception {
        // 前処理(引数)
        String str = "123456789012345678901234567890";
        Reader reader = new BufferedReader(new StringReader(str));
        String encoding = null;
        int totalBytes = 10;

        // テスト実施
        try {
            new LineFeed0LineReader(reader, encoding, totalBytes);
            fail();
        } catch (Exception e) {
            // 判定
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("fileEncoding is required.", e.getMessage());
        }
    }

    /**
     * 異常<br>
     * totalBytes < 0
     * @throws Exception
     */
    @Test
    public void testLineFeed0LineReader04() throws Exception {
        // 前処理(引数)
        String str = "123456789012345678901234567890";
        Reader reader = new BufferedReader(new StringReader(str));
        String encoding = "UTF-8";
        int totalBytes = -1;

        // テスト実施
        try {
            new LineFeed0LineReader(reader, encoding, totalBytes);
            fail();
        } catch (Exception e) {
            // 判定
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("totalBytes is larger than 0.", e.getMessage());
        }
    }

    /**
     * 正常<br>
     * @throws Exception
     */
    @Test
    public void testReadLine01() throws Exception {
        // 前処理(引数)
        String str = "111122223333444455556";
        Reader reader = new BufferedReader(new StringReader(str));
        String encoding = "UTF-8";
        int totalBytes = 4;

        LineFeed0LineReader target = new LineFeed0LineReader(reader, encoding, totalBytes);

        // テスト実施
        // 判定
        assertEquals(target.readLine(), "1111");
        assertEquals(target.readLine(), "2222");
        assertEquals(target.readLine(), "3333");
        assertEquals(target.readLine(), "4444");
        assertEquals(target.readLine(), "5555");
        assertEquals(target.readLine(), "6");
        assertEquals(target.readLine(), "");
    }

    /**
     * 正常<br>
     * @throws Exception
     */
    @Test
    public void testReadLine02() throws Exception {
        // 前処理(引数)
        String str = "11112222333344445555";
        Reader reader = new BufferedReader(new StringReader(str));
        String encoding = "UTF-8";
        int totalBytes = 4;

        LineFeed0LineReader target = new LineFeed0LineReader(reader, encoding, totalBytes);

        // テスト実施
        // 判定
        assertEquals(target.readLine(), "1111");
        assertEquals(target.readLine(), "2222");
        assertEquals(target.readLine(), "3333");
        assertEquals(target.readLine(), "4444");
        assertEquals(target.readLine(), "5555");
        assertEquals(target.readLine(), "");
    }

    /**
     * 正常<br>
     * encoding : UTF-8
     * @throws Exception
     */
    @Test
    public void testReadLine03() throws Exception {
        // 前処理(引数)
        String str = "1あ2い３う４え五お";
        Reader reader = new BufferedReader(new StringReader(str));
        String encoding = "UTF-8";
        int totalBytes = 4;

        LineFeed0LineReader target = new LineFeed0LineReader(reader, encoding, totalBytes);

        // テスト実施
        // 判定
        assertEquals(target.readLine(), "1あ");
        assertEquals(target.readLine(), "2い");
        assertEquals(target.readLine(), "３う");
        assertEquals(target.readLine(), "４え");
        assertEquals(target.readLine(), "五お");
        assertEquals(target.readLine(), "");
    }

    /**
     * 正常<br>
     * encoding : UTF-8
     * @throws Exception
     */
    @Test
    public void testReadLine04() throws Exception {
        // 前処理(引数)
        String str = "\r\r\n\n\\\"\"\".,/@<>";
        Reader reader = new BufferedReader(new StringReader(str));
        String encoding = "UTF-8";
        int totalBytes = 4;

        LineFeed0LineReader target = new LineFeed0LineReader(reader, encoding, totalBytes);

        // テスト実施
        // 判定
        assertEquals(target.readLine(), "\r\r\n\n");
        assertEquals(target.readLine(), "\\\"\"\"");
        assertEquals(target.readLine(), ".,/@");
        assertEquals(target.readLine(), "<>");
        assertEquals(target.readLine(), "");
    }

    /**
     * 正常<br>
     * encoding : Shift-JIS
     * @throws Exception
     */
    @Test
    public void testReadLine05() throws Exception {
        // 前処理(引数)
        String str = "1あ12い2３う４え五お";
        Reader reader = new BufferedReader(new StringReader(str));
        String encoding = "Shift-JIS";
        int totalBytes = 4;

        LineFeed0LineReader target = new LineFeed0LineReader(reader, encoding, totalBytes);

        // テスト実施
        // 判定
        assertEquals(target.readLine(), "1あ1");
        assertEquals(target.readLine(), "2い2");
        assertEquals(target.readLine(), "３う");
        assertEquals(target.readLine(), "４え");
        assertEquals(target.readLine(), "五お");
        assertEquals(target.readLine(), "");
    }

    /**
     * 正常<br>
     * encoding : Shift-JIS
     * @throws Exception
     */
    @Test
    public void testReadLine06() throws Exception {
        // 前処理(引数)
        String str = "\r\r\n\n\\\"\"\".,/@<>";
        Reader reader = new BufferedReader(new StringReader(str));
        String encoding = "Shift-JIS";
        int totalBytes = 4;

        LineFeed0LineReader target = new LineFeed0LineReader(reader, encoding, totalBytes);

        // テスト実施
        // 判定
        assertEquals(target.readLine(), "\r\r\n\n");
        assertEquals(target.readLine(), "\\\"\"\"");
        assertEquals(target.readLine(), ".,/@");
        assertEquals(target.readLine(), "<>");
        assertEquals(target.readLine(), "");
    }

    /**
     * 正常<br>
     * encoding : EUC-JP
     * @throws Exception
     */
    @Test
    public void testReadLine07() throws Exception {
        // 前処理(引数)
        String str = "1あ12い2３う４え五お";
        Reader reader = new BufferedReader(new StringReader(str));
        String encoding = "EUC-JP";
        int totalBytes = 4;

        LineFeed0LineReader target = new LineFeed0LineReader(reader, encoding, totalBytes);

        // テスト実施
        // 判定
        assertEquals(target.readLine(), "1あ1");
        assertEquals(target.readLine(), "2い2");
        assertEquals(target.readLine(), "３う");
        assertEquals(target.readLine(), "４え");
        assertEquals(target.readLine(), "五お");
        assertEquals(target.readLine(), "");
    }

    /**
     * 正常<br>
     * encoding : EUC-JP
     * @throws Exception
     */
    @Test
    public void testReadLine08() throws Exception {
        // 前処理(引数)
        String str = "\r\r\n\n\\\"\"\".,/@<>";
        Reader reader = new BufferedReader(new StringReader(str));
        String encoding = "EUC-JP";
        int totalBytes = 4;

        LineFeed0LineReader target = new LineFeed0LineReader(reader, encoding, totalBytes);

        // テスト実施
        // 判定
        assertEquals(target.readLine(), "\r\r\n\n");
        assertEquals(target.readLine(), "\\\"\"\"");
        assertEquals(target.readLine(), ".,/@");
        assertEquals(target.readLine(), "<>");
        assertEquals(target.readLine(), "");
    }

    /**
     * 異常<br>
     * encoding : 不正値
     * @throws Exception
     */
    @Test
    public void testReadLine09() throws Exception {
        // 前処理(引数)
        String str = "123456789012345678901234567890";
        Reader reader = new BufferedReader(new StringReader(str));
        String encoding = "";
        int totalBytes = 10;

        LineFeed0LineReader target = new LineFeed0LineReader(reader, encoding, totalBytes);
        // テスト実施
        try {
            target.readLine();
            fail();
        } catch (Exception e) {
            // 判定
            assertTrue(e instanceof FileException);
            assertEquals("Reader control operation was failed.", e.getMessage());
            assertTrue(e.getCause() instanceof IOException);
        }
    }

    /**
     * 異常<br>
     * Reader#read() : IOException
     * @throws Exception
     */
    @Test
    public void testReadLine10() throws Exception {
        // 前処理(引数)
        String encoding = "UTF-8";
        int totalBytes = 10;

        // Mock作成
        Reader reader = mock(Reader.class);
        when(reader.read()).thenThrow(new IOException());

        LineFeed0LineReader target = new LineFeed0LineReader(reader, encoding, totalBytes);
        // テスト実施
        try {
            target.readLine();
            fail();
        } catch (Exception e) {
            // 判定
            assertTrue(e instanceof FileException);
            assertEquals("Reader control operation was failed.", e.getMessage());
            assertTrue(e.getCause() instanceof IOException);
        }
    }
}
