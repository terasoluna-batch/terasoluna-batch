package jp.terasoluna.fw.batch.blogic;

import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

public class TransactionStatusStub implements TransactionStatus {

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

    public void releaseSavepoint(Object savepoint) throws TransactionException {

    }

    public void rollbackToSavepoint(Object savepoint)
                                                     throws TransactionException {

    }

	public void flush() {


	}

}
