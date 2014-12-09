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

import com.ibatis.sqlmap.client.SqlMapSession;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;

/**
 * {@link UpdateDAOiBatisImpl}の試験のために使用されるスタブ。
 *
 * {@link UpdateDAOiBatisImple_SqlMapSessionImpl}を使用するために使用される。
 *
 */
public class UpdateDAOiBatisImpl_SqlMapClientStub01 extends SqlMapClientImpl {

    private UpdateDAOiBatisImple_SqlMapSessionImpl sqlMapSession
        = new UpdateDAOiBatisImple_SqlMapSessionImpl();

    /**
     * コンストラクタ。
     * @param delegate
     */
    public UpdateDAOiBatisImpl_SqlMapClientStub01() {
        this(new SqlMapExecutorDelegate());
        sqlMapSession = new UpdateDAOiBatisImple_SqlMapSessionImpl();
    }

    /**
     * コンストラクタ。
     * @param delegate
     */
    public UpdateDAOiBatisImpl_SqlMapClientStub01(SqlMapExecutorDelegate delegate) {
        super(delegate);
    }

    /**
     * テスト用のSqlMapSessionを返却する。
     * @return テスト用のSqlMapSession
     */
    @Override
    public SqlMapSession openSession() {
        // TODO 自動生成されたメソッド・スタブ
        return sqlMapSession;
    }

    

}
