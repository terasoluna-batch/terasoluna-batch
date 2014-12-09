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

import java.lang.reflect.Proxy;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

/**
 *  プロキシ関連のユーティリティクラス。
 *
 */
public class ProxyUtil {
    /**
     * プロキシのターゲットクラスを取得する。
     * @param proxy プロキシオブジェクト。
     * @return 指定したプロキシのターゲットクラス。
     */
    public static Class getTargetClass(Object proxy) {
        // Nullチェック
        if (proxy == null) {
            throw new IllegalArgumentException("Proxy object is null.");
        }
        if (AopUtils.isCglibProxy(proxy)) {
            return proxy.getClass().getSuperclass();
        }
        if (proxy instanceof Advised) {
            // 取得したターゲットがプロキシの場合、ネストしたターゲットを取得する
            do {
                proxy = getAdvisedTarget((Advised) proxy);
            } while (Proxy.isProxyClass(proxy.getClass()));
        }
        return proxy.getClass();
    }
    /**
     * プロキシのターゲットを取得する。
     * @param advised オブジェクト。
     * @return 指定したプロキシのターゲットオブジェクト。
     * @throws CannotGetTargetException 
     */
    private static Object getAdvisedTarget(Advised advised) {
        try {
            return advised.getTargetSource().getTarget();
        } catch (Exception e) {
            // ターゲットが取得できなかった場合の例外処理。
            throw new CannotGetTargetException(e);
        }
    }
}
