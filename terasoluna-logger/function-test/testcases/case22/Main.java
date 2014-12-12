package case22;

import java.util.Locale;

import jp.terasoluna.fw.logger.TLogger;

public class Main {
    private static final TLogger LOGGER = TLogger.getLogger(Main.class);

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        TLogger.setLocale(Locale.ENGLISH);
        LOGGER.debug("DEB001");
        LOGGER.debug("DEB002");
    }
}
