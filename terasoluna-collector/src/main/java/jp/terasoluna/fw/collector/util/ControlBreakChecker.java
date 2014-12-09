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

package jp.terasoluna.fw.collector.util;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.collector.LogId;
import jp.terasoluna.fw.collector.util.strategy.ComparatorCompareStrategy;
import jp.terasoluna.fw.collector.util.strategy.CompareStrategy;
import jp.terasoluna.fw.logger.TLogger;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * コントロールブレイクチェッカー.<br>
 */
public class ControlBreakChecker {

    /**
     * Log.
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(ControlBreakChecker.class);

    /**
     * コンストラクタ.
     */
    protected ControlBreakChecker() {
    }

    /**
     * 前処理コントロールブレイク判定メソッド.<br>
     * @param collector Collector&lt;?&gt;
     * @param keys String...
     * @return true:コントロールブレイクを行う/false:コントロールブレイクしない
     */
    public static boolean isPreBreak(Collector<?> collector, String... keys) {
        return isPreBreak(collector, null, keys);
    }

    /**
     * 前処理コントロールブレイク判定メソッド.<br>
     * keysの数とcompareStrategiesの数と、比較時に利用されるCompareStrategyの関係は以下のようになる.<br>
     * <ul>
     * <li>keys : compareStrategies = N : N (またはN以上)の場合
     * <ul>
     * <li>keys[i]のCompareStrategyはcompareStrategies[i]</li>
     * </ul>
     * </li>
     * <li>keys : compareStrategies = N : 1の場合
     * <ul>
     * <li>keys[i]のCompareStrategyはcompareStrategies[0]</li>
     * </ul>
     * </li>
     * <li>keys : compareStrategies = N : M (N &gt; M)の場合
     * <ul>
     * <li>keys[i] (i &lt; M)のCompareStrategyはcompareStrategies[i]</li>
     * <li>keys[i] (i &gt;= M)のCompareStrategyはnull</li>
     * </ul>
     * </li>
     * </ul>
     * 比較仕様は、{@link #equalsObjects(Object, Object, CompareStrategy)} を参照のこと.
     * @param collector Collector&lt;?&gt;
     * @param compareStrategies CompareStrategy&lt;?&gt;[]
     * @param keys String[]
     * @return true:コントロールブレイクを行う/false:コントロールブレイクしない
     * @see #equalsObjects(Object, Object, CompareStrategy)
     */
    public static boolean isPreBreak(Collector<?> collector,
            CompareStrategy<?>[] compareStrategies, String[] keys) {
        if (collector != null) {
            Object current = collector.getCurrent();
            Object other = collector.getPrevious();

            return isBreakInternal(current, other, compareStrategies, keys);
        }
        return false;
    }

    /**
     * 後処理コントロールブレイク判定メソッド.<br>
     * @param collector Collector&lt;?&gt;
     * @param keys String...
     * @return true:コントロールブレイクを行う/false:コントロールブレイクしない
     */
    public static boolean isBreak(Collector<?> collector, String... keys) {
        return isBreak(collector, null, keys);
    }

    /**
     * 後処理コントロールブレイク判定メソッド.<br>
     * keysの数とcompareStrategiesの数と、比較時に利用されるCompareStrategyの関係は以下のようになる.<br>
     * <ul>
     * <li>keys : compareStrategies = N : N (またはN以上)の場合
     * <ul>
     * <li>keys[i]のCompareStrategyはcompareStrategies[i]</li>
     * </ul>
     * </li>
     * <li>keys : compareStrategies = N : 1の場合
     * <ul>
     * <li>keys[i]のCompareStrategyはcompareStrategies[0]</li>
     * </ul>
     * </li>
     * <li>keys : compareStrategies = N : M (N &gt; M)の場合
     * <ul>
     * <li>keys[i] (i &lt; M)のCompareStrategyはcompareStrategies[i]</li>
     * <li>keys[i] (i &gt;= M)のCompareStrategyはnull</li>
     * </ul>
     * </li>
     * </ul>
     * 比較仕様は、{@link #equalsObjects(Object, Object, CompareStrategy)} を参照のこと.
     * @param collector Collector&lt;?&gt;
     * @param compareStrategies CompareStrategy&lt;?&gt;[]
     * @param keys String[]
     * @return true:コントロールブレイクを行う/false:コントロールブレイクしない
     * @see #equalsObjects(Object, Object, CompareStrategy)
     */
    public static boolean isBreak(Collector<?> collector,
            CompareStrategy<?>[] compareStrategies, String[] keys) {
        if (collector != null) {
            Object current = collector.getCurrent();
            Object other = collector.getNext();

            return isBreakInternal(current, other, compareStrategies, keys);
        }
        return false;
    }

    /**
     * コントロールブレイク判定メソッド.<br>
     * このメソッドは、ver.1.1.x以前との互換性を保つために残している.<br>
     * 新規に作成するコードの場合は、<br>
     * {@link #isBreak(Collector, CompareStrategy[], String[])}、<br>
     * {@link #isPreBreak(Collector, CompareStrategy[], String[])}、<br>
     * {@link #isBreakInternal(Object, Object, CompareStrategy[], String...)}<br>
     * を使用すること.<br>
     * @param current Object 比較元オブジェクト
     * @param other Object 比較先オブジェクト
     * @param comparators Comparator&lt;?&gt;[]
     * @param keys String...
     * @return true:コントロールブレイクを行う/false:コントロールブレイクしない
     * @see #isBreak(Collector, CompareStrategy[], String[])
     * @see #isPreBreak(Collector, CompareStrategy[], String[])
     * @see #isBreakInternal(Object, Object, CompareStrategy[], String...)
     */
    protected static boolean isBreakInternal(Object current, Object other,
            Comparator<?>[] comparators, String... keys) {

        // comparator -> compareStrategyへの詰め替えを行う
        if (comparators != null) {
            CompareStrategy<?>[] compareStrategies = new CompareStrategy[comparators.length];

            for (int i = 0; i < comparators.length; i++) {
                compareStrategies[i] = new ComparatorCompareStrategy(
                        comparators[i]);
            }
            return isBreakInternal(current, other, compareStrategies, keys);
        } else {
            return isBreakInternal(current, other, (CompareStrategy[]) null,
                    keys);
        }

    }

    /**
     * コントロールブレイク判定メソッド.<br>
     * keysの数とcompareStrategiesの数と、比較時に利用されるCompareStrategyの関係は以下のようになる.<br>
     * <ul>
     * <li>keys : compareStrategies = N : N (またはN以上)の場合
     * <ul>
     * <li>keys[i]のCompareStrategyはcompareStrategies[i]</li>
     * </ul>
     * </li>
     * <li>keys : compareStrategies = N : 1の場合
     * <ul>
     * <li>keys[i]のCompareStrategyはcompareStrategies[0]</li>
     * </ul>
     * </li>
     * <li>keys : compareStrategies = N : M (N &gt; M)の場合
     * <ul>
     * <li>keys[i] (i &lt; M)のCompareStrategyはcompareStrategies[i]</li>
     * <li>keys[i] (i &gt;= M)のCompareStrategyはnull</li>
     * </ul>
     * </li>
     * </ul>
     * 比較には {@link #equalsObjects(Object, Object, CompareStrategy)} メソッドを使用する.
     * @param current Object 比較元オブジェクト
     * @param other Object 比較先オブジェクト
     * @param compareStrategies CompareStrategy&lt;?&gt;[]
     * @param keys String...
     * @return true:コントロールブレイクを行う/false:コントロールブレイクしない
     * @see #equalsObjects(Object, Object, CompareStrategy)
     */
    protected static boolean isBreakInternal(Object current, Object other,
            CompareStrategy<?>[] compareStrategies, String... keys) {

        // keyリストが空もしくはnullの場合はfalse
        if (keys == null || keys.length == 0) {
            // コントロールブレイクなし
            return false;
        }

        // 片方がnullで、もう片方がnot nullの場合はtrue
        if ((current != null && other == null)
                || (current == null && other != null)) {
            // コントロールブレイク発生
            return true;
        }

        if (other != null && current != null) {

            for (int keyIndex = 0; keyIndex < keys.length; keyIndex++) {
                String key = keys[keyIndex];
                CompareStrategy<?> compareStrategy = null;

                if (compareStrategies != null) {
                    if (compareStrategies.length == 1) {
                        compareStrategy = compareStrategies[0];
                    } else if (keyIndex < compareStrategies.length) {
                        compareStrategy = compareStrategies[keyIndex];
                    }
                }

                if (key != null && key.length() != 0) {
                    Object currentValue = null;
                    Object otherValue = null;

                    // 値を取得する
                    try {
                        currentValue = PropertyUtils.getProperty(current, key);
                    } catch (Exception e) {
                        logOutputPropNotFound(e, current, key);
                        // ログを出力して次の項目をチェック
                        continue;
                    }

                    // 値を取得する
                    try {
                        otherValue = PropertyUtils.getProperty(other, key);
                    } catch (Exception e) {
                        logOutputPropNotFound(e, other, key);
                        // ログを出力して次の項目をチェック
                        continue;
                    }

                    // 比較
                    if (!equalsObjects(currentValue, otherValue, compareStrategy)) {
                        return true;
                    }
                }
            }
        }
        // コントロールブレイクなし
        return false;
    }

    /**
     * 前処理コントロールブレイクキー取得.<br>
     * @param collector Collector&lt;?&gt;
     * @param keys String...
     * @return コントロールブレイクキーリスト
     */
    public static Map<String, Object> getPreBreakKey(Collector<?> collector,
            String... keys) {
        return getPreBreakKey(collector, null, keys);
    }

    /**
     * 前処理コントロールブレイクキー取得.<br>
     * keysの数とcompareStrategiesの数と、比較時に利用されるCompareStrategyの関係は以下のようになる.<br>
     * <ul>
     * <li>keys : compareStrategies = N : N (またはN以上)の場合
     * <ul>
     * <li>keys[i]のCompareStrategyはcompareStrategies[i]</li>
     * </ul>
     * </li>
     * <li>keys : compareStrategies = N : 1の場合
     * <ul>
     * <li>keys[i]のCompareStrategyはcompareStrategies[0]</li>
     * </ul>
     * </li>
     * <li>keys : compareStrategies = N : M (N &gt; M)の場合
     * <ul>
     * <li>keys[i] (i &lt; M)のCompareStrategyはcompareStrategies[i]</li>
     * <li>keys[i] (i &gt;= M)のCompareStrategyはnull</li>
     * </ul>
     * </li>
     * </ul>
     * 比較仕様は、{@link #equalsObjects(Object, Object, CompareStrategy)} を参照のこと.
     * @param collector Collector&lt;?&gt;
     * @param compareStrategies CompareStrategy&lt;?&gt;[]
     * @param keys String[]
     * @return コントロールブレイクキーリスト
     * @see #equalsObjects(Object, Object, CompareStrategy)
     */
    public static Map<String, Object> getPreBreakKey(Collector<?> collector,
            CompareStrategy<?>[] compareStrategies, String[] keys) {
        if (collector != null) {
            Object current = collector.getCurrent();
            Object other = collector.getPrevious();

            return getBreakKeyInternal(current, other,
                    (CompareStrategy<?>[]) compareStrategies, keys);
        }
        return new LinkedHashMap<String, Object>();
    }

    /**
     * 後処理コントロールブレイクキー取得.<br>
     * @param collector Collector&lt;?&gt;
     * @param keys String...
     * @return コントロールブレイクキーリスト
     */
    public static Map<String, Object> getBreakKey(Collector<?> collector,
            String... keys) {
        return getBreakKey(collector, null, keys);
    }

    /**
     * 後処理コントロールブレイクキー取得.<br>
     * keysの数とcompareStrategiesの数と、比較時に利用されるCompareStrategyの関係は以下のようになる.<br>
     * <ul>
     * <li>keys : compareStrategies = N : N (またはN以上)の場合
     * <ul>
     * <li>keys[i]のCompareStrategyはcompareStrategies[i]</li>
     * </ul>
     * </li>
     * <li>keys : compareStrategies = N : 1の場合
     * <ul>
     * <li>keys[i]のCompareStrategyはcompareStrategies[0]</li>
     * </ul>
     * </li>
     * <li>keys : compareStrategies = N : M (N &gt; M)の場合
     * <ul>
     * <li>keys[i] (i &lt; M)のCompareStrategyはcompareStrategies[i]</li>
     * <li>keys[i] (i &gt;= M)のCompareStrategyはnull</li>
     * </ul>
     * </li>
     * </ul>
     * 比較仕様は、{@link #equalsObjects(Object, Object, CompareStrategy)} を参照のこと.
     * @param collector Collector&lt;?&gt;
     * @param compareStrategies CompareStrategy&lt;?&gt;[]
     * @param keys String[]
     * @return コントロールブレイクキーリスト
     * @see #equalsObjects(Object, Object, CompareStrategy)
     */
    public static Map<String, Object> getBreakKey(Collector<?> collector,
            CompareStrategy<?>[] compareStrategies, String[] keys) {
        if (collector != null) {
            Object current = collector.getCurrent();
            Object other = collector.getNext();

            return getBreakKeyInternal(current, other,
                    (CompareStrategy<?>[]) compareStrategies, keys);
        }
        return new LinkedHashMap<String, Object>();
    }

    /**
     * コントロールブレイクキー取得.<br>
     * このメソッドは、ver.1.1.x以前との互換性を保つために残している.<br>
     * 新規に作成するコードの場合は、<br>
     * {@link #getBreakKey(Collector, CompareStrategy[], String[])}、<br>
     * {@link #getPreBreakKey(Collector, CompareStrategy[], String[])}、<br>
     * {@link #getBreakKeyInternal(Object, Object, CompareStrategy[], String...)}<br>
     * を使用すること.<br>
     * @param current Object 比較元オブジェクト
     * @param other Object 比較先オブジェクト
     * @param comparators Comparator&lt;?&gt;[]
     * @param keys String...
     * @return コントロールブレイクキーリスト
     * @see #getBreakKey(Collector, CompareStrategy[], String[])
     * @see #getPreBreakKey(Collector, CompareStrategy[], String[])
     * @see #getBreakKeyInternal(Object, Object, CompareStrategy[], String...)
     */
    protected static Map<String, Object> getBreakKeyInternal(Object current,
            Object other, Comparator<?>[] comparators, String... keys) {

        // comparator -> compareStrategyへの詰め替えを行う
        if (comparators != null) {
            CompareStrategy<?>[] compareStrategies = new CompareStrategy[comparators.length];

            for (int i = 0; i < comparators.length; i++) {
                compareStrategies[i] = new ComparatorCompareStrategy(
                        comparators[i]);
            }
            return getBreakKeyInternal(current, other, compareStrategies, keys);
        } else {
            return getBreakKeyInternal(current, other,
                    (CompareStrategy[]) null, keys);
        }
    }

    /**
     * コントロールブレイクキー取得.<br>
     * keysの数とcompareStrategiesの数と、比較時に利用されるCompareStrategyの関係は以下のようになる.<br>
     * <ul>
     * <li>keys : compareStrategies = N : N (またはN以上)の場合
     * <ul>
     * <li>keys[i]のCompareStrategyはcompareStrategies[i]</li>
     * </ul>
     * </li>
     * <li>keys : compareStrategies = N : 1の場合
     * <ul>
     * <li>keys[i]のCompareStrategyはcompareStrategies[0]</li>
     * </ul>
     * </li>
     * <li>keys : compareStrategies = N : M (N &gt; M)の場合
     * <ul>
     * <li>keys[i] (i &lt; M)のCompareStrategyはcompareStrategies[i]</li>
     * <li>keys[i] (i &gt;= M)のCompareStrategyはnull</li>
     * </ul>
     * </li>
     * </ul>
     * 比較には {@link #equalsObjects(Object, Object, CompareStrategy)} メソッドを使用する.
     * @param current Object 比較元オブジェクト
     * @param other Object 比較先オブジェクト
     * @param compareStrategies CompareStrategy&lt;?&gt;[]
     * @param keys String...
     * @return コントロールブレイクキーリスト
     * @see #equalsObjects(Object, Object, CompareStrategy)
     */
    protected static Map<String, Object> getBreakKeyInternal(Object current,
            Object other, CompareStrategy<?>[] compareStrategies,
            String... keys) {
        boolean inBreak = false;
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        // keyリストが空もしくはnullの場合はfalse
        if (keys == null || keys.length == 0) {
            // コントロールブレイクなし
            return result;
        }

        for (int keyIndex = 0; keyIndex < keys.length; keyIndex++) {
            String key = keys[keyIndex];
            CompareStrategy<?> compareStrategy = null;
            Object currentValue = null;
            Object otherValue = null;

            if (compareStrategies != null) {
                if (compareStrategies.length == 1) {
                    compareStrategy = compareStrategies[0];
                } else if (keyIndex < compareStrategies.length) {
                    compareStrategy = compareStrategies[keyIndex];
                }
            }

            if (key != null && key.length() != 0) {

                // 値を取得する
                if (current != null) {
                    try {
                        currentValue = PropertyUtils.getProperty(current, key);
                    } catch (Exception e) {
                        logOutputPropNotFound(e, current, key);
                        // ログを出力して次の項目をチェック
                        continue;
                    }
                }

                // 値を取得する
                if (other != null) {
                    try {
                        otherValue = PropertyUtils.getProperty(other, key);
                    } catch (Exception e) {
                        logOutputPropNotFound(e, other, key);
                        // ログを出力して次の項目をチェック
                        continue;
                    }
                }

                if (!inBreak) {
                    // 片方がnullで、もう片方がnot nullの場合はtrue
                    if ((current != null && other == null)
                            || (current == null && other != null)) {
                        // コントロールブレイク発生
                        inBreak = true;
                    }

                    // 比較
                    if (!equalsObjects(currentValue, otherValue, compareStrategy)) {
                        // コントロールブレイク発生
                        inBreak = true;
                    }
                }
            }

            if (inBreak) {
                result.put(key, currentValue);
            }
        }
        return result;
    }

    /**
     * あるオブジェクトと別のオブジェクトが等しいかどうか比較する.<br>
     * <code>equalsObjects(value1, value2, null);</code> を実行するのと等価である.<br>
     * このメソッドは、ver.1.1.x以前とのコンパイル互換性を保つために残している.<br>
     * 
     * 以下のクラスを<b>除く</b>、Comparable実装クラスのインスタンス同士の比較は、
     * ver.1.1.x以前とは比較結果が異なる可能性がある.<br>
     * <ul>
     * <li>ver.1.1.x以前ではequalsメソッドで比較され、かつ、
     * Comparableの実装とequalsの実装に一貫性があるクラス.
     * <ul>
     * <li>java.math.BigInteger</li>
     * <li>java.lang.Byte</li>
     * <li>java.lang.Double</li>
     * <li>java.lang.Float</li>
     * <li>java.lang.Integer</li>
     * <li>java.lang.Long</li>
     * <li>java.lang.Short</li>
     * <li>java.lang.Boolean</li>
     * <li>java.lang.Character</li>
     * <li>java.lang.String</li>
     * <li>java.util.Date(java.sql.Date等のサブクラスを含まない)</li>
     * </ul>
     * </li>
     * </ul>
     * 
     * このメソッドの代わりに、<br>
     * {@link #equalsObjects(Object, Object, CompareStrategy)}<br>
     * を使用すること.<br>
     * @param value1 Object 比較元オブジェクト
     * @param value2 Object 比較先オブジェクト
     * @return 等しい場合:true / そうでない場合:false
     * @see #equalsObjects(Object, Object, CompareStrategy)
     * @deprecated このメソッドの代わりに、{@link #equalsObjects(Object, Object, CompareStrategy)}を使用すること.
     */
    protected static boolean equalsObjects(Object value1, Object value2) {
        return equalsObjects(value1, value2, null);
    }

    /**
     * ログ出力（プロパティが見つからなかった場合）.<br>
     * @param e Exception
     * @param obj Object
     * @param key String
     */
    protected static void logOutputPropNotFound(Exception e, Object obj,
            String key) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(LogId.WAL041002, key, obj == null ? null : obj
                    .getClass().getSimpleName(),
                    e == null ? null : e.getMessage());
        }
    }

    /**
     * あるオブジェクトと別のオブジェクトが等しいかどうか比較する.<br>
     * <ul>
     * <li>どちらもnullでない場合、以下のように比較する.
     * <ul>
     * <li>compareStrategyがnullでない場合、compareStrategyで比較</li>
     * <li>compareStrategyがnullである場合、比較元オブジェクトの型に応じて、以下のように比較
     * <ul>
     * <li>比較元オブジェクトがComparable実装クラスのインスタンスの場合、Comparable#compareToで比較
     * <li>比較元オブジェクトがClassまたはそのスーパークラスのインスタンスの場合、Object#equalsで比較
     * <li>上記2つ以外の場合、org.apache.commons.lang.builder.EqualsBuilder#reflectionEqualsで比較
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>どちらもnullの場合、等しいとみなす.</li>
     * <li>どちか一方のみがnullの場合、等くないとみなす.</li>
     * </ul>
     * @param value1 Object 比較元オブジェクト
     * @param value2 Object 比較先オブジェクト
     * @param compareStrategy CompareStrategy
     * @return 等しい場合:true / そうでない場合:false
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static boolean equalsObjects(Object value1,
            Object value2, CompareStrategy compareStrategy) {

        if (value1 != null && value2 != null) {
            // value1,value2のどちらにも値が入っている場合
            if (compareStrategy != null) {
                return compareStrategy.equalsObjects(value1, value2);
            } else {
                Class<? extends Object> clazz = value1.getClass();

                if (value1 instanceof Comparable) {
                    // value1がComparableを実装している場合はcompareToで比較する
                    return (((Comparable) value1).compareTo(value2) == 0);
                } else if (clazz.isAssignableFrom(Class.class)) {
                    // value1がClassまたはそのスーパークラスのインスタンスの場合Object#equalsで比較する
                    return value1.equals(value2);
                } else {
                    // それ以外の場合はreflectionEqualsで比較する
                    return EqualsBuilder.reflectionEquals(value1, value2);
                }
            }
        } else if (value1 == null && value2 == null) {
            // value1,value2のどちらにも値が入っていない場合
            return true;
        }
        // value1,value2のどちらか一方に値が入っている場合
        return false;

    }
}
