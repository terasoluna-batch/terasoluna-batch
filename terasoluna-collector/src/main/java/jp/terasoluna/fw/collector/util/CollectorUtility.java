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

package jp.terasoluna.fw.collector.util;

import java.io.IOException;

import jp.terasoluna.fw.collector.Collector;
import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.dao.FileLineIterator;
import jp.terasoluna.fw.file.dao.FileLineWriter;

/**
 * CollectorUtilityクラス.
 */
public class CollectorUtility {

    /**
     * コンストラクタ.
     */
    protected CollectorUtility() {
    }

    /**
     * Collectorをクローズする。<br>
     * <p>
     * 引数に渡されたcollectorがnullでなければクローズする。<br>
     * また、クローズする際に例外が発生した場合は無視する。<br>
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
     * FileLineIteratorをクローズする。<br>
     * <p>
     * 引数に渡されたiteratorがnullでなければクローズする。<br>
     * また、クローズする際にFileException例外が発生した場合は無視する。<br>
     * </p>
     * @param <T>
     * @param iterator FileLineIterator&lt;T&gt;
     */
    public static <T> void closeQuietly(FileLineIterator<T> iterator) {
        try {
            if (iterator != null) {
                iterator.closeFile();
            }
        } catch (FileException e) {
            // なにもしない
        }
    }

    /**
     * FileLineWriterをクローズする。<br>
     * <p>
     * 引数に渡されたwriterがnullでなければクローズする。<br>
     * また、クローズする際にFileException例外が発生した場合は無視する。<br>
     * </p>
     * @param <T>
     * @param writer FileLineWriter&lt;T&gt;
     */
    public static <T> void closeQuietly(FileLineWriter<T> writer) {
        try {
            if (writer != null) {
                writer.closeFile();
            }
        } catch (FileException e) {
            // なにもしない
        }
    }
}
