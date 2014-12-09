package jp.terasoluna.fw.batch.executor.vo;

import junit.framework.TestCase;

public class BLogicResultTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSetAndGetBlogicStatus() {
        BLogicResult r = new BLogicResult();
        r.setBlogicStatus(100);
        assertEquals(100, r.getBlogicStatus());
    }

    public void testSetAndGetBlogicThrowable() {
        BLogicResult r = new BLogicResult();
        Exception e = new Exception();
        r.setBlogicThrowable(e);
        assertEquals(e, r.getBlogicThrowable());
    }

}
