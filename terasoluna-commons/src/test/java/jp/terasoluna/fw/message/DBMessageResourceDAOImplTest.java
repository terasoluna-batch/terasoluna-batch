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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import jp.terasoluna.utlib.MockDataSource;
import jp.terasoluna.utlib.UTUtil;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.message.DBMessageResourceDAOImpl} クラスのブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4> DBからメッセージリソースを取得するDBMessageResourceDAOの実装クラス
 * <p>
 * @see jp.terasoluna.fw.message.DBMessageResourceDAOImpl
 */
public class DBMessageResourceDAOImplTest {

    private TestLogger logger = TestLoggerFactory.getTestLogger(
            DBMessageResourceDAOImpl.class);

    /**
     * testSetTableName01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) tableName:"test01"<br>
     * (状態) this.tableName:"MESSAGES"<br>
     * <br>
     * 期待値：(状態変化) this.tableName:引数で設定した値<br>
     * <br>
     * tableName属性のsetterメソッドのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetTableName01() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl daoImpl = new DBMessageResourceDAOImpl();
        daoImpl.tableName = "MESSAGES";

        // テスト実施
        daoImpl.setTableName("test01");

        // 判定
        assertEquals("test01", UTUtil.getPrivateField(daoImpl, "tableName"));
    }

    /**
     * testSetCodeColumn01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) codeColumn:"test01"<br>
     * (状態) this.codeColumn:"CODE"<br>
     * <br>
     * 期待値：(状態変化) this.codeColumn:引数で設定した値<br>
     * <br>
     * codeColumn属性のsetterメソッドのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetCodeColumn01() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl daoImpl = new DBMessageResourceDAOImpl();
        daoImpl.codeColumn = "CODE";

        // テスト実施
        daoImpl.setCodeColumn("test01");

        // 判定
        assertEquals("test01", UTUtil.getPrivateField(daoImpl, "codeColumn"));
    }

    /**
     * testSetLaunguageColumn01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) languageColumn:"test01"<br>
     * (状態) this.languageColumn:null<br>
     * <br>
     * 期待値：(状態変化) this.languageColumn:引数で設定した値<br>
     * <br>
     * languageColumn属性のsetterメソッドのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetLaunguageColumn01() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl daoImpl = new DBMessageResourceDAOImpl();
        daoImpl.languageColumn = null;

        // テスト実施
        daoImpl.setLanguageColumn("test01");

        // 判定
        assertEquals("test01", UTUtil.getPrivateField(daoImpl,
                "languageColumn"));
    }

    /**
     * testSetCountryColumn01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) countryColumn:"test01"<br>
     * (状態) this.countryColumn:null<br>
     * <br>
     * 期待値：(状態変化) this.countryColumn:引数で設定した値<br>
     * <br>
     * countryColumn属性のsetterメソッドのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetCountryColumn01() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl daoImpl = new DBMessageResourceDAOImpl();
        daoImpl.countryColumn = null;

        // テスト実施
        daoImpl.setCountryColumn("test01");

        // 判定
        assertEquals("test01", UTUtil.getPrivateField(daoImpl,
                "countryColumn"));
    }

    /**
     * testSetVariantColumn01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) variantColumn:"test01"<br>
     * (状態) this.variantColumn:null<br>
     * <br>
     * 期待値：(状態変化) this.variantColumn:引数で設定した値<br>
     * <br>
     * variantColumn属性のsetterメソッドのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetVariantColumn01() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl daoImpl = new DBMessageResourceDAOImpl();
        daoImpl.variantColumn = null;

        // テスト実施
        daoImpl.setVariantColumn("test01");

        // 判定
        assertEquals("test01", UTUtil.getPrivateField(daoImpl,
                "variantColumn"));
    }

    /**
     * testSetMessageColumn01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) messageColumn:"test01"<br>
     * (状態) this.messageColumn:"MESSAGE"<br>
     * <br>
     * 期待値：(状態変化) this.messageColumn:引数で設定した値<br>
     * <br>
     * messageColumn属性のsetterメソッドのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetMessageColumn01() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl daoImpl = new DBMessageResourceDAOImpl();
        daoImpl.messageColumn = "MESSAGE";

        // テスト実施
        daoImpl.setMessageColumn("test01");

        // 判定
        assertEquals("test01", UTUtil.getPrivateField(daoImpl,
                "messageColumn"));
    }

    /**
     * testSetFindMessageSql01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) findMessageSql:"test01"<br>
     * (状態) this.findMessageSql:null<br>
     * <br>
     * 期待値：(状態変化) this.findMessageSql:引数で設定した値<br>
     * <br>
     * findMessageSql属性のsetterメソッドのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetFindMessageSql01() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl daoImpl = new DBMessageResourceDAOImpl();
        daoImpl.findMessageSql = null;

        // テスト実施
        daoImpl.setFindMessageSql("test01");

        // 判定
        assertEquals("test01", UTUtil.getPrivateField(daoImpl,
                "findMessageSql"));
    }

    /**
     * testInitDao01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(状態) getDataSource():not null<br>
     * (状態) makeSql():"SELECT FOO FROM BAR"<br>
     * (状態) dBMessageQuery:null<br>
     * (状態) codeColumn:"CODE"<br>
     * (状態) languageColumn:"LANGUAGE"<br>
     * (状態) countryColumn:"COUNTRY"<br>
     * (状態) variantColumn:"VARIANT"<br>
     * (状態) messageColumn:"MESSAGE"<br>
     * <br>
     * 期待値：(状態変化) DBMessageQuery:DBMessageQueryオブジェクトが生成される。 <br>
     * <br>
     * DBMessageQueryが呼び出され、this.dBMessageQueryに格納されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInitDao01() throws Exception {
        // 前処理
        DataSource dataSource = new MockDataSource();
        DBMessageResourceDAOImpl_JdbcTemplateStub01 jdbc = new DBMessageResourceDAOImpl_JdbcTemplateStub01();
        DBMessageResourceDAOImpl_DBMessageResourceDAOImplStub01 dbmr = new DBMessageResourceDAOImpl_DBMessageResourceDAOImplStub01();
        jdbc.ds = dataSource;
        UTUtil.setPrivateField(dbmr, "jdbcTemplate", jdbc);

        dbmr.dBMessageQuery = null;
        dbmr.codeColumn = "CODE";
        dbmr.languageColumn = "LANGUAGE";
        dbmr.countryColumn = "COUNTRY";
        dbmr.variantColumn = "VARIANT";
        dbmr.messageColumn = "MESSAGE";

        // テスト実施
        dbmr.initDao();

        // 判定
        DBMessageQuery query = dbmr.dBMessageQuery;

        assertEquals("CODE", query.rsCodeColumn);
        assertEquals("LANGUAGE", query.rsLanguageColumn);
        assertEquals("COUNTRY", query.rsCountryColumn);
        assertEquals("VARIANT", query.rsVariantColumn);
        assertEquals("MESSAGE", query.rsMessageColumn);
        assertTrue(dbmr.isRead);
    }

    /**
     * testInitDao02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) getDataSource():null<br>
     * (状態) makeSql():"SELECT FOO FROM BAR"<br>
     * (状態) dBMessageQuery:null<br>
     * (状態) codeColumn:"CODE"<br>
     * (状態) languageColumn:null<br>
     * (状態) countryColumn:null<br>
     * (状態) variantColumn:null<br>
     * (状態) messageColumn:"MESSAGE"<br>
     * <br>
     * 期待値：(状態変化) DBMessageQuery:ー<br>
     * (状態変化) 例外:illgalArgumentExceptionが発生することを確認する。 <br>
     * ＜メッセージ＞<br>
     * Missing dataSource in spring configuration file.<br>
     * (状態変化) ログ:【エラーログ】<br>
     * ＜メッセージ＞<br>
     * Missing dataSource in spring configuration file.<br>
     * <br>
     * DBMessageQueryが呼び出されず、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInitDao02() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl_JdbcTemplateStub01 jdbc = new DBMessageResourceDAOImpl_JdbcTemplateStub01();
        DBMessageResourceDAOImpl_DBMessageResourceDAOImplStub01 dbmr = new DBMessageResourceDAOImpl_DBMessageResourceDAOImplStub01();
        jdbc.ds = null;

        dbmr.dBMessageQuery = null;

        dbmr.codeColumn = "CODE";
        dbmr.languageColumn = "LANGUAGE";
        dbmr.countryColumn = "COUNTRY";
        dbmr.variantColumn = "VARIANT";
        dbmr.messageColumn = "MESSAGE";

        // テスト実施
        try {
            dbmr.initDao();
            fail();
        } catch (IllegalArgumentException e) {
            // 判定
            assertEquals("Missing dataSource in spring configuration file.", e
                    .getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "Missing dataSource in spring configuration file."))));
        }
    }

    /**
     * testInitDao03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(状態) getDataSource():not null<br>
     * (状態) makeSql():"SELECT FOO FROM BAR"<br>
     * (状態) dBMessageQuery:null<br>
     * (状態) codeColumn:"CODE"<br>
     * (状態) languageColumn:null<br>
     * (状態) countryColumn:null<br>
     * (状態) variantColumn:null<br>
     * (状態) messageColumn:"MESSAGE"<br>
     * <br>
     * 期待値：(状態変化) DBMessageQuery:DBMessageQueryオブジェクトが生成される。 <br>
     * <br>
     * DBMessageQueryが呼び出され、this.dBMessageQueryに格納されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testInitDao03() throws Exception {
        // 前処理
        DataSource dataSource = new MockDataSource();
        DBMessageResourceDAOImpl_JdbcTemplateStub01 jdbc = new DBMessageResourceDAOImpl_JdbcTemplateStub01();
        DBMessageResourceDAOImpl_DBMessageResourceDAOImplStub01 dbmr = new DBMessageResourceDAOImpl_DBMessageResourceDAOImplStub01();
        jdbc.ds = dataSource;
        UTUtil.setPrivateField(dbmr, "jdbcTemplate", jdbc);

        dbmr.dBMessageQuery = null;
        dbmr.codeColumn = "CODE";
        dbmr.languageColumn = null;
        dbmr.countryColumn = null;
        dbmr.variantColumn = null;
        dbmr.messageColumn = "MESSAGE";

        // テスト実施
        dbmr.initDao();

        // 判定
        // 判定
        DBMessageQuery query = dbmr.dBMessageQuery;

        assertEquals("CODE", query.rsCodeColumn);
        assertNull(query.rsLanguageColumn);
        assertNull(query.rsCountryColumn);
        assertNull(query.rsVariantColumn);
        assertEquals("MESSAGE", query.rsMessageColumn);
        assertTrue(dbmr.isRead);
    }

    /**
     * testMakeSql01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(状態) codeColumn:"CODE"<br>
     * (状態) languageColumn:"LANGUAGE"<br>
     * (状態) countryColumn:"COUNTRY"<br>
     * (状態) variantColumn:"VARIANT"<br>
     * (状態) messageColumn:"MESSAGE"<br>
     * (状態) tableName:"MESSAGES"<br>
     * (状態) findMessageSql:null<br>
     * <br>
     * 期待値：(戻り値) sql:"SELECT CODE,LANGUAGE,COUNTRY,VARIANT,MESSAGE FROM MESSAGES"<br>
     * <br>
     * 引数としてあたえられた文字列からＳＱＬ文を作成し、Stringで返却する。<br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMakeSql01() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl dbmr = new DBMessageResourceDAOImpl();
        dbmr.codeColumn = "CODE";
        dbmr.languageColumn = "LANGUAGE";
        dbmr.countryColumn = "COUNTRY";
        dbmr.variantColumn = "VARIANT";
        dbmr.messageColumn = "MESSAGE";
        dbmr.tableName = "MESSAGES";
        dbmr.findMessageSql = null;

        // テスト実施
        String sql = dbmr.makeSql();

        // 判定
        assertEquals(
                "SELECT CODE,LANGUAGE,COUNTRY,VARIANT,MESSAGE FROM MESSAGES",
                sql);
    }

    /**
     * testMakeSql02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G<br>
     * <br>
     * 入力値：(状態) codeColumn:""<br>
     * (状態) languageColumn:"LANGUAGE"<br>
     * (状態) countryColumn:"COUNTRY"<br>
     * (状態) variantColumn:"VARIANT"<br>
     * (状態) messageColumn:"MESSAGE"<br>
     * (状態) tableName:"MESSAGES"<br>
     * (状態) findMessageSql:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException：<br>
     * ＜メッセージ＞<br>
     * "codeColumn is illegalAurgument"<br>
     * (状態変化) ログ:【エラーログ】<br>
     * ＜メッセージ＞<br>
     * "codeColumn is illegalAurgument"<br>
     * <br>
     * コードカラムが空文字のため、呼び出し先メソッドにて停止する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMakeSql02() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl dbmr = new DBMessageResourceDAOImpl();
        dbmr.codeColumn = "";
        dbmr.languageColumn = "LANGUAGE";
        dbmr.countryColumn = "COUNTRY";
        dbmr.variantColumn = "VARIANT";
        dbmr.messageColumn = "MESSAGE";
        dbmr.tableName = "MESSAGES";
        dbmr.findMessageSql = null;

        // テスト実施
        try {
            logger.clear();
            dbmr.makeSql();
            fail();
        } catch (IllegalArgumentException e) {

            // 判定
            assertEquals("illegalArgument: codeColumn is null or empty.", e
                    .getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "illegalArgument: codeColumn is null or empty."))));
        }
    }

    /**
     * testMakeSql03() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) codeColumn:null<br>
     * (状態) languageColumn:"LANGUAGE"<br>
     * (状態) countryColumn:"COUNTRY"<br>
     * (状態) variantColumn:"VARIANT"<br>
     * (状態) messageColumn:"MESSAGE"<br>
     * (状態) tableName:"MESSAGES"<br>
     * (状態) findMessageSql:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException：<br>
     * ＜メッセージ＞<br>
     * "codeColumn is illegalArgument"<br>
     * (状態変化) ログ:【エラーログ】<br>
     * ＜メッセージ＞<br>
     * "codeColumn is illegalArgument"<br>
     * <br>
     * コードカラムがnullのため、呼び出し先メソッドにて停止する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMakeSql03() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl dbmr = new DBMessageResourceDAOImpl();
        dbmr.codeColumn = null;
        dbmr.languageColumn = "LANGUAGE";
        dbmr.countryColumn = "COUNTRY";
        dbmr.variantColumn = "VARIANT";
        dbmr.messageColumn = "MESSAGE";
        dbmr.tableName = "MESSAGES";
        dbmr.findMessageSql = null;

        // テスト実施
        try {
            logger.clear();
            dbmr.makeSql();
            fail();
        } catch (IllegalArgumentException e) {

            // 判定
            assertEquals("illegalArgument: codeColumn is null or empty.", e
                    .getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "illegalArgument: codeColumn is null or empty."))));
        }
    }

    /**
     * testMakeSql04() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) codeColumn:"CODE"<br>
     * (状態) languageColumn:""<br>
     * (状態) countryColumn:null<br>
     * (状態) variantColumn:null<br>
     * (状態) messageColumn:null<br>
     * (状態) tableName:"MESSAGES"<br>
     * (状態) findMessageSql:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException：<br>
     * ＜メッセージ＞<br>
     * "languageColumn is illegalArgument"<br>
     * (状態変化) ログ:【エラーログ】<br>
     * ＜メッセージ＞<br>
     * "languageColumn is illegalArgument"<br>
     * <br>
     * 言語カラムが空文字のため、呼び出し先メソッドにて停止する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMakeSql04() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl dbmr = new DBMessageResourceDAOImpl();
        dbmr.codeColumn = "CODE";
        dbmr.languageColumn = "";
        dbmr.countryColumn = null;
        dbmr.variantColumn = null;
        dbmr.messageColumn = null;
        dbmr.tableName = "MESSAGES";
        dbmr.findMessageSql = null;

        // テスト実施
        try {
            logger.clear();
            dbmr.makeSql();
            fail();
        } catch (IllegalArgumentException e) {

            // 判定
            assertEquals("illegalArgument: languageColumn is empty.", e
                    .getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "illegalArgument: languageColumn is empty."))));
        }
    }

    /**
     * testMakeSql05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) codeColumn:"CODE"<br>
     * (状態) languageColumn:null<br>
     * (状態) countryColumn:""<br>
     * (状態) variantColumn:null<br>
     * (状態) messageColumn:null<br>
     * (状態) tableName:"MESSAGES"<br>
     * (状態) findMessageSql:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException：<br>
     * ＜メッセージ＞<br>
     * "countryColumn is illegalArgument"<br>
     * (状態変化) ログ:【エラーログ】<br>
     * ＜メッセージ＞<br>
     * "countryColumn is illegalArgument"<br>
     * <br>
     * 国カラムが空文字のため、呼び出し先メソッドにて停止する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMakeSql05() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl dbmr = new DBMessageResourceDAOImpl();
        dbmr.codeColumn = "CODE";
        dbmr.languageColumn = null;
        dbmr.countryColumn = "";
        dbmr.variantColumn = null;
        dbmr.messageColumn = null;
        dbmr.tableName = "MESSAGES";
        dbmr.findMessageSql = null;

        // テスト実施
        try {
            logger.clear();
            dbmr.makeSql();
            fail();
        } catch (IllegalArgumentException e) {

            // 判定
            assertEquals("illegalArgument: countryColumn is empty.", e
                    .getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "illegalArgument: countryColumn is empty."))));
        }
    }

    /**
     * testMakeSql06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) codeColumn:"CODE"<br>
     * (状態) languageColumn:null<br>
     * (状態) countryColumn:null<br>
     * (状態) variantColumn:""<br>
     * (状態) messageColumn:null<br>
     * (状態) tableName:"MESSAGES"<br>
     * (状態) findMessageSql:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException：<br>
     * ＜メッセージ＞<br>
     * "variantColumn is illegalArgument"<br>
     * (状態変化) ログ:【エラーログ】<br>
     * ＜メッセージ＞<br>
     * "variantColumn is illegalArgument"<br>
     * <br>
     * バリアントカラムが空文字のため、呼び出し先メソッドにて停止する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMakeSql06() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl dbmr = new DBMessageResourceDAOImpl();
        dbmr.codeColumn = "CODE";
        dbmr.languageColumn = null;
        dbmr.countryColumn = null;
        dbmr.variantColumn = "";
        dbmr.messageColumn = null;
        dbmr.tableName = "MESSAGES";
        dbmr.findMessageSql = null;

        // テスト実施
        try {
            logger.clear();
            dbmr.makeSql();
            fail();
        } catch (IllegalArgumentException e) {

            // 判定
            assertEquals("illegalArgument: variantColumn is empty.", e
                    .getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "illegalArgument: variantColumn is empty."))));
        }
    }

    /**
     * testMakeSql07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) codeColumn:"CODE"<br>
     * (状態) languageColumn:null<br>
     * (状態) countryColumn:null<br>
     * (状態) variantColumn:null<br>
     * (状態) messageColumn:""<br>
     * (状態) tableName:"MESSAGES"<br>
     * (状態) findMessageSql:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException：<br>
     * ＜メッセージ＞<br>
     * "messageColumn is illegalArgument"<br>
     * (状態変化) ログ:【エラーログ】<br>
     * ＜メッセージ＞<br>
     * "messageColumn is illegalArgument"<br>
     * <br>
     * メッセージカラムが空文字のため、呼び出し先メソッドにて停止する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMakeSql07() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl dbmr = new DBMessageResourceDAOImpl();
        dbmr.codeColumn = "CODE";
        dbmr.languageColumn = null;
        dbmr.countryColumn = null;
        dbmr.variantColumn = null;
        dbmr.messageColumn = "";
        dbmr.tableName = "MESSAGES";
        dbmr.findMessageSql = null;

        // テスト実施
        try {
            logger.clear();
            dbmr.makeSql();
            fail();
        } catch (IllegalArgumentException e) {

            // 判定
            assertEquals("illegalArgument: messageColumn is null or empty.", e
                    .getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "illegalArgument: messageColumn is null or empty."))));
        }
    }

    /**
     * testMakeSql08() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) codeColumn:"CODE"<br>
     * (状態) languageColumn:null<br>
     * (状態) countryColumn:null<br>
     * (状態) variantColumn:null<br>
     * (状態) messageColumn:null<br>
     * (状態) tableName:"MESSAGES"<br>
     * (状態) findMessageSql:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException：<br>
     * ＜メッセージ＞<br>
     * "MessageColumn is illegalArgument"<br>
     * (状態変化) ログ:【エラーログ】<br>
     * ＜メッセージ＞<br>
     * "MessageColumn is illegalArgument"<br>
     * <br>
     * メッセージカラムがnullのため、呼び出し先メソッドにて停止する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMakeSql08() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl dbmr = new DBMessageResourceDAOImpl();
        dbmr.codeColumn = "CODE";
        dbmr.languageColumn = null;
        dbmr.countryColumn = null;
        dbmr.variantColumn = null;
        dbmr.messageColumn = null;
        dbmr.tableName = "MESSAGES";
        dbmr.findMessageSql = null;

        // テスト実施
        try {
            logger.clear();
            dbmr.makeSql();
            fail();
        } catch (IllegalArgumentException e) {

            // 判定
            assertEquals("illegalArgument: messageColumn is null or empty.", e
                    .getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "illegalArgument: messageColumn is null or empty."))));
        }
    }

    /**
     * testMakeSql09() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) codeColumn:"CODE"<br>
     * (状態) languageColumn:null<br>
     * (状態) countryColumn:null<br>
     * (状態) variantColumn:null<br>
     * (状態) messageColumn:"MESSAGE"<br>
     * (状態) tableName:""<br>
     * (状態) findMessageSql:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException：<br>
     * ＜メッセージ＞<br>
     * "tableName is illegalArgument"<br>
     * (状態変化) ログ:【エラーログ】<br>
     * ＜メッセージ＞<br>
     * "tableName is illegalArgument"<br>
     * <br>
     * テーブル名が空文字のため、呼び出し先メソッドにて停止する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMakeSql09() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl dbmr = new DBMessageResourceDAOImpl();
        dbmr.codeColumn = "CODE";
        dbmr.languageColumn = null;
        dbmr.countryColumn = null;
        dbmr.variantColumn = null;
        dbmr.messageColumn = "MESSAGE";
        dbmr.tableName = "";
        dbmr.findMessageSql = null;

        // テスト実施
        try {
            logger.clear();
            dbmr.makeSql();
            fail();
        } catch (IllegalArgumentException e) {

            // 判定
            assertEquals("illegalArgument: tableName is null or empty.", e
                    .getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "illegalArgument: tableName is null or empty."))));
        }
    }

    /**
     * testMakeSql10() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(状態) codeColumn:"CODE"<br>
     * (状態) languageColumn:null<br>
     * (状態) countryColumn:null<br>
     * (状態) variantColumn:null<br>
     * (状態) messageColumn:"MESSAGE"<br>
     * (状態) tableName:null<br>
     * (状態) findMessageSql:null<br>
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException：<br>
     * ＜メッセージ＞<br>
     * "tableName is illegalArgument"<br>
     * (状態変化) ログ:【エラーログ】<br>
     * ＜メッセージ＞<br>
     * "tableName is illegalArgument"<br>
     * <br>
     * テーブル名がnullのため、呼び出し先メソッドにて停止する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMakeSql10() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl dbmr = new DBMessageResourceDAOImpl();
        dbmr.codeColumn = "CODE";
        dbmr.languageColumn = null;
        dbmr.countryColumn = null;
        dbmr.variantColumn = null;
        dbmr.messageColumn = "MESSAGE";
        dbmr.tableName = null;
        dbmr.findMessageSql = null;

        // テスト実施
        try {
            logger.clear();
            dbmr.makeSql();
            fail();
        } catch (IllegalArgumentException e) {

            // 判定
            assertEquals("illegalArgument: tableName is null or empty.", e
                    .getMessage());
            assertThat(logger.getLoggingEvents(), is(asList(error(
                    "illegalArgument: tableName is null or empty."))));
        }
    }

    /**
     * testMakeSql11() <br>
     * <br>
     * (正常系) <br>
     * 観点：A,F <br>
     * <br>
     * 入力値：(状態) codeColumn:"CODE"<br>
     * (状態) languageColumn:null<br>
     * (状態) countryColumn:null<br>
     * (状態) variantColumn:null<br>
     * (状態) messageColumn:"MESSAGE"<br>
     * (状態) tableName:"MESSAGES"<br>
     * (状態) findMessageSql:"SELECT CODE,LANGUAGE,COUNTRY,VARIANT,MESSAGE FROM MESSAGES"<br>
     * <br>
     * 期待値：(戻り値) sql:"SELECT CODE,LANGUAGE,COUNTRY,VARIANT,MESSAGE FROM MESSAGES"<br>
     * <br>
     * 引数としてあたえられたSQL文を、Stringで返却する。<br>
     * SQL文生成ロジックは使用しない。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMakeSql11() throws Exception {
        // 前処理
        DBMessageResourceDAOImpl dbmr = new DBMessageResourceDAOImpl();
        String sql = "SELECT CODE,LANGUAGE,COUNTRY,VARIANT,MESSAGE FROM MESSAGES";
        dbmr.codeColumn = "CODE";
        dbmr.languageColumn = null;
        dbmr.countryColumn = null;
        dbmr.variantColumn = null;
        dbmr.messageColumn = "MESSAGE";
        dbmr.tableName = "MESSAGES";
        dbmr.findMessageSql = sql;

        // テスト実施
        String sqlReturn = dbmr.makeSql();

        // 判定
        assertEquals(sql, sqlReturn);
    }

    /**
     * testFindDBMessages01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(状態) dBmessageQuery:not null<br>
     * <br>
     * 期待値：(戻り値) dBMessageQuery.execute()の結果<br>
     * <br>
     * executeメソッドが呼び出され、戻り値返ってくることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testFindDBMessages01() throws Exception {
        // 前処理
        DataSource ds = new MockDataSource();
        DBMessageResourceDAOImpl_DBMessageQueryStub01 query = new DBMessageResourceDAOImpl_DBMessageQueryStub01(ds, "SELECT CODE,MESSAGE FROM MESSAGES", "CODE", "LANGUAGE", "COUNTRY", "VARIANT", "MESSAGE");
        DBMessageResourceDAOImpl dbmr = new DBMessageResourceDAOImpl();
        dbmr.dBMessageQuery = query;

        List<String> list = new ArrayList<String>();
        list.add("success");
        query.list = list;

        // テスト実施
        List<?> listReturn = dbmr.findDBMessages();

        // 判定
        assertSame(list, listReturn);
    }
}
