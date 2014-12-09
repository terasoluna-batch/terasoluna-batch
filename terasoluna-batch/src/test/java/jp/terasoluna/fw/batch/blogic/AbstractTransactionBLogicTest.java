package jp.terasoluna.fw.batch.blogic;

import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.exception.BatchException;
import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AbstractTransactionBLogicTest extends TestCase {

    /**
     * testExecute001
     */
    public void testExecute001() {
        AbstractTransactionBLogic blogic = new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                return 0;
            }
        };
        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "jp/terasoluna/fw/batch/blogic/TestContext.xml" });
        blogic.setApplicationContext(context);

        BLogicParam param = new BLogicParam();

        // テスト
        int result = blogic.execute(param);

        assertEquals(0, result);
    }

    /**
     * testExecute002
     */
    public void testExecute002() {
        AbstractTransactionBLogic blogic = new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                throw new BatchException("hoge");
            }
        };
        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "jp/terasoluna/fw/batch/blogic/TestContext2.xml" });
        blogic.setApplicationContext(context);

        BLogicParam param = new BLogicParam();

        // テスト
        @SuppressWarnings("unused")
        int result = -1;
        try {
            result = blogic.execute(param);
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals(BatchException.class, e.getClass());
            assertEquals("hoge", e.getMessage());
            return;
        }
        fail();
    }

    /**
     * testExecute011
     */
    public void testExecute011() {
        AbstractTransactionBLogic blogic = new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                throw new NullPointerException("ぬるぽ");
            }
        };
        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "jp/terasoluna/fw/batch/blogic/TestContext.xml" });
        blogic.setApplicationContext(context);
        BLogicParam param = new BLogicParam();

        // テスト
        try {
            blogic.execute(param);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals(NullPointerException.class, e.getClass());
        }
    }

    /**
     * testExecute012
     */
    public void testExecute012() {
        AbstractTransactionBLogic blogic = new AbstractTransactionBLogic() {
            @Override
            public int doMain(BLogicParam param) {
                throw new OutOfMemoryError("※テスト※メモリ不足※テスト※");
            }
        };
        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "jp/terasoluna/fw/batch/blogic/TestContext.xml" });
        blogic.setApplicationContext(context);
        BLogicParam param = new BLogicParam();

        // テスト
        try {
            blogic.execute(param);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals(BatchException.class, e.getClass());
            assertEquals(OutOfMemoryError.class, e.getCause().getClass());
        }
    }

}
