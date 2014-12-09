package jp.terasoluna.fw.collector;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class AbstractCollectorStubHasNext4<P> extends AbstractCollector<P> {
    private static AtomicInteger foCount = new AtomicInteger(0);

    public AbstractCollectorStubHasNext4() {
        this.fo = new Future<P>() {

            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            public P get() throws InterruptedException, ExecutionException {
                return null;
            }

            public P get(long timeout, TimeUnit unit)
                                                     throws InterruptedException,
                                                     ExecutionException,
                                                     TimeoutException {
                return null;
            }

            public boolean isCancelled() {
                return false;
            }

            public boolean isDone() {
                if ((foCount.get() % 2) == 1) {
                    foCount.incrementAndGet();
                    return true;
                }
                foCount.incrementAndGet();
                return false;
            }
        };
    }

    public Integer call() throws Exception {
        return null;
    }

    @Override
    protected void afterExecute() {
        setFinish();
        super.afterExecute();
    }

    @Override
    protected boolean isFinish() {
        return true;
    }
}
