package jp.terasoluna.fw.batch.annotation.util;

import jp.terasoluna.fw.batch.annotation.JobComponent;
import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;

@JobComponent("T001")
public class AnnotationTestBLogic implements BLogic {

    public int execute(BLogicParam param) {
        return 0;
    }

}
