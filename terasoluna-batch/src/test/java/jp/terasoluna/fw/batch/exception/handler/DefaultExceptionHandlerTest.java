package jp.terasoluna.fw.batch.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.test.util.ReflectionTestUtils;

import jp.terasoluna.fw.batch.exception.BatchException;
import junit.framework.TestCase;

public class DefaultExceptionHandlerTest extends TestCase {

    /**
     * testSetExceptionToStatusMap001
     */
    public void testSetExceptionToStatusMap001() {
        DefaultExceptionHandler handler = new DefaultExceptionHandler();

        Map<Class<? extends Throwable>, Integer> exceptionToStatusMap = new HashMap<Class<? extends Throwable>, Integer>();

        // テスト
        handler.setExceptionToStatusMap(exceptionToStatusMap);

        Object result;
        result = ReflectionTestUtils.getField(handler, "exceptionToStatusMap");

        assertNotNull(result);
        assertEquals(exceptionToStatusMap, result);
    }

    /**
     * testHandleThrowableException001
     */
    public void testHandleThrowableException001() {
        DefaultExceptionHandler handler = new DefaultExceptionHandler();

        Map<Class<? extends Throwable>, Integer> exceptionToStatusMap = new HashMap<Class<? extends Throwable>, Integer>();
        exceptionToStatusMap.put(BatchException.class, 3);
        handler.setExceptionToStatusMap(exceptionToStatusMap);

        // テスト
        int result = handler.handleThrowableException(new BatchException());

        assertEquals(3, result);
    }

    /**
     * testHandleThrowableException002
     */
    public void testHandleThrowableException002() {
        DefaultExceptionHandler handler = new DefaultExceptionHandler();

        Map<Class<? extends Throwable>, Integer> exceptionToStatusMap = new HashMap<Class<? extends Throwable>, Integer>();
        exceptionToStatusMap.put(IllegalArgumentException.class, 3);
        handler.setExceptionToStatusMap(exceptionToStatusMap);

        // テスト
        int result = handler.handleThrowableException(new BatchException());

        assertEquals(255, result);
    }

    /**
     * testHandleThrowableException003
     */
    public void testHandleThrowableException003() {
        DefaultExceptionHandler handler = new DefaultExceptionHandler();

        Map<Class<? extends Throwable>, Integer> exceptionToStatusMap = null;
        handler.setExceptionToStatusMap(exceptionToStatusMap);

        // テスト
        int result = handler.handleThrowableException(new BatchException());

        assertEquals(255, result);
    }
}
