package common;

import ext.ExtLib;
import jp.terasoluna.fw.exception.TException;
import jp.terasoluna.fw.exception.TRuntimeException;

public class Main37_41 {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
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
        
        try {
            ExtLib.throwException();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
