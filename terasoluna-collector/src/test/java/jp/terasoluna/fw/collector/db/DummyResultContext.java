package jp.terasoluna.fw.collector.db;

import org.apache.ibatis.session.ResultContext;

public class DummyResultContext implements ResultContext<HogeBean> {

    private HogeBean object = null;
    private int count = 0;
    private boolean stopped = false;

    public void setResultObject(HogeBean object) {
        this.object = object;
    }

    public HogeBean getResultObject() {
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
