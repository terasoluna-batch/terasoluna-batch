package jp.terasoluna.fw.collector;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandlerStatus;
import jp.terasoluna.fw.collector.file.SkipValidationErrorHandler;
import jp.terasoluna.fw.collector.validate.ExceptionValidationErrorHandler;
import jp.terasoluna.fw.collector.validate.ValidateErrorStatus;
import jp.terasoluna.fw.collector.validate.ValidationErrorException;
import jp.terasoluna.fw.collector.validate.ValidationErrorHandler;
import jp.terasoluna.fw.collector.vo.DataValueObject;
import jp.terasoluna.fw.exception.SystemException;
import jp.terasoluna.fw.file.dao.FileLineException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AbstractCollector003Test {

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
     * データが0件の場合のテスト。
     * 入力データ：[]
     * 出力データ：[]
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testNormal001() throws Exception {
        int dataNum = 0;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素が無いことを確認
        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertNull(collector.getPrevious());
        assertNull(collector.getCurrent());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データが1件の場合
     * 入力データ：[1]
     * 出力データ：[1]
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testNormal002() throws Exception {
        int dataNum = 1;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データが3件の場合
     * 入力データ：[1, 2, 3]
     * 出力データ：[1, 2, 3]
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testNormal003() throws Exception {
        int dataNum = 3;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertTrue(collector.hasNext());
        assertEquals("3", collector.next().getHoge());
        assertEquals("2", collector.getPrevious().getHoge());
        assertEquals("3", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("2", collector.getPrevious().getHoge());
        assertEquals("3", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データのbeanとしてnullが含まれる場合
     * 入力データ：[null, 2, 3, 4, 5, 6]
     * 出力データ：[null, 2, 3, 4, 5, 6]
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testContainsNull001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Arrays.asList(1);
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertNull(collector.next());
        assertNull(collector.getPrevious());
        assertNull(collector.getCurrent());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("2", collector.getCurrent().getHoge());
        assertEquals("3", collector.getNext().getHoge());

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データのbeanとしてnullが含まれる場合
     * 入力データ：[1, 2, null, 4, 5, 6]
     * 出力データ：[1, 2, null, 4, 5, 6]
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testContainsNull002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Arrays.asList(3);
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        assertEquals("1", collector.getPrevious().getHoge());
        assertEquals("2", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        // 第3要素
        assertTrue(collector.hasNext());
        assertNull(collector.next());
        assertEquals("2", collector.getPrevious().getHoge());
        assertNull(collector.getCurrent());
        assertEquals("4", collector.getNext().getHoge());

        // 第4要素
        assertTrue(collector.hasNext());
        assertEquals("4", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("4", collector.getCurrent().getHoge());
        assertEquals("5", collector.getNext().getHoge());

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データのbeanとしてnullが含まれる場合
     * 入力データ：[1, 2, 3, 4, 5, null]
     * 出力データ：[1, 2, 3, 4, 5, null]
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testContainsNull003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Arrays.asList(6);
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("5", collector.next().getHoge());
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        // 第6要素
        assertTrue(collector.hasNext());
        assertNull(collector.next());
        assertEquals("5", collector.getPrevious().getHoge());
        assertNull(collector.getCurrent());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertNull(collector.getCurrent());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1(入力チェックエラー), 2, 3, 4, 5, 6]
     * ValidationErrorHandlerの返却値：ValidateErrorStatus.CONTINUE
     * 出力データ：[1, 2, 3, 4, 5, 6]
     * ※ValidateErrorStatus.CONTINUEの場合、入力チェックエラー自体が無かったことになる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorContinue001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(1);
        ValidationErrorHandler argValidationErrorHandler = new SkipValidationErrorHandler(ValidateErrorStatus.CONTINUE);
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1, 2, 3(入力チェックエラー), 4, 5, 6]
     * ValidationErrorHandlerの返却値：ValidateErrorStatus.CONTINUE
     * 出力データ：[1, 2, 3, 4, 5, 6]
     * ※ValidateErrorStatus.CONTINUEの場合、入力チェックエラー自体が無かったことになる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorContinue002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(3);
        ValidationErrorHandler argValidationErrorHandler = new SkipValidationErrorHandler(ValidateErrorStatus.CONTINUE);
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1, 2, 3, 4, 5, 6(入力チェックエラー)]
     * ValidationErrorHandlerの返却値：ValidateErrorStatus.CONTINUE
     * 出力データ：[1, 2, 3, 4, 5, 6]
     * ※ValidateErrorStatus.CONTINUEの場合、入力チェックエラー自体が無かったことになる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorContinue003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(6);
        ValidationErrorHandler argValidationErrorHandler = new SkipValidationErrorHandler(ValidateErrorStatus.CONTINUE);
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1(入力チェックエラー), 2, 3, 4, 5, 6]
     * ValidationErrorHandlerの返却値：ValidateErrorStatus.SKIP
     * 出力データ：[2, 3, 4, 5, 6]
     * ※ValidateErrorStatus.SKIPの場合、入力チェックエラーが発生したデータは、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorSkip001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(1);
        ValidationErrorHandler argValidationErrorHandler = new SkipValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("2", collector.getCurrent().getHoge());
        assertEquals("3", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第3要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第4要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1, 2, 3(入力チェックエラー), 4, 5, 6]
     * ValidationErrorHandlerの返却値：ValidateErrorStatus.SKIP
     * 出力データ：[1, 2, 4, 5, 6]
     * ※ValidateErrorStatus.SKIPの場合、入力チェックエラーが発生したデータは、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorSkip002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(3);
        ValidationErrorHandler argValidationErrorHandler = new SkipValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "4");

        // 第3要素
        assertNextData(collector, "4", "2", "4", "5");

        // 第4要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1, 2, 3, 4, 5, 6(入力チェックエラー)]
     * ValidationErrorHandlerの返却値：ValidateErrorStatus.SKIP
     * 出力データ：[1, 2, 3, 4, 5]
     * ※ValidateErrorStatus.SKIPの場合、入力チェックエラーが発生したデータは、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorSkip003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(6);
        ValidationErrorHandler argValidationErrorHandler = new SkipValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("5", collector.next().getHoge());
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1(入力チェックエラー), 2(入力チェックエラー), 3, 4(入力チェックエラー), 5(入力チェックエラー), 6, 7(入力チェックエラー), 8(入力チェックエラー)]
     * ValidationErrorHandlerの返却値：ValidateErrorStatus.SKIP
     * 出力データ：[3, 6]
     * ※ValidateErrorStatus.SKIPの場合、入力チェックエラーが発生したデータは、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorSkip004() throws Exception {
        int dataNum = 8;
        List<Integer> validationErrorOccurPoints = Arrays.asList(1, 2, 4, 5, 7, 8);
        ValidationErrorHandler argValidationErrorHandler = new SkipValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("3", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("3", collector.getCurrent().getHoge());
        assertEquals("6", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("3", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("3", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1(入力チェックエラー), 2(入力チェックエラー), 3(入力チェックエラー), 4(入力チェックエラー)]
     * ValidationErrorHandlerの返却値：ValidateErrorStatus.SKIP
     * 出力データ：[]
     * ※ValidateErrorStatus.SKIPの場合、入力チェックエラーが発生したデータは、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorSkip005() throws Exception {
        int dataNum = 4;
        List<Integer> validationErrorOccurPoints = Arrays.asList(1, 2, 3, 4);
        ValidationErrorHandler argValidationErrorHandler = new SkipValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素が無いことを確認
        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertNull(collector.getPrevious());
        assertNull(collector.getCurrent());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1(入力チェックエラー), 2, 3, 4, 5, 6]
     * ValidationErrorHandlerの返却値：ValidateErrorStatus.END
     * 出力データ：[]
     * ※ValidateErrorStatus.ENDの場合、入力チェックエラーが発生したデータの直前のデータが最後のデータ扱いとなる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorEnd001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(1);
        ValidationErrorHandler argValidationErrorHandler = new SkipValidationErrorHandler(ValidateErrorStatus.END);
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素が無いことを確認
        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertNull(collector.getPrevious());
        assertNull(collector.getCurrent());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1, 2, 3(入力チェックエラー), 4, 5, 6]
     * ValidationErrorHandlerの返却値：ValidateErrorStatus.END
     * 出力データ：[1, 2]
     * ※ValidateErrorStatus.ENDの場合、入力チェックエラーが発生したデータの直前のデータが最後のデータ扱いとなる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorEnd002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(3);
        ValidationErrorHandler argValidationErrorHandler = new SkipValidationErrorHandler(ValidateErrorStatus.END);
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        assertEquals("1", collector.getPrevious().getHoge());
        assertEquals("2", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("1", collector.getPrevious().getHoge());
        assertEquals("2", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1, 2, 3, 4, 5, 6(入力チェックエラー)]
     * ValidationErrorHandlerの返却値：ValidateErrorStatus.END
     * 出力データ：[1, 2]
     * ※ValidateErrorStatus.ENDの場合、入力チェックエラーが発生したデータの直前のデータが最後のデータ扱いとなる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorEnd003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(6);
        ValidationErrorHandler argValidationErrorHandler = new SkipValidationErrorHandler(ValidateErrorStatus.END);
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("5", collector.next().getHoge());
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合 (入力チェック付きコレクタのデフォルト設定)
     * 入力データ：[1(入力チェックエラー), 2, 3, 4, 5, 6]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandler：なし
     * 出力データ：[1(入力チェックエラー), 2, 3, 4, 5, 6]
     * ※入力チェックエラー付きの出力データは、next()のみ例外がスローされ、getPrevious()、getCurrent()、getNext()ではデータが取得できる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithoutExceptionHandler001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(1);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (ValidationErrorException e) {
        }
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合 (入力チェック付きコレクタのデフォルト設定)
     * 入力データ：[1, 2, 3(入力チェックエラー), 4, 5, 6]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandler：なし
     * 出力データ：[1, 2, 3(入力チェックエラー), 4, 5, 6]
     * ※入力チェックエラー付きの出力データは、next()のみ例外がスローされ、getPrevious()、getCurrent()、getNext()ではデータが取得できる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithoutExceptionHandler002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(3);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (ValidationErrorException e) {
        }
        assertEquals("2", collector.getPrevious().getHoge());
        assertEquals("3", collector.getCurrent().getHoge());
        assertEquals("4", collector.getNext().getHoge());

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合 (入力チェック付きコレクタのデフォルト設定)
     * 入力データ：[1, 2, 3, 4, 5, 6(入力チェックエラー)]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandler：なし
     * 出力データ：[1, 2, 3, 4, 5, 6(入力チェックエラー)]
     * ※入力チェックエラー付きの出力データは、next()のみ例外がスローされ、getPrevious()、getCurrent()、getNext()ではデータが取得できる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithoutExceptionHandler003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(6);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (ValidationErrorException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1(入力チェックエラー), 2, 3, 4, 5, 6]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.THROW
     * 出力データ：[1(入力チェックエラー), 2, 3, 4, 5, 6]
     * ※入力チェックエラー付きの出力データは、next()のみ例外がスローされ、getPrevious()、getCurrent()、getNext()ではデータが取得できる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithThrowExceptionHandler001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(1);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = new ThrowCollectorExceptionHandler();
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (ValidationErrorException e) {
        }
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1, 2, 3(入力チェックエラー), 4, 5, 6]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.THROW
     * 出力データ：[1, 2, 3(入力チェックエラー), 4, 5, 6]
     * ※入力チェックエラー付きの出力データは、next()のみ例外がスローされ、getPrevious()、getCurrent()、getNext()ではデータが取得できる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithThrowExceptionHandler002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(3);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = new ThrowCollectorExceptionHandler();
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (ValidationErrorException e) {
        }
        assertEquals("2", collector.getPrevious().getHoge());
        assertEquals("3", collector.getCurrent().getHoge());
        assertEquals("4", collector.getNext().getHoge());

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1, 2, 3, 4, 5, 6(入力チェックエラー)]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.THROW
     * 出力データ：[1, 2, 3, 4, 5, 6(入力チェックエラー)]
     * ※入力チェックエラー付きの出力データは、next()のみ例外がスローされ、getPrevious()、getCurrent()、getNext()ではデータが取得できる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithThrowExceptionHandler003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(6);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = new ThrowCollectorExceptionHandler();
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (ValidationErrorException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1(入力チェックエラー), 2, 3, 4, 5, 6]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.SKIP
     * 出力データ：[2, 3, 4, 5, 6]
     * ※CollectorExceptionHandlerStatus.SKIPの場合、入力チェックエラーが発生したデータは、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithSkipExceptionHandler001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(1);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = new SkipCollectorExceptionHandler();
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("2", collector.getCurrent().getHoge());
        assertEquals("3", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第3要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第4要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1, 2, 3(入力チェックエラー), 4, 5, 6]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.SKIP
     * 出力データ：[1, 2, 4, 5, 6]
     * ※CollectorExceptionHandlerStatus.SKIPの場合、入力チェックエラーが発生したデータは、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithSkipExceptionHandler002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(3);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = new SkipCollectorExceptionHandler();
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "4");

        // 第3要素
        assertNextData(collector, "4", "2", "4", "5");

        // 第4要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1, 2, 3, 4, 5, 6(入力チェックエラー)]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.SKIP
     * 出力データ：[1, 2, 3, 4, 5]
     * ※CollectorExceptionHandlerStatus.SKIPの場合、入力チェックエラーが発生したデータは、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithSkipExceptionHandler003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(6);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = new SkipCollectorExceptionHandler();
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("5", collector.next().getHoge());
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1(入力チェックエラー), 2(入力チェックエラー), 3, 4(入力チェックエラー), 5(入力チェックエラー), 6, 7(入力チェックエラー), 8(入力チェックエラー)]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.SKIP
     * 出力データ：[3, 6]
     * ※CollectorExceptionHandlerStatus.SKIPの場合、入力チェックエラーが発生したデータは、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithSkipExceptionHandler004() throws Exception {
        int dataNum = 8;
        List<Integer> validationErrorOccurPoints = Arrays.asList(1, 2, 4, 5, 7, 8);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = new SkipCollectorExceptionHandler();
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("3", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("3", collector.getCurrent().getHoge());
        assertEquals("6", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("3", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("3", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1(入力チェックエラー), 2(入力チェックエラー), 3(入力チェックエラー), 4(入力チェックエラー)]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.SKIP
     * 出力データ：[]
     * ※CollectorExceptionHandlerStatus.SKIPの場合、入力チェックエラーが発生したデータは、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithSkipExceptionHandler005() throws Exception {
        int dataNum = 4;
        List<Integer> validationErrorOccurPoints = Arrays.asList(1, 2, 3, 4);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = new SkipCollectorExceptionHandler();
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素が無いことを確認
        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertNull(collector.getPrevious());
        assertNull(collector.getCurrent());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1(入力チェックエラー), 2, 3, 4, 5, 6]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.END
     * 出力データ：[]
     * ※CollectorExceptionHandlerStatus.ENDの場合、入力チェックエラーが発生したデータの直前のデータが最後のデータ扱いとなる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithEndExceptionHandler001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(1);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = new EndCollectorExceptionHandler();
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素が無いことを確認
        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertNull(collector.getPrevious());
        assertNull(collector.getCurrent());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1, 2, 3(入力チェックエラー), 4, 5, 6]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.END
     * 出力データ：[1, 2]
     * ※CollectorExceptionHandlerStatus.ENDの場合、入力チェックエラーが発生したデータの直前のデータが最後のデータ扱いとなる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithEndExceptionHandler002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(3);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = new EndCollectorExceptionHandler();
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        assertEquals("1", collector.getPrevious().getHoge());
        assertEquals("2", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("1", collector.getPrevious().getHoge());
        assertEquals("2", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 入力チェックエラーが発生した場合
     * 入力データ：[1, 2, 3, 4, 5, 6(入力チェックエラー)]
     * ValidationErrorHandlerの結果：ValidationErrorExceptionをスロー
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.END
     * 出力データ：[1, 2, 3, 4, 5]
     * ※CollectorExceptionHandlerStatus.ENDの場合、入力チェックエラーが発生したデータの直前のデータが最後のデータ扱いとなる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurValidationErrorThrowWithEndExceptionHandler003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Arrays.asList(6);
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Collections.emptyList();
        CollectorExceptionHandler argExceptionHandler = new EndCollectorExceptionHandler();
        Exception thrownException = null;
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("5", collector.next().getHoge());
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に例外が発生した場合
     * 入力データ：[(例外), 2, 3, 4, 5, 6]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.SKIP
     * 出力データ：[2, 3, 4, 5, 6]
     * ※CollectorExceptionHandlerStatus.SKIPの場合、入力時に例外が発生した要素は、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithSkipExceptionHandler001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(1);
        CollectorExceptionHandler argExceptionHandler = new SkipCollectorExceptionHandler();
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("2", collector.getCurrent().getHoge());
        assertEquals("3", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第3要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第4要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に例外が発生した場合
     * 入力データ：[1, 2, (例外), 4, 5, 6]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.SKIP
     * 出力データ：[1, 2, 4, 5, 6]
     * ※CollectorExceptionHandlerStatus.SKIPの場合、入力時に例外が発生した要素は、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithSkipExceptionHandler002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(3);
        CollectorExceptionHandler argExceptionHandler = new SkipCollectorExceptionHandler();
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "4");

        // 第3要素
        assertNextData(collector, "4", "2", "4", "5");

        // 第4要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に例外が発生した場合
     * 入力データ：[1, 2, 3, 4, 5, (例外)]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.SKIP
     * 出力データ：[1, 2, 3, 4, 5]
     * ※CollectorExceptionHandlerStatus.SKIPの場合、入力時に例外が発生した要素は、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithSkipExceptionHandler003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(6);
        CollectorExceptionHandler argExceptionHandler = new SkipCollectorExceptionHandler();
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("5", collector.next().getHoge());
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に例外が発生した場合
     * 入力データ：[(例外), (例外), 3, (例外), (例外), 6, (例外), (例外)]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.SKIP
     * 出力データ：[3, 6]
     * ※CollectorExceptionHandlerStatus.SKIPの場合、入力時に例外が発生した要素は、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithSkipExceptionHandler004() throws Exception {
        int dataNum = 8;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(1, 2, 4, 5, 7, 8);
        CollectorExceptionHandler argExceptionHandler = new SkipCollectorExceptionHandler();
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("3", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("3", collector.getCurrent().getHoge());
        assertEquals("6", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("3", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("3", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に例外が発生した場合
     * 入力データ：[(例外), (例外), (例外), (例外)]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.SKIP
     * 出力データ：[]
     * ※CollectorExceptionHandlerStatus.SKIPの場合、入力時に例外が発生した要素は、スキップされ、出力されない。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithSkipExceptionHandler005() throws Exception {
        int dataNum = 4;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(1, 2, 3, 4);
        CollectorExceptionHandler argExceptionHandler = new SkipCollectorExceptionHandler();
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素が無いことを確認
        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertNull(collector.getPrevious());
        assertNull(collector.getCurrent());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に例外が発生した場合
     * 入力データ：[(例外), 2, 3, 4, 5, 6]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.END
     * 出力データ：[]
     * ※CollectorExceptionHandlerStatus.ENDの場合、入力時に例外が発生した要素の直前のデータが最後のデータ扱いとなる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithEndExceptionHandler001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(1);
        CollectorExceptionHandler argExceptionHandler = new EndCollectorExceptionHandler();
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素が無いことを確認
        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertNull(collector.getPrevious());
        assertNull(collector.getCurrent());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に例外が発生した場合
     * 入力データ：[1, 2, (例外), 4, 5, 6]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.END
     * 出力データ：[1, 2]
     * ※CollectorExceptionHandlerStatus.ENDの場合、入力時に例外が発生した要素の直前のデータが最後のデータ扱いとなる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithEndExceptionHandler002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(3);
        CollectorExceptionHandler argExceptionHandler = new EndCollectorExceptionHandler();
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        assertEquals("1", collector.getPrevious().getHoge());
        assertEquals("2", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("1", collector.getPrevious().getHoge());
        assertEquals("2", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に例外が発生した場合
     * 入力データ：[1, 2, 3, 4, 5, (例外)]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.END
     * 出力データ：[1, 2, 3, 4, 5]
     * ※CollectorExceptionHandlerStatus.ENDの場合、入力時に例外が発生した要素の直前のデータが最後のデータ扱いとなる。
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithEndExceptionHandler003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(6);
        CollectorExceptionHandler argExceptionHandler = new EndCollectorExceptionHandler();
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("5", collector.next().getHoge());
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時にランタイム例外が発生した場合
     * 入力データ：[(ランタイム例外), 2, 3, 4, 5, 6]
     * CollectorExceptionHandler：なし
     * 出力データ：[(ランタイム例外), 2, 3, 4, 5, 6]
     * ※ランタイム例外発生要素は、next()、getPrevious()、getCurrent()、getNext()でランタイム例外がそのままスローされる
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurRuntimeExceptionWithoutExceptionHandler001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(1);
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertNull(collector.getPrevious());
        try {
            collector.getCurrent();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        try {
            collector.getPrevious();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertEquals("2", collector.getCurrent().getHoge());
        assertEquals("3", collector.getNext().getHoge());

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時にランタイム例外が発生した場合
     * 入力データ：[1, 2, (ランタイム例外), 4, 5, 6]
     * CollectorExceptionHandler：なし
     * 出力データ：[1, 2, (ランタイム例外), 4, 5, 6]
     * ※ランタイム例外発生要素は、next()、getPrevious()、getCurrent()、getNext()でランタイム例外がそのままスローされる
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurRuntimeExceptionWithoutExceptionHandler002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(3);
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        assertEquals("1", collector.getPrevious().getHoge());
        assertEquals("2", collector.getCurrent().getHoge());
        try {
            collector.getNext();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }

        // 第3要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertEquals("2", collector.getPrevious().getHoge());
        try {
            collector.getCurrent();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertEquals("4", collector.getNext().getHoge());

        // 第4要素
        assertTrue(collector.hasNext());
        assertEquals("4", collector.next().getHoge());
        try {
            collector.getPrevious();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertEquals("4", collector.getCurrent().getHoge());
        assertEquals("5", collector.getNext().getHoge());

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時にランタイム例外が発生した場合
     * 入力データ：[1, 2, 3, 4, 5, (ランタイム例外)]
     * CollectorExceptionHandler：なし
     * 出力データ：[1, 2, 3, 4, 5, (ランタイム例外)]
     * ※ランタイム例外発生要素は、next()、getPrevious()、getCurrent()、getNext()でランタイム例外がそのままスローされる
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurRuntimeExceptionWithoutExceptionHandler003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(6);
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("5", collector.next().getHoge());
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        try {
            collector.getNext();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }

        // 第6要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertEquals("5", collector.getPrevious().getHoge());
        try {
            collector.getCurrent();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        try {
            collector.getCurrent();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時にランタイム例外が発生した場合
     * 入力データ：[(ランタイム例外), 2, 3, 4, 5, 6]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.THROW
     * 出力データ：[(ランタイム例外), 2, 3, 4, 5, 6]
     * ※ランタイム例外発生要素は、next()、getPrevious()、getCurrent()、getNext()でランタイム例外がそのままスローされる
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurRuntimeExceptionWithThrowExceptionHandler001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(1);
        CollectorExceptionHandler argExceptionHandler = new ThrowCollectorExceptionHandler();
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertNull(collector.getPrevious());
        try {
            collector.getCurrent();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        try {
            collector.getPrevious();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertEquals("2", collector.getCurrent().getHoge());
        assertEquals("3", collector.getNext().getHoge());

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時にランタイム例外が発生した場合
     * 入力データ：[1, 2, (ランタイム例外), 4, 5, 6]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.THROW
     * 出力データ：[1, 2, (ランタイム例外), 4, 5, 6]
     * ※ランタイム例外発生要素は、next()、getPrevious()、getCurrent()、getNext()でランタイム例外がそのままスローされる
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurRuntimeExceptionWithThrowExceptionHandler002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(3);
        CollectorExceptionHandler argExceptionHandler = new ThrowCollectorExceptionHandler();
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        assertEquals("1", collector.getPrevious().getHoge());
        assertEquals("2", collector.getCurrent().getHoge());
        try {
            collector.getNext();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }

        // 第3要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertEquals("2", collector.getPrevious().getHoge());
        try {
            collector.getCurrent();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertEquals("4", collector.getNext().getHoge());

        // 第4要素
        assertTrue(collector.hasNext());
        assertEquals("4", collector.next().getHoge());
        try {
            collector.getPrevious();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertEquals("4", collector.getCurrent().getHoge());
        assertEquals("5", collector.getNext().getHoge());

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時にランタイム例外が発生した場合
     * 入力データ：[1, 2, 3, 4, 5, (ランタイム例外)]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.THROW
     * 出力データ：[1, 2, 3, 4, 5, (ランタイム例外)]
     * ※ランタイム例外発生要素は、next()、getPrevious()、getCurrent()、getNext()でランタイム例外がそのままスローされる
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurRuntimeExceptionWithThrowExceptionHandler003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(6);
        CollectorExceptionHandler argExceptionHandler = new ThrowCollectorExceptionHandler();
        Exception thrownException = new FileLineException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("5", collector.next().getHoge());
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        try {
            collector.getNext();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }

        // 第6要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertEquals("5", collector.getPrevious().getHoge());
        try {
            collector.getCurrent();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        try {
            collector.getCurrent();
            fail();
        } catch (FileLineException e) {
            assertSame(thrownException, e);
        }
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に検査例外が発生した場合
     * 入力データ：[(検査例外), 2, 3, 4, 5, 6]
     * CollectorExceptionHandler：なし
     * 出力データ：[(SystemExceptionにラップされた検査例外), 2, 3, 4, 5, 6]
     * ※検査例外発生要素は、next()、getPrevious()、getCurrent()、getNext()でSystemExceptionに検査例外がラップされスローされる
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithoutExceptionHandler001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(1);
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = new IOException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertNull(collector.getPrevious());
        try {
            collector.getCurrent();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        try {
            collector.getPrevious();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertEquals("2", collector.getCurrent().getHoge());
        assertEquals("3", collector.getNext().getHoge());

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に検査例外が発生した場合
     * 入力データ：[1, 2, (検査例外), 4, 5, 6]
     * CollectorExceptionHandler：なし
     * 出力データ：[1, 2, (SystemExceptionにラップされた検査例外), 4, 5, 6]
     * ※検査例外発生要素は、next()、getPrevious()、getCurrent()、getNext()でSystemExceptionに検査例外がラップされスローされる
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithoutExceptionHandler002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(3);
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = new IOException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        assertEquals("1", collector.getPrevious().getHoge());
        assertEquals("2", collector.getCurrent().getHoge());
        try {
            collector.getNext();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }

        // 第3要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertEquals("2", collector.getPrevious().getHoge());
        try {
            collector.getCurrent();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertEquals("4", collector.getNext().getHoge());

        // 第4要素
        assertTrue(collector.hasNext());
        assertEquals("4", collector.next().getHoge());
        try {
            collector.getPrevious();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertEquals("4", collector.getCurrent().getHoge());
        assertEquals("5", collector.getNext().getHoge());

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に検査例外が発生した場合
     * 入力データ：[1, 2, 3, 4, 5, (検査例外)]
     * CollectorExceptionHandler：なし
     * 出力データ：[1, 2, 3, 4, 5, (SystemExceptionにラップされた検査例外)]
     * ※検査例外発生要素は、next()、getPrevious()、getCurrent()、getNext()でSystemExceptionに検査例外がラップされスローされる
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithoutExceptionHandler003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(6);
        CollectorExceptionHandler argExceptionHandler = null;
        Exception thrownException = new IOException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("5", collector.next().getHoge());
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        try {
            collector.getNext();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }

        // 第6要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertEquals("5", collector.getPrevious().getHoge());
        try {
            collector.getCurrent();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        try {
            collector.getCurrent();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に検査例外が発生した場合
     * 入力データ：[(検査例外), 2, 3, 4, 5, 6]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.THROW
     * 出力データ：[(SystemExceptionにラップされた検査例外), 2, 3, 4, 5, 6]
     * ※検査例外発生要素は、next()、getPrevious()、getCurrent()、getNext()でSystemExceptionに検査例外がラップされスローされる
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithThrowExceptionHandler001() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(1);
        CollectorExceptionHandler argExceptionHandler = new ThrowCollectorExceptionHandler();
        Exception thrownException = new IOException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertNull(collector.getPrevious());
        try {
            collector.getCurrent();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        try {
            collector.getPrevious();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertEquals("2", collector.getCurrent().getHoge());
        assertEquals("3", collector.getNext().getHoge());

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に検査例外が発生した場合
     * 入力データ：[1, 2, (検査例外), 4, 5, 6]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.THROW
     * 出力データ：[1, 2, (SystemExceptionにラップされた検査例外), 4, 5, 6]
     * ※検査例外発生要素は、next()、getPrevious()、getCurrent()、getNext()でSystemExceptionに検査例外がラップされスローされる
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithThrowExceptionHandler002() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(3);
        CollectorExceptionHandler argExceptionHandler = new ThrowCollectorExceptionHandler();
        Exception thrownException = new IOException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertTrue(collector.hasNext());
        assertEquals("2", collector.next().getHoge());
        assertEquals("1", collector.getPrevious().getHoge());
        assertEquals("2", collector.getCurrent().getHoge());
        try {
            collector.getNext();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }

        // 第3要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertEquals("2", collector.getPrevious().getHoge());
        try {
            collector.getCurrent();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertEquals("4", collector.getNext().getHoge());

        // 第4要素
        assertTrue(collector.hasNext());
        assertEquals("4", collector.next().getHoge());
        try {
            collector.getPrevious();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertEquals("4", collector.getCurrent().getHoge());
        assertEquals("5", collector.getNext().getHoge());

        // 第5要素
        assertNextData(collector, "5", "4", "5", "6");

        // 第6要素
        assertTrue(collector.hasNext());
        assertEquals("6", collector.next().getHoge());
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        assertEquals("6", collector.getCurrent().getHoge());
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * データ入力時に検査例外が発生した場合
     * 入力データ：[1, 2, 3, 4, 5, (検査例外)]
     * CollectorExceptionHandlerの結果：CollectorExceptionHandlerStatus.THROW
     * 出力データ：[1, 2, 3, 4, 5, (SystemExceptionにラップされた検査例外)]
     * ※検査例外発生要素は、next()、getPrevious()、getCurrent()、getNext()でSystemExceptionに検査例外がラップされスローされる
     * ・hasNext()、next()、getPrevious()、getCurrent()、getNext()の結果が、出力データに則したものであること
     * ・次のデータが無い状態でnext()を実行するとNoSuchElementExceptionがスローされること
     * ・次のデータが無い状態でnext()を実行しても、getPrevious()、getCurrent()、getNext()の結果はnext()実行前と変わらないこと
     */
    @Test
    public void testOccurExceptionWithThrowExceptionHandler003() throws Exception {
        int dataNum = 6;
        List<Integer> validationErrorOccurPoints = Collections.emptyList();
        ValidationErrorHandler argValidationErrorHandler = new ExceptionValidationErrorHandler();
        List<Integer> exceptionOccurPoints = Arrays.asList(6);
        CollectorExceptionHandler argExceptionHandler = new ThrowCollectorExceptionHandler();
        Exception thrownException = new IOException("test");
        List<Integer> nullBeanPoints = Collections.emptyList();
        Collector<AbstractCollectorTestBean> collector = createTestCollector(dataNum, validationErrorOccurPoints, argValidationErrorHandler, exceptionOccurPoints, argExceptionHandler, thrownException, nullBeanPoints);

        // 第1要素
        assertTrue(collector.hasNext());
        assertEquals("1", collector.next().getHoge());
        assertNull(collector.getPrevious());
        assertEquals("1", collector.getCurrent().getHoge());
        assertEquals("2", collector.getNext().getHoge());

        // 第2要素
        assertNextData(collector, "2", "1", "2", "3");

        // 第3要素
        assertNextData(collector, "3", "2", "3", "4");

        // 第4要素
        assertNextData(collector, "4", "3", "4", "5");

        // 第5要素
        assertTrue(collector.hasNext());
        assertEquals("5", collector.next().getHoge());
        assertEquals("4", collector.getPrevious().getHoge());
        assertEquals("5", collector.getCurrent().getHoge());
        try {
            collector.getNext();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }

        // 第6要素
        assertTrue(collector.hasNext());
        try {
            collector.next();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertEquals("5", collector.getPrevious().getHoge());
        try {
            collector.getCurrent();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertNull(collector.getNext());

        assertFalse(collector.hasNext());

        try {
            collector.next();
            fail();
        } catch (NoSuchElementException e) {
        }
        assertEquals("5", collector.getPrevious().getHoge());
        try {
            collector.getCurrent();
            fail();
        } catch (SystemException e) {
            assertSame(thrownException, e.getCause());
        }
        assertNull(collector.getNext());

        collector.close();
    }

    /**
     * 単純にデータが取得できる場合に使用できる簡易アサーションメソッド。<br>
     * assertTrue(collector.hasNext());<br>
     * assertEquals("2", collector.next().getHoge());<br>
     * assertEquals("1", collector.getPrevious().getHoge());<br>
     * assertEquals("2", collector.getCurrent().getHoge());<br>
     * assertEquals("3", collector.getNext().getHoge());<br>
     * を<br>
     * assertNextData("2", "1", "2", "3");<br>
     * と記述できる。<br>
     * 例外発生ケースやnullを返すケースでは、このメソッドは使用せず、個別にアサーションを実施すること。
     * @param collector コレクタ
     * @param nextExpectedHoge collector.next().getHoge()の期待値
     * @param getPreviousExpectedHoge collector.getPrevious().getHoge()の期待値
     * @param getCurrentExpectedHoge collector.getCurrent().getHoge()の期待値
     * @param getNextExpectedHoge collector.getNext().getHoge()の期待値
     */
    private static void assertNextData(Collector<AbstractCollectorTestBean> collector, String nextExpectedHoge, String getPreviousExpectedHoge, String getCurrentExpectedHoge, String getNextExpectedHoge) {
        assertTrue(collector.hasNext());
        assertEquals(nextExpectedHoge, collector.next().getHoge());
        assertEquals(getPreviousExpectedHoge, collector.getPrevious().getHoge());
        assertEquals(getCurrentExpectedHoge, collector.getCurrent().getHoge());
        assertEquals(getNextExpectedHoge, collector.getNext().getHoge());
    }

    /**
     * テストデータを提供するコレクタを生成する。
     * <p>
     * 例1) 入力データ：[1, 2, 3, 4, 5]の作り方<br>
     * createTestCollector(5, Collections.EMPTY_LIST, new ExceptionValidationErrorHandler(), Collections.EMPTY_LIST, null, null, Collections.EMPTY_LIST);<br>
     * <br>
     * 例2) 入力データ：[1, null, 3, null, 5]の作り方<br>
     * createTestCollector(5, Collections.EMPTY_LIST, new ExceptionValidationErrorHandler(), Collections.EMPTY_LIST, null, null, Arrays.asList(2, 4));<br>
     * <br>
     * 例3) 入力データ：[1, 2(入力チェックエラー), 3, 4(入力チェックエラー), 5]の作り方<br>
     * createTestCollector(5, Arrays.asList(2, 4), new ExceptionValidationErrorHandler(), Collections.EMPTY_LIST, null, null, Collections.EMPTY_LIST);<br>
     * <br>
     * 例4) 入力データ：[1, (例外), 3, 4(例外), 5]の作り方<br>
     * createTestCollector(5, Collections.EMPTY_LIST, new ExceptionValidationErrorHandler(), Arrays.asList(2, 4), null, 例外, Collections.EMPTY_LIST);<br>
     * <br>
     * ※タイプセーフにする場合は、Collections.EMPTY_LISTの代わりに、Collections.emptyList()をList＜Integer＞型の変数に入れて使用する。
     * @param dataNum データの個数
     * @param validationErrorOccurPoints 入力チェックエラー発生ポイント
     * @param argValidationErrorHandler ValidationErrorHandler
     * @param exceptionOccurPoints 例外発生ポイント
     * @param argExceptionHandler ExceptionHandler
     * @param thrownException スローする例外
     * @param nullBeanPoints Beanの代わりにnullを詰めるポイント
     * @return テストデータを提供するコレクタ
     */
    private static Collector<AbstractCollectorTestBean> createTestCollector(final int dataNum, final List<Integer> validationErrorOccurPoints, final ValidationErrorHandler argValidationErrorHandler, final List<Integer> exceptionOccurPoints, final CollectorExceptionHandler argExceptionHandler, final Exception thrownException, final List<Integer> nullBeanPoints) {
        Collector<AbstractCollectorTestBean> collector = new AbstractCollector<AbstractCollectorTestBean>() {
            {
                this.validator = new Validator() {

                    public void validate(Object target, Errors errors) {
                        AbstractCollectorTestBean data = (AbstractCollectorTestBean) target;
                        if (validationErrorOccurPoints.contains(new Integer(data.getHoge()))) {
                            errors.rejectValue("hoge", "errors.required");
                        }
                    }

                    public boolean supports(Class<?> clazz) {
                        return (clazz == AbstractCollectorTestBean.class);
                    }
                };
                this.validationErrorHandler = argValidationErrorHandler;
                this.exceptionHandler = argExceptionHandler;
            }

            public Integer call() throws Exception {
                for (int count = 1; count <= dataNum; count++) {
                    if (exceptionOccurPoints.contains(count)) {
                        addQueue(new DataValueObject(thrownException, count));
                    } else if (nullBeanPoints.contains(count)) {
                        addQueue(new DataValueObject(null, count));
                    } else {
                        AbstractCollectorTestBean bean = new AbstractCollectorTestBean();
                        bean.setHoge(String.valueOf(count));
                        addQueue(new DataValueObject(bean, count));
                    }
                }

                setFinish();
                return 0;
            }

        };

        return collector;

    }

    /**
     * CollectorExceptionHandlerStatus.ENDを返却するCollectorExceptionHandler。
     */
    private static class EndCollectorExceptionHandler implements CollectorExceptionHandler {
        public CollectorExceptionHandlerStatus handleException(
                DataValueObject dataValueObject) {
            return CollectorExceptionHandlerStatus.END;
        }
    }

    /**
     * CollectorExceptionHandlerStatus.SKIPを返却するCollectorExceptionHandler。
     */
    private static class SkipCollectorExceptionHandler implements CollectorExceptionHandler {
        public CollectorExceptionHandlerStatus handleException(
                DataValueObject dataValueObject) {
            return CollectorExceptionHandlerStatus.SKIP;
        }
    }

    /**
     * CollectorExceptionHandlerStatus.THROWを返却するCollectorExceptionHandler。
     */
    private static class ThrowCollectorExceptionHandler implements CollectorExceptionHandler {
        public CollectorExceptionHandlerStatus handleException(
                DataValueObject dataValueObject) {
            return CollectorExceptionHandlerStatus.THROW;
        }
    }

}
