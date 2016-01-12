package jp.terasoluna.fw.collector.db;

public class DaoCollectorPrePostProcessStub003 implements
                                               DaoCollectorPrePostProcess {

    @SuppressWarnings("rawtypes")
    public <P> void preprocess(DaoCollector<P> collector) {
        collector.resultHandler = new QueueingResultHandlerImpl();
    }

    @SuppressWarnings("rawtypes")
    public <P> void postprocessComplete(DaoCollector<P> collector) {
        collector.resultHandler = new QueueingResultHandlerImpl();
    }

    public <P> DaoCollectorPrePostProcessStatus postprocessException(
            DaoCollector<P> collector, Throwable throwable) {
        if ("postprocessExceptionテスト".equals(throwable.getMessage())) {
            return DaoCollectorPrePostProcessStatus.THROW;
        } else {
            return DaoCollectorPrePostProcessStatus.END;
        }
    }

}
