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
import jp.terasoluna.fw.dao.QueryRowHandleDAO;
import jp.terasoluna.fw.exception.SystemException;
import jp.terasoluna.fw.logger.TLogger;

/**
 * DBCollector<br>
 * 独立した別スレッドを起動し、QueryRowHandleDAOを非同期で実行する。
 * @param &ltP&gt
 */
public class DBCollector<P> extends AbstractCollector<P> {

    /**
     * Log.
     */
    private static final TLogger LOGGER = TLogger.getLogger(DBCollector.class);

    /** QueryRowHandleDAO */
    protected QueryRowHandleDAO queryRowHandleDAO = null;

    /** 実行するSQLのID */
    protected String sqlID = null;

    /** SQLにバインドする値を格納したオブジェクト */
    protected Object bindParams = null;

    /** QueueingDataRowHandlerインスタンス */
    protected QueueingDataRowHandler rowHandler = null;

    /** QueueingDataRowHandlerのクラス型 */
    protected Class<? extends QueueingDataRowHandler> queueingDataRowHandlerClass = QueueingDataRowHandlerImpl.class;

    /** DBCollector前後処理 */
    protected DBCollectorPrePostProcess dbCollectorPrePostProcess = null;

    /**
     * DBCollectorコンストラクタ<br>
     */
    protected DBCollector() {
    }

    /**
     * DBCollectorコンストラクタ<br>
     * @param queryRowHandleDAO QueryRowHandleDAOインスタンス
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     */
    public DBCollector(QueryRowHandleDAO queryRowHandleDAO, String sqlID,
            Object bindParams) {
        this(new DBCollectorConfig(queryRowHandleDAO, sqlID, bindParams));
    }

    /**
     * DBCollectorコンストラクタ<br>
     * @param queryRowHandleDAO QueryRowHandleDAOインスタンス
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param relation1n 1:Nマッピング使用時はtrue
     */
    public DBCollector(QueryRowHandleDAO queryRowHandleDAO, String sqlID,
            Object bindParams, boolean relation1n) {

        this(new DBCollectorConfig(queryRowHandleDAO, sqlID, bindParams)
                .addRelation1n(relation1n));
    }

    /**
     * DBCollectorコンストラクタ<br>
     * @param queryRowHandleDAO QueryRowHandleDAOインスタンス
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param queueSize キューのサイズ（1以上を設定すること。0以下は無視）
     */
    public DBCollector(QueryRowHandleDAO queryRowHandleDAO, String sqlID,
            Object bindParams, int queueSize) {
        this(new DBCollectorConfig(queryRowHandleDAO, sqlID, bindParams)
                .addQueueSize(queueSize));
    }

    /**
     * DBCollectorコンストラクタ<br>
     * @param queryRowHandleDAO QueryRowHandleDAOインスタンス
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param exceptionHandler 例外ハンドラ
     */
    public DBCollector(QueryRowHandleDAO queryRowHandleDAO, String sqlID,
            Object bindParams, CollectorExceptionHandler exceptionHandler) {
        this(new DBCollectorConfig(queryRowHandleDAO, sqlID, bindParams)
                .addExceptionHandler(exceptionHandler));
    }

    /**
     * DBCollectorコンストラクタ<br>
     * @param queryRowHandleDAO QueryRowHandleDAOインスタンス
     * @param sqlID 実行するSQLのID
     * @param bindParams SQLにバインドする値を格納したオブジェクト
     * @param queueSize キューのサイズ（1以上を設定すること。0以下は無視）
     * @param relation1n 1:Nマッピング使用時はtrue
     * @param exceptionHandler 例外ハンドラ
     * @param dbCollectorPrePostProcess DBCollector前後処理
     */
    public DBCollector(QueryRowHandleDAO queryRowHandleDAO, String sqlID,
            Object bindParams, int queueSize, boolean relation1n,
            CollectorExceptionHandler exceptionHandler,
            DBCollectorPrePostProcess dbCollectorPrePostProcess) {
        this(new DBCollectorConfig(queryRowHandleDAO, sqlID, bindParams)
                .addQueueSize(queueSize).addRelation1n(relation1n)
                .addExceptionHandler(exceptionHandler)
                .addDbCollectorPrePostProcess(dbCollectorPrePostProcess));
    }

    /**
     * DBCollectorコンストラクタ<br>
     * @param config DBCollectorConfig DBCollector設定項目
     */
    public DBCollector(DBCollectorConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("The parameter is null.");
        }

        this.queryRowHandleDAO = config.getQueryRowHandleDAO();
        this.sqlID = config.getSqlID();
        this.bindParams = config.getBindParams();
        if (config.getQueueSize() > 0) {
            setQueueSize(config.getQueueSize());
        }
        if (config.isRelation1n()) {
            this.queueingDataRowHandlerClass = Queueing1NRelationDataRowHandlerImpl.class;
        }
        this.exceptionHandler = config.getExceptionHandler();
        this.dbCollectorPrePostProcess = config.getDbCollectorPrePostProcess();

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
            DBCollectorPrePostProcessStatus expStatus = null;
            do {
                try {
                    // SQL実行前処理
                    preprocess();

                    // QueryRowHandleDAO 実行
                    this.queryRowHandleDAO.executeWithRowHandler(this.sqlID,
                            bindParams, this.rowHandler);
                    this.rowHandler.delayCollect();

                } catch (Throwable th) {
                    // SQL実行後処理（例外）
                    expStatus = postprocessException(th);

                    // ステータス判定
                    if (expStatus == null
                            || DBCollectorPrePostProcessStatus.THROW
                                    .equals(expStatus)) {
                        // 例外をスロー
                        throw th;
                    } else if (expStatus == null
                            || DBCollectorPrePostProcessStatus.END
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
                    && DBCollectorPrePostProcessStatus.RETRY.equals(expStatus));
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

            return Integer.valueOf(-1);
        } finally {
            setFinish();
        }

        return Integer.valueOf(0);
    }

    /**
     * SQL実行前処理
     */
    protected void preprocess() {
        if (this.dbCollectorPrePostProcess != null) {
            this.dbCollectorPrePostProcess.preprocess(this);
        }
    }

    /**
     * SQL実行後処理（例外）
     * @param th Throwable
     * @return DBCollectorPrePostProcessStatus
     */
    protected DBCollectorPrePostProcessStatus postprocessException(Throwable th) {
        DBCollectorPrePostProcessStatus expStatus = null;
        if (this.dbCollectorPrePostProcess != null) {
            expStatus = this.dbCollectorPrePostProcess.postprocessException(
                    this, th);
        }
        return expStatus;
    }

    /**
     * SQL実行後処理
     */
    protected void postprocessComplete() {
        if (this.dbCollectorPrePostProcess != null) {
            this.dbCollectorPrePostProcess.postprocessComplete(this);
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
        if (obj instanceof DBCollector) {
            DBCollector<P> qac = (DBCollector<P>) obj;
            qac.rowHandler = getDataRowHandler();
            qac.rowHandler.setDbCollector(this);
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
