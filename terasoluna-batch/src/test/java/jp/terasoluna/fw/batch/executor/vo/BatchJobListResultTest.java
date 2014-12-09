package jp.terasoluna.fw.batch.executor.vo;

import junit.framework.TestCase;

public class BatchJobListResultTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSetAndGetJobSequenceId() {
        BatchJobListResult r = new BatchJobListResult();
        r.setJobSequenceId("a");
        assertEquals("a", r.getJobSequenceId());
    }

    public void testToString() {
        BatchJobListResult r = new BatchJobListResult();
        r.setJobSequenceId("a");
        assertEquals("BatchJobListResult[jobSequenceId=a]", r.toString());
    }

}
