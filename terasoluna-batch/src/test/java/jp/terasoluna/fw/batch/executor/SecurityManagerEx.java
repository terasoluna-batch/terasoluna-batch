package jp.terasoluna.fw.batch.executor;

import java.security.Permission;

public class SecurityManagerEx extends SecurityManager {
    
    public void checkPermission(Permission permission) {
        if ("exitVM".equals(permission.getName())) {
        }
    }

    public void checkExit(int status) {
        throw new ExitException(status);
    }

    @SuppressWarnings("serial")
    public static class ExitException extends SecurityException {
        public int state = 0;

        public ExitException(int state) {
            this.state = state;
        }
    }

}
