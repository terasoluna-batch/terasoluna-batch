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

package jp.terasoluna.fw.transaction.util;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.SimpleTransactionStatus;

/**
 * TransactionUtil ブラックボックステスト。<br>
 * {@link jp.terasoluna.fw.util.TransactionUtil} クラスのテスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * 例外を発生させずともロールバックを実行するさいに使用するTransactionUtilクラス。<br>
 * setRollbackOnlyメソッド使用してisRollbackOnlyステータスをtrueに変更する。
 * <p>
 * 
 * @see jp.terasoluna.fw.util.TransactionUtil
 */
public class TransactionUtilTest extends TestCase {

    /**
     * 例外が発生した時の為にメソッドを用意。
     */
    protected Method exceptionalMethod;

    /**
     * コンストラクタ。
     */
    public TransactionUtilTest() {
        try {
            // テストするメソッドをキャッシュ
            exceptionalMethod = ITestBean.class.getMethod("exceptional",
                    new Class[] { Throwable.class });
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException("Shouldn't happen", ex);
        }
    }

    /**
     * testSetRollbackOnly() <br>
     * <br>
     * 
     * (正常系) <br>
     * 観点：A<br>
     * <br>
     * 入力値：(呼び出しメソッド) setRollbackOnly();<br>
     * (状態) isRollbackOnly:false<br>
     * 
     * <br>
     * 期待値：(状態変化) isRollbackOnly:"true"<br>
     * 
     * <br>
     * TransactionUtilクラスのsetRollbackOnlyメソッドが呼び出された時、isRollbackOnlyがtrueになります。
     * <br>
     * 
     * @throws Throwable
     *             このメソッドで発生した例外
     */
    public void testSetRollbackOnly() throws Throwable {

        // 前処理
        TransactionAttribute txatt = new DefaultTransactionAttribute();

        MapTransactionAttributeSource tas = new MapTransactionAttributeSource();
        tas.register(exceptionalMethod, txatt);
        SimpleTransactionStatus status = new SimpleTransactionStatus();

        // モック作成
        PlatformTransactionManager mock = createMock(PlatformTransactionManager.class);

        // getTransactionメソッドは引数txatt、戻り値statusで1回呼ばれる
        expect(mock.getTransaction(txatt)).andReturn(status);
        expectLastCall().times(1);

        // commitメソッドは引数status、戻り値はvoidで1回呼ばれる
        mock.commit(status);
        expectLastCall().times(1);

        // モックを有効化
        replay(mock);

        TestBean outer = new TestBean() {
            @Override
            public void exceptional(Throwable t) throws Throwable {

                // テスト実行
                TransactionUtil.setRollbackOnly();

                SimpleTransactionStatus sts = new SimpleTransactionStatus();
                sts = (SimpleTransactionStatus) TransactionAspectSupport
                        .currentTransactionStatus();

                // 判定
                assertEquals(true, sts.isRollbackOnly());

            }
        };
        ITestBean outerProxy = (ITestBean) advised(outer, mock, tas);
        outerProxy.exceptional(null);

        // モックが規定通りのメソッド、および回数が呼び出されていたか検査
        verify(mock);

    }

    /**
     * アドバイスオブジェクトとトランザクションのセットアップ用のオブジェクトを作成するテンプレートメソッド。<br>
     * TransactionInterceptorを作成してそれを適用します。
     * 
     * @param target
     *            TransactionUtilTest null値
     * @param ptm
     *            Proxy0 インターフェースのためのEasyMock
     * @param tas
     *            MapTransactionAttributeSource ハッシュマップ
     * @return pf ProxyFactory JDBCインターフェースのプロキシ
     */
    Object advised(Object target, PlatformTransactionManager ptm,
            TransactionAttributeSource tas) {
        TransactionInterceptor ti = new TransactionInterceptor();
        ti.setTransactionManager(ptm);
        assertEquals(ptm, ti.getTransactionManager());
        ti.setTransactionAttributeSource(tas);
        assertEquals(tas, ti.getTransactionAttributeSource());

        ProxyFactory pf = new ProxyFactory(target);
        pf.addAdvice(0, ti);
        return pf.getProxy();
    }
}
