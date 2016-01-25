/*
 * Copyright (c) 2011-2016 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.terasoluna.fw.batch.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("deprecation")
public class ThreadGroupApplicationContextHolderTest {

    private ApplicationContext ctx = Mockito.mock(ApplicationContext.class);

    /**
     * pre-process
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // いったんクリア
        Map<?, ?> tga = (Map<?, ?>) ReflectionTestUtils.getField(
                ThreadGroupApplicationContextHolder.class, "tga");
        tga.clear();
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
        String beforeTga = ReflectionTestUtils.getField(
                ThreadGroupApplicationContextHolder.class, "tga").toString();

        try {
            ReflectionTestUtils.setField(th, "group", null);
            ThreadGroupApplicationContextHolder.removeApplicationContext();
        } finally {
            ReflectionTestUtils.setField(th, "group", g);
        }
        assertEquals(beforeTga, ReflectionTestUtils.getField(
                ThreadGroupApplicationContextHolder.class, "tga").toString());
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
     * getThreadGroup
     * @return
     * @throws Exception
     */
    private ThreadGroup getThreadGroup() {
        Method method;
        try {
            method = ThreadGroupApplicationContextHolder.class
                    .getDeclaredMethod("getThreadGroup");
            method.setAccessible(true);
            return (ThreadGroup) method.invoke(
                    ThreadGroupApplicationContextHolder.class);
        } catch (Exception e) {
            throw new RuntimeException("faild.", e);
        }
    }
}
