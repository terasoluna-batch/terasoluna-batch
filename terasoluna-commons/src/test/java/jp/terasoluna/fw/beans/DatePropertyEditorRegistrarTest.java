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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

import org.springframework.beans.propertyeditors.CustomDateEditor;

/**
 * {@link jp.terasoluna.fw.beans.DatePropertyEditorRegistrar} クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * Date型のプロパティエディタを生成するクラス。
 * <p>
 * 
 * @see jp.terasoluna.fw.beans.DatePropertyEditorRegistrar
 */
public class DatePropertyEditorRegistrarTest extends TestCase {


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
    public DatePropertyEditorRegistrarTest(String name) {
        super(name);
    }

    /**
     * testSetDateFormat01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) dateFormat:SimpleDateFormat("yyyyMMdd")<br>
     *         (状態) this.dateFormat:SimpleDateFormat("yyyy/MM/dd")<br>
     *         (状態) なし:ー<br>
     *         
     * <br>
     * 期待値：(状態変化) this.dateFormat:SimpleDateFormat("yyyyMMdd")<br>
     *         
     * <br>
     * dateFormate属性のsetメソッドのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testSetDateFormat01() throws Exception {
        // 前処理
    	DatePropertyEditorRegistrar registrar
    		= new DatePropertyEditorRegistrar();
    	DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        // テスト実施
    	registrar.setDateFormat(dateFormat);

        // 判定
    	assertSame(dateFormat, UTUtil.getPrivateField(registrar, "dateFormat"));
    }

    /**
     * testRegisterCustomEditors01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) registry:PropertyEditorRegistryのモック<br>
     *         (状態) this.dateFormat:SimpleDateFormat("yyyy/MM/dd")<br>
     *         
     * <br>
     * 期待値：(状態変化) registry.registerCustomEditor（）:引数を受け取ったことを確認する。<br>
     *         
     * <br>
     * プロパティエディタ生成メソッドを呼び出すテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testRegisterCustomEditors01() throws Exception {
        // 前処理
    	DatePropertyEditorRegistrar registrar
            = new DatePropertyEditorRegistrar();
    	PropertyEditorRegistrar_PropertyEditorRegistryStub01 registry
    		= new PropertyEditorRegistrar_PropertyEditorRegistryStub01();

        // テスト実施
    	registrar.registerCustomEditors(registry);

        // 判定
    	assertSame(Date.class, registry.clazz);
    	assertSame(CustomDateEditor.class, registry.editor.getClass());
    	SimpleDateFormat resultDateFormat 
    		= (SimpleDateFormat) UTUtil.getPrivateField(registry.editor, "dateFormat");
    	assertSame("yyyy/MM/dd", UTUtil.getPrivateField(resultDateFormat, "pattern"));
    }

}
