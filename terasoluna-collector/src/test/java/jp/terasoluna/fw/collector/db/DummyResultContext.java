package jp.terasoluna.fw.collector.db;

import org.apache.ibatis.session.ResultContext;

public class DummyResultContext implements ResultContext {

    private Object object = null;
    private int count = 0;
    private boolean stopped = false;

    public void setResultObject(Object object) {
        this.object = object;
    }

    public Object getResultObject() {
        return object;
    }

    public void setResultCount(int count) {
        this.count = count;
    }

    public int getResultCount() {
        return count;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void stop() {
        stopped = true;
    }

}
