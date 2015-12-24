/*
 * Copyright (c) 2011 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.annotation.util;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * GenericBeanFactoryAccessorの拡張クラス。
 * <p>
 * アノテーションの型でBeanを探索するメソッドを 親コンテキストからもbean取得できるように変更。
 * singleton(デフォルト)のbeanにJDKプロキシがついていても、アノテーションの検査ができるように変更。
 * </p>
 * @see org.springframework.beans.factory.BeanFactoryUtils
 */
public class GenericBeanFactoryAccessorEx {

    private ListableBeanFactory beanFactory;
    /**
     * コンストラクタ
     * @param beanFactory Beanファクトリオブジェクト
     */
    public GenericBeanFactoryAccessorEx(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 指定されたアノテーションをもとに{@code BeanFactory}内に該当するオブジェクトを検索する。
     * @param annotationType アノテーション型
     * @return Bean名をキーとし、{@code BeanFactory}から取得可能なオブジェクトを値とした{@code Map}
     */
    public Map<String, Object> getBeansWithAnnotation(
            Class<? extends Annotation> annotationType) {
        Map<String, Object> results = new LinkedHashMap<String, Object>();
        String[] beanNames = BeanFactoryUtils
                .beanNamesIncludingAncestors(this.beanFactory);
        for (String beanName : beanNames) {
            if (findAnnotationOnBean(beanName, annotationType) != null) {
                results.put(beanName, this.beanFactory.getBean(beanName));
            }
        }
        return results;
    }

    /**
     * {@inheritDoc}
     */
    public <A extends Annotation> A findAnnotationOnBean(String beanName,
            Class<A> annotationType) {
        Object object = this.beanFactory.getBean(beanName);
        Class<?> handlerType = null;
        if (AopUtils.isAopProxy(object)) {
            handlerType = AopUtils.getTargetClass(object);
        } else {
            handlerType = object.getClass();
        }
        A ann = AnnotationUtils.findAnnotation(handlerType, annotationType);
        if (ann == null && this.beanFactory instanceof ConfigurableBeanFactory
                && this.beanFactory.containsBeanDefinition(beanName)) {
            ConfigurableBeanFactory cbf = (ConfigurableBeanFactory) this.beanFactory;
            BeanDefinition bd = cbf.getMergedBeanDefinition(beanName);
            if (bd instanceof AbstractBeanDefinition) {
                AbstractBeanDefinition abd = (AbstractBeanDefinition) bd;
                if (abd.hasBeanClass()) {
                    Class<?> beanClass = abd.getBeanClass();
                    ann = AnnotationUtils.findAnnotation(beanClass,
                            annotationType);
                }
            }
        }
        return ann;
    }
}
