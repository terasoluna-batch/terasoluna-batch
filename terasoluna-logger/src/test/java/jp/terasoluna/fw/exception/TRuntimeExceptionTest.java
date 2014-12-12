package jp.terasoluna.fw.exception;

import static org.junit.Assert.*;

import java.util.Locale;

import jp.terasoluna.fw.common.LogId;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TRuntimeExceptionTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        ExceptionConfig.setLocale(null);
    }

    
    @Test
    public void testConstructor01() {
        TRuntimeException e = new TRuntimeException(LogId.ERR005);
        assertEquals(ExceptionConfig.MESSAGE_MANAGER.getMessage(true,
                LogId.ERR005), e.getMessage());
        assertNull(e.getCause());
        assertEquals(LogId.ERR005, e.getMessageId());
        assertNotNull(e.getArgs());
        assertEquals(0, e.getArgs().length);
    }

    @Test
    public void testConstructor02() {
        TRuntimeException e = new TRuntimeException(LogId.ERR011, "hoge", "foo");
        assertEquals(ExceptionConfig.MESSAGE_MANAGER.getMessage(true,
                LogId.ERR011, "hoge", "foo"), e.getMessage());
        assertNull(e.getCause());
        assertEquals(LogId.ERR011, e.getMessageId());
        assertNotNull(e.getArgs());
        assertEquals(2, e.getArgs().length);
        assertEquals("hoge", e.getArgs()[0]);
        assertEquals("foo", e.getArgs()[1]);
    }

    @Test
    public void testConstructor03() {
        ExceptionConfig.setLocale(Locale.ENGLISH);
        TRuntimeException e = new TRuntimeException(LogId.ERR005);
        assertEquals(ExceptionConfig.MESSAGE_MANAGER.getMessage(true,
                LogId.ERR005, Locale.ENGLISH), e.getMessage());
        assertNull(e.getCause());
        assertEquals(LogId.ERR005, e.getMessageId());
        assertNotNull(e.getArgs());
        assertEquals(0, e.getArgs().length);
    }
    
    @Test
    public void testConstructor04() {
        Exception cause = new Exception();
        TRuntimeException e = new TRuntimeException(LogId.ERR005, cause);
        assertEquals(ExceptionConfig.MESSAGE_MANAGER.getMessage(true,
                LogId.ERR005), e.getMessage());
        assertSame(cause, e.getCause());
        assertEquals(LogId.ERR005, e.getMessageId());
        assertNotNull(e.getArgs());
        assertEquals(0, e.getArgs().length);
    }

}
