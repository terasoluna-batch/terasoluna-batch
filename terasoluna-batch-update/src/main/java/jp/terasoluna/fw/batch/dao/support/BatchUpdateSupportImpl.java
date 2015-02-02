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

package jp.terasoluna.fw.batch.dao.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import jp.terasoluna.fw.dao.SqlHolder;
import jp.terasoluna.fw.dao.UpdateDAO;
import jp.terasoluna.fw.logger.TLogger;

/**
 * バッチ更新サポートクラス<br>
 * <p>
 * 本クラスを利用すことでUpdateDAOのバッチ更新処理の実行をSqlID毎に整列された状態で行うことができる。<br>
 * SqlIDでソートすることでJDBCのPreparedStatement#executeBatchの実行回数を減らせる事により性能に寄与する。
 * </p>
 * <p>
 * sortメソッドを実行せずにSQLを実行した場合は、初回に追加したSQL-IDの順が維持される。<br>
 * <p>
 * <li>たとえば以下のような順でSQL-IDを追加した場合</li><br>
 * A C B A B C B C A
 * </p>
 * <p>
 * <li>sortメソッドを実行せずにSQLを実行した場合の実行順</li><br>
 * A A A C C C B B B
 * </p>
 * <p>
 * <li>sortメソッドを実行後にSQLを実行した場合の実行順</li><br>
 * A A A B B B C C C
 * </p>
 * </p>
 * <p>
 * <b> ※マルチスレッドセーフではないため複数スレッドで利用する場合は、各スレッドごとに別インスタンスを生成すること。 </b>
 * </p>
 * @see UpdateDAO
 */
public class BatchUpdateSupportImpl implements BatchUpdateSupport {

    /**
     * ロガー.
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(BatchUpdateExecutor.class);

    /** UpdateDAO */
    protected UpdateDAO updateDAO = null;

    /** SqlIDをソートする時に使用するComparator */
    protected volatile Comparator<String> comparator = null;

    /** バッチ実行SQLを保持する. */
    protected final Map<String, Queue<SqlHolder>> batchSqlsMap = new LinkedHashMap<String, Queue<SqlHolder>>();

    /**
     * ソートフラグ
     */
    protected volatile boolean sortMode = false;

    /** バッチ実行SQL登録件数 */
    protected volatile AtomicLong count = new AtomicLong(0);

    /** SQL-IDの実行順序 */
    private String[] sqlIdOrder = null;

    /**
     * バッチ更新サポートクラスコンストラクタ.
     */
    public BatchUpdateSupportImpl() {
        // 何もしない
    }

    /**
     * バッチ更新サポートクラスコンストラクタ.
     * @param updateDAO UpdateDAO
     */
    public BatchUpdateSupportImpl(UpdateDAO updateDAO) {
        this(updateDAO, (Comparator<String>) null);
    }

    /**
     * バッチ更新サポートクラスコンストラクタ.
     * @param updateDAO UpdateDAO
     * @param comparator Comparator&lt;String&gt;
     */
    public BatchUpdateSupportImpl(UpdateDAO updateDAO,
            Comparator<String> comparator) {
        this.updateDAO = updateDAO;
        this.comparator = comparator;

        if (this.updateDAO == null) {
            LOGGER.warn(LogId.WAL036002);
        }
    }

    /**
     * バッチ更新サポートクラスコンストラクタ.
     * @param updateDAO UpdateDAO
     * @param sqlIdOrder SQL-IDの実行順序を指定する
     */
    public BatchUpdateSupportImpl(UpdateDAO updateDAO, String... sqlIdOrder) {
        this.updateDAO = updateDAO;
        this.sqlIdOrder = sqlIdOrder;

        if (this.updateDAO == null) {
            LOGGER.warn(LogId.WAL036002);
        }
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.dao.support.BatchUpdateSupportIf#addBatch(java.lang.String, java.lang.Object)
     */
    public void addBatch(final String sqlID, final Object bindParams) {
        if (sqlID == null || sqlID.length() == 0) {
            LOGGER.warn(LogId.WAL036003);
            return;
        }

        Queue<SqlHolder> sqlQueue = this.batchSqlsMap.get(sqlID);

        if (sqlQueue == null) {
            // 再取得
            sqlQueue = this.batchSqlsMap.get(sqlID);

            if (sqlQueue == null) {
                sqlQueue = new ConcurrentLinkedQueue<SqlHolder>();
                this.batchSqlsMap.put(sqlID, sqlQueue);
            }
        }

        if (sqlQueue != null) {
            sqlQueue.add(new SqlHolder(sqlID, bindParams));
            this.count.incrementAndGet();
        }
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.dao.support.BatchUpdateSupport#executeBatch()
     */
    public int executeBatch() {
        return executeBatch(this.updateDAO, this.comparator, this.sqlIdOrder);
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.dao.support.BatchUpdateSupport#executeBatch(jp.terasoluna.fw.dao.UpdateDAO)
     */
    public int executeBatch(UpdateDAO updateDAO) {
        return executeBatch(updateDAO, this.comparator, this.sqlIdOrder);
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.dao.support.BatchUpdateSupport#executeBatch(jp.terasoluna.fw.dao.UpdateDAO,
     * java.util.Comparator)
     */
    public int executeBatch(UpdateDAO updateDAO, Comparator<String> comparator) {
        return executeBatch(updateDAO, comparator, null);
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.dao.support.BatchUpdateSupport#executeBatch(jp.terasoluna.fw.dao.UpdateDAO,
     * java.lang.String[])
     */
    public int executeBatch(UpdateDAO updateDAO, String... sqlIdOrder) {
        return executeBatch(updateDAO, null, sqlIdOrder);
    }

    /**
     * バッチ実行を行う。
     * @param updateDAO UpdateDAO
     * @param comparator Comparator&lt;String&gt;
     * @param sqlIdOrder SQL-IDの実行順序を指定する
     * @return SQLの実行結果
     */
    protected int executeBatch(UpdateDAO updateDAO,
            Comparator<String> comparator, String[] sqlIdOrder) {
        int result = -1000;

        if (updateDAO == null) {
            LOGGER.warn(LogId.WAL036002);
            return ERROR_UPDATE_DAO_IS_NULL;
        }

        List<SqlHolder> sqlHolderList = new ArrayList<SqlHolder>();

        // SQL-IDリストを取得
        List<String> keyList = new ArrayList<String>(this.batchSqlsMap.keySet());

        if (sqlIdOrder != null) {
            List<String> sqlIdOrderList = Arrays.asList(sqlIdOrder);

            // 件数チェック
            if (keyList.size() > sqlIdOrderList.size()) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(LogId.WAL036004, keyList.size(), sqlIdOrderList
                            .size());
                }
                return ERROR_UNKNOWN_SQL_ID;
            }
            // SQL-ID存在チェック
            for (String key : keyList) {
                if (!sqlIdOrderList.contains(key)) {
                    LOGGER.warn(LogId.WAL036005, key);
                    return ERROR_UNKNOWN_SQL_ID;
                }
            }
            // キーリストを上書き
            keyList = sqlIdOrderList;
        } else if (this.sortMode || comparator != null) {
            // SQL-IDでソートする
            if (comparator != null) {
                Collections.sort(keyList, comparator);
            } else {
                Collections.sort(keyList);
            }
        }

        for (String key : keyList) {
            if (this.batchSqlsMap.containsKey(key)) {
                sqlHolderList.addAll(this.batchSqlsMap.get(key));
            }
        }

        // バッチ更新実行
        result = updateDAO.executeBatch(sqlHolderList);

        this.batchSqlsMap.clear();
        this.count.set(0);
        this.sortMode = false;

        return result;
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.dao.support.BatchUpdateSupport#sort()
     */
    public void sort() {
        this.sortMode = true;
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.dao.support.BatchUpdateSupport#sort(java.util.Comparator)
     */
    public void sort(Comparator<String> comparator) {
        this.sortMode = true;
        this.comparator = comparator;
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.dao.support.BatchUpdateSupportIf#clear()
     */
    public void clear() {
        this.batchSqlsMap.clear();
        this.count.set(0);
        this.sortMode = false;
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.dao.support.BatchUpdateSupport#size()
     */
    public long size() {
        return this.count.get();
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.dao.support.BatchUpdateSupport#getSqlHolderList()
     */
    public List<SqlHolder> getSqlHolderList() {
        return getSqlHolderList(this.comparator);
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.dao.support.BatchUpdateSupport#getSqlHolderList(java.util.Comparator)
     */
    public List<SqlHolder> getSqlHolderList(Comparator<String> comparator) {
        return getSqlHolderList(comparator, null);
    }

    /*
     * (non-Javadoc)
     * @see jp.terasoluna.fw.batch.dao.support.BatchUpdateSupport#getSqlHolderList(java.lang.String[])
     */
    public List<SqlHolder> getSqlHolderList(String... sqlIdOrder) {
        return getSqlHolderList(null, sqlIdOrder);
    }

    /**
     * SQL-IDで整列されたSqlHolderリストを取得する。
     * @param comparator Comparator&lt;String&gt;
     * @param sqlIdOrder SQL-IDの実行順序を指定する
     * @return SqlHolderリスト
     */
    protected List<SqlHolder> getSqlHolderList(Comparator<String> comparator,
            String[] sqlIdOrder) {
        List<SqlHolder> sqlHolderList = new ArrayList<SqlHolder>();

        // SQL-IDリストを取得
        List<String> keyList = new ArrayList<String>(this.batchSqlsMap.keySet());

        if (sqlIdOrder != null) {
            List<String> sqlIdOrderList = Arrays.asList(sqlIdOrder);

            // 件数チェック
            if (keyList.size() > sqlIdOrderList.size()) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(LogId.WAL036004, keyList.size(), sqlIdOrderList
                            .size());
                }
                return null;
            }
            // SQL-ID存在チェック
            for (String key : keyList) {
                if (!sqlIdOrderList.contains(key)) {
                    LOGGER.warn(LogId.WAL036005, key);
                    return null;
                }
            }
            // キーリストを上書き
            keyList = sqlIdOrderList;
        } else if (this.sortMode || comparator != null) {
            // SQL-IDでソートする
            if (comparator != null) {
                Collections.sort(keyList, comparator);
            } else {
                Collections.sort(keyList);
            }
        }

        for (String key : keyList) {
            if (this.batchSqlsMap.containsKey(key)) {
                sqlHolderList.addAll(this.batchSqlsMap.get(key));
            }
        }

        this.sortMode = false;

        return sqlHolderList;
    }
}
