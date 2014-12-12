package case34;

import org.apache.log4j.MDC;

import jp.terasoluna.fw.exception.TException;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        try {
            throw new TException("WAR001");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(MDC.get("keyword"));
    }
}
