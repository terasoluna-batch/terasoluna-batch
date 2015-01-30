/*
 * Copyright (c) 2014 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.mock;

import java.util.*;

/**
 * モックRepository抽象クラスです。
 * 
 * <pre>
 * 本クラスを継承したモックRepositoryクラスでは
 * 
 * ・期待値の設定（試験前）
 * ・メソッド実行時の引数取得（試験後）
 * 
 * が行えます。
 * 
 * 設定した期待値は内部でキューに追加され、追加した順番でメソッド実行時に返り値として取り出されます。
 * </pre>
 * 
 */
public class AbstractMockDao {
    /**
     * メソッド実行結果の期待値を保持するキュー
     */
    protected final LinkedList<Object> results = new LinkedList<Object>();

    /**
     * メソッド実行時のパラメータを保持するリスト
     */
    protected final List<DaoParam> params = new ArrayList<DaoParam>();

    /**
     * 期待値をキューから取り出して返却します。
     * 
     * <pre>
     * キューが空の場合はnullを返却します。
     * 期待値が{@link RuntimeException}継承例外の場合、その例外をスローします。
     * </pre>
     * 
     * @return 期待値
     */
    protected Object poll() {
        Object result = results.poll();

        if (result instanceof RuntimeException) {
            throw (RuntimeException) result;
        }

        return result;
    }

    /**
     * 期待値を取り出してintとして返却します。
     * 
     * <pre>
     * 取りだしたオブジェクトがInteger出ない場合、{@link ClassCastException}をスローします。
     * </pre>
     * 
     * @return 期待値(int)
     * @throws ClassCastException
     * @see {@link #poll()}
     */
    protected int pollInt() throws ClassCastException {
        Object result = poll();

        if (result != null && result instanceof Integer) {
            return (Integer) result;
        } else {
            throw new ClassCastException(
                    "Integer was expected but the actual was " + (result == null ? null : result.getClass().getName()));
        }
    }

    /**
     * 期待値を取り出して配列として返却します。
     * 
     * <pre>
     * 取り出したオブジェクトが配列でない場合、{@link ClassCastException}をスローします。
     * </pre>
     * 
     * @return 期待値(配列)
     * @throws ClassCastException
     * @see {@link #poll()}
     */
    protected Object[] pollArray() throws ClassCastException {
        Object result = poll();

        if (result == null || !result.getClass().isArray()) {
            throw new ClassCastException(
                    "Array was expected but the actual was " + (result == null ? null : result.getClass().getName()));
        }

        return (Object[]) result;
    }

    /**
     * 期待値を取り出してリストとして返却します。
     * 
     * <pre>
     * 取り出したオブジェクトがリストでない場合、{@link ClassCastException}をスローします。
     * </pre>
     * 
     * @return 期待値(リスト)
     * @throws ClassCastException
     * @see {@link #poll()}
     */
    protected List<?> pollList() throws ClassCastException {
        Object result = poll();

        if (!(result instanceof List)) {
            throw new ClassCastException(
                    "java.util.List was expected but the actual was " + (result == null ? null : result.getClass().getName()));
        }

        return (List<?>) result;
    }

    /**
     * 期待値を取り出してマップとして返却します。
     * 
     * <pre>
     * 取り出したオブジェクトがマップでない場合、{@link ClassCastException}をスローします。
     * </pre>
     * 
     * @return 期待値(マップ)
     * @throws ClassCastException
     * @see {@link #poll()}
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Object> pollMap() throws ClassCastException {
        Object result = poll();

        if (!(result instanceof Map)) {
            throw new ClassCastException(
                    "java.util.Map was expected but the actual was " + (result == null ? null : result.getClass().getName()));
        }

        return (Map<String, Object>) result;
    }

    /**
     * 期待値を取り出してマップ配列として返却します。
     * 
     * <pre>
     * 取り出したオブジェクトが配列でない場合、{@link ClassCastException}をスローします。
     * </pre>
     * 
     * @return 期待値(マップ配列)
     * @throws ClassCastException
     * @see {@link #pollArray()}
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Object>[] pollMapArray() throws ClassCastException {
        return (Map<String, Object>[]) pollArray();
    }

    /**
     * 期待値を取り出してマップリストとして返却します。
     * 
     * <pre>
     * 取り出したオブジェクトがリストでない場合、{@link ClassCastException}をスローします。
     * </pre>
     * 
     * @return 期待値(マップリスト)
     * @throws ClassCastException
     * @see {@link #pollList()}
     */
    @SuppressWarnings("unchecked")
    protected List<Map<String, Object>> pollMapList() throws ClassCastException {
        return (List<Map<String, Object>>) pollList();
    }

    /**
     * コールバックを取り出して実行します。
     * 
     * <pre>
     * 取り出したオブジェクトがコールバックの場合、実行します。
     * 期待位置が{@link RuntimeException}継承例外の場合、その例外をスローします。
     * </pre>
     * 
     * @param args
     * @throws ClassCastException
     * @see {@link #poll()}
     */
    protected void pollAndExecuteIfCallback(Object[] args)
            throws ClassCastException {
        Object first = results.peek();
        if (first instanceof Callback) {
            ((Callback) poll()).execute(args);
        } else if (first instanceof RuntimeException) {
            poll();
        }
    }

    /**
     * メソッド実行時のパラメータをリストに追加します。
     * 
     * @param param パラメータ
     */
    protected void addParam(DaoParam param) {
        String methodName = null;

        StackTraceElement[] stacTraceElements = new Throwable().getStackTrace();
        if (stacTraceElements != null && stacTraceElements.length > 0) {
            // 呼び出し元メソッド名を取得
            methodName = stacTraceElements[1].getMethodName();
        }
        param.setMethodName(methodName);
        params.add(param);
    }

    /**
     * 期待値をキューに追加します。
     * 
     * @param result 期待値
     */
    public void addResult(Object result) {
        results.add(result);
    }

    /**
     * {@link java.util.Collection}で渡された期待値を全てキューに追加します。
     * @param results
     * @since 2.1.0
     */
    public void addResults(Collection<?> results) {
        this.results.addAll(results);
    }

    /**
     * メソッド実行時のパラメータを保持するリストを返却します。
     * 
     * @return メソッド実行時のパラメータを保持するリスト
     */
    public List<DaoParam> getParams() {
        return params;
    }

    /**
     * 初期化処理を行います。
     * 
     * <pre>
     * 実行結果期待値キューおよび、実行時パラメータリストを空にします。
     * </pre>
     */
    public void clear() {
        results.clear();
        params.clear();
    }
}
