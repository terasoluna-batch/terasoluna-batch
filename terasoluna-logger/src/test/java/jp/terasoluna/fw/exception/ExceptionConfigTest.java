package jp.terasoluna.fw.exception;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExceptionConfigTest extends ExceptionConfig {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConstructor() {
        assertNotNull(new ExceptionConfig());
    }

    @Test
    public void testGetAndSetLocale() {
        Locale org = ExceptionConfig.getLocale();
        try {
            ExceptionConfig.setLocale(Locale.ENGLISH);
            assertEquals(Locale.ENGLISH, ExceptionConfig.getLocale());
        } finally {
            ExceptionConfig.setLocale(org);
        }
    }
}
