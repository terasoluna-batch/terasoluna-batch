package jp.terasoluna.fw.collector.db;


public class DaoCollectorPrePostProcessStub003 implements DaoCollectorPrePostProcess {

    public <P> void preprocess(DaoCollector<P> collector) {
    	collector.resultHandler = new QueueingResultHandlerImpl();
    }

    public <P> void postprocessComplete(DaoCollector<P> collector) {
    	collector.resultHandler = new QueueingResultHandlerImpl();
    }

    public <P> DaoCollectorPrePostProcessStatus postprocessException(
            DaoCollector<P> collector, Throwable throwable) {
    	if("postprocessExceptionテスト".equals(throwable.getMessage())) {
    		return DaoCollectorPrePostProcessStatus.THROW;
    	} else {
    		return DaoCollectorPrePostProcessStatus.END;
    	}
    }

}
