/*
 * Copyright (c) 2012 NTT DATA Corporation
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

package jp.terasoluna.fw.beans.jxpath;

import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.DomainCombiner;
import java.security.Permissions;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.JXPathIntrospector;

import jp.terasoluna.utlib.LogUTUtil;
import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.beans.jxpath.JXPATH152PatchActivator} クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * commons-JXPathのバグ(JXPATH-152)用パッチをアクティベートするクラス。<br>
 * 前提条件：<br>
 * ・このテストを実行する際、JVMのセキュリティマネージャを有効にしないこと<br>
 * ・このテストを実行する際、同一JVM内で同時に他のテストを実行しないこと<br>
 *   (一時的にセキュリティマネージャを有効にするため、有効になっている間に他のテストが動作すると、
 *    そのテストでエラーが発生する可能性がある。
 *    なお、同時に実行さえしなければ、同一JVM内であとから他のテストを実行してもよい。)<br>
 * ・このテストを実行する際、JREのセキュリティポリシーファイル<br>
 *   (デフォルトでは、JREインストールディレクトリ/lib/security/java.policy)にて、<br>
 *   permission java.lang.reflect.ReflectPermission "suppressAccessChecks";<br>
 *   の権限を与えないこと。(インストール後の状態のままであれば、この権限はない。)<br>
 * ・このテストを実行する際、テスト対象となるclassファイルと、テストケースのclassファイルは、同一のディレクトリに出力しないこと。
 * <p>
 * 
 * @see jp.terasoluna.fw.beans.jxpath.JXPATH152PatchActivator
 */
@SuppressWarnings("unchecked")
public class JXPATH152PatchActivatorTest extends TestCase {

    private Map byClassBak = null;

    private Map byInterfaceBak = null;


    /**
     * 初期化処理を行う。
     * 
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // クラスロードされていなければクラスロード
        // (クラスロード時にstaticイニシャライザ実行)
        new JXPATH152PatchActivator();

        // JXPATH152PatchActivatorのstaticイニシャライザ実行後の状態を保持
        // (tearDownでstaticイニシャライザ実行前の状態に戻してしまわないように)
        byClassBak = (Map) UTUtil.getPrivateField(JXPathIntrospector.class, "byClass");
        byInterfaceBak = (Map) UTUtil.getPrivateField(JXPathIntrospector.class, "byInterface");
        LogUTUtil.flush();
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
        UTUtil.setPrivateField(JXPathIntrospector.class, "byClass", byClassBak);
        UTUtil.setPrivateField(JXPathIntrospector.class, "byInterface", byInterfaceBak);
    }

    /**
     * testActivate01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) －<br>
     *         (状態) JXPathIntrospector.byClasst:HashMap<br>
     *         (状態) JXPathIntrospector.byInterface:HashMap<br>
     *         
     * <br>
     * 期待値：(状態変化) JXPathIntrospector.byClassがHashMapForJXPathIntrospectorになっている。<br>
     *         (状態変化) JXPathIntrospector.byInterfaceがHashMapForJXPathIntrospectorになっている。<br>
     *         (状態変化) ログ:ログレベル：INFO<br>
     *                    JXPATH-152 Patch activation succeeded.<br>
     *         
     * <br>
     * セキュリティマネージャに阻止されなければ、
     * JXPathIntrospectorのbyClassとbyInterfaceがHashMapForJXPathIntrospectorインスタンスになっていることのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testActivate01() throws Exception {
        // 前処理
        UTUtil.setPrivateField(JXPathIntrospector.class, "byClass", new HashMap());
        UTUtil.setPrivateField(JXPathIntrospector.class, "byInterface", new HashMap());

        // テスト実施
        UTUtil.invokePrivate(JXPATH152PatchActivator.class, "activate");

        // 判定
        assertTrue(LogUTUtil.checkInfo("JXPATH-152 Patch activation succeeded."));
        assertTrue(UTUtil.getPrivateField(JXPathIntrospector.class, "byClass").getClass() == HashMapForJXPathIntrospector.class);
        assertTrue(UTUtil.getPrivateField(JXPathIntrospector.class, "byInterface").getClass() == HashMapForJXPathIntrospector.class);
    }

    /**
     * testActivate01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) －<br>
     *         (状態) JXPathIntrospector.byClasst:HashMap<br>
     *         (状態) JXPathIntrospector.byInterface:HashMap<br>
     *         
     * <br>
     * 期待値：(状態変化) JXPathIntrospector.byClassがHashMapのまま。<br>
     *         (状態変化) JXPathIntrospector.byInterfaceがHashMapのまま。<br>
     *         (状態変化) ログ:ログレベル：FATAL<br>
     *                    JXPATH-152 Patch activation failed.<br>
     *         
     * <br>
     * セキュリティマネージャに阻止された場合、
     * FATALログを出力することのテスト。
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testActivate02() throws Exception {
//      Java7環境下ではAccessController.doPrivileged()に渡しているAccessControlContextのDomainCombiner#combine()が呼び出されず、本テストの動作確認が不可能であるため、コメントアウトしている。
//        // アクセス権設定
//        final ProtectionDomain testTargetProtectionDomain = JXPATH152PatchActivator.class.getProtectionDomain();
//        DomainCombiner domainCombiner = new DomainCombiner() {
//            public ProtectionDomain[] combine(
//                    ProtectionDomain[] currentDomains,
//                    ProtectionDomain[] assignedDomains) {
//                ProtectionDomain[] ret = new ProtectionDomain[currentDomains.length];
//                for (int i = 0; i < currentDomains.length ;i++) {
//                    // テストケースクラスやライブラリにあるクラス等、
//                    // 試験対象クラス(が含まれるクラスパスにあるクラス)以外は、全操作に対する権限を与える
//                    if (currentDomains[i].getCodeSource() != testTargetProtectionDomain.getCodeSource()) {
//                        Permissions permissions = new Permissions();
//                        permissions.add(new AllPermission());
//                        ProtectionDomain pd = new ProtectionDomain(currentDomains[i].getCodeSource(), permissions);
//                        ret[i] = pd;
//                    } else {
//                        // 試験対象クラス(が含まれるクラスパスにあるクラス)は、デフォルトの権限のまま
//                        // (Field#setAccessibleが禁止される)
//                        ret[i] = currentDomains[i];
//                    }
//                }
//                return ret;
//            }
//        };
//        AccessControlContext acc = new AccessControlContext(AccessController.getContext(), domainCombiner);
//        System.setSecurityManager(new SecurityManager());
//
//        // 上記のDomainCombinerで編集したアクセス権設定で、テストを実行
//        AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
//
//            public Void run() throws Exception {
//                try {
//                    // 前処理
//                    UTUtil.setPrivateField(JXPathIntrospector.class, "byClass", new HashMap());
//                    UTUtil.setPrivateField(JXPathIntrospector.class, "byInterface", new HashMap());
//
//                    // テスト実施
//                    UTUtil.invokePrivate(JXPATH152PatchActivator.class, "activate");
//
//                    // 判定
//                    assertTrue(LogUTUtil.checkFatal("JXPATH-152 Patch activation failed.", new AccessControlException("")));
//                    assertTrue(UTUtil.getPrivateField(JXPathIntrospector.class, "byClass").getClass() == HashMap.class);
//                    assertTrue(UTUtil.getPrivateField(JXPathIntrospector.class, "byInterface").getClass() == HashMap.class);
//                } finally {
//                    System.setSecurityManager(null);
//                }
//
//                return null;
//            }
//
//        }, acc);
    }
}
