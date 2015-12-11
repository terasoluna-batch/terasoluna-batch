package jp.terasoluna.fw.collector.db;

import org.apache.ibatis.session.ResultContext;

public class QueueingResultHandlerStub002<T> implements QueueingResultHandler<T> {

    public QueueingResultHandlerStub002() throws InstantiationException {
        throw new InstantiationException();
    }

    public void delayCollect() {

    }

    public void setDaoCollector(DaoCollector<T> daoCollector) {

    }

    public void handleResult(ResultContext<? extends T> resultContext) {

    }
}
