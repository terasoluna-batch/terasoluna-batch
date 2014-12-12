package common;

import ext.ExtLib;
import jp.terasoluna.fw.logger.TLogger;

public class Main23_27 {
    private static final TLogger LOGGER = TLogger.getLogger(Main23_27.class);

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        LOGGER.error("errors.required", "çÄî‘" + System.getenv("TESTNO"));
        LOGGER.debug("DEB001");
        LOGGER.debug("DEB002");
        LOGGER.fatal("nothing");
        LOGGER.warn("WAR001");
        ExtLib.ext();
    }
}
