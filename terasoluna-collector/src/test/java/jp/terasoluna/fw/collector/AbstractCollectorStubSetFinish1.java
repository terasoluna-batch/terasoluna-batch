package jp.terasoluna.fw.collector;

import java.util.concurrent.BlockingQueue;

import jp.terasoluna.fw.collector.concurrent.ArrayBlockingQueueEx;
import jp.terasoluna.fw.collector.vo.DataValueObject;

public class AbstractCollectorStubSetFinish1<P> extends AbstractCollector<P> {
    public AbstractCollectorStubSetFinish1() {
    	super();
    }

    public Integer call() throws Exception {
        return null;
    }

    /**
     * ArrayBlockingQueueEx型のキューを作成する
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
        return new ArrayBlockingQueueEx<DataValueObject>(this.queueSize);
    }

}
