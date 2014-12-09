package jp.terasoluna.fw.collector.file;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ValidatorStub implements Validator {

    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(ValidatorStub.class);

    private int callSupports = 0;

    private int callValidate = 0;

    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        callSupports++;
        return true;
    }

    public void validate(Object target, Errors errors) {
        callValidate++;
        if (logger.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Call ValidatorStub#validate target:[");
            sb.append(target);
            sb.append("]");
            logger.trace(sb.toString());
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "familyname",
                "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname",
                "field.required");
    }

    public int getCallSupports() {
        return callSupports;
    }

    public int getCallValidate() {
        return callValidate;
    }

}
