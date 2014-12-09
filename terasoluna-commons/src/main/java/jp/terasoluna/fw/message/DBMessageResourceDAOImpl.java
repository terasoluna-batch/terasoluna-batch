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

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * DBからメッセージリソースを取得するDBMessageResourceDAOの実装クラス。
 * 本クラスは、メッセージリソースを格納したDBから検索SQL文を使用し、
 * メッセージリソースをDBMessageオブジェクトのリストとしてまとめ、返却する。
 * <br><br>
 * <strong>使用方法</strong>
 * <br>
 * このクラスを使用するにはアプリケーションコンテキスト起動時にDAOとして
 * 認識させる必要がある。<br>
 * <br>
 * <strong>設定例</strong><br>
 * DAOの実装クラスとして本クラスを使用する場合、Bean定義ファイルに
 * 以下の記述をする。<br>
 * <pre>
 * &lt;bean id = &quot;dBMessageResourceDAO&quot;
 *   class = &quot;jp.terasoluna.fw.message.DBMessageResourceDAOImpl&quot;&gt;
 *   &lt;property name = &quot;dataSource&quot;&gt;
 *     &lt;ref bean = &quot;dataSource&quot;&gt;&lt;/ref&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * <strong>解説</strong><br>
 * &lt;bean&gt;要素のid属性にDBMessageResourceDAOを指定し、&lt;bean&gt;要素内
 * &lt;property&gt;要素にはdataSourceを設定する。<br> 
 * 
 * <h3>検索SQL文について</h3>
 * 
 * DBからメッセージリソースを取得する検索SQL文には初期値が与えられている。
 * デフォルトの検索SQL文は
 * <pre>SELECT CODE,MESSAGE FROM MESSAGES</pre>
 * である。
 * デフォルトの検索SQL文を使用する場合、DBのカラム名は以下の通りとなる。<br>
 * テーブル名 = MESSAGES<br>
 * メッセージコードを格納するカラム名 = CODE<br>
 * メッセージ本文を格納するカラム名 = MESSAGE<br>
 * 尚、デフォルトの検索SQL文を使用する場合、ロケール対応は行われない。
 * ロケール対応する場合は、下記、検索SQL文の変更が必要となる。
 * 
 * 
 * <h4>検索SQL文の変更1</h4>
 * この変更方法は検索SQL文のフォーマットにしたがって、テーブル名及び各カラム名を
 * 独自に指定し、本クラスの機能によって、検索SQL文を生成する方法である。この方法
 * を実施することで、以下のことが可能になる。<br>
 * １．DBのテーブル名及び各カラム名の自由な設定<br>
 * ２．ロケール対応<br>
 * 検索SQL文のフォーマット
 * <pre>SELECT メッセージコードのカラム名 , 言語コードのカラム名 , 国コードのカラム名, バリアントコードのカラム名 ,メッセージ本体のカラム名 FROM テーブル名
 * </pre>
 * テーブル名及び各カラム名の全てもしくは一部を指定することでDBのテーブル名及び
 * カラム名を自由に設定出来る。一部の値のみを指定した場合、指定されていない値は
 * 上記デフォルトの検索SQL文の値が使用される。<br>
 * 又、言語コードのカラム名、国コードのカラム名、バリアントコードのカラム名を
 * 指定するし、各カラムを有効にすることにより、これらのコードによるロケールの
 * 判別が可能となる。
 * これらの値は本クラス内に実装されている各々のセッターを利用する事で変更出来る。
 * <br>
 * <br>
 * <strong>設定例</strong><br>
 * Bean定義ファイル内で以下のような記述をする。<br>
 * テーブル名及び各カラム名の全てを独自に設定する場合。
 * 
 * <pre>
 * &lt;bean id = &quot;DBMessageResourceDAO&quot;
 *   class = &quot;jp.terasoluna.fw.message.DBMessageResourceDAOImpl&quot;&gt;
 *     &lt;ref bean = &quot;dataSource&quot;&gt;&lt;/ref&gt;
 *   &lt;/property&gt;
 *   &lt;property name = &quot;tableName&quot;&gt;
 *     &lt;value&gt;DBMESSAGES&lt;/value&gt;
 *   &lt;/property&gt;
 *   &lt;property name = &quot;codeColumn&quot;&gt;
 *     &lt;value&gt;BANGOU&lt;/value&gt;
 *   &lt;/property&gt;
 *   &lt;property name = &quot;languageColumn&quot;&gt;
 *     &lt;value&gt;GENGO&lt;/value&gt;
 *   &lt;/property&gt;
 *   &lt;property name = &quot;countryColumn&quot;&gt;
 *     &lt;value&gt;KUNI&lt;/value&gt;
 *   &lt;/property&gt;
 *   &lt;property name = &quot;variantColumn&quot;&gt;
 *     &lt;value&gt;HOUGEN&lt;/value&gt;
 *   &lt;/property&gt;
 *   &lt;property name = &quot;messageColumn&quot;&gt;
 *     &lt;value&gt;MESSAGE&lt;/value&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * &lt;bean&gt;要素内&lt;properities&gt;要素のname属性に変更したいテーブル名や
 * カラム名を指定し、value属性にて設定したい値を指定する。<br>
 * <br>
 * 上記設定により検索SQL文は
 * <pre>SELECT BANGOU,GENGO,KUNI,HOUGEN,HONBUN FROM DBMESSAGES</pre>
 * となる。<br>
 * またDBのテーブル名及びカラム名は以下の通りとなる。 <br>
 * テーブル名 = DBMESSAGES<br>
 * メッセージコードを格納するカラム名 = BANGOU<br>
 * メッセージの言語コードを格納するカラム名 = GENGO<br>
 * メッセージの国コードを格納するカラム名 = KUNI<br>
 * メッセージのバリアントコードを格納するカラム名 = HOUGEN<br>
 * メッセージ本文を格納するカラム名 = HONBUN<br>
 * 
 * 
 * <h4>検索SQL文の変更2</h4>
 * この変更方法は本クラスの機能による検索SQL文の生成を行わずに、検索SQL文を独自
 * に指定する方法である。WHERE句など検索SQL文のフォーマットでは対応出来ない
 * クエリを利用する場合に有効である。<br>
 * <br>
 * <strong>設定例</strong><br>
 * Bean定義ファイル内で以下のような記述をする。
 * 検索SQL文及びテーブル名、各カラム名の全てを独自に設定する場合。<br>
 * <br>
 * 
 * <pre>
 * &lt;bean id = &quot;DBMessageResourceDAO&quot;
 *   class = &quot;jp.terasoluna.fw.message.DBMessageResourceDAOImpl&quot;&gt;
 *   &lt;property name = &quot;dataSource&quot;&gt;
 *     &lt;ref bean = &quot;dataSource&quot;&gt;&lt;/ref&gt;
 *   &lt;/property&gt;
 *   &lt;property name = &quot;findMessageSql&quot;&gt;
 *     &lt;value&gt;
 *       SELECT BANGOU as CODE,HONBUN as MESSAGE FROM DBDATA WHERE CATEGORY = "DBMESSAGE"
 *     &lt;/value&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * &lt;bean&gt;要素内&lt;properities&gt;要素のname属性に検索SQL文と使用する
 * カラム名を指定し、value属性にて設定したいSQL文を指定する。<br>
 * <br>
 * SQL文で<br>
 * 新規カラム名 as デフォルトのカラム名<br>
 * とすることでDBのカラム名を変更することが出来る。
 * ただし、デフォルトで値が渡されているメッセージコード及びメッセージの２カラム
 * のみの対応となる。ロケール対応する場合、その他のカラムを設定する必要がある。
 * その場合は検索SQL文の変更1に倣い、カラムを有効にする必要がある。<br>
 * <br>
 * 上記設定により検索SQL文は
 * <pre>SELECT BANGOU as CODE,HONBUN as MESSAGE FROM DBDATA WHERE CATEGORY = "DBMESSAGE"
 * </pre>
 * となる。<br>
 * 
 * またDBのテーブル名及びカラム名は以下の通りとなる。<br>
 * テーブル名 = DBDATA<br>
 * メッセージコードを格納するカラム名 = BANGOU<br>
 * メッセージの言語コードを格納するカラム名 = null<br>
 * メッセージの国コードを格納するカラム名 = null<br>
 * メッセージのバリアントコードを格納するカラム名 = null<br>
 * メッセージ本文を格納するカラム名 = HONBUN<br>
 * 
 * @see jp.terasoluna.fw.message.DataSourceMessageSource
 * @see jp.terasoluna.fw.message.DBMessage
 * @see jp.terasoluna.fw.message.DBMessageQuery
 * @see jp.terasoluna.fw.message.DBMessageResourceDAO
 * 
 */
public class DBMessageResourceDAOImpl extends JdbcDaoSupport implements
        DBMessageResourceDAO {
    /**
     * メッセージを格納するDBのテーブル名。デフォルトはMESSAGES。
     */
    protected String tableName = "MESSAGES";

    /**
     * メッセージコードを格納するDBのカラム名。デフォルトはCODE。
     */
    protected String codeColumn = "CODE";

    /**
     * 言語コードを格納するDBのカラム名。デフォルトはnull。
     */
    protected String languageColumn = null;

    /**
     * 国コードを格納するDBのカラム名。デフォルトはnull。
     */
    protected String countryColumn = null;

    /**
     * バリアントコードを格納するDBのカラム名。デフォルトはnull。
     */
    protected String variantColumn = null;

    /**
     * メッセージを格納するDBのカラム名。デフォルトはMESSAGE。
     */
    protected String messageColumn = "MESSAGE";

    /**
     * 外部から設定されるDB検索時に使用されるSQL文。
     * 設定されている場合、こちらが実行される。
     */
    protected String findMessageSql = null;

    /**
     * ログクラス。
     */
    private static Log log = LogFactory.getLog(DBMessageResourceDAOImpl.class);

    /**
     * メッセージを格納するDBのテーブル名を設定する。設定されていない場合は
     * デフォルトの値MESSAGESが使用される。
     * 
     * @param tableName
     *            メッセージを格納したDBのテーブル名。
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * メッセージコードを格納するDBのカラム名を設定する。設定されていない場合は
     * デフォルトの値CODEが使用される。
     * 
     * @param codeColumn
     *            メッセージコードを格納したDBのカラム名。
     */
    public void setCodeColumn(String codeColumn) {
        this.codeColumn = codeColumn;
    }

    /**
     * 言語コードを格納するDBのカラム名を設定する。設定されていない場合は
     * デフォルトの値nullが使用される。
     * 
     * @param languageColumn
     *            言語コードを格納したDBのカラム名。
     */
    public void setLanguageColumn(String languageColumn) {
        this.languageColumn = languageColumn;
    }

    /**
     * 国コードを格納するDBのカラム名を設定する。設定されていない場合は
     * デフォルトの値nullが使用される。
     * 
     * @param countryColumn
     *            国コードを格納したDBのカラム名。
     */
    public void setCountryColumn(String countryColumn) {
        this.countryColumn = countryColumn;
    }

    /**
     * バリアントコードを格納するDBのカラム名を設定する。設定されていない場合は
     * デフォルトの値nullが使用される。
     * 
     * @param variantColumn
     *            バリアントコードを格納したDBのカラム名。
     */
    public void setVariantColumn(String variantColumn) {
        this.variantColumn = variantColumn;
    }

    /**
     * メッセージを格納するDBのカラム名を設定する。設定されていない場合は
     * デフォルトの値MESSAGEが使用される。
     * 
     * @param messageColumn
     *            メッセージを格納したDBのカラム名。
     */
    public void setMessageColumn(String messageColumn) {
        this.messageColumn = messageColumn;
    }

    /**
     * DBからメッセージリソースを検索するSQL文を設定する。設定されていない場合は
     * makeSqlメソッドにて作成されたSQL文が実行される。
     * 
     * @param findMessageSql
     *            外部から設定されるDB検索時に使用されるSQL文。
     */
    public void setFindMessageSql(String findMessageSql) {
        this.findMessageSql = findMessageSql;
    }

    /**
     * メッセージリソースを取得するRDBMSオペレーションクラス。
     */
    protected DBMessageQuery dBMessageQuery = null;
    
    /**
     * DBMessageResourceDAOImplを生成する。
     */
    protected DBMessageResourceDAOImpl() {
        super();
    }

    /**
     * DBよりメッセージリソースを取得するDBMessageQueryを生成する。
     * コンストラクタに渡される値のうち、メッセージコードのカラム名、
     * 言語コードのカラム名、国コードのカラム名はnullの場合がある。<br>
     * nullが渡された場合、これらのカラムはDBに存在しないものとして処理される。
     * 
     * @throws IllegalArgumentException
     *             DBとの接続が取得できなかった場合
     */
    @Override
    protected void initDao() {
        DataSource dataSource = getDataSource();
        if (dataSource == null) {
            log.error("Missing dataSource in spring configuration file.");
            throw new IllegalArgumentException("Missing dataSource in spring"
                    + " configuration file.");
        }
        this.dBMessageQuery = new DBMessageQuery(dataSource, makeSql(),
                codeColumn, languageColumn, countryColumn, variantColumn,
                messageColumn);
    }

    /**
     * DBから取得したメッセージリソースをDBMessageオブジェクトに格納し、リスト型
     * で返却する。
     * 
     * @return メッセージリソースのリスト
     */
    @SuppressWarnings("unchecked")
    public List<DBMessage> findDBMessages() {
        // JDBCDaoSupportにてDBMessageQueryが必ず生成されるため、
        // nullにはならない。
        return dBMessageQuery.execute();
    }

    /**
     * DBからメッセージリソースを取得するSQL文を生成する。
     * SQL文生成前にカラム名及びテーブル名に不正な値が渡されていないかの
     * チェックをする。必須カラム名（メッセージコード、メッセージ本体）
     * とテーブル名はnullチェック及び空文字チェックを実施する。
     * その他のカラム名は空文字チェックのみを実施する。
     * 
     * @return DBからメッセージリソースを取得するSQL文。
     *          nullは返却しない。
     * 
     */
    protected String makeSql() {
        // カラム名チェック
        checkRequiredColumnName(codeColumn, "codeColumn");
        checkNotRequiredColumnName(languageColumn, "languageColumn");
        checkNotRequiredColumnName(countryColumn, "countryColumn");
        checkNotRequiredColumnName(variantColumn, "variantColumn");
        checkRequiredColumnName(messageColumn, "messageColumn");
        checkRequiredColumnName(tableName, "tableName");
        // 外部からSQL文を指定された場合、そちらを使用する。
        StringBuilder sql = null;
        if (findMessageSql != null) {
            sql = new StringBuilder(findMessageSql);
        } else {
            // SQL文の指定がない場合、新たに生成する。
            sql = new StringBuilder("SELECT ");
            sql.append(codeColumn);
            sql.append(",");
            // 言語コードのカラム名が、設定されていない場合は
            // 言語コードを検索しない。
            if (languageColumn != null) {
                sql.append(languageColumn);
                sql.append(",");
            }
            // 国コードのカラム名が、設定されていない場合は国コードを検索しない。
            if (countryColumn != null) {
                sql.append(countryColumn);
                sql.append(",");
            }
            // バリアントコードのカラム名が、設定されていない場合は
            // バリアントコードを検索しない。
            if (variantColumn != null) {
                sql.append(variantColumn);
                sql.append(",");
            }
            sql.append(messageColumn);
            sql.append(" FROM ");
            sql.append(tableName);
        }
        if (log.isDebugEnabled()) {
            log.debug("sql=[" + sql + "]");
        }
        return sql.toString();
    }

    /**
     * 必須カラムのカラム名及びテーブル名をチェックする。
     * nullチェック及び空文字チェックを実施する。
     * 
     * @param value
     *            DBでのカラム名もしくはテーブル名
     * @param columnName
     *            検査対象のカラムもしくはテーブル
     */
    protected void checkRequiredColumnName(String value, String columnName) {
        // カラム名のエラーチェック。
        // メッセージコードのカラム名がnullもしくは空文字の場合、エラーを返す。
        if (value == null || "".equals(value)) {
            log.error("illegalArgument: " + columnName + " is null or empty.");
            throw new IllegalArgumentException("illegalArgument: " + columnName
                    + " is null or empty.");
        }
    }

    /**
     * 必須カラム以外のカラム名をチェックする。 空文字チェックを実施する。
     * 
     * @param value
     *            DBでのカラム名
     * @param columnName
     *            検査対象のカラム
     */
    protected void checkNotRequiredColumnName(String value, String columnName) {
        // カラム名のエラーチェック。
        // メッセージコードのカラム名が空文字の場合、エラーを返す。
        if ("".equals(value)) {
            log.error("illegalArgument: " + columnName + " is empty.");
            throw new IllegalArgumentException("illegalArgument: " + columnName
                    + " is empty.");
        }
    }
}
