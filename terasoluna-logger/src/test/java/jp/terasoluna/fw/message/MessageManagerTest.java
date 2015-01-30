package jp.terasoluna.fw.message;

import static org.junit.Assert.*;
import static jp.terasoluna.fw.ex.unit.util.AssertUtils.*;

import java.net.URL;
import java.util.Arrays;

import jp.terasoluna.fw.message.execption.MessageRuntimeException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MessageManagerTest {
    private ClassLoader currentClassLoader;

    /**
     * 前処理
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        currentClassLoader = Thread.currentThread().getContextClassLoader();
    }

    /**
     * 後処理
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        Thread.currentThread().setContextClassLoader(currentClassLoader);
    }

    /**
     * コンストラクタの試験【01:正常系】
     * 
     * <pre>
     * 事前条件：
     * ・読み込み対象のプロパティファイルに何も設定されていない
     * 
     * 確認項目：
     * ・メッセージIDのフォーマットが[%s]
     * ・メッセージプロパティファイルのベースネームリストが空
     * ・例外スローフラグがfalse
     * </pre>
     */
    @Test
    public void testConstructor01() {
        // 設定のないプロパティファイル
        MessageManager manager = new MessageManager(
                "jp/terasoluna/fw/message/terasoluna-logger01.properties");
        assertEquals("[%s] ", manager.messageIdFormat);
        assertCollectionEmpty(manager.basenames);
        assertFalse(manager.throwIfResourceNotFound);
    }

    /**
     * コンストラクタの試験【02:正常系】
     * 
     * <pre>
     * 事前条件：
     * ・読み込み対象のプロパティファイルに各要素の値が設定されている
     * 
     * 確認項目：
     * ・メッセージIDのフォーマットが[%s]
     * ・メッセージプロパティファイルのベースネームリストに対象ファイル名が一件取得されている
     * ・例外スローフラグがtrue
     * </pre>
     */
    @Test
    public void testConstructor02() {
        // message.id.formatとmessage.basenameが設定されている、throw.if.resource.not.foundがtrue
        MessageManager manager = new MessageManager(
                "jp/terasoluna/fw/message/terasoluna-logger02.properties");
        assertEquals("|%s| ", manager.messageIdFormat);
        assertCollectionEquals(
                Arrays.asList("jp/terasoluna/fw/message/log-messages2"),
                manager.basenames);
        assertTrue(manager.throwIfResourceNotFound);
    }

    /**
     * コンストラクタの試験【03:正常系】
     * 
     * <pre>
     * 事前条件：
     * ・読み込み対象のプロパティファイルにmessage.basenameが複数設定されている
     * 
     * 確認項目：
     * ・メッセージIDのフォーマットが[%s]
     * ・メッセージプロパティファイルのベースネームリストに対象ファイル名が複数件取得されている
     * ・例外スローフラグがfalse
     * </pre>
     */
    @Test
    public void testConstructor03() {
        // message.basenameが複数設定されている、throw.if.resource.not.foundがfalse
        MessageManager manager = new MessageManager(
                "jp/terasoluna/fw/message/terasoluna-logger03.properties");
        assertEquals("[%s] ", manager.messageIdFormat);
        assertCollectionEquals(Arrays.asList(
                "jp/terasoluna/fw/message/log-messages3-1",
                "jp/terasoluna/fw/message/log-messages3-2"), manager.basenames);
        assertFalse(manager.throwIfResourceNotFound);
    }

    /**
     * コンストラクタの試験【04:正常系】
     * 
     * <pre>
     * 事前条件：
     * ・読み込み対象のプロパティファイルがクラスローダ上に複数存在する
     * 
     * 確認項目：
     * ・メッセージIDのフォーマットが[%s]
     * ・メッセージプロパティファイルのベースネームリストに対象ファイル名が複数件取得されている
     * ・例外スローフラグがfalse
     * </pre>
     */
    @Test
    public void testConstructor04() {
        // 該当するプロパティファイルがクラスローダ上に複数存在する
        MockClassLoader cl = new MockClassLoader();
        cl.addMapping("META-INF/terasoluna-logger.properties",
                getResource("terasoluna-logger01.properties"),
                getResource("terasoluna-logger02.properties"),
                getResource("terasoluna-logger03.properties"));
        // クラスローダの差し替え
        Thread.currentThread().setContextClassLoader(cl);

        MessageManager manager = new MessageManager(
                "META-INF/terasoluna-logger.properties");
        assertEquals("[%s] ", manager.messageIdFormat);
        assertCollectionEquals(Arrays.asList(
                "jp/terasoluna/fw/message/log-messages2",
                "jp/terasoluna/fw/message/log-messages3-1",
                "jp/terasoluna/fw/message/log-messages3-2"), manager.basenames);
        assertFalse(manager.throwIfResourceNotFound);
    }

    /**
     * コンストラクタの試験【05:正常系】
     * 
     * <pre>
     * 事前条件：
     * ・読み込み対象のプロパティファイルがクラスローダ上に複数存在する
     * 
     * 確認項目：
     * ・メッセージIDのフォーマットが[%s]
     * ・メッセージプロパティファイルのベースネームリストに対象ファイル名が複数件取得されている
     * ・例外スローフラグがtrue（優先度の高いプロパティファイルの値になっているか確認する）
     * </pre>
     */
    @Test
    public void testConstructor05() {
        MockClassLoader cl = new MockClassLoader();
        cl.addMapping("META-INF/terasoluna-logger.properties",
                getResource("terasoluna-logger02.properties"),
                getResource("terasoluna-logger01.properties"),
                getResource("terasoluna-logger03.properties"));
        // クラスローダの差し替え
        Thread.currentThread().setContextClassLoader(cl);

        MessageManager manager = new MessageManager(
                "META-INF/terasoluna-logger.properties");
        assertEquals("|%s| ", manager.messageIdFormat);
        assertCollectionEquals(Arrays.asList(
                "jp/terasoluna/fw/message/log-messages2",
                "jp/terasoluna/fw/message/log-messages3-1",
                "jp/terasoluna/fw/message/log-messages3-2"), manager.basenames);
        assertTrue(manager.throwIfResourceNotFound);
    }

    /**
     * コンストラクタの試験【06:異常系】
     * 
     * <pre>
     * 事前条件：
     * ・不正なクラスローダを設定する
     * 
     * 確認項目：
     * ・コンストラクタの呼び出しによる、MessageRuntimeExceptionの発生を確認
     */
    @Test
    public void testConstructor06() {
        Thread.currentThread().setContextClassLoader(new BadClassLoader());
        try {
            new MessageManager("");
            fail("例外が発生しませんでした");
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("java.lang.RuntimeException: hoge", e.getMessage());
            assertEquals(MessageRuntimeException.class, e.getClass());
        }
    }

    @Test
    public void testConstructor07() throws Exception {
        MessageManager manager = new MessageManager(
                "jp/terasoluna/fw/message/terasoluna-logger05.properties");
        assertEquals(SampleMessageFormatter01.class,
                manager.messageFormatter.getClass());
    }

    @Test
    public void testConstructor08() throws Exception {
        try {
            new MessageManager(
                    "jp/terasoluna/fw/message/terasoluna-logger06.properties");
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals(MessageRuntimeException.class, e.getClass());
            assertEquals(ClassNotFoundException.class, e.getCause().getClass());
        }
    }

    protected static URL getResource(String name) {
        return MessageManager.class.getClassLoader().getResource(
                "jp/terasoluna/fw/message/" + name);
    }

    /**
     * getMessageメソッド試験【01:正常系】 getMessagePatternメソッドの動作を確認する
     * 
     * <pre>
     * 事前条件：
     * ・読み込むプロパティファイルが存在する
     * 
     * 確認項目：
     * ・指定したメッセージIDが存在しない場合、null値が返る
     * ・指定したメッセージIDがnull値の場合、null値が返る
     * </pre>
     */
    @Test
    public void testGetMessage01() {
        MessageManager manager = new MessageManager(
                "META-INF/terasoluna-logger.properties");
        assertNull(manager.getMessagePattern("hoge", null));
        assertNull(manager.getMessagePattern(null, null));
    }

    /**
     * getMessageメソッド試験【02:異常系】 getMessagePatternメソッドの例外処理を確認する
     * 
     * <pre>
     * 事前条件：
     * ・読み込むプロパティファイルが存在する
     * ・例外スローフラグがtrue
     * 
     * 確認項目：
     * ・指定したメッセージIDが存在しない場合、MessageRuntimeExceptionが返る
     * </pre>
     */
    @Test
    public void testGetMessage02() {
        MessageManager manager = new MessageManager(
                "jp/terasoluna/fw/message/terasoluna-logger02.properties");
        try {
            manager.getMessagePattern("hoge", null);
            fail("例外が発生しませんでした");
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("key[hoge] is not found", e.getMessage());
            assertEquals(MessageRuntimeException.class, e.getClass());
        }
    }

    /**
     * getMessageメソッド試験【03:正常系】 プロパティファイルに存在するメッセージを取得
     * 
     * <pre>
     * 事前条件：
     * ・読み込むプロパティファイルに該当するメッセージが設定されている
     * 
     * 確認項目：
     * ・プロパティファイルから期待するメッセージが取得されている
     * </pre>
     */
    @Test
    public void testGetMessage03() {
        MessageManager manager = new MessageManager(
                "jp/terasoluna/fw/message/terasoluna-logger04.properties");
        assertEquals("[message01] メッセージ01",
                manager.getMessage(true, "message01"));

    }

    /**
     * getMessageメソッド試験【04:正常系】 プロパティファイルに存在するメッセージを取得
     * 
     * <pre>
     * 事前条件：
     * ・リソース無しの条件でgetMessageメソッドを呼び出している
     * 
     * 確認項目：
     * ・設定した置換パラメータが正常に出力されている
     * </pre>
     */
    @Test
    public void testGetMessage04() {
        MessageManager manager = new MessageManager("");
        assertEquals("メッセージ01 置換文字列01=hoge,置換文字列02=foo", manager.getMessage(
                false, "メッセージ01 置換文字列01={0},置換文字列02={1}", null, "hoge", "foo"));
    }

    /**
     * getMessageメソッド試験【05:異常系】
     * 
     * <pre>
     * 事前条件：
     * ・読み込み対象のプロパティファイルに不正な置換パラメータが設定されたメッセージが存在する
     * ・例外スローフラグがtrue
     * 
     * 確認項目：
     * ・不正置換パラメータを設定したメッセージの呼び出しにより、MessageRuntimeExceptionが返る
     * </pre>
     */
    @Test
    public void testGetMessage05() {
        MessageManager manager = new MessageManager(
                "jp/terasoluna/fw/message/terasoluna-logger04.properties");

        try {
            manager.getMessage(true, "message02");
            fail("例外が発生しませんでした");
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals(
                    "message pattern is illeagal. pattern=メッセージ02{a}] logId=message02",
                    e.getMessage());
            assertEquals(MessageRuntimeException.class, e.getClass());
        }

    }

    /**
     * getStringOrNullメソッド試験【01:正常系】
     * 
     * <pre>
     * 事前条件：
     * ・getStringOrNullメソッドの引数であるResourceBundleの値がnullである
     * 
     * 確認項目：
     * ・null値が返る
     * </pre>
     */
    @Test
    public void testGetStringOrNull01() {
        MessageManager manager = new MessageManager(
                "jp/terasoluna/fw/message/terasoluna-logger02.properties");
        assertNull(manager.getStringOrNull(null, null));
    }

    /**
     * getStringOrNullメソッド試験【02:異常系】
     * 
     * <pre>
     * 事前条件：
     * ・getStringOrNullメソッドの引数であるString型変数keyの値がnullである
     * ・例外スローフラグがtrue
     * 
     * 確認項目：
     * ・MessageRuntimeExceptionが返ること
     * </pre>
     */
    @Test
    public void testGetStringOrNull02() {
        MessageManager manager = new MessageManager(
                "jp/terasoluna/fw/message/terasoluna-logger02.properties");
        try {
            manager.getMessagePattern(null, null);
            fail("例外が発生しませんでした");
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("key is null", e.getMessage());
            assertEquals(MessageRuntimeException.class, e.getClass());
        }
    }

    /**
     * getResourceBundleメソッド試験【01:異常系】 getStringOrNullの例外処理を確認する
     * 
     * <pre>
     * 事前条件：
     * ・読み込み対象のプロパティファイルが存在する
     * ・存在しないプロパティファイルを指定してgetResourceBundleメソッドを呼び出す
     * ・例外スローフラグがtrue
     * 
     * 確認項目：
     * ・getResourceBundleメソッドからMessageRuntimeExceptionが返る
     * </pre>
     */
    @Test
    public void testGetResourceBundle() {
        MessageManager manager = new MessageManager(
                "jp/terasoluna/fw/message/terasoluna-logger02.properties");
        try {
            manager.getResourceBundle("META-INF/hoge.properties", null);
            fail("例外が発生しませんでした");
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("resource[META-INF/hoge.properties] is not found",
                    e.getMessage());
            assertEquals(MessageRuntimeException.class, e.getClass());
        }
    }

    /**
     * getClassLoaderメソッド試験【01:正常系】
     * 
     * <pre>
     * 確認項目：
     * ・スレッドに設定されたコンテキストクラスローダがgetClassLoaderメソッドにより取得できることを確認
     * </pre>
     */
    @Test
    public void testGetClassLoader01() {
        assertSame(Thread.currentThread().getContextClassLoader(),
                MessageManager.getClassLoader());
    }

    /**
     * getClassLoaderメソッド試験【02:正常系】
     * 
     * <pre>
     * 確認項目：
     * ・スレッドに設定されたコンテキストクラスローダに対し、null値を設定する
     * ・MessageManager.getClassLoaderの結果がMessageManager.classをロードしたクラスローダになってることを確認
     * </pre>
     */
    @Test
    public void testGetClassLoader02() {
        Thread.currentThread().setContextClassLoader(null);
        assertSame(MessageManager.class.getClassLoader(),
                MessageManager.getClassLoader());
    }
}
