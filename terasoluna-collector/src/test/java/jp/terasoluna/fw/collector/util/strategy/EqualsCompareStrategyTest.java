package jp.terasoluna.fw.collector.util.strategy;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EqualsCompareStrategyTest {

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

    @Test
    public void testEqualsObjects0001() {
        // compareToで比較すると0を返すが
        // equalsで比較するとfalseを返す組み合わせ。
        BigDecimal decimal1 = new BigDecimal("1.00");
        BigDecimal decimal2 = new BigDecimal("1.0");
        EqualsCompareStrategy strategy = new EqualsCompareStrategy();

        boolean ret = strategy.equalsObjects(decimal1, decimal2);

        assertFalse(ret);
    }

    @Test
    public void testEqualsObjects0002() {
        // equalsで比較するとtrueを返す組み合わせ。
        BigDecimal decimal1 = new BigDecimal("1.00");
        BigDecimal decimal2 = new BigDecimal("1.00");
        EqualsCompareStrategy strategy = new EqualsCompareStrategy();

        boolean ret = strategy.equalsObjects(decimal1, decimal2);

        assertTrue(ret);
    }

}
