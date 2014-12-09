package jp.terasoluna.fw.collector.validate;

import static org.junit.Assert.*;
import jp.terasoluna.fw.collector.validate.ValidationErrorException;
import jp.terasoluna.fw.collector.vo.DataValueObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

public class ValidationErrorExceptionTest {

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

    /**
     * 
     */
    @Test
    public void testValidationErrorException001() {
        ValidationErrorException ex = new ValidationErrorException();

        assertNotNull(ex);
    }

    /**
     * 
     */
    @Test
    public void testValidationErrorExceptionString001() {
        String message = "hogehoge";
        ValidationErrorException ex = new ValidationErrorException(message);

        assertNotNull(ex);
        assertEquals(message, ex.getMessage());
    }

    /**
     * 
     */
    @Test
    public void testValidationErrorExceptionStringThrowable001() {
        String message = "hogehoge";
        Exception exception = new Exception();
        ValidationErrorException ex = new ValidationErrorException(message,
                exception);

        assertNotNull(ex);
        assertEquals(message, ex.getMessage());
        assertNotNull(ex.getStackTrace());
        assertTrue(ex.getStackTrace().length > 10);
    }

    /**
     * 
     */
    @Test
    public void testValidationErrorExceptionThrowable001() {
        Exception exception = new Exception();
        ValidationErrorException ex = new ValidationErrorException(exception);

        assertNotNull(ex);
        assertNotNull(ex.getStackTrace());
        assertTrue(ex.getStackTrace().length > 10);
    }

    /**
     * 
     */
    @Test
    public void testValidationErrorExceptionDataValueObjectErrors001() {
        DataValueObject dataValueObject = null;
        Errors errors = null;
        ValidationErrorException ex = new ValidationErrorException(
                dataValueObject, errors);

        assertNotNull(ex);
        assertNotNull(ex.getStackTrace());
        assertTrue(ex.getStackTrace().length > 10);
    }

    /**
     * 
     */
    @Test
    public void testValidationErrorExceptionDataValueObjectErrors002() {
        DataValueObject dataValueObject = new DataValueObject("hoge");
        Errors errors = new BindException("value", "String");

        ValidationErrorException ex = new ValidationErrorException(
                dataValueObject, errors);

        assertNotNull(ex);
        assertNotNull(ex.getStackTrace());
        assertTrue(ex.getStackTrace().length > 10);
    }

    /**
     * 
     */
    @Test
    public void testGetDataValueObject001() {
        DataValueObject dataValueObject = new DataValueObject("hoge");
        Errors errors = new BindException("value", "String");

        ValidationErrorException ex = new ValidationErrorException(
                dataValueObject, errors);

        assertNotNull(ex.getDataValueObject());
        assertEquals(DataValueObject.class, ex.getDataValueObject().getClass());
    }

    /**
     * 
     */
    @Test
    public void testGetErrors001() {
        DataValueObject dataValueObject = new DataValueObject("hoge");
        Errors errors = new BindException("value", "String");

        ValidationErrorException ex = new ValidationErrorException(
                dataValueObject, errors);

        assertNotNull(ex.getErrors());
        assertEquals(BindException.class, ex.getErrors().getClass());
    }

}
