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

package jp.terasoluna.fw.batch.exception.handler;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.logger.TLogger;

/**
 * 例外ハンドラのデフォルト実装.
 *
 * 例外クラスと返却するステータス値のマップ<br>
 * <p>
 * Bean定義に例外の型と対応するステータス値とのマッピングを定義することで、例外ごとに返却するステータス値を変えることができる。<br>
 * マッピング設定を省略した場合は、すべての例外に対してステータス値255を返却する。<br>
 * </p>
 * <p>
 * <fieldset style="border:1pt solid black;padding:10px;width:100%;"><br>
 * <legend>Bean定義記述例</legend>
 *
 * <pre>
 * &lt;bean id=&quot;defaultExceptionHandler&quot; class=&quot;jp.terasoluna.fw.batch.exception.handler.DefaultExceptionHandler&quot;&gt;
 *   &lt;property name=&quot;exceptionToStatusMap&quot;&gt;
 *     &lt;map&gt;
 *       &lt;entry key=&quot;jp.terasoluna.fw.batch.exception.BatchException&quot; value=&quot;123&quot;/&gt;
 *       &lt;entry key=&quot;java.lang.Exception&quot; value=&quot;100&quot;/&gt;
 *     &lt;/map&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 *
 * </fieldset>
 * </p>
 */
public class DefaultExceptionHandler implements ExceptionHandler {
    /**
     * ロガー。
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(DefaultExceptionHandler.class);

    /**
     * デフォルト例外ハンドラのリターンコード.
     */
    protected static final int DEFAULT_EXCEPTION_HANDLER_STATUS = 255;

    /**
     * ハンドリング対象となる例外クラスと返却されるステータス値のマップ
     */
    protected Map<Class<? extends Throwable>, Integer> exceptionToStatusMap = null;

    /**
     * 例外クラスと返却するステータス値のマップを設定する。
     * @param exceptionToStatusMap Map&lt;Class&lt;? extends Throwable&gt;, Integer&gt;
     */
    public void setExceptionToStatusMap(
            Map<Class<? extends Throwable>, Integer> exceptionToStatusMap) {
        this.exceptionToStatusMap = exceptionToStatusMap;
    }

    /**
     * {@inheritDoc}
     */
    public int handleThrowableException(Throwable e) {
        // WARNログを出力する
        LOGGER.warn(LogId.WAL025007,e);

        // 例外クラスと返却するステータス値のマップが設定されていた場合はそれに従う
        if (this.exceptionToStatusMap != null && e != null) {
            Class<? extends Throwable> exClass = e.getClass();

            Set<Entry<Class<? extends Throwable>, Integer>> es = this.exceptionToStatusMap
                    .entrySet();

            for (Entry<Class<? extends Throwable>, Integer> ent : es) {
                if (ent != null && ent.getKey() != null
                        && ent.getValue() != null) {
                    Class<? extends Throwable> entClass = ent.getKey();

                    if (entClass.isAssignableFrom(exClass)) {
                        if(LOGGER.isDebugEnabled()){
                            LOGGER.debug(LogId.DAL025017, exClass.getName(),entClass.getName(),ent.getValue());
                        }

                        return ent.getValue().intValue();
                    }
                }
            }
        }

        // デフォルトのステータス値を返す。
        return DEFAULT_EXCEPTION_HANDLER_STATUS;
    }
}
