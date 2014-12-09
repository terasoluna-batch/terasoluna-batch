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

package jp.terasoluna.fw.web.jndi;

/**
 * <p>JNDI関連のユーティリティインタフェース。</p>
 * 
 * WebAPコンテナのJNDIリソースを扱うためにはこのインタフェースを実装する
 * 必要がある。<br>
 * TERASOLUNAはデフォルト実装クラスとして{@link DefaultJndiSupport}を提供する。
 * <br>
 * <br>
 * @see jp.terasoluna.fw.web.jndi.DefaultJndiSupport
 */
public interface JndiSupport {
    
    /**
     * DIコンテナから取得するJndiSupport実装クラスのキー
     */
    public static final String JNDI_SUPPORT_KEY = "jndiSupport";
    
    /**
     * 指定されたオブジェクトを取得する。
     *
     * @param name オブジェクト名
     * @return オブジェクト
     */
    public Object lookup(String name);
    
    /**
     * 名前をオブジェクトにバインドして、
     * 既存のバインディングを上書きする。
     *
     * @param name オブジェクト名
     * @param obj バインドされるオブジェクト
     */
    public void rebind(String name, Object obj);
    
    /**
     * 指定されたオブジェクトをアンバインドする。
     * 
     * @param name オブジェクト名
     */
    public void unbind(String name);
}
