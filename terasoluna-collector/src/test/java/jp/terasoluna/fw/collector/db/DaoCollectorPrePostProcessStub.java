package jp.terasoluna.fw.collector.db;

import java.util.concurrent.atomic.AtomicInteger;

public class DaoCollectorPrePostProcessStub implements DaoCollectorPrePostProcess {

    protected AtomicInteger preCount = new AtomicInteger(0);

    protected AtomicInteger postCount = new AtomicInteger(0);

    public int getPreCount() {
        return preCount.get();
    }

    public int getPostCount() {
        return postCount.get();
    }

    public <P> void preprocess(DaoCollector<P> collector) {
        preCount.incrementAndGet();
    }

    public <P> void postprocessComplete(DaoCollector<P> collector) {
        postCount.incrementAndGet();
    }

    public <P> DaoCollectorPrePostProcessStatus postprocessException(
            DaoCollector<P> collector, Throwable throwable) {
        postCount.incrementAndGet();
        return null;
    }

}
