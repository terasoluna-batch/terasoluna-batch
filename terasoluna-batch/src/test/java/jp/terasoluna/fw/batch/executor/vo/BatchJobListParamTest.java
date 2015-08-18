package jp.terasoluna.fw.batch.executor.vo;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import jp.terasoluna.fw.ex.unit.util.AssertUtils;

public class BatchJobListParamTest {

    @Test
    public void testSetAndGetJobAppCd() {
        BatchJobListParam p = new BatchJobListParam();
        p.setJobAppCd("a");
        assertEquals("a", p.getJobAppCd());
    }

    @Test
    public void testSetAndGetCurAppStatusList() {
        BatchJobListParam p = new BatchJobListParam();
        p.setCurAppStatusList(Arrays.asList("a", "b"));
        AssertUtils.assertCollectionEquals(Arrays.asList("a", "b"),
                p.getCurAppStatusList());
    }

    @Test
    public void testToString() {
        BatchJobListParam p = new BatchJobListParam();
        p.setJobAppCd("cd");
        p.setCurAppStatusList(Arrays.asList("a", "b"));
        assertEquals("BatchJobListParam[jobAppCd=cd,curAppStatusList=[a, b]]", p.toString());
    }

}
