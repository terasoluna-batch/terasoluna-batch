package jp.terasoluna.fw.batch.executor.vo;

import java.util.Arrays;

import jp.terasoluna.fw.ex.unit.util.AssertUtils;
import junit.framework.TestCase;

public class BatchJobListParamTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSetAndGetJobAppCd() {
        BatchJobListParam p = new BatchJobListParam();
        p.setJobAppCd("a");
        assertEquals("a", p.getJobAppCd());
    }

    public void testSetAndGetCurAppStatusList() {
        BatchJobListParam p = new BatchJobListParam();
        p.setCurAppStatusList(Arrays.asList("a", "b"));
        AssertUtils.assertCollectionEquals(Arrays.asList("a", "b"),
                p.getCurAppStatusList());
    }

    public void testToString() {
        BatchJobListParam p = new BatchJobListParam();
        p.setJobAppCd("cd");
        p.setCurAppStatusList(Arrays.asList("a", "b"));
        assertEquals("BatchJobListParam[jobAppCd=cd,curAppStatusList=[a, b]]", p.toString());
    }

}
