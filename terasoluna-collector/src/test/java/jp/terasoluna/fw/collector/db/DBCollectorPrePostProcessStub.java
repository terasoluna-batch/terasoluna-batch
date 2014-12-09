package jp.terasoluna.fw.collector.db;

import java.util.concurrent.atomic.AtomicInteger;

public class DBCollectorPrePostProcessStub implements DBCollectorPrePostProcess {

    protected AtomicInteger preCount = new AtomicInteger(0);

    protected AtomicInteger postCount = new AtomicInteger(0);

    public int getPreCount() {
        return preCount.get();
    }

    public int getPostCount() {
        return postCount.get();
    }

    public <P> void preprocess(DBCollector<P> collector) {
        preCount.incrementAndGet();
    }

    public <P> void postprocessComplete(DBCollector<P> collector) {
        postCount.incrementAndGet();
    }

    public <P> DBCollectorPrePostProcessStatus postprocessException(
            DBCollector<P> collector, Throwable throwable) {
        postCount.incrementAndGet();
        return null;
    }

}
