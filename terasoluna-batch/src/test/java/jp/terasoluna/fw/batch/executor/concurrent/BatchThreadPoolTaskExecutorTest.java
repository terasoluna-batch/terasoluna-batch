package jp.terasoluna.fw.batch.executor.concurrent;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


public class BatchThreadPoolTaskExecutorTest {

    /**
     * setQueueCapacityの引数に関わらず、queueCapacityに0が設定されること。
     */
    @Test
    public void testSetQueueCapacity() throws Exception {
        BatchThreadPoolTaskExecutor executor = new BatchThreadPoolTaskExecutor();
        executor.setQueueCapacity(100);
        Field f = ThreadPoolTaskExecutor.class.getDeclaredField("queueCapacity");
        f.setAccessible(true);
        assertEquals(0, f.getInt(executor));
    }

    /**
     * queueCapacityに0が設定されること。
     */
    @Test
    public void testBatchThreadPoolTaskExecutor() throws Exception {
        BatchThreadPoolTaskExecutor executor = new BatchThreadPoolTaskExecutor();
        Field f = ThreadPoolTaskExecutor.class.getDeclaredField("queueCapacity");
        f.setAccessible(true);
        assertEquals(0, f.getInt(executor));
    }

}
