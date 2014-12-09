package jp.terasoluna.fw.batch.executor;

import java.util.Map;

import jp.terasoluna.fw.ex.unit.util.ReflectionUtils;
import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ThreadGroupApplicationContextHolderTest extends TestCase {

    ApplicationContext ctx = new ClassPathXmlApplicationContext(
            "classpath:beansDef/AdminContext.xml");

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        // ‚¢‚Á‚½‚ñƒNƒŠƒA
        ((Map<?, ?>) ReflectionUtils.getField(
                ThreadGroupApplicationContextHolder.class, "tga")).clear();
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
     * testThreadGroupApplicationContextHolder001
     * @throws Exception
     */
    public void testThreadGroupApplicationContextHolder001() throws Exception {
        ThreadGroupApplicationContextHolder holder = new ThreadGroupApplicationContextHolder();
        assertNotNull(holder);
    }

    /**
     * testGetThreadGroupApplicationContext001
     * @throws Exception
     */
    public void testGetThreadGroupApplicationContext001() throws Exception {
        ThreadGroup g = new ThreadGroup("hoge");
        Thread th = new Thread(g, new Runnable() {
            public void run() {
                ThreadGroupApplicationContextHolder.setApplicationContext(ctx);
            }
        });
        th.start();
        th.join();
        assertEquals(ctx, ThreadGroupApplicationContextHolder
                .getThreadGroupApplicationContext(g));
    }

    /**
     * testSetAndGetApplicationContext001
     */
    public void testSetAndGetApplicationContext001() {
        assertNull(ThreadGroupApplicationContextHolder
                .getCurrentThreadGroupApplicationContext());
        ThreadGroupApplicationContextHolder.setApplicationContext(ctx);
        assertEquals(ctx, ThreadGroupApplicationContextHolder
                .getCurrentThreadGroupApplicationContext());
    }

    /**
     * testSetAndGetApplicationContext002
     */
    public void testSetAndGetApplicationContext002() {
        ThreadGroupApplicationContextHolder.setApplicationContext(null);
        assertNull(ThreadGroupApplicationContextHolder
                .getCurrentThreadGroupApplicationContext());
    }

    /**
     * testRemoveApplicationContext001
     */
    public void testRemoveApplicationContext001() {
        ThreadGroupApplicationContextHolder.setApplicationContext(ctx);
        assertEquals(ctx, ThreadGroupApplicationContextHolder
                .getCurrentThreadGroupApplicationContext());
        ThreadGroupApplicationContextHolder.removeApplicationContext();
        assertNull(ThreadGroupApplicationContextHolder
                .getCurrentThreadGroupApplicationContext());
    }

    /**
     * testRemoveApplicationContext002
     */
    public void testRemoveApplicationContext002() {
        Thread th = Thread.currentThread();
        ThreadGroup g = th.getThreadGroup();
        try {
            ReflectionUtils.setField(th, "group", null);
            ThreadGroupApplicationContextHolder.removeApplicationContext();
        } finally {
            ReflectionUtils.setField(th, "group", g);
        }
    }

    /**
     * getThreadGroup
     * @return
     */
    public ThreadGroup getThreadGroup() {
        return ReflectionUtils.invoke(
                ThreadGroupApplicationContextHolder.class, "getThreadGroup");
    }

    /**
     * testGetThreadGroup001
     */
    public void testGetThreadGroup001() {
        assertEquals(Thread.currentThread().getThreadGroup(), getThreadGroup());
    }

    /**
     * testGetThreadGroup002
     */
    public void testGetThreadGroup002() {
        Thread th = Thread.currentThread();
        ThreadGroup g = th.getThreadGroup();
        try {
            ReflectionUtils.setField(th, "group", null);
            assertEquals(null, getThreadGroup());
        } finally {
            ReflectionUtils.setField(th, "group", g);
        }
    }

    /**
     * getThreadMessage
     * @return
     */
    public String getThreadMessage() {
        return ReflectionUtils.invoke(
                ThreadGroupApplicationContextHolder.class, "getThreadMessage");
    }

    /**
     * testGetThreadMessage001
     */
    public void testGetThreadMessage001() {
        Thread th = Thread.currentThread();
        ThreadGroup g = th.getThreadGroup();
        String expected = String.format(" tg:[%s] t:[%s]", g.getName(), th
                .getName());
        assertEquals(expected, getThreadMessage());
    }

    /**
     * testGetThreadMessage002
     */
    public void testGetThreadMessage002() {
        Thread th = Thread.currentThread();
        ThreadGroup g = th.getThreadGroup();
        try {
            ReflectionUtils.setField(th, "group", null);
            String expected = String.format(" t:[%s]", g.getName(), th
                    .getName());
            assertEquals(expected, getThreadMessage());
        } finally {
            ReflectionUtils.setField(th, "group", g);
        }
    }
}
