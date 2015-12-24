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

package jp.terasoluna.fw.collector;

import java.beans.Introspector;
import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import jp.terasoluna.fw.collector.concurrent.ArrayBlockingQueueEx;
import jp.terasoluna.fw.collector.concurrent.NotificationBlockingQueue;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandlerStatus;
import jp.terasoluna.fw.collector.validate.ValidateErrorStatus;
import jp.terasoluna.fw.collector.validate.ValidationErrorHandler;
import jp.terasoluna.fw.collector.vo.CollectorStatus;
import jp.terasoluna.fw.collector.vo.DataValueObject;
import jp.terasoluna.fw.exception.SystemException;
import jp.terasoluna.fw.logger.TLogger;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * AbstractCollector抽象クラス
 * @param &lt;P&gt;
 */
public abstract class AbstractCollector<P> implements Collector<P>, Closeable,
                                           Callable<Integer>, Cloneable {
    /**
     * Log.
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(AbstractCollector.class);

    /** デフォルトのキューサイズ */
    public static final int DEFAULT_QUEUE_SIZE = 20;

    /** デフォルトスリープ時間(msec) */
    protected static final int DEFAULT_SLEEP_WAIT = 1;

    /** 現在キュー保持数チェックサイズ */
    protected static final int CURRENT_QUEUE_CHECK_SIZE = 1;

    /** 後方キュー保持数チェックサイズ */
    protected static final int PREVIOUS_QUEUE_CHECK_SIZE = 2;

    /** 冗長ログ出力フラグ. */
    protected static AtomicBoolean verboseLog = new AtomicBoolean(false);

    /** キューサイズ */
    protected int queueSize = DEFAULT_QUEUE_SIZE;

    /** スリープ時間(msec) */
    protected int sleepWait = DEFAULT_SLEEP_WAIT;

    /** キュー */
    protected BlockingQueue<DataValueObject> queue = null;

    /** 現在キュー */
    protected Queue<DataValueObject> currentQueue = null;

    /** 後方キュー */
    protected Queue<DataValueObject> previousQueue = null;

    /** 非同期処理の結果を取得するためのクラス */
    protected volatile Future<?> fo = null;

    /** 終了フラグ */
    protected volatile boolean finish = false;

    /** 実行開始フラグ */
    protected volatile boolean beginning = false;

    /** Validator */
    protected Validator validator = null;

    /** ValidationErrorHandler */
    protected ValidationErrorHandler validationErrorHandler = null;

    /** CollectorExceptionHandler */
    protected CollectorExceptionHandler exceptionHandler = null;

    /** 子スレッド側インスタンス */
    protected AbstractCollector<?> child = null;

    /**
     * AbstractCollectorを実行する。
     */
    @SuppressWarnings("unchecked")
    protected void execute() {
        if (this.beginning) {
            return;
        }
        synchronized (this) {
            if (!this.beginning) {
                try {
                    // 実行前処理
                    beforeExecute();

                    if (this.queue == null) {
                        // キュー生成
                        this.queue = createQueue();
                    }

                    if (this.fo == null) {
                        // 自分自身のクローンを作成
                        Callable<Integer> callable = null;
                        try {
                            callable = (Callable<Integer>) this.clone();
                        } catch (CloneNotSupportedException e) {
                            SystemException exception = new SystemException(e);
                            exception.setMessage("The clone cannot be made.");
                            throw exception;
                        }

                        if (callable instanceof AbstractCollector) {
                            this.child = (AbstractCollector<P>) callable;
                        }

                        // ExecutorService取得
                        ExecutorService ex = getExecutor();

                        try {
                            // 別スレッドで実行
                            this.fo = ex.submit(callable);
                        } catch (Exception e) {
                            SystemException exception = new SystemException(e);
                            exception
                                    .setMessage("The thread cannot be started.");
                            throw exception;
                        } finally {
                            ex.shutdown();
                        }
                    }
                } finally {
                    // 実行後処理
                    afterExecute();
                }

                // 実行開始フラグを立てる
                this.beginning = true;
            }
        }
    }

    /**
     * 実行前処理。<br>
     * 子スレッド(コレクタスレッド)を起動する前に実行される。
     */
    protected void beforeExecute() {
    }

    /**
     * 実行後処理。<br>
     * 子スレッド(コレクタスレッド)を起動した後に実行される。
     * 子スレッドの終了は待たず、子スレッドの起動が完了した後に実行される。
     * 子スレッドの起動に失敗した場合も、実行される。
     */
    protected void afterExecute() {
    }

    /**
     * 繰り返し処理でさらに要素がある場合に true を返します。<br>
     * つまり、next の呼び出しが例外をスローすることなく要素を返す場合は、true を返します。
     * <p>
     * <b>※本メソッドはマルチスレッドセーフでありません。</b>
     * </p>
     * @return 反復子がさらに要素を持つ場合は true
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return (getNextObject() != null);
    }

    /**
     * 繰り返し処理で次の要素を返します。
     * <p>
     * <b>※本メソッドはマルチスレッドセーフでありません。</b>
     * </p>
     * @return 繰り返し処理で次の要素
     * @throws NoSuchElementException 繰り返し処理でそれ以上要素がない場合
     * @see java.util.Iterator#next()
     */
    @SuppressWarnings("unchecked")
    public P next() {
        // 実行開始（初回のみ）
        execute();

        DataValueObject nextValue = getNextObject();
        if (nextValue != null) {
            if (this.previousQueue != null) {
                while (this.previousQueue.size() > PREVIOUS_QUEUE_CHECK_SIZE) {
                    this.previousQueue.remove();
                }
                this.previousQueue.add(nextValue);
            }
            if (this.currentQueue != null) {
                while (this.currentQueue.size() > CURRENT_QUEUE_CHECK_SIZE) {
                    this.currentQueue.remove();
                }
                this.currentQueue.add(nextValue);
            }
        } else {
            if (verboseLog.get() && LOGGER.isTraceEnabled()) {
                LOGGER.trace(LogId.TAL041007, this.queue.size());
            }
            setFinish(true);
            close();
            throw new NoSuchElementException();
        }

        // キューから1件データを取得する
        try {
            this.queue.poll(this.sleepWait, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.warn(LogId.WAL041003, e);
        }

        if (nextValue.getThrowable() != null) {
            Throwable throwable = nextValue.getThrowable();
            // 例外をスローする
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            } else {
                throw new SystemException(throwable);
            }
        }
        return (P) nextValue.getValue();
    }

    /**
     * ポインタを次の要素に移さずに次の要素を返します。<br>
     * <p>
     * nullの場合は次の要素が存在しないことを示します。<br>
     * ポインタは移動しません。
     * </p>
     * <p>
     * 該当データの取得時に例外が発生した場合、以下条件により異なる例外がスローされます。
     * <ul>
     * <li>ランタイム例外発生時：RuntimeExceptionをそのままスローする</li>
     * <li>その他例外発生時：SystemExceptionでラップしてスローする</li>
     * </ul>
     * </p>
     * <p>入力チェックに失敗した場合にnext()メソッドで該当データにアクセスすると
     * 例外が発生するケースでも、本メソッドでは該当データが取得できます。</p>
     * </p>
     * <b>※本メソッドはマルチスレッドセーフでありません。</b>
     * </p>
     * @return &lt;P&gt;
     * @see jp.terasoluna.fw.collector.Collector#getNext()
     */
    @SuppressWarnings("unchecked")
    public P getNext() {
        DataValueObject value = getNextObject();

        if (value == null) {
            return null;
        } else if (value.getValue() == null) {
            // 例外発生時はスローする
            if (value.getThrowable() != null) {
                Throwable throwable = value.getThrowable();
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException) throwable;
                } else {
                    throw new SystemException(throwable);
                }
            }
            return null;
        }

        return (P) value.getValue();
    }

    /**
     * ポインタを次の要素に移さずに次のQueue要素を返します。<br>
     * <p>
     * nullの場合は次の要素が存在しないことを示します。<br>
     * ポインタは移動しません。
     * </p>
     * <p>当該データ取得によるステータスが返却された場合に以下の挙動となります。
     * <table border="1">
     * <tr><th>当該データ取得によるステータス</th><th>getNextObject()の挙動</th></tr>
     * <tr>
     * <td>CollectorExceptionHandlerStatus.SKIP</td><td>当該データは一件読み捨てられ、次のデータを取得する。</td>
     * </tr>
     * <tr>
     * <td>ValidateErrorStatus.END</td><td>データ終端としてnullを返却する。</td>
     * </tr>
     * <tr>
     * <td>CollectorExceptionHandlerStatus.END</td><td>データ終端としてnullを返却する。</td>
     * </tr>
     * <tr>
     * <td>CollectorStatus.END</td><td>データ終端としてnullを返却する。</td>
     * </tr>
     * </table>
     * </p>
     * <p>
     * 取得された当該データのgetThrowable()がnullではない場合、例外ハンドラを実行し上記ステータスの判定を行っている。
     * よって、本メソッドから返却されたオブジェクトのgetThrowable()がnullではない場合、例外ハンドラの結果はnull
     * あるいはCollectorExceptionHandlerStatus.THROWとなる。
     * </p>
     * <p>
     * <b>※本メソッドはマルチスレッドセーフでありません。</b>
     * </p>
     * @return 次に取得されるデータのDataValueObject（次がない場合はnullが返却される）
     */
    protected DataValueObject getNextObject() {
        // 実行開始（初回のみ）
        execute();

        DataValueObject value = null;
        do {
            // キューから1件データを取得する（削除しない）
            if (this.queue != null) {
                value = this.queue.peek();
            }

            // 終了フラグを検査
            if (isFinish() && this.queue.isEmpty()) {
                if (verboseLog.get() && LOGGER.isTraceEnabled()) {
                    LOGGER.trace(LogId.TAL041014);
                }
                break;
            }

            if (value != null && value.getValidateStatus() != null) {
                ValidateErrorStatus validateStatus = value
                        .getValidateStatus();
                if (ValidateErrorStatus.END.equals(validateStatus)) {
                    return null;
                }
            }

            CollectorExceptionHandlerStatus es = null;

            if (value != null && value.getThrowable() != null) {
                try {
                    // 例外ハンドラを実行する
                    es = handleException(value);
                } catch (Throwable e) {
                    LOGGER.warn(LogId.WAL041004, e);
                    // ここでの例外はログに残すのみで何もしない
                }
                if (es == null || CollectorExceptionHandlerStatus.THROW.equals(es)) {
                    break;
                } else if (CollectorExceptionHandlerStatus.SKIP.equals(es)) {
                    // ステータスがSKIPの場合、キューから1件読み捨ててループを継続させる。
                    this.queue.poll();
                    value = null;
                    continue;
                } else if (CollectorExceptionHandlerStatus.END.equals(es)) {
                    // ループを抜けてnullを返却する。
                    return null;
                }
            }

            if (value != null && CollectorStatus.END.equals(value.getCollectorStatus())) {
                setFinish(true);
                return null;
            }

            // nullの場合はスリープする
            if (value == null) {
                try {
                    if (verboseLog.get() && LOGGER.isTraceEnabled()) {
                        LOGGER.trace(LogId.TAL041019, this.sleepWait);
                    }
                    // sleepWait ms待つ
                    Thread.sleep(this.sleepWait);

                } catch (InterruptedException e) {
                    LOGGER.warn(LogId.WAL041003, e);
                    break;
                }
                if (verboseLog.get() && LOGGER.isTraceEnabled()) {
                    LOGGER.trace(LogId.TAL041008, this.queue.size());
                }
            }
        } while (value == null);

        return value;
    }

    /**
     * 1件前の要素を返します。<br>
     * <p>
     * 1件目の場合はnullが返ります。<br>
     * ポインタは移動しません。
     * </p>
     * <p>
     * 該当データの取得時に例外が発生した場合、以下条件により異なる例外がスローされます。
     * <ul>
     * <li>ランタイム例外発生時：RuntimeExceptionをそのままスローする</li>
     * <li>その他例外発生時：SystemExceptionでラップしてスローする</li>
     * </ul>
     * </p>
     * <p>入力チェックに失敗した場合にnext()メソッドで該当データにアクセスすると
     * 例外が発生するケースでも、本メソッドでは該当データが取得できます。</p>
     * </p>
     * <p>
     * <b>※本メソッドはマルチスレッドセーフでありません。</b>
     * </p>
     * @return &lt;P&gt;
     * @see jp.terasoluna.fw.collector.Collector#getPrevious()
     */
    @SuppressWarnings("unchecked")
    public P getPrevious() {
        DataValueObject value = getPreviousObject();

        if (value == null) {
            return null;
        } else if (value.getValue() == null) {
            // 例外発生時はスローする
            if (value.getThrowable() != null) {
                Throwable throwable = value.getThrowable();
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException) throwable;
                } else {
                    throw new SystemException(throwable);
                }
            }
            return null;
        }

        return (P) value.getValue();
    }

    /**
     * 1件前のQueue要素を返します。<br>
     * <p>
     * 1件目の場合はnullが返ります。<br>
     * ポインタは移動しません。
     * </p>
     * <p>
     * <b>※本メソッドはマルチスレッドセーフでありません。</b>
     * </p>
     * @return next()により取得されたデータのひとつ前のデータのDataValueObject
     */
    protected DataValueObject getPreviousObject() {
        // 実行開始（初回のみ）
        execute();

        DataValueObject value = null;
        if (this.previousQueue != null && this.previousQueue.size() > 1) {
            while (this.previousQueue.size() > PREVIOUS_QUEUE_CHECK_SIZE) {
                this.previousQueue.remove();
            }

            value = this.previousQueue.peek();
        }

        return value;
    }

    /**
     * 現在の要素を返します。<br>
     * <p>
     * nullの場合は現在の要素が存在しないことを示します。<br>
     * ポインタは移動しません。
     * </p>
     * <p>
     * 該当データの取得時に例外が発生した場合、以下条件により異なる例外がスローされます。
     * <ul>
     * <li>ランタイム例外発生時：RuntimeExceptionをそのままスローする</li>
     * <li>その他例外発生時：SystemExceptionでラップしてスローする</li>
     * </ul>
     * </p>
     * <p>入力チェックに失敗した場合にnext()メソッドで該当データにアクセスすると
     * 例外が発生するケースでも、本メソッドでは該当データが取得できます。</p>
     * </p>
     * <p>
     * <b>※本メソッドはマルチスレッドセーフでありません。</b>
     * </p>
     * @return &lt;P&gt;
     * @see jp.terasoluna.fw.collector.Collector#getCurrent()
     */
    @SuppressWarnings("unchecked")
    public P getCurrent() {
        // 実行開始（初回のみ）
        execute();

        DataValueObject value = getCurrentObject();

        if (value == null) {
            return null;
        } else if (value.getValue() == null) {
            // 例外発生時はスローする
            if (value.getThrowable() != null) {
                Throwable throwable = value.getThrowable();
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException) throwable;
                } else {
                    throw new SystemException(throwable);
                }
            }
            return null;
        }

        return (P) value.getValue();
    }

    /**
     * 現在のQueue要素を返します。<br>
     * <p>
     * nullの場合は現在の要素が存在しないことを示します。<br>
     * ポインタは移動しません。
     * </p>
     * <p>
     * <b>※本メソッドはマルチスレッドセーフでありません。</b>
     * </p>
     * @return next()により直近で取得されたデータのDataValueObject
     */
    protected DataValueObject getCurrentObject() {
        // 実行開始（初回のみ）
        execute();

        DataValueObject value = null;

        if (this.currentQueue != null && this.currentQueue.size() > 0) {
            while (this.currentQueue.size() > CURRENT_QUEUE_CHECK_SIZE) {
                this.currentQueue.remove();
            }
            value = this.currentQueue.peek();
        }

        return value;
    }

    /**
     * このストリームを閉じて、それに関連するすべてのシステムリソースを解放します。<br>
     * ストリームがすでに閉じられている場合は、このメソッドを呼び出しても何の効果もありません。
     * <p>
     * <b>※本メソッドはマルチスレッドセーフでありません。</b>
     * </p>
     * @see java.io.Closeable#close()
     */
    public void close() {
        if (!isFinish()) {
            if (this.fo != null) {
                this.fo.cancel(true);
            }
        }
    }

    /**
     * 基になるコレクションから、反復子によって最後に返された要素を削除します (任意のオペレーション)。<br>
     * このメソッドは、next の呼び出しごとに 1 回だけ呼び出すことができます。反復子の動作は、<br>
     * 繰り返し処理がこのメソッドの呼び出し以外の方法で実行されているときに基になるコレクションが変更された場合は保証されません。
     * <p>
     * <b>※本メソッドはサポートされていません。</b>
     * </p>
     * @throws UnsupportedOperationException Iterator が remove オペレーションをサポートしない場合
     * @throws IllegalStateException next メソッドがまだ呼び出されてない場合、または next メソッドの最後の呼び出しのあとに remove メソッドがすでに呼び出されている場合
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<P> iterator() {
        return this;
    }

    /**
     * getExecutorメソッド.
     * @return ExecutorService
     */
    protected ExecutorService getExecutor() {
        // 新しいエグゼキュータを返却
        return Executors.newSingleThreadExecutor(createThreadFactory());
    }

    /**
     * スレッドファクトリを生成する.
     * @return スレッドファクトリ
     */
    protected ThreadFactory createThreadFactory() {
        return new CollectorThreadFactory();
    }

    /**
     * キューを作成する
     * @return
     */
    protected BlockingQueue<DataValueObject> createQueue() {
        if (this.currentQueue == null) {
            // currentキュー生成
            this.currentQueue = createCurrentQueue();
        }
        if (this.previousQueue == null) {
            // previousキュー生成
            this.previousQueue = createPreviousQueue();
        }
        return new ArrayBlockingQueueEx<DataValueObject>(this.queueSize);
    }

    /**
     * currentキューを作成する
     * @return Queue&lt;DataValueObject&gt;
     */
    protected Queue<DataValueObject> createCurrentQueue() {
        return new ConcurrentLinkedQueue<DataValueObject>();
    }

    /**
     * previousキューを作成する
     * @return Queue&lt;DataValueObject&gt;
     */
    protected Queue<DataValueObject> createPreviousQueue() {
        return new ConcurrentLinkedQueue<DataValueObject>();
    }

    /**
     * キューを取得する。
     * @return Queue&lt;DataValueObject&gt;
     */
    protected Queue<DataValueObject> getQueue() {
        return this.queue;
    }

    /**
     * キューサイズを指定する。<br>
     * @param queueSize int
     */
    protected void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    /**
     * スリープ時間(msec)を取得する<br>
     * @return スリープ時間(msec)
     */
    protected int getSleepWait() {
        return sleepWait;
    }

    /**
     * スリープ時間(msec)を設定する<br>
     * @param sleepWait スリープ時間(msec)
     */
    protected void setSleepWait(int sleepWait) {
        this.sleepWait = sleepWait;
    }

    /**
     * キューにデータを追加する。
     * @param dataValueObject DataValueObject
     * @throws InterruptedException
     */
    protected void addQueue(DataValueObject dataValueObject)
                                                            throws InterruptedException {
        addQueue(dataValueObject, false);
    }

    /**
     * キューにデータを追加する。
     * @param dataValueObject DataValueObject
     * @param force boolean 強制キューイングフラグ
     * @throws InterruptedException
     */
    protected void addQueue(DataValueObject dataValueObject, boolean force)
                                                                           throws InterruptedException {
        if (force && this.queue != null) {
            this.queue.offer(dataValueObject);
            return;
        }

        boolean finish = isFinish();

        if (!finish) {
            // 入力チェック
            ValidateErrorStatus vs = null;
            if (this.validator != null) {
                try {
                    vs = validate(dataValueObject);
                } catch (Exception e) {
                    // 取得したデータに発生した例外を設定し1件キューにつめる
                    if (dataValueObject == null) {
                        this.queue.put(new DataValueObject(e));
                    } else {
                        dataValueObject.setThrowable(e);
                        this.queue.put(dataValueObject);
                    }
                    return;
                }
            }

            if (vs == null || ValidateErrorStatus.CONTINUE.equals(vs)) {
                // 取得したデータを1件キューにつめる
                this.queue.put(dataValueObject);
            } else if (ValidateErrorStatus.END.equals(vs)) {
                DataValueObject errorStop = new DataValueObject(vs);
                this.queue.put(errorStop);
                // 強制停止（以降のキューイングを強制停止）
                setFinish(true);
            } else if (ValidateErrorStatus.SKIP.equals(vs)) {
            	// スキップはキューにつめない
            }
        } else {
            if (LOGGER.isTraceEnabled()) {
                long dc = -1;
                if (dataValueObject != null) {
                    dc = dataValueObject.getDataCount();
                }
                LOGGER.trace(LogId.TAL041013, finish, "", dc);
            }
            throw new InterruptedException(
                    "The stop demand of the thread is carried out.");
        }
    }

    /**
     * 入力チェックを行う.<br>
     * @param dataValueObject DataValueObject
     * @return ValidateStatus
     */
    protected ValidateErrorStatus validate(DataValueObject dataValueObject) {
        ValidateErrorStatus vs = ValidateErrorStatus.CONTINUE;

        if (this.validator != null) {
            Class<?> clazz = null;
            String objectName = null;
            Errors errors = null;

            // 入力オブジェクトのクラス型を取得
            if (dataValueObject != null && dataValueObject.getValue() != null) {
                clazz = dataValueObject.getValue().getClass();

                if (clazz != null) {
                    objectName = clazz.getSimpleName();
                    if (objectName != null) {
                        objectName = Introspector.decapitalize(objectName);
                        // Errorsオブジェクト生成
                        errors = new BindException(dataValueObject.getValue(),
                                objectName);
                    }
                }
            }

            if (clazz != null && errors != null
                    && this.validator.supports(clazz)) {
                // 入力チェック
                this.validator.validate(dataValueObject.getValue(), errors);

                if (errors.hasErrors()) {
                    vs = handleValidationError(dataValueObject, errors);
                }
            }
        }

        return vs;
    }

    /**
     * 入力チェックエラー時の処理.<br>
     * @param dataValueObject DataValueObject
     * @param errors Errors
     * @return ValidateErrorStatus
     */
    protected ValidateErrorStatus handleValidationError(
            DataValueObject dataValueObject, Errors errors) {

        if (this.validationErrorHandler != null) {
            return this.validationErrorHandler.handleValidationError(
                    dataValueObject, errors);
        }

        return ValidateErrorStatus.SKIP;
    }

    /**
     * 例外発生時の処理
     * @param dataValueObject DataValueObject
     * @return CollectorExceptionHandlerStatus
     */
    protected CollectorExceptionHandlerStatus handleException(
            DataValueObject dataValueObject) {
        CollectorExceptionHandlerStatus result = dataValueObject.getExceptionHandlerStatus();
        // 既に判定済みならばそのときの結果を返す。
        if (result != null) {
            return result;
        }

        if (this.exceptionHandler != null) {
            result = this.exceptionHandler.handleException(dataValueObject);
            dataValueObject.setExceptionHandlerStatus(result);
        }
        return result;
    }

    /**
     * 終了フラグの状態を確認する。
     * @return boolean
     */
    protected boolean isFinish() {
        boolean finish = this.finish;
        AbstractCollector<?> localChild = this.child;
        Future<?> future = this.fo;

        if (future != null) {
            boolean done = future.isDone();

            if (localChild != null) {
                // 子スレッド側の終了フラグを参照する
                if (localChild.isFinish()) {
                    finish = localChild.isFinish();
                }
            }
            return finish || done;
        }

        if (localChild != null) {
            // 子スレッド側の終了フラグを参照する
            if (localChild.isFinish()) {
                finish = localChild.isFinish();
            }
        }
        return finish;
    }

    /**
     * 終了フラグを設定する。
     */
    protected void setFinish() {
        if (verboseLog.get() && LOGGER.isTraceEnabled()) {
            LOGGER.trace(LogId.TAL041012, Thread.currentThread().getName());
        }
        setFinish(true);

        // 終了フラグをキューにつめる
        try {
            addQueue(new DataValueObject(CollectorStatus.END), true);
        } catch (InterruptedException ie) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(LogId.TAL041012, ie, Thread.currentThread()
                        .getName());
            }
        }

        // キューに対しキューイングの終了を通知する。
        if (queue instanceof NotificationBlockingQueue) {
            ((NotificationBlockingQueue<DataValueObject>) queue)
                    .finishQueueing();
        }
    }

    /**
     * 終了フラグを設定する。
     * @param finish
     */
    protected void setFinish(boolean finish) {
        this.finish = finish;
    }

    /**
     * Collectorをクローズする。<br>
     * <p>
     * 引数に渡されたcollectorがnullでなければクローズする。<br>
     * また、クローズする際にIO例外が発生した場合は無視する。<br>
     * </p>
     * @param collector Collector
     */
    public static void closeQuietly(Collector<?> collector) {
        try {
            if (collector != null) {
                collector.close();
            }
        } catch (IOException e) {
            // なにもしない
        }
    }

    /**
     * 冗長ログ出力フラグを設定する。
     * @param verbose 冗長ログ出力フラグ
     */
    public static void setVerbose(boolean verbose) {
        verboseLog.set(verbose);
    }
}
