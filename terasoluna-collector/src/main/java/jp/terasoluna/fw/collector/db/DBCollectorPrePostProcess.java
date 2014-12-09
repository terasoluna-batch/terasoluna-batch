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

package jp.terasoluna.fw.collector.db;

/**
 * DBCollectorのSQL実行時の前後処理インタフェース
 */
public interface DBCollectorPrePostProcess {
    /**
     * SQL実行開始前に実行されるメソッド.
     */
    <P> void preprocess(DBCollector<P> collector);

    /**
     * SQL実行終了時に実行されるメソッド.<br>
     * SQL実行時に例外が発生した場合は、postprocessExceptionメソッドの次に、このメソッドが実行される。
     */
    <P> void postprocessComplete(DBCollector<P> collector);

    /**
     * SQL実行終了時（例外）に実行されるメソッド.
     */
    <P> DBCollectorPrePostProcessStatus postprocessException(
            DBCollector<P> collector, Throwable throwable);
}
