package jp.terasoluna.fw.collector;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import jp.terasoluna.fw.collector.vo.DataValueObject;

public class AbstractCollectorStubSetFinish2<P> extends AbstractCollector<P> {
    public AbstractCollectorStubSetFinish2() {
    	super();
    }

    public Integer call() throws Exception {
        return null;
    }

    /**
     * ArrayBlockingQueue型のキューを作成する
     * @return
     */
    protected BlockingQueue<DataValueObject> createQueue() {
        if (this.currentQueue == null) {
            // currentキュー生成
            this.currentQueue = createCurrentQueue();
        }
        if (this.previousQueue == null) {
            // previousキュー生成
            this.previousQueue = createPreviousQueue();
        }
        return new ArrayBlockingQueue<DataValueObject>(this.queueSize);
    }

}
