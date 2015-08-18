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

package jp.terasoluna.fw.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;


/**
 * {@link jp.terasoluna.fw.util.ProxyUtil} クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * プロキシ関連のユーティリティクラス。
 * <p>
 * 
 * @see jp.terasoluna.fw.util.ProxyUtil
 */
public class ProxyUtilTest {

    /**
     * testGetTargetClass01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：C
     * <br><br>
     * 入力値：(引数) proxy:null<br>
     *         
     * <br>
     * 期待値：(状態変化) 例外:IllegalArgumentException<br>
     *                    "Proxy object is null."<br>
     *         
     * <br>
     * プロキシオブジェクトがnullの場合のテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetTargetClass01() throws Exception {
    	// テスト実施
    	try {
    		ProxyUtil.getTargetClass(null);
    		fail();
    	} catch (IllegalArgumentException e) {
    		// OK
    		assertEquals("Proxy object is null.", e.getMessage());
    	}
    }

    /**
     * testGetTargetClass02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) proxy:Cglib2AopProxy<br>
     *                　∟JavaBeanオブジェクト<br>
     *         
     * <br>
     * 期待値：(戻り値) Class:JavaBean.class<br>
     *         
     * <br>
     * プロキシオブジェクトがCGLIBで作成された場合のテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetTargetClass02() throws Exception {
        // 前処理
    	ProxyFactory pf = new ProxyFactory(new ProxyUtil_JavaBeanStub01());
    	pf.setProxyTargetClass(true);
        Object proxy = pf.getProxy();
        assertTrue(AopUtils.isCglibProxy(proxy));

        // テスト実施
    	Class result = ProxyUtil.getTargetClass(proxy);

        // 判定
    	assertSame(ProxyUtil_JavaBeanStub01.class, result);
    }

    /**
     * testGetTargetClass03()
     * <br><br>
     * 
     * (正常系) 
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) proxy:JavaBean<br>
     *         
     * <br>
     * 期待値：(戻り値) Class:JavaBean.class<br>
     *         
     * <br>
     * ターゲットオブジェクトにプロキシがかかっていない場合のテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetTargetClass03() throws Exception {
        // テスト実施
    	Class result = ProxyUtil.getTargetClass(new ProxyUtil_JavaBeanStub01());

        // 判定
    	assertSame(ProxyUtil_JavaBeanStub01.class, result);
    }

    /**
     * testGetTargetClass04()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) proxy:JdkDynamicAopProxy<br>
     *                　∟JavaBean<br>
     *         
     * <br>
     * 期待値：(戻り値) Class:JavaBean.class<br>
     *         
     * <br>
     * プロキシオブジェクトがProxyで作成された場合のテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetTargetClass04() throws Exception {
    	// 前処理
    	ProxyFactory pf = new ProxyFactory(new ProxyUtil_JavaBeanStub01());
        Object proxy = pf.getProxy();

        assertTrue(AopUtils.isJdkDynamicProxy(proxy));

        // テスト実施
    	Class result = ProxyUtil.getTargetClass(proxy);

        // 判定
    	assertSame(ProxyUtil_JavaBeanStub01.class, result);
    }

    /**
     * testGetTargetClass05()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) proxy:JdkDynamicAopProxy<br>
     *                　∟JdkDynamicAopProxy<br>
     *                　　　　∟JavaBean<br>
     *         
     * <br>
     * 期待値：(戻り値) Class:JavaBean.class<br>
     *         
     * <br>
     * プロキシオブジェクトがネストしたProxyで作成された場合のテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetTargetClass05() throws Exception {
        // 前処理
    	ProxyFactory parentPf = new ProxyFactory(new ProxyUtil_JavaBeanStub01());
        Object parent = parentPf.getProxy();
        
        ProxyFactory pf = new ProxyFactory(parent);
        Object proxy = pf.getProxy();

        assertTrue(AopUtils.isJdkDynamicProxy(proxy));

        // テスト実施
    	Class result = ProxyUtil.getTargetClass(proxy);

        // 判定
    	assertSame(ProxyUtil_JavaBeanStub01.class, result);
    }
}
