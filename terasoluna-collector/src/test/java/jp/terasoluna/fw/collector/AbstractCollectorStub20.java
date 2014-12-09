package jp.terasoluna.fw.collector;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import jp.terasoluna.fw.collector.db.ValidatorStub;
import jp.terasoluna.fw.collector.vo.DataValueObject;
import jp.terasoluna.fw.exception.SystemException;

public class AbstractCollectorStub20<P> extends AbstractCollector<P> {

    public AbstractCollectorStub20() {

        this.validator = new ValidatorStub();

    }

    @Override
    public void close() {
        throw new SystemException(new Exception(), "2");
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
