package jp.terasoluna.fw.batch.executor.vo;

import junit.framework.TestCase;

public class BatchJobManagementParamTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSetAndGetJobSequenceId() {
        BatchJobManagementParam p = new BatchJobManagementParam();
        p.setJobSequenceId("a");
        assertEquals("a", p.getJobSequenceId());
    }

    public void testSetAndGetForUpdate() {
        BatchJobManagementParam p = new BatchJobManagementParam();
        p.setForUpdate(true);
        assertTrue(p.getForUpdate());
    }

    public void testToString() {
        BatchJobManagementParam p = new BatchJobManagementParam();
        p.setJobSequenceId("a");
        p.setForUpdate(true);
        assertEquals("BatchJobManagementParam[jobSequenceId=a,forUpdate=true]", p.toString());
    }

}
