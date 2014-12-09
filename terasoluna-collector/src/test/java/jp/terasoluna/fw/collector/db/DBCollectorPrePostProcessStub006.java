package jp.terasoluna.fw.collector.db;

import java.util.concurrent.atomic.AtomicInteger;

public class DBCollectorPrePostProcessStub006 implements DBCollectorPrePostProcess {

    protected AtomicInteger preprocessExecCount = new AtomicInteger(0);
    
    protected boolean execPreprocFlg = false;
    protected boolean execPostprocCompFlg = false;
    protected boolean execPostprocExcpFlg = false;

    public <P> void preprocess(DBCollector<P> collector) {
    	execPreprocFlg = true;
    }

    public <P> void postprocessComplete(DBCollector<P> collector) {
    	execPostprocCompFlg = true;
    }

    public <P> DBCollectorPrePostProcessStatus postprocessException(
            DBCollector<P> collector, Throwable throwable) {
        execPostprocExcpFlg = true;
        return DBCollectorPrePostProcessStatus.END;	// END‚ð•Ô‚·
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
