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

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.test.util.ReflectionTestUtils;
import jp.terasoluna.utlib.MockDataSource;
import junit.framework.TestCase;

import com.mockrunner.mock.jdbc.MockResultSet;

import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;
import static uk.org.lidalia.slf4jtest.LoggingEvent.warn;
import static uk.org.lidalia.slf4jtest.LoggingEvent.debug;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

/**
 * {@link jp.terasoluna.fw.message.DBMessageQuery} クラスのブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4> メッセージリソースを取得するRDBMSオペレーションクラス
 * <p>
 * @see jp.terasoluna.fw.message.DBMessageQuery
 */
public class DBMessageQueryTest extends TestCase {

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            DBMessageQuery.class);

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 終了処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        logger.clear();
        super.tearDown();
    }

    /**
     * コンストラクタ。
     * @param name このテストケースの名前。
     */
    public DBMessageQueryTest(String name) {
        super(name);
    }

    /**
     * testDBMessageDataSource01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) ds:not null<br>
     * (引数) sql:"SELECT CODE,MESSAGE FROM MESSAGES"<br>
     * (引数) codeColumn:"CODE"<br>
     * (引数) languageColumn:"LANGUAGE"<br>
     * (引数) countryColumn:"COUNTRY"<br>
     * (引数) variantColumn:"VARIANT"<br>
     * (引数) messageColumn:"MESSAGE"<br>
     * <br>
     * 期待値：(状態変化) rsCodeColumn:"CODE"<br>
     * (状態変化) rsLanguageColumn:"LANGUAGE"<br>
     * (状態変化) rsCountryColumn:"COUNTRY"<br>
     * (状態変化) rsVariantColumn:"VARIANT"<br>
     * (状態変化) message:"MESSAGE"<br>
     * (状態変化) compile():呼び出されたことを確認する。<br>
     * <br>
     * 引数がStringだった場合、引き渡された値が変化なく格納されるかを確認。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testDBMessageDataSource01() throws Exception {
        // 前処理
        DataSource ds = new MockDataSource();
        DBMessageQuery db = new DBMessageQuery(ds, "SELECT CODE,MESSAGE FROM MESSAGES", "CODE", "LANGUAGE", "COUNTRY", "VARIANT", "MESSAGE");

        // テスト実施

        // 判定
        assertEquals("CODE", ReflectionTestUtils.getField(db, "rsCodeColumn"));
        assertEquals("LANGUAGE", ReflectionTestUtils.getField(db,
                "rsLanguageColumn"));
        assertEquals("COUNTRY", ReflectionTestUtils.getField(db,
                "rsCountryColumn"));
        assertEquals("VARIANT", ReflectionTestUtils.getField(db,
                "rsVariantColumn"));
        assertEquals("MESSAGE", ReflectionTestUtils.getField(db,
                "rsMessageColumn"));
        assertTrue(db.isCompiled());
    }

    /**
     * testDBMessageDataSource02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A,C <br>
     * <br>
     * 入力値：(引数) ds:not null<br>
     * (引数) sql:"SELECT CODE,MESSAGE FROM MESSAGES"<br>
     * (引数) codeColumn:""<br>
     * (引数) languageColumn:""<br>
     * (引数) countryColumn:""<br>
     * (引数) variantColumn:""<br>
     * (引数) messageColumn:""<br>
     * <br>
     * 期待値：(状態変化) rsCodeColumn:""<br>
     * (状態変化) rsLanguageColumn:""<br>
     * (状態変化) rsCountryColumn:""<br>
     * (状態変化) rsVariantColumn:""<br>
     * (状態変化) message:""<br>
     * (状態変化) compile():呼び出されたことを確認する。<br>
     * <br>
     * 引数が空文字だった場合、引き渡された値が変化なく格納されるかを確認。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testDBMessageDataSource02() throws Exception {
        // 前処理
        DataSource ds = new MockDataSource();
        DBMessageQuery db = new DBMessageQuery(ds, "SELECT CODE,MESSAGE FROM MESSAGES", "", "", "", "", "");

        // テスト実施

        // 判定
        assertEquals("", ReflectionTestUtils.getField(db, "rsCodeColumn"));
        assertEquals("", ReflectionTestUtils.getField(db, "rsLanguageColumn"));
        assertEquals("", ReflectionTestUtils.getField(db, "rsCountryColumn"));
        assertEquals("", ReflectionTestUtils.getField(db, "rsVariantColumn"));
        assertEquals("", ReflectionTestUtils.getField(db, "rsMessageColumn"));
        assertTrue(db.isCompiled());
    }

    /**
     * testDBMessageDataSource03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A,C <br>
     * <br>
     * 入力値：(引数) ds:not null<br>
     * (引数) sql:"SELECT CODE,MESSAGE FROM MESSAGES"<br>
     * (引数) codeColumn:null<br>
     * (引数) languageColumn:null<br>
     * (引数) countryColumn:null<br>
     * (引数) variantColumn:null<br>
     * (引数) messageColumn:null<br>
     * <br>
     * 期待値：(状態変化) rsCodeColumn:null<br>
     * (状態変化) rsLanguageColumn:null<br>
     * (状態変化) rsCountryColumn:null<br>
     * (状態変化) rsVariantColumn:null<br>
     * (状態変化) message:null<br>
     * (状態変化) compile():呼び出されたことを確認する。<br>
     * <br>
     * 引数がnullだった場合、引き渡された値が変化なく格納されるかを確認。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testDBMessageDataSource03() throws Exception {
        // 前処理
        DataSource ds = new MockDataSource();
        DBMessageQuery db = new DBMessageQuery(ds, "SELECT CODE,MESSAGE FROM MESSAGES", null, null, null, null, null);

        // テスト実施

        // 判定
        assertNull(ReflectionTestUtils.getField(db, "rsCodeColumn"));
        assertNull(ReflectionTestUtils.getField(db, "rsLanguageColumn"));
        assertNull(ReflectionTestUtils.getField(db, "rsCountryColumn"));
        assertNull(ReflectionTestUtils.getField(db, "rsVariantColumn"));
        assertNull(ReflectionTestUtils.getField(db, "rsMessageColumn"));
        assertTrue(db.isCompiled());
    }

    /**
     * testMapRow01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A,E <br>
     * <br>
     * 入力値：(引数) rs:|"code"="test01"|"language"="ja"|"country"="JP" |"variant"="kaisai"|"message"="テストメッセージ０１"|"hoge"="関係ないカラム"|
<<<<<<< HEAD
     * <br>
     * という内容のResultSet<br>
     * (引数) rowNum:not null<br>
     * <br>
     * 期待値：(戻り値) DBMessage Bean:code->"test01"<br>
     * language->"ja"<br>
     * country->"JP"<br>
     * variant->"kansai"<br>
     * message->"テストメッセージ０１"<br>
     * <br>
=======
     * <br>
     * という内容のResultSet<br>
     * (引数) rowNum:not null<br>
     * <br>
     * 期待値：(戻り値) DBMessage Bean:code->"test01"<br>
     * language->"ja"<br>
     * country->"JP"<br>
     * variant->"kansai"<br>
     * message->"テストメッセージ０１"<br>
     * <br>
>>>>>>> 9e2585cc0f431d938fdb24bd9f2d84a23b1f5e2f
     * ResultSetのカラムの内容がStringであった場合、値の取得が出来るかの確認する。 また、要求していない"hoge"カラムがある場合、エラーにならずに無視するかを 確認する。<br>
     * 取得したString文字列をそのままDBMessageBeanに格納する。 "hoge"カラムは無視され、どこにも影響しない。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMapRow01() throws Exception {
        // 前処理
        DataSource ds = new MockDataSource();
        DBMessageQuery db = new DBMessageQuery(ds, "SELECT CODE,MESSAGE FROM MESSAGES", "CODE", "LANGUAGE", "COUNTRY", "VARIANT", "MESSAGE");
        int rowNum = 0;
        db.rsCodeColumn = "code";
        db.rsLanguageColumn = "language";
        db.rsCountryColumn = "country";
        db.rsVariantColumn = "variant";
        db.rsMessageColumn = "message";

        // 擬似ResultSetの設定
        MockResultSet rs = new MockResultSet("TestResult");

        List<String> list1 = new ArrayList<String>();
        list1.add("test01");
        rs.addColumn("code", list1);

        List<String> list2 = new ArrayList<String>();
        list2.add("ja");
        rs.addColumn("language", list2);

        List<String> list3 = new ArrayList<String>();
        list3.add("JP");
        rs.addColumn("country", list3);

        List<String> list4 = new ArrayList<String>();
        list4.add("kansai");
        rs.addColumn("variant", list4);

        List<String> list5 = new ArrayList<String>();
        list5.add("テストメッセージ０１");
        rs.addColumn("message", list5);

        rs.first();

        // テスト実施
        DBMessage dbmReturn = (DBMessage) db.mapRow(rs, rowNum);

        // 判定
        assertEquals("test01", dbmReturn.getCode());
        assertEquals("ja", dbmReturn.getLanguage());
        assertEquals("JP", dbmReturn.getCountry());
        assertEquals("kansai", dbmReturn.getVariant());
        assertEquals("テストメッセージ０１", dbmReturn.getMessage());
    }

    /**
     * testMapRow02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A,E <br>
     * <br>
     * 入力値：(引数) rs:|"code"=""|"language"=""|"country"=""|"variant"=""| "message"=""|"hoge"=""|<br>
     * という内容のResultSet<br>
     * (引数) rowNum:not null<br>
     * <br>
     * 期待値：(戻り値) DBMessage Bean:code->""<br>
     * language->""<br>
     * country->""<br>
     * variant->""<br>
     * message->""<br>
     * <br>
     * ResultSetのカラムの内容が空文字であった場合、値の取得が出来るかの確認する。 また、要求していない"hoge"カラムがある場合、エラーにならずに無視するかを 確認する。<br>
     * 取得した空文字をそのままDBMessageBeanに格納する。"hoge"カラムは無視され、 どこにも影響しない。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMapRow02() throws Exception {
        // 前処理
        DataSource ds = new MockDataSource();
        DBMessageQuery db = new DBMessageQuery(ds, "SELECT CODE,MESSAGE FROM MESSAGES", "CODE", "LANGUAGE", "COUNTRY", "VARIANT", "MESSAGE");
        int rowNum = 0;
        db.rsCodeColumn = "code";
        db.rsLanguageColumn = "language";
        db.rsCountryColumn = "country";
        db.rsVariantColumn = "variant";
        db.rsMessageColumn = "message";

        // 擬似ResultSetの設定
        MockResultSet rs = new MockResultSet("TestResult");

        List<String> list1 = new ArrayList<String>();
        list1.add("");
        rs.addColumn("code", list1);

        List<String> list2 = new ArrayList<String>();
        list2.add("");
        rs.addColumn("language", list2);

        List<String> list3 = new ArrayList<String>();
        list3.add("");
        rs.addColumn("country", list3);

        List<String> list4 = new ArrayList<String>();
        list4.add("");
        rs.addColumn("variant", list4);

        List<String> list5 = new ArrayList<String>();
        list5.add("");
        rs.addColumn("message", list5);

        rs.first();

        // テスト実施
        DBMessage dbmReturn = (DBMessage) db.mapRow(rs, rowNum);

        // 判定
        assertEquals("", dbmReturn.getCode());
        assertEquals("", dbmReturn.getLanguage());
        assertEquals("", dbmReturn.getCountry());
        assertEquals("", dbmReturn.getVariant());
        assertEquals("", dbmReturn.getMessage());
    }

    /**
     * testMapRowResultSetint03() <br>
     * <br>
     * (異常系) <br>
     * 観点：A,E <br>
     * <br>
     * 入力値：(引数) rs:|"code"=null|"language"=null|"country"=null |"variant"=null|"message"=null|"hoge"=null|<br>
     * という内容のResultSet<br>
     * (引数) rowNum:not null<br>
     * <br>
     * 期待値：(戻り値) DBMessage Bean:code->""<br>
     * language->""<br>
     * country->""<br>
     * variant->""<br>
     * message->""<br>
     * (状態変化) ログ:【警告ログ】<br>
     * ＜メッセージ＞<br>
     * "MessageCode is null"<br>
     * <br>
     * ResultSetのカラムの内容がnullであった場合、"hoge"カラムを除き、空文字に 変換してDBMessageBeanに格納する。またMessageCodeカラムの内容がnullで
     * あった場合は、警告ログを出力する。"hoge"カラムは無視され、 どこにも影響しない。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMapRowResultSetint03() throws Exception {
        // 前処理
        logger.setEnabledLevels(Level.WARN);
        DataSource ds = new MockDataSource();
        DBMessageQuery db = new DBMessageQuery(ds, "SELECT CODE,MESSAGE FROM MESSAGES", "CODE", "LANGUAGE", "COUNTRY", "VARIANT", "MESSAGE");
        int rowNum = 0;
        db.rsCodeColumn = "code";
        db.rsLanguageColumn = "language";
        db.rsCountryColumn = "country";
        db.rsVariantColumn = "variant";
        db.rsMessageColumn = "message";

        // 擬似ResultSetの設定
        MockResultSet rs = new MockResultSet("TestResult");

        List<String> list1 = new ArrayList<String>();
        list1.add(null);
        rs.addColumn("code", list1);

        List<String> list2 = new ArrayList<String>();
        list2.add(null);
        rs.addColumn("language", list2);

        List<String> list3 = new ArrayList<String>();
        list3.add(null);
        rs.addColumn("country", list3);

        List<String> list4 = new ArrayList<String>();
        list4.add(null);
        rs.addColumn("variant", list4);

        List<String> list5 = new ArrayList<String>();
        list5.add(null);
        rs.addColumn("message", list5);

        rs.first();

        // テスト実施
        logger.clear();
        DBMessage dbmReturn = (DBMessage) db.mapRow(rs, rowNum);

        // 判定
        assertEquals("", dbmReturn.getCode());
        assertEquals("", dbmReturn.getLanguage());
        assertEquals("", dbmReturn.getCountry());
        assertEquals("", dbmReturn.getVariant());
        assertEquals("", dbmReturn.getMessage());
        assertThat(logger.getLoggingEvents(), is(asList(warn(
                "MessageCode is null"), debug(",,,,"))));

    }
}
