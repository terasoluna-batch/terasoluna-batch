package jp.terasoluna.fw.batch.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

public class ThreadGroupApplicationContextHolderTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:beansDef/AdminContext.xml");

    /*
     * (non-Javadoc)
     */
    @Before
    public void setUp() throws Exception {
        // いったんクリア
        Field field = ThreadGroupApplicationContextHolder.class.getDeclaredField("tga");
        field.setAccessible(true);
        ((Map<?, ?>) field.get(ThreadGroupApplicationContextHolder.class)).clear();
    }

    /**
     * testThreadGroupApplicationContextHolder001
     * @throws Exception
     */
    @Test
    public void testThreadGroupApplicationContextHolder001() throws Exception {
        ThreadGroupApplicationContextHolder holder = new ThreadGroupApplicationContextHolder();
        assertNotNull(holder);
    }

    /**
     * testGetThreadGroupApplicationContext001
     * @throws Exception
     */
    @Test
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
    @Test
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
    @Test
    public void testSetAndGetApplicationContext002() {
        ThreadGroupApplicationContextHolder.setApplicationContext(null);
        assertNull(ThreadGroupApplicationContextHolder
                .getCurrentThreadGroupApplicationContext());
    }

    /**
     * testRemoveApplicationContext001
     */
    @Test
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
    @Test
    public void testRemoveApplicationContext002() {
        Thread th = Thread.currentThread();
        ThreadGroup g = th.getThreadGroup();
        try {
            ReflectionTestUtils.setField(th, "group", null);
            ThreadGroupApplicationContextHolder.removeApplicationContext();
        } finally {
            ReflectionTestUtils.setField(th, "group", g);
        }
    }

    /**
     * getThreadGroup
     * @return
     * @throws Exception 
     */
    public ThreadGroup getThreadGroup() {
        Method method;
        try {
            method = ThreadGroupApplicationContextHolder.class.getDeclaredMethod("getThreadGroup");
            method.setAccessible(true);
            return (ThreadGroup) method.invoke(ThreadGroupApplicationContextHolder.class);
        } catch (Exception e) {
            fail();
        }
        return null;
    }

    /**
     * testGetThreadGroup001
     */
    @Test
    public void testGetThreadGroup001() {
        assertEquals(Thread.currentThread().getThreadGroup(), getThreadGroup());
    }

    /**
     * testGetThreadGroup002
     */
    @Test
    public void testGetThreadGroup002() {
        Thread th = Thread.currentThread();
        ThreadGroup g = th.getThreadGroup();
        try {
            ReflectionTestUtils.setField(th, "group", null);
            assertEquals(null, getThreadGroup());
        } finally {
            ReflectionTestUtils.setField(th, "group", g);
        }
    }

    /**
     * getThreadMessage
     * @return
     */
    public String getThreadMessage() {
        Method method;
        try {
            method = ThreadGroupApplicationContextHolder.class.getDeclaredMethod("getThreadMessage");
            method.setAccessible(true);
            return (String) method.invoke(ThreadGroupApplicationContextHolder.class);
        } catch (Exception e) {
            fail();
        }
        return null;
    }

    /**
     * testGetThreadMessage001
     */
    @Test
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
    @Test
    public void testGetThreadMessage002() {
        Thread th = Thread.currentThread();
        ThreadGroup g = th.getThreadGroup();
        try {
            ReflectionTestUtils.setField(th, "group", null);
            String expected = String.format(" t:[%s]", g.getName(), th
                    .getName());
            assertEquals(expected, getThreadMessage());
        } finally {
            ReflectionTestUtils.setField(th, "group", g);
        }
    }
}
