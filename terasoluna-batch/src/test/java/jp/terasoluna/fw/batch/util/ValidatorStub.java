package jp.terasoluna.fw.batch.util;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ValidatorStub implements Validator {

    public boolean supports(Class clazz) {
        return false;
    }

    public void validate(Object target, Errors errors) {
    }

}
