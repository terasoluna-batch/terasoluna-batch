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
import java.util.List;

/**
 * IndexedBeanWrapperImplTest#testGetIndexedPropertyNameList04で使用する
 * スタブクラス。
 */
public class JXPathIndexedBeanWrapperImpl_JavaBeanStub04 {

    /**
     * テスト用プロパティ。
     */
    private List<Foo> foo = new ArrayList<Foo>();
    
    /**
     * テスト用プロパティ。
     */
    private Foo[] foos = null;

    /**
     * @return foo を戻します。
     */
    public List<Foo> getFoo() {
        return foo;
    }

    /**
     * @param foo 設定する foo。
     */
    public void setFoo(List<Foo> foo) {
        this.foo = foo;
    }
    
    /**
     * foosを取得する。
     * @return foos。
     */
    public Foo[] getFoos() {
        return foos;
    }

    /**
     * foosを設定する
     * @param foos foos。
     */
    public void setFoos(Foo[] foos) {
        this.foos = foos;
    }

    /**
     * テスト用プロパティクラス。
     */
    public static class Foo {

        /**
         * テスト用プロパティ。
         */
        private Bar bar = null;

        /**
         * テスト用プロパティ。
         */
        private String property = "";
        
        /**
         * @return bar を戻します。
         */
        public Bar getBar() {
            return bar;
        }

        /**
         * @param bar 設定する bar。
         */
        public void setBar(Bar bar) {
            this.bar = bar;
        }

        /**
         * propertyを取得する。
         * @return property。
         */
        public String getProperty() {
            return property;
        }

        /**
         * propertyを設定する
         * @param property property。
         */
        public void setProperty(String property) {
            this.property = property;
        }

    }

    /**
     * テスト用プロパティクラス。
     */
    public static class Bar {

        /**
         * テスト用プロパティ。
         */
        private String hoge = null;
        
        /**
         * テスト用プロパティ。
         */
        private String[] hogeArray = null;

        /**
         * @return hoge を戻します。
         */
        public String getHoge() {
            return hoge;
        }

        /**
         * @param hoge 設定する hoge。
         */
        public void setHoge(String hoge) {
            this.hoge = hoge;
        }

        /**
         * hogeArrayを取得する。
         * @return hogeArray
         */
        public String[] getHogeArray() {
            return hogeArray;
        }

        /**
         * hogeArrayを設定する。
         * @param hogeArray hogeArray
         */
        public void setHogeArray(String[] hogeArray) {
            this.hogeArray = hogeArray;
        }

    }

    

}
