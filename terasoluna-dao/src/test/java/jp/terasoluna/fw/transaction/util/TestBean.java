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

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;

/**
 * beanやAOPフレームワークなどの試験に使われるTestBean。
 */
public class TestBean implements BeanNameAware, BeanFactoryAware, ITestBean,
        Comparable {

    private String beanName;

    private BeanFactory beanFactory;

    private String name;

    public TestBean() {
    }

    public TestBean(String name) {
        this.name = name;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @see ITestBeanクラスのexceptional(Throwable)
     */
    public void exceptional(Throwable t) throws Throwable {
        if (t != null) {
            throw t;
        }
    }

    public int compareTo(Object other) {
        if (this.name != null && other instanceof TestBean) {
            return this.name.compareTo(((TestBean) other).getName());
        }
        return 1;
    }

}
