package jp.terasoluna.fw.collector.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CollectorUtilityTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * testCollectorUtility001.
     */
    @Test
    public void testCollectorUtility001() {
        CollectorUtility cu = new CollectorUtility();

        assertNotNull(cu);
    }

    /**
     * testCloseQuietly001.
     */
    @Test
    public void testCloseQuietly001() {
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();

        // テスト
        CollectorUtility.closeQuietly(collector);

        // 検証
        assertTrue(collector.isCloseCalled());
    }

    /**
     * testCloseQuietly002.
     */
    @Test
    public void testCloseQuietly002() {
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setCloseException(true);

        // テスト
        CollectorUtility.closeQuietly(collector);

        // 検証
        assertTrue(collector.isCloseCalled());
    }

    /**
     * testCloseQuietlyFileLineIteratorOfT001.
     */
    @Test
    public void testCloseQuietlyFileLineIteratorOfT001() {
        FileLineIteratorStub<Hoge> iterator = new FileLineIteratorStub<Hoge>();

        // テスト
        CollectorUtility.closeQuietly(iterator);

        // 検証
        assertTrue(iterator.isCloseFileCalled());
    }

    /**
     * testCloseQuietlyFileLineIteratorOfT002.
     */
    @Test
    public void testCloseQuietlyFileLineIteratorOfT002() {
        FileLineIteratorStub<Hoge> iterator = new FileLineIteratorStub<Hoge>();
        iterator.setCloseException(true);

        // テスト
        CollectorUtility.closeQuietly(iterator);

        // 検証
        assertTrue(iterator.isCloseFileCalled());
    }

    /**
     * testCloseQuietlyFileLineWriterOfT001.
     */
    @Test
    public void testCloseQuietlyFileLineWriterOfT001() {
        FileLineWriterStub<Hoge> writer = new FileLineWriterStub<Hoge>();

        // テスト
        CollectorUtility.closeQuietly(writer);

        // 検証
        assertTrue(writer.isCloseFileCalled());
    }

    /**
     * testCloseQuietlyFileLineWriterOfT002.
     */
    @Test
    public void testCloseQuietlyFileLineWriterOfT002() {
        FileLineWriterStub<Hoge> writer = new FileLineWriterStub<Hoge>();
        writer.setCloseException(true);

        // テスト
        CollectorUtility.closeQuietly(writer);

        // 検証
        assertTrue(writer.isCloseFileCalled());
    }
}
