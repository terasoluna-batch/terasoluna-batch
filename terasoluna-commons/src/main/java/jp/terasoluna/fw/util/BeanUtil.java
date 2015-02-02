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

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;


/**
 *  Bean関連のユーティリティクラス。
 *
 * @see jp.terasoluna.fw.util.PropertyAccessException
 *
 */
public final class BeanUtil {

    /**
     * 指定したJavaBeanのプロパティに値を格納する。
     *
     * @param bean
     *            値の格納先とするJavaBean
     * @param property
     *            JavaBeanのプロパティ
     * @param value
     *            格納する値
     * @throws PropertyAccessException
     *             値格納時に発生した例外をラップした例外
     */
    public static void setBeanProperty(Object bean, String property,
            Object value) throws PropertyAccessException {

        try {
            // 入力値設定メソッドを実行
            PropertyUtils.setProperty(bean, property, value);
        } catch (IllegalArgumentException e) {
            throw new PropertyAccessException(e);
        } catch (IllegalAccessException e) {
            throw new PropertyAccessException(e);
        } catch (InvocationTargetException e) {
            throw new PropertyAccessException(e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new PropertyAccessException(e);
        }
    }

    /**
     * 指定したJavaBeanのプロパティから値を取得する。
     *
     * @param bean
     *            値の取得元とするJavaBean
     * @param property
     *            JavaBeanのプロパティ
     * @return value 取得した値
     * @throws PropertyAccessException
     *             値取得時に発生した例外をラップした例外
     */
    public static Object getBeanProperty(Object bean, String property)
            throws PropertyAccessException {

        Object value = null;
        try {
            value = PropertyUtils.getProperty(bean, property);
        } catch (IllegalArgumentException e) {
            throw new PropertyAccessException(e);
        } catch (IllegalAccessException e) {
            throw new PropertyAccessException(e);
        } catch (InvocationTargetException e) {
            throw new PropertyAccessException(e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new PropertyAccessException(e);
        }
        return value;
    }
    
    /**
     * 指定したJavaBeanのプロパティから型を取得する。
     * @param bean
     *            値の取得元とするJavaBean
     * @param property
     *            JavaBeanのプロパティ
     * @return 属性のクラス。
     * @throws PropertyAccessException 値取得時に発生した例外をラップした例外
     */
    public static Class getBeanPropertyType(Object bean, String property) 
        throws PropertyAccessException {
        try {
            Class type = null;
            if (bean instanceof DynaBean) {
                DynaProperty descriptor = ((DynaBean) bean).getDynaClass()
                    .getDynaProperty(property);
                if (descriptor != null){
                    type = descriptor.getType();
                }
            }
            else{
                type = PropertyUtils.getPropertyType(bean, property);
            }
            return type;
        } catch (IllegalArgumentException e) {
            throw new PropertyAccessException(e);
        } catch (IllegalAccessException e) {
            throw new PropertyAccessException(e);
        } catch (InvocationTargetException e) {
            throw new PropertyAccessException(e);
        } catch (NoSuchMethodException e) {
            throw new PropertyAccessException(e);
        }
    }
}

