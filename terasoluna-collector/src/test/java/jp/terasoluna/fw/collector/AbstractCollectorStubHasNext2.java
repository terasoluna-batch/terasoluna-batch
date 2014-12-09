package jp.terasoluna.fw.collector;

public class AbstractCollectorStubHasNext2<P> extends AbstractCollector<P> {

    public Integer call() throws Exception {
        return null;
    }

    @Override
    protected void afterExecute() {
        setFinish();
        super.afterExecute();
    }

    @Override
    protected boolean isFinish() {
        return true;
    }

}
