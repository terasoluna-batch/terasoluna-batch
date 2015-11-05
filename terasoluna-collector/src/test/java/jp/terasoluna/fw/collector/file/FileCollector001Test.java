package jp.terasoluna.fw.collector.file;

import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.CollectorTestUtil;
import jp.terasoluna.fw.collector.db.UserBean;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.util.MemoryInfo;
import jp.terasoluna.fw.collector.vo.DataValueObject;
import jp.terasoluna.fw.file.dao.FileQueryDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.test.context.ContextConfiguration;
import jp.terasoluna.fw.collector.unit.testcase.junit4.DaoTestCaseJunit4;
import jp.terasoluna.fw.collector.unit.testcase.junit4.loader.DaoTestCaseContextLoader;

@ContextConfiguration(locations = {
        "classpath:jp/terasoluna/fw/collector/db/dataSource.xml" }, loader = DaoTestCaseContextLoader.class)
public class FileCollector001Test extends DaoTestCaseJunit4 {
    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(FileCollector001Test.class);

    private FileQueryDAO csvFileQueryDAO = null;

    public void setCsvFileQueryDAO(FileQueryDAO csvFileQueryDAO) {
        this.csvFileQueryDAO = csvFileQueryDAO;
    }

    @Before
    public void onSetUp() throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        System.gc();
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
    }

    @After
    public void onTearDown() throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        System.gc();
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        CollectorTestUtil.allInterrupt();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testFileCollector001() throws Exception {
        if (this.csvFileQueryDAO == null) {
            fail("csvFileQueryDAOがnullです。");
        }

        URL url = getClass().getClassLoader().getResource("USER_TEST.csv");
        if (logger.isDebugEnabled()) {
            if (url != null) {
                logger.debug("url.getPath() : " + url.getPath());
            } else {
                logger.debug("url.getPath() : " + null);
            }
        }

        if (url == null) {
            fail("urlがnullです。");
        }

        // int count_first = 0;
        CollectorExceptionHandler exceptionHandler = new CollectorExceptionHandlerStub();

        Collector<B000001Data> it = new FileCollector<B000001Data>(this.csvFileQueryDAO, url
                .getPath(), B000001Data.class, exceptionHandler);

        it.close();
    }

    /**
     * testFileCollector002
     * @throws Exception
     */
    @Test
    public void testFileCollector002() throws Exception {
        FileCollectorConfig<UserBean> config = null;
        FileCollector<UserBean> col = null;
        try {
            col = new FileCollector<UserBean>(config);
            fail("失敗");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
            assertEquals("The parameter is null.", e.getMessage());
        } finally {
            if (col != null) {
                col.close();
            }
        }
    }

    /**
     * testCall001
     * @throws Exception
     */
    @Test
    public void testCall001() throws Exception {
        if (this.csvFileQueryDAO == null) {
            fail("csvFileQueryDAOがnullです。");
        }

        URL url = getClass().getClassLoader().getResource("USER_TEST.csv");
        if (logger.isDebugEnabled()) {
            if (url != null) {
                logger.debug("url.getPath() : " + url.getPath());
            } else {
                logger.debug("url.getPath() : " + null);
            }
        }

        if (url == null) {
            fail("urlがnullです。");
        }

        // int count_first = 0;
        CollectorExceptionHandler exceptionHandler = new CollectorExceptionHandlerStub();

        @SuppressWarnings("resource")
        FileCollector<B000001Data> it = new FileCollector<B000001Data>(this.csvFileQueryDAO, url
                .getPath(), B000001Data.class, exceptionHandler) {
            int count = 0;

            @Override
            public Integer call() throws Exception {
                this.fo = new Future<Object>() {
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false;
                    }

                    public Object get() throws InterruptedException, ExecutionException {
                        return null;
                    }

                    public Object get(long timeout,
                            TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                        return null;
                    }

                    public boolean isCancelled() {
                        return false;
                    }

                    public boolean isDone() {
                        return false;
                    }
                };
                return super.call();
            }

            @Override
            protected void addQueue(
                    DataValueObject dataValueObject) throws InterruptedException {
                count++;
                if (count == 1) {
                    throw new RuntimeException("hoge");
                } else if (count == 2) {
                    throw new InterruptedException("hoge");
                }
                super.addQueue(dataValueObject);
            }
        };

        it.call();
    }

    /**
     * testCall002
     * @throws Exception
     */
    @Test
    public void testCall002() throws Exception {
        if (this.csvFileQueryDAO == null) {
            fail("csvFileQueryDAOがnullです。");
        }

        URL url = getClass().getClassLoader().getResource("USER_TEST.csv");
        if (logger.isDebugEnabled()) {
            if (url != null) {
                logger.debug("url.getPath() : " + url.getPath());
            } else {
                logger.debug("url.getPath() : " + null);
            }
        }

        if (url == null) {
            fail("urlがnullです。");
        }

        // int count_first = 0;
        CollectorExceptionHandler exceptionHandler = new CollectorExceptionHandlerStub();

        @SuppressWarnings("resource")
        FileCollector<B000001Data> it = new FileCollector<B000001Data>(this.csvFileQueryDAO, url
                .getPath(), B000001Data.class, exceptionHandler) {
            int count = 0;

            @Override
            public Integer call() throws Exception {
                this.fo = new Future<Object>() {
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false;
                    }

                    public Object get() throws InterruptedException, ExecutionException {
                        return null;
                    }

                    public Object get(long timeout,
                            TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                        return null;
                    }

                    public boolean isCancelled() {
                        return false;
                    }

                    public boolean isDone() {
                        return false;
                    }
                };
                return super.call();
            }

            @Override
            protected void addQueue(
                    DataValueObject dataValueObject) throws InterruptedException {
                count++;
                if (count == 1 || count == 2) {
                    throw new RuntimeException("hoge");
                } else if (count == 3) {
                    throw new InterruptedException("hoge");
                }
                super.addQueue(dataValueObject);
            }
        };

        it.call();
    }

    /**
     * testCall003
     * @throws Exception
     */
    @Test
    public void testCall003() throws Exception {
        FileQueryDAO csvFileQueryDAO = new FileQueryDAOStub();
        ;

        URL url = getClass().getClassLoader().getResource("USER_TEST.csv");
        if (logger.isDebugEnabled()) {
            if (url != null) {
                logger.debug("url.getPath() : " + url.getPath());
            } else {
                logger.debug("url.getPath() : " + null);
            }
        }

        if (url == null) {
            fail("urlがnullです。");
        }

        // int count_first = 0;
        CollectorExceptionHandler exceptionHandler = new CollectorExceptionHandlerStub();

        @SuppressWarnings("resource")
        FileCollector<B000001Data> it = new FileCollector<B000001Data>(csvFileQueryDAO, url
                .getPath(), B000001Data.class, exceptionHandler) {

            @Override
            public Integer call() throws Exception {
                this.fo = new Future<Object>() {
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false;
                    }

                    public Object get() throws InterruptedException, ExecutionException {
                        return null;
                    }

                    public Object get(long timeout,
                            TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                        return null;
                    }

                    public boolean isCancelled() {
                        return false;
                    }

                    public boolean isDone() {
                        return false;
                    }
                };
                return super.call();
            }

            @Override
            protected void addQueue(
                    DataValueObject dataValueObject) throws InterruptedException {
            }
        };

        it.call();
    }

}
