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

/**
 *
 */
public class JXPathIndexedBeanWrapperImplStub04 extends
        JXPathIndexedBeanWrapperImpl {

    /**
     * 引数を保持する属性。
     */
    public ArrayList<String> extractAttributeNameArg1 = new ArrayList<String>();

    /**
     * 引数を保持する属性。
     */
    public ArrayList<String> extractMapAttributeNameArg1 = new ArrayList<String>();
    
    private String[] extractMapAttributeNameReturnValue = { "aaa", "ddd" };
    
    private int extractMapAttributeNameInnerCount = 0;

    /**
     * 引数を保持する属性。
     */
    public ArrayList<String> extractMapKeyArg1 = new ArrayList<String>();
    private String[] extractMapKeyReturnValue = { "bbb", "eee" };
    private int extractMapKeyInnerCount = 0;

    /**
     * 引数を保持する属性。
     */
    public ArrayList<String> extractDecrementIndexArg1 = new ArrayList<String>();
    
    /**
     * 戻り値を保持する属性。
     */
    public String extractDecrementIndexReturnValue = null;

    /**
     * 引数を保持する属性。
     */
    public ArrayList<String> isMapAttributeArg1 = new ArrayList<String>();
    
    /**
     * 戻り値を保持する属性。
     */
    public boolean isMapAttributeReturnValue = false;

    /**
     * 戻り値を保持する属性。
     */
    public ArrayList<String> isMapObjectArg1 = new ArrayList<String>();
    
    /**
     * 戻り値を保持する属性。
     */
    public boolean isMapObjectReturnValue = false;
    
    /**
     * コンストラクタ
     * @param target ターゲットのJavaBean
     */
    public JXPathIndexedBeanWrapperImplStub04(Object target) {
        super(target);
    }

    /**
     * 
     */
    @Override
    protected String extractAttributeName(String node) {
        extractAttributeNameArg1.add(node);
        return "aaa";
    }

    /**
     * ※注意：呼び出し回数により 戻り値が変わる。
     */
    @Override
    protected String extractMapAttributeName(String node) {
        String returnValue = null;

        extractMapAttributeNameArg1.add(node);
        returnValue = extractMapAttributeNameReturnValue[extractMapAttributeNameInnerCount];

        extractMapAttributeNameInnerCount++;

        return returnValue;
    }

    /**
     * ※注意：呼び出し回数により 戻り値が変わる。
     */
    @Override
    protected String extractMapKey(String node) {
        String returnValue = null;

        extractMapKeyArg1.add(node);
        returnValue = extractMapKeyReturnValue[extractMapKeyInnerCount];

        extractMapKeyInnerCount++;

        return returnValue;
    }

    /**
     * 【呼び出し方法】
     * extractDecrementIndexReturnValue に戻り値を設定した後で呼び出す。
     */
    @Override
    protected String extractDecrementIndex(String node) {
        extractDecrementIndexArg1.add(node);
        return extractDecrementIndexReturnValue;
    }

    /**
     * 【呼び出し方法】
     *  isMapAttributeReturnValue に戻り値を設定した後で呼び出す。
     */
    @Override
    protected boolean isMapAttribute(String node) {
        isMapAttributeArg1.add(node);
        return isMapAttributeReturnValue;
    }

    /**
     * 【呼び出し方法】
     *  isMapObjectReturnValue に戻り値を設定した後で呼び出す。
     */
    @Override
    protected boolean isMapObject(String node) {
        isMapObjectArg1.add(node);
        return isMapObjectReturnValue;
    }

}
