/*
 * Copyright (c) 2011 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.util;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.exception.IllegalClassTypeException;
import jp.terasoluna.fw.logger.TLogger;
import jp.terasoluna.fw.util.PropertyUtil;

import org.apache.commons.logging.Log;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * バッチ実装用ユーティリティ。<br>
 * <br>
 * 各種バッチ実装にて使用するユーティリティメソッドを定義する。
 */
public class BatchUtil {
    /** ロガー. */
    private static final TLogger LOGGER = TLogger.getLogger(BatchUtil.class);

    /**
     * コンストラクタ
     */
    protected BatchUtil() {
    }

    /**
     * 汎用文字列結合メソッド。
     * @param args 任意の値
     * @return 引数を結合した文字列
     */
    public static String cat(Object... args) {

        StringBuilder str = new StringBuilder();

        if (args == null) {
            return null;
        }

        for (Object o : args) {
            if (o != null) {
                str.append(o);
            }
        }

        return str.toString();
    }

    /**
     * インフォログの開始メッセージを取得する。
     * @param jobCd ジョブ業務コード
     * @return String メッセージ
     */
    public static String getInfoLogStartMsg(String jobCd) {

        return BatchUtil.cat("[", jobCd, "] ", "処理開始");
    }

    /**
     * インフォログの終了メッセージを取得する。
     * @param jobCd ジョブ業務コード
     * @return String メッセージ
     */
    public static String getInfoLogEndMsg(String jobCd) {

        return BatchUtil.cat("[", jobCd, "] ", "処理終了");
    }

    /**
     * デフォルトのTransactionDefinitionを取得する
     * @return
     */
    public static TransactionDefinition getTransactionDefinition() {
        return new DefaultTransactionDefinition();
    }

    /**
     * デフォルトのTransactionDefinitionを取得する
     * @param propagationBehavior トランザクション伝搬モード(@see TransactionDefinition) デフォルト：TransactionDefinition.PROPAGATION_REQUIRED
     * @param isolationLevel トランザクション分離レベル(@see TransactionDefinition) デフォルト：TransactionDefinition.ISOLATION_DEFAULT
     * @param timeout トランザクションタイムアウト(秒) デフォルト：TransactionDefinition.TIMEOUT_DEFAULT (タイムアウトなし)
     * @param readOnly リードオンリートランザクション デフォルト：false
     * @return
     */
    public static TransactionDefinition getTransactionDefinition(
            int propagationBehavior, int isolationLevel, int timeout,
            boolean readOnly) {
        DefaultTransactionDefinition td = new DefaultTransactionDefinition();
        td.setPropagationBehavior(propagationBehavior);
        td.setIsolationLevel(isolationLevel);
        td.setTimeout(timeout);
        td.setReadOnly(readOnly);
        return td;
    }

    /**
     * トランザクションを開始させる
     * @param tran PlatformTransactionManager
     * @return TransactionStatus
     */
    public static TransactionStatus startTransaction(
            PlatformTransactionManager tran) {
        return startTransaction(tran, getTransactionDefinition(), null);
    }

    /**
     * トランザクションを開始させる
     * @param tran PlatformTransactionManager
     * @param log Log
     * @return TransactionStatus
     */
    public static TransactionStatus startTransaction(
            PlatformTransactionManager tran, Log log) {
        return startTransaction(tran, getTransactionDefinition(), log);
    }

    /**
     * トランザクションを開始させる
     * @param tran PlatformTransactionManager
     * @param propagationBehavior トランザクション伝搬モード(@see TransactionDefinition) デフォルト：TransactionDefinition.PROPAGATION_REQUIRED
     * @param isolationLevel トランザクション分離レベル(@see TransactionDefinition) デフォルト：TransactionDefinition.ISOLATION_DEFAULT
     * @param timeout トランザクションタイムアウト(秒) デフォルト：TransactionDefinition.TIMEOUT_DEFAULT (タイムアウトなし)
     * @param readOnly リードオンリートランザクション デフォルト：false
     * @return TransactionStatus
     */
    public static TransactionStatus startTransaction(
            PlatformTransactionManager tran, int propagationBehavior,
            int isolationLevel, int timeout, boolean readOnly) {
        return startTransaction(tran, getTransactionDefinition(
                propagationBehavior, isolationLevel, timeout, readOnly), null);
    }

    /**
     * トランザクションを開始させる
     * @param tran PlatformTransactionManager
     * @param propagationBehavior トランザクション伝搬モード(@see TransactionDefinition) デフォルト：TransactionDefinition.PROPAGATION_REQUIRED
     * @param isolationLevel トランザクション分離レベル(@see TransactionDefinition) デフォルト：TransactionDefinition.ISOLATION_DEFAULT
     * @param timeout トランザクションタイムアウト(秒) デフォルト：TransactionDefinition.TIMEOUT_DEFAULT (タイムアウトなし)
     * @param readOnly リードオンリートランザクション デフォルト：false
     * @param log Log
     * @return TransactionStatus
     */
    public static TransactionStatus startTransaction(
            PlatformTransactionManager tran, int propagationBehavior,
            int isolationLevel, int timeout, boolean readOnly, Log log) {
        return startTransaction(tran, getTransactionDefinition(
                propagationBehavior, isolationLevel, timeout, readOnly), log);
    }

    /**
     * トランザクションを開始させる
     * @param tran PlatformTransactionManager
     * @param def TransactionDefinition
     * @return TransactionStatus
     */
    public static TransactionStatus startTransaction(
            PlatformTransactionManager tran, TransactionDefinition def) {
        return startTransaction(tran, def, null);
    }

    /**
     * トランザクションを開始させる
     * @param tran PlatformTransactionManager
     * @param def TransactionDefinition
     * @param log Log
     * @return TransactionStatus
     */
    public static TransactionStatus startTransaction(
            PlatformTransactionManager tran, TransactionDefinition def, Log log) {
        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025033, tran);
            if (def != null) {
                logDebug(log, LogId.DAL025034, def.getPropagationBehavior(), def
                        .getIsolationLevel(), def.getTimeout(), def
                                .isReadOnly(), def.getName());
            }
        }

        TransactionStatus stat = null;
        if (tran != null) {
            stat = tran.getTransaction(def);
        }

        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025035, stat);
        }
        return stat;
    }

    /**
     * トランザクションを開始させる
     * @param tranDef TransactionDefinition
     * @param tranMap PlatformTransactionManagerマップ
     * @return TransactionStatusマップ
     */
    public static Map<String, TransactionStatus> startTransactions(
            TransactionDefinition tranDef, Map<?, ?> tranMap) {
        return startTransactions(tranDef, tranMap, null);
    }

    /**
     * トランザクションを開始させる
     * @param tranDef TransactionDefinition
     * @param tranMap PlatformTransactionManagerマップ
     * @param log Log
     * @return TransactionStatusマップ
     */
    public static Map<String, TransactionStatus> startTransactions(
            TransactionDefinition tranDef, Map<?, ?> tranMap, Log log) {
        Map<String, TransactionStatus> statMap = new LinkedHashMap<String, TransactionStatus>();

        Set<?> entrySet = tranMap.entrySet();

        if (entrySet != null) {
            for (Object entObj : entrySet) {
                String key = null;
                PlatformTransactionManager ptm = null;

                // マップエントリー
                if (entObj instanceof Map.Entry) {
                    Map.Entry<?, ?> ent = (Entry<?, ?>) entObj;
                    if (ent != null) {
                        // キー取り出し
                        if (ent.getKey() instanceof String) {
                            key = (String) ent.getKey();
                        }
                        // トランザクションマネージャ取り出し
                        if (ent.getValue() instanceof PlatformTransactionManager) {
                            ptm = (PlatformTransactionManager) ent.getValue();
                        }
                    }
                }

                if (ptm != null) {

                    if (log != null && log.isDebugEnabled()) {
                        logDebug(log, LogId.DAL025033, key);
                        if (tranDef != null) {
                            logDebug(log, LogId.DAL025034, tranDef
                                    .getPropagationBehavior(), tranDef
                                            .getIsolationLevel(), tranDef
                                                    .getTimeout(), tranDef
                                                            .isReadOnly(),
                                    tranDef.getName());
                        }
                    }

                    // トランザクション開始
                    TransactionStatus trnStat = null;
                    try {
                        trnStat = ptm.getTransaction(tranDef);
                    } catch (TransactionException e) {
                        if (log != null && log.isErrorEnabled()) {
                            logError(log, LogId.EAL025048, e, key);
                        }
                        endTransactions(tranMap, statMap, log);
                        throw e;
                    }

                    // トランザクションステータスを格納
                    if (statMap != null) {
                        statMap.put(key, trnStat);
                    }

                    if (log != null && log.isDebugEnabled()) {
                        logDebug(log, LogId.DAL025036, key, trnStat);
                    }
                }
            }
        }

        return statMap;
    }

    /**
     * トランザクションをコミットさせる コネクションのコミットを行う
     * @param tran PlatformTransactionManager
     * @param stat TransactionStatus
     */
    public static void commitTransaction(PlatformTransactionManager tran,
            TransactionStatus stat) {
        commitTransaction(tran, stat, null);
    }

    /**
     * トランザクションをコミットさせる コネクションのコミットを行う
     * @param tran PlatformTransactionManager
     * @param stat TransactionStatus
     * @param log Log
     */
    public static void commitTransaction(PlatformTransactionManager tran,
            TransactionStatus stat, Log log) {
        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025037, stat);
        }

        if (tran != null && stat != null) {
            tran.commit(stat);
        }
        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025038, stat);
        }
    }

    /**
     * トランザクションをコミットさせる
     * @param tranMap トランザクションマップ
     * @param statMap トランザクション状態マップ
     */
    public static void commitTransactions(Map<?, ?> tranMap,
            Map<String, TransactionStatus> statMap) {
        commitTransactions(tranMap, statMap, null);
    }

    /**
     * トランザクションをコミットさせる
     * @param tranMap トランザクションマップ
     * @param statMap トランザクション状態マップ
     * @param log ロガー
     */
    public static void commitTransactions(Map<?, ?> tranMap,
            Map<String, TransactionStatus> statMap, Log log) {

        Set<Entry<String, TransactionStatus>> statSet = statMap.entrySet();

        if (statSet.isEmpty()) {
            return;
        }

        Stack<Entry<String, TransactionStatus>> stack = new Stack<Entry<String, TransactionStatus>>();
        for (Entry<String, TransactionStatus> stat : statSet) {
            stack.push(stat);
        }

        while (!stack.isEmpty()) {
            // トランザクション開始の逆順ステータスでコミットを実施する。
            Entry<String, TransactionStatus> statEntry = stack.pop();
            String key = statEntry.getKey();
            TransactionStatus trnStat = statEntry.getValue();
            if (trnStat == null) {
                continue;
            }

            // トランザクションマネージャ取り出し
            Object ptmObj = tranMap.get(key);
            if (ptmObj == null || !(ptmObj instanceof PlatformTransactionManager)) {
                continue;
            }
            PlatformTransactionManager ptm = (PlatformTransactionManager) ptmObj;

            if (log != null && log.isDebugEnabled()) {
                logDebug(log, LogId.DAL025039, key, trnStat);
                logDebug(log, LogId.DAL025038, trnStat);
            }
            // コミット
            ptm.commit(trnStat);
        }
    }

    /**
     * トランザクションを終了させる（未コミット時ロールバック）
     * @param tran PlatformTransactionManager
     * @param stat TransactionStatus
     */
    public static void endTransaction(PlatformTransactionManager tran,
            TransactionStatus stat) {
        endTransaction(tran, stat, null);
    }

    /**
     * トランザクションを終了させる（未コミット時ロールバック）
     * @param tran PlatformTransactionManager
     * @param stat TransactionStatus
     * @param log Log
     */
    public static void endTransaction(PlatformTransactionManager tran,
            TransactionStatus stat, Log log) {
        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025040, stat);
        }

        if (tran != null && stat != null && !stat.isCompleted()) {
            tran.rollback(stat);
        }

        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025041, stat);
        }
    }

    /**
     * トランザクションを終了させる（未コミット時ロールバック）
     * @param tranMap PlatformTransactionManagerマップ
     * @param statMap TransactionStatusマップ
     * @return 引数で与えられたPlatformTransactionManagerが全て正常に終了できた場合にtrueを返却する
     */
    public static boolean endTransactions(Map<?, ?> tranMap,
            Map<String, TransactionStatus> statMap) {
        return endTransactions(tranMap, statMap, null);
    }

    /**
     * トランザクションを終了させる（未コミット時ロールバック）
     * @param tranMap PlatformTransactionManagerマップ
     * @param statMap TransactionStatusマップ
     * @param log Log
     * @return 引数で与えられたPlatformTransactionManagerが全て正常に終了できた場合にtrueを返却する
     */
    public static boolean endTransactions(Map<?, ?> tranMap,
            Map<String, TransactionStatus> statMap, Log log) {
        boolean isNormal = true;

        Set<Entry<String, TransactionStatus>> statSet = statMap.entrySet();

        if (statSet == null || statSet.isEmpty()) {
            return isNormal;
        }

        Stack<Entry<String, TransactionStatus>> stack = new Stack<Entry<String, TransactionStatus>>();
        for (Entry<String, TransactionStatus> stat : statSet) {
            stack.push(stat);
        }

        while (!stack.isEmpty()) {
            // トランザクション開始の逆順でトランザクションを終了する。
            Entry<String, TransactionStatus> statEntry = stack.pop();
            String key = statEntry.getKey();
            TransactionStatus trnStat = statEntry.getValue();

            if (trnStat == null) {
                continue;
            }
            
            // トランザクションマネージャ取り出し
            Object ptmObj = tranMap.get(key);
            if (ptmObj == null || !(ptmObj instanceof PlatformTransactionManager)) {
                continue;
            }
            PlatformTransactionManager ptm = (PlatformTransactionManager) ptmObj;

            // トランザクション終了（トランザクションが未完了の場合はロールバックする）
            if (trnStat.isCompleted()) {
                continue;
            }

            if (log != null && log.isDebugEnabled()) {
                logDebug(log, LogId.DAL025042, key, trnStat);
                logDebug(log, LogId.DAL025045, trnStat);
            }

            // ロールバック
            try {
                ptm.rollback(trnStat);
            } catch (TransactionException e) {
                if (log != null && log.isErrorEnabled()) {
                    logError(log, LogId.EAL025045, e, key);
                }
                isNormal = false;
                // 例外が発生しても途中終了せず、他のトランザクション終了を試みる
            }

            if (log != null && log.isDebugEnabled()) {
                logDebug(log, LogId.DAL025041, trnStat);
            }
        }
        return isNormal;
    }

    /**
     * セーブポイントを設定する
     * @param stat TransactionStatus
     * @return Object セーブポイント
     */
    public static Object setSavepoint(TransactionStatus stat) {
        return setSavepoint(stat, null);
    }

    /**
     * セーブポイントを設定する
     * @param stat TransactionStatus
     * @param log Log
     * @return Object セーブポイント
     */
    public static Object setSavepoint(TransactionStatus stat, Log log) {
        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025046, stat);
        }

        Object savepoint = stat.createSavepoint();

        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025047, stat);
        }

        return savepoint;
    }

    /**
     * セーブポイントをリリースする
     * @param stat TransactionStatus
     * @param savepoint セーブポイント
     */
    public static void releaseSavepoint(TransactionStatus stat, Object savepoint) {
        releaseSavepoint(stat, savepoint, null);
    }

    /**
     * セーブポイントをリリースする
     * @param stat TransactionStatus
     * @param savepoint セーブポイント
     * @param log Log
     */
    public static void releaseSavepoint(TransactionStatus stat,
            Object savepoint, Log log) {
        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025048, stat);
        }

        stat.releaseSavepoint(savepoint);

        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025049, savepoint);
        }
    }

    /**
     * セーブポイントまでロールバックさせる
     * @param stat TransactionStatus
     * @param savepoint セーブポイント
     */
    public static void rollbackSavepoint(TransactionStatus stat,
            Object savepoint) {
        rollbackSavepoint(stat, savepoint, null);
    }

    /**
     * セーブポイントまでロールバックさせる
     * @param stat TransactionStatus
     * @param savepoint セーブポイント
     * @param log Log
     */
    public static void rollbackSavepoint(TransactionStatus stat,
            Object savepoint, Log log) {
        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025050, stat);
        }

        stat.rollbackToSavepoint(savepoint);

        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025051, stat);
        }
    }

    /**
     * トランザクション開始までロールバックする。
     * @param tran トランザクションマネージャ
     * @param stat TransactionStatus
     */
    public static void rollbackTransaction(PlatformTransactionManager tran,
            TransactionStatus stat) {
        rollbackTransaction(tran, stat, null);
    }

    /**
     * トランザクション開始までロールバックする。
     * @param tran トランザクションマネージャ
     * @param stat TransactionStatus
     * @param log Log
     */
    public static void rollbackTransaction(PlatformTransactionManager tran,
            TransactionStatus stat, Log log) {
        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025052, stat);
        }
        if (tran != null && stat != null && !stat.isCompleted()) {
            tran.rollback(stat);
        }
        if (log != null && log.isDebugEnabled()) {
            logDebug(log, LogId.DAL025053, stat);
        }

    }

    /**
     * トランザクションをコミットさせ、トランザクションを再度開始させる
     * @param tran PlatformTransactionManager
     * @param stat TransactionStatus
     * @return
     */
    public static TransactionStatus commitRestartTransaction(
            PlatformTransactionManager tran, TransactionStatus stat) {
        commitTransaction(tran, stat, null);
        endTransaction(tran, stat, null);
        return startTransaction(tran);
    }

    /**
     * トランザクションをコミットさせ、トランザクションを再度開始させる
     * @param tran PlatformTransactionManager
     * @param stat TransactionStatus
     * @param log Log
     */
    public static TransactionStatus commitRestartTransaction(
            PlatformTransactionManager tran, TransactionStatus stat, Log log) {
        commitTransaction(tran, stat, log);
        endTransaction(tran, stat, log);
        return startTransaction(tran, log);
    }

    /**
     * トランザクションをコミットさせ、トランザクションを再度開始させる
     * @param tran PlatformTransactionManager
     * @param stat TransactionStatus
     * @param def TransactionDefinition
     */
    public static TransactionStatus commitRestartTransaction(
            PlatformTransactionManager tran, TransactionStatus stat,
            TransactionDefinition def) {
        commitTransaction(tran, stat, null);
        endTransaction(tran, stat, null);
        return startTransaction(tran, def);
    }

    /**
     * トランザクションをコミットさせ、トランザクションを再度開始させる
     * @param tran PlatformTransactionManager
     * @param stat TransactionStatus
     * @param def TransactionDefinition
     * @param log Log
     */
    public static TransactionStatus commitRestartTransaction(
            PlatformTransactionManager tran, TransactionStatus stat,
            TransactionDefinition def, Log log) {
        commitTransaction(tran, stat, log);
        endTransaction(tran, stat, log);
        return startTransaction(tran, def, log);
    }

    /**
     * トランザクションをロールバックさせ、トランザクションを再度開始させる
     * @param tran PlatformTransactionManager
     * @param stat TransactionStatus
     */
    public static TransactionStatus rollbackRestartTransaction(
            PlatformTransactionManager tran, TransactionStatus stat) {
        rollbackTransaction(tran, stat, null);
        endTransaction(tran, stat, null);
        return startTransaction(tran);
    }

    /**
     * トランザクションをロールバックさせ、トランザクションを再度開始させる
     * @param tran PlatformTransactionManager
     * @param stat TransactionStatus
     * @param log Log
     */
    public static TransactionStatus rollbackRestartTransaction(
            PlatformTransactionManager tran, TransactionStatus stat, Log log) {
        rollbackTransaction(tran, stat, log);
        endTransaction(tran, stat, log);
        return startTransaction(tran, log);
    }

    /**
     * トランザクションをロールバックさせ、トランザクションを再度開始させる
     * @param tran PlatformTransactionManager
     * @param stat TransactionStatus
     * @param def TransactionDefinition
     */
    public static TransactionStatus rollbackRestartTransaction(
            PlatformTransactionManager tran, TransactionStatus stat,
            TransactionDefinition def) {
        rollbackTransaction(tran, stat, null);
        endTransaction(tran, stat, null);
        return startTransaction(tran, def);
    }

    /**
     * トランザクションをロールバックさせ、トランザクションを再度開始させる
     * @param tran PlatformTransactionManager
     * @param stat TransactionStatus
     * @param def TransactionDefinition
     * @param log Log
     */
    public static TransactionStatus rollbackRestartTransaction(
            PlatformTransactionManager tran, TransactionStatus stat,
            TransactionDefinition def, Log log) {
        rollbackTransaction(tran, stat, log);
        endTransaction(tran, stat, log);
        return startTransaction(tran, def, log);
    }

    /**
     * Listを配列型に変換する Listの中に複数の型が混じっている場合は使用できない
     * @param <E> 返却値の型
     * @param list 入力データ
     * @param clazz 返却値の型をあらわすClass型のインスタンス
     * @return Listの中身を配列にしたもの
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <E> E[] changeListToArray(List<E> list, Class clazz) {

        if (clazz == null) {
            throw new IllegalClassTypeException();
        }

        // SQLの実行：値の取得
        List<E> castedList = list;

        // 配列に変換
        E[] retArray = (E[]) Array.newInstance(clazz, castedList.size());
        try {
            castedList.toArray(retArray);
        } catch (ArrayStoreException e) {
            throw new IllegalClassTypeException(e);
        }

        return retArray;
    }

    /**
     * .propertiesファイルからグループキー指定で値を取り出す グループキーに合致したキーに対して昇順ソートを行ってから 返却リストへ値をセットしている
     * @param propertyName .propertiesファイルの名前（.propertiesは必要ない）
     * @param grpKey グループキー
     * @return propertyNameに存在するgrpKeyPrefix
     */
    public static List<String> getProperties(String propertyName, String grpKey) {

        Properties properties = PropertyUtil.loadProperties(propertyName);
        Enumeration<String> propNames = PropertyUtil.getPropertyNames(
                properties, grpKey);

        List<String> propNamesList = Collections.list(propNames);
        Collections.sort(propNamesList);

        List<String> resultList = new ArrayList<String>();

        for (String key : propNamesList) {
            resultList.add(PropertyUtil.getProperty(key));
        }

        return resultList;
    }

    /**
     * Java 仮想マシンのメモリ総容量、使用量、 使用を試みる最大メモリ容量の情報を返します。
     * @return Java 仮想マシンのメモリ情報
     */
    public static String getMemoryInfo() {
        DecimalFormat f1 = new DecimalFormat("#,###KB");
        DecimalFormat f2 = new DecimalFormat("##.#");

        Runtime rt = Runtime.getRuntime();
        long free = rt.freeMemory() / 1024;
        long total = rt.totalMemory() / 1024;
        long max = rt.maxMemory() / 1024;
        long used = total - free;
        double ratio = (used * 100 / (double) total);

        StringBuilder sb = new StringBuilder();

        sb.append("Java memory info : ");
        sb.append("used=");
        sb.append(f1.format(used));
        sb.append(" (");
        sb.append(f2.format(ratio));
        sb.append("%), ");
        sb.append("total=");
        sb.append(f1.format(total));
        sb.append(", ");
        sb.append("max=");
        sb.append(f1.format(max));

        return sb.toString();
    }

    /**
     * ログをデバッグレベル出力する。
     * TLogger / commons.logging のどちらのロガーにも対応する
     * @param log ロガー
     * @param logId 出力するログID
     * @param args ログの引数
     */
    private static void logDebug(Log log, String logId, Object... args) {
        if (log instanceof TLogger) {
            TLogger logger = (TLogger) log;
            logger.debug(logId, args);
        } else {
            String msg = LOGGER.getLogMessage(logId, args);
            log.debug(msg);
        }
    }

    /**
     * ログをエラーレベルで出力する
     * TLogger / commons.logging のどちらのロガーにも対応する
     * @param log ロガー
     * @param logId 出力するログID
     * @param args ログの引数
     */
    private static void logError(Log log, String logId, Object... args) {        
        if (log instanceof TLogger) {
            TLogger logger = (TLogger) log;
            logger.error(logId, args);
        } else {
            String msg = LOGGER.getLogMessage(logId, args);
            log.error(msg);
        }
    }
}
