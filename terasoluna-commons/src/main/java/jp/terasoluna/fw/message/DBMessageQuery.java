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

package jp.terasoluna.fw.message;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.object.MappingSqlQuery;

/**
 * メッセージリソースを取得するRDBMSオペレーションクラス。<br>
 * DBから取得したメッセージリソースをDBMessageオブジェクトに格納し、返却する。
 * <br>
 * DBMessageオブジェクト内にはメッセージコード、言語コード、国コード、
 * バリアントコード、メッセージ本体が格納される。ただし、言語コード、国コード
 * 及びバリアントコードは必須ではない。存在しない場合は、DBMessageオブジェクト
 * 返却時に該当部分にnullを設定する。
 * 
 * @see jp.terasoluna.fw.message.DataSourceMessageSource
 * @see jp.terasoluna.fw.message.DBMessage
 * @see jp.terasoluna.fw.message.DBMessageResourceDAO
 * @see jp.terasoluna.fw.message.DBMessageResourceDAOImpl
 * 
 */
public class DBMessageQuery extends MappingSqlQuery {
  
    /**
     * メッセージコードを格納した結果セットのカラム名。
     */
    protected String rsCodeColumn = null;
    
    /**
     * メッセージの言語コードを格納した結果セットのカラム名。
     */
    protected String rsLanguageColumn = null;
    
    /**
     * メッセージの国コードを格納した結果セットのカラム名。
     */
    protected String rsCountryColumn = null;
    
    /**
     * メッセージのバリアントコードを格納した結果セットのカラム名。
     */
    protected String rsVariantColumn = null;
    
    /**
     * メッセージ本体を格納した結果セットのカラム名。
     */
    protected String rsMessageColumn = null;
    
    /**
     * ログクラス。
     */
    private static Log log = LogFactory.getLog(DBMessageQuery.class);
    
    /**
     * コンストラクタ内で親クラスにSQL文を渡し、コンパイル処理をする。
     * コンパイル処理前にカラム名に不正な値が渡されていないかをチェックする。
     * 必須カラム名（メッセージコード、メッセージ本体）はnullチェック及び空文字
     * チェックを実施する。その他のカラム名は空文字チェックのみを実施する。
     * 
     * @param ds
     *            メッセージリソースを格納したデータセット。
     * @param sql
     *            DBからメッセージリソースを取得するSQL文。
     * @param codeColumn
     *            メッセージコードが格納されたDB内のカラム名。
     *            存在しない場合は警告を出す。
     * @param languageColumn
     *            メッセージの言語コードが格納されたDB内のカラム名。
     *            検索対象としない場合はnullとする。
     * @param countryColumn
     *            メッセージの国コードが格納されたDB内のカラム名。
     *            検索対象としない場合はnullとする。
     * @param variantColumn
     *            メッセージのバリアントコードが格納されたDB内のカラム名。
     *            検索対象としない場合はnullとする。
     * @param messageColumn
     *            メッセージ本体が格納されたDB内のカラム名。
     *            存在しない場合は警告を出す。
     * 
     */
    public DBMessageQuery(DataSource ds, String sql, String codeColumn,
            String languageColumn, String countryColumn, String variantColumn,
            String messageColumn) {
        super(ds, sql);
        rsCodeColumn = codeColumn;
        rsLanguageColumn = languageColumn;
        rsCountryColumn = countryColumn;
        rsVariantColumn = variantColumn;
        rsMessageColumn = messageColumn;
        compile();
    }
    
    /**
     * DBから取得したメッセージリソースをDBMessageオブジェクトに格納、返却する。
     * 引数として渡された結果セットの現在行の内容を元にして作成したDBMessage
     * オブジェクトを返す。
     * 
     * @return メッセージリソースを格納したDBMessageオブジェクト
     * 
     * @param rs
     *            DBから取得した値を保持する結果セット
     * @param rowNum
     *            処理している結果セットの行番号
     * 
     * @throws SQLException
     *             SQL例外
     */
    @Override
    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        // メッセージコード。
        String code = null;
        // メッセージの言語コード。
        String language = null;
        // メッセージの国コード。
        String country = null;
        // メッセージのバリアントコード。
        String variant = null;
        // メッセージ本体。
        String message = null;

        // メッセージコードが存在しない場合、警告する。
        code = rs.getString(rsCodeColumn);
        if (code == null) {
            code = "";
            if (log.isWarnEnabled()) {
                log.warn("MessageCode is null");
            }
        }

        // 言語コードカラムが存在するが、言語コードが存在しない場合、空文字を入れる。
        if (rsLanguageColumn != null) {
            language = rs.getString(rsLanguageColumn);
            if (language == null) {
                language = "";
            }
        }

        // 国コードカラムが存在するが、国コードが存在しない場合、空文字を入れる。
        if (rsCountryColumn != null) {
            country = rs.getString(rsCountryColumn);
            if (country == null) {
                country = "";
            }
        }

        // バリアントコードカラムが存在するが、バリアントコードが存在しない場合、
        // 空文字を入れる。
        if (rsVariantColumn != null) {
            variant = rs.getString(rsVariantColumn);
            if (variant == null) {
                variant = "";
            }
        }

        // メッセージ本体が存在しない場合、空文字を入れる。
        message = rs.getString(rsMessageColumn);
        if (message == null) {
            message = "";
        }
        
        if (log.isDebugEnabled()) {
            log.debug(code + "," + language + "," + country + "," + variant
                    + "," + message);
        }
        return new DBMessage(code, language, country, variant, message);
    }
}