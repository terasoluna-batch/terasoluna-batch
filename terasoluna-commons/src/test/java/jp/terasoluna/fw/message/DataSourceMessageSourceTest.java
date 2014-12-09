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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import jp.terasoluna.utlib.LogUTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.message.DataSourceMessageSource}
 * クラスのブラックボックステスト。
 * <p>
 * <h4>【クラスの概要】</h4>
 * DAOから取得したメッセージリソースより、メッセージコード及びロケールをキー<br>
 * として、メッセージもしくはメッセージフォーマットを決定するクラス
 * <p>
 * 
 * @see jp.terasoluna.fw.message.DataSourceMessageSource
 */
public class DataSourceMessageSourceTest extends TestCase {

    /**
     * 初期化処理を行う。
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 終了処理を行う。
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * コンストラクタ。
     * 
     * @param name
     *            このテストケースの名前。
     */
    public DataSourceMessageSourceTest(String name) {
        super(name);
    }

    /**
     * testSetDefaultLocale01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) defaultLocale:Locale JAPAN<br>
     *         (状態) this.defaultLocale:null<br>
     *         
     * <br>
     * 期待値：(状態変化) this.defaultLocale:引数で設定したLocale<br>
     *         
     * <br>
     * Locale属性のsetterメソッドのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testSetDefaultLocale01() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        ds.defaultLocale = null;
        
        Locale locale = Locale.JAPAN;
        
        // テスト実施
        ds.setDefaultLocale(locale);

        // 判定
        assertEquals(locale, ds.defaultLocale);
    }

    /**
     * testSetDbMessageResourceDAO01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) dbMessageResourceDAO:DBMessageResourceDAOオブジェクト<br>
     *         (状態) this.dbMessageResourceDAO:null<br>
     *         
     * <br>
     * 期待値：(状態変化) this.dbMessageResourceDAO:引数で設定したDAOオブジェクト<br>
     *         
     * <br>
     * DBMessageResourceDAO属性のsetterメソッドのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testSetDbMessageResourceDAO01() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        DataSourceMessageSource_DBMessageResoueceDAOStub01 dbmr
                = new DataSourceMessageSource_DBMessageResoueceDAOStub01();
        ds.dbMessageResourceDAO = null;

        // テスト実施
        ds.setDbMessageResourceDAO(dbmr);
        // 判定
        assertSame(dbmr, ds.dbMessageResourceDAO);
    }

    /**
     * testAfterPropertiesSet01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：
     * <br>
     * 期待値：(状態変化) readMessageFromDataSource:呼び出されているのを確認する。<br>
     *         
     * <br>
     * afterPropertiesSetが実行されるとreadMessageFromDataSourceが実行されるのを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testAfterPropertiesSet01() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub01 ds
                = new DataSourceMessageSource_DataSourceMessageSourceStub01();

        // テスト実施
        ds.afterPropertiesSet();

        // 判定
        assertTrue(ds.isRead);
    }

    /**
     * testReloadDataSourceMessage01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：
     * <br>
     * 期待値：(状態変化) readMessageFromDataSource:呼び出されているのを確認する。<br>
     *         
     * <br>
     * reloadDataSourceMessageが実行されるとreadMessageFromDataSourceが実行されるのを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testReloadDataSourceMessage01() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub01 ds 
                = new DataSourceMessageSource_DataSourceMessageSourceStub01();

        // テスト実施
        ds.reloadDataSourceMessage();

        // 判定
        assertTrue(ds.isRead);
    }

    /**
     * testReadMessageFromDataSource01() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E <br>
     * <br>
     * 入力値：(状態) dbMessageResourceDAO.findDBMessage():Listの要素数が０<br>
     *         
     * <br>
     * 期待値：(状態変化) cachedMergedProperties.clear():呼び出されることを確認する。<br>
     *         (状態変化) cachedMessageFormats.clear():呼び出されることを確認する。<br>
     *         (状態変化) dbMessageResourceDAO.findDBMessage():呼び出されることを確認する。<br>
     *         
     * <br>
     * DBMessageのキャッシュが行なわれず、何もしないで終了する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testReadMessageFromDataSource01() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub02 ds 
                = new DataSourceMessageSource_DataSourceMessageSourceStub02();
        DataSourceMessageSource_DBMessageResoueceDAOStub01 dbmr 
                = new DataSourceMessageSource_DBMessageResoueceDAOStub01();
        Properties prop = new Properties();
        HashMap map = new HashMap();
        ds.cachedMergedProperties.put(Locale.JAPAN, prop);
        ds.cachedMessageFormats.put("foo", map);
        ds.dbMessageResourceDAO = dbmr;
        
        dbmr.list = new ArrayList();
        
        // テスト実施
        ds.readMessagesFromDataSource();

        // 判定
        assertTrue(ds.cachedMergedProperties.isEmpty());
        assertTrue(ds.cachedMessageFormats.isEmpty());
        assertTrue(dbmr.isRead);
        assertEquals(0, ds.list.size());
    }

    /**
     * testReadMessageFromDataSource02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E <br>
     * <br>
     * 入力値：(状態) dbMessageResourceDAO.findDBMessage():Listの要素数が１<br>
     *                DBMessage("test01","JP","ja","kaisai","テストメッセージ０１")<br>
     *         
     * <br>
     * 期待値：(状態変化) cachedMergedProperties.clear():呼び出されることを確認する。<br>
     *         (状態変化) cachedMessageFormats.clear():呼び出されることを確認する。<br>
     *         (状態変化) dbMessageResourceDAO.findDBMessage():呼び出されることを確認する。<br>
     *         (状態変化) mapMessage():呼び出されたことを確認する。<br>
     *                    引数がわたさたれていることを確認する。<br>
     *         
     * <br>
     * DBMessageオブジェクトから値を取り出し、マップにつめなおすことを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testReadMessageFromDataSource02() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub02 ds 
                = new DataSourceMessageSource_DataSourceMessageSourceStub02();
        DataSourceMessageSource_DBMessageResoueceDAOStub01 dbmr 
                = new DataSourceMessageSource_DBMessageResoueceDAOStub01();
        Properties prop = new Properties();
        HashMap map = new HashMap();
        ds.cachedMergedProperties.put(Locale.JAPAN, prop);
        ds.cachedMessageFormats.put("foo", map);
        ds.dbMessageResourceDAO = dbmr;
        
        List<DBMessage> list = new ArrayList<DBMessage>();
        DBMessage db1 = new DBMessage("test01", "JP", "ja", "kaisai",
                "テストメッセージ０１");
        list.add(db1);
        dbmr.list = list;
        
     
        // テスト実施
        ds.readMessagesFromDataSource();

        // 判定
        assertTrue(ds.cachedMergedProperties.isEmpty());
        assertTrue(ds.cachedMessageFormats.isEmpty());
        assertTrue(dbmr.isRead);
        assertEquals(1, ds.list.size());
        assertSame(db1, ds.list.get(0));
    }

    /**
     * testReadMessageFromDataSource03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D,E <br>
     * <br>
     * 入力値：(状態) dbMessageResourceDAO.findDBMessage():LIstの要素数が３<br>
     *                DBMessage("test01","JP","ja","kaisai","テストメッセージ０１")<br>
     *                DBMessage("test02","JP","ja","kaisai","テストメッセージ０２")<br>
     *                DBMessage("test03","JP","ja","kaisai","テストメッセージ０３")<br>
     *         
     * <br>
     * 期待値：(状態変化) cachedMergedProperties.clear():呼び出されることを確認する。<br>
     *         (状態変化) cachedMessageFormats.clear():呼び出されることを確認する。<br>
     *         (状態変化) dbMessageResourceDAO.findDBMessage():呼び出されることを確認する。<br>
     *         (状態変化) mapMessage():呼び出されたことを確認する。<br>
     *                    引数がわたさたれていることを確認する。<br>
     *         
     * <br>
     * DBMessageオブジェクトから値を取り出し、マップにつめなおすことを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testReadMessageFromDataSource03() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub02 ds
                = new DataSourceMessageSource_DataSourceMessageSourceStub02();
        DataSourceMessageSource_DBMessageResoueceDAOStub01 dbmr 
                = new DataSourceMessageSource_DBMessageResoueceDAOStub01();
        Properties prop = new Properties();
        HashMap map = new HashMap();
        ds.cachedMergedProperties.put(Locale.JAPAN, prop);
        ds.cachedMessageFormats.put("foo", map);
        
        ds.dbMessageResourceDAO = dbmr;
        
        List<DBMessage> list = new ArrayList<DBMessage>();
        DBMessage db1 = new DBMessage("test01", "JP", "ja", "kaisai",
                "テストメッセージ０１");
        DBMessage db2 = new DBMessage("test02", "JP", "ja", "kaisai",
                "テストメッセージ０２");
        DBMessage db3 = new DBMessage("test03", "JP", "ja", "kaisai",
                "テストメッセージ０３");
        list.add(db1);
        list.add(db2);
        list.add(db3);
        dbmr.list = list;        

        // テスト実施
        ds.readMessagesFromDataSource();

        // 判定
        assertTrue(ds.cachedMergedProperties.isEmpty());
        assertTrue(ds.cachedMessageFormats.isEmpty());
        assertTrue(dbmr.isRead);
        assertEquals(3, ds.list.size());
        assertSame(db1, ds.list.get(0));
        assertSame(db2, ds.list.get(1));
        assertSame(db3, ds.list.get(2));
    }

    /**
     * testReadMessageFromDataSource04() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(状態) dbMessageResourceDAO.findDBMessage():Listの要素数が１<br>
     *                DBMessage("","","","","")<br>
     *         
     * <br>
     * 期待値：(状態変化) cachedMergedProperties.clear():呼び出されることを確認する。<br>
     *         (状態変化) cachedMessageFormats.clear():呼び出されることを確認する。<br>
     *         (状態変化) dbMessageResourceDAO.findDBMessage():呼び出されることを確認する。<br>
     *         (状態変化) mapMessage():呼び出されたことを確認する。<br>
     *                    引数がわたさたれていることを確認する。<br>
     *         
     * <br>
     * DBMessageオブジェクトから値を取り出し、マップにつめなおすことを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testReadMessageFromDataSource04() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub02 ds
                = new DataSourceMessageSource_DataSourceMessageSourceStub02();
        DataSourceMessageSource_DBMessageResoueceDAOStub01 dbmr 
                = new DataSourceMessageSource_DBMessageResoueceDAOStub01();
        Properties prop = new Properties();
        HashMap map = new HashMap();
        ds.cachedMergedProperties.put(Locale.JAPAN, prop);
        ds.cachedMessageFormats.put("foo", map);
        
        ds.dbMessageResourceDAO = dbmr;
        
        List<DBMessage> list = new ArrayList<DBMessage>();
        DBMessage db1 = new DBMessage("", "", "", "", "");
        
        list.add(db1);
        dbmr.list = list;           
        

        // テスト実施
        ds.readMessagesFromDataSource();

        // 判定
        assertTrue(ds.cachedMergedProperties.isEmpty());
        assertTrue(ds.cachedMessageFormats.isEmpty());
        assertTrue(dbmr.isRead);
        assertEquals(1, ds.list.size());
        assertSame(db1, ds.list.get(0));
    }

    /**
     * testReadMessageFromDataSource05() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(状態) dbMessageResourceDAO.findDBMessage():Listの要素数が１<br>
     *                DBMessage(null,null,null.null.null)<br>
     *         
     * <br>
     * 期待値：(状態変化) cachedMergedProperties.clear():呼び出されることを確認する。<br>
     *         (状態変化) cachedMessageFormats.clear():呼び出されることを確認する。<br>
     *         (状態変化) dbMessageResourceDAO.findDBMessage():呼び出されることを確認する。<br>
     *         (状態変化) mapMessage():呼び出されていないことを確認する。<br>
     *         
     * <br>
     * メッセージコードもしくはメッセージ本体がないとマップに格納されないことを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testReadMessageFromDataSource05() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub02 ds
                = new DataSourceMessageSource_DataSourceMessageSourceStub02();
        DataSourceMessageSource_DBMessageResoueceDAOStub01 dbmr 
                = new DataSourceMessageSource_DBMessageResoueceDAOStub01();
        Properties prop = new Properties();
        HashMap map = new HashMap();
        ds.cachedMergedProperties.put(Locale.JAPAN, prop);
        ds.cachedMessageFormats.put("foo", map);
        
        ds.dbMessageResourceDAO = dbmr;
        
        List list = new ArrayList();
        DBMessage db1 = new DBMessage(null, null, null, null, null);
        list.add(db1);
        dbmr.list = list;
        
        // テスト実施
        ds.readMessagesFromDataSource();

        // 判定
        assertTrue(ds.cachedMergedProperties.isEmpty());
        assertTrue(ds.cachedMessageFormats.isEmpty());
        assertTrue(dbmr.isRead);
        assertEquals(0, ds.list.size());
    }

    /**
     * testReadMessageFromDataSource06() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(状態) dbMessageResourceDAO.findDBMessage():LIstの要素数が１<br>
     *                DBMessage(null,"JP","ja","kaisai","テストメッセージ０１")<br>
     *         
     * <br>
     * 期待値：(状態変化) cachedMergedProperties.clear():呼び出されることを確認する。<br>
     *         (状態変化) cachedMessageFormats.clear():呼び出されることを確認する。<br>
     *         (状態変化) dbMessageResourceDAO.findDBMessage():呼び出されることを確認する。<br>
     *         (状態変化) mapMessage():呼び出されていないことを確認する。<br>
     *         
     * <br>
     * メッセージコードもしくはメッセージ本体がないとマップに格納されないことを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testReadMessageFromDataSource06() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub02 ds 
                = new DataSourceMessageSource_DataSourceMessageSourceStub02();
        DataSourceMessageSource_DBMessageResoueceDAOStub01 dbmr 
                = new DataSourceMessageSource_DBMessageResoueceDAOStub01();
        Properties prop = new Properties();
        HashMap map = new HashMap();
        ds.cachedMergedProperties.put(Locale.JAPAN, prop);
        ds.cachedMessageFormats.put("foo", map);

        ds.dbMessageResourceDAO = dbmr;
        
        List list = new ArrayList();
        DBMessage db1 
            = new DBMessage(null, "JP", "ja", "kaisai", "テストメッセージ０１");
        list.add(db1);
        dbmr.list = list;
        
        // テスト実施
        ds.readMessagesFromDataSource();

        // 判定
        assertTrue(ds.cachedMergedProperties.isEmpty());
        assertTrue(ds.cachedMessageFormats.isEmpty());
        assertTrue(dbmr.isRead);
        assertEquals(0, ds.list.size());
    }

    /**
     * testReadMessageFromDataSource07() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(状態) dbMessageResourceDAO.findDBMessage():Listの要素数が１<br>
     *                DBMessage("test01","JP","ja","kaisai",null)<br>
     *         
     * <br>
     * 期待値：(状態変化) cachedMergedProperties.clear():呼び出されることを確認する。<br>
     *         (状態変化) cachedMessageFormats.clear():呼び出されることを確認する。<br>
     *         (状態変化) dbMessageResourceDAO.findDBMessage():呼び出されることを確認する。<br>
     *         (状態変化) mapMessage():呼び出されていないことを確認する。<br>
     *         
     * <br>
     * メッセージコードもしくはメッセージ本体がないとマップに格納されないことを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testReadMessageFromDataSource07() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub02 ds 
                = new DataSourceMessageSource_DataSourceMessageSourceStub02();
        DataSourceMessageSource_DBMessageResoueceDAOStub01 dbmr 
                = new DataSourceMessageSource_DBMessageResoueceDAOStub01();
        Properties prop = new Properties();
        HashMap map = new HashMap();
        ds.cachedMergedProperties.put(Locale.JAPAN, prop);
        ds.cachedMessageFormats.put("foo", map);

        ds.dbMessageResourceDAO = dbmr;
        List list = new ArrayList();
        DBMessage db1 = new DBMessage("test01", "JP", "ja", "kaisai", null);
        list.add(db1);   
        
        dbmr.list = list;        
        
        // テスト実施
        ds.readMessagesFromDataSource();

        // 判定
        assertTrue(ds.cachedMergedProperties.isEmpty());
        assertTrue(ds.cachedMessageFormats.isEmpty());
        assertTrue(dbmr.isRead);
        assertEquals(0, ds.list.size());
    }

    /**
     * testReadMessageFromDataSource08() <br>
     * <br>
     * (異常系) <br>
     * 観点：E,G <br>
     * <br>
     * 入力値：(状態) dbMessageResourceDAO.findDBMessage():Listの要素数が１<br>
     *                DBMessageオブジェクト以外のオブジェクト<br>
     *         
     * <br>
     * 期待値：(状態変化) cachedMergedProperties.clear():呼び出されることを確認する。<br>
     *         (状態変化) cachedMessageFormats.clear():呼び出されることを確認する。<br>
     *         (状態変化) dbMessageResourceDAO.findDBMessage():呼び出されることを確認する。<br>
     *         (状態変化) mapMessage():呼び出されていないことを確認する。<br>
     *         (状態変化) 例外:ClassCastException<br>
     *         
     * <br>
     * DBMessageオブジェクトを渡さないと、例外が発生することを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testReadMessageFromDataSource08() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub02 ds 
                = new DataSourceMessageSource_DataSourceMessageSourceStub02();
        DataSourceMessageSource_DBMessageResoueceDAOStub01 dbmr 
                = new DataSourceMessageSource_DBMessageResoueceDAOStub01();
        Properties prop = new Properties();
        HashMap map = new HashMap();
        ds.cachedMergedProperties.put(Locale.JAPAN, prop);
        ds.cachedMessageFormats.put("foo", map);

        ds.dbMessageResourceDAO = dbmr;
        List list = new ArrayList();
        Object db1 = new Object();
        list.add(db1);
        dbmr.list = list;
        
        try {
            // テスト実施
            ds.readMessagesFromDataSource();
            fail();
        } catch (ClassCastException e) {
            // 判定
            assertTrue(ds.cachedMergedProperties.isEmpty());
            assertTrue(ds.cachedMessageFormats.isEmpty());
            assertTrue(dbmr.isRead);
            assertEquals(0, ds.list.size());
        }
    }

    /**
     * testMapMessage01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) message:DBMessageオブジェクト<br>
     *         
     * <br>
     * 期待値：(状態変化) createLocale(message):指定された引数で呼び出されていることを確認する。<br>
     *         (状態変化) getMessages:指定された引数で呼び出されていることを確認する。<br>
     *         (状態変化) messages:messageより取得した値がmessagesに格納されていることを確認する。<br>
     *         
     * <br>
     * 取得したDBMessageオブジェクトから値を取り出し、テーブルに格納することを確認。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testMapMessage01() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub03 ds 
                = new DataSourceMessageSource_DataSourceMessageSourceStub03();
        DBMessage message = new DBMessage("a", "b", "c", "d", "e");
        Locale locale = new Locale("ja", "JP");
        ds.locale = locale;
        Properties props = new Properties();
        ds.messages = props;
        
        // テスト実施
        ds.mapMessage(message);

        // 判定
        assertSame(message, ds.dbm);
        assertSame(locale, ds.locale2);
        assertSame(props, ds.messages);

    }

    /**
     * testCreateLocale01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) message:Languageカラムがnullの場合。<br>
     *                DBMessage("test01",null,null,null,"テストメッセージ０１")<br>
     *         (状態) defaultLocale:Locale.FRENCH<br>
     *         
     * <br>
     * 期待値：(戻り値) Localeオブジェクト:Locale("fr","","")<br>
     *         
     * <br>
     * 与えられた引数からLocaleが決定できない場合、デフォルトロケールを使う。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testCreateLocale01() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        DBMessage message = new DBMessage("test01", null, null, null,
                "テストメッセージ０１");
        ds.defaultLocale = Locale.FRENCH;

        // テスト実施
        Locale returnLocale = ds.createLocale(message);

        // 判定
        assertSame(ds.defaultLocale, returnLocale);
    }

    /**
     * testCreateLocale02() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) message:Languageカラムがnullの場合。<br>
     * DBMessage("test01",null,null,null,"テストメッセージ０１")<br>
     * (状態) defaultLocale:null<br>
     * <br>
     * 期待値：(戻り値) Localeオブジェクト:例外が発生する。<br>
     * (状態変化) 例外:IllegalArgumentException<br>
     * メッセージ：Can't resolve Locale.Define Locale in MessageSource or
     * Defaultlocale<br>
     * (状態変化) ログ:エラーログ：Can't resolve Locale.Define Locale in MessageSource or
     * Defaultlocale<br>
     * <br>
     * Localeが指定できず、例外を出す。 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testCreateLocale02() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        DBMessage message = new DBMessage("test01", null, null, null,
                "テストメッセージ０１");
        ds.defaultLocale = null;

        // テスト実施
        try {
            ds.createLocale(message);
            fail();
        } catch (IllegalArgumentException e) {

            // 判定
            assertEquals(
                    "Can't resolve Locale.Define Locale in MessageSource or Defaultlocale.",
                    e.getMessage());
            assertTrue(LogUTUtil
                    .checkError("Can't resolve Locale.Define Locale in MessageSource or Defaultlocale."));
        }
    }

    /**
     * testCreateLocale03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) message:Countryカラムがnullの場合。<br>
     *                DBMessage("test01","ja",null,null,"テストメッセージ０１")<br>
     *         (状態) defaultLocale:null<br>
     *         
     * <br>
     * 期待値：(戻り値) Localeオブジェクト:Locale("ja","","")<br>
     *         
     * <br>
     * 与えられた引数から、Localeを決定する。与えられる引数は言語コードである。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testCreateLocale03() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        DBMessage message = new DBMessage("test01", "ja", null, null,
                "テストメッセージ０１");
        ds.defaultLocale = null;

        // テスト実施
        Locale returnLocale = ds.createLocale(message);

        // 判定
        assertEquals("ja", returnLocale.getLanguage());
        assertEquals("", returnLocale.getCountry());
        assertEquals("", returnLocale.getVariant());
    }

    /**
     * testCreateLocale04() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) message:Variantカラムがnullの場合。<br>
     *                DBMessage("test01","ja","JP",null,"テストメッセージ０１")<br>
     *         (状態) defaultLocale:null<br>
     *         
     * <br>
     * 期待値：(戻り値) Localeオブジェクト:Locale("ja","JP","")<br>
     *         
     * <br>
     * 与えられた引数から、Localeを決定する。与えられる引数は言語コードと国コードである。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testCreateLocale04() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        DBMessage message = new DBMessage("test01", "ja", "JP", null,
                "テストメッセージ０１");
        ds.defaultLocale = null;

        // テスト実施
        Locale returnLocale = ds.createLocale(message);

        // 判定
        assertEquals("ja", returnLocale.getLanguage());
        assertEquals("JP", returnLocale.getCountry());
        assertEquals("", returnLocale.getVariant());

    }

    /**
     * testCreateLocale05() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
      * 入力値：(引数) message:Variantカラムがnullでない場合。<br>
     *                DBMessage("test01","ja","JP","kansai","テストメッセージ０１")<br>
     *         (状態) defaultLocale:null<br>
     *         
     * <br>
     * 期待値：(戻り値) Localeオブジェクト:Locale("ja","JP","kansai")<br>
     *         
     * <br>
     * 与えられた引数から、Localeを決定する。与えられる引数は言語コードと
     * 国コードとバリアントコードである。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testCreateLocale05() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        DBMessage message = new DBMessage("test01", "ja", "JP", "kansai",
                "テストメッセージ０１");
        ds.defaultLocale = null;
        
        // テスト実施
        Locale returnLocale = ds.createLocale(message);

        // 判定
        assertEquals("ja", returnLocale.getLanguage());
        assertEquals("JP", returnLocale.getCountry());
        assertEquals("kansai", returnLocale.getVariant());
    }

    /**
     * testGetMessages01() <br>
     * <br>
     * (正常系)<br>
     * 観点：F<br>
     * <br>
     * 入力値：(引数) locale:Locale("ja,JP,"")<br>
     * (状態) cachedMergedProperties.get(locale):HashMap｛<br>
     * Local(ja_JP)=Properties()<br> ｝<br>
     * <br>
     * 期待値：(戻り値) messages:Properties()<br>
     * (状態変化) cachedMergedProperties.get(locale):指定された引数で呼び出され
     * ていることを確認する。<br>
     * <br>
     * 引数をキーとしてマップから値を取り出し、その値を戻り値として返却すること
     * を確認する。 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testGetMessages01() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        Locale locale = new Locale("ja", "JP", "");
        Properties props = new Properties();
        ds.cachedMergedProperties.put(locale, props);
        
        // テスト実施
        Properties returnProp = ds.getMessages(locale);

        // 判定
        assertSame(props, returnProp);
                
    }

    /**
     * testGetMessages02() <br>
     * <br>
     * (正常系) <br>
     * 観点：F<br>
     * <br>
     * 入力値：(引数) locale:Locale("ja,JP,"")<br>
     * (状態) cachedMergedProperties.get(locale):空のHashMap<br>
     * <br>
     * 期待値：(戻り値) messages:空のPropertiesオブジェクト<br>
     * (状態変化) cachedMergedProperties.get(locale):指定された引数で
     * 呼び出されていることを確認する。<br>
     * (状態変化) cachedMergedProperties.put(locale,messages):
     * HashMap<Locale,<null,null>><br>
     * <br>
     * 引数をキーとしてマップから取り出した値がnullだった場合、新たに生成し、格納する。 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testGetMessages02() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        Locale locale = new Locale("ja", "JP", "");
        ds.cachedMergedProperties.clear();
        
        // テスト実施
        Properties returnProps = ds.getMessages(locale);

        // 判定
        assertEquals(1, ds.cachedMergedProperties.size());
        Properties result = null;
        result = ds.cachedMergedProperties.get(locale);
        
        assertSame(returnProps, result);         
    }

    /**
     * testResolveCodeWithoutArguments01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A<br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     * (引数) locale:Locale("ja","JP","")<br>
     * (状態) internalResolveCodeWithoutArguments:"テストメッセージ０１"<br>
     * <br>
     * 期待値：(戻り値) msg:"テストメッセージ０１"<br>
     * (状態変化) internalResolveCodeWithoutArguments:指定された引数で呼び出されていることを確認する。<br>
     * <br>
     * 与えられた引数よりメッセージを決定し、返却する。 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testResolveCodeWithoutArguments01() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub04 ds
                = new DataSourceMessageSource_DataSourceMessageSourceStub04();
        String code = "code01";
        Locale locale = new Locale("ja", "JP", "");
        String msg = "テストメッセージ０１";
        ds.msg = msg;
        
        // テスト実施
        String returnMsg = ds.resolveCodeWithoutArguments(code, locale);

        // 判定
        assertEquals(code, ds.code);
        assertSame(locale, ds.locale);
        assertTrue(ds.isRead);
        assertEquals(msg, returnMsg);
    }

    /**
     * testResolveCodeWithoutArguments02() <br>
     * <br>
     * (正常系)<br>
     * 観点：A,C<br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     * (引数) locale:Locale("ja","JP","")<br>
     * (状態) internalResolveCodeWithoutArguments:null<br>
     * <br>
     * 期待値：(戻り値) msg:null<br>
     * (状態変化) internalResolveCodeWithoutArguments:指定された引数で呼び出されていることを確認する。<br>
     * <br>
     * 与えられた引数よりメッセージが決定できず、nullを返却する。 <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    public void testResolveCodeWithoutArguments02() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub04 ds
                = new DataSourceMessageSource_DataSourceMessageSourceStub04();
        String code = "code01";
        Locale locale = new Locale("ja", "JP", "");
        
        // テスト実施
        String returnMsg = ds.resolveCodeWithoutArguments(code, locale);

        // 判定
        assertEquals(code, ds.code);
        assertSame(locale, ds.locale);
        assertTrue(ds.isRead);
        assertNull(returnMsg);
    }

    /**
     * testInternalResolveCodeWithoutArguments01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F<br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     *         (引数) locale:Locale("ja","JP","")<br>
     *         (状態) getMessage().getProperties:"テストメッセージ０１"<br>
     *         (状態) メッセージのlocale:Locale("ja",JP","")<br>
     *         (状態) defaultLocale:null<br>
     *         
     * <br>
     * 期待値：(戻り値) msg:"テストメッセージ０１"<br>
     *         (状態変化) getMessage().getProperties:指定された引数で呼び出されていることを確認する。<br>
     *         
     * <br>
     * 指定された引数よりメッセージを決定し、返却する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testInternalResolveCodeWithoutArguments01() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        ds.cachedMergedProperties = null;
        
        String code = "code01";
        Locale locale = new Locale("ja", "JP", "");
        ds.defaultLocale = null;
        
        //cachedMergedProperties生成
        Map map = new HashMap();
        Properties props = new Properties();
        String msg = "テストメッセージ０１";
        props.put(code, msg);
        map.put(locale, props);
        ds.cachedMergedProperties = map;
        
        // テスト実施
        String returnMsg = ds.internalResolveCodeWithoutArguments(code, locale);
        
        // 判定
        assertEquals(msg, returnMsg);
    }

    /**
     * testInternalResolveCodeWithoutArguments02() <br>
     * <br>
     * (正常系)<br>
     * 観点：F<br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     * (引数) locale:Locale("ja","JP","")<br>
     * (状態) getMessage().getProperties:null<br>
     * (状態) メッセージのlocale:Locale("ja","","")<br>
     * (状態) defaultLocale:null<br>
     * (状態) getAlternativeLocales（）:List｛<br>
     * Locale（"ja")<br> ｝<br>
     * (状態) getMessage(locales.get()).getProperties():1回目の戻り値<br>
     * "テストメッセージ０１"<br>
     * <br>
     * 期待値：(戻り値) msg:"テストメッセージ０１"<br>
     * (状態変化) getMessage().getProperties:指定された引数で呼び出されていることを確認する。<br>
     * (状態変化) getAlternativeLocales:指定された引数で呼び出されていることを確認する。<br>
     * (状態変化)
     * getMessages(locales.get()).getProperties():指定された引数で呼び出されていることを確認する。<br>
     * <br>
     * 指定された引数でメッセージを決定できず、新たに作成した引数によりメッセージを決定し、
     * 返却する。また、ロケールがnullでもエラーにならないことを確認する。
     * <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testInternalResolveCodeWithoutArguments02() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        
        String code = "code01";
        Locale locale = new Locale("ja", "JP", "");
        ds.defaultLocale = null;
        
        //cachedMergedProperties生成
        Map map = new HashMap();
        Properties props = new Properties();
        Locale msglocale = new Locale("ja", "", "");
        String msg = "テストメッセージ０１";
        props.put(code, msg);
        map.put(msglocale, props);
        ds.cachedMergedProperties = map;
        
        // テスト実施
        String returnMsg = ds.internalResolveCodeWithoutArguments(code, locale);
        
        // 判定
        assertEquals("テストメッセージ０１", returnMsg);
    }

    /**
     * testInternalResolveCodeWithoutArguments03() <br>
     * <br>
     * (正常系) <br>
     * 観点：F<br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     * (引数) locale:Locale("ja","JP","")<br>
     * (状態) getMessage().getProperties:null<br>
     * (状態) メッセージのlocale:Locale("en","","")<br>
     * (状態) defaultLocale:Locale("en","US","")<br>
     * (状態) getAlternativeLocales（）:List｛<br>
     * Locale("ja","","")<br>
     * Locale("en","US","")<br>
     * Locale（"en","","")<br> ｝<br>
     * (状態) getMessage(locales.get()).getProperties():１回目の戻り値<br>
     * null<br>
     * ２回目の戻り値<br>
     * null<br>
     * ３回目の戻り値<br>
     * "テストメッセージ０１"<br>
     * <br>
     * 期待値：(戻り値) msg:"テストメッセージ０１"<br>
     * (状態変化) getMessage().getProperties:指定された引数で呼び出されていることを確認する。<br>
     * (状態変化) getAlternativeLocales:指定された引数で呼び出されていることを確認する。<br>
     * (状態変化)
     * getMessages(locales.get()).getProperties():指定された引数で
     * 呼び出されていることを確認する。<br>
     * <br>
     * 指定された引数でメッセージを決定できず、新たに作成した引数により
     * メッセージを決定し、返却する。また、ロケールがnullでもエラーにならないことを確認する。
     * <br>
     * 
     * @throws Exception
     *             このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testInternalResolveCodeWithoutArguments03() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();

        String code = "code01";
        Locale locale = new Locale("ja", "JP", "");
        ds.defaultLocale = new Locale("en", "US", "");
        
        //cachedMergedProperties生成
        Map map = new HashMap();
        Properties props = new Properties();
        Locale msglocale = new Locale("en", "", "");
        String msg = "テストメッセージ０１";
        props.put(code, msg);
        map.put(msglocale, props);
        ds.cachedMergedProperties = map;
        
        // テスト実施
        String returnMsg = ds.internalResolveCodeWithoutArguments(code, locale);
        
        // 判定
        assertEquals("テストメッセージ０１", returnMsg);
    }

    /**
     * testInternalResolveCodeWithoutArguments04() <br>
     * <br>
     * (正常系)<br>
     * 観点：F<br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     *         (引数) locale:Locale("ja","JP","")<br>
     *         (状態) getMessage().getProperties:null<br>
     *         (状態) メッセージのlocale:Locale("ja","JP","kansai")<br>
     *         (状態) defaultLocale:Locale("en","US","")<br>
     *         (状態) getAlternativeLocales（）:List｛<br>
     *                  Locale("ja","","")<br>
     *                  Locale("en","US","")<br>
     *                　Locale（"en","","")<br>
     *                ｝<br>
     *         (状態) getMessage(locales.get()).getProperties():１回目の戻り値<br>
     *                null<br>
     *                ２回目の戻り値<br>
     *                null<br>
     *                ３回目の戻り値<br>
     *                null<br>
     *         
     * <br>
     * 期待値：(戻り値) msg:null<br>
     *         (状態変化) getMessage().getProperties:指定された引数で呼び出されていることを確認する。<br>
     *         (状態変化) getAlternativeLocales:指定された引数で呼び出されていることを確認する。<br>
     *         (状態変化) getMessages(locales.get()).getProperties():
     *         指定された引数で呼び出されていることを確認する。<br>
     *         
     * <br>
     * 指定された引数及び新たに生成した引数より、メッセージが決定できない場合はnullを返却する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testInternalResolveCodeWithoutArguments04() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        
        String code = "code01";
        Locale locale = new Locale("ja", "JP", "");
        ds.defaultLocale = new Locale("en", "US", "");
        
        //cachedMergedProperties生成
        Map map = new HashMap();
        Properties props = new Properties();
        Locale msglocale = new Locale("ja", "JP", "kansai");
        String msg = "テストメッセージ０１やで";
        props.put(code, msg);
        map.put(msglocale, props);
        ds.cachedMergedProperties = map;
        
        // テスト実施
        String returnMsg = ds.internalResolveCodeWithoutArguments(code, locale);
        
        // 判定
        assertNull(returnMsg);
    }

    /**
     * testInternalResolveCodeWithoutArguments05() <br>
     * <br>
     * (正常系)<br>
     * 観点：F<br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     *         (引数) locale:Locale("ja","","")<br>
     *         (状態) getMessage().getProperties:null<br>
     *         (状態) メッセージのlocale:Locale("ja","JP","kansai")<br>
     *         (状態) defaultLocale:null<br>
     *         (状態) getAlternativeLocales（）:List｛<br>
     *                ｝<br>
     *         
     * <br>
     * 期待値：(戻り値) msg:null<br>
     *         (状態変化) getMessage().getProperties:指定された引数で呼び出されていることを確認する。<br>
     *         (状態変化) getAlternativeLocales:指定された引数で呼び出されていることを確認する。<br>
     *         
     * <br>
     * ロケールを取得できない場合、nullを返却する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testInternalResolveCodeWithoutArguments05() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        
        String code = "code01";
        Locale locale = new Locale("ja", "", "");
        ds.defaultLocale = null;
        
        //cachedMergedProperties生成
        Map map = new HashMap();
        Properties props = new Properties();
        Locale msglocale = new Locale("ja", "JP", "kansai");
        String msg = "テストメッセージ０１やで";
        props.put(code, msg);
        map.put(msglocale, props);
        ds.cachedMergedProperties = map;
        
        // テスト実施
        String returnMsg = ds.internalResolveCodeWithoutArguments(code, locale);
        
        // 判定
        assertNull(returnMsg);
    }

    /**
     * testGetAlternativeLocales01() <br>
     * <br>
     * (正常系)<br>
     * 観点：F<br>
     * <br>
     * 入力値：(引数) locale:Locale("ja","","")<br>
     *         (状態) defaultLocale:Locale("en","","")<br>
     *         
     * <br>
     * 期待値：(戻り値) locales:要素数1のList<br>
     *                  Locale{"en","",""}<br>
     *         
     * <br>
     * 引数localeに言語コードしか存在しない場合、新たなlocaleのパターンを
     * 生成しない。また、defaultLocaleに言語コードしか存在しない場合、1パターンのlocaleオブジェクトを生成し、リストに格納する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetAlternativeLocales01() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        Locale locale = new Locale("ja", "", "");
        Locale defaultLocale = new Locale("en", "", "");
        ds.defaultLocale = defaultLocale;
        
        // テスト実施
        List locales = ds.getAlternativeLocales(locale);
        
        // 判定
        assertEquals(1, locales.size());
        assertEquals(defaultLocale, locales.get(0));
    }

    /**
     * testGetAlternativeLocales02() <br>
     * <br>
     * (正常系)<br>
     * 観点：F<br>
     * <br>
     * 入力値：(引数) locale:Locale("ja","JP","")<br>
     *         (状態) defaultLocale:Locale("en","US","")<br>
     *         
     * <br>
     * 期待値：(戻り値) locales:要素数2のList<br>
     *                  Locale{"ja","",""},<br>
     *                  Locale{"en","US",""}<br>
     *                  Locale{"en","",""}<br>
     *         
     * <br>
     * 引数localeに国コードまで存在する場合、1パターンのLocaleオブジェクトを
     * 生成する。また、defaultLocaleに国コードまで存在する場合、2パターンのlocaleオブジェクトを生成し、リストに格納する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetAlternativeLocales02() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        Locale locale = new Locale("ja", "JP", "");
        Locale defaultLocale = new Locale("en", "US", "");
        ds.defaultLocale = defaultLocale;
        
        Locale locale1 = new Locale("ja", "", "");
        Locale defaultLocale2 = new Locale("en", "", "");
        
        // テスト実施
        List locales = ds.getAlternativeLocales(locale);
        
        // 判定
        assertEquals(3, locales.size());
        assertEquals(locale1, locales.get(0));
        assertEquals(defaultLocale, locales.get(1));
        assertEquals(defaultLocale2, locales.get(2));
    }

    /**
     * testGetAlternativeLocales03() <br>
     * <br>
     * (正常系)<br>
     * 観点：F<br>
     * <br>
     * 入力値：(引数) locale:Locale("ja","JP","kansai")<br>
     *         (状態) defaultLocale:Locale("en","US","NY")<br>
     *         
     * <br>
     * 期待値：(戻り値) locales:要素数5のList<br>
     *                  Locale{"ja","JP",""}<br>
     *                  Locale{"ja","",""}<br>
     *                  Locale{"en","US","NY"}<br>
     *                  Locale{"en","US",""}<br>
     *                  Locale{"en","",""}<br>
     *         
     * <br>
     * 引数localeにバリアントコードまで存在する場合、2パターンの
     * Localeオブジェクトを生成する。
     * また、defaultLocaleにバリアントコードまで存在する場合、
     * 3パターンのlocaleオブジェクトを生成し、リストに格納する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetAlternativeLocales03() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        Locale locale = new Locale("ja", "JP", "kansai");
        Locale defaultLocale = new Locale("en", "US", "NY");
        ds.defaultLocale = defaultLocale;
        
        Locale locale1 = new Locale("ja", "JP", "");
        Locale locale2 = new Locale("ja", "", "");
        Locale defaultLocale2 = new Locale("en", "US", "");
        Locale defaultLocale3 = new Locale("en", "", "");

        // テスト実施
        List locales = ds.getAlternativeLocales(locale);
        
        // 判定
        assertEquals(5, locales.size());
        assertEquals(locale1, locales.get(0));
        assertEquals(locale2, locales.get(1));
        assertEquals(defaultLocale, locales.get(2));
        assertEquals(defaultLocale2, locales.get(3));
        assertEquals(defaultLocale3, locales.get(4));     
    }

    /**
     * testGetAlternativeLocales04() <br>
     * <br>
     * (正常系)<br>
     * 観点：F <br>
     * <br>
     * 入力値：(引数) locale:Locale("ja","JP","kansai")<br>
     *         (状態) defaultLocale:Locale("ja","JP","kansai")<br>
     *         
     * <br>
     * 期待値：(戻り値) locales:要素数2のList<br>
     *                  Locale{"ja","JP",""}<br>
     *                  Locale{"ja","",""}<br>
     *         
     * <br>
     * 引数localeとdefaultLocaleが同値であった場合、引数localeから
     * 新たなlocaleオブジェクトを生成し、リストに格納する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetAlternativeLocales04() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        Locale locale = new Locale("ja", "JP", "kansai");
        ds.defaultLocale = locale;
        
        Locale locale1 = new Locale("ja", "JP", "");
        Locale locale2 = new Locale("ja", "", "");
 
        // テスト実施
        List locales = ds.getAlternativeLocales(locale);
        
        // 判定
        assertEquals(2, locales.size());
        assertEquals(locale1, locales.get(0));
        assertEquals(locale2, locales.get(1)); 
    }

    /**
     * testGetAlternativeLocales05() <br>
     * <br>
     * (正常系) <br>
     * 観点：F<br>
     * <br>
     * 入力値：(引数) locale:Locale("ja","","")<br>
     *         (状態) defaultLocale:null<br>
     *         
     * <br>
     * 期待値：(戻り値) locales:要素数0のList<br>
     *         
     * <br>
     * defaultLocaleの設定が行われていない場合は、Listが0で返る。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetAlternativeLocales05() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        Locale locale = new Locale("ja", "", "");
        ds.defaultLocale = null;
        
        // テスト実施
        List locales = ds.getAlternativeLocales(locale);
        
        // 判定
        assertEquals(0, locales.size());
    }
    /**
     * testResolveCode01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A<br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     *         (引数) locale:Locale("ja","JP","")<br>
     *         (状態) getMessageFormat(code,locale):"テストメッセージ０１"<br>
     *         (状態) defaultLocale:null<br>
     *         (状態) メッセージのロケール:Locale("ja","JP","")<br>
     *         
     * <br>
     * 期待値：(戻り値) messageFormat:"テストメッセージ０１"<br>
     *         (状態変化) getMessageFormat(code,locale):指定された引数により
     *         呼び出されたことを確認する。<br>
     *         
     * <br>
     * 指定された引数よりメッセージフォーマットを決定し、返却する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveCode01() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        String code = "code01";
        Locale locale = new Locale("ja", "JP", "");
        
        //cachedMergedProperties生成
        Map map = new HashMap();
        Locale formatlocale = new Locale("ja", "JP", "");
        MessageFormat mFormat = new MessageFormat("");
        mFormat.setLocale(formatlocale);
        mFormat.applyPattern("テストメッセージ０１");
        map.put(formatlocale, mFormat);
        ds.cachedMessageFormats.put(code, map);
        
        // テスト実施
        MessageFormat mfreturn = ds.resolveCode(code, locale);
        
        // 判定
        assertSame(mFormat, mfreturn);
    }

    /**
     * testResolveCode02() <br>
     * <br>
     * (正常系)<br>
     * 観点：A<br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     *         (引数) locale:Locale("ja","JP","")<br>
     *         (状態) getMessageFormat(code,locale):null<br>
     *         (状態) defaultLocale:null<br>
     *         (状態) メッセージのロケール:Locale("ja","","")<br>
     *         (状態) getAlternativeLocales（）:List｛<br>
     *                　Locale（"ja")<br>
     *                ｝<br>
     *         (状態) getMessageFormat(code.locales.get(i)):1回目の戻り値<br>
     *                "テストメッセージ０１"<br>
     *         
     * <br>
     * 期待値：(戻り値) messageFormat:"テストメッセージ０１"<br>
     *         (状態変化) getAlternativeLocale:指定された引数により呼び出された
     *         ことを確認する。<br>
     *         (状態変化) getMessageFormat(code,locale):指定された引数により
     *         呼び出されたことを確認する。<br>
     *         (状態変化) getMessageFormat(code.locales.get(i)):指定された引数に
     *         より呼び出されたことを確認する。<br>
     *         
     * <br>
     * 指定された引数でメッセージフォーマットを決定できず、新たに作成した引数に
     * よりメッセージフォーマットを決定し、返却する。
     * また、ロケールがnullでもエラーにならないことを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveCode02() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        String code = "code01";
        Locale locale = new Locale("ja", "JP", "");
        
        //cachedMergedProperties生成
        Map map = new HashMap();
        Locale formatlocale = new Locale("ja", "", "");
        MessageFormat mFormat = new MessageFormat("");
        mFormat.setLocale(formatlocale);
        mFormat.applyPattern("テストメッセージ０１");
        map.put(formatlocale, mFormat);
        ds.cachedMessageFormats.put(code, map);
        
        // テスト実施
        MessageFormat mfreturn = ds.resolveCode(code, locale);
        
        // 判定
        assertSame(mFormat, mfreturn);
    }

    /**
     * testResolveCode03() <br>
     * <br>
     * (正常系)<br>
     * 観点：A<br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     *         (引数) locale:Locale("ja","JP","kansai")<br>
     *         (状態) getMessageFormat(code,locale):null<br>
     *         (状態) defaultLocale:Locale("en","","")<br>
     *         (状態) メッセージのロケール:Locale("en","","")<br>
     *         (状態) getAlternativeLocales（）:List｛<br>
     *                  Locale("ja","JP","")<br>
     *                  Locale("ja","","")<br>
     *                　Locale（"en","","")<br>
     *                ｝<br>
     *         (状態) getMessageFormat(code.locales.get(i)):１回目の戻り値<br>
     *                null<br>
     *                ２回目の戻り値<br>
     *                null<br>
     *                ３回目の戻り値<br>
     *                "テストメッセージ０１"<br>
     *         
     * <br>
     * 期待値：(戻り値) messageFormat:"テストメッセージ０１"<br>
     *         (状態変化) getAlternativeLocale:指定された引数により呼び出された
     *         ことを確認する。<br>
     *         (状態変化) getMessageFormat(code,locale):指定された引数により
     *         呼び出されたことを確認する。<br>
     *         (状態変化) getMessageFormat(code.locales.get(i)):指定された引数
     *         により呼び出されたことを確認する。<br>
     *         
     * <br>
     * 指定された引数でメッセージフォーマットを決定できず、新たに作成した引数
     * によりメッセージフォーマットを決定し、返却する。
     * また、ロケールがnullでもエラーにならないことを確認する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveCode03() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        String code = "code01";
        Locale locale = new Locale("ja", "JP", "kansai");
        Locale defaultLocale = new Locale("en", "", "");
        ds.defaultLocale = defaultLocale;
        
        //cachedMergedProperties生成
        Map map = new HashMap();
        Locale formatlocale = new Locale("en", "", "");
        MessageFormat mFormat = new MessageFormat("");
        mFormat.setLocale(formatlocale);
        mFormat.applyPattern("テストメッセージ０１");
        map.put(formatlocale, mFormat);
        ds.cachedMessageFormats.put(code, map);
        
        // テスト実施
        MessageFormat mfreturn = ds.resolveCode(code, locale);
        
        // 判定
        assertSame(mFormat, mfreturn);
    }

    /**
     * testResolveCode04() <br>
     * <br>
     * (正常系)<br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     *         (引数) locale:Locale("ja","JP","kansai")<br>
     *         (状態) getMessageFormat(code,locale):null<br>
     *         (状態) defaultLocale:Locale("en","","")<br>
     *         (状態) メッセージのロケール:Locale("en","US","NY")<br>
     *         (状態) getAlternativeLocales（）:List｛<br>
     *                  Locale("ja","JP","")<br>
     *                  Locale("ja","","")<br>
     *                　Locale（"en","","")<br>
     *                ｝<br>
     *         (状態) getMessageFormat(code.locales.get(i)):１回目の戻り値<br>
     *                null<br>
     *                ２回目の戻り値<br>
     *                null<br>
     *                ３回目の戻り値<br>
     *                null<br>
     *         
     * <br>
     * 期待値：(戻り値) messageFormat:null<br>
     *         (状態変化) getAlternativeLocale:指定された引数により呼び出された
     *         ことを確認する。<br>
     *         (状態変化) getMessageFormat(code,locale):指定された引数により
     *         呼び出されたことを確認する。<br>
     *         (状態変化) getMessageFormat(code.locales.get(i)):指定された引数
     *         により呼び出されたことを確認する。<br>
     *         
     * <br>
     * 指定された引数及び新たに生成した引数より、メッセージフォーマットが
     * 決定できない場合はnullを返却する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveCode04() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        String code = "code01";
        Locale locale = new Locale("ja", "JP", "kansai");
        Locale defaultLocale = new Locale("en", "", "");
        ds.defaultLocale = defaultLocale;
        
        //cachedMergedProperties生成
        Map map = new HashMap();
        Locale formatlocale = new Locale("en", "US", "NY");
        MessageFormat mFormat = new MessageFormat("");
        mFormat.setLocale(formatlocale);
        mFormat.applyPattern("テストメッセージ０１");
        map.put(formatlocale, mFormat);
        ds.cachedMessageFormats.put(code, map);
        
        // テスト実施
        MessageFormat mfreturn = ds.resolveCode(code, locale);
        
        // 判定
        assertNull(mfreturn);
    }

    /**
     * testResolveCode05() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     *         (引数) locale:Locale("ja","","")<br>
     *         (状態) getMessageFormat(code,locale):null<br>
     *         (状態) defaultLocale:null<br>
     *         (状態) メッセージのロケール:Locale("en","US","NY")<br>
     *         (状態) getAlternativeLocales（）:List｛<br>
     *                ｝<br>
     *         
     * <br>
     * 期待値：(戻り値) messageFormat:null<br>
     *         (状態変化) getAlternativeLocale:指定された引数で呼び出されている
     *         ことを確認する。<br>
     *         (状態変化) getMessageFormat(code,locale):指定された引数で
     *         呼び出されていることを確認する。<br>
     *         
     * <br>
     * ロケールを取得できない場合、nullを返却する。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testResolveCode05() throws Exception {
        // 前処理
        DataSourceMessageSource ds = new DataSourceMessageSource();
        String code = "code01";
        Locale locale = new Locale("ja", "", "");
        ds.defaultLocale = null;
        
        //cachedMergedProperties生成
        Map map = new HashMap();
        Locale formatlocale = new Locale("en", "US", "NY");
        MessageFormat mFormat = new MessageFormat("");
        mFormat.setLocale(formatlocale);
        mFormat.applyPattern("テストメッセージ０１");
        map.put(formatlocale, mFormat);
        ds.cachedMessageFormats.put(code, map);
        
        // テスト実施
        MessageFormat mfreturn = ds.resolveCode(code, locale);
        
        // 判定
        assertNull(mfreturn);
    }

    /**
     * testGetMessageFormat01() <br>
     * <br>
     * (正常系)<br>
     * 観点：A<br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     *         (引数) locale:Locale("ja",JP","")<br>
     *         (状態) cashedMessageFormats:<"code01",<Locale("ja","JP"),
     *         MessageFormat("")>><br>
     *         (状態) メッセージフォーマットのlocale:Locale("ja",JP","")<br>
     *         
     * <br>
     * 期待値：(戻り値) result:メッセージフォーマットオブジェクト<br>
     *         
     * <br>
     * 引数に対応する値がすでにキャッシュされたMapにあり、かつ取り出した値が
     * nullでない場合。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testGetMessageFormat01() throws Exception {
        // 前処理
        DataSourceMessageSource ds 
                = new DataSourceMessageSource();
        String code = "code01";
        Locale locale = new Locale("ja", "JP", "");
        MessageFormat mf = new MessageFormat("");
        Map map = new HashMap();
        map.put(locale, mf);
        ds.cachedMessageFormats.put(code, map);
        
        // テスト実施
        MessageFormat result = ds.getMessageFormat(code, locale);
        
        // 判定
        assertSame(mf, result);
    }

    /**
     * testGetMessageFormat02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     *         (引数) locale:Locale("ja",JP","")<br>
     *         (状態) cashedMessageFormats:<"code01",<Locale("ja","JP"),null>><br>
     *         (状態) メッセージフォーマットのlocale:Locale("ja",JP","")<br>
     *         (状態) getMessage(locale):Properties<"code01","テストメッセージ０１"><br>
     *         
     * <br>
     * 期待値：(戻り値) result:メッセージフォーマットオブジェクト<br>
     *         
     * <br>
     * 引数に対応する値がすでにキャッシュされたMapにあるが取り出した値がnull、
     * かつメッセージリソースに引数に対応する値があり、
     * メッセージフォーマットの生成に成功した場合。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testGetMessageFormat02() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub18 ds 
                = new DataSourceMessageSource_DataSourceMessageSourceStub18();
        String code = "code01";
        Locale locale = new Locale("ja", "JP", "");
        MessageFormat mf = null;
        Map map = new HashMap();
        map.put(locale, mf);
        ds.cachedMessageFormats.put(code, map);
        
        // テスト実施
        MessageFormat result = ds.getMessageFormat(code, locale);
        
        // 判定
        assertSame(mf, result);
        assertTrue(ds.isRead_A1);
    }

    /**
     * testGetMessageFormat03() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     *         (引数) locale:Locale("ja",JP","")<br>
     *         (状態) cashedMessageFormats:<"code01",<Locale("ja","JP"),null><br>
     *         (状態) メッセージフォーマットのlocale:Locale("ja",JP","")<br>
     *         (状態) getMessage(locale):Properties<"abc",""><br>
     *         
     * <br>
     * 期待値：(戻り値) result:null<br>
     *         
     * <br>
     * 引数に対応する値がすでにキャッシュされたMapにあるが取り出した値がnull、
     * かつメッセージリソースに引数に対応する値がない場合。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    public void testGetMessageFormat03() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub19 ds 
                = new DataSourceMessageSource_DataSourceMessageSourceStub19();
        String code = "code01";
        Locale locale = new Locale("ja", "JP", "");
        MessageFormat mf = null;
        Map map = new HashMap();
        map.put(locale, mf);
        ds.cachedMessageFormats.put(code, map);
        
        // テスト実施
        MessageFormat result = ds.getMessageFormat(code, locale);
        
        // 判定
        assertNull(result);
        assertTrue(ds.isRead_A1);
    }


    /**
     * testGetMessageFormat04() <br>
     * <br>
     * (正常系)<br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) code:"code01"<br>
     *         (引数) locale:Locale("ja",JP","")<br>
     *         (状態) cashedMessageFormats:<"code01",null><br>
     *         (状態) メッセージフォーマットのlocale:Locale("ja",JP","")<br>
     *         (状態) getMessage(locale):Properties<"code01","テストメッセージ０１"><br>
     *         
     * <br>
     * 期待値：(戻り値) result:メッセージフォーマットオブジェクト<br>
     *         
     * <br>
     * 引数に対応する値がすでにキャッシュされたMapになく、かつメッセージリソース
     * に引数に対応する値があり、メッセージフォーマットの生成に成功した場合。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetMessageFormat04() throws Exception {
        // 前処理
        DataSourceMessageSource_DataSourceMessageSourceStub18 ds 
                = new DataSourceMessageSource_DataSourceMessageSourceStub18();
        String code = "code01";
        Locale locale = new Locale("ja", "JP", "");
        ds.cachedMessageFormats.put(code, null);
        MessageFormat mf = new MessageFormat("");
        ds.messageFormat = mf;
        
        ds.cachedMessageFormats.clear();
        
        // テスト実施
        MessageFormat result = ds.getMessageFormat(code, locale);
        
        // 判定
        assertSame(locale, ds.locale);
        assertSame("テストメッセージ０１", ds.msg);
        assertSame(mf, result);
        
        assertEquals(1, ds.cachedMessageFormats.size());
        Map hm = ds.cachedMessageFormats.get(code);
        assertSame(mf, hm.get(locale));
        assertEquals(1, ds.cachedMessageFormats.size());
        assertTrue(ds.isRead_A1);
        
    }
}
