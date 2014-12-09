package jp.terasoluna.fw.batch.executor;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

public class PlatformTransactionManagerStub02 implements PlatformTransactionManager {

    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        return new TransactionStatus() {

            public void rollbackToSavepoint(Object savepoint) throws TransactionException {
                // TODO Auto-generated method stub
            }

            public void releaseSavepoint(Object savepoint) throws TransactionException {
                // TODO Auto-generated method stub
            }

            public Object createSavepoint() throws TransactionException {
                // TODO Auto-generated method stub
                return null;
            }

            public void setRollbackOnly() {
                // TODO Auto-generated method stub
            }

            public boolean isRollbackOnly() {
                // TODO Auto-generated method stub
                return false;
            }

            public boolean isNewTransaction() {
                // TODO Auto-generated method stub
                return false;
            }

            public boolean isCompleted() {
                // TODO Auto-generated method stub
                return false;
            }

            public boolean hasSavepoint() {
                // TODO Auto-generated method stub
                return false;
            }

            public void flush() {
                // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
    }
}
