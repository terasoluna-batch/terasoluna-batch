package jp.terasoluna.fw.collector.db;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ValidatorStub implements Validator {

    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return true;
    }

    public void validate(Object target, Errors errors) {

    }

}
