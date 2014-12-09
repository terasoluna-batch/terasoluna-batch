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

package jp.terasoluna.fw.dao;

/**
 * SQLを保持するクラス。<br/>
 * <@link UpdateDAO#executeBatch(List)>の引数として使用する。
 * 
 */
public class SqlHolder {
    
    /**
     * SQLID
     */
    protected String sqlID = null;
    
    /**
     * SQLの引数
     */
    protected Object bindParams = null;

    /**
     * コンストラクタ。
     * @param sqlID SQLID
     * @param bindParams SQLの引数
     */
    public SqlHolder(String sqlID, Object bindParams) {
        this.sqlID = sqlID;
        this.bindParams = bindParams;
    }
    
    /**
     * SQLIDを取得する。
     * @return SQLID
     */
    public String getSqlID() {
        return sqlID;
    }
    
    /**
     * SQLの引数を取得する。
     * @return SQLの引数
     */
    public Object getBindParams() {
        return bindParams;
    }

}
