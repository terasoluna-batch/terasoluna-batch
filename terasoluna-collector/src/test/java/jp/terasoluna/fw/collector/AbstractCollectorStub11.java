package jp.terasoluna.fw.collector;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import jp.terasoluna.fw.collector.vo.DataValueObject;

public class AbstractCollectorStub11<P> extends AbstractCollector<P> {

    /** 冗長ログ出力フラグ. */
    protected static AtomicBoolean verboseLog = new AtomicBoolean(true);

    private static AtomicInteger foCount = new AtomicInteger(0);

    public AbstractCollectorStub11() {
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
                if ((foCount.get() % 2) == 0) {
                    foCount.incrementAndGet();
                    return false;
                }
                foCount.incrementAndGet();
                return true;
            }

        };

        this.queue = new BlockingQueue<DataValueObject>() {

            public DataValueObject element() {
                return null;
            }

            public boolean offer(DataValueObject o) {
                return false;
            }

            public DataValueObject peek() {
                return null;
            }

            public DataValueObject poll() {
                return new DataValueObject(new Exception());
            }

            public DataValueObject remove() {
                return null;
            }

            public boolean add(DataValueObject o) {
                return false;
            }

            public boolean addAll(Collection<? extends DataValueObject> c) {
                return false;
            }

            public void clear() {
            }

            public boolean contains(Object o) {
                return false;
            }

            public boolean containsAll(Collection<?> c) {
                return false;
            }

            public boolean isEmpty() {
                return true;
            }

            public Iterator<DataValueObject> iterator() {
                return null;
            }

            public boolean remove(Object o) {
                return false;
            }

            public boolean removeAll(Collection<?> c) {
                return false;
            }

            public boolean retainAll(Collection<?> c) {
                return false;
            }

            public int size() {
                return 0;
            }

            public Object[] toArray() {
                return null;
            }

            public <T> T[] toArray(T[] a) {
                return null;
            }

            public int drainTo(Collection<? super DataValueObject> c) {
                return 0;
            }

            public int drainTo(Collection<? super DataValueObject> c,
                    int maxElements) {
                return 0;
            }

            public boolean offer(DataValueObject o, long timeout, TimeUnit unit)
                                                                                throws InterruptedException {
                return false;
            }

            public DataValueObject poll(long timeout, TimeUnit unit)
                                                                    throws InterruptedException {
                return null;
            }

            public void put(DataValueObject o) throws InterruptedException {
            }

            public int remainingCapacity() {
                return 0;
            }

            public DataValueObject take() throws InterruptedException {
                return null;
            }
        };
    }

    @Override
    protected boolean isFinish() {
        return true;
    }

    public Integer call() throws Exception {
        return null;
    }

}
