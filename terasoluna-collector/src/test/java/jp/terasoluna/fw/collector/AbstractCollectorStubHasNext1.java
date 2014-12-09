package jp.terasoluna.fw.collector;

import jp.terasoluna.fw.collector.AbstractCollector;

public class AbstractCollectorStubHasNext1<P> extends AbstractCollector<P> {

    public Integer call() throws Exception {
        return null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}
