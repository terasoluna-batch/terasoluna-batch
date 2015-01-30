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

import java.util.Comparator;

/**
 * CompareStrategy実装クラス.<br>
 * 外部Comparatorのcompareメソッドで比較するストラテジ。
 * compareメソッドが0を返せば等しい、0以外を返せば等しくない、という結果になる。<br>
 * このクラスは、既存のComparatorをCompareStrategyとして利用するために用意している。<br>
 * コントロールブレイク判定のために、新規に比較ロジックを実装する場合は、
 * このクラスに与えるComparatorを新規に作成するのではなく、
 * CompareStrategy実装クラスを作成することを推奨する。<br>
 * (CompareStrategyはComparatorと異なり、2つのオブジェクトの大小関係を決める仕様が不要。)<br>
 * なお、外部Comparatorがステートレスであれば、このクラスもステートレスである。
 * そのため、外部Comparatorがステートレスであれば、
 * 比較のたびに外部Comparatorやこのクラスのインスタンスを作成しなおす必要は無い。
 * @see Comparator
 * @see CompareStrategy
 */
public class ComparatorCompareStrategy implements CompareStrategy<Object> {

    /**
     * 外部Comparator.
     */
    @SuppressWarnings("rawtypes")
    private Comparator comparator = null;

    /**
     * コンストラクタ.
     * @param comparator 2つのオブジェクトを比較するComparator
     */
    public ComparatorCompareStrategy(Comparator<?> comparator) {
        this.comparator = comparator;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean equalsObjects(Object value1, Object value2) {
        return (comparator.compare(value1, value2) == 0);
    }
}
