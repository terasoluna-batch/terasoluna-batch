package jp.terasoluna.fw.batch.util;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

public class PlatformTransactionManagerStub2 implements
                                            PlatformTransactionManager {

    public void commit(TransactionStatus status) throws TransactionException {

    }

    public TransactionStatus getTransaction(TransactionDefinition definition)
                                                                             throws TransactionException {

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
        throw new TransactionException("hoge") {
            private static final long serialVersionUID = 1L;
        };
    }

}
