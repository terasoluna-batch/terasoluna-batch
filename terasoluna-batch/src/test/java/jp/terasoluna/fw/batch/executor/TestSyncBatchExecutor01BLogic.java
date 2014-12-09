package jp.terasoluna.fw.batch.executor;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;

import org.springframework.stereotype.Component;

@Component
public class TestSyncBatchExecutor01BLogic implements BLogic{

    public int execute(BLogicParam param) {
        return 100;
    }
}
