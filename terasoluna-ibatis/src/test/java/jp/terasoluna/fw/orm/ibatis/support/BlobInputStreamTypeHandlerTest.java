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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

import org.springframework.jdbc.support.lob.LobHandler;

/**
 * {@link jp.terasoluna.fw.orm.ibatis.support.BlobInputStreamTypeHandler}
 * クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * iBATISから利用されるBLOBとストリームをマッピングする実装のiBATISのタイプハンドラ。
 * <p>
 * 
 * @see jp.terasoluna.fw.orm.ibatis.support.BlobInputStreamTypeHandler
 */
@SuppressWarnings("unused")
public class BlobInputStreamTypeHandlerTest extends TestCase {

    /**
     * このテストケースを実行する為の
     * GUI アプリケーションを起動する。
     * 
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(BlobInputStreamTypeHandlerTest.class);
    }

    /**
     * 初期化処理を行う。
     * 
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 終了処理を行う。
     * 
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * コンストラクタ。
     * 
     * @param name このテストケースの名前。
     */
    public BlobInputStreamTypeHandlerTest(String name) {
        super(name);
    }

    /**
     * testBlobInputStreamTypeHandler01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) lobHandler:not null<br>
     *         
     * <br>
     * 期待値：(状態変化) lobHandler:引き数lobHandlerと同一のlobHandler<br>
     *         
     * <br>
     * 引き数がnot nullの場合、属性に設定することのテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testBlobInputStreamTypeHandler01() throws Exception {
        // 前処理
        LobHandler lob = new LobHandlerImpl01();

        // テスト実施
        BlobInputStreamTypeHandler handler =
            new BlobInputStreamTypeHandler(lob);

        // 判定
        assertSame(lob, UTUtil.getPrivateField(handler, "lobHandler"));
    }

    /**
     * testBlobInputStreamTypeHandler02()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) lobHandler:null<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalStateException<br>
     *         
     * <br>
     * 引数がnullの場合、IllegalStateExceptionがスローをすることのテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testBlobInputStreamTypeHandler02() throws Exception {
        // 前処理
        LobHandler lob = null;

        // テスト実施
        try {
            BlobInputStreamTypeHandler handler =
                new BlobInputStreamTypeHandler(lob);
            fail();
        } catch (IllegalStateException e) {
            // 判定            
        }
    }

    /**
     * testSetParameterInternal01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) value:InputStreamインスタンス<br>
     *         (状態) lobCreator.setBlobAsBinaryStream():正常<br>
     *         
     * <br>
     * 期待値：(戻り値) void:正常<br>
     *         (状態変化) lobCreator.setBlobAsBinaryStream():呼び出されていることを確認<br>
     *         
     * <br>
     * lobCreator.setBlobAsBinaryStream()が実行されていることのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testSetParameterInternal01() throws Exception {
        // 前処理
        LobHandler lob = new LobHandlerImpl01();
        
        PreparedStatement ps = null;
        int index = 0;
        // value : InputStream
        ByteArrayInputStream value = new ByteArrayInputStream("".getBytes());
        String jdbcType = null;
        // LobCreator実装クラス : 呼び出し確認
        LobCreatorImpl01 lobCreator = new LobCreatorImpl01();
        
        BlobInputStreamTypeHandler handler =
            new BlobInputStreamTypeHandler(lob);

        // テスト実施
        handler.setParameterInternal(ps, index, value, jdbcType, lobCreator);

        // 判定
        boolean b = ((Boolean) lobCreator.isSetBlobAsBinaryStream).booleanValue();
        assertTrue(b);
        
        value.close();
    }

    /**
     * testSetParameterInternal02()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) value:InputStream以外のインスタンス<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:ClassCastExceptinon<br>
     *         
     * <br>
     * ClassCastExceptionがスローされることのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testSetParameterInternal02() throws Exception {
        // 前処理
        LobHandler lob = new LobHandlerImpl01();
        
        PreparedStatement ps = null;
        int index = 0;
        // value : InputStream以外
        String value = "";
        String jdbcType = null;
        // LobCreator実装クラス
        LobCreatorImpl01 lobCreator = new LobCreatorImpl01();
        
        BlobInputStreamTypeHandler handler =
            new BlobInputStreamTypeHandler(lob);

        // テスト実施
        try {
            handler.setParameterInternal(ps, index, value, jdbcType, lobCreator);
            fail();
        } catch (ClassCastException e) {
            // 判定
        }
    }

    /**
     * testSetParameterInternal03()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(状態) lobCreator.setBlobAsBinaryStream():SQLException<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:SQLException<br>
     *         
     * <br>
     * SQLExceptionがスローされることのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testSetParameterInternal03() throws Exception {
        // 前処理
        LobHandler lob = new LobHandlerImpl01();
        
        PreparedStatement ps = null;
        int index = 0;
        // value : -
        Object value = null;
        String jdbcType = null;
        // LobCreator実装クラス : SQLException
        LobCreatorImpl02 lobCreator = new LobCreatorImpl02();
        
        BlobInputStreamTypeHandler handler =
            new BlobInputStreamTypeHandler(lob);

        // テスト実施
        try {
            handler.setParameterInternal(ps, index, value, jdbcType, lobCreator);
            fail();
        } catch (SQLException e) {
            // 判定
        }
    }

    /**
     * testGetResultInternal01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(状態) lobHandler.getBlobAsBinaryStream():not null<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:lobHandler.getBlobAsBinaryStream()の戻り値と同一インスタンス<br>
     *         (状態変化) lobHandler.getBlobAsBinaryStream():呼び出されていることを確認<br>
     *         
     * <br>
     * lobHandler.getBlobAsBinaryStream()の戻り値と同一インスタンスを返却することのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetResultInternal01() throws Exception {
        // 前処理
        LobHandlerImpl01 lob = new LobHandlerImpl01();
        
        ResultSet ps = null;
        int index = 0;
        
        // LobHandler実装クラスのgetBlobAsBinaryStreamのリターン値 : 
        // ByteArrayInputStreamインスタンス
        ByteArrayInputStream bais = new ByteArrayInputStream("".getBytes());
        UTUtil.setPrivateField(lob, "is", bais);
        
        BlobInputStreamTypeHandler handler =
            new BlobInputStreamTypeHandler(lob);

        // テスト実施
        ByteArrayInputStream input =
            (ByteArrayInputStream) handler.getResultInternal(ps, index, lob);

        // 判定
        boolean b = ((Boolean) lob.isGetBlobAsBinaryStream).booleanValue();
        assertTrue(b);
        assertSame(bais, input);
        
        bais.close();
    }

    /**
     * testGetResultInternal02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(状態) lobHandler.getBlobAsBinaryStream():null<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:null<br>
     *         (状態変化) lobHandler.getBlobAsBinaryStream():呼び出されていることを確認<br>
     *         
     * <br>
     * nullを返却することのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetResultInternal02() throws Exception {
        // 前処理
        LobHandlerImpl01 lob = new LobHandlerImpl01();
        
        ResultSet ps = null;
        int index = 0;
        
        // LobHandler実装クラスのgetBlobAsBinaryStreamのリターン値 : null
        ByteArrayInputStream bais = null;
        UTUtil.setPrivateField(lob, "is", bais);
        
        BlobInputStreamTypeHandler handler =
            new BlobInputStreamTypeHandler(lob);

        // テスト実施
        ByteArrayInputStream input =
            (ByteArrayInputStream) handler.getResultInternal(ps, index, lob);

        // 判定
        boolean b = ((Boolean) lob.isGetBlobAsBinaryStream).booleanValue();
        assertTrue(b);
        assertNull(input);
    }

    /**
     * testGetResultInternal03()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(状態) lobHandler.getBlobAsBinaryStream():SQLException<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:SQLException<br>
     *         
     * <br>
     * SQLExceptionがスローされることのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGetResultInternal03() throws Exception {
        // 前処理
        // getBlobAsBinaryStream : SQLException
        LobHandlerImpl02 lob = new LobHandlerImpl02();
        
        ResultSet ps = null;
        int index = 0;
        
        BlobInputStreamTypeHandler handler =
            new BlobInputStreamTypeHandler(lob);

        // テスト実施
        try {
            ByteArrayInputStream input =
                (ByteArrayInputStream) handler.getResultInternal(ps, index, lob);
            fail();
        } catch (SQLException e) {
            // 判定
        }
    }

    /**
     * testValueOf01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) s:null<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:null<br>
     *         
     * <br>
     * nullを返却することのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValueOf01() throws Exception {
        // 前処理
        LobHandlerImpl01 lob = new LobHandlerImpl01();
        
        BlobInputStreamTypeHandler handler =
            new BlobInputStreamTypeHandler(lob);
        
        String s = null;

        // テスト実施
        Object obj = handler.valueOf(s);

        // 判定
        assertNull(obj);
    }

    /**
     * testValueOf02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) s:""<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:new ByteArrayInputStream("".getBytes())<br>
     *         
     * <br>
     * ""をストリーム化したものを返却することのテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValueOf02() throws Exception {
        // 前処理
        LobHandlerImpl01 lob = new LobHandlerImpl01();
        
        BlobInputStreamTypeHandler handler =
            new BlobInputStreamTypeHandler(lob);
        
        String s = "";

        // テスト実施
        Object obj = handler.valueOf(s);

        // 判定
        assertEquals(ByteArrayInputStream.class.getName(),
                obj.getClass().getName());
        
        byte[] b1 = (byte[]) UTUtil.getPrivateField(obj, "buf");
        byte[] b2 = "".getBytes();
        for(int i=0; i<b1.length; i++) {
            assertEquals(b2[i], b1[i]);
        }
    }
    
    /**
     * testValueOf03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) s:"ABC"<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:new ByteArrayInputStream("ABC".getBytes())<br>
     *         
     * <br>
     * "ABC"をストリーム化したものを返却するbsことのテスト
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testValueOf03() throws Exception {
        // 前処理
        LobHandler lob = new LobHandlerImpl01();
        
        BlobInputStreamTypeHandler handler =
            new BlobInputStreamTypeHandler(lob);
        
        String s = "ABC";

        // テスト実施
        Object obj = handler.valueOf(s);

        // 判定
        assertEquals(ByteArrayInputStream.class.getName(),
                obj.getClass().getName());
        
        byte[] b1 = (byte[]) UTUtil.getPrivateField(obj, "buf");
        byte[] b2 = "ABC".getBytes();
        for(int i=0; i<b1.length; i++) {
            assertEquals(b2[i], b1[i]);
        }
    }
}