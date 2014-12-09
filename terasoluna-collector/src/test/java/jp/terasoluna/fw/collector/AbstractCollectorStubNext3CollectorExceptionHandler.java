package jp.terasoluna.fw.collector;

import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandlerStatus;
import jp.terasoluna.fw.collector.vo.DataValueObject;

public class AbstractCollectorStubNext3CollectorExceptionHandler implements
                                                                CollectorExceptionHandler {

    public CollectorExceptionHandlerStatus handleException(
            DataValueObject dataValueObject) {
        if (true) {
            throw new RuntimeException("hoge");
        }
        return null;
    }

}
