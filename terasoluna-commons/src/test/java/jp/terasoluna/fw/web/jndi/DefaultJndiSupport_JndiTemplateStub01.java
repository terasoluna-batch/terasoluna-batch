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

package jp.terasoluna.fw.web.jndi;

import javax.naming.NamingException;

import org.springframework.jndi.JndiTemplate;

/**
 * DefaultJndiSupportTestクラスで利用する。
 * 
 */
public class DefaultJndiSupport_JndiTemplateStub01 extends JndiTemplate {

    private boolean isCallRebind = false;
    private boolean isCallUnbind = false;
    private boolean isCallLookup = false;
    
    private String jndiNameToUse = null;
    private Object obj = null;

    @Override
    public void rebind(String name, Object object) throws NamingException {
        this.isCallRebind = true;
        this.jndiNameToUse = name;
        this.obj = object;
    }

    @Override
    public Object lookup(String name) throws NamingException {
        this.isCallLookup = true;
        this.jndiNameToUse = name;
        return "return";
    }

    @Override
    public void unbind(String name) throws NamingException {
        this.isCallUnbind = true;
        this.jndiNameToUse = name;
    }

    public boolean isCallLookup() {
        return isCallLookup;
    }

    public void setCallLookup(boolean isCallLookup) {
        this.isCallLookup = isCallLookup;
    }

    public boolean isCallUnbind() {
        return isCallUnbind;
    }

    public void setCallUnbind(boolean isCallUnbind) {
        this.isCallUnbind = isCallUnbind;
    }

    public boolean isCallRebind() {
        return isCallRebind;
    }

    public void setCallRebind(boolean isCallRebind) {
        this.isCallRebind = isCallRebind;
    }

    public String getJndiNameToUse() {
        return jndiNameToUse;
    }

    public void setJndiNameToUse(String jndiNameToUse) {
        this.jndiNameToUse = jndiNameToUse;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}