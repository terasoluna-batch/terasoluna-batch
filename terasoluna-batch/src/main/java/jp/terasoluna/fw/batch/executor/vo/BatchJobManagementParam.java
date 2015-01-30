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
 * ジョブ1件取得用DAOの入力パラメータ。<br>
 */
public class BatchJobManagementParam {
    /**
     * フィールド [jobSequenceId] 項目の型 [java.lang.String]<br>
     * ジョブシーケンスコード
     */
    private String jobSequenceId;

    /**
     * フィールド [forUpdate] 項目の型 [boolean]<br>
     * FOR UPDATEフラグ
     */
    private boolean ForUpdate;

    /**
     * フィールド [jobSequenceId]のセッターメソッド 項目の型 [java.lang.String]<br>
     * ジョブシーケンスコード
     * @param jobSequenceId フィールド[jobSequenceId]に格納したい値
     */
    public void setJobSequenceId(final String jobSequenceId) {
        this.jobSequenceId = jobSequenceId;
    }

    /**
     * フィールド[jobSequenceId]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * ジョブシーケンスコード
     * @return フィールド[jobSequenceId]に格納されている値
     */
    public String getJobSequenceId() {
        return jobSequenceId;
    }

    /**
     * フィールド [forUpdate]のセッターメソッド 項目の型 [boolean]<br>
     * FOR UPDATEフラグ
     * @param argForUpdate フィールド[forUpdate]に格納したい値
     */
    public void setForUpdate(final boolean argForUpdate) {
        this.ForUpdate = argForUpdate;
    }

    /**
     * フィールド[forUpdate]のゲッターメソッド 項目の型 [boolean]<br>
     * FOR UPDATEフラグ
     * @return フィールド[forUpdate]に格納されている値
     */
    public boolean getForUpdate() {
        return ForUpdate;
    }

    /**
     * このバリューオブジェクトの文字列表現を取得します。 オブジェクトのシャロー範囲でしかtoStringされない点に注意して利用してください。
     * @return バリューオブジェクトの文字列表現。
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("BatchJobManagementParam[");
        buf.append("jobSequenceId=" + jobSequenceId);
        buf.append(",forUpdate=" + ForUpdate);
        buf.append("]");
        return buf.toString();
    }
}
