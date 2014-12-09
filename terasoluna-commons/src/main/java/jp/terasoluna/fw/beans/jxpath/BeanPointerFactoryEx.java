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

package jp.terasoluna.fw.beans.jxpath;

import java.util.Locale;

import org.apache.commons.jxpath.JXPathBeanInfo;
import org.apache.commons.jxpath.JXPathIntrospector;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointerFactory;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;

/**
 * Beanポインタファクトリの拡張クラス。
 * {@link #getOrder()}にてBeanPointerFactoryより先にロードされる
 * 必要がある。
 *
 */
public class BeanPointerFactoryEx extends BeanPointerFactory {

    /**
     * Beanポインタファクトリがソートされる順番。
     */
    public static final int BEAN_POINTER_FACTORY_EX_ORDER = 850;
    
    /**
     * ソート順を取得する。
     * @return ソート順
     */
    @Override
    public int getOrder() {
        return BEAN_POINTER_FACTORY_EX_ORDER;
    }
    
    /**
     * ノードポインタを生成する。
     * @param name QName
     * @param bean ターゲットとなるBean
     * @param locale ロケール
     * @return Beanポインタ
     */
    @Override
    public NodePointer createNodePointer(
        QName name, Object bean, Locale locale) {
        JXPathBeanInfo bi = JXPathIntrospector.getBeanInfo(bean.getClass());
        return new BeanPointerEx(name, bean, bi, locale);
    }

    /**
     * ノードポインタを生成する。
     * @param parent 親のポインタ
     * @param name QName
     * @param bean ターゲットとなるBean
     * @return Beanポインタ
     */
    @Override
    public NodePointer createNodePointer(
        NodePointer parent, QName name, Object bean) {
        if (bean == null) {
            return new NullPointer(parent, name);
        }
        JXPathBeanInfo bi = JXPathIntrospector.getBeanInfo(bean.getClass());
        return new BeanPointerEx(parent, name, bean, bi);
    }
}
