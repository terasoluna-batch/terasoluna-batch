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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.orm.ibatis.support.AbstractLobTypeHandler;

/**
 * iBATISから利用されるBLOBとストリームをマッピングする実装のiBATISのタイプハンドラ。
 *
 * <p>
 * OracleのBLOB型をストリームで扱う場合は本APIを使用する。（※本クラスはPostgreSQLでは利用できない）<br>
 * BLOB列をバイト配列として扱う場合、本クラスを利用する必要はない。<br>
 * BLOB列のサイズが大きく、メモリに全データを格納できない可能性がある場合は、本クラスを利用すること。<br>
 * </p>
 *
 * <p>
 * 本クラスを利用するためにはOracleLobHandlerのBean定義とsqlMapClientFactoryBeanへの設定を行うこと。<br>
 * また、OracleLobHandlerにはNativeJdbcExtractor実装クラスを設定すること。<br>
 * なお、SpringがOracleLobHandlerといくつかのNativeJdbcExtractor実装クラスを提供している。<br>
 * </p>
 * 
 * <p>
 * OracleLobHandlerは、
 * OracleのJDBCドライバのコネクション(oracle.jdbc.OracleConnectionインタフェース実装クラス)のAPIを使用して、
 * Lobのハンドリングを行う。<br>
 * そのために、コネクションプールから取得したコネクション
 * (closeするとコネクションがプールに返るしくみが実装されている、コネクションのラッパ)から、
 * JDBCドライバのコネクションを取得する役割を果たすのが、NativeJdbcExtractor(Springのインタフェース)である。<br>
 * JDBCドライバのコネクションの取得方法は
 * 使用しているコネクションプールの実装によって異なるため、Springが様々なNativeJdbcExtractor実装クラスを提供している。
 * </p>
 * 
 * <p>
 * Springが提供している実装クラスの一つにSimpleNativeJdbcExtractorがある。<br>
 * SimpleNativeJdbcExtractorは、簡単かつ、特定のコネクションプールを意識しない実装がされている。<br>
 * ただし、SimpleNativeJdbcExtractorを利用しても、
 * commons-dbcp-1.3以降など、コネクションプールの実装によっては、JDBCコネクションを取得できない場合がある。<br>
 * その場合、Springが提供している他のNativeJdbcExtractor実装クラスを使用するか、
 * 各APサーバ用に新たにNativeJdbcExtractor実装クラスを作成する必要がある。<br>
 * </p>
 * 
 * <p>
 * 例えば、
 * commons-dbcp-1.3以降、あるいはそれを使用しているバージョンのTomcatの場合は、CommonsDbcpNativeJdbcExtractorを使用する。
 * APサーバがWebLogicの場合は、WebLogicNativeJdbcExtractorを使用する。
 * 上記のNativeJdbcExtractor実装クラスは、どちらもSpringが提供している。
 * </p>
 *
 * <p>
 *  【<code>Bean定義ファイル</code>の設定例】<br>
 * <code><pre>
 *   &lt;!-- LOBフィールドを扱うためのハンドラ --&gt;
 *   &lt;bean id="oracleLobHandler"
 *            class="org.springframework.jdbc.support.lob.OracleLobHandler"&gt;
 *     &lt;property name="nativeJdbcExtractor" ref="simpleExtractor"/&gt;
 *   &lt;/bean&gt;
 *
 *   &lt;!-- iBATIS データベース層のためのSQlMapの設定 --&gt;
 *   &lt;bean id="sqlMapClient"
 *       class="org.springframework.orm.ibatis.SqlMapClientFactoryBean"&gt;
 *     &lt;property name="configLocation" value="WEB-INF/sql-map-config.xml"/&gt;
 *     &lt;property name="dataSource" ref="dataSource"/&gt;
 *     &lt;property name="lobHandler" ref="oracleLobHandler"/&gt;
 *   &lt;/bean&gt;
 *   
 *  &lt;!-- simpleExtractorのBean定義設定 --&gt;
 *  &lt;!-- ※ OC4JのJNDIからデータソースを取得する場合は、プロパティはすべてtrueにしておくこと。 --&gt;
 *  &lt;bean id="simpleExtractor"
 *        class="org.springframework.jdbc.support.nativejdbc.SimpleNativeJdbcExtractor"&gt;
 *    &lt;property name="nativeConnectionNecessaryForNativeStatements" value="true"/&gt;
 *    &lt;property name="nativeConnectionNecessaryForNativePreparedStatements" value="true"/&gt;
 *    &lt;property name="nativeConnectionNecessaryForNativeCallableStatements" value="true"/&gt;
 *  &lt;/bean&gt;
 * </pre></code>
 * </p>
 *
 * <p>
 * 本クラスを利用したiBATIS設定ファイルの記述方法を以下に示す。
 * </p>
 *
 * <p>
 *  【BLOB_TESTテーブル定義】<br>
 *   <table border="1" CELLPADDING="8">
 *     <th>列名</th>
 *     <th>型</th>
 *     <th>制約</th>
 *
 *     <tr>
 *       <td align=center>PK</td>
 *       <td>INTEGER</td>
 *       <td>NOT NULL</td>
 *     </tr>
 *
 *     <tr>
 *       <td align=center>MAP</td>
 *       <td>BLOB</td>
 *       <td>NOT NULL</td>
 *     </tr>
 *  </table>
 * </p>
 *
 * <p>
 *  【<code>iBATIS設定ファイル</code>の設定例】<br>
 * <code><pre>
 * &lt;!-- update文の設定 --&gt;
 * &lt;parameterMap id="blobParam" class="java.util.Map"&gt;
 *   &lt;parameter property="pk"/&gt;
 *   &lt;parameter property="map"
 *       typeHandler="jp.terasoluna.fw.orm.ibatis.support.BlobInputStreamTypeHandler"/&gt;
 * &lt;/parameterMap&gt;
 *
 * &lt;insert id="insertBLobTest" parameterMap="blobParam"&gt;
 *   INSERT INTO BLOB_TEST (PK, MAP) VALUES (?, ?)
 * &lt;/insert&gt;
 *
 * &lt;!-- select文の設定 --&gt;
 * &lt;resultMap id="blobResult" class="java.util.HashMap"&gt;
 *   &lt;result property="pk"/&gt;
 *   &lt;result property="map"
 *       typeHandler="jp.terasoluna.fw.orm.ibatis.support.BlobInputStreamTypeHandler"/&gt;
 * &lt;/resultMap&gt;
 *
 * &lt;select id="selectBLobTest" resultMap="blobResult"&gt;
 *   SELECT PK, MAP FROM BLOB_TEST
 * &lt;/select&gt;
 * </pre></code>
 * </p>
 *
 */
public class BlobInputStreamTypeHandler extends AbstractLobTypeHandler {

    /**
     * コンストラクタ。
     */
    public BlobInputStreamTypeHandler() {
        super();
    }

    /**
     * コンストラクタ。
     * @param lobHandler LobHandler
     */
    protected BlobInputStreamTypeHandler(LobHandler lobHandler) {
        super(lobHandler);
    }

    /**
     * パラメータを設定する。
     *
     * @param ps セット先のPreparedStatement
     * @param index パラメータのインデックス
     * @param value セットするパラメータ
     * @param jdbcType パラメータのJDBC型
     * @param lobCreator 利用するLobCreator
     * @throws SQLException SQL例外
     */
    @Override
    protected void setParameterInternal(
            PreparedStatement ps,
            int index,
            Object value,
            String jdbcType,
            LobCreator lobCreator)
                throws SQLException {
        lobCreator.setBlobAsBinaryStream(ps, index, (InputStream) value, 0);
    }

    /**
     * 結果を取得する。
     * @param rs 取得元のResultSet
     * @param index ResultSetのインデックス
     * @param lobHandler 利用するLobHandler
     * @return 取得結果
     * @throws SQLException SQL例外
     */
    @Override
    protected Object getResultInternal(
            ResultSet rs,
            int index,
            LobHandler lobHandler)
                throws SQLException {
        return lobHandler.getBlobAsBinaryStream(rs, index);
    }

    /**
     * 文字列を本Handlerが扱う型に変換する。
     * @param s 文字列
     * @return Handlerが扱う型のインスタンス
     */
    public Object valueOf(String s) {
        if (s == null) {
            return null;
        }
        return new ByteArrayInputStream(s.getBytes());
    }
}
