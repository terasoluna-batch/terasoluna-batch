package jp.terasoluna.fw.collector.db;

import java.util.concurrent.atomic.AtomicInteger;

public class DaoCollectorPrePostProcessStub006 implements DaoCollectorPrePostProcess {

    protected AtomicInteger preprocessExecCount = new AtomicInteger(0);
    
    protected boolean execPreprocFlg = false;
    protected boolean execPostprocCompFlg = false;
    protected boolean execPostprocExcpFlg = false;

    public <P> void preprocess(DaoCollector<P> collector) {
    	execPreprocFlg = true;
    }

    public <P> void postprocessComplete(DaoCollector<P> collector) {
    	execPostprocCompFlg = true;
    }

    public <P> DaoCollectorPrePostProcessStatus postprocessException(
            DaoCollector<P> collector, Throwable throwable) {
        execPostprocExcpFlg = true;
        return DaoCollectorPrePostProcessStatus.END;	// ENDを返す
    }

    public boolean getExecPreprocFlg() {
    	return execPreprocFlg;
    }
    
    public boolean getExecPostProcCompFlg() {
    	
    	return execPostprocCompFlg;
    }
    
    public boolean getExecPostProcExcpFlg() {
    	return execPostprocExcpFlg;
    }
}
