package jp.terasoluna.fw.collector.vo;

import static org.junit.Assert.*;
import jp.terasoluna.fw.collector.validate.ValidateErrorStatus;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataValueObjectTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * 
     */
    @Test
    public void testDataValueObjectObject001() {
        String hoge = "hoge";
        DataValueObject dvo = new DataValueObject(hoge);

        assertNotNull(dvo);
        assertEquals(hoge, dvo.getValue());
    }

    /**
     * 
     */
    @Test
    public void testDataValueObjectObjectLong001() {
        String hoge = "hoge";
        long dataCount = 3;
        DataValueObject dvo = new DataValueObject(hoge, dataCount);

        assertNotNull(dvo);
        assertEquals(hoge, dvo.getValue());
        assertEquals(dataCount, dvo.getDataCount());
    }

    /**
     * 
     */
    @Test
    public void testDataValueObjectThrowable001() {
        Exception exception = new Exception();
        DataValueObject dvo = new DataValueObject(exception);

        assertNotNull(dvo);
        assertEquals(exception, dvo.getThrowable());
    }

    /**
     * 
     */
    @Test
    public void testDataValueObjectValidateErrorStatus001() {
        ValidateErrorStatus validateStatus = ValidateErrorStatus.END;
        DataValueObject dvo = new DataValueObject(validateStatus);

        assertNotNull(dvo);
        assertEquals(validateStatus, dvo.getValidateStatus());
    }

    /**
     * 
     */
    @Test
    public void testDataValueObjectCollectorStatus001() {
        CollectorStatus collectorStatus = CollectorStatus.END;
        DataValueObject dvo = new DataValueObject(collectorStatus);

        assertNotNull(dvo);
        assertEquals(collectorStatus, dvo.getCollectorStatus());
    }

    /**
     * 
     */
    @Test
    public void testDataValueObjectCollectorStatus002() {
        CollectorStatus collectorStatus1 = null;
        CollectorStatus collectorStatus2 = CollectorStatus.END;
        DataValueObject dvo = new DataValueObject(collectorStatus1);
        dvo.setCollectorStatus(collectorStatus2);

        assertNotNull(dvo);
        assertEquals(collectorStatus2, dvo.getCollectorStatus());
    }
}
