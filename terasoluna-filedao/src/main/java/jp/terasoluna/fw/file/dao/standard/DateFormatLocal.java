/*
 * Copyright (c) 2007 NTT DATA Corporation
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

package jp.terasoluna.fw.file.dao.standard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * DateFormatのThreadLocalクラス。<br>
 * SimpleDateFormatがスレッドセーフではないため、ThreadLocalを使用して<br>
 * スレッドセーフにする。
 */
public class DateFormatLocal extends ThreadLocal<DateFormat> {

    /**
     * フォーマットパターン
     */
    private String pattern = null;

    /**
     * コンストラクタ
     */
    public DateFormatLocal(String pattern) {
        this.pattern = pattern;
    }

    /**
     * 初期化。
     * @return DateFormatインスタンス
     */
    @Override
    protected DateFormat initialValue() {
        // スレッド毎の初期化
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        return sdf;
    }
}
