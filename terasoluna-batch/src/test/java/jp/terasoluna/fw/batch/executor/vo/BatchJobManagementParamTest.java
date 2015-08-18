package jp.terasoluna.fw.batch.executor.vo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BatchJobManagementParamTest {

    @Test
    public void testSetAndGetJobSequenceId() {
        BatchJobManagementParam p = new BatchJobManagementParam();
        p.setJobSequenceId("a");
        assertEquals("a", p.getJobSequenceId());
    }

    @Test
    public void testSetAndGetForUpdate() {
        BatchJobManagementParam p = new BatchJobManagementParam();
        p.setForUpdate(true);
        assertTrue(p.getForUpdate());
    }

    @Test
    public void testToString() {
        BatchJobManagementParam p = new BatchJobManagementParam();
        p.setJobSequenceId("a");
        p.setForUpdate(true);
        assertEquals("BatchJobManagementParam[jobSequenceId=a,forUpdate=true]", p.toString());
    }

}
