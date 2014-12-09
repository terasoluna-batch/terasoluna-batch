package jp.terasoluna.fw.collector.file;

import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandlerStatus;
import jp.terasoluna.fw.collector.vo.DataValueObject;

public class CollectorExceptionHandlerStub implements CollectorExceptionHandler {

    public CollectorExceptionHandlerStatus handleException(
            DataValueObject dataValueObject) {
        return CollectorExceptionHandlerStatus.SKIP;
    }

}
