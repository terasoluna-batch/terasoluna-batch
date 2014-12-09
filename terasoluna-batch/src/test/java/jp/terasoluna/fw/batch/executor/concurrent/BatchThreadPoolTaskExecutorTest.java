package jp.terasoluna.fw.batch.executor.concurrent;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


public class BatchThreadPoolTaskExecutorTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }


    /**
     * setQueueCapacityÇÃà¯êîÇ…ä÷ÇÌÇÁÇ∏ÅAqueueCapacityÇ…0Ç™ê›íËÇ≥ÇÍÇÈÇ±Ç∆ÅB
     */
    public void testSetQueueCapacity() throws Exception {
        BatchThreadPoolTaskExecutor executor = new BatchThreadPoolTaskExecutor();
        executor.setQueueCapacity(100);
        Field f = ThreadPoolTaskExecutor.class.getDeclaredField("queueCapacity");
        f.setAccessible(true);
        assertEquals(0, f.getInt(executor));
    }

    /**
     * queueCapacityÇ…0Ç™ê›íËÇ≥ÇÍÇÈÇ±Ç∆ÅB
     */
    public void testBatchThreadPoolTaskExecutor() throws Exception {
        BatchThreadPoolTaskExecutor executor = new BatchThreadPoolTaskExecutor();
        Field f = ThreadPoolTaskExecutor.class.getDeclaredField("queueCapacity");
        f.setAccessible(true);
        assertEquals(0, f.getInt(executor));
    }

}
