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

import java.lang.reflect.Method;

/**
 * DaoCollector<br>
 * 独立した別スレッドを起動し、QueryRowHandleDaoを非同期で実行する。
 * @param &ltP&gt
 */
public class DaoCollector<P> extends AbstractCollector<P> {

    /**
     * Log.
     */
    private static final TLogger LOGGER = TLogger.getLogger(DaoCollector.class);

    /** QueryRowHandleDao */
    protected Object queryRowHandleDao = null;

    /** SQLにバインドする値を格納したオブジェクト */
    protected Object bindParams = null;

    /** 行単位データアクセスの呼び出しで使用されるDaoのメソッド名 */
    protected String methodName = null;

    /** QueueingDataRowHandlerインスタンス */
    protected QueueingDataRowHandler rowHandler = null;

    /** QueueingDataRowHandlerのクラス型 */
    protected Class<? extends QueueingDataRowHandler> queueingDataRowHandlerClass = QueueingDataRowHandlerImpl.class;

    /** DaoCollector前後処理 */
    protected DaoCollectorPrePostProcess daoCollectorPrePostProcess = null;

    /**
     * DaoCollectorコンストラクタ<br>
     */
    protected DaoCollector() {
    }

    /**
     * DaoCollectorコンストラクタ<br>
     * @param queryRowHandleDao QueryRowHandleDaoインスタンス
     * @param methodName 実行するDaoのメソッド名
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     */
    public DaoCollector(Object queryRowHandleDao, String methodName,
            Object bindParams) {
        this(new DaoCollectorConfig(queryRowHandleDao, methodName, bindParams));
    }

    /**
     * DaoCollectorコンストラクタ<br>
     * @param queryRowHandleDao QueryRowHandleDaoインスタンス
     * @param methodName 実行するDaoのメソッド名
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param relation1n 1:Nマッピング使用時はtrue
     */
    public DaoCollector(Object queryRowHandleDao, String methodName,
            Object bindParams, boolean relation1n) {

        this(new DaoCollectorConfig(queryRowHandleDao, methodName, bindParams)
                .addRelation1n(relation1n));
    }

    /**
     * DaoCollectorコンストラクタ<br>
     * @param queryRowHandleDao QueryRowHandleDaoインスタンス
     * @param methodName 実行するDaoのメソッド名
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param queueSize キューのサイズ（1以上を設定すること。0以下は無視）
     */
    public DaoCollector(Object queryRowHandleDao, String methodName,
            Object bindParams, int queueSize) {
        this(new DaoCollectorConfig(queryRowHandleDao, methodName, bindParams)
                .addQueueSize(queueSize));
    }

    /**
     * DaoCollectorコンストラクタ<br>
     * @param queryRowHandleDao QueryRowHandleDaoインスタンス
     * @param methodName 実行するDaoのメソッド名
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param exceptionHandler 例外ハンドラ
     */
    public DaoCollector(Object queryRowHandleDao, String methodName,
            Object bindParams, CollectorExceptionHandler exceptionHandler) {
        this(new DaoCollectorConfig(queryRowHandleDao, methodName, bindParams)
                .addExceptionHandler(exceptionHandler));
    }

    /**
     * DaoCollectorコンストラクタ<br>
     * @param queryRowHandleDao QueryRowHandleDaoインスタンス
     * @param methodName 実行するDaoのメソッド名
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param queueSize キューのサイズ（1以上を設定すること。0以下は無視）
     * @param relation1n 1:Nマッピング使用時はtrue
     * @param exceptionHandler 例外ハンドラ
     * @param daoCollectorPrePostProcess DaoCollector前後処理
     */
    public DaoCollector(Object queryRowHandleDao, String methodName,
            Object bindParams, int queueSize, boolean relation1n,
            CollectorExceptionHandler exceptionHandler,
            DaoCollectorPrePostProcess daoCollectorPrePostProcess) {
        this(new DaoCollectorConfig(queryRowHandleDao, methodName, bindParams)
                .addQueueSize(queueSize).addRelation1n(relation1n)
                .addExceptionHandler(exceptionHandler)
                .addDaoCollectorPrePostProcess(daoCollectorPrePostProcess));
    }

    /**
     * DaoCollectorコンストラクタ<br>
     * @param config DaoCollectorConfig DaoCollector設定項目
     */
    public DaoCollector(DaoCollectorConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("The parameter is null.");
        }

        this.queryRowHandleDao = config.getQueryRowHandleDao();
        this.methodName = config.getMethodName();
        this.bindParams = config.getBindParams();
        if (config.getQueueSize() > 0) {
            setQueueSize(config.getQueueSize());
        }
        if (config.isRelation1n()) {
            this.queueingDataRowHandlerClass = Queueing1NRelationDataRowHandlerImpl.class;
        }
        this.exceptionHandler = config.getExceptionHandler();
        this.daoCollectorPrePostProcess = config.getDaoCollectorPrePostProcess();

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

                    Class<?> queryRowHandleDaoClazz = this.queryRowHandleDao.getClass();
                    Method collectMethod = queryRowHandleDaoClazz.getMethod(this.methodName,
                            Object.class, ResultHandler.class);

                    // QueryRowHandleDAO 実行
                    collectMethod.invoke(this.queryRowHandleDao, this.bindParams, this.rowHandler);

                    this.rowHandler.delayCollect();

                } catch (Throwable th) {
                    // SQL実行後処理（例外）
                    expStatus = postprocessException(th);

                    // ステータス判定
                    if (expStatus == null
                            || DaoCollectorPrePostProcessStatus.THROW
                                    .equals(expStatus)) {
                        // 例外をスロー
                        throw th;
                    } else if (DaoCollectorPrePostProcessStatus.END
                                    .equals(expStatus)) {
                        // 例外をスローせずに終了
                        break;
                    }
                } finally {
                    // SQL実行後処理
                    postprocessComplete();
                }

                // ステータスがリトライなら再度SQLを実行する
            } while (expStatus != null
                    && DaoCollectorPrePostProcessStatus.RETRY.equals(expStatus));
        } catch (Throwable e) {
            // シャットダウン中は発生した例外をキューに詰めない
            if (!isFinish()) {
                // 発生した例外をキューにつめる
                try {
                    addQueue(new DataValueObject(e));
                } catch (InterruptedException ie) {
                    LOGGER.warn(LogId.WAL041003, e);
                    LOGGER.warn(LogId.WAL041003, ie);
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(LogId.DAL041002, e.getClass().getName(),
                            Thread.currentThread().getName());
                }
            }

            return -1;
        } finally {
            setFinish();
        }

        return 0;
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
    protected DaoCollectorPrePostProcessStatus postprocessException(Throwable th) {
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
     * getDataRowHandlerメソッド.
     * @return QueueingDataRowHandler
     */
    protected QueueingDataRowHandler getDataRowHandler() {
        QueueingDataRowHandler dataRowHandler = null;

        try {
            dataRowHandler = this.queueingDataRowHandlerClass.newInstance();
        } catch (InstantiationException e) {
            SystemException exception = new SystemException(e);
            exception.setMessage("Generation of an instance goes wrong.");
            throw exception;
        } catch (IllegalAccessException e) {
            SystemException exception = new SystemException(e);
            exception.setMessage("Generation of an instance goes wrong.");
            throw exception;
        }

        return dataRowHandler;
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
            DaoCollector<P> qac = (DaoCollector<P>) obj;
            qac.rowHandler = getDataRowHandler();
            qac.rowHandler.setDaoCollector(this);
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.collector.AbstractCollector#addQueue(jp.terasoluna.fw.collector.vo.DataValueObject)
     */
    @Override
    protected void addQueue(DataValueObject dataValueObject)
                                                            throws InterruptedException {
        super.addQueue(dataValueObject);
    }
}
