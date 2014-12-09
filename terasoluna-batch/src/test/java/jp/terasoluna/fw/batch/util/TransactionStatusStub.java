package jp.terasoluna.fw.batch.util;

import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

public class TransactionStatusStub implements TransactionStatus {

    private String name;
    private boolean rollbackOnly = false;
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

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

        return rollbackOnly;
    }

    public void setRollbackOnly() {
        this.rollbackOnly = true;
    }

    public Object createSavepoint() throws TransactionException {

        return new Object();
    }

    public void releaseSavepoint(Object savepoint) throws TransactionException {

    }

    public void rollbackToSavepoint(Object savepoint)
                                                     throws TransactionException {

    }

}
