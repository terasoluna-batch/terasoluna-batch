package jp.terasoluna.fw.batch.executor.vo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BLogicResultTest {

    @Test
    public void testSetAndGetBlogicStatus() {
        BLogicResult r = new BLogicResult();
        r.setBlogicStatus(100);
        assertEquals(100, r.getBlogicStatus());
    }

    @Test
    public void testSetAndGetBlogicThrowable() {
        BLogicResult r = new BLogicResult();
        Exception e = new Exception();
        r.setBlogicThrowable(e);
        assertEquals(e, r.getBlogicThrowable());
    }

}
