package jp.terasoluna.fw.collector;

import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandlerStatus;
import jp.terasoluna.fw.collector.vo.DataValueObject;

public class CollectorExceptionHandlerStub1 implements
                                           CollectorExceptionHandler {

    public CollectorExceptionHandlerStatus handleException(
            DataValueObject dataValueObject) {
        return CollectorExceptionHandlerStatus.END;
    }

}
