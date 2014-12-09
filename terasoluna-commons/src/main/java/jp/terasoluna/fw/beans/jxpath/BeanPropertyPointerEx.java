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

import org.apache.commons.jxpath.JXPathBeanInfo;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPropertyPointer;

/**
 * null値を扱うためのBeanプロパティポインタ拡張クラス。
 * 
 * <p>デフォルトのBeanプロパティポインタでは、
 * final指定されていないクラス（Object,Date、ArrayList、etc）の属性に
 * nullが入っていた場合、値がないものとして扱われる。
 * nullを取得したい場合、本クラスを使用する必要がある。</p>
 * 
 * @see jp.terasoluna.fw.beans.jxpath.BeanPointerFactoryEx
 */
public class BeanPropertyPointerEx extends BeanPropertyPointer {

    /**
     * シリアルバージョンID。
     */
    private static final long serialVersionUID = -4617365154553497991L;

    /**
     * コンストラクタ。
     * @param parent 親となるBeanポインタ
     * @param beanInfo ターゲットのBeanの情報
     */
    public BeanPropertyPointerEx(NodePointer parent, JXPathBeanInfo beanInfo) {
        super(parent, beanInfo);
    }

    /**
     * 要素数を取得する。
     * @return 要素数
     */
    @Override
    public int getLength() {
        int length = super.getLength();
        
        // 要素の値がnullの場合、要素数を１とする
        if (length == 0 && getBaseValue() == null) { 
            return 1;
        }
        return length;
    }
    
    /**
     * 配列かどうか判断する。
     * nullの場合、配列とはみなさない。
     * @return 配列の場合、trueを返す。それ以外はfalseを返す。
     */
    @Override
    public boolean isCollection() {
        if (getBaseValue() == null) {
        	return false;
        }
        return super.isCollection();
    }
}
