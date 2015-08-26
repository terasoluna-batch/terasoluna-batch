package jp.terasoluna.fw.batch.executor;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

public class PlatformTransactionManagerStub02 implements PlatformTransactionManager {

    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        return new TransactionStatus() {

            public void rollbackToSavepoint(Object savepoint) throws TransactionException {
            }

            public void releaseSavepoint(Object savepoint) throws TransactionException {
            }

            public Object createSavepoint() throws TransactionException {
                return null;
            }

            public void setRollbackOnly() {
            }

            public boolean isRollbackOnly() {
                return false;
            }

            public boolean isNewTransaction() {
                return false;
            }

            public boolean isCompleted() {
                return false;
            }

            public boolean hasSavepoint() {
                return false;
            }

            public void flush() {
            }
        };
    }

    public void commit(TransactionStatus status) throws TransactionException {
        throw new TransactionException("コミット確認用") {
            /**
             * 
             */
            private static final long serialVersionUID = 2554564900714359430L;
        };
    }

    public void rollback(TransactionStatus status) throws TransactionException {
    }
}
