/**
 * 
 */
package jp.terasoluna.fw.batch.util;

import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * 
 */
public class ValidationUtilTest extends TestCase {

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * testValidationUtil001
     */
    public void testValidationUtil001() {
        ValidationUtil vu = new ValidationUtil();
        assertNotNull(vu);
    }

    /**
     * testValidate001
     */
    public void testValidate001() {
        Validator validator = new ValidatorStub();
        Object value = new HogeBean();

        Errors result = ValidationUtil.validate(validator, value);

        assertNotNull(result);
        assertEquals(0, result.getErrorCount());
    }

    /**
     * testGetFieldErrorList001.
     */
    public void testGetFieldErrorList001() {
        Validator validator = new ValidatorStub();
        Object value = new HogeBean();

        Errors errors = ValidationUtil.validate(validator, value);
        errors.rejectValue("hoge", "abc");

        List<FieldError> result = ValidationUtil.getFieldErrorList(errors);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * testGetObjectErrorList001
     */
    public void testGetObjectErrorList001() {
        Validator validator = new ValidatorStub();
        Object value = new HogeBean();

        Errors errors = ValidationUtil.validate(validator, value);
        errors.rejectValue("hoge", "abc");

        List<ObjectError> result = ValidationUtil.getObjectErrorList(errors);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * testGetDefaultMessageSourceResolvableList001
     */
    public void testGetDefaultMessageSourceResolvableList001() {
        Validator validator = new ValidatorStub();
        Object value = new HogeBean();

        Errors errors = ValidationUtil.validate(validator, value);
        errors.rejectValue("hoge", "abc");

        List<DefaultMessageSourceResolvable> result = ValidationUtil
                .getDefaultMessageSourceResolvableList(errors);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

}
