package case36;

import java.util.Locale;

import jp.terasoluna.fw.exception.ExceptionConfig;
import jp.terasoluna.fw.exception.TException;

public class Main {
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        ExceptionConfig.setLocale(Locale.ENGLISH);
        try {
            throw new TException("DEB001");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
