package jp.terasoluna.fw.collector.db;

import java.util.concurrent.atomic.AtomicInteger;

public class DBCollectorPrePostProcessStub002 implements DBCollectorPrePostProcess {

    protected AtomicInteger preprocessExecCount = new AtomicInteger(0);
    
    protected boolean retryFlag = false;

    public <P> void preprocess(DBCollector<P> collector) {
    	// preprocessメソッドが呼ばれるたびに実行回数をインクリメント
    	preprocessExecCount.getAndIncrement();
    	if(preprocessExecCount.get() >= 2) {
    		// 2回以上この処理が実行されている場合はリトライフラグをtrueにする
    		retryFlag = true;
    	}
    }

    public <P> void postprocessComplete(DBCollector<P> collector) {
    	/* NOP */
    }

    public <P> DBCollectorPrePostProcessStatus postprocessException(
            DBCollector<P> collector, Throwable throwable) {
        if (preprocessExecCount.get() == 1) {
        	// 1回目はRETRYを返してリトライさせる
        	return DBCollectorPrePostProcessStatus.RETRY;
        } else {
        	// 2回目以降はENDを返して終了させる
        	return DBCollectorPrePostProcessStatus.END;
        }
    }

    public boolean getRetryFlag() {
    	return retryFlag;
    }
}
