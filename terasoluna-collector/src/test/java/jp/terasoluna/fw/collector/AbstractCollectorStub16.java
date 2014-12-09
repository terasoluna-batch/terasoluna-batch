package jp.terasoluna.fw.collector;

import jp.terasoluna.fw.collector.db.ValidatorStub;
import jp.terasoluna.fw.collector.validate.ValidateErrorStatus;
import jp.terasoluna.fw.collector.validate.ValidationErrorException;
import jp.terasoluna.fw.collector.vo.DataValueObject;

public class AbstractCollectorStub16<P> extends AbstractCollector<P> {

    public AbstractCollectorStub16() {

        this.validator = new ValidatorStub();

    }

    @Override
    protected ValidateErrorStatus validate(DataValueObject dataValueObject) {
        throw new ValidationErrorException();
    }

    public Integer call() throws Exception {
        Thread.sleep(1000);
        return null;
    }

    @Override
    protected boolean isFinish() {
        return false;
    }

}
