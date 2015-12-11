package jp.terasoluna.fw.collector.db;

import org.apache.ibatis.session.ResultContext;

public class QueueingResultHandlerStub001<T> implements QueueingResultHandler<T> {

    public QueueingResultHandlerStub001() {
    }

    public void delayCollect() {

    }

    public void setDaoCollector(DaoCollector<T> daoCollector) {

    }

    public void handleResult(ResultContext<? extends T> resultContext) {

    }
}
