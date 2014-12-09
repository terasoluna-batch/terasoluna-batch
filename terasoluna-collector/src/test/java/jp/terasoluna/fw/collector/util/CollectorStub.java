package jp.terasoluna.fw.collector.util;

import java.io.IOException;
import java.util.Iterator;

import jp.terasoluna.fw.collector.Collector;

public class CollectorStub<P> implements Collector<P> {

    private P following = null;

    private P previous = null;

    private P next = null;

    private P current = null;

    private boolean closeCalled = false;

    private boolean closeException = false;

    public boolean isCloseCalled() {
        return closeCalled;
    }

    public void setCloseCalled(boolean closeCalled) {
        this.closeCalled = closeCalled;
    }

    public void close() throws IOException {
        this.closeCalled = true;
        if (this.closeException) {
            throw new IOException("hoge");
        }
    }

    public boolean isCloseException() {
        return closeException;
    }

    public void setCloseException(boolean closeException) {
        this.closeException = closeException;
    }

    public P getNext() {
        return this.following;
    }

    public P getPrevious() {
        return this.previous;
    }

    public P getCurrent() {
        return this.current;
    }

    public boolean hasNext() {
        return false;
    }

    public P next() {
        return this.next;
    }

    public void remove() {

    }

    public Iterator<P> iterator() {
        return null;
    }

    public void setFollowing(P following) {
        this.following = following;
    }

    public void setPrevious(P previous) {
        this.previous = previous;
    }

    public void setCurrent(P current) {
        this.current = current;
    }

    public void setNext(P next) {
        this.next = next;
    }

}
