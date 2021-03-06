/*
 * Copyright (c) 2012 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.terasoluna.fw.batch.unit.util;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

/**
 * 何もしないトランザクションマネージャです。
 * 
 * 
 */
public class MockTransactionManager implements PlatformTransactionManager {

    public TransactionStatus getTransaction(TransactionDefinition definition)
            throws TransactionException {
        return null;
    }

    public void commit(TransactionStatus status) throws TransactionException {
    }

    public void rollback(TransactionStatus status) throws TransactionException {
    }

}
