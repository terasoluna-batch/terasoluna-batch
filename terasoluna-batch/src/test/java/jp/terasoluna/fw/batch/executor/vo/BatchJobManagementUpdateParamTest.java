package jp.terasoluna.fw.batch.executor.vo;

import java.sql.Timestamp;

import junit.framework.TestCase;

public class BatchJobManagementUpdateParamTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSetAndGetJobSequenceId() {
        BatchJobManagementUpdateParam p = new BatchJobManagementUpdateParam();
        p.setJobSequenceId("a");
        assertEquals("a", p.getJobSequenceId());
    }

    public void testSetAndGetBLogicAppStatus() {
        BatchJobManagementUpdateParam p = new BatchJobManagementUpdateParam();
        p.setBLogicAppStatus("a");
        assertEquals("a", p.getBLogicAppStatus());
    }

    public void testSetAndGetCurAppStatus() {
        BatchJobManagementUpdateParam p = new BatchJobManagementUpdateParam();
        p.setCurAppStatus("a");
        assertEquals("a", p.getCurAppStatus());
    }

    public void testSetAndGetUpdDateTime() {
        BatchJobManagementUpdateParam p = new BatchJobManagementUpdateParam();
        Timestamp ts = Timestamp.valueOf("2011-01-01 00:00:00");
        p.setUpdDateTime(ts);
        assertEquals(ts, p.getUpdDateTime());
    }

    public void testToString() {
        BatchJobManagementUpdateParam p = new BatchJobManagementUpdateParam();
        p.setJobSequenceId("a");
        p.setBLogicAppStatus("b");
        p.setCurAppStatus("c");
        Timestamp ts = Timestamp.valueOf("2011-01-01 00:00:00");
        p.setUpdDateTime(ts);
        assertEquals("BatchJobManagementUpdateParam[jobSequenceId=a,BLogicAppStatus=b,curAppStatus=c,updDateTime=2011-01-01 00:00:00.0]", p.toString());
    }

}
