package common;

import jp.terasoluna.fw.exception.TException;
import jp.terasoluna.fw.exception.TRuntimeException;

public class Main28_33$35 {
    public static void main(String[] args) {
        try {
            throw new TException("errors.required", 100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            throw new TRuntimeException("DEB001");
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        try {
            throw new TRuntimeException("nothing");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
