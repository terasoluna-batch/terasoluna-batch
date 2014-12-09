package jp.terasoluna.fw.collector.util.strategy;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ComparatorCompareStrategyTest {

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
        Object obj1 = new Object();
        Object obj2 = new Object();
        ComparatorStub1 comparator = new ComparatorStub1();
        comparator.returnValue = 1;
        ComparatorCompareStrategy strategy = new ComparatorCompareStrategy(comparator);

        boolean ret = strategy.equalsObjects(obj1, obj2);

        assertFalse(ret);
        assertSame(obj1, comparator.obj1);
        assertSame(obj2, comparator.obj2);
    }

    @Test
    public void testEqualsObjects0002() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        ComparatorStub1 comparator = new ComparatorStub1();
        comparator.returnValue = 0;
        ComparatorCompareStrategy strategy = new ComparatorCompareStrategy(comparator);

        boolean ret = strategy.equalsObjects(obj1, obj2);

        assertTrue(ret);
        assertSame(obj1, comparator.obj1);
        assertSame(obj2, comparator.obj2);
    }

}
