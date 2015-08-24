package jp.terasoluna.fw.batch.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

public class PlatformTransactionManagerStub4 implements
        PlatformTransactionManager {

    private List<TransactionStatus> statusList = new ArrayList<TransactionStatus>();

    public TransactionStatus getTransaction(TransactionDefinition definition)
            throws TransactionException {
        return null;
    }

    public void commit(TransactionStatus status) throws TransactionException {
        statusList.add(status);
    }

    public void rollback(TransactionStatus status) throws TransactionException {
        statusList.add(status);
    }

    public List<TransactionStatus> getStatusList() {
        return statusList;
    }
}
