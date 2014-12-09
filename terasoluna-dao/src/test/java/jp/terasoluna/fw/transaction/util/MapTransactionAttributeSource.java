/*
 * Copyright (c) 2007 NTT DATA Corporation
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

package jp.terasoluna.fw.transaction.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttribute;

/**
 * AbstractFallbackTransactionAttributeSourceÇ©ÇÁfallbackÇÃêUÇÈïëÇ¢Çåpè≥ÇµÇ‹Ç∑ÅB
 */
public class MapTransactionAttributeSource extends
        AbstractFallbackTransactionAttributeSource {

    /** Map from Method or Clazz to TransactionAttribute */
    private final Map attributeMap = new HashMap();

    @SuppressWarnings("unchecked")
    public void register(Method m, TransactionAttribute txAtt) {
        this.attributeMap.put(m, txAtt);
    }

    @SuppressWarnings("unchecked")
    public void register(Class clazz, TransactionAttribute txAtt) {
        this.attributeMap.put(clazz, txAtt);
    }

    @Override
    protected TransactionAttribute findTransactionAttribute(Method method) {
        return (TransactionAttribute) this.attributeMap.get(method);
    }

    @Override
    protected TransactionAttribute findTransactionAttribute(Class clazz) {
        return (TransactionAttribute) this.attributeMap.get(clazz);
    }

}
