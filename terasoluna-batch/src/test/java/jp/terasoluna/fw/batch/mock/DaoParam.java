/*
 * Copyright (c) 2014 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.mock;

import org.apache.ibatis.session.RowBounds;

/**
 * Daoメソッド実行時のパラメータクラス。
 * 
 */
public class DaoParam {
    private RowBounds rowBounds;
    private Object bindParams;
    private String methodName;

    public DaoParam(Object bindParams) {
        super();
        this.bindParams = bindParams;
    }

    public DaoParam(RowBounds rowBounds, Object bindParams) {
        super();
        this.rowBounds = rowBounds;
        this.bindParams = bindParams;
    }

    public RowBounds getRowBounds() {
        return rowBounds;
    }

    public void setRowBounds(RowBounds rowBounds) {
        this.rowBounds = rowBounds;
    }

    public Object getBindParams() {
        return bindParams;
    }

    public void setBindParams(Object bindParams) {
        this.bindParams = bindParams;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String toString() {
        return "RepositoryParam [rowBounds=" + rowBounds + ", bindParams="
                + bindParams + ", methodName=" + methodName + "]";
    }
}
