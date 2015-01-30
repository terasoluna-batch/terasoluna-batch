package jp.terasoluna.fw.collector.db;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import jp.terasoluna.fw.collector.vo.DataValueObject;

public class DaoCollectorStub003 extends DaoCollector<HogeBean> {

    protected BlockingQueue<DataValueObject> inQueue = null;

    private boolean blocked = false;

    public DaoCollectorStub003() {
        this.inQueue = new ArrayBlockingQueue<DataValueObject>(1);
        // あらかじめダミーオブジェクトをキューに入れて、次回追加時にブロックされる状態をつくる。
        this.inQueue.add(new DataValueObject("dummy"));
    }

    @Override
    protected void addQueue(DataValueObject dataValueObject, boolean force)
            throws InterruptedException {
        if (inQueue.remainingCapacity() == 0) {
            this.blocked = true;
        }
        // キューの容量を超えているため、ここでブロックする。
        inQueue.put(dataValueObject);
    }

    public boolean isBlocked() {
        return this.blocked;
    }
}
