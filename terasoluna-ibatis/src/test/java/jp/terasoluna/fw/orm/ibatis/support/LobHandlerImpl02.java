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

package jp.terasoluna.fw.orm.ibatis.support;

import java.io.InputStream;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

/**
 * LobHandler実装クラス。
 * 
 */
public class LobHandlerImpl02 implements LobHandler {

    /**
     * SQLException
     */
    public SQLException e = new SQLException();
    
    /**
     * BlobInputStreamTypeHandler#setParameterInternalテスト
     * SQLExceptionをスローする。
     */
    public InputStream getBlobAsBinaryStream(ResultSet arg0, int arg1)
            throws SQLException {
        throw e;
    }
    
    /**
     * ClobReaderTypeHandler#setParameterInternalテスト
     * SQLExceptionをスローする。
     */
    public Reader getClobAsCharacterStream(ResultSet arg0, int arg1)
            throws SQLException {
        throw e;
    }
    
    
    public byte[] getBlobAsBytes(ResultSet arg0, String arg1)
            throws SQLException {
        return null;
    }

    public byte[] getBlobAsBytes(ResultSet arg0, int arg1) throws SQLException {
        return null;
    }

    public InputStream getBlobAsBinaryStream(ResultSet arg0, String arg1)
            throws SQLException {
        return null;
    }

    public String getClobAsString(ResultSet arg0, String arg1)
            throws SQLException {
        return null;
    }

    public String getClobAsString(ResultSet arg0, int arg1) throws SQLException {
        return null;
    }

    public InputStream getClobAsAsciiStream(ResultSet arg0, String arg1)
            throws SQLException {
        return null;
    }

    public InputStream getClobAsAsciiStream(ResultSet arg0, int arg1)
            throws SQLException {
        return null;
    }

    public Reader getClobAsCharacterStream(ResultSet arg0, String arg1)
            throws SQLException {
        return null;
    }

    public LobCreator getLobCreator() {
        return null;
    }
}