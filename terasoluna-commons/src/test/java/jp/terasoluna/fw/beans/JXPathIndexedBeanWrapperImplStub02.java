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

/**
 * JXPathIndexedBeanWrapperImplTestで使用するスタブクラス。
 * 
 * 
 * testExtractIncrementIndexStringXX()メソッドで使用する。
 *
 */
public class JXPathIndexedBeanWrapperImplStub02 extends
        JXPathIndexedBeanWrapperImpl {
 
    
    /**
     * コンストラクタ。
     * @param obj ラップするBean
     */
    public JXPathIndexedBeanWrapperImplStub02(Object obj) {
        super(obj);
    }

    /**
     * インクリメントされた添え字を取り出すスタブメソッド。
     * @param property Javaプロパティ名。
     * @return String XPath形式の添え字。 
     */
    @Override
    protected String extractIncrementIndex(String property,int increment) {
        // 動的に戻り値を作成。
        return property + ":" + increment;
    }
    

}
