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

package jp.terasoluna.fw.collector.file;

import jp.terasoluna.fw.collector.AbstractCollector;
import jp.terasoluna.fw.collector.LogId;
import jp.terasoluna.fw.collector.exception.CollectorExceptionHandler;
import jp.terasoluna.fw.collector.vo.DataValueObject;
import jp.terasoluna.fw.file.dao.FileLineIterator;
import jp.terasoluna.fw.file.dao.FileQueryDAO;
import jp.terasoluna.fw.logger.TLogger;

/**
 * FileCollector.<br>
 * 独立した別スレッドを起動し、FileQueryDAOを非同期で実行する。
 * @param &ltP&gt
 */
public class FileCollector<P> extends AbstractCollector<P> {

    /**
     * Log.
     */
    private static TLogger LOGGER = TLogger.getLogger(FileCollector.class);

    /** FileQueryDAO */
    protected FileQueryDAO fileQueryDAO = null;

    /** ファイル名（絶対パスまたは相対パスのどちらか） */
    protected String fileName = null;

    /** 1行分の文字列を格納するファイル行オブジェクトクラス */
    protected Class<P> clazz = null;

    /**
     * FileCollectorコンストラクタ.<br>
     */
    protected FileCollector() {
    }

    /**
     * FileCollectorコンストラクタ.<br>
     * @param fileQueryDAO FileQueryDAOインスタンス
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     */
    public FileCollector(FileQueryDAO fileQueryDAO, String fileName,
            Class<P> clazz) {
        this(new FileCollectorConfig<P>(fileQueryDAO, fileName, clazz));
    }

    /**
     * FileCollectorコンストラクタ.<br>
     * @param fileQueryDAO FileQueryDAOインスタンス
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     * @param exceptionHandler 例外ハンドラ
     */
    public FileCollector(FileQueryDAO fileQueryDAO, String fileName,
            Class<P> clazz, CollectorExceptionHandler exceptionHandler) {
        this(new FileCollectorConfig<P>(fileQueryDAO, fileName, clazz)
                .addExceptionHandler(exceptionHandler));
    }

    /**
     * FileCollectorコンストラクタ.<br>
     * @param fileQueryDAO FileQueryDAOインスタンス
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     * @param queueSize キューのサイズ（1以上を設定すること）
     * @param exceptionHandler 例外ハンドラ
     */
    public FileCollector(FileQueryDAO fileQueryDAO, String fileName,
            Class<P> clazz, int queueSize,
            CollectorExceptionHandler exceptionHandler) {
        this(new FileCollectorConfig<P>(fileQueryDAO, fileName, clazz)
                .addQueueSize(queueSize).addExceptionHandler(exceptionHandler));
    }

    /**
     * FileCollectorコンストラクタ.<br>
     * @param config FileCollectorConfig FileCollector設定項目
     */
    public FileCollector(FileCollectorConfig<P> config) {
        if (config == null) {
            throw new IllegalArgumentException("The parameter is null.");
        }

        this.fileQueryDAO = config.getFileQueryDAO();
        this.fileName = config.getFileName();
        this.clazz = config.getClazz();
        if (config.getQueueSize() > 0) {
            setQueueSize(config.getQueueSize());
        }
        this.exceptionHandler = config.getExceptionHandler();

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
        FileLineIterator<P> fli = null;
        long dataCount = 0;
        try {
            // FileQueryDAO実行
            fli = this.fileQueryDAO.execute(this.fileName, this.clazz);

            if (fli != null) {
                while (fli.hasNext()) {
                    dataCount++;
                    try {
                        Object value = fli.next();

                        // 取得したデータを1件キューにつめる
                        addQueue(new DataValueObject(value, dataCount));
                    } catch (InterruptedException e) {
                        if (LOGGER.isTraceEnabled()) {
                            LOGGER.trace(LogId.TAL041002, Thread
                                    .currentThread().getName());
                        }
                        break;
                    } catch (Throwable e) {
                        // 発生した例外をキューにつめる
                        try {
                            addQueue(new DataValueObject(e, dataCount));
                        } catch (InterruptedException ie) {
                            LOGGER.warn(LogId.WAL041003, e);
                            LOGGER.warn(LogId.WAL041003, ie);
                            break;
                        }
                        // 次の行を読むため、ループは継続する
                    }
                }
            }
        } catch (Throwable e) {
            // シャットダウン中は発生した例外をキューに詰めない
            if (!isFinish()) {
                // 発生した例外をキューにつめる
                try {
                    addQueue(new DataValueObject(e, dataCount));
                } catch (InterruptedException ie) {
                    LOGGER.warn(LogId.WAL041003, e);
                    LOGGER.warn(LogId.WAL041003, ie);
                }
            }

            return Integer.valueOf(-1);
        } finally {
            try {
                // ファイルクローズ
                if (fli != null) {
                    fli.closeFile();
                }
            } catch (Throwable e) {
                // 何もしない
            } finally {
                // 終了フラグを立てる
                setFinish();
                if (verboseLog.get() && LOGGER.isTraceEnabled()) {
                    LOGGER.trace(LogId.TAL041018, dataCount);
                }
            }
        }

        return Integer.valueOf(0);
    }
}
