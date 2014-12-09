package jp.terasoluna.fw.collector.file;

import java.net.URL;
import java.util.List;
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
import jp.terasoluna.fw.ex.unit.testcase.DaoTestCase;
import jp.terasoluna.fw.file.dao.FileQueryDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileCollector001Test extends DaoTestCase {
    /**
     * Log.
     */
    private static Log logger = LogFactory.getLog(FileCollector001Test.class);

    private FileQueryDAO csvFileQueryDAO = null;

    private int previousThreadCount = 0;

    public void setCsvFileQueryDAO(FileQueryDAO csvFileQueryDAO) {
        this.csvFileQueryDAO = csvFileQueryDAO;
    }

    @Override
    protected void onSetUp() throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        System.gc();
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        super.onSetUp();
        this.previousThreadCount = CollectorTestUtil.getCollectorThreadCount();
    }

    @Override
    protected void onTearDown() throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        System.gc();
        if (logger.isInfoEnabled()) {
            logger.info(MemoryInfo.getMemoryInfo());
        }
        CollectorTestUtil.allInterrupt();
        super.onTearDown();
    }

    @Override
    protected void addConfigLocations(List<String> configLocations) {
        configLocations.add("jp/terasoluna/fw/collector/db/dataSource.xml");
    }

    /**
     * @throws Exception
     */
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

        Collector<B000001Data> it = new FileCollector<B000001Data>(
                this.csvFileQueryDAO, url.getPath(), B000001Data.class,
                exceptionHandler);

        it.close();
    }

    /**
     * testFileCollector002
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testFileCollector002() throws Exception {
        FileCollectorConfig config = null;

        @SuppressWarnings("unused")
        FileCollector<UserBean> dbc = null;
        try {
            dbc = new FileCollector<UserBean>(config);
            fail("失敗");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
            assertEquals("The parameter is null.", e.getMessage());
        }
    }

    /**
     * testCall001
     * @throws Exception
     */
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

        FileCollector<B000001Data> it = new FileCollector<B000001Data>(
                this.csvFileQueryDAO, url.getPath(), B000001Data.class,
                exceptionHandler) {
            int count = 0;

            @Override
            public Integer call() throws Exception {
                this.fo = new Future<Object>() {
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false;
                    }

                    public Object get() throws InterruptedException,
                                       ExecutionException {
                        return null;
                    }

                    public Object get(long timeout, TimeUnit unit)
                                                                  throws InterruptedException,
                                                                  ExecutionException,
                                                                  TimeoutException {
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
            protected void addQueue(DataValueObject dataValueObject)
                                                                    throws InterruptedException {
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

        FileCollector<B000001Data> it = new FileCollector<B000001Data>(
                this.csvFileQueryDAO, url.getPath(), B000001Data.class,
                exceptionHandler) {
            int count = 0;

            @Override
            public Integer call() throws Exception {
                this.fo = new Future<Object>() {
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false;
                    }

                    public Object get() throws InterruptedException,
                                       ExecutionException {
                        return null;
                    }

                    public Object get(long timeout, TimeUnit unit)
                                                                  throws InterruptedException,
                                                                  ExecutionException,
                                                                  TimeoutException {
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
            protected void addQueue(DataValueObject dataValueObject)
                                                                    throws InterruptedException {
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

        FileCollector<B000001Data> it = new FileCollector<B000001Data>(
                csvFileQueryDAO, url.getPath(), B000001Data.class,
                exceptionHandler) {

            @Override
            public Integer call() throws Exception {
                this.fo = new Future<Object>() {
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false;
                    }

                    public Object get() throws InterruptedException,
                                       ExecutionException {
                        return null;
                    }

                    public Object get(long timeout, TimeUnit unit)
                                                                  throws InterruptedException,
                                                                  ExecutionException,
                                                                  TimeoutException {
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
            protected void addQueue(DataValueObject dataValueObject)
                                                                    throws InterruptedException {
            }
        };

        it.call();
    }

}
