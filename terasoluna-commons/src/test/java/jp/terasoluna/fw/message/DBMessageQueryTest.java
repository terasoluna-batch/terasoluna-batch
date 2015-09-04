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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Test;

import jp.terasoluna.utlib.MockDataSource;
import jp.terasoluna.utlib.UTUtil;
import static org.mockito.Mockito.*;

import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * {@link jp.terasoluna.fw.message.DBMessageQuery} クラスのブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4> メッセージリソースを取得するRDBMSオペレーションクラス
 * <p>
 * @see jp.terasoluna.fw.message.DBMessageQuery
 */
public class DBMessageQueryTest {

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            DBMessageQuery.class);

    /**
     * テスト後処理：ロガーのクリアを行う。
     */
    @After
    public void tearDown() {
        logger.clear();
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
    @Test
    public void testDBMessageDataSource01() throws Exception {
        // 前処理
        DataSource ds = new MockDataSource();
        DBMessageQuery db = new DBMessageQuery(ds, "SELECT CODE,MESSAGE FROM MESSAGES", "CODE", "LANGUAGE", "COUNTRY", "VARIANT", "MESSAGE");

        // テスト実施

        // 判定
        assertEquals("CODE", UTUtil.getPrivateField(db, "rsCodeColumn"));
        assertEquals("LANGUAGE", UTUtil.getPrivateField(db,
                "rsLanguageColumn"));
        assertEquals("COUNTRY", UTUtil.getPrivateField(db, "rsCountryColumn"));
        assertEquals("VARIANT", UTUtil.getPrivateField(db, "rsVariantColumn"));
        assertEquals("MESSAGE", UTUtil.getPrivateField(db, "rsMessageColumn"));
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
    @Test
    public void testDBMessageDataSource02() throws Exception {
        // 前処理
        DataSource ds = new MockDataSource();
        DBMessageQuery db = new DBMessageQuery(ds, "SELECT CODE,MESSAGE FROM MESSAGES", "", "", "", "", "");

        // テスト実施

        // 判定
        assertEquals("", UTUtil.getPrivateField(db, "rsCodeColumn"));
        assertEquals("", UTUtil.getPrivateField(db, "rsLanguageColumn"));
        assertEquals("", UTUtil.getPrivateField(db, "rsCountryColumn"));
        assertEquals("", UTUtil.getPrivateField(db, "rsVariantColumn"));
        assertEquals("", UTUtil.getPrivateField(db, "rsMessageColumn"));
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
    @Test
    public void testDBMessageDataSource03() throws Exception {
        // 前処理
        DataSource ds = new MockDataSource();
        DBMessageQuery db = new DBMessageQuery(ds, "SELECT CODE,MESSAGE FROM MESSAGES", null, null, null, null, null);

        // テスト実施

        // 判定
        assertNull(UTUtil.getPrivateField(db, "rsCodeColumn"));
        assertNull(UTUtil.getPrivateField(db, "rsLanguageColumn"));
        assertNull(UTUtil.getPrivateField(db, "rsCountryColumn"));
        assertNull(UTUtil.getPrivateField(db, "rsVariantColumn"));
        assertNull(UTUtil.getPrivateField(db, "rsMessageColumn"));
        assertTrue(db.isCompiled());
    }

    /**
     * testMapRow01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A,E <br>
     * <br>
     * 入力値：(引数) rs:|"code"="test01"|"language"="ja"|"country"="JP" |"variant"="kaisai"|"message"="テストメッセージ０１"|"hoge"="関係ないカラム"|
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
     * ResultSetのカラムの内容がStringであった場合、値の取得が出来るかの確認する。 また、要求していない"hoge"カラムがある場合、エラーにならずに無視するかを 確認する。<br>
     * 取得したString文字列をそのままDBMessageBeanに格納する。 "hoge"カラムは無視され、どこにも影響しない。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
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
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString("code")).thenReturn("test01");
        when(rs.getString("language")).thenReturn("ja");
        when(rs.getString("country")).thenReturn("JP");
        when(rs.getString("variant")).thenReturn("kansai");
        when(rs.getString("message")).thenReturn("テストメッセージ０１");

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
    @Test
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
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString("code")).thenReturn("");
        when(rs.getString("language")).thenReturn("");
        when(rs.getString("country")).thenReturn("");
        when(rs.getString("variant")).thenReturn("");
        when(rs.getString("message")).thenReturn("");

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
    @Test
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
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString("code")).thenReturn(null);
        when(rs.getString("language")).thenReturn(null);
        when(rs.getString("country")).thenReturn(null);
        when(rs.getString("variant")).thenReturn(null);
        when(rs.getString("message")).thenReturn(null);

        rs.first();

        // 不要なログをクリア
        logger.clear();

        // テスト実施
        DBMessage dbmReturn = (DBMessage) db.mapRow(rs, rowNum);

        // 判定
        assertEquals("", dbmReturn.getCode());
        assertEquals("", dbmReturn.getLanguage());
        assertEquals("", dbmReturn.getCountry());
        assertEquals("", dbmReturn.getVariant());
        assertEquals("", dbmReturn.getMessage());

        assertTrue(logger.getLoggingEvents().get(0).getMessage().contains(
                "MessageCode is null") && logger.getLoggingEvents().get(0)
                        .getLevel() == Level.WARN);
    }
}
