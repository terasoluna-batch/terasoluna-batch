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

import java.util.List;

/**
 * ジョブリスト取得用DAOの入力パラメータ。<br>
 */
public class BatchJobListParam {
    /**
     * フィールド [jobAppCd] 項目の型 [java.lang.String]<br>
     * ジョブ業務コード
     */
    private String JobAppCd;

    /**
     * フィールド [curAppStatusList] 項目の型 [java.util.List]<br>
     * ステータスリスト
     */
    private List<String> CurAppStatusList;

    /**
     * フィールド [jobAppCd]のセッターメソッド 項目の型 [java.lang.String]<br>
     * ジョブ業務コード
     * @param argJobAppCd フィールド[jobAppCd]に格納したい値
     */
    public void setJobAppCd(final String argJobAppCd) {
        this.JobAppCd = argJobAppCd;
    }

    /**
     * フィールド[jobAppCd]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * ジョブ業務コード
     * @return フィールド[jobAppCd]に格納されている値
     */
    public String getJobAppCd() {
        return JobAppCd;
    }

    /**
     * フィールド [curAppStatusList]のセッターメソッド 項目の型 [java.util.List]<br>
     * ステータスリスト
     * @param argCurAppStatusList フィールド[curAppStatusList]に格納したい値
     */
    public void setCurAppStatusList(final List<String> argCurAppStatusList) {
        this.CurAppStatusList = argCurAppStatusList;
    }

    /**
     * フィールド[curAppStatusList]のゲッターメソッド 項目の型 [java.util.List]<br>
     * ステータスリスト
     * @return フィールド[curAppStatusList]に格納されている値
     */
    public List<String> getCurAppStatusList() {
        return CurAppStatusList;
    }

    /**
     * このバリューオブジェクトの文字列表現を取得します。 オブジェクトのシャロー範囲でしかtoStringされない点に注意して利用してください。
     * @return バリューオブジェクトの文字列表現。
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BatchJobListParam[");
        sb.append("jobAppCd=" + JobAppCd);
        sb.append(",curAppStatusList=" + CurAppStatusList);
        sb.append("]");
        return sb.toString();
    }
}
