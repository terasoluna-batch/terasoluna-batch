/**
 * 
 */
package jp.terasoluna.fw.batch.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * 
 */
public class ValidationUtilTest {

    /**
     * testValidationUtil001
     */
    @Test
    public void testValidationUtil001() {
        ValidationUtil vu = new ValidationUtil();
        assertNotNull(vu);
    }

    /**
     * testValidate001
     */
    @Test
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
    @Test
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
    @Test
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
    @Test
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
