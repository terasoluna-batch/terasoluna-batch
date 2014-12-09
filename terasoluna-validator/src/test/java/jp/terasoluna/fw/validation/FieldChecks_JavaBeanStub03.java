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

package jp.terasoluna.fw.validation;

import java.lang.reflect.InvocationTargetException;

/**
 * {@link FieldChecksExtend}をテストするためのJavaBean。
 * 
 *
 */
public class FieldChecks_JavaBeanStub03 {

    /**
     * field1。
     */
    @SuppressWarnings("unused")
    private String field1 = null;

    /**
     * field2。
     */
    @SuppressWarnings("unused")
    private String field2 = null;

    /**
     * InvocationTargetException をスローする。
     * 
     * @return field1。
     * @throws InvocationTargetException スローされる例外。
     */
    public String getField1() throws InvocationTargetException {
        throw new InvocationTargetException(new RuntimeException());
    }

    /**
     * field1 を設定する。
     * 
     * @param field1 設定する field1。
     */
    public void setField1(String field1) {
        this.field1 = field1;
    }

    /**
     * InvocationTargetException をスローする。
     * 
     * @return field2。
     * @throws InvocationTargetException スローされる例外。
     */
    public String getField2() throws InvocationTargetException {
        throw new InvocationTargetException(new RuntimeException());
    }

    /**
     * field2 を設定する。
     * 
     * @param field2 設定する field2。
     */
    public void setField2(String field2) {
        this.field2 = field2;
    }
}
