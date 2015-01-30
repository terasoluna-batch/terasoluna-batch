/**
 * @(#)CollectorExceptionTest.java
 * システム名:TERASOLUNA-IDE
 * ファイルバージョン:$Id: CollectorExceptionTest.java 6835 2012-01-13 13:44:36Z bthashidumets $
 * Copyright 2009 NTT DATA Corporation.
 */
package jp.terasoluna.fw.collector.exception;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * [このクラスの説明を書きましょう]
 * @version $Revision$
 */
public class CollectorExceptionTest {

    /**
     * [メソッドの説明を書きましょう]
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * [メソッドの説明を書きましょう]
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * [メソッドの説明を書きましょう]
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * [メソッドの説明を書きましょう]
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * {@link jp.terasoluna.fw.collector.exception.CollectorException#CollectorException()} のためのテスト・メソッド。
     */
    @Test
    public void testCollectorException001() {
        CollectorException ex = new CollectorException();

        assertNotNull(ex);
    }

    /**
     * {@link jp.terasoluna.fw.collector.exception.CollectorException#CollectorException(java.lang.String)} のためのテスト・メソッド。
     */
    @Test
    public void testCollectorExceptionString001() {
        CollectorException ex = new CollectorException("hoge");

        assertNotNull(ex);
    }

    /**
     * {@link jp.terasoluna.fw.collector.exception.CollectorException#CollectorException(java.lang.Throwable)} のためのテスト・メソッド。
     */
    @Test
    public void testCollectorExceptionThrowable001() {
        Exception cause = new Exception();
        CollectorException ex = new CollectorException(cause);

        assertNotNull(ex);
    }

    /**
     * {@link jp.terasoluna.fw.collector.exception.CollectorException#CollectorException(java.lang.String, java.lang.Throwable)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testCollectorExceptionStringThrowable001() {
        Exception cause = new Exception();
        CollectorException ex = new CollectorException("hoge", cause);

        assertNotNull(ex);
    }

}
