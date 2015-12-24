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

package jp.terasoluna.fw.batch.executor.vo;

/**
 * ビジネスロジック実行結果パラメータ。<br>
 */
public class BLogicResult {

    /**
     * ビジネスロジックの戻り値
     */
    protected int blogicStatus = 255;

    /**
     * ビジネスロジックで発生した例外
     */
    protected Throwable blogicThrowable = null;

    /**
     * ビジネスロジックの戻り値
     * @return the blogicStatus
     */
    public int getBlogicStatus() {
        return blogicStatus;
    }

    /**
     * ビジネスロジックの戻り値
     * @param blogicStatus the blogicStatus to set
     */
    public void setBlogicStatus(int blogicStatus) {
        this.blogicStatus = blogicStatus;
    }

    /**
     * ビジネスロジックで発生した例外を取得する。
     * @return ビジネスロジックで発生した例外
     */
    public Throwable getBlogicThrowable() {
        return blogicThrowable;
    }

    /**
     * ビジネスロジックで発生した例外を設定する。
     * @param blogicThrowable ビジネスロジックで発生した例外
     */
    public void setBlogicThrowable(Throwable blogicThrowable) {
        this.blogicThrowable = blogicThrowable;
    }

}
