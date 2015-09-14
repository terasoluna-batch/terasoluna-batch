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

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

import org.apache.commons.jxpath.JXPathIntrospector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * commons-JXPathのバグ(JXPATH-152)用パッチをアクティベートするクラス。
 * <p>
 * クラスロードのタイミングで、 JXPathIntrospectorの、問題を抱えているprivate staticフィールド(byClass, byInterface)内のHashMapを パッチオブジェクト(
 * {@link HashMapForJXPathIntrospector})に差し替える。<br>
 * </p>
 * <p>
 * このアクティベータを使用するには、アプリケーション起動時に、このクラスをロードする(ロードされていないときにロードする)必要がある。<br>
 * applicationContext.xmlに、以下の記述を追加することで、可能である。 <fieldset> <legend>applicationContext.xml</legend> &lt;bean
 * id=&quot;jxpathPatchActivator&quot; class=&quot;jp.terasoluna.fw.beans.jxpath.JXPATH152PatchActivator&quot;/&gt; </fieldset>
 * </p>
 * <p>
 * 特記事項：<br>
 * このクラスは、JXPathIntrospectorのprivate staticフィールドにアクセスするため、 セキュリティマネージャを設定している場合には、
 * privateフィールドへのアクセス権(java.lang.reflect.ReflectPermissionのsuppressAccessChecks)を付与する必要がある。<br>
 * なお、privateフィールドアクセスは特権モードで実行する(呼び出し元のクラスの権限が低い場合でも、このクラスに与えられた権限で実行する)ため、 アクセス権の付与対象を絞りたい場合は、以下の例のように、
 * このクラスを含むjarファイルのみに限定してprivateフィールドへのアクセス権を付与すればよい。 <fieldset> <legend>セキュリティポリシーファイルの設定例(Tomcatの例)</legend>
 * 
 * <pre>
 * grant codeBase "file:${catalina.home}/webapps/＜ContextRoot＞/WEB-INF/lib/＜このクラスを含むjarファイル名＞" {
 *     permission java.lang.reflect.ReflectPermission "suppressAccessChecks";
 * };
 * </pre>
 * 
 * </fieldset> (アクセス権の付与対象を絞る必要が無い場合は、「codeBase "file:～"」の部分を省略する。)<br>
 * セキュリティマネージャが有効かつ権限不足でパッチをアクティベートできなかった場合や、 commons-JXPath-1.3とはJXPathIntrospectorの実装が異なるバージョンのcommons-JXPathを使用するなどして
 * パッチをアクティベートできなかった場合には、FATALログを出力する。<br>
 * ただし、Errorが発生しない限り、パッチをアクティベートできなくても、例外はスローしない。<br>
 * したがって、Errorが発生する場合を除き、パッチをアクティベートできなかった場合、パッチが当たっていない状態でアプリケーションを継続する。
 * </p>
 * @see HashMapForJXPathIntrospector
 */
public class JXPATH152PatchActivator {

    /**
     * ログクラス。
     */
    private static final Log log = LogFactory.getLog(
            JXPATH152PatchActivator.class);

    static {
        // 特権モードで実行する。
        // (呼び出し元のクラスの権限が低い場合でも、このクラスに与えられた権限で実行する。)
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                activate();
                return null;
            }
        });
    }

    /**
     * パッチをアクティベートする。
     */
    private static void activate() {
        try {
            // もともと使用されているMapオブジェクトを取得
            Field byClassField = JXPathIntrospector.class.getDeclaredField(
                    "byClass");
            byClassField.setAccessible(true);
            Map<?, ?> byClass = (Map<?, ?>) byClassField.get(null);

            Field byInterfaceField = JXPathIntrospector.class.getDeclaredField(
                    "byInterface");
            byInterfaceField.setAccessible(true);
            Map<?, ?> byInterface = (Map<?, ?>) byInterfaceField.get(null);

            // Mapオブジェクトを差し替える
            byClassField.set(null,
                    new HashMapForJXPathIntrospector<Object, Object>(byClass));
            byInterfaceField.set(null,
                    new HashMapForJXPathIntrospector<Object, Object>(byInterface));

            log.info("JXPATH-152 Patch activation succeeded.");
        } catch (Exception e) {
            // セキュリティマネージャの制限による例外や、
            // 想定していないバージョンのcommons-JXPathを使用したことによる例外
            log.fatal("JXPATH-152 Patch activation failed.", e);
        }
    }
}
