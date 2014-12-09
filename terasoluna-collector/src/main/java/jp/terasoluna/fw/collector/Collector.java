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

package jp.terasoluna.fw.collector;

import java.io.Closeable;
import java.util.Iterator;

/**
 * コレクタインタフェース<br>
 * @param &lt;P&gt;
 */
public interface Collector<P> extends Iterator<P>, Iterable<P>, Closeable {
    /**
     * 1件前の要素を返します。<br>
     * <p>
     * 1件目の場合はnullが返ります。<br>
     * ポインタは移動しません。
     * </p>
     * @return &lt;P&gt;
     */
    P getPrevious();

    /**
     * 現在の要素を返します。<br>
     * <p>
     * nullの場合は現在の要素が存在しないことを示します。<br>
     * ポインタは移動しません。
     * </p>
     * @return &lt;P&gt;
     */
    P getCurrent();

    /**
     * ポインタを次の要素に移さずに次の要素を返します。<br>
     * <p>
     * nullの場合は次の要素が存在しないことを示します。<br>
     * ポインタは移動しません。
     * </p>
     * @return &lt;P&gt;
     */
    P getNext();
}
