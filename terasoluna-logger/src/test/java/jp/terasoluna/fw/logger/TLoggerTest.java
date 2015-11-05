package jp.terasoluna.fw.logger;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import jp.terasoluna.fw.common.LogId;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TLoggerTest {
    private static BufferedReader logReader;

    private static final TLogger LOGGER = TLogger.getLogger(TLoggerTest.class);

    static {
        try {
            File f = new File("log/ut.log");
            f.createNewFile();
            logReader = new BufferedReader(new FileReader(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        while (logReader.readLine() != null)
            ;
    }

    @Test
    public void testLog01() throws Exception {
        LOGGER.log(LogId.TRA001);
        assertEquals(
                "[TRACE][TLoggerTest] " + LOGGER.getLogMessage(LogId.TRA001),
                logReader.readLine());
        LOGGER.log(LogId.DEB001);
        assertEquals(
                "[DEBUG][TLoggerTest] " + LOGGER.getLogMessage(LogId.DEB001),
                logReader.readLine());
        LOGGER.log(LogId.INF001);
        assertEquals(
                "[INFO][TLoggerTest] " + LOGGER.getLogMessage(LogId.INF001),
                logReader.readLine());
        LOGGER.log(LogId.WAR001);
        assertEquals(
                "[WARN][TLoggerTest] " + LOGGER.getLogMessage(LogId.WAR001),
                logReader.readLine());
        LOGGER.log(LogId.ERR001);
        assertEquals(
                "[ERROR][TLoggerTest] " + LOGGER.getLogMessage(LogId.ERR001),
                logReader.readLine());
        LOGGER.log(LogId.FAT001);
        assertEquals(
                "[ERROR][TLoggerTest] " + LOGGER.getLogMessage(LogId.FAT001),
                logReader.readLine()); // FATALログはERRORレベルで出力される。
        LOGGER.log("HOGE");
        assertEquals("[DEBUG][TLoggerTest] [HOGE] ", logReader.readLine());
    }

    @Test
    public void testLog02() throws Exception {
        Exception e = new Exception("hoge");
        LOGGER.log(LogId.TRA001, e);
        assertEquals(
                "[TRACE][TLoggerTest] " + LOGGER.getLogMessage(LogId.TRA001),
                logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
        while (logReader.readLine() != null)
            ;

        LOGGER.log(LogId.DEB001, e);
        assertEquals(
                "[DEBUG][TLoggerTest] " + LOGGER.getLogMessage(LogId.DEB001),
                logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
        while (logReader.readLine() != null)
            ;

        LOGGER.log(LogId.INF001, e);
        assertEquals(
                "[INFO][TLoggerTest] " + LOGGER.getLogMessage(LogId.INF001),
                logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
        while (logReader.readLine() != null)
            ;

        LOGGER.log(LogId.WAR001, e);
        assertEquals(
                "[WARN][TLoggerTest] " + LOGGER.getLogMessage(LogId.WAR001),
                logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
        while (logReader.readLine() != null)
            ;

        LOGGER.log(LogId.ERR001, e);
        assertEquals(
                "[ERROR][TLoggerTest] " + LOGGER.getLogMessage(LogId.ERR001),
                logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
        while (logReader.readLine() != null)
            ;

        LOGGER.log(LogId.FAT001, e);
        assertEquals(
                "[ERROR][TLoggerTest] " + LOGGER.getLogMessage(LogId.FAT001),
                logReader.readLine()); // FATALログはERRORレベルとして出力される。
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
        while (logReader.readLine() != null)
            ;

        LOGGER.log("HOGE", e);
        assertEquals("[DEBUG][TLoggerTest] [HOGE] ", logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
        while (logReader.readLine() != null)
            ;
    }

    @Test
    public void testError01() throws Exception {
        LOGGER.error(LogId.ERR001);
        assertEquals(
                "[ERROR][TLoggerTest] " + LOGGER.getLogMessage(LogId.ERR001),
                logReader.readLine());
    }

    @Test
    public void testError02() throws Exception {
        LOGGER.error(LogId.ERR011, "a", "b");
        assertEquals(
                "[ERROR][TLoggerTest] "
                        + LOGGER.getLogMessage(LogId.ERR011, "a", "b"),
                logReader.readLine());
    }

    @Test
    public void testError03() throws Exception {
        LOGGER.error(LogId.ERR001, new Exception("hoge"));
        assertEquals(
                "[ERROR][TLoggerTest] " + LOGGER.getLogMessage(LogId.ERR001),
                logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @Test
    public void testError04() throws Exception {
        LOGGER.error(LogId.ERR011, new Exception("hoge"), "a", "b");
        assertEquals(
                "[ERROR][TLoggerTest] "
                        + LOGGER.getLogMessage(LogId.ERR011, "a", "b"),
                logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @Test
    public void testError05() throws Exception {
        LOGGER.error(true, LogId.ERR011, new Exception("hoge"), "a", "b");
        assertEquals(
                "[ERROR][TLoggerTest] "
                        + LOGGER.getLogMessage(LogId.ERR011, "a", "b"),
                logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @Test
    public void testError06() throws Exception {
        LOGGER.error(false, "{0} is {1}", new Exception("hoge"), "a", "b");
        assertEquals("[ERROR][TLoggerTest] a is b", logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @Test
    public void testError07() throws Exception {
        TLogger logger = TLogger.getLogger("FATAL_TEST");
        logger.error(false, "{0} is {1}", new Exception("hoge"), "a", "b");
        assertEquals("[ERROR][FATAL_TEST] a is b", logReader.readLine()); // FATAL指定のloggerはDEBUGレベルとして出力される。
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testError08() throws Exception {
        LOGGER.error(new StringBuilder("hoge"));
        assertEquals("[ERROR][TLoggerTest] hoge", logReader.readLine());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testError09() throws Exception {
        LOGGER.error(new StringBuilder("hoge"), new Exception("hoge"));
        assertEquals("[ERROR][TLoggerTest] hoge", logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @Test
    public void testFatal01() throws Exception {
        TLogger logger = TLogger.getLogger("FATAL_TEST");
        logger.fatal(LogId.FAT010);
        assertEquals(
                "[ERROR][FATAL_TEST] " + logger.getLogMessage(LogId.FAT010),
                logReader.readLine());

    }

    @Test
    public void testFatal02() throws Exception {
        TLogger logger = TLogger.getLogger("FATAL_TEST");
        logger.fatal(false, "{0} is {1}", "a", "b");
        assertEquals("[ERROR][FATAL_TEST] a is b", logReader.readLine());
    }

    @Test
    public void testFatal03() throws Exception {
        TLogger logger = TLogger.getLogger("FATAL_TEST");
        logger.fatal(false, "{0} is {1}", new Exception("hoge"), "a", "b");
        assertEquals("[ERROR][FATAL_TEST] a is b", logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testFatal04() throws Exception {
        TLogger logger = TLogger.getLogger("FATAL_TEST");
        logger.fatal(new StringBuilder("hoge"));
        assertEquals("[ERROR][FATAL_TEST] hoge", logReader.readLine());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testFatal05() throws Exception {
        TLogger logger = TLogger.getLogger("FATAL_TEST");
        logger.fatal(new StringBuilder("hoge"), new Exception("hoge"));
        assertEquals("[ERROR][FATAL_TEST] hoge", logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }
    
    @Test
    public void testFatal06() throws Exception {
        LOGGER.fatal(LogId.FAT001, new Exception("hoge"));
        assertEquals("[ERROR][TLoggerTest] " + LOGGER.getLogMessage(LogId.FAT001),
                logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }
    
    @Test
    public void testWarn01() throws Exception {
        {
            TLogger logger = TLogger.getLogger("WARN_TEST");
            logger.warn(LogId.WAR010);
            assertEquals(
                    "[WARN][WARN_TEST] " + logger.getLogMessage(LogId.WAR010),
                    logReader.readLine());
        }
        {
            TLogger logger = TLogger.getLogger("FATAL_TEST");
            logger.warn(LogId.WAR010);
            assertEquals("[WARN][FATAL_TEST] [WAR010] ワーンメッセージ10", logReader.readLine()); // FATAL 指定のloggerはDEBUGレベルとして解釈されるため、FATAL_TESTカテゴリでも出力される。
        }
    }

    @Test
    public void testWarn02() throws Exception {
        TLogger logger = TLogger.getLogger("WARN_TEST");
        logger.warn(false, "{0} is {1}", "a", "b");
        assertEquals("[WARN][WARN_TEST] a is b", logReader.readLine());
    }

    @Test
    public void testWarn03() throws Exception {
        TLogger logger = TLogger.getLogger("WARN_TEST");
        logger.warn(false, "{0} is {1}", new Exception("hoge"), "a", "b");
        assertEquals("[WARN][WARN_TEST] a is b", logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testWarn04() throws Exception {
        TLogger logger = TLogger.getLogger("WARN_TEST");
        logger.warn(new StringBuilder("hoge"));
        assertEquals("[WARN][WARN_TEST] hoge", logReader.readLine());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testWarn05() throws Exception {
        TLogger logger = TLogger.getLogger("WARN_TEST");
        logger.warn(new StringBuilder("hoge"), new Exception("hoge"));
        assertEquals("[WARN][WARN_TEST] hoge", logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }
    
    @Test
    public void testWarn06() throws Exception {
        LOGGER.warn(LogId.WAR001, new Exception("hoge"));
        assertEquals("[WARN][TLoggerTest] " + LOGGER.getLogMessage(LogId.WAR001),
                logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }
    @Test
    public void testInfo01() throws Exception {
        {
            TLogger logger = TLogger.getLogger("INFO_TEST");
            logger.info(LogId.INF010);
            assertEquals(
                    "[INFO][INFO_TEST] " + logger.getLogMessage(LogId.INF010),
                    logReader.readLine());
        }
        {
                TLogger logger = TLogger.getLogger("WARN_TEST");
                logger.info(LogId.INF010);
                assertNull(logReader.readLine()); // 出力されない
        }
    }

    @Test
    public void testInfo02() throws Exception {
        TLogger logger = TLogger.getLogger("INFO_TEST");
        logger.info(false, "{0} is {1}", "a", "b");
        assertEquals("[INFO][INFO_TEST] a is b", logReader.readLine());
    }

    @Test
    public void testInfo03() throws Exception {
        TLogger logger = TLogger.getLogger("INFO_TEST");
        logger.info(false, "{0} is {1}", new Exception("hoge"), "a", "b");
        assertEquals("[INFO][INFO_TEST] a is b", logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testInfo04() throws Exception {
        TLogger logger = TLogger.getLogger("INFO_TEST");
        logger.info(new StringBuilder("hoge"));
        assertEquals("[INFO][INFO_TEST] hoge", logReader.readLine());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testInfo05() throws Exception {
        TLogger logger = TLogger.getLogger("INFO_TEST");
        logger.info(new StringBuilder("hoge"), new Exception("hoge"));
        assertEquals("[INFO][INFO_TEST] hoge", logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @Test
    public void testInfo06() throws Exception {
        LOGGER.info(LogId.INF001, new Exception("hoge"));
        assertEquals("[INFO][TLoggerTest] " + LOGGER.getLogMessage(LogId.INF001),
                logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }
    
    @Test
    public void testDebug01() throws Exception {
        {
            TLogger logger = TLogger.getLogger("DEBUG_TEST");
            logger.debug(LogId.DEB010);
            assertEquals(
                    "[DEBUG][DEBUG_TEST] " + logger.getLogMessage(LogId.DEB010),
                    logReader.readLine());
        }
        {
            TLogger logger = TLogger.getLogger("INFO_TEST");
            logger.debug(LogId.DEB010);
            assertNull(logReader.readLine()); // 出力されない
        }
    }

    @Test
    public void testDebug02() throws Exception {
        TLogger logger = TLogger.getLogger("DEBUG_TEST");
        logger.debug(false, "{0} is {1}", "a", "b");
        assertEquals("[DEBUG][DEBUG_TEST] a is b", logReader.readLine());
    }

    @Test
    public void testDebug03() throws Exception {
        TLogger logger = TLogger.getLogger("DEBUG_TEST");
        logger.debug(false, "{0} is {1}", new Exception("hoge"), "a", "b");
        assertEquals("[DEBUG][DEBUG_TEST] a is b", logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testDebug04() throws Exception {
        TLogger logger = TLogger.getLogger("DEBUG_TEST");
        logger.debug(new StringBuilder("hoge"));
        assertEquals("[DEBUG][DEBUG_TEST] hoge", logReader.readLine());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testDebug05() throws Exception {
        TLogger logger = TLogger.getLogger("DEBUG_TEST");
        logger.debug(new StringBuilder("hoge"), new Exception("hoge"));
        assertEquals("[DEBUG][DEBUG_TEST] hoge", logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @Test
    public void testDebug06() throws Exception {
        LOGGER.debug(LogId.DEB001, new Exception("hoge"));
        assertEquals("[DEBUG][TLoggerTest] " + LOGGER.getLogMessage(LogId.DEB001),
                logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }
    
    @Test
    public void testTrace01() throws Exception {
        {
            TLogger logger = TLogger.getLogger("TRACE_TEST");
            logger.trace(LogId.TRA010);
            assertEquals(
                    "[TRACE][TRACE_TEST] " + logger.getLogMessage(LogId.TRA010),
                    logReader.readLine());
        }
        {
            TLogger logger = TLogger.getLogger("DEBUG_TEST");
            logger.trace(LogId.TRA010);
            assertNull(logReader.readLine()); // 出力されない
        }
    }

    @Test
    public void testTrace02() throws Exception {
        TLogger logger = TLogger.getLogger("TRACE_TEST");
        logger.trace(false, "{0} is {1}", "a", "b");
        assertEquals("[TRACE][TRACE_TEST] a is b", logReader.readLine());
    }

    @Test
    public void testTrace03() throws Exception {
        TLogger logger = TLogger.getLogger("TRACE_TEST");
        logger.trace(false, "{0} is {1}", new Exception("hoge"), "a", "b");
        assertEquals("[TRACE][TRACE_TEST] a is b", logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testTrace04() throws Exception {
        TLogger logger = TLogger.getLogger("TRACE_TEST");
        logger.trace(new StringBuilder("hoge"));
        assertEquals("[TRACE][TRACE_TEST] hoge", logReader.readLine());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testTrace05() throws Exception {
        TLogger logger = TLogger.getLogger("TRACE_TEST");
        logger.trace(new StringBuilder("hoge"), new Exception("hoge"));
        assertEquals("[TRACE][TRACE_TEST] hoge", logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @Test
    public void testTrace06() throws Exception {
        LOGGER.trace(LogId.TRA001, new Exception("hoge"));
        assertEquals("[TRACE][TLoggerTest] " + LOGGER.getLogMessage(LogId.TRA001),
                logReader.readLine());
        assertEquals("java.lang.Exception: hoge", logReader.readLine());
    }

    @Test
    public void testCreateMessage01() {
        assertEquals("a b", LOGGER.createMessage(false, "{0} {1}", "a", "b"));
        TLogger.setLocale(Locale.ENGLISH);
        assertEquals("[ERR001] error1",
                LOGGER.createMessage(true, LogId.ERR001));
        TLogger.setLocale(Locale.getDefault());
        assertEquals("[ERR001] エラーメッセージ1",
                LOGGER.createMessage(true, LogId.ERR001));
    }

    @Test
    public void testGetLogMessage01() {
        assertEquals("[ERR001] エラーメッセージ1", LOGGER.getLogMessage(LogId.ERR001));
        TLogger.setLocale(Locale.ENGLISH);
        assertEquals("[ERR001] error1", LOGGER.getLogMessage(LogId.ERR001));
        TLogger.setLocale(Locale.getDefault());
    }
}
