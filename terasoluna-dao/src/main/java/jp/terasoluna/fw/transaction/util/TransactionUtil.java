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

import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * トランザクション関連のユーティリティクラス。
 * <h4>【クラスの概要】</h4>
 * 例外を発生させずともロールバックを実行するさいに使用するTransactionUtilクラス。<br>
 * setRollbackOnlyメソッド使用してisRollbackOnlyステータスをtrueに変更する。
 * <p>
 */
public class TransactionUtil {

    /**
     * ロールバック実行のフラグを立てる。
     * 
     * <p>
     * 業務処理でのトランザクション処理において、例外を発生させずともif文
     * の条件分岐などによってロールバックを実行するためのフラグを設定する。
     * </p>
     */
    public static void setRollbackOnly() {

        // RollbackOnlyのフラグをtrueに設定する。
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

    }
}
