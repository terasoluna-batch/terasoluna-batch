package jp.terasoluna.fw.collector.db;

import org.apache.ibatis.session.ResultContext;

public class QueueingResultHandlerStub002 implements QueueingResultHandler {

    public QueueingResultHandlerStub002() throws InstantiationException {
        throw new InstantiationException();
    }

    public void delayCollect() {

    }

    public void setDaoCollector(DaoCollector<?> daoCollector) {

    }

    public void handleResult(ResultContext resultContext) {

    }
}
