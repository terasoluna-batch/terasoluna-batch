package jp.terasoluna.fw.collector.validate;

import static org.junit.Assert.*;
import jp.terasoluna.fw.collector.validate.ExceptionValidationErrorHandler;
import jp.terasoluna.fw.collector.validate.ValidationErrorException;
import jp.terasoluna.fw.collector.vo.DataValueObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.validation.Errors;

public class ExceptionValidationErrorHandlerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetValidateStatus001() {
        ExceptionValidationErrorHandler handler = new ExceptionValidationErrorHandler();

        DataValueObject dataValueObject = null;
        Errors errors = null;

        try {
            handler.getValidateStatus(dataValueObject, errors);
        } catch (Throwable e) {
            assertNotNull(e);
            assertEquals(ValidationErrorException.class, e.getClass());
        }
    }

}
