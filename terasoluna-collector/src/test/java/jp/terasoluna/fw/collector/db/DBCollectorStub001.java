package jp.terasoluna.fw.collector.db;

import jp.terasoluna.fw.collector.vo.DataValueObject;

public class DBCollectorStub001 extends DBCollector<HogeBean> {

    protected boolean exceptionFlag = false;

    public DBCollectorStub001() {
        execute();
    }

    @Override
    protected void addQueue(DataValueObject dataValueObject)
                                                            throws InterruptedException {

        if (this.exceptionFlag) {
            throw new InterruptedException();
        }
        super.addQueue(dataValueObject);
    }
}
