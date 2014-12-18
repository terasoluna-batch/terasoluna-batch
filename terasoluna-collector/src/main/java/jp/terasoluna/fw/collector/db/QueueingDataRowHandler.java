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

import org.apache.ibatis.session.ResultHandler;

/**
 * QueueingDataRowHandlerインタフェース<br>
 * DataRowHandlerの拡張インタフェース。
 */
public interface QueueingDataRowHandler extends ResultHandler {

    /**
     * 前回handleRowメソッドに渡された<code>Row</code>データをキューに格納する。
     */
    void delayCollect();

    /**
     * DBCollectorを設定する。<br>
     * @param dbCollector DBCollector&lt;?&gt;
     */
    void setDbCollector(DBCollector<?> dbCollector);
}
