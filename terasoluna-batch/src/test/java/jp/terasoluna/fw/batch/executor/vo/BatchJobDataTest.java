package jp.terasoluna.fw.batch.executor.vo;

import java.sql.Timestamp;

import junit.framework.TestCase;

public class BatchJobDataTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSetAndGetJobSequenceId() {
        BatchJobData data = new BatchJobData();
        data.setJobSequenceId("a");
        assertEquals("a", data.getJobSequenceId());
    }

    public void testSetAndGetJobAppCd() {
        BatchJobData data = new BatchJobData();
        data.setJobAppCd("a");
        assertEquals("a", data.getJobAppCd());
    }

    public void testSetAndGetJobArgNm1() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm1("a");
        assertEquals("a", data.getJobArgNm1());
    }

    public void testSetAndGetJobArgNm2() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm2("a");
        assertEquals("a", data.getJobArgNm2());
    }

    public void testSetAndGetJobArgNm3() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm3("a");
        assertEquals("a", data.getJobArgNm3());
    }

    public void testSetAndGetJobArgNm4() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm4("a");
        assertEquals("a", data.getJobArgNm4());
    }

    public void testSetAndGetJobArgNm5() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm5("a");
        assertEquals("a", data.getJobArgNm5());
    }

    public void testSetAndGetJobArgNm6() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm6("a");
        assertEquals("a", data.getJobArgNm6());
    }

    public void testSetAndGetJobArgNm7() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm7("a");
        assertEquals("a", data.getJobArgNm7());
    }

    public void testSetAndGetJobArgNm8() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm8("a");
        assertEquals("a", data.getJobArgNm8());
    }

    public void testSetAndGetJobArgNm9() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm9("a");
        assertEquals("a", data.getJobArgNm9());
    }

    public void testSetAndGetJobArgNm10() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm10("a");
        assertEquals("a", data.getJobArgNm10());
    }

    public void testSetAndGetJobArgNm11() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm11("a");
        assertEquals("a", data.getJobArgNm11());
    }

    public void testSetAndGetJobArgNm12() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm12("a");
        assertEquals("a", data.getJobArgNm12());
    }

    public void testSetAndGetJobArgNm13() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm13("a");
        assertEquals("a", data.getJobArgNm13());
    }

    public void testSetAndGetJobArgNm14() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm14("a");
        assertEquals("a", data.getJobArgNm14());
    }

    public void testSetAndGetJobArgNm15() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm15("a");
        assertEquals("a", data.getJobArgNm15());
    }

    public void testSetAndGetJobArgNm16() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm16("a");
        assertEquals("a", data.getJobArgNm16());
    }

    public void testSetAndGetJobArgNm17() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm17("a");
        assertEquals("a", data.getJobArgNm17());
    }

    public void testSetAndGetJobArgNm18() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm18("a");
        assertEquals("a", data.getJobArgNm18());
    }

    public void testSetAndGetJobArgNm19() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm19("a");
        assertEquals("a", data.getJobArgNm19());
    }

    public void testSetAndGetJobArgNm20() {
        BatchJobData data = new BatchJobData();
        data.setJobArgNm20("a");
        assertEquals("a", data.getJobArgNm20());
    }

    public void testSetAndGetErrAppStatus() {
        BatchJobData data = new BatchJobData();
        data.setErrAppStatus("a");
        // TODO 正しい？
        assertEquals("a", data.getBLogicAppStatus());
    }

    public void testSetAndGetCurAppStatus() {
        BatchJobData data = new BatchJobData();
        data.setCurAppStatus("a");
        assertEquals("a", data.getCurAppStatus());
    }

    public void testSetAndGetAddDateTime() {
        BatchJobData data = new BatchJobData();
        Timestamp ts = Timestamp.valueOf("2011-01-01 00:00:00");
        data.setAddDateTime(ts);
        assertEquals(ts, data.getAddDateTime());
    }

    public void testSetAndGetUpdDateTime() {
        BatchJobData data = new BatchJobData();
        Timestamp ts = Timestamp.valueOf("2011-01-01 00:00:00");
        data.setUpdDateTime(ts);
        assertEquals(ts, data.getUpdDateTime());
    }

    public void testToString() {
        BatchJobData data = new BatchJobData();
        data.setJobSequenceId("a");
        data.setJobAppCd("a0");
        data.setJobArgNm1("a1");
        data.setJobArgNm2("a2");
        data.setJobArgNm3("a3");
        data.setJobArgNm4("a4");
        data.setJobArgNm5("a5");
        data.setJobArgNm6("a6");
        data.setJobArgNm7("a7");
        data.setJobArgNm8("a8");
        data.setJobArgNm9("a9");
        data.setJobArgNm10("a10");
        data.setJobArgNm11("a11");
        data.setJobArgNm12("a12");
        data.setJobArgNm13("a13");
        data.setJobArgNm14("a14");
        data.setJobArgNm15("a15");
        data.setJobArgNm16("a16");
        data.setJobArgNm17("a17");
        data.setJobArgNm18("a18");
        data.setJobArgNm19("a19");
        data.setJobArgNm20("a20");
        data.setErrAppStatus("b");
        data.setCurAppStatus("c");
        data.setAddDateTime(Timestamp.valueOf("2011-01-01 00:00:00"));
        data.setUpdDateTime(Timestamp.valueOf("2011-02-01 00:00:00"));
        assertEquals(
                "BatchJobData[jobSequenceId=a,jobAppCd=a0,jobArgNm1=a1,jobArgNm2=a2,jobArgNm3=a3,jobArgNm4=a4,jobArgNm5=a5,jobArgNm6=a6,jobArgNm7=a7,jobArgNm8=a8,jobArgNm9=a9,jobArgNm10=a10,jobArgNm11=a11,jobArgNm12=a12,jobArgNm13=a13,jobArgNm14=a14,jobArgNm15=a15,jobArgNm16=a16,jobArgNm17=a17,jobArgNm18=a18,jobArgNm19=a19,jobArgNm20=a20,blogicAppStatus=b,curAppStatus=c,addDateTime=2011-01-01 00:00:00.0,updDateTime=2011-02-01 00:00:00.0]",
                data.toString());
    }

}
