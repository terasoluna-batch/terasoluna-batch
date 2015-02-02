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
 * CompareStrategy実装クラス.<br>
 * 比較対象オブジェクトのequalsメソッドで比較するストラテジ。<br>
 * コントロールブレイク判定において、
 * <ul>
 * <li>Comparable実装クラスだが、compareToメソッドではなくequalsメソッドで比較したい</li>
 * <li>Comparable実装クラスではないが、EqualsBuilder#reflectionEqualsではなくequalsメソッドで比較したい</li>
 * </ul>
 * という場合に、このクラスを利用する。<br>
 * なお、このクラスはステートレスであるため、比較のたびにインスタンスを作成しなおす必要は無い。
 */
public class EqualsCompareStrategy implements CompareStrategy<Object> {

    /**
     * {@inheritDoc}
     */
    public boolean equalsObjects(Object value1, Object value2) {
        return (value1.equals(value2));
    }
}
