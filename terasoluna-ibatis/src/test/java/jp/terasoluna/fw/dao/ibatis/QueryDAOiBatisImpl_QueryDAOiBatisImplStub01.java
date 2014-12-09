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

package jp.terasoluna.fw.dao.ibatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link QueryDAOiBatisImpl}の試験のために使用されるスタブ。
 * 
 * {@link QueryDAOiBatisImpl}からの呼び出し確認用に使用される。
 * 
 */
@SuppressWarnings("unchecked")
public class QueryDAOiBatisImpl_QueryDAOiBatisImplStub01 extends QueryDAOiBatisImpl {

    /**
     * テスト用executeForObjectArrayメソッド
     */
    @Override
    public <E> E[] executeForObjectArray(String sqlID, Object bindParams, Class clazz) {
        this.called = true;
        this.sqlID = sqlID;
        this.bindParams = bindParams;
        this.clazz = clazz;
        Map<String, Object>[] map = new HashMap[]{new HashMap<String, Object>()};
        map[0].put("abc","123");
        return (E[]) map;
    }

    
    /**
     * テスト用executeForObjectListメソッド
     */
    @Override
    public <E> List<E> executeForObjectList(String sqlID, Object bindParams) {
        this.called = true;
        this.sqlID = sqlID;
        this.bindParams = bindParams;
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("abc", "123");
        list.add(map);
        return (List<E>) list;
    }
    
    
    
    /**
     * テスト用executeForObjectArrayメソッド
     */
    @Override
    public <E> E[] executeForObjectArray(String sqlID, Object bindParams, Class clazz, int beginIndex, int maxCount) {
        this.called = true;
        this.sqlID = sqlID;
        this.bindParams = bindParams;
        this.clazz = clazz;
        this.beginIndex = beginIndex;
        this.maxCount = maxCount;
        Map<String, Object>[] map = new HashMap[]{new HashMap<String, Object>()};
        map[0].put("abc","123");
        return (E[]) map;
    }
    
    
    /**
     * テスト用executeForObjectListメソッド
     */
    @Override
    public <E> List<E> executeForObjectList(String sqlID, Object bindParams, int beginIndex, int maxCount) {
        this.called = true;
        this.sqlID = sqlID;
        this.bindParams = bindParams;
        this.beginIndex = beginIndex;
        this.maxCount = maxCount;
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("abc", "123");
        list.add(map);
        return (List<E>) list;
    }
    

    /*
     * 呼び出し確認用変数
     */
    private boolean called = false;
    private String sqlID = null;
    private Object bindParams = null;
    private Class clazz = null;
    private int beginIndex = 0;
    private int maxCount = 0;

    public boolean isCalled() {
        return called;
    }

    public Object getBindParams() {
        return bindParams;
    }

    public String getSqlID() {
        return sqlID;
    }

    public Class getClazz() {
        return clazz;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public int getMaxCount() {
        return maxCount;
    }

}
