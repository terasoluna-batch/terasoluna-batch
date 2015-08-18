package jp.terasoluna.fw.batch.executor.vo;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;

import org.junit.Test;

public class BatchJobManagementUpdateParamTest {

    @Test
    public void testSetAndGetJobSequenceId() {
        BatchJobManagementUpdateParam p = new BatchJobManagementUpdateParam();
        p.setJobSequenceId("a");
        assertEquals("a", p.getJobSequenceId());
    }

    @Test
    public void testSetAndGetBLogicAppStatus() {
        BatchJobManagementUpdateParam p = new BatchJobManagementUpdateParam();
        p.setBLogicAppStatus("a");
        assertEquals("a", p.getBLogicAppStatus());
    }

    @Test
    public void testSetAndGetCurAppStatus() {
        BatchJobManagementUpdateParam p = new BatchJobManagementUpdateParam();
        p.setCurAppStatus("a");
        assertEquals("a", p.getCurAppStatus());
    }

    @Test
    public void testSetAndGetUpdDateTime() {
        BatchJobManagementUpdateParam p = new BatchJobManagementUpdateParam();
        Timestamp ts = Timestamp.valueOf("2011-01-01 00:00:00");
        p.setUpdDateTime(ts);
        assertEquals(ts, p.getUpdDateTime());
    }

    @Test
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
