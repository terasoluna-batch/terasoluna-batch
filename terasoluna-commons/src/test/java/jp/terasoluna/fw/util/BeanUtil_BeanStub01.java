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

package jp.terasoluna.fw.util;

/**
 * {@link jp.terasoluna.fw.util.BeanUtilTest} クラスで使用する。
 * 
 * @see jp.terasoluna.fw.util.BeanUtilTest
 */
@SuppressWarnings("unused")
public class BeanUtil_BeanStub01 {
    
    private String param2 = null;

    /**
     * Exceptionをスローする。
     * @return 文字列。
     * @throws Exception 例外
     */
    public String getParam1() throws Exception {
        throw new Exception();
    }
    
    /**
     * Exceptionをスローする。
     * @param param1 パラメータ
     * @throws Exception 例外
     */
    public void setParam1(String param1) throws Exception {
        throw new Exception();
    }
    
    /**
     * param2を返却する。
     * 
     * @return param2
     */
    public String getParam2() {
        return param2;
    }
    
    /**
     * param2を設定する。
     * @param param2 パラメータ
     */
    public void setParam2(String param2) {
        this.param2 = param2;
    }
}
