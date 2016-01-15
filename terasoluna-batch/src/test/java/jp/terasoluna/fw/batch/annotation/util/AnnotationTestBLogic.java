package jp.terasoluna.fw.batch.annotation.util;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import org.springframework.stereotype.Component;

@Component("T001")
public class AnnotationTestBLogic implements BLogic {

    public int execute(BLogicParam param) {
        return 0;
    }

}
