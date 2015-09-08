package jp.terasoluna.fw.batch.executor.vo;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

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
        assertThat(p.getCurAppStatusList(), is(asList("a", "b")));
    }

    @Test
    public void testToString() {
        BatchJobListParam p = new BatchJobListParam();
        p.setJobAppCd("cd");
        p.setCurAppStatusList(Arrays.asList("a", "b"));
        assertEquals("BatchJobListParam[jobAppCd=cd,curAppStatusList=[a, b]]", p
                .toString());
    }

}
