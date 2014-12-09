package jp.terasoluna.fw.collector.db;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import jp.terasoluna.fw.collector.vo.DataValueObject;

public class DBCollectorStub004 extends DBCollector<HogeBean> {

    protected BlockingQueue<DataValueObject> inQueue = null;

    /**
     * コンストラクタ。
     *
     * @param queueCount ブロックされずにキューに詰められる数
     */
    public DBCollectorStub004(int queueCount) {
        this.inQueue = new ArrayBlockingQueue<DataValueObject>(queueCount);
    }

    @Override
    protected void addQueue(DataValueObject dataValueObject)
            throws InterruptedException {
        // キューの容量を超えている場合、ここでブロックする。
        inQueue.put(dataValueObject);
    }

    @Override
    public void setFinish(boolean finish) {
        super.setFinish(finish);
    }

    @Override
    public BlockingQueue<DataValueObject> getQueue() {
        return inQueue;
    }
}
