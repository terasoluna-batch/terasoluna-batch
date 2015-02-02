package jp.terasoluna.fw.collector;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import jp.terasoluna.fw.collector.exception.CollectorExceptionHandlerStatus;
import jp.terasoluna.fw.collector.vo.DataValueObject;


public class AbstractCollectorStubHasNext5<P> extends AbstractCollector<P> {

	// コンストラクタ
	AbstractCollectorStubHasNext5(int size) {
		this.queue = new ArrayBlockingQueue<DataValueObject>(size);
	}

	AbstractCollectorStubHasNext5(int size, CollectorExceptionHandlerStatus status) {
		this.queue = new ArrayBlockingQueue<DataValueObject>(size);
		this.exceptionHandler = new CollectorExceptionHandlerStub2(status);
	}
	
    @Override
    protected BlockingQueue<DataValueObject> createQueue() {
        if (this.currentQueue == null) {
            // currentキュー生成
            this.currentQueue = createCurrentQueue();
        }
        if (this.previousQueue == null) {
            // previousキュー生成
            this.previousQueue = createPreviousQueue();
        }
        return new LinkedBlockingQueue<DataValueObject>();
    }
    
    public Integer call() throws Exception {
        return null;
    }

}
