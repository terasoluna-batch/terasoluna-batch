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
 * <br><br>
 * 
 * testEscapeMapPropertyXX()メソッドで使用する。<br>
 * testEextractIncremantIndexStringIntXX()メソッドで使用する。<br>
 *
 */
public class JXPathIndexedBeanWrapperImplStub01 extends
        JXPathIndexedBeanWrapperImpl {
    
    /**
     * extractIndexスタブメソッドの入力確認用。
     */
    public String extractIndexParam1 = null;
    
    /**
     * extractIndexスタブメソッドの戻り値設定用。
     */
    public String extractIndexResult = null;
    
    /**
     * コンストラクタ。
     * @param obj ラップするBean
     */
    public JXPathIndexedBeanWrapperImplStub01(Object obj) {
        super(obj);
    }
    
    /**
     * Map型属性のプロパティ名を取り出すスタブメソッド。
     * @param property Javaプロパティ名。
     * @return String XPath。 
     */
    @Override
    protected String extractMapPropertyName(String property) {
        // 動的に戻り値を作成。
        return property + "Name";
    }
    
    /**
     * Map型属性のキー名を取り出すスタブメソッド。
     * @param property Javaプロパティ名。
     * @return String XPath。 
     */
    @Override
    protected String extractMapPropertyKey(String property) {
        // 動的に戻り値を作成。
        return property + "Key";
    }
    
    
    /**
     * 配列インデックスを取得するスタブメソッド。
     * @param property プロパティ名。
     * @return 配列インデックス。
     */
    @Override
    protected String extractIndex(String property) {
        // 入力確認用メンバに格納。
        this.extractIndexParam1 = property;
        
        // 戻り値設定用メンバの値を返す。
        return this.extractIndexResult;
    }

    
}
