package jp.terasoluna.fw.collector.db;

import jp.terasoluna.fw.collector.vo.DataValueObject;

public class DaoCollectorStub001 extends DaoCollector<HogeBean> {

    protected boolean exceptionFlag = false;

    public DaoCollectorStub001() {
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
