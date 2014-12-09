package jp.terasoluna.fw.collector;

import java.util.Queue;

import jp.terasoluna.fw.collector.AbstractCollector;
import jp.terasoluna.fw.collector.vo.DataValueObject;

public class AbstractCollectorStub12<P> extends AbstractCollector<P> {
    
    @Override
    protected Queue<DataValueObject> createCurrentQueue() {
        return null;
    }

    public Integer call() throws Exception {
        return null;
    }

}
