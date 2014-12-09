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

package jp.terasoluna.fw.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IndexedBeanWrapperImplTestで使用するスタブクラス。
 */
public class JXPathIndexedBeanWrapperImpl_JavaBeanStub01 {

    /**
     * テスト用プロパティ。
     */
    private Object property = null;

    /**
     * テスト用プロパティ。
     */
    private String property2 = null;
    
    /**
     * テスト用プロパティ。
     */
    private Date property3 = null;

    /**
     * テスト用プロパティ。
     */
    private List property4 = null;
    
    /**
     * テスト用プロパティ。
     */
    private int[] property5 = null;
    
    /**
     * テスト用プロパティ。
     */
    private boolean boolProperty = false;
    
    /**
     * テスト用プロパティ。
     */
    private Map map = new HashMap();
    
    /**
     * テスト用プロパティ。
     */
    private List list = new ArrayList();
    
    /**
    * テスト用プロパティ。
    */
    private Object[] objectArray = null;

    /**
     * @return property2 を戻します。
     */
    public String getProperty2() {
        return this.property2;
    }

    /**
     * @param property2 設定する property2。
     */
    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    /**
     * boolPropertyを取得する。
     * @return boolProperty。
     */
    public boolean isBoolProperty() {
        return boolProperty;
    }

    /**
     * boolPropertyを設定する
     * @param boolProperty boolProperty。
     */
    public void setBoolProperty(boolean boolProperty) {
        this.boolProperty = boolProperty;
    }

    /**
     * mapを取得する。
     * @return map。
     */
    public Map getMap() {
        return map;
    }

    /**
     * mapを設定する
     * @param map map。
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * propertyを取得する。
     * @return property。
     */
    public Object getProperty() {
        return property;
    }

    /**
     * propertyを設定する
     * @param property property。
     */
    public void setProperty(Object property) {
        this.property = property;
    }

    /**
     * listを取得する。
     * @return list。
     */
    public List getList() {
        return list;
    }

    /**
     * listを設定する
     * @param list list。
     */
    public void setList(List list) {
        this.list = list;
    }

    /**
     * property3を取得する。
     * @return property3。
     */
    public Date getProperty3() {
        return property3;
    }

    /**
     * property3を設定する
     * @param property3 property3。
     */
    public void setProperty3(Date property3) {
        this.property3 = property3;
    }

    /**
     * property4を取得する。
     * @return property4。
     */
    public List getProperty4() {
        return property4;
    }

    /**
     * property4を設定する
     * @param property4 property4。
     */
    public void setProperty4(List property4) {
        this.property4 = property4;
    }

    /**
     * property5を取得する。
     * @return property5。
     */
    public int[] getProperty5() {
        return property5;
    }

    /**
     * property5を設定する
     * @param property5 property5。
     */
    public void setProperty5(int[] property5) {
        this.property5 = property5;
    }

    /**
     * objectArrayを取得する。
     * @return objectArray objectArray
     */
    public Object[] getObjectArray() {
        return objectArray;
    }

    
    /**
     * objectArrayを設定する。
     * @param objectArray
     */
    public void setObjectArray(Object[] objectArray) {
        this.objectArray = objectArray;
    }

}
