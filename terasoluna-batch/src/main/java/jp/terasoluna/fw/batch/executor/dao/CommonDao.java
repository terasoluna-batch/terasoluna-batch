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

package jp.terasoluna.fw.batch.executor.dao;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 共通DAOインタフェース。
 */
public interface CommonDao {

    /**
     * シーケンス名からシステムコードを取得する。
     *
     * @param seqName シーケンス名
     * @return システムコード
     */
    String readSysCode(String seqName);

    /**
     * 現在時刻を表すオブジェクトを取得する。
     * @return 現在時刻を表すオブジェクト
     */
    Timestamp readCurrentTime();

    /**
     * 現在日付を表すオブジェクトを取得する。
     * @return 現在日付を表すオブジェクト
     */
    Date readCurrentDate();

}
