/**
 * 
 */
package jp.terasoluna.fw.batch.constants;

import junit.framework.TestCase;

/**
 *
 */
public class JobStatusConstantsTest extends TestCase {

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
     * testJobStatusConstants001.
     * @throws Exception
     */
    public void testJobStatusConstants001() throws Exception {
        JobStatusConstants jsc = new JobStatusConstants();

        assertNotNull(jsc);
    }
}
