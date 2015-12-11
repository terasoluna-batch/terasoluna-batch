package jp.terasoluna.fw.collector.db;

import org.apache.ibatis.session.ResultContext;

public class QueueingResultHandlerStub003<T> implements QueueingResultHandler<T> {

    public QueueingResultHandlerStub003() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public void delayCollect() {

    }

    public void setDaoCollector(DaoCollector<T> daoCollector) {

    }

    public void handleResult(ResultContext<? extends T> resultContext) {

    }
}
