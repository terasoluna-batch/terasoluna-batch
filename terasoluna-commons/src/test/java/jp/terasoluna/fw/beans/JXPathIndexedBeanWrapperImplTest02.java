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

package jp.terasoluna.fw.beans;

import jp.terasoluna.utlib.LogUTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.beans.JXPathIndexedBeanWrapperImpl} クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * JavaBeanの配列・コレクション型属性にアクセスできるクラス。<br>
 * 前提条件：<br>
 * 「toXPath」メソッドの引数にはnullは入らない。<br>
 * 「extractIncrementIndex(String)」メソッドの引数にはnullは入らない。<br>
 * 「extractIncrementIndex(String,int)」メソッドの第１引数にはnullは入らない。<br>
 * 「extractIncrementIndex(String,int)」メソッドの第２引数には1か-1しか入らない。<br>
 * 「extractIndex」メソッドの引数にはnullは入らない。<br>
 * 「escapeMapProperty」メソッドの引数にはnullは入らない。<br>
 * 「extractMapPropertyName」メソッドの引数にはnullは入らない。<br>
 * 「extractMapPropertyKey」メソッドの引数にはnullは入らない。<br>
 * 「isMapProperty」メソッドの引数にはnullは入らない。
 * <p>
 * 
 * @see jp.terasoluna.fw.beans.JXPathIndexedBeanWrapperImpl
 */
public class JXPathIndexedBeanWrapperImplTest02 extends TestCase {


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
    public JXPathIndexedBeanWrapperImplTest02(String name) {
        super(name);
    }

    /**
     * testToXPath01()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) property:""（空文字）<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    　メッセージ：Property name is null or blank.<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    Property name is null or blank.<br>
     *         
     * <br>
     * 【空文字のテスト】<br>
     * 引数のpropertyが空文字のテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testToXPath01() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub03 test = 
            new JXPathIndexedBeanWrapperImplStub03(object);
        
        // 入力値設定
        String property = "";
        
        // テスト実施
        try{
            test.toXPath(property);
            fail();
        }catch(IllegalArgumentException e){
            // 判定
            assertEquals("Property name is null or blank.", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                            "Property name is null or blank."));
            
        }
    }

    /**
     * testToXPath02()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"."<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    　メッセージ：Property name is null or blank.<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    Property name is null or blank.<br>
     *         
     * <br>
     * 【文字のテスト】<br>
     * 引数のpropertyが"."のみのパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testToXPath02() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub03 test = 
            new JXPathIndexedBeanWrapperImplStub03(object);
        
        // 入力値設定
        String property = ".";
        
        // テスト実施
        try{
            test.toXPath(property);
            fail();
        }catch(IllegalArgumentException e){
            // 判定
            assertEquals("Property name is null or blank.", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                            "Property name is null or blank."));
            
        }
    }

    /**
     * testToXPath03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc"<br>
     *         (状態) isMapProperty:falseを返す。<br>
     *         (状態) extractAttributeName:property+"Attribute"を返す。<br>
     *         (状態) extractIncrementIndex:"[3]"を返す。<br>
     *         
     * <br>
     * 期待値：(戻り値) String:/abcAttribute[3]<br>
     *         (状態変化) isMapProperty:引数"abc"が渡されたことを確認する。<br>
     *         (状態変化) extractAttributeName:当メソッドは引数(property="abc")で戻り値property+"Attribute"とする。<br>
     *         (状態変化) extractIncrementIndex:引数"abc"が渡されたことを確認する。<br>
     *         
     * <br>
     * 【JavaBean or Primitiveのテスト】<br>
     * for文1回実行の場合。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testToXPath03() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub03 test = 
            new JXPathIndexedBeanWrapperImplStub03(object);
        
        // 入力値設定
        String property = "abc";
        
        // 前提（スタブ）設定
        test.isMapPropertyResult = false;
        test.extractIncrementIndexResult = "[3]";
        
        // テスト実施
        String result = test.toXPath(property);
        // 判定
        assertEquals("abc",test.isMapPropertyParam1);
        assertEquals("abc",test.extractIncrementIndexParam1);
        assertEquals("/abcAttribute[3]",result);
            
    }

    /**
     * testToXPath04()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc.def.ghi"<br>
     *         (状態) isMapProperty:falseを返す。<br>
     *         (状態) extractAttributeName:property+"Attribute"を返す。<br>
     *         (状態) extractIncrementIndex:""を返す。<br>
     *         
     * <br>
     * 期待値：(戻り値) String:/abcAttribute/defAttribute/ghiAttribute<br>
     *         (状態変化) isMapProperty:引数"ghi"が渡されたことを確認する。<br>
     *         (状態変化) extractAttributeName:当メソッドは引数(property="abc")で戻り値property+"Attribute"とする。<br>
     *         (状態変化) extractIncrementIndex:引数"ghi"が渡されたことを確認する。<br>
     *         
     * <br>
     * 【JavaBean or Primitiveのテスト】<br>
     * for文複数回実行の場合。<br>
     * （引数のpropertyに"."を含む文字列のパターン）
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testToXPath04() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub03 test = 
            new JXPathIndexedBeanWrapperImplStub03(object);
        
        // 入力値設定
        String property = "abc.def.ghi";
        
        // 前提（スタブ）設定
        test.isMapPropertyResult = false;
        test.extractIncrementIndexResult = "";
        
        // テスト実施
        String result = test.toXPath(property);
        
        // 判定
        assertEquals("ghi",test.isMapPropertyParam1);
        assertEquals("ghi",test.extractIncrementIndexParam1);
        assertEquals("/abcAttribute/defAttribute/ghiAttribute",result);
    }

    /**
     * testToXPath05()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc"<br>
     *         (状態) isMapProperty:trueを返す。<br>
     *         (状態) escapeMapProperty:property+"[@name='key']"を返す。<br>
     *         (状態) extractIncrementIndex:"[3]"を返す。<br>
     *         
     * <br>
     * 期待値：(戻り値) String:/abc[@name='key'][3]<br>
     *         (状態変化) isMapProperty:引数"abc"が渡されたことを確認する。<br>
     *         (状態変化) escapeMapProperty:当メソッドは引数(property="abc")で戻り値property+"[@name='key']"とする。<br>
     *         (状態変化) extractIncrementIndex:引数"abc"が渡されたことを確認する。<br>
     *         
     * <br>
     * 【Mapのテスト】<br>
     * for文1回実行の場合。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testToXPath05() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub03 test = 
            new JXPathIndexedBeanWrapperImplStub03(object);
        
        // 入力値設定
        String property = "abc";
        
        // 前提（スタブ）設定
        test.isMapPropertyResult = true;
        test.extractIncrementIndexResult = "[3]";
        
        // テスト実施
        String result = test.toXPath(property);
        
        // 判定
        assertEquals("abc",test.isMapPropertyParam1);
        assertEquals("abc",test.extractIncrementIndexParam1);
        assertEquals("/abc[@name='key'][3]",result);
    }

    /**
     * testToXPath06()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc.def..ghi"<br>
     *         (状態) isMapProperty:trueを返す。<br>
     *         (状態) escapeMapProperty:property+"[@name='key']"を返す。<br>
     *         (状態) extractIncrementIndex:""を返す。<br>
     *         
     * <br>
     * 期待値：(戻り値) String:/abc[@name='key']/def[@name='key']/ghi[@name='key']<br>
     *         (状態変化) isMapProperty:引数"ghi"が渡されたことを確認する。<br>
     *         (状態変化) escapeMapProperty:当メソッドは引数(property="abc")で戻り値property+"[@name='key']"とする。<br>
     *         (状態変化) extractIncrementIndex:引数"ghi"が渡されたことを確認する。<br>
     *         
     * <br>
     * 【Mapのテスト】<br>
     * for文複数回実行の場合。<br>
     * （引数のpropertyに".."を含む文字列のパターン）
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testToXPath06() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub03 test = 
            new JXPathIndexedBeanWrapperImplStub03(object);
        
        // 入力値設定
        String property = "abc.def..ghi";
        
        // 前提（スタブ）設定
        test.isMapPropertyResult = true;
        test.extractIncrementIndexResult = "";
        
        // テスト実施
        String result = test.toXPath(property);
        
        // 判定
        assertEquals("ghi",test.isMapPropertyParam1);
        assertEquals("ghi",test.extractIncrementIndexParam1);
        assertEquals("/abc[@name='key']/def[@name='key']/ghi[@name='key']",
                result);
    }

    /**
     * testToXPath07()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc.def..ghi"<br>
     *         (状態) isMapProperty:falseを返す。<br>
     *         (状態) extractAttributeName:property+"Attribute"を返す。<br>
     *         (状態) extractIncrementIndex:""を返す。<br>
     *         
     * <br>
     * 期待値：(戻り値) String:/abcAttribute/defAttribute/ghiAttribute<br>
     *         (状態変化) isMapProperty:引数"ghi"が渡されたことを確認する。<br>
     *         (状態変化) extractAttributeName:当メソッドは引数(property="abc")で戻り値property+"Attribute"とする。<br>
     *         (状態変化) extractIncrementIndex:引数"ghi"が渡されたことを確認する。<br>
     *         
     * <br>
     * 【不正文字のテスト】<br>
     * 引数のpropertyに".."（"."2回連続）を含む文字列のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testToXPath07() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub03 test = 
            new JXPathIndexedBeanWrapperImplStub03(object);
        
        // 入力値設定
        String property = "abc.def..ghi";
        
        // 前提（スタブ）設定
        test.isMapPropertyResult = false;
        test.extractIncrementIndexResult = "";
        
        // テスト実施
        String result = test.toXPath(property);
        
        // 判定
        assertEquals("ghi",test.isMapPropertyParam1);
        assertEquals("ghi",test.extractIncrementIndexParam1);
        assertEquals("/abcAttribute/defAttribute/ghiAttribute",result);
    }

    /**
     * testExtractIncrementIndexString01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc"<br>
     *         (状態) extractIncremantIndex(String,int):「property+":"+increment」を返す。<br>
     *         
     * <br>
     * 期待値：(戻り値) Object:"abc:1"<br>
     *         (状態変化) extractIncremantIndex(String,int):当メソッドは引数(property="abc",increment=1)で戻り値property+":"+incrementとする。<br>
     *         
     * <br>
     * 【extractIncremantIndex(String,int)　　呼び出しのテスト】
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractIncrementIndexString01() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub02 test = 
            new JXPathIndexedBeanWrapperImplStub02(object);
        
        // 入力値設定
        String property = "abc";
        
        // テスト実施
        String result = test.extractIncrementIndex(property);
        
        // 判定
        assertEquals("abc:1",result);
    }

    /**
     * testExtractIncremantIndexStringint01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc"<br>
     *         (引数) increment:1<br>
     *         (状態) extractIndex:""を返す。<br>
     *         
     * <br>
     * 期待値：(戻り値) String:""<br>
     *         (状態変化) extractIndex:引数"abc"が渡されたことを確認する。<br>
     *         
     * <br>
     * 【文字列のテスト】<br>
     * incrementが1で<br>
     * extractIndexが""を返すパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractIncremantIndexStringint01() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub01 test = 
            new JXPathIndexedBeanWrapperImplStub01(object);
        
        // 入力値
        String property = "abc";
        int increment = 1;
        
        // 前提（スタブ）
        test.extractIndexResult = "";
        
        // テスト実施
        String result = test.extractIncrementIndex(property,increment);
        
        // 判定
        assertEquals("abc",test.extractIndexParam1);
        assertEquals("",result);
    }

    /**
     * testExtractIncremantIndexStringint02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc"<br>
     *         (引数) increment:1<br>
     *         (状態) extractIndex:"2"を返す。<br>
     *         
     * <br>
     * 期待値：(戻り値) String:"[3]"<br>
     *         (状態変化) extractIndex:引数"abc"が渡されたことを確認する。<br>
     *         
     * <br>
     * 【文字列のテスト】<br>
     * incrementが1で<br>
     * extractIndexが"2"を返すパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractIncremantIndexStringint02() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub01 test = 
            new JXPathIndexedBeanWrapperImplStub01(object);
        
        // 入力値
        String property = "abc";
        int increment = 1;
        
        // 前提（スタブ）
        test.extractIndexResult = "2";
        
        // テスト実施
        String result = test.extractIncrementIndex(property,increment);
        
        // 判定
        assertEquals("abc",test.extractIndexParam1);
        assertEquals("[3]",result);
    }

    /**
     * testExtractIncremantIndexStringint03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc"<br>
     *         (引数) increment:-1<br>
     *         (状態) extractIndex:"2"を返す。<br>
     *         
     * <br>
     * 期待値：(戻り値) String:"[1]"<br>
     *         (状態変化) extractIndex:引数"abc"が渡されたことを確認する。<br>
     *         
     * <br>
     * 【文字列のテスト】<br>
     * incrementが-1で<br>
     * extractIndexが"2"を返すパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractIncremantIndexStringint03() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub01 test = 
            new JXPathIndexedBeanWrapperImplStub01(object);
        
        // 入力値
        String property = "abc";
        int increment = -1;
        
        // 前提（スタブ）
        test.extractIndexResult = "2";
        
        // テスト実施
        String result = test.extractIncrementIndex(property,increment);
        
        // 判定
        assertEquals("abc",test.extractIndexParam1);
        assertEquals("[1]",result);
    }

    /**
     * testExtractIndex01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) property:""[空文字]<br>
     *         
     * <br>
     * 期待値：(戻り値) String:""<br>
     *         
     * <br>
     * 【空文字のテスト】<br>
     * 引数のpropertyが空文字のテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractIndex01() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = "";
                
        // テスト実施
        String result = test.extractIndex(property);
        
        // 判定
        assertEquals("",result);
    }

    /**
     * testExtractIndex02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc"<br>
     *         
     * <br>
     * 期待値：(戻り値) String:""<br>
     *         
     * <br>
     * 【文字のテスト】<br>
     * 引数のpropertyに"["または"]"が入っていないパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractIndex02() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = "abc";
                
        // テスト実施
        String result = test.extractIndex(property);
        
        // 判定
        assertEquals("",result);
    }

    /**
     * testExtractIndex03()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"["<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    　メッセージ：Cannot get Index. Invalid property name. '['<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    Cannot get Index. Invalid property name. '['<br>
     *         
     * <br>
     * 【不正文字のテスト】<br>
     * 引数のpropertyが"["のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractIndex03() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = "[";
        
        // テスト実施
        try{
            test.extractIndex(property);
            fail();
        }catch(IllegalArgumentException e){
            // 判定
            assertEquals("Cannot get Index. Invalid property name. '['", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                            "Cannot get Index. Invalid property name. '['"));
            
        }
    }

    /**
     * testExtractIndex04()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"]"<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    　メッセージ：Cannot get Index. Invalid property name. ']'<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    Cannot get Index. Invalid property name. ']'<br>
     *         
     * <br>
     * 【不正文字のテスト】<br>
     * 引数のpropertyが"]"のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractIndex04() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = "]";
        
        // テスト実施
        try{
            test.extractIndex(property);
            fail();
        }catch(IllegalArgumentException e){
            // 判定
            assertEquals("Cannot get Index. Invalid property name. ']'", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                            "Cannot get Index. Invalid property name. ']'"));
            
        }
    }

    /**
     * testExtractIndex05()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"]["<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    　メッセージ：Cannot get Index. Invalid property name. ']['<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    Cannot get Index. Invalid property name. ']['<br>
     *         
     * <br>
     * 【不正文字のテスト】<br>
     * 引数のpropertyが"]["のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractIndex05() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = "][";
        
        // テスト実施
        try{
            test.extractIndex(property);
            fail();
        }catch(IllegalArgumentException e){
            // 判定
            assertEquals("Cannot get Index. Invalid property name. ']['", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                            "Cannot get Index. Invalid property name. ']['"));
            
        }
    }

    /**
     * testExtractIndex06()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"[]"<br>
     *         
     * <br>
     * 期待値：(戻り値) String:""<br>
     *         
     * <br>
     * 【文字のテスト】<br>
     * 引数のpropertyが"[]"のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractIndex06() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = "[]";
                
        // テスト実施
        String result = test.extractIndex(property);
        
        // 判定
        assertEquals("",result);
    }

    /**
     * testExtractIndex07()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"ab[c]d[3]"<br>
     *         
     * <br>
     * 期待値：(戻り値) String:"3"<br>
     *         
     * <br>
     * 【文字のテスト】<br>
     * 引数のpropertyに"[]"が二つ入っているパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractIndex07() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = "ab[c]d[3]";
                
        // テスト実施
        String result = test.extractIndex(property);
        
        // 判定
        assertEquals("3",result);
    }

    /**
     * testEscapeMapProperty01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc"<br>
     *         (状態) extractMapPropertyName:property+"Name"を返す。<br>
     *         (状態) extractMapPropertyKey:property+"Key"<br>
     *         
     * <br>
     * 期待値：(戻り値) String:abcName/abcKey<br>
     *         (状態変化) extractMapPropertyName:当メソッドは引数(property="abc")で戻り値property+"Name"とする。<br>
     *         (状態変化) extractMapPropertyKey:当メソッドは引数(property="abc")で戻り値property+"Key"とする。<br>
     *         
     * <br>
     * 【メソッドの呼び出しと結果の整形のテスト】
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testEscapeMapProperty01() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub01 test = 
            new JXPathIndexedBeanWrapperImplStub01(object);
        
        // 入力値設定
        String property = "abc";
        
        // テスト実施
        String result = test.escapeMapProperty(property);
        
        // 判定
        assertEquals("abcName/abcKey",result);
    }

    /**
     * testExtractMapPropertyName01()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) property:""（空文字）<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    　メッセージ：Cannot get Map attribute. Invalid property name. ''<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    Cannot get Map attribute. Invalid property name. ''<br>
     *         
     * <br>
     * 【空文字のテスト】<br>
     * 引数のpropertyが空文字のテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractMapPropertyName01() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = "";
        
        // テスト実施
        try{
            test.extractMapPropertyName(property);
            fail();
        }catch(IllegalArgumentException e){
            // 判定
            assertEquals(
                    "Cannot get Map attribute. Invalid property name. ''", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                    "Cannot get Map attribute. Invalid property name. ''"));
            
        }
    }

    /**
     * testExtractMapPropertyName02()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc"<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    　メッセージ：Cannot get Map attribute. Invalid property name. 'abc'<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    Cannot get Map attribute. Invalid property name. 'abc'<br>
     *         
     * <br>
     * 【不正文字のテスト】<br>
     * 引数のpropertyに"("または")"が入っていないパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractMapPropertyName02() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = "abc";
        
        // テスト実施
        try{
            test.extractMapPropertyName(property);
            fail();
        }catch(IllegalArgumentException e){
            // 判定
            assertEquals("Cannot get Map attribute. Invalid property name. 'abc'", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                            "Cannot get Map attribute. Invalid property name. 'abc'"));
            
        }
    }

    /**
     * testExtractMapPropertyName03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"("<br>
     *         
     * <br>
     * 期待値：(戻り値) String:""<br>
     *         
     * <br>
     * 【文字のテスト】<br>
     * 引数のpropertyが"("のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractMapPropertyName03() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値設定
        String property = "(";
        
        // テスト実施
        String result = test.extractMapPropertyName(property);
        
        // 判定
        assertEquals("",result);
    }

    /**
     * testExtractMapPropertyName04()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"ab()"<br>
     *         
     * <br>
     * 期待値：(戻り値) String:"ab"<br>
     *         
     * <br>
     * 【文字のテスト】<br>
     * 引数のpropertyの"()"の前にだけ文字が入っているパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractMapPropertyName04() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値設定
        String property = "ab()";
        
        // テスト実施
        String result = test.extractMapPropertyName(property);
        
        // 判定
        assertEquals("ab",result);
    }

    /**
     * testExtractMapPropertyKey01()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) property:""（空文字）<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    　メッセージ：Cannot get Map key. Invalid property name. ''<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    Cannot get Map key. Invalid property name. ''<br>
     *         
     * <br>
     * 【空文字のテスト】<br>
     * 引数のpropertyが空文字のテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractMapPropertyKey01() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = "";
        
        // テスト実施
        try{
            test.extractMapPropertyKey(property);
            fail();
        }catch(IllegalArgumentException e){
            // 判定
            assertEquals("Cannot get Map key. Invalid property name. ''", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                            "Cannot get Map key. Invalid property name. ''"));
            
        }
    }

    /**
     * testExtractMapPropertyKey02()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc"<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    　メッセージ：Cannot get Map key. Invalid property name. 'abc'<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    Cannot get Map key. Invalid property name. 'abc'<br>
     *         
     * <br>
     * 【不正文字のテスト】<br>
     * 引数のpropertyに"("または")"が入っていないパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractMapPropertyKey02() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = "abc";
        
        // テスト実施
        try{
            test.extractMapPropertyKey(property);
            fail();
        }catch(IllegalArgumentException e){
            // 判定
            assertEquals("Cannot get Map key. Invalid property name. 'abc'", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                            "Cannot get Map key. Invalid property name. 'abc'"));
            
        }
    }

    /**
     * testExtractMapPropertyKey03()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"("<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    　メッセージ：Cannot get Map key. Invalid property name. '('<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    Cannot get Map key. Invalid property name. '('<br>
     *         
     * <br>
     * 【不正文字のテスト】<br>
     * 引数のpropertyが"("のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractMapPropertyKey03() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = "(";
        
        // テスト実施
        try{
            test.extractMapPropertyKey(property);
            fail();
        }catch(IllegalArgumentException e){
            // 判定
            assertEquals("Cannot get Map key. Invalid property name. '('", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                            "Cannot get Map key. Invalid property name. '('"));
            
        }
    }

    /**
     * testExtractMapPropertyKey04()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:")"<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    　メッセージ：Cannot get Map key. Invalid property name. ')'<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    Cannot get Map key. Invalid property name. ')'<br>
     *         
     * <br>
     * 【不正文字のテスト】<br>
     * 引数のpropertyが")"のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractMapPropertyKey04() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = ")";
        
        // テスト実施
        try{
            test.extractMapPropertyKey(property);
            fail();
        }catch(IllegalArgumentException e){
            // 判定
            assertEquals("Cannot get Map key. Invalid property name. ')'", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                            "Cannot get Map key. Invalid property name. ')'"));
            
        }
    }

    /**
     * testExtractMapPropertyKey05()
     * <br><br>
     * 
     * (異常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:")("<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    　メッセージ：Cannot get Map key. Invalid property name. ')('<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    Cannot get Map key. Invalid property name. ')('<br>
     *         
     * <br>
     * 【不正文字のテスト】<br>
     * 引数のpropertyが")("のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractMapPropertyKey05() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値
        String property = ")(";
        
        // テスト実施
        try{
            test.extractMapPropertyKey(property);
            fail();
        }catch(IllegalArgumentException e){
            // 判定
            assertEquals("Cannot get Map key. Invalid property name. ')('", 
                        e.getMessage());
            assertTrue(LogUTUtil.checkError(
                            "Cannot get Map key. Invalid property name. ')('"));
            
        }
    }

    /**
     * testExtractMapPropertyKey06()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"()"<br>
     *         
     * <br>
     * 期待値：(戻り値) String:""<br>
     *         
     * <br>
     * 【文字のテスト】<br>
     * 引数のpropertyが"()"のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractMapPropertyKey06() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値設定
        String property = "()";
        
        // テスト実施
        String result = test.extractMapPropertyKey(property);
        
        // 判定
        assertEquals("",result);
    }

    /**
     * testExtractMapPropertyKey07()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"ab(cd)e"<br>
     *         
     * <br>
     * 期待値：(戻り値) String:"cd"<br>
     *         
     * <br>
     * 【文字のテスト】<br>
     * 引数のpropertyの"("と")"の前後に文字が入っているパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testExtractMapPropertyKey07() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImpl test = 
            new JXPathIndexedBeanWrapperImpl(object);
        
        // 入力値設定
        String property = "ab(cd)e";
        
        // テスト実施
        String result = test.extractMapPropertyKey(property);
        
        // 判定
        assertEquals("cd",result);
    }

    /**
     * testIsMapProperty01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) property:""（空文字）<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         
     * <br>
     * 【空文字のテスト】<br>
     * 引数のpropertyが空文字のテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsMapProperty01() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub01 test = 
            new JXPathIndexedBeanWrapperImplStub01(object);
        
        // 入力値設定
        String property = "";
        
        // テスト実施
        boolean result = test.isMapProperty(property);
        
        // 判定
        assertFalse(result);
    }

    /**
     * testIsMapProperty02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"abc"<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         
     * <br>
     * 【文字のテスト】<br>
     * 引数のpropertyに"("または")"が入っていないパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsMapProperty02() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub01 test = 
            new JXPathIndexedBeanWrapperImplStub01(object);
        
        // 入力値設定
        String property = "abc";
        
        // テスト実施
        boolean result = test.isMapProperty(property);
        
        // 判定
        assertFalse(result);
    }

    /**
     * testIsMapProperty03()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"("<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         
     * <br>
     * 【文字のテスト】<br>
     * 引数のpropertyが"("のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsMapProperty03() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub01 test = 
            new JXPathIndexedBeanWrapperImplStub01(object);
        
        // 入力値設定
        String property = "(";
        
        // テスト実施
        boolean result = test.isMapProperty(property);
        
        // 判定
        assertFalse(result);
    }

    /**
     * testIsMapProperty04()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:")"<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         
     * <br>
     * 【文字のテスト】<br>
     * 引数のpropertyが")"のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsMapProperty04() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub01 test = 
            new JXPathIndexedBeanWrapperImplStub01(object);
        
        // 入力値設定
        String property = ")";
        
        // テスト実施
        boolean result = test.isMapProperty(property);
        
        // 判定
        assertFalse(result);
    }

    /**
     * testIsMapProperty05()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:")("<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         
     * <br>
     * 【文字のテスト】<br>
     * 引数のpropertyが")("のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsMapProperty05() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub01 test = 
            new JXPathIndexedBeanWrapperImplStub01(object);
        
        // 入力値設定
        String property = ")(";
        
        // テスト実施
        boolean result = test.isMapProperty(property);
        
        // 判定
        assertTrue(result);
    }

    /**
     * testIsMapProperty06()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) property:"()"<br>
     *         
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         
     * <br>
     * 【文字のテスト】<br>
     * 引数のpropertyが"()"のパターンのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsMapProperty06() throws Exception {
        // 前処理
        Object object = new Object();
        JXPathIndexedBeanWrapperImplStub01 test = 
            new JXPathIndexedBeanWrapperImplStub01(object);
        
        // 入力値設定
        String property = "()";
        
        // テスト実施
        boolean result = test.isMapProperty(property);
        
        // 判定
        assertTrue(result);
    }

}
