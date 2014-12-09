package jp.terasoluna.fw.collector.util;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.util.strategy.CompareStrategy;
import jp.terasoluna.fw.file.dao.FileLineException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ControlBreakCheckerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * testControlBreakChecker0001.
     */
    @Test
    public void testControlBreakChecker0001() {
        ControlBreakChecker cbc = new ControlBreakChecker();

        assertNotNull(cbc);
    }

    /**
     * testIsPreBreak0001.
     */
    @Test
    public void testIsPreBreak0001() {
        Collector<HogeBean> collector = null;
        String s = null;

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsPreBreak0002.
     */
    @Test
    public void testIsPreBreak0002() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String s = "hogeString";

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s);

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s, resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s));
        assertEquals(String.class, resultKey.get(s).getClass());
        assertEquals("aaa", resultKey.get(s));
    }

    /**
     * testIsPreBreak0003.
     */
    @Test
    public void testIsPreBreak0003() {
        Collector<HogeBean> collector = null;
        String[] s = null;

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsPreBreak0004.
     */
    @Test
    public void testIsPreBreak0004() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString" };

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s);

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s[0], resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s[0]));
        assertEquals(String.class, resultKey.get(s[0]).getClass());
        assertEquals("aaa", resultKey.get(s[0]));
    }

    /**
     * testIsPreBreak0005.
     */
    @Test
    public void testIsPreBreak0005() {
        Collector<HogeBean> collector = null;
        String[] s = null;

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsPreBreak0006.
     */
    @Test
    public void testIsPreBreak0006() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = null;

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsPreBreak0007.
     */
    @Test
    public void testIsPreBreak0007() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(null);
        List<String> s = Arrays.asList(new String[] { "hogeString" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNull(resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0008.
     */
    @Test
    public void testIsPreBreak0008() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(null);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(String.class, resultKey.get(s.get(0)).getClass());
        assertEquals("aaa", resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0009.
     */
    @Test
    public void testIsPreBreak0009() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeStringHoge" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsPreBreak0010.
     */
    @Test
    public void testIsPreBreak0010() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString(null);
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNull(resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0020.
     */
    @Test
    public void testIsPreBreak0020() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(String.class, resultKey.get(s.get(0)).getClass());
        assertEquals("aaa", resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0021.
     */
    @Test
    public void testIsPreBreak0021() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeBoolean(Boolean.FALSE);
        following.setHogeBoolean(Boolean.TRUE);
        previous.setHogeBoolean(Boolean.TRUE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeBoolean" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Boolean.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Boolean.FALSE, resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0022.
     */
    @Test
    public void testIsPreBreak0022() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeCharacter(Character.valueOf('a'));
        following.setHogeCharacter(Character.valueOf('b'));
        previous.setHogeCharacter(Character.valueOf('c'));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeCharacter" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Character.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Character.valueOf('a'), resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0023.
     */
    @Test
    public void testIsPreBreak0023() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeByte(Byte.valueOf((byte) 0));
        following.setHogeByte(Byte.valueOf((byte) 1));
        previous.setHogeByte(Byte.valueOf((byte) 2));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeByte" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Byte.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Byte.valueOf((byte) 0), resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0024.
     */
    @Test
    public void testIsPreBreak0024() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeShort(Short.valueOf((short) 0));
        following.setHogeShort(Short.valueOf((short) 1));
        previous.setHogeShort(Short.valueOf((short) 2));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeShort" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Short.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Short.valueOf((short) 0), resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0025.
     */
    @Test
    public void testIsPreBreak0025() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeInteger(Integer.valueOf(0));
        following.setHogeInteger(Integer.valueOf(1));
        previous.setHogeInteger(Integer.valueOf(2));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeInteger" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Integer.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Integer.valueOf(0), resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0026.
     */
    @Test
    public void testIsPreBreak0026() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeLong(Long.valueOf(0));
        following.setHogeLong(Long.valueOf(1));
        previous.setHogeLong(Long.valueOf(2));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeLong" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Long.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Long.valueOf(0), resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0027.
     */
    @Test
    public void testIsPreBreak0027() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeFloat(Float.valueOf("0.0"));
        following.setHogeFloat(Float.valueOf("1.0"));
        previous.setHogeFloat(Float.valueOf("2.0"));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeFloat" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Float.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Float.valueOf("0.0"), resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0028.
     */
    @Test
    public void testIsPreBreak0028() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeDouble(Double.valueOf("0.0"));
        following.setHogeDouble(Double.valueOf("1.0"));
        previous.setHogeDouble(Double.valueOf("2.0"));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeDouble" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Double.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Double.valueOf("0.0"), resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0029.
     */
    @Test
    public void testIsPreBreak0029() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeBigInteger(BigInteger.valueOf(0));
        following.setHogeBigInteger(BigInteger.valueOf(1));
        previous.setHogeBigInteger(BigInteger.valueOf(2));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeBigInteger" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(BigInteger.class, resultKey.get(s.get(0)).getClass());
        assertEquals(BigInteger.valueOf(0), resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0030.
     */
    @Test
    public void testIsPreBreak0030() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeBigDecimal(BigDecimal.valueOf(0));
        following.setHogeBigDecimal(BigDecimal.valueOf(1));
        previous.setHogeBigDecimal(BigDecimal.valueOf(2));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeBigDecimal" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(BigDecimal.class, resultKey.get(s.get(0)).getClass());
        assertEquals(BigDecimal.valueOf(0), resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0031.
     */
    @Test
    public void testIsPreBreak0031() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogePrimitiveInt(0);
        following.setHogePrimitiveInt(1);
        previous.setHogePrimitiveInt(2);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogePrimitiveInt" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Integer.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Integer.valueOf(0), resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0032.
     */
    @Test
    public void testIsPreBreak0032() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        HogeBean2 previousHogeHogeBean2 = new HogeBean2();
        HogeBean2 currentHogeHogeBean2 = new HogeBean2();
        HogeBean2 followingHogeHogeBean2 = new HogeBean2();
        previousHogeHogeBean2.setHogeString("aaa");
        currentHogeHogeBean2.setHogeString("aaa");
        followingHogeHogeBean2.setHogeString("aaa");
        current.setHogeHogeBean2(currentHogeHogeBean2);
        following.setHogeHogeBean2(followingHogeHogeBean2);
        previous.setHogeHogeBean2(previousHogeHogeBean2);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeHogeBean2" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
        // assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // // ----
        // assertNotNull(resultKey.get(s.get(0)));
        // assertEquals(StateObject.class, resultKey.get(s.get(0)).getClass());
        // StateObject tc = (StateObject) resultKey.get(s.get(0));
        // assertEquals(previousHogeHogeBean2, tc.previous());
        // assertEquals(currentHogeHogeBean2, tc.current());
        // assertEquals(followingHogeHogeBean2, tc.following());
    }

    /**
     * testIsPreBreak0033.
     */
    @Test
    public void testIsPreBreak0033() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        HogeBean2 previousHogeHogeBean2 = new HogeBean2();
        HogeBean2 currentHogeHogeBean2 = new HogeBean2();
        HogeBean2 followingHogeHogeBean2 = new HogeBean2();
        previousHogeHogeBean2.setHogeString("aaa");
        currentHogeHogeBean2.setHogeString("aab");
        followingHogeHogeBean2.setHogeString("aac");
        current.setHogeHogeBean2(currentHogeHogeBean2);
        following.setHogeHogeBean2(followingHogeHogeBean2);
        previous.setHogeHogeBean2(previousHogeHogeBean2);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeHogeBean2" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(HogeBean2.class, resultKey.get(s.get(0)).getClass());
        assertEquals(currentHogeHogeBean2, resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0034.
     */
    @Test
    public void testIsPreBreak0034() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        HogeBean2 previousHogeHogeBean2 = new HogeBean2();
        HogeBean2 currentHogeHogeBean2 = new HogeBean2();
        HogeBean2 followingHogeHogeBean2 = new HogeBean2();
        previousHogeHogeBean2.setHogeString("aaa");
        currentHogeHogeBean2.setHogeString("aab");
        followingHogeHogeBean2.setHogeString("aac");
        current.setHogeHogeBean2(currentHogeHogeBean2);
        following.setHogeHogeBean2(followingHogeHogeBean2);
        previous.setHogeHogeBean2(previousHogeHogeBean2);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays
                .asList(new String[] { "hogeHogeBean2.hogeString" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(String.class, resultKey.get(s.get(0)).getClass());
        assertEquals("aab", resultKey.get(s.get(0)));
    }

    /**
     * testIsPreBreak0041.
     */
    @Test
    public void testIsPreBreak0041() {
        Collector<HogeBean> collector = null;
        String s[] = null;
        // Comparator comp = new ComparatorStub();
        // Comparator[] comparators = new Comparator[] { comp, comp };

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsPreBreak0042.
     */
    @Test
    public void testIsPreBreak0042() {
        Collector<HogeBean> collector = null;
        String s[] = null;
        // Comparator comp = new ComparatorStub();
        // Comparator[] comparators = new Comparator[] { comp };

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsPreBreak0050.
     */
    @Test
    public void testIsPreBreak0050() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeBoolean(Boolean.TRUE);
        following.setHogeBoolean(Boolean.TRUE);
        previous.setHogeBoolean(Boolean.TRUE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });
        // Comparator comp = new ComparatorStub();
        // Comparator[] comparators = new Comparator[] { comp, comp };

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsPreBreak0051.
     */
    @Test
    public void testIsPreBreak0051() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        current.setHogeBoolean(Boolean.TRUE);
        following.setHogeBoolean(Boolean.FALSE);
        previous.setHogeBoolean(Boolean.FALSE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });
        // Comparator comp = new ComparatorStub();
        // Comparator[] comparators = new Comparator[] { comp, comp };

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(2, resultKey.size());
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(String.class, resultKey.get(s.get(0)).getClass());
        assertEquals("aaa", resultKey.get(s.get(0)));
        // ----
        assertNotNull(resultKey.get(s.get(1)));
        assertEquals(Boolean.class, resultKey.get(s.get(1)).getClass());
        assertEquals(Boolean.TRUE, resultKey.get(s.get(1)));
    }

    /**
     * testIsPreBreak0052.
     */
    @Test
    public void testIsPreBreak0052() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeBoolean(Boolean.TRUE);
        following.setHogeBoolean(Boolean.FALSE);
        previous.setHogeBoolean(Boolean.FALSE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });
        // Comparator comp = new ComparatorStub();
        // Comparator[] comparators = new Comparator[] { comp, comp };

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        // ----
        assertNotNull(resultKey.get(s.get(1)));
        assertEquals(Boolean.class, resultKey.get(s.get(1)).getClass());
        assertEquals(Boolean.TRUE, resultKey.get(s.get(1)));
    }

    /**
     * testIsPreBreak0053.
     */
    @Test
    public void testIsPreBreak0053() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeBoolean(Boolean.TRUE);
        following.setHogeBoolean(Boolean.FALSE);
        previous.setHogeBoolean(Boolean.FALSE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });
        // Comparator comp = new ComparatorStub();
        // Comparator[] comparators = new Comparator[] { comp };

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        // ----
        assertNotNull(resultKey.get(s.get(1)));
        assertEquals(Boolean.class, resultKey.get(s.get(1)).getClass());
        assertEquals(Boolean.TRUE, resultKey.get(s.get(1)));
    }

    /**
     * testIsPreBreak0054.
     */
    @Test
    public void testIsPreBreak0054() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeBoolean(Boolean.TRUE);
        following.setHogeBoolean(Boolean.TRUE);
        previous.setHogeBoolean(Boolean.TRUE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsPreBreak0055.
     */
    @Test
    public void testIsPreBreak0055() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aab");
        following.setHogeString("aac");
        previous.setHogeString("aaa");
        current.setHogeBoolean(Boolean.TRUE);
        following.setHogeBoolean(Boolean.TRUE);
        previous.setHogeBoolean(Boolean.TRUE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(2, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        assertEquals(s.get(1), resultKey.keySet().toArray()[1]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(String.class, resultKey.get(s.get(0)).getClass());
        assertEquals("aab", resultKey.get(s.get(0)));
        // ----
        assertNotNull(resultKey.get(s.get(1)));
        assertEquals(Boolean.class, resultKey.get(s.get(1)).getClass());
        assertEquals(Boolean.TRUE, resultKey.get(s.get(1)));
    }

    /**
     * testIsPreBreak0055.
     */
    @Test
    public void testIsPreBreak0056() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeBoolean(Boolean.FALSE);
        following.setHogeBoolean(Boolean.TRUE);
        previous.setHogeBoolean(Boolean.TRUE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(1), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(1)));
        assertEquals(Boolean.class, resultKey.get(s.get(1)).getClass());
        assertEquals(Boolean.FALSE, resultKey.get(s.get(1)));
    }

    /**
     * testIsPreBreak0057.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=0(null)<br>
     * compareStrategyの数=0(null)<br>
     */
    @Test
    public void testIsPreBreak0057() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = null;

        // compareStrategy生成。中身はnull
        CompareStrategy<?>[] compareStrategy = null;

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                compareStrategy, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, compareStrategy, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());

    }

    /**
     * testIsPreBreak0058.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=0(null)<br>
     * compareStrategyの数=1<br>
     */
    @Test
    public void testIsPreBreak0058() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = null;

        // compareStrategy生成。
        CompareStrategy<?> comp = new CompareStrategyStub1();
        CompareStrategy<?>[] compareStrategies = { comp };

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, compareStrategies, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsPreBreak0059.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=1<br>
     * compareStrategyの数=0<br>
     */
    @Test
    public void testIsPreBreak0059() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aab");
        following.setHogeString("aac");
        previous.setHogeString("aaa");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString" };

        // comparator生成

        CompareStrategy<?>[] compareStrategies = null;

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, compareStrategies, s);

        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        // ----
        assertNotNull(resultKey.get(s[0]));
        assertEquals(String.class, resultKey.get(s[0]).getClass());
        assertEquals("aab", resultKey.get(s[0]));
    }

    /**
     * testIsPreBreak0060.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=1<br>
     * compareStrategyの数=1<br>
     */
    @Test
    public void testIsPreBreak0060() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString" };

        // comparator生成
        CompareStrategy<?> comp1 = new CompareStrategyStub1();
        CompareStrategy<?>[] compareStrategies = { comp1 };

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, compareStrategies, s);

        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsPreBreak0061.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=1<br>
     * compareStrategyの数=2<br>
     */
    @Test
    public void testIsPreBreak0061() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aab");
        following.setHogeString("aac");
        previous.setHogeString("aaa");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString" };

        // comparator生成
        CompareStrategy<?> comp1 = new CompareStrategyStub1();
        CompareStrategy<?> comp2 = new CompareStrategyStub2();
        CompareStrategy<?>[] compareStrategies = { comp1, comp2 };

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, compareStrategies, s);

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        // ----
        assertNotNull(resultKey.get(s[0]));
        assertEquals(String.class, resultKey.get(s[0]).getClass());
        assertEquals("aab", resultKey.get(s[0]));
    }

    /**
     * testIsPreBreak0062.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=2<br>
     * compareStrategyの数=0<br>
     */
    @Test
    public void testIsPreBreak0062() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeInteger(1);
        following.setHogeInteger(1);
        previous.setHogeInteger(1);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString", "hogeInteger" };

        // comparator生成
        CompareStrategy<?>[] compareStrategies = null;

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, compareStrategies, s);

        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());

    }

    /**
     * testIsPreBreak0063.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=2<br>
     * compareStrategyの数=1<br>
     */
    @Test
    public void testIsPreBreak0063() {
        HogeBean3 current = new HogeBean3();
        HogeBean3 following = new HogeBean3();
        HogeBean3 previous = new HogeBean3();
        current.setHogeString("baa");
        following.setHogeString("aaa");
        previous.setHogeString("caa");
        current.setHogeString2("aab");
        following.setHogeString2("aaa");
        previous.setHogeString2("aac");
        CollectorStub<HogeBean3> collector = new CollectorStub<HogeBean3>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString", "hogeString2" };

        // comparator生成
        CompareStrategy<?> comp1 = new CompareStrategyStub2();
        CompareStrategy<?>[] compareStrategies = { comp1 };

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, compareStrategies, s);

        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        // --
        assertNotNull(resultKey.get(s[1]));
        assertEquals(String.class, resultKey.get(s[1]).getClass());
        assertEquals("aab", resultKey.get(s[1]));
    }

    /**
     * testIsPreBreak0064.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=2<br>
     * compareStrategyの数=2<br>
     */
    @Test
    public void testIsPreBreak0064() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeInteger(1);
        following.setHogeInteger(1);
        previous.setHogeInteger(1);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString", "hogeInteger" };

        // comparator生成。
        CompareStrategy<?> comp1 = new CompareStrategyStub2();
        CompareStrategy<?> comp2 = new CompareStrategyStub1();
        CompareStrategy<?>[] compareStrategies = { comp1, comp2 };

        // テスト
        boolean result = ControlBreakChecker.isPreBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getPreBreakKey(
                collector, compareStrategies, s);

        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsPreBreak0065.<br>
     * 使用するgetXxxが例外をスローした場合に例外がスローされることの確認。
     * Collector#getPreviousが例外をスローするケース
     */
    @Test
    public void testIsPreBreak0065() {
        HogeBean current = new HogeBean();
        current.setHogeString("aaa");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>() {
            @Override
            public HogeBean getPrevious() {
                throw new FileLineException("test");
            }
        };
        collector.setCurrent(current);
        String[] s = { "hogeString" };

        // テスト
        try {
            ControlBreakChecker.isPreBreak(collector, s);
            fail();
        } catch (FileLineException e) {
        }
        try {
            ControlBreakChecker.getPreBreakKey(collector, s);
            fail();
        } catch (FileLineException e) {
        }
    }

    /**
     * testIsPreBreak0066.<br>
     * 使用するgetXxxが例外をスローした場合に例外がスローされることの確認。
     * Collector#getCurrentが例外をスローするケース
     */
    @Test
    public void testIsPreBreak0066() {
        HogeBean previous = new HogeBean();
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>() {
            @Override
            public HogeBean getCurrent() {
                throw new FileLineException("test");
            }
        };
        collector.setPrevious(previous);
        String[] s = { "hogeString" };

        // テスト
        try {
            ControlBreakChecker.isPreBreak(collector, s);
            fail();
        } catch (FileLineException e) {
        }
        try {
            ControlBreakChecker.getPreBreakKey(collector, s);
            fail();
        } catch (FileLineException e) {
        }
    }

    /**
     * testIsBreak0001.
     */
    @Test
    public void testIsBreak0001() {
        Collector<HogeBean> collector = null;
        String s = null;

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreak0002.
     */
    @Test
    public void testIsBreak0002() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String s = "hogeString";

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s);

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s, resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s));
        assertEquals(String.class, resultKey.get(s).getClass());
        assertEquals("aaa", resultKey.get(s));
    }

    /**
     * testIsBreak0003.
     */
    @Test
    public void testIsBreak0003() {
        Collector<HogeBean> collector = null;
        String[] s = null;

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreak0004.
     */
    @Test
    public void testIsBreak0004() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString" };

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s);

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s[0], resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s[0]));
        assertEquals(String.class, resultKey.get(s[0]).getClass());
        assertEquals("aaa", resultKey.get(s[0]));
    }

    /**
     * testIsBreak0005.
     */
    @Test
    public void testIsBreak0005() {
        Collector<HogeBean> collector = null;
        String[] s = null;

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreak0006.
     */
    @Test
    public void testIsBreak0006() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = null;

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreak0007.
     */
    @Test
    public void testIsBreak0007() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(null);
        List<String> s = Arrays.asList(new String[] { "hogeString" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNull(resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0008.
     */
    @Test
    public void testIsBreak0008() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(null);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(String.class, resultKey.get(s.get(0)).getClass());
        assertEquals("aaa", resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0009.
     */
    @Test
    public void testIsBreak0009() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeStringHoge" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreak0010.
     */
    @Test
    public void testIsBreak0010() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString(null);
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
    }

    /**
     * testIsBreak0020.
     */
    @Test
    public void testIsBreak0020() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(String.class, resultKey.get(s.get(0)).getClass());
        assertEquals("aaa", resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0021.
     */
    @Test
    public void testIsBreak0021() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeBoolean(Boolean.TRUE);
        following.setHogeBoolean(Boolean.FALSE);
        previous.setHogeBoolean(Boolean.TRUE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeBoolean" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Boolean.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Boolean.TRUE, resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0022.
     */
    @Test
    public void testIsBreak0022() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeCharacter(Character.valueOf('a'));
        following.setHogeCharacter(Character.valueOf('b'));
        previous.setHogeCharacter(Character.valueOf('c'));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeCharacter" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Character.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Character.valueOf('a'), resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0023.
     */
    @Test
    public void testIsBreak0023() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeByte(Byte.valueOf((byte) 0));
        following.setHogeByte(Byte.valueOf((byte) 1));
        previous.setHogeByte(Byte.valueOf((byte) 2));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeByte" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Byte.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Byte.valueOf((byte) 0), resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0024.
     */
    @Test
    public void testIsBreak0024() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeShort(Short.valueOf((short) 0));
        following.setHogeShort(Short.valueOf((short) 1));
        previous.setHogeShort(Short.valueOf((short) 2));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeShort" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Short.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Short.valueOf((short) 0), resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0025.
     */
    @Test
    public void testIsBreak0025() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeInteger(Integer.valueOf(0));
        following.setHogeInteger(Integer.valueOf(1));
        previous.setHogeInteger(Integer.valueOf(2));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeInteger" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Integer.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Integer.valueOf(0), resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0026.
     */
    @Test
    public void testIsBreak0026() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeLong(Long.valueOf(0));
        following.setHogeLong(Long.valueOf(1));
        previous.setHogeLong(Long.valueOf(2));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeLong" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Long.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Long.valueOf(0), resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0027.
     */
    @Test
    public void testIsBreak0027() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeFloat(Float.valueOf("0.0"));
        following.setHogeFloat(Float.valueOf("1.0"));
        previous.setHogeFloat(Float.valueOf("2.0"));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeFloat" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Float.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Float.valueOf("0.0"), resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0028.
     */
    @Test
    public void testIsBreak0028() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeDouble(Double.valueOf("0.0"));
        following.setHogeDouble(Double.valueOf("1.0"));
        previous.setHogeDouble(Double.valueOf("2.0"));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeDouble" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Double.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Double.valueOf("0.0"), resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0029.
     */
    @Test
    public void testIsBreak0029() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeBigInteger(BigInteger.valueOf(0));
        following.setHogeBigInteger(BigInteger.valueOf(1));
        previous.setHogeBigInteger(BigInteger.valueOf(2));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeBigInteger" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(BigInteger.class, resultKey.get(s.get(0)).getClass());
        assertEquals(BigInteger.valueOf(0), resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0030.
     */
    @Test
    public void testIsBreak0030() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeBigDecimal(BigDecimal.valueOf(0));
        following.setHogeBigDecimal(BigDecimal.valueOf(1));
        previous.setHogeBigDecimal(BigDecimal.valueOf(2));
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeBigDecimal" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(BigDecimal.class, resultKey.get(s.get(0)).getClass());
        assertEquals(BigDecimal.valueOf(0), resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0031.
     */
    @Test
    public void testIsBreak0031() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogePrimitiveInt(0);
        following.setHogePrimitiveInt(1);
        previous.setHogePrimitiveInt(2);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogePrimitiveInt" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(Integer.class, resultKey.get(s.get(0)).getClass());
        assertEquals(Integer.valueOf(0), resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0032.
     */
    @Test
    public void testIsBreak0032() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        HogeBean2 previousHogeHogeBean2 = new HogeBean2();
        HogeBean2 currentHogeHogeBean2 = new HogeBean2();
        HogeBean2 followingHogeHogeBean2 = new HogeBean2();
        previousHogeHogeBean2.setHogeString("aaa");
        currentHogeHogeBean2.setHogeString("aaa");
        followingHogeHogeBean2.setHogeString("aaa");
        current.setHogeHogeBean2(currentHogeHogeBean2);
        following.setHogeHogeBean2(followingHogeHogeBean2);
        previous.setHogeHogeBean2(previousHogeHogeBean2);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeHogeBean2" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
        // assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // // ----
        // assertNotNull(resultKey.get(s.get(0)));
        // assertEquals(StateObject.class, resultKey.get(s.get(0)).getClass());
        // StateObject tc = (StateObject) resultKey.get(s.get(0));
        // assertEquals(previousHogeHogeBean2, tc.previous());
        // assertEquals(currentHogeHogeBean2, tc.current());
        // assertEquals(followingHogeHogeBean2, tc.following());
    }

    /**
     * testIsBreak0033.
     */
    @Test
    public void testIsBreak0033() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        HogeBean2 previousHogeHogeBean2 = new HogeBean2();
        HogeBean2 currentHogeHogeBean2 = new HogeBean2();
        HogeBean2 followingHogeHogeBean2 = new HogeBean2();
        previousHogeHogeBean2.setHogeString("aaa");
        currentHogeHogeBean2.setHogeString("aab");
        followingHogeHogeBean2.setHogeString("aac");
        current.setHogeHogeBean2(currentHogeHogeBean2);
        following.setHogeHogeBean2(followingHogeHogeBean2);
        previous.setHogeHogeBean2(previousHogeHogeBean2);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeHogeBean2" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(HogeBean2.class, resultKey.get(s.get(0)).getClass());
        assertEquals(currentHogeHogeBean2, resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0034.
     */
    @Test
    public void testIsBreak0034() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        HogeBean2 previousHogeHogeBean2 = new HogeBean2();
        HogeBean2 currentHogeHogeBean2 = new HogeBean2();
        HogeBean2 followingHogeHogeBean2 = new HogeBean2();
        previousHogeHogeBean2.setHogeString("aaa");
        currentHogeHogeBean2.setHogeString("aab");
        followingHogeHogeBean2.setHogeString("aac");
        current.setHogeHogeBean2(currentHogeHogeBean2);
        following.setHogeHogeBean2(followingHogeHogeBean2);
        previous.setHogeHogeBean2(previousHogeHogeBean2);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays
                .asList(new String[] { "hogeHogeBean2.hogeString" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(String.class, resultKey.get(s.get(0)).getClass());
        assertEquals("aab", resultKey.get(s.get(0)));
    }

    /**
     * testIsBreak0041.
     */
    @Test
    public void testIsBreak0041() {
        Collector<HogeBean> collector = null;
        String s[] = null;
        // Comparator comp = new ComparatorStub();
        // Comparator[] comparators = new Comparator[] { comp, comp };

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreak0050.
     */
    @Test
    public void testIsBreak0050() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeBoolean(Boolean.TRUE);
        following.setHogeBoolean(Boolean.TRUE);
        previous.setHogeBoolean(Boolean.TRUE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });
        // Comparator comp = new ComparatorStub();
        // Comparator[] comparators = new Comparator[] { comp, comp };

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreak0051.
     */
    @Test
    public void testIsBreak0051() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        current.setHogeBoolean(Boolean.TRUE);
        following.setHogeBoolean(Boolean.FALSE);
        previous.setHogeBoolean(Boolean.FALSE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });
        // Comparator comp = new ComparatorStub();
        // Comparator[] comparators = new Comparator[] { comp, comp };

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(2, resultKey.size());
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(String.class, resultKey.get(s.get(0)).getClass());
        assertEquals("aaa", resultKey.get(s.get(0)));
        // ----
        assertNotNull(resultKey.get(s.get(1)));
        assertEquals(Boolean.class, resultKey.get(s.get(1)).getClass());
        assertEquals(Boolean.TRUE, resultKey.get(s.get(1)));
    }

    /**
     * testIsBreak0052.
     */
    @Test
    public void testIsBreak0052() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeBoolean(Boolean.TRUE);
        following.setHogeBoolean(Boolean.FALSE);
        previous.setHogeBoolean(Boolean.FALSE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });
        // Comparator comp = new ComparatorStub();
        // Comparator[] comparators = new Comparator[] { comp, comp };

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        // ----
        assertNotNull(resultKey.get(s.get(1)));
        assertEquals(Boolean.class, resultKey.get(s.get(1)).getClass());
        assertEquals(Boolean.TRUE, resultKey.get(s.get(1)));
    }

    /**
     * testIsBreak0053.
     */
    @Test
    public void testIsBreak0053() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeBoolean(Boolean.TRUE);
        following.setHogeBoolean(Boolean.FALSE);
        previous.setHogeBoolean(Boolean.FALSE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });
        // Comparator comp = new ComparatorStub();
        // Comparator[] comparators = new Comparator[] { comp };

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        // ----
        assertNotNull(resultKey.get(s.get(1)));
        assertEquals(Boolean.class, resultKey.get(s.get(1)).getClass());
        assertEquals(Boolean.TRUE, resultKey.get(s.get(1)));
    }

    /**
     * testIsBreak0054.
     */
    @Test
    public void testIsBreak0054() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeBoolean(Boolean.TRUE);
        following.setHogeBoolean(Boolean.TRUE);
        previous.setHogeBoolean(Boolean.TRUE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreak0055.
     */
    @Test
    public void testIsBreak0055() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aab");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeBoolean(Boolean.TRUE);
        following.setHogeBoolean(Boolean.TRUE);
        previous.setHogeBoolean(Boolean.TRUE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(2, resultKey.size());
        assertEquals(s.get(0), resultKey.keySet().toArray()[0]);
        assertEquals(s.get(1), resultKey.keySet().toArray()[1]);
        // ----
        assertNotNull(resultKey.get(s.get(0)));
        assertEquals(String.class, resultKey.get(s.get(0)).getClass());
        assertEquals("aab", resultKey.get(s.get(0)));
        // ----
        assertNotNull(resultKey.get(s.get(1)));
        assertEquals(Boolean.class, resultKey.get(s.get(1)).getClass());
        assertEquals(Boolean.TRUE, resultKey.get(s.get(1)));
    }

    /**
     * testIsBreak0056.
     */
    @Test
    public void testIsBreak0056() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeBoolean(Boolean.FALSE);
        following.setHogeBoolean(Boolean.TRUE);
        previous.setHogeBoolean(Boolean.TRUE);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        List<String> s = Arrays.asList(new String[] { "hogeString",
                "hogeBoolean" });

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                s.toArray(new String[0]));
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, s.toArray(new String[0]));

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(s.get(1), resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(s.get(1)));
        assertEquals(Boolean.class, resultKey.get(s.get(1)).getClass());
        assertEquals(Boolean.FALSE, resultKey.get(s.get(1)));
    }

    /**
     * testIsBreak0057.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=0(null)<br>
     * compareStrategyの数=0(null)<br>
     */
    @Test
    public void testIsBreak0057() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = null;

        // comparator生成。中身はnull
        CompareStrategy<?>[] compareStrategies = null;

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, compareStrategies, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreak0058.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=0(null)<br>
     * compareStrategyの数=1<br>
     */
    @Test
    public void testIsBreak0058() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aab");
        previous.setHogeString("aac");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = null;

        // comparator生成。
        CompareStrategy<?> comp = new CompareStrategyStub2();
        CompareStrategy<?>[] compareStrategies = { comp };

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, compareStrategies, s);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());

    }

    /**
     * testIsBreak0059.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=1<br>
     * compareStrategyの数=0<br>
     */
    @Test
    public void testIsBreak0059() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString" };

        // comparator生成
        CompareStrategy<?>[] compareStrategies = null;

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, compareStrategies, s);

        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());

    }

    /**
     * testIsPreBreak0060.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=1<br>
     * compareStrategyの数=1<br>
     */
    @Test
    public void testIsBreak0060() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aab");
        following.setHogeString("aac");
        previous.setHogeString("aaa");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString" };

        // comparator生成
        CompareStrategy<?> comp1 = new CompareStrategyStub1();
        CompareStrategy<?>[] compareStrategies = { comp1 };

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, compareStrategies, s);

        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        // ----
        assertNotNull(resultKey.get(s[0]));
        assertEquals(String.class, resultKey.get(s[0]).getClass());
        assertEquals("aab", resultKey.get(s[0]));
    }

    /**
     * testIsPreBreak0061.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=1<br>
     * compareStrategyの数=2<br>
     */
    @Test
    public void testIsBreak0061() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString" };

        // comparator生成
        CompareStrategy<?> comp1 = new CompareStrategyStub1();
        CompareStrategy<?> comp2 = new CompareStrategyStub1();
        CompareStrategy<?>[] compareStrategies = { comp1, comp2 };

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, compareStrategies, s);

        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsPreBreak0062.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=2<br>
     * compareStrategyの数=0<br>
     */
    @Test
    public void testIsBreak0062() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("aaa");
        previous.setHogeString("aaa");
        current.setHogeInteger(1);
        following.setHogeInteger(1);
        previous.setHogeInteger(1);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString", "hogeInteger" };

        // comparator生成
        CompareStrategy<?>[] compareStrategies = null;

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, compareStrategies, s);

        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());

    }

    /**
     * testIsPreBreak0063.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=2<br>
     * compareStrategyの数=1<br>
     */
    @Test
    public void testIsBreak0063() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aab");
        following.setHogeString("aac");
        previous.setHogeString("aaa");
        previous.setHogeInteger(1);
        current.setHogeInteger(2);
        following.setHogeInteger(3);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString", "hogeInteger" };

        // comparator生成
        CompareStrategy<?> comp1 = new CompareStrategyStub2();
        CompareStrategy<?>[] compareStrategies = { comp1 };

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, compareStrategies, s);

        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(2, resultKey.size());
        // ----
        assertNotNull(resultKey.get(s[0]));
        assertEquals(String.class, resultKey.get(s[0]).getClass());
        assertEquals("aab", resultKey.get(s[0]));
        // ----
        assertNotNull(resultKey.get(s[1]));
        assertEquals(Integer.class, resultKey.get(s[1]).getClass());
        assertEquals(2, resultKey.get(s[1]));
    }

    /**
     * testIsPreBreak0064.<br>
     * compareStrategyを作成して試験を行う<br>
     * breakkeyの数=2<br>
     * compareStrategyの数=2<br>
     */
    @Test
    public void testIsBreak0064() {
        HogeBean current = new HogeBean();
        HogeBean following = new HogeBean();
        HogeBean previous = new HogeBean();
        current.setHogeString("aaa");
        following.setHogeString("baa");
        previous.setHogeString("caa");
        current.setHogeInteger(1);
        following.setHogeInteger(1);
        previous.setHogeInteger(1);
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>();
        collector.setFollowing(following);
        collector.setPrevious(previous);
        collector.setCurrent(current);
        String[] s = { "hogeString", "hogeInteger" };

        // comparator生成。
        CompareStrategy<?> comp1 = new CompareStrategyStub2();
        CompareStrategy<?> comp2 = new CompareStrategyStub1();
        CompareStrategy<?>[] compareStrategies = { comp1, comp2 };

        // テスト
        boolean result = ControlBreakChecker.isBreak(collector,
                compareStrategies, s);
        Map<String, Object> resultKey = ControlBreakChecker.getBreakKey(
                collector, compareStrategies, s);

        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreak0065.
     * 使用するgetXxxが例外をスローした場合に例外がスローされることの確認。
     * Collector#getCurrentが例外をスローするケース
     */
    @Test
    public void testIsBreak0065() {
        HogeBean following = new HogeBean();
        following.setHogeString("aab");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>() {
            @Override
            public HogeBean getCurrent() {
                throw new FileLineException("test");
            }
        };
        collector.setFollowing(following);
        String[] s = { "hogeString" };

        // テスト
        try {
            ControlBreakChecker.isBreak(collector, s);
            fail();
        } catch (FileLineException e) {
        }
        try {
            ControlBreakChecker.getBreakKey(collector, s);
            fail();
        } catch (FileLineException e) {
        }
    }

    /**
     * testIsBreak0066.
     * 使用するgetXxxが例外をスローした場合に例外がスローされることの確認。
     * Collector#getNextが例外をスローするケース
     */
    @Test
    public void testIsBreak0066() {
        HogeBean current = new HogeBean();
        current.setHogeString("aaa");
        CollectorStub<HogeBean> collector = new CollectorStub<HogeBean>() {
            @Override
            public HogeBean getNext() {
                throw new FileLineException("test");
            }
        };
        collector.setCurrent(current);
        String[] s = { "hogeString" };

        // テスト
        try {
            ControlBreakChecker.isBreak(collector, s);
            fail();
        } catch (FileLineException e) {
        }
        try {
            ControlBreakChecker.getBreakKey(collector, s);
            fail();
        } catch (FileLineException e) {
        }
    }

    /**
     * testIsBreakInternal0001.
     */
    @Test
    public void testIsBreakInternal0001() {
        Object current = null;
        Object other = null;
        Comparator<?>[] comparators = null;
        String keys = null;

        // テスト
        boolean result = ControlBreakChecker.isBreakInternal(current, other,
                comparators, keys);
        Map<String, Object> resultKey = ControlBreakChecker
                .getBreakKeyInternal(current, other, comparators, keys);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreakInternal0002.
     */
    @Test
    public void testIsBreakInternal0002() {
        Object current = new HogeBean();
        Object other = null;
        Comparator<?>[] comparators = null;
        String keys = "hogeString";

        // テスト
        boolean result = ControlBreakChecker.isBreakInternal(current, other,
                comparators, keys);
        Map<String, Object> resultKey = ControlBreakChecker
                .getBreakKeyInternal(current, other, comparators, keys);

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(keys, resultKey.keySet().toArray()[0]);
        // ----
        assertNull(resultKey.get(keys));
    }

    /**
     * testIsBreakInternal0003.
     */
    @Test
    public void testIsBreakInternal0003() {
        Object current = new HogeBean();
        Object other = new Object();
        Comparator<?>[] comparators = null;
        String keys = "hogeString";

        // テスト
        boolean result = ControlBreakChecker.isBreakInternal(current, other,
                comparators, keys);
        Map<String, Object> resultKey = ControlBreakChecker
                .getBreakKeyInternal(current, other, comparators, keys);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreakInternal0004.
     */
    @Test
    public void testIsBreakInternal0004() {
        Object current = new HogeBean();
        Object other = new HogeBean();
        Comparator<?> comp1 = new ComparatorStub2();
        Comparator<?>[] comparators = new Comparator[] { comp1 };
        String keys = "hogeString";

        // テスト
        boolean result = ControlBreakChecker.isBreakInternal(current, other,
                comparators, keys);
        Map<String, Object> resultKey = ControlBreakChecker
                .getBreakKeyInternal(current, other, comparators, keys);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreakInternal0005.
     */
    @Test
    public void testIsBreakInternal0005() {
        HogeBean current = new HogeBean();
        HogeBean other = new HogeBean();
        current.setHogeString("hoge1");
        other.setHogeString("hoge2");
        Comparator<?> comp1 = new ComparatorStub2();
        Comparator<?>[] comparators = new Comparator[] { comp1 };
        String keys = "hogeString";

        // テスト
        boolean result = ControlBreakChecker.isBreakInternal(current, other,
                comparators, keys);
        Map<String, Object> resultKey = ControlBreakChecker
                .getBreakKeyInternal(current, other, comparators, keys);

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(keys, resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(keys));
        assertEquals("hoge1", resultKey.get(keys));
    }

    /**
     * testIsBreakInternal0006.
     */
    @Test
    public void testIsBreakInternal0006() {
        HogeBean current = new HogeBean();
        HogeBean other = new HogeBean();
        current.setHogeString("hoge1");
        other.setHogeString("hoge2");
        Comparator<?> comp1 = new ComparatorStub2();
        Comparator<?> comp2 = new ComparatorStub2();
        Comparator<?>[] comparators = new Comparator[] { comp1, comp2 };
        String keys = "hogeString";

        // テスト
        boolean result = ControlBreakChecker.isBreakInternal(current, other,
                comparators, keys);
        Map<String, Object> resultKey = ControlBreakChecker
                .getBreakKeyInternal(current, other, comparators, keys);

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(keys, resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(keys));
        assertEquals("hoge1", resultKey.get(keys));
    }

    /**
     * testIsBreakInternal0007. <br>
     * compareStrategyを渡す
     */
    @Test
    public void testIsBreakInternal0007() {
        Object current = null;
        Object other = null;
        CompareStrategy<?>[] compareStrategies = null;
        String keys = null;

        // テスト
        boolean result = ControlBreakChecker.isBreakInternal(current, other,
                compareStrategies, keys);
        Map<String, Object> resultKey = ControlBreakChecker
                .getBreakKeyInternal(current, other, compareStrategies, keys);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreakInternal0008. <br>
     * compareStrategyを渡す
     */
    @Test
    public void testIsBreakInternal0008() {
        Object current = new HogeBean();
        Object other = null;
        CompareStrategy<?>[] compareStrategies = null;
        String keys = null;

        // テスト
        boolean result = ControlBreakChecker.isBreakInternal(current, other,
                compareStrategies, keys);
        Map<String, Object> resultKey = ControlBreakChecker
                .getBreakKeyInternal(current, other, compareStrategies, keys);

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
        // ----
        assertNull(resultKey.get(keys));
    }

    /**
     * testIsBreakInternal0003.
     */
    @Test
    public void testIsBreakInternal0009() {
        Object current = new HogeBean();
        Object other = new Object();
        CompareStrategy<?>[] compareStrategies = null;
        String keys = "hogeString";

        // テスト
        boolean result = ControlBreakChecker.isBreakInternal(current, other,
                compareStrategies, keys);
        Map<String, Object> resultKey = ControlBreakChecker
                .getBreakKeyInternal(current, other, compareStrategies, keys);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreakInternal0004.
     */
    @Test
    public void testIsBreakInternal0010() {
        Object current = new HogeBean();
        Object other = new HogeBean();
        CompareStrategy<?> comp1 = new CompareStrategyStub1();
        CompareStrategy<?>[] compareStrategies = new CompareStrategy[] { comp1 };
        String keys = "hogeString";

        // テスト
        boolean result = ControlBreakChecker.isBreakInternal(current, other,
                compareStrategies, keys);
        Map<String, Object> resultKey = ControlBreakChecker
                .getBreakKeyInternal(current, other, compareStrategies, keys);

        // 検証
        assertFalse(result);
        assertNotNull(resultKey);
        assertEquals(0, resultKey.size());
    }

    /**
     * testIsBreakInternal0005.
     */
    @Test
    public void testIsBreakInternal0011() {
        HogeBean current = new HogeBean();
        HogeBean other = new HogeBean();
        current.setHogeString("hoge1");
        other.setHogeString("hoge2");
        CompareStrategy<?> comp1 = new CompareStrategyStub1();
        CompareStrategy<?>[] compareStrategies = new CompareStrategy[] { comp1 };
        String keys = "hogeString";

        // テスト
        boolean result = ControlBreakChecker.isBreakInternal(current, other,
                compareStrategies, keys);
        Map<String, Object> resultKey = ControlBreakChecker
                .getBreakKeyInternal(current, other, compareStrategies, keys);

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(keys, resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(keys));
        assertEquals("hoge1", resultKey.get(keys));
    }

    /**
     * testIsBreakInternal0006.
     */
    @Test
    public void testIsBreakInternal0012() {
        HogeBean current = new HogeBean();
        HogeBean other = new HogeBean();
        current.setHogeString("hoge1");
        other.setHogeString("hoge2");
        CompareStrategy<?> comp1 = new CompareStrategyStub1();
        CompareStrategy<?> comp2 = new CompareStrategyStub1();
        CompareStrategy<?>[] compareStrategies = new CompareStrategy[] { comp1,
                comp2 };
        String keys = "hogeString";

        // テスト
        boolean result = ControlBreakChecker.isBreakInternal(current, other,
                compareStrategies, keys);
        Map<String, Object> resultKey = ControlBreakChecker
                .getBreakKeyInternal(current, other, compareStrategies, keys);

        // 検証
        assertTrue(result);
        assertNotNull(resultKey);
        assertEquals(1, resultKey.size());
        assertEquals(keys, resultKey.keySet().toArray()[0]);
        // ----
        assertNotNull(resultKey.get(keys));
        assertEquals("hoge1", resultKey.get(keys));
    }

    /**
     * testEqualsObjects0001.
     */
    @Test
    public void testEqualsObjects0001() {
        Object value1 = null;
        Object value2 = null;

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertTrue(result);
    }

    /**
     * testEqualsObjects0002.
     */
    @Test
    public void testEqualsObjects0002() {
        Object value1 = "hoge";
        Object value2 = null;

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0003.
     */
    @Test
    public void testEqualsObjects0003() {
        Object value1 = null;
        Object value2 = "hoge";

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0004.
     */
    @Test
    public void testEqualsObjects0004() {
        Object value1 = "hoge";
        Object value2 = "hoge";

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertTrue(result);
    }

    /**
     * testEqualsObjects0005.
     */
    @Test
    public void testEqualsObjects0005() {
        Object value1 = "hoge";
        Object value2 = "hoga";

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0006.
     */
    @Test
    public void testEqualsObjects0006() {
        Object value1 = Integer.valueOf(3);
        Object value2 = null;

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0007.
     */
    @Test
    public void testEqualsObjects0007() {
        Object value1 = null;
        Object value2 = Integer.valueOf(3);

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0008.
     */
    @Test
    public void testEqualsObjects0008() {
        Object value1 = Integer.valueOf(3);
        Object value2 = Integer.valueOf(3);

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertTrue(result);
    }

    /**
     * testEqualsObjects0009.
     */
    @Test
    public void testEqualsObjects0009() {
        Object value1 = Integer.valueOf(3);
        Object value2 = Integer.valueOf(4);

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0010.
     */
    @Test
    public void testEqualsObjects0010() {
        Object value1 = new AtomicInteger(3);
        Object value2 = null;

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0011.
     */
    @Test
    public void testEqualsObjects0011() {
        Object value1 = null;
        Object value2 = new AtomicInteger(3);

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0012.
     */
    @Test
    public void testEqualsObjects0012() {
        Object value1 = new AtomicInteger(3);
        Object value2 = new AtomicInteger(3);

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertTrue(result);
    }

    /**
     * testEqualsObjects0013.
     */
    @Test
    public void testEqualsObjects0013() {
        Object value1 = new AtomicInteger(3);
        Object value2 = new AtomicInteger(4);

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0014.
     */
    @Test
    public void testEqualsObjects0014() {
        Object value1 = new AtomicLong(3);
        Object value2 = null;

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0015.
     */
    @Test
    public void testEqualsObjects0015() {
        Object value1 = null;
        Object value2 = new AtomicLong(3);

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0016.
     */
    @Test
    public void testEqualsObjects0016() {
        Object value1 = new AtomicLong(3);
        Object value2 = new AtomicLong(3);

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertTrue(result);
    }

    /**
     * testEqualsObjects0017.
     */
    @Test
    public void testEqualsObjects0017() {
        Object value1 = new AtomicLong(3);
        Object value2 = new AtomicLong(4);

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0018.
     */
    @Test
    public void testEqualsObjects0018() {
        Object value1 = new AtomicBoolean(true);
        Object value2 = null;

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0019.
     */
    @Test
    public void testEqualsObjects0019() {
        Object value1 = null;
        Object value2 = new AtomicBoolean(true);

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0020.
     */
    @Test
    public void testEqualsObjects0020() {
        Object value1 = new AtomicBoolean(true);
        Object value2 = new AtomicBoolean(true);

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertTrue(result);
    }

    /**
     * testEqualsObjects0021.
     */
    @Test
    public void testEqualsObjects0021() {
        Object value1 = new AtomicBoolean(true);
        Object value2 = new AtomicBoolean(false);

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0022.<br>
     * java.sql.Date型の比較テスト<br>
     */
    @Test
    public void testEqualsObjects0022() {

        Calendar cal = Calendar.getInstance();

        java.sql.Date value1 = null;
        java.sql.Date value2 = new java.sql.Date(cal.getTimeInMillis());

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0023.<br>
     * java.sql.Date型の比較テスト<br>
     */
    @Test
    public void testEqualsObjects0023() {

        Calendar cal = Calendar.getInstance();

        java.sql.Date value1 = new java.sql.Date(cal.getTimeInMillis());
        java.sql.Date value2 = null;

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0024.<br>
     * java.sql.Date型の比較テスト<br>
     */
    @Test
    public void testEqualsObjects0024() {

        Calendar cal = Calendar.getInstance();

        java.sql.Date value1 = new java.sql.Date(cal.getTimeInMillis());
        java.sql.Date value2 = new java.sql.Date(cal.getTimeInMillis());

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertTrue(result);
    }

    /**
     * testEqualsObjects0025.<br>
     * java.sql.Date型の比較テスト<br>
     */
    @Test
    public void testEqualsObjects0025() {

        Calendar cal = Calendar.getInstance();

        java.sql.Date value1 = new java.sql.Date(cal.getTimeInMillis());

        cal.add(Calendar.DATE, -1);
        java.sql.Date value2 = new java.sql.Date(cal.getTimeInMillis());

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0026.<br>
     * Class型の比較テスト<br>
     */
    @Test
    public void testEqualsObjects0026() {

        Class value1 = null;
        Class value2 = String.class;

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0027.<br>
     * Class型の比較テスト<br>
     */
    @Test
    public void testEqualsObjects0027() {

        Class value1 = String.class;
        Class value2 = null;

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0028.<br>
     * Class型の比較テスト<br>
     */
    @Test
    public void testEqualsObjects0028() {

        Class value1 = String.class;
        Class value2 = String.class;

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertTrue(result);
    }

    /**
     * testEqualsObjects0029.<br>
     * Class型の比較テスト<br>
     */
    @Test
    public void testEqualsObjects0029() {

        Class value1 = String.class;
        Class value2 = Integer.class;

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0030.<br>
     * CompareStrategyを考慮した比較テスト<br>
     * java.util.Date型を使用<br>
     * Hourの精度で比較するCompareStrategy無し<br>
     * Hourの値まで同じ(Minuteは1分違い)
     */
    @Test
    public void testEqualsObjects0030() {

        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        Date value1 = new Date(cal.getTimeInMillis());

        cal.add(Calendar.MINUTE, 1);
        Date value2 = new Date(cal.getTimeInMillis());

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, null);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0031.<br>
     * CompareStrategyを考慮した比較テスト<br>
     * java.util.Date型を使用<br>
     * Hourの精度で比較するCompareStrategy無し(引数無し)<br>
     * Hourの値まで同じ(Minuteは1分違い)
     */
    @Test
    public void testEqualsObjects0031() {

        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        Date value1 = new Date(cal.getTimeInMillis());

        cal.add(Calendar.MINUTE, 1);
        Date value2 = new Date(cal.getTimeInMillis());

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2);

        // 検証
        assertFalse(result);
    }

    /**
     * testEqualsObjects0032.<br>
     * CompareStrategyを考慮した比較テスト<br>
     * java.util.Date型を使用<br>
     * Hourの精度で比較するCompareStrategyあり<br>
     * Hourの値まで同じ(Minuteは1分違い)
     */
    @Test
    public void testEqualsObjects0032() {

        CompareStrategyStub3 strategy = new CompareStrategyStub3();
        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        Date value1 = new Date(cal.getTimeInMillis());

        cal.add(Calendar.MINUTE, 1);
        Date value2 = new Date(cal.getTimeInMillis());

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, strategy);

        // 検証
        assertTrue(result);
    }

    /**
     * testEqualsObjects0033.<br>
     * CompareStrategyを考慮した比較テスト<br>
     * java.util.Date型を使用<br>
     * Hourの精度で比較するCompareStrategyあり<br>
     * Hourの値が異なる(Minuteは1分違い)
     */
    @Test
    public void testEqualsObjects0033() {

        CompareStrategyStub3 strategy = new CompareStrategyStub3();
        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        Date value1 = new Date(cal.getTimeInMillis());

        cal.add(Calendar.MINUTE, -1); // 繰り下がりでHourの値が変わる
        Date value2 = new Date(cal.getTimeInMillis());

        // テスト
        boolean result = ControlBreakChecker.equalsObjects(value1, value2, strategy);

        // 検証
        assertFalse(result);
    }
}
