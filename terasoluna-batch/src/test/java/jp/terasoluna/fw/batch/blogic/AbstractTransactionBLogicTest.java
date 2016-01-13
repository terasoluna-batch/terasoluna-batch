package jp.terasoluna.fw.batch.blogic;

import static org.junit.Assert.*;

import java.util.Map;

import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.exception.BatchException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.TransactionStatus;

public class AbstractTransactionBLogicTest {

    /**
     * testExecute001
     */
    @Test
    public void testExecute001() {
        AbstractTransactionBLogic blogic = new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                return 0;
            }
        };
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
                "jp/terasoluna/fw/batch/blogic/TestContext.xml" });
        blogic.setApplicationContext(context);

        BLogicParam param = new BLogicParam();

        // テスト
        int result = blogic.execute(param);

        assertEquals(0, result);

        @SuppressWarnings("unchecked")
        Map<String, TransactionStatus> txStatusMap = (Map<String, TransactionStatus>) ReflectionTestUtils
                .getField(blogic, "transactionStatusMap");

        assertEquals(1, txStatusMap.size());
        for (TransactionStatus txStatus : txStatusMap.values()) {
            assertTrue(txStatus.isCompleted());
        }
    }

    /**
     * testExecute002
     */
    @Test
    public void testExecute002() {
        AbstractTransactionBLogic blogic = new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                throw new BatchException("hoge");
            }
        };
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
                "jp/terasoluna/fw/batch/blogic/TestContext.xml" });
        blogic.setApplicationContext(context);

        BLogicParam param = new BLogicParam();

        // テスト
        try {
            blogic.execute(param);
            fail("An exception has not been detected.");
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals(BatchException.class, e.getClass());
            assertEquals("hoge", e.getMessage());

            @SuppressWarnings("unchecked")
            Map<String, TransactionStatus> txStatusMap = (Map<String, TransactionStatus>) ReflectionTestUtils
                    .getField(blogic, "transactionStatusMap");

            assertEquals(1, txStatusMap.size());
            for (TransactionStatus txStatus : txStatusMap.values()) {
                assertTrue(txStatus.isCompleted());
            }
        }
    }

    /**
     * testExecute003
     */
    @Test
    public void testExecute003() {
        AbstractTransactionBLogic blogic = new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                throw new NullPointerException("ぬるぽ");
            }
        };
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
                "jp/terasoluna/fw/batch/blogic/TestContext.xml" });
        blogic.setApplicationContext(context);
        BLogicParam param = new BLogicParam();

        // テスト
        try {
            blogic.execute(param);
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());

            @SuppressWarnings("unchecked")
            Map<String, TransactionStatus> txStatusMap = (Map<String, TransactionStatus>) ReflectionTestUtils
                    .getField(blogic, "transactionStatusMap");

            assertEquals(1, txStatusMap.size());
            for (TransactionStatus txStatus : txStatusMap.values()) {
                assertTrue(txStatus.isCompleted());
            }
        }
    }

    /**
     * testExecute004
     */
    @Test
    public void testExecute004() {
        AbstractTransactionBLogic blogic = new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                throw new OutOfMemoryError("※テスト※メモリ不足※テスト※");
            }
        };
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
                "jp/terasoluna/fw/batch/blogic/TestContext.xml" });
        blogic.setApplicationContext(context);
        BLogicParam param = new BLogicParam();

        // テスト
        try {
            blogic.execute(param);
        } catch (Exception e) {
            assertEquals(BatchException.class, e.getClass());
            assertEquals(OutOfMemoryError.class, e.getCause().getClass());

            @SuppressWarnings("unchecked")
            Map<String, TransactionStatus> txStatusMap = (Map<String, TransactionStatus>) ReflectionTestUtils
                    .getField(blogic, "transactionStatusMap");

            assertEquals(1, txStatusMap.size());
            for (TransactionStatus txStatus : txStatusMap.values()) {
                assertTrue(txStatus.isCompleted());
            }
        }
    }

    /**
     * testExecute005
     */
    @Test
    public void testExecute005() {
        AbstractTransactionBLogic blogic = new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                return 0;
            }
        };
        ApplicationContext parent = new ClassPathXmlApplicationContext(new String[] {
                "jp/terasoluna/fw/batch/blogic/TestContext.xml" });
        ApplicationContext child = new ClassPathXmlApplicationContext(new String[] {
                "jp/terasoluna/fw/batch/blogic/NullContext.xml" }, parent);

        blogic.setApplicationContext(child);

        BLogicParam param = new BLogicParam();

        // テスト
        int result = blogic.execute(param);

        assertEquals(0, result);

        @SuppressWarnings("unchecked")
        Map<String, TransactionStatus> txStatusMap = (Map<String, TransactionStatus>) ReflectionTestUtils
                .getField(blogic, "transactionStatusMap");

        assertEquals(1, txStatusMap.size());
        for (TransactionStatus txStatus : txStatusMap.values()) {
            assertTrue(txStatus.isCompleted());
        }
    }

}
