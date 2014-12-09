package jp.terasoluna.fw.collector;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AbstractCollectorStubHasNext3<P> extends AbstractCollector<P> {

    public AbstractCollectorStubHasNext3() {
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
                return true;
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
