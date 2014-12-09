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

package jp.terasoluna.fw.util;

import java.io.Serializable;

public class ProxyUtil_JavaBeanStub01 implements Serializable, ProxyUtil_IJavaBeanStub01 {
    /**
     * シリアルバージョンID。
     */
    private static final long serialVersionUID = -3029460937604647413L;
    private String name = "";

    /* (非 Javadoc)
     * @see jp.terasoluna.fw.util.ProxyUtil_IJavaBeanStub01#getName()
     */
    public String getName() {
        return name;
    }

    /* (非 Javadoc)
     * @see jp.terasoluna.fw.util.ProxyUtil_IJavaBeanStub01#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }
}
