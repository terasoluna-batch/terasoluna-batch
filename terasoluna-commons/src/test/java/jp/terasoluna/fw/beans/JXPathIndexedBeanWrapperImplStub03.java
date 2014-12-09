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
 */
public class JXPathIndexedBeanWrapperImplStub03 extends
        JXPathIndexedBeanWrapperImpl {
 
    /**
     * isMapPropertyスタブメソッドの入力確認用メンバ。
     */
    public String isMapPropertyParam1 = null;
    
    /**
     * isMapPropertyスタブメソッドの戻り値設定用メンバ。
     */
    public boolean isMapPropertyResult = false;
    
    /**
     * extractIncrementIndexスタブメソッドの入力確認用。
     */
    public String extractIncrementIndexParam1 = null;
    
    /**
     * extractIncrementIndexスタブメソッドの戻り値設定用。
     */
    public String extractIncrementIndexResult = null;
    
    
    /**
     * コンストラクタ。
     * @param obj ラップするBean
     */
    public JXPathIndexedBeanWrapperImplStub03(Object obj) {
        super(obj);
    }

    /**
     * Map型属性かどうか判断するスタブメソッド。
     * @param property Javaプロパティ名。
     * @return boolean trueを返す。 
     */
    @Override
    protected boolean isMapProperty(String property) {
        // 入力確認用メンバに格納。
        this.isMapPropertyParam1 = property;
        
        // 戻り値設定用メンバの値を返す。
        return this.isMapPropertyResult;
    }
    

    /**
     * MapプロパティをXPath形式にエスケープするスタブメソッド。
     * @param property Javaプロパティ名。
     * @return String XPath。 
     */
    @Override
    protected String escapeMapProperty(String property) {
        // 動的に戻り値を作成。
        return property+"[@name='key']";
    }
    
    /**
     * 属性名を取り出すスタブメソッド。
     * @param property XPathのノード。
     * @return 属性名。
     */
    @Override
    protected String extractAttributeName(String property) {
        // 動的に戻り値を作成。
        return property+"Attribute";
    }
    
    /**
     * インクリメントされた添え字を取り出すスタブメソッド。
     * @param property Javaプロパティ名。
     * @return String XPath形式の添え字。 
     */
    @Override
    protected String extractIncrementIndex(String property) {
        // 入力確認用メンバに格納。
        this.extractIncrementIndexParam1 = property;
        
        // 戻り値設定用メンバの値を返す。
        return extractIncrementIndexResult;
    }

    

    

}
