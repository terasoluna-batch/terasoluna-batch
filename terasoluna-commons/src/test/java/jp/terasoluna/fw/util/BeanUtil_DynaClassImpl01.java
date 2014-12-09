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
import org.apache.commons.beanutils.DynaProperty;

/**
 * {@link BeanUtil}をテストするための{@link DynaClass}実装クラス。
 * 
 * @see org.apache.commons.beanutils.DynaClass
 * @see jp.terasoluna.fw.util.BeanUtilTest
 */
public class BeanUtil_DynaClassImpl01 implements DynaClass {

    /**
     * dynaProperty。
     */
    private DynaProperty dynaProperty = null; 
    
    /**
     * コンストラクタ
     */
    public BeanUtil_DynaClassImpl01() {
        super();
    }

    /*
     * @see org.apache.commons.beanutils.DynaClass#getName()
     */
    public String getName() {
        return null;
    }

    /**
     * dynaPropertyを返却する。 
     * @param arg0 パラメータ
     */
    public DynaProperty getDynaProperty(String arg0) {
        return dynaProperty;
    }
    
    /** 
     * dynaPropertyを設定する。 
     * @param property DynaProperty型の属性
     */
    public void setDynaProperty(DynaProperty property) {
        this.dynaProperty = property;
    }     

    /* 
     * @see org.apache.commons.beanutils.DynaClass#getDynaProperties()
     */
    public DynaProperty[] getDynaProperties() {
        return null;
    }

    /* 
     * @see org.apache.commons.beanutils.DynaClass#newInstance()
     */
    public DynaBean newInstance() throws IllegalAccessException,
            InstantiationException {
        return null;
    }

}
