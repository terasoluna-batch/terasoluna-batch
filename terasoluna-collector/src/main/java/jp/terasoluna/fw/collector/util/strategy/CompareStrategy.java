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

package jp.terasoluna.fw.collector.util.strategy;

/**
 * 2つのオブジェクトが等しいか等しくないかを判断する方法を実装/提供するためのインタフェース。
 */
public interface CompareStrategy<T> {

    /**
     * value1とvalue2が等しいか否かを判断する。
     * @param value1 比較元（null以外のオブジェクト）。
     * @param value2 比較先（null以外のオブジェクト）。
     * @return value1とvalue2が等しい場合true/等しくない場合false
     */
    public boolean equalsObjects(T value1, T value2);

}
