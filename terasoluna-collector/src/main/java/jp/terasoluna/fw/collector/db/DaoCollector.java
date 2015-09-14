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

package jp.terasoluna.fw.collector.db;

import jp.terasoluna.fw.collector.AbstractCollector;
import jp.terasoluna.fw.collector.LogId;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.vo.DataValueObject;
import jp.terasoluna.fw.exception.SystemException;
import jp.terasoluna.fw.logger.TLogger;

import org.apache.ibatis.session.ResultHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * DaoCollector<br>
 * 独立した別スレッドを起動し、QueryResultHandleDaoを非同期で実行する。
 * @param &ltP&gt
 */
public class DaoCollector<P> extends AbstractCollector<P> {

    /**
     * Log.
     */
    private static final TLogger LOGGER = TLogger.getLogger(DaoCollector.class);

    /** queryResultHandleDao */
    protected Object queryResultHandleDao = null;

    /** SQLにバインドする値を格納したオブジェクト */
    protected Object bindParams = null;

    /** 行単位データアクセスの呼び出しで使用されるDaoのメソッド名 */
    protected String methodName = null;

    /** QueueingResultHandlerインスタンス */
    protected QueueingResultHandler resultHandler = null;

    /** QueueingResultHandlerのクラス型 */
    protected Class<? extends QueueingResultHandler> queueingResultHandlerClass = QueueingResultHandlerImpl.class;

    /** DaoCollector前後処理 */
    protected DaoCollectorPrePostProcess daoCollectorPrePostProcess = null;

    /**
     * DaoCollectorコンストラクタ<br>
     */
    protected DaoCollector() {
    }

    /**
     * DaoCollectorコンストラクタ<br>
     * @param queryResultHandleDao QueryResultHandleDaoインスタンス
     * @param methodName 実行するDaoのメソッド名
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     */
    public DaoCollector(Object queryResultHandleDao, String methodName,
            Object bindParams) {
        this(new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams));
    }

    /**
     * DaoCollectorコンストラクタ<br>
     * @param queryResultHandleDao QueryResultHandleDaoインスタンス
     * @param methodName 実行するDaoのメソッド名
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param relation1n 1:Nマッピング使用時はtrue
     * @deprecated
     */
    @Deprecated
    public DaoCollector(Object queryResultHandleDao, String methodName,
            Object bindParams, boolean relation1n) {

        this(new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams)
                .addRelation1n(relation1n));
    }

    /**
     * DaoCollectorコンストラクタ<br>
     * @param queryResultHandleDao QueryResultHandleDaoインスタンス
     * @param methodName 実行するDaoのメソッド名
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param queueSize キューのサイズ（1以上を設定すること。0以下は無視）
     */
    public DaoCollector(Object queryResultHandleDao, String methodName,
            Object bindParams, int queueSize) {
        this(new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams)
                .addQueueSize(queueSize));
    }

    /**
     * DaoCollectorコンストラクタ<br>
     * @param queryResultHandleDao QueryResultHandleDaoインスタンス
     * @param methodName 実行するDaoのメソッド名
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param exceptionHandler 例外ハンドラ
     */
    public DaoCollector(Object queryResultHandleDao, String methodName,
            Object bindParams, CollectorExceptionHandler exceptionHandler) {
        this(new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams)
                .addExceptionHandler(exceptionHandler));
    }

    /**
     * DaoCollectorコンストラクタ<br>
     * @param queryResultHandleDao QueryResultHandleDaoインスタンス
     * @param methodName 実行するDaoのメソッド名
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param queueSize キューのサイズ（1以上を設定すること。0以下は無視）
     * @param relation1n 1:Nマッピング使用時はtrue
     * @param exceptionHandler 例外ハンドラ
     * @param daoCollectorPrePostProcess DaoCollector前後処理
     * @deprecated
     */
    @Deprecated
    public DaoCollector(Object queryResultHandleDao, String methodName,
            Object bindParams, int queueSize, boolean relation1n,
            CollectorExceptionHandler exceptionHandler,
            DaoCollectorPrePostProcess daoCollectorPrePostProcess) {
        this(new DaoCollectorConfig(queryResultHandleDao, methodName, bindParams)
                .addQueueSize(queueSize).addRelation1n(relation1n)
                .addExceptionHandler(exceptionHandler)
                .addDaoCollectorPrePostProcess(daoCollectorPrePostProcess));
    }

    /**
     * DaoCollectorコンストラクタ<br>
     * @param config DaoCollectorConfig DaoCollector設定項目
     */
    @SuppressWarnings("deprecation")
    public DaoCollector(DaoCollectorConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("The parameter is null.");
        }

        this.queryResultHandleDao = config.getQueryResultHandleDao();
        this.methodName = config.getMethodName();
        this.bindParams = config.getBindParams();
        if (config.getQueueSize() > 0) {
            setQueueSize(config.getQueueSize());
        }
        if (config.isRelation1n()) {
            this.queueingResultHandlerClass = Queueing1NRelationResultHandlerImpl.class;
        }
        this.exceptionHandler = config.getExceptionHandler();
        this.daoCollectorPrePostProcess = config
                .getDaoCollectorPrePostProcess();

        if (config.isExecuteByConstructor()) {
            // 実行開始
            execute();
        }
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.Callable#call()
     */
    public Integer call() throws Exception {
        try {
            DaoCollectorPrePostProcessStatus expStatus = null;
            do {
                try {
                    // SQL実行前処理
                    preprocess();

                    Class<?> queryResultHandleDaoClazz = this.queryResultHandleDao
                            .getClass();
                    Method collectMethod = queryResultHandleDaoClazz.getMethod(
                            this.methodName, Object.class, ResultHandler.class);

                    try {
                        // QueryResultHandleDAO 実行
                        collectMethod.invoke(this.queryResultHandleDao,
                                this.bindParams, this.resultHandler);
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }

                    this.resultHandler.delayCollect();

                } catch (Throwable th) {
                    // SQL実行後処理（例外）
                    expStatus = postprocessException(th);

                    // ステータス判定
                    if (expStatus == null
                            || DaoCollectorPrePostProcessStatus.THROW.equals(
                                    expStatus)) {
                        // 例外をスロー
                        handleException(th);
                        return -1;
                    } else if (DaoCollectorPrePostProcessStatus.END.equals(
                            expStatus)) {
                        // 例外をスローせずに終了
                        break;
                    }
                } finally {
                    // SQL実行後処理
                    postprocessComplete();
                }

                // ステータスがリトライなら再度SQLを実行する
            } while (expStatus != null && DaoCollectorPrePostProcessStatus.RETRY
                    .equals(expStatus));
        } catch (Exception e) {
            handleException(e);
            return -1;
        } finally {
            setFinish();
        }

        return 0;
    }

    /**
     * コレクタの行処理と割り込みでThrowableがスローされた場合の エンキューを行う。
     * @param th Throwable
     */
    protected void handleException(Throwable th) {
        // シャットダウン中は発生した例外をキューに詰めない
        if (!isFinish()) {
            // 発生した例外をキューにつめる
            try {
                addQueue(new DataValueObject(th));
            } catch (InterruptedException ie) {
                LOGGER.warn(LogId.WAL041003, th);
                LOGGER.warn(LogId.WAL041003, ie);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(LogId.DAL041002, th.getClass().getName(), Thread
                        .currentThread().getName());
            }
        }
    }

    /**
     * SQL実行前処理
     */
    protected void preprocess() {
        if (this.daoCollectorPrePostProcess != null) {
            this.daoCollectorPrePostProcess.preprocess(this);
        }
    }

    /**
     * SQL実行後処理（例外）
     * @param th Throwable
     * @return DaoCollectorPrePostProcessStatus
     */
    protected DaoCollectorPrePostProcessStatus postprocessException(
            Throwable th) {
        DaoCollectorPrePostProcessStatus expStatus = null;
        if (this.daoCollectorPrePostProcess != null) {
            expStatus = this.daoCollectorPrePostProcess.postprocessException(
                    this, th);
        }
        return expStatus;
    }

    /**
     * SQL実行後処理
     */
    protected void postprocessComplete() {
        if (this.daoCollectorPrePostProcess != null) {
            this.daoCollectorPrePostProcess.postprocessComplete(this);
        }
    }

    /**
     * getResultHandlerメソッド.
     * @return QueueingResultHandler
     */
    protected QueueingResultHandler getResultHandler() {
        QueueingResultHandler resultHandler = null;

        try {
            resultHandler = this.queueingResultHandlerClass.newInstance();
        } catch (InstantiationException e) {
            SystemException exception = new SystemException(e);
            exception.setMessage("Generation of an instance goes wrong.");
            throw exception;
        } catch (IllegalAccessException e) {
            SystemException exception = new SystemException(e);
            exception.setMessage("Generation of an instance goes wrong.");
            throw exception;
        }

        return resultHandler;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Object obj = super.clone();
        if (obj instanceof DaoCollector) {
            @SuppressWarnings("resource")
            DaoCollector<P> qac = (DaoCollector<P>) obj;
            qac.resultHandler = getResultHandler();
            qac.resultHandler.setDaoCollector(this);
        }
        
        return obj;
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.collector.AbstractCollector#addQueue(jp.terasoluna.fw.collector.vo.DataValueObject)
     */
    @Override
    protected void addQueue(
            DataValueObject dataValueObject) throws InterruptedException {
        super.addQueue(dataValueObject);
    }
}
