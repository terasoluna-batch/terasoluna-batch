package common;

import jp.terasoluna.fw.logger.TLogger;

public class Main {
    private static final TLogger LOGGER = TLogger.getLogger(Main.class);

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        LOGGER.error("errors.required", "çÄî‘" + System.getenv("TESTNO"));
        LOGGER.debug("DEB001");
        LOGGER.debug("DEB002");
        LOGGER.fatal("nothing");
    }
}
