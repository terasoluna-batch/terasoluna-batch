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
 * IndexedBeanWrapperImplTest#testGetIndexedPropertyNameList06で使用する
 * スタブクラス。
 */
public class JXPathIndexedBeanWrapperImpl_JavaBeanStub05 {

    /**
     * テスト用プロパティ。
     */
    private Foo foo = null;

    /**
     * @return foo を戻します。
     */
    public Foo getFoo() {
        return foo;
    }

    /**
     * @param foo 設定する foo。
     */
    public void setFoo(Foo foo) {
        this.foo = foo;
    }

    /**
     * テスト用プロパティクラス。
     */
    public static class Foo {

        /**
         * テスト用プロパティ。
         */
        private List<Bar> bar = new ArrayList<Bar>();

        /**
         * @return bar を戻します。
         */
        public List<Bar> getBar() {
            return bar;
        }

        /**
         * @param bar 設定する bar。
         */
        public void setBar(List<Bar> bar) {
            this.bar = bar;
        }

    }

    /**
     * テスト用プロパティクラス。
     */
    public static class Bar {

        /**
         * テスト用プロパティ。
         */
        private List<String> hoge = new ArrayList<String>();

        /**
         * @return hoge を戻します。
         */
        public List<String> getHoge() {
            return hoge;
        }

        /**
         * @param hoge 設定する hoge。
         */
        public void setHoge(List<String> hoge) {
            this.hoge = hoge;
        }

    }

}
