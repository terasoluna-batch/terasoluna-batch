package jp.terasoluna.fw.collector;

import java.util.concurrent.TimeUnit;

//import java.util.concurrent.atomic.AtomicInteger;

public class AbstractCollectorStub3<P> extends AbstractCollector<P> {
//    private static AtomicInteger foCount = new AtomicInteger(0);

    public AbstractCollectorStub3() {
//        this.fo = new Future<P>() {
//
//            public boolean cancel(boolean mayInterruptIfRunning) {
//                return false;
//            }
//
//            public P get() throws InterruptedException, ExecutionException {
//                return null;
//            }
//
//            public P get(long timeout, TimeUnit unit)
//                                                     throws InterruptedException,
//                                                     ExecutionException,
//                                                     TimeoutException {
//                return null;
//            }
//
//            public boolean isCancelled() {
//                return false;
//            }
//
//            public boolean isDone() {
//                if ((foCount.get() % 2) == 0) {
//                    foCount.incrementAndGet();
//                    return false;
//                }
//                foCount.incrementAndGet();
//                return true;
//            }
//
//        };
    }

    public Integer call() throws Exception {
        TimeUnit.MILLISECONDS.sleep(3000);
        setFinish();
        return null;
    }

}
