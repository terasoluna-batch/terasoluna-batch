package jp.terasoluna.fw.collector.db;

import jp.terasoluna.fw.collector.vo.DataValueObject;
import jp.terasoluna.fw.exception.SystemException;

public class DBCollectorStub002 extends DBCollector<HogeBean> {

    protected boolean exceptionFlag = false;

    public DBCollectorStub002() {
        execute();
    }

    @Override
    protected void addQueue(DataValueObject dataValueObject)
                                                            throws InterruptedException {

        if (this.exceptionFlag) {
            throw new SystemException(null);
        }
        super.addQueue(dataValueObject);
    }
}
