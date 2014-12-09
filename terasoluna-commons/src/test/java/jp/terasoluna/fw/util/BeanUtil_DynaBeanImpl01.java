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

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;

/**
 * {@link BeanUtil}をテストするための{@link DynaClass}実装クラス。
 * 
 * @see org.apache.commons.beanutils.DynaBean
 * @see jp.terasoluna.fw.util.BeanUtilTest
 */
public class BeanUtil_DynaBeanImpl01 implements DynaBean {
    
    /**
     * dynaClass。
     */
    private DynaClass dynaClass = null;

    /**
     * コンストラクタ
     * 
     * @param dynaClass
     */
    public BeanUtil_DynaBeanImpl01(DynaClass dynaClass) {
        super();
        this.dynaClass = dynaClass;
    }

    /** 
     * dynaClassを返却する。
     * @return dynaClass
     */
    public DynaClass getDynaClass() {
        return dynaClass;
    }
    
    /* 
     * @see org.apache.commons.beanutils.DynaBean#contains(java.lang.String, java.lang.String)
     */
    public boolean contains(String arg0, String arg1) {
        return false;
    }

    /* 
     * @see org.apache.commons.beanutils.DynaBean#get(java.lang.String)
     */
    public Object get(String arg0) {
        return null;
    }

    /*
     * @see org.apache.commons.beanutils.DynaBean#get(java.lang.String, int)
     */
    public Object get(String arg0, int arg1) {
        return null;
    }

    /* 
     * @see org.apache.commons.beanutils.DynaBean#get(java.lang.String, java.lang.String)
     */
    public Object get(String arg0, String arg1) {
        return null;
    }

    /*
     * @see org.apache.commons.beanutils.DynaBean#remove(java.lang.String, java.lang.String)
     */
    public void remove(String arg0, String arg1) {

    }

    /*
     * @see org.apache.commons.beanutils.DynaBean#set(java.lang.String, java.lang.Object)
     */
    public void set(String arg0, Object arg1) {

    }

    /* 
     * @see org.apache.commons.beanutils.DynaBean#set(java.lang.String, int, java.lang.Object)
     */
    public void set(String arg0, int arg1, Object arg2) {

    }

    /* 
     * @see org.apache.commons.beanutils.DynaBean#set(java.lang.String, java.lang.String, java.lang.Object)
     */
    public void set(String arg0, String arg1, Object arg2) {

    }

}
