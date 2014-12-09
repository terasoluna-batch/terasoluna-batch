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

package jp.terasoluna.fw.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * FieldChecksTestで使用するスタブクラス。
 *
 */
public class FieldChecks_JavaBeanStub01 {

    /**
     * テスト用プロパティ。
     */
    private String field = "testProperty";

    /**
     * テスト用配列プロパティ。
     */
    private String[] array = null;

    /**
     * テスト用List型プロパティ。
     */
    private List list = new ArrayList();

    /**
     * テスト用int配列プロパティ。
     */
    private int[] intArray = null;

    /**
     * テスト用JavaBean配列プロパティ。
     */
    private Object[] beanArray = null;

    /**
     * テスト用配列プロパティ。
     */
    private String[] field1 = null;

    /**
     * テスト用String型プロパティ。
     */
    private String field2 = null;

    /**
     * テスト用Collection型プロパティ。
     */
    private Collection field3 = null;

    /**
     * テスト用プリミティブ配列型プロパティ。
     */
    private int[] field4 = null;

    /**
     * テスト用配列プロパティ。
     */
    @SuppressWarnings("unused")
    private String[] field5 = null;

    /**
     * @return field を戻します。
     */
    public String getField() {
        return field;
    }

    /**
     * @param field 設定する field。
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * @return array を戻します。
     */
    public String[] getArray() {
        return array;
    }

    /**
     * @param array 設定する array。
     */
    public void setArray(String[] array) {
        this.array = array;
    }

    /**
     * @return list を戻します。
     */
    public List getList() {
        return list;
    }

    /**
     * @param list 設定する list。
     */
    public void setList(List list) {
        this.list = list;
    }

    /**
     * @return intArray を戻します。
     */
    public int[] getIntArray() {
        return intArray;
    }

    /**
     * @param intArray 設定する intArray。
     */
    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }


    /**
     * @return beanArray を戻します。
     */
    public Object[] getBeanArray() {
        return beanArray;
    }

    /**
     * @param beanArray 設定する beanArray。
     */
    public void setBeanArray(Object[] beanArray) {
        this.beanArray = beanArray;
    }

    /**
     * @return field1 を戻します。
     */
    public String[] getField1() {
        return field1;
    }

    /**
     * @param field1 設定する field1。
     */
    public void setField1(String[] field1) {
        this.field1 = field1;
    }

    /**
     * @return field2 を戻します。
     */
    public String getField2() {
        return field2;
    }

    /**
     * @param field2 設定する field2。
     */
    public void setField2(String field2) {
        this.field2 = field2;
    }

    /**
     * @return field3 を戻します。
     */
    public Collection getField3() {
        return field3;
    }

    /**
     * @param field3 設定する field3。
     */
    public void setField3(Collection field3) {
        this.field3 = field3;
    }

    /**
     * @return field4 を戻します。
     */
    public int[] getField4() {
        return field4;
    }

    /**
     * @param field4 設定する field4。
     */
    public void setField4(int[] field4) {
        this.field4 = field4;
    }

    /**
     * @return field5 を戻します。
     */
    public String[] getField5() {
        throw new RuntimeException();
    }

    /**
     * @param field5 設定する field5。
     */
    public void setField5(String[] field5) {
        this.field5 = field5;
    }

}
