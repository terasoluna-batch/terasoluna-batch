package jp.terasoluna.fw.batch.blogic;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Map;

import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.exception.BatchException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;

@RunWith(MockitoJUnitRunner.class)
public class AbstractTransactionBLogicTest {

    @Captor
    private ArgumentCaptor<Map<String, TransactionStatus>> trnStsMap;

    /**
     * testExecute001
     */
    @Test
    public void testExecute001() {
        AbstractTransactionBLogic blogic = spy(new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                return 0;
            }
        });

        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
                "jp/terasoluna/fw/batch/blogic/TestContext.xml" });
        blogic.setApplicationContext(context);

        BLogicParam param = new BLogicParam();

        // テスト
        int result = blogic.execute(param);

        assertEquals(0, result);

        verify(blogic).startTransactions(any(Map.class));
        verify(blogic).commitTransactions(Matchers.<Map<?, ?>>any(),
                Matchers.<Map<String, TransactionStatus>>any());

        verify(blogic).endTransactions(any(Map.class), trnStsMap.capture());

        assertEquals(1, trnStsMap.getValue().size());
        for (Object txStatusObj : trnStsMap.getValue().values()) {
            TransactionStatus txStatus = TransactionStatus.class.cast(txStatusObj);
            assertTrue(txStatus.isCompleted());
        }
    }

    /**
     * testExecute002
     */
    @Test
    public void testExecute002() {
        AbstractTransactionBLogic blogic = spy(new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                throw new BatchException("hoge");
            }
        });
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

            verify(blogic).startTransactions(any(Map.class));
            verify(blogic, never()).commitTransactions(any(Map.class),
                    Matchers.<Map<String, TransactionStatus>>any());

            verify(blogic).endTransactions(any(Map.class), trnStsMap.capture());

            assertEquals(1, trnStsMap.getValue().size());
            for (Object txStatusObj : trnStsMap.getValue().values()) {
                TransactionStatus txStatus
                        = TransactionStatus.class.cast(txStatusObj);
                assertTrue(txStatus.isCompleted());
            }
        }
    }

    /**
     * testExecute003
     */
    @Test
    public void testExecute003() {
        AbstractTransactionBLogic blogic = spy(new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                throw new NullPointerException("ぬるぽ");
            }
        });
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
                "jp/terasoluna/fw/batch/blogic/TestContext.xml" });
        blogic.setApplicationContext(context);
        BLogicParam param = new BLogicParam();

        // テスト
        try {
            blogic.execute(param);
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());

            verify(blogic).startTransactions(any(Map.class));
            verify(blogic, never()).commitTransactions(any(Map.class),
                    Matchers.<Map<String, TransactionStatus>>any());

            verify(blogic).endTransactions(any(Map.class), trnStsMap.capture());

            assertEquals(1, trnStsMap.getValue().size());
            for (Object txStatusObj : trnStsMap.getValue().values()) {
                TransactionStatus txStatus
                        = TransactionStatus.class.cast(txStatusObj);
                assertTrue(txStatus.isCompleted());
            }
        }
    }

    /**
     * testExecute004
     */
    @Test
    public void testExecute004() {
        AbstractTransactionBLogic blogic = spy(new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                throw new OutOfMemoryError("※テスト※メモリ不足※テスト※");
            }
        });
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

            verify(blogic).startTransactions(any(Map.class));
            verify(blogic, never()).commitTransactions(any(Map.class),
                    Matchers.<Map<String, TransactionStatus>>any());

            verify(blogic).endTransactions(any(Map.class), trnStsMap.capture());

            assertEquals(1, trnStsMap.getValue().size());
            for (Object txStatusObj : trnStsMap.getValue().values()) {
                TransactionStatus txStatus
                        = TransactionStatus.class.cast(txStatusObj);
                assertTrue(txStatus.isCompleted());
            }
        }
    }

    /**
     * testExecute005
     */
    @Test
    public void testExecute005() {
        AbstractTransactionBLogic blogic = spy(new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                return 0;
            }
        });
        ApplicationContext parent = new ClassPathXmlApplicationContext(new String[] {
                "jp/terasoluna/fw/batch/blogic/TestContext.xml" });
        ApplicationContext child = new ClassPathXmlApplicationContext(new String[] {
                "jp/terasoluna/fw/batch/blogic/NullContext.xml" }, parent);

        blogic.setApplicationContext(child);

        BLogicParam param = new BLogicParam();

        // テスト
        int result = blogic.execute(param);

        assertEquals(0, result);

        verify(blogic).startTransactions(any(Map.class));
        verify(blogic).commitTransactions(any(Map.class),
                Matchers.<Map<String, TransactionStatus>>any());

        verify(blogic).endTransactions(any(Map.class), trnStsMap.capture());

        assertEquals(1, trnStsMap.getValue().size());
        for (Object txStatusObj : trnStsMap.getValue().values()) {
            TransactionStatus txStatus
                    = TransactionStatus.class.cast(txStatusObj);
            assertTrue(txStatus.isCompleted());
        }
    }

    /**
     * testExecute006
     */
    @Test
    public void testExecute006() {
        AbstractTransactionBLogic blogic = spy(new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                return 0;
            }
        });

        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
                "jp/terasoluna/fw/batch/blogic/MultiDataSourceContext.xml" });
        blogic.setApplicationContext(context);

        BLogicParam param = new BLogicParam();

        // テスト
        int result = blogic.execute(param);

        assertEquals(0, result);

        verify(blogic).startTransactions(any(Map.class));
        verify(blogic).commitTransactions(any(Map.class),
                Matchers.<Map<String, TransactionStatus>>any());

        verify(blogic).endTransactions(any(Map.class), trnStsMap.capture());

        assertEquals(2, trnStsMap.getValue().size());
        for (Object txStatusObj : trnStsMap.getValue().values()) {
            TransactionStatus txStatus = TransactionStatus.class.cast(txStatusObj);
            assertTrue(txStatus.isCompleted());
        }
    }

    /**
     * testExecute007
     */
    @Test
    public void testExecute007() {
        AbstractTransactionBLogic blogic = spy(new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                throw new RuntimeException("throwing RuntimeException");
            }
        });
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
                "jp/terasoluna/fw/batch/blogic/MultiDataSourceContext.xml" });
        blogic.setApplicationContext(context);
        BLogicParam param = new BLogicParam();

        // テスト
        try {
            blogic.execute(param);
        } catch (Exception e) {
            assertEquals(RuntimeException.class, e.getClass());

            verify(blogic).startTransactions(any(Map.class));
            verify(blogic, never()).commitTransactions(any(Map.class),
                    Matchers.<Map<String, TransactionStatus>>any());

            verify(blogic).endTransactions(any(Map.class), trnStsMap.capture());

            assertEquals(2, trnStsMap.getValue().size());
            for (Object txStatusObj : trnStsMap.getValue().values()) {
                TransactionStatus txStatus
                        = TransactionStatus.class.cast(txStatusObj);
                assertTrue(txStatus.isCompleted());
            }
        }
    }
}
