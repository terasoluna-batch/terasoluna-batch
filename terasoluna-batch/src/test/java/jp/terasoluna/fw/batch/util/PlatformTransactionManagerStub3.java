package jp.terasoluna.fw.batch.util;

import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

public class PlatformTransactionManagerStub3 implements
                                            PlatformTransactionManager {
    
    private boolean failStartTx = false;
    
    private boolean calledGetTransaction = false;
    
    private boolean calledRollback = false;
    
    public void setFailStartTx(boolean failStartTx) {
        this.failStartTx = failStartTx;
    }

    public boolean wasCalledGetTransaction() {
        return calledGetTransaction;
    }

    public boolean wasCalledRollback() {
        return calledRollback;
    }

    public void commit(TransactionStatus status) throws TransactionException {

    }

    public TransactionStatus getTransaction(TransactionDefinition definition)
                                                                             throws TransactionException {
        calledGetTransaction = true;

        if (failStartTx) {
            throw new CannotCreateTransactionException("test");
        }

        return new TransactionStatus() {

            public void flush() {

            }

            public boolean hasSavepoint() {

                return false;
            }

            public boolean isCompleted() {

                return false;
            }

            public boolean isNewTransaction() {

                return false;
            }

            public boolean isRollbackOnly() {

                return false;
            }

            public void setRollbackOnly() {

            }

            public Object createSavepoint() throws TransactionException {

                return null;
            }

            public void releaseSavepoint(Object savepoint)
                                                          throws TransactionException {

            }

            public void rollbackToSavepoint(Object savepoint)
                                                             throws TransactionException {

            }
        };
    }

    public void rollback(TransactionStatus status) throws TransactionException {
        calledRollback = true;
    }

}
