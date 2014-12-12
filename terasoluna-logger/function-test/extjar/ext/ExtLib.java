package ext;

import jp.terasoluna.fw.exception.TRuntimeException;
import jp.terasoluna.fw.logger.TLogger;

public class ExtLib {
    private static final TLogger LOGGER = TLogger.getLogger(ExtLib.class);

    @SuppressWarnings("serial")
    public static class ExtException extends TRuntimeException {

        public ExtException(String messageId, Object... args) {
            super(messageId, args);
        }
    }

    public static void ext() {
        LOGGER.info("EXT001");
    }

    public static void throwException() throws ExtException {
        throw new ExtException("EXT002", System.getenv("TESTNO"));
    }
}
