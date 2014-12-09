/**
 * 
 */
package jp.terasoluna.fw.batch.constants;

import junit.framework.TestCase;

/**
 *
 */
public class EventConstantsTest extends TestCase {

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
     * testEventConstants001.
     * @throws Exception
     */
    public void testEventConstants001() throws Exception {
        EventConstants ec = new EventConstants();

        assertNotNull(ec);
    }

}
