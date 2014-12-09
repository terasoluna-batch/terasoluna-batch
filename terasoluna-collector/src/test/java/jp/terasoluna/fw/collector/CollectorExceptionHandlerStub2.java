package jp.terasoluna.fw.collector;

import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandlerStatus;
import jp.terasoluna.fw.collector.vo.DataValueObject;

public class CollectorExceptionHandlerStub2 implements
                                           CollectorExceptionHandler {

	private CollectorExceptionHandlerStatus collectorExceptionHandlerStatus = null;
	
	// コンストラクタ
	public CollectorExceptionHandlerStub2(CollectorExceptionHandlerStatus status) {
		this.collectorExceptionHandlerStatus = status;
	}
	
    public CollectorExceptionHandlerStatus handleException(
            DataValueObject dataValueObject) {
        return collectorExceptionHandlerStatus;
    }

}
