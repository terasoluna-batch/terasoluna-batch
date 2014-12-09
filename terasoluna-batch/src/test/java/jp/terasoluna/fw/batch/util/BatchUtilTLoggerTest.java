package jp.terasoluna.fw.batch.util;

import jp.terasoluna.fw.logger.TLogger;

import org.apache.commons.logging.Log;

public class BatchUtilTLoggerTest extends BatchUtilTestBase {

    @Override
    Log getLog() {
        return TLogger.getLogger(BatchUtilTLoggerTest.class);
    }

}
