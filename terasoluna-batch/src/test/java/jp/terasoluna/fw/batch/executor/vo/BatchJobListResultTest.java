package jp.terasoluna.fw.batch.executor.vo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BatchJobListResultTest {

    @Test
    public void testSetAndGetJobSequenceId() {
        BatchJobListResult r = new BatchJobListResult();
        r.setJobSequenceId("a");
        assertEquals("a", r.getJobSequenceId());
    }

    @Test
    public void testToString() {
        BatchJobListResult r = new BatchJobListResult();
        r.setJobSequenceId("a");
        assertEquals("BatchJobListResult[jobSequenceId=a]", r.toString());
    }

}
