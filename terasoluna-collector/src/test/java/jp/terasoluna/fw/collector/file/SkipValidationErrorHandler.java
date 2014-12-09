package jp.terasoluna.fw.collector.file;

import jp.terasoluna.fw.collector.validate.AbstractValidationErrorHandler;
import jp.terasoluna.fw.collector.validate.ValidateErrorStatus;
import jp.terasoluna.fw.collector.vo.DataValueObject;

import org.springframework.validation.Errors;

public class SkipValidationErrorHandler extends AbstractValidationErrorHandler {

    private ValidateErrorStatus status = ValidateErrorStatus.SKIP;

    public SkipValidationErrorHandler() {
        super();
    }

    public SkipValidationErrorHandler(ValidateErrorStatus status) {
        super();
        this.status = status;
    }

    @Override
    protected ValidateErrorStatus getValidateStatus(
            DataValueObject dataValueObject, Errors errors) {
        return status;
    }

}
