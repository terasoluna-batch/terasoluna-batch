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

package jp.terasoluna.fw.collector.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * AbstractCollector用ArrayBlockingQueueサブクラス。
 * <p>
 * {@link ArrayBlockingQueue#peek()}と {@link ArrayBlockingQueue#isEmpty()}に、 キューが空であれば、キューに要素が入るか、キューイング終了フラグが上がるまで 待つ機能をつけている。<br>
 * ArrayBlockingQueue内の、ブロック制御を行っているConditionフィールドは サブクラスに公開されていないため、 このクラスではArrayBlockingQueueと冗長な実装をしている。<br>
 * </p>
 * <p>
 * 実装はAbstractCollectorに使用されるものに絞っているため、 すべてのメソッドが使用できるわけではない。<br>
 * このクラスでオーバーライドしているメソッド以外で、 キューの状態を変更するメソッドや、待ちが発生するメソッドを実行してはならない。
 * </p>
 * <p>
 * キューに要素を詰め終わった後は、キューに要素を詰めるスレッドで、必ずfinishQueueingメソッドを実行すること。
 * </p>
 * @param <E> コレクション内に存在する要素の型
 */
public class ArrayBlockingQueueEx<E> extends ArrayBlockingQueue<E>
                                                                  implements
                                                                  NotificationBlockingQueue<E> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7441765139909417804L;

    /**
     * キューの操作を同期化するロック。
     */
    protected final ReentrantLock queueLock = new ReentrantLock();

    /**
     * キューが空でなくなったときに送信されるシグナル。
     */
    protected final Condition notEmpty = queueLock.newCondition();

    /**
     * キューがFullでなくなったときに送信されるシグナル。
     */
    protected final Condition notFull = queueLock.newCondition();

    /**
     * キューサイズ。
     */
    protected final int capacity;

    /**
     * キューイング終了フラグ。
     */
    protected volatile boolean finishQueueingFlag = false;

    /**
     * 指定された (固定) 容量および指定されたアクセスポリシーを使用して、ArrayBlockingQueue を作成する。
     * @param capacity キューの容量
     * @param fair true の場合、挿入または削除時にブロックされたスレッドに対するキューアクセスは、FIFO の順序で処理される。 false の場合、アクセス順序は指定されない。
     * @see ArrayBlockingQueue#ArrayBlockingQueue(int, boolean)
     */
    public ArrayBlockingQueueEx(int capacity, boolean fair) {
        super(capacity, fair);
        this.capacity = capacity;
    }

    /**
     * 指定された (固定) 容量およびデフォルトのアクセスポリシーを使用して、ArrayBlockingQueue を作成する。
     * @param capacity キューの容量
     * @see ArrayBlockingQueue#ArrayBlockingQueue(int)
     */
    public ArrayBlockingQueueEx(int capacity) {
        super(capacity);
        this.capacity = capacity;
    }

    /**
     * キューイングの終了を通知する。
     * <p>
     * キューに要素が入るのを待っているスレッドがいる場合、そのブロックを解除する。 キューに要素を詰めるスレッドは、キューイングが完了したあとで、必ずこのメソッドを実行すること。
     * </p>
     */
    @Override
    public void finishQueueing() {
        queueLock.lock();
        try {
            finishQueueingFlag = true;

            // 要素の入り待ちを行っているスレッドのブロックを解除する
            notEmpty.signalAll();
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * 指定された要素をこのキューの末尾に挿入する。必要に応じ、空間が利用可能になるのを指定された時間まで待機する。
     * <p>
     * このメソッドの定義は、{@link ArrayBlockingQueue#offer(Object, long, TimeUnit)}と同じ。
     * </p>
     * @param o 追加する要素
     * @param timeout 処理を中止するまでの待機時間。単位は unit
     * @param unit timeout パラメータの解釈方法を指定する TimeUnit
     * @return 成功した場合は true、空間が利用可能になる前に指定された待機時間が経過した場合は false
     * @throws InterruptedException 待機中に割り込みが発生した場合
     * @throws NullPointerException 指定された要素が null である場合
     * @see ArrayBlockingQueue#offer(Object, long, TimeUnit)
     */
    @Override
    public boolean offer(E o, long timeout, TimeUnit unit)
                                                          throws InterruptedException {
        if (o == null) {
            throw new NullPointerException();
        }
        long nanos = unit.toNanos(timeout);
        queueLock.lockInterruptibly();
        try {
            while (size() == capacity) {

                // キューが空くのを待つ
                nanos = notFull.awaitNanos(nanos);
                if (nanos <= 0) {

                    // タイムアウト
                    return false;
                }
            }
            boolean success = super.offer(o);
            if (success) {

                // 要素の入り待ちを行っているスレッドのブロックを解除する
                notEmpty.signal();
            }
            return success;
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * 可能であれば、このキューの末尾に指定された要素を挿入する。このキューがいっぱいである場合には、即座に返す。
     * <p>
     * このメソッドの定義は、{@link ArrayBlockingQueue#offer(Object)}と同じ。
     * </p>
     * @param o 追加する要素
     * @return 要素をこのキューに追加可能な場合は true、そうでない場合は false
     * @throws NullPointerException 指定された要素が null である場合
     * @see ArrayBlockingQueue#offer(Object)
     */
    @Override
    public boolean offer(E o) {
        queueLock.lock();
        try {
            if (size() == capacity) {
                return false;
            }
            boolean success = super.offer(o);
            if (success) {

                // 要素の入り待ちを行っているスレッドのブロックを解除する
                notEmpty.signal();
            }
            return success;
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * 指定された要素をこのキューの末尾に追加する。必要に応じ、空間が利用可能になるまで待機する。
     * @param o 追加する要素
     * @throws InterruptedException 待機中に割り込みが発生した場合
     * @throws NullPointerException 指定された要素が null である場合
     */
    @Override
    public void put(E o) throws InterruptedException {
        if (o == null) {
            throw new NullPointerException();
        }
        queueLock.lock();
        try {
            while (size() == capacity) {

                // キューが空くのを待つ
                notFull.await();
            }
            super.put(o);

            // 要素の入り待ちを行っているスレッドのブロックを解除する
            notEmpty.signal();
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * キューの先頭を取得するが、削除しない。
     * <p>
     * 拡張仕様：<b> キューが空の場合は、キューに要素が入るか、キューイングの終了が通知されるまで待つ。<br>
     * キューイングの終了が通知された後、キューが空の場合は null を返す。
     * </p>
     * <p>
     * キューに要素がある場合や、キューイングの終了が通知された後の仕様は、 {@link ArrayBlockingQueue#peek()}と同じ。
     * </p>
     * @return キューの先頭。キューイング終了後にキューが空の場合は null
     */
    @Override
    public E peek() {
        queueLock.lock();
        try {
            while (!finishQueueingFlag && size() == 0) {
                try {

                    // キューに要素が入るのをのを待つ
                    notEmpty.await();
                } catch (InterruptedException e) {
                    return null;
                }
            }
            return super.peek();
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * このキューの先頭を取得および削除する。このキューに要素が存在しない場合は、必要に応じて指定された時間だけ待機する。
     * <p>
     * 拡張仕様：<b> キューイングの終了が通知された後、キューが空の場合は、タイムアウトを待たずに null を返す。
     * </p>
     * <p>
     * キューイングの終了が通知される前の仕様は、 {@link ArrayBlockingQueue#poll(long, TimeUnit)}と同じ。
     * </p>
     * @param timeout 処理を中止するまでの待機時間。単位は unit
     * @param unit timeout パラメータの解釈方法を指定する TimeUnit
     * @return このキューの先頭。指定された待機時間が経過、あるいはキューイングの終了が通知された後も要素が存在しない場合は null
     * @throws InterruptedException 待機中に割り込みが発生した場合
     */
    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        queueLock.lock();
        try {
            while (!finishQueueingFlag && size() == 0) {

                // キューに要素が入るのをのを待つ
                nanos = notEmpty.awaitNanos(nanos);
                if (nanos <= 0) {

                    // タイムアウト
                    return null;
                }
            }
            if (finishQueueingFlag && size() == 0) {
                // キューイングの終了が通知された後、かつ、キューが空
                return null;
            }
            E elm = super.poll(timeout, unit);
            if (elm != null) {

                // キューの空き待ちを行っているスレッドのブロックを解除する
                notFull.signal();
            }
            return elm;
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * このキューの先頭を取得および削除する。
     * @return このキューの先頭。要素が存在しない場合は null
     */
    @Override
    public E poll() {
        queueLock.lock();
        try {
            E elm = super.poll();
            if (elm != null) {

                // キューの空き待ちを行っているスレッドのブロックを解除する
                notFull.signal();
            }
            return elm;
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * キューに要素がない場合に true を返す。
     * <p>
     * 拡張仕様：<b> キューが空の場合は、キューに要素が入るか、キューイングの終了が通知されるまで待つ。<br>
     * キューイングの終了が通知された後、キューが空の場合は true を返す。
     * </p>
     */
    @Override
    public boolean isEmpty() {
        queueLock.lock();
        try {
            while (!finishQueueingFlag && size() == 0) {
                try {

                    // キューに要素が入るのをのを待つ
                    notEmpty.await();
                } catch (InterruptedException e) {
                    return true;
                }
            }
            return super.isEmpty();
        } finally {
            queueLock.unlock();
        }
    }

}
