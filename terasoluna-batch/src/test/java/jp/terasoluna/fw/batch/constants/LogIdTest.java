/**
 * 
 */
package jp.terasoluna.fw.batch.constants;

import junit.framework.TestCase;

/**
 *
 */
public class LogIdTest extends TestCase {

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * testLogId001
     * @throws Exception
     */
    public void testLogId001() throws Exception {
        LogId li = new LogId();

        assertNotNull(li);
    }

}
