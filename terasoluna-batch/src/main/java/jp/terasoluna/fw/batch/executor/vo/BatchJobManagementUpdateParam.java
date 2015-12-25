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

import java.sql.Timestamp;

/**
 * ジョブレコード更新用DAOの入力パラメータ。<br>
 */
public class BatchJobManagementUpdateParam {
    /**
     * フィールド [jobSequenceId] 項目の型 [java.lang.String]<br>
     * ジョブシーケンスコード
     */
    private String jobSequenceId;

    /**
     * フィールド [errAppStatus] 項目の型 [java.lang.String]<br>
     * BLogicステータス
     */
    private String blogicAppStatus;

    /**
     * フィールド [curAppStatus] 項目の型 [java.lang.String]<br>
     * ステータスリスト
     */
    private String CurAppStatus;

    /**
     * フィールド [updDateTime] 項目の型 [java.sql.Timestamp]<br>
     * 更新日時（ミリ秒）
     */
    private Timestamp UpdDateTime;

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
     * フィールド [errAppStatus]のセッターメソッド 項目の型 [java.lang.String]<br>
     * BLogicステータス
     * @param argBLogicAppStatus フィールド[BLogicAppStatus]に格納したい値
     */
    public void setBLogicAppStatus(final String argBLogicAppStatus) {
        blogicAppStatus = argBLogicAppStatus;
    }

    /**
     * フィールド[BLogicAppStatus]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * BLogicステータス
     * @return フィールド[BLogicAppStatus]に格納されている値
     */
    public String getBLogicAppStatus() {
        return blogicAppStatus;
    }

    /**
     * フィールド [curAppStatus]のセッターメソッド 項目の型 [java.lang.String]<br>
     * ステータスリスト
     * @param argCurAppStatus フィールド[curAppStatus]に格納したい値
     */
    public void setCurAppStatus(final String argCurAppStatus) {
        CurAppStatus = argCurAppStatus;
    }

    /**
     * フィールド[curAppStatus]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * ステータスリスト
     * @return フィールド[curAppStatus]に格納されている値
     */
    public String getCurAppStatus() {
        return CurAppStatus;
    }

    /**
     * フィールド [updDateTime]のセッターメソッド 項目の型 [java.sql.Timestamp]<br>
     * 更新日時（ミリ秒）
     * @param argUpdDateTime フィールド[updDateTime]に格納したい値
     */
    public void setUpdDateTime(final Timestamp argUpdDateTime) {
        UpdDateTime = argUpdDateTime;
    }

    /**
     * フィールド[updDateTime]のゲッターメソッド 項目の型 [java.sql.Timestamp]<br>
     * 更新日時（ミリ秒）
     * @return フィールド[updDateTime]に格納されている値
     */
    public Timestamp getUpdDateTime() {
        return UpdDateTime;
    }

    /**
     * このバリューオブジェクトの文字列表現を取得します。 オブジェクトのシャロー範囲でしかtoStringされない点に注意して利用してください。
     * @return バリューオブジェクトの文字列表現。
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BatchJobManagementUpdateParam[");
        sb.append("jobSequenceId=" + jobSequenceId);
        sb.append(",BLogicAppStatus=" + blogicAppStatus);
        sb.append(",curAppStatus=" + CurAppStatus);
        sb.append(",updDateTime=" + UpdDateTime);
        sb.append("]");
        return sb.toString();
    }
}
