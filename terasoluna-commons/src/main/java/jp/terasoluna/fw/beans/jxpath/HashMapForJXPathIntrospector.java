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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * commons-JXPathのバグ(JXPATH-152)回避用HashMap。
 * <p>
 * commons-JXPath-1.3の
 * JXPathIntrospectorの実装に合わせて、
 * putとgetのみ、スレッドセーフ化している。<br>
 * 同期化制御には、ReadWriteLockを利用しており、
 * putの実行中は、他のスレッドはputもgetも実行できない(一時的に待ち状態となる)が、
 * putの実行中でなければ、複数のスレッドで同時にgetを実行することができる。
 * </p>
 * @see JXPATH152PatchActivator
 */
public class HashMapForJXPathIntrospector<K, V> extends HashMap<K, V> {

    /**
     * シリアルバージョンID。
     */
    private static final long serialVersionUID = 1944915046869984094L;

    /**
     * 読み込みロックと書き込みロックのペアを制御するReadWriteLock。
     */
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * 読み込みロック。
     */
    private final Lock readLock = readWriteLock.readLock();

    /**
     * 書き込みロック。
     */
    private final Lock writeLock = readWriteLock.writeLock();

    /**
     * 指定された Map と同じマッピングで新規 HashMapForJXPathIntrospector を作成する。
     * @param m 初期マッピングを保持したマップ(JXPathIntrospectorから取得したマップ)
     * @throws NullPointerException 指定されたマップが null の場合
     */
    public HashMapForJXPathIntrospector(Map<? extends K, ? extends V> m) {
        super(m);
    }

    /**
     * キーにマッピングされている値を返す。
     * <p>
     * このメソッドは、読み込みロックを獲得した状態で、{@link HashMap#get(Object)}に委譲する。<br>
     * </p>
     * @param key キー
     * @see HashMap#get(Object)
     */
    @Override
    public V get(Object key) {
        readLock.lock();
        try {
            return super.get(key);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 指定されたキーで指定された値をマッピングする。
     * <p>
     * このメソッドは、書き込みロックを獲得した状態で、{@link HashMap#put(Object, Object)}に委譲する。<br>
     * </p>
     * @param key キー
     * @param value 値
     * @see HashMap#put(Object, Object)
     */
    @Override
    public V put(K key, V value) {
        writeLock.lock();
        try {
            return super.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }
}
