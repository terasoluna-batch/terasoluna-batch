package jp.terasoluna.fw.collector.db;

import org.springframework.validation.Errors;

import jp.terasoluna.fw.collector.validate.ValidateErrorStatus;
import jp.terasoluna.fw.collector.validate.ValidationErrorHandler;
import jp.terasoluna.fw.collector.vo.DataValueObject;

public class ValidationErrorHandlerStub implements ValidationErrorHandler {

    public ValidateErrorStatus handleValidationError(
            DataValueObject dataValueObject, Errors errors) {
        return ValidateErrorStatus.CONTINUE;
    }

}
