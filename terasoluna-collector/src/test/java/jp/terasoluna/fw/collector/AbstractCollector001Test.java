package jp.terasoluna.fw.collector;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import jp.terasoluna.fw.collector.validate.ValidateErrorStatus;
import jp.terasoluna.fw.collector.validate.ValidationErrorException;
import jp.terasoluna.fw.collector.vo.DataValueObject;
import jp.terasoluna.fw.exception.SystemException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AbstractCollector001Test {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        AbstractCollector.setVerbose(true);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        AbstractCollector.setVerbose(false);
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
    public void testExecute001() {
        AbstractCollectorStubHasNext1<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext1<AbstractCollectorTestBean>();

        // テスト
        try {
            col.execute();
        } catch (SystemException e) {
            assertNotNull(e);
            assertEquals(SystemException.class, e.getClass());
            return;
        }

        fail();
        return;
    }

    /**
     * 
     */
    @Test
    public void testExecute002() {
        AbstractCollectorStub7<AbstractCollectorTestBean> col = new AbstractCollectorStub7<AbstractCollectorTestBean>();

        // テスト
        col.execute();
    }

    /**
     * 
     */
    @Test
    public void testHasNext001() {
        AbstractCollectorStubHasNext2<AbstractCollectorTestBean> col = new AbstractCollectorStubHasNext2<AbstractCollectorTestBean>();

        // テスト
        boolean result = col.hasNext();

        assertFalse(result);
    }

    /**
     * 
     */
    @Test
    public void testHasNext002() {
        AbstractCollectorStub3<AbstractCollectorTestBean> col = new AbstractCollectorStub3<AbstractCollectorTestBean>();

        // テスト
        boolean result = col.hasNext();

        assertFalse(result);
    }

    @Test
    public void testNext001() {
        AbstractCollectorStubNext1<AbstractCollectorTestBean> col = new AbstractCollectorStubNext1<AbstractCollectorTestBean>();

        AbstractCollectorTestBean result = null;

        try {
            result = col.next();
            fail();
        } catch (NoSuchElementException e) {
            assertNotNull(e);
            assertNull(result);
        }
    }

    @Test
    public void testNext002() {
        AbstractCollectorStubNext2<AbstractCollectorTestBean> col = new AbstractCollectorStubNext2<AbstractCollectorTestBean>();

        @SuppressWarnings("unused")
        AbstractCollectorTestBean result = null;
        try {
            result = col.next();
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals(SystemException.class, e.getClass());
            assertEquals(Exception.class, e.getCause().getClass());
            assertEquals(null, e.getCause().getMessage());
            return;
        }

        fail();
    }

    @Test
    public void testNext003() {
        AbstractCollectorStubNext3<AbstractCollectorTestBean> col = new AbstractCollectorStubNext3<AbstractCollectorTestBean>();

        @SuppressWarnings("unused")
        AbstractCollectorTestBean result = null;
        try {
            result = col.next();
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals(SystemException.class, e.getClass());
            assertEquals(Exception.class, e.getCause().getClass());
            assertEquals("hoge", e.getCause().getMessage());
            return;
        }

        fail();
    }

    @Test
    public void testNext004() {
        AbstractCollectorStub8<AbstractCollectorTestBean> col = new AbstractCollectorStub8<AbstractCollectorTestBean>();

        AbstractCollectorTestBean result = null;

        result = col.next();
        // poll()時にInterruptedExceptionが発生しても、それ以前にpeek()でオブジェクトが得られている。
        assertNotNull(result);
        assertEquals("hoge", result.getHoge());
    }

    @Test
    public void testNext005() {
        AbstractCollectorStub9<AbstractCollectorTestBean> col = new AbstractCollectorStub9<AbstractCollectorTestBean>();

        @SuppressWarnings("unused")
        AbstractCollectorTestBean result = null;
        try {
            result = col.next();
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals(SystemException.class, e.getClass());
            assertEquals(Exception.class, e.getCause().getClass());
            assertEquals(null, e.getCause().getMessage());
            return;
        }

        fail();
    }

    @Test
    public void testNext006() {
        AbstractCollectorStub10<AbstractCollectorTestBean> col = new AbstractCollectorStub10<AbstractCollectorTestBean>();

        AbstractCollectorTestBean result = null;

        try {
            result = col.next();
            fail();
        } catch (NoSuchElementException e) {
            assertNotNull(e);
            assertNull(result);
        }
    }

    @Test
    public void testGetNext001() {
        AbstractCollectorStub11<AbstractCollectorTestBean> col = new AbstractCollectorStub11<AbstractCollectorTestBean>();

        AbstractCollectorTestBean result = null;
        result = col.getNext();

        assertEquals(null, result);
        return;
    }

    @Test
    public void testGetPrevious001() {
        AbstractCollectorStub15<AbstractCollectorTestBean> col = new AbstractCollectorStub15<AbstractCollectorTestBean>();

        AbstractCollectorTestBean result = null;
        result = col.getPrevious();

        assertEquals(null, result);
        return;
    }

    @Test
    public void testGetCurrent001() {
        AbstractCollectorStub12<AbstractCollectorTestBean> col = new AbstractCollectorStub12<AbstractCollectorTestBean>();

        AbstractCollectorTestBean result = null;
        result = col.getCurrent();

        assertEquals(null, result);
        return;
    }

    @Test
    public void testRemove001() {
        AbstractCollectorStub13<AbstractCollectorTestBean> col = new AbstractCollectorStub13<AbstractCollectorTestBean>();

        try {
            col.remove();
            fail();
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void addQueue001() throws InterruptedException {
        AbstractCollectorStub16<AbstractCollectorTestBean> col = new AbstractCollectorStub16<AbstractCollectorTestBean>();

        col.execute();
        col.addQueue(null);

        assertNotNull(col.getQueue());
        DataValueObject dvo = ((BlockingQueue<DataValueObject>) col.getQueue())
                .poll(1, TimeUnit.MILLISECONDS);
        assertNotNull(dvo);
        assertEquals(ValidationErrorException.class, dvo.getThrowable()
                .getClass());
        return;
    }

    @Test
    public void addQueue002() throws InterruptedException {
        AbstractCollectorStub17<AbstractCollectorTestBean> col = new AbstractCollectorStub17<AbstractCollectorTestBean>();

        col.execute();
        col.addQueue(null);

        assertNotNull(col.getQueue());
        DataValueObject dvo = ((BlockingQueue<DataValueObject>) col.getQueue())
                .poll();
        assertNotNull(dvo);
        assertEquals(ValidationErrorException.class, dvo.getThrowable()
                .getClass());
        return;
    }

    @Test
    public void addQueue003() throws InterruptedException {
        AbstractCollectorStub18<AbstractCollectorTestBean> col = new AbstractCollectorStub18<AbstractCollectorTestBean>();
        DataValueObject dvo = new DataValueObject("hoge");

        col.execute();
        col.addQueue(dvo);

        assertNotNull(col.getQueue());
        DataValueObject dvo2 = ((BlockingQueue<DataValueObject>) col.getQueue())
                .poll();
        assertNotNull(dvo2);
        assertEquals("hoge", dvo.getValue());
        return;
    }

    @Test
    public void addQueue004() throws InterruptedException {
        AbstractCollectorStub19<AbstractCollectorTestBean> col = new AbstractCollectorStub19<AbstractCollectorTestBean>();
        DataValueObject dvo = new DataValueObject("hoge");

        col.execute();
        col.addQueue(dvo);

        assertNotNull(col.getQueue());
        DataValueObject dvo2 = ((BlockingQueue<DataValueObject>) col.getQueue())
                .poll();
        assertNotNull(dvo2);
        assertEquals(ValidateErrorStatus.END, dvo2.getValidateStatus());
        return;
    }

    @Test
    public void testHandleValidationError001() {
        AbstractCollectorStub14<AbstractCollectorTestBean> col = new AbstractCollectorStub14<AbstractCollectorTestBean>();

        assertEquals(ValidateErrorStatus.SKIP, col.handleValidationError(null,
                null));
        return;

    }

    @Test
    public void testGetQueue001() {
        AbstractCollectorStub13<AbstractCollectorTestBean> col = new AbstractCollectorStub13<AbstractCollectorTestBean>();

        assertNull(col.getQueue());
        col.execute();
        assertNotNull(col.getQueue());
        assertEquals(0, col.getQueue().size());
        return;

    }

    @Test
    public void testGetSleepWait001() {
        AbstractCollectorStub13<AbstractCollectorTestBean> col = new AbstractCollectorStub13<AbstractCollectorTestBean>();

        assertNotNull(col.getSleepWait());
        assertEquals(1, col.getSleepWait());
        return;

    }

    @Test
    public void testSetSleepWait001() {
        AbstractCollectorStub13<AbstractCollectorTestBean> col = new AbstractCollectorStub13<AbstractCollectorTestBean>();

        col.setSleepWait(10);
        assertEquals(10, col.getSleepWait());
        return;

    }

    // @Test
    // public void testIterator() {
    // fail("まだ実装されていません");
    // }
    //
    // @Test
    // public void testSetQueueSize() {
    // // fail("まだ実装されていません");
    // }

}
