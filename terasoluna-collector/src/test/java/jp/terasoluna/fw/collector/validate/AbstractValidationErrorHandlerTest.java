package jp.terasoluna.fw.collector.validate;

import static org.junit.Assert.*;
import jp.terasoluna.fw.collector.validate.AbstractValidationErrorHandler;
import jp.terasoluna.fw.collector.validate.ValidateErrorStatus;
import jp.terasoluna.fw.collector.validate.ValidationErrorLoglevel;
import jp.terasoluna.fw.collector.vo.DataValueObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class AbstractValidationErrorHandlerTest {

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
    public void testSkipValidationErrorHandler001() {
        AbstractValidationErrorHandler handler = new AbstractValidationErrorHandler() {
            @Override
            protected ValidateErrorStatus getValidateStatus(
                    DataValueObject dataValueObject, Errors errors) {
                return null;
            }
        };
        assertNotNull(handler);
    }

    /**
     * 
     */
    @Test
    public void testSkipValidationErrorHandlerString001() {
        AbstractValidationErrorHandler handler = new AbstractValidationErrorHandler(
                ValidationErrorLoglevel.DEBUG) {

            @Override
            protected ValidateErrorStatus getValidateStatus(
                    DataValueObject dataValueObject, Errors errors) {
                return null;
            }
        };

        assertNotNull(handler);
        assertEquals(ValidationErrorLoglevel.DEBUG,
                handler.logLevel);
    }

    /**
     * 
     */
    @Test
    public void testHandleValidationError001() {
        AbstractValidationErrorHandler handler = new AbstractValidationErrorHandler(
                ValidationErrorLoglevel.DEBUG) {

            @Override
            protected ValidateErrorStatus getValidateStatus(
                    DataValueObject dataValueObject, Errors errors) {
                return null;
            }
        };

        DataValueObject dataValueObject = null;
        Errors errors = null;

        handler.handleValidationError(dataValueObject, errors);

        assertNotNull(handler);
        assertEquals(ValidationErrorLoglevel.DEBUG,
                handler.logLevel);
    }

    /**
     * 
     */
    @Test
    public void testHandleValidationError002() {
        AbstractValidationErrorHandler handler = new AbstractValidationErrorHandler(
                ValidationErrorLoglevel.DEBUG) {
            @Override
            protected ValidateErrorStatus getValidateStatus(
                    DataValueObject dataValueObject, Errors errors) {
                return ValidateErrorStatus.SKIP;
            }
        };

        DataValueObject dataValueObject = new DataValueObject((Object) null);
        HogeBean value = new HogeBean();
        String objectName = "object";
        Errors errors = new BindException(value, objectName);
        ValidationUtils.rejectIfEmpty(errors, "hoge", "error.hoge");

        // テスト
        ValidateErrorStatus status = handler.handleValidationError(
                dataValueObject, errors);

        assertNotNull(handler);
        assertEquals(ValidationErrorLoglevel.DEBUG,
                handler.logLevel);
        assertEquals(ValidateErrorStatus.SKIP, status);
    }

    /**
     * 
     */
    @Test
    public void testHandleValidationError003() {
        AbstractValidationErrorHandler handler = new AbstractValidationErrorHandler(
                ValidationErrorLoglevel.INFO) {
            @Override
            protected ValidateErrorStatus getValidateStatus(
                    DataValueObject dataValueObject, Errors errors) {
                return ValidateErrorStatus.SKIP;
            }
        };

        DataValueObject dataValueObject = new DataValueObject((Object) null);
        HogeBean value = new HogeBean();
        String objectName = "object";
        Errors errors = new BindException(value, objectName);
        ValidationUtils.rejectIfEmpty(errors, "hoge", "error.hoge");

        // テスト
        ValidateErrorStatus status = handler.handleValidationError(
                dataValueObject, errors);

        assertNotNull(handler);
        assertEquals(ValidationErrorLoglevel.INFO,
                handler.logLevel);
        assertEquals(ValidateErrorStatus.SKIP, status);
    }

    /**
     * 
     */
    @Test
    public void testHandleValidationError004() {
        AbstractValidationErrorHandler handler = new AbstractValidationErrorHandler(
                ValidationErrorLoglevel.TRACE) {
            @Override
            protected ValidateErrorStatus getValidateStatus(
                    DataValueObject dataValueObject, Errors errors) {
                return ValidateErrorStatus.SKIP;
            }
        };

        DataValueObject dataValueObject = new DataValueObject((Object) null);
        HogeBean value = new HogeBean();
        String objectName = "object";
        Errors errors = new BindException(value, objectName);
        ValidationUtils.rejectIfEmpty(errors, "hoge", "error.hoge");

        // テスト
        ValidateErrorStatus status = handler.handleValidationError(
                dataValueObject, errors);

        assertNotNull(handler);
        assertEquals(ValidationErrorLoglevel.TRACE,
                handler.logLevel);
        assertEquals(ValidateErrorStatus.SKIP, status);
    }

    /**
     * 
     */
    @Test
    public void testHandleValidationError005() {
        AbstractValidationErrorHandler handler = new AbstractValidationErrorHandler(
                ValidationErrorLoglevel.WARN) {
            @Override
            protected ValidateErrorStatus getValidateStatus(
                    DataValueObject dataValueObject, Errors errors) {
                return ValidateErrorStatus.SKIP;
            }
        };

        DataValueObject dataValueObject = new DataValueObject((Object) null);
        HogeBean value = new HogeBean();
        String objectName = "object";
        Errors errors = new BindException(value, objectName);
        ValidationUtils.rejectIfEmpty(errors, "hoge", "error.hoge");

        // テスト
        ValidateErrorStatus status = handler.handleValidationError(
                dataValueObject, errors);

        assertNotNull(handler);
        assertEquals(ValidationErrorLoglevel.WARN,
                handler.logLevel);
        assertEquals(ValidateErrorStatus.SKIP, status);
    }

    /**
     * 
     */
    @Test
    public void testHandleValidationError006() {
        AbstractValidationErrorHandler handler = new AbstractValidationErrorHandler(
                ValidationErrorLoglevel.ERROR) {
            @Override
            protected ValidateErrorStatus getValidateStatus(
                    DataValueObject dataValueObject, Errors errors) {
                return ValidateErrorStatus.SKIP;
            }
        };

        DataValueObject dataValueObject = new DataValueObject((Object) null);
        HogeBean value = new HogeBean();
        String objectName = "object";
        Errors errors = new BindException(value, objectName);
        ValidationUtils.rejectIfEmpty(errors, "hoge", "error.hoge");

        // テスト
        ValidateErrorStatus status = handler.handleValidationError(
                dataValueObject, errors);

        assertNotNull(handler);
        assertEquals(ValidationErrorLoglevel.ERROR,
                handler.logLevel);
        assertEquals(ValidateErrorStatus.SKIP, status);
    }

    /**
     * 
     */
    @Test
    public void testHandleValidationError007() {
        AbstractValidationErrorHandler handler = new AbstractValidationErrorHandler(
                ValidationErrorLoglevel.FATAL) {
            @Override
            protected ValidateErrorStatus getValidateStatus(
                    DataValueObject dataValueObject, Errors errors) {
                return ValidateErrorStatus.SKIP;
            }
        };

        DataValueObject dataValueObject = new DataValueObject((Object) null);
        HogeBean value = new HogeBean();
        String objectName = "object";
        Errors errors = new BindException(value, objectName);
        ValidationUtils.rejectIfEmpty(errors, "hoge", "error.hoge");

        // テスト
        ValidateErrorStatus status = handler.handleValidationError(
                dataValueObject, errors);

        assertNotNull(handler);
        assertEquals(ValidationErrorLoglevel.FATAL,
                handler.logLevel);
        assertEquals(ValidateErrorStatus.SKIP, status);
    }

    /**
     * 
     */
    @Test
    public void testHandleValidationError008() {
        AbstractValidationErrorHandler handler = new AbstractValidationErrorHandler(
                null) {
            @Override
            protected ValidateErrorStatus getValidateStatus(
                    DataValueObject dataValueObject, Errors errors) {
                return ValidateErrorStatus.SKIP;
            }
        };

        DataValueObject dataValueObject = new DataValueObject((Object) null);
        HogeBean value = new HogeBean();
        String objectName = "object";
        Errors errors = new BindException(value, objectName);
        ValidationUtils.rejectIfEmpty(errors, "hoge", "error.hoge");

        // テスト
        ValidateErrorStatus status = handler.handleValidationError(
                dataValueObject, errors);

        assertNotNull(handler);
        assertEquals(null, handler.logLevel);
        assertEquals(ValidateErrorStatus.SKIP, status);
    }

    /**
     * 
     */
    @Test
    public void testGetErrorFieldCount001() {
        AbstractValidationErrorHandler handler = new AbstractValidationErrorHandler() {
            @Override
            protected ValidateErrorStatus getValidateStatus(
                    DataValueObject dataValueObject, Errors errors) {
                return ValidateErrorStatus.SKIP;
            }
        };

        DataValueObject dataValueObject = new DataValueObject((Object) null);
        HogeBean value = new HogeBean();
        String objectName = "object";
        Errors errors = new BindException(value, objectName);
        ValidationUtils.rejectIfEmpty(errors, "hoge", "error.hoge");

        ValidateErrorStatus status = handler.handleValidationError(
                dataValueObject, errors);

        // テスト
        int count = handler.getErrorFieldCount();

        assertNotNull(handler);
        assertEquals(ValidationErrorLoglevel.INFO,
                handler.logLevel);
        assertEquals(ValidateErrorStatus.SKIP, status);
        assertEquals(1, count);
    }

    /**
     * 
     */
    @Test
    public void testGetErrors001() {
        AbstractValidationErrorHandler handler = new AbstractValidationErrorHandler() {
            @Override
            protected ValidateErrorStatus getValidateStatus(
                    DataValueObject dataValueObject, Errors errors) {
                return ValidateErrorStatus.SKIP;
            }
        };

        DataValueObject dataValueObject = new DataValueObject((Object) null);
        HogeBean value = new HogeBean();
        String objectName = "object";
        Errors errors = new BindException(value, objectName);
        ValidationUtils.rejectIfEmpty(errors, "hoge", "error.hoge");

        ValidateErrorStatus status = handler.handleValidationError(
                dataValueObject, errors);

        // テスト
        Errors[] result = handler.getErrors();

        assertNotNull(handler);
        assertEquals(ValidationErrorLoglevel.INFO,
                handler.logLevel);
        assertEquals(ValidateErrorStatus.SKIP, status);
        assertNotNull(result);
        assertEquals(1, result.length);
    }

    /**
     * 
     */
    @Test
    public void testSetLogLevel001() {
        AbstractValidationErrorHandler handler = new AbstractValidationErrorHandler() {
            @Override
            protected ValidateErrorStatus getValidateStatus(
                    DataValueObject dataValueObject, Errors errors) {
                return ValidateErrorStatus.SKIP;
            }
        };

        DataValueObject dataValueObject = new DataValueObject((Object) null);
        HogeBean value = new HogeBean();
        String objectName = "object";
        Errors errors = new BindException(value, objectName);
        ValidationUtils.rejectIfEmpty(errors, "hoge", "error.hoge");

        // テスト
        handler.setLogLevel(ValidationErrorLoglevel.ERROR);

        ValidateErrorStatus status = handler.handleValidationError(
                dataValueObject, errors);

        assertNotNull(handler);
        assertEquals(ValidationErrorLoglevel.ERROR,
                handler.logLevel);
        assertEquals(ValidateErrorStatus.SKIP, status);
    }

}
